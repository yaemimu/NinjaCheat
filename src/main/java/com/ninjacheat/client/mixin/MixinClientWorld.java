package com.ninjacheat.client.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ClientWorld の Mixin。
 * エンティティ追加/削除フック (LogoutSpots, ESP 等)。
 */
@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void onAddEntity(Entity entity, CallbackInfo ci) {
        // 今後 LogoutSpots や StaffDetector のフックに使用
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    private void onRemoveEntity(int entityId, Entity.RemovalReason reason, CallbackInfo ci) {
        com.ninjacheat.client.NinjaCheat nc = com.ninjacheat.client.NinjaCheat.get();
        if (nc == null) return;
        com.ninjacheat.client.modules.render.LogoutSpots ls =
                nc.modules().get(com.ninjacheat.client.modules.render.LogoutSpots.class);
        if (ls != null && ls.isEnabled()) {
            ClientWorld world = (ClientWorld) (Object) this;
            Entity e = world.getEntityById(entityId);
            if (e instanceof net.minecraft.entity.player.PlayerEntity) {
                ls.onPlayerLogout((net.minecraft.entity.player.PlayerEntity) e);
            }
        }
    }
}
