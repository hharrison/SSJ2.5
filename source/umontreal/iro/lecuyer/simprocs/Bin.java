

/*
 * Class:        Bin
 * Description:  corresponds to a pile of identical tokens, and a list of
                 processes waiting for the tokens when the list is empty
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

import java.util.ListIterator;
import umontreal.iro.lecuyer.simevents.Simulator;
import umontreal.iro.lecuyer.simevents.Accumulate;
import umontreal.iro.lecuyer.simevents.LinkedListStat;
import umontreal.iro.lecuyer.stat.Tally;
import umontreal.iro.lecuyer.util.PrintfFormat;


/**
 * A <TT>Bin</TT> corresponds to a pile of identical tokens, and a list of
 * processes waiting for the tokens when the list is empty.
 * It is a producer/consumer process synchronization device.
 * Tokens can be added to the pile (i.e., <EM>produced</EM>) by the
 * method {@link #put put}.
 * A process can request tokens from the pile (i.e., <EM>consume</EM>)
 * by calling {@link #take take}.
 * 
 * <P>
 * The behavior of a <TT>Bin</TT> is somewhat similar to that of a
 * {@link Resource}.  Each <TT>Bin</TT> has a single queue of waiting
 * processes, with FIFO or LIFO service policy, and
 * which can be accessed via the method {@link #waitList waitList}.
 * This list actually contains objects of the class {@link UserRecord}.
 * Each {@link UserRecord} points to a process
 * and contains some additional information.
 * 
 */
public class Bin  {

   private static final int FIFO  = 1;
   private static final int LIFO  = 2;

    // Variables

        private ProcessSimulator sim;
        private String name;
        private int available = 0;
        private int policy = FIFO;
        private LinkedListStat<UserRecord> waitingList;
        private ListIterator<UserRecord> iter;
        private Accumulate statAvail;
        private boolean stats;
        private double     initStatTime;



   /**
    * Constructs a new bin, initially empty, with
    *    service policy FIFO and linked with the default simulator.
    * 
    */
   public Bin()  {
      this ("");
   }


   /**
    * Constructs a new bin, initially empty, with
    *    service policy FIFO and linked with simulator sim.
    *  
    * @param sim Simulator associated to the current variable.
    * 
    * 
    */
   public Bin(ProcessSimulator sim)  {
      this (sim, "");
   }


   /**
    * Constructs a new bin, initially empty, with service policy FIFO,
    *   identifier <TT>name</TT> and linked with the default simulator.
    *  
    * @param name name associated to the bin
    * 
    * 
    */
   public Bin (String name)  {
      try {
         ProcessSimulator.initDefault();
         this.sim = (ProcessSimulator)Simulator.getDefaultSimulator();
         waitingList = new LinkedListStat<UserRecord>(sim);
         this.name = name;
         iter = waitingList.listIterator();
         stats = false;
      }
      catch (ClassCastException e) {
         throw new IllegalArgumentException("Wrong default Simulator type");
      }
   }


   /**
    * Constructs a new bin, initially empty, with
    *    service policy FIFO, identifier <TT>name</TT> and linked with simulator sim.
    *  
    * @param sim Simulator associated to the current variable.
    * 
    *    @param name name associated to the bin
    * 
    */
   public Bin (ProcessSimulator sim, String name)  {
      this.sim = sim;
      waitingList = new LinkedListStat<UserRecord>(sim);
      this.name = name;
      iter = waitingList.listIterator();
      stats = false;
   }


   /**
    * Reinitializes this bin by clearing up its pile of tokens and
    *    its waiting list.
    *    The processes in this list (if any) remain in the same states.
    * 
    */
   public void init()  {
      int oldAvail = available;
      waitingList.clear();
      available = 0;
      if (stats) initStat();
   }


   /**
    * Starts or stops collecting statistics on the list returned
    *   by {@link #waitList waitList} for this bin.
    *   If the statistical collection is turned ON, It also constructs (if not yet done)
    *   and initializes an additional statistical
    *   collector of class
    *    {@link umontreal.iro.lecuyer.simevents.Accumulate Accumulate}
    *   for this bin.  This collector will be updated
    *   automatically.  It can be accessed via {@link #statOnAvail statOnAvail}, and
    *    monitors the evolution of the
    *   available tokens of the bin
    *   as a function of time.
    *  
    * @param b <TT>true</TT> to turn statistical collection ON, <TT>false</TT> to turn it OFF
    * 
    * 
    */
   public void setStatCollecting (boolean b)  {
      if (b) {
         if (stats)
            throw new IllegalStateException ("Already collecting statistics for this resource");
         stats = true;
         waitingList.setStatCollecting (true);
         if (statAvail != null)
            initStat();
         else {
            statAvail = new Accumulate (sim, "StatOnAvail");
            statAvail.update (available);
         }
      }
      else {
         if (stats)
            throw new IllegalStateException ("Not collecting statistics for this resource");
         stats = false;
         waitingList.setStatCollecting (false);
      }
   }


   /**
    * Reinitializes all the statistical collectors for this
    *    bin.  These collectors must exist, i.e.,
    *    {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> must have been invoked earlier for
    *    this bin.
    * 
    */
   public void initStat() {
        if (!stats)  throw new IllegalStateException (
                               "Not collecting statistics for this resource");
        statAvail.init();
        statAvail.update (available);
        waitingList.initStat();
        initStatTime = sim.time();
   }


   /**
    * Sets the service policy for ordering processes waiting
    *    for tokens on the bin to FIFO (<SPAN  CLASS="textit">first in, first out</SPAN>):
    *    the processes are placed in the
    *    list (and served) according to their order of arrival.
    * 
    */
   public void setPolicyFIFO() {
      policy = FIFO;
   }


   /**
    * Sets the service policy for ordering processes waiting
    *    for tokens on the bin to LIFO (<SPAN  CLASS="textit">last in, first out</SPAN>):
    *     the processes are placed in the
    *    list (and served) according to their inverse order of arrival:
    *    the last arrived are served first.
    * 
    */
   public void setPolicyLIFO() {
      policy = LIFO;
   }


   /**
    * Returns the number of available tokens for this bin.
    *  
    * @return the number of available tokens
    * 
    */
   public int getAvailable()  {
      return available;
   }


   /**
    * The executing process invoking this method requests
    *    <TT>n</TT> tokens from this bin.  If enough tokens are available,
    *    the number of tokens in the bin is reduced by <TT>n</TT> and the
    *    process continues its execution.
    *    Otherwise, the executing process is placed into the
    *    {@link #waitList waitList} list (the queue) for this bin and is suspended
    *    until it can obtain the requested number of tokens.
    *  
    * @param n number of requested tokens
    * 
    * 
    */
   public void take (int n)  {
        SimProcess p = sim.currentProcess();
        if (n <= available) {
            // The process gets the resource right away.
            available -= n;
            if (stats) {
               statAvail.update (available);
               waitingList.statSojourn().add (0.0);
            }
        }
        else {
            // Not enough units of the resource are available.
            // The process joins the queue waitingList;
            UserRecord record = new UserRecord (n, p, sim.time());
            switch (policy) {
                case FIFO : waitingList.addLast (record); break;
                case LIFO : waitingList.addFirst (record); break;
                default   : throw new IllegalStateException (
                                       "the policy must be LIFO or FIFO");
            }
            p.suspend();
        }
   }


   /**
    * Adds <TT>n</TT> tokens to this bin.
    *   If there are processes waiting for tokens from this bin and
    *   whose requests can now be satisfied, they obtain the tokens and
    *   resume their execution.
    *  
    * @param n number of tokens to put into the bin
    * 
    * 
    */
   public void put (int n)  {
        available+=n;
        if (stats) statAvail.update (available);
        if (waitingList.size()>0)  wakeProcess();
   }

   private void wakeProcess() {

        UserRecord record;
        ListIterator<UserRecord> iter = waitingList.listIterator();
        while (iter.hasNext() && available > 0) {
            record=iter.next();
            if (!record.process.isAlive())
                throw new IllegalStateException(
                           "process not alive");
                // The thread for this process is still alive.

            if (record.numUnits <= available) {
                // This request can now be satisfied
                record.process.resume();
                available -= record.numUnits;
                if (stats) statAvail.update (available);
                iter.remove();
            }
        }
    } 


   /**
    * Returns the list of {@link UserRecord}
    *    for the processes waiting for tokens from this bin.
    *  
    * @return the list of waiting process user records
    * 
    */
   public LinkedListStat waitList()  {
      return waitingList;
   }


   /**
    * Returns the statistical collector for the available tokens
    *   on the bin as a function of time.
    *   This collector exists only if {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT>
    *   has been invoked previously.
    *  
    * @return the probe for available tokens
    * 
    */
   public Accumulate statOnAvail()  {
      return statAvail;
   }


   /**
    * Returns a string containing a complete statistical report on
    *   this  bin. The method 
    * <BR>{@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> must have
    *   been invoked previously, otherwise no statistics will have been collected.
    *   The report contains statistics on the available tokens, queue size and
    *   waiting time for this bin.
    *  
    * @return a statistical report for this bin, represented as a string
    * 
    */
   public String report()  {

    if (statAvail == null) throw new IllegalStateException ("Asking a report for a bin "
                          +"for which setStatCollecting (true) has not been called");

    Accumulate sizeWait = waitingList.statSize();
    Tally sojWait = waitingList.statSojourn();

    PrintfFormat str = new PrintfFormat();

    str.append ("REPORT ON BIN : ").append (name).append (PrintfFormat.NEWLINE);
    str.append ("   From time : ").append (7, 2, 2, initStatTime);
    str.append ("   to time : ");
    str.append (10,2, 2, sim.time());
    str.append (PrintfFormat.NEWLINE + "                    min        max     average  ");
    str.append ("standard dev.  nb. obs.");

    str.append (PrintfFormat.NEWLINE + "   Available tokens ");
    str.append (8, (int)(statAvail.min()+0.5));
    str.append (11, (int)(statAvail.max()+0.5));
    str.append (12, 3, 2, statAvail.average());

    str.append (PrintfFormat.NEWLINE + "   Queue Size  ");
    str.append (8, (int)(sizeWait.min()+0.5));
    str.append (11, (int)(sizeWait.max()+0.5));
    str.append (12, 3, 2, sizeWait.average());

    str.append (PrintfFormat.NEWLINE + "   Wait    ");
    str.append (12, 3, 2, sojWait.min()).append (' ');
    str.append (10, 3, 2, sojWait.max()).append (' ');
    str.append (11, 3, 2, sojWait.average()).append (' ');
    str.append (10, 3, 2, sojWait.standardDeviation());
    str.append (10, sojWait.numberObs());

    return str.toString();
   }


   /**
    * Returns the current simulator of this continuous-time variable.
    *  
    * @return the current simulator of the variable
    * 
    */
   public Simulator simulator()  {
      return sim;
   } 


   /**
    * Set the current simulator of this continuous-time variable.
    *  This method must not be called while a simulator is running.
    *  
    * @param sim the simulator of the current variable
    * 
    * 
    */
   public void setSimulator (ProcessSimulator sim)  {
      this.sim = sim;
   } 

}
