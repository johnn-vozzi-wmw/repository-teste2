package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class BonifCfgFaixaQtdeDbxDao extends CrudDbxDao {

	private static BonifCfgFaixaQtdeDbxDao instance;

	public BonifCfgFaixaQtdeDbxDao() {
		super(BonifCfgFaixaQtde.TABLE_NAME);
	}

	public static BonifCfgFaixaQtdeDbxDao getInstance() {
		if (instance == null) {
			instance = new BonifCfgFaixaQtdeDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		BonifCfgFaixaQtde bonifCfgFaixaQtde = new BonifCfgFaixaQtde();
		int i = 1;
		bonifCfgFaixaQtde.rowKey = rs.getString(i++);
		bonifCfgFaixaQtde.cdEmpresa = rs.getString(i++);
		bonifCfgFaixaQtde.cdBonifCfg = rs.getString(i++);
		bonifCfgFaixaQtde.cdFaixaQtde = rs.getString(i++);
		bonifCfgFaixaQtde.qtVendida = rs.getInt(i++);
		bonifCfgFaixaQtde.qtBonificada = rs.getInt(i++);
		bonifCfgFaixaQtde.flConcedeBrindes = rs.getString(i++);
		bonifCfgFaixaQtde.flConcedeMultiplos = rs.getString(i++);
		if (domainFilter != null) {
			if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
				bonifCfgFaixaQtde.qtBonificacaoAtual = rs.getInt(i);
			} else {
				bonifCfgFaixaQtde.qtBonificacaoAtual = rs.getDouble(i);
			}
		}
		return bonifCfgFaixaQtde;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("tb.ROWKEY");
		sql.append(", tb.CDEMPRESA");
		sql.append(", tb.CDBONIFCFG");
		sql.append(", tb.CDFAIXAQTDE");
		sql.append(", tb.QTVENDIDA");
		sql.append(", tb.QTBONIFICADA");
		sql.append(", tb.FLCONCEDEBRINDES");
		sql.append(", tb.FLCONCEDEMULTIPLOS");
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
		BonifCfgFaixaQtde bonifCfgFaixaQtdeFilter = (BonifCfgFaixaQtde) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhereByPrimaryKey(bonifCfgFaixaQtdeFilter, sqlWhereClause);
		sqlWhereClause.addAndConditionEquals("QTVENDIDA", bonifCfgFaixaQtdeFilter.qtVendida);
		sqlWhereClause.addAndConditionEquals("QTBONIFICADA", bonifCfgFaixaQtdeFilter.qtBonificada);
		sqlWhereClause.addAndConditionEquals("FLCONCEDEBRINDES", bonifCfgFaixaQtdeFilter.flConcedeBrindes);
		sqlWhereClause.addAndConditionEquals("FLCONCEDEMULTIPLOS", bonifCfgFaixaQtdeFilter.flConcedeMultiplos);
		sql.append(sqlWhereClause.getSql());
	}

	private void addWhereByPrimaryKey(BonifCfgFaixaQtde bonifCfgFaixaQtdeFilter, SqlWhereClause sqlWhereClause) {
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", bonifCfgFaixaQtdeFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDBONIFCFG", bonifCfgFaixaQtdeFilter.cdBonifCfg);
		sqlWhereClause.addAndConditionEquals("CDFAIXAQTDE", bonifCfgFaixaQtdeFilter.cdFaixaQtde);
	}

	public BonifCfgFaixaQtde findBonifFaixaQtdeAtingida(BonifCfgFaixaQtde bonifCfgFaixaQtdeFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(bonifCfgFaixaQtdeFilter, sql);
		sql.append(" FROM ").append(tableName).append(" tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhereByPrimaryKey(bonifCfgFaixaQtdeFilter, sqlWhereClause);
		sqlWhereClause.addAndCondition(" QTVENDIDA <= ", bonifCfgFaixaQtdeFilter.qtFaixaFilter);
		sql.append(sqlWhereClause.getSql());
		addOrderBy(sql, bonifCfgFaixaQtdeFilter);
		addLimit(sql, bonifCfgFaixaQtdeFilter);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (BonifCfgFaixaQtde) populate(null, rs);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return null;
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonifCfgFaixaQtde();
	}
	
	public Map<String, BonifCfgFaixaQtde> findBonifCfgFaixaByPedido(Pedido pedidoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(null, sql);
		if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
			sql.append(", CASE WHEN FLCONCEDEMULTIPLOS = 'S' THEN CAST((QTBONIFICACAO / tb.QTBONIFICADA) AS INT) * tb.QTBONIFICADA ELSE tb.QTBONIFICADA END AS qtBonificacaoAtual");
		} else {
			sql.append(", CASE WHEN FLCONCEDEMULTIPLOS = 'S' THEN (QTBONIFICACAO / tb.QTBONIFICADA) * tb.QTBONIFICADA ELSE tb.QTBONIFICADA END AS qtBonificacaoAtual");
		}
		sql.append(" FROM ").append(tableName).append(" tb ")
			.append(" JOIN (SELECT CDEMPRESA, CDBONIFCFG, SUM(QTBONIFICACAO) QTBONIFICACAO")
			.append(" FROM TBLVPITEMPEDIDOBONIFCFG")
			.append(" WHERE NUPEDIDO = ").append(Sql.getValue(pedidoFilter.nuPedido))
			.append(" AND CDEMPRESA = ").append(Sql.getValue(pedidoFilter.cdEmpresa))
			.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedidoFilter.cdRepresentante))
			.append(" AND FLTIPOREGISTRO = ").append(Sql.getValue(ItemPedidoBonifCfg.FLTIPOREGISTRO_CREDITO))
			.append(" GROUP BY CDEMPRESA, CDREPRESENTANTE, NUPEDIDO, CDBONIFCFG")
			.append(") itb ");
		sql.append(" ON tb.CDEMPRESA = itb.CDEMPRESA")
			.append(" AND tb.CDBONIFCFG = itb.CDBONIFCFG")
			.append(" WHERE QTBONIFICACAO >= QTBONIFICADA")
			.append(" ORDER BY tb.CDBONIFCFG, QTBONIFICADA DESC");
		Map<String, BonifCfgFaixaQtde> result = new HashMap<>();
		
		//Deve retornar a maior faixa atingida por cdBonifCfg
		List<String> cdBonifCfgList = new ArrayList<>();
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				BonifCfgFaixaQtde bonifCfgFaixaQtde = (BonifCfgFaixaQtde) populate(pedidoFilter, rs);
				if (!cdBonifCfgList.contains(bonifCfgFaixaQtde.cdBonifCfg)) {
					cdBonifCfgList.add(bonifCfgFaixaQtde.cdBonifCfg);
					bonifCfgFaixaQtde.qtBonificacaoAtual = rs.getDouble("qtBonificacaoAtual");
					result.put(bonifCfgFaixaQtde.cdBonifCfg, bonifCfgFaixaQtde);
				}
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return result;
	}
}
