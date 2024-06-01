package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class VerbaSaldoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaSaldo();
	}

    private static VerbaSaldoPdbxDao instance;

    public VerbaSaldoPdbxDao() {
        super(VerbaSaldo.TABLE_NAME);
    }

    public static VerbaSaldoPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaSaldoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	VerbaSaldo verbaSaldo = new VerbaSaldo();
        verbaSaldo.rowKey = rs.getString("rowkey");
        verbaSaldo.setCdEmpresa(rs.getString("cdEmpresa"));
        verbaSaldo.cdRepresentante = rs.getString("cdRepresentante");
        verbaSaldo.cdContaCorrente = rs.getString("cdContaCorrente");
        verbaSaldo.flOrigemSaldo = rs.getString("flOrigemSaldo");
        verbaSaldo.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaSaldo.vlSaldoInicial = ValueUtil.round(rs.getDouble("vlSaldoInicial"));
        verbaSaldo.dtSaldo = rs.getDate("dtSaldo");
        if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        	verbaSaldo.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        	verbaSaldo.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        }
        verbaSaldo.vlMinVerba = rs.getDouble("vlMinVerba");
        verbaSaldo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        verbaSaldo.cdUsuario = rs.getString("cdUsuario");
        return verbaSaldo;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONTACORRENTE,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" VLSALDOINICIAL,");
        sql.append(" DTSALDO,");
        if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        	sql.append(" DTVIGENCIAINICIAL,");
        	sql.append(" DTVIGENCIAFINAL,");
        }
        sql.append(" VLMINVERBA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONTACORRENTE,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" DTSALDO,");
        if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        	sql.append(" DTVIGENCIAINICIAL,");
        	sql.append(" DTVIGENCIAFINAL,");
        }
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaSaldo verbaSaldo = (VerbaSaldo) domain;
        sql.append(Sql.getValue(verbaSaldo.getCdEmpresa())).append(",");
        sql.append(Sql.getValue(verbaSaldo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaSaldo.cdContaCorrente)).append(",");
        sql.append(Sql.getValue(verbaSaldo.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(verbaSaldo.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaSaldo.dtSaldo)).append(",");
        if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
        	sql.append(Sql.getValue(verbaSaldo.dtVigenciaInicial)).append(",");
        	sql.append(Sql.getValue(verbaSaldo.dtVigenciaFinal)).append(",");
        }
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(verbaSaldo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(verbaSaldo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaSaldo verbaSaldo = (VerbaSaldo) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaSaldo.vlSaldo)).append(",");
        sql.append(" DTSALDO = ").append(Sql.getValue(verbaSaldo.dtSaldo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaSaldo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaSaldo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaSaldo verbaSaldo = (VerbaSaldo) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndConditionEquals("CDEMPRESA", verbaSaldo.getCdEmpresa());
        sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", verbaSaldo.cdRepresentante);
        sqlWhereClause.addAndConditionEquals("CDCONTACORRENTE", verbaSaldo.cdContaCorrente);
        sqlWhereClause.addAndConditionEquals("FLORIGEMSALDO", verbaSaldo.flOrigemSaldo);
		sqlWhereClause.addAndCondition(" DTVIGENCIAINICIAL <= ", verbaSaldo.dtVigenciaInicial);
		sqlWhereClause.addAndCondition(" DTVIGENCIAFINAL >= ", verbaSaldo.dtVigenciaInicial);
        sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by flOrigemSaldo , cdContaCorrente ");
    }

    //@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    public void updateVigenciaSaldoApp(VerbaSaldo verbaSaldo) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(tableName);
        sql.append(" SET DTVIGENCIAINICIAL = ").append(Sql.getValue(verbaSaldo.dtVigenciaInicial)).append(",");
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(verbaSaldo.dtVigenciaFinal));
        sql.append(" WHERE ROWKEY = ").append(Sql.getValue(verbaSaldo.getRowKey()));
        executeUpdate(sql.toString());
    }

}
