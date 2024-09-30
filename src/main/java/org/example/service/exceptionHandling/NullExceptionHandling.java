package org.example.service.exceptionHandling;

import org.example.domain.BaseEntity;
import org.example.exeptions.NotFoundSomething;

import java.util.Objects;

public class NullExceptionHandling<T extends BaseEntity> {
    private static NullExceptionHandling nullExceptionHandling;
    static {
        nullExceptionHandling = new NullExceptionHandling();
    }

    public static NullExceptionHandling getInstance() {
        return nullExceptionHandling;
    }
    public T handlingNullExceptions(T t ,Class<T> tClass) throws NotFoundSomething {
        if (Objects.isNull(t)){
             throw  new NotFoundSomething(tClass.getClass().getName());
        }
        return (T) t;
    }
}
