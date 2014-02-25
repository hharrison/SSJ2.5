

/*
 * Class:        KorobovLattice
 * Description:  Korobov lattice
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


/**
 * This class implements <SPAN  CLASS="textit">Korobov lattices</SPAN>, which represents the same point
 * sets as in class {@link LCGPointSet}, but implemented differently.
 * The parameters are the modulus <SPAN CLASS="MATH"><I>n</I></SPAN> and the multiplier <SPAN CLASS="MATH"><I>a</I></SPAN>, for arbitrary
 * integers 
 * <SPAN CLASS="MATH">1&nbsp;&lt;=&nbsp;<I>a</I> &lt; <I>n</I></SPAN>.
 * The number of points is <SPAN CLASS="MATH"><I>n</I></SPAN>, their dimension is 
 * <SPAN CLASS="MATH"><I>s</I></SPAN>, and they are defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = (<I>i</I>/<I>n</I>)(1, <I>a</I>, <I>a</I><SUP>2</SUP>,&#8230;, <I>a</I><SUP>s-1</SUP>) mod 1
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>n</I> - 1</SPAN>.
 * 
 * <P>
 * It is also possible to build a ``shifted'' Korobov lattice with the first
 * <SPAN CLASS="MATH"><I>t</I></SPAN> coordinates rejected. The <SPAN CLASS="MATH"><I>s</I></SPAN>-dimensionnal points are then defined as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = (<I>i</I>/<I>n</I>)(<I>a</I><SUP>t</SUP>, <I>a</I><SUP>t+1</SUP>, <I>a</I><SUP>t+2</SUP>,&#8230;, <I>a</I><SUP>t+s-1</SUP>) mod 1
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>n</I> - 1</SPAN> and fixed <SPAN CLASS="MATH"><I>t</I></SPAN>.
 * 
 */
public class KorobovLattice extends Rank1Lattice  {
   protected int genA;                        // Multiplier a.

   // Method modPower is inherited from Rank1Lattice.


   /**
    * Instantiates a Korobov lattice point set with modulus <SPAN CLASS="MATH"><I>n</I></SPAN> and 
    *    multiplier <SPAN CLASS="MATH"><I>a</I></SPAN> in dimension <SPAN CLASS="MATH"><I>s</I></SPAN>.
    * 
    */
   public KorobovLattice (int n, int a, int s)  {
      super (n, null, 0);
      if (a < 1 || a >= n) 
         throw new IllegalArgumentException 
            ("KorobovLattice must have 1 <= a < n");
      genA = a;
      dim = s;
      v = new double[s];
      long[] B = new long[dim];
      B[0] = 1;
      v[0] = normFactor;
      for (int j = 1; j < dim; j++) {
         B[j] = (a * B[j - 1]) % n;
         v[j] = normFactor * B[j];
      }
   }


   /**
    * Instantiates a shifted Korobov lattice point set with modulus <SPAN CLASS="MATH"><I>n</I></SPAN> and 
    *    multiplier <SPAN CLASS="MATH"><I>a</I></SPAN> in dimension <SPAN CLASS="MATH"><I>s</I></SPAN>. The first <SPAN CLASS="MATH"><I>t</I></SPAN> coordinates of a
    *    standard Korobov lattice are dropped as described above.
    *    The case <SPAN CLASS="MATH"><I>t</I> = 0</SPAN> corresponds to the standard  Korobov lattice.
    * 
    */
   public KorobovLattice (int n, int a, int s, int t)  {
      super (n, null, 0);
      if (a < 1 || a >= n) 
         throw new IllegalArgumentException 
            ("KorobovLattice must have 1 <= a < n");
      if (t < 1) 
         throw new IllegalArgumentException 
            ("KorobovLattice: must have 0 < t");
      dim = s;
      genA = a;
      v = new double[s];
      long[] B = new long[dim];
      int j;
      B[0] = a;
      for (j = 1; j < t; j++)
         B[0] *= a;
      v[0] = B[0] * normFactor;
      for (j = 1; j < dim; j++) {
         B[j] = (a * B[j - 1]) % n;
         v[j] = normFactor * B[j];
      }
   }


   /**
    * Returns the multiplier <SPAN CLASS="MATH"><I>a</I></SPAN> of the lattice.
    * 
    */
   public int getA()  {
      return genA;
   }



   public String toString() {
      StringBuffer sb = new StringBuffer ("KorobovLattice:" +
                                           PrintfFormat.NEWLINE);
      sb.append ("Multiplier a: " + genA + PrintfFormat.NEWLINE);
      sb.append (super.toString());
      return sb.toString();
   }
}
