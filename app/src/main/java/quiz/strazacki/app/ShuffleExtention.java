package quiz.strazacki.app;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShuffleExtention {
	private static Random random = new Random();

	public static <T> List<T> shuffle(List<T> list) {
		int size = list.size();
		for (int i = size; i > 1; i--)
			Collections.swap(list, i - 1, random.nextInt(i));
		return list;
	}
}
