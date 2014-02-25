
/*
 * Class:        TableFormat
 * Description:  Provides methods to format arrays into String's
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

package umontreal.iro.lecuyer.util;


/**
 * This class provides methods to format arrays and matrices into
 * {@link String}s in different styles.
 * This  could be useful for printing arrays and subarrays, or for
 * putting them in files for further treatment by other softwares such
 * as <EM>Mathematica</EM>, <EM>Matlab</EM>, etc.
 * 
 */
public class TableFormat {
   private TableFormat() {}


   /**
    * Plain text matrix printing style
    * 
    */
   public static final int PLAIN = 0; 


   /**
    * Mathematica matrix printing style
    * 
    */
   public static final int MATHEMATICA = 1; 


   /**
    * Matlab matrix printing style
    * 
    */
   public static final int MATLAB = 2; 


   /**
    * Formats a {@link String} containing the elements <TT>n1</TT>
    *   to <TT>n2</TT> (inclusive) of table <TT>V</TT>,
    *   <TT>k</TT> elements per line, <TT>p</TT> positions per element.
    *   If  <TT>k</TT> = 1, the array index will also appear on the left
    *   of each element, i.e., each line <TT>i</TT> will have the form <TT>i V[i]</TT>.
    *  
    * @param V array to be formated
    * 
    *    @param n1 index of the first element being formated
    * 
    *    @param n2 index of the last element being formated
    * 
    *    @param k number of elements per line
    * 
    *    @param p number of positions per element
    * 
    *    @return formated string repreenting the elements
    * 
    */
   public static String format (int V[], int n1, int n2, int k, int p) {
      int i;
      StringBuffer sb = new StringBuffer();
      if (k > 1) {
         sb.append ("Elements  " + n1 + "  to  " + n2 +
                     PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
         for (i = n1; i <= n2; i++) {
            sb.append (PrintfFormat.d (p, V[i]));
            if (((i + 1 - n1) % k) == 0)
               sb.append (PrintfFormat.NEWLINE);
         }
         sb.append (PrintfFormat.NEWLINE);
      }
      else {
         sb.append (PrintfFormat.NEWLINE + " Index        Element" +
                    PrintfFormat.NEWLINE);
         for (i = n1; i <= n2; i++)
            sb.append (PrintfFormat.d (6, i) + "   " +
                      PrintfFormat.d (12, V[i]) + PrintfFormat.NEWLINE);
      }
      sb.append (PrintfFormat.NEWLINE);
      return sb.toString();
   }


   /**
    * Similar to the previous method, but for an array of <TT>double</TT>'s.
    *   Gives at least <TT>p1</TT> positions per element,
    *   <TT>p2</TT> digits after the decimal point, and at least <TT>p3</TT>
    *   significant digits.
    *  
    * @param V array to be formated
    * 
    *    @param n1 index of the first element being formated
    * 
    *    @param n2 index of the last element being formated
    * 
    *    @param k number of elements per line
    * 
    *    @param p1 number of positions per element
    * 
    *    @param p2 number of digits after the decimal point
    * 
    *    @param p3 number of significant digits
    * 
    *    @return formated string repreenting the elements
    * 
    */
   public static String format (double V[], int n1, int n2,
                                int k, int p1, int p2, int p3) {
      int i;
      StringBuffer sb = new StringBuffer();
      if (k > 1) {
         sb.append ("Elements  " + n1 + "  to  " + n2  + 
                     PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
         for (i = n1; i <= n2; i++) {
            sb.append (PrintfFormat.format (p1, p2, p3, V[i]));
            if (((i + 1 - n1) % k) == 0)
               sb.append (PrintfFormat.NEWLINE);
         }
         sb.append (PrintfFormat.NEWLINE);

      } else {
         sb.append (PrintfFormat.NEWLINE + " Index            Element" +
                    PrintfFormat.NEWLINE);
         for (i = n1; i <= n2; i++)
            sb.append (PrintfFormat.d (6, i) + "   " +
                       PrintfFormat.format (p1, p2, p3, V[i]) +
                       PrintfFormat.NEWLINE);
      }
      sb.append (PrintfFormat.NEWLINE);
      return sb.toString();
   }


   private static int Style = PLAIN;

   private static char OuvrantMat = ' ';     // Matrix delimitors
   private static char FermantMat = ' ';

   private static char OuvrantVec = ' ';     // Vector delimitors
   private static char FermantVec = ' ';

   private static char SepareVec = ' ';      // Element separators
   private static char SepareElem = ' ';

   private static void fixeDelim (int style) {
      /* Fixe les delimiteurs pour imprimer une matrice selon un format
         approprie */
      Style = style;
      switch (style) {
      case MATHEMATICA:
         OuvrantMat = '{';
         FermantMat = '}';
         OuvrantVec = '{';
         FermantVec = '}';
         SepareVec = ',';
         SepareElem = ',';
         break;
      case MATLAB:
         OuvrantMat = '[';
         FermantMat = ']';
         OuvrantVec = ' ';
         FermantVec = ' ';
         SepareVec = ' ';
         SepareElem = ' ';
         break;
      default:
         OuvrantMat = ' ';
         FermantMat = ' ';
         OuvrantVec = ' ';
         FermantVec = ' ';
         SepareVec = ' ';
         SepareElem = ' ';
         break;
      }
   }

   /**
    * Formats a submatrix of integers.
    * 
    * @param Mat matrix to be formated
    * 
    *    @param i1 index of the first row being formated
    * 
    *    @param i2 index of the last row being formated
    * 
    *    @param j1 index of the first column being formated
    * 
    *    @param j2 index of the last column being formated
    * 
    *    @param w number of positions of the elements
    * 
    *    @param p number of digits after the decimal point of the elements
    * 
    *    @param style formating style of the submatrix, being one of
    *         {@link #MATHEMATICA MATHEMATICA}, {@link #MATLAB MATLAB}, or {@link #PLAIN PLAIN}
    * 
    *    @param Name descriptive name of the submatrix
    * 
    *    @return formated string repreenting the submatrix
    * 
    */
   public static String format (int[][] Mat, int i1, int i2,
                                int j1, int j2, int w, int p,
                                int style, String Name) {
      int i;
      int j;

      fixeDelim (style);
      StringBuffer sb = new StringBuffer();
      if (Name.length() > 0)
         sb.append (Name + " = ");

      sb.append (OuvrantMat + PrintfFormat.NEWLINE);
      for (i = i1; i <= i2; i++) {
         sb.append (OuvrantVec);
         for (j = j1; j <= j2; j++) {
            sb.append (PrintfFormat.d (w, Mat[i][j]));
            if (j < j2)
               sb.append (SepareElem);
         }
         sb.append (FermantVec);
         if (i < i2)
            sb.append (SepareVec + PrintfFormat.NEWLINE);
      }
      sb.append (FermantMat + PrintfFormat.NEWLINE + PrintfFormat.NEWLINE);
      return sb.toString();
   }


   /**
    * Formats the submatrix with lines
    *    <TT>i1</TT> <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;<I>i</I>&nbsp;&lt;=&nbsp;</SPAN> <TT>i2</TT> and columns
    *    <TT>j1</TT> <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;<I>j</I>&nbsp;&lt;=&nbsp;</SPAN> <TT>j2</TT> of the matrix <TT>Mat</TT>, using the
    *    formatting style <TT>style</TT>.
    *    The elements are formated in <TT>w</TT> positions each,
    *    with a precision of <TT>p</TT> digits.
    *    The string <TT>Name</TT> provides an identifier for the submatrix.
    * 
    * <P>
    * To be treated by <TT>Matlab</TT>, this string containing the matrix
    *    must be copied to a file with extension <TT>.m</TT>.
    *    If the file is named <TT>poil.m</TT>, for example, it can be accessed by
    *    calling <TT>poil</TT> in <TT>Matlab</TT>.
    *    For <TT>Mathematica</TT>, if the file is named <TT>poil</TT>,
    *    it will be read using <TT>&#171; poil;</TT>.
    *  
    * @param Mat matrix to be formated
    * 
    *    @param i1 index of the first row being formated
    * 
    *    @param i2 index of the last row being formated
    * 
    *    @param j1 index of the first column being formated
    * 
    *    @param j2 index of the last column being formated
    * 
    *    @param w number of positions of the elements
    * 
    *    @param p number of digits after the decimal point of the elements
    * 
    *    @param style formating style of the submatrix, being one of
    *         {@link #MATHEMATICA MATHEMATICA}, {@link #MATLAB MATLAB}, or {@link #PLAIN PLAIN}
    * 
    *    @param Name descriptive name of the submatrix
    * 
    *    @return formated string repreenting the submatrix
    */
   public static String format (double[][] Mat, int i1, int i2,
                                int j1, int j2, int w, int p,
                                int style, String Name) {
      int k;
      int j;
      int i;
      double x;
      String S;

      fixeDelim (style);
      StringBuffer sb = new StringBuffer();
      if (Name.length() > 0)
         sb.append (Name + " = ");

      double prec = Math.pow (10.0, (double)p);
      sb.append (OuvrantMat + PrintfFormat.NEWLINE);
      for (i = i1; i <= i2; i++) {
         sb.append (OuvrantVec);
         for (j = j1; j <= j2; j++) {
            sb.append (' ');
            switch (style) {
            case MATHEMATICA:
               x = Mat[i][j];
               if (((x != 0.0) && (Math.abs (x) < 0.1)) ||
                   (Math.abs (x) > prec)) {
                  S = PrintfFormat.G (0, p, x);
                  int exppos = S.indexOf ('E');
                  if (exppos != -1)
                     S = S.substring (0, exppos) + "*10^(" +
                          S.substring (exppos+1) + ")";
               }
               else
                  S = PrintfFormat.f (0, p, x);
               S = PrintfFormat.s (w, S);
               break;
            default:
               // MATLAB, Default */
               sb.append (PrintfFormat.G (w, p, Mat[i][j]));
               break;
            }
            if (j < j2)
               sb.append (SepareElem);
         }
         sb.append (FermantVec);
         if (i < i2)
            sb.append (SepareVec + PrintfFormat.NEWLINE);
      }
      sb.append (FermantMat + PrintfFormat.NEWLINE);
      return sb.toString();
   }
}
