package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.SaldoVendaRep;
import totalcross.sql.ResultSet;

public class SaldoVendaRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SaldoVendaRep();
	}

    private static SaldoVendaRepDbxDao instance;

    public SaldoVendaRepDbxDao() {
        super(SaldoVendaRep.TABLE_NAME); 
    }
    
    public static SaldoVendaRepDbxDao getInstance() {
        if (instance == null) {
            instance = new SaldoVendaRepDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	SaldoVendaRep saldoVendaRep = new SaldoVendaRep();
        saldoVendaRep.rowKey = rs.getString("rowkey");
        saldoVendaRep.cdEmpresa = rs.getString("cdEmpresa");
        saldoVendaRep.cdRepresentante = rs.getString("cdRepresentante");
        saldoVendaRep.dtUltimoSaldo = rs.getDate("dtUltimosaldo");
        saldoVendaRep.vlUltimoSaldo = ValueUtil.round(rs.getDouble("vlUltimosaldo"));
        saldoVendaRep.cdUsuario = rs.getString("cdUsuario");
        saldoVendaRep.nuCarimbo = rs.getInt("nuCarimbo");
        saldoVendaRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        saldoVendaRep.dtAlteracao = rs.getDate("dtAlteracao");
        saldoVendaRep.hrAlteracao = rs.getString("hrAlteracao");
        return saldoVendaRep;
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
        sql.append(" DTULTIMOSALDO,");
        sql.append(" VLULTIMOSALDO,");
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
        sql.append(" DTULTIMOSALDO,");
        sql.append(" VLULTIMOSALDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
    	SaldoVendaRep saldoVendaRep = (SaldoVendaRep) domain;
        sql.append(Sql.getValue(saldoVendaRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.dtUltimoSaldo)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.vlUltimoSaldo)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.cdUsuario)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.dtAlteracao)).append(",");
        sql.append(Sql.getValue(saldoVendaRep.hrAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
    	SaldoVendaRep saldoVendaRep = (SaldoVendaRep) domain;
        sql.append(" VLULTIMOSALDO = ").append(Sql.getValue(saldoVendaRep.vlUltimoSaldo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(saldoVendaRep.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(saldoVendaRep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(saldoVendaRep.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(saldoVendaRep.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(saldoVendaRep.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
    	SaldoVendaRep saldoVendaRep = (SaldoVendaRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", saldoVendaRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", saldoVendaRep.cdRepresentante);
		sqlWhereClause.addAndCondition("DTULTIMOSALDO = ", saldoVendaRep.dtUltimoSaldo);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}