package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.HudRenderEvent;
import com.ninjacheat.client.event.KeyEvent;
import com.ninjacheat.client.event.RenderEvent;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MinecraftClient への Mixin。
 * - コンストラクタでNinjaCheat初期化 (エントリポイント以外の早期フック)
 * - tick() で TickEvent 配信 + 各モジュール onTick 呼び出し
 * - render() 系で RenderEvent / HudRenderEvent 配信
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(RunArgs args, CallbackInfo ci) {
        // 既に onInitializeClient で初期化済みの想定だが安全策
        if (NinjaCheat.get() == null) {
            NinjaCheat.setInstance(new NinjaCheat());
            NinjaCheat.get().modules().registerDefaults();
            NinjaCheat.get().commands().registerDefaults();
            NinjaCheat.get().hud().registerDefaults();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.player == null) return;
        NinjaCheat.get().events().post(TickEvent.INSTANCE);
        for (Module m : NinjaCheat.get().modules().all()) {
            if (m.isEnabled()) m.onTick();
        }
    }

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        KeyEvent ev = KeyEvent.INSTANCE;
        ev.reset(key, scancode, action, modifiers);
        NinjaCheat.get().events().post(ev);
    }
}
