

/*
 * Class:        BasicRandomStreamFactory
 * Description:  basic random stream factory
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
import umontreal.iro.lecuyer.rng.MRG32k3a;


/**
 * Represents a basic random stream factory that can constructs new
 * instances of a given {@link RandomStream} implementation via the
 * {@link #newInstance(()) newInstance} method.
 * The class name of the implementation to be used must be passed to
 * the constructor as a <TT>String</TT>, which must be the name of
 * a nullary constructor of a {@link RandomStream} object
 * (i.e., a constructor that has no parameters).
 * The streams are constructed by the factory by reflection from this
 * <TT>String</TT>. 
 * 
 */
public class BasicRandomStreamFactory implements RandomStreamFactory {
   private Class rsClass;


   /**
    * Constructs a new basic random stream factory with
    *  random stream class <TT>rsClass</TT>.
    *  The supplied class object must represent an implementation
    *  of {@link RandomStream} and must provide a nullary
    *  constructor.  For example, to construct a factory
    *  producing {@link MRG32k3a} random streams, this constructor
    *  must be called with <TT>MRG33k3a.class</TT>.
    * 
    * @param rsClass the random stream class being used.
    * 
    *    @exception NullPointerException if <TT>rsClass</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>rsClass</TT> does
    *     not represent an implementation of {@link RandomStream}, or
    *     does not provide a nullary constructor.
    * 
    * 
    */
   public BasicRandomStreamFactory (Class rsClass) {
      checkRandomStreamClass (rsClass);
      this.rsClass = rsClass;
   }


   /**
    * Returns the random stream class associated with this
    *  object.
    * 
    * @return the associated random stream class.
    * 
    */
   public Class getRandomStreamClass() {
      return rsClass;
   }


   /**
    * Sets the associated random stream class to
    *  <TT>rsClass</TT>.
    *  The supplied class object must represent an implementation
    *  of {@link RandomStream} and must provide a nullary
    *  constructor.
    * 
    * @param rsClass the random stream class being used.
    * 
    *    @exception NullPointerException if <TT>rsClass</TT> is <TT>null</TT>.
    * 
    *    @exception IllegalArgumentException if <TT>rsClass</TT> does
    *     not represent an implementation of {@link RandomStream}, or
    *     does not provide a nullary constructor.
    * 
    * 
    */
   public void setRandomStreamClass (Class rsClass) {
      checkRandomStreamClass (rsClass);
      this.rsClass = rsClass;
   }


   private void checkRandomStreamClass (final Class rsClass) {
      if (!RandomStream.class.isAssignableFrom (rsClass))
         throw new IllegalArgumentException
            ("The random class must implement the RandomStream interface");
      try {
         rsClass.getConstructor (new Class[0]);
      }
      catch (NoSuchMethodException nme) {
         throw new IllegalArgumentException
            ("The random stream class " + rsClass.getName() + " does not have a " +
             "nullary public constructor.");
      }
   }

   public RandomStream newInstance() {
      try {
         return (RandomStream)rsClass.newInstance();
      }
      catch (IllegalAccessException iae) {
         throw new RandomStreamInstantiationException
            (this, "Cannot access constructor for random stream class " + rsClass.getName(),
             iae);
      }
      catch (InstantiationException ie) {
         throw new RandomStreamInstantiationException
            (this, "Cannot instantiate random stream class "
             + rsClass.getName(), ie);
      }
      catch (Exception e) {
         throw new RandomStreamInstantiationException
            (this, "Exception while calling the nullary constructor for random stream class "
             + rsClass.getName(), e);
      }
   }

   public String toString() {
      return "Basic random stream factory constructing streams of class " + rsClass.getName();
   }
}
