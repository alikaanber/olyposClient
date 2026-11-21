package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;

public class StorageFinder extends Module {
    public StorageFinder() {
        super("Depolama Finder", "Sandik, varil, shulker gibi depolama bloklarini gosterir.", Category.RENDER);
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
