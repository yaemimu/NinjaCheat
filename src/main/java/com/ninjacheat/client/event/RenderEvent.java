package com.ninjacheat.client.event;

/**
 * 画面描画後に発火。HUD / ESP / Tracers のレンダリングに使用。
 * dt は部分ティック (renderTickDelta)。
 */
public class RenderEvent implements Event {

    public static RenderEvent INSTANCE = new RenderEvent();

    public float tickDelta;
    public long nanoTime;

    private RenderEvent() {}

    public void reset(float tickDelta, long nanoTime) {
        this.tickDelta = tickDelta;
        this.nanoTime = nanoTime;
    }
}
