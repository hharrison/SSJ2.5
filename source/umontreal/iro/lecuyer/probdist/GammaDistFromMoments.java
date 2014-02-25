

/*
 * Class:        GammaDistFromMoments
 * Description:  gamma distribution
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
 * Extends the {@link GammaDist} distribution with constructors accepting the
 * mean <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and variance <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP></SPAN> as arguments instead of a shape parameter
 * <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and a scale parameter <SPAN CLASS="MATH"><I>&#955;</I></SPAN>.
 * Since 
 * <SPAN CLASS="MATH"><I>&#956;</I> = <I>&#945;</I>/<I>&#955;</I></SPAN>, and 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP> = <I>&#945;</I>/<I>&#955;</I><SUP>2</SUP></SPAN>,
 * the shape and scale parameters are 
 * <SPAN CLASS="MATH"><I>&#945;</I> = <I>&#956;</I><SUP>2</SUP>/<I>&#963;</I><SUP>2</SUP></SPAN>, and
 * 
 * <SPAN CLASS="MATH"><I>&#955;</I> = <I>&#956;</I>/<I>&#963;</I><SUP>2</SUP></SPAN>, respectively.
 * 
 */
public class GammaDistFromMoments extends GammaDist {



   /**
    * Constructs a gamma distribution with mean <TT>mean</TT>,
    *  variance <TT>var</TT>, and <TT>d</TT> decimal of precision.
    * 
    * @param mean the desired mean.
    * 
    *    @param var the desired variance.
    * 
    *    @param d the number of decimals of precision.
    * 
    * 
    */
   public GammaDistFromMoments (double mean, double var, int d) {
      super (mean * mean / var, mean / var, d);
   }


   /**
    * Constructs a gamma distribution with
    *  mean <TT>mean</TT>, and variance <TT>var</TT>.
    * 
    * @param mean the desired mean.
    * 
    *    @param var the desired variance.
    * 
    */
   public GammaDistFromMoments (double mean, double var) {
      super (mean * mean / var, mean / var);
   }
}
