package com.ninjacheat.client.event;

/**
 * キー入力時に発火。
 */
public class KeyEvent implements Event {

    public static KeyEvent INSTANCE = new KeyEvent();

    public int key;
    public int scancode;
    public int action;
    public int modifiers;

    private KeyEvent() {}

    public void reset(int key, int scancode, int action, int modifiers) {
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }

    public int getModifiers() {
        return modifiers;
    }
}
