package nl.tno.oorti.accessor.consumer;

@FunctionalInterface
public interface ObjByteConsumer<T> {

  void accept(T t, byte b);
}
