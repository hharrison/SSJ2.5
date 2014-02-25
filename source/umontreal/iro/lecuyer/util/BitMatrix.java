

/*
 * Class:        BitMatrix
 * Description:  implements matrices of bits and their operations
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

import java.io.Serializable;



/**
 * This class implements matrices of bits of arbitrary dimensions. Basic
 * facilities for bits operations, multiplications and exponentiations are
 * provided.
 * 
 */
public class BitMatrix implements Serializable, Cloneable  {

   static final long serialVersionUID = 2472769969919959608L;

   private BitVector[] rows;        //rows vectors

   private int r, c;                //number of rows / columns

 

   /**
    * Creates a new <TT>BitMatrix</TT> with <TT>r</TT> rows and
    *   <TT>c</TT> columns filled with 0's.
    * 
    * @param r the number of rows
    * 
    *   @param c the number of columns
    * 
    * 
    */
   public BitMatrix (int r, int c)  {
      rows = new BitVector[r];
      for(int i = 0; i < r; i++)
         rows[i] = new BitVector(c);
      this.r = r;
      this.c = c;
   } 


   /**
    * Creates a new <TT>BitMatrix</TT> using the data in <TT>rows</TT>.
    *   Each of the {@link BitVector} will be one of the rows of the
    *   <TT>BitMatrix</TT>.
    * 
    * @param rows the rows of the new <TT>BitMatrix</TT>
    * 
    * 
    */
   public BitMatrix (BitVector[] rows)  {
      this.rows = new BitVector[rows.length];
      for(int i = 0; i < rows.length; i++)
         this.rows[i] = new BitVector(rows[i]);
      this.r = rows.length;
      this.c = r > 0 ? rows[0].size() : 0;
   } 


   /**
    * Creates a new <TT>BitMatrix</TT> with <TT>r</TT> rows and <TT>c</TT>
    *   columns using the data in <TT>data</TT>. Note that the orders of the
    *   bits for the rows are using the same order than for the {@link BitVector}.
    *   This does mean that the first bit is the lowest order bit of the last
    *   <TT>int</TT> in the row and the last bit is the highest order bit of the
    *   first <TT>int</TT> int the row.
    * 
    * @param data the data of the new <TT>BitMatrix</TT>
    * 
    *   @param r the number of rows
    * 
    *   @param c the number of columns
    * 
    * 
    */
   public BitMatrix (int[][] data, int r, int c)  {
      this.rows = new BitVector[r];
      for(int i = 0; i < r; i++)
         this.rows[i] = new BitVector(data[i],c);
      this.r = r;
      this.c = c;
   } 


   /**
    * Copy constructor.
    * 
    * @param that the <TT>BitMatrix</TT> to copy
    * 
    */
   public BitMatrix (BitMatrix that)  {
      this.r = that.r;
      this.c = that.c;
      this.rows = new BitVector[this.r];
      for(int i = 0; i < this.r; i++)
         this.rows[i] = new BitVector(that.rows[i]);
   } 

   /**
    * Creates a copy of the <TT>BitMatrix</TT>.
    * 
    * @return a deep copy of the <TT>BitMatrix</TT>
    * 
    */
   public Object clone()  {
      try{
         BitMatrix c = (BitMatrix)super.clone();
         c.rows = (BitVector[])rows.clone();
         for(int i = 0; i < rows.length; i++)
            c.rows[i] = (BitVector)rows[i].clone();
         return c;
      } catch(CloneNotSupportedException e) {
         IllegalStateException ne = new IllegalStateException();
         ne.initCause(e);
         throw ne;
      }
   } 


   /**
    * Verifies that <TT>this</TT> and  <TT>that</TT> are strictly
    *  identical. They must both have the same dimensions and data.
    * 
    * @param that the <TT>BitMatrix</TT> to compare
    * 
    *   @return if the two <TT>BitMatrix</TT> are identical
    * 
    */
   public boolean equals (BitMatrix that)  {
      if(this.r != that.r || this.c != that.c)
         return false;
      for(int i = 0; i < r; i++)
         if(!this.rows[i].equals(that.rows[i]))
            return false;
      return true;
   } 


   /**
    * Creates a {@link String} containing all the data of
    *   the <TT>BitMatrix</TT>. The result is displayed in a matrix form, with
    *   each row being put on a different line. Note that the bit at (0,0) is
    *   at the upper left of the matrix, while the bit at (0) in a
    *   {@link BitVector} is the least significant bit.
    * 
    * @return the content of the <TT>BitMatrix</TT>
    * 
    */
   public String toString()  {
      StringBuffer sb = new StringBuffer();

      // on affiche les bits sur les lignes dans l'order inverse de l'ordre
      // affiche pour le BitVector pour que le bit a (0,0) soit en haut
      // a gauche

      sb.append("{ ");
      for(int i = 0; i < rows.length - 1; i++) {
         for(int j = 0; j < rows[i].size(); j++)
            sb.append(rows[i].getBool(j) ? "1" : "0");
         sb.append(PrintfFormat.NEWLINE + "  ");
      }
      if(r > 0) {
         for(int j = 0; j < c; j++)
            sb.append(rows[r-1].getBool(j) ? "1" : "0");
         sb.append(" }");
      } else
         sb.append(" }");

      return sb.toString();
   } 


   /**
    * Creates a {@link String} containing all the data of
    *   the <TT>BitMatrix</TT>. The data is displayed in the same format as are the
    *   <TT>int[][]</TT> in <TT>Java</TT> code. This allows the user to print the
    *   representation of a <TT>BitMatrix</TT> to be put, directly in the source
    *   code, in the constructor <TT>BitMatrix(int[][], int, int)</TT>. The output
    *   is not designed to be human-readable.
    * 
    * @return the content of the <TT>BitMatrix</TT>
    * 
    */
   public String printData()  {
      StringBuffer sb = new StringBuffer();

      sb.append("{ ");
      for(int i = 0; i < r; i++)
          {
              sb.append("{");
              for(int j = 0; j < (c + 31) / 32; j++)
                  {
                      sb.append(rows[i].getInt(j));
                      if(j != (c - 1) / 32)
                          sb.append(", ");
                  }
              sb.append("}");
              if(i != r - 1)
                  sb.append("," + PrintfFormat.NEWLINE + "  ");
          }
      sb.append(" }");

      return sb.toString();
   } 


   /**
    * Returns the number of rows of the <TT>BitMatrix</TT>.
    * 
    * @return the number of rows
    * 
    */
   public int numRows()  {
      return r;
   } 


   /**
    * Returns the number of columns of the <TT>BitMatrix</TT>.
    * 
    * @return the number of columns
    * 
    */
   public int numColumns()  {
      return c;
   } 


   /**
    * Returns the value of the bit in the specified row and column.
    *   If the value is 1, return <TT>true</TT>. If it is 0, return <TT>false</TT>.
    * 
    * @param row the row of the selected bit
    * 
    *   @param column the column of the selected bit
    * 
    *   @return the value of the bit as a boolean
    *   @exception IndexOutOfBoundsException if the selected bit would
    *     be outside the <TT>BitMatrix</TT>
    * 
    * 
    */
   public boolean getBool (int row, int column)  {
      if(row >= r || column >= c)
         throw new IndexOutOfBoundsException();

      return rows[row].getBool(column);
   } 


   /**
    * Changes the value of the bit in the specified row and column.
    *   If <TT>value</TT> is <TT>true</TT>, changes it to 1. If <TT>value</TT> is
    *   <TT>false</TT> changes it to 0.
    * 
    * @param row the row of the selected bit
    * 
    *   @param column the column of the selected bit
    * 
    *   @param value the new value of the bit as a boolean
    * 
    *   @exception IndexOutOfBoundsException if the selected bit would
    *     be outside the <TT>BitMatrix</TT>
    * 
    * 
    */
   public void setBool (int row, int column, boolean value)  {
      if(row >= r || column >= c)
         throw new IndexOutOfBoundsException();

      rows[row].setBool(column,value);
   } 


   /**
    * Returns the transposed matrix. The rows and columns are
    *   interchanged.
    * 
    * @return the transposed matrix
    * 
    */
   public BitMatrix transpose()  {
      BitMatrix result = new BitMatrix(c,r);

      for(int i = 0; i < r; i++)
         for(int j = 0; j < c; j++)
            result.rows[j].setBool(i, rows[i].getBool(j));

      return result;
   } 


   /**
    * Returns the <TT>BitMatrix</TT> resulting from the application of
    *   the <TT>not</TT> operator on the original <TT>BitMatrix</TT>. The effect is to
    *   swap all the bits of the <TT>BitMatrix</TT>, turning all 0 into 1 and all 1
    *   into 0.
    * 
    * @return the result of the <TT>not</TT> operator
    * 
    */
   public BitMatrix not()  {
      BitMatrix result = new BitMatrix(this);
      for(int i = 0; i < r; i++)
         result.rows[i].selfNot();
      return result;
   } 


   /**
    * Returns the <TT>BitMatrix</TT> resulting from the application of
    *   the <TT>and</TT> operator on the original <TT>BitMatrix</TT> and <TT>that</TT>.
    *   Only bits which were at 1 in both <TT>BitMatrix</TT> are set at 1 in the
    *   result. All others are set to 0.
    * 
    * @param that the second operand of the <TT>and</TT> operator
    * 
    *   @return the result of the <TT>and</TT> operator
    *   @exception IncompatibleDimensionException if the two <TT>BitMatrix</TT> are
    *     not of the same dimensions
    * 
    * 
    */
   public BitMatrix and (BitMatrix that)  {
      if(this.c != that.c || this.r != that.r)
         throw new IncompatibleDimensionException
         ("Both matrices must have the same dimension. " +
          "this is a " + this.r + "x" + this.c + " matrix " +
          "while that is a " + that.r + "x" + that.c + " matrix.");
      BitMatrix result = new BitMatrix(this);
      for(int i = 0; i < r; i++)
         result.rows[i].selfAnd(that.rows[i]);
      return result;
   } 


   /**
    * Returns the <TT>BitMatrix</TT> resulting from the application of
    *   the <TT>or</TT> operator on the original <TT>BitMatrix</TT> and <TT>that</TT>.
    *   Only bits which were at 0 in both <TT>BitMatrix</TT> are set at 0 in the
    *   result. All others are set to 1.
    * 
    * @param that the second operand of the <TT>or</TT> operator
    * 
    *   @return the result of the <TT>or</TT> operator
    *   @exception IncompatibleDimensionException if the two <TT>BitMatrix</TT> are
    *     not of the same dimensions
    * 
    * 
    */
   public BitMatrix or (BitMatrix that)  {
      if(this.c != that.c || this.r != that.r)
         throw new IncompatibleDimensionException
         ("Both matrices must have the same dimension. " +
          "this is a " + this.r + "x" + this.c + " matrix " +
          "while that is a " + that.r + "x" + that.c + " matrix.");
      BitMatrix result = new BitMatrix(this);
      for(int i = 0; i < r; i++)
         result.rows[i].selfOr(that.rows[i]);
      return result;
   } 


   /**
    * Returns the <TT>BitMatrix</TT> resulting from the application of
    *   the <TT>xor</TT> operator on the original <TT>BitMatrix</TT> and <TT>that</TT>.
    *   Only bits which were at 1 in only one of the two <TT>BitMatrix</TT> are set
    *   at 1 in the result. All others are set to 0.
    * 
    * @param that the second operand of the <TT>xor</TT> operator
    * 
    *   @return the result of the <TT>xor</TT> operator
    *   @exception IncompatibleDimensionException if the two <TT>BitMatrix</TT> are
    *     not of the same dimensions
    * 
    * 
    */
   public BitMatrix xor (BitMatrix that)  {
      if(this.c != that.c || this.r != that.r)
         throw new IncompatibleDimensionException
         ("Both matrices must have the same dimension. " +
          "this is a " + this.r + "x" + this.c + " matrix " +
          "while that is a " + that.r + "x" + that.c + " matrix.");
      BitMatrix result = new BitMatrix(this);
      for(int i = 0; i < r; i++)
         result.rows[i].selfXor(that.rows[i]);
      return result;
   } 


   /**
    * Multiplies the column {@link BitVector} by a <TT>BitMatrix</TT>
    *   and returns the result. The result is 
    * <SPAN CLASS="MATH"><I>A</I>&#215;<I>v</I></SPAN>, where <SPAN CLASS="MATH"><I>A</I></SPAN> is the
    *   <TT>BitMatrix</TT>, and <SPAN CLASS="MATH"><I>v</I></SPAN> is the {@link BitVector}.
    * 
    * @param vect the vector to multiply
    * 
    *   @return the result of the multiplication
    * 
    */
   public BitVector multiply (BitVector vect)  {
      BitVector res = new BitVector(r);

      for(int i = 0; i < r; i++)
         res.setBool(i, rows[i].scalarProduct(vect));

      return res;
   } 


   /**
    * Multiplies <TT>vect</TT>, seen as a column {@link BitVector}, by
    *   a <TT>BitMatrix</TT>. (See {@link BitVector} to see the conversion between
    *   <TT>int</TT> and {@link BitVector}.) The result is 
    * <SPAN CLASS="MATH"><I>A</I>&#215;<I>v</I></SPAN>,
    *   where <SPAN CLASS="MATH"><I>A</I></SPAN> is the <TT>BitMatrix</TT>, and <SPAN CLASS="MATH"><I>v</I></SPAN> is the {@link BitVector}.
    * 
    * @param vect the vector to multiply
    * 
    *   @return the result of the multiplication, as an <TT>int</TT>
    * 
    */
   public int multiply (int vect)  {
      BitVector temp = new BitVector(new int[]{vect});

      return multiply(temp).getInt(0);
   } 


   /**
    * Multiplies two <TT>BitMatrix</TT>'s together. The result is
    *   
    * <SPAN CLASS="MATH"><I>A</I>&#215;<I>B</I></SPAN>, where <SPAN CLASS="MATH"><I>A</I></SPAN> is the <TT>this BitMatrix</TT> and <SPAN CLASS="MATH"><I>B</I></SPAN> is the
    *   <TT>that BitMatrix</TT>.
    * 
    * @param that the other <TT>BitMatrix</TT> to multiply
    * 
    *   @return the result of the multiplication
    *   @exception IncompatibleDimensionException if the number of columns
    *     of the first <TT>BitMatrix</TT> is not equal to the number of rows of
    *     the second <TT>BitMatrix</TT>
    * 
    * 
    */
   public BitMatrix multiply (BitMatrix that)  {
      if(this.c != that.r)
         throw new IncompatibleDimensionException
         ("The number of columns of this (" + this.c +
          ") must be equal to the number of rows of that (" +
          that.r + ").");

      /*
      BitVector[] res = new BitVector[this.r];

      for(int i = 0; i < this.r; i++) {
         res[i] = new BitVector(that.c);

         for(int j = 0; j < that.c; j++) {
            temp = false;
            for(int k = 0; k < this.c; k++)
               if(this.rows[i].getBool(k) &&
                     that.rows[k].getBool(j))
                  temp = !temp;
            res[i].setBool(j,temp);
         }
      }

      return BitMatrix(res);
      */

      // methode plus efficace

      BitMatrix res = new BitMatrix(this.r, that.c);

      for(int i = 0; i < res.r; i++)
          for(int j = 0; j < res.c; j++)
              if(this.rows[i].getBool(j))
                  res.rows[i].selfXor(that.rows[j]);

      return res;
   } 


   /**
    * Raises the <TT>BitMatrix</TT> to the power <TT>p</TT>.
    * 
    * @param p the power up to which to raise the <TT>BitMatrix</TT>
    * 
    *   @return the power of the <TT>BitMatrix</TT>
    *   @exception IncompatibleDimensionException if the <TT>BitMatrix</TT>
    *     is not square
    * 
    *   @exception IllegalArgumentException if <TT>p</TT> is negative
    * 
    * 
    */
   public BitMatrix power (long p)  {
      if(c != r)
         throw new IncompatibleDimensionException
         ("Only square matrices can be raised to a power.");

      if(p < 0)
          throw new IllegalArgumentException
              ("Only non-negative powers are accepted.");

      if(p == 0)
          {
              //the identity matrix
              BitMatrix bm = new BitMatrix(r,r);
              for(int i = 0; i < r; i++)
                  bm.setBool(i,i,true);
              return bm;
          }

      if(p == 1)
         return this;

      if(p % 2 == 0) {
         BitMatrix temp = this.power(p/2);
         return temp.multiply(temp);
      } else
         return this.multiply(this.power(p-1));
   } 


   /**
    * Raises the <TT>BitMatrix</TT> to power 
    * <SPAN CLASS="MATH">2<SUP><TT>e</TT></SUP></SPAN>.
    * 
    * @param e the exponent of the power up to which to raise the <TT>BitMatrix</TT>
    * 
    *   @return the power of the <TT>BitMatrix</TT>
    *   @exception IncompatibleDimensionException if the <TT>BitMatrix</TT> is
    *     not square
    * 
    */
   public BitMatrix power2e (int e)  {
      if(c != r)
          throw new IncompatibleDimensionException
         ("Only square matrices can be raised to a power.");
      BitMatrix result = this;

      for(int i = 0; i < e; i++)
         result = result.multiply(result);

      return result;
   } 

   /**
    * Runtime exception raised when the dimensions of the
    *   <TT>BitMatrix</TT> are not appropriate for the operation.
    * 
    */
   public class IncompatibleDimensionException extends RuntimeException

  
   {
      private IncompatibleDimensionException() {
         super();
      }

      private IncompatibleDimensionException (String message) {
         super(message);
      }
   }

}

