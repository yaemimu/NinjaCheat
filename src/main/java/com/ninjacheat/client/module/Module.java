package com.ninjacheat.client.module;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.setting.Setting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 全モジュールの基底クラス。
 * CheatUtils の Module インターフェース (マーカー) と FDPClient の Module 抽象クラスを
 * 統合した、有効/無効切替・設定保持・イベント購読を備えた実装。
 */
public abstract class Module {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();

    private boolean enabled;
    private int keyCode = GLFW.GLFW_KEY_UNKNOWN;
    private boolean drawn = true;   // ClickGUI に表示するか

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // ---- ライフサイクル ----

    /** モジュール有効化時に呼ばれる (サブクラスでオーバーライド) */
    protected void onEnable() {}

    /** モジュール無効化時に呼ばれる (サブクラスでオーバーライド) */
    protected void onDisable() {}

    /** ティック毎 (クライアント) に呼ばれる (サブクラスでオーバーライド) */
    public void onTick() {}

    /** 設定変更時に呼ばれる */
    protected void onSettingsChanged() {}

    // ---- 有効/無効 ----

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
            NinjaCheat.get().events().subscribe(this);
        } else {
            NinjaCheat.get().events().unsubscribe(this);
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    /** コンストラクタ内で初期有効状態を設定する (イベント購読は発火しない安全版)。
     *  registerDefaults() 後に events().subscribe を呼ぶ必要がある場合は別途呼ぶこと。 */
    protected void setInitialEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // ---- キーバインド ----

    public void setKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKey() {
        return keyCode;
    }

    public boolean hasKey() {
        return keyCode != GLFW.GLFW_KEY_UNKNOWN;
    }

    // ---- 表示 ----

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public boolean isDrawn() {
        return drawn;
    }

    // ---- 設定 ----

    protected <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    // ---- メタ情報 ----

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    /** ClickGUI / HUD で表示する識別子 (空白は除去) */
    public String getDisplayName() {
        return name;
    }

    @Override
    public String toString() {
        return name + (enabled ? " [ON]" : " [OFF]");
    }
}
