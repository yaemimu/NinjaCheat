package com.ninjacheat.client.event;

/**
 * キャンセル可能なイベント。
 * Mixin 側で元メソッドの実行を抑制するのに使う。
 */
public abstract class Cancellable implements Event {

    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
