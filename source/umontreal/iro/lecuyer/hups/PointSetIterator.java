

/*
 * Interface:    PointSetIterator
 * Description:  Iterator over point sets
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

package umontreal.iro.lecuyer.hups;

import umontreal.iro.lecuyer.rng.RandomStream;

/**
 * Objects of classes that implement this interface are <SPAN  CLASS="textit">iterators</SPAN> 
 * that permit one to enumerate
 * (or observe) the successive points of a point set and the successive 
 * coordinates of these points.
 * Each {@link PointSetIterator} is associated with a given point set
 * and maintains a <SPAN  CLASS="textit">current point</SPAN> index <SPAN CLASS="MATH"><I>i</I></SPAN> and a <SPAN  CLASS="textit">current coordinate</SPAN>
 * index <SPAN CLASS="MATH"><I>j</I></SPAN>, which are both initialized to zero.
 * 
 * <P>
 * Successive coordinates can be accessed one or many at a time by the methods
 * {@link #nextCoordinate nextCoordinate} and {@link #nextCoordinates nextCoordinates}, respectively.
 * The current coordinate index <SPAN CLASS="MATH"><I>j</I></SPAN> can be set explicitely by
 * {@link #setCurCoordIndex setCurCoordIndex} and {@link #resetCurCoordIndex resetCurCoordIndex}. 
 * Similar methods are available for resetting and accessing the current point.
 * The method {@link #nextPoint nextPoint} permits one to 
 * enumerate the successive points in natural order. 
 * 
 * <P>
 * This class also implements the {@link RandomStream} interface.
 * This permits one to replace random numbers by the coordinates of
 * (randomized) quasi-Monte Carlo points without changing the code that calls
 * the generators in a simulation program.
 * That is, the same simulation program can be used for both Monte Carlo 
 * and quasi-Monte Carlo simulations.
 * The method {@link #nextDouble nextDouble} does exactly the same as 
 * {@link #nextCoordinate nextCoordinate}, it returns the current coordinate of the 
 * current point and advances the current coordinate by one.  
 * The substreams correspond to the points, so  
 * {@link #resetStartSubstream resetStartSubstream} resets the current point coordinate to zero, 
 * {@link #resetNextSubstream resetNextSubstream} resets the iterator to the next point, and
 * {@link #resetStartStream resetStartStream} resets the iterator to the first point of 
 * the point set.
 * 
 * <P>
 * There can be several iterators over the same point set.
 * These iterators are independent from each other.  
 * Classes that implement this interface must maintain enough information 
 * so that each iterator is unaffected by other iterator's operations.
 * However, the iterator does not need to be independent of the underlying 
 * point set.  If the point set is modified (e.g., randomized), the iterator
 * may continue to work as usual.
 * 
 * <P>
 * Point set iterators are implemented as inner classes because
 * this gives a direct access to the private members (or variables) 
 * of the class.  This is important for efficiency.
 * They are quite similar to the iterators in Java <SPAN  CLASS="textit">collections</SPAN>.
 * 
 */
public interface PointSetIterator extends RandomStream {

   /**
    * Sets the current coordinate index to <SPAN CLASS="MATH"><I>j</I></SPAN>, so that 
    *    the next calls to {@link #nextCoordinate nextCoordinate} or  {@link #nextCoordinates nextCoordinates}
    *    will return the values 
    * <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB>, <I>u</I><SUB>i, j+1</SUB>,...</SPAN>, where <SPAN CLASS="MATH"><I>i</I></SPAN> is the
    *    index of the current point.
    *  
    * @param j index of the new current coordinate
    * 
    * 
    */
   public void setCurCoordIndex (int j);


   /**
    * Equivalent to {@link #setCurCoordIndex setCurCoordIndex}&nbsp;<TT>(0)</TT>.
    * 
    */
   public void resetCurCoordIndex();


   /**
    * Returns the index <SPAN CLASS="MATH"><I>j</I></SPAN> of the current coordinate.  This may be useful,
    *    e.g., for testing if enough coordinates are still available.
    *  
    * @return index of the current coordinate
    * 
    */
   public int getCurCoordIndex();


   /**
    * Returns <TT>true</TT> if the current point has another coordinate.
    *    This can be useful for testing if coordinates are still available.
    *  
    * @return <TT>true</TT> if the current point has another coordinate
    * 
    */
   public boolean hasNextCoordinate();


   /**
    * Returns the current coordinate <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN> and advances to the next one.
    *    If no current coordinate is available (either because the current
    *    point index has reached the number of points or because the current
    *    coordinate index has reached the number of dimensions), it throws a 
    *    {@link java.util.NoSuchElementException NoSuchElementException}.
    *  
    * @return value of the current coordinate
    *    @exception NoSuchElementException if no such coordinate is available
    * 
    * 
    */
   public double nextCoordinate();


   /**
    * Returns the next <TT>d</TT> coordinates of the current point in <TT>p</TT>
    *    and advances the current coordinate index by <TT>d</TT>.
    *    If the remaining number of coordinates is too small, a
    *    <TT>NoSuchElementException</TT> is thrown, as in {@link #nextCoordinate nextCoordinate}.
    * 
    * @param p array to be filled with the coordinates, starting at index 0
    * 
    *    @param d number of coordinates to get
    * 
    *    @exception NoSuchElementException if there are not enough 
    *              remaining coordinates in the current point
    * 
    * 
    */
   public void nextCoordinates (double[] p, int d);


   /**
    * Resets the current point index to <SPAN CLASS="MATH"><I>i</I></SPAN> and the current coordinate 
    *    index to zero.  If <TT>i</TT> is larger or equal to the number of points,
    *    an exception will <SPAN  CLASS="textit">not</SPAN> be raised here, but only later if we 
    *    ask for a new coordinate or point. 
    * 
    * @param i new index of the current point
    * 
    * 
    */
   public void setCurPointIndex (int i);


   /**
    * Equivalent to {@link #setCurPointIndex setCurPointIndex}&nbsp;<TT>(0)</TT>.
    * 
    */
   public void resetCurPointIndex();


   /**
    * Increases the current point index by 1 and returns its new value.
    *    If there is no more point, an exception will be raised only if we 
    *    ask for a new coordinate or point later on. 
    * 
    * @return index of the new current point
    * 
    */
   public int resetToNextPoint();


   /**
    * Returns the index <SPAN CLASS="MATH"><I>i</I></SPAN> of the current point.  
    *    This can be useful, e.g., for caching point sets.
    * 
    * @return index of the current point
    * 
    */
   public int getCurPointIndex();


   /**
    * Returns <TT>true</TT> if there is a next point.
    *    This can be useful for testing if points are still available.
    *  
    * @return <TT>true</TT> if a next point is available from the iterated point set
    * 
    */
   public boolean hasNextPoint();


   /**
    * Returns the <SPAN  CLASS="textit">first</SPAN> <TT>d</TT> coordinates of the <SPAN  CLASS="textit">current</SPAN> 
    *    point in <TT>p</TT>, advances to the next point, and
    *    returns the index of the <SPAN  CLASS="textit">new</SPAN> current point.
    *    Even if the current coordinate index is 0, the point returned
    *    starts from coordinate 0.
    *    After obtaining the last point via this method, the current point
    *    index (returned by the method) is equal to the number of points,
    *    so it is no longer a valid point index.
    *    An exception will then be raised if we attempt to generate additional
    *    points or coordinates.
    * 
    * <P>
    * Specialized implementations of this method often allow for increased 
    *   efficiency, e.g., for cycle-based point sets where the cycles 
    *   (but not the points)
    *   are stored explicitly or for digital nets  
    *   by allowing non-incremental point enumerations via Gray-code counters.
    *  
    * @param p array to be filled with the coordinates, 
    *              starting from array index 0
    * 
    *    @param d number of coordinates to return
    * 
    *    @return index of the new current point
    *    @exception NoSuchElementException if there are not enough coordinates 
    *      available in the current point for filling <TT>p</TT>
    * 
    */
   public int nextPoint (double[] p, int d);
}
