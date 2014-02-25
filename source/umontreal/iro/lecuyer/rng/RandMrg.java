

/*
 * Class:        RandMrg
 * Description:  Old class replaced by MRG32k3a
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

import umontreal.iro.lecuyer.util.PrintfFormat;
import java.io.Serializable;


@Deprecated
/**
 * <SPAN  CLASS="textit">USE</SPAN> {@link MRG32k3a} <SPAN  CLASS="textit">INSTEAD of this class</SPAN>.
 * This class implements the interface {@link RandomStream} directly, with a few
 * additional tools.  It uses the same backbone (or main) generator as
 * {@link MRG32k3a}, but it is an older
 *  implementation that does not extend {@link RandomStreamBase},
 *  and it is about 10% slower.
 * 
 */
public class RandMrg implements CloneableRandomStream, Serializable {

   private static final long serialVersionUID = 70510L;
   //La date de modification a l'envers, lire 10/05/2007

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private constants.

   private static final double m1     = 4294967087.0;
   private static final double m2     = 4294944443.0;
   private static final double a12    =  1403580.0;
   private static final double a13n   =   810728.0;
   private static final double a21    =   527612.0;
   private static final double a23n   =   1370589.0;
   private static final double two17    =  131072.0;
   private static final double two53    =  9007199254740992.0;
   private static final double invtwo24 = 5.9604644775390625e-8;
//   private static final double norm   = 2.328306549295727688e-10;
   private static final double norm   = 1.0 / (m1 + 1.0);

   private static final double InvA1[][] = {   // Inverse of A1p0
      { 184888585.0, 0.0, 1945170933.0 },
      {         1.0, 0.0,          0.0 },
      {         0.0, 1.0,          0.0 }
      };
   private static final double InvA2[][] = {   // Inverse of A2p0
      { 0.0, 360363334.0, 4225571728.0 },
      { 1.0,         0.0,          0.0 },
      { 0.0,         1.0,          0.0 }
      };
   private static final double A1p0[][]  =  {
      {       0.0,       1.0,      0.0 },
      {       0.0,       0.0,      1.0 },
      { -810728.0, 1403580.0,      0.0 }
      };
   private static final double A2p0[][]  =  {
      {        0.0,   1.0,         0.0 },
      {        0.0,   0.0,         1.0 },
      { -1370589.0,   0.0,    527612.0 }
      };
   private static final double A1p76[][] = {
      {      82758667.0, 1871391091.0, 4127413238.0 },
      {    3672831523.0,   69195019.0, 1871391091.0 },
      {    3672091415.0, 3528743235.0,   69195019.0 }
      };
   private static final double A2p76[][] = {
      {    1511326704.0, 3759209742.0, 1610795712.0 },
      {    4292754251.0, 1511326704.0, 3889917532.0 },
      {    3859662829.0, 4292754251.0, 3708466080.0 }
      };
   private static final double A1p127[][] = {
      {    2427906178.0, 3580155704.0,  949770784.0 },
      {     226153695.0, 1230515664.0, 3580155704.0 },
      {    1988835001.0,  986791581.0, 1230515664.0 }
      };
   private static final double A2p127[][] = {
      {    1464411153.0,  277697599.0, 1610723613.0 },
      {      32183930.0, 1464411153.0, 1022607788.0 },
      {    2824425944.0,   32183930.0, 2093834863.0 }
      };


// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private variables (fields) for each stream.

   private static double nextSeed[] = {12345, 12345, 12345, 12345, 12345, 12345};
   // Default seed of the package and seed for the next stream to be created.

   private double Cg[] = new double[6];
   private double Bg[] = new double[6];
   private double Ig[] = new double[6];
   // The arrays \texttt{Cg}, \texttt{Bg}, and \texttt{Ig} contain the current state, 
   // the starting point of the current substream,
   // and the starting point of the stream, respectively.

   private boolean anti;
   // This stream generates antithetic variates if 
   // and only if \texttt{anti = true}.

   private boolean prec53;
   // The precision of the output numbers is ``increased'' (see
   // \texttt{increasedPrecis}) if and only if \texttt{prec53 = true}.

   private String descriptor;
   // Describes the stream (for writing the state, error messages, etc.).

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private methods

    //--------------------------------------------------------------
    /* Compute (a*s + c) MOD m ; m must be < 2^35 */
    /* Works also for s, c < 0.                   */
    private static double multModM 
           (double a, double s, double c, double m) {
        double v;
        int a1;
        v = a * s + c;
        if (v >= two53 || v <= -two53 ) {
                a1 = (int)(a / two17);  a -= a1 * two17;
                v  = a1 * s;
                a1 = (int)(v / m);    v -= a1 * m;
                v  = v * two17 + a * s + c;
        }
        a1 = (int)(v / m);
        if ((v -= a1 * m) < 0.0) return v += m; else return v;
    }


    //-----------------------------------------------------------
    /* Returns v = A*s MOD m.  Assumes that -m < s[i] < m. */
    /* Works even if v = s.                                */
    private static void matVecModM (double A[][], double s[], 
                                    double v[], double m) {
            int i;
            double x[] = new double[3];
            for (i = 0; i < 3;  ++i) {
                x[i] = multModM (A[i][0], s[0], 0.0, m);
                x[i] = multModM (A[i][1], s[1], x[i], m);
                x[i] = multModM (A[i][2], s[2], x[i], m);
            }
            for (i = 0; i < 3;  ++i)  v[i] = x[i];
    }

    //------------------------------------------------------------
    /* Returns C = A*B MOD m */
    /* Note: work even if A = C or B = C or A = B = C.         */
    private static void matMatModM (double A[][], double B[][], 
                                    double C[][], double m){
             int i, j;
             double V[] = new double[3], W[][] = new double[3][3];
             for (i = 0; i < 3;  ++i) {
                    for (j = 0; j < 3;  ++j)  V[j] = B[j][i];
                            matVecModM (A, V, V, m);
                    for (j = 0; j < 3;  ++j)  W[j][i] = V[j];
             }
             for (i = 0; i < 3;  ++i) {
                    for (j = 0; j < 3;  ++j)
                        C[i][j] = W[i][j];
             }
    }

    //-------------------------------------------------------------
    /* Compute matrix B = (A^(2^e) Mod m);  works even if A = B */
    private static void matTwoPowModM (double A[][], double B[][], 
                                       double m, int e) {
            int i, j;
            /* initialize: B = A */
            if (A != B) {
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3;  ++j)  B[i][j] = A[i][j];
                }
            }
            /* Compute B = A^{2^e} */
            for (i = 0; i < e; i++) matMatModM (B, B, B, m);
    }

    //-------------------------------------------------------------
    /* Compute matrix D = A^c Mod m ;  works even if A = B */
    private static void matPowModM (double A[][], double B[][], 
                                    double m, int c){
            int i, j;
            int n = c;
            double W[][] = new double[3][3];

            /* initialize: W = A; B = I */
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3;  ++j)  {
                    W[i][j] = A[i][j];
                    B[i][j] = 0.0;
                }
            }
            for (j = 0; j < 3;  ++j)   B[j][j] = 1.0;

            /* Compute B = A^c mod m using the binary decomp. of c */
            while (n > 0) {
                if ((n % 2)==1) matMatModM (W, B, B, m);
                matMatModM (W, W, W, m);
                n /= 2;
            }
    } 

   //-------------------------------------------------------------
   // Generate a uniform random number, with 32 bits of resolution.
   private double U01() {
        int k;
        double p1, p2, u;
        /* Component 1 */
        p1 = a12 * Cg[1] - a13n * Cg[0];
        k = (int)(p1 / m1);
        p1 -= k * m1;
        if (p1 < 0.0) p1 += m1;
        Cg[0] = Cg[1];   Cg[1] = Cg[2];   Cg[2] = p1;
        /* Component 2 */
        p2 = a21 * Cg[5] - a23n * Cg[3];
        k  = (int)(p2 / m2);
        p2 -= k * m2;
        if (p2 < 0.0) p2 += m2;
        Cg[3] = Cg[4];   Cg[4] = Cg[5];   Cg[5] = p2;
        /* Combination */
        u = ((p1 > p2) ? (p1 - p2) * norm : (p1 - p2 + m1) * norm);
        return (anti) ? (1 - u) : u;
   }

   //-------------------------------------------------------------
   // Generate a uniform random number, with 52 bits of resolution.
   private double U01d() {
        double u = U01();
        if (anti) {
            // Antithetic case: note that U01 already returns 1-u.
            u += (U01() - 1.0) * invtwo24;
            return (u < 0.0) ? u + 1.0 : u;
        } else {
            u += U01() * invtwo24;
            return (u < 1.0) ? u : (u - 1.0);
        }
   }



   /**
    * Constructs a new stream, initializes its seed <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>,
    *    sets <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> and <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> equal to <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>, and sets its antithetic switch 
    *    to <TT>false</TT>.
    *    The seed <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN> is equal to the initial seed of the package given by 
    *    {@link #setPackageSeed(long[]) setPackageSeed} if this is the first stream created,
    *    otherwise it is <SPAN CLASS="MATH"><I>Z</I></SPAN> steps ahead of that of the stream most recently
    *    created in this class.
    * 
    */
   public RandMrg()  {
      anti = false;
      prec53 = false;
      for (int i = 0; i < 6; ++i)  
         Bg[i] = Cg[i] = Ig[i] = nextSeed[i];
      matVecModM (A1p127, nextSeed, nextSeed, m1);
      double temp[] = new double[3];
      for (int i = 0; i < 3; ++i)  
         temp[i] = nextSeed[i + 3];
      matVecModM (A2p127, temp, temp, m2);
      for (int i = 0; i < 3; ++i)  
         nextSeed[i + 3] = temp[i];
   }


   /**
    * Constructs a new stream with an identifier <TT>name</TT>
    *    (can be used when printing the stream state, in error messages, etc.).
    *  
    * @param name name of the stream
    * 
    */
   public RandMrg (String name)  {
      this();
      descriptor = name;
   }


   /**
    * Sets the initial seed for the class <TT>RandMrg</TT> to the 
    *    six integers in the vector <TT>seed[0..5]</TT>.
    *    This will be the seed (initial state) of the first stream.
    *    If this method is not called, the default initial seed
    *    is 
    * <SPAN CLASS="MATH">(12345, 12345, 12345, 12345, 12345, 12345)</SPAN>.
    *    If it is called, the first 3 values of the seed must all be
    *    less than 
    * <SPAN CLASS="MATH"><I>m</I><SUB>1</SUB> = 4294967087</SPAN>, and not all 0;
    *    and the last 3 values 
    *    must all be less than 
    * <SPAN CLASS="MATH"><I>m</I><SUB>2</SUB> = 4294944443</SPAN>, and not all 0.
    *  
    * @param seed array of 6 elements representing the seed
    * 
    * 
    */
   public static void setPackageSeed (long seed[])  {
      // Must use long because there is no unsigned int type.
      if (seed.length != 6)
         throw new IllegalArgumentException ("Seed must contain 6 values");
      if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0)
         throw new IllegalArgumentException ("The first 3 values must not be 0");
      if (seed[5] == 0 && seed[3] == 0 && seed[4] == 0)
         throw new IllegalArgumentException ("The last 3 values must not be 0");
      final long m1 = 4294967087L;
      if (seed[0] >= m1 || seed[1] >= m1 || seed[2] >= m1)
         throw new IllegalArgumentException ("The first 3 values must be less than " + m1);
      final long m2 = 4294944443L;
      if (seed[5] >= m2 || seed[3] >= m2 || seed[4] >= m2)
         throw new IllegalArgumentException ("The last 3 values must be less than " + m2);
      for (int i = 0; i < 6;  ++i)  nextSeed[i] = seed[i];
   }


   public void resetStartStream()  {
      for (int i = 0; i < 6;  ++i)  Cg[i] = Bg[i] = Ig[i];
   } 

   public void resetStartSubstream()  {
      for (int i = 0; i < 6;  ++i)  Cg[i] = Bg[i];
   } 

   public void resetNextSubstream()  {
      int i;
      matVecModM (A1p76, Bg, Bg, m1);
      double temp[] = new double[3];
      for (i = 0; i < 3; ++i) temp[i] = Bg[i + 3];
      matVecModM (A2p76, temp, temp, m2);
      for (i = 0; i < 3; ++i) Bg[i + 3] = temp[i];
      for (i = 0; i < 6;  ++i) Cg[i] = Bg[i];
   } 


   /**
    * After calling this method with <TT>incp = true</TT>, each call to 
    *   the generator (direct or indirect) for this stream 
    *   will return a uniform random number with (roughly) 53 bits of resolution 
    *   instead of 32 bits,
    *   and will advance the state of the stream by 2 steps instead of 1.  
    *   More precisely, if <TT>s</TT> is a stream of the class <TT>RandMrg</TT>,
    *   in the non-antithetic case, the instruction
    *   ``<TT>u = s.nextDouble()</TT>'', when the resolution has been increased,
    *   is equivalent to
    *   ``<TT>u = (s.nextDouble() + s.nextDouble()*fact) % 1.0</TT>'' 
    *   where the constant <TT>fact</TT> is equal to <SPAN CLASS="MATH">2<SUP>-24</SUP></SPAN>.
    *   This also applies when calling <TT>nextDouble</TT> indirectly
    *   (e.g., via <TT>nextInt</TT>, etc.).
    * 
    * <P>
    * By default, or if this method is called again with <TT>incp = false</TT>, 
    *   each call to <TT>nextDouble</TT> for this stream advances the state by 1 step
    *   and returns a number with 32 bits of resolution.
    *  
    * @param incp <TT>true</TT> if increased precision is desired, <TT>false</TT> otherwise
    * 
    * 
    */
   public void increasedPrecis (boolean incp)  {
      prec53 = incp;
   }


   public void setAntithetic (boolean anti)  {
      this.anti = anti;
   }

   /**
    * Advances the state of this stream by <SPAN CLASS="MATH"><I>k</I></SPAN> values,
    *   without modifying the states of other streams (as in <TT>setSeed</TT>),
    *   nor the values of <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> and <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN> associated with this stream.
    *   If <SPAN CLASS="MATH"><I>e</I> &gt; 0</SPAN>, then <SPAN CLASS="MATH"><I>k</I> = 2<SUP>e</SUP> + <I>c</I></SPAN>; 
    *   if <SPAN CLASS="MATH"><I>e</I> &lt; 0</SPAN>,  then 
    * <SPAN CLASS="MATH"><I>k</I> = - 2<SUP>-e</SUP> + <I>c</I></SPAN>; and if <SPAN CLASS="MATH"><I>e</I> = 0</SPAN>,  then <SPAN CLASS="MATH"><I>k</I> = <I>c</I></SPAN>.
    *   Note: <SPAN CLASS="MATH"><I>c</I></SPAN> is allowed to take negative values.
    *   This method should be used only in very 
    *   exceptional cases; proper use of the <TT>reset...</TT> methods 
    *   and of the stream constructor cover most reasonable situations.
    *  
    * @param e an exponent
    * 
    *    @param c a constant
    * 
    * 
    */
   public void advanceState (int e, int c)  {
      double B1[][]= new double[3][3], C1[][]=new double[3][3];
      double B2[][]= new double[3][3], C2[][]=new double[3][3];

      if (e > 0) {
          matTwoPowModM (A1p0, B1, m1, e);
          matTwoPowModM (A2p0, B2, m2, e);
      } else if (e < 0) {
          matTwoPowModM (InvA1, B1, m1, -e);
          matTwoPowModM (InvA2, B2, m2, -e);
      }

      if (c >= 0) {
          matPowModM (A1p0, C1, m1, c);
          matPowModM (A2p0, C2, m2, c);
      } else {
          matPowModM (InvA1, C1, m1, -c);
          matPowModM (InvA2, C2, m2, -c);
      }

      if (e != 0) {
          matMatModM (B1, C1, C1, m1);
          matMatModM (B2, C2, C2, m2);
      }

      matVecModM (C1, Cg, Cg, m1);
      double[] cg3 = new double[3];
      for (int i = 0; i < 3; i++)  cg3[i] = Cg[i+3];
      matVecModM (C2, cg3, cg3, m2);
      for (int i = 0; i < 3; i++)  Cg[i+3] = cg3[i];
   }


   /**
    * Sets the initial seed <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN> of this stream 
    *   to the vector <TT>seed[0..5]</TT>.  This vector must satisfy the same 
    *   conditions as in <TT>setPackageSeed</TT>.
    *   The stream is then reset to this initial seed.
    *   The states and seeds of the other streams are not modified.
    *   As a result, after calling this method, the initial seeds
    *   of the streams are no longer spaced <SPAN CLASS="MATH"><I>Z</I></SPAN> values apart.
    *   For this reason, this method should be used only in very 
    *   exceptional situations; proper use of <TT>reset...</TT> 
    *   and of the stream constructor is preferable.
    *  
    * @param seed array of 6 integers representing the new seed
    * 
    * 
    */
   public void setSeed (long seed[])  {
      // Must use long because there is no unsigned int type.
      if (seed.length != 6)
         throw new IllegalArgumentException ("Seed must contain 6 values");
      if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0)
         throw new IllegalArgumentException ("The first 3 values must not be 0");
      if (seed[3] == 0 && seed[4] == 0 && seed[5] == 0)
         throw new IllegalArgumentException ("The last 3 values must not be 0");
      final long m1 = 4294967087L;
      if (seed[0] >= m1 || seed[1] >= m1 || seed[2] >= m1)
         throw new IllegalArgumentException ("The first 3 values must be less than " + m1);
      final long m2 = 4294944443L;
      if (seed[3] >= m2 || seed[4] >= m2 || seed[5] >= m2)
         throw new IllegalArgumentException ("The last 3 values must be less than " + m2);
      for (int i = 0; i < 6;  ++i)
         Cg[i] = Bg[i] = Ig[i] = seed[i];
   }


   /**
    * Returns the current state <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> of this stream.
    *   This is a vector of 6 integers represented in floating-point format.
    *   This method is convenient if we want to save the state for 
    *   subsequent use.  
    *  
    * @return the current state of the generator
    * 
    */
   public double[] getState()  {
      return Cg;
   }

   public String toString() {
       PrintfFormat str = new PrintfFormat();
       
       str.append ("The current state of the RandMrg");
       if (descriptor != null && descriptor.length() > 0)
          str.append (" " + descriptor);
       str.append (":" + PrintfFormat.NEWLINE + "   Cg = { ");
       for (int i = 0; i < 5; i++)
          str.append ((long) Cg[i] + ", ");
       str.append ((long) Cg[5] + " }" + PrintfFormat.NEWLINE +
              PrintfFormat.NEWLINE);
     
       return str.toString();
   } 

   /**
    * Returns a string containing the name of this stream and the 
    *    values of all its internal variables.
    *  
    * @return the detailed state of the generator, formatted as a string
    * 
    */
   public String toStringFull()  {
       PrintfFormat str = new PrintfFormat();
       str.append ("The RandMrg");
       if (descriptor != null && descriptor.length() > 0)
          str.append (" " + descriptor);
       str.append (":" + PrintfFormat.NEWLINE + "   anti = " +
          (anti ? "true" : "false")).append(PrintfFormat.NEWLINE);

       str.append ("   Ig = { ");
       for (int i = 0; i < 5; i++)
          str.append ((long) Ig[i] + ", ");
       str.append ((long) Ig[5] + " }" + PrintfFormat.NEWLINE);

       str.append ("   Bg = { ");
       for (int i = 0; i < 5; i++)
          str.append ((long) Bg[i] + ", ");
       str.append ((long) Bg[5] + " }" + PrintfFormat.NEWLINE);

       str.append ("   Cg = { ");
       for (int i = 0; i < 5; i++)
          str.append ((long) Cg[i] + ", ");
       str.append ((long) Cg[5] + " }" + PrintfFormat.NEWLINE +
           PrintfFormat.NEWLINE);
 
       return str.toString();
   }


   /**
    * Returns a (pseudo)random number from the uniform distribution
    *    over the interval <SPAN CLASS="MATH">(0, 1)</SPAN>, using this stream,
    *    after advancing its state by one step.  
    *    Normally, the returned number has 32 bits of resolution,
    *    in the sense that it is always a multiple of 
    * <SPAN CLASS="MATH">1/(2<SUP>32</SUP> - 208)</SPAN>.
    *    However, if the precision has been increased by calling 
    *    <TT>increasedPrecis</TT> for this stream, the resolution is higher
    *    and the stream state advances by two steps.
    * 
    * 
    */
   public double nextDouble()  {
      if (prec53) return this.U01d();
      else return this.U01();
   }


   public void nextArrayOfDouble (double[] u, int start, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n must be positive.");
      for (int i = start; i < start+n; i++)
         u[i] = nextDouble();
   }

   public int nextInt (int i, int j)  {
      // This works even for an interval [0, 2^31 - 1].
      // It would not with u*(j - i + 1)
      return (i + (int)(nextDouble() * (j - i + 1.0)));
   }

   public void nextArrayOfInt (int i, int j, int[] u, int start, int n) {
      if (n <= 0)
         throw new IllegalArgumentException ("n must be positive.");
      for (int k = start; k < start+n; k++)
         u[k] = nextInt (i, j);
   }


   /**
    * Clones the current generator and return its copy.
    *  
    *  @return A deep copy of the current generator
    * 
    */
   public RandMrg clone()  {
      RandMrg retour = null;
      try {
         retour = (RandMrg)super.clone();
         retour.Cg = new double[6];
         retour.Bg = new double[6];
         retour.Ig = new double[6];
         for (int i = 0; i<6; i++) {
            retour.Cg[i] = Cg[i];
            retour.Bg[i] = Bg[i];
            retour.Ig[i] = Ig[i];
         }
      }
      catch (CloneNotSupportedException cnse) {
         cnse.printStackTrace(System.err);
      }
      return retour;
   }


}
