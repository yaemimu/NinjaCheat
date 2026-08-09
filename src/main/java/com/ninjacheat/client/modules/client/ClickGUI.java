package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import org.lwjgl.glfw.GLFW;

/**
 * ClickGUI — ClickGUI 画面を開くためのトグルモジュール。
 * MixinKeyboard が getKey() を参照して右Shiftで画面を開く。
 * FDPClient の ClickGUI モジュール統合。
 */
public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", "Opens the click GUI (Right-Shift)", Category.CLIENT);
        setKey(NinjaCheat.CLICK_GUI_KEY); // 340 = GLFW_KEY_RIGHT_SHIFT
        // デフォルト有効 (メニューを開くための「常時待機」モジュール)
        // ただし ON=待機状態、OFF=無効化 と解釈
        setInitialEnabled(true);
    }

    @Override
    public void toggle() {
        // ClickGUI 自体はトグルではなく画面を開く
        if (mc != null) {
            mc.setScreen(new com.ninjacheat.client.gui.ClickGUIScreen());
        }
    }

    @Override
    protected void onEnable() {
        // 何もしない (待機状態になるだけ)
    }

    @Override
    protected void onDisable() {
        // 何もしない
    }
}
