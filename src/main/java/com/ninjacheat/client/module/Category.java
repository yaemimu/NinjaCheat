package com.ninjacheat.client.module;

/**
 * モジュールのカテゴリ。
 * FDPClient の Category enum をベースにしつつ、CheatUtils のパッケージ分類
 * (automation/esp/hacks/visuals/utilities) の概念も取り入れた統合版。
 */
public enum Category {

    COMBAT("Combat", 0xFFE74C3C, 'C'),
    MOVEMENT("Movement", 0xFF3498DB, 'M'),
    RENDER("Render", 0xFF2ECC71, 'R'),
    PLAYER("Player", 0xFFF1C40F, 'P'),
    WORLD("World", 0xFFE67E22, 'W'),
    EXPLOIT("Exploit", 0xFF9B59B6, 'X'),
    OTHER("Other", 0xFF1ABC9C, 'O'),
    CLIENT("Client", 0xFF95A5A6, 'L');

    private final String displayName;
    private final int color;
    private final char hotkey;

    Category(String displayName, int color, char hotkey) {
        this.displayName = displayName;
        this.color = color;
        this.hotkey = hotkey;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** ARGB カラー (ClickGUI / HUD で使用) */
    public int getColor() {
        return color;
    }

    public char getHotkey() {
        return hotkey;
    }
}
