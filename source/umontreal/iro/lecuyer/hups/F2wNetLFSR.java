

/*
 * Class:        F2wNetLFSR
 * Description:  digital net in base 2 starting from a linear feedback
                 shift register generator
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

package umontreal.iro.lecuyer.hups;  

import umontreal.iro.lecuyer.util.PrintfFormat;


/**
 * This class implements a digital net in base 2 starting from a
 * linear feedback shift register generator.  It is exactly the same
 * point set as the one defined in the class 
 * {@link umontreal.iro.lecuyer.hups.F2wCycleBasedLFSR F2wCycleBasedLFSR}. 
 *  See the description
 * of this class for more details on the way the point set is constructed.
 * 
 * <P>
 * Constructing a point set using this class, instead of using
 *  {@link umontreal.iro.lecuyer.hups.F2wCycleBasedLFSR F2wCycleBasedLFSR},
 * makes SSJ construct a digital net in base 2.  This is useful if one
 * wants to randomize using one of the randomizations included in the class
 * {@link umontreal.iro.lecuyer.hups.DigitalNet DigitalNet}.
 * 
 */
public class F2wNetLFSR extends DigitalNetBase2  
{
   private F2wStructure param;

    /* *
     * Constructs and stores the set of cycles for an LCG with
     *    modulus <SPAN CLASS="MATH"><I>n</I></SPAN> and multiplier <SPAN CLASS="MATH"><I>a</I></SPAN>.
     *   If pgcd<SPAN CLASS="MATH">(<I>a</I>, <I>n</I>) = 1</SPAN>, this constructs a full-period LCG which has two
     *   cycles, one containing 0 and one, the LCG period.
     *
     * @param n required number of points and modulo of the LCG
     *
     *    @param a generator <SPAN CLASS="MATH"><I>a</I></SPAN> of the LCG
     *
     *
     */



   /**
    * Constructs a point set with <SPAN CLASS="MATH">2<SUP>rw</SUP></SPAN> points.  See the description of the class
    * {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure} 
    * for the meaning of the parameters.
    * 
    */
   public F2wNetLFSR (int w, int r, int modQ, int step, int nbcoeff,
                      int coeff[], int nocoeff[], int dim)  
   {
      param = new F2wStructure (w, r, modQ, step, nbcoeff, coeff, nocoeff);
      param.initParamLFSR ();
      initNet (r, w, dim);
   }



   /**
    * Constructs a point set after reading its parameters from
    *    file <TT>filename</TT>; the parameters are located at line numbered <TT>no</TT>
    *    of <TT>filename</TT>. The available files are listed in the description of class
    * {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure}.
    * 
    */
   public F2wNetLFSR (String filename, int no, int dim)  
   {
      param = new F2wStructure (filename, no);
      param.initParamLFSR ();
      initNet (param.r, param.w, dim);
   }



   public String toString ()
   {
       String s = "F2wNetLFSR:" + PrintfFormat.NEWLINE;
       return s + param.toString ();
   }


   private void initNet (int r, int w, int dim)
   {
      numCols = r * w;
      numRows = 31;
      outDigits = 31;
      numPoints = (1 << numCols);
      this.dim = dim;
      normFactor = 1.0 / (1L << 31);
      genMat = new int[dim * numCols];

      for (int j = 0; j < numCols; j++) {
         param.state = 1 << (r * w - 1 - j);
         param.initF2wLFSR ();
         genMat[j] = param.output;
         for (int i = 1; i < dim; i++) {
            param.F2wLFSR ();
            genMat[i * numCols + j] = param.output;
         }
      }
   }
}

