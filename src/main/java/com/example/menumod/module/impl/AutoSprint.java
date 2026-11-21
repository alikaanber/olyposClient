package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {
    public AutoSprint() {
        super("Otomatik Kosu", "Ileri giderken otomatik kosar.", Category.MOVEMENT);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (mc.currentScreen == null && mc.options.forwardKey.isPressed() && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
