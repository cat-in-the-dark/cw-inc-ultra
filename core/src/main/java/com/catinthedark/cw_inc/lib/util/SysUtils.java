package com.catinthedark.cw_inc.lib.util;

import com.catinthedark.cw_inc.lib.common.LogicalFunction;

import java.util.function.Supplier;

/**
 * Created by over on 01.12.14.
 */
public class SysUtils {
    public static LogicalFunction conditional(Supplier<Boolean> predicate, LogicalFunction fn) {
        return (delay) -> {
            if (predicate.get())
                fn.apply(delay);
        };
    }
}
