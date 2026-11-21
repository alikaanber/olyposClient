package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

/** Sol elde totem yoksa envanterden bir totem alir. */
public class AutoTotem extends Module {
    public AutoTotem() {
        super("Oto Totem", "Sol ele otomatik totem koyar.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (mc.interactionManager == null) return;
        if (player.currentScreenHandler != player.playerScreenHandler) return;
        if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                int slot = i < 9 ? 36 + i : i; // hotbar -> 36..44, ana envanter -> 9..35
                mc.interactionManager.clickSlot(0, slot, 40, SlotActionType.SWAP, player);
                return;
            }
        }
    }
}
