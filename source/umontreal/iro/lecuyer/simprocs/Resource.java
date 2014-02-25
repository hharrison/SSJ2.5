

/*
 * Class:        Resource
 * Description:  resources with limited capacity which can be requested 
                 and released by Process objects.
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
import umontreal.iro.lecuyer.util.PrintfFormat;
// import umontreal.iro.lecuyer.simevents.Simulator;
import umontreal.iro.lecuyer.simevents.LinkedListStat;
import umontreal.iro.lecuyer.simevents.Accumulate;
import umontreal.iro.lecuyer.simprocs.ProcessSimulator;
import umontreal.iro.lecuyer.stat.Tally;


/**
 * Objects of this class are resources having limited capacity,
 * and which can be requested and released by {@link Process} objects.
 * These resources act indirectly as synchonization devices for
 * processes.
 * 
 * <P>
 * A resource is created with a finite capacity, specified when 
 * invoking the {@link #Resource(int) Resource} constructor, and can be changed
 * later on.  A resource also has an infinite-capacity queue
 * (waiting line) and a service policy that defaults to FIFO and
 * can be changed later on.
 * 
 * <P>
 * A process must ask for a certain number of units of the resource
 * ({@link #request request}), and obtain it before using it.
 * When it is done with the resource, the process must release 
 * it so that others can use it ({@link #release release}).
 * A process does not have to request [release] all the resource
 * units that it needs by a single call to the {@link #request request}
 * [{@link #release release}] method.  It can make several successive requests
 * or releases, and can also hold different resources simultaneously.
 * 
 * <P>
 * Each resource maintains two lists: one contains the processes 
 * waiting for the resource (the waiting queue) and the other contains
 * the processes currently holding units of this resource.
 * The methods {@link #waitList waitList} and {@link #servList servList} permit one to
 * access these two lists.
 * These lists actually contain objects of the class {@link UserRecord}
 * instead of {@link SimProcess} objects.
 * 
 */
public class Resource  {

   private static final int FIFO  = 1;
   private static final int LIFO  = 2;

        private ProcessSimulator sim;

        private String name;
        private int capacity = 0;
        private int available = 0;
        private int policy = FIFO;

        private LinkedListStat<UserRecord> serviceList;
        private LinkedListStat<UserRecord> waitingList;

        private boolean    stats = false;
        private double     initStatTime;
        private Accumulate statUtil;
        private Accumulate statCapacity;
        private Tally      statSojourn;


   /**
    * Constructs a new resource linked with the default simulator,
    *   with initial capacity <TT>capacity</TT>, and service policy FIFO.
    * 
    * @param capacity initial capacity of the resource
    * 
    * 
    */
   public Resource (int capacity)  {
      this (capacity, "");
   }


   /**
    * Constructs a new resource linked with the simulator
    *  <TT>sim</TT>, with initial capacity <TT>capacity</TT>, and service policy FIFO.
    * 
    * @param sim current simulator of the variable
    * 
    *    @param capacity initial capacity of the resource
    * 
    * 
    */
   public Resource (ProcessSimulator sim, int capacity)  {
      this (sim, capacity, "");
   }


   /**
    * Constructs a new resource linked with the default simulator, with
    *  initial capacity <TT>capacity</TT>, service policy FIFO, and identifier 
    *  <TT>name</TT>.
    * 
    * @param capacity initial capacity of the resource
    * 
    *    @param name identifier or name of the resource
    * 
    * 
    */
   public Resource (int capacity, String name)  {
      try {
         ProcessSimulator.initDefault();
         this.sim = (ProcessSimulator)ProcessSimulator.getDefaultSimulator();
         this.capacity = available = capacity;
         this.name = name;
         serviceList = new LinkedListStat<UserRecord> (
              sim," Service List for " + name);
         waitingList = new LinkedListStat<UserRecord> (
              sim," Waiting List for " + name);
      }
      catch (ClassCastException e) {
         throw new IllegalArgumentException("Wrong default Simulator type");
      }
   }


   /**
    * Constructs a new resource linked with the simulator <TT>sim</TT>,
    *    with initial capacity <TT>capacity</TT>,
    *    service policy FIFO, and identifier (or name) <TT>name</TT>.
    * 
    * @param sim current simulator of the variable
    * 
    *    @param capacity initial capacity of the resource
    * 
    *    @param name identifier or name of the resource
    * 
    */
   public Resource (ProcessSimulator sim, int capacity, String name)  {
      this.capacity = available = capacity;
      this.name = name;
      this.sim = sim;
      serviceList = new LinkedListStat<UserRecord> (
          sim," Service List for " + name);
      waitingList = new LinkedListStat<UserRecord> (
          sim," Waiting List for " + name);
   }


   /**
    * Starts or stops collecting statistics on the lists returned
    *   by {@link #waitList waitList} and {@link #servList servList} for this resource.
    *   If the statistical collection is turned ON, the method
    *   also constructs (if not yet done) 
    *   and initializes three additional statistical
    *   collectors for this resource.  These collectors will be updated
    *   automatically.  They can be accessed via {@link #statOnCapacity statOnCapacity},
    *   {@link #statOnUtil statOnUtil}, and {@link #statOnSojourn statOnSojourn}, respectively.
    *   The first two, of class
    *    {@link umontreal.iro.lecuyer.simevents.Accumulate Accumulate},
    *    monitor the evolution of the
    *   capacity and of the utilization (number of units busy) of the resource 
    *   as a function of time.
    *   The third one, of class {@link umontreal.iro.lecuyer.stat.Tally Tally},
    *   collects statistics on the 
    *   processes sojourn times (wait + service);  it samples a new value
    *   each time a process releases all the units of this resource that it
    *   holds (i.e., when its {@link UserRecord} disappears).
    * 
    * @param b <TT>true</TT> to turn statistical collecting ON, <TT>false</TT> to turn it OFF
    * 
    * 
    */
   public void setStatCollecting (boolean b)  {
      if (b) {
         if (stats)
            throw new IllegalStateException ("Already collecting statistics for this resource");
         stats = true;
         waitingList.setStatCollecting (true);
         serviceList.setStatCollecting (true);
         
         if (statUtil != null)
            initStat();
         else {
            statUtil = new Accumulate (sim, "StatOnUtil");
            statUtil.update (capacity - available);
            statCapacity = new Accumulate (sim, "StatOnCapacity");
            statCapacity.update (capacity);
            statSojourn = new Tally ("StatOnSojourn");
         }
      }
      else {
         if (stats)
           throw new IllegalStateException ("Not collecting statistics for this resource");
         stats = false;
         waitingList.setStatCollecting (false);
         serviceList.setStatCollecting (false);
      }
   }


   /**
    * Reinitializes all the statistical collectors for this 
    *    resource.  These collectors must exist, i.e., 
    *    {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> must have been invoked earlier for 
    *    this resource.
    * 
    */
   public void initStat()  {
        if (!stats)  throw new IllegalStateException (
                               "Not collecting statistics for this resource");
        statUtil.init();
        statUtil.update (capacity - available);
        statCapacity.init();
        statCapacity.update (capacity);
        statSojourn.init();
        serviceList.initStat();
        waitingList.initStat();
        initStatTime = sim.time();
   }


   /**
    * Reinitializes this resource by clearing up its waiting list
    *    and service list.  The processes that were in these lists (if any)
    *    remain in the same states.   If statistical collection is ``on'',
    *    {@link #initStat initStat} is invoked as well.
    * 
    */
   public void init()  {
      serviceList.clear();
      waitingList.clear();
      available = capacity;
      if (stats) initStat();
   }


   /**
    * Returns the current capacity of the resource.
    * 
    * @return the capacity of the resource
    * 
    */
   public int getCapacity()  {
      return capacity;
   }


   /**
    * Set the service policy to FIFO (<SPAN  CLASS="textit">first in, first out</SPAN>): 
    *    the processes are placed in the
    *    list (and served) according to their order of arrival.
    * 
    */
   public void setPolicyFIFO() {
      policy = FIFO;
   }


   /**
    * Set the service policy to LIFO (<SPAN  CLASS="textit">last in, first out</SPAN>):
    *    the processes are placed in the
    *    list (and served) according to their inverse order of arrival,
    *    the last arrived are served first.
    * 
    */
   public void setPolicyLIFO() {
      policy = LIFO;
   }


   /**
    * Modifies by <TT>diff</TT> units (increases if <TT>diff &gt; 0</TT>,
    *    decreases if <TT>diff &lt; 0</TT>) the capacity (i.e., the number of units)
    *    of the resource.
    *    If <TT>diff &gt; 0</TT> and there are processes in the waiting list whose
    *    request can now be satisfied, they obtain the resource.
    *    If <TT>diff &lt; 0</TT>, there must be at least <TT>diff</TT> units of this
    *    resource available, otherwise an illegal argument exception will be thrown,
    *    printing an  error message (this is not a strong limitation, because one 
    *    can check first and release some units, if needed, before invoking 
    *    <TT>changeCapacity</TT>).
    *    In particular, the capacity of a resource can never become negative.
    * 
    * @param diff number of capacity units to add to the actual capacity,
    *      can be negative to reduce the capacity
    * 
    *    @exception IllegalArgumentException if <TT>diff</TT> is negative and
    *      the capacity cannot be reduced as required
    * 
    *    @exception IllegalStateException if <TT>diff</TT> is negative and
    *     the capacity could not be reduced due to a lack of available units
    * 
    * 
    */
   public void changeCapacity (int diff)  {
             if (diff > 0) {
                available += diff;
                capacity += diff;
                if (waitingList.size() > 0) startNewCust();
             }
             else {
                if (-diff > available) 
                   throw new IllegalArgumentException("Trying to diminish the capacity "
                         + "of a resource more than its current availability");
                available -= -diff;
                capacity -= -diff;
                }
                if (stats) statCapacity.update (capacity);
    }


   /**
    * Sets the capacity to <TT>newcap</TT>.
    *    Equivalent to {@link #changeCapacity changeCapacity}&nbsp;<TT>(newcap - old)</TT> if <TT>old</TT> is the
    *    current capacity.
    * 
    * @param newcap new capacity of the resource
    * 
    *    @exception IllegalArgumentException if the passed capacity is negative
    * 
    *    @exception IllegalStateException if <TT>diff</TT> is negative and
    *     the capacity could not be reduced due to a lack of available units
    * 
    * 
    */
   public void setCapacity (int newcap)  {
      if (newcap < 0)
         throw new IllegalArgumentException ("capacity cannot be negative");
      changeCapacity (newcap - capacity);
   }


   /**
    * Returns the number of available units, i.e., the capacity
    *    minus the number of units busy.
    * 
    * @return the number of available units
    * 
    */
   public int getAvailable()  { 
      return available;
   }


   /**
    * The executing process invoking this method requests for
    *    <TT>n</TT> units of this resource.  If there are enough units available
    *    to fill up the request immediately, the process obtains the desired
    *    number of units and holds them until it invokes {@link #release release}
    *    for this same resource.  The process is also inserted into the 
    *    {@link #servList servList} list for this resource.
    *    On the other hand, if there are less than <TT>n</TT> units of this
    *    resource available, the executing process is placed into the 
    *    {@link #waitList waitList} list (the queue) for this resource and is suspended
    *    until it can obtain the requested number of units of the resource.
    * 
    * @param n number of required units
    * 
    */
   public void request (int n)  {
        SimProcess p = sim.currentProcess();
        UserRecord record = new UserRecord (n, p, sim.time());
        if (n <= available) {
            // The process gets the resource right away.
            available -= n;
            serviceList.addLast (record);
            if (stats) {
               waitingList.statSojourn().add (0.0);
               statUtil.update (capacity - available);
            }
        }
        else {
            // Not enough units of the resource are available.
            // The process joins the queue waitingList;
            switch (policy) {
                case FIFO : waitingList.addLast (record); break;
                case LIFO : waitingList.addFirst (record); break;
                default   : throw new IllegalStateException(
                                                "policy must be FIFO or LIFO");
            }
            p.suspend();
        }
   }


   // Called by \texttt{release}. 
   private void startNewCust() {
       UserRecord record;
       ListIterator<UserRecord> iterWait = waitingList.listIterator();
       while (iterWait.hasNext() && available > 0) {
           record = iterWait.next();
           if (record.process.getState() == SimProcess.DEAD) iterWait.remove(); 
              // the process was killed, so we remove it from the waiting list.
              // or maybe we stop the program by throwing IllegalStateException
              //"Resource.startNewCust: process not alive");

               // The thread for this process is still alive.
           else if (record.numUnits <= available) {
               // This request can now be satisfied.
               serviceList.addLast (record);
               record.process.resume();
               available -= record.numUnits;
               iterWait.remove();
           }
       }
    }

   /**
    * The executing process that invokes this method releases 
    *   <TT>n</TT> units of the resource.  If this process is holding exactly
    *   <TT>n</TT> units, it is removed from the service list of this resource
    *   and its {@link UserRecord} object disappears.
    *   If this process is holding less than <TT>n</TT> units, 
    *   the program throws an illegal argument exception.
    *   If there are other processes waiting for this resource whose requests
    *   can now be satisfied, they obtain the resource.
    * 
    * @param n number of released units
    * 
    *    @exception IllegalArgumentException if the process did not request <TT>n</TT>
    *       units before releasing them
    * 
    * 
    */
   public void release (int n)  {
        SimProcess p = sim.currentProcess();
        int temp = 0;
        UserRecord record;
        ListIterator<UserRecord> iterServ = serviceList.listIterator();
        while (temp<n && iterServ.hasNext()) {
            record = iterServ.next();
            if (p == record.process) {
                temp = temp + record.numUnits;
                if (temp <= n) {
                    iterServ.remove();
                    if (stats) statSojourn.add
                                   (sim.time() - record.requestTime);
                }
                else {
                    record.numUnits = temp - n;
                    temp = n;
                }
            }
        }
        if (temp < n)  throw new IllegalArgumentException ("trying to release "
                +"more units of a Resource than the process currently holds");
        available += temp;
        if (waitingList.size() > 0)  startNewCust();
        if (stats) statUtil.update (capacity - available);
    }


   /**
    * Returns the list that contains the
    *   {@link UserRecord} objects
    *   for the processes in the waiting list for this resource.
    * 
    * @return the list of process user records waiting for the resource
    * 
    */
   public LinkedListStat waitList()  { 
      return waitingList;
   }


   /**
    * Returns the list that contains the 
    *   {@link UserRecord} objects
    *   for the processes in the service list for this resource.
    * 
    * @return the list of process user records using this resource
    * 
    */
   public LinkedListStat servList()  { 
      return serviceList;
   }


   /**
    * Returns the statistical collector that measures the evolution of
    *   the capacity of the resource as a function of time.
    *   This collector exists only if {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> has been invoked
    *   previously.  
    * 
    * @return the probe for resource capacity
    * 
    */
   public Accumulate statOnCapacity()  {
      return statCapacity;
   }


   /**
    * Returns the statistical collector for the utilization
    *   of the resource (number of units busy) as a function of time.
    *   This collector exists only if {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT>
    *   has been invoked previously.  
    *   The <EM>utilization rate</EM> of a resource can be obtained as the
    *   <EM>time average</EM> computed by this collector, divided by the
    *   capacity of the resource.
    *   The collector returned by {@link #servList() servList()}<TT>.</TT>{@link umontreal.iro.lecuyer.simevents.LinkedListStat#statSize() statSize()} 
    *   tracks the number of {@link UserRecord}
    *   in the service list;
    *   it differs from this collector because a process may hold more than one
    *   unit of the resource by given {@link UserRecord}.
    * 
    * @return the probe for resource utilization
    * 
    */
   public Accumulate statOnUtil()  {
      return statUtil;
   }


   /**
    * Returns the statistical collector for the sojourn times of
    *   the {@link UserRecord} for this resource.
    *   This collector exists only if {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> has been invoked
    *   previously.  
    *   It gets a new observation each time a process releases all the units
    *   of this resource that it had requested by a single {@link #request request}
    *   call.
    * 
    * @return the probe for the sojourn times
    * 
    */
   public Tally statOnSojourn()  {
      return statSojourn;
   }


   /**
    * Returns the name (or identifier) associated to this
    *   resource.  If it was not given upon resource construction, this returns
    *    <TT>null</TT>.
    * 
    * @return the name associated to this resource, or <TT>null</TT> if it was not given
    * 
    */
   public String getName() {
      return name;
   }


   /**
    * Returns a string containing a complete statistical report on this 
    *   resource.  The method {@link #setStatCollecting setStatCollecting}&nbsp;<TT>(true)</TT> must have been invoked 
    *   previously, otherwise no statistics have been collected.
    *   The report contains statistics on the waiting times, service
    *   times, and waiting times for this resource, on the capacity,
    *   number of units busy, and size of the queue as a function of time,
    *   and on the utilization rate.
    * 
    * @return a statistical report for this resource, represented as a string
    */
   public String report()  {

    if (statUtil == null) throw new IllegalStateException ("Asking a report for a resource "
                          +"for which setStatCollecting (true) has not been called");
  
    Accumulate sizeWait = waitingList.statSize();
    Tally sojWait = waitingList.statSojourn();
    Tally sojServ = serviceList.statSojourn();
   
    PrintfFormat str = new PrintfFormat(); 

    str.append ("REPORT ON RESOURCE : ").append (name).append (PrintfFormat.NEWLINE);
    str.append ("   From time : ").append (7, 2, 2, initStatTime);
    str.append ("   to time : ");
    str.append (10,2, 2, sim.time());
    str.append (PrintfFormat.NEWLINE + "                    min        max     average  ");
    str.append ("standard dev.  nb. obs.");

    str.append (PrintfFormat.NEWLINE + "   Capacity    ");
    str.append (8, (int)(statCapacity.min()+0.5));
    str.append (11, (int)(statCapacity.max()+0.5));
    str.append (12, 3, 2, statCapacity.average());
 
    str.append (PrintfFormat.NEWLINE + "   Utilization ");
    str.append (8, (int)(statUtil.min()+0.5));
    str.append (11, (int)(statUtil.max()+0.5));
    str.append (12, 3, 2, statUtil.average());
 
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
   
    str.append (PrintfFormat.NEWLINE + "   Service ");
    str.append (12, 3, 2, sojServ.min()).append (' ');  
    str.append (10, 3, 2, sojServ.max()).append (' ');
    str.append (11, 3, 2, sojServ.average()).append (' ');
    str.append (10, 3, 2, sojServ.standardDeviation());
    str.append (10, sojServ.numberObs());
    
    str.append (PrintfFormat.NEWLINE + "   Sojourn ");
    str.append (12, 3, 2, statSojourn.min()).append (' ');
    str.append (10, 3, 2, statSojourn.max()).append (' ');
    str.append (11, 3, 2, statSojourn.average()).append (' ');
    str.append (10, 3, 2, statSojourn.standardDeviation());
    str.append (10, statSojourn.numberObs()).append (PrintfFormat.NEWLINE);
    
    return str.toString();
   }
}
