

/*
 * Class:        KernelDensityVarCorrectGen
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
 * This class is a variant of {@link KernelDensityGen}, but with
 * a rescaling of the empirical distribution so that the variance
 * of the density used to generate the random variates is equal
 * to the empirical variance,
 *  as suggested by Silverman.
 * 
 * <P>
 * Let <SPAN CLASS="MATH">bar(x)<SUB>n</SUB></SPAN> and <SPAN CLASS="MATH"><I>s</I><SUB>n</SUB><SUP>2</SUP></SPAN> be the sample mean and sample variance 
 * of the observations.
 * The distance between each generated random variate and the 
 * sample mean <SPAN CLASS="MATH">bar(x)<SUB>n</SUB></SPAN> is multiplied by the correcting factor
 * 
 * <SPAN CLASS="MATH">1/<I>&#963;</I><SUB>e</SUB></SPAN>, where 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>e</SUB><SUP>2</SUP> = 1 + (<I>h&#963;</I><SUB>k</SUB>/<I>s</I><SUB>n</SUB>)<SUP>2</SUP></SPAN>.
 * The constant 
 * <SPAN CLASS="MATH"><I>&#963;</I><SUB>k</SUB><SUP>2</SUP></SPAN> must be passed to the constructor.
 * Its value can be found in 
 * the Table in {@link KernelDensityGen} for some popular
 * kernels.
 * 
 */
public class KernelDensityVarCorrectGen extends KernelDensityGen {

   protected double sigmak2;   // Value of sigma_k^2.
   protected double mean;      // Sample mean of the observations.
   protected double invSigmae; // 1 / sigma_e.



   /**
    * Creates a new generator for a kernel density estimated 
    *     from the observations given by the empirical distribution <TT>dist</TT>,
    *     using stream <TT>s</TT> to select the observations,
    *     generator <TT>kGen</TT> to generate the added noise from the kernel
    *     density, bandwidth <TT>h</TT>, and 
    * <SPAN CLASS="MATH"><I>&#963;</I><SUB>k</SUB><SUP>2</SUP> =</SPAN> <TT>sigmak2</TT> used for
    *     the variance correction.
    * 
    */
   public KernelDensityVarCorrectGen (RandomStream s, EmpiricalDist dist,
                                      RandomVariateGen kGen, double h,
                                      double sigmak2) {
      super (s, dist, kGen, h);
      this.sigmak2 = sigmak2;
      mean = dist.getSampleMean();
      double var = dist.getSampleVariance();
      invSigmae = 1.0 / Math.sqrt (1.0 + h * h * sigmak2 / var);
   }


   /**
    * This constructor uses a gaussian kernel and the default 
    *     bandwidth suggested in Table&nbsp; for the gaussian 
    *     distribution.
    * 
    */
   public KernelDensityVarCorrectGen (RandomStream s, EmpiricalDist dist,
                                      NormalGen kGen) {
      this (s, dist, kGen, 0.77639 * getBaseBandwidth (dist), 1.0);
   }


   public void setBandwidth (double h) {
      if (h < 0)
         throw new IllegalArgumentException ("h < 0");
      bandwidth = h;
      double var = ((EmpiricalDist) dist).getSampleVariance();
      invSigmae = 1.0 / Math.sqrt (1.0 + h * h * sigmak2 / var);
   }

   public double nextDouble() {
      double x = mean + invSigmae * (dist.inverseF (stream.nextDouble())
                  - mean + bandwidth * kernelGen.nextDouble());
      if (positive)
         return Math.abs (x);
      else
         return x;
   }
}
