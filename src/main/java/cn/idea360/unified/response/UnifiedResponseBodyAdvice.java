package cn.idea360.unified.response;

import cn.idea360.unified.annotation.UnifiedIgnore;
import cn.idea360.unified.configuration.UnifiedProperties;
import cn.idea360.unified.exception.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author cuishiying
 */
@EnableConfigurationProperties(UnifiedProperties.class)
@ConditionalOnProperty(value = "idea360.framework.unified.enable", havingValue = "true", matchIfMissing = true)
@RestControllerAdvice
public class UnifiedResponseBodyAdvice implements ResponseBodyAdvice<Object>, BeanFactoryAware {

	// 过滤swagger相关的请求的接口，不然swagger会提示base-url被拦截
	private static final List<String> INNER_IGNORES = Arrays.asList("/swagger-ui.html", "/swagger-ui/**",
			"/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/webjars/**", "/doc**", "/version",
			"/actuator/**", "/favicon.ico");

	private ObjectMapper objectMapper;

	@Resource
	private UnifiedProperties unifiedProperties;

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return !returnType.getGenericParameterType().equals(UnifiedResult.class)
				&& !returnType.hasMethodAnnotation(UnifiedIgnore.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		String servletPath = ((ServletServerHttpRequest) request).getServletRequest().getServletPath();
		if (this.ignoring(servletPath)) {
			return body;
		}
		// returnType.getGenericParameterType():返回约定类型 body:返回数据类型
		if (returnType.getGenericParameterType().equals(String.class) || body instanceof String) {
			try {
				response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
				return objectMapper.writeValueAsString(new UnifiedResult.Builder<>().data(body).build());
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new BizException("返回String类型错误");
			}
		}

		return new UnifiedResult.Builder<>().data(body).build();
	}

	private boolean ignoring(String uri) {
		return this.contains(INNER_IGNORES, uri) || this.contains(unifiedProperties.getIgnores(), uri);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		objectMapper = beanFactory.getBean(ObjectMapper.class);
	}

	protected boolean contains(List<String> paths, String servletPath) {
		if (Objects.nonNull(paths) && !paths.isEmpty()) {
			for (String path : paths) {
				String uriPattern = path.trim();
				if (ANT_PATH_MATCHER.match(uriPattern, servletPath)) {
					return true;
				}
			}
		}
		return false;
	}

}
