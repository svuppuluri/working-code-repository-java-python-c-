package com.fau.csphd.datasecurity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Assignment2 {
	private SecretKey aesKey;
	private Cipher cipher;
	private String AES_KEY_SALT = "12345678";
	private final int DATA_LENGTH = 128;
	private IvParameterSpec ivParamSpec;
	private String keyword = "";
	private String aesOperationMode = "";

	public Assignment2(String mode, String keyword) throws Exception {
		super();
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		this.ivParamSpec = new IvParameterSpec(iv);
		this.keyword = keyword;

		this.aesOperationMode = mode;
		this.setSecretKey(this.getKeyFromPassword(this.keyword, this.AES_KEY_SALT));
		System.out.println("Initializing using algorithm: " + this.aesOperationMode);
		this.cipher = Cipher.getInstance(this.aesOperationMode);
	}

	public void encryptFile(String iFile, String oFile) throws Exception {

		// Cipher cipher = Cipher.getInstance(this.aesOperationMode);
		// cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ivParamSpec);
		FileInputStream inputStream = new FileInputStream(iFile);
		FileOutputStream outputStream = new FileOutputStream(oFile);

		byte[] bufInput = new byte[64];

		int bytesRead;
		while ((bytesRead = inputStream.read(bufInput)) != -1) {
			System.out.println("Bytes read: " + bytesRead);
			byte[] bufOutput = cipher.update(bufInput, 0, bytesRead);
			System.out.println("cipher updated length: " + bufOutput.length);
			// byte[] oBuffer = new byte[bytesRead];
			// System.arraycopy(iBuffer, 0, oBuffer, 0, bytesRead);
			if (bufOutput != null) {
				outputStream.write(bufOutput);
			}
		}
		byte[] outputBytes = cipher.doFinal();
		System.out.println("cipher final length: " + outputBytes.length);
		if (outputBytes != null) {
			outputStream.write(outputBytes);
		}
		inputStream.close();
		outputStream.close();
	}

	public byte[] encrypt(byte[] plainText) throws Exception {

		System.out.println("Encrypting using algorithm: " + this.aesOperationMode);
		switch (this.aesOperationMode) {
		case "AES/ECB/PKCS5Padding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
			break;
		case "AES/CBC/PKCS5Padding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/CFB/NoPadding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/OFB/NoPadding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/CTR/NoPadding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/GCM/NoPadding":
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
			break;
		default:
			this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		}
		byte[] cipherText = this.cipher.doFinal(plainText);
		return cipherText;
	}

	public byte[] decrypt(byte[] cipherText) throws Exception {

		System.out.println("Decrypting using algorithm: " + this.aesOperationMode);
		switch (this.aesOperationMode) {
		case "AES/ECB/PKCS5Padding":
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey);
			break;
		case "AES/CBC/PKCS5Padding":
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/CFB/NoPadding":
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/OFB/NoPadding":
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/CTR/NoPadding":
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		case "AES/GCM/NoPadding":
			GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, this.cipher.getIV());
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, spec);
			break;
		default:
			this.cipher.init(Cipher.DECRYPT_MODE, this.aesKey, this.ivParamSpec);
			break;
		}
		byte[] plainText = this.cipher.doFinal(cipherText);
		return plainText;
	}

	public String processFile(String iFile, int operation) throws Exception {
		byte[] bufOutput;
		String oFile = "";
		// read input plain text file
		byte[] bufInput = Files.readAllBytes(Paths.get(iFile));
		if (operation == 1) {
			oFile = iFile + ".enc";
			bufOutput = this.encrypt(bufInput);}
		else {
			oFile = iFile + ".dec";
			bufOutput = this.decrypt(bufInput);}
		Path path = Paths.get(oFile);
		Files.write(path, bufOutput);
		return(oFile);
	}

	public void decryptFile(String iFile, String oFile) throws Exception {

		// Cipher cipher = Cipher.getInstance(this.aesOperationMode);
		// cipher.init(Cipher.DECRYPT_MODE, this.aeskey, this.ivParamSpec);
		FileInputStream inputStream = new FileInputStream(iFile);
		FileOutputStream outputStream = new FileOutputStream(oFile);

		byte[] bufInput = new byte[64];

		int bytesRead;
		while ((bytesRead = inputStream.read(bufInput)) != -1) {
			byte[] bufOutput = cipher.update(bufInput, 0, bytesRead);
			if (bufOutput != null) {
				outputStream.write(bufOutput);
			}
		}
		byte[] outputBytes = cipher.doFinal();
		if (outputBytes != null) {
			outputStream.write(outputBytes);
		}
		inputStream.close();
		outputStream.close();
	}

	private void setSecretKey(SecretKey key) {
		this.aesKey = key;
	}

	public SecretKey getKeyFromPassword(String password, String salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;
	}

	public static void main(String args[]) throws Exception {
		int iOperation = -1;
		String sOperation = "";
		String inputFilename = "";
		String outputFilename = "";
		int iOperationMode = -1;
		String sOperationMode = "";
		String strKeyword = "";
		int iKeyLength = -1;
		String strPlainText = "";
		String strCipherText = "";
		int iInputType = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out
					.println("Select AES key length:\r\n" + "1. 128 bits\r\n" + "2. 192 bits\r\n" + "3. 256 bits\r\n");
			int iLength = Integer.parseInt(br.readLine());
			iKeyLength = iLength == 1 ? 128 : (iLength == 2 ? 192 : 256);
			if (iKeyLength == 128 || iKeyLength == 192 || iKeyLength == 256)
				break;
		}
		System.out.println("Input keyword for AES key generation: ");
		br = new BufferedReader(new InputStreamReader(System.in));
		strKeyword = br.readLine();
		while (true) {
			System.out.println("Select AES operation mode:\r\n1. ECB (Electronic Code Book)\r\n"
					+ "2. CBC (Cipher Block Chaining)\r\n" + "3. CFB (Cipher FeedBack)\r\n"
					+ "4. OFB (Output FeedBack)\r\n" + "5. CTR (Counter)\r\n" + "6. GCM (Galois/Counter Mode)\r\n");

			iOperationMode = Integer.parseInt(br.readLine());
			if (iOperationMode > 0 && iOperationMode < 7)
				break;
		}
		switch (iOperationMode) {
		case 1:
			sOperationMode = "AES/ECB/PKCS5Padding";
			break;
		case 2:
			sOperationMode = "AES/CBC/PKCS5Padding";
			break;
		case 3:
			sOperationMode = "AES/CFB/NoPadding";
			break;
		case 4:
			sOperationMode = "AES/OFB/NoPadding";
			break;
		case 5:
			sOperationMode = "AES/CTR/NoPadding";
			break;
		case 6:
			sOperationMode = "AES/GCM/NoPadding";
			break;
		default:
			sOperationMode = "AES/CBC/PKCS5Padding";
			break;
		}
		Assignment2 assign2 = new Assignment2(sOperationMode, strKeyword);
		while (true) {
			System.out.println("Select operation:\r\n" + "1. Encrypt\r\n" + "2. Decrypt\r\n" + "3. Exit");
			iOperation = Integer.parseInt(br.readLine());
			if ((iOperation > 0 || iOperation < 4))
				;
			else
				continue;
			if (iOperation == 3)
				break;
			sOperation = (iOperation == 1 ? "Encrypt" : "Decrypt");
			if (iOperation == 1) {
				System.out.println("Specify input type:\r\n" + "1. Type plain text to be encrypted, or\r\n"
						+ "2. Provide full path for file to be encrypted\r\n");

				while (true) {
					iInputType = Integer.parseInt(br.readLine());
					if (iInputType == 1 || iInputType == 2)
						break;
				}
				if (iInputType == 1) {
					System.out.println("Type text to be encrypted:\r\n");
					strPlainText = br.readLine();
					System.out.println("Following are the AES parameters:" + "\r\nOperation: " + sOperation
							+ "\r\nInput plain text: " + strPlainText + "\r\nAES operation mode: " + sOperationMode
							+ "\r\nAES key length: " + iKeyLength + "\r\nAES keyword: " + strKeyword);
					try {
						byte[] bytesCipherText = assign2.encrypt(strPlainText.getBytes());
						strCipherText = bytesCipherText.toString();
						System.out.println("Encrypted text:\r\n" + strCipherText);
						System.out.println();
					} catch (Exception e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}

				} else {
					System.out.println("Specify file name to be encrypted (full path): ");
					inputFilename = br.readLine();
					outputFilename = inputFilename + ".enc";
					System.out.println("Following are the AES parameters:" + "\r\nOperation: " + sOperation
							+ "\r\nInput file: " + inputFilename + "\r\nOutput file: " + outputFilename
							+ "\r\nAES operation mode: " + sOperationMode + "\r\nAES key length: " + iKeyLength
							+ "\r\nAES keyword: " + strKeyword);
					try {
						outputFilename = assign2.processFile(inputFilename, iOperation);
						System.out.println("Encrypted file is now available:\r\n " + outputFilename);
						System.out.println();
					} catch (Exception e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}

				}
			} else {
				// selected operation is decryption and we should work with current cipher text
				// or file
				if (iInputType == 1) {
					System.out.println("Following are the AES parameters:" + "\r\nOperation: " + sOperation
							+ "\r\nInput cipher text: " + strCipherText + "\r\nAES operation mode: " + sOperationMode
							+ "\r\nAES key length: " + iKeyLength + "\r\nAES keyword: " + strKeyword);
					try {
						byte[] bytesPlaintext = assign2.decrypt(strCipherText.getBytes());
						strPlainText = bytesPlaintext.toString();
						System.out.println("Decrypted text:\r\n" + strPlainText);
						System.out.println();
					} catch (Exception e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}
				} else {
					inputFilename = outputFilename;
					outputFilename = inputFilename + ".txt";
					System.out.println("Following are the AES parameters:" + "\r\nOperation: " + sOperation
							+ "\r\nInput file: " + inputFilename + "\r\nOutput file: " + outputFilename
							+ "\r\nAES operation mode: " + sOperationMode + "\r\nAES key length: " + iKeyLength
							+ "\r\nAES keyword: " + strKeyword);
					try {
						outputFilename = assign2.processFile(inputFilename, iOperation);
						System.out.println("Decrypted file is now available:\r\n" + outputFilename);
						System.out.println();
					} catch (Exception e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}

				}
			}
		}
	}
}
