package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.client.ClickGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keyboard の Mixin。キー押下でモジュールをトグル。
 * FDPClient の KeyListener / CheatUtils の KeyBindingHelper を統合。
 */
@Mixin(Keyboard.class)
public abstract class MixinKeyboard {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_REPEAT) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.currentScreen != null) return; // チャット/UI 中は無視
        if (NinjaCheat.get() == null) return;

        // ClickGUI を開く
        ClickGUI gui = NinjaCheat.get().modules().get(ClickGUI.class);
        if (gui != null && gui.isEnabled() && key == gui.getKey()) {
            mc.setScreen(new com.ninjacheat.client.gui.ClickGUIScreen());
            return;
        }

        // 各モジュールのキーを判定してトグル
        for (com.ninjacheat.client.module.Module m : NinjaCheat.get().modules().all()) {
            if (m == gui) continue;
            if (m.hasKey() && m.getKey() == key) {
                m.toggle();
            }
        }
    }
}
