package cn.idea360.unified.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author cuishiying
 */
public class BizException extends RuntimeException {

	private Integer code;

	private String message;

	private String stackTrace;

	private transient Throwable throwable;

	public BizException() {
	}

	public BizException(String message) {
		super(message);
		this.code = this.formatErrCode(-1);
		this.message = message;
	}

	public BizException(int code, String message) {
		super(message);
		this.code = this.formatErrCode(code);
		this.message = message;
	}

	public BizException(int code, Throwable throwable) {
		super(throwable);
		this.code = this.formatErrCode(code);
		this.message = throwable.getMessage();
		this.stackTrace = ExceptionUtils.getStackTrace(throwable);
		this.throwable = throwable;
	}

	public BizException(int code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = this.formatErrCode(code);
		this.message = message;
		this.throwable = throwable;
		this.stackTrace = ExceptionUtils.getStackTrace(throwable);
	}

	private int formatErrCode(int errCode) {
		return errCode == 0 ? -1 : errCode;
	}

	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		if (!StringUtils.isEmpty(this.message)) {
			return this.message;
		}
		else {
			return this.throwable != null ? this.throwable.getMessage() : "";
		}
	}

	public String getStackTrace2String() {
		return this.stackTrace == null ? "" : this.stackTrace;
	}

	@Override
	public String toString() {
		Integer errCode = this.getCode();
		return "ErrCode:" + errCode + ", ErrMsg:" + this.getMessage();
	}

}
