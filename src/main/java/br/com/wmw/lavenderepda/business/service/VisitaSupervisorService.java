package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.VisitaSupervisor;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaSupervisorPdbxDao;

public class VisitaSupervisorService extends CrudService {

    private static VisitaSupervisorService instance;

    private VisitaSupervisorService() {
       super();
    }

    public static VisitaSupervisorService getInstance() {
        if (instance == null) {
            instance = new VisitaSupervisorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VisitaSupervisorPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(visita.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.EMPRESA_NOME_ENTIDADE);
        }
        //cdVisita
        if (ValueUtil.isEmpty(visita.cdVisita)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_NOME_ENTIDADE);
        }
        //cdSupervisor
        if (ValueUtil.isEmpty(visita.cdSupervisor)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SUPERVISOR_NOME_ENTIDADE);
        }
        //flOrigemVisita
        if (ValueUtil.isEmpty(visita.flOrigemVisita)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_FLORIGEMVISITA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(visita.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
        }
        //cdCliente
        if (ValueUtil.isEmpty(visita.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTE_NOME_ENTIDADE);
        }
        //dtVisita
        if (ValueUtil.isEmpty(visita.dtVisita)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.DATA_LABEL_DATA);
        }
        //hrVisita
        String dataLabelHora = Messages.DATA_LABEL_HORA;
        if (ValueUtil.isEmpty(visita.hrVisita)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, dataLabelHora);
        } else if (!TimeUtil.isValidTimeHHMM(visita.hrVisita)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_HORA_INVALIDA, dataLabelHora);
        }
        //qtTempoAtendimento
        if (ValueUtil.isNotEmpty(visita.qtTempoAtendimento) && !TimeUtil.isValidTimeHHMM(visita.qtTempoAtendimento)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_HORA_INVALIDA, Messages.VISITA_LABEL_QTTEMPOATENDIMENTO);
        }
        //dsObservacao
        if (ValueUtil.isEmpty(visita.dsObservacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.OBSERVACAO_LABEL);
        }
    }

    //@Override
    public void insert(BaseDomain domain) throws SQLException {
    	VisitaSupervisor visita = (VisitaSupervisor) domain;
    	visita.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	visita.cdVisita = generateIdGlobal();
    	visita.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	visita.flOrigemVisita = VisitaSupervisor.TIPOORIGEM_PALM;
    	super.insert(domain);
    }

}