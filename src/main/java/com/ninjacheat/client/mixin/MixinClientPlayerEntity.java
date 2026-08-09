package com.ninjacheat.client.mixin;

import com.mojang.authlib.GameProfile;
import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.event.PacketSendEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ClientPlayerEntity の Mixin。
 * 移動・回転・パケット送信の横取りポイント。
 * Fly / Speed / Scaffold / NoRotate / Blink がここを経由して動く。
 */
@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        if (NinjaCheat.get() == null) return;
        // Blink モジュールはパケット送信を一時停止する
        com.ninjacheat.client.modules.exploit.Blink blink =
                NinjaCheat.get().modules().get(com.ninjacheat.client.modules.exploit.Blink.class);
        if (blink != null && blink.isEnabled() && blink.isHoldingPackets()) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "sendMovementPackets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSneaking()Z"
            )
    )
    private boolean redirectSneak(ClientPlayerEntity self) {
        com.ninjacheat.client.modules.movement.Scaffold scaf =
                NinjaCheat.get().modules().get(com.ninjacheat.client.modules.movement.Scaffold.class);
        if (scaf != null && scaf.isEnabled() && scaf.shouldSneak()) {
            return true; // Eagle/Sneak 下げ
        }
        return self.isSneaking();
    }
}
