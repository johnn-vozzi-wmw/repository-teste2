package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class FotoProdutoGradeDbxDao extends LavendereCrudDbxDao {

	private static FotoProdutoGradeDbxDao instance;

	public static FotoProdutoGradeDbxDao getInstance() {
		if (instance == null) {
			instance = new FotoProdutoGradeDbxDao(FotoProdutoGrade.TABLE_NAME);
		}
		return instance;
	}

	public FotoProdutoGradeDbxDao(String newTableName) {
		super(newTableName);
	}

	@Override
	protected BaseDomain populate(BaseDomain baseDomain, ResultSet rs) throws SQLException {
		FotoProdutoGrade fotoProdutoGrade = new FotoProdutoGrade();
		fotoProdutoGrade.rowKey = rs.getString("rowkey");
		fotoProdutoGrade.dsAgrupadorGrade = rs.getString("dsAgrupadorGrade");
		fotoProdutoGrade.nmFoto = rs.getString("nmFoto");
		fotoProdutoGrade.nuTamanho = rs.getInt("nuTamanho");
		fotoProdutoGrade.dtModificacao = rs.getDate("dtModificacao");
		fotoProdutoGrade.hrModificacao = rs.getString("hrModificacao");
		fotoProdutoGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
		fotoProdutoGrade.nuCarimbo = rs.getInt("nuCarimbo");
		fotoProdutoGrade.cdUsuario = rs.getString("cdUsuario");
		return fotoProdutoGrade;
	}

	@Override
	public void addSelectColumns(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.DSAGRUPADORGRADE,");
		sql.append(" tb.NMFOTO,");
		sql.append(" tb.NUTAMANHO,");
		sql.append(" tb.DTMODIFICACAO,");
		sql.append(" tb.HRMODIFICACAO,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.CDUSUARIO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoProdutoGrade fotoProduto = (FotoProdutoGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSAGRUPADORGRADE = ", fotoProduto.dsAgrupadorGrade);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoProduto.nmFoto);
		sqlWhereClause.addAndCondition("NUCARIMBO = ", fotoProduto.nuCarimbo);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", fotoProduto.flTipoAlteracao);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoProdutoGrade();
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" DSAGRUPADORGRADE,");
		sql.append(" NMFOTO,");
		sql.append(" NUTAMANHO,");
		sql.append(" DTMODIFICACAO,");
		sql.append(" HRMODIFICACAO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO,");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoProdutoGrade fotoProduto = (FotoProdutoGrade) domain;
		sql.append(Sql.getValue(fotoProduto.dsAgrupadorGrade)).append(",");
		sql.append(Sql.getValue(fotoProduto.nmFoto)).append(",");
		sql.append(Sql.getValue(fotoProduto.nuTamanho)).append(",");
		sql.append(Sql.getValue(fotoProduto.dtModificacao)).append(",");
		sql.append(Sql.getValue(fotoProduto.hrModificacao)).append(",");
		sql.append(Sql.getValue(fotoProduto.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(fotoProduto.nuCarimbo)).append(",");
		sql.append(Sql.getValue(fotoProduto.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoProdutoGrade fotoProduto = (FotoProdutoGrade) domain;
		sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoProduto.nuTamanho)).append(",");
		sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoProduto.dtModificacao)).append(",");
		sql.append(" HRMODIFICACAO = ").append(Sql.getValue(fotoProduto.hrModificacao)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoProduto.flTipoAlteracao)).append(",");
		sql.append(" nuCarimbo = ").append(Sql.getValue(fotoProduto.nuCarimbo)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoProduto.cdUsuario));
	}

	public Vector findAllNaoAlterados() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		sql.append(" dsAgrupadorGrade, nmFoto, flTipoAlteracao, nuCarimbo, dtModificacao, hrModificacao ");
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereAllNaoAlterados(sql);
		addOrderBy(sql, null);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector fotoProdutoGradeList = new Vector(0);
			FotoProdutoGrade fotoProdutoGrade;
			while (rs.next()) {
				fotoProdutoGrade = new FotoProdutoGrade();
				fotoProdutoGrade.dsAgrupadorGrade = rs.getString(1);
				fotoProdutoGrade.nmFoto = rs.getString(2);
				fotoProdutoGrade.flTipoAlteracao = rs.getString(3);
				fotoProdutoGrade.nuCarimbo = rs.getInt(4);
				fotoProdutoGrade.dtModificacao = rs.getDate(5);
				fotoProdutoGrade.hrModificacao = rs.getString(6);
				fotoProdutoGradeList.addElement(fotoProdutoGrade);
			}
			return fotoProdutoGradeList;
		}
	}
	
	public int countAllNaoAlterados() throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as qtde FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereAllNaoAlterados(sql);
		return getInt(sql.toString());
	}

	public int countFotoCargaInicial() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(*) NUM FROM ").append(tableName).append(" WHERE FLTIPOALTERACAO IS NOT NULL ");
		return getInt(sql.toString());
	}
	
	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLTIPOALTERACAO = '").append(BaseDomain.FLTIPOALTERACAO_INSERIDO)
		.append("' WHERE FLTIPOALTERACAO IS NOT NULL ");
		updateAll(sql.toString());
	}
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_ALTERADO);
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_INSERIDO);
	}

	private void updateResetFlTipoAlteracao(String flTipoAlteracao, String flTipoAlteracaoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLTIPOALTERACAO = '").append(flTipoAlteracao)
		.append("' WHERE FLTIPOALTERACAO = '").append(flTipoAlteracaoFilter).append("'");
		updateAll(sql.toString());
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if (domain != null && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} else {
			sql.append(" ORDER BY CAST(CDFOTOPRODUTOGRADE AS INT) ");
		}
	}

}
