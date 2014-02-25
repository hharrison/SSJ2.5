


/*
 * Class:        HaltonSequence
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
 * This class implements the sequence of Halton,
 * which is essentially a modification of Hammersley nets for producing 
 * an infinite sequence of points having low discrepancy.
 * The <SPAN CLASS="MATH"><I>i</I></SPAN>th point in <SPAN CLASS="MATH"><I>s</I></SPAN> dimensions is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="HaltonSequenceimg1.png"
 *  ALT="$\displaystyle \mbox{\boldmath$u$}$"><SUB>i</SUB> = (<I>&#968;</I><SUB>b<SUB>1</SUB></SUB>(<I>i</I>), <I>&#968;</I><SUB>b<SUB>2</SUB></SUB>(<I>i</I>),..., <I>&#968;</I><SUB>b<SUB>s</SUB></SUB>(<I>i</I>)),
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0, 1, 2,...</SPAN>, where <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB></SPAN> is the radical inverse function
 * in base <SPAN CLASS="MATH"><I>b</I></SPAN>, defined in class {@link RadicalInverse}, and where
 * 
 * <SPAN CLASS="MATH">2 = <I>b</I><SUB>1</SUB> &lt; <SUP> ... </SUP> &lt; <I>b</I><SUB>s</SUB></SPAN> are the <SPAN CLASS="MATH"><I>s</I></SPAN> smallest prime numbers in 
 * increasing order.
 * 
 * <P>
 * A fast method is implemented to generate randomized Halton sequences, starting from an arbitrary point <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB></SPAN>.
 * 
 * <P>
 * The points can be ``scrambled'' by applying a permutation to the 
 * digits of <SPAN CLASS="MATH"><I>i</I></SPAN> before computing each coordinate, in the same way as for the class
 * {@link HammersleyPointSet}, for all coordinates <SPAN CLASS="MATH"><I>j</I>&nbsp;&gt;=&nbsp; 0</SPAN>.
 * 
 */
public class HaltonSequence extends PointSet { 
   private int[] base;           // Vector of prime bases.
   private int[][] permutation;  // Digits permutation, for each dimension.
   private boolean permuted;     // Permute digits?
   private RadicalInverse[] radinv; // Vector of RadicalInverse's.
   private int[] start;          // starting indices
   private final static int positiveBitMask = ~Integer.reverse(1);


   /**
    * Constructs a new Halton sequence 
    * in <TT>dim</TT> dimensions.
    * 
    * @param dim dimension
    * 
    */
   public HaltonSequence (int dim)  {
      if (dim < 1)
         throw new IllegalArgumentException
            ("Halton sequence must have positive dimension dim");
      this.dim  = dim;
      numPoints = Integer.MAX_VALUE;
      base = RadicalInverse.getPrimes (dim);
      start = new int[dim];
      java.util.Arrays.fill(start, 0);
   }


   /**
    * Initializes the Halton sequence starting at point <TT>x0</TT>.
    *    For each coordinate <SPAN CLASS="MATH"><I>j</I></SPAN>, the sequence starts at index <SPAN CLASS="MATH"><I>i</I><SUB>j</SUB></SPAN> such that 
    *    <TT>x0[<SPAN CLASS="MATH"><I>j</I></SPAN>]</TT> is the radical inverse of <SPAN CLASS="MATH"><I>i</I><SUB>j</SUB></SPAN>.
    *    The dimension of <TT>x0</TT> must be at least as large as the dimension
    *    of this object.
    *  
    * @param x0 starting point of the Halton sequence
    * 
    * 
    */
   public void setStart (double[] x0)  {
      for (int i = 0; i < dim; i++)
         start[i] = RadicalInverse.radicalInverseInteger(base[i], x0[i]);
   }


   /**
    * Initializes the Halton sequence starting at point <TT>x0</TT>.
    *    The dimension of <TT>x0</TT> must be at least as large as the dimension
    *    of this object.
    * 
    * @param x0 starting point of the Halton sequence
    * 
    * 
    */
   public void init (double[] x0)  {
      radinv = new RadicalInverse[dim];
      for (int i = 0; i < dim; i++)
         radinv[i] = new RadicalInverse (base[i], x0[i]);
   }


   /**
    * Permutes the digits using permutations from for all coordinates.
    * After the method is called, the coordinates <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN> are generated via
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>u</I><SUB>i, j</SUB> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>&#960;</I><SUB>j</SUB>[<I>a</I><SUB>r</SUB>]<I>b</I><SUB>j</SUB><SUP>-r-1</SUP>,
    * </DIV><P></P>
    * for 
    * <SPAN CLASS="MATH"><I>j</I> = 0,..., <I>s</I> - 1</SPAN>,
    *  where <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN> is the Faure-Lemieux (2008) permutation of 
    * <SPAN CLASS="MATH">{0,..., <I>b</I><SUB>j</SUB> -1}</SPAN>.
    * 
    */
   public void addFaureLemieuxPermutations() {
      permutation = new int[dim][];
      for (int i = 0; i < dim; i++) {
         permutation[i] = new int[base[i]];
         RadicalInverse.getFaureLemieuxPermutation (i, permutation[i]);
      }
      permuted = true;
   }



   /**
    * Permutes the digits using Faure permutations for all coordinates.
    *   After the method is called, the coordinates <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN> are generated via
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>u</I><SUB>i, j</SUB> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>&#960;</I><SUB>j</SUB>[<I>a</I><SUB>r</SUB>]<I>b</I><SUB>j</SUB><SUP>-r-1</SUP>,
    * </DIV><P></P>
    * for 
    * <SPAN CLASS="MATH"><I>j</I> = 0,..., <I>s</I> - 1</SPAN>,
    *  where <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN> is the Faure permutation of 
    * <SPAN CLASS="MATH">{0,..., <I>b</I><SUB>j</SUB> -1}</SPAN>.
    * 
    */
   public void addFaurePermutations() {
      permutation = new int[dim][];
      for (int i = 0; i < dim; i++) {
         permutation[i] = new int[base[i]];
         RadicalInverse.getFaurePermutation (base[i], permutation[i]);
      }
      permuted = true;
   }



   /**
    * Erases the permutations: from now on, the digits will not be
    *   permuted.
    * 
    */
   public void ErasePermutations() {
      permuted = false;
      permutation = null;
   }



    
   public int getNumPoints () {
      return Integer.MAX_VALUE;
   }

   public double getCoordinate (int i, int j) {
      if (radinv != null) {
         if (!permuted) {
            return radinv[j].nextRadicalInverse ();
         } else {
            throw new UnsupportedOperationException (
            "Fast radical inverse is not implemented in case of permutation");
         }
      } else {
         int k = start[j] + i;
         // if overflow, restart at first nonzero point
         // (Struckmeier restarts at zero)
         if (k < 0)
            k = (k & positiveBitMask) + 1;
         if (permuted)
            return RadicalInverse.permutedRadicalInverse 
            (base[j], permutation[j], k);
         else 
            return RadicalInverse.radicalInverse (base[j], k);
      }
   }
}

