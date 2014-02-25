

/*
 * Class:        DigitalSequenceBase2
 * Description:  abstract class with methods specific to digital sequences
                 in base 2
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


/**
 * This abstract class describes methods specific to digital sequences in base 2.
 * Concrete classes must implement the {@link #extendSequence extendSequence} method
 * that increases the number of points of the digital sequence.
 * Calling the  methods {@link #toNet toNet} or {@link #toNetShiftCj toNetShiftCj}
 * will transform the digital sequence into a digital net, which has
 * a fixed number of points <SPAN CLASS="MATH"><I>n</I></SPAN>.
 * 
 */
public abstract class DigitalSequenceBase2 extends DigitalNetBase2  { 



   /**
    * Increases the number of points to <SPAN CLASS="MATH"><I>n</I> = 2<SUP>k</SUP></SPAN> from now on.
    * 
    * @param k there will be 2^k points
    * 
    * 
    */
   public abstract void extendSequence (int k);


   private int[] copyDigitalShift (int[] S) {
      // Copy the shift S into T and returns T.
      if (S == null) return null;
      int[] T = new int [S.length];
      for (int i = 0; i < S.length; ++i)
         T[i] = S[i];
      return T;
   }

   private DigitalNetBase2 initNetVar (boolean shiftFlag) {
      // Initializes the net for the two toNet methods below.
      DigitalNetBase2 net = new DigitalNetBase2 ();
      if (shiftFlag)
         net.dim = dim + 1;
      else
         net.dim = dim;
      net.numPoints = numPoints;
      net.numCols = numCols;
      net.numRows = numRows;
      net.outDigits = outDigits;
      net.normFactor = normFactor;
      net.factor = new double[outDigits];
      net.genMat = new int[net.dim * numCols];
      net.shiftStream = shiftStream;
      net.capacityShift = capacityShift;
      net.dimShift = dimShift;
      net.digitalShift = copyDigitalShift (digitalShift);
      if (shiftFlag && shiftStream != null) {
          net.addRandomShift (dimShift, dimShift + 1, shiftStream);
      }
      return net;
   } 

   /**
    * Transforms this digital sequence into a digital net without changing
    *    the coordinates of the points. Returns the digital net.
    * 
    */
   public DigitalNetBase2 toNet()  {
      DigitalNetBase2 net = initNetVar (false);
      for (int i = 0; i < dim * numCols; i++)
         net.genMat[i] = genMat[i];
      return net;
   }


   /**
    * Transforms this digital sequence into a digital net by adding one dimension
    *   and shifting all coordinates by one position. The first coordinate of point
    *   <SPAN CLASS="MATH"><I>i</I></SPAN> is <SPAN CLASS="MATH"><I>i</I>/<I>n</I></SPAN>, where <SPAN CLASS="MATH"><I>n</I></SPAN> is the total number of points.
    *   Thus if the coordinates of a point of the digital sequence were
    *   
    * <SPAN CLASS="MATH">(<I>x</I><SUB>0</SUB>, <I>x</I><SUB>1</SUB>, <I>x</I><SUB>2</SUB>,&#8230;, <I>x</I><SUB>s-1</SUB>)</SPAN>, then the coordinates of the
    *   point of the digital net will be 
    * <SPAN CLASS="MATH">(<I>i</I>/<I>n</I>, <I>x</I><SUB>0</SUB>, <I>x</I><SUB>1</SUB>,&#8230;, <I>x</I><SUB>s-1</SUB>)</SPAN>.
    *   In other words, for the digital net, 
    * <SPAN CLASS="MATH"><B>C</B><SUB>0</SUB></SPAN> is the reflected 
    *   identity and for <SPAN CLASS="MATH"><I>j</I>&nbsp;&gt;=&nbsp;1</SPAN>, the 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> used is the
    *   
    * <SPAN CLASS="MATH"><B>C</B><SUB>j-1</SUB></SPAN> of the digital sequence.  If the digital sequence uses
    *   a digital shift, then the digital net will include the digital shift with
    *   one more dimension also. Returns the digital net.
    * 
    */
   public DigitalNetBase2 toNetShiftCj()  {
      DigitalNetBase2 net = initNetVar (true);
      int c;
      for (c = (dim + 1) * numCols - 1; c >= numCols; --c)
         net.genMat[c] = genMat[c - numCols];

      // the first dimension, j = 0.
      for (c = 0; c < numCols; c++)
         net.genMat[c] = (1 << (outDigits-numCols+c));
      return net;
   }


   /**
    * Similar to {@link #iterator iterator}, except that the first coordinate
    *   of the points is <SPAN CLASS="MATH"><I>i</I>/<I>n</I></SPAN>, the second coordinate is obtained via
    *   the generating matrix 
    * <SPAN CLASS="MATH"><B>C</B><SUB>0</SUB></SPAN>, the next one via 
    * <SPAN CLASS="MATH"><B>C</B><SUB>1</SUB></SPAN>, 
    *   and so on. Thus, this iterator shifts all coordinates of each point
    *   one position to the right and sets the first coordinate of point <SPAN CLASS="MATH"><I>i</I></SPAN>
    *   to  <SPAN CLASS="MATH"><I>i</I>/<I>n</I></SPAN>, so that the points enumerated with this iterator have one more
    *   dimension. A digital shift, if present, will have one more dimension also.
    *   This iterator uses the Gray code.
    * 
    */
   public PointSetIterator iteratorShift() {
      return new DigitalNetBase2IteratorShiftGenerators();
   }


   /**
    * This iterator shifts all coordinates of each point one position to the right
    *   and sets the first coordinate of point <SPAN CLASS="MATH"><I>i</I></SPAN> to  <SPAN CLASS="MATH"><I>i</I>/<I>n</I></SPAN>, so that the points 
    *   enumerated with this iterator have one more dimension. This iterator
    *   does not use the Gray code: the points are enumerated in the order of 
    *   their first coordinate before randomization.
    *   A digital shift, if present, will have one more dimension also.
    * 
    */
   public PointSetIterator iteratorShiftNoGray() {
      return new DigitalNetBase2IteratorShiftNoGray();
   }



   // *******************************************************************

   protected class DigitalNetBase2IteratorShiftGenerators 
                   extends DigitalNetBase2Iterator {
      // Similar to DigitalNetBase2Iterator; the first coordinate
      // of point i is i/n, and all the others are shifted one position
      // to the right. The points have one more dimension.

      public DigitalNetBase2IteratorShiftGenerators() {
         super(); 
         dimS = dim + 1;
         if (digitalShift != null && dimShift < dimS)
            addRandomShift (dimShift, dimS, shiftStream);
         resetCurPointIndex();
      }

      public void init2() {  // This method is necessary to overload
      }                      // the init2() of DigitalNetBase2Iterator

      protected void addShiftToCache () {
         if (digitalShift == null)
            for (int j = 0; j < dimS; j++)
               cachedCurPoint[j] = 0;
         else
            for (int j = 0; j < dimS; j++)
               cachedCurPoint[j] = digitalShift[j];
      }

      public void setCurPointIndex (int i) {
         if (i == 0) {
            resetCurPointIndex();   return;
         }
         // Out of order computation, must recompute the cached current
         // point from scratch.
         curPointIndex = i;  
         curCoordIndex = 0;
         addShiftToCache ();

         int j;
         int grayCode = i ^ (i >> 1);
         int pos = 0;      // Position of the bit that is examined.
         while ((grayCode >> pos) != 0) {
            if (((grayCode >> pos) & 1) != 0) {
               cachedCurPoint[0] ^= 1 << (outDigits - numCols + pos);
               for (j = 1; j <= dim; j++)
                  cachedCurPoint[j] ^= genMat[(j-1) * numCols + pos];
            }
            pos++;
         }
      }

      public int resetToNextPoint() {
         int pos = 0;  // Will be position of change in Gray code,
                       // = pos. of first 0 in binary code of point index.
         while (((curPointIndex >> pos) & 1) != 0)
            pos++;
         if (pos < numCols) {
            // The matrix C for first coord. is a reflected identity.
            // Col. pos has a 1 in line k-1-pos.
            cachedCurPoint[0] ^= 1 << (outDigits - numCols + pos);
            for (int j = 1; j <= dim; j++)
               cachedCurPoint[j] ^= genMat[(j-1) * numCols + pos];
         }
         curCoordIndex = 0;
         return ++curPointIndex;
      }

   }


   // *******************************************************************

   protected class DigitalNetBase2IteratorShiftNoGray 
                   extends DigitalNetBase2Iterator {
      // Similar to DigitalNetBase2IteratorShiftGenerators, 
      // except that the Gray code is not used.
      private boolean shiftDimFlag = false; // ensures that the random shift
                             // has been initialized with dim + 1 components

      public DigitalNetBase2IteratorShiftNoGray() {
         super(); 
         dimS = dim + 1;
         if (digitalShift != null && dimShift < dimS)
            addRandomShift (dimShift, dimS, shiftStream);
         resetCurPointIndex();
      }

      public void init2() {  // This method is necessary to overload
      }                      // the init2() of DigitalNetBase2Iterator

      protected void addShiftToCache () {
         if (digitalShift == null)
            for (int j = 0; j <= dim; j++)
               cachedCurPoint[j] = 0;
         else
            for (int j = 0; j <= dim; j++)
               cachedCurPoint[j] = digitalShift[j];
      }

      public void setCurPointIndex (int i) {
         if (i == 0) {
            resetCurPointIndex();
            return;
         }
         // Out of order computation, must recompute the cached current
         // point from scratch.
         curPointIndex = i;  
         curCoordIndex = 0;
         addShiftToCache ();

         int pos = 0;      // Position of the bit that is examined.
         while ((i >> pos) != 0) {
            if (((i >> pos) & 1) != 0) {
               cachedCurPoint[0] ^= 1 << (outDigits - numCols + pos);
               for (int j = 1; j <= dim; j++)
                  cachedCurPoint[j] ^= genMat[(j-1) * numCols + pos];
            }
            pos++;
         }
      }

      public int resetToNextPoint() {
         // Contains the bits of i that changed.
         if (curPointIndex + 1 >= numPoints)
            return ++curPointIndex;
         int diff = curPointIndex ^ (curPointIndex + 1);
         int pos = 0;      // Position of the bit that is examined.
         while ((diff >> pos) != 0) {
            if (((diff >> pos) & 1) != 0) {
               cachedCurPoint[0] ^= 1 << (outDigits - numCols + pos);
               for (int j = 1; j <= dim; j++)
                  cachedCurPoint[j] ^= genMat[(j-1) * numCols + pos];
            }
            pos++;
         }
         curCoordIndex = 0;
         return ++curPointIndex;
      }
   }
}

