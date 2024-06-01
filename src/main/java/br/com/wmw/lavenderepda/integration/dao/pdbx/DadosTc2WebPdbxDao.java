package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DadosTc2Web;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class DadosTc2WebPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return  null;
	}

	private static DadosTc2WebPdbxDao instance;


	public DadosTc2WebPdbxDao() {
		super("");
	}


    public static DadosTc2WebPdbxDao getInstance() {
        if (instance == null) {
            instance = new DadosTc2WebPdbxDao();
        }
        return instance;
    }


    public void updateDadosTc2WebEnviadosServidor(String tableName, Vector listRowkeys) throws SQLException {
    	int size = listRowkeys.size();
    	for (int i = 0; i < size; i++) {
	    	StringBuilder sql = new StringBuilder();
	        sql.append("update ");
	        sql.append(tableName);
	        sql.append(" set flTipoAlteracao = ").append(Sql.getValue(DadosTc2Web.FLTIPOALTERACAO_ORIGINAL));
	        if (VisitaFotoDao.TABLE_NAME.equals(tableName)) {
	        	sql.append(", flEnviadoServidor = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
	        }
	        sql.append(" where rowKey = ").append(Sql.getValue((String) listRowkeys.items[i]));
	        executeUpdate(sql.toString());
    	}
    }


//	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}


//	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
	}


//	@Override
	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
	}


//	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}


//	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}


//	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}

}
