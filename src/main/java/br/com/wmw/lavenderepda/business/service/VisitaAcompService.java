package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaAcompDbxDao;
import totalcross.util.Vector;

public class VisitaAcompService extends CrudService {

    private static VisitaAcompService instance;

    private VisitaAcompService() {
        //--
    }

    public static VisitaAcompService getInstance() {
        if (instance == null) {
            instance = new VisitaAcompService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VisitaAcompDbxDao.getInstance();
    }

    public double[] sumVisitasPositivasAndNegativasDoMes(String cdRepresentante, String cdEmpresa) throws SQLException {
    	return VisitaAcompDbxDao.getInstance().sumVisitasPositivasAndNegativasDoMes(cdRepresentante, cdEmpresa);
    }

    public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
    	Vector list = super.findAllByExample(domain);
    	return list;
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        VisitaAcomp visitaAcomp = (VisitaAcomp) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(visitaAcomp.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(visitaAcomp.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_CDREPRESENTANTE);
        }
        //dtRegistro
        if (ValueUtil.isEmpty(visitaAcomp.dtRegistro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_DTREGISTRO);
        }
        //qtVisitasPositivas
        if (ValueUtil.isEmpty(visitaAcomp.qtVisitasPositivas)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_QTVISITASPOSITIVAS);
        }
        //qtVisitasNegativas
        if (ValueUtil.isEmpty(visitaAcomp.qtVisitasNegativas)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_QTVISITASNEGATIVAS);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(visitaAcomp.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(visitaAcomp.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(visitaAcomp.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITAACOMP_LABEL_CDUSUARIO);
        }
*/
    }

}