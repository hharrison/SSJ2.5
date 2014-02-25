

/*
 * Class:        BiNormalDist
 * Description:  bivariate normal distribution
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

package umontreal.iro.lecuyer.probdistmulti;

import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.probdist.NormalDistQuick;


/**
 * Extends the class {@link ContinuousDistribution2Dim} for the <EM>bivariate 
 * normal</EM> distribution. It has means 
 * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = <I>&#956;</I><SUB>1</SUB></SPAN>,
 *   
 * <SPAN CLASS="MATH"><I>E</I>[<I>Y</I>] = <I>&#956;</I><SUB>2</SUB></SPAN>, and variances var
 * <SPAN CLASS="MATH">[<I>X</I>] = <I>&#963;</I><SUB>1</SUB><SUP>2</SUP></SPAN>,
 * var
 * <SPAN CLASS="MATH">[<I>Y</I>] = <I>&#963;</I><SUB>2</SUB><SUP>2</SUP></SPAN> such that 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> &gt; 0</SPAN> and 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB> &gt; 0</SPAN>. 
 *  The correlation between <SPAN CLASS="MATH"><I>X</I></SPAN> and <SPAN CLASS="MATH"><I>Y</I></SPAN> is <SPAN CLASS="MATH"><I>r</I></SPAN>.
 * Its density function is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>, <I>y</I>) = <I>e</I><SUP>-T</SUP>/(2<I>&#960;&#963;</I><SUB>1</SUB><I>&#963;</I><SUB>2</SUB>(1-r^2)<SUP>1/2</SUP>)
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>T</I> = [((<I>x</I> - <I>&#956;</I><SUB>1</SUB>)/<I>&#963;</I><SUB>1</SUB>)<SUP>2</SUP> -2<I>r</I>((<I>x</I> - <I>&#956;</I><SUB>1</SUB>)/<I>&#963;</I><SUB>1</SUB>)((<I>y</I> - <I>&#956;</I><SUB>2</SUB>)/<I>&#963;</I><SUB>2</SUB>) + ((<I>y</I> - <I>&#956;</I><SUB>2</SUB>)/<I>&#963;</I><SUB>2</SUB>)<SUP>2</SUP>]/(2(1 - <I>r</I><SUP>2</SUP>))
 * </DIV><P></P>
 * and the corresponding distribution function is (the <TT>cdf</TT> method)
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#934;</I>(<I>&#956;</I><SUB>1</SUB>, <I>&#963;</I><SUB>1</SUB>, <I>x</I>, <I>&#956;</I><SUB>2</SUB>, <I>&#963;</I><SUB>2</SUB>, <I>y</I>, <I>r</I>) = &int;<SUB>-&#8734;</SUB><SUP>x</SUP><I>dx</I>&int;<SUB>-&#8734;</SUB><SUP>y</SUP><I>dy</I>&nbsp;<I>e</I><SUP>-T</SUP>/(2<I>&#960;&#963;</I><SUB>1</SUB><I>&#963;</I><SUB>2</SUB>(1 - r^2)<SUP>1/2</SUP>).
 * </DIV><P></P>
 * We also define the upper distribution function (the <TT>barF</TT> method) as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * bar(&Phi;)(<I>&#956;</I><SUB>1</SUB>, <I>&#963;</I><SUB>1</SUB>, <I>x</I>, <I>&#956;</I><SUB>2</SUB>, <I>&#963;</I><SUB>2</SUB>, <I>y</I>, <I>r</I>) = &int;<SUP>&#8734;</SUP><SUB>x</SUB><I>dx</I>&int;<SUP>&#8734;</SUP><SUB>y</SUB><I>dy</I>&nbsp;<I>e</I><SUP>-T</SUP>/(2<I>&#960;&#963;</I><SUB>1</SUB><I>&#963;</I><SUB>2</SUB>(1 - r^2)<SUP>1/2</SUP>).
 * </DIV><P></P>
 * When 
 * <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB> = <I>&#956;</I><SUB>2</SUB> = 0</SPAN> and
 *  
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> = <I>&#963;</I><SUB>2</SUB> = 1</SPAN>, we have the <EM>standard binormal</EM> 
 * distribution, with corresponding distribution function
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#934;</I>(<I>x</I>, <I>y</I>, <I>r</I>) = &int;<SUP>&#8734;</SUP><SUB>x</SUB><I>dx</I>&int;<SUP>&#8734;</SUP><SUB>y</SUB><I>dy</I>&nbsp;<I>e</I><SUP>-S</SUP>/(2<I>&#960;</I>(1 - r^2)<SUP>1/2</SUP>)
 * </DIV><P></P>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>S</I> = (<I>x</I><SUP>2</SUP> -2<I>rxy</I> + <I>y</I><SUP>2</SUP>)/(2(1 - <I>r</I><SUP>2</SUP>)).
 * </DIV><P></P>
 * 
 */
public class BiNormalDist extends ContinuousDistribution2Dim  {
   protected int ndigit;        // Number of decimal digits of accuracy
   protected double mu1, mu2;
   protected double sigma1, sigma2;
   protected double rho;
   protected double racRho;        // sqrt(1 - rho^2)
   protected double detS;          // 2*PI*sigma1*sigma2*sqrt(1 - rho^2)
   protected static final double RHO_SMALL = 1.0e-8; // neglect small rhos

   private static final double Z[] = { 
      0.04691008, 0.23076534, 0.5, 0.76923466, 0.95308992 };

   private static final double W[] = {
      0.018854042, 0.038088059, 0.0452707394, 0.038088059, 0.018854042 };

   private static final double AGauss[] = {
      -0.72657601, 0.71070688, -0.142248368, 0.127414796 };




   /**
    * Constructs a <TT>BiNormalDist</TT> object with default parameters 
    * <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB> = <I>&#956;</I><SUB>2</SUB> = 0</SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> = <I>&#963;</I><SUB>2</SUB> = 1</SPAN> and correlation <SPAN CLASS="MATH"><I>&#961;</I> =</SPAN><TT> rho</TT>.
    * 
    */
   public BiNormalDist (double rho)  {
      setParams (0.0, 1.0, 0.0, 1.0, rho);
   }


   /**
    * Constructs a <TT>BiNormalDist</TT> object with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *  <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = 
    *  <TT>sigma2</TT> and <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT>.
    * 
    */
   public BiNormalDist (double mu1, double sigma1,
                        double mu2, double sigma2, double rho)  {
      setParams (mu1, sigma1, mu2, sigma2, rho);
   }


   public double density (double x, double y) {
      if (Math.abs(rho) == 1.)
         throw new IllegalArgumentException ("|rho| = 1");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      double T = (X*X - 2.0*rho*X*Y + Y*Y) / (2.0*racRho*racRho);
      return Math.exp(-T) / detS;
   }

   /**
    * Computes the <EM>standard binormal</EM> density function with 
    * <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB> = <I>&#956;</I><SUB>2</SUB> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> = <I>&#963;</I><SUB>2</SUB> = 1</SPAN>.
    * 
    */
   public static double density (double x, double y, double rho)  {
       return density (0.0, 1.0, x, 0.0, 1.0, y, rho);
   }


   /**
    * Computes the <EM>binormal</EM> density function
    *   with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>, <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> =
    *    <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = <TT>sigma2</TT> and <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT>.
    * 
    */
   public static double density (double mu1, double sigma1, double x, 
                                 double mu2, double sigma2, double y,
                                 double rho)  {
      if (sigma1 <= 0.)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0.)
         throw new IllegalArgumentException ("sigma2 <= 0");
      if (Math.abs(rho) >= 1.)
         throw new IllegalArgumentException ("|rho| >= 1");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      double fRho = (1.0 - rho)*(1.0 + rho);
      double T = (X*X - 2.0*rho*X*Y + Y*Y) / (2.0*fRho);
      return Math.exp (-T)/ (2.0*Math.PI*sigma1*sigma2*Math.sqrt(fRho));

   }
 

   protected static double Gauss (double z) {
       // Computes normal probabilities to within accuracy of 10^(-7)
       // Drezner Z., and G.O. Wesolowsky (1990) On the computation of the
       // bivariate normal integral.  J. Statist. Comput. Simul. 35:101-107.

       final double x = 1.0/(1.0 + 0.23164189 * Math.abs(z));
       double G = 0.53070271;
       for (int i = 0; i < 4; ++i)
          G = G*x + AGauss[i];
       G = G * x * Math.exp(-z*z/2.0);
       if (z > 0.0)
          G = 1.0 - G;
       return G;
   }


   protected static double specialCDF (double x, double y, double rho, double xbig) {
      // Compute the bivariate normal CDF for limiting cases and returns
      // its value. If non limiting case, returns -2 as flag.
      // xbig is practical infinity

      if (Math.abs (rho) > 1.0)
         throw new IllegalArgumentException ("|rho| > 1");
      if (x == 0.0 && y == 0.0)
         return 0.25 + Math.asin(rho)/(2.0*Math.PI);

      if (rho == 1.0) {
         if (y < x)
            x = y;
         return NormalDist.cdf01(x);
      }
      if (rho == -1.0) {
         if (y <= -x)
            return 0.0;
         else
            return NormalDist.cdf01(x) - NormalDist.cdf01(-y);
      }
      if (Math.abs (rho) < RHO_SMALL)
         return NormalDist.cdf01(x) * NormalDist.cdf01(y);

      if ((x <= -xbig) || (y <= -xbig))
         return 0.0;
      if (x >= xbig)
         return NormalDist.cdf01(y);
      if (y >= xbig)
         return NormalDist.cdf01(x);

      return -2.0;
   }
   
   /**
    * Computes the standard <EM>binormal</EM> distribution
    *    using the fast Drezner-Wesolowsky method described in. 
    *    The absolute error is expected to be smaller  than 
    * <SPAN CLASS="MATH">2&#8901;10<SUP>-7</SUP></SPAN>.
    * 
    */
   public static double cdf (double x, double y, double rho)  {
      double bvn = specialCDF (x, y, rho, 20.0);
      if (bvn >= 0.0)
         return bvn;
      bvn = 0.0;

      /* prob(x <= h1, y <= h2), where x and y are standard binormal, 
         with rho = corr(x,y),  error < 2e-7.
         Drezner Z., and G.O. Wesolowsky (1990) On the computation of the
         bivariate normal integral.  J. Statist. Comput. Simul. 35:101-107. */

      int i;
      double r1, r2, r3, rr, aa, ab, h3, h5, h6, h7;
      final double h1 = -x;
      double h2 = -y;
      final double h12 = (h1 * h1 + h2 * h2) / 2.;

      if (Math.abs (rho) >= 0.7) {
         r2 = (1. - rho) * (1. + rho);
         r3 = Math.sqrt (r2);
         if (rho < 0.)
            h2 = -h2;
         h3 = h1 * h2;
         if (h3 < 300.)
            h7 = Math.exp (-h3 / 2.);
         else
            h7 = 0.;
         if (r2 != 0.) {
            h6 = Math.abs (h1 - h2);
            h5 = h6 * h6 / 2.;
            h6 /= r3;
            aa = .5 - h3 / 8.;
            ab = 3. - 2. * aa * h5;
            bvn = .13298076 * h6 * ab * (1.0 - Gauss(h6))
               - Math.exp (-h5 / r2) * (ab + aa * r2) * 0.053051647;
//          if (h7 > 0.  && -h3 < 500.0)
            for (i = 0; i < 5; i++) {
               r1 = r3 * Z[i];
               rr = r1 * r1;
               r2 = Math.sqrt (1. - rr);
               bvn -= W[i] * Math.exp (-h5 / rr) * 
                   (Math.exp (-h3 / (1. + r2)) / r2 / h7 - 1. - aa * rr);
            }
         }

         if (rho > 0.)
            bvn = bvn * r3 * h7 + (1.0 - Gauss (Math.max (h1, h2)));
         else if (rho < 0.)
            bvn = (h1 < h2 ? Gauss (h2) - Gauss (h1) : 0.) - bvn * r3 * h7;

      } else {
         h3 = h1 * h2;
         for (i = 0; i < 5; i++) {
            r1 = rho * Z[i];
            r2 = 1. - r1 * r1;
            bvn += W[i] * Math.exp ((r1 * h3 - h12) / r2) / Math.sqrt (r2);
         }
         bvn = (1.0 - Gauss (h1)) * (1.0 - Gauss (h2)) + rho * bvn;
      }

      if (bvn <= 0.0)
         return 0.0;
      if (bvn <= 1.0)
         return bvn;
      return 1.0;
   }


   public double cdf (double x, double y) {
      return cdf ((x-mu1)/sigma1, (y-mu2)/sigma2, rho);
   }
    
   /**
    * Computes the <EM>binormal</EM> distribution function with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *  <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> =
    *    <TT>sigma2</TT> and <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT>.
    *    Uses the fast Drezner-Wesolowsky method described in. 
    *    The absolute error is expected to be smaller  than 
    * <SPAN CLASS="MATH">2&#8901;10<SUP>-7</SUP></SPAN>.
    * 
    */
   public static double cdf (double mu1, double sigma1, double x, 
                             double mu2, double sigma2, double y,
                             double rho)  {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      return cdf (X, Y, rho);
   }


   /**
    * Computes the standard upper <EM>binormal</EM> distribution 
    *    with 
    * <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB> = <I>&#956;</I><SUB>2</SUB> = 0</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> = <I>&#963;</I><SUB>2</SUB> = 1</SPAN>.
    *    Uses the fast Drezner-Wesolowsky method described in. 
    *    The absolute error is expected to be smaller  than 
    * <SPAN CLASS="MATH">2&#8901;10<SUP>-7</SUP></SPAN>.
    * 
    */
   public static double barF (double x, double y, double rho)  {
      return cdf (-x, -y, rho);
    }


   public double barF (double x, double y) {
      return barF ((x-mu1)/sigma1, (y-mu2)/sigma2, rho);
   }

   /**
    * Computes the upper <EM>binormal</EM> distribution function  with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *    <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> =
    *   <TT>sigma2</TT> and <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT>.
    *    Uses the fast Drezner-Wesolowsky method described in. 
    *    The absolute error is expected to be smaller  than 
    * <SPAN CLASS="MATH">2&#8901;10<SUP>-7</SUP></SPAN>.
    * 
    */
   public static double barF (double mu1, double sigma1, double x, 
                              double mu2, double sigma2, double y,
                              double rho)  {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      return barF (X, Y, rho);
   }


   public double[] getMean() {
      return getMean (mu1, mu2, sigma1, sigma2, rho);
   }

   /**
    * Return the mean vector 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (<I>&#956;</I><SUB>1</SUB>, <I>&#956;</I><SUB>2</SUB>)</SPAN> of the binormal distribution.
    * 
    */
   public static double[] getMean(double mu1, double sigma1,
                                  double mu2, double sigma2, double rho) {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

      double mean[] = new double[2];

      mean[0] = mu1;
      mean[1] = mu2;

      return mean;
   }


   public double[][] getCovariance() {
      return getCovariance (mu1, sigma1, mu2, sigma2, rho);
   }

   /**
    * Return the covariance matrix of the binormal distribution.
    * 
    */
   public static double[][] getCovariance (double mu1, double sigma1,
                                           double mu2, double sigma2,
                                           double rho) {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

      double cov[][] = new double[2][2];

      cov[0][0] = sigma1 * sigma1;
      cov[0][1] = rho * sigma1 * sigma2;
      cov[1][0] = cov[0][1];
      cov[1][1] = sigma2 * sigma2;

      return cov;
   }


   public double[][] getCorrelation() {
      return getCovariance (mu1, sigma1, mu2, sigma2, rho);
   }

   /**
    * Return the correlation matrix of the binormal distribution.
    * 
    */
   public static double[][] getCorrelation (double mu1, double sigma1,
                                            double mu2, double sigma2,
                                            double rho) {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

      double corr[][] = new double[2][2];

      corr[0][0] = 1.0;
      corr[0][1] = rho;
      corr[1][0] = rho;
      corr[1][1] = 1.0;

      return corr;
   }

   
   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN>.
    * 
    */
   public double getMu1()  {
      return mu1;
   }

   
   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN>.
    * 
    */
   public double getMu2()  {
      return mu2;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN>.
    * 
    */
   public double getSigma1()  {
      return sigma1;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN>.
    * 
    */
   public double getSigma2()  {
      return sigma2;
   }


   /**
    * Sets the parameters  <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *  <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = 
    *   <TT>sigma2</TT> and <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> of this object.
    * 
    */
   protected void setParams (double mu1, double sigma1, 
                             double mu2, double sigma2, double rho)  {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");
      this.dimension = 2;
      this.mu1 = mu1;
      this.sigma1 = sigma1;
      this.mu2 = mu2;
      this.sigma2 = sigma2;
      this.rho = rho; 
      racRho = Math.sqrt((1.0 - rho)*(1.0 + rho));
      detS = 2.0*Math.PI*sigma1*sigma2*racRho;
   }
 
}
