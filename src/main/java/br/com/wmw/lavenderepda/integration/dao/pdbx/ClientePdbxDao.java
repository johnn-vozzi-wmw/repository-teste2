package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import br.com.wmw.lavenderepda.business.domain.UF;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.ResultSetMetaData;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ClientePdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Cliente();
	}

    private static ClientePdbxDao instance;

    public ClientePdbxDao() {
        super(Cliente.TABLE_NAME);
    }

    public static ClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new ClientePdbxDao();
        }
        return instance;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" NUCNPJ,");
        sql.append(" NUFONE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLLIMITECREDITO,");
        sql.append(" FLESPECIAL,");
        sql.append(" QTDIASPRAZOPAGTOESTENDIDO,");
        if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento() || LavenderePdaConfig.isUsaModuloPagamento()) {
        	sql.append(" CDCONDICAOPAGAMENTO,");
        }
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() || !ValueUtil.isEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente) || LavenderePdaConfig.isUsaModuloPagamento()) {
			sql.append(" CDTIPOPAGAMENTO,");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			sql.append(" CDTIPOPEDIDO,");
		}
		sql.append(" DSBAIRROCOMERCIAL,");
		sql.append(" DSCIDADECOMERCIAL,");
		sql.append(" CDESTADOCOMERCIAL,");
		sql.append(" DSESTADOCOMERCIAL,");
		sql.append(" DSCEPCOMERCIAL,");
		sql.append(" NULOGRADOUROCOMERCIAL,");
		sql.append(" DSLOGRADOUROCOMERCIAL,");
		if (LavenderePdaConfig.usaPrecoPorUf) {
			sql.append(" DSUFPRECO,");
		}
		if (usaColunaRegiao()) {
			sql.append(" CDREGIAO,");
		}
    	if (!LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
    		sql.append(" FLSTATUSCLIENTE,");
    	} else if (LavenderePdaConfig.permiteInativarClienteProspeccao){
			sql.append(" FLSTATUSCLIENTE as FLSTATUSCLIENTEREAL,");
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.controlaVencimentoAlvaraCliente > 0) {
			sql.append(" DTVENCIMENTOALVARA,");
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			sql.append(" NULICENCAPRODUTOCONTROLADO,");
		}
		if (LavenderePdaConfig.isBloqueiaCondPagtoPorDiasCliente() || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			sql.append(" QTDIASMAXIMOPAGAMENTO,");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
			sql.append(" CDROTAENTREGA,");
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" VLINDICEFINANCEIRO,");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido || LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido()) {
			sql.append(" CDCONTRATOESPECIAL,");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			sql.append(" CDTIPOENTREGA,");
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesPorCliente) {
			sql.append(" FLCONSISTECONVERSAOUNIDADE,");
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			sql.append(" FLAPLICAST,");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			sql.append(" FLOPTANTESIMPLES,");
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			sql.append(" CDTRIBUTACAOCLIENTE,");
			sql.append(" CDTRIBUTACAOCLIENTE2,");
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			sql.append(" VLINDICECOMISSAO,");
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
			sql.append(" VLINDICEFINANCEIROESPECIAL,");
		}
		if (LavenderePdaConfig.indiceFinanceiroCondPagtoClientePorDias || LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
			sql.append(" VLINDICEFINANCEIROCONDPAGTO,");
		}
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			sql.append(" FLCREDITOANTECIPADO,");
		}
		if (LavenderePdaConfig.usaLeadTimePadraoClienteNoPedido) {
			sql.append(" NUDIASPREVISAOENTREGA,");
		}
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0) {
			sql.append(" DTATUALIZACAOCADASTRO,");
		}
		if (LavenderePdaConfig.usaTransportadoraPedido()) {
			sql.append(" CDTRANSPORTADORA,");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() || LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			sql.append(" CDRAMOATIVIDADE,");
		}
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sql.append(" CDCLASSIFICACAO,");
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente() || LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente) {
			sql.append(" CDRESTRICAOVENDACLI,");
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			sql.append(" CDCIDADECOMERCIAL,");
		}
		if (LavenderePdaConfig.usaLocalEstoquePorCliente()) {
			sql.append(" CDLOCALESTOQUE,");
		}
		if (LavenderePdaConfig.controlaVencimentoAfeCliente > 0) {
			sql.append(" DTVENCIMENTOAFE,");
		}
		if (LavenderePdaConfig.usaGrupoCondPagtoCli) {
			sql.append(" CDGRUPOCONDICAO,");
		}
		sql.append(" CDCATEGORIA,");
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" CDGRUPODESCCLI,");
		}
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente() || LavenderePdaConfig.isUsaNavegacaoGpsNoMapa() || LavenderePdaConfig.exibeBotaoClientesNoMapa() || LavenderePdaConfig.exibeBotaoRotaParaClientes()) {
			sql.append(" CDLATITUDE,");
			sql.append(" CDLONGITUDE,");
		}
		sql.append(" VLMAXPEDIDO,");
		sql.append(" VLMINPEDIDO,");
		sql.append(" FLDIVIDEVLMIN,");
		if (LavenderePdaConfig.apresentaListaClienteRedeTituloFinanceiro || LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes || LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() || LavenderePdaConfig.usaFiltroTipoClienteRede || LavenderePdaConfig.usaVolumeVendaMensalRede) {
			sql.append(" CDREDE,");
		}
		if (LavenderePdaConfig.isUsaDescontoMaximoItemPorCliente() || LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo()) {
			sql.append(" VLPCTMAXDESCONTO,");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			sql.append(" CDCANALCLIENTE,");
			sql.append(" VLPCTCONTRATOCLI,");
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || LavenderePdaConfig.isUsaGeracaoPdfPedido()) {
			sql.append(" VLPCTICMS,");
			sql.append(" NUINSCRICAOESTADUAL,");
			sql.append(" FLDEBITAPISCOFINSZONAFRANCA,");
		}
		if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
        	sql.append(" FLTIPOCADASTRO,");
        }
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			sql.append(" FLKEYACCOUNT,");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			sql.append(" CDSTATUSRENTCLI,");
		}
		if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueado) {
			sql.append(" DSSITUACAO,");
		}
		if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
			sql.append(" FLRECEBEEMAIL,");
		}
		if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
			sql.append(" FLRECEBESMS,");
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente) {
			sql.append(" CDGRUPOPERMPROD,");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			sql.append(" VLPCTMINDESCONTO,");
		}
	    if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.permiteInserirEmailAlternativoPedEmOrcamento) {
			sql.append(" DSEMAIL,");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			sql.append(" flConsignaPedido,");
			sql.append(" vlLimiteCreditoConsig,");
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				sql.append(" vlPctDevolucaoConsig,");
			}
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.usaFretePedidoPorToneladaCliente) {
			sql.append(" VLTONFRETE,");
		}
		if (LavenderePdaConfig.usaTransportadoraPreferencialCliente) {
			sql.append(" CDTRANSPORTADORACIF,");
			sql.append(" CDTRANSPORTADORAFOB,");
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			sql.append(" VLVENDAMENSAL,");
		}
		if (LavenderePdaConfig.isUsaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso()) {
			sql.append(" VLMAXTITULOSATRASO,");
			sql.append(" NUDIASTOLERANCIAATRASO,");
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			sql.append("FLCONSUMIDORFINAL,");
			sql.append("FLCONTRIBUINTE,");
		}

		if (LavenderePdaConfig.usaProdutoRestrito) {
			sql.append("flPermiteProdutoRestrito,");
			sql.append("VLPCTDESCPRODUTORESTRITO,");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			sql.append("vlPctVpc,");
		}
		if (LavenderePdaConfig.habilitaAtualizacaoCadastroCliente) {
			sql.append(" DTATUALIZACAOCADASTROWMW,")
			.append(" FLATUALIZACADASTROWMW,");
		}
		sql.append(" FLATENDIDO,");
		sql.append(" DTCADASTRO,");
		if (LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
			sql.append(" DTABERTURA,");
			sql.append(" DTFUNDACAO,");
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			sql.append(" FLFRETEEMBUTIDO,");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			sql.append(" DSOBSERVACAO,");
		}
		sql.append(" NUDIASSEMPEDIDO,");
		sql.append(" FLOCULTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" VLTICKETMEDIO,");
		sql.append(" CDCONDICAOCOMERCIAL,");
		sql.append(" CDCONDICAOPAGTOBLOQUEADA, ");
		sql.append(" VLPCTDESCHISTORICOVENDAS, ");
		sql.append(" CDVENCIMENTOADICBOLETO, ");
		sql.append(" DSDESCONTOBLOQUEADOLIST, ");
		sql.append(" DSACRESCIMOBLOQUEADOLIST, ");
		if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
			sql.append(" FLBOLETOACEITE, ");
		}
		sql.append(" CDCANAL,");
		sql.append(" CDSEGMENTO,");
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLINDICEIMPOSTOS,");
		}
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
		    sql.append(" CDFAIXACLI,");
	    }
		sql.append(" FLOBRIGAORDEMCOMPRA");
		if (LavenderePdaConfig.usaCodigosProdutosParaClientes()) {
			sql.append(", FLCODIGOINTERNOCLIENTE");
		}
		if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
			sql.append(", ").append("CASE WHEN TPRET.DTMAXRETIRADA <= DATE () THEN 1 WHEN TPRET.DTMAXRETIRADA > DATE () THEN 2 ELSE 0 END FLSTATUSRETIRADA");
	    }
		if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto()) {
			sql.append(", FLIGNORAVERBAGRPPROD");
		}
    }

	private boolean usaColunaRegiao() {
		return (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao())
			|| LavenderePdaConfig.filtraTabelaPrecoPelaRegiaoDoCliente || LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0 
			|| LavenderePdaConfig.usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente || LavenderePdaConfig.aplicaVariacaoPrecoProdutoPorCliente || LavenderePdaConfig.usaCalculoFretePersonalizado();
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.rowKey = rs.getString("rowkey");
        cliente.cdEmpresa = rs.getString("cdEmpresa");
        cliente.cdRepresentante = rs.getString("cdRepresentante");
        cliente.cdCliente = rs.getString("cdCliente");
        cliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
        cliente.nmFantasia = rs.getString("nmFantasia");
        cliente.nuCnpj = rs.getString("nuCnpj");
        cliente.nuFone = rs.getString("nuFone");
        cliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
        cliente.vlLimiteCredito = ValueUtil.round(rs.getDouble("vlLimiteCredito"));
        cliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cliente.vlTicketMedio = rs.getDouble("vlTicketMedio");
        cliente.flEspecial = rs.getString("flEspecial");
        cliente.qtDiasPrazoPagtoEstendido = rs.getInt("qtDiasPrazoPagtoEstendido");
        if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento() || LavenderePdaConfig.isUsaModuloPagamento()) {
        	cliente.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        }
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() || !ValueUtil.isEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente) || LavenderePdaConfig.isUsaModuloPagamento()) {
			cliente.cdTipoPagamento = rs.getString("cdTipoPagamento");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			cliente.cdTipoPedido = rs.getString("cdTipoPedido");
		}
		cliente.dsBairroComercial = rs.getString("dsBairroComercial");
		cliente.dsCidadeComercial = rs.getString("dsCidadeComercial");
		cliente.cdEstadoComercial = rs.getString("cdEstadoComercial");
		cliente.dsEstadoComercial = rs.getString("dsEstadoComercial");
		cliente.dsCepComercial = rs.getString("dsCepComercial");
		cliente.nuLogradouroComercial = rs.getString("nuLogradouroComercial");
		cliente.dsLogradouroComercial = rs.getString("dsLogradouroComercial");
		if (LavenderePdaConfig.usaPrecoPorUf) {
			cliente.dsUfPreco = rs.getString("dsUfPreco");
		}
		if (usaColunaRegiao()) {
			cliente.cdRegiao = rs.getString("cdRegiao");
		}
    	if (!LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
    		cliente.setFlStatusCliente(rs.getString("flStatusCliente"));
    	} else if (LavenderePdaConfig.permiteInativarClienteProspeccao){
			cliente.flStatusClienteReal = rs.getString("flStatusClienteReal");
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.controlaVencimentoAlvaraCliente > 0) {
			cliente.dtVencimentoAlvara = rs.getDate("dtVencimentoAlvara");
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			cliente.nuLicencaProdutoControlado = rs.getString("nuLicencaProdutoControlado");
		}
		if (LavenderePdaConfig.isBloqueiaCondPagtoPorDiasCliente() || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			cliente.qtDiasMaximoPagamento = rs.getInt("qtDiasMaximoPagamento");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
			cliente.cdRotaEntrega = rs.getString("cdRotaEntrega");
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			cliente.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido || LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido()) {
			cliente.cdContratoEspecial = rs.getString("cdContratoEspecial");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			cliente.cdTipoEntrega = rs.getString("cdTipoEntrega");
		}
		if (LavenderePdaConfig.consisteConversaoUnidadesPorCliente) {
			cliente.flConsisteConversaoUnidade = rs.getString("flConsisteConversaoUnidade");
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			cliente.flAplicaSt = rs.getString("flAplicaSt");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			cliente.flOptanteSimples = rs.getString("flOptanteSimples");
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			cliente.cdTributacaoCliente = rs.getString("cdTributacaoCliente");
			cliente.cdTributacaoCliente2 = rs.getString("cdTributacaoCliente2");
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			cliente.vlIndiceComissao = rs.getDouble("vlIndiceComissao");
		}
    	if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
    		cliente.vlIndiceFinanceiroEspecial = rs.getDouble("vlIndiceFinanceiroEspecial");
    	}
		if (LavenderePdaConfig.indiceFinanceiroCondPagtoClientePorDias || LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
			cliente.vlIndiceFinanceiroCondPagto = rs.getDouble("vlIndiceFinanceiroCondPagto");
		}
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			cliente.flCreditoAntecipado = rs.getString("flCreditoAntecipado");
		}
		if (LavenderePdaConfig.usaLeadTimePadraoClienteNoPedido) {
			cliente.nuDiasPrevisaoEntrega = rs.getInt("nuDiasPrevisaoEntrega");
		}
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0) {
			cliente.dtAtualizacaoCadastro = rs.getDate("dtAtualizacaoCadastro");
		}
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0) {
			cliente.dtAtualizacaoCadastro = rs.getDate("dtAtualizacaoCadastro");
		}
		if (LavenderePdaConfig.usaTransportadoraPedido()) {
			cliente.cdTransportadora = rs.getString("cdTransportadora");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() || LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			cliente.cdRamoAtividade = rs.getString("cdRamoAtividade");
		}
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			cliente.cdClassificacao = rs.getString("cdClassificacao");
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente() || LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente) {
			cliente.cdRestricaoVendaCli = rs.getString("cdRestricaoVendaCli");
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			cliente.cdCidadeComercial = rs.getString("cdCidadeComercial");
		}
		if (LavenderePdaConfig.usaLocalEstoquePorCliente()) {
			cliente.cdLocalEstoque = rs.getString("cdLocalEstoque");
		}
		if (LavenderePdaConfig.controlaVencimentoAfeCliente > 0) {
			cliente.dtVencimentoAfe = rs.getDate("dtVencimentoAfe");
		}
		if (LavenderePdaConfig.usaGrupoCondPagtoCli) {
			cliente.cdGrupoCondicao = rs.getString("cdGrupoCondicao");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.usaPoliticaComercial() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			cliente.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
		}
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente() || LavenderePdaConfig.isUsaNavegacaoGpsNoMapa() || LavenderePdaConfig.exibeBotaoClientesNoMapa() || LavenderePdaConfig.exibeBotaoRotaParaClientes()) {
			cliente.cdLatitude = rs.getDouble("cdLatitude");
			cliente.cdLongitude = rs.getDouble("cdLongitude");
		}
        //----
		cliente.cdCategoria = rs.getString("cdCategoria");
        cliente.vlTotalPedidosValidateLimiteCredito = -1;
        cliente.vlMaxPedido = rs.getDouble("vlMaxPedido");
        cliente.vlMinPedido = rs.getDouble("vlMinPedido");
        cliente.flDivideVlMin = rs.getString("flDivideVlMin");
        if (LavenderePdaConfig.isUsaDescontoMaximoItemPorCliente() || LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo()) {
        	cliente.vlPctMaxDesconto = rs.getDouble("vlPctMaxDesconto");
		}
        if (LavenderePdaConfig.apresentaListaClienteRedeTituloFinanceiro || LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes || LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() || LavenderePdaConfig.usaFiltroTipoClienteRede || LavenderePdaConfig.usaVolumeVendaMensalRede) {      
        	cliente.cdRede = rs.getString("cdRede");
		}
        if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
        	cliente.cdCanalCliente = rs.getString("cdCanalCliente");
        	cliente.vlPctContratoCli = rs.getDouble("vlPctContratoCli");
        }
        if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || LavenderePdaConfig.isUsaGeracaoPdfPedido()) {
        	cliente.vlPctIcms = rs.getDouble("vlPctIcms");
        	cliente.nuInscricaoEstadual = rs.getString("nuInscricaoEstadual");
        	cliente.flDebitaPisCofinsZonaFranca = rs.getString("flDebitaPisCofinsZonaFranca");
		}
        if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
        	cliente.flTipoCadastro = rs.getString("flTipoCadastro");
        }
        if (LavenderePdaConfig.usaClienteKeyAccount) {
        	cliente.flKeyAccount = rs.getString("flKeyAccount");
        }
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			cliente.cdStatusRentCli = rs.getString("cdStatusRentCli");
		}
        if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueado) {
        	cliente.dsSituacao = rs.getString("dsSituacao");
        }
		if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
			cliente.flRecebeEmail = rs.getString("flRecebeEmail");
		}
		if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
			cliente.flRecebeSMS = rs.getString("flRecebeSMS");
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente) {
			cliente.cdGrupoPermProd = rs.getString("cdGrupoPermProd");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			cliente.vlPctMinDesconto = rs.getDouble("vlPctMinDesconto");
		}
	    if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.permiteInserirEmailAlternativoPedEmOrcamento) {
			cliente.dsEmail = rs.getString("DSEMAIL");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			cliente.flConsignaPedido = rs.getString("flConsignaPedido");
			cliente.vlLimiteCreditoConsig = rs.getDouble("vlLimiteCreditoConsig");
			cliente.vlTotalPedidosValidateLimiteCreditoConsignado = -1;
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				cliente.vlPctDevolucaoConsig = rs.getDouble("vlPctDevolucaoConsig");
			}
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.usaFretePedidoPorToneladaCliente) {
			cliente.vlTonFrete = rs.getDouble("VLTONFRETE");
		}
		if (LavenderePdaConfig.usaTransportadoraPreferencialCliente) {
			cliente.cdTransportadoraCif = rs.getString("cdTransportadoraCif");
			cliente.cdTransportadoraFob = rs.getString("cdTransportadoraFob");
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			cliente.vlVendaMensal = rs.getDouble("vlVendaMensal");
		}
		if (LavenderePdaConfig.isUsaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso()) {
			cliente.vlMaxTitulosAtraso = rs.getDouble("vlMaxTitulosAtraso");
			cliente.nuDiasToleranciaAtraso = rs.getInt("nuDiasToleranciaAtraso");
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			cliente.flContribuinte = rs.getString("flContribuinte");
			cliente.flConsumidorFinal = rs.getString("flConsumidorFinal");
		}

		if (LavenderePdaConfig.usaProdutoRestrito) {
			cliente.flPermiteProdutoRestrito = rs.getString("flPermiteProdutoRestrito");
			cliente.vlPctDescProdutoRestrito = rs.getDouble("vlPctDescProdutoRestrito");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			cliente.vlPctVpc = rs.getDouble("vlPctVpc");
		}
		if (LavenderePdaConfig.habilitaAtualizacaoCadastroCliente) {
			cliente.dtAtualizacaoCadastroWmw = rs.getDate("dtAtualizacaoCadastroWmw");
			cliente.flAtualizaCadastroWmw = rs.getString("flAtualizaCadastroWmw");
		}
		cliente.flAtendido = rs.getString("flAtendido");
		cliente.nuDiasSemPedido = rs.getInt("nuDiasSemPedido");
		cliente.nuDiasSemPedidoInvalido = rs.getObject("nuDiasSemPedido") == null;
		cliente.dtCadastro = rs.getDate("dtCadastro");
		if (LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
			cliente.dtAbertura = rs.getDate("dtAbertura");
			cliente.dtFundacao = rs.getDate("dtFundacao");			
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			cliente.flFreteEmbutido = rs.getString("flFreteEmbutido");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			cliente.dsObservacao = rs.getString("dsObservacao");
		}
		cliente.flOculto = rs.getString("flOculto");
		cliente.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
		cliente.cdCondicaoPagtoBloqueada = rs.getString("cdCondicaoPagtoBloqueada");
		cliente.vlPctDescHistoricoVendas = rs.getDouble("vlPctDescHistoricoVendas");
		cliente.cdVencimentoAdicBoleto = rs.getString("cdVencimentoAdicBoleto");
		cliente.dsDescontoBloqueadoList = rs.getString("dsDescontoBloqueadoList");
		cliente.dsAcrescimoBloqueadoList = rs.getString("dsAcrescimoBloqueadoList");
		if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
			cliente.flBoletoAceite = rs.getString("flBoletoAceite");
		}
		cliente.cdCanal = rs.getString("cdCanal");
		cliente.cdSegmento = rs.getString("cdSegmento");
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			cliente.vlIndiceImpostos = rs.getDouble("vlIndiceImpostos");
		}

		if (LavenderePdaConfig.usaCodigosProdutosParaClientes()) {
			cliente.flCodigoInternoCliente = rs.getString("flCodigoInternoCliente");
		}
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
		    cliente.cdFaixaCli = rs.getString("cdFaixaCli");
	    }
	    if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
	    	cliente.flStatusRetirada = rs.getString("flStatusRetirada");
	    }
		cliente.flObrigaOrdemCompra = rs.getString("flObrigaOrdemCompra");  
		if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto()) {
			cliente.flIgnoraVerbaGrpProd = rs.getString("flIgnoraVerbaGrpProd");
		}
		return cliente;
    }

    //@Override
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
		if (domainFilter != null) {
			Cliente clienteFilter = (Cliente) domainFilter;
			if (clienteFilter.isTotalizadorConsulta) {
				cliente.cdEmpresa = clienteFilter.cdEmpresa;
				cliente.cdRepresentante = rs.getString(2);
				cliente.cdCliente = rs.getString(3);
				cliente.flAtendido = rs.getString(4);
				String flStatus = rs.getString(5);
				cliente.setFlStatusCliente(flStatus);
				if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
					cliente.setFlStatusClienteFichaFin(flStatus);
				}
				return cliente;
			}
		}
        cliente.cdEmpresa = rs.getString("cdEmpresa");
        cliente.cdRepresentante = rs.getString("cdRepresentante");
        cliente.cdCliente = rs.getString("cdCliente");
        cliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
        cliente.nmFantasia = rs.getString("nmFantasia");
        if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido || LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() || LavenderePdaConfig.isUsaPoliticaBonificacaoCategoria()) {
            cliente.cdCategoria = rs.getString("cdCategoria");
        }
        if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido) {
            cliente.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        }
        if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
        	cliente.vlIndiceFinanceiroEspecial = rs.getDouble("vlIndiceFinanceiroEspecial");
        }
        if (LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
        	cliente.flEspecial = rs.getString("flEspecial");
    	}
        if (LavenderePdaConfig.usaPesquisaMercado) {
        	cliente.dsUfPreco = rs.getString("dsUfPreco");
        }
        if (LavenderePdaConfig.usaConfigCalculoComissao()) {
        	cliente.vlIndiceComissao = rs.getDouble("vlIndiceComissao");
        }
        if (LavenderePdaConfig.anulaDescontoPessoaFisica) {
        	cliente.nuCnpj = rs.getString("nuCnpj");
        }
        if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
        	cliente.flOptanteSimples = rs.getString("flOptanteSimples");
        }
        if (LavenderePdaConfig.indiceFinanceiroCondPagtoClientePorDias || LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
        	cliente.vlIndiceFinanceiroCondPagto = rs.getDouble("vlIndiceFinanceiroCondPagto");
        }
        if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
        	cliente.flTipoCadastro = rs.getString("flTipoCadastro");
        }
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
        	cliente.cdStatusRentCli = rs.getString("cdStatusRentCli");
        }
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() || LavenderePdaConfig.usaFiltroTipoClienteRede || LavenderePdaConfig.usaVolumeVendaMensalRede) {
			cliente.cdRede = rs.getString("cdRede");
		}
        if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
        	cliente.dsLogradouroComercial = rs.getString("dsLogradouroComercial");
        	cliente.nuLogradouroComercial = rs.getString("nuLogradouroComercial");
        }
        cliente.dtCadastro = rs.getDate("dtCadastro");
        if (LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
        	cliente.dtAbertura = rs.getDate("dtAbertura");
        	cliente.dtFundacao = rs.getDate("dtFundacao");			
		}
        cliente.nuDiasSemPedido = rs.getInt("nuDiasSemPedido");
        cliente.nuDiasSemPedidoInvalido = rs.getObject("nuDiasSemPedido") == null;
        cliente.flAtendido = rs.getString("flAtendido");
        String flStatus = rs.getString("flStatusCliente");
        cliente.setFlStatusCliente(flStatus);
		if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
			cliente.setFlStatusClienteFichaFin(flStatus);
			if (LavenderePdaConfig.permiteInativarClienteProspeccao){
				cliente.flStatusClienteReal = rs.getString("flStatusClienteReal");
			}
		}
        cliente.flOculto = rs.getString("flOculto");
        cliente.dsBairroComercial = rs.getString("dsBairroComercial");
        cliente.dsCidadeComercial = rs.getString("dsCidadeComercial");
        cliente.cdEstadoComercial = rs.getString("cdEstadoComercial");
        cliente.dsEstadoComercial = rs.getString("dsEstadoComercial");
        cliente.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        cliente.cdCondicaoPagtoBloqueada = rs.getString("cdCondicaoPagtoBloqueada");
        cliente.cdVencimentoAdicBoleto = rs.getString("cdVencimentoAdicBoleto");
	    if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
	    	cliente.flBoletoAceite = rs.getString("flBoletoAceite");
	    }
	    if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
	    	cliente.flStatusRetirada = rs.getString("flStatusRetirada");
	    }
    	if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && ValueUtil.isNotEmpty(cliente.cdRede)) {
    		populateSummaryJoinRede(rs, cliente);
    	}
       	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && ValueUtil.isNotEmpty(cliente.cdCategoria)) {
    		populateSummaryJoinCategoria(rs, cliente);
    	}
       	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
       		cliente.cdMarcadores = rs.getString("cdMarcadores");
       	}
        return cliente;
    }

	private void populateSummaryJoinCategoria(ResultSet rs, Cliente cliente) throws SQLException {
		cliente.categoria = new Categoria();
		cliente.categoria.cdCategoria = cliente.cdCategoria;
		cliente.categoria.dsCategoria = rs.getString("dsCategoria");
	}

	private void populateSummaryJoinRede(ResultSet rs, Cliente cliente) throws SQLException {
		cliente.rede = new Rede();
		cliente.rede.cdRede = cliente.cdRede;
		cliente.rede.dsRede = rs.getString("dsRede");
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" ROWKEY");
    }
    //@Override
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {

		if (domain != null) {
			Cliente cliente = (Cliente) domain;
			if (cliente.isTotalizadorConsulta) {
				sql.append(" TB.CDEMPRESA,");
				sql.append(" TB.CDREPRESENTANTE,");
				sql.append(" TB.CDCLIENTE,");
				sql.append(" TB.FLATENDIDO,");
				sql.append(" TB.FLSTATUSCLIENTE");
				return;
			}
		}

        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDCLIENTE,");
        sql.append(" TB.NMRAZAOSOCIAL,");
        sql.append(" TB.NMFANTASIA,");
        if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido || LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() || LavenderePdaConfig.isUsaPoliticaBonificacaoCategoria()) {
            sql.append(" TB.CDCATEGORIA,");
        }
        if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido) {
            sql.append(" TB.VLINDICEFINANCEIRO,");
        }
        if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
        	sql.append(" TB.VLINDICEFINANCEIROESPECIAL,");
        }
        if (LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
            sql.append(" TB.FLESPECIAL,");
    	}
        if (LavenderePdaConfig.usaPesquisaMercado) {
            sql.append(" TB.DSUFPRECO,");
        }
        if (LavenderePdaConfig.usaConfigCalculoComissao()) {
        	sql.append(" TB.VLINDICECOMISSAO,");
        }
        if (LavenderePdaConfig.anulaDescontoPessoaFisica) {
        	sql.append(" TB.NUCNPJ,");
        }
        if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
        	sql.append(" TB.FLOPTANTESIMPLES,");
        }
        if (LavenderePdaConfig.indiceFinanceiroCondPagtoClientePorDias || LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
        	sql.append(" TB.VLINDICEFINANCEIROCONDPAGTO,");
        }
        if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
        	sql.append(" TB.FLTIPOCADASTRO,");
        }
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			sql.append(" TB.CDSTATUSRENTCLI,");
		}
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() || LavenderePdaConfig.usaFiltroTipoClienteRede || LavenderePdaConfig.usaVolumeVendaMensalRede) {
			sql.append(" TB.CDREDE,");
		}
        if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
        	sql.append(" TB.DSLOGRADOUROCOMERCIAL,");
        	sql.append(" TB.NULOGRADOUROCOMERCIAL,");
        }
        if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
        	sql.append(" FF.FLSTATUSCLIENTE,");
			if (LavenderePdaConfig.permiteInativarClienteProspeccao){
				sql.append(" TB.FLSTATUSCLIENTE AS FLSTATUSCLIENTEREAL,");
			}
        } else {
        	sql.append(" TB.FLSTATUSCLIENTE,");
        }
        sql.append(" TB.DTCADASTRO,");        
        if (LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
        	sql.append(" DTABERTURA,");
        	sql.append(" DTFUNDACAO,");			
		}
        sql.append(" NUDIASSEMPEDIDO,");
        sql.append(" FLATENDIDO,");
        sql.append(" FLOCULTO,");
        sql.append(" DSBAIRROCOMERCIAL,");
        sql.append(" DSCIDADECOMERCIAL,");
        sql.append(" CDESTADOCOMERCIAL,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSESTADOCOMERCIAL,");
        sql.append(" CDCONDICAOPAGTOBLOQUEADA, ");
        sql.append(" CDVENCIMENTOADICBOLETO");
        if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
        	sql.append(", FLBOLETOACEITE");
        }
        if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {
        	sql.append(", NMCONTATOPRINCIPAL");
        	sql.append(", TC.NMCONTATO");
        	sql.append(", TC.CDCONTATO");
        	sql.append(", TCERP.NMCONTATO");
        }
        if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
        	sql.append(", ").append(DaoUtil.ALIAS_REDE).append(".DSREDE");
    }
        if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
        	sql.append(", ").append(DaoUtil.ALIAS_CATEGORIA).append(".DSCATEGORIA");
        }
        if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
        	sql.append(", ").append("CASE WHEN TPRET.DTMAXRETIRADA <= DATE () THEN 1 WHEN TPRET.DTMAXRETIRADA > DATE () THEN 2 ELSE 0 END FLSTATUSRETIRADA");
		}
        if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
        	DaoUtil.addSelectMarcadores(sql);
        }
    }
   
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	Cliente cliente = (Cliente) domain;
        sql.append(Sql.getValue(cliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(cliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(cliente.cdCliente)).append(",");
        sql.append(Sql.getValue(cliente.nmRazaoSocial)).append(",");
        sql.append(Sql.getValue(cliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cliente.cdUsuario)).append(",");
        sql.append(Sql.getValue(cliente.rowKey));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cliente cliente = (Cliente) domain;
        if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente()) {
        	sql.append(" CDLATITUDE = ").append(Sql.getValue(cliente.cdLatitude)).append(",");
        	sql.append(" CDLONGITUDE = ").append(Sql.getValue(cliente.cdLongitude)).append(",");
        }
		if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
			sql.append(" FLRECEBEEMAIL = ").append(Sql.getValue(cliente.flRecebeEmail)).append(",");
		}
		if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
			sql.append(" FLRECEBESMS = ").append(Sql.getValue(cliente.flRecebeSMS)).append(",");
		}
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cliente.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
        	sql.append(" VLVENDAMENSAL = ").append(Sql.getValue(cliente.vlVendaMensal)).append(",");
       	}
		if (LavenderePdaConfig.permiteInativarClienteProspeccao) {
			sql.append(" FLSTATUSCLIENTE = ").append(Sql.getValue(cliente.flStatusCliente)).append(",");
		}
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cliente.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cliente cliente = (Cliente) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", cliente.cdEmpresa);
        sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", cliente.cdRepresentante);
        sqlWhereClause.addAndCondition("TB.CDCLIENTE = ", cliente.cdClienteExato);
        sqlWhereClause.addAndCondition("TB.CDCLIENTE != ", cliente.cdClienteDif);
        if (!LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {
        	sqlWhereClause.addAndCondition("TB.NUCNPJ = ", cliente.nuCnpj);
        }
        if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
        	sqlWhereClause.addAndCondition("FF.FLSTATUSCLIENTE = ", cliente.flStatusClienteFilter);
        } else {
        	sqlWhereClause.addAndCondition("TB.FLSTATUSCLIENTE = ", cliente.flStatusClienteFilter);
        }
        sqlWhereClause.addAndCondition("TB.CDREDE = ", cliente.cdRede);
        sqlWhereClause.addAndCondition("TB.CDCATEGORIA = ", cliente.cdCategoria);
        sqlWhereClause.addAndCondition("TB.NUDIASSEMPEDIDO >= ", cliente.nuDiasSemPedido);
		if (ValueUtil.VALOR_NAO.equals(cliente.flAtendido)) {
			sqlWhereClause.addAndCondition("FLATENDIDO = ", ValueUtil.VALOR_NAO);
		}
        //--
        sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("NMRAZAOSOCIAL", cliente.nmRazaoSocial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("TB.CDCLIENTE", cliente.cdCliente, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("NMFANTASIA", cliente.nmFantasia, false);
       	if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("DSLOGRADOUROCOMERCIAL", cliente.dsLogradouroComercial, false);
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("NULOGRADOUROCOMERCIAL", cliente.nuLogradouroComercial, false);
       	}
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("DSCIDADECOMERCIAL", cliente.dsCidadeComercial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("DSBAIRROCOMERCIAL", cliente.dsBairroComercial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("DSESTADOCOMERCIAL", cliente.dsEstadoComercial, false);
       	if (LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("NUCNPJ", cliente.nuCnpj, false);
       	}
       	if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {       		
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("NMCONTATOPRINCIPAL", cliente.nmContatoCliente, false);
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("TC.NMCONTATO", cliente.nmContatoCliente, false);
       		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("TCERP.NMCONTATO", cliente.nmContatoCliente, false);
       	}
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
       	sqlWhereClause.addAndCondition("CDESTADOCOMERCIAL = ", cliente.cdEstadoComercialFilter);
       	sqlWhereClause.addAndCondition("DSCIDADECOMERCIAL = ", cliente.dsCidadeComercialFilter);
       	sqlWhereClause.addAndCondition("DSBAIRROCOMERCIAL = ", cliente.dsBairroComercialFilter);
       	sqlWhereClause.addAndCondition("DSCEPCOMERCIAL >= ", cliente.cepInicialFilter);
       	sqlWhereClause.addAndCondition("DSCEPCOMERCIAL <= ", cliente.cepFinalFilter);
	    if (cliente.isModoDeProspeccao()) {
	    	sqlWhereClause.addAndCondition("FLTIPOCADASTRO = ", Cliente.TIPO_PROSPECTS);
	    } else if (ValueUtil.isNotEmpty(cliente.flTipoCadastro) && !ValueUtil.valueEqualsIfNotNull(cliente.flTipoCadastro, Cliente.TIPO_PROSPECTS)) {
	    	sqlWhereClause.addAndCondition("(FLTIPOCADASTRO <> ", Cliente.TIPO_PROSPECTS);
	    	sqlWhereClause.addOrCondition(" FLTIPOCADASTRO IS NULL)");
	    }
	    if (ValueUtil.isNotEmpty(cliente.flTipoClienteRede)) {
	    	if (cliente.isTipoClienteIndividual()) {
	    		sqlWhereClause.addAndCondition("TB.CDREDE = ''"); 
	    	} else {
	    		sqlWhereClause.addAndConditionNotEquals("TB.CDREDE", "");
	    	}
	    }
		if (ValueUtil.isNotEmpty(cliente.flOculto)) {
			sqlWhereClause.addAndCondition(" (FLOCULTO <>", cliente.flOculto);
			sqlWhereClause.addOrCondition(" FLOCULTO IS NULL)");
		}
		if (ValueUtil.isNotEmpty(cliente.flFiltroStatusClienteTipoRequisicao)) {
			if (ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_RESTRITO, cliente.flFiltroStatusClienteTipoRequisicao)) {
				sqlWhereClause.addAndConditionIn(" FLSTATUSCLIENTE ", StringUtil.split(StringUtil.getStringValue(cliente.dsStatusClienteListTipoRequisicao), RequisicaoServTipo.SEPARADOR_CAMPOS));
			}
			if (ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_EXCECAO, cliente.flFiltroStatusClienteTipoRequisicao)) {
				sqlWhereClause.addAndConditionNotIn(" FLSTATUSCLIENTE ", StringUtil.split(StringUtil.getStringValue(cliente.dsStatusClienteListTipoRequisicao), RequisicaoServTipo.SEPARADOR_CAMPOS));
			}
		}
       	sql.append(sqlWhereClause.getSql());
       	
       	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && ValueUtil.isNotEmpty(cliente.cdMarcador)) {
       		sql.append(" AND EXISTS(SELECT cdMarcador from TBLVPMARCADORCLIENTE ml where ml.cdEmpresa = tb.cdEmpresa and ml.cdRepresentante = tb.CDREPRESENTANTE AND ml.cdCliente = tb.CDCLIENTE AND ml.cdMarcador = ").append(Sql.getValue(cliente.cdMarcador)).append(") ");
       	}
       	if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && ValueUtil.isNotEmpty(cliente.cdFornecedor)) {
       		sql.append(" AND NOT EXISTS (")
       		.append("SELECT 1 FROM TBLVPPEDIDOERP ERP ")
       		.append(" JOIN TBLVPITEMPEDIDOERP IERP ON ERP.CDEMPRESA = IERP.CDEMPRESA AND ")
       		.append(" ERP.CDREPRESENTANTE = IERP.CDREPRESENTANTE AND ")
       		.append(" ERP.NUPEDIDO = IERP.NUPEDIDO AND ")
       		.append(" ERP.FLORIGEMPEDIDO = IERP.FLORIGEMPEDIDO ")
       		.append(" JOIN TBLVPPRODUTO PROD ON PROD.CDEMPRESA = ERP.CDEMPRESA AND ")
       		.append(" PROD.CDREPRESENTANTE = ERP.CDREPRESENTANTE AND ")
       		.append(" PROD.CDPRODUTO = IERP.CDPRODUTO ")
       		.append(" WHERE ERP.CDEMPRESA = TB.CDEMPRESA AND ")
       		.append(" ERP.CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
       		.append(" ERP.CDCLIENTE = TB.CDCLIENTE AND ")
       		.append(" PROD.CDFORNECEDOR = ").append(Sql.getValue(cliente.cdFornecedor)).append(" AND ");
       		Date date = DateUtil.getCurrentDate();
       		DateUtil.decDay(date, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor);
       		sql.append(" ERP.DTEMISSAO >= ").append(Sql.getValue(date))
       		.append(")");
       	}
    }

    public Vector findAllEmpresaCliente(String cdCliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select distinct cdEmpresa from ");
    	sql.append(tableName);
    	sql.append(" where CDCLIENTE = ");
    	sql.append(Sql.getValue(cdCliente));
    	sql.append(" and CDREPRESENTANTE = ");
    	sql.append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante));
    	String[] cdEmpresaArray = getCurrentDriver().getStrings1(sql.toString());
		return cdEmpresaArray != null ? new Vector(cdEmpresaArray) : new Vector();
    }


    public Vector findAllUfsClientes() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT DSUFPRECO FROM TBLVPCLIENTE");
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		sql.append(" WHERE CDEMPRESA=").append(Sql.getValue(SessionLavenderePda.cdEmpresa)).append(" AND CDREPRESENTANTE=").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
    	}
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listUf = new Vector(50);
        	while (rs.next()) {
    			UF unidadeFed = new UF();
    			unidadeFed.cdUf = rs.getString("DSUFPRECO");
    			unidadeFed.dsUf = rs.getString("DSUFPRECO");
				if (listUf.indexOf(unidadeFed) == -1 && !ValueUtil.isEmpty(unidadeFed.dsUf)) {
					listUf.addElement(unidadeFed);
				}
        	}
        	return listUf;
    	}
    }

	public void insertByNovoCliente(String rowKeyNovoCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select * from ");
		sql.append(NovoCliente.TABLE_NAME);
		sql.append(" where rowKey = ");
		sql.append(Sql.getValue(rowKeyNovoCliente));
		int countAux = 0;
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int sizeColumns = rsmd.getColumnCount();
				Vector nmColumnsList = new Vector(sizeColumns);
				for (int i = 1; i <= sizeColumns; i++) {
					if (!rsmd.getColumnLabel(i).equals("rowkey")) {
						nmColumnsList.addElement(rsmd.getColumnLabel(i));
					}
				}
				//--
				try (Statement st2 = getCurrentDriver().getStatement();
		    			ResultSet rs2 = st2.executeQuery(getSqlMetaData())) {
					ResultSetMetaData rsmd2 = rs2.getMetaData();
					sizeColumns = rsmd2.getColumnCount();
					sql = getSqlBuffer();
					sql.append(" insert into ");
					sql.append(tableName);
					sql.append(" ( cdCliente, flStatusCliente,");
					String nmColumnOficial;
					for (int i = 1; i <= sizeColumns; i++) {
						nmColumnOficial = rsmd2.getColumnLabel(i);
						if (nmColumnsList.indexOf(nmColumnOficial) != -1 || verificaSePossuiColunaNovoCliente(nmColumnsList, nmColumnOficial)) {
							if (!rsmd2.getColumnLabel(i).equals("rowkey")) {
								sql.append(nmColumnOficial);
								sql.append(",");
							} else {
								countAux = i;	
							}
						}
					}
					sql.append(" rowkey) values ( ");
					sql.append(Sql.getValue(rs.getString("nucnpj")));
					sql.append(", ");
					sql.append(Sql.getValue(StatusCliente.STATUSCLIENTE_CDEMAVALIACAO));
					sql.append(", ");
					String tpColumnOficial;
					for (int i = 1; i <= sizeColumns; i++) {
						if (i == countAux) {
							continue;
						}
						nmColumnOficial = rsmd2.getColumnLabel(i);
						tpColumnOficial = rsmd2.getColumnTypeName(i);
						if (nmColumnsList.indexOf(nmColumnOficial) != -1) {
							if (LavenderePdaConfig.marcaClienteOcultoReplicaoNovoCliente && "FLOCULTO".equalsIgnoreCase(nmColumnOficial)) {
								sql.append(Sql.getValue(ValueUtil.VALOR_SIM));
							} else {
								setValuesToInsertColumns(sql, rs, nmColumnOficial, tpColumnOficial);
							}
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_CDESTADOCOMERCIAL, NovoCliente.NMCOLUNA_CDUFCOMERCIAL)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_CDUFCOMERCIAL, tpColumnOficial);
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NULOGRADOUROCOMERCIAL, NovoCliente.NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL, tpColumnOficial);
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_DTCRIACAO, NovoCliente.NMCOLUNA_DTCADASTRO)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_DTCADASTRO, tpColumnOficial);
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_HRCRIACAO, NovoCliente.NMCOLUNA_HRCADASTRO)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_HRCADASTRO, tpColumnOficial);
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NUCEPCOMERCIAL, NovoCliente.NMCOLUNA_DSCEPCOMERCIAL)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_DSCEPCOMERCIAL, tpColumnOficial);
							sql.append(",");
						} else if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NUINSCRICAOESTADUAL, NovoCliente.NMCOLUNA_NUIERG)) {
							setValuesToInsertColumns(sql, rs, NovoCliente.NMCOLUNA_NUIERG, tpColumnOficial);
							sql.append(",");
						}
					}
					sql.append("'");
					sql.append(rs.getString("cdempresa"));
					sql.append(";");
					sql.append(rs.getString("cdrepresentante"));
					sql.append(";");
					sql.append(rs.getString("nucnpj"));
					sql.append(";");
					sql.append("')");
					executeUpdate(sql.toString());
				}
			}
		}
	}

	private boolean verificaSePossuiColunaNovoCliente(Vector nmColumnsList, String nmColumnOficial) {
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_CDESTADOCOMERCIAL, NovoCliente.NMCOLUNA_CDUFCOMERCIAL)) return true;
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NULOGRADOUROCOMERCIAL, NovoCliente.NMCOLUNA_DSNUMEROLOGRADOUROCOMERCIAL)) return true;
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_DTCRIACAO, NovoCliente.NMCOLUNA_DTCADASTRO)) return true;
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_HRCRIACAO, NovoCliente.NMCOLUNA_HRCADASTRO)) return true;
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NUCEPCOMERCIAL, NovoCliente.NMCOLUNA_DSCEPCOMERCIAL)) return true;
		if (verificaColunaIgualNovoCliente(nmColumnsList, nmColumnOficial, Cliente.NMCOLUNA_NUINSCRICAOESTADUAL, NovoCliente.NMCOLUNA_NUIERG)) return true;
		return false;
	}

	private boolean verificaColunaIgualNovoCliente(Vector nmColumnsList, String nmColumnOficial, String nmColunaCliente, String nmColunaNovoCliente) {
		return nmColunaCliente.equalsIgnoreCase(nmColumnOficial) && (nmColumnsList.contains(nmColunaNovoCliente) || nmColumnsList.contains(nmColunaNovoCliente.toUpperCase()));
	}

	private void setValuesToInsertColumns(StringBuffer sql, ResultSet resultSet, String nmColumnOficial, String tpColumnOficial) throws SQLException {
		switch (tpColumnOficial.toUpperCase()) {
		case "DATE":
			sql.append(Sql.getValue(DateUtil.getDateValue(DateUtil.toDate(resultSet.getString(nmColumnOficial)))));
			break;
		case "INT":
			sql.append(Sql.getValue(ValueUtil.getIntegerValue(resultSet.getString(nmColumnOficial))));
			break;
		case "DOUBLE":
			sql.append(Sql.getValue(ValueUtil.getDoubleValue(resultSet.getString(nmColumnOficial))));
			break;
		default:
			sql.append(Sql.getValue(resultSet.getString(nmColumnOficial)));
			break;
		}
	}

    public void updateByNovoCliente(String rowKeyNovoCliente, String nuCnpj, String cdRepresentante, boolean isModoProspeccao, boolean isUpdate) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select * from ");
    	sql.append(NovoCliente.TABLE_NAME);
    	sql.append(" where rowKey = ");
    	sql.append(Sql.getValue(rowKeyNovoCliente));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			ResultSetMetaData rsmd = rs.getMetaData();
    			int sizeColumns = rsmd.getColumnCount();
    			Vector nmColumnsList = new Vector(sizeColumns);
    			for (int i = 1; i < sizeColumns; i++) {
    				nmColumnsList.addElement(rsmd.getColumnLabel(i));
    			}
    			//--
    			try (Statement st2 = getCurrentDriver().getStatement();
		    			ResultSet rs2 = st2.executeQuery(getSqlMetaData())) {
					ResultSetMetaData rsmd2 = rs2.getMetaData();
					sizeColumns = rsmd2.getColumnCount();
					sql = getSqlBuffer();
			        sql.append(" update ");
			        sql.append(tableName);
			        sql.append(" set ");
			        boolean first = true;
			        String nmColumnOficial;
			        String tpColumnOficial;
					for (int i = 1; i < sizeColumns; i++) {
						nmColumnOficial = rsmd2.getColumnLabel(i);
						tpColumnOficial = rsmd2.getColumnTypeName(i);
						if (nmColumnOficial.equalsIgnoreCase(Cliente.NMCOLUNA_FLTIPOCADASTRO) && ValueUtil.isNotEmpty(rs.getString("cdClienteOriginal"))) {
							if (!first) {
								sql.append(", ");
							} 
							sql.append(nmColumnOficial);
							sql.append(" = ").append("null");
							continue;
						}
						if (nmColumnOficial.equalsIgnoreCase(Cliente.NMCOLUNA_FLSTATUSCLIENTE) && ValueUtil.isNotEmpty(rs.getString("cdClienteOriginal"))) {
							if (!first) {
								sql.append(", ");
							} 
							sql.append(nmColumnOficial);
							sql.append(" = ").append(Sql.getValue(StatusCliente.STATUSCLIENTE_CDEMAVALIACAO));
							continue;
						}
						if (nmColumnOficial.equalsIgnoreCase(Cliente.NMCOLUNA_CDCLIENTE) && ValueUtil.isNotEmpty(rs.getString("cdClienteOriginal"))) {
							if (!first) {
								sql.append(", ");
							}
							sql.append(nmColumnOficial);
							sql.append(" = ").append(Sql.getValue(rs.getString("nucnpj")));
						}
						if (nmColumnsList.indexOf(nmColumnOficial) != -1) {
							if (!first) {
								sql.append(", ");
							}
							sql.append(nmColumnOficial);
							sql.append(" = ");
							if (LavenderePdaConfig.marcaClienteOcultoReplicaoNovoCliente && "FLOCULTO".equalsIgnoreCase(nmColumnOficial)) {
								sql.append(Sql.getValue(ValueUtil.VALOR_SIM));
							} else {
								setValuesToUpdateColumns(sql, rs, nmColumnOficial, tpColumnOficial);
							}
							first = false;
						}
					}
					if (isModoProspeccao || isUpdate) {
						sql.append(", rowkey ");
						sql.append(" = ").append(Sql.getValue(rs.getString("cdempresa") + ";" + cdRepresentante + ";" + rs.getString("nucnpj") + ";"));
						sql.append(" where cdempresa = ").append(Sql.getValue(rs.getString("cdempresa")));
						sql.append(" and cdrepresentante = ").append(Sql.getValue(cdRepresentante));
						String cdNovoCliente = nuCnpj;
						String cdcliente = nuCnpj;
						if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
							cdcliente = rs.getString("CDCLIENTEORIGINAL");
						}
						sql.append(" and (cdcliente = ").append(Sql.getValue(cdNovoCliente));
						sql.append(" or cdcliente = ").append(Sql.getValue(cdcliente)).append(") ");
						sql.append(" and nucnpj = ").append(Sql.getValue(nuCnpj));
					} else {
						sql.append(" where rowkey = '");
						sql.append(rs.getString("cdempresa"));
						sql.append(";");
						sql.append(rs.getString("cdrepresentante"));
						sql.append(";");
						sql.append(rs.getString("nucnpj"));
						sql.append(";");
						sql.append("'");
					}
					//--
					executeUpdate(sql.toString());
    			}
    		}
    	}
    }

	private void setValuesToUpdateColumns(StringBuffer sql, ResultSet rs, String nmColumnOficial, String tpColumnOficial) throws SQLException {
		switch (tpColumnOficial.toUpperCase()) {
		case "DATE":
			sql.append(Sql.getValue(DateUtil.getDateValue(rs.getDate(nmColumnOficial))));
			break;
		case "INT":
			sql.append(Sql.getValue(ValueUtil.getIntegerValue(rs.getString(nmColumnOficial))));
			break;
		case "DOUBLE":
			sql.append(Sql.getValue(ValueUtil.getDoubleValue(rs.getString(nmColumnOficial))));
			break;
		default:
			sql.append(Sql.getValue(rs.getString(nmColumnOficial)));
			break;
		}
	}

    public Vector findAllClientesRede(Cliente clienteFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT * FROM ").append(tableName);
    	sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(clienteFilter.cdEmpresa)).append(" AND CDREPRESENTANTE = ").append(Sql.getValue(clienteFilter.cdRepresentante));
    	sql.append(" AND CDREDE = ").append(Sql.getValue(clienteFilter.cdRede));
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector clienteList = new Vector(50);
        	while (rs.next()) {
    			Cliente cliente = new Cliente();
    			cliente.cdCliente = rs.getString("CDCLIENTE");
    			cliente.nmRazaoSocial = rs.getString("NMRAZAOSOCIAL");
    			cliente.nmFantasia = rs.getString("NMFANTASIA");
    			cliente.cdEmpresa = rs.getString("CDEMPRESA");
    			cliente.cdRepresentante = rs.getString("CDREPRESENTANTE");
    			cliente.cdRede = rs.getString("CDREDE");
    			clienteList.addElement(cliente);
        	}
        	return clienteList;
    	}
    }

    //-----------------------------------------------------------------
    // ENTIDADES DINAMICAS - METODOS ESPECIFICOS PARA CONSULTAS QUE INCLUAM COLUNAS DINAMICAS
    //-----------------------------------------------------------------

    protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws SQLException {
    	Cliente cliente = new Cliente();
    	cliente.cdEmpresa = rs.getString("cdEmpresa");
    	cliente.cdRepresentante = rs.getString("cdRepresentante");
    	cliente.cdCliente = rs.getString("cdCliente");
    	cliente.dtVencimentoAlvara = rs.getDate("dtVencimentoAlvara");
        cliente.setFlStatusCliente(rs.getString("flStatusCliente"));
        if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
        	cliente.nuCnpj = rs.getString("nuCnpj");
        }
    	cliente.dtCadastro = rs.getDate("dtCadastro");
    	cliente.nuDiasSemPedido = rs.getInt("nuDiasSemPedido");
    	cliente.nuDiasSemPedidoInvalido = rs.getObject("nuDiasSemPedido") == null;
    	cliente.flAtendido = rs.getString("flAtendido");
        return cliente;
    }



    public void updateFlAtendido(Cliente cliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" UPDATE ");
        sql.append(tableName);
        sql.append(" SET FLATENDIDO = ").append(Sql.getValue(cliente.flAtendido));
        sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
        sql.append(" AND CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
        executeUpdate(sql.toString());
    }

	public void updateFlTipoCadastro(NovoCliente novoCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ");
		sql.append(tableName);
		sql.append(" SET FLTIPOCADASTRO = ").append(Sql.getValue(novoCliente.flTipoCadastro));
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(novoCliente.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(novoCliente.cdRepresentante));
		sql.append(" AND CDCLIENTE = ").append(Sql.getValue(novoCliente.nuCnpj));
		executeUpdate(sql.toString());
	}

	public Vector findAllCidadeByExample(Cliente cliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT DSCIDADECOMERCIAL FROM ");
		sql.append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
        if (cliente.cdEstadoComercial != null) {
        	sql.append(" AND CDESTADOCOMERCIAL = ").append(Sql.getValue(cliente.cdEstadoComercial));
        }
        sql.append(" ORDER BY DSCIDADECOMERCIAL ");
        try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector dscidadeComercialList = new Vector(50);
    		String dsCidadeComercial;
        	while (rs.next()) {
        		dsCidadeComercial = rs.getString("DSCIDADECOMERCIAL");
        		if (ValueUtil.isEmpty(dsCidadeComercial)) continue;
        		dscidadeComercialList.addElement(dsCidadeComercial);
        	}
        	return dscidadeComercialList;
    	}
	}

	public Vector findAllBairroByExample(Cliente cliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT DSBAIRROCOMERCIAL FROM ");
		sql.append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
        sql.append(" AND DSCIDADECOMERCIAL = ").append(Sql.getValue(cliente.dsCidadeComercial));
        sql.append(" AND CDESTADOCOMERCIAL = ").append(Sql.getValue(cliente.cdEstadoComercial));
        sql.append(" ORDER BY DSBAIRROCOMERCIAL ");
        try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector dsBairroComercial = new Vector(50);
        	while (rs.next()) {
    			dsBairroComercial.addElement(rs.getString("DSBAIRROCOMERCIAL"));
        	}
        	return dsBairroComercial;
    	}
	}
	
	public void updateFlConsumidorFinal(String nuCnpj) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName).append(" SET ")
		.append("FLCONSUMIDORFINAL = ").append(Sql.getValue(ValueUtil.VALOR_SIM))
		.append(" WHERE NUCNPJ = ").append(Sql.getValue(nuCnpj));
		executeUpdate(sql.toString());
	}
	
	public double sumVlVendasMensalClientesRede(String cdRede) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(VLVENDAMENSAL) VLVENDASMENSALREDE FROM ").append(tableName).append(" TB");
		sql.append(" WHERE TB.CDREDE = ").append(Sql.getValue(cdRede));
		return getDouble(sql.toString());
	}
	
	public void updateFlAtualizaCadastroWMW(Cliente cliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName).append(" SET ");
		sql.append("FLATUALIZACADASTROWMW = ").append(Sql.getValue(cliente.flAtualizaCadastroWmw));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (LavenderePdaConfig.usaCodigoClienteUnico) {
			sqlWhereClause.addAndCondition("CDCLIENTE = ", cliente.cdCliente);
		} else {
			sqlWhereClause.addAndCondition("ROWKEY = ", cliente.getRowKey());
		}
		sql.append(sqlWhereClause.getSql());
		executeUpdate(sql.toString());
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
    		boolean possuiTB = sql.indexOf(" tb ") > -1 || sql.indexOf("tb.") > -1; 
    		sql.append(" order by ");
    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
    		for (int i = 0; i < sortAtributtes.length; i++) {
    			boolean cdClienteAsNumber = LavenderePdaConfig.usaOrdemNumericaColunaCodigoCliente && "CDCLIENTE".equals(sortAtributtes[i]);
    			if (cdClienteAsNumber) sql.append("CAST(");
    			sql.append(possuiTB ? "tb." + sortAtributtes[i] : sortAtributtes[i]);
    			if (cdClienteAsNumber) sql.append(" AS DECIMAL)");
    			sql.append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
    			if (!(i == (sortAtributtes.length - 1))) {
    				sql.append(" , ");
    			}
			}
    	}
	}

	private String getSqlPrecosClienteRede(ItemPedido itemPedido, String cdRede, boolean count) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ").append(count ? "COUNT(1)" : "CLI.CDEMPRESA, CLI.CDREPRESENTANTE, CLI.CDCLIENTE, CLI.NMFANTASIA, PED.DTEMISSAO, ITEM.VLITEMPEDIDO ")
		.append("FROM TBLVPCLIENTE CLI JOIN TBLVPPEDIDOERP PED ON ")
		.append("CLI.CDEMPRESA = PED.CDEMPRESA AND ")
		.append("CLI.CDREPRESENTANTE = PED.CDREPRESENTANTE AND ")
		.append("CLI.CDCLIENTE = PED.CDCLIENTE ")
		.append("JOIN TBLVPITEMPEDIDOERP ITEM ON ")
		.append("PED.CDEMPRESA = ITEM.CDEMPRESA AND ")
		.append("PED.CDREPRESENTANTE = ITEM.CDREPRESENTANTE AND ")
		.append("PED.NUPEDIDO = ITEM.NUPEDIDO AND ")
		.append("PED.FLORIGEMPEDIDO = ITEM.FLORIGEMPEDIDO ")
		.append("WHERE CLI.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ")
		.append("CLI.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ")
		.append("CLI.CDREDE = ").append(Sql.getValue(cdRede)).append(" AND ")
		.append("ITEM.CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto));
		if (LavenderePdaConfig.nuDiasConsideraPedido > 0) {
			Date date = DateUtil.getCurrentDate();
			DateUtil.decDay(date, LavenderePdaConfig.nuDiasConsideraPedido);
			sql.append(" AND PED.DTEMISSAO >= ").append(Sql.getValue(date));
		}
		addOrderBy(sql, itemPedido);
		return sql.toString();
	}
	
	
	public Vector findUltimosPrecosClienteRede(ItemPedido itemPedido, String cdRede) throws SQLException {
		Vector list = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(getSqlPrecosClienteRede(itemPedido, cdRede, false))) {
			while(rs.next()) {
				list.addElement(populateUltimosPrecosRedeCliente(rs));
			}
			return list;
		}
	}
	
	public int countUltimosPrecosClienteRede(ItemPedido itemPedido, String cdRede) throws SQLException {
		return getInt(getSqlPrecosClienteRede(itemPedido, cdRede, true));
	}
	
	private Cliente populateUltimosPrecosRedeCliente(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = rs.getString("cdEmpresa");
		cliente.cdRepresentante = rs.getString("cdRepresentante");
		cliente.cdCliente = rs.getString("cdCliente");
		cliente.nmFantasia = rs.getString("nmFantasia");
		cliente.dtEmissao = rs.getDate("dtEmissao");
		cliente.vlUltimoPreco = rs.getDouble("vlItemPedido");
		return cliente;
	}
    
    @Override
    protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoinSummary(domainFilter, sql);
    	if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
    		sql.append(" LEFT OUTER JOIN TBLVPFICHAFINANCEIRA FF ON ");
    		sql.append(" TB.CDEMPRESA = FF.CDEMPRESA");
    		sql.append(" AND TB.CDREPRESENTANTE = FF.CDREPRESENTANTE");
    		sql.append(" AND TB.CDCLIENTE = FF.CDCLIENTE");
    	}
    	if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {    	
    		sql.append(" LEFT OUTER JOIN ( ");
    		sql.append(" SELECT CDEMPRESA ,CDREPRESENTANTE ,CDCLIENTE, CDCONTATO ");
    		sql.append(" ,GROUP_CONCAT(NMCONTATO) NMCONTATO ");
    		sql.append(" FROM TBLVPCONTATO ");
    		sql.append(" GROUP BY CDEMPRESA ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ) TC ON TB.CDEMPRESA = TC.CDEMPRESA ");
    		sql.append(" AND TB.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
    		sql.append(" AND TB.CDCLIENTE = TC.CDCLIENTE ");
    		sql.append(" LEFT OUTER JOIN ( ");
    		sql.append(" SELECT CDEMPRESA ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ,GROUP_CONCAT(NMCONTATO) NMCONTATO ");
    		sql.append(" FROM TBLVPCONTATOERP ");
    		sql.append(" GROUP BY CDEMPRESA ");
    		sql.append(" ,CDREPRESENTANTE ,CDCLIENTE ");
    		sql.append(" ) TCERP ON TB.CDEMPRESA = TCERP.CDEMPRESA ");
    		sql.append(" AND TB.CDREPRESENTANTE = TCERP.CDREPRESENTANTE ");
    		sql.append(" AND TB.CDCLIENTE = TCERP.CDCLIENTE ");
    	}
    	if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
    		DaoUtil.addJoinRede(sql);
    }
    	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
    		DaoUtil.addJoinCategoria(sql);
    	}
    	if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
    		DaoUtil.addJoinProdutoRetirada(sql);
    	}
    	
    	DescProgressivoConfig descProgressivoConfigFilter;
    	if (LavenderePdaConfig.usaDescProgressivoPersonalizado && (descProgressivoConfigFilter = ((Cliente)domainFilter).descProgressivoConfigFilter) != null) {
    		sql.append(" JOIN TBLVPDESCPROGCONFIGFACLI DES ON ")
    		.append(" DES.CDDESCPROGRESSIVO = ").append(Sql.getValue(descProgressivoConfigFilter.cdDescProgressivo))
    		.append(" AND DES.CDEMPRESA =  ").append(Sql.getValue(descProgressivoConfigFilter.cdEmpresa))
    		.append(" AND DES.CDREPRESENTANTE =  ").append(Sql.getValue(descProgressivoConfigFilter.cdRepresentante));
    		
    		if (descProgressivoConfigFilter.nuNivelCliente == 1) sql.append(" AND DES.CDCLIENTE = TB.CDCLIENTE ");
    		else if (descProgressivoConfigFilter.nuNivelCliente == 2) {
    			sql.append(" AND (DES.CDFAIXACLI = TB.CDFAIXACLI OR (TB.CDFAIXACLI IS NULL OR TB.CDFAIXACLI = '') AND DES.CDFAIXACLI IN (SELECT CDFAIXACLI FROM TBLVPFAIXACLIENTE faixa WHERE faixa.CDCLIENTE = TB.CDCLIENTE AND faixa.CDEMPRESA = TB.CDEMPRESA AND faixa.CDREPRESENTANTE = TB.CDREPRESENTANTE))");
    		}
    	}
    	
    }
    
    @Override
    protected void addGroupBySummary(BaseDomain domain, StringBuffer sql) {
    	super.addGroupBySummary(domain, sql);
    	DescProgressivoConfig descProgressivoConfigFilter;
    	if (LavenderePdaConfig.usaDescProgressivoPersonalizado && (descProgressivoConfigFilter = ((Cliente)domain).descProgressivoConfigFilter) != null && descProgressivoConfigFilter.nuNivelCliente == 2) {
    		sql.append(" GROUP BY TB.CDCLIENTE ");
    	}
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
    		DaoUtil.addJoinProdutoRetirada(sql);
    	}
    }

	public int resetaNuDivulgaInfo() {
		try {
			StringBuffer sql = getSqlBuffer();
			sql.append("UPDATE ")
					.append(tableName)
					.append(" SET ")
					.append(Cliente.NUDIVULGAINFO).append(" = ").append(Sql.getValue(0));
			return executeUpdate(sql.toString());
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return 0;
		}
	}

	public int updateVisualizacaoDivulgaInfo(String rowKey) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ")
				.append(tableName)
				.append(" SET ")
				.append(Cliente.NUDIVULGAINFO).append(" = COALESCE(").append(Cliente.NUDIVULGAINFO).append(", 0) + 1")
				.append(" WHERE rowKey = ").append(Sql.getValue(rowKey));
		return executeUpdate(sql.toString());
	}
	
	public Vector fildAllDistinctRedeCliente() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Vector list = new Vector();
		sql.append("SELECT DISTINCT(tb.CDREDE), DSREDE FROM ").append(tableName).append(" tb");
		sql.append(" INNER JOIN TBLVPREDE tr ON tb.CDREDE = tr.CDREDE");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while(rs.next()) {
				Vector cdRedeDsRede = new Vector();
				String cdRede = rs.getString("CDREDE");
				cdRedeDsRede.addElement(cdRede);
				String dsRede = rs.getString("DSREDE");
				cdRedeDsRede.addElement(dsRede);
				list.addElement(cdRedeDsRede);
			}
			return list;
		}
	}
	
	public Vector findAllByExampleForCombo(BaseDomain clienteFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" SELECT ");
        addSelectColumns(clienteFilter, sql);
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" TB ");
        addJoin(clienteFilter, sql);
        addWhereByExample(clienteFilter, sql);
        sql.append(" ORDER BY TB.NMRAZAOSOCIAL ASC ");
        addLimit(sql, clienteFilter);
        return findAll(clienteFilter, sql.toString());
	}

	public Vector findDistinctStatusCliente(Cliente cliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select distinct FLSTATUSCLIENTE from ");
		sql.append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", cliente.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", cliente.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector flStatusClientes = new Vector();
			while (rs.next()) {
				flStatusClientes.addElement(rs.getString("FLSTATUSCLIENTE"));
			}
			return flStatusClientes;
		}
	}
	
	
	protected void addSelectDistinctColumnsWithoutRepresentante(StringBuffer sql) {
		sql.append("SELECT CDEMPRESA,");
        sql.append(" CDCLIENTE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" NUCNPJ,");
        sql.append(" CDUSUARIO ");
	}

	public void updateDtFimPesquisa(PesquisaApp pesquisaApp) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" UPDATE ");
        sql.append(tableName);
        sql.append(" SET DTFIMPESQUISA = ").append(Sql.getValue(pesquisaApp.dtFimPesquisa));
        sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pesquisaApp.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pesquisaApp.cdRepresentante));
        sql.append(" AND CDCLIENTE = ").append(Sql.getValue(pesquisaApp.cdCliente));
        executeUpdate(sql.toString());
	}
	
    public void updateDsEstadoComercial(Cliente cliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" UPDATE ");
        sql.append(tableName);
        sql.append(" SET DSESTADOCOMERCIAL = ").append(Sql.getValue(cliente.dsEstadoComercial));
        sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
        sql.append(" AND CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
        executeUpdate(sql.toString());
    }
	
}
