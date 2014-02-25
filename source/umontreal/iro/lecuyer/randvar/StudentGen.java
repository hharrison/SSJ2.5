

/*
 * Class:        StudentGen
 * Description:  Student-t random variate generators 
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

package umontreal.iro.lecuyer.randvar;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;


/**
 * This class implements methods for generating random variates from the
 * <EM>Student</EM> distribution with <SPAN CLASS="MATH"><I>n</I> &gt; 0</SPAN> degrees of freedom.
 * Its density function is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I> (<I>x</I>) = [<I>&#915;</I>((<I>n</I> + 1)/2)/(<I>&#915;</I>(<I>n</I>/2)(&pi;n)<SUP>1/2</SUP>)][1 + <I>x</I><SUP>2</SUP>/<I>n</I>]<SUP>-(n+1)/2</SUP>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for  - &#8734; &lt; <I>x</I> &lt; &#8734;,
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>&#915;</I>(<I>x</I>)</SPAN> is the gamma function defined in
 * {@link GammaGen}.
 * 
 * <P>
 * The <TT>nextDouble</TT> method simply calls <TT>inverseF</TT> on the
 * distribution.
 * 
 * <P>
 * The following table gives the CPU time needed to generate <SPAN CLASS="MATH">10<SUP>7</SUP></SPAN> Student
 *  random variates using the different implementations available in SSJ.
 *  The second test (Q) was made with the inverse in
 *   {@link StudentDistQuick},
 *  while the first test was made with the inverse in {@link StudentDist}.
 * These tests were made on a machine with processor AMD Athlon 4000, running
 * Red Hat Linux, with clock speed at 2400 MHz.
 * 
 * <P>
 * <DIV ALIGN="CENTER">
 * <TABLE CELLPADDING=3 BORDER="1">
 * <TR><TD ALIGN="LEFT">Generator</TD>
 * <TD ALIGN="CENTER">time in seconds</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>StudentGen</TT></TD>
 * <TD ALIGN="CENTER">22.4</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>StudentGen(Q)</TT></TD>
 * <TD ALIGN="CENTER">6.5</TD>
 * </TR>
 * <TR><TD ALIGN="LEFT"><TT>StudentPolarGen</TT></TD>
 * <TD ALIGN="CENTER">1.4</TD>
 * </TR>
 * </TABLE>
 * </DIV>
 * 
 */
public class StudentGen extends RandomVariateGen  {
   protected int n = -1;


   /**
    * Creates a Student random variate generator with
    *  <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public StudentGen (RandomStream s, int n)  {
      super (s, new StudentDist(n));
      setN (n);
   }


   /**
    * Creates a new generator for the Student distribution <TT>dist</TT>
    *      and stream <TT>s</TT>.
    * 
    */
   public StudentGen (RandomStream s, StudentDist dist)  {
      super (s, dist);
      if (dist != null)
         setN (dist.getN ());
   }


   /**
    * Generates a new variate from the Student distribution
    *    with <SPAN CLASS="MATH"><I>n</I> =</SPAN>&nbsp;<TT>n</TT> degrees of freedom, using stream <TT>s</TT>.
    * 
    */
   public static double nextDouble (RandomStream s, int n)  {
      return StudentDist.inverseF (n, s.nextDouble());
    }


   /**
    * Returns the value of <SPAN CLASS="MATH"><I>n</I></SPAN> for this object.
    * 
    * 
    */
   public int getN() {
      return n;
   }



   protected void setN (int nu) {
      if (nu <= 0)
         throw new IllegalArgumentException ("n <= 0");
      this.n = nu;
   }
}
