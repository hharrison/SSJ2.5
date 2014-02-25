

/*
 * Class:        RandomStreamBase
 * Description:  Base class of all random number generators
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

import java.io.Serializable; 

/**
 * This class provides a convenient foundation on which RNGs can
 * be built. It implements all the methods which do not depend directly on
 * the generator itself, but only on its output, which is to be defined
 * by implementing the <TT>abstract</TT> method <TT>nextValue</TT>. In the present 
 * class, all methods returning random numbers directly or indirectly 
 * (<TT>nextDouble</TT>, <TT>nextArrayOfDouble</TT>, <TT>nextInt</TT> and
 *  <TT>nextArrayOfInt</TT>) call <TT>nextValue</TT>.  Thus, to define a subclass 
 * that implements a RNG, it suffices to implement <TT>nextValue</TT>, in addition
 *  to the <TT>reset...</TT> and <TT>toString</TT> methods.
 * Of course, the other methods may also be overridden for improved efficiency.
 * 
 * <P>
 * If the <TT>nextValue</TT> already generates numbers with a precision of
 *  53-bits or higher, then {@link #nextDouble nextDouble} can be overridden to improve 
 * the performance. The mechanism for increasing the precision assumes that
 *  <TT>nextValue</TT> returns at least 29 bits of precision, in which case 
 * the higher precision numbers will have roughly 52 bits of precision.
 * This mechanism was designed primarily for RNGs that return numbers with 
 * around 30 to 32 bits of precision.
 * 
 * <P>
 * {@link RandomStreamBase} and its subclasses are implementing the {@link Serializable} interface.
 * Each class has a serial number wich represent the class version. 
 * For instance <TT>70510</TT> means that the last change was the <TT>10th May 2007</TT>.
 * 
 */
public abstract class RandomStreamBase implements CloneableRandomStream,
                                                  Serializable  {

   private static final long serialVersionUID = 70510L;
   //La date de modification a l'envers, lire 10/05/2007
   
   //constants
   protected static double invtwo24 = 5.9604644775390625e-8;  //2^(-24)
   private static double EPSILON = 5.5511151231257827e-17;    //2^(-54)

   protected String name = null;

   // prec53 keeps track if the precision has been increased or not.
   protected boolean prec53 = false;
   protected boolean anti = false;  // Deprecated.



   public abstract void resetStartStream();
   public abstract void resetStartSubstream();
   public abstract void resetNextSubstream();
   public abstract String toString();


   /**
    * After calling this method with <TT>incp = true</TT>, each call to 
    *   the RNG (direct or indirect) for this stream 
    *   will return a uniform random number with more bits of precision than what
    *   is returned by <TT>nextValue</TT>,
    *   and will advance the state of the stream by 2 steps instead of 1
    *   (i.e., <TT>nextValue</TT> will be called twice for each random number).
    * 
    * <P>
    * More precisely, if <TT>s</TT> is a stream of a subclass of 
    *   <TT>RandomStreamBase</TT>, when the precision has been increased,
    *   the instruction ``<TT>u = s.nextDouble()</TT>'',  is equivalent to
    *   ``<TT>u = (s.nextValue() + s.nextValue()*fact) % 1.0</TT>'' 
    *   where the constant <TT>fact</TT> is equal to <SPAN CLASS="MATH">2<SUP>-24</SUP></SPAN>.
    *   This also applies when calling <TT>nextDouble</TT> indirectly
    *   (e.g., via <TT>nextInt</TT>, etc.).
    *   By default, or if this method is called again with <TT>incp = false</TT>, 
    *   each call to <TT>nextDouble</TT> for this stream advances the state by 1 step
    *   and returns the same number as <TT>nextValue</TT>.  
    * 
    * @param incp if the generator will be set to high precision mode
    * 
    * 
    */
   public void increasedPrecision (boolean incp)  {
      prec53 = incp;
   }


   /**
    * This method should return the next random number (between 0 and 1) 
    *   from the current stream.
    *   If the stream is set to the high precision mode 
    *   (<TT>increasedPrecision(true)</TT> was called), then each call to 
    *   <TT>nextDouble</TT> will call <TT>nextValue</TT> twice, otherwise it will
    *   call it only once. 
    * 
    * @return a number in the interval (0,1)
    * 
    */
   protected abstract double nextValue();


   /**
    * Returns a uniform random number between 0 and 1 from the stream. 
    *   Its behavior depends on the last call to {@link #increasedPrecision increasedPrecision}.
    *   The generators programmed in SSJ never return the values 0 or 1.
    * 
    * @return a number in the interval (0,1)
    * 
    */
   public double nextDouble()  {
      double u = nextValue();
      if (prec53)
         u = (u + nextValue() * invtwo24) % 1.0 + EPSILON;
      if(anti)
         return 1.0 - u;
      else
         return u;
   }


   /**
    * Calls <TT>nextDouble</TT> <TT>n</TT> times to fill the array <TT>u</TT>.
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
      if(u.length == 0)
         throw new NullPointerException("The array must be initialized.");
      if (u.length < n + start)
         throw new IndexOutOfBoundsException("The array is too small.");
      if(start < 0)
         throw new IndexOutOfBoundsException("Must start at a " +
                                             "non-negative index.");
      if(n < 0)
         throw new IllegalArgumentException("Must have a non-negative " +
                                            "number of elements.");

      for(int ii = start; ii < start + n; ii++)
         u[ii] = nextDouble();
   }

  
   /**
    * Calls <TT>nextDouble</TT> once to create one integer between
    *   <TT>i</TT> and <TT>j</TT>. This method always uses the highest order bits
    *   of the random number. It should be overridden if a faster implementation 
    *   exists for the specific generator.
    * 
    * @param i the smallest possible returned integer
    * 
    *   @param j the largest possible returned integer
    * 
    *   @return a random integer between i and j
    * 
    */
   public int nextInt (int i, int j)  {
      if(i > j)
         throw new IllegalArgumentException(i + " is larger than " +
                                            j + ".");
      // This works even for an interval [0, 2^31 - 1]. It would not with 
      // return i + (int)(nextDouble() * (j - i + 1));
      return i + (int)(nextDouble() * (j - i + 1.0));
   } 


   /**
    * Calls <TT>nextInt</TT> <TT>n</TT> times to fill the array <TT>u</TT>.
    *   This method should be overridden if a faster implementation exists for
    *   the specific generator.
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
      if(u == null)
         throw new NullPointerException("The array must be " +
                                        "initialized.");
      if (u.length < n + start)
         throw new IndexOutOfBoundsException("The array is too small.");
      if(start < 0)
         throw new IndexOutOfBoundsException("Must start at a " +
                                             "non-negative index.");
      if(n < 0)
         throw new IllegalArgumentException("Must have a non-negative " +
                                            "number of elements.");

      for(int ii = start; ii < start + n; ii++)
         u[ii] = nextInt(i,j);
   } 


   /**
    * Use the <TT>toString</TT> method.
    * 
    * 
    */
   @Deprecated
   public String formatState()  {
      return toString();
   }


   @Deprecated
   public String formatStateFull() {
      throw new UnsupportedOperationException (
            "   call the toStringFull() method instead.");
   }

   /**
    * Clones the current generator and return its copy.
    *  
    *  @return A deep copy of the current generator
    * 
    */
   public RandomStreamBase clone()  {
      RandomStreamBase retour = null;
      try {
         retour = (RandomStreamBase)super.clone();
      }
      catch(CloneNotSupportedException cnse) {
         cnse.printStackTrace(System.err);
      }
      return retour;
   }
   

}

