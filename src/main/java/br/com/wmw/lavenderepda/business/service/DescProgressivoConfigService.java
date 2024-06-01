package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgressivoConfigDbxDao;
import totalcross.util.Vector;

public class DescProgressivoConfigService extends CrudService {

    private static DescProgressivoConfigService instance;

    private DescProgressivoConfigService() {}
	public static DescProgressivoConfigService getInstance() { return instance == null ? instance = new DescProgressivoConfigService() : instance; }

	@Override protected CrudDao getCrudDao() { return DescProgressivoConfigDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

	public int countByCliente(DescProgressivoConfig descProgressivoConfig) throws SQLException {
    	return DescProgressivoConfigDbxDao.getInstance().countByCliente(descProgressivoConfig);
	}

	public DescProgressivoConfig findByCdDescProgressivo(String cdDescProgressivo) throws SQLException {
		if (ValueUtil.isNotEmpty(cdDescProgressivo)) {
			DescProgressivoConfig config = new DescProgressivoConfig();
			config.cdDescProgressivo = cdDescProgressivo;
			config.cdEmpresa = SessionLavenderePda.cdEmpresa;
			config.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			
			Vector list = findAllByExample(config);
			if (ValueUtil.isNotEmpty(list)) {
				return (DescProgressivoConfig)list.items[0];
			}
		}
		return null;
	}
	
	public DescProgressivoConfig findVlMinimoAndQtdFamiliaByDescProgressivo(DescProgressivoConfig descProgressivoConfig) throws SQLException {
		DescProgressivoConfig descprog = DescProgressivoConfigDbxDao.getInstance().findVlAtingidoAndQtdFamiliaByDescProgressivo(descProgressivoConfig, false);
		return DescProgressivoConfigDbxDao.getInstance().findVlAtingidoAndQtdFamiliaByDescProgressivo(descprog, true);
	}

	public DescProgConfigFaDes getDescProgConfigFaDesItem(ItemPedido itemPedido) throws SQLException {
		return itemPedido.auxiliarVariaveis.recalculandoDescProgEdit ? itemPedido.auxiliarVariaveis.descProgRecalculo : DescProgConfigFaDesService.getInstance().findFaixaDescProgByProdutoCliente(itemPedido, false);
	}

	public void processaDescontoProgressivo(ItemPedido itemPedido, DescProgConfigFaDes descProgConfigFaDes) throws SQLException {
		if (descProgConfigFaDes == null) {
			if (ValueUtil.isNotEmpty(itemPedido.cdDescProgressivo)) {
				itemPedido.oldQtItemFisicoDescQtd = -1;
				applyInItem(itemPedido, null, 0);
			}
			itemPedido.auxiliarVariaveis.recalculandoDescProgEdit = false;
			return;
		}
		applyInItem(itemPedido, descProgConfigFaDes.cdDescProgressivo, descProgConfigFaDes.vlPctDescProg);
	}

	private void applyInItem(ItemPedido itemPedido, final String cdDescProgressivo, final double vlPctDescProg) throws SQLException {
		if (itemPedido.hasDescProgressivo() && vlPctDescProg == 0) {
			itemPedido.auxiliarVariaveis.changedDescProgressivo = true;
		}
		itemPedido.cdDescProgressivo = cdDescProgressivo;
		itemPedido.vlPctDescProg = vlPctDescProg;
		itemPedido.vlPctDesconto = vlPctDescProg;
		if (itemPedido.vlPctDesconto > 0d) {
			itemPedido.vlPctAcrescimo = 0d;
		}
		if (itemPedido.auxiliarVariaveis.recalculandoDescProgEdit) {
			ItemPedidoService.getInstance().aplicaDescontoItemPedido(itemPedido);
			itemPedido.auxiliarVariaveis.recalculandoDescProgEdit = false;
		}
	}

	public int countQtExtraByPedido(DescProgressivoConfig descProgressivoConfig, Pedido pedido, boolean consideraOutrosPedidos) throws SQLException {
    	return DescProgressivoConfigDbxDao.getInstance().countQtExtraByPedido(descProgressivoConfig, pedido, consideraOutrosPedidos);
	}

}
