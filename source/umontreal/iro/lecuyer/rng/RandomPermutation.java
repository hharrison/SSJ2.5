

/*
 * Class:        RandomPermutation
 * Description:  Provides methods to randomly shuffle arrays or lists
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

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;


/**
 * Provides methods to randomly shuffle arrays
 * or lists using a random stream.
 * 
 */
public class RandomPermutation {
   private static final int SHUFFLE_THRESHOLD = 5;


   /**
    * Initializes <TT>array</TT> with the first <SPAN CLASS="MATH"><I>n</I></SPAN>
    *   positive integers in natural order as <TT>array</TT><SPAN CLASS="MATH">[<I>i</I> - 1] = <I>i</I></SPAN>, for
    *   <SPAN CLASS="MATH"><I>i</I> = 1,..., <I>n</I></SPAN>.  The size of <TT>array</TT> must be at least <SPAN CLASS="MATH"><I>n</I></SPAN>.
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (byte[] array, int n) {
      for (byte k = 1; k <= n; k++)
         array[k-1] = k;
   }


   /**
    * Similar to {@link #init init}<TT>(byte[], int)</TT>.
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (short[] array, int n) {
      for (short k = 1; k <= n; k++)
         array[k-1] = k;
   }


   /**
    * Similar to {@link #init init}<TT>(byte[], int)</TT>.
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (int[] array, int n) {
      for (int k = 1; k <= n; k++)
         array[k-1] = k;
   }


   /**
    * Similar to {@link #init init}<TT>(byte[], int)</TT>.
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (long[] array, int n) {
      for (int k = 1; k <= n; k++)
         array[k-1] = k;
   }


   /**
    * Similar to {@link #init init}<TT>(byte[], int)</TT>.
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (float[] array, int n) {
      for (int k = 1; k <= n; k++)
         array[k-1] = k;
   }


   /**
    * Similar to {@link #init init}<TT>(byte[], int)</TT>.
    * 
    * <P><P>
    * <BR>
    * 
    * @param array the array to initialize.
    * 
    *    @param n number of elements initialized.
    * 
    * 
    */
   public static void init (double[] array, int n) {
      for (int k = 1; k <= n; k++)
         array[k-1] = k;
   }

@SuppressWarnings("unchecked")
   /**
    * Same as <TT>java.util.Collections.shuffle(List&lt;?&gt;, Random)</TT>,
    *  but uses a {@link RandomStream} instead of <TT>java.util.Random</TT>.
    * 
    * @param list the list being shuffled.
    * 
    *    @param stream the random stream used to generate integers.
    * 
    * 
    */
   public static void shuffle (List<?> list, RandomStream stream) {
      // The implementation is inspired from Sun's Collections.shuffle
      final int size = list.size ();
      if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
         for (int i = size; i > 1; i--)
            Collections.swap (list, i - 1, stream.nextInt (0, i - 1));

      } else {
         final Object arr[] = list.toArray ();

         // Shuffle array<
         shuffle (arr, stream);

         // Dump array back into list
         final ListIterator it = list.listIterator ();
         for (Object element : arr) {
            it.next ();
            it.set (element);
         }
      }
   }


   /**
    * Randomly permutes <TT>array</TT> using <TT>stream</TT>.
    *   This method permutes the whole array.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (Object[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final Object tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Randomly permutes <TT>array</TT> using <TT>stream</TT>.
    *   This method permutes the whole array.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (byte[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final byte tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (short[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final short tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (int[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final int tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (long[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final long tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (char[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final char tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (boolean[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final boolean tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (float[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final float tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }


   /**
    * Similar to {@link #shuffle shuffle}<TT>(byte[], RandomStream)</TT>.
    * 
    * <P><P>
    * <BR>
    * 
    * @param array the array being shuffled.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (double[] array, RandomStream stream) {
      final int size = array.length;
      for (int i = size - 1; i > 0; i--) {
         final int j = stream.nextInt (0, i);
         final double tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
      }
   }

@SuppressWarnings("unchecked")
   /**
    * Partially permutes <TT>list</TT> as follows using <TT>stream</TT>:
    *  draws the first <SPAN CLASS="MATH"><I>k</I></SPAN> new elements of <TT>list</TT> randomly among the <SPAN CLASS="MATH"><I>n</I></SPAN> old
    *  elements of <TT>list</TT>, assuming that <SPAN CLASS="MATH"><I>k</I>&nbsp;&lt;=&nbsp;<I>n</I> =</SPAN> <TT>list.size()</TT>.
    *  In other words, <SPAN CLASS="MATH"><I>k</I></SPAN> elements are selected at random without replacement from
    *  the <SPAN CLASS="MATH"><I>n</I></SPAN> <TT>list</TT> entries and are placed in the first <SPAN CLASS="MATH"><I>k</I></SPAN> positions,
    *  in random order.
    * 
    * @param list the list being shuffled.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate integers.
    * 
    * 
    */
   public static void shuffle (List<?> list, int k, RandomStream stream) {
      // @precondition 0 <= k <= n <= size.

      // The implementation is inspired from Sun's Collections.shuffle
      final int size = list.size ();
      if (k < 0 || k > size)
         throw new IllegalArgumentException("k must be   0 <= k <= list.size()");
      if (0 == k) return;
      if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
         for (int i = 0; i < k; i++) {
            // Get random j in {i,...,n-1} and interchange a[i] with a[j].
            int j = stream.nextInt (i, size-1);
            Collections.swap (list, i, j);
         }

      } else {
         final Object arr[] = list.toArray ();

         // Shuffle array<
         shuffle (arr, size, k, stream);

         // Dump array back into list
         final ListIterator it = list.listIterator ();
         for (Object element : arr) {
            it.next ();
            it.set (element);
         }
      }
   }


   /**
    * Partially permutes <TT>array</TT> as follows
    *  using <TT>stream</TT>: draws the new <SPAN CLASS="MATH"><I>k</I></SPAN> elements, <TT>array[0]</TT> to
    *  <TT>array[k-1]</TT>, randomly among the old <SPAN CLASS="MATH"><I>n</I></SPAN> elements, <TT>array[0]</TT>
    *  to <TT>array[n-1]</TT>, assuming that 
    * <SPAN CLASS="MATH"><I>k</I>&nbsp;&lt;=&nbsp;<I>n</I>&nbsp;&lt;=&nbsp;</SPAN> <TT>array.length</TT>.
    *  In other words, <SPAN CLASS="MATH"><I>k</I></SPAN> elements are selected at random without replacement
    *  from the first <SPAN CLASS="MATH"><I>n</I></SPAN> array elements and are placed in the first 
    *  <SPAN CLASS="MATH"><I>k</I></SPAN> positions, in random order.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (Object[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      // Replace by 
      // if (k < 0 || k > n) throw new IllegalArgumentException();
      // or at least assert 0 <= k && k <= n;
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         Object temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (byte[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         byte temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (short[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         short temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (int[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         int temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (long[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         long temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (char[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         char temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (boolean[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         boolean temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (float[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         float temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }


   /**
    * Similar to 
    *  {@link #shuffle shuffle}<TT>(Object[], n, k, RandomStream)</TT>.
    * 
    * @param array the array being shuffled.
    * 
    *    @param n selection amongst the first n elements.
    * 
    *    @param k number of elements selected.
    * 
    *    @param stream the random stream used to generate random numbers.
    * 
    * 
    */
   public static void shuffle (double[] array, int n, int k,
                               RandomStream stream) {
      // @precondition 0 <= k <= n <= a.length.
      if (k < 0 || k > n)
         throw new IllegalArgumentException("k must be   0 <= k <= n");
      for (int i = 0; i < k; i++) {
         // Get random j in {i,...,n-1} and interchange a[i] with a[j].
         int j = stream.nextInt (i, n-1);
         double temp = array[j];
         array[j] = array[i];
         array[i] = temp;
      }
   }

}
