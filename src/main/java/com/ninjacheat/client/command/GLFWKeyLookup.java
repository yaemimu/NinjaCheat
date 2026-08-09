package com.ninjacheat.client.command;

import org.lwjgl.glfw.GLFW;

import java.util.Locale;

/**
 * GLFW のキー名 <-> コード変換ヘルパ。
 */
final class GLFWKeyLookup {

    private GLFWKeyLookup() {}

    static int byName(String name) {
        if (name == null || name.isEmpty()) return -1;
        String n = name.toLowerCase(Locale.ROOT);
        // 単文字
        if (n.length() == 1) {
            char c = n.charAt(0);
            if (c >= 'a' && c <= 'z') return GLFW.GLFW_KEY_A + (c - 'a');
            if (c >= '0' && c <= '9') return GLFW.GLFW_KEY_0 + (c - '0');
        }
        return switch (n) {
            case "space" -> GLFW.GLFW_KEY_SPACE;
            case "tab" -> GLFW.GLFW_KEY_TAB;
            case "enter", "return" -> GLFW.GLFW_KEY_ENTER;
            case "escape", "esc" -> GLFW.GLFW_KEY_ESCAPE;
            case "backspace" -> GLFW.GLFW_KEY_BACKSPACE;
            case "insert" -> GLFW.GLFW_KEY_INSERT;
            case "delete", "del" -> GLFW.GLFW_KEY_DELETE;
            case "right_shift", "rshift" -> GLFW.GLFW_KEY_RIGHT_SHIFT;
            case "left_shift", "lshift", "shift" -> GLFW.GLFW_KEY_LEFT_SHIFT;
            case "right_ctrl", "rctrl" -> GLFW.GLFW_KEY_RIGHT_CONTROL;
            case "left_ctrl", "lctrl", "ctrl" -> GLFW.GLFW_KEY_LEFT_CONTROL;
            case "right_alt", "ralt" -> GLFW.GLFW_KEY_RIGHT_ALT;
            case "left_alt", "lalt", "alt" -> GLFW.GLFW_KEY_LEFT_ALT;
            case "up" -> GLFW.GLFW_KEY_UP;
            case "down" -> GLFW.GLFW_KEY_DOWN;
            case "left" -> GLFW.GLFW_KEY_LEFT;
            case "right" -> GLFW.GLFW_KEY_RIGHT;
            case "home" -> GLFW.GLFW_KEY_HOME;
            case "end" -> GLFW.GLFW_KEY_END;
            case "page_up", "pageup" -> GLFW.GLFW_KEY_PAGE_UP;
            case "page_down", "pagedown" -> GLFW.GLFW_KEY_PAGE_DOWN;
            case "f1" -> GLFW.GLFW_KEY_F1;
            case "f2" -> GLFW.GLFW_KEY_F2;
            case "f3" -> GLFW.GLFW_KEY_F3;
            case "f4" -> GLFW.GLFW_KEY_F4;
            case "f5" -> GLFW.GLFW_KEY_F5;
            case "f6" -> GLFW.GLFW_KEY_F6;
            case "f7" -> GLFW.GLFW_KEY_F7;
            case "f8" -> GLFW.GLFW_KEY_F8;
            case "f9" -> GLFW.GLFW_KEY_F9;
            case "f10" -> GLFW.GLFW_KEY_F10;
            case "f11" -> GLFW.GLFW_KEY_F11;
            case "f12" -> GLFW.GLFW_KEY_F12;
            default -> -1;
        };
    }
}
