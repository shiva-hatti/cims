package com.iris.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.iris.exception.PropertiesConfigException;

public class PropertiesBundle {

	private static Map<String, PropertiesBundle> bundles = new HashMap<String, PropertiesBundle>();
	private PropertiesConfiguration config;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(PropertiesBundle.class);
	private PropertiesBundle(String fileName) throws PropertiesConfigException {
		try {
			config = new PropertiesConfiguration(fileName);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			throw new PropertiesConfigException(e);
		}
	}

	/**
	 * Gets a property bundle using the specified file name <br>
	 * fileName should be with file extension. e.g. LDAPConfig.properties
	 * 
	 * @param fileName a fully qualified class name
	 * @return PropertiesBundle
	 * @throws PropertiesConfigException
	 */
	public static PropertiesBundle load(String fileName) throws PropertiesConfigException {
		PropertiesBundle bundle = bundles.get(fileName);
		if (bundle == null) {
			bundle = new PropertiesBundle(fileName);
			bundles.put(fileName, bundle);
			return bundle;
		} else {
			return bundle;
		}
	}

	public String getProperty(String key) {
		return config.getString(key, key);
	}
}
