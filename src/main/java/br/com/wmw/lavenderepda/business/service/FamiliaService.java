package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Familia;
import br.com.wmw.lavenderepda.business.domain.MetaVendaTipo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FamiliaDbxDao;
import totalcross.util.Vector;

public class FamiliaService extends CrudService {

    private static FamiliaService instance;

    private FamiliaService() {
        //--
    }

    public static FamiliaService getInstance() {
        if (instance == null) {
            instance = new FamiliaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FamiliaDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        Familia familia = (Familia) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(familia.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_CDEMPRESA);
        }
        //cdFamilia
        if (ValueUtil.isEmpty(familia.cdFamilia)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_CDFAMILIA);
        }
        //dsFamilia
        if (ValueUtil.isEmpty(familia.dsFamilia)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_DSFAMILIA);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(familia.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(familia.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(familia.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FAMILIA_LABEL_CDUSUARIO);
        }
*/
    }

    public String getDsFamilia(String cdFamilia) throws SQLException {
    	Familia familia = new Familia();
    	familia.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	familia.cdFamilia = cdFamilia;
    	familia.dsFamilia = findColumnByRowKey(familia.getRowKey(), "DSFAMILIA");
    	return familia.toString();
    }

    public Vector findAllFamiliasByMetaVenda(String cdMetaVenda) throws SQLException {
    	Familia familia = new Familia();
		familia.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector familias = findAllByExample(familia);
		//--
		MetaVendaTipo metaVendaFamilia = new MetaVendaTipo();
		metaVendaFamilia.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metaVendaFamilia.cdMetaVenda = cdMetaVenda;
		Vector metasVendaFamilliaList = MetaVendaTipoService.getInstance().findAllByExample(metaVendaFamilia);
		//--
		Vector listFinal = new Vector();
		for (int i = 0; i < metasVendaFamilliaList.size(); i++) {
			MetaVendaTipo metaVendaFamilliaTemp = (MetaVendaTipo)metasVendaFamilliaList.items[i];
			Familia familliaTemp = new Familia();
			familliaTemp.cdEmpresa = metaVendaFamilliaTemp.cdEmpresa;
			familliaTemp.cdFamilia = metaVendaFamilliaTemp.cdFamilia;
			int index = familias.indexOf(familliaTemp);
			if (index != -1) {
				listFinal.addElement(familias.items[index]);
			}
		}
		return listFinal;

    }

}