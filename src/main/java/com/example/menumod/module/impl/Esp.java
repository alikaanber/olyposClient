package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

/** Canli varliklara duvar arkasindan gorunen parlak kontur verir (EntityMixin kullanir). */
public class Esp extends Module {
    private final BoolSetting players = addBool("Oyuncular", true);
    private final BoolSetting hostiles = addBool("Dusman moblar", true);
    private final BoolSetting animals = addBool("Hayvanlar/diger", false);

    public Esp() {
        super("ESP", "Varliklari duvarlarin arkasindan parlak konturla gosterir.", Category.RENDER);
    }

    public boolean shouldGlow(Entity e) {
        if (e == MinecraftClient.getInstance().player) return false;
        if (e instanceof PlayerEntity) return players.value;
        if (e instanceof HostileEntity) return hostiles.value;
        if (e instanceof LivingEntity) return animals.value;
        return false;
    }
}
