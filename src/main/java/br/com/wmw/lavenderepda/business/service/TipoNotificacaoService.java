package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoNotificacaoDbxDao;

public class TipoNotificacaoService extends CrudService {

	private static TipoNotificacaoService instance;

	private TipoNotificacaoService() {
		//--
	}

	public static TipoNotificacaoService getInstance() {
		if (instance == null) {
			instance = new TipoNotificacaoService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return TipoNotificacaoDbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) {
/*    
        TipoNotificacao tipoNotificacao = (TipoNotificacao) domain;

        //cdTipoNotificacao
        if (ValueUtil.isEmpty(tipoNotificacao.cdTipoNotificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_CDTIPONOTIFICACAO);
        }
        //dsTipoNotificacao
        if (ValueUtil.isEmpty(tipoNotificacao.dsTipoNotificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_DSTIPONOTIFICACAO);
        }
        //flAtivo
        if (ValueUtil.isEmpty(tipoNotificacao.flAtivo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_FLATIVO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(tipoNotificacao.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(tipoNotificacao.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(tipoNotificacao.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_CDUSUARIO);
        }
        //dtAlteracao
        if (ValueUtil.isEmpty(tipoNotificacao.dtAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_DTALTERACAO);
        }
        //hrAlteracao
        if (ValueUtil.isEmpty(tipoNotificacao.hrAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPONOTIFICACAO_LABEL_HRALTERACAO);
        }
*/
	}

}