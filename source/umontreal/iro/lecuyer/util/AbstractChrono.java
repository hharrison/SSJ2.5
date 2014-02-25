

/*
 * Class:        AbstractChrono
 * Description:  calculates CPU time of parts of a program
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


/**
 * <TT>AbstractChrono</TT> is a class that acts as an interface to
 * the system clock and calculates the CPU or system time consumed by
 * parts of a program.
 * 
 * <P>
 * Every object of class <TT>AbstractChrono</TT> acts as an independent <EM>stopwatch</EM>.
 * Several <TT>AbstractChrono</TT> objects can run at any given time.
 * The method {@link #init init} resets the stopwatch to zero,
 * {@link #getSeconds getSeconds}, {@link #getMinutes getMinutes} and {@link #getHours getHours}
 *  return its current reading,
 * and {@link #format format} converts this reading to a {@link String}.
 * The returned value includes the execution time of the method
 * from <TT>AbstractChrono</TT>.
 * 
 * <P>
 * Below is an example of how it may be used.
 * A stopwatch named <TT>timer</TT> is constructed (and initialized).
 * When 2.1 seconds of CPU time have been consumed,
 * the stopwatch is read and reset to zero.
 * Then, after an additional 330 seconds (or 5.5 minutes) of CPU time,
 * the stopwatch is read again and the value is printed to the output
 * in minutes.
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * 
 * <BR>
 * AbstractChrono timer = new Chrono();
 * <BR></TT>
 * </DIV>
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * 
 * <BR>
 * <BR>&nbsp;&nbsp;&nbsp;suppose 2.1 CPU seconds are used here
 * <BR>
 * <BR></TT>
 * </DIV>
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * double t = timer.getSeconds(); &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Here, t = 2.1
 * <BR>
 * timer.init();
 * <BR>
 * t = timer.getSeconds(); &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Here, t = 0.0
 * <BR></TT>
 * </DIV>
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * 
 * <BR>
 * <BR>&nbsp;&nbsp;&nbsp;suppose 330 CPU seconds are used here.
 * <BR>
 * <BR></TT>
 * </DIV>
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * t = timer.getMinutes(); &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Here, t = 5.5
 * <BR>
 * System.out.println (timer.format()); &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Prints: 0:5:30.00
 * <BR></TT>
 * </DIV>
 * 
 */
public abstract class AbstractChrono {

   private long m_second;
   private long m_microsec;
   private long[] now = new long[2];
   // tab[0] = seconds, tab[1] = microseconds

   protected abstract void getTime (long[] tab);


   public AbstractChrono() {
   }


   /**
    * Initializes this <TT>AbstractChrono</TT> to zero.
    * 
    */
   public void init()  {
      getTime (now);
      m_second = now[0];
      m_microsec = now[1];
   }


   /**
    * Returns the CPU time in seconds used by the program since the last call to
    *   {@link #init init} for this <TT>AbstractChrono</TT>.
    *  
    * @return the number of seconds
    * 
    */
   public double getSeconds() {
      getTime (now);
      double time = (now[1] - m_microsec)/1000000.0
             + (now[0] - m_second);
      return time;
   }


   /**
    * Returns the CPU time in minutes used by the program since the last call to
    *   {@link #init init} for this <TT>AbstractChrono</TT>.
    *  
    * @return the number of minutes
    * 
    */
   public double getMinutes() {
      getTime (now);
      double time = (now[1] - m_microsec)/1000000.0
             + (now[0] - m_second);
      return time*1.666666667*0.01;
   }


   /**
    * Returns the CPU time in hours used by the program since the last call to
    *   {@link #init init} for this <TT>AbstractChrono</TT>.
    *  
    * @return the number of hours
    * 
    */
   public double getHours() {
      getTime (now);
      double time = (now[1] - m_microsec)/1000000.0
             + (now[0] - m_second);
      return time*2.777777778*0.0001;
   }


   /**
    * Converts the CPU time used by the program since its last
    *    call to {@link #init init} for this <TT>AbstractChrono</TT> to a
    *   {@link String} in  the <TT>HH:MM:SS.xx</TT> format.
    *   
    * @return the string representation of the CPU time
    * 
    */
   public String format() {
      return format (getSeconds());
   }


   /**
    * Converts the time <TT>time</TT>, given in seconds, to a
    *   {@link String} in the <TT>HH:MM:SS.xx</TT> format.
    *   
    * @return the string representation of the time <TT>time</TT>
    * 
    */
   public static String format (double time) {
      int second, hour, min, centieme;
      hour = (int)(time/3600.0);
      if (hour > 0) time -= ((double)hour*3600.0);
      min = (int)(time/60.0);
      if (min > 0) time -= ((double)min*60.0);
      second = (int)time;
      centieme = (int)(100.0*(time - (double)second) + 0.5);
      return String.valueOf (hour) + ":" +
                      min + ":" +
                      second + "." +
                      centieme;
   }

}
