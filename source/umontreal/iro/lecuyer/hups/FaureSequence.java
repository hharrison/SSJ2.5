

/*
 * Class:        FaureSequence
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



/**
 * This class implements digital nets or digital sequences formed by the
 *  first <SPAN CLASS="MATH"><I>n</I> = <I>b</I><SUP>k</SUP></SPAN> points of the Faure sequence in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
 * Values of <SPAN CLASS="MATH"><I>n</I></SPAN> up to <SPAN CLASS="MATH">2<SUP>31</SUP></SPAN> are allowed.
 * One has <SPAN CLASS="MATH"><I>r</I> = <I>k</I></SPAN>.
 * The generator matrices are
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>C</B><SUB>j</SUB> = <B>P</B><SUP>j</SUP>&nbsp;mod&nbsp;<I>b</I>
 * </DIV><P></P>
 * for 
 * <SPAN CLASS="MATH"><I>j</I> = 0,..., <I>s</I> - 1</SPAN>, where 
 * <SPAN CLASS="MATH"><B>P</B></SPAN> is a <SPAN CLASS="MATH"><I>k</I>&#215;<I>k</I></SPAN> upper
 *  triangular matrix whose entry <SPAN CLASS="MATH">(<I>l</I>, <I>c</I>)</SPAN> is the number of combinations
 * of <SPAN CLASS="MATH"><I>l</I></SPAN> objects among <SPAN CLASS="MATH"><I>c</I></SPAN>,
 * for <SPAN CLASS="MATH"><I>l</I>&nbsp;&lt;=&nbsp;<I>c</I></SPAN> and is 0 for <SPAN CLASS="MATH"><I>l</I> &gt; <I>c</I></SPAN>.
 * The matrix 
 * <SPAN CLASS="MATH"><B>C</B><SUB>0</SUB></SPAN> is the identity, 
 * <SPAN CLASS="MATH"><B>C</B><SUB>1</SUB> = <B>P</B></SPAN>,
 * and the other 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>'s can be defined recursively via
 * 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB> = <B>P</B><B>C</B><SUB>j-1</SUB>mod&nbsp;<I>b</I></SPAN>.
 * Our implementation uses the recursion
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * Combination(<I>c</I>, <I>l</I> )&nbsp; = &nbsp;Combination(<I>c</I> - 1, <I>l</I> ) &nbsp; + &nbsp;Combination(<I>c</I> - 1, <I>l</I> - 1)
 * </DIV><P></P>
 * to evaluate the binomial coefficients in the matrices 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>,
 * as suggested by Fox.
 * The entries <SPAN CLASS="MATH"><I>x</I><SUB>j, l, c</SUB></SPAN> of 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> are computed as follows:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="LEFT"><I>x</I><SUB>j, c, c</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT">1</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp; for <I>c</I> = 0,..., <I>k</I> - 1,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="LEFT"><I>x</I><SUB>j, 0, c</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT"><I>jx</I><SUB>j, 0, c-1</SUB></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp; for <I>c</I> = 1,..., <I>k</I> - 1,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="LEFT"><I>x</I><SUB>j, l, c</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT"><I>x</I><SUB>j, l-1, c-1</SUB> + <I>jx</I><SUB>j, l, c-1</SUB></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp; for 2&nbsp;&lt;=&nbsp;<I>c</I> &lt; <I>l</I>&nbsp;&lt;=&nbsp;<I>k</I> - 1,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="LEFT"><I>x</I><SUB>j, l, c</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT">0</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp; for <I>c</I> &gt; <I>l</I> or <I>l</I>&nbsp;&gt;=&nbsp;<I>k</I>.</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * 
 * <P>
 * For any integer <SPAN CLASS="MATH"><I>m</I> &gt; 0</SPAN> and <SPAN CLASS="MATH"><I>&#957;</I>&nbsp;&gt;=&nbsp; 0</SPAN>, if we look at the
 * vector 
 * <SPAN CLASS="MATH">(<I>u</I><SUB>i, j, 1</SUB>,..., <I>u</I><SUB>i, j, m</SUB>)</SPAN> (the first <SPAN CLASS="MATH"><I>m</I></SPAN> digits
 * of coordinate <SPAN CLASS="MATH"><I>j</I></SPAN> of the output) when <SPAN CLASS="MATH"><I>i</I></SPAN> goes from
 * <SPAN CLASS="MATH"><I>&#957;b</I><SUP>m</SUP></SPAN> to 
 * <SPAN CLASS="MATH">(<I>&#957;</I> +1)<I>b</I><SUP>m</SUP> - 1</SPAN>, this vector takes each of its <SPAN CLASS="MATH"><I>b</I><SUP>m</SUP></SPAN>
 * possible values exactly once.
 * In particular, for <SPAN CLASS="MATH"><I>&#957;</I> = 0</SPAN>, <SPAN CLASS="MATH"><I>u</I><SUB>i, j</SUB></SPAN> visits each value in the
 * set 
 * <SPAN CLASS="MATH">{0, 1/<I>b</I><SUP>m</SUP>, 2/<I>b</I><SUP>m</SUP>,...,(<I>b</I><SUP>m</SUP> -1)/<I>b</I><SUP>m</SUP>}</SPAN> exactly once, so all
 * one-dimensional projections of the point set are identical.
 * However, the values are visited in a different order for
 * the different values of <SPAN CLASS="MATH"><I>j</I></SPAN> (otherwise all coordinates would be identical).
 * For <SPAN CLASS="MATH"><I>j</I> = 0</SPAN>, they are visited in the same
 * order as in the van der Corput sequence in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
 * 
 * <P>
 * An important property of Faure nets is that for any integers <SPAN CLASS="MATH"><I>m</I> &gt; 0</SPAN>
 * and <SPAN CLASS="MATH"><I>&#957;</I>&nbsp;&gt;=&nbsp; 0</SPAN>, the point set
 * 
 * <SPAN CLASS="MATH">{<SPAN CLASS="MATH"><B><I>u</I></B></SPAN><SUB>i</SUB></SPAN> for 
 * <SPAN CLASS="MATH"><B><I>i</I> = <I>&#957;b</I><SUP>m</SUP>,...,(<I>&#957;</I> +1)<I>b</I><SUP>m</SUP> -1}</B></SPAN>
 * is a <SPAN CLASS="MATH"><B>(0, <I>m</I>, <I>s</I>)</B></SPAN>-net in base <SPAN CLASS="MATH"><B><I>b</I></B></SPAN>.
 * In particular, for <SPAN CLASS="MATH"><B><I>n</I> = <I>b</I><SUP>k</SUP></B></SPAN>, the first <SPAN CLASS="MATH"><B><I>n</I></B></SPAN> points form a
 *  <SPAN CLASS="MATH"><B>(0, <I>k</I>, <I>s</I>)</B></SPAN>-net in base <SPAN CLASS="MATH"><B><I>b</I></B></SPAN>.
 * The Faure nets are also projection-regular and dimension-stationary.
 * 
 * <P>
 * To obtain digital nets from the <SPAN  CLASS="textit">generalized Faure sequence</SPAN>
 * , where 
 * <SPAN CLASS="MATH"><B><B>P</B><SUB>j</SUB></B></SPAN> is left-multiplied by some
 * invertible matrix 
 * <SPAN CLASS="MATH"><B><B>A</B><SUB>j</SUB></B></SPAN>, it suffices to apply an appropriate
 * matrix scramble (e.g., via {@link #leftMatrixScramble leftMatrixScramble}).
 * This changes the order in which <SPAN CLASS="MATH"><B><I>u</I><SUB>i, j</SUB></B></SPAN> visits its different
 * values, for each coordinate <SPAN CLASS="MATH"><B><I>j</I></B></SPAN>, but does not change the set of values
 * that are visited.  The <SPAN CLASS="MATH"><B>(0, <I>m</I>, <I>s</I>)</B></SPAN>-net property stated above remains valid.
 * 
 */
public class FaureSequence extends DigitalSequence  {

    // Maximum dimension for the case where b is not specified.
    // Can be extended by extending the precomputed array prime[].
    private static final int MAXDIM = 500;

    // For storing the generator matrices for given dim and numPoints.
    private int[][][] v;



   /**
    * Constructs a digital net in base <SPAN CLASS="MATH"><B><I>b</I></B></SPAN>,
    *     with <SPAN CLASS="MATH"><B><I>n</I> = <I>b</I><SUP>k</SUP></B></SPAN> points and <SPAN CLASS="MATH"><B><I>w</I></B></SPAN> output digits,
    *     in <TT>dim</TT> dimensions.
    *     The points are the first <SPAN CLASS="MATH"><B><I>n</I></B></SPAN> points of the Faure sequence.
    *     The generator matrices 
    * <SPAN CLASS="MATH"><B><B>C</B><SUB>j</SUB></B></SPAN> are <SPAN CLASS="MATH"><B><I>r</I>&#215;<I>k</I></B></SPAN>.
    *     Unless, one plans to apply a randomization on more than <SPAN CLASS="MATH"><B><I>k</I></B></SPAN> digits
    *     (e.g., a random digital shift for <SPAN CLASS="MATH"><B><I>w</I> &gt; <I>k</I></B></SPAN> digits, or a linear
    *     scramble yielding <SPAN CLASS="MATH"><B><I>r</I> &gt; <I>k</I></B></SPAN> digits), one should
    *     take <SPAN CLASS="MATH"><B><I>w</I> = <I>r</I> = <I>k</I></B></SPAN> for better computational efficiency.
    *     Restrictions: <TT>dim</TT> <SPAN CLASS="MATH"><B>&nbsp;&lt;=&nbsp;500</B></SPAN> and 
    * <SPAN CLASS="MATH"><B><I>b</I><SUP>k</SUP>&nbsp;&lt;=&nbsp;2<SUP>31</SUP></B></SPAN>.
    * 
    * @param b base
    * 
    *    @param k there will be b^k points
    * 
    *    @param r number of rows in the generator matrices
    * 
    *    @param w number of output digits
    * 
    *    @param dim dimension of the point set
    * 
    * 
    */
   public FaureSequence (int b, int k, int r, int w, int dim)  {
      init (b, k, r, w, dim);
   }

   private void init (int b, int k, int r, int w, int dim) {
      if (dim < 1)
         throw new IllegalArgumentException
            ("Dimension for FaureSequence must be > 1");
      if ((double)k * Math.log ((double)b) > (31.0 * Math.log (2.0)))
         throw new IllegalArgumentException
            ("Trying to construct a FaureSequence with too many points");
      if (r < k || w < r)
         throw new IllegalArgumentException
            ("One must have k <= r <= w for FaureSequence");
      this.b    = b;
      numCols   = k;
      numRows   = r;
      outDigits = w;
      this.dim  = dim;

      int i, j;
      numPoints = b;
      for (i=1; i<k; i++) numPoints *= b;

      // Compute the normalization factors.
      normFactor = 1.0 / Math.pow ((double) b, (double) outDigits);
//      EpsilonHalf = 0.5*normFactor;
      double invb = 1.0 / b;
      factor = new double[outDigits];
      factor[0] = invb;
      for (j = 1; j < outDigits; j++)
         factor[j] = factor[j-1] * invb;

      genMat = new int[dim * numCols][numRows];
      initGenMat();
   }



   /**
    * Same as {@link #FaureSequence FaureSequence}<TT>(b, k, w, w, dim)</TT>
    *   with base <SPAN CLASS="MATH"><B><I>b</I></B></SPAN> equal to the smallest prime larger or equal to <TT>dim</TT>,
    *   and with <SPAN  CLASS="textit">at least</SPAN> <TT>n</TT> points.
    * 
    *   The values of <SPAN CLASS="MATH"><B><I>k</I></B></SPAN>, <SPAN CLASS="MATH"><B><I>r</I></B></SPAN>, and <SPAN CLASS="MATH"><B><I>w</I></B></SPAN> are taken as
    *   
    * <SPAN CLASS="MATH"><B><I>k</I> = ceil(log<SUB>b</SUB><I>n</I>)</B></SPAN> and
    *   
    * <SPAN CLASS="MATH"><B><I>r</I> = <I>w</I> = max(<I>k</I>, floor(30/log<SUB>2</SUB><I>b</I>))</B></SPAN>.
    * 
    * @param n minimal number of points
    * 
    *    @param dim dimension of the point set
    * 
    * 
    */
   public FaureSequence (int n, int dim)  {
      if ((dim < 1) || (dim > MAXDIM))
         throw new IllegalArgumentException
            ("Dimension for Faure net must be > 1 and < " + MAXDIM);
      b = getSmallestPrime (dim);
      numCols = (int) Math.ceil (Math.log ((double) n)
                                 / Math.log ((double) b));
      outDigits = (int) Math.floor (Math.log ((double)(1 << (MAXBITS - 1)))
                                 / Math.log ((double)b));
      outDigits = Math.max (outDigits, numCols);
      numRows = outDigits;
      init (b, numCols, numRows, outDigits, dim);
   }



   public String toString() {
      StringBuffer sb = new StringBuffer ("Faure sequence:" +
                  PrintfFormat.NEWLINE);
      sb.append (super.toString());
      return sb.toString();
   }


   public void extendSequence (int k) {
      init (b, k, numRows, outDigits, dim);
   }


   // Fills up the generator matrices in genMat for a Faure sequence.
   // See Glasserman (2004), \cite{fGLA04a}, page 301.
   private void initGenMat() {
      int j, c, l;
      // Initialize C_0 to the identity (for first coordinate).
      for (c = 0; c < numCols; c++) {
         for (l = 0; l < numRows; l++)
            genMat[c][l] = 0;
         genMat[c][c] = 1;
      }
      // Compute C_1, ... ,C_{dim-1}.
      for (j = 1; j < dim; j++) {
         genMat[j*numCols][0] = 1;
         for (c = 1; c < numCols; c++) {
            genMat[j*numCols+c][c] = 1;
            genMat[j*numCols+c][0] = (j * genMat[j*numCols+c-1][0]) % b;
         }
         for (c = 2; c < numCols; c++) {
            for (l = 1; l < c; l++)
               genMat[j*numCols+c][l] = (genMat[j*numCols+c-1][l-1]
                                        + j * genMat[j*numCols+c-1][l]) % b;
         }
         for (c = 0; c < numCols; c++)
            for (l = c+1; l < numRows; l++)
               genMat[j*numCols+c][l] = 0;
      }
/*
      for (j = 0; j < dim; j++) {
     for (l = 0; l < numRows; l++) {
         for (c = 0; c < numCols; c++)
            System.out.print ("  " + genMat[j*numCols+c][l]);
       System.out.println ("");
      }
        System.out.println ("");
  }
*/
   }

/*
   // Fills up the generator matrices in genMat for a Faure net.
   // See Glasserman (2004), \cite{fGLA04a}, page 301.
   protected void initGenMatNet() {
      int j, c, l, start;
      // Initialize C_0 to the reflected identity (for first coordinate).
      for (c = 0; c < numCols; c++) {
         for (l = 0; l < numRows; l++)
            genMat[c][l] = 0;
         genMat[c][numCols-c-1] = 1;
      }
      // Initialize C_1 to the identity (for second coordinate).
      for (c = 0; c < numCols; c++) {
         for (l = 0; l < numRows; l++)
            genMat[numCols+c][l] = 0;
         genMat[numCols+c][c] = 1;
      }
      // Compute C_2, ... ,C_{dim-1}.
      for (j = 2; j < dim; j++) {
         start = j * numCols;
         genMat[start][0] = 1;
         for (c = 1; c < numCols; c++) {
            genMat[start+c][c] = 1;
            genMat[start+c][0] = ((j-1) * genMat[start+c-1][0]) % b;
         }
         for (c = 2; c < numCols; c++) {
            for (l = 1; l < c; l++)
               genMat[start+c][l] = (genMat[start+c-1][l-1]
                                     + (j-1) * genMat[start+c-1][l]) % b;
         }
         for (c = 0; c < numCols; c++)
            for (l = c+1; l < numRows; l++)
               genMat[start+c][l] = 0;
      }
   }
*/

   // Returns the smallest prime larger or equal to d.
   private int getSmallestPrime (int d) {
      return primes[d-1];
   }

   // Gives the appropriate prime base for each dimension.
   // Perhaps should be internal to getPrime, and non-static, to avoid
   // wasting time and memory when this array is not needed ???
   static final int primes[] =
      {2,2,3,5,5,7,7,11,11,11,11,13,13,17,17,17,17,19,19,23,
     23,23,23,29,29,29,29,29,29,31,31,37,37,37,37,37,37,41,41,41,
     41,43,43,47,47,47,47,53,53,53,53,53,53,59,59,59,59,59,59,61,
     61,67,67,67,67,67,67,71,71,71,71,73,73,79,79,79,79,79,79,83,
     83,83,83,89,89,89,89,89,89,97,97,97,97,97,97,97,97,101,101,101,
     101,103,103,107,107,107,107,109,109,113,113,113,113,127,127,127,127,127,127,127,
     127,127,127,127,127,127,127,131,131,131,131,137,137,137,137,137,137,139,139,149,
     149,149,149,149,149,149,149,149,149,151,151,157,157,157,157,157,157,163,163,163,
     163,163,163,167,167,167,167,173,173,173,173,173,173,179,179,179,179,179,179,181,
     181,191,191,191,191,191,191,191,191,191,191,193,193,197,197,197,197,199,199,211,
     211,211,211,211,211,211,211,211,211,211,211,223,223,223,223,223,223,223,223,223,
     223,223,223,227,227,227,227,229,229,233,233,233,233,239,239,239,239,239,239,241,
     241,251,251,251,251,251,251,251,251,251,251,257,257,257,257,257,257,263,263,263,
     263,263,263,269,269,269,269,269,269,271,271,277,277,277,277,277,277,281,281,281,
     281,283,283,293,293,293,293,293,293,293,293,293,293,307,307,307,307,307,307,307,
     307,307,307,307,307,307,307,311,311,311,311,313,313,317,317,317,317,331,331,331,
     331,331,331,331,331,331,331,331,331,331,331,337,337,337,337,337,337,347,347,347,
     347,347,347,347,347,347,347,349,349,353,353,353,353,359,359,359,359,359,359,367,
     367,367,367,367,367,367,367,373,373,373,373,373,373,379,379,379,379,379,379,383,
     383,383,383,389,389,389,389,389,389,397,397,397,397,397,397,397,397,401,401,401,
     401,409,409,409,409,409,409,409,409,419,419,419,419,419,419,419,419,419,419,421,
     421,431,431,431,431,431,431,431,431,431,431,433,433,439,439,439,439,439,439,443,
     443,443,443,449,449,449,449,449,449,457,457,457,457,457,457,457,457,461,461,461,
     461,463,463,467,467,467,467,479,479,479,479,479,479,479,479,479,479,479,479,487,
     487,487,487,487,487,487,487,491,491,491,491,499,499,499,499,499,499,499,499,503};

}

