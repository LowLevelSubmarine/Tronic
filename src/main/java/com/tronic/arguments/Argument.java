package com.tronic.arguments;

public interface Argument<T> {

    T parse(Arguments arguments) throws InvalidArgumentException;

}
