
/*
 * Class:        DMatrix
 * Description:  Methods for matrix calculations with double numbers
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
   import cern.colt.matrix.DoubleMatrix2D;
   import cern.colt.matrix.DoubleFactory2D;
   import cern.colt.matrix.impl.DenseDoubleMatrix2D;
   import cern.colt.matrix.linalg.*;
   import cern.jet.math.Functions;


/**
 * This class implements a few methods for matrix calculations
 * with <TT>double</TT> numbers.
 * 
 */
public class DMatrix  {
   private double [][] mat;        // matrix of double's
   private int r, c;               // number of rows, columns

   // [9/9] Padé numerator coefficients for exp;
   private static double[] cPade = {17643225600d, 8821612800d, 2075673600d,
       302702400, 30270240,  2162160, 110880, 3960, 90, 1};

   /* *
    Matrix multiplication $C = AB$. All three matrices are square, banded,
    and upper triangular. $A$ has a non-zero diagonal, \texttt{sa} non-zero
    superdiagonals, and thus a bandwidth of \texttt{sa + 1}. The non-zero
    elements of $A_{ij}$ are those for which $j - s_a \le i \le j$.
    Similarly for $B$ which has a bandwidth of \texttt{sb + 1}.
    The resulting matrix $C$ has \texttt{sa + sb} non-zero superdiagonals,
    and a bandwidth of \texttt{sa + sb + 1}.
   */
   private static void innerMultBand (DoubleMatrix2D A, int sa,
                                      DoubleMatrix2D B, int sb,
                                      DoubleMatrix2D C) {
      final int n = A.rows();
      int kmin, kmax;
      double x, y, z;
      for (int i = 0; i < n; ++i) {
         int jmax = Math.min(i + sa + sb, n - 1);
         for (int j = i; j <= jmax; ++j) {
            kmin = Math.max(i, j - sb);
            kmax = Math.min(i + sa, j);
            z = 0;
            for (int k = kmin; k <= kmax; ++k) {
               x = A.getQuick (i, k);
               y = B.getQuick (k, j);
               z += x * y;
            }
            C.setQuick (i, j, z);
         }
      }
   }


	/* *
	 * Compute the 1-norm for matrix B, which is bidiagonal. The only non-zero
	 * elements are on the diagonal and the first superdiagonal.
	 *
	 * @param B
	 * @return the norm
	 */
	private static double norm1 (DoubleMatrix2D B) {
		final int n = B.rows();
		double x;
		double norm = Math.abs(B.getQuick(0, 0));
		for (int i = 1; i < n; ++i) {
			x = Math.abs(B.getQuick(i, i - 1)) + Math.abs(B.getQuick(i, i));
			if (x > norm)
				norm = x;
		}
		return norm;
	}


   /**
    * Creates a new <TT>DMatrix</TT> with <TT>r</TT> rows and
    *   <TT>c</TT> columns.
    * 
    * @param r the number of rows
    * 
    *   @param c the number of columns
    * 
    * 
    */
   public DMatrix (int r, int c)  {
      mat = new double[r][c];
      this.r = r;
      this.c = c;
   } 


   /**
    * Creates a new <TT>DMatrix</TT> with <TT>r</TT> rows and
    *  <TT>c</TT> columns using the data in <TT>data</TT>.
    * 
    * @param data the data of the new <TT>DMatrix</TT>
    * 
    *   @param r the number of rows
    * 
    *   @param c the number of columns
    * 
    * 
    */
   public DMatrix (double[][] data, int r, int c)  {
      this (r, c);
      for(int i = 0; i < r; i++)
         for(int j = 0; j < c; j++)
         mat[i][j] = data[i][j];
   } 


   /**
    * Copy constructor.
    * 
    * @param that the <TT>DMatrix</TT> to copy
    * 
    */
   public DMatrix (DMatrix that)  {
      this (that.mat, that.r, that.c);
   } 


   /**
    * Given a symmetric positive-definite matrix <SPAN CLASS="MATH"><I>M</I></SPAN>, performs the
    * Cholesky decomposition of <SPAN CLASS="MATH"><I>M</I></SPAN> and returns the result as a lower triangular
    * matrix <SPAN CLASS="MATH"><I>L</I></SPAN>, such that <SPAN CLASS="MATH"><I>M</I> = <I>LL</I><SUP>T</SUP></SPAN>.
    * 
    * @param M the input matrix
    * 
    *   @param L the Cholesky lower triangular matrix
    * 
    * 
    */
   public static void CholeskyDecompose (double[][] M, double[][] L)  {
      int d = M.length;
      DoubleMatrix2D MM = new DenseDoubleMatrix2D (M);
      DoubleMatrix2D LL = new DenseDoubleMatrix2D (d, d);
      CholeskyDecomposition decomp = new CholeskyDecomposition (MM);
      LL = decomp.getL();
      for(int i = 0; i < L.length; i++)
         for(int j = 0; j <= i; j++)
            L[i][j] = LL.get(i,j);
      for(int i = 0; i < L.length; i++)
         for(int j = i + 1; j < L.length; j++)
            L[i][j] = 0.0;
    } 


   /**
    * Given a symmetric positive-definite matrix <SPAN CLASS="MATH"><I>M</I></SPAN>, performs the
    * Cholesky decomposition of <SPAN CLASS="MATH"><I>M</I></SPAN> and returns the result as a lower triangular
    * matrix <SPAN CLASS="MATH"><I>L</I></SPAN>, such that <SPAN CLASS="MATH"><I>M</I> = <I>LL</I><SUP>T</SUP></SPAN>.
    * 
    * @param M the input matrix
    * 
    *   @return the Cholesky lower triangular matrix
    * 
    */
   public static DoubleMatrix2D CholeskyDecompose (DoubleMatrix2D M)  {
      CholeskyDecomposition decomp = new CholeskyDecomposition (M);
      return decomp.getL();
    } 


   /**
    * Computes the principal components decomposition <SPAN CLASS="MATH"><I>M</I></SPAN> =
    *  
    * <SPAN CLASS="MATH"><I>U&#923;U</I><SUP>t</SUP></SPAN> by using the singular value decomposition of matrix
    * <SPAN CLASS="MATH"><I>M</I></SPAN>. Puts the eigenvalues, which are the diagonal elements of matrix <SPAN CLASS="MATH"><I>&#923;</I></SPAN>,
    * sorted by decreasing size, in vector <TT>lambda</TT>, and puts matrix
    *  
    * <SPAN CLASS="MATH"><I>A</I> = <I>U</I>(&Lambda;)<SUP>1/2</SUP></SPAN> in <TT>A</TT>.
    * 
    * @param M input matrix
    * 
    *   @param A matrix square root of M
    * 
    *   @param lambda the eigenvalues
    * 
    * 
    */
   public static void PCADecompose (double[][] M, double[][] A,
                                    double[] lambda) {
      int d = M.length;
      DoubleMatrix2D MM = new DenseDoubleMatrix2D (M);
      DoubleMatrix2D AA = new DenseDoubleMatrix2D (d, d);
      AA = PCADecompose(MM, lambda);

      for(int i = 0; i < d; i++)
         for(int j = 0; j < d; j++)
            A[i][j] = AA.get(i,j);
    } 


   /**
    * Computes the principal components decomposition <SPAN CLASS="MATH"><I>M</I></SPAN> =
    *  
    * <SPAN CLASS="MATH"><I>U&#923;U</I><SUP>t</SUP></SPAN> by using the singular value decomposition of matrix
    * <SPAN CLASS="MATH"><I>M</I></SPAN>. Puts the eigenvalues, which are the diagonal elements of matrix <SPAN CLASS="MATH"><I>&#923;</I></SPAN>,
    * sorted by decreasing size, in vector <TT>lambda</TT>. Returns matrix
    *  
    * <SPAN CLASS="MATH"><I>A</I> = <I>U</I>(&Lambda;)<SUP>1/2</SUP></SPAN>.
    * 
    * @param M input matrix
    * 
    *   @param lambda the eigenvalues
    * 
    *   @return matrix square root of M
    * 
    */
   public static DoubleMatrix2D PCADecompose (DoubleMatrix2D M,
                                              double[] lambda) {
      // L'objet SingularValueDecomposition permet de recuperer la matrice
      // des valeurs propres en ordre decroissant et celle des vecteurs propres de
      // sigma (pour une matrice symétrique et définie-positive seulement).

      SingularValueDecomposition sv = new SingularValueDecomposition(M);
      // D contient les valeurs propres sur la diagonale
      DoubleMatrix2D D = sv.getS ();

      for (int i = 0; i < D.rows(); ++i)
         lambda[i] = D.getQuick (i, i);

      // Calculer la racine carree des valeurs propres
      for (int i = 0; i < D.rows(); ++i)
         D.setQuick (i, i, Math.sqrt (lambda[i]));
      DoubleMatrix2D P = sv.getV();
      // Multiplier par la matrice orthogonale (ici P)
      return P.zMult (D, null);
   }


   /**
    * Solve the triangular matrix equation <SPAN CLASS="MATH"><I>UX</I> = <I>B</I></SPAN> for <SPAN CLASS="MATH"><I>X</I></SPAN>.
    *  <SPAN CLASS="MATH"><I>U</I></SPAN> is a square upper triangular matrix. <SPAN CLASS="MATH"><I>B</I></SPAN> and <SPAN CLASS="MATH"><I>X</I></SPAN> must have the same
    * number of columns.
    * 
    * @param U input matrix
    * 
    *   @param B right-hand side matrix
    * 
    *   @param X output matrix
    * 
    * 
    */
   public static void solveTriangular (DoubleMatrix2D U, DoubleMatrix2D B,
                                       DoubleMatrix2D X)  {
      final int n = U.rows();
      final int m = B.columns();
      double y, z;
      for (int j = 0; j < m; ++j) {
         for (int i = n - 1; i >= 0; --i) {
            z = B.getQuick(i, j);
            for (int k = i + 1; k < n; ++k)
               z -= U.getQuick(i, k) * X.getQuick(k, j);
            z /= U.getQuick(i, i);
            X.setQuick(i, j, z);
         }
      }
   } 


   /**
    * Similar to {@link #exp((DoubleMatrix2D)) exp}<TT>(A)</TT>.
    * 
    * @param A input matrix
    * 
    *   @return the exponential of <SPAN CLASS="MATH"><I>A</I></SPAN>
    * 
    */
   public static double[][] exp (double[][] A)  {
      DoubleMatrix2D V = new DenseDoubleMatrix2D(A);
      DoubleMatrix2D R = exp(V);
      return R.toArray();
   } 


   /**
    * Returns <SPAN CLASS="MATH"><I>e</I><SUP>A</SUP></SPAN>, the exponential of the square matrix <SPAN CLASS="MATH"><I>A</I></SPAN>.
    * The squaring and scaling method is used with
    * Pad&#233; approximants to compute the exponential.
    * <SPAN CLASS="MATH"><I>A</I></SPAN> is unchanged by the method.
    * 
    * @param A input matrix
    * 
    *   @return the exponential of <SPAN CLASS="MATH"><I>A</I></SPAN>
    * 
    */
   public static DoubleMatrix2D exp (final DoubleMatrix2D A)  {
      /*
       * Use the diagonal Padé approximant of order [9/9] for exp:
       * See Higham J.H., Functions of matrices, SIAM, 2008.
       */
      DoubleMatrix2D B = A.copy();
      int n = B.rows();
      Algebra alge = new Algebra();
      final double mu = alge.trace(B) / n;
      double x;

      // B <-- B - mu*I
      for (int i = 0; i < n; ++i) {
         x = B.getQuick (i, i);
         B.setQuick (i, i, x - mu);
      }
      /*
      int bal = 0;
      if (bal > 0) {
         throw new UnsupportedOperationException ("   balancing");
      } */

      final double THETA9 = 2.097847961257068;   // in Higham
      final double norm = alge.norm1(B) / THETA9;
      int s;
      if (norm > 1)
         s = (int) Math.ceil(Num.log2(norm));
      else
         s = 0;

      Functions F = Functions.functions;    // alias F
      // B <-- B/2^s
      double v = 1.0 / Math.pow(2.0, s);
      if (v <= 0)
          throw new IllegalArgumentException ("   v <= 0");
      B.assign (F.mult(v));

      DoubleFactory2D fac = DoubleFactory2D.dense;
      final DoubleMatrix2D B0 = fac.identity(n);    // B^0 = I
      final DoubleMatrix2D B2 = alge.mult(B, B);    // B^2
      final DoubleMatrix2D B4 = alge.mult(B2, B2);  // B^4

      DoubleMatrix2D T = B2.copy();          // T = work matrix
      DoubleMatrix2D W = B4.copy();          // W = work matrix
      W.assign (F.mult(cPade[9]));           // W <-- W*cPade[9]
      W.assign (T, F.plusMult(cPade[7]));    // W <-- W + T*cPade[7]
      DoubleMatrix2D U = alge.mult(B4, W);   // U <-- B4*W

      // T = B2.copy();
      W = B4.copy();
      W.assign (F.mult(cPade[5]));           // W <-- W*cPade[5]
      W.assign (T, F.plusMult(cPade[3]));    // W <-- W + T*cPade[3]
      W.assign (B0, F.plusMult(cPade[1]));   // W <-- W + B0*cPade[1]
      U.assign (W, F.plus);                  // U <-- U + W
      U = alge.mult(B, U);                   // U <-- B*U

      // T = B2.copy();
      W = B4.copy();
      W.assign (F.mult(cPade[8]));           // W <-- W*cPade[8]
      W.assign (T, F. plusMult(cPade[6]));   // W <-- W + T*cPade[6]
      DoubleMatrix2D V = alge.mult(B4, W);   // V <-- B4*W

      // T = B2.copy();
      W = B4.copy();
      W.assign (F.mult(cPade[4]));           // W <-- W*cPade[4]
      W.assign (T, F.plusMult(cPade[2]));    // W <-- W + T*cPade[2]
      W.assign (B0, F.plusMult(cPade[0]));   // W <-- W + B0*cPade[0]
      V.assign (W, F.plus);                  // V <-- V + W

      W = V.copy();
      W.assign(U, F.plus);                   // W = V + U, Padé numerator
      T = V.copy();
      T.assign(U, F.minus);                  // T = V - U, Padé denominator

      // Compute Padé approximant for exponential = W / T
      LUDecomposition lu = new LUDecomposition(T);
      B = lu.solve(W);

      if (false) {
         // This overflows for large |mu|
         // B <-- B^(2^s)
         for(int i = 0; i < s; i++)
            B = alge.mult(B, B);
         /*
         if (bal > 0) {
            throw new UnsupportedOperationException ("   balancing");
         } */
         v = Math.exp(mu);
         B.assign (F.mult(v));               // B <-- B*e^mu

      } else {
         // equivalent to B^(2^s) * e^mu, but only if no balancing
         double r = mu * v;
         r = Math.exp(r);
         B.assign (F.mult(r));
         for (int i = 0; i < s; i++)
            B = alge.mult(B, B);
      }

      return B;
   } 


   /**
    * Returns <SPAN CLASS="MATH"><I>e</I><SUP>A</SUP></SPAN>, the exponential of the <SPAN  CLASS="textit">bidiagonal</SPAN>
    *  square matrix <SPAN CLASS="MATH"><I>A</I></SPAN>. The only non-zero elements of <SPAN CLASS="MATH"><I>A</I></SPAN> are on the diagonal and
    *  on the first superdiagonal. This method is faster than
    * {@link #exp((DoubleMatrix2D)) exp}<TT>(A)</TT> because of the special form of
    * <SPAN CLASS="MATH"><I>A</I></SPAN>: as an example, for 
    * <SPAN CLASS="MATH">100&#215;100</SPAN> matrices, it is about 10 times faster
    * than the general method <TT>exp</TT>.
    * 
    * @param A input bidiagonal matrix
    * 
    *   @return the exponential of <SPAN CLASS="MATH"><I>A</I></SPAN>
    * 
    * 
    */
   public static DoubleMatrix2D expBidiagonal (final DoubleMatrix2D A)  {
      // Use the diagonal Padé approximant of order [9/9] for exp:
      // See Higham J.H., Functions of matrices, SIAM, 2008.

      DoubleMatrix2D B = A.copy();
      final int n = B.rows();
      Algebra alge = new Algebra();
      final double mu = alge.trace(B) / n;
      double x;

      // B <-- B - mu*I
      for (int i = 0; i < n; ++i) {
         x = B.getQuick(i, i);
         B.setQuick(i, i, x - mu);
      }

      final double THETA9 = 2.097847961257068; // in Higham
      final double norm = norm1(B) / THETA9;
      int s;
      if (norm > 1)
         s = (int) Math.ceil(Num.log2(norm));
      else
         s = 0;

      final double v = 1.0 / Math.pow(2.0, s);
      if (v <= 0)
         throw new IllegalArgumentException("   v <= 0");
      DMatrix.multBand(B, 1, v); // B <-- B/2^s

      DoubleFactory2D fac = DoubleFactory2D.dense;
      DoubleMatrix2D T = fac.make(n, n);
      DoubleMatrix2D B4 = fac.make(n, n);
      DMatrix.multBand(B, 1, B, 1, T); // B^2
      DMatrix.multBand(T, 2, T, 2, B4); // B^4

      DoubleMatrix2D W = B4.copy(); // W = work matrix
      DMatrix.addMultBand(cPade[9], W, 4, cPade[7], T, 2); // W <-- W*cPade[9] + T*cPade[7]
      DoubleMatrix2D U = fac.make(n, n);
      DMatrix.multBand(W, 4, B4, 4, U); // U <-- B4*W

      W = B4.copy();
      DMatrix.addMultBand(cPade[5], W, 4, cPade[3], T, 2); // W <-- W*cPade[5] + T*cPade[3]
      for (int i = 0; i < n; ++i) {   // W <-- W + I*cPade[1]
         x = W.getQuick(i, i);
         W.setQuick(i, i, x + cPade[1]);
      }
      DMatrix.addMultBand(1.0, U, 8, 1.0, W, 4); // U <-- U + W
      DMatrix.multBand(B, 1, U, 8, U); // U <-- B*U

      W = B4.copy();
      DMatrix.addMultBand(cPade[8], W, 4, cPade[6], T, 2); // W <-- W*cPade[8] + T*cPade[6]
      DoubleMatrix2D V = B;
      DMatrix.multBand(W, 4, B4, 4, V); // V <-- B4*W

      W = B4.copy();
      DMatrix.addMultBand(cPade[4], W, 4, cPade[2], T, 2); // W <-- W*cPade[4] + T*cPade[2]
      for (int i = 0; i < n; ++i) {   // W <-- W + I*cPade[0]
         x = W.getQuick(i, i);
         W.setQuick(i, i, x + cPade[0]);
      }
      DMatrix.addMultBand(1.0, V, 8, 1.0, W, 4); // V <-- V + W

      W = V.copy();
      DMatrix.addMultBand(1.0, W, 9, 1.0, U, 9); // W = V + U, Padé numerator
      T = V.copy();
      DMatrix.addMultBand(1.0, T, 9, -1.0, U, 9); // T = V - U, Padé denominator

      // Compute Padé approximant B = W/T for exponential
      DMatrix.solveTriangular(T, W, B);

      // equivalent to B^(2^s) * e^mu
      double r = mu * v;
      r = Math.exp(r);
      DMatrix.multBand(B, n - 1, r); // B <-- B*r
      for (int i = 0; i < s; i++) {
         DMatrix.multBand(B, n - 1, B, n - 1, T);
         B = T.copy();
      }

      return B;
	}


   /**
    * Matrix multiplication <SPAN CLASS="MATH"><I>C</I> = <I>AB</I></SPAN>. All three matrices are square,
    * banded, and upper triangular. <SPAN CLASS="MATH"><I>A</I></SPAN> has a non-zero diagonal, <TT>sa</TT> non-zero
    *  superdiagonals, and thus a bandwidth of <TT>sa + 1</TT>.
    * The non-zero elements of <SPAN CLASS="MATH"><I>A</I><SUB>ij</SUB></SPAN> are those for which 
    * <SPAN CLASS="MATH"><I>j</I> - <I>s</I><SUB>a</SUB>&nbsp;&lt;=&nbsp;<I>i</I>&nbsp;&lt;=&nbsp;<I>j</I></SPAN>.
    * Similarly for <SPAN CLASS="MATH"><I>B</I></SPAN> which has a bandwidth of <TT>sb + 1</TT>.
    * The resulting matrix <SPAN CLASS="MATH"><I>C</I></SPAN> has <TT>sa + sb</TT> non-zero superdiagonals, and a
    * bandwidth of <TT>sa + sb + 1</TT>.
    * 
    * @param A input matrix
    * 
    *   @param sa number of superdiagonals of A
    * 
    *   @param B input matrix
    * 
    *   @param sb number of superdiagonals of B
    * 
    *   @param C result
    * 
    * 
    */
   static void multBand (final DoubleMatrix2D A, int sa,
                                final DoubleMatrix2D B, int sb,
                                DoubleMatrix2D C)  {
      DoubleMatrix2D T, S;
      if (A == C)
         S = A.copy ();
      else
         S = A;
      if (B == C)
         T = B.copy ();
      else
         T = B;
      innerMultBand (S, sa, T, sb, C);
   } 


   /**
    * Multiplication of the matrix <SPAN CLASS="MATH"><I>A</I></SPAN> by the scalar <SPAN CLASS="MATH"><I>h</I></SPAN>.
    * <SPAN CLASS="MATH"><I>A</I></SPAN> is a square banded upper triangular matrix. It has a non-zero diagonal,
    * <TT>sa</TT> superdiagonals, and thus a bandwidth of <TT>sa + 1</TT>.
    * The result of the multiplication is put back in <SPAN CLASS="MATH"><I>A</I></SPAN>.
    * 
    * @param A input and output matrix
    * 
    *   @param sa number of superdiagonals of A
    * 
    *   @param h scalar
    * 
    * 
    */
   static void multBand (DoubleMatrix2D A, int sa, double h)  {
      final int n = A.rows();
      double z;
      for (int i = 0; i < n; ++i) {
         int jmax = Math.min(i + sa, n - 1);
         for (int j = i; j <= jmax; ++j) {
            z = A.getQuick (i, j);
            A.setQuick (i, j, z*h);
         }
      }
   } 


   static void addMultBand (double g, DoubleMatrix2D A, int sa,
                                   double h, final DoubleMatrix2D B, int sb)  {
      final int n = A.rows();
      double z;
      for (int i = 0; i < n; ++i) {
         int jmax = Math.max(i + sa, i + sb);
         jmax = Math.min(jmax, n - 1);
         for (int j = i; j <= jmax; ++j) {
            z = g*A.getQuick (i, j) + h*B.getQuick (i, j);
            A.setQuick (i, j, z);
         }
      }
   } 


   /**
    * Returns matrix <SPAN CLASS="MATH"><I>M</I></SPAN> as a string.
    * It is displayed in matrix form, with each row on a line.
    * 
    * @return the content of <SPAN CLASS="MATH"><I>M</I></SPAN>
    * 
    */
   public static String toString(double[][] M)  {
      StringBuffer sb = new StringBuffer();

      sb.append("{" + PrintfFormat.NEWLINE);
      for (int i = 0; i < M.length; i++) {
         sb.append("   { ");
         for(int j = 0; j < M[i].length; j++) {
            sb.append(M[i][j] + " ");
            if (j < M.length - 1)
               sb.append(" ");
         }
         sb.append("}" + PrintfFormat.NEWLINE);
      }
      sb.append("}");

      return sb.toString();
   } 


   /**
    * Creates a {@link String} containing all the data of
    *   the <TT>DMatrix</TT>. The result is displayed in matrix form, with
    *   each row on a line.
    * 
    * @return the content of the <TT>DMatrix</TT>
    * 
    */
   public String toString()  {
      return toString(mat);
   } 


   /**
    * Returns the number of rows of the <TT>DMatrix</TT>.
    * 
    * @return the number of rows
    * 
    */
   public int numRows()  {
      return r;
   } 


   /**
    * Returns the number of columns of the <TT>DMatrix</TT>.
    * 
    * @return the number of columns
    * 
    */
   public int numColumns()  {
      return c;
   } 


   /**
    * Returns the matrix element in the specified row and column.
    * 
    * @param row the row of the selected element
    * 
    *   @param column the column of the selected element
    * 
    *   @return the value of the element
    *   @exception IndexOutOfBoundsException if the selected element would
    *     be outside the <TT>DMatrix</TT>
    * 
    * 
    */
   public double get (int row, int column)  {
      if (row >= r || column >= c)
         throw new IndexOutOfBoundsException();

      return mat[row][column];
   } 


   /**
    * Sets the value of the element in the specified row and column.
    * 
    * @param row the row of the selected element
    * 
    *   @param column the column of the selected element
    * 
    *   @param value the new value of the element
    * 
    *   @exception IndexOutOfBoundsException if the selected element would
    *     be outside the <TT>DMatrix</TT>
    * 
    * 
    */
   public void set (int row, int column, double value)  {
      if (row >= r || column >= c)
         throw new IndexOutOfBoundsException();

      mat[row][column] = value;
   } 


   /**
    * Returns the transposed matrix. The rows and columns are
    *   interchanged.
    * 
    * @return the transposed matrix
    * 
    */
   public DMatrix transpose()  {
      DMatrix result = new DMatrix(c,r);

      for(int i = 0; i < r; i++)
         for(int j = 0; j < c; j++)
            result.mat[j][i] = mat[i][j];

      return result;
   } 


}

