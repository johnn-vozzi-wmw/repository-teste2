package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;

public class DescProgressivoPersonalizadoVigenciaException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public DescProgressivoPersonalizadoVigenciaException() {
		super(Messages.DESC_PROG_CONFIG_VIGENCIA_EXTRAPOLADA_ERROR);
	}
}
