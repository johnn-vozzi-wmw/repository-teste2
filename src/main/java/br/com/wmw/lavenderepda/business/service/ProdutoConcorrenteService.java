package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.ProdutoConcorrente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoConcorrenteDbxDao;
import totalcross.util.Vector;

public class ProdutoConcorrenteService extends CrudService {

    private static ProdutoConcorrenteService instance;
    
    private ProdutoConcorrenteService() {
        //--
    }
    
    public static ProdutoConcorrenteService getInstance() {
        if (instance == null) {
            instance = new ProdutoConcorrenteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoConcorrenteDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public ProdutoConcorrente getProdutoByPesqMercado(PesquisaMercado pesquisaMercado) throws SQLException {
		ProdutoConcorrente produtoConcorrenteFilter = new ProdutoConcorrente(pesquisaMercado.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoConcorrente.class), pesquisaMercado.cdProdutoConcorrente, pesquisaMercado.cdConcorrente);
		return (ProdutoConcorrente) findByRowKey(produtoConcorrenteFilter.getRowKey());
	}

	public Vector filtraProdutoConcorrente(String dsFiltro, String cdConcorrente) throws SQLException {
		ProdutoConcorrente produtoConcorrenteFilter = new ProdutoConcorrente();
		produtoConcorrenteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoConcorrenteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoConcorrenteFilter.cdConcorrente = cdConcorrente;
		produtoConcorrenteFilter.cdProdutoConcorrente = dsFiltro;
		produtoConcorrenteFilter.dsProdutoConcorrente = dsFiltro;
		return findAllByExample(produtoConcorrenteFilter);
	}

}