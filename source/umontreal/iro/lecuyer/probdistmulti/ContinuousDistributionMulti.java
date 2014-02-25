

/*
 * Class:        ContinuousDistributionMulti
 * Description:  mother class for continuous multidimensional distributions 
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
 * Classes implementing continuous multi-dimensional distributions should inherit 
 * from this class. Such distributions are characterized by a <SPAN  CLASS="textit">density</SPAN>
 *  function 
 * <SPAN CLASS="MATH"><I>f</I> (<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>)</SPAN>;
 * thus the signature of a <TT>density</TT> method is supplied here.
 * All array indices start at 0.
 * 
 */
public abstract class ContinuousDistributionMulti {
   protected int dimension;



   /**
    * Returns 
    * <SPAN CLASS="MATH"><I>f</I> (<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>)</SPAN>, the probability density of
    *   <SPAN CLASS="MATH"><I>X</I></SPAN> evaluated at the point
    *  <SPAN CLASS="MATH"><I>x</I></SPAN>, where 
    * <SPAN CLASS="MATH"><I>x</I> = {<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>}</SPAN>. The convention is that 
    *   
    * <SPAN CLASS="MATH"><texttt>x</texttt>[<texttt>i</texttt> - <texttt>1</texttt>] = <I>x</I><SUB>i</SUB></SPAN>.
    * 
    * @param x value at which the density is evaluated
    * 
    *    @return density function evaluated at <TT>x</TT>
    * 
    */
   public abstract double density (double[] x);


   /**
    * Returns the dimension <SPAN CLASS="MATH"><I>d</I></SPAN> of the distribution.
    * 
    */
   public int getDimension() {
      return dimension;
   }


   /**
    * Returns the mean vector of the distribution, defined as 
    * <SPAN CLASS="MATH"><I>&#956;</I><SUB>i</SUB> = <I>E</I>[<I>X</I><SUB>i</SUB>]</SPAN>.
    * 
    */
   public abstract double[] getMean();


   /**
    * Returns the variance-covariance matrix of the distribution, defined as
    * <BR>   
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>ij</SUB> = <I>E</I>[(<I>X</I><SUB>i</SUB> - <I>&#956;</I><SUB>i</SUB>)(<I>X</I><SUB>j</SUB> - <I>&#956;</I><SUB>j</SUB>)]</SPAN>.
    * 
    */
   public abstract double[][] getCovariance();


   /**
    * Returns the correlation matrix of the distribution, defined as
    *       
    * <SPAN CLASS="MATH"><I>&#961;</I><SUB>ij</SUB> = <I>&#963;</I><SUB>ij</SUB>/(&sigma;_ii&sigma;_jj)<SUP>1/2</SUP></SPAN>.
    * 
    */
   public abstract double[][] getCorrelation();

}
