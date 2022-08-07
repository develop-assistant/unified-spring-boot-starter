package cn.idea360.unified.request;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.validation.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * 校验请求体参数
 *
 * @author cuishiying
 */
@ConditionalOnProperty(value = "idea360.framework.unified.enable", havingValue = "true", matchIfMissing = true)
@RestControllerAdvice
public class ValidateRequestBodyAdvice implements RequestBodyAdvice {

	private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class).configure()
			.failFast(true).buildValidatorFactory().getValidator();

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		// 已添加注解的无需二次处理
		boolean matchValidAnnotation = Arrays.stream(methodParameter.getMethodAnnotations())
				.anyMatch(t -> t.annotationType().equals(Validated.class) || t.annotationType().equals(Valid.class));
		// String不校验
		boolean matchString = methodParameter.getParameterType().isAssignableFrom(String.class);
		// 匹配则校验
		boolean matchPostOrPutMapping = Arrays.stream(methodParameter.getMethodAnnotations()).anyMatch(
				t -> t.annotationType().equals(PostMapping.class) || t.annotationType().equals(PutMapping.class));
		// 匹配则校验
		boolean matchPostOrPutRequestMapping = Arrays.stream(methodParameter.getMethodAnnotations())
				.anyMatch(t -> t.annotationType().equals(RequestMapping.class)
						&& Arrays.stream(AnnotationUtils.getAnnotation(t, RequestMapping.class).method())
								.anyMatch(m -> m.equals(RequestMethod.POST) || m.equals(RequestMethod.PUT)));
		return (matchPostOrPutMapping || matchPostOrPutRequestMapping) && !matchValidAnnotation && !matchString;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(body);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

}
