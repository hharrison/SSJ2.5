

/*
 * Class:        StatProbe
 * Description:  statistical probe
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
import java.util.List;
import java.util.ArrayList;
import umontreal.iro.lecuyer.util.PrintfFormat;


/**
 * The objects of this class are <SPAN  CLASS="textit">statistical probes</SPAN> or 
 * <SPAN  CLASS="textit">collectors</SPAN>, which are elementary devices for collecting
 * statistics. Each probe collects statistics on a given variable.
 * The subclasses {@link Tally}, {@link TallyStore}, and
 * {@link umontreal.iro.lecuyer.simevents.Accumulate Accumulate} (from package
 * {@link umontreal.iro.lecuyer.simevents simevents})
 * implement two kinds of probes, for the case of successive observations
 * 
 * <SPAN CLASS="MATH"><I>X</I><SUB>1</SUB>, <I>X</I><SUB>2</SUB>, <I>X</I><SUB>3</SUB>,...</SPAN>, and for the case of a variable whose value
 * evolves in time, respectively.
 * 
 * <P>
 * Each instance of
 * {@link StatProbe} contains a list of {@link ObservationListener} that can listen
 * to individual observations.  
 * When a probe is updated, i.e., receives a new statistical observation,
 * it broadcasts this new data to all registered observers.
 * The broadcasting of observations to registered observers can be turned
 * ON or OFF at any time.  
 * It is initially OFF by default and should stay OFF when there are 
 * no registered observers, to avoid unnecessary overhead.
 * 
 * <P>
 * The data collection by the statistical probe itself can also be turned
 * ON or OFF.  By default, it is initially ON.
 * We can turn it OFF, for example, if we want to use the statistical probe
 * only to pass data to the observers, and do not need it to store
 * any information.
 * 
 * <P>
 * In the simplest programs, collection is ON, broadcast is OFF, and the overall
 * stats are accessed via the methods 
 * <TT>min</TT>, <TT>max</TT>, <TT>sum</TT>, <TT>average</TT>, ... of the
 * collector.
 * 
 */
public abstract class StatProbe { 

   private List<ObservationListener> listeners = new ArrayList<ObservationListener>();
   protected String name;
   protected double maxValue;
   protected double minValue;
   protected double sumValue;
   protected boolean collect = true;
   protected boolean broadcast = false;
   protected boolean showNobs = true;




   /**
    * Initializes the statistical collector.
    * 
    */
   abstract public void init();


   /**
    * Sets the name of this statistical collector to <TT>name</TT>.
    * 
    */
   public void setName (String name)  {
      this.name = name; 
   } 


   /**
    * Returns the name associated with this probe,
    *    or <TT>null</TT> if no name was specified upon construction.
    * 
    * @return the name associated to this collector, 
    *    or <TT>null</TT> if not specified
    * 
    */
   public String getName() {
      return name;
   }


   /**
    * Returns the smallest value taken by the variable 
    *    since the last initialization
    *    of this probe.
    *    This returns <TT>Double.POSITIVE_INFINITY</TT>
    *    if the probe was not updated since the last initialization.
    *  
    * @return the smallest value taken by the collector since last initialization
    * 
    */
   public double min()  { 
      return minValue; 
   } 


   /**
    * Returns the largest value taken by the variable 
    *    since the last initialization
    *    of this probe.
    *    This returns <TT>Double.NEGATIVE_INFINITY</TT>
    *    if the probe was not updated since the last initialization.
    *  
    * @return the largest value taken by the collector since last initialization
    * 
    */
   public double max()  { 
      return maxValue; 
   } 


   /**
    * Returns the sum cumulated so far for this probe.
    *    The meaning of this sum depends on the subclass (e.g., {@link Tally} or
    *    {@link umontreal.iro.lecuyer.simevents.Accumulate Accumulate}).
    *    This returns 0
    *    if the probe was not updated since the last initialization.
    *  
    * @return the sum for this probe
    * 
    */
   public double sum()  { 
      return sumValue; 
   } 


   /**
    * Returns the average for this collector.
    *    This returns <TT>Double.NaN</TT>
    *    if the probe was not updated since the last initialization.
    *  
    * @return the average value of the collected observations
    * 
    */
   abstract public double average();


   /**
    * Returns a string containing a report for this statistical
    *    collector. The contents of this report depends on the statistical probe as
    *    well as on the parameters set by the user through probe-specific methods.
    *  
    * @return a report for this probe, represented as a string
    * 
    */
   abstract public String report();


   /**
    * Formats and returns a short, one-line report
    *  about this statistical probe.  This line is composed of
    *  whitespace-separated fields which must
    *  correspond to the column names given by {@link #shortReportHeader shortReportHeader}().
    *   This report should not contain any end-of-line character, and does not
    *    include the name of the probe.
    *    Its contents depends on the statistical probe as well as  on the
    *    parameters set by the user through probe-specific methods.
    * 
    * @return the short report for the probe.
    * 
    */
   abstract public String shortReport();


   /**
    * Returns a string containing
    *  the name of the values returned in the report
    *  strings.  The returned string must depend on the type of probe and on
    * the reporting options only.  It must not depend on the observations
    * received by the probe.
    * This can be used as header when printing
    *  several reports. For example,
    * <PRE>
    *          System.out.println (probe1.shortReportHeader());
    *          System.out.println (probe1.getName() + " " + probe1.shortReport());
    *          System.out.println (probe2.getName() + " " + probe2.shortReport());
    *          ...
    * </PRE>
    *   Alternatively, one can use {@link #report report}&nbsp;<TT>(String,StatProbe[])</TT>
    *   to get a report with aligned probe names.
    * 
    * @return the header string for the short reports.
    * 
    */
   abstract public String shortReportHeader();


   /**
    * Formats short reports for each statistical probe in the array
    *    <TT>probes</TT> while aligning the probes' names.
    *    This method first formats the given global name.
    *    It then determines the maximum length <SPAN CLASS="MATH"><IMG
    *  ALIGN="BOTTOM" BORDER="0" SRC="StatProbeimg1.png"
    *  ALT="$ \ell$"></SPAN> of the names of probes in the
    *    given array.
    *    The first line of the report is composed of <SPAN CLASS="MATH">[tex2html_wrap_inline221] + 3</SPAN> spaces followed by the
    *    string returned by {@link #shortReportHeader shortReportHeader} called on the first probe in
    *    <TT>probes</TT>. Each remaining line corresponds to a statistical probe; it
    *    contains the probe's name followed by the contents returned by
    *    {@link #shortReport shortReport}. Note that this method assumes that <TT>probes</TT>
    *    contains no <TT>null</TT> element.
    * 
    * @param globalName the global name given to the formatted report.
    * 
    *    @param probes the probes to include in the report.
    * 
    *    @return the formatted report.
    * 
    */
   public static String report (String globalName, StatProbe[] probes) {
      int maxn = 0;
      StatProbe firstProbe = null;
      for (StatProbe probe : probes) {
         if (firstProbe == null)
            firstProbe = probe;
         String s = probe.getName();
         if (s != null && s.length() > maxn)
            maxn = s.length();
      }
      if (firstProbe == null)
         return "";
      StringBuffer sb = new StringBuffer ("Report for ");
      sb.append (globalName).append (PrintfFormat.NEWLINE);
      for (int i = 0; i < maxn; i++)
         sb.append (' ');
      sb.append ("   ");
      sb.append (firstProbe.shortReportHeader()).append (PrintfFormat.NEWLINE);
      for (StatProbe probe : probes) {
         sb.append
            (PrintfFormat.s (-maxn, probe.getName()));
         sb.append ("   ");
         sb.append (probe.shortReport()).append (PrintfFormat.NEWLINE);
      }
      return sb.toString();
   }


   /**
    * Equivalent to {@link #report((String,StatProbe[])) report}, except that
    *    <TT>probes</TT> is an {@link Iterable} object instead of an array.
    *    Of course, the iterator returned by <TT>probes</TT> should enumerate the
    *    statistical probes to include in the report in a consistent and sensible order.
    * 
    * @param globalName the global name given to the formatted report.
    * 
    *    @param probes the probes to include in the report.
    * 
    *    @return the formatted report.
    * 
    */
   public static String report (String globalName,
                                Iterable<? extends StatProbe> probes) {
      int maxn = 0;
      StatProbe firstProbe = null;
      for (StatProbe probe : probes) {
         if (firstProbe == null)
            firstProbe = probe;
         String s = probe.getName();
         int sl = s == null ? 4 : s.length();
         if (sl > maxn)
            maxn = sl;
      }
      if (firstProbe == null)
         return "";
      StringBuffer sb = new StringBuffer ("Report for ");
      sb.append (globalName).append (PrintfFormat.NEWLINE);
      for (int i = 0; i < maxn; i++)
         sb.append (' ');
      sb.append ("   ");
      sb.append (firstProbe.shortReportHeader()).append (PrintfFormat.NEWLINE);
      for (StatProbe probe : probes) {
         sb.append
            (PrintfFormat.s (-maxn, probe.getName()));
         sb.append ("   ");
         sb.append (probe.shortReport()).append (PrintfFormat.NEWLINE);
      }
      return sb.toString();
   }


   /**
    * Determines if this statistical probe
    *  is broadcasting observations to registered observers.
    *  The default is <TT>false</TT>.
    * 
    * @return the status of broadcasting.
    * 
    */
   public boolean isBroadcasting() {
      return broadcast;
   }


   /**
    * Instructs the probe to turn its broadcasting ON or OFF.
    *   The default value is OFF. 
    * 
    * <P>
    * Warning: To avoid useless overhead and performance degradation, broadcasting
    *   should never be turned ON when there are no registered observers.
    * 
    * @param b <TT>true</TT> to turn broadcasting ON, <TT>false</TT> to turn it OFF
    * 
    * 
    */
   public void setBroadcasting (boolean b) {
      broadcast = b;
   }


   /**
    * Determines if this statistical probe
    *  is collecting values. The default is <TT>true</TT>.
    * 
    * @return the status of statistical collecting.
    * 
    */
   public boolean isCollecting() {
      return collect;
   }


   /**
    * Turns ON or OFF the collection of statistical
    *   observations.  The default value is ON.
    *   When statistical collection is turned OFF, 
    *   observations added to the probe are passed to the
    *   registered observers if broadcasting is turned ON, but are not
    *   counted as observations by the probe itself.
    * 
    * @param b <TT>true</TT> to activate statistical collection, 
    *    <TT>false</TT> to deactivate it
    * 
    * 
    */
   public void setCollecting (boolean b) {
      collect = b;
   }


   /**
    * Adds the observation listener <TT>l</TT> to the list of observers of
    *     this statistical probe.
    * 
    * @param l the new observation listener.
    * 
    *    @exception NullPointerException if <TT>l</TT> is <TT>null</TT>.
    * 
    * 
    */
   public void addObservationListener (ObservationListener l) {
      if (l == null)
         throw new NullPointerException();
      if (!listeners.contains (l))
         listeners.add (l);
   }


   /**
    * Removes the observation listener <TT>l</TT> from the list of observers of
    *     this statistical probe.
    * 
    * @param l the observation listener to be deleted.
    * 
    * 
    */
   public void removeObservationListener (ObservationListener l) {
      listeners.remove (l);
   }


   /**
    * Removes all observation listeners from the list of observers of
    *     this statistical probe.
    * 
    */
   public void clearObservationListeners() {
      listeners.clear();
   }


   /**
    * Notifies the observation <TT>x</TT> to all registered observers
    *    if broadcasting is ON.  Otherwise, does nothing.
    * 
    */
   public void notifyListeners (double x) {
      if (!broadcast)
         return;
      // We could also use the enhanced for loop here, but this is less efficient.
      final int nl = listeners.size();
      for (int i = 0; i < nl; i++)
         listeners.get (i).newObservation (this, x);
   }


   public StatProbe clone() throws CloneNotSupportedException {
      StatProbe s = (StatProbe)super.clone();
      s.listeners = new ArrayList<ObservationListener>(listeners);
      return s;
   }
} 

