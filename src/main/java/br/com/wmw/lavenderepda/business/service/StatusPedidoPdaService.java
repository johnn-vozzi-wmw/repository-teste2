package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusPedidoPdaPdbxDao;
import totalcross.util.Vector;

public class StatusPedidoPdaService extends CrudService {

    private StatusPedidoPdaService() {
    }

    public static StatusPedidoPdaService getInstance() {
		return new StatusPedidoPdaService();
    }

    //@Override
    protected CrudDao getCrudDao() {
        return StatusPedidoPdaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDsStatusPedido(String cdStatusPedido) throws SQLException {
		StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
		statusPedidoFilter.cdStatusPedido = cdStatusPedido;
		String dsStatusPedido = findColumnByRowKey(statusPedidoFilter.getRowKey(), "DSSTATUSPEDIDO");
		if (dsStatusPedido != null) {
			return StringUtil.getStringValue(dsStatusPedido);
		} else {
			return StringUtil.getStringValue(cdStatusPedido);
		}
    }
    
    public String getDsStatusPedidoAberto() throws SQLException {
    	return getDsStatusPedido(LavenderePdaConfig.cdStatusPedidoAberto);
    }

    public boolean isStatusPedidoPdaComplementavel(String cdStatusPedido) throws SQLException {
    	StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
		statusPedidoFilter.cdStatusPedido = cdStatusPedido;
		String flComplementavel = findColumnByRowKey(statusPedidoFilter.getRowKey(), "FLCOMPLEMENTAVEL");
		return ValueUtil.VALOR_SIM.equals(flComplementavel);
    }
    
    public boolean isPossuiStatusConsignacao() throws SQLException {
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
			statusPedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoConsignado;
			return ValueUtil.isNotEmpty(findColumnByRowKey(statusPedidoFilter.getRowKey(), "DSSTATUSPEDIDO"));
		}
		return false;
	}

	public boolean isFlLiberaEstoque(String cdStatusPedido) throws SQLException {
		StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
		statusPedidoFilter.cdStatusPedido = cdStatusPedido;
		String flLiberaEstoque = findColumnByRowKey(statusPedidoFilter.getRowKey(), "FLLIBERAESTOQUE");
		
		return !"S".equalsIgnoreCase(flLiberaEstoque);
	}

	public Vector listIdStatusRelacionaPedidoPda() throws SQLException {
		return StatusPedidoPdaPdbxDao.getInstance().findIdStatusRelacionaPedidoPda();
	}
	
	public String isConverteTipoPedidoReplicacao(String cdStatusPedido) throws SQLException {
		StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
		statusPedidoFilter.cdStatusPedido = cdStatusPedido;
		String flConverteTipoPedidoReplicacao = findColumnByRowKey(statusPedidoFilter.getRowKey(), "FLCONVERTETIPOPEDIDOREPLICACAO");
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, StringUtil.getStringValue(flConverteTipoPedidoReplicacao))?ValueUtil.VALOR_SIM:ValueUtil.VALOR_NAO;
    }
	
	public boolean isOcultaValoresComissao(String cdStatusPedido) throws SQLException {
		StatusPedidoPda statusPedidoFilter = new StatusPedidoPda();
		statusPedidoFilter.cdStatusPedido = cdStatusPedido;
		return ValueUtil.getBooleanValue(findColumnByRowKey(statusPedidoFilter.getRowKey(), "FLOCULTAVALORESCOMISSAO"));
	}
}