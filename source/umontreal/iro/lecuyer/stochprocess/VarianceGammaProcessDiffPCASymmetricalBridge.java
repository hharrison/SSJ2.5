

/*
 * Class:        VarianceGammaProcessDiffPCASymmetricalBridge
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @authors       Jean-Sébastien Parent-Chartier and Maxime Dion 
 * @since        2008

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

package umontreal.iro.lecuyer.stochprocess;
import umontreal.iro.lecuyer.rng.*;



/**
 * Same as {@link VarianceGammaProcessDiff}, but the two
 * inner {@link GammaProcess}'es are of the PCASymmetricalBridge type.
 * Also, <TT>generatePath(double[] uniform01)</TT> distributes the
 * lowest coordinates uniforms to the inner
 * GammaProcessPCA according to their eigenvalues.
 * 
 */
public class VarianceGammaProcessDiffPCASymmetricalBridge extends 
             VarianceGammaProcessDiffPCA  {






   /**
    * Constructs a new 
    * {@link VarianceGammaProcessDiffPCASymmetricalBridge} with 
    * parameters  
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> 
    * and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.  There is only
    * one {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} here which is
    * used for the two inner {@link GammaProcessPCASymmetricalBridge}'s.  The other
    * parameters are set as in {@link VarianceGammaProcessDiff}.
    * 
    */
public VarianceGammaProcessDiffPCASymmetricalBridge (
                                               double s0, double theta, 
                                               double sigma, double nu, 
                                               RandomStream stream)  {
    super(s0, theta, sigma, nu, new GammaProcessPCASymmetricalBridge (0.0, 1.0, 1.0, stream),
	  new GammaProcessPCASymmetricalBridge (0.0, 1.0, 1.0, stream));
    // Params mu, nu of the 2 gamma processes are redefined in init()
    // which will be called after a call to 'setObservTimes'
}


}
