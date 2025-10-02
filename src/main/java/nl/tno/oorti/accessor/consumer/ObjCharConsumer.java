package nl.tno.oorti.accessor.consumer;

@FunctionalInterface
public interface ObjCharConsumer<T> {

  void accept(T t, char b);
}
