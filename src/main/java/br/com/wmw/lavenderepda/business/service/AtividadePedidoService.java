package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.StatusAtividade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtividadePedidoDao;
import totalcross.util.Vector;

public class AtividadePedidoService extends CrudPersonLavendereService {

    private static AtividadePedidoService instance = null;
    
    private AtividadePedidoService() {
        //--
    }
    
    public static AtividadePedidoService getInstance() {
        if (instance == null) {
            instance = new AtividadePedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AtividadePedidoDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) {}
	
	@Override
	public BaseDomain findByRowKeyDyn(String rowKey) throws SQLException {
		AtividadePedido atividadePedido = (AtividadePedido) super.findByRowKeyDyn(rowKey);
		Vector atividadePedidoList = new Vector();
		atividadePedidoList.addElement(atividadePedido);
		populaStatusAtividadePedido(atividadePedidoList);
		return atividadePedido;
	}
	
	public Vector getAtividadePedidoList(Pedido pedido) throws SQLException {
		if (pedido != null) {
    		AtividadePedido atividadePedidoFilter = new AtividadePedido();
    		atividadePedidoFilter.cdEmpresa = pedido.cdEmpresa;
    		atividadePedidoFilter.cdRepresentante = pedido.cdRepresentante;
    		atividadePedidoFilter.flOrigemPedido = ValueUtil.isNotEmpty(pedido.flOrigemPedidoRelacionado) ? pedido.flOrigemPedidoRelacionado : pedido.flOrigemPedido;
    		atividadePedidoFilter.nuPedido = ValueUtil.isNotEmpty(pedido.nuPedidoRelacionado) ? pedido.nuPedidoRelacionado : pedido.nuPedido;
    		atividadePedidoFilter.sortAtributte = AtividadePedido.NMCOLUNA_NUSEQUENCIA;
    		atividadePedidoFilter.sortAsc = ValueUtil.VALOR_SIM;
    		return populaStatusAtividadePedido(findAllByExampleDyn(atividadePedidoFilter));
    	}
    	return null;
    }

	private Vector populaStatusAtividadePedido(Vector atividadePedidoList) throws SQLException {
		for (int i = 0; i < atividadePedidoList.size(); i++) {
			AtividadePedido atividadePedido = (AtividadePedido) atividadePedidoList.items[i];
			StatusAtividade statusAtividade = (StatusAtividade) StatusAtividadeService.getInstance().findByRowKey(atividadePedido.cdStatusAtividade + ";");
			atividadePedido.statusAtividade = statusAtividade != null ? statusAtividade : new StatusAtividade(atividadePedido.cdStatusAtividade, "");
		}
		return atividadePedidoList;
	}

	public void deletaAtividadePedidoAntigos() throws SQLException {
		AtividadePedido atividadePedidoFilter = new AtividadePedido();
		atividadePedidoFilter.dtAlteracao = DateUtil.getCurrentDate();
		DateUtil.decDay(atividadePedidoFilter.dtAlteracao, LavenderePdaConfig.getNuDiasPermanenciaAtividadePedido());
		deleteAllByExample(atividadePedidoFilter);
	}
 
}