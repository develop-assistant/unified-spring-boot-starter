package cn.idea360.unified.response;

import java.io.Serializable;

/**
 * @author cuishiying
 */
public class UnifiedResult<T> implements Serializable {

	public static final int SUCCESS = 0;

	public static final int ERROR = -1;

	private String msg;

	private int code = SUCCESS;

	private T data;

	public UnifiedResult() {

	}

	public UnifiedResult(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static class Builder<T> {

		private String msg = "OK";

		private int code = SUCCESS;

		private T data;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public Builder<T> data(T data) {
			this.data = data;
			return this;
		}

		public Builder<T> error(int code, String msg) {
			this.code = code;
			this.msg = msg;
			return this;
		}

		public Builder<T> error(int code) {
			this.code = code;
			return this;
		}

		public UnifiedResult<T> build() {
			return new UnifiedResult<>(this.code, this.msg, this.data);
		}

		public Builder<T> message(String msg) {
			this.msg = msg;
			return this;
		}

	}

}
