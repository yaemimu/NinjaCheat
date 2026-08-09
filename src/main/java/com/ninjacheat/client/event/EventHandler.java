package com.ninjacheat.client.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * モジュール内のイベントハンドラメソッドを示すアノテーション。
 * 単一の Event 引数を取るメソッドに付与する。
 * <pre>
 *   &#64;EventHandler
 *   private void onPacket(PacketSendEvent e) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
}
