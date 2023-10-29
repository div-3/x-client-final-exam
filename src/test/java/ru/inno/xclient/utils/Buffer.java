package ru.inno.xclient.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Buffer {
    private Object buffer;
    private List<Object> listBuffer;

    public Object getBuffer(){
        Object retObject = buffer;
        buffer = null;
        return retObject;
    }
    public <T> void setListBuffer(List<T> ts){
        this.listBuffer = ts
                .stream()
                .map(c -> (Object) c)
                .collect(Collectors.toList());
    }
     public <T> List<T> getListBuffer(T t){
        List<T> retList =  listBuffer
                 .stream()
                 .map(c -> (T)c)
                 .collect(Collectors.toList());
        listBuffer.clear();
        return retList;
     }
}
