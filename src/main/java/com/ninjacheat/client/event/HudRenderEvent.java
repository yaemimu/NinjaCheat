package com.ninjacheat.client.event;

/**
 * 2D HUD 描画時に発火。
 */
public class HudRenderEvent implements Event {

    public static HudRenderEvent INSTANCE = new HudRenderEvent();

    public float tickDelta;

    private HudRenderEvent() {}

    public void reset(float tickDelta) {
        this.tickDelta = tickDelta;
    }
}
