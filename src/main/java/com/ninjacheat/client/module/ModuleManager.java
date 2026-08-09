package com.ninjacheat.client.module;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全モジュールを登録・管理する中央レジストリ。
 * CheatUtils の Modules シングルトンと FDPClient の ModuleManager を統合。
 */
public class ModuleManager {

    private final Map<String, Module> modules = new LinkedHashMap<>();

    /** モジュールを登録 */
    public void register(Module module) {
        if (modules.containsKey(module.getName().toLowerCase())) {
            throw new IllegalStateException("Duplicate module name: " + module.getName());
        }
        modules.put(module.getName().toLowerCase(), module);
    }

    /** 名前(大文字小文字無視) で取得 */
    public Module get(String name) {
        return modules.get(name.toLowerCase());
    }

    /** 型で取得 (初回) */
    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> type) {
        for (Module m : modules.values()) {
            if (type.isInstance(m)) {
                return (T) m;
            }
        }
        return null;
    }

    public Collection<Module> all() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public int size() {
        return modules.size();
    }

    /** カテゴリ別に分類して取得 */
    public Map<Category, List<Module>> byCategory() {
        return modules.values().stream()
                .collect(Collectors.groupingBy(Module::getCategory, LinkedHashMap::new, Collectors.toList()));
    }

    /** 現在有効なモジュール一覧 (HUD array 用) */
    public List<Module> enabled() {
        return modules.values().stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
    }

    /** デフォルトの全モジュールを登録 (Client 初期化時に呼ばれる) */
    public void registerDefaults() {
        // Combat
        register(new com.ninjacheat.client.modules.combat.KillAura());
        register(new com.ninjacheat.client.modules.combat.Criticals());
        register(new com.ninjacheat.client.modules.combat.AutoClicker());
        register(new com.ninjacheat.client.modules.combat.Velocity());
        register(new com.ninjacheat.client.modules.combat.HitBox());
        register(new com.ninjacheat.client.modules.combat.Reach());
        register(new com.ninjacheat.client.modules.combat.AutoCrystal());
        register(new com.ninjacheat.client.modules.combat.Surround());

        // Movement
        register(new com.ninjacheat.client.modules.movement.Fly());
        register(new com.ninjacheat.client.modules.movement.Speed());
        register(new com.ninjacheat.client.modules.movement.NoFall());
        register(new com.ninjacheat.client.modules.movement.Scaffold());
        register(new com.ninjacheat.client.modules.movement.Step());
        register(new com.ninjacheat.client.modules.movement.Jesus());
        register(new com.ninjacheat.client.modules.movement.Spider());
        register(new com.ninjacheat.client.modules.movement.HighJump());
        register(new com.ninjacheat.client.modules.movement.InvMove());
        register(new com.ninjacheat.client.modules.movement.Strafe());
        register(new com.ninjacheat.client.modules.movement.AntiVoid());

        // Render
        register(new com.ninjacheat.client.modules.render.ESP());
        register(new com.ninjacheat.client.modules.render.Tracers());
        register(new com.ninjacheat.client.modules.render.FullBright());
        register(new com.ninjacheat.client.modules.render.NameTags());
        register(new com.ninjacheat.client.modules.render.FreeCam());
        register(new com.ninjacheat.client.modules.render.XRay());
        register(new com.ninjacheat.client.modules.render.Chams());
        register(new com.ninjacheat.client.modules.render.Zoom());
        register(new com.ninjacheat.client.modules.render.HoleESP());
        register(new com.ninjacheat.client.modules.render.StorageESP());
        register(new com.ninjacheat.client.modules.render.NoRender());
        register(new com.ninjacheat.client.modules.render.Trajectories());
        register(new com.ninjacheat.client.modules.render.LogoutSpots());

        // Player
        register(new com.ninjacheat.client.modules.player.AutoEat());
        register(new com.ninjacheat.client.modules.player.AutoTool());
        register(new com.ninjacheat.client.modules.player.AutoTotem());
        register(new com.ninjacheat.client.modules.player.NoRotate());
        register(new com.ninjacheat.client.modules.player.FastUse());
        register(new com.ninjacheat.client.modules.player.AutoArmor());
        register(new com.ninjacheat.client.modules.player.Regen());

        // World
        register(new com.ninjacheat.client.modules.world.Nuker());
        register(new com.ninjacheat.client.modules.world.FastBreak());
        register(new com.ninjacheat.client.modules.world.FastPlace());
        register(new com.ninjacheat.client.modules.world.Timer());
        register(new com.ninjacheat.client.modules.world.AutoFish());
        register(new com.ninjacheat.client.modules.world.AirPlace());

        // Exploit
        register(new com.ninjacheat.client.modules.exploit.Disabler());
        register(new com.ninjacheat.client.modules.exploit.PingSpoof());
        register(new com.ninjacheat.client.modules.exploit.Blink());
        register(new com.ninjacheat.client.modules.exploit.Phase());
        register(new com.ninjacheat.client.modules.exploit.GhostHand());

        // Other
        register(new com.ninjacheat.client.modules.other.ChestStealer());
        register(new com.ninjacheat.client.modules.other.MiddleClick());
        register(new com.ninjacheat.client.modules.other.AntiAFK());
        register(new com.ninjacheat.client.modules.other.AutoSoup());

        // Client
        register(new com.ninjacheat.client.modules.client.ClickGUI());
        register(new com.ninjacheat.client.modules.client.Hud());
        register(new com.ninjacheat.client.modules.client.AntiBot());
        register(new com.ninjacheat.client.modules.client.StaffDetector());
        register(new com.ninjacheat.client.modules.client.DiscordRPC());
        register(new com.ninjacheat.client.modules.client.Macros());
    }
}
