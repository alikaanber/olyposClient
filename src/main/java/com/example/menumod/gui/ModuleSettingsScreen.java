package com.example.menumod.gui;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Module;
import com.example.menumod.module.NumberSetting;
import com.example.menumod.module.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** Bir modulun ayarlarini gosterir. */
public class ModuleSettingsScreen extends Screen {
    private static final int W = 160;
    private static final int H = 20;
    private static final int GAP = 4;

    private final Module module;
    private final Screen parent;

    public ModuleSettingsScreen(Module module, Screen parent) {
        super(Text.literal(module.getName() + " Ayarlari"));
        this.module = module;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - W / 2;
        int y = 40;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BoolSetting bool) {
                this.addDrawableChild(ButtonWidget.builder(boolLabel(bool), b -> {
                    bool.value = !bool.value;
                    b.setMessage(boolLabel(bool));
                }).dimensions(x, y, W, H).build());
            } else if (setting instanceof NumberSetting number) {
                this.addDrawableChild(new NumberSlider(x, y, W, H, number));
            }
            y += H + GAP;
        }
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Geri"), b -> this.close())
                .dimensions(x, y + 8, W, H).build());
    }

    private static Text boolLabel(BoolSetting s) {
        return Text.literal(s.name + ": " + (s.value ? "ACIK" : "KAPALI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
