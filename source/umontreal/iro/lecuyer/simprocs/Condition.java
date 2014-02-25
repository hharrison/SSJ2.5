

/*
 * Class:        Condition
 * Description:  boolean indicator with a list of processes waiting for 
                 the indicator to be true
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since

 * SSJ is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License (GPL) as published by the
 * Free Software Foundation, either version 3 of the License, or
 * any later version.

 * SSJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * A copy of the GNU General Public License is available at
   <a href="http://www.gnu.org/licenses">GPL licence site</a>.
 */

package umontreal.iro.lecuyer.simprocs;
import java.util.Observable;
import umontreal.iro.lecuyer.simevents.Simulator;
import umontreal.iro.lecuyer.simevents.LinkedListStat;

/**
 * A <TT>Condition</TT> is a boolean indicator, with a list of processes  
 * waiting for the indicator to be <TT>true</TT> (when it is <TT>false</TT>).
 * A process calling {@link #waitFor waitFor} on a condition that is currently
 * <TT>false</TT> is suspended until the condition becomes <TT>true</TT>.
 * The list of waiting processes can be accessed via {@link #waitList waitList}.
 * 
 */
public class Condition extends Observable  {

   private String name;
   private LinkedListStat<UserRecord> waitingList;
   private boolean state;
   private boolean broadcasting;
   private ProcessSimulator sim;


   /**
    * Constructs a new <TT>Condition</TT> with initial value <TT>val</TT>, linked with the default simulator.
    *  
    * @param val initial state of the condition
    * 
    * 
    */
   public Condition (boolean val)  {
      this (val, "");
   }


   /**
    * Constructs a new <TT>Condition</TT> with initial value <TT>val</TT>, linked with simulator <TT>sim</TT>.
    *  
    * @param sim current simulator of the variable
    * 
    *    @param val initial state of the condition
    * 
    * 
    */
   public Condition (ProcessSimulator sim, boolean val)  {
      this (sim, val, "");
   }


   /**
    * Constructs a new <TT>Condition</TT> with initial value <TT>val</TT>, 
    *  identifier <TT>name</TT> and linked with the default simulator.
    * 
    * @param val initial state of the condition
    * 
    *    @param name identifier or name for the condition
    * 
    * 
    */
   public Condition (boolean val, String name)  {
      super();
      try {
         ProcessSimulator.initDefault();
         this.sim = (ProcessSimulator)Simulator.getDefaultSimulator();
         waitingList = new LinkedListStat<UserRecord>(sim);
         this.name = name;
         broadcasting = false;
         state = val;
      }
      catch (ClassCastException e) {
         throw new IllegalArgumentException("Wrong default Simulator type");
      }
   }


   /**
    * Constructs a new <TT>Condition</TT> with initial value <TT>val</TT>, 
    *  identifier <TT>name</TT> and linked with simulator <TT>sim</TT>.
    *  
    * @param sim current simulator of the variable
    * 
    *    @param val initial state of the condition
    * 
    *    @param name identifier or name for the condition
    * 
    */
   public Condition (ProcessSimulator sim, boolean val, String name)  {
      super();
      this.sim = sim;
      waitingList = new LinkedListStat<UserRecord>(sim);
      this.name = name;
      broadcasting = false;
      state = val;
   }


   /**
    * Reinitializes this <TT>Condition</TT> by clearing up 
    *    its waiting list and resetting its state to <TT>val</TT>.
    *    The processes in this list (if any) remain in the same states.
    *  
    * @param val initial state of the condition
    * 
    * 
    */
   public void init (boolean val)  {
      waitingList.clear();
      state = val;
      if (broadcasting) {
         setChanged();
         notifyObservers (new Boolean (state));
      }
   }


   /**
    * Sets the condition to <TT>val</TT>.
    *    If <TT>val</TT> is <TT>true</TT>, all the processes waiting 
    *    for this condition now resume their execution, in the same order as
    *    they have called {@link #waitFor waitFor} for this condition.
    *    (Note that a process can never wait for more than one condition at
    *    a time, because it cannot call {@link #waitFor waitFor} while it is suspended.
    *  
    * @param val new state of the condition
    * 
    * 
    */
   public void set (boolean val)  { 
      state = val;
      if (state) {
         UserRecord record;
         while (!waitingList.isEmpty()) {
            record= waitingList.removeLast();
            record.process.resume();
         }    
      } 
      if (broadcasting) {
         setChanged();
         notifyObservers (new Boolean (val));
      }
   }


   /**
    * Returns the state (<TT>true</TT> or <TT>false</TT>) of the condition.
    *  
    * @return the current state of the condition
    * 
    */
   public boolean state()  { 
      return state;
   }


   /**
    * The executing process invoking this method
    *   must wait for this condition to be <TT>true</TT>.
    *   If it is already <TT>true</TT>, the 
    *   process continues its execution immediately
    *    Otherwise, the executing process is placed into the 
    *    {@link #waitList waitList} list for this condition and is suspended
    *    until the condition becomes <TT>true</TT>.
    * 
    */
   public void waitFor()  {
      SimProcess p = sim.currentProcess();
      if (state == true) {
         // The process can continus right away.
         if (waitingList.statSojourn() != null)
            waitingList.statSojourn().add (0.0);
         return;
      }
      else {
         // The process joins the waitingList;
         UserRecord record = new UserRecord (1, p, sim.time());
         waitingList.addLast (record);
         p.suspend();
      }
   }


   /**
    * Returns the list of {@link UserRecord}
    *    for the processes waiting for this condition.
    *  
    * @return the list of processes user records waiting for the condition
    * 
    */
   public LinkedListStat waitList()  { 
      return waitingList;
   }


   /**
    * Returns the name (or identifier) associated
    *   to this condition.
    * 
    * @return the name associated to this object
    * 
    */
   public String getName() {
      return name;
   }


   /**
    * Instructs the condition to start or stop observation broadcasting.
    *   When this is turned ON, a {@link Boolean} observation is notified to
    *   registered observers when the state of the condition changes.  This
    *   boolean gives the new state of the condition.
    * 
    * <P>
    * Warning: Since this can reduce program performance, this should be
    * turned on only when there are registered observers.
    * 
    * @param b <TT>true</TT> to turn broadcasting ON, <TT>false</TT> to turn it OFF
    * 
    */
   public void setBroadcasting (boolean b) {
      broadcasting = b;
   }
}
