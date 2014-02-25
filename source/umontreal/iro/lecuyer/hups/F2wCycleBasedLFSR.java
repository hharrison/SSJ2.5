

/*
 * Class:        F2wCycleBasedLFSR
 * Description:  point set based upon a linear feedback shift register
                 sequence
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
import cern.colt.list.*;
import umontreal.iro.lecuyer.util.PrintfFormat;



/**
 * This class creates a point set based upon a linear feedback shift register
 *  sequence. The recurrence used to produce the point set is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>m</I><SUB>n</SUB> = &sum;<SUB>i=1</SUB><SUP>r</SUP><I>b</I><SUB>i</SUB><I>m</I><SUB>n-i</SUB>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>m</I><SUB>n</SUB>&#8712;<B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN>, <SPAN CLASS="MATH"><I>n</I>&nbsp;&gt;=&nbsp; 0</SPAN>
 *   and 
 * <SPAN CLASS="MATH"><I>b</I><SUB>i</SUB>&#8712;<B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN>.
 * There is a polynomial in 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]</SPAN>
 *  associated with this recurrence called
 * the <SPAN  CLASS="textit">characteristic polynomial</SPAN>.  It is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>P</I>(<I>z</I>) = <I>z</I><SUP>r</SUP> + &sum;<SUB>i=1</SUB><SUP>r</SUP><I>b</I><SUB>i</SUB><I>z</I><SUP>r-i</SUP>.
 * </DIV><P></P>
 * In the implementation, this polynomial is stored in an object
 * <TT>F2wStructure</TT>.
 * 
 * <P>
 * Let 
 * <SPAN CLASS="MATH"><B>x</B> = (<I>x</I><SUP>(0)</SUP>,&#8230;, <I>x</I><SUP>(p-1)</SUP>)&#8712;<B>F</B><SUB>2</SUB><SUP>p</SUP></SPAN>
 * be a <SPAN CLASS="MATH"><I>p</I></SPAN>-bit vector.
 * Let us define the function 
 * <SPAN CLASS="MATH"><I>&#966;</I>(<B>x</B>) = &sum;<SUB>i=1</SUB><SUP>p</SUP>2<SUP>-i</SUP><I>x</I><SUP>(i-1)</SUP></SPAN>.
 * The point set in <SPAN CLASS="MATH"><I>t</I></SPAN> dimensions produced by this class is
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * {(<I>&#966;</I>(<B>y</B><SUB>0</SUB>), <I>&#966;</I>(<B>y</B><SUB>s</SUB>),&#8230;, <I>&#966;</I>(<B>y</B><SUB>s(t-1)</SUB>) : (<B>v</B><SUB>0</SUB>,&#8230;,<B>v</B><SUB>r-1</SUB>)&#8712;<B>F</B><SUB>2</SUB><SUP>rw</SUP>}
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><B>y</B><SUB>n</SUB> = trunc<SUB>h</SUB>(<B>v</B><SUB>n</SUB>,<B>v</B><SUB>n+1</SUB>,&#8230;)</SPAN>,
 * 
 * <SPAN CLASS="MATH"><B>v</B><SUB>n</SUB></SPAN> is the representation of <SPAN CLASS="MATH"><I>m</I><SUB>n</SUB></SPAN> under the polynomial basis of
 * 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN> over
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2</SUB></SPAN>, and
 *  
 * <SPAN CLASS="MATH"><I>h</I> = <I>w</I> floor(31/<I>w</I>)</SPAN>.
 * The parameter <SPAN CLASS="MATH"><I>s</I></SPAN> is called the stepping parameter of the recurrence.
 * 
 */
public class F2wCycleBasedLFSR extends CycleBasedPointSetBase2  {

   private F2wStructure param;


   /**
    * Constructs a point set with <SPAN CLASS="MATH">2<SUP>rw</SUP></SPAN> points.  See the description of the class
    * {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure} for the meaning
    *  of the parameters.
    * 
    */
   public F2wCycleBasedLFSR (int w, int r, int modQ, int step, int nbcoeff,
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
      init ();
   }


   /**
    * Constructs a point set after reading its parameters from
    *    file <TT>filename</TT>; the parameters are located at line numbered <TT>no</TT>
    *    of  <TT>filename</TT>.  The available files are listed in the description of class
    * {@link umontreal.iro.lecuyer.hups.F2wStructure F2wStructure}.
    * 
    */
   public F2wCycleBasedLFSR (String filename, int no) 
   {
      param = new F2wStructure (filename, no);
      init ();
   }



   private void init ()
   {
      param.initParamLFSR ();
      normFactor = param.normFactor;
      EpsilonHalf = param.EpsilonHalf;
      numBits = param.numBits;
      fillCyclesLFSR ();
   }

   public String toString ()
   {
       String s = "F2wCycleBasedLFSR:" + PrintfFormat.NEWLINE;
       return s + param.toString ();
   }

   private void fillCyclesLFSR ()
   {
      int n = 1 << param.getLog2N ();
      IntArrayList c;             // Array used to store the current cycle.
      int currentState;           // The state currently visited.
      int i;
      boolean stateVisited[] = new boolean[n];

      // Indicates which states have been visited so far.
      for (i = 0; i < n; i++)
         stateVisited[i] = false;
      int startState = 0;    // First state of the cycle currently considered.
      numPoints = 0;
      while (startState < n) {
         stateVisited[startState] = true;
         c = new IntArrayList ();
         param.state = startState;
         param.initF2wLFSR ();
         c.add (param.output);
         param.F2wLFSR ();
         // Warning: watch for overflow !!!
         while (param.state != startState) {
            stateVisited[param.state] = true;
            c.add (param.output);
            param.F2wLFSR ();
         }
         addCycle (c);
         for (i = startState + 1; i < n; i++)
            if (stateVisited[i] == false)
               break;
         startState = i;
      }
   }
}

