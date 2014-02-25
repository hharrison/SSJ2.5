

/*
 * Class:        InverseFromDensityGen
 * Description:  generator of random variates by numerical inversion of
                 an arbitrary continuous distribution when only the 
                 probability density is known
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        June 2008

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

package umontreal.iro.lecuyer.randvar;
   import umontreal.iro.lecuyer.functions.MathFunction;
   import umontreal.iro.lecuyer.rng.RandomStream;
   import umontreal.iro.lecuyer.probdist.ContinuousDistribution;
   import umontreal.iro.lecuyer.probdist.InverseDistFromDensity;



/**
 * Implements a method for generating random variates by numerical inversion of
 * an <SPAN  CLASS="textit">arbitrary continuous</SPAN> distribution when only the probability density
 * is known.  The cumulative probabilities (cdf) are pre-computed by
 *  numerical quadrature  of the
 * density using Gauss-Lobatto integration over suitably small intervals to
 * satisfy the required precision, and these values are kept in tables. Then the
 *  algorithm uses polynomial interpolation  over the tabulated values to get
 *   the inverse cdf. The user can select the
 *   desired precision and the degree of the interpolating polynomials.
 * 
 * <P>
 * The algorithm may fail for some distributions for which the density
 *  becomes infinite at a point (for ex. the Gamma and the Beta distributions
 *  with 
 * <SPAN CLASS="MATH"><I>&#945;</I> &lt; 1</SPAN>)  if one requires too high a precision
 * (a  too small <TT>eps</TT>, for ex. 
 * <SPAN CLASS="MATH"><I>&#949;</I>&#8764;10<SUP>-15</SUP></SPAN>).
 * However, it should work also for continuous densities with finite discontinuities.
 * 
 * <P>
 * While the setup time is relatively slow, the generation of random variables
 * is extremely fast and practically independent of the required precision
 * and of the specific distribution. The following table shows the time needed
 * (in seconds) to generate <SPAN CLASS="MATH">10<SUP>8</SUP></SPAN> random numbers using inversion
 * from a given class, then the numerical inversion with Gauss-Lobatto integration
 * implemented here, and finally the speed ratios between the two methods.
 * The speed ratio is  the speed of the latter over the former.
 * Thus for the beta distribution with parameters (5, 500), generating random
 * variables with the Gauss-Lobatto integration implemented in this class is
 * more than 1700
 * times faster than using inversion from the <TT>BetaDist</TT> class.
 * These tests were made on a machine with processor AMD Athlon 4000, running
 * Red Hat Linux, with clock speed at 2403 MHz.
 * 
 * <P>
 * <DIV ALIGN="CENTER">
 * <TABLE CELLPADDING=3 BORDER="1">
 * <TR><TD ALIGN="LEFT">Distribution</TD>
 * <TD ALIGN="CENTER">Inversion</TD>
 * <TD ALIGN="CENTER">Gauss-Lobatto</TD>
 * <TD ALIGN="CENTER">speed ratio</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">9.19</TD>
 * <TD ALIGN="CENTER">8.89</TD>
 * <TD ALIGN="CENTER">1.03</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ExponentialDist(5)</TT></TD>
 * <TD ALIGN="CENTER">17.72</TD>
 * <TD ALIGN="CENTER">8.82</TD>
 * <TD ALIGN="CENTER">2.0</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>CauchyDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">18.30</TD>
 * <TD ALIGN="CENTER">8.81</TD>
 * <TD ALIGN="CENTER">2.1</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaSymmetricalDist(10.5)</TT></TD>
 * <TD ALIGN="CENTER">242.80</TD>
 * <TD ALIGN="CENTER">8.85</TD>
 * <TD ALIGN="CENTER">27.4</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>GammaDist(55)</TT></TD>
 * <TD ALIGN="CENTER">899.50</TD>
 * <TD ALIGN="CENTER">8.89</TD>
 * <TD ALIGN="CENTER">101</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ChiSquareNoncentralDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">5326.90</TD>
 * <TD ALIGN="CENTER">8.85</TD>
 * <TD ALIGN="CENTER">602</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaDist(5, 500)</TT></TD>
 * <TD ALIGN="CENTER">15469.10</TD>
 * <TD ALIGN="CENTER">8.86</TD>
 * <TD ALIGN="CENTER">1746</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * 
 * <P>
 * The following table gives the time (in sec.) needed to
 * create an object (setup time) and to generate one random variable for
 * this class compared to the same for the inversion method specific to each class,
 * and the ratios of the times (init + one random variable) of the two methods.
 * For inversion, we initialized <SPAN CLASS="MATH">10<SUP>8</SUP></SPAN> times; for this class, we
 * initialized <SPAN CLASS="MATH">10<SUP>4</SUP></SPAN> times.
 * 
 * <P>
 * <DIV ALIGN="CENTER">
 * <TABLE CELLPADDING=3 BORDER="1">
 * <TR><TD ALIGN="LEFT">Distribution</TD>
 * <TD ALIGN="CENTER">Inversion</TD>
 * <TD ALIGN="CENTER">Gauss-Lobatto</TD>
 * <TD ALIGN="CENTER">time ratio</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">&nbsp;</TD>
 * <TD ALIGN="CENTER"><SPAN CLASS="MATH">10<SUP>8</SUP></SPAN> init</TD>
 * <TD ALIGN="CENTER"><SPAN CLASS="MATH">10<SUP>4</SUP></SPAN> init</TD>
 * <TD ALIGN="CENTER">for 1 init</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">5.30</TD>
 * <TD ALIGN="CENTER">38.29</TD>
 * <TD ALIGN="CENTER">26426</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ExponentialDist(5)</TT></TD>
 * <TD ALIGN="CENTER">3.98</TD>
 * <TD ALIGN="CENTER">27.05</TD>
 * <TD ALIGN="CENTER">12466</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>CauchyDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">5.05</TD>
 * <TD ALIGN="CENTER">58.39</TD>
 * <TD ALIGN="CENTER">25007</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaSymmetricalDist(10.5)</TT></TD>
 * <TD ALIGN="CENTER">90.66</TD>
 * <TD ALIGN="CENTER">68.33</TD>
 * <TD ALIGN="CENTER">2049</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>GammaDist(55)</TT></TD>
 * <TD ALIGN="CENTER">13.15</TD>
 * <TD ALIGN="CENTER">58.34</TD>
 * <TD ALIGN="CENTER">639</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ChiSquareNoncentralDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">190.48</TD>
 * <TD ALIGN="CENTER">248.98</TD>
 * <TD ALIGN="CENTER">451</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaDist(5, 500)</TT></TD>
 * <TD ALIGN="CENTER">63.60</TD>
 * <TD ALIGN="CENTER">116.57</TD>
 * <TD ALIGN="CENTER">75</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * 
 * <P>
 * If only a few random variables are needed, then using this class
 * is not efficient because of the slow set-up. But if one wants to generate
 * large samples from the same distribution with fixed parameters, then
 * this class will be very efficient. The following table gives the number of
 * random variables generated beyond which, using this class will be
 * worthwhile.
 * 
 * <P>
 * <DIV ALIGN="CENTER">
 * <TABLE CELLPADDING=3 BORDER="1">
 * <TR><TD ALIGN="LEFT">Distribution</TD>
 * <TD ALIGN="CENTER">number of generated variables</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">41665</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ExponentialDist(5)</TT></TD>
 * <TD ALIGN="CENTER">15266</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>CauchyDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">31907</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaSymmetricalDist(10.5)</TT></TD>
 * <TD ALIGN="CENTER">2814</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>GammaDist(55)</TT></TD>
 * <TD ALIGN="CENTER">649</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>ChiSquareNoncentralDist(10.5, 5)</TT></TD>
 * <TD ALIGN="CENTER">467</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>BetaDist(5, 500)</TT></TD>
 * <TD ALIGN="CENTER">75</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * 
 * <P>
 * Thus, for example, if one needs to generate less than 15266 exponential
 * random variables, then using the <TT>InverseFromDensityGen</TT> class
 * is not wortwhile: it will be faster to use inversion from the
 * <TT>ExponentialGen</TT> class.
 * 
 */
public class InverseFromDensityGen extends RandomVariateGen  {


   /**
    * Creates a new generator for the <SPAN  CLASS="textit">continuous</SPAN> distribution
    * <TT>dis</TT>, using stream <TT>stream</TT>. <TT>dis</TT> must have a well-defined
    * density method; its other methods are unused. For a non-standard distribution
    * <TT>dis</TT>, the user may wish to set the left and the right boundaries
    * between which the density is non-zero by calling methods
    * {@link umontreal.iro.lecuyer.probdist.ContinuousDistribution#setXinf setXinf}
    * and
    * {@link umontreal.iro.lecuyer.probdist.ContinuousDistribution#setXsup setXsup}
    * of <TT>dis</TT>, for better efficiency.
    * Argument <TT>xc</TT> can be the mean,
    * the mode or any other <SPAN CLASS="MATH"><I>x</I></SPAN> for which the density is relatively large.
    * The <SPAN CLASS="MATH"><I>u</I></SPAN>-resolution <TT>eps</TT> is the desired absolute error in the CDF,
    * and <TT>order</TT> is the degree of the
    * Newton interpolating polynomial over each interval.
    * An <TT>order</TT> of 3 or 5, and an <TT>eps</TT> of <SPAN CLASS="MATH">10<SUP>-6</SUP></SPAN> to <SPAN CLASS="MATH">10<SUP>-12</SUP></SPAN>
    * are usually good choices.
    *  Restrictions: 
    * <SPAN CLASS="MATH">3&nbsp;&lt;=&nbsp;<texttt>order</texttt>&nbsp;&lt;=&nbsp;12</SPAN>.
    * 
    */
   public InverseFromDensityGen (RandomStream stream,
                                 ContinuousDistribution dis,
                                 double xc, double eps, int order)  {
      super (stream, null);
      dist = new InverseDistFromDensity (dis, xc, eps, order);
   }


   /**
    * Creates a new generator from the <SPAN  CLASS="textit">continuous</SPAN> probability density
    * <TT>dens</TT>. The left and the right boundaries of the density are
    * <TT>xleft</TT> and <TT>xright</TT> (the density is 0 outside the
    * interval <TT>[xleft, xright]</TT>).
    * See the description of the other constructor.
    * 
    */
   public InverseFromDensityGen (RandomStream stream, MathFunction dens,
                                 double xc, double eps, int order,
                                 double xleft, double xright)  {
      super (stream, null);
      dist = new InverseDistFromDensity (dens, xc, eps, order, xleft, xright);
   } 


   /**
    * Generates a new random variate.
    * 
    */
   public double nextDouble()  {
      return dist.inverseF (stream.nextDouble());
   }


   /**
    * Returns the <TT>xc</TT> given in the constructor.
    * 
    */
   public double getXc() {
      return ((InverseDistFromDensity)dist).getXc();
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>u</I></SPAN>-resolution <TT>eps</TT>.
    * 
    */
   public double getEpsilon() {
      return ((InverseDistFromDensity)dist).getEpsilon();
   }



   /**
    * Returns the order of the interpolating polynomial.
    * 
    * 
    */
   public int getOrder() {
      return ((InverseDistFromDensity)dist).getOrder();
   }


}