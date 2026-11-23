package com.example.menumod.module.impl;

import com.example.menumod.module.BoolSetting;
import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;

/** Shows only the ores you pick and hides every other block. */
public class Xray extends Module {
    private final BoolSetting coal = addBool("Coal", true);
    private final BoolSetting iron = addBool("Iron", true);
    private final BoolSetting copper = addBool("Copper", true);
    private final BoolSetting gold = addBool("Gold", true);
    private final BoolSetting redstone = addBool("Redstone", true);
    private final BoolSetting lapis = addBool("Lapis", true);
    private final BoolSetting diamond = addBool("Diamond", true);
    private final BoolSetting emerald = addBool("Emerald", true);
    private final BoolSetting quartz = addBool("Nether Quartz", true);
    private final BoolSetting debris = addBool("Ancient Debris", true);

    public Xray() {
        super("X-Ray", "Shows only the selected ores and hides all other blocks.", Category.RENDER);
    }

    /** @param path block id path, e.g. "deepslate_diamond_ore" */
    public boolean shows(String path) {
        if (path.equals("ancient_debris")) return debris.value;
        if (path.endsWith("coal_ore")) return coal.value;
        if (path.endsWith("iron_ore")) return iron.value;
        if (path.endsWith("copper_ore")) return copper.value;
        if (path.endsWith("gold_ore")) return gold.value;
        if (path.endsWith("redstone_ore")) return redstone.value;
        if (path.endsWith("lapis_ore")) return lapis.value;
        if (path.endsWith("diamond_ore")) return diamond.value;
        if (path.endsWith("emerald_ore")) return emerald.value;
        if (path.endsWith("quartz_ore")) return quartz.value;
        return false;
    }

    @Override
    protected void onEnable() {
        ModuleManager.reloadWorld();
    }

    @Override
    protected void onDisable() {
        ModuleManager.reloadWorld();
    }

    @Override
    public void onSettingsClosed() {
        if (isEnabled()) ModuleManager.reloadWorld();
    }
}
