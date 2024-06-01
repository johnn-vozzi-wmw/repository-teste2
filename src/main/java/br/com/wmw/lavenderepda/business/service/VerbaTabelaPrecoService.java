package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.VerbaTabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaTabelaPrecoPdbxDao;
import totalcross.util.Vector;

public class VerbaTabelaPrecoService extends CrudService {

    private static VerbaTabelaPrecoService instance;

    private VerbaTabelaPrecoService() {
        //--
    }

    public static VerbaTabelaPrecoService getInstance() {
        if (instance == null) {
            instance = new VerbaTabelaPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaTabelaPrecoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getVerbaTabelaPrecoList( String cdTabelaPreco, String cdEmpresa, String cdRepresentante) throws SQLException {
    	VerbaTabelaPreco verbaTabelaPrecoFilter = new VerbaTabelaPreco();
    	verbaTabelaPrecoFilter.cdEmpresa = cdEmpresa;
    	verbaTabelaPrecoFilter.cdRepresentante = cdRepresentante;
    	verbaTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
    	return VerbaTabelaPrecoService.getInstance().findAllByExample(verbaTabelaPrecoFilter);
    }

    public VerbaTabelaPreco getVerbaTabelaPreco( String cdTabelaPreco, String cdEmpresa, String cdRepresentante, String cdVerba) throws SQLException {
    	VerbaTabelaPreco verbaTabelaPrecoFilter = new VerbaTabelaPreco();
    	verbaTabelaPrecoFilter.cdEmpresa = cdEmpresa;
    	verbaTabelaPrecoFilter.cdRepresentante = cdRepresentante;
    	verbaTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
    	verbaTabelaPrecoFilter.cdVerba = cdVerba;
    	return (VerbaTabelaPreco)VerbaTabelaPrecoService.getInstance().findByRowKey(verbaTabelaPrecoFilter.getRowKey());
    }

}