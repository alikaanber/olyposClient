package com.example.menumod.mixin;

import com.example.menumod.module.BlockFilter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * X-Ray / Netherite Finder / Depolama Finder:
 * Bu modullerden biri aciksa sadece secilen bloklarin yuzleri cizilir, digerleri cizilmez.
 * Hedef metod: Block.shouldDrawSide(BlockState, BlockState, Direction).
 * Hata verirse bu metodun 1.21.11 Yarn'daki imzasini kontrol et.
 */
@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "shouldDrawSide(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("HEAD"), cancellable = true)
    private static void menumod$filter(BlockState state, BlockState otherState, Direction side,
                                       CallbackInfoReturnable<Boolean> cir) {
        if (!BlockFilter.active()) return;
        String path = Registries.BLOCK.getId(state.getBlock()).getPath();
        cir.setReturnValue(BlockFilter.shouldShow(path));
    }
}
