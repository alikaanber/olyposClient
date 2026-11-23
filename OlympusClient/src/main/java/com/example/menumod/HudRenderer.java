package com.example.menumod;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Draws the HUD modules (watermark, FPS, coordinates, clock, module list). */
public final class HudRenderer {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final int ACCENT = 0xFFB07CFF;
    private static final int WHITE = 0xFFFFFFFF;

    private HudRenderer() {}

    public static void render(DrawContext context, MinecraftClient mc) {
        if (mc.player == null || mc.options.hudHidden) return;
        TextRenderer tr = mc.textRenderer;

        // Top left
        int y = 4;
        if (ModuleManager.WATERMARK.isEnabled()) {
            context.drawTextWithShadow(tr, "Olympus Client", 4, y, ACCENT);
            y += 11;
        }
        if (ModuleManager.FPS.isEnabled()) {
            context.drawTextWithShadow(tr, mc.getCurrentFps() + " FPS", 4, y, WHITE);
            y += 10;
        }
        if (ModuleManager.COORDS.isEnabled()) {
            String pos = String.format("XYZ: %.1f / %.1f / %.1f",
                    mc.player.getX(), mc.player.getY(), mc.player.getZ());
            context.drawTextWithShadow(tr, pos, 4, y, WHITE);
            y += 10;
        }
        if (ModuleManager.CLOCK.isEnabled()) {
            context.drawTextWithShadow(tr, LocalTime.now().format(TIME), 4, y, WHITE);
        }

        // Top right: enabled modules, longest name first
        if (ModuleManager.MODULE_LIST.isEnabled()) {
            List<Module> active = new ArrayList<>();
            for (Module m : ModuleManager.getAll()) {
                if (m.isEnabled() && m.getCategory() != Category.HUD) active.add(m);
            }
            active.sort((a, b) -> tr.getWidth(b.getName()) - tr.getWidth(a.getName()));

            int screenW = mc.getWindow().getScaledWidth();
            int ry = 4;
            for (Module m : active) {
                int w = tr.getWidth(m.getName());
                context.drawTextWithShadow(tr, m.getName(), screenW - w - 4, ry, ACCENT);
                ry += 10;
            }
        }
    }
}
