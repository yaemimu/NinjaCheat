package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.util.math.Vec3d;

/**
 * AntiVoid — 虚空へ落ちそうになった時にテレポートで戻す。
 * FDPClient の AntiVoid をシンプル化。
 */
public class AntiVoid extends Module {

    private Vec3d safePos = null;

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isOnGround() && !mc.world.getBlockState(mc.player.getBlockPos().down()).isAir()) {
            safePos = mc.player.getPos();
            return;
        }
        // Y が -5 以下 or 落下中かつ地面がない場合は戻す
        if (mc.player.getY() < -5 && safePos != null) {
            mc.player.setVelocity(Vec3d.ZERO);
            mc.player.setPosition(safePos);
        }
    }
}
