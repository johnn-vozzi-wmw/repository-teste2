package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonifCfgFaixaQtdeDbxDao;

public class BonifCfgFaixaQtdeService extends CrudService {

	public static BonifCfgFaixaQtdeService instance;

	public static BonifCfgFaixaQtdeService getInstance() {
		if (instance == null) {
			instance = new BonifCfgFaixaQtdeService();
		}
		return instance;
	}

	public static void invalidateInstance() {
		instance = null;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		//nao cadastrado no app
	}

	@Override
	protected CrudDao getCrudDao() {
		return BonifCfgFaixaQtdeDbxDao.getInstance();
	}

	public BonifCfgFaixaQtde findBonifCfgFaixaQtdeAtingida(BonifCfg bonifCfg, double qtFaixaFilter) throws SQLException {
		BonifCfgFaixaQtde bonifCfgFaixaQtdeFilter = new BonifCfgFaixaQtde();
		bonifCfgFaixaQtdeFilter.cdEmpresa = bonifCfg.cdEmpresa;
		bonifCfgFaixaQtdeFilter.cdBonifCfg = bonifCfg.cdBonifCfg;
		bonifCfgFaixaQtdeFilter.qtFaixaFilter = qtFaixaFilter;
		bonifCfgFaixaQtdeFilter.sortAtributte = BonifCfgFaixaQtde.NMCOLUNA_QTVENDIDA;
		bonifCfgFaixaQtdeFilter.sortAsc = ValueUtil.VALOR_NAO;
		bonifCfgFaixaQtdeFilter.limit = 1;
		return BonifCfgFaixaQtdeDbxDao.getInstance().findBonifFaixaQtdeAtingida(bonifCfgFaixaQtdeFilter);
	}

	public double getQtBonificacaoAtingida(BonifCfgFaixaQtde bonifCfgFaixaQtdeAtingida, double qtItemFisico, double qtBonificacaoAtual, boolean isFirstItemPedidoFromList, boolean isBonificacaoAutomatica) {
		if (bonifCfgFaixaQtdeAtingida == null) {
			return 0d;
		}

		double qtBonificacaoAtingida = 0;
		if (bonifCfgFaixaQtdeAtingida.isConcedeMultiplos()) {
			int multiplo = (int) (qtItemFisico / bonifCfgFaixaQtdeAtingida.qtVendida);
			qtBonificacaoAtingida = multiplo * bonifCfgFaixaQtdeAtingida.qtBonificada;
		} else {
			qtBonificacaoAtingida = bonifCfgFaixaQtdeAtingida.qtBonificada;
		}
		if (qtBonificacaoAtingida == qtBonificacaoAtual && !isFirstItemPedidoFromList && !isBonificacaoAutomatica) {
			return 0;
		}
		return LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValueTruncated(qtBonificacaoAtingida) : qtBonificacaoAtingida;
	}
	
	public Map<String, BonifCfgFaixaQtde> findBonifCfgFaixaByPedido(Pedido pedidoFilter) throws SQLException {
		return ((BonifCfgFaixaQtdeDbxDao)getCrudDao()).findBonifCfgFaixaByPedido(pedidoFilter);
	}

	public void avisaTrocaBonifCfgFaixaQtde(Pedido pedido) throws SQLException {
		if (pedido.bonifCfgFaixaQtdeAtualMap == null || pedido.bonifCfgFaixaQtdeOldMap == null || pedido.isReplicandoPedido) {
			return;
		}
		String bonifCfgAlterado = getMessageTrocaBonifCfgFaixaQtde(pedido.bonifCfgFaixaQtdeAtualMap, pedido.bonifCfgFaixaQtdeOldMap, true);
		if (bonifCfgAlterado != null) {
			UiUtil.showInfoMessage(bonifCfgAlterado);
		} else {
			bonifCfgAlterado = getMessageTrocaBonifCfgFaixaQtde(pedido.bonifCfgFaixaQtdeOldMap, pedido.bonifCfgFaixaQtdeAtualMap, false);
			if (bonifCfgAlterado != null) {
				UiUtil.showInfoMessage(bonifCfgAlterado);
			}
		}
	}

	private String getMessageTrocaBonifCfgFaixaQtde(Map<String, BonifCfgFaixaQtde> bonifCfgFaixaQtdeListPercorrida, Map<String, BonifCfgFaixaQtde> bonifCfgFaixaQtdeListComparada, boolean checkCredito) throws SQLException {
		for (BonifCfgFaixaQtde bonifCfgFaixaQtdePercorrida : bonifCfgFaixaQtdeListPercorrida.values()) {
			BonifCfgFaixaQtde bonifCfgFaixaQtdeComparada = bonifCfgFaixaQtdeListComparada.get(bonifCfgFaixaQtdePercorrida.cdBonifCfg);
			String msg = null;
			if (houveMudancaFaixa(bonifCfgFaixaQtdePercorrida, bonifCfgFaixaQtdeComparada)) {
				BonifCfg bonifCfg = BonifCfgService.getInstance().getBonifCfgByFaixaQtde(bonifCfgFaixaQtdePercorrida);
				if (bonifCfg != null) {
					msg = checkCredito ? Messages.MSG_TROCA_FAIXA_QTDE_POLITICA_BONIFICACAO : Messages.MSG_PERDA_FAIXA_QTDE_POLITICA_BONIFICACAO;
					return MessageUtil.getMessage(msg, bonifCfg.dsBonifCfg);
				}
			} else if (houveMudancaMultiploMesmaFaixa(bonifCfgFaixaQtdePercorrida, bonifCfgFaixaQtdeComparada)) {
				BonifCfg bonifCfg = BonifCfgService.getInstance().getBonifCfgByFaixaQtde(bonifCfgFaixaQtdePercorrida);
				if (bonifCfg != null) {
					if (checkCredito && bonifCfgFaixaQtdePercorrida.qtBonificacaoAtual > bonifCfgFaixaQtdeComparada.qtBonificacaoAtual) {
						msg = Messages.MSG_TROCA_MULTIPLO_FAIXA_QTDE_POLITICA_BONIFICACAO;
					} else if (bonifCfgFaixaQtdePercorrida.qtBonificacaoAtual < bonifCfgFaixaQtdeComparada.qtBonificacaoAtual) {
						msg = Messages.MSG_PERDA_MULTIPLO_FAIXA_QTDE_POLITICA_BONIFICACAO;
					}
					return MessageUtil.getMessage(msg, bonifCfg.dsBonifCfg);
				}
			}
		}
		return null;
	}

	public void avisaFaixaBonifCfgFaixaQtdeDeleted(Pedido pedido) throws SQLException {
		if (pedido.bonifCfgFaixaQtdeDeletedMap != null) {
			for (BonifCfgFaixaQtde bonifCfgFaixaQtdeDeleted : pedido.bonifCfgFaixaQtdeDeletedMap.values()) {
				BonifCfg bonifCfg = BonifCfgService.getInstance().getBonifCfgByFaixaQtde(bonifCfgFaixaQtdeDeleted);
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_PERDA_FAIXA_QTDE_POLITICA_BONIFICACAO, bonifCfg.dsBonifCfg));
			}
			pedido.bonifCfgFaixaQtdeDeletedMap = null;
		}
	}

	private boolean houveMudancaFaixa(BonifCfgFaixaQtde bonifCfgFaixaQtdePercorrida, BonifCfgFaixaQtde bonifCfgFaixaQtdeComparada) {
		return !ValueUtil.valueEquals(bonifCfgFaixaQtdePercorrida, bonifCfgFaixaQtdeComparada);
	}

	private boolean houveMudancaMultiploMesmaFaixa(BonifCfgFaixaQtde bonifCfgFaixaQtdePercorrida, BonifCfgFaixaQtde bonifCfgFaixaQtdeComparada) {
		return bonifCfgFaixaQtdeComparada != null && bonifCfgFaixaQtdePercorrida.qtBonificacaoAtual != bonifCfgFaixaQtdeComparada.qtBonificacaoAtual;
	}

}
