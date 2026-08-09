package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.ColorSetting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

/**
 * Chams — プレイヤー/モブを壁透過でチームカラー描画。
 * FDPClient の Chams を統合。
 */
public class Chams extends Module {

    private final ColorSetting color = addSetting(new ColorSetting("Color", "Chams color", 0x80FF3B3B));

    public Chams() {
        super("Chams", "Renders entities through walls in a solid color", Category.RENDER);
    }

    /** MixinLivingEntityRenderer から VertexConsumer を差し替えるために呼ばれる */
    public VertexConsumer replaceVertexConsumer(VertexConsumer original) {
        // 本格実装では RenderLayer.getLineStrip() / getEntitySolid の色上書き版を返す
        // 簡易版: そのまま返す (RenderLayer 切り替えは Mixin 拡張余地として残す)
        return original;
    }
}
