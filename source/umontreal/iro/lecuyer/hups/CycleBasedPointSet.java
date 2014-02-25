

/*
 * Class:        CycleBasedPointSet
 * Description:  provides the basic structures for storing and manipulating
                 a highly uniform point set defined by a set of cycles
 defined by a set of cycles.
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

import umontreal.iro.lecuyer.util.*;
import umontreal.iro.lecuyer.rng.RandomStream;
import cern.colt.list.*;


/**
 * This abstract class provides the basic structures for
 * storing and manipulating a <SPAN  CLASS="textit">highly uniform point set</SPAN>
 *  defined by a set of cycles.
 * The <SPAN CLASS="MATH"><I>s</I></SPAN>-dimensional points are all the vectors of <SPAN CLASS="MATH"><I>s</I></SPAN> successive
 * values found in any of the cycles, from any starting point.
 * Since this is defined for any positive integer <SPAN CLASS="MATH"><I>s</I></SPAN>, the points
 * effectively have an infinite number of dimensions.
 * The number of points, <SPAN CLASS="MATH"><I>n</I></SPAN>, is the sum of lengths of all the cycles.
 * The cycles of the point set are simply stored as a list of arrays,
 * where each array contains the successive values for a given cycle.
 * By default, the values are stored in <TT>double</TT>.
 * 
 * <P>
 * This structure is convenient for implementing recurrence-based point
 * sets, where the point set in <SPAN CLASS="MATH"><I>s</I></SPAN> dimensions is defined as
 * the set of all vectors of <SPAN CLASS="MATH"><I>s</I></SPAN> successive values of a periodic recurrence,
 * from all its possible initial states.
 * 
 */
public abstract class CycleBasedPointSet extends PointSet  {

   protected int numCycles = 0;     // Total number of cycles.
   // dim = Integer.MAX_VALUE;      // Dimension is infinite.
   private double[] shift;          // Random shift, initially null.
                                    // Entry j is for dimension j.
   protected ObjectArrayList cycles = new ObjectArrayList(); // List of cycles.




   public double getCoordinate (int i, int j) {
      // Find cycle that contains point i, then index in cycle.
      int l = 0;         // Length of next cycle.
      int n = 0;         // Total length of cycles added so far.
      int k;
      for (k = 0;  n <= i;  k++)
         n += l = ((AbstractList) cycles.get (k)).size();
      AbstractList curCycle = (AbstractList) cycles.get (k-1);
      int coordinate = (i - n + l + j) % curCycle.size();
//      double[] curCycleD = ((DoubleArrayList) curCycle).elements();
//      return curCycleD[coordinate];
      double x = ((DoubleArrayList) curCycle).get (coordinate);
      return x;
   }

   /**
    * Adds a random shift to all the points
    *   of the point set, using stream <TT>stream</TT> to generate the random numbers,
    *   for coordinates <TT>d1</TT> to <TT>d2 - 1</TT>. This applies an addition modulo 1
    *   of a single random point to all the points.
    * 
    * @param stream Stream used to generate random numbers
    * 
    * 
    */
   public void addRandomShift (int d1, int d2, RandomStream stream) {
      if (null == stream)
         throw new IllegalArgumentException (
              PrintfFormat.NEWLINE +
              "   Calling addRandomShift with null stream");
      if (0 == d2)
         d2 = Math.max (dim, 1);
      if (shift == null) {
         shift = new double[d2];
         capacityShift = d2;
      } else if (d2 > capacityShift) {
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
         shift[i] = stream.nextDouble ();
      shiftStream = stream;
   }


   public void clearRandomShift() {
      super.clearRandomShift();
      shift = null;
   }


   /**
    * Adds the cycle <TT>c</TT> to the list of all cycles.
    *    This method is used by subclass constructors to fill up the list of cycles.
    * 
    */
   protected void addCycle (AbstractList c)  {
      // Adds the cycle \texttt{c} to the list of all cycles.
      // Used by subclass constructors to fill up the list of cycles.
      cycles.add (c);
      numCycles++;
      numPoints += c.size();
   }


   public int getDimension() {
     return Integer.MAX_VALUE;
   }

   public PointSetIterator iterator(){
      return new  CycleBasedPointSetIterator();
   }

   public String toString() {
      String s = super.toString();
      return s + PrintfFormat.NEWLINE + "Number of cycles: " + numCycles;
   }

   public String formatPoints() {
      StringBuffer sb = new StringBuffer (toString());
      for (int c = 0; c < numCycles; c++) {
         AbstractList curCycle = (AbstractList)cycles.get (c);
         double[] cycle = ((DoubleArrayList)curCycle).elements();
         sb.append (PrintfFormat.NEWLINE + "Cycle " + c + ": (");
         boolean first = true;
         for (int e = 0; e < curCycle.size(); e++) {
            if (first)
               first = false;
            else
               sb.append (", ");
            sb.append (cycle[e]);
         }
         sb.append (")");
      }
      return sb.toString();
   }

   // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

   public class CycleBasedPointSetIterator extends DefaultPointSetIterator {

      protected int startPointInCycle = 0;   // Index where the current point
                                             // starts in the current cycle.
      protected int curCoordInCycle = 0;     // Index of the current coordinate
                                             // in the current cycle.
      protected int curCycleIndex = 0;       // Index of the current cycle.
      protected AbstractList curCycle;       // The current cycle.
      protected double[] curCycleD;          // The array for current cycle


      public CycleBasedPointSetIterator () {
         init();
      }

      protected void init () {
         resetCurCycle(0);
      }

      public void resetCurCycle (int index) {
         curCycleIndex = index;
         curCycle = (AbstractList) cycles.get (index);
         curCycleD = ((DoubleArrayList) curCycle).elements();
      }

      public void setCurCoordIndex (int i) {
         curCoordIndex = i;
         curCoordInCycle = (i + startPointInCycle) % curCycle.size();
      }

      public void resetCurCoordIndex() {
         curCoordIndex = 0;
         curCoordInCycle = startPointInCycle;
      }

      public boolean hasNextCoordinate() {
         return true;
      }

      // We want to avoid generating 0 or 1
      public double nextDouble() {
         return nextCoordinate() + EpsilonHalf;
      }

      public double nextCoordinate() {
         // First, verify if there are still points....
         if (getCurPointIndex() >= getNumPoints ())
            outOfBounds();
         double x = curCycleD [curCoordInCycle];
         if (shift != null) {
             if (curCoordIndex >= dimShift)   // Extend the shift.
                addRandomShift (dimShift, curCoordIndex + 1, shiftStream);
             x += shift[curCoordIndex];
             if (x >= 1.0)
                x -= 1.0;
             if (x <= 0.0)
                x = EpsilonHalf;  // avoid x = 0
         }
         curCoordIndex++;
         curCoordInCycle++;
         if (curCoordInCycle >= curCycle.size())
            curCoordInCycle = 0;
         return x;
      }

      public void nextCoordinates (double p[], int dim) {
         // First, verify if there are still points....
         if (getCurPointIndex() >= getNumPoints ())
            outOfBounds();
         if (curCoordIndex + dim >= dimShift)
            addRandomShift (dimShift, curCoordIndex + dim + 1, shiftStream);
         int j = curCoordInCycle;
         int maxj = curCycle.size();
         double x;
         for (int i = 0; i < dim; i++) {
            x = curCycleD [curCoordInCycle++];
            if (curCoordInCycle >= maxj) curCoordInCycle = 0;
            if (shift != null) {
               x += shift[curCoordIndex + i];
               if (x >= 1.0)
                  x -= 1.0;
               if (x <= 0.0)
                  x = EpsilonHalf;  // avoid x = 0
           }
            p[i] = x;
         }
         curCoordIndex += dim;
      }

      public void setCurPointIndex (int i) {
         int l = 0;
         int n = 0;
         int j ;
         for (j=0;  n <= i;  j++)
            n += l = ((AbstractList) cycles.get (j)).size();
         resetCurCycle (j-1);
         startPointInCycle = i - n + l;
         curPointIndex = i;
         curCoordIndex = 0;
         curCoordInCycle = startPointInCycle;
      }

      public void resetCurPointIndex() {
         resetCurCycle (0);
         startPointInCycle = 0;
         curPointIndex = 0;
         curCoordIndex = 0;
         curCoordInCycle = 0;
      }

      public int resetToNextPoint() {
         curPointIndex++;
         startPointInCycle++;
         if (startPointInCycle >= curCycle.size()) {
            startPointInCycle = 0;
            if (curCycleIndex < (numCycles - 1))
               resetCurCycle (curCycleIndex + 1);
         }
         curCoordIndex = 0;
         curCoordInCycle = startPointInCycle;
         return curPointIndex;
      }

      public int nextPoint (double p[], int dim) {
         // First, verify if there are still points....
         if (getCurPointIndex() >= getNumPoints ())
            outOfBounds();
         int j = startPointInCycle;
         int maxj = curCycle.size() - 1;
         for (int i = 0; i < dim; i++) {
            p[i] = curCycleD [j];
            if (j < maxj) j++;  else j = 0;
         }
         resetToNextPoint();
         return curPointIndex;
      }

      public String formatState() {
         return super.formatState() + PrintfFormat.NEWLINE +
           "Current cycle: " + curCycleIndex;
      }
   }

}
