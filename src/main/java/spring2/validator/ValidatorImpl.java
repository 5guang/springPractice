package spring2.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author 张文光
 * @version 1.0
 * @Date 2018/12/17 0017 21:57
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    /**
     * 实现校验方法并返回校验结果
     * @param bean
     * @return
     */
    public ValidationResult validate(Object bean) {
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);

        if (constraintViolations.size() > 0) {
            result.setHasErrors(true);
            constraintViolations.forEach(a->{
                String errMsg = a.getMessage();
                String properName = a.getPropertyPath().toString();
                result.getErrorMsgMap().put(properName,errMsg);
            });
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
