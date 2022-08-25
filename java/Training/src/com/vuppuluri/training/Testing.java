/**
 * 
 */
package com.vuppuluri.training;

import java.io.File;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/**
 * @author Binathi
 *
 */
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		/*
		 * Singleton x = Singleton.getInstance(); Singleton y = Singleton.getInstance();
		 * Singleton z = Singleton.getInstance();
		 * System.out.println("Hashcode for x is " + x.hashCode());
		 * System.out.println("Hashcode for y is " + y.hashCode());
		 * System.out.println("Hashcode for z is " + z.hashCode());
		 */
		Singleton instance1 = Singleton.getInstance();
		/*
		 * Singleton instance1 = Singleton.INSTANCE;
		 * 
		 * Singleton instance2 = null; try { Constructor[] constructors =
		 * Singleton.class.getDeclaredConstructors(); for (Constructor constructor :
		 * constructors) { constructor.setAccessible(true); instance2 = (Singleton)
		 * constructor.newInstance(); break; } } catch (Exception e) {
		 * e.printStackTrace(); } System.out.println("instance1.hashcode() =" +
		 * instance1.hashCode()); System.out.println("instance2.hashcode() =" +
		 * instance2.hashCode());
		 */
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream("file.txt"));
		out.writeObject(out);
		out.close();

		ObjectInput in = new ObjectInputStream(new FileInputStream("file.txt"));
		Singleton instance2 = (Singleton) in.readObject();
		in.close();
		System.out.println("instance1.hashcode() =" + instance1.hashCode());
		System.out.println("instance2.hashcode() =" + instance2.hashCode());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
