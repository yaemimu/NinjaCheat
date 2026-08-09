package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Strafe — 移動方向に応じて横/縦ストレイフで加速。
 * FDPClient の Strafe をシンプル化。
 */
public class Strafe extends Module {

    private final BoolSetting onlyInAir = addSetting(new BoolSetting("Only In Air", "Only boost while airborne", true));

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (onlyInAir.get() && mc.player.isOnGround()) return;
        double yaw = Math.toRadians(mc.player.getYaw());
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        if (forward == 0 && strafe == 0) return;
        double speed = 0.33;
        double vx = (-Math.cos(yaw) * strafe - Math.sin(yaw) * forward) * speed;
        double vz = (Math.sin(yaw) * -strafe + Math.cos(yaw) * -forward) * speed;
        Vec3d cur = mc.player.getVelocity();
        // 既存速度より速い場合のみ上書き
        if (vx * vx + vz * vz > cur.x * cur.x + cur.z * cur.z) {
            mc.player.setVelocity(vx, cur.y, vz);
        }
    }
}
