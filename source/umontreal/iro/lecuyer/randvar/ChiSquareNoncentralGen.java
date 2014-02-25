

/*
 * Class:        ChiSquareNoncentralGen
 * Description:  random variate generators for the noncentral chi square distribution
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements random variate generators for the
 * <EM>noncentral chi square</EM> distribution with <SPAN CLASS="MATH"><I>&#957;</I> &gt; 0</SPAN> degrees of freedom
 * and noncentrality parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>. See the definition in
 *  {@link umontreal.iro.lecuyer.probdist.ChiSquareNoncentralDist ChiSquareNoncentralDist}.
 * 
 */
public class ChiSquareNoncentralGen extends RandomVariateGen  {
   protected double nu = -1.0;
   protected double lambda = -1.0;




   /**
    * Creates a <SPAN  CLASS="textit">noncentral chi square</SPAN>  random variate generator
    *  with <TT>nu</TT> <SPAN CLASS="MATH">= <I>&#957;</I> &gt; 0</SPAN> degrees of freedom and noncentrality parameter 
    * <TT>lambda</TT> 
    * <SPAN CLASS="MATH">= <I>&#955;</I> &gt; 0</SPAN>,
    *  using stream <TT>s</TT>.
    * 
    */
   public ChiSquareNoncentralGen (RandomStream s, double nu, double lambda)  {
      super (s, new ChiSquareNoncentralDist(nu, lambda));
      setParams (nu, lambda);
   }


   /**
    * Create a new generator for the distribution <TT>dist</TT>
    *     and stream <TT>s</TT>.
    * 
    */
   public ChiSquareNoncentralGen (RandomStream s,
                                  ChiSquareNoncentralDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getNu (), dist.getLambda());
   }


   /**
    * Generates a new variate from the noncentral chi square
    * distribution with <TT>nu</TT> = <SPAN CLASS="MATH"><I>&#957;</I></SPAN> degrees of freedom and noncentrality 
    * parameter <TT>lambda</TT> <SPAN CLASS="MATH">= <I>&#955;</I></SPAN>,
    *  using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s,
                                    double nu, double lambda)  {
      return ChiSquareNoncentralDist.inverseF (nu, lambda, s.nextDouble());
   }



   /**
    * Returns the value of  <SPAN CLASS="MATH"><I>&#957;</I></SPAN> of this object.
    * 
    */
   public double getNu() {
      return nu;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#955;</I></SPAN> for this object.
    * 
    * 
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN> <TT>nu</TT> and <SPAN CLASS="MATH"><I>&#955;</I> =</SPAN>
    *   <TT>lambda</TT> of this object.
    * 
    */
   protected void setParams (double nu, double lambda) {
      if (nu <= 0.0)
         throw new IllegalArgumentException ("nu <= 0");
      if (lambda < 0.0)
         throw new IllegalArgumentException ("lambda < 0");
      this.nu = nu;
      this.lambda = lambda;
   }

}
