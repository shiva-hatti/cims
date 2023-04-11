package com.iris.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.util.constant.ErrorConstants;

/**
 * This class would handle various file related operations
 * 
 * @author Suman Kumar
 * @version 1.0
 */
public class FileManager implements Serializable {

	private static final long serialVersionUID = 5656832078140522319L;

	private static final Logger LOGGER = LogManager.getLogger(FileManager.class);

	/**
	 * This method would clean the given directory without deleting it.
	 * 
	 * @param directory The directory to clean
	 * @return true if successful, else false
	 */
	public static boolean cleanDirectory(File directory) {
		try {
			FileUtils.cleanDirectory(directory);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would check if a directory exist or not
	 * 
	 * @param directory The directory to create
	 * @return true if successful, else false
	 */
	public static boolean checkDirExistence(File directory) {
		try {
			if (directory.exists()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would create the directory along with any non-existence of parent directory
	 * 
	 * @param directory The directory to be created
	 * @return true if successful, else false
	 */
	public static boolean makeDirWithParentDir(File directory) {
		try {
			FileUtils.forceMkdir(directory);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would check two files if they are equal using the defined char set name
	 * 
	 * @param file1       File one to check
	 * @param file2       File two to check
	 * @param charsetName The character encoding to be used. May be null, in which case the platform default is used
	 * @return true if given files are equal, else false
	 */
	public static boolean contentEquals(File file1, File file2, String charsetName) {
		try {
			return FileUtils.contentEqualsIgnoreEOL(file1, file2, charsetName);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would check two files if they are equal
	 * 
	 * @param file1 File one to check
	 * @param file2 File two to check
	 * @return true if given files are equal, else false
	 */
	public static boolean contentEquals(File file1, File file2) {
		try {
			return FileUtils.contentEquals(file1, file2);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would convert the collection of files to File Array
	 * 
	 * @param files The collection of files
	 * @return File array
	 */
	public static File[] convertFileCollectionToFileArray(Collection<File> files) {
		try {
			return FileUtils.convertFileCollectionToFileArray(files);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return null;
		}
	}

	/**
	 * This method would copy one directory to another location
	 * 
	 * @param srcDir  The source directory to copy
	 * @param destDir The destination directory
	 * @return true if success, else false
	 */
	public static boolean copyDirectory(File srcDir, File destDir) {
		try {
			FileUtils.copyDirectory(srcDir, destDir);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy one directory to another location with date of the file as preserved
	 * 
	 * @param srcDir           The source directory to copy
	 * @param destDir          The destination directory
	 * @param preserveFileDate true if date of the file is to be preserved, else false
	 * @return true if success, else false
	 */
	public static boolean copyDirectory(File srcDir, File destDir, boolean preserveFileDate) {
		try {
			FileUtils.copyDirectory(srcDir, destDir, preserveFileDate);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy file from one location to another
	 * 
	 * @param srcFile  The source file to copy
	 * @param destFile The destination of the copy file
	 * @return true if success, else false
	 */
	public boolean copyFile(File srcFile, File destFile) {
		try {
			FileUtils.copyFile(srcFile, destFile);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy file from one location to another preserving the file date
	 * 
	 * @param srcFile          The source file to copy
	 * @param destFile         The destination of the copy file
	 * @param preserveFileDate true if date of the file is to be preserved, else false
	 * @return true if success, else false
	 */
	public static boolean copyFile(File srcFile, File destFile, boolean preserveFileDate) {
		try {
			FileUtils.copyFile(srcFile, destFile, preserveFileDate);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy file from one location to another directory
	 * 
	 * @param srcFile The source file
	 * @param destDir The destination directory
	 * @return true if success, else false
	 */
	public static boolean copyFileToDirectory(File srcFile, File destDir) {
		try {
			FileUtils.copyFileToDirectory(srcFile, destDir);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy file from one location to another directory preserving the file date
	 * 
	 * @param srcFile          The source file
	 * @param destDir          The destination directory
	 * @param preserveFileDate true if date of the file is to be preserved, else false
	 * @return true if success, else false
	 */
	public static boolean copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) {
		try {
			FileUtils.copyFileToDirectory(srcFile, destDir, preserveFileDate);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would copy the content of the URL to the destination file
	 * 
	 * @param source      The source URL
	 * @param destination The destination URL
	 * @return true if success, else false
	 */
	public static boolean copyURLToFile(URL source, File destination) {
		try {
			FileUtils.copyURLToFile(source, destination);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would delete the directory
	 * 
	 * @param directory The directory to be deleted
	 * @return true if success, else false
	 */
	public static boolean deleteDirectory(File directory) {
		try {
			FileUtils.deleteDirectory(directory);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would check if the directory contains the given file
	 * 
	 * @param directory The directory to check
	 * @param child     The file to check in the directory
	 * @return true if success, else false
	 */
	public static boolean directoryContains(File directory, File child) {
		try {
			return FileUtils.directoryContains(directory, child);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would forcefully delete the file
	 * 
	 * @param file The file to delete
	 * @return true if success, else false
	 */
	public static boolean forceDelete(File file) {
		try {
			FileUtils.forceDelete(file);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would move the source directory to destination directory
	 * 
	 * @param srcDir  The source directory
	 * @param destDir The destination directory
	 * @return true if success, else false
	 */
	public static boolean moveDirectory(File srcDir, File destDir) {
		try {
			FileUtils.moveDirectory(srcDir, destDir);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would move the directory to another directory and create if directory not present
	 * 
	 * @param src           The source directory
	 * @param destDir       The destination directory
	 * @param createDestDir true if destination directory is to be created, else false
	 * @return true if success, else false
	 */
	public static boolean moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) {
		try {
			FileUtils.moveDirectoryToDirectory(src, destDir, createDestDir);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would move the file from one location to another
	 * 
	 * @param srcFile  The source file
	 * @param destFile The destination file
	 * @return true if success, else false
	 */
	public static boolean moveFile(File srcFile, File destFile) {
		try {
			FileUtils.moveFile(srcFile, destFile);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would move the file to another directory
	 * 
	 * @param srcFile       The source file
	 * @param destDir       The destination directory
	 * @param createDestDir true if destination directory is to be created, else false
	 * @return true if success, else false
	 */
	public static boolean moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) {
		try {
			FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would read the file in byte array format
	 * 
	 * @param file The file to be read
	 * @return File content in byte array fromat
	 */
	public static byte[] readFileToByteArray(File file) {
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return null;
		}
	}

	/**
	 * This method would read the file in string format
	 * 
	 * @param file The file to be read
	 * @return File content in string format
	 */
	//	public static String readFileToString(File file) {
	//		try {
	//			return FileUtils.readFileToString(file, null);
	//		} catch (Exception e) {
	//			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
	//			return null;
	//		}
	//	}

	/**
	 * This method would read the file in string format based upon the encoding provided
	 * 
	 * @param file     The file to be read
	 * @param encoding The encoding to be used, null means platform default
	 * @return File content in string format
	 */
	public static String readFileToString(File file, String encoding) {
		try {
			return FileUtils.readFileToString(file, encoding);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return null;
		}
	}

	/**
	 * This method would read file line by line
	 * 
	 * @param file The file to be read
	 * @return File content in list of string format
	 */
	//	public static List<String> readLines(File file) {
	//		try {
	//			return FileUtils.readLines(file, null);
	//		} catch (Exception e) {
	//			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
	//			return null;
	//		}
	//	}

	/**
	 * This method would read file line by line based upon the encoding provided
	 * 
	 * @param file     The file to be read
	 * @param encoding The encoding to be used, null means platform default
	 * @return File content in list of string format
	 */
	public static List<String> readLines(File file, String encoding) {
		try {
			return FileUtils.readLines(file, encoding);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return null;
		}
	}

	/**
	 * This method would get the size of the file
	 * 
	 * @param file The file whose size is to be get
	 * @return The size of the file
	 */
	public static long sizeOf(File file) {
		try {
			return FileUtils.sizeOf(file);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return 0;
		}
	}

	/**
	 * This method would get the size of the directory
	 * 
	 * @param directory The directory whose size is to be get
	 * @return The size of the file
	 */
	public static long sizeOfDirectory(File directory) {
		try {
			return FileUtils.sizeOfDirectory(directory);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return 0;
		}
	}

	/**
	 * This method would get the Mime Type Of the File
	 * 
	 * @param directory The actual File
	 * @return Mime Type of file
	 */
	//	public static String mimeTypeOfFile(File file) {
	//		try {
	//			InputStream is = FileUtils.openInputStream(file);
	//			BufferedInputStream bis = new BufferedInputStream(is);
	//			AutoDetectParser parser = new AutoDetectParser();
	//			Detector detector = parser.getDetector();
	//			Metadata md = new Metadata();
	//			md.add(Metadata.RESOURCE_NAME_KEY, file.getName());
	//			MediaType mediaType = detector.detect(bis, md);
	//			return mediaType.toString();
	//		} catch (IOException e) {
	//			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
	//			return null;
	//		}
	//	}

	/**
	 * This method would write the string format of data in the file using provided encoding
	 * 
	 * @param file     The file to be created
	 * @param data     The content to be written
	 * @param encoding The encoding to be used, null means platform default
	 * @return true means success, else false
	 */
	public static boolean writeStringToFile(File file, String data, String encoding) {
		try {
			FileUtils.writeStringToFile(file, data, encoding);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	/**
	 * This method would append the string format of data in the file using provided encoding
	 * 
	 * @param file     The file to be created
	 * @param data     The content to be written
	 * @param encoding The encoding to be used, null means platform default
	 * @param append   if true, then the String will be added to the end of the file rather than overwriting
	 * @return true if success, else false
	 */
	public static boolean writeStringToFile(File file, String data, String encoding, boolean append) {
		try {
			FileUtils.writeStringToFile(file, data, encoding, append);
			return true;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
	}

	public static boolean writeByteArrayToFile(File file, byte[] data) {
		try {
			FileUtils.writeByteArrayToFile(file, data);
		} catch (IOException e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
			return false;
		}
		return true;
	}

}