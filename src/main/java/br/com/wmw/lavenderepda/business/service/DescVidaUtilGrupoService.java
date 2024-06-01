package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescVidaUtilGrupo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescVidaUtilGrupoPdbxDao;
import totalcross.util.Vector;

public class DescVidaUtilGrupoService extends CrudService {

    private static DescVidaUtilGrupoService instance;

    private DescVidaUtilGrupoService() {
        //--
    }

    public static DescVidaUtilGrupoService getInstance() {
        if (instance == null) {
            instance = new DescVidaUtilGrupoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescVidaUtilGrupoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public DescVidaUtilGrupo getDescVidaUtilByLote(double vlPctVidaUtil, String cdGrupoProduto1) throws SQLException {
    	if (ValueUtil.isEmpty(cdGrupoProduto1)) {
    		return null;
    	}
    	DescVidaUtilGrupo descVidaUtilGrupo = new DescVidaUtilGrupo();
    	descVidaUtilGrupo.cdGrupoproduto1 = cdGrupoProduto1;
    	descVidaUtilGrupo.vlPctvidautilminimo = vlPctVidaUtil;
    	descVidaUtilGrupo.vlPctvidautilmaximo = vlPctVidaUtil;
    	Vector descList = findAllByExample(descVidaUtilGrupo);
    	if (descList.size() > 0) {
			return (DescVidaUtilGrupo)descList.items[0];
		} else {
			return null;
		}
    }

}