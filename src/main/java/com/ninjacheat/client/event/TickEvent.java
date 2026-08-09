package com.ninjacheat.client.event;

/**
 * クライアントティック毎に発火するイベント。
 */
public class TickEvent implements Event {

    public static final TickEvent INSTANCE = new TickEvent();

    private TickEvent() {}
}
