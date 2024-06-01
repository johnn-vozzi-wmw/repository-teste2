package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteEndAtuaDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ClienteEndAtuaService extends CrudPersonLavendereService {

    private static ClienteEndAtuaService instance;
    
    private ClienteEndAtuaService() {
        //--
    }
    
    public static ClienteEndAtuaService getInstance() {
        if (instance == null) {
            instance = new ClienteEndAtuaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteEndAtuaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	ClienteEndAtua clienteEndAtua  = (ClienteEndAtua) domain;
    	if (!clienteEndAtua.isEnderecoExcluido()) {
    		setUfNaoPermiteInscEstadualVazio(ClienteEndAtua.TABLE_NAME);
    		super.validate(domain);
    	}
    }
    
    public void validaCnpj(boolean flEntregaEditavel, boolean nuCnpjEditavel, boolean nuCnpjObrigatorio, BaseDomain domain) {
    	ClienteEndereco clienteEndereco = (ClienteEndereco)domain;
    	if (ValueUtil.isNotEmpty(clienteEndereco.nuCnpj) && !CpfCnpjValidator.validateCnpjCpf(clienteEndereco.nuCnpj)) {
    		throw new ValidationException(Messages.NOVOCLIENTE_MSG_NUCNPJCPF_INVALIDO);
    	}
    	if (!nuCnpjObrigatorio && flEntregaEditavel && nuCnpjEditavel && (clienteEndereco.isEntrega() && ValueUtil.isEmpty(clienteEndereco.nuCnpj))) {
    		throw new ValidationException(Messages.CLIENTEENDERECO_ENDENTREGA_CNPJVAZIO);
    	}
    }
    
    public void validaCpf(boolean nuCnpjEditavel, boolean flEntregaEditavel,  ClienteEndereco clienteEndereco) {
    	if (!nuCnpjEditavel || ValueUtil.isEmpty(clienteEndereco.nuCnpj) || !clienteEndereco.isFisica()) return;
    	
    	if (!CpfCnpjValidator.validateCnpjCpf(clienteEndereco.nuCnpj)) {    	
    		throw new ValidationException(Messages.NOVOCLIENTE_MSG_NUCNPJCPF_INVALIDO);
    	}
    	
    	if (LavenderePdaConfig.isObrigaCnpjParaEnderecosEntregaCliente() && flEntregaEditavel  && clienteEndereco.isEntrega()) {
    		throw new ValidationException(Messages.CLIENTEENDERECO_ENDENTREGA_JURIDICA);
    	}
    }
    
    public void populaCamposDinamicosByClienteEndereco(ClienteEndAtua clienteEndAtua, ClienteEndereco clienteEndereco) {
    	if (clienteEndAtua == null || clienteEndereco == null) return;
    	
		Field[] camposClienteEndereco = ClienteEndereco.class.getDeclaredFields();
		if (ValueUtil.isEmpty(camposClienteEndereco)) return;
		
		for (int i = 0; i < camposClienteEndereco.length; i++) {
			Field field = camposClienteEndereco[i];
			if (clienteEndAtua.getHashValuesDinamicos().exists(field.getName().toUpperCase())) {
				try {
					Object value = clienteEndereco.getHashValuesDinamicos().get(field.getName().toUpperCase());
					value = ValueUtil.isEmpty(value) ? field.get(clienteEndereco) : value;
					if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(ClienteEndAtua.TABLE_NAME, field.getName().toUpperCase())) {
						value = ValueUtil.getValidNumbers((String) value);
						field.set(clienteEndereco, value);
	    			}
					clienteEndAtua.getHashValuesDinamicos().put(field.getName().toUpperCase(), value);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				} 
			}
		}
		populaCamposDinamicosByClienteEnderecoDinamico(clienteEndAtua, clienteEndereco);
    }
    
    public void populaCamposDinamicosClienteEndereco(ClienteEndereco clienteEndereco) {
    	Vector camposDinamicoList = BasePersonDomain.getConfigPersonCadListDyn(ClienteEndereco.TABLE_NAME);
    	int size = camposDinamicoList.size();
    	for (int i = 0; i < size; i++) {
    		Campo campoDinamico = (Campo) camposDinamicoList.items[i];
    		if (!campoDinamico.isEditavel()) continue;
    		
    		
    		ClienteEndAtua clienteEndAtua = findClienteEndAtuaByClienteEndereco(clienteEndereco);
    		
    		if (!clienteEndAtua.getHashValuesDinamicos().exists(campoDinamico.nmCampo)) continue;
    	}
    	
    }
    
    private ClienteEndAtua findClienteEndAtuaByClienteEndereco(ClienteEndereco clienteEndereco) {
		return null;
	}

	public void populaCamposDinamicosByClienteEnderecoDinamico(ClienteEndAtua clienteEndAtua, ClienteEndereco clienteEndereco) {
    	Vector camposDinamicoList = BasePersonDomain.getConfigPersonCadListDyn(ClienteEndereco.TABLE_NAME);
    	int size = camposDinamicoList.size();
    	for (int i = 0; i < size; i++) {
    		Campo campoDinamico = (Campo) camposDinamicoList.items[i];
    		if (!campoDinamico.isEditavel()) continue;
    		
    		if (!clienteEndAtua.getHashValuesDinamicos().exists(campoDinamico.nmCampo)) continue;
    		
    		String value = (String) clienteEndereco.getHashValuesDinamicos().get(campoDinamico.nmCampo);
    		if (value == null) continue;
    		
    		clienteEndAtua.getHashValuesDinamicos().put(campoDinamico.nmCampo, value); 
		}
    }
    
    public void deleteClienteEndAtuaAntigos() throws SQLException {
    	if (LavenderePdaConfig.nuDiasPermanenciaAtualizacaoEnderecoCliente() > 0) {
    		Date dataLimite = DateUtil.getCurrentDate();
    		dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaAtualizacaoEnderecoCliente());
    		ClienteEndAtua clienteEndAtua = new ClienteEndAtua();
    		clienteEndAtua.dtAtualizacaoMenorIgualFilter = dataLimite;
    		deleteAllByExample(clienteEndAtua);
    	}
    }
    
    private boolean hasChangesValues(ClienteEndAtua clienteEndAtua, ClienteEndereco clienteEndereco) {
    	if (clienteEndAtua == null || clienteEndereco == null) return false;
    	
    	Field[] camposClienteEndereco = ClienteEndereco.class.getDeclaredFields();
    	if (ValueUtil.isEmpty(camposClienteEndereco)) return false;
    		
    	for (int i = 0; i < camposClienteEndereco.length; i++) {
    		Field field = camposClienteEndereco[i];
    		if (!clienteEndAtua.getHashValuesDinamicos().exists(field.getName().toUpperCase())) continue;
    		
    		try {
    			Object valorOriginal = field.get(clienteEndereco);
    			Object valorAtual = clienteEndAtua.getHashValuesDinamicos().get(field.getName().toUpperCase());
    			if (ValueUtil.valueEquals(valorAtual, valorOriginal)) continue;
    			
    			return true;
    		} catch (Throwable e) {
    			// Não faz nada
    		} 
    	}
		return false;
	}
    
    private boolean hasChangesValuesDinamic(ClienteEndAtua clienteEndAtua, ClienteEndereco clienteEndereco) throws SQLException {
    	if (clienteEndAtua == null || clienteEndereco == null) return false;
    	
    	Vector camposDinamicoList = BasePersonDomain.getConfigPersonCadListDyn(ClienteEndereco.TABLE_NAME);
    	int size = camposDinamicoList.size();
    	if (size == 0) return true;
    	
    	for (int i = 0; i < size; i++) {
    		Campo campoDinamico = (Campo) camposDinamicoList.items[i];
    		if (!campoDinamico.isEditavel()) continue;
    		
    		if (!clienteEndAtua.getHashValuesDinamicos().exists(campoDinamico.nmCampo)) continue;
    	
    		String valorOriginal = ClienteEnderecoService.getInstance().findColumnByRowKey(clienteEndereco.getRowKey(), campoDinamico.nmCampo);
    		String valorAtual = (String) clienteEndAtua.getHashValuesDinamicos().get(campoDinamico.nmCampo);
    		if (ValueUtil.valueEquals(valorAtual, valorOriginal)) continue;
			
    		return true;
		}
    	return false;
	}
    
    public void validateValuesDynamicsChange(ClienteEndAtua clienteEndAtua, ClienteEndereco clienteEndereco) throws SQLException {
    	if (!hasChangesValues(clienteEndAtua, clienteEndereco) && !hasChangesValuesDinamic(clienteEndAtua, clienteEndereco)) {
			throw new ValidationException(Messages.CLIENTEATUA_MSG_NENHUMA_INFO_ALTERADA);
		}
    }

}