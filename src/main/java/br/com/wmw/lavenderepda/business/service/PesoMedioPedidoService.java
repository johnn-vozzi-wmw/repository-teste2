package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PerguntaPdbxDao;

public class PesoMedioPedidoService extends CrudPersonLavendereService {

	private static PesoMedioPedidoService instance;

	public static PesoMedioPedidoService getInstance() {
		return instance == null ? instance = new PesoMedioPedidoService() : instance;
	}

	@Override protected CrudDao getCrudDao() {
		return PerguntaPdbxDao.getInstance();
	}

	public double calculatePesoMedioPedido(double vlPesoAtual, double vlTotalPedido) {
		
		if (vlTotalPedido == 0 || vlPesoAtual == 0) return 0;
		return ValueUtil.round(vlTotalPedido / vlPesoAtual);
	}

}