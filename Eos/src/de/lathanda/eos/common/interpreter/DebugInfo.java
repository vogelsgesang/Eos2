package de.lathanda.eos.common.interpreter;

/**
 * Enthält die aktuelle Position der Ausführung.
 * 
 * @author Peter (Lathanda) Schneider
 */
public class DebugInfo {
    private Marker codeRange;

    public DebugInfo() {
        this.codeRange = null;
    }
    public void resetAll() {
        codeRange = null;
    }
    public Marker getCodeRange() {
        return codeRange;
    }

    public void setCodeRange(Marker codeRange) {
        this.codeRange = codeRange;
    }  

    @Override
    public String toString() {
        return "DebugInfo{" + "codeRange=" + codeRange + '}';
    }
}
