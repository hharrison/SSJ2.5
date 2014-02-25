

/*
 * Class:        F2wStructure
 * Description:  Tools for point sets and sequences based on field F_{2^w}
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
import java.io.*;
import java.util.*;


/**
 * This class implements methods and fields needed by the classes
 *  {@link umontreal.iro.lecuyer.hups.F2wNetLFSR F2wNetLFSR},
 *  {@link umontreal.iro.lecuyer.hups.F2wNetPolyLCG F2wNetPolyLCG},
 *  {@link umontreal.iro.lecuyer.hups.F2wCycleBasedLFSR F2wCycleBasedLFSR} and
 *  {@link umontreal.iro.lecuyer.hups.F2wCycleBasedPolyLCG F2wCycleBasedPolyLCG}.
 * It also stores the parameters of these point sets which will contain
 * <SPAN CLASS="MATH">2<SUP>rw</SUP></SPAN> points (see the meaning of <SPAN CLASS="MATH"><I>r</I></SPAN> and <SPAN CLASS="MATH"><I>w</I></SPAN> below).
 * The parameters can be stored as a polynomial <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)</SPAN> over
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]</SPAN>
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>P</I>(<I>z</I>) = <I>z</I><SUP>r</SUP> + &sum;<SUB>i=1</SUB><SUP>r</SUP><I>b</I><SUB>i</SUB><I>z</I><SUP>r-i</SUP>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>b</I><SUB>i</SUB>&#8712;<B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN> for 
 * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>r</I></SPAN>.
 *  Let <SPAN CLASS="MATH"><I>&#950;</I></SPAN> be the root of an irreducible polynomial
 *  
 * <SPAN CLASS="MATH"><I>Q</I>(<I>z</I>)&#8712;<B>F</B><SUB>2</SUB>[<I>z</I>]</SPAN>.  It is well known
 * that <SPAN CLASS="MATH"><I>&#950;</I></SPAN> is a generator of the finite field
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN>.
 * The elements of 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN> are
 *  represented using the polynomial ordered
 *  basis 
 * <SPAN CLASS="MATH">(1, <I>&#950;</I>,&#8230;, <I>&#950;</I><SUP>w-1</SUP>)</SPAN>.
 * 
 * <P>
 * In this class, only the non-zero coefficients of <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)</SPAN> are stored.
 *  It is stored as
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>P</I>(<I>z</I>) = <I>z</I><SUP><TT>r</TT></SUP> + &sum;<SUB>i=0</SUB><SUP><TT>nbcoeff</TT></SUP><TT>coeff</TT>[<I>i</I>]<I>z</I><SUP><TT>nocoeff</TT>[i]</SUP>
 * </DIV><P></P>
 * where the coefficients in <TT>coeff[]</TT> represent the non-zero
 *  coefficients <SPAN CLASS="MATH"><I>b</I><SUB>i</SUB></SPAN> of <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)</SPAN> using the polynomial basis.
 * The finite field 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN> used is
 *  defined by the polynomial
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>Q</I>(<I>z</I>) = <I>z</I><SUP>w</SUP> + &sum;<SUB>i=1</SUB><SUP>w</SUP><I>a</I><SUB>i</SUB><I>z</I><SUP>w-i</SUP>
 * </DIV><P></P>
 * where 
 * <SPAN CLASS="MATH"><I>a</I><SUB>i</SUB>&#8712;<B>F</B><SUB>2</SUB></SPAN>,
 *  for 
 * <SPAN CLASS="MATH"><I>i</I> = 1,&#8230;, <I>w</I></SPAN>. Polynomial <SPAN CLASS="MATH"><I>Q</I></SPAN> is
 *  stored as the bit vector <TT>modQ</TT> = 
 * <SPAN CLASS="MATH">(<I>a</I><SUB>w</SUB>,&#8230;, <I>a</I><SUB>1</SUB>)</SPAN>.
 * 
 * <P>
 * The class also stores the parameter <TT>step</TT> that is used by the classes
 * {@link umontreal.iro.lecuyer.hups.F2wNetLFSR F2wNetLFSR},
 *  {@link umontreal.iro.lecuyer.hups.F2wNetPolyLCG F2wNetPolyLCG},
 *  {@link umontreal.iro.lecuyer.hups.F2wCycleBasedLFSR F2wCycleBasedLFSR} and
 *  {@link umontreal.iro.lecuyer.hups.F2wCycleBasedPolyLCG F2wCycleBasedPolyLCG}.
 * This parameter is such that the implementation of the recurrence
 *  will output a value  at every <TT>step</TT> iterations.
 * 
 */
public class F2wStructure  {

   private final int ALLONES = 2147483647; // 2^31-1 --> 01111...1
   int w;
   int r;
   int numBits;
   private int modQ;
   private int step;
   private int[] coeff;
   private int[] nocoeff;
   private int nbcoeff;
   int S;
   private int maskw;
   private int maskrw;
   private int maskZrm1;
   private int mask31;
   private int t;
   private int masktrw;
   private int[] maskv;
   int state;
   int output;            // augmented state
   double normFactor;
   double EpsilonHalf;
   final static int MBL = 140; //maximum of bytes in 1 line
   //92 bytes for a number of coeff = 15


   private void init (int w, int r, int modQ, int step,
      int nbcoeff, int coeff[], int nocoeff[])
   {
      normFactor = 1.0 / (1L << 31); // 4.65661287307739258e-10;
      EpsilonHalf = 0.5*normFactor;
      numBits = 31;
      this.step = step;
      this.w = w;
      this.r = r;
      S = 31 - r * w;
      mask31 = ~(1 << 31);
      maskw = (1 << w) - 1;
      maskrw = ((1 << (r * w)) - 1) << S;
      maskZrm1 = (ALLONES >> (r * w)) ^ (ALLONES >> ((r - 1) * w));
      this.modQ = modQ;
      this.nbcoeff = nbcoeff;
      this.nocoeff = new int[nbcoeff];
      this.coeff = new int[nbcoeff];
      for (int j = 0; j < nbcoeff; j++) {
         this.nocoeff[j] = nocoeff[j];
         this.coeff[j] = coeff[j];
      }
   }

   void initParamLFSR ()
   {
      t = (31 - r * w) / w;
      masktrw = (~0) << (31 - (t + r) * w) & mask31;
      maskv = new int[r];
      for (int j = 0; j < r; j++) {
         maskv[j] = maskw << (S + ((r - 1 - j) * w));
      }
   }




   /**
    * Constructs a <TT>F2wStructure</TT> object that contains  the parameters of a
    *   polynomial in 
    * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]</SPAN>,
    *  as well as a stepping parameter.
    * 
    */
   F2wStructure (int w, int r, int modQ, int step, int nbcoeff,
                 int coeff[], int nocoeff[]) 
   {
      init (w, r, modQ, step, nbcoeff, coeff, nocoeff);
   }



   /**
    * Constructs a polynomial in 
    * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB>[<I>z</I>]</SPAN>
    *    after reading its parameters from file <TT>filename</TT>;
    *    the parameters of this polynomial are stored  at line number
    *    <TT>no</TT> of <TT>filename</TT>.
    *    The files are kept in different
    *    directories depending on the criteria used in the searches for the
    *    parameters defining the polynomials. The different criteria for the
    *    searches and the theory behind it are described in.
    *    The existing files and the number of polynomials they contain are
    *    given in the following tables.
    *    The first table below contains files in subdirectory
    *     <TT>LFSR_equid_max</TT>. The name of each
    *    file indicates the value of <SPAN CLASS="MATH"><I>r</I></SPAN> and <SPAN CLASS="MATH"><I>w</I></SPAN> for the polynomials.
    *    For example, file <TT>f2wR2_W5.dat</TT> in directory
    *    <TT>LFSR_equid_max</TT> contains the parameters of 2358
    *    polynomials with <SPAN CLASS="MATH"><I>r</I> = 2</SPAN> and <SPAN CLASS="MATH"><I>w</I> = 5</SPAN>. For example, to use the 5<SPAN  CLASS="textit">-th</SPAN>
    *     polynomial of file <TT>f2wR2_W5.dat</TT>, one may call
    *    <TT>F2wStructure("f2wR2_W5.dat", 5)</TT>.
    *    The files of parameters have been stored at the address
    *    <TT><A NAME="tex2html1"
    *   HREF="http://www.iro.umontreal.ca/~simardr/ssj-1/dataF2w/">http://www.iro.umontreal.ca/~simardr/ssj-1/dataF2w/</A></TT>.
    * 
    */
   F2wStructure (String filename, int no)
   {
     // If filename can be found starting from the program's directory,
     // it will be used; otherwise, the filename in the Jar archive will
     // be used.
     BufferedReader input;
     try {
       if ((new File (filename)).exists()) {
          input = new BufferedReader (new FileReader (filename));
       } else {
          DataInputStream dataInput;
          dataInput = new DataInputStream (
             F2wStructure.class.getClassLoader().getResourceAsStream (
                 "umontreal/iro/lecuyer/hups/dataF2w/" + filename));
          input = new BufferedReader (new InputStreamReader (dataInput));
       }
       initFromReader (filename, input, no);
       input.close ();

     } catch (Exception e) {
       System.out.println ("IO Error: problems finding file " + filename);
       System.exit (1);
     }
   }



   private int multiplyz (int a, int k)
   {
      int i;
      if (k == 0)
         return a & maskw;
      else {
         for (i = 0; i < k; i++) {
            if ((1 & a) == 1) {
               a = (a >> 1) ^ modQ;
            } else
               a = a >> 1;
         }
         return a & maskw;
      }
   }


   /**
    * This method returns the product <SPAN CLASS="MATH"><I>rw</I></SPAN>.
    * 
    */
   int getLog2N ()
   {
      return r * w;
   }


   /**
    * Method that multiplies two elements in
    *  
    * <SPAN CLASS="MATH"><B>F</B><SUB>2<SUP>w</SUP></SUB></SPAN>.
    * 
    */
   int multiply (int a, int b)

   {
      int i;
      int res = 0, verif = 1;
      for (i = 0; i < w; i++) {
         if ((b & verif) == verif)
            res ^= multiplyz (a, w - 1 - i);
         verif <<= 1;
      }
      return res & maskw;
   }
 

   void initF2wLFSR ()     // Initialisation de l'etat d'un LFSR
   {
      int v, d = 0;
      int tempState;

      tempState = state << S;
      output = tempState;
      for (int i = 1; i <= t; i++) {
         d = 0;
         for (int j = 0; j < nbcoeff; j++) {
            v = (tempState & maskv[nocoeff[j]]) >>
                 (S + (r - 1 - nocoeff[j]) * w);
            d ^= multiply (coeff[j], v);
         }
         output |= d << (S - i * w);
         tempState = (output << (i * w)) & maskrw;
      }
   }


   void F2wLFSR ()       // Une iteration d'un LFSR
   {
      int v, d = 0;
      int tempState;
      for (int i = 0; i < step; i++) {
         tempState = (output << (t * w)) & maskrw;
         d = 0;
         for (int j = 0; j < nbcoeff; j++) {
            v = (tempState & maskv[nocoeff[j]]) >>
                (S + (r - 1 - nocoeff[j]) * w);
            d ^= multiply (coeff[j], v);
         }
         output = ((output << w) & masktrw) |
                  (d << (31 - (r + t) * w));
      }
      state = (output & maskrw) >> S;
   }


   int F2wPolyLCG ()    // Une iteration d'un PolyLCG
   {
      int Zrm1, d;
      for (int i = 0; i < step; i++) {
         Zrm1 = (state & maskZrm1) >> S;
         state = (state >> w) & maskrw;
         for (int j = 0; j < nbcoeff; j++)
            state ^=
               multiply (coeff[j], Zrm1) << (S + (r - 1 - nocoeff[j]) * w);
      }
      return state;
   }

   /**
    * Prints the content of file <TT>filename</TT>. See the constructor
    *     above for the conditions on <TT>filename</TT>.
    * 
    */
   public static void print (String filename)
   {
     BufferedReader input;
     try {
       if ((new File (filename)).exists()) {
          input = new BufferedReader (new FileReader (filename));
       } else {
          DataInputStream dataInput;
          dataInput = new DataInputStream (
             F2wStructure.class.getClassLoader().getResourceAsStream (
                 "umontreal/iro/lecuyer/hups/dataF2w/" + filename));
          input = new BufferedReader (new InputStreamReader (dataInput));
       }

     String s;
     for (int i = 0; i < 4; i++)
        input.readLine ();
     while ((s = input.readLine ()) != null)
        System.out.println (s);
     input.close ();

     } catch (Exception e) {
       System.out.println ("IO Error: problems reading file " + filename);
       System.exit (1);
     }
   }


   /**
    * This method returns a string containing the polynomial <SPAN CLASS="MATH"><I>P</I>(<I>z</I>)</SPAN> and
    *  the stepping parameter.
    * 
    */
   public String toString ()
   {
      StringBuffer sb = new StringBuffer ("z^");
      sb.append (r);
      for (int j = nbcoeff - 1; j >= 0; j--)
         sb.append (" + (" + coeff[j] + ") z^" + nocoeff[j]);
      sb.append ("   modQ = " + modQ + "    w = " + w + "   step = " + step);
      return sb.toString ();
   }

 
    private void initFromReader (String filename, BufferedReader input, int no)
    {
      int w, r, modQ, step, nbcoeff;
      int coeff[], nocoeff[];
      StringTokenizer line;
      int nl = no + 4;

      try {
        for (int j = 1; j < nl ; j++)
          input.readLine ();

        line = new StringTokenizer (input.readLine ());
        w = Integer.parseInt (line.nextToken ());
        r = Integer.parseInt (line.nextToken ());
        modQ = Integer.parseInt (line.nextToken ());
        step = Integer.parseInt (line.nextToken ());
        nbcoeff = Integer.parseInt (line.nextToken ());
        nocoeff = new int[nbcoeff];
        coeff = new int[nbcoeff];
        for (int i = 0; i < nbcoeff; i++) {
          coeff[i] = Integer.parseInt (line.nextToken ());
          nocoeff[i] = Integer.parseInt (line.nextToken ());
        }
        init (w, r, modQ, step, nbcoeff, coeff, nocoeff);
        input.close ();

      } catch (Exception e) {
        System.out.println ("Input Error: problems reading file " + filename);
        System.exit (1);
      }
    }
  }
  
   /**
    * .
    * <TABLE  WIDTH="317">
    * <TR><TD>
    *    <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_equid_max</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">2358</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1618</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">507</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">26</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">3</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">369</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">26</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">117</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">165</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">36</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">10</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">11</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE><TABLE  WIDTH="317">
    * <TR><TD>
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_equid_sum</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">2276</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1121</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">474</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">37</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">6</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">381</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">65</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">7</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">154</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">688</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">5</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">70</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">9</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">3</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">3</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE>
    * <P><P>
    * <BR>
    * <P><TABLE  WIDTH="317">
    * <TR><TD>
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_mindist_max</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">4</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE><TABLE  WIDTH="317">
    * <TR><TD>
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_mindist_sum</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE>
    * <P>
    * <P><P>
    * <BR><TABLE  WIDTH="317">
    * <TR><TD>
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_tvalue_max</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">7</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">14</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE><TABLE  WIDTH="317">
    * <TR><TD>
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER" COLSPAN=2><SPAN> Directory LFSR_tvalue_sum</SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">Filename</TD>
    * <TD ALIGN="CENTER">Num of poly.</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W5.dat</TD>
    * <TD ALIGN="CENTER">15</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W7.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W8.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR2_W9.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W5.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR3_W6.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W3.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR4_W4.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W2.dat</TD>
    * <TD ALIGN="CENTER">13</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR5_W3.dat</TD>
    * <TD ALIGN="CENTER">2</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W2.dat</TD>
    * <TD ALIGN="CENTER">12</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR6_W3.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR7_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR8_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">f2wR9_W2.dat</TD>
    * <TD ALIGN="CENTER">1</TD>
    * </TR>
    * </TABLE></TD></TR>
    * </TABLE>
    */
