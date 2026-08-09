package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.EnumSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;

/**
 * KillAura — 最寄りの敵を自動攻撃。
 * CheatUtils の KillAura (範囲・回転・プライオリティ) と
 * FDPClient の KillAura (tick ベース・silent rotations) を統合した実装。
 */
public class KillAura extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Attack range (blocks)", 4.2, 1.0, 6.0, 0.1));
    private final NumberSetting cps = addSetting(new NumberSetting("CPS", "Attacks per second", 8, 1, 20, 1));
    private final EnumSetting sortMode = addSetting(new EnumSetting("Priority", "Target selection", "Distance", "Distance", "Health", "Angle"));
    private final BoolSetting rotate = addSetting(new BoolSetting("Silent Rotations", "Snap look at target before attack", true));
    private final BoolSetting playersOnly = addSetting(new BoolSetting("Players Only", "Only attack players", false));
    private final BoolSetting throughWalls = addSetting(new BoolSetting("Through Walls", "Attack even without line of sight", false));

    private long lastAttack = 0;

    public KillAura() {
        super("KillAura", "Automatically attacks the nearest entity in range", Category.COMBAT);
        setKey(GLFW.GLFW_KEY_R);
    }

    @Override
    protected void onEnable() {
        lastAttack = 0;
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        LivingEntity target = findTarget();
        if (target == null) return;

        if (rotate.get()) {
            faceEntity(target);
        }

        long now = System.currentTimeMillis();
        long interval = (long) (1000.0 / cps.get());
        if (now - lastAttack >= interval) {
            attack(target);
            lastAttack = now;
        }
    }

    private LivingEntity findTarget() {
        return mc.world.getEntities().stream()
                .filter(e -> e instanceof LivingEntity le && le.isAlive() && e != mc.player)
                .filter(e -> !(playersOnly.get()) || e instanceof PlayerEntity)
                .filter(e -> e.squaredDistanceTo(mc.player) <= range.get() * range.get())
                .filter(e -> throughWalls.get() || mc.player.canSee(e))
                .map(e -> (LivingEntity) e)
                .min(getComparator())
                .orElse(null);
    }

    private Comparator<LivingEntity> getComparator() {
        return switch (sortMode.get()) {
            case "Health" -> Comparator.comparingDouble(LivingEntity::getHealth);
            case "Angle" -> Comparator.comparingDouble(this::angleTo);
            default -> Comparator.comparingDouble(e -> e.squaredDistanceTo(mc.player));
        };
    }

    private double angleTo(LivingEntity e) {
        Vec3d diff = e.getEyePos().subtract(mc.player.getEyePos());
        double yaw = Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90 - mc.player.getYaw();
        return MathHelper.abs(MathHelper.wrapDegrees((float) yaw));
    }

    private void faceEntity(LivingEntity target) {
        Vec3d eye = mc.player.getEyePos();
        Vec3d targetEye = target.getEyePos();
        double dx = targetEye.x - eye.x;
        double dy = targetEye.y - eye.y;
        double dz = targetEye.z - eye.z;
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distXZ));
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    private void attack(LivingEntity target) {
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
