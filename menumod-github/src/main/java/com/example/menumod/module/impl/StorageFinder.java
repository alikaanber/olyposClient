package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;

public class StorageFinder extends Module {
    public StorageFinder() {
        super("Storage Finder", "Shows chests, barrels, shulkers and other storage blocks.", Category.RENDER);
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
