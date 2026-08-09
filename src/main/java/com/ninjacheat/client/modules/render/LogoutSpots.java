package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.render.RenderUtil;
import com.ninjacheat.client.setting.ColorSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LogoutSpots — プレイヤーがログアウトした場所をマーク表示。
 * CheatUtils の logoutSpot / FDPClient の LogoutSpots 統合。
 * MixinClientWorld.onRemoveEntity から onPlayerLogout() が呼ばれる。
 * WorldRenderDispatcher から render() が呼ばれる。
 */
public class LogoutSpots extends Module {

    private final NumberSetting maxSpots = addSetting(new NumberSetting("Max Spots", "Maximum stored spots", 20, 1, 100, 1));
    private final ColorSetting color = addSetting(new ColorSetting("Color", "Logout spot box color", 0xFFFF3B3B));

    private static final class Spot {
        final UUID uuid;
        final String name;
        final Vec3d pos;
        final long time;

        Spot(UUID uuid, String name, Vec3d pos) {
            this.uuid = uuid;
            this.name = name;
            this.pos = pos;
            this.time = System.currentTimeMillis();
        }
    }

    private final Map<UUID, Spot> spots = new ConcurrentHashMap<>();

    public LogoutSpots() {
        super("LogoutSpots", "Marks where players logged out", Category.RENDER);
    }

    /** MixinClientWorld から呼ばれる: プレイヤーがワールドから消えた時 */
    public void onPlayerLogout(PlayerEntity player) {
        if (player == null || player.getUuid() == null) return;
        // 自分自身は無視
        if (mc.player != null && player.getUuid().equals(mc.player.getUuid())) return;
        spots.put(player.getUuid(), new Spot(player.getUuid(), player.getName().getString(), player.getPos()));
        // 上限を超えたら古いものから削除
        while (spots.size() > maxSpots.getInt()) {
            UUID oldest = null;
            long oldestTime = Long.MAX_VALUE;
            for (Map.Entry<UUID, Spot> e : spots.entrySet()) {
                if (e.getValue().time < oldestTime) {
                    oldestTime = e.getValue().time;
                    oldest = e.getKey();
                }
            }
            if (oldest != null) spots.remove(oldest); else break;
        }
    }

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.world == null || mc.player == null) return;
        if (spots.isEmpty()) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        var cam = camera.getPos();

        for (Spot spot : spots.values()) {
            // プレイヤーが再ログインしていたら除去
            if (mc.world.getPlayerByUuid(spot.uuid) != null) {
                spots.remove(spot.uuid);
                continue;
            }
            matrices.push();
            Box box = new Box(spot.pos.x - 0.3, spot.pos.y, spot.pos.z - 0.3,
                              spot.pos.x + 0.3, spot.pos.y + 1.8, spot.pos.z + 0.3)
                    .offset(-cam.x, -cam.y, -cam.z);
            RenderUtil.drawBox(matrices, immediate, box, color.get(), 1.5f);
            matrices.pop();
        }
        immediate.draw();
    }

    @Override
    protected void onDisable() {
        spots.clear();
    }
}
