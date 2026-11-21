package com.example.menumod.module;

public enum Category {
    COMBAT("Savas"),
    RENDER("Gorsel"),
    MOVEMENT("Hareket");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
