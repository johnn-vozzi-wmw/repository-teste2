package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.UF;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UFDao;
import totalcross.util.Vector;

public class UFService extends CrudPersonLavendereService {
	
	private static UFService instance;
	
    public static UFService getInstance() {
    	if (instance == null) {
            instance = new UFService();
        }
        return instance;
    }

	//@Override
	protected CrudDao getCrudDao() {
		return UFDao.getInstance();
	}
	
	public int[] getCdUfIbge() throws SQLException {
		UF filter = new UF();
		Empresa emp = new Empresa();
		emp.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.dsUf = EmpresaService.getInstance().findColumnByRowKey(emp.getRowKey(), "dsEstado").trim();
		Vector list = findColumnValuesByExample(filter, "cdUfIbge");
		if (ValueUtil.isNotEmpty(list) && list.items[0] != null) {
			return ValueUtil.charsToIntArray((String)list.items[0]);
		}
		return null;
	}
	
	public String getDsUf(String cdUf) throws SQLException {
		UF filter = new UF(cdUf);
		Vector list = findColumnValuesByExample(filter,"dsUf");
		if (ValueUtil.isNotEmpty(list) && list.items[0] != null) {
			return (String)list.items[0];
		}
		return null;
	}

}
