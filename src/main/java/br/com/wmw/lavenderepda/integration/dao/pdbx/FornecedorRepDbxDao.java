package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FornecedorRep;
import totalcross.sql.ResultSet;

public class FornecedorRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FornecedorRep();
	}

    private static FornecedorRepDbxDao instance;

    public FornecedorRepDbxDao() {
        super(FornecedorRep.TABLE_NAME); 
    }
    
    public static FornecedorRepDbxDao getInstance() {
        if (instance == null) {
            instance = new FornecedorRepDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FornecedorRep fornecedorRep = new FornecedorRep();
        fornecedorRep.rowKey = rs.getString("rowkey");
        fornecedorRep.cdEmpresa = rs.getString("cdEmpresa");
        fornecedorRep.cdRepresentante = rs.getString("cdRepresentante");
        fornecedorRep.cdFornecedor = rs.getString("cdFornecedor");
        fornecedorRep.cdUsuario = rs.getString("cdUsuario");
        fornecedorRep.nuCarimbo = rs.getInt("nuCarimbo");
        fornecedorRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return fornecedorRep;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFORNECEDOR,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFORNECEDOR,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FornecedorRep fornecedorRep = (FornecedorRep) domain;
        sql.append(Sql.getValue(fornecedorRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fornecedorRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fornecedorRep.cdFornecedor)).append(",");
        sql.append(Sql.getValue(fornecedorRep.cdUsuario)).append(",");
        sql.append(Sql.getValue(fornecedorRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(fornecedorRep.flTipoAlteracao)).append(",");
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FornecedorRep fornecedorRep = (FornecedorRep) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fornecedorRep.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fornecedorRep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fornecedorRep.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FornecedorRep fornecedorRep = (FornecedorRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fornecedorRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fornecedorRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFORNECEDOR = ", fornecedorRep.cdFornecedor);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}