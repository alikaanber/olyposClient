package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

/**
 * Hold a fishing rod: casts it, reels in when the bobber gets pulled down, then casts again.
 * Bite detection watches the bobber's downward speed, so the threshold may need tuning.
 */
public class AutoFish extends Module {
    private final NumberSetting recastDelay = addNumber("Recast delay (ticks)", 25.0, 10.0, 80.0);
    private int cooldown = 0;

    public AutoFish() {
        super("Auto Fish", "Casts and reels in automatically (hold a fishing rod).", Category.FARM);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (mc.interactionManager == null || mc.currentScreen != null) return;

        Hand hand;
        if (player.getMainHandStack().isOf(Items.FISHING_ROD)) {
            hand = Hand.MAIN_HAND;
        } else if (player.getOffHandStack().isOf(Items.FISHING_ROD)) {
            hand = Hand.OFF_HAND;
        } else {
            return;
        }

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        FishingBobberEntity bobber = player.fishHook;
        if (bobber == null) {
            // No line out: cast, then wait for the bobber to appear and settle
            mc.interactionManager.interactItem(player, hand);
            cooldown = 20;
            return;
        }

        Vec3d v = bobber.getVelocity();
        if (bobber.isTouchingWater() && v.y < -0.08) {
            // Bite: reel in, the next tick after the delay casts again
            mc.interactionManager.interactItem(player, hand);
            cooldown = (int) recastDelay.value;
        }
    }

    @Override
    protected void onDisable() {
        cooldown = 0;
    }
}
