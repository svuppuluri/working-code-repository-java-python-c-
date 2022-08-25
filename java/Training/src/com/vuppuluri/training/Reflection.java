package com.vuppuluri.training;
import java.lang.reflect.*;

public class Reflection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < args.length; i++) {
				System.out.println("argument[" + i + "] is " + args[i]);
			}
			Class c = Class.forName(args[0]);
			Method m[] = c.getDeclaredMethods();
			for (int i = 0; i < m.length; i++) {
				System.out.println("method[" + i + "] is " + m[i]);
			}
		} catch (Throwable e) {
			System.out.println(e);
		}

	}

}
