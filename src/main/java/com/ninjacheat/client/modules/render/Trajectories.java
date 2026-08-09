package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.render.RenderUtil;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.ColorSetting;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

/**
 * Trajectories — 投擲物 (弓・雪玉・エンダーパール等) の弾道を予測線で表示。
 * CheatUtils の trajectories / FDPClient の Projectiles 統合。
 * WorldRenderDispatcher から render() が呼ばれる。
 */
public class Trajectories extends Module {

    private final ColorSetting color = addSetting(new ColorSetting("Color", "Trajectory line color", 0xFF3BEAFF));
    private final BoolSetting showLanding = addSetting(new BoolSetting("Show Landing", "Mark the predicted landing point", true));

    public Trajectories() {
        super("Trajectories", "Predicts trajectory of throwables you hold", Category.RENDER);
    }

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.player == null || mc.world == null) return;
        // 手持ちアイテムが投擲物系か判定 (簡易: 弓・雪球・エンダーパール・卵・トライデント)
        var stack = mc.player.getMainHandStack();
        var item = stack.getItem();
        String id = net.minecraft.registry.Registries.ITEM.getId(item).toString();
        if (!id.matches(".*(bow|snowball|ender_pearl|egg|trident|splash_potion|lingering_potion|experience_bottle|wind_charge).*")) {
            return;
        }

        // 簡易弾道シミュレーション: 視線方向に速度を与え、重力で落下
        Vec3d start = mc.player.getEyePos();
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();

        // 弓はチャージ量に依存するが、教育目的なので最大チャージ相当の速度を使用
        double speed = isBow(id) ? 3.0 : 1.5;
        double vx = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed;
        double vz = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * speed;
        double vy = -Math.sin(Math.toRadians(pitch)) * speed;

        double gravity = 0.05; // バニラの投擲物重力 (大まか)
        double drag = 0.99;    // 空気抵抗

        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        var cam = camera.getPos();
        Vec3d prev = start.subtract(cam);

        for (int i = 0; i < 300; i++) {
            Vec3d next = prev.add(vx, vy, vz);
            vy -= gravity;
            vx *= drag;
            vy *= drag;
            vz *= drag;

            RenderUtil.drawLine(matrices, immediate, prev, next, color.get());

            // ブロック衝突判定
            Vec3d worldPrev = prev.add(cam);
            Vec3d worldNext = next.add(cam);
            BlockHitResult hit = mc.world.raycast(new RaycastContext(
                    worldPrev, worldNext,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    mc.player));
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                if (showLanding.get()) {
                    Vec3d land = hit.getPos().subtract(cam);
                    // 着地点に小さな十字を描く
                    RenderUtil.drawLine(matrices, immediate, land.add(-0.3, 0, 0), land.add(0.3, 0, 0), color.get());
                    RenderUtil.drawLine(matrices, immediate, land.add(0, 0, -0.3), land.add(0, 0, 0.3), color.get());
                }
                break;
            }
            prev = next;
            if (prev.lengthSquared() > 250000) break; // ~500ブロックで打ち切り
        }
        immediate.draw();
    }

    private boolean isBow(String id) {
        return id.contains("bow");
    }
}
