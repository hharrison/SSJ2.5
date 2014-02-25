
/*
 * Class:        BitVector
 * Description:  implements vectors of bits and their operations
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
 * This class implements vectors of bits and the operations needed to use
 * them. The vectors can be of arbitrary length. The operations provided are
 * all the binary operations available to the <TT>int</TT> and <TT>long</TT>
 * primitive types in Java.
 * 
 * <P>
 * All bit operations are present in two forms: a normal form and a <TT>self</TT>
 * form. The normal form returns a newly created object containing the result,
 * while the <TT>self</TT> form puts the result in the calling object (<TT>this</TT>).
 * The return value of the <TT>self</TT> form is the calling object itself. This is
 * done to allow easier manipulation of the results, making it possible to 
 * chain operations.
 * 
 */
public class BitVector implements Serializable, Cloneable  {

   static final long serialVersionUID = -3448233092524725148L;

   private int[] v;       //the bits data
   private int length;    //number of data bits (in bits, not in bytes)

   private final static int all_1 = -1;  //integer with all bits set to 1
   private final static int one_1 = 1;   //integer with only his last bit set to 1
   /*
     Note sur le format interne du vecteur de bits :
     On fait toujours en sorte que les bits redondants (ceux qui apparaissent
     quand length % 32 != 0) soient mis a 0. Ceci permet de faire des 
     operations entre des vecteurs de longeurs differentes en posant que
     les bits manquants sur le plus petit des deux vecteurs ont la valeur 0.
   */
 

   /**
    * Creates a new <TT>BitVector</TT> of length <TT>length</TT> with 
    *   all its bits set to 0.
    * 
    * @param length the length of the <TT>BitVector</TT>
    * 
    * 
    */
   public BitVector (int length)  {
      this.length = length;
      v = new int[(length + 31) / 32];
      for(int i = 0; i < v.length; i++)
         v[i] = 0;
   } 


   /**
    * Creates a new <TT>BitVector</TT> of length <TT>length</TT> using
    *   the data in <TT>vect</TT>. Component <TT>vect[0]</TT> makes the 32 lowest order
    *   bits, 
    *   with <TT>vect[1]</TT> being the 32 next lowest order bits, and so on.
    *   The normal bit order is then used to fill the 32 bits (the first bit 
    *   is the lowest order bit and the last bit is largest order bit). 
    *   Note that the sign bit is used as the largest order bit.
    * 
    * @param vect the bits data
    * 
    *   @param length the length of the vector
    * 
    *   @exception IllegalArgumentException when the length of <TT>vect</TT> is
    *     not compatible with the <TT>length</TT> provided
    * 
    * 
    */
   public BitVector (int[] vect, int length)  {
      if (((length + 31)/ 32) != vect.length)
         throw new IllegalArgumentException
         ("The int[] length must be equal to the (length + 31) / 32");

      this.length = length;
      v = new int[vect.length];
      for(int i = 0; i < vect.length; i++)
         v[i] = vect[i];

      //the extra bits must be set at zero
      v[v.length - 1] &= (all_1 >>> (31 - (length - 1) % 32));
   } 


   /**
    * Creates a new <TT>BitVector</TT> using the data in <TT>vect</TT>.
    *   The length of the <TT>BitVector</TT> is always equals to 32 times the 
    *   length of <TT>vect</TT>.
    * 
    * @param vect the bits data
    * 
    * 
    */
   public BitVector (int[] vect)  {
      this(vect, vect.length * 32);
   } 


   /**
    * Creates a copy of the <TT>BitVector that</TT>.
    * 
    * @param that the <TT>BitVector</TT> to copy
    * 
    */
   public BitVector (BitVector that)  {
      this.length = that.length;
      this.v = new int[that.v.length];
      for(int i = 0; i < that.v.length; i++)
         this.v[i] = that.v[i];
   } 

   /**
    * Creates a copy of the <TT>BitVector</TT>.
    * 
    * @return a deep copy of the <TT>BitVector</TT>
    * 
    */
   public Object clone()  {
      try{
         BitVector c = (BitVector)super.clone();
         c.v = (int[])v.clone();
         return c;
      }catch(CloneNotSupportedException e) {
         IllegalStateException ne = new IllegalStateException();
         ne.initCause(e);
         throw ne;
      }      
   } 


   /**
    * Verifies if two <TT>BitVector</TT>'s have the same length and
    *   the same data.
    * 
    * @param that the other <TT>BitVector</TT> to compare to
    * 
    *   @return if the two <TT>BitVector</TT>'s are identiqual
    * 
    */
   public boolean equals (BitVector that)  {
      if(this.length != that.length)
         return false;
      for(int i = 0; i < this.v.length; i++)
         if(this.v[i] != that.v[i])
            return false;
      return true;
   } 


   /**
    * Returns the length of the <TT>BitVector</TT>.
    * 
    * @return the length of the <TT>BitVector</TT>
    * 
    */
   public int size()  {
      return length;
   } 


   /**
    * Resizes the <TT>BitVector</TT> so that its length is equal
    *   to <TT>size</TT>. If the <TT>BitVector</TT> is enlarged, then the newly 
    *   added bits are given the value 1 if <TT>filling</TT> is set to 
    *   <TT>true</TT> and 0 otherwise.
    * 
    * @param size the new size of the <TT>BitVector</TT>
    * 
    *   @param filling the state of the new bits
    * 
    * 
    */
   public void enlarge (int size, boolean filling)  {
      if(size < 0)
         throw new NegativeArraySizeException
         ("The BitVector must have a non-negative size");
      if(filling && (length % 32) != 0)
         v[v.length - 1] ^= all_1 << (length % 32);
      if((size + 31) / 32 != v.length) {
         int[] new_v = new int[(size + 31) / 32];
         int i;
         for(i = 0; i < new_v.length && i < v.length; i++)
            new_v[i] = v[i];
         for(; i < new_v.length; i++)
            new_v[i] = filling ? all_1 : 0;
         v = new_v;
      }
      length = size;

      //the extra bits must be set at zero
      v[v.length - 1] &= (all_1 >>> (31 - (length - 1) % 32));
   } 


   /**
    * Resizes the <TT>BitVector</TT> so that its length is equal
    *   to <TT>size</TT>. Any new bit added is set to 0.
    * 
    * @param size the new size of the <TT>BitVector</TT>
    * 
    * 
    */
   public void enlarge (int size)  {
      enlarge(size, false);
   } 


   /**
    * Gives the value of the bit in position <TT>pos</TT>. If the
    *   value is 1, returns <TT>true</TT>; otherwise, returns <TT>false</TT>.
    * 
    * @param pos the position of the checked bit
    * 
    *   @return the value of the bit as a boolean
    *   @exception ArrayIndexOutOfBoundsException if <TT>pos</TT> is outside
    *     the range of the <TT>BitVector</TT>
    * 
    * 
    */
   public boolean getBool (int pos)  {
      if(pos < 0 || pos >= length)
         throw new ArrayIndexOutOfBoundsException(pos);
      return (v[pos >>> 5] & (one_1 << (pos & 31))) != 0;
   } 


   /**
    * Sets the value of the bit in position <TT>pos</TT>. If <TT>value</TT>
    *   is equal to <TT>true</TT>, sets it to 1; otherwise, sets it to 0.
    * 
    * @param pos the position of the bit to modify
    * 
    *   @param value the new value of the bit as a boolean
    * 
    *   @exception ArrayIndexOutOfBoundsException if <TT>pos</TT> is outside
    *     the range of the <TT>BitVector</TT>
    * 
    * 
    */
   public void setBool (int pos, boolean value)  {
      if(pos > length || pos < 0)
         throw new ArrayIndexOutOfBoundsException(pos);
      if(value)
         v[pos / 32] |= (one_1 << (pos % 32));
      else
         v[pos / 32] &= (one_1 << (pos % 32)) ^ all_1;
   } 


   /**
    * Returns an <TT>int</TT> containing all the bits in the interval
    *   
    * <SPAN CLASS="MATH">[<TT>pos</TT>&#215;32,<TT>pos</TT>&#215;32 + 31]</SPAN>.
    * 
    * @param pos the selected position
    * 
    *   @return the int at the specified position
    *   @exception ArrayIndexOutOfBoundsException if <TT>pos</TT> is outside
    *     the range of the <TT>BitVector</TT>
    * 
    * 
    */
   public int getInt (int pos)  {
      if(pos >= v.length || pos < 0)
         throw new ArrayIndexOutOfBoundsException(pos);
      return v[pos];
   } 


   /**
    * Returns a string containing all the bits of the <TT>BitVector</TT>,
    *   starting with the highest order bit and finishing with the lowest order bit.
    *   The bits are grouped by groups of 8 bits for ease of reading.
    * 
    * @return all the bits of the <TT>BitVector</TT>
    * 
    */
   public String toString()  {
      StringBuffer sb = new StringBuffer();

      for(int i = length - 1; i > 0; i--)
         sb.append(getBool(i) ? "1" : "0").append(i%8 == 0 ? " " : "");
      sb.append(getBool(0) ? "1" : "0");

      return sb.toString();
   } 


   /**
    * Returns a <TT>BitVector</TT> which is the result of the <TT>not</TT>
    *   operator on the current <TT>BitVector</TT>. The <TT>not</TT> operator is 
    *   equivalent to the <code>~</code> operator in Java and thus swap all bits (bits 
    *   previously set to 0 become 1 and bits previously set to 1 become 0).
    * 
    * @return the effect of the <TT>not</TT> operator
    * 
    */
   public BitVector not()  {
      BitVector bv = new BitVector(length);
      for(int i = 0; i < v.length; i++)
         bv.v[i] = v[i] ^ all_1;

      //the extra bits must be set at zero
      bv.v[v.length - 1] &= (all_1 >>> (31 - (length - 1) % 32));

      return bv;
   }  


   /**
    * Applies the <TT>not</TT> operator on the current <TT>BitVector</TT>
    *   and returns it.
    * 
    * @return the <TT>BitVector</TT> itself
    * 
    */
   public BitVector selfNot()  {
      for(int i = 0; i < v.length; i++)
         v[i] = v[i] ^ all_1;

      //the extra bits must be set at zero
      v[v.length - 1] &= (all_1 >>> (31 - (length - 1) % 32));

      return this;
   } 


   /**
    * Returns a <TT>BitVector</TT> which is the result of the <TT>xor</TT>
    *   operator applied on <TT>this</TT> and <TT>that</TT>. The <TT>xor</TT> operator is
    *   equivalent to the <code>^</code> operator in Java. All bits which were set to 0 in
    *   one of the vector and to 1 in the other vector are set to 1. The others
    *   are set to 0. This is equivalent to the addition in modulo 2 arithmetic.
    * 
    * @param that the second operand to the <TT>xor</TT> operator
    * 
    *   @return the result of the <TT>xor</TT> operation
    * 
    */
   public BitVector xor (BitVector that)  {
      //we always consider that this is longer than that
      if(that.length > this.length)
         return that.xor(this);

      BitVector bv = new BitVector(this.length);

      int max = this.v.length;
      int min = that.v.length;

      for(int i = 0; i < min; i++)
         bv.v[i] = this.v[i] ^ that.v[i];
      for(int i = min; i < max; i++)
         bv.v[i] = this.v[i];

      return bv;
   } 


   /**
    * Applies the <TT>xor</TT> operator on <TT>this</TT> with <TT>that</TT>.
    *   Stores the result in <TT>this</TT> and returns it.
    * 
    * @param that the second operand to the <TT>xor</TT> operator
    * 
    *   @return <TT>this</TT>
    * 
    */
   public BitVector selfXor (BitVector that)  {
      //we assume that this is large enough to contain that
      if(this.length < that.length)
         this.enlarge(that.length);

      int min = that.v.length;

      for(int i = 0; i < min; i++)
         this.v[i] ^= that.v[i];

      return this;
   } 


   /**
    * Returns a <TT>BitVector</TT> which is the result of the <TT>and</TT>
    *   operator with both the <TT>this</TT> and <TT>that</TT> <TT>BitVector</TT>'s. The
    *   <TT>and</TT> operator is equivalent to the <code>&amp;</code> operator in Java. Only
    *   bits which are set to 1 in both <TT>this</TT> and <TT>that</TT> are set to 1
    *   in the result, all the others are set to 0.
    * 
    * @param that the second operand to the <TT>and</TT> operator
    * 
    *   @return the result of the <TT>and</TT> operation
    * 
    */
   public BitVector and (BitVector that)  {
      //we always consider this as longer than that
      if(that.length > this.length)
         return that.and(this);

      BitVector bv = new BitVector(this.length);

      int max = this.v.length;
      int min = that.v.length;

      for(int i = 0; i < min; i++)
         bv.v[i] = this.v[i] & that.v[i];
      if(that.length % 32 != 0)
         bv.v[min - 1] |= this.v[min - 1] & (all_1 << (that.length % 32));
      for(int i = min; i < max; i++)
         bv.v[i] = 0;

      return bv;
   } 


   /**
    * Applies the <TT>and</TT> operator on <TT>this</TT> with <TT>that</TT>.
    *   Stores the result  in <TT>this</TT> and returns it.
    * 
    * @param that the second operand to the <TT>and</TT> operator
    * 
    *   @return <TT>this</TT>
    * 
    */
   public BitVector selfAnd (BitVector that)  {
      //we assume that this is large enough to contain that
      if(this.length < that.length)
         this.enlarge(that.length, true);

      int min = that.v.length;

      for(int i = 0; i < min - 1; i++)
         this.v[i] &= that.v[i];
      this.v[min - 1] &= (that.v[min - 1] | (all_1 << (that.length % 32)));

      return this;
   } 


   /**
    * Returns a <TT>BitVector</TT> which is the result of the <TT>or</TT>
    *   operator with both the <TT>this</TT> and <TT>that</TT> <TT>BitVector</TT>'s. The 
    *   <TT>or</TT> operator is equivalent to the <TT>|</TT> operator in Java. Only
    *   bits which are set to 0 in both <TT>this</TT> and <TT>that</TT> are set to to 0
    *   in the result, all the others are set to 1.
    * 
    * @param that the second operand to the <TT>or</TT> operator
    * 
    *   @return the result of the <TT>or</TT> operation
    * 
    */
   public BitVector or (BitVector that)  {
      //we always consider this is longer than that
      if(that.length > this.length)
         return that.or(this);

      BitVector bv = new BitVector(this.length);

      int max = this.v.length;
      int min = that.v.length;

      for(int i = 0; i < min; i++)
         bv.v[i] = this.v[i] | that.v[i];
      for(int i = min; i < max; i++)
         bv.v[i] = 0;

      return bv;
   } 


   /**
    * Applies the <TT>or</TT> operator on <TT>this</TT> with <TT>that</TT>.
    *   Stores the result  in <TT>this</TT> and returns it.
    * 
    * @param that the second operand to the <TT>or</TT> operator
    * 
    *   @return <TT>this</TT>
    * 
    */
   public BitVector selfOr (BitVector that)  {
      //we assume that this is large enough to contain that
      if(this.length < that.length)
         this.enlarge(that.length);

      int min = that.v.length;

      for(int i = 0; i < min; i++)
         this.v[i] |= that.v[i];

      return this;
   } 


   /**
    * Returns a <TT>BitVector</TT> equal to the original with
    *   all the bits shifted <TT>j</TT> positions to the right if <TT>j</TT> is
    *   positive, and shifted <TT>j</TT> positions  to the left if <TT>j</TT> is negative.
    *   The new bits that appears to the left or to the right are set to 0.
    *   If <TT>j</TT> is positive, this operation is equivalent to the <TT>&#187;&gt;</TT>
    *   operator in Java, otherwise, it is equivalent to the <TT>&#171;</TT> operator.
    * 
    * @param j the size of the shift
    * 
    *   @return the shifted <TT>BitVector</TT>
    * 
    */
   public BitVector shift (int j)  {
      BitVector bv = new BitVector(length);

      if(j == 0)
         return bv;
      else if(j > 0) {
         int a = j / 32;
         int b = j % 32;
         int c = 32 - b;

         int i;
         for(i = 0; i+a < v.length; i++)
            bv.v[i] = v[i+a] >>> b;
         // la retenue
         for(i = 0; i+a+1 < v.length; i++)
            bv.v[i] ^= v[i+a+1] << c;
      } else // if(j < 0)
      {
         j = -j;
         int a = j / 32;
         int b = j % 32;
         int c = 32 - b;

         int i;
         for(i = a; i < v.length; i++)
            bv.v[i] ^= v[i-a] << b;
         // la retenue
         for(i = a+1; i < v.length; i++)
            bv.v[i] ^= v[i-a-1] >>> c;
      }

      return bv;
   } 


   /**
    * Shift all the bits of the current <TT>BitVector</TT> <TT>j</TT>
    *   positions to the right if <TT>j</TT> is positive, and  <TT>j</TT> positions 
    *   to the left if <TT>j</TT> is negative.
    *  The new bits that appears to the left or to the rigth are set to 0.
    *  Returns <TT>this</TT>.
    * 
    * @param j the size of the shift
    * 
    *   @return <TT>this</TT>
    * 
    */
   public BitVector selfShift (int j)  {
      if(j == 0)
         return this;
      else if(j > length || j < -length) {
         for(int i = 0; i < v.length; i++)
            v[i] = 0;
      } else if(j > 0) {
         int a = j / 32;
         int b = j % 32;
         int c = 32 - b;

         int i;
         for(i = 0; i+a+1 < v.length; i++) {
            v[i] = v[i+a] >>> b;
            // la retenue
            v[i] ^= v[i+a+1] << c;
         }
         v[i] = v[i+a] >>> b;
         for(i += 1; i < v.length; i++)
            v[i] = 0;
      } else // if(j < 0)
      {
         j = -j;
         int a = j / 32;
         int b = j % 32;
         int c = 32 - b;

         int i;
         for(i = v.length - 1; i > a; i--) {
            v[i] = v[i-a] << b;
            // la retenue
            v[i] ^= v[i-a-1] >>> c;
         }
         v[i] = v[i-a] << b;
         for(i -= 1; i >= 0; i--)
            v[i] = 0;
      }

      return this;
   } 


   /**
    * Returns the scalar product of two <TT>BitVector</TT>'s modulo 2.
    *   It returns <TT>true</TT> if there is an odd number of bits with a value of 1 
    *   in the result of the <TT>and</TT> operator applied on <TT>this</TT> and
    *   <TT>that</TT>, and returns <TT>false</TT> otherwise.
    * 
    * @param that the other <TT>BitVector</TT> with which to do the scalar product
    * 
    *   @return the scalar product
    * 
    */
   public boolean scalarProduct (BitVector that)  {
      //we must take that is not longer than this
      if(that.v.length > this.v.length)
         return that.scalarProduct(this);

      boolean result = false;
      int prod;

      for(int i = 0; i < that.v.length; i++) {
         prod = this.v[i] & that.v[i];
         while(prod != 0) {
            // a chaque iteration, on enleve le 1 le plus a droite
            prod &= prod - 1;
            result = !result;
         }
      }

      return result;
   } 


}

