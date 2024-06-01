package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoSupervisor;
import totalcross.sql.ResultSet;

public class VerbaSaldoSupervisorDao extends CrudDbxDao {
	
	private static VerbaSaldoSupervisorDao instance;

	public VerbaSaldoSupervisorDao() {
		super(VerbaSaldoSupervisor.TABLE_NAME);
	}
	
	public static CrudDao getInstance() {
		if (instance == null) {
			instance = new VerbaSaldoSupervisorDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		VerbaSaldoSupervisor verba = new VerbaSaldoSupervisor();
		verba.cdEmpresa = rs.getString("cdEmpresa");
		verba.cdSupervisor = rs.getString("cdSupervisor");
		verba.cdContaCorrente = rs.getString("cdContaCorrente");
		verba.vlSaldo = rs.getDouble("vlSaldo");
		verba.vlSaldoInicial = rs.getDouble("vlSaldoInicial");
		verba.dtSaldo = rs.getDate("dtSaldo");
		verba.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
		verba.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
		return verba;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("CDEMPRESA, ")
		.append("CDSUPERVISOR, ")
		.append("CDCONTACORRENTE, ")
		.append("VLSALDO, ")
		.append("VLSALDOINICIAL, ")
		.append("DTSALDO, ")
		.append("DTVIGENCIAINICIAL, ")
		.append("DTVIGENCIAFINAL");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append("rowkey, ")
		.append("CDEMPRESA, ")
		.append("CDSUPERVISOR, ")
		.append("CDCONTACORRENTE, ")
		.append("VLSALDO, ")
		.append("VLSALDOINICIAL, ")
		.append("DTSALDO, ")
		.append("DTVIGENCIAINICIAL, ")
		.append("DTVIGENCIAFINAL, ")
		.append("NUCARIMBO, ")
		.append("FLTIPOALTECAO, ")
		.append("CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		VerbaSaldoSupervisor verba = (VerbaSaldoSupervisor) domain;
		sql.append(Sql.getValue(verba.getRowKey())).append(", ")
		.append(Sql.getValue(verba.cdEmpresa)).append(", ")
		.append(Sql.getValue(verba.cdSupervisor)).append(", ")
		.append(Sql.getValue(verba.cdContaCorrente)).append(", ")
		.append(Sql.getValue(verba.vlSaldo)).append(", ")
		.append(Sql.getValue(verba.vlSaldoInicial)).append(", ")
		.append(Sql.getValue(verba.dtSaldo)).append(", ")
		.append(Sql.getValue(verba.dtVigenciaInicial)).append(", ")
		.append(Sql.getValue(verba.dtVigenciaFinal)).append(", ")
		.append(Sql.getValue(verba.nuCarimbo)).append(", ")
		.append(Sql.getValue(verba.flTipoAlteracao)).append(", ")
		.append(Sql.getValue(verba.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		VerbaSaldoSupervisor verba = (VerbaSaldoSupervisor) domain;
		sql.append(" VLSALDO = ").append(Sql.getValue(verba.vlSaldo)).append(", ")
		.append(" VLSALDOINICIAL = ").append(Sql.getValue(verba.vlSaldoInicial)).append(", ")
		.append(" DTSALDO = ").append(Sql.getValue(verba.dtSaldo)).append(", ")
		.append(" DTVIGENCIAINICIAL = ").append(Sql.getValue(verba.dtVigenciaInicial)).append(", ")
		.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(verba.dtVigenciaFinal));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		VerbaSaldoSupervisor verba = (VerbaSaldoSupervisor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verba.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", verba.cdSupervisor);
		sqlWhereClause.addAndCondition("CDCONTACORRENTE = ", verba.cdContaCorrente);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL = ", verba.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("DTVIGENCIAFINAL = ", verba.dtVigenciaFinal);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaSaldoSupervisor();
	}

}
