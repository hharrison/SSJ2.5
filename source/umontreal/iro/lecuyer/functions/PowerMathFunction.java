

/*
 * Class:        PowerMathFunction
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Éric Buist
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

package umontreal.iro.lecuyer.functions;



/**
 * Represents a function computing 
 * <SPAN CLASS="MATH">(<I>af</I> (<I>x</I>) + <I>b</I>)<SUP>p</SUP></SPAN> for a user-defined function
 * <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN> and power <SPAN CLASS="MATH"><I>p</I></SPAN>.
 * 
 */
public class PowerMathFunction implements MathFunction

,
      MathFunctionWithFirstDerivative {
   private MathFunction func;
   private double a, b;
   private double power;


   /**
    * Constructs a new power function for function <TT>func</TT> and power
    *  <TT>power</TT>. The values of the constants are <SPAN CLASS="MATH"><I>a</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>b</I> = 0</SPAN>.
    * 
    * @param func the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    *    @param power the power <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * 
    */
   public PowerMathFunction (MathFunction func, double power) {
      this (func, 1, 0, power);
   }


   /**
    * Constructs a new power function for function <TT>func</TT>, power
    *  <TT>power</TT>, and constants <TT>a</TT> and <TT>b</TT>.
    * 
    * @param func the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    *    @param power the power <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    *    @param a the multiplicative constant.
    * 
    *    @param b the additive constant.
    * 
    * 
    */
   public PowerMathFunction (MathFunction func, double a, double b, double power) {
      if (func == null)
         throw new NullPointerException ();
      this.func = func;
      this.a = a;
      this.b = b;
      this.power = power;
   }


   /**
    * Returns the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    * @return the function.
    * 
    */
   public MathFunction getFunction () {
      return func;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    * @return the value of <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    */
   public double getA () {
      return a;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    * @return the value of <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    */
   public double getB () {
      return b;
   }


   /**
    * Returns the power <SPAN CLASS="MATH"><I>p</I></SPAN>.
    * 
    * @return the power.
    * 
    */
   public double getPower () {
      return power;
   }


   public double derivative (double x) {
      final double fder = MathFunctionUtil.derivative (func, x);
      return getA()*getPower()*Math.pow (getA() * func.evaluate (x) + getB(), getPower() - 1)*fder;
   }

   public double evaluate (double x) {
      final double v = func.evaluate (x);
      return Math.pow (a * v + b, power);
   }
}
