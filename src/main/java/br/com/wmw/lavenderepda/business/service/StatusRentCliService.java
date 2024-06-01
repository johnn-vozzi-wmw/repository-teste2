package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.StatusRentCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusRentCliDbxDao;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.gfx.Color;

public class StatusRentCliService extends CrudService {

    private static StatusRentCliService instance;
    
    private StatusRentCliService() {
        //--
    }
    
    public static StatusRentCliService getInstance() {
        if (instance == null) {
            instance = new StatusRentCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return StatusRentCliDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public StatusRentCli getStatusRentabilidade(String cdEmpresa, String cdStatusRentCli) throws SQLException {
    	StatusRentCli status = new StatusRentCli();
    	status.cdEmpresa = cdEmpresa;
    	status.cdStatusRentCli = cdStatusRentCli;
    	return (StatusRentCli) findByPrimaryKeyInCacheVector(status);
    }
    
    public int getStatusRentListColor(String cdEmpresa, String cdStatusRentCli) throws SQLException {
    	StatusRentCli status = getStatusRentabilidade(cdEmpresa, cdStatusRentCli);
    	if (status == null || (status.vlRLista == 255 && status.vlGLista == 255 && status.vlBLista == 255)) {
    		return LavendereColorUtil.formsBackColor;
    	}
    	return Color.getRGB(status.vlRLista, status.vlGLista, status.vlBLista);
    }
    
}