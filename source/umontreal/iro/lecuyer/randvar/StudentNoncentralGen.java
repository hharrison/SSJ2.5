

/*
 * Class:        StudentNoncentralGen
 * Description:  random variate generator for the noncentral Student-t distribution
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
 * This class implements random variate generators for the 
 * <SPAN  CLASS="textit">noncentral Student-t</SPAN> distribution with <SPAN CLASS="MATH"><I>n</I> &gt; 0</SPAN> degrees of freedom and
 * noncentrality parameter <SPAN CLASS="MATH"><I>&#948;</I></SPAN>. If <SPAN CLASS="MATH"><I>X</I></SPAN> is distributed according to a 
 * normal distribution with mean  <SPAN CLASS="MATH"><I>&#948;</I></SPAN> and variance 1, and <SPAN CLASS="MATH"><I>Y</I></SPAN> (statistically
 * independent of <SPAN CLASS="MATH"><I>X</I></SPAN>) is distributed according to a chi-square distribution 
 * with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom, then
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>T</I>&nbsp;' = <I>X</I>/(Y/n)<SUP>1/2</SUP>
 * </DIV><P></P>
 * has a noncentral <SPAN CLASS="MATH"><I>t</I></SPAN>-distribution with <SPAN CLASS="MATH"><I>n</I></SPAN> degrees of freedom and 
 * noncentrality parameter  <SPAN CLASS="MATH"><I>&#948;</I></SPAN>.
 * 
 */
public class StudentNoncentralGen extends RandomVariateGen  {
   private NormalGen normgen;
   private ChiSquareGen chigen;
   private int n;   // degrees of freedom of chi-square

   public double nextDouble()  {
      double x = normgen.nextDouble();
      double y = chigen.nextDouble();
      return x / Math.sqrt(y/n);
   }



   /**
    * Creates a <SPAN  CLASS="textit">noncentral-t</SPAN> random variate generator
    *  using normal generator <TT>ngen</TT> and chi-square generator <TT>cgen</TT>.
    * 
    */
   public StudentNoncentralGen (NormalGen ngen, ChiSquareGen cgen)  {
      super (null, null);
      setNormalGen (ngen);
      setChiSquareGen (cgen);
   }


   /**
    * Sets the normal generator to <TT>ngen</TT>.
    * 
    */
   public void setNormalGen (NormalGen ngen) {
      if (1.0 != ngen.getSigma())
         throw new IllegalArgumentException ("   variance of normal must be 1");
      normgen = ngen;
   }


   /**
    * Sets the chi-square generator to <TT>cgen</TT>.
    * 
    */
   public void setChiSquareGen (ChiSquareGen cgen) {
      chigen = cgen;
      n = cgen.getN();
   }


}

