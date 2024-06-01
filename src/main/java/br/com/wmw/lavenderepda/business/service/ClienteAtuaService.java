package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteAtuaDbxDao;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ClienteAtuaService extends CrudPersonLavendereService {

    private static ClienteAtuaService instance;

    private ClienteAtuaService() {
        //--
    }

    public static ClienteAtuaService getInstance() {
        if (instance == null) {
            instance = new ClienteAtuaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
    	return ClienteAtuaDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	ClienteAtua clienteAtua = (ClienteAtua) domain;
    	//cdCliente
    	if (ValueUtil.isEmpty(clienteAtua.cdCliente)) {
    		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTE_NOME_ENTIDADE);
    	}
    	if (LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente()) {
    		if (!hasChangesValuesDynamics(clienteAtua) && !ishouveAlteracaoCpfCnpj(clienteAtua)) {
    			throw new ValidationException(Messages.CLIENTEATUA_MSG_NENHUMA_INFO_ALTERADA);
    		}
    	}
		if (ValueUtil.isNotEmpty(clienteAtua.nuCnpj)) {
			if (!CpfCnpjValidator.validateCnpjCpf(clienteAtua.nuCnpj)) {
                throw new ValidationException(Messages.CLIENTEATUA_MSG_NUCNPJCPF_INVALIDO);
            }
		}
    	super.validate(domain);
    }

	public void deleteAllEnviadosServidor() throws SQLException {
		if (LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente()) {
			ClienteAtua clienteAtua = new ClienteAtua();
			clienteAtua.flTipoAlteracao = ClienteAtua.FLTIPOALTERACAO_ORIGINAL;
			deleteAllByExample(clienteAtua);
		}
	}
	
	public void loadValuesClienteAtuaByCliente(ClienteAtua clienteAtua, Cliente cliente) {
		if (clienteAtua != null && cliente != null) {
			loadValuesFixosClienteIntoClienteAtua(clienteAtua, cliente);
			loadValuesDinamicosClienteIntoClienteAtua(clienteAtua, cliente);
		}
	}
	
	private void loadValuesFixosClienteIntoClienteAtua(ClienteAtua clienteAtua, Cliente cliente) {
		Field[] camposCliente = Cliente.class.getDeclaredFields();
		if (ValueUtil.isNotEmpty(camposCliente)) {
			for (int i = 0; i < camposCliente.length; i++) {
				Field field = camposCliente[i];
				try {
					if (clienteAtua.getHashValuesDinamicos().exists(field.getName().toUpperCase())) {
    					if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(ClienteAtua.TABLE_NAME, field.getName().toUpperCase())) {
    						field.set(cliente, ValueUtil.getValidNumbers((String) field.get(cliente)));
    	    			}
    					clienteAtua.getHashValuesDinamicos().put(field.getName().toUpperCase(), field.get(cliente));
					}
				} catch (Throwable e) {
					// Não faz nada
				}
			}
		}
	}
	
	private void loadValuesDinamicosClienteIntoClienteAtua(ClienteAtua clienteAtua, Cliente cliente) {
		Vector camposDinamicosClienteProspectList = cliente.getHashValuesDinamicos().getKeys();
		for (int i = 0; i < camposDinamicosClienteProspectList.size(); i++) {
			String coluna = ((String) camposDinamicosClienteProspectList.items[i]).toUpperCase();
			String value = (String) cliente.getHashValuesDinamicos().get(coluna);
			if (ValueUtil.isNotEmpty(value) && clienteAtua.getHashValuesDinamicos().exists(coluna)) {
				if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(ClienteAtua.TABLE_NAME, coluna)) {
					value = ValueUtil.getValidNumbers(value);
				}
				clienteAtua.getHashValuesDinamicos().put(coluna, value);
			}			
		}
	}
	
	public boolean hasChangesValuesDynamics(ClienteAtua clienteAtua) {
		if (clienteAtua != null) {
			Vector camposDinamicosList = clienteAtua.getHashValuesDinamicos().getKeys();
			for (int i = 0; i < camposDinamicosList.size(); i++) {
				String nmColuna = ((String) camposDinamicosList.items[i]).toUpperCase();
				Object value = clienteAtua.getHashValuesDinamicos().get(nmColuna);
				Object valueOriginal = clienteAtua.hashValuesDinamicosOriginal.get(nmColuna);
				if (ValueUtil.isNotEmpty(StringUtil.getStringValue(value)) && !ValueUtil.valueEquals(value, valueOriginal)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void limpaInfoDinamicasSemAlteracoesCadastro(ClienteAtua clienteAtua) {
		if (clienteAtua != null) {
			Vector camposDinamicosList = clienteAtua.getHashValuesDinamicos().getKeys();
			for (int i = 0; i < camposDinamicosList.size(); i++) {
				String nmColuna = ((String) camposDinamicosList.items[i]).toUpperCase();
				Object value = clienteAtua.getHashValuesDinamicos().get(nmColuna);
				Object valueOriginal = clienteAtua.hashValuesDinamicosOriginal.get(nmColuna);
				if (ValueUtil.valueEquals(value, valueOriginal)) {
					clienteAtua.getHashValuesDinamicos().put(nmColuna, "");
				}
			}
		}
	}
	
	public void loadHashValuesDinamicosOriginal(ClienteAtua clienteAtua) {
		if (clienteAtua != null) {
			clienteAtua.hashValuesDinamicosOriginal = new Hashtable(0);
			Vector camposDinamicosList = clienteAtua.getHashValuesDinamicos().getKeys();
			for (int i = 0; i < camposDinamicosList.size(); i++) {
				String nmColuna = ((String) camposDinamicosList.items[i]).toUpperCase();
				Object value = clienteAtua.getHashValuesDinamicos().get(nmColuna);
				clienteAtua.hashValuesDinamicosOriginal.put(nmColuna, value);
			}
		}
	}
	
	private boolean ishouveAlteracaoCpfCnpj(ClienteAtua clienteAtua) {
		return LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente ? clienteAtua.houveAlteracaoCpfCnpj : false;
	}
	
	public ClienteAtua getClienteAtuaFilter(Cliente cliente, int nuDias) {
		Date dtAtualizacaoLimite = DateUtil.getCurrentDate();
		dtAtualizacaoLimite.advance(nuDias * -1);
		ClienteAtua clienteAtuaFilter = new ClienteAtua();
		if (!LavenderePdaConfig.usaCodigoClienteUnico) {
			clienteAtuaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteAtuaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
		clienteAtuaFilter.cdCliente = cliente.cdCliente;
		clienteAtuaFilter.dtAtualizacaoLimite = dtAtualizacaoLimite;
		return clienteAtuaFilter;
	}

}