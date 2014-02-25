

/*
 * Class:        DiscreteDistributionIntMulti
 * Description:  mother class for discrete distributions over the integers
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


/**
 * Classes implementing multi-dimensional discrete distributions over the integers
 * should inherit from this class.
 * It specifies the signature of methods for computing the mass function
 * (or probability) 
 * <SPAN CLASS="MATH"><I>p</I>(<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>) = <I>P</I>[<I>X</I><SUB>1</SUB> = <I>x</I><SUB>1</SUB>, <I>X</I><SUB>2</SUB> = <I>x</I><SUB>2</SUB>,&#8230;, <I>X</I><SUB>d</SUB> = <I>x</I><SUB>d</SUB>]</SPAN> and the cumulative probabilities 
 * for a random vector <SPAN CLASS="MATH"><I>X</I></SPAN> with a discrete distribution over the integers.
 * 
 */
public abstract class DiscreteDistributionIntMulti {
   protected int dimension;
  

   /**
    * Returns the probability mass function 
    * <SPAN CLASS="MATH"><I>p</I>(<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>)</SPAN>,
    *    which should be a real number in <SPAN CLASS="MATH">[0, 1]</SPAN>.
    *  
    * @param x value at which the mass function must be evaluated
    * 
    *    @return the mass function evaluated at <TT>x</TT>
    * 
    */
   public abstract double prob (int[] x);


   /**
    * Computes the cumulative probability function <SPAN CLASS="MATH"><I>F</I></SPAN> of the distribution evaluated
    *    at <TT>x</TT>, assuming the lowest values start at 0, i.e. computes
    * 
    * <P></P>
    * <DIV ALIGN="CENTER" CLASS="mathdisplay">
    * <I>F</I>(<I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>d</SUB>) = &sum;<SUB>s<SUB>1</SUB>=0</SUB><SUP>x<SUB>1</SUB></SUP>&sum;<SUB>s<SUB>2</SUB>=0</SUB><SUP>x<SUB>2</SUB> ... </SUP>&sum;<SUB>s<SUB>d</SUB>=0</SUB><SUP>x<SUB>d</SUB></SUP><I>p</I>(<I>s</I><SUB>1</SUB>, <I>s</I><SUB>2</SUB>,&#8230;, <I>s</I><SUB>d</SUB>).
    * </DIV><P></P>
    * Uses the naive implementation, is very inefficient and may underflows.
    * 
    */
   public double cdf (int x[]) {
      int is[] = new int[x.length];
      for (int i = 0; i < is.length; i++)
         is[i] = 0;

      boolean end = false;
      double sum = 0.0;
      int j;
      while (!end) {
         sum += prob (is);
         is[0]++;

         if (is[0] > x[0]) {
            is[0] = 0;
            j = 1;
            while (j < x.length && is[j] == x[j])
               is[j++] = 0;

            if (j == x.length)
               end = true;
            else
               is[j]++;
         }
      }

      return sum;
   }


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
