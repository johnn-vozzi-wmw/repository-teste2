package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.ClienteChurnAtua;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteChurnAtuaDao;
import totalcross.util.Date;

public class ClienteChurnAtuaService extends CrudPersonLavendereService {

    private static ClienteChurnAtuaService instance = null;
    
    private ClienteChurnAtuaService() { }
    
    public static ClienteChurnAtuaService getInstance() {
        if (instance == null) {
            instance = new ClienteChurnAtuaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteChurnAtuaDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { }
    
    
	protected void insereOuAtualiza(ClienteChurn clienteChurn) throws SQLException {
		ClienteChurnAtua clienteChurnAtua =  (ClienteChurnAtua) findAllByExample(new ClienteChurnAtua(clienteChurn)).items[0];
		if (clienteChurnAtua == null || clienteChurnAtua.isEnviadoServidor()) {
			insere(clienteChurn);
			return;
		}
		atualiza(clienteChurnAtua, clienteChurn);
	}

	private void insere(ClienteChurn clienteChurn) throws SQLException {
		ClienteChurnAtua clienteChurnAtua = new ClienteChurnAtua(clienteChurn);
		clienteChurnAtua.cdChurnAtua = generateIdGlobal();
		clienteChurnAtua.cdMotivoChurn = clienteChurn.cdMotivoChurn;
		clienteChurnAtua.dsObsChurn = clienteChurn.dsObsChurn;
		clienteChurnAtua.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		clienteChurnAtua.dtAlteracao = new Date();
		clienteChurnAtua.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		insert(clienteChurnAtua);
	}
	
	private void atualiza(ClienteChurnAtua clienteChurnAtua, ClienteChurn clienteChurn) throws SQLException {
		clienteChurnAtua.cdMotivoChurn = clienteChurn.cdMotivoChurn;
		clienteChurnAtua.dsObsChurn = clienteChurn.dsObsChurn;
		clienteChurnAtua.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		clienteChurnAtua.dtAlteracao = new Date();
		clienteChurnAtua.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		update(clienteChurnAtua);
	}

}