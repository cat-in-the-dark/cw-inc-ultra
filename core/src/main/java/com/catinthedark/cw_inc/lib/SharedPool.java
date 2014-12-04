package com.catinthedark.cw_inc.lib;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by over on 15.11.14.
 */
public class SharedPool<T> {
    private final T[] memory;
    Queue<Integer> pointerPool;

    public SharedPool(Class<? extends T> clazz, int size) {
        memory = (T[]) Array.newInstance(clazz, size);
        pointerPool = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toCollection(() -> new LinkedList<>()));
    }

    public T map(int pointer) {
        return memory[pointer];
    }

    public int alloc(T data) {
        System.out.println("alloc:poll");
        int pointer = pointerPool.poll();
        System.out.println("after alloc:poll");
        memory[pointer] = data;
        return pointer;
    }


    public void update(int pointer, Consumer<T> update) {
        update.accept(memory[pointer]);
    }

    public void free(int pointer) {
        pointerPool.add(pointer);
    }
}
