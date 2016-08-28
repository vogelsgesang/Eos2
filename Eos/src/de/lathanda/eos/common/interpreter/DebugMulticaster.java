package de.lathanda.eos.common.interpreter;

import java.util.LinkedList;

public class DebugMulticaster {

    private final LinkedList<DebugListener> debugListener;

    public DebugMulticaster() {
        debugListener = new LinkedList<>();
    }

    public void add(DebugListener cl) {
        debugListener.add(cl);
    }

    public void remove(DebugListener cl) {
        debugListener.remove(cl);
    }

    public void fireDebugPoint(DebugInfo debug) {
        debugListener.forEach((dl) -> {
            dl.debugPointReached(debug);
        });
    }
}
