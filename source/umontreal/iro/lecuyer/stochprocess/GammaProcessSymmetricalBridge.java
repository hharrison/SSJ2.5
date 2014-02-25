

/*
 * Class:        GammaProcessSymmetricalBridge
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @authors      Pierre Tremblay and Jean-Sébastien Parent
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
import umontreal.iro.lecuyer.util.*;



/**
 * This class differs from <TT>GammaProcessBridge</TT> only in that it requires
 * the number of interval of the path to be
 * a power of 2 and of equal size.
 * It is then possible to generate the bridge process using
 * a special implementation of the beta random variate generator
 * (using the <SPAN  CLASS="textit">symmetrical</SPAN> beta distribution)
 * that is much faster (HOW MUCH? QUANTIFY!) than the general case.
 * Note that when the method <TT>setObservationTimes</TT> is called,
 * the equality of the size of the time steps is verified.
 * To allow for differences due to floating point errors, time steps
 * are considered to be equal if their relative difference is less
 * than <SPAN CLASS="MATH">10<SUP>-15</SUP></SPAN>.
 * 
 */
public class GammaProcessSymmetricalBridge extends GammaProcessBridge  {
    protected BetaSymmetricalGen BSgen;



   /**
    * Constructs a new <TT>GammaProcessSymmetricalBridge</TT>
    * with parameters 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> and initial
    * value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.
    * The random variables are created using the
    * {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} <TT>stream</TT>.
    * Note that the same {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}
    * <TT>stream</TT> is used for the
    * {@link umontreal.iro.lecuyer.randvar.GammaGen GammaGen} and for the
    * {@link umontreal.iro.lecuyer.randvar.BetaSymmetricalGen BetaSymmetricalGen}
    * inluded in this class.
    * 
    */
   public GammaProcessSymmetricalBridge (double s0, double mu, double nu,
                                         RandomStream stream)  {
        this (s0, mu, nu, new GammaGen (stream, new GammaDist (1.0)),
              new BetaSymmetricalGen (stream, new BetaSymmetricalDist (1.0)));
    }


   /**
    * Constructs a new <TT>GammaProcessSymmetricalBridge</TT>
    * with parameters 
    * <SPAN CLASS="MATH"><I>&#956;</I> = <texttt>mu</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> and initial
    * value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.  Note that the
    * {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} included in
    * the {@link umontreal.iro.lecuyer.randvar.BetaSymmetricalGen BetaSymmetricalGen}
    * is sets to the one included in the
    * {@link umontreal.iro.lecuyer.randvar.GammaGen GammaGen} to avoid confusion.
    * This {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} is then used to
    * generate all the random variables.
    * 
    * 
    */
   public GammaProcessSymmetricalBridge (double s0, double mu, double nu,
                                         GammaGen Ggen,
                                         BetaSymmetricalGen BSgen)  {
        super (s0, mu, nu, Ggen, BSgen);
        this.BSgen = BSgen;
        BSgen.setStream(Ggen.getStream());
    }
 

    public double nextObservation()  {
      double s;
      if (bridgeCounter == -1) {
         s = x0 + Ggen.nextDouble(stream, mu2dTOverNu, muOverNu);
         if (s <= x0)
              s = setLarger (x0);
         bridgeCounter = 0;
         observationIndex = d;
      } else {
         int j = bridgeCounter * 3;
         int oldIndexL = wIndexList[j];
         int newIndex = wIndexList[j + 1];
         int oldIndexR = wIndexList[j + 2];

         double y = BSgen.nextDouble(stream, bMu2dtOverNuL[newIndex]);

         s = path[oldIndexL] + (path[oldIndexR] - path[oldIndexL]) * y;
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
            int j = bridgeCounter*3;
            int oldIndexL = wIndexList[j];
            int newIndex  = wIndexList[j + 1];
            int oldIndexR = wIndexList[j + 2];

            t[newIndex] = nextT;
            bMu2dtOverNuL[newIndex] = mu2OverNu
                                      * (t[newIndex] - t[oldIndexL]);

            double y =  BSgen.nextDouble(stream, bMu2dtOverNuL[newIndex]);

            s = path[oldIndexL] + (path[oldIndexR] - path[oldIndexL]) * y;
            if (s <= path[oldIndexL])
                s = setLarger (path, oldIndexL, oldIndexR);
            bridgeCounter++;
            observationIndex = newIndex;
        }
        observationCounter = bridgeCounter + 1;
        path[observationIndex] = s;
        return s;
    }

    public double[] generatePath() {
        int oldIndexL, oldIndexR, newIndex;

        path[d] = x0 + Ggen.nextDouble(stream, mu2dTOverNu, muOverNu);
        for (int j = 0; j < 3*(d-1); j+=3) {
            oldIndexL   = wIndexList[j];
            newIndex    = wIndexList[j + 1];
            oldIndexR   = wIndexList[j + 2];

            double y =  BSgen.nextDouble(stream, bMu2dtOverNuL[newIndex]);

            path[newIndex] = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y;
            if (path[newIndex] <= path[oldIndexL])
                setLarger (path, oldIndexL, newIndex, oldIndexR);
        }
        observationIndex   = d;
        observationCounter = d;
        return path;
    }

    public double[] generatePath (double[] uniform01) {
        int oldIndexL, oldIndexR, newIndex;

        path[d] = x0 + GammaDist.inverseF(mu2dTOverNu, muOverNu, 10, uniform01[0]);
        for (int j = 0; j < 3*(d-1); j+=3) {
            oldIndexL   = wIndexList[j];
            newIndex    = wIndexList[j + 1];
            oldIndexR   = wIndexList[j + 2];

            double y =  BetaSymmetricalDist.inverseF(bMu2dtOverNuL[newIndex], uniform01[1 + j/3]);

            path[newIndex] = path[oldIndexL] +
              (path[oldIndexR] - path[oldIndexL]) * y;
           if (path[newIndex] <= path[oldIndexL])
               setLarger (path, oldIndexL, newIndex, oldIndexR);
        }
        observationIndex   = d;
        observationCounter = d;
        return path;
    }

    protected void init () {
        super.init ();
        if (observationTimesSet) {

            /* Testing to make sure number of observations n = 2^k */
            int k = 0;
            int x = d;
            int y = 1;
            while (x>1) {
            x = x / 2;
            y = y * 2;
            k++;
            }
            if (y != d) throw new IllegalArgumentException
            ( "GammaProcessSymmetricalBridge:"
                +"Number 'n' of observation times is not a power of 2" );

            /* Testing that time intervals are equidistant */
            boolean equidistant = true;
            double macheps = 1.0e-13; // Num.DBL_EPSILON;
            double dt = t[1] - t[0];
            for (int i=1; i<d; i++) {
                if ((t[i+1] - t[i]) != dt) { // not equidistant
                    equidistant = false;
                    /* This compensates the fact that the dt's
                    may be different due to numerical idiosyncracies */
                    if (dt != 0.0)
                        if (Math.abs ((t[i+1] - t[i]) - dt) / dt <= macheps)
                            equidistant = true;
                }
            }
            if (!equidistant) throw new IllegalArgumentException
                        ( "GammaProcessSymmetricalBridge:"
                        +"Observation times of sample paths are not equidistant" );
        }
    }
}

