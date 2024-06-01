package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.validation.BonifCfgProdutoNaoEncontradoException;
import br.com.wmw.lavenderepda.business.validation.BonifCfgProdutoQuantidadeAutomaticaExcedenteException;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class BonifCfgProdutoDbxDao extends CrudDbxDao {

	private static BonifCfgProdutoDbxDao instance;

	public BonifCfgProdutoDbxDao() {
		super(BonifCfgProduto.TABLE_NAME);
	}

	public static BonifCfgProdutoDbxDao getInstance() {
		if (instance == null) {
			instance = new BonifCfgProdutoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		BonifCfgProduto bonifCfgProduto = new BonifCfgProduto();
		int i = 1;
		bonifCfgProduto.rowKey = rs.getString(i++);
		bonifCfgProduto.cdEmpresa = rs.getString(i++);
		bonifCfgProduto.cdBonifCfg = rs.getString(i++);
		bonifCfgProduto.cdProduto = rs.getString(i++);
		bonifCfgProduto.flGeraBonificacao = rs.getString(i++);
		bonifCfgProduto.flPermiteBonificar = rs.getString(i);
		return bonifCfgProduto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("tb.ROWKEY");
		sql.append(", tb.CDEMPRESA");
		sql.append(", tb.CDBONIFCFG");
		sql.append(", tb.CDPRODUTO");
		sql.append(", tb.FLGERABONIFICACAO");
		sql.append(", tb.FLPERMITEBONIFICAR");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		//nao cadastra no app
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//nao cadastra no app
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		//nao atualiza no app
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		BonifCfgProduto bonifCfgProdutoFilter = (BonifCfgProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhereByPrimaryKey(bonifCfgProdutoFilter, sqlWhereClause);
		sqlWhereClause.addAndConditionEquals("TB.FLGERABONIFICACAO", bonifCfgProdutoFilter.flGeraBonificacao);
		sqlWhereClause.addAndConditionEquals("TB.FLPERMITEBONIFICAR", bonifCfgProdutoFilter.flPermiteBonificar);
		sql.append(sqlWhereClause.getSql());
	}

	private void addWhereByPrimaryKey(BonifCfgProduto bonifCfgProdutoFilter, SqlWhereClause sqlWhereClause) {
		sqlWhereClause.addAndConditionEquals("TB.CDEMPRESA", bonifCfgProdutoFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TB.CDBONIFCFG", bonifCfgProdutoFilter.cdBonifCfg);
		sqlWhereClause.addAndConditionEquals("TB.CDPRODUTO", bonifCfgProdutoFilter.cdProduto);
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonifCfgProduto();
	}

	public BonifCfgProduto findBonifCfgProdutoBonificar(BonifCfgProduto bonifCfgProdutoFilter, BonifCfg bonifCfg) throws SQLException, BonifCfgProdutoQuantidadeAutomaticaExcedenteException, BonifCfgProdutoNaoEncontradoException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumns(bonifCfgProdutoFilter, sql);
		sql.append(" FROM ").append(BonifCfgProduto.TABLE_NAME).append(" TB ");
		sql.append(" JOIN ").append(Produto.TABLE_NAME).append(" ").append(DaoUtil.ALIAS_PRODUTO);
		sql.append(" ON TB.CDEMPRESA = ").append(DaoUtil.ALIAS_PRODUTO).append(".CDEMPRESA");
		sql.append(" AND TB.CDPRODUTO = ").append(DaoUtil.ALIAS_PRODUTO).append(".CDPRODUTO");
		sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTO).append(".CDREPRESENTANTE = ").append(Sql.getValue(bonifCfgProdutoFilter.cdRepresentanteFilter));
		addWhereByExample(bonifCfgProdutoFilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector bonifCfgProdutoList = new Vector();
			//necessario tratar retorno da query para mais de um resultado, e informar que a configuracao da regra esta errada
			while (rs.next()) {
				bonifCfgProdutoList.addElement(populate(bonifCfgProdutoFilter, rs));
			}
			if (bonifCfgProdutoList.size() > 1) {
				throw new BonifCfgProdutoQuantidadeAutomaticaExcedenteException(bonifCfgProdutoList);
			} else if (bonifCfgProdutoList.size() > 0) {
				return (BonifCfgProduto) bonifCfgProdutoList.elementAt(0);
			} else {
				throw new BonifCfgProdutoNaoEncontradoException(bonifCfg);
			}
		}
	}

}
