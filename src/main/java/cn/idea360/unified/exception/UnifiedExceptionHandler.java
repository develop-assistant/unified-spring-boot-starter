package cn.idea360.unified.exception;

import cn.idea360.unified.response.UnifiedResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author cuishiying
 */
@RestControllerAdvice
public class UnifiedExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(UnifiedExceptionHandler.class);

	@ExceptionHandler({ BindException.class })
	public UnifiedResult<?> exceptionHandler(BindException e) {
		log.error("参数绑定异常", e);
		BindingResult bindingResult = e.getBindingResult();
		return new UnifiedResult.Builder<>()
				.error(UnifiedResult.ERROR, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
				.build();
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public UnifiedResult<?> exceptionHandler(MethodArgumentNotValidException e) {
		log.error("参数校验异常", e);
		BindingResult bindingResult = e.getBindingResult();
		return new UnifiedResult.Builder<>()
				.error(UnifiedResult.ERROR, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
				.build();
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public UnifiedResult<?> handler(ConstraintViolationException e) {
		log.error("参数验证异常", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, e.getMessage()).build();
	}

	@ExceptionHandler(HttpMessageConversionException.class)
	public UnifiedResult<?> handler(HttpMessageConversionException e) {
		log.error("参数类型转换错误", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, e.getMessage()).build();
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public UnifiedResult<?> handler(HttpMessageNotReadableException e) {
		log.error("参数格式异常", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, e.getMessage()).build();
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public UnifiedResult<?> handler(HttpRequestMethodNotSupportedException e) {
		log.error("请求方式异常", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, "请求方式错误").build();
	}

	@ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
	public UnifiedResult<?> handler(HttpMediaTypeNotSupportedException e) {
		log.error("媒体类型异常", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, "媒体类型错误").build();
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public UnifiedResult<?> handler(MissingServletRequestParameterException e) {
		log.error("请求参数丢失", e);
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, e.getMessage()).build();
	}

	@ExceptionHandler({ BizException.class })
	public UnifiedResult<?> exceptionHandler(BizException e) {
		log.error("业务异常", e);
		return new UnifiedResult.Builder<>().error(e.getCode(), e.getMessage()).build();
	}

	@ExceptionHandler(value = Throwable.class)
	public UnifiedResult<?> exceptionHandler(Throwable e) {
		log.error("兜底异常: {}", ExceptionUtils.getStackTrace(e));
		String message = e.getMessage();
		if (message.contains("RpcException")) {
			message = "dubbo服务异常";
		}
		return new UnifiedResult.Builder<>().error(UnifiedResult.ERROR, message).build();
	}

}
