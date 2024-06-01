package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdCli;
import totalcross.sql.ResultSet;

public class VariacaoProdCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VariacaoProdCli();
	}

    private static VariacaoProdCliDbxDao instance;
	

    public VariacaoProdCliDbxDao() {
        super(VariacaoProdCli.TABLE_NAME);
    }
    
    public static VariacaoProdCliDbxDao getInstance() {
        if (instance == null) {
            instance = new VariacaoProdCliDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VariacaoProdCli variacaoProdCli = new VariacaoProdCli();
        variacaoProdCli.rowKey = rs.getString("rowkey");
        variacaoProdCli.cdEmpresa = rs.getString("cdEmpresa");
        variacaoProdCli.cdRepresentante = rs.getString("cdRepresentante");
        variacaoProdCli.cdProduto = rs.getString("cdProduto");
        variacaoProdCli.cdCliente = rs.getString("cdCliente");
        variacaoProdCli.vlPctVariacao = ValueUtil.round(rs.getDouble("vlPctVariacao"));
        variacaoProdCli.dtInicioVigencia = rs.getDate("dtInicioVigencia");
        variacaoProdCli.dtFimVigencia = rs.getDate("dtFimVigencia");
        variacaoProdCli.nuCarimbo = rs.getInt("nuCarimbo");
        variacaoProdCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        variacaoProdCli.cdUsuario = rs.getString("cdUsuario");
        return variacaoProdCli;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLPCTVARIACAO,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTFIMVIGENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLPCTVARIACAO,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTFIMVIGENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCli variacaoProdCli = (VariacaoProdCli) domain;
        sql.append(Sql.getValue(variacaoProdCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.cdProduto)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.cdCliente)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.vlPctVariacao)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.dtInicioVigencia)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.dtFimVigencia)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(variacaoProdCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCli variacaoProdCli = (VariacaoProdCli) domain;
        sql.append(" VLPCTVARIACAO = ").append(Sql.getValue(variacaoProdCli.vlPctVariacao)).append(",");
        sql.append(" DTINICIOVIGENCIA = ").append(Sql.getValue(variacaoProdCli.dtInicioVigencia)).append(",");
        sql.append(" DTFIMVIGENCIA = ").append(Sql.getValue(variacaoProdCli.dtFimVigencia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(variacaoProdCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(variacaoProdCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(variacaoProdCli.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCli variacaoProdCli = (VariacaoProdCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", variacaoProdCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", variacaoProdCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", variacaoProdCli.cdProduto);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", variacaoProdCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}