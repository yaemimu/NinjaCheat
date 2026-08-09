package com.ninjacheat.client.event;

import com.ninjacheat.client.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * イベント配送の中核。
 * モジュールが有効化された時に @EventHandler アノテーション付きメソッドを
 * スキャンして購読登録し、イベント発火時に呼び出す。
 * FDPClient の EventManager / CheatUtils の EventBroker を統合したシンプル版。
 */
public class EventManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("NinjaCheat/Events");

    /** イベント型 -> [購読者] */
    private final Map<Class<?>, List<Subscription>> subscriptions = new ConcurrentHashMap<>();

    /** モジュール有効化時に呼ばれる: @EventHandler を自動登録 */
    public void subscribe(Module module) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                if (!Event.class.isAssignableFrom(eventType)) continue;
                method.setAccessible(true);
                subscriptions.computeIfAbsent(eventType, k -> new ArrayList<>())
                        .add(new Subscription(module, method));
            }
        }
    }

    /** モジュール無効化時に呼ばれる: 当該モジュールの購読を全削除 */
    public void unsubscribe(Module module) {
        for (List<Subscription> list : subscriptions.values()) {
            list.removeIf(sub -> sub.module == module);
        }
    }

    /** イベントを発火して購読者に配送。イベント自身を返す (キャンセル状態確認用) */
    @SuppressWarnings("unchecked")
    public <T extends Event> T post(T event) {
        List<Subscription> list = subscriptions.get(event.getClass());
        if (list == null || list.isEmpty()) return event;
        // コピーして反復中の変更に強くする
        for (Subscription sub : new ArrayList<>(list)) {
            try {
                sub.method.invoke(sub.module, event);
            } catch (Throwable t) {
                LOGGER.error("Error dispatching {} to {}#{}", event.getClass().getSimpleName(),
                        sub.module.getClass().getSimpleName(), sub.method.getName(), t);
            }
        }
        return event;
    }

    private record Subscription(Module module, Method method) {}
}
