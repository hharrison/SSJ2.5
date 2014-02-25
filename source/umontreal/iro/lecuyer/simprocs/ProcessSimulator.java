


/*
 * Class:        ProcessSimulator
 * Description:  Special type of simulator capable of managing processes
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
import umontreal.iro.lecuyer.simevents.Simulator;


/**
 * Defines a special type of simulator capable of managing processes.
 * This class extends the {@link Simulator} class
 * with methods providing basic facilities used by process simulators
 * to suspend, resume, and kill processes.
 * It also specifies a mechanism for creating control events used for process synchronization.
 * These methods are not called directly by the user, but they
 * need to be implemented by any framework managing processes.
 * 
 * <P>
 * A process simulator is usually constructed by the {@link #newInstance newInstance} method which
 * selects the appropriate implementation based on system properties.
 * It can also be constructed manually  by calling the appropriate constructor explicitly.
 * A default, implicit, simulator can also be used for compatibility with older programs.
 * This can be done by calling the {@link #initDefault initDefault} method or setting the
 * <TT>ssj.processes</TT> property.
 * 
 */
public abstract class ProcessSimulator extends Simulator  {

   protected SimProcess currentProcess;
   // The process who has control right now (current process).
   // If no process has control (an event is executing), current = null.



   /**
    * Returns the currently active process for this simulator.
    * 
    */
   public SimProcess currentProcess() {
      return currentProcess;
   }


   protected void setCurrentProcess (SimProcess currentProcess) {
      this.currentProcess = currentProcess;
   }


   /**
    * Constructs and returns a new {@link Event} object used for synchronization.
    * Such control events are used by process simulator to start a process or to resume it if it is already
    * started.
    * 
    */
   public abstract Event createControlEvent (SimProcess process);


   /**
    * Suspends the execution of <TT>process</TT> and
    *     schedules it to resume its execution in <TT>delay</TT> units of simulation
    *     time.  The state of the process also changes to <TT>DELAYED</TT>.
    * 
    * @param process SimProcess variable to delay.
    * 
    *    @param delay delay value, in simulation time units.
    * 
    * 
    */
   public abstract void delay (SimProcess process, double delay);


   /**
    * Suspends <TT>process</TT>.
    *    If the process is <TT>EXECUTING</TT>, this suspends its execution.
    *    If the process is <TT>DELAYED</TT>, this cancels its control event.
    *    This method also places the process in the <TT>SUSPENDED</TT> state.
    * 
    * @param process SimProcess variable to suspend.
    * 
    * 
    */
   public abstract void suspend (SimProcess process);


   /**
    * Terminates the life of <TT>process</TT> and sets its state to
    *    <TT>DEAD</TT>, after canceling its control event if there is one.
    * 
    */
   public abstract void kill (SimProcess process);


   /**
    * Kills all currently living (active, delayed, or suspended) processes managed by
    *   this simulator.
    * 
    */
   public abstract void killAll();


   /**
    * Initializes the default simulator to use processes.
    *     If the field <TT>Simulator.defaultSimulator</TT> is already initialized to a class
    *     extending <TT>ProcessSimulator</TT>, this
    *     method does nothing.
    *     Otherwise, it initializes the field to the return value of
    *     {@link #newInstance newInstance}.
    * 
    * <P>
    * Warning: <SPAN  CLASS="textbf">this method must be called before any event or process is
    *    constructed</SPAN>,  otherwise the program could return some exceptions like
    *    {@link ClassCastException} or {@link NullPointerException}.
    *    Alternatively, one can set the <TT>ssj.processes</TT> system property, which
    *    instructs this class to call this method at the time it is loaded.
    * 
    * <P>
    * The aim of this method is to allow the user to use default process-oriented
    *    simulation without giving options to the Java Virtual Machine.
    *    See package
    *    <TT>simevents</TT> and class {@link Simulator} for more information about the default simulator.
    * 
    */
   public static void initDefault()  {
      if (defaultSimulator instanceof ProcessSimulator) {
         defaultSimulator.init();
         return;
      }
      if (defaultSimulator instanceof Simulator) {
         Simulator temp = defaultSimulator;
         defaultSimulator = newInstance();
         defaultSimulator.init(temp.getEventList());
         return;
      }
      defaultSimulator = newInstance();
      defaultSimulator.init();
   }

   static {
      if (System.getProperty ("ssj.processes") != null)
         initDefault();
   } 


   /**
    * Constructs and returns a new process-oriented simulator.
    *   This method selects the concrete subclass of process simulator as follows.
    *   If the <TT>ssj.processSimulator</TT> system property is set, this gives the
    *   fully qualified class name of the process simulator to be instantiated.
    *   The given class must not be abstract, and must have a no-argument constructor.
    *   Otherwise, if the <TT>ssj.withThread</TT> system property is set, this returns a
    *   {@link ThreadProcessSimulator}.
    *   Otherwise, if the <TT>ssj.withDSOL</TT> system property is set, this
    *   returns a {@link DSOLProcessSimulator} instance.
    *   If no system property is set, this returns a {@link ThreadProcessSimulator}.
    * 
    * <P>
    * For example, if a program is called using <TT>java -Dssj.withDSOL ...</TT>, it will use DSOL
    *   for process simulation.
    * 
    */
   public static ProcessSimulator newInstance() {
      if(System.getProperty("ssj.processSimulator") != null) {
         ProcessSimulator myProcessSimulator = null;
         try{
            myProcessSimulator = (ProcessSimulator)(Class.forName(System.getProperty("ssj.processSimulator"))).newInstance();
         }
         catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
         catch (IllegalAccessException iae)  { iae.printStackTrace(); }
         catch (InstantiationException ie)   { ie.printStackTrace(); }
         return myProcessSimulator;
      }
      else if(System.getProperty("ssj.withThread") != null)
         return new ThreadProcessSimulator();
      else if (System.getProperty("ssj.withDSOL") != null)
         return new DSOLProcessSimulator();
      else
         return new ThreadProcessSimulator();
   }

}
