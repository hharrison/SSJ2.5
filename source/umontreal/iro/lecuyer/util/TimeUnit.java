/**
 * Represents a time unit for conversion of time durations.
 * A time unit instance can be used to get information about
 * the time unit and as a selector to perform conversions.
 * Each time unit has a short name used when representing a time unit,
 * a full descriptive name, and the number of hours corresponding to one unit.
 * 
 */


/*
 * Class:        TimeUnit
 * Description:  Represents a time unit for conversion of time durations.
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


public enum TimeUnit {


   /**
    * Represents a nanosecond which has short name <TT>ns</TT>.
    * 
    */
   NANOSECOND

("ns", "nanosecond", 1.0/3600000000000.0),



   /**
    * Represents a microsecond which has short name <TT>us</TT>.
    * 
    */
   MICROSECOND

("us", "microsecond", 1.0/3600000000.0),



   /**
    * Represents a millisecond which has short name <TT>ms</TT>.
    * 
    */
   MILLISECOND

("ms", "millisecond", 1.0/3600000.0),



   /**
    * Represents a second which has short name <TT>s</TT>.
    * 
    */
   SECOND

("s", "second", 1.0/3600.0),



   /**
    * Represents a minute which has short name <TT>min</TT>.
    * 
    */
   MINUTE

("min", "minute", 1.0/60.0),



   /**
    * Represents an hour which has short name <TT>h</TT>.
    * 
    */
   HOUR

("h", "hour", 1.0),



   /**
    * Represents a day which has short name <TT>d</TT>.
    * 
    */
   DAY

("d", "day", 24.0),



   /**
    * Represents a week which has short name <TT>w</TT>.
    * 
    */
   WEEK

("w", "week", 24.0*7);



   private String shortName;
   private String longName;
   private transient double numHours;

   private TimeUnit (String shortName, String longName, double numHours) {
      this.shortName = shortName;
      this.longName = longName;
      this.numHours = numHours;
   }


   /**
    * Returns the short name representing this unit in
    *  a string specifying a time duration.
    * 
    * @return the short name of this time unit.
    * 
    */
   public String getShortName() {
      return shortName;
   }


   /**
    * Returns the long name of this time unit.
    * 
    * @return the long name of this time unit.
    * 
    */
   public String getLongName() {
      return longName;
   }


   /**
    * Calls {@link #getLongName(()) getLongName}.
    * 
    * @return the result of {@link #getLongName(()) getLongName}.
    * 
    */
   public String toString() {
      return longName;
   }


   /**
    * Returns this time unit represented in hours.
    *  This returns the number of hours corresponding to
    *  one unit.
    * 
    * @return the time unit represented in hours.
    * 
    */
   public double getHours() {
      return numHours;
   }


   /**
    * Converts <TT>value</TT> expressed in time unit <TT>srcUnit</TT> to
    *  a time duration expressed in <TT>dstUnit</TT> and returns
    *  the result of the conversion.
    * 
    * @param value the value being converted.
    * 
    *    @param srcUnit the source time unit.
    * 
    *    @param dstUnit the destination time unit.
    * 
    *    @return the converted value.
    *    @exception NullPointerException if <TT>srcUnit</TT> or
    *     <TT>dstUnit</TT> are <TT>null</TT>.
    * 
    */
   public static double convert (double value, TimeUnit srcUnit,
                                 TimeUnit dstUnit) {
      double hours = value*srcUnit.getHours();
      return hours/dstUnit.getHours();
   }
}
