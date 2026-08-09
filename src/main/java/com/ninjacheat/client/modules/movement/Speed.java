package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import com.ninjacheat.client.setting.NumberSetting;

/**
 * Speed — 移動速度を上げる。Vanilla/NCP/Vulcan 等のモード選択。
 * FDPClient の Speed + 各種 speedmode を統合した教育用サンプル。
 */
public class Speed extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Speed bypass", "Vanilla", "Vanilla", "NCPHop", "VulcanHop", "Strafe"));
    private final NumberSetting baseSpeed = addSetting(new NumberSetting("Speed", "Base speed multiplier", 1.4, 1.0, 3.0, 0.05));

    private int ticks = 0;

    public Speed() {
        super("Speed", "Move faster than normal", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        ticks = 0;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (!mc.player.isOnGround()) return;
        if (mc.player.input.movementForward == 0 && mc.player.input.movementSideways == 0) return;

        double s = baseSpeed.get();
        switch (mode.get()) {
            case "NCPHop" -> {
                if (ticks % 2 == 0) mc.player.jump();
                applyStrafe(s);
            }
            case "VulcanHop" -> {
                if (ticks == 0) mc.player.jump();
                applyStrafe(s);
            }
            case "Strafe" -> {
                applyStrafe(s);
            }
            default -> applyStrafe(s);
        }
        ticks++;
    }

    private void applyStrafe(double mult) {
        double yaw = Math.toRadians(mc.player.getYaw());
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        double speed = 0.2873 * mult; // バニラ歩行速度 ≈ 0.2873 blocks/tick
        double vx = (-Math.cos(yaw) * strafe - Math.sin(yaw) * forward) * speed;
        double vz = (Math.sin(yaw) * -strafe + Math.cos(yaw) * -forward) * speed;
        mc.player.setVelocity(vx, mc.player.getVelocity().y, vz);
    }
}
