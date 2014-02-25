

/*
 * Class:        DataField
 * Description:  Represents a data field
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

import java.lang.reflect.Array;


/**
 * This class represents a data field from a file read by an instance of a class
 *  implementing {@link DataReader}.
 * 
 */
public class DataField  {
   protected String label;
   protected Object data;
   protected int effectiveLength;


   /**
    * Constructor. Creates a field named <TT>label</TT> of value <TT>data</TT>.
    * 
    * @param label name of the field
    * 
    * @param data value of the field
    * 
    * 
    */
   public DataField (String label, Object data)  {
      this(label, data, -1);
   }
   

   
   /**
    * Constructor. Creates a field named <TT>label</TT> of value <TT>data</TT>.
    *  <TT>effectiveLength</TT> is the number of significant elements contained in
    *  <TT>data</TT> if it is an array.
    * 
    * @param label name of the field
    * 
    * @param data value of the field
    * 
    * @param effectiveLength number of significant elements contained in <TT>data</TT>
    * 
    */
   public DataField (String label, Object data, int effectiveLength)  {
      this.label = label;
      this.data = data;
      this.effectiveLength = effectiveLength;
   }
   

   /**
    * Returns the field label (or name).
    * 
    */
   public String getLabel()  {
      return label;
   }
   


   /**
    * Returns the type of the field.
    * 
    */
   public Class getType()  {
      return data.getClass();
   }
   

   
   /**
    * Returns <TT>true</TT> if the field value is atomic data.
    * 
    */
   public boolean isAtomic()  {
      return !isArray();
   }
   

   
   /**
    * Returns <TT>true</TT> if the field contains an array.
    * 
    */
   public boolean isArray()  {
      return data.getClass().isArray();
   }
   


   /**
    * Returns <TT>true</TT> if the field contains a two-dimensional array.
    * 
    */
   public boolean isArray2D()  {
      return isArray() && Array.get(data, 0).getClass().isArray();
   }
   

   
   /**
    * Returns the length of the array contained by the field, or <TT>-1</TT>
    *   if it is not an array.
    * 
    */
   public int getArrayLength()  {
      if (!isArray()) return -1;
      if (effectiveLength < 0) return Array.getLength(data);
      return effectiveLength;
   }
   


   /**
    * Returns <TT>true</TT> if the field value is an atomic <TT>String</TT>.
    * 
    */
   public boolean isString()  {
      return (data instanceof String);
   }
   


   /**
    * Returns <TT>true</TT> if the field value is an atomic <TT>int</TT>.
    * 
    */
   public boolean isInt()  {
      return (data instanceof Integer);
   }
   

   
   /**
    * Returns <TT>true</TT> if the field value is an atomic <TT>float</TT>.
    * 
    */
   public boolean isFloat()  {
      return (data instanceof Float);
   }
   


   /**
    * Returns <TT>true</TT> if the field value is an atomic <TT>double</TT>.
    * 
    */
   public boolean isDouble()  {
      return (data instanceof Double);
   }
   


   /**
    * Returns the value as <TT>String</TT>, or <TT>null</TT> if it is not
    *   of type <TT>String</TT>.
    * See {@link #isString isString}.
    * 
    */
   public String asString()  {
      return (data instanceof String) ? (String)data : null;
   }
   

   
   /**
    * Returns the value as <TT>int</TT> or <TT>0</TT> if it is not of type <TT>int</TT>
    * See {@link #isInt isInt}.
    * 
    */
   public int asInt()  {
      return (data instanceof Integer) ? ((Integer)data).intValue() : 0;
   }
   

   
   /**
    * Returns the value as <TT>float</TT> or <TT>0</TT> if it is not of type <TT>float</TT>
    * See {@link #isFloat isFloat}.
    * 
    */
   public float asFloat()  {
      return (data instanceof Float) ? ((Float)data).floatValue() : 0;
   }
   

   
   /**
    * Returns the value as <TT>double</TT> or <TT>0</TT> if it is not of type <TT>double</TT>
    * See {@link #isDouble isDouble}.
    * 
    */
   public double asDouble()  {
      return (data instanceof Double) ? ((Double)data).doubleValue() : 0;
   }
   


   /**
    * Returns the value as one-dimensional <TT>String</TT> array or <TT>null</TT> if it is not of type <TT>String[]</TT>.
    * 
    */
   public String[] asStringArray()  {
      return (data instanceof String[]) ? (String[])data : null;
   }
   

      
   /**
    * Returns the value as one-dimensional <TT>int</TT> array or <TT>null</TT> if it is not of type <TT>int[]</TT>.
    * 
    */
   public int[] asIntArray()  {
      return (data instanceof int[]) ? (int[])data : null;
   }
   

   
   /**
    * Returns the value as one-dimensional <TT>float</TT> array or <TT>null</TT> if it is not of type <TT>float[]</TT>.
    * 
    */
   public float[] asFloatArray()  {
      return (data instanceof float[]) ? (float[])data : null;
   }
   

   
   /**
    * Returns the value as one-dimensional <TT>double</TT> array or <TT>null</TT> if it is not of type <TT>double[]</TT>.
    * 
    */
   public double[] asDoubleArray()  {
      return (data instanceof double[]) ? (double[])data : null;
   }
   

   
   /**
    * Returns the value as two-dimensional <TT>String</TT> array or <TT>null</TT> if it is not of type <TT>String[][]</TT>.
    * 
    */
   public String[][] asStringArray2D()  {
      return (data instanceof String[][]) ? (String[][])data : null;
   }
   


   /**
    * Returns the value as two-dimensional <TT>int</TT> array or <TT>null</TT> if it is not of type <TT>int[][]</TT>.
    * 
    */
   public int[][] asIntArray2D()  {
      return (data instanceof int[][]) ? (int[][])data : null;
   }
   

   
   /**
    * Returns the value as two-dimensional <TT>float</TT> array or <TT>null</TT> if it is not of type <TT>float[][]</TT>.
    * 
    */
   public float[][] asFloatArray2D()  {
      return (data instanceof float[][]) ? (float[][])data : null;
   }
   


   /**
    * Returns the value as two-dimensional <TT>double</TT> array or <TT>null</TT> if it is not of type <TT>double[][]</TT>.
    * 
    */
   public double[][] asDoubleArray2D()  {
      return (data instanceof double[][]) ? (double[][])data : null;
   }
   

   /**
    * Returns the value of the field as an <TT>Object</TT>.
    * 
    */
   public Object asObject()  {
      return data;
   }
   

}
