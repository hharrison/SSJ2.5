

/*
 * Class:        RandomStreamManager
 * Description:  Manages a list of random streams
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

package umontreal.iro.lecuyer.rng;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import umontreal.iro.lecuyer.rng.RandomStream;


/**
 * Manages a list of random streams for more convenient synchronization.
 * All streams in the list can be reset simultaneously by a single 
 * call to the appropriate method of this stream manager,
 * instead of calling explicitly the reset method for each 
 * individual stream.
 * 
 * <P>
 * After a random stream manager is constructed, any
 * existing {@link RandomStream} object can be registered
 * to this stream manager (i.e., added to the list)
 * and eventually unregistered (removed from the list).
 * 
 */
public class RandomStreamManager {
   private List streams = new ArrayList();


   /**
    * Adds the given <TT>stream</TT> to the internal list of
    *  this random stream manager and returns the added stream.
    * 
    * @param stream the stream being added.
    * 
    *    @return the added stream.
    *    @exception NullPointerException if <TT>stream</TT> is <TT>null</TT>.
    * 
    * 
    */
   public RandomStream add (RandomStream stream) {
      if (stream == null)
         throw new NullPointerException();
      if (streams.contains (stream))
         return stream;
      streams.add (stream);
      return stream;
   }


   /**
    * Removes the given stream from the internal list of this random
    *  stream manager.  Returns <TT>true</TT> if the stream was
    *  properly removed, <TT>false</TT> otherwise.
    * 
    * @param stream the stream being removed.
    * 
    *    @return the success indicator of the operation.
    * 
    */
   public boolean remove (RandomStream stream) {
      return streams.remove (stream);
   }


   /**
    * Removes all the streams from the internal list
    *  of this random stream manager.
    * 
    */
   public void clear() {
      streams.clear();
   }


   /**
    * Returns an unmodifiable list containing all the
    *  random streams in this random
    *  stream manager.  The returned list, constructed by
    *  {@link java.util.Collections#unmodifiableList((List)) unmodifiableList}, can be assumed
    *  to contain non-<TT>null</TT> {@link RandomStream} instances.
    * 
    * @return the list of managed random streams.
    * 
    */
   public List getStreams() {
      return Collections.unmodifiableList (streams);
   }


   /**
    * Forwards to the
    *  {@link umontreal.iro.lecuyer.rng.RandomStream#resetStartStream(()) resetStartStream} methods
    *  of all streams in the list.
    * 
    */
   public void resetStartStream() {
      for (int s = 0; s < streams.size(); s++)
         ((RandomStream)streams.get (s)).resetStartStream();
   }


   /**
    * Forwards to the
    *  {@link umontreal.iro.lecuyer.rng.RandomStream#resetStartSubstream(()) resetStartSubstream} methods
    *  of all streams in the list.
    * 
    */
   public void resetStartSubstream() {
      for (int s = 0; s < streams.size(); s++)
         ((RandomStream)streams.get (s)).resetStartSubstream();
   }


   /**
    * Forwards to the
    *  {@link umontreal.iro.lecuyer.rng.RandomStream#resetNextSubstream(()) resetNextSubstream} methods
    *  of all streams in the list.
    * 
    */
   public void resetNextSubstream() {
      for (int s = 0; s < streams.size(); s++)
         ((RandomStream)streams.get (s)).resetNextSubstream();
   }


   public String toString() {
      StringBuffer sb = new StringBuffer (getClass().getName());
      sb.append ('[');
      sb.append ("number of stored streams: ").append (streams.size());
      sb.append (']');
      return sb.toString();
   }
}
