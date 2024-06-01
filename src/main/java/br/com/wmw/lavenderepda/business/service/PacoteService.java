package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PacotePdbxDao;
import totalcross.util.Vector;

public class PacoteService extends CrudPersonLavendereService {

    private static PacoteService instance;

    public static PacoteService getInstance() {
        if (instance == null) {
            instance = new PacoteService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() { return PacotePdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
	public Vector findAllPacoteByItemPedido(ItemPedido itemPedido) throws SQLException {
		Pacote pacoteFilter = new Pacote(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
		pacoteFilter.cdProdutoFilter = itemPedido.cdProduto;
		pacoteFilter.cdTabelaPrecoFilter = (LavenderePdaConfig.usaTabPrecoDescQuantidadePorPacote) ? itemPedido.cdTabelaPreco : DescontoPacote.CD_TABELA_PRECO_0;
		return PacotePdbxDao.getInstance().findAllPacoteByItemPedido(pacoteFilter);
	}
	
    
}