package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MetaVenda;
import br.com.wmw.lavenderepda.business.domain.MetaVendaRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaVendaDbxDao;
import totalcross.util.Vector;

public class MetaVendaService extends CrudService {

    private static MetaVendaService instance;

    private MetaVendaService() {
        //--
    }

    public static MetaVendaService getInstance() {
        if (instance == null) {
            instance = new MetaVendaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaVendaDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        MetaVenda metaVenda = (MetaVenda) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(metaVenda.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_CDEMPRESA);
        }
        //cdMetaVenda
        if (ValueUtil.isEmpty(metaVenda.cdMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_CDMETAVENDA);
        }
        //dsMetaVenda
        if (ValueUtil.isEmpty(metaVenda.dsMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_DSMETAVENDA);
        }
        //cdTipoMetaVenda
        if (ValueUtil.isEmpty(metaVenda.cdTipoMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_CDTIPOMETAVENDA);
        }
        //cdVariavelMetaVenda
        if (ValueUtil.isEmpty(metaVenda.cdVariavelMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_CDVARIAVELMETAVENDA);
        }
        //dtInicialVigencia
        if (ValueUtil.isEmpty(metaVenda.dtInicialVigencia)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_DTINICIALVIGENCIA);
        }
        //dtFinalVigencia
        if (ValueUtil.isEmpty(metaVenda.dtFinalVigencia)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_DTFINALVIGENCIA);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(metaVenda.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(metaVenda.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(metaVenda.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDA_LABEL_CDUSUARIO);
        }
*/
    }

    public String getDsVarivavelMeta(MetaVenda metaVenda) {
    	if (MetaVenda.CDVARIAVELMETA_TONELADAS.equals(metaVenda.cdVariavelMetaVenda)) {
    		return Messages.META_VENDA_TONELADAS;
    	} else if (MetaVenda.CDVARIAVELMETA_VALOR.equals(metaVenda.cdVariavelMetaVenda)) {
    		return Messages.META_VENDA_VALOR;
    	}
    	return Messages.META_VENDA_VARIAVEL;
    }

    public Vector findAllMetasVigentesRepresentante(String cdRepresentante) throws SQLException {
    	MetaVenda metaVenda = new MetaVenda();
		metaVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metaVenda.dtInicialVigencia = DateUtil.getCurrentDate();
		metaVenda.dtFinalVigencia = DateUtil.getCurrentDate();
		Vector metasVigentes = findAllByExample(metaVenda);
		//--
		MetaVendaRep metaVendaRep = new MetaVendaRep();
		metaVendaRep.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			metaVendaRep.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		} else {
			metaVendaRep.cdRepresentante = cdRepresentante;
		}
//		metaVendaRep.cdTipoMetaVenda = MetaVenda.CDTIPOMETA_FAMILIA_PRODUTOS;
		Vector metasVendaRepList = MetaVendaRepService.getInstance().findAllByExample(metaVendaRep);
		//--
		Vector listFinal = new Vector();
		for (int i = 0; i < metasVendaRepList.size(); i++) {
			MetaVendaRep metaVendaRepTemp = (MetaVendaRep)metasVendaRepList.items[i];
			MetaVenda metaVendaTemp = new MetaVenda();
			metaVendaTemp.cdEmpresa = metaVendaRepTemp.cdEmpresa;
			metaVendaTemp.cdMetaVenda = metaVendaRepTemp.cdMetaVenda;
			int index = metasVigentes.indexOf(metaVendaTemp);
			if (index != -1) {
				if (listFinal.indexOf(metasVigentes.items[index]) == -1) {
					listFinal.addElement(metasVigentes.items[index]);
				}
			}
		}
		return listFinal;
    }

}