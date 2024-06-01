package br.com.wmw.lavenderepda.business.domain.vo;

import br.com.wmw.lavenderepda.Messages;

public class TipoPendenciaPedido {

	public String cdTipoPendenciaPedido;
	public String dsTipoPendenciaPedido;
	
	public static final String CDTIPOPENDENCIA_LIMCREDEXTRAPOLADO = "1";
	public static final String CDTIPOPENDENCIA_CONDPAGTO_DIFERENTEPADRAO = "2";
	public static final String CDTIPOPENDENCIA_CONDPAGTO_QTDIASPAGTO_EXTRAPOLADO = "3";
	
	public TipoPendenciaPedido(String cdTipoPendenciaPedido) {
		this.cdTipoPendenciaPedido = cdTipoPendenciaPedido;
		this.dsTipoPendenciaPedido = getDsTipoPendenciaByCdTipoPendencia(cdTipoPendenciaPedido);
	}
	
	private String getDsTipoPendenciaByCdTipoPendencia(final String cdTipoPendencia) {
		switch (cdTipoPendencia) {
		case CDTIPOPENDENCIA_LIMCREDEXTRAPOLADO:
			return Messages.TIPOPENDENCIAPEDIDO_LIMITECREDITO;
		case CDTIPOPENDENCIA_CONDPAGTO_DIFERENTEPADRAO:
		case CDTIPOPENDENCIA_CONDPAGTO_QTDIASPAGTO_EXTRAPOLADO:
			return Messages.TIPOPENDENCIAPEDIDO_CONDICAOPAGAMENTO;
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.dsTipoPendenciaPedido;
	}
}
