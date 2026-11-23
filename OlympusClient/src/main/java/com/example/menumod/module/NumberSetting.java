package com.example.menumod.module;

public class NumberSetting extends Setting {
    public double value;
    public final double min;
    public final double max;

    public NumberSetting(String name, double defaultValue, double min, double max) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
    }
}
