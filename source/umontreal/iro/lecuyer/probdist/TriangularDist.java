

/*
 * Class:        TriangularDist
 * Description:  triangular distribution
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

package umontreal.iro.lecuyer.probdist;

import java.util.Arrays;



/**
 * Extends the class {@link ContinuousDistribution} for
 * the <EM>triangular</EM> distribution with domain <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> and <SPAN  CLASS="textit">mode</SPAN> 
 * (or shape parameter) <SPAN CLASS="MATH"><I>m</I></SPAN>, where  
 * <SPAN CLASS="MATH"><I>a</I>&nbsp;&lt;=&nbsp;<I>m</I>&nbsp;&lt;=&nbsp;<I>b</I></SPAN>. 
 * The density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">2(<I>x</I> - <I>a</I>)/[(<I>b</I> - <I>a</I>)(<I>m</I> - <I>a</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>m</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">2(<I>b</I> - <I>x</I>)/[(<I>b</I> - <I>a</I>)(<I>b</I> - <I>m</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>m</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>f</I> (<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; elsewhere,</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * the distribution function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">0</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &lt; <I>a</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">(<I>x</I> - <I>a</I>)<SUP>2</SUP>/[(<I>b</I> - <I>a</I>)(<I>m</I> - <I>a</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>a</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>m</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1 - (<I>b</I> - <I>x</I>)<SUP>2</SUP>/[(<I>b</I> - <I>a</I>)(<I>b</I> - <I>m</I>)]</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if <I>m</I>&nbsp;&lt;=&nbsp;<I>x</I>&nbsp;&lt;=&nbsp;<I>b</I>,</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I>(<I>x</I>) =</TD>
 * <TD ALIGN="LEFT">1</TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> &gt; <I>b</I>,</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * and the inverse distribution function is given by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I><SUP>-1</SUP>(<I>u</I>) =</TD>
 * <TD ALIGN="LEFT"><I>a</I> + ((b - a)(m - a)u)<SUP>1/2</SUP></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if 0&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;(<I>m</I> - <I>a</I>)/(<I>b</I> - <I>a</I>),</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>F</I><SUP>-1</SUP>(<I>u</I>) =</TD>
 * <TD ALIGN="LEFT"><I>b</I> - ((b - a)(b - m)(1 - u))<SUP>1/2</SUP></TD>
 * <TD ALIGN="LEFT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; if (<I>m</I> - <I>a</I>)/(<I>b</I> - <I>a</I>&nbsp;&lt;=&nbsp;<I>u</I>&nbsp;&lt;=&nbsp;1.</TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * 
 */
public class TriangularDist extends ContinuousDistribution {
   private double a;
   private double b;
   private double m;



   /**
    * Constructs a <TT>TriangularDist</TT> object with default parameters
    *    <SPAN CLASS="MATH"><I>a</I> = 0</SPAN>, <SPAN CLASS="MATH"><I>b</I> = 1</SPAN>, and <SPAN CLASS="MATH"><I>m</I> = 0.5</SPAN>.
    * 
    */
   public TriangularDist() {
      setParams (0.0, 1.0, 0.5);
   }


   /**
    * Constructs a <TT>TriangularDist</TT> object with parameters <SPAN CLASS="MATH"><I>a</I> = 0</SPAN> ,
    *  <SPAN CLASS="MATH"><I>b</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> = <TT>m</TT>.
    * 
    */
   public TriangularDist (double m) {
      setParams (0.0, 1.0, m);
   }


   /**
    * Constructs a <TT>TriangularDist</TT> object with
    *    parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN>.
    * 
    */
   public TriangularDist (double a, double b, double m) {
      setParams (a, b, m);
   }


   public double density (double x) {
      return density (a, b, m, x);
   }
       
   public double cdf (double x) {
      return cdf (a, b, m, x);
   }

   public double barF (double x) {
      return barF (a, b, m, x);
   }
     
   public double inverseF (double u){
      return inverseF (a, b, m, u);
   }

   public double getMean() {
      return TriangularDist.getMean (a, b, m);
   }

   public double getVariance() {
      return TriangularDist.getVariance (a, b, m);
   }

   public double getStandardDeviation() {
      return TriangularDist.getStandardDeviation (a, b, m);
   }

   /**
    * Computes the density function.
    * 
    */
   public static double density (double a, double b, double m, double x) {
      if (m < a || m > b)
         throw new IllegalArgumentException ("m is not in [a,b]");
      if (x < a || x > b)
         return 0.0;
      else if (x <= m && m != a)
         return 2.0*(x - a)/((b - a)*(m - a));
      else
         return 2.0*(b - x)/((b - a)*(b - m));
   }
      

   /**
    * Computes the  distribution function.
    * 
    */
   public static double cdf (double a, double b, double m, double x) {
      if (m < a || m > b)
         throw new IllegalArgumentException ("m is not in [a,b]");
      if (x <= a)
         return 0.0;
      else if (x <= m && m != a)
         return (x - a)*(x - a)/((b - a)*(m - a));
      else if (x < b)
         return 1.0 - (b - x)*(b - x)/((b - a)*(b - m));
      else
         return 1.0;
   }


   /**
    * Computes the complementary distribution function.
    * 
    */
   public static double barF (double a, double b, double m, double x) {
      if (m < a || m > b)
         throw new IllegalArgumentException ("m is not in [a,b]");
      if (x <= a)
         return 1.0;
      else if (x <= m && m != a)
         return 1.0 - (x - a)*(x - a)/((b - a)*(m - a));
      else if (x < b)
         return (b - x)*(b - x)/((b - a)*(b - m));
      else
         return 0.0;
   }


   /**
    * Computes the inverse distribution function.
    * 
    */
   public static double inverseF (double a, double b, double m, double u) {
      if (m < a || m > b)
         throw new IllegalArgumentException ("m is not in [a,b]");
      if (u < 0.0 || u > 1.0)
         throw new IllegalArgumentException ("u is not in [0,1]");
      if (u <= 0.0)
         return a;
      if (u >= 1.0)
         return b;
       // the code is taken and adapted from unuran
       // file /distributions/c_triangular_gen.c
       double h = (m - a)/(b - a);
       return u <= h && m != a ? a + Math.sqrt ((b - a)*(m - a)*u) 
                : b - Math.sqrt ((b - a)*(b - m)*(1 - u));
   }


   /**
    * Estimates the parameter <SPAN CLASS="MATH"><I>m</I></SPAN> of the triangular distribution using the
    *    maximum likelihood method, from the <SPAN CLASS="MATH"><I>n</I></SPAN> observations <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>,
    *    
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>. The estimated parameter is returned in a one-element
    *    array: [<SPAN CLASS="MATH">hat(m)</SPAN>].  See.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @param a lower limit of range
    * 
    *    @param b upper limit of range
    * 
    *    @return returns the parameter [<SPAN CLASS="MATH"><I>m</I></SPAN>]
    * 
    */
   public static double[] getMLE (double[] x, int n, double a, double b) {
      if (n <= 0)
         throw new IllegalArgumentException ("n <= 0");
      double[] Y = new double[n];   // sorted x[i]
      System.arraycopy (x, 0, Y, 0, n);
      Arrays.sort (Y);

      int rmax = -1;
      double prodmax = -1.0e300;
      final double ba = b - a;
      double z;
      int i;
      for (int r = 0; r < n; r++) {
         z = (Y[r] - a) / ba;
         if ((z <= (double)r/n) || (z >= (double)(r + 1)/n))
            continue;    // MLE cannot be there
         double prod = 1.0;
         double d = Y[r] - a;
         for (i = 0; i < r; i++)
            prod *= (Y[i] - a)/d;
         
         d = b - Y[r];
         for (i = r+1; i < n; i++)
            prod *= (b - Y[i])/d;

         if (prod > prodmax) {
            prodmax = prod;
            rmax = r;
         }
      }
      
      if (rmax < 0)
         throw new UnsupportedOperationException (
            "   data cannot fit a triangular distribution");

      double[] param = new double[1];
      param[0] = Y[rmax];
      return param;
   }


   /**
    * Creates a new instance of a triangular distribution with parameters
    *   <TT>a</TT> and <TT>b</TT>.  <SPAN CLASS="MATH"><I>m</I></SPAN> is estimated using the maximum
    *   likelihood method based on the <SPAN CLASS="MATH"><I>n</I></SPAN> observations
    *   <SPAN CLASS="MATH"><I>x</I>[<I>i</I>]</SPAN>, 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>n</I> - 1</SPAN>.
    * 
    * @param x the list of observations used to evaluate parameters
    * 
    *    @param n the number of observations used to evaluate parameters
    * 
    *    @param a lower limit of range
    * 
    *    @param b upper limit of range
    * 
    * 
    */
   public static TriangularDist getInstanceFromMLE (double[] x, int n,
                                                    double a, double b) {
      double param[] = getMLE (x, n, a, b);
      return new TriangularDist (a, b, param[0]);
   }


   /**
    * Computes and returns the mean 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (<I>a</I> + <I>b</I> + <I>m</I>)/3</SPAN>
    *    of the triangular distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>m</I></SPAN>.
    * 
    * @return the mean of the triangular distribution
    * 
    */
   public static double getMean (double a, double b, double m) {
      if ((a == 0.0 && b == 1.0) && (m < 0 || m > 1))
         throw new IllegalArgumentException ("m is not in [0,1]");
      else if (m < a || m > b) 
         throw new IllegalArgumentException ("m is not in [a,b]");

      return ((a + b + m) / 3.0);
   }


   /**
    * Computes and returns the variance 
    *    
    * <SPAN CLASS="MATH">Var[<I>X</I>] = (<I>a</I><SUP>2</SUP> + <I>b</I><SUP>2</SUP> + <I>m</I><SUP>2</SUP> - <I>ab</I> - <I>am</I> - <I>bm</I>)/18</SPAN>
    *    of the triangular distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>m</I></SPAN>.
    * 
    * @return the variance of the triangular distribution
    * 
    */
   public static double getVariance (double a, double b, double m) {
      if ((a == 0.0 && b == 1.0) && (m < 0 || m > 1))
         throw new IllegalArgumentException ("m is not in [0,1]");
      else if (m < a || m > b) 
         throw new IllegalArgumentException ("m is not in [a,b]");

      return ((a * a + b * b + m * m - a * b - a * m - b * m) / 18.0);
   }


   /**
    * Computes and returns the standard deviation
    *    of the triangular distribution with parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>m</I></SPAN>.
    * 
    * @return the standard deviation of the triangular distribution
    * 
    */
   public static double getStandardDeviation (double a, double b, double m) {
      return Math.sqrt (TriangularDist.getVariance (a, b, m));
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>a</I></SPAN> for this object.
    * 
    */
   public double getA() {
      return a;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>b</I></SPAN> for this object.
    * 
    */
   public double getB() {
      return b;
   }
      

   /**
    * Returns the value of <SPAN CLASS="MATH"><I>m</I></SPAN> for this object.
    * 
    */
   public double getM() {
      return m;
   }
      

   /**
    * Sets the value of the parameters <SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN> and <SPAN CLASS="MATH"><I>m</I></SPAN> for this object.
    * 
    */
   public void setParams (double a, double b, double m) {
      if ((a == 0.0 && b == 1.0) && (m < 0 || m > 1))
         throw new IllegalArgumentException ("m is not in [0,1]");
      else if (a >= b)
         throw new IllegalArgumentException ("a >= b");
      else if (m < a || m > b) 
         throw new IllegalArgumentException ("m is not in [a,b]");
      this.a = a;
      this.b = b;
      this.m = m;
      supportA = a;
      supportB = b;
   }


   /**
    * Return a table containing the parameters of the current distribution.
    *    This table is put in regular order: [<SPAN CLASS="MATH"><I>a</I></SPAN>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>m</I></SPAN>].
    * 
    */
   public double[] getParams () {
      double[] retour = {a, b, m};
      return retour;
   }


   /**
    * Returns a <TT>String</TT> containing information about the current distribution.
    * 
    */
   public String toString () {
      return getClass().getSimpleName() + " : a = " + a + ", b = " + b + ", m = " + m;
   }

}
