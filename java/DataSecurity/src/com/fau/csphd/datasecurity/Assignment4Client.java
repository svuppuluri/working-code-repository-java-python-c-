package com.fau.csphd.datasecurity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Assignment4Client {
	private SecretKey aesKey;
	private Cipher cipher;
	private String AES_KEY_SALT = "1234567890";
	private final int DATA_LENGTH = 128;
	private IvParameterSpec ivParamSpec;
	private String keyword = "";
	private String aesOperationMode = "";
	private BigInteger publicKey, alphaGenKey, alphaPublicKey, gammaKey, deltaKey;

	public BigInteger getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(BigInteger publicKey) {
		this.publicKey = publicKey;
	}

	public BigInteger getAlphaGenKey() {
		return alphaGenKey;
	}

	public void setAlphaGenKey(BigInteger alphaGenKey) {
		this.alphaGenKey = alphaGenKey;
	}

	public BigInteger getAlphaPublicKey() {
		return alphaPublicKey;
	}

	public void setAlphaPublicKey(BigInteger alphaPublicKey) {
		this.alphaPublicKey = alphaPublicKey;
	}

	private static MillerRabin mr;
	private static ModPower mp;
	private byte[] IV;

	public Assignment4Client(String mode, String keyword, int keylen) throws Exception {
		super();
		IV = new byte[16];
		new SecureRandom().nextBytes(IV);
		this.ivParamSpec = new IvParameterSpec(IV);
		System.out.println("after IV param spec: " + ivParamSpec.toString());
		this.keyword = keyword;
		mr = new MillerRabin();
		mp = new ModPower();
		this.aesOperationMode = mode;
		this.setSecretKey(this.getKeyFromPassword(this.keyword, this.AES_KEY_SALT, keylen));
		this.cipher = Cipher.getInstance(this.aesOperationMode);
	}

	public void encryptFile(String iFile, String oFile) throws Exception {

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

	public void importPublicKey(String filename) throws Exception {
		String bufInput = Files.readAllLines(Paths.get(filename)).get(0);
		StringTokenizer st = new StringTokenizer(bufInput.toString(), ",");
		this.publicKey = new BigInteger(st.nextToken());
		this.alphaGenKey = new BigInteger(st.nextToken());
		this.alphaPublicKey = new BigInteger(st.nextToken());
	}

	public void exportSecretKey(String filename) throws Exception {
		
		// Write key
		//byte[] aesKeyEncoded = aesKey.getEncoded();
		// Write base64 encoded key
		String bufInput = Base64.getEncoder().encodeToString(aesKey.getEncoded());
		System.out.println("Before export AES: " + bufInput);
		//encrypt aeskey using el gamal
		String bufEncrypted = this.bigEncryptElGamal(bufInput);
		System.out.println("After export AES: " + bufEncrypted);
		FileOutputStream fs = new FileOutputStream(new File(filename + ".key"));
		BufferedOutputStream bos = new BufferedOutputStream(fs);
		//bos.write(aesKeyEncoded);
		bos.write(bufEncrypted.getBytes());
		bos.close();
				
		// Write IV
		fs = new FileOutputStream(new File(filename + ".param"));
		bos = new BufferedOutputStream(fs);
		if (this.aesOperationMode.equals("AES/GCM/NoPadding")) {
			bos.write(this.cipher.getIV());
		} else
			bos.write(IV);
		bos.close();

		// Write AES mode
		fs = new FileOutputStream(new File(filename + ".mode"));
		bos = new BufferedOutputStream(fs);
		bos.write(this.cipher.getAlgorithm().getBytes());
		bos.close();
	}

	public void exportAESParameters(String filename) throws IOException {
		String strAesParams = "";
		String output = this.bigEncryptElGamal(strAesParams);
		Path path = Paths.get(filename);
		Files.write(path, output.getBytes());
	}

	public byte[] encryptAES(byte[] plainText) throws Exception {
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

	public byte[] decryptAES(byte[] cipherText) throws Exception {
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
			bufOutput = this.encryptAES(bufInput);
		} else {
			oFile = iFile + ".dec";
			bufOutput = this.decryptAES(bufInput);
		}
		Path path = Paths.get(oFile);
		Files.write(path, bufOutput);
		return (oFile);
	}

	private void setSecretKey(SecretKey key) {
		this.aesKey = key;
	}

	// encrypts with built in public key
	public void encryptElGamal(BigInteger m) {
		BigInteger k = mr.randomNumber(BigInteger.ONE, publicKey.subtract(BigInteger.valueOf(2)));
		gammaKey = mp.squareMultipy(alphaGenKey, k, publicKey);
		deltaKey = mp.squareMultipy(alphaPublicKey, k, publicKey);
		deltaKey = m.multiply(deltaKey).mod(publicKey);
	}

	// version of encrypt which uses the default public key
	public String bigEncryptElGamal(String message) {
		byte[] b = message.getBytes();
		BigInteger[][] cipher = new BigInteger[b.length][2];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			encryptElGamal(new BigInteger("" + b[i] + ""));
			cipher[i][0] = gammaKey;
			cipher[i][1] = deltaKey;
		}
		for (int i = 0; i < b.length; i++) {
			sb.append("(");
			sb.append(cipher[i][0]);
			sb.append(",");
			sb.append(cipher[i][1]);
			sb.append(")");
		}
		return (new String(sb));
	}

	public SecretKey getKeyFromPassword(String password, String salt, int keylen) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, keylen);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		System.out.println("AES key format: " + secret.getFormat());
		System.out.println("AES key: " + secret.getEncoded());
		System.out.println("AES algorithm: " + secret.getAlgorithm());
		System.out.println("AES key string: " + secret.toString());
		System.out.println("AES key length: " + secret.getEncoded().length);
		System.out.println("AES key base64 encoded: " + Base64.getEncoder().encodeToString(secret.getEncoded()));
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
		Assignment4Client assign2 = new Assignment4Client(sOperationMode, strKeyword, iKeyLength);
		assign2.importPublicKey("C:\\Users\\BillyVuppuluri\\Documents\\Data-Security\\week-2\\PUBLIC.KEY");
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
						byte[] bytesCipherText = assign2.encryptAES(strPlainText.getBytes());
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
						// assign2.exportSecretKeytoFile(inputFilename + "KEY");
					} catch (Exception e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}

				}
				assign2.exportSecretKey("C:\\Users\\BillyVuppuluri\\Documents\\Data-Security\\week-4\\AES");
			} else {
				// selected operation is decryption and we should work with current cipher text
				// or file
				if (iInputType == 1) {
					System.out.println("Following are the AES parameters:" + "\r\nOperation: " + sOperation
							+ "\r\nInput cipher text: " + strCipherText + "\r\nAES operation mode: " + sOperationMode
							+ "\r\nAES key length: " + iKeyLength + "\r\nAES keyword: " + strKeyword);
					try {
						byte[] bytesPlaintext = assign2.decryptAES(strCipherText.getBytes());
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
