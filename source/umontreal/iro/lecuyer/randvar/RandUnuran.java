

/*
 * Class:        RandUnuran
 * Description:  provides the access point to the C package UNURAN
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


/**
 * This internal class provides the access point to the C
 * package UNURAN.  It provides native methods used
 * by {@link UnuranContinuous}, {@link UnuranDiscrete} and
 * {@link UnuranEmpirical}.
 * 
 */
class RandUnuran {

   // These arrays are used to store precomputed uniforms
   // from main and auxiliary streams when generating
   // arrays of variates.
   protected double[] unifArray = null;
   protected double[] unifAuxArray = null;

   protected RandomStream mainStream;
   protected RandomStream auxStream;
   protected int nativeParams = 0;

   protected RandUnuran() {}

   protected native void init (String genStr);
   protected void finalize() {
      close();
   }

   public native void close();

   // random variate generation native methods
   protected native int getRandDisc (double u, int np);
   protected native double getRandCont (double u, int np);
   protected native void getRandVec (double u, int np, double[] vec);

   // random array of variates generation native methods
   protected native void getRandDiscArray (int np, double[] u, double[] uaux,
        int[] v, int start, int n);
   protected native void getRandContArray (int np, double[] u, double[] uaux,
        double[] v, int start, int n);
   
   // methods to query the type of distribution for error checking
   protected native boolean isDiscrete();
   protected native boolean isContinuous();
   protected native boolean isContinuousMultivariate();
   protected native boolean isEmpirical();
   protected native boolean isEmpiricalMultivariate();

   static {
      System.loadLibrary ("randvar");
   }
}