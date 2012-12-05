package jnr.signal;

import org.junit.Assert;
import org.junit.Test;

public class SignalTest {
    @Test
    public void testSimple() {
        Signal s = new Signal("HUP");
        final boolean[] fired = {false};
        Signal.handle(s, new SignalHandler() {
            public void handle(Signal signal) {
                fired[0] = true;
            }
        });
        
        Signal.raise(s);
        
        while (!fired[0]);
        Assert.assertTrue(fired[0]);
    }
}
