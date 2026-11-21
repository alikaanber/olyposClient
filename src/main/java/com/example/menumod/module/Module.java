package com.example.menumod.module;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tum ozelliklerin temel sinifi.
 * Yeni ozellik: bu sinifi extend et, onTick/onEnable/onDisable'i override et,
 * ModuleManager'a register(...) ile ekle.
 */
public class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public List<Setting> getSettings() { return Collections.unmodifiableList(settings); }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean value) {
        if (value == enabled) return;
        enabled = value;
        if (value) onEnable(); else onDisable();
    }

    /** Modul acildiginda cagrilir. */
    protected void onEnable() {}

    /** Modul kapandiginda cagrilir. */
    protected void onDisable() {}

    /** Modul aciksa her oyun tikinde (saniyede 20) cagrilir. Oyuncu ve dunya null degildir. */
    public void onTick(MinecraftClient mc) {}

    protected BoolSetting addBool(String settingName, boolean defaultValue) {
        BoolSetting s = new BoolSetting(settingName, defaultValue);
        settings.add(s);
        return s;
    }

    protected NumberSetting addNumber(String settingName, double defaultValue, double min, double max) {
        NumberSetting s = new NumberSetting(settingName, defaultValue, min, max);
        settings.add(s);
        return s;
    }
}
