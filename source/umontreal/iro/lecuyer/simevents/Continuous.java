

/*
 * Class:        Continuous
 * Description:  provides the basic structures and tools for 
                 continuous-time simulation
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

package umontreal.iro.lecuyer.simevents;

/**
 * Represents a variable in a continuous-time simulation.
 * This abstract class provides the basic structures and tools
 * for continuous-time simulation, where certain variables evolve 
 * continuously in time, according to differential equations.
 * Such continuous variables can be mixed with events and processes.
 * 
 * <P>
 * Each type of continuous-time variable should be defined as a 
 * subclass of <TT>Continuous</TT>.
 * The instances of these subclasses are the actual continuous-time
 * variables.  Each subclass must implement the method
 * {@link #derivative derivative} which returns its derivative with respect to time.
 * The trajectory of this variable is determined by integrating this
 * derivative.
 * The subclass may also reimplement the method {@link #afterEachStep afterEachStep}, 
 * which is executed immediately after each integration step.
 * 
 * By default (in the class <TT>Continuous</TT>), this method does nothing.
 * This method could, for example, verify if the variable has reached
 * a given threshold, or update a graphical illustration of the variable
 * trajectory.
 * 
 * <P>
 * When creating a class representing a continuous variable,
 * the {@link #toString toString} method can be overridden to display
 * information about the continuous variable.  This information will
 * be displayed when formating the event list as a string.
 * 
 * <P>
 * Each continuous variable has a linked simulator represented by an instance of the
 * {@link Simulator} class.
 * If no simulator is provided explicitly when constructing a variable,
 * the default simulator returned by
 * <TT>Simulator.getDefaultSimulator</TT> is used.
 * 
 */
public abstract class Continuous  {

   // Private variables:

   boolean active; // This variable is currently being integrated.
   double  value;  // Current value of the variable.
   Event  ev;      // Event to be executed after each integ. step,

   //String name;
   double phi;
   double pi;
   double buffer;
   double sum;

   private Simulator sim;
  



   /**
    * Constructs a new continuous-time variable 
    *    linked to the default simulator, <EM>without</EM> initializing it.
    * 
    */
   public Continuous()  {
      active = false;
      this.sim = Simulator.getDefaultSimulator();
   } 


   /**
    * Constructs a new continuous-time variable linked to
    *      the given simulator, <EM>without</EM>
    *     initializing it.
    *    
    * @param sim the simulator associated to this variable.
    * 
    */
   public Continuous (Simulator sim)  {
       if (sim == null)
          throw new NullPointerException();
      active = false;
      this.sim = sim;
   } 


   /**
    * Initializes or reinitializes the continuous-time variable
    *    to <TT>val</TT>.
    *  
    * @param val initial value of the variable
    * 
    * 
    */
   public void init (double val)  {
      value = val;
   } 


   /**
    * Returns the current value of this continuous-time variable.
    *  
    * @return the current value of the variable
    * 
    */
   public double value()  {
      return value;
   } 


   /**
    * Returns the simulator linked to this continuous-time variable.
    *  
    * @return the current simulator of the variable
    * 
    */
   public Simulator simulator()  {
      return sim;
   } 


   /**
    * Sets the simulator linked to this continuous-time variable.
    *    This method should not be called while this variable is active.
    *  
    * @param sim the simulator of the current variable
    * 
    * 
    */
   public void setSimulator(Simulator sim)  {
       if (sim == null)
          throw new NullPointerException();
      this.sim = sim;
   } 


   /**
    * Starts the integration process that will change the state of
    *   this variable at each integration step.
    * 
    */
   public void startInteg()  {
      sim.continuousState().startInteg(this);
   } 


   /**
    * Same as {@link #startInteg startInteg}, after initializing the variable 
    *    to <TT>val</TT>.
    *  
    * @param val initial value to start integration from
    * 
    * 
    */
   public void startInteg (double val)  {
      init (val);   startInteg();
   } 


   /**
    * Stops the integration process for this continuous variable.
    *   The variable keeps the value it took at the last integration step
    *   before calling <TT>stopInteg</TT>.
    * 
    */
   public void stopInteg()  {
      sim.continuousState().stopInteg(this);
   } 


   /**
    * This method should return the derivative of this variable
    *    with respect to time, at time <SPAN CLASS="MATH"><I>t</I></SPAN>.
    *    Every subclass of <TT>Continuous</TT> that is to be instantiated 
    *    must implement it.
    *    If the derivative does not depend explicitly on time, <SPAN CLASS="MATH"><I>t</I></SPAN> becomes
    *    a dummy parameter.  Internally, the method is used with <SPAN CLASS="MATH"><I>t</I></SPAN> not
    *    necessarily equal to the current simulation time.
    *   
    * @param t time at which the derivative must be computed
    * 
    * 
    */
   public abstract double derivative (double t);


   /**
    * This method is executed after each integration step
    *    for this <TT>Continuous</TT> variable.
    *    Here, it does nothing, but every subclass of <TT>Continuous</TT> may
    *    reimplement it.
    * 
    */
   public void afterEachStep()  {
   } 


   /**
    * Selects the Euler method as the integration method,
    *   with the integration step size <TT>h</TT>, in time units, for the default simulator.
    *   The non-static method {@link #selectEuler selectEuler} in {@link ContinuousState}
    *   can be used to set the integration method for any given simulator.
    *   This method appears here only to keep compatibility with older versions of SSJ; using
    *   a non-static {@link Simulator} instance rather than the default simulator is recommended.
    *   
    * @param h integration step, in simulation time units
    * 
    * 
    */
   public static void selectEuler(double h)  {
      Simulator.getDefaultSimulator().continuousState().selectEuler(h);
   } 


   /**
    * Selects a Runge-Kutta method of order&nbsp;4 as the integration
    *   method to be used, with step size <TT>h</TT>.
    *   The non-static method {@link #selectRungeKutta4 selectRungeKutta4} in {@link ContinuousState}
    *   can be used to set the integration method for any given simulator.
    *   This method appears here only to keep compatibility with older versions of SSJ; using
    *   a non-static {@link Simulator} instance rather than the default simulator is recommended.
    * 
    */
   public static void selectRungeKutta4(double h)  {
      Simulator.getDefaultSimulator().continuousState().selectRungeKutta4(h);
   } 


   /**
    * Selects a Runge-Kutta method of order&nbsp;2 as the integration
    *   method to be used, with step size <TT>h</TT>.
    *   The non-static method {@link #selectRungeKutta2 selectRungeKutta2} in {@link ContinuousState}
    *   can be used to set the integration method for any given simulator.
    *   This method appears here only to keep compatibility with older versions of SSJ; using
    *   a non-static {@link Simulator} instance rather than the default simulator is recommended.
    * 
    */
   public static void selectRungeKutta2(double h)  {
      Simulator.getDefaultSimulator().continuousState().selectRungeKutta2(h);
   } 

}
