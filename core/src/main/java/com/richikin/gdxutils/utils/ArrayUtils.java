package com.richikin.gdxutils.utils;

// XXX these should be changed to reflect the actual memory allocator we use.
// it looks like right now objects want to be powers of 2 minus 8
// and the array size eats another 4 bytes

// NOTE: This is NOT my code. I cannot remember where i got it from.
//       If anyone reading this recognises this code as theirs,
//       please let me know.

import java.lang.reflect.Array;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class ArrayUtils
{
    private static final Object[] EMPTY      = new Object[0];
    private static final int      CACHE_SIZE = 73;
    private static final Object[] sCache     = new Object[CACHE_SIZE];

    private static int idealByteArraySize(int need)
    {
        for (int i = 4; i < 32; i++)
        {
            if (need <= ((1 << i) - 12))
            {
                return (1 << i) - 12;
            }
        }

        return need;
    }

    public static int idealBooleanArraySize(int need)
    {
        return idealByteArraySize(need);
    }

    public static int idealShortArraySize(int need)
    {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealCharArraySize(int need)
    {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealIntArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealFloatArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealObjectArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need)
    {
        return idealByteArraySize(need * 8) / 8;
    }

    public static boolean equals(byte[] array1, byte[] array2, int length)
    {
        if (length < 0)
        {
            throw new IllegalArgumentException();
        }

        if (array1 == array2)
        {
            return true;
        }

        if ((array1 == null) || (array2 == null) || (array1.length < length) || (array2.length < length))
        {
            return false;
        }

        for (int i = 0; i < length; i++)
        {
            if (array1[i] != array2[i])
            {
                return false;
            }
        }

        return true;
    }

    public static <T> T[] emptyArray(Class<T> kind)
    {
        if (kind == Object.class)
        {
            return (T[]) EMPTY;
        }

        int    bucket = ((System.identityHashCode(kind) / 8) & 0x7FFFFFFF) % CACHE_SIZE;
        Object cache  = sCache[bucket];

        if (cache == null || cache.getClass().getComponentType() != kind)
        {
            cache          = Array.newInstance(kind, 0);
            sCache[bucket] = cache;
        }

        return (T[]) cache;
    }

    public static <T> boolean contains(T[] array, T value)
    {
        return indexOf(array, value) != -1;
    }

    public static <T> int indexOf(T[] array, T value)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == null)
            {
                if (value == null)
                {
                    return i;
                }
            }
            else
            {
                if (array[i].equals(value))
                {
                    return i;
                }
            }
        }

        return -1;
    }

    public static <T> boolean containsAll(T[] array, T[] check)
    {
        for (T checkItem : check)
        {
            if (!contains(array, checkItem))
            {
                return false;
            }
        }

        return true;
    }

    public static <T> int countItems(T[] array, T check)
    {
        int count = 0;

        for (T element : array)
        {
            if (element == check)
            {
                count++;
            }
        }

        return count;
    }

    public static boolean contains(int[] array, int value)
    {
        for (int element : array)
        {
            if (element == value)
            {
                return true;
            }
        }

        return false;
    }

    public static long total(long[] array)
    {
        long total = 0;

        for (long value : array)
        {
            total += value;
        }

        return total;
    }

    public static <T> T[] appendElement(Class<T> kind, T[] array, T element)
    {
        T[] result;
        int end;

        if (array != null)
        {
            end    = array.length;
            result = (T[]) Array.newInstance(kind, end + 1);
            System.arraycopy(array, 0, result, 0, end);
        }
        else
        {
            end    = 0;
            result = (T[]) Array.newInstance(kind, 1);
        }

        result[end] = element;

        return result;
    }

    public static <T> T[] removeElement(Class<T> kind, T[] array, T element)
    {
        if (array != null)
        {
            int length = array.length;

            for (int i = 0; i < length; i++)
            {
                if (array[i] == element)
                {
                    if (length == 1)
                    {
                        return null;
                    }

                    T[] result = (T[]) Array.newInstance(kind, length - 1);

                    System.arraycopy(array, 0, result, 0, i);
                    System.arraycopy(array, i + 1, result, i, length - i - 1);

                    return result;
                }
            }
        }

        return array;
    }

    public static int[] appendInt(int[] cur, int val)
    {
        if (cur == null)
        {
            return new int[]{val};
        }

        int N = cur.length;

        for (int aCur : cur)
        {
            if (aCur == val)
            {
                return cur;
            }
        }

        int[] ret = new int[N + 1];

        System.arraycopy(cur, 0, ret, 0, N);
        ret[N] = val;

        return ret;
    }

    public static int[] removeInt(int[] cur, int val)
    {
        if (cur == null)
        {
            return null;
        }

        int N = cur.length;

        for (int i = 0; i < N; i++)
        {
            if (cur[i] == val)
            {
                int[] ret = new int[N - 1];

                if (i > 0)
                {
                    System.arraycopy(cur, 0, ret, 0, i);
                }

                if (i < (N - 1))
                {
                    System.arraycopy(cur, i + 1, ret, i, N - i - 1);
                }

                return ret;
            }
        }

        return cur;
    }

    public static <T> T[] deepCopyOf(T[] array)
    {
        if (0 >= array.length)
        {
            return array;
        }

        return (T[]) deepCopyOf
            (
                array,
                Array.newInstance(array[0].getClass(), array.length),
                0
            );
    }

    private static Object deepCopyOf(Object array, Object copiedArray, int index)
    {
        if (index >= Array.getLength(array))
        {
            return copiedArray;
        }

        Object element = Array.get(array, index);

        if (element.getClass().isArray())
        {
            Array.set
                (
                    copiedArray, index, deepCopyOf
                        (
                            element,
                            Array.newInstance
                                (
                                    element.getClass().getComponentType(),
                                    Array.getLength(element)
                                ),
                            0
                        )
                );
        }
        else
        {
            Array.set(copiedArray, index, element);
        }

        return deepCopyOf(array, copiedArray, ++index);
    }
}
