

/*
 * Class:        Accumulate
 * Description:  collects statistics on a variable that evolves in
                 simulation time
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
// This class doesn't belong to package stat because objects of this class
// always depend of Simulator

import java.util.Observable;
import umontreal.iro.lecuyer.util.PrintfFormat;
import umontreal.iro.lecuyer.stat.StatProbe;


/**
 * A subclass of {@link umontreal.iro.lecuyer.stat.StatProbe StatProbe},
 * for collecting statistics on a
 * variable that evolves in simulation time, with a piecewise-constant trajectory.
 * Each time the variable changes its value, the method {@link #update(double) update}
 * must be called to inform the probe of the new value.
 * The probe can be reinitialized by {@link #init init}.
 * 
 */
public class Accumulate extends StatProbe implements Cloneable  {

   private double initTime;    // Initialization time.
   private double lastTime;    // Last update time.
   private double lastValue;   // Value since last update.
   private Simulator sim;



   /**
    * Constructs a new <TT>Accumulate</TT> statistical probe using the
    *   default simulator and initializes it by invoking <TT>init()</TT>.
    * 
    */
   public Accumulate()  {
      super();
      sim = Simulator.getDefaultSimulator();
      init();
   } 


   /**
    * Constructs a new <TT>Accumulate</TT> statistical probe linked to
    *     the given simulator,
    *    and initializes it by invoking <TT>init()</TT>.
    *  
    *  @param inSim the simulator of the current variable
    * 
    * 
    */
   public Accumulate (Simulator inSim)  {
      super();
      if (inSim == null)
          throw new NullPointerException();
      sim = inSim;
      init();
   } 


   /**
    * Construct and initializes a new <TT>Accumulate</TT>
    *    statistical probe with name <TT>name</TT> and initial time 0, using the default simulator.
    * 
    */
   public Accumulate (String name)  {
      super();
      sim = Simulator.getDefaultSimulator();
      this.name = name;
      init();
   } 


   /**
    * Construct and initializes a new <TT>Accumulate</TT>
    *    statistical probe with name <TT>name</TT> and initial time 0.
    *  
    * @param name descriptive name for the probe
    * 
    *    @param inSim the simulator of the current variable
    * 
    */
   public Accumulate (Simulator inSim, String name)  {
      super();
      if (inSim == null)
          throw new NullPointerException();
      sim = inSim;
      this.name = name;
      init();
   } 


   /**
    * Initializes the statistical collector and puts the current
    *    value of the corresponding variable to 0.
    * 
    */
   public void init()  {
       maxValue = Double.MIN_VALUE;
       minValue = Double.MAX_VALUE;
       lastValue = 0.0;
       sumValue = 0.0;
       initTime = lastTime = sim.time();
   } 


   /**
    * Same as {@link #init init} followed by {@link #update(double) update}<TT>(x)</TT>.
    *  
    * @param x initial value of the probe
    * 
    * 
    */
   public void init (double x)  {
       init();  update (x);
   } 


   /**
    * Updates the accumulator using the last value passed
    *   to {@link #update(double) update}.
    * 
    */
   public void update() {
      update (lastValue);
   }


   /**
    * Gives a new observation <TT>x</TT> to the statistical collector.
    *    If broadcasting to observers is activated for this object,
    *    this method will also transmit the new information to the
    *    registered observers by invoking the methods
    * {@link #notifyListeners(double) notifyListeners}.
    * 
    * @param x new observation given to the probe
    * 
    * 
    */
   public void update (double x)  {
      if (collect) {
         double time = sim.time();
         if (x < minValue) minValue = x;
         if (x > maxValue) maxValue = x;
         sumValue += lastValue * (time - lastTime);
         lastValue = x;
         lastTime = time;
      }
      if (broadcast) {
         //setChanged();
         notifyListeners (x);
      }
   }


   public double sum()  {
      update (lastValue);
      return sumValue;
   } 

   /**
    * Returns the time-average since the last initialization
    *     to the last call to <TT>update</TT>.
    * 
    */
   public double average()  {
      update (lastValue);
      double periode = lastTime - initTime;
      if (periode > 0.0)  return sumValue/periode;
      else  return 0.0;
   }

   public String shortReportHeader() {
      PrintfFormat pf = new PrintfFormat();
      pf.append (-9, "from time").append ("   ");
      pf.append (-9, "to time").append ("   ");
      pf.append (-8, "   min").append ("   ");
      pf.append (-8, "   max").append ("   ");
      pf.append (-10, " average");
      return pf.toString();
   }

   public String shortReport() {
      update();
      PrintfFormat pf = new PrintfFormat();
      pf.append (9, 2, 2, getInitTime()).append ("   ");
      pf.append (9, 2, 2, getLastTime()).append ("   ");
      pf.append (8, 3, 2, min()).append ("   ");
      pf.append (8, 3, 2, max()).append ("   ");
      pf.append (10, 3, 2, average());
      return pf.toString();
   }



   public String report()  {
      update (lastValue);
      PrintfFormat str = new PrintfFormat();
      str.append ("REPORT on Accumulate stat. collector ==> " + name);
      str.append (PrintfFormat.NEWLINE + "      from time   to time       min         max");
      str.append ("         average").append(PrintfFormat.NEWLINE);
      str.append (12, 2, 2, initTime);
      str.append (13, 2, 2, lastTime);
      str.append (11, 3, 2, minValue);
      str.append (12, 3, 2, (double)maxValue);
      str.append (14, 3, 2, (double)average()).append (PrintfFormat.NEWLINE);

      return str.toString();
    }


   /**
    * Returns the initialization time for this object.
    *   This is the simulation time when {@link #init init} was called for
    *   the last time.
    * 
    * @return the initialization time for this object
    * 
    */
   public double getInitTime() {
      return initTime;
   }


   /**
    * Returns the last update time for this object.
    *    This is the simulation time of the last call to {@link #update update} or
    *    the initialization time if {@link #update update} was never called after
    *    {@link #init init}.
    * 
    * @return the last update time of this object
    * 
    */
   public double getLastTime() {
      return lastTime;
   }


   /**
    * Returns the value passed to this probe by the last call
    *    to its {@link #update update} method (or the initial value if
    *    {@link #update update} was never called after {@link #init init}).
    * 
    * @return the last update value for this object
    * 
    */
   public double getLastValue() {
      return lastValue;
   }


   /**
    * Returns the simulator associated with this statistical probe.
    *  
    * @return the associated simulator.
    * 
    */
   public Simulator simulator()  {
      return sim;
   } 


   /**
    * Sets the simulator associated with this probe to <TT>sim</TT>.
    *    One should call {@link #init init} after this method to reset the statistical probe.
    *  
    * @param sim the simulator of this probe
    * 
    * 
    */
   public void setSimulator(Simulator sim)  {
       if (sim == null)
          throw new NullPointerException();
      this.sim = sim;
   } 


   /**
    * Clone this object.
    * 
    */
   public Accumulate clone() {
      try {
         return (Accumulate)super.clone();
      } catch (CloneNotSupportedException e) {
         throw new IllegalStateException ("Accumulate can't clone");
      }
   }

}
