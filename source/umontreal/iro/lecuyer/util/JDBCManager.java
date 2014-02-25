
/*
 * Class:        JDBCManager
 * Description:  Interface to access databases
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

import java.io.*;
import java.net.URL;
import java.sql.*;
import javax.sql.DataSource;
import java.util.Properties;
import javax.naming.*;


/**
 * This class provides some facilities to connect to a SQL database and to
 * retrieve data stored in it.
 * JDBC provides a standardized interface for accessing a database
 * independently of a specific database management system (DBMS).
 * The user of JDBC must create a {@link Connection} object
 * used to send SQL queries to the underlying DBMS, but
 * the creation of the connection adds a DBMS-specific portion
 * in the application.
 * This class helps the developer in moving the DBMS-specific
 * information out of the source code by storing it in
 * a properties file.  The methods in this class can
 * read such a properties file and establish the JDBC connection.
 * The connection can be made by using a {@link DataSource} obtained
 * through a JNDI server, or by a JDBC URI associated with a
 * driver class.
 * Therefore, the properties used to connect to the
 * database must be a JNDI name (<TT>jdbc.jndi-name</TT>),
 * or a driver to load
 * (<TT>jdbc.driver</TT>) with the URI of a database (<TT>jdbc.uri</TT>).
 * 
 * <DIV CLASS="vcode" ALIGN="LEFT">
 * <TT>
 * 
 * <BR>&nbsp;&nbsp;&nbsp;jdbc.driver=com.mysql.jdbc.Driver
 * <BR>
 * <BR>&nbsp;&nbsp;&nbsp;jdbc.uri=jdbc:mysql://mysql.iro.umontreal.ca/database?user=foo&amp;password=bar
 * <BR></TT>
 * </DIV>
 * 
 * <P>
 * The connection is established using the
 * {@link #connectToDatabase((Properties)) connectToDatabase}
 * method.  Shortcut methods are also available to read the
 * properties from a file or a resource before establishing the connection.
 * This class also provides shortcut methods to read data from a database
 * and to copy the data into Java arrays.
 * 
 */
public class JDBCManager {


   /**
    * Connects to the database using the properties <TT>prop</TT> and returns the 
    *    an object representing the connection.
    *    The properties stored in <TT>prop</TT> must be a JNDI name
    *     (<TT>jdbc.jndi-name</TT>), or the name of a driver
    *    (<TT>jdbc.driver</TT>) to load and the URI of the database (<TT>jdbc.uri</TT>).
    *    When a JNDI name is given, this method constructs a
    *    context using the nullary constructor of {@link InitialContext},
    *    uses the context to get a {@link DataSource} object,
    *    and uses the data source to obtain a connection.
    *     This method assumes that JNDI is configured correctly;
    *    see the class {@link InitialContext} for more information about configuring
    *    JNDI.
    *    If no JNDI name is specified, the method looks for a JDBC URI.
    *    If a driver class name is specified along with the URI, the corresponding driver
    *    is loaded and registered with the JDBC {@link DriverManager}.
    *    The driver manager is then used to obtain the connection using the URI.
    *  This method throws
    *    an {@link SQLException} if the connection failed and an
    *   {@link IllegalArgumentException}
    *    if the properties do not contain the required values.
    * 
    * @param prop the properties to connect to the database.
    * 
    *    @return the connection to the database.
    *    @exception SQLException if the connection failed.
    * 
    *    @exception IllegalArgumentException if the properties do not contain the require values.
    * 
    * 
    */
   public static Connection connectToDatabase (Properties prop)
            throws SQLException  {
      Connection connection = null;
      String jndiName;
        
      if ((jndiName = prop.getProperty ("jdbc.jndi-name")) != null)
      {
         try
         {
            InitialContext context = new InitialContext();
            connection = ((DataSource) context.lookup (jndiName)).getConnection();
         }
         catch (NamingException e)
         {
            throw new IllegalArgumentException
                  ("The jdbc.jndi-name property refers to the invalid name " + jndiName);
         }
      }
      else
      {
         String driver = prop.getProperty ("jdbc.driver");
         String uri = prop.getProperty ("jdbc.uri");
         if (uri != null)
         {
            if (driver != null) {
               try
               {
                  Class driverClass = Class.forName (driver);
                  if (!Driver.class.isAssignableFrom (driverClass))
                     throw new IllegalArgumentException
                        ("The driver name " + driver +
                     " does not correspond to a class implementing the java.sql.Driver interface");
                  // Needed by some buggy drivers
                  driverClass.newInstance();
               }
               catch (ClassNotFoundException cnfe) {
                  throw new IllegalArgumentException ("Could not find the driver class " + driver);
               }
               catch (IllegalAccessException iae) {
                  throw new IllegalArgumentException
                        ("An illegal access prevented the instantiation of driver class " + driver);
               }
               catch (InstantiationException ie) {
                  throw new IllegalArgumentException
                        ("An instantiation exception prevented the instantiation of driver class " + driver +
                                ": " + ie.getMessage());
               }
            }
            connection = DriverManager.getConnection (uri);
         }
         else
         {
            throw new IllegalArgumentException
                ("The jdbc.driver and jdbc.uri properties must be given if jdbc.jndi-name is not set");
         }            
      }

      return connection;
   }


   /**
    * Returns a connection to the database using the properties read from stream <TT>is</TT>.
    *    This method loads the properties from the given stream, and
    *    calls {@link #connectToDatabase((Properties)) connectToDatabase} to establish the connection.
    * 
    * @param is the stream to read for the properties.
    * 
    *    @return the connection to the database.
    *    @exception SQLException if the connection failed.
    * 
    *    @exception IOException if the stream can not be read correctly.
    * 
    *    @exception IllegalArgumentException if the properties do not contain the require values.
    * 
    * 
    */
   public static Connection connectToDatabase (InputStream is)
            throws IOException, SQLException  {
      Properties prop = new Properties();
        
      prop.load (is);
        
      return connectToDatabase (prop);
   }


   /**
    * Equivalent to {@link #connectToDatabase((InputStream)) connectToDatabase} <TT>(url.openStream())</TT>.
    * 
    */
   public static Connection connectToDatabase (URL url)
            throws IOException, SQLException {
    
      InputStream is = url.openStream();
      try {
         return connectToDatabase (is);
      }
      finally {
         is.close();
      }
   }


   /**
    * Equivalent to {@link #connectToDatabase((InputStream)) connectToDatabase} <TT>(new FileInputStream (file))</TT>.
    * 
    */
   public static Connection connectToDatabase (File file)
            throws IOException, SQLException {
    
      FileInputStream is = new FileInputStream (file);
      try {
         return connectToDatabase (is);
      }
      finally {
         is.close();
      }
   }


   /**
    * Equivalent to {@link #connectToDatabase((InputStream)) connectToDatabase} <TT>(new FileInputStream (fileName))</TT>.
    * 
    */
   public static Connection connectToDatabase (String fileName)
            throws IOException, SQLException {

      FileInputStream is = new FileInputStream (fileName);
      try {
         return connectToDatabase (is);
      }
      finally {
         is.close();
      }
   }

    
   /**
    * Uses {@link #connectToDatabase((InputStream)) connectToDatabase} with the stream obtained from
    *    the resource <TT>resource</TT>.
    *    This method searches the file <TT>resource</TT> on the class path, opens
    *    the first resource found, and extracts properties from it.
    *    It then uses {@link #connectToDatabase((Properties)) connectToDatabase}
    *    to establish the connection.
    * 
    */
   public static Connection connectToDatabaseFromResource (String resource)
            throws IOException, SQLException {
        
      InputStream is = JDBCManager.class.getClassLoader().getResourceAsStream (resource);
      try {
         return connectToDatabase (is);
      }
      finally {
         is.close();
      }
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of double-precision values.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    assumes that the first column of the result set contains double-precision values.
    *    Each row of the result set then becomes an element of an array of double-precision
    *    values which is returned by this method.
    *     This method throws an {@link SQLException} if the query is not valid.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static double[] readDoubleData (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      double[] res = new double[rs.getRow()];
      rs.first();
        
      for (int i = 0; i < res.length; i++)
      {
         res[i] = rs.getDouble (1);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of double-precision values.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readDoubleData((Statement,String)) readDoubleData}, which returns an
    *    array of double-precision values.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static double[] readDoubleData (Connection connection,
                                          String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readDoubleData (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readDoubleData((Statement, String)) readDoubleData}
    *    <TT>(stmt, "SELECT column FROM table")</TT>.
    * 
    */
   public static double[] readDoubleData (Statement stmt, String table,
                                          String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readDoubleData (stmt, query);
    }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readDoubleData((Connection, String)) readDoubleData}
    *    <TT>(connection, "SELECT column FROM table")</TT>.
    * 
    */
   public static double[] readDoubleData (Connection connection,
                                          String table, String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readDoubleData (connection, query);
    }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of integers.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    assumes that the first column of the result set contains integer values.
    *    Each row of the result set then becomes an element of an array of integers
    *     which is returned by this method.
    *     This method throws an {@link SQLException} if the query is not valid.
    *    The given statement <TT>stmt</TT> must not be set up to
    *    produce forward-only result sets.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static int[] readIntData (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      int[] res = new int[rs.getRow()];
      rs.first();
        
      for (int i = 0; i < res.length; i++)
      {
         res[i] = rs.getInt (1);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of integers.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readIntData((Statement,String)) readIntData}, which returns an
    *    array of integers.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static int[] readIntData (Connection connection, String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readIntData (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readIntData((Statement, String)) readIntData}
    *    <TT>(stmt, "SELECT column FROM table")</TT>.
    * 
    */
   public static int[] readIntData (Statement stmt, String table,
                                    String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readIntData (stmt, query);
    }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readIntData((Connection, String)) readIntData}
    *    <TT>(connection, "SELECT column FROM table")</TT>.
    * 
    */
   public static int[] readIntData (Connection connection, String table,
                                    String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readIntData (connection, query);
    }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of objects.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    extracts values from the first column of the obtained result set by
    *    using the <TT>getObject</TT> method.
    *    Each row of the result set then becomes an element of an array of objects
    *     which is returned by this method.
    *     The type of the objects in the array depends on the column type of
    *     the result set, which depends on the database and query.
    *     This method throws an {@link SQLException} if the query is not valid.
    *    The given statement <TT>stmt</TT> must not be set up to
    *    produce forward-only result sets.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static Object[] readObjectData (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      Object[] res = new Object[rs.getRow()];
      rs.first();
        
      for (int i = 0; i < res.length; i++)
      {
         res[i] = rs.getObject (1);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into an array of objects.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readObjectData((Statement,String)) readObjectData}, which returns an
    *    array of integers.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the first column of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static Object[] readObjectData (Connection connection,
                                          String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readObjectData (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readObjectData((Statement, String)) readObjectData}
    *    <TT>(stmt, "SELECT column FROM table")</TT>.
    * 
    */
   public static Object[] readObjectData (Statement stmt,
                                          String table, String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readObjectData (stmt, query);
    }


   /**
    * Returns the values of the column <TT>column</TT> of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readObjectData((Connection, String)) readObjectData}
    *    <TT>(connection, "SELECT column FROM table")</TT>.
    * 
    */
   public static Object[] readObjectData (Connection connection,
                                          String table, String column)
            throws SQLException {
        final String query = "SELECT " + column + " FROM " + table;
        
        return readObjectData (connection, query);
    }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a
    *    rectangular 2D array of double-precision values.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    assumes that the columns of the result set contain double-precision values.
    *    Each row of the result set then becomes a row of a 2D array of double-precision
    *    values which is returned by this method.
    *     This method throws an {@link SQLException} if the query is not valid.
    *    The given statement <TT>stmt</TT> must not be set up to
    *    produce forward-only result sets.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static double[][] readDoubleData2D (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      int c = rs.getMetaData().getColumnCount();
      double[][] res = new double[rs.getRow()][c];
      rs.first();
        
      for (int i = 0; i < res.length; i++) {
         for (int j = 0; j < res[i].length; j++)
            res[i][j] = rs.getDouble (1 + j);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a rectangular 2D array
    *    of double-precision values.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readDoubleData2D((Statement,String)) readDoubleData2D}, which returns a 2D
    *    array of double-precision values.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static double[][] readDoubleData2D (Connection connection,
                                              String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readDoubleData2D (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readDoubleData2D((Statement, String)) readDoubleData2D}
    *    <TT>(stmt, "SELECT * FROM table")</TT>.
    * 
    */
   public static double[][] readDoubleData2DTable (Statement stmt,
                                                   String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readDoubleData2D (stmt, query);
    }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readDoubleData2D((Connection, String)) readDoubleData2D}
    *    <TT>(connection, "SELECT * FROM table")</TT>.
    * 
    */
   public static double[][] readDoubleData2DTable (Connection connection,
                                                   String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readDoubleData2D (connection, query);
    }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a
    *    rectangular 2D array of integers.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    assumes that the columns of the result set contain integers.
    *    Each row of the result set then becomes a row of a 2D array of integers
    *    which is returned by this method.
    *     This method throws an {@link SQLException} if the query is not valid.
    *    The given statement <TT>stmt</TT> must not be set up to
    *    produce forward-only result sets.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static int[][] readIntData2D (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      int c = rs.getMetaData().getColumnCount();
      int[][] res = new int[rs.getRow()][c];
      rs.first();
        
      for (int i = 0; i < res.length; i++) {
         for (int j = 0; j < res[i].length; j++)
            res[i][j] = rs.getInt (1 + j);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a rectangular 2D array
    *    of integers.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readIntData2D((Statement,String)) readIntData2D}, which returns a 2D
    *    array of integers.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static int[][] readIntData2D (Connection connection, String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readIntData2D (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readIntData2D((Statement, String)) readIntData2D}
    *    <TT>(stmt, "SELECT * FROM table")</TT>.
    * 
    */
   public static int[][] readIntData2DTable (Statement stmt, String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readIntData2D (stmt, query);
    }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readIntData2D((Connection, String)) readIntData2D}
    *    <TT>(connection, "SELECT * FROM table")</TT>.
    * 
    */
   public static int[][] readIntData2DTable (Connection connection,
                                             String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readIntData2D (connection, query);
    }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a
    *    rectangular 2D array of objects.
    *    This method uses the statement <TT>stmt</TT> to execute the given query, and
    *    extracts values from the obtained result set by using the <TT>getObject</TT> method.
    *    Each row of the result set then becomes a row of a 2D array of objects
    *    which is returned by this method.
    *     The type of the objects in the 2D array depends on the column types of
    *     the result set, which depend on the database and query.
    *     This method throws an {@link SQLException} if the query is not valid.
    *    The given statement <TT>stmt</TT> must not be set up to
    *    produce forward-only result sets.
    * 
    * @param stmt the statement used to make the query.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static Object[][] readObjectData2D (Statement stmt, String query)
            throws SQLException {
      ResultSet rs = stmt.executeQuery (query);
      rs.last();
      int c = rs.getMetaData().getColumnCount();
      Object[][] res = new Object[rs.getRow()][c];
      rs.first();
        
      for (int i = 0; i < res.length; i++) {
         for (int j = 0; j < res[i].length; j++)
            res[i][j] = rs.getObject (1 + j);
         rs.next();
      }
      rs.close();
            
      return res;
   }


   /**
    * Copies the result of the SQL query <TT>query</TT> into a rectangular 2D array
    *    of integers.
    *    This method uses the active connection <TT>connection</TT> to create
    *    a statement, and passes this statement, with the query, to
    *    {@link #readObjectData2D((Statement,String)) readObjectData2D}, which returns a 2D
    *    array of integers.
    * 
    * @param connection the active connection to the database.
    * 
    *    @param query the SQL query to execute.
    * 
    *    @return the columns of the result set.
    *    @exception SQLException if the query is not valid.
    * 
    * 
    */
   public static Object[][] readObjectData2D (Connection connection,
                                              String query)
            throws SQLException {
      Statement stmt = connection.createStatement
      (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      try {
         return readObjectData2D (stmt, query);
      }
      finally {
         stmt.close();
      }
   }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readObjectData2D((Statement, String)) readObjectData2D}
    *    <TT>(stmt, "SELECT * FROM table")</TT>.
    * 
    */
   public static Object[][] readObjectData2DTable (Statement stmt,
                                                   String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readObjectData2D (stmt, query);
    }


   /**
    * Returns the values of the columns of the table <TT>table</TT>.
    *    This method is equivalent to {@link #readObjectData2D((Connection, String)) readObjectData2D}
    *    <TT>(connection, "SELECT * FROM table")</TT>.
    * 
    */
   public static Object[][] readObjectData2DTable (Connection connection,
                                                   String table)
            throws SQLException {
        final String query = "SELECT * FROM " + table;
        
        return readObjectData2D (connection, query);
    }

}
