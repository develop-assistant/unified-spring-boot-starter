package cn.idea360.unified.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cuishiying
 */
@ConfigurationProperties(prefix = UnifiedProperties.UNIFIED_PREFIX)
public class UnifiedProperties {

	public static final String UNIFIED_PREFIX = "idea360.framework.unified";

	private final List<String> ignores = new ArrayList<>();

	private Boolean enable = true;

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public List<String> getIgnores() {
		return ignores;
	}

}
