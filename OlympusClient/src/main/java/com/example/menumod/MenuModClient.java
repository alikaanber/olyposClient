package com.example.menumod;

import com.example.menumod.gui.ClickGuiScreen;
import com.example.menumod.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
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
        LoggerFactory.getLogger("menumod").info("Menu Mod loaded - press Right Shift for the menu");

        // 1.21.9+ : KeyBinding needs a Category object
        KeyBinding.Category category =
                KeyBinding.Category.create(Identifier.of("menumod", "main"));

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.menumod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean handled = false;

            // Open via the key binding system
            while (openMenuKey.wasPressed()) {
                handled |= openGui(client);
            }

            // Backup: read Right Shift straight from GLFW. Also closes the menu.
            boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(),
                    GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !rightShiftWasDown && !handled) {
                if (client.currentScreen == null) {
                    openGui(client);
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.setScreen(null);
                }
            }
            rightShiftWasDown = down;

            ModuleManager.tick(client);
        });

        // Restore the FOV if the game is closed while Zoom is on
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ModuleManager.ZOOM.setEnabled(false));

        HudElementRegistry.addLast(Identifier.of("menumod", "hud"),
                (context, tickCounter) -> HudRenderer.render(context, MinecraftClient.getInstance()));
    }

    private static boolean openGui(MinecraftClient client) {
        if (client.currentScreen == null && client.player != null) {
            client.setScreen(new ClickGuiScreen());
            return true;
        }
        return false;
    }
}
