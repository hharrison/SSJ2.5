

/*
 * Class:        ListOfTallies
 * Description:  List of statistical collectors.
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

import umontreal.iro.lecuyer.util.PrintfFormat;
import cern.colt.list.DoubleArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import umontreal.iro.lecuyer.stat.Tally;
import umontreal.iro.lecuyer.stat.TallyStore;



/**
 * Represents a list of tally statistical collectors.
 * Each element of the list is an instance of {@link umontreal.iro.lecuyer.stat.Tally Tally},
 * and a vector of observations can be added with
 * the {@link #add((double[])) add} method.
 * This class defines factory methods to fill a newly-constructed list
 * with <TT>Tally</TT> or <TT>TallyStore</TT> instances.
 * 
 */
public class ListOfTallies<E extends Tally> extends ListOfStatProbes<E> {



   /**
    * Constructs a new empty list of tallies.
    * 
    */
   public ListOfTallies() {
      super();
   }


   /**
    * Constructs a new empty list of tallies with name <TT>name</TT>.
    * 
    * @param name the name of the new list.
    * 
    * 
    */
   public ListOfTallies (String name) {
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
   public static ListOfTallies<Tally> createWithTally (int size) {
      ListOfTallies<Tally> list = new ListOfTallies<Tally>();
      for (int i = 0; i < size; i++)
         list.add (new Tally());
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
   public static ListOfTallies<TallyStore> createWithTallyStore (int size) {
      ListOfTallies<TallyStore> list = new ListOfTallies<TallyStore>();
      for (int i = 0; i < size; i++)
         list.add (new TallyStore());
      return list;
   }


   /**
    * Adds the observation <TT>x[i]</TT> in
    *  tally <TT>i</TT> of this list, for <TT>i = 0,..., size() - 1</TT>.
    *  No observation is added if the value is <TT>Double.NaN</TT>,
    *  or if collecting is turned OFF.
    *  If broadcasting is ON, the given array is notified
    *  to all registered observers.
    *  The given array <TT>x</TT> not being stored by this object,
    *  it can be freely used and modified after the call to this method.
    * 
    * @param x the array of observations.
    * 
    *    @exception NullPointerException if <TT>x</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the length of
    *     <TT>x</TT> does not correspond to <TT>size()</TT>.
    * 
    * 
    */
   public void add (double[] x) {
      int l = size();
      if (x.length != l)
         throw new IllegalArgumentException
            ("Incompatible array length: given " +
            x.length + ", required " + l);
      if (collect)
         for (int i = 0; i < l; i++) {
            double v = x[i];
            Tally ta = get (i);
            if (!Double.isNaN (v) && ta != null)
               ta.add (v);
         }
         notifyListeners (x);
   }


   /**
    * Assuming that each tally in this list contains
    *  the same number of observations, returns
    *  the number of observations in tally&nbsp;0, or
    *   0 if this list is empty.
    * 
    * @return the number of observations.
    * 
    */
   public int numberObs() {
      if (size() == 0)
         return 0;
      Tally t0 = get (0);
      return t0 == null ? 0 : t0.numberObs();
   }


   /**
    * Tests that every tally in this list contains the
    *  same number of observations.
    *  This returns <TT>true</TT> if and only if all
    *  tallies have the same number of observations, or if this list is empty.
    *  If observations are always added using the
    *  {@link #add((double[])) add} method from this class, and not
    *  {@link umontreal.iro.lecuyer.stat.Tally#add((double)) add} from
    *    {@link Tally}, this method always  returns <TT>true</TT>.
    * 
    * @return the success indicator of the test.
    * 
    */
   public boolean areAllNumberObsEqual() {
      final int l = size();
      int n = numberObs();
      for (int i = 1; i < l; i++) {
         Tally t = get (i);
         if (t.numberObs() != n)
            return false;
      }
      return true;
   }


   /**
    * Computes the average for each tally
    *  in this list, and stores the averages in the array <TT>r</TT>.
    *  If the tally <TT>i</TT> has no observation,
    *  the <TT>Double.NaN</TT> value is stored
    *  in the array, at index&nbsp;<TT>i</TT>.
    * 
    */
   public void average (double[] r) {
      final int l = size();
      for (int i = 0; i < l; i++) {
          // Manual check to avoid repetitive logs when all tallies
          // have 0 observation.
         Tally ta = get (i);
         double v = ta == null || ta.numberObs() == 0 ? Double.NaN : ta.average();
         r[i] = v;
      }
   }


   /**
    * For each tally in this list, computes
    *  the sample variance, and stores the variances into the array <TT>v</TT>.
    *  If, for some tally&nbsp;<TT>i</TT>, there are not enough
    *  observations for estimating the variance,
    *  <TT>Double.NaN</TT> is stored in the array.
    * 
    * @param v the array to be filled with sample variances.
    * 
    *    @exception NullPointerException if <TT>v</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>v.length</TT>
    *     does not correspond to {@link umontreal.iro.lecuyer.stat.list.ListOfStatProbes#size(()) size}.
    * 
    * 
    */
   public void variance (double[] v) {
      if (size() != v.length)
         throw new IllegalArgumentException
            ("Invalid length of given array");
      for (int i = 0; i < v.length; i++) {
         Tally tally = get (i);
         if (tally == null || tally.numberObs() < 2)
            v[i] = Double.NaN;
         else
            v[i] = tally.variance();
      }
   }


   /**
    * For each tally in this list, computes
    *  the sample standard deviation, and stores the standard deviations
    *  into the array <TT>std</TT>.
    *  This is equivalent to calling {@link #variance((double[])) variance} and
    *  performing a square root on every element
    *  of the filled array.
    * 
    * @param std the array to be filled with standard deviations.
    * 
    *    @exception NullPointerException if <TT>std</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>std.length</TT>
    *     does not correspond to <TT>size()</TT>.
    * 
    * 
    */
   public void standardDeviation (double[] std) {
      if (size() != std.length)
         throw new IllegalArgumentException
            ("Invalid length of given array");
      for (int i = 0; i < std.length; i++) {
         Tally tally = get (i);
         if (tally == null || tally.numberObs() < 2)
            std[i] = Double.NaN;
         else
            std[i] = tally.standardDeviation();
      }
   }


   /**
    * Returns the empirical covariance of the observations in tallies
    *  with indices <TT>i</TT> and <TT>j</TT>.  If 
    * <SPAN CLASS="MATH"><I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>n</SUB></SPAN> represent the
    *  observations in tally <TT>i</TT> whereas 
    * <SPAN CLASS="MATH"><I>y</I><SUB>1</SUB>,&#8230;, <I>y</I><SUB>n</SUB></SPAN> represent the
    *  observations in tally <TT>j</TT>, then the covariance is given by
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>S</I><SUB>X, Y</SUB> = <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="ListOfTalliesimg1.png"
    *  ALT="$\displaystyle {\frac{{1}}{{n-1}}}$">&sum;<SUB>k=1</SUB><SUP>n</SUP>(<I>x</I><SUB>k</SUB> - bar(X)<SUB>n</SUB>)(<I>y</I><SUB>k</SUB> - bar(Y)<SUB>n</SUB>) = [tex2html_wrap_indisplay273](&sum;<SUB>k=1</SUB><SUP>n</SUP><I>x</I><SUB>k</SUB><I>y</I><SUB>k</SUB> - <IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="ListOfTalliesimg2.png"
    *  ALT="$\displaystyle {\frac{{1}}{{n}}}$">&sum;<SUB>k=1</SUB><SUP>n</SUP><I>x</I><SUB>k</SUB>&sum;<SUB>r=1</SUB><SUP>n</SUP><I>y</I><SUB>r</SUB>).
    * </DIV><P></P>
    * This returns <TT>Double.NaN</TT>
    *    if the tallies do not contain the same number of observations, or
    *  if they contain less than two observations.
    *  This method throws an exception if the
    *  underlying tallies are not capable of storing
    *  observations, i.e. if the tallies are not TallyStores.
    *  The {@link ListOfTalliesWithCovariance}
    *  subclass provides an alternative implementation
    *  of this method which does not require the
    *  observations to be stored.
    * 
    * @param i the index of the first tally.
    * 
    *    @param j the index of the second tally.
    * 
    *    @return the value of the covariance.
    *    @exception ArrayIndexOutOfBoundsException if one or both
    *     indices are out of bounds.
    * 
    * 
    */
   public double covariance (int i, int j) {
      if (i == j)
         return get (i).variance();

      TallyStore tallyi = (TallyStore)get (i);
      TallyStore tallyj = (TallyStore)get (j);
      return tallyi.covariance (tallyj);
   }


   /**
    * Returns the empirical correlation between
    *  the observations in tallies with indices <TT>i</TT> and <TT>j</TT>.
    *  If the tally <TT>i</TT> contains a sample of the random
    *  variate <SPAN CLASS="MATH"><I>X</I></SPAN> and the tally <TT>j</TT> contains a sample of <SPAN CLASS="MATH"><I>Y</I></SPAN>,
    *  this corresponds to
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * Cor(<I>X</I>, <I>Y</I>) = Cov(<I>X</I>, <I>Y</I>)/((X)(Y))<SUP>1/2</SUP>.
    * </DIV><P></P>
    * 
    * <P>
    * This method uses {@link #covariance((int, int)) covariance}
    *  to obtain an estimate of the covariance, and
    *  {@link umontreal.iro.lecuyer.stat.Tally#variance(()) variance} in
    * class {@link Tally} to obtain the sample variances.
    * 
    * @param i the index of the first tally.
    * 
    *    @param j the index of the second tally.
    * 
    *    @return the value of the correlation.
    *    @exception ArrayIndexOutOfBoundsException if one or both
    *     indices are out of bounds.
    * 
    * 
    */
   public double correlation (int i, int j) {
      if (i == j)
         return 1.0;
      double cov = covariance (i, j);
      Tally tallyi = get (i);
      Tally tallyj = get (j);
      if (tallyi == null || tallyj == null)
         return Double.NaN;
      return cov/Math.sqrt (tallyi.variance()*tallyj.variance());
   }


   /**
    * Constructs and returns the sample covariance matrix
    *  for the tallies in this list.  The given <SPAN CLASS="MATH"><I>d</I>&#215;<I>d</I></SPAN> matrix <TT>c</TT>,
    *  where <SPAN CLASS="MATH"><I>d</I> =</SPAN>&nbsp;<TT>size()</TT>,
    *  is filled with the computed sample covariances.
    *  Element <TT>c.get (i, j)</TT> corresponds to
    *  the result of
    *  <TT>covariance (i, j)</TT>.
    * 
    * @param c the matrix to be filled with the sample covariances.
    * 
    *    @exception NullPointerException if <TT>c</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the number of rows or columns
    *     in <TT>c</TT> does not correspond to <TT>size()</TT>.
    * 
    * 
    */
   public void covariance (DoubleMatrix2D c) {
      int l = size();
      if (c.rows() != l)
         throw new IllegalArgumentException
            ("Invalid number of rows in covariance matrix");
      if (c.columns() != l)
         throw new IllegalArgumentException
            ("Invalid number of columns in covariance matrix");
      for (int i1 = 0; i1 < l; i1++)
         c.setQuick (i1, i1, get (i1).variance());
      for (int i1 = 0; i1 < l - 1; i1++)
         for (int i2 = i1 + 1; i2 < l; i2++) {
            double cov = covariance (i1, i2);
            c.setQuick (i1, i2, cov);
            c.setQuick (i2, i1, cov);
         }
   }


   /**
    * Similar to {@link #covariance((DoubleMatrix2D)) covariance} for computing
    *  the sample correlation matrix.
    * 
    * @param c the matrix to be filled with the correlations.
    * 
    *    @exception NullPointerException if <TT>c</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the number of rows or columns in <TT>c</TT>
    *     does not correspond to {@link umontreal.iro.lecuyer.stat.list.ListOfStatProbes#size(()) size}.
    * 
    * 
    */
   public void correlation (DoubleMatrix2D c) {
      int l = size();
      if (c.rows() != l)
         throw new IllegalArgumentException
            ("Invalid number of rows in correlation matrix");
      if (c.columns() != l)
         throw new IllegalArgumentException
            ("Invalid number of columns in correlation matrix");
      for (int i1 = 0; i1 < l; i1++)
         c.setQuick (i1, i1, 1.0);
      for (int i1 = 0; i1 < l - 1; i1++)
         for (int i2 = i1 + 1; i2 < l; i2++) {
            double cor = correlation (i1, i2);
            c.setQuick (i1, i2, cor);
            c.setQuick (i2, i1, cor);
         }
   }


   /**
    * Clones this object.   This makes a shallow copy
    *   of this list, i.e., this does not clone all the tallies in the list.
    *   The created clone is modifiable, even if the original list is unmodifiable.
    * 
    */
   public ListOfTallies<E> clone() {
      return (ListOfTallies<E>)super.clone();
   }
}
