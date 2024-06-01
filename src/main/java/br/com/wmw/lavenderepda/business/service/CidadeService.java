package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CidadeDbxDao;

public class CidadeService extends CrudService {

    private static CidadeService instance;
    
    private CidadeService() {
        //--
    }
    
    public static CidadeService getInstance() {
        if (instance == null) {
            instance = new CidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CidadeDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        Cidade cidade = (Cidade) domain;

        //cdCidade
        if (ValueUtil.isEmpty(cidade.cdCidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_CDCIDADE);
        }
        //nmCidade
        if (ValueUtil.isEmpty(cidade.nmCidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_NMCIDADE);
        }
        //cdUf
        if (ValueUtil.isEmpty(cidade.cdUf)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_CDUF);
        }
        //flCapital
        if (ValueUtil.isEmpty(cidade.flCapital)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_FLCAPITAL);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(cidade.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(cidade.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(cidade.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CIDADE_LABEL_CDUSUARIO);
        }
*/
    }
    
    public String getNmCidade(String cdCidade) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdCidade)) {
	    	Cidade cidade = getCidade(cdCidade);
	    	if (cidade != null && ValueUtil.isNotEmpty(cidade.nmCidade) &&  !"0".equals(cidade.nmCidade.trim())) {
	    		return cidade.nmCidade;
	    	}
    	}
    	return "";
    }
 
    public String getUfCidade(String cdCidade) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdCidade)) {
    		Cidade cidade = getCidade(cdCidade);
    		if (cidade != null && ValueUtil.isNotEmpty(cidade.cdUf) &&  !"0".equals(cidade.cdUf.trim())) {
    			return cidade.cdUf;
    		}
    	}
    	return "";
    }

	private Cidade getCidade(String cdCidade) throws SQLException {
		Cidade cidade = new Cidade();
		cidade.cdCidade = cdCidade;
		return (Cidade)CidadeService.getInstance().findByRowKey(cidade.getRowKey());
	}

}