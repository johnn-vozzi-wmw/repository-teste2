package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoVeiculoDao;

public class TipoVeiculoService extends CrudService {

    private static TipoVeiculoService instance;
    
    private TipoVeiculoService() {
        //--
    }
    
    public static TipoVeiculoService getInstance() {
        if (instance == null) {
            instance = new TipoVeiculoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoVeiculoDao.getInstance();
    }

	@Override
	public void validate(BaseDomain arg0) { }
}