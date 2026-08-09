package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.render.RenderUtil;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.ColorSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;

/**
 * StorageESP — チェスト・シュルカー・かまど等の収納ブロックを可視化。
 * CheatUtils の StorageESP / FDPClient の ChestESP 統合。
 * WorldRenderDispatcher から render() が呼ばれる。
 */
public class StorageESP extends Module {

    private final NumberSetting range = addSetting(new NumberSetting("Range", "Detection range", 64, 8, 256, 8));
    private final BoolSetting chests = addSetting(new BoolSetting("Chests", "Highlight chests & ender chests", true));
    private final BoolSetting shulkers = addSetting(new BoolSetting("Shulkers", "Highlight shulker boxes", true));
    private final BoolSetting echest = addSetting(new BoolSetting("Ender Chests", "Highlight ender chests", true));
    private final BoolSetting furnaces = addSetting(new BoolSetting("Furnaces", "Highlight furnaces", false));
    private final BoolSetting dispensers = addSetting(new BoolSetting("Dispensers", "Highlight dispensers/droppers", false));
    private final BoolSetting hoppers = addSetting(new BoolSetting("Hoppers", "Highlight hoppers", false));
    private final ColorSetting chestColor = addSetting(new ColorSetting("Chest Color", "", 0xFFFFC400));
    private final ColorSetting shulkerColor = addSetting(new ColorSetting("Shulker Color", "", 0xFFFF00FF));
    private final ColorSetting echestColor = addSetting(new ColorSetting("Ender Color", "", 0xFF00C8FF));
    private final ColorSetting furnaceColor = addSetting(new ColorSetting("Furnace Color", "", 0xFF888888));
    private final ColorSetting dispenserColor = addSetting(new ColorSetting("Dispenser Color", "", 0xFF44FF44));
    private final ColorSetting hopperColor = addSetting(new ColorSetting("Hopper Color", "", 0xFFFFFFFF));

    public StorageESP() {
        super("StorageESP", "Highlights storage blocks through walls", Category.RENDER);
    }

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        if (mc.world == null || mc.player == null) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        var cam = camera.getPos();
        double r2 = range.get() * range.get();

        for (BlockEntity be : mc.world.blockEntities) {
            if (be == null) continue;
            BlockPos pos = be.getPos();
            if (mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > r2) continue;
            Integer color = colorFor(be);
            if (color == null) continue;
            matrices.push();
            Box box = new Box(pos).offset(-cam.x, -cam.y, -cam.z);
            RenderUtil.drawBox(matrices, immediate, box, color, 1.5f);
            matrices.pop();
        }
        immediate.draw();
    }

    private Integer colorFor(BlockEntity be) {
        if (chests.get() && be instanceof ChestBlockEntity) return chestColor.get();
        if (echest.get() && be instanceof EnderChestBlockEntity) return echestColor.get();
        if (shulkers.get() && be instanceof ShulkerBoxBlockEntity) return shulkerColor.get();
        if (furnaces.get() && be instanceof FurnaceBlockEntity) return furnaceColor.get();
        if (furnaces.get() && be instanceof BrewingStandBlockEntity) return furnaceColor.get();
        if (dispensers.get() && (be instanceof DispenserBlockEntity || be instanceof DropperBlockEntity)) return dispenserColor.get();
        if (hoppers.get() && be instanceof HopperBlockEntity) return hopperColor.get();
        if (chests.get() && be instanceof BarrelBlockEntity) return chestColor.get();
        return null;
    }
}
