package com.fau.csphd.datasecurity;

import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

class ElGamal {
	private BigInteger publicKey, alphaGenKey, privateKey, alphaPublicKey, gammaKey, deltaKey;
	private Random r;
	private int iKeyLength = 10;

	public ElGamal() {
		r = new Random();
		publicKey = BigInteger.probablePrime(iKeyLength, r);
		System.out.println("Prime number generated: " + publicKey);
		alphaGenKey = new BigInteger("4893003337626352152463254152616458181260144281");
		privateKey = new BigInteger("843900337326351225463254152616458181260144281");
		alphaPublicKey = alphaGenKey.modPow(privateKey, publicKey);
		gammaKey = new BigInteger("0");
		deltaKey = new BigInteger("0");
	}

	// encrypts with built in public key
	public void encrypt(BigInteger m) {
		// System.out.println("m: " + m.toString());
		// constructs a random bit integer k with any number of bits from 3 to 1 less
		// than p
		BigInteger k = randomPrime(publicKey.bitCount() - (int) (3 + Math.random() * publicKey.bitCount()));
		gammaKey = alphaGenKey.modPow(k, publicKey);
		deltaKey = m.xor(alphaPublicKey.modPow(k, publicKey));
		// System.out.println("y1: " + y1);
		// System.out.println("y2: " + y2);
	}

	public BigInteger decrypt(BigInteger[] b) {
		return b[1].xor(b[0].modPow(privateKey, publicKey));
	}

	// version of encrypt which takes a new public key
	/*
	 * public String bigEncrypt(String message,BigInteger g, BigInteger y,
	 * BigInteger p){ this.g = g; this.y = y; this.p = p; return
	 * bigEncrypt(message); }
	 */

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
		ElGamal e = new ElGamal();
		String k = "14893003337626352152463254152616458181260144281,4893003337626352152463254152616458181260144281,5260810279682188795512623296546807031696158558";
		String temp = e.bigEncrypt("my message", k);
		System.out.println(temp);
		// System.out.println(e.bigDecrypt(temp));
		// BigInteger m = new BigInteger("180");
		// String mess="this is my message";
		// System.out.println("Encrypting: " + mess );
		// System.out.println("ciphertext: " + e.bigEncrypt(mess));
		// BigInteger[] temp = e.encrypt(m,k);
		// BigInteger i = e.decrypt(temp);
		// BigInteger g = new
		// BigInteger("4893003337626352152463254152616458181260144281");
		// BigInteger a = new
		// BigInteger("843900337326351225463254152616458181260144281");
		// BigInteger p = new
		// BigInteger("14893003337626352152463254152616458181260144281");
		// System.out.println("y: " + (g.modPow(a,p)).toString());
	}

	public BigInteger randomPrime(int i) {
		return BigInteger.valueOf(i); //randomPrime(i, 30);
	}

}