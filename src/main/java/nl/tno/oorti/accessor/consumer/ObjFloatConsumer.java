package nl.tno.oorti.accessor.consumer;

@FunctionalInterface
public interface ObjFloatConsumer<T> {

  void accept(T t, float b);
}
