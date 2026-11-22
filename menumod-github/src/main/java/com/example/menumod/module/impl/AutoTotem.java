package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

/** Puts a totem into your offhand when it is empty. */
public class AutoTotem extends Module {
    private final BoolSetting inventoryOnly = addBool("Inventory only", true);

    public AutoTotem() {
        super("Auto Totem", "Moves a totem to your offhand automatically.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (mc.interactionManager == null) return;
        if (inventoryOnly.value && !(mc.currentScreen instanceof InventoryScreen)) return;
        if (player.currentScreenHandler != player.playerScreenHandler) return;
        if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                int slot = i < 9 ? 36 + i : i; // hotbar -> 36..44, main inventory -> 9..35
                mc.interactionManager.clickSlot(0, slot, 40, SlotActionType.SWAP, player);
                return;
            }
        }
    }
}
