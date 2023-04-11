package com.iris.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileCheckSumUtility {

	static final Logger LOGGER = LogManager.getLogger(FileCheckSumUtility.class);

	private static final String CHECKSUM_ALGORITHM = "SHA-1";

	//	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
	//		String filePath = "C:\\Users\\sajadhav\\Desktop\\server.xml";
	//		System.out.println("checksum calculation start: "+new Date());
	//		System.out.println("Checksum == " + calculateFileChecksum(filePath));
	//		System.out.println("checksum calculation end: "+new Date());
	//	}

	public static String calculateFileChecksum(String filePath) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(CHECKSUM_ALGORITHM); // SHA, MD2, MD5, SHA-256, SHA-384...
		return checksum(md, filePath);
	}

	private static String checksum(MessageDigest digest, String filePath) throws IOException {
		File file = new File(filePath);
		// Get file input stream for reading the file content
		StringBuilder sb = new StringBuilder();

		try (FileInputStream fis = new FileInputStream(file);) {
			// Create byte array to read data in chunks
			byte[] byteArray = new byte[1024];
			int bytesCount = 0;
			// Read file data and update in message digest
			while ((bytesCount = fis.read(byteArray)) != -1) {
				digest.update(byteArray, 0, bytesCount);
			}

			// Get the hash's bytes
			byte[] bytes = digest.digest();

			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
		}
		return sb.toString();

	}

}
