package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.render.RenderUtil;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.ColorSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/**
 * HoleESP — Crystal PvP で安全な「穴」(obsidian/bedrock 4方囲み) を可視化。
 * CheatUtils の HoleESP 由来。WorldRenderDispatcher から render() が呼ばれる。
 */
public class HoleESP extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Scan range (blocks)", 6, 1, 16, 1));
    private final ColorSetting safeColor = addSetting(new ColorSetting("Safe Color", "Full obsidian/bedrock hole", 0xFF00FF66));
    private final ColorSetting unsafeColor = addSetting(new ColorSetting("Unsafe Color", "Mixed (some bedrock)", 0xFFFFAA00));
    private final BoolSetting flatten = addSetting(new BoolSetting("Flatten", "Draw flat box at floor", true));

    public HoleESP() {
        super("HoleESP", "Highlights safe holes for Crystal PvP", Category.RENDER);
    }

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.world == null || mc.player == null) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        BlockPos origin = mc.player.getBlockPos();
        int r = range.getInt();

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = origin.add(x, y, z);
                    if (!isAir(pos)) continue;
                    if (!isAir(pos.up())) continue;
                    if (!isSolidFloor(pos.down())) continue;
                    if (isSolid(pos.up(2))) continue; // 2段目が塞がれている穴は除外

                    int safe = safetyScore(pos);
                    if (safe < 0) continue; // 穴ではない

                    int color = (safe == 4) ? safeColor.get() : unsafeColor.get();
                    matrices.push();
                    var cam = camera.getPos();
                    Box box = flatten.get()
                            ? new Box(pos).withMaxY(pos.getY() + 1.05)
                            : new Box(pos).withMaxY(pos.getY() + 2.0);
                    box = box.offset(-cam.x, -cam.y, -cam.z);
                    RenderUtil.drawBox(matrices, immediate, box, color, 1.5f);
                    matrices.pop();
                }
            }
        }
        immediate.draw();
    }

    /** -1 = 穴ではない, 0..4 = 安全度 (obsidian/bedrock の壁の数) */
    private int safetyScore(BlockPos pos) {
        int safeWalls = 0;
        BlockPos[] walls = { pos.north(), pos.south(), pos.east(), pos.west() };
        for (BlockPos w : walls) {
            BlockState s = mc.world.getBlockState(w);
            if (s.getBlock() == Blocks.OBSIDIAN || s.getBlock() == Blocks.BEDROCK) {
                safeWalls++;
            } else if (s.isAir() || !s.isOpaque()) {
                return -1; // 壁がない = 穴ではない
            }
            // その他の硬いブロックは unsafe 扱い (safeWalls には加算しない)
        }
        return safeWalls;
    }

    private boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).isAir();
    }

    private boolean isSolidFloor(BlockPos pos) {
        BlockState s = mc.world.getBlockState(pos);
        return !s.isAir() && s.isOpaque();
    }

    private boolean isSolid(BlockPos pos) {
        BlockState s = mc.world.getBlockState(pos);
        return !s.isAir() && s.isOpaque();
    }
}
