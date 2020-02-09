package goodbank.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DivisableByConstraintValidator.class})
public @interface DivisableBy {

    String message() default "Must be divisable by {value}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    long value();
}
