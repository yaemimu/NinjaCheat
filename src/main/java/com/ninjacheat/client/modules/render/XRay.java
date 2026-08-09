package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.ColorSetting;

/**
 * XRay — 不要なブロックを非表示にして鉱石を浮かび上がらせる。
 * FDPClient の XRay をシンプル化 (BlockRenderManager Mixin が必要な本格実装)。
 */
public class XRay extends Module {

    private final ColorSetting oreColor = addSetting(new ColorSetting("Ore Color", "Tint for visible ores", 0xFFFFD700));

    public XRay() {
        super("XRay", "Hide non-ore blocks to spot minerals", Category.RENDER);
    }
}
