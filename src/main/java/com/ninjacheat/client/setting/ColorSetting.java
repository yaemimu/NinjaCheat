package com.ninjacheat.client.setting;

/**
 * 色設定 (ARGB)。
 * Render 系モジュール (ESP / Tracers / HoleESP など) で多用する。
 */
public class ColorSetting extends Setting<Integer> {

    private final boolean allowAlpha;

    public ColorSetting(String name, String description, int defaultArgb, boolean allowAlpha) {
        super(name, description, defaultArgb);
        this.allowAlpha = allowAlpha;
    }

    public ColorSetting(String name, String description, int defaultArgb) {
        this(name, description, defaultArgb, true);
    }

    public boolean allowsAlpha() {
        return allowAlpha;
    }

    public int getRgb() {
        return value & 0x00FFFFFF;
    }

    public int getAlpha() {
        return (value >> 24) & 0xFF;
    }

    @Override
    public String getType() {
        return "color";
    }
}
