package com.fau.csphd.datasecurity;

import java.io.DataInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class Assignment4Server {
	private BigInteger publicKey, alphaGenKey, privateKey, alphaPublicKey, gammaKey, deltaKey;

	public BigInteger getPublicKey() {
		return publicKey;
	}

	public void exportPublicKey(String filename) throws Exception {
		Path path = Paths.get(filename);
		Files.write(path,
				(publicKey.toString() + "," + alphaGenKey.toString() + "," + alphaPublicKey).toString().getBytes());
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

	public BigInteger getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}

	public BigInteger getAlphaPublicKey() {
		return alphaPublicKey;
	}

	public void setAlphaPublicKey(BigInteger alphaPublicKey) {
		this.alphaPublicKey = alphaPublicKey;
	}

	public int getiKeyLength() {
		return iKeyLength;
	}

	public void setiKeyLength(int iKeyLength) {
		this.iKeyLength = iKeyLength;
	}

	private final int DATA_LENGTH = 128;
	private Random r;
	private static int iterations = 50;
	private int iKeyLength = 16;
	private static MillerRabin mr;
	private static ModPower mp;
	private SecretKey aesKey;
	private byte[] IV;
	private IvParameterSpec ivParamSpec;
	private String aesOperationMode = "";
	private Cipher cipher;

	public Assignment4Server(int keylen) {
		mr = new MillerRabin();
		mp = new ModPower();
		iKeyLength = keylen;
		while (true) {
			r = new Random();
			publicKey = BigInteger.probablePrime(iKeyLength, r);
			if (mr.isMillerRabinPrime(publicKey, iterations))
				break;
		}
		alphaGenKey = mr.randomNumber(BigInteger.valueOf(2), publicKey.subtract(BigInteger.ONE));
		while (true) {
			privateKey = mr.randomNumber(BigInteger.ONE, publicKey.subtract(BigInteger.valueOf(2)));
			if (mp.findGCD(publicKey, privateKey).equals(BigInteger.ONE))
				break;
		}
		alphaPublicKey = mp.squareMultipy(alphaGenKey, privateKey, publicKey);
		gammaKey = new BigInteger("0");
		deltaKey = new BigInteger("0");
	}

	// encrypts with built in public key
	public void encrypt(BigInteger m) {
		BigInteger k = mr.randomNumber(BigInteger.ONE, publicKey.subtract(BigInteger.valueOf(2)));
		gammaKey = mp.squareMultipy(alphaGenKey, k, publicKey);
		deltaKey = mp.squareMultipy(alphaPublicKey, k, publicKey);
		deltaKey = m.multiply(deltaKey).mod(publicKey);
	}

	public String bigEncrypt(String message, String key) {
		StringTokenizer st = new StringTokenizer(key, "(),");
		publicKey = new BigInteger(st.nextToken());
		alphaGenKey = new BigInteger(st.nextToken());
		alphaPublicKey = new BigInteger(st.nextToken());
		return bigEncrypt(message);
	}

	// version of encrypt which uses the default public key
	public String bigEncrypt(String message) {
		byte[] b = message.getBytes();
		BigInteger[][] cipher = new BigInteger[b.length][2];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			encrypt(new BigInteger("" + b[i] + ""));
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

	public BigInteger decrypt(BigInteger[] b) {
		BigInteger m;
		gammaKey = b[0];
		deltaKey = b[1];
		gammaKey = mp.squareMultipy(gammaKey, publicKey.subtract(BigInteger.ONE).subtract(privateKey), publicKey);
		m = gammaKey.multiply(deltaKey).mod(publicKey);
		return (m);
	}

	public String bigDecrypt(String c) {
		StringTokenizer st = new StringTokenizer(c, "(),");
		BigInteger[] temp = new BigInteger[2];
		StringBuffer plain = new StringBuffer();
		while (st.hasMoreTokens()) {
			temp[0] = new BigInteger(st.nextToken());
			temp[1] = new BigInteger(st.nextToken());
			plain.append((char) (decrypt(temp)).intValue());
		}
		return new String(plain);
	}

	public void importSecretKey(String filename) throws Exception {
		// read binary key
		//byte[] byteInput = Files.readAllBytes(Paths.get(filename));
		//aesKey = new SecretKeySpec(byteInput, "AES");
		// read base64 encoded and encrypted key
		String bufCipher = Files.readAllLines(Paths.get(filename)).get(0);
		System.out.println("AES key read: " + bufCipher);
		// decrypt key
		String bufEncoded = this.bigDecrypt(bufCipher);
		System.out.println("AES key decrypted: " + bufEncoded);
		byte byteInput[] = Base64.getDecoder().decode(bufEncoded);
		aesKey = new SecretKeySpec(byteInput, "AES");

		StringBuilder strb = new StringBuilder(filename);
		int index = strb.lastIndexOf("key");
		strb.replace(index, "key".length() + index, "param");
		filename = strb.toString();

		byteInput = Files.readAllBytes(Paths.get(filename));
		IV = byteInput;
		this.ivParamSpec = new IvParameterSpec(IV);

		// Read AES mode
		strb = new StringBuilder(filename);
		index = strb.lastIndexOf("param");
		strb.replace(index, "param".length() + index, "mode");
		filename = strb.toString();

		// filename = filename.replaceAll("param", "mode");
		String bufInput = Files.readAllLines(Paths.get(filename)).get(0);
		this.cipher = Cipher.getInstance(bufInput);
		this.aesOperationMode = this.cipher.getAlgorithm();
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
			GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, IV);
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

	public static void main(String args[]) throws Exception {
		Assignment4Server e = new Assignment4Server(2048);
		System.out.println("Public key component (public): " + e.publicKey + ", (alpha): " + e.alphaGenKey
				+ ", alpha**private: " + e.alphaPublicKey);
		System.out.println("Private key: " + e.privateKey);
		e.exportPublicKey("C:\\Users\\BillyVuppuluri\\Documents\\Data-Security\\week-4\\PUBLIC.KEY");
		e.importSecretKey("C:\\Users\\BillyVuppuluri\\Documents\\Data-Security\\week-2\\aes.key");
		String message = "This is the input for ElGamal encryption!";
		String cipher = e.bigEncrypt(message);
		System.out.println("Input plain text: " + message);
		System.out.println("Encryped message: " + cipher);
		System.out.println("Decryped message: " + e.bigDecrypt(cipher));
	}
}