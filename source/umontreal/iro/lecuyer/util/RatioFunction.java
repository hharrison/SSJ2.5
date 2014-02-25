

/*
 * Class:        RatioFunction
 * Description:  Represents a function computing a ratio of two values.
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

package umontreal.iro.lecuyer.util;



/**
 * Represents a function computing a ratio of two values.
 * 
 */
public class RatioFunction implements MultivariateFunction {
   private double zeroOverZero = Double.NaN;



   /**
    * Constructs a new ratio function.
    * 
    */
   public RatioFunction() {}


   /**
    * Constructs a new ratio function that returns
    *    <TT>zeroOverZero</TT> for the special case of <SPAN CLASS="MATH">0/0</SPAN>.
    *    See the {@link #getZeroOverZeroValue getZeroOverZeroValue}  method for more information.
    *    The default value of <TT>zeroOverZero</TT> is <TT>Double.NaN</TT>.
    * 
    * @param zeroOverZero the value for <SPAN CLASS="MATH">0/0</SPAN>.
    * 
    * 
    */
   public RatioFunction (double zeroOverZero) {
      this.zeroOverZero = zeroOverZero;
   }


   /**
    * Returns the value returned by {@link #evaluate((double[])) evaluate} in the
    *  case where the <SPAN CLASS="MATH">0/0</SPAN> function is calculated.
    *  The default value for <SPAN CLASS="MATH">0/0</SPAN> is <TT>Double.NaN</TT>.
    * 
    * <P>
    * Generally, <SPAN CLASS="MATH">0/0</SPAN> is undefined, and therefore associated with the <TT>Double.NaN</TT>
    *   constant, meaning <SPAN  CLASS="textit">not-a-number</SPAN>.
    *  However, in certain applications, it can be defined differently to accomodate some special cases.
    *  For exemple, in a queueing system, if there are no arrivals, no customers are served,
    *  lost, queued, etc.  As a result, many performance measures of interest turn out to be
    *  <SPAN CLASS="MATH">0/0</SPAN>.  Specifically, the loss probability, i.e., the ratio of
    *  lost customers over the number of arrivals, should be 0 if there is no arrival;
    *  in this case, <SPAN CLASS="MATH">0/0</SPAN> means 0.
    *  On the other hand, the service level, i.e., the fraction of customers waiting less than
    *  a fixed threshold, could be fixed to 1 if there is no arrival.
    * 
    * @return the value for <SPAN CLASS="MATH">0/0</SPAN>.
    * 
    */
   public double getZeroOverZeroValue() {
      return zeroOverZero;
   }


   /**
    * Sets the value returned by {@link #evaluate((double[])) evaluate} for
    *  the undefined function <SPAN CLASS="MATH">0/0</SPAN> to <TT>zeroOverZero</TT>.
    *  See {@link #getZeroOverZeroValue getZeroOverZeroValue} for more information.
    * 
    * @param zeroOverZero the new value for <SPAN CLASS="MATH">0/0</SPAN>.
    * 
    * 
    */
   public void setZeroOverZeroValue (double zeroOverZero) {
      this.zeroOverZero = zeroOverZero;
   }


   public int getDimension() {
      return 2;
   }

   public double evaluate (double... x) {
      if (x.length != 2)
         throw new IllegalArgumentException
            ("Invalid length of x");
      if (x[0] == 0 && x[1] == 0)
         return zeroOverZero;
      return x[0]/x[1];
   }

   public double evaluateGradient (int i, double... x) {
      if (x.length != 2)
         throw new IllegalArgumentException
            ("Invalid length of x");
      switch (i) {
      case 0: return 1.0/x[1];
      case 1: return -x[0]/(x[1]*x[1]);
      default: throw new IndexOutOfBoundsException
         ("Invalid value of i: " + i); 
      }
   }
}
