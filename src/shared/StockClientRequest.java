package shared;

import java.io.Serializable;

public class StockClientRequest<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected final RequestType	requestType;
	protected final T			requestData;
	
	public StockClientRequest (RequestType requestType, T requestData) {
		this.requestType = requestType;
		this.requestData = requestData;
	}
	
	public RequestType getRequestType() {
		return requestType;
	}

	public T getRequestData() {
		return requestData;
	}
}
