

/*
 * Class:        DigitalNetFromFile
 * Description:  read the parameters defining a digital net from a file
                 or from a URL address
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
import java.net.URL;
import java.net.MalformedURLException;
import umontreal.iro.lecuyer.util.PrintfFormat;


/**
 * This class allows us to read the parameters defining a digital net either
 * from a file, or from a URL address on the World Wide Web.
 * The parameters used in building the net are those defined in class
 * {@link umontreal.iro.lecuyer.hups.DigitalNet DigitalNet}.
 * The format of the data files must be the following:
 * (see the format in <TT>guidehups.pdf</TT>)
 * <DIV ALIGN="CENTER">
 * <IMG
 *  BORDER="0" SRC="DigitalNetFromFileimg1.png"
 *  ALT="\begin{figure}\begin{center}
 * \tt
 * \fbox{
 * \begin {tabular}{llll}
 * \multicolumn{4}{...
 * ... $c_{2k}$\ &amp; $\cdots$\ &amp; $c_{rk}$\ \\
 * \end {tabular}
 * }
 * \end{center}\end{figure}">
 * </DIV>
 * 
 * <P>
 * The figure above gives the general format of the data file
 * needed by <TT>DigitalNetFromFile</TT>.
 * The values of the parameters on the left must appear in the file
 * as integers. On the right of each parameter, there is an optional
 *  comment that is disregarded by the reader program. In general, the
 *  Java line comments  <TT>//</TT> are accepted anywhere and will
 * ensure that the rest of the line is dropped by the reader. Blank lines
 * are also disregarded by the reader program. For each dimension, there must
 *  be a <SPAN CLASS="MATH"><I>k</I>&#215;<I>r</I></SPAN> matrix of integers in 
 * <SPAN CLASS="MATH">{0, 1,&#8230;, <I>b</I> - 1}</SPAN> (note that
 * the matrices must appear in transposed form).
 * 
 * <P>
 * The predefined files of parameters are kept in different directories,
 * depending on the criteria used in the searches for the parameters defining
 * the digital net. These files have all been stored at the address
 *  <TT><A NAME="tex2html1"
 *   HREF="http://www.iro.umontreal.ca/~simardr/ssj/data">http://www.iro.umontreal.ca/~simardr/ssj/data</A></TT>.
 *  Each file contains the parameters for a specific digital net.
 * The name of the files gives information about the main parameters of
 * the digital net. For example, the file named <TT>Edel/OOA2/B3S13R9C9St6</TT>
 *  contains the parameters for a digital net proposed by Yves Edel
 * (see <TT><A NAME="tex2html2"
 *   HREF="http://www.mathi.uni-heidelberg.de/~yves/index.html">http://www.mathi.uni-heidelberg.de/~yves/index.html</A></TT>) based
 * on ordered orthogonal arrays; the digital net has base <TT>B = 3</TT>,
 * dimension <TT>S = 13</TT>, the generating matrices have <TT>R = 9</TT> rows
 * and <TT>C = 9</TT> columns, and the strength of the net is <TT>St = 6</TT>.
 * 
 */
public class DigitalNetFromFile extends DigitalNet  {
   private String filename;

   private void readMatrices (StreamTokenizer st,
                              int r, int k, int dim)
      throws IOException, NumberFormatException {
      // Read dim matrices with r rows and k columns.
      // dim is the dimension of the digital net.
      genMat = new int[dim * k][r];
      for (int i = 0; i < dim; i++)
         for (int c = 0; c < k; c++) {
             for (int j = 0; j < r; j++) {
                 st.nextToken ();
                 genMat[i*numCols + c][j]  = (int) st.nval;
             }
             // If we do not use all the rows, drop the unused ones.
             for (int j = r; j < numRows; j++) {
                 st.nextToken ();
             }
         }
   }


   void readData (StreamTokenizer st) throws
                                      IOException, NumberFormatException
   {
      // Read beginning of data file, but do not read the matrices
      st.eolIsSignificant (false);
      st.slashSlashComments (true);
      int i = st.nextToken ();
      if (i != StreamTokenizer.TT_NUMBER)
         throw new NumberFormatException(" readData: cannot read base");
      b = (int) st.nval;
      st.nextToken ();   numCols = (int) st.nval;
      st.nextToken ();   numRows = (int) st.nval;
      st.nextToken ();   numPoints = (int) st.nval;
      st.nextToken ();   dim = (int) st.nval;
      if (dim < 1)
         throw new IllegalArgumentException (" dimension dim <= 0");
   }


   static BufferedReader openURL (String filename)
                                  throws MalformedURLException, IOException {
      try {
         URL url = new URL (filename);
         BufferedReader input = new BufferedReader (
                                    new InputStreamReader (
                                        url.openStream()));
         return input;

      } catch (MalformedURLException e) {
         System.err.println (e + "   Invalid URL address:   " + filename);
         throw e;

      }  catch (IOException e) {
          // This can receive a FileNotFoundException
         System.err.println (e + " in openURL with " + filename);
         throw e;
      }
   }

   static BufferedReader openFile (String filename) throws
            IOException {
      try {
         BufferedReader input;
         File f = new File (filename);

         // If file with relative path name exists, read it
         if (f.exists()) {
            if (f.isDirectory())
               throw new IOException (filename + " is a directory");
            input = new BufferedReader (new FileReader (filename));
         } else {              // else read it from ssj.jar
            String pseudo = "umontreal/iro/lecuyer/hups/data/";
            StringBuffer pathname = new StringBuffer (pseudo);
            for (int ci = 0; ci < filename.length(); ci++) {
               char ch = filename.charAt (ci);
               if (ch == File.separatorChar)
                  pathname.append ('/');
               else
                  pathname.append (ch);
            }
            InputStream dataInput =
                DigitalNetFromFile.class.getClassLoader().getResourceAsStream (
                  pathname.toString());
            if (dataInput == null)
               throw new FileNotFoundException();
            input = new BufferedReader (new InputStreamReader (dataInput));
         }
         return input;

       } catch (FileNotFoundException e) {
         System.err.println (e + " *** cannot find  " + filename);
         throw e;

      } catch (IOException e) {
         // This will never catch FileNotFoundException since there
         // is a catch clause above.
         System.err.println (e + " cannot read from  " + filename);
         throw e;
      }
   }




   /**
    * Constructs a digital net after reading its parameters from file
    *     <TT>filename</TT>. If a file named <TT>filename</TT>
    *    can be found relative to the program's directory, then the parameters
    *    will be read from this file; otherwise, they will be read from the file
    *    named  <TT>filename</TT> in the <TT>ssj.jar</TT> archive.
    *    If <TT>filename</TT> is a URL string, it will be read on
    *    the World Wide Web.
    *    For example, to construct a digital net from the parameters in file
    *    <TT>B3S13R9C9St6</TT> in the current directory,  one must give the string
    *    <TT>"B3S13R9C9St6"</TT> as argument to the constructor.
    *    As an example of a file read from the WWW, one may give
    *    as argument to the constructor the string
    *    <TT>
    *   "http://www.iro.umontreal.ca/&#732;simardr/ssj/data/Edel/OOA3/B3S13R6C6St4"</TT>.
    *    Parameter <TT>w</TT> gives the number of digits of resolution, <TT>r1</TT> is
    *    the number of rows, and <TT>s1</TT> is the dimension.
    *    Restrictions: <TT>s1</TT> must be less than the maximal dimension, and
    *    <TT>r1</TT> less than the maximal number of rows in the data file.
    *    Also <TT>w</TT> <SPAN CLASS="MATH">&nbsp;&gt;=&nbsp;</SPAN> <TT>r1</TT>.
    * 
    * @param filename Name of the file to be read
    * 
    *    @param r1 Number of rows for the generating matrices
    * 
    *    @param w Number of digits of resolution
    * 
    *    @param s1 Number of dimensions
    * 
    * 
    */
   public DigitalNetFromFile (String filename, int r1, int w, int s1)
          throws MalformedURLException, IOException 
   {
      super ();
      BufferedReader input = null;
      StreamTokenizer st = null;
      try {
         if (filename.startsWith("http:") || filename.startsWith("ftp:"))
            input = openURL(filename);
         else
            input = openFile(filename);
         st = new StreamTokenizer (input);
         readData (st);

      } catch (MalformedURLException e) {
         System.err.println ("   Invalid URL address:   " + filename);
         throw e;
      } catch (FileNotFoundException e) {
         System.err.println ("   Cannot find  " + filename);
         throw e;
      } catch (NumberFormatException e) {
         System.err.println ("   Cannot read number from " + filename);
         throw e;
      }  catch (IOException e) {
         System.err.println ("   IOException:   " + filename);
         throw e;
      }

      if (b == 2) {
         System.err.println ("   base = 2, use DigitalNetBase2FromFile");
         throw new IllegalArgumentException
             ("base = 2, use DigitalNetBase2FromFile");
      }
      if ((double)numCols * Math.log ((double)b) > (31.0 * Math.log (2.0)))
         throw new IllegalArgumentException
            ("DigitalNetFromFile:   too many points" + PrintfFormat.NEWLINE);
      if (r1 > numRows)
         throw new IllegalArgumentException
            ("DigitalNetFromFile:   One must have   r1 <= Max num rows" +
                PrintfFormat.NEWLINE);
      if (s1 > dim)
         throw new IllegalArgumentException
            ("DigitalNetFromFile:   One must have   s1 <= Max dimension" +
                 PrintfFormat.NEWLINE);
      if (w < 0) {
         r1 = w = numRows;
         s1 = dim;
      }
      if (w < numRows)
         throw new IllegalArgumentException
            ("DigitalNetFromFile:   One must have   w >= numRows" +
              PrintfFormat.NEWLINE);

      try {
         readMatrices (st, r1, numCols, s1);
      } catch (NumberFormatException e) {
         System.err.println (e + "   cannot read matrices from " + filename);
         throw e;
      }  catch (IOException e) {
         System.err.println (e + "   cannot read matrices from  " + filename);
         throw e;
      }
      input.close();

      this.filename = filename;
      numRows = r1;
      dim = s1;
      outDigits = w;
      int x = b;
      for (int i=1; i<numCols; i++) x *= b;
      if (x != numPoints) {
         System.out.println ("DigitalNetFromFile:   numPoints != b^k");
         throw new IllegalArgumentException (" numPoints != b^k");
      }

      // Compute the normalization factors.
      normFactor = 1.0 / Math.pow ((double) b, (double) outDigits);
      double invb = 1.0 / b;
      factor = new double[outDigits];
      factor[0] = invb;
      for (int j = 1; j < outDigits; j++)
         factor[j] = factor[j-1] * invb;
  }


   /**
    * Same as {@link #DigitalNetFromFile DigitalNetFromFile}<TT>(filename, r, r, s)</TT> where
    *    <TT>s</TT> is the dimension and  <TT>r</TT> is given in data file <TT>filename</TT>.
    * 
    * @param filename Name of the file to be read
    * 
    *    @param s Number of dimensions
    * 
    */
   public DigitalNetFromFile (String filename, int s)
          throws MalformedURLException, IOException 
   {
       this (filename, -1, -1, s);
   }

   DigitalNetFromFile ()
   {
       super ();
   }


   public String toString() {
      StringBuffer sb = new StringBuffer ("File:   " + filename +
         PrintfFormat.NEWLINE);
      sb.append (super.toString());
      return sb.toString();
   }

   /**
    * Writes the parameters and the generating matrices of this digital net
    *     to a string. This is useful to check that the file parameters have been
    *     read correctly.
    * 
    */
   public String toStringDetailed()  {
      StringBuffer sb = new StringBuffer (toString());
      sb.append (PrintfFormat.NEWLINE + "n = " + numPoints  +
                 PrintfFormat.NEWLINE);
      sb.append ("dim = " + dim  + PrintfFormat.NEWLINE);
      for (int i = 0; i < dim; i++) {
         sb.append (PrintfFormat.NEWLINE + " // dim = " + (1 + i) +
                    PrintfFormat.NEWLINE);
         for (int c = 0; c < numCols; c++) {
            for (int r = 0; r < numRows; r++)
                sb.append (genMat[i*numCols + c][r] + " ");
            sb.append (PrintfFormat.NEWLINE);
         }
      }
      return sb.toString ();
   }
 

   static class NetComparator implements Comparator {
      // Used to sort list of nets. Sort first by base, then by dimension,
      // then by the number of rows. Don't forget that base = 4 are in files
      // named B4_2* and that the computations are done in base 2.
      public int compare (Object o1, Object o2) {
         DigitalNetFromFile net1 = (DigitalNetFromFile) o1;
         DigitalNetFromFile net2 = (DigitalNetFromFile) o2;
         if (net1.b < net2.b)
            return -1;
         if (net1.b > net2.b)
            return 1;
         if (net1.filename.indexOf("_") >= 0 &&
             net2.filename.indexOf("_") < 0 )
            return 1;
         if (net2.filename.indexOf("_") >= 0 &&
             net1.filename.indexOf("_") < 0 )
            return -1;
         if (net1.dim < net2.dim)
            return -1;
         if (net1.dim > net2.dim)
            return 1;
         if (net1.numRows < net2.numRows)
            return -1;
         if (net1.numRows > net2.numRows)
            return 1;
         return 0;
      }
   }


   private static List getListDir (String dirname) throws IOException {
      try {
         String pseudo = "umontreal/iro/lecuyer/hups/data/";
         StringBuffer pathname = new StringBuffer (pseudo);
         for (int ci = 0; ci < dirname.length(); ci++) {
            char ch = dirname.charAt (ci);
            if (ch == File.separatorChar)
               pathname.append ('/');
            else
               pathname.append (ch);
         }
         URL url = DigitalNetFromFile.class.getClassLoader().getResource (
                      pathname.toString());
         File dir = new File (url.getPath());
         if (!dir.isDirectory())
            throw new IllegalArgumentException (
               dirname + " is not a directory");
         File[] files = dir.listFiles();
         List alist = new ArrayList (200);
         if (!dirname.endsWith (File.separator))
            dirname += File.separator;
         for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
               continue;
            if (files[i].getName().endsWith ("gz") ||
                files[i].getName().endsWith ("zip"))
               continue;
            DigitalNetFromFile net = new DigitalNetFromFile();
            BufferedReader input = net.openFile(dirname + files[i].getName());
            StreamTokenizer st = new StreamTokenizer (input);
            net.readData (st);
            net.filename = files[i].getName();
            alist.add (net);
         }
         if (alist != null && !files[0].isDirectory())
            Collections.sort (alist, new NetComparator ());
         return alist;

      } catch (NullPointerException e) {
         System.err.println ("getListDir: cannot find directory   " + dirname);
         throw e;

      } catch (NumberFormatException e) {
         System.err.println (e + "***   cannot read number ");
         throw e;

      }  catch (IOException e) {
         System.err.println (e);
         throw e;
      }
   }


   private static String listFiles (String dirname) {
      try {
         String pseudo = "umontreal/iro/lecuyer/hups/data/";
         StringBuffer pathname = new StringBuffer (pseudo);
         for (int ci = 0; ci < dirname.length(); ci++) {
            char ch = dirname.charAt (ci);
            if (ch == File.separatorChar)
               pathname.append ('/');
            else
               pathname.append (ch);
         }
         URL url = DigitalNetFromFile.class.getClassLoader().getResource (
                      pathname.toString());
         File dir = new File (url.getPath());
         File[] list = dir.listFiles();
         List alist = new ArrayList (200);
         final int NPRI = 3;
         StringBuffer sb = new StringBuffer(1000);
         for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
               sb.append (PrintfFormat.s(-2, list[i].getName()));
               sb.append (File.separator + PrintfFormat.NEWLINE);
            } else {
               sb.append (PrintfFormat.s(-25, list[i].getName()));
               if (i % NPRI == 2)
                  sb.append (PrintfFormat.NEWLINE);
            }
         }
         if (list.length % NPRI > 0)
            sb.append (PrintfFormat.NEWLINE);
         return sb.toString();

      } catch (NullPointerException e) {
         System.err.println ("listFiles: cannot find directory   " + dirname);
         throw e;
      }
   }

   /**
    * Lists all files (or directories) in directory <TT>dirname</TT>. Only relative
    *   pathnames should be used. The files are  parameter files used in defining
    *   digital nets.  For example, calling <TT>listDir("")</TT> will give the list
    *   of the main data directory in SSJ, while calling <TT>listDir("Edel/OOA2")</TT>
    *   will give the list of all files in directory <TT>Edel/OOA2</TT>.
    * 
    */
   public static String listDir (String dirname) throws IOException  {
      try {
         List list = getListDir (dirname);
         if (list == null || list.size() == 0)
            return listFiles (dirname);
         StringBuffer sb = new StringBuffer(1000);

         sb.append ("Directory:   " + dirname  + PrintfFormat.NEWLINE +
                    PrintfFormat.NEWLINE);
         sb.append (PrintfFormat.s(-25, "     File") +
                    PrintfFormat.s(-15, "       Base") +
                    PrintfFormat.s(-10, "Dimension") +
                    PrintfFormat.s(-10, " numRows") +
                    PrintfFormat.s(-10, "numColumns" +
                    PrintfFormat.NEWLINE));
         int base = 0;
         for (int i = 0; i < list.size(); i++) {
            DigitalNet net = (DigitalNet) list.get(i);
            int j = ((DigitalNetFromFile)net).filename.lastIndexOf
                (File.separator);
            if (net.b != base) {
               sb.append (
      "----------------------------------------------------------------------"
            + PrintfFormat.NEWLINE);
            base = net.b;
            }
            String name = ((DigitalNetFromFile)net).filename.substring(j+1);
            sb.append (PrintfFormat.s(-25, name) +
                       PrintfFormat.d(10, net.b) +
                       PrintfFormat.d(10, net.dim) +
                       PrintfFormat.d(10, net.numRows) +
                       PrintfFormat.d(10, net.numCols) +
                       PrintfFormat.NEWLINE);
         }
         return sb.toString();

      } catch (NullPointerException e) {
         System.err.println (
            "formatPlain: cannot find directory   " + dirname);
         throw e;
      }
   }


   /**
    * Creates a list of all data files in directory <TT>dirname</TT> and writes
    * that list in format HTML in output file <TT>filename</TT>.
    * Each data file contains the parameters required to build a digital net.
    * The resulting list contains a line for each data file giving the
    * name of the file, the base, the dimension, the number of rows and
    * the number of columns of the corresponding digital net.
    * 
    */
   public static void listDirHTML (String dirname, String filename)
          throws IOException  {
      String list = listDir(dirname);
      StreamTokenizer st = new StreamTokenizer (new StringReader(list));
      st.eolIsSignificant(true);
      st.ordinaryChar('/');
      st.ordinaryChar('_');
      st.ordinaryChar('-');
      st.wordChars('-', '-');
      st.wordChars('_', '_');
      st.slashSlashComments(false);
      st.slashStarComments(false);
      PrintWriter out = new PrintWriter (
                            new BufferedWriter (
                               new FileWriter (filename)));
      out.println ("<html>" + PrintfFormat.NEWLINE +
          "<head>" + PrintfFormat.NEWLINE + "<title>");
      while (st.nextToken () != st.TT_EOL)
         ;
      out.println ( PrintfFormat.NEWLINE + "</title>" +
           PrintfFormat.NEWLINE + "</head>");
//      out.println ("<body background bgcolor=#e1eae8 vlink=#c00000>");
      out.println ("<body>");
      out.println ("<table border>");
      out.println ("<caption> Directory: " + dirname + "</caption>");
      st.nextToken(); st.nextToken();
      while (st.sval.compareTo ("File") != 0)
         st.nextToken();
      out.print ("<tr align=center><th>" + st.sval + "</th>");
      while (st.nextToken () != st.TT_EOL) {
         out.print ("<th>" + st.sval + "</th>" );
      }
      out.println ("</tr>" + PrintfFormat.NEWLINE);
      while (st.nextToken () != st.TT_EOF) {
          switch(st.ttype) {
          case StreamTokenizer.TT_EOL:
             out.println ("</tr>");
             break;
          case StreamTokenizer.TT_NUMBER:
             out.print ("<td>" + (int) st.nval + "</td>" );
             break;
          case StreamTokenizer.TT_WORD:
             if (st.sval.indexOf ("---") >= 0) {
                st.nextToken ();
                continue;
             }
             out.print ("<tr align=center><td>" + st.sval + "</td>");
             break;
          default:
             out.print (st.sval);
             break;
        }
      }

      out.println ("</table>");
      out.println ("</body>" + PrintfFormat.NEWLINE + "</html>");
      out.close();
}


}

