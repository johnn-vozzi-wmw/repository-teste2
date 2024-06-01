package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteInativacao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteInativacaoDbxDao;
import totalcross.util.pdf.Base;

import java.sql.SQLException;

public class ClienteInativacaoService extends CrudService {

    private static ClienteInativacaoService instance;

    private ClienteInativacaoService() { }
    
    public static ClienteInativacaoService getInstance() {
        if (instance == null) {
            instance = new ClienteInativacaoService();
        }
        return instance;
    }

    public void insertOrUpdate(BaseDomain domain) throws SQLException {
        if (findByPrimaryKey(domain) == null) {
            super.insert(domain);
        } else {
            update(domain);
        }
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteInativacaoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws SQLException { }

    @Override
    public void delete(BaseDomain domain) throws SQLException {
        ClienteInativacao clienteInativacao = (ClienteInativacao) domain;
        clienteInativacao.flCancelado = ValueUtil.VALOR_SIM;
        clienteInativacao.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
        getCrudDao().update(domain);
    }

    public void deleteCancelados() throws SQLException {
        getCrudDao().deleteAllByExample(new ClienteInativacao(true));
    }
}