package com.fau.csphd.datasecurity;

import java.math.BigInteger;

public class ModPower {

	public ModPower() {
		// TODO Auto-generated constructor stub
	}

	public int memoryEfficient(int base, int exponent, int modulus) {
		if (modulus == 1) {
			return 0;
		}
		int c = 1;
		for (int e_prime = 0; e_prime < exponent; e_prime++) {
			c = (c * base) % modulus;
		}
		return c;
	}

	public int squareMultipy(int base, int exponent, int modulus) {
		int answer = 1; // Initialize result
		base = Math.floorMod(base, modulus);
		if (base == 0)
			return 0;
		while (exponent > 0) {
			if (exponent % 2 == 1)
				answer = Math.floorMod((answer * base), modulus);
			exponent = exponent >> 1;
			base = Math.floorMod((base * base), modulus);
		}
		return answer;
	}

	public long squareMultipy(long base, long exponent, long modulus) {
		long answer = 1; // Initialize result
		base = Math.floorMod(base, modulus);
		if (base == 0)
			return 0;
		while (exponent > 0) {
			if (exponent % 2 == 1)
				answer = Math.floorMod((answer * base), modulus);
			exponent = exponent >> 1;
			base = Math.floorMod((base * base), modulus);
		}
		return answer;
	}
	
	public BigInteger squareMultipy(BigInteger base, BigInteger exponent, BigInteger modulus) {
		BigInteger biTWO = BigInteger.valueOf(2);
		BigInteger answer = BigInteger.ZERO;
		base = base.mod(modulus);
		if (base.equals(BigInteger.ZERO))
			return answer;
		answer = BigInteger.ONE;
		while (exponent.compareTo(BigInteger.ZERO) > 0) {
			if (exponent.mod(biTWO).compareTo(BigInteger.ONE) == 0)
				answer = answer.multiply(base).mod(modulus);
			exponent = exponent.shiftRight(1);
			base = base.multiply(base).mod(modulus);
		}
		return answer;
	}
	
	public int findLCM(int num1, int num2) {
		return ((num1 * num2) / findGCD(BigInteger.valueOf(num1), BigInteger.valueOf(num2)).intValue());
	}

	public int findGCD(int num1, int num2) {
		return (findGCD(BigInteger.valueOf(num1), BigInteger.valueOf(num2)).intValue());
	}

	public long findGCD(long num1, long num2) {
		return (findGCD(BigInteger.valueOf(num1), BigInteger.valueOf(num2)).longValue());
	}

	public BigInteger findGCD(BigInteger num1, BigInteger num2) {
		BigInteger biGcd;

		if (num1.compareTo(num2) > 0)
			biGcd = num1.gcd(num2);
		else
			biGcd = num2.gcd(num1);
		return (biGcd);
	}


	public int findModInverse (int base, int modulus) {
		return (BigInteger.valueOf(base).modInverse(BigInteger.valueOf(modulus)).intValue());
	}

	public long findModInverse (long base, long modulus) {
		return (BigInteger.valueOf(base).modInverse(BigInteger.valueOf(modulus)).longValue());
	}

	public BigInteger findModInverse (BigInteger base, BigInteger modulus) {
		return (base.modInverse(modulus));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModPower mp = new ModPower();
		int b = 4, e = 12, m = 5;
		// int b = 4, e = 13, m = 497;
		System.out.println("Timestamp: " + System.currentTimeMillis());
		System.out.println("Modular exponentiation using memory efficient method of base= " + b + ", exponent " + e
				+ ", modulus " + m + " is " + mp.memoryEfficient(b, e, m));
		System.out.println("Timestamp: " + System.currentTimeMillis());
		System.out.println("Modular exponentiation using square and multiply method of base= " + b + ", exponent " + e
				+ ", modulus " + m + " is " + mp.squareMultipy(b, e, m));
		System.out.println("Timestamp: " + System.currentTimeMillis());
		int num1= 2940; int num2 = 3150;
		System.out.println("GCD of " + num1 + ", " + num2 + " is " + mp.findGCD(num1, num2));
		System.out.println("LCM of " + num1 + ", " + num2 + " is " + mp.findLCM(num1, num2));
		int base = 17, modulus = 26;
		System.out.println("Inverse of " + base + " mod " + modulus + " is " + mp.findModInverse(base, modulus));
	}

}
