package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;

public class NetheriteFinder extends Module {
    public NetheriteFinder() {
        super("Netherite Finder", "Shows only ancient debris and hides other blocks.", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        ModuleManager.reloadWorld();
    }

    @Override
    protected void onDisable() {
        ModuleManager.reloadWorld();
    }
}
