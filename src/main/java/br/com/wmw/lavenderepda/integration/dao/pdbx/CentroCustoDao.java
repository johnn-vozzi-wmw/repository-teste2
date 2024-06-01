package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class CentroCustoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CentroCusto();
	}

	private static CentroCustoDao instance;

    public CentroCustoDao() {
        super(CentroCusto.TABLE_NAME);
    }

    public static CentroCustoDao getInstance() {
        if (instance == null) {
            instance = new CentroCustoDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.rowKey = rs.getString("rowkey");
        centroCusto.cdEmpresa = rs.getString("cdEmpresa");
        centroCusto.cdRepresentante = rs.getString("cdRepresentante");
        centroCusto.cdCentroCusto = rs.getString("cdCentroCusto");
        centroCusto.dsCentroCusto = rs.getString("dsCentroCusto");
        centroCusto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        centroCusto.nuCarimbo = rs.getInt("nuCarimbo");
        centroCusto.cdUsuario = rs.getString("cdUsuario");
        centroCusto.flOcultaEstListProd = rs.getString("flOcultaEstListProd");
		if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto()) {
			centroCusto.cdLocalEstoque = rs.getString("cdLocalEstoque");
		}
        return centroCusto;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCENTROCUSTO,");
        sql.append(" tb.DSCENTROCUSTO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
		if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto()) {
			sql.append(" tb.CDLOCALESTOQUE,");
		}
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLOCULTAESTLISTPROD");
    }

	//@Override
	protected void addInsertColumns(StringBuffer arg0) {
	}

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		CentroCusto centroCusto = (CentroCusto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", centroCusto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", centroCusto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", centroCusto.cdCentroCusto);
		sqlWhereClause.addAndCondition("tb.FLOCULTAESTLISTPROD = ", centroCusto.flOcultaEstListProd);
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findAllDistinctByExample(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select distinct ");
    	addSelectColumns(domain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addJoin(domain, sql);
    	addWhereByExample(domain, sql);
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		sql.append(" GROUP BY tb.CDCENTROCUSTO");
    	}
    	addOrderBy(sql, domain);
    	addLimit(sql, domain);
    	return findAll(domain, sql.toString());
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		if (domainFilter == null || !LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) return;
		CentroCusto centroCusto = (CentroCusto) domainFilter;
		sql.append(" JOIN TBLVPPLATAFORMAVENDACLIENTE platcli ON tb.CDEMPRESA = platcli.CDEMPRESA AND tb.CDREPRESENTANTE = platcli.CDREPRESENTANTE AND tb.CDCENTROCUSTO = platcli.CDCENTROCUSTO");
		if (ValueUtil.isNotEmpty(centroCusto.cdCliente) && !centroCusto.ignoreCliente) {
			sql.append(" AND platcli.CDCLIENTE =").append(Sql.getValue(centroCusto.cdCliente));
		}
	}
	
	
	public Vector findColumnValuesByExampleJoinPlataformaVendaCli(BaseDomain domain, String column) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append("select ");
        sql.append(column);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoin(domain, sql);
        addWhereByExample(domain, sql);
        addOrderBy(sql, domain);
        addLimit(sql, domain);
        sql.append("GROUP BY ").append(column);
        return findColumn(column, sql);
	}
	
	@Override
	protected void addGroupBy(StringBuffer sql) throws SQLException {
		if (!LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) return;
		
		sql.append(" GROUP BY 1");
	}

}