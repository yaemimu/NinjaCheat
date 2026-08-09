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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * ESP — エンティティをボックス/輪郭で可視化。
 * CheatUtils の EntityEsp と FDPClient の ESP / ESP2D を統合。
 */
public class ESP extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Detection range", 64, 8, 256, 8));
    private final BoolSetting players = addSetting(new BoolSetting("Players", "Render players", true));
    private final BoolSetting hostiles = addSetting(new BoolSetting("Hostiles", "Render hostile mobs", true));
    private final BoolSetting friendlies = addSetting(new BoolSetting("Friendlies", "Render passive mobs", false));
    private final BoolSetting items = addSetting(new BoolSetting("Items", "Render dropped items", false));
    private final ColorSetting playerColor = addSetting(new ColorSetting("Player Color", "Box color for players", 0xFFFF3B3B));
    private final ColorSetting hostileColor = addSetting(new ColorSetting("Hostile Color", "Box color for hostiles", 0xFFFF8C00));
    private final ColorSetting friendlyColor = addSetting(new ColorSetting("Friendly Color", "Box color for friendlies", 0xFF3BEAFF));
    private final ColorSetting itemColor = addSetting(new ColorSetting("Item Color", "Box color for items", 0xFFFFFFFF));

    public ESP() {
        super("ESP", "Highlights entities through walls with bounding boxes", Category.RENDER);
    }

    /** WorldRenderDispatcher から呼ばれる */
    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.world == null || mc.player == null) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            if (entity.squaredDistanceTo(mc.player) > range.get() * range.get()) continue;
            int color = colorFor(entity);
            if (color == 0) continue;
            // カメラ相対座標にシフト
            matrices.push();
            Vec3d cam = camera.getPos();
            Box box = entity.getBoundingBox().offset(-cam.x, -cam.y, -cam.z);
            RenderUtil.drawBox(matrices, immediate, box, color, 1.5f);
            matrices.pop();
        }
        immediate.draw();
    }

    /** MixinEntityRenderer から呼ばれる (個別エンティティレンダー時) */
    public void renderEntityBox(Entity entity, MatrixStack matrices, VertexConsumerProvider provider) {
        // WorldRenderer 側で一括描画しているのでここでは省略可
    }

    private int colorFor(Entity entity) {
        if (entity instanceof PlayerEntity && players.get()) return playerColor.get();
        if (entity instanceof LivingEntity le) {
            if (le.isAlive()) {
                if (isHostile(le) && hostiles.get()) return hostileColor.get();
                if (!isHostile(le) && friendlies.get()) return friendlyColor.get();
            }
        }
        if (entity instanceof net.minecraft.entity.ItemEntity && items.get()) return itemColor.get();
        return 0;
    }

    private boolean isHostile(LivingEntity e) {
        String name = e.getType().getName().getString();
        return name.matches("(?i).*(zombie|skeleton|creeper|spider|enderman|witch|blaze|ghast|phantom|drowned|husk|stray|wither|warden|piglin|hoglin).*");
    }
}
