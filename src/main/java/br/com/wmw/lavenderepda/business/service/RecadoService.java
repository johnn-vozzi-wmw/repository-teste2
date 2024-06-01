package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.TipoRecado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RecadoPdbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class RecadoService extends CrudService {

    private static RecadoService instance;

    private RecadoService() {
        //--
    }

    public static RecadoService getInstance() {
        if (instance == null) {
            instance = new RecadoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RecadoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

        Recado recado = (Recado) domain;

        //cdUsuariodestinatario
        if (ValueUtil.isEmpty(recado.cdUsuarioDestinatario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RECADO_LABEL_CDUSUARIODESTINATARIO);
        }
        //dsAssunto
        if (ValueUtil.isEmpty(recado.dsAssunto) || ValueUtil.isEmpty(recado.dsAssunto.trim())) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RECADO_LABEL_DSASSUNTO);
        }
        //dsRecado
        if (ValueUtil.isEmpty(recado.dsRecado)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RECADO_LABEL_DSRECADO);
        }
    }

    public boolean existeRecadosNaoLidos() throws SQLException {
    	Recado recadoFilter = new Recado();
    	recadoFilter.flLido = ValueUtil.VALOR_NAO;
    	recadoFilter.cdUsuarioDestinatario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	return RecadoService.getInstance().findAllByExample(recadoFilter).size() > 0;
    }

    public boolean existeRecadosNaoRespondidos() throws SQLException {
    	Recado recadoFilter = new Recado();
    	recadoFilter.flObrigaResposta = ValueUtil.VALOR_SIM;
    	recadoFilter.flRespostaEnviada = ValueUtil.VALOR_NAO;
    	recadoFilter.cdUsuarioDestinatario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	return RecadoPdbxDao.getInstance().countByExample(recadoFilter) > 0;

    }

    public Vector getListRecadosByTipoAndStatus(int tipoRecado, BaseDomain domain) throws SQLException {
    	Vector recadoList = new Vector();
    	if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA) {
    		recadoList = getRecadosRecebidosList(domain, null);
    	} else if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_SAIDA) {
    		recadoList = getRecadosEnviadosList(Recado.STATUSENVIO_CAIXASAIDA, domain, null);
    	} else if (tipoRecado == TipoRecado.TIPORECADO_CDITENS_ENVIADOS) {
    		recadoList = getRecadosEnviadosList(Recado.STATUSENVIO_ITENSENVIADOS, domain, null);
    	}
    	return recadoList;
    }
    
    public Vector getListRecadosByTipoAndStatusAndClienteSelecionado(int tipoRecado, BaseDomain domain, Cliente cliente) throws SQLException {
    	Vector recadoList = new Vector();
    	if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA) {
    		recadoList = getRecadosRecebidosList(domain, cliente);
    	} else if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_SAIDA) {
    		recadoList = getRecadosEnviadosList(Recado.STATUSENVIO_CAIXASAIDA, domain, cliente);
    	} else if (tipoRecado == TipoRecado.TIPORECADO_CDITENS_ENVIADOS) {
    		recadoList = getRecadosEnviadosList(Recado.STATUSENVIO_ITENSENVIADOS, domain, cliente);
    	}
    	return recadoList;
    }

    private Vector getRecadosRecebidosList(BaseDomain domain, Cliente cliente) throws SQLException {
		Recado recadoFilter = (Recado)domain;
    	recadoFilter.cdUsuarioDestinatario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	if(cliente != null) {
    		recadoFilter.cdCliente = cliente.cdCliente;
    	}
    	return findAllByExample(recadoFilter);
    }

    private Vector getRecadosEnviadosList(int statusRecado , BaseDomain domain, Cliente cliente) throws SQLException {
    	Vector recadoList = new Vector();
    	Vector recadoTempList = new Vector();
		Recado recadoFilter = (Recado) domain;
    	recadoFilter.cdUsuarioRemetente = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	recadoFilter.cdStatusEnvio = StringUtil.getStringValue(statusRecado);
    	if(cliente != null) {
    		recadoFilter.cdCliente = cliente.cdCliente;
    	}
    	recadoTempList = findAllByExample(recadoFilter);
    	int sizRecado = recadoTempList.size();
    	Recado recado;
    	for (int j = 0; j < sizRecado; j++) {
    		recado = (Recado)recadoTempList.items[j];
			if (!isRecadoInList(recado, recadoList)) {
				recadoList.addElement(recado);
			}
		}
        recadoTempList = null;
    	return recadoList;
    }

    private boolean isRecadoInList(Recado recado, Vector recadoList) {
    	boolean recadoInList = false;
		int size = recadoList.size();
		Recado recadoTemp;
        for (int i = 0; i < size; i++) {
        	recadoTemp = (Recado)recadoList.items[i];
    		if (ValueUtil.valueEquals(recadoTemp.cdUsuarioRemetente, recado.cdUsuarioRemetente) && ValueUtil.valueEquals(recadoTemp.dsAssunto,recado.dsAssunto) && ValueUtil.valueEquals(recadoTemp.hrCadastro,recado.hrCadastro)) {
    			recadoInList = true;
    		}
    	}
    	return recadoInList;
    }

    public void updateAllRecadosToEnviados() throws SQLException {
		Vector recadoList = getRecadosEnviadosListAfterSync(Recado.STATUSENVIO_CAIXASAIDA);
		int size = recadoList.size();
		Recado recado;
        for (int i = 0; i < size; i++) {
        	recado = (Recado)recadoList.items[i];
			recado.cdStatusEnvio = StringUtil.getStringValue(Recado.STATUSENVIO_ITENSENVIADOS);
			getCrudDao().update(recado);
		}
    }

	public void updateDtHrEnvioRecados() throws SQLException {
		((RecadoPdbxDao)getCrudDao()).updateDtHrEnvioRecados();
	}

    private Vector getRecadosEnviadosListAfterSync(int statusRecado) throws SQLException {
    	Vector recadoList = new Vector();
    	Vector recadoTempList = new Vector();
		Recado recadoFilter = new Recado();
    	recadoFilter.cdUsuarioRemetente = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	recadoFilter.cdStatusEnvio = StringUtil.getStringValue(statusRecado);
    	recadoTempList = findAllByExample(recadoFilter);
    	int sizRecado = recadoTempList.size();
    	Recado recado;
    	for (int j = 0; j < sizRecado; j++) {
    		recado = (Recado)recadoTempList.items[j];
			recadoList.addElement(recado);
		}
        recadoTempList = null;
    	return recadoList;
    }

    public void delete(BaseDomain domain) throws java.sql.SQLException {
    	Recado recado = (Recado) domain;
    	recado.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_EXCLUIDO;
		getCrudDao().update(domain);
    }

	public void deleteAllPurged() throws SQLException {
		((RecadoPdbxDao)getCrudDao()).deleteAllPurged();
	}
		
	public boolean enviarRecadoServidor() {
		try {
			SyncInfo syncInfo = new SyncInfo();
	        syncInfo.tableName = Recado.TABLE_NAME;
	        syncInfo.keys = new String[] {"cdrecado"};
	        syncInfo.flRemessa = true;
	        syncInfo.flRetorno = true;
	        Hashtable hash = new Hashtable(1);
			hash.put(syncInfo.tableName, syncInfo);
			return SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), hash);
		} catch (Throwable e) {
			throw new ValidationException(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
		}
	}

	public void updateRespostaEnviada(String cdRecadoRelacionado) throws SQLException {
		((RecadoPdbxDao)getCrudDao()).updateRespostaEnviada(cdRecadoRelacionado);
	}

}