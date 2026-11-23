package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Harvests fully grown crops (wheat, carrots, potatoes, beetroots) around you.
 * With "Replant" on, it plants the seeds/crop you hold in your main hand back on the farmland.
 */
public class CropFarm extends Module {
    private final NumberSetting range = addNumber("Range", 3.5, 1.0, 4.5);
    private final NumberSetting delay = addNumber("Delay (ticks)", 2.0, 0.0, 10.0);
    private final BoolSetting replant = addBool("Replant (hold seeds)", true);

    private int cooldown = 0;
    private BlockPos pendingReplant = null;

    public CropFarm() {
        super("Crop Farm", "Harvests grown crops and replants them (hold seeds in your hand).", Category.FARM);
    }

    private static boolean isSeed(ItemStack stack) {
        return stack.isOf(Items.WHEAT_SEEDS) || stack.isOf(Items.CARROT)
                || stack.isOf(Items.POTATO) || stack.isOf(Items.BEETROOT_SEEDS);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (mc.currentScreen != null || mc.interactionManager == null) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        ClientPlayerEntity player = mc.player;

        // Step 1: replant the spot we harvested last
        if (pendingReplant != null) {
            BlockPos pos = pendingReplant;
            pendingReplant = null;
            BlockPos soil = pos.down();
            if (replant.value
                    && mc.world.getBlockState(pos).isAir()
                    && mc.world.getBlockState(soil).isOf(Blocks.FARMLAND)
                    && isSeed(player.getMainHandStack())) {
                Vec3d hit = Vec3d.ofCenter(soil).add(0.0, 0.5, 0.0);
                mc.interactionManager.interactBlock(player, Hand.MAIN_HAND,
                        new BlockHitResult(hit, Direction.UP, soil, false));
                player.swingHand(Hand.MAIN_HAND);
                cooldown = (int) delay.value;
                return;
            }
        }

        // Step 2: find the closest fully grown crop in range
        int r = (int) Math.ceil(range.value);
        double maxSq = range.value * range.value;
        Vec3d eye = player.getEyePos();
        BlockPos base = player.getBlockPos();

        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    BlockState state = mc.world.getBlockState(pos);
                    if (state.getBlock() instanceof CropBlock crop && crop.isMature(state)) {
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
            mc.interactionManager.attackBlock(best, Direction.UP);
            player.swingHand(Hand.MAIN_HAND);
            pendingReplant = best;
            cooldown = (int) delay.value;
        }
    }

    @Override
    protected void onDisable() {
        pendingReplant = null;
        cooldown = 0;
    }
}
