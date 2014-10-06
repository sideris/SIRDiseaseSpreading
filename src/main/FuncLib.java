package main;

import java.util.Random;

public class FuncLib {
	// Implementing Fisher–Yates shuffle
	public static int[] shuffleArray(int[] ar)	  {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		return ar;
	}
}
