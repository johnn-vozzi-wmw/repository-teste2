package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConcorrente;
import totalcross.sql.ResultSet;

public class PesquisaMercadoConcorrenteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaMercadoConcorrente();
	}

	private static PesquisaMercadoConcorrenteDbxDao instance;

	public PesquisaMercadoConcorrenteDbxDao() {
		super(PesquisaMercadoConcorrente.TABLE_NAME);
	}

	public static PesquisaMercadoConcorrenteDbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoConcorrenteDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrente = new PesquisaMercadoConcorrente();
		pesquisaMercadoConcorrente.rowKey = rs.getString("rowkey");
		pesquisaMercadoConcorrente.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaMercadoConcorrente.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		pesquisaMercadoConcorrente.cdConcorrente = rs.getString("cdConcorrente");
		pesquisaMercadoConcorrente.dsConcorrente = rs.getString("dsConcorrente");
		return pesquisaMercadoConcorrente;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		getColumns(sql);
		sql.append(", c.DSCONCORRENTE ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDPESQUISAMERCADOCONFIG,");
		sql.append(" tb.CDCONCORRENTE,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrente = (PesquisaMercadoConcorrente) domain;
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.cdEmpresa)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.cdPesquisaMercadoConfig)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.cdConcorrente)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.nuCarimbo)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConcorrente.cdUsuario));
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" join ");
		sql.append(Concorrente.TABLE_NAME);
		sql.append(" c on tb.CDEMPRESA = c.CDEMPRESA");
		sql.append(" and tb.CDCONCORRENTE = c.CDCONCORRENTE ");
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrente = (PesquisaMercadoConcorrente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pesquisaMercadoConcorrente.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConcorrente.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition("tb.CDCONCORRENTE = ", pesquisaMercadoConcorrente.cdConcorrente);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrente = (PesquisaMercadoConcorrente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pesquisaMercadoConcorrente.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConcorrente.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" CDCONCORRENTE = ", pesquisaMercadoConcorrente.cdConcorrente);
		sql.append(sqlWhereClause.getSql());
	}
}
