

/*
 * Class:        AntitheticStream
 * Description:  container class allows the user to force any RandomStream
                 to return antithetic variates
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
 * This container class allows the user to force any {@link RandomStream} to 
 * return antithetic variates.  That is, {@link #nextDouble nextDouble} returns 
 * <SPAN CLASS="MATH">1 - <I>u</I></SPAN> instead of <SPAN CLASS="MATH"><I>u</I></SPAN> and the corresponding change is made in 
 * {@link #nextInt nextInt}.
 * Any instance of this class behaves exactly like a {@link RandomStream},
 * except that it depends on another random number generator stream, called the
 * <SPAN  CLASS="textit">base stream</SPAN>, to generate its numbers. 
 * Any call to one of the <TT>next...</TT>
 * methods of this class will modify the state of the base stream. 
 * 
 */
public class AntitheticStream implements RandomStream  {

   // The base stream.
   private RandomStream st;




   /**
    * Constructs a new antithetic stream, using the random numbers
    *   from the base stream <TT>stream</TT>.
    * 
    */
   public AntitheticStream (RandomStream stream)  {
      this.st = stream;
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
    * Returns a string starting with <TT>"Antithetic of "</TT>
    *   and finishing with the result of the call to the <TT>toString</TT>
    *   method of the generator.
    * 
    */
   public String toString()  {
      return "Antithetic of " + st.toString();
   }


   /**
    * Returns <TT>1.0 - s.nextDouble()</TT> where <TT>s</TT> is the 
    *   base stream.
    * 
    */
   public double nextDouble()  {
      return 1.0 - st.nextDouble();
   }


   /**
    * Returns <TT>j - i - s.nextInt(i, j)</TT> where <TT>s</TT> is the
    *   base stream.
    * 
    */
   public int nextInt (int i, int j)  {
      // pas (j - st.nextInt(0,j-i)), au cas ou le resultat varie.
      return j - i - st.nextInt(i, j);
   }


   /**
    * Calls <TT>nextArrayOfDouble (u, start, n)</TT> for the base stream,
    *   then replaces each <TT>u[i]</TT> by <TT>1.0 - u[i]</TT>.
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
       for (int ii = start; ii < start + n; ii++)
          u[ii] = 1.0 - u[ii];
   }


   /**
    * Calls <TT>nextArrayOfInt (i, j, u, start, n)</TT> for the base stream,
    *   then replaces each <TT>u[i]</TT> by <TT>j - i - u[i]</TT>.
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
       st.nextArrayOfInt (i, j, u, start, n);
       for (int ii = start; ii < start + n; ii++)
          u[ii] = j - i - u[ii];
   }


}

