package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RestricaoVendaCliDbxDao;

public class RestricaoVendaCliService extends CrudService {

    private static RestricaoVendaCliService instance;

    private RestricaoVendaCliService() {
        //--
    }

    public static RestricaoVendaCliService getInstance() {
        if (instance == null) {
            instance = new RestricaoVendaCliService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return RestricaoVendaCliDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void validaBloqueiaVendaProdutoSelecionado(Produto produto, Cliente cliente) throws SQLException {
		if (ValueUtil.isNotEmpty(produto.cdRestricaoVendaProd) && ValueUtil.isNotEmpty(cliente.cdRestricaoVendaCli)) {
			RestricaoVendaCli restricaoVendaCli = getRestricaoVendaCli(produto, cliente);
			if (restricaoVendaCli != null) {
				if (restricaoVendaCli.isBloqueiaVenda()) {
					throw new ValidationException(Messages.PRODUTO_MSG_BLOQUEADO);
				}
			}
		}
	}

	private RestricaoVendaCli getRestricaoVendaCli(Produto produto, Cliente cliente) throws SQLException {
		RestricaoVendaCli restricaoVendaCliFilter = new RestricaoVendaCli();
		restricaoVendaCliFilter.cdEmpresa = cliente.cdEmpresa;
		restricaoVendaCliFilter.cdRepresentante = cliente.cdRepresentante;
		restricaoVendaCliFilter.cdRestricaoVendaCli = cliente.cdRestricaoVendaCli;
		restricaoVendaCliFilter.cdRestricaoVendaProd = produto.cdRestricaoVendaProd;
		RestricaoVendaCli restricaoVendaCli = (RestricaoVendaCli) findByRowKey(restricaoVendaCliFilter.getRowKey());
		return restricaoVendaCli;
	}

	public boolean isPermiteAlteracaoPreco(Produto produto, Cliente cliente) throws SQLException {
		if (ValueUtil.isNotEmpty(produto.cdRestricaoVendaProd) && ValueUtil.isNotEmpty(cliente.cdRestricaoVendaCli)) {
			RestricaoVendaCli restricaoVendaCli = getRestricaoVendaCli(produto, cliente);
			if (restricaoVendaCli != null) {
				if (restricaoVendaCli.isBloqueiaAlteracaoPreco()) {
					return false;
				}
			}
		}
		return true;
	}

}