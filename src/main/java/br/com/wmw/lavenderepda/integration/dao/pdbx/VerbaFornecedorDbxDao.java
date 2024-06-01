package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaFornecedor;
import totalcross.sql.ResultSet;

public class VerbaFornecedorDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaFornecedor();
	}

    private static VerbaFornecedorDbxDao instance;
	

    public VerbaFornecedorDbxDao() {
        super(VerbaFornecedor.TABLE_NAME);
    }
    
    public static VerbaFornecedorDbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaFornecedorDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VerbaFornecedor verbaFornecedor = new VerbaFornecedor();
        verbaFornecedor.rowKey = rs.getString("rowkey");
        verbaFornecedor.cdEmpresa = rs.getString("cdEmpresa");
        verbaFornecedor.cdRepresentante = rs.getString("cdRepresentante");
        verbaFornecedor.cdFornecedor = rs.getString("cdFornecedor");
        verbaFornecedor.flOrigemVerba = rs.getString("flOrigemVerba");
        verbaFornecedor.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaFornecedor.vlSaldoInicial = ValueUtil.round(rs.getDouble("vlSaldoInicial"));
        verbaFornecedor.nuCarimbo = rs.getInt("nuCarimbo");
        verbaFornecedor.flTipoAlteracao = rs.getString("flTipoAlteracao");
        verbaFornecedor.cdUsuario = rs.getString("cdUsuario");
        return verbaFornecedor;
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
        sql.append(" CDFORNECEDOR,");
        sql.append(" FLORIGEMVERBA,");
        sql.append(" VLSALDO,");
        sql.append(" VLSALDOINICIAL,");
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
        sql.append(" CDFORNECEDOR,");
        sql.append(" FLORIGEMVERBA,");
        sql.append(" VLSALDO,");
        sql.append(" VLSALDOINICIAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaFornecedor verbaFornecedor = (VerbaFornecedor) domain;
        sql.append(Sql.getValue(verbaFornecedor.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.cdFornecedor)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.flOrigemVerba)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.vlSaldoInicial)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(verbaFornecedor.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaFornecedor verbaFornecedor = (VerbaFornecedor) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaFornecedor.vlSaldo)).append(",");
        sql.append(" VLSALDOINICIAL = ").append(Sql.getValue(verbaFornecedor.vlSaldoInicial)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaFornecedor.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaFornecedor.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaFornecedor.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaFornecedor verbaFornecedor = (VerbaFornecedor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbaFornecedor.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbaFornecedor.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFORNECEDOR = ", verbaFornecedor.cdFornecedor);
		sqlWhereClause.addAndCondition("FLORIGEMVERBA = ", verbaFornecedor.flOrigemVerba);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}