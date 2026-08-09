package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import com.ninjacheat.client.setting.NumberSetting;
import org.lwjgl.glfw.GLFW;

/**
 * FullBright — 暗所を明るくする。
 * ghhe4vgy.jar (L3nnart_ 氏の FullBright) の中核機能を再現 + 拡張。
 * Gamma 上書きモード と Lightmap 強制モードを選べる。
 */
public class FullBright extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Brightness method", "Gamma", "Gamma", "Lightmap", "Potion"));
    private final NumberSetting gamma = addSetting(new NumberSetting("Gamma Level", "Brightness level when on", 15.0, 1.0, 30.0, 0.5));

    public FullBright() {
        super("FullBright", "Removes darkness / max brightness (inspired by L3nnart_ FullBright)", Category.RENDER);
        setKey(GLFW.GLFW_KEY_G);
    }

    /** MixinSimpleOption が gamma を上書きするのに使う値 */
    public double getGammaOverride() {
        return gamma.get();
    }

    /** MixinLightmapTextureManager が完全明るさを使うか */
    public boolean isFullBrightMode() {
        return "Lightmap".equals(mode.get());
    }

    @Override
    protected void onEnable() {
        // ポーションモードなら夜視ポーション相当のフラグを立てる (簡易版)
        if ("Potion".equals(mode.get()) && mc.player != null) {
            // 本格実装では StatusEffectInstance をクライアント側で付与
        }
    }
}
