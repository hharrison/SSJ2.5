


/*
 * Class:        KorobovLatticeSequence
 * Description:  
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


/**
 * This class implements Korobov lattice sequences, defined as follows.
 * One selects a <SPAN  CLASS="textit">basis</SPAN> <SPAN CLASS="MATH"><I>b</I></SPAN> and a (large) multiplier <SPAN CLASS="MATH"><I>a</I></SPAN>.
 * For each integer <SPAN CLASS="MATH"><I>k</I>&nbsp;&gt;=&nbsp; 0</SPAN>, we may consider the
 * <SPAN CLASS="MATH"><I>n</I></SPAN>-point Korobov lattice with modulus <SPAN CLASS="MATH"><I>n</I> = <I>b</I><SUP>k</SUP></SPAN> and multiplier
 * 
 * <SPAN CLASS="MATH">&#227; = <I>a</I> mod <I>n</I></SPAN>.
 * Its points have the form
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = (<I>a</I><SUP>i</SUP>(1, <I>a</I>, <I>a</I><SUP>2</SUP>,&#8230;) mod<I>n</I>)/<I>n</I> = (&#227;<SUP>i</SUP>(1,&#227;,&#227;<SUP>2</SUP>,&#8230;) mod<I>n</I>)/<I>n</I>
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>n</I> - 1</SPAN>.
 * For 
 * <SPAN CLASS="MATH"><I>k</I> = 0, 1,...</SPAN>, we have an increasing sequence of lattices
 * contained in one another.
 * 
 * <P>
 * These embedded lattices contain an infinite sequence of points that 
 * can be enumerated as follows:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = <I>&#968;</I><SUB>b</SUB>(<I>i</I>)(1, <I>a</I>, <I>a</I><SUP>2</SUP>,&#8230;) mod 1.
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN> is the radical inverse function in base <SPAN CLASS="MATH"><I>b</I></SPAN>,
 * defined in {@link RadicalInverse}.
 * The first <SPAN CLASS="MATH"><I>n</I> = <I>b</I><SUP>k</SUP></SPAN> points in this sequence are exactly the same as
 * the <SPAN CLASS="MATH"><I>n</I></SPAN> points in, for each <SPAN CLASS="MATH"><I>k</I>&nbsp;&gt;=&nbsp; 0</SPAN>.
 * 
 */
public class KorobovLatticeSequence extends KorobovLattice  { 
   int base;         // Base for radical inversion
   int inverse;      // global variables for radical inverssion,
   int n;            // since bloody JAVA cannot pass references

   // Method modPower is inherited from Rank1Lattice.



   /**
    * Constructs a new lattice sequence with base <TT>b</TT> and 
    *  <TT>generator</TT> <SPAN CLASS="MATH">= <I>a</I></SPAN>.
    *  
    * @param b number of points (modulus) is a power of b
    * 
    *    @param a multiplier <SPAN CLASS="MATH"><I>a</I></SPAN> of this lattice sequence
    * 
    * 
    */
   public KorobovLatticeSequence (int b, int a)  {
// Pas termine: ne fonctionne pas
      super (2, 3, 1);
      if (a < 1)
         throw new IllegalArgumentException
             ("KorobovLatticeSequence:   Multiplier a must be >= 1");
//      dim       = Integer.MAX_VALUE;
//      numPoints = Integer.MAX_VALUE;
      base = b;
throw new UnsupportedOperationException ("NOT FINISHED");
   } 
 

   // A very inefficient way of generating the points!
   public double getCoordinate (int i, int j) {
      int n;
      int inverse;
      if (i == 0)
         return 0.0;
      else if (j == 0)
         return radicalInverse (base, i);
      else {
         // integerRadicalInverse (i);
         n = 1;
         for (inverse = 0; i > 0; i /= base) {
            inverse = inverse * base + (i % base);
            n *= base;
         }
         return (double) ((inverse * modPower (genA, j, n)) % n) / (double) n;
      }
   }

   // ... has been unrolled in getCoordinate.
   private void integerRadicalInverse (int i) {
      // Attention: returns results in variables n and inverse.
      n = 1;
      for (inverse = 0; i > 0; i /= base) {
         inverse = inverse * base + (i % base);
         n *= base;
      }
   }
 
}
