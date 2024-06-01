package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.builder.UsuarioConfigEmpBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfigEmp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioConfigEmpDao;

public class UsuarioConfigEmpService extends CrudService {

    private static UsuarioConfigEmpService instance;
    
    private UsuarioConfigEmpService() {
        //--
    }
    
    public static UsuarioConfigEmpService getInstance() {
        if (instance == null) {
            instance = new UsuarioConfigEmpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioConfigEmpDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    protected double getVlPctMaxDesconto() throws SQLException {
    	UsuarioConfigEmp usuarioConfigEmp = (UsuarioConfigEmp) findByRowKey(new UsuarioConfigEmpBuilder().build().getRowKey());
    	if (usuarioConfigEmp == null) {
    		if (LavenderePdaConfig.isDsOpcoesFaltaDescAcrescPorUsuarioApenasDesconto()) {
    			return 100;
    		} else if (LavenderePdaConfig.isDsOpcoesFaltaDescAcrescPorUsuario()) {
        	return UsuarioConfigService.getInstance().getVlPctDescLiberacaoPedido();
    		} else {
    			return 0;
    	}
    	}
    	return usuarioConfigEmp.vlPctMaxDesconto;
    }
    
    protected double getVlPctMaxAcrescimo() throws SQLException {
    	UsuarioConfigEmp usuarioConfigEmp = (UsuarioConfigEmp) findByRowKey(new UsuarioConfigEmpBuilder().build().getRowKey());
    	if (usuarioConfigEmp == null) {
    		if (LavenderePdaConfig.isDsOpcoesFaltaDescAcrescPorUsuarioApenasAcrescimo()) {
    			return 999999999;
    		} else if (LavenderePdaConfig.isDsOpcoesFaltaDescAcrescPorUsuario()) {
    		return UsuarioConfigService.getInstance().getVlPctAcrescLiberacaoPedido();
    		} else {
    			return 0;
    	}
    	}
    	return usuarioConfigEmp.vlPctMaxAcrescimo;
    }

}