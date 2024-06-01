package br.com.wmw.lavenderepda.business.validation;

public class RelProdutosGradesInconsistentesException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public Object[] params = null;
	
	public RelProdutosGradesInconsistentesException(String message) {
		super(message);
	}
	
	public RelProdutosGradesInconsistentesException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}
	
	public RelProdutosGradesInconsistentesException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}
	
	public String toString() {
		return "RelProdutosGradesInconsistentesException";
	}
	
}