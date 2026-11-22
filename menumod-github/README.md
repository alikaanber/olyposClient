# Menu Mod / Olympus (Fabric, Minecraft 1.21.11)

Right Shift opens a categorized menu (Combat, Render, Movement, HUD). Every feature is its own module.

## Adding a module
1. Create a class in `module/impl/` that extends `Module` (onTick / onEnable / onDisable).
2. Use `addBool(...)` / `addNumber(...)` for settings; a "..." button appears in the menu automatically.
3. Add `public static final X X = register(new X());` to `ModuleManager.java`.

## Building
GitHub Actions (`.github/workflows/build.yml`) builds the jar: Actions tab -> `menumod-jar`.
