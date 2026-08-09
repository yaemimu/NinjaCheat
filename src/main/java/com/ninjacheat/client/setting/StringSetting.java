package com.ninjacheat.client.setting;

/**
 * 文字列設定 (テキスト入力)。
 * Spammer / Macros などで使用。
 */
public class StringSetting extends Setting<String> {

    private final int maxLength;

    public StringSetting(String name, String description, String defaultValue, int maxLength) {
        super(name, description, defaultValue);
        this.maxLength = maxLength;
    }

    public StringSetting(String name, String description, String defaultValue) {
        this(name, description, defaultValue, 256);
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public String getType() {
        return "string";
    }
}
