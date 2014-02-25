

/*
 * Class:        Rank1Lattice
 * Description:  Rank-1 lattice
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
import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.rng.RandomStream;


/**
 * This class implements point sets defined by integration
 * lattices of rank 1, defined as follows.
 * One selects an arbitrary positive integer <SPAN CLASS="MATH"><I>n</I></SPAN> and a <SPAN CLASS="MATH"><I>s</I></SPAN>-dimensional
 * integer vector 
 * <SPAN CLASS="MATH">(<I>a</I><SUB>0</SUB>,..., <I>a</I><SUB>s-1</SUB>)</SPAN>, where 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>a</I><SUB>j</SUB> &lt; <I>n</I></SPAN> for each <SPAN CLASS="MATH"><I>j</I></SPAN>.
 * Usually, <SPAN CLASS="MATH"><I>a</I><SUB>0</SUB> = 1</SPAN>.  The points are defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = (<I>i</I>/<I>n</I>)(<I>a</I><SUB>0</SUB>, <I>a</I><SUB>1</SUB>,&#8230;, <I>a</I><SUB>s-1</SUB>) mod 1
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>n</I> - 1</SPAN>.
 * These <SPAN CLASS="MATH"><I>n</I></SPAN> points are distinct provided that <SPAN CLASS="MATH"><I>n</I></SPAN> and the <SPAN CLASS="MATH"><I>a</I><SUB>j</SUB></SPAN>'s have
 * no common factor.
 * 
 */
public class Rank1Lattice extends PointSet  {

   protected int[] genAs;          // Lattice generator:  a[i]
   protected double[] v;           // Lattice vector:  v[i] = a[i]/n
   protected double normFactor;    // 1/n.
   protected double[] shift;       // Random shift, initially null.




   /**
    * Instantiates a {@link Rank1Lattice} with <SPAN CLASS="MATH"><I>n</I></SPAN> points and lattice
    *    vector <SPAN CLASS="MATH"><I>a</I></SPAN> of dimension <SPAN CLASS="MATH"><I>s</I></SPAN>.
    *  
    * @param n there are n points
    * 
    *    @param a the lattice vector
    * 
    *    @param s dimension of the lattice vector a
    * 
    */
   public Rank1Lattice (int n, int[] a, int s)  {
      dim = s;
      numPoints = n;
      normFactor = 1.0 / (double) n;
      v = new double[s];
      genAs = new int[s];
      for (int j = 0; j < s; j++) {
         if (a[j] < 0 || a[j] >= n)
            throw new IllegalArgumentException
               ("Rank1Lattice must have 0 <= a[j] < n");
         v[j] = normFactor * a[j];
         genAs[j] = a[j];
      }
   }


   /**
    * Returns the generator <SPAN CLASS="MATH"><I>a</I><SUB>j</SUB></SPAN> of the lattice. Its components
    *   are returned as <TT>a[<SPAN CLASS="MATH"><I>j</I></SPAN>]</TT>, for 
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;,(<I>s</I> - 1)</SPAN>.
    * 
    */
   public int[] getAs()  {
      return genAs;
   }


   /**
    * Adds a random shift to all the points of the point set,
    *   using stream <TT>stream</TT> to generate the random numbers.
    *   For each coordinate <SPAN CLASS="MATH"><I>j</I></SPAN> from <TT>d1</TT> to <TT>d2-1</TT>,
    *   the shift <SPAN CLASS="MATH"><I>d</I><SUB>j</SUB></SPAN> is generated uniformly over <SPAN CLASS="MATH">[0, 1)</SPAN> and added modulo <SPAN CLASS="MATH">1</SPAN> to
    *   all the coordinates of all the points.
    * 
    * @param d1 lower dimension of shift
    * 
    *    @param d2 upper dimension of shift is d2 - 1
    * 
    *    @param stream random number stream used to generate uniforms
    * 
    * 
    */
   public void addRandomShift (int d1, int d2, RandomStream stream)  {
      if (null == stream)
         throw new IllegalArgumentException (
              PrintfFormat.NEWLINE +
                  "   Calling addRandomShift with null stream");
      if (0 == d2)
         d2 = Math.max (1, dim);
      if (shift == null) {
         shift = new double[d2];
         capacityShift = d2;
      } else if (d2 > capacityShift) {
         int d3 = Math.max (4, capacityShift);
         while (d2 > d3)
            d3 *= 2;
         double[] temp = new double[d3];
         capacityShift = d3;
         for (int i = 0; i < d1; i++)
            temp[i] = shift[i];
         shift = temp;
      }
      dimShift = d2;
      for (int i = d1; i < d2; i++)
         shift[i] = stream.nextDouble ();
      shiftStream = stream;
   }


   /**
    * Clears the random shift.
    * 
    */
   public void clearRandomShift()  {
      super.clearRandomShift();
      shift = null;
   }

 

   public String toString() {
      StringBuffer sb = new StringBuffer ("Rank1Lattice:" +
                                           PrintfFormat.NEWLINE);
      sb.append (super.toString());
      return sb.toString();
   }


   public double getCoordinate (int i, int j) {
      double x = (v[j] * i) % 1.0;
      if (shift != null) {
         if (j >= dimShift)   // Extend the shift.
            addRandomShift (dimShift, j + 1, shiftStream);
         x += shift[j];
         if (x >= 1.0)
            x -= 1.0;
         if (x <= 0.0)
            x = EpsilonHalf;  // avoid x = 0
       }
      return x;
   }


   // Recursive method that computes a^e mod m.
   protected long modPower (long a, int e, int m) {
      // If parameters a and m == numPoints could be omitted, then
      // the routine would run much faster due to reduced stack usage.
      // Note that a can be larger than m, e.g. in lattice sequences !

      if (e == 0)
         return 1;
      else if (e == 1)
         return a % m;
      else if ((e & 1) == 1)
         return (a * modPower(a, e - 1, m)) % m;
      else {
         long p = modPower(a, e / 2, m);
         return (p * p) % m;
      }
   }

   protected double radicalInverse (int base, int i) {
      double digit, radical, inverse;
      digit = radical = 1.0 / (double) base;
      for (inverse = 0.0; i > 0; i /= base) {
         inverse += digit * (double) (i % base);
         digit *= radical;
      }
      return inverse;
   }

   public PointSetIterator iterator() {
      return new Rank1LatticeIterator();
   }

// ************************************************************************

   protected class Rank1LatticeIterator extends PointSet.DefaultPointSetIterator
   {
      public double nextCoordinate() {
         // I tried with long's and with double's. The double version is
         // 4.5 times faster than the long version.
         if (curPointIndex >= numPoints || curCoordIndex >= dim)
            outOfBounds();
//      return (curPointIndex * v[curCoordIndex++]) % 1.0;
         double x = (curPointIndex * v[curCoordIndex]) % 1.0;
         if (shift != null) {
             if (curCoordIndex >= dimShift)   // Extend the shift.
                addRandomShift (dimShift, curCoordIndex + 1, shiftStream);
             x += shift[curCoordIndex];
             if (x >= 1.0)
                x -= 1.0;
             if (x <= 0.0)
                x = EpsilonHalf;  // avoid x = 0
         }
         curCoordIndex++;
         return x;
      }
   }
}
