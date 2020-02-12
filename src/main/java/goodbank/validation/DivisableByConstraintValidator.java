
package goodbank.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DivisableByConstraintValidator implements ConstraintValidator<DivisableBy, Integer> {

    private long divisor;

    public void initialize(DivisableBy constraintAnnotation) {
        if (constraintAnnotation.value() < 1) {
            throw new IllegalArgumentException("Can not divide by 0 or negative value.");
        }
        this.divisor = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return value % divisor == 0;
    }
}
