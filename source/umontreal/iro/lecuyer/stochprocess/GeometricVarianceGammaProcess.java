

/*
 * Class:        GeometricVarianceGammaProcess
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Pierre Tremblay
 * @since        July 2003

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
 * This class represents a <SPAN  CLASS="textit">geometric variance gamma</SPAN> process <SPAN CLASS="MATH"><I>S</I>(<I>t</I>)</SPAN>
 * (see). This stochastic process is defined by the
 * equation
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="GeoVGeqn"></A>
 * <I>S</I>(<I>t</I>) = <I>S</I>(0) exp(<I>&#956;t</I> + <I>X</I>(<I>t</I>;<I>&#963;</I>, <I>&#957;</I>, <I>&#952;</I>) + <I>&#969;t</I>),
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>X</I></SPAN> is a variance gamma process and
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="omegaEqn"></A>
 * <I>&#969;</I> = (1/<I>&#957;</I>) ln(1 - <I>&#952;&#957;</I> - <I>&#963;</I><SUP>2</SUP><I>&#957;</I>/2).
 * </DIV><P></P>
 * 
 */
public class GeometricVarianceGammaProcess extends StochasticProcess  {
    protected VarianceGammaProcess vargamma;
    protected double        theta,
                            nu,
                            mu,
                            sigma,
                            omega,
                            muPlusOmega;
    protected double[]      mudt;



   /**
    * Constructs a new <TT>GeometricVarianceGammaProcess</TT> with parameters
    * 
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN>,
    * 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN> and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.
    * The <TT>stream</TT> is  used to generate the {@link VarianceGammaProcess} object used to implement
    * <SPAN CLASS="MATH"><I>X</I></SPAN> in.
    * 
    */
   public GeometricVarianceGammaProcess (double s0, double theta,
                                         double sigma, double nu,
                                         double mu, RandomStream stream)  {
        vargamma = new VarianceGammaProcess (0.0, theta, sigma, nu, stream);
        setParams (s0, theta, sigma, nu, mu);
    }


   /**
    * Constructs a new <TT>GeometricVarianceGammaProcess</TT>.
    * The parameters 
    * <SPAN CLASS="MATH"><I>&#952;</I>, <I>&#963;</I>, <I>&#957;</I></SPAN> are set to the parameters of the
    *  {@link VarianceGammaProcess} <TT>vargamma</TT>. The parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>
    * is set to <TT>mu</TT> and the initial values 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.
    * 
    */
   public GeometricVarianceGammaProcess (double s0, double mu,
                                         VarianceGammaProcess vargamma)
  {
        this.vargamma = vargamma;
        setParams (s0, vargamma.getTheta (), vargamma.getSigma (),
                   vargamma.getNu (), mu);
    }


   public double nextObservation() {
        double nextX  = vargamma.nextObservation();
        observationIndex = vargamma.getCurrentObservationIndex();
        // Could be different than simply 'observationIndex++' because of the
        // possibility of Gamma/Brownian bridge
        observationCounter++;

        double s = x0 * Math.exp (muPlusOmega * (t[observationIndex] - t[0])
                                  + nextX);
        path[observationIndex] = s;
        return s;
    }

   public double[] generatePath() {
        double s = x0;
        resetStartProcess();
        double[] vgpath = vargamma.generatePath();
        for (int i = 0; i < d; i++) {
            s *= Math.exp (mudt[i] + vgpath[i+1] - vgpath[i]);
            path[i+1] = s;
        }
        observationIndex = d;
        observationCounter++;
        return path;
    }
    // allows the user to create a path by specifiying the uniform random numbers to be used
   public double[] generatePath (double[] uniform01) {
        double s = x0;
        resetStartProcess();

        double[] vgpath = vargamma.generatePath(uniform01);
        for (int i = 0; i < d; i++) {
            s *= Math.exp (mudt[i] + vgpath[i+1] - vgpath[i]);
            path[i+1] = s;
        }
        observationIndex = d;
        observationCounter++;
        return path;
    }


    // method not verified by JS...  old stuff
   public double getCurrentUpperBound()  {
        // Find index for last observation generated (chronologically)
        int j = 0; // By default, t0 !
        int i = observationCounter - 1;
        double tForIthObserv;
        while (i > 0) {
            tForIthObserv = t[observationIndexFromCounter[i]];
            if (tForIthObserv <= t[observationCounter] && tForIthObserv > t[j])
                j = i;
            i--;
        }

        // Calculate bound following recipe
        double u = 0.0;
        GammaProcess gpos = ((VarianceGammaProcessDiff) vargamma).getGpos();
        double[] gposPath = gpos.getPath();
        double deltaGpos = gposPath[observationIndex] - gposPath[j];
        double s = path[observationIndex];
        if (muPlusOmega < 0)
             u = s * Math.exp (deltaGpos);
        else u = s * Math.exp (muPlusOmega * (t[observationIndex] - t[j])
                               + deltaGpos);
        return u;
    }


   /**
    * Resets the <TT>GeometricaVarianceGammaProcess</TT>,
    * but also applies the <TT>resetStartProcess</TT> method to the
    * {@link VarianceGammaProcess} object used to generate this process.
    * 
    */
   public void resetStartProcess()  {
        observationIndex   = 0;
        observationCounter = 0;
        vargamma.resetStartProcess();
    }


   /**
    * Sets the parameters
    * 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>,
    * 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> and 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN> of the process.
    * <SPAN  CLASS="textit">Warning</SPAN>: This method will recompute some quantities stored internally,
    * which may be slow if called repeatedly.
    * 
    */
   public void setParams (double s0, double theta, double sigma, double nu,
                          double mu)  {
        this.x0    = s0;
        this.theta = theta;
        this.sigma = sigma;
        this.nu    = nu;
        this.mu    = mu;
        if (observationTimesSet) init(); // Otherwise no need to.
    }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#952;</I></SPAN>.
    * 
    */
   public double getTheta()  { return theta; }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN>.
    * 
    */
   public double getMu()  { return mu; }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
    * 
    */
   public double getNu()  { return nu; }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public double getSigma()  { return sigma; }


   /**
    * Returns the value of the quantity <SPAN CLASS="MATH"><I>&#969;</I></SPAN> defined in.
    * 
    */
   public double getOmega()  { return omega; }


   /**
    * Returns a reference to the variance gamma process <SPAN CLASS="MATH"><I>X</I></SPAN> defined
    * in the constructor.
    * 
    */
   public VarianceGammaProcess getVarianceGammaProcess()  {
        return vargamma;
}


    protected void init() {
        super.init();
        if (1 <= theta*nu + sigma*sigma*nu / 2.0)
           throw new IllegalArgumentException ("theta*nu + sigma*sigma*nu / 2 >= 1");
        omega = Math.log (1 - theta*nu - sigma*sigma*nu / 2.0) / nu;
        muPlusOmega = mu + omega;

        if (observationTimesSet) {
            // Telling the variance gamma proc. about the observ. times
            vargamma.setObservationTimes (t, d);

            // We need to know in which order the observations are generated
            this.observationIndexFromCounter
                = vargamma.getArrayMappingCounterToIndex();

            mudt = new double[d];
            for (int i = 0; i < d; i++)
                mudt[i] = muPlusOmega * (t[i+1] - t[i]);
        }
    }


   public void setStream (RandomStream stream)  {
        vargamma.setStream(stream);
   }


   public RandomStream getStream () {
      return vargamma.getStream();
   }

}

