package com.nttdata.bc39.grupo04.product.enumerator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 
public class EnumTypeProductValueValidator implements ConstraintValidator<Enum, String>
{
    private Enum annotation;
 
    @Override
    public void initialize(Enum annotation)
    {
        this.annotation = annotation;
    }
 
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext)
    {
    	if (value == null) return true; 
    	return Arrays.stream(TypeProductEnum.values()).anyMatch(e  -> e.getName().equals(value));
    }
}