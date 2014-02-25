

/*
 * Class:        ContinuousDistribution2Dim
 * Description:  Mother class 2-dimensional continuous distributions
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

import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.util.Num;


/**
 * Classes implementing 2-dimensional continuous distributions should inherit 
 * from this class.
 * Such distributions are characterized by a <SPAN  CLASS="textit">density</SPAN> function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>, <I>y</I>)</SPAN>;
 * thus the signature of a <TT>density</TT> method is supplied here.
 * This class also provides a default implementation of 
 * <SPAN CLASS="MATH">bar(F)(<I>x</I>, <I>y</I>)</SPAN>, 
 * the upper <TT>CDF</TT>. The inverse function <SPAN CLASS="MATH"><I>F</I><SUP>-1</SUP>(<I>u</I>)</SPAN> represents a curve
 * <SPAN CLASS="MATH"><I>y</I> = <I>h</I>(<I>x</I>)</SPAN> of constant <SPAN CLASS="MATH"><I>u</I></SPAN> and it is not implemented.
 * 
 */
public abstract class ContinuousDistribution2Dim
                          extends ContinuousDistributionMulti {

   /**
    * Defines the target number of decimals of accuracy when
    *  approximating a distribution function, but there is <SPAN  CLASS="textit">no guarantee</SPAN> that
    *  this target is always attained.
    * 
    */
   public int decPrec = 15;
 

    // x infinity for some distributions
     protected static final double XINF = Double.MAX_VALUE;  

    // x infinity for some distributions                                       
    protected static final double XBIG = 1000.0;  

    // EPSARRAY[j]: Epsilon required for j decimal degits of precision
    protected static final double[] EPSARRAY = {
    0.5, 0.5E-1, 0.5E-2, 0.5E-3, 0.5E-4, 0.5E-5, 0.5E-6, 0.5E-7, 0.5E-8,
    0.5E-9, 0.5E-10, 0.5E-11, 0.5E-12, 0.5E-13, 0.5E-14, 0.5E-15, 0.5E-16,
    0.5E-17, 0.5E-18, 0.5E-19, 0.5E-20, 0.5E-21, 0.5E-22, 0.5E-23, 0.5E-24,
    0.5E-25, 0.5E-26, 0.5E-27, 0.5E-28, 0.5E-29, 0.5E-30, 0.5E-31, 0.5E-32,
    0.5E-33, 0.5E-34, 0.5E-35
    };

   /**
    * Returns <SPAN CLASS="MATH"><I>f</I> (<I>x</I>, <I>y</I>)</SPAN>, the density of <SPAN CLASS="MATH">(<I>X</I>, <I>Y</I>)</SPAN> evaluated at <SPAN CLASS="MATH">(<I>x</I>, <I>y</I>)</SPAN>.
    * 
    * @param x value <SPAN CLASS="MATH"><I>x</I></SPAN> at which the density is evaluated
    * 
    *    @param y value <SPAN CLASS="MATH"><I>y</I></SPAN> at which the density is evaluated
    * 
    *    @return density function evaluated at <SPAN CLASS="MATH">(<I>x</I>, <I>y</I>)</SPAN>
    * 
    */
   public abstract double density (double x, double y);


   /**
    * Simply calls <TT>density (x[0], x[1])</TT>.
    * 
    * @param x point 
    * <SPAN CLASS="MATH">(<I>x</I>[0], <I>x</I>[1])</SPAN> at which the density is evaluated
    * 
    *    @return density function evaluated at 
    * <SPAN CLASS="MATH">(<I>x</I>[0], <I>x</I>[1])</SPAN>
    * 
    */
   public double density (double[] x) {
      if (x.length != 2)
         throw new IllegalArgumentException("x must be in dimension 2");

      return density (x[0], x[1]);
   }


   /**
    * .
    * 
    * Computes the distribution function <SPAN CLASS="MATH"><I>F</I>(<I>x</I>, <I>y</I>)</SPAN>: 
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>F</I>(<I>x</I>, <I>y</I>) = <I>P</I>[<I>X</I>&nbsp;&lt;=&nbsp;<I>x</I>, <I>Y</I>&nbsp;&lt;=&nbsp;<I>y</I>] = &int;<SUB>-&#8734;</SUB><SUP>x</SUP><I>ds</I>&int;<SUB>-&#8734;</SUB><SUP>y</SUP><I>dt</I>&nbsp;<I>f</I> (<I>s</I>, <I>t</I>).
    * </DIV><P></P>
    *  
    *  @param x value <SPAN CLASS="MATH"><I>x</I></SPAN> at which the distribution function is evaluated
    * 
    *     @param y value <SPAN CLASS="MATH"><I>y</I></SPAN> at which the distribution function is evaluated
    * 
    *     @return distribution function evaluated at  <SPAN CLASS="MATH">(<I>x</I>, <I>y</I>)</SPAN>
    *  
    */
   public abstract double cdf (double x, double y); 

   /**
    * .
    * 
    * Computes  the upper cumulative distribution function
    *   
    * <SPAN CLASS="MATH">bar(F)(<I>x</I>, <I>y</I>)</SPAN>:
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * bar(F)(<I>x</I>, <I>y</I>) = <I>P</I>[<I>X</I>&nbsp;&gt;=&nbsp;<I>x</I>, <I>Y</I>&nbsp;&gt;=&nbsp;<I>y</I>] = &int;<SUP>&#8734;</SUP><SUB>x</SUB><I>ds</I>&int;<SUP>&#8734;</SUP><SUB>y</SUB><I>dt</I>&nbsp;<I>f</I> (<I>s</I>, <I>t</I>).
    * </DIV><P></P>
    * 
    * @param x value <SPAN CLASS="MATH"><I>x</I></SPAN> at which the upper distribution is evaluated
    * 
    *    @param y value <SPAN CLASS="MATH"><I>y</I></SPAN> at which the upper distribution is evaluated
    * 
    *    @return upper distribution function evaluated at  <SPAN CLASS="MATH">(<I>x</I>, <I>y</I>)</SPAN>
    * 
    */
   public double barF (double x, double y)  {
      double u = 1.0 + cdf (x, y) - cdf (XINF, y) - cdf (x, XINF);
      if (u <= 0.0) return 0.0;
      if (u >= 1.0) return 1.0;
      return u;
   }


   /**
    * .
    * 
    * Computes the cumulative probability in the square region
    *  
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>P</I>[<I>a</I><SUB>1</SUB>&nbsp;&lt;=&nbsp;<I>X</I>&nbsp;&lt;=&nbsp;<I>b</I><SUB>1</SUB>,&nbsp;<I>a</I><SUB>2</SUB>&nbsp;&lt;=&nbsp;<I>Y</I>&nbsp;&lt;=&nbsp;<I>b</I><SUB>2</SUB>] = &int;<SUB>a<SUB>1</SUB></SUB><SUP>b<SUB>1</SUB></SUP><I>dx</I>&int;<SUB>a<SUB>2</SUB></SUB><SUP>b<SUB>2</SUB></SUP><I>dy</I>&nbsp;<I>f</I> (<I>x</I>, <I>y</I>).
    * </DIV><P></P> 
    *  
    * @param a1 <SPAN CLASS="MATH"><I>x</I></SPAN> lower limit of the square
    * 
    *    @param a2 <SPAN CLASS="MATH"><I>y</I></SPAN> lower limit of the square
    * 
    *    @param b1 <SPAN CLASS="MATH"><I>x</I></SPAN> upper limit of the square
    * 
    *    @param b2 <SPAN CLASS="MATH"><I>y</I></SPAN> upper limit of the square
    * 
    *    @return the cumulative probability in the square region
    * 
    */
   public double cdf (double a1, double a2, double b1, double b2)  {
      if (a1 >= b1 || a2 >= b2) return 0.0;
      return cdf (b1, b2) - cdf (a1, b2) - cdf (b1, a2) + cdf(a1, a2);
   }


}
