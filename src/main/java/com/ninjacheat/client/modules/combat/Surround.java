package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Surround — プレイヤーの周囲4〜6方向に Obsidian を自動設置して爆発を防ぐ。
 * Crystal PvP / anvil trap 対策の定番 "こんなのがあったらいいな" 機能。
 */
public class Surround extends Module {

    private final BoolSetting onlyOnGround = addSetting(new BoolSetting("Only On Ground", "Only place while grounded", true));
    private final BoolSetting center = addSetting(new BoolSetting("Center", "Snap to block center on enable", true));

    public Surround() {
        super("Surround", "Automatically walls you in with Obsidian", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        if (mc.player == null) return;
        if (center.get() && mc.player.isOnGround()) {
            // ブロック中心へ移動 (サーバーに送られる)
            mc.player.setVelocity(Vec3d.ZERO);
            mc.player.setPosition(
                    Math.floor(mc.player.getX()) + 0.5,
                    mc.player.getY(),
                    Math.floor(mc.player.getZ()) + 0.5);
        }
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (onlyOnGround.get() && !mc.player.isOnGround()) return;

        int slot = findObsidianSlot();
        if (slot < 0) return;
        int prev = mc.player.getInventory().getSelectedSlot();
        boolean switched = false;
        if (slot < 9) {
            mc.player.getInventory().setSelectedSlot(slot);
            switched = true;
        } else {
            // インベントリ内ならホットバーへ移動 (簡易: 既にホットバーにある前提)
            return;
        }

        BlockPos base = mc.player.getBlockPos();
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos placePos = base.offset(dir);
            BlockState state = mc.world.getBlockState(placePos);
            if (state.isReplaceable()) {
                // 設置先の裏に支えるブロックが必要 — 下方向を使う簡易版
                BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(placePos), Direction.UP, placePos, false);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }

        if (switched) mc.player.getInventory().setSelectedSlot(prev);
    }

    private int findObsidianSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.OBSIDIAN) return i;
        }
        return -1;
    }
}
