

/*
 * Class:        Sim
 * Description:  contains the executive of a discrete-event simulation
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

package umontreal.iro.lecuyer.simevents;
import umontreal.iro.lecuyer.simevents.eventlist.EventList;
import umontreal.iro.lecuyer.simevents.eventlist.SplayTree;
import umontreal.iro.lecuyer.simevents.Event;
import umontreal.iro.lecuyer.simevents.Simulator;

/**
 * This static class contains the executive of a discrete-event simulation.
 * It maintains the simulation clock and starts executing the events 
 * in the appropriate order.
 * Its methods permit one to start, stop, and (re)initialize the simulation,
 * and read the simulation clock.
 * 
 * <P>
 * Starting from SSJ-2.0, the <TT>Sim</TT> class now uses
 * the default simulator returned by the <TT>getDefaultSimulator()</TT>
 * method in the {@link Simulator} class. Although the <TT>Sim</TT> class is 
 * perfectly adequate for simple simulations, the {@link Simulator} class
 * is more general and supports more functionnalities.
 * For example, if one needs to have more than one simulation clock and event list,
 * one will have to use the {@link Simulator} class instead of the simpler
 * <TT>Sim</TT> class.
 * 
 */
public final class Sim  {

   // Prevents construction of this object
   private Sim() {}   



   /**
    * Returns the current value of the simulation clock. 
    * @return the current simulation time
    * 
    */
   public static double time()  {
      return Simulator.getDefaultSimulator().time();
   }


   /**
    * Reinitializes the simulation executive by clearing up the event
    *    list, and resetting the simulation clock to zero.
    *    This method must not be used to initialize process-driven
    *    simulation; {@link umontreal.iro.lecuyer.simprocs.SimProcess#init SimProcess.init}
    *    must be used instead.
    * 
    */
   public static void init()  {
     // This has to be done another way in order to separate events and processes.
      Simulator.getDefaultSimulator().init();
   }

 
   /**
    * Same as {@link #init init}, but also chooses <TT>evlist</TT> as the 
    *     event list to be used. For example, calling 
    *     <TT>init(new DoublyLinked())</TT> initializes the simulation
    *     with a doubly linked linear structure for the event list.
    *    This method must not be used to initialize process-driven
    *    simulation; 
    * {@link umontreal.iro.lecuyer.simprocs#DSOLProcessSimulator(init) umontreal.iro.lecuyer.simprocs.DSOLProcessSimulator}&nbsp;<TT>(EventList)</TT> or 
    * <BR>{@link umontreal.iro.lecuyer.simprocs#ThreadProcessSimulator(init) umontreal.iro.lecuyer.simprocs.ThreadProcessSimulator}&nbsp;<TT>(EventList)</TT> 
    *    must be used instead.
    *   
    * @param evlist selected event list implementation
    * 
    * 
    */
   public static void init (EventList evlist)  {
     Simulator.getDefaultSimulator().init(evlist);
   }


   /**
    * Gets the currently used event list.
    * 
    * @return the currently used event list
    * 
    * 
    */
   public static EventList getEventList() {
      return Simulator.getDefaultSimulator().getEventList();
   }

   protected static Event removeFirstEvent() {
       return Simulator.getDefaultSimulator().removeFirstEvent();
   }


   /**
    * Starts the simulation executive.
    *    There must be at least one <TT>Event</TT> in the
    *    event list when this method is called.
    * 
    */
   public static void start()  {
      Simulator.getDefaultSimulator().start();
   }


   /**
    * Tells the simulation executive to stop as soon as it takes control,
    *    and to return control to the program that called {@link #start start}.
    *    This program will then continue
    *    executing from the instructions right after its call to <TT>Sim.start</TT>.
    *    If an {@link Event} is currently executing (and this event has just called
    *    <TT>Sim.stop</TT>), the executive will take
    *    control when the event terminates its execution.
    * 
    */
   public static void stop()  {
      Simulator.getDefaultSimulator().stop();
   }


   // Used to passivate and activate the main process.
   // See the SimProcess.dispatch() and SimThread.actions()
 
//    protected static void activate() { 
 //       synchronized (eventList) {eventList.notify(); } 
 //    }
         
//     protected static void passivate() {
//         synchronized (eventList) {
  //          try { eventList.wait(); } catch (InterruptedException e) {}
  //       }
  //   }
}
