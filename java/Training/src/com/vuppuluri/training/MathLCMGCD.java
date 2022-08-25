package com.vuppuluri.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MathLCMGCD {

	public int findGCD(int p1, int p2) {
		System.out.println("Received " + p1 + " " + p2);
		if (p1 == 0)
			return p2;
		if (p2 == 0)
			return p1;
		int rem = p1 % p2;
		if (rem == 0)
			return p2;
		else
			return this.findGCD(p2, rem);
	}

	public int findLCM(int p1, int p2) {
		System.out.println("Received " + p1 + " " + p2);
		if (p1 == 0)
			return p2;
		if (p2 == 0)
			return p1;
		return ((p1 * p2) / this.findGCD(p1, p2));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num1 = 0;
		int num2 = 0;
		int gcd = 0;
		int lcm = 0;
		MathLCMGCD mathLcmGcd = new MathLCMGCD();
		System.out.println("Enter first number: ");
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		try {
			num1 = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Enter second number: ");
		br = new BufferedReader (new InputStreamReader(System.in));
		try {
			num2 = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gcd = mathLcmGcd.findGCD(num1, num2);
		lcm = mathLcmGcd.findLCM(num1, num2);
		System.out.println("GCD = " + gcd + ", LCM = " + lcm);
	}

}
