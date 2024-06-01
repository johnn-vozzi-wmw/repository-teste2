package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ErroEnvio;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ErroEnvioDbxDao;

public class ErroEnvioService extends CrudService {

    private static ErroEnvioService instance;
    
    private ErroEnvioService() {
        //--
    }
    
    public static ErroEnvioService getInstance() {
        if (instance == null) {
            instance = new ErroEnvioService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ErroEnvioDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

    public void saveErroEnvioPedido(String rowKey, String msgErro) throws SQLException {
    	if (LavenderePdaConfig.isApresentaPedidosComErroNoEnvio()) {
    		ErroEnvio erroEnvio = new ErroEnvio();
    		erroEnvio.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		erroEnvio.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ErroEnvio.class);
    		erroEnvio.nmEntidade = Pedido.TABLE_NAME_PEDIDO;
    		erroEnvio.dsChave = rowKey;
    		erroEnvio.dsErro = msgErro;
    		try {
    			insert(erroEnvio);
    		} catch (Throwable e) {
    			update(erroEnvio);
    		}
    	}
    }

    public boolean hasPedidosErroEnvioServidor() throws SQLException {
    	if (LavenderePdaConfig.isApresentaPedidosComErroNoEnvio()) {
    		ErroEnvio erroEnvioFilter = new ErroEnvio();
    		erroEnvioFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		erroEnvioFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ErroEnvio.class);
    		erroEnvioFilter.nmEntidade = Pedido.TABLE_NAME_PEDIDO;
    		return countByExample(erroEnvioFilter) > 0;
    	}
    	return false;
    }
    

	public void removeErroEnvioPedido(Pedido pedido) {
		if (LavenderePdaConfig.isApresentaPedidosComErroNoEnvio()) {
			ErroEnvio erroEnvio = new ErroEnvio();
    		erroEnvio.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		erroEnvio.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ErroEnvio.class);
    		erroEnvio.nmEntidade = Pedido.TABLE_NAME_PEDIDO;
    		erroEnvio.dsChave = pedido.getRowKey();
    		try {
				delete(erroEnvio);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}
    
}
