package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CestaPromocional;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaPromocionalPdbxDao;
import totalcross.util.Vector;

public class CestaPromocionalService extends CrudService {

    private static CestaPromocionalService instance;

    private CestaPromocionalService() {
        //--
    }

    public static CestaPromocionalService getInstance() {
        if (instance == null) {
            instance = new CestaPromocionalService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CestaPromocionalPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public CestaPromocional findCestaByClienteProduto(String cdProduto, String cdCliente) throws SQLException {
		CestaPromocional cestaPromocional = new CestaPromocional();
		cestaPromocional.cdCliente = cdCliente;
		cestaPromocional.cdProduto = cdProduto;
		cestaPromocional.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cestaPromocional.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cestaPromocional.dtVigenciaInicial = DateUtil.getCurrentDate();
		cestaPromocional.dtVigenciaFinal = DateUtil.getCurrentDate();
		Vector listCestaPromo =  findAllByExample(cestaPromocional);
		if (listCestaPromo.size() > 0) {
			return (CestaPromocional)listCestaPromo.items[0];
		}
		return null;
    }

}