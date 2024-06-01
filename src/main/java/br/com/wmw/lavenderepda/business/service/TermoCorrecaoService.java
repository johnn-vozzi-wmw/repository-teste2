package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TermoCorrecao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TermoCorrecaoDbxDao;
import totalcross.util.Vector;

public class TermoCorrecaoService extends CrudService {

    private static TermoCorrecaoService instance;
    
    private TermoCorrecaoService() {
        //--
    }
    
    public static TermoCorrecaoService getInstance() {
        if (instance == null) {
            instance = new TermoCorrecaoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TermoCorrecaoDbxDao.getInstance();
    }
    
	public String getDsTermoCorrigido(String dsFiltro) throws SQLException {
		if (LavenderePdaConfig.usaDicionarioCorrecaoNoFiltroDeProduto && ValueUtil.isNotEmpty(dsFiltro)) {
    		String termoCorrigido = "";
    		String[] termos = dsFiltro.split(" ");
    		if (termos == null) return dsFiltro;
    		for (String termo : termos) {
    			TermoCorrecao termoCorrecaoFilter = new TermoCorrecao();
    			termoCorrecaoFilter.dsTermo = termo;
    			Vector termoCorrecaoList = TermoCorrecaoService.getInstance().findAllByExample(termoCorrecaoFilter);
    			if (termoCorrecaoList != null && termoCorrecaoList.size() > 0) {
    				termoCorrigido += ((TermoCorrecao) termoCorrecaoList.items[0]).dsTermocorrigido + " ";
    			} else {
    				termoCorrigido += termo + " ";
    			}
    		}
			return termoCorrigido.trim();
		}
		return dsFiltro;
	}
    
    @Override
    public void validate(BaseDomain domain) {
/*    
        Termocorrecao termocorrecao = (Termocorrecao) domain;

        dsTermo
        if (ValueUtil.isEmpty(termocorrecao.dsTermo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_DSTERMO);
        }
        dsTermocorrigido
        if (ValueUtil.isEmpty(termocorrecao.dsTermocorrigido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_DSTERMOCORRIGIDO);
        }
        cdUsuario
        if (ValueUtil.isEmpty(termocorrecao.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_CDUSUARIO);
        }
        nuCarimbo
        if (ValueUtil.isEmpty(termocorrecao.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_NUCARIMBO);
        }
        flTipoAlteracao
        if (ValueUtil.isEmpty(termocorrecao.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_FLTIPOALTERACAO);
        }
        dtAlteracao
        if (ValueUtil.isEmpty(termocorrecao.dtAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_DTALTERACAO);
        }
        hrAlteracao
        if (ValueUtil.isEmpty(termocorrecao.hrAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TERMOCORRECAO_LABEL_HRALTERACAO);
        }
*/
    }

}