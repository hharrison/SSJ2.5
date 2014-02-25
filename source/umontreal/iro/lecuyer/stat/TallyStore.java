

/*
 * Class:        TallyStore
 * Description:  
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

package umontreal.iro.lecuyer.stat;
import cern.colt.list.DoubleArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import umontreal.iro.lecuyer.util.PrintfFormat;



/**
 * This class is a variant of {@link Tally} for which the individual
 * observations are stored in a list implemented as a
 * {@link cern.colt.list.DoubleArrayList DoubleArrayList}.
 * The {@link cern.colt.list.DoubleArrayList DoubleArrayList} object used to
 * store the values can be either passed to the constructor or created by
 * the constructor, and can be accessed via the
 * {@link #getDoubleArrayList getDoubleArrayList} method.
 * 
 * <P>
 * The same counters as in {@link Tally} are maintained and are used by
 * the inherited methods.  One must access the list of observations
 * to compute quantities not supported by the methods in
 * {@link Tally}, and/or to use methods provided by the COLT package.
 * 
 * <P>
 * <SPAN  CLASS="textit">Never add or remove observations directly</SPAN> on the
 * {@link cern.colt.list.DoubleArrayList DoubleArrayList} object,
 * because this would put the counters of the <TT>TallyStore</TT> object in an
 * inconsistent state.
 * 
 * <P>
 * There are two potential reasons for using a {@link TallyStore} object
 * instead of directly using a {@link cern.colt.list.DoubleArrayList DoubleArrayList} object:
 * (a) it can broadcast observations and
 * (b) it maintains a few additional counters that may speed up some operations
 * such as computing the average.
 * 
 */
public class TallyStore extends Tally  {

   private DoubleArrayList array = null;  // Where the observations are stored.
   private Logger log = Logger.getLogger ("umontreal.iro.lecuyer.stat");



   /**
    * Constructs a new <TT>TallyStore</TT> statistical probe.
    * 
    */
   public TallyStore()  {
      super();
      array = new DoubleArrayList();
   }


   /**
    * Constructs a new <TT>TallyStore</TT> statistical probe with name <TT>name</TT>.
    * 
    * @param name the name of the tally.
    * 
    * 
    */
   public TallyStore (String name)  {
      super (name);
      array = new DoubleArrayList();
   }


   /**
    * Constructs a new <TT>TallyStore</TT> statistical probe
    *     with given initial capacity <TT>capacity</TT> for its associated array.
    * 
    * @param capacity initial capacity of the array of observations
    * 
    * 
    */
   public TallyStore (int capacity)  {
      super();
      array = new DoubleArrayList (capacity);
   }


   /**
    * Constructs a new <TT>TallyStore</TT> statistical probe with
    * name <TT>name</TT> and given initial capacity <TT>capacity</TT> for its
    *  associated array.
    * 
    * @param name the name of the tally.
    * 
    *    @param capacity initial capacity of the array of observations
    * 
    * 
    */
   public TallyStore (String name, int capacity)  {
      super (name);
      array = new DoubleArrayList (capacity);
   }


   /**
    * Constructs a new <TT>TallyStore</TT> statistical probe
    *     with given associated array.  This array must be empty.
    * 
    * @param a array that will contain observations
    * 
    */
   public TallyStore (DoubleArrayList a)  {
      super();
      array = a;
      array.clear();
   }


   public void init() {
       super.init();
       // We must call super before any actions inside constructors.
       // Unfortunately, the base class calls init, which would
       // result in a NullPointerException.
       if (array != null)
          array.clear();
   }

   public void add (double x) {
      if (collect) array.add (x);
      super.add(x);
   }

   /**
    * Returns the observations stored in this probe.
    * 
    * @return the array of observations associated with this object
    * 
    */
   public double[] getArray()  {
      array.trimToSize();
      return array.elements();
   }


   /**
    * Returns the {@link cern.colt.list.DoubleArrayList DoubleArrayList}
    *    object that contains the observations for this probe. <SPAN  CLASS="textbf">WARNING:</SPAN>
    *    In previous releases, this function was named <TT>getArray</TT>.
    * 
    * @return the array of observations associated with this object
    * 
    */
   public DoubleArrayList getDoubleArrayList()  {
      array.trimToSize();
      return array;
   }


   /**
    * Sorts the elements of this probe using the <TT>quicksort</TT>
    *   from Colt.
    * 
    */
   public void quickSort()  {
       array.quickSort();
   }


   /**
    * Returns the sample covariance of the observations contained
    *  in this tally, and the other tally <TT>t2</TT>.
    *  Both tallies must have the same number of observations.
    *    This returns <TT>Double.NaN</TT>
    *    if the tallies do not contain the same number of observations, or
    *  if they contain less than two observations.
    * 
    * @param t2 the other tally.
    * 
    *    @return the sample covariance.
    * 
    */
   public double covariance (TallyStore t2) {
      if (numberObs() != t2.numberObs()) {
         //System.err.println
         //   ("******* TallyStore.covariance():   Tally's with different number of observations");
         log.logp (Level.WARNING, "TallyStore", "covariance",
            "This tally, with name " + getName() + ", contains " + numberObs() + " observations while " +
              "the given tally, with name " + t2.getName() + ", contains " + t2.numberObs()
              + "observations");
         return Double.NaN;
      }

      if (numberObs() < 2 || t2.numberObs() < 2) {
         //System.err.println
         //   ("******* TallyStore.covariance()   with " + numberObs() + " Observation");
         log.logp (Level.WARNING, "TallyStore", "covariance",
            "This tally, with name " + getName() + ", contains " + numberObs() + " observation");
         return Double.NaN;
      }

      return cern.jet.stat.Descriptive.covariance (
          getDoubleArrayList(), t2.getDoubleArrayList());
   }


   /**
    * Clones this object and the array which stores the observations.
    * 
    */
   public TallyStore clone() {
      TallyStore t = (TallyStore)super.clone();
      t.array = (DoubleArrayList)array.clone();
      return t;
   }


   /**
    * Returns the observations stored in this object as a <TT>String</TT>.
    * 
    */
   public String toString() {
      StringBuffer sb = new StringBuffer ();
      for (int i=0; i<numberObs(); i++)
         sb.append (i + "    " + array.getQuick(i) +
                           PrintfFormat.NEWLINE);
      return sb.toString();
   }

}
