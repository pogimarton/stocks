package shared;

import java.io.Serializable;

public class StockServerResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected final RequestType responseTo;
	protected final T			responseData;

	public StockServerResponse (RequestType responseTo, T responseData) {
		this.responseTo = responseTo;
		this.responseData = responseData;
	}

	public RequestType getResponseTo() {
		return responseTo;
	}

	public T getResponseData() {
		return responseData;
	}
}
