package com.ninjacheat.client.modules.movement;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.EnumSetting;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Scaffold — 足元に自動でブロックを置いて歩道を作る。
 * FDPClient の Scaffold (Eagle / SameY / Tower) と CheatUtils の Scaffold を統合。
 */
public class Scaffold extends Module {

    private final EnumSetting mode = addSetting(new EnumSetting("Mode", "Tower behavior", "Normal", "Normal", "SameY", "Eagle"));
    private final BoolSetting tower = addSetting(new BoolSetting("Tower", "Jump to build upward", true));
    private final BoolSetting swing = addSetting(new BoolSetting("Swing", "Swing arm on place", true));

    private int startY;

    public Scaffold() {
        super("Scaffold", "Automatically places blocks below you to bridge gaps", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        if (mc.player != null) startY = mc.player.getBlockPos().getY();
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        int slot = findBlockSlot();
        if (slot < 0) return;
        int prev = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);

        // Tower: ジャンプキー長押しで上に積む
        if (tower.get() && mc.options.jumpKey.isPressed() && mc.player.isOnGround()) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.42, mc.player.getVelocity().z);
        }

        // 設置座標
        BlockPos placePos = computePlacePos();
        if (placePos != null) {
            Direction side = findSupportSide(placePos);
            if (side != null) {
                facePlace(placePos, side);
                BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(placePos.offset(side)), side, placePos.offset(side), false);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                if (swing.get()) mc.player.swingHand(Hand.MAIN_HAND);
            }
        }

        mc.player.getInventory().setSelectedSlot(prev);
    }

    private BlockPos computePlacePos() {
        BlockPos below = mc.player.getBlockPos().down();
        if (mc.world.getBlockState(below).isAir()) return below;
        // 移動方向の下を見る
        Vec3d look = mc.player.getRotationVec(1f);
        BlockPos ahead = BlockPos.ofFloored(mc.player.getPos().add(look.x, -1, look.z));
        if (mc.world.getBlockState(ahead).isAir()) return ahead;
        return null;
    }

    private Direction findSupportSide(BlockPos pos) {
        if (mc.world.getBlockState(pos.down()).isSolidBlock(mc.world, pos.down())) return Direction.DOWN;
        for (Direction d : Direction.values()) {
            if (d == Direction.UP) continue;
            BlockPos n = pos.offset(d);
            if (mc.world.getBlockState(n).isSolidBlock(mc.world, n)) return d;
        }
        return null;
    }

    private void facePlace(BlockPos pos, Direction side) {
        Vec3d target = Vec3d.ofCenter(pos).add(Vec3d.of(side.getVector()).multiply(0.5));
        Vec3d eye = mc.player.getEyePos();
        double dx = target.x - eye.x;
        double dy = target.y - eye.y;
        double dz = target.z - eye.z;
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        mc.player.setYaw((float) Math.toDegrees(Math.atan2(dz, dx)) - 90f);
        mc.player.setPitch(MathHelper.clamp((float) -Math.toDegrees(Math.atan2(dy, distXZ)), -90, 90));
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem bi && bi.getBlock().getDefaultState().isSolidBlock(mc.world, BlockPos.ORIGIN)) {
                // 溶岩/水などの非固体は除外
                if (stack.getItem() == net.minecraft.item.Items.SAND || stack.getItem() == net.minecraft.item.Items.GRAVEL) continue;
                return i;
            }
        }
        return -1;
    }

    /** Eagle: 設置時だけ sneak (MixinClientPlayerEntity から呼ばれる) */
    public boolean shouldSneak() {
        return isEnabled() && mode.get().equals("Eagle");
    }
}
