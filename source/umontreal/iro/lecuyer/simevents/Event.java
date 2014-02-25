

/*
 * Class:        Event
 * Description:  provides event scheduling tools
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

/**
 * This abstract class provides event scheduling tools.
 * Each type of event should be defined as a subclass of the
 * class <TT>Event</TT>, and should provide an implementation of the method
 * {@link #actions actions} which is executed when an event of this type occurs.
 * The instances of these subclasses are the actual events.
 * 
 * <P>
 * Each event is linked to a simulator represented by an instance of
 * {@link Simulator} before it can be scheduled and processed.
 * A default simulator, given by <TT>Simulator.getDefaultSimulator</TT>,
 * is used if no simulator is linked explicitly with an event.
 * When an event is constructed, it is not scheduled. It must be scheduled
 * separately by calling one of the scheduling methods {@link #schedule schedule},
 * {@link #scheduleNext scheduleNext}, {@link #scheduleBefore scheduleBefore}, etc.
 * An event can also be cancelled before it occurs.
 * 
 * <P>
 * A scheduled event has an associated time at which it will happen and a priority,
 * which can be queried using the methods {@link #time time} and {@link #priority priority}, respectively.
 * By default, events occur in ascending order of time, and have priority 1.
 * Events with the same time occur in ascending order of priority.
 * For example, if events <TT>e1</TT> and <TT>e2</TT> occur at the same time
 * with priority 2 and 1 respectively, then <TT>e2</TT> will occur before <TT>e1</TT>.
 * Events with the same time and priority occur in the order they were scheduled.
 * 
 */
public abstract class Event implements Comparable<Event> {

   protected Simulator sim;
   //simulator linked with the current event

   protected double priority;
   //priority of the event. Priority is a second parameter (after eventTime)
   // used to class events for their running order, in the EventList.

   protected double eventTime;
   // Planned time of occurence of this event.  Negative if not planned.
   // Is protected because it is used (changed) in Process.

   // Replace that with instanceof simProcess to completely detach processes.
   // protected boolean isProcess = false;
   // Will be true for objects of the subclass Process of the class Event.
   // (i.e., true if this event is a process.)

   private int myra = 0;
   // A new event must always occur after those with same time and 
   // same priority in the Event list. myra is used for that in
   // SplayTree.java.

   // For internal use
   public final int getRa() { return myra; }
   public final void setRa(int r) { myra = r; }



   /**
    * Constructs a new event instance, which can be placed afterwards
    *     into the event list of the default simulator.
    *     For example, if <TT>Bang</TT> is an <TT>Event</TT> subclass,
    *     the statement ``<TT>new Bang().scheduleNext();</TT>'' creates a new
    *     <TT>Bang</TT> event and places it at the beginning of the event list.
    * 
    */
   public Event()  {
      this (Simulator.getDefaultSimulator());
   } 


   /**
    * Construct a new event instance associated with the given simulator.
    *   
    * @param sim Instance of class Simulator associated with the new Event
    * 
    */
   public Event (Simulator sim)  {
      if (sim == null)
         throw new NullPointerException();
      eventTime = -10.0;
      priority = 1.0;
      this.sim = sim;
   } 


   /**
    * Schedules this event to happen in <TT>delay</TT> time units,
    *    i.e., at time <TT>sim.time() + delay</TT>, by inserting it in the event list.
    *    When two or more events are scheduled to happen at the same time and
    *     with the same priority,
    *    they are placed in the event list (and executed) in the same order
    *    as they have been scheduled.
    *    Note that the priority of this event should be adjusted using
    *    {@link #setPriority setPriority} <SPAN  CLASS="textit">before</SPAN> it is scheduled.
    *   
    * @param delay simulation time that must pass before the event happens
    * 
    * 
    */
   public void schedule (double delay)  {
      if (delay < 0.0)
         throw new IllegalArgumentException ("Cannot schedule in the past.");
      if (eventTime > -1.0)
         throw new IllegalStateException ("Event already scheduled");
      eventTime = sim.time() + delay;
      sim.eventList.add (this);
   } 


   /**
    * Schedules this event as the <EM>first</EM> event in the event
    *    list, to be executed at the current time (as the next event).
    * 
    */
   public void scheduleNext()  {
      if (eventTime > -1.0)
         throw new IllegalStateException ("Event already scheduled");
      eventTime = sim.time();
      priority  = 0.0;
      sim.eventList.addFirst (this);
   } 


   /**
    * Schedules this event to happen just before, and at the same
    *    time, as the event <TT>other</TT>.
    *     For example, if <TT>Bing</TT> and <TT>Bang</TT> are <TT>Event</TT>
    *      subclasses, after the statements
    * 
    * <P>
    * 
    * <DIV CLASS="vcode" ALIGN="LEFT">
    * <TT>
    * 
    * <BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bang bigOne = new Bang().schedule(12.0);
    * <BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new Bing().scheduleBefore(bigOne);
    * <BR>&nbsp;&nbsp;&nbsp;</TT>
    * </DIV>
    * the event list contains two new events scheduled to happen in 12
    *     units of time: a <TT>Bing</TT> event, followed by a <TT>Bang</TT> called
    *     <TT>bigOne</TT>.
    *   
    * @param other event before which this event will be scheduled
    * 
    * 
    */
   public void scheduleBefore (Event other)  {
      if (eventTime > -1.0)
         throw new IllegalStateException ("Event already scheduled");
      eventTime = other.eventTime;
      priority = other.priority;
      sim.eventList.addBefore (this, other);
   } 


   /**
    * Schedules this event to happen just after, and at the same
    *    time, as the event <TT>other</TT>.
    *   
    * @param other event after which this event will be scheduled
    * 
    * 
    */
   public void scheduleAfter (Event other)  {
      if (eventTime > -1.0)
         throw new IllegalStateException ("Event already scheduled");
      eventTime = other.eventTime;
      priority = other.priority;
      sim.eventList.addAfter (this, other);
   } 


   /**
    * Cancels this event and reschedules it to happen
    *    in <TT>delay</TT> time units.
    *   
    * @param delay simulation time units that must elapse before the event happens
    * 
    * 
    */
   public void reschedule (double delay)  {
      if (delay < 0.0)
         throw new IllegalArgumentException ("Cannot schedule in the past.");
      if (eventTime < -1.0)
         throw new IllegalStateException ("Event not scheduled");
      sim.getEventList().remove (this);
      eventTime = sim.time() + delay;
      sim.getEventList().add (this);
   } 


   /**
    * Cancels this event before it occurs.
    *    Returns <TT>true</TT> if cancellation succeeds (this event was found
    *    in the event list), <TT>false</TT> otherwise.
    *   
    * @return <TT>true</TT> if the event could be cancelled
    * 
    */
   public boolean cancel()  {
      boolean removed = false;
      if (eventTime >= sim.time()) removed = sim.getEventList().remove (this);
      eventTime = -10.0;
      return removed;
   } 


   /**
    * Finds the first occurence of an event of class ``type''
    *     in the event list, and cancels it.
    *     Returns <TT>true</TT> if cancellation succeeds, <TT>false</TT> otherwise.
    *   
    * @param type name of an event subclass
    * 
    *    @return <TT>true</TT> if an event of this class was found and cancelled
    * 
    */
   public final boolean cancel (String type)  {
      Event ev = sim.getEventList().getFirstOfClass (type);
      return ev.cancel();
   } 


   /**
    * Returns the simulator linked to this event.
    *   
    * @return the simulator linked to the event
    * 
    */
   public final Simulator simulator()  {
      return sim;
   } 


   /**
    * Sets the simulator associated with this event to
    *     <TT>sim</TT>.
    *     This method should not be called while this event is in an event list.
    *   
    * @param sim the Simulator
    * 
    * 
    */
   public final void setSimulator (Simulator sim)  {
      if (sim == null)
          throw new NullPointerException();
      if (eventTime > -1.0)
         throw new UnsupportedOperationException (
            "Unable to set Simulator, current Event already scheduled");
      this.sim = sim;
   } 


   /**
    * Returns the (planned) time of occurence of this event.
    *   
    * @return the time of occurence of the event
    * 
    */
   public final double time()  {
      return eventTime;
   } 


   /**
    * Sets the (planned) time of occurence of this event to <TT>time</TT>.
    *    This method should never be called after the event was scheduled, otherwise
    *    the events would not execute in ascending time order anymore.
    * 
    * @param time new time of occurence for the event
    * 
    * 
    */
   public final void setTime (double time)  {
      if (eventTime > -1.0)
         throw new UnsupportedOperationException(
            "Unable to set time, current Event already scheduled");
      eventTime = time;
   } 


   /**
    * Returns the priority of this event.
    *   
    * @return the priority of the event
    * 
    */
   public final double priority()  {
      return priority;
   } 


   /**
    * Sets the priority of this event to <TT>inPriority</TT>.
    *    This method should never be called after the event was scheduled, otherwise
    *    the events would not execute in ascending priority order anymore.
    *   
    * @param priority new priority for the event
    * 
    * 
    */
   public final void setPriority (double priority)  {
      if(eventTime > -1.0)
         throw new UnsupportedOperationException(
            "Unable to set priority, current Event already scheduled");
      this.priority = priority;
   } 


   /**
    * Compares this object with the specified object <TT>e</TT> for
    * order. Returns <SPAN CLASS="MATH">-1</SPAN> or <SPAN CLASS="MATH">+1</SPAN> as this event occurs before or after the specified
    * event <TT>e</TT>, respectively. If the two events occur at the same time, then
    * returns <SPAN CLASS="MATH">-1</SPAN>, <SPAN CLASS="MATH">0</SPAN>, or <SPAN CLASS="MATH">+1</SPAN> as this event has a smaller, equal, or larger
    * priority than event <TT>e</TT>.
    * 
    */
   public int compareTo (Event e)  {
      if (eventTime < e.time())
         return -1;
      if (eventTime > e.time())
         return 1;
      // Si le moment de declenchement des "Event" est identique, on
      // examine leurs priorites.
      if (priority < e.priority())
         return -1;
      if (priority > e.priority())
         return 1;
      return 0;
   } 


   /**
    * This is the method that is executed when this event occurs.
    *    Every subclass of <TT>Event</TT> that is to be instantiated must provide
    *    an implementation of this method.
    * 
    */
   public abstract void actions();

}

