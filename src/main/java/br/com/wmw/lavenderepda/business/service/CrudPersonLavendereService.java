package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CampoService;
import br.com.wmw.framework.business.service.CrudPersonService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoEntrega;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LavendereCrudPersonDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PeriodoEntregaDbxDao;
import totalcross.util.Vector;

public abstract class CrudPersonLavendereService extends CrudPersonService {

	@Override
	protected CampoService getCampoService() {
		return CampoLavendereService.getInstance();
	}
	
	@Override
	public Vector findAllForEntidadeRelacionamento(String nmEntidadeRelacionamento) throws SQLException {
		String[] columns = getNmColumnKeyAndDescription(nmEntidadeRelacionamento);
		String tableName = getPdaTableNameFromNmEntidade(nmEntidadeRelacionamento);
		try {
			Vector result = new Vector(0);
			if (PeriodoEntrega.TABLE_NAME.equals(tableName)) {
				return PeriodoEntregaDbxDao.getInstance().findAllCampoDinamicoComboBox(columns);
			}
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			}
			if (ValueUtil.isEmpty(result)) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
			}
			return result;
		} catch (Throwable e) {
			try {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, null);
			} catch (Throwable ex) {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns);
			}
		}
	}
	
	@Override
	public Vector findAllForEntidadeRelacionamento(String nmEntidadeRelacionamento, String filter) throws SQLException {
		String[] columns = getNmColumnKeyAndDescription(nmEntidadeRelacionamento);
		String tableName = getPdaTableNameFromNmEntidade(nmEntidadeRelacionamento);
		try {
			Vector result = new Vector(0);
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, filter);
			}
			if (ValueUtil.isEmpty(result)) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, filter);
			}
			return result;
		} catch (Throwable e) {
			try {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, SessionLavenderePda.cdEmpresa, null, filter);
			} catch (Throwable ex) {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllPerson(tableName, columns, filter);
			}
		}
	}

	@Override
	public String findColumnValue(String tableName, String nmColuna, String columnKeyName, String columnKeyValue) throws SQLException {
		try {
			String columnValue = null;
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				columnValue = ((LavendereCrudPersonDbxDao)getCrudDao()).findColumnValue(tableName, nmColuna, columnKeyName, columnKeyValue, SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			}
			if ((columnValue == null) || columnValue.equals(columnKeyValue)) {
				columnValue = ((LavendereCrudPersonDbxDao)getCrudDao()).findColumnValue(tableName, nmColuna, columnKeyName, columnKeyValue, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
			}
			return columnValue;
		} catch (Throwable e) {
			try {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findColumnValue(tableName, nmColuna, columnKeyName, columnKeyValue, SessionLavenderePda.cdEmpresa, null);
			} catch (Throwable ex) {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findColumnValue(tableName, nmColuna, columnKeyName, columnKeyValue, null, null);
			}
		}
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
		super.validate(domain);
		BasePersonDomain basePersonDomain = (BasePersonDomain) domain;
        Vector listPerson = BasePersonDomain.getConfigPersonCadList(getCrudDao().getTableNameToCamposDinamicos());
        if (listPerson != null) {
        	int size = listPerson.size();
        	String nmCampoDinamico;
        	for (int i = 0; i < size; i++) {
        		Campo config = (Campo)listPerson.items[i];
        		if ((!ValueUtil.VALOR_SIM.equals(config.flVisivelCad)) || (!ValueUtil.VALOR_SIM.equals(config.flEditavel))) {
            		continue;
            	}
        		nmCampoDinamico = config.nmCampo.toUpperCase();
        		if ((Campo.TIPO_INTEIRO == config.cdDominio) || (Campo.TIPO_DECIMAL == config.cdDominio)) {
        			double vlCampo = ValueUtil.getDoubleValue((String)basePersonDomain.getHashValuesDinamicos().get(nmCampoDinamico));
        			if (vlCampo == 0) {
        				continue;
        			}
        		} else {
        			String vlCampo = (String)basePersonDomain.getHashValuesDinamicos().get(nmCampoDinamico);
        			if (ValueUtil.isEmpty(vlCampo)) {
        				continue;
        			}
        		}
        		if (LavenderePdaConfig.obrigaCampoRelacionadoCasoCampoEstejaPreenchido && ValueUtil.isNotEmpty(config.dsCampoRelacionado) && !Campo.FORMATO_CPF_CNPJ.equals(config.dsFormato) && !Campo.FORMATO_INSCRICAOESTADUAL.equals(config.dsFormato)) {
        			Campo campoRelacionado = getCampoRelacionado(listPerson, config.dsCampoRelacionado);
        			if (campoRelacionado != null) {
        				if ((!ValueUtil.VALOR_SIM.equals(campoRelacionado.flVisivelCad)) || (!ValueUtil.VALOR_SIM.equals(campoRelacionado.flEditavel))) {
        					continue;
        				}
        				String nmCampoRelacionado = campoRelacionado.nmCampo.toUpperCase();
        				if ((Campo.TIPO_INTEIRO == campoRelacionado.cdDominio) || (Campo.TIPO_DECIMAL == campoRelacionado.cdDominio)) {
        					double vlCampoRelacionado = ValueUtil.getDoubleValue((String)basePersonDomain.getHashValuesDinamicos().get(nmCampoRelacionado));
        					if (vlCampoRelacionado == 0) {
        						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, campoRelacionado.dsLabel);
        					}
        				} else if (Campo.TIPO_CHECKBOXGROUP == campoRelacionado.cdDominio) {
        					String vlCampoRelacionado = (String)basePersonDomain.getHashValuesDinamicos().get(nmCampoRelacionado);
        					if (ValueUtil.isEmpty(vlCampoRelacionado) || vlCampoRelacionado.indexOf(ValueUtil.VALOR_SIM) == -1) {
        						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, campoRelacionado.dsLabel);
        					}
        				} else if (Campo.TIPO_CHECK == config.cdDominio) { 
        					String vlCampo = (String)basePersonDomain.getHashValuesDinamicos().get(nmCampoDinamico);
        					String vlCampoRelacionado = (String)basePersonDomain.getHashValuesDinamicos().get(nmCampoRelacionado);
        					if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlCampo) && ValueUtil.isEmpty(vlCampoRelacionado)) {
        						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, campoRelacionado.dsLabel);
        					}
        				} else if (ValueUtil.isEmpty((String)basePersonDomain.getHashValuesDinamicos().get(nmCampoRelacionado))) {
        					throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, campoRelacionado.dsLabel);
        				}
        			}
        		}
        	}
        }
	}
	
	private Campo getCampoRelacionado(Vector configPersonCadList, String nmCampoRelacionado) throws SQLException {
		if (ValueUtil.isNotEmpty(configPersonCadList)) {
			Campo campoExample = new Campo();
	    	campoExample.cdSistema = Campo.CD_SISTEMA_PADRAO;
	    	campoExample.nmEntidade = CampoLavendereService.getCampoLavendereInstance().getNmEntidadeFromPdaTableName(getCrudDao().getTableNameToCamposDinamicos());
	    	campoExample.nmCampo = nmCampoRelacionado.toUpperCase();
	    	int index = configPersonCadList.indexOf(campoExample);
	    	if (index != -1) {
	    		Campo campo = (Campo)configPersonCadList.items[index];
	    		return campo;
	    	}
		}
		return null;
	}
	
	@Override
	public Vector findAllComboRelacionadaFilter(String nmEntidadeDependente, String nmEntidadeRelacionada, String dsValorRelacionado) throws SQLException {
		String[] columns = getNmColumnKeyAndDescription(nmEntidadeRelacionada);
		Vector result = new Vector(0);
		try {
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllComboRelacionadaFilter(getPdaTableNameFromNmEntidade(nmEntidadeDependente), getPdaTableNameFromNmEntidade(nmEntidadeRelacionada), getColumnKeyName(nmEntidadeDependente), dsValorRelacionado, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			}
			if (ValueUtil.isEmpty(result)) {
				result = ((LavendereCrudPersonDbxDao)getCrudDao()).findAllComboRelacionadaFilter(getPdaTableNameFromNmEntidade(nmEntidadeDependente), getPdaTableNameFromNmEntidade(nmEntidadeRelacionada), getColumnKeyName(nmEntidadeDependente), dsValorRelacionado, columns, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
			}
			return result;
		} catch (Throwable e) {
			try {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllComboRelacionadaFilter(getPdaTableNameFromNmEntidade(nmEntidadeDependente), getPdaTableNameFromNmEntidade(nmEntidadeRelacionada), getColumnKeyName(nmEntidadeDependente), dsValorRelacionado, columns, SessionLavenderePda.cdEmpresa, null);
			} catch (Throwable ex) {
				return ((LavendereCrudPersonDbxDao)getCrudDao()).findAllComboRelacionadaFilter(getPdaTableNameFromNmEntidade(nmEntidadeDependente), getPdaTableNameFromNmEntidade(nmEntidadeRelacionada), getColumnKeyName(nmEntidadeDependente), dsValorRelacionado, columns);
			}
		}
	}
	
	protected void setUfNaoPermiteInscEstadualVazio(String tableName) {
		String informaUfNaoPermiteInscEstadualVazio = ValueUtil.valueEquals(ValueUtil.VALOR_NAO, LavenderePdaConfig.informaUfNaoPermiteInscEstadualVazio) ? null : LavenderePdaConfig.informaUfNaoPermiteInscEstadualVazio;
		
		Vector camposDinamicoList = BasePersonDomain.getConfigPersonCadList(tableName);
    	int size = camposDinamicoList.size();
    	
    	for (int i = 0; i < size; i++) {
    		Campo campo = (Campo) camposDinamicoList.items[i];
    		if (!campo.isFormatoInscricaoEstadual()) continue;
    			
    		campo.setUfNaoPermiteInscEstadualVazio(informaUfNaoPermiteInscEstadualVazio);
    		break;
    	}
	}

}