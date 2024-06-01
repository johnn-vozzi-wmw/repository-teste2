package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class DescItemMaiorDescProgressivoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public DescItemMaiorDescProgressivoException(String message) {
		super(message);
	}

	public DescItemMaiorDescProgressivoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public DescItemMaiorDescProgressivoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return "DescItemMaiorDescProgressivoException";
	}
}
