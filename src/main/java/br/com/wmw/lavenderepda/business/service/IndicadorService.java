package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Indicador;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IndicadorPdbxDao;
import totalcross.util.Date;

public class IndicadorService extends CrudService {

    private static IndicadorService instance;

    private IndicadorService() {
        //--
    }

    public static IndicadorService getInstance() {
        if (instance == null) {
            instance = new IndicadorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return IndicadorPdbxDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public String getDescription(String cdIndicador) throws SQLException {
		Indicador indicadorFilter = new Indicador();
		indicadorFilter.cdIndicador = cdIndicador;
		indicadorFilter.dsIndicador = IndicadorService.getInstance().findColumnByRowKey(indicadorFilter.getRowKey(), "DSINDICADOR");
		return indicadorFilter.toString();
	}
	
	public List<ValorIndicador> buscaIndicadoresPor(String cdEmpresa, String cdRepresentante, Date dtReferencia) throws java.sql.SQLException {
    	return IndicadorPdbxDao.getInstance().buscaIndicadoresPor(cdEmpresa, cdRepresentante, dtReferencia);
    }
}