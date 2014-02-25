

/*
 * Class:        RadicalInverse
 * Description:  Implements radical inverses of integers in an arbitrary basis
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
 * This class implements basic methods for working with radical
 * inverses of integers in an arbitrary basis <SPAN CLASS="MATH"><I>b</I></SPAN>.
 * These methods are used in classes that implement
 * point sets and sequences based on the van der Corput sequence
 * (the Hammersley nets and the Halton sequence, for example).
 * 
 * <P>
 * We recall that for a <SPAN CLASS="MATH"><I>k</I></SPAN>-digit integer <SPAN CLASS="MATH"><I>i</I></SPAN> whose digital 
 * <SPAN CLASS="MATH"><I>b</I></SPAN>-ary expansion is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>i</I> = <I>a</I><SUB>0</SUB> + <I>a</I><SUB>1</SUB><I>b</I> + ... + <I>a</I><SUB>k-1</SUB><I>b</I><SUP>k-1</SUP>,
 * </DIV><P></P>
 * the <SPAN  CLASS="textit">radical inverse</SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN> is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#968;</I><SUB>b</SUB>(<I>i</I>) = <I>a</I><SUB>0</SUB><I>b</I><SUP>-1</SUP> + <I>a</I><SUB>1</SUB><I>b</I><SUP>-2</SUP> + <SUP> ... </SUP> + <I>a</I><SUB>k-1</SUB><I>b</I><SUP>-k</SUP>.
 * </DIV><P></P>
 * The <SPAN  CLASS="textit">van der Corput sequence in base <SPAN CLASS="MATH"><I>b</I></SPAN></SPAN> is the sequence
 * 
 * <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(0), <I>&#968;</I><SUB>b</SUB>(1), <I>&#968;</I><SUB>b</SUB>(2),...</SPAN> 
 * 
 * <P>
 * Note that <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN> cannot always be represented exactly
 * as a floating-point number on the computer (e.g., if <SPAN CLASS="MATH"><I>b</I></SPAN> is not 
 * a power of two).  For an exact representation, one can use the integer 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>b</I><SUP>k</SUP><I>&#968;</I><SUB>b</SUB>(<I>i</I>) = <I>a</I><SUB>k-1</SUB> + <SUP> ... </SUP> + <I>a</I><SUB>1</SUB><I>b</I><SUP>k-2</SUP> + <I>a</I><SUB>0</SUB><I>b</I><SUP>k-1</SUP>,
 * </DIV><P></P>
 * which we called the <SPAN  CLASS="textit">integer radical inverse</SPAN> representation.
 * This representation is simply a mirror image of the digits of the 
 * usual <SPAN CLASS="MATH"><I>b</I></SPAN>-ary representation of <SPAN CLASS="MATH"><I>i</I></SPAN>.
 * 
 * <P>
 * It is common practice to permute locally the values of the
 * van der Corput sequence.  One way of doing this is to apply a
 * permutation to the digits of <SPAN CLASS="MATH"><I>i</I></SPAN> before computing <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN>. 
 * That is, for a permutation <SPAN CLASS="MATH"><I>&#960;</I></SPAN> of the digits 
 * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}</SPAN>, 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#968;</I><SUB>b</SUB>(<I>i</I>) = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>-r-1</SUP>
 * </DIV><P></P>
 * is replaced by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>&#960;</I>(<I>a</I><SUB>r</SUB>)<I>b</I><SUP>-r-1</SUP>.
 * </DIV><P></P>
 * Applying such a permutation only changes the order in which the 
 * values of <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN> are enumerated.  For every integer <SPAN CLASS="MATH"><I>k</I></SPAN>, the first
 * <SPAN CLASS="MATH"><I>b</I><SUP>k</SUP></SPAN> values that are enumerated remain the same (they are the values
 * of <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN> for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,..., <I>b</I><SUP>k</SUP> - 1</SPAN>), but they are enumerated in a 
 * different order.  Often, different permutations <SPAN CLASS="MATH"><I>&#960;</I></SPAN> will be applied
 * for different coordinates of a point set.
 * 
 * <P>
 * The permutation <SPAN CLASS="MATH"><I>&#960;</I></SPAN> can be deterministic or random. One (deterministic)
 *  possibility implemented here is the Faure permutation <SPAN CLASS="MATH"><I>&#963;</I><SUB>b</SUB></SPAN> of
 *  
 * <SPAN CLASS="MATH">{0,..., <I>b</I> - 1}</SPAN> defined as follows.
 * For <SPAN CLASS="MATH"><I>b</I> = 2</SPAN>, take 
 * <SPAN CLASS="MATH"><I>&#963;</I> = <I>I</I></SPAN>, the identical permutation. For <SPAN  CLASS="textit">even</SPAN>
 *  <SPAN CLASS="MATH"><I>b</I> = 2<I>c</I> &gt; 2</SPAN>,  take 
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>2<I>&#964;</I>[<I>i</I>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<I>i</I> = 0, 1,&#8230;, <I>c</I> - 1</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I> + <I>c</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>2<I>&#964;</I>[<I>i</I>] + 1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<I>i</I> = 0, 1,&#8230;, <I>c</I> - 1</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 *  
 *  where <SPAN CLASS="MATH"><I>&#964;</I>[<I>i</I>]</SPAN> is the Faure 
 * permutation for base <SPAN CLASS="MATH"><I>c</I></SPAN>.  For <SPAN  CLASS="textit">odd</SPAN> <SPAN CLASS="MATH"><I>b</I> = 2<I>c</I> + 1</SPAN>, 
 *  take 
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>c</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>c</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>&#964;</I>[<I>i</I>],&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if 0&nbsp;&lt;=&nbsp;<I>&#964;</I>[<I>i</I>] &lt; <I>c</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>&#964;</I>[<I>i</I>] + 1,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>c</I>&nbsp;&lt;=&nbsp;<I>&#964;</I>[<I>i</I>] &lt; 2<I>c</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 *  
 * for 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>c</I></SPAN>, and take
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>&#964;</I>[<I>i</I> - 1],&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if 0&nbsp;&lt;=&nbsp;<I>&#964;</I>[<I>i</I> - 1] &lt; <I>c</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>&#963;</I>[<I>i</I>]</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>&#964;</I>[<I>i</I> - 1] + 1,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>c</I>&nbsp;&lt;=&nbsp;<I>&#964;</I>[<I>i</I> - 1] &lt; 2<I>c</I></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 *  
 * for  
 * <SPAN CLASS="MATH"><I>c</I> &lt; <I>i</I>&nbsp;&lt;=&nbsp;2<I>c</I></SPAN>, and where <SPAN CLASS="MATH"><I>&#964;</I>[<I>i</I>]</SPAN> is the Faure 
 * permutation for base <SPAN CLASS="MATH"><I>c</I></SPAN>. The Faure permutations give very small 
 * discrepancies (amongst the best known today) for small bases.
 * 
 */
public class RadicalInverse  {
   private static final int NP = 168;     // First NP primes in table below.
   private static final int PLIM = 1000;  // NP primes < PLIM

   // The first NP prime numbers
   private static final int[] PRIMES = { 
    2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67,
    71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 
    149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 
    227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 
    307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 
    389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 
    467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 
    571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 
    653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 
    751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 
    853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 
    947, 953, 967, 971, 977, 983, 991, 997 };

    // Multiplicative factors for permutations as proposed by Faure & Lemieux (2008).
    // The index corresponds to the coordinate.
    private static final int[] FAURE_LEMIEUX_FACTORS = {
      1, 1, 3, 3, 4, 9, 7, 5, 9, 18, 18, 8, 13, 31, 9, 19, 36, 33, 21, 44, 43, 
      61, 60, 56, 26, 71, 32, 77, 26, 95, 92, 47, 29, 61, 57, 69, 115, 63, 92, 
      31, 104, 126, 50, 80, 55, 152, 114, 80, 83, 97, 95, 150, 148, 55, 80, 192, 
      71, 76, 82, 109, 105, 173, 58, 143, 56, 177, 203, 239, 196, 143, 278, 227, 
      87, 274, 264, 84, 226, 163, 231, 177, 95, 116, 165, 131, 156, 105, 188, 
      142, 105, 125, 269, 292, 215, 182, 294, 152, 148, 144, 382, 194, 346, 323, 
      220, 174, 133, 324, 215, 246, 159, 337, 254, 423, 484, 239, 440, 362, 464, 
      376, 398, 174, 149, 418, 306, 282, 434, 196, 458, 313, 512, 450, 161, 315, 
      441, 549, 555, 431, 295, 557, 172, 343, 472, 604, 297, 524, 251, 514, 385, 
      531, 663, 674, 255, 519, 324, 391, 394, 533, 253, 717, 651, 399, 596, 676, 
      425, 261, 404, 691, 604, 274, 627, 777, 269, 217, 599, 447, 581, 640, 666, 
      595, 669, 686, 305, 460, 599, 335, 258, 649, 771, 619, 666, 669, 707, 737, 
      854, 925, 818, 424, 493, 463, 535, 782, 476, 451, 520, 886, 340, 793, 390, 
      381, 274, 500, 581, 345, 363, 1024, 514, 773, 932, 556, 954, 793, 294, 
      863, 393, 827, 527, 1007, 622, 549, 613, 799, 408, 856, 601, 1072, 938, 
      322, 1142, 873, 629, 1071, 1063, 1205, 596, 973, 984, 875, 918, 1133, 
      1223, 933, 1110, 1228, 1017, 701, 480, 678, 1172, 689, 1138, 1022, 682, 
      613, 635, 984, 526, 1311, 459, 1348, 477, 716, 1075, 682, 1245, 401, 774, 
      1026, 499, 1314, 743, 693, 1282, 1003, 1181, 1079, 765, 815, 1350, 1144, 
      1449, 718, 805, 1203, 1173, 737, 562, 579, 701, 1104, 1105, 1379, 827, 
      1256, 759, 540, 1284, 1188, 776, 853, 1140, 445, 1265, 802, 932, 632, 
      1504, 856, 1229, 1619, 774, 1229, 1300, 1563, 1551, 1265, 905, 1333, 493, 
      913, 1397, 1250, 612, 1251, 1765, 1303, 595, 981, 671, 1403, 820, 1404, 
      1661, 973, 1340, 1015, 1649, 855, 1834, 1621, 1704, 893, 1033, 721, 1737, 
      1507, 1851, 1006, 994, 923, 872, 1860
    };

   private static final int NRILIM = 1000;  // For nextRadicalInverse
   private int b;                     // Base
   private double invb;               // 1/b
   private double logb;               // natural log(b)
   private int JMAX;                  // b^JMAX = 2^32
   private int co;                    // Counter for nextRadicalInverse
   private double xx;                 // Current value of x
   private long ix;                   // xx = RadicalInverse (ix)
/*
   // For Struckmeier's algorithm
   private static final int PARTITION_MAX = 54; // Size of partitionL, bi
   private int partM;                 // Effective size of partitionL, bi
   private double[] bi;               // bi[i] = (b + 1)/b^i - 1
   private double[] partitionL;       // L[i] = 1 - 1/b^i
               // Boundaries of Struckmeier partitions Lkp of [0, 1]
*/




   /**
    * Initializes the  base of this object  to <SPAN CLASS="MATH"><I>b</I></SPAN>
    *   and its first value of <SPAN CLASS="MATH"><I>x</I></SPAN> to <TT>x0</TT>.
    * 
    * @param b Base
    * 
    *    @param x0 Initial value of x
    * 
    */
   public RadicalInverse (int b, double x0) {
      co = 0;
      this.b = b;
      invb = 1.0 / b;
      logb = Math.log (b);
      JMAX = (int) (32.0 * 0.69314718055994530941 / logb);
      xx = x0;
      ix = computeI (x0);
//      initStruckmeier (b);
   }

   private long computeI (double x) {
      // Compute i such that x = RadicalInverse (i).
      int[] digits = new int[JMAX];              // Digits of x
      int j;
      for (j = 0; (j < JMAX) && (x > 0.5e-15); j++) {
         x *= b;
         digits[j] = (int) x;
         x -= digits[j];
      }
      long i = 0;
      for (j = JMAX - 1; j >= 0; j--) {
         i = i * b + digits[j];
      }
      return i;
   }

   /**
    * Provides an elementary method for obtaining the first <SPAN CLASS="MATH"><I>n</I></SPAN> prime
    *    numbers larger than 1. 
    *    Creates and returns an array that contains
    *    these numbers.  This is useful for determining the prime bases for
    *    the different coordinates of the Halton sequence and Hammersley nets.
    *  
    * @param n number of prime numbers to return
    * 
    *    @return an array with the first <TT>n</TT> prime numbers
    * 
    */
   public static int[] getPrimes (int n)  {
      // Allocates an array of size n filled with the first n prime
      // numbers. n must be positive (n > 0). Routine may fail if not enough
      // memory for the array is available. The first prime number is 2. 
      int i;
      boolean moreTests;
      int[] prime = new int[n];

      int n1 = Math.min (NP, n);
      for (i = 0; i < n1; i++)
         prime[i] = PRIMES[i];
      if (NP < n) {
         i = NP;
         for (int candidate = PLIM + 1; i < n; candidate += 2) {
             prime[i] = candidate;
             for (int j = 1; (moreTests = prime[j] <= candidate / prime[j])
                     && ((candidate % prime[j]) > 0); j++);
             if (! moreTests)
                 i++;
         }
      }
      return prime;
   }
 

   /**
    * Computes the radical inverse of <SPAN CLASS="MATH"><I>i</I></SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
    *    If 
    * <SPAN CLASS="MATH"><I>i</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>r</SUP></SPAN>, the method computes and returns
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>x</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>-r-1</SUP>.
    * </DIV><P></P>
    *  
    * @param b base used for the operation
    * 
    *    @param i the value for which the radical inverse will be computed
    * 
    *    @return the radical inverse of <TT>i</TT> in base <TT>b</TT>
    * 
    */
   public static double radicalInverse (int b, long i)  {
      double digit, radical, inverse;
      digit = radical = 1.0 / (double) b;
      for (inverse = 0.0; i > 0; i /= b) {
         inverse += digit * (double) (i % b);
         digit *= radical;
      }
      return inverse;
   }
 

   /**
    * Computes the radical inverse of <SPAN CLASS="MATH"><I>x</I></SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * If <SPAN CLASS="MATH"><I>x</I></SPAN> has more decimals in base <SPAN CLASS="MATH"><I>b</I></SPAN> than <SPAN CLASS="MATH">log<SUB>b</SUB></SPAN>(<TT>Long.MAX_VALUE</TT>),
    * it is truncated to its minimum precision in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
    *    If 
    * <SPAN CLASS="MATH"><I>x</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>-r-1</SUP></SPAN>, the method computes and returns
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>i</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>r</SUP>.
    * </DIV><P></P>
    *  
    * @param b base used for the operation
    * 
    *    @param x the value for which the radical inverse will be computed
    * 
    *    @return the radical inverse of <TT>x</TT> in base <TT>b</TT>
    * 
    */
   public static int radicalInverseInteger (int b, double x)  {
      int digit = 1;
      int inverse = 0;
      int precision = Integer.MAX_VALUE / (2 * b * b);
      while (x > 0 && inverse < precision) {
        int p = digit * b;
        double y = Math.floor(x * p);
        inverse += digit * (int)y;
        x -= y / (double)p;
        digit *= b;
      }
      return inverse;
   }
 

   /**
    * Computes the radical inverse of <SPAN CLASS="MATH"><I>x</I></SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * If <SPAN CLASS="MATH"><I>x</I></SPAN> has more decimals in base <SPAN CLASS="MATH"><I>b</I></SPAN> than <SPAN CLASS="MATH">log<SUB>b</SUB></SPAN>(<TT>Long.MAX_VALUE</TT>),
    * it is truncated to its minimum precision in base <SPAN CLASS="MATH"><I>b</I></SPAN>.
    *    If 
    * <SPAN CLASS="MATH"><I>x</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>-r-1</SUP></SPAN>, the method computes and returns
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>i</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>r</SUP>.
    * </DIV><P></P>
    *  
    * @param b base used for the operation
    * 
    *    @param x the value for which the radical inverse will be computed
    * 
    *    @return the radical inverse of <TT>x</TT> in base <TT>b</TT>
    * 
    */
   public static long radicalInverseLong (int b, double x)  {
      long digit = 1;
      long inverse = 0;
      long precision = Long.MAX_VALUE / (b * b * b);
      while (x > 0 && inverse < precision) {
        long p = digit * b;
        double y = Math.floor(x * p);
        inverse += digit * (long)y;
        x -= y / (double)p;
        digit *= b;
      }
      return inverse;
   }
 

   /**
    * A fast method that incrementally computes the radical inverse <SPAN CLASS="MATH"><I>x</I><SUB>i+1</SUB></SPAN> 
    *    in base <SPAN CLASS="MATH"><I>b</I></SPAN> from <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> = <TT>x</TT> = <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN>,  using addition with <EM>rigthward carry</EM>.
    *    The parameter <TT>invb</TT> is equal to <SPAN CLASS="MATH">1/<I>b</I></SPAN>.
    *    Using long incremental streams (i.e., calling this method several times
    *    in a row) cause increasing inaccuracy in <SPAN CLASS="MATH"><I>x</I></SPAN>. Thus the user should
    *    recompute the radical inverse directly by calling
    *    {@link #radicalInverse radicalInverse} every once in a while (i.e. in every few
    *    thousand  calls).
    *  
    * @param invb <SPAN CLASS="MATH">1/<I>b</I></SPAN> where <SPAN CLASS="MATH"><I>b</I></SPAN> is the base
    * 
    *    @param x the inverse <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN>
    * 
    *    @return the radical inverse <SPAN CLASS="MATH"><I>x</I><SUB>i+1</SUB></SPAN>
    * 
    */
   public static double nextRadicalInverse (double invb, double x)  {
      // Calculates the next radical inverse from x in base b.
      // Repeated application causes a loss of accuracy.
      // Note that x can be any number from [0,1).

      final double ALMOST_ONE = 1.0 - 1e-10;
      double nextInverse = x + invb;
      if (nextInverse < ALMOST_ONE)
         return nextInverse;
      else {
         double digit1 = invb;
         double digit2 = invb * invb;
         while (x + digit2 >= ALMOST_ONE) {
            digit1 = digit2;
            digit2 *= invb;
         }
         return x + (digit1 - 1.0) + digit2;
      }
   }



   /**
    * A fast method that incrementally computes the radical inverse <SPAN CLASS="MATH"><I>x</I><SUB>i+1</SUB></SPAN> 
    *    in base <SPAN CLASS="MATH"><I>b</I></SPAN> from <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN> = <SPAN CLASS="MATH"><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN>,
    *    using addition with <EM>rigthward carry</EM> as described in
    *  Wang and Hickernell.
    *    Since using long incremental streams (i.e., calling this method several
    *    times in a row) cause increasing inaccuracy in <SPAN CLASS="MATH"><I>x</I></SPAN>, the method
    *    recomputes the radical inverse directly from <SPAN CLASS="MATH"><I>i</I></SPAN> by calling
    *    {@link #radicalInverse radicalInverse} once in every 1000 calls.
    *  
    * @return the radical inverse <SPAN CLASS="MATH"><I>x</I><SUB>i+1</SUB></SPAN>
    * 
    * 
    */
   public double nextRadicalInverse ()  {
      // Calculates the next radical inverse from xx in base b.
      // Repeated application causes a loss of accuracy.
      // For each NRILIM calls, a direct calculation via radicalInverse
      // is inserted.

      co++;
      if (co >= NRILIM) {
         co = 0;
         ix += NRILIM;
         xx = radicalInverse (b, ix);
         return xx;
      }
      final double ALMOST_ONE = 1.0 - 1e-10;
      double nextInverse = xx + invb;
      if (nextInverse < ALMOST_ONE) {
         xx = nextInverse;
         return xx;
      } else {
         double digit1 = invb;
         double digit2 = invb * invb;
         while (xx + digit2 >= ALMOST_ONE) {
            digit1 = digit2;
            digit2 *= invb;
         }
         xx += (digit1 - 1.0) + digit2;
         return xx;
      }
   }

  
/*
   private void initStruckmeier (int b) {
      bi = new double[1 + PARTITION_MAX];
      partitionL = new double[1 + PARTITION_MAX];
      logb = Math.log (b);
      partitionL[0] = 0.0;
      bi[0] = 1.0;
      int i = 0;
      while ((i < PARTITION_MAX) && (partitionL[i] < 1.0)) {
         ++i;
         bi[i] = bi[i - 1] / b;
         partitionL[i] = 1.0 - bi[i];
      }
      partM = i - 1;

      for (i = 0; i <= partM + 1; ++i)
         bi[i] = (b + 1) * bi[i] - 1.0;
   }

   public double nextRadicalInverse (double x) {
      int k;
      if (x < partitionL[partM]) {
         k = 1;
         // Find k such:    partitionL[k-1] <= x < partitionL[k]  
         while (x >= partitionL[k])
            ++k;

      } else {           // x >= partitionL [partM]
         k = 1 + (int)(-Math.log(1.0 - x) / logb);
      }
      return x + bi[k];
   } */
 

   /**
    * Given the <SPAN CLASS="MATH"><I>k</I></SPAN> <SPAN CLASS="MATH"><I>b</I></SPAN>-ary digits of <SPAN CLASS="MATH"><I>i</I></SPAN> in <TT>bdigits</TT>, returns the 
    *    <SPAN CLASS="MATH"><I>k</I></SPAN> digits of the integer radical inverse of <SPAN CLASS="MATH"><I>i</I></SPAN> in <TT>idigits</TT>.
    *    This simply reverses the order of the digits.
    *  
    * @param k number of digits in arrays
    * 
    *    @param bdigits digits in original order
    * 
    *    @param idigits digits in reverse order
    * 
    * 
    */
   public static void reverseDigits (int k, int bdigits[], int idigits[]) {
      for (int l = 0; l < k; l++)
         idigits[l] = bdigits[k-l];
   }
 

   /**
    * Computes the integer radical inverse of <SPAN CLASS="MATH"><I>i</I></SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN>,
    *    equal to 
    * <SPAN CLASS="MATH"><I>b</I><SUP>k</SUP><I>&#968;</I><SUB>b</SUB>(<I>i</I>)</SPAN> if <SPAN CLASS="MATH"><I>i</I></SPAN> has <SPAN CLASS="MATH"><I>k</I></SPAN> <SPAN CLASS="MATH"><I>b</I></SPAN>-ary digits.
    *  
    * @param b base used for the operation
    * 
    *    @param i the value for which the integer radical inverse will be computed
    * 
    *    @return the integer radical inverse of <TT>i</TT> in base <TT>b</TT>
    * 
    */
   public static int integerRadicalInverse (int b, int i)  {
      // Simply flips digits of i in base b.
      int inverse;
      for (inverse = 0; i > 0; i /= b)
         inverse = inverse * b + (i % b);
      return inverse;
   }
 

   /**
    * Given the <SPAN CLASS="MATH"><I>k</I></SPAN> digits of the integer radical inverse of <SPAN CLASS="MATH"><I>i</I></SPAN> in <TT>bdigits</TT>,
    *   in base <SPAN CLASS="MATH"><I>b</I></SPAN>, this method replaces them by the digits of the integer 
    *   radical inverse of <SPAN CLASS="MATH"><I>i</I> + 1</SPAN> and returns their number.
    *   The array must be large enough to hold this new number of digits.
    *  
    * @param b base
    * 
    *    @param k initial number of digits in arrays
    * 
    *    @param idigits digits of integer radical inverse
    * 
    *    @return new number of digits in arrays
    * 
    */
   public static int nextRadicalInverseDigits (int b, int k, int idigits[]) {
      int l;
      for (l = k-1; l >= 0; l--)
         if (idigits[l] == b-1) 
            idigits[l] = 0;
         else {
            idigits[l]++;
            return k;
         }
      if (l == 0) {
         idigits[k] = 1;
         return ++k;
      }
      return 0;
   }


   /**
    * Computes the permutations as proposed in <SPAN CLASS="MATH"><I>&#963;</I><SUB>b</SUB></SPAN> of the set
    *    
    * <SPAN CLASS="MATH">{0,&#8230;, <I>b</I> - 1}</SPAN> and puts it in array <TT>pi</TT>.
    *  
    * @param coordinate the coordinate
    * 
    *    @param pi an array of size at least <TT>b</TT>, 
    *               to be filled with the permutation
    * 
    * 
    */
   public static void getFaureLemieuxPermutation (int coordinate, int[] pi)  {
      int f = FAURE_LEMIEUX_FACTORS[coordinate];
      int b = PRIMES[coordinate];
      for (int k = 0; k < pi.length; k++)
         pi[k] = f * k % b;
   }
  

   /**
    * Computes the Faure permutation <SPAN CLASS="MATH"><I>&#963;</I><SUB>b</SUB></SPAN> of the set
    *    
    * <SPAN CLASS="MATH">{0,&#8230;, <I>b</I> - 1}</SPAN> and puts it in array <TT>pi</TT>.
    *  See the description in the introduction above.
    *  
    * @param b the base
    * 
    *    @param pi an array of size at least <TT>b</TT>, 
    *               to be filled with the permutation
    * 
    * 
    */
   public static void getFaurePermutation (int b, int[] pi)  {
      // This is a recursive implementation.  
      // Perhaps not the most efficient...
      int i;
      if (b == 2) {
         pi[0] = 0;
         pi[1] = 1;
      }
      else if ((b & 1) != 0) {
         // b is odd.
         b--;
         getFaurePermutation (b, pi);
         for (i = 0; i < b; i++)
            if (pi[i] >= b / 2)
               pi[i]++;
         for (i = b; i > b / 2; i--)
            pi[i] = pi[i - 1];
         pi[b / 2] = b / 2;
      }
      else {
         b /= 2;
         getFaurePermutation (b, pi);
         for (i = 0; i < b; i++) {
            pi[i] *= 2;
            pi[i + b] = pi[i] + 1;
         }
      }
   }  
  

   /**
    * Computes the radical inverse of <SPAN CLASS="MATH"><I>i</I></SPAN> in base <SPAN CLASS="MATH"><I>b</I></SPAN>, where the digits
    *    are permuted using the permutation <SPAN CLASS="MATH"><I>&#960;</I></SPAN>.
    *    If 
    * <SPAN CLASS="MATH"><I>i</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>a</I><SUB>r</SUB><I>b</I><SUP>r</SUP></SPAN>, the method will compute and return
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>x</I> = &sum;<SUB>r=0</SUB><SUP>k-1</SUP><I>&#960;</I>[<I>a</I><SUB>r</SUB>]<I>b</I><SUP>-r-1</SUP>.
    * </DIV><P></P>
    *  
    * @param b base <SPAN CLASS="MATH"><I>b</I></SPAN> used for the operation
    * 
    *    @param pi an array of length at least <TT>b</TT> containing the permutation
    *       used during the computation
    * 
    *    @param i the value for which the radical inverse will be computed
    * 
    *    @return the radical inverse of <TT>i</TT> in base <TT>b</TT>
    * 
    */
   public static double permutedRadicalInverse (int b, int[] pi, long i) {
      double digit, radical, inverse;
      digit = radical = 1.0 / (double) b;
      for (inverse = 0.0; i > 0; i /= b) {
         inverse += digit * (double) pi[(int)(i % b)];
         digit *= radical;
      }
      return inverse;
   }


}