package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * Jesus — 水上を歩く / 浮く。
 * FDPClient の Jesus をシンプル化 (水上に固体ブロックがあるかのように振る舞う)。
 */
public class Jesus extends Module {

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        BlockPos below = mc.player.getBlockPos().down();
        boolean water = mc.world.getBlockState(below).isOf(Blocks.WATER);
        if (water && !mc.player.isSneaking()) {
            // 水に入らないよう Y速度を上向きに
            mc.player.setVelocity(mc.player.getVelocity().x, 0.3, mc.player.getVelocity().z);
            mc.player.fallDistance = 0;
        }
    }
}
