package org.texttechnologylab.project.sentiment_radar.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
  private static Properties loadProperties() throws IOException {
    String appConfigPath = Thread.currentThread().getContextClassLoader().getResource("app.properties").getPath();
    Properties appProps = new Properties();
    appProps.load(new FileInputStream(appConfigPath));
    return appProps;
  }

  public static String getRemoteHost() {
    try {
      return loadProperties().getProperty("remote_host");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteDatabase() {
    try {
      return loadProperties().getProperty("remote_database");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteUser() {
    try {
      return loadProperties().getProperty("remote_user");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemotePassword() {
    try {
      return loadProperties().getProperty("remote_password");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemotePort() {
    try {
      return loadProperties().getProperty("remote_port");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getRemoteCollection() {
    try {
      return loadProperties().getProperty("remote_collection");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
