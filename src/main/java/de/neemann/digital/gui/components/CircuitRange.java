package de.neemann.digital.gui.components;

public class CircuitRange {
    private final int min;
    private final int max;

    public CircuitRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}