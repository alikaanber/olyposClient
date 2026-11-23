package com.example.menumod.module;

import com.example.menumod.module.impl.AutoFish;
import com.example.menumod.module.impl.AutoSprint;
import com.example.menumod.module.impl.AutoTotem;
import com.example.menumod.module.impl.CropFarm;
import com.example.menumod.module.impl.Esp;
import com.example.menumod.module.impl.MobFarm;
import com.example.menumod.module.impl.NetheriteFinder;
import com.example.menumod.module.impl.StorageFinder;
import com.example.menumod.module.impl.TreeFarm;
import com.example.menumod.module.impl.Xray;
import com.example.menumod.module.impl.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.ArrayList;
import java.util.List;

/** Registry of all modules. Add new modules here with register(...). */
public final class ModuleManager {
    // NOTE: ALL must be declared before the fields that call register(...).
    private static final List<Module> ALL = new ArrayList<>();

    // Combat
    public static final AutoTotem AUTO_TOTEM = register(new AutoTotem());

    // Farm
    public static final MobFarm MOB_FARM = register(new MobFarm());
    public static final CropFarm CROP_FARM = register(new CropFarm());
    public static final TreeFarm TREE_FARM = register(new TreeFarm());
    public static final AutoFish AUTO_FISH = register(new AutoFish());

    // Render
    public static final Esp ESP = register(new Esp());
    public static final Module FULLBRIGHT = register(new Module(
            "Fullbright", "Makes everything bright, even in the dark.", Category.RENDER));
    public static final Zoom ZOOM = register(new Zoom());
    public static final Xray XRAY = register(new Xray());
    public static final NetheriteFinder NETHERITE_FINDER = register(new NetheriteFinder());
    public static final StorageFinder STORAGE_FINDER = register(new StorageFinder());

    // Movement
    public static final AutoSprint AUTO_SPRINT = register(new AutoSprint());

    // HUD (drawn by HudRenderer)
    public static final Module WATERMARK = register(new Module(
            "Watermark", "Shows the client name in the top left.", Category.HUD));
    public static final Module FPS = register(new Module(
            "FPS", "Shows your frames per second.", Category.HUD));
    public static final Module COORDS = register(new Module(
            "Coordinates", "Shows your XYZ position.", Category.HUD));
    public static final Module CLOCK = register(new Module(
            "Clock", "Shows the real time.", Category.HUD));
    public static final Module MODULE_LIST = register(new Module(
            "Module List", "Lists enabled modules on the right side.", Category.HUD));

    private static boolean nightVisionAdded = false;

    static {
        WATERMARK.setEnabled(true);
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

    /** Called every game tick from the client entrypoint. */
    public static void tick(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) {
            nightVisionAdded = false;
            return;
        }

        for (Module m : ALL) {
            if (m.isEnabled()) m.onTick(mc);
        }

        // Fullbright or a block filter needs night vision (client side only)
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

    /** Rebuilds the chunks so a block filter change shows up immediately. */
    public static void reloadWorld() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world != null && mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }
}
