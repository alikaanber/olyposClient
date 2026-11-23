package com.example.menumod.gui;

import com.example.menumod.module.NumberSetting;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** NumberSetting icin kaydirma cubugu. */
public class NumberSlider extends SliderWidget {
    private final NumberSetting setting;

    public NumberSlider(int x, int y, int width, int height, NumberSetting setting) {
        super(x, y, width, height, Text.empty(),
                (setting.value - setting.min) / (setting.max - setting.min));
        this.setting = setting;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (setting == null) return; // ust sinif kurucusu bunu erken cagirirsa
        this.setMessage(Text.literal(setting.name + ": " + String.format("%.1f", setting.value)));
    }

    @Override
    protected void applyValue() {
        double raw = setting.min + this.value * (setting.max - setting.min);
        setting.value = Math.round(raw * 10.0) / 10.0;
    }
}
