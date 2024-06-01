package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.BaseService;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.vo.TipoPendenciaPedido;
import totalcross.util.Vector;

public class TipoPendenciaPedidoService extends BaseService {
	
	private static TipoPendenciaPedidoService instance;
	
	public static TipoPendenciaPedidoService getInstance() {
		if (instance == null) {
			instance = new TipoPendenciaPedidoService();
		}
		return instance;
	}
	
	public Vector findAllAvailable() {
		TipoPendenciaPedido tipoPendencia;
		Vector list = new Vector(0);
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
			tipoPendencia = new TipoPendenciaPedido(TipoPendenciaPedido.CDTIPOPENDENCIA_LIMCREDEXTRAPOLADO); 
			list.addElement(tipoPendencia);
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			tipoPendencia = new TipoPendenciaPedido(LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagtoDiferentePadrao() 
					? TipoPendenciaPedido.CDTIPOPENDENCIA_CONDPAGTO_DIFERENTEPADRAO : TipoPendenciaPedido.CDTIPOPENDENCIA_CONDPAGTO_QTDIASPAGTO_EXTRAPOLADO); 
			list.addElement(tipoPendencia);
		}
		return list;
	}
}
