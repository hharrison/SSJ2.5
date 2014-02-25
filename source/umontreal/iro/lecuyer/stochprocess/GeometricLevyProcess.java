

/*
 * Class:        GeometricLevyProcess
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
 * Abstract class used as a parent class for the exponentiation
 * of a L&#233;vy process <SPAN CLASS="MATH"><I>X</I>(<I>t</I>)</SPAN>:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>S</I>(<I>t</I>) = <I>S</I>(0)exp(<I>X</I>(<I>t</I>) + (<I>r</I> - <I>&#969;</I><SUB>RN</SUB>)<I>t</I>).
 * </DIV><P></P>
 * The interest is here denoted <SPAN CLASS="MATH"><I>r</I></SPAN> and is refered to
 * as <TT>muGeom</TT> in the class below.
 * The risk neutral correction is given by 
 * <SPAN CLASS="MATH"><I>&#969;</I><SUB>RN</SUB></SPAN>
 * and takes into account risk aversion in the pricing of
 * assets; its value depends on the specific L&#233;vy process
 * that is used.
 * 
 * <P>
 * {@link GeometricNormalInverseGaussianProcess} is
 * implemented as a child of this class
 * and so could {@link GeometricVarianceGammaProcess}
 * and {@link GeometricBrownianMotion}.
 * 
 */
public abstract class GeometricLevyProcess extends StochasticProcess  {

    protected StochasticProcess levyProcess;
    protected double omegaRiskNeutralCorrection;
    protected double muGeom;  // usually the interest rate
    protected double[] muGeomRNdt; // risk neutral corrected 
    protected double[] muGeomRNdT; // risk neutral corrected, from time t0.

    protected void init()
    {
        super.init();
        if (observationTimesSet)
        {
            // Telling the variance gamma proc. about the observ. times
            levyProcess.setObservationTimes (t, d);

            // We need to know in which order the observations are generated
            this.observationIndexFromCounter 
                = levyProcess.getArrayMappingCounterToIndex();

            muGeomRNdt = new double[d];
            for (int i = 0; i < d; i++){
                muGeomRNdt[i] = (muGeom-omegaRiskNeutralCorrection) * 
                                (t[i+1] - t[i]);
            }
            muGeomRNdT = new double[d+1];
            for (int i = 0; i <= d; i++){
                muGeomRNdT[i] = (muGeom-omegaRiskNeutralCorrection) * 
                                (t[i] - t[0]);                
            }
        }
    }



   /**
    * Generates a path.
    * 
    */
   public double[] generatePath()  {
        double s = x0;
        resetStartProcess();
        double[] arithmPath = levyProcess.generatePath();
        for (int i = 0; i < d; i++)
        {
            s *= Math.exp (muGeomRNdt[i] + arithmPath[i+1] - arithmPath[i]);
            path[i+1] = s;
        }
        observationIndex = d;
        return path;
    }


   /**
    * Returns the next observation. 
    * It will also work on a L&#233;vy process which is sampled using
    * the bridge order, but it will return the observations in 
    * the bridge order.
    * If the underlying L&#233;vy process is of the PCA type, this
    * method is not usable.
    * 
    */
   public double nextObservation()  {
       double levy = levyProcess.nextObservation();
       observationIndex = levyProcess.getCurrentObservationIndex();
       path[observationIndex] = x0 * 
             Math.exp( muGeomRNdT[observationIndex] + levy );
       return path[observationIndex];
    }


   /**
    * Resets the step counter of the geometric process and 
    * the underlying L&#233;vy process to the start value.
    * 
    */
   public void resetStartProcess()  {
        super.init();
        levyProcess.resetStartProcess();
    }


   /**
    * Sets the observation times on the geometric process
    * and the underlying L&#233;vy process.
    * 
    */
   public void setObservationTimes(double[] time, int d)  {
        super.setObservationTimes(time, d);
        levyProcess.setObservationTimes(time, d);
    }


   /**
    * Returns the risk neutral correction.
    * 
    */
   public double getOmega()  {
        return omegaRiskNeutralCorrection;
    }


   /**
    * Returns the geometric drift parameter,
    * which is usually the interest rate, <SPAN CLASS="MATH"><I>r</I></SPAN>.
    * 
    */
   public double getMuGeom()  {
        return muGeom;
    }


   /**
    * Sets the drift parameter (interest rate) of the geometric term.
    * 
    */
   public void setMuGeom (double muGeom)  {
        this.muGeom = muGeom;
    }


   /**
    * Returns the L&#233;vy process.
    * 
    */
   public StochasticProcess getLevyProcess()  {
        return levyProcess;
    }


   /**
    * Changes the value of 
    * <SPAN CLASS="MATH"><I>&#969;</I><SUB>RN</SUB></SPAN>.
    * There should usually be no need to redefine the risk neutral
    * correction from the value set by the constructor.  However it
    * is sometimes not unique, e.g. in {@link GeometricNormalInverseGaussianProcess}.
    * 
    */
   public void resetRiskNeutralCorrection (double omegaRN)  {
       omegaRiskNeutralCorrection = omegaRN;
       init();
    }


   /**
    * Returns the stream from the underlying L&#233;vy process.
    * If the underlying L&#233;vy process has multiple streams, it returns
    * what the <TT>getStream()</TT> method of that process was made to return.
    * 
    */
   public RandomStream getStream()  {
        return levyProcess.getStream();
    }


   /**
    * Resets the stream in the underlying L&#233;vy process.
    * If the underlying L&#233;vy process has multiple streams, it sets
    * the streams on this process in the same way as <TT>setStream()</TT>
    * for that process.
    * 
    */
   public void setStream (RandomStream stream)  {
        levyProcess.setStream(stream);
    }


}

