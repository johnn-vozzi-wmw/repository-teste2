package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.TipoPessoa;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProspectDbxDao;
import totalcross.util.Date;

public class ProspectService extends CrudPersonLavendereService {

    private static ProspectService instance;
    
    private ProspectService() {
        //--
    }
    
    public static ProspectService getInstance() {
        if (instance == null) {
            instance = new ProspectService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ProspectDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	super.validate(domain);
        Prospect prospect = (Prospect) domain;
        validateCampoDataProspect(prospect);
        //cdEmpresa
        if (ValueUtil.isEmpty(prospect.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.EMPRESA_NOME_ENTIDADE);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(prospect.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_NOME_ENTIDADE);
        }
        //flOrigemProspect
        if (ValueUtil.isEmpty(prospect.flOrigemProspect)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PROSPECT_LABEL_FLORIGEMPROSPECT);
        }
        //cdProspect
        if (ValueUtil.isEmpty(prospect.cdProspect)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PROSPECT_NOME_ENTIDADE);
        }
        //nuCnpj
        validateCpfCnpj(LavenderePdaConfig.ignoraSeparadoresProspectCpfCnpj ? prospect.nuCnpj : ValueUtil.getValidNumbers(prospect.nuCnpj), prospect.flTipoPessoa);
        if (ValueUtil.isEmpty(prospect.rowKey)) {
	        validateCpjCnpjJaExistente(prospect.nuCnpj);
        }
        //dtCadastro
        if (ValueUtil.isEmpty(prospect.dtCadastro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PROSPECT_DATA_CADASTRO);
        }
        //hrCadastro
        if (ValueUtil.isEmpty(prospect.hrCadastro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PROSPECT_HORA_CADASTRO);
        }
        //FotoProspect
        if (LavenderePdaConfig.obrigaTirarFotoProspect() && ValueUtil.isEmpty(prospect.fotoProspectList)) {
        	throw new ValidationException(Messages.PROSPECT_MSG_OBRIGATORIEDADE_FOTO);
        }
    }
    
    public void deleteRegistrosProspectsAntigos(Date limite) throws SQLException {
    	Prospect filter = new Prospect();
    	filter.dtCadastro = limite;
    	FotoProspectService.getInstance().deleteFotosByDtLimite(filter);
    	deleteAllByExample(filter);
    }
    
    private void validateCampoDataProspect(Prospect prospect) {
    	String nmCampo = LavenderePdaConfig.nmCampoValidaDataProspect;
    	if (ValueUtil.isNotEmpty(nmCampo) && !nmCampo.equals(ValueUtil.VALOR_NAO) && prospect.getHashValuesDinamicos().exists(nmCampo)) {
    		Date dtProspect = DateUtil.getDateValue(prospect.getHashValuesDinamicos().getString(nmCampo));
    		double intervalo = DateUtil.getYearsBetween(dtProspect, DateUtil.getCurrentDate());
    		if (intervalo < LavenderePdaConfig.nuAnoValidaDataProspect) {
    			throw new ValidationException(MessageUtil.getMessage(Messages.NOVO_CLIENTE_VALIDADE_DATA, LavenderePdaConfig.nuAnoValidaDataProspect));
    		}
    	}
    }
    
    private void validateCpfCnpj(String cnpj, String flTipoPessoa) {
    	if (ValueUtil.isEmpty(cnpj)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOVOCLIENTE_LABEL_NUCNPJ);
		} else if (!CpfCnpjValidator.validateCnpjCpf(cnpj)) {
			if (Messages.TIPOPESSOA_FLAG_JURIDICA.equals(flTipoPessoa)) {
				throw new ValidationException(Messages.NOVOCLIENTE_MSG_NUCNPJ_INVALIDO);
			} else {
				throw new ValidationException(Messages.PROSPECT_CPF_INVALIDO);
			}
		}
    }
    
    private void validateCpjCnpjJaExistente(String cnpj) throws SQLException {
    	if (ProspectDbxDao.getInstance().existeProspectCnpj(cnpj)) {
    		throw new ValidationException(Messages.PROSPECT_CPF_CNPJ_CADASTRADO);
    	}
    }
    
    @Override
    public void insert(BaseDomain domain) throws SQLException {
    	Prospect prospect = (Prospect) domain;
    	super.insert(domain);
    	FotoProspectService.getInstance().insereFotoProspectNoBanco(prospect);
    }
    
    @Override
    public void update(BaseDomain domain) throws SQLException {
    	Prospect prospect = (Prospect) domain;
    	super.update(domain);
    	FotoProspectService.getInstance().insereFotoProspectNoBanco(prospect);
    }

}