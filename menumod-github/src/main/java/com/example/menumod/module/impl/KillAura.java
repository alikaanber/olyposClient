package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Hand;

/** Attacks the closest HOSTILE MOB in range. Never targets players. */
public class KillAura extends Module {
    private final NumberSetting range = addNumber("Range", 3.5, 2.0, 6.0);

    public KillAura() {
        super("Kill Aura", "Attacks nearby hostile mobs automatically (never players).", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (mc.currentScreen != null || mc.interactionManager == null) return;
        if (player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        double best = range.value * range.value;
        Entity target = null;
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof HostileEntity && e.isAlive()) {
                double d = player.squaredDistanceTo(e);
                if (d <= best) {
                    best = d;
                    target = e;
                }
            }
        }
        if (target != null) {
            mc.interactionManager.attackEntity(player, target);
            player.swingHand(Hand.MAIN_HAND);
        }
    }
}
