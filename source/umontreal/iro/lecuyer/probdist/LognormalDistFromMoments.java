

/*
 * Class:        LognormalDistFromMoments
 * Description:  lognormal distribution
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

package umontreal.iro.lecuyer.probdist;


/**
 * Extends the {@link LognormalDist} class with a constructor accepting the
 * mean <SPAN CLASS="MATH"><I>m</I></SPAN> and the variance <SPAN CLASS="MATH"><I>v</I></SPAN> of the distribution as arguments.
 * The mean and variance of a lognormal random variable with
 * parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> are
 * 
 * <SPAN CLASS="MATH"><I>e</I><SUP><I>&#956;</I>+<I>&#963;</I><SUP>2</SUP>/2</SUP></SPAN> and
 * 
 * <SPAN CLASS="MATH"><I>e</I><SUP>2<I>&#956;</I>+<I>&#963;</I><SUP>2</SUP></SUP>(<I>e</I><SUP><I>&#963;</I><SUP>2</SUP></SUP> - 1)</SPAN> respectively, so
 * the parameters are given by 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP> = ln(<I>v</I>/<I>m</I><SUP>2</SUP>+1)</SPAN> and
 * 
 * <SPAN CLASS="MATH"><I>&#956;</I> = ln(<I>m</I>) - <I>&#963;</I><SUP>2</SUP>/2</SPAN>.
 * 
 */
public class LognormalDistFromMoments extends LognormalDist {



   public LognormalDistFromMoments (double mean, double var) {
      super (getMu (mean, var), Math.sqrt (getSigma2 (mean, var)));
   }

   private static double getMu (double mean, double var) {
      final double sigma2 = getSigma2 (mean, var);
      return Math.log (mean) - sigma2 / 2.0;
   }

   private static double getSigma2 (double mean, double var) {
      if (mean <= 0)
         throw new IllegalArgumentException ("Mean must be positive");
      if (var <= 0)
         throw new IllegalArgumentException ("Variance must be positive");
      return Math.log (var / (mean * mean) + 1);
   }
}
