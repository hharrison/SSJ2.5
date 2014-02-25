
/*
 * Class:        MultivariateFunction
 * Description:  Represents a function of multiple variables.
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
 * Represents a function of multiple variables.
 * This interface specifies a method <TT>evaluate</TT> that computes
 * a 
 * <SPAN CLASS="MATH"><I>g</I>(<B>x</B>)</SPAN> function, where 
 * <SPAN CLASS="MATH"><B>x</B> = (<I>x</I><SUB>0</SUB>,&#8230;, <I>x</I><SUB>d-1</SUB>)&#8712;<B>R</B><SUP>d</SUP></SPAN>.  It also specifies
 * a method <TT>evaluateGradient</TT> for computing
 * its gradient 
 * <SPAN CLASS="MATH">&#8711;<I>g</I>(<B>x</B>)</SPAN>.
 * 
 * <P>
 * The dimension <SPAN CLASS="MATH"><I>d</I></SPAN> can be fixed or variable.  When <SPAN CLASS="MATH"><I>d</I></SPAN> is fixed, the
 * methods specified by this interface always take the same number of
 * arguments.  This is the case, for example, with a ratio of two
 * variables.
 * When <SPAN CLASS="MATH"><I>d</I></SPAN> is variable, the implementation can compute the
 * function for a vector 
 * <SPAN CLASS="MATH"><B>x</B></SPAN> of any length.  This can happen for a
 * product or sum of variables.
 * 
 * <P>
 * The methods of this interface take a variable number of arguments to
 * accomodate the common case of fixed dimension with more convenience;
 * the programmer can call the method without creating an array.
 * For the generic case, however, one can replace the arguments with an
 * array.
 * 
 */
public interface MultivariateFunction {

   /**
    * Returns <SPAN CLASS="MATH"><I>d</I></SPAN>, the dimension of the function computed
    *    by this implementation.  If the dimension is not fixed,
    *    this method must return a negative value.
    * 
    * @return the dimension.
    * 
    */
   public int getDimension();


   /**
    * Computes the function 
    * <SPAN CLASS="MATH"><I>g</I>(<B>x</B>)</SPAN>
    *  for the vector <TT>x</TT>.  The length of the
    *  given array must correspond to the dimension of
    *  this function.  The method must compute and return the result
    *  of the function without modifying the elements
    *  in <TT>x</TT> since the array can be reused for
    *  further computation.
    * 
    * @param x a vector 
    * <SPAN CLASS="MATH"><B>x</B></SPAN>.
    * 
    *    @return the value of 
    * <SPAN CLASS="MATH"><I>g</I>(<B>x</B>)</SPAN>.
    *    @exception NullPointerException if <TT>x</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>x.length</TT>
    *     does not correspond to the dimension of this function.
    * 
    * 
    */
   public double evaluate (double... x);


   /**
    * Computes 
    * <SPAN CLASS="MATH">&#8706;<I>g</I>(<B>x</B>)/&#8706;<I>x</I><SUB>i</SUB></SPAN>,
    *  the derivative of 
    * <SPAN CLASS="MATH"><I>g</I>(<B>x</B>)</SPAN>
    *  with respect to <SPAN CLASS="MATH"><I>x</I><SUB>i</SUB></SPAN>.    The length of the
    *  given array must correspond to the dimension of
    *  this function.  The method must compute and return the result
    *  of the derivative without modifying the elements
    *  in <TT>x</TT> since the array can be reused for
    *  further computations, e.g., the gradient 
    * <SPAN CLASS="MATH">&#8711;<I>g</I>(<B>x</B>)</SPAN>.
    * 
    * @param i the variable to derive with respect to.
    * 
    *    @param x a vector 
    * <SPAN CLASS="MATH"><B>x</B></SPAN>.
    * 
    *    @return the value of the partial derivative.
    *    @exception NullPointerException if <TT>x</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>x.length</TT>
    *     does not correspond to the dimension of this function.
    * 
    *    @exception IndexOutOfBoundsException if <TT>i</TT> is negative
    *     or greater than or equal to the dimension of this function.
    * 
    * 
    */
   public double evaluateGradient (int i, double... x);
}
