package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.StatusNfe;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusNfeDao;

public class StatusNfeService extends CrudService {

	private static StatusNfeService instance;

    private StatusNfeService() {
        //--
    }

    public static StatusNfeService getInstance() {
        if (instance == null) {
            instance = new StatusNfeService();
        }
        return instance;
    }

	protected CrudDao getCrudDao() {
		return StatusNfeDao.getInstance();
	}

	public void validate(BaseDomain arg0) {
	}
	
	public boolean isStatusNfeBloqueiaImpressao(Nfe nfe) throws SQLException {
		StatusNfe statusNfe = new StatusNfe();
		statusNfe.cdEmpresa = nfe.cdEmpresa;
		statusNfe.cdRepresentante = nfe.cdRepresentante;
		statusNfe.cdStatusNfe = nfe.cdStatusNfe;
		String retornoNFe = ValueUtil.isNotEmpty(nfe.dsMensagemRetorno) ? "Mensagem retorno: "+nfe.dsMensagemRetorno : "" ;
		
		statusNfe = (StatusNfe) StatusNfeService.getInstance().findByPrimaryKey(statusNfe);
		if (statusNfe != null) {
			if (statusNfe.isBloqueiaImpressao()) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONFE_MSG_STATUS_NFE,
						new String[] { statusNfe.toString(), retornoNFe }));
			}
			return statusNfe.isBloqueiaImpressao();
		} else {
			return false;
		}
	}
	
}
