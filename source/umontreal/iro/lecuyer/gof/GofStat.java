

/*
 * Class:        GofStat
 * Description:  Goodness-of-fit test statistics
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

package umontreal.iro.lecuyer.gof;
   import cern.colt.list.*;

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.probdist.*;
import java.util.Arrays;

/**
 * This class provides methods to compute several types of EDF goodness-of-fit
 * test statistics and to apply certain transformations to a set of
 * observations.  This includes the probability integral transformation
 * 
 * <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB> = <I>F</I>(<I>X</I><SUB>i</SUB>)</SPAN>, as well as the power ratio and iterated spacings
 * transformations. Here, 
 * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN> stand
 * for <SPAN CLASS="MATH"><I>N</I></SPAN> observations 
 * <SPAN CLASS="MATH"><I>U</I><SUB>0</SUB>,..., <I>U</I><SUB>N-1</SUB></SPAN> sorted by increasing order, where
 * 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>U</I><SUB>i</SUB>&nbsp;&lt;=&nbsp;1</SPAN>.
 * 
 * <P>
 * Note: This class uses the Colt library.
 * 
 */
public class GofStat {
   private GofStat() {}


   // Used in discontinuous distributions
   private static double EPSILOND = 1.0E-15;


   /**
    * Applies the transformation 
    * <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB> = <I>F</I>(<I>V</I><SUB>i</SUB>)</SPAN> for 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>N</I> - 1</SPAN>,
    *   where <SPAN CLASS="MATH"><I>F</I></SPAN> is a <EM>continuous</EM> distribution function,
    *   and returns the result as an array of length <SPAN CLASS="MATH"><I>N</I></SPAN>.
    *   <SPAN CLASS="MATH"><I>V</I></SPAN> represents the <SPAN CLASS="MATH"><I>N</I></SPAN> observations contained in <TT>data</TT>,
    *   and <SPAN CLASS="MATH"><I>U</I></SPAN>, the returned transformed observations.
    *   If <TT>data</TT> contains random variables from the distribution function
    *   <TT>dist</TT>, then the result will contain uniform random variables
    *   over <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @param data array of observations to be transformed
    * 
    *    @param dist assumed distribution of the observations
    * 
    *    @return the array of transformed observations
    * 
    */
   public static DoubleArrayList unifTransform (DoubleArrayList data,
                                         ContinuousDistribution dist) {
      double[] v = data.elements();
      int n = data.size();

      double[] u = new double[n];
      for (int i = 0; i < n; i++)
         u[i] = dist.cdf (v[i]);
      return new DoubleArrayList(u);
   }


   /**
    * Applies the transformation 
    * <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB> = <I>F</I>(<I>V</I><SUB>i</SUB>)</SPAN> for 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>N</I> - 1</SPAN>,
    *    where <SPAN CLASS="MATH"><I>F</I></SPAN> is a <EM>discrete</EM> distribution function,
    *    and returns the result as an array of length <SPAN CLASS="MATH"><I>N</I></SPAN>.
    *   <SPAN CLASS="MATH"><I>V</I></SPAN> represents the <SPAN CLASS="MATH"><I>N</I></SPAN> observations contained in <TT>data</TT>,
    *   and <SPAN CLASS="MATH"><I>U</I></SPAN>, the returned transformed observations.
    * 
    * <P>
    * Note: If <SPAN CLASS="MATH"><I>V</I></SPAN> are the values of random variables with
    *    distribution function <TT>dist</TT>, then the result will contain
    *    the values of <EM>discrete</EM> random variables distributed over the
    *    set of values taken by <TT>dist</TT>,
    *    not uniform random variables over <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @param data array of observations to be transformed
    * 
    *    @param dist assumed distribution of the observations
    * 
    *    @return the array of transformed observations
    * 
    */
   public static DoubleArrayList unifTransform (DoubleArrayList data,
                                        DiscreteDistribution dist) {
       double[] v = data.elements();
       int n = data.size();

       double[] u = new double[n];
       for (int i = 0; i < n; i++)
          u[i] = dist.cdf ((int)v[i]);
       return new DoubleArrayList (u);
   }


   /**
    * Assumes that the real-valued observations 
    * <SPAN CLASS="MATH"><I>U</I><SUB>0</SUB>,..., <I>U</I><SUB>N-1</SUB></SPAN>
    *   contained in <TT>sortedData</TT>
    *   are already sorted in increasing order and computes the differences
    *   between the successive observations. Let <SPAN CLASS="MATH"><I>D</I></SPAN> be the differences
    *   returned in <TT>spacings</TT>.
    *   The difference 
    * <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB> - <I>U</I><SUB>i-1</SUB></SPAN> is put in <SPAN CLASS="MATH"><I>D</I><SUB>i</SUB></SPAN> for
    *   <TT>n1 &lt; i &lt;= n2</TT>, whereas 
    * <SPAN CLASS="MATH"><I>U</I><SUB>n1</SUB> - <I>a</I></SPAN> is put into <SPAN CLASS="MATH"><I>D</I><SUB>n1</SUB></SPAN>
    *   and 
    * <SPAN CLASS="MATH"><I>b</I> - <I>U</I><SUB>n2</SUB></SPAN> is put into <SPAN CLASS="MATH"><I>D</I><SUB>n2+1</SUB></SPAN>.
    * The number of observations must be greater or equal than <TT>n2</TT>, we
    *   must have
    *   <TT>n1 &lt; n2</TT>, and <TT>n1</TT> and <TT>n2</TT> are greater than 0.
    *   The size of <TT>spacings</TT> will be at least <SPAN CLASS="MATH"><I>N</I> + 1</SPAN> after
    *   the call returns.
    * 
    * @param sortedData array of sorted observations
    * 
    *    @param spacings pointer to an array object that will be filled with spacings
    * 
    *    @param n1 starting index, in <TT>sortedData</TT>, of the processed observations
    * 
    *    @param n2 ending index, in <TT>sortedData</TT> of the processed observations
    * 
    *    @param a minimum value of the observations
    * 
    *    @param b maximum value of the observations
    * 
    * 
    */
   public static void diff (IntArrayList sortedData, IntArrayList spacings,
                            int n1, int n2, int a, int b) {
      if (n1 < 0 || n2 < 0 || n1 >= n2 || n2 >= sortedData.size())
         throw new IllegalArgumentException ("n1 and n2 not valid.");
      int[] u = sortedData.elements();
      int n = sortedData.size();
      if (spacings.size() <= (n2 + 2))
         spacings.setSize (n2 + 2);
      int[] d = spacings.elements();

      d[n1] = u[n1] - a;
      for (int i = n1 + 1; i <= n2; i++)
         d[i] = u[i] - u[i - 1];
      d[n2+1] = b - u[n2];
   }


   /**
    * Same as method
    *   {@link #diff diff}<TT>(IntArrayList,IntArrayList,int,int,int,int)</TT>,
    *    but for the continuous case.
    * 
    * @param sortedData array of sorted observations
    * 
    *    @param spacings pointer to an array object that will be filled with spacings
    * 
    *    @param n1 starting index of the processed observations in <TT>sortedData</TT>
    * 
    *    @param n2 ending index, in <TT>sortedData</TT> of the processed observations
    * 
    *    @param a minimum value of the observations
    * 
    *    @param b maximum value of the observations
    * 
    * 
    */
   public static void diff (DoubleArrayList sortedData,
                            DoubleArrayList spacings,
                            int n1, int n2, double a, double b) {

      if (n1 < 0 || n2 < 0 || n1 >= n2 || n2 >= sortedData.size())
         throw new IllegalArgumentException ("n1 and n2 not valid.");
      double[] u = sortedData.elements();
      int n = sortedData.size();
      if (spacings.size() <= (n2 + 2))
         spacings.setSize (n2 + 2);
      double[] d = spacings.elements();

      d[n1] = u[n1] - a;
      for (int i = n1 + 1; i <= n2; i++)
         d[i] = u[i] - u[i - 1];
      d[n2+1] = b - u[n2];
   }


   /**
    * Applies one iteration of the <EM>iterated spacings</EM>
    *    transformation.
    *    Let <SPAN CLASS="MATH"><I>U</I></SPAN> be the <SPAN CLASS="MATH"><I>N</I></SPAN> observations contained into <TT>data</TT>,
    *    and let <SPAN CLASS="MATH"><I>S</I></SPAN> be the spacings contained into <TT>spacings</TT>,
    *    Assumes that <SPAN CLASS="MATH"><I>S</I>[0..<I>N</I>]</SPAN> contains the <EM>spacings</EM>
    *    between <SPAN CLASS="MATH"><I>N</I></SPAN> real numbers 
    * <SPAN CLASS="MATH"><I>U</I><SUB>0</SUB>,..., <I>U</I><SUB>N-1</SUB></SPAN> in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>.
    *    These spacings are defined by
    *     
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>S</I><SUB>i</SUB> = <I>U</I><SUB>(i)</SUB> - <I>U</I><SUB>(i-1)</SUB>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>N</I>,
    * </DIV><P></P>
    * where <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB> = 0</SPAN>, 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(N-1)</SUB> = 1</SPAN>, and
    *    
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>,  are the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> sorted in increasing order.
    * These spacings may have been obtained by calling
    *    {@link #diff(DoubleArrayList,DoubleArrayList,int,int,double,double) diff}.
    *    This method transforms the spacings into new
    *    spacings:
    * it sorts 
    * <SPAN CLASS="MATH"><I>S</I><SUB>0</SUB>,..., <I>S</I><SUB>N</SUB></SPAN> to obtain
    *    
    * <SPAN CLASS="MATH"><I>S</I><SUB>(0)</SUB>&nbsp;&lt;=&nbsp;<I>S</I><SUB>(1)</SUB>&nbsp;&lt;=&nbsp;<I>S</I><SUB>(2)</SUB>&nbsp;&lt;=&nbsp;<SUP> ... </SUP>&nbsp;&lt;=&nbsp;<I>S</I><SUB>(N)</SUB></SPAN>,
    *    computes the weighted differences
    *   <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>S</I><SUB>0</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>(<I>N</I> + 1)<I>S</I><SUB>(0)</SUB>,</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>S</I><SUB>1</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP><I>N</I>(<I>S</I><SUB>(1)</SUB> - <I>S</I><SUB>(0)</SUB>),</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>S</I><SUB>2</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>(<I>N</I> - 1)(<I>S</I><SUB>(2)</SUB> - <I>S</I><SUB>(1)</SUB>),</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT">&nbsp;</TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>...</TD>
    * <TD>&nbsp;</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>S</I><SUB>N</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP><I>S</I><SUB>(N)</SUB> - <I>S</I><SUB>(N-1)</SUB>,</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    *    and computes 
    * <SPAN CLASS="MATH"><I>V</I><SUB>i</SUB> = <I>S</I><SUB>0</SUB> + <I>S</I><SUB>1</SUB> + <SUP> ... </SUP> + <I>S</I><SUB>i</SUB></SPAN> for 
    * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>i</I> &lt; <I>N</I></SPAN>.
    *    It then returns 
    * <SPAN CLASS="MATH"><I>S</I><SUB>0</SUB>,..., <I>S</I><SUB>N</SUB></SPAN> in <TT>S[0..N]</TT> and
    *    
    * <SPAN CLASS="MATH"><I>V</I><SUB>1</SUB>,..., <I>V</I><SUB>N</SUB></SPAN> in <TT>V[1..N]</TT>.
    * 
    * <P>
    * Under the assumption that the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> are i.i.d. <SPAN CLASS="MATH"><I>U</I>(0, 1)</SPAN>, the new
    *   <SPAN CLASS="MATH"><I>S</I><SUB>i</SUB></SPAN> can be considered as a new set of spacings having the same
    *   distribution as the original spacings, and the <SPAN CLASS="MATH"><I>V</I><SUB>i</SUB></SPAN> are a new sample
    *   of i.i.d. <SPAN CLASS="MATH"><I>U</I>(0, 1)</SPAN> random variables, sorted by increasing order.
    * 
    * <P>
    * This transformation is useful to detect <EM>clustering</EM> in a data
    *   set: A pair of observations that are close to each other is transformed
    *   into an observation close to zero.  A data set with unusually clustered
    *   observations is thus transformed to a data set with an
    *   accumulation of observations near zero, which is easily detected by
    *   the Anderson-Darling GOF test.
    *  
    * @param data array of observations
    * 
    *    @param spacings spacings between the observations, will be filled with the new spacings
    * 
    * 
    */
   public static void iterateSpacings (DoubleArrayList data,
                                       DoubleArrayList spacings) {
      if (spacings.size() < (data.size()+1))
         throw new IllegalArgumentException ("Invalid array sizes.");
      double[] v = data.elements();
      spacings.quickSortFromTo (0, data.size());
      double[] s = spacings.elements();
      int n = data.size();

      for (int i = 0; i < n; i++)
         s[n - i] = (i + 1) *  (s[n - i] - s[n - i - 1]);
      s[0] = (n + 1) * s[0];
      v[0] = s[0];
      for (int i = 1; i < n; i++)
         v[i] = v[i - 1] + s[i];
   }


   /**
    * Applies the <EM>power ratios</EM> transformation <SPAN CLASS="MATH"><I>W</I></SPAN>.
    *    Let <SPAN CLASS="MATH"><I>U</I></SPAN> be the <SPAN CLASS="MATH"><I>N</I></SPAN> observations contained into <TT>sortedData</TT>.
    *    Assumes that <SPAN CLASS="MATH"><I>U</I></SPAN> contains <SPAN CLASS="MATH"><I>N</I></SPAN> real numbers
    *    
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN> from the interval <SPAN CLASS="MATH">[0, 1]</SPAN>,
    *    already sorted in increasing order, and computes the transformations:
    *      
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>U'</I><SUB>i</SUB> = (<I>U</I><SUB>(i)</SUB>/<I>U</I><SUB>(i+1)</SUB>)<SUP>i+1</SUP>,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<I>i</I> = 0,..., <I>N</I> - 1,
    * </DIV><P></P>
    * with 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(N)</SUB> = 1</SPAN>.
    *    These <SPAN CLASS="MATH"><I>U'</I><SUB>i</SUB></SPAN> are sorted in increasing order and put back in
    *    <TT>U[1...N]</TT>.
    *    If the <SPAN CLASS="MATH"><I>U</I><SUB>(i)</SUB></SPAN> are i.i.d. <SPAN CLASS="MATH"><I>U</I>(0, 1)</SPAN> sorted by increasing order,
    *    then the <SPAN CLASS="MATH"><I>U'</I><SUB>i</SUB></SPAN> are also i.i.d. <SPAN CLASS="MATH"><I>U</I>(0, 1)</SPAN>.
    * 
    * <P>
    * This transformation is useful to detect clustering, as explained in
    *   {@link #iterateSpacings(DoubleArrayList,DoubleArrayList) iterateSpacings},
    *    except that here a pair of
    *   observations close to each other is transformed
    *   into an observation close to 1.
    *   An accumulation of observations near 1 is also easily detected by
    *   the Anderson-Darling GOF test.
    *  
    * @param sortedData sorted array of real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    *       that will be overwritten with the transformed observations
    * 
    */
   public static void powerRatios (DoubleArrayList sortedData) {

      double[] u = sortedData.elements();
      int n = sortedData.size();

      for (int i = 0; i < (n-1); i++) {
         if (u[i + 1] == 0.0 || u[i + 1] == -0.0)
            u[i] = 1.0;
         else
            u[i] = Math.pow (u[i] / u[i + 1], (double) i + 1);
      }

      u[n-1] = Math.pow (u[n-1], (double) n);
      sortedData.quickSortFromTo (0, sortedData.size() - 1);
   }


   /**
    * This class helps managing the partitions of possible outcomes
    * into categories for applying chi-square tests.
    * It permits one to automatically regroup categories to make sure that
    * the expected number of observations in each category is large enough.
    * To use this facility, one must first construct an
    * <TT>OutcomeCategoriesChi2</TT> object by passing to the constructor
    * the expected number of observations for each original category.
    * Then, calling the method {@link #regroupCategories regroupCategories} will regroup
    * categories in a way that the expected number of observations in each
    * category reaches a given threshold <TT>minExp</TT>.
    * Experts in statistics recommend that <TT>minExp</TT> be always larger
    * than or equal to 5 for the chi-square test to be valid. Thus,
    * <TT>minExp</TT> = 10 is a safe value to use.
    * After the call, <TT>nbExp</TT> gives the expected numbers in the new
    * categories and <TT>loc[i]</TT> gives the relocation of category <SPAN CLASS="MATH"><I>i</I></SPAN>,
    * for each <SPAN CLASS="MATH"><I>i</I></SPAN>.  That is, <TT>loc[i] = j</TT> means that category <SPAN CLASS="MATH"><I>i</I></SPAN> has
    * been merged with category <SPAN CLASS="MATH"><I>j</I></SPAN> because its original expected number was
    * too small, and <TT>nbExp[i]</TT> has been added to <TT>nbExp[j]</TT>
    * and then set to zero.
    * In this case, all observations that previously belonged
    * to category <SPAN CLASS="MATH"><I>i</I></SPAN> are redirected to category <SPAN CLASS="MATH"><I>j</I></SPAN>.
    * The variable <TT>nbCategories</TT> gives the final number of categories,
    * <TT>smin</TT> contains the new index of the lowest category,
    * and <TT>smax</TT> the new index of the highest category.
    * 
    */
   public static class OutcomeCategoriesChi2 {


   /**
    * Total number of categories. 
    * 
    */
      public int nbCategories;


   /**
    * Minimum index for valid expected numbers
    *    in the array <TT>nbExp</TT>.
    * 
    */
      public int smin;


   /**
    * Maximum index for valid expected numbers
    *    in the array <TT>nbExp</TT>.
    * 
    */
      public int smax;


   /**
    * Expected number of observations for each category. 
    * 
    */
      public double[] nbExp;


   /**
    * <TT>loc[i]</TT> gives the relocation of the category <TT>i</TT> in
    *    the <TT>nbExp</TT> array. 
    * 
    */
      public int[] loc;


   /**
    * Constructs an <TT>OutcomeCategoriesChi2</TT> object
    *   using the array <TT>nbExp</TT> for the number of expected observations in
    *   each category. The <TT>smin</TT> and <TT>smax</TT> fields are set to 0 and
    *   <SPAN CLASS="MATH">(<I>n</I> - 1)</SPAN> respectively, where <SPAN CLASS="MATH"><I>n</I></SPAN> is  the length of array <TT>nbExp</TT>.
    *   The <TT>loc</TT> field is set such that <TT>loc[i]=i</TT> for each <TT>i</TT>.
    *   The field <TT>nbCategories</TT> is set to <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @param nbExp array of expected observations for each category
    * 
    * 
    */
      public OutcomeCategoriesChi2 (double[] nbExp) {
         this.nbExp = nbExp;
         smin = 0;
         smax = nbExp.length - 1;
         nbCategories = nbExp.length;
         loc = new int[nbExp.length];
         for (int i = 0; i < nbExp.length; i++)
            loc[i] = i;
      }


   /**
    * Constructs an <TT>OutcomeCategoriesChi2</TT> object using the
    *   given <TT>nbExp</TT> expected observations array.  Only the expected
    *   numbers from the <TT>smin</TT> to <TT>smax</TT> (inclusive) indices will be
    *   considered valid. The <TT>loc</TT> field is set such that <TT>loc[i]=i</TT>
    *   for each <TT>i</TT> in the interval <TT>[smin, smax]</TT>. All <TT>loc[i]</TT>
    *   for <TT>i <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;</SPAN> smin</TT> are set to <TT>smin</TT>, and all <TT>loc[i]</TT> for
    *   <TT>i <SPAN CLASS="MATH">&nbsp;&gt;=&nbsp;</SPAN> smax</TT> are set to <TT>smax</TT>.
    *   The field <TT>nbCategories</TT> is set to (<TT>smax - smin + 1</TT>).
    * 
    * @param nbExp array of expected observations for each category
    * 
    *    @param smin Minimum index for valid expected number of observations
    * 
    *    @param smax Maximum index for valid expected number of observations
    * 
    * 
    */
      public OutcomeCategoriesChi2 (double[] nbExp, int smin, int smax) {
         this.nbExp = nbExp;
         this.smin = smin;
         this.smax = smax;
         nbCategories = smax - smin + 1;
         loc = new int[nbExp.length];
         for (int i = 0; i < smin; i++)
            loc[i] = smin;
         for (int i = smin; i < smax; i++)
            loc[i] = i;
         for (int i = smax; i < nbExp.length; i++)
            loc[i] = smax;
      }


   /**
    * Constructs an <TT>OutcomeCategoriesChi2</TT> object.
    *    The field <TT>nbCategories</TT> is set to  <TT>nbCat</TT>.
    *    
    * @param nbExp array of expected observations for each category
    * 
    *    @param smin Minimum index for valid expected number of observations
    * 
    *    @param smax Maximum index for valid expected number of observations
    * 
    *    @param loc array for which <TT>loc[i]</TT> gives the relocation of the category <TT>i</TT>
    * 
    * 
    */
      public OutcomeCategoriesChi2 (double[] nbExp, int[] loc,
                                    int smin, int smax, int nbCat) {
         this.nbExp = nbExp;
         this.smin = smin;
         this.smax = smax;
         this.nbCategories = nbCat;
         this.loc = loc;
      }


   /**
    * Regroup categories as explained earlier, so that the expected
    *    number of observations in each category is at least <TT>minExp</TT>.
    *    We usually choose <TT>minExp</TT> = 10.
    *  
    * @param minExp mininum number of expected observations in each category
    * 
    * 
    */
      public void regroupCategories (double minExp) {
         int s0 = 0, j;
         double somme;

         nbCategories = 0;
         int s = smin;
         while (s <= smax) {
            /* Merge categories to ensure that the number expected
               in each category is >= minExp. */
            if (nbExp[s] < minExp) {
               s0 = s;
               somme = nbExp[s];
               while (somme < minExp && s < smax) {
                  nbExp[s] = 0.0;
                  ++s;
                  somme += nbExp[s];
               }
               nbExp[s] = somme;
               for (j = s0; j <= s; j++)
                  loc[j] = s;

            } else
               loc[s] = s;

            ++nbCategories;
            ++s;
         }
         smin = loc[smin];

         // Special case: the last category, if nbExp < minExp
         if (nbExp[smax] < minExp) {
            if (s0 > smin)
               --s0;
            nbExp[s0] += nbExp[smax];
            nbExp[smax] = 0.0;
            --nbCategories;
            for (j = s0 + 1; j <= smax; j++)
               loc[j] = s0;
            smax = s0;
         }
         if (nbCategories <= 1)
           throw new IllegalStateException ("nbCategories < 2");
         }


   /**
    * Provides a report on the categories.
    * 
    * @return the categories represented as a string
    * 
    */
      public String toString() {
         int s, s0;
         double somme;
         final double EPSILON = 5.0E-16;
         StringBuffer sb = new StringBuffer();
         sb.append ("-----------------------------------------------" +
                     PrintfFormat.NEWLINE);
         if (nbExp[smin] < EPSILON)
            sb.append ("Only expected numbers larger than " +
                       PrintfFormat.g (6, 1, EPSILON) + "  are printed" +
                                       PrintfFormat.NEWLINE);
         sb.append ("Number of categories: " +
               PrintfFormat.d (4, nbCategories) + PrintfFormat.NEWLINE +
               "Expected numbers per category:" + PrintfFormat.NEWLINE +
                PrintfFormat.NEWLINE + "Category s      nbExp[s]" +
                PrintfFormat.NEWLINE);

         // Do not print values < EPSILON
         s = smin;
         while (nbExp[s] < EPSILON)
            s++;
         int s1 = s;

         s = smax;
         while (nbExp[s] < EPSILON)
            s--;
         int s2 = s;

         somme = 0.0;
         for (s = s1 ; s <= s2; s++)
            if (loc[s] == s) {
               somme += nbExp[s];
               sb.append (PrintfFormat.d (4, s) + " " +
                          PrintfFormat.f (18, 4, nbExp[s]) +
                          PrintfFormat.NEWLINE);
            }
         sb.append (PrintfFormat.NEWLINE + "Total expected number = " +
                    PrintfFormat.f (18, 2, somme) + PrintfFormat.NEWLINE +
                    PrintfFormat.NEWLINE +
                    "The groupings:" + PrintfFormat.NEWLINE +
                    " Category s      loc[s]" + PrintfFormat.NEWLINE);
         for (s = smin; s <= smax; s++) {
            if ((s == smin) && (s > 0))
               sb.append ("<= ");
            else if ((s == smax) && (s < loc.length - 1))
               sb.append (">= ");
            else
               sb.append ("   ");
            sb.append (PrintfFormat.d (4, s) + " " +
                       PrintfFormat.d (12, loc[s]) + PrintfFormat.NEWLINE);
         }

         sb.append (PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
         return sb.toString();
      }

      }
   

   /**
    * Computes and returns the chi-square statistic for the
    *  observations <SPAN CLASS="MATH"><I>o</I><SUB>i</SUB></SPAN> in <TT>count[smin...smax]</TT>, for which the
    *  corresponding expected values <SPAN CLASS="MATH"><I>e</I><SUB>i</SUB></SPAN> are in <TT>nbExp[smin...smax]</TT>.
    *  Assuming that <SPAN CLASS="MATH"><I>i</I></SPAN> goes from 1 to <SPAN CLASS="MATH"><I>k</I></SPAN>, where <SPAN CLASS="MATH"><I>k</I> =</SPAN> <TT>smax-smin+1</TT>
    *  is the number of categories, the chi-square statistic is defined as
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>X</I><SUP>2</SUP> = &sum;<SUB>i=1</SUB><SUP>k</SUP>(<I>o</I><SUB>i</SUB> - <I>e</I><SUB>i</SUB>)<SUP>2</SUP>/<I>e</I><SUB>i</SUB>.<P>
    * </DIV><P></P>
    * Under the hypothesis that the <SPAN CLASS="MATH"><I>e</I><SUB>i</SUB></SPAN> are the correct expectations and
    *  if these <SPAN CLASS="MATH"><I>e</I><SUB>i</SUB></SPAN> are large enough, <SPAN CLASS="MATH"><I>X</I><SUP>2</SUP></SPAN> follows approximately the
    *  chi-square distribution with <SPAN CLASS="MATH"><I>k</I> - 1</SPAN> degrees of freedom.
    *  If some of the <SPAN CLASS="MATH"><I>e</I><SUB>i</SUB></SPAN> are too small, one can use
    *  <TT>OutcomeCategoriesChi2</TT> to regroup categories.
    * 
    * @param nbExp numbers expected in each category
    * 
    *    @param count numbers observed in each category
    * 
    *    @param smin index of the first valid data in <TT>count</TT> and <TT>nbExp</TT>
    * 
    *    @param smax index of the last valid data in <TT>count</TT> and <TT>nbExp</TT>
    * 
    *    @return the <SPAN CLASS="MATH"><I>X</I><SUP>2</SUP></SPAN> statistic
    * 
    */
   public static double chi2 (double[] nbExp, int[] count,
                              int smin, int smax) {
      double diff, khi = 0.0;

      for (int s = smin; s <= smax; s++) {
         if (nbExp[s] <= 0.0) {
            if (count[s] != 0)
              throw new IllegalArgumentException (
                             "nbExp[s] = 0 and count[s] > 0");
         }
         else {
            diff = count[s] - nbExp[s];
            khi += diff * diff / nbExp[s];
         }
      }
      return khi;
   }


   /**
    * Computes and returns the chi-square statistic for the
    *  observations <SPAN CLASS="MATH"><I>o</I><SUB>i</SUB></SPAN> in <TT>count</TT>, for which the
    *  corresponding expected values <SPAN CLASS="MATH"><I>e</I><SUB>i</SUB></SPAN> are in <TT>cat</TT>.
    *  This assumes that <TT>cat.regroupCategories</TT> has been called before
    *  to regroup categories in order to make sure that the expected numbers in each
    *  category are large enough for the chi-square test.
    * 
    * @param cat numbers expected in each category
    * 
    *    @param count numbers observed in each category
    * 
    *    @return the <SPAN CLASS="MATH"><I>X</I><SUP>2</SUP></SPAN> statistic
    * 
    */
   public static double chi2 (OutcomeCategoriesChi2 cat, int[] count) {
      int[] newcount = new int[1 + cat.smax];
      for (int s = cat.smin; s <= cat.smax; s++) {
         newcount[cat.loc[s]] += count[s];
      }

      double diff, khi = 0.0;

      for (int s = cat.smin; s <= cat.smax; s++) {
         if (cat.nbExp[s] > 0.0) {
            diff = newcount[s] - cat.nbExp[s];
            khi += diff * diff / cat.nbExp[s];
         }
      }
      newcount = null;
      return khi;
   }


   /**
    * Computes and returns the chi-square statistic for the
    *    observations stored in <TT>data</TT>, assuming that these observations follow
    *    the discrete distribution <TT>dist</TT>.  For <TT>dist</TT>, we assume that
    *    there is one set 
    * <SPAN CLASS="MATH"><I>S</I> = {<I>a</I>, <I>a</I> + 1,..., <I>b</I> - 1, <I>b</I>}</SPAN>, where <SPAN CLASS="MATH"><I>a</I> &lt; <I>b</I></SPAN> and <SPAN CLASS="MATH"><I>a</I>&nbsp;&gt;=&nbsp; 0</SPAN>,
    *    for which  <SPAN CLASS="MATH"><I>p</I>(<I>s</I>) &gt; 0</SPAN> if <SPAN CLASS="MATH"><I>s</I>&#8712;<I>S</I></SPAN> and <SPAN CLASS="MATH"><I>p</I>(<I>s</I>) = 0</SPAN> otherwise.
    * 
    * <P>
    * Generally, it is not possible to divide the integers in intervals satisfying
    *    
    * <SPAN CLASS="MATH"><I>nP</I>(<I>a</I><SUB>0</SUB>&nbsp;&lt;=&nbsp;<I>s</I> &lt; <I>a</I><SUB>1</SUB>) = <I>nP</I>(<I>a</I><SUB>1</SUB>&nbsp;&lt;=&nbsp;<I>s</I> &lt; <I>a</I><SUB>2</SUB>) = <SUP> ... </SUP> = <I>nP</I>(<I>a</I><SUB>j-1</SUB>&nbsp;&lt;=&nbsp;<I>s</I> &lt; <I>a</I><SUB>j</SUB>)</SPAN>
    *    for a discrete distribution, where <SPAN CLASS="MATH"><I>n</I></SPAN> is the sample size, i.e.,
    *    the number of
    *    observations stored into <TT>data</TT>.
    *    To perform a general chi-square test, the method starts
    *    from <TT>smin</TT> and finds the first non-negligible
    *    probability 
    * <SPAN CLASS="MATH"><I>p</I>(<I>s</I>)&nbsp;&gt;=&nbsp;<I>&#949;</I></SPAN>, where
    *    <SPAN CLASS="MATH"><I>&#949;</I> =</SPAN> {@link DiscreteDistributionInt#EPSILON DiscreteDistributionInt.EPSILON}.
    *    It uses <TT>smax</TT> to allocate an array storing the
    *    number of expected observations (<SPAN CLASS="MATH"><I>np</I>(<I>s</I>)</SPAN>) for each <SPAN CLASS="MATH"><I>s</I>&nbsp;&gt;=&nbsp;</SPAN> <TT>smin</TT>.
    *    Starting from <SPAN CLASS="MATH"><I>s</I> =</SPAN> <TT>smin</TT>, the <SPAN CLASS="MATH"><I>np</I>(<I>s</I>)</SPAN> terms are computed and
    *    the allocated array grows if required until a negligible probability
    *    term is found.
    *    This gives the number of expected elements for
    *    each category, where an outcome category corresponds here to
    *    an interval in which sample observations could lie.
    *    The categories are regrouped to have at least
    *    <TT>minExp</TT> observations per category.
    *    The method then counts the number of samples in each categories and calls
    *    {@link #chi2(double[],int[],int,int) chi2} to get the chi-square test
    *    statistic.  If <TT>numCat</TT> is not
    *    <TT>null</TT>, the number of categories after regrouping is returned
    *    in <TT>numCat[0]</TT>. The number of degrees of freedom is equal to
    *    <TT>numCat[0]-1</TT>. We usually choose <TT>minExp</TT> = 10.
    * 
    * @param data observations, not necessarily sorted
    * 
    *    @param dist assumed probability distribution
    * 
    *    @param smin estimated minimum value of <SPAN CLASS="MATH"><I>s</I></SPAN> for which <SPAN CLASS="MATH"><I>p</I>(<I>s</I>) &gt; 0</SPAN>
    * 
    *    @param smax estimated maximum value of <SPAN CLASS="MATH"><I>s</I></SPAN> for which <SPAN CLASS="MATH"><I>p</I>(<I>s</I>) &gt; 0</SPAN>
    * 
    *    @param minExp minimum number of expected observations in each
    *     interval
    * 
    *    @param numCat one-element array that will be filled with the number of
    *     categories after regrouping
    * 
    *    @return the chi-square statistic for a discrete distribution
    * 
    */
   public static double chi2 (IntArrayList data, DiscreteDistributionInt dist,
                              int smin, int smax, double minExp, int[] numCat) {
      int i;
      int n = data.size();

      // Find the first non-negligible probability term and fix
      // the real smin.  The linear search starts from the given smin.
      i = smin;
      while (dist.prob (i)*n <= DiscreteDistributionInt.EPSILON)
         i++;
      smin = i--;

      // smax > smin is required
      while (smax <= smin)
         smax = 2*smax + 1;

      // Allocate and fill the array of expected observations
      // Each category s corresponds to a value s for which p(s)>0.
      double[] nbExp = new double[smax+1];
      do {
         i++;
         if (i > smax) {
            smax *= 2;
            double[] newNbExp = new double[smax + 1];
            System.arraycopy (nbExp, smin, newNbExp, smin, nbExp.length - smin);
            nbExp = newNbExp;
         }
         nbExp[i] = dist.prob (i)*n;
      }
      while (nbExp[i] > DiscreteDistributionInt.EPSILON);
      smax = i - 1;

      // Regroup the expected observations intervals
      // satisfying np(s)>=minExp
      OutcomeCategoriesChi2 cat = new OutcomeCategoriesChi2
         (nbExp, smin, smax);
      cat.regroupCategories (minExp);
      if (numCat != null)
         numCat[0] = cat.nbCategories;

      // Count the number of observations in each categories.
      int[] count = new int[cat.smax+1];
      for (i = 0; i < count.length; i++)
         count[i] = 0;
      for (i = 0; i < n; i++) {
         int s = data.get (i);
         while (cat.loc[s] != s)
            s = cat.loc[s];
         count[s]++;
      }

      // Perform the chi-square test
      return chi2 (cat.nbExp, count, cat.smin, cat.smax);
   }


   /**
    * Similar to {@link #chi2(double[],int[],int,int) chi2},
    *    except that the expected
    *   number of observations per category is assumed to be the same for
    *   all categories, and equal to <TT>nbExp</TT>.
    * 
    * @param nbExp number of expected observations in each category (or interval)
    * 
    *    @param count number of counted observations in each category
    * 
    *    @param smin index of the first valid data in <TT>count</TT> and <TT>nbExp</TT>
    * 
    *    @param smax index of the last valid data in <TT>count</TT> and <TT>nbExp</TT>
    * 
    *    @return the <SPAN CLASS="MATH"><I>X</I><SUP>2</SUP></SPAN> statistic
    * 
    */
   public static double chi2Equal (double nbExp, int[] count,
                                   int smin, int smax) {

      double diff, khi = 0.0;
      for (int s = smin; s <= smax; s++) {
         diff = count[s] - nbExp;
         khi += diff * diff;
      }
      return khi / nbExp;
   }


   /**
    * Computes the chi-square statistic for a continuous distribution.
    *    Here, the equiprobable case can be used.  Assuming that <TT>data</TT> contains
    *    observations coming from the uniform distribution, the <SPAN CLASS="MATH">[0, 1]</SPAN> interval
    *    is divided into <SPAN CLASS="MATH">1/<I>p</I></SPAN> subintervals, where <SPAN CLASS="MATH"><I>p</I> =</SPAN> <TT>minExp</TT><SPAN CLASS="MATH">/<I>n</I></SPAN>, <SPAN CLASS="MATH"><I>n</I></SPAN>
    *    being the sample size, i.e., the number of observations stored in
    *    <TT>data</TT>.  For each subinterval, the method counts the number of
    *    contained observations and the chi-square statistic is computed
    *    using {@link #chi2Equal(double,int[],int,int) chi2Equal}.
    *    We usually choose <TT>minExp</TT> = 10.
    * 
    * @param data array of observations in <SPAN CLASS="MATH">[0, 1)</SPAN>
    * 
    *    @param minExp minimum number of expected observations in each subintervals
    * 
    *    @return the chi-square statistic for a continuous distribution
    * 
    */
   public static double chi2Equal (DoubleArrayList data, double minExp) {
      int n = data.size();
      if (n < (int)Math.ceil (minExp))
         throw new IllegalArgumentException ("Not enough observations");
      double p = minExp/n;
      int m = (int)Math.ceil (1.0/p);
      // to avoid an exception when data[i] = 1/p, reserve one element more
      int[] count = new int[m + 1];
      for (int i = 0; i < n; i++) {
         int j = (int)Math.floor (data.get (i)/p);
         count[j]++;
      }
      // put the elements in count[m] where they belong: in count[m-1]
      count[m - 1] += count[m];
      return chi2Equal (minExp, count, 0, m - 1);
   }


   /**
    * Equivalent to <TT>chi2Equal (data, 10)</TT>.
    * 
    * @param data array of observations in <SPAN CLASS="MATH">[0, 1)</SPAN>
    * 
    *    @return the chi-square statistic for a continuous distribution
    * 
    */
   public static double chi2Equal (DoubleArrayList data) {
   return chi2Equal (data, 10.0);
}


   /**
    * Computes and returns the scan statistic <SPAN CLASS="MATH"><I>S</I><SUB>N</SUB>(<I>d</I> )</SPAN>,
    *   defined in {@link FBar#scan FBar.scan}.
    *   Let <SPAN CLASS="MATH"><I>U</I></SPAN> be the <SPAN CLASS="MATH"><I>N</I></SPAN> observations contained into <TT>sortedData</TT>.
    *   The <SPAN CLASS="MATH"><I>N</I></SPAN> observations in <SPAN CLASS="MATH"><I>U</I>[0..<I>N</I> - 1]</SPAN> must be real numbers
    *   in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>, sorted in increasing order.
    *   (See {@link FBar#scan FBar.scan} for the distribution function of <SPAN CLASS="MATH"><I>S</I><SUB>N</SUB>(<I>d</I> )</SPAN>).
    *  
    * @param sortedData sorted array of real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @param d length of the test interval (<SPAN CLASS="MATH">&#8712;(0, 1)</SPAN>)
    * 
    *    @return the scan statistic
    * 
    */
   public static int scan (DoubleArrayList sortedData, double d) {

      double[] u = sortedData.elements();
      int n = sortedData.size();

      int m = 1, j = 0, i = -1;
      double High = 0.0;

      while (j < (n-1) && High < 1.0) {
         ++i;

         High = u[i] + d;
         while (j < n && u[j] < High)
            ++j;
         // j is now the index of the first obs. to the right of High.
         if (j - i > m)
            m = j - i;
      }
      return m;
   }


   /**
    * Computes and returns the Cram&#233;r-von Mises statistic <SPAN CLASS="MATH"><I>W</I><SUB>N</SUB><SUP>2</SUP></SPAN>. It is
    *     defined by
    *   
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>W</I><SUB>N</SUB><SUP>2</SUP> = 1/(12<I>N</I>) + &sum;<SUB>j=0</SUB><SUP>N-1</SUP>(<I>U</I><SUB>(j)</SUB> - (<I>j</I> + 0.5)/<I>N</I>)<SUP>2</SUP>,<P>
    * </DIV><P></P>
    * assuming that <TT>sortedData</TT> contains 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>
    *  sorted in increasing order.
    *  
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return the Cram&#233;r-von Mises statistic
    * 
    */
   public static double cramerVonMises (DoubleArrayList sortedData) {
      double w, w2;
      double[] u = sortedData.elements();
      int n = sortedData.size();

      if (n <= 0) {
         System.err.println ("cramerVonMises:  n <= 0");
         return 0.0;
      }

      w2 = 1.0 / (12 * n);
      for (int i = 0; i < n; i++) {
         w = u[i] - (i + 0.5) / n;
         w2 += w * w;
      }
      return w2;
   }


   /**
    * Computes and returns the Watson statistic <SPAN CLASS="MATH"><I>G</I><SUB>N</SUB></SPAN>. It is
    *     defined by
    *  <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>G</I><SUB>N</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>(N)<SUP>1/2</SUP>max<SUB>0&nbsp;&lt;=&nbsp;j&nbsp;&lt;=&nbsp;N-1</SUB>{(<I>j</I> + 1)/<I>N</I> - <I>U</I><SUB>(j)</SUB> + bar(U)<SUB>N</SUB> -1/2}</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT">&nbsp;</TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>(N)<SUP>1/2</SUP>(<I>D</I><SUB>N</SUB><SUP>+</SUP> + bar(U)<SUB>N</SUB> - 1/2),</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    *   where 
    * <SPAN CLASS="MATH">bar(U)<SUB>N</SUB></SPAN> is the average of the observations <SPAN CLASS="MATH"><I>U</I><SUB>(j)</SUB></SPAN>,
    *   assuming that <TT>sortedData</TT> contains the sorted 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>.
    *  
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return the Watson statistic <SPAN CLASS="MATH"><I>G</I><SUB>N</SUB></SPAN>
    * 
    */
   public static double watsonG (DoubleArrayList sortedData) {
      double[] u = sortedData.elements();
      int n = sortedData.size();
      double sumZ;
      double d2;
      double dp, g;
      double unSurN = 1.0 / n;

      if (n <= 0) {
         System.err.println ("watsonG: n <= 0");
         return 0.0;
      }

      // degenerate case n = 1
      if (n == 1)
         return 0.0;

      // We assume that u is already sorted.
      dp = sumZ = 0.0;
      for (int i = 0; i < n; i++) {
         d2 = (i + 1) * unSurN - u[i];
         if (d2 > dp)
            dp = d2;
         sumZ += u[i];
      }
      sumZ = sumZ * unSurN - 0.5;
      g = Math.sqrt ((double) n) * (dp + sumZ);
      return g;
   }


   /**
    * Computes and returns the Watson statistic  <SPAN CLASS="MATH"><I>U</I><SUB>N</SUB><SUP>2</SUP></SPAN>. It is
    *      defined by
    *   <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>W</I><SUB>N</SUB><SUP>2</SUP></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>1/(12<I>N</I>) + &sum;<SUB>j=0</SUB><SUP>N-1</SUP>{<I>U</I><SUB>(j)</SUB> - (<I>j</I> + 0.5)/<I>N</I>}<SUP>2</SUP>,</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>U</I><SUB>N</SUB><SUP>2</SUP></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP><I>W</I><SUB>N</SUB><SUP>2</SUP> - <I>N</I>(bar(U)<SUB>N</SUB> -1/2)<SUP>2</SUP>.<P></TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    *   where 
    * <SPAN CLASS="MATH">bar(U)<SUB>N</SUB></SPAN> is the average of the observations <SPAN CLASS="MATH"><I>U</I><SUB>(j)</SUB></SPAN>,
    *   assuming that <TT>sortedData</TT> contains  the sorted
    *   
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>.
    *  
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return the Watson statistic <SPAN CLASS="MATH"><I>U</I><SUB>N</SUB><SUP>2</SUP></SPAN>
    * 
    * 
    */
   public static double watsonU (DoubleArrayList sortedData) {
      double sumZ, w, w2, u2;
      double[] u = sortedData.elements();
      int n = sortedData.size();

      if (n <= 0) {
         System.err.println ("watsonU: n <= 0");
         return 0.0;
      }

      // degenerate case n = 1
      if (n == 1)
         return 1.0 / 12.0;

      sumZ = 0.0;
      w2 = 1.0 / (12 * n);
      for (int i = 0; i < n; i++) {
         sumZ += u[i];
         w = u[i] - (i + 0.5) / n;
         w2 += w * w;
      }
      sumZ = sumZ / n - 0.5;
      u2 = w2 - sumZ * sumZ * n;
      return u2;
   }



   public static double EPSILONAD = Num.DBL_EPSILON / 2.0;


   /**
    * Computes and returns the Anderson-Darling statistic <SPAN CLASS="MATH"><I>A</I><SUB>N</SUB><SUP>2</SUP></SPAN>
    * (see method {@link #andersonDarling(double[]) andersonDarling}).
    *  
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return the Anderson-Darling statistic
    * 
    */
   public static double andersonDarling (DoubleArrayList sortedData) {
      double[] v = sortedData.elements();
      return andersonDarling (v);
   }


   /**
    * Computes and returns the Anderson-Darling statistic <SPAN CLASS="MATH"><I>A</I><SUB>N</SUB><SUP>2</SUP></SPAN>.
    *    It is
    *      defined by
    *   <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>A</I><SUB>N</SUB><SUP>2</SUP></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>- <I>N</I> - 1/<I>N</I>&nbsp;&nbsp;&nbsp;&nbsp;&sum;<SUB>j=0</SUB><SUP>N-1</SUP>{(2<I>j</I> + 1)ln(<I>U</I><SUB>(j)</SUB>) + (2<I>N</I> - 1 - 2<I>j</I>)ln(1 - <I>U</I><SUB>(j)</SUB>)},</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    *   assuming that <TT>sortedData</TT> contains 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>.
    * 
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return the Anderson-Darling statistic
    * 
    */
   public static double andersonDarling (double[] sortedData) {
      double u1;
      double u, a2;
      int n = sortedData.length;

      if (n <= 0) {
         System.err.println ("andersonDarling: n <= 0");
         return 0.0;
      }

      a2 = 0.0;
      for (int i = 0; i < n; i++) {
         u = sortedData[i];
         u1 = 1.0 - u;
         if (u < EPSILONAD)
            u = EPSILONAD;
         else if (u1 < EPSILONAD)
            u1 = EPSILONAD;
         a2 += (2*i + 1)*Math.log (u) + (1 + 2*(n - i - 1))*
                    Math.log (u1);
      }
      a2 = -n - a2 / n;
      return a2;
   }



   /**
    * Computes the Kolmogorov-Smirnov (KS) test statistics
    *  <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN>, and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB></SPAN> (see method
    *  {@link #kolmogorovSmirnov(DoubleArrayList) kolmogorovSmirnov}).
    * 
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return an array containing <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN> and <SPAN CLASS="MATH"><I>DN</I></SPAN>, in that order
    * 
    */
   public static double[] kolmogorovSmirnov (double[] sortedData) {
      DoubleArrayList v = new DoubleArrayList(sortedData);
      return kolmogorovSmirnov (v);
   }


   /**
    * Computes the Kolmogorov-Smirnov (KS) test statistics
    *  <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN>, and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB></SPAN>. It is
    *  defined by
    *  <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>D</I><SUB>N</SUB><SUP>+</SUP></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>max<SUB>0&nbsp;&lt;=&nbsp;j&nbsp;&lt;=&nbsp;N-1</SUB>((<I>j</I> + 1)/<I>N</I> - <I>U</I><SUB>(j)</SUB>),</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>D</I><SUB>N</SUB><SUP>-</SUP></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>max<SUB>0&nbsp;&lt;=&nbsp;j&nbsp;&lt;=&nbsp;N-1</SUB>(<I>U</I><SUB>(j)</SUB> - <I>j</I>/<I>N</I>),</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>D</I><SUB>N</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP>max&nbsp;(<I>D</I><SUB>N</SUB><SUP>+</SUP>, <I>D</I><SUB>N</SUB><SUP>-</SUP>).</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    *  and returns an array of length 3 that contains their values at positions
    *  0, 1, and 2, respectively.
    * 
    * <P>
    * These statistics compare the empirical distribution of
    *  
    * <SPAN CLASS="MATH"><I>U</I><SUB>(1)</SUB>,..., <I>U</I><SUB>(N)</SUB></SPAN>, which are assumed to be in <TT>sortedData</TT>,
    *  with the uniform distribution over <SPAN CLASS="MATH">[0, 1]</SPAN>.
    * 
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @return an array containing <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN> and <SPAN CLASS="MATH"><I>DN</I></SPAN>, in that order
    * 
    */
   public static double[] kolmogorovSmirnov (DoubleArrayList sortedData) {
      double[] ret = new double[3];
      int n = sortedData.size();

      if (n <= 0) {
         ret[0] = ret[1] = ret[2] = 0.0;
         System.err.println ("kolmogorovSmirnov:   n <= 0");
         return ret;
      }

      double[] retjo = kolmogorovSmirnovJumpOne (sortedData, 0.0);
      ret[0] = retjo[0];
      ret[1] = retjo[1];
      if (ret[1] > ret[0])
         ret[2] = ret[1];
      else
         ret[2] = ret[0];

      return ret;
   }


   /**
    * Compute the KS statistics <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP>(<I>a</I>)</SPAN> and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP>(<I>a</I>)</SPAN> defined in
    *   the description of the method
    *   {@link FDist#kolmogorovSmirnovPlusJumpOne FDist.kolmogorovSmirnovPlusJumpOne}, assuming that <SPAN CLASS="MATH"><I>F</I></SPAN> is the
    *   uniform distribution over <SPAN CLASS="MATH">[0, 1]</SPAN> and that
    *   
    * <SPAN CLASS="MATH"><I>U</I><SUB>(1)</SUB>,..., <I>U</I><SUB>(N)</SUB></SPAN> are in <TT>sortedData</TT>.
    *   Returns an array of length 2 that contains their values at positions
    *   0 and 1, respectively.
    *  
    * @param sortedData array of sorted real-valued observations in the interval <SPAN CLASS="MATH">[0, 1]</SPAN>
    * 
    *    @param a size of the jump
    * 
    *    @return an array containing <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN> and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN>, in that order
    * 
    */
   public static double[] kolmogorovSmirnovJumpOne (DoubleArrayList sortedData,
                                                    double a) {
      /* Statistics KS+ and KS-. Case with 1 jump at a, near the lower tail of
         the distribution. */

      double[] u = sortedData.elements();
      int n = sortedData.size();
      int j, i;
      double d2, d1, unSurN;
      double[] ret = new double[2];

      if (n <= 0) {
         ret[0] = ret[1] = 0.0;
         System.err.println ("kolmogorovSmirnovJumpOne: n <= 0");
         return ret;
      }

      ret[0] = 0.0;
      ret[1] = 0.0;
      unSurN = 1.0 / n;
      j = 0;

      while (j < n && u[j] <= a + EPSILOND) ++j;

      for (i = j - 1; i < n; i++) {
         if (i >= 0) {
            d1 = (i + 1) * unSurN - u[i];
            if (d1 > ret[0])
               ret[0] = d1;
         }
         if (i >= j) {
            d2 = u[i] - i * unSurN;
            if (d2 > ret[1])
               ret[1] = d2;
         }
      }
      return ret;
   }


   /**
    * Computes a variant of the <SPAN CLASS="MATH"><I>p</I></SPAN>-value <SPAN CLASS="MATH"><I>p</I></SPAN> whenever a test statistic
    *   has a <EM>discrete</EM> probability distribution.
    *   This <SPAN CLASS="MATH"><I>p</I></SPAN>-value is defined as follows:
    *   <BR>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * 
    * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>p</I><SUB>L</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP><I>P</I>[<I>Y</I>&nbsp;&lt;=&nbsp;<I>y</I>]</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>p</I><SUB>R</SUB></TD>
    * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
    * <TD ALIGN="LEFT" NOWRAP><I>P</I>[<I>Y</I>&nbsp;&gt;=&nbsp;<I>y</I>]</TD>
    * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
    * &nbsp;</TD></TR>
    * </TABLE></DIV>
    * <BR CLEAR="ALL">
    * 
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <TABLE>
    * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>p</I> =</TD>
    * <TD ALIGN="LEFT"><I>p</I><SUB>R</SUB>,</TD>
    * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if <I>p</I><SUB>R</SUB> &lt; <I>p</I><SUB>L</SUB>,</TD>
    * </TR>
    * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>p</I> =</TD>
    * <TD ALIGN="LEFT">1 - <I>p</I><SUB>L</SUB>,</TD>
    * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if <I>p</I><SUB>R</SUB>&nbsp;&gt;=&nbsp;<I>p</I><SUB>L</SUB> and <I>p</I><SUB>L</SUB> &lt; 0.5,</TD>
    * </TR>
    * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>p</I> =</TD>
    * <TD ALIGN="LEFT">0.5</TD>
    * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;otherwise.</TD>
    * </TR>
    * </TABLE>
    * </DIV><P></P>
    * The function takes <SPAN CLASS="MATH"><I>p</I><SUB>L</SUB></SPAN> and <SPAN CLASS="MATH"><I>p</I><SUB>R</SUB></SPAN> as input and returns <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @param pL left <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    * 
    *    @param pR right <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    * 
    *    @return the <SPAN CLASS="MATH"><I>p</I></SPAN>-value for a test on a discrete distribution
    */
   public static double pDisc (double pL, double pR) {
      double p;

      if (pR < pL)
         p = pR;
      else if (pL > 0.5)
         p = 0.5;
      else
         p = 1.0 - pL;
      // Note: si p est tres proche de 1, on perd toute la precision ici!
      // Note2: je ne pense pas que cela puisse se produire a cause des if (RS)
      return p;
   }
}
