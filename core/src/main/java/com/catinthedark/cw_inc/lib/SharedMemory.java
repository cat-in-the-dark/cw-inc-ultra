package com.catinthedark.cw_inc.lib;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by over on 15.11.14.
 */
public class SharedMemory<T> {
    private final T[] memory;
    Queue<Integer> pointerPool;

    public final Reader reader = new Reader();
    public final Writer writer = new Writer();

    public SharedMemory(Class<? extends T> clazz, int size) {
        memory = (T[]) Array.newInstance(clazz, size);
        pointerPool = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toCollection(() -> new LinkedList<>()));
    }

    public class Reader {
        public T map(int pointer) {
            return memory[pointer];
        }
    }

    public class Writer {
        public int alloc(T data) {
            System.out.println("alloc:poll");
            int pointer = pointerPool.poll();
            System.out.println("after alloc:poll");
            memory[pointer] = data;
            return pointer;
        }

        public T map(int pointer) {
            return memory[pointer];
        }

        public void free(int pointer) {
            pointerPool.add(pointer);
        }
    }
}
