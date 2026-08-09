package com.ninjacheat.client;

/**
 * NinjaCheat のグローバル定数。
 * 参照した 3 つのクライアント (CheatUtils の Constants, FDPClient の ClientInfo,
 * FullBright の addon.json) の慣例を統合したもの。
 */
public final class NinjaCheat {

    /** mod id (fabric.mod.json と同一にすること) */
    public static final String MOD_ID = "ninjacheat";
    /** 表示名 */
    public static final String MOD_NAME = "NinjaCheat";
    /** バージョン */
    public static final String MOD_VERSION = "1.0.0";

    /** ClickGUI を開くキーコード (右Shift) のデフォルト */
    public static final int CLICK_GUI_KEY = 340;   // GLFW_KEY_RIGHT_SHIFT
    /** コマンド接頭辞 */
    public static final String COMMAND_PREFIX = ".";

    /** シングルトンインスタンス (クライアント初期化時に設定) */
    private static NinjaCheat INSTANCE;

    private final ModuleManager moduleManager;
    private final EventManager eventManager;
    private final CommandManager commandManager;
    private final HudManager hudManager;
    private final ConfigManager configManager;

    public NinjaCheat() {
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.hudManager = new HudManager();
        this.configManager = new ConfigManager();
    }

    public static NinjaCheat get() {
        return INSTANCE;
    }

    static void setInstance(NinjaCheat instance) {
        INSTANCE = instance;
    }

    public ModuleManager modules() {
        return moduleManager;
    }

    public EventManager events() {
        return eventManager;
    }

    public CommandManager commands() {
        return commandManager;
    }

    public HudManager hud() {
        return hudManager;
    }

    public ConfigManager config() {
        return configManager;
    }
}
