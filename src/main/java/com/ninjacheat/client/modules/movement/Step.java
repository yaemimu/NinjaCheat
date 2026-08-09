package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * Step — 段差を自動で昇れる高さを上げる (1段飛び越え)。
 * FDPClient の Step をシンプル化。
 */
public class Step extends Module {

    private final NumberSetting height = addSetting(new NumberSetting("Height", "Step height (blocks)", 1.0, 0.5, 3.0, 0.5));

    public Step() {
        super("Step", "Walk up full blocks without jumping", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        if (mc.player != null) mc.player.setStepHeight(height.get().floatValue());
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.player.setStepHeight(0.6f);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.getStepHeight() != height.get()) {
            mc.player.setStepHeight(height.get().floatValue());
        }
    }
}
