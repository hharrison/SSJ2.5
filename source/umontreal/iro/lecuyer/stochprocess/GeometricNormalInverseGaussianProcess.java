

/*
 * Class:        GeometricNormalInverseGaussianProcess
 * Description:  
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

package umontreal.iro.lecuyer.stochprocess;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;
import umontreal.iro.lecuyer.randvar.*;



/**
 * .
 * 
 * The geometric normal inverse gaussian (GNIG) process
 * is the exponentiation of a {@link NormalInverseGaussianProcess}:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>S</I>(<I>t</I>) = <I>S</I><SUB>0</SUB>exp[(<I>r</I> - <I>&#969;</I><SUB>RN</SUB>)<I>t</I> + NIG(<I>t</I>;<I>&#945;</I>, <I>&#946;</I>, <I>&#956;</I>, <I>&#948;</I>)],
 * </DIV><P></P>
 * where  <SPAN CLASS="MATH"><I>r</I></SPAN> is the interest rate.
 * It is a strictly positive process, which is useful in finance.
 * There is also a neutral
 * correction in the exponential, 
 * <SPAN CLASS="MATH"><I>&#969;</I><SUB>RN</SUB> = <I>&#956;</I> + <I>&#948;&#947;</I> - <I>&#948;</I>(&alpha;^2-(1+&beta;)^2)<SUP>1/2</SUP></SPAN>,
 * which takes into account the market price of risk.
 * The underlying NIG process must start at zero, NIG<SPAN CLASS="MATH">(<I>t</I><SUB>0</SUB>) = 0</SPAN>
 * and the initial time should also be set to zero, <SPAN CLASS="MATH"><I>t</I><SUB>0</SUB> = 0</SPAN>,
 * both for the NIG and GNIG.
 * 
 */
public class GeometricNormalInverseGaussianProcess extends
                                                   GeometricLevyProcess  {



   /**
    * Constructs a new <TT>GeometricNormalInverseGaussianProcess</TT>.
    * The parameters of the NIG process will be overwritten by the parameters
    * given to the GNIG, with the initial value of the NIG set to 0.
    * The observation times of the NIG will also be changed to
    * those of the GNIG.
    * 
    */
   public GeometricNormalInverseGaussianProcess (
                                        double s0, double muGeom,
                                        double alpha, double beta,
                                        double mu, double delta,
                                        RandomStream streamBrownian,
                                        NormalInverseGaussianProcess nigP) {
        levyProcess = nigP;
        ((NormalInverseGaussianProcess)levyProcess).setParams(0.0, alpha, beta, mu, delta);
        this.x0 = s0;
        this.muGeom = muGeom;
        omegaRiskNeutralCorrection =
            mu + delta*((NormalInverseGaussianProcess)levyProcess).getGamma() -
            delta*Math.sqrt(alpha*alpha - (1.+beta)*(1.+beta));
    }


   /**
    * Constructs a new <TT>GeometricNormalInverseGaussianProcess</TT>.
    * The process <TT>igP</TT>
    * will be used internally by the underlying {@link NormalInverseGaussianProcess}.
    * 
    */
   public GeometricNormalInverseGaussianProcess (
                                        double s0, double muGeom,
                                        double alpha, double beta,
                                        double mu, double delta,
                                        RandomStream streamBrownian,
                                        InverseGaussianProcess igP)  {

        levyProcess = new NormalInverseGaussianProcess (0.0, alpha, beta, mu,
                                                        delta, streamBrownian, igP);
        this.x0 = s0;
        this.muGeom = muGeom;
        omegaRiskNeutralCorrection =
            mu + delta*((NormalInverseGaussianProcess)levyProcess).getGamma() -
            delta*Math.sqrt(alpha*alpha - (1.+beta)*(1.+beta));
    }


   /**
    * Constructs a new <TT>GeometricNormalInverseGaussianProcess</TT>.
    * The drift of the geometric term, <TT>muGeom</TT>, is usually the interest rate <SPAN CLASS="MATH"><I>r</I></SPAN>.
    * <TT>s0</TT> is the initial value of the process and the other four parameters
    * are the parameters of the underlying {@link NormalInverseGaussianProcess} process.
    * 
    */
   public GeometricNormalInverseGaussianProcess (
                                        double s0, double muGeom,
                                        double alpha, double beta,
                                        double mu, double delta,
                                        RandomStream streamBrownian,
                                        RandomStream streamNIG1,
                                        RandomStream streamNIG2,
                                        String igType)  {
        levyProcess = new NormalInverseGaussianProcess (0.0, alpha, beta, mu, delta,
                                                        streamBrownian, streamNIG1,
                                                        streamNIG2, igType);
        this.x0 = s0;
        this.muGeom = muGeom;
        omegaRiskNeutralCorrection =
            mu + delta*((NormalInverseGaussianProcess)levyProcess).getGamma() -
            delta*Math.sqrt(alpha*alpha - (1.+beta)*(1.+beta));
    }


   /**
    * Constructs a new <TT>GeometricNormalInverseGaussianProcess</TT>.
    * The String <TT>igType</TT> corresponds to the type of {@link InverseGaussianProcess}
    * that will be used by the underlying {@link NormalInverseGaussianProcess}.
    * All {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}'s used to generate the underlying
    * {@link NormalInverseGaussianProcess} and its underlying {@link InverseGaussianProcess}
    * are set to the same given <TT>streamAll</TT>.
    * 
    */
   public GeometricNormalInverseGaussianProcess (
                                        double s0, double muGeom,
                                        double alpha, double beta,
                                        double mu, double delta,
                                        RandomStream streamAll,
                                        String igType)  {
        this(s0,muGeom,alpha,beta,mu,delta,streamAll,streamAll,streamAll,igType);
    }


}

