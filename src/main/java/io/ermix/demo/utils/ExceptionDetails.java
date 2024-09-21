package io.ermix.demo.utils;

import jakarta.validation.ConstraintViolation;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionDetails {


    public static String getDetails(Set<ConstraintViolation<?>> errors) {
        return errors.stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }

    public static String getDetails(String message) {
        Matcher matcher = Pattern
                .compile("Key \\((.*?)\\)=\\(.*?\\) (.*?\\.)")
                .matcher(message);
        return matcher.find() ? matcher.group(1) + ": " + matcher.group(2) : "";
    }

    public static String getDetails(List<ObjectError> errors) {
        return errors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }
}
