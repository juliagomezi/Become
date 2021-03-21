package com.pes.become.backend.persistence;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class PostgreSQLExample {

    public static void main(String args) {

        try
        {
            PostgreSQLExample postgreSQLExample = new PostgreSQLExample();

            Connection conn = postgreSQLExample.getPostgreSQLConnection();

            /* You can use the connection object to do any insert, delete, query or update action to the mysql server.*/

            /* Do not forget to close the database connection after use, this can release the database connection.*/
            conn.close();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /* This method return java.sql.Connection object. */
    public Connection getPostgreSQLConnection()
    {
        /* Declare and initialize a sql Connection variable. */
        Connection ret = null;

        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            /* Register jdbc driver class. */
            Class.forName("org.postgresql.Driver");

            /* Create connection url. */
            String mysqlConnUrl = "jdbc:postgresql://b1wwof5edrdzatsbzct8-" +
                    "postgresql.services.clever-cloud.com:5432/b1wwof5edrdzatsbzct8";

            /* user name. */
            String mysqlUserName = "u73b0iot0g3vscseq85a";

            /* password. */
            String mysqlPassword = "E5MvIEB4aEUae2JmDn5p";

            /* Get the Connection object. */
            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName , mysqlPassword);

            /* Get related meta data for this mysql server to verify db connect successfully.. */
            DatabaseMetaData dbmd = ret.getMetaData();

            String dbName = dbmd.getDatabaseProductName();

            String dbVersion = dbmd.getDatabaseProductVersion();

            String dbUrl = dbmd.getURL();

            String userName = dbmd.getUserName();

            String driverName = dbmd.getDriverName();

            System.out.println("Database Name is " + dbName);

            System.out.println("Database Version is " + dbVersion);

            System.out.println("Database Connection Url is " + dbUrl);

            System.out.println("Database User Name is " + userName);

            System.out.println("Database Driver Name is " + driverName);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            return ret;
        }
    }

}