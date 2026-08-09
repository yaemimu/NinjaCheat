package com.ninjacheat.client.config;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 設定 (モジュール有効状態・キー・各設定値) を JSON に保存/読込する。
 * config ファイルは <game-dir>/config/ninjacheat.json に置く。
 * CheatUtils の ConfigProfileManager / FDPClient の Value の永続化を統合。
 */
public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("NinjaCheat/Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Path path;

    public ConfigManager() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.runDirectories != null && !mc.runDirectories.isEmpty()) {
            path = mc.runDirectories.get(0).toPath().resolve("config").resolve(NinjaCheat.MOD_ID + ".json");
        } else {
            path = Path.of("config", NinjaCheat.MOD_ID + ".json");
        }
    }

    public Path getPath() {
        return path;
    }

    public void save() {
        try {
            if (NinjaCheat.get() == null) return;
            JsonObject root = new JsonObject();
            JsonObject modules = new JsonObject();
            for (Module m : NinjaCheat.get().modules().all()) {
                JsonObject entry = new JsonObject();
                entry.addProperty("enabled", m.isEnabled());
                entry.addProperty("key", m.getKey());
                entry.addProperty("drawn", m.isDrawn());
                JsonObject settings = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    settings.addProperty(s.getName(), String.valueOf(s.get()));
                }
                entry.add("settings", settings);
                modules.add(m.getName(), entry);
            }
            root.add("modules", modules);
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(root));
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    public void load() {
        try {
            if (!Files.exists(path) || NinjaCheat.get() == null) return;
            JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            if (!root.has("modules")) return;
            JsonObject modules = root.getAsJsonObject("modules");
            for (Module m : NinjaCheat.get().modules().all()) {
                if (!modules.has(m.getName())) continue;
                JsonObject entry = modules.getAsJsonObject(m.getName());
                if (entry.has("key")) m.setKey(entry.get("key").getAsInt());
                if (entry.has("drawn")) m.setDrawn(entry.get("drawn").getAsBoolean());
                if (entry.has("settings") && entry.get("settings").isJsonObject()) {
                    JsonObject settings = entry.getAsJsonObject("settings");
                    for (Setting<?> s : m.getSettings()) {
                        if (settings.has(s.getName())) {
                            applySetting(s, settings.get(s.getName()).getAsString());
                        }
                    }
                }
                // ClickGUI / Hud はデフォルト有効のままにする
                if (entry.has("enabled") && entry.get("enabled").getAsBoolean()) {
                    if (!(m instanceof com.ninjacheat.client.modules.client.ClickGUI)) {
                        m.setEnabled(true);
                    }
                }
            }
            LOGGER.info("Config loaded from {}", path);
        } catch (Exception e) {
            LOGGER.error("Failed to load config", e);
        }
    }

    public void reset() {
        for (Module m : NinjaCheat.get().modules().all()) {
            if (m.isEnabled() && !(m instanceof com.ninjacheat.client.modules.client.ClickGUI)) {
                m.setEnabled(false);
            }
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void applySetting(Setting<?> setting, String raw) {
        try {
            switch (setting.getType()) {
                case "bool" -> ((Setting<Boolean>) setting).set(Boolean.parseBoolean(raw));
                case "number" -> ((Setting<Double>) setting).set(Double.parseDouble(raw));
                case "enum", "string" -> ((Setting<String>) setting).set(raw);
                case "color" -> ((Setting<Integer>) setting).set((int) Long.parseLong(raw.replace("#", ""), 16));
            }
        } catch (Exception ignored) {}
    }
}
