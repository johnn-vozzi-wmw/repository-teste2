package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoVigencia;
import totalcross.sql.ResultSet;

public class VerbaSaldoVigenciaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaSaldoVigencia();
	}

    private static VerbaSaldoVigenciaDbxDao instance;

    public VerbaSaldoVigenciaDbxDao() {
        super(VerbaSaldoVigencia.TABLE_NAME); 
    }
    
    public static VerbaSaldoVigenciaDbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaSaldoVigenciaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        VerbaSaldoVigencia verbaSaldoVigencia = new VerbaSaldoVigencia();
        verbaSaldoVigencia.rowKey = rs.getString("rowkey");
        verbaSaldoVigencia.cdEmpresa = rs.getString("cdEmpresa");
        verbaSaldoVigencia.cdRepresentante = rs.getString("cdRepresentante");
        verbaSaldoVigencia.cdMesSaldo = rs.getInt("cdMesSaldo");
        verbaSaldoVigencia.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaSaldoVigencia.dtSaldo = rs.getDate("dtSaldo");
        verbaSaldoVigencia.dtInicioVigencia = rs.getDate("dtInicioVigencia");
        verbaSaldoVigencia.dtFimVigencia = rs.getDate("dtFimVigencia");
        verbaSaldoVigencia.cdUsuario = rs.getString("cdUsuario");
        verbaSaldoVigencia.nuCarimbo = rs.getInt("nuCarimbo");
        verbaSaldoVigencia.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return verbaSaldoVigencia;
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
        sql.append(" CDMESSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" DTSALDO,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTFIMVIGENCIA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMESSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" DTSALDO,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTFIMVIGENCIA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        VerbaSaldoVigencia verbaSaldoVigencia = (VerbaSaldoVigencia) domain;
        sql.append(Sql.getValue(verbaSaldoVigencia.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.cdMesSaldo)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.dtSaldo)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.dtInicioVigencia)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.dtFimVigencia)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.cdUsuario)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaSaldoVigencia.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        VerbaSaldoVigencia verbaSaldoVigencia = (VerbaSaldoVigencia) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaSaldoVigencia.vlSaldo)).append(",");
        sql.append(" DTSALDO = ").append(Sql.getValue(verbaSaldoVigencia.dtSaldo)).append(",");
        sql.append(" DTINICIOVIGENCIA = ").append(Sql.getValue(verbaSaldoVigencia.dtInicioVigencia)).append(",");
        sql.append(" DTFIMVIGENCIA = ").append(Sql.getValue(verbaSaldoVigencia.dtFimVigencia)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaSaldoVigencia.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaSaldoVigencia.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaSaldoVigencia.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        VerbaSaldoVigencia verbaSaldoVigencia = (VerbaSaldoVigencia) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbaSaldoVigencia.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbaSaldoVigencia.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMESSALDO = ", verbaSaldoVigencia.cdMesSaldo);
		if (ValueUtil.isNotEmpty(verbaSaldoVigencia.dtVigenciaFilter)) {
			sqlWhereClause.addAndCondition("DTINICIOVIGENCIA <= ", verbaSaldoVigencia.dtVigenciaFilter);
			sqlWhereClause.addAndCondition("DTFIMVIGENCIA >= ",verbaSaldoVigencia.dtVigenciaFilter);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public void deleteVerbaSaldoForaVigencia() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("DELETE FROM TBLVPVERBASALDOVIGENCIA")
    	.append(" WHERE DTFIMVIGENCIA < ").append(Sql.getValue(DateUtil.getCurrentDate()));
    	try {
    		executeUpdate(sql.toString());
    	} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
    }
    
}