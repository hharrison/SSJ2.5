

/*
 * Class:        FNoncentralGen
 * Description:  random variate generators for the noncentral F-distribution
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
 * <SPAN  CLASS="textit">noncentral F</SPAN>-distribution.
 * If <SPAN CLASS="MATH"><I>X</I></SPAN> is a noncentral chi-square random variable with <SPAN CLASS="MATH"><I>&#957;</I><SUB>1</SUB> &gt; 0</SPAN> degrees of 
 * freedom and noncentrality parameter 
 * <SPAN CLASS="MATH"><I>&#955;</I> &gt; 0</SPAN>, and <SPAN CLASS="MATH"><I>Y</I></SPAN> is a chi-square 
 * random variable (statistically independent of <SPAN CLASS="MATH"><I>X</I></SPAN>) with <SPAN CLASS="MATH"><I>&#957;</I><SUB>2</SUB> &gt; 0</SPAN> degrees
 *  of freedom, then
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>F</I>&nbsp;' = (<I>X</I>/<I>&#957;</I><SUB>1</SUB>)/(<I>Y</I>/<I>&#957;</I><SUB>2</SUB>)
 * </DIV><P></P>
 * has a noncentral <SPAN CLASS="MATH"><I>F</I></SPAN>-distribution.
 * 
 */
public class FNoncentralGen extends RandomVariateGen  {
   private ChiSquareNoncentralGen noncenchigen;
   private ChiSquareGen chigen;
   private double nu1;   // degrees of freedom of noncenchigen
   private int nu2;   // degrees of freedom of chigen

   public double nextDouble()  {
      double x = noncenchigen.nextDouble();
      double y = chigen.nextDouble();
      return (x * nu2) / (y * nu1);
   }



   /**
    * Creates a <SPAN  CLASS="textit">noncentral-F</SPAN> random variate generator
    *  using noncentral chi-square generator <TT>ncgen</TT> and 
    *  chi-square generator <TT>cgen</TT>.
    * 
    */
   public FNoncentralGen (ChiSquareNoncentralGen ncgen, ChiSquareGen cgen)  {
      super (null, null);
      setChiSquareNoncentralGen (ncgen);
      setChiSquareGen (cgen);
   }


   /**
    * Sets the noncentral chi-square generator to <TT>ncgen</TT>.
    * 
    */
   public void setChiSquareNoncentralGen (ChiSquareNoncentralGen ncgen) {
      nu1 = ncgen.getNu();
      noncenchigen = ncgen;
   }


   /**
    * Sets the chi-square generator to <TT>cgen</TT>.
    * 
    */
   public void setChiSquareGen (ChiSquareGen cgen) {
      nu2 = cgen.getN();
      chigen = cgen;
   }


}

