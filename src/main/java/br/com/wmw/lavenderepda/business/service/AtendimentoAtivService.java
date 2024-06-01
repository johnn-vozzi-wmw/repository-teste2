package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AtendimentoAtivDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class AtendimentoAtivService extends CrudService {

    private static AtendimentoAtivService instance;
    
    private AtendimentoAtivService() {
        //--
    }
    
    public static AtendimentoAtivService getInstance() {
        if (instance == null) {
            instance = new AtendimentoAtivService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AtendimentoAtivDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
		AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) domain;

    	if (ValueUtil.isEmpty(atendimentoAtiv.dsObservacao)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SAC_DETALHES_ATENDIMENTO_TITULO);
    	}
    }

    public Date getDataAtendimento(Sac sac) throws SQLException {
    	Date dataBase = new Date();
    	AtendimentoAtiv atendimentoAtivFilter = new AtendimentoAtiv();
    	atendimentoAtivFilter.cdEmpresa = sac.cdEmpresa;
    	atendimentoAtivFilter.cdRepresentante = sac.cdRepresentante;
    	atendimentoAtivFilter.cdCliente = sac.cdCliente;
    	atendimentoAtivFilter.cdSac = sac.cdSac;
    	Vector atendimentoAtivList = AtendimentoAtivService.getInstance().findAllByExample(atendimentoAtivFilter);
    	if (ValueUtil.isNotEmpty(atendimentoAtivList) && atendimentoAtivList.size() > 0) {
    		atendimentoAtivFilter = (AtendimentoAtiv) atendimentoAtivList.items[atendimentoAtivList.size() - 1];
    		dataBase = atendimentoAtivFilter.dtAtendimento;
    	} else {
    		dataBase = sac.dtCadastro;
    	}
    	return dataBase;
    }
    

	public boolean possuiAtendimentoAberto(Sac sac) throws SQLException {
		AtendimentoAtiv atendimentoAtivFilter = new AtendimentoAtiv();
    	atendimentoAtivFilter.cdEmpresa = sac.cdEmpresa;
    	atendimentoAtivFilter.cdRepresentante = sac.cdRepresentante;
    	atendimentoAtivFilter.cdCliente = sac.cdCliente;
    	atendimentoAtivFilter.cdSac = sac.cdSac;
    	atendimentoAtivFilter.cdStatusAtendimento = AtendimentoAtiv.CDSTATUSATENDIMENTO_EM_ANDAMENTO;
        return  AtendimentoAtivService.getInstance().findAllByExample(atendimentoAtivFilter).size() > 0;
	}
    
	public boolean possuiAtendimentoAtiv(Sac sac) throws SQLException {
		AtendimentoAtiv atendimentoAtivFilter = new AtendimentoAtiv();
		atendimentoAtivFilter.cdEmpresa = sac.cdEmpresa;
		atendimentoAtivFilter.cdRepresentante = sac.cdRepresentante;
		atendimentoAtivFilter.cdCliente = sac.cdCliente;
		atendimentoAtivFilter.cdSac = sac.cdSac;
		return  AtendimentoAtivService.getInstance().findAllByExample(atendimentoAtivFilter).size() > 0;
	}
	
}