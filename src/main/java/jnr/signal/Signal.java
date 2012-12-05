package jnr.signal;

import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;

public class Signal {
    private final jnr.constants.platform.Signal signal;
    private static final POSIX posix = POSIXFactory.getPOSIX();
    
    public int getNumber() {
        return signal.intValue();
    }

    public String getName() {
        return signal.name().substring("SIG".length());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.getNumber();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Signal other = (Signal) obj;
        if (this.getNumber() != other.getNumber()) {
            return false;
        }
        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "SIG" + getName();
    }

    public Signal(String name) {
        signal = jnr.constants.platform.Signal.valueOf("SIG" + name);
    }
    
    private static class POSIXSignalHandler implements jnr.posix.SignalHandler {
        private final Signal signal;
        private final SignalHandler handler;
        
        POSIXSignalHandler(Signal signal, SignalHandler handler) {
            this.signal = signal;
            this.handler = handler;
        }
        
        public void handle(int sig) {
            assert signal.getNumber() == sig;
            handler.handle(signal);
        }
    }

    public static synchronized SignalHandler handle(final Signal signal, final SignalHandler sh) throws IllegalArgumentException {
        jnr.posix.SignalHandler oldHandler = posix.signal(signal.signal, new POSIXSignalHandler(signal, sh));
        
        if (oldHandler instanceof POSIXSignalHandler) {
            return ((POSIXSignalHandler)oldHandler).handler;
        } else {
            return null;
        }
    }

    public static void raise(Signal signal) throws IllegalArgumentException {
        posix.kill(posix.getpid(), signal.getNumber());
    }
}
