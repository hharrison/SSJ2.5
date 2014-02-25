

/*
 * Class:        BiStudentDist
 * Description:  standard bivariate Student t-distribution
 * Environment:  Java
 * Software:     SSJ 
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since
 */

package umontreal.iro.lecuyer.probdistmulti;

import umontreal.iro.lecuyer.probdist.StudentDist;


/**
 * Extends the class {@link ContinuousDistribution2Dim} for the <EM> standard  bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN></EM> distribution. 
 *  The correlation between <SPAN CLASS="MATH"><I>X</I></SPAN> and <SPAN CLASS="MATH"><I>Y</I></SPAN> is <SPAN CLASS="MATH"><I>r</I></SPAN>
 *  and the number of degrees of freedom is <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
 * Its probability density is 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>, <I>y</I>) = (1 + (<I>x</I><SUP>2</SUP> -2<I>&#961;xy</I> + <I>y</I><SUP>2</SUP>)/(<I>&#957;</I>(1 - <I>&#961;</I><SUP>2</SUP>)))<SUP>-(<I>&#957;</I>+2)/2</SUP>/(2<I>&#960;</I>(1-r^2)<SUP>1/2</SUP>),
 * </DIV><P></P>
 * and the corresponding distribution function (the <TT>cdf</TT>) is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>T</I><SUB><I>&#957;</I></SUB>(<I>x</I>, <I>y</I>, <I>r</I>) = &int;<SUB>-&#8734;</SUB><SUP>x</SUP><I>dx</I>&int;<SUB>-&#8734;</SUB><SUP>y</SUP><I>dy</I>&nbsp;<I>f</I> (<I>x</I>, <I>y</I>)/(2<I>&#960;</I>(1 - r^2)<SUP>1/2</SUP>).
 * </DIV><P></P>
 * We also define the upper distribution function called <TT>barF</TT> as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * bar(T)<SUB><I>&#957;</I></SUB>(<I>x</I>, <I>y</I>, <I>r</I>) = &int;<SUP>&#8734;</SUP><SUB>x</SUB><I>dx</I>&int;<SUP>&#8734;</SUP><SUB>y</SUB><I>dy</I>&nbsp;<I>f</I> (<I>x</I>, <I>y</I>)/(2<I>&#960;</I>(1 - r^2)<SUP>1/2</SUP>).
 * </DIV><P></P>
 * 
 */
public class BiStudentDist extends ContinuousDistribution2Dim  {
   protected int nu;               // Number of degrees of freedom
   protected double rho;
   protected double facRho;        // sqrt(1 - rho^2)




   /**
    * Constructs a <TT>BiStudentDist</TT> object with correlation <SPAN CLASS="MATH"><I>&#961;</I> =</SPAN><TT> rho</TT>
    *   and <SPAN CLASS="MATH"><I>&#957;</I></SPAN> = <TT>nu</TT> degrees of freedom.
    * 
    */
   public BiStudentDist (int nu, double rho)  {
      setParams (nu, rho);
   }


   public double density (double x, double y) {
      if (Math.abs(rho) == 1.)
         throw new IllegalArgumentException ("|rho| = 1");
      double T = 1.0 + (x*x - 2.0*rho*x*y + y*y) / (nu*facRho*facRho);
      return 1.0 / (Math.pow (T, (nu + 2)/2.0) * (2.0*Math.PI*facRho));
   }

   public double cdf (double x, double y) {
      return cdf (nu, x, y, rho);
   }

   public double barF (double x, double y) {
      return barF (nu, x, y, rho);
   }


   /**
    * Computes the standard bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> density function with correlation <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> and
    *    <SPAN CLASS="MATH"><I>&#957;</I></SPAN> = <TT>nu</TT>  degrees  of freedom.
    * 
    */
   public static double density (int nu, double x, double y, double rho)  {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
      if (Math.abs(rho) >= 1.)
         throw new IllegalArgumentException ("|rho| >= 1");
      double fRho = (1.0 - rho)*(1.0 + rho);
      double T = 1.0 + (x*x - 2.0*rho*x*y + y*y) / (nu*fRho);
      return 1.0 / (Math.pow (T, (nu + 2)/2.0) * (2.0*Math.PI*Math.sqrt(fRho)));
   }


   /**
    * Computes the standard bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> distribution
    *    using the method described in. The code for the <TT>cdf</TT> 
    *   was translated directly from the Matlab code written by Alan Genz
    *   and available from his web page at
    *   <TT><A NAME="tex2html1"
    *   HREF="http://www.math.wsu.edu/faculty/genz/homepage">http://www.math.wsu.edu/faculty/genz/homepage</A></TT>  (the code is copyrighted by Alan Genz 
    *   and is included in this package with the kind permission of the author).
    *   The correlation is <SPAN CLASS="MATH"><I>&#961;</I> =</SPAN><TT> rho</TT> and the number of 
    *   degrees of freedom is <SPAN CLASS="MATH"><I>&#957;</I></SPAN> = <TT>nu</TT>.
    *  
    */
   public static double cdf (int nu, double x, double y, double rho)  {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

// This function is translated from Alan Genz's Matlab code.
/*
% function p = bvtl( nu, dh, dk, r )
%
%    A function for computing bivariate t probabilities.
%    bvtl calculates the probability that x < dh and y < dk;
%   parameters
%     nu number of degrees of freedom
%     dh 1st upper integration limit
%     dk 2nd upper integration limit
%     r   correlation coefficient
%
%        This function is based on the method described by 
%          Dunnett, C.W. and M. Sobel, (1954),
%          A bivariate generalization of Student's t-distribution
%          with tables for certain special cases,
%          Biometrika 41, pp. 153-169,
%
%       Alan Genz
%       Department of Mathematics
%       Washington State University
%       Pullman, Wa 99164-3113
%       Email : alangenz@wsu.edu
%
*/
      final double dh = x;
      final double dk = y;
      final double eps = 1.0e-15;
      final double tpi = 2.*Math.PI;
      double hrk, krh, bvt, snu;
      double gmph, gmpk, xnkh, xnhk, qhrk, hkn, hpk, hkrn;
      double btnckh, btnchk, btpdkh, btpdhk;
       
      if (1. - rho <= eps) {
         x = Math.min (dh, dk);
         return StudentDist.cdf (nu, x);
      }
      if (rho + 1. <= eps) {
         if (dh > -dk)  
            return StudentDist.cdf(nu, dh) - StudentDist.cdf (nu, -dk);
         else
            return 0.;
      }
      final double ors = (1. - rho)*(1. + rho);
      hrk = dh - rho*dk; krh = dk - rho*dh; 
      if (Math.abs(hrk) + ors > 0.) {
        xnhk = hrk*hrk/( hrk*hrk + ors*( nu + dk*dk));
        xnkh = krh*krh/( krh*krh + ors*( nu + dh*dh));
      } else {
        xnhk = 0.;
        xnkh = 0.; 
      }
      int hs, ks, j;
      if (dh - rho*dk > 0.)
         hs = 1;
      else if (dh - rho*dk < 0.)
         hs = -1;
      else
         hs = 0;
      if (dk - rho*dh > 0.)
         ks = 1;
      else if (dk - rho*dh < 0.)
         ks = -1;
      else
         ks = 0;
      if (nu % 2 == 0) {
         bvt = Math.atan2 (Math.sqrt(ors), -rho)/tpi;
         gmph = dh/Math.sqrt (16.*( nu + dh*dh));
         gmpk = dk/Math.sqrt (16.*( nu + dk*dk)); 
         btnckh = 2.*Math.atan2 (Math.sqrt(xnkh), Math.sqrt(1. - xnkh))/Math.PI;
         btpdkh = 2.*Math.sqrt( xnkh*( 1. - xnkh))/Math.PI;
         btnchk = 2.*Math.atan2( Math.sqrt(xnhk), Math.sqrt(1. - xnhk))/Math.PI;
         btpdhk = 2.*Math.sqrt( xnhk*( 1. - xnhk))/Math.PI;
         for (j = 1; j <= nu/2; ++j) {
            bvt = bvt + gmph*( 1. + ks*btnckh); 
            bvt = bvt + gmpk*( 1. + hs*btnchk);
            btnckh = btnckh + btpdkh;
            btpdkh = 2*j*btpdkh*( 1. - xnkh )/(2*j+1); 
            btnchk = btnchk + btpdhk;
            btpdhk = 2*j*btpdhk*( 1. - xnhk )/(2*j+1); 
            gmph = gmph*( j - 0.5 )/( j*( 1. + dh*dh/nu));
            gmpk = gmpk*( j - 0.5 )/( j*( 1. + dk*dk/nu));
         }

      } else {
         qhrk = Math.sqrt( dh*dh + dk*dk - 2.*rho*dh*dk + nu*ors); 
         hkrn = dh*dk + rho*nu; hkn = dh*dk - nu; hpk = dh + dk;
         bvt = Math.atan2( -Math.sqrt(nu)*(hkn*qhrk+hpk*hkrn),
                            hkn*hkrn-nu*hpk*qhrk )/tpi; 
         if (bvt < -10.*eps)
            bvt = bvt + 1;
         gmph = dh/( tpi*Math.sqrt(nu)*( 1. + dh*dh/nu)); 
         gmpk = dk/( tpi*Math.sqrt(nu)*( 1. + dk*dk/nu)); 
         btnckh = Math.sqrt( xnkh ); btpdkh = btnckh;
         btnchk = Math.sqrt( xnhk ); btpdhk = btnchk; 
         for (j = 1; j <=  (nu - 1)/2; ++j) {
            bvt = bvt + gmph*( 1. + ks*btnckh ); 
            bvt = bvt + gmpk*( 1. + hs*btnchk );
            btpdkh = (2*j-1)*btpdkh*( 1. - xnkh )/(2*j);
            btnckh = btnckh + btpdkh; 
            btpdhk = (2*j-1)*btpdhk*( 1. - xnhk )/(2*j);
            btnchk = btnchk + btpdhk; 
            gmph = gmph*j/(( j + 0.5 )*( 1. + dh*dh/nu));
            gmpk = gmpk*j/(( j + 0.5 )*( 1. + dk*dk/nu));
         }
      }
      return bvt;
   }


   /**
    * Computes the standard upper bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> distribution.
    * 
    */
   public static double barF (int nu, double x, double y, double rho) 
   {
      double u = 1.0 + cdf (nu, x, y, rho) - cdf (nu, XBIG, y, rho) -
                 cdf (nu, x, XBIG, rho);
      final double eps = 1.0e-15;
      if (u < eps) return 0.0;
      if (u <= 1.0) return u;
      return 1.0;
    }


   public double[] getMean() {
      return getMean (nu, rho);
   }

   /**
    * Returns the mean vector 
    * <SPAN CLASS="MATH"><I>E</I>[<I>X</I>] = (0, 0)</SPAN> of the bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> distribution.
    * 
    */
   public static double[] getMean (int nu, double rho) {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

      double mean[] = new double[2];

      mean[0] = 0;
      mean[1] = 0;

      return mean;
   }


   public double[][] getCovariance() {
      return getCovariance (nu, rho);
   }

   /**
    * Returns the covariance matrix of the bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> distribution.
    * 
    */
   public static double[][] getCovariance (int nu, double rho) {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");

      double cov[][] = new double[2][2];

      double coeff = (double) nu / ((double) nu - 2.0);

      cov[0][0] = coeff;
      cov[0][1] = coeff * rho;
      cov[1][0] = coeff * rho;
      cov[1][1] = coeff;

      return cov;
   }


   public double[][] getCorrelation() {
      return getCovariance (nu, rho);
   }

   /**
    * Returns the correlation matrix of the bivariate Student's <SPAN CLASS="MATH"><I>t</I></SPAN> distribution.
    * 
    */
   public static double[][] getCorrelation (int nu, double rho) {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
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
    * Sets the parameters <SPAN CLASS="MATH"><I>&#957;</I></SPAN> = <TT>nu</TT> and
    *   <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> of this object.
    * 
    */
   protected void setParams (int nu, double rho)  {
      if (nu < 1)
         throw new IllegalArgumentException ("nu < 1");
      if (Math.abs(rho) > 1.)
         throw new IllegalArgumentException ("|rho| > 1");
      this.dimension = 2;
      this.nu = nu;
      this.rho = rho; 
      facRho = Math.sqrt((1.0 - rho)*(1.0 + rho));
   }
 
}
