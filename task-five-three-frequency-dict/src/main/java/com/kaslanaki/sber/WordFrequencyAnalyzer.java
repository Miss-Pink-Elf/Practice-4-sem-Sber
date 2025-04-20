package com.kaslanaki.sber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordFrequencyAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Использование: java com.kaslanaki.sber.WordFrequencyAnalyzer <путь_к_файлу>");
            return;
        }

        String filePath = args[0];
        Map<String, Integer> wordCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // \p{Punct} Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_\`{|}~
                line = line.replaceAll("[\\p{Punct}]", "").toLowerCase();
                String[] words = line.split("\\s+");

                for (String word : words) {
                    if (!word.trim().isEmpty()) {
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        System.out.println("Частотный анализ слов:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
