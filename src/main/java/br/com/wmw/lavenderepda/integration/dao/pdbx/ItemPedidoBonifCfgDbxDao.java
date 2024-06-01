package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemPedidoBonifCfgDbxDao extends CrudDbxDao {

	private static ItemPedidoBonifCfgDbxDao instance;
	
	public ItemPedidoBonifCfgDbxDao() {
		super(ItemPedidoBonifCfg.TABLE_NAME);
	}
	
	public static ItemPedidoBonifCfgDbxDao getInstance() {
		if (instance == null) {
			instance = new ItemPedidoBonifCfgDbxDao();
		}
		return instance;
	}
	
	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoBonifCfg();
	}
	
	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg();
		itemPedBon.rowKey = rs.getString("rowkey");
		itemPedBon.cdEmpresa = rs.getString("cdEmpresa");
		itemPedBon.cdRepresentante = rs.getString("cdRepresentante");
		itemPedBon.flOrigemPedido = rs.getString("flOrigemPedido");
		itemPedBon.nuPedido = rs.getString("nuPedido");
		itemPedBon.cdProduto = rs.getString("cdProduto");
		itemPedBon.flTipoItemPedido = rs.getString("flTipoItemPedido");
		itemPedBon.nuSeqProduto = rs.getInt("nuSeqProduto");
		itemPedBon.cdBonifCfg = rs.getString("cdBonifCfg");
		itemPedBon.qtBonificacao = rs.getDouble("qtBonificacao");
		itemPedBon.vlBonificacao = rs.getDouble("vlBonificacao");
		itemPedBon.flTipoRegistro = rs.getString("flTipoRegistro");
		itemPedBon.flBonificacaoAutomatica = rs.getString("flBonificacaoAutomatica");
		itemPedBon.flTipoAlteracao = rs.getString("flTipoAlteracao");
		itemPedBon.cdUsuario = rs.getString("cdUsuario");
		itemPedBon.setBonifCfgBrinde(rs.getDouble("qtBrinde"), rs.getString("flOpcional"));
		return itemPedBon;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.FLORIGEMPEDIDO,");
		sql.append(" tb.NUPEDIDO,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.FLTIPOITEMPEDIDO,");
		sql.append(" tb.NUSEQPRODUTO,");
		sql.append(" tb.CDBONIFCFG,");
		sql.append(" tb.QTBONIFICACAO,");
		sql.append(" tb.VLBONIFICACAO,");
		sql.append(" tb.FLTIPOREGISTRO,");
		sql.append(" tb.FLBONIFICACAOAUTOMATICA,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(DaoUtil.ALIAS_BONIFCFGBRINDE + ".QTBRINDE,");
		sql.append(DaoUtil.ALIAS_BONIFCFGBRINDE + ".FLOPCIONAL");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDBONIFCFG,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" QTBONIFICACAO,");
        sql.append(" VLBONIFICACAO,");
        sql.append(" FLBONIFICACAOAUTOMATICA, ");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain;
		sql.append(Sql.getValue(itemPedBon.cdEmpresa)).append(",");
		sql.append(Sql.getValue(itemPedBon.cdRepresentante)).append(",");
		sql.append(Sql.getValue(itemPedBon.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(itemPedBon.nuPedido)).append(",");
		sql.append(Sql.getValue(itemPedBon.cdProduto)).append(",");
		sql.append(Sql.getValue(itemPedBon.flTipoItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedBon.nuSeqProduto)).append(",");
		sql.append(Sql.getValue(itemPedBon.cdBonifCfg)).append(",");
		sql.append(Sql.getValue(itemPedBon.flTipoRegistro)).append(",");
		sql.append(Sql.getValue(itemPedBon.qtBonificacao)).append(",");
		sql.append(Sql.getValue(itemPedBon.vlBonificacao)).append(",");
		sql.append(Sql.getValue(itemPedBon.flBonificacaoAutomatica)).append(",");
		sql.append(Sql.getValue(itemPedBon.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
		sql.append(Sql.getValue(itemPedBon.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain;
		sql.append(" FLTIPOREGISTRO = ").append(Sql.getValue(itemPedBon.flTipoRegistro)).append(",");
		sql.append(" QTBONIFICACAO = ").append(Sql.getValue(itemPedBon.qtBonificacao)).append(",");
		sql.append(" VLBONIFICACAO = ").append(Sql.getValue(itemPedBon.vlBonificacao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		addWhereByExampleAlias(domain, sql, true);
	}
	
	protected void addWhereByExampleAlias(BaseDomain domain, StringBuffer sql, boolean isUseAlias) {
		String alias = isUseAlias ? "tb." : "";
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(alias + "CDEMPRESA", itemPedBon.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(alias + "CDREPRESENTANTE", itemPedBon.cdRepresentante);
		sqlWhereClause.addAndConditionEquals(alias + "FLORIGEMPEDIDO", itemPedBon.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals(alias + "NUPEDIDO", itemPedBon.nuPedido);
		sqlWhereClause.addAndConditionEquals(alias + "CDPRODUTO", itemPedBon.cdProduto);
		sqlWhereClause.addAndConditionEquals(alias + "FLTIPOITEMPEDIDO", itemPedBon.flTipoItemPedido);
		sqlWhereClause.addAndConditionEquals(alias + "CDBONIFCFG", itemPedBon.cdBonifCfg);
		sqlWhereClause.addAndConditionEquals(alias + "FLTIPOREGISTRO", itemPedBon.flTipoRegistro);
		sqlWhereClause.addAndConditionNotIn(alias + "FLTIPOREGISTRO", itemPedBon.notInFlTipoRegistro);
		sqlWhereClause.addAndConditionEquals(DaoUtil.ALIAS_BONIFCFGBRINDE + ".FLOPCIONAL", itemPedBon.getBonifCfgBrinde().flOpcional);
		sqlWhereClause.addAndConditionEquals("FLBONIFICACAOAUTOMATICA", itemPedBon.flBonificacaoAutomatica);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector getSumVlBonicacaoByBonifCfgPedido(Pedido pedido) throws SQLException {
		StringBuffer sb = getSqlBuffer();
		sb.append("select tb.cdempresa, tb.cdrepresentante, tb.florigempedido, tb.nupedido, tb.cdbonifcfg, BCFG.dsbonifcfg, BCFG.flOpcional, BCFG.CDTIPOREGRABONIFICACAO, BCFG.QTSALDOCONTACORRENTE, ")
			.append("sum(tb.vlbonificacao * (case tb.fltiporegistro when 'D' then -1 when 'B' THEN 0 else 1 end)) vlbonificacao, ")
			.append("sum(tb.QTBONIFICACAO * (case tb.fltiporegistro when 'D' then -1 when 'B' THEN 0 else 1 end)) sumQtBonificacao from ")
			.append(tableName).append(" tb ");
		DaoUtil.addJoinBonifCfg(sb, DaoUtil.ALIAS_BONIFCFG);
		DaoUtil.addJoinBonifCfgBrinde(sb, DaoUtil.ALIAS_BONIFCFGBRINDE);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", pedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", pedido.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", pedido.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", pedido.flOrigemPedido);
		sb.append(sqlWhereClause.getSql());
		sb.append(" group by tb.cdempresa, tb.cdrepresentante, tb.florigempedido, tb.nupedido, tb.cdbonifcfg, BCFG.flOpcional, BCFG.CDTIPOREGRABONIFICACAO");
		
		Vector result = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sb.toString())) {
			while (rs.next()) {
				result.addElement(populateBonifCfgPedido(rs));
			}	
			return result;
		}
		
	}
	
	protected BonifCfg populateBonifCfgPedido(ResultSet rs) throws SQLException {
		BonifCfg bonifCfgPed = new BonifCfg(rs.getString("cdEmpresa"), rs.getString("cdBonifCfg"));
		bonifCfgPed.dsBonifCfg = rs.getString("dsBonifCfg");
		bonifCfgPed.sumVlBonificacao = ValueUtil.round(rs.getDouble("vlBonificacao"));
		bonifCfgPed.sumQtBonificacao = rs.getDouble("sumQtBonificacao");
		bonifCfgPed.flOpcional = rs.getString("flOpcional");
		bonifCfgPed.cdTipoRegraBonificacao = rs.getString("CDTIPOREGRABONIFICACAO");
		bonifCfgPed.qtSaldoContaCorrente = rs.getDouble("qtSaldoContaCorrente");
		return bonifCfgPed;
	}
	
	public Vector findAllItemPedidoBonif(ItemPedidoBonifCfg itemPedBon) throws SQLException {
		boolean isSupervisor = SessionLavenderePda.isUsuarioSupervisor();
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(itemPedBon, sql);
		sql.append(", ")
			.append(DaoUtil.ALIAS_PRODUTO).append(".DSPRODUTO, ")
			.append(DaoUtil.ALIAS_BONIFCFG).append(".VLPCTSOBREVENDA, ")
			.append(DaoUtil.ALIAS_BONIFCFG).append(".CDTIPOREGRABONIFICACAO")
			.append(" FROM ").append(tableName).append(" tb ");
		sql.append(" JOIN TBLVPPRODUTO ").append(DaoUtil.ALIAS_PRODUTO).append(" ON ");
		sql.append(DaoUtil.ALIAS_PRODUTO).append(".CDEMPRESA = tb.CDEMPRESA AND ").append(DaoUtil.ALIAS_PRODUTO).append(".CDPRODUTO = tb.CDPRODUTO ");
		if (isSupervisor) {
			sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTO).append(".CDREPRESENTANTE = ")
				.append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class)));
		} else {
			sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTO).append(".CDREPRESENTANTE = tb.CDREPRESENTANTE ");
		}
		DaoUtil.addJoinBonifCfg(sql, DaoUtil.ALIAS_BONIFCFG);
		DaoUtil.addJoinBonifCfgBrinde(sql, DaoUtil.ALIAS_BONIFCFGBRINDE);
		addWhereByBonifCfgPedido(sql, itemPedBon);
		addOrderBy(sql, itemPedBon);
		
		Vector result = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			ItemPedidoBonifCfg itemPedBonResult;
			while (rs.next()) {
				itemPedBonResult = (ItemPedidoBonifCfg) populate(null, rs);
				itemPedBonResult.getProduto().dsProduto = rs.getString("dsProduto");
				itemPedBonResult.getBonifCfg().vlPctSobreVenda = rs.getDouble("vlPctSobreVenda");
				itemPedBonResult.getBonifCfg().cdTipoRegraBonificacao = rs.getString("cdTipoRegraBonificacao");
				result.addElement(itemPedBonResult);
			}
			return result;
		}
	}
	
	public double getVlSaldoBonificacao(ItemPedidoBonifCfg itemPedBon) throws SQLException {
		StringBuffer sb = getSqlBuffer();
		sb.append("select SUM(CASE WHEN FLTIPOREGISTRO = 'D' THEN -1 ELSE 1 END * VLBONIFICACAO) SUMVLBONIFICACAO from ")
		.append(tableName).append(" tb ");
		addWhereByBonifCfgPedido(sb, itemPedBon);
		sb.append(" GROUP BY CDEMPRESA, CDREPRESENTANTE, FLORIGEMPEDIDO, NUPEDIDO, CDBONIFCFG");
		return getDouble(sb.toString());
	}
	
	private void addWhereByBonifCfgPedido(StringBuffer sb, ItemPedidoBonifCfg itemPedBon) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause(); 
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", itemPedBon.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", itemPedBon.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", itemPedBon.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", itemPedBon.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.CDBONIFCFG", itemPedBon.cdBonifCfg);
		sb.append(sqlWhereClause.getSql());
	}

	public Double[] sumBonificacaoBonifCfgValorQtd(ItemPedidoBonifCfg itemPedidoBonifCfgFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT " + DaoUtil.ALIAS_BONIFCFG + ".CDTIPOREGRABONIFICACAO, ")
		.append("SUM(TB.VLBONIFICACAO) AS SUMVLBONIFICACAO, ")
		.append("CAST(SUM(TB.QTBONIFICACAO) AS DOUBLE) AS SUMQTBONIFICACAO")
		.append(" FROM ").append(tableName).append(" TB ");
		DaoUtil.addJoinBonifCfg(sql, DaoUtil.ALIAS_BONIFCFG);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("TB.CDEMPRESA", itemPedidoBonifCfgFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("TB.CDREPRESENTANTE", itemPedidoBonifCfgFilter.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("TB.FLORIGEMPEDIDO", itemPedidoBonifCfgFilter.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("TB.NUPEDIDO", itemPedidoBonifCfgFilter.nuPedido);
		sqlWhereClause.addAndConditionEquals("TB.CDPRODUTO", itemPedidoBonifCfgFilter.cdProduto);
		sqlWhereClause.addAndConditionEquals("TB.FLTIPOITEMPEDIDO", itemPedidoBonifCfgFilter.flTipoItemPedido);
		sqlWhereClause.addAndConditionIn("TB.FLTIPOREGISTRO", itemPedidoBonifCfgFilter.inFlTipoRegistro);
		sqlWhereClause.addAndConditionIn(DaoUtil.ALIAS_BONIFCFG + ".CDTIPOREGRABONIFICACAO", new String[] {BonifCfg.CDTIPOREGRA_QTDE, BonifCfg.CDTIPOREGRA_VALOR});
		sql.append(sqlWhereClause.getSql());
		sql.append(" GROUP BY " + DaoUtil.ALIAS_BONIFCFG + ".CDTIPOREGRABONIFICACAO");
		
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Double[] result = null;
			//query pode retornar até duas linhas com duas colunas, porem em cada linha somente uma coluna interessa
			while (rs.next()) {
				String cdTipoRegra = rs.getString("CDTIPOREGRABONIFICACAO");
				result = new Double[2];
				if (ValueUtil.valueEquals(cdTipoRegra, BonifCfg.CDTIPOREGRA_VALOR)) {
					result[0] = rs.getDouble("SUMVLBONIFICACAO");
				} else if (ValueUtil.valueEquals(cdTipoRegra, BonifCfg.CDTIPOREGRA_QTDE)) {
					result[1] = rs.getDouble("SUMQTBONIFICACAO");
				}
			}
			return result;
		}
	}

	public ItemPedidoBonifCfg findItemPedidoBonifByExample(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domain, sql);
		addWhereByExample(domain, sql);
		addGroupBy(sql);
		addOrderBy(sql, domain);
		addLimit(sql, domain);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (ItemPedidoBonifCfg) populate(domain, rs);
			}
		}
		return null;
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		DaoUtil.addJoinBonifCfgBrinde(sql, DaoUtil.ALIAS_BONIFCFGBRINDE);
	}
	
	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		addWhereByExampleAlias(domain, sql, false);
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		//Ordem: 1.Creditos  2.Debitos  3.Brindes
		domain.sortAtributte = ItemPedidoBonifCfg.NMCAMPOTIPOITEMPEDIDO + "," + ItemPedidoBonifCfg.NMCAMPOTIPOREGISTRO;
		domain.sortAsc = ValueUtil.VALOR_NAO + "," + ValueUtil.VALOR_NAO;
		super.addOrderBy(sql, domain);
	}

	public Double[] sumQtTotalBonificadoAutomaticoEBrindesPorItemPedido(ItemPedidoBonifCfg itemPedidoBonifCfgFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM( CASE WHEN TB.FLTIPOREGISTRO || BCFGBRIN.FLOPCIONAL = ")
			.append(Sql.getValue(ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE + ValueUtil.VALOR_NAO))
			.append(" THEN QTBRINDE ELSE 0 END) AS QTBRINDEOBRIGATORIO,");
		sql.append(" SUM(CASE WHEN TB.FLBONIFICACAOAUTOMATICA = ")
			.append(Sql.getValue(ValueUtil.VALOR_SIM))
			.append(" THEN QTBONIFICACAO ELSE 0 END) AS QTBONIFICACAOAUTOMATICA");
		sql.append(" FROM ").append(tableName).append(" TB ");
		addJoin(itemPedidoBonifCfgFilter, sql);
		addWhereByExample(itemPedidoBonifCfgFilter, sql);
		Double[] sumResult = new Double[2];
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				sumResult[0] = rs.getDouble(1);
				sumResult[1] = rs.getDouble(2);
				return sumResult;
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return null;
	}


	public double sumByQtBrinde(BaseDomain domain) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(QTBRINDE) AS SOMA FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domain, sql);
		addWhereByExample(domain, sql);
		return getDouble(sql.toString());
	}
	
	public int findNuSeqProdutoBonifCfg(ItemPedidoBonifCfg filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(NUSEQPRODUTO) FROM TBLVPITEMPEDIDO tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", filter.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", filter.flOrigemPedido);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", filter.cdProduto);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMPEDIDO = ", TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		sqlWhereClause.addAndConditionForced("COALESCE(tb.CDKIT, '') = ", ValueUtil.VALOR_NI);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
	}
	
	public int findNuSeqProdutoBonificacaoAvulsa(ItemPedidoBonifCfg filter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(NUSEQPRODUTO) FROM TBLVPITEMPEDIDO TB");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", filter.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", filter.flOrigemPedido);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", filter.cdProduto);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMPEDIDO = ", TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
	}
	
	public int countItemPedidoContaCorrente(ItemPedidoBonifCfg itemPedidoBonifCfg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM ").append(tableName).append(" tb ");
		DaoUtil.addJoinBonifCfg(sql, DaoUtil.ALIAS_BONIFCFG);
		addWhereByExample(itemPedidoBonifCfg, sql);
		sql.append(" AND ").append(DaoUtil.ALIAS_BONIFCFG).append(".CDTIPOREGRABONIFICACAO = ")
		.append(Sql.getValue(BonifCfg.CDTIPOREGRA_CONTACORRENTE));
		return getInt(sql.toString());
	}
	
	public void updateFlAlteracao(ItemPedidoBonifCfg itemPedidoBonifCfg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" update ");
        sql.append(tableName);
        sql.append(" set ")
        .append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoBonifCfg.flTipoAlteracao));
        addWhereByExampleAlias(itemPedidoBonifCfg, sql, false);
        executeUpdate(sql.toString());
	}

	public void deleteItemPedidoBonigCfgByItemPedido(ItemPedido itemPedido) {
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM ");
		sql.append(tableName).append(" WHERE");
		sql.append(" CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante));
		sql.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido));
		sql.append(" AND CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto));
		sql.append(" AND FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido));
		sql.append(" AND NUSEQPRODUTO = ").append(Sql.getValue(itemPedido.nuSeqProduto));
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

}