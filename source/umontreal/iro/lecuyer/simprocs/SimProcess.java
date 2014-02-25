

/*
 * Class:        SimProcess
 * Description:  Abstract class providing process scheduling tools
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

import umontreal.iro.lecuyer.simevents.Event;
import umontreal.iro.lecuyer.simevents.eventlist.EventList;
import umontreal.iro.lecuyer.simevents.Simulator;
import umontreal.iro.lecuyer.simprocs.ProcessSimulator;

/**
 * This abstract class provides process scheduling tools.
 * Each type of process should be defined as a subclass of the
 * class <TT>SimProcess</TT>, and must provide an implementation of the method
 * {@link #actions actions} which describes the life of a process of this class.
 * Whenever a process instance starts, its {@link #actions actions} method
 * begins executing.
 * 
 * <P>
 * Just like an event, a process must first be constructed, then scheduled.
 * Scheduling a process is equivalent to placing an event in the event list
 * that will start this process when
 * the simulation clock reaches the specified time.
 * The {@link #toString toString} method can be overridden to return
 * information about the process.  This information will
 * be returned when formating the event list as a string, if the
 * process is delayed.
 * 
 * <P>
 * A process can be in one of the following states: <TT>INITIAL</TT>,
 * <TT>EXECUTING</TT>, <TT>DELAYED</TT>, <TT>SUSPENDED</TT>, and <TT>DEAD</TT>
 * .
 * At most <EM>one</EM> process can be in the <TT>EXECUTING</TT> state at
 * any given time, and when there is one, this executing process
 * (called the <EM>current process</EM>) is said to <EM>have control</EM>
 * and is executing one of the instructions of its {@link #actions actions} method.
 * A process that has been constructed but not yet scheduled is in
 * the <TT>INITIAL</TT> state.
 * A process is in the <TT>DELAYED</TT> state if there is a planned event
 * in the event list to activate it (give it control).
 * When a process is scheduled, it is placed in the <TT>DELAYED</TT> state.
 * It is in the <TT>SUSPENDED</TT> state if it is waiting to be reactivated
 * (i.e., obtain control) without having an event to do so in the event list.
 * A process can suspends itself by calling {@link #suspend suspend} directly or
 * indirectly (e.g., by requesting a busy {@link Resource}).
 * Usually, a <TT>SUSPENDED</TT> process must be reactivated by another process
 * or event via the {@link #resume resume} method.
 * A process in the <TT>DEAD</TT> state no longer exists.
 * 
 * <P>
 * To construct new processes, the user needs to extend {@link SimProcess}.
 * 
 * <P>
 * Note: the user needs to ensure that the <TT>actions</TT>
 * method of any process can be terminated, i.e., no infinite loops.
 * For example, using threads process-oriented simulator,
 * if such a method never terminates, threads will not be recycled,
 * causing memory problems.
 * 
 */
public class SimProcess  {



   /**
    * The process has been created but not yet scheduled. 
    * 
    */
   public static final int INITIAL   = 0;


   /**
    * The process is the one currently executing its
    *    {@link #actions actions} method. 
    * 
    */
   public static final int EXECUTING = 1;


   /**
    * The process is not executing but has an event in the event list to
    *    reactivate it later on.  
    * 
    */
   public static final int DELAYED   = 2;


   /**
    * The process is not executing and will have to be reactivated by another
    *    process or event later on.  
    * 
    */
   public static final int SUSPENDED = 3;


   /**
    * The process has terminated its execution.  
    * 
    */
   public static final int DEAD      = 4;

   // The state of a process is not kept explicitly, but
   // can be recovered from its eventTime variable.
    public static final double STARTING = -20.0;
    public static final double WAITING  = -10.0;
    // Variables

   protected ProcessSimulator sim;
   // Simulator linked with this simProcess.

   protected Event scheduledEvent = null;
   // the next scheduled event



   /**
    * Constructs a new process without scheduling it
    *   and associates this new process with the default simulator; one
    *   can get additional knowledge with
    *  {@link umontreal.iro.lecuyer.simevents.Simulator Simulator} static methods.
    *    It will have to be scheduled later on.
    *    The process is in the <TT>INITIAL</TT> state.
    * 
    */
   public SimProcess()  {
      this((ProcessSimulator)Simulator.getDefaultSimulator());
   }


   /**
    * Constructs a new process associated with <TT>sim</TT>
    *   without scheduling it. It will have to be scheduled later on.
    *    The process is in the <TT>INITIAL</TT> state.
    *  
    *  @param sim Link the current variable to ProcessSimulator <TT>sim</TT>
    * 
    */
   public SimProcess (ProcessSimulator sim)  {
      this.sim = sim;
      scheduledEvent = sim.createControlEvent(this);
   }


   /**
    * Schedules the process to start in <TT>delay</TT> time units.
    *    This puts the process in the <TT>DELAYED</TT> state.
    *   
    * @param delay delay, in simulation time, before the process starts
    * 
    * 
    */
   public void schedule (double delay)  {
      if (scheduledEvent == null)
         throw new IllegalStateException ("Cannot schedule a dead process");
      if (scheduledEvent.time() != STARTING)
         throw new IllegalStateException ("Only a process in INITIAL state can call schedule");

      scheduledEvent.schedule (delay);
   } 


   /**
    * Schedules this process to start at the current time, by placing
    *    it at the beginning of the event list.
    *    This puts the process in the <TT>DELAYED</TT> state.
    * 
    */
   public void scheduleNext()  {
      if (scheduledEvent == null)
         throw new IllegalStateException ("Cannot schedule a dead process");
      if (scheduledEvent.time() != STARTING)
         throw new IllegalStateException
            ("Only a process in INITIAL state can call scheduleNext");
      scheduledEvent.scheduleNext();
   } 


   /**
    * Returns the <TT>Event</TT> associated with the current variable.
    * 
    */
   public Event scheduledEvent()  {
      return scheduledEvent;
   } 


   /**
    * Sets the event associated to the current variable.
    *   
    * @param scheduledEvent new scheduledEvent for the current variable
    * 
    * 
    */
   public void setScheduledEvent (Event scheduledEvent)  {
      this.scheduledEvent = scheduledEvent;
   } 


   /**
    * Returns the priority of the current variable.
    * 
    * @return priority of the current variable.
    * 
    */
   public double priority()  {
      return scheduledEvent.priority();
   } 


   /**
    * Sets the priority assigned to the current variable in the simulation.
    *    This method should never be called after the event has been scheduled, otherwise
    *    the events will not execute in ascending priority order anymore.
    *   
    * @param priority priority assigned to the current variable in the simulation
    * 
    * 
    */
   public void setPriority (double priority)  {
      scheduledEvent.setPriority(priority);
   } 


   /**
    * Returns <TT>true</TT> if the process is alive, otherwise <TT>false</TT>.
    *   
    * @return <TT>true</TT> if the process is not in the <TT>DEAD</TT> state
    * 
    */
   public final boolean isAlive()  {
      return scheduledEvent != null;
   } 


   /**
    * Returns the state of the process.
    *   
    * @return one of the process states {@link #INITIAL INITIAL},
    *       {@link #EXECUTING EXECUTING}, {@link #DELAYED DELAYED}, {@link #SUSPENDED SUSPENDED}, or
    *       {@link #DEAD DEAD}.
    * 
    */
   public int getState()  {
      if (scheduledEvent == null)                return DEAD;
      else if (sim.currentProcess() == this)             return EXECUTING;
      else if (scheduledEvent.time() >= 0.0)  return DELAYED;
      else if (scheduledEvent.time() == STARTING) return INITIAL;
      else return SUSPENDED;
   } 


   /**
    * If the process is in the <TT>DELAYED</TT> state, returns
    *    the remaining time until the planned occurrence of its
    *    activating event.
    *    Otherwise, an illegal state exception will be thrown printing an error message.
    *   
    * @return remaining delay before the process starts
    *    @exception IllegalStateException if the process is not in <TT>DELAYED</TT> state
    * 
    * 
    */
   public double getDelay()  {
      if (scheduledEvent == null)
         throw new IllegalStateException ("Trying to getDelay() on a dead process");
      if (scheduledEvent.time() < 0.0)
         throw new IllegalStateException
                   ("Calling getDelay for a process not in DELAYED state");
      return scheduledEvent.time() - sim.time();
   } 


   /**
    * If the process is in the <TT>DELAYED</TT> state, removes it from
    *    the event list and reschedules it in <TT>delay</TT> units of time.
    *    Example: If the process <TT>p</TT> has called {@link #delay delay}<TT>(5.0)</TT>
    *    at time 10.0, and another process invokes {@link #reschedule reschedule}<TT>(p, 6.2)</TT>
    *    at time 13.5, then the process <TT>p</TT> will resume at time
    *    
    * <SPAN CLASS="MATH">13.5 + 6.2 = 19.7</SPAN>.
    *   
    * @param delay new delay, in simulation time units, before the process starts or is resumed
    * 
    * 
    */
   public void reschedule (double delay)  {
      if (sim.currentProcess() == this)
         throw new IllegalStateException
                   ("reschedule() for a process in EXECUTING state");
      if (scheduledEvent == null)
         throw new IllegalStateException ("reschedule() for a dead process ");
      if (delay < 0.0) throw new IllegalArgumentException
                                 ("Calling reschedule() with negative delay");
      scheduledEvent.reschedule (delay);
   } 


   /**
    * Places this process at the beginning of the event list
    *    to resume its execution.  If the process was <TT>DELAYED</TT>, cancels
    *    its earlier activating event.
    * 
    */
   public void resume()  {
      if (scheduledEvent == null)   // DEAD state
         throw new IllegalStateException ("calling resume() for a dead process");
      if (scheduledEvent.time() >= 0.0)  scheduledEvent.cancel();
      scheduledEvent.scheduleNext();
   } 


   /**
    * Cancels the activating event that was supposed to resume
    *    this process, and places the process in the <TT>SUSPENDED</TT> state.
    *    This method can be invoked only for a process in the <TT>DELAYED</TT>
    *    state.
    *   
    * @return <TT>true</TT> if the process was canceled successfully
    * 
    */
   public boolean cancel()  {
      if (scheduledEvent == null) // DEAD state
         throw new IllegalStateException ("calling cancel() for a dead process");
      if (scheduledEvent.time() < 0.0 )
         throw new IllegalStateException
                   ("cancel() for a process not in DELAYED state");
      boolean removed = scheduledEvent.cancel();
      scheduledEvent.setTime (WAITING);
      return removed;
   } 


   /**
    * Suspends the execution of the currently executing process and
    *     schedules it to resume its execution in <TT>delay</TT> units of simulation
    *     time.  It moves in the <TT>DELAYED</TT> state.
    *     Only the process in the <TT>EXECUTING</TT> state can call this method.
    * 
    */
   public void delay (double delay)  {
      sim.delay(this, delay);
   } 


   /**
    * This method can only be invoked for the <TT>EXECUTING</TT>
    *    or a <TT>DELAYED</TT> process.
    *    If the process is <TT>EXECUTING</TT>, suspends execution.
    *    If the process is <TT>DELAYED</TT>, cancels its activating event.
    *    This places the process in the <TT>SUSPENDED</TT> state.
    * 
    */
   public void suspend()  {
      sim.suspend(this);
   } 


   /**
    * Terminates the life of this process and sets its state to
    *    <TT>DEAD</TT>, after canceling its activating event if there is one.
    * 
    */
   public void kill()  {
      sim.kill(this);
   } 


   /**
    * This is the method that is called when this process is
    *    executing. Every subclass of <TT>SimProcess</TT> that is to be
    *     instantiated must provide an implementation of this method.
    * 
    */
   public void actions() { }; 


   /**
    * This method calls <TT>ProcessSimulator.initDefault()</TT>,
    *     which initializes the default simulator to use processes.
    * 
    */
   public static void init()  {
      ProcessSimulator.initDefault();
   } 
 
}
