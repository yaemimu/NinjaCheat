package com.ninjacheat.client.modules.player;

import com.ninjacheat.client.module.Category;
import com.ninjacheat.client.module.Module;
import com.ninjacheat.client.setting.BoolSetting;

/**
 * NoRotate — サーバーからの強制回転パケットを無視して視点を維持。
 * FDPClient の NoRotate / CheatUtils の noRotate 統合。
 * 教育目的の簡易実装: 回転上書きパケット受信時にプレイヤーの手動視点を保持。
 * (本格実装は PacketReceiveEvent で PlayerPositionLookS2CPacket の yaw/pitch を
 *  クライアント現在値に書き換える)
 */
public class NoRotate extends Module {

    private final BoolSetting silent = addSetting(new BoolSetting("Silent", "Keep rotations but appear to comply", true));

    public NoRotate() {
        super("NoRotate", "Prevents server from rotating your head", Category.PLAYER);
    }

    public boolean isSilent() {
        return silent.get();
    }
}
