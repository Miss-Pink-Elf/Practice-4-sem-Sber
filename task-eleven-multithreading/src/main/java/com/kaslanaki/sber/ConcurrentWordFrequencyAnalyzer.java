package com.kaslanaki.sber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ConcurrentWordFrequencyAnalyzer {

    private static final ConcurrentHashMap<String, Integer> totalWordCountMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: java -cp src/main/java com.kaslanaki.sber.ConcurrentWordFrequencyAnalyzer <путь_к_директории test_data/> <количество_потоков>");
            return;
        }

        String directoryPath = args[0];
        int numberOfThreads = Integer.parseInt(args[1]);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.currentTimeMillis();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt")) // Или другие расширения файлов
                    .forEach(filePath -> executorService.submit(() -> analyzeFile(filePath)));
        } catch (IOException e) {
            System.out.println("Ошибка при обходе директории: " + e.getMessage());
            executorService.shutdown();
            return;
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Потоки были прерваны: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("\nОбщая частота слов по всем файлам:");
        totalWordCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nВремя выполнения (" + numberOfThreads + " потоков): " + duration + " мс");
    }

    private static void analyzeFile(Path filePath) {
        Map<String, Integer> fileWordCountMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[\\p{Punct}]", "").toLowerCase();
                String[] words = line.split("\\s+");

                for (String word : words) {
                    if (!word.trim().isEmpty()) {
                        fileWordCountMap.put(word, fileWordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла " + filePath.getFileName() + ": " + e.getMessage());
        }

        // Объединение результатов текущего файла с общей статистикой
        for (Map.Entry<String, Integer> entry : fileWordCountMap.entrySet()) {
            totalWordCountMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
}