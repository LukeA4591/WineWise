package seng202.team0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team0.services.CounterService;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App, default from Maven
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testApp()
    {
            assertTrue( true);
    }

    @Test
    public void failCounterTest() {
        CounterService fail = new CounterService();
        fail.incrementCounter();
        Assertions.assertEquals(2, fail.getCurrentCount());
    }
}
