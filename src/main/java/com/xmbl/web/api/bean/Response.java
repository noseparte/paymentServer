package com.xmbl.web.api.bean;

public class Response {
	 	private static final String STATE = "ok";
	    private static final String ERROR = "error";

	    private Meta meta;	//response的对象(状态 | 状态码)
	    private Object data;	//response的数据

	    public Response success() {
	        this.meta = new Meta(true, STATE);
	        return this;
	    }

	    public Response success(Object data) {
	        this.meta = new Meta(true, STATE);
	        this.data = data;
	        return this;
	    }

	    public Response failure() {
	        this.meta = new Meta(false, ERROR);
	        return this;
	    }

	    public Response failure(String message) {
	        this.meta = new Meta(false, message);
	        return this;
	    }

	    public Meta getMeta() {
	        return meta;
	    }

	    public Object getData() {
	        return data;
	    }

	    public class Meta {

	        private boolean success;
	        private String message;

	        public Meta(boolean success) {
	            this.success = success;
	        }

	        public Meta(boolean success, String message) {
	            this.success = success;
	            this.message = message;
	        }

	        public boolean isSuccess() {
	            return success;
	        }

	        public String getMessage() {
	            return message;
	        }
	    }

}
