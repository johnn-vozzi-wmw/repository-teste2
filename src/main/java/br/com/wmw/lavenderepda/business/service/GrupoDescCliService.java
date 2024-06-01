package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.GrupoDescCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoDescCliDbxDao;

public class GrupoDescCliService extends CrudService {

    private static GrupoDescCliService instance;
    
    private GrupoDescCliService() {}

	@Override
	public void validate(BaseDomain domain) throws SQLException {}
    
    public static GrupoDescCliService getInstance() {
    	return (instance == null) ? instance = new GrupoDescCliService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return GrupoDescCliDbxDao.getInstance();
    }

    public String getDsGrupoCliente(final String cdEmpresa, final String cdRepresentante, final String cdCliente, final String cdGrupoCliente) throws SQLException {
		GrupoDescCli grupoDescCli = new GrupoDescCli();
		grupoDescCli.cdEmpresa = cdEmpresa;
		grupoDescCli.cdRepresentante = cdRepresentante;
		grupoDescCli.cdCliente = cdCliente;
		grupoDescCli.cdGrupoDescCli = cdGrupoCliente;
		grupoDescCli.dsGrupoDescCli = findColumnByRowKey(grupoDescCli.getRowKey(), "DSGRUPODESCCLI");
   		return grupoDescCli.toString();
    }

}