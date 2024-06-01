package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.EnderecoGpsPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EnderecoGpsPdaDbxDao;

public class EnderecoGpsPdaService extends CrudService {

    private static EnderecoGpsPdaService instance;

    private EnderecoGpsPdaService() {
        //--
    }

    public static EnderecoGpsPdaService getInstance() {
        if (instance == null) {
            instance = new EnderecoGpsPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EnderecoGpsPdaDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }

    public void insertOrUpdate(BaseDomain domain) throws SQLException {
    	try {
    		super.insert(domain);
		} catch (Throwable e) {
			update(domain);
		}
    }

	public void deleteAllEnviadosServidor() throws SQLException {
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente()) {
			EnderecoGpsPda enderecoGpsPda = new EnderecoGpsPda();
			enderecoGpsPda.flTipoAlteracao = EnderecoGpsPda.FLTIPOALTERACAO_ORIGINAL;
			EnderecoGpsPdaDbxDao.getInstance().deleteAllByExample(enderecoGpsPda);
		}
	}

	public boolean isCadastraCoordenada(Cliente cliente) throws SQLException {
		if (cliente.cdLatitude == 0 && cliente.cdLongitude == 0) {
			EnderecoGpsPda enderecoGpsPdaFilter = new EnderecoGpsPda();
			enderecoGpsPdaFilter.cdCliente = cliente.cdCliente;
			return EnderecoGpsPdaDbxDao.getInstance().countByExample(enderecoGpsPdaFilter) == 0;
		}
		return false;
	}
}