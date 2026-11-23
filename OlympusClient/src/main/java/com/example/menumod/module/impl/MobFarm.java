package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.Hand;

/**
 * Attacks the closest selected HOSTILE MOB in range. Never targets players.
 * Piglins, zombified piglins and wardens are always skipped.
 */
public class MobFarm extends Module {
    private final NumberSetting range = addNumber("Range", 3.5, 2.0, 6.0);
    private final BoolSetting zombies = addBool("Zombies", true);
    private final BoolSetting skeletons = addBool("Skeletons", true);
    private final BoolSetting creepers = addBool("Creepers", true);
    private final BoolSetting spiders = addBool("Spiders", true);
    private final BoolSetting endermen = addBool("Endermen", false);
    private final BoolSetting otherHostiles = addBool("Other hostile mobs", true);

    public MobFarm() {
        super("Mob Farm", "Attacks nearby hostile mobs automatically (never players).", Category.FARM);
    }

    private boolean wanted(Entity e) {
        if (!(e instanceof HostileEntity) || !e.isAlive()) return false;
        if (e instanceof AbstractPiglinEntity || e instanceof ZombifiedPiglinEntity) return false;
        if (e instanceof WardenEntity) return false;
        if (e instanceof ZombieEntity) return zombies.value;
        if (e instanceof AbstractSkeletonEntity) return skeletons.value;
        if (e instanceof CreeperEntity) return creepers.value;
        if (e instanceof SpiderEntity) return spiders.value;
        if (e instanceof EndermanEntity) return endermen.value;
        return otherHostiles.value;
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (mc.currentScreen != null || mc.interactionManager == null) return;
        if (player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        double best = range.value * range.value;
        Entity target = null;
        for (Entity e : mc.world.getEntities()) {
            if (wanted(e)) {
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
