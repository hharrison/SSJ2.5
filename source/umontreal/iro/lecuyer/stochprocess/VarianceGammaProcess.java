

/*
 * Class:        VarianceGammaProcess
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since        2004

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
 * This class represents a <SPAN  CLASS="textit">variance gamma</SPAN> (VG) process
 * 
 * <SPAN CLASS="MATH">{<I>S</I>(<I>t</I>) = <I>X</I>(<I>t</I>;<I>&#952;</I>, <I>&#963;</I>, <I>&#957;</I>) : <I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN>. This process is
 * obtained as a subordinate of the Brownian motion process 
 * <SPAN CLASS="MATH"><I>B</I>(<I>t</I>;<I>&#952;</I>, <I>&#963;</I>)</SPAN>
 * using the operational time 
 * <SPAN CLASS="MATH"><I>G</I>(<I>t</I>;1, <I>&#957;</I>)</SPAN> (see):
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="VGeqn"></A>
 * <I>X</I>(<I>t</I>;<I>&#952;</I>, <I>&#963;</I>, <I>&#957;</I>) : = <I>B</I>(<I>G</I>(<I>t</I>;1, <I>&#957;</I>), <I>&#952;</I>, <I>&#963;</I>).
 * </DIV><P></P>
 * See also for applications to modelling
 * asset returns and option pricing.
 * 
 * <P>
 * The process is sampled as follows: when <TT>generatePath()</TT> is called, the method
 * <TT>generatePath()</TT> of the inner {@link GammaProcess} is called;
 * its path is then used to set the observation times of the
 * {@link BrownianMotion}.  Finally, the method <TT>generatePath()</TT> of
 * the {@link BrownianMotion} is called.
 * <SPAN  CLASS="textit">Warning</SPAN>: If one wants to reduced the variance as much as possible
 * in a QMC simulation, this way of proceeding is not optimal.  Use
 * the method <TT>generatePath(uniform01)</TT> instead.
 * 
 * <P>
 * If one calls the <TT>nextObservation</TT> method, the operational
 * time is generated first, followed by the corresponding
 * brownian motion increment, which is then returned.
 * 
 * <P>
 * Note that if one wishes to use <SPAN  CLASS="textit">bridge</SPAN> sampling with the
 * <TT>nextObservation</TT> method, both the gamma process <SPAN CLASS="MATH"><I>G</I></SPAN>
 * and the Brownian motion process <SPAN CLASS="MATH"><I>B</I></SPAN> should use bridge sampling so that
 * their observations are synchronized.
 * 
 */
public class VarianceGammaProcess extends StochasticProcess  {

    protected GammaProcess   randomTime;  // For the transformed time method
    protected BrownianMotion BM;

    protected double       theta,
                           sigma,
                           nu;



   public VarianceGammaProcess() {} 

   /**
    * Constructs a new <TT>VarianceGammaProcess</TT> with parameters
    * 
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN>
    * and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.
    * <TT>stream</TT> is used to generate both the {@link BrownianMotion} <SPAN CLASS="MATH"><I>B</I></SPAN> and the
    * {@link GammaProcess} <SPAN CLASS="MATH"><I>G</I></SPAN> in.
    * 
    */
   public VarianceGammaProcess (double s0, double theta, double sigma,
                                double nu, RandomStream stream)  {
        this (s0, new BrownianMotion (s0, theta, sigma, stream),
                  new GammaProcess (0.0, 1.0, nu, stream));
    }


   /**
    * Constructs a new <TT>VarianceGammaProcess</TT>.
    * The parameters <SPAN CLASS="MATH"><I>&#952;</I></SPAN> and  <SPAN CLASS="MATH"><I>&#963;</I></SPAN> are set to the parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and
    * <SPAN CLASS="MATH"><I>&#963;</I></SPAN>, respectively, of the {@link BrownianMotion} <TT>BM</TT> and
    * the parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN> is set to the parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN> of the {@link GammaProcess}
    * <TT>Gamma</TT>. The parameters <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>x</I>0</SPAN> of the {@link GammaProcess} are
    * overwritten to equal 1 and 0 respectively.
    * The initial value of the process is 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <IMG
    *  ALIGN="BOTTOM" BORDER="0" SRC="VarianceGammaProcessimg1.png"
    *  ALT="$ \tt s0$"></SPAN>.
    * 
    */
   public VarianceGammaProcess (double s0, BrownianMotion BM,
                                GammaProcess Gamma)  {
        this.BM         = BM;
        Gamma.setParams(0.0, 1.0, Gamma.getNu());  // forces the average of the GammaProcess
        randomTime      = Gamma;                   // to be 1.0 and the initial value to be 0.0
        setParams (s0, BM.getMu(), BM.getSigma(), Gamma.getNu());
    }


   /**
    * Generates the observation for the next time.
    * It also works with bridge sampling; however <SPAN  CLASS="textit">both</SPAN>
    * {@link BrownianMotionBridge} and {@link GammaProcessBridge}
    * must be used in the constructor in that case.  Furthermore, for bridge
    * sampling, the order of the observations is that of the bridge,
    * not sequential order.
    * 
    */
   public double nextObservation() {
        // We first generate w, then verify what its new counter value is
        // This is necessary to be general enough to handle bridge sampling
        double nextBM = BM.nextObservation (randomTime.nextObservation ()); 
        observationIndex = BM.getCurrentObservationIndex();
        path[observationIndex] = nextBM;
        observationCounter++;
        return nextBM;
    }


   /**
    * Generates and returns the path. To do so, it
    * first generates the complete path of the inner {@link GammaProcess}
    * and sets the observation times of the inner {@link BrownianMotion}
    * to this path.  This method is not optimal to reduce the variance in
    * QMC simulations; use <TT>generatePath(double[] uniform01)</TT> for that.
    * 
    */
   public double[] generatePath()  {
        BM.setObservationTimes(randomTime.generatePath(), d);
        path = BM.generatePath();
        observationIndex = d;
        observationCounter = d;
        return path;
    }


   /**
    * Similar to the usual <TT>generatePath()</TT>, but here the uniform
    * random numbers used for the simulation must be provided to the method.  This
    * allows to properly use the uniform random variates in QMC simulations.
    * This method divides the table of uniform random
    * numbers <TT>uniform01</TT> in two smaller tables, the first one, containing the
    * odd indices of <TT>uniform01</TT> which are used to generate the path of the inner
    * {@link GammaProcess}, and the even indices (in the second table) are used to
    * generate the path of the inner {@link BrownianMotion}. This way of proceeding
    * reduces the variance as much as possible for QMC simulations.
    * 
    */
   public double[] generatePath (double[] uniform01)  {
        int dd = uniform01.length;
        int d = dd / 2;

        if (dd % 2 != 0) {
            throw new IllegalArgumentException (
                     "The Array uniform01 must have a even length");
        }

        double[] QMCpointsGP = new double[d];
        double[] QMCpointsBM = new double[d];

        for(int i = 0; i < d; i++){
            QMCpointsGP[i] = uniform01[2 * i];  // the odd numbers for the gamma process
            QMCpointsBM[i] = uniform01[2 * i + 1];  //  and the even for the BM process
        }
        BM.setObservationTimes(randomTime.generatePath(QMCpointsGP), d);

        path = BM.generatePath(QMCpointsBM);
        observationIndex = d;
        observationCounter = d;
        return path;
    }


   /**
    * Resets the observation index and counter to 0 and
    * applies the <TT>resetStartProcess</TT> method to the
    * {@link BrownianMotion} and the {@link GammaProcess} objects
    * used to generate this process.
    * 
    */
    public void resetStartProcess()  {
        observationIndex   = 0;
        observationCounter = 0;
        BM.resetStartProcess();
        randomTime.resetStartProcess();
    }


   /**
    * Sets the parameters
    * 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) =</SPAN> <TT>s0</TT>, <SPAN CLASS="MATH"><I>&#952;</I> =</SPAN> <TT>theta</TT>, <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT>
    * and <SPAN CLASS="MATH"><I>&#957;</I> =</SPAN> <TT>nu</TT> of the process.
    * <SPAN  CLASS="textit">Warning</SPAN>: This method will recompute some quantities stored internally,
    * which may be slow if called repeatedly.
    * 
    */
   public void setParams (double s0, double theta, double sigma, double nu)  {
        this.x0    = s0;
        this.theta = theta;
        this.sigma = sigma;
        this.nu    = nu; 
        if (observationTimesSet) init(); // Otherwise no need to.
    }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#952;</I></SPAN>.
    * 
    */
   public double getTheta()  { return theta; }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public double getSigma()  { return sigma; }


   /**
    * Returns the value of the parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>.
    * 
    */
   public double getNu()  { return nu; }


    protected void init() {
        super.init();
        if(observationTimesSet){
            randomTime.setObservationTimes(t, d);
            randomTime.x0 = t[0];
        }
    }

   /**
    * Sets the observation times on the <TT>VarianceGammaProcess</TT> as
    * usual, but also sets the observation times of the underlying
    * {@link GammaProcess}. It furthermore sets the starting <SPAN  CLASS="textit">value</SPAN>
    * of the {@link GammaProcess} to <TT>t[0]</TT>.
    * 
    */
    public void setObservationTimes (double t[], int d)  {
        super.setObservationTimes(t, d);  //sets the observation times of the GammaProcess by
    }                                     //calling init()



   /**
    * Resets the {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}'s.
    * Warning: this method sets both the
    * {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} of the
    * {@link BrownianMotion} and of the {@link GammaProcess} to
    * the same {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}.
    * 
    */
    public void setStream (RandomStream stream)  {
         BM.setStream (stream);
   }


   /**
    * Returns the random stream of the {@link BrownianMotion} process, which should
    * be the same as for the {@link GammaProcess}.
    * 
    */
   public RandomStream getStream() {
        return BM.getStream();
   }


   /**
    * Returns a reference to the inner {@link BrownianMotion}.
    * 
    */
   public BrownianMotion getBrownianMotion()  {
      return BM;
   }


   /**
    * Returns a reference to the inner {@link GammaProcess}.
    * 
    */
   public GammaProcess getGammaProcess()  {
      return randomTime;
   }

}

