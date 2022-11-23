package com.project.lovlev.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import static com.project.lovlev.enums.Sex.*;

@Converter
public class SexConverter implements AttributeConverter<Sex, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sex sex) {
        return switch (sex) {
            case Male -> 1;
            case Female -> 2;
            case Other -> 3;
        };
    }

    @Override
    public Sex convertToEntityAttribute(Integer integer) {
        return switch (integer){
            case 1 -> Male;
            case 2 -> Female;
            case 3 -> Other;
            default -> throw new IllegalStateException("Unexpected value: " + integer);
        };
    }
}
