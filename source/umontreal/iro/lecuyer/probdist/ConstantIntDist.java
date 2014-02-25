

/*
 * Class:        ConstantIntDist
 * Description:  constant integer distribution
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

package umontreal.iro.lecuyer.probdist;


/**
 * Extends the class {@link UniformIntDist} for a <SPAN  CLASS="textit">constant</SPAN>
 * discrete distribution taking a single integer value with probability 1.
 * Its mass function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>p</I>(<I>x</I>) = 1,&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for <I>x</I> = <I>c</I>,
 * </DIV><P></P>
 * and <SPAN CLASS="MATH"><I>p</I>(<I>x</I>) = 0</SPAN> elsewhere.
 * 
 */
public class ConstantIntDist extends UniformIntDist {


   /**
    * Constructs a new constant distribution with probability 1 at <TT>c</TT>.
    * 
    * 
    */
   public ConstantIntDist (int c) {
      super (c, c);
   }


   public String toString () {
      return getClass().getSimpleName() + " : c = " + i;
   }

}
