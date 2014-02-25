

/*
 * Class:        BinaryDataWriter
 * Description:  Binary data writer
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       David Munger 
 * @since        August 2009

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

package umontreal.iro.lecuyer.util.io;

import java.io.*;


/**
 * Binary data writer.
 * 
 * <P>
 * Stores a sequence of fields in binary file, which can be either atoms or arrays,
 * each of which having the following format:
 * 
 * <UL>
 * <LI>Field label:
 *       
 * <UL>
 * <LI>Pipe character (<TT>|</TT>)
 * </LI>
 * <LI>Label length (32-bit integer, big endian)
 * </LI>
 * <LI>Label string (array of bytes of the specified length)
 *       
 * </LI>
 * </UL>
 * 
 * <P>
 * </LI>
 * <LI>Field type (byte):
 *       
 * <UL>
 * <LI><TT>i</TT> (32-bit integer)
 * </LI>
 * <LI><TT>f</TT> (32-bit float)
 * </LI>
 * <LI><TT>d</TT> (64-bit double)
 * </LI>
 * <LI><TT>S</TT> (string)
 *       
 * </LI>
 * </UL>
 * 
 * <P>
 * </LI>
 * <LI>Number of dimensions (8-bit integer)
 * </LI>
 * <LI>Dimensions (array of 32-bit integers, big endian)
 * </LI>
 * <LI>Field data (in the specified format, big endian)
 * </LI>
 * </UL>
 * 
 * <P>
 * In the case of an atomic field, the number of dimensions is set to zero.
 * 
 * <P>
 * A string field is stored in the following format:
 * 
 * <UL>
 * <LI>String length (32-bit integer)
 * </LI>
 * <LI>Array of bytes of the specified length
 * </LI>
 * </UL>
 * 
 * <P>
 * Also supports anonymous fields (fields with an empty label).
 * 
 * <P>
 * Arrays up to two dimensions are supported.
 * 
 * <P>
 * Modules for reading data exported with this class are available in Java ({@link BinaryDataReader}), Matlab and Python (numpy).
 * 
 */
public class BinaryDataWriter extends AbstractDataWriter  {
   protected DataOutputStream out;
   
   /* *
    * Utility method to write string data.
    *
    */    
   protected void writeStringData(String s) throws IOException {
      if (s != null) {
         out.writeInt(s.length());
         out.writeBytes(s);
      }
      else {
         out.writeInt(0);
      }
   }
   
   /* *
    * Starts a new field by writing its label.
    *
    * @param label   name of the field (can be {@code null})
    *
    */
   protected void writeLabel(String label) throws IOException {
      out.writeByte(TYPECHAR_LABEL);
      writeStringData(label);
   }


   /**
    * <SPAN  CLASS="textit">Field-type</SPAN> symbol indicating a label (it more accurately a field separator symbol).
    * 
    */
   public final static byte TYPECHAR_LABEL   = '|';


   /**
    * <SPAN  CLASS="textit">Field-type</SPAN> symbol indicating <TT>String</TT> data.
    * 
    */
   public final static byte TYPECHAR_STRING  = 'S';


   /**
    * <SPAN  CLASS="textit">Field-type</SPAN> symbol indicating <TT>int</TT> data.
    * 
    */
   public final static byte TYPECHAR_INTEGER = 'i';


   /**
    * <SPAN  CLASS="textit">Field-type</SPAN> symbol indicating <TT>float</TT> data.
    * 
    */
   public final static byte TYPECHAR_FLOAT   = 'f';


   /**
    * <SPAN  CLASS="textit">Field-type</SPAN> symbol indicating <TT>double</TT> data.
    * 
    */
   public final static byte TYPECHAR_DOUBLE  = 'd';


   /**
    * Data will be output to the file with the specified name.
    * 
    * @param filename name of the file to be created or appended to
    * 
    * @param append an existing file with the specified name will be appended to if <TT>true</TT> or truncated if <TT>false</TT>
    * 
    * 
    */
   public BinaryDataWriter (String filename, boolean append)
         throws IOException  {
      this.out = new DataOutputStream(new FileOutputStream(filename, append));
   }
   

   
   /**
    * Data will be output to the specified file.
    * 
    * @param file file to be created or appended to
    * 
    * @param append an existing file with the specified name will be appended to if <TT>true</TT> or truncated if <TT>false</TT>
    * 
    * 
    */
   public BinaryDataWriter (File file, boolean append) throws IOException  {
      this.out = new DataOutputStream(new FileOutputStream(file, append));
   }
   

   
   /**
    * Truncates any existing file with the specified name.
    * 
    * @param filename name of the file to be created
    * 
    * 
    */
   public BinaryDataWriter (String filename) throws IOException  {
      this.out = new DataOutputStream(new FileOutputStream(filename));
   }
   


   /**
    * Truncates any existing file with the specified name.
    * 
    * @param file file to be created
    * 
    * 
    */
   public BinaryDataWriter (File file) throws IOException  {
      this.out = new DataOutputStream(new FileOutputStream(file));
   }
   


   /**
    * Constructor.
    * 
    * @param outputStream output stream to write to
    * 
    */
   public BinaryDataWriter (OutputStream outputStream) throws IOException  {
      this.out = new DataOutputStream(outputStream);
   }
   

   /**
    * Writes an atomic string field.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String s) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_STRING);
      out.writeByte(0);
      writeStringData(s);
   }
   


   /**
    * Writes an atomic 32-bit integer (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_INTEGER);
      out.writeByte(0);
      out.writeInt(a);
   }
   


   /**
    * Writes an atomic 32-bit float  (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_FLOAT);
      out.writeByte(0);
      out.writeFloat(a);
   }
   


   /**
    * Writes an atomic 64-bit double (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_DOUBLE);
      out.writeByte(0);
      out.writeDouble(a);
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of strings.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String[] a, int n) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_STRING);
      out.writeByte(1);
      out.writeInt(n);
      for (int i = 0; i < n; i++)
         writeStringData(a[i]);
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 32-bit integers (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int[] a, int n) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_INTEGER);
      out.writeByte(1);
      out.writeInt(n);
      for (int i = 0; i < n; i++)
         out.writeInt(a[i]);
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 32-bit floats (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float[] a, int n) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_FLOAT);
      out.writeByte(1);
      out.writeInt(n);
      for (int i = 0; i < n; i++)
         out.writeFloat(a[i]);
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 64-bit doubles (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double[] a, int n) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_DOUBLE);
      out.writeByte(1);
      out.writeInt(n);
      for (int i = 0; i < n; i++)
         out.writeDouble(a[i]);
   }
   


   /**
    * Writes a two-dimensional array of strings.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String[][] a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_STRING);
      out.writeByte(2);
      out.writeInt(a.length);
      out.writeInt(a[0].length);
      for (int i = 0; i < a.length; i++)
         for (int j = 0; j < a[i].length; j++)
            writeStringData(a[i][j]);
   }
   


   /**
    * Writes a two-dimensional array of 32-bit integers (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int[][] a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_INTEGER);
      out.writeByte(2);
      out.writeInt(a.length);
      out.writeInt(a[0].length);
      for (int i = 0; i < a.length; i++)
         for (int j = 0; j < a[i].length; j++)
            out.writeInt(a[i][j]);
   }
   


   /**
    * Writes a two-dimensional array of 32-bit floats (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float[][] a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_FLOAT);
      out.writeByte(2);
      out.writeInt(a.length);
      out.writeInt(a[0].length);
      for (int i = 0; i < a.length; i++)
         for (int j = 0; j < a[i].length; j++)
            out.writeFloat(a[i][j]);
   }
   


   /**
    * Writes a two-dimensional array of 64-bit doubles (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double[][] a) throws IOException  {
      writeLabel(label);
      out.writeByte(TYPECHAR_DOUBLE);
      out.writeByte(2);
      out.writeInt(a.length);
      out.writeInt(a[0].length);
      for (int i = 0; i < a.length; i++)
         for (int j = 0; j < a[i].length; j++)
            out.writeDouble(a[i][j]);
   }
   


   /**
    * Flushes any pending data and closes the file.
    * 
    */
   public void close() throws IOException  {
      out.close();
   }
   

}
