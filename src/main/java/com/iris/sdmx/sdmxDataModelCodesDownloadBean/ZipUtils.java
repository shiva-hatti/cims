package com.iris.sdmx.sdmxDataModelCodesDownloadBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipUtils {

	private static final Logger LOGGER = LogManager.getLogger(ZipUtils.class);

	/**
	 * Zip it
	 * 
	 * @param zipFile output ZIP file location
	 */
	public void zipIt(String zipFile, List<String> fileList) {

		FileOutputStream fileOutStream = null;
		ZipOutputStream zipOutStream = null;
		FileInputStream fileInputStream = null;
		try {
			fileOutStream = new FileOutputStream(zipFile);
			zipOutStream = new ZipOutputStream(fileOutStream);

			byte[] buffer = null;
			ZipEntry ze = null;
			File file = null;
			for (String filName : fileList) {
				buffer = new byte[1024];
				file = new File(filName);
				ze = new ZipEntry(file.getName());
				zipOutStream.putNextEntry(ze);
				fileInputStream = new FileInputStream(filName);
				int len;
				while ((len = fileInputStream.read(buffer)) > 0) {
					zipOutStream.write(buffer, 0, len);
				}
				buffer = null;
				if (null != fileInputStream) {
					fileInputStream.close();
				}
			}

			if (null != zipOutStream) {
				zipOutStream.closeEntry();
				zipOutStream.close();
				zipOutStream = null;
			}

			if (null != fileOutStream) {
				fileOutStream.close();
				fileOutStream = null;
			}

		} catch (IOException e) {
			LOGGER.error("Exception : ", e);
		} finally {
			try {
				if (null != fileInputStream) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
			try {
				if (null != zipOutStream) {
					zipOutStream.close();
					zipOutStream.closeEntry();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
			try {
				if (null != fileOutStream) {
					fileOutStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
		}
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile input zip file
	 * @param output  zip file output folder
	 */
	public void unZipIt(String zipFile, String outputFolder) {
		FileOutputStream fileOutStream = null;
		ZipInputStream zipInputStream = null;
		FileInputStream fileInputStream = null;
		try {
			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			// get the zip file content

			fileInputStream = new FileInputStream(zipFile);

			zipInputStream = new ZipInputStream(fileInputStream);

			// get the zipped file list entry
			ZipEntry ze = zipInputStream.getNextEntry();

			byte[] buffer = null;
			File newFile = null;
			String fileName = null;
			while (ze != null) {
				fileName = ze.getName();
				newFile = new File(outputFolder + File.separator + fileName);
				buffer = new byte[1024];
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
				fileOutStream = new FileOutputStream(newFile);
				int len;
				while ((len = zipInputStream.read(buffer)) > 0) {
					fileOutStream.write(buffer, 0, len);
				}

				if (null != fileOutStream) {
					fileOutStream.close();
				}

				ze = zipInputStream.getNextEntry();
				buffer = null;
			}
		} catch (IOException e) {
			LOGGER.error("Exception : ", e);
		} finally {
			try {
				if (zipInputStream != null) {
					zipInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
			try {
				if (null != fileInputStream) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
			try {
				if (null != fileOutStream) {
					fileOutStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
			try {
				if (null != fileOutStream) {
					fileOutStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Exception : ", e);
			}
		}
	}

	//	public static void main(String[] args) {
	//		ZipUtils zipFile = new ZipUtils();
	//		List<String> fileList = new ArrayList<>();
	//		fileList.add("C:\\iFileProd\\VALIDATE_META\\CIMA210331R13901Q.txt");
	//		fileList.add("C:\\iFileProd\\VALIDATE_META\\HDFC210331R15235Q.xml");
	//		fileList.add("C:\\iFileProd\\VALIDATE_META\\HDFC180930R05806Q.xml");
	//		
	//		zipFile.zipIt("C:\\iFileProd\\VALIDATE_META\\test.zip", fileList);
	//	}
}