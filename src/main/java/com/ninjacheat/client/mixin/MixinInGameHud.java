package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.HudRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * InGameHud の Mixin。
 * render() の最後に HudRenderEvent を発火して NinjaCheat の HUD を描画。
 */
@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        if (NinjaCheat.get().modules().get(com.ninjacheat.client.modules.client.Hud.class) == null
                || !NinjaCheat.get().modules().get(com.ninjacheat.client.modules.client.Hud.class).isEnabled()) {
            return;
        }
        HudRenderEvent ev = HudRenderEvent.INSTANCE;
        ev.reset(tickCounter.getTickDelta(true));
        NinjaCheat.get().events().post(ev);
        NinjaCheat.get().hud().render(ev.tickDelta);
    }
}
