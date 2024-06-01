package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.MotivoChurn;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteChurnDao;
import totalcross.util.Vector;

public class ClienteChurnService extends CrudPersonLavendereService {

    private static ClienteChurnService instance = null;
    
    private ClienteChurnService() { }
    
    public static ClienteChurnService getInstance() {
        if (instance == null) {
            instance = new ClienteChurnService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteChurnDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { 
    	ClienteChurn clienteChurn = (ClienteChurn) domain;
    	if (ValueUtil.isEmpty(clienteChurn.cdMotivoChurn) || clienteChurn.getMotivoChurn().isSemMotivoInformado()) {
    		throw new ValidationException(Messages.CLIENTECHURN_CAMPO_MOTIVO_OBRIGATORIO_VALIDATE);
    	}
    }
    
     @Override
    public void update(BaseDomain domain) throws SQLException {
    	CrudDbxDao.getCurrentDriver().startTransaction();
    	try {
    		super.update(domain);
    		ClienteChurnAtuaService.getInstance().insereOuAtualiza((ClienteChurn) domain);
    		CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable ex) {
			throw new ValidationException(MessageUtil.getMessage(Messages.CLIENTECHURN_ERRO_ATUALIZAR, ex.getMessage()));
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
    }

	public int getTotalGeralClienteChurn(ClienteChurn clienteChurn) throws SQLException {
		return ClienteChurnDao.getInstance().getTotalGeralClienteChurn(clienteChurn);
	}
	
	public int getTotalParcialClienteChurn(ClienteChurn clienteChurn) throws SQLException {
		return ClienteChurnDao.getInstance().getTotalParcialClienteChurn(clienteChurn);
	}
	
	public int getTotalClienteChurnComMotivoInformado() throws SQLException {
		ClienteChurn clienteChurnFilter = new ClienteChurn(SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteChurn.class));
		return ClienteChurnDao.getInstance().getTotalClienteChurnComMotivoInformado(clienteChurnFilter);
	}
	
	public boolean obrigaRegistrarRiscoChurn() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) return false;
		
		int nudias = LavenderePdaConfig.nuDiasObrigarMotivoRiscoChurn();
		if (nudias == 0) return false;
		
		ClienteChurn clienteChurnFilter = new ClienteChurn(SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteChurn.class));
		clienteChurnFilter.setMotivoChurn(MotivoChurn.CD_SEM_MOTIVO_INFORMADO);
		Vector clienteChurnList = ClienteChurnService.getInstance().findAllByExample(clienteChurnFilter);
		int size = clienteChurnList.size();
		for (int i = 0; i < size; i++) {
			ClienteChurn clienteChurn = (ClienteChurn) clienteChurnList.items[i];
			if (nudias < DateUtil.getDaysBetween(DateUtil.getCurrentDate(), clienteChurn.dtEntradaChurn)) {
				UiUtil.showWarnMessage(Messages.CLIENTECHURN_CLIENTES_PRAZO_EXTRAPOLADO);
				return true;
			}
		}
		return false;
	}

	public ClienteChurn getClienteChurn(Cliente cliente) throws SQLException {
		ClienteChurn clienteChurnFilter = new ClienteChurn(SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteChurn.class));
		clienteChurnFilter.setCliente(cliente);
		return (ClienteChurn) findAllByExample(clienteChurnFilter).items[0];
	}

}