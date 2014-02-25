

/*
 * Class:        TallyHistogram
 * Description:  Histogram of a tally
 * Environment:  Java
 * Software:     SSJ 
 * Copyright (C) 2001  Pierre L'Ecuyer and Université de Montréal
 * Organization: DIRO, Université de Montréal
 * @author       Richard Simard
 * @since        January 2011

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
import java.util.logging.Level;
import java.util.logging.Logger;
import umontreal.iro.lecuyer.util.PrintfFormat;



/**
 * This class is a extension of {@link Tally} which gives a more detailed view
 * of the observations statistics. The individual observations are assumed to
 * fall into different bins (boxes) of equal width on an interval. 
 * The total number of observations falling into the bins are kept in an array
 * of counters. This is useful, for example, if one wish to build a histogram
 * from the observations. One must access the array of bin counters
 * to compute quantities not supported by the methods in {@link Tally}.
 * 
 * <P>
 * <SPAN  CLASS="textit">Never add or remove observations directly</SPAN> on the array of 
 * bin counters because this would put the {@link Tally} counters in an
 * inconsistent state.
 * 
 */
public class TallyHistogram extends Tally  {
   private int[] co;         // counter: num of values in bin[i]
   private int numBins;      // number of bins
   private double m_h;       // width of 1 bin
   private double m_a;       // left boundary of first bin
   private double m_b;       // right boundary of last bin
   private Logger log = Logger.getLogger ("umontreal.iro.lecuyer.stat");



   /**
    * Constructs a <TT>TallyHistogram</TT> statistical probe.
    * Divide the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> into <SPAN CLASS="MATH"><I>s</I></SPAN> bins of equal width and initializes
    * a counter to 0 for each bin. Whenever an observation falls into a bin,
    * the bin counter is increased by 1. There are two extra bins (and counters)
    * that count the number of observations <SPAN CLASS="MATH"><I>x</I></SPAN> that fall outside the interval 
    * <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>: one for those <SPAN CLASS="MATH"><I>x</I> &lt; <I>a</I></SPAN>, and the other for  those <SPAN CLASS="MATH"><I>x</I> &gt; <I>b</I></SPAN>.
    * 
    * @param a left boundary of interval
    * 
    *    @param b right boundary of interval
    * 
    *    @param s number of bins
    * 
    * 
    */
   public TallyHistogram(double a, double b, int s)  {
      super();
      init (a, b, s);
   }


   /**
    * Constructs a new <TT>TallyHistogram</TT> statistical probe with
    * name <TT>name</TT>.
    * 
    * @param name the name of the tally.
    * 
    *    @param a left boundary of interval
    * 
    *    @param b right boundary of interval
    * 
    *    @param s number of bins
    * 
    */
   public TallyHistogram (String name, double a, double b, int s)  {
      super (name);
      init (a, b, s);
   }


   /**
    * Initializes this object.
    * Divide the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> into <SPAN CLASS="MATH"><I>s</I></SPAN> bins of equal width and initializes
    * all counters to 0.
    * 
    * @param s number of bins
    * 
    *    @param a left boundary of interval
    * 
    *    @param b right boundary of interval
    * 
    * 
    */
   public void init (double a, double b, int s)  {
      /* The counters co[1] to co[s] contains the number of observations
         falling in the interval [a, b]. 
         co[0] is the number of observations < a,
         and co[s+1] is the number of observations > b. 
      */

      super.init();
      if (b <= a) 
         throw new IllegalArgumentException ("   b <= a");
      co = new int[s + 2];
      numBins = s;
      m_h = (b - a) / s;
      m_a = a;
      m_b = b;
      for (int i = 0; i <= s + 1; i++)
         co[i] = 0;
   } 


   /**
    * Gives a new observation <SPAN CLASS="MATH"><I>x</I></SPAN> to the statistical collectors.
    * Increases by 1 the bin counter in which value <SPAN CLASS="MATH"><I>x</I></SPAN> falls.
    * Values that fall outside the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> are added in extra bin
    * counter bin[0] if <SPAN CLASS="MATH"><I>x</I> &lt; <I>a</I></SPAN>, and in bin[<SPAN CLASS="MATH"><I>s</I> + 1</SPAN>] if <SPAN CLASS="MATH"><I>x</I> &gt; <I>b</I></SPAN>.
    * 
    * @param x observation value
    * 
    * 
    */
   public void add (double x)  {
      super.add(x);
      if (x < m_a)
        ++co[0];
      else if (x > m_b)
        ++co[1 + numBins];
      else {
         int i = 1 + (int) ((x - m_a) / m_h);
         ++co[i];
      }
   }


   /**
    * Returns the bin counters. Each counter contains the number of
    * observations that fell in its corresponding bin.
    * The counters bin[<SPAN CLASS="MATH"><I>i</I></SPAN>], 
    * <SPAN CLASS="MATH"><I>i</I> = 1, 2,&#8230;, <I>s</I></SPAN> contain the number of observations
    * that fell in each subinterval of <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>. Values that fell outside the interval
    * <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN> were added in extra bin counter bin[0] if <SPAN CLASS="MATH"><I>x</I> &lt; <I>a</I></SPAN>, and in bin[<SPAN CLASS="MATH"><I>s</I> + 1</SPAN>]
    * if <SPAN CLASS="MATH"><I>x</I> &gt; <I>b</I></SPAN>. There are thus <SPAN CLASS="MATH"><I>s</I> + 2</SPAN> counters.
    * 
    * @return the array of counters
    * 
    */
   public int[] getCounters()  {
      return co;
   }


   /**
    * Returns the number of bins <SPAN CLASS="MATH"><I>s</I></SPAN> dividing the interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>. 
    * Does not count the two extra bins for the values of <SPAN CLASS="MATH"><I>x</I> &lt; <I>a</I></SPAN> or <SPAN CLASS="MATH"><I>x</I> &gt; <I>b</I></SPAN>.
    * 
    * @return the number of bins
    * 
    */
   public int getNumBins()  {
      return numBins;
   }


   /**
    * Returns the left boundary <SPAN CLASS="MATH"><I>a</I></SPAN> of interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @return left boundary of interval
    * 
    */
   public double getA()  {
      return m_a;
   }


   /**
    * Returns the right boundary <SPAN CLASS="MATH"><I>b</I></SPAN> of interval <SPAN CLASS="MATH">[<I>a</I>, <I>b</I>]</SPAN>.
    * 
    * @return right boundary of interval
    * 
    */
   public double getB()  {
      return m_b;
   }


   /**
    * Clones this object and the array which stores the counters.
    * 
    */
   public TallyHistogram clone() {
      TallyHistogram image = (TallyHistogram)super.clone();
      int[] coco = new int[2 + numBins];
      System.arraycopy (co, 0, coco, 0, 2 + numBins);
      image.co = coco;
      image.m_h = m_h;
      image.m_a = m_a;
      image.m_b = m_b;
      image.numBins = numBins;
      return image;
   }


   /**
    * Returns the bin counters as a <TT>String</TT>.
    * 
    */
   public String toString() {
      StringBuffer sb = new StringBuffer ();
      sb.append ("---------------------------------------" + 
                PrintfFormat.NEWLINE);
      sb.append (name + PrintfFormat.NEWLINE);
      sb.append ("Interval = [ " + m_a + ", " + m_b + " ]" +
                 PrintfFormat.NEWLINE);
      sb.append ("Number of bins = " + numBins + " + 2" + PrintfFormat.NEWLINE);
      sb.append (PrintfFormat.NEWLINE + "Counters = {" +
                 PrintfFormat.NEWLINE);
      sb.append ("   (-inf, " + PrintfFormat.f(6, 3, m_a)
                 + ")    " + co[0] + PrintfFormat.NEWLINE);
      for (int i = 1; i <= numBins; i++) {
         double a = m_a + (i-1)*m_h; 
         double b = m_a + i*m_h; 
         sb.append ("   (" +
            PrintfFormat.f(6, 3, a) + ", " +
            PrintfFormat.f(6, 3, b) + ")    " + co[i] +
                 PrintfFormat.NEWLINE);
      }
      sb.append ("   (" + PrintfFormat.f(6, 3, m_b)
                 + ", inf)    " + co[numBins + 1] +
                 PrintfFormat.NEWLINE);
      sb.append ("}" + PrintfFormat.NEWLINE);
      return sb.toString();
   }

}
