package com.ninjacheat.client.modules.other;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * AntiAFK — AFK キックを防ぐための自動行動。
 * FDPClient の AntiAFK / CheatUtils の antiAfk 統合。
 * 教育目的: 定期的に視点をわずかに動かす / ジャンプ / チャット。
 */
public class AntiAFK extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Anti-AFK method", "Rotate", "Rotate", "Jump", "Sneak", "Swing"));
    private final NumberSetting interval = addSetting(new NumberSetting("Interval", "Action interval (ticks)", 80, 20, 600, 10));

    private int counter = 0;

    public AntiAFK() {
        super("AntiAFK", "Prevents being kicked for being idle", Category.OTHER);
    }

    @Override
    protected void onEnable() {
        counter = 0;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        counter++;
        if (counter < interval.getInt()) return;
        counter = 0;

        switch (mode.get()) {
            case "Rotate" -> {
                // わずかに視点を回す
                mc.player.setYaw(mc.player.getYaw() + 1.5f);
            }
            case "Jump" -> {
                if (mc.player.isOnGround()) mc.player.jump();
            }
            case "Sneak" -> {
                // 1tick sneak をトグル相当 (簡易: 状態を反転)
                mc.options.sneakKey.setPressed(!mc.options.sneakKey.isPressed());
            }
            case "Swing" -> {
                mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
            }
        }
    }
}
