

/*
 * Class:        GammaProcessBridge
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Pierre Tremblay and Jean-Sébastien Parent
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
import umontreal.iro.lecuyer.util.Num;


/**
 * This class represents a gamma process
 * 
 * <SPAN CLASS="MATH">{<I>S</I>(<I>t</I>) = <I>G</I>(<I>t</I>;<I>&#956;</I>, <I>&#957;</I>) : <I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN> with mean parameter <SPAN CLASS="MATH"><I>&#956;</I></SPAN> and
 * variance parameter <SPAN CLASS="MATH"><I>&#957;</I></SPAN>, sampled using the <SPAN  CLASS="textit">gamma bridge</SPAN> method
 * (see for example).
 * This is analogous to the bridge sampling used in
 * {@link BrownianMotionBridge}.
 * 
 * <P>
 * Note that gamma bridge sampling requires not only gamma variates, but also
 * <SPAN  CLASS="textit">beta</SPAN> variates. The latter generally take a longer time to generate
 * than the former. The class <TT>GammaSymmetricalBridgeProcess</TT> provides
 * a faster implementation when the number of observation times
 * is a power of two.
 * 
 * <P>
 * The warning from class {@link BrownianMotionBridge} applies verbatim
 * to this class.
 * 
 */
public class GammaProcessBridge extends GammaProcess  {
    protected BetaGen      Bgen;
    protected double       mu2OverNu,
                           mu2dTOverNu;
    protected double[]     bMu2dtOverNuL,  // For precomputations for G Bridge
                           bMu2dtOverNuR;
    protected int[]        wIndexList;
    protected int          bridgeCounter = -1; // Before 1st observ



   /**
    * Constructs a new <TT>GammaProcessBridge</TT> with parameters
    * 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.
    * Uses <TT>stream</TT> to generate the gamma and beta variates by inversion.
    * 
    */
   public GammaProcessBridge (double s0, double mu, double nu,
                              RandomStream stream)  {
        this (s0, mu, nu, new GammaGen (stream, new GammaDist (1.0)),
                          new BetaGen (stream, new BetaDist (1.0, 1.0)));
    }


   /**
    * Constructs a new <TT>GammaProcessBridge</TT>. Uses the
    * random variate generators <TT>Ggen</TT> and <TT>Bgen</TT> to generate the gamma
    * and beta variates, respectively. Note that both generator uses the
    * same {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}. Furthermore, the
    * parameters of the
    * {@link umontreal.iro.lecuyer.randvar.GammaGen GammaGen} and
    * {@link umontreal.iro.lecuyer.randvar.BetaGen BetaGen} objects are not
    * important since the implementation forces the generators to use
    * the correct parameters.
    * (as defined in).
    * 
    */
   public GammaProcessBridge (double s0, double mu, double nu,
                              GammaGen Ggen, BetaGen Bgen)  {
        super (s0, mu, nu, Ggen);
        this.Bgen = Bgen;
        this.Bgen.setStream(Ggen.getStream()); // to avoid confusion in streams
        this.stream = Ggen.getStream();
    }


   public double nextObservation()  {
        double s;
        if (bridgeCounter == -1) {
            s = x0 + Ggen.nextDouble(stream, mu2dTOverNu, muOverNu);
            if (s <= x0)
                s = setLarger (x0);
            bridgeCounter    = 0;
            observationIndex = d;
        } else {
            int j = bridgeCounter * 3;
            int oldIndexL = wIndexList[j];
            int newIndex  = wIndexList[j + 1];
            int oldIndexR = wIndexList[j + 2];

            double y =  Bgen.nextDouble(stream, bMu2dtOverNuL[newIndex],
                                         bMu2dtOverNuR[newIndex], 0.0, 1.0);
            s = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y ;
           // make sure the process is strictly increasing
           if (s <= path[oldIndexL])
               s = setLarger (path, oldIndexL, oldIndexR);
            bridgeCounter++;
            observationIndex = newIndex;
        }
        observationCounter = bridgeCounter + 1;
        path[observationIndex] = s;
        return s;
    }

   public double nextObservation (double nextT) {
        double s;
        if (bridgeCounter == -1) {
            t[d] = nextT;
            mu2dTOverNu = mu2OverNu * (t[d] - t[0]);
            s = x0 + Ggen.nextDouble(stream, mu2dTOverNu, muOverNu);
            if (s <= x0)
                s = setLarger (x0);
            bridgeCounter    = 0;
            observationIndex = d;
        } else {
            int j = bridgeCounter * 3;
            int oldIndexL = wIndexList[j];
            int newIndex  = wIndexList[j + 1];
            int oldIndexR = wIndexList[j + 2];

            t[newIndex] = nextT;
            bMu2dtOverNuL[newIndex] = mu2OverNu
                                      * (t[newIndex] - t[oldIndexL]);
            bMu2dtOverNuR[newIndex] = mu2OverNu
                              * (t[oldIndexR] - t[newIndex]);

            double y = Bgen.nextDouble(stream, bMu2dtOverNuL[newIndex],
                                         bMu2dtOverNuR[newIndex], 0.0, 1.0);

            s = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y ;
            // make sure the process is strictly increasing
            if (s <= path[oldIndexL])
               s = setLarger (path, oldIndexL, oldIndexR);
            bridgeCounter++;
            observationIndex = newIndex;
        }
        observationCounter = bridgeCounter + 1;
        path[observationIndex] = s;
        return s;
    }

   public double[] generatePath (double[] uniform01) {
        int oldIndexL, oldIndexR, newIndex;
        double y;

        path[d] = x0 + GammaDist.inverseF (mu2dTOverNu, muOverNu, 10, uniform01[0]);
        for (int j = 0; j < 3*(d-1); j+=3) {
            oldIndexL   = wIndexList[j];
            newIndex    = wIndexList[j + 1];
            oldIndexR   = wIndexList[j + 2];

            y = BetaDist.inverseF(bMu2dtOverNuL[newIndex], bMu2dtOverNuR[newIndex], 8, uniform01[1 + j/3]);

            path[newIndex] = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y;
            // make sure the process is strictly increasing
            if (path[newIndex] <= path[oldIndexL])
               setLarger (path, oldIndexL, newIndex, oldIndexR);
        }
        //resetStartProcess();
        observationIndex   = d;
        observationCounter = d;
        return path;
    }

    public double[] generatePath() {
        int oldIndexL, oldIndexR, newIndex;
        double y;

        path[d] = x0 + Ggen.nextDouble(stream, mu2dTOverNu, muOverNu);
        for (int j = 0; j < 3*(d-1); j+=3) {
            oldIndexL   = wIndexList[j];
            newIndex    = wIndexList[j + 1];
            oldIndexR   = wIndexList[j + 2];

            y = Bgen.nextDouble(stream, bMu2dtOverNuL[newIndex], bMu2dtOverNuR[newIndex], 0.0, 1.0);
            path[newIndex] = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y;
           // make sure the process is strictly increasing
           if (path[newIndex] <= path[oldIndexL])
               setLarger (path, oldIndexL, newIndex, oldIndexR);
        }
        //resetStartProcess();
        observationIndex   = d;
        observationCounter = d;
        return path;
    }

   public void resetStartProcess() {
        observationIndex   = 0;
        observationCounter = 0;
        bridgeCounter = -1;
    }

   protected void init() {
        super.init();
        if (observationTimesSet) {

        // Quantities for gamma bridge process
        bMu2dtOverNuL = new double[d+1];
        bMu2dtOverNuR = new double[d+1];
        wIndexList  = new int[3*d];

        int[] ptIndex = new int[d+1];
        int   indexCounter = 0;
        int   newIndex, oldLeft, oldRight;

        ptIndex[0] = 0;
        ptIndex[1] = d;

        mu2OverNu   = mu * mu / nu;
        mu2dTOverNu = mu2OverNu * (t[d] - t[0]);

        for (int powOfTwo = 1; powOfTwo <= d/2; powOfTwo *= 2) {
            /* Make room in the indexing array "ptIndex" */
            for (int j = powOfTwo; j >= 1; j--) { ptIndex[2*j] = ptIndex[j]; }

            /* Insert new indices and Calculate constants */
            for (int j = 1; j <= powOfTwo; j++) {
                oldLeft  = 2*j - 2;
                oldRight = 2*j;
                newIndex = (int) (0.5*(ptIndex[oldLeft] + ptIndex[oldRight]));

                bMu2dtOverNuL[newIndex] = mu * mu
                                   * (t[newIndex] - t[ptIndex[oldLeft]]) / nu;
                bMu2dtOverNuR[newIndex] = mu * mu
                                  * (t[ptIndex[oldRight]] - t[newIndex]) / nu;

                ptIndex[oldLeft + 1]       = newIndex;
                wIndexList[indexCounter]   = ptIndex[oldLeft];
                wIndexList[indexCounter+1] = newIndex;
                wIndexList[indexCounter+2] = ptIndex[oldRight];

                indexCounter += 3;
            }
        }
        /* Check if there are holes remaining and fill them */
        for (int k = 1; k < d; k++) {
            if (ptIndex[k-1] + 1 < ptIndex[k]) {
            // there is a hole between (k-1) and k.

                bMu2dtOverNuL[ptIndex[k-1]+1] = mu * mu
                                  * (t[ptIndex[k-1]+1] - t[ptIndex[k-1]]) / nu;
                bMu2dtOverNuR[ptIndex[k-1]+1] = mu * mu
                                  * (t[ptIndex[k]] - t[ptIndex[k-1]+1]) / nu;

                wIndexList[indexCounter]   = ptIndex[k]-2;
                wIndexList[indexCounter+1] = ptIndex[k]-1;
                wIndexList[indexCounter+2] = ptIndex[k];
                indexCounter += 3;
            }
        }
        }
    }

   /**
    * Resets the {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}
    * of the {@link umontreal.iro.lecuyer.randvar.GammaGen GammaGen} and
    * the {@link umontreal.iro.lecuyer.randvar.BetaGen BetaGen} to <TT>stream</TT>.
    * 
    */
   public void setStream (RandomStream stream) {
        super.setStream(stream);
        this.Bgen.setStream(stream);
        this.stream = stream;
}


}

