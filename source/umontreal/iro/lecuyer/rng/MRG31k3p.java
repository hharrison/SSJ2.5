

/*
 * Class:        MRG31k3p
 * Description:  combined multiple recursive generator proposed by L'Ecuyer and Touzin 
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

package umontreal.iro.lecuyer.rng; 


import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.rng.RandomStreamBase;
import umontreal.iro.lecuyer.util.ArithmeticMod;
import java.io.Serializable;


/**
 * Extends the abstract class {@link RandomStreamBase}, thus
 * implementing the {@link RandomStream} interface indirectly. The backbone
 * generator is the combined multiple recursive generator (CMRG) <TT>MRG31k3p</TT>
 * proposed by L'Ecuyer and Touzin,
 * implemented in 32-bit integer  arithmetic.
 * This RNG has a period length of approximately
 *  <SPAN CLASS="MATH">2<SUP>185</SUP></SPAN>. The values of <SPAN CLASS="MATH"><I>V</I></SPAN>, <SPAN CLASS="MATH"><I>W</I></SPAN> and <SPAN CLASS="MATH"><I>Z</I></SPAN> are <SPAN CLASS="MATH">2<SUP>62</SUP></SPAN>,
 * <SPAN CLASS="MATH">2<SUP>72</SUP></SPAN> and <SPAN CLASS="MATH">2<SUP>134</SUP></SPAN> respectively. (See {@link RandomStream} for their
 * definition.) The seed and the state of a stream at any given step
 * are six-dimensional vectors of 32-bit integers.
 * The default initial seed is 
 * <SPAN CLASS="MATH">(12345, 12345, 12345, 12345, 12345, 12345)</SPAN>.
 * The method <TT>nextValue</TT> provides 31 bits of precision.
 * 
 * <P>
 * The difference between the RNG of class {@link MRG32k3a} and this one is
 * that this one has all its coefficients of the form
 * 
 * <SPAN CLASS="MATH"><I>a</I> = &#177;2<SUP>q</SUP>&#177;2<SUP>r</SUP></SPAN>. This permits a faster implementation than for
 * arbitrary coefficients.  On a 32-bit computer, <TT>MRG31k3p</TT> is about twice
 * as fast as <TT>MRG32k3a</TT>.
 * On the other hand, the latter does a little better in the spectral test
 * and has been more extensively tested.
 * 
 */
public class MRG31k3p extends RandomStreamBase  {

   private static final long serialVersionUID = 70510L;
   //La date de modification a l'envers, lire 10/05/2007

   //generator constants :
   private static final int M1 = 2147483647;    //2^31 - 1
   private static final int M2 = 2147462579;    //2^31 - 21069
   private static final int MASK12 = 511;       //2^9 - 1
   private static final int MASK13 = 16777215;  //2^24 - 1
   private static final int MASK2 = 65535;      //2^16 - 1
   private static final int MULT2 = 21069;
   private static final double NORM = 4.656612873077392578125e-10;
   //private static final double NORM = 1.0 / (M1 + 1.0);

   //state variables
   private int x11;
   private int x12;
   private int x13;
   private int x21;
   private int x22;
   private int x23;

   //stream and substream variables
   private int[] stream;
   private int[] substream;
   private static int[] curr_stream = {12345, 12345, 12345,
                                       12345, 12345, 12345};

   //streams constants :
   private static final int[][] A1p0 =
      {{0, 4194304, 129},
       {1, 0, 0},
       {0, 1, 0}};
   private static final int[][] A2p0 =
      {{32768, 0, 32769},
       {1, 0, 0},
       {0, 1, 0}};

   private static final int[][] A1p72 =
      {{1516919229, 758510237, 499121365},
       {1884998244, 1516919229, 335398200},
       {601897748, 1884998244, 358115744}};
   private static final int[][] A2p72 =
      {{1228857673, 1496414766, 954677935},
       {1133297478, 1407477216, 1496414766},
       {2002613992, 1639496704, 1407477216}};

   private static final int[][] A1p134 =
      {{1702500920, 1849582496, 1656874625},
       {828554832, 1702500920, 1512419905},
       {1143731069, 828554832, 102237247}};
   private static final int[][] A2p134 =
      {{796789021, 1464208080, 607337906},
       {1241679051, 1431130166, 1464208080},
       {1401213391, 1178684362, 1431130166}};


   //multiply the first half of v by A with a modulo of m1
   //and the second half by B with a modulo of m2
   private static void multMatVect(int[] v, int[][] A, int m1,
                                   int[][] B, int m2) {
      int[] vv = new int[3];
      for(int i = 0; i < 3; i++)
         vv[i] = v[i];
      ArithmeticMod.matVecModM(A, vv, vv, m1);
      for(int i = 0; i < 3; i++)
         v[i] = vv[i];

      for(int i = 0; i < 3; i++)
         vv[i] = v[i + 3];
      ArithmeticMod.matVecModM(B, vv, vv, m2);
      for(int i = 0; i < 3; i++)
         v[i + 3] = vv[i];

   }



   /**
    * Constructs a new stream, initialized at its beginning.
    *   Its seed is 
    * <SPAN CLASS="MATH"><I>Z</I> = 2<SUP>134</SUP></SPAN> steps away from the previous seed.
    * 
    */
   public MRG31k3p()  {
      name = null;

      prec53 = false;
      anti = false;

      stream = new int[6];
      substream = new int[6];
      for(int i = 0; i < 6; i++)
         stream[i] = curr_stream[i];

      resetStartStream();

      multMatVect(curr_stream, A1p134, M1, A2p134, M2);
   }


   /**
    * Constructs a new stream with the identifier <TT>name</TT>
    *   (used when formatting the stream state).
    * 
    * @param name name of the stream
    * 
    */
   public MRG31k3p (String name)   {
      this();
      this.name = name;
   }

   /**
    * Sets the initial seed for the class <TT>MRG31k3p</TT> to the six
    *   integers of the vector <TT>seed[0..5]</TT>.
    *   This will be the initial state (or seed) of the next created stream.
    *   By default, if this method is not called, the first stream is created with
    *   the seed 
    * <SPAN CLASS="MATH">(12345, 12345, 12345, 12345, 12345, 12345)</SPAN>. If it is called,
    *   the first 3 values of the seed must all be less than 
    * <SPAN CLASS="MATH"><I>m</I><SUB>1</SUB> = 2147483647</SPAN>,
    *   and not all 0; and the last 3 values must all be less than 
    * <SPAN CLASS="MATH"><I>m</I><SUB>2</SUB> = 2147462579</SPAN>, and not all 0.
    * 
    * @param seed array of 6 elements representing the seed
    * 
    * 
    */
   public static void setPackageSeed (int seed[])  {
      if (seed.length < 6)
         throw new IllegalArgumentException ("Seed must contain 6 values");
      if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0)
         throw new IllegalArgumentException ("The first 3 values must not be 0");
      if (seed[5] == 0 && seed[3] == 0 && seed[4] == 0)
         throw new IllegalArgumentException ("The last 3 values must not be 0");
      if (seed[0] >= M1 || seed[1] >= M1 || seed[2] >= M1)
         throw new IllegalArgumentException ("The first 3 values must be less than " + M1);

      if (seed[5] >= M2 || seed[3] >= M2 || seed[4] >= M2)
         throw new IllegalArgumentException ("The last 3 values must be less than " + M2);
      for (int i = 0; i < 6;  ++i)
         curr_stream[i] = seed[i];
   } 


   /**
    * <SPAN  CLASS="textit">Use of this method is strongly discouraged</SPAN>.
    *   Initializes the stream at the beginning of a stream with the
    *   initial seed <TT>seed[0..5]</TT>. This vector must satisfy the same conditions
    *   as in <TT>setPackageSeed</TT>.
    *   This method only affects the specified stream, all the others are not
    *   modified, so the beginning of the streams are no longer spaced <SPAN CLASS="MATH"><I>Z</I></SPAN> values
    *   apart.
    *   For this reason, this method should be used only in very
    *   exceptional situations; proper use of <TT>reset...</TT>
    *   and of the stream constructor is preferable.
    * 
    * @param seed array of 6 integers representing the new seed
    * 
    * 
    */
   public void setSeed (int seed[])  {
      if (seed.length < 6)
         throw new IllegalArgumentException ("Seed must contain 6 values");
      if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0)
         throw new IllegalArgumentException ("The first 3 values must not be 0");
      if (seed[3] == 0 && seed[4] == 0 && seed[5] == 0)
         throw new IllegalArgumentException ("The last 3 values must not be 0");
      if (seed[0] >= M1 || seed[1] >= M1 || seed[2] >= M1)
         throw new IllegalArgumentException ("The first 3 values must be less than " + M1);
      if (seed[3] >= M2 || seed[4] >= M2 || seed[5] >= M2)
         throw new IllegalArgumentException ("The last 3 values must be less than " + M2);
      for (int i = 0; i < 6;  ++i)
         stream[i] = seed[i];
      resetStartStream();
   } 

  

   public void resetStartStream() {
      for(int i = 0; i < 6; i++)
         substream[i] = stream[i];
      resetStartSubstream();
   }

   public void resetStartSubstream() {
      x11 = substream[0];
      x12 = substream[1];
      x13 = substream[2];
      x21 = substream[3];
      x22 = substream[4];
      x23 = substream[5];
   }

   public void resetNextSubstream() {
      multMatVect(substream, A1p72, M1, A2p72, M2);
      resetStartSubstream();
   }


   /**
    * Returns the current state <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> of this stream.
    *   This is a vector of 6 integers represented.
    *   This method is convenient if we want to save the state for
    *   subsequent use.
    *  
    * @return the current state of the generator
    * 
    */
   public int[] getState()  {
      return new int[]{x11, x12, x13, x21, x22, x23};
   } 


   /**
    * Clones the current generator and return its copy.
    *  
    *  @return A deep copy of the current generator
    * 
    */
   public MRG31k3p clone()  {
      MRG31k3p retour = null;
      retour = (MRG31k3p)super.clone();
      retour.substream = new int[6];
      retour.stream = new int[6];
      for (int i = 0; i<6; i++) {
         retour.substream[i] = substream[i];
         retour.stream[i] = stream[i];
      }
      return retour;
   }


   public String toString() {
      if(name == null)
         return "The state of the MRG31k3p is : " +
                x11 + "," + x12 + "," + x13 + "; " +
                x21 + "," + x22 + "," + x23 + ".";
      else
         return "The state of " + name + " is : " +
                x11 + "," + x12 + "," + x13 + "; " +
                x21 + "," + x22 + "," + x23 + ".";
   }

   protected double nextValue()  {
      int y1, y2;

      //first component
      y1 = ((x12 & MASK12) << 22) + (x12 >>> 9)
           + ((x13 & MASK13) << 7) + (x13 >>> 24);
      if(y1 < 0 || y1 >= M1)     //must also check overflow
         y1 -= M1;
      y1 += x13;
      if(y1 < 0 || y1 >= M1)
         y1 -= M1;

      x13 = x12;
      x12 = x11;
      x11 = y1;

      //second component
      y1 = ((x21 & MASK2) << 15) + (MULT2 * (x21 >>> 16));
      if(y1 < 0 || y1 >= M2)
         y1 -= M2;
      y2 = ((x23 & MASK2) << 15) + (MULT2 * (x23 >>> 16));
      if(y2 < 0 || y2 >= M2)
         y2 -= M2;
      y2 += x23;
      if(y2 < 0 || y2 >= M2)
         y2 -= M2;
      y2 += y1;
      if(y2 < 0 || y2 >= M2)
         y2 -= M2;

      x23 = x22;
      x22 = x21;
      x21 = y2;

      //Must never return either 0 or M1+1
      if(x11 <= x21)
         return (x11 - x21 + M1) * NORM;
      else
         return (x11 - x21) * NORM;
   }

}
