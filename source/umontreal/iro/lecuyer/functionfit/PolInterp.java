

/*
 * Class:        PolInterp
 * Description:  polynomial that interpolates through a set of points
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

import java.io.Serializable;
import umontreal.iro.lecuyer.functions.Polynomial;

import umontreal.iro.lecuyer.util.PrintfFormat;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;


/**
 * Represents a polynomial that interpolates through a set of points. More
 * specifically, let 
 * <SPAN CLASS="MATH">(<I>x</I><SUB>0</SUB>, <I>y</I><SUB>0</SUB>),&#8230;,(<I>x</I><SUB>n</SUB>, <I>y</I><SUB>n</SUB>)</SPAN> be a set of points and
 * <SPAN CLASS="MATH"><I>p</I>(<I>x</I>)</SPAN> the constructed polynomial of degree <SPAN CLASS="MATH"><I>n</I></SPAN>. Then, for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I></SPAN>,
 * 
 * <SPAN CLASS="MATH"><I>p</I>(<I>x</I><SUB>i</SUB>) = <I>y</I><SUB>i</SUB></SPAN>.
 * 
 */
public class PolInterp extends Polynomial implements Serializable {
   private static final long serialVersionUID = -710451931485296501L;
   private static final Algebra alg = new Algebra ();
   private double[] x;
   private double[] y;



   /**
    * Constructs a new polynomial interpolating through the given points
    *  <TT>(x[0], y[0]), ..., (x[n], y[n])</TT>. This constructs a polynomial of
    *  degree <TT>n</TT> from <TT>n+1</TT> points.
    * 
    * @param x the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the points.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the points.
    * 
    *    @exception NullPointerException if <TT>x</TT> or <TT>y</TT> are <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if the lengths of <TT>x</TT> and <TT>y</TT> are different,
    *                or if less than two points are specified.
    * 
    */
   public PolInterp (double[] x, double[] y) {
      super (getCoefficients (x, y));
      this.x = x.clone ();
      this.y = y.clone ();
   }


   /**
    * Computes and returns the coefficients the polynomial interpolating
    *  through the given points <TT>(x[0], y[0]), ..., (x[n], y[n])</TT>. 
    *  This polynomial has degree <TT>n</TT> and there are <TT>n+1</TT> coefficients.
    * 
    * @param x the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the points.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the points.
    * 
    *    @return the coefficients the interpolating polynomial.
    * 
    */
   public static double[] getCoefficients (double[] x, double[] y) {
      if (x.length != y.length)
         throw new IllegalArgumentException (
               "x and y must have the same length");
      if (x.length <= 1)
         throw new IllegalArgumentException ("At least two points are needed");
      final DoubleMatrix2D u = new DenseDoubleMatrix2D (x.length, x.length);
      for (int i = 0; i < x.length; i++) {
         double v = 1;
         for (int j = 0; j < x.length; j++) {
            u.setQuick (i, j, v);
            v *= x[i];
         }
      }
      final DoubleMatrix2D yMat = new DenseDoubleMatrix2D (x.length, 1);
      yMat.viewColumn (0).assign (y);
      final DoubleMatrix2D bMat = alg.solve (u, yMat);
      return bMat.viewColumn (0).toArray ();
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the interpolated points.
    * 
    * @return the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the interpolated points.
    * 
    */
   public double[] getX() {
      return x.clone ();
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the interpolated points.
    * 
    * @return the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the interpolated points.
    * 
    */
   public double[] getY() {
      return y.clone ();
   }


   /**
    * Makes a string representation of a set of points.
    * 
    * @param x the <SPAN CLASS="MATH"><I>x</I></SPAN> coordinates of the points.
    * 
    *    @param y the <SPAN CLASS="MATH"><I>y</I></SPAN> coordinates of the points.
    * 
    *    @return the string representing the points.
    * 
    */
   public static String toString (double[] x, double[] y) {
      final StringBuilder sb = new StringBuilder ("Points: ");
      for (int i = 0; i < x.length; i++) {
         if (i > 0)
            sb.append (", ");
         final String xstr = PrintfFormat.format (8, 3, 3, x[i]);
         final String ystr = PrintfFormat.format (8, 3, 3, y[i]);
         sb.append ('(').append (xstr).append (", ").append (ystr).append (')');
      }
      return sb.toString ();
   }

   @Override



   /**
    * Calls {@link #toString(&nbsp;) toString}<TT>(double[], double[])</TT> with the
    *  associated  points.
    * 
    * @return a string containing the points.
    * 
    */
   public String toString() {
      return toString (x, y);
   }


   public PolInterp clone() {
      final PolInterp p = (PolInterp) super.clone ();
      p.x = x.clone ();
      p.y = y.clone ();
      return p;
   }
}
