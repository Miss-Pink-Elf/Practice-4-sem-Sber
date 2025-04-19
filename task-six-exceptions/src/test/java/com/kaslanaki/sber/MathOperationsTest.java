package com.kaslanaki.sber;

import junit.framework.Test;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathOperationsTest {
    @Test(expected = MathOperations.class)
    public void testOddNumberException() throws MathOperations {
        MathOperations mathOps = new MathOperations();
        mathOps.multiplyEvenNumbers(1, 2); // нечетное
    }

    @Test(expected = OddNumberException.class)
    public void testOddNumberException2() throws OddNumberException {
        MathOperations mathOps = new MathOperations();
        mathOps.multiplyEvenNumbers(2, 3); // нечетное
    }
}
