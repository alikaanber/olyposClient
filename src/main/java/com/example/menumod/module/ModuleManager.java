package com.example.menumod.module;

import com.example.menumod.module.impl.AutoSprint;
import com.example.menumod.module.impl.AutoTotem;
import com.example.menumod.module.impl.Esp;
import com.example.menumod.module.impl.KillAura;
import com.example.menumod.module.impl.NetheriteFinder;
import com.example.menumod.module.impl.StorageFinder;
import com.example.menumod.module.impl.Xray;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.ArrayList;
import java.util.List;

/** Butun modullerin kayit defteri. Yeni modulu buraya register(...) ile ekle. */
public final class ModuleManager {
    // NOT: ALL listesi, register(...) kullanan alanlardan ONCE tanimlanmali.
    private static final List<Module> ALL = new ArrayList<>();

    // Savas
    public static final KillAura KILL_AURA = register(new KillAura());
    public static final AutoTotem AUTO_TOTEM = register(new AutoTotem());

    // Gorsel
    public static final Esp ESP = register(new Esp());
    public static final Module FULLBRIGHT = register(new Module(
            "Tam Parlaklik", "Karanlikta bile her yeri aydinlik gosterir (gece gorusu).", Category.RENDER));
    public static final Xray XRAY = register(new Xray());
    public static final NetheriteFinder NETHERITE_FINDER = register(new NetheriteFinder());
    public static final StorageFinder STORAGE_FINDER = register(new StorageFinder());
    public static final Module FPS = register(new Module(
            "FPS Goster", "Sol ustte FPS yazar.", Category.RENDER));
    public static final Module COORDS = register(new Module(
            "Koordinat Goster", "Sol ustte koordinatlari yazar.", Category.RENDER));

    // Hareket
    public static final AutoSprint AUTO_SPRINT = register(new AutoSprint());

    private static boolean nightVisionAdded = false;

    static {
        FPS.setEnabled(true);
        COORDS.setEnabled(true);
    }

    private ModuleManager() {}

    private static <T extends Module> T register(T module) {
        ALL.add(module);
        return module;
    }

    public static List<Module> getAll() {
        return ALL;
    }

    public static List<Module> getByCategory(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : ALL) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }

    /** Her oyun tikinde Client'tan cagrilir. */
    public static void tick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) {
            nightVisionAdded = false;
            return;
        }

        for (Module m : ALL) {
            if (m.isEnabled()) m.onTick(mc);
        }

        // Tam parlaklik ya da blok filtresi aciksa gece gorusu ver (sadece istemcide)
        boolean wantNightVision = FULLBRIGHT.isEnabled() || BlockFilter.active();
        if (wantNightVision) {
            if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NIGHT_VISION, 1_000_000, 0, false, false));
                nightVisionAdded = true;
            }
        } else if (nightVisionAdded) {
            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            nightVisionAdded = false;
        }
    }

    /** Chunk'lari yeniden olusturur, blok filtresi degisikligi hemen gorunsun. */
    public static void reloadWorld() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world != null && mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
