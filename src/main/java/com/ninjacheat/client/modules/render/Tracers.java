package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.render.RenderUtil;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.ColorSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Tracers — ターゲットまで直線を引く。
 * FDPClient の Tracers と CheatUtils の tracers シェーダー由来。
 */
public class Tracers extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Max tracer length", 128, 16, 512, 16));
    private final BoolSetting players = addSetting(new BoolSetting("Players", "Draw tracers to players", true));
    private final BoolSetting hostiles = addSetting(new BoolSetting("Hostiles", "Draw tracers to hostiles", true));
    private final ColorSetting color = addSetting(new ColorSetting("Color", "Tracer line color", 0xFFFF3B3B));
    private final BoolSetting fromEyes = addSetting(new BoolSetting("From Eyes", "Start line at eye height", true));

    public Tracers() {
        super("Tracers", "Draws lines from you to nearby entities", Category.RENDER);
    }

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.world == null || mc.player == null) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        Vec3d cam = camera.getPos();
        Vec3d eye = (fromEyes.get() ? mc.player.getEyePos() : mc.player.getPos()).subtract(cam);

        mc.world.getEntities().forEach(entity -> {
            if (entity == mc.player) return;
            if (entity.squaredDistanceTo(mc.player) > range.get() * range.get()) return;
            boolean isPlayer = entity instanceof PlayerEntity;
            if (isPlayer && !players.get()) return;
            if (!isPlayer && !hostiles.get()) return;
            Vec3d target = entity.getPos().subtract(cam);
            RenderUtil.drawLine(matrices, immediate, eye, target, color.get());
        });
        immediate.draw();
    }
}
