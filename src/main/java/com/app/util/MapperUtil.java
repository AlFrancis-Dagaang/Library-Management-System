package com.app.util;

import org.modelmapper.ModelMapper;

public class MapperUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }
}
