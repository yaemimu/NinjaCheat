package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.util.math.Vec3d;

/**
 * FreeCam — プレイヤー本体を動かさず視点だけ移動。
 * CheatUtils の FreeCam と FDPClient の FreeCam を統合。
 */
public class FreeCam extends Module {

    private final NumberSetting speed = addSetting(new NumberSetting("Speed", "Camera move speed", 1.0, 0.2, 4.0, 0.1));

    private Vec3d cameraPos = Vec3d.ZERO;
    private boolean active;

    public FreeCam() {
        super("FreeCam", "Detaches the camera from your body to fly around", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        if (mc.player != null) {
            cameraPos = mc.player.getEyePos();
            active = true;
        }
    }

    @Override
    protected void onDisable() {
        active = false;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || !active) return;
        double s = speed.get();
        double yaw = Math.toRadians(mc.player.getYaw());
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        double vx = (-Math.cos(yaw) * strafe - Math.sin(yaw) * forward) * s;
        double vz = (Math.sin(yaw) * -strafe + Math.cos(yaw) * -forward) * s;
        double vy = 0;
        if (mc.options.jumpKey.isPressed()) vy += s;
        if (mc.options.sneakKey.isPressed()) vy -= s;
        cameraPos = cameraPos.add(vx, vy, vz);
        // 簡易版: 実際のカメラ位置上書きは GameRenderer/ Camera Mixin が必要
    }
}
