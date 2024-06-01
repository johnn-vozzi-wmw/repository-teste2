package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgCategoria;
import br.com.wmw.lavenderepda.business.domain.BonifCfgCliente;
import br.com.wmw.lavenderepda.business.domain.BonifCfgConjunto;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFamilia;
import br.com.wmw.lavenderepda.business.domain.BonifCfgLinha;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import br.com.wmw.lavenderepda.business.domain.BonifCfgRepresentante;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class BonifCfgDbxDao extends CrudDbxDao {
	
	private static BonifCfgDbxDao instance;
	
	public static BonifCfgDbxDao getInstance() {
		if (instance == null) {
			instance = new BonifCfgDbxDao();
		}
		return instance;
	}

	public BonifCfgDbxDao() {
		super(BonifCfg.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		BonifCfg bonConfig = new BonifCfg(rs.getString("cdEmpresa"), rs.getString("cdBonifCfg"));
		bonConfig.rowKey = rs.getString("rowKey");
		bonConfig.dsBonifCfg = rs.getString("dsBonifCfg");
		bonConfig.dtVigenciaIni = rs.getDate("dtVigenciaIni");
		bonConfig.dtVigenciaFim = rs.getDate("dtVigenciaFim");
		bonConfig.flOpcional = rs.getString("flOpcional");
		bonConfig.vlPctSobreVenda = rs.getDouble("vlPctSobreVenda");
		bonConfig.nuPrioridade = rs.getInt("nuPrioridade");
		bonConfig.cdTipoRegraBonificacao = rs.getString("cdTipoRegraBonificacao");
		bonConfig.flBonificacaoAutomatica = rs.getString("flBonificacaoAutomatica");
		bonConfig.qtSaldoContaCorrente = rs.getDouble("qtSaldoContaCorrente");
		return bonConfig;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.cdEmpresa,");
		sql.append(" tb.cdBonifCfg,");
		sql.append(" tb.dsBonifCfg,");
		sql.append(" tb.dtVigenciaIni,");
		sql.append(" tb.dtVigenciaFim,");
		sql.append(" tb.flOpcional,");
		sql.append(" tb.vlPctSobreVenda,");
		sql.append(" tb.nuPrioridade,");
		sql.append(" tb.cdTipoRegraBonificacao,");
		sql.append(" tb.flBonificacaoAutomatica,");
		sql.append(" tb.qtSaldoContaCorrente");
	}
	
	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		//Essa entidade não é cadastrada pelo app
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//Essa entidade não é cadastrada pelo app
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//Essa entidade não é atualizada pelo app
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		BonifCfg bonConfig = (BonifCfg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" tb.CDEMPRESA ", bonConfig.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" tb.CDBONIFCFG ", bonConfig.cdBonifCfg);
		sqlWhereClause.addAndCondition(" DATE('now', 'localtime') BETWEEN tb.DTVIGENCIAINI and tb.DTVIGENCIAFIM ");
		sql.append(sqlWhereClause.getSql());
		
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonifCfg();
	}
	
	private void addWhereExistsBonifCfg(StringBuffer sql, BonifCfg bonifCfg) {
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoRepresentante() && bonifCfg.getBonCfgRep() != null) {
			addCompareExistsBonifCfgRep(sql, bonifCfg.getBonCfgRep());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoCategoria() && bonifCfg.getBonCfgCat() != null) {
			addCompareExistsBonifCfgCat(sql, bonifCfg.getBonCfgCat());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoLinha() && bonifCfg.getBonCfgLinha() != null) {
			addCompareExistsBonifCfgLin(sql, bonifCfg.getBonCfgLinha());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoFamilia() && bonifCfg.getBonCfgFam() != null) {
			addCompareExistsBonifCfgFam(sql, bonifCfg.getBonCfgFam());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto() && bonifCfg.getBonifCfgProduto() != null) {
			addCompareExistsBonifCfgProd(sql, bonifCfg.getBonifCfgProduto());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos() && bonifCfg.getBonifCfgConjunto() != null) {
			addCompareExistsBonifCfgConjunto(sql, bonifCfg.getBonifCfgConjunto());
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoClientes() && bonifCfg.getBonifCfgCliente() != null) {
			addCompareExistsBonifCfgCliente(sql, bonifCfg.getBonifCfgCliente());
		}
	}
	
	private void addCompareExists(StringBuffer sql, String tableName, String alias) {
		sql.append(" SELECT 1 FROM ")
		.append(tableName).append(" ").append(alias)
		.append(" WHERE tb.CDEMPRESA = ")
		.append(alias).append(".CDEMPRESA")
		.append(" AND tb.CDBONIFCFG = ")
		.append(alias).append(".CDBONIFCFG");
	}
	
	public Vector findAllBonifCfgByExample(BonifCfg bonCfg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(bonCfg, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(bonCfg, sql);
    	addWhereExistsBonifCfg(sql, bonCfg);
    	sql.append(" order by CASE WHEN COALESCE(NUPRIORIDADE, 0) = 0 THEN 99999999 ELSE NUPRIORIDADE END ASC ");
    	
    	Vector resultList = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				resultList.addElement(populate(null, rs));
			} 
		}
    	return resultList;
	}
	
	public int countAllBonifCfgByExample(BonifCfg bonCfg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select count(1)");
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(bonCfg, sql);
    	addWhereExistsBonifCfg(sql, bonCfg);
    	sql.append(" order by CASE WHEN COALESCE(NUPRIORIDADE, 0) = 0 THEN 99999999 ELSE NUPRIORIDADE END ASC ");
    	return getInt(sql.toString());
	}
	
	private String getFlBonificacaoClause(String alias, String flGeraBonificacao, String flPermiteBonificar) {
		String result = "";
		if (ValueUtil.isNotEmpty(flGeraBonificacao)) {
			result += " AND "+ alias + ".FLGERABONIFICACAO = " + Sql.getValue(flGeraBonificacao);
		}
		if (ValueUtil.isNotEmpty(flPermiteBonificar)) {
			result += " AND "+ alias + ".FLPERMITEBONIFICAR = " + Sql.getValue(flPermiteBonificar);
		}
		return result;
	}
	
	private void addCompareExistsBonifCfgRep(StringBuffer sql, BonifCfgRepresentante bonCfgRep) {
		sql.append(" AND (EXISTS (");
		addCompareExists(sql, BonifCfgRepresentante.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGREPRESENTANTE);
		sql.append(" AND ")
		.append(DaoUtil.ALIAS_BONIFCFGREPRESENTANTE)
		.append(".CDREPRESENTANTE = ")
		.append(Sql.getValue(bonCfgRep.cdRepresentante));
		sql.append(") OR NOT EXISTS (");
		addCompareExists(sql, BonifCfgRepresentante.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGREPRESENTANTE);
		sql.append(" ))");
	}
	
	private void addCompareExistsBonifCfgCat(StringBuffer sql, BonifCfgCategoria bonCfgCat) {
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonCfgCat.cdCategoria)) {
			sql.append("EXISTS (");
			addCompareExists(sql, BonifCfgCategoria.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCATEGORIA);
			sql.append(" AND ")
			.append(DaoUtil.ALIAS_BONIFCFGCATEGORIA)
			.append(".CDCATEGORIA = ")
			.append(Sql.getValue(bonCfgCat.cdCategoria));
			sql.append(") OR ");
		}
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgCategoria.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCATEGORIA);
		sql.append(" ))");
	}
	
	private void addCompareExistsBonifCfgLin(StringBuffer sql, BonifCfgLinha bonCfgLin) {
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonCfgLin.cdLinha)) {
			sql.append("EXISTS (");
			addCompareExists(sql, BonifCfgLinha.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGLINHA);
			sql.append(" AND ")
			.append(DaoUtil.ALIAS_BONIFCFGLINHA)
			.append(".CDLINHA = ")
			.append(Sql.getValue(bonCfgLin.cdLinha));
			sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGLINHA, bonCfgLin.flGeraBonificacao, bonCfgLin.flPermiteBonificar))
			.append(") OR ");
		}
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgLinha.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGLINHA);
		sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGLINHA, bonCfgLin.flGeraBonificacao, bonCfgLin.flPermiteBonificar))
		.append(" ))");
	}
	
	private void addCompareExistsBonifCfgFam(StringBuffer sql, BonifCfgFamilia bonCfgFam) {
		sql.append(" AND (EXISTS (");
		addCompareExists(sql, BonifCfgFamilia.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGFAMILIA);
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonCfgFam.cdFamilia)) {
			sql.append(DaoUtil.ALIAS_BONIFCFGFAMILIA)
			.append(".CDFAMILIA = ")
			.append(Sql.getValue(bonCfgFam.cdFamilia))
			.append(" OR ");
		}
		sql.append(" EXISTS (SELECT 1 FROM TBLVPFAMILIAPRODUTO fam WHERE CDEMPRESA = ")
		.append(Sql.getValue(bonCfgFam.cdEmpresa))
		.append(" AND CDPRODUTO = ")
		.append(Sql.getValue(bonCfgFam.getProdutoFilter().cdProduto))
		.append(" AND ")
		.append(DaoUtil.ALIAS_BONIFCFGFAMILIA)
		.append(".CDFAMILIA = fam.CDFAMILIA ))")
		.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGFAMILIA, bonCfgFam.flGeraBonificacao, bonCfgFam.flPermiteBonificar))
		.append(") OR ");
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgFamilia.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGFAMILIA);
		sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGFAMILIA, bonCfgFam.flGeraBonificacao, bonCfgFam.flPermiteBonificar))
		.append(" ))");
	}

	private void addCompareExistsBonifCfgProd(StringBuffer sql, BonifCfgProduto bonifCfgProduto) {
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonifCfgProduto.cdProduto)) {
			sql.append("EXISTS (");
			addCompareExists(sql, BonifCfgProduto.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGPROD);
			sql.append(" AND ")
					.append(DaoUtil.ALIAS_BONIFCFGPROD)
					.append(".CDPRODUTO = ")
					.append(Sql.getValue(bonifCfgProduto.cdProduto));
			sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGPROD, bonifCfgProduto.flGeraBonificacao, bonifCfgProduto.flPermiteBonificar))
					.append(") OR ");
		}
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgProduto.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGPROD);
		sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGPROD, bonifCfgProduto.flGeraBonificacao, bonifCfgProduto.flPermiteBonificar))
				.append(" ))");
	}
	
	private void addCompareExistsBonifCfgConjunto(StringBuffer sql, BonifCfgConjunto bonifCfgConjunto) {
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonifCfgConjunto.cdConjunto)) {
			sql.append("EXISTS (");
			addCompareExists(sql, BonifCfgConjunto.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCONJUNTO);
			sql.append(" AND ")
					.append(DaoUtil.ALIAS_BONIFCFGCONJUNTO)
					.append(".CDCONJUNTO = ")
					.append(Sql.getValue(bonifCfgConjunto.cdConjunto));
			sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGCONJUNTO, bonifCfgConjunto.flGeraBonificacao, bonifCfgConjunto.flPermiteBonificar))
					.append(") OR ");
		}
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgConjunto.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCONJUNTO);
		sql.append(getFlBonificacaoClause(DaoUtil.ALIAS_BONIFCFGCONJUNTO, bonifCfgConjunto.flGeraBonificacao, bonifCfgConjunto.flPermiteBonificar))
				.append(" ))");
	}
	
	private void addCompareExistsBonifCfgCliente(StringBuffer sql, BonifCfgCliente bonCfgCliente) {
		sql.append(" AND (");
		if (ValueUtil.isNotEmpty(bonCfgCliente.cdCliente)) {
			sql.append("EXISTS (");
			addCompareExists(sql, BonifCfgCliente.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCLIENTE);
			sql.append(" AND ")
			.append(DaoUtil.ALIAS_BONIFCFGCLIENTE)
			.append(".CDCLIENTE = ")
			.append(Sql.getValue(bonCfgCliente.cdCliente));
			sql.append(") OR ");
		}
		sql.append("NOT EXISTS (");
		addCompareExists(sql, BonifCfgCliente.TABLE_NAME, DaoUtil.ALIAS_BONIFCFGCLIENTE);
		sql.append(" ))");
	}
	
	public void updateSaldoContaCorrente(BonifCfg bonifCfg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName).append(" SET ")
		.append(" QTSALDOCONTACORRENTE = ").append(Sql.getValue(bonifCfg.qtSaldoContaCorrente))
		.append(" where rowkey = ")
        .append(Sql.getValue(bonifCfg.getRowKey()));
		executeUpdate(sql.toString());
	}

}
