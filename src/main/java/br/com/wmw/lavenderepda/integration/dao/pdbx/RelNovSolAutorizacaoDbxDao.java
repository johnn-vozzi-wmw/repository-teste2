package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.RelNovSolAutorizacao;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;


public class RelNovSolAutorizacaoDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelNovSolAutorizacao();
	}

	private static RelNovSolAutorizacaoDbxDao instance;

	public RelNovSolAutorizacaoDbxDao() {
		super(RelNovSolAutorizacao.TABLE_NAME);
	}

	public static RelNovSolAutorizacaoDbxDao getInstance() {
		if (instance == null) {
			instance = new RelNovSolAutorizacaoDbxDao();
		}
		return instance;
	}


	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao = new RelNovSolAutorizacao();
		relNovSolAutorizacao.rowKey = rs.getString("rowkey");
		relNovSolAutorizacao.cdEmpresa = rs.getString("cdEmpresa");
		relNovSolAutorizacao.cdRepresentante = rs.getString("cdRepresentante");
		relNovSolAutorizacao.cdSolAutorizacao = rs.getString("cdSolAutorizacao");
		relNovSolAutorizacao.cdTipoSolAutorizacao = rs.getInt("cdTipoSolAutorizacao");
		relNovSolAutorizacao.flOrigemPedido = rs.getString("flOrigemPedido");
		relNovSolAutorizacao.cdTipoNovidade = rs.getString("cdTipoNovidade");
		relNovSolAutorizacao.nuPedido = rs.getString("nuPedido");
		relNovSolAutorizacao.cdCliente = rs.getString("cdCliente");
		relNovSolAutorizacao.cdProduto = rs.getString("cdProduto");
		relNovSolAutorizacao.flTipoItemPedido = rs.getString("flTipoItemPedido");
		relNovSolAutorizacao.nuSeqProduto = rs.getInt("nuSeqProduto");
		relNovSolAutorizacao.flAutorizado = rs.getString("flAutorizado");
		relNovSolAutorizacao.dtEmissaoRelatorio = rs.getDate("dtEmissaoRelatorio");
		relNovSolAutorizacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		relNovSolAutorizacao.dsProduto = rs.getString("dsProduto");
		return relNovSolAutorizacao;
	}

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		RelNovSolAutorizacao relNovSolAutorizacao = (RelNovSolAutorizacao) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.getRowKey());
		}
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdEmpresa);
		}
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdRepresentante);
		}
		if ("CDSOLAUTORIZACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdSolAutorizacao);
		}
		if ("CDTIPOSOLAUTORIZACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdTipoSolAutorizacao);
		}
		if ("FLORIGEMPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.flOrigemPedido);
		}
		if ("CDTIPONOVIDADE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdTipoNovidade);
		}
		if ("NUPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.nuPedido);
		}
		if ("CDCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdCliente);
		}
		if ("CDPRODUTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdProduto);
		}
		if ("FLTIPOITEMPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.flTipoItemPedido);
		}
		if ("NUSEQPRODUTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.nuSeqProduto);
		}
		if ("FLAUTORIZADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.flAutorizado);
		}
		if ("DTEMISSAORELATORIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.dtEmissaoRelatorio);
		}
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.cdUsuario);
		}
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(relNovSolAutorizacao.flTipoAlteracao);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDSOLAUTORIZACAO,");
		sql.append(" tb.CDTIPOSOLAUTORIZACAO,");
		sql.append(" tb.FLORIGEMPEDIDO,");
		sql.append(" tb.CDTIPONOVIDADE,");
		sql.append(" tb.NUPEDIDO,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.FLTIPOITEMPEDIDO,");
		sql.append(" tb.NUSEQPRODUTO,");
		sql.append(" tb.FLAUTORIZADO,");
		sql.append(" tb.DTEMISSAORELATORIO,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" prod.DSPRODUTO");
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {

	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		DaoUtil.addJoinProduto(sql, "prod", false);
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao = (RelNovSolAutorizacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", relNovSolAutorizacao.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", relNovSolAutorizacao.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDSOLAUTORIZACAO = ", relNovSolAutorizacao.cdSolAutorizacao);
		sqlWhereClause.addAndCondition("tb.CDTIPOSOLAUTORIZACAO = ", relNovSolAutorizacao.cdTipoSolAutorizacao);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", relNovSolAutorizacao.flOrigemPedido);
		sqlWhereClause.addAndCondition("tb.CDTIPONOVIDADE = ", relNovSolAutorizacao.cdTipoNovidade);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", relNovSolAutorizacao.nuPedido);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", relNovSolAutorizacao.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", relNovSolAutorizacao.cdProduto);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMPEDIDO = ", relNovSolAutorizacao.flTipoItemPedido);
		sqlWhereClause.addAndCondition("tb.NUSEQPRODUTO = ", relNovSolAutorizacao.nuSeqProduto);
		sqlWhereClause.addAndCondition("tb.FLAUTORIZADO = ", relNovSolAutorizacao.flAutorizado);
		sqlWhereClause.addAndCondition("tb.DTEMISSAORELATORIO = ", relNovSolAutorizacao.dtEmissaoRelatorio);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findAllByExampleQuantidade(RelNovSolAutorizacao relNovSolAutorizacao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) as qtRegistro, tb.cdTipoNovidade ");
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" tb GROUP BY tb.cdTipoNovidade");
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				RelNovSolAutorizacao domain = new RelNovSolAutorizacao();
				domain.cdEmpresa = relNovSolAutorizacao.cdEmpresa;
				domain.cdRepresentante = relNovSolAutorizacao.cdRepresentante;
				domain.qtRegistrosTipoNovidade = rs.getInt("qtRegistro");
				domain.cdTipoNovidade = rs.getString("cdTipoNovidade");
				list.addElement(domain);
			}
		}
		return list;
	}

	public void deleteAllByDtEmissao(RelNovSolAutorizacao relNovSolAutorizacao) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" delete from ").append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" DTEMISSAORELATORIO <= ", relNovSolAutorizacao.dtEmissaoRelatorio);
		sql.append(sqlWhereClause.getSql());
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException ignored) {
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

}
