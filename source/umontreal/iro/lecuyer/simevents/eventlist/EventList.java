

/*
 * Class:        EventList
 * Description:  interface for implementations of event lists
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

package umontreal.iro.lecuyer.simevents.eventlist; 

import java.util.ListIterator; 
import umontreal.iro.lecuyer.simevents.Event;


/**
 * An interface for implementations of event lists.
 * Different implementations are provided in SSJ:
 * doubly-linked list, splay tree, Henricksen's method, etc.
 * The <EM>events</EM> in the event list are objects of the class
 * {@link umontreal.iro.lecuyer.simevents.Event Event}.
 * The method {@link umontreal.iro.lecuyer.simevents.Sim#init(EventList) Sim.init}
 * permits one to select
 * the actual implementation used in a simulation.
 * 
 * <P>
 * To allow the user to print the event list, the 
 * {@link java.lang.Object#toString toString} method
 * from the {@link Object} class should be reimplemented in all <TT>EventList</TT>
 * implementations.  It will return a string in the following format:
 * ``<TT>Contents of the event list </TT><SPAN  CLASS="textit">event list class</SPAN><TT>:</TT>'' for the first line and
 * each subsequent line has format 
 * ``<SPAN  CLASS="textit">scheduled event time</SPAN><TT>, </TT><SPAN  CLASS="textit">event priority</SPAN>&nbsp;<TT>:</TT>&nbsp;<SPAN  CLASS="textit">event string</SPAN>''.
 * The <SPAN  CLASS="textit">event string</SPAN> is obtained by calling the
 * <TT>toString</TT> method of the event objects.
 * The string should not end with the end-of-line character.
 * 
 * <P>
 * The following example is the event list of the bank example,
 * printed at 10h30.  See <TT>examples.pdf</TT> for more information.
 * 
 * <P>
 * <PRE>
 * Contents of the event list SplayTree:
 *    10.51,        1 : BankEv$Arrival@cfb549
 *    10.54,        1 : BankEv$Departure@8a7efd
 *       11,        1 : BankEv$3@86d4c1
 *       14,        1 : BankEv$4@f9f9d8
 *       15,        1 : BankEv$5@820dda
 * </PRE>
 * 
 */
public interface EventList extends Iterable<Event> {

 
   /**
    * Returns <TT>true</TT> if and only if the event list is empty
    *    (no event is scheduled).
    *  
    * @return <TT>true</TT> if the event list is empty
    * 
    */
   public boolean isEmpty();


   /**
    * Empties the event list, i.e., cancels all events.
    * 
    */
   public void clear();


   /**
    * Adds a new event in the event list, according to
    *   the time of <TT>ev</TT>.
    *   If the event list contains events scheduled to happen at the same time as
    *   <TT>ev</TT>, <TT>ev</TT> must be added after all these events.
    *  
    * @param ev event to be added
    * 
    * 
    */
   public void add (Event ev);


   /**
    * Adds a new event at the beginning of the event list.  The given
    *    event <TT>ev</TT> will occur at the current simulation time.
    *  
    * @param ev event to be added
    * 
    * 
    */
   public void addFirst (Event ev);


   /**
    * Same as {@link #add add}, but adds the new event <TT>ev</TT>
    *   immediately before the event <TT>other</TT> in the list.
    *  
    * @param ev event to be added
    * 
    *    @param other reference event before which <TT>ev</TT> will be added
    * 
    * 
    */
   public void addBefore (Event ev, Event other);


   /**
    * Same as {@link #add add}, but adds the new event <TT>ev</TT>
    *   immediately after the event <TT>other</TT> in the list.
    *  
    * @param ev event to be added
    * 
    *    @param other reference event after which <TT>ev</TT> will be added
    * 
    * 
    */
   public void addAfter (Event ev, Event other);


   /**
    * Returns the first event in the event list.
    *  If the event list is empty, returns <TT>null</TT>.
    *  
    * @return the first event in the event list, or <TT>null</TT> if the list is empty
    * 
    */
   public Event getFirst();


   /**
    * Returns the first event of the class <TT>cl</TT> (a subclass of
    *   <TT>Event</TT>) in the event list.  If no such event is found, returns
    *   <TT>null</TT>.
    *  
    * @return the first event of class <TT>cl</TT>, or <TT>null</TT> if no such event exists in the list
    * 
    */
   public Event getFirstOfClass (String cl);


   /**
    * Returns the first event of the class <TT>E</TT> (a subclass of
    *   <TT>Event</TT>) in the event list. If no such event is found, returns
    *   <TT>null</TT>.
    *  
    * @return the first event of class <TT>cl</TT>, or <TT>null</TT> if no such event exists in the list
    * 
    */
   public <E extends Event> E getFirstOfClass (Class<E> cl);


   /**
    * Returns a list iterator over the elements of the class <TT>Event</TT> in this list.
    * 
    * @return a list iterator over the elements of the class <TT>Event</TT> in this list
    * 
    */
   public ListIterator<Event> listIterator();


   /**
    * Removes the event <TT>ev</TT> from the event list (cancels this event).
    *  Returns <TT>true</TT> if and only if the event removal has succeeded.
    *  
    * @param ev event to be removed
    * 
    *    @return <TT>true</TT> if the event was successfully removed from the list
    * 
    */
   public boolean remove (Event ev);


   /**
    * Removes the first event from the event list (to cancel or
    *     execute this event).  Returns the removed event.  If the list is empty,
    *     then <TT>null</TT> is returned.
    *  
    * @return the first event removed from the list, or <TT>null</TT> if the list is empty
    * 
    */
   public Event removeFirst();

}
