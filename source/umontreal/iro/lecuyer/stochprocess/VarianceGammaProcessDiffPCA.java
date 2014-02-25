

/*
 * Class:        VarianceGammaProcessDiffPCA
 * Description:  
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       
 * @since        2008

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

package umontreal.iro.lecuyer.stochprocess;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.probdist.*;
import umontreal.iro.lecuyer.randvar.*;



/**
 * Same as {@link VarianceGammaProcessDiff}, but the two inner 
 * {@link GammaProcess}'es are of PCA type.  Also, 
 * <TT>generatePath(double[] uniforms01)</TT> distributes 
 * the uniform random variates to the {@link GammaProcessPCA}'s  according to their 
 * eigenvalues, i.e. the {@link GammaProcessPCA} with the higher eigenvalue 
 * gets the next uniform random number.  If one should decide to create a 
 * {@link VarianceGammaProcessDiffPCA} by giving two {@link GammaProcessPCA}'s to an 
 * objet of the class {@link VarianceGammaProcessDiff}, the uniform random 
 * numbers would not be given this way
 * to the {@link GammaProcessPCA}'s; this  might give less variance reduction when 
 * used with QMC.
 * 
 */
public class VarianceGammaProcessDiffPCA extends VarianceGammaProcessDiff  {
    int[] indexEigenUp;
    int[] indexEigenDw;




   /**
    * Constructs a new {@link VarianceGammaProcessDiffPCA} with 
    * parameters  
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> 
    * and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.  There is only
    * one {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} here which is
    * used for the two inner {@link GammaProcessPCA}'s.  The other
    * parameters are set as in {@link VarianceGammaProcessDiff}.
    * 
    */
   public VarianceGammaProcessDiffPCA (double s0, double theta,
                                       double sigma, double nu,
                                       RandomStream stream)  {
     super(s0, theta, sigma, nu, 
	  new GammaProcessPCA (0.0, 1.0, 1.0, stream),
	  new GammaProcessPCA (0.0, 1.0, 1.0, stream));
    // Params mu, nu of the 2 gamma processes are redefined in init()
    // which will be called after a call to 'setObservTimes'
}


   /**
    * Constructs a new {@link VarianceGammaProcessDiffPCA} with 
    * parameters  
    * <SPAN CLASS="MATH"><I>&#952;</I> = <texttt>theta</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#963;</I> = <texttt>sigma</texttt></SPAN>, 
    * <SPAN CLASS="MATH"><I>&#957;</I> = <texttt>nu</texttt></SPAN> 
    * and initial value 
    * <SPAN CLASS="MATH"><I>S</I>(<I>t</I><SUB>0</SUB>) = <texttt>s0</texttt></SPAN>.  As in 
    * {@link VarianceGammaProcessDiff}, the 
    * {@link umontreal.iro.lecuyer.rng.RandomStream RandomStream} of <TT>gneg</TT> is replaced by
    * the one of <TT>gpos</TT> to avoid any confusion.
    * 
    */
   public VarianceGammaProcessDiffPCA (double s0, double theta,
                                       double sigma, double nu,
                                       GammaProcessPCA gpos,
                                       GammaProcessPCA gneg)  {
    super(s0, theta, sigma, nu, gpos, gneg); // from VarianceGammaProcessDiff
    // Params mu, nu of the 2 gamma processes are redefined in init()
    // which will be called after a call to 'setObservTimes'
}
 

   /**
    * This method is not implemented is this class since
    * the path cannot be generated sequentially.
    * 
    */
   public double nextObservation()  {
        throw new UnsupportedOperationException 
        ("Impossible with PCA, use generatePath() instead.");
    }

 

   public double[] generatePath() {
        double[] u = new double[2*d];
        for(int i =0; i < 2*d; i++)
            u[i] = getStream().nextDouble();
        return generatePath(u);
    }

   public double[] generatePath(double[] uniform01)  {
        int dd = uniform01.length;
        int d = dd / 2;

        if(dd % 2 != 0){
            throw new IllegalArgumentException (
                     "The Array uniform01 must have a even length");
        }

        double[] QMCpointsUP = new double[d];
        double[] QMCpointsDW = new double[d];

        for(int i = 0; i < d; i++){
             QMCpointsUP[i] = uniform01[ indexEigenUp[i] ];
             QMCpointsDW[i] = uniform01[ indexEigenDw[i] ];
        }
        gpos.resetStartProcess();
        gneg.resetStartProcess();

        double[] pathUP = gpos.generatePath(QMCpointsUP);
        double[] pathDOWN = gneg.generatePath(QMCpointsDW);

        for (int i=0; i<d; i++) {
           path[i+1] = x0 + pathUP[i+1] - pathDOWN[i+1];
        }
        observationIndex   = d;
        observationCounter = d;
        return path;
    }


   protected void init() {
        super.init ();  // from VarianceGammaProcessDiff
	if( observationTimesSet){
        // Two lines below (casts) should be reinstated after fix inheritance PCA/PCABridge.
	    double[] eigenValUp = ((GammaProcessPCA)gpos).getBMPCA().getSortedEigenvalues();
	    double[] eigenValDw = ((GammaProcessPCA)gneg).getBMPCA().getSortedEigenvalues();
	    indexEigenUp = new int[d];
	    indexEigenDw = new int[d];

	    int iUp = 0;
	    int iDw = 0;
	    for(int iQMC = 0; iQMC < 2*d; iQMC++){
		if(iUp == d) {indexEigenDw[iDw] = iQMC; iDw++;continue;}
        if(iDw == d) {indexEigenUp[iUp] = iQMC; iUp++;continue;}
        if( eigenValUp[iUp] >= eigenValDw[iDw] ){
		    indexEigenUp[iUp] = iQMC; 
		    iUp++;
		}
		else{
		    indexEigenDw[iDw] = iQMC; 
		    iDw++;
		}
	    }
	}

    }

}

