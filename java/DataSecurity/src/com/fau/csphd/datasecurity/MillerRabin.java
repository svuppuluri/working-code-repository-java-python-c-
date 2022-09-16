package com.fau.csphd.datasecurity;

import java.math.*;
import java.util.Random;

import com.fau.csphd.datasecurity.ModPower;

public class MillerRabin {

	private static ModPower mp;

	public MillerRabin() {
		// TODO Auto-generated constructor stub
		mp = new ModPower();
	}

	public static BigInteger randomNumber(BigInteger lower, BigInteger upper) {
		BigInteger bigIntegerRange = upper.subtract(lower);
		Random rnd = new Random();
		int maxNumBitLength = upper.bitLength();

		BigInteger aRandomBigInt;

		aRandomBigInt = new BigInteger(maxNumBitLength, rnd);
		if (aRandomBigInt.compareTo(lower) < 0)
			aRandomBigInt = aRandomBigInt.add(lower);
		if (aRandomBigInt.compareTo(upper) >= 0)
			aRandomBigInt = aRandomBigInt.mod(bigIntegerRange).add(lower);
		return aRandomBigInt;
	}

	static boolean isMillerRabinPrime(BigInteger n, int k) {
		BigInteger a;
		BigInteger biNminus1 = n.subtract(BigInteger.ONE);
		// Corner cases
		if (n.intValue() <= 1 || n.intValue() == 4)
			return false;
		if (n.intValue() <= 3)
			return true;

		// Find r such that n - 1 = 2^s * r + 1
		// for some r >= 1
		BigInteger r = n.subtract(BigInteger.ONE);
		BigInteger biTWO = BigInteger.valueOf(2);
		BigInteger s = BigInteger.ZERO;
		while (r.mod(biTWO).equals(BigInteger.ZERO)) {
			r = r.divide(biTWO);
			s = s.add(BigInteger.ONE);
		}
		// Iterate given number of 'k' times
		for (int i = 0; i < k; i++) {
			a = randomNumber(BigInteger.valueOf(2), n.subtract(BigInteger.valueOf(2)));
			BigInteger y = mp.squareMultipy(a, r, n);
			s = s.subtract(BigInteger.ONE);
			if (y.equals(BigInteger.ONE) || y.equals(biNminus1))
				continue;
			while (s.compareTo(BigInteger.ZERO) == 1) {
				y = y.multiply(y).mod(n);
				// return composite
				if (!(y.equals(biNminus1)))
					break;
				s = s.subtract(BigInteger.ONE);
			}
		}
		// return probably prime
		return true;
	}

	public static void main(String args[]) {
		MillerRabin mrpt = new MillerRabin();
		int k = 50; // Number of iterations
		int len = 128;
		Random r = new Random();
		BigInteger n = BigInteger.probablePrime(len, r);
		System.out.println("Testing if " + n + " is a prime number...");

		if (isMillerRabinPrime(n, k))
			System.out.println(n + " is a prime number");
		else
			System.out.println(n + " is not a prime number");
	}
}
