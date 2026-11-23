# Olympus Client (Fabric, Minecraft 1.21.11)

Right Shift opens a categorized menu (Combat, Farm, Render, Movement, HUD). Every feature is its own module.

## Adding a module
1. Create a class in `module/impl/` that extends `Module` (onTick / onEnable / onDisable).
2. Use `addBool(...)` / `addNumber(...)` for settings; a "..." button appears in the menu automatically.
3. Add `public static final X X = register(new X());` to `ModuleManager.java`.
4. To add a category, add it to `Category.java`; empty categories are hidden.

## Building
GitHub Actions (`.github/workflows/build.yml`) builds the jar: Actions tab -> `menumod-jar` -> `olympus-client-1.0.0.jar`.

## License
MIT
