
/*
 * Class:        DoubleArrayComparator
 * Description:  Compares two double's arrays
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

import java.util.Comparator;


/**
 * An implementation of {@link java.util.Comparator} which compares two
 * <TT>double</TT> arrays by comparing their <TT>i</TT>-<SPAN  CLASS="textit">th</SPAN> element,
 * where <TT>i</TT> is given in the constructor.
 * Method <TT>compare(d1, d2)</TT> returns <SPAN CLASS="MATH">-1</SPAN>, <SPAN CLASS="MATH">0</SPAN>, or <SPAN CLASS="MATH">1</SPAN> depending on
 * whether <TT>d1[i]</TT> is less than, equal to, or greater than
 * <TT>d2[i]</TT>.
 * 
 */
public class DoubleArrayComparator implements Comparator<double[]>  {
   private int i;



   /**
    * Constructs a comparator, where <TT>i</TT> is the index
    *  used for the comparisons.
    * 
    * @param i index used for comparison
    * 
    */
   public DoubleArrayComparator (int i)  {
      this.i = i;
   }


   /**
    * Returns <SPAN CLASS="MATH">-1</SPAN>, <SPAN CLASS="MATH">0</SPAN>, or <SPAN CLASS="MATH">1</SPAN> depending on
    * whether  <TT>d1[i]</TT> is less than, equal
    * to, or greater than <TT>d2[i]</TT>.
    * 
    * @param d1 first array
    * 
    *    @param d2 second array
    * 
    * 
    */
   public int compare (double[] d1, double[] d2)  {
      if (i >= d1.length || i >= d2.length)
         throw new IllegalArgumentException("Comparing in a"+
                    "dimension larger than arary dimension");
      return (d1[i]< d2[i] ? -1 : (d1[i] > d2[i] ? 1 : 0));
   } 

}
