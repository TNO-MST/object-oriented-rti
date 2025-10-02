package nl.tno.oorti.accessor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Array accessor implementation as a replacement for the slower java.lang.reflect.array
 * implementation.
 *
 * <p>See also: https://stackoverflow.com/questions/30306160/performance-of-java-lang-reflect-array
 *
 * @author bergtwvd
 */
public interface ArrayAccessor {

  public Object get(Object array, int index);

  public void set(Object array, int index, Object value);

  public int length(Object array);

  public Object newInstance(int size);

  static ArrayAccessor create(Class type) {
    if (type.equals(short.class)) {
      return new ShortArrayAccessor();
    } else if (type.equals(Short.class)) {
      return new ShortObjectArrayAccessor();
    } else if (type.equals(int.class)) {
      return new IntArrayAccessor();
    } else if (type.equals(Integer.class)) {
      return new IntObjectArrayAccessor();
    } else if (type.equals(long.class)) {
      return new LongArrayAccessor();
    } else if (type.equals(Long.class)) {
      return new LongObjectArrayAccessor();
    } else if (type.equals(float.class)) {
      return new FloatArrayAccessor();
    } else if (type.equals(Float.class)) {
      return new FloatObjectArrayAccessor();
    } else if (type.equals(double.class)) {
      return new DoubleArrayAccessor();
    } else if (type.equals(Double.class)) {
      return new DoubleObjectArrayAccessor();
    } else if (type.equals(char.class)) {
      return new CharArrayAccessor();
    } else if (type.equals(Character.class)) {
      return new CharObjectArrayAccessor();
    } else if (type.equals(byte.class)) {
      return new ByteArrayAccessor();
    } else if (type.equals(Byte.class)) {
      return new ByteObjectArrayAccessor();
    } else if (type.equals(List.class)) {
      return new ListAccessor();
    } else {
      return new ObjectArrayAccessor(type);
    }
  }

  class ShortArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((short[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((short[]) array)[index] = (short) value;
    }

    @Override
    public int length(Object array) {
      return ((short[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new short[size];
    }
  }

  class ShortObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Short[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Short[]) array)[index] = (Short) value;
    }

    @Override
    public int length(Object array) {
      return ((Short[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Short[size];
    }
  }

  class IntArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((int[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((int[]) array)[index] = (int) value;
    }

    @Override
    public int length(Object array) {
      return ((int[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new int[size];
    }
  }

  class IntObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Integer[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Integer[]) array)[index] = (Integer) value;
    }

    @Override
    public int length(Object array) {
      return ((Integer[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Integer[size];
    }
  }

  class LongArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((long[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((long[]) array)[index] = (long) value;
    }

    @Override
    public int length(Object array) {
      return ((long[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new long[size];
    }
  }

  class LongObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Long[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Long[]) array)[index] = (Long) value;
    }

    @Override
    public int length(Object array) {
      return ((Long[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Long[size];
    }
  }

  class FloatArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((float[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((float[]) array)[index] = (float) value;
    }

    @Override
    public int length(Object array) {
      return ((float[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new float[size];
    }
  }

  class FloatObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Float[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Float[]) array)[index] = (Float) value;
    }

    @Override
    public int length(Object array) {
      return ((Float[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Float[size];
    }
  }

  class DoubleArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((double[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((double[]) array)[index] = (double) value;
    }

    @Override
    public int length(Object array) {
      return ((double[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new double[size];
    }
  }

  class DoubleObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Double[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Double[]) array)[index] = (Double) value;
    }

    @Override
    public int length(Object array) {
      return ((Double[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Double[size];
    }
  }

  class CharArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((char[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((char[]) array)[index] = (char) value;
    }

    @Override
    public int length(Object array) {
      return ((char[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new char[size];
    }
  }

  class CharObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Character[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Character[]) array)[index] = (Character) value;
    }

    @Override
    public int length(Object array) {
      return ((Character[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Character[size];
    }
  }

  class ByteArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((byte[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((byte[]) array)[index] = (byte) value;
    }

    @Override
    public int length(Object array) {
      return ((byte[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new byte[size];
    }
  }

  class ByteObjectArrayAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      return ((Byte[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Byte[]) array)[index] = (Byte) value;
    }

    @Override
    public int length(Object array) {
      return ((Byte[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return new Byte[size];
    }
  }

  class ListAccessor implements ArrayAccessor {

    @Override
    public Object get(Object array, int index) {
      if (index < ((List) array).size()) {
        return ((List) array).get(index);
      } else {
        // return null for index oustide range
        return null;
      }
    }

    @Override
    public void set(Object array, int index, Object value) {
      List<Object> list = (List<Object>) array;
      int size = list.size();

      if (index == size) {
        // just add the value
        list.add(value);
      } else if (index < size) {
        list.set(index, value);
      } else {
        // (index > size)
        // fill up intermediate elements with nulls
        for (int i = size; i < index; i++) {
          list.add(null);
        }
        // add the value
        list.add(value);
      }
    }

    @Override
    public int length(Object array) {
      return ((List) array).size();
    }

    @Override
    public Object newInstance(int size) {
      return new ArrayList(size);
    }
  }

  class ObjectArrayAccessor implements ArrayAccessor {

    Class type;

    public ObjectArrayAccessor(Class type) {
      this.type = type;
    }

    @Override
    public Object get(Object array, int index) {
      return ((Object[]) array)[index];
    }

    @Override
    public void set(Object array, int index, Object value) {
      ((Object[]) array)[index] = value;
    }

    @Override
    public int length(Object array) {
      return ((Object[]) array).length;
    }

    @Override
    public Object newInstance(int size) {
      return Array.newInstance(type, size);
    }
  }
}
