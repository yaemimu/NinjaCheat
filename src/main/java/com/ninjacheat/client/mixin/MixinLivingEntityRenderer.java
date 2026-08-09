package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.render.Chams;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * LivingEntityRenderer の Mixin。
 * Chams が有効なら RenderLayer を無視して強制的にチームカラーで描画。
 */
@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"),
            index = 1
    )
    private net.minecraft.client.render.VertexConsumer modifyVertexConsumer(
            net.minecraft.client.render.VertexConsumer original) {
        Chams chams = NinjaCheat.get() != null
                ? NinjaCheat.get().modules().get(Chams.class) : null;
        if (chams != null && chams.isEnabled()) {
            return chams.replaceVertexConsumer(original);
        }
        return original;
    }
}
