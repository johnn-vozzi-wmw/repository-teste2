package br.com.wmw.lavenderepda.integration.dao.pdbx;



import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.NotaCredito;
import totalcross.sql.ResultSet;

public class NotaCreditoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NotaCredito();
	}

    private static NotaCreditoDao instance = null;

    public NotaCreditoDao() {
        super(NotaCredito.TABLE_NAME);
    }
    
    public static NotaCreditoDao getInstance() {
        if (instance == null) {
            instance = new NotaCreditoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        NotaCredito notaCredito = new NotaCredito();
        notaCredito.rowKey = rs.getString("rowkey");
        notaCredito.cdEmpresa = rs.getString("cdEmpresa");
        notaCredito.cdRepresentante = rs.getString("cdRepresentante");
        notaCredito.cdNotaCredito = rs.getString("cdNotaCredito");
        notaCredito.cdCliente = rs.getString("cdCliente");
        notaCredito.vlNotaCredito = ValueUtil.round(rs.getDouble("vlNotaCredito"));
        notaCredito.flUtilizada = rs.getString("flUtilizada");
        notaCredito.cdUsuario = rs.getString("cdUsuario");
        notaCredito.nuCarimbo = rs.getInt("nuCarimbo");
        notaCredito.flTipoAlteracao = rs.getString("flTipoAlteracao");
        notaCredito.dtAlteracao = rs.getDate("dtAlteracao");
        notaCredito.hrAlteracao = rs.getString("hrAlteracao");
        return notaCredito;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDNOTACREDITO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLNOTACREDITO,");
        sql.append(" FLUTILIZADA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        NotaCredito notaCredito = (NotaCredito) domain;
        sql.append(" FLUTILIZADA = ").append(Sql.getValue(notaCredito.flUtilizada)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(notaCredito.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(notaCredito.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(notaCredito.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(notaCredito.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        NotaCredito notaCredito = (NotaCredito) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		setParametros(notaCredito, sqlWhereClause);
		sqlWhereClause.addAndCondition("(FLUTILIZADA = 'N' OR FLUTILIZADA IS NULL OR FLUTILIZADA = '')");
		sql.append(sqlWhereClause.getSql());
    }
    
	public void atualizaNotaCreditoUtilizada(NotaCredito notaCredito) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" UPDATE ");
        sql.append(tableName);
        sql.append(" SET FLUTILIZADA = ").append(Sql.getValue(notaCredito.flUtilizada));
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        setParametros(notaCredito, sqlWhereClause);
        sqlWhereClause.addAndConditionIn("CDNOTACREDITO" ,  notaCredito.cdNotaCreditoFilterToArray());
        sql.append(sqlWhereClause.getSql());
        executeUpdate(sql.toString());
	}
	
	private void setParametros(NotaCredito notaCredito, SqlWhereClause sqlWhereClause) {
		sqlWhereClause.addAndCondition("CDEMPRESA = ", notaCredito.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", notaCredito.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", notaCredito.cdCliente);
	}
	
}