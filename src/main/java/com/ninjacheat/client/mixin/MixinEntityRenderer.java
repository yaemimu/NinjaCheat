package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.render.Chams;
import com.ninjacheat.client.modules.render.ESP;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * EntityRenderer の Mixin。
 * ESP のボックス/輪郭描画をレンダー時にフック。
 */
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private <E extends Entity, S extends EntityRenderState> void onRender(
            E entity, S state, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        ESP esp = NinjaCheat.get().modules().get(ESP.class);
        if (esp != null && esp.isEnabled()) {
            esp.renderEntityBox(entity, matrices, vertexConsumers);
        }
    }
}
