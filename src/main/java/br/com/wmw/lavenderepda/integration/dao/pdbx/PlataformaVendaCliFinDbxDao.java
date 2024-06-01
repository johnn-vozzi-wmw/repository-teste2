package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliFin;
import totalcross.sql.ResultSet;

public class PlataformaVendaCliFinDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlataformaVendaCliFin();
	}

    private static PlataformaVendaCliFinDbxDao instance;

    public PlataformaVendaCliFinDbxDao() {
        super(PlataformaVendaCliFin.TABLE_NAME); 
    }
    
    public static PlataformaVendaCliFinDbxDao getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaCliFinDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PlataformaVendaCliFin plataformaVendaCliFin = new PlataformaVendaCliFin();
        plataformaVendaCliFin.rowKey = rs.getString("rowkey");
        plataformaVendaCliFin.cdEmpresa = rs.getString("cdEmpresa");
        plataformaVendaCliFin.cdRepresentante = rs.getString("cdRepresentante");
        plataformaVendaCliFin.cdCliente = rs.getString("cdCliente");
        plataformaVendaCliFin.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        plataformaVendaCliFin.cdLinha = rs.getString("cdLinha");
		//-- ROUND COM 7 CASAS PARA QUEBRAR O PONTO FLUTUANTE DO JAVA, NÃO REMOVER!
	    plataformaVendaCliFin.vlIndiceFinanceiro = ValueUtil.round(rs.getDouble("vlIndiceFinanceiro"), 7);
        plataformaVendaCliFin.nuCarimbo = rs.getInt("nuCarimbo");
        plataformaVendaCliFin.flTipoAlteracao = rs.getString("flTipoAlteracao");
        plataformaVendaCliFin.cdUsuario = rs.getString("cdUsuario");
        return plataformaVendaCliFin;
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
        sql.append(" CDCLIENTE,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDLINHA,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    @Override
    protected void addInsertColumns(StringBuffer sql) { }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaCliFin plataformaVendaCliFin = (PlataformaVendaCliFin) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", plataformaVendaCliFin.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", plataformaVendaCliFin.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", plataformaVendaCliFin.cdCliente);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", plataformaVendaCliFin.cdPlataformaVenda);
		sqlWhereClause.addAndCondition("CDLINHA = ", plataformaVendaCliFin.cdLinha);
		sql.append(sqlWhereClause.getSql());
    }
}