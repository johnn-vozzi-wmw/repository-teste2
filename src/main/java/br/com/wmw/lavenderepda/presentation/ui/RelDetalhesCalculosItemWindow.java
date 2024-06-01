package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoCliente;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;
import br.com.wmw.lavenderepda.business.service.FecopService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteService;
import br.com.wmw.lavenderepda.business.service.STService;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.util.Vector;

public class RelDetalhesCalculosItemWindow extends WmwWindow {
	
	private Pedido pedido;
	private ItemPedido itemPedido;
	private Tributacao tributacao;
	private TributacaoVlBase tributacaoVlBase;
	private TributacaoConfig tributacaoConfig;
	private ScrollTabbedContainer scrollTabbedContainer;
	private SessionContainer formulaRentabilidadeContainer;
	private SessionContainer formulaGastoTotalVariavelContainer;
	private SessionContainer formulaIcmsContainer;
	private SessionContainer formulaStContainer;
	private SessionContainer formulaIndiceRentabilidadeItemContainer;
	private SessionContainer formulaMargemPercentualItemContainer;
	private SessionContainer formulaMargemValorItemContainer;
	private SessionContainer formulaReceitaVirtualItemContainer;
	private SessionContainer formulaVerbaGeradaContainer;
	private SessionContainer formulaVerbaNecessariaContainer;
	private SessionContainer formulaValorNeutroContainer;
	private SessionContainer formulaPisContainer;
	private SessionContainer formulaCofinsContainer;
	private SessionContainer formulaIpiContainer;
	private SessionContainer formulaVerbaEmpresaContainer;
	private LabelValue lbFormulaRentabilidade;
	private LabelValue lbFormulaGastoTotalVariavel;
	private LabelValue lbFormulaIcms;
	private LabelValue lbFormulaSt;
	private LabelValue lbFormulaIndiceRentabilidadeItem;
	private LabelValue lbFormulaMargemPercentualItem;
	private LabelValue lbFormulaMargemValorItem;
	private LabelValue lbFormulaReceitaVirtualItem;
	private LabelValue lbFormulaVerbaGerada;
	private LabelValue lbFormulaVerbaNecessaria;
	private LabelValue lbFormulaValorNeutro;
	private LabelValue lbFormulaPis;
	private LabelValue lbFormulaCofins;
	private LabelValue lbFormulaIpi;
	private LabelValue lbFormulaVerbaEmpresa;
	private LabelName lbPctRentabilidade;
	private LabelValue lvPctRentabilidade;
	private LabelName lbVlRentabilidade;
	private LabelValue lvVlRentabilidade;
	private LabelName lbPrecoTotalComImpostos;
	private LabelValue lvPrecoTotalComImpostos;
	private LabelName lbPrecoTotalCusto;
	private LabelValue lvPrecoTotalCusto;
	private LabelName lbGastoTotalVariavel;
	private LabelValue lvGastoTotalVariavel;
	private LabelName lbCustoTotalFixo;
	private LabelValue lvCustoTotalFixo;
	private LabelName lbVlTotalPis;
	private LabelValue lvVlTotalPis;
	private LabelName lbVlTotalCofins;
	private LabelValue lvVlTotalCofins;
	private LabelName lbVlTotalIcms;
	private LabelValue lvVlTotalIcms;
	private LabelName lbVlTotalSt;
	private LabelValue lvVlTotalSt;
	private LabelName lbVlTotalIpi;
	private LabelValue lvVlTotalIpi;
	private LabelName lbVlTotalIrpj;
	private LabelValue lvVlTotalIrpj;
	private LabelName lbVlTotalCsll;
	private LabelValue lvVlTotalCsll;
	private LabelName lbVlTotalCpp;
	private LabelValue lvVlTotalCpp;
	private LabelName lbVlTotalCustoVariavelProduto;
	private LabelValue lvVlTotalCustoVariavelProduto;
	private LabelName lbVlTotalCustoVariavel;
	private LabelValue lvVlTotalCustoVariavel;
	private LabelName lbVlGastoTotalVariavel;
	private LabelValue lvVlGastoTotalVariavel;
	private LabelName lbPrecoTotalVenda;
	private LabelValue lvPrecoTotalVenda;
	private LabelName lbBaseCalculoIcms;
	private LabelName lbValorIcms;
	private LabelValue lvBaseCalculoIcms;
	private LabelValue lvValorIcms;
	private LabelName lbVlFreteCalcIcms;
	private LabelValue lvVlFreteCalcIcms;
	private LabelName lbVlDespesasAcessorias;
	private LabelValue lvVlDespesasAcessorias;
	private LabelName lbPctIpiCalcIcms;
	private LabelValue lvPctIpiCalcIcms;
	private LabelName lbPctRepasseCalcIcms;
	private LabelValue lvPctRepasseCalcIcms;
	private LabelName lbPctReducaoCalcIcms;
	private LabelValue lvPctReducaoCalcIcms;
	private LabelName lbAliquotaIcms;
	private LabelValue lvAliquotaIcms;
	private LabelName lbPctReducaoIcms;
	private LabelValue lvPctReducaoIcms;
	private LabelName lbVlIcmsCalcIcms;
	private LabelValue lvVlIcmsCalcIcms;
	private LabelName lbBaseCalculoRetido;
	private LabelValue lvBaseCalculoRetido;
	private LabelName lbSTRetido;
	private LabelValue lvSTRetido;
	private LabelName lbVlFreteCalcSt;
	private LabelValue lvVlFreteCalcSt;
	private LabelName lbPctIpiCalcSt;
	private LabelValue lvPctIpiCalcSt;
	private LabelName lbPctRepasseCalcSt;
	private LabelValue lvPctRepasseCalcSt;
	private LabelName lbMargemAgregada;
	private LabelValue lvMargemAgregada;
	private LabelName lbPctReducaoCalcSt;
	private LabelValue lvPctReducaoCalcSt;
	private LabelName lbAliquotaRetido;
	private LabelValue lvAliquotaRetido;
	private LabelName lbVlIcmsCalcSt;
	private LabelValue lvVlIcmsCalcSt;
	private LabelName lbAliquotaFecop;
	private LabelValue lvAliquotaFecop;
	private LabelName lbVlFecopRecolher;
	private LabelValue lvVlFecopRecolher;
	private LabelName lbVlSt;
	private LabelValue lvVlSt;
	private LabelName lbVlStRentabilidade;
	private LabelValue lvVlStRentabilidade;
	private LabelName lbMargemPercentual;
	private LabelValue lvMargemPercentual;
	private LabelName lbMargemMinima;
	private LabelValue lvMargemMinima;
	private LabelName lbMargemEspecifica;
	private LabelValue lvMargemEspecifica;
	private LabelName lbVlIndiceRentabilidadeItem;
	private LabelValue lvVlIndiceRentabilidadeItem;
	private LabelName lbMargemValor;
	private LabelValue lvMargemValor;
	private LabelName lbReceitaVirtual;
	private LabelValue lvReceitaVirtual;
	private LabelName lbVlMargemPercentual;
	private LabelValue lvVlMargemPercentual;
	private LabelName lbReceitaLiquida;
	private LabelValue lvReceitaLiquida;
	private LabelName lbCusto;
	private LabelValue lvCusto;
	private LabelName lbVerbaEmpresa;
	private LabelValue lvVerbaEmpresa;
	private LabelName lbVerbaGerada;
	private LabelValue lvVerbaGerada;
	private LabelName lbVerbaUsada;
	private LabelValue lvVerbaUsada;
	private LabelName lbVlMargemValor;
	private LabelValue lvVlMargemValor;
	private LabelName lbValorVenda;
	private LabelValue lvValorVenda;
	private LabelName lbAliquotaPis;
	private LabelValue lvAliquotaPis;
	private LabelName lbVlIcmsCalcRecVirtual;
	private LabelValue lvVlIcmsCalcRevVirtual;
	private LabelName lbVlStCalcRecVirtual;
	private LabelValue lvVlStCalcRecVirtual;
	private LabelName lbVerbaEmpresaCalcRecVirtual;
	private LabelValue lvVerbaEmpresaCalcRecVirtual;
	private LabelName lbVerbaGeradaCalcRecVirtual; 
	private LabelValue lvVerbaGeradaCalcRecVirtual; 
	private LabelName lbVerbaUsadaCalcRecVirtual; 
	private LabelValue lvVerbaUsadaCalcRecVirtual;
	private LabelName lbAliquotaIcmsCalcRecVirtual;
	private LabelValue lvAliquotaIcmsCalcRecVirtual;
	private LabelName lbVlReceitaVirtual;
	private LabelValue lvVlReceitaVirtual;
	private LabelName lbAliquotaIcmsCalcVerbaGerada;
	private LabelValue lvAliquotaIcmsCalcVerbaGerada;
	private LabelName lbAliquotaPisCalcVerbaGerada;
	private LabelValue lvAliquotaPisCalcVerbaGerada;
	private LabelName lbVlVerbaGerada;
	private LabelValue lvVlVerbaGerada;
	private LabelName lbVlTabelaVerbaEmpresa;
	private LabelValue lvVlTabelaVerbaEmpresa;
	private LabelName lbAliquotaIcmsCalcVerbaEmpresa;
	private LabelValue lvAliquotaIcmsCalcVerbaEmpresa;
	private LabelName lbAliquotaPisCalcVerbaEmpresa;
	private LabelValue lvAliquotaPisCalcVerbaEmpresa;
	private LabelName lbCalcVerbaEmpresa;
	private LabelValue lvCalcVerbaEmpresa;
	private LabelName lbValorNeutroCalcVerbaEmpresa;
	private LabelValue lvValorNeutroCalcVerbaEmpresa;
	private LabelName lbValorVendaCalcVerbaNecessaria;
	private LabelValue lvValorVendaCalcVerbaNecessaria;
	private LabelName lbValorNeutroCalcVerbaNecessaria;
	private LabelValue lvValorNeutroCalcVerbaNecessaria;
	private LabelName lbAliquotaIcmsCalcVerbaNecessaria;
	private LabelValue lvAliquotaIcmsCalcVerbaNecessaria;
	private LabelName lbAliquotaPisCalcVerbaNecessaria;
	private LabelValue lvAliquotaPisCalcVerbaNecessaria;
	private LabelName lbVerbaEmpresaCalcVerbaNecessaria;
	private LabelValue lvVerbaEmpresaCalcVerbaNecessaria;
	private LabelName lbVlVerbaNecessaria;
	private LabelValue lvVlVerbaNecessaria;
	private LabelName lbCustoCalcValorNeutro;
	private LabelValue lvCustoCalcValorNeutro;
	private LabelName lbAliquotaIcmsCalcValorNeutro;
	private LabelValue lvAliquotaIcmsCalcValorNeutro;
	private LabelName lbAliquotaPisCalcValorNeutro;
	private LabelValue lvAliquotaPisCalcValorNeutro;
	private LabelName lbMargemEspecificaCalcValorNeutro;
	private LabelValue lvMargemEspecificaCalcValorNeutro;
	private LabelName lbValorIcmsCalcValorNeutro;
	private LabelValue lvValorIcmsCalcValorNeutro;
	private LabelName lbValorStCalcValorNeutro;
	private LabelValue lvValorStCalcValorNeutro;
	private LabelName lbVlValorNeutro;
	private LabelValue lvVlValorNeutro;
	private LabelName lbTotalItemPedidoPis;
	private LabelValue lvTotalItemPedidoPis;
	private LabelName lbTotalItemPedidoFretePis;
	private LabelValue lvTotalItemPedidoFretePis;
	private LabelName lbSeguroItemPedidoPis;
	private LabelValue lvSeguroItemPedidoPis;
	private LabelName lbPctPis;
	private LabelValue lvPctPis;
	private LabelName lbTotalItemPedidoCofins;
	private LabelValue lvTotalItemPedidoCofins;
	private LabelName lbTotalItemPedidoFreteCofins;
	private LabelValue lvTotalItemPedidoFreteCofins;
	private LabelName lbSeguroItemPedidoCofins;
	private LabelValue lvSeguroItemPedidoCofins;
	private LabelName lbPctCofins;
	private LabelValue lvPctCofins;
	private LabelName lbTotalItemPedidoIpi;
	private LabelValue lvTotalItemPedidoIpi;
	private LabelName lbTotalItemPedidoFreteIpi;
	private LabelValue lvTotalItemPedidoFreteIpi;
	private LabelName lbSeguroItemPedidoIpi;
	private LabelValue lvSeguroItemPedidoIpi;
	private LabelName lbPisNoIpi;
	private LabelValue lvPisNoIpi;
	private LabelName lbCofinsNoIpi;
	private LabelValue lvCofinsNoIpi;
	private LabelName lbPctIpi;
	private LabelValue lvPctIpi;
	private LabelName lbBaseIcms;
	private LabelValue lvBaseIcms;
	private LabelName lbQtdItemPedidoIcms;
	private LabelValue lvQtdItemPedidoIcms;
	private LabelName lbTotalItemPedidoIcms;
	private LabelValue lvTotalItemPedidoIcms;
	private LabelName lbTotalItemPedidoFreteIcms;
	private LabelValue lvTotalItemPedidoFreteIcms;
	private LabelName lbSeguroItemPedidoIcms;
	private LabelValue lvSeguroItemPedidoIcms;
	private LabelName lbPisNoIcms;
	private LabelValue lvPisNoIcms;
	private LabelName lbIpiNoIcms;
	private LabelValue lvIpiNoIcms;
	private LabelName lbCofinsNoIcms;
	private LabelValue lvCofinsNoIcms;
	private LabelName lbPctIcms;
	private LabelValue lvPctIcms;
	private LabelName lbBaseSt;
	private LabelValue lvBaseSt;
	private LabelName lbPercentualReducaoSt;
	private LabelValue lvPercentualReducaoSt;
	private LabelName lbQtdItemPedidoSt;
	private LabelValue lvQtdItemPedidoSt;
	private LabelName lbTotalItemPedidoSt;
	private LabelValue lvTotalItemPedidoSt;
	private LabelName lbTotalItemPedidoFreteSt;
	private LabelValue lvTotalItemPedidoFreteSt;
	private LabelName lbSeguroItemPedidoSt;
	private LabelValue lvSeguroItemPedidoSt;
	private LabelName lbIpiNaSt;
	private LabelValue lvIpiNaSt;
	private LabelName lbPctSt;
	private LabelValue lvPctSt;
	private LabelName lbIcmsNaSt;
	private LabelValue lvIcmsNaSt;
	private LabelName lbVlVenda;
	private LabelValue lvVlVenda;
	private LabelName lbVlTabela;
	private LabelValue lvVlTabela;
	
	private LabelName lbVlImposto;
	private LabelValue lvVlImposto;
	
	private LabelName lbVlRetorno;
	private LabelValue lvVlRetorno;
	
	private LabelName lbVlCustoFinanceiro;
	private LabelValue lvVlCustoFinanceiro;
	
	private LabelName lbVlCustosVariaveis;
	private LabelValue lvVlCustosVariaveis;

	private LabelName lbIndiceFinanceiroCondPagtoPis;
	private LabelValue lvIndiceFinanceiroCondPagtoPis;
	private LabelName lbIndiceFinanceiroCondPagtoCofins;
	private LabelValue lvIndiceFinanceiroCondPagtoCofins;
	private LabelName lbIndiceFinanceiroCondPagtoIpi;
	private LabelValue lvIndiceFinanceiroCondPagtoIpi;
	private LabelName lbIndiceFinanceiroCondPagtoIcms;
	private LabelValue lvIndiceFinanceiroCondPagtoIcms;
	private LabelName lbIndiceFinanceiroCondPagtoSt;
	private LabelValue lvIndiceFinanceiroCondPagtoSt;
	
	private int indexTab;
	
	public RelDetalhesCalculosItemWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		super(Messages.MENU_OPCAO_DETALHES_CALCULOS);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		inicializaInfosTributacao(pedido, itemPedido);
		Vector tabsCaptions = configuraTabsCaptions();
		scrollTabbedContainer = new ScrollTabbedContainer((String[])tabsCaptions.toObjectArray());
		formulaRentabilidadeContainer = new SessionContainer();
		formulaGastoTotalVariavelContainer = new SessionContainer();
		formulaIcmsContainer = new SessionContainer();
		formulaStContainer = new SessionContainer();
		formulaIndiceRentabilidadeItemContainer = new SessionContainer();
		formulaMargemPercentualItemContainer = new SessionContainer();
		formulaMargemValorItemContainer = new SessionContainer();
		formulaReceitaVirtualItemContainer = new SessionContainer();
		formulaVerbaGeradaContainer = new SessionContainer();
		formulaVerbaNecessariaContainer = new SessionContainer();
		formulaValorNeutroContainer = new SessionContainer();
		formulaPisContainer = new SessionContainer();
		formulaCofinsContainer = new SessionContainer();
		formulaIpiContainer = new SessionContainer();
		formulaVerbaEmpresaContainer = new SessionContainer();
		lbFormulaRentabilidade = new LabelValue();
		lbFormulaRentabilidade.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaGastoTotalVariavel = new LabelValue();
		lbFormulaGastoTotalVariavel.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaIcms = new LabelValue();
		lbFormulaIcms.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaSt = new LabelValue();
		lbFormulaSt.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaIndiceRentabilidadeItem = new LabelValue();
		lbFormulaIndiceRentabilidadeItem.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaMargemPercentualItem = new LabelValue();
		lbFormulaMargemPercentualItem.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaMargemValorItem = new LabelValue();
		lbFormulaMargemValorItem.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaReceitaVirtualItem = new LabelValue();
		lbFormulaReceitaVirtualItem.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaVerbaGerada = new LabelValue();
		lbFormulaVerbaGerada.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaVerbaNecessaria = new LabelValue();
		lbFormulaVerbaNecessaria.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaValorNeutro = new LabelValue();
		lbFormulaValorNeutro.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaPis = new LabelValue();
		lbFormulaPis.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaCofins = new LabelValue();
		lbFormulaCofins.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaIpi = new LabelValue();
		lbFormulaIpi.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaVerbaEmpresa = new LabelValue();
		lbFormulaVerbaEmpresa.setForeColor(ColorUtil.sessionContainerForeColor);
		lbPctRentabilidade = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTRENTABILIDADE);
		lvPctRentabilidade = new LabelValue();
		lbVlRentabilidade = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLRENTABILIDADE);
		lvVlRentabilidade = new LabelValue();
		lbPrecoTotalComImpostos = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALCOMIMPOSTOS);
		lvPrecoTotalComImpostos = new LabelValue();
		lbPrecoTotalCusto = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALCUSTO);
		lvPrecoTotalCusto = new LabelValue();
		lbGastoTotalVariavel = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_GASTOTOTALVARIAVEL);
		lvGastoTotalVariavel = new LabelValue();
		lbCustoTotalFixo = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_CUSTOTOTALFIXO);
		lvCustoTotalFixo = new LabelValue();
		lbVlTotalPis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALPIS);
		lvVlTotalPis = new LabelValue();
		lbVlTotalCofins = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCOFINS);
		lvVlTotalCofins = new LabelValue();
		lbVlTotalIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALICMS);
		lvVlTotalIcms = new LabelValue();
		lbVlTotalSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALST);
		lvVlTotalSt = new LabelValue();
		lbVlTotalIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI);
		lvVlTotalIpi = new LabelValue();
		lbVlTotalIrpj = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIRPJ);
		lvVlTotalIrpj = new LabelValue();
		lbVlTotalCsll = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCSLL);
		lvVlTotalCsll = new LabelValue();
		lbVlTotalCpp = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCPP);
		lvVlTotalCpp = new LabelValue();
		lbVlTotalCustoVariavelProduto = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCUSTOVARIAVELPRODUTO);
		lvVlTotalCustoVariavelProduto = new LabelValue();
		lbVlTotalCustoVariavel = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCUSTOVARIAVEL);
		lvVlTotalCustoVariavel = new LabelValue();
		lbVlGastoTotalVariavel = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_GASTOTOTALVARIAVEL);
		lvVlGastoTotalVariavel = new LabelValue();
		lbPrecoTotalVenda = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALVENDA);
		lvPrecoTotalVenda = new LabelValue();
		lbBaseCalculoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULOICMS);
		lvBaseCalculoIcms = new LabelValue();
		lbValorIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS);
		lvValorIcms = new LabelValue();
		lbVlFreteCalcIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLFRETE);
		lvVlFreteCalcIcms = new LabelValue();
		lbVlDespesasAcessorias = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLDESPESASACESSORIAS);
		lvVlDespesasAcessorias = new LabelValue();
		lbPctIpiCalcIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI);
		lvPctIpiCalcIcms = new LabelValue();
		lbPctRepasseCalcIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE);
		lvPctRepasseCalcIcms = new LabelValue();
		lbPctReducaoCalcIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO);
		lvPctReducaoCalcIcms = new LabelValue();
		lbAliquotaIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvAliquotaIcms = new LabelValue();
		lbPctReducaoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAOICMS);
		lvPctReducaoIcms = new LabelValue();
		lbVlIcmsCalcIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS);
		lvVlIcmsCalcIcms = new LabelValue();
		lbBaseCalculoRetido = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULORETIDO);
		lvBaseCalculoRetido = new LabelValue();
		lbSTRetido = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLST);
		lvSTRetido = new LabelValue();
		lbVlFreteCalcSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLFRETE);
		lvVlFreteCalcSt = new LabelValue();
		lbPctIpiCalcSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI);
		lvPctIpiCalcSt = new LabelValue();
		lbPctRepasseCalcSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE);
		lvPctRepasseCalcSt = new LabelValue();
		lbMargemAgregada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MERGEMAGREGADA);
		lvMargemAgregada = new LabelValue();
		lbPctReducaoCalcSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO);
		lvPctReducaoCalcSt = new LabelValue();
		lbAliquotaRetido = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTARETIDO);
		lvAliquotaRetido = new LabelValue();
		lbVlIcmsCalcSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS);
		lvVlIcmsCalcSt = new LabelValue();
		lbAliquotaFecop = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAFECOP);
		lvAliquotaFecop = new LabelValue();
		lbVlFecopRecolher = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLFECOPRECOLHER);
		lvVlFecopRecolher = new LabelValue();
		lbVlSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLST);
		lvVlSt = new LabelValue();
		lbVlStRentabilidade = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLST);
		lvVlStRentabilidade = new LabelValue();		
		lbMargemPercentual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_PERCENTUAL);
		lvMargemPercentual = new LabelValue();
		lbMargemMinima = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_MINIMA);
		lvMargemMinima = new LabelValue();
		lbMargemEspecifica = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA);
		lvMargemEspecifica = new LabelValue();
		lbVlIndiceRentabilidadeItem = new LabelName(Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_ITEM);
		lvVlIndiceRentabilidadeItem = new LabelValue();
		lbMargemValor = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_VALOR);
		lvMargemValor = new LabelValue();
		lbReceitaVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_VIRTUAL);
		lvReceitaVirtual = new LabelValue();
		lbVlMargemPercentual = new LabelName(Messages.REL_DETALHES_CALCULOS_MARGEM_PERCENTUAL_ITEM);
		lvVlMargemPercentual = new LabelValue();
		lbReceitaLiquida = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_LIQUIDA);
		lvReceitaLiquida = new LabelValue();
		lbCusto = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO);
		lvCusto = new LabelValue();
		lbVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);
		lvVerbaEmpresa = new LabelValue();
		lbVerbaGerada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA);
		lvVerbaGerada = new LabelValue();
		lbVerbaUsada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_USADA);
		lvVerbaUsada = new LabelValue();
		lbVlMargemValor = new LabelName(Messages.REL_DETALHES_CALCULOS_MARGEM_VALOR_ITEM);
		lvVlMargemValor = new LabelValue();
		lbValorVenda = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA);
		lvValorVenda = new LabelValue();
		lbAliquotaPis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS);
		lvAliquotaPis = new LabelValue();
		lbVlIcmsCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS);
		lvVlIcmsCalcRevVirtual = new LabelValue();
		lbVlStCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLST);
		lvVlStCalcRecVirtual = new LabelValue();
		lbAliquotaIcmsCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvAliquotaIcmsCalcRecVirtual = new LabelValue();
		lbVlReceitaVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_RECEITA_VIRTUAL_ITEM);
		lvVlReceitaVirtual = new LabelValue();
		lbAliquotaIcmsCalcVerbaGerada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvAliquotaIcmsCalcVerbaGerada = new LabelValue();
		lbAliquotaPisCalcVerbaGerada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS);
		lvAliquotaPisCalcVerbaGerada = new LabelValue();
		lbVlVerbaGerada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA);
		lvVlVerbaGerada = new LabelValue();
		lbValorVendaCalcVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA);
		lvValorVendaCalcVerbaNecessaria = new LabelValue();
		lbValorNeutroCalcVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO);
		lvValorNeutroCalcVerbaNecessaria = new LabelValue();
		lbAliquotaIcmsCalcVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvAliquotaIcmsCalcVerbaNecessaria = new LabelValue();
		lbAliquotaPisCalcVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS);
		lvAliquotaPisCalcVerbaNecessaria = new LabelValue();
		lbVerbaEmpresaCalcVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);
		lvVerbaEmpresaCalcVerbaNecessaria = new LabelValue();
		lbVlVerbaNecessaria = new LabelName(Messages.REL_DETALHES_CALCULOS_VERBA_NECESSARIA);
		lvVlVerbaNecessaria = new LabelValue();
		lbCustoCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO);
		lvCustoCalcValorNeutro = new LabelValue();
		lbAliquotaIcmsCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvAliquotaIcmsCalcValorNeutro = new LabelValue();
		lbAliquotaPisCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS);
		lvAliquotaPisCalcValorNeutro = new LabelValue();
		lbMargemEspecificaCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA);
		lvMargemEspecificaCalcValorNeutro = new LabelValue();
		lbValorIcmsCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS);
		lvValorIcmsCalcValorNeutro = new LabelValue();
		lbValorStCalcValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLST);
		lvValorStCalcValorNeutro = new LabelValue();
		lbVlValorNeutro = new LabelName(Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO);
		lvVlValorNeutro = new LabelValue();
		lbTotalItemPedidoPis = new LabelName(Messages.TOOLTIP_LABEL_VLTOTALITEM);
		lvTotalItemPedidoPis = new LabelValue();
		lbTotalItemPedidoFretePis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE);
		lvTotalItemPedidoFretePis = new LabelValue();
		lbSeguroItemPedidoPis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO);
		lvSeguroItemPedidoPis = new LabelValue();
		lbPctPis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTPIS);
		lvPctPis = new LabelValue();
		lbTotalItemPedidoCofins = new LabelName(Messages.TOOLTIP_LABEL_VLTOTALITEM);
		lvTotalItemPedidoCofins = new LabelValue();
		lbTotalItemPedidoFreteCofins = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE);
		lvTotalItemPedidoFreteCofins = new LabelValue();
		lbSeguroItemPedidoCofins = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO);
		lvSeguroItemPedidoCofins = new LabelValue();
		lbPctCofins = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTCOFINS);
		lvPctCofins = new LabelValue();
		lbTotalItemPedidoIpi = new LabelName(Messages.TOOLTIP_LABEL_VLTOTALITEM);
		lvTotalItemPedidoIpi = new LabelValue();
		lbTotalItemPedidoFreteIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE);
		lvTotalItemPedidoFreteIpi = new LabelValue();
		lbSeguroItemPedidoIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO);
		lvSeguroItemPedidoIpi = new LabelValue();
		lbPisNoIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLIPIS);
		lvPisNoIpi = new LabelValue();
		lbCofinsNoIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICOFINS);
		lvCofinsNoIpi = new LabelValue();
		lbPctIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI);
		lvPctIpi = new LabelValue();
		lbBaseIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLBASEICMS);
		lvBaseIcms = new LabelValue();
		lbQtdItemPedidoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA);
		lvQtdItemPedidoIcms = new LabelValue();
		lbTotalItemPedidoIcms = new LabelName(Messages.TOOLTIP_LABEL_VLTOTALITEM);
		lvTotalItemPedidoIcms = new LabelValue();
		lbTotalItemPedidoFreteIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE);
		lvTotalItemPedidoFreteIcms = new LabelValue();
		lbSeguroItemPedidoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO);
		lvSeguroItemPedidoIcms = new LabelValue();
		lbPisNoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLIPIS);
		lvPisNoIcms = new LabelValue();
		lbCofinsNoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLICOFINS);
		lvCofinsNoIcms = new LabelValue();
		lbPctIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS);
		lvPctIcms = new LabelValue();
		lbIpiNoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI);
		lvIpiNoIcms = new LabelValue();
		lbBaseSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULORETIDO);
		lvBaseSt = new LabelValue();
		lbPercentualReducaoSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PERCENTUALREDUCAOST);
		lvPercentualReducaoSt = new LabelValue();
		lbQtdItemPedidoSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA);
		lvQtdItemPedidoSt = new LabelValue();
		lbTotalItemPedidoSt = new LabelName(Messages.TOOLTIP_LABEL_VLTOTALITEM);
		lvTotalItemPedidoSt = new LabelValue();
		lbTotalItemPedidoFreteSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE);
		lvTotalItemPedidoFreteSt = new LabelValue();
		lbSeguroItemPedidoSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO);
		lvSeguroItemPedidoSt = new LabelValue();
		lbIpiNaSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI);
		lvIpiNaSt = new LabelValue();
		lbPctSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_PCTST);
		lvPctSt = new LabelValue();
		lbIcmsNaSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALICMS);
		lvIcmsNaSt = new LabelValue();
		lbValorNeutroCalcVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO);
		lvValorNeutroCalcVerbaEmpresa = new LabelValue();
		lbVlTabelaVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_TABELA);
		lvVlTabelaVerbaEmpresa = new LabelValue();
		lbVlTabelaVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_TABELA);         
		lvVlTabelaVerbaEmpresa = new LabelValue();        
		lbAliquotaIcmsCalcVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS); 
		lvAliquotaIcmsCalcVerbaEmpresa = new LabelValue();
		lbAliquotaPisCalcVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS);  
		lvAliquotaPisCalcVerbaEmpresa = new LabelValue();
		lbCalcVerbaEmpresa = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);             
		lvCalcVerbaEmpresa = new LabelValue();
		lbVlVenda = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA);
		lbVlTabela = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_TABELA);
		lvVlVenda = new LabelValue();
		lvVlTabela = new LabelValue();
		lbVerbaEmpresaCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);
		lvVerbaEmpresaCalcRecVirtual = new LabelValue();
		lbVerbaGeradaCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA); 
		lvVerbaGeradaCalcRecVirtual = new LabelValue();
		lbVerbaUsadaCalcRecVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_USADA);  
		lvVerbaUsadaCalcRecVirtual = new LabelValue();
		lbIndiceFinanceiroCondPagtoPis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO);
		lvIndiceFinanceiroCondPagtoPis = new LabelValue();
		lbIndiceFinanceiroCondPagtoCofins = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO);
		lvIndiceFinanceiroCondPagtoCofins = new LabelValue();
		lbIndiceFinanceiroCondPagtoIpi = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO);
		lvIndiceFinanceiroCondPagtoIpi = new LabelValue();
		lbIndiceFinanceiroCondPagtoIcms = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO);
		lvIndiceFinanceiroCondPagtoIcms = new LabelValue();
		lbIndiceFinanceiroCondPagtoSt = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO);
		lvIndiceFinanceiroCondPagtoSt = new LabelValue();
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
			lbVlImposto = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_IMPOSTO);
			lvVlImposto = new LabelValue();
			lbVlRetorno = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_RETORNO);
			lvVlRetorno = new LabelValue();
			lbVlCustoFinanceiro = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO_FINANCEIRO);
			lvVlCustoFinanceiro = new LabelValue();
			lbVlCustosVariaveis = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO_VARIAIVEIS);
			lvVlCustosVariaveis = new LabelValue();
		}
		setDefaultRect();
	}

	private void inicializaInfosTributacao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			tributacaoConfig = itemPedido.getTributacaoConfigItem();
		}
		if (tributacaoConfig == null) {
			tributacaoConfig = new TributacaoConfig();
		}
		tributacao = itemPedido.getTributacaoItem(pedido.getCliente());
		if (tributacao == null) {
			tributacao = new Tributacao();
		}
		tributacaoVlBase = itemPedido.getVlBaseTributacaoItem();
		if (tributacaoVlBase == null) {
			tributacaoVlBase = new TributacaoVlBase();
		}
	}
	
	private Vector configuraTabsCaptions() {
		Vector tabsCaptions = new Vector();
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_PIS);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_COFINS);
			if (tributacaoConfig.isCalculaIpi()) {
				tabsCaptions.addElement(Messages.ITEMPEDIDO_LABEL_VL_IPI);
			}
		}
		if (LavenderePdaConfig.isUsaCalculaStItemPedido()) {
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_ICMS);
			tabsCaptions.addElement(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA);
		} else if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			if (tributacaoConfig.isCalculaIcms()) {
				tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_ICMS);
			}
			if (tributacaoConfig.isCalculaSt()) {
				tabsCaptions.addElement(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA);
			}
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			tabsCaptions.addElement(Messages.RENTABILIDADE_NOME_ENTIDADE);
			if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 3) {
				tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_GASTOTOTALVARIAVEL);
			}
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_ITEM);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_MARGEM_PERCENTUAL_ITEM);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_MARGEM_VALOR_ITEM);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_RECEITA_VIRTUAL_ITEM);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_VERBA_NECESSARIA);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);
		}
		return tabsCaptions;
	}
	
	@Override
	public void initUI() {
	   try {
		super.initUI();
		indexTab = -1;
		UiUtil.add(this, scrollTabbedContainer, getTop() + HEIGHT_GAP, footerH);
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			indexTab++;
			Container tabPis = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesPis(tabPis);
			indexTab++;
			Container tabCofins = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesCofins(tabCofins);
			if (tributacaoConfig.isCalculaIpi()) {
				indexTab++;
				Container tabIpi = scrollTabbedContainer.getContainer(indexTab);
				adicionaComponentesIpi(tabIpi);
			}
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			if (LavenderePdaConfig.isUsaCalculaStItemPedido() || tributacaoConfig.isCalculaIcms()) {
				indexTab++;
				Container tabIcms = scrollTabbedContainer.getContainer(indexTab);
				adicionaComponentesIcms(tabIcms);
			}
			if (LavenderePdaConfig.isUsaCalculaStItemPedido() || tributacaoConfig.isCalculaSt()) {
				indexTab++;
				Container tabSt = scrollTabbedContainer.getContainer(indexTab);
				adicionaComponentesSt(tabSt);
			}
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			indexTab++;
			adicionaComponentesRentabilidade();
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			indexTab++;
			Container tabIndiceRentabilidadeItem = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesIndiceRentabilidadeItem(tabIndiceRentabilidadeItem);
			indexTab++;
			Container tabMargemPercentualItem = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesMargemPercentualItem(tabMargemPercentualItem);
			indexTab++;
			Container tabMargemValorItem = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesMargemValorItem(tabMargemValorItem);
			indexTab++;
			Container tabReceitaVirtualItem = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesReceitaVirtualItem(tabReceitaVirtualItem);
			indexTab++;
			Container tabVerbaGeradaItem = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesVerbaGeradaItem(tabVerbaGeradaItem);
			indexTab++;
			Container tabVerbaNecessaria = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesVerbaNecessaria(tabVerbaNecessaria);
			indexTab++;
			Container tabValorNeutro = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesValorNeutro(tabValorNeutro);
			indexTab++;
			Container tabVerbaEmpresa = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesVerbaEmpresa(tabVerbaEmpresa);
			indexTab++;
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void adicionaComponentesRentabilidade() {
		Container tabRentabilidade = scrollTabbedContainer.getContainer(indexTab);
		UiUtil.add(tabRentabilidade, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabRentabilidade, formulaRentabilidadeContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
		UiUtil.add(formulaRentabilidadeContainer, lbFormulaRentabilidade, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaRentabilidade.setMultipleLinesText(getFormulaRentabilidade());
		formulaRentabilidadeContainer.resizeHeight();
		formulaRentabilidadeContainer.resetSetPositions();
		formulaRentabilidadeContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaRentabilidadeContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 3) {
			UiUtil.add(tabRentabilidade, lbPrecoTotalComImpostos, lvPrecoTotalComImpostos, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabRentabilidade, lbPrecoTotalCusto, lvPrecoTotalCusto, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbGastoTotalVariavel, lvGastoTotalVariavel, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbCustoTotalFixo, lvCustoTotalFixo, SAME, AFTER + HEIGHT_GAP);
			Button sep1 = new Button("");
			sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabRentabilidade, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabRentabilidade, lbVlRentabilidade, lvVlRentabilidade, SAME, AFTER + HEIGHT_GAP);
			//--
			indexTab++;
			Container tabGastoTotalVariavel = scrollTabbedContainer.getContainer(indexTab);
			UiUtil.add(tabGastoTotalVariavel, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, formulaGastoTotalVariavelContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
			UiUtil.add(formulaGastoTotalVariavelContainer, lbFormulaGastoTotalVariavel, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
			lbFormulaGastoTotalVariavel.setMultipleLinesText(getFormulaGastoTotalVariavel());
			formulaGastoTotalVariavelContainer.resizeHeight();
			formulaGastoTotalVariavelContainer.resetSetPositions();
			formulaGastoTotalVariavelContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaGastoTotalVariavelContainer.getHeight() + BaseContainer.HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalPis, lvVlTotalPis, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalCofins, lvVlTotalCofins, SAME, AFTER + HEIGHT_GAP);
			if ((LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProdutoComICMS())) {
				UiUtil.add(tabGastoTotalVariavel, lbVlTotalIcms, lvVlTotalIcms, SAME, AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalSt, lvVlTotalSt, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalIpi, lvVlTotalIpi, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalIrpj, lvVlTotalIrpj, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalCsll, lvVlTotalCsll, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalCpp, lvVlTotalCpp, SAME, AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProduto()) {
				UiUtil.add(tabGastoTotalVariavel, lbVlTotalCustoVariavelProduto, lvVlTotalCustoVariavelProduto, SAME, AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabGastoTotalVariavel, lbVlTotalCustoVariavel, lvVlTotalCustoVariavel, SAME, AFTER + HEIGHT_GAP);
			Button sep2 = new Button("");
			sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabGastoTotalVariavel, sep2, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabGastoTotalVariavel, lbVlGastoTotalVariavel, lvVlGastoTotalVariavel, SAME, AFTER + HEIGHT_GAP);
		} else if (LavenderePdaConfig.usaValorRentabilidadeSemCalculo()) {
			UiUtil.add(tabRentabilidade, lbVlVenda, lvVlVenda, getLeft(), AFTER + HEIGHT_GAP_BIG);
			if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 5) {
				UiUtil.add(tabRentabilidade, lbVlStRentabilidade, lvVlStRentabilidade, getLeft(), AFTER + HEIGHT_GAP_BIG);
			}
			UiUtil.add(tabRentabilidade, lbPrecoTotalCusto, lvPrecoTotalCusto, SAME, AFTER + HEIGHT_GAP);
			Button sep3 = new Button("");
			sep3.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabRentabilidade, sep3, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabRentabilidade, lbPctRentabilidade, lvPctRentabilidade, SAME, AFTER + HEIGHT_GAP);
		} else if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
			UiUtil.add(tabRentabilidade, lbPrecoTotalVenda, lvPrecoTotalVenda, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabRentabilidade, lbPrecoTotalCusto, lvPrecoTotalCusto, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbVlImposto, lvVlImposto, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbVlRetorno, lvVlRetorno, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbVlCustosVariaveis, lvVlCustosVariaveis, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabRentabilidade, lbVlCustoFinanceiro, lvVlCustoFinanceiro, SAME, AFTER + HEIGHT_GAP);
			Button sep3 = new Button("");
			sep3.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabRentabilidade, sep3, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabRentabilidade, lbVlRentabilidade, lvVlRentabilidade, SAME, AFTER + HEIGHT_GAP);			
		} else {
			UiUtil.add(tabRentabilidade, lbPrecoTotalVenda, lvPrecoTotalVenda, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabRentabilidade, lbPrecoTotalCusto, lvPrecoTotalCusto, SAME, AFTER + HEIGHT_GAP);
			Button sep3 = new Button("");
			sep3.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabRentabilidade, sep3, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabRentabilidade, lbVlRentabilidade, lvVlRentabilidade, SAME, AFTER + HEIGHT_GAP);
		}
	}
	
	private void adicionaComponentesIcms(Container tabIcms) throws SQLException {
		UiUtil.add(tabIcms, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabIcms, formulaIcmsContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaIcmsContainer, lbFormulaIcms, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaIcms.setMultipleLinesText(getFormulaIcms());
		formulaIcmsContainer.resizeHeight();
		formulaIcmsContainer.resetSetPositions();
		formulaIcmsContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaIcmsContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			adicionaComponentesIcmsValorFixoImpostos(tabIcms);
			return;
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			adicionaComponentesIcmsPersonalizado(tabIcms);
			return;
		} 
		adicionaComponentesIcmsNormal(tabIcms);
	}
	
	private void adicionaComponentesIcmsPersonalizado(Container tabIcms) throws SQLException {
		if (tributacaoVlBase.vlBaseIcms > 0) {
			UiUtil.add(tabIcms, lbBaseIcms, lvBaseIcms, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabIcms, lbQtdItemPedidoIcms, lvQtdItemPedidoIcms, SAME, AFTER + HEIGHT_GAP);
		} else {
			double vlBaseCalculoIcms = STService.getInstance().getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, tributacaoVlBase.isExistePrecoBaseIcmsCalcRetido());
			UiUtil.add(tabIcms, lbTotalItemPedidoIcms, lvTotalItemPedidoIcms, getLeft(), AFTER + HEIGHT_GAP_BIG);
			if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseICMS()) {
				UiUtil.add(tabIcms, lbIndiceFinanceiroCondPagtoIcms, lvIndiceFinanceiroCondPagtoIcms, SAME, AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
				double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
				boolean aplicaIndiceCondPagtoVlBaseICMS = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseICMS();
				double vlTotalItemPedidoComSeguroICMS = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseICMS, vlIndiceCondPagto);
				vlBaseCalculoIcms += vlTotalItemPedidoComSeguroICMS;
				UiUtil.add(tabIcms, lbSeguroItemPedidoIcms, lvSeguroItemPedidoIcms, SAME, AFTER + HEIGHT_GAP);
			}
			if (tributacaoConfig.isAplicaValorFreteNaBaseIcms()) {
				vlBaseCalculoIcms += itemPedido.vlTotalItemPedidoFrete;
				UiUtil.add(tabIcms, lbTotalItemPedidoFreteIcms, lvTotalItemPedidoFreteIcms, SAME, AFTER + HEIGHT_GAP);
			}
			if (tributacaoConfig.isAplicaValorIpiNaBaseIcms()) {
				vlBaseCalculoIcms += itemPedido.vlTotalIpiItem;
				UiUtil.add(tabIcms, lbIpiNoIcms, lvIpiNoIcms, SAME, AFTER + HEIGHT_GAP);
			}
			if (tributacao.isBaseIcmsRetidoComRepasse()) {
				UiUtil.add(tabIcms, lbPctRepasseCalcIcms, lvPctRepasseCalcIcms, SAME, AFTER + HEIGHT_GAP);
				double pctRepasseIcms = STService.getInstance().calculaRepasseIcms(vlBaseCalculoIcms, tributacao.vlPctRepasseIcms);
				lvPctRepasseCalcIcms.setValue(pctRepasseIcms);
				vlBaseCalculoIcms -= pctRepasseIcms;
			}
			UiUtil.add(tabIcms, lbPctReducaoCalcIcms, lvPctReducaoCalcIcms, SAME, AFTER + HEIGHT_GAP);
			double pctReducaoBaseIcms = STService.getInstance().calculaReducaoBaseIcms(vlBaseCalculoIcms, tributacao.vlPctReducaoBaseIcms);
			lvPctReducaoCalcIcms.setValue(pctReducaoBaseIcms);
			vlBaseCalculoIcms -= pctReducaoBaseIcms;
			if (tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				UiUtil.add(tabIcms, lbPisNoIcms, lvPisNoIcms, SAME, AFTER + HEIGHT_GAP);
				UiUtil.add(tabIcms, lbCofinsNoIcms, lvCofinsNoIcms, SAME, AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabIcms, lbPctReducaoIcms, lvPctReducaoIcms, SAME, AFTER + HEIGHT_GAP);
			lvPctReducaoIcms.setValue(STService.getInstance().calculaReducaoIcms(vlBaseCalculoIcms, tributacao.vlPctReducaoIcms));
		}
		UiUtil.add(tabIcms, lbPctIcms, lvPctIcms, SAME, AFTER + HEIGHT_GAP);
	}

	private void adicionaComponentesIcmsNormal(Container tabIcms) {
		UiUtil.add(tabIcms, lbBaseCalculoIcms, lvBaseCalculoIcms, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabIcms, lbVlFreteCalcIcms, lvVlFreteCalcIcms, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbVlDespesasAcessorias, lvVlDespesasAcessorias, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbPctIpiCalcIcms, lvPctIpiCalcIcms, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbPctRepasseCalcIcms, lvPctRepasseCalcIcms, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbPctReducaoCalcIcms, lvPctReducaoCalcIcms, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbAliquotaIcms, lvAliquotaIcms, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIcms, lbPctReducaoIcms, lvPctReducaoIcms, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabIcms, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabIcms, lbVlIcmsCalcIcms, lvVlIcmsCalcIcms, SAME, AFTER + HEIGHT_GAP);
	}

	private void adicionaComponentesIcmsValorFixoImpostos(Container tabIcms) {
		UiUtil.add(tabIcms, lbValorIcms, lvValorIcms, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabIcms, lbQtdItemPedidoIcms, lvQtdItemPedidoIcms, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesSt(Container tabSt) throws SQLException {
		UiUtil.add(tabSt, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabSt, formulaStContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaStContainer, lbFormulaSt, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaSt.setMultipleLinesText(getFormulaSt());
		formulaStContainer.resizeHeight();
		formulaStContainer.resetSetPositions();
		formulaStContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaStContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			adicionaComponentesStValorFixoImpostos(tabSt);
			return;
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			adicionaComponentesStPersonalizado(tabSt);
			return;
		} 
		adicionaComponentesStNormal(tabSt);
	}
	
	private void adicionaComponentesStPersonalizado(Container tabSt) throws SQLException {
		if (tributacaoVlBase.vlBaseIcmsRetidoCalcRetido != 0) {
			double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
			boolean aplicaIndiceCondPagtoVlBaseST = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST();
			double vlTotalItemPedidoComSeguroST = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseST, vlIndiceCondPagto);
			if (tributacaoConfig.isVerificaValorItem() && ValueUtil.round(vlTotalItemPedidoComSeguroST) >= ValueUtil.round(tributacaoVlBase.vlBaseIcmsRetidoCalcRetido)) {
				UiUtil.add(tabSt, lbTotalItemPedidoSt, lvTotalItemPedidoSt, getLeft(), AFTER + HEIGHT_GAP_BIG);
				if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST()) {
					UiUtil.add(tabSt, lbIndiceFinanceiroCondPagtoSt, lvIndiceFinanceiroCondPagtoSt, SAME, AFTER + HEIGHT_GAP);
				}
				double vlBaseIcmsRetidoCalcRetido = itemPedido.vlTotalItemPedido;
				if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
					UiUtil.add(tabSt, lbSeguroItemPedidoSt, lvSeguroItemPedidoSt, SAME, AFTER + HEIGHT_GAP);
					vlBaseIcmsRetidoCalcRetido += itemPedido.vlItemPedidoFrete;
				}
				if (tributacaoConfig.isAplicaValorFreteNaBaseSt()) {
					UiUtil.add(tabSt, lbTotalItemPedidoFreteSt, lvTotalItemPedidoFreteSt, SAME, AFTER + HEIGHT_GAP);
					vlBaseIcmsRetidoCalcRetido += itemPedido.getVlTotalSeguro();
				}
				if (tributacaoConfig.isAplicaValorIpiNaBaseSt()) {
					UiUtil.add(tabSt, lbIpiNaSt, lvIpiNaSt, SAME, AFTER + HEIGHT_GAP);
					vlBaseIcmsRetidoCalcRetido += itemPedido.vlTotalIpiItem;
				}
				if (tributacao.isBaseIcmsRetidoComRepasse()) {
					UiUtil.add(tabSt, lbPctRepasseCalcSt, lvPctRepasseCalcSt, SAME, AFTER + HEIGHT_GAP);
					lvPctRepasseCalcSt.setValue(STService.getInstance().calculaRepasseIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctRepasseIcms));
				}
				UiUtil.add(tabSt, lbMargemAgregada, lvMargemAgregada, SAME, AFTER + HEIGHT_GAP);
				UiUtil.add(tabSt, lbPctReducaoCalcSt, lvPctReducaoCalcSt, SAME, AFTER + HEIGHT_GAP);
				lvMargemAgregada.setValue(STService.getInstance().calculaMargemAgregada(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctMargemAgregada));
				lvPctReducaoCalcSt.setValue(STService.getInstance().calculaReducaoIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctReducaoBaseIcmsRetido));
			} else {
				UiUtil.add(tabSt, lbBaseSt, lvBaseSt, getLeft(), AFTER + HEIGHT_GAP_BIG);
				if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flAplicaReducaoBaseIcmsRetido)) {
					UiUtil.add(tabSt, lbPercentualReducaoSt, lvPercentualReducaoSt, SAME, AFTER + HEIGHT_GAP);
				}
				UiUtil.add(tabSt, lbQtdItemPedidoSt, lvQtdItemPedidoSt, SAME, AFTER + HEIGHT_GAP);
			}
		} else {
			UiUtil.add(tabSt, lbTotalItemPedidoSt, lvTotalItemPedidoSt, getLeft(), AFTER + HEIGHT_GAP_BIG);
			double vlBaseIcmsRetidoCalcRetido = itemPedido.vlTotalItemPedido;
			if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST()) {
				UiUtil.add(tabSt, lbIndiceFinanceiroCondPagtoSt, lvIndiceFinanceiroCondPagtoSt, SAME, AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
				UiUtil.add(tabSt, lbSeguroItemPedidoSt, lvSeguroItemPedidoSt, SAME, AFTER + HEIGHT_GAP);
				vlBaseIcmsRetidoCalcRetido += itemPedido.vlItemPedidoFrete;
			}
			if (tributacaoConfig.isAplicaValorFreteNaBaseSt()) {
				UiUtil.add(tabSt, lbTotalItemPedidoFreteSt, lvTotalItemPedidoFreteSt, SAME, AFTER + HEIGHT_GAP);
				vlBaseIcmsRetidoCalcRetido += itemPedido.getVlTotalSeguro();
			}
			if (tributacaoConfig.isAplicaValorIpiNaBaseSt()) {
				UiUtil.add(tabSt, lbIpiNaSt, lvIpiNaSt, SAME, AFTER + HEIGHT_GAP);
				vlBaseIcmsRetidoCalcRetido += itemPedido.vlTotalIpiItem;
			}
			if (tributacao.isBaseIcmsRetidoComRepasse()) {
				UiUtil.add(tabSt, lbPctRepasseCalcSt, lvPctRepasseCalcSt, SAME, AFTER + HEIGHT_GAP);
				lvPctRepasseCalcSt.setValue(STService.getInstance().calculaRepasseIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctRepasseIcms));
			}
			UiUtil.add(tabSt, lbMargemAgregada, lvMargemAgregada, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbPctReducaoCalcSt, lvPctReducaoCalcSt, SAME, AFTER + HEIGHT_GAP);
			lvMargemAgregada.setValue(STService.getInstance().calculaMargemAgregada(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctMargemAgregada));
			lvPctReducaoCalcSt.setValue(STService.getInstance().calculaReducaoIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctReducaoBaseIcmsRetido));
		}
		UiUtil.add(tabSt, lbPctSt, lvPctSt, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabSt, lbIcmsNaSt, lvIcmsNaSt, SAME, AFTER + HEIGHT_GAP);
	}

	private void adicionaComponentesStNormal(Container tabSt) {
		if (LavenderePdaConfig.isUsaCalculaStItemPedido() && tributacao != null) {
			UiUtil.add(tabSt, lbBaseCalculoRetido, lvBaseCalculoRetido, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(tabSt, lbVlFreteCalcSt, lvVlFreteCalcSt, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbPctIpiCalcSt, lvPctIpiCalcSt, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbPctRepasseCalcSt, lvPctRepasseCalcSt, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbMargemAgregada, lvMargemAgregada, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbPctReducaoCalcSt, lvPctReducaoCalcSt, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabSt, lbAliquotaRetido, lvAliquotaRetido, SAME, AFTER + HEIGHT_GAP);
			if (tributacao.isPossuiFundoDePobreza()) {
				UiUtil.add(tabSt, lbAliquotaFecop, lvAliquotaFecop, SAME, AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabSt, lbVlIcmsCalcSt, lvVlIcmsCalcSt, SAME, AFTER + HEIGHT_GAP);
			if (tributacao.isPossuiFundoDePobreza()) {
				UiUtil.add(tabSt, lbVlFecopRecolher, lvVlFecopRecolher, SAME, AFTER + HEIGHT_GAP);
			}
			Button sep1 = new Button("");
			sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			UiUtil.add(tabSt, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
			UiUtil.add(tabSt, lbVlSt, lvVlSt, SAME, AFTER + HEIGHT_GAP);
		}
	}

	private void adicionaComponentesStValorFixoImpostos(Container tabSt) {
		UiUtil.add(tabSt, lbSTRetido, lvSTRetido, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabSt, lbQtdItemPedidoSt, lvQtdItemPedidoSt, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesIndiceRentabilidadeItem(Container tabIndiceRentabilidadeItem) {
		UiUtil.add(tabIndiceRentabilidadeItem, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabIndiceRentabilidadeItem, formulaIndiceRentabilidadeItemContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaIndiceRentabilidadeItemContainer, lbFormulaIndiceRentabilidadeItem, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaIndiceRentabilidadeItem.setMultipleLinesText(getFormulaIndiceRentabilidadeItem());
		formulaIndiceRentabilidadeItemContainer.resizeHeight();
		formulaIndiceRentabilidadeItemContainer.resetSetPositions();
		formulaIndiceRentabilidadeItemContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaIndiceRentabilidadeItemContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabIndiceRentabilidadeItem, lbMargemPercentual, lvMargemPercentual, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabIndiceRentabilidadeItem, lbMargemMinima, lvMargemMinima, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIndiceRentabilidadeItem, lbMargemEspecifica, lvMargemEspecifica, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabIndiceRentabilidadeItem, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabIndiceRentabilidadeItem, lbVlIndiceRentabilidadeItem, lvVlIndiceRentabilidadeItem, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesMargemPercentualItem(Container tabMargemPercentualItem) {
		UiUtil.add(tabMargemPercentualItem, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabMargemPercentualItem, formulaMargemPercentualItemContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaMargemPercentualItemContainer, lbFormulaMargemPercentualItem, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaMargemPercentualItem.setMultipleLinesText(getFormulaMargemPercentualItem());
		formulaMargemPercentualItemContainer.resizeHeight();
		formulaMargemPercentualItemContainer.resetSetPositions();
		formulaMargemPercentualItemContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaMargemPercentualItemContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabMargemPercentualItem, lbMargemValor, lvMargemValor, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabMargemPercentualItem, lbReceitaVirtual, lvReceitaVirtual, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabMargemPercentualItem, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabMargemPercentualItem, lbVlMargemPercentual, lvVlMargemPercentual, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesMargemValorItem(Container tabMargemValorItem) {
		UiUtil.add(tabMargemValorItem, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabMargemValorItem, formulaMargemValorItemContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaMargemValorItemContainer, lbFormulaMargemValorItem, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaMargemValorItem.setMultipleLinesText(getFormulaMargemValorItem());
		formulaMargemValorItemContainer.resizeHeight();
		formulaMargemValorItemContainer.resetSetPositions();
		formulaMargemValorItemContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaMargemValorItemContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabMargemValorItem, lbReceitaLiquida, lvReceitaLiquida, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabMargemValorItem, lbCusto, lvCusto, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabMargemValorItem, lbVerbaEmpresa, lvVerbaEmpresa, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabMargemValorItem, lbVerbaGerada, lvVerbaGerada, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabMargemValorItem, lbVerbaUsada, lvVerbaUsada, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabMargemValorItem, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabMargemValorItem, lbVlMargemValor, lvVlMargemValor, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesReceitaVirtualItem(Container tabReceitaVirtualItem) {
		UiUtil.add(tabReceitaVirtualItem, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabReceitaVirtualItem, formulaReceitaVirtualItemContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaReceitaVirtualItemContainer, lbFormulaReceitaVirtualItem, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaReceitaVirtualItem.setMultipleLinesText(getFormulaReceitaVirtualItem());
		formulaReceitaVirtualItemContainer.resizeHeight();
		formulaReceitaVirtualItemContainer.resetSetPositions();
		formulaReceitaVirtualItemContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaReceitaVirtualItemContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabReceitaVirtualItem, lbValorVenda, lvValorVenda, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabReceitaVirtualItem, lbAliquotaPis, lvAliquotaPis, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabReceitaVirtualItem, lbAliquotaIcmsCalcRecVirtual, lvAliquotaIcmsCalcRecVirtual, SAME, AFTER + HEIGHT_GAP);
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			UiUtil.add(tabReceitaVirtualItem, lbVlIcmsCalcRecVirtual, lvVlIcmsCalcRevVirtual, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabReceitaVirtualItem, lbVlStCalcRecVirtual, lvVlStCalcRecVirtual, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabReceitaVirtualItem, lbVerbaEmpresaCalcRecVirtual, lvVerbaEmpresaCalcRecVirtual, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabReceitaVirtualItem, lbVerbaGeradaCalcRecVirtual, lvVerbaGeradaCalcRecVirtual, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabReceitaVirtualItem, lbVerbaUsadaCalcRecVirtual, lvVerbaUsadaCalcRecVirtual, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabReceitaVirtualItem, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabReceitaVirtualItem, lbVlReceitaVirtual, lvVlReceitaVirtual, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesVerbaGeradaItem(Container tabVerbaGeradaItem) {
		UiUtil.add(tabVerbaGeradaItem, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabVerbaGeradaItem, formulaVerbaGeradaContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaVerbaGeradaContainer, lbFormulaVerbaGerada, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaVerbaGerada.setMultipleLinesText(getFormulaVerbaGerada());
		formulaVerbaGeradaContainer.resizeHeight();
		formulaVerbaGeradaContainer.resetSetPositions();
		formulaVerbaGeradaContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaVerbaGeradaContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabVerbaGeradaItem, lbAliquotaPisCalcVerbaGerada, lvAliquotaPisCalcVerbaGerada, getLeft(), AFTER + HEIGHT_GAP);
		if (tributacao == null || tributacao.vlIcmsRetido == 0) {
			UiUtil.add(tabVerbaGeradaItem, lbAliquotaIcmsCalcVerbaGerada, lvAliquotaIcmsCalcVerbaGerada, SAME, AFTER + HEIGHT_GAP_BIG);
		}
		UiUtil.add(tabVerbaGeradaItem, lbVlVenda, lvVlVenda, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabVerbaGeradaItem, lbVlTabela, lvVlTabela, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabVerbaGeradaItem, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabVerbaGeradaItem, lbVlVerbaGerada, lvVlVerbaGerada, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesVerbaNecessaria(Container tabVerbaNecessaria) {
		UiUtil.add(tabVerbaNecessaria, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabVerbaNecessaria, formulaVerbaNecessariaContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaVerbaNecessariaContainer, lbFormulaVerbaNecessaria, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaVerbaNecessaria.setMultipleLinesText(getFormulaVerbaNecessaria());
		formulaVerbaNecessariaContainer.resizeHeight();
		formulaVerbaNecessariaContainer.resetSetPositions();
		formulaVerbaNecessariaContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaVerbaNecessariaContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabVerbaNecessaria, lbValorVendaCalcVerbaNecessaria, lvValorVendaCalcVerbaNecessaria, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabVerbaNecessaria, lbValorNeutroCalcVerbaNecessaria, lvValorNeutroCalcVerbaNecessaria, SAME, AFTER + HEIGHT_GAP);
		if (tributacao == null || tributacao.vlIcmsRetido == 0) {
			UiUtil.add(tabVerbaNecessaria, lbAliquotaIcmsCalcVerbaNecessaria, lvAliquotaIcmsCalcVerbaNecessaria, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabVerbaNecessaria, lbAliquotaPisCalcVerbaNecessaria, lvAliquotaPisCalcVerbaNecessaria, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabVerbaNecessaria, lbVerbaEmpresaCalcVerbaNecessaria, lvVerbaEmpresaCalcVerbaNecessaria, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabVerbaNecessaria, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabVerbaNecessaria, lbVlVerbaNecessaria, lvVlVerbaNecessaria, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesValorNeutro(Container tabValorNeutro) {
		UiUtil.add(tabValorNeutro, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, formulaValorNeutroContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaValorNeutroContainer, lbFormulaValorNeutro, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaValorNeutro.setMultipleLinesText(getFormulaValorNeutro());
		formulaValorNeutroContainer.resizeHeight();
		formulaValorNeutroContainer.resetSetPositions();
		formulaValorNeutroContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaValorNeutroContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, lbCustoCalcValorNeutro, lvCustoCalcValorNeutro, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabValorNeutro, lbAliquotaIcmsCalcValorNeutro, lvAliquotaIcmsCalcValorNeutro, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, lbAliquotaPisCalcValorNeutro, lvAliquotaPisCalcValorNeutro, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, lbMargemEspecificaCalcValorNeutro, lvMargemEspecificaCalcValorNeutro, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, lbValorIcmsCalcValorNeutro, lvValorIcmsCalcValorNeutro, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabValorNeutro, lbValorStCalcValorNeutro, lvValorStCalcValorNeutro, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabValorNeutro, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabValorNeutro, lbVlValorNeutro, lvVlValorNeutro, SAME, AFTER + HEIGHT_GAP);
	}
	
	private String getFormulaRentabilidade() {
		String vlRentabilidadeItem = Messages.REL_DETALHES_CALCULOS_LABEL_VLRENTABILIDADE;
		String precoTotalCusto = Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALCUSTO;
		switch (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido) {
		case 3:
			return getFormulaRentabilidadeTipo3(vlRentabilidadeItem, precoTotalCusto);
		case 5:
			return getFormulaRentabilidadeTipo5(precoTotalCusto);
		case 6:
			return getFormulaRentabilidade(precoTotalCusto);
		case 7:
			return getFormulaRentabilidade(precoTotalCusto);
		case 8:
			return getFormulaRentabilidadeTipo8(vlRentabilidadeItem);	
		default:
			return getFormulaRentabilidadeDemaisTipos(vlRentabilidadeItem, precoTotalCusto);
		}
	}
	
	private String getFormulaRentabilidadeTipo8(String vlRentabilidadeItem) {
		String precoTotalVendaItem = Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_PRECOTOTVENDA;
		String precoTotalCustoItem = Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_PRECOTOTCUSTO;
		String imposto = Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_IMPOSTO;
		String retorno = Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_RETORNO;
		String custoVariaveis = Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_CUSTOSVARIAV;
		String custoFinanceiro =  Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA_CUSTOFINANCEIROS;
		return new StringBuilder(vlRentabilidadeItem).append(" = ").append(precoTotalVendaItem)
				.append(" - (").append(precoTotalCustoItem).append(" + ").append(imposto).append(" + ").append(retorno).append(" + ((").append(precoTotalVendaItem)
				.append(" * ").append(custoVariaveis).append(") + (").append(precoTotalVendaItem).append(" * ").append(custoFinanceiro).append(")))").toString();
	}

	private String getFormulaRentabilidadeTipo3(String vlRentabilidadeItem, String precoTotalCusto) {
		String precoTotalComImpostos = Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALCOMIMPOSTOS;
		String gastoTotalVariavel = Messages.REL_DETALHES_CALCULOS_LABEL_GASTOTOTALVARIAVEL;
		String custoTotalFixo = Messages.REL_DETALHES_CALCULOS_LABEL_CUSTOTOTALFIXO;
		return vlRentabilidadeItem + " = " + precoTotalComImpostos + " - " + precoTotalCusto + " - " + gastoTotalVariavel + " - " + custoTotalFixo;
	}

	private String getFormulaRentabilidadeTipo5(String precoTotalCusto) {
		String valorItem = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA;
		String valorSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
		String pctRentabilidadeItem = Messages.REL_DETALHES_CALCULOS_LABEL_PCTRENTABILIDADE;
		return new StringBuilder(pctRentabilidadeItem).append(" = (1 -  ").append(precoTotalCusto).append(" / (").append(valorItem).append(" + ").append(valorSt).append(") ) * 100 ").toString();
	}
	
	private String getFormulaRentabilidade(String precoTotalCusto) {
		String valorItem = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALITEM;
		String pctRentabilidadeItem = Messages.REL_DETALHES_CALCULOS_LABEL_PCTRENTABILIDADE;
		return new StringBuilder(pctRentabilidadeItem).append(" = (").append(valorItem).append(" / ").append(precoTotalCusto).append(" - 1) * 100 ").toString();
	}
	
	private String getFormulaRentabilidadeDemaisTipos(String vlRentabilidadeItem, String precoTotalCusto) {
		String precoTotalVenda = Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALVENDA;
		return vlRentabilidadeItem + " = " + precoTotalVenda + " - " + precoTotalCusto;
	}
	
	private String getFormulaGastoTotalVariavel() {
		String gastoTotalVariavel = Messages.REL_DETALHES_CALCULOS_LABEL_GASTOTOTALVARIAVEL;
		String vlTotalPis = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALPIS;
		String vlTotalCofins = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCOFINS;
		String vlTotalIcms = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALICMS;
		String vlTotalSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALST;
		String vlTotalIpi = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI;
		String vlTotalIrpj = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIRPJ;
		String vlTotalCsll = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCSLL;
		String vlTotalCpp = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCPP;
		String vlTotalCustoVariavel = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCUSTOVARIAVEL;
		String vlTotalCustoVariavelProduto = Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCUSTOVARIAVELPRODUTO;
		return gastoTotalVariavel + " = " + vlTotalPis + " + " + vlTotalCofins + (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProdutoComICMS() ? vlTotalIcms + " + " : "" )
				+ vlTotalSt + " + " + vlTotalIpi + " + " + vlTotalIrpj + " + " + vlTotalCsll + " + " + vlTotalCpp + " + " + 
				(LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProduto() ? vlTotalCustoVariavelProduto + " + " : "")
				+  vlTotalCustoVariavel;
	}
	
	private String getFormulaIcms() throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			String totalIcms = Messages.REL_DETALHES_CALCULOS_LABEL_TOTALICMS;
			String vlIcms = Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
			String qtVendida = Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA;
			return totalIcms + " = (" + vlIcms + " * " + qtVendida + ")";
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			boolean possuiBaseIcms = tributacaoVlBase.vlBaseIcms > 0;
			String formulaIcms = "";
			formulaIcms = possuiBaseIcms ? Messages.REL_DETALHES_CALCULOS_LABEL_VLBASEICMS : Messages.TOOLTIP_LABEL_VLTOTALITEM;
			if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseICMS()) {
				formulaIcms = "(" + formulaIcms + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
			}
			if (possuiBaseIcms) {
				formulaIcms += " * " + Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA;
			} else {
				if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
					formulaIcms += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO;
				}
				if (tributacaoConfig.isAplicaValorFreteNaBaseIcms()) {
					formulaIcms += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE;
				}
				if (tributacaoConfig.isAplicaValorIpiNaBaseIcms()) {
					formulaIcms += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI;
				}
				if (tributacao.isBaseIcmsRetidoComRepasse()) {
					formulaIcms += " - " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO;
				}
				formulaIcms += " - " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE;
				if (tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
					formulaIcms += " - " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALPIS + " - " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCOFINS;
				}
			}
			formulaIcms = "(" + formulaIcms + ") * " + Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
			formulaIcms = !possuiBaseIcms ? "(" + formulaIcms + ") - " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAOICMS : formulaIcms;
			return Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS + " = " + formulaIcms;
		} else {
			String vlIcms = Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
			String baseCalculoIcms = Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULOICMS;
			String vlFrete = Messages.REL_DETALHES_CALCULOS_LABEL_VLFRETE;
			String vlDespesasAcessorias = Messages.REL_DETALHES_CALCULOS_LABEL_VLDESPESASACESSORIAS;
			String pctIpi = Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI;
			String pctRepasse = Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE;
			String pctReducao = Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO;
			String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
			String pctReducaoIcms = Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAOICMS;
			return vlIcms + " = ((" + baseCalculoIcms + " + " + vlFrete + " + " + vlDespesasAcessorias + " + " + pctIpi + " - " + pctRepasse + " - " + pctReducao + ") * " + aliquotaIcms + ") - " + pctReducaoIcms;
		}
	}
	
	private String getFormulaSt() throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			String totalST = Messages.REL_DETALHES_CALCULOS_LABEL_TOTALST;
			String vlST = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
			String qtVendida = Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA;
			return totalST + " = (" + vlST + " * " + qtVendida + ")";
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			String pctIcms = Messages.REL_DETALHES_CALCULOS_LABEL_PCTST;
			String formulaSt = "";
			if (tributacaoVlBase.vlBaseIcmsRetidoCalcRetido != 0) { 
				double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
				boolean aplicaIndiceCondPagtoVlBaseST = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST();
				double vlTotalItemPedidoComSeguroST = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseST, vlIndiceCondPagto);
				if (tributacaoConfig.isVerificaValorItem() && ValueUtil.round(vlTotalItemPedidoComSeguroST) >= ValueUtil.round(tributacaoVlBase.vlBaseIcmsRetidoCalcRetido)) {
					String vlSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
					if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST()) {
						vlSt = "(" + vlSt + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
					}
					String baseCalculoRetido = Messages.REL_DETALHES_CALCULOS_LABEL_PRECOTOTALVENDA;
					if (tributacaoConfig.isAplicaValorFreteNaBaseSt()) {
						baseCalculoRetido += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLFRETE;
					}
					if (tributacaoConfig.isAplicaValorIpiNaBaseSt()) {
						baseCalculoRetido += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI;
					}
					baseCalculoRetido += ") + " + Messages.REL_DETALHES_CALCULOS_LABEL_MERGEMAGREGADA;
					String aliquotaRetido = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTARETIDO;
					String vlIcms = tributacao.vlPctOutorga != 0 ? Messages.REL_DETALHES_CALCULOS_LABEL_VLICMSOUTORGA : Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
					return vlSt + " = (((" + baseCalculoRetido + ") * " + aliquotaRetido + ") - " + vlIcms;
				} else {
					formulaSt = Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULORETIDO;
					if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flAplicaReducaoBaseIcmsRetido)) {
						formulaSt += " - " +  Messages.REL_DETALHES_CALCULOS_LABEL_PERCENTUALDEREDUCAOST;
					}
					formulaSt += " * " + Messages.REL_DETALHES_CALCULOS_LABEL_QTDVENDIDA;
					return Messages.REL_DETALHES_CALCULOS_LABEL_VLST + " = ((" + formulaSt + ") * " + pctIcms + ") - " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALICMS; 
				}
			} else {
				formulaSt = Messages.TOOLTIP_LABEL_VLTOTALITEM;
				if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST()) {
					formulaSt = "(" + formulaSt + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
				}
				if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
					formulaSt += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO;
				}
				if (tributacaoConfig.isAplicaValorFreteNaBaseSt()) {
					formulaSt += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE;
				}
				if (tributacaoConfig.isAplicaValorIpiNaBaseSt()) {
					formulaSt += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALIPI;
				}
				if (tributacao.isBaseIcmsRetidoComRepasse()) {
					formulaSt += " - " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE;
				}
				formulaSt += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_MERGEMAGREGADA;
				formulaSt += " - " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO;
				return Messages.REL_DETALHES_CALCULOS_LABEL_VLST + " = ((" + formulaSt + ") * " + pctIcms + ") - " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALICMS; 
			}
		} else if (LavenderePdaConfig.isUsaCalculaStItemPedido() && tributacao != null) {
			String vlSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
			String baseCalculoRetido = Messages.REL_DETALHES_CALCULOS_LABEL_BASECALCULORETIDO;
			String vlFrete = Messages.REL_DETALHES_CALCULOS_LABEL_VLFRETE;
			String pctIpi = Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI;
			String pctRepasse = Messages.REL_DETALHES_CALCULOS_LABEL_PCTREPASSE;
			String margemAgregada = Messages.REL_DETALHES_CALCULOS_LABEL_MERGEMAGREGADA;
			String pctReducao = Messages.REL_DETALHES_CALCULOS_LABEL_PCTREDUCAO;
			String aliquotaRetido = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTARETIDO;
			String vlIcms = tributacao.vlPctOutorga != 0 ? Messages.REL_DETALHES_CALCULOS_LABEL_VLICMSOUTORGA : Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
			if (tributacao.isPossuiFundoDePobreza()) {
				String aliquotaFecop = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAFECOP;
				String vlFecopRecolher = Messages.REL_DETALHES_CALCULOS_LABEL_VLFECOPRECOLHER;
				return vlSt + " = (" + baseCalculoRetido + " + " + vlFrete + " + " + pctIpi + " - " + pctRepasse + " + " + margemAgregada + " - " + pctReducao + ") * (" + aliquotaRetido + " + " + aliquotaFecop + ") - " + vlIcms + " - " + vlFecopRecolher;
			} else {
				return vlSt + " = (" + baseCalculoRetido + " + " + vlFrete + " + " + pctIpi + " - " + pctRepasse + " + " + margemAgregada + " - " + pctReducao + ") * (" + aliquotaRetido + ") - " + vlIcms; 
			}
		} else {
			return "Sem Tributação";
		}
	}
	
	private String getFormulaIndiceRentabilidadeItem() {
		String vlIndiceRentabilidadeItem = Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_ITEM;
		String margemPercentual = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_PERCENTUAL;
		String margemMinima = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_MINIMA;
		String margemEspecifica = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA;
		return vlIndiceRentabilidadeItem + " = (" + margemPercentual + " * " + margemMinima + " / " + margemEspecifica + ") * 100"; 
	}
	
	private String getFormulaMargemPercentualItem() {
		String margemPercentualItem = Messages.REL_DETALHES_CALCULOS_MARGEM_PERCENTUAL_ITEM;
		String margemValor = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_VALOR;
		String receitaVirtual = Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_VIRTUAL;
		return margemPercentualItem + " = " + margemValor + " / " + receitaVirtual;
	}
	
	private String getFormulaMargemValorItem() {
		String margemValorItem = Messages.REL_DETALHES_CALCULOS_MARGEM_VALOR_ITEM;
		String receitaLiquida = Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_LIQUIDA;
		String custo = Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO;
		String verbaEmpresa = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA;
		String verbaGerada = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA;
		String verbaUsada = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_USADA;
		return margemValorItem + " = " + receitaLiquida + " - " + custo + " + " + verbaEmpresa + " - " + verbaGerada + " + " + verbaUsada;
	}
	
	private String getFormulaReceitaVirtualItem() {
		StringBuffer receitaVirtual = new StringBuffer();
		receitaVirtual.append(Messages.REL_DETALHES_CALCULOS_RECEITA_VIRTUAL_ITEM);
		String valorVenda = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA;
		String verbaEmpresa = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA;
		String verbaGerada = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA;
		String verbaUsada = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_USADA;
		String aliquotaPis = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS;
		String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			String valorIcms = Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
			String valorSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
			receitaVirtual.append(" = (");
			receitaVirtual.append(valorVenda);
			receitaVirtual.append(" * (1 - ");
			receitaVirtual.append(aliquotaPis);
			receitaVirtual.append(") + ");
			receitaVirtual.append(verbaUsada);
			receitaVirtual.append(" + ");
			receitaVirtual.append(verbaEmpresa);
			receitaVirtual.append(" - ");
			receitaVirtual.append(verbaGerada);
			receitaVirtual.append(" - ");
			receitaVirtual.append(valorIcms);
			receitaVirtual.append(" - ");
			receitaVirtual.append(valorSt);
			receitaVirtual.append(") / (1 - (");
			receitaVirtual.append(aliquotaPis);
			receitaVirtual.append(" + ");
			receitaVirtual.append(aliquotaIcms);
			receitaVirtual.append("))");
		} else {
			receitaVirtual.append(" = (");
			receitaVirtual.append(valorVenda);
			receitaVirtual.append(" + ");
			receitaVirtual.append(verbaUsada);
			receitaVirtual.append(" + ");
			receitaVirtual.append(verbaEmpresa);
			receitaVirtual.append(" - ");
			receitaVirtual.append(verbaGerada);
			receitaVirtual.append(") / (1 - (");
			receitaVirtual.append(aliquotaPis);
			receitaVirtual.append(" + ");
			receitaVirtual.append(aliquotaIcms);
			receitaVirtual.append(" ))");
		}
		return receitaVirtual.toString();
	}
	
	private String getFormulaVerbaGerada() {
		StringBuffer verbaGerada = new StringBuffer();
		verbaGerada.append(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_GERADA);
		String valorVenda = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA;
		String valorTabela = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_TABELA;
		String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
		String aliquotaPis = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS;
		verbaGerada.append(" = (");
		verbaGerada.append(valorVenda);
		verbaGerada.append(" - ");
		verbaGerada.append(valorTabela);
		verbaGerada.append(" ) * (1 - ");
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			verbaGerada.append(aliquotaPis);
			verbaGerada.append(")");
		} else {
			verbaGerada.append(" ( ");
			verbaGerada.append(aliquotaPis);
			verbaGerada.append(" + ");
			verbaGerada.append(aliquotaIcms);
			verbaGerada.append(" )");
		}
		return verbaGerada.toString();
	}
	
	private String getFormulaVerbaNecessaria() {
		StringBuffer verbaNecessaria = new StringBuffer();
		verbaNecessaria.append(Messages.REL_DETALHES_CALCULOS_VERBA_NECESSARIA);
		
		String valorVenda = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_VENDA;
		String valorNeutro = Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO;
		String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
		String aliquotaPis = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS;
		String verbaEmpresa = Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA;
		verbaNecessaria.append(" = (");
		verbaNecessaria.append(valorNeutro);
		verbaNecessaria.append(" - ");
		verbaNecessaria.append(valorVenda);
		verbaNecessaria.append(") * (1 -");
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			verbaNecessaria.append(aliquotaPis);
			verbaNecessaria.append(") -");
			verbaNecessaria.append(verbaEmpresa);
		} else {
			verbaNecessaria.append(" (");
			verbaNecessaria.append(aliquotaPis);
			verbaNecessaria.append(" + ");
			verbaNecessaria.append(aliquotaIcms);
			verbaNecessaria.append(")) -");
			verbaNecessaria.append(verbaEmpresa);
		}
		return verbaNecessaria.toString();
	}
	
	private String getFormulaValorNeutro() {
		String valorNeutro = Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO;
		String custo = Messages.REL_DETALHES_CALCULOS_LABEL_CUSTO;
		String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
		String aliquotaPis = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS;
		String margemEspecifica = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA;
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			String valorIcms = Messages.REL_DETALHES_CALCULOS_LABEL_VLICMS;
			String valorSt = Messages.REL_DETALHES_CALCULOS_LABEL_VLST;
			return valorNeutro + " = (" + custo + " * (1 - (" + aliquotaIcms + " + " + aliquotaPis + ")) / (1 - (" + aliquotaIcms + " + " + aliquotaPis + " + " + margemEspecifica + ")) + " + valorIcms + " + " + valorSt + ") / (1 - (" + aliquotaPis + "))";   
		} else {
			return valorNeutro + " = " + custo + " / (1 - (" + aliquotaIcms + " + " + aliquotaPis + " + " + margemEspecifica + "))";
		}
	}
	
	@Override
	protected void onPopup() {
	   try {
		super.onPopup();
		loadValoresCalculoRentabilidade();
		loadValoresCalculoPisCofins();
		loadValoresCalculoIpi();
		loadValoresCalculoIcms();
		loadValoresCalculoSt();
		loadValoresCalculoIndiceRentabilidadeItem();
		loadValoresCalculoMargemPercentualItem();
		loadValoresCalculoMargemValorItem();
		loadValoresCalculoReceitaVirtualItem();
		loadValoresCalculoVerbaGeradaItem();
		loadValoresCalculoVerbaNecessaria();
		loadValoresCalculoValorNeutro();
		loadValoresVerbaEmpresa();
		} catch (Throwable ee) {ExceptionUtil.handle(ee);}
	}
	
	private void loadValoresVerbaEmpresa() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvValorNeutroCalcVerbaEmpresa.setValue(itemPedido.getItemPedidoAud().vlItemPedidoNeutro);
			lvAliquotaIcmsCalcVerbaEmpresa.setValue(tributacao.vlPctIcms / 100);
			lvAliquotaPisCalcVerbaEmpresa.setValue(tributacao.vlPctPis / 100);
			lvVlTabelaVerbaEmpresa.setValue(itemPedido.vlBaseItemPedido);
			lvCalcVerbaEmpresa.setValue(itemPedido.getItemPedidoAud().vlVerbaEmpresa);
		}
	}
	
	private void loadValoresCalculoRentabilidade() throws SQLException {
		if (!LavenderePdaConfig.isUsaRentabilidadeNoPedido()) return; 
		
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 3) {
			setaTotalizadoresDeRentabilidade();
		}
		switch (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido) {
		case 5:
			loadValoresCalculoRentabilidadeTipo5();
			break;
		case 6:
			loadValoresCalculoRentabilidade(itemPedido.vlTotalItemPedido);
			break;
		case 7:
			loadValoresCalculoRentabilidade(itemPedido.getVlTotalItemBruto());
			break;
		case 8:
			loadValoresCalculoRentabilidade8();
			break;
		default:
			loadValoresCalculoRentabilidadeDemaisTipos();
		}

	}

	private void setaTotalizadoresDeRentabilidade() throws SQLException {
		lvPrecoTotalComImpostos.setValue(itemPedido.getVlTotalItemComTributos());
		lvGastoTotalVariavel.setValue(itemPedido.getVlGastoTotalVariavel());
		lvCustoTotalFixo.setValue(itemPedido.getVlTotalCustoFixo());
		lvVlTotalPis.setValue(itemPedido.getVlTotalPis());
		lvVlTotalCofins.setValue(itemPedido.getVlTotalCofins());
		lvVlTotalIcms.setValue(itemPedido.getVlTotalIcms());
		lvVlTotalSt.setValue(itemPedido.getVlTotalST());
		lvVlTotalIpi.setValue(itemPedido.getVlTotalIpi());
		lvVlTotalIrpj.setValue(itemPedido.getVlTotalIrpj());
		lvVlTotalCsll.setValue(itemPedido.getVlTotalCSLL());
		lvVlTotalCpp.setValue(itemPedido.getVlTotalCPP());
		lvVlTotalCustoVariavelProduto.setValue(itemPedido.getVlTotalCustoVariavelProduto());
		lvVlTotalCustoVariavel.setValue(itemPedido.getVlTotalCustoVariavel());
		lvVlGastoTotalVariavel.setValue(itemPedido.getVlGastoTotalVariavel());
	}

	private void loadValoresCalculoRentabilidadeTipo5() throws SQLException {
		lvPctRentabilidade.setValue(itemPedido.vlRentabilidade);
		lvVlVenda.setValue(itemPedido.vlItemPedido);
		double qtItemFisico = itemPedido.getQtItemFisico() == 0 ? 1 : itemPedido.getQtItemFisico();
		lvPrecoTotalCusto.setValue(ItemPedidoService.getInstance().getVlPrecoTotalCustoItem(pedido, itemPedido) / qtItemFisico);
	}
	
	private void loadValoresCalculoRentabilidade(double vlVenda) throws SQLException {
		lvPctRentabilidade.setValue(itemPedido.vlRentabilidade);
		lvVlVenda.setValue(vlVenda);
		lvPrecoTotalCusto.setValue(ItemPedidoService.getInstance().getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido));
	}
	
	private void loadValoresCalculoRentabilidade8() throws SQLException {
		CondicaoPagamento condpag = pedido.getCondicaoPagamento();	
		lvVlImposto.setValue(itemPedido.getSomaValoresTribPersonalizada());
		lvVlRetorno.setValue(itemPedido.vlRetornoProduto);
		lvVlCustoFinanceiro.setValue(condpag.vlPctCustoFinanceiro);
		lvVlCustosVariaveis.setValue(getVlCustosVariaveis());
		lvVlRentabilidade.setValue(itemPedido.vlRentabilidade);
		lvPrecoTotalVenda.setValue(itemPedido.vlTotalItemPedido);
		lvPrecoTotalCusto.setValue(ItemPedidoService.getInstance().getVlPrecoTotalCustoItem(pedido, itemPedido));
	}
	
	private void loadValoresCalculoRentabilidadeDemaisTipos() throws SQLException {
		lvVlRentabilidade.setValue(itemPedido.vlRentabilidade);
		lvPrecoTotalVenda.setValue(itemPedido.vlTotalItemPedido);
		lvPrecoTotalCusto.setValue(ItemPedidoService.getInstance().getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido));
	}
	
	private void loadValoresCalculoPisCofins() throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			lvTotalItemPedidoPis.setValue(itemPedido.vlTotalItemPedido);
			lvSeguroItemPedidoPis.setValue(itemPedido.getVlTotalSeguro());
			lvTotalItemPedidoFretePis.setValue(itemPedido.vlTotalItemPedidoFrete);
			lvPctPis.setValue(tributacao != null && tributacao.vlPctPis != 0 ? tributacao.vlPctPis : itemPedido.getProduto().vlPctPis);
			lvTotalItemPedidoCofins.setValue(itemPedido.vlTotalItemPedido);
			lvTotalItemPedidoFreteCofins.setValue(itemPedido.vlTotalItemPedidoFrete);
			lvSeguroItemPedidoCofins.setValue(itemPedido.getVlTotalSeguro());
			lvPctCofins.setValue(tributacao != null && tributacao.vlPctCofins != 0 ? tributacao.vlPctCofins : itemPedido.getProduto().vlPctCofins);
			lvIndiceFinanceiroCondPagtoCofins.setValue(itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro);
			lvIndiceFinanceiroCondPagtoPis.setValue(itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro);
		}
	}
	
	private void loadValoresCalculoIpi() throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			lvTotalItemPedidoIpi.setValue(itemPedido.vlTotalItemPedido);
			lvSeguroItemPedidoIpi.setValue(itemPedido.getVlTotalSeguro());
			lvTotalItemPedidoFreteIpi.setValue(itemPedido.vlTotalItemPedidoFrete);
			lvPisNoIpi.setValue(itemPedido.vlTotalPisItem);
			lvCofinsNoIpi.setValue(itemPedido.vlTotalCofinsItem);
			lvPctIpi.setValue(tributacao != null && tributacao.vlPctIpi != 0 ? tributacao.vlPctIpi : itemPedido.getProduto().vlPctIpi);
			lvIndiceFinanceiroCondPagtoIpi.setValue(itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro);
		}
	}
	
	private void loadValoresCalculoIcms() throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			lvValorIcms.setValue(tributacao.vlIcms);
			lvQtdItemPedidoIcms.setValue(itemPedido.getQtItemFisico());
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			lvBaseIcms.setValue(tributacaoVlBase.vlBaseIcms);
			lvQtdItemPedidoIcms.setValue(itemPedido.getQtItemFisico());
			lvTotalItemPedidoIcms.setValue(itemPedido.vlTotalItemPedido);
			lvSeguroItemPedidoIcms.setValue(itemPedido.getVlTotalSeguro());
			lvTotalItemPedidoFreteIcms.setValue(itemPedido.vlTotalItemPedidoFrete);
			lvPisNoIcms.setValue(itemPedido.vlTotalPisItem);
			lvCofinsNoIcms.setValue(itemPedido.vlTotalCofinsItem);
			lvIpiNoIcms.setValue(itemPedido.vlTotalIpiItem);
			lvPctIcms.setValue(STService.getInstance().getPctIcmsParaCalculoPersonalizado(itemPedido, tributacao));
			lvIndiceFinanceiroCondPagtoIcms.setValue(itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro);
		} else if (LavenderePdaConfig.isUsaCalculaStItemPedido()) {
			if (tributacao != null) {
				double vlBaseCalculoIcms = STService.getInstance().getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, tributacaoVlBase.isExistePrecoBaseIcmsCalcRetido());
				double vlIcmsNormal = vlBaseCalculoIcms;
				lvBaseCalculoIcms.setValue(vlBaseCalculoIcms);
				if (!tributacaoVlBase.isExistePrecoBaseIcmsCalcRetido()) {
					if (tributacao.isAplicaFrete()) {
						double vlFrete = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(itemPedido.vlItemPedidoFrete, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : itemPedido.vlItemPedidoFrete;
						lvVlFreteCalcIcms.setValue(vlFrete);
						vlIcmsNormal += vlFrete;
					} else {
						lvVlFreteCalcIcms.setValue(0d);
					}
					double vlDespesaAcessoria = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(tributacao.vlDespesaAcessoria, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : tributacao.vlDespesaAcessoria;
					lvVlDespesasAcessorias.setValue(vlDespesaAcessoria);
					vlIcmsNormal += vlDespesaAcessoria;
					if (tributacao.isAplicaIpiNoIcms()) {
						double vlPctIpi = STService.getInstance().calculaIpi(tributacao, itemPedido, vlIcmsNormal);
						vlIcmsNormal += vlPctIpi;
						lvPctIpiCalcIcms.setValue(vlPctIpi);
					} else {
						lvPctIpiCalcIcms.setValue(0d);
					}
					double vlPctRepasse = STService.getInstance().calculaRepasseIcms(vlIcmsNormal, tributacao.vlPctRepasseIcms);
					lvPctRepasseCalcIcms.setValue(vlPctRepasse);
					vlIcmsNormal -= vlPctRepasse;
					double vlPctReducao = STService.getInstance().calculaReducaoBaseIcms(vlIcmsNormal, tributacao.vlPctReducaoBaseIcms);
					lvPctReducaoCalcIcms.setValue(vlPctReducao);
					vlIcmsNormal -= vlPctReducao;
				}
				double vlAliquotaIcms = STService.getInstance().calculaAliquota(vlIcmsNormal, tributacao.vlPctIcms);
				lvAliquotaIcms.setValue(tributacao.vlPctIcms / 100);
				vlIcmsNormal = vlAliquotaIcms;
				if (!tributacaoVlBase.isExistePrecoBaseIcmsCalcRetido()) {
					double vlReducaoIcms = STService.getInstance().calculaReducaoIcms(vlIcmsNormal, tributacao.vlPctReducaoIcms);
					lvPctReducaoIcms.setValue(vlReducaoIcms);
					vlIcmsNormal -= vlReducaoIcms;
				} else {
					lvPctReducaoIcms.setValue(0d);
				}
				lvVlIcmsCalcIcms.setValue(itemPedido.vlIcms);
			}
		}
	}
	
	private void loadValoresCalculoSt() throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
			lvSTRetido.setValue(tributacao.vlIcmsRetido);
			lvQtdItemPedidoSt.setValue(itemPedido.getQtItemFisico());
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			lvBaseSt.setValue(STService.getInstance().getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacaoVlBase));
			lvPercentualReducaoSt.setValue(tributacao.vlPctReducaoBaseIcmsRetido);
			lvQtdItemPedidoSt.setValue(itemPedido.getQtItemFisico());
			lvTotalItemPedidoSt.setValue(itemPedido.vlTotalItemPedido);
			lvSeguroItemPedidoSt.setValue(itemPedido.getVlTotalSeguro());
			lvTotalItemPedidoFreteSt.setValue(itemPedido.vlTotalItemPedidoFrete);
			lvIpiNaSt.setValue(itemPedido.vlTotalIpiItem);
			lvPctSt.setValue(tributacao.vlPctIcmsRetido);
			lvIcmsNaSt.setValue(itemPedido.vlTotalIcmsItem);
			lvIndiceFinanceiroCondPagtoSt.setValue(itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro);
		} else if (LavenderePdaConfig.isUsaCalculaStItemPedido() && tributacao != null) {
			double vlBaseIcmsRetidoCalcRetido = STService.getInstance().getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido());
			double vlIcmsFinal = vlBaseIcmsRetidoCalcRetido;
			double vlFrete = 0d;
			double vlIpiParaCalculo = 0d;
			lvBaseCalculoRetido.setValue(vlBaseIcmsRetidoCalcRetido);
			if (tributacao.isAplicaFrete() && !tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido()) {
	    		vlFrete = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(itemPedido.vlItemPedidoFrete, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : itemPedido.vlItemPedidoFrete;
	    		vlIcmsFinal += vlFrete;
	    		lvVlFreteCalcSt.setValue(vlFrete);
	    	} else {
	    		lvVlFreteCalcSt.setValue(0d);
	    	}
			if (tributacao.isAplicaIpiNoIcmsRetido() && !tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido()) {
				vlIpiParaCalculo = STService.getInstance().calculaIpi(tributacao, itemPedido, vlIcmsFinal);
				vlIcmsFinal += vlIpiParaCalculo;
				lvPctIpiCalcSt.setValue(vlIpiParaCalculo);
			} else {
				lvPctIpiCalcSt.setValue(0d);
			}
			if (tributacao.isBaseIcmsRetidoComRepasse()) {
				double vlRepasseSt = STService.getInstance().calculaRepasseIcms(vlIcmsFinal, tributacao.vlPctRepasseIcms);
	    		vlIcmsFinal -= vlRepasseSt;
	    		lvPctRepasseCalcSt.setValue(vlRepasseSt);
	    	} else {
	    		lvPctRepasseCalcSt.setValue(0d);
	    	}
			double vlMargemAgregada = STService.getInstance().calculaMargemAgregada(vlIcmsFinal, tributacao.vlPctMargemAgregada);
			vlIcmsFinal += vlMargemAgregada;
			lvMargemAgregada.setValue(vlMargemAgregada);
			double vlPctReducaoSt = STService.getInstance().calculaReducaoBaseIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctReducaoBaseIcmsRetido);
			vlIcmsFinal -= vlPctReducaoSt;
			lvPctReducaoCalcSt.setValue(vlPctReducaoSt);
			lvAliquotaRetido.setValue(tributacao.vlPctIcmsRetido / 100);
			if (tributacao.isPossuiFundoDePobreza()) {
	    		lvAliquotaFecop.setValue(tributacao.vlPctFecop / 100);
				if (tributacao.cdTipoFecop != 0) {
					lvVlFecopRecolher.setValue(FecopService.getInstance().aplicaFecopNoItemPedido(itemPedido, tributacao, tributacaoVlBase));
				} else {
					lvVlFecopRecolher.setValue(FecopService.getInstance().calculaValorFecopARecolher(vlBaseIcmsRetidoCalcRetido, tributacao, itemPedido.vlIcms, vlFrete, tributacao.vlPctMargemAgregada, tributacao.vlPctIcmsRetido, vlIpiParaCalculo, true));
				}
	    	} else {
				if (tributacao.cdTipoFecop != 0) {
					lvAliquotaFecop.setValue(tributacao.vlPctFecop / 100);
					lvVlFecopRecolher.setValue(FecopService.getInstance().aplicaFecopNoItemPedido(itemPedido, tributacao, tributacaoVlBase));
				} else {
					lvAliquotaFecop.setValue(0d);
					lvVlFecopRecolher.setValue(0d);
				}
	    	}
			if (tributacao.vlPctOutorga != 0) {
				lvVlIcmsCalcSt.setValue(STService.getInstance().calculaIcmsOutorgado(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctIcms, tributacao.vlPctOutorga));
			} else {
				lvVlIcmsCalcSt.setValue(itemPedido.vlIcms);
			}
			lvVlSt.setValue(itemPedido.vlSt);
			lvVlStRentabilidade.setValue(itemPedido.vlSt);
			
		}
	}
	
	private void loadValoresCalculoIndiceRentabilidadeItem() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvMargemPercentual.setValue(itemPedido.getItemPedidoAud().vlPctMargemItem);
			lvMargemMinima.setValue((double) LavenderePdaConfig.indiceMinimoRentabilidadePedido);
			lvMargemEspecifica.setValue(itemPedido.getItemPedidoAud().vlPctMargemRentabilidade);
			lvVlIndiceRentabilidadeItem.setValue(itemPedido.vlRentabilidade);
		}
	}
	
	private void loadValoresCalculoMargemPercentualItem() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvMargemValor.setValue(itemPedido.getItemPedidoAud().vlMargemItem);
			lvReceitaVirtual.setValue(itemPedido.getItemPedidoAud().vlReceitaVirtual);
			lvVlMargemPercentual.setValue(itemPedido.getItemPedidoAud().vlPctMargemItem);
		}
	}
	
	private void loadValoresCalculoMargemValorItem() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvReceitaLiquida.setValue(itemPedido.getItemPedidoAud().vlReceitaLiquida);
			lvCusto.setValue(itemPedido.vlBaseFlex);
			lvVerbaEmpresa.setValue(itemPedido.getItemPedidoAud().vlVerbaEmpresa);
			lvVerbaGerada.setValue(itemPedido.vlVerbaItemPositivo);
			lvVerbaUsada.setValue(itemPedido.vlVerbaItem);
			lvVlMargemValor.setValue(itemPedido.getItemPedidoAud().vlMargemItem);
		}
	}
	
	private void loadValoresCalculoReceitaVirtualItem() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvValorVenda.setValue(itemPedido.vlItemPedido);
			lvAliquotaPis.setValue(tributacao.vlPctPis / 100);
			lvAliquotaIcmsCalcRecVirtual.setValue(tributacao.vlPctIcms / 100);
			if (tributacao != null) {
				lvVlIcmsCalcRevVirtual.setValue(tributacao.vlIcms);
				lvVlStCalcRecVirtual.setValue(tributacao.vlIcmsRetido);
			}
			lvVerbaGeradaCalcRecVirtual.setValue(itemPedido.vlVerbaItemPositivo);
			lvVerbaEmpresaCalcRecVirtual.setValue(itemPedido.getItemPedidoAud().vlVerbaEmpresa);
			lvVerbaUsadaCalcRecVirtual.setValue(itemPedido.getVlVerbaManual());
			lvVlReceitaVirtual.setValue(itemPedido.getItemPedidoAud().vlReceitaVirtual);
		}
	}
	
	private void loadValoresCalculoVerbaGeradaItem() {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvAliquotaIcmsCalcVerbaGerada.setValue(tributacao.vlPctIcms / 100);
			lvAliquotaPisCalcVerbaGerada.setValue(tributacao.vlPctPis / 100);
			lvVlVenda.setValue(itemPedido.vlItemPedido);
			lvVlTabela.setValue(itemPedido.vlBaseItemPedido);
			lvVlVerbaGerada.setValue(itemPedido.vlVerbaItemPositivo);
		}
	}
	
	private void loadValoresCalculoVerbaNecessaria() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvValorVendaCalcVerbaNecessaria.setValue(itemPedido.vlItemPedido);
			lvValorNeutroCalcVerbaNecessaria.setValue(itemPedido.getItemPedidoAud().vlItemPedidoNeutro);
			lvAliquotaIcmsCalcVerbaNecessaria.setValue(tributacao.vlPctIcms / 100);
			lvAliquotaPisCalcVerbaNecessaria.setValue(tributacao.vlPctPis / 100);
			lvVerbaEmpresaCalcVerbaNecessaria.setValue(itemPedido.getItemPedidoAud().vlVerbaEmpresa);
			lvVlVerbaNecessaria.setValue(itemPedido.getItemPedidoAud().vlVerbaNecessaria);
		}
	}
	
	private void loadValoresCalculoValorNeutro() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvCustoCalcValorNeutro.setValue(itemPedido.vlBaseFlex);
			lvAliquotaIcmsCalcValorNeutro.setValue(itemPedido.getItemPedidoAud().vlPctIcms / 100);
			lvAliquotaPisCalcValorNeutro.setValue(itemPedido.getItemPedidoAud().vlPctPis / 100);
			lvMargemEspecificaCalcValorNeutro.setValue(itemPedido.getItemPedidoAud().vlPctMargemRentabilidade / 100);
			lvValorIcmsCalcValorNeutro.setValue(itemPedido.vlIcms);
			lvValorStCalcValorNeutro.setValue(itemPedido.vlSt);
			lvVlValorNeutro.setValue(itemPedido.getItemPedidoAud().vlItemPedidoNeutro);
		}
	}
	
	private void adicionaComponentesPis(Container tabPis) {
		UiUtil.add(tabPis, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabPis, formulaPisContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaPisContainer, lbFormulaPis, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaPis.setMultipleLinesText(getFormulaPis());
		formulaPisContainer.resizeHeight();
		formulaPisContainer.resetSetPositions();
		formulaPisContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaPisContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabPis, lbTotalItemPedidoPis, lvTotalItemPedidoPis, getLeft(), AFTER + HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBasePIS()) {
			UiUtil.add(tabPis, lbIndiceFinanceiroCondPagtoPis, lvIndiceFinanceiroCondPagtoPis, SAME, AFTER + HEIGHT_GAP);
		}
		if (tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
			UiUtil.add(tabPis, lbTotalItemPedidoFretePis, lvTotalItemPedidoFretePis, SAME, AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			UiUtil.add(tabPis, lbSeguroItemPedidoPis, lvSeguroItemPedidoPis, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabPis, lbPctPis, lvPctPis, SAME, AFTER + HEIGHT_GAP);
	}
	
	private String getFormulaPis() {
		String formulaPis = Messages.TOOLTIP_LABEL_VLTOTALITEM;
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBasePIS()) {
			formulaPis = "(" + formulaPis + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido() || tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
			formulaPis = "(" + formulaPis;
			if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
				formulaPis += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO;
			}
			if (tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
				formulaPis += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE;
			}
			formulaPis += ")";
		}
		return Messages.REL_DETALHES_CALCULOS_LABEL_VLIPIS + " = " + formulaPis + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTPIS;  
	}
	
	private void adicionaComponentesCofins(Container tabCofins) {
		UiUtil.add(tabCofins, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabCofins, formulaCofinsContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaCofinsContainer, lbFormulaCofins, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaCofins.setMultipleLinesText(getFormulaCofins());
		formulaCofinsContainer.resizeHeight();
		formulaCofinsContainer.resetSetPositions();
		formulaCofinsContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaCofinsContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabCofins, lbTotalItemPedidoCofins, lvTotalItemPedidoCofins, getLeft(), AFTER + HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseCOFINS()) {
			UiUtil.add(tabCofins, lbIndiceFinanceiroCondPagtoCofins, lvIndiceFinanceiroCondPagtoCofins, SAME, AFTER + HEIGHT_GAP);
		}
		if (tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
			UiUtil.add(tabCofins, lbTotalItemPedidoFreteCofins, lvTotalItemPedidoFreteCofins, SAME, AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			UiUtil.add(tabCofins, lbSeguroItemPedidoCofins, lvSeguroItemPedidoCofins, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabCofins, lbPctCofins, lvPctCofins, SAME, AFTER + HEIGHT_GAP);
	}
	
	private String getFormulaCofins() {
		String formulaCofins = Messages.TOOLTIP_LABEL_VLTOTALITEM;
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBasePIS()) {
			formulaCofins = "(" + formulaCofins + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido() || tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
			formulaCofins = "(" + formulaCofins;
			if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
				formulaCofins += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO;
			}
			if (tributacaoConfig.isAplicaValorFreteNaBasePisCofins()) {
				formulaCofins += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE;
			}
			formulaCofins += ")";
		}
		return Messages.REL_DETALHES_CALCULOS_LABEL_VLICOFINS + " = " + formulaCofins + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTCOFINS;  
	}
	
	private void adicionaComponentesVerbaEmpresa(Container tabVerbaEmpresa) {
		UiUtil.add(tabVerbaEmpresa, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabVerbaEmpresa, formulaVerbaEmpresaContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaVerbaEmpresaContainer, lbFormulaVerbaEmpresa, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaVerbaEmpresa.setMultipleLinesText(getFormulaVerbaEmpresa());
		formulaVerbaEmpresaContainer.resizeHeight();
		formulaVerbaEmpresaContainer.resetSetPositions();
		formulaVerbaEmpresaContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaVerbaEmpresaContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabVerbaEmpresa, lbValorNeutroCalcVerbaEmpresa, lvValorNeutroCalcVerbaEmpresa, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabVerbaEmpresa, lbVlTabelaVerbaEmpresa, lvVlTabelaVerbaEmpresa, SAME, AFTER + HEIGHT_GAP);
		if (tributacao == null || tributacao.vlIcmsRetido == 0) {
			UiUtil.add(tabVerbaEmpresa, lbAliquotaIcmsCalcVerbaEmpresa, lvAliquotaIcmsCalcVerbaEmpresa, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabVerbaEmpresa, lbAliquotaPisCalcVerbaEmpresa, lvAliquotaPisCalcVerbaEmpresa, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabVerbaEmpresa, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabVerbaEmpresa, lbCalcVerbaEmpresa, lvCalcVerbaEmpresa, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesIpi(Container tabIpi) throws SQLException {
		UiUtil.add(tabIpi, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabIpi, formulaIpiContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaIpiContainer, lbFormulaIpi, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaIpi.setMultipleLinesText(getFormulaIpi());
		formulaIpiContainer.resizeHeight();
		formulaIpiContainer.resetSetPositions();
		formulaIpiContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaIpiContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabIpi, lbTotalItemPedidoIpi, lvTotalItemPedidoIpi, getLeft(), AFTER + HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseIPI()) {
			UiUtil.add(tabIpi, lbIndiceFinanceiroCondPagtoIpi, lvIndiceFinanceiroCondPagtoIpi, SAME, AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			UiUtil.add(tabIpi, lbSeguroItemPedidoIpi, lvSeguroItemPedidoIpi, SAME, AFTER + HEIGHT_GAP);
		}
		if (tributacaoConfig.isAplicaValorFreteNaBaseIpi()) {
			UiUtil.add(tabIpi, lbTotalItemPedidoFreteIpi, lvTotalItemPedidoFreteIpi, SAME, AFTER + HEIGHT_GAP);
		}
		if (tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
			UiUtil.add(tabIpi, lbPisNoIpi, lvPisNoIpi, SAME, AFTER + HEIGHT_GAP);
			UiUtil.add(tabIpi, lbCofinsNoIpi, lvCofinsNoIpi, SAME, AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabIpi, lbPctIpi, lvPctIpi, SAME, AFTER + HEIGHT_GAP);
	}
	
	private String getFormulaVerbaEmpresa() {
		StringBuffer verbaEmpresa = new StringBuffer();
		verbaEmpresa.append(Messages.REL_DETALHES_CALCULOS_LABEL_VERBA_EMPRESA);
		String valorTabela = Messages.REL_DETALHES_CALCULOS_LABEL_VALOR_TABELA;
		String aliquotaIcms = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTAICMS;
		String aliquotaPis = Messages.REL_DETALHES_CALCULOS_LABEL_ALIQUOTA_PIS;
		String valorNeutro = Messages.REL_DETALHES_CALCULOS_VALOR_NEUTRO;
		verbaEmpresa.append(" = (");
		verbaEmpresa.append(valorNeutro);
		verbaEmpresa.append(" - ");
		verbaEmpresa.append(valorTabela);
		verbaEmpresa.append(") * (1 -");
		if (tributacao != null && tributacao.vlIcmsRetido != 0) {
			verbaEmpresa.append(aliquotaPis);
			verbaEmpresa.append(" / 100)");
		} else {
			verbaEmpresa.append(" (");
			verbaEmpresa.append(aliquotaPis);
			verbaEmpresa.append(" + ");
			verbaEmpresa.append(aliquotaIcms);
			verbaEmpresa.append("))");
		}
		return verbaEmpresa.toString();
	}
	
	private String getFormulaIpi() throws SQLException {
		String formulaIpi = Messages.TOOLTIP_LABEL_VLTOTALITEM;
		if (LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseIPI()) {
			formulaIpi = "(" + formulaIpi + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_INDICE_FINANCEIRO + ")";
		}
		boolean calcSeguroItemPedido = LavenderePdaConfig.isCalculaSeguroNoItemPedido();
		boolean aplicaVlFreteBase = tributacaoConfig.isAplicaValorFreteNaBasePisCofins();
		boolean calcDebPisCofinsZonaF = tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca();
		if (calcSeguroItemPedido || aplicaVlFreteBase || calcDebPisCofinsZonaF) {
			formulaIpi = "(" + formulaIpi;
			if (calcSeguroItemPedido) {
				formulaIpi += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALSEGURO;
			}
			if (aplicaVlFreteBase) {
				formulaIpi += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALFRETE;
			}
			if (calcDebPisCofinsZonaF) {
				formulaIpi += " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALPIS + " + " + Messages.REL_DETALHES_CALCULOS_LABEL_VLTOTALCOFINS;
			}
			formulaIpi += ")";
		}
		return Messages.REL_DETALHES_CALCULOS_LABEL_VLIPI + " = " + formulaIpi + " * " + Messages.REL_DETALHES_CALCULOS_LABEL_PCTIPI;   
	}
	
	private double getVlCustosVariaveis() throws SQLException {
		ProdutoCliente produtoClienteFilter = new ProdutoCliente();
		produtoClienteFilter.cdEmpresa = pedido.cdEmpresa;
		produtoClienteFilter.cdRepresentante =  pedido.cdRepresentante;
		produtoClienteFilter.cdProduto = itemPedido.cdProduto;
		produtoClienteFilter.cdCliente =  pedido.cdCliente;
		
		ProdutoCliente produtoCliente = (ProdutoCliente) ProdutoClienteService.getInstance().findByRowKey(produtoClienteFilter.getPrimaryKey());
		
		double vlPctDiretoria = itemPedido.getItemTabelaPreco() == null ? 0 : itemPedido.getItemTabelaPreco().vlPctDiretoria;
		double vlPctComissao =  pedido.getEmpresa() == null ? 0 : pedido.getEmpresa().vlPctComissao;
		double vlPctFidelidade = 0;
		double vlPctRoyalt = 0;
		if (produtoCliente != null) {
			vlPctFidelidade = produtoCliente.vlPctFidelidade;
			vlPctRoyalt =  produtoCliente.vlPctRoyalt;	
		}
		return vlPctDiretoria + vlPctComissao + vlPctFidelidade + vlPctRoyalt;
	}

}
