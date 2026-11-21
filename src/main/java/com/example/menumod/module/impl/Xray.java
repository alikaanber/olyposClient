package com.example.menumod.module.impl;

import com.example.menumod.module.Category;
import com.example.menumod.module.Module;
import com.example.menumod.module.ModuleManager;

public class Xray extends Module {
    public Xray() {
        super("X-Ray", "Sadece cevherleri gosterir, diger bloklari gizler.", Category.RENDER);
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
