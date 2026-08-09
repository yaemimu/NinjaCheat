package com.ninjacheat.client.setting;

/**
 * 真偽値設定 (トグル)。
 */
public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public String getType() {
        return "bool";
    }
}
