

/*
 * Class:        PrintfFormat
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

package umontreal.iro.lecuyer.util; 

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Formatter;



/**
 * This class acts like a {@link StringBuffer} which defines new types
 * of <TT>append</TT> methods.
 * It defines certain functionalities of the ANSI C <TT>printf</TT>
 * function that also can be accessed through static methods.
 * The information given here is strongly inspired
 * from the <TT>man</TT> page of the C <TT>printf</TT> function.
 * 
 * <P>
 * Most methods of this class format numbers for the English US
 * locale only. One can use the Java class {@link java.util.Formatter Formatter}
 * for performing locale-independent formatting.
 * 
 */
public class PrintfFormat implements CharSequence, Appendable {
   private static NumberFormat nf = NumberFormat.getInstance (Locale.US);
   private static DecimalFormatSymbols dfs =
       new DecimalFormatSymbols (Locale.US);
   private static final int NDEC = 50;
   private static DecimalFormat[] dfe = new DecimalFormat[NDEC+1];
   private static DecimalFormat[] dfg = new DecimalFormat[NDEC+1];
   private StringBuffer sb;
/*
   private static void round (int b, StringBuffer num) {
      // round a real number up if it is >= 0.5
      // base is b

      // round up the fractional part
      int j = num.length() - 1;
      String bm1 = String.valueOf (b - 1);
      while (j >= 0 && num.charAt(j) == bm1.charAt(0)) {
         num.deleteCharAt(j);
         j--;
      }

      char c;
      if (j < 0)
         return;
      if (num.charAt(j) != '.') {
         c = num.charAt(j);
         ++c;
         num.replace(j, j + 1, Character.toString(c));
         return;
      }

      // round up the integral part
      j--;
      while (j >= 0 && num.charAt(j) == bm1.charAt(0)) {
         num.replace(j, j + 1, "0");
         j--;
      }

     if (j < 0 || num.charAt(j) == '-') {
         num.insert( j + 1, '1');
     } else if (num.charAt(j) == ' ') {
         num.replace(j, j + 1, "1");
     } else {
         c = num.charAt(j);
         ++c;
         num.replace(j, j + 1, Character.toString(c));
      }
  }*/ 


   /**
    * End-of-line symbol or line separator. It is ``
    * <SPAN CLASS="MATH">&#92;</SPAN>n''
    *  for Unix/Linux, ``
    * <SPAN CLASS="MATH">&#92;</SPAN>r
    * <SPAN CLASS="MATH">&#92;</SPAN>n" for MS-DOS/MS-Windows, and
    *   ``
    * <SPAN CLASS="MATH">&#92;</SPAN>r'' for Apple OSX.
    * 
    */
   public static final String NEWLINE =
               System.getProperty("line.separator");


   /**
    * Same as <TT>NEWLINE</TT>.
    * 
    */
   @Deprecated
   public static final String LINE_SEPARATOR =
               System.getProperty("line.separator");


   /**
    * Constructs a new buffer object containing an empty string.
    * 
    */
   public PrintfFormat() {
      sb = new StringBuffer();
   }


   /**
    * Constructs a new buffer object with an initial capacity of <TT>length</TT>.
    * 
    * @param length initial length of the buffer
    * 
    * 
    */
   public PrintfFormat (int length) {
      sb = new StringBuffer (length);
   }


   /**
    * Constructs a new buffer object containing the initial string <TT>str</TT>.
    * 
    * @param str initial contents of the buffer
    * 
    */
   public PrintfFormat (String str) {
      sb = new StringBuffer (str);
   }


   /**
    * Appends <TT>str</TT> to the buffer.
    * 
    * @param str string to append to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (String str) {
      sb.append (str);
      return this;
   }


   /**
    * Uses the {@link #s(int,String) s} static method to append <TT>str</TT> to the buffer.
    *    A minimum of <TT>fieldwidth</TT> characters will be used.
    * 
    * @param fieldwidth minimum number of characters that will be added to the buffer
    * 
    *    @param str string to append to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, String str) {
      sb.append (s (fieldwidth, str));
      return this;
   }


   /**
    * Appends <TT>x</TT> to the buffer.
    * 
    * @param x value being added to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (double x) {
      sb.append (x);
      return this;
   }


   /**
    * Uses the {@link #f(int,double) f} static method to append <TT>x</TT> to the buffer.
    *    A minimum of <TT>fieldwidth</TT> characters will be used.
    * 
    * @param fieldwidth minimum length of the converted string to be appended
    * 
    *    @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, double x) {
      sb.append (f (fieldwidth, x));
      return this;
   }


   /**
    * Uses the {@link #f(int,int,double) f} static method to append <TT>x</TT> to the buffer.
    *    A minimum of <TT>fieldwidth</TT> characters will be used with the given <TT>precision</TT>.
    * 
    * @param fieldwidth minimum length of the converted string to be appended
    * 
    *    @param precision number of digits after the decimal point of the converted value
    * 
    *    @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, int precision, double x) {
      sb.append (f (fieldwidth, precision, x));
      return this;
   }


   /**
    * Appends <TT>x</TT> to the buffer.
    * 
    * @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int x) {
      sb.append (x);
      return this;
   }


   /**
    * Uses the {@link #d(int,long) d} static method to append <TT>x</TT> to the buffer.
    *    A minimum of <TT>fieldwidth</TT> characters will be used.
    * 
    * @param fieldwidth minimum length of the converted string to be appended
    * 
    *    @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, int x) {
      sb.append (d (fieldwidth, x));
      return this;
   }


   /**
    * Appends <TT>x</TT> to the buffer.
    * 
    * @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (long x) {
      sb.append (x);
      return this;
   }


   /**
    * Uses the {@link #d(int,long) d} static method to append <TT>x</TT> to the buffer.
    *    A minimum of <TT>fieldwidth</TT> characters will be used.
    * 
    * @param fieldwidth minimum length of the converted string to be appended
    * 
    *    @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, long x) {
      sb.append (d (fieldwidth, x));
      return this;
   }


   /**
    * Uses the {@link #format(int,int,int,double) format}
    *    static method with the same four arguments to append <TT>x</TT> to the buffer.
    * 
    * @param fieldwidth minimum length of the converted string to be appended
    * 
    *    @param accuracy number of digits after the decimal point
    * 
    *    @param precision number of significant digits
    * 
    *    @param x value to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (int fieldwidth, int accuracy, int precision,
                               double x) {
      sb.append (format (fieldwidth, accuracy, precision, x));
      return this;
   }


   /**
    * Appends a single character to the buffer.
    * 
    * @param c character to be appended to the buffer
    * 
    *    @return this object
    * 
    */
   public PrintfFormat append (char c) {
      sb.append (c);
      return this;
   }


   /**
    * Clears the contents of the buffer.
    * 
    */
   public void clear() {
      sb.setLength (0);
   }


   /**
    * Returns the {@link StringBuffer} associated with that object.
    * 
    * @return the internal {@link StringBuffer} object
    * 
    */
   public StringBuffer getBuffer() {
      return sb;
   }


   /**
    * Converts the buffer into a {@link String}.
    * 
    * @return the {@link String} conversion of the internal buffer
    * 
    */
   public String toString() {
      return sb.toString();
   }


   /**
    * Same as {@link #s(int,String) s}&nbsp;<TT>(0, str)</TT>. If the string <TT>str</TT> is null,
    *    it returns the string ``null''.
    * 
    * @param str the string to process
    * 
    *    @return the same string
    * 
    */
   public static String s (String str) {
      if (str == null)
         return "null";
      else
         return str;
   }


   /**
    * Formats the string <TT>str</TT> like the <TT>%s</TT> in the C <TT>printf</TT>
    *  function. The <TT>fieldwidth</TT> argument gives the minimum length of the
    *  resulting string.  If <TT>str</TT> is shorter than <TT>fieldwidth</TT>, it is
    * left-padded with spaces.  If <TT>fieldwidth</TT> is negative, the string
    * is right-padded with spaces if necessary.  The {@link String} will never
    * be truncated. If <TT>str</TT> is null, it calls
    * {@link #s(int, String) s}&nbsp;<TT>(fieldwidth, ``null'')</TT>.
    * The <TT>fieldwidth</TT> argument
    * has the same effect for the other methods in this class.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param str the string to process
    * 
    *    @return the same string padded with spaces if necessary
    * 
    */
   public static String s (int fieldwidth, String str) {
      if (str == null)
         return s (fieldwidth, "null");

      int fw = Math.abs (fieldwidth);
      if (str.length() < fw) {
         // We have to pad with spaces
         StringBuffer buf = new StringBuffer();
         int sl = str.length();
         for (int i = 0; i < fw-sl; i++)
            buf.append (' ');
         // Add the spaces before or after
         return fieldwidth >= 0 ? buf.toString() + str
                     : str + buf.toString();
      }
      else
         return str;
   }


   /**
    * Same as {@link #d(int,int,long) d}&nbsp;<TT>(0, 1, x)</TT>.
    * 
    * @param x the string to process
    * 
    *    @return the same string, padded with spaces or zeros if appropriate
    * 
    */
   public static String d (long x) {
        return d (0, 1, x);
   }


   /**
    * Same as {@link #d(int,int,long) d}&nbsp;<TT>(fieldwidth, 1, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the string to process
    * 
    *    @return the same string, padded with spaces or zeros if appropriate
    * 
    */
   public static String d (int fieldwidth, long x) {
        return d (fieldwidth, 1, x);
   }


   /**
    * Formats the long integer <TT>x</TT> into a string like <TT>%d</TT>
    * in the C <TT>printf</TT> function.  It converts its argument to decimal
    * notation, <TT>precision</TT> gives the minimum
    * number of digits that must appear; if the converted
    * value  requires  fewer  digits, it is padded on the
    * left with zeros.  When
    * zero is printed with an explicit precision 0, the
    * output is empty.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of digits in the returned string
    * 
    *    @param x the string to process
    * 
    *    @return the same string, padded with spaces or zeros if appropriate
    * 
    */
   public static String d (int fieldwidth, int precision, long x) {
        if (precision < 0)
            throw new IllegalArgumentException ("precision must " +
                                               "not be negative.");
        if (precision == 0 && x == 0)
            return s (fieldwidth, "");

        nf.setGroupingUsed (false);
        nf.setMinimumIntegerDigits (precision);
        nf.setMaximumFractionDigits (0); // will also set the min to 0
        return s (fieldwidth, nf.format (x));
   }


   /**
    * Same as {@link #d(int,int,long) d}&nbsp;<TT>(0, 1, x)</TT>.
    * 
    * @param x the value to be processed
    * 
    *    @return the string resulting from the conversion
    * 
    */
   public static String format (long x) {
      return d (0, 1, x);
   }


   /**
    * Converts a long integer to a {@link String} with a minimum length
    * of <TT>fieldwidth</TT>, the result is right-padded with spaces if
    * necessary but it is not truncated.  If only one argument is specified,
    * a <TT>fieldwidth</TT> of 0 is assumed.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be processed
    * 
    *    @return the string resulting from the conversion
    * 
    */
   public static String format (int fieldwidth, long x) {
      return d (fieldwidth, 1, x);
   }


   /**
    * Same as {@link #formatBase(int,int,long) formatBase}&nbsp;<TT>(0, b, x)</TT>.
    * 
    * @param b the base used for conversion
    * 
    *    @param x the value to be processed
    * 
    *    @return a string representing <TT>x</TT> in base <TT>b</TT>
    * 
    */
   public static String formatBase (int b, long x) {
      return formatBase (0, b, x);
   }


   /**
    * Converts the integer <TT>x</TT> to a {@link String} representation in base
    * <TT>b</TT>. Restrictions: <SPAN CLASS="MATH">2&nbsp;&lt;=&nbsp;</SPAN> <TT>b</TT> <SPAN CLASS="MATH">&nbsp;&lt;=&nbsp;10</SPAN>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param b the base used for conversion
    * 
    *    @param x the value to be processed
    * 
    *    @return a string representing <TT>x</TT> in base <TT>b</TT>
    * 
    */
   public static String formatBase (int fieldwidth, int b, long x) {
      boolean neg = false;                   // insert a '-' if true
      if (b < 2 || b > 10)
         throw new IllegalArgumentException ("base must be between 2 and 10.");

      if (x < 0) {
         neg = true;
         x = -x;
      } else {
         if (x == 0)
            return "0";

         neg = false;
      }
      StringBuffer sb = new StringBuffer();
      while (x > 0) {
         sb.insert(0, x % b);
         x = x/b;
      }
      if (neg)
         sb.insert(0, '-');
      return s (fieldwidth, sb.toString());
   }


   /**
    * Same as {@link #E(int,int,double) E}&nbsp;<TT>(0, 6, x)</TT>.
    * 
    * @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String E (double x) {
        return E (0, 6, x);
   }


   /**
    * Same as {@link #E(int,int,double) E}&nbsp;<TT>(fieldwidth, 6, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String E (int fieldwidth, double x) {
        return E (fieldwidth, 6, x);
   }


   /**
    * Formats a double-precision number <TT>x</TT> like <TT>%E</TT> in C <TT>printf</TT>.  The double argument is rounded and converted in the
    * style <TT>[-]d.dddE+-dd</TT> where there is one digit  before
    * the  decimal-point character and the number of digits
    * after it is equal to the precision;
    * if the precision is 0, no decimal-point character appears.
    * The  exponent  always
    * contains at least two digits; if the value is zero,
    * the exponent is <TT>00</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of digits after the decimal point
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String E (int fieldwidth, int precision, double x) {
        if (precision < 0)
            throw new IllegalArgumentException ("precision must " +
                                               "not be negative.");
        if (Double.isNaN (x))
           return s (fieldwidth, "NaN");
        if (Double.isInfinite (x))
           return s (fieldwidth, (x < 0 ? "-" : "") + "Infinite");

        DecimalFormat df;
        if (precision >= dfe.length || dfe[precision] == null) {
          // We need to create one pattern per precision value
          StringBuffer pattern = new StringBuffer ("0.");
          for (int i = 0; i < precision; i++)
              pattern.append ("0");
          pattern.append ("E00");
          df = new DecimalFormat (pattern.toString(), dfs);
          df.setGroupingUsed (false);
          if (precision < dfe.length)
            dfe[precision] = df;
        }
        else
          df = dfe[precision];
        String res = df.format (x);
        // DecimalFormat doesn't add the + sign before the value of
        // the exponent.
        int exppos = res.indexOf ('E');
        if (exppos != -1 && res.charAt (exppos+1) != '-')
            res = res.substring (0, exppos+1) + "+" + res.substring (exppos+1);
        return s (fieldwidth, res);
   }


   /**
    * Same as {@link #e(int,int,double) e}&nbsp;<TT>(0, 6, x)</TT>.
    * 
    * @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String e (double x) {
        return e (0, 6, x);
   }


   /**
    * Same as {@link #e(int,int,double) e}&nbsp;<TT>(fieldwidth, 6, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String e (int fieldwidth, double x) {
        return e (fieldwidth, 6, x);
   }


   /**
    * The same as <TT>E</TT>, except that <TT>`e'</TT> is used as the exponent
    * character instead of <TT>`E'</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of digits after the decimal point
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String e (int fieldwidth, int precision, double x) {
        String res = E (fieldwidth, precision, x);
        int exppos = res.indexOf ('E');
        return exppos == -1 ? res : res.substring (0,
                               exppos) + 'e' + res.substring (exppos+1);
   }


   /**
    * Same as {@link #f(int,int,double) f}&nbsp;<TT>(0, 6, x)</TT>.
    * 
    * @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String f (double x) {
        return f (0, 6, x);
   }


   /**
    * Same as {@link #f(int,int,double) f}&nbsp;<TT>(fieldwidth, 6, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String f (int fieldwidth, double x) {
        return f (fieldwidth, 6, x);
   }


   /**
    * Formats the double-precision <TT>x</TT> into a string like
    * <TT>%f</TT> in  C <TT>printf</TT>.
    * The argument is  rounded  and  converted to
    * decimal notation in the style <TT>[-]ddd.ddd</TT>, where the
    * number of digits after the decimal-point character
    * is  equal  to  the precision specification.
    * If the precision is explicitly 0, no decimal-point
    * character appears.  If a decimal point appears, at least
    * one digit appears before it.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of digits after the decimal point
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String f (int fieldwidth, int precision, double x) {
        if (precision < 0)
            throw new IllegalArgumentException ("precision must " +
                                               "not be negative.");
        if (Double.isNaN (x))
           return s (fieldwidth, "NaN");
        if (Double.isInfinite (x))
           return s (fieldwidth, (x < 0 ? "-" : "" ) + "Infinite");

        nf.setGroupingUsed (false);
        nf.setMinimumIntegerDigits (1);
        nf.setMinimumFractionDigits (precision);
        nf.setMaximumFractionDigits (precision);
        return s (fieldwidth, nf.format (x));
   }


   /**
    * Same as {@link #G(int,int,double) G}&nbsp;<TT>(0, 6, x)</TT>.
    * 
    * @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String G (double x) {
        return G (0, 6, x);
   }


   /**
    * Same as {@link #G(int,int,double) G}&nbsp;<TT>(fieldwidth, 6, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String G (int fieldwidth, double x) {
        return G (fieldwidth, 6, x);
   }


   /**
    * Formats the double-precision <TT>x</TT> into a string like
    * <TT>%G</TT> in C <TT>printf</TT>.
    * The argument is converted in style <TT>%f</TT> or <TT>%E</TT>.
    * <TT>precision</TT> specifies
    * the number of significant digits.  If it is
    * 0,  it  is treated as 1.  Style <TT>%E</TT> is used if the
    * exponent from its conversion is less than <SPAN CLASS="MATH">-4</SPAN> or
    * greater than or equal to <TT>precision</TT>.  Trailing
    * zeros are removed from the fractional part of the
    * result; a decimal point appears only if it is followed
    * by at least one digit.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of significant digits
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String G (int fieldwidth, int precision, double x) {
        if (precision < 0)
            throw new IllegalArgumentException ("precision must " +
                                               "not be negative.");
        if (precision == 0)
            precision = 1;

        if (Double.isNaN (x))
           return s (fieldwidth, "NaN");
        if (Double.isInfinite (x))
           return s (fieldwidth, (x < 0 ? "-" : "" ) + "Infinite");

        // Calculate the scientific notation format.
        // We cannot use E because it can make trailing zeros
        // that must be removed afterward.
        DecimalFormat df;
        if (precision >= dfg.length || dfg[precision] == null) {
          StringBuffer pattern = new StringBuffer ("0.");
          for (int i = 0; i < (precision-1); i++)
              pattern.append ("#"); // Do not show trailing zeros
          pattern.append ("E00");
          df = new DecimalFormat (pattern.toString(), dfs);
          df.setGroupingUsed (false);
          if (precision < dfg.length)
            dfg[precision] = df;
        }
        else
          df = dfg[precision];
        String res = df.format (x);

        int exppos = res.indexOf ('E');
        if (exppos == -1)
           return res;
        int expval = Integer.parseInt (res.substring (exppos+1));
        if (expval < -4 || expval >= precision) {
           // add the plus sign for the exponent if necessary.
           if (res.charAt (exppos+1) != '-')
               return s (fieldwidth, res.substring (0, exppos+1) + "+" +
                          res.substring (exppos+1));
           else
               return s (fieldwidth, res);
        }

        // Calculate the decimal notation format
        nf.setGroupingUsed (false);
        nf.setMinimumIntegerDigits (1);
        nf.setMinimumFractionDigits (0);
        // We need the number of digits after the decimal point to
        // to get precisions significant digits.
        // The integer part of x contains at most precision digits.
        // If that was not true, expval would be greater than precision.
        // If expval=0, we have a number of the form 1.234...
        // To have precisions significant digits, we need precision-1 digits.
        // If expval<0, x<1 and we need -expval additionnal
        // decimal digits.
        // If expval>0, we need less decimal digits.
        nf.setMaximumFractionDigits (precision-expval-1);
        res = nf.format (x);
        return s (fieldwidth, res);
   }


   /**
    * Same as {@link #g(int,int,double) g}&nbsp;<TT>(0, 6, x)</TT>.
    * 
    * @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String g (double x) {
        return g (0, 6, x);
   }


   /**
    * Same as {@link #g(int,int,double) g}&nbsp;<TT>(fieldwidth, 6, x)</TT>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String g (int fieldwidth, double x) {
        return g (fieldwidth, 6, x);
   }


   /**
    * The same as <TT>G</TT>, except that <TT>`e'</TT> is used in the scientific
    * notation.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param precision number of significant digits
    * 
    *    @param x the value to be converted to string
    * 
    *    @return the converted value as a string
    * 
    */
   public static String g (int fieldwidth, int precision, double x) {
        String res = G (fieldwidth, precision, x);
        int exppos = res.indexOf ('E');
        return exppos == -1 ? res :
            res.substring (0, exppos) + 'e' + res.substring (exppos+1);
   }


   /**
    * Returns a {@link String} containing <TT>x</TT>.  Uses a total of at least
    *    <TT>fieldwidth</TT> positions (including the sign and point when they appear),
    *    <TT>accuracy</TT> digits after the decimal point and at least <TT>precision</TT>
    *    significant digits. <TT>accuracy</TT> and <TT>precision</TT> must be strictly
    *    smaller than <TT>fieldwidth</TT>. The number is rounded if necessary.
    *    If there is not enough space to format the number in decimal notation
    *    with at least <TT>precision</TT> significant digits (<TT>accuracy</TT> or
    *    <TT>fieldwidth</TT> is too small), it will be converted to scientific
    *    notation with at least <TT>precision</TT> significant digits.
    *    In that case, <TT>fieldwidth</TT> is increased if necessary.
    *   
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param accuracy number of digits after the decimal point
    * 
    *    @param precision number of significant digits
    * 
    *    @param x the value to be processed
    * 
    *    @return the converted value as a string
    * 
    */
   public static String format (int fieldwidth, int accuracy, int precision,
                                double x) {
        if (Double.isNaN (x))
           return s (fieldwidth, "NaN");
        if (Double.isInfinite (x))
           return s (fieldwidth, (x < 0 ? "-" : "" ) + "Infinite");

       if (canUseDecimalNotation (fieldwidth, accuracy, precision, x))
          return f (fieldwidth, accuracy, x);
       // Use scientific notation
       else {
          String S = E (fieldwidth, precision - 1 , x);
          return processExp (S);
       }
   }

   private static boolean canUseDecimalNotation (int fieldwidth, int accuracy,
                                                 int precision, double x) {
      // Le nombre de positions occupees par la partie entiere de x
      int PosEntier = 0;
      // Le nombre de chiffres significatifs avant le point
      int EntierSign;
      // La position de l'exposant dans le string S et la longueur de S
      int Neg = 0;

      if (x == 0.0)
         EntierSign = 1;
      else {
         EntierSign = PosEntier = (int)Math.floor (
               Math.log10 (Math.abs (x)) + 1);
         if (x < 0.0)
             Neg = 1;
      }
      if (EntierSign <= 0)
          PosEntier = 1;
      return x == 0.0 || (((EntierSign + accuracy) >= precision) &&
                          (fieldwidth >= (PosEntier + accuracy + Neg + 1)));
   }

   private static int getMinAccuracy (double x) {
      if (Math.abs (x) >= 1 || x == 0)
         return 0;
      else
         return -(int)Math.floor (Math.log10 (Math.abs (x)));
   }

   private static String processExp (String s) {
      int p = s.indexOf ("E+0");
      if (p == -1)
         p = s.indexOf ("E-0");

      // remove the 0 in E-0 and in E+0
      if (p != -1)
         s = " " + s.substring (0, p + 2) + s.substring (p + 3);

      p = s.indexOf (".E");
      if (p != -1)
         s = " " + s.substring (0, p) + s.substring (p + 1);
      return s;
   }


   /**
    * This method is equivalent to
    *   {@link #format(int,int,int,double) format}, except it formats the given
    *   value for the locale <TT>locale</TT>.
    *     
    * @param locale the locale being used for formatting
    * 
    *    @param fieldwidth minimum length of the returned string
    * 
    *    @param accuracy number of digits after the decimal point
    * 
    *    @param precision number of significant digits
    * 
    *    @param x the value to be processed
    * 
    *    @return the converted value as a string
    * 
    */
   public static String format (Locale locale, int fieldwidth, int accuracy,
                                int precision, double x) {
         Formatter fmt = new Formatter (locale);
        if (Double.isNaN (x))
           return fmt.format ("%" + fieldwidth + "s", "NaN").toString();
        if (Double.isInfinite (x))
           return fmt.format ("%" + fieldwidth + "s", (x < 0 ? "-" : "" ) + "Infinite").toString();

       if (canUseDecimalNotation (fieldwidth, accuracy, precision, x))
          return fmt.format ("%" + fieldwidth + "." + accuracy + "f", x).toString();
       // Use scientific notation
       else {
          String S = fmt.format ("%" + fieldwidth + "." + (precision - 1) + "E", x).toString();
          return processExp (S);
       }
   }


   /**
    * Converts <SPAN CLASS="MATH"><I>x</I></SPAN> to a String representation in base <SPAN CLASS="MATH"><I>b</I></SPAN> using formatting similar
    * to the <SPAN CLASS="MATH"><I>f</I></SPAN> methods. Uses a total of at least <TT>fieldwidth</TT> positions
    * (including the sign and point when they appear) and <TT>accuracy</TT> digits
    *  after the decimal point. If <TT>fieldwidth</TT> is negative, the number is
    *   printed  left-justified, otherwise right-justified.
    *   Restrictions: 
    * <SPAN CLASS="MATH">2&nbsp;&lt;=&nbsp;<I>b</I>&nbsp;&lt;=&nbsp;10</SPAN> and 
    * <SPAN CLASS="MATH">| <I>x</I>| &lt; 2<SUP>63</SUP></SPAN>.
    * 
    * @param fieldwidth minimum length of the returned string
    * 
    *    @param accuracy number of digits after the decimal point
    * 
    *    @param b base
    * 
    *    @param x the value to be processed
    * 
    *    @return the converted value as a string
    * 
    * 
    */
   public static String formatBase (int fieldwidth, int accuracy, int b,
                                    double x) {
      if (Double.isNaN (x))
         return s (fieldwidth, "NaN");
      if (Double.isInfinite (x))
         return s (fieldwidth, (x < 0 ? "-" : "" ) + "Infinite");
      if (0. == x || -0. == x)
         return s (fieldwidth, "0");
      if (Math.abs(x) >= Num.TWOEXP[63])
         throw new UnsupportedOperationException ("   |x| >= 2^63");

      long n = (long) x;
      String mant = formatBase(-1, b, n);
      if (n == x)
         return s (fieldwidth, mant);
      if (n == 0) {
         if (x < 0) {
            mant = "-0";
         } else
            mant = "0";
      }
      // round before printing
      if (x > 0)
         x += 0.5*Math.pow(b, -accuracy - 1);
      else if (x < 0)
         x -= 0.5*Math.pow(b, -accuracy - 1);
       x -= n;
      if (x < 0)
         x = -x;

      StringBuffer frac = new StringBuffer(".");
      long y;
      int j;
      for (j = 0; j < accuracy; ++j) {
         x *= b;
         y = (long) x;
         frac.append(y);
         x -= y;
         if (x == 0.)
            break;
      }

      StringBuffer number = new StringBuffer(mant);
      number.append(frac);

      // remove trailing spaces and 0
      j = number.length() - 1;
      while (j > 0 && (number.charAt(j) == '0' || number.charAt(j) == ' ' )) {
         number.deleteCharAt(j);
         j--;
      }

      return s (fieldwidth, number.toString());
   }

   // Interface CharSequence
   public char charAt (int index) {
      return sb.charAt (index);
   }

   public int length() {
      return sb.length();
   }

   public CharSequence subSequence (int start, int end) {
      return sb.subSequence (start, end);
   }

   // Interface Appendable
   public Appendable append (CharSequence csq) {
      return sb.append (csq);
   }

   public Appendable append (CharSequence csq, int start, int end) {
      return sb.append (csq, start, end);
   }


   /**
    * Stores a string containing <TT>x</TT> into <TT>res[0]</TT>, and
    *    a string containing <TT>error</TT> into <TT>res[1]</TT>, both strings being
    *    formatted with the same notation.
    *    Uses a total of at least
    *    <TT>fieldwidth</TT> positions (including the sign and point when they appear)
    *    for <TT>x</TT>, <TT>fieldwidtherr</TT> positions for <TT>error</TT>,
    *    <TT>accuracy</TT> digits after the decimal point and at least <TT>precision</TT>
    *    significant digits. <TT>accuracy</TT> and <TT>precision</TT> must be strictly
    *    smaller than <TT>fieldwidth</TT>. The numbers are rounded if necessary.
    *    If there is not enough space to format <TT>x</TT> in decimal notation
    *    with at least <TT>precision</TT> significant digits (<TT>accuracy</TT> or
    *    <TT>fieldwidth</TT> are too small), it will be converted to scientific
    *    notation with at least <TT>precision</TT> significant digits.
    *    In that case, <TT>fieldwidth</TT> is increased if necessary, and
    *    the error is also formatted in scientific notation.
    * 
    * @param fieldwidth minimum length of the value string
    * 
    *    @param fieldwidtherr minimum length of the error string
    * 
    *    @param accuracy number of digits after the decimal point for the value and error
    * 
    *    @param precision number of significant digits for the value
    * 
    *    @param x the value to be processed
    * 
    *    @param error the error on the value to be processed
    * 
    *    @param res an array that will be filled with the formatted value and formatted error
    * 
    * 
    */
   public static void formatWithError (int fieldwidth, int fieldwidtherr,
          int accuracy, int precision, double x, double error, String[] res) {
      if (res.length != 2)
         throw new IllegalArgumentException ("The given res array must contain two elements");
      if (Double.isNaN (x)) {
         res[0] = s (fieldwidth, "NaN");
         res[1] = s (fieldwidtherr, "");
         return;
      }
      if (Double.isInfinite (x)) {
         res[0] = s (fieldwidth, (x < 0 ? "-" : "" ) + "Infinite");
         res[1] = s (fieldwidtherr, "");
         return;
      }
      if (accuracy < 0)
         accuracy = 0;

      if (canUseDecimalNotation (fieldwidth, accuracy, precision, x)) {
         res[0] = f (fieldwidth, accuracy, x);
         res[1] = f (fieldwidtherr, accuracy, error);
      }
      // Use scientific notation
      else {
         res[0] = processExp (E (fieldwidth, precision - 1, x));
         int xExp = x == 0 ? 0 : (int)Math.floor (Math.log10 (Math.abs (x)));
         int errorExp = error == 0 ? 0 : (int)Math.floor (Math.log10 (Math.abs (error)));
         int errorPrecision = precision - 1 - (xExp - errorExp);
         if (errorPrecision < 0)
            errorPrecision = 0;
         res[1] = processExp (E (fieldwidtherr, errorPrecision, error));
      }
   }


   /**
    * Stores a string containing <TT>x</TT> into <TT>res[0]</TT>, and
    *    a string containing <TT>error</TT> into <TT>res[1]</TT>, both strings being
    *    formatted with the same notation.
    *    This calls {@link #formatWithError(int,int,int,int,double,double,String[]) formatWithError} with
    *    the minimal accuracy for which the formatted string for <TT>error</TT> is non-zero.
    *    If <TT>error</TT> is 0, the accuracy is 0.
    *    If this minimal accuracy causes the strings to be formatted using scientific
    *    notation, this method increases the accuracy until the decimal notation can be used.
    * 
    * @param fieldwidth minimum length of the value string
    * 
    *    @param fieldwidtherr minimum length of the error string
    * 
    *    @param precision number of significant digits for the value
    * 
    *    @param x the value to be processed
    * 
    *    @param error the error on the value to be processed
    * 
    *    @param res an array that will be filled with the formatted value and formatted error
    * 
    * 
    */
   public static void formatWithError (int fieldwidth, int fieldwidtherr,
          int precision, double x, double error, String[] res) {
      int accuracy = getMinAccuracy (error);
      if (!canUseDecimalNotation (fieldwidth, accuracy, precision, x)) {
         int posEntier = (int)Math.floor (Math.log (Math.abs (x)) / Math.log (10) + 1);
         if (posEntier < 0)
            posEntier = 1;
         int newAccuracy = precision - posEntier;
         if (canUseDecimalNotation (fieldwidth, newAccuracy, precision, x))
            accuracy = newAccuracy;
      }
      formatWithError (fieldwidth, fieldwidtherr, accuracy, precision, x, error, res);
   }


   /**
    * This method is equivalent to
    *   {@link #formatWithError(int,int,int,double,double,String[]) formatWithError},
    *   except that it formats the given value and error for the
    *   locale <TT>locale</TT>.
    * 
    * @param locale the locale being used
    * 
    *    @param fieldwidth minimum length of the value string
    * 
    *    @param fieldwidtherr minimum length of the error string
    * 
    *    @param accuracy number of digits after the decimal point for the value and error
    * 
    *    @param precision number of significant digits for the value
    * 
    *    @param x the value to be processed
    * 
    *    @param error the error on the value to be processed
    * 
    *    @param res an array that will be filled with the formatted value and formatted error
    * 
    * 
    */
   public static void formatWithError (Locale locale, int fieldwidth,
          int fieldwidtherr, int accuracy, int precision, double x,
          double error, String[] res) {
      if (res.length != 2)
         throw new IllegalArgumentException ("The given res array must contain two elements");
      Formatter fmt = new Formatter (locale);
      Formatter fmtErr = new Formatter (locale);
      if (Double.isNaN (x)) {
         res[0] = fmt.format ("%" + fieldwidth + "s", "NaN").toString();
         res[1] = fmtErr.format ("%" + fieldwidtherr + "s", "").toString();
         return;
      }
      if (Double.isInfinite (x)) {
         res[0] = fmt.format ("%" + fieldwidth + "s", (x < 0 ? "-" : "" ) + "Infinite").toString();
         res[1] = fmtErr.format ("%" + fieldwidtherr + "s", "").toString();
         return;
      }
      if (accuracy < 0)
         accuracy = 0;

      if (canUseDecimalNotation (fieldwidth, accuracy, precision, x)) {
         res[0] = fmt.format ("%" + fieldwidth + "." + accuracy + "f", x).toString();
         res[1] = fmtErr.format ("%" + fieldwidtherr + "." + accuracy + "f", error).toString();
      }
      // Use scientific notation
      else {
         res[0] = processExp (fmt.format ("%" + fieldwidth + "." + (precision - 1) + "E", x).toString());
         int xExp = x == 0 ? 0 : (int)Math.floor (Math.log10 (Math.abs (x)));
         int errorExp = error == 0 ? 0 : (int)Math.floor (Math.log10 (Math.abs (error)));
         int errorPrecision = precision - 1 - (xExp - errorExp);
         if (errorPrecision < 0)
            errorPrecision = 0;
         res[1] = processExp (fmtErr.format
         ("%" + fieldwidtherr + "." + errorPrecision + "E", error).toString());
      }
   }


   /**
    * This method is equivalent to
    *   {@link #formatWithError(int,int,int,double,double,String[]) formatWithError},
    *   except that it formats the given value and error for the
    *   locale <TT>locale</TT>.
    * 
    * @param locale the locale being used
    * 
    *    @param fieldwidth minimum length of the value string
    * 
    *    @param fieldwidtherr minimum length of the error string
    * 
    *    @param precision number of significant digits for the value
    * 
    *    @param x the value to be processed
    * 
    *    @param error the error on the value to be processed
    * 
    *    @param res an array that will be filled with the formatted value and formatted error
    * 
    */
   public static void formatWithError (Locale locale, int fieldwidth,
          int fieldwidtherr, int precision, double x, double error,
          String[] res) {
      int accuracy = getMinAccuracy (error);
      if (!canUseDecimalNotation (fieldwidth, accuracy, precision, x)) {
         int posEntier = (int)Math.floor (Math.log (Math.abs (x)) / Math.log (10) + 1);
         if (posEntier < 0)
            posEntier = 1;
         int newAccuracy = precision - posEntier;
         if (canUseDecimalNotation (fieldwidth, newAccuracy, precision, x))
            accuracy = newAccuracy;
      }
      formatWithError (locale, fieldwidth, fieldwidtherr, accuracy, precision, x, error, res);
   }

}
