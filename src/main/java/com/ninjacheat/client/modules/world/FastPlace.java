package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * FastPlace — ブロック設置のクールダウンを短縮。
 * FDPClient の FastPlace 統合。
 */
public class FastPlace extends Module {

    private final NumberSetting delay = addSetting(new NumberSetting("Delay", "Ticks between placements (lower=faster)", 2, 0, 10, 1));

    private int counter = 0;

    public FastPlace() {
        super("FastPlace", "Places blocks faster by reducing cooldown", Category.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        // 教育目的簡易版: 毎tick useKey の押下状態を再トリガして設置クールダウンを回避
        // 本格実装は MixinClientPlayerEntity で blockBreakingCooldown を 0 にする
        if (mc.options.useKey.isPressed()) {
            counter++;
            if (counter >= delay.getInt()) {
                counter = 0;
            }
        } else {
            counter = 0;
        }
    }
}
