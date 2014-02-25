

/*
 * Class:        HammersleyPointSet
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
 * This class implements <SPAN  CLASS="textit">Hammersley point sets</SPAN>, 
 * which are defined as follows.
 * Let 
 * <SPAN CLASS="MATH">2 = <I>b</I><SUB>1</SUB> &lt; <I>b</I><SUB>2</SUB> &lt; <SUP> ... </SUP></SPAN> denote the sequence of all prime 
 * numbers by increasing order.
 * The Hammersley point set with <SPAN CLASS="MATH"><I>n</I></SPAN> points in <SPAN CLASS="MATH"><I>s</I></SPAN> dimensions contains
 * the points
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>u</B><SUB>i</SUB> = (<I>i</I>/<I>n</I>, <I>&#968;</I><SUB>b<SUB>1</SUB></SUB>(<I>i</I>), <I>&#968;</I><SUB>b<SUB>2</SUB></SUB>(<I>i</I>),..., <I>&#968;</I><SUB>b<SUB>s-1</SUB></SUB>(<I>i</I>)),
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>n</I> - 1</SPAN>, where <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB></SPAN> is the radical inverse function
 * in base <SPAN CLASS="MATH"><I>b</I></SPAN>, defined in {@link RadicalInverse}.
 * This class is not a subclass of {@link DigitalNet}, because the basis
 * is not the same for all coordinates.
 * We do obtain a net in a generalized sense if 
 * 
 * <SPAN CLASS="MATH"><I>n</I> = <I>b</I><SUB>1</SUB><SUP>k<SUB>1</SUB></SUP> = <I>b</I><SUB>2</SUB><SUP>k<SUB>2</SUB></SUP> = <SUP> ... </SUP> = <I>b</I><SUB>s-1</SUB><SUP>k<SUB>s-1</SUB></SUP></SPAN>
 * for some integers 
 * <SPAN CLASS="MATH"><I>k</I><SUB>1</SUB>,..., <I>k</I><SUB>s-1</SUB></SPAN>.
 * 
 * <P>
 * The points of a Hammersley point set can be ``scrambled'' by applying a 
 * permutation to the digits of <SPAN CLASS="MATH"><I>i</I></SPAN> before computing each coordinate.  If 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>i</I> = <I>a</I><SUB>0</SUB> + <I>a</I><SUB>1</SUB><I>b</I><SUB>j</SUB> + ... + <I>a</I><SUB>k<SUB>j</SUB>-1</SUB><I>b</I><SUB>j</SUB><SUP>k<SUB>j</SUB>-1</SUP>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN> is a permutation of the digits 
 * <SPAN CLASS="MATH">{0,..., <I>b</I><SUB>j</SUB> -1}</SPAN>, then
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#968;</I><SUB>b<SUB>j</SUB></SUB>(<I>i</I>) = &sum;<SUB>r=0</SUB><SUP>k<SUB>j</SUB>-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUB>j</SUB><SUP>-r-1</SUP>
 * </DIV><P></P>
 * is replaced  by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>u</I><SUB>i, j</SUB> = &sum;<SUB>r=0</SUB><SUP>k<SUB>j</SUB>-1</SUP><I>&#960;</I><SUB>j</SUB>[<I>a</I><SUB>r</SUB>]<I>b</I><SUB>j</SUB><SUP>-r-1</SUP>.
 * </DIV><P></P>
 * The permutations <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN> can be deterministic or random.
 * One (deterministic) possibility implemented here is to use
 * the Faure permutation of 
 * <SPAN CLASS="MATH">{0,..., <I>b</I><SUB>j</SUB>}</SPAN> for <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN>, for each 
 * coordinate <SPAN CLASS="MATH"><I>j</I> &gt; 0</SPAN>.
 * 
 */
public class HammersleyPointSet extends PointSet {
   private int[] base;           // Vector of prime bases.
   private int[][] permutation;  // Digits permutation, for each dimension.
   private boolean permuted;     // Permute digits?



   /**
    * Constructs a new Hammersley point set with <TT>n</TT> points in <TT>dim</TT>
    *    dimensions.
    *  
    * @param n number of points
    * 
    *    @param dim dimension of the point set
    * 
    */
   public HammersleyPointSet (int n, int dim)  {
      if (n < 0 || dim < 1)
         throw new IllegalArgumentException
            ("Hammersley point sets must have positive dimension and n >= 0");
      numPoints = n;
      this.dim  = dim;
      if (dim > 1)
         base = RadicalInverse.getPrimes (dim - 1);
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
    * <SPAN CLASS="MATH"><I>j</I> = 1,..., <I>s</I> - 1</SPAN> and 
    * <SPAN CLASS="MATH"><I>u</I><SUB>i, 0</SUB> = <I>i</I>/<I>n</I></SPAN>,
    *  where <SPAN CLASS="MATH"><I>&#960;</I><SUB>j</SUB></SPAN> is the Faure permutation of 
    * <SPAN CLASS="MATH">{0,..., <I>b</I><SUB>j</SUB> -1}</SPAN>.
    * 
    */
   public void addFaurePermutations() {
      if (dim > 1) {
         permutation = new int[dim][];
         for (int i = 0; i < dim - 1; i++) {
            permutation[i] = new int[base[i]];
            RadicalInverse.getFaurePermutation (base[i], permutation[i]);
         }
      }
      permuted = true;
   }


   /**
    * Erases the Faure permutations: from now on, the digits will not be
    *   Faure permuted.
    * 
    */
   public void ErasePermutations() {
      permuted = false;
      permutation = null;
   }




   public double getCoordinate (int i, int j) {
      if (j == 0)
         return (double) i / (double) numPoints;
      if (permuted)
         return RadicalInverse.permutedRadicalInverse 
                   (base[j - 1], permutation[j - 1], i);
      else 
         return RadicalInverse.radicalInverse (base[j - 1], i);
   }
}

