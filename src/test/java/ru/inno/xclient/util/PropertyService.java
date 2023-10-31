package ru.inno.xclient.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyService {
    private final static String API_PROPERTIES_FILE_PATH = "src/test/resources/API_x_client.properties";
    private int counter;

    private PropertyService() {}

    public static PropertyService getInstance() {
        return PropertyService.SingletonHolder.HOLDER_INSTANCE;
    }

    public static class SingletonHolder {
        public static final PropertyService HOLDER_INSTANCE = new PropertyService();
    }

    public String getProperty(PropertiesType type, String propertyName){
        switch (type){
            case API -> {return getProperties(API_PROPERTIES_FILE_PATH).getProperty(propertyName);}
            default -> {return null;}
        }
    }


    public Properties getProperties(PropertiesType type){
        switch (type){
            case API -> {return getProperties(API_PROPERTIES_FILE_PATH);}
            default -> {return null;}
        }
    }

    //Получить параметры из файла
    private Properties getProperties(String path) {
        File propFile = new File(path);
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
