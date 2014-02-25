

/*
 * Class:        BiNormalDonnellyDist
 * Description:  bivariate normal distribution using Donnelly's code
 * Environment:  Java
 * Software:     SSJ 
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since
 */

package umontreal.iro.lecuyer.probdistmulti;

import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.util.Num;


/**
 * Extends the class {@link BiNormalDist} for the <EM>bivariate 
 *   normal</EM> distribution
 *   using a translation of Donnelly's F<SMALL>ORTRAN</SMALL> code.
 * 
 */
public class BiNormalDonnellyDist extends BiNormalDist  {

private static final double TWOPI = 2.0*Math.PI;
private static final double SQRTPI = Math.sqrt(Math.PI);
private static final int KMAX = 6;

private static double BB[] = new double[KMAX + 1];
private static final double CB[] = {
    0.9999936, -0.9992989, 0.9872976,
   -0.9109973, 0.6829098, -0.3360210, 0.07612251 };


private static double BorthT (double h, double a)
{
   int k;
   final double w = a * h / Num.RAC2;
   final double w2 = w * w;
   double z = w * Math.exp(-w2);

   BB[0] = SQRTPI * (Gauss (Num.RAC2 * w) - 0.5);
   for (k = 1; k <= KMAX; ++k) {
      BB[k] = ((2 * k - 1) * BB[k - 1] - z) / 2.0;
      z *= w2;
   }

   final double h2 = h * h / 2.0;
   z = h / Num.RAC2;
   double sum = 0;
   for (k = 0; k <= KMAX; ++k) {
      sum += CB[k] * BB[k] / z;
      z *= h2;
   }
   return sum * Math.exp (-h2) / TWOPI;
}





   /**
    * Constructor with default  parameters
    *  
    * <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB> = <I>&#956;</I><SUB>2</SUB> = 0</SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB> = <I>&#963;</I><SUB>2</SUB> = 1</SPAN>, correlation
    *  <SPAN CLASS="MATH"><I>&#961;</I> =</SPAN><TT> rho</TT>, and <SPAN CLASS="MATH"><I>d</I> =</SPAN> <TT>ndig</TT> digits of accuracy 
    *  (the absolute error is smaller than <SPAN CLASS="MATH">10<SUP>-d</SUP></SPAN>). Restriction: <SPAN CLASS="MATH"><I>d</I>&nbsp;&lt;=&nbsp;15</SPAN>.
    * 
    */
   public BiNormalDonnellyDist (double rho, int ndig)  {
       super (rho);
       if (ndig > 15)
          throw new IllegalArgumentException ("ndig > 15");
       decPrec = ndigit = ndig;
   }


   /**
    * Same as {@link #BiNormalDonnellyDist(double,int) BiNormalDonnellyDist}&nbsp;<TT>(rho, 15)</TT>.
    * 
    */
   public BiNormalDonnellyDist (double rho)  {
       this (rho, 15);
   }


   /**
    * Constructor with parameters
    *   <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>, <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>, 
    *  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = <TT>sigma2</TT>, <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT>, and <SPAN CLASS="MATH"><I>d</I> =</SPAN> <TT>ndig</TT>
    *   digits of accuracy. Restriction: <SPAN CLASS="MATH"><I>d</I>&nbsp;&lt;=&nbsp;15</SPAN>.
    * 
    */
   public BiNormalDonnellyDist (double mu1, double sigma1, double mu2,
                                double sigma2, double rho, int ndig)  {
      super (mu1, sigma1, mu2, sigma2, rho);
      if (ndig > 15)
         throw new IllegalArgumentException ("ndig > 15");
      decPrec = ndigit = ndig;
   }


   /**
    * Same as {@link #BiNormalDonnellyDist(double,double,double,double,double,int) BiNormalDonnellyDist}&nbsp;<TT>(mu1, sigma1, mu2, sigma2, rho, 15)</TT>.
    * 
    */
   public BiNormalDonnellyDist (double mu1, double sigma1, double mu2,
                                double sigma2, double rho)  {
      this (mu1, sigma1, mu2, sigma2, rho, 15);
   }


   /**
    * Computes the standard <EM>binormal</EM> distribution
    *   with the method described in, where <TT>ndig</TT> is the
    *   number of decimal digits of accuracy provided (<TT>ndig</TT> <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;15</SPAN>).
    *   The code was translated from the Fortran program written by T. G. Donnelly
    *   and copyrighted by the ACM (see 
    *   <TT><A NAME="tex2html1"
    *   HREF="http://www.acm.org/pubs/copyright_policy/#Notice">http://www.acm.org/pubs/copyright_policy/#Notice</A></TT>). The absolute error
    *   is expected to be smaller than <SPAN CLASS="MATH">10<SUP>-d</SUP></SPAN>, where <SPAN CLASS="MATH"><I>d</I> =</SPAN> <TT>ndig</TT>.
    * 
    */
   public static double cdf (double x, double y, double rho, int ndig)  {
   /* 
    * This is a translation of the FORTRAN code published by Thomas G. Donnelly
    * in the CACM, Vol. 16, Number 10, p. 638, (1973)
    */

      if (ndig > 15)
         throw new IllegalArgumentException ("ndig > 15");
      double b = specialCDF (x, y, rho, 13.0);
      if (b >= 0.0)
         return b;
      b = 0;

      final boolean SINGLE_FLAG = ndig <= 7 ? true : false;
      final double TWO_PI = 2.0 * Math.PI;
      final double r = rho;
      final double ah = -x;
      final double ak = -y;

      double a2, ap, cn, conex, ex, g2, gh, gk, gw = 0, h2, h4, rr, s1, s2,
         sgn, sn, sp, sqr, t, temp, w2, wh = 0, wk = 0;
      int is = -1;

      if (SINGLE_FLAG) {
         gh = Gauss (x) / 2.0;
         gk = Gauss (y) / 2.0;
      } else {
         gh = NormalDist.cdf01 (x) / 2.0;
         gk = NormalDist.cdf01 (y) / 2.0;
      }
      boolean flag = true;    // Easiest way to translate a Fortran goto

      rr = (1 - r) * (1 + r);
      if (rr < 0)
         throw new IllegalArgumentException ("|rho| > 1");
      sqr = Math.sqrt(rr);
      final double con = Math.PI * Num.TEN_NEG_POW[ndig];
      final double EPSILON = 0.5*Num.TEN_NEG_POW[ndig];

      if (ah != 0) {
         b = gh;
         if (ah * ak < 0)
            b -= 0.5;
         else if (ah * ak == 0) {
            flag = false;
         }
      } else if (ak == 0) {
         return Math.asin (r) / TWO_PI + 0.25;
      }

      if (flag)
         b += gk;
      if (ah != 0) {
         flag = false;
         wh = -ah;
         wk = (ak / ah - r) / sqr;
         gw = 2 * gh;
         is = -1;
      }

      do {
         if (flag) {
            wh = -ak;
            wk = (ah / ak - r) / sqr;
            gw = 2 * gk;
            is = 1;
         }
         flag = true;
         sgn = -1;
         t = 0;
         if (wk != 0) {
            if (Math.abs (wk) >= 1)
               if (Math.abs (wk) == 1) {
                  t = wk * gw * (1 - gw) / 2;
                  b += sgn * t;
                  if (is >= 0)        // Another Fortran goto
                     break;
                  else
                     continue;
               } else {
                  sgn = -sgn;
                  wh = wh * wk;
                  if (SINGLE_FLAG)
                     g2 = Gauss(wh);
                  else
                     g2 = NormalDist.cdf01(wh);
                  wk = 1 / wk;
                  if (wk < 0)
                     b = b + .5;
                  b = b - (gw + g2) / 2 + gw * g2;
               }
        /* ****
            // Cette m'ethode de Borth est plus lente que simple Donnelly 
            if (ndig <= 7 && Math.abs (wh) > 1.6 && Math.abs (wk) > 0.3) {
               b += sgn * BorthT (wh, wk);
               if (is >= 0)
                  break;
               else
                  continue;
            }
        *****/
            h2 = wh * wh;
            a2 = wk * wk;
            h4 = h2 * .5;
            ex = 0;
            if (h4 < 300.0)
               ex = Math.exp (-h4);
            w2 = h4 * ex;
            ap = 1;
            s2 = ap - ex;
            sp = ap;
            s1 = 0;
            sn = s1;
            conex = Math.abs (con / wk);
            do {
               cn = ap * s2 / (sn + sp);
               s1 += cn;
               if (Math.abs (cn) <= conex)
                  break;
               sn = sp;
               sp += 1;
               s2 -= w2;
               w2 *= h4 / sp;
               ap = -ap * a2;
            } while (true);
            t = (Math.atan (wk) - wk * s1) / TWO_PI;
            b += sgn * t;
         }
         if (is >= 0)
            break;
      } while (ak != 0);

      if (b < EPSILON)
         b = 0;
      if (b > 1)
         b = 1;
      return b;
}


   /**
    * Computes the <EM>binormal</EM> distribution function with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *  <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = <TT>sigma2</TT>,
    *  correlation <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> and <TT>ndig</TT> decimal digits of accuracy.
    * 
    */
   public static double cdf (double mu1, double sigma1, double x, 
                             double mu2, double sigma2, double y,
                             double rho, int ndig)  {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      return cdf (X, Y, rho, ndig);
   }


   /**
    * Computes the upper <EM>binormal</EM> distribution function  with parameters <SPAN CLASS="MATH"><I>&#956;</I><SUB>1</SUB></SPAN> = <TT>mu1</TT>,
    *  <SPAN CLASS="MATH"><I>&#956;</I><SUB>2</SUB></SPAN> = <TT>mu2</TT>, <SPAN CLASS="MATH"><I>&#963;</I><SUB>1</SUB></SPAN> = <TT>sigma1</TT>,  <SPAN CLASS="MATH"><I>&#963;</I><SUB>2</SUB></SPAN> = <TT>sigma2</TT>,
    *  <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> and <TT>ndig</TT> decimal digits of accuracy.
    * 
    */
   public static double barF (double mu1, double sigma1, double x, 
                              double mu2, double sigma2, double y,
                              double rho, int ndig)  {
      if (sigma1 <= 0)
         throw new IllegalArgumentException ("sigma1 <= 0");
      if (sigma2 <= 0)
         throw new IllegalArgumentException ("sigma2 <= 0");
      double X = (x - mu1)/sigma1;
      double Y = (y - mu2)/sigma2;
      return barF (X, Y, rho, ndig);
   }


   /**
    * Computes the upper <EM>standard binormal</EM> distribution function  with parameters <SPAN CLASS="MATH"><I>&#961;</I></SPAN> = <TT>rho</TT> and 
    *    <TT>ndig</TT> decimal digits of accuracy.
    * 
    */
   public static double barF (double x, double y, double rho, int ndig)   {
      return cdf (-x, -y, rho, ndig);
   }
  

   public double cdf (double x, double y) {
      return cdf ((x-mu1)/sigma1, (y-mu2)/sigma2, rho, ndigit);
   }

   public static double cdf (double x, double y, double rho) {
       return cdf (x, y, rho, 15);
   }
 
   public static double cdf (double mu1, double sigma1, double x, 
                             double mu2, double sigma2, double y,
                             double rho) {
      return cdf (mu1, sigma1, x, mu2, sigma2, y, rho, 15);
   }

   public double barF (double x, double y) {
      return barF ((x-mu1)/sigma1, (y-mu2)/sigma2, rho, ndigit);
   }

   public static double barF (double mu1, double sigma1, double x, 
                              double mu2, double sigma2, double y,
                              double rho) {
      return barF (mu1, sigma1, x, mu2, sigma2, y, rho, 15);
   }

   public static double barF (double x, double y, double rho) {
      return barF (x, y, rho, 15);
   }
}
