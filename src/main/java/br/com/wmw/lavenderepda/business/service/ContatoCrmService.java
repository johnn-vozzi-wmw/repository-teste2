package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.EmailUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContatoCrmDao;
import totalcross.util.Vector;

public class ContatoCrmService extends CrudService {

    private static ContatoCrmService instance;
    
    private ContatoCrmService() {
        //--
    }
    
    public static ContatoCrmService getInstance() {
        if (instance == null) {
            instance = new ContatoCrmService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContatoCrmDao.getInstance();
    }
 
    //@Override
	
	public void validate(BaseDomain domain) throws SQLException {
		ContatoCrm contatoCrm = (ContatoCrm) domain;
		if (ValueUtil.isEmpty(contatoCrm.nmContato)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_LABEL_CONTATO);
		}
		if (!EmailUtil.validateEmail(contatoCrm.dsEmail) && ValueUtil.isNotEmpty(contatoCrm.dsEmail)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_EMAIL_INVALIDO, contatoCrm.dsEmail));
		}
		
		if (ValueUtil.isEmpty(contatoCrm.nuFone) && ValueUtil.isEmpty(contatoCrm.dsEmail)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.SAC_MSG_VALIDACAO_CAMPO_CONTATO, new String[] { Messages.SAC_LABEL_FONE_CONTATO, Messages.SAC_LABEL_EMAIL_CONTATO }));
			
		} else {
			if (!(ValueUtil.isNotEmpty(contatoCrm.nuFone) || ValueUtil.isNotEmpty(contatoCrm.dsEmail))) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, ValueUtil.isEmpty(contatoCrm.nuFone) ? Messages.SAC_LABEL_FONE_CONTATO : Messages.SAC_LABEL_EMAIL_CONTATO);
			}
		}
	}

    
    public ContatoCrm getContatoBySac(Sac sac) throws SQLException {
    	ContatoCrm contatoCrmFilter = new ContatoCrm();
    	contatoCrmFilter.cdEmpresa = sac.cdEmpresa;
    	contatoCrmFilter.cdRepresentante = sac.cdRepresentante;
    	contatoCrmFilter.cdCliente = sac.cdCliente;
    	contatoCrmFilter.cdContato = ValueUtil.isEmpty(sac.cdContato) ? "0" : sac.cdContato;
    	return (ContatoCrm) findByRowKey(contatoCrmFilter.getRowKey());
    
    }
}