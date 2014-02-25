

/*
 * Class:        BernoulliGen
 * Description:  random variate generators for the Bernoulli distribution
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
import umontreal.iro.lecuyer.probdist.BernoulliDist;
import umontreal.iro.lecuyer.rng.RandomStream;

/**
 * This class implements random variate generators for the 
 * <EM>Bernoulli</EM> distribution (see class
 *  {@link umontreal.iro.lecuyer.probdist.BernoulliDist BernoulliDist}).
 * 
 */
public class BernoulliGen extends RandomVariateGenInt  {
   protected double p;    
    


   /**
    * Creates a Bernoulli random variate generator with parameter <SPAN CLASS="MATH"><I>p</I></SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public BernoulliGen (RandomStream s, double p) {
      super (s, new BernoulliDist (p));
      setParams (p);
   }


   /**
    * Creates a random variate generator for the <EM>Bernoulli</EM> 
    *     distribution <TT>dist</TT> and the random stream <TT>s</TT>.
    * 
    */
   public BernoulliGen (RandomStream s, BernoulliDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getP());
   }


   /**
    * Generates a new integer from the <EM>Bernoulli</EM> distribution with
    *   parameter <SPAN CLASS="MATH"><I>p</I> =</SPAN>&nbsp;<TT>p</TT>, using the given stream <TT>s</TT>.
    * 
    */
   public static int nextInt (RandomStream s, double p) {
      return BernoulliDist.inverseF (p, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    * 
    */
   public double getP() {
      return p;
   }
   


   /**
    * Sets the parameter <SPAN CLASS="MATH"><I>p</I></SPAN> of this object.
    * 
    */
   protected void setParams (double p) {
      if (p < 0.0 || p > 1.0)
         throw new IllegalArgumentException ("p not in range [0, 1]");
      this.p = p;
   }

}
