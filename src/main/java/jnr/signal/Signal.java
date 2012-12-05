package jnr.signal;

public class Signal {
    public int getNumber() {
        return nativeSignal.getNumber();
    }

    public String getName() {
        return nativeSignal.getName();
    }

    @Override
    public boolean equals(Object obj) {
        return nativeSignal.equals(obj);
    }

    @Override
    public int hashCode() {
        return nativeSignal.hashCode();
    }

    public String toString() {
        return nativeSignal.toString();
    }

    public Signal(String name) {
        nativeSignal = new NativeSignal(name);
    }

    public static synchronized SignalHandler handle(final Signal signal, final SignalHandler sh) throws IllegalArgumentException {
        return NativeSignal.handle(signal, sh);
    }

    public static void raise(Signal signal) throws IllegalArgumentException {
        NativeSignal.raise(signal);
    }
    
    private final NativeSignal nativeSignal;
}
