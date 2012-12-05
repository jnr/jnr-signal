package jnr.signal;

import java.util.Hashtable;
import jnr.constants.platform.Signal;
import jnr.ffi.Library;
import jnr.ffi.annotations.Clear;
import jnr.ffi.annotations.Delegate;

public final class NativeSignal {
    private static Hashtable<NativeSignal, SignalHandler> handlers = new Hashtable();
    private static Hashtable<Integer, NativeSignal> signals = new Hashtable();
    private Signal signal;

    public int getNumber() {
        return signal.intValue();
    }

    public String getName() {
        return signal.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NativeSignal other = (NativeSignal) obj;
        if (this.signal != other.signal) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getNumber();
    }

    public String toString() {
        return "SIG" + getName();
    }

    public NativeSignal(String name) {
        signal = Signal.valueOf("SIG" + name);
        if (signal == null) {
            throw new IllegalArgumentException("Unknown signal: " + name);
        }
    }

    public static synchronized SignalHandler handle(final jnr.signal.Signal signal, final SignalHandler sh) throws IllegalArgumentException {
        functions.signal(signal.getNumber(), new SignalCallback() {
            public void signal(int value) {
                sh.handle(signal);
            }
        });
        return sh;
    }
    
    public interface SignalCallback {
        public @Delegate void signal(int value);
    }
    
    public interface SignalFunctions {
        public int signal(int sig, SignalCallback callback);
        public int kill(int pid, int sig);
        public int getpid();
    }
    
    private static SignalFunctions functions = Library.loadLibrary("c", SignalFunctions.class);

    public static void raise(jnr.signal.Signal signal) throws IllegalArgumentException {
        functions.kill(functions.getpid(), signal.getNumber());
    }
}
