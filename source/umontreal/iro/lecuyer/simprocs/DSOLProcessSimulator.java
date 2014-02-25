

/*
 * Class:        DSOLProcessSimulator
 * Description:  simulation process whose actions method is interpreted by
                 the DSOL interpreter
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
import nl.tudelft.simulation.dsol.interpreter.process.Process;

/*
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/">
 * www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public
 * License (GPL) </a>, no warranty <br>
 *
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter
 *         Jacobs </a>
 * @version $Revision$ $Date$
 * @since 1.2
 */ 


/**
 * Represents a simulation process whose <TT>actions</TT> method is
 * interpreted by the DSOL interpreter, written by
 * Peter Jacobs (<TT><A NAME="tex2html1"
 *   HREF="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">http://www.tbm.tudelft.nl/webstaf/peterja/index.htm</A></TT>).
 * When a process executes, a virtual machine
 * implemented in Java is invoked to interpret the byte-code.
 * The processes are then simulated in a single Java thread, which allows
 * a much larger number of threads than when each process has its own
 * native thread, at the expense of a slower execution time.
 * 
 */
public class DSOLProcessSimulator extends ProcessSimulator {



   /**
    * Constructs a new DSOLProcessSimulator variable without initialization.
    * 
    */
   public DSOLProcessSimulator()  {
   }


   /**
    * Initializes the process-driven simulation. Calls super.init().
    * 
    */
   public void init() {
      super.init();
   }


   /**
    * Initializes the simulation and sets the given event list <TT>evlist</TT>
    *   to be used by the simulation executive. Calls super.init(evlist).
    * 
    * @param evlist selected event list implementation
    * 
    * 
    */
   public void init (EventList evlist) {
      super.init (evlist);
   }

   public ResumeEvent createControlEvent (SimProcess process) {
      return new ResumeEvent (this, new InterpretedThread (process));
   }

   public void delay (SimProcess process, double delay) {
      if (process.scheduledEvent() == null) // DEAD state
         throw new IllegalStateException
               ("Calling delay() for a dead process");
      if (currentProcess != process)
         throw new IllegalStateException
               ("Calling delay() for a process not in EXECUTING state");
      if (delay < 0.0)
         throw new IllegalArgumentException
               ("Calling delay() with negative delay");
      process.scheduledEvent().schedule (delay);
      process.suspend();
   }

   public void suspend(SimProcess process) {
      if (process.scheduledEvent() == null) // DEAD state
         throw new IllegalStateException
               ("Calling suspend() for a dead process");
      if (currentProcess == process) {
         ((ResumeEvent)process.scheduledEvent()).target().suspend();               /// attention, pas le meme process
         return;
      }

      if (process.scheduledEvent().time() >= 0.0) {
         process.scheduledEvent().cancel();
         process.scheduledEvent().setTime (SimProcess.WAITING);
         return;
      }

      if (process.scheduledEvent().time() == SimProcess.STARTING)
      // INITIAL state
         throw new IllegalStateException
               ("Calling suspend() for a process in INITIAL state");
      // SUSPENDED state
      throw new IllegalStateException
            ("Calling suspend() for a suspended process");
   }

   public void kill(SimProcess process) {
      if (process.scheduledEvent() == null)
         throw new IllegalStateException
            ("Calling kill() on a DEAD process");
      if (currentProcess == process) {
         process.setScheduledEvent(null);
         InterpretedThread pr = ((ResumeEvent)process.scheduledEvent()).target();
         ((ResumeEvent)process.scheduledEvent()).setTarget(null);
         pr.suspend();
         return;
      }

      if (process.scheduledEvent().time() >= 0.0)
         process.scheduledEvent().cancel();

      process.setScheduledEvent(null);
      ((ResumeEvent)process.scheduledEvent()).setTarget(null);
   }

   public void killAll() {}


}

   final class ResumeEvent extends Event {
      // the process to resume */
      private InterpretedThread target = null;

      public ResumeEvent (DSOLProcessSimulator sim, final InterpretedThread target) {
         super(sim);
         this.target = target;
         setTime (SimProcess.STARTING);
      }

      public InterpretedThread target() {
         return target;
      }

      public void setTarget(InterpretedThread target) {
         this.target = target;
      }

      public void actions() {
         ((ProcessSimulator)sim).setCurrentProcess(target.getProcess());
         target.resume();
      }


      public String toString() {
         // To get something useful when printing the event list
         return "Start or resume process ";
      }
   }


// The InterpretedThread is used by SSJ as basis for the DSOL SimProcess.
final class InterpretedThread extends Process {
   // the process to execute
   private SimProcess simProcess = null;

   public InterpretedThread(final SimProcess simProcess) {
      this.simProcess = simProcess;
   }

   public SimProcess getProcess() {
      return simProcess;
   }

   public void process() {
      simProcess.actions();
      simProcess.setScheduledEvent (null);
      //((ResumeEvent)simProcess.scheduledEvent()).setTarget( null );
      simProcess = null;
   }
}
