package com.board.testboard.database;

import java.util.List;

public interface Dao<T> {
    boolean saveOrUpdate(T t);
    List<T> getAll();
    T getObjectById(String s);
}
 