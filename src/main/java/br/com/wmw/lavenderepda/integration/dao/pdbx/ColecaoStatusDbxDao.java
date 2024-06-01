package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ColecaoStatus;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ColecaoStatusDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ColecaoStatus();
	}

    private static ColecaoStatusDbxDao instance;

    public ColecaoStatusDbxDao() {
        super(ColecaoStatus.TABLE_NAME); 
    }
    
    public static ColecaoStatusDbxDao getInstance() {
        if (instance == null) {
            instance = new ColecaoStatusDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ColecaoStatus colecaoStatus = new ColecaoStatus();
        colecaoStatus.rowKey = rs.getString("rowkey");
        colecaoStatus.cdEmpresa = rs.getString("cdEmpresa");
        colecaoStatus.cdStatusColecao = rs.getString("cdStatusColecao");
        colecaoStatus.dsStatusColecao = rs.getString("dsStatusColecao");
        colecaoStatus.flTipoAlteracao = rs.getString("flTipoAlteracao");
        colecaoStatus.nuCarimbo = rs.getInt("nuCarimbo");
        colecaoStatus.cdUsuario = rs.getString("cdUsuario");
        return colecaoStatus;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSTATUSCOLECAO,");
        sql.append(" DSSTATUSCOLECAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSTATUSCOLECAO,");
        sql.append(" DSSTATUSCOLECAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ColecaoStatus colecaoStatus = (ColecaoStatus) domain;
        sql.append(Sql.getValue(colecaoStatus.cdEmpresa)).append(",");
        sql.append(Sql.getValue(colecaoStatus.cdStatusColecao)).append(",");
        sql.append(Sql.getValue(colecaoStatus.dsStatusColecao)).append(",");
        sql.append(Sql.getValue(colecaoStatus.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(colecaoStatus.nuCarimbo)).append(",");
        sql.append(Sql.getValue(colecaoStatus.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ColecaoStatus colecaoStatus = (ColecaoStatus) domain;
        sql.append(" DSSTATUSCOLECAO = ").append(Sql.getValue(colecaoStatus.dsStatusColecao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(colecaoStatus.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(colecaoStatus.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(colecaoStatus.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ColecaoStatus colecaoStatus = (ColecaoStatus) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", colecaoStatus.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSTATUSCOLECAO = ", colecaoStatus.cdStatusColecao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllColecaoStatus(ColecaoStatus colecaoStatusFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		sql.append("cdStatusColecao, dsStatusColecao ");
		sql.append("FROM ");
		sql.append(ColecaoStatus.TABLE_NAME);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" cdEmpresa = ", colecaoStatusFilter.cdEmpresa);
		//sqlWhereClause.addAndCondition(" cdUsuario = ", colecaoStatusFilter.cdUsuario);
		sql.append(sqlWhereClause.getSql());
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateDsStatusColecao(rs));
			}
		}
		return list;
    }
    
    protected ColecaoStatus populateDsStatusColecao(ResultSet rs)  throws SQLException {
        ColecaoStatus colecaoStatus = new ColecaoStatus();
        colecaoStatus.cdStatusColecao = rs.getString("cdStatusColecao");
        colecaoStatus.dsStatusColecao = rs.getString("dsStatusColecao");
        return colecaoStatus;
    }
}