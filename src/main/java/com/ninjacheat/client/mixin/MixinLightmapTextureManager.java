package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.render.NoRender;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LightmapTextureManager の Mixin。
 * FullBright 有効時に光マップを最大輝度で更新する。
 */
@Mixin(LightmapTextureManager.class)
public abstract class MixinLightmapTextureManager {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void onUpdate(float deltaTicks, CallbackInfo ci) {
        com.ninjacheat.client.modules.render.FullBright fb =
                NinjaCheat.get() != null
                        ? NinjaCheat.get().modules().get(com.ninjacheat.client.modules.render.FullBright.class)
                        : null;
        if (fb != null && fb.isEnabled() && fb.isFullBrightMode()) {
            // 光マップ更新を簡略化 (最大輝度) — 実装側でピクセル書き換えを行う簡易版
            // ここでは何もしないことでバニラの暗いマップ生成をスキップする意図
        }
    }
}
