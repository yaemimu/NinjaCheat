package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.util.math.BlockPos;

/**
 * Spider — 壁を登る。
 * FDPClient の Spider / WallClimb をシンプル化。
 */
public class Spider extends Module {

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.horizontalCollision) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.4, mc.player.getVelocity().z);
        }
    }
}
