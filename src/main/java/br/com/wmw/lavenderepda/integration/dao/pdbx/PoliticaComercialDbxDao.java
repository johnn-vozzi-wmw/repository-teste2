package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliFin;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercial;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class PoliticaComercialDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PoliticaComercial();
	}

    private static PoliticaComercialDbxDao instance;

    public PoliticaComercialDbxDao() {
        super(PoliticaComercial.TABLE_NAME);
    }

    public static PoliticaComercialDbxDao getInstance() {
        if (instance == null) {
            instance = new PoliticaComercialDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PoliticaComercial politicaComercial = new PoliticaComercial();
        politicaComercial.rowKey = rs.getString("rowkey");
        politicaComercial.cdEmpresa = rs.getString("cdEmpresa");
        politicaComercial.cdPoliticaComercial = rs.getString("cdPoliticaComercial");
        politicaComercial.dtIniVigencia = rs.getDate("dtIniVigencia");
        politicaComercial.dtFimVigencia = rs.getDate("dtFimVigencia");
        politicaComercial.flCampanha = rs.getString("flCampanha");
        politicaComercial.cdProduto = rs.getString("cdProduto");
        politicaComercial.cdLinha = rs.getString("cdLinha");
        politicaComercial.cdClasse = rs.getString("cdClasse");
        politicaComercial.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        politicaComercial.cdConjunto = rs.getString("cdConjunto");
        politicaComercial.cdMarca = rs.getString("cdMarca");
        politicaComercial.cdColecao = rs.getString("cdColecao");
        politicaComercial.cdStatusColecao = rs.getString("cdStatusColecao");
        politicaComercial.cdFamilia = rs.getString("cdFamilia");
		//-- ROUND COM 7 CASAS PARA QUEBRAR O PONTO FLUTUANTE DO JAVA, NÃO REMOVER!
	    politicaComercial.vlIndiceCliente = ValueUtil.round(rs.getDouble("vlIndiceCliente"), 7);
        politicaComercial.cdCentroCusto = rs.getString("cdCentroCusto");
        politicaComercial.cdRepresentantePolitica = rs.getString("cdRepresentantePolitica");
        politicaComercial.cdCategoria = rs.getString("cdCategoria");
        politicaComercial.cdCliente = rs.getString("cdCliente");
        politicaComercial.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
        politicaComercial.cdTabelaPreco = rs.getString("cdTabelaPreco");
        politicaComercial.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        politicaComercial.flAcumulaIndices = rs.getString("flAcumulaIndices");
        politicaComercial.dsReferencia = rs.getString("dsReferencia");
        politicaComercial.flVendaEncerrada = rs.getString("flVendaEncerrada");
        politicaComercial.flTipoAlteracao = rs.getString("flTipoAlteracao");
        politicaComercial.nuCarimbo = rs.getInt("nuCarimbo");
        politicaComercial.cdUsuario = rs.getString("cdUsuario");
        politicaComercial.vlPctPoliticaComercialMin = rs.getDouble("vlPctPoliticaComercialMin");
        politicaComercial.vlPctPoliticaComercialMax = rs.getDouble("vlPctPoliticaComercialMax");
        politicaComercial.cdGrupoCondPagtoPolitComerc = rs.getString("cdGrupoCondPagtoPolitComerc");

        return politicaComercial;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDPOLITICACOMERCIAL,");
        sql.append(" tb.DTINIVIGENCIA,");
        sql.append(" tb.DTFIMVIGENCIA,");
        sql.append(" tb.FLCAMPANHA,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDLINHA,");
        sql.append(" tb.CDCLASSE,");
        sql.append(" tb.CDGRUPOPRODUTO1,");
        sql.append(" tb.CDCONJUNTO,");
        sql.append(" tb.CDMARCA,");
        sql.append(" tb.CDCOLECAO,");
        sql.append(" tb.CDSTATUSCOLECAO,");
        sql.append(" tb.CDFAMILIA,");
        sql.append(" tb.VLINDICECLIENTE,");
        sql.append(" tb.CDCENTROCUSTO,");
        sql.append(" tb.CDREPRESENTANTEPOLITICA,");
        sql.append(" tb.CDCATEGORIA,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDGRUPODESCCLI,");
        sql.append(" tb.CDTABELAPRECO,");
        sql.append(" tb.CDCONDICAOPAGAMENTO,");
        sql.append(" tb.FLACUMULAINDICES,");
        sql.append(" tb.DSREFERENCIA,");
        sql.append(" tb.FLVENDAENCERRADA,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.VLPCTPOLITICACOMERCIALMIN,");
        sql.append(" tb.VLPCTPOLITICACOMERCIALMAX,");
        sql.append(" tb.CDGRUPOCONDPAGTOPOLITCOMERC");
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) { }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PoliticaComercial politicaComercial = (PoliticaComercial) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", politicaComercial.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPOLITICACOMERCIAL = ", politicaComercial.cdPoliticaComercial);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	private void addWhereFiltrosPlataformaVendaFin(PoliticaComercial politicaComercial, StringBuffer sql) {
		if (politicaComercial.vlIndiceCliente == 0d) {
			sql.append(" AND (printf('%.7f', tb.VLINDICECLIENTE) = ").append("printf('%.7f', pvclifin.VLINDICEFINANCEIRO) ")
					.append(" OR tb.VLINDICECLIENTE = 0.0 OR tb.VLINDICECLIENTE = 0 OR tb.VLINDICECLIENTE IS NULL)");
		} else {
			sql.append(" AND (printf('%.7f', tb.VLINDICECLIENTE) = ").append("printf('%.7f', ").append(politicaComercial.vlIndiceCliente).append(")")
					.append(" OR tb.VLINDICECLIENTE = 0.0 OR tb.VLINDICECLIENTE = 0 OR tb.VLINDICECLIENTE IS NULL)");
		}
	}

	private static void addWhereFiltrosProduto(PoliticaComercial politicaComercial, StringBuffer sql) {
		sql.append(" (tb.CDPRODUTO = ").append(Sql.getValue(politicaComercial.cdProduto))
				.append(" OR tb.CDPRODUTO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDPRODUTO IS NULL)")
				.append(" AND (tb.CDLINHA = ").append(Sql.getValue(politicaComercial.cdLinha))
				.append(" OR tb.CDLINHA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDLINHA IS NULL)")
				.append(" AND (tb.CDCLASSE = ").append(Sql.getValue(politicaComercial.cdClasse))
				.append(" OR tb.CDCLASSE = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCLASSE IS NULL)")
				.append(" AND (tb.CDGRUPOPRODUTO1 = ").append(Sql.getValue(politicaComercial.cdGrupoProduto1))
				.append(" OR tb.CDGRUPOPRODUTO1 = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDGRUPOPRODUTO1 IS NULL)")
				.append(" AND (tb.CDCONJUNTO = ").append(Sql.getValue(politicaComercial.cdConjunto))
				.append(" OR tb.CDCONJUNTO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCONJUNTO IS NULL)")
				.append(" AND (tb.CDMARCA = ").append(Sql.getValue(politicaComercial.cdMarca))
				.append(" OR tb.CDMARCA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDMARCA IS NULL)")
				.append(" AND (tb.CDCOLECAO = ").append(Sql.getValue(politicaComercial.cdColecao))
				.append(" OR tb.CDCOLECAO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCOLECAO IS NULL)")
				.append(" AND (tb.CDSTATUSCOLECAO = ").append(Sql.getValue(politicaComercial.cdStatusColecao))
				.append(" OR tb.CDSTATUSCOLECAO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDSTATUSCOLECAO IS NULL)")
				.append(" AND (tb.DSREFERENCIA = ").append(Sql.getValue(politicaComercial.dsReferencia))
				.append(" OR tb.DSREFERENCIA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.DSREFERENCIA IS NULL)")
				.append(" AND (tb.FLVENDAENCERRADA = ").append(Sql.getValue(politicaComercial.flVendaEncerrada))
				.append(" OR tb.FLVENDAENCERRADA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.FLVENDAENCERRADA IS NULL)")
				.append(" AND (tb.CDTABELAPRECO = ").append(Sql.getValue(politicaComercial.cdTabelaPreco))
				.append(" OR tb.CDTABELAPRECO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDTABELAPRECO IS NULL)");
		if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()) {
			sql.append(" AND (tb.CDFAMILIA = ").append(Sql.getValue(politicaComercial.cdFamilia))
					.append(" OR tb.CDFAMILIA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
					.append(" OR tb.CDFAMILIA IS NULL)");
		}
		if (LavenderePdaConfig.isUsaAgrupadorKitPolitica()) {
			sql.append(" AND (tb.DSAGRUPADORKIT = ").append(Sql.getValue(politicaComercial.dsAgrupadorKit))
					.append(" OR tb.DSAGRUPADORKIT = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
					.append(" OR tb.DSAGRUPADORKIT IS NULL)");
		}
		if (!LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()) {
			sql.append(" AND (");
			sql.append(" (TB.CDFAMILIA = ").append(Sql.getValue("0")).append(" OR TB.CDFAMILIA IS NULL OR TB.CDFAMILIA = '') ");
			sql.append(" OR EXISTS (SELECT 1 FROM TBLVPFAMILIAPRODUTO FAMILIAPRODUTO WHERE ");
			sql.append(" FAMILIAPRODUTO.CDEMPRESA = TB.CDEMPRESA ");
			sql.append(" AND FAMILIAPRODUTO.CDPRODUTO = ").append(Sql.getValue(politicaComercial.cdProduto));
			sql.append(" AND FAMILIAPRODUTO.CDFAMILIA = TB.CDFAMILIA");
			sql.append(" )) ");
		}
	}

	private static void addWhereFiltrosPedido(PoliticaComercial politicaComercial, StringBuffer sql) {
		sql.append("tb.CDEMPRESA = ").append(Sql.getValue(politicaComercial.cdEmpresa))
				.append(" AND tb.DTINIVIGENCIA <= ").append(Sql.getValue(DateUtil.getCurrentDate()))
				.append(" AND tb.DTFIMVIGENCIA >= ").append(Sql.getValue(DateUtil.getCurrentDate()));
		sql.append(" AND (tb.CDCENTROCUSTO = ").append(Sql.getValue(politicaComercial.cdCentroCusto))
				.append(" OR tb.CDCENTROCUSTO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCENTROCUSTO IS NULL)")
				.append(" AND (tb.CDREPRESENTANTEPOLITICA = ").append(Sql.getValue(politicaComercial.cdRepresentantePolitica))
				.append(" OR tb.CDREPRESENTANTEPOLITICA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDREPRESENTANTEPOLITICA IS NULL)")
				.append(" AND (tb.CDCATEGORIA = ").append(Sql.getValue(politicaComercial.cdCategoria))
				.append(" OR tb.CDCATEGORIA = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCATEGORIA IS NULL)")
				.append(" AND (tb.CDCLIENTE = ").append(Sql.getValue(politicaComercial.cdCliente))
				.append(" OR tb.CDCLIENTE = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCLIENTE IS NULL)")
				.append(" AND (tb.CDGRUPODESCCLI = ").append(Sql.getValue(politicaComercial.cdGrupoDescCli))
				.append(" OR tb.CDGRUPODESCCLI = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDGRUPODESCCLI IS NULL)")
				.append(" AND (tb.CDCONDICAOPAGAMENTO = ").append(Sql.getValue(politicaComercial.cdCondicaoPagamento))
				.append(" OR tb.CDCONDICAOPAGAMENTO = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDCONDICAOPAGAMENTO IS NULL)")
				.append(" AND (tb.CDGRUPOCONDPAGTOPOLITCOMERC = ").append(Sql.getValue(politicaComercial.cdGrupoCondPagtoPolitComerc))
				.append(" OR tb.CDGRUPOCONDPAGTOPOLITCOMERC = ").append(Sql.getValue(ValueUtil.VALOR_ZERO))
				.append(" OR tb.CDGRUPOCONDPAGTOPOLITCOMERC IS NULL)");
	}

	protected void addOrderByRegraPoliticaComercial(StringBuffer sql) {
    	List<String> colunas = LavenderePdaConfig.getListaOrdenacaoPoliticaComercial();
		if (ValueUtil.isEmpty(colunas)) {
			return;
		}

		boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > - 1;
		sql.append(" order by ");
		List<String> ascDescColumn;
		for (int i = 0; i < colunas.size(); i++) {
			String coluna = colunas.get(i);
			ascDescColumn = Arrays.asList(coluna.split(":"));
			if (PoliticaComercial.NMCOLUNA_DTINIVIGENCIA.equalsIgnoreCase(ascDescColumn.get(0)) || PoliticaComercial.NMCOLUNA_DTFIMVIGENCIA.equalsIgnoreCase(ascDescColumn.get(0))) {
				appendOrderColunaData(sql, possuiTB, ascDescColumn, i, colunas);
				continue;
			} else {
				sql.append(" case when COALESCE(").append(possuiTB ? "tb." : "").append(ascDescColumn.get(0)).append(", 'N')").append(" in ('0', 'N')  then '0' else '1' end");
			}
			addAscDescParameter(sql, ascDescColumn);
			appendComma(sql, i, colunas);
		}
    }

	private void appendOrderColunaData(StringBuffer sql, boolean possuiTB, List<String> ascDescColumn, int i, List<String> colunas) {
		sql.append(" case when ").append(possuiTB ? "tb." : "").append(ascDescColumn.get(0))
				.append(" in ('0', 'N', null)  then '0' else ")
				.append(" CAST(strftime('%Y%m%d', ").append(ascDescColumn.get(0)).append(") AS int)")
				.append(" end ");
		addAscDescParameter(sql, ascDescColumn);
		appendComma(sql, i, colunas);
	}

	private void appendComma(StringBuffer sql, int i, List<String> colunas) {
		if (i < colunas.size() - 1) {
			sql.append(", ");
		}
	}

	private void addAscDescParameter(StringBuffer sql, List<String> ascDescColumn) {
		if (ascDescColumn.size() > 1) {
			sql.append(" ").append(ascDescColumn.get(1)).append(" ");
		} else {
			sql.append(" DESC ");
		}
	}

    public PoliticaComercial findByRegraPoliticaComercial(BaseDomain domain) throws SQLException {
	    StringBuffer sql = getSqlBuffer();
	    sql.append(" SELECT ");
	    addSelectColumns(domain, sql);
	    addSelectPlataformaVendaCliFin(sql);
	    sql.append(" FROM POLITICACOMERCIALPEDIDO tb");
	    PoliticaComercial domainFilter = (PoliticaComercial) domain;
	    addJoinPlataformaVendaCliFin(domainFilter, sql);
		sql.append(" WHERE ");
	    addWhereFiltrosProduto(domainFilter, sql);
		addWhereFiltrosPlataformaVendaFin(domainFilter, sql);
    	addOrderByRegraPoliticaComercial(sql);
    	sql.append(" LIMIT 1 ");
    	Vector result = findAllByRegraPoliticaComercial(sql.toString());
    	return (PoliticaComercial) result.items[0];
    }

	protected Vector findAllByRegraPoliticaComercial(String sql) throws SQLException {
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql)) {
			Vector result = new Vector();
			while (rs.next()) {
				result.addElement(populateByRegraPoliticaComercial(rs));
			}
			return result;
		}
	}

	private void addSelectPlataformaVendaCliFin(StringBuffer sql) {
		sql.append(", pvclifin.rowKey AS rowKeyPvCliFin");
		sql.append(", pvclifin.VLINDICEFINANCEIRO");
		sql.append(", pvclifin.CDPLATAFORMAVENDA");
	}

	private PoliticaComercial populateByRegraPoliticaComercial(ResultSet rs) throws SQLException {

		PlataformaVendaCliFin plataformaVendaCliFin = new PlataformaVendaCliFin();
		plataformaVendaCliFin.rowKey = rs.getString("rowKeyPvCliFin");
		plataformaVendaCliFin.cdEmpresa = rs.getString("cdEmpresa");
		plataformaVendaCliFin.cdRepresentante = rs.getString("cdRepresentantePolitica");
		plataformaVendaCliFin.cdCliente = rs.getString("cdCliente");
		plataformaVendaCliFin.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
		plataformaVendaCliFin.cdLinha = rs.getString("cdLinha");
		//-- ROUND COM 7 CASAS PARA QUEBRAR O PONTO FLUTUANTE DO JAVA, NÃO REMOVER!
		plataformaVendaCliFin.vlIndiceFinanceiro = ValueUtil.round(rs.getDouble("vlIndiceFinanceiro"), 7);
		plataformaVendaCliFin.nuCarimbo = rs.getInt("nuCarimbo");
		plataformaVendaCliFin.flTipoAlteracao = rs.getString("flTipoAlteracao");
		plataformaVendaCliFin.cdUsuario = rs.getString("cdUsuario");

		PoliticaComercial politicaComercial = (PoliticaComercial)populate(getBaseDomainDefault(), rs);
		politicaComercial.plataformaVendaCliFin = plataformaVendaCliFin;

        return politicaComercial;
	}

	private void addJoinPlataformaVendaCliFin(PoliticaComercial domainFilter, StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPPLATAFORMAVENDACLIFIN pvclifin ON tb.CDEMPRESA = pvclifin.CDEMPRESA")
				.append(" AND pvclifin.CDREPRESENTANTE = ").append(Sql.getValue(domainFilter.cdRepresentantePolitica))
				.append(" AND pvclifin.CDCLIENTE = ").append(Sql.getValue(domainFilter.cdCliente));
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			sql.append(" AND pvclifin.CDPLATAFORMAVENDA = ").append(Sql.getValue(domainFilter.cdPlataformaVenda));
		}
		sql.append(" AND pvclifin.CDLINHA = ").append(Sql.getValue(domainFilter.cdLinha));
	}

	public void createTabelaTemporariaPoliticaComercialPedido(PoliticaComercial politicaComercialFilter) throws SQLException {
		dropTabelaTemporariaPoliticaComercialPedido();
		StringBuffer sql = getSqlBuffer();
		sql.append(" CREATE TEMPORARY TABLE POLITICACOMERCIALPEDIDO AS ");
		sql.append(" SELECT tb.* FROM ").append(tableName).append(" tb ");
		sql.append(" WHERE ");
		addWhereFiltrosPedido(politicaComercialFilter, sql);
		addOrderByRegraPoliticaComercial(sql);
		getCurrentDriver().execute(sql.toString());
	}

	public void dropTabelaTemporariaPoliticaComercialPedido() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("DROP TABLE IF EXISTS POLITICACOMERCIALPEDIDO");
		getCurrentDriver().execute(sql.toString());
	}

}
