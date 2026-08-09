package com.ninjacheat.client.mixin;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.command.CommandManager;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Screen の charTyped / keyPressed を横取りして、コマンドプレフィックスを
 * 処理できるようにする。ChatScreen 上での "." コマンド入力を遮断する。
 */
@Mixin(Screen.class)
public abstract class MixinScreen {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendMessage(String message, boolean toHud, CallbackInfoReturnable<Boolean> cir) {
        if (NinjaCheat.get() == null) return;
        CommandManager mgr = NinjaCheat.get().commands();
        if (message.startsWith(NinjaCheat.COMMAND_PREFIX)) {
            mgr.dispatch(message);
            cir.setReturnValue(true);
        }
    }
}
