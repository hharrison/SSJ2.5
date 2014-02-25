

/*
 * Class:        BetaSymmetricalPolarGen
 * Description:  symmetrical beta random variate generators using
                 Ulrich's polar method 
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
import umontreal.iro.lecuyer.probdist.BetaSymmetricalDist;


/**
 * This class implements <EM>symmetrical beta</EM> random variate generators using
 * Ulrich's polar method. The method generates two uniform
 * random variables 
 * <SPAN CLASS="MATH"><I>x</I>&#8712;[0, 1]</SPAN> and 
 * <SPAN CLASS="MATH"><I>y</I>&#8712;[- 1, 1]</SPAN> until
 *  
 * <SPAN CLASS="MATH"><I>x</I><SUP>2</SUP> + <I>y</I><SUP>2</SUP>&nbsp;&lt;=&nbsp;1</SPAN>. Then it returns
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq.beta.ulrich"></A>
 * (1/2) + (<I>xy</I>(1 - S^2/(2&alpha;- 1))<SUP>1/2</SUP>)/<I>S</I>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>S</I> = <I>x</I><SUP>2</SUP> + <I>y</I><SUP>2</SUP></SPAN>, and <SPAN CLASS="MATH"><I>&#945;</I></SPAN> is the shape parameter of the beta
 * distribution. The method is valid only when 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
 * 
 */
public class BetaSymmetricalPolarGen extends BetaSymmetricalGen  {
   private double afactor;      // = 2/(2*alpha - 1)
   private RandomStream stream2;



   /**
    * Creates a symmetrical beta random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>,  using stream <TT>s1</TT> to generate <SPAN CLASS="MATH"><I>x</I></SPAN>
    *   and stream <TT>s2</TT> to generate <SPAN CLASS="MATH"><I>y</I></SPAN>, as in above. Restriction: 
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public BetaSymmetricalPolarGen (RandomStream s1, RandomStream s2,
                                   double alpha)  {
      super (s1, null);
      stream2 = s2;
      if (alpha <= 0.5)
         throw new IllegalArgumentException ("  must have alpha > 1/2");
      afactor = 2.0/(2.0*alpha - 1.0);
      setParams (alpha, alpha, 0.0, 1.0);
   }


   /**
    * Creates a symmetrical beta random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>,  using stream <TT>s1</TT> to generate <SPAN CLASS="MATH"><I>x</I></SPAN>
    *  and <SPAN CLASS="MATH"><I>y</I></SPAN>, as in above.  Restriction: 
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public BetaSymmetricalPolarGen (RandomStream s1, double alpha)  {
      this (s1, s1, alpha);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>, using
    *    stream <TT>s1</TT> to generate <SPAN CLASS="MATH"><I>x</I></SPAN> and stream <TT>s2</TT> to generate <SPAN CLASS="MATH"><I>y</I></SPAN>,
    *    as in above.
    *     Restriction: <TT>dist</TT> must have 
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public BetaSymmetricalPolarGen (RandomStream s1, RandomStream s2,
                                   BetaSymmetricalDist dist)  {
      super (s1, dist);
      stream2 = s2;
      double alp = dist.getAlpha();
      if (alp <= 0.5)
         throw new IllegalArgumentException ("  must have alpha > 1/2");
      afactor = 2.0/(2.0*dist.getAlpha() - 1.0);
      setParams (alp, alp, 0.0, 1.0);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using only one stream <TT>s1</TT>.
    *     Restriction: <TT>dist</TT> must have 
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public BetaSymmetricalPolarGen (RandomStream s1,
                                   BetaSymmetricalDist dist)  {
      this (s1, s1, dist);
   }


   /**
    * Generates a random number using Ulrich's polar method. Stream
    *   <TT>s1</TT> generates <SPAN CLASS="MATH"><I>x</I></SPAN> and stream <TT>s2</TT>  generates <SPAN CLASS="MATH"><I>y</I></SPAN>
    *    [see eq.].
    *   Restriction:  
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s1, RandomStream s2,
                                    double alpha)   {
      double u, v, S;
      do {
         u = s1.nextDouble();
         v = -1.0 + 2.0*s2.nextDouble();
         S = u*u + v*v;
      } while (S > 1.0);
      return 0.5 + u*v/S* Math.sqrt(1.0 - Math.pow(S, 2.0/(2.0*alpha - 1.0)));
   }


   /**
    * Generates a random number by Ulrich's polar method using
    *   stream <TT>s</TT>.  Restriction:  
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 1/2</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha)   {
      return nextDouble (s, s, alpha);
   }
 

   public double nextDouble() {
      // Generates a random number using Ulrich's polar method.
      double u, v, S;
      do {
         u = stream.nextDouble();
         v = -1.0 + 2.0*stream2.nextDouble();
         S = u*u + v*v;
      } while (S > 1.0);
      return 0.5 + u*v/S* Math.sqrt(1.0 - Math.pow(S, afactor));
  }

   /**
    * Returns stream <TT>s2</TT> associated with this object.
    * 
    */
   public RandomStream getStream2() {
      return stream2;
   }

}

