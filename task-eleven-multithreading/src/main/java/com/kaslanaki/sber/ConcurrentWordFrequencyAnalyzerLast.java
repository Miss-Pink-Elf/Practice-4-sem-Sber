package com.kaslanaki.sber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentWordFrequencyAnalyzerLast {

    private static ConcurrentHashMap<String, Integer> totalWordCountMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: java -cp src/main/java com.kaslanaki.sber.ConcurrentWordFrequencyAnalyzer <путь_к_директории test_data/> <количество_потоков>");
            return;
        }

        String directoryPath = args[0];
        int numThreadsToUse = Integer.parseInt(args[1]);

        long startTime = System.currentTimeMillis();

        File rootDir = new File(directoryPath);

        List<File> textFiles = new ArrayList<>();
        findTextFiles(rootDir, textFiles);

        List<Thread> activeThreads = new ArrayList<>();

        for (File fileToAnalyze : textFiles) {
            while (activeThreads.size() >= numThreadsToUse) {
                try {
                    Thread.sleep(100);
                    activeThreads.removeIf(t -> !t.isAlive());
                } catch (InterruptedException e) {
                    System.out.println("Главный поток прерван во время ожидания.");
                    Thread.currentThread().interrupt();
                }
            }

            Thread thread = new Thread(() -> {
                analyzeSingleFile(fileToAnalyze);
            });
            activeThreads.add(thread);
            thread.start();
        }

        for (Thread thread : activeThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Ошибка ожидания потока: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("\nОбщая статистика слов:");
        for (Map.Entry<String, Integer> entry : totalWordCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\nВремя работы программы (" + numThreadsToUse + " потоков): " + duration + " мс");
    }

    private static void findTextFiles(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findTextFiles(file, fileList);
                } else if (file.getName().endsWith(".txt")) {
                    fileList.add(file);
                }
            }
        }
    }

    private static void analyzeSingleFile(File fileToProcess) {
        Map<String, Integer> currentFileWordCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileToProcess))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[.,!?;:\"()\\-]", "").toLowerCase();
                String[] words = line.split(" ");

                for (String word : words) {
                    if (word.length() > 0) {
                        if (currentFileWordCounts.containsKey(word)) {
                            currentFileWordCounts.put(word, currentFileWordCounts.get(word) + 1);
                        } else {
                            currentFileWordCounts.put(word, 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + fileToProcess.getName() + ": " + e.getMessage());
        }

        for (Map.Entry<String, Integer> entry : currentFileWordCounts.entrySet()) {
            totalWordCountMap.merge(entry.getKey(), entry.getValue(), (oldVal, newVal) -> oldVal + newVal);
        }
    }
}