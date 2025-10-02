package nl.tno.oorti.accessor.consumer;

@FunctionalInterface
public interface ObjShortConsumer<T> {

  void accept(T t, short b);
}
