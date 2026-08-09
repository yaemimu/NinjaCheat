package com.ninjacheat.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

/**
 * 3D 描画ヘルパ。
 * CheatUtils の RenderHelper / FDPClient の RenderUtils を統合した、
 * ワールド空間にボックス・ライン・輪郭を描くためのユーティリティ集。
 */
public final class RenderUtil {

    private RenderUtil() {}

    /** カメラ相対座標へ変換した Vec3d を返す */
    public static Vec3d cameraRelative(Camera camera, Vec3d worldPos) {
        Vec3d cam = camera.getPos();
        return new Vec3d(worldPos.x - cam.x, worldPos.y - cam.y, worldPos.z - cam.z);
    }

    /** ワールド座標の Box を (カメラ相対) 行列スタック上にライン描画 */
    public static void drawBox(MatrixStack matrices, VertexConsumerProvider provider,
                               Box box, int argb, float lineWidth) {
        // 簡易実装: 12 本のエッジをラインで描く
        VertexConsumer vc = provider.getBuffer(net.minecraft.client.render.RenderLayer.getLines());
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        float a = ((argb >> 24) & 0xFF) / 255f;

        double minX = box.minX, minY = box.minY, minZ = box.minZ;
        double maxX = box.maxX, maxY = box.maxY, maxZ = box.maxZ;

        // 4 本の下辺 + 上辺 + 4 本の縦辺
        edge(matrices, vc, r, g, b, a, minX, minY, minZ, maxX, minY, minZ);
        edge(matrices, vc, r, g, b, a, maxX, minY, minZ, maxX, minY, maxZ);
        edge(matrices, vc, r, g, b, a, maxX, minY, maxZ, minX, minY, maxZ);
        edge(matrices, vc, r, g, b, a, minX, minY, maxZ, minX, minY, minZ);
        edge(matrices, vc, r, g, b, a, minX, maxY, minZ, maxX, maxY, minZ);
        edge(matrices, vc, r, g, b, a, maxX, maxY, minZ, maxX, maxY, maxZ);
        edge(matrices, vc, r, g, b, a, maxX, maxY, maxZ, minX, maxY, maxZ);
        edge(matrices, vc, r, g, b, a, minX, maxY, maxZ, minX, maxY, minZ);
        edge(matrices, vc, r, g, b, a, minX, minY, minZ, minX, maxY, minZ);
        edge(matrices, vc, r, g, b, a, maxX, minY, minZ, maxX, maxY, minZ);
        edge(matrices, vc, r, g, b, a, maxX, minY, maxZ, maxX, maxY, maxZ);
        edge(matrices, vc, r, g, b, a, minX, minY, maxZ, minX, maxY, maxZ);
    }

    private static void edge(MatrixStack matrices, VertexConsumer vc, float r, float g, float b, float a,
                             double x1, double y1, double z1, double x2, double y2, double z2) {
        var ps = matrices.peek();
        vc.vertex(ps, (float) x1, (float) y1, (float) z1).color(r, g, b, a).normal(ps, 0f, 1f, 0f);
        vc.vertex(ps, (float) x2, (float) y2, (float) z2).color(r, g, b, a).normal(ps, 0f, 1f, 0f);
    }

    /** 2点間にラインを引く (Tracers 用) */
    public static void drawLine(MatrixStack matrices, VertexConsumerProvider provider,
                                Vec3d from, Vec3d to, int argb) {
        VertexConsumer vc = provider.getBuffer(net.minecraft.client.render.RenderLayer.getLines());
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        float a = ((argb >> 24) & 0xFF) / 255f;
        var ps = matrices.peek();
        vc.vertex(ps, (float) from.x, (float) from.y, (float) from.z).color(r, g, b, a).normal(ps, 1f, 0f, 0f);
        vc.vertex(ps, (float) to.x, (float) to.y, (float) to.z).color(r, g, b, a).normal(ps, 1f, 0f, 0f);
    }

    /** Entity の衝突ボックスを取得してワールド Box に */
    public static Box entityBox(Entity entity) {
        return entity.getBoundingBox();
    }

    /** 指定距離以内か (除外フィルタ用) */
    public static boolean withinDistance(Entity entity, double maxDist) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return false;
        return mc.player.squaredDistanceTo(entity) <= maxDist * maxDist;
    }
}
