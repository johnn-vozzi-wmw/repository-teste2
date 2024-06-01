package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;

public class RentabilidadeMenorMinimaException extends ValidationException {
	
	private static final long serialVersionUID = 1L;

	public RentabilidadeMenorMinimaException() {
		super(Messages.RENTABILIDADE_MENOR_MINIMO_PERMITIDO);
	}
	
}
