package me.emmy.tulip.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
@Getter
@Setter
public class MutableTriple<A, B,C> {
    private A a;
    private B b;
    private C c;

    /**
     * Create a new mutable triple
     *
     * @param a the first value
     * @param b the second value
     * @param c the third value
     */
    public MutableTriple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}