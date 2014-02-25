

/*
 * Class:        GeometricBrownianMotion
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
 * Represents a <SPAN  CLASS="textit">geometric Brownian motion</SPAN> (GBM) process 
 * <SPAN CLASS="MATH">{<I>S</I>(<I>t</I>),&nbsp;<I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN>,  
 * which evolves according to the stochastic differential equation 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:GBM"></A>
 * <I>dS</I>(<I>t</I>) = <I>&#956;S</I>(<I>t</I>)<I>dt</I> + <I>&#963;S</I>(<I>t</I>)<I>dB</I>(<I>t</I>),
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN> are the drift and volatility parameters,
 * and 
 * <SPAN CLASS="MATH">{<I>B</I>(<I>t</I>),&nbsp;<I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN> is a standard Brownian motion 
 * (for which 
 * <SPAN CLASS="MATH"><I>B</I>(<I>t</I>)&#8764;<I>N</I>(0, <I>t</I>)</SPAN>).
 * This process can also be written as the exponential of a Brownian motion:
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:GBM2"></A>
 * <I>S</I>(<I>t</I>) = <I>S</I>(0)exp[(<I>&#956;</I> - <I>&#963;</I><SUP>2</SUP>/2)<I>t</I> + <I>&#963;tB</I>(<I>t</I>)] = <I>S</I>(0)exp[<I>X</I>(<I>t</I>)],
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>X</I>(<I>t</I>) = (<I>&#956;</I> - <I>&#963;</I><SUP>2</SUP>/2)<I>t</I> + <I>&#963;tB</I>(<I>t</I>)</SPAN>.
 * The GBM process is simulated by simulating the BM process <SPAN CLASS="MATH"><I>X</I></SPAN> and taking the exponential.
 * This BM process is stored internally.
 * 
 */
public class GeometricBrownianMotion extends StochasticProcess  {

    protected NormalGen      gen;
    protected BrownianMotion bm;   // The underlying BM process X.
    protected double         mu,
                             sigma;
    protected double[]       mudt;



   /**
    * Same as <TT>GeometricBrownianMotion (s0, mu, sigma,
    * new BrownianMotion (0.0, 0.0, 1.0, stream))</TT>.
    * 
    */
   public GeometricBrownianMotion (double s0, double mu, double sigma,
                                   RandomStream stream)  {
        this (s0, mu, sigma, new BrownianMotion (0.0, 0.0, 1.0, stream));
    }


   /**
    * Constructs a new <TT>GeometricBrownianMotion</TT> with parameters
    *  
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, and 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>,
    * using <TT>bm</TT> as the underlying {@link BrownianMotion}.
    * The parameters of <TT>bm</TT> are automatically reset to
    * 
    * <SPAN CLASS="MATH"><I>&#956;</I> - <I>&#963;</I><SUP>2</SUP>/2</SPAN> and <SPAN CLASS="MATH"><I>&#963;</I></SPAN>, regardless of the original parameters
    *  of <TT>bm</TT>.
    * The observation times are the same as those of <TT>bm</TT>. The generation
    * method depends on that of <TT>bm</TT> (sequential, bridge sampling, PCA, etc.).
    * 
    */
   public GeometricBrownianMotion (double s0, double mu, double sigma,
                                   BrownianMotion bm)  {
        this.bm = bm;
        setParams (s0, mu, sigma);
    }

   public void setObservationTimes (double[] t, int d) {
        this.d = d;
        super.setObservationTimes (t, d);
        bm.setObservationTimes (t, d);
    }

   public double nextObservation() {
        // Note : this implementation is general, to deal with
        // the possibility of generating bm with bridge sampling, for example.  ???

        double s = x0 * Math.exp (bm.nextObservation());
        observationIndex = bm.getCurrentObservationIndex();
        path[observationIndex] = s;
        // Could be different than simply 'observationCounter++' because of the
        // possibility of Brownian bridge

        return s;
    }

   public double[] generatePath() {
        path[0] = x0;
        bm.generatePath ();
        for (int i = 1; i <= d; ++i)
            path[i] = x0 * Math.exp (bm.getObservation(i));
        observationCounter = d;
        return path;
    }

   public double[] generatePath (RandomStream stream) {
        setStream (stream);
        return generatePath();
    }

   /**
    * Same as in <TT>StochasticProcess</TT>, but also invokes 
    * <TT>resetStartProcess</TT> for the underlying <TT>BrownianMotion</TT> object.
    * 
    */
   public void resetStartProcess()  {
        observationCounter = 0;
        bm.resetStartProcess();
    }


   /**
    * Sets the parameters 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN> and 
    * 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN> of the process. 
    * <SPAN  CLASS="textit">Warning</SPAN>: This method will recompute some quantities stored internally, 
    * which may be slow if called repeatedly.
    * 
    */
   public void setParams (double s0, double mu, double sigma)  { 
        this.x0    = s0;
        this.mu    = mu;
        this.sigma = sigma;
        bm.setParams (0.0, mu - 0.5 * sigma * sigma, sigma);
        if (observationTimesSet) init(); // Otherwise not needed.
    }


   /**
    * Resets the {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} 
    * for the underlying Brownian motion to <TT>stream</TT>.
    * 
    */
   public void setStream (RandomStream stream)  { (bm.gen).setStream (stream); }


   /**
    * Returns the {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}
    * for the underlying Brownian motion.
    * 
    */
   public RandomStream getStream()  { return (bm.gen).getStream (); }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#956;</I></SPAN>.
    * 
    */
   public double getMu()  { return mu; }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>&#963;</I></SPAN>.
    * 
    */
   public double getSigma()  { return sigma; }


   /**
    * Returns the {@link umontreal.iro.lecuyer.randvar.NormalGen NormalGen} used.
    * 
    */
   public NormalGen getGen()  { return gen; }


   /**
    * Returns a reference to the {@link BrownianMotion} object
    * used to generate the process.
    * 
    */
   public BrownianMotion getBrownianMotion()  { 
        return bm;
    }
 

    protected void init() {
        super.init();   // Maybe useless...
    }

}
