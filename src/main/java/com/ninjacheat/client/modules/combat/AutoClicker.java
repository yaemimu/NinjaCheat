package com.ninjacheat.client.modules.combat;

import com.ninjacheat.client.event.EventHandler;
import com.ninjacheat.client.event.TickEvent;
import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;
import com.ninjacheat.client.setting.NumberSetting;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

/**
 * AutoClicker — 左クリック長押しで連打。CPS レンジ付き。
 * FDPClient の AutoClicker (drag click / butter click) 相当。
 */
public class AutoClicker extends Module {

    private final NumberSetting minCps = addSetting(new NumberSetting("Min CPS", "Minimum clicks per second", 7, 1, 20, 1));
    private final NumberSetting maxCps = addSetting(new NumberSetting("Max CPS", "Maximum clicks per second", 12, 1, 20, 1));
    private final BoolSetting breakBlocks = addSetting(new BoolSetting("Break Blocks", "Also mine blocks while held", true));

    private long nextClick = 0;

    public AutoClicker() {
        super("AutoClicker", "Holding left click auto-attacks at high CPS", Category.COMBAT);
        setKey(GLFW.GLFW_KEY_UNKNOWN);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.options == null) return;
        boolean pressed = mc.options.attackKey.isPressed();
        if (!pressed) {
            nextClick = 0;
            return;
        }
        long now = System.currentTimeMillis();
        if (nextClick == 0) nextClick = now;
        if (now >= nextClick) {
            if (mc.interactionManager != null) {
                if (breakBlocks.get() && mc.crosshairTarget != null) {
                    // ブロック破壊進行はバニラが管理するためここでは攻撃のみ
                }
                mc.doAttack();
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            double min = minCps.get();
            double max = Math.max(min, maxCps.get());
            double cps = ThreadLocalRandom.current().nextDouble(min, max + 0.001);
            nextClick = now + (long) (1000.0 / cps);
        }
    }
}
