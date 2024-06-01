package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoRepPdbxDao;

public class TabelaPrecoRepService extends CrudService {

    private static TabelaPrecoRepService instance;

    private TabelaPrecoRepService() {
        //--
    }

    public static TabelaPrecoRepService getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoRepService();
        }
        return instance;
    }
    
    public TabelaPrecoRep getTabelaPrecoRep(String cdTabelaPreco) throws SQLException {
    	TabelaPrecoRep tabelaPreco = getTabelaPrecoInstance();
    	tabelaPreco.cdTabelaPreco = cdTabelaPreco;
    	tabelaPreco = (TabelaPrecoRep) findByRowKey(tabelaPreco.getRowKey());
    	if (tabelaPreco == null) {
    		tabelaPreco = new TabelaPrecoRep();
    	}
    	return tabelaPreco;
    }
    
	private TabelaPrecoRep getTabelaPrecoInstance() {
		TabelaPrecoRep tabelaPreco = new TabelaPrecoRep();
		tabelaPreco.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tabelaPreco.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPrecoRep.class);
		return tabelaPreco;
	}

    //@Override
    protected CrudDao getCrudDao() {
        return TabelaPrecoRepPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}