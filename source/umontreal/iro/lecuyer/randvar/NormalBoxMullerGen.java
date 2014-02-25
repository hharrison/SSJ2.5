

/*
 * Class:        NormalBoxMullerGen
 * Description:  normal random variate generators using the Box-Muller method
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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements <EM>normal</EM> random variate generators using
 *  the <EM>Box-Muller</EM> method. 
 * Since the method generates two variates at a time, 
 * the second variate is returned upon the next call to the {@link #nextDouble nextDouble}.
 * 
 */
public class NormalBoxMullerGen extends NormalGen  {
   private boolean available = false;
   private double[] variates = new double[2];
   private static double[] staticVariates = new double[2];
   // used by polar method which calculate always two random values; 
  


   /**
    * Creates a normal random variate generator with mean <TT>mu</TT>
    *   and standard deviation <TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public NormalBoxMullerGen (RandomStream s, double mu, double sigma)  {
      super (s, null);
      setParams (mu, sigma);
   }


   /**
    * Creates a standard normal random variate generator with mean
    *   <TT>0</TT> and standard deviation <TT>1</TT>, using stream <TT>s</TT>.
    * 
    */
   public NormalBoxMullerGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }


   /**
    * Creates a random variate generator for the normal distribution 
    *   <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public NormalBoxMullerGen (RandomStream s, NormalDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }
 
  
   public double nextDouble() {
      if (available) {
         available = false;
         return mu + sigma*variates[1];
      }
      else {
         boxMuller (stream, mu, sigma, variates);
         available = true;
         return mu + sigma*variates[0];
      }
   }

   public static double nextDouble (RandomStream s, double mu, double sigma) {
      boxMuller (s, mu, sigma, staticVariates);
      return mu + sigma*staticVariates[0];
   }
   /**
    * Generates a variate from the normal distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN>&nbsp;<TT>sigma</TT>, using
    *    stream <TT>s</TT>.
    * 
    */


//>>>>>>>>>>>>>>>>>>>>  P R I V A T E S    M E T H O D S   <<<<<<<<<<<<<<<<<<<< 

   private static void boxMuller (RandomStream stream, double mu, 
                                  double sigma, double[] variates) {
      final double pi = Math.PI;
      double u,v,s;
    
      u = stream.nextDouble();
      v = stream.nextDouble();
      s = Math.sqrt (-2.0*Math.log (u));
      variates[1] = s*Math.sin (2*pi*v);
      variates[0] = s*Math.cos (2*pi*v);
   }

} 
