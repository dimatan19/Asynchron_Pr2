import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int NUM_THREADS = 3;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        // Введення діапазону чисел
        System.out.print("Введіть мінімальне значення діапазону: ");
        int min = scanner.nextInt();

        System.out.print("Введіть максимальне значення діапазону: ");
        int max = scanner.nextInt();

        if (min >= max) {
            System.out.println("Мінімальне значення має бути меншим за максимальне.");
            return;
        }

        // Генерація масиву
        int size = new Random().nextInt(21) + 40; // Розмір від 40 до 60
        int[] array = new Random().ints(size, min, max + 1).toArray();

        System.out.println("Згенерований масив: " + Arrays.toString(array));

        // Розбивка на частини
        int chunkSize = Math.max(size / NUM_THREADS, 1);

        // Використання ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        List<Future<int[]>> futures = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(0);

        long startTime = System.nanoTime(); // Фіксуємо час початку

        while (index.get() < array.length) {
            int start = index.getAndAdd(chunkSize);
            int end = Math.min(start + chunkSize, array.length);

            // Відправка копії частини масиву в Callable
            Callable<int[]> task = new Task(Arrays.copyOfRange(array, start, end));
            futures.add(executor.submit(task));
        }

        // Збір результатів
        CopyOnWriteArraySet<int[]> results = new CopyOnWriteArraySet<>();
        for (Future<int[]> future : futures) {
            if (!future.isCancelled()) {
                results.add(future.get());
            }
        }

        // Об'єднання результатів
        List<Integer> finalResult = new ArrayList<>();
        for (int[] chunk : results) {
            for (int num : chunk) {
                finalResult.add(num);
            }
        }

        long endTime = System.nanoTime(); // фіксуємо кінцевий час

        // Виводимо результат та час виконання
        System.out.println("Результуючий масив: " + finalResult);
        System.out.println("Час виконання програми (мс): " + (endTime - startTime) / 1_000_000);

        // Завершення ExecutorService
        executor.shutdown();
    }
}


