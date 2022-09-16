package com.fau.csphd.datasecurity;

import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

class Assignment3 {
	private BigInteger publicKey, alphaGenKey, privateKey, alphaPublicKey, gammaKey, deltaKey;

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

	private Random r;
	private static int iterations = 50;
	private int iKeyLength = 16;
	private static MillerRabin mr;
	private static ModPower mp;

	public Assignment3(int keylen) {
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
		// return b[1].xor(b[0].modPow(privateKey, publicKey));
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

	public static void main(String args[]) {
		Assignment3 e = new Assignment3(256);
		System.out.println("Public key component (public): " + e.publicKey + ", (alpha): " + e.alphaGenKey
				+ ", alpha**private: " + e.alphaPublicKey);
		System.out.println("Private key: " + e.privateKey);
		String message = "This is the input for ElGamal encryption!";
		String cipher = e.bigEncrypt(message);
		System.out.println("Input plain text: " + message);
		System.out.println("Encryped message: " + cipher);
		System.out.println("Decryped message: " + e.bigDecrypt(cipher));
	}
}