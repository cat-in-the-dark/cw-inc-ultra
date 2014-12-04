package com.catinthedark.cw_inc.lib;

import sun.java2d.pipe.SpanIterator;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by over on 03.12.14.
 */
public class SharedArray<T> {
    private final T[] array;
    private final int _length;
    public final Reader reader = new Reader();
    public final Writer writer = new Writer();

    public SharedArray(T[] array) {
        this.array = array;
        _length = array.length;
    }

    public class Reader implements Iterable<T> {
        public final int length = _length;
        public T get(int index) {
            return array[index];
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int index = 0;

                @Override
                public boolean hasNext() {
                    return index < array.length;
                }

                @Override
                public T next() {
                    return array[index++];
                }
            };
        }

        @Override
        public Spliterator<T> spliterator() {
            return Spliterators.spliterator(array, Spliterator.SIZED);
        }

        public Stream<T> stream(){
            return StreamSupport.stream(spliterator(), false);
        }


        /**
         * Great responsibility! Don't modify returned array!
         * @return
         */
        public T[] mapRaw(){
            return array;
        }
    }

    public class Writer {
        public final int length = _length;
        public void update(int index, Consumer<T> updater) {
            updater.accept(array[index]);
        }
    }


}
