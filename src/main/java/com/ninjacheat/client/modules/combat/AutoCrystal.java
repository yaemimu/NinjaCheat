package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * AutoCrystal — エンドクリスタルを自動設置・起爆。
 * Crystal PvP で必須の "こんなのがあったらいいな" 機能。
 * 敵の近くの溶岩/溶岩岩盤台にクリスタルを置き、即起爆する。
 */
public class AutoCrystal extends Module {

    private final NumberSetting placeRange = addSetting(new NumberSetting("Place Range", "Crystal placement range", 4.5, 1.0, 6.0, 0.1));
    private final NumberSetting breakRange = addSetting(new NumberSetting("Break Range", "Crystal break range", 4.5, 1.0, 6.0, 0.1));
    private final NumberSetting breakDelay = addSetting(new NumberSetting("Break Delay (ms)", "Delay between breaks", 50, 0, 500, 10));
    private final NumberSetting placeDelay = addSetting(new NumberSetting("Place Delay (ms)", "Delay between placements", 50, 0, 500, 10));
    private final NumberSetting maxDamage = addSetting(new NumberSetting("Max Self Damage", "Max self damage to allow", 6.0, 0.0, 20.0, 0.5));
    private final NumberSetting minDamage = addSetting(new NumberSetting("Min Target Damage", "Min damage to target required", 2.0, 0.0, 20.0, 0.5));
    private final BoolSetting rotate = addSetting(new BoolSetting("Rotate", "Face placement before acting", true));

    private long lastBreak = 0;
    private long lastPlace = 0;

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks End Crystals for Crystal PvP", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        lastBreak = lastPlace = 0;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        long now = System.currentTimeMillis();

        // 1) 既存クリスタルを起爆
        EndCrystalEntity crystal = findCrystalToBreak();
        if (crystal != null && now - lastBreak >= breakDelay.get()) {
            mc.interactionManager.attackEntity(mc.player, crystal);
            mc.player.swingHand(Hand.MAIN_HAND);
            lastBreak = now;
            return; // 同ティックで置かない
        }

        // 2) 新規設置
        if (now - lastPlace < placeDelay.get()) return;
        if (mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL
                && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) return;

        PlayerEntity target = findTarget();
        if (target == null) return;
        BlockPos pos = findPlacement(target);
        if (pos == null) return;

        Hand hand = mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL ? Hand.MAIN_HAND : Hand.OFF_HAND;
        if (rotate.get()) facePos(Vec3d.ofCenter(pos));
        mc.interactionManager.interactBlock(mc.player, hand,
                new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));
        mc.player.swingHand(hand);
        lastPlace = now;
    }

    private EndCrystalEntity findCrystalToBreak() {
        List<EndCrystalEntity> crystals = mc.world.getEntitiesByClass(EndCrystalEntity.class,
                mc.player.getBoundingBox().expand(breakRange.get()), e -> true);
        EndCrystalEntity best = null;
        double bestDamage = -1;
        for (EndCrystalEntity c : crystals) {
            for (PlayerEntity p : mc.world.getPlayers()) {
                if (p == mc.player || !p.isAlive()) continue;
                double dmg = estimateDamage(c.getPos(), p);
                if (dmg > bestDamage) {
                    bestDamage = dmg;
                    best = c;
                }
            }
        }
        return best;
    }

    private PlayerEntity findTarget() {
        PlayerEntity best = null;
        double bestDist = 16 * 16;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || !p.isAlive() || p.isSpectator()) continue;
            double d = p.squaredDistanceTo(mc.player);
            if (d < bestDist) { bestDist = d; best = p; }
        }
        return best;
    }

    private BlockPos findPlacement(PlayerEntity target) {
        BlockPos origin = mc.player.getBlockPos();
        int r = (int) Math.ceil(placeRange.get());
        BlockPos best = null;
        double bestScore = -1;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos pos = origin.add(dx, dy, dz);
                    if (!isCrystalBase(pos)) continue;
                    if (mc.player.squaredDistanceTo(Vec3d.ofCenter(pos)) > placeRange.get() * placeRange.get()) continue;
                    if (!canPlaceCrystalAt(pos)) continue;
                    Vec3d center = Vec3d.ofCenter(pos).add(0, 0.5, 0);
                    double targetDmg = estimateDamage(center, target);
                    double selfDmg = estimateDamage(center, mc.player);
                    if (selfDmg > maxDamage.get()) continue;
                    if (targetDmg < minDamage.get()) continue;
                    double score = targetDmg - selfDmg;
                    if (score > bestScore) {
                        bestScore = score;
                        best = pos;
                    }
                }
            }
        }
        return best;
    }

    private boolean isCrystalBase(BlockPos pos) {
        return mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN)
                || mc.world.getBlockState(pos).isOf(Blocks.BEDROCK);
    }

    private boolean canPlaceCrystalAt(BlockPos pos) {
        // 上2段が空である必要
        return mc.world.isAir(pos.up()) && mc.world.isAir(pos.up(2))
                && mc.world.getOtherEntities(null, Box.from(Vec3d.ofCenter(pos.up())).expand(0.5)).isEmpty();
    }

    /** 簡易ダメージ推定 (本格運用は爆発計算APIを使用) */
    private double estimateDamage(Vec3d source, PlayerEntity target) {
        double dist = source.distanceTo(target.getEyePos());
        double exposure = 1.0 - dist / 12.0;
        if (exposure < 0) exposure = 0;
        return 12.0 * exposure * 6.0; // 6 = クリスタル爆発の簡易係数
    }

    private void facePos(Vec3d pos) {
        Vec3d eye = mc.player.getEyePos();
        double dx = pos.x - eye.x;
        double dy = pos.y - eye.y;
        double dz = pos.z - eye.z;
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        mc.player.setYaw((float) Math.toDegrees(Math.atan2(dz, dx)) - 90f);
        mc.player.setPitch((float) -Math.toDegrees(Math.atan2(dy, distXZ)));
    }
}
