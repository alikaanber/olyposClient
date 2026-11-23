package com.example.menumod.gui;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import com.example.menumod.module.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** Shows the settings of one module. */
public class ModuleSettingsScreen extends Screen {
    private static final int W = 160;
    private static final int H = 20;
    private static final int GAP = 4;

    private final Module module;
    private final Screen parent;

    public ModuleSettingsScreen(Module module, Screen parent) {
        super(Text.literal(module.getName() + " Settings"));
        this.module = module;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int count = module.getSettings().size();
        int rowH = Math.max(16, Math.min(H, (this.height - 90) / Math.max(1, count + 1) - GAP));
        int x = this.width / 2 - W / 2;
        int y = 40;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BoolSetting bool) {
                this.addDrawableChild(ButtonWidget.builder(boolLabel(bool), b -> {
                    bool.value = !bool.value;
                    b.setMessage(boolLabel(bool));
                }).dimensions(x, y, W, rowH).build());
            } else if (setting instanceof NumberSetting number) {
                this.addDrawableChild(new NumberSlider(x, y, W, rowH, number));
            }
            y += rowH + GAP;
        }
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), b -> this.close())
                .dimensions(x, y + 8, W, rowH).build());
    }

    private static Text boolLabel(BoolSetting s) {
        return Text.literal(s.name + ": " + (s.value ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFB07CFF);
    }

    @Override
    public void close() {
        module.onSettingsClosed();
        this.client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
