package com.kaslanaki.sber;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SortsTest {

    @Test
    public void testNegPosNumArrayBub() {
        int[] arr = {-10, 10, 1, 2, 5, -9, 0, -5};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new int[]{-10, -9, -5, 0, 1, 2, 5, 10}, arr);
    }

    @Test
    public void testNegPosNumArrayQui() {
        int[] arr = {-10, 10, 1, 2, 5, -9, 0, -5};
        Sorts.quickSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{-10, -9, -5, 0, 1, 2, 5, 10}, arr);
    }

    @Test
    public void testSortedArrayBub() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }

    @Test
    public void testSortedArrayQui() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Sorts.quickSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }


    @Test
    public void testReverseArrayBub() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }

    @Test
    public void testReverseArrayQui() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Sorts.quickSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }

    @Test
    public void testEmptyArrayBub() {
        int[] arr = {};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testEmptyArrayQui() {
        int[] arr = {};
        Sorts.quickSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testArrayDuplicatesBub() {
        int[] arr = {10, 3, 1, 2, 10, 3, 4, 7, 1, 9, 8};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 7, 8, 9, 10, 10}, arr);
    }

    @Test
    public void testArrayDuplicatesQui() {
        int[] arr = {10, 3, 1, 2, 10, 3, 4, 7, 1, 9, 8};
        Sorts.quickSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 7, 8, 9, 10, 10}, arr);
    }

}
