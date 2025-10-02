package nl.tno.oorti.accessor.consumer;

@FunctionalInterface
public interface ObjBooleanConsumer<T> {

  void accept(T t, boolean b);
}
