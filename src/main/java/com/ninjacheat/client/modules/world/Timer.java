package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * Timer — クライアントのゲームティック速度を変更し、移動等を加速。
 * FDPClient の Timer / CheatUtils の timer 統合。
 * 教育目的簡易版: 毎tick の処理を複数回呼ぶ擬似タイマー。
 * 本格実装は MixinRenderTickCounter で tickDelta を倍率化する。
 */
public class Timer extends Module {

    private final NumberSetting speed = addSetting(new NumberSetting("Speed", "Tick speed multiplier", 1.5, 0.1, 10.0, 0.1));

    public Timer() {
        super("Timer", "Changes client-side tick speed", Category.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        // 教育目的: ここでは multiplier を保持するのみ。
        // 実効は MixinRenderTickCounter / MixinMinecraftClient#tick で onTick を倍数呼ぶ形で実装すべき。
    }

    public float getSpeedFloat() {
        return speed.getFloat();
    }
}
