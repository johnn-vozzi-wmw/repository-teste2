package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import br.com.wmw.lavenderepda.business.domain.FeriadoEmpresa;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FeriadoEmpresaDbxDao;
import totalcross.util.Vector;

public class FeriadoEmpresaService extends CrudService {

    private static FeriadoEmpresaService instance;
    
    private FeriadoEmpresaService() {
        //--
    }
    
    public static FeriadoEmpresaService getInstance() {
        if (instance == null) {
            instance = new FeriadoEmpresaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FeriadoEmpresaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public Vector findAllByExampleFeriadoEmpresaAsFeriado(Feriado feriadoFilter) throws SQLException {
    	Vector feriadoList = new Vector();
		FeriadoEmpresa feriadoEmpresaFilter = new FeriadoEmpresa();
		feriadoEmpresaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		feriadoEmpresaFilter.nuDia = feriadoFilter.nuDia;
		feriadoEmpresaFilter.nuMes = feriadoFilter.nuMes;
		feriadoEmpresaFilter.nuAno = feriadoFilter.nuAno;
		Vector feriadoEmpresaList = findAllByExample(feriadoEmpresaFilter);
		for (int i = 0; i < feriadoEmpresaList.size(); i++) {
			FeriadoEmpresa feriadoEmpresa = (FeriadoEmpresa) feriadoEmpresaList.items[i];
			Feriado feriado = new Feriado();
			feriado.nuDia = feriadoEmpresa.nuDia;
			feriado.nuMes = feriadoEmpresa.nuMes;
			feriado.nuAno = feriadoEmpresa.nuAno;
			feriado.dsFeriado = feriadoEmpresa.dsFeriado;
			feriadoList.addElement(feriado);
		}
    	return feriadoList;
    }
    
    public int countByExampleFeriadoEmpresaByFeriado(Feriado feriado) throws SQLException {
		FeriadoEmpresa feriadoEmpresaFilter = new FeriadoEmpresa();
		feriadoEmpresaFilter.nuDia = feriado.nuDia;
		feriadoEmpresaFilter.nuMes = feriado.nuMes;
		feriadoEmpresaFilter.nuAno = feriado.nuAno;
    	return super.countByExample(feriadoEmpresaFilter);
    }

}