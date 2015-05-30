package suncertify.ui;

public class UIControllerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UIControllerException() {}
	
	public UIControllerException(String message) {
		super(message);
	}

	public UIControllerException(Throwable cause) {
		super(cause);
	}
}
