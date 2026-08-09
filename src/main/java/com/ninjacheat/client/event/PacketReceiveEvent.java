package com.ninjacheat.client.event;

/**
 * パケット受信時に発火。キャンセルで処理を抑制可能。
 */
public class PacketReceiveEvent extends Cancellable {

    private Object packet;

    public PacketReceiveEvent(Object packet) {
        this.packet = packet;
    }

    public Object getPacket() {
        return packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) packet;
    }
}
