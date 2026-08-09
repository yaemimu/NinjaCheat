package com.ninjacheat.client.setting;

/**
 * 全設定の基底クラス。
 * FDPClient の Value<T> の概念をシンプルに再実装したもの。
 * 変更時にコールバックを呼べるようにしている。
 */
public abstract class Setting<T> {

    private final String name;
    private final String description;
    protected T value;
    private Runnable onChange;

    protected Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        if (this.value == value || (this.value != null && this.value.equals(value))) return;
        this.value = value;
        if (onChange != null) onChange.run();
    }

    public Setting<T> onChange(Runnable r) {
        this.onChange = r;
        return this;
    }

    /** 設定型を識別する (ClickGUI の描画分岐用) */
    public abstract String getType();
}
