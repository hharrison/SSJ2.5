

/*
 * Class:        OrnsteinUhlenbeckProcessEuler
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
 * This class represents an <SPAN  CLASS="textit">Ornstein-Uhlenbeck</SPAN> process
 * as in {@link OrnsteinUhlenbeckProcess}, but
 * the process is generated using the simple Euler scheme
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay"><A NAME="eq:ornstein-seqEuler"></A>
 * <I>X</I>(<I>t</I><SUB>j</SUB>) - <I>X</I>(<I>t</I><SUB>j-1</SUB>) = <I>&#945;</I>(<I>b</I> - <I>X</I>(<I>t</I><SUB>j-1</SUB>))(<I>t</I><SUB>j</SUB> - <I>t</I><SUB>j-1</SUB>) + <I>&#963;</I>(t_j - t_j-1)<SUP>1/2</SUP>&nbsp;<I>Z</I><SUB>j</SUB>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>Z</I><SUB>j</SUB>&#8764;<I>N</I>(0, 1)</SPAN>.
 * This is a good approximation only for small time intervals 
 * <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB> - <I>t</I><SUB>j-1</SUB></SPAN>.
 * 
 */
public class OrnsteinUhlenbeckProcessEuler extends OrnsteinUhlenbeckProcess {



   /**
    * Constructor with 
    * parameters <SPAN CLASS="MATH"><I>&#945;</I> =</SPAN> <TT>alpha</TT>, <SPAN CLASS="MATH"><I>b</I></SPAN>, <SPAN CLASS="MATH"><I>&#963;</I> =</SPAN> <TT>sigma</TT> and initial
    * value 
    * <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>0</SUB>) =</SPAN> <TT>x0</TT>. The normal variates <SPAN CLASS="MATH"><I>Z</I><SUB>j</SUB></SPAN> will be 
    * generated by inversion using the stream <TT>stream</TT>.
    * 
    */
   public OrnsteinUhlenbeckProcessEuler (double x0, double alpha, double b,
                                         double sigma, RandomStream stream)
    {
      this (x0, alpha, b, sigma, new NormalGen (stream));
   }


   /**
    * Here, the normal variate generator is specified directly
    * instead of specifying the stream.
    * The normal generator <TT>gen</TT> can use another method than inversion.
    * 
    */
   public OrnsteinUhlenbeckProcessEuler (double x0, double alpha, double b,
                                         double sigma, NormalGen gen)  {
      super (x0, alpha, b, sigma, gen);
   }


   /**
    * Generates and returns the next observation at time <SPAN CLASS="MATH"><I>t</I><SUB>j+1</SUB> =</SPAN>
    *  <TT>nextTime</TT>. Assumes the previous observation time is <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN> defined 
    * earlier (either by this method or by <TT>setObservationTimes</TT>), 
    * as well as the value of the previous observation <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN>. 
    * <SPAN  CLASS="textit">Warning</SPAN>: This method will reset the observations time <SPAN CLASS="MATH"><I>t</I><SUB>j+1</SUB></SPAN>
    * for this process to <TT>nextTime</TT>. The user must make sure that
    * the <SPAN CLASS="MATH"><I>t</I><SUB>j+1</SUB></SPAN> supplied is 
    * <SPAN CLASS="MATH">&nbsp;&gt;=&nbsp;<I>t</I><SUB>j</SUB></SPAN>.
    * 
    */
   public double nextObservation() {
      double xOld = path[observationIndex];
      double x = xOld + (beta - xOld) * alphadt[observationIndex]
                 + sigmasqrdt[observationIndex] * gen.nextDouble();
      observationIndex++;
      path[observationIndex] = x;
      return x;
   }

   public double nextObservation (double nextTime) {
      double previousTime = t[observationIndex];
      double xOld = path[observationIndex];
      observationIndex++;
      t[observationIndex] = nextTime;
      double dt = nextTime - previousTime;
      double x = xOld + alpha * (beta - xOld) * dt
           + sigma * Math.sqrt (dt) * gen.nextDouble();
      path[observationIndex] = x;
      return x;
   }


   /**
    * Generates and returns an observation of the process 
    * in <TT>dt</TT> time units,
    * assuming that the process has value <SPAN CLASS="MATH"><I>x</I></SPAN> at the current time.
    * Uses the process parameters specified in the constructor.
    * Note that this method does not affect the sample path of the process 
    * stored internally (if any).
    * 
    */
   public double nextObservation (double x, double dt) {
      x = x + alpha * (beta - x) * dt
            + sigma * Math.sqrt (dt) * gen.nextDouble();
      return x;
    }


   /**
    * Generates a sample path of the process at all observation times,
    *  which are provided in array <TT>t</TT>.
    *  Note that <TT>t[0]</TT> should be the observation time of <TT>x0</TT>, 
    *  the initial value of the process, and <TT>t[]</TT> should have at least <SPAN CLASS="MATH"><I>d</I> + 1</SPAN>
    *  elements (see the <TT>setObservationTimes</TT> method).
    * 
    */
   public double[] generatePath() {
      double x;
      double xOld = x0;
      for (int j = 0; j < d; j++) {
          x = xOld + (beta - xOld)*alphadt[j] + sigmasqrdt[j]*gen.nextDouble();
          path[j + 1] = x;
          xOld = x;
      }
      observationIndex = d;
      return path;
   }


   protected void initArrays(int d) {
      double dt;
      for (int j = 0; j < d; j++) {
          dt = t[j+1] - t[j];
          alphadt[j]      = alpha * (dt);
          sigmasqrdt[j]   = sigma * Math.sqrt (dt);
      }
   }
}