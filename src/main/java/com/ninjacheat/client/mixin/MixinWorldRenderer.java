package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.RenderEvent;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * WorldRenderer の Mixin。
 * 3D レンダー後に RenderEvent を発火して ESP / Tracers / Trajectories を描画。
 */
@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE_STRING",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                    args = "ldc=weather",
                    shift = At.Shift.BEFORE
            )
    )
    private void onRenderEnd(MatrixStack matrices, float tickDelta, long limitTime,
                             boolean renderBlockOutline, net.minecraft.client.render.Camera camera,
                             net.minecraft.client.render.Frustum frustum,
                             net.minecraft.client.render.GameRenderer gameRenderer,
                             net.minecraft.client.render.BackgroundRenderer fog,
                             net.minecraft.client.render.LightmapTextureManager lightmap,
                             net.minecraft.client.render.VertexConsumerProvider.Immediate vertexConsumers,
                             CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        RenderEvent ev = RenderEvent.INSTANCE;
        ev.reset(tickDelta, limitTime);
        NinjaCheat.get().events().post(ev);
        com.ninjacheat.client.render.WorldRenderDispatcher.dispatch(matrices, camera, tickDelta);
    }
}
