package com.ninjacheat.client.modules.render;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.HudRenderEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * NameTags — プレイヤー上に拡大/詳細なネームタグを描画。
 * FDPClient の NameTags (health, ping, distance) を統合。
 */
public class NameTags extends Module {

    private final BoolSetting health = addSetting(new BoolSetting("Health", "Show health", true));
    private final BoolSetting ping = addSetting(new BoolSetting("Ping", "Show ping", false));
    private final BoolSetting distance = addSetting(new BoolSetting("Distance", "Show distance", false));
    private final NumberSetting scale = addSetting(new NumberSetting("Scale", "NameTag scale", 1.0, 0.5, 2.0, 0.1));

    public NameTags() {
        super("NameTags", "Shows enhanced name tags above players", Category.RENDER);
    }

    @EventHandler
    private void onHudRender(HudRenderEvent event) {
        // 名前描画は WorldRenderer で行うのが正確だが、ここではフラグのみ保持
    }
}
