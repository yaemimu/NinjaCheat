package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * HighJump — ジャンプ高度を上げる。
 * FDPClient の HighJump をシンプル化 (jumpStrength を上書き)。
 */
public class HighJump extends Module {

    private final NumberSetting height = addSetting(new NumberSetting("Height", "Jump height multiplier", 2.0, 1.0, 5.0, 0.25));

    public HighJump() {
        super("HighJump", "Jump much higher than normal", Category.MOVEMENT);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.isOnGround() && mc.options.jumpKey.isPressed()) {
            double v = 0.42 * height.get();
            mc.player.setVelocity(mc.player.getVelocity().x, v, mc.player.getVelocity().z);
        }
    }
}
