package com.java.springportfolio.config;

import com.java.springportfolio.entity.VoteType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VoteTypeConverter implements Converter<String, VoteType> {

    @Override
    public VoteType convert(String source) {
        return VoteType.lookup(source);
    }
}
