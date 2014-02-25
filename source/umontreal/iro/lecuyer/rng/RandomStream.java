

/*
 * Class:        RandomStream
 * Description:  basic structures to handle multiple streams of uniform
                 (pseudo)-random numbers and tools to move around within
                 and across these streams
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Pierre L'Ecuyer
 * @since        2000

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

/**
 * This interface defines the basic structures to handle multiple streams
 * of uniform (pseudo)random numbers and convenient
 * tools to move around within and across these streams.
 * The actual random number generators (RNGs) are provided in classes
 * that implement this <TT>RandomStream</TT> interface.
 * Each stream of random numbers is an object of the class that implements
 * this interface, and can be viewed as a virtual random number generator.
 * 
 * <P>
 * For each type of base RNG (i.e., each implementation of the 
 * <TT>RandomStream</TT> interface), the full period of the generator
 * is cut into adjacent <EM>streams</EM> (or segments) of length <SPAN CLASS="MATH"><I>Z</I></SPAN>, 
 * and each of these streams is partitioned into <SPAN CLASS="MATH"><I>V</I></SPAN> <EM>substreams</EM>
 * of length <SPAN CLASS="MATH"><I>W</I></SPAN>, where <SPAN CLASS="MATH"><I>Z</I> = <I>VW</I></SPAN>.
 * The values of <SPAN CLASS="MATH"><I>V</I></SPAN> and <SPAN CLASS="MATH"><I>W</I></SPAN> depend on the specific RNG, but are usually
 * larger than <SPAN CLASS="MATH">2<SUP>50</SUP></SPAN>.
 * Thus, the distance <SPAN CLASS="MATH"><I>Z</I></SPAN> between the starting points of two successive 
 * streams provided by an RNG usually exceeds <SPAN CLASS="MATH">2<SUP>100</SUP></SPAN>.
 * The initial seed of the RNG is the starting point of the first stream.  
 * It has a default value for each type of RNG,
 * but this initial value can be changed by calling <TT>setPackageSeed</TT> 
 * for the corresponding class.
 * Each time a new <TT>RandomStream</TT> is created, its starting point
 * (initial seed) is computed automatically,
 * <SPAN CLASS="MATH"><I>Z</I></SPAN> steps ahead of the starting point of the previously created stream
 * of the same type, and its current state is set equal to this starting point.
 * 
 * <P>
 * For each stream, one can advance by one step and generate one value,
 * or go ahead to the beginning of the next substream within this stream, 
 * or go back to the beginning of the current substream, or to the beginning
 * of the stream, or jump ahead or back by an arbitrary number of steps.
 * Denote by <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> the current state of a stream <SPAN CLASS="MATH"><I>g</I></SPAN>,
 * <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN> its initial state, <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> the state at the beginning of the
 * current substream, and <SPAN CLASS="MATH"><I>N</I><SUB>g</SUB></SPAN> the state at the beginning of the next substream.
 * 
 * The form of the state of a stream depends on its type.
 * For example, the state of a stream of class {@link MRG32k3a} is a vector
 * of six 32-bit integers represented internally as floating-point numbers
 * (in <TT>double</TT>).
 * 
 * <P>
 * The methods for manipulating the streams and generating random
 * numbers are implemented differently for each type of RNG.
 * The methods whose formal parameter types do not depend
 * on the RNG type are specified in the interface <TT>RandomStream</TT>.
 * The others (e.g., for setting the seeds) are given only in the
 * classes that implement the specific RNG types.
 * 
 * <P>
 * 
 * <P>
 * Methods for generating random variates from non-uniform distributions
 * are provided in the {@link umontreal.iro.lecuyer.randvar randvar} package.
 * 
 */
public interface RandomStream  { 


   /**
    * Reinitializes the stream to its initial state <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>:
    *    <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> and <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> are set to <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>.
    * 
    */
   public void resetStartStream();


   /**
    * Reinitializes the stream to the beginning of its current
    *    substream: <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> is set to <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN>.
    * 
    */
   public void resetStartSubstream();


   /**
    * Reinitializes the stream to the beginning of its next
    *    substream: <SPAN CLASS="MATH"><I>N</I><SUB>g</SUB></SPAN> is computed, and
    *    <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> and <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> are set to <SPAN CLASS="MATH"><I>N</I><SUB>g</SUB></SPAN>.
    * 
    */
   public void resetNextSubstream();


   /**
    * Returns a string containing the current state of this stream.
    *   
    * @return the state of the generator formated as a string
    * 
    */
   public String toString();


   /**
    * Returns a (pseudo)random number from the uniform distribution
    *    over the interval <SPAN CLASS="MATH">(0, 1)</SPAN>, using this stream, after advancing its
    *    state by one step.  The generators programmed in SSJ never 
    *    return the values 0 or 1.
    *   
    * @return the next generated uniform
    * 
    */
   public double nextDouble();


   /**
    * Generates <TT>n</TT> (pseudo)random numbers from the
    *    uniform distribution and stores them into the array <TT>u</TT>
    *    starting at index <TT>start</TT>.
    *   
    * @param u array that will contain the generated uniforms
    * 
    *    @param start starting index, in the array <TT>u</TT>, to write uniforms from
    * 
    *    @param n number of uniforms to generate
    * 
    * 
    */
   public void nextArrayOfDouble (double[] u, int start, int n);


   /**
    * Returns a (pseudo)random number from the discrete uniform 
    *    distribution over the integers 
    * <SPAN CLASS="MATH">{<I>i</I>, <I>i</I> + 1,..., <I>j</I>}</SPAN>,
    *    using this stream.  (Calls <TT>nextDouble</TT> once.)
    *  
    * @param i smallest integer that can be generated
    * 
    *    @param j greatest integer that can be generated
    * 
    *    @return the generated integer
    * 
    */
   public int nextInt (int i, int j);


   /**
    * Generates <TT>n</TT> (pseudo)random numbers
    *    from the discrete uniform 
    *    distribution over the integers 
    * <SPAN CLASS="MATH">{<I>i</I>, <I>i</I> + 1,..., <I>j</I>}</SPAN>,
    *    using this stream and stores the result in the array <TT>u</TT>
    *    starting at index <TT>start</TT>.  (Calls <TT>nextInt</TT> <TT>n</TT> times.)
    *   
    * @param i smallest integer that can be generated
    * 
    *    @param j greatest integer that can be generated
    * 
    *    @param u array that will contain the generated values
    * 
    *    @param start starting index, in the array <TT>u</TT>, to write integers from
    * 
    *    @param n number of values being generated
    * 
    * 
    */
   public void nextArrayOfInt (int i, int j, int[] u, int start, int n);
 
}

