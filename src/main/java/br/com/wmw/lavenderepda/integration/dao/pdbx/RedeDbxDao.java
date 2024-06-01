package br.com.wmw.lavenderepda.integration.dao.pdbx;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Rede;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RedeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Rede();
	}

    private static RedeDbxDao instance;
	

    public RedeDbxDao() {
        super(Rede.TABLE_NAME);
    }

    public static RedeDbxDao getInstance() {
        if (instance == null) {
            instance = new RedeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Rede rede = new Rede();
        rede.rowKey = rs.getString("rowkey");
        rede.cdEmpresa = rs.getString("cdEmpresa");
        rede.cdRepresentante = rs.getString("cdRepresentante");
        rede.cdRede = rs.getString("cdRede");
        rede.dsRede = rs.getString("dsRede");
        rede.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        rede.vlLimiteCredito = ValueUtil.round(rs.getDouble("vlLimiteCredito"));
        rede.nuCarimbo = rs.getInt("nuCarimbo");
        rede.flTipoAlteracao = rs.getString("flTipoAlteracao");
        rede.cdUsuario = rs.getString("cdUsuario");
        return rede;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREDE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DSREDE,");
        sql.append(" VLSALDO,");
        sql.append(" VLLIMITECREDITO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDREDE,");
        sql.append(" DSREDE,");
        sql.append(" VLSALDO,");
        sql.append(" VLLIMITECREDITO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Rede rede = (Rede) domain;
        sql.append(Sql.getValue(rede.cdEmpresa)).append(",");
        sql.append(Sql.getValue(rede.cdRepresentante)).append(",");
        sql.append(Sql.getValue(rede.cdRede)).append(",");
        sql.append(Sql.getValue(rede.dsRede)).append(",");
        sql.append(Sql.getValue(rede.vlSaldo)).append(",");
        sql.append(Sql.getValue(rede.vlLimiteCredito)).append(",");
        sql.append(Sql.getValue(rede.nuCarimbo)).append(",");
        sql.append(Sql.getValue(rede.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(rede.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Rede rede = (Rede) domain;
        sql.append(" DSREDE = ").append(Sql.getValue(rede.dsRede)).append(",");
        sql.append(" VLSALDO = ").append(Sql.getValue(rede.vlSaldo)).append(",");
        sql.append(" VLLIMITECREDITO = ").append(Sql.getValue(rede.vlLimiteCredito)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(rede.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(rede.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(rede.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Rede rede = (Rede) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", rede.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", rede.cdRepresentante);
		sqlWhereClause.addAndCondition("CDREDE = ", rede.cdRede);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllRedeComboByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select ");
    	addSelectColumns(domain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(domain, sql);
    	sql.append("GROUP BY CDREDE, DSREDE");
    	addOrderBy(sql, domain);
    	addLimit(sql, domain);
    	return findAll(domain, sql.toString());

    }
}