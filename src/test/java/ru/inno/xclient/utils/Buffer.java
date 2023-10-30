package ru.inno.xclient.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Buffer {
    private Map<String, Object> buffer = new HashMap<>();
    private Map<String, List<Object>> listBuffer = new HashMap<>();


    public <T> void save(String varName, T t) {
        //Если такая переменная уже сохранена, то сначала очищаем значение
        if (buffer.containsKey(varName)) buffer.entrySet().removeIf(entry -> entry.getKey().contains(varName));

        this.buffer.put(varName, t);
    }

    public <T> T get(String varName) {
        return (T) buffer.get(varName);
    }

    public <T> void saveList(String varName, List<T> ts) {
        //Если такая переменная уже сохранена, то сначала очищаем значение
        if (listBuffer.containsKey(varName)) listBuffer.entrySet().removeIf(entry -> entry.getKey().contains(varName));

        List<Object> tmpList = ts.stream().map(c -> (Object) c).toList();
        this.listBuffer.put(varName, tmpList);
    }

    public <T> List<T> getList(String varName) {
        return listBuffer.get(varName).stream().map(c -> (T) c).toList();
    }
}
