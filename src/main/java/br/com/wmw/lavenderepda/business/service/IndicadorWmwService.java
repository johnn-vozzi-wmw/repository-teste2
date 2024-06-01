package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IndicadorWmwDbxDao;
import totalcross.util.Date;

public class IndicadorWmwService extends CrudService {

    private static IndicadorWmwService instance;
    
    private IndicadorWmwService() {
        //--
    }
    
    public static IndicadorWmwService getInstance() {
        if (instance == null) {
            instance = new IndicadorWmwService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return IndicadorWmwDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public List<ValorIndicador> buscaIndicadoresPor(String cdEmpresa, String cdRepresentante, Date dtReferencia) throws SQLException {
		return IndicadorWmwDbxDao.getInstance().buscaIndicadoresPor(cdEmpresa, cdRepresentante, dtReferencia);
	}

}