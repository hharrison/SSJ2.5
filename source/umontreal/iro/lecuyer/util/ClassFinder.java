
/*
 * Class:        ClassFinder
 * Description:  Convert a simple class name to a fully qualified class object
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



/**
 * Utility class used to convert a simple class name to
 * a fully qualified class object.
 * The {@link java.lang.Class Class} class can be used to obtain
 * information about a class (its name, its fields, methods,
 * constructors, etc.), and to construct objects, even
 * if the exact class is known at runtime only.
 * It provides a {@link java.lang.Class#forName((String)) forName} static method converting
 * a string to a {@link java.lang.Class Class}, but the given string
 * must be a fully qualified name.
 * 
 * <P>
 * Sometimes, configuration files may need
 * to contain Java class names. After they are
 * extracted from the file, these class names are
 * given to {@link java.lang.Class#forName((String)) forName} to be converted into
 * {@link java.lang.Class Class} objects.
 * Unfortunately, only
 * fully qualified class names will be accepted
 * as input, which clutters
 * configuration files, especially if long package names are used.
 * This class permits the definition of a set of
 * import declarations in a way similar to the
 * Java Language Specification.
 * It provides methods to convert a simple class name
 * to a {@link java.lang.Class Class} object and to generate a simple name
 * from a {@link java.lang.Class Class} object, based on the import
 * rules.
 * 
 * <P>
 * The first step for using a class finder is to construct an instance of
 * this class.
 * Then, one needs to retrieve the initially empty list of import
 * declarations by
 * using {@link #getImports getImports}, and update it with the
 * actual import declarations.
 * Then, the method {@link #findClass findClass} can find a class using the
 * import declarations.
 * For example, the following code retrieves the class object for the
 * <TT>List</TT> class in package <TT>java.util</TT>
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * ClassFinder cf = new ClassFinder();
 * <BR>&nbsp;&nbsp;&nbsp;cf.getImports().add (&#34;java.util.*&#34;);
 * <BR>&nbsp;&nbsp;&nbsp;Class&lt;?&gt; listClass = cf.findClass (&#34;List&#34;);
 * <BR></TT>
 * </DIV>
 * 
 */
public class ClassFinder implements Cloneable, java.io.Serializable {
   private static final long serialVersionUID = -4847630831331065792L;


   /**
    * Contains the saved import lists.
    *  Each element of this list is a nested {@link java.util.List List} containing
    *  {@link java.lang.String String}'s, each string containing the fully qualified
    *  name of an imported package or class.
    * 
    * 
    */
   private List<List<String>> imports = new LinkedList<List<String>>();



   /**
    * Constructs a new class finder with
    *  an empty list of import declarations.
    * 
    */
   public ClassFinder() {
      List<String> imp = new ArrayList<String>();
      imports.add (imp);
   }


   /**
    * Returns the current list of import declarations.
    *  This list may contain only {@link java.lang.String String}'s
    *  of the form <TT>java.class.name</TT> or
    *  <TT>java.package.name.*</TT>.
    * 
    * @return the current list of import declarations.
    * 
    */
   public List<String> getImports() {
      return imports.get (imports.size() - 1);
   }


   /**
    * Saves the current import list on the import stack.
    *  This method makes a copy of the list returned
    *  by {@link #getImports(()) getImports} and puts it on top
    *  of a stack to be restored later by
    *  {@link #restoreImports(()) restoreImports}.
    * 
    */
   public void saveImports() {
      List<String> imp = getImports();
      List<String> impBack = new ArrayList<String> (imp);
      imports.add (impBack);
   }


   /**
    * Restores the list of import declarations.
    *  This method removes the last list of import
    *  declarations from the stack. If the stack
    *  contains only one list, this list is cleared.
    * 
    */
   public void restoreImports() {
      if (imports.size() == 1)
         getImports().clear();
      else
         imports.remove (imports.size() - 1);
   }


   /**
    * Tries to find the class corresponding to the simple
    *  name <TT>name</TT>.  The method first considers
    *  the argument as a fully qualified class name
    *  and calls {@link java.lang.Class#forName((String)) forName} <TT>(name)</TT>.
    *  If the class cannot be found, it considers the argument
    *  as a simple name.  A simple name refers to a class without
    *  specifying the package declaring it.  To convert
    *  simple names to qualified names, the method iterates through
    *  all the strings in the list returned by {@link #getImports(()) getImports},
    *  applying
    *  the same rules as a Java compiler to resolve
    *  the class name.  However, if an imported package or
    *  class does not exist, it will be ignored whereas
    *  the compiler would stop with an error.
    * 
    * <P>
    * For the class with simple name <TT>name</TT> to
    *  be loaded, it must be imported explicitly (single-type import) or
    *  one of the imported packages must contain it (type import on-demand).
    *  If the class with name <TT>name</TT> is imported explicitly,
    *  this import declaration has precedence over
    *  any imported packages.
    *  If several import declaration match the given simple
    *  name, e.g., if several fully qualified names with the same
    *  simple name are imported, or if a class with simple
    *  name <TT>name</TT> exists in several packages,
    *  a {@link NameConflictException} is thrown.
    * 
    * @param name the simple name of the class.
    * 
    *    @return a reference to the class being loaded.
    *    @exception ClassNotFoundException if the class
    *     cannot be loaded.
    * 
    *    @exception NameConflictException if a name conflict occurred.
    * 
    * 
    */
   public Class<?> findClass (String name) throws
      ClassNotFoundException, NameConflictException {
      // Try to consider the name as a fully qualified class name
      try {
         return Class.forName (name);
      }
      catch (ClassNotFoundException cnfe) {}

      List<String> imports = getImports();
      Class<?> candidate = null;
      String candidateImportString = "";
      boolean candidateImportOnDemand = false;

      // Determines the name of the outermost class
      // if name corresponds to the simple name of a nested class, e.g., for A.B.C
      // outerName will contain A.
      int idxOut = name.indexOf ('.');
      String outerName;
      if (idxOut == -1)
         outerName = name;
      else
         outerName = name.substring (0, idxOut);
      for (String importString : imports) {
         // For each import declaration, we try to load
         // a class and store the result in cl.
         // When cl is not null, the Class object is
         // compared with the best candidate candidate.
         Class<?> cl = null;
         boolean onDemand = false;
         if (!importString.endsWith (".*")) {
            // Single-type import declaration
            // We must ensure that this will correspond to
            // the desired class name. For example, if we
            // search List and the import java.util.ArrayList
            // is found in importString, the ArrayList
            // class must not be returned.
            if (importString.endsWith ("." + outerName)) {
               // The name of outer class was found in importString and
               // a period is found left to it.
               // So try to load this class.
               // Simple class names have precedence over
               // on-demand names.

               // Replace, in importString, the name of
               // the outer class with the true class name
               // we want to load.  If the simple name
               // does not refer to a nested class, this
               // has no effect.
               String cn = importString.substring
                  (0, importString.length() - outerName.length()) + name;
               try {
                  cl = Class.forName (cn);
               }
               catch (ClassNotFoundException cnfe) {}
            }
         }
         else {
            // Type import on demand declaration
            try {
               // Replace the * with name and
               // try to load the class.
               // If that succeeds, our candidate cl
               // is onDemand.
               cl = Class.forName
                  (importString.substring (0, importString.length() - 1) + name);
               onDemand = true;
            }
            catch (ClassNotFoundException cnfe) {}
         }
         if (cl != null) {
            // Something was loaded
            if (candidate == null ||
                (candidateImportOnDemand && !onDemand)) {
               // We had no candidate or the candidate was imported
               // on-demand while this one is a single-type import.
               candidate = cl;
               candidateImportString = importString;
               candidateImportOnDemand = onDemand;
            }
            else if (candidate != cl)
               throw new NameConflictException
                  (this, name,
                   "simple class name " + name +
                   " matches " + candidate.getName() +
                   " (import string " + candidateImportString + ") or " +
                   cl.getName() + " (import string " + importString + ")");
         }
      }
      if (candidate == null)
         throw new ClassNotFoundException
            ("Cannot find the class with name " + name);
      return candidate;
   }


   /**
    * Returns the simple name of the class <TT>cls</TT> that
    *  can be used when the imports contained
    *  in this class finder are used.
    *  For example, if <TT>java.lang.String.class</TT> is given
    *  to this method, <TT>String</TT> is returned if
    *  <TT>java.lang.*</TT> is among the import declarations.
    * 
    * <P>
    * Note: this method does not try to find name conflicts.
    *  This operation is performed by {@link #findClass((String)) findClass} only.
    *  For example, if the list of imported declarations
    *  contains <TT>foo.bar.*</TT> and <TT>test.Foo</TT>, and
    *  the simple name for <TT>test.Foo</TT> is queried,
    *  the method returns <TT>Foo</TT> even if the package
    *  <TT>foo.bar</TT> contains a <TT>Foo</TT> class.
    * 
    * @param cls the class for which the simple name is queried.
    * 
    *    @return the simple class name.
    * 
    */
   public String getSimpleName (Class<?> cls) {
      if (cls.isArray())
         return getSimpleName (cls.getComponentType()) + "[]";
      if (cls.isPrimitive())
         return cls.getName();
      Class<?> outer = cls;
      while (outer.getDeclaringClass() != null)
         outer = outer.getDeclaringClass();
      boolean needsFullyQualified = true;
      for (String importString : getImports()) {
         if (importString.equals (outer.getName()))
             // A single-type import is given, can return an unqualified name.
            needsFullyQualified = false;
         else if (importString.endsWith (".*")) {
            // Remove the .* at the end of the import string to get a package name.
            String pack = importString.substring (0, importString.length() - 2);
            // Compare this package name.
            if (pack.equals (cls.getPackage().getName()))
               needsFullyQualified = false;
         }
      }
      if (needsFullyQualified)
         return cls.getName();
      else {
         String name = cls.getName();
         String pack = cls.getPackage().getName();
         if (!name.startsWith (pack))
            throw new IllegalStateException
               ("The class name " + name +
                " does not contain the package name " + pack);

         // Removes the package and the . from the fully qualified class name.
         return name.substring (pack.length() + 1);
      }         
   }


   /**
    * Clones this class finder, and copies its lists of
    *   import declarations.
    * 
    */
   public ClassFinder clone() {
      ClassFinder cf;
      try {
         cf = (ClassFinder)super.clone();
      }
      catch (CloneNotSupportedException cne) {
         throw new InternalError
            ("CloneNotSupported thrown for a class implementing Cloneable");
      }
      cf.imports = new LinkedList<List<String>>();
      for (List<String> imp : imports) {
         List<String> impCpy = new ArrayList<String> (imp);
         cf.imports.add (impCpy);
      }
      return cf;
   }
}
