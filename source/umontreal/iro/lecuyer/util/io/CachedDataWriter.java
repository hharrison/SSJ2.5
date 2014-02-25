

/*
 * Class:        CachedDataWriter
 * Description:  
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
import java.util.LinkedList;


/**
 * This abstract class implements shared functionality for data writers that store
 * all fields in memory before outputing them with {@link umontreal.iro.lecuyer.util.io.DataWriter#close close}.
 * 
 */
public abstract class CachedDataWriter extends AbstractDataWriter  {

   // don't use a map because ordering is important
   protected LinkedList<DataField> fields;

   protected LinkedList<DataField> getFields() {
      return fields;
   }



   /**
    * Class constructor.
    * 
    */
   public CachedDataWriter()  {
      this.fields = new LinkedList<DataField>();
   }
   


   /**
    * Writes an atomic string field.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String s) throws IOException  {
      fields.add(new DataField(label, s));
   }
   


   /**
    * Writes an atomic 32-bit integer (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int a) throws IOException  {
      fields.add(new DataField(label, a));
   }
   


   /**
    * Writes an atomic 32-bit float (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float a) throws IOException  {
      fields.add(new DataField(label, a));
   }
   


   /**
    * Writes an atomic 64-bit double (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double a) throws IOException  {
      fields.add(new DataField(label, a));
   }
   

   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of strings.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String[] a, int n) throws IOException  {
      fields.add(new DataField(label, a.clone(), n));
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 32-bit integers (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int[] a, int n) throws IOException  {
      fields.add(new DataField(label, a.clone(), n));
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 32-bit floats (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float[] a, int n) throws IOException  {
      fields.add(new DataField(label, a.clone(), n));
   }
   


   /**
    * Writes the first <TT>n</TT> elements of a one-dimensional array
    * of 64-bit doubles (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double[] a, int n) throws IOException  {
      fields.add(new DataField(label, a.clone(), n));
   }
   


   /**
    * Writes a two-dimensional array of strings.
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, String[][] a) throws IOException  {
      fields.add(new DataField(label, a.clone()));
   }
   


   /**
    * Writes a two-dimensional array of 32-bit integers (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, int[][] a) throws IOException  {
      fields.add(new DataField(label, a.clone()));
   }
   


   /**
    * Writes a two-dimensional array of 32-bit floats (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, float[][] a) throws IOException  {
      fields.add(new DataField(label, a.clone()));
   }
   


   /**
    * Writes a two-dimensional array of 64-bit doubles (big endian).
    * Writes an anonymous field if <TT>label</TT> is <TT>null</TT>.
    * 
    */
   public void write (String label, double[][] a) throws IOException  {
      fields.add(new DataField(label, a.clone()));
   }
   

}
