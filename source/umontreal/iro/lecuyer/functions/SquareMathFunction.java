

/*
 * Class:        SquareMathFunction
 * Description:  function computing the square of another function
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
 * 
 * <SPAN CLASS="MATH">(<I>af</I> (<I>x</I>) + <I>b</I>)<SUP>2</SUP></SPAN> for a user-defined function
 * <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
 * 
 */
public class SquareMathFunction implements MathFunctionWithFirstDerivative  {
   private MathFunction func;
   private double a, b;


   /**
    * Constructs a new square function
    *  for function <TT>func</TT>.
    *  The values of the constants are
    *  <SPAN CLASS="MATH"><I>a</I> = 1</SPAN> and <SPAN CLASS="MATH"><I>b</I> = 0</SPAN>.
    * 
    * @param func the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    * 
    */
   public SquareMathFunction (MathFunction func) {
      this (func, 1, 0);
   }


   /**
    * Constructs a new power function
    *  for function <TT>func</TT>, and constants
    *  <TT>a</TT> and <TT>b</TT>.
    * 
    * @param func the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    *    @param a <
    * #30#>the multiplicative constant.
    *    @param b the additive constant.
    * 
    * 
    */
   public SquareMathFunction (MathFunction func, double a, double b) {
      if (func == null)
         throw new NullPointerException();
      this.func = func;
      this.a = a;
      this.b = b;
   }


   /**
    * Returns the function <SPAN CLASS="MATH"><I>f</I> (<I>x</I>)</SPAN>.
    * 
    * @return the function.
    * 
    */
   public MathFunction getFunction() {
      return func;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    * @return the value of <SPAN CLASS="MATH"><I>a</I></SPAN>.
    * 
    */
   public double getA() {
      return a;
   }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    * @return the value of <SPAN CLASS="MATH"><I>b</I></SPAN>.
    * 
    */
   public double getB() {
      return b;
   }


   public double evaluate (double x) {
      final double v = a*func.evaluate (x) + b;
      return v*v;
   }

   public double derivative (double x) {
      final double fder = MathFunctionUtil.derivative (func, x);
      return 2*a*(a*func.evaluate (x) + b)*fder;
   }
}
