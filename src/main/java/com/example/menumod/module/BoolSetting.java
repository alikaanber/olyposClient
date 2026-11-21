package com.example.menumod.module;

public class BoolSetting extends Setting {
    public boolean value;

    public BoolSetting(String name, boolean defaultValue) {
        super(name);
        this.value = defaultValue;
    }
}
