package org.wow.grollj.world.packet;

import io.netty.buffer.ByteBuf;

public interface SPacketProducer<T extends SPacket> {
    T produce(ByteBuf in);
}
