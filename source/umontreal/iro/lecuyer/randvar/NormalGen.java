

/*
 * Class:        NormalGen
 * Description:  random variates generator from the normal distribution
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
 * This class implements methods for generating random variates from the
 * <EM>normal</EM> distribution 
 * <SPAN CLASS="MATH"><I>N</I>(<I>&#956;</I>, <I>&#963;</I>)</SPAN>.
 * It has mean <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and variance <SPAN CLASS="MATH"><I>&#963;</I><SUP>2</SUP></SPAN>, where <SPAN CLASS="MATH"><I>&#963;</I> &gt; 0</SPAN>.
 *  Its density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = 1/(2&pi;)<SUP>1/2</SUP><I>&#963;e</I><SUP>(x-<I>&#956;</I>)<SUP>2</SUP>/(2<I>&#963;</I><SUP>2</SUP>)</SUP>
 * </DIV><P></P>
 * The <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 * <P>
 * The following table gives the CPU time needed to generate <SPAN CLASS="MATH">10<SUP>8</SUP></SPAN> standard
 * normal random variates using the different implementations available in SSJ.
 * The first time is for a generator object (non-static method), and
 * the second time is for the static method
 * where no object is created.
 * These tests were made on a machine with processor AMD Athlon 4000, running
 * Red Hat Linux, with clock speed at 2403 MHz. The static method
 *  <TT>nextDouble()</TT> for <TT>NormalBoxMullerGen</TT> and
 *   <TT>NormalPolarGen</TT> uses only one number out of two that are
 *   generated; thus they are twice slower than the non-static method.
 * 
 * <P>
 * <DIV ALIGN="CENTER">
 * <TABLE CELLPADDING=3 BORDER="1">
 * <TR><TD ALIGN="LEFT">Generator</TD>
 * <TD ALIGN="CENTER">time in seconds</TD>
 * <TD ALIGN="CENTER">time in seconds</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">&nbsp;</TD>
 * <TD ALIGN="CENTER">(object)</TD>
 * <TD ALIGN="CENTER">(static)</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalGen</TT></TD>
 * <TD ALIGN="CENTER">7.67</TD>
 * <TD ALIGN="CENTER">7.72</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalACRGen</TT></TD>
 * <TD ALIGN="CENTER">4.71</TD>
 * <TD ALIGN="CENTER">4.76</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalBoxMullerGen</TT></TD>
 * <TD ALIGN="CENTER">16.07</TD>
 * <TD ALIGN="CENTER">31.45</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalPolarGen</TT></TD>
 * <TD ALIGN="CENTER">7.31</TD>
 * <TD ALIGN="CENTER">13.74</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>NormalKindermannRamageGen</TT></TD>
 * <TD ALIGN="CENTER">5.38</TD>
 * <TD ALIGN="CENTER">5.34</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * 
 */
public class NormalGen extends RandomVariateGen  {
   protected double mu;
   protected double sigma = -1.0;


   /**
    * Creates a normal random variate generator with mean <TT>mu</TT>
    *   and standard deviation <TT>sigma</TT>, using stream <TT>s</TT>.
    * 
    */
   public NormalGen (RandomStream s, double mu, double sigma)  {
      super (s, new NormalDist(mu, sigma));
      setParams (mu, sigma);
   }


   /**
    * Creates a standard normal random variate generator with mean
    *   <TT>0</TT> and standard deviation <TT>1</TT>, using stream <TT>s</TT>.
    * 
    */
   public NormalGen (RandomStream s)  {
      this (s, 0.0, 1.0);
   }


   /**
    * Creates a random variate generator for the normal distribution
    *   <TT>dist</TT> and stream <TT>s</TT>.
    * 
    */
   public NormalGen (RandomStream s, NormalDist dist)  {
      super (s, dist);
      if (dist != null)
         setParams (dist.getMu(), dist.getSigma());
   }


   /**
    * Generates a variate from the normal distribution with
    *    parameters <SPAN CLASS="MATH"><I>&#956;</I> =</SPAN>&nbsp;<TT>mu</TT> and <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN>&nbsp;<TT>sigma</TT>, using
    *    stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, double mu, double sigma)  {
      return NormalDist.inverseF (mu, sigma, s.nextDouble());
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> of this object.
    * 
    */
   public double getMu() {
      return mu;
   }


   /**
    * Returns the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    * 
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> of this object.
    * 
    */
   protected void setParams (double mu, double sigma) {
      if (sigma <= 0)
         throw new IllegalArgumentException ("sigma <= 0");
      this.mu = mu;
      this.sigma = sigma;
   }

}
