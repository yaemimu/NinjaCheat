package com.ninjacheat.client.hud;

import com.ninjacheat.client.module.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * HUD エレメントの基底クラス (Watermark, ArrayList 等)。
 */
public abstract class HudElement {

    private final String name;
    private boolean enabled = true;

    protected HudElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** 毎フレーム描画。戻り値は描画した高さ (次の要素のオフセット計算用) */
    public abstract int render(int x, int y, float tickDelta);

    /** HUD 設定画面での表示順重み (小さいほど上) */
    public int priority() {
        return 100;
    }
}
