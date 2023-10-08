package com.yanggu.metric_calculate.core.kryo.serializer.window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoLongSerializer;
import com.yanggu.metric_calculate.core.window.SessionWindow;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 会话窗口序列化和反序列化器
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public class SessionWindowSerializer<IN, ACC, OUT> extends Serializer<SessionWindow<IN, ACC, OUT>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8703347303877277421L;

    private final KryoLongSerializer longSerializer = new KryoLongSerializer();

    private final KryoCollectionSerializer<List> inListSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, SessionWindow<IN, ACC, OUT> sessionWindow) {
        longSerializer.write(kryo, output, sessionWindow.getStartTimestamp());
        longSerializer.write(kryo, output, sessionWindow.getEndTimestamp());
        inListSerializer.write(kryo, output, sessionWindow.getInList());
    }

    @Override
    public SessionWindow<IN, ACC, OUT> read(Kryo kryo, Input input, Class<? extends SessionWindow<IN, ACC, OUT>> type) {
        SessionWindow<IN, ACC, OUT> sessionWindow = new SessionWindow<>();
        sessionWindow.setStartTimestamp(longSerializer.read(kryo, input, Long.class));
        sessionWindow.setEndTimestamp(longSerializer.read(kryo, input, Long.class));
        sessionWindow.setInList(inListSerializer.read(kryo, input, ArrayList.class));
        return sessionWindow;
    }

}
