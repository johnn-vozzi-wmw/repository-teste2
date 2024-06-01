package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Vector;

public class KitTipo3VigenciaException extends ValidationException {

	private static final long serialVersionUID = 1L;
	public final Vector kitsExtrapolados;

	public KitTipo3VigenciaException(Vector kitsExtrapolados) {
		super(Messages.KIT_TIPO_3_VIGENCIA_EXTRAPOLADA_MSG);
		this.kitsExtrapolados = kitsExtrapolados;
	}

}
