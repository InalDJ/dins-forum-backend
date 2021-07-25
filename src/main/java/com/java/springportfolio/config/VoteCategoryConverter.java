package com.java.springportfolio.config;

import com.java.springportfolio.entity.VoteCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VoteCategoryConverter implements Converter<String, VoteCategory> {

    @Override
    public VoteCategory convert(String source) {
        return VoteCategory.lookup(source);
    }
}
