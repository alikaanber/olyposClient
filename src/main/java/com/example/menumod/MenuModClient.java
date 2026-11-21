package com.example.menumod;

import com.example.menumod.gui.ClickGuiScreen;
import com.example.menumod.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.LoggerFactory;

public class MenuModClient implements ClientModInitializer {
    private static KeyBinding openMenuKey;
    private boolean rightShiftWasDown = false;

    @Override
    public void onInitializeClient() {
        LoggerFactory.getLogger("menumod").info("Menu Mod yuklendi - menu icin Sag Shift");

        // 1.21.9+ : KeyBinding artik bir Category nesnesi istiyor
        KeyBinding.Category category =
                KeyBinding.Category.create(Identifier.of("menumod", "main"));

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.menumod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Menuyu ac: tus ayari sistemi
            while (openMenuKey.wasPressed()) {
                openGui(client);
            }

            // Yedek yontem: Sag Shift'i dogrudan GLFW'den oku
            boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(),
                    GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !rightShiftWasDown) {
                openGui(client);
            }
            rightShiftWasDown = down;

            // Tum aktif moduller
            ModuleManager.tick(client);
        });

        // Ekranin sol ustune FPS / koordinat yaz
        HudElementRegistry.addLast(Identifier.of("menumod", "hud"), (context, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            int y = 4;
            if (ModuleManager.FPS.isEnabled()) {
                context.drawTextWithShadow(mc.textRenderer, mc.getCurrentFps() + " FPS", 4, y, 0xFFFFFFFF);
                y += 10;
            }
            if (ModuleManager.COORDS.isEnabled()) {
                String pos = String.format("XYZ: %.1f / %.1f / %.1f",
                        mc.player.getX(), mc.player.getY(), mc.player.getZ());
                context.drawTextWithShadow(mc.textRenderer, pos, 4, y, 0xFFFFFFFF);
            }
        });
    }

    private static void openGui(MinecraftClient client) {
        if (client.currentScreen == null && client.player != null) {
            client.setScreen(new ClickGuiScreen());
        }
    }
}
