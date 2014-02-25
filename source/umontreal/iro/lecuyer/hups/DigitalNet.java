

/*
 * Class:        DigitalNet
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

import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.rng.*;


/**
 * This class provides the basic structures for storing and
 * manipulating <SPAN  CLASS="textit">linear digital nets in base <SPAN CLASS="MATH"><I>b</I></SPAN></SPAN>, for an arbitrary
 * base <SPAN CLASS="MATH"><I>b</I>&nbsp;&gt;=&nbsp;2</SPAN>.  We recall that a net contains <SPAN CLASS="MATH"><I>n</I> = <I>b</I><SUP>k</SUP></SPAN> points in
 * <SPAN CLASS="MATH"><I>s</I></SPAN> dimensions, where the <SPAN CLASS="MATH"><I>i</I></SPAN>th point 
 * <SPAN CLASS="MATH"><B>u</B><SUB>i</SUB></SPAN>,
 * for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>b</I><SUP>k</SUP> - 1</SPAN>, is defined as follows:
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>i</I></TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&nbsp;&nbsp;&nbsp;&nbsp; = &nbsp;&nbsp;&nbsp;&nbsp;</TD>
 * <TD ALIGN="LEFT" NOWRAP>&sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>i, r</SUB><I>b</I><SUP>r</SUP>,</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT">(<I>u</I><SUB>i, j, 1</SUB>&nbsp;<I>u</I><SUB>i, j, 2</SUB>&nbsp;&#8230;)<SUP>T</SUP></TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&nbsp;&nbsp;&nbsp;&nbsp; = &nbsp;&nbsp;&nbsp;&nbsp;</TD>
 * <TD ALIGN="LEFT" NOWRAP><B>C</B><SUB>j</SUB>&nbsp;(<I>a</I><SUB>i, 0</SUB>&nbsp;<I>a</I><SUB>i, 1</SUB>&nbsp;&#8230;&nbsp;<I>a</I><SUB>i, k-1</SUB>)<SUP>T</SUP>,</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>u</I><SUB>i, j</SUB></TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&nbsp;&nbsp;&nbsp;&nbsp; = &nbsp;&nbsp;&nbsp;&nbsp;</TD>
 * <TD ALIGN="LEFT" NOWRAP>&sum;<SUB>r=1</SUB><SUP>&#8734;</SUP><I>u</I><SUB>i, j, r</SUB><I>b</I><SUP>-r</SUP>,</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><B>u</B><SUB>i</SUB></TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&nbsp;&nbsp;&nbsp;&nbsp; = &nbsp;&nbsp;&nbsp;&nbsp;</TD>
 * <TD ALIGN="LEFT" NOWRAP>(<I>u</I><SUB>i, 0</SUB>,&nbsp;&#8230;,&nbsp;<I>u</I><SUB>i, s-1</SUB>).</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 * 
 * In our implementation, the matrices 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> are <SPAN CLASS="MATH"><I>r</I>&#215;<I>k</I></SPAN>,
 * so the expansion of <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN> is truncated to its first <SPAN CLASS="MATH"><I>r</I></SPAN> terms.
 * The points are stored implicitly by storing the generator matrices
 * 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> in a large two-dimensional array of integers,
 *  with <SPAN CLASS="MATH"><I>srk</I></SPAN> elements.
 * 
 * <P>
 * The points 
 * <SPAN CLASS="MATH"><B>u</B><SUB>i</SUB></SPAN> are enumerated using the Gray code
 * technique.
 * With this technique, the <SPAN CLASS="MATH"><I>b</I></SPAN>-ary representation of <SPAN CLASS="MATH"><I>i</I></SPAN>,
 * 
 * <SPAN CLASS="MATH"><B>a</B><SUB>i</SUB> = (<I>a</I><SUB>i, 0</SUB>,..., <I>a</I><SUB>i, k-1</SUB>)</SPAN>, is replaced  by a Gray code representation of <SPAN CLASS="MATH"><I>i</I></SPAN>,
 * 
 * <SPAN CLASS="MATH"><B>g</B><SUB>i</SUB> = (<I>g</I><SUB>i, 0</SUB>,..., <I>g</I><SUB>i, k-1</SUB>)</SPAN>. The Gray code 
 * <SPAN CLASS="MATH"><B>g</B><SUB>i</SUB></SPAN>
 *  used here is defined by 
 * <SPAN CLASS="MATH"><I>g</I><SUB>i, k-1</SUB> = <I>a</I><SUB>i, k-1</SUB></SPAN> and
 * 
 * 
 * <SPAN CLASS="MATH"><I>g</I><SUB>i, <I>&#957;</I></SUB> = (<I>a</I><SUB>i, <I>&#957;</I></SUB> - <I>a</I><SUB>i, <I>&#957;</I>+1</SUB>)mod&nbsp;<I>b</I></SPAN> for 
 * <SPAN CLASS="MATH"><I>&#957;</I> = 0,..., <I>k</I> - 2</SPAN>.
 * It has the property that
 * 
 * <SPAN CLASS="MATH"><B>g</B><SUB>i</SUB> = (<I>g</I><SUB>i, 0</SUB>,..., <I>g</I><SUB>i, k-1</SUB>)</SPAN> and
 * 
 * <SPAN CLASS="MATH"><B>g</B><SUB>i+1</SUB> = (<I>g</I><SUB>i+1, 0</SUB>,..., <I>g</I><SUB>i+1, k-1</SUB>)</SPAN>
 * differ only in the position of the smallest index
 * <SPAN CLASS="MATH"><I>&#957;</I></SPAN> such that
 * 
 * <SPAN CLASS="MATH"><I>a</I><SUB>i, <I>&#957;</I></SUB> &lt; <I>b</I> - 1</SPAN>, and we have 
 * <SPAN CLASS="MATH"><I>g</I><SUB>i+1, <I>&#957;</I></SUB> = (<I>g</I><SUB>i, <I>&#957;</I></SUB> +1)mod&nbsp;<I>b</I></SPAN>
 * in that position.
 * 
 * <P>
 * This Gray code representation permits a more efficient enumeration
 * of the points by the iterators.
 * It changes the order in which the points 
 * <SPAN CLASS="MATH"><B>u</B><SUB>i</SUB></SPAN> are enumerated,
 * but the first <SPAN CLASS="MATH"><I>b</I><SUP>m</SUP></SPAN> points remain the same for every integer <SPAN CLASS="MATH"><I>m</I></SPAN>.
 * The <SPAN CLASS="MATH"><I>i</I></SPAN>th point of the sequence with the Gray enumeration
 * is the <SPAN CLASS="MATH"><I>i'</I></SPAN>th point of the original enumeration, where <SPAN CLASS="MATH"><I>i'</I></SPAN> is the
 * integer whose <SPAN CLASS="MATH"><I>b</I></SPAN>-ary representation 
 * <SPAN CLASS="MATH"><B>a</B><SUB>i'</SUB></SPAN> is given by the Gray
 * code 
 * <SPAN CLASS="MATH"><B>g</B><SUB>i</SUB></SPAN>.
 * To enumerate all the points successively, we never need to compute
 * the Gray codes explicitly.
 * It suffices to know the position <SPAN CLASS="MATH"><I>&#957;</I></SPAN> of the Gray code digit
 * that changes at each step, and this can be found quickly from the
 * <SPAN CLASS="MATH"><I>b</I></SPAN>-ary representation 
 * <SPAN CLASS="MATH"><B>a</B><SUB>i</SUB></SPAN>.
 * The digits of each coordinate <SPAN CLASS="MATH"><I>j</I></SPAN> of the current point can be updated by
 * adding column <SPAN CLASS="MATH"><I>&#957;</I></SPAN> of the generator matrix 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> to the old digits,
 * modulo <SPAN CLASS="MATH"><I>b</I></SPAN>.
 * 
 * <P>
 * One should avoid using the method {@link #getCoordinate getCoordinate}(i, j)
 * for arbitrary values of <TT>i</TT> and <TT>j</TT>, because this is much
 * slower than using an iterator to access successive coordinates.
 * 
 * <P>
 * Digital nets can be randomized in various ways.
 * Several types of randomizations specialized for nets are implemented
 * directly in this class.
 * 
 * <P>
 * A simple but important randomization is the <SPAN  CLASS="textit">random digital shift</SPAN>
 * in base <SPAN CLASS="MATH"><I>b</I></SPAN>, defined as follows: replace each digit 
 * <SPAN CLASS="MATH"><I>u</I><SUB>i, j, <I>&#957;</I></SUB></SPAN> in the third equation above
 *  by 
 * <SPAN CLASS="MATH">(<I>u</I><SUB>i, j, <I>&#957;</I></SUB> + <I>d</I><SUB>j, <I>&#957;</I></SUB>)mod&nbsp;<I>b</I></SPAN>,
 * where the <SPAN CLASS="MATH"><I>d</I><SUB>j, <I>&#957;</I></SUB></SPAN>'s are i.i.d. uniform over 
 * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}</SPAN>.
 * This is equivalent to applying a single random shift to all the points
 * in a formal series representation of their coordinates.
 * In practice, the digital shift is truncated to <SPAN CLASS="MATH"><I>w</I></SPAN> digits,
 * for some integer <SPAN CLASS="MATH"><I>w</I>&nbsp;&gt;=&nbsp;<I>r</I></SPAN>.
 * Applying a digital shift does not change the equidistribution
 * and <SPAN CLASS="MATH">(<I>t</I>, <I>m</I>, <I>s</I>)</SPAN>-net properties of a point set.
 * Moreover, with the random shift, each point is uniformly distributed over
 * the unit hypercube (but the points are not independent, of course).
 * 
 * <P>
 * A second class of randomizations specialized for digital nets
 * are the <SPAN  CLASS="textit">linear matrix scrambles</SPAN>, which multiply the matrices
 *  
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>
 * by a random invertible matrix 
 * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN>, modulo <SPAN CLASS="MATH"><I>b</I></SPAN>.
 * There are several variants, depending on how 
 * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> is generated, and on
 * whether 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> is multiplied on the left or on the right.
 * In our implementation, the linear matrix scrambles are incorporated directly
 * into the matrices 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>, so they
 *  do not slow down  the enumeration of points.
 * Methods are available for applying
 * linear matrix scrambles and for removing these randomizations.
 * These methods generate the appropriate random numbers and make
 * the corresponding changes to the 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>'s.
 * A copy of the original 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>'s is maintained, so the point
 * set can be returned to its original unscrambled state at any time.
 * When a new linear matrix scramble is applied, it is always applied to
 * the <SPAN  CLASS="textit">original</SPAN> generator matrices.
 * The method {@link #resetGeneratorMatrices resetGeneratorMatrices} removes the current matrix
 * scramble by resetting the generator matrices to their original state.
 * On the other hand, the method {@link #eraseOriginalGeneratorMatrices eraseOriginalGeneratorMatrices}
 * replaces the original generator matrices by the current
 * ones, making the changes permanent.
 * This is useful if one wishes to apply two or more linear matrix
 * scrambles on top of each other.
 * 
 * <P>
 * Linear matrix scrambles are usually combined with a random digital shift;
 * this combination is called an <SPAN  CLASS="textit">affine matrix scramble</SPAN>.
 * These two randomizations are applied via separate methods.
 * The linear matrix scrambles are incorporated into the matrices 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>
 * whereas the digital random shift is stored and applied separately,
 * independently of the other scramblings.
 * 
 * <P>
 * Applying a digital shift or a linear matrix scramble to a digital net
 * invalidates all iterators for that randomized point,
 * because each iterator uses a
 * <SPAN  CLASS="textit">cached</SPAN> copy of the current point, which is updated only when
 * the current point index of that iterator changes, and the update also
 * depends on the cached copy of the previous point.
 * After applying any kind of scrambling, the iterators must be
 * reinitialized to the <SPAN  CLASS="textit">initial point</SPAN> by invoking
 * {@link PointSetIterator#resetCurPointIndex resetCurPointIndex}
 * or reinstantiated by the {@link #iterator iterator} method
 * (this is not done automatically).
 * 
 */
public class DigitalNet extends PointSet  {

   // Variables to be initialized by the constructor subclasses.
   protected int b = 0;         // Base.
   protected int numCols = 0;   // The number of columns in each C_j. (= k)
   protected int numRows = 0;   // The number of rows in each C_j. (= r)
   protected int outDigits = 0; // Number of output digits (= w)
   private int[][] originalMat; // Original gen. matrices without randomizat.
   protected int[][] genMat;    // The current generator matrices.
                                // genMat[j*numCols+c][l] contains column c
                                // and row l of C_j.
   protected int[][] digitalShift; // The digital shift, initially zero (null).
                                // Entry [j][l] is for dimension j, digit l,
                                // for 0 <= l < outDigits.
   protected double normFactor; // To convert output to (0,1); 1/b^outDigits.
   protected double[] factor;   // Lookup table in ascending order: factor[i]
                                // = 1/b^{i+1} for 0 <= i < outDigits.

   // primes gives the first index in array FaureFactor
   // for the prime p. If primes[i] = p, then
   // FaureFactor[p][j] contains the Faure ordered factors of base p.
   private int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
      43, 47, 53, 59, 61, 67};

   // Factors on the diagonal corresponding to base b = prime[i] ordered by
   //  increasing Bounds.
   private int[][] FaureFactor = {{1}, {1, 2}, {2, 3, 1, 4},
      { 2, 3, 4, 5, 1, 6}, {3, 4, 7, 8, 2, 5, 6, 9, 1, 10},
      { 5, 8, 3, 4, 9, 10, 2, 6, 7, 11, 1, 12},
      { 5, 7, 10, 12, 3, 6, 11, 14, 4, 13, 2, 8, 9, 15, 1, 16},
      { 7, 8, 11, 12, 4, 5, 14, 15, 3, 6, 13, 16, 2, 9, 10, 17, 1, 18},
      { 5, 9, 14, 18, 7, 10, 13, 16, 4, 6, 17, 19, 3, 8, 15, 20, 2, 11, 12,
        21, 1, 22},
      { 8, 11, 18, 21, 12, 17, 9, 13, 16, 20, 5, 6, 23, 24, 4, 7, 22, 25, 3,
        10, 19, 26, 2, 14, 15, 27, 1, 28},
      { 12, 13, 18, 19, 11, 14, 17, 20, 7, 9, 22, 24, 4, 8, 23, 27, 5, 6, 25,
        26, 3, 10, 21, 28, 2, 15, 16, 29, 1, 30},
      { 8, 14, 23, 29, 10, 11, 26, 27, 13, 17, 20, 24, 7, 16, 21, 30, 5, 15,
        22, 32, 6, 31, 4, 9, 28, 33, 3, 12, 25, 34, 2, 18, 19, 35, 1, 36},
      { 16, 18, 23, 25, 11, 15, 26, 30, 12, 17, 24, 29, 9, 32, 13, 19, 22,
        28, 6, 7, 34, 35, 5, 8, 33, 36, 4, 10, 31, 37, 3, 14, 27, 38, 2, 20,
        21, 39, 1, 40},
      { 12, 18, 25, 31, 9, 19, 24, 34, 8, 16, 27, 35, 10, 13, 30, 33, 15, 20,
        23, 28, 5, 17, 26, 38, 6, 7, 36, 37, 4, 11, 32, 39, 3, 14, 29, 40, 2,
        21, 22, 41, 1, 42},
      { 13, 18, 29, 34, 11, 17, 30, 36, 10, 14, 33, 37, 7, 20, 27, 40, 9, 21,
        26, 38, 15, 22, 25, 32, 6, 8, 39, 41, 5, 19, 28, 42, 4, 12, 35, 43,
        3, 16, 31, 44, 2, 23, 24, 45, 1, 46},
      { 14, 19, 34, 39, 23, 30, 12, 22, 31, 41, 8, 11, 20, 24, 29, 33, 42, 45,
        10, 16, 37, 43, 7, 15, 38, 46, 17, 25, 28, 36, 5, 21, 32, 48, 6, 9,
        44, 47, 4, 13, 40, 49, 3, 18, 35, 50, 2, 26, 27, 51, 1, 52},
      { 25, 26, 33, 34, 18, 23, 36, 41, 14, 21, 38, 45, 24, 27, 32, 35, 11,
        16, 43, 48, 9, 13, 46, 50, 8, 22, 37, 51, 7, 17, 42, 52, 19, 28, 31,
        40, 6, 10, 49, 53, 5, 12, 47, 54, 4, 15, 44, 55, 3, 20, 39, 56, 2,
        29, 30, 57, 1, 58},
      { 22, 25, 36, 39, 17, 18, 43, 44, 24, 28, 33, 37, 13, 14, 47, 48, 16,
        19, 42, 45, 9, 27, 34, 52, 8, 23, 38, 53, 11, 50, 7, 26, 35, 54, 21,
        29, 32, 40, 6, 10, 51, 55, 5, 12, 49, 56, 4, 15, 46, 57, 3, 20, 41,
        58, 2, 30, 31, 59, 1, 60},
      { 18, 26, 41, 49, 14, 24, 43, 53, 12, 28, 39, 55, 29, 30, 37, 38, 10,
        20, 47, 57, 16, 21, 46, 51, 8, 25, 42, 59, 13, 31, 36, 54, 9, 15,
        52, 58, 7, 19, 48, 60, 23, 32, 35, 44, 5, 27, 40, 62, 6, 11, 56,
        61, 4, 17, 50, 63, 3, 22, 45, 64, 2, 33, 34, 65, 1, 66}
};


   public double getCoordinate (int i, int j) {
      // convert i to Gray representation, put digits in gdigit[].
      int l, c, sum;
      int[] bdigit = new int[numCols];
      int[] gdigit = new int[numCols];
      int idigits = intToDigitsGray (b, i, numCols, bdigit, gdigit);
      double result = 0;
      if (digitalShift != null && dimShift < j)
         addRandomShift (dimShift, j, shiftStream);
      for (l = 0; l < outDigits; l++) {
         if (digitalShift == null)
            sum = 0;
         else
            sum = digitalShift[j][l];
         if (l < numRows)
            for (c = 0; c < idigits; c++)
               sum += genMat[j*numCols+c][l] * gdigit[c];
         result += (sum % b) * factor[l];
      }
      if (digitalShift != null)
         result += EpsilonHalf;
      return result;
   }

   public PointSetIterator iterator() {
      return new DigitalNetIterator();
   }


   /**
    * Empty constructor.
    * 
    */
   public DigitalNet () {
     }


   /**
    * Returns <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN>, the coordinate <SPAN CLASS="MATH"><I>j</I></SPAN> of point <SPAN CLASS="MATH"><I>i</I></SPAN>, the points
    *    being enumerated in the standard order (no Gray code).
    *  
    * @param i point index
    * 
    *    @param j coordinate index
    * 
    *    @return the value of <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN>
    * 
    */
   public double getCoordinateNoGray (int i, int j) {
      // convert i to b-ary representation, put digits in bdigit[].
      int l, c, sum;
      int[] bdigit = new int[numCols];
      int idigits = 0;
      for (c = 0; i > 0; c++) {
         idigits++;
         bdigit[c] = i % b;
         i = i / b;
      }
      if (digitalShift != null && dimShift < j)
         addRandomShift (dimShift, j, shiftStream);
      double result = 0;
      for (l = 0; l < outDigits; l++) {
         if (digitalShift == null)
            sum = 0;
         else
            sum = digitalShift[j][l];
         if (l < numRows)
            for (c = 0; c < idigits; c++)
               sum += genMat[j*numCols+c][l] * bdigit[c];
         result += (sum % b) * factor[l];
      }
      if (digitalShift != null)
         result += EpsilonHalf;
      return result;
   }


   /**
    * This iterator does not use the Gray code. Thus the points are enumerated
    *   in the order of their first coordinate before randomization.
    * 
    */
   public PointSetIterator iteratorNoGray() {
      return new DigitalNetIteratorNoGray();
   }


   /**
    * Adds a random digital shift to all the points of the point set,
    *   using stream <TT>stream</TT> to generate the random numbers.
    *   For each coordinate <SPAN CLASS="MATH"><I>j</I></SPAN> from <TT>d1</TT> to <TT>d2-1</TT>,
    *   the shift vector 
    * <SPAN CLASS="MATH">(<I>d</I><SUB>j, 0</SUB>,..., <I>d</I><SUB>j, k-1</SUB>)</SPAN>
    *   is generated uniformly over 
    * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}<SUP>k</SUP></SPAN> and added modulo <SPAN CLASS="MATH"><I>b</I></SPAN> to
    *   the digits of all the points.
    *   After adding a digital shift, all iterators must be reconstructed or
    *   reset to zero.
    * 
    * @param stream random number stream used to generate uniforms
    * 
    * 
    */
   public void addRandomShift (int d1, int d2, RandomStream stream) {
      if (null == stream)
         throw new IllegalArgumentException (
              PrintfFormat.NEWLINE +
              "   Calling addRandomShift with null stream");
      if (0 == d2)
         d2 = Math.max (1, dim);
      if (digitalShift == null) {
         digitalShift = new int[d2][outDigits];
         capacityShift = d2;
      } else if (d2 > capacityShift) {
         int d3 = Math.max (4, capacityShift);
         while (d2 > d3)
            d3 *= 2;
         int[][] temp = new int[d3][outDigits];
         capacityShift = d3;
         for (int i = 0; i < d1; i++)
            for (int j = 0; j < outDigits; j++)
               temp[i][j] = digitalShift[i][j];
         digitalShift = temp;
      }
      for (int i = d1; i < d2; i++)
         for (int j = 0; j < outDigits; j++)
            digitalShift[i][j] = stream.nextInt (0, b - 1);
      dimShift = d2;
      shiftStream = stream;
     }


   /**
    * Same as {@link #addRandomShift addRandomShift}<TT>(0, dim, stream)</TT>,
    *   where <TT>dim</TT> is the dimension of the digital net.
    * 
    * @param stream random number stream used to generate uniforms
    * 
    * 
    */
   public void addRandomShift (RandomStream stream) {
      addRandomShift (0, dim, stream);
     }


   public void clearRandomShift() {
      super.clearRandomShift();
      digitalShift = null;
   }


   public String toString() {
      StringBuffer sb = new StringBuffer (100);
      if (b > 0) {
         sb.append ("Base = ");   sb.append (b);
         sb.append (PrintfFormat.NEWLINE);
      }
      sb.append ("Num cols = ");   sb.append (numCols);
      sb.append (PrintfFormat.NEWLINE + "Num rows = ");
      sb.append (numRows);
      sb.append (PrintfFormat.NEWLINE + "outDigits = ");
      sb.append (outDigits);
      return sb.toString();
   }


   // Print matrices M for dimensions 0 to N-1.
   private void printMat (int N, int[][][] A, String name) {
      for (int i = 0; i < N; i++) {
         System.out.println ("-------------------------------------" +
            PrintfFormat.NEWLINE + name + "   dim = " + i);
         int l, c;   // row l, column c, dimension i for A[i][l][c].
         for (l = 0; l < numRows; l++) {
            for (c = 0; c < numCols; c++) {
               System.out.print (A[i][l][c] + "  ");
            }
            System.out.println ("");
         }
      }
      System.out.println ("");
   }


   // Print matrix M
   private void printMat0 (int[][] A, String name) {
         System.out.println ("-------------------------------------" +
                             PrintfFormat.NEWLINE + name);
         int l, c;   // row l, column c for A[l][c].
         for (l = 0; l < numCols; l++) {
            for (c = 0; c < numCols; c++) {
               System.out.print (A[l][c] + "  ");
            }
            System.out.println ("");
         }
      System.out.println ("");
   }


   // Left-multiplies lower-triangular matrix Mj by original C_j,
   // where original C_j is in originalMat and result is in genMat.
   // This implementation is safe only if (numRows*(b-1)^2) is a valid int.
   private void leftMultiplyMat (int j, int[][] Mj) {
      int l, c, i, sum;   // Dimension j, row l, column c for new C_j.
      for (l = 0; l < numRows ; l++) {
         for (c = 0; c < numCols; c++) {
            // Multiply row l of M_j by column c of C_j.
            sum = 0;
            for (i = 0; i <= l; i++)
               sum += Mj[l][i] * originalMat[j*numCols+c][i];
            genMat[j*numCols+c][l] = sum % b;
         }
      }
   }


   // Left-multiplies diagonal matrix Mj by original C_j,
   // where original C_j is in originalMat and result is in genMat.
   // This implementation is safe only if (numRows*(b-1)^2) is a valid int.
   private void leftMultiplyMatDiag (int j, int[][] Mj) {
      int l, c, sum;   // Dimension j, row l, column c for new C_j.
      for (l = 0; l < numRows ; l++) {
         for (c = 0; c < numCols; c++) {
            // Multiply row l of M_j by column c of C_j.
            sum = Mj[l][l] * originalMat[j*numCols+c][l];
            genMat[j*numCols+c][l] = sum % b;
         }
      }
   }


   // Right-multiplies original C_j by upper-triangular matrix Mj,
   // where original C_j is in originalMat and result is in genMat.
   // This implementation is safe only if (numCols*(b-1)^2) is a valid int.
   private void rightMultiplyMat (int j, int[][] Mj) {
      int l, c, i, sum;   // Dimension j, row l, column c for new C_j.
      for (l = 0; l < numRows ; l++) {
         for (c = 0; c < numCols; c++) {
            // Multiply row l of C_j by column c of M_j.
            sum = 0;
            for (i = 0; i <= c; i++)
               sum += originalMat[j*numCols+i][l] * Mj[i][c];
            genMat[j*numCols+c][l] = sum % b;
         }
      }
   }


   private int getFaureIndex (String method, int sb, int flag) {
      // Check for errors in ...FaurePermut. Returns the index ib of the
      // base b in primes, i.e.  b = primes[ib].
      if (sb >= b)
         throw new IllegalArgumentException (PrintfFormat.NEWLINE +
            "   sb >= base in " + method);
      if (sb < 1)
         throw new IllegalArgumentException (PrintfFormat.NEWLINE +
            "   sb = 0 in " + method);
      if ((flag > 2) || (flag < 0))
         throw new IllegalArgumentException (
             PrintfFormat.NEWLINE + "   lowerFlag not in {0, 1, 2} in "
             + method);

      // Find index ib of base in array primes
      int ib = 0;
      while ((ib < primes.length) && (primes[ib] < b))
          ib++;
      if (ib >= primes.length)
         throw new IllegalArgumentException ("base too large in " + method);
      if (b != primes[ib])
         throw new IllegalArgumentException (
            "Faure factors are not implemented for this base in " + method);
      return ib;
      }


   /**
    * Applies a linear scramble by multiplying each 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> on the left
    *    by a <SPAN CLASS="MATH"><I>w</I>&#215;<I>w</I></SPAN> nonsingular lower-triangular matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN>,
    *    as suggested by Matou&#353;ek and implemented
    *    by Hong and Hickernell. The diagonal entries of
    *    each matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are generated uniformly over
    *    
    * <SPAN CLASS="MATH">{1,..., <I>b</I> - 1}</SPAN>, the entries below the diagonal are generated uniformly
    *    over 
    * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}</SPAN>, and all these entries are generated independently.
    *    This means that in base <SPAN CLASS="MATH"><I>b</I> = 2</SPAN>, all diagonal elements are equal to 1.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    * 
    */
   public void leftMatrixScramble (RandomStream stream)  {
      int j, l, c;  // dimension j, row l, column c.

      // If genMat contains the original gen. matrices, copy to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         for (l = 0; l < numRows; l++) {
            for (c = 0; c < numRows; c++) {
               if (c == l)                   // No zero on the diagonal.
                  scrambleMat[j][l][c] = stream.nextInt(1, b - 1) ;
               else if (c < l)
                  scrambleMat[j][l][c] = stream.nextInt(0, b - 1);
               else
                  scrambleMat[j][l][c] = 0;  // Zeros above the diagonal;
            }
         }
      }

      // Multiply M_j by the generator matrix C_j for each j.
      for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
   }


   /**
    * Similar to {@link #leftMatrixScramble leftMatrixScramble} except that all the
    *    off-diagonal elements of the 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are 0.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    * 
    */
   public void leftMatrixScrambleDiag (RandomStream stream)  {
      int j, l, c;  // dimension j, row l, column c.

      // If genMat contains the original gen. matrices, copy to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the diagonal scrambling matrices M_j, r by r.
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         for (l = 0; l < numRows; l++) {
            for (c = 0; c < numRows; c++) {
               if (c == l)                   // No zero on the diagonal.
                  scrambleMat[j][l][c] = stream.nextInt(1, b - 1) ;
               else
                  scrambleMat[j][l][c] = 0;  // Diagonal matrix;
            }
         }
      }

      // Multiply M_j by the generator matrix C_j for each j.
      for (j = 0 ; j < dim; j++) leftMultiplyMatDiag (j, scrambleMat[j]);
   }


   private void LMSFaurePermut (String method, RandomStream stream, int sb,
      int lowerFlag) {
/*
   If \texttt{lowerFlag = 2}, the off-diagonal elements below the diagonal
   of $\mathbf{M}_j$ are chosen as in \method{leftMatrixScramble}{}.
   If \texttt{lowerFlag = 1}, the off-diagonal elements below the diagonal of
   $\mathbf{M}_j$ are also chosen from the restricted set. If
    \texttt{lowerFlag = 0}, the off-diagonal elements of $\mathbf{M}_j$ are all 0.
*/
      int ib = getFaureIndex (method, sb, lowerFlag);

      // If genMat contains the original gen. matrices, copy to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int jb;
      int j, l, c;  // dimension j, row l, column c.
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         for (l = 0; l < numRows; l++) {
            for (c = 0; c < numRows; c++) {
               if (c == l) {
                  jb = stream.nextInt(0, sb - 1);
                  scrambleMat[j][l][c] = FaureFactor[ib][jb];
               } else if (c < l) {
                  if (lowerFlag == 2) {
                     scrambleMat[j][l][c] = stream.nextInt(0, b - 1);
                  } else if (lowerFlag == 1) {
                     jb = stream.nextInt(0, sb - 1);
                     scrambleMat[j][l][c] = FaureFactor[ib][jb];
                  } else {   // lowerFlag == 0
                     scrambleMat[j][l][c] = 0;
                  }
               } else
                  scrambleMat[j][l][c] = 0;  // Zeros above the diagonal;
            }
         }
      }
      // printMat (dim, scrambleMat, method);

      // Multiply M_j by the generator matrix C_j for each j.
      if (lowerFlag == 0)
         for (j = 0 ; j < dim; j++) leftMultiplyMatDiag (j, scrambleMat[j]);
      else
         for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
   }

   /**
    * Similar to {@link #leftMatrixScramble leftMatrixScramble} except that the diagonal elements
    *    of each matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are chosen from a restricted set of the best
    *    integers as calculated by Faure. They are generated
    *    uniformly over the first <TT>sb</TT> elements of array <SPAN CLASS="MATH"><I>F</I></SPAN>, where <SPAN CLASS="MATH"><I>F</I></SPAN> is
    *    made up of a permutation of the integers 
    * <SPAN CLASS="MATH">[1..(<I>b</I> - 1)]</SPAN>. These integers are
    *    sorted by increasing  order of the upper bounds of the extreme discrepancy
    *    for the given integer.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void leftMatrixScrambleFaurePermut (RandomStream stream, int sb) {
       LMSFaurePermut ("leftMatrixScrambleFaurePermut", stream, sb, 2);
   }


   /**
    * Similar to {@link #leftMatrixScrambleFaurePermut leftMatrixScrambleFaurePermut} except that all
    *    off-diagonal elements are 0.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void leftMatrixScrambleFaurePermutDiag (RandomStream stream,
                                                  int sb) {
       LMSFaurePermut ("leftMatrixScrambleFaurePermutDiag",
                              stream, sb, 0);
   }


   /**
    * Similar to {@link #leftMatrixScrambleFaurePermut leftMatrixScrambleFaurePermut} except that the
    *    elements under the diagonal are also
    *    chosen from the same restricted set as the diagonal elements.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void leftMatrixScrambleFaurePermutAll (RandomStream stream,
                                                 int sb) {
       LMSFaurePermut ("leftMatrixScrambleFaurePermutAll",
                              stream, sb, 1);
   }


   /**
    * Applies the <SPAN CLASS="MATH"><I>i</I></SPAN>-binomial matrix scramble proposed by Tezuka
    *    .
    *    This multiplies each 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> on the left
    *    by a <SPAN CLASS="MATH"><I>w</I>&#215;<I>w</I></SPAN> nonsingular lower-triangular matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> as in
    *    {@link #leftMatrixScramble leftMatrixScramble}, but with the additional constraint that
    *    all entries on any given diagonal or subdiagonal of 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are identical.
    * 
    * @param stream random number stream used as generator of the randomness
    * 
    * 
    */
   public void iBinomialMatrixScramble (RandomStream stream) {
      int j, l, c;  // dimension j, row l, column c.
      int diag;     // random entry on the diagonal;
      int col1;     // random entries below the diagonal;

      // If genMat is original generator matrices, copy it to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         diag = stream.nextInt(1, b - 1);
         for (l = 0; l < numRows; l++) {
            // Single random nonzero element on the diagonal.
            scrambleMat[j][l][l] = diag;
            for (c = l+1; c < numRows; c++) scrambleMat[j][l][c] = 0;
         }
         for (l = 1; l < numRows; l++) {
            col1 = stream.nextInt(0, b - 1);
            for (c = 0; l+c < numRows; c++) scrambleMat[j][l+c][c] = col1;
         }
      }
      // printMat (dim, scrambleMat,  "iBinomialMatrixScramble");
      for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
   }


   private void iBMSFaurePermut (String method, RandomStream stream,
                                 int sb, int lowerFlag) {
      int ib = getFaureIndex (method, sb, lowerFlag);

      // If genMat is original generator matrices, copy it to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int j, l, c;  // dimension j, row l, column c.
      int diag;     // random entry on the diagonal;
      int col1;     // random entries below the diagonal;
      int jb;
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         jb = stream.nextInt(0, sb - 1);
         diag = FaureFactor[ib][jb];
         for (l = 0; l < numRows; l++) {
            // Single random nonzero element on the diagonal.
            scrambleMat[j][l][l] = diag;
            for (c = l+1; c < numRows; c++) scrambleMat[j][l][c] = 0;
         }
         for (l = 1; l < numRows; l++) {
            if (lowerFlag == 2) {
               col1 = stream.nextInt(0, b - 1);
            } else if (lowerFlag == 1) {
               jb = stream.nextInt(0, sb - 1);
               col1 = FaureFactor[ib][jb];
            } else {   // lowerFlag == 0
               col1 = 0;
            }
            for (c = 0; l+c < numRows; c++) scrambleMat[j][l+c][c] = col1;
         }
      }
      // printMat (dim, scrambleMat, method);

      if (lowerFlag > 0)
         for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
      else
         for (j = 0 ; j < dim; j++) leftMultiplyMatDiag (j, scrambleMat[j]);
   }

   /**
    * Similar to {@link #iBinomialMatrixScramble iBinomialMatrixScramble} except that the diagonal
    *    elements of each matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are chosen as in
    *   {@link #leftMatrixScrambleFaurePermut leftMatrixScrambleFaurePermut}.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void iBinomialMatrixScrambleFaurePermut (RandomStream stream,
                                                   int sb) {
       iBMSFaurePermut ("iBinomialMatrixScrambleFaurePermut",
                              stream, sb, 2);
   }


   /**
    * Similar to {@link #iBinomialMatrixScrambleFaurePermut iBinomialMatrixScrambleFaurePermut} except that all the
    *    off-diagonal elements are 0.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void iBinomialMatrixScrambleFaurePermutDiag (RandomStream stream,
                                                       int sb) {
       iBMSFaurePermut ("iBinomialMatrixScrambleFaurePermutDiag",
                               stream, sb, 0);
   }


   /**
    * Similar to {@link #iBinomialMatrixScrambleFaurePermut iBinomialMatrixScrambleFaurePermut} except that the
    *    elements under the diagonal are also
    *    chosen from the same restricted set as the diagonal elements.
    * 
    * @param stream random number stream used to generate the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void iBinomialMatrixScrambleFaurePermutAll (RandomStream stream,
                                                      int sb) {
       iBMSFaurePermut ("iBinomialMatrixScrambleFaurePermutAll",
                               stream, sb, 1);
   }


   /**
    * Applies the <SPAN  CLASS="textit">striped matrix scramble</SPAN> proposed by Owen.
    *    It multiplies each 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> on the left
    *    by a <SPAN CLASS="MATH"><I>w</I>&#215;<I>w</I></SPAN> nonsingular lower-triangular matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> as in
    *    {@link #leftMatrixScramble leftMatrixScramble}, but with the additional constraint that
    *    in any column, all entries below the diagonal are equal to the
    *    diagonal entry, which is generated randomly over 
    * <SPAN CLASS="MATH">{1,..., <I>b</I> - 1}</SPAN>.
    *    Note that for <SPAN CLASS="MATH"><I>b</I> = 2</SPAN>, the matrices 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> become deterministic, with
    *    all entries on and below the diagonal equal to 1.
    * 
    * @param stream random number stream used as generator of the randomness
    * 
    * 
    */
   public void stripedMatrixScramble (RandomStream stream) {
      int j, l, c;  // dimension j, row l, column c.
      int diag;     // random entry on the diagonal;
      int col1;     // random entries below the diagonal;

      // If genMat contains original gener. matrices, copy it to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         for (c = 0; c < numRows; c++) {
            diag = stream.nextInt (1, b - 1);   // Random entry in this column.
            for (l = 0; l < c; l++)         scrambleMat[j][l][c] = 0;
            for (l = c; l < numRows; l++) scrambleMat[j][l][c] = diag;
         }
      }
      // printMat (dim, scrambleMat,  "stripedMatrixScramble");
      for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
   }


   /**
    * Similar to {@link #stripedMatrixScramble stripedMatrixScramble} except that the
    *    elements on and under the diagonal of each matrix 
    * <SPAN CLASS="MATH"><B>M</B><SUB>j</SUB></SPAN> are
    *    chosen as in {@link #leftMatrixScrambleFaurePermut leftMatrixScrambleFaurePermut}.
    * 
    * @param stream random number stream used as generator of the randomness
    * 
    *    @param sb Only the first <SPAN CLASS="MATH"><I>sb</I></SPAN> elements of <SPAN CLASS="MATH"><I>F</I></SPAN> are used
    * 
    * 
    */
   public void stripedMatrixScrambleFaurePermutAll (RandomStream stream,
                                                    int sb) {
      int ib = getFaureIndex ("stripedMatrixScrambleFaurePermutAll", sb, 1);

      // If genMat contains original gener. matrices, copy it to originalMat.
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Constructs the lower-triangular scrambling matrices M_j, r by r.
      int j, l, c;  // dimension j, row l, column c.
      int diag;     // random entry on the diagonal;
      int col1;     // random entries below the diagonal;
      int jb;
      int[][][] scrambleMat = new int[dim][numRows][numRows];
      for (j = 0 ; j < dim; j++) {
         for (c = 0; c < numRows; c++) {
            jb = stream.nextInt(0, sb - 1);
            diag = FaureFactor[ib][jb];  // Random entry in this column.
            for (l = 0; l < c; l++)         scrambleMat[j][l][c] = 0;
            for (l = c; l < numRows; l++) scrambleMat[j][l][c] = diag;
         }
      }
      // printMat (dim, scrambleMat,  "stripedMatrixScrambleFaurePermutAll");
      for (j = 0 ; j < dim; j++) leftMultiplyMat (j, scrambleMat[j]);
   }


   /**
    * Applies a linear scramble by multiplying each 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> on the right
    *    by a single <SPAN CLASS="MATH"><I>k</I>&#215;<I>k</I></SPAN> nonsingular upper-triangular matrix 
    * <SPAN CLASS="MATH"><B>M</B></SPAN>,
    *    as suggested by Faure and Tezuka.
    *    The diagonal entries of the matrix 
    * <SPAN CLASS="MATH"><B>M</B></SPAN> are generated uniformly over
    *    
    * <SPAN CLASS="MATH">{1,..., <I>b</I> - 1}</SPAN>, the entries above the diagonal are generated uniformly
    *    over 
    * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}</SPAN>, and all the entries are generated independently.
    *    The effect of this scramble is only to change the order in which the
    *    points are generated.  If one computes the average value of a
    *    function over <SPAN  CLASS="textit">all</SPAN> the points of a given digital net, or over
    *    a number of points that is a power of the basis, then this
    *    scramble makes no difference.
    * 
    * @param stream random number stream used as generator of the randomness
    * 
    * 
    */
   public void rightMatrixScramble (RandomStream stream)  {
      int j, c, l, i, sum;  // dimension j, row l, column c, of genMat.

      // SaveOriginalMat();
      if (originalMat == null) {
         originalMat = genMat;
         genMat = new int[dim * numCols][numRows];
      }

      // Generate an upper triangular matrix for Faure-Tezuka right-scramble.
      // Entry [l][c] is in row l, col c.
      int[][] scrambleMat = new int[numCols][numCols];
      for (c = 0; c < numCols; c++) {
         for (l = 0; l < c; l++) scrambleMat[l][c] = stream.nextInt (0, b - 1);
         scrambleMat[c][c] = stream.nextInt (1, b - 1);
         for (l = c+1; l < numCols; l++) scrambleMat[l][c] = 0;
      }

      // printMat0 (scrambleMat,  "rightMatrixScramble");
      // Right-multiply the generator matrices by the scrambling matrix.
      for (j = 0 ; j < dim; j++) rightMultiplyMat (j, scrambleMat);
   }


   public void unrandomize() {
      resetGeneratorMatrices();
      digitalShift = null;
   }


   /**
    * Restores the original generator matrices.
    *    This removes the current linear matrix scrambles.
    * 
    */
   public void resetGeneratorMatrices() {
      if (originalMat != null) {
         genMat = originalMat;
         originalMat = null;
      }
   }


   /**
    * Erases the original generator matrices and replaces them by
    *    the current ones.  The current linear matrix scrambles thus become
    *    <SPAN  CLASS="textit">permanent</SPAN>.  This is useful if we want to apply several
    *    scrambles in succession to a given digital net.
    * 
    */
   public void eraseOriginalGeneratorMatrices() {
      originalMat = null;
   }


   /**
    * Prints the generator matrices in standard form for dimensions 1 to <SPAN CLASS="MATH"><I>s</I></SPAN>.
    * 
    */
   public void printGeneratorMatrices (int s)  {
      // row l, column c, dimension j.
      for (int j = 0; j < s; j++) {
         System.out.println ("dim = " + (j+1) + PrintfFormat.NEWLINE);
         for (int l = 0; l < numRows; l++) {
            for (int c = 0; c < numCols; c++)
               System.out.print (genMat[j*numCols+c][l] + "  ");
            System.out.println ("");
         }
         System.out.println ("----------------------------------");
      }
   }


      // Computes the digital expansion of $i$ in base $b$, and the digits
      // of the Gray code of $i$.
      // These digits are placed in arrays \texttt{bary} and \texttt{gray},
      // respectively, and the method returns the number of digits in the
      // expansion.  The two arrays are assumed to be of sizes larger or
      // equal to this new number of digits.
      protected int intToDigitsGray (int b, int i, int numDigits,
                                            int[] bary, int[] gray) {
         if (i == 0)
            return 0;
         int idigits = 0; // Num. of digits in b-ary and Gray repres.
         int c;
         // convert i to b-ary representation, put digits in bary[].
         for (c = 0; i > 0; c++) {
            idigits++;
            bary[c] = i % b;
            i = i / b;
         }
         // convert b-ary representation to Gray code.
         gray[idigits-1] = bary[idigits-1];
         int diff;
         for (c = 0;  c < idigits-1; c++) {
            diff = bary[c] - bary[c+1];
            if (diff < 0)
               gray[c] = diff + b;
            else
               gray[c] = diff;
         }
         for (c = idigits; c < numDigits; c++) gray[c] = bary[c] = 0;
         return idigits;
      }


// ************************************************************************

   protected class DigitalNetIterator extends
          PointSet.DefaultPointSetIterator {

      protected int idigits;        // Num. of digits in current code.
      protected int[] bdigit;       // b-ary code of current point index.
      protected int[] gdigit;       // Gray code of current point index.
      protected int dimS;           // = dim except in the shift iterator
                                    // children where it is = dim + 1.
      protected int[] cachedCurPoint; // Digits of coords of the current point,
                           // with digital shift already applied.
                           // Digit l of coord j is at pos. [j*outDigits + l].
                           // Has one more dimension for the shift iterators.

      public DigitalNetIterator() {
 //        EpsilonHalf = 0.5 / Math.pow ((double) b, (double) outDigits);
         bdigit = new int[numCols];
         gdigit = new int[numCols];
         dimS = dim;
         cachedCurPoint = new int[(dim + 1)* outDigits];
         init();  // This call is important so that subclasses don't
                  // automatically call 'resetCurPointIndex' at the time
                  // of construction as this may cause a subtle
                  // 'NullPointerException'
      }

      public void init() { // See constructor
         resetCurPointIndex();
      }

      // We want to avoid generating 0 or 1
      public double nextDouble() {
         return nextCoordinate() + EpsilonHalf;
      }

      public double nextCoordinate() {
         if (curPointIndex >= numPoints || curCoordIndex >= dimS)
            outOfBounds();
         int start = outDigits * curCoordIndex++;
         double sum = 0;
         // Can always have up to outDigits digits, because of digital shift.
         for (int k = 0; k < outDigits; k++)
            sum += cachedCurPoint [start+k] * factor[k];
         if (digitalShift != null)
            sum += EpsilonHalf;
        return sum;
      }

      public void resetCurPointIndex() {
         if (digitalShift == null)
            for (int i = 0; i < cachedCurPoint.length; i++)
               cachedCurPoint[i] = 0;
         else {
            if (dimShift < dimS)
               addRandomShift (dimShift, dimS, shiftStream);
            for (int j = 0; j < dimS; j++)
               for (int k = 0; k < outDigits; k++)
                  cachedCurPoint[j*outDigits + k] = digitalShift[j][k];
         }
         for (int i = 0; i < numCols; i++) bdigit[i] = 0;
         for (int i = 0; i < numCols; i++) gdigit[i] = 0;
         curPointIndex = 0;
         curCoordIndex = 0;
         idigits = 0;
      }

      public void setCurPointIndex (int i) {
         if (i == 0) {
            resetCurPointIndex();
            return;
         }
         curPointIndex = i;
         curCoordIndex = 0;
         if (digitalShift != null && dimShift < dimS)
             addRandomShift (dimShift, dimS, shiftStream);

         // Digits of Gray code, used to reconstruct cachedCurPoint.
         idigits = intToDigitsGray (b, i, numCols, bdigit, gdigit);
         int c, j, l, sum;
         for (j = 0; j < dimS; j++) {
            for (l = 0; l < outDigits; l++) {
               if (digitalShift == null)
                  sum = 0;
               else
                  sum = digitalShift[j][l];
               if (l < numRows)
                  for (c = 0; c < idigits; c++)
                     sum += genMat[j*numCols+c][l] * gdigit[c];
               cachedCurPoint [j*outDigits+l] = sum % b;
            }
         }
      }

      public int resetToNextPoint() {
         // incremental computation.
         curPointIndex++;
         curCoordIndex = 0;
         if (curPointIndex >= numPoints)
            return curPointIndex;

         // Update the digital expansion of i in base b, and find the
         // position of change in the Gray code. Set all digits == b-1 to 0
         // and increase the first one after by 1.
         int pos;      // Position of change in the Gray code.
         for (pos = 0; gdigit[pos] == b-1; pos++)
            gdigit[pos] = 0;
         gdigit[pos]++;

         // Update the cachedCurPoint by adding the column of the gener.
         // matrix that corresponds to the Gray code digit that has changed.
         // The digital shift is already incorporated in the cached point.
         int c, j, l;
         int lsup = numRows;        // Max index l
         if (outDigits < numRows)
            lsup = outDigits;
         for (j = 0; j < dimS; j++) {
            for (l = 0; l < lsup; l++) {
               cachedCurPoint[j*outDigits + l] += genMat[j*numCols + pos][l];
               cachedCurPoint[j * outDigits + l] %= b;
            }
         }
         return curPointIndex;
      }
   }


// ************************************************************************

   protected class DigitalNetIteratorNoGray extends DigitalNetIterator {

      public DigitalNetIteratorNoGray() {
         super();
      }

      public void setCurPointIndex (int i) {
         if (i == 0) {
            resetCurPointIndex();
            return;
         }
         curPointIndex = i;
         curCoordIndex = 0;
         if (dimShift < dimS)
             addRandomShift (dimShift, dimS, shiftStream);

         // Convert i to b-ary representation, put digits in bdigit.
         idigits = intToDigitsGray (b, i, numCols, bdigit, gdigit);
         int c, j, l, sum;
         for (j = 0; j < dimS; j++) {
            for (l = 0; l < outDigits; l++) {
               if (digitalShift == null)
                  sum = 0;
               else
                  sum = digitalShift[j][l];
               if (l < numRows)
                  for (c = 0; c < idigits; c++)
                     sum += genMat[j*numCols+c][l] * bdigit[c];
               cachedCurPoint [j*outDigits+l] = sum % b;
            }
         }
      }

      public int resetToNextPoint() {
         curPointIndex++;
         curCoordIndex = 0;
         if (curPointIndex >= numPoints)
            return curPointIndex;

         // Find the position of change in the digits of curPointIndex in base
         // b. Set all digits = b-1 to 0; increase the first one after by 1.
         int pos;
         for (pos = 0; bdigit[pos] == b-1; pos++)
            bdigit[pos] = 0;
         bdigit[pos]++;

         // Update the digital expansion of curPointIndex in base b.
         // Update the cachedCurPoint by adding 1 unit at the digit pos.
         // If pos > 0, remove b-1 units in the positions < pos. Since
         // calculations are mod b, this is equivalent to adding 1 unit.
         // The digital shift is already incorporated in the cached point.
         int c, j, l;
         int lsup = numRows;        // Max index l
         if (outDigits < numRows)
            lsup = outDigits;
         for (j = 0; j < dimS; j++) {
            for (l = 0; l < lsup; l++) {
               for (c = 0; c <= pos; c++)
                  cachedCurPoint[j*outDigits + l] += genMat[j*numCols + c][l];
               cachedCurPoint[j * outDigits + l] %= b;
            }
         }
         return curPointIndex;
      }
   }

}
