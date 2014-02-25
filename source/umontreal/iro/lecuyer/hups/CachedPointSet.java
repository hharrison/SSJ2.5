

/*
 * Class:        CachedPointSet
 * Description:  
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

    import umontreal.iro.lecuyer.rng.RandomStream;


/**
 * This container class caches a point set by precomputing
 * and storing its points locally in an array.
 * This can be used to speed up computations when using
 * a small low-dimensional point set more than once.
 * 
 */
public class CachedPointSet extends PointSet  {
   protected PointSet P;        // Original PointSet which is cached here.
   protected double x[][];      // Cached points.
   protected CachedPointSet() {}



   /**
    * Creates a new <TT>PointSet</TT> object that contains an array storing
    *    the first <TT>dim</TT> coordinates of the first <TT>n</TT> points of <TT>P</TT>.
    *    The original point set <TT>P</TT> itself is not modified.
    * 
    * @param P point set to be cached
    * 
    *    @param n number of points
    * 
    *    @param dim number of dimensions of the points
    * 
    * 
    */
   public CachedPointSet (PointSet P, int n, int dim)  {
      if (P.getNumPoints() < n)
         throw new IllegalArgumentException(
            "Cannot cache more points than in point set P.");
      if (P.getDimension() < dim)
         throw new IllegalArgumentException(
            "Cannot cache points with more coordinates than the dimension.");
      numPoints = n;
      this.dim = dim;
      this.P = P;
      init ();
   }

   protected void init () {
      PointSetIterator itr = P.iterator();
      x = new double[numPoints][dim];
      for (int i = 0; i < numPoints; i++)
         itr.nextPoint (x[i], dim);
   }


   /**
    * Creates a new <TT>PointSet</TT> object that contains an array storing
    *    the points of <TT>P</TT>.
    *    The number of points and their dimension are the same as in the
    *    original point set.  Both must be finite.
    * 
    * @param P point set to be cached
    * 
    */
   public CachedPointSet (PointSet P)  {
      numPoints = P.getNumPoints();
      dim = P.getDimension();
      if (numPoints == Integer.MAX_VALUE)
         throw new IllegalArgumentException(
            "Cannot cache infinite number of points");
      if (dim == Integer.MAX_VALUE)
         throw new IllegalArgumentException(
            "Cannot cache infinite dimensional points");
      this.P = P;
      init ();
   }


   /**
    * Add the shift to the contained point set and recaches the points. See the doc of the
    * overridden method {@link umontreal.iro.lecuyer.hups.PointSet#addRandomShift((int, int, RandomStream)) addRandomShift}<TT>(d1, d2, stream)</TT> in {@link PointSet}.
    * 
    */
   public void addRandomShift(int d1, int d2, RandomStream stream) {
        P.addRandomShift(d1, d2, stream);
        init();
   }


   /**
    * Randomizes the underlying point set using <TT>rand</TT> and
    * recaches the points.
    * 
    */
   public void randomize (PointSetRandomization rand) {
      P.randomize(rand);
      init();
   }


   public String toString() {
     StringBuffer sb = new StringBuffer ("Cached point set" +
          PrintfFormat.NEWLINE);
     sb.append (super.toString());
     sb.append (PrintfFormat.NEWLINE + "Cached point set information {"
                + PrintfFormat.NEWLINE);
     sb.append (P.toString());
     sb.append (PrintfFormat.NEWLINE + "}");
     return sb.toString();
   }

   public double getCoordinate (int i, int j) {
      return x[i][j];
   }

}

