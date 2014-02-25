

/*
 * Class:        ListOfTalliesWithCovariance
 * Description:  List of tallies with covariance
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist 
 * @since        2007

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

package umontreal.iro.lecuyer.stat.list;

import umontreal.iro.lecuyer.stat.Tally;
import umontreal.iro.lecuyer.stat.TallyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import cern.colt.matrix.DoubleMatrix1D;



/**
 * Extends {@link ListOfTallies} to add support for the computation
 * of the sample covariance between each pair of elements
 * in a list, without storing all observations.
 * This list of tallies contains internal structures to keep track of
 * 
 * <SPAN CLASS="MATH">bar(X)<SUB>n, i</SUB></SPAN> for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>d</I> - 1</SPAN>, and
 * 
 * <SPAN CLASS="MATH">&sum;<SUB>k=0</SUB><SUP>n-1</SUP>(<I>X</I><SUB>i, k</SUB> - bar(X)<SUB>k, i</SUB>)(<I>X</I><SUB>j, k</SUB> - bar(X)<SUB>k, j</SUB>)/<I>n</I></SPAN>, for
 * 
 * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>d</I> - 2</SPAN> and 
 * <SPAN CLASS="MATH"><I>j</I> = 1,&#8230;, <I>d</I> - 1</SPAN>, with <SPAN CLASS="MATH"><I>j</I> &gt; <I>i</I></SPAN>.
 * Here, 
 * <SPAN CLASS="MATH">bar(X)<SUB>n, i</SUB></SPAN> is the <SPAN CLASS="MATH"><I>i</I></SPAN>th component of 
 * <SPAN CLASS="MATH">bar(<I><B>X</B></I>)<SUB>n</SUB></SPAN>, the average vector,
 * and 
 * <SPAN CLASS="MATH">bar(X)<SUB>0, i</SUB> = 0</SPAN> for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>d</I> - 1</SPAN>.
 * The value <SPAN CLASS="MATH"><I>X</I><SUB>i, k</SUB></SPAN> corresponds to the <SPAN CLASS="MATH"><I>i</I></SPAN>th component of the <SPAN CLASS="MATH"><I>k</I></SPAN>th observation
 * <SPAN CLASS="MATH"><I><B>X</B></I><SUB>k</SUB></SPAN>.
 * These sums are updated every time a vector is added to this list, and
 * are used to estimate the covariances.
 * 
 * <P>
 * Note: the size of the list of tallies must remain fixed because of the
 * data structures used for computing sample covariances.
 * As a result, the first call to <TT>init</TT> makes this list
 * unmodifiable.
 * 
 * <P>
 * Note: for the sample covariance to be computed between a pair
 * of tallies, the number of observations in each tally
 * should be the same.  It is therefore recommended to always
 * add complete vectors of observations to this list.
 * Moreover, one must use
 * the {@link #add add} method in this class to add vectors of observations for
 * the sums used for covariance estimation to be updated correctly.
 * Failure to use this method, e.g., adding observations
 * to each individual tally in the list, will result in an incorrect
 * estimate of the covariances, unless the tallies
 * in the list can store observations.
 * For example, the following code, which adds the vector <TT>v</TT> in
 * the list of tallies <TT>list</TT>, works correctly only if the list
 * contains instances of {@link TallyStore}:
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * for (int i = 0; i &lt; v.length; i++)
 * <BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;list.get (i).add (v[i]);
 * <BR></TT>
 * </DIV>
 * But the following code is always correct:
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * list.add (v);
 * <BR></TT>
 * </DIV>
 * 
 */
public class ListOfTalliesWithCovariance<E extends Tally>
       extends ListOfTallies<E> {
   private double[] tempArray;
   private double[][] sxy;

   // Determines if we use a numerically stable covariance
   // formula.
   private boolean isStable = true;

   // The average of the first observations, for each tally
   private double[] curAverages;

   // The sum (xi - average)(yi - average) of the first observations
   private double[][] curSum2;
   private Logger log = Logger.getLogger ("umontreal.iro.lecuyer.stat.list");



   /**
    * Creates an empty list of tallies with covariance support.
    *    One must fill the list with tallies, and call {@link #init init} before
    *   adding any observation.
    * 
    */
   public ListOfTalliesWithCovariance() {
      super();
   }


   /**
    * Creates an empty list of tallies with covariance
    *   support and name <TT>name</TT>.
    *    One must fill the list with tallies, and call {@link #init init} before
    *   adding any observation.
    * 
    * @param name the name of the new list.
    * 
    * 
    */
   public ListOfTalliesWithCovariance (String name) {
      super (name);
   }


   /**
    * This factory method constructs and returns a list of tallies with <TT>size</TT> instances of
    *    {@link Tally}.
    * 
    * @param size the size of the list.
    * 
    *    @return the created list.
    * 
    */
   public static ListOfTalliesWithCovariance<Tally> createWithTally (int size) {
      ListOfTalliesWithCovariance<Tally> list = new ListOfTalliesWithCovariance<Tally>();
      for (int i = 0; i < size; i++)
         list.add (new Tally());
      list.init();
      return list;
   }


   /**
    * This factory method constructs and returns a list of tallies with <TT>size</TT> instances of
    *    {@link TallyStore}.
    * 
    * @param size the size of the list.
    * 
    *    @return the created list.
    * 
    */
   public static ListOfTalliesWithCovariance<TallyStore> createWithTallyStore
                                             (int size) {
      ListOfTalliesWithCovariance<TallyStore> list = new ListOfTalliesWithCovariance<TallyStore>();
      for (int i = 0; i < size; i++)
         list.add (new TallyStore());
      list.init();
      return list;
   }


   private void createSxy() {
      int l = size();
      if (isStable) {
         curAverages = new double[l];
         curSum2 = new double[l-1][];
         for (int i = 0; i < l - 1; i++)
            curSum2[i] = new double[l - 1 - i];
      }
      else {
         sxy = new double[l - 1][];
         for (int i = 0; i < l - 1; i++)
            sxy[i] = new double[l - 1 - i];
      }
      tempArray = new double[l];
   }

   public void init() {
      super.init();

      if (isModifiable()) {
         setUnmodifiable();
         createSxy();
      }
      if (isStable) {
         for (int i = 0; i < curAverages.length; i++)
            curAverages[i] = 0;
         for (int i = 0; i < curSum2.length; i++)
            for (int j = 0; j < curSum2[i].length; j++)
               curSum2[i][j] = 0;
      }
      else
         for (int i = 0; i < sxy.length; i++)
            for (int j = 0; j < sxy[i].length; j++)
               sxy[i][j] = 0;
   }

   /**
    * Adds a new vector of observations <TT>x</TT> to this
    *   list of tallies, and updates the internal data structures computing
    *   averages, and sums of products.
    *   One must use this method instead of adding observations to
    *   individual tallies to get a covariance estimate.
    * 
    * @param x the new vector of observations.
    * 
    * 
    */
   public void add (double[] x) {
      int l = size();

      int structSize = 0;
      structSize = (isStable) ? curSum2.length : sxy.length;
      if (structSize != l - 1)
            throw new IllegalArgumentException ("The structure's size mismatches the list's size");

      super.add (x);
      if (isStable) {
         int numObs = get (0).numberObs();
         // get (i1).average() would return the average over the n
         // observations, but we need the average over the last n-1 observations.
         for (int i1 = 0; i1 < l - 1; i1++)
            for (int i2 = i1 + 1; i2 < l; i2++)
               curSum2[i1][i2 - i1 - 1] += (numObs - 1)*
                  (x[i1] - curAverages[i1])*(x[i2] - curAverages[i2])/numObs;
         for (int i = 0; i < l; i++)
            curAverages[i] += (x[i] - curAverages[i])/numObs;
         // Now, curAverages[i] == get (i).average()
      }
      else
         for (int i1 = 0; i1 < l - 1; i1++)
            for (int i2 = i1 + 1; i2 < l; i2++)
               sxy[i1][i2 - i1 - 1] += x[i1]*x[i2];
   }


   public void add (DoubleMatrix1D x) {
      x.toArray (tempArray);
      add (tempArray);
   }

   public double covariance (int i, int j) {
      if (i == j)
         return get (i).variance();
      if (i > j) {
         // Make sure that i1 < i2, to have a single case
         int tmp = i;
         i = j;
         j = tmp;
      }

      Tally tallyi = get (i);
      Tally tallyj = get (j);
      if (tallyi == null || tallyj == null)
         return Double.NaN;
      int n = tallyi.numberObs();
      if (n != tallyj.numberObs()) {
         log.logp (Level.WARNING, "ListOfTalliesWithCovariance", "covariance",
            "Tally " + i  + ", with name " + tallyi.getName() + ", contains " 
            + n + " observations while " +
              "tally " + j + ", with name " + tallyj.getName() + ", contains " + tallyj.numberObs() + "observations");
         return Double.NaN;
      }

      if (n < 2) {
         log.logp (Level.WARNING, "ListOfTalliesWithCovariance", "covariance",
            "Tally " + i + ", with name " + tallyi.getName() + ", contains " + n + " observation");
         return Double.NaN;
      }
      if (tallyi instanceof TallyStore && tallyj instanceof TallyStore)
         return ((TallyStore) tallyi).covariance ((TallyStore) tallyj);
      else if (isStable)
         return curSum2[i][j - i - 1]/(n-1);
      else {
         double sum1 = tallyi.sum();
         double sum2 = tallyj.sum();
         double sum12 = sxy[i][j - i - 1];
         return (sum12 - sum1*sum2/n)/(n-1);
      }
   }

   /**
    * Clones this object.
    *    This clones the list of tallies and the data structures holding the sums of products but
    *    not the tallies comprising the list.
    *   The created clone is modifiable, even though the original list is unmodifiable.
    * 
    */
   public ListOfTalliesWithCovariance<E> clone() {
      ListOfTalliesWithCovariance<E> ta = (ListOfTalliesWithCovariance<E>)super.clone();
      ta.tempArray = new double[size()];
      if (curAverages != null)
         ta.curAverages = curAverages.clone();
      if (sxy != null) {
         ta.sxy = new double[sxy.length][];
         for (int i = 0; i < sxy.length; i++)
            ta.sxy[i] = sxy[i].clone();
      }
      if (curSum2 != null) {
         ta.curSum2 = new double[curSum2.length][];
         for (int i = 0; i < curSum2.length; i++)
            ta.curSum2[i] = curSum2[i].clone();
      }
      return ta;
   }
}
