package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaProduto;
import totalcross.sql.ResultSet;

public class PlataformaVendaProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlataformaVendaProduto();
	}

    private static PlataformaVendaProdutoDbxDao instance;

    public PlataformaVendaProdutoDbxDao() {
        super(PlataformaVendaProduto.TABLE_NAME); 
    }
    
    public static PlataformaVendaProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaProdutoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PlataformaVendaProduto plataformavendaproduto = new PlataformaVendaProduto();
        plataformavendaproduto.rowKey = rs.getString("rowkey");
        plataformavendaproduto.cdEmpresa = rs.getString("cdEmpresa");
        plataformavendaproduto.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        plataformavendaproduto.cdPlataformaVendaProd = rs.getString("cdPlataformaVendaProd");
        plataformavendaproduto.cdMarca = rs.getString("cdMarca");
        plataformavendaproduto.cdLinha = rs.getString("cdLinha");
        plataformavendaproduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        plataformavendaproduto.cdUsuario = rs.getString("cdUsuario");
        return plataformavendaproduto;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDPLATAFORMAVENDAPROD,");
        sql.append(" CDMARCA,");
        sql.append(" CDLINHA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDPLATAFORMAVENDAPROD,");
        sql.append(" CDMARCA,");
        sql.append(" CDLINHA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaProduto plataformavendaproduto = (PlataformaVendaProduto) domain;
        sql.append(Sql.getValue(plataformavendaproduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.cdPlataformaVenda)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.cdPlataformaVendaProd)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.cdMarca)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.cdLinha)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(plataformavendaproduto.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaProduto plataformavendaproduto = (PlataformaVendaProduto) domain;
        sql.append(" CDMARCA = ").append(Sql.getValue(plataformavendaproduto.cdMarca)).append(",");
        sql.append(" CDLINHA = ").append(Sql.getValue(plataformavendaproduto.cdLinha)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(plataformavendaproduto.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(plataformavendaproduto.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaProduto plataformavendaproduto = (PlataformaVendaProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", plataformavendaproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", plataformavendaproduto.cdPlataformaVenda);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDAPROD = ", plataformavendaproduto.cdPlataformaVendaProd);
		sqlWhereClause.addAndCondition("CDLINHA = ", plataformavendaproduto.cdLinha);
		sqlWhereClause.addAndCondition("CDMARCA = ", plataformavendaproduto.cdMarca);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}