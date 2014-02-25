

/*
 * Class:        F2wCycleBasedPolyLCG
 * Description:  point set based upon a linear congruential sequence in a
                 finite field
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
import cern.colt.list.*;



/**
 * This class creates a point set based upon
 * a linear congruential sequence in the finite field
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]/<I>P</I>(<I>z</I>)</SPAN>.
 * The recurrence is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>q</I><SUB>n</SUB>(<I>z</I>) = <I>z</I><SUP>s</SUP><I>q</I><SUB>n-1</SUB>(<I>z</I>)&nbsp;mod&nbsp;<I>P</I>(<I>z</I>)
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)&#8712;<B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]</SPAN> has degree <SPAN CLASS="MATH"><I>r</I></SPAN> and
 * 
 * <SPAN CLASS="MATH"><I>q</I><SUB>n</SUB>(<I>z</I>) = <I>q</I><SUB>n, 1</SUB><I>z</I><SUP>r-1</SUP> + <SUP> ... </SUP> + <I>q</I><SUB>n, r</SUB>&#8712;<B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]/<I>P</I>(<I>z</I>)</SPAN>.
 * The parameter <SPAN CLASS="MATH"><I>s</I></SPAN> is
 * called the stepping parameter of the recurrence.
 * The polynomial <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)</SPAN> is not necessarily the characteristic polynomial
 * of this recurrence, but it can still be used to store the parameters of
 *  the recurrence.
 * In the implementation, it is stored in an object of the class
 *  {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure}.  See the description
 * of this class for more details on how the polynomial
 * is stored.
 * 
 * <P>
 * Let 
 * <SPAN CLASS="MATH"><B>x</B> = (<I>x</I><SUP>(0)</SUP>,&#8230;, <I>x</I><SUP>(p-1)</SUP>)&#8712;<B>F</B><SUB>2</SUB><SUP>p</SUP></SPAN> be a <SPAN CLASS="MATH"><I>p</I></SPAN>-bit vector.
 * Let us define the function 
 * <SPAN CLASS="MATH"><I>&#966;</I>(<B>x</B>) = &sum;<SUB>i=1</SUB><SUP>p</SUP>2<SUP>-i</SUP><I>x</I><SUP>(i-1)</SUP></SPAN>.
 * The point set in <SPAN CLASS="MATH"><I>t</I></SPAN> dimensions produced by this class is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * {(<I>&#966;</I>(<B>y</B><SUB>0</SUB>), <I>&#966;</I>(<B>y</B><SUB>1</SUB>),&#8230;, <I>&#966;</I>(<B>y</B><SUB>t-1</SUB>) : (<B>q</B><SUB>0, 1</SUB>,&#8230;,<B>q</B><SUB>0, r-1</SUB>)&#8712;<B>F</B><SUB>2</SUB><SUP>rw</SUP>}
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><B>y</B><SUB>n</SUB> = (<B>q</B><SUB>n, 1</SUB>,&#8230;,<B>q</B><SUB>n, r</SUB>)</SPAN>,
 *  
 * <SPAN CLASS="MATH"><B>q</B><SUB>n, i</SUB></SPAN>
 *  is the representation of <SPAN CLASS="MATH"><I>q</I><SUB>n, i</SUB></SPAN> under the polynomial basis of
 * 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN> over
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2</SUB></SPAN>.
 * 
 */
public class F2wCycleBasedPolyLCG extends CycleBasedPointSetBase2  {

   private F2wStructure param;


   /**
    * Constructs a point set with <SPAN CLASS="MATH">2<SUP>rw</SUP></SPAN> points.  See the description of the class
    *  {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure}
    *  for the meaning of the parameters.
    * 
    */
   public F2wCycleBasedPolyLCG (int w, int r, int modQ, int step, int nbcoeff,
                                int coeff[], int nocoeff[]) 
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
   {
      param = new F2wStructure (w, r, modQ, step, nbcoeff, coeff, nocoeff);
      numBits = param.numBits;
      normFactor = param.normFactor;
      EpsilonHalf = param.EpsilonHalf;
      fillCyclesPolyLCG ();
   }


   /**
    * Constructs a point set after reading its parameters from
    *    file <TT>filename</TT>; the parameters are located at line numbered <TT>no</TT>
    *    of <TT>filename</TT>. The available files are listed in the description of class
    * {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure}.
    * 
    */
   public F2wCycleBasedPolyLCG (String filename, int no) 
   {
      param = new F2wStructure (filename, no);
      numBits = param.numBits;
      normFactor = param.normFactor;
      fillCyclesPolyLCG ();
   }



   public String toString ()
   {
       String s = "F2wCycleBasedPolyLCG:" + PrintfFormat.NEWLINE;
       return s + param.toString ();
   }

   private void fillCyclesPolyLCG ()
   {
      int n = 1 << param.getLog2N();
      int i;
      int mask1 = (1 << (31 - param.r * param.w)) - 1;
      int mask2 = ~mask1;
      RandomStream random = new MRG32k3a();
      IntArrayList c;             // Array used to store the current cycle.
      int currentState;           // The state currently visited.

      boolean stateVisited[] = new boolean[n];
      // Indicates which states have been visited so far.
      for (i = 0; i < n; i++)
         stateVisited[i] = false;
      int startState = 0;  // First state of the cycle currently considered.
      numPoints = 0;
      while (startState < n) {
         stateVisited[startState] = true;
         c = new IntArrayList ();
         param.state = startState << param.S;
         c.add (param.state);
         // c.add ((state & mask2) | (mask1 &
         // (random.nextInt(0,2147483647))));
         param.output = param.F2wPolyLCG ();
         // Warning: watch for overflow !!!
         while (param.state != (startState << param.S)) {
            stateVisited[param.state >> param.S] = true;
            // c.add ((param.state&mask2) | (mask1 &
            // (random.nextInt(0,2147483647))));
            c.add (param.state);
            param.output = param.F2wPolyLCG ();
         }
         addCycle (c);
         for (i = startState + 1; i < n; i++)
            if (stateVisited[i] == false)
               break;
         startState = i;
      }
   }
}

