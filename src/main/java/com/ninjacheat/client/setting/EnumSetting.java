package com.ninjacheat.client.setting;

import java.util.List;

/**
 * 列挙型設定 (ドロップダウン / サイクルボタン)。
 * FDPClient の ListValue 相当。
 */
public class EnumSetting extends Setting<String> {

    private final List<String> options;

    public EnumSetting(String name, String description, String defaultValue, List<String> options) {
        super(name, description, defaultValue);
        if (!options.contains(defaultValue)) {
            throw new IllegalArgumentException("Default value not in options: " + defaultValue);
        }
        this.options = options;
    }

    public EnumSetting(String name, String description, String defaultValue, String... options) {
        this(name, description, defaultValue, java.util.Arrays.asList(options));
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public void set(String value) {
        if (!options.contains(value)) return;
        super.set(value);
    }

    public void cycle() {
        int idx = options.indexOf(this.value);
        super.set(options.get((idx + 1) % options.size()));
    }

    @Override
    public String getType() {
        return "enum";
    }
}
