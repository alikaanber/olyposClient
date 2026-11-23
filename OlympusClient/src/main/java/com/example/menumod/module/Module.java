package com.example.menumod.module;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class of every feature.
 * New feature: extend this class, override onTick/onEnable/onDisable,
 * then add it to ModuleManager with register(...).
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

    /** Called when the module is turned on. */
    protected void onEnable() {}

    /** Called when the module is turned off. */
    protected void onDisable() {}

    /** Called every game tick (20 per second) while enabled. Player and world are never null. */
    public void onTick(MinecraftClient mc) {}

    /** Called when the player leaves this module's settings screen. */
    public void onSettingsClosed() {}

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
