package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.client.MinecraftClient;

/** Zooms in by lowering the field of view. Minecraft does not allow FOV below 30. */
public class Zoom extends Module {
    private final NumberSetting fov = addNumber("FOV", 30, 30, 70);
    private Integer previous = null;

    public Zoom() {
        super("Zoom", "Zooms in by lowering your field of view.", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        previous = mc.options.getFov().getValue();
        mc.options.getFov().setValue((int) fov.value);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        int target = (int) fov.value;
        if (mc.options.getFov().getValue() != target) {
            mc.options.getFov().setValue(target);
        }
    }

    @Override
    protected void onDisable() {
        if (previous != null) {
            MinecraftClient.getInstance().options.getFov().setValue(previous);
            previous = null;
        }
    }
}
