package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.KeyEvent;
import com.ninjacheat.client.modules.client.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mouse の Mixin (キーボード側の onKeyDown で ClickGUI を開くための補助)。
 * 実際のキー処理は MixinMinecraftClient#onKey で行う。
 */
@Mixin(Mouse.class)
public abstract class MixinMouse {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        // ClickGUI のドラッグは ClickGUIScreen 側で処理するためここでは何もしない
    }
}
