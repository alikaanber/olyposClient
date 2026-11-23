package com.example.menumod.module;

public enum Category {
    COMBAT("Combat"),
    FARM("Farm"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    HUD("HUD");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
