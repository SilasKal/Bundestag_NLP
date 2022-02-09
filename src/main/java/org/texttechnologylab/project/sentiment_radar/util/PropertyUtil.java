package org.texttechnologylab.project.sentiment_radar.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
  /**
   * This class contains functions to load properties for a mongoDB connection.
   * @author Philipp Daechert
   * @throws IOException
   */
  private static Properties loadProperties() throws IOException {
    /**
     * Function to load an app.properties file to extract connection information.
     */
    Properties appProps = new Properties();
    appProps.load(PropertyUtil.class.getClassLoader().getResourceAsStream("app.properties"));
    return appProps;
  }

  public static String getRemoteHost() {
    /**
     * Function to get the "remote_host" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_host");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteDatabase() {
    /**
     * Function to get the "remote_database" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_database");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteUser() {
    /**
     * Function to get the "remote_user" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_user");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemotePassword() {
    /**
     * Function to get the "remote_password" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_password");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemotePort() {
    /**
     * Function to get the "remote_port" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_port");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteCollection() {
    /**
     * Function to get the "remote_collection" information of the Property Class.
     * @throws IOException
     */
    try {
      return loadProperties().getProperty("remote_collection");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
