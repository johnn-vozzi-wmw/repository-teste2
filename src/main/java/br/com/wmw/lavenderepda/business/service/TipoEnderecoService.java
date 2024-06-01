package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.TipoEndereco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoEnderecoDbxDao;

public class TipoEnderecoService extends CrudService {

    private static TipoEnderecoService instance = null;
    
    private TipoEnderecoService() {
        //--
    }
    
    public static TipoEnderecoService getInstance() {
        if (instance == null) {
            instance = new TipoEnderecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoEnderecoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public String getTipoEnderecoCliente(String cdTipoEndereco) throws SQLException {
		if (cdTipoEndereco == null) {
			return ValueUtil.VALOR_NI;
		}
		TipoEndereco tipoEnderecoFilter = new TipoEndereco();
		tipoEnderecoFilter.cdTipoEndereco = cdTipoEndereco;
		tipoEnderecoFilter = (TipoEndereco) findByPrimaryKey(tipoEnderecoFilter);
		return tipoEnderecoFilter != null && tipoEnderecoFilter.cdTipoEndereco != null ? tipoEnderecoFilter.toString() : "";
	}

	public String findHoraLimiteEnvioPedidoPor(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEndereco) throws SQLException {
		TipoEndereco tipoEndereco = getTipoEndereco(cdEmpresa, cdRepresentante, cdCliente, cdEndereco);
		return tipoEndereco != null ? tipoEndereco.hrLimiteEnvioPedido : null;
	}

	public Integer findNumeroMinimoDiasPedidoPor(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEndereco) throws SQLException {
		TipoEndereco tipoEndereco = getTipoEndereco(cdEmpresa, cdRepresentante, cdCliente, cdEndereco);
		return tipoEndereco != null ? tipoEndereco.nuMinDiasEntregaPedido : null;
	}
	
	private TipoEndereco getTipoEndereco(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEndereco) throws SQLException {
		if (ValueUtil.isEmpty(cdEmpresa) || ValueUtil.isEmpty(cdRepresentante) || ValueUtil.isEmpty(cdCliente) || ValueUtil.isEmpty(cdEndereco)) return null;
		ClienteEndereco endereco = new ClienteEndereco();
		endereco.cdEmpresa = cdEmpresa;
		endereco.cdRepresentante = cdRepresentante;
		endereco.cdCliente = cdCliente;
		endereco.cdEndereco = cdEndereco;
		endereco = (ClienteEndereco) ClienteEnderecoService.getInstance().findByPrimaryKey(endereco);
		if (endereco != null && ValueUtil.isNotEmpty(endereco.cdTipoEndereco)) {
			TipoEndereco tipoEndereco = new TipoEndereco();
			tipoEndereco.cdEmpresa = endereco.cdEmpresa;
			tipoEndereco.cdTipoEndereco = endereco.cdTipoEndereco;
			return (TipoEndereco) findByPrimaryKey(tipoEndereco);
		}
		return null;
	}
	
}