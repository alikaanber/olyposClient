package com.example.menumod.mixin;

import com.example.menumod.module.ModuleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** ESP modulu: secili varliklara vanilla "glowing" konturu verir. */
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void menumod$esp(CallbackInfoReturnable<Boolean> cir) {
        if (!ModuleManager.ESP.isEnabled()) return;
        if (ModuleManager.ESP.shouldGlow((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
