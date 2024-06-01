package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContatoPdaPdbxDao;
import totalcross.util.Vector;

public class ContatoPdaService extends CrudPersonLavendereService {

    private static ContatoPdaService instance;

    private ContatoPdaService() {
        //--
    }

    public static ContatoPdaService getInstance() {
        if (instance == null) {
            instance = new ContatoPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContatoPdaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	super.validate(domain);
    }
    
    public Vector findAllContatosGroupByRepresentante(BaseDomain domain) throws SQLException {
    	return ContatoPdaPdbxDao.getInstance().findAllGroupByRepresentante(domain);
    }

	public void deleteContatosNovoCliente(NovoCliente novoCliente) throws SQLException {
		ContatoPda filter = new ContatoPda();
		filter.cdEmpresa = novoCliente.cdEmpresa;
		filter.cdRepresentante = novoCliente.cdRepresentante;
		filter.cdCliente = novoCliente.nuCnpj;
		deleteAllByExample(filter);
	}
}