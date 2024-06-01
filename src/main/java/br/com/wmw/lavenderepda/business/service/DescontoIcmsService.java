package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.DescontoIcms;
import br.com.wmw.lavenderepda.business.domain.IcmsCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoIcmsDbxDao;

public class DescontoIcmsService extends CrudService {

    private static DescontoIcmsService instance;
    
    private DescontoIcmsService() {
        //--
    }
    
    public static DescontoIcmsService getInstance() {
        if (instance == null) {
            instance = new DescontoIcmsService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return DescontoIcmsDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    public DescontoIcms findDescontoIcmsPorIcmsCliente(IcmsCliente icmsCliente) throws SQLException {
    	DescontoIcms filter = new DescontoIcms();
    	filter.cdEmpresa = icmsCliente.cdEmpresa;
    	filter.cdRepresentante = icmsCliente.cdRepresentante;
    	filter.vlPctIcms = icmsCliente.vlPctIcms;
    	return (DescontoIcms) findByPrimaryKey(filter);
    }

}