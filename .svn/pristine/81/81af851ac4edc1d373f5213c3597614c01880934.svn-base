/**
 * 
 */
package com.iris.sdmx.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.io.Files;

/**
 * @author apagaria
 *
 */
public class SdmxUtil {

	/**
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void copyFile(String from, String to) throws IOException {
		Path src = Paths.get(from);
		Path dest = Paths.get(to);
		Files.copy(src.toFile(), dest.toFile());
	}

	public String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		return format.format(date);
	}
}
