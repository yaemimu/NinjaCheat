package com.ninjacheat.client.modules.client;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import org.lwjgl.glfw.GLFW;

/**
 * Hud — HUD の表示/非表示をトグル。
 * MixinInGameHud が isEnabled() を参照して HUD 描画を制御。
 * FDPClient の HUD 統合。
 */
public class Hud extends Module {

    public Hud() {
        super("Hud", "Toggles the on-screen HUD overlay", Category.CLIENT);
        setKey(GLFW.GLFW_KEY_H);
        // デフォルト有効
        setInitialEnabled(true);
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
