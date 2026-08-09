package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.other.ChestStealer;
import com.ninjacheat.client.modules.player.AutoArmor;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * HandledScreen (チェスト等) の Mixin。
 * ChestStealer / AutoArmor の毎ティック処理をここから駆動。
 */
@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        HandledScreen<?> self = (HandledScreen<?>) (Object) this;
        ScreenHandler handler = self.getScreenHandler();
        ChestStealer cs = NinjaCheat.get().modules().get(ChestStealer.class);
        if (cs != null && cs.isEnabled()) cs.onScreenTick(handler);
    }
}
