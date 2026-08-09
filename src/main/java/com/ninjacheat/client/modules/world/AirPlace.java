package com.ninjacheat.client.modules.world;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * AirPlace — ブロックを空中に (支持ブロックなしで) 設置可能にする。
 * CheatUtils の airPlace / FDPClient の Scaffold 補助由来的機能。
 * 視線先の遠い空中位置にブロックを置く (教育目的の簡易版)。
 */
public class AirPlace extends Module {

    public AirPlace() {
        super("AirPlace", "Place blocks in mid-air without support", Category.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (!mc.options.useKey.isPressed()) return;

        ItemStack stack = mc.player.getMainHandStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) return;

        // 視線先の空中位置を計算 (5ブロック先)
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = mc.player.getRotationVec(1.0f);
        Vec3d target = eye.add(look.multiply(5));
        BlockPos pos = BlockPos.ofFloored(target);

        // 既にブロックがあれば無視
        if (!mc.world.getBlockState(pos).isAir()) return;

        BlockHitResult hit = new BlockHitResult(target, Direction.UP, pos, false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
