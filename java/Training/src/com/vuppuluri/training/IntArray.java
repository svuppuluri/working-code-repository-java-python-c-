package com.vuppuluri.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.*;
import java.util.random.*;

public class IntArray {

	int myArray[] = null;
	int dim = 0;

	public IntArray(int dim) {
		myArray = new int[dim];
		this.dim = dim;
		for (int i = 0; i < dim; i++) {
			myArray[i] = (int) (Math.random() * 9991);
		}
	}

	public void printArray() {
		for (int i = 0; i < this.dim; i++) {
			if (i == 0)
				System.out.print("Array: " + myArray[i]);
			else
				System.out.print(", " + myArray[i]);
		}
		System.out.println();
	}

	public int sortBubbleArray() {
		int iOps = 0;
		int min = 0;
		int temp = 0;
		for (int i = 0; i < this.dim; i++) {
			for (int j = 0; j < this.dim - 1; j++) {
				iOps++;
				if (myArray[j] > myArray[j + 1]) {
					temp = myArray[j];
					myArray[j] = myArray[j + 1];
					myArray[j + 1] = temp;
				}
			}
		}
		this.printArray();
		return iOps;
	}

	public int sortBubbleArrayOptimized() {
		int iOps = 0;
		int min = 0;
		int temp = 0;
		for (int i = 0; i < this.dim; i++) {
			for (int j = 0; j < this.dim - 1-i; j++) {
				iOps++;
				if (myArray[j] > myArray[j + 1]) {
					temp = myArray[j];
					myArray[j] = myArray[j + 1];
					myArray[j + 1] = temp;
				}
			}
		}
		this.printArray();
		return iOps;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int dim = 0;
		int ops = 0;
		System.out.println("Enter dimensions for the array: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			dim = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IntArray intArray = new IntArray(dim);
		intArray.printArray();
		ops = intArray.sortBubbleArray();
		System.out.println("Number of operations: " + ops);
		ops = intArray.sortBubbleArrayOptimized();
		System.out.println("Number of operations: " + ops);
	}

}
