

/*
 * Class:        ContinuousState
 * Description:  Represents the portion of the simulator's state associated
                 with continuous-time simulation.
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

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


/**
 * Represents the portion of the simulator's state associated with
 * continuous-time simulation.
 * Any simulator, including the default static one, can have an associate continuous state
 * which is obtained using the <TT>continuousState()</TT> method
 * of the {@link Simulator} class.
 * This state includes all active integration variables as well as the current integration method.
 * 
 * <P>
 * One of the methods {@link #selectEuler selectEuler}, {@link #selectRungeKutta2 selectRungeKutta2} or
 * {@link #selectRungeKutta4 selectRungeKutta4} must be called before starting 
 * any integration.
 * These methods permit one to select the numerical integration method 
 * and the step size <TT>h</TT> (in time units) that will be used
 * for <SPAN  CLASS="textit">all</SPAN> continuous-time variables linked to the simulator.
 * For all the methods, an integration step at time <SPAN CLASS="MATH"><I>t</I></SPAN> changes 
 * the values of the variables from their old values at time <SPAN CLASS="MATH"><I>t</I> - <I>h</I></SPAN> to their
 * new values at time <SPAN CLASS="MATH"><I>t</I></SPAN>.
 * 
 * <P>
 * Each integration step is scheduled as an event and added to the event list.
 * 
 */
public class ContinuousState  {


   // Integration methods
   public enum IntegMethod{ 
      EULER,            // Euler integration method
      RUNGEKUTTA2,      // Runge-Kutta integration method of order 2
      RUNGEKUTTA4       // Runge-Kutta integration method of order 4
   }

   private double stepSize;            // Integration step size.
   private IntegMethod integMethod;    // Integration method in use.
   private int order;                  // Order of the method in use.
   private double[] A = new double[4];
   private double[] B = new double[4];
   private double[] C = new double[4];

   // The event that actually executes integration steps.
   private StepEvent stepEv = null;

 // Class of event that executes an integration step.
   private class StepEvent extends Event {
      public StepEvent(Simulator sim) { super(sim); }
      public void actions() {
         switch (integMethod) {
            case EULER:       oneStepEuler();  break;
            case RUNGEKUTTA2: oneStepRK();  break;
            case RUNGEKUTTA4: oneStepRK();  break;
            default: throw new IllegalArgumentException 
                ("Integration step with undefined method");
         }
         this.schedule (stepSize);
         // if (afterInteg != null) afterInteg.actions();
      }

      public String toString() {
         return "Integration step for continuous variable ";
      }
   }

   private List<Continuous> list;
   private Simulator sim;


   /**
    * Creates a new {@link ContinuousState} object linked to the given simulator. 
    *  Usually, the user should not call this constructor directly since a new object 
    *  is created automatically by the <TT>continuousState()</TT> method of
    *  class {@link Simulator}.
    * 
    */
   protected ContinuousState (Simulator sim)  {
      this.list = new ArrayList<Continuous>();
      this.sim = sim;
      assert sim != null;
   }



   /**
    * Returns the list of continuous-time variables currently
    *   integrated by the simulator.
    *   The returned list is updated automatically as variables are added or removed, but it
    *   cannot be modified directly. One must instead use
    *   <TT>startInteg</TT> or <TT>stopInteg</TT> in class {@link Continuous} to add
    *   or remove variables.
    * 
    * 
    */
   public List<Continuous> getContinuousVariables() {
       return Collections.unmodifiableList (list);
   }


   /**
    * Starts the integration process that will change the state of
    *   {@link Continuous} variable at each integration step.
    * 
    */
   protected void startInteg(Continuous c) {
      // The following is done only the first time this method is called.
      if (stepEv == null) 
         stepEv = new StepEvent(sim);
      c.active = true;
      // Inserts this in list of active variables.
      if (list.isEmpty()) {
         stepEv.schedule (stepSize);
      }   // There was no active variable.
      list.add(c);
   }


   protected void stopInteg(Continuous c) {
      c.active = false;
      list.remove(c);
      if (list.isEmpty()) stepEv.cancel();
   }


   /**
    * Return an integer that represent the integration method in use.
    *  
    * @return Interger that represent the integration method in use.
    * 
    */
   public IntegMethod integMethod () {
      return integMethod;
   }


   /**
    * Selects the Euler method as the integration method,
    *   with the integration step size <TT>h</TT>, in time units.
    *  
    * @param h integration step, in simulation time units
    * 
    * 
    */
   public void selectEuler (double h) {
      integMethod = IntegMethod.EULER;
      stepSize = h;
   }


   /**
    * Selects a Runge-Kutta method of order&nbsp;2 as the integration
    *   method to be used, with step size <TT>h</TT>.
    *  
    * @param h integration step, in simulation time units
    * 
    * 
    */
   public void selectRungeKutta2 (double h) {
      integMethod = IntegMethod.RUNGEKUTTA2;
      stepSize = h;
      order = 2;
      A[0] = 1.0;  A[1] = 0.0;
      B[0] = 0.5;  B[1] = 0.5;
      C[0] = 0.0;  C[1] = 1.0;
   }


   /**
    * Selects a Runge-Kutta method of order&nbsp;4 as the integration
    *   method to be used, with step size <TT>h</TT>.
    *  
    * @param h integration step, in simulation time units
    * 
    * 
    */
   public void selectRungeKutta4 (double h) {
      integMethod = IntegMethod.RUNGEKUTTA4;
      stepSize = h;
      order = 4;
      A[0] = 0.5;  A[1] = 0.5;  A[2] = 1.0;  A[3] = 0.0;
      B[0] = 1.0/6.0;   B[1] = 1.0/3.0;
      B[2] = 1.0/3.0;   B[3] = 1.0/6.0;
      C[0] = 0.0;  C[1] = 0.5;  C[2] = 0.5;  C[3] = 1.0;
   }


   private void oneStepEuler()  {
     Continuous v;
      double t = sim.time() - stepSize;
      int current;
      current = list.size();
      while (current > 0) {
         v = list.get(--current);
         v.phi = v.value + stepSize * v.derivative (t);
      }
      current = list.size();
      while (current > 0) {
         v = list.get(--current);
         v.value = v.phi;
         if (v.ev != null) 
            v.ev.scheduleNext();
         v.afterEachStep();
      }
   }

   private void oneStepRK() {
      Continuous v;
      double t = sim.time() - stepSize;
      int current = list.size();
      while (current > 0) {
         v = list.get(--current);
         v.buffer = v.value;
         v.sum = 0.0;
         v.pi = 0.0;
      }
      for (int i = 1; i <= order-1; i++) {
         current = list.size();
         while (current > 0) {
            v = list.get(--current);
            v.pi = v.derivative (t + stepSize * C[i-1]);
            v.sum = v.sum + v.pi * B[i-1];
            v.phi = v.buffer + stepSize * v.pi * A[i-1];
         }
         current = list.size();
         while (current > 0) { 
            v = list.get(--current);
            v.value = v.phi;
         }
      } 
      current = list.size();
      while (current > 0) {
         v = list.get(--current);
         v.pi = v.derivative (t + stepSize * C[order-1]);
         v.value = v.buffer + stepSize * (v.sum + v.pi * B[order-1]);
         if (v.ev != null) v.ev.scheduleNext();
         v.afterEachStep();
      }
   }
}
