package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.UsuarioConfigBuilder;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioConfigDao;

public class UsuarioConfigService extends CrudService {

    private static UsuarioConfigService instance;
    
    private UsuarioConfigService() {
        //--
    }
    
    public static UsuarioConfigService getInstance() {
        if (instance == null) {
            instance = new UsuarioConfigService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioConfigDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public double getVlPctDescLiberacaoPedido() throws SQLException {
		UsuarioConfig usuarioConfig = (UsuarioConfig) findByRowKey(new UsuarioConfigBuilder().build().getRowKey());
		return usuarioConfig != null ? usuarioConfig.vlPctDescLiberacaoPedido : 0;
	}
    
    public double getVlPctAcrescLiberacaoPedido() throws SQLException {
    	UsuarioConfig usuarioConfig = (UsuarioConfig) findByRowKey(new UsuarioConfigBuilder().build().getRowKey());
    	return usuarioConfig != null ? usuarioConfig.vlPctAcrescLiberacaoPedido : 0;
    }

	public String getCdRepresentantePadrao() throws SQLException {
		UsuarioConfig usuarioConfig = new UsuarioConfig(UsuarioPdaService.getInstance().getCdUsuarioPda());
		usuarioConfig = (UsuarioConfig) findByRowKey(usuarioConfig.getRowKey());
		return usuarioConfig != null ? usuarioConfig.cdRepresentantePadrao : Representante.CDREPRESENTANTE_COMBO_DEFAULT;
	}
    
    public boolean isLiberaItemPendente() throws SQLException {
    	UsuarioConfig usuarioConfig = (UsuarioConfig) findByRowKey(new UsuarioConfigBuilder().build().getRowKey());
    	if (usuarioConfig != null) {
    		return ValueUtil.getBooleanValue(usuarioConfig.flLiberaItemPendente);
    	}
    	return false;
    }

	public boolean isLiberaPedidoOutraOrdem() throws SQLException {
    	UsuarioConfig usuarioConfig = (UsuarioConfig) findByRowKey(new UsuarioConfigBuilder().build().getRowKey());
    	if (usuarioConfig != null) {
    		return ValueUtil.getBooleanValue(usuarioConfig.flLiberaOutraOrdem);
    	}
    	return false;
	}

}