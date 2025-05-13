//package com.kaslanaki.sber;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class WordFrequencyAnalyzerTest {
//    @Test
//    public void testAnalyzeFile() throws IOException {
//        // Создаем тестовый файл
//        File tempFile = File.createTempFile("testfile", ".txt");
//        FileWriter writer = new FileWriter(tempFile);
//        writer.write("Hello world\nHello\nHello world");
//        writer.close();
//
//        // Вызываем метод анализа
//        Map<String, Integer> result = WordFrequencyAnalyzer.analyzeFile(tempFile.getAbsolutePath());
//
//        // Проверяем результаты
//        assertEquals(3, result.get("hello"));
//        assertEquals(2, result.get("world"));
//
//        // Удаляем временный файл
//        tempFile.delete();
//    }
//
//    @Test
//    public void testEmptyFile() throws IOException {
//        // Создаем пустой тестовый файл
//        File tempFile = File.createTempFile("emptytestfile", ".txt");
//        tempFile.deleteOnExit(); // Удалить файл при выходе
//
//        // Вызываем метод анализа
//        Map<String, Integer> result = WordFrequencyAnalyzer.analyzeFile(tempFile.getAbsolutePath());
//
//        // Проверяем, что карта слов пуста
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    public void testFileWithPunctuation() throws IOException {
//        // Создаем файл с пунктуацией
//        File tempFile = File.createTempFile("punctuationfile", ".txt");
//        FileWriter writer = new FileWriter(tempFile);
//        writer.write("Hello, world! Hello.\nHello; world?");
//        writer.close();
//
//        // Вызываем метод анализа
//        Map<String, Integer> result = WordFrequencyAnalyzer.analyzeFile(tempFile.getAbsolutePath());
//
//        // Проверяем результаты
//        assertEquals(3, result.get("hello"));
//        assertEquals(2, result.get("world"));
//
//        // Удаляем временный файл
//        tempFile.delete();
//    }
//}
