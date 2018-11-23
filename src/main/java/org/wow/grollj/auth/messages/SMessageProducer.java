package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;

public interface SMessageProducer<T extends SMessage> {
    T produce(ByteBuf in);
}
