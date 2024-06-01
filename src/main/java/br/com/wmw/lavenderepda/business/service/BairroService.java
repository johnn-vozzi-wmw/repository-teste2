package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Bairro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BairroDbxDao;

public class BairroService extends CrudService {

    private static BairroService instance;
    
    private BairroService() {
        //--
    }
    
    public static BairroService getInstance() {
        if (instance == null) {
            instance = new BairroService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return BairroDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        Bairro bairro = (Bairro) domain;

        //cdBairro
        if (ValueUtil.isEmpty(bairro.cdBairro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.BAIRRO_LABEL_CDBAIRRO);
        }
        //dsBairro
        if (ValueUtil.isEmpty(bairro.dsBairro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.BAIRRO_LABEL_DSBAIRRO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(bairro.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.BAIRRO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(bairro.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.BAIRRO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(bairro.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.BAIRRO_LABEL_CDUSUARIO);
        }
*/
    }
    
    public String getDsBairro(String cdBairro) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdBairro)) {
        	Bairro bairro = new Bairro();
    		bairro.cdBairro = cdBairro;
    		bairro = (Bairro) BairroService.getInstance().findByRowKey(bairro.getRowKey());
    		if (bairro != null && ValueUtil.isNotEmpty(bairro.dsBairro) &&  !"0".equals(bairro.dsBairro.trim())) {
    			return bairro.dsBairro;
    		}
    	}
    	return "";
    }

}