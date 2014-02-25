

/*
 * Class:        KernelDensityGen
 * Description:  random variate generators for distributions obtained via
                 kernel density estimation methods
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
import umontreal.iro.lecuyer.probdist.*;
import umontreal.iro.lecuyer.rng.RandomStream;

/**
 * This class implements random variate generators for distributions
 * obtained via <SPAN  CLASS="textit">kernel density estimation</SPAN> methods from a set of
 * <SPAN CLASS="MATH"><I>n</I></SPAN> individual observations 
 * <SPAN CLASS="MATH"><I>x</I><SUB>1</SUB>,..., <I>x</I><SUB>n</SUB></SPAN>. 
 * The basic idea is to center a copy of the same symmetric density 
 * at each observation and take an equally weighted mixture of the <SPAN CLASS="MATH"><I>n</I></SPAN>
 * copies as an estimator of the density from which the observations come.
 * The resulting kernel density has the general form
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I><SUB>n</SUB>(<I>x</I>) = (1/<I>nh</I>)&sum;<SUB>i=1</SUB><SUP>n</SUP><I>k</I>((<I>x</I> - <I>x</I><SUB>i</SUB>)/<I>h</I>),
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>k</I></SPAN> is a fixed pre-selected density called the <EM>kernel</EM> 
 * and <SPAN CLASS="MATH"><I>h</I></SPAN> is a positive constant called the <SPAN  CLASS="textit">bandwidth</SPAN> or
 * <SPAN  CLASS="textit">smoothing factor</SPAN>.
 * A difficult practical issue is the selection of <SPAN CLASS="MATH"><I>k</I></SPAN> and <SPAN CLASS="MATH"><I>h</I></SPAN>.
 * Several approaches have been proposed for that; see, e.g.,.
 * 
 * <P>
 * The constructor of a generator from a kernel density requires a
 * random stream <SPAN CLASS="MATH"><I>s</I></SPAN>, the <SPAN CLASS="MATH"><I>n</I></SPAN> observations in the form of an empirical
 * distribution, a random variate generator for the kernel density <SPAN CLASS="MATH"><I>k</I></SPAN>,
 * and the value of the bandwidth <SPAN CLASS="MATH"><I>h</I></SPAN>.
 * The random variates are then generated as follows:
 * select an observation <SPAN CLASS="MATH"><I>x</I><SUB>I</SUB></SPAN> at random, by inversion, using stream <SPAN CLASS="MATH"><I>s</I></SPAN>,
 * then generate random variate <SPAN CLASS="MATH"><I>Y</I></SPAN> with the generator provided for
 * the density <SPAN CLASS="MATH"><I>k</I></SPAN>, and return <SPAN CLASS="MATH"><I>x</I><SUB>I</SUB> + <I>hY</I></SPAN>.
 * 
 * <P>
 * A simple formula for the bandwidth, suggested in,
 * is 
 * <SPAN CLASS="MATH"><I>h</I> = <I>&#945;</I><SUB>k</SUB><I>h</I><SUB>0</SUB></SPAN>, where
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:bandwidth0"></A>
 * <I>h</I><SUB>0</SUB> = 1.36374min(<I>s</I><SUB>n</SUB>, <I>q</I>/1.34)<I>n</I><SUP>-1/5</SUP>,
 * </DIV><P></P>
 * <SPAN CLASS="MATH"><I>s</I><SUB>n</SUB></SPAN> and <SPAN CLASS="MATH"><I>q</I></SPAN> are the empirical standard deviation and the 
 * interquartile range of the <SPAN CLASS="MATH"><I>n</I></SPAN> observations,
 * and <SPAN CLASS="MATH"><I>&#945;</I><SUB>k</SUB></SPAN> is a constant that depends on the type of kernel <SPAN CLASS="MATH"><I>k</I></SPAN>.
 * It is defined by
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>&#945;</I><SUB>k</SUB> = (<I>&#963;</I><SUB>k</SUB><SUP>-4</SUP>&int;<SUB>-&#8734;</SUB><SUP>&#8734;</SUP><I>k</I>(<I>x</I>)<I>dx</I>)<SUP>1/5</SUP>
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#963;</I><SUB>k</SUB></SPAN> is the standard deviation of the density <SPAN CLASS="MATH"><I>k</I></SPAN>.
 * The static method {@link #getBaseBandwidth getBaseBandwidth} permits one to compute <SPAN CLASS="MATH"><I>h</I><SUB>0</SUB></SPAN> 
 * for a given empirical distribution.
 * 
 * <P>
 * <BR><P></P>
 * <DIV ALIGN="CENTER">
 * <DIV ALIGN="CENTER">
 * 
 * </DIV> 
 * <A NAME="tab:kernels"></A><A NAME="34"></A>
 * <TABLE CELLPADDING=3 BORDER="1">
 * <CAPTION><STRONG>Table:</STRONG>
 * Some suggested kernels</CAPTION>
 * <TR><TD ALIGN="LEFT">name</TD>
 * <TD ALIGN="LEFT">constructor</TD>
 * <TD ALIGN="CENTER"><SPAN CLASS="MATH"><I>&#945;</I><SUB>k</SUB></SPAN></TD>
 * <TD ALIGN="CENTER">
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>k</SUB><SUP>2</SUP></SPAN></TD>
 * <TD ALIGN="CENTER">efficiency</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">Epanechnikov</TD>
 * <TD ALIGN="LEFT"><TT>BetaSymmetricalDist(2, -1, 1)</TT></TD>
 * <TD ALIGN="CENTER">1.7188</TD>
 * <TD ALIGN="CENTER">1/5</TD>
 * <TD ALIGN="CENTER">1.000</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">triangular</TD>
 * <TD ALIGN="LEFT"><TT>TriangularDist(-1, 1, 0)</TT></TD>
 * <TD ALIGN="CENTER">1.8882</TD>
 * <TD ALIGN="CENTER">1/6</TD>
 * <TD ALIGN="CENTER">0.986</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">Gaussian</TD>
 * <TD ALIGN="LEFT"><TT>NormalDist()</TT></TD>
 * <TD ALIGN="CENTER">0.7764</TD>
 * <TD ALIGN="CENTER">1</TD>
 * <TD ALIGN="CENTER">0.951</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">boxcar</TD>
 * <TD ALIGN="LEFT"><TT>UniformDist(-1, 1)</TT></TD>
 * <TD ALIGN="CENTER">1.3510</TD>
 * <TD ALIGN="CENTER">1/3</TD>
 * <TD ALIGN="CENTER">0.930</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">logistic</TD>
 * <TD ALIGN="LEFT"><TT>LogisticDist()</TT></TD>
 * <TD ALIGN="CENTER">0.4340</TD>
 * <TD ALIGN="CENTER">3.2899</TD>
 * <TD ALIGN="CENTER">0.888</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT">Student-t(3)</TD>
 * <TD ALIGN="LEFT"><TT>StudentDist(3)</TT></TD>
 * <TD ALIGN="CENTER">0.4802</TD>
 * <TD ALIGN="CENTER">3</TD>
 * <TD ALIGN="CENTER">0.674</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * <BR>
 * 
 * <P>
 * Table&nbsp; gives the precomputed values of <SPAN CLASS="MATH"><I>&#963;</I><SUB>k</SUB></SPAN> and
 * <SPAN CLASS="MATH"><I>&#945;</I><SUB>k</SUB></SPAN> for selected (popular) kernels.
 * The values are taken from.
 * The second column gives the name of a function (in this package)
 * that constructs the corresponding distribution.
 * The <SPAN  CLASS="textit">efficiency</SPAN> of a kernel is defined as the ratio of its
 * mean integrated square error over that of the Epanechnikov kernel,
 * which has optimal efficiency and corresponds to the beta distribution
 * with parameters <SPAN CLASS="MATH">(2, 2)</SPAN> over the interval <SPAN CLASS="MATH">(- 1, 1)</SPAN>.
 * 
 */
public class KernelDensityGen extends RandomVariateGen {

   protected RandomVariateGen kernelGen;
   protected double bandwidth;
   protected boolean positive;   // If we want positive reflection.



   /**
    * Creates a new generator for a kernel density estimated 
    *     from the observations given by the empirical distribution <TT>dist</TT>,
    *     using stream <TT>s</TT> to select the observations,
    *     generator <TT>kGen</TT> to generate the added noise from the kernel
    *     density, and bandwidth <TT>h</TT>.
    * 
    */
   public KernelDensityGen (RandomStream s, EmpiricalDist dist,
                            RandomVariateGen kGen, double h) {
      super (s, dist);
      if (h < 0.0)
         throw new IllegalArgumentException ("h < 0");
      if (kGen == null)
         throw new IllegalArgumentException ("kGen == null");
      kernelGen = kGen;
      bandwidth = h;
   }


   /**
    * This constructor uses a gaussian kernel and the default 
    *     bandwidth 
    * <SPAN CLASS="MATH"><I>h</I> = <I>&#945;</I><SUB>k</SUB><I>h</I><SUB>0</SUB></SPAN> with the <SPAN CLASS="MATH"><I>&#945;</I><SUB>k</SUB></SPAN> 
    *     suggested in Table&nbsp; for the gaussian distribution.
    *     This kernel has an efficiency of 0.951.
    * 
    */
   public KernelDensityGen (RandomStream s, EmpiricalDist dist,
                            NormalGen kGen) {
      this (s, dist, kGen, 0.77639 * getBaseBandwidth (dist));
   }


   /**
    * Computes and returns the value of <SPAN CLASS="MATH"><I>h</I><SUB>0</SUB></SPAN> in.
    * 
    */
   public static double getBaseBandwidth (EmpiricalDist dist) {
      double r = dist.getInterQuartileRange() / 1.34;
      double sigma = dist.getSampleStandardDeviation();
      if (sigma < r) r = sigma;
      return (1.36374 * r / Math.exp (0.2 * Math.log (dist.getN())));
   }


   /**
    * Sets the bandwidth to <TT>h</TT>.
    * 
    */
   public void setBandwidth (double h) {
      if (h < 0)
         throw new IllegalArgumentException ("h < 0");
      bandwidth = h;
   }


   /**
    * After this method is called with <TT>true</TT>,
    *   the generator will produce only positive values, by using 
    *   the <SPAN  CLASS="textit">reflection method</SPAN>: replace all negative values by their
    *   <SPAN  CLASS="textit">absolute values</SPAN>.
    *   That is, {@link #nextDouble nextDouble} will return <SPAN CLASS="MATH">| <I>x</I>|</SPAN> if <SPAN CLASS="MATH"><I>x</I></SPAN> is the 
    *   generated variate.  The mecanism is disabled when the method is
    *   called with <TT>false</TT>.
    * 
    */
   public void setPositiveReflection (boolean reflect) {
      positive = reflect;
   }


   public double nextDouble() {
      double x = (dist.inverseF (stream.nextDouble())
                  + bandwidth * kernelGen.nextDouble());
      if (positive)
         return Math.abs (x);
      else
         return x;
   }
}
