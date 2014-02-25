

/*
 * Class:        ThreadProcessSimulator
 * Description:  process simulator using Java threads for process synchronization
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist
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
import umontreal.iro.lecuyer.simprocs.SimProcess;

/**
 * Represents a process simulator using Java threads for process synchronization.
 * The simulation process threads are synchronized so only one process runs
 * at a time.
 * 
 */
public class ThreadProcessSimulator extends ProcessSimulator  {

   private SimThread threadAllHead = null;
   //Tete de la liste des SimThread associated to this ThreadProcessSimulator


   /**
    * Creates a new {@link ThreadProcessSimulator} variable.
    * 
    */
   public ThreadProcessSimulator()  {
   }

   /**
    * Initializes the thread process-driven simulation  using
    *  {@link umontreal.iro.lecuyer.simevents.eventlist.SplayTree SplayTree} algorithm
    *  as {@link umontreal.iro.lecuyer.simevents.eventlist.EventList EventList}.
    *    This kills all processes already associated with the current variable.
    * 
    */
   public void init()  {
      super.init();
      /*if(threadAllHead != null)
         killAll();
      threadAllHead = null;                    //!!!!! utilisation de killAll instable !!!!!*/
   } 


   /**
    * Initializes the thread process-driven simulation
    *    using <TT>evlist</TT> variable as {@link EventList}.
    *    This kills all processes already associated with the current variable.
    * 
    * @param evlist EventList assigned to the current variable eventlist field
    * 
    * 
    */
   public void init (EventList evlist)  {
      super.init (evlist);
      /*if(threadAllHead != null)
         killAll();                            //!!!!! utilisation de killAll instable !!!!!*/
   } 


   public SimThread createControlEvent (SimProcess process) {
      return SimThread.getThread(process, this);
   }

   public void delay (SimProcess process, double delay) {
      if (currentProcess != process)
         throw new IllegalStateException  ("Calling delay() for a process not in EXECUTING state");
      if (delay < 0.0)
         throw new IllegalArgumentException ("Calling delay() with negative delay");
      process.scheduledEvent().schedule (delay);
      dispatch();
      ((SimThread)process.scheduledEvent()).passivate();
   }

   public void suspend(SimProcess process) {
      SimThread ev = (SimThread)process.scheduledEvent();

      if (ev == null)             // DEAD state
         throw new IllegalStateException ("Calling suspend() for a dead process");

      if (currentProcess == process) {             // EXECUTING state
         dispatch();
         ev.passivate();
         return;
      }

      if (ev.time() >= 0.0 ) { // DELAYED state
         ev.cancel();
         ev.setTime (SimProcess.WAITING);
         return;
      }

      if (ev.time() == SimProcess.STARTING ) // INITIAL state
         throw new IllegalStateException
                   ("Calling suspend() for a process in INITIAL state");

                                         // SUSPENDED state
      throw new IllegalStateException ("Calling suspend() for a suspended process");
   }

   public void kill(SimProcess process) {
      if (process.scheduledEvent() == null)
         throw new IllegalStateException ("cannot kill a DEAD process");
      ((SimThread)process.scheduledEvent()).kill();
   }

   /**
    * Kills all threads linked to the current variable.
    * 
    */
   public void killAll()  {
      SimThread.killAll(this);
   } 



    // This method looks for the next process to give it the control.
    // If there are some event in the eventList, they will be executed
    // This method doesn't reactivate the executive.
    // Its behavior is the same of the executive (see the Sim.start method)
   protected void dispatch() {
      Event ev;
      while ((ev = removeFirstEvent()) != null) {
         if (ev instanceof SimThread) {
            // This is a process, the control will transfered to it.
            currentProcess = ((SimThread)ev).myProcess;
            ((SimThread)ev).activate();
            return;
         }
         else ev.actions();
         // This event is executed by the calling process.
      }
      SimThread.simActivate(this);                     // Simulation is over.
   }

   protected SimThread threadAllHead() {
      return threadAllHead;
   }

   protected void setThreadAllHead(SimThread thread) {
      threadAllHead = thread;
   }
}





// %%%%%%%%%%%%%%%%%%%%%%%%%%%%   SimThread   %%%%%%%%%%%%%%%%%%%%%%%%%%%%
// thrown by the passivate() method and caught by the run() method.
// used to kill a process and recycle its associated thread.
// This empty class allows to control which Errors are thrown.
// If the JVM happens to throw an Error, it must not be caught
// by the SimThread class.
final class SimThreadError extends Error {
   public SimThreadError() {}
}


final class SimThread extends Event implements Runnable {
   static SimThreadError error = new SimThreadError();
    // thrown by the passivate() method and caught by the run() method.
    // used to kill a process and recycle its associated thread.

   private int n = 0;
    // Semaphore: if n < 0 this thread must wait.

   SimProcess myProcess;
    // The Process to which this thread is associated.

   private Thread myThread;
   // The Thread in which this SimThread object will run.

    // link with the next thread in the list headed by sim.threadAllHead, used by killAll().
   private SimThread nextAll = null;

   // Head of the free SimThread list
   private static SimThread threadFreeHead = null;
    // link with the next thread in the list
   private SimThread nextFree = null;

    // Constructor.
   private SimThread (SimProcess p, ThreadProcessSimulator inSim) {
      super(inSim);
      eventTime = SimProcess.STARTING;
      myProcess = p;
      myThread = new Thread (this);
      myThread.setDaemon (true);
      myThread.start();
      nextAll = ((ThreadProcessSimulator)sim).threadAllHead();
      ((ThreadProcessSimulator)sim).setThreadAllHead(this);
    }

   public static final SimThread getThread (SimProcess p, ThreadProcessSimulator inSim) {
      if (threadFreeHead == null) return new SimThread (p, inSim);
      SimThread th = threadFreeHead;   threadFreeHead = threadFreeHead.nextFree;
      th.myProcess = p;
      th.eventTime = SimProcess.STARTING;
      th.sim       = inSim;
      th.priority  = 1.0;
      return th;
    }

   // A SimThread object created and started is never destroyed
   // till the end of the simulation.
   // if the associated process is killed, an error exception will be thrown.
   // this exception will be caught here and the thread will be
   // passivated (in the next iteration).

   public final void run() {
       while(true) {
           try {
               passivate();
               myProcess.actions();  // myProcess starts its life.
               ((ThreadProcessSimulator)sim).dispatch(); // Give control to another process.
           } catch (SimThreadError e) {} // An SimThreadError exception is thrown because
                                // the process is killed.
           myProcess.setScheduledEvent(null);
           myProcess = null;
           nextFree = threadFreeHead;   threadFreeHead = this;
       }
   }

   public void actions() {
   // This method will be executed only once.
   // It transfers the control from the executive to this thread.
   // The control will then be passed from process to process,
   // which will execute the events if any.
   // Control will be returned to the executive only at the end of simulation.
      ((ThreadProcessSimulator)sim).setCurrentProcess(myProcess);
      activate();  // Notify this thread to be ready to take control
                   // as soon as the caller's thread is passivated.
      simPassivate(((ThreadProcessSimulator)sim));  // Passivate the main thread (executive).
   }


   protected synchronized final void activate() {
   // Notifies this thread to be ready to take control.
   // It will take control when the calling thread passivates.
      n++;   notify();
   }


   protected synchronized final void passivate() {
      try {n--;  if (n<0)  wait();}
         catch (InterruptedException e) {
            n++;   throw error;
             // This thread is waken up and interrupted because
             // the kill() method was called on its associated process.
             // Throws a SimThreadError that
             // will be caught by run() method.
         }
   }


   protected static final void simActivate(ThreadProcessSimulator sim) {
      EventList evl = sim.getEventList();
      synchronized (evl) {evl.notify(); }
   }

   protected static final void simPassivate(ThreadProcessSimulator sim) {
      EventList evl = sim.getEventList();
      synchronized (evl) {
         try { evl.wait(); } catch (InterruptedException e) {}
      }
   }

   // The following methods are called by kill and killAll
   // of the Process class.

   protected void kill() {
        if (eventTime >= 0.0)
           cancel();
        myThread.interrupt();
   }

   protected static void  killAll(ThreadProcessSimulator sim) {
       SimThread th = sim.threadAllHead();
       while (th != null) {
           if (th.myProcess != null) th.kill();
           th = th.nextAll;

       }
   }

   public String toString() {
      // To get something useful when printing the event list
      return "Start or resume process " + myProcess.toString();
   }
}

