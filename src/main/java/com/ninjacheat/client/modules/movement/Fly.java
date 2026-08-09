package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.EnumSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Fly — 自由飛行。Vanilla / Jetpack モード。
 * FDPClient の Flight (複数モード) と CheatUtils の FlyHack を統合。
 */
public class Fly extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Flight style", "Vanilla", "Vanilla", "Jetpack"));
    private final NumberSetting speed = addSetting(new NumberSetting("Speed", "Flight speed", 1.0, 0.1, 5.0, 0.1));

    public Fly() {
        super("Fly", "Allows you to fly in survival", Category.MOVEMENT);
        setKey(GLFW.GLFW_KEY_F);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        switch (mode.get()) {
            case "Jetpack" -> {
                // 前進 + 上昇 (jump押下時)
                mc.player.setVelocity(mc.player.getVelocity().x, 0.4, mc.player.getVelocity().z);
            }
            default -> {
                // Vanilla: 速度を直接制御
                mc.player.getAbilities().flying = true;
                mc.player.setVelocity(Vec3d.ZERO);
                double s = speed.get();
                double yaw = Math.toRadians(mc.player.getYaw());
                double forward = mc.player.input.movementForward;
                double strafe = mc.player.input.movementSideways;
                double vx = (-Math.cos(yaw) * strafe - Math.sin(yaw) * forward) * s;
                double vz = (Math.sin(yaw) * -strafe + Math.cos(yaw) * -forward) * s;
                double vy = 0;
                if (mc.options.jumpKey.isPressed()) vy += s;
                if (mc.options.sneakKey.isPressed()) vy -= s;
                mc.player.setVelocity(vx, vy, vz);
            }
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player == null) return;
        if (mode.get().equals("Vanilla") && !mc.player.getAbilities().creativeMode) {
            mc.player.getAbilities().flying = false;
        }
    }
}
