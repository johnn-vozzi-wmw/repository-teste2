package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CondTipoPagto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondTipoPagtoPdbxDao;

public class CondTipoPagtoService extends CrudService {

    private static CondTipoPagtoService instance;

    private CondTipoPagtoService() {
        //--
    }

    public static CondTipoPagtoService getInstance() {
        if (instance == null) {
            instance = new CondTipoPagtoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondTipoPagtoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public double getVlIndice(ItemPedido itemPedido) throws SQLException {
		CondTipoPagto filter = new CondTipoPagto();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
		filter.cdCondicaoPagamento = itemPedido.pedido.cdCondicaoPagamento;
		filter.cdTipoPagamento = itemPedido.pedido.cdTipoPagamento;
		double vlIndice = ValueUtil.getDoubleValue(findColumnByRowKey(filter.getRowKey(), "VLINDICE"));
		return vlIndice != 0 ? vlIndice : 1;
	}
	
	public double getVlIndice(Pedido pedido) throws SQLException {
		CondTipoPagto filter = new CondTipoPagto();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
		filter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		filter.cdTipoPagamento = pedido.cdTipoPagamento;
		double vlIndice = ValueUtil.getDoubleValue(findColumnByRowKey(filter.getRowKey(), "VLINDICE"));
		return vlIndice != 0 ? vlIndice : 1;
	}

	public double getQtMinValorParcelaCondPagtoTipoPagto(String cdCondicaoPagamento, String cdTipoPagamento) throws SQLException {
		if (cdCondicaoPagamento == null || cdTipoPagamento == null) return 0;
		CondTipoPagto condTipoPagtoFilter = new CondTipoPagto();
		condTipoPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		condTipoPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondTipoPagto.class);
		condTipoPagtoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
		condTipoPagtoFilter.cdTipoPagamento = cdTipoPagamento;
		CondTipoPagto condTipoPagtoTabela = (CondTipoPagto) CondTipoPagtoService.getInstance().findByPrimaryKey(condTipoPagtoFilter);
		return condTipoPagtoTabela != null && condTipoPagtoTabela.qtMinValorParcela > 0 ? condTipoPagtoTabela.qtMinValorParcela : 0;
	}
}