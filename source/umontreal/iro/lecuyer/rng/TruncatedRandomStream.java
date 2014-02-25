

/*
 * Class:        TruncatedRandomStream
 * Description:  container random stream generating numbers in an interval
                 (a,b) instead of in (0,1)
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

import umontreal.iro.lecuyer.rng.RandomStream;


/**
 * Represents a container random stream generating numbers in an interval 
 * <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN> instead of in <SPAN CLASS="MATH">(0, 1)</SPAN>, where 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>a</I> &lt; <I>b</I>&nbsp;&lt;=&nbsp;1</SPAN>, 
 * by using the contained stream.
 * If <TT>nextDouble</TT> returns <SPAN CLASS="MATH"><I>u</I></SPAN> for the contained stream,
 * it will return 
 * <SPAN CLASS="MATH"><I>v</I> = <I>a</I> + (<I>b</I> - <I>a</I>)<I>u</I></SPAN>, which is uniform over <SPAN CLASS="MATH">(<I>a</I>, <I>b</I>)</SPAN>,
 * for the truncated stream.
 * The method <TT>nextInt</TT> returns the integer that corresponds to <SPAN CLASS="MATH"><I>v</I></SPAN>
 * (by inversion); this integer is no longer uniformly distributed in general.
 * 
 */
public class TruncatedRandomStream implements RandomStream {
   private RandomStream stream;
   private double a;
   private double bminusa;



   public TruncatedRandomStream (RandomStream stream, double a, double b) {
      if (stream == null)
         throw new NullPointerException ("The given stream must not be null");
      if (a >= b)
         throw new IllegalArgumentException ("a must be smaller than b");
      if (a < 0 || b < 0 || a > 1 || b > 1)
         throw new IllegalArgumentException ("a and b must be in [0, 1]");
      this.stream = stream;
      this.a = a;
      bminusa = b - a;
   }

   public void resetStartStream () {
      stream.resetStartStream ();
   }

   public void resetStartSubstream () {
      stream.resetStartSubstream ();
   }

   public void resetNextSubstream () {
      stream.resetNextSubstream ();
   }

   public double nextDouble () {
      double v = stream.nextDouble ();
      return a + v * bminusa;
   }

   public void nextArrayOfDouble (double[] u, int start, int n) {
      stream.nextArrayOfDouble (u, start, n);
      for (int i = start; i < start + n; i++)
         u[i] = a + u[i] * bminusa;
   }

   public int nextInt (int i, int j) {
      return i + (int) (nextDouble () * (j - i + 1));
   }

   public void nextArrayOfInt (int i, int j, int[] u, int start, int n) {
      for (int x = start; x < start + n; x++)
         u[x] = nextInt (i, j);
   }
}
