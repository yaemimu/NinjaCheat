package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.render.FullBright;
import com.ninjacheat.client.modules.render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * SimpleOption (gamma, fov 等) の Mixin。
 * FullBright が有効なら gamma を 15.0 に固定して返す。
 */
@Mixin(SimpleOption.class)
public abstract class MixinSimpleOption {

    @SuppressWarnings("unchecked")
    @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
    private <T> void onGetValue(CallbackInfoReturnable<T> cir) {
        SimpleOption<T> opt = (SimpleOption<T>) (Object) this;
        // gamma (明るさ) オプションのみ
        if ("options.gamma".equals(opt.getKey()) || opt.toString().contains("gamma")) {
            FullBright fb = NinjaCheat.get() != null
                    ? NinjaCheat.get().modules().get(FullBright.class) : null;
            if (fb != null && fb.isEnabled()) {
                T override = (T) Double.valueOf(fb.getGammaOverride());
                cir.setReturnValue(override);
            }
        }
    }
}
