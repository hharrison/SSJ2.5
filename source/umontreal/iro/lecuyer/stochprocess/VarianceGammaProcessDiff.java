

/*
 * Class:        VarianceGammaProcessDiff
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
 * <SPAN CLASS="MATH">{<I>S</I>(<I>t</I>) = <I>X</I>(<I>t</I>;<I>&#952;</I>, <I>&#963;</I>, <I>&#957;</I>) : <I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN>.
 * This process is generated using <SPAN  CLASS="textit">difference of gamma sampling</SPAN>
 * (see), which uses the representation of the VG process
 * as the difference of two independent {@link GammaProcess}'es (see):
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="dblGammaEqn"></A>
 * <I>X</I>(<I>t</I>;<I>&#952;</I>, <I>&#963;</I>, <I>&#957;</I>) : = <I>X</I>(0) + <I>&#915;</I><SUP>+</SUP>(<I>t</I>;<I>&#956;</I><SUB>p</SUB>, <I>&#957;</I><SUB>p</SUB>) - <I>&#915;</I><SUP>-</SUP>(<I>t</I>;<I>&#956;</I><SUB>n</SUB>, <I>&#957;</I><SUB>n</SUB>)
 * </DIV><P></P>
 * where <SPAN CLASS="MATH"><I>X</I>(0)</SPAN> is a constant corresponding to the initial value of the process
 * and
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="dblGammaParams"></A>
 * <TABLE>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>&#956;</I><SUB>p</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT">(( &thetas;^2 + 2&sigma;^2/&nu;)<SUP>1/2</SUP> + <I>&#952;</I>)/2</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>&#956;</I><SUB>n</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT">(( &thetas;^2 + 2&sigma;^2/&nu;)<SUP>1/2</SUP> - <I>&#952;</I>)/2</TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>&#957;</I><SUB>p</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT"><I>&#957;&#956;</I><SUB>p</SUB><SUP>2</SUP></TD>
 * </TR>
 * <TR VALIGN="MIDDLE"><TD ALIGN="RIGHT"><I>&#957;</I><SUB>n</SUB></TD>
 * <TD ALIGN="CENTER">=</TD>
 * <TD ALIGN="LEFT"><I>&#957;&#956;</I><SUB>n</SUB><SUP>2</SUP></TD>
 * </TR>
 * </TABLE>
 * </DIV><P></P>
 * 
 */
public class VarianceGammaProcessDiff extends VarianceGammaProcess  {
    protected GammaProcess gpos;
    protected GammaProcess gneg;
    protected double       mup, mun,
                           nup, nun;



   /**
    * Constructs a new <TT>VarianceGammaProcessDiff</TT> with parameters
    * 
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN>
    * and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>. <TT>stream</TT> is
    * used by two instances of {@link GammaProcess}, 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> and 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN>,
    * respectively. The other parameters are
    * as in the class {@link VarianceGammaProcess}.
    * The {@link GammaProcess} objects for 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> and 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN> are
    * constructed using the parameters from and their
    * initial values  
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP>(<I>t</I><SUB>0</SUB>)</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP>(<I>t</I><SUB>0</SUB>)</SPAN> are set to <SPAN CLASS="MATH">0</SPAN>.
    * 
    */
   public VarianceGammaProcessDiff (double s0, double theta, double sigma,
                                    double nu, RandomStream stream)  {
        this (s0, theta, sigma, nu,
              new GammaProcess (0.0, 1.0, 1.0, stream),
              new GammaProcess (0.0, 1.0, 1.0, stream));
        // Params mu, nu of the 2 gamma processes are redefined in init()
        // which will be called after a call to 'setObservTimes'
    }


   /**
    * The parameters of the {@link GammaProcess}
    * objects for 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> and 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN> are
    * set to those of and their
    * initial values  
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP>(<I>t</I><SUB>0</SUB>)</SPAN> and 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP>(<I>t</I><SUB>0</SUB>)</SPAN> are set to <SPAN CLASS="MATH"><I>t</I><SUB>0</SUB></SPAN>.
    * The <TT>RandomStream</TT>  of the 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN> process is overwritten with the
    * <TT>RandomStream</TT>  of the 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> process.
    * 
    */
   public VarianceGammaProcessDiff (double s0, double theta, double sigma,
                                    double nu, GammaProcess gpos,
                                    GammaProcess gneg)  {
        this.gpos = gpos;
        this.gneg = gneg;
        setParams (s0, theta, sigma, nu);
        gneg.setStream(gpos.getStream());  // to avoid confusion with stream because there is only
                                 // one stream in the other constructor
    }


   public double nextObservation() {
        // This implementation takes possible bridge sampling into account
        double s = x0 + gpos.nextObservation() - gneg.nextObservation();
        observationIndex = gpos.getCurrentObservationIndex();
        path[observationIndex] = s;
        observationCounter++;
        return s;
     }

// no longer useful, this method was created to automaticaly alternate
// between the two processes the uniform random variables used in the
// in the simulation.  However, this method does not work if the two
// GammaProcess are PCA...
//    public double[] generatePath()  {
//         gpos.resetStartProcess();
//         gneg.resetStartProcess();
//         double s;
//         for (int i=1; i<=d; i++) {
//            s = x0 + gpos.nextObservation() - gneg.nextObservation();
//            path[gpos.getCurrentObservationIndex()] = s;
//            // Note: we must get the observCounter from gpos in the case that
//            // the process is generated by a Gamma bridge
//            // The observCounter of gneg should be the same because both
//            // gamma processes should be generated the same way
//         }
//         observationIndex   = d;
//         observationCounter = d;
//         return path;
//     }


   /**
    * Generates, returns and saves the path.  To do so, the
    * path of 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> is first generated and then the path
    * of 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN>.  This is not the optimal way of proceeding
    * in order to reduce the variance in QMC simulations; for that,
    * use <TT>generatePath(double[] uniform01)</TT> instead.
    * 
    */
   public double[] generatePath()  {
        double[] pathUP = gpos.generatePath();
        double[] pathDOWN = gneg.generatePath();

        for (int i=0; i<d; i++) {
           path[i+1] = x0 + pathUP[i+1] - pathDOWN[i+1];
        }
        observationIndex   = d;
        observationCounter = d;
        return path;
    }


   /**
    * Similar to the usual <TT>generatePath()</TT>, but here the uniform
    * random numbers used for the simulation must be provided to the method.  This
    * allows to properly use the uniform random variates in QMC simulations.
    * This method divides the table of uniform random
    * numbers <TT>uniform01</TT> in two smaller tables, the first one containing
    * the odd indices of <TT>uniform01</TT> are used to generate the path of 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN>
    * and the even indices are used to generate the path of  
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN>.
    * This way of proceeding further reduces the
    * variance for QMC simulations.
    * 
    */
   public double[] generatePath (double[] uniform01)  {
        int dd = uniform01.length;
        int d = dd / 2;

        if (dd % 2 != 0) {
           throw new IllegalArgumentException (
                     "The Array uniform01 must have a even length");
        }

        double[] QMCpointsUP = new double[d];
        double[] QMCpointsDW = new double[d];

        for(int i = 0; i < d; i++){
            QMCpointsUP[i] = uniform01[2*i];  // keeps the odd numbers for the gamma process
            QMCpointsDW[i] = uniform01[2*i + 1]; // and the even for the BM process
        }
        gpos.resetStartProcess();
        gneg.resetStartProcess();

        double[] pathUP = gpos.generatePath(QMCpointsUP);
        double[] pathDOWN = gneg.generatePath(QMCpointsDW);

        for (int i=0; i<d; i++) {
           path[i+1] = x0 + pathUP[i+1] - pathDOWN[i+1];
        }
        observationIndex   = d;
        observationCounter = d;
        return path;
    }



   /**
    * Sets the observation times on the <TT>VarianceGammaProcessDiff</TT>
    * as usual, but also applies the <TT>resetStartProcess</TT> method to the two
    * {@link GammaProcess} objects used to generate this process.
    * 
    */
   public void resetStartProcess()  {
        observationIndex   = 0;
        observationCounter = 0;
        gpos.resetStartProcess();
        gneg.resetStartProcess();
    }


   /**
    * Returns a reference to the {@link GammaProcess} object <TT>gpos</TT>
    * used to generate the 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> component of the process.
    * 
    */
   public GammaProcess getGpos()  {
        return gpos;
    }


   /**
    * Returns a reference to the {@link GammaProcess} object <TT>gneg</TT>
    * used to generate the 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>-</SUP></SPAN> component of the process.
    * 
    */
   public GammaProcess getGneg()  {
        return gneg;
    }


    protected void init() {
        // super.init() is not called because the init() in VarianceGammaProcess
        // is very different.
        mup = 0.5 * (Math.sqrt (theta*theta + 2*sigma*sigma/nu) + theta);
        mun = 0.5 * (Math.sqrt (theta*theta + 2*sigma*sigma/nu) - theta);
        nup = mup * mup * nu;
        nun = mun * mun * nu;
        if (observationTimesSet) {
            path[0] = x0;
            gpos.setParams(t[0], mup, nup);
            gneg.setParams(t[0], mun, nun);
        }
    }

   /**
    * Sets the observation times on the <TT>VarianceGammaProcesDiff</TT>
    * as usual, but also sets the observation times of the underlying
    * {@link GammaProcess}'es.
    * 
    */
   public void setObservationTimes (double t[], int d)  {
         gpos.setObservationTimes(t, d);
         gneg.setObservationTimes(t, d);
         super.setObservationTimes(t, d);
// the initial value is set to t[0] in the init, which is called in
// super.setObservationTimes(t, d).
     }


   /**
    * Returns the <TT>RandomStream</TT> of  the 
    * <SPAN CLASS="MATH"><I>&#915;</I><SUP>+</SUP></SPAN> process.
    * 
    */
   public RandomStream getStream()  {
      return gpos.getStream ();
   }


   /**
    * Sets the {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream}
    * of the two {@link GammaProcess}'es to  <TT>stream</TT>.
    * 
    */
   public void setStream (RandomStream stream)  {
         gpos.setStream(stream);
         gneg.setStream(stream);
   }

}
