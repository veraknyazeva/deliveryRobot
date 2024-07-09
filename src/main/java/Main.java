import java.util.*;

public class Main {
    private static final int THREADS_NUMBER = 1000;
    private static final int LENGTH = 100;
    private static final String LETTERS = "RLRFR";

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        long startTs = System.currentTimeMillis(); // start time
        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {

                String text = generateRoute(LETTERS, LENGTH);
                int count = 0;
                for (int j = 0; j < text.length(); j++) {
                    char c = text.charAt(j);
                    if (c == 'R') {
                        count++;
                    }
                }

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        Integer value = sizeToFreq.get(count);
                        value += 1;
                        sizeToFreq.put(count, value);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");

        int maxRepeats = 0;
        Integer keyOfMaxRepeats = null;
        for (Integer key : sizeToFreq.keySet()) {
            Integer value = sizeToFreq.get(key);
            if (value > maxRepeats) {
                maxRepeats = value;
                keyOfMaxRepeats = key;
            }
        }
        sizeToFreq.remove(keyOfMaxRepeats);

        String info = String.format("Самое частое количество повторений %d (встретилось %d раз)", keyOfMaxRepeats, maxRepeats);
        System.out.println(info);
        System.out.println("Другие размеры:");

        String formatForOtherSize = "- %d (%d раз)";

        Set<Integer> keys = sizeToFreq.keySet();
        for (Integer key : keys) {
            System.out.println(String.format(formatForOtherSize, key, sizeToFreq.get(key)));
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
