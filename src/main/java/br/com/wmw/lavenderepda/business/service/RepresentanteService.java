package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RepresentantePdbxDao;
import totalcross.util.Vector;

public class RepresentanteService extends CrudService {

    private static RepresentanteService instance;

    private RepresentanteService() {
        //--
    }

    public static RepresentanteService getInstance() {
        if (instance == null) {
            instance = new RepresentanteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RepresentantePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDescription(String cdRepresentante) throws SQLException {
    	String result = StringUtil.getStringValue(cdRepresentante);
    	if (!ValueUtil.isEmpty(cdRepresentante)) {
    		Representante representanteFilter = new Representante();
    		representanteFilter.cdRepresentante = cdRepresentante;
    		Representante representante = (Representante)RepresentanteService.getInstance().findByRowKeyInCache(representanteFilter);
    		if (representante != null) {
    			result = StringUtil.getStringValue(representante.nmRepresentante);
    		}
    	}
    	return result;
    }

    public String getDescriptionWithId(String cdRepresentante) throws SQLException {
    	String result = StringUtil.getStringValue(cdRepresentante);
    	if (!ValueUtil.isEmpty(cdRepresentante)) {
    		Representante representanteFilter = new Representante();
    		representanteFilter.cdRepresentante = cdRepresentante;
    		Representante representante = (Representante)RepresentanteService.getInstance().findByRowKeyInCache(representanteFilter);
    		if (representante != null) {
    			result = representante.toString();
    		}
    	}
    	return result;
    }

    public Representante getRepresentanteById(String cdRepresentante) throws SQLException {
    	if (!ValueUtil.isEmpty(cdRepresentante)) {
    		Representante representanteFilter = new Representante();
    		representanteFilter.cdRepresentante = cdRepresentante;
    		Representante representante = (Representante)RepresentanteService.getInstance().findByRowKeyInCache(representanteFilter);
    		if (representante != null) {
    			return representante;
    		}
    	}
    	return null;
    }

	public Vector getRepresentanteListByCdEmpresa(String cdEmpresa) throws SQLException {
		Vector repEmpList = new Vector(0);
		RepresentanteEmp repEmp = new RepresentanteEmp();
		repEmp.cdEmpresa = cdEmpresa;
		repEmpList = RepresentanteEmpService.getInstance().findAllByExampleJoinUsuarioPdaRep(repEmp);
		//--
		Vector repist = new Vector(0);
		if (ValueUtil.isNotEmpty(repEmpList)) {
			int size = repEmpList.size();
			for (int i = 0; i < size; i++) {
				RepresentanteEmp repEmpFilter = (RepresentanteEmp) repEmpList.items[i];
				Representante rep = RepresentanteService.getInstance().getRepresentanteById(repEmpFilter.cdRepresentante);
				if (rep != null) {
					rep.flDefault = repEmpFilter.flDefault;
					repist.addElement(rep);
				}
			}
		}
		return repist;
	}

    /**
     * Recarrega o representante na sessão
    * @throws SQLException 
     */
    public void updateRepresentanteInSession() throws SQLException{
    	Representante rep = (Representante) RepresentanteService.getInstance().findByRowKey(SessionLavenderePda.usuarioPdaRep.representante.getRowKey());
    	if(rep != null){
    		SessionLavenderePda.usuarioPdaRep.representante = rep;
    	}
    	if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
		    double vlIndiceFinanceiro = ValueUtil.getDoubleSimpleValue(RepresentanteService.getInstance().findColumnByRowKey(SessionLavenderePda.getRepresentante().getRowKey(), Representante.NMCOLUNA_VLINDICEFINANCEIRO));
		    SessionLavenderePda.getRepresentante().vlIndiceFinanceiro = vlIndiceFinanceiro;
	    }
    }
}