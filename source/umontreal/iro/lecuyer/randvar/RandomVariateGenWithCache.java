

/*
 * Class:        RandomVariateGenWithCache
 * Description:  random variate generator whose values are cached for efficiency
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

package umontreal.iro.lecuyer.randvar;

import umontreal.iro.lecuyer.randvar.RandomVariateGen;
import cern.colt.list.DoubleArrayList;
import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.probdist.Distribution;


/**
 * This class represents a random variate generator whose values
 * are cached for more efficiency when using
 * common random numbers.  An object
 * from this class is constructed with a reference to a
 * {@link RandomVariateGen} instance used to
 * get the random numbers.  These numbers
 * are stored in an internal array to be retrieved later.
 * The dimension of the array increases as the values
 * are generated.
 * If the {@link #nextDouble(()) nextDouble} method is called after
 * the object is reset (by calling 
 * {@link #setCachedValues((DoubleArrayList)) setCachedValues}), it gives back the cached
 * values instead of computing new ones.
 * If the cache is exhausted before the generator is reset,
 * new values are computed and added to the cache.
 * 
 * <P>
 * Such caching allows for a better performance with
 * common random numbers, when
 * generating random variates is time-consuming.
 * However, using such caching may lead to memory problems if
 * a large quantity of random numbers are needed.
 * 
 */
public class RandomVariateGenWithCache extends RandomVariateGen {
   private RandomVariateGen rvg;
   private DoubleArrayList values;
   private int index = 0;
   private boolean caching = true;



   /**
    * Constructs a new cached random variate generator with
    *  internal generator <TT>rvg</TT>.
    * 
    * @param rvg the random variate generator whose values are cached.
    * 
    *    @exception NullPointerException if <TT>rvg</TT> is <TT>null</TT>.
    * 
    * 
    */
   public RandomVariateGenWithCache (RandomVariateGen rvg) {
      if (rvg == null)
         throw new NullPointerException
            ("The given random variate generator cannot be null");
      this.rvg = rvg;
      values = new DoubleArrayList();
   }


   /**
    * Constructs a new cached random variate generator
    *  with internal generator <TT>rvg</TT>.  The <TT>initialCapacity</TT>
    *  parameter is used to set the initial capacity of the internal array
    *  which can grow as needed; it does not limit the maximal
    *  number of cached values.
    * 
    * @param rvg the random variate generator whose values are cached.
    * 
    *    @param initialCapacity the number of cached values.
    * 
    *    @exception NullPointerException if <TT>rvg</TT> is <TT>null</TT>.
    * 
    */
   public RandomVariateGenWithCache (RandomVariateGen rvg,
                                     int initialCapacity) {
      if (rvg == null)
         throw new NullPointerException
            ("The given random variate generator cannot be null");
      this.rvg = rvg;
      values = new DoubleArrayList (initialCapacity);
   }


   /**
    * Determines if the random variate generator is caching values,
    *  default being <TT>true</TT>.
    *  When caching is turned OFF, the {@link #nextDouble(()) nextDouble}
    *  method simply calls the corresponding method on the internal
    *  random variate generator, without storing the generated values.
    * 
    * @return the caching indicator.
    * 
    */
   public boolean isCaching() {
      return caching;
   }


   /**
    * Sets the caching indicator to <TT>caching</TT>.
    *  If caching is turned OFF, this method calls {@link #clearCache(()) clearCache}
    *  to clear the cached values.
    * 
    * @param caching the new value of the caching indicator.
    * 
    * 
    */
   public void setCaching (boolean caching) {
      if (this.caching && !caching)
         clearCache();
      this.caching = caching;
   }


   /**
    * Returns a reference to the random variate generator
    *  whose values are cached.
    * 
    * @return a reference to the random variate generator whose values are cached.
    * 
    */
   public RandomVariateGen getCachedGen() {
      return rvg;
   }


   /**
    * Sets the random variate generator whose values are cached to
    *  <TT>rvg</TT>.  If the generator is changed, the {@link #clearCache(()) clearCache}
    *  method is called.
    * 
    * @param rvg the new random variate generator whose values are cached.
    * 
    *    @exception NullPointerException if <TT>rvg</TT> is <TT>null</TT>.
    * 
    * 
    */
   public void setCachedGen (RandomVariateGen rvg) {
      if (rvg == null)
         throw new NullPointerException
            ("The given random variate generator cannot be null");
      if (rvg == this.rvg)
         return;
      this.rvg = rvg;
      clearCache();
   }


   /**
    * Clears the cached values for this cached generator.
    *  Any subsequent call will then obtain new values
    *  from the internal generator.
    * 
    */
   public void clearCache() {
      //values.clear();
      // Keep the array previously returned by getCachedValues
      // intact to allow caching values for several
      // replications.
      values = new DoubleArrayList();
      index = 0;
   }


   /**
    * Resets this generator to recover values from the cache.
    *  Subsequent calls
    *  to {@link #nextDouble(()) nextDouble} will return the cached random
    *  values until all the values are returned.  When the array
    *  of cached values is exhausted, the internal random variate
    *  generator is used to generate new values which are added
    *  to the internal array as well.
    *  This method is equivalent to calling {@link #setCacheIndex((int)) setCacheIndex}.
    * 
    */
   public void initCache() {
      index = 0;
   }


   /**
    * Returns the total number of values cached by this generator.
    * 
    * @return the total number of cached values.
    * 
    */
   public int getNumCachedValues() {
      return values.size();
   }


   /**
    * Return the index of the next cached value that will be
    *  returned by the generator.
    *  If the cache is exhausted, the 
    *  returned value corresponds to the value returned by
    *  {@link #getNumCachedValues(()) getNumCachedValues}, and a subsequent call to
    *  {@link #nextDouble(()) nextDouble} will generate a new variate rather than
    *  reading a previous one from the cache.
    *  If caching is disabled, this always returns 0.
    * 
    * @return the index of the next cached value.
    * 
    */
   public int getCacheIndex() {
      return index;
   }


   /**
    * Sets the index, in the cache, of the next value returned
    *  by {@link #nextDouble(()) nextDouble}.
    *  If <TT>newIndex</TT> is 0, this is equivalent to
    *  calling {@link #initCache(()) initCache}.
    *  If <TT>newIndex</TT> is {@link #getNumCachedValues(()) getNumCachedValues},
    *  subsequent calls to {@link #nextDouble(()) nextDouble} will add
    *  new values to the cache.
    * 
    * @param newIndex the new index.
    * 
    *    @exception IllegalArgumentException if <TT>newIndex</TT>
    *  is negative or greater than or equal to the cache size.
    * 
    * 
    */
   public void setCacheIndex (int newIndex) {
      if (newIndex < 0 || newIndex > values.size())
         throw new IllegalArgumentException
         ("newIndex must not be negative or greater than the cache size");
      index = newIndex;
   }


   /**
    * Returns an array list containing the values
    *  cached by this random variate generator.
    * 
    * @return the array of cached values.
    * 
    */
   public DoubleArrayList getCachedValues() {
      return values;
   }


   /**
    * Sets the array list containing the cached
    *  values to <TT>values</TT>.
    *  This resets the cache index to
    *  the size of the given array.
    * 
    * @param values the array list of cached values.
    * 
    *    @exception NullPointerException if <TT>values</TT> is <TT>null</TT>.
    * 
    * 
    */
   public void setCachedValues (DoubleArrayList values) {
      if (values == null)
         throw new NullPointerException();
      this.values = values;
      index = values.size();
   }


   public double nextDouble() {
      if (!caching)
         return rvg.nextDouble();
      else if (index >= values.size()) {
         double v = rvg.nextDouble();
         values.add (v);
         ++index;
         return v;
      }
      else
         return values.getQuick (index++);
   }

   public void nextArrayOfDouble (double[] v, int start, int n) {
      if (!caching) {
         rvg.nextArrayOfDouble (v, start, n);
         return;
      }
      int remainingValues = values.size() - index;
      if (remainingValues < 0)
         remainingValues = 0;
      int ncpy = Math.min (n, remainingValues);
      if (ncpy > 0) {
         System.arraycopy (values.elements(), index, v, start, ncpy);
         index += ncpy;
      }
      int ngen = n - ncpy;
      if (ngen > 0) {
         rvg.nextArrayOfDouble (v, start + ncpy, ngen);
         for (int i = ncpy; i < n; i++) {
            values.add (v[start + i]);
            ++index;
         }
      }
   }

   public RandomStream getStream() {
      return rvg.getStream();
   }

   public Distribution getDistribution() {
      return rvg.getDistribution();
   }
}
