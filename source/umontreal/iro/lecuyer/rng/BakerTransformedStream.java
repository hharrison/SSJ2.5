

/*
 * Class:        BakerTransformedStream
 * Description:  container class permits one to apply the baker's 
                 transformation to the output of any RandomStream
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

/**
 * This container class permits one to apply the baker's transformation to
 * the output of any {@link RandomStream}.  
 * It transforms each 
 * <SPAN CLASS="MATH"><I>u</I>&#8712;[0, 1]</SPAN> into <SPAN CLASS="MATH">2<I>u</I></SPAN> if <SPAN CLASS="MATH"><I>u</I>&nbsp;&lt;=&nbsp;1/2</SPAN> and <SPAN CLASS="MATH">2(1 - <I>u</I>)</SPAN>
 * if <SPAN CLASS="MATH"><I>u</I> &gt; 1/2</SPAN>.
 * The {@link #nextDouble nextDouble} method will return the result of this transformation
 * and the other <TT>next...</TT> methods are affected accordingly.
 * Any instance of this class contains a {@link RandomStream} called its
 * <SPAN  CLASS="textit">base stream</SPAN>, used to generate its numbers and to which the
 * transformation is applied. 
 * Any call to one of the <TT>next...</TT>
 * methods of this class will modify the state of the base stream. 
 * 
 * <P>
 * The baker transformation is often applied when the {@link RandomStream}
 * is actually an iterator over a point set used for quasi-Monte Carlo
 * integration (see the <TT>hups</TT> package).
 * 
 */
public class BakerTransformedStream implements RandomStream  {

   // The base stream.
   private RandomStream st;




   /**
    * Constructs a new baker transformed stream, using the random
    *   numbers from the base stream <TT>stream</TT>.
    * 
    */
   public BakerTransformedStream (RandomStream stream)  {
      st = stream;
   } 

   public void resetStartStream() {
      st.resetStartStream();
   }

   public void resetStartSubstream() {
      st.resetStartSubstream();
   }

   public void resetNextSubstream() {
      st.resetNextSubstream();
   }

   /**
    * Returns a string starting with <TT>"Baker transformation of "</TT>
    *   and finishing with the result of the call to the <TT>toString</TT>
    *   method of the generator.
    * 
    */
   public String toString()  {
      return "Baker transformation of " + st.toString();
   }


   /**
    * Returns the baker transformation of <TT>s.nextDouble()</TT> 
    *   where <TT>s</TT> is the base stream.
    * 
    */
   public double nextDouble()  {
      double u = st.nextDouble();
      if (u > 0.5) return 2.0 * (1.0 - u);
      else return u + u;
   }


   /**
    * Generates a random integer in 
    * <SPAN CLASS="MATH">{<I>i</I>,..., <I>j</I>}</SPAN> via
    *  {@link #nextDouble nextDouble} (in which the baker transformation is applied).
    * 
    */
   public int nextInt (int i, int j)  {
      return i + (int)(nextDouble() * (j - i + 1.0));
   }


   /**
    * Calls <TT>nextArrayOfDouble (u, start, n)</TT> for the base stream,
    *   then applies the baker transformation.
    * 
    * @param u the array in which the numbers will be stored
    * 
    *   @param start the first index of <TT>u</TT> to be used
    * 
    *   @param n the number of random numbers to put in <TT>u</TT>
    * 
    * 
    */
   public void nextArrayOfDouble (double[] u, int start, int n)  {
       st.nextArrayOfDouble (u, start, n);
       for (int i = start; i < start + n; i++)
          if (u[i] > 0.5) u[i] = 2.0 * (1.0 - u[i]);
          else u[i] += u[i];
       }


   /**
    * Fills up the array by calling <TT>nextInt (i, j)</TT>.
    * 
    * @param i the smallest possible integer to put in <TT>u</TT>
    * 
    *   @param j the largest possible integer to put in <TT>u</TT>
    * 
    *   @param u the array in which the numbers will be stored
    * 
    *   @param start the first index of <TT>u</TT> to be used
    * 
    *   @param n the number of random numbers to put in <TT>u</TT>
    * 
    * 
    */
   public void nextArrayOfInt (int i, int j, int[] u, int start, int n)  {
      for(int ii = start; ii < start + n; ii++)
         u[ii] = nextInt(i,j);
   }


}

