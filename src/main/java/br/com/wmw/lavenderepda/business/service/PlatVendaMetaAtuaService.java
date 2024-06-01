package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaAtua;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlatVendaMetaAtuaDbxDao;

public class PlatVendaMetaAtuaService extends CrudService {

    private static PlatVendaMetaAtuaService instance;
    
    public static PlatVendaMetaAtuaService getInstance() {
        if (instance == null) {
            instance = new PlatVendaMetaAtuaService();
        }
        return instance;
    }
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return PlatVendaMetaAtuaDbxDao.getInstance();
	}

	public void insertOrUpdate(PlatVendaMetaAtua platVendaMetaAtua) throws SQLException {
		try {
			insert(platVendaMetaAtua);
		} catch (Throwable e) {
			update(platVendaMetaAtua);
		}
	}
	
	public void setPropertiesByAtuaIfExists(PlatVendaMeta platVendaMeta) throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = (PlatVendaMetaAtua) PlatVendaMetaAtuaService.getInstance().findByRowKey(platVendaMeta.getRowKey());
		if (platVendaMetaAtua != null) {
			platVendaMeta.vlMetaPlanejadaRep = platVendaMetaAtua.vlMetaPlanejadaRep;
			platVendaMeta.vlMetaPlanejadaSup = platVendaMetaAtua.vlMetaPlanejadaSup;
			platVendaMeta.flPlanejado = platVendaMetaAtua.flPlanejado;
			platVendaMeta.flEncerrado = platVendaMetaAtua.flEncerrado;
		}
	}
	
	public void deleteAllEnviadosServidor() throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = new PlatVendaMetaAtua();
		platVendaMetaAtua.flTipoAlteracao = PlatVendaMetaAtua.FLTIPOALTERACAO_ORIGINAL;
		deleteAllByExample(platVendaMetaAtua);
	}
	
}
