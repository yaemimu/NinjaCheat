package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;

/**
 * AutoFish — 釣り竿の当たりを検知して自動で引き上げ・再投げ。
 * FDPClient の AutoFish / CheatUtils の autoFish 統合。
 * 1.21 では CAUGHT_FISH DataTracker は公開されていないため、
 * bobber の垂直速度の急変と位置の安定度でニブリを検知する。
 */
public class AutoFish extends Module {

    private boolean wasNibbling = false;
    private int recastDelay = 0;
    private double prevY = Double.NaN;
    private double prevVy = 0;

    public AutoFish() {
        super("AutoFish", "Automatically catches fish and recasts", Category.WORLD);
    }

    @Override
    protected void onDisable() {
        wasNibbling = false;
        recastDelay = 0;
        prevY = Double.NaN;
        prevVy = 0;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (!(mc.player.getMainHandStack().getItem() instanceof net.minecraft.item.FishingRodItem)
            && !(mc.player.getOffHandStack().getItem() instanceof net.minecraft.item.FishingRodItem)) return;

        FishingBobberEntity bobber = mc.player.fishHook;
        if (bobber == null) {
            // 投げていない → 遅延後に再投げ
            if (recastDelay > 0) {
                recastDelay--;
            } else {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                recastDelay = 20;
            }
            return;
        }

        // ニブリ検知: bobber の Y 速度が急に下方へ変化 (魚が引き込む)
        double y = bobber.getY();
        double vy = bobber.getVelocity().y;
        boolean nibbling = false;
        if (!Double.isNaN(prevY)) {
            // 速度が大きく下方に変化、またはY位置が急降下
            double dv = vy - prevVy;
            if (vy < -0.05 && Math.abs(dv) > 0.04) {
                nibbling = true;
            }
        }
        prevY = y;
        prevVy = vy;

        if (nibbling && !wasNibbling) {
            // 引き上げ
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            recastDelay = 15;
            prevY = Double.NaN;
            prevVy = 0;
        }
        wasNibbling = nibbling;
    }
}
