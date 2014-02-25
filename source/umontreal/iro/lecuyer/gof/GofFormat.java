

/*
 * Class:        GofFormat
 * Description:  
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

import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.probdist.*;
import java.io.PrintWriter;

/**
 * This class contains methods used to format results of GOF
 * test statistics, or to apply a series of tests
 * simultaneously and format the results.
 * It is in fact a translation from C to Java of a set of functions that
 * were specially written for the implementation of TestU01, a software
 * package for testing uniform random number generators.
 * 
 * <P>
 * Strictly speaking, applying several tests simultaneously makes the
 * <SPAN CLASS="MATH"><I>p</I></SPAN>-values ``invalid'' in the sense that the probability of having
 * <EM>at least one</EM> <SPAN CLASS="MATH"><I>p</I></SPAN>-value less than 0.01, say, is larger than 0.01.
 * One must therefore be careful with the interpretation of these
 * <SPAN CLASS="MATH"><I>p</I></SPAN>-values (one could use, e.g., the Bonferroni inequality).
 * Applying simultaneous tests is convenient in some situations, such as in
 * screening experiments for detecting statistical deficiencies
 * in random number generators.  In that context, rejection of the null
 * hypothesis typically occurs with extremely small <SPAN CLASS="MATH"><I>p</I></SPAN>-values (e.g., less
 * than <SPAN CLASS="MATH">10<SUP>-15</SUP></SPAN>), and the interpretation is quite obvious in this case.
 * 
 * <P>
 * The class also provides tools to plot an empirical or
 * theoretical distribution function, by creating a data file that
 * contains a graphic plot in a format compatible with the software
 * specified by the environment variable {@link #graphSoft graphSoft}.
 *   NOTE: see also the more recent package
 *   {@link umontreal.iro.lecuyer.charts charts}.
 * 
 * <P>
 * Note: This class uses the Colt library.
 * 
 */
public class GofFormat {
   private GofFormat() {} 


   /**
    * Data file format used for plotting functions with Gnuplot.
    * 
    */
   public static final int GNUPLOT = 0; 


   /**
    * Data file format used for creating graphics with Mathematica.
    * 
    */
   public static final int MATHEMATICA = 1; 


   /**
    * Environment variable that selects the type of software to be
    *    used for plotting the graphs of functions.
    *    The data files produced by {@link #graphFunc graphFunc} and
    *    {@link #graphDistUnif graphDistUnif} will be in a format suitable
    *    for this selected software.
    *    The default value is <TT>GNUPLOT</TT>.
    *    To display a graphic in file <TT>f</TT> using <TT>gnuplot</TT>, for example,
    *    one can use the command ``<TT>plot f with steps, x with lines</TT>''
    *    in <TT>gnuplot</TT>.
    * <TT>graphSoft</TT> can take the values {@link #GNUPLOT GNUPLOT} or {@link #MATHEMATICA MATHEMATICA}.
    * 
    */
   public static int graphSoft = GNUPLOT;


   private static String formatMath2 (double x, double y)    {
      // Writes the pair (x, y) in file f, in a format understood
      // by Mathematica
      StringBuffer sb = new StringBuffer();
      String S;

      sb.append ("   { ");
      if ((x != 0.0) && (x < 0.1 || x > 1.0)) {
         S = PrintfFormat.E (16, 7, x);
         int exppos = S.indexOf ('E');
         if (exppos != -1)
            S = S.substring (0, exppos) + "*10^(" +
                             S.substring (exppos+1) + ")";
      }
      else
         S = PrintfFormat.g (16, 8, x);

      sb.append (S + ",     ");

      if (y != 0.0 && (y < 0.1 || y > 1.0)) {
         S = PrintfFormat.E (16, 7, y);
         int exppos = S.indexOf ('E');
         if (exppos != -1)
            S = S.substring (0, exppos) + "*10^(" +
                             S.substring (exppos+1) + ")";
      }
      else
        S = PrintfFormat.g (16, 8, y);

      sb.append (S + " }");
      return sb.toString();
   }


   private static String graphFunc (ContinuousDistribution dist, double a,
                                    double b, int m, int mono, String desc) {
// Renommer drawCDF en fixant mono = 1 et éliminant mono.
      int i;
      double yprec, y, x, h;
      StringBuffer sb = new StringBuffer();
      String openComment = "";
      String closeComment = "";
      String openGraph = "";
      String closeGraph = "";
      if (mono != 1 && mono != -1)
         throw new IllegalArgumentException ("mono must be 1 or -1");
      switch (graphSoft) {
      case GNUPLOT:
        openComment = "# ";
        closeComment = "";
        openGraph = "";
        closeGraph = PrintfFormat.NEWLINE;
        break;
      case MATHEMATICA:
        openComment = "(* ";
        closeComment = " *)";
        openGraph = "points = { " + PrintfFormat.NEWLINE;
        closeGraph = "}" + PrintfFormat.NEWLINE;
        break;
      }

      sb.append (openComment + "----------------------------------" +
                   closeComment  + PrintfFormat.NEWLINE);
      sb.append (openComment + PrintfFormat.s (-70, desc)
                 + closeComment  + PrintfFormat.NEWLINE +
                   PrintfFormat.NEWLINE);

      sb.append (openGraph);
      h = (b - a) / m;
      if (mono == 1)
         yprec = -Double.MAX_VALUE;
      else if (mono == -1)
         yprec = Double.MAX_VALUE;
      else
         yprec = 0.0;

      for (i = 0; i <= m; i++) {
         x = a + i*h;
         y = mono == 1 ? dist.cdf (x) : dist.barF (x);
         switch (graphSoft) {
         case MATHEMATICA:
            sb.append (formatMath2 (x, y));
            if (i < m)
               sb.append (',');
            break;
         default: // Default and GNUPLOT
            sb.append (PrintfFormat.g (20, 14, x) +  "      " +
                       PrintfFormat.g (20, 14, y));
         }

         switch (mono) {
         case 1:
            if (y < yprec)
               sb.append ("    " + openComment +
                    "  DECREASING" + closeComment);
            break;
         case -1:
            if (y > yprec)
               sb.append ("    " + openComment +
                    "  INCREASING" + closeComment);
            break;
         default:
            break;
         }
         sb.append (PrintfFormat.NEWLINE);
         yprec = y;
      }
      sb.append (closeGraph);
      return sb.toString();
   }


   /**
    * Formats data to plot the graph of the distribution function <SPAN CLASS="MATH"><I>F</I></SPAN> over the
    *   interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>, and returns the result as a {@link String}.
    *   The method <TT>dist.cdf(x)</TT> returns the value of <SPAN CLASS="MATH"><I>F</I></SPAN> at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    * The {@link String} <TT>desc</TT> gives a short caption for the graphic plot.
    *   The method computes the <SPAN CLASS="MATH"><I>m</I> + 1</SPAN> points 
    * <SPAN CLASS="MATH">(<I>x</I><SUB>i</SUB>,&nbsp;<I>F</I>(<I>x</I><SUB>i</SUB>))</SPAN>,
    *   where 
    * <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB> = <I>a</I> + <I>i</I>(<I>b</I> - <I>a</I>)/<I>m</I></SPAN> for 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I></SPAN>, and formats these points
    *   into a <TT>String</TT> in a format suitable for the
    *   software specified by {@link #graphSoft graphSoft}.
    *   NOTE: see also the more recent class
    *   {@link umontreal.iro.lecuyer.charts.ContinuousDistChart ContinuousDistChart}.
    *  
    * @param dist continuous distribution function to plot
    * 
    *    @param a lower bound of the interval to plot
    * 
    *    @param b upper bound of the interval to plot
    * 
    *    @param m number of points in the plot minus one
    * 
    *    @param desc short caption describing the plot
    * 
    *    @return a string representation of the plot data
    * 
    */
   public static String drawCdf (ContinuousDistribution dist, double a,
                                 double b, int m, String desc) {
      return graphFunc (dist, a, b, m, 1, desc);
   }


   /**
    * Formats data to plot the graph of the density <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> over the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>,
    *   and returns the result as a {@link String}. The method
    *   <TT>dist.density(x)</TT> returns the value of <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> at <SPAN CLASS="MATH"><I>x</I></SPAN>.
    *   The {@link String} <TT>desc</TT> gives a short caption for the graphic
    *   plot. The method computes the <SPAN CLASS="MATH"><I>m</I> + 1</SPAN> points 
    * <SPAN CLASS="MATH">(<I>x</I><SUB>i</SUB>,&nbsp;<I>f</I> (<I>x</I><SUB>i</SUB>))</SPAN>,
    *   where 
    * <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB> = <I>a</I> + <I>i</I>(<I>b</I> - <I>a</I>)/<I>m</I></SPAN> for 
    * <SPAN CLASS="MATH"><I>i</I> = 0, 1,&#8230;, <I>m</I></SPAN>, and formats these points
    *   into a <TT>String</TT> in a format suitable for the
    *   software specified by {@link #graphSoft graphSoft}.
    *   NOTE: see also the more recent class
    *   {@link umontreal.iro.lecuyer.charts.ContinuousDistChart ContinuousDistChart}.
    *  
    * @param dist continuous density function to plot
    * 
    *    @param a lower bound of the interval to plot
    * 
    *    @param b upper bound of the interval to plot
    * 
    *    @param m number of points in the plot minus one
    * 
    *    @param desc short caption describing the plot
    * 
    *    @return a string representation of the plot data
    * 
    */
   public static String drawDensity (ContinuousDistribution dist, double a,
                                     double b, int m, String desc) {
      int i;
      double y, x, h;
      StringBuffer sb = new StringBuffer();
      String openComment = "";
      String closeComment = "";
      String openGraph = "";
      String closeGraph = "";

      switch (graphSoft) {
      case GNUPLOT:
        openComment = "# ";
        closeComment = "";
        openGraph = "";
        closeGraph = PrintfFormat.NEWLINE;
        break;
      case MATHEMATICA:
        openComment = "(* ";
        closeComment = " *)";
        openGraph = "points = { " + PrintfFormat.NEWLINE;
        closeGraph = "}" + PrintfFormat.NEWLINE;
        break;
      }

      sb.append (openComment + "----------------------------------" +
                   closeComment  + PrintfFormat.NEWLINE);
      sb.append (openComment + PrintfFormat.s (-70, desc)
                    + closeComment  + PrintfFormat.NEWLINE +
                      PrintfFormat.NEWLINE);

      sb.append (openGraph);
      h = (b - a) / m;

      for (i = 0; i <= m; i++) {
         x = a + i*h;
         y = dist.density (x);

         switch (graphSoft) {
         case MATHEMATICA:
            sb.append (formatMath2 (x, y));
            if (i < m)
               sb.append (',');
            break;
         default: // Default and GNUPLOT
            sb.append (PrintfFormat.g (16, 8, x) +  "      " +
                       PrintfFormat.g (16, 8, y));
         }
         sb.append (PrintfFormat.NEWLINE);
      }
      sb.append (closeGraph);
      return sb.toString();
   }


   /**
    * Formats data to plot the empirical distribution of
    *   
    * <SPAN CLASS="MATH"><I>U</I><SUB>(1)</SUB>,..., <I>U</I><SUB>(N)</SUB></SPAN>, which are assumed to be in <TT>data[0...N-1]</TT>,
    *   and to compare it with the uniform distribution. The <SPAN CLASS="MATH"><I>U</I><SUB>(i)</SUB></SPAN> must be sorted.
    *   The two endpoints <SPAN CLASS="MATH">(0, 0)</SPAN> and <SPAN CLASS="MATH">(1, 1)</SPAN> are always included in the plot.
    *   The string <TT>desc</TT> gives a short caption for the graphic plot.
    *   The data is printed in a format suitable for the
    *   software specified by {@link #graphSoft graphSoft}.
    *    NOTE: see also the more recent class
    *   {@link umontreal.iro.lecuyer.charts.EmpiricalChart EmpiricalChart}.
    *  
    * @param data array of observations to plot
    * 
    *    @param desc short caption describing the plot
    * 
    *    @return a string representation of the plot data
    * 
    */
   public static String graphDistUnif (DoubleArrayList data, String desc) {
      double[] u = data.elements();
      int n = data.size();
      int i;
      double unSurN = 1.0/n;
      StringBuffer sb = new StringBuffer();

      switch (graphSoft) {
      case GNUPLOT:
         sb.append ("#----------------------------------" +
                     PrintfFormat.NEWLINE);
         sb.append ("# " + PrintfFormat.s (-70, desc) +
                     PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
         sb.append (PrintfFormat.g (16, 8, 0.0) + "  " +
                    PrintfFormat.g (16, 8, 0.0) + PrintfFormat.NEWLINE);
         for (i = 0; i < n; i++)
            sb.append (PrintfFormat.g (16, 8, u[i]) + "  " +
                       PrintfFormat.g (16, 8, (i + 1)*unSurN) +
                       PrintfFormat.NEWLINE);

         sb.append (PrintfFormat.g (16, 8, 1.0) + "  " +
                    PrintfFormat.g (16, 8, 1.0) + PrintfFormat.NEWLINE +
                    PrintfFormat.NEWLINE);
         break;
      case MATHEMATICA:
         sb.append ("(*----------------------------------*)" +
                     PrintfFormat.NEWLINE);
         sb.append ("(* " + PrintfFormat.s (-70, desc)  +
                     PrintfFormat.NEWLINE + " *)" +
                     PrintfFormat.NEWLINE + PrintfFormat.NEWLINE +
                     "points = { " + PrintfFormat.NEWLINE);

         sb.append (formatMath2 (0.0, 0.0) + "," + PrintfFormat.NEWLINE);
         for (i = 0; i < n; i++)
            sb.append (formatMath2 (u[i], (i + 1)*unSurN) + "," +
                       PrintfFormat.NEWLINE);
         sb.append (formatMath2 (1.0, 1.0) + PrintfFormat.NEWLINE);
         break;
      default:
         throw new IllegalArgumentException ("graphSoft unknown");
      }
      return sb.toString();
   }



   /**
    * Environment variable used in {@link #formatp0 formatp0} to determine
    *    which <SPAN CLASS="MATH"><I>p</I></SPAN>-values are too close to 0 or 1 to be printed explicitly.
    *    If <TT>EPSILONP</TT> 
    * <SPAN CLASS="MATH">= <I>&#949;</I></SPAN>, then any <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    *    (or significance level) less than <SPAN CLASS="MATH"><I>&#949;</I></SPAN> or larger than
    *    
    * <SPAN CLASS="MATH">1 - <I>&#949;</I></SPAN> is <EM>not</EM> written explicitly;
    *    the program simply writes ``<TT>eps</TT>'' or ``<TT>1-eps</TT>''.
    *    The default value is <SPAN CLASS="MATH">10<SUP>-15</SUP></SPAN>.
    * 
    */
   public static double EPSILONP = 1.0E-15;


   /**
    * Environment variable used in {@link #formatp1 formatp1} to determine
    *    which <SPAN CLASS="MATH"><I>p</I></SPAN>-values should be marked as suspect when printing test results.
    *    If <TT>SUSPECTP</TT> <SPAN CLASS="MATH">= <I>&#945;</I></SPAN>, then any <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    *    (or significance level) less than <SPAN CLASS="MATH"><I>&#945;</I></SPAN> or larger than
    *    <SPAN CLASS="MATH">1 - <I>&#945;</I></SPAN> is considered suspect and is
    *    ``singled out'' by <TT>formatp1</TT>.
    *    The default value is 0.01.
    * 
    */
   public static double SUSPECTP = 0.01;


   /**
    * Returns the significance level (or <SPAN CLASS="MATH"><I>p</I></SPAN>-value) <SPAN CLASS="MATH"><I>p</I></SPAN> of a test,
    *    in the format ``<SPAN CLASS="MATH">1 - <I>p</I></SPAN>'' if <SPAN CLASS="MATH"><I>p</I></SPAN> is close to 1, and <SPAN CLASS="MATH"><I>p</I></SPAN> otherwise.
    *    Uses the environment variable  {@link #EPSILONP EPSILONP} and replaces <SPAN CLASS="MATH"><I>p</I></SPAN>
    *    by <SPAN CLASS="MATH"><I>&#949;</I></SPAN> when it is too small.
    * 
    * @param p the <SPAN CLASS="MATH"><I>p</I></SPAN>-value or significance level to be formated
    * 
    *    @return the string representation of the <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    * 
    */
   public static String formatp0 (double p) {
      // Formats the significance level of a test, without a descriptor
      if ((p >= 0.01) && (p <= 0.99))
         return PrintfFormat.format (8, 2, 1, p);
      else if (p < EPSILONP)
         return "   eps  ";
      else if (p < 0.01)
         return PrintfFormat.format (8, 2, 2, p);
      else if (p >= 1.0 - EPSILONP)
         return " 1 - eps ";
      else
         return " 1 - " + PrintfFormat.g (8, 2, 1.0 - p);
   }


   /**
    * Returns the string ``<TT>Significance level of test : </TT>'',
    *   then calls {@link #formatp0 formatp0} to print <SPAN CLASS="MATH"><I>p</I></SPAN>, and adds
    *   the marker ``<TT>****</TT>'' if <SPAN CLASS="MATH"><I>p</I></SPAN> is considered suspect
    *   (uses the environment variable <TT>RSUSPECTP</TT> for this).
    * 
    * @param p the <SPAN CLASS="MATH"><I>p</I></SPAN>-value or significance level to be formated
    * 
    *    @return the string representation of the significance level of test
    * 
    */
   public static String formatp1 (double p) {
      // Prints the significance level of a test, with a descriptor.
      StringBuffer sb = new StringBuffer();
      sb.append ("Significance level of test            :" + formatp0 (p));
      if (p < SUSPECTP || p > 1.0 - SUSPECTP)
         sb.append ("    *****");

      sb.append (PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
      return sb.toString();
   }


   /**
    * Returns <TT>x</TT> on a single line, then go to the next line
    *    and calls {@link #formatp1 formatp1}.
    * 
    * @param x value of the statistic for which the significance level is formated
    * 
    *    @param p the <SPAN CLASS="MATH"><I>p</I></SPAN>-value or significance level to be formated
    * 
    *    @return the string representation of the significance level of test
    * 
    */
   public static String formatp2 (double x, double p) {
      // Prints the statistic x and its significance level p.
      return PrintfFormat.format (8, 2, 1, x) + PrintfFormat.NEWLINE +
             formatp1 (p);
   }


   /**
    * Formats the test statistic <TT>x</TT> for a test named <TT>testName</TT>
    *    with <SPAN CLASS="MATH"><I>p</I></SPAN>-value <TT>p</TT>.  The first line of the returned string contains
    *    the name of the test and the statistic whereas the second line contains
    *    its significance level.  The formated values of <TT>x</TT> and <TT>p</TT> are
    *    aligned.
    * 
    * @param testName name of the test that was performed
    * 
    *    @param x value of the test statistic
    * 
    *    @param p significance level (or <SPAN CLASS="MATH"><I>p</I></SPAN>-value) of the test
    * 
    *    @return the string representation of the test result
    * 
    */
   public static String formatp3 (String testName, double x, double p) {
      final String SLT = "Significance level of test";
      int l = Math.max (SLT.length(), testName.length());
      PrintfFormat pf = new PrintfFormat();
      pf.append (-l, testName).append (" : ").append (8, 2, 1, x).append
               (PrintfFormat.NEWLINE);
      pf.append (-l, SLT).append (" : ").append (formatp0 (p));
      if (p < SUSPECTP || p > 1.0 - SUSPECTP)
         pf.append ("    *****");
      pf.append (PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
      return pf.toString();
   }


   /**
    * Computes the <SPAN CLASS="MATH"><I>p</I></SPAN>-value of the chi-square statistic
    *   <TT>chi2</TT> for a test with <TT>k</TT> intervals.  Uses <SPAN CLASS="MATH"><I>d</I></SPAN> decimal digits
    *   of precision in the calculations. The result of the
    *   test is returned as a string.  The <SPAN CLASS="MATH"><I>p</I></SPAN>-value is computed using
    *   {@link GofStat#pDisc pDisc}.
    * 
    * @param k number of subintervals for the chi-square test
    * 
    *    @param chi2 chi-square statistic
    * 
    *    @return the string representation of the test result and <SPAN CLASS="MATH"><I>p</I></SPAN>-value
    * 
    */
   public static String formatChi2 (int k, int d, double chi2) {
      StringBuffer sb = new StringBuffer();
      sb.append ("Chi2 statistic                        : " +
                  PrintfFormat.format (8, 2, 1, chi2));
      sb.append (PrintfFormat.NEWLINE +
                 "p-value                               : " +
                 formatp0 (GofStat.pDisc
                          (ChiSquareDist.cdf (k - 1, d, chi2),
                           ChiSquareDist.barF (k - 1, d, chi2))));
      sb.append (PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
      return sb.toString();
   }


   /**
    * Computes the <SPAN CLASS="MATH"><I>p</I></SPAN>-values of the three Kolmogorov-Smirnov statistics
    *   <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN>, and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB></SPAN>, whose values are in <TT>dp, dm, d</TT>,
    *   respectively, assuming a sample of size <TT>n</TT>.
    *   Then formats these statistics and their <SPAN CLASS="MATH"><I>p</I></SPAN>-values
    *   using {@link #formatp2 formatp2} for each one.
    * 
    * @param n sample size
    * 
    *    @param dp value of the <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN> statistic
    * 
    *    @param dm value of the <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN> statistic
    * 
    *    @param d value of the <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB></SPAN> statistic
    * 
    *    @return the string representation of the Kolmogorov-Smirnov statistics and their
    *      significance levels
    * 
    */
   public static String formatKS (int n, double dp,
                                  double dm, double d) {
      // Prints the results of a Kolmogorov-Smirnov test
      return "Kolmogorov-Smirnov+ statistic = D+    :" +
             formatp2 (dp, KolmogorovSmirnovPlusDist.barF (n, dp)) +
             "Kolmogorov-Smirnov- statistic = D-    :" +
             formatp2 (dm, KolmogorovSmirnovPlusDist.barF (n, dm)) +
             "Kolmogorov-Smirnov statistic = D      :" +
             formatp2 (d, KolmogorovSmirnovDistQuick.barF (n, d)) +
                       PrintfFormat.NEWLINE + PrintfFormat.NEWLINE;
   }


   /**
    * Computes the KS test statistics to compare the
    *  empirical distribution of the observations in <TT>data</TT>
    *  with the theoretical distribution <TT>dist</TT> and
    *  formats the results.
    * 
    * @param data array of observations to be tested
    * 
    *    @param dist assumed distribution of the observations
    * 
    *    @return the string representation of the Kolmogorov-Smirnov statistics and their
    *      significance levels
    * 
    */
   public static String formatKS (DoubleArrayList data,
                                  ContinuousDistribution dist) {

      double[] v = data.elements();
      int n = data.size();

      DoubleArrayList dataUnif = GofStat.unifTransform (data, dist);
      dataUnif.quickSortFromTo (0, dataUnif.size() - 1);
      double[] ret = GofStat.kolmogorovSmirnov (dataUnif);
      return formatKS (n, ret[0], ret[1], ret[2]);
   }


   /**
    * Similar to {@link #formatKS(int,double,double,double) formatKS},
    *    but for the KS statistic <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP>(<I>a</I>)</SPAN>.
    *    Writes a header,
    *   computes the <SPAN CLASS="MATH"><I>p</I></SPAN>-value and calls {@link #formatp2 formatp2}.
    * 
    * @param n sample size
    * 
    *    @param a size of the jump
    * 
    *    @param dp value of <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP>(<I>a</I>)</SPAN>
    * 
    *    @return the string representation of the Kolmogorov-Smirnov statistic and its significance level
    * 
    */
   public static String formatKSJumpOne (int n, double a, double dp) {
      double d = 1.0 - FDist.kolmogorovSmirnovPlusJumpOne (n, a, dp);

      return PrintfFormat.NEWLINE +
             "Kolmogorov-Smirnov+ statistic = D+    : " +
             PrintfFormat.g (8, 2, dp) + PrintfFormat.NEWLINE +
             formatp1 (d) + PrintfFormat.NEWLINE;
   }


   /**
    * Similar to {@link #formatKS(DoubleArrayList,ContinuousDistribution) formatKS},
    *   but for <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP>(<I>a</I>)</SPAN>.
    * 
    * @param data array of observations to be tested
    * 
    *    @param dist assumed distribution of the data
    * 
    *    @param a size of the jump
    * 
    *    @return string representation of the Kolmogorov-Smirnov statistic and its significance level
    * 
    */
   public static String formatKSJumpOne (DoubleArrayList data,
                                         ContinuousDistribution dist,
                                         double a) {

      double[] v = data.elements();
      int n = data.size();
      DoubleArrayList dataUnif = GofStat.unifTransform (data, dist);
      dataUnif.quickSortFromTo (0, dataUnif.size() - 1);
      double[] ret =  GofStat.kolmogorovSmirnovJumpOne (dataUnif, a);
      return formatKSJumpOne (n, a, ret[0]);
   }


   /**
    * Kolmogorov-Smirnov+ test 
    * 
    */
   public  static final int KSP = 0;


   /**
    * Kolmogorov-Smirnov<SPAN CLASS="MATH">-</SPAN> test 
    * 
    */
   public static final int KSM = 1;


   /**
    * Kolmogorov-Smirnov test  
    * 
    */
   public static final int KS = 2;


   /**
    * Anderson-Darling test  
    * 
    */
   public static final int AD = 3;


   /**
    * Cram&#233;r-von Mises test  
    * 
    */
   public static final int CM = 4;


   /**
    * Watson G test  
    * 
    */
   public static final int WG = 5;


   /**
    * Watson U test   
    * 
    */
   public static final int WU = 6;


   /**
    * Mean  
    * 
    */
   public static final int MEAN = 7;


   /**
    * Correlation  
    * 
    */
   public static final int COR = 8;


   /**
    * Total number of test types  
    * 
    */
   public static final int NTESTTYPES = 9;


   /**
    * Name of each <TT>testType</TT> test.
    *   Could be used for printing the test results, for example.
    * 
    */
   public static final String[] TESTNAMES = {
    "KolmogorovSmirnovPlus", "KolmogorovSmirnovMinus",
    "KolmogorovSmirnov", "Anderson-Darling",
    "CramerVon-Mises", "Watson G", "Watson U",
    "Mean", "Correlation"
   };


   /**
    * The set of EDF tests that are to be performed when calling
    *   the methods {@link #activeTests activeTests}, {@link #formatActiveTests formatActiveTests}, etc.
    *   By default, this set contains <TT>KSP</TT>, <TT>KSM</TT>,
    *   and <TT>AD</TT>.  Note: <TT>MEAN</TT> and <TT>COR</TT> are <EM>always excluded</EM>
    *   from this set of active tests.
    * The valid indices for this array are {@link #KSP KSP}, {@link #KSM KSM},
    *       {@link #KS KS}, {@link #AD AD}, {@link #CM CM}, {@link #WG WG},
    *       {@link #WU WU}, {@link #MEAN MEAN}, and {@link #COR COR}.
    * 
    */
   public static boolean[] activeTests = null;
   private static void initActiveTests() {
      activeTests = new boolean[NTESTTYPES];
      for (int i = 0; i < activeTests.length; i++)
        activeTests[i] = false;
      activeTests[KSP] = activeTests[KSM] = true;
      activeTests[AD] = activeTests[MEAN] = activeTests[COR] = true;
   }
   static {
      initActiveTests();
   }


   /**
    * Computes all EDF test statistics
    *   to compare the empirical
    *   distribution of  
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN> with the uniform distribution,
    *   assuming that these sorted observations are in <TT>sortedData</TT>.
    *   If <SPAN CLASS="MATH"><I>N</I> &gt; 1</SPAN>, returns <TT>sVal</TT> with the values of the KS
    *   statistics <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>+</SUP></SPAN>, <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB><SUP>-</SUP></SPAN> and <SPAN CLASS="MATH"><I>D</I><SUB>N</SUB></SPAN>, of the Cram&#233;r-von Mises
    *   statistic <SPAN CLASS="MATH"><I>W</I><SUB>N</SUB><SUP>2</SUP></SPAN>, Watson's <SPAN CLASS="MATH"><I>G</I><SUB>N</SUB></SPAN> and <SPAN CLASS="MATH"><I>U</I><SUB>N</SUB><SUP>2</SUP></SPAN>, Anderson-Darling's
    *   <SPAN CLASS="MATH"><I>A</I><SUB>N</SUB><SUP>2</SUP></SPAN>, and the average of the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN>'s, respectively.
    *   If <SPAN CLASS="MATH"><I>N</I> = 1</SPAN>, only puts <SPAN CLASS="MATH">1 -</SPAN><TT>sortedData.get (0)</TT> in <TT>sVal[KSP]</TT>.
    *   Calling this method is more efficient than computing these statistics
    *   separately by calling the corresponding methods in {@link GofStat}.
    * 
    * @param sortedData array of sorted observations
    * 
    *    @param sVal array that will be filled with the results of the tests
    * 
    * 
    */
   public static void tests (DoubleArrayList sortedData, double[] sVal) {
      double[] u = sortedData.elements();
      int n = sortedData.size();
      int i;
      double a2 = 0.0, w2, dm = 0.0, dp = 0.0, w;
      double u1, ui, d2, d1;
      double sumZ;
      double unSurN;

      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");
      if (sVal.length != NTESTTYPES)
        throw new IllegalArgumentException ("sVal must " +
                              "be of size NTESTTYPES.");

      // We assume that u is already sorted.
      if (n == 1) {
         sVal[KSP] = 1.0 - u[0];
         sVal[MEAN] = u[0];
         return;
      }
      unSurN = 1.0 / n;
      w2 = unSurN / 12.0;
      sumZ = 0.0;
      for (i = 0; i < n; i++) {
         // Statistics KS
         d1 = u[i] - i*unSurN;
         d2 = (i + 1)*unSurN - u[i];
         if (d1 > dm)
            dm = d1;
         if (d2 > dp)
            dp = d2;
         // Watson U and G
         sumZ += u[i];
         w = u[i] - (i + 0.5)*unSurN;
         w2 += w*w;
         // Anderson-Darling
         ui = u[i];
         u1 = 1.0 - ui;
         if (ui < GofStat.EPSILONAD)
            ui = GofStat.EPSILONAD;
         else if (u1 < GofStat.EPSILONAD)
            u1 = GofStat.EPSILONAD;
         a2 += (2*i + 1) * Math.log (ui) + (1 + 2*(n - i - 1))*Math.log (u1);
      }
      if (dm > dp)
         sVal[KS] = dm;
      else
         sVal[KS] = dp;
      sVal[KSM] = dm;
      sVal[KSP] = dp;
      sumZ = sumZ * unSurN - 0.5;
      sVal[CM] = w2;
      sVal[WG] = Math.sqrt ((double) n) * (dp + sumZ);
      sVal[WU] = w2 - sumZ * sumZ * n;
      sVal[AD] = -n - a2 * unSurN;
      sVal[MEAN] = sumZ + 0.5;  // Nouveau ...
   }


   /**
    * The observations <SPAN CLASS="MATH"><I>V</I></SPAN> are in <TT>data</TT>,
    *   not necessarily sorted, and their empirical
    *   distribution is compared with the continuous distribution <TT>dist</TT>.
    *  
    * If <SPAN CLASS="MATH"><I>N</I> = 1</SPAN>, only puts <TT>data.get (0)</TT> in <TT>sVal[MEAN]</TT>,
    *  and <SPAN CLASS="MATH">1 -</SPAN><TT>dist.cdf (data.get (0))</TT> in <TT>sVal[KSP]</TT>.
    * 
    * @param data array of observations to test
    * 
    *    @param dist assumed distribution of the observations
    * 
    *    @param sVal array that will be filled with the results of the tests
    * 
    * 
    */
   public static void tests (DoubleArrayList data,
                             ContinuousDistribution dist, double[] sVal) {

      double[] v = data.elements();
      int n = data.size();

      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");

      DoubleArrayList sortedData = GofStat.unifTransform (data, dist);
      sortedData.quickSortFromTo (0, sortedData.size()-1);
      tests (sortedData, sVal);
      if (n == 1)
         sVal[MEAN] = v[0];     // On veut v[0], pas u[0].
   }


   /**
    * Computes the EDF test statistics by calling
    *   {@link #tests(DoubleArrayList,double[]) tests}, then computes the <SPAN CLASS="MATH"><I>p</I></SPAN>-values of those
    *   that currently belong to <TT>activeTests</TT>,
    * and return these quantities in <TT>sVal</TT> and <TT>pVal</TT>, respectively.
    *   Assumes that 
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN> are in <TT>sortedData</TT>
    *   and that we want to compare their empirical distribution
    *   with the uniform distribution.
    *   If <SPAN CLASS="MATH"><I>N</I> = 1</SPAN>, only puts <SPAN CLASS="MATH">1 -</SPAN><TT>sortedData.get (0)</TT> in
    *   <TT>sVal[KSP], pVal[KSP]</TT>, and <TT>pVal[MEAN]</TT>.
    * 
    * @param sortedData array of sorted observations
    * 
    *    @param sVal array that will be filled with the results of the tests
    * 
    *    @param pVal array that will be filled with the <SPAN CLASS="MATH"><I>p</I></SPAN>-values
    * 
    * 
    */
   public static void activeTests (DoubleArrayList sortedData,
                                   double[] sVal, double[] pVal) {

      double[] u = sortedData.elements();
      int n = sortedData.size();

      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");

      if (sVal.length != NTESTTYPES || pVal.length != NTESTTYPES)
        throw new IllegalArgumentException ("sVal and pVal must " +
              "be of length NTESTTYPES.");

      if (n == 1) {
         sVal[KSP] = 1.0 - u[0];
         pVal[KSP] = 1.0 - u[0];
         pVal[MEAN] = pVal[KSP];
         return;
      }
      // We assume that u is already sorted.
      tests (sortedData, sVal);

      if (activeTests.length != NTESTTYPES) {
        initActiveTests();
        System.err.println ("activeTests was invalid, it was reinitialized.");
      }

      if (activeTests[KSP])
         pVal[KSP] = KolmogorovSmirnovPlusDist.barF (n, sVal[KSP]);

      if (activeTests[KSM])
         pVal[KSM] = KolmogorovSmirnovPlusDist.barF (n, sVal[KSM]);

      if (activeTests[KS])
         pVal[KS] = KolmogorovSmirnovDistQuick.barF (n, sVal[KS]);

      if (activeTests[AD])
         pVal[AD] = AndersonDarlingDistQuick.barF (n, sVal[AD]);

      if (activeTests[CM])
         pVal[CM] = CramerVonMisesDist.barF (n, sVal[CM]);

      if (activeTests[WG])
         pVal[WG] = WatsonGDist.barF (n, sVal[WG]);

      if (activeTests[WU])
         pVal[WU] = WatsonUDist.barF (n, sVal[WU]);
   }


   /**
    * The observations are in <TT>data</TT>,
    *  not necessarily sorted, and we want to compare their empirical
    *  distribution with the distribution <TT>dist</TT>.
    *  If <SPAN CLASS="MATH"><I>N</I> = 1</SPAN>, only puts <TT>data.get(0)</TT> in <TT>sVal[MEAN]</TT>,
    *  and <SPAN CLASS="MATH">1 -</SPAN><TT>dist.cdf (data.get (0))</TT> in <TT>sVal[KSP], pVal[KSP]</TT>,
    *  and <TT>pVal[MEAN]</TT>.
    * 
    * @param data array of observations to test
    * 
    *    @param dist assumed distribution of the observations
    * 
    *    @param sVal array that will be filled with the results of the tests
    * 
    *    @param pVal array that will be filled with the <SPAN CLASS="MATH"><I>p</I></SPAN>-values
    * 
    * 
    */
   public static void activeTests (DoubleArrayList data,
                                   ContinuousDistribution dist,
                                   double[] sVal, double[] pVal) {
      double[] v = data.elements();
      int n = data.size();

      if (n <= 0)
        throw new IllegalArgumentException ("n <= 0");

      DoubleArrayList sortedData = GofStat.unifTransform (data, dist);
      sortedData.quickSortFromTo (0, sortedData.size() - 1);

      activeTests (sortedData, sVal, pVal);
      if (n == 1)
         sVal[MEAN] = v[0];
   }


   /**
    * Gets the <SPAN CLASS="MATH"><I>p</I></SPAN>-values of the <EM>active</EM> EDF test statistics,
    *   which are in <TT>activeTests</TT>.  It is assumed that the values
    *   of these statistics and their <SPAN CLASS="MATH"><I>p</I></SPAN>-values are <EM>already computed</EM>,
    *   in <TT>sVal</TT> and <TT>pVal</TT>, and that the sample size is <TT>n</TT>.
    *   These statistics and <SPAN CLASS="MATH"><I>p</I></SPAN>-values are formated
    *   using {@link #formatp2 formatp2} for each one.
    *   If <TT>n=1</TT>, prints only <TT>pVal[KSP]</TT> using {@link #formatp1 formatp1}.
    * 
    * @param n sample size
    * 
    *    @param sVal array containing the results of the tests
    * 
    *    @param pVal array containing the <SPAN CLASS="MATH"><I>p</I></SPAN>-values
    * 
    *    @return the results formated as a string
    * 
    */
   public static String formatActiveTests (int n, double[] sVal,
                                           double[] pVal) {

      if (activeTests.length != NTESTTYPES) {
        initActiveTests();
        System.err.println ("activeTests was invalid, it was reinitialized.");
      }
      if (sVal.length != NTESTTYPES || pVal.length != NTESTTYPES)
        throw new IllegalArgumentException ("The length of " +
           "sVal and pVal must be NTESTTYPES.");
      if (n == 1)
         return formatp1 (pVal[KSP]);;

      StringBuffer sb = new StringBuffer (PrintfFormat.NEWLINE);
      if (activeTests[KSP])
         sb.append ("Kolmogorov-Smirnov+ statistic = D+    :" +
           formatp2 (sVal[KSP], pVal[KSP]));
      if (activeTests[KSM])
         sb.append ("Kolmogorov-Smirnov- statistic = D-    :" +
           formatp2 (sVal[KSM], pVal[KSM]));
      if (activeTests[KS])
         sb.append ("Kolmogorov-Smirnov statistic  = D     :" +
           formatp2 (sVal[KS], pVal[KS]));
      if (activeTests[AD])
         sb.append ("Anderson-Darling statistic = A2       :" +
           formatp2 (sVal[AD], pVal[AD]));
      if (activeTests[CM])
         sb.append ("Cramer-von Mises statistic = W2       :" +
           formatp2 (sVal[CM], pVal[CM]));
      if (activeTests[WG])
         sb.append ("Watson statistic = G                  :" +
           formatp2 (sVal[WG], pVal[WG]));
      if (activeTests[WU])
         sb.append ("Watson statistic = U2                 :" +
           formatp2 (sVal[WU], pVal[WU]));
      sb.append (PrintfFormat.NEWLINE);
      return sb.toString();
   }


   /**
    * Repeats the following <TT>k</TT> times:
    *   Applies the {@link GofStat#iterateSpacings GofStat.iterateSpacings}
    *   transformation to the
    *   
    * <SPAN CLASS="MATH"><I>U</I><SUB>(0)</SUB>,..., <I>U</I><SUB>(N-1)</SUB></SPAN>, assuming that these observations are in
    *   <TT>sortedData</TT>, then computes the EDF test statistics and calls
    *   {@link #activeTests(DoubleArrayList,double[],double[]) activeTests} after each transformation.
    *   The function returns the <EM>original</EM> array <TT>sortedData</TT> (the
    *   transformations are applied on a copy of <TT>sortedData</TT>).
    * If <TT>printval = true</TT>, stores all the values into the returned
    *   {@link String} after each iteration.
    *   If <TT>graph = true</TT>, calls {@link #graphDistUnif graphDistUnif} after each iteration
    *   to print to stream <TT>f</TT> the data for plotting the distribution
    *   function of the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN>.
    * 
    * @param sortedData array containing the sorted observations
    * 
    *    @param k number of times the tests are applied
    * 
    *    @param printval if <TT>true</TT>, stores all the values of the observations at each iteration
    * 
    *    @param graph if <TT>true</TT>, the distribution of the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> will be plotted after each
    *      iteration
    * 
    *    @param f stream where the plots are written to
    * 
    *    @return a string representation of the test results
    * 
    */
   public static String iterSpacingsTests (DoubleArrayList sortedData, int k,
                                           boolean printval, boolean graph,
                                           PrintWriter f) {

      int n = sortedData.size();

      DoubleArrayList sortedDataCopy = (DoubleArrayList)sortedData.clone();
      DoubleArrayList diffArrayList = new DoubleArrayList(sortedData.size()+2);

      int j;
      int i;
      double[] sVal = new double[NTESTTYPES], pVal = new double[NTESTTYPES];

      StringBuffer sb = new StringBuffer (PrintfFormat.NEWLINE);

      for (j = 1; j <= k; j++) {
         sb.append ("-----------------------------------" +
                     PrintfFormat.NEWLINE +
                     "EDF Tests after \"iterateSpacings\", level : " +
                     PrintfFormat.d (2, j) + PrintfFormat.NEWLINE);

         GofStat.diff (sortedDataCopy, diffArrayList, 0, n - 1, 0.0, 1.0);
         GofStat.iterateSpacings (sortedDataCopy, diffArrayList);
         sortedDataCopy.quickSortFromTo (0, sortedDataCopy.size() - 1);
         activeTests (sortedDataCopy, sVal, pVal);

         sb.append (formatActiveTests (n, sVal, pVal));
         String desc = "Values of Uniforms after iterateSpacings, level " +
             PrintfFormat.d (2, j);
         if (printval) {
          sb.append (desc + PrintfFormat.NEWLINE +
                     "------------------------" + PrintfFormat.NEWLINE);
          sb.append (sortedDataCopy + PrintfFormat.NEWLINE);
         }
         if (graph && f != null)
          f.print (graphDistUnif (sortedDataCopy, desc));
         else if (graph && f == null)
          sb.append (graphDistUnif (sortedDataCopy, desc));
       }
       return sb.toString();
   }


   /**
    * Similar to {@link #iterSpacingsTests iterSpacingsTests}, but with the
    *   {@link GofStat#powerRatios GofStat.powerRatios} transformation.
    * 
    * @param sortedData array containing the sorted observations
    * 
    *    @param k number of times the tests are applied
    * 
    *    @param printval if <TT>true</TT>, stores all the values of the observations at each iteration
    * 
    *    @param graph if <TT>true</TT>, the distribution of the <SPAN CLASS="MATH"><I>U</I><SUB>i</SUB></SPAN> will be plotted after each
    *      iteration
    * 
    *    @param f stream where the plots are written to
    * 
    *    @return a string representation of the test results
    */
   public static String iterPowRatioTests (DoubleArrayList sortedData, int k,
                                           boolean printval, boolean graph,
                                           PrintWriter f) {

      int n = sortedData.size();
      DoubleArrayList sortedDataCopy = (DoubleArrayList)sortedData.clone();

      int i;
      int j;
      double[] sVal = new double[NTESTTYPES], pVal = new double[NTESTTYPES];

      StringBuffer sb = new StringBuffer (PrintfFormat.NEWLINE);

      for (i = 1; i <= k; i++) {
         GofStat.powerRatios (sortedDataCopy);
         sb.append ("-----------------------------------" +
                    PrintfFormat.NEWLINE +
                    "EDF Tests after \"powerRatios\", level : " +
                    PrintfFormat.d (2, i) + PrintfFormat.NEWLINE);

         sortedDataCopy.quickSortFromTo (0, sortedDataCopy.size() - 1);

         activeTests (sortedDataCopy, sVal, pVal);
         sb.append (formatActiveTests (n, sVal, pVal));
         String desc = "Values of Uniforms after PowerRatios, level " +
             PrintfFormat.d (2, i);
         if (printval) {
           sb.append (desc + PrintfFormat.NEWLINE +
                      "--------------------------" + PrintfFormat.NEWLINE);
           sb.append (sortedDataCopy + PrintfFormat.NEWLINE);
         }
         if (graph && f != null)
            f.print (graphDistUnif (sortedDataCopy, desc));
         else if (graph && f == null)
            sb.append (graphDistUnif (sortedDataCopy, desc));
      }
      return sb.toString();
   }
}
