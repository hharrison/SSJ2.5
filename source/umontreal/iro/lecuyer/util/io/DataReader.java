

/*
 * Class:        DataReader
 * Description:  Data reader interface
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

import java.io.IOException;
import java.util.Map;


/**
 * Data reader interface.
 * 
 */
public interface DataReader  {



   /**
    * Reads the first field labeled as <TT>label</TT> and returns its <TT>String</TT> value.
    * 
    */
   public String readString (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its <TT>int</TT> value.
    * 
    */
   public int readInt (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its <TT>float</TT> value.
    * 
    */
   public float readFloat (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its <TT>double</TT> value.
    * 
    */
   public double readDouble (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a one-dimensional array of <TT>String</TT>'s.
    * 
    */
   public String[] readStringArray (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a one-dimensional array of <TT>int</TT>'s.
    * 
    */
   public int[] readIntArray (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a one-dimensional array of <TT>float</TT>'s.
    * 
    */
   public float[] readFloatArray (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a one-dimensional array of <TT>double</TT>'s.
    * 
    */
   public double[] readDoubleArray (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a two-dimensional array of <TT>String</TT>'s.
    * 
    */
   public String[][] readStringArray2D (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a two-dimensional array of <TT>int</TT>'s.
    * 
    */
   public int[][] readIntArray2D (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a two-dimensional array of <TT>float</TT>'s.
    * 
    */
   public float[][] readFloatArray2D (String label) throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT> and returns its value as a two-dimensional array of <TT>double</TT>'s.
    * 
    */
   public double[][] readDoubleArray2D (String label) throws IOException;


   /**
    * Reads all remaining fields in the file and returns a hashmap indexed
    * by field labels. Anonymous fields are mapped to <code>"_data01_"</code>, <code>"_data02_"</code>, ...
    * 
    */
   public Map<String, DataField> readAllNextFields() throws IOException;

   
   /**
    * Reads all fields in the file and returns a hashmap indexed
    * by field labels. Anonymous fields are mapped to <code>"_data01_"</code>, <code>"_data02_"</code>, ...
    * 
    */
   public Map<String, DataField> readAllFields() throws IOException;

   
   /**
    * Reads the next available field.
    * 
    * @return a newly created DataField instance or null if not found
    * 
    */
   public DataField readNextField() throws IOException;


   /**
    * Reads the first field labeled as <TT>label</TT>.
    * 
    * @return a newly created DataField instance or null if not found
    * 
    */
   public DataField readField (String label) throws IOException;


   /**
    * Closes the input stream.
    * 
    */
   public void close() throws IOException;


   /**
    * Resets the reader to its initial state, i.e. goes back to the beginning of the data stream, if possible.
    * 
    */
   public void reset() throws IOException;

   
   /**
    * Returns <TT>true</TT> if there remains data to be read.
    * 
    */
   public boolean dataPending() throws IOException;

}
