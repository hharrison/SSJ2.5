

/*
 * Class:        LogarithmicGen
 * Description:  random variate generators for the (discrete) logarithmic distribution
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
import umontreal.iro.lecuyer.util.Num;


/**
 * This class implements random variate generators for the (discrete)
 * <EM>logarithmic</EM> distribution. Its  mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = -<I>&#952;</I><SUP>x</SUP>/<I>x</I> log(1-<I>&#952;</I>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; for <I>x</I> = 1, 2,&#8230;,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH">0 &lt; <I>&#952;</I> &lt; 1</SPAN>.
 * It uses inversion with the LS chop-down algorithm if 
 * <SPAN CLASS="MATH"><I>&#952;</I> &lt; <I>&#952;</I><SUB>0</SUB></SPAN>
 * and the LK transformation algorithm if 
 * <SPAN CLASS="MATH"><I>&#952;</I>&nbsp;&gt;=&nbsp;<I>&#952;</I><SUB>0</SUB></SPAN>,
 * as described in.
 * The threshold <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB></SPAN> can be specified when invoking the constructor.
 * Its default value is 
 * <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB> = 0.96</SPAN>, as suggested in.
 * 
 * <P>
 * A local copy of the parameter <SPAN CLASS="MATH"><I>&#952;</I></SPAN> is maintained in this class.
 * 
 */
public class LogarithmicGen extends RandomVariateGenInt  {
   private static final double default_theta_limit = 0.96;

   private double theta_limit = default_theta_limit;
   private double theta;
   private double t;      // = log (1.0-theta).
   private double h;      // = -theta/log (1.0-theta).



   /**
    * Creates a logarithmic random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT> and default value 
    * <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB> = 0.96</SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public LogarithmicGen (RandomStream s, double theta) {
      this (s, theta, default_theta_limit);
   }


   /**
    * Creates a logarithmic random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT> and 
    * <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB> = <texttt>theta0</texttt></SPAN>,
    *   using stream <TT>s</TT>.
    * 
    */
   public LogarithmicGen (RandomStream s, double theta, double theta0) {
      super (s, null);
      this.theta = theta;
      theta_limit = theta0;
      init();
   }


   /**
    * Creates a new generator with distribution <TT>dist</TT> and
    *   stream <TT>s</TT>, with default value 
    * <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB> = 0.96</SPAN>.
    * 
    */
   public LogarithmicGen (RandomStream s, LogarithmicDist dist) {
      this (s, dist, default_theta_limit);
   }


   /**
    * Creates a new generator with distribution <TT>dist</TT>
    *    and stream <TT>s</TT>, with 
    * <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB> = <texttt>theta0</texttt></SPAN>.
    * 
    */
   public LogarithmicGen (RandomStream s, LogarithmicDist dist,
                          double theta0) {
      super (s, dist);
      theta_limit = theta0;
      if (dist != null)
         theta = dist.getTheta();
      init();
   }


   private void init () {
      if (theta <= 0.0 || theta >= 1.0)
         throw new IllegalArgumentException ("theta not in (0, 1)");
      if (theta >= theta_limit)
         h = Math.log1p(-theta);
      else
         t = -theta / Math.log1p(-theta);
   }

   public int nextInt() {
      if (theta < theta_limit)
         return ls (stream, theta, t);
      else   // Transformation
         return lk (stream, theta, h);
   }

   /**
    * Uses stream <TT>s</TT> to generate
    *    a new variate from the <EM>logarithmic</EM> distribution with parameter
    *  <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT>.
    * 
    */
   public static int nextInt (RandomStream s, double theta) {
      if (theta < default_theta_limit)
         return ls (s, theta, -theta/Math.log1p(-theta));
      else   // Transformation
         return lk (s, theta, Math.log1p(-theta));
   }



//>>>>>>>>>>>>>>>>>>>>  P R I V A T E    M E T H O D S   <<<<<<<<<<<<<<<<<<<<


   private static int ls (RandomStream stream, double theta, double t) {
      double u = stream.nextDouble();
      int x = 1;

      double p =  t;

      while (u > p) {
            u -= p;
            x++;
            p *= theta*((double) x - 1.0)/((double)x);
      }
      return x;
   }

   private static int lk (RandomStream stream, double theta, double h) {
      double u, v, p, q;
      int x;

      u = stream.nextDouble();
      if (u > theta)
            return 1;
      v = stream.nextDouble();
      q = 1.0 - Math.exp(v * h);
      if ( u <= q * q) {
           x = (int)(1. + (Math.log(u) / Math.log(q)));
           return x;
      }
      return ((u > q) ? 1 : 2);
   }

   /**
    * Returns the <SPAN CLASS="MATH"><I>&#952;</I></SPAN> associated with this object.
    * 
    */
   public double getTheta() {
      return theta;
   }


   /**
    * Returns the <SPAN CLASS="MATH"><I>&#952;</I><SUB>0</SUB></SPAN> associated with this object.
    * 
    */
   public double getTheta0() {
      return theta_limit;
   }

}
