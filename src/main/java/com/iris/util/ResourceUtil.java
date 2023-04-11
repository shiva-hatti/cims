package com.iris.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceUtil {

	final static Logger logger = LogManager.getLogger(ResourceUtil.class);
	private static Properties prop = null;

	private ResourceUtil() {
	}

	public static Properties getResourcePropertyFile() {
		if (prop != null) {
			return prop;
		} else {
			prop = new Properties();
			InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream("resource.properties");
			// load a properties file
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				logger.error("Exception : ", e);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Exception : ", e);
				}
			}
			return prop;
		}
	}

	public static String getKeyValue(String key) {
		if (prop != null) {
			return prop.getProperty(key);
		} else {
			prop = new Properties();
			InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream("resource.properties");
			try {
				prop.load(inputStream);
				return prop.getProperty(key);
			} catch (IOException e) {
				logger.error("Exception : ", e);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Exception : ", e);
				}
			}
			return prop.getProperty(key);
		}
	}
}