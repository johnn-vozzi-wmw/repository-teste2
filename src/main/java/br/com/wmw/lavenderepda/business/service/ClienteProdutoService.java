package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteProdutoDbxDao;
import totalcross.util.Vector;

public class ClienteProdutoService extends CrudService {

    private static ClienteProdutoService instance;

    public static ClienteProdutoService getInstance() {
        if (instance == null) {
            instance = new ClienteProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ClienteProdutoDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException { /**/ }
    
    protected Vector findClienteProdutoList(String cdCliente, String flTipoRelacao) throws SQLException {
    	Vector clienteProdutoList = new Vector();
    	ClienteProduto clienteProdutoFilter = new ClienteProduto();
	    clienteProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    clienteProdutoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
	    clienteProdutoFilter.cdCliente = cdCliente;
	    clienteProdutoFilter.flTipoRelacao = flTipoRelacao;
    	clienteProdutoList = findAllByExample(clienteProdutoFilter);
    	if (ClienteProduto.RELACAOEXCECAO.equals(flTipoRelacao)) {
    		//Necessário verificar se existe outros produtos restrito a outros clientes
    		clienteProdutoFilter.cdCliente = null;
    		clienteProdutoFilter.flTipoRelacao = ClienteProduto.RELACAORESTRICAO;
    		Vector clienteProdutoOutrosClientesList = findAllByExample(clienteProdutoFilter);
    		for (int i = 0; i < clienteProdutoOutrosClientesList.size(); i++) {
    			ClienteProduto clienteProduto = (ClienteProduto) clienteProdutoOutrosClientesList.items[i];
				clienteProdutoList.addElement(clienteProduto);
			}    		
    	}
    	return clienteProdutoList;
    }
    
    public boolean isPossuiRegistroExclusivo() throws SQLException {
    	ClienteProduto filter = new ClienteProduto();
    	filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteProduto.class);
    	filter.flTipoRelacao = ClienteProduto.RELACAOEXCLUSIVA;
    	filter.validandoCount = true;
    	return countByExample(filter) > 0;
    }
    
    public ClienteProduto getClienteProdutoFilter(String cdCliente) {
    	ClienteProduto clienteProdutoFilter = new ClienteProduto();
		clienteProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		clienteProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteProduto.class);
		clienteProdutoFilter.cdCliente = cdCliente;
		clienteProdutoFilter.flTipoRelacaoList = new String[] {ClienteProduto.RELACAOEXCLUSIVA, ClienteProduto.RELACAOEXCECAO, ClienteProduto.RELACAORESTRICAO};
		return clienteProdutoFilter;
	}

}