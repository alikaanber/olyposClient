package com.example.menumod.gui;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu: one dark panel per category. Panels wrap into several rows when the window is narrow.
 * Click a module to toggle it, click "..." to open its settings.
 *
 * The real (invisible) vanilla buttons handle clicks; a flat overlay is drawn on top of them.
 */
public class ClickGuiScreen extends Screen {
    private static final int PANEL_W = 112;
    private static final int PANEL_GAP = 8;
    private static final int MARGIN = 10;
    private static final int HEADER_H = 18;
    private static final int SET_W = 18;
    private static final int PAD = 2;
    private static final int TOP = 30;

    private static final int PANEL_BG = 0xD0101018;
    private static final int HEADER_BG = 0xFF6A3BC8;
    private static final int ROW_BASE = 0xFF1B1B26;
    private static final int ROW_HOVER = 0xFF2B2B3B;
    private static final int ROW_ON = 0xFF5B34B0;
    private static final int ROW_ON_HOVER = 0xFF7048D0;
    private static final int ACCENT = 0xFFB07CFF;

    private record Row(Module module, int x, int y, int w, int h, int setX, int setW) {}

    private final List<Row> rows = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.literal("Olympus Client"));
    }

    @Override
    protected void init() {
        rows.clear();

        List<Category> cats = new ArrayList<>();
        for (Category c : Category.values()) {
            if (!ModuleManager.getByCategory(c).isEmpty()) cats.add(c);
        }

        int rowH = this.height >= 380 ? 16 : 14;
        int perRow = Math.max(1, (this.width - 2 * MARGIN + PANEL_GAP) / (PANEL_W + PANEL_GAP));
        perRow = Math.min(perRow, Math.max(1, cats.size()));

        int cursorY = TOP;
        for (int start = 0; start < cats.size(); start += perRow) {
            int end = Math.min(cats.size(), start + perRow);
            int count = end - start;
            int rowTotalW = count * PANEL_W + (count - 1) * PANEL_GAP;
            int x0 = this.width / 2 - rowTotalW / 2;
            int maxH = 0;

            for (int i = start; i < end; i++) {
                Category cat = cats.get(i);
                List<Module> modules = ModuleManager.getByCategory(cat);
                int px = x0 + (i - start) * (PANEL_W + PANEL_GAP);
                int top = cursorY;
                int panelH = HEADER_H + PAD + modules.size() * (rowH + 1) + PAD;
                String title = cat.getDisplayName();
                maxH = Math.max(maxH, panelH);

                // Panel background + header
                this.addDrawable((ctx, mx, my, d) -> {
                    ctx.fill(px, top, px + PANEL_W, top + panelH, PANEL_BG);
                    ctx.fill(px, top, px + PANEL_W, top + HEADER_H, HEADER_BG);
                    ctx.drawTextWithShadow(this.textRenderer, title, px + 6, top + 5, 0xFFFFFFFF);
                });

                int y = top + HEADER_H + PAD;
                int innerX = px + PAD;
                int innerW = PANEL_W - PAD * 2;

                for (Module module : modules) {
                    boolean hasSettings = !module.getSettings().isEmpty();
                    int modW = hasSettings ? innerW - SET_W - 1 : innerW;

                    ButtonWidget toggle = ButtonWidget.builder(Text.empty(), b -> module.toggle())
                            .dimensions(innerX, y, modW, rowH).build();
                    toggle.setTooltip(Tooltip.of(Text.literal(module.getDescription())));
                    this.addDrawableChild(toggle);

                    int setX = 0;
                    int setW = 0;
                    if (hasSettings) {
                        setX = innerX + modW + 1;
                        setW = SET_W;
                        this.addDrawableChild(ButtonWidget.builder(Text.empty(),
                                        b -> this.client.setScreen(new ModuleSettingsScreen(module, this)))
                                .dimensions(setX, y, setW, rowH).build());
                    }

                    rows.add(new Row(module, innerX, y, modW, rowH, setX, setW));
                    y += rowH + 1;
                }
            }
            cursorY += maxH + PANEL_GAP;
        }

        // Flat overlay drawn on top of the buttons
        this.addDrawable((ctx, mx, my, d) -> {
            for (Row r : rows) {
                boolean on = r.module().isEnabled();
                boolean hover = inside(mx, my, r.x(), r.y(), r.w(), r.h());
                int color = on ? (hover ? ROW_ON_HOVER : ROW_ON) : (hover ? ROW_HOVER : ROW_BASE);
                ctx.fill(r.x(), r.y(), r.x() + r.w(), r.y() + r.h(), color);
                if (on) {
                    ctx.fill(r.x(), r.y(), r.x() + 2, r.y() + r.h(), ACCENT);
                }
                String label = this.textRenderer.trimToWidth(r.module().getName(), r.w() - 8);
                ctx.drawTextWithShadow(this.textRenderer, label,
                        r.x() + 6, r.y() + (r.h() - 8) / 2, on ? 0xFFFFFFFF : 0xFFA8A8B8);

                if (r.setW() > 0) {
                    boolean setHover = inside(mx, my, r.setX(), r.y(), r.setW(), r.h());
                    ctx.fill(r.setX(), r.y(), r.setX() + r.setW(), r.y() + r.h(),
                            setHover ? ROW_HOVER : ROW_BASE);
                    ctx.drawTextWithShadow(this.textRenderer, "...",
                            r.setX() + 4, r.y() + (r.h() - 8) / 2, setHover ? 0xFFFFFFFF : 0xFFA8A8B8);
                }
            }
        });
    }

    private static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, ACCENT);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Right Shift: close   |   ... : module settings"),
                this.width / 2, this.height - 14, 0xFF808090);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
