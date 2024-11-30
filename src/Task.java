import java.util.concurrent.Callable;

// Callable для обчислення попарних добутків
public class Task implements Callable<int[]> {
    private final int[] array;

    public Task(int[] array) {
        this.array = array;
    }

    @Override
    public int[] call() {
        int[] result = new int[array.length / 2];
        for (int i = 0; i < array.length - 1; i += 2) {
            result[i / 2] = array[i] * array[i + 1];
        }
        return result;
    }
}