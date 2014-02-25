

/*
 * Class:        RandShiftedPointSet
 * Description:  Point set to which a random shift modulo 1 is applied
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
import umontreal.iro.lecuyer.rng.*;


/**
 * This container class embodies a point set to which a random shift
 * modulo 1 is applied (i.e., a single uniform random point is added
 * to all points, modulo 1, to randomize the inner point set).
 * When calling {@link #addRandomShift addRandomShift}, a new random shift will be generated.
 * This shift is represented by a vector of <SPAN CLASS="MATH"><I>d</I></SPAN> uniforms over <SPAN CLASS="MATH">(0, 1)</SPAN>,
 * where <SPAN CLASS="MATH"><I>d</I></SPAN> is the current dimension of the shift.
 * 
 */
public class RandShiftedPointSet extends ContainerPointSet  {

   protected double[] shift;           // The random shift.
   protected int dimShift = 0;         // Current dimension of the shift.
   protected int capacityShift = 0;    // Number of array elements;
                                       // always >= dimShift.
   protected RandomStream shiftStream; // Used to generate random shifts.



   /**
    * Constructs a structure to contain a randomly shifted version of <TT>P</TT>.
    *   The random shifts will be generated in up to <TT>dimShift</TT> dimensions,
    *   using stream <TT>stream</TT>.
    *  
    * @param P point set being randomized
    * 
    *    @param dimShift dimension of the initial shift
    * 
    *    @param stream stream used for generating random shifts
    * 
    */
   public RandShiftedPointSet (PointSet P, int dimShift, RandomStream stream) {
      init (P);
      if (dimShift <= 0) {
         throw new IllegalArgumentException (
            "Cannot construct RandShiftedPointSet with dimShift <= 0");
      }
      shiftStream = stream;
      shift = new double [dimShift];
      capacityShift = this.dimShift = dimShift;
   }


   /**
    * Returns the number of dimensions of the current random shift.
    * 
    */
   public int getShiftDimension() {
      return dimShift;
   }


   /**
    * Changes the stream used for the random shifts to <TT>stream</TT>, then
    *   refreshes the shift for coordinates <TT>d1</TT> to <TT>d2-1</TT>.
    * 
    */
   public void addRandomShift (int d1, int d2, RandomStream stream)  {
      if (null == stream)
         throw new IllegalArgumentException (
              PrintfFormat.NEWLINE +
                  "   Calling addRandomShift with null stream");
      if (stream != shiftStream)
         shiftStream = stream;
      addRandomShift (d1, d2);
   }


   /**
    * Changes the stream used for the random shifts to <TT>stream</TT>, then
    *   refreshes all coordinates of the random shift, up to its current dimension.
    * 
    * 
    */
   public void addRandomShift (RandomStream stream)  {
      if (stream != shiftStream)
         shiftStream = stream;
      addRandomShift (0, dimShift);
   }


   /**
    * Refreshes the random shift (generates new uniform values for the
    *   random shift coordinates) for coordinates <TT>d1</TT> to <TT>d2-1</TT>.
    * 
    */
   @Deprecated
   public void addRandomShift (int d1, int d2) {
      if (d1 < 0 || d1 > d2)
         throw new IllegalArgumentException ("illegal parameter d1 or d2");
      if (d2 > capacityShift) {
         int d3 = Math.max (4, capacityShift);
         while (d2 > d3)
            d3 *= 2;
         double[] temp = new double[d3];
         capacityShift = d3;
         for (int i = 0; i < d1; i++)
            temp[i] = shift[i];
         shift = temp;
      }
      dimShift = d2;
      for (int i = d1; i < d2; i++)
         shift[i] = shiftStream.nextDouble();

 // Just for testing, to see the single uniform random point
 //     for(int k = 0; k < d2; k++)
 //       System.out.println ("shift " + k + " = " + shift[k]);
 //     System.out.println();

   }


   @Deprecated
   public void addRandomShift()  {
      addRandomShift (0, dimShift);
   }


   public String toString() {
      return "RandShiftedPointSet of: {" + PrintfFormat.NEWLINE
              + P.toString() + PrintfFormat.NEWLINE + "}";
   }

   public PointSetIterator iterator() {
      return new RandShiftedPointSetIterator();
   }

   // ***************************************************************

   private class RandShiftedPointSetIterator
                 extends ContainerPointSetIterator {

      public double getCoordinate (int i, int j) {
         int d1 = innerIterator.getCurCoordIndex();
         if (dimShift <= d1)
            // Must extend randomization.
            addRandomShift (dimShift, 1 + d1);
         double u = P.getCoordinate(i, j) + shift[j];
         if (u >= 1.0)
            u -= 1.0;
         if (u > 0.0)
            return u;
         return EpsilonHalf;  // avoid u = 0
      }

      public double nextCoordinate() {
         int d1 = innerIterator.getCurCoordIndex();
         if (dimShift <= d1)
            addRandomShift (dimShift,1 + d1 );
         double u = shift [d1];
         u += innerIterator.nextCoordinate();
         if (u >= 1.0)
            u -= 1.0;
         if (u > 0.0)
            return u;
         return EpsilonHalf;  // avoid u = 0
      }

   }
}
