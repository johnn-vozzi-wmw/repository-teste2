package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MotivoColeta;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoColetaDbxDao;
import totalcross.util.Vector;

public class MotivoColetaService extends CrudService {

    private static MotivoColetaService instance;
    
    private MotivoColetaService() {
        //--
    }
    
    public static MotivoColetaService getInstance() {
        if (instance == null) {
            instance = new MotivoColetaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotivoColetaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public MotivoColeta findMotivoColetaPadrao() throws SQLException {
    	MotivoColeta motivoColeta = new MotivoColeta();
    	motivoColeta.flEncerraAutomatico = ValueUtil.VALOR_SIM;
    	Vector motivoColetaList = findAllByExample(motivoColeta);
    	if (ValueUtil.isEmpty(motivoColetaList)) {
			return new MotivoColeta();
		}
    	return (MotivoColeta) motivoColetaList.items[0];
    }

}