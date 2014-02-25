

/*
 * Class:        DirichletGen
 * Description:  multivariate generator for a Dirichlet} distribution
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

package umontreal.iro.lecuyer.randvarmulti;

import umontreal.iro.lecuyer.probdist.GammaDist;
import umontreal.iro.lecuyer.randvar.RandomVariateGen;
import umontreal.iro.lecuyer.randvar.GammaAcceptanceRejectionGen;
import umontreal.iro.lecuyer.rng.RandomStream;



/**
 * Extends {@link RandomMultivariateGen} for a
 * <SPAN  CLASS="textit">Dirichlet</SPAN> distribution.  This distribution uses the
 * parameters
 * 
 * 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>1</SUB>,..., <I>&#945;</I><SUB>k</SUB></SPAN>, and has density
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>k</SUB>) = <I>&#915;</I>(<I>&#945;</I><SUB>0</SUB>)&prod;<SUB>i=1</SUB><SUP>k</SUP><I>x</I><SUB>i</SUB><SUP><I>&#945;</I><SUB>i</SUB>-1</SUP>&nbsp;/&nbsp;&prod;<SUB>i=1</SUB><SUP>k</SUP><I>&#915;</I>(<I>&#945;</I><SUB>i</SUB>))
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>0</SUB> = &sum;<SUB>i=1</SUB><SUP>k</SUP><I>&#945;</I><SUB>i</SUB></SPAN>.
 * 
 * <P>
 * Here, the successive coordinates of the Dirichlet vector are generated
 * 
 * via the class
 * {@link umontreal.iro.lecuyer.randvar.GammaAcceptanceRejectionGen GammaAcceptanceRejectionGen}
 * in package <TT>randvar</TT>, using the same stream for all the uniforms.
 * 
 */
public class DirichletGen extends RandomMultivariateGen {
   private GammaAcceptanceRejectionGen[] ggens;


   /**
    * Constructs a new Dirichlet
    *  generator with parameters 
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>i+1</SUB> =</SPAN>&nbsp;<TT>alphas[i]</TT>,
    *  for 
    * <SPAN CLASS="MATH"><I>i</I> = 0,&#8230;, <I>k</I> - 1</SPAN>, and the stream <TT>stream</TT>.
    * 
    * @param stream the random number stream used to generate uniforms.
    * 
    *    @param alphas the <SPAN CLASS="MATH"><I>&#945;</I><SUB>i</SUB></SPAN> parameters of the generated distribution.
    * 
    *    @exception IllegalArgumentException if one <SPAN CLASS="MATH"><I>&#945;</I><SUB>k</SUB></SPAN> is negative or 0.
    * 
    *    @exception NullPointerException if any argument is <TT>null</TT>.
    * 
    */
   public DirichletGen (RandomStream stream, double[] alphas) {
      if (stream == null)
         throw new NullPointerException ("stream is null");
      this.stream = stream;
      dimension = alphas.length;
      ggens = new GammaAcceptanceRejectionGen[alphas.length];
      for (int k = 0; k < alphas.length; k++)
         ggens[k] = new GammaAcceptanceRejectionGen
            (stream, new GammaDist (alphas[k], 1.0/2.0));
   }


   /**
    * Returns the 
    * <SPAN CLASS="MATH"><I>&#945;</I><SUB>i+1</SUB></SPAN> parameter for this
    *  Dirichlet generator.
    * 
    * @param i the index of the parameter.
    * 
    *    @return the value of the parameter.
    *    @exception ArrayIndexOutOfBoundsException if <TT>i</TT> is
    *     negative or greater than or equal to {@link #getDimension(()) getDimension}.
    * 
    * 
    */
   public double getAlpha (int i) {
      return ((GammaDist)ggens[i].getDistribution()).getAlpha();
   }


   /**
    * Generates a new point from the Dirichlet distribution with
    *  parameters <TT>alphas</TT>, using the stream <TT>stream</TT>.
    *  The generated values are placed into <TT>p</TT>.
    * 
    * @param stream the random number stream used to generate the uniforms.
    * 
    *    @param alphas the <SPAN CLASS="MATH"><I>&#945;</I><SUB>i</SUB></SPAN> parameters of the distribution, for
    *     
    * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>k</I></SPAN>.
    * 
    *    @param p the array to be filled with the generated point.
    * 
    * 
    */
   public static void nextPoint (RandomStream stream, double[] alphas,
                                 double[] p) {
      double total = 0;
      for (int i = 0; i < alphas.length; i++) {
         p[i] = GammaAcceptanceRejectionGen.nextDouble
            (stream, stream, alphas[i], 1.0/2.0);
         total += p[i];
      }
      for (int i = 0; i < alphas.length; i++)
         p[i] /= total;
   }


   /**
    * Generates a point from the Dirichlet distribution.
    * 
    * @param p the array to be filled with the generated point.
    * 
    */
   public void nextPoint (double[] p) {
      int n = ggens.length;
      double total = 0;
      for (int i = 0; i < n; i++) {
         p[i] = ggens[i].nextDouble();
         total += p[i];
      }
      for (int i = 0; i < n; i++)
         p[i] /= total;
   }
}
