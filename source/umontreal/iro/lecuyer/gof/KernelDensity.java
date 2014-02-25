

/*
 * Class:        KernelDensity
 * Description:  Kernel density estimators
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

package umontreal.iro.lecuyer.gof;
   import umontreal.iro.lecuyer.probdist.*;

import umontreal.iro.lecuyer.randvar.KernelDensityGen;


/**
 * This class provides methods to compute a kernel density estimator from a set
 * of <SPAN CLASS="MATH"><I>n</I></SPAN> individual observations 
 * <SPAN CLASS="MATH"><I>x</I><SUB>0</SUB>,&#8230;, <I>x</I><SUB>n-1</SUB></SPAN>, and returns its value
 * at <SPAN CLASS="MATH"><I>m</I></SPAN> selected points. For details on how the kernel density is defined,
 * and how to select the kernel and the bandwidth <SPAN CLASS="MATH"><I>h</I></SPAN>,
 * see the documentation of class 
 * {@link umontreal.iro.lecuyer.randvar.KernelDensityGen KernelDensityGen}
 * in package <TT>randvar</TT>.
 * 
 */
public class KernelDensity  {

   private static double estimate (EmpiricalDist dist,
                                   ContinuousDistribution kern,
                                   double h, double y) {
      // Computes and returns the kernel density estimate at $y$, where the 
      // kernel is the density kern.density(x), and the bandwidth is $h$.
      double z;
      double a = kern.getXinf();       // lower limit of density
      double b = kern.getXsup();       // upper limit of density
      double sum = 0;
      int n = dist.getN();
      for (int i = 0; i < n; i++) {
         z = (y - dist.getObs(i))/h;
         if ((z >= a) && (z <= b))
            sum += kern.density(z);
      }

      sum /= (h*n);
      return sum;
   }


   /**
    * Given the empirical distribution <TT>dist</TT>, this method computes the 
    * kernel density estimate at each of the <SPAN CLASS="MATH"><I>m</I></SPAN> points <TT>Y[<SPAN CLASS="MATH"><I>j</I></SPAN>]</TT>,
    *  
    * <SPAN CLASS="MATH"><I>j</I> = 0, 1,&#8230;,(<I>m</I> - 1)</SPAN>, where <SPAN CLASS="MATH"><I>m</I></SPAN> is the length of <TT>Y</TT>, the kernel
    *  is <TT>kern.density(x)</TT>,
    *  and the bandwidth is <SPAN CLASS="MATH"><I>h</I></SPAN>. Returns the estimates as an array of <SPAN CLASS="MATH"><I>m</I></SPAN> values.
    * 
    */
   public static double[] computeDensity (EmpiricalDist dist,
                                          ContinuousDistribution kern,
                                          double h, double[] Y) {
      int m = Y.length;
      double[] u = new double[m];
      for (int j = 0; j < m; j++)
         u[j] = estimate (dist, kern, h, Y[j]);
      return u;
   }


   /**
    * Similar to method 
    * {@link #computeDensity((EmpiricalDist,ContinuousDistribution,double,double[])) computeDensity} ,
    * but the bandwidth <SPAN CLASS="MATH"><I>h</I></SPAN> is obtained from the method
    * {@link umontreal.iro.lecuyer.randvar.KernelDensityGen#getBaseBandwidth(EmpiricalDist) KernelDensityGen.getBaseBandwidth}<TT>(dist)</TT> in package <TT>randvar</TT>.
    * 
    */
   public static double[] computeDensity (EmpiricalDist dist,
                                          ContinuousDistribution kern,
                                          double[] Y) {
      double h = KernelDensityGen.getBaseBandwidth(dist);
      return computeDensity (dist, kern, h, Y);
   }

}
