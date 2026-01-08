package net.togyk.myneheroes.util;

import java.util.Objects;

@FunctionalInterface
public interface HexConsumer<A, B, C, D, E, F> {
    void accept(A a, B b, C c, D d, E e, F f);

    default HexConsumer<A, B, C, D, E, F> andThen(HexConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d, E e, F f) -> {
            accept(a, b, c, d, e, f);
            after.accept(a, b, c, d, e, f);
        };
    }
}
