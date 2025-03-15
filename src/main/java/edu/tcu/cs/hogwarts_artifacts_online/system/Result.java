package edu.tcu.cs.hogwarts_artifacts_online.system;

public class Result {
	
	
	private boolean flag;
	private Integer code;
	private String message;
	private Object data;
	
	
	public Result() {
		super();
	}


	public Result(boolean flag, Integer code, String message) {
		super();
		this.flag = flag;
		this.code = code;
		this.message = message;
	}


	public Result(boolean flag, Integer code, String message, Object data) {
		super();
		this.flag = flag;
		this.code = code;
		this.message = message;
		this.data = data;
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
	
	
	
}
