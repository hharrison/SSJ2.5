

/*
 * Class:        LeastSquares
 * Description:  polynomial obtained by the least squares method on set of points
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

package umontreal.iro.lecuyer.functionfit;
   import umontreal.iro.lecuyer.functions.Polynomial;


import java.io.Serializable;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;


/**
 * Represents a polynomial obtained by the least squares method on a set of points.
 * More specifically, let 
 * <SPAN CLASS="MATH">(<I>x</I><SUB>0</SUB>, <I>y</I><SUB>0</SUB>),&#8230;,(<I>x</I><SUB>n</SUB>, <I>y</I><SUB>n</SUB>)</SPAN> be a set of points and
 * <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN> the constructed polynomial of degree <SPAN CLASS="MATH"><I>m</I></SPAN>. The constructed polynomial
 * minimizes the square error 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>E</I><SUP>2</SUP> = &sum;<SUB>i=0</SUB><SUP>n</SUP>[<I>y</I><SUB>i</SUB> - <I>p</I>(<I>x</I><SUB>i</SUB>)]<SUP>2</SUP>.
 * </DIV><P></P>
 * 
 */
public class LeastSquares extends Polynomial implements Serializable {
   private static final long serialVersionUID = -4997132164503234983L;
   private static final Algebra alg = new Algebra ();
   private double[] x;
   private double[] y;



   /**
    * Constructs a new least squares polynomial with points <TT>(x[0],
    *  y[0]),..., (x[n], y[n])</TT>. The constructed polynomial has degree
    *  <TT>degree</TT>.
    * 
    * @param x the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the points.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the points.
    * 
    *    @param degree the degree of the polynomial.
    * 
    *    @exception NullPointerException if <TT>x</TT> or <TT>y</TT> are <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the lengths of <TT>x</TT> and <TT>y</TT> are different,
    *                or if less than <TT>degree + 1</TT> points are specified.
    * 
    */
   public LeastSquares (double[] x, double[] y, int degree) {
      super (getCoefficients (x, y, degree));
      this.x = x.clone ();
      this.y = y.clone ();
   }


   /**
    * Computes and returns the coefficients of the fitting polynomial of
    *   degree <TT>degree</TT>.
    *  The coordinates of the given points are  <TT>(x[i], y[i])</TT>.
    * 
    * @param x the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the points.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the points.
    * 
    *    @param degree the degree of the polynomial.
    * 
    *    @return the coefficients of the fitting polynomial.
    * 
    */
   public static double[] getCoefficients (double[] x, double[] y,
                                           int degree) {
      if (x.length != y.length)
         throw new IllegalArgumentException ("Length of x and y not equal");
      if (x.length < degree + 1)
         throw new IllegalArgumentException ("Not enough points");

      final double[] xSums = new double[2 * degree + 1];
      final double[] xySums = new double[degree + 1];
      xSums[0] = x.length;
      for (int i = 0; i < x.length; i++) {
         double xv = x[i];
         xySums[0] += y[i];
         for (int j = 1; j <= 2 * degree; j++) {
            xSums[j] += xv;
            if (j <= degree)
               xySums[j] += xv * y[i];
            xv *= x[i];
         }
      }
      final DoubleMatrix2D A = new DenseDoubleMatrix2D (degree + 1, degree + 1);
      final DoubleMatrix2D B = new DenseDoubleMatrix2D (degree + 1, 1);
      for (int i = 0; i <= degree; i++) {
         for (int j = 0; j <= degree; j++) {
            final int d = i + j;
            A.setQuick (i, j, xSums[d]);
         }
         B.setQuick (i, 0, xySums[i]);
      }
      final DoubleMatrix2D aVec = alg.solve (A, B);
      return aVec.viewColumn (0).toArray ();
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the fitted points.
    * 
    * @return the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the fitted points.
    * 
    */
   public double[] getX() {
      return x;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the fitted points.
    * 
    * @return the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the fitted points.
    * 
    */
   public double[] getY() {
      return y;
   }


   /**
    * Calls {@link umontreal.iro.lecuyer.functionfit.PolInterp#toString(double[], double[]) toString}
    *  with the associated points.
    * 
    * @return a string containing the points.
    * 
    */
   public String toString() {
      return PolInterp.toString (x, y);
   }

   @Override



   public LeastSquares clone() {
      final LeastSquares ls = (LeastSquares) super.clone ();
      ls.x = x.clone ();
      ls.y = y.clone ();
      return ls;
   }
}
