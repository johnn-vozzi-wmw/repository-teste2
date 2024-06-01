package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class ConexaoPdaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConexaoPda();
	}

    private static ConexaoPdaPdbxDao instance;

    public ConexaoPdaPdbxDao() {
        super(ConexaoPda.TABLE_NAME);
    }

    public static ConexaoPdaPdbxDao getInstance() {
        if (instance == null) {
            instance = new ConexaoPdaPdbxDao();
        }
        return instance;
    }

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" cdConexao,");
        sql.append(" dsConexao,");
        sql.append(" dsUrlWebService,");
        sql.append(" flConexaoDiscada,");
        sql.append(" flDefault,");
        sql.append(" flTipoAlteracao,");
        sql.append(" flConexaoSecundaria");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ConexaoPda conexaoPda = new ConexaoPda();
        conexaoPda.rowKey = rs.getString("rowkey");
        conexaoPda.cdConexao = rs.getString("cdConexao");
        conexaoPda.dsConexao = rs.getString("dsConexao");
        conexaoPda.dsUrlWebService = rs.getString("dsUrlWebService");
        conexaoPda.flConexaoDiscada = rs.getString("flConexaoDiscada");
        conexaoPda.flDefault = rs.getString("flDefault");
        conexaoPda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        conexaoPda.flConexaoSecundaria = rs.getString("flConexaoSecundaria");
        return conexaoPda;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" cdConexao,");
        sql.append(" dsConexao,");
        sql.append(" dsUrlWebService,");
        sql.append(" flConexaoDiscada,");
        sql.append(" flDefault,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao");

    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConexaoPda conexaoPda = (ConexaoPda) domain;
        sql.append(Sql.getValue(conexaoPda.cdConexao)).append(",");
        sql.append(Sql.getValue(conexaoPda.dsConexao)).append(",");
        sql.append(Sql.getValue(conexaoPda.dsUrlWebService)).append(",");
        sql.append(Sql.getValue(conexaoPda.flConexaoDiscada)).append(",");
        sql.append(Sql.getValue(conexaoPda.flDefault)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(conexaoPda.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConexaoPda conexaoPda = (ConexaoPda) domain;
        sql.append(" dsConexao = ").append(Sql.getValue(conexaoPda.dsConexao)).append(",");
        sql.append(" dsUrlWebService = ").append(Sql.getValue(conexaoPda.dsUrlWebService)).append(",");
        sql.append(" flConexaoDiscada = ").append(Sql.getValue(conexaoPda.flConexaoDiscada)).append(",");
        sql.append(" flDefault = ").append(Sql.getValue(conexaoPda.flDefault)).append(",");
        sql.append(" flTipoAlteracao = ").append(Sql.getValue(conexaoPda.flTipoAlteracao));

    }

    public void cleanDefault() throws SQLException {
    	StringBuffer strBuff = getSqlBuffer();
    	strBuff.append("update ").append(tableName).append(" set FLDEFAULT = ").append(Sql.getValue(ValueUtil.VALOR_NAO));
    	updateAll(strBuff.toString());
    }
    
    public void updateAllConections(String sql) throws SQLException {
    	if (ValueUtil.isEmpty(sql)) return;
    	updateAll(sql);
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ConexaoPda conexaopda = (ConexaoPda) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("flDefault = ", conexaopda.flDefault);
       	sqlWhereClause.addAndCondition("flConexaoSecundaria = ", conexaopda.flConexaoSecundaria);
        sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findConexaoPdaSorted() throws SQLException {
    	return findAllByExample(getBaseDomainDefault(), getOrderByConexaoPda());
    }
    
    public List<HttpConnection> findConexaoPdaSortedAsList() throws SQLException {
    	Vector findConexaoPdaSorted = findConexaoPdaSorted();
    	List<HttpConnection> list = new ArrayList<>();
    	for (Object object : findConexaoPdaSorted.items) {
    		if (object != null) {
    			list.add((ConexaoPda)object);
    		}
		}
    	return list;
    }

	private String getOrderByConexaoPda() {
		StringBuffer sql = getSqlBuffer();
		sql.append(" IFNULL(FLDEFAULT, 'N') DESC, ")
		.append("IFNULL(FLCONEXAOSECUNDARIA, 'N') DESC");
		return sql.toString();
	}
	
}