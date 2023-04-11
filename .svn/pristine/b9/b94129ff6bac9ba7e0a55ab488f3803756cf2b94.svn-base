/**
 * 
 */
package com.iris.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.reflect.TypeToken;
import com.iris.controller.FileMetaDataValidateController;
import com.iris.sdmx.bean.DataSet;

/**
 * @author sajadhav
 *
 */
public class JSONValidate {

	private static final Logger LOGGER = LogManager.getLogger(JSONValidate.class);

	public boolean isValidEBRJsonDocument(String filePath) throws IOException {
		try {
			File csvFile = new File(filePath);
			StringBuilder stringBuilder = new StringBuilder();
			try (InputStream csvFileInputStream = new FileInputStream(csvFile); BufferedReader br = new BufferedReader(new InputStreamReader(csvFileInputStream));) {
				br.lines().forEach(line -> stringBuilder.append(line));
			} catch (Exception e) {
				LOGGER.error(e);
			}

			Type listToken = new TypeToken<List<DataSet>>() {
			}.getType();

			JsonUtility.getGsonObject().fromJson(stringBuilder.toString(), listToken);

			return true;
		} catch (Exception e) {
			LOGGER.error(e);
			return false;
		}
	}

}
