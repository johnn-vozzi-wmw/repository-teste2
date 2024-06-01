package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RelNovidadePesquisaMercadoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelNovidadePesquisaMercado();
	}

	private static RelNovidadePesquisaMercadoDbxDao instance;

	public RelNovidadePesquisaMercadoDbxDao() {
		super(RelNovidadePesquisaMercado.TABLE_NAME);
	}

	public static RelNovidadePesquisaMercadoDbxDao getInstance() {
		if (instance == null) {
			instance = new RelNovidadePesquisaMercadoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = new RelNovidadePesquisaMercado();
		relNovidadePesquisaMercado.rowKey = rs.getString("rowkey");
		relNovidadePesquisaMercado.cdEmpresa = rs.getString("cdEmpresa");
		relNovidadePesquisaMercado.cdRepresentante = rs.getString("cdRepresentante");
		relNovidadePesquisaMercado.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		relNovidadePesquisaMercado.cdTipoNovidade = rs.getString("cdTipoNovidade");
		relNovidadePesquisaMercado.dsNovidadePesquisa = rs.getString("dsNovidadePesquisa");
		relNovidadePesquisaMercado.dtEmissaoRelatorio = rs.getDate("dtEmissaoRelatorio");
		return relNovidadePesquisaMercado;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		getColumns(sql);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDPESQUISAMERCADOCONFIG,");
		sql.append(" tb.CDTIPONOVIDADE,");
		sql.append(" tb.DSNOVIDADEPESQUISA,");
		sql.append(" tb.DTEMISSAORELATORIO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = (RelNovidadePesquisaMercado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", relNovidadePesquisaMercado.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", relNovidadePesquisaMercado.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", relNovidadePesquisaMercado.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" CDTIPONOVIDADE = ", relNovidadePesquisaMercado.cdTipoNovidade);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = (RelNovidadePesquisaMercado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", relNovidadePesquisaMercado.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", relNovidadePesquisaMercado.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPESQUISAMERCADOCONFIG = ", relNovidadePesquisaMercado.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition("tb.CDTIPONOVIDADE = ", relNovidadePesquisaMercado.cdTipoNovidade);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findAllByExampleQuantidadeJoinConfig(RelNovidadePesquisaMercado relNovidadePesquisaMercado) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) as qtRegistro, tb.cdTipoNovidade, cfg.cdPesquisaMercadoConfig ");
		sql.append(" FROM ");
		addJoinConfig(relNovidadePesquisaMercado, sql);
		sql.append(" GROUP BY tb.cdTipoNovidade");
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				RelNovidadePesquisaMercado domain = new RelNovidadePesquisaMercado();
				domain.cdEmpresa = relNovidadePesquisaMercado.cdEmpresa;
				domain.cdRepresentante = relNovidadePesquisaMercado.cdRepresentante;
				domain.qtRegistrosTipoNovidade = rs.getInt("qtRegistro");
				domain.cdTipoNovidade = rs.getString("cdTipoNovidade");
				domain.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
				list.addElement(domain);
			}
		}
		return list;
	}

	public Vector findAllByExampleJoinConfig(RelNovidadePesquisaMercado relNovidadePesquisaMercado) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(relNovidadePesquisaMercado, sql);
		sql.append(" FROM ");
		addJoinConfig(relNovidadePesquisaMercado, sql);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populate(relNovidadePesquisaMercado, rs));
			}
		}
		return list;
	}

	private void addJoinConfig(RelNovidadePesquisaMercado relNovidadePesquisaMercado, StringBuffer sql) {
		sql.append(RelNovidadePesquisaMercado.TABLE_NAME);
		sql.append(" tb join ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" cfg ON tb.CDEMPRESA = cfg.CDEMPRESA ");
		sql.append(" AND tb.CDREPRESENTANTE = cfg.CDREPRESENTANTE ");
		sql.append(" AND tb.CDPESQUISAMERCADOCONFIG = cfg.CDPESQUISAMERCADOCONFIG ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", relNovidadePesquisaMercado.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", relNovidadePesquisaMercado.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDTIPONOVIDADE = ", relNovidadePesquisaMercado.cdTipoNovidade);
		sqlWhereClause.addAndOrLikeCondition(" cfg.DSPESQUISAMERCADO ", " cfg.CDPESQUISAMERCADOCONFIG ", relNovidadePesquisaMercado.dsFiltro);
		sqlWhereClause.addAndCondition(" tb.DTEMISSAORELATORIO = ", relNovidadePesquisaMercado.dtEmissaoRelatorio);
		sql.append(sqlWhereClause.getSql());
	}
}
