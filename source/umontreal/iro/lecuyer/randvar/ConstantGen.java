

/*
 * Class:        ConstantGen
 * Description:  random variate generator for a constant distribution
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

package umontreal.iro.lecuyer.randvar;


/**
 * This class implements a random variate generator
 * that returns a constant value.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = 1,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = <I>c</I>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>p</I>(<I>x</I>) = 0</SPAN> elsewhere.
 * 
 */
public class ConstantGen extends RandomVariateGen  {
   private double val;



   /**
    * Constructs a new constant generator returning the given value
    * <TT>val</TT>.
    * 
    */
   public ConstantGen (double val) {
      this.val = val;
   }

   @Override
   public double nextDouble() {
      return val;
   }

   @Override
   public void nextArrayOfDouble (double[] v, int start, int n) {
      for (int i = 0; i < n; i++)
         v[start + i] = val;
   }

}