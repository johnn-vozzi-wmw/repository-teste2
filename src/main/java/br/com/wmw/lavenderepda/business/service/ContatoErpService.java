package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContatoErpPdbxDao;
import totalcross.util.Vector;

public class ContatoErpService extends CrudPersonLavendereService {

    private static ContatoErpService instance;

    private ContatoErpService() {
        //--
    }

    public static ContatoErpService getInstance() {
        if (instance == null) {
            instance = new ContatoErpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContatoErpPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    //@Override
    public void update(BaseDomain domain) throws SQLException {
    	ContatoErp contatoErp = (ContatoErp)domain;
    	//--
    	ContatoPda contato = new ContatoPda();
    	contato.cdCliente = contatoErp.cdCliente;
    	contato.cdContato = contatoErp.cdContato;
    	contato.cdEmpresa = contatoErp.cdEmpresa;
    	contato.cdRegistro = ContatoPda.CD_REGISTRO_DEFAULT;
    	contato.cdRepresentante = contatoErp.cdRepresentante;
    	contato.setHashValuesDinamicos(contatoErp.getHashValuesDinamicos());
    	contato.flOrigemContato = OrigemPedido.FLORIGEMPEDIDO_ERP;
    	contato.flAcaoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
    	ContatoPdaService.getInstance().insert(contato);
    	//--
    	getCrudDao().delete(contatoErp);
    }

  //@Override
    public void delete(BaseDomain domain) throws java.sql.SQLException {
    	ContatoErp contatoErp = (ContatoErp)domain;
    	//--
    	ContatoPda contato = new ContatoPda();
    	contato.cdCliente = contatoErp.cdCliente;
    	contato.cdContato = contatoErp.cdContato;
    	contato.cdEmpresa = contatoErp.cdEmpresa;
    	contato.cdRegistro = ContatoPda.CD_REGISTRO_DEFAULT;
    	contato.cdRepresentante = contatoErp.cdRepresentante;
    	contato.flOrigemContato = OrigemPedido.FLORIGEMPEDIDO_ERP;
    	contato.setHashValuesDinamicos(contatoErp.getHashValuesDinamicos());
    	contato.flAcaoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
    	ContatoPdaService.getInstance().insert(contato);
    	//--
    	getCrudDao().delete(contatoErp);
    }
    
    public Vector findAllContatosGroupByRepresentante(BaseDomain domain) throws SQLException {
    	return ContatoErpPdbxDao.getInstance().findAllGroupByRepresentante(domain);
    }
    
    public ContatoErp findContatoDefault(BaseDomain domain) throws SQLException {
    	Vector result = findAllByExample(domain);
    	if (ValueUtil.isNotEmpty(result)) {
    		return (ContatoErp) result.items[0];
    	}
    	return null;
    }
    
}