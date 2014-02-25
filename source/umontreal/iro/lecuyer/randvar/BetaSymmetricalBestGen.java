

/*
 * Class:        BetaSymmetricalBestGen
 * Description:  symmetrical beta random variate generators using
                 Devroye's one-liner method.
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
 * Devroye's one-liner method. It is based on Best's relation 
 * between a Student-<SPAN CLASS="MATH"><I>t</I></SPAN> variate and a symmetrical beta variate:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>B</I><SUB><I>&#945;</I>, <I>&#945;</I></SUB><IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="BetaSymmetricalBestGenimg1.png"
 *  ALT="$\displaystyle \;\stackrel{{ L}}{{=}}\;$"><IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="BetaSymmetricalBestGenimg2.png"
 *  ALT="$\displaystyle {\textstyle\frac{1}{2}}$">(1 + <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="BetaSymmetricalBestGenimg3.png"
 *  ALT="$\displaystyle {\frac{{T_{2\alpha}}}{{\sqrt{2\alpha + T_{2\alpha}^2}}}}$">).
 * </DIV><P></P>
 * If <SPAN CLASS="MATH"><I>S</I></SPAN> is a random sign and <SPAN CLASS="MATH"><I>U</I><SUB>1</SUB></SPAN>, <SPAN CLASS="MATH"><I>U</I><SUB>2</SUB></SPAN> are two independent uniform <SPAN CLASS="MATH">[0, 1]</SPAN>
 * random variates, then the following gives a symmetrical
 *  beta variate:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq.beta.best"></A>
 * <I>B</I><SUB><I>&#945;</I>, <I>&#945;</I></SUB>[tex2html_wrap_indisplay202][tex2html_wrap_indisplay203] + <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="BetaSymmetricalBestGenimg4.png"
 *  ALT="$\displaystyle {\frac{{S}}{{2
 * \sqrt{1 + \frac{1}{(U_1^{-1/\alpha} - 1)\cos^2(2\pi U_2)}}}}}$">
 * </DIV><P></P>
 * valid for any shape parameter 
 * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>.
 * 
 */
public class BetaSymmetricalBestGen extends BetaSymmetricalGen  {
   private RandomStream stream2;
   private RandomStream stream3;
   private double afactor;          // = 1/alpha
   private static final double TWOPI = 2.0*Math.PI;      // = 2 Pi



   /**
    * Creates a symmetrical beta random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>, using stream <TT>s1</TT> to generate
    *   <SPAN CLASS="MATH"><I>U</I><SUB>1</SUB></SPAN>, stream <TT>s2</TT> to generate <SPAN CLASS="MATH"><I>U</I><SUB>2</SUB></SPAN> and stream <TT>s3</TT> to
    *   generate <SPAN CLASS="MATH"><I>S</I></SPAN>, as given in equation.
    * 
    */
   public BetaSymmetricalBestGen (RandomStream s1, RandomStream s2,
                                  RandomStream s3, double alpha)  {
      super (s1, null);
      stream2 = s2;
      stream3 = s3;
      afactor = 1.0/alpha;
      setParams (alpha, alpha, 0.0, 1.0);
   }


   /**
    * Creates a symmetrical beta random variate generator with
    *  parameter <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>,  using only one stream <TT>s1</TT>
    *   to generate <SPAN CLASS="MATH"><I>U</I><SUB>1</SUB></SPAN>, <SPAN CLASS="MATH"><I>U</I><SUB>2</SUB></SPAN>, and <SPAN CLASS="MATH"><I>S</I></SPAN> as given in equation.
    * 
    */
   public BetaSymmetricalBestGen (RandomStream s1, double alpha)  {
     this (s1, s1, s1, alpha);
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *  using stream <TT>s1</TT> to generate <SPAN CLASS="MATH"><I>U</I><SUB>1</SUB></SPAN>, stream <TT>s2</TT> to generate <SPAN CLASS="MATH"><I>U</I><SUB>2</SUB></SPAN>
    *  and stream <TT>s3</TT> to generate <SPAN CLASS="MATH"><I>S</I></SPAN> as given in equation.
    * 
    */
   public BetaSymmetricalBestGen (RandomStream s1, RandomStream s2,
                                  RandomStream s3, BetaSymmetricalDist dist)  {
      super (s1, dist);
      stream2 = s2;
      stream3 = s3;
      afactor = 1.0/dist.getAlpha();
      if (dist != null)
         setParams (dist.getAlpha(), dist.getAlpha(), dist.getA(), dist.getB());
   }


   /**
    * Creates a new generator for the distribution <TT>dist</TT>,
    *      using only one stream <TT>s1</TT>.
    * 
    */
   public BetaSymmetricalBestGen (RandomStream s1, BetaSymmetricalDist dist)  {
     this (s1, s1, s1, dist);
   }


   /**
    * Generates a random number using Devroye's one-liner method.
    *    Restriction:  
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s1, RandomStream s2,
                                    RandomStream s3, double alpha)   {
      double cos, temp, v, S;
      cos = Math.cos (TWOPI * s2.nextDouble());
      temp = 1.0/Math.pow(s1.nextDouble(), 1.0/alpha) - 1.0;
      v = Math.sqrt(1.0 + 1.0 / (temp*cos*cos));
      S = s3.nextDouble();
      if (S < 0.5)
         return 0.5 - 0.5/v;
      else
         return 0.5 + 0.5/v;
   }


   /**
    * Generates a random number using Devroye's one-liner method with
    *   only one stream <TT>s</TT>.
    *   Restriction:  
    * <SPAN CLASS="MATH"><I>&#945;</I> &gt; 0</SPAN>.
    * 
    */
   public static double nextDouble (RandomStream s, double alpha)   {
      return nextDouble (s, s, s, alpha);
   }


   public double nextDouble() {
      // Generates a random number using Devroye's one liner method
      double cos, temp, v, S;
      cos = Math.cos (TWOPI * stream2.nextDouble());
      temp = 1.0/Math.pow(stream.nextDouble(), afactor) - 1.0;
      v = Math.sqrt(1.0 + 1.0 / (temp*cos*cos));
      S = stream3.nextDouble();
      if (S < 0.5)
         return 0.5 - 0.5/v;
      else
         return 0.5 + 0.5/v;
  }

   /**
    * Returns stream <TT>s2</TT> associated with this object.
    * 
    */
   public RandomStream getStream2() {
      return stream2;
   }


   /**
    * Returns stream <TT>s3</TT> associated with this object.
    * 
    */
   public RandomStream getStream3() {
      return stream3;
   }

}

