package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.DescProgFamilia;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescProgConfigFamDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgConfigFam();
	}

    private static DescProgConfigFamDbxDao instance;

    public DescProgConfigFamDbxDao() { super(DescProgConfigFam.TABLE_NAME); }
    public static DescProgConfigFamDbxDao getInstance() { return (instance == null) ? instance = new DescProgConfigFamDbxDao() : instance; }
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    DescProgConfigFam descProgConfigFam = new DescProgConfigFam();
	    descProgConfigFam.rowKey = rs.getString("rowkey");
	    descProgConfigFam.cdEmpresa = rs.getString("cdEmpresa");
	    descProgConfigFam.cdRepresentante = rs.getString("cdRepresentante");
	    descProgConfigFam.cdDescProgressivo = rs.getString("cdDescProgressivo");
	    descProgConfigFam.cdFamiliaDescProg = rs.getString("cdFamiliaDescProg");
	    descProgConfigFam.flTipoFamiliaPro = rs.getString("flTipoFamiliaPro");
	    descProgConfigFam.flTipoFamiliaCon = rs.getString("flTipoFamiliaCon");
	    descProgConfigFam.flFamAcuValorMin = rs.getString("flFamAcuValorMin");
		descProgConfigFam.flFamAcuValorMax = rs.getString("flFamAcuValorMax");
	    descProgConfigFam.cdUsuario = rs.getString("cdUsuario");
	    populateDescProgFamilia(rs, descProgConfigFam);
	    return descProgConfigFam;
    }

	private void populateDescProgFamilia(ResultSet rs, DescProgConfigFam descProgConfigFam) throws SQLException {
		descProgConfigFam.descProgFamilia = new DescProgFamilia();
		descProgConfigFam.descProgFamilia.cdEmpresa = descProgConfigFam.cdEmpresa;
		descProgConfigFam.descProgFamilia.cdRepresentante = descProgConfigFam.cdRepresentante;
		descProgConfigFam.descProgFamilia.cdFamiliaDescProg = descProgConfigFam.cdFamiliaDescProg;
		descProgConfigFam.descProgFamilia.dsFamiliaProd = rs.getString("dsFamiliaProd");
	}

	public Vector findAllFamByDescProg(DescProgConfigFam filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDDESCPROGRESSIVO,");
		sql.append(" tb.CDFAMILIADESCPROG,");
		sql.append(" tb.FLTIPOFAMILIAPRO,");
		sql.append(" tb.FLTIPOFAMILIACON,");
		sql.append(" tb.FLFAMACUVALORMIN,");
		sql.append(" tb.FLFAMACUVALORMAX,");
		sql.append(" tb.CDUSUARIO, ");
		sql.append(" DESCPROGFAMILIA.DSFAMILIAPROD, ");
		sql.append(" (SELECT COUNT(1) AS CNT FROM TBLVPPRODUTO PROD WHERE PROD.CDEMPRESA = TB.CDEMPRESA AND PROD.CDREPRESENTANTE = TB.CDREPRESENTANTE AND PROD.CDFAMILIADESCPROG = TB.CDFAMILIADESCPROG) AS QTPRODUTOS, ");
		boolean usaPedido = filter.pedidoFilter != null;
		if (filter.isFlTipoFamiliaCon()) {
			sql.append(getSubSqlTipoFamilia("(", getSubSqlTipoErp(filter.cliente), "AND  TC.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO AND TB.FLTIPOFAMILIACON = ", " ) AS FLCONSUMIUERP, "));
			if (usaPedido) {
				sql.append(getSubSqlTipoFamilia("(", getSubSqlTipoPda(filter.pedidoFilter, true), "AND IT.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO AND TB.FLTIPOFAMILIACON = ", " ) AS FLCONSUMIUPDA, "));
				if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
					sql.append(getSubSqlTipoFamilia("(", getSubSqlTipoPda(filter.pedidoFilter, false), "AND IT.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO AND TB.FLTIPOFAMILIACON = ", " ) AS FLCONSUMIUPDAOUTROS, "));
				}
			}
		} else {
			sql.append(addFlagFalse(usaPedido, " 'N' AS FLCONSUMIUERP, 'N' AS FLCONSUMIUPDAOUTROS, ", " 'N' AS FLCONSUMIUPDA, "));
		}

		if (filter.isFlTipoFamiliaPro()) {
			sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoErp(filter.cliente), " AND TB.FLTIPOFAMILIAPRO = ", " ) AS FLPRODUZIUERP, "));
			if (usaPedido) {
				sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoPda(filter.pedidoFilter, true), " AND TB.FLTIPOFAMILIAPRO = ", " ) AS FLPRODUZIUPDA, "));
				if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
					sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoPda(filter.pedidoFilter, false), " AND TB.FLTIPOFAMILIAPRO = ", " ) AS FLPRODUZIUPDAOUTROS, "));
				}
			}
		} else {
			sql.append(addFlagFalse(usaPedido, " 'N' AS FLPRODUZIUERP, 'N' AS FLPRODUZIUPDAOUTROS, ", " 'N' AS FLPRODUZIUPDA, "));
		}

		if (filter.isFlFamAcuValorMin()) {
			sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoErp(filter.cliente), " AND TB.FLFAMACUVALORMIN = ", " ) AS FLACUMULOUERP, "));
			if (usaPedido) {
				sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoPda(filter.pedidoFilter, true), " AND TB.FLFAMACUVALORMIN = ", " ) AS FLACUMULOUPDA, "));
				if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
					sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoPda(filter.pedidoFilter, false), " AND TB.FLFAMACUVALORMIN = ", " ) AS FLACUMULOUPDAOUTROS, "));
				}
			}
		} else {
			sql.append(addFlagFalse(usaPedido, " 'N' AS FLACUMULOUERP, 'N' AS FLACUMULOUPDAOUTROS, ", "'N' AS FLACUMULOUPDA, "));
		}

		if (filter.isFlFamAcuValorMax()) {
			sql.append(getSubSqlTipoFamilia(" ( ", getSubSqlTipoErp(filter.cliente), " AND TB.FLFAMACUVALORMAX = ", " ) AS FLACUMULOUMAXERP "));
			if (usaPedido) {
				sql.append(getSubSqlTipoFamilia(" , ( ", getSubSqlTipoPda(filter.pedidoFilter, true), " AND TB.FLFAMACUVALORMAX = ", " ) AS FLACUMULOUMAXPDA "));
				if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
					sql.append(getSubSqlTipoFamilia(" , ( ", getSubSqlTipoPda(filter.pedidoFilter, false), " AND TB.FLFAMACUVALORMAX = ", " ) AS FLACUMULOUMAXPDAOUTROS "));
				}
			}
		} else {
			sql.append(addFlagFalse(usaPedido, " 'N' AS FLACUMULOUMAXERP, 'N' AS FLACUMULOUMAXPDAOUTROS ", ", 'N' AS FLACUMULOUMAXPDA "));
		}

		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoinSummary(filter, sql);
		addWhereByExample(filter, sql);
		addGroupBySummary(filter, sql);
		addOrderBy(sql, filter);
		addLimit(sql, filter);
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				DescProgConfigFam descProgConfigFam = new DescProgConfigFam();
				descProgConfigFam.rowKey = rs.getString("rowkey");
				descProgConfigFam.cdEmpresa = rs.getString("cdEmpresa");
				descProgConfigFam.cdRepresentante = rs.getString("cdRepresentante");
				descProgConfigFam.cdDescProgressivo = rs.getString("cdDescProgressivo");
				descProgConfigFam.cdFamiliaDescProg = rs.getString("cdFamiliaDescProg");
				descProgConfigFam.flTipoFamiliaPro = rs.getString("flTipoFamiliaPro");
				descProgConfigFam.flTipoFamiliaCon = rs.getString("flTipoFamiliaCon");
				descProgConfigFam.flFamAcuValorMin = rs.getString("flFamAcuValorMin");
				descProgConfigFam.flFamAcuValorMax = rs.getString("flFamAcuValorMax");
				descProgConfigFam.qtProdutos = rs.getInt("qtProdutos");
				descProgConfigFam.flProduziuErp = rs.getString("flProduziuErp");
				descProgConfigFam.flConsumiuErp = rs.getString("flConsumiuErp");
				descProgConfigFam.flAcumulouErp = rs.getString("flAcumulouErp");
				descProgConfigFam.flAcumulouMaxErp = rs.getString("flAcumulouMaxErp");
				if (usaPedido) {
					if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
						descProgConfigFam.flProduziuPdaOutros = rs.getString("flProduziuPdaOutros");
						descProgConfigFam.flConsumiuPdaOutros = rs.getString("flConsumiuPdaOutros");
						descProgConfigFam.flAcumulouPdaOutros = rs.getString("flAcumulouPdaOutros");
						descProgConfigFam.flAcumulouMaxPdaOutros = rs.getString("flAcumulouMaxPdaOutros");
					}
					descProgConfigFam.flProduziuPda = rs.getString("flProduziuPda");
					descProgConfigFam.flConsumiuPda = rs.getString("flConsumiuPda");
					descProgConfigFam.flAcumulouPda = rs.getString("flAcumulouPda");
					descProgConfigFam.flAcumulouMaxPda = rs.getString("flAcumulouMaxPda");
				}
				descProgConfigFam.cdUsuario = rs.getString("cdUsuario");
				populateDescProgFamilia(rs, descProgConfigFam);
				result.addElement(descProgConfigFam);
			}
			return result;
		}

	}

	private String addFlagFalse(boolean usaPedido, String erp, String pda) {
		StringBuffer sql = getSqlBuffer();
		sql.append(erp);
		if (usaPedido) {
			sql.append(pda);
		}
		return sql.toString();
	}

	private String getSubSqlTipoFamilia(String open, String subSqlTipoFamilia, String condition, String close) {
		StringBuffer sql = getSqlBuffer();
		sql.append(open);
		sql.append(subSqlTipoFamilia);
		sql.append(condition).append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append(close);
		return sql.toString();
	}

	private String getSubSqlTipoPda(Pedido pedido, boolean ignoraOutrosPedidosAbertos) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT CASE WHEN COUNT(1) > 0 THEN 'S' ELSE 'N' END FROM TBLVPITEMPEDIDO IT ");
		sql.append("         JOIN TBLVPDESCPROGRESSIVOCONFIG DESCPROGRESSIVOCONFIG ON ");
		sql.append("            DESCPROGRESSIVOCONFIG.CDEMPRESA = IT.CDEMPRESA ");
		sql.append("        AND DESCPROGRESSIVOCONFIG.CDREPRESENTANTE = IT.CDREPRESENTANTE ");
		sql.append("        AND DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ");
		sql.append("         JOIN TBLVPPEDIDO PEDIDOPDA ON ");
		sql.append("            PEDIDOPDA.CDEMPRESA = IT.CDEMPRESA ");
		sql.append("        AND PEDIDOPDA.CDREPRESENTANTE = IT.CDREPRESENTANTE ");
		sql.append("        AND PEDIDOPDA.NUPEDIDO = IT.NUPEDIDO ");
		sql.append("        AND PEDIDOPDA.FLORIGEMPEDIDO = IT.FLORIGEMPEDIDO ");
		sql.append("         JOIN TBLVPPRODUTO PRODUTO ON ");
		sql.append("            PRODUTO.CDEMPRESA = IT.CDEMPRESA ");
		sql.append("        AND PRODUTO.CDREPRESENTANTE = IT.CDREPRESENTANTE ");
		sql.append("        AND PRODUTO.CDPRODUTO = IT.CDPRODUTO ");
		sql.append("        AND PRODUTO.CDFAMILIADESCPROG = tb.CDFAMILIADESCPROG ");
		sql.append("        WHERE IT.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("          AND IT.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append(DescProgressivoConfigDbxDao.addItemPedidoRequirements("IT"));
		sql.append("          AND PEDIDOPDA.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append("          AND PEDIDOPDA.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		if (ignoraOutrosPedidosAbertos) {
			sql.append("          AND PEDIDOPDA.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		} else {
			sql.append("          AND PEDIDOPDA.NUPEDIDO <> ").append(Sql.getValue(pedido.nuPedido));
		}
		sql.append("          AND PEDIDOPDA.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(pedido.cdCliente, "PEDIDOPDA", null, false, false, true));
		return sql.toString();
	}

	private String getSubSqlTipoErp(Cliente cliente) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT CASE WHEN COUNT(1) > 0 THEN 'S' ELSE 'N' END FROM TBLVPITEMPEDIDOERP TC ");
		sql.append("         JOIN TBLVPDESCPROGRESSIVOCONFIG DESCPROGRESSIVOCONFIG ON ");
		sql.append("            DESCPROGRESSIVOCONFIG.CDEMPRESA = TC.CDEMPRESA ");
		sql.append("        AND DESCPROGRESSIVOCONFIG.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
		sql.append("        AND DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ");
		sql.append("         JOIN TBLVPPEDIDOERP PEDIDO ON ");
		sql.append("            PEDIDO.CDEMPRESA = TC.CDEMPRESA ");
		sql.append("        AND PEDIDO.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
		sql.append("        AND PEDIDO.NUPEDIDO = TC.NUPEDIDO ");
		sql.append("        AND PEDIDO.FLORIGEMPEDIDO = TC.FLORIGEMPEDIDO ");
		sql.append("         JOIN TBLVPPRODUTO PRODUTO ON ");
		sql.append("            PRODUTO.CDEMPRESA = TC.CDEMPRESA ");
		sql.append("        AND PRODUTO.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
		sql.append("        AND PRODUTO.CDPRODUTO = TC.CDPRODUTO ");
		sql.append("        AND PRODUTO.CDFAMILIADESCPROG = tb.CDFAMILIADESCPROG ");
		sql.append("        WHERE TC.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("          AND TC.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append(DescProgressivoConfigDbxDao.addItemPedidoRequirements("TC"));
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(cliente.cdCliente, "PEDIDO", "DESCPROGRESSIVOCONFIG", true, false, false));
		return sql.toString();
	}

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    sql.append(" tb.CDDESCPROGRESSIVO,");
	    sql.append(" tb.CDFAMILIADESCPROG,");
		sql.append(" tb.FLTIPOFAMILIAPRO,");
		sql.append(" tb.FLTIPOFAMILIACON,");
		sql.append(" tb.FLFAMACUVALORMIN,");
		sql.append(" tb.FLFAMACUVALORMAX,");
	    sql.append(" tb.CDUSUARIO, ");
		sql.append(" DESCPROGFAMILIA.DSFAMILIAPROD ");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
    	addSelectColumns(null, sql);
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return populate(domainFilter, rs);
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		addOrderByWithoutAlias(sql, domain);
	}

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgConfigFam descProgConfigFam = (DescProgConfigFam) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", descProgConfigFam.cdEmpresa);
	    sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", descProgConfigFam.cdRepresentante);
	    sqlWhereClause.addAndCondition("tb.CDDESCPROGRESSIVO  = ", descProgConfigFam.cdDescProgressivo);
		sqlWhereClause.addAndCondition("tb.CDFAMILIADESCPROG  = ", descProgConfigFam.cdFamiliaDescProg);
		sqlWhereClause.addAndCondition("tb.FLTIPOFAMILIAPRO  = ", descProgConfigFam.flTipoFamiliaPro);
		sqlWhereClause.addAndCondition("tb.FLTIPOFAMILIACON  = ", descProgConfigFam.flTipoFamiliaCon);
		sqlWhereClause.addAndCondition("tb.FLFAMACUVALORMIN  = ", descProgConfigFam.flFamAcuValorMin);
		sqlWhereClause.addAndCondition("tb.FLFAMACUVALORMAX  = ", descProgConfigFam.flFamAcuValorMax);
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
		super.addJoinSummary(domainFilter, sql);
		sql.append(addJoinDescProgFamilia());
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		super.addJoin(domainFilter, sql);
		sql.append(addJoinDescProgFamilia());

	}

	private String addJoinDescProgFamilia() {
		StringBuffer sql = getSqlBuffer();
		sql.append(" JOIN TBLVPDESCPROGFAMILIA DESCPROGFAMILIA ON ")
				.append("    DESCPROGFAMILIA.CDEMPRESA = TB.CDEMPRESA ")
				.append("AND DESCPROGFAMILIA.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
				.append("AND DESCPROGFAMILIA.CDFAMILIADESCPROG = TB.CDFAMILIADESCPROG ");
		return sql.toString();
	}

}
