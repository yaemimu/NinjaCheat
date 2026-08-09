package com.ninjacheat.client.event;

/**
 * パケット送信時に発火。キャンセルで送信を抑制可能。
 */
public class PacketSendEvent extends Cancellable {

    private Object packet;

    public PacketSendEvent(Object packet) {
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
