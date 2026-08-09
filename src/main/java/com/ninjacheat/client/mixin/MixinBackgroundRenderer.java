package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * BackgroundRenderer の Mixin。
 * Fog モジュール (チートutils Fog / FDPClient NoFOV 由来) で霧を調整する。
 */
@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {

    @ModifyArg(
            method = "applyFog",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Fog;linear(FF)V"),
            index = 0
    )
    private static float modifyFogStart(float start) {
        com.ninjacheat.client.modules.render.NoRender nr =
                NinjaCheat.get() != null
                        ? NinjaCheat.get().modules().get(com.ninjacheat.client.modules.render.NoRender.class)
                        : null;
        if (nr != null && nr.isEnabled() && nr.isNoFog()) {
            return 1000f;
        }
        return start;
    }
}
