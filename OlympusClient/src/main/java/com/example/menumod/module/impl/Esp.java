package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

/** Gives entities a see-through-walls glowing outline (used by EntityMixin). */
public class Esp extends Module {
    private final BoolSetting players = addBool("Players", true);
    private final BoolSetting hostiles = addBool("Hostile mobs", true);
    private final BoolSetting animals = addBool("Animals / other mobs", false);
    private final BoolSetting items = addBool("Dropped items", true);

    public Esp() {
        super("ESP", "Shows entities through walls with a glowing outline.", Category.RENDER);
    }

    public boolean shouldGlow(Entity e) {
        if (e == MinecraftClient.getInstance().player) return false;
        if (e instanceof PlayerEntity) return players.value;
        if (e instanceof HostileEntity) return hostiles.value;
        if (e instanceof LivingEntity) return animals.value;
        if (e instanceof ItemEntity) return items.value;
        return false;
    }
}
