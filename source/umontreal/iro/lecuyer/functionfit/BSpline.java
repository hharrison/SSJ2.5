

/*
 * Class:        BSpline
 * Description:  B-spline
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

import umontreal.iro.lecuyer.util.Misc;
import umontreal.iro.lecuyer.util.RootFinder;
import umontreal.iro.lecuyer.functions.MathFunction;
import umontreal.iro.lecuyer.functions.MathFunctionWithIntegral;
import umontreal.iro.lecuyer.functions.MathFunctionWithDerivative;
import umontreal.iro.lecuyer.functions.MathFunctionWithFirstDerivative;
import umontreal.iro.lecuyer.functions.MathFunctionUtil;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleFactory2D;
import java.util.Arrays;
import java.util.Random;
import java.io.*;


/**
 * Represents a B-spline with control points at
 * 
 * <SPAN CLASS="MATH">(<I>X</I><SUB>i</SUB>, <I>Y</I><SUB>i</SUB>)</SPAN>.
 * Let 
 * <SPAN CLASS="MATH"><B>P</B><SUB><B>i</B></SUB> = (<I>X</I><SUB>i</SUB>, <I>Y</I><SUB>i</SUB>)</SPAN>, for 
 * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I> - 1</SPAN>, be a <SPAN  CLASS="textit">control point</SPAN> and
 * let <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN>, for 
 * <SPAN CLASS="MATH"><I>j</I> = 0,&#8230;, <I>m</I> - 1</SPAN> be a <SPAN  CLASS="textit">knot</SPAN>.
 * A B-spline of degree <SPAN CLASS="MATH"><I>p</I> = <I>m</I> - <I>n</I> - 1</SPAN> is a parametric curve defined as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <B>P</B>(<B>t</B>) = &sum;<SUB>i=0</SUB><SUP>n-1</SUP><I>N</I><SUB>i, p</SUB>(<I>t</I>)<B>P</B><SUB><B>i</B></SUB>, for <I>t</I><SUB>p</SUB>&nbsp;&lt;=&nbsp;<I>t</I>&nbsp;&lt;=&nbsp;<I>t</I><SUB>m-p-1</SUB>.
 * </DIV><P></P>
 * Here,
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * 
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>N</I><SUB>i, p</SUB>(<I>t</I>)</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>((<I>t</I> - <I>t</I><SUB>i</SUB>)/(<I>t</I><SUB>i+p</SUB> - <I>t</I><SUB>i</SUB>))<I>N</I><SUB>i, p-1</SUB>(<I>t</I>) + ((<I>t</I><SUB>i+p+1</SUB> - <I>t</I>)/(<I>t</I><SUB>i+p+1</SUB> - <I>t</I><SUB>i+1</SUB>))<I>N</I><SUB>i+1, p-1</SUB>(<I>t</I>)</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>N</I><SUB>i, 0</SUB>(<I>t</I>)</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>=</TD>
 * <TD ALIGN="LEFT" NOWRAP>{<IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="BSplineimg1.png"
 *  ALT="$\displaystyle \begin{array}{ll}
 * 1&amp;\mbox{ for }t_i\le t\le t_{i+1},\\
 * 0&amp;\mbox{ elsewhere}.
 * \end{array}$"></TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 * 
 * <P>
 * This class provides methods to evaluate
 * 
 * <SPAN CLASS="MATH"><B>P</B>(<B>t</B>) = (<I>X</I>(<I>t</I>), <I>Y</I>(<I>t</I>))</SPAN> at any value of <SPAN CLASS="MATH"><I>t</I></SPAN>,
 * for a B-spline of any degree <SPAN CLASS="MATH"><I>p</I>&nbsp;&gt;=&nbsp;1</SPAN>.
 * Note that the
 * {@link #evaluate(double) evaluate} method
 * of this class can be slow, since it
 * uses a root finder to determine
 * the value of <SPAN CLASS="MATH"><I>t</I><SUP>*</SUP></SPAN> for which <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUP>*</SUP>) = <I>x</I></SPAN>
 * before it computes <SPAN CLASS="MATH"><I>Y</I>(<I>t</I><SUP>*</SUP>)</SPAN>.
 * 
 */
public class BSpline implements MathFunction

,
MathFunctionWithIntegral, MathFunctionWithDerivative, MathFunctionWithFirstDerivative{
   // Formula taken from http://www.ibiblio.org/e-notes/Splines/Basis.htm
   // and http://en.wikipedia.org/wiki/De_Boor_algorithm
   private double[] x;     //x original
   private double[] y;     //y original

   private int degree;

   //variables sur lesquelles on travaille
   private double[] myX;
   private double[] myY;
   private double[] knots;



   /**
    * Constructs a new uniform B-spline of degree <TT>degree</TT>
    *    with control points at (<TT>x[i]</TT>, <TT>y[i]</TT>).
    *    The knots of the resulting B-spline are set uniformly from
    *     <TT>x[0]</TT> to <TT>x[n-1]</TT>.
    * 
    * @param x the values of <SPAN CLASS="MATH"><I>X</I></SPAN>.
    * 
    *    @param y the values of <SPAN CLASS="MATH"><I>Y</I></SPAN>.
    * 
    *    @param degree the degree of the B-spline.
    * 
    * 
    */
   public BSpline (final double[] x, final double[] y, final int degree) {
      if (x.length != y.length)
         throw new IllegalArgumentException("The arrays x and y must share the same length");
      this.degree = degree;
      this.x = x.clone();
      this.y = y.clone();
      init(x, y, null);
   }


   /**
    * Constructs a new uniform B-spline
    *    with control points at (<TT>x[i]</TT>, <TT>y[i]</TT>), and
    *    knot vector given by the array <TT>knots</TT>.
    * 
    * @param x the values of <SPAN CLASS="MATH"><I>X</I></SPAN>.
    * 
    *    @param y the values of <SPAN CLASS="MATH"><I>Y</I></SPAN>.
    * 
    *    @param knots the knots of the B-spline.
    * 
    */
   public BSpline (final double[] x, final double[] y, final double[] knots) {
      if (x.length != y.length)
         throw new IllegalArgumentException("The arrays x and y must share the same length");
      if (knots.length <= x.length + 1)
         throw new IllegalArgumentException("The number of knots must be at least n+2");

      this.x = x.clone();
      this.y = y.clone();
      this.knots = knots.clone();
      init(x, y, knots);
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>X</I><SUB>i</SUB></SPAN> coordinates for this spline.
    * 
    * @return the <SPAN CLASS="MATH"><I>X</I><SUB>i</SUB></SPAN> coordinates.
    * 
    */
   public double[] getX() {
      return myX.clone ();
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>Y</I><SUB>i</SUB></SPAN> coordinates for this spline.
    * 
    * @return the <SPAN CLASS="MATH"><I>Y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    */
   public double[] getY() {
      return myY.clone ();
   }


   /**
    * Returns the knot maximal value.
    * 
    * @return the <SPAN CLASS="MATH"><I>Y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    */
   public double getMaxKnot() {
      return knots[knots.length-1];
   }


   /**
    * Returns the knot minimal value.
    * 
    * @return the <SPAN CLASS="MATH"><I>Y</I><SUB>i</SUB></SPAN> coordinates.
    * 
    */
   public double getMinKnot() {
      return knots[0];
   }


   /**
    * Returns an array containing the knot vector 
    * <SPAN CLASS="MATH">(<I>t</I><SUB>0</SUB>, <I>t</I><SUB>m-1</SUB>)</SPAN>.
    * 
    * @return the knot vector.
    * 
    */
   public double[] getKnots() {
      return knots.clone ();
   }


   /**
    * Returns a B-spline curve of degree <TT>degree</TT> interpolating the
    *    
    * <SPAN CLASS="MATH">(<I>x</I><SUB>i</SUB>, <I>y</I><SUB>i</SUB>)</SPAN> points.
    *    This method uses the uniformly spaced method for interpolating
    *    points with a B-spline curve, and a uniformed clamped knot vector,
    *    as described in <TT><A NAME="tex2html1"
    *   HREF="http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/">http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/</A></TT>.
    *    
    * @param x the values of <SPAN CLASS="MATH"><I>X</I></SPAN>.
    * 
    *    @param y the values of <SPAN CLASS="MATH"><I>Y</I></SPAN>.
    * 
    *    @param degree the degree of the B-spline.
    * 
    *    @return the B-spline curve.
    * 
    */
   public static BSpline createInterpBSpline (double[] x, double[] y,
                                              int degree)  {
      if (x.length != y.length)
         throw new IllegalArgumentException("The arrays x and y must share the same length");
      if (x.length <= degree)
         throw new IllegalArgumentException("The arrays length must be greater than degree");

      int n = x.length-1;
      //compute t : parameters vector uniformly from 0 to 1
      double[] t = new double[x.length];
      for(int i =0; i<t.length; i++) {
         t[i] = (double)i/(t.length-1);
      }

      //compute U : clamped knots vector uniformly from 0 to 1
      double U[] = new double[x.length + degree + 1];
      int m = U.length-1;
      for(int i =0; i<=degree; i++)
         U[i] = 0;
      for(int i =1; i<x.length-degree; i++)
         U[i+degree] = (double)i/(x.length-degree);
      for(int i = U.length-1-degree; i<U.length; i++)
         U[i] = 1;


      //compute matrix N : made of BSpline coefficients
      double [][] N = new double[x.length][x.length];
      for(int i = 0; i<x.length; i++) {
            N[i] = computeN(U, degree, t[i], x.length);
      }

      //initialize D : initial points matrix
      double [][] D = new double[x.length][2];
      for(int i =0; i<x.length; i++) {
         D[i][0] = x[i];
         D[i][1] = y[i];
      }

      //solve the linear equation system using colt library
      DoubleMatrix2D coltN = DoubleFactory2D.dense.make(N);
      DoubleMatrix2D coltD = DoubleFactory2D.dense.make(D);
      DoubleMatrix2D coltP;
      coltP = Algebra.ZERO.solve(coltN, coltD);

      return new BSpline(coltP.viewColumn(0).toArray(), coltP.viewColumn(1).toArray(), U);
   }


   /**
    * Returns a B-spline curve of degree <TT>degree</TT> smoothing
    *    
    * <SPAN CLASS="MATH">(<I>x</I><SUB>i</SUB>, <I>y</I><SUB>i</SUB>)</SPAN>, for 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>n</I></SPAN> points. The
    *    precision depends on the parameter <SPAN CLASS="MATH"><I>h</I></SPAN>: 
    * <SPAN CLASS="MATH">1&nbsp;&lt;=&nbsp;<texttt>degree</texttt>&nbsp;&lt;=&nbsp;<I>h</I> &lt; <I>n</I></SPAN>, which
    *    represents the number of control points used by the new B-spline curve,
    *    minimizing the quadratic error
    *    
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>L</I> = &sum;<SUB>i=0</SUB><SUP>n</SUP>(<IMG
    *  ALIGN="MIDDLE" BORDER="0" SRC="BSplineimg2.png"
    *  ALT="$\displaystyle {\frac{{Y_i - S_i(X_i)}}{{W_i}}}$">)<SUP>2</SUP>.
    * </DIV><P></P>
    * This method uses the uniformly spaced method for interpolating
    *    points with a B-spline curve and a uniformed clamped knot vector,
    *    as described in <TT><A NAME="tex2html2"
    *   HREF="http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/">http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/</A></TT>.
    * 
    * @param x the values of <SPAN CLASS="MATH"><I>X</I></SPAN>.
    * 
    *    @param y the values of <SPAN CLASS="MATH"><I>Y</I></SPAN>.
    * 
    *    @param degree the degree of the B-spline.
    * 
    *    @param h the desired number of control points.
    * 
    *    @return the B-spline curve.
    * 
    */
   public static BSpline createApproxBSpline (double[] x, double[] y,
                                              int degree, int h)  {
      if (x.length != y.length)
         throw new IllegalArgumentException("The arrays x and y must share the same length");
      if (x.length <= degree)
         throw new IllegalArgumentException("The arrays length must be greater than degree");

      //compute t : parameters vector uniformly from 0 to 1
      double[] t = new double[x.length];
      for(int i =0; i<t.length; i++) {
         t[i] = (double)i/(t.length-1);
      }

      //compute U : clamped knots vector uniformly from 0 to 1
      double U[] = new double[x.length + degree + 1];
      int m = U.length-1;
      for(int i =0; i<=degree; i++)
         U[i] = 0;
      for(int i =1; i<x.length-degree; i++)
         U[i+degree] = (double)i/(x.length-degree);
      for(int i = U.length-1-degree; i<U.length; i++)
         U[i] = 1;


      //compute matrix N : composed of BSpline coefficients
      double [][] N = new double[x.length][x.length];
      for(int i = 1; i<x.length; i++) {
            N[i] = computeN(U, degree, t[i], x.length);
      }

      //initialize D : initial points matrix
      double [][] D = new double[x.length][2];
      for(int i =0; i<x.length; i++) {
         D[i][0] = x[i];
         D[i][1] = y[i];
      }

      //compute Q :
      double[][] tempQ = new double[x.length][2];
      for(int k = 1; k < x.length-1; k++) {
         tempQ[k][0] = D[k][0] - N[k][0]*D[0][0] - N[k][h]*D[D.length-1][0];
         tempQ[k][1] = D[k][1] - N[k][0]*D[0][1] - N[k][h]*D[D.length-1][1];
      }
      double[][] Q = new double[h-1][2];
      for(int i = 1; i < h; i++) {
         Q[i-1][0] = 0;
         Q[i-1][1] = 0;
         for(int k = 1; k<x.length; k++) {
            Q[i-1][0] += N[k][i]*tempQ[k][0];
            Q[i-1][1] += N[k][i]*tempQ[k][1];
         }
      }

      //initialize P : new control point matrix
      double [][] P = new double[h+1][2];

      //solve the linear equation system using colt library
      DoubleMatrix2D coltQ = DoubleFactory2D.dense.make(Q);
      DoubleMatrix2D coltN = Algebra.ZERO.subMatrix(DoubleFactory2D.dense.make(N), 1, x.length-1, 1, h-1).copy();
      DoubleMatrix2D coltM = Algebra.ZERO.mult(Algebra.ZERO.transpose(coltN), coltN);
      DoubleMatrix2D coltP;
      coltP = Algebra.ZERO.solve(coltM, coltQ);
      double[] pxTemp = coltP.viewColumn(0).toArray();
      double[] pyTemp = coltP.viewColumn(1).toArray();
      double[] px = new double[h+1];
      double[] py = new double[h+1];
      px[0] = D[0][0];
      py[0] = D[0][1];
      px[h] = D[D.length-1][0];
      py[h] = D[D.length-1][1];
      for(int i =0; i< pxTemp.length; i++) {
         px[i+1] = pxTemp[i];
         py[i+1] = pyTemp[i];
      }
/*
      Writer dos = null;
      try {
         dos = new FileWriter("resss");

         int j = 0;
         //BSpline bs = new BSpline(x, y, 5);
         for (int i = 0; i < px.length; i++) {
            dos.write(px[i] + "   " + py[i] + PrintfFormat.NEWLINE);
         }
      }
      catch (FileNotFoundException e) {e.printStackTrace(); }
      catch (IOException e) { e.printStackTrace();}
      finally {
         try {
            dos.close();
         }
         catch (IOException e) {}
      }*/
      return new BSpline(px, py, U);
   }


   /**
    * Returns the derivative B-spline object of the current variable.
    *    Using this function and the returned object, instead of the
    *   <TT>derivative</TT> method,
    *    is strongly recommended if one wants to compute many derivative values.
    * 
    * @return the derivative B-spline of the current variable.
    * 
    */
   public BSpline derivativeBSpline()  {
      double xTemp[] = new double[this.myX.length-1];
      double yTemp[] = new double[this.myY.length-1];
      for(int i = 0; i<xTemp.length; i++) {
         xTemp[i] = (myX[i+1]-myX[i])*degree/(knots[i+degree+1]-knots[i+1]);
         yTemp[i] = (myY[i+1]-myY[i])*degree/(knots[i+degree+1]-knots[i+1]);
      }

      double [] newKnots = new double[knots.length-2];
      for(int i = 0; i < newKnots.length; i++) {
         newKnots[i] = knots[i+1];
      }

      //tri pas optimise du tout
      double xTemp2[] = new double[this.myX.length-1];
      double yTemp2[] = new double[this.myY.length-1];
      for(int i = 0; i<xTemp.length; i++) {
         int k=0;
         for(int j = 0; j<xTemp.length; j++) {
            if(xTemp[i] > xTemp[j])
               k++;
         }
         while(xTemp2[k] != 0)
            k++;
         xTemp2[k] = xTemp[i];
         yTemp2[k] = yTemp[i];
      }

      return new BSpline(xTemp2, yTemp2, newKnots);
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>i</I></SPAN>th derivative B-spline object of the current variable;
    *    <SPAN CLASS="MATH"><I>i</I></SPAN> must be less than the degree of the original B-spline.
    *    Using this function and the returned object, instead of the
    *   <TT>derivative</TT> method,
    *    is strongly recommended if one wants to compute many derivative values.
    * 
    * @param i the degree of the derivative.
    * 
    *    @return the ith derivative.
    * 
    */
   public BSpline derivativeBSpline (int i)  {
      BSpline bs = this;
      while(i > 0) {
         i--;
         bs = bs.derivativeBSpline();
      }
      return bs;
   }

   public double evaluate(final double u) {
      final MathFunction xFunction = new MathFunction () {
         public double evaluate (double t) {
            return evalX(t) - u;
         }
      };
      final double t = RootFinder.brentDekker (0, 1-1.0E-6, xFunction, 1e-6);
      return evalY(t);
   }

   public double integral (double a, double b) {
      return MathFunctionUtil.simpsonIntegral (this, a, b, 500);
   }

   public double derivative(double u) {
      return derivativeBSpline().evaluate(u);
   }

   public double derivative(double u, int n) {
      return derivativeBSpline(n).evaluate(u);
   }

   private void init(double[] x, double[] y, double [] initialKnots) {
      if(initialKnots == null) {
      //Cree un vecteur de noeud uniforme entre 0 et 1
         knots = new double[x.length+degree+1];
         for(int i = degree; i < this.knots.length-degree; i++)
            this.knots[i]= (double)(i-degree)/(knots.length - (2.0*degree) -1);
         for(int i = this.knots.length-degree; i < this.knots.length; i++)
            this.knots[i]=this.knots[i-1];
         for(int i = degree; i > 0; i--)
            this.knots[i-1]=this.knots[i];

      // cree notre vecteur interne de Points de controle
      // ici, aucune modification a faire sur les tableaux originaux
         myX = x;
         myY = y;
      }
      else {
         degree = initialKnots.length - x.length -1;

      // on adapte le tableau des noeuds a notre algorithme
      // le tableau knot necessite d'avoir degree fois la meme valeur en debut et en fin de tableau
      // on adapte la taille des tableau X et Y en consequence afin de continuer a respecter la condition :
      // x.length + degree + 1 = this.knots.length
      // Cette modification n'influence pas le resultat et permet de fermer notre courbe

         //Compute the number of values wich need to be added
         int iBorneInf = 1, iBorneSup = initialKnots.length-2;
         while(initialKnots[iBorneInf] == initialKnots[0])
            iBorneInf++;
         if(iBorneInf <= degree)
            iBorneInf = degree-iBorneInf+1;
         else
            iBorneInf=0;//on a alors iBorneInf valeurs a rajouter en debut de tableau

         while(initialKnots[iBorneSup] == initialKnots[initialKnots.length-1])
            iBorneSup--;
         if(iBorneSup >= initialKnots.length-1-degree)
            iBorneSup = degree+1-(initialKnots.length-1-iBorneSup);
         else
            iBorneSup = 0; //on a alors iBorneSup valeurs a rajouter en fin de tableau

         //add computed values
         this.knots = new double[initialKnots.length + iBorneInf + iBorneSup];
         myX = new double[x.length + iBorneInf + iBorneSup];
         myY = new double[y.length + iBorneInf + iBorneSup];
         for(int i = 0; i<iBorneInf; i++) {
            this.knots[i] = initialKnots[0];
            myX[i] = x[0];
            myY[i] = y[0];
         }
         for(int i = 0; i<initialKnots.length; i++)
            this.knots[iBorneInf + i] = initialKnots[i];
         for(int i = 0; i<x.length; i++) {
            myX[iBorneInf + i] = x[i];
            myY[iBorneInf + i] = y[i];
         }
         for(int i = 0; i<iBorneSup; i++) {
            this.knots[this.knots.length-1 - i] = initialKnots[initialKnots.length-1];
            myX[myX.length-1-i] = x[x.length-1];
            myY[myY.length-1-i] = y[y.length-1];
         }
      }
   }

   public double evalX(double u) {
      int k = Misc.getTimeInterval (knots, 0, knots.length - 1, u);
      double[][] X = new double[degree+1][myX.length];

      for(int i = k-degree; i<=k; i++)
         X[0][i] = myX[i];
      for(int j=1; j<= degree; j++) {
         for(int i = k-degree+j; i <= k; i++) {
            double aij = (u - this.knots[i]) / (this.knots[i+1+degree-j] - this.knots[i]);
            X[j][i] = (1-aij) * X[j-1][i-1] + aij * X[j-1][i];
         }
      }
      return X[degree][k];
   }

   public double evalY(double u) {
      int k = Misc.getTimeInterval (knots, 0, knots.length - 1, u);
      double[][] Y = new double[degree+1][myX.length];

      for(int i = k-degree; i<=k; i++)
         Y[0][i] = myY[i];
      for(int j=1; j<= degree; j++) {
         for(int i = k-degree+j; i <= k; i++) {
            double aij = (u - this.knots[i]) / (this.knots[i+1+degree-j] - this.knots[i]);
            Y[j][i] = (1-aij) * Y[j-1][i-1] + aij * Y[j-1][i];
         }
      }
      return Y[degree][k];
   }

   private static double[] computeN(double[] U, int degree, double u, int n) {
      double[] N = new double[n];

      //cas particuliers
      if(u == U[0]) {
         N[0] = 1.0;
         return N;
      }
      else if (u == U[U.length-1]) {
         N[N.length-1] = 1.0;
         return N;
      }

      //trouve l'intervalle de u
      int k = Misc.getTimeInterval (U, 0, U.length - 1, u);

      //calcule N, tableaux des coefficients des BSplines
      N[k] = 1.0;
      for(int d = 1; d <= degree; d++) {
         N[k-d] = N[k-d+1] * (U[k+1] - u) / (U[k+1] - U[k-d+1]);
         for(int i = k-d+1; i<= k-1; i++)
            N[i] = (u - U[i]) / (U[i+d]-U[i]) * N[i] + (U[i+d+1] - u)/(U[i+d+1] - U[i+1]) * N[i+1];
         N[k] = (u - U[k]) / (U[k+d] - U[k]) * N[k];
      }
      return N;
   }

}
