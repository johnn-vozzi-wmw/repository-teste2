package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class ItemTabelaPrecoPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemTabelaPreco();
	}

	private static ItemTabelaPrecoPdbxDao instance;
	private boolean initializingCache;

	public ItemTabelaPrecoPdbxDao() {
		super(ItemTabelaPreco.TABLE_NAME);
	}

	public static ItemTabelaPrecoPdbxDao getInstance() {
		if (instance == null) {
			instance = new ItemTabelaPrecoPdbxDao();
		}
		return instance;
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDITEMGRADE1,");
		sql.append(" tb.CDITEMGRADE2,");
		sql.append(" tb.CDITEMGRADE3,");
		sql.append(" tb.CDUNIDADE,");
		sql.append(" tb.QTITEM,");
		sql.append(" tb.CDPRAZOPAGTOPRECO,");
		sql.append(" tb.VLBASE,");
		sql.append(" tb.VLUNITARIO,");
		sql.append(" tb.VLPCTMAXDESCONTO,");
		sql.append(" tb.VLPCTMAXACRESCIMO,");
		sql.append(" tb.VLMAXDESCONTO,");
		sql.append(" tb.VLMAXACRESCIMO,");
		sql.append(" tb.VLPCTCOMISSAO,");
		sql.append(" tb.VLPRECOEMBALAGEM,");
		sql.append(" tb.FLPROMOCAO,");
		sql.append(" tb.QTMINITENSNORMAIS,");
		sql.append(" tb.VLPRECOCUSTO,");
		if (LavenderePdaConfig.usaPrecoPorUf) {
			sql.append(" tb.CDUF,");
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			sql.append(" tb.CDLINHA,");
		}
		if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			sql.append(" tb.VLBASEESPECIAL,");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" tb.VLPRECOFRETE,");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			sql.append(" tb.VLREDUCAOOPTANTESIMPLES,");
		}
		sql.append(" tb.vlPctDescPromocional,");
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" tb.FLBLOQUEADO,");
		}
		if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial) {
			sql.append(" tb.VLUNITARIOESPECIAL,");
		}
		sql.append(" tb.QTMAXVENDA,");
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			sql.append(" tb.NUCONVERSAOUNIDADE,");
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			sql.append(" tb.VLOPORTUNIDADE,");
		}
		if (LavenderePdaConfig.destacaProdutoComPrecoEmQueda) {
			sql.append(" tb.FLPRECOQUEDA,");
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			sql.append(" tb.FLUSADESCONTO3,");
		}
		if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0) {
			sql.append(" tb.VLPCTPREVISAORENTABILIDADE,");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" tb.VLPCTMARGEMRENTABILIDADE,");
 			sql.append(" tb.NUPROMOCAO,");
 			sql.append(" tb.VLDESCONTOPROMO,");
 			sql.append(" tb.DTINICIOPROMOCAO,");
        }
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco) {
			sql.append(" tb.VLPCTRENTABILIDADEESP,");
			sql.append(" tb.VLPCTRENTABILIDADEMIN,");
		}
		if (LavenderePdaConfig.usaPctMaxParticipacaoItemBonificacao) {
			sql.append(" tb.VLPCTMAXPARTICIPACAO,");
		}
		if (LavenderePdaConfig.usaValorMinimoItemPedidoPorItemTabelaPreco() || LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() ) {
			sql.append(" tb.VLMINITEMPEDIDO,");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" tb.DTVENCIMENTOPRECO, tb.VLPCTTAXA,");
		}
		if (LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente) {
			sql.append(" tb.VLPCTDESCVALORBASE,");
		}
		if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorItemTabPreco()) {
			sql.append(" tb.VLPRECOMAXIMOCONSUMIDOR,");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			sql.append(" tb.VLPCTDESCAPROVADO,");
		}

		if (LavenderePdaConfig.controlaDescontoUsandoVerbaAtravesQtMinItens) {
			sql.append(" tb.QTMINIMAVENDA,");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			sql.append(" tb.CDPRAZOINI,")
			.append(" tb.CDPRAZOFIM,")
			.append(" tb.VLUNITPRAZOINI,")
			.append(" tb.VLUNITPRAZOFIM,");
		}
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
			sql.append(" tb.VLPCTDIRETORIA,");
		}
		sql.append(" tb.FLBLOQUEIADESCPROMO,");
		sql.append(" tb.FLBLOQUEIADESCPADRAO,");
		sql.append(" tb.FLBLOQUEIADESCCONDICAO,");
		sql.append(" tb.FLBLOQUEIADESC2,");
		sql.append(" tb.FLBLOQUEIADESC3,");
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(" tb.vlBasePontuacao,");
		}
		sql.append(" tb.VLPRECOEMBALAGEMPRIMARIA");
		if (initializingCache && LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			sql.append(", cond.vlUnitario as vlUnitarioExcec");
			sql.append(", cond.vlBase as vlBaseExcec");
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			sql.append(", TB.VLEMBALAGEMST");
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			sql.append(", TB.VLINDICECOMISSAO");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(", TB.VLMAODEOBRA");
			sql.append(", TB.VLPCTCUSTOCOMERCIAL");
			sql.append(", TB.VLCREDITOIMPOSTOS ");
		}
		if (LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco) {
			sql.append(", tb.VLNCM ");
			sql.append(", tb.VLPCTICMS ");
			sql.append(", tb.VLPCTST ");
			sql.append(", tb.VLPCTIPI ");
			sql.append(", tb.VLPCTPIS ");
			sql.append(", tb.VLPCTCOFINS ");
			sql.append(", tb.VLCST ");
			sql.append(", tb.VLPCTMVA");
			sql.append(", tb.VLCFOP");
			sql.append(", tb.VLPCTPISCOFINS");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			sql.append(", tb.VLCOTACAOMOEDA");
		}
	}

	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.rowKey = rs.getString("rowkey");
		itemTabelaPreco.cdEmpresa = rs.getString("cdEmpresa");
		itemTabelaPreco.cdRepresentante = rs.getString("cdRepresentante");
		itemTabelaPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemTabelaPreco.cdProduto = rs.getString("cdProduto");
		itemTabelaPreco.cdItemGrade1 = rs.getString("cdItemGrade1");
		itemTabelaPreco.cdItemGrade2 = rs.getString("cdItemGrade2");
		itemTabelaPreco.cdItemGrade3 = rs.getString("cdItemGrade3");
		itemTabelaPreco.cdUnidade = rs.getString("cdUnidade");
		itemTabelaPreco.qtItem = rs.getInt("qtItem");
		itemTabelaPreco.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
		itemTabelaPreco.vlBase = ValueUtil.round(rs.getDouble("vlBase"));
		itemTabelaPreco.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
		  itemTabelaPreco.vlEmbalagemSt = ValueUtil.round(rs.getDouble("vlEmbalagemSt"));
	    }
		itemTabelaPreco.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
		itemTabelaPreco.vlPctMaxAcrescimo = ValueUtil.round(rs.getDouble("vlPctMaxAcrescimo"));
		itemTabelaPreco.vlMaxDesconto = ValueUtil.round(rs.getDouble("vlMaxDesconto"));
		itemTabelaPreco.vlMaxAcrescimo = ValueUtil.round(rs.getDouble("vlMaxAcrescimo"));
		itemTabelaPreco.vlPrecoEmbalagemPrimaria = ValueUtil.round(rs.getDouble("vlPrecoEmbalagemPrimaria"));
		if (!LavenderePdaConfig.isNuCasasDecimaisComissaoLigado) {
			itemTabelaPreco.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
		} else {
			itemTabelaPreco.vlPctComissao = ValueUtil.getDoubleValueTruncated(rs.getDouble("vlPctComissao"), LavenderePdaConfig.nuCasasDecimaisComissao);
		}
		itemTabelaPreco.vlPrecoEmbalagem = ValueUtil.round(rs.getDouble("vlPrecoEmbalagem"));
		itemTabelaPreco.flPromocao = rs.getString("flPromocao");
		itemTabelaPreco.qtMinItensNormais = rs.getInt("qtMinItensNormais");
		itemTabelaPreco.vlPrecoCusto = rs.getDouble("vlPrecoCusto");
		if (LavenderePdaConfig.usaPrecoPorUf) {
			itemTabelaPreco.cdUf = rs.getString("cdUf");
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			itemTabelaPreco.cdLinha = rs.getString("cdLinha");
		}
		if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			itemTabelaPreco.vlBaseEspecial = ValueUtil.round(rs.getDouble("vlBaseEspecial"));
		}
		itemTabelaPreco.vlPctDescPromocional = ValueUtil.round(rs.getDouble("vlPctDescPromocional"));
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemTabelaPreco.vlPrecoFrete = ValueUtil.round(rs.getDouble("vlPrecoFrete"));
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			itemTabelaPreco.vlReducaoOptanteSimples = ValueUtil.round(rs.getDouble("vlReducaoOptanteSimples"));
		}
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			itemTabelaPreco.flBloqueado = rs.getString("flBloqueado");
		}
		if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial) {
			itemTabelaPreco.vlUnitarioEspecial = ValueUtil.round(rs.getDouble("vlUnitarioEspecial"));
		}
		itemTabelaPreco.qtMaxVenda = ValueUtil.round(rs.getDouble("qtMaxVenda"));
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			itemTabelaPreco.setNuConversaoUnidade(ValueUtil.round(rs.getDouble("nuConversaoUnidade")));
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			itemTabelaPreco.vlOportunidade = ValueUtil.round(rs.getDouble("vlOportunidade"));
		}
		if (LavenderePdaConfig.destacaProdutoComPrecoEmQueda) {
			itemTabelaPreco.flPrecoQueda = rs.getString("flPrecoQueda");
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			itemTabelaPreco.flUsaDesconto3 = rs.getString("flUsaDesconto3");
		}
		if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0) {
			itemTabelaPreco.vlPctPrevisaRentabilidade = ValueUtil.round(rs.getDouble("vlPctPrevisaoRentabilidade"));
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			itemTabelaPreco.vlPctMargemRentabilidade = ValueUtil.round(rs.getDouble("vlPctMargemRentabilidade"));
 			itemTabelaPreco.nuPromocao = rs.getInt("nuPromocao");
 			itemTabelaPreco.vlDescontoPromo = ValueUtil.round(rs.getDouble("vlDescontoPromo"));
 			itemTabelaPreco.dtInicioPromocao = rs.getDate("dtInicioPromocao");
        }
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco) {
			itemTabelaPreco.vlPctRentabilidadeEsp = ValueUtil.round(rs.getDouble("vlPctRentabilidadeEsp"));
			itemTabelaPreco.vlPctRentabilidadeMin = ValueUtil.round(rs.getDouble("vlPctRentabilidadeMin"));
		}
		if (LavenderePdaConfig.usaPctMaxParticipacaoItemBonificacao) {
			itemTabelaPreco.vlPctMaxParticipacao = rs.getDouble("vlPctMaxParticipacao");
		}
		if (LavenderePdaConfig.usaValorMinimoItemPedidoPorItemTabelaPreco() || LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
			itemTabelaPreco.vlMinItemPedido = rs.getDouble("vlMinItemPedido");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			itemTabelaPreco.dtVencimentoPreco = rs.getDate("dtVencimentoPreco");
			itemTabelaPreco.vlPctTaxa = rs.getDouble("vlPctTaxa");
		}
		if (LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente) {
			itemTabelaPreco.vlPctDescValorBase = rs.getDouble("vlPctDescValorBase");
		}
		if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorItemTabPreco()) {
			itemTabelaPreco.vlPrecoMaximoConsumidor = rs.getDouble("vlPrecoMaximoConsumidor");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			itemTabelaPreco.vlPctDescAprovado = rs.getDouble("vlPctDescAprovado");
		}

		if (LavenderePdaConfig.controlaDescontoUsandoVerbaAtravesQtMinItens) {
			itemTabelaPreco.qtMinimaVenda = rs.getDouble("qtMinimaVenda");
		}
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
			itemTabelaPreco.vlPctDiretoria = rs.getDouble("vlPctDiretoria");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemTabelaPreco.cdPrazoIni = rs.getInt("cdPrazoIni");
			itemTabelaPreco.cdPrazoFim = rs.getInt("cdPrazoFim");
			itemTabelaPreco.vlUnitPrazoIni = rs.getDouble("vlUnitPrazoIni");
			itemTabelaPreco.vlUnitPrazoFim = rs.getDouble("vlUnitPrazoFim");
		}
		if (initializingCache && LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			itemTabelaPreco.condComercialExcec.cdProduto = itemTabelaPreco.cdProduto;
			itemTabelaPreco.condComercialExcec.vlBase = rs.getDouble("vlBaseExcec");
			itemTabelaPreco.condComercialExcec.vlUnitario = rs.getDouble("vlUnitarioExcec");
		}
		itemTabelaPreco.flBloqueiaDescPromo = rs.getString("FLBLOQUEIADESCPROMO");
		itemTabelaPreco.flBloqueiaDescPadrao = rs.getString("FLBLOQUEIADESCPADRAO");
		itemTabelaPreco.flBloqueiaDescCondicao = rs.getString("FLBLOQUEIADESCCONDICAO");
		itemTabelaPreco.flBloqueiaDesc2 = rs.getString("FLBLOQUEIADESC2");
		itemTabelaPreco.flBloqueiaDesc3 = rs.getString("FLBLOQUEIADESC3");
		if (LavenderePdaConfig.usaControlePontuacao) {
			itemTabelaPreco.vlBasePontuacao = ValueUtil.round(rs.getDouble("vlBasePontuacao"));
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			itemTabelaPreco.vlIndiceComissao = rs.getDouble("VLINDICECOMISSAO");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemTabelaPreco.vlMaoDeObra = rs.getDouble("VLMAODEOBRA");
			itemTabelaPreco.vlPctCustoComercial = rs.getDouble("VLPCTCUSTOCOMERCIAL");
			itemTabelaPreco.vlCreditoImpostos = rs.getDouble("VLCREDITOIMPOSTOS");
		}

		if (LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco){
			itemTabelaPreco.vlNcm = rs.getString("VLNCM");
			itemTabelaPreco.vlPctIcms = rs.getDouble("VLPCTICMS");
			itemTabelaPreco.vlPctSt = rs.getDouble("VLPCTST");
			itemTabelaPreco.vlPctIpi = rs.getDouble("VLPCTIPI");
			itemTabelaPreco.vlPctPis = rs.getDouble("VLPCTPIS");
			itemTabelaPreco.vlPctCofins = rs.getDouble("VLPCTCOFINS");
			itemTabelaPreco.vlCst = rs.getString("VLCST");
			itemTabelaPreco.vlPctMva= rs.getDouble("VLPCTMVA");
			itemTabelaPreco.vlCfop = rs.getString("VLCFOP");
			itemTabelaPreco.vlPctPisCofins = rs.getDouble("VLPCTPISCOFINS");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			itemTabelaPreco.vlCotacaoMoeda = rs.getDouble("VLCOTACAOMOEDA");
		}
		return itemTabelaPreco;
	}

	//@Override
	protected BaseDomain populateCache(ResultSet rs) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.rowKey = rs.getString(1);
		itemTabelaPreco.cdEmpresa = rs.getString(2);
		itemTabelaPreco.cdRepresentante = rs.getString(3);
		itemTabelaPreco.cdTabelaPreco = rs.getString(4);
		itemTabelaPreco.cdProduto = rs.getString(6);
		itemTabelaPreco.cdUf = rs.getString(5);
		itemTabelaPreco.cdItemGrade1 = rs.getString(7);
		itemTabelaPreco.cdItemGrade2 = rs.getString(8);
		itemTabelaPreco.cdItemGrade3 = rs.getString(9);
		itemTabelaPreco.cdUnidade = rs.getString(10);
		itemTabelaPreco.qtItem = rs.getInt(11);
		itemTabelaPreco.cdPrazoPagtoPreco = rs.getInt(12);
		itemTabelaPreco.flPromocao = rs.getString(13);
		itemTabelaPreco.qtMaxVenda = rs.getDouble(14);
		itemTabelaPreco.vlUnitario = rs.getDouble(15);
		itemTabelaPreco.vlBase = rs.getDouble(16);
		int i = 17;
		if (LavenderePdaConfig.usaFiltroComissao) {
			itemTabelaPreco.vlPctComissao = rs.getDouble(i++);			
		}
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			itemTabelaPreco.setNuConversaoUnidade(rs.getDouble(i++));
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			itemTabelaPreco.vlPctTaxa = rs.getDouble(i++);
			itemTabelaPreco.dtVencimentoPreco = rs.getDate(i++);
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			itemTabelaPreco.vlBasePontuacao = ValueUtil.round(rs.getDouble(i++));
		}
		if (initializingCache && LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			itemTabelaPreco.condComercialExcec.cdProduto = itemTabelaPreco.cdProduto;
			itemTabelaPreco.condComercialExcec.vlBase = rs.getDouble(i++);
			itemTabelaPreco.condComercialExcec.vlUnitario = rs.getDouble(i++);
		}
		return itemTabelaPreco;
	}

	//@Override
	protected void addCacheColumns(StringBuffer sql) {
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.CDUF,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDITEMGRADE1,");
		sql.append(" tb.CDITEMGRADE2,");
		sql.append(" tb.CDITEMGRADE3,");
		sql.append(" tb.CDUNIDADE,");
		sql.append(" tb.QTITEM,");
		sql.append(" tb.CDPRAZOPAGTOPRECO,");
		sql.append(" tb.FLPROMOCAO,");
		sql.append(" tb.QTMAXVENDA,");
		sql.append(" tb.VLUNITARIO,");
		sql.append(" tb.VLBASE");
		if (LavenderePdaConfig.usaFiltroComissao) {
			sql.append(" ,tb.VLPCTCOMISSAO");
		}
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			sql.append(" ,tb.NUCONVERSAOUNIDADE");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" ,tb.VLPCTTAXA, tb.DTVENCIMENTOPRECO");
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(", tb.VLBASEPONTUACAO");
		}
		if (initializingCache && LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			sql.append(", cond.vlUnitario as vlUnitarioExcec");
			sql.append(", cond.vlBase as vlBaseExcec");
		}
	}
	
	@Override
	protected void addJoinCache(BaseDomain domainFilter, StringBuffer sql) {
		addJoinCondComercialExcec(domainFilter, sql);
	}
	
	@Override
	protected void addJoinGrid(BaseDomain domainFilter,StringBuffer sql) {
		addJoinCondComercialExcec(domainFilter, sql);
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter,StringBuffer sql) {
		if (initializingCache && LavenderePdaConfig.usaCondicaoComercialPedido && LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			addJoinCondComercialExcec(domainFilter, sql);
		}
	}

	@Override
	protected void addJoinSummary(BaseDomain domainFilter,StringBuffer sql) {
		addJoinCondComercialExcec(domainFilter, sql);
	}
	
	private void addJoinCondComercialExcec(BaseDomain domainFilter, StringBuffer sql) {
		ItemTabelaPreco itemTabelaPrecoFilter = (ItemTabelaPreco) domainFilter;
		sql.append(" left join tbLvpCondComercialExcec cond");
		sql.append(" on cond.cdEmpresa = tb.cdEmpresa");
		sql.append(" and cond.cdRepresentante = tb.cdRepresentante");
		sql.append(" and cond.cdProduto = tb.cdProduto");
		sql.append(" and cond.cdItemGrade1 = tb.cdItemGrade1");
		sql.append(" and cond.cdCondicaoComercial = ").append(Sql.getValue(itemTabelaPrecoFilter.condComercialExcec.cdCondicaoComercial));
	}
	
	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) domain;
		sql.append(" where tb.CDEMPRESA = ").append(Sql.getValue(itemTabelaPreco.cdEmpresa));
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdRepresentante)) {
			sql.append(" and tb.CDREPRESENTANTE = ").append(Sql.getValue(itemTabelaPreco.cdRepresentante));
		}
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdTabelaPreco)) {
			sql.append(" and tb.CDTABELAPRECO = ").append(Sql.getValue(itemTabelaPreco.cdTabelaPreco));
		}
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdProduto)) {
			sql.append(" and tb.CDPRODUTO = ").append(Sql.getValue(itemTabelaPreco.cdProduto));
		}
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdUf)) {
			sql.append(" and tb.CDUF = ").append(Sql.getValue(itemTabelaPreco.cdUf));
		}
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdUnidade)) {
			sql.append(" and tb.CDUNIDADE = ").append(Sql.getValue(itemTabelaPreco.cdUnidade));
		}
		if (itemTabelaPreco.cdPrazoPagtoPreco != 0) {
			sql.append(" and tb.CDPRAZOPAGTOPRECO = ").append(Sql.getValue(itemTabelaPreco.cdPrazoPagtoPreco));
		}
		if (!ValueUtil.isEmpty(itemTabelaPreco.cdItemGrade1)) {
			sql.append(" and tb.CDITEMGRADE1 = ").append(Sql.getValue(itemTabelaPreco.cdItemGrade1));
		}
		if (initializingCache) {
			sql.append(" and tb.CDPRODUTO in (select CDPRODUTO from ").append(LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? ProdutoTabPreco.TABLE_NAME : Produto.TABLE_NAME).append(") ");
		}
		if (LavenderePdaConfig.usaTabelaPrecoPorCliente && LavenderePdaConfig.isEntidadeSyncWebAppRepZero(ItemTabelaPreco.TABLE_NAME.substring(5))) {
			TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, false);
		}
		if (LavenderePdaConfig.selecionaTabelaPromocionalAoClicarNoProduto && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ValueUtil.isNotEmpty(itemTabelaPreco.flPromocao)) {
			sql.append(" AND TB.FLPROMOCAO = ").append(Sql.getValue(itemTabelaPreco.flPromocao));
		}
	}


	//-----------------------------------------------------------------
	// ENTIDADES DINAMICAS - METODOS ESPECIFICOS PARA CONSULTAS INCLUINDO COLUNAS DINAMICAS
	//-----------------------------------------------------------------

	protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemTabelaPreco.vlUnitario = rs.getDouble("vlUnitario");
		itemTabelaPreco.qtMaxVenda = rs.getDouble("qtMaxVenda");
		return itemTabelaPreco;
	}
	
	public int countItemTabelaPrecoQualquerGrade(String cdProduto, String[] cdsTabelaPreco) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select count(*) AS QTTABELAS from ").append(ItemTabelaPreco.TABLE_NAME)
			.append(" where CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa))
			.append(" and CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante))
			.append(" and CDPRODUTO = ").append(Sql.getValue(cdProduto))
			.append(" and (");
		for (String cdTabelaPreco : cdsTabelaPreco) {
			sql.append(" CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco)).append(" OR");
		}
		sql.delete(sql.length() - 3, sql.length());
		sql.append(")");
		return getInt(sql.toString());
	}
	
	public ItemTabelaPreco findTabelaPrecoComMaiorDescontoPromocional(String[] cdsTabelaPreco, String cdProduto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select CDTABELAPRECO, VLPCTMAXDESCONTO from ").append(ItemTabelaPreco.TABLE_NAME)
			.append(" where CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa))
			.append(" and CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante))
			.append(" and CDPRODUTO = ").append(Sql.getValue(cdProduto))
			.append(" and (");
		for (String cdTabelaPreco : cdsTabelaPreco) {
			sql.append(" CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco)).append(" OR");
		}
		sql.delete(sql.length() - 3, sql.length());
		sql.append(")");
		sql.append(" order by VLPCTDESCPROMOCIONAL desc");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {			
				ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
				itemTabelaPreco.cdTabelaPreco = rs.getString(1);
				itemTabelaPreco.vlPctMaxDesconto = rs.getDouble(2);
				return itemTabelaPreco;
			}
		}
		return null;
	}

	@Override
	public void initCacheByExample(BaseDomain domainFilter) throws SQLException {
		initializingCache = true;
		super.initCacheByExample(domainFilter);
		initializingCache = false;
	}
	
	public void setInitializingCache(boolean initializingCache) {
		this.initializingCache = initializingCache; 
	}

	public boolean hasItemTabelaPrecoZero() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) qt FROM ").append(ItemTabelaPreco.TABLE_NAME).append(" WHERE CDTABELAPRECO = '0' LIMIT 1");
		return getInt(sql.toString()) > 0;
	}

	public String findPrimeiroCdTabelaPrecoPromocional(ItemTabelaPreco itemTabelaPrecoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT TB.CDTABELAPRECO FROM ").append(tableName).append(" TB ");
		SqlWhereClause whereClause = new SqlWhereClause();
		whereClause.addAndCondition(" TB.CDEMPRESA = ", itemTabelaPrecoFilter.cdEmpresa);
		whereClause.addAndCondition(" TB.CDREPRESENTANTE = ", SessionLavenderePda.getCdRepresentanteFiltroDados(ItemTabelaPreco.class));
		whereClause.addAndCondition(" TB.CDPRODUTO = ", itemTabelaPrecoFilter.cdProduto);
		whereClause.addAndCondition(" TB.CDUF = ", itemTabelaPrecoFilter.cdUf);
		whereClause.addAndCondition(" TB.CDPRAZOPAGTOPRECO = ", itemTabelaPrecoFilter.cdPrazoPagtoPreco);
		whereClause.addAndCondition(" TB.CDITEMGRADE1 = ", itemTabelaPrecoFilter.cdItemGrade1);
		whereClause.addAndCondition(" TB.FLPROMOCAO = ", itemTabelaPrecoFilter.flPromocao);
		sql.append(whereClause.getSql());
		if (LavenderePdaConfig.usaSegmentoNoPedido && LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			if (LavenderePdaConfig.usaTabelaPrecoPorCliente) {
				TabelaPrecoCliPdbxDao.addTabPrecoCliFilterComSegmento(itemTabelaPrecoFilter, sql);
			} else {
				TabelaPrecoSegDbxDao.getInstance().addTabPrecoSegmentoFilter(itemTabelaPrecoFilter, sql);
			}
		} else if (LavenderePdaConfig.usaTabelaPrecoPorCliente) {
			TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, false);
		}
		addOrderBy(sql, itemTabelaPrecoFilter);
		addLimit(sql, itemTabelaPrecoFilter);
		String cdTabelaPreco = "";
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				cdTabelaPreco = rs.getString(1);
			}
		}
		return cdTabelaPreco;
	}
}