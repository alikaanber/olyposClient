package com.example.menumod.gui;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

/** Ana menu: ustte kategori sekmeleri, altta o kategorinin modulleri. */
public class ClickGuiScreen extends Screen {
    private static final int TAB_W = 70;
    private static final int TAB_H = 20;
    private static final int ROW_W = 140;
    private static final int ROW_H = 20;
    private static final int SETTINGS_W = 20;
    private static final int GAP = 4;

    private static Category selected = Category.COMBAT;

    public ClickGuiScreen() {
        super(Text.literal("Menu"));
    }

    @Override
    protected void init() {
        // Kategori sekmeleri
        Category[] cats = Category.values();
        int tabsWidth = cats.length * TAB_W + (cats.length - 1) * GAP;
        int tabX = this.width / 2 - tabsWidth / 2;
        int tabY = 30;
        for (Category cat : cats) {
            final Category c = cat;
            Text label = Text.literal((c == selected ? "> " : "") + c.getDisplayName());
            this.addDrawableChild(ButtonWidget.builder(label, b -> {
                selected = c;
                this.clearAndInit();
            }).dimensions(tabX, tabY, TAB_W, TAB_H).build());
            tabX += TAB_W + GAP;
        }

        // Secili kategorinin modulleri
        List<Module> modules = ModuleManager.getByCategory(selected);
        int totalW = ROW_W + GAP + SETTINGS_W;
        int x = this.width / 2 - totalW / 2;
        int y = tabY + TAB_H + 12;
        for (Module module : modules) {
            ButtonWidget toggle = ButtonWidget.builder(label(module), b -> {
                module.toggle();
                b.setMessage(label(module));
            }).dimensions(x, y, ROW_W, ROW_H).build();
            toggle.setTooltip(Tooltip.of(Text.literal(module.getDescription())));
            this.addDrawableChild(toggle);

            if (!module.getSettings().isEmpty()) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal("..."), b ->
                        this.client.setScreen(new ModuleSettingsScreen(module, this))
                ).dimensions(x + ROW_W + GAP, y, SETTINGS_W, ROW_H).build());
            }
            y += ROW_H + GAP;
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> this.close())
                .dimensions(this.width / 2 - ROW_W / 2, y + 8, ROW_W, ROW_H).build());
    }

    private static Text label(Module module) {
        return Text.literal(module.getName() + ": " + (module.isEnabled() ? "ACIK" : "KAPALI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
