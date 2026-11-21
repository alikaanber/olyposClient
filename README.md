# Menu Mod (Fabric, Minecraft 1.21.11)

Sag Shift ile acilan, kategorili menu. Her ozellik ayri bir modul.

## Yeni modul eklemek
1. `module/impl/` altinda `Module`'u extend eden bir sinif yaz (onTick / onEnable / onDisable).
2. Ayar eklemek icin `addBool(...)` / `addNumber(...)` kullan, menude otomatik "..." dugmesi cikar.
3. `ModuleManager.java` icine `public static final X X = register(new X());` satirini ekle.

## Derleme
GitHub Actions (`.github/workflows/build.yml`) jar'i derler: Actions sekmesi -> `menumod-jar`.
