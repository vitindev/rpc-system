package com.vitindev.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class KryoUtils {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        final var kryo = new Kryo();
        kryo.register(UUID.class, new DefaultSerializers.UUIDSerializer());
        kryo.setRegistrationRequired(false); // Permite a auto-registração de classes
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy())); // Lida com classes que não tem construtores vazios
        return kryo;
    });

    private static Kryo getKryo() {
        return kryoThreadLocal.get();
    }

    public static byte[] serialize(Object obj) {

        final var dataOutputStream = new ByteArrayOutputStream();

        try (final var output = new Output(dataOutputStream)) {
            getKryo().writeClassAndObject(output, obj);
        }

        return dataOutputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] data) {

        final var dataInputStream = new ByteArrayInputStream(data);

        try (final var input = new Input(dataInputStream)) {
            return (T) getKryo().readClassAndObject(input);
        }

    }

}