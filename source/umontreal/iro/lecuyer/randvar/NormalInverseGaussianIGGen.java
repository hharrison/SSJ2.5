

/*
 * Class:        NormalInverseGaussianIGGen
 * Description:  normal inverse gaussian random variate generator
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        June 2008

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
 * .
 * 
 * This class implements a  (<SPAN CLASS="MATH"><I>NIG</I></SPAN>) random variate generator by
 * using a normal generator (<SPAN CLASS="MATH"><I>N</I></SPAN>) and an inverse gaussian generator (<SPAN CLASS="MATH"><I>IG</I></SPAN>),
 *  as described in the following
 * <BR>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="nig2"></A>
 * <TABLE CELLPADDING="0" ALIGN="CENTER" WIDTH="100%">
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>Y</I></TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&#8764;</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>IG</I>(<I>&#948;</I>/<I>&#947;</I>, <I>&#948;</I><SUP>2</SUP>)</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * <TR VALIGN="MIDDLE"><TD NOWRAP ALIGN="RIGHT"><I>X</I> | (<I>Y</I> = <I>y</I>)</TD>
 * <TD WIDTH="10" ALIGN="CENTER" NOWRAP>&#8764;</TD>
 * <TD ALIGN="LEFT" NOWRAP><I>N</I>(<I>&#956;</I> + <I>&#946;y</I>, <I>y</I>).</TD>
 * <TD CLASS="eqno" WIDTH=10 ALIGN="RIGHT">
 * &nbsp;</TD></TR>
 * </TABLE></DIV>
 * <BR CLEAR="ALL">
 * 
 * The normal 
 * <SPAN CLASS="MATH"><I>N</I>(<I>&#956;</I>, <I>&#963;</I><SUP>2</SUP>)</SPAN> has mean <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and variance <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP></SPAN>, while
 * the inverse gaussian has the parametrization described in{@link umontreal.iro.lecuyer.randvar.InverseGaussianGen InverseGaussianGen}.
 * If 
 * <SPAN CLASS="MATH"><I>&#947;</I> = (&alpha;^2 - &beta;^2)<SUP>1/2</SUP></SPAN> with 
 * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;| <I>&#946;</I>| &lt; <I>&#945;</I></SPAN> and
 * <SPAN CLASS="MATH"><I>&#948;</I> &gt; 0</SPAN>, then 
 * <SPAN CLASS="MATH"><I>X</I>&#8764;<I>NIG</I>(<I>&#945;</I>, <I>&#946;</I>, <I>&#956;</I>, <I>&#948;</I>)</SPAN>. 
 * 
 */
public class NormalInverseGaussianIGGen extends NormalInverseGaussianGen  {
   private NormalGen genN;
   private InverseGaussianGen genIG;


   /**
    * Creates a  random variate generator with parameters
    *   <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>,
    *  using generators  <TT>ig</TT> and <TT>ng</TT>, as described
    * above. The parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> are included in generator <TT>ig</TT>.
    * 
    */
   public NormalInverseGaussianIGGen (InverseGaussianGen ig, NormalGen ng,
                                      double beta, double mu)  {
      super (null, null);
      setParams (ig, ng, beta, mu);
   }


   /**
    * Generates a new variate from the  distribution with 
    * parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN>, <SPAN CLASS="MATH"><I>&#946;</I></SPAN> = <TT>beta</TT>, <SPAN CLASS="MATH"><I>&#956;</I></SPAN> = <TT>mu</TT> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN>, 
    * using generators <TT>ig</TT> and <TT>ng</TT>, as described in eq..
    * The parameters <SPAN CLASS="MATH"><I>&#945;</I></SPAN> and <SPAN CLASS="MATH"><I>&#948;</I></SPAN> are included in generator <TT>ig</TT>.
    * 
    */
   public static double nextDouble (InverseGaussianGen ig, NormalGen ng,
                                    double beta, double mu)  {
      return mynig (ig, ng, beta, mu);
   }
 

   public double nextDouble() {
      return mynig (genIG, genN, beta, mu);
   }


// >>>>>>>>>>>>>>>>>>>>  P R I V A T E     M E T H O D S   <<<<<<<<<<<<<<<<<<<

   private static double mynig (InverseGaussianGen ig, NormalGen ng,
                                double beta, double mu) {

      double y = ig.nextDouble ();
      double x = mu + beta*y + Math.sqrt(y)*ng.nextDouble ();
      return x;
   }


   protected void setParams (InverseGaussianGen ig, NormalGen ng,
                             double beta, double mu) {
      delta = Math.sqrt(ig.getLambda());
      gamma = delta / ig.getMu();
      alpha = Math.sqrt(gamma*gamma + beta*beta);
      setParams (alpha, beta, mu, delta);
      this.genN = ng;
      this.genIG = ig;
   }
}
