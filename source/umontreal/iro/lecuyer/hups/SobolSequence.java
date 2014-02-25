

/*
 * Class:        SobolSequence
 * Description:  Sobol sequences
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
import java.io.*;
import java.net.MalformedURLException;


/**
 * This class implements digital nets or digital sequences in base 2 formed by
 *  the first <SPAN CLASS="MATH"><I>n</I> = 2<SUP>k</SUP></SPAN> points of a Sobol' sequence.
 * Values of <SPAN CLASS="MATH"><I>n</I></SPAN> up to <SPAN CLASS="MATH">2<SUP>30</SUP></SPAN> are allowed. 
 * <P>
 * In Sobol's proposal, the generator matrices 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> are upper triangular 
 * matrices defined by a set of <SPAN  CLASS="textit">direction numbers</SPAN> 
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>v</I><SUB>j, c</SUB> = <I>m</I><SUB>j, c</SUB>2<SUP>-c</SUP> = &sum;<SUB>l=1</SUB><SUP>c</SUP><I>v</I><SUB>j, c, l</SUB>2<SUP>-l</SUP>,
 * </DIV><P></P>
 * where each <SPAN CLASS="MATH"><I>m</I><SUB>j, c</SUB></SPAN> is an <SPAN  CLASS="textit">odd</SPAN> integer smaller than <SPAN CLASS="MATH">2<SUP>c</SUP></SPAN>, 
 * for 
 * <SPAN CLASS="MATH"><I>c</I> = 1,..., <I>k</I></SPAN> and 
 * <SPAN CLASS="MATH"><I>j</I> = 0,..., <I>s</I> - 1</SPAN>.
 * The digit <SPAN CLASS="MATH"><I>v</I><SUB>j, c, l</SUB></SPAN> is the element <SPAN CLASS="MATH">(<I>l</I>, <I>c</I>)</SPAN> of 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>,
 * so <SPAN CLASS="MATH"><I>v</I><SUB>j, c</SUB></SPAN> represents column <SPAN CLASS="MATH"><I>c</I></SPAN> of 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN>.
 * One can also write
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>m</I><SUB>j, c</SUB> = &sum;<SUB>l=1</SUB><SUP>c</SUP><I>v</I><SUB>j, c, l</SUB>2<SUP>c-l</SUP>,
 * </DIV><P></P>
 * so column <SPAN CLASS="MATH"><I>c</I></SPAN> of 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> contains the <SPAN CLASS="MATH"><I>c</I></SPAN> digits of the binary expansion
 * of <SPAN CLASS="MATH"><I>m</I><SUB>j, c</SUB></SPAN>, from the most to the least significant, 
 * followed by <SPAN CLASS="MATH"><I>w</I> - <I>c</I></SPAN> zeros, where <SPAN CLASS="MATH"><I>w</I></SPAN> is the number of output digits.
 * Since each <SPAN CLASS="MATH"><I>m</I><SUB>j, c</SUB></SPAN> is odd, the first <SPAN CLASS="MATH"><I>k</I></SPAN> rows of each 
 * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> form
 * a non-singular upper triangular matrix whose diagonal elements 
 * are all ones.
 * 
 * <P>
 * For each dimension <SPAN CLASS="MATH"><I>j</I></SPAN>, the integers <SPAN CLASS="MATH"><I>m</I><SUB>j, c</SUB></SPAN> are defined by
 * selecting a primitive polynomial over
 *  
 * <SPAN CLASS="MATH"><B>F</B><SUB>2</SUB></SPAN> of degree <SPAN CLASS="MATH"><I>c</I><SUB>j</SUB></SPAN>,
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>f</I><SUB>j</SUB>(<I>z</I>) = <I>z</I><SUP>c<SUB>j</SUB></SUP> + <I>a</I><SUB>j, 1</SUB><I>z</I><SUP>c<SUB>j</SUB>-1</SUP> + <SUP> ... </SUP> + <I>a</I><SUB>j, c<SUB>j</SUB></SUB>,
 * </DIV><P></P>
 * and the first <SPAN CLASS="MATH"><I>c</I><SUB>j</SUB></SPAN> integers 
 * <SPAN CLASS="MATH"><I>m</I><SUB>j, 0</SUB>,..., <I>m</I><SUB>j, c<SUB>j</SUB>-1</SUB></SPAN>.
 * Then the following integers 
 * <SPAN CLASS="MATH"><I>m</I><SUB>j, c<SUB>j</SUB></SUB>, <I>m</I><SUB>j, c<SUB>j</SUB>+1</SUB>,...</SPAN> are
 * determined by the recurrence
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>m</I><SUB>j, c</SUB> = 2<I>a</I><SUB>j, 1</SUB><I>m</I><SUB>j, c-1</SUB>&#8853;<SUP> ... </SUP>&#8853;2<SUP>c<SUB>j</SUB>-1</SUP><I>a</I><SUB>j, c<SUB>j</SUB>-1</SUB><I>m</I><SUB>j, c-c<SUB>j</SUB>+1</SUB>&#8853;2<SUP>c<SUB>j</SUB></SUP><I>m</I><SUB>j, c-c<SUB>j</SUB></SUB>&#8853;<I>m</I><SUB>j, c-c<SUB>j</SUB></SUB>
 * </DIV><P></P>
 * for <SPAN CLASS="MATH"><I>c</I>&nbsp;&gt;=&nbsp;<I>c</I><SUB>j</SUB></SPAN>, or equivalently,
 * 
 * <P></P>
 * <DIV ALIGN="CENTER" CLASS="mathdisplay">
 * <I>v</I><SUB>j, c, l</SUB> = <I>a</I><SUB>j, 1</SUB><I>v</I><SUB>j, c-1, l</SUB>&#8853;<SUP> ... </SUP>&#8853;<I>a</I><SUB>j, c<SUB>j</SUB>-1</SUB><I>v</I><SUB>j, c-c<SUB>j</SUB>+1, l</SUB>&#8853;<I>v</I><SUB>j, c-c<SUB>j</SUB>, l</SUB>&#8853;<I>v</I><SUB>j, c-c<SUB>j</SUB>, l+c<SUB>j</SUB></SUB>
 * </DIV><P></P>
 * for <SPAN CLASS="MATH"><I>l</I>&nbsp;&gt;=&nbsp; 0</SPAN>, where <SPAN CLASS="MATH">&#8853;</SPAN> means bitwise exclusive or
 * (i.e., bitwise addition modulo 2).
 * Sobol' has shown that with this construction, if the 
 * primitive polynomials <SPAN CLASS="MATH"><I>f</I><SUB>j</SUB>(<I>z</I>)</SPAN> are all distinct, one obtains
 * a <SPAN CLASS="MATH">(<I>t</I>, <I>s</I>)</SPAN>-sequence whose <SPAN CLASS="MATH"><I>t</I></SPAN>-value does not exceed
 * 
 * <SPAN CLASS="MATH"><I>c</I><SUB>0</SUB> + <SUP> ... </SUP> + <I>c</I><SUB>s-1</SUB> + 1 - <I>s</I></SPAN>.
 * He then suggested to list the set of all primitive polynomials over
 * 
 * <SPAN CLASS="MATH"><B>F</B><SUB>2</SUB></SPAN> by increasing order of degree, starting with 
 * <SPAN CLASS="MATH"><I>f</I><SUB>0</SUB>(<I>z</I>)&#8801;1</SPAN>
 * (whose corresponding matrix 
 * <SPAN CLASS="MATH"><B>C</B><SUB>0</SUB></SPAN> is the identity), and take <SPAN CLASS="MATH"><I>f</I><SUB>j</SUB>(<I>z</I>)</SPAN>
 * as the <SPAN CLASS="MATH">(<I>j</I> + 1)</SPAN>th polynomial in the list, for <SPAN CLASS="MATH"><I>j</I>&nbsp;&gt;=&nbsp; 0</SPAN>.
 * 
 * <P>
 * This list of primitive polynomials, as well as default choices
 * for the direction numbers, are stored in precomputed tables.
 * The ordered list of primitive polynomials  was taken from Florent Chabaud's web site, 
 * at <TT><A NAME="tex2html1"
 *   HREF="http://fchabaud.free.fr/">http://fchabaud.free.fr/</A></TT>.
 * Each polynomial <SPAN CLASS="MATH"><I>f</I><SUB>j</SUB>(<I>z</I>)</SPAN> is stored in the form of the integer
 * 
 * <SPAN CLASS="MATH">2<SUP>c<SUB>j</SUB></SUP> + <I>a</I><SUB>j, 1</SUB>2<SUP>c<SUB>j</SUB>-1</SUP> + <SUP> ... </SUP> + <I>a</I><SUB>j, c<SUB>j</SUB></SUB></SPAN>, whose binary 
 * representation gives the polynomial coefficients.
 * 
 * <P>
 * For the set of direction numbers, there are several possibilities
 * based on different selection criteria.
 * The original values proposed by Sobol' and implemented in the code of
 * Bratley and Fox for <SPAN CLASS="MATH"><I>j</I>&nbsp;&lt;=&nbsp;40</SPAN> 
 * were selected in terms of his properties <SPAN CLASS="MATH"><I>A</I></SPAN> and <SPAN CLASS="MATH"><I>A'</I></SPAN>, 
 * which are equivalent to <SPAN CLASS="MATH"><I>s</I></SPAN>-distribution with one and 
 * two bits of accuracy, respectively.
 * The default direction numbers used here have been taken from 
 * Lemieux et al.
 * For <SPAN CLASS="MATH"><I>j</I>&nbsp;&lt;=&nbsp;40</SPAN>, they are the same as in 
 *  Bratley and Fox.
 * Several files of parameters for Sobol sequences are given on F. Kuo's
 * Web site at <TT><A NAME="tex2html2"
 *   HREF="http://web.maths.unsw.edu.au/~fkuo/sobol/index.html">http://web.maths.unsw.edu.au/~fkuo/sobol/index.html</A></TT>.
 * 
 */
public class SobolSequence extends DigitalSequenceBase2  { 

    // Maximal dimension for primitive polynomials included in this file
    protected static final int MAXDIM    = 360;
    protected static final int MAXDEGREE = 18;  // Of primitive polynomial
    private String filename = null;



   /**
    * Constructs a new digital net with <SPAN CLASS="MATH"><I>n</I> = 2<SUP>k</SUP></SPAN> points and <SPAN CLASS="MATH"><I>w</I></SPAN> 
    *    output digits, in dimension <TT>dim</TT>, formed by taking the first
    *    <SPAN CLASS="MATH"><I>n</I></SPAN> points of the Sobol' sequence.
    *    The predefined generator matrices 
    * <SPAN CLASS="MATH"><B>C</B><SUB>j</SUB></SPAN> are <SPAN CLASS="MATH"><I>w</I>&#215;<I>k</I></SPAN>. Restrictions: 
    * <SPAN CLASS="MATH">0&nbsp;&lt;=&nbsp;<I>k</I>&nbsp;&lt;=&nbsp;30</SPAN>, <SPAN CLASS="MATH"><I>k</I>&nbsp;&lt;=&nbsp;<I>w</I></SPAN> and <TT>dim</TT> <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;360</SPAN>.
    * 
    * @param k there will be 2^k points
    * 
    * @param w number of output digits
    * 
    *    @param dim dimension of the point set
    * 
    * 
    */
   public SobolSequence (int k, int w, int dim)  {
      init (k, w, w, dim);
   }

   private void init (int k, int r, int w, int dim) {
      if (filename == null)
         if ((dim < 1) || (dim > MAXDIM))
            throw new IllegalArgumentException 
               ("Dimension for SobolSequence must be > 0 and <= " + MAXDIM);
      else
         if (dim < 1)
            throw new IllegalArgumentException 
               ("Dimension for SobolSequence must be > 0");

      if (r < k || w < r || w > MAXBITS || k >= MAXBITS) 
         throw new IllegalArgumentException
            ("One must have k < 31 and k <= r <= w <= 31 for SobolSequence");
      numCols   = k;
      numRows   = r;   // Not used!
      outDigits = w;
      numPoints = (1 << k);
      this.dim  = dim;
      normFactor = 1.0 / ((double) (1L << (outDigits)));
      genMat = new int[dim * numCols];
      initGenMat();
   } 



   /**
    * Constructs a Sobol point set with <SPAN  CLASS="textit">at least</SPAN> <TT>n</TT> points  and 31 
    *    output digits, in dimension <TT>dim</TT>. Equivalent to 
    *   <TT>SobolSequence (k, 31, dim)</TT> with 
    * <SPAN CLASS="MATH"><I>k</I> = ceil(log<SUB>2</SUB><I>n</I>)</SPAN>.
    * 
    * @param dim dimension of the point set
    * 
    *    @param n minimal number of points
    * 
    * 
    */
   public SobolSequence (int n, int dim)  {
      numCols = MAXBITS;      // Defined in PointSet.  
      while (((n >> numCols) & 1) == 0)
         numCols--;
      if (1 << numCols != n)
         numCols++;
      init (numCols, MAXBITS, MAXBITS, dim);
    }
 

   /**
    * Constructs a new digital net using the direction numbers provided in file
    *    <TT>filename</TT>. The net has <SPAN CLASS="MATH"><I>n</I> = 2<SUP>k</SUP></SPAN> points, <SPAN CLASS="MATH"><I>w</I></SPAN> output digits and
    *    dimension <TT>dim</TT>. The file can be either on the user's host, or
    *    somewhere on the Internet: in that case, the full <SPAN  CLASS="textbf">url</SPAN> address must
    *    be given using either the <SPAN  CLASS="textit">http</SPAN> or <SPAN  CLASS="textit">ftp</SPAN> protocol. For
    *    example:
    * 
    * <P>
    * <TT>net = new SobolSequence(
    * <BR>      "http://web.maths.unsw.edu.au/&#732;fkuo/sobol/joe-kuo-6.16900", k, w, dim);</TT>
    * 
    * <P>
    * The file must have the following format
    * (the first line is treated as a comment by the program and discarded):
    * <DIV ALIGN="CENTER">
    * <TABLE CELLPADDING=3 BORDER="1">
    * <TR><TD ALIGN="CENTER">dim</TD>
    * <TD ALIGN="LEFT"><SPAN CLASS="MATH"><I>s</I></SPAN></TD>
    * <TD ALIGN="LEFT"><SPAN CLASS="MATH"><I>a</I></SPAN></TD>
    * <TD ALIGN="LEFT"><SPAN CLASS="MATH"><I>m</I><SUB>i</SUB></SPAN></TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">2</TD>
    * <TD ALIGN="LEFT">1</TD>
    * <TD ALIGN="LEFT">0</TD>
    * <TD ALIGN="LEFT">1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">3</TD>
    * <TD ALIGN="LEFT">2</TD>
    * <TD ALIGN="LEFT">1</TD>
    * <TD ALIGN="LEFT">1 3</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">4</TD>
    * <TD ALIGN="LEFT">3</TD>
    * <TD ALIGN="LEFT">1</TD>
    * <TD ALIGN="LEFT">1 3 1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">5</TD>
    * <TD ALIGN="LEFT">3</TD>
    * <TD ALIGN="LEFT">2</TD>
    * <TD ALIGN="LEFT">1 1 1</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">6</TD>
    * <TD ALIGN="LEFT">4</TD>
    * <TD ALIGN="LEFT">1</TD>
    * <TD ALIGN="LEFT">1 1 3 3</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">7</TD>
    * <TD ALIGN="LEFT">4</TD>
    * <TD ALIGN="LEFT">4</TD>
    * <TD ALIGN="LEFT">1 3 5 13</TD>
    * </TR>
    * <TR><TD ALIGN="CENTER">&nbsp;</TD>
    * <TD ALIGN="LEFT">&vellip;</TD>
    * <TD ALIGN="LEFT">&vellip;</TD>
    * <TD ALIGN="LEFT">&nbsp;</TD>
    * </TR>
    * </TABLE>
    * </DIV>
    * dim is the
    * dimension, <SPAN CLASS="MATH"><I>s</I></SPAN> the degree of the polynomial, the binary representation of <SPAN CLASS="MATH"><I>a</I></SPAN>
    *   gives the inner coefficients of the polynomial
    *  (the first and the last coefficients are always 1), and <SPAN CLASS="MATH"><I>m</I><SUB>i</SUB></SPAN> are the 
    * direction numbers. Thus if 
    * <SPAN CLASS="MATH"><I>a</I> = (<I>a</I><SUB>1</SUB><I>a</I><SUB>2</SUB>&#8230;<I>a</I><SUB>s-1</SUB>)<SUB>2</SUB></SPAN> for a given <SPAN CLASS="MATH"><I>s</I></SPAN>,
    * then the polynomial is 
    * <SPAN CLASS="MATH"><I>x</I><SUP>s</SUP> + <I>a</I><SUB>1</SUB><I>x</I><SUP>s-1</SUP> + <I>a</I><SUB>2</SUB><I>x</I><SUP>s-2</SUP> + <SUP> ... </SUP> + <I>a</I><SUB>s-1</SUB><I>x</I> + 1</SPAN>. 
    * For example, if <SPAN CLASS="MATH"><I>s</I> = 4</SPAN> and 
    * <SPAN CLASS="MATH"><I>a</I> = 4 = 100<SUB>2</SUB></SPAN>, then the
    * polynomial is 
    * <SPAN CLASS="MATH"><I>x</I><SUP>4</SUP> + <I>x</I><SUP>3</SUP> + 1</SPAN>. 
    * 
    * @param k number of points is <SPAN CLASS="MATH">2<SUP>k</SUP></SPAN>
    * 
    *    @param w number of output digits
    * 
    *    @param dim dimension of the point set
    * 
    *    @param filename file containing the direction numbers
    * 
    * 
    */
   public SobolSequence (String filename, int k, int w, int dim)  {

      poly_from_file = new int[dim];
      for (int i = 0; i < dim; i++)
         poly_from_file[i] = 0;

      minit_from_file = new int[dim][MAXDEGREE];
      for (int i = 0; i < dim; i++) {
         for (int j = 0; j < MAXDEGREE; j++) {
            minit_from_file[i][j] = 0;
         }
      }

      // Dimension d = 1
      int d = 1;
      poly_from_file[d - 1] = 1;

      // Read the direction number file up to a certain number of dimension dim
      try {
         // If filename can be found starting from the program's directory,
         // it will be used; otherwise, the filename in the Jar archive will
         // be used.
         int prev_d = 1;

         BufferedReader reader;

         if (filename.startsWith("http") || filename.startsWith("ftp"))
            reader = DigitalNetFromFile.openURL(filename);
         else
            reader = new BufferedReader(new FileReader(filename));

         // First line of file is a comment; discard it
         String line = reader.readLine();
         String[] tokens;

         while ((line = reader.readLine()) != null) {
            tokens = line.split("[\t ]+");

            // Direction number lines from dimension 2 and up
            if (tokens.length < 4) {
               System.err.println("\nBad direction number file format!\n");
               System.exit(1);
            }

            // Parse dim d, polynomial degree s and coefficients a
            d = Integer.parseInt(tokens[0]);

            int s = Integer.parseInt(tokens[1]);
            int a = Integer.parseInt(tokens[2]);

            if (s + 3 != tokens.length) {
               System.err.println("\nBad direction number file format!\n");
               System.exit(1);
            }

            if (d != prev_d + 1) {
               System.err.println("Dimension in file shall be in ");
               System.err.println("increasing order, one per line!");
               System.exit(1);
            }
            prev_d = d;

            // If d in the file exceeds dim, stop reading!
            if (d > dim)
               break;

            poly_from_file[d - 1] = (1 << s) ^ (a << 1) ^ 1;

            // Parse the s direction numbers
            for (int i = 0; i < s; i++)
               minit_from_file[d - 2][i] = Integer.parseInt(tokens[i + 3]);
         } // end while

      } catch (MalformedURLException e) {
         System.err.println ("   Invalid URL address:   " + filename);
         System.exit(1);

      } catch (IOException e) {
         System.err.println("Error: " + e);
         System.exit(1);
      }

      if (dim > d) {
         System.err.printf("\n\nNot enough dimension in file: %s", filename);
         System.exit(1);
      }

      this.filename = filename;

      init(k, w, w, dim);
   }
 

   public String toString() {
      StringBuffer sb = new StringBuffer ("Sobol sequence:" +
                                           PrintfFormat.NEWLINE);
      sb.append (super.toString());
      return sb.toString();
   }


   public void extendSequence (int k) {
      int start, degree, nextCol;
      int i, j, c;
      int oldNumCols = numCols;
      int[] oldGenMat = genMat;  // Save old generating matrix.
 
      numCols   = k;
      numPoints = (1 << k);
      genMat = new int[dim * numCols];

      // the first dimension, j = 0.
      for (c = 0; c < numCols; c++)
         genMat[c] = (1 << (outDigits-c-1));

      // the other dimensions j > 0.
      for (j = 1; j < dim; j++) {
         // find the degree of primitive polynomial f_j
         for (degree = MAXDEGREE;  ((poly[j] >> degree) & 1) == 0; degree--)
            ;
         // Get initial direction numbers m_{j,0},..., m_{j,degree-1}.
         start = j * numCols;
         for (c = 0; (c < degree && c < numCols); c++)
            genMat[start+c] = minit[j-1][c] << (outDigits-c-1);

         // Compute the following ones via the recursion.
         for (c = degree; c < numCols; c++) {
            if (c < oldNumCols)
               genMat[start+c] = oldGenMat[j*oldNumCols + c];
            else {
               nextCol = genMat[start+c-degree] >> degree;
               for (i = 0; i < degree; i++)
                  if (((poly[j] >> i) & 1) == 1)
                     nextCol ^= genMat[start+c-degree+i];
               genMat[start+c] = nextCol;
            }
         }
      } 
   }


   // Initializes the generator matrices for a sequence.
   private void initGenMat()  {
      int start, degree, nextCol;
      int i, j, c;

      // the first dimension, j = 0.
      for (c = 0; c < numCols; c++)
         genMat[c] = (1 << (outDigits-c-1));

      // the other dimensions j > 0.
      for (j = 1; j < dim; j++) {
         // if a direction number file was provided, use it
         int polynomial = (filename != null ? poly_from_file[j] : poly[j]);
         // find the degree of primitive polynomial f_j
         for (degree = MAXDEGREE; ((polynomial >> degree) & 1) == 0; degree--)
            ;
         // Get initial direction numbers m_{j,0},..., m_{j,degree-1}.
         start = j * numCols;
         for (c = 0; (c < degree && c < numCols); c++) {
             int m_i = (filename != null ? 
                        minit_from_file[j-1][c] : minit[j-1][c]);
             genMat[start+c] = m_i << (outDigits-c-1);
         }

         // Compute the following ones via the recursion.
         for (c = degree; c < numCols; c++) {
            nextCol = genMat[start+c-degree] >> degree;
            for (i = 0; i < degree; i++)
               if (((polynomial >> i) & 1) == 1)
                  nextCol ^= genMat[start+c-degree+i];
            genMat[start+c] = nextCol;
         }
      } 
   }
    
/*
   // Initializes the generator matrices for a net.
   protected void initGenMatNet()  {
      int start, degree, nextCol;
      int i, j, c;

      // the first dimension, j = 0.
      for (c = 0; c < numCols; c++)
         genMat[c] = (1 << (outDigits-numCols+c));

      // the second dimension, j = 1.
      for (c = 0; c < numCols; c++)
         genMat[numCols+c] = (1 << (outDigits-c-1));

      // the other dimensions j > 1.
      for (j = 2; j < dim; j++) {
         // find the degree of primitive polynomial f_j
         for (degree = MAXDEGREE; ((poly[j-1] >> degree) & 1) == 0; degree--); 
         // Get initial direction numbers m_{j,0},..., m_{j,degree-1}.
         start = j * numCols;
         for (c = 0; (c < degree && c < numCols); c++)
            genMat[start+c] = minit[j-2][c] << (outDigits-c-1);

         // Compute the following ones via the recursion.
         for (c = degree; c < numCols; c++) {
            nextCol = genMat[start+c-degree] >> degree;
            for (i = 0; i < degree; i++)
               if (((poly[j-1] >> i) & 1) == 1)
                  nextCol ^= genMat[start+c-degree+i];
            genMat[start+c] = nextCol;
         }
      } 
   }
*/

    // *******************************************************
    // The ordered list of first MAXDIM primitive polynomials.

    protected int[] poly_from_file;

    protected static final int[] poly = {
     1, 3, 7, 11, 13, 19, 25, 37, 59,
     47, 61, 55, 41, 67, 97, 91, 109, 103, 
     115, 131, 193, 137, 145, 143, 241, 157, 185, 
     167, 229, 171, 213, 191, 253, 203, 211, 239, 
     247, 285, 369, 299, 425, 301, 361, 333, 357, 
     351, 501, 355, 397, 391, 451, 463, 487, 529, 
     545, 539, 865, 557, 721, 563, 817, 601, 617, 
     607, 1001, 623, 985, 631, 953, 637, 761, 647, 
     901, 661, 677, 675, 789, 687, 981, 695, 949, 
     701, 757, 719, 973, 731, 877, 787, 803, 799, 
     995, 827, 883, 847, 971, 859, 875, 895, 1019, 
     911, 967, 1033, 1153, 1051, 1729, 1063, 1825, 1069, 
     1441, 1125, 1329, 1135, 1969, 1163, 1673, 1221, 1305, 
     1239, 1881, 1255, 1849, 1267, 1657, 1279, 2041, 1293, 
     1413, 1315, 1573, 1341, 1509, 1347, 1557, 1367, 1877, 
     1387, 1717, 1423, 1933, 1431, 1869, 1479, 1821, 1527, 
     1917, 1531, 1789, 1555, 1603, 1591, 1891, 1615, 1939, 
     1627, 1747, 1663, 2035, 1759, 2011, 1815, 1863, 2053, 
     2561, 2071, 3713, 2091, 3393, 2093, 2881, 2119, 3617, 
     2147, 3169, 2149, 2657, 2161, 2273, 2171, 3553, 2189, 
     2833, 2197, 2705, 2207, 3985, 2217, 2385, 2225, 2257, 
     2255, 3889, 2279, 3697, 2283, 3441, 2293, 2801, 2317, 
     2825, 2323, 3209, 2341, 2633, 2345, 2377, 2363, 3529, 
     2365, 3017, 2373, 2601, 2395, 3497, 2419, 3305, 2421, 
     2793, 2431, 4073, 2435, 3097, 2447, 3865, 2475, 3417, 
     2477, 2905, 2489, 2521, 2503, 3641, 2533, 2681, 2551, 
     3833, 2567, 3589, 2579, 3205, 2581, 2693, 2669, 2917, 
     2687, 4069, 2717, 2965, 2727, 3669, 2731, 3413, 2739, 
     3285, 2741, 2773, 2783, 4021, 2799, 3957, 2811, 3573, 
     2819, 3085, 2867, 3277, 2879, 4045, 2891, 3373, 2911, 
     4013, 2927, 3949, 2941, 3053, 2951, 3613, 2955, 3357, 
     2963, 3229, 2991, 3933, 2999, 3805, 3005, 3037, 3035, 
     3517, 3047, 3709, 3083, 3331, 3103, 3971, 3159, 3747, 
     3179, 3427, 3187, 3299, 3223, 3731, 3227, 3475, 3251, 
     3283, 3263, 4051, 3271, 3635, 3319, 3827, 3343, 3851, 
     3367, 3659, 3399, 3627, 3439, 3947, 3487, 3995, 3515, 
     3547, 3543, 3771, 3559, 3707, 3623, 3655, 3679, 4007, 
     3743, 3991, 3791, 3895, 4179, 6465, 4201, 4801, 4219, 
     7105, 4221, 6081, 4249, 4897, 4305, 4449, 4331, 6881, 
     4359, 7185, 4383, 7953, 4387, 6289, 4411, 7057, 4431
     };


    // The direction numbers.  For j > 0 and c < c_j,
    // minit[j-1][c] contains the integer m_{j,c}.
    // The values for j=0 are not stored, since C_0 is the identity matrix.

   protected int minit_from_file[][];

   protected static final int minit[][] = {
     {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 3, 7, 0, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 3, 9, 9, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 7, 13, 3, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 5, 11, 27, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 5, 1, 15, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 7, 3, 29, 0, 0, 0, 0, 0, 0, 0},
     {1, 3, 7, 7, 21, 0, 0, 0, 0, 0, 0, 0},
     {1, 1, 1, 9, 23, 37, 0, 0, 0, 0, 0, 0},
     {1, 3, 3, 5, 19, 33, 0, 0, 0, 0, 0, 0},
     {1, 1, 3, 13, 11, 7, 0, 0, 0, 0, 0, 0},
     {1, 1, 7, 13, 25, 5, 0, 0, 0, 0, 0, 0},
     {1, 3, 5, 11, 7, 11, 0, 0, 0, 0, 0, 0},
     {1, 1, 1, 3, 13, 39, 0, 0, 0, 0, 0, 0},
     {1, 3, 1, 15, 17, 63, 13, 0, 0, 0, 0, 0},
     {1, 1, 5, 5, 1, 27, 33, 0, 0, 0, 0, 0},
     {1, 3, 3, 3, 25, 17, 115, 0, 0, 0, 0, 0},
     {1, 1, 3, 15, 29, 15, 41, 0, 0, 0, 0, 0},
     {1, 3, 1, 7, 3, 23, 79, 0, 0, 0, 0, 0},
     {1, 3, 7, 9, 31, 29, 17, 0, 0, 0, 0, 0},
     {1, 1, 5, 13, 11, 3, 29, 0, 0, 0, 0, 0},
     {1, 3, 1, 9, 5, 21, 119, 0, 0, 0, 0, 0},
     {1, 1, 3, 1, 23, 13, 75, 0, 0, 0, 0, 0},
     {1, 3, 3, 11, 27, 31, 73, 0, 0, 0, 0, 0},
     {1, 1, 7, 7, 19, 25, 105, 0, 0, 0, 0, 0},
     {1, 3, 5, 5, 21, 9, 7, 0, 0, 0, 0, 0},
     {1, 1, 1, 15, 5, 49, 59, 0, 0, 0, 0, 0},
     {1, 1, 1, 1, 1, 33, 65, 0, 0, 0, 0, 0},
     {1, 3, 5, 15, 17, 19, 21, 0, 0, 0, 0, 0},
     {1, 1, 7, 11, 13, 29, 3, 0, 0, 0, 0, 0},
     {1, 3, 7, 5, 7, 11, 113, 0, 0, 0, 0, 0},
     {1, 1, 5, 3, 15, 19, 61, 0, 0, 0, 0, 0},
     {1, 3, 1, 1, 9, 27, 89, 7, 0, 0, 0, 0},
     {1, 1, 3, 7, 31, 15, 45, 23, 0, 0, 0, 0},
     {1, 3, 3, 9, 9, 25, 107, 39, 0, 0, 0, 0},
     {1, 1, 3, 13, 7, 35, 61, 91, 0, 0, 0, 0},
     {1, 1, 7, 11, 5, 35, 55, 75, 0, 0, 0, 0},
     {1, 3, 5, 5, 11, 23, 29, 139, 0, 0, 0, 0},
     {1, 1, 1, 7, 11, 15, 17, 81, 0, 0, 0, 0},
     {1, 1, 7, 9, 5, 57, 79, 103, 0, 0, 0, 0},
     {1, 1, 7, 13, 19, 5, 5, 185, 0, 0, 0, 0},
     {1, 3, 1, 3, 13, 57, 97, 131, 0, 0, 0, 0},
     {1, 1, 5, 5, 21, 25, 125, 197, 0, 0, 0, 0},
     {1, 3, 3, 9, 31, 11, 103, 201, 0, 0, 0, 0},
     {1, 1, 5, 3, 7, 25, 51, 121, 0, 0, 0, 0},
     {1, 3, 7, 15, 19, 53, 73, 189, 0, 0, 0, 0},
     {1, 1, 1, 15, 19, 55, 27, 183, 0, 0, 0, 0},
     {1, 1, 7, 13, 3, 29, 109, 69, 0, 0, 0, 0},
     {1, 1, 5, 15, 15, 23, 15, 1, 57, 0, 0, 0},
     {1, 3, 1, 3, 23, 55, 43, 143, 397, 0, 0, 0},
     {1, 1, 3, 11, 29, 9, 35, 131, 411, 0, 0, 0},
     {1, 3, 1, 7, 27, 39, 103, 199, 277, 0, 0, 0},
     {1, 3, 7, 3, 19, 55, 127, 67, 449, 0, 0, 0},
     {1, 3, 7, 3, 5, 29, 45, 85, 3, 0, 0, 0},
     {1, 3, 5, 5, 13, 23, 75, 245, 453, 0, 0, 0},
     {1, 3, 1, 15, 21, 47, 3, 77, 165, 0, 0, 0},
     {1, 1, 7, 9, 15, 5, 117, 73, 473, 0, 0, 0},
     {1, 3, 1, 9, 1, 21, 13, 173, 313, 0, 0, 0},
     {1, 1, 7, 3, 11, 45, 63, 77, 49, 0, 0, 0},
     {1, 1, 1, 1, 1, 25, 123, 39, 259, 0, 0, 0},
     {1, 1, 1, 5, 23, 11, 59, 11, 203, 0, 0, 0},
     {1, 3, 3, 15, 21, 1, 73, 71, 421, 0, 0, 0},
     {1, 1, 5, 11, 15, 31, 115, 95, 217, 0, 0, 0},
     {1, 1, 3, 3, 7, 53, 37, 43, 439, 0, 0, 0},
     {1, 1, 1, 1, 27, 53, 69, 159, 321, 0, 0, 0},
     {1, 1, 5, 15, 29, 17, 19, 43, 449, 0, 0, 0},
     {1, 1, 3, 9, 1, 55, 121, 205, 255, 0, 0, 0},
     {1, 1, 3, 11, 9, 47, 107, 11, 417, 0, 0, 0},
     {1, 1, 1, 5, 17, 25, 21, 83, 95, 0, 0, 0},
     {1, 3, 5, 13, 31, 25, 61, 157, 407, 0, 0, 0},
     {1, 1, 7, 9, 25, 33, 41, 35, 17, 0, 0, 0},
     {1, 3, 7, 15, 13, 39, 61, 187, 461, 0, 0, 0},
     {1, 3, 7, 13, 5, 57, 23, 177, 435, 0, 0, 0},
     {1, 1, 3, 15, 11, 27, 115, 5, 337, 0, 0, 0},
     {1, 3, 7, 3, 15, 63, 61, 171, 339, 0, 0, 0},
     {1, 3, 3, 13, 15, 61, 59, 47, 1, 0, 0, 0},
     {1, 1, 5, 15, 13, 5, 39, 83, 329, 0, 0, 0},
     {1, 1, 5, 5, 5, 27, 25, 39, 301, 0, 0, 0},
     {1, 1, 5, 11, 31, 41, 35, 233, 27, 0, 0, 0},
     {1, 3, 5, 15, 7, 37, 119, 171, 419, 0, 0, 0},
     {1, 3, 5, 5, 3, 29, 21, 189, 417, 0, 0, 0},
     {1, 1, 1, 1, 21, 41, 117, 119, 351, 0, 0, 0},
     {1, 1, 3, 1, 7, 27, 87, 19, 213, 0, 0, 0},
     {1, 1, 1, 1, 17, 7, 97, 217, 477, 0, 0, 0},
     {1, 1, 7, 1, 29, 61, 103, 231, 269, 0, 0, 0},
     {1, 1, 7, 13, 9, 27, 107, 207, 311, 0, 0, 0},
     {1, 1, 7, 5, 25, 21, 107, 179, 423, 0, 0, 0},
     {1, 3, 5, 11, 7, 1, 17, 245, 281, 0, 0, 0},
     {1, 3, 5, 9, 1, 5, 53, 59, 125, 0, 0, 0},
     {1, 1, 7, 1, 31, 57, 71, 245, 125, 0, 0, 0},
     {1, 1, 7, 5, 5, 57, 53, 253, 441, 0, 0, 0},
     {1, 3, 1, 13, 19, 35, 119, 235, 381, 0, 0, 0},
     {1, 3, 1, 7, 19, 59, 115, 33, 361, 0, 0, 0},
     {1, 1, 3, 5, 13, 1, 49, 143, 501, 0, 0, 0},
     {1, 1, 3, 5, 1, 63, 101, 85, 189, 0, 0, 0},
     {1, 1, 5, 11, 27, 63, 13, 131, 5, 0, 0, 0},
     {1, 1, 5, 7, 15, 45, 75, 59, 455, 585, 0, 0},
     {1, 3, 1, 3, 7, 7, 111, 23, 119, 959, 0, 0},
     {1, 3, 3, 9, 11, 41, 109, 163, 161, 879, 0, 0},
     {1, 3, 5, 1, 21, 41, 121, 183, 315, 219, 0, 0},
     {1, 1, 3, 9, 15, 3, 9, 223, 441, 929, 0, 0},
     {1, 1, 7, 9, 3, 5, 93, 57, 253, 457, 0, 0},
     {1, 1, 7, 13, 15, 29, 83, 21, 35, 45, 0, 0},
     {1, 1, 3, 7, 13, 61, 119, 219, 85, 505, 0, 0},
     {1, 1, 3, 3, 17, 13, 35, 197, 291, 109, 0, 0},
     {1, 1, 3, 3, 5, 1, 113, 103, 217, 253, 0, 0},
     {1, 1, 7, 1, 15, 39, 63, 223, 17, 9, 0, 0},
     {1, 3, 7, 1, 17, 29, 67, 103, 495, 383, 0, 0},
     {1, 3, 3, 15, 31, 59, 75, 165, 51, 913, 0, 0},
     {1, 3, 7, 9, 5, 27, 79, 219, 233, 37, 0, 0},
     {1, 3, 5, 15, 1, 11, 15, 211, 417, 811, 0, 0},
     {1, 3, 5, 3, 29, 27, 39, 137, 407, 231, 0, 0},
     {1, 1, 3, 5, 29, 43, 125, 135, 109, 67, 0, 0},
     {1, 1, 1, 5, 11, 39, 107, 159, 323, 381, 0, 0},
     {1, 1, 1, 1, 9, 11, 33, 55, 169, 253, 0, 0},
     {1, 3, 5, 5, 11, 53, 63, 101, 251, 897, 0, 0},
     {1, 3, 7, 1, 25, 15, 83, 119, 53, 157, 0, 0},
     {1, 3, 5, 13, 5, 5, 3, 195, 111, 451, 0, 0},
     {1, 3, 1, 15, 11, 1, 19, 11, 307, 777, 0, 0},
     {1, 3, 7, 11, 5, 5, 17, 231, 345, 981, 0, 0},
     {1, 1, 3, 3, 1, 33, 83, 201, 57, 475, 0, 0},
     {1, 3, 7, 7, 17, 13, 35, 175, 499, 809, 0, 0},
     {1, 1, 5, 3, 3, 17, 103, 119, 499, 865, 0, 0},
     {1, 1, 1, 11, 27, 25, 37, 121, 401, 11, 0, 0},
     {1, 1, 1, 11, 9, 25, 25, 241, 403, 3, 0, 0},
     {1, 1, 1, 1, 11, 1, 39, 163, 231, 573, 0, 0},
     {1, 1, 1, 13, 13, 21, 75, 185, 99, 545, 0, 0},
     {1, 1, 1, 15, 3, 63, 69, 11, 173, 315, 0, 0},
     {1, 3, 5, 15, 11, 3, 95, 49, 123, 765, 0, 0},
     {1, 1, 1, 15, 3, 63, 77, 31, 425, 711, 0, 0},
     {1, 1, 7, 15, 1, 37, 119, 145, 489, 583, 0, 0},
     {1, 3, 5, 15, 3, 49, 117, 211, 165, 323, 0, 0},
     {1, 3, 7, 1, 27, 63, 77, 201, 225, 803, 0, 0},
     {1, 1, 1, 11, 23, 35, 67, 21, 469, 357, 0, 0},
     {1, 1, 7, 7, 9, 7, 25, 237, 237, 571, 0, 0},
     {1, 1, 3, 15, 29, 5, 107, 109, 241, 47, 0, 0},
     {1, 3, 5, 11, 27, 63, 29, 13, 203, 675, 0, 0},
     {1, 1, 3, 9, 9, 11, 103, 179, 449, 263, 0, 0},
     {1, 3, 5, 11, 29, 63, 53, 151, 259, 223, 0, 0},
     {1, 1, 3, 7, 9, 25, 5, 197, 237, 163, 0, 0},
     {1, 3, 7, 13, 5, 57, 67, 193, 147, 241, 0, 0},
     {1, 1, 5, 15, 15, 33, 17, 67, 161, 341, 0, 0},
     {1, 1, 3, 13, 17, 43, 21, 197, 441, 985, 0, 0},
     {1, 3, 1, 5, 15, 33, 33, 193, 305, 829, 0, 0},
     {1, 1, 1, 13, 19, 27, 71, 187, 477, 239, 0, 0},
     {1, 1, 1, 9, 9, 17, 41, 177, 229, 983, 0, 0},
     {1, 3, 5, 9, 15, 45, 97, 205, 43, 767, 0, 0},
     {1, 1, 1, 9, 31, 31, 77, 159, 395, 809, 0, 0},
     {1, 3, 3, 3, 29, 19, 73, 123, 165, 307, 0, 0},
     {1, 3, 1, 7, 5, 11, 77, 227, 355, 403, 0, 0},
     {1, 3, 5, 5, 25, 31, 1, 215, 451, 195, 0, 0},
     {1, 3, 7, 15, 29, 37, 101, 241, 17, 633, 0, 0},
     {1, 1, 5, 1, 11, 3, 107, 137, 489, 5, 0, 0},
     {1, 1, 1, 7, 19, 19, 75, 85, 471, 355, 0, 0},
     {1, 1, 3, 3, 9, 13, 113, 167, 13, 27, 0, 0},
     {1, 3, 5, 11, 21, 3, 89, 205, 377, 307, 0, 0},
     {1, 1, 1, 9, 31, 61, 65, 9, 391, 141, 867, 0},
     {1, 1, 1, 9, 19, 19, 61, 227, 241, 55, 161, 0},
     {1, 1, 1, 11, 1, 19, 7, 233, 463, 171, 1941, 0},
     {1, 1, 5, 7, 25, 13, 103, 75, 19, 1021, 1063, 0},
     {1, 1, 1, 15, 17, 17, 79, 63, 391, 403, 1221, 0},
     {1, 3, 3, 11, 29, 25, 29, 107, 335, 475, 963, 0},
     {1, 3, 5, 1, 31, 33, 49, 43, 155, 9, 1285, 0},
     {1, 1, 5, 5, 15, 47, 39, 161, 357, 863, 1039, 0},
     {1, 3, 7, 15, 1, 39, 47, 109, 427, 393, 1103, 0},
     {1, 1, 1, 9, 9, 29, 121, 233, 157, 99, 701, 0},
     {1, 1, 1, 7, 1, 29, 75, 121, 439, 109, 993, 0},
     {1, 1, 1, 9, 5, 1, 39, 59, 89, 157, 1865, 0},
     {1, 1, 5, 1, 3, 37, 89, 93, 143, 533, 175, 0},
     {1, 1, 3, 5, 7, 33, 35, 173, 159, 135, 241, 0},
     {1, 1, 1, 15, 17, 37, 79, 131, 43, 891, 229, 0},
     {1, 1, 1, 1, 1, 35, 121, 177, 397, 1017, 583, 0},
     {1, 1, 3, 15, 31, 21, 43, 67, 467, 923, 1473, 0},
     {1, 1, 1, 7, 1, 33, 77, 111, 125, 771, 1975, 0},
     {1, 3, 7, 13, 1, 51, 113, 139, 245, 573, 503, 0},
     {1, 3, 1, 9, 21, 49, 15, 157, 49, 483, 291, 0},
     {1, 1, 1, 1, 29, 35, 17, 65, 403, 485, 1603, 0},
     {1, 1, 1, 7, 19, 1, 37, 129, 203, 321, 1809, 0},
     {1, 3, 7, 15, 15, 9, 5, 77, 29, 485, 581, 0},
     {1, 1, 3, 5, 15, 49, 97, 105, 309, 875, 1581, 0},
     {1, 3, 5, 1, 5, 19, 63, 35, 165, 399, 1489, 0},
     {1, 3, 5, 3, 23, 5, 79, 137, 115, 599, 1127, 0},
     {1, 1, 7, 5, 3, 61, 27, 177, 257, 91, 841, 0},
     {1, 1, 3, 5, 9, 31, 91, 209, 409, 661, 159, 0},
     {1, 3, 1, 15, 23, 39, 23, 195, 245, 203, 947, 0},
     {1, 1, 3, 1, 15, 59, 67, 95, 155, 461, 147, 0},
     {1, 3, 7, 5, 23, 25, 87, 11, 51, 449, 1631, 0},
     {1, 1, 1, 1, 17, 57, 7, 197, 409, 609, 135, 0},
     {1, 1, 1, 9, 1, 61, 115, 113, 495, 895, 1595, 0},
     {1, 3, 7, 15, 9, 47, 121, 211, 379, 985, 1755, 0},
     {1, 3, 1, 3, 7, 57, 27, 231, 339, 325, 1023, 0},
     {1, 1, 1, 1, 19, 63, 63, 239, 31, 643, 373, 0},
     {1, 3, 1, 11, 19, 9, 7, 171, 21, 691, 215, 0},
     {1, 1, 5, 13, 11, 57, 39, 211, 241, 893, 555, 0},
     {1, 1, 7, 5, 29, 21, 45, 59, 509, 223, 491, 0},
     {1, 1, 7, 9, 15, 61, 97, 75, 127, 779, 839, 0},
     {1, 1, 7, 15, 17, 33, 75, 237, 191, 925, 681, 0},
     {1, 3, 5, 7, 27, 57, 123, 111, 101, 371, 1129, 0},
     {1, 3, 5, 5, 29, 45, 59, 127, 229, 967, 2027, 0},
     {1, 1, 1, 1, 17, 7, 23, 199, 241, 455, 135, 0},
     {1, 1, 7, 15, 27, 29, 105, 171, 337, 503, 1817, 0},
     {1, 1, 3, 7, 21, 35, 61, 71, 405, 647, 2045, 0},
     {1, 1, 1, 1, 1, 15, 65, 167, 501, 79, 737, 0},
     {1, 1, 5, 1, 3, 49, 27, 189, 341, 615, 1287, 0},
     {1, 1, 1, 9, 1, 7, 31, 159, 503, 327, 1613, 0},
     {1, 3, 3, 3, 3, 23, 99, 115, 323, 997, 987, 0},
     {1, 1, 1, 9, 19, 33, 93, 247, 509, 453, 891, 0},
     {1, 1, 3, 1, 13, 19, 35, 153, 161, 633, 445, 0},
     {1, 3, 5, 15, 31, 5, 87, 197, 183, 783, 1823, 0},
     {1, 1, 7, 5, 19, 63, 69, 221, 129, 231, 1195, 0},
     {1, 1, 5, 5, 13, 23, 19, 231, 245, 917, 379, 0},
     {1, 3, 1, 15, 19, 43, 27, 223, 171, 413, 125, 0},
     {1, 1, 1, 9, 1, 59, 21, 15, 509, 207, 589, 0},
     {1, 3, 5, 3, 19, 31, 113, 19, 23, 733, 499, 0},
     {1, 1, 7, 1, 19, 51, 101, 165, 47, 925, 1093, 0},
     {1, 3, 3, 9, 15, 21, 43, 243, 237, 461, 1361, 0},
     {1, 1, 1, 9, 17, 15, 75, 75, 113, 715, 1419, 0},
     {1, 1, 7, 13, 17, 1, 99, 15, 347, 721, 1405, 0},
     {1, 1, 7, 15, 7, 27, 23, 183, 39, 59, 571, 0},
     {1, 3, 5, 9, 7, 43, 35, 165, 463, 567, 859, 0},
     {1, 3, 3, 11, 15, 19, 17, 129, 311, 343, 15, 0},
     {1, 1, 1, 15, 31, 59, 63, 39, 347, 359, 105, 0},
     {1, 1, 1, 15, 5, 43, 87, 241, 109, 61, 685, 0},
     {1, 1, 7, 7, 9, 39, 121, 127, 369, 579, 853, 0},
     {1, 1, 1, 1, 17, 15, 15, 95, 325, 627, 299, 0},
     {1, 1, 3, 13, 31, 53, 85, 111, 289, 811, 1635, 0},
     {1, 3, 7, 1, 19, 29, 75, 185, 153, 573, 653, 0},
     {1, 3, 7, 1, 29, 31, 55, 91, 249, 247, 1015, 0},
     {1, 3, 5, 7, 1, 49, 113, 139, 257, 127, 307, 0},
     {1, 3, 5, 9, 15, 15, 123, 105, 105, 225, 1893, 0},
     {1, 3, 3, 1, 15, 5, 105, 249, 73, 709, 1557, 0},
     {1, 1, 1, 9, 17, 31, 113, 73, 65, 701, 1439, 0},
     {1, 3, 5, 15, 13, 21, 117, 131, 243, 859, 323, 0},
     {1, 1, 1, 9, 19, 15, 69, 149, 89, 681, 515, 0},
     {1, 1, 1, 5, 29, 13, 21, 97, 301, 27, 967, 0},
     {1, 1, 3, 3, 15, 45, 107, 227, 495, 769, 1935, 0},
     {1, 1, 1, 11, 5, 27, 41, 173, 261, 703, 1349, 0},
     {1, 3, 3, 3, 11, 35, 97, 43, 501, 563, 1331, 0},
     {1, 1, 1, 7, 1, 17, 87, 17, 429, 245, 1941, 0},
     {1, 1, 7, 15, 29, 13, 1, 175, 425, 233, 797, 0},
     {1, 1, 3, 11, 21, 57, 49, 49, 163, 685, 701, 0},
     {1, 3, 3, 7, 11, 45, 107, 111, 379, 703, 1403, 0},
     {1, 1, 7, 3, 21, 7, 117, 49, 469, 37, 775, 0},
     {1, 1, 5, 15, 31, 63, 101, 77, 507, 489, 1955, 0},
     {1, 3, 3, 11, 19, 21, 101, 255, 203, 673, 665, 0},
     {1, 3, 3, 15, 17, 47, 125, 187, 271, 899, 2003, 0},
     {1, 1, 7, 7, 1, 35, 13, 235, 5, 337, 905, 0},
     {1, 3, 1, 15, 1, 43, 1, 27, 37, 695, 1429, 0},
     {1, 3, 1, 11, 21, 27, 93, 161, 299, 665, 495, 0},
     {1, 3, 3, 15, 3, 1, 81, 111, 105, 547, 897, 0},
     {1, 3, 5, 1, 3, 53, 97, 253, 401, 827, 1467, 0},
     {1, 1, 1, 5, 19, 59, 105, 125, 271, 351, 719, 0},
     {1, 3, 5, 13, 7, 11, 91, 41, 441, 759, 1827, 0},
     {1, 3, 7, 11, 29, 61, 61, 23, 307, 863, 363, 0},
     {1, 1, 7, 1, 15, 35, 29, 133, 415, 473, 1737, 0},
     {1, 1, 1, 13, 7, 33, 35, 225, 117, 681, 1545, 0},
     {1, 1, 1, 3, 5, 41, 83, 247, 13, 373, 1091, 0},
     {1, 3, 1, 13, 25, 61, 71, 217, 233, 313, 547, 0},
     {1, 3, 1, 7, 3, 29, 3, 49, 93, 465, 15, 0},
     {1, 1, 1, 9, 17, 61, 99, 163, 129, 485, 1087, 0},
     {1, 1, 1, 9, 9, 33, 31, 163, 145, 649, 253, 0},
     {1, 1, 1, 1, 17, 63, 43, 235, 287, 111, 567, 0},
     {1, 3, 5, 13, 29, 7, 11, 69, 153, 127, 449, 0},
     {1, 1, 5, 9, 11, 21, 15, 189, 431, 493, 1219, 0},
     {1, 1, 1, 15, 19, 5, 47, 91, 399, 293, 1743, 0},
     {1, 3, 3, 11, 29, 53, 53, 225, 409, 303, 333, 0},
     {1, 1, 1, 15, 31, 31, 21, 81, 147, 287, 1753, 0},
     {1, 3, 5, 5, 5, 63, 35, 125, 41, 687, 1793, 0},
     {1, 1, 1, 9, 19, 59, 107, 219, 455, 971, 297, 0},
     {1, 1, 3, 5, 3, 51, 121, 31, 245, 105, 1311, 0},
     {1, 3, 1, 5, 5, 57, 75, 107, 161, 431, 1693, 0},
     {1, 3, 1, 3, 19, 53, 27, 31, 191, 565, 1015, 0},
     {1, 3, 5, 13, 9, 41, 35, 249, 287, 49, 123, 0},
     {1, 1, 5, 7, 27, 17, 21, 3, 151, 885, 1165, 0},
     {1, 1, 7, 1, 15, 17, 65, 139, 427, 339, 1171, 0},
     {1, 1, 1, 5, 23, 5, 9, 89, 321, 907, 391, 0},
     {1, 1, 7, 9, 15, 1, 77, 71, 87, 701, 917, 0},
     {1, 1, 7, 1, 17, 37, 115, 127, 469, 779, 1543, 0},
     {1, 3, 7, 3, 5, 61, 15, 37, 301, 951, 1437, 0},
     {1, 1, 1, 13, 9, 51, 127, 145, 229, 55, 1567, 0},
     {1, 3, 7, 15, 19, 47, 53, 153, 295, 47, 1337, 0},
     {1, 3, 3, 5, 11, 31, 29, 133, 327, 287, 507, 0},
     {1, 1, 7, 7, 25, 31, 37, 199, 25, 927, 1317, 0},
     {1, 1, 7, 9, 3, 39, 127, 167, 345, 467, 759, 0},
     {1, 1, 1, 1, 31, 21, 15, 101, 293, 787, 1025, 0},
     {1, 1, 5, 3, 11, 41, 105, 109, 149, 837, 1813, 0},
     {1, 1, 3, 5, 29, 13, 19, 97, 309, 901, 753, 0},
     {1, 1, 7, 1, 19, 17, 31, 39, 173, 361, 1177, 0},
     {1, 3, 3, 3, 3, 41, 81, 7, 341, 491, 43, 0},
     {1, 1, 7, 7, 31, 35, 29, 77, 11, 335, 1275, 0},
     {1, 3, 3, 15, 17, 45, 19, 63, 151, 849, 129, 0},
     {1, 1, 7, 5, 7, 13, 47, 73, 79, 31, 499, 0},
     {1, 3, 1, 11, 1, 41, 59, 151, 247, 115, 1295, 0},
     {1, 1, 1, 9, 31, 37, 73, 23, 295, 483, 179, 0},
     {1, 3, 1, 15, 13, 63, 81, 27, 169, 825, 2037, 0},
     {1, 3, 5, 15, 7, 11, 73, 1, 451, 101, 2039, 0},
     {1, 3, 5, 3, 13, 53, 31, 137, 173, 319, 1521, 0},
     {1, 3, 1, 3, 29, 1, 73, 227, 377, 337, 1189, 0},
     {1, 3, 3, 13, 27, 9, 31, 101, 229, 165, 1983, 0},
     {1, 3, 1, 13, 13, 19, 19, 111, 319, 421, 223, 0},
     {1, 1, 7, 15, 25, 37, 61, 55, 359, 255, 1955, 0},
     {1, 1, 5, 13, 17, 43, 49, 215, 383, 915, 51, 0},
     {1, 1, 3, 1, 3, 7, 13, 119, 155, 585, 967, 0},
     {1, 3, 1, 13, 1, 63, 125, 21, 103, 287, 457, 0},
     {1, 1, 7, 1, 31, 17, 125, 137, 345, 379, 1925, 0},
     {1, 1, 3, 5, 5, 25, 119, 153, 455, 271, 2023, 0},
     {1, 1, 7, 9, 9, 37, 115, 47, 5, 255, 917, 0},
     {1, 3, 5, 3, 31, 21, 75, 203, 489, 593, 1, 0},
     {1, 3, 7, 15, 19, 63, 123, 153, 135, 977, 1875, 0},
     {1, 1, 1, 1, 5, 59, 31, 25, 127, 209, 745, 0},
     {1, 1, 1, 1, 19, 45, 67, 159, 301, 199, 535, 0},
     {1, 1, 7, 1, 31, 17, 19, 225, 369, 125, 421, 0},
     {1, 3, 3, 11, 7, 59, 115, 197, 459, 469, 1055, 0},
     {1, 3, 1, 3, 27, 45, 35, 131, 349, 101, 411, 0},
     {1, 3, 7, 11, 9, 3, 67, 145, 299, 253, 1339, 0},
     {1, 3, 3, 11, 9, 37, 123, 229, 273, 269, 515, 0},
     {1, 3, 7, 15, 11, 25, 75, 5, 367, 217, 951, 0},
     {1, 1, 3, 7, 9, 23, 63, 237, 385, 159, 1273, 0},
     {1, 1, 5, 11, 23, 5, 55, 193, 109, 865, 663, 0},
     {1, 1, 7, 15, 1, 57, 17, 141, 51, 217, 1259, 0},
     {1, 1, 3, 3, 15, 7, 89, 233, 71, 329, 203, 0},
     {1, 3, 7, 11, 11, 1, 19, 155, 89, 437, 573, 0},
     {1, 3, 1, 9, 27, 61, 47, 109, 161, 913, 1681, 0},
     {1, 1, 7, 15, 1, 33, 19, 15, 23, 913, 989, 0},
     {1, 3, 1, 1, 25, 39, 119, 193, 13, 571, 157, 0},
     {1, 1, 7, 13, 9, 55, 59, 147, 361, 935, 515, 0},
     {1, 1, 1, 9, 7, 59, 67, 117, 71, 855, 1493, 0},
     {1, 3, 1, 3, 13, 19, 57, 141, 305, 275, 1079, 0},
     {1, 1, 1, 9, 17, 61, 33, 7, 43, 931, 781, 0},
     {1, 1, 3, 1, 11, 17, 21, 97, 295, 277, 1721, 0},
     {1, 3, 1, 13, 15, 43, 11, 241, 147, 391, 1641, 0},
     {1, 1, 1, 1, 1, 19, 37, 21, 255, 263, 1571, 0},
     {1, 1, 3, 3, 23, 59, 89, 17, 475, 303, 757, 543},
     {1, 3, 3, 9, 11, 55, 35, 159, 139, 203, 1531, 1825},
     {1, 1, 5, 3, 17, 53, 51, 241, 269, 949, 1373, 325},
     {1, 3, 7, 7, 5, 29, 91, 149, 239, 193, 1951, 2675},
     {1, 3, 5, 1, 27, 33, 69, 11, 51, 371, 833, 2685},
     {1, 1, 1, 15, 1, 17, 35, 57, 171, 1007, 449, 367},
     {1, 1, 1, 7, 25, 61, 73, 219, 379, 53, 589, 4065},
     {1, 3, 5, 13, 21, 29, 45, 19, 163, 169, 147, 597},
     {1, 1, 5, 11, 21, 27, 7, 17, 237, 591, 255, 1235},
     {1, 1, 7, 7, 17, 41, 69, 237, 397, 173, 1229, 2341},
     {1, 1, 3, 1, 1, 33, 125, 47, 11, 783, 1323, 2469},
     {1, 3, 1, 11, 3, 39, 35, 133, 153, 55, 1171, 3165},
     {1, 1, 5, 11, 27, 23, 103, 245, 375, 753, 477, 2165},
     {1, 3, 1, 15, 15, 49, 127, 223, 387, 771, 1719, 1465},
     {1, 1, 1, 9, 11, 9, 17, 185, 239, 899, 1273, 3961},
     {1, 1, 3, 13, 11, 51, 73, 81, 389, 647, 1767, 1215},
     {1, 3, 5, 15, 19, 9, 69, 35, 349, 977, 1603, 1435},
     {1, 1, 1, 1, 19, 59, 123, 37, 41, 961, 181, 1275},
     {1, 1, 1, 1, 31, 29, 37, 71, 205, 947, 115, 3017},
     {1, 1, 7, 15, 5, 37, 101, 169, 221, 245, 687, 195},
     {1, 1, 1, 1, 19, 9, 125, 157, 119, 283, 1721, 743},
     {1, 1, 7, 3, 1, 7, 61, 71, 119, 257, 1227, 2893},
     {1, 3, 3, 3, 25, 41, 25, 225, 31, 57, 925, 2139}
     };
}

