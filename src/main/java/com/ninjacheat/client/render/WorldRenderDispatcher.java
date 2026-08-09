package com.ninjacheat.client.render;

import com.ninjacheat.client.NinjaCheat;
import com.ninjacheat.client.modules.render.ESP;
import com.ninjacheat.client.modules.render.HoleESP;
import com.ninjacheat.client.modules.render.LogoutSpots;
import com.ninjacheat.client.modules.render.StorageESP;
import com.ninjacheat.client.modules.render.Tracers;
import com.ninjacheat.client.modules.render.Trajectories;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

/**
 * 3D ワールド描画系モジュールのディスパッチャ。
 * MixinWorldRenderer#onRenderEnd から呼ばれ、各 Render モジュールの
 * render(matrices, camera, tickDelta) を順次呼び出す。
 */
public final class WorldRenderDispatcher {

    private WorldRenderDispatcher() {}

    public static void dispatch(MatrixStack matrices, Camera camera, float tickDelta) {
        if (NinjaCheat.get() == null) return;

        invoke(Tracers.class, m -> m.render(matrices, camera, tickDelta));
        invoke(ESP.class, m -> m.render(matrices, camera, tickDelta));
        invoke(HoleESP.class, m -> m.render(matrices, camera, tickDelta));
        invoke(StorageESP.class, m -> m.render(matrices, camera, tickDelta));
        invoke(LogoutSpots.class, m -> m.render(matrices, camera, tickDelta));
        invoke(Trajectories.class, m -> m.render(matrices, camera, tickDelta));
    }

    private static <T> void invoke(Class<T> type, java.util.function.Consumer<T> action) {
        T mod = NinjaCheat.get().modules().get(type);
        if (mod != null && ((com.ninjacheat.client.module.Module) mod).isEnabled()) {
            try {
                action.accept(mod);
            } catch (Throwable ignored) {}
        }
    }
}
