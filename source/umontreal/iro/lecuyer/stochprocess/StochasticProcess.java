

/*
 * Class:        StochasticProcess
 * Description:  Base class for all stochastic processes
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
import umontreal.iro.lecuyer.rng.RandomStream;



/**
 * Abstract base class for a stochastic process 
 * <SPAN CLASS="MATH">{<I>X</I>(<I>t</I>) : <I>t</I>&nbsp;&gt;=&nbsp;0}</SPAN>
 * sampled (or observed) at a finite number of time points,
 * 
 * <SPAN CLASS="MATH">0 = <I>t</I><SUB>0</SUB> &lt; <I>t</I><SUB>1</SUB> &lt; <SUP> ... </SUP> &lt; <I>t</I><SUB>d</SUB></SPAN>.
 * The observation times are usually all specified before generating a sample path.
 * This can be done via <TT>setObservationTimes</TT>.
 * The method <TT>generatePath</TT> generates 
 * <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>1</SUB>),..., <I>X</I>(<I>t</I><SUB>d</SUB>)</SPAN> and memorizes
 * them in a vector, which can be recovered by <TT>getPath</TT>.
 * 
 * <P>
 * Alternatively, for some types of processes, the observations <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN> can be
 * generated sequentially, one at a time, by invoking  <TT>resetStartProcess</TT>
 * first, and then <TT>nextObservation</TT> repeatedly.
 * For some types of processes, the observation times can be specified one by one
 * as well, when generating the path.  This may be convenient or even necessary
 * if the observation times are random, for example.
 * 
 * <P>
 * <SPAN  CLASS="textbf">WARNING: After having called the constructor for one of the subclass,
 *  one must always set the observation times of the process,
 *  by calling method <TT>setObservationTimes</TT> for example or otherwise.</SPAN>
 * 
 */
public abstract class StochasticProcess  {
    // Used in some subclasses to make sure the 'setObservationTimes'
    // method has already been invoked before calling 'init'
    protected boolean observationTimesSet = false;

    protected double x0 = 0.0;       // Default Initial Value of the process
    protected int d = -1;             // Num. of observation times
    protected int observationIndex = 0; // Index of last generated obs time
    protected int observationCounter = 0; // Counts how many observations have
              // been generated so far. Useful when they are not generated in
              // chronological order.
    protected double[] t;            // Observation times
    protected double[] path;            // Observations of the process
    //protected RandomStream stream; // Random stream used to generate the process
    protected int[] observationIndexFromCounter; // Links counter# to index#



   /**
    * Sets the observation times of the process to a copy of <TT>T</TT>,
    * with <SPAN CLASS="MATH"><I>t</I><SUB>0</SUB> =</SPAN> <TT>T[0]</TT> and <SPAN CLASS="MATH"><I>t</I><SUB>d</SUB> =</SPAN> <TT>T[d]</TT>.
    * The size of <TT>T</TT> must be <SPAN CLASS="MATH"><I>d</I> + 1</SPAN>.
    * 
    */
   public void setObservationTimes (double[] T, int d) {
        if (d <= 0) throw new IllegalArgumentException (
                    "Number of observation times d <= 0");
        this.d = d;
        observationTimesSet = true;

        // Copy of the observation times
        this.t = new double[d+1];
        System.arraycopy (T, 0, this.t, 0, d+1);

        // Test chronological order
        for (int i = 0; i < d; i++) {
            if (T[i+1] < T[i])
                throw new IllegalArgumentException (
                     "Observation times T[] are not time-ordered");
        }

        // Construction of 'path' object
        // We do not do it in 'init()' because we don't have to change the
        // path object if the user only calls 'setParams'
        path = new double[d+1];

        /* Process specific initialization; usually precomputes quantities
             that depend on the observation times.   */
        init();
    } 


   /**
    * Sets equidistant observation times at 
    * <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB> = <I>j&#948;</I></SPAN>, for 
    * <SPAN CLASS="MATH"><I>j</I> = 0,..., <I>d</I></SPAN>, and <TT>delta</TT> = <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
    * 
    */
   public void setObservationTimes (double delta, int d) {
        t = new double[d+1];
        for (int i=0; i<=d; i++) t[i] = i*delta;
        setObservationTimes (t, d);
    } 


   /**
    * Returns a reference to the array that contains the observation times
    * 
    * <SPAN CLASS="MATH">(<I>t</I><SUB>0</SUB>,..., <I>t</I><SUB>d</SUB>)</SPAN>.
    * <SPAN  CLASS="textit">Warning</SPAN>: This method should only be used to <SPAN  CLASS="textit">read</SPAN> the observation times.
    * Changing the values in the array directly may have unexpected consequences.
    * The method <TT>setObservationTimes</TT> should be used to modify the observation times.
    * 
    */
   public double[] getObservationTimes()  {
        return t;
    }


   /**
    * Returns the number of observation times excluding the time <SPAN CLASS="MATH"><I>t</I><SUB>0</SUB></SPAN>.
    * 
    */
   public int getNbObservationTimes()  {
        return d;
    }


   /**
    * Generates, returns, and saves the sample path
    * 
    * <SPAN CLASS="MATH">{<I>X</I>(<I>t</I><SUB>0</SUB>), <I>X</I>(<I>t</I><SUB>1</SUB>),&#8230;, <I>X</I>(<I>t</I><SUB>d</SUB>)}</SPAN>. It can then be accessed via
    * <TT>getPath</TT>, <TT>getSubpath</TT>, or <TT>getObservation</TT>.
    * The generation method depends on the process type.
    * 
    */
   public abstract double[] generatePath();


   /**
    * Same as <TT>generatePath()</TT>, but first resets the stream to <TT>stream</TT>.
    * 
    */
   public double[] generatePath (RandomStream stream)  {
        setStream (stream);
        return generatePath();
    }


   /**
    * Returns a <SPAN  CLASS="textit">reference</SPAN> to the last generated sample path
    * 
    * <SPAN CLASS="MATH">{<I>X</I>(<I>t</I><SUB>0</SUB>),..., <I>X</I>(<I>t</I><SUB>d</SUB>)}</SPAN>.
    * <SPAN  CLASS="textit">Warning</SPAN>: The returned array and its size should not be modified,
    * because this is the one that memorizes the observations (not a copy of it).
    * To obtain a copy, use <TT>getSubpath</TT> instead.
    * 
    */
   public double[] getPath()  {
        return path;
    }


   /**
    * Returns in <TT>subpath</TT> the values of the process at a subset of the observation times,
    * specified as the times <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN> whose indices <SPAN CLASS="MATH"><I>j</I></SPAN> are in the array <TT>pathIndices</TT>.
    * The size of <TT>pathIndices</TT> should be at least as much as that of <TT>subpath</TT>.
    * 
    */
   public void getSubpath (double[] subpath, int[] pathIndices)  {
        for (int j=0; j<subpath.length; j++) {
            subpath[j] = path[pathIndices[j]];
        }
    }


   /**
    * Returns <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN> from the current sample path.
    * <SPAN  CLASS="textit">Warning</SPAN>: If the observation <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN> for the current path has not yet been
    * generated, then the value returned is unpredictable.
    * 
    */
   public double getObservation (int j)  {
        return path[j];
    }


   /**
    * Resets the observation counter to its initial value <SPAN CLASS="MATH"><I>j</I> = 0</SPAN>, so
    * that the current observation <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN> becomes <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>0</SUB>)</SPAN>. This method should
    *  be invoked before generating observations sequentially one by one
    * via {@link #nextObservation nextObservation}, for a new sample path.
    * 
    */
   public void resetStartProcess()  {
        observationIndex   = 0;
        observationCounter = 0;
    }


   /**
    * Returns <TT>true</TT> if <SPAN CLASS="MATH"><I>j</I> &lt; <I>d</I></SPAN>, where <SPAN CLASS="MATH"><I>j</I></SPAN> is the number of observations of the current
    * sample path generated since the last call to {@link #resetStartProcess resetStartProcess}.
    * Otherwise returns <TT>false</TT>.
    * 
    */
   public boolean hasNextObservation()  {
        if (observationCounter < d) return true;
        else return false;
    }


   /**
    * Generates and returns the next observation <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN> of the stochastic process.
    * The processes are usually sampled <SPAN  CLASS="textit">sequentially</SPAN>, i.e.
    * if the last observation generated was for time <SPAN CLASS="MATH"><I>t</I><SUB>j-1</SUB></SPAN>, the next observation
    * returned will be for time <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN>.
    * In some cases, subclasses extending this abstract class
    * may use non-sequential sampling algorithms (such as bridge sampling).
    * The order of generation of the <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN>'s is then specified by the subclass.
    * All the processes generated using principal components analysis (PCA) do not have
    * this method.
    * 
    */
   public double nextObservation() {
        throw new UnsupportedOperationException("Method not defined in this class");
    }


   /**
    * Returns the value of the index <SPAN CLASS="MATH"><I>j</I></SPAN> corresponding to
    * the time <SPAN CLASS="MATH"><I>t</I><SUB>j</SUB></SPAN> of the last generated observation.
    * 
    */
   public int getCurrentObservationIndex()  {
        return observationIndex;
    }


   /**
    * Returns the value of the last generated observation <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>j</SUB>)</SPAN>.
    * 
    */
   public double getCurrentObservation()  {
        return path[observationIndex];
    }


   /**
    * Returns the initial value <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>0</SUB>)</SPAN> for this process.
    * 
    */
   public double getX0()  {
        return x0;
    }


   /**
    * Sets the initial value <SPAN CLASS="MATH"><I>X</I>(<I>t</I><SUB>0</SUB>)</SPAN> for this process to <TT>s0</TT>,
    * and reinitializes.
    * 
    */
   public void setX0 (double s0)  {
        x0 = s0;
        init();
    }


   /**
    * Resets the random stream of the underlying generator to <TT>stream</TT>.
    * 
    */
   public abstract void setStream (RandomStream stream);


   /**
    * Returns the random stream of the underlying generator.
    * 
    */
   public abstract RandomStream getStream();


    /* ** Called by 'setObservationTimes' to initialize arrays and precompute
         constants to speed up execution. See overriding method 'init'
         in subclasses for details ***/
    protected void init() {
        if (observationTimesSet) //   If observation times are not defined, do nothing.
           path[0] = x0;
           // We do this here because the s0 parameter may have changed through
           // a call to the 'setParams' method.
    }

   /**
    * Returns a reference to an array that maps an integer <SPAN CLASS="MATH"><I>k</I></SPAN>
    * to <SPAN CLASS="MATH"><I>i</I><SUB>k</SUB></SPAN>, the index of the observation 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>i<SUB>k</SUB></SUB>)</SPAN> corresponding
    * to the <SPAN CLASS="MATH"><I>k</I></SPAN>-th observation to be generated for a sample path of this process.
    * If this process is sampled sequentially, then this map is trivial
    * (i.e. <SPAN CLASS="MATH"><I>i</I><SUB>k</SUB> = <I>k</I></SPAN>). But it can be useful in a more general setting where
    * the process is not sampled sequentially
    * (for example, by a Brownian or gamma bridge) and one wants to know which
    * observations of the current sample path were previously generated
    * or will be generated next.
    * 
    */
   public int[] getArrayMappingCounterToIndex()  {
        return observationIndexFromCounter;
    }
 
} 
