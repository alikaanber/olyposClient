package com.example.menumod.module;

/** Bir modulun ayari. Alt siniflar: BoolSetting, NumberSetting. */
public abstract class Setting {
    public final String name;

    protected Setting(String name) {
        this.name = name;
    }
}
