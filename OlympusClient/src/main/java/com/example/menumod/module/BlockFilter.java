package com.example.menumod.module;

/** Shared block filter of X-Ray, Netherite Finder and Storage Finder (used by BlockMixin). */
public final class BlockFilter {
    private BlockFilter() {}

    public static boolean active() {
        return ModuleManager.XRAY.isEnabled()
                || ModuleManager.NETHERITE_FINDER.isEnabled()
                || ModuleManager.STORAGE_FINDER.isEnabled();
    }

    /** @param path path part of the block id, e.g. "diamond_ore" */
    public static boolean shouldShow(String path) {
        if (ModuleManager.XRAY.isEnabled() && ModuleManager.XRAY.shows(path)) {
            return true;
        }
        if (ModuleManager.NETHERITE_FINDER.isEnabled() && path.equals("ancient_debris")) {
            return true;
        }
        return ModuleManager.STORAGE_FINDER.isEnabled() && isStorage(path);
    }

    private static boolean isStorage(String path) {
        return path.equals("chest") || path.equals("trapped_chest") || path.equals("barrel")
                || path.equals("ender_chest") || path.equals("hopper") || path.equals("dispenser")
                || path.equals("dropper") || path.endsWith("shulker_box");
    }
}
