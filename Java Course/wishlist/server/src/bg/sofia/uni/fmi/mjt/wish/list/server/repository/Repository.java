package bg.sofia.uni.fmi.mjt.wish.list.server.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface Repository<T, V> {
    String add(T key, V value);
    void remove(T element);
}
