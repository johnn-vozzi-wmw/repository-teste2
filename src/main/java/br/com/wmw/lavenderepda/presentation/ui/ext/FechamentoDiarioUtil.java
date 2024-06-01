package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.util.Date;

public class FechamentoDiarioUtil {
	
	public static boolean isBloqueiaPorFechamentoDiario() throws SQLException {
		if (!LavenderePdaConfig.usaConfigFechamentoDiarioVendas()) return false;
		if (FechamentoDiarioService.getInstance().isHouveFechamentoDiario(new Date())) {
			UiUtil.showWarnMessage(Messages.FECHAMENTO_DIARIO_MSG_FECHAMENTO_DIARIO_CONCLUIDO);
			return true;
		}
		return isNaoHouveFechamentoDiarioAnterior();
	}
	

	private static boolean isNaoHouveFechamentoDiarioAnterior() throws SQLException {
		int qtdPagamentos = PagamentoService.getInstance().countPagamento();
		int qtdPedidos = PedidoService.getInstance().countPedidosDiferenteAbertoECancelado(null);
		if (qtdPedidos > 0 || qtdPagamentos > 0) {
			Date dateFilter = new Date();
			dateFilter.advance(-1);
			boolean houveFechamentoDiario = FechamentoDiarioService.getInstance().isHouveFechamentoDiario(dateFilter);
			if (!houveFechamentoDiario) {
				boolean houveFechamentoTodosPedidosDiasAnteriores = verificaPedidosFechadosTransmitidosSemFechamentoDiario(qtdPedidos);
				boolean houveFechamentoTodosPagamentosDiasAnteriores = verificaPagamentosRealizadosSemFechamentoDiario(qtdPagamentos);
				if (!houveFechamentoTodosPedidosDiasAnteriores || !houveFechamentoTodosPagamentosDiasAnteriores) {
					UiUtil.showWarnMessage(Messages.FECHAMENTO_DIARIO_MSG_FECHAMENTO_DIARIO_ANTERIOR_CONCLUIDO);
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean verificaPagamentosRealizadosSemFechamentoDiario(int qtdPagamentos) throws SQLException {
		if (qtdPagamentos > 0) {
			return isHouveFechamentoDiarioPagamento();
		}
		return true;
	}

	private static boolean verificaPedidosFechadosTransmitidosSemFechamentoDiario(int qtdPedidos) throws SQLException {
		if (qtdPedidos > 0) {
			return isHouveFechamentoDiarioPedido();
		}
		return true;
	}
	
    private static boolean isHouveFechamentoDiarioPedido() throws SQLException {
    	Date dateFilter = new Date();
    	Date dataUltimoFechamentoDiario = FechamentoDiarioService.getInstance().getDataUltimoFechamentoDiario(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(FechamentoDiario.class), null);
    	if (ValueUtil.isEmpty(dataUltimoFechamentoDiario)) return false;
    	while (dateFilter.isAfter(dataUltimoFechamentoDiario)) {
    		DateUtil.decDay(dateFilter, 1);
    		if (PedidoService.getInstance().isExistePedidoDiferenteAbertoECancelado(dateFilter)) { 
    			return FechamentoDiarioService.getInstance().isHouveFechamentoDiario(dateFilter);
    		}
		}
    	return true; 
    }
    
    private static boolean isHouveFechamentoDiarioPagamento() throws SQLException {
    	Date dateFilter = new Date();
    	Date dataUltimoFechamentoDiario = FechamentoDiarioService.getInstance().getDataUltimoFechamentoDiario(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(FechamentoDiario.class), null);
    	if (ValueUtil.isEmpty(dataUltimoFechamentoDiario)) return false;
    	while (dateFilter.isAfter(dataUltimoFechamentoDiario)) {
    		DateUtil.decDay(dateFilter, 1);
    		if (PagamentoService.getInstance().isExistePagamento(dateFilter)) {
    			return FechamentoDiarioService.getInstance().isHouveFechamentoDiario(dateFilter);
    		}
    	}
    	return true;
    }

}
