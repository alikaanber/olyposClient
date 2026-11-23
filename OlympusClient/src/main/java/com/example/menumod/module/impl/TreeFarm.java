package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/** Chops the closest log in range, one after another. Breaks EVERY log in range, including builds. */
public class TreeFarm extends Module {
    private final NumberSetting range = addNumber("Range", 4.0, 1.0, 4.5);
    private boolean breaking = false;

    public TreeFarm() {
        super("Tree Farm", "Chops logs in range automatically (also breaks logs in your builds).", Category.FARM);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (mc.currentScreen != null || mc.interactionManager == null) return;
        ClientPlayerEntity player = mc.player;

        int r = (int) Math.ceil(range.value);
        double maxSq = range.value * range.value;
        Vec3d eye = player.getEyePos();
        BlockPos base = player.getBlockPos();

        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -2; dy <= r + 2; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    BlockState state = mc.world.getBlockState(pos);
                    if (state.isIn(BlockTags.LOGS)) {
                        double d = eye.squaredDistanceTo(Vec3d.ofCenter(pos));
                        if (d <= maxSq && d < bestDist) {
                            bestDist = d;
                            best = pos;
                        }
                    }
                }
            }
        }

        if (best != null) {
            mc.interactionManager.updateBlockBreakingProgress(best, Direction.UP);
            player.swingHand(Hand.MAIN_HAND);
            breaking = true;
        } else if (breaking) {
            mc.interactionManager.cancelBlockBreaking();
            breaking = false;
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (breaking && mc.interactionManager != null) {
            mc.interactionManager.cancelBlockBreaking();
        }
        breaking = false;
    }
}
