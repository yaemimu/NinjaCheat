package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Nuker — 視線先の周囲ブロックを一気破壊。
 * FDPClient の Nuker / CheatUtils の nuker 統合。
 */
public class Nuker extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Break radius (blocks)", 4.2, 1.0, 6.0, 0.1));
    private final NumberSetting radius = addSetting(new NumberSetting("Radius", "Horizontal radius around target", 2, 1, 5, 1));

    public Nuker() {
        super("Nuker", "Breaks all blocks around the one you mine", Category.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (!mc.options.attackKey.isPressed()) return;

        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult bhr)) return;
        BlockPos center = bhr.getBlockPos();
        int rad = radius.getInt();

        for (int dx = -rad; dx <= rad; dx++) {
            for (int dy = -rad; dy <= rad; dy++) {
                for (int dz = -rad; dz <= rad; dz++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    if (mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > range.get() * range.get()) continue;
                    if (mc.world.getBlockState(pos).isAir()) continue;
                    mc.interactionManager.attackBlock(pos, Direction.UP);
                    mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
        }
    }
}
