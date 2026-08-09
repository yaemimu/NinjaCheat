package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.client.gui.screen.Screen;

/**
 * InvMove — インベントリ/チャット画面を開いていても移動できる。
 * FDPClient の InvMove と CheatUtils の InvMove を統合。
 */
public class InvMove extends Module {

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null) return;
        Screen screen = mc.currentScreen;
        if (screen == null) return;
        // 各キーが押されているか判定して入力を供給する (バニラは画面中は入力を止めるため)
        // 簡易版: screen に居ても mc.options.* は押下状態を保持しているので直接参照可能
        double yaw = Math.toRadians(mc.player.getYaw());
        double forward = 0, strafe = 0;
        if (mc.options.forwardKey.isPressed()) forward += 1;
        if (mc.options.backKey.isPressed()) forward -= 1;
        if (mc.options.leftKey.isPressed()) strafe += 1;
        if (mc.options.rightKey.isPressed()) strafe -= 1;
        double speed = 0.2873;
        double vx = (-Math.cos(yaw) * strafe - Math.sin(yaw) * forward) * speed;
        double vz = (Math.sin(yaw) * -strafe + Math.cos(yaw) * -forward) * speed;
        if (forward != 0 || strafe != 0) {
            mc.player.setVelocity(vx, mc.player.getVelocity().y, vz);
        }
        if (mc.options.jumpKey.isPressed() && mc.player.isOnGround()) {
            mc.player.jump();
        }
    }
}
