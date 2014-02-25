

/*
 * Class:        AverageMathFunction
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
 * Represents a function computing the average of several functions.
 * Let 
 * <SPAN CLASS="MATH"><I>f</I><SUB>0</SUB>(<I>x</I>),&#8230;, <I>f</I><SUB>n-1</SUB>(<I>x</I>)</SPAN> be a set of <SPAN CLASS="MATH"><I>n</I></SPAN> functions.
 * This function represents the average 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = <IMG
 *  ALIGN="MIDDLE" BORDER="0" SRC="AverageMathFunctionimg1.png"
 *  ALT="$\displaystyle {\frac{1}{n}}$">&sum;<SUB>i=0</SUB><SUP>n-1</SUP><I>f</I><SUB>i</SUB>(<I>x</I>).
 * </DIV><P></P>
 * 
 */
public class AverageMathFunction implements MathFunction

,
   MathFunctionWithFirstDerivative, MathFunctionWithDerivative,
   MathFunctionWithIntegral {
   private MathFunction[] func;


   /**
    * Constructs a function computing the average
    *  of the functions in the array <TT>func</TT>.
    * 
    * @param func the array of functions to average.
    * 
    * 
    */
   public AverageMathFunction (MathFunction... func) {
      if (func == null)
         throw new NullPointerException();
      this.func = func.clone ();
   }


   /**
    * Returns the functions being averaged.
    * 
    * @return the averaged functions.
    * 
    */
   public MathFunction[] getFunctions() {
      return func.clone ();
   }


   public double evaluate (double x) {
      double sum = 0;
      for (final MathFunction fi : func)
         sum += fi.evaluate (x);
      return sum / func.length;
   }
   
   public double derivative (double x, int n) {
      if (n < 0)
         throw new IllegalArgumentException ("n must be greater than or equal to 0");
      if (n == 0)
         return evaluate (x);
      double sum = 0;
      for (final MathFunction fi : func)
         sum += MathFunctionUtil.derivative (fi, x, n);
      return sum / func.length;
   }

   public double derivative (double x) {
      double sum = 0;
      for (final MathFunction fi : func)
         sum += MathFunctionUtil.derivative (fi, x);
      return sum / func.length;
   }

   public double integral (double a, double b) {
      double sum = 0;
      for (final MathFunction fi : func)
         sum += MathFunctionUtil.integral (fi, a, b);
      return sum / func.length;
   }
}
