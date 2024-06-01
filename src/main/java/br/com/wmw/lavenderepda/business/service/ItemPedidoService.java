package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import br.com.wmw.lavenderepda.business.domain.CondPagtoLinha;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.DescProgQtd;
import br.com.wmw.lavenderepda.business.domain.DescProgressivo;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DescQuantPesoPrdBlq;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import br.com.wmw.lavenderepda.business.domain.DescVidaUtilGrupo;
import br.com.wmw.lavenderepda.business.domain.DescontoComissao;
import br.com.wmw.lavenderepda.business.domain.DescontoIcms;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto2;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto3;
import br.com.wmw.lavenderepda.business.domain.HistoricoItem;
import br.com.wmw.lavenderepda.business.domain.IcmsCliente;
import br.com.wmw.lavenderepda.business.domain.IndiceGrupoProd;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import br.com.wmw.lavenderepda.business.domain.MargemRentab;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliFin;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercial;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercialFaixa;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import br.com.wmw.lavenderepda.business.domain.PrazoEntrega;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoMargem;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.domain.TabPrecoClasseProd;
import br.com.wmw.lavenderepda.business.domain.TabPrecoGrupoProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoRep;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoFreteTabPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TipoRegistro;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoDTO;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService.EmbalagensResultantes;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.business.validation.ItemKitPedidoInseridoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoSemPrecoException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosRentabilidadeSemAlcadaException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMinPedidoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListLimiteAvisoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListProdutoPrecoErroWindow;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoApp2WebRunnable;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.json.JSONArray;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;


public class ItemPedidoService extends CrudService {

	private static ItemPedidoService instance;
	
	private StringBuffer strBuffer = new StringBuffer();
	public boolean emTransacao;

	public ItemPedidoService() {
		//--
	}

	public static ItemPedidoService getInstance() {
		if (ItemPedidoService.instance == null) {
			ItemPedidoService.instance = new ItemPedidoService();
		}
		return ItemPedidoService.instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return ItemPedidoPdbxDao.getInstance();
	}

	public ItemPedidoPdbxDao getItemPedidoPdbxDao() {
		return (ItemPedidoPdbxDao) getCrudDao();
	}

	public Vector findAllByExampleUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedido) domain).flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().findAllByExampleUnique(domain);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().findAllByExampleUnique(domain);
		}
	}

	public Vector findAllByExampleSummaryUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedido) domain).flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().findAllByExampleSummaryUnique(domain);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().findAllByExampleSummaryUnique(domain);
		}
	}
	
	public BaseDomain findByRowKeyErp(final BaseDomain domain) throws SQLException {
		return ItemPedidoPdbxDao.getInstanceErp().findByRowKey(domain.getRowKey());
	}

	@Override
	public double sumByExample(BaseDomain domain, String column) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedido) domain).flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().sumByExample(domain, column);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().sumByExample(domain, column);
		}
	}

	public double countItensPedido(ItemPedido itemPedido) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(itemPedido.flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().countItensPedido(itemPedido);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().countItensPedido(itemPedido);
		}
	}
	
	public void updateColumnErp(String rowkey, String dsColumn, Object value, int type) throws SQLException {
		ItemPedidoPdbxDao.getInstanceErp().updateColumn(rowkey, dsColumn, value, type);
	}

	@Override
	public void validate(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		itemPedido.loadHashValoresInfoComplementares();
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			validateCalculoPrecoPorMetroQuadrado(itemPedido);
		}
		validateItemPedido(itemPedido);
		validateItemPedidoVenda(itemPedido);
		//Quando kit ja fez as validações necessárias
		if (itemPedido.isFazParteKitFechado()) return;
		//--
		validaValorItemPedido(itemPedido);
		validaValorTotalItemPedido(itemPedido);
	}

	private void validaValorTotalItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.permiteItemPedidoComQuantidadeZero || !TipoPedidoService.getInstance().validaDescontoItem(itemPedido.pedido.getTipoPedido()) ||
				LavenderePdaConfig.permiteValorZeroPedidoEItem || itemPedido.vlTotalItemPedido != 0 || !validaQuantidadeGondola(itemPedido) || LavenderePdaConfig.isConfigGradeProduto()) return;
		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO);
	}

	private void validaValorItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.permiteItemPedidoComQuantidadeZero || !TipoPedidoService.getInstance().validaDescontoItem(itemPedido.pedido.getTipoPedido()) ||
				LavenderePdaConfig.permiteValorZeroPedidoEItem || itemPedido.vlItemPedido != 0 || !validaQuantidadeGondola(itemPedido) || LavenderePdaConfig.isConfigGradeProduto()) return;
		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO);
	}


	private boolean validaQuantidadeGondola(ItemPedido itemPedido) {
		return !itemPedido.pedido.isGondola() || itemPedido.inCarrousel || itemPedido.qtItemGondola < 0;
	}

	public void validaComprimentoLargura(ItemPedido itemPedido) throws SQLException{
		GrupoProduto1 grupo = GrupoProduto1Service.getInstance().findGrupoProduto1ByItemPedido(itemPedido); 
		Produto produto = itemPedido.getProduto();
		if ((grupo == null || grupo.isValidaInfoCompProduto()) && !produto.isApresentaInfoComplCalculoPrecoPorVolumePorProduto()) return;
		double altura = itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_ALTURA) != null ? (double) itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_ALTURA) : 0.0;
		double largura = itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_LARGURA) != null ? (double) itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_LARGURA) : 0.0; 
		double comprimento = itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_VLCOMPRIMENTO) != null ? (double) itemPedido.hashValoresInfoComplementares.get(Messages.LABEL_VLCOMPRIMENTO) : 0.0;
		if (produto.isApresentaInfoComplCalculoPrecoPorVolumePorProduto()) {
			validaValoresMinimosMaximos(Messages.LABEL_ALTURA, altura, produto.vlAlturaMin, produto.vlAlturaMax);
		}
		validaValoresMinimosMaximos(Messages.LABEL_LARGURA, largura, produto.vlLarguraMin, produto.vlLarguraMax);
		validaValoresMinimosMaximos(Messages.LABEL_VLCOMPRIMENTO, comprimento, produto.vlComprimentoMin, produto.vlComprimentoMax);
	}
	
	private void validaValoresMinimosMaximos(String campo, double atual, double min, double max) {
		if (min == 0 && max == 0) return;
		BigDecimal maxB = ValueUtil.getBigDecimalValue(max);
		BigDecimal minB = ValueUtil.getBigDecimalValue(min);
		BigDecimal atualB = ValueUtil.getBigDecimalValue(atual);
		if (min > 0 && atualB.compareTo(minB) < 0) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_INFO_COMPLEMENTAR_VALOR_MINIMO, new Object[] {campo, min+""}));
		}
		if (max > 0 && atualB.compareTo(maxB) > 0) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_INFO_COMPLEMENTAR_VALOR_MAXIMO, new Object[] {campo, max+""}));
		}
	}

	public void validateCamposObrigatoriosInfosComplementarItemPedido(ItemPedido itemPedido) {
		Map<String, String> mapCamposObrigatorios = LavenderePdaConfig.loadCamposObrigatoriosInfoComplementarItemPedido();
		if (mapCamposObrigatorios.isEmpty()) return;
		StringBuilder exceptions = new StringBuilder();
		Iterator<String> campoIterator = mapCamposObrigatorios.values().iterator();
		boolean hasFail = false;
		while (campoIterator.hasNext()) {
			String key = campoIterator.next();
			Object value = itemPedido.hashValoresInfoComplementares.get(key);
			if (value == null) {
				exceptions.append(hasFail ? ", " : "").append(key);
				hasFail = true;
			} else {
				if (value instanceof Double && ((Double) value) <= 0.0) {
					exceptions.append(hasFail ? ", " : "").append(key);
					hasFail = true;
				}
				if (value instanceof Date && ((Date) value).isBefore(new Date())) {
					throw new ValidationException(Messages.ERRO_CAMPO_OBRIGATORIO_DATAMENORATUAL);
				}
			}
		}
		if (exceptions.length() > 0) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_INFO_COMPLEMENTAR, exceptions.toString()));
		}
	
	}
	
	public boolean validaPosicaoVincoLargura(ItemPedido item) {
		List<String> camposList = LavenderePdaConfig.getInfoComplementarItemPedidoListaCampos();
		boolean valido = false;
		item.vlPosVinco1 = camposList.contains("vlPosVinco1") ? item.vlPosVinco1 : 0;
		item.vlPosVinco2 = camposList.contains("vlPosVinco2") ? item.vlPosVinco2 : 0;
		item.vlPosVinco3 = camposList.contains("vlPosVinco3") ? item.vlPosVinco3 : 0;
		item.vlPosVinco4 = camposList.contains("vlPosVinco4") ? item.vlPosVinco4 : 0;
		item.vlPosVinco5 = camposList.contains("vlPosVinco5") ? item.vlPosVinco5 : 0;
		item.vlPosVinco6 = camposList.contains("vlPosVinco6") ? item.vlPosVinco6 : 0;
		item.vlPosVinco7 = camposList.contains("vlPosVinco7") ? item.vlPosVinco7 : 0;
		item.vlPosVinco8 = camposList.contains("vlPosVinco8") ? item.vlPosVinco8 : 0;
		item.vlPosVinco9 = camposList.contains("vlPosVinco9") ? item.vlPosVinco9 : 0;
		item.vlPosVinco10 = camposList.contains("vlPosVinco10") ? item.vlPosVinco10 : 0;
		if(item.vlPosVinco1 != 0 || item.vlPosVinco2 != 0 || 
				item.vlPosVinco3 != 0 || item.vlPosVinco4 != 0 || item.vlPosVinco5 != 0 || 
				item.vlPosVinco6 != 0 || item.vlPosVinco7 != 0 || item.vlPosVinco8 != 0 ||
				item.vlPosVinco9 != 0 || item.vlPosVinco10 != 0) {
			valido = (item.vlPosVinco1 + item.vlPosVinco2 + 
					item.vlPosVinco3 + item.vlPosVinco4 + 
					item.vlPosVinco5 + item.vlPosVinco6 + 
					item.vlPosVinco7 + item.vlPosVinco8 + 
					item.vlPosVinco9 + item.vlPosVinco10) != item.vlLargura;
		}
		return valido;
	}

	public void validateCalculoPrecoPorMetroQuadrado(ItemPedido itemPedido) throws SQLException {
		Unidade unidade = UnidadeService.getInstance().findUnidadeByCdUnidade(itemPedido.cdUnidade);
		if (unidade == null || unidade.flCalculaPrecoMetroQuadrado == null || !unidade.flCalculaPrecoMetroQuadrado.equals(ValueUtil.VALOR_SIM)) return;
		if (itemPedido.vlLargura == 0 || itemPedido.vlComprimento == 0) throw new ValidationException(Messages.INFOCOMPLEMENTAR_LARGURA_COMPRIMETO_ZERO);
	}

	private void validateItemPedidoVenda(final ItemPedido itemPedido) throws SQLException {
		//vlBaseItemTabelaPreco
		if (itemPedido.vlBaseItemTabelaPreco == 0) {
			throw new ProdutoSemPrecoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_SEM_PRECO, itemPedido.cdProduto));
		}

		//-- Cesta Promocional usa um fluxo totalmente diferente dos descontos e uso de tabelas do sistema.
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		if (!itemPedido.usaCestaPromo) {
			//cdTabelaPreco
			if (ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECO_NOME_TABELA);
			}
			//Valida o desconto por comissão
			if (TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				validateDescComissao(itemPedido);
			}
			//Valida a quantidade mínima baseado na regra do desconto por quantidade
			if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && !DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido) && TipoPedidoService.getInstance().validaQuantidadeItem(itemPedido.pedido.getTipoPedido())) {
				if (ValueUtil.round(itemPedido.getQtItemFisico()) < ValueUtil.round(itemPedido.qtItemPedidoMinimo)) {
					if (!LavenderePdaConfig.permiteVenderProdutoMenorDescontoQuantidade && !itemPedido.isIgnoraDescQtdPro()) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTD_MENOR_MINIMO_PERMITIDO, new Object[] { Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()), Util.getQtItemPedidoFormatted(itemPedido.qtItemPedidoMinimo) }));
					}
				}
			}
			if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido) && TipoPedidoService.getInstance().validaQuantidadeItem(itemPedido.pedido.getTipoPedido())) {
				if (!LavenderePdaConfig.permiteVenderProdutoMenorDescontoQuantidade) {
					if (ValueUtil.round(itemPedido.getQtItemFisico()) < ValueUtil.round(itemPedido.qtItemPedidoMinimo) && !itemPedido.isIgnoraDescQtdPro()) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTD_MENOR_MINIMO_PERMITIDO, new Object[] { Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()), Util.getQtItemPedidoFormatted(itemPedido.qtItemPedidoMinimo) }));
					}
				}
			}
			//Valida se existe saldo no limite de oportunidade
			if (LavenderePdaConfig.usaControleSaldoOportunidade && LavenderePdaConfig.usaOportunidadeVenda && (itemPedido.pedido.isOportunidade() || itemPedido.isOportunidade())) {
				LimiteOportunidadeService.getInstance().validateSaldo(itemPedido);
			}
			if (LavenderePdaConfig.usaControleNoDescontoPromocional && itemPedido.permiteAplicarDesconto()) {
				itemPedido.flAbaixoPtReferenciaDesc = false;
				double vlPctDescPromocional = itemPedido.getItemTabelaPreco().vlPctDescPromocional;
				if (vlPctDescPromocional != 0) {
					itemPedido.vlPtReferencialDesc = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (vlPctDescPromocional / 100)), 2);
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido, 2);
					if (itemPedido.vlItemPedido < itemPedido.vlPtReferencialDesc) {
						itemPedido.flAbaixoPtReferenciaDesc = true;
					}
				}
			}
			if (itemPedido.permiteAplicarDesconto()) {
				if (LavenderePdaConfig.validaDescontoCCPPorItem && TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
					if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
						if (ValueUtil.isEmpty(DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.getProduto().cdProduto))) {
							validaDescontoCCPPorItem(itemPedido);
						} else {
							validaMaxDescAndAcrescimoNoItem(itemPedido);
						}
					} else {
						validaDescontoCCPPorItem(itemPedido);
					}
				} else {
					validaMaxDescAndAcrescimoNoItem(itemPedido);
				}
			}

			//Validação % menor que 0% ou maior que 100%
			if (LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && !TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				validaPctDescontoNegativoOuMaiorQueCem(itemPedido);
			}

			//Consistir desconto máximo do lote do produto
			if (itemPedido.usaPctMaxDescLoteProduto && TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				LoteProduto lote = LoteProdutoService.getInstance().findByItemPedido(itemPedido);
				if (lote != null) {
					DescVidaUtilGrupo descVidaUtil = DescVidaUtilGrupoService.getInstance().getDescVidaUtilByLote(lote.vlPctvidautilproduto, itemPedido.getProduto().cdGrupoProduto1);
					descVidaUtil = (descVidaUtil != null) ? descVidaUtil : new DescVidaUtilGrupo();
					double vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * descVidaUtil.vlPctDesconto) / 100));
					if (itemPedido.vlItemPedido < vlItemPedido) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, descVidaUtil.vlPctDesconto));
					}
				}
			}

			//Consistir desconto máximo do item
			if ((LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() && LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) || LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli &&
					TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				if (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
					if (ValueUtil.round(itemPedido.vlItemPedido) < ValueUtil.round(itemPedido.vlBaseItemPedido)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO, new String[]{StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(itemPedido.vlBaseItemPedido)}));
					}
				} else {
					double pctMaxDesconto = itemPedido.pedido.getCliente().vlPctMaxDesconto;
					if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
						pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, itemPedido.vlPctDescontoCanal, itemPedido.pedido.getCliente().vlPctContratoCli);
					}
					if (pctMaxDesconto != 0 && itemPedido.vlPctDesconto > pctMaxDesconto) {
						throw new ValidationException(MessageUtil.getMessage(Messages.BONIFICACAO_MSG_PCTDESCONTOMAX_VALIDACAO, new String[]{StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(pctMaxDesconto)}));
					}
				}
			}

			//Valida se existe saldo na verba para consumir
			if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba() && !itemPedido.isIgnoraControleVerba() && !VerbaGrupoSaldoService.getInstance().ignoraValidacaoVerbaSaldoPorGrupoProduto(itemPedido.pedido)) {
				if ((((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) && itemPedido.getProduto().isUtilizaVerba()) || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba()  || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
					VerbaService.getInstance().validateSaldo(itemPedido);
					//------------------------------------
					if (LavenderePdaConfig.informaVerbaManual && (!itemPedido.isItemBonificacao())) {
						double vlBase = LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade ? itemPedido.getItemPedidoAud().vlItemPedidoNeutro : itemPedido.getItemTabelaPreco().vlBase;
						double vlDiferencaPreco = vlBase - itemPedido.vlItemPedido;
						double totDescPermitido = ValueUtil.round(vlDiferencaPreco * itemPedido.getQtItemFisico());
						double vlVerbaItem = ValueUtil.round(itemPedido.vlVerbaItem * -1);
						if ((totDescPermitido < vlVerbaItem) && vlVerbaItem != 0) {
							String msgErro = MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_ULTRAPASSOU_DESC_MAX, vlDiferencaPreco);
							throw new ValidationException(StringUtil.getStringValue(msgErro));
						}
					}
				}
			}

			//Valida a quantidade e preço minimo liberados por senha
			if (LavenderePdaConfig.liberaComSenhaPrecoProduto && itemPedido.isFlPrecoLiberadoSenha()) {
				if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && ValueUtil.round(itemPedido.qtItemMinAfterLibPreco) > ValueUtil.round(itemPedido.getQtItemFisico())) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTD_MENOR_MINIMO_PERMITIDO,
							new String[] { StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), StringUtil.getStringValueToInterface(itemPedido.qtItemMinAfterLibPreco) }));
				}
				double minAfterLibPreco = itemPedido.vlItemMinAfterLibPreco;
				if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
					minAfterLibPreco = SolAutorizacaoService.getInstance().getVlLiberadoBySolAutorizacao(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
				}
				double modificadorPreco = 0;
				if (VerbaGrupoSaldoService.getInstance().ignoraValidacaoVerbaSaldoPorGrupoProduto(itemPedido.pedido) && LavenderePdaConfig.usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto) {
					modificadorPreco = itemPedido.vlVerbaItem;
				}
				if (minAfterLibPreco + modificadorPreco > ValueUtil.round(itemPedido.vlItemPedido, ValueUtil.doublePrecision)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(minAfterLibPreco) }));
				}
			}
			//Valida a quantidade e preço liberados por senha
			if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && itemPedido.isFlPrecoLiberadoSenha()) {
				if (ValueUtil.round(itemPedido.qtItemAfterLibPreco) != ValueUtil.round(itemPedido.getQtItemFisico())) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTD_LIBERADA_SENHA_PERMITIDO,
							new String[] { StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), StringUtil.getStringValueToInterface(itemPedido.qtItemAfterLibPreco) }));
				}
				if (ValueUtil.round(itemPedido.vlItemAfterLibPreco) != ValueUtil.round(itemPedido.vlItemPedido)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_LIBERADA_SENHA_PERMITIDO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(itemPedido.vlItemAfterLibPreco) }));
				}
			}
			//Valida Peso Máximo item do pedido
			if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
				PedidoService.getInstance().validaPesoMaximoPedido(itemPedido.pedido, itemPedido);
			}
			// Valida percentual mínimo de rentabilidade do item
			boolean possuiDescPromocionalTabPreco = itemPedido.possuiItemTabelaPreco() && itemPedido.getItemTabelaPreco().vlPctDescPromocional > 0;
    		boolean possuiGrupoDescPromocional = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido);
    		boolean possuiGrupoDescPromocionalQtde = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido);
			if (LavenderePdaConfig.usaRentabilidadeMinimaItemPedido && !possuiDescPromocionalTabPreco && !possuiGrupoDescPromocional && !possuiGrupoDescPromocionalQtde && (tipoPedido != null && !tipoPedido.isBonificacao()) &&
					TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				double vlPctRentabilidade = 0d;
				if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
					vlPctRentabilidade = itemPedido.getVlPctRentabilidadeLiquida();
				} else {
					vlPctRentabilidade = itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false);
				}
				double vlPctMinRentabilidade = getVlPctRentabilidadeMinima(itemPedido);
				if (vlPctRentabilidade < vlPctMinRentabilidade) {
					String msg = "";
					String[] params = new String[] {};
					if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
						msg = Messages.ITEMPEDIDO_MSG_PCT_RENTABILIDADE_MENOR_PERMITIDO_FAIXA;
						params = new String[] {ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadeItemPedido(itemPedido), ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadeMinima(itemPedido)};
					} else {
						msg = Messages.ITEMPEDIDO_MSG_PCT_RENTABILIDADE_MENOR_PERMITIDO;
						params = new String[] {StringUtil.getStringValueToInterface(vlPctRentabilidade), StringUtil.getStringValueToInterface(vlPctMinRentabilidade)};
					}
					throw new ValidationException(MessageUtil.getMessage(msg, params));
				}
			}
			if (LavenderePdaConfig.controlaDescontoUsandoVerbaAtravesQtMinItens && itemPedido.vlVerbaItem != 0) {
				double qtMinVenda = LavenderePdaConfig.usaUnidadeAlternativa ? itemPedido.getItemTabelaPreco().qtMinimaVenda : itemPedido.getProduto().qtMinimaVenda;
				qtMinVenda = getQtdMinVendaUnidadeAlternativa(itemPedido, qtMinVenda);
				if (qtMinVenda > itemPedido.getQtItemFisico()) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_QTD_ABAIXO_MIN_USO_VERBA, new String[] {StringUtil.getStringValueToInterface(qtMinVenda), StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico())}));
				}
			}
		} else {
			double vlPrecoMin = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * itemPedido.pctMaxDescCestaPromo) / 100);
			if (itemPedido.vlItemPedido < vlPrecoMin && TipoPedidoService.getInstance().validaDescontoItem(tipoPedido)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, itemPedido.pctMaxDescCestaPromo));
			}
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher()) {
			validaInserirItemTroca(itemPedido);
		}	
		validateVlMinItemPedido(itemPedido);
	
		if (LavenderePdaConfig.usaValidaConversaoFOB() && itemPedido.pedido.isTipoFreteFob() && itemPedido.pedido.isPedidoAberto()) {
			double qtConversaoUnidadeFob = itemPedido.getProduto().nuConversaoFob;
			if (qtConversaoUnidadeFob > 0 && itemPedido.getQtItemFisico() > 0 && itemPedido.getQtItemFisico() % qtConversaoUnidadeFob != 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_FOB_CONVERSAO_INVALIDA,
					new String[] {
						StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()),
						StringUtil.getStringValueToInterface(qtConversaoUnidadeFob)
					})
				);
			}
		}

	}

	private void validateVlMinItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isIgnoraValidacoesVlMinItemPedPorItemTabPreco() && itemPedido.auxiliarVariaveis.isItemEmSolicitacaoPreco || 
				DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido)) {
			return;
		}
		if (LavenderePdaConfig.usaValorMinimoItemPedidoPorItemTabelaPreco() && TipoPedidoService.getInstance().validaQuantidadeItem(itemPedido.pedido.getTipoPedido())) {
			if (LavenderePdaConfig.ignoraVlMinDescontoMaximoMenor()) { 
				double vlPctMaxDesconto = 0;
				TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido) && tipoPedido.vlPctMaxDesconto != 0) {
					vlPctMaxDesconto = tipoPedido.vlPctMaxDesconto;
				} else {
					vlPctMaxDesconto = itemPedido.getItemTabelaPreco().vlPctMaxDesconto;
				}
				if (vlPctMaxDesconto < 100) {
					return;
				}
			} 
			double vlMinItemPedido = itemPedido.getItemTabelaPreco().vlMinItemPedido;
			if (LavenderePdaConfig.usaValorStCalculoMinimoItemPedidoPorItemTabelaPreco) {
				double vlItemComSt = ValueUtil.round(itemPedido.getVlItemComSt(), LavenderePdaConfig.nuCasasDecimais);
				if (ValueUtil.round(vlMinItemPedido, LavenderePdaConfig.nuCasasDecimais) > vlItemComSt) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_ULTRAPASSADO, vlMinItemPedido));
				}
			} else if (vlMinItemPedido > itemPedido.vlItemPedido) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_ULTRAPASSADO, vlMinItemPedido));
			}
		}
	}
	
	private void validateItemPedido(final ItemPedido itemPedido) throws SQLException {
		//cdRepresentante
		if (ValueUtil.isEmpty(itemPedido.cdRepresentante)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.REPRESENTANTE_NOME_ENTIDADE).toString());
		}
		//flOrigemPedido
		if (ValueUtil.isEmpty(itemPedido.flOrigemPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.PEDIDO_LABEL_FLORIGEM).toString());
		}
		//nuPedido
		if (ValueUtil.isEmpty(itemPedido.nuPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.PEDIDO_LABEL_NUPEDIDO).toString());
		}
		//flTipoItemPedido
		if (ValueUtil.isEmpty(itemPedido.flTipoItemPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.ITEMPEDIDO_LABEL_FLTIPOITEMPEDIDO).toString());
		}
		//nuSeqItemPedido
		if (itemPedido.nuSeqItemPedido == 0) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.ITEMPEDIDO_LABEL_NUSEQITEMPEDIDO).toString());
		}
		//cdProduto
		if (ValueUtil.isEmpty(itemPedido.cdProduto)) {
			throw new ValidationException(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.PRODUTO_NOME_ENTIDADE));
		}
		//qtItemFaturamento
		if (LavenderePdaConfig.usaConversaoUnidadesMedida && !LavenderePdaConfig.ocultaQtItemFaturamento && itemPedido.qtItemFaturamento == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + (ValueUtil.isEmpty(itemPedido.getProduto().dsUnidadeFaturamento) ? "UN" : itemPedido.getProduto().dsUnidadeFaturamento));
		}
		//qtItemFisico
		if (!LavenderePdaConfig.permiteItemPedidoComQuantidadeZero) {
			if (!(LavenderePdaConfig.usaConversaoUnidadesMedida && LavenderePdaConfig.permiteValorZeroPedidoEItem)) {
				if (itemPedido.getQtItemFisico() == 0 && validaQuantidadeGondola(itemPedido) && !LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
					if (LavenderePdaConfig.usaConversaoUnidadesMedida && !ValueUtil.isEmpty(itemPedido.getProduto().dsUnidadeFisica)) {
						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + itemPedido.getProduto().dsUnidadeFisica);
					} else {
						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
					}
				}
			}
		}
		//qtItemDesejado
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			if (itemPedido.qtItemDesejado <= 0 && itemPedido.getQtItemFisico() <= 0) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_QTD_DESEJADA);
			}
		}
		if (LavenderePdaConfig.usaPctMaxParticipacaoItemBonificacao && itemPedido.pedido.isPedidoBonificacao() && itemPedido.getItemTabelaPreco() != null && itemPedido.getItemTabelaPreco().vlPctMaxParticipacao == 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_MSG_ERRO_PARTICIPACAO_NAO_PERMITIDA);
		}
		// Quantidade inteira
		if (!LavenderePdaConfig.permiteItemPedidoComQuantidadeZero) {
			if (LavenderePdaConfig.usaConversaoUnidadesMedida && itemPedido.flEditandoQtItemFaturamento && LavenderePdaConfig.isUsaQtdItemPedidoFaturamentoInteiro()) {
				double qtItemFat = 0;
				try {
					qtItemFat = Convert.toDouble(Convert.toString(itemPedido.qtItemFaturamento, 5));
				} catch (InvalidNumberException e) {
					//--
				}
				if ((qtItemFat % 1) != 0) {
					throw new ValidationException(Messages.ITEMPEDIDO_MSG_QTD_INTEIRA, StringUtil.getStringValueToInterface(itemPedido.qtItemFaturamento));
				}
			} else if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
				double qtItemFis = 0;
				try {
					qtItemFis = Convert.toDouble(Convert.toString(itemPedido.getQtItemFisico(), 5));
				} catch (InvalidNumberException e) {
					//--
				}
				if ((qtItemFis % 1) != 0) {
					throw new ValidationException(Messages.ITEMPEDIDO_MSG_QTD_INTEIRA, StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()));
				}
			}
		}

		//Limite de margem de rentabilidade do item
		validaLimiteMargemRentab(itemPedido);
	}

	private void validaLimiteMargemRentab(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemRentabilidade() || !LavenderePdaConfig.usaValidacaoPercentualMargemRentab() || ValueUtil.isEmpty(itemPedido.cdMargemRentab)) {
			return;
		}
		MargemRentab margemRentab = (MargemRentab) MargemRentabService.getInstance().findByRowKey(new MargemRentab(itemPedido).getRowKey());
		if (margemRentab == null) {
			return;
		}
		if (LavenderePdaConfig.validaPercentualMinimoMargemRentab() && itemPedido.vlPctMargemRentab < margemRentab.vlPctMargemRentabMin) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PCT_MIN_MARGEMRENTAB_ITEM_NAO_ATINGIDO, StringUtil.getStringValueToInterface(margemRentab.vlPctMargemRentabMin)));
		}
		if (LavenderePdaConfig.validaPercentualMaximoMargemRentab() && itemPedido.vlPctMargemRentab > margemRentab.vlPctMargemRentabMax) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PCT_MAX_MARGEMRENTAB_ITEM_ATINGIDO, StringUtil.getStringValueToInterface(margemRentab.vlPctMargemRentabMax)));
		}
	}

	private void validaNivelGradeObrigatorio(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto().nuNivelGradeObrigatorio == 1 && ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
			throw new ValidationException(Messages.GRADE_VALIDACAO_NIVEL1);
		} else if (itemPedido.getProduto().nuNivelGradeObrigatorio == 2 && ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade2)) {
			throw new ValidationException(Messages.GRADE_VALIDACAO_NIVEL2);
		} else if (itemPedido.getProduto().nuNivelGradeObrigatorio == 3 && ValueUtil.isEmpty(itemPedido.itemPedidoGradeList)) {
			throw new ValidationException(Messages.GRADE_VALIDACAO_NIVEL3);
		}
	}

	private void validaMaxDescAndAcrescimoNoItem(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido.isPedidoCritico() || itemPedido.hasDescProgressivo()) return;
		
		if (LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento() && itemPedido.pedido.statusOrcamento != null && !itemPedido.pedido.statusOrcamento.permiteFecharPedido()){
			validaPctDescontoNegativoOuMaiorQueCem(itemPedido);
			validaPctAcrescimoNegativo(itemPedido);
			return;
		}
		
		if (itemPedido.politicaComercial != null && LavenderePdaConfig.isValidaPctPoliticaComercial()) {
			validaVlPctMaxMinPoliticaComercial(itemPedido);
			return;
		}
		
		if (!LavenderePdaConfig.apenasAvisaDescontoAcrescimoMaximo && TipoPedidoService.getInstance().validaDescontoItem(itemPedido.pedido.getTipoPedido())) {
			// VALIDA O MAXIMO DE DESCONTO PARA O ITEM
			validaDescontoMaximoPermitido(itemPedido);
			// VALIDA O MAXIMO DE ACRESCIMO PARA O ITEM
			validaAcrescimoMaximoPermitido(itemPedido);
		}
		
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow()) {
			validaEAvisaMaxDescontoUsuario(itemPedido, false);
			validaDescMaximoUsuarioDesc(itemPedido);
		} else if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
			validaEAvisaMaxDescontoUsuario(itemPedido, true);
		}
		
	}
	
	private void validaDescMaximoUsuarioDesc(final ItemPedido itemPedido) throws SQLException {
		double vlPctMaxDescUsu = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(true, true);
		if (itemPedido.vlPctDesconto > ValueUtil.round(vlPctMaxDescUsu)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_ITEM_MSG_MAXIMO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto) + "%", StringUtil.getStringValueToInterface(vlPctMaxDescUsu) + "%" }));
		}
	}
	
	private void validaEAvisaMaxDescontoUsuario(ItemPedido itemPedido, final boolean isAlcada) throws SQLException {
		double maxDescUsuario = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(isAlcada, false);
		if (itemPedido.vlPctDesconto > maxDescUsuario) {
			itemPedido.pendenteMaxDesc = true;
		} else if (!isAlcada) {
			itemPedido.pendenteMaxDesc = false;
		}
	}

	//-- Método Coberto por TESTCASE
	public void validaDescontoMaximoPermitido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.politicaComercial != null && LavenderePdaConfig.isValidaPctMaxPoliticaComercial()) return;

		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			double vlPctMaxDescontoItem = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			if (itemPedido.vlPctDesconto > vlPctMaxDescontoItem) {
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDescontoItem / 100);
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_VALIDACO_PCTDESC_MAIOR_PERMITIDO, new Object[] {StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(vlPctMaxDescontoItem)}), vlPermitido);
			}
			if (itemPedido.vlPctDesconto3 >= 100) {
				throw new ValidationException(Messages.ITEMPEDIDO_VALIDACO_PCTDESC_MAIOR_100);
			}
			return;
		}
		if (LavenderePdaConfig.usaDescMaxProdCli && itemPedido.getDescMaxProdCli() != null) {
			if (itemPedido.vlPctDesconto > itemPedido.getDescMaxProdCli().vlPctDescMax) {
				String[] params = {
						StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto),
						StringUtil.getStringValueToInterface(itemPedido.getDescMaxProdCli().vlPctDescMax)
				};
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - itemPedido.getDescMaxProdCli().vlPctDescMax / 100);
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_MAX_DESC_PRODUTO_CLIENTE, params), vlPermitido);
			}
			return;
		}
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		if (LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco && LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido() && itemTabPreco.vlPctDescPromocional > 0 && itemPedido.vlPctDesconto > 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_MSG_DESCONTO_ITEM_DESC_PROMOCIONAL);
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			itemPedido.pendenteMaxDesc = false;
		}
		//Validação % menor que 0% ou maior que 100%
		if (LavenderePdaConfig.isPermiteDescontoPercentualItemPedido()) {
			if (itemPedido.vlPctDesconto < 0) {
				throw new ValidationException("validacao_campo_negativo", "itemPedido_vlPctDesconto");
			}
			double vlPctDesconto = itemPedido.vlPctDesconto;
			if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem() && (vlPctDesconto += itemPedido.vlPctDescPedido) > 100) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ACUMULA_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlPctDescPedido), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(vlPctDesconto) }));
			} else if (vlPctDesconto > 100) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[] { "100" }));
			}
			if (isUsaDescQtdEAcumulaDescontoManualComAFaixaQtd(itemPedido)) {
				vlPctDesconto = itemPedido.vlPctFaixaDescQtd + itemPedido.vlPctDesconto;
				if (vlPctDesconto >= 100) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ACUMULA_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlPctFaixaDescQtd), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(vlPctDesconto)}));
				}
			}
		}
		try {
			double vlPctMaxDescontoItem = getVlPctMaxDescontoItemPedido(itemPedido);
			vlPctMaxDescontoItem = LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata ? itemPedido.vlPctDesconto : vlPctMaxDescontoItem;
			double vlMaxDescontoItem = itemTabPreco.vlMaxDesconto;
			if ((DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) || itemPedido.getProduto().isEspecial())
					&& !LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli 
					&& !LavenderePdaConfig.isUsaValidacaoDescMaxNoDescontoPromocional()) {
				vlMaxDescontoItem = 0;
			}
			boolean validaVerbaGrupoProdComTolerancia = LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto && VerbaGrupoSaldoService.getInstance().hasVerbaGrupoSaldoWeb(itemPedido) && itemPedido.getProduto().isUtilizaVerba();
			VerbaGrupoSaldo verbaGrupoSaldo = new VerbaGrupoSaldo();
			if (validaVerbaGrupoProdComTolerancia) {
				verbaGrupoSaldo = VerbaGrupoSaldoService.getInstance().getVerbaGrupoSaldoWeb(itemPedido);
				if (verbaGrupoSaldo != null && itemPedido.descQuantidade == null) {
					vlPctMaxDescontoItem = verbaGrupoSaldo.vlPctToleranciaDesc;
				}
			}
			//--
			boolean itemPossuiDescontoQtdeGrupoProduto = DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido);
			boolean itemPossuiDescontoQtdePacoteProduto = LavenderePdaConfig.usaDescQuantidadePorPacote && ValueUtil.isNotEmpty(itemPedido.cdPacote);
			boolean validaDescMaiorDescQtd = !LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo.equals("2");
			boolean pedidoPossuiDescQtdPeso = LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlItemPedido() && itemPedido.pedido.descQuantidadePeso != null && !produtoDescQuantidadePesoBloqueado(itemPedido.pedido,itemPedido);
			//Validação do % máximo de desconto do desconto quantidade
			if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido  && itemPedido.permiteAplicarDesconto() && !itemPedido.ignoraValidacaoDesconto && !itemPossuiDescontoQtdeGrupoProduto && !itemPossuiDescontoQtdePacoteProduto) {
				boolean validaPorqueNaoUsaVerba = !LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || (!LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex || (itemPedido.vlVerbaItem >= 0));
				if (validaPorqueNaoUsaVerba && validaDescMaiorDescQtd || validaVerbaGrupoProdComTolerancia) {
					boolean fazValidacaoEmPercetual = !LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex();
					double vlBaseCalculo = getVlBaseCalculoParaRotinasDeValidacaoDescontoMaximo(itemPedido);
					double vlItemPedidoMaxPermitido = ValueUtil.round(vlBaseCalculo);
					boolean naoConsisteDescontoMaximoProdutoSemQtd = true;
					if (itemPedido.descQuantidade != null && !LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta) {
						if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
							if (LavenderePdaConfig.isUsaDescontoMaximoEmValor()) {
								vlItemPedidoMaxPermitido = ValueUtil.round(itemPedido.vlBaseItemPedido - itemPedido.getItemTabelaPreco().vlMaxDesconto, LavenderePdaConfig.nuCasasDecimais);
							} else {
								vlItemPedidoMaxPermitido = ValueUtil.round(itemPedido.descQuantidade.getVlDescVlBaseItemPedido(itemPedido.vlBaseItemPedido), LavenderePdaConfig.nuCasasDecimais);
							}
//							itemPedido.vlPctDesconto = ValueUtil.round((1 - ((itemPedido.vlBaseItemPedido - itemPedido.descQuantidade.vlDesconto) / itemPedido.vlBaseItemPedido)) * 100);
//							itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido, LavenderePdaConfig.nuCasasDecimais);
						} else if (validaVerbaGrupoProdComTolerancia) {
							vlItemPedidoMaxPermitido = itemPedido.vlBaseItemPedido * (1 - ((itemPedido.descQuantidade.vlPctDesconto + verbaGrupoSaldo.vlPctToleranciaDesc) / 100));
						} else {
							vlItemPedidoMaxPermitido = itemPedido.vlBaseItemPedido * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100));
						}
					} else {
						if (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco) {
							vlItemPedidoMaxPermitido = itemPedido.vlBaseItemPedido * (1 - (vlPctMaxDescontoItem / 100));
						} 
						naoConsisteDescontoMaximoProdutoSemQtd = LavenderePdaConfig.isNaoConsisteDescontoMaximo();
					}
					if (naoConsisteDescontoMaximoProdutoSemQtd
							&& !(itemPedido.politicaComercial != null && LavenderePdaConfig.isValidaPctMaxPoliticaComercial())) {
						if (LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
							vlItemPedidoMaxPermitido = vlItemPedidoMaxPermitido * (1 - (itemTabPreco.getTabelaPreco().vlPctMaxDescAdicionalItem / 100));
						}
						if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedidoValidandoVlMinimo()) {
							double vlItemPedido = getVlItemPedidoUltimoPedidoCliente(itemPedido);
							if (vlItemPedido != 0) {
								vlItemPedidoMaxPermitido = vlItemPedido;
							}
						}
						vlItemPedidoMaxPermitido = ValueUtil.round(vlItemPedidoMaxPermitido);
						itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
						// Valida o valor final, se ultrapassou o valor mínimo permitido
						if (itemPedido.vlItemPedido < vlItemPedidoMaxPermitido) {
							throw new DescAcresMaximoException(MessageUtil.getMessage((!LavenderePdaConfig.permiteDescAdicionalPorTabPreco) ? Messages.ITEMPEDIDO_MSG_VL_MENOR_DESC_POR_QTD : Messages.ITEMPEDIDO_MSG_VL_MENOR_DESC_ADICIONAL,
									new Object[] { StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(vlItemPedidoMaxPermitido) }), vlItemPedidoMaxPermitido);
						}
						// Valida o % desconto final, se ultrapassou o desconto maximo permitido
						if (fazValidacaoEmPercetual) {
							double vlPctMaxDesconto = 0;
							if (itemPedido.descQuantidade != null) {
								if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
									vlPctMaxDesconto = itemPedido.vlPctDesconto;
								} else if (validaVerbaGrupoProdComTolerancia) {
									vlPctMaxDesconto = itemPedido.vlPctDesconto + verbaGrupoSaldo.vlPctToleranciaDesc;
								} else {
									vlPctMaxDesconto = itemPedido.descQuantidade.vlPctDesconto;
								}
							} else {
								if (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco) {
									vlPctMaxDesconto = vlPctMaxDescontoItem;
								}
							}
							if (LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
								vlPctMaxDesconto += itemTabPreco.getTabelaPreco().vlPctMaxDescAdicionalItem;
							}
							if (itemPedido.vlPctDesconto > vlPctMaxDesconto) {
								double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDesconto / 100);
								throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[] { StringUtil.getStringValue(vlPctMaxDesconto) }), vlPermitido);
							} 
						}
					}
				}
			}
			//Base de cálculo para as rotinas de validação de desconto máximo
			boolean fazValidacaoEmPercetual = true;
			double vlBaseCalculo = 0d;
			if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && itemPedido.vlEmbalagemElementar != 0) {
				if (LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex()) {
					fazValidacaoEmPercetual = !LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex();
					vlBaseCalculo = getVlBaseCalculoParaRotinasDeValidacaoDescontoMaximo(itemPedido) / itemPedido.qtEmbalagemElementar;
				} else {
					vlBaseCalculo = itemPedido.vlBaseEmbalagemElementar;
				}
			} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
				vlBaseCalculo = itemPedido.vlBaseItemIpi;
			} else {
				fazValidacaoEmPercetual = !LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex();
				vlBaseCalculo = getVlBaseCalculoParaRotinasDeValidacaoDescontoMaximo(itemPedido);
			}

			// Validação do % máximo de desconto do item da tabela de preço
			try {
				if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
					itemPedido.ignoraValidacaoDescontoOld = itemPedido.ignoraValidacaoDesconto;
					boolean isItemPedidoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
					if (isItemPedidoAutorizado) {
						itemPedido.ignoraValidacaoDesconto = LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento();
					}
				}
				boolean isDeveValidarDesconto = !itemPedido.ignoraValidacaoDesconto && !itemPedido.isCombo() && !itemPedido.isKitTipo3();
				boolean isDescPromocionalEncontrado = (DescPromocionalService.getInstance().isDescPromocionalEncontrado(itemPedido) && isDeveValidarDesconto);
				isDeveValidarDesconto &= !itemPedido.usaPctMaxDescLoteProduto && !itemPedido.hasDescProgressivo();
				
				//Validação do percentual máximo de desconto por representante
				if (LavenderePdaConfig.validaPctMaxDescPorRepresentante > 0 && isDeveValidarDesconto) {
					validatePctMaxDescRep(itemPedido, findVlPctMaxDescontoRep(), vlBaseCalculo);
				}
				
				isDeveValidarDesconto &= !LavenderePdaConfig.isNaoConsisteDescontoMaximo();
				isDeveValidarDesconto |= isDescPromocionalEncontrado && pedidoPossuiDescQtdPeso;
				
				if (isDeveValidarDesconto) {
					if ((LavenderePdaConfig.isValidaDescMaxMesmoComDescQuantidadeEDescontoGrupo() || (itemPedido.descQuantidade == null && !itemPossuiDescontoQtdeGrupoProduto && !itemPossuiDescontoQtdePacoteProduto)) && !pedidoPossuiDescQtdPeso) {
						double vlItemPedidoMaxPermitido = ValueUtil.round(vlBaseCalculo);
						if (LavenderePdaConfig.isUsaDescontoMaximoEmValor() && !isIgnoraDescontoMaximoEmValor(itemTabPreco)) {
							vlItemPedidoMaxPermitido -= vlMaxDescontoItem;
						} else {
							vlItemPedidoMaxPermitido *= 1 - (vlPctMaxDescontoItem / 100);
						}
						vlItemPedidoMaxPermitido = getVlItemPedidoMaxDescAcrescPermitido(itemPedido, vlItemPedidoMaxPermitido, vlPctMaxDescontoItem, true);
						if (LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
							vlItemPedidoMaxPermitido = vlItemPedidoMaxPermitido * (1 - (itemTabPreco.getTabelaPreco().vlPctMaxDescAdicionalItem / 100));
						}
						if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
							vlItemPedidoMaxPermitido = ValueUtil.round(vlBaseCalculo) * (1 - (CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(itemPedido, itemPedido.pedido.getCliente()), itemPedido.pedido.getCliente().vlPctContratoCli) / 100));
						}
						double vlItemVenda = 0;
						if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && itemPedido.vlEmbalagemElementar != 0) {
							vlItemVenda = ValueUtil.round(itemPedido.vlEmbalagemElementar);
						} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
							vlItemVenda = ValueUtil.round(itemPedido.vlItemIpi);
						} else {
							vlItemVenda = ValueUtil.round(itemPedido.vlItemPedido);
							if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem && itemPedido.pedido.getCliente().isOptanteSimples()) {
								vlItemPedidoMaxPermitido = vlItemPedidoMaxPermitido - itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples;
							}
						}
						if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedidoValidandoVlMinimo()) {
							double vlItemPedido = getVlItemPedidoUltimoPedidoCliente(itemPedido);
							if (vlItemPedido != 0) {
								vlItemPedidoMaxPermitido = vlItemPedido;
							}
						}
						if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
							vlItemPedidoMaxPermitido = vlItemPedidoMaxPermitido * (1 - (itemPedido.vlPctDescCondPagto / 100));
						}
						double vlItemPedidoMaxPermitidoCeiledToInterface = vlItemPedidoMaxPermitido;
						if (itemPedido.isEditandoValorItem() && ValueUtil.doublePrecision > ValueUtil.doublePrecisionInterface) {
							vlItemPedidoMaxPermitidoCeiledToInterface = ValueUtil.round(Math.ceil(vlItemPedidoMaxPermitido * (Math.pow(10, ValueUtil.doublePrecisionInterface))) / (Math.pow(10, ValueUtil.doublePrecisionInterface)));
						} else {
							vlItemPedidoMaxPermitidoCeiledToInterface = ValueUtil.round(vlItemPedidoMaxPermitidoCeiledToInterface);
						}
						if (vlItemVenda < vlItemPedidoMaxPermitidoCeiledToInterface) {
							bloqueiaEAvisaVendaDescMaxPorValorUltrapassado(itemPedido, vlItemVenda, vlItemPedidoMaxPermitidoCeiledToInterface);
						}
						itemPedido.pedido.removendoItem = false;
						// Valida o % de desconto final, se ultrapassou o desconto maximo permitido
						if (fazValidacaoEmPercetual && !LavenderePdaConfig.isUsaDescontoMaximoEmValor() || LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa) {
							double vlPctMaxDesconto = vlPctMaxDescontoItem;
							if (LavenderePdaConfig.permiteDescAdicionalPorTabPreco) {
								vlPctMaxDesconto += itemTabPreco.getTabelaPreco().vlPctMaxDescAdicionalItem;
							}
							vlPctMaxDesconto = ValueUtil.round(vlPctMaxDesconto);
							if (itemPedido.vlPctDesconto > vlPctMaxDesconto) {
								double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDesconto / 100);
								throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(vlPctMaxDesconto) }), vlPermitido);
							}
						}
					}
					//Validação % máximo de desconto quantidade por pacote de produto
					if (itemPossuiDescontoQtdePacoteProduto && validaDescMaiorDescQtd && !itemPedido.isReabrindoPedido) {
						DescontoPacoteService.getInstance().validatePctMaxDescontoPacoteMaiorFaixaDisponivel(itemPedido);
					}

					//Validação % máximo de desconto quantidade por grupo de produto
					if (itemPossuiDescontoQtdeGrupoProduto && validaDescMaiorDescQtd && !itemPedido.isReabrindoPedido) {
						DescontoGrupoService.getInstance().validatePctMaxDescontoMaiorFaixaDisponivel(itemPedido);
					}

					//Validação do percentual máximo de desconto por tipo de Pedido e tipo Pedido por Produto Especifico
					if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido) || LavenderePdaConfig.isUtilizaTipoPedProdEspecificoDescAcresMaximo()) {
						TipoPedProdDescAcresService.getInstance().validadePctDescMaxTipoPedido(itemPedido, ValueUtil.round(vlPctMaxDescontoItem));
					}
				} 
			} finally {
				if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
					itemPedido.ignoraValidacaoDesconto = itemPedido.ignoraValidacaoDescontoOld;
				}
			}
		} catch (ValidationException ex) {
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescItem() || LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow()) {
				itemPedido.pendenteMaxDesc = true;
			} else {
				throw ex;
			}
		}
	}

	public double getVlItemPedidoUltimoPedidoCliente(ItemPedido itemPedido) throws SQLException {
		ItemPedido itemPedidoUltimoPedidoCliente = itemPedido.getItemPedidoUltimoPedidoCliente();
		if (itemPedidoUltimoPedidoCliente != null) {
			if (LavenderePdaConfig.isIgnoraAcrescimoDesconto() && itemPedidoUltimoPedidoCliente.vlBaseItemPedido > 0 && ValueUtil.isNotEmpty(itemPedidoUltimoPedidoCliente.vlBaseItemPedido)) {
				itemPedidoUltimoPedidoCliente.vlItemPedido = itemPedidoUltimoPedidoCliente.vlBaseItemPedido;
				zeraDescAcresItemPedido(itemPedidoUltimoPedidoCliente);
			}
			double vlItemPedido = itemPedidoUltimoPedidoCliente.vlItemPedido;
			String cdUnidade = itemPedidoUltimoPedidoCliente.cdUnidade;
			if (LavenderePdaConfig.usaUnidadeAlternativa && itemPedido.cdUnidade != null && cdUnidade != null && !ValueUtil.valueEquals(cdUnidade, itemPedido.cdUnidade)) {
				ItemPedido itemPedidoAux = new ItemPedido();
				itemPedidoAux = (ItemPedido) itemPedido.clone();
				itemPedidoAux.flTipoEdicao = itemPedido.getFltipoEdicao();
				itemPedidoAux.vlPctDesconto = ValueUtil.round(itemPedidoUltimoPedidoCliente.vlPctDesconto);
				itemPedidoAux.vlPctAcrescimo = ValueUtil.round(itemPedidoUltimoPedidoCliente.vlPctAcrescimo);
				PedidoService.getInstance().resetDadosItemPedido(itemPedidoAux.pedido, itemPedidoAux);
				calculate(itemPedidoAux, itemPedidoAux.pedido);
				vlItemPedido = itemPedidoAux.vlItemPedido;
			}
			return ValueUtil.round(vlItemPedido);
		}
		return 0d;
	}
	
	public double getVlPctMaxDescontoItemPedido(ItemPedido itemPedido) throws SQLException {
		double vlPctMaxDescontoItem = 0;
		if (itemPedido != null) {
			ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
			if ((LavenderePdaConfig.isUsaDescontoMaximoItemPorCliente() || LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo()) && !LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
				vlPctMaxDescontoItem = itemPedido.pedido.getCliente().vlPctMaxDesconto;
			} else {
				vlPctMaxDescontoItem = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDescontoItem = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
					vlPctMaxDescontoItem = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
					if (vlPctMaxDescontoItem == 0 && itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto()) != 0) {
						vlPctMaxDescontoItem = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
					}
				}
			}
			if (LavenderePdaConfig.getVlPctMargemDescontoItemPedido() > 0) {
				vlPctMaxDescontoItem = LavenderePdaConfig.getVlPctMargemDescontoItemPedido();
				ProdutoMargem produtoMargem = ProdutoMargemService.getInstance().getProdutoMargem(itemPedido.getProduto(), itemPedido.pedido.getCliente().cdRamoAtividade);
				if (produtoMargem != null) {
					vlPctMaxDescontoItem = produtoMargem.vlPctMargemDesconto;
				}
			}
			if (LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente) {
				vlPctMaxDescontoItem = getVlPctMaxDescPorRedutorCliente(itemTabPreco, itemPedido.pedido.getCliente().vlPctMaxDesconto, itemPedido.getProduto());
			}
			if (LavenderePdaConfig.validaPctMaxDescAcrescPorUsuario && !itemPedido.isFlPrecoLiberadoSenha()) {
				if (LavenderePdaConfig.getVlPctMargemDescontoItemPedido() > 0) {
					vlPctMaxDescontoItem += UsuarioConfigEmpService.getInstance().getVlPctMaxDesconto();
					if (vlPctMaxDescontoItem > 100) {
						vlPctMaxDescontoItem = 100;
					}
				} else {
					vlPctMaxDescontoItem = UsuarioConfigEmpService.getInstance().getVlPctMaxDesconto();
				}
			}
			if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
				vlPctMaxDescontoItem = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, itemPedido.vlPctDescontoCanal, itemPedido.pedido.getCliente().vlPctContratoCli);
			}
			if (((DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido)) && !ValueUtil.getBooleanValue(itemPedido.flIgnoraDescQtdPro) || itemPedido.getProduto().isEspecial()) 
					&& !LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli 
					&& !LavenderePdaConfig.isUsaValidacaoDescMaxNoDescontoPromocional()) {
				vlPctMaxDescontoItem = 0;
			}
			if (LavenderePdaConfig.isAcumulaComDescDoItem() && LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
				vlPctMaxDescontoItem += itemPedido.vlPctDescPedido;
			}
			if (LavenderePdaConfig.usaPoliticaComercial() && !itemPedido.pedido.loadedPoliticaComercialOnCondicaoPagtoChange 
					&& LavenderePdaConfig.isValidaPctMaxPoliticaComercial() || LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial()) {
				loadPoliticaComercial(itemPedido, itemPedido.pedido);
				vlPctMaxDescontoItem = getVlPctMaxDescontoPoliticaComercial(vlPctMaxDescontoItem, itemPedido);
			}
		}
		return vlPctMaxDescontoItem;
	}

	//Metodo possui test case
	public double getVlPctMaxDescPorRedutorCliente(ItemTabelaPreco itemTabPreco, double vlPctMaxDescontoCli, Produto produto) {
		double vlPctDescMax = itemTabPreco.vlPctDescValorBase - vlPctMaxDescontoCli + itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(produto);
		if (vlPctDescMax > 100) {
			return 100;
		} else if (vlPctDescMax < 0) {
			return 0;
		}
		return vlPctDescMax;
	}

	private double getVlBaseCalculoParaRotinasDeValidacaoDescontoMaximo(ItemPedido itemPedido) throws SQLException {
		double vlBaseCalculo = itemPedido.vlBaseItemPedido;
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			vlBaseCalculo -= itemPedido.vlDescontoCCP;
		}
		if (LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex() && itemPedido.permiteUtilizarVlBaseFlexComoVlBaseCalculo()) {
			vlBaseCalculo = itemPedido.vlBaseFlex;
		}
		return vlBaseCalculo;
	}
	
	public void validaAcrescimoMaximoPermitido(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			itemPedido.pendenteMaxAcresc = false;
		}
		
		validaPctAcrescimoNegativo(itemPedido);
		
		if (itemPedido.politicaComercial != null && LavenderePdaConfig.isValidaPctMaxPoliticaComercial()) return;
		
		try {
			boolean itemPedidoAutorizado = false;
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				itemPedido.ignoraValidacaoDescontoOld = itemPedido.ignoraValidacaoDesconto;
				itemPedidoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
				if (itemPedidoAutorizado) {
					itemPedido.ignoraValidacaoDesconto = false;
				}
			}
			double vlPctMaxAcrescimo = itemPedido.getItemTabelaPreco().vlPctMaxAcrescimo;
			if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido() && LavenderePdaConfig.isUtilizaTipoPedProdEspecificoDescAcresMaximo()) {
				vlPctMaxAcrescimo = TipoPedProdDescAcresService.getInstance().getPctAcrescimoPorTipoPedidoEProduto(itemPedido);
			} else if (TipoPedidoService.getInstance().isUsaAcrescimoMaximoPorTipoPedido(itemPedido.pedido)) {
				vlPctMaxAcrescimo = itemPedido.pedido.getTipoPedido().vlPctMaxAcrescimo;
			}
			double vlMaxAcrescimo = itemPedido.getItemTabelaPreco().vlMaxAcrescimo;
			if ((DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido))
					&& !LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli 
					&& !LavenderePdaConfig.isUsaValidacaoAcrescMaxNoDescontoPromocional()) {
				vlPctMaxAcrescimo = 0;
				vlMaxAcrescimo = 0;
			}
			if (LavenderePdaConfig.validaPctMaxDescAcrescPorUsuario && !itemPedido.isFlPrecoLiberadoSenha()) {
				vlPctMaxAcrescimo = UsuarioConfigEmpService.getInstance().getVlPctMaxAcrescimo();
			}
			//Consistir acréscimo máximo
			if ((LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido() || LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido()) && !itemPedido.ignoraValidacaoDesconto) {
				consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, itemPedidoAutorizado);
			}
		} catch (ValidationException ex) {
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescItem()) {
				itemPedido.pendenteMaxAcresc = true;
			} else {
				throw ex;
			}
		} finally {
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				itemPedido.ignoraValidacaoDesconto = itemPedido.ignoraValidacaoDescontoOld;
		}
	}
	}

	protected void consisteAcrescimoMaximo(final ItemPedido itemPedido, double vlPctMaxAcrescimo, double vlMaxAcrescimo, boolean isItemAutorizadoSolicitacaoDesc) throws SQLException {
		if (!LavenderePdaConfig.isNaoConsisteAcrescimoMaximo()) {
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && isItemAutorizadoSolicitacaoDesc) {
				vlPctMaxAcrescimo = vlMaxAcrescimo = 0;
			}
			boolean usaAcrescimoMaximoEmValor = LavenderePdaConfig.isUsaAcrescimoMaximoEmValor();
			double vlItemPedido = getVlItemPedidoForCalculoAcrescimo(itemPedido);
			double vlBaseCalculo = getVlBaseCalculoForCalculoAcrescimo(itemPedido);
			double vlItemPedidoMaxPermitido = getVlItemPedidoMaxPermitidoForCalculoAcrescimo(itemPedido, usaAcrescimoMaximoEmValor, vlBaseCalculo, vlMaxAcrescimo, vlPctMaxAcrescimo);
			double vlMaxAutorizado = isItemAutorizadoSolicitacaoDesc ? ValueUtil.round(SolAutorizacaoService.getInstance().getVlLiberadoBySolAutorizacao(itemPedido, null)) : itemPedido.vlItemMinAfterLibPreco;

			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && isItemAutorizadoSolicitacaoDesc && itemPedido.vlItemPedido > vlMaxAutorizado) {
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VALOR_ACRESCIMO_ULTRAPASSADO_SOL_AUT, vlMaxAutorizado), vlMaxAutorizado);
			} else if (usaAcrescimoMaximoEmValor && vlItemPedido > vlItemPedidoMaxPermitido) {
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VALOR_ACRESCIMO_ULTRAPASSADO, vlMaxAcrescimo), vlItemPedidoMaxPermitido);
			} else if (!usaAcrescimoMaximoEmValor && itemPedido.vlPctAcrescimo > vlPctMaxAcrescimo && !isItemAutorizadoSolicitacaoDesc) {
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ACRESCIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(vlItemPedido), StringUtil.getStringValueToInterface(vlItemPedidoMaxPermitido)}), vlItemPedidoMaxPermitido);
			}
		}
	}

	private double getVlItemPedidoForCalculoAcrescimo(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && itemPedido.vlEmbalagemElementar != 0) {
			return ValueUtil.round(itemPedido.vlEmbalagemElementar);
		} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			return ValueUtil.round(itemPedido.vlItemIpi);
		} else {
			double vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem && itemPedido.pedido.getCliente().isOptanteSimples()) {
				vlItemPedido = ValueUtil.round(vlItemPedido + itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples);
			}
			return vlItemPedido;
		}
	}

	private double getVlBaseCalculoForCalculoAcrescimo(ItemPedido itemPedido) {
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && itemPedido.vlEmbalagemElementar != 0) {
			return itemPedido.vlBaseEmbalagemElementar;
		} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			return itemPedido.vlBaseItemIpi;
		} else {
			return itemPedido.vlBaseItemPedido;
		}
	}
	
	public double getVlItemPedidoMaxPermitidoForCalculoAcrescimo(ItemPedido itemPedido, boolean usaAcrescimoMaximoEmValor,  double vlBaseCalculo, double vlMaxAcrescimo, double vlPctMaxAcrescimo  ) throws SQLException {
		double vlItemPedidoMaxPermitido = 0;
		if (usaAcrescimoMaximoEmValor) {
			vlItemPedidoMaxPermitido = ValueUtil.round(ValueUtil.round(vlBaseCalculo) + vlMaxAcrescimo);
		} else {
			vlItemPedidoMaxPermitido = ValueUtil.round(ValueUtil.round(vlBaseCalculo) * (1 + (vlPctMaxAcrescimo / 100)));
		}
		return getVlItemPedidoMaxDescAcrescPermitido(itemPedido, vlItemPedidoMaxPermitido, vlPctMaxAcrescimo, false);
	}
	
	public void bloqueiaEAvisaVendaDescMaxPorValorUltrapassado(final ItemPedido itemPedido, double vlItemVenda, double vlItemPedidoMaxPermitido) {
		if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			throw new ValidationValorMinPedidoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO, new String[]{StringUtil.getStringValueToInterface(vlItemVenda), StringUtil.getStringValueToInterface(vlItemPedidoMaxPermitido)}), vlItemPedidoMaxPermitido);
		} else {
			if (!itemPedido.pedido.removendoItem) {
				throw new ValidationValorMinPedidoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO, new String[]{StringUtil.getStringValueToInterface(vlItemVenda), StringUtil.getStringValueToInterface(vlItemPedidoMaxPermitido)}), vlItemPedidoMaxPermitido);
			}
		}
	}

	protected void validaPctAcrescimoNegativo(final ItemPedido itemPedido) {
		if (LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido() && itemPedido.vlPctAcrescimo < 0) {
			throw new ValidationException("validacao_campo_negativo", "itemPedido_vlPctAcrescimo");
		}
	}
	
	protected void validaPctDescontoNegativoOuMaiorQueCem(ItemPedido itemPedido) {
		if (itemPedido.vlPctDesconto < 0) {
			throw new ValidationException("validacao_campo_negativo", "itemPedido_vlPctDesconto");
		}
		if (itemPedido.vlPctDesconto > 100) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[] { StringUtil.getStringValue("100") }));
		}
	}
	
	public void validaCondicaoComercialItemPedido(ItemPedido itemPedido, Vector condicaoComercialList) {
		boolean valido = false;
		for (int i = 0; i < condicaoComercialList.size(); i++) {
			CondicaoComercial condicaoComercial = (CondicaoComercial) condicaoComercialList.items[i];
			if (condicaoComercial.cdCondicaoComercial.equals(itemPedido.cdCondicaoComercial)) {
				valido = true;
			}
		}
		if (!valido) {
			throw new ValidationException(Messages.PEDIDO_MSG_CONDICAO_COMERCIAL_INVALIDA);
		}
	}

	public void validaTabelaPrecoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		Pedido pedidoClone = (Pedido) pedido.clone();
		Vector tabelaPrecoList = new Vector();
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			if (ValueUtil.isNotEmpty(itemPedido.cdCondicaoComercial)) {
				pedidoClone.cdCondicaoComercial = itemPedido.cdCondicaoComercial;
			}
			tabelaPrecoList = TabelaPrecoService.getInstance().carregaTabelaPrecoCondicaoComercial(pedidoClone);
		} else {
			tabelaPrecoList = TabelaPrecoService.getInstance().loadTabelasPrecos(pedidoClone);
		}
		boolean valido = false;
		for (int i = 0; i < tabelaPrecoList.size(); i++) {
			TabelaPreco tabelaPreco = (TabelaPreco) tabelaPrecoList.items[i];
			if (tabelaPreco.cdTabelaPreco.equals(itemPedido.cdTabelaPreco)) {
				valido = true;
			}
		}
		if (!valido) {
			throw new ValidationException(Messages.PEDIDO_MSG_TABELA_PRECO_INVALIDA);
		}
	}
	
	private void validateInsertOrUpdate(final ItemPedido itemPedido) throws SQLException {
		validateProdBloqTabPreco(itemPedido);
		validateMinGradesAndQtMin(itemPedido);
		if (LavenderePdaConfig.isGradeProdutoModoLista() && itemPedido.getProduto().nuNivelGradeObrigatorio >= 1 && itemPedido.getProduto().nuNivelGradeObrigatorio <= 3) {
			validaNivelGradeObrigatorio(itemPedido);
		}
		if (LavenderePdaConfig.isUsaObrigaInfoComplementarItemPedido()) {
			validateCamposObrigatoriosInfosComplementarItemPedido(itemPedido);
		}
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			validateCalculoPrecoPorMetroQuadrado(itemPedido);
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido()) {
			validaComprimentoLargura(itemPedido);	
		}
	}

	public void validateQtNiveisGrade(final ItemPedido itemPedido) throws SQLException {
		DescPromocional descPromocional = itemPedido.getDescPromocional();
		if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) && descPromocional.qtMinVenda <= 0 && descPromocional.qtMinGrade1 <= 0 && descPromocional.qtMinGrade2 <= 0) {
			DescPromocionalGradeService.getInstance().validateQtItemGradeMultiplos(itemPedido.itemPedidoGradeList, itemPedido.descPromocionalGradeList, itemPedido.itemGradeList, itemPedido);
		} else {
			validateGradeAberta(itemPedido, true);
			validateGradeAberta(itemPedido, false);
			try {
				validateQtMinVendaDescPromocional(itemPedido);
			}  catch (ValidationException e) {
				boolean gradeFechadaOk = false; 
				if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional && ValueUtil.isNotEmpty(itemPedido.descPromocionalGradeList) && itemPedido.descPromocional != null) {
					gradeFechadaOk = DescPromocionalGradeService.getInstance().validateQtItemGradeMultiplos(itemPedido.itemPedidoGradeList, itemPedido.descPromocionalGradeList, itemPedido.itemGradeList, itemPedido);
					itemPedido.gradeAbertaValidated = gradeFechadaOk;
				}
				if (!gradeFechadaOk) {
					itemPedido.gradeAbertaValidated = gradeFechadaOk;
					throw e;
				}
			}
		}
		validateQtItensGrade2(itemPedido.itemPedidoGradeList, itemPedido, LavenderePdaConfig.usaQtMinProdTabPrecoEGrupo() ? itemPedido.getProduto().cdGrupoProduto1 : null);
		validateQtItensGrade1(itemPedido.itemPedidoGradeList, itemPedido, LavenderePdaConfig.usaQtMinProdTabPrecoEGrupo() ? itemPedido.getProduto().cdGrupoProduto1 : null);
	}

	private void validateProdBloqTabPreco(final ItemPedido itemPedido) throws SQLException {
		//Produto bloqueado na tabela de preço
		//Se o item possui preço especial referente a cesta promocional não é considerado bloqueado
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda && itemPedido.getItemTabelaPreco().isFlBloqueadoBoolean() && !itemPedido.usaCestaPromo) {
			throw new ValidationException(Messages.ITEMTABELAPRECO_MSG_BLOQUEADO);
		}
	}

	private void validateDescComissao(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
			if (itemPedido.vlPctComissao != 0) {
				DescComiFaixa descComiGrupoItem = itemPedido.descComissaoGrupo;
				if (descComiGrupoItem != null) {
					if (ValueUtil.round(itemPedido.vlPctDesconto) > ValueUtil.round(descComiGrupoItem.vlPctDesconto)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MENOR_DESC_COMISSAO_GRUPO,
								new Object[] { StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto),
										StringUtil.getStringValueToInterface(descComiGrupoItem.vlPctDesconto) }));
					}
				}
			}
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			if (itemPedido.vlPctComissao != 0) {
				DescComiFaixa descComiProdItem = itemPedido.descComissaoProd;
				if (descComiProdItem != null) {
					if (ValueUtil.round(itemPedido.getQtItemFisico()) < ValueUtil.round(descComiProdItem.qtItem)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_MINIMO_PERMITIDO_DESC_COMISSAO,
								new Object[] { Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()),
										Util.getQtItemPedidoFormatted(descComiProdItem.qtItem) }));
					}
					if (ValueUtil.round(itemPedido.vlPctDesconto) > ValueUtil.round(descComiProdItem.vlPctDesconto)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MENOR_DESC_COMISSAO_PROD,
								new Object[] { StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto),
										StringUtil.getStringValueToInterface(descComiProdItem.vlPctDesconto) }));
					}
				}
			}
		}
	}

	public int getQtProdutoDoItemJaInseridoPedido(Pedido pedido, ItemPedido itemPedido, boolean contaGondola) {
		int count = 0;
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidotemp = (ItemPedido) pedido.itemPedidoList.items[i];
				if (isItemPedidoEquals(itemPedido, itemPedidotemp)) {
					count += itemPedidotemp.getQtItemFisico();
					if (contaGondola && itemPedidotemp.getQtItemFisico() == 0 && itemPedidotemp.isGondola()) {
						count++;
					}
				}
			}
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			if (!ValueUtil.isEmpty(pedido.itemPedidoTrocaList)) {
				int size = pedido.itemPedidoTrocaList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedidotemp = (ItemPedido) pedido.itemPedidoTrocaList.items[i];
					if (itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && (itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido))) {
						count += itemPedidotemp.getQtItemFisico();
						if (contaGondola && itemPedidotemp.getQtItemFisico() == 0 && itemPedidotemp.isGondola()) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	private boolean isItemPedidoEquals(ItemPedido itemPedido, ItemPedido itemPedidotemp) {
		boolean itemKitTipo3 = !LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao() || !itemPedidotemp.isKitTipo3();
		return itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && (itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido)) && itemKitTipo3;
	}

	public int getQtProdutoDoItemJaInseridoPedido(Pedido pedido, ItemPedido itemPedido) {
		return getQtProdutoDoItemJaInseridoPedido(pedido, itemPedido, false);
	}
	
	public ItemPedido getProdutoDoKitJaInseridoNoPedido(ItemPedido itemPedidoKit) {
		for (int i = 0; i < itemPedidoKit.itemKitPedidoList.size(); i++) {
			ItemKitPedido itemKitPedido = (ItemKitPedido) itemPedidoKit.itemKitPedidoList.items[i];
			for (int j = 0; j < itemPedidoKit.pedido.itemPedidoList.size(); j++) {
				ItemPedido item = (ItemPedido) itemPedidoKit.pedido.itemPedidoList.items[j];
				if (item.cdProduto.equals(itemKitPedido.cdProduto)) {
					return item;
				}
			}
		}
		return null;
	}
	
	public ItemPedido isProdutoJaInseridoNoPedidoEmUmKit(ItemPedido itemPedido) {
		for (int i = 0; i < itemPedido.pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedidoJaInserido = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			for (int j = 0; j < itemPedidoJaInserido.itemKitPedidoList.size(); j++) {
				ItemKitPedido itemKit = (ItemKitPedido) itemPedidoJaInserido.itemKitPedidoList.items[j];
				if (itemPedido.cdProduto.equals(itemKit.cdProduto)) {
					return itemPedidoJaInserido;
				}
			}
		}
		return null;
	}

	public ItemPedido isProdutoJaInseridoNoPedidoEmUmKitByCdKitPreenchido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemBonificacao()) {
			return null;
		}
		for (int i = 0; i < itemPedido.pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedidoJaInserido = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			if (ValueUtil.isEmpty(itemPedidoJaInserido.cdKit) || itemPedidoJaInserido.isItemBonificacao()) {
				continue;
			}
			if (ValueUtil.valueEquals(itemPedidoJaInserido.cdProduto, itemPedido.cdProduto)) {
				return itemPedidoJaInserido;
			}
		}
		return null;
	}

	public boolean isItemJaInseridoNoPedidoComOutroLote(Pedido pedido, ItemPedido itemPedido) {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			boolean hasMesmoProdutoLoteDiferente = false;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidotemp = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido) && ValueUtil.valueEquals(itemPedidotemp.cdLoteProduto, itemPedido.cdLoteProduto)) {
					return false;
				}
				if (itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido) && !ValueUtil.valueEquals(itemPedidotemp.cdLoteProduto, itemPedido.cdLoteProduto)) {
					hasMesmoProdutoLoteDiferente = true;
				}
			}
			return hasMesmoProdutoLoteDiferente;
		}
		return false;
	}
	
	public boolean isItemJaInseridoNoPedidoComOutraUnidade(Pedido pedido, ItemPedido itemPedido) {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			boolean hasMesmoProdutoUnidadeDiferente = false;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidotemp = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido) && ValueUtil.valueEquals(itemPedidotemp.cdUnidade, itemPedido.cdUnidade)) {
					return false;
				}
				if (itemPedidotemp.cdProduto.equals(itemPedido.cdProduto) && itemPedidotemp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido) && !ValueUtil.valueEquals(itemPedidotemp.cdUnidade, itemPedido.cdUnidade)) {
					hasMesmoProdutoUnidadeDiferente = true;
				}
			}
			return hasMesmoProdutoUnidadeDiferente;
		}
		return false;
	}
	
	public boolean isItemJaInseridoMesmaUnidade(Pedido pedido, ItemPedido item) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList) || item == null) {
			return false;
		}
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidotemp = (ItemPedido) pedido.itemPedidoList.items[i];
			if (ValueUtil.valueEquals(itemPedidotemp.cdProduto, item.cdProduto) && ValueUtil.valueEquals(itemPedidotemp.flTipoItemPedido, item.flTipoItemPedido) && ValueUtil.valueEquals(itemPedidotemp.cdUnidade, item.cdUnidade)) {
				if (LavenderePdaConfig.isConfigGradeProduto()) {
					return item.itemPedidoGradeList.size() == itemPedidotemp.itemPedidoGradeList.size();
				}
				return true;
			}
		}
		return false;
	}
	
	public double getValorTotalTotalizadoresPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			PedidoService.getInstance().findItemPedidoList(pedido);
			boolean itemJaLancado = isItemJaInseridoMesmaUnidade(pedido, itemPedido);
			if (itemJaLancado) {
				if (itemPedido.vlTotalItemPedido > pedido.vlTotalBaseItens) {
					return 0;
				}
				return pedido.vlTotalBaseItens - itemPedido.vlTotalBrutoItemPedido;
			}
		}
		return ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) && pedido.isPedidoTroca() ? pedido.vlTrocaRecolher : pedido.vlTotalBaseItens;
	}
	

	public double getValorTotalGradesJaLancadasPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		double sumVlTotalGradesJaLancadas = 0d;
		if (LavenderePdaConfig.isConfigGradeProduto() && !isItemJaInseridoMesmaUnidade(pedido, itemPedido) && pedido.itemPedidoList != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido itemPedidoTemp = null;
			for (int i = 0; i < size; i++) {
				itemPedidoTemp = (ItemPedido) pedido.itemPedidoList.items[i];
				if (!(ValueUtil.valueEquals(itemPedidoTemp.cdProduto, itemPedido.cdProduto) && ValueUtil.valueEquals(itemPedidoTemp.flTipoItemPedido, itemPedido.flTipoItemPedido) && ValueUtil.valueEquals(itemPedidoTemp.cdUnidade, itemPedido.cdUnidade))) {
					continue;
				} else {
					break;
				}
			}
			if (itemPedidoTemp != null && ValueUtil.valueEquals(itemPedidoTemp.cdProduto, itemPedido.cdProduto) && ValueUtil.valueEquals(itemPedidoTemp.flTipoItemPedido, itemPedido.flTipoItemPedido) && ValueUtil.valueEquals(itemPedidoTemp.cdUnidade, itemPedido.cdUnidade)) {
				if (itemPedido.itemPedidoGradeList.size() > itemPedidoTemp.itemPedidoGradeList.size()) {
					for (int j = 0; j < itemPedido.itemPedidoGradeList.size(); j++) {
						ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[j];
						for (int k = 0; k < itemPedidoTemp.itemPedidoGradeList.size(); k++) {
							ItemPedidoGrade itemPedidoGradeTemp = (ItemPedidoGrade) itemPedidoTemp.itemPedidoGradeList.items[k];
							if (itemPedidoGrade.cdItemGrade1.equals(itemPedidoGradeTemp.cdItemGrade1) && itemPedidoGrade.cdItemGrade2.equals(itemPedidoGradeTemp.cdItemGrade2) && itemPedidoGrade.cdItemGrade3.equals(itemPedidoGradeTemp.cdItemGrade3)) {
								sumVlTotalGradesJaLancadas += itemPedidoGradeTemp.qtItemFisico * itemPedido.vlBaseItemPedido;
							}
						}
					}
				}
			}
		}
		return sumVlTotalGradesJaLancadas;
	}
	
	public int getQtItensDiferentesInseridosNoPedido(Vector itemPedidoList) {
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido || (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) || (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && LavenderePdaConfig.permiteIncluirMesmoProdutoLoteDiferenteNoPedido)) {
			Vector newItemPedidoList = new Vector();
			for (int i = 0; i < itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				boolean itemInseridoNaNovaLista = false;
				for (int j = 0; j < newItemPedidoList.size(); j++) {
					ItemPedido itemPedidoTemp = (ItemPedido) newItemPedidoList.items[j];
					if (ValueUtil.valueEquals(itemPedido.cdProduto, itemPedidoTemp.cdProduto)) {
						itemInseridoNaNovaLista = true;
						break;
					}
					
				}
				if (!itemInseridoNaNovaLista) {
					newItemPedidoList.addElement(itemPedido);
				}
			}
			return newItemPedidoList.size();
		}
		return itemPedidoList.size(); 
	}

	public void insertItemSimples(final ItemPedido itemPedido) throws SQLException {
		validateDuplicated(itemPedido);
		itemPedido.cdUsuario = Session.getCdUsuario();
		calculaMargemContribuicaoItemPedido(itemPedido);
		setDadosAlteracao(itemPedido);
		ItemPedidoAudService.getInstance().insert(itemPedido);
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().geraCreditoInsercaoItemSeNecessario(itemPedido);
		}
		getCrudDao().insert(itemPedido);
		//--
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			ItemPedidoGradeService.getInstance().insert(itemPedido, itemPedido.itemPedidoGradeList);
		}
	}
	
	private void beforeInsertOrUpdate(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			itemPedido.vlItemPedido -= itemPedido.vlDescontoCCP;
			itemPedido.vlTotalItemPedido -= (itemPedido.vlTotalItemPedido * itemPedido.vlPctDescontoCCP) / 100;
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			itemPedido.vlPctMargemProduto = itemPedido.getTabelaPreco() != null && !itemPedido.getTabelaPreco().isAplicaDescQtdeAuto() ? itemPedido.getProduto().vlPctMargemProduto : 0;
		}
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco && itemPedido.getTabelaPreco() != null) {
			itemPedido.cdVerba = itemPedido.getTabelaPreco().cdVerba;
		} else {
			itemPedido.cdVerba = ValueUtil.VALOR_NI;
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito() && itemPedido.getProduto() != null) {
			itemPedido.flRestrito = StringUtil.getStringValue(itemPedido.getProduto().isProdutoRestrito());
		}
		if (LavenderePdaConfig.usaIndicacaoItemPedidoProdutoPromocional && (ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(itemPedido.cdProduto, itemPedido.getItemTabelaPreco().cdTabelaPreco) || itemPedido.getProduto().isProdutoPromocao() || itemPedido.getItemTabelaPreco().isFlPromocao())
				&& !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido)) {
			itemPedido.flPromocao = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			itemPedido.flErroRecalculo = ValueUtil.VALOR_NAO;
		}
		if (itemPedido.pedido != null && LavenderePdaConfig.isUsaReservaEstoqueCentralizado()) {
			itemPedido.cdLocalEstoque = itemPedido.getCdLocalEstoque();
		}
		setValoresUnidadeMedida(itemPedido);
	}
	
	public void setValoresUnidadeMedida(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			itemPedido.nuConversaoUnidadePu = produtoUnidade.nuConversaoUnidade;
			itemPedido.flDivideMultiplicaPu = produtoUnidade.flDivideMultiplica;
			itemPedido.vlIndiceFinanceiroPu = produtoUnidade.vlIndiceFinanceiro;
		}
	}

	public void calculaMargemContribuicaoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra1() && itemPedido.isFlTipoCreditoAlcada()) {
			if (itemPedido.isItemBonificacao()) {
				itemPedido.vlTotalMargemItem = (itemPedido.vlTotalStItem + (itemPedido.getVlPrecoCusto() * itemPedido.getQtItemFisico())) * -1;
			} else {
				itemPedido.vlTotalMargemItem = itemPedido.vlTotalItemPedido - (itemPedido.vlTotalPisItem + itemPedido.vlTotalCofinsItem + itemPedido.vlTotalIcmsItem) - (itemPedido.getVlPrecoCusto() * itemPedido.getQtItemFisico());
			}
		}
	}

	@Override
	public void insert(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		validate(itemPedido);
		validateInsertOrUpdate(itemPedido);
		validateDuplicated(itemPedido);
		itemPedido.pedido.flModoEstoque = itemPedido.pedido.getFlModoEstoque();
		if (LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao() && !itemPedido.isKitTipo3()) {
			ItemPedidoBonifCfgService.getInstance().validateItemPoliticaJaInseridoPedido(itemPedido);
		}
		//--
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			RemessaEstoqueService.getInstance().insertEstoque(itemPedido);
		}
		itemPedido.cdUsuario = Session.getCdUsuario();
		//--
		if (LavenderePdaConfig.usaFaceamento() && (itemPedido.faceamentoEstoque != null)) {
			itemPedido.faceamentoEstoque.flUltilizadoPedidoDtAtual = ValueUtil.VALOR_SIM;
			FaceamentoEstoqueService.getInstance().update(itemPedido.faceamentoEstoque);
		}
		beforeInsertOrUpdate(itemPedido);
		if (itemPedido.isOportunidade()) {
			return;
		}
		if (LavenderePdaConfig.usaIndicacaoItemPedidoProdutoPromocional && (ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(itemPedido.cdProduto, itemPedido.getItemTabelaPreco().cdTabelaPreco) || itemPedido.getProduto().isProdutoPromocao() || itemPedido.getItemTabelaPreco().isFlPromocao())
				&& !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido)) {
			itemPedido.flPromocao = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().geraCreditoInsercaoItemSeNecessario(itemPedido);
		}
		calculaMargemContribuicaoItemPedido(itemPedido);	
		setDadosAlteracao(itemPedido);
		setPrazoEntregaItemPedido(itemPedido);
		ItemPedidoAudService.getInstance().insert(itemPedido);
		getCrudDao().insert(itemPedido);
		//--
		if (LimiteOportunidadeService.getInstance().isPersisteLimiteVendaItensOportunidade(itemPedido)) {
			LimiteOportunidadeService.getInstance().insertVlSaldo(itemPedido);
		}
		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			VerbaService.getInstance().insertVlSaldo(itemPedido.pedido, itemPedido);
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
			RentabilidadeSaldoService.getInstance().insertVlRentabilidadeSaldo(itemPedido);
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
			ItemKitPedidoService.getInstance().insert(itemPedido.itemKitPedidoList, itemPedido.nuSeqProduto, itemPedido.pedido, itemPedido.qtItemFisico);
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			ItemPedidoGradeService.getInstance().insert(itemPedido, itemPedido.itemPedidoGradeList);
		}
		if (LavenderePdaConfig.atualizarEstoqueInterno && !LavenderePdaConfig.usaControleEstoquePorRemessa || 
				itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			if ((!itemPedido.pedido.isIgnoraControleEstoque()) && !(itemPedido.pedido.isPedidoReplicado() && itemPedido.pedido.isIgnoraEstoqueReplicacao())) {
				if (LavenderePdaConfig.isUsaGradeProduto1A4() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
					atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), true, Estoque.FLORIGEMESTOQUE_PDA);
				} else {
					String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && itemPedido.pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA; 
					EstoqueService.getInstance().updateEstoqueInterno(itemPedido, getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico(), false), true, flOrigemEstoque);
					itemPedido.oldQtEstoqueConsumido = getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisicoAtualizaEstoque(), false);
				}
			}
		}
		boolean ignoraEstoque = itemPedido.pedido.getTipoPedido() != null && itemPedido.pedido.getTipoPedido().isIgnoraControleEstoque();
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto() && !ignoraEstoque) {
			LoteProdutoService.getInstance().consomeEstoqueLote(itemPedido);
		}
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			ItemPedidoLogService.getInstance().saveItemPedidoLog(TipoRegistro.INCLUSAO, itemPedido);
		}
		NaoVendaProdPedidoService.getInstance().removeProduto(itemPedido);
		marcaItemPedidoPorMotivoPendencia(itemPedido);
	}
	
	private void setPrazoEntregaItemPedido(ItemPedido itemPedido) throws SQLException {
		if(!LavenderePdaConfig.calculaPrazoEntregaPorProduto) {
			return;
		}
		Vector prazoEntregaVector = PrazoEntregaService.getInstance().findPrazoEntregaByItemPedido(itemPedido.cdProduto, itemPedido.getQtItemLista());
		if(ValueUtil.isEmpty(prazoEntregaVector)) {
			itemPedido.isPedidoSemPrazoCadastrado = true;
			itemPedido.nuDiasPrazoEntrega = 0;
			return;
		}
		PrazoEntrega prazoEntregaItem = (PrazoEntrega) prazoEntregaVector.items[0];
		itemPedido.nuDiasPrazoEntrega = prazoEntregaItem.nuDiasEntrega;
	}

	public void atualizaPctFreteItens(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido == null || tipoPedido.isIgnoraCalculoFrete() || !LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || !LavenderePdaConfig.isConfigCalculoPesoPedido() || !LavenderePdaConfig.isUsaTipoFretePedido()) return;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
			if (LavenderePdaConfig.usaGradeProduto4()) {
				atualizaPctFreteItemAgrupado(pedido, item);
			} else {
				atualizaPctFreteItemNormal(pedido, item);
			}
		}
	}

	private void atualizaPctFreteItemAgrupado(Pedido pedido, ItemPedido item) throws SQLException {
		Vector listItensPedidoAgrupado = item.itemPedidoPorGradePrecoList;
		if (listItensPedidoAgrupado == null) return;
		int size = listItensPedidoAgrupado.size();
		for (int i = 0; i < size; i++) {
			atualizaPctFreteItemNormal(pedido, (ItemPedido) listItensPedidoAgrupado.elementAt(i));
		}
	}

	private void atualizaPctFreteItemNormal(Pedido pedido, ItemPedido item) throws SQLException {
		if (isAtualizaPctFrete(pedido, item)) {
			alteraTipoEdicaoDescAcres(item);
			PedidoService.getInstance().calculateItemPedido(item.pedido, item, false);
			getCrudDao().update(item);
		}
	}

	private void alteraTipoEdicaoDescAcres(ItemPedido item) {
		if (item.vlPctDesconto != 0) {
			item.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		} else if (item.vlPctAcrescimo != 0) {
			item.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
		}
	}

	private boolean isAtualizaPctFrete(Pedido pedido, ItemPedido item) {
		return (pedido.qtPeso >= item.qtPesoTipoFreteTabPrecoPosterior && item.qtPesoTipoFreteTabPrecoPosterior != item.qtPesoTipoFreteTabPrecoAtual) || 
						pedido.qtPeso < item.qtPesoTipoFreteTabPrecoAtual || item.qtPesoTipoFreteTabPrecoAtual == -1 || item.qtPesoTipoFreteTabPrecoPosterior == -1;
	}

	@Override
	public void validateDuplicated(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (findColumnByRowKey(itemPedido.getRowKey(), "ROWKEY") != null) {
			if (LavenderePdaConfig.usaGradeProduto1()) {
				if (itemPedido.itemPedidoGradeList.size() > 0) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeNivel1((ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[0], itemPedido.getCdTabelaPreco())));
				} else {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, ItemPedidoGradeService.getInstance().getDescricaoItemPedidoNivel1(itemPedido)));
				}
			} else {
				if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, itemPedido.getDsProduto() + " (" + UnidadeService.getInstance().getDsUnidade(itemPedido.cdUnidade) + ")"));
				} else if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && LavenderePdaConfig.permiteIncluirMesmoProdutoLoteDiferenteNoPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, itemPedido.getDsProduto() + " (Lote: " + itemPedido.cdLoteProduto + ")"));
				} else {
					if (!LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido && !isPermiteInclusaoItemPedidoParaKitFechado(itemPedido)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, itemPedido.getDsProduto()));
					}
				}
			}
		}
		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			for (int i = 0; i < itemPedido.itemPedidoPorGradePrecoList.size(); i++) {
				ItemPedido itemPedidoGrade = (ItemPedido)itemPedido.itemPedidoPorGradePrecoList.items[i];
				if (findColumnByRowKey(itemPedidoGrade.getRowKey(), "ROWKEY") != null) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, itemPedido.getProduto().dsProduto));
				}
			}
		}
		if (LavenderePdaConfig.usaOportunidadeVenda && !itemPedido.pedido.isOportunidade()) {
			int size = itemPedido.pedido.itemPedidoOportunidadeList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido item = (ItemPedido) itemPedido.pedido.itemPedidoOportunidadeList.items[i];
				if (itemPedido.getRowKey().equals(item.getRowKey())) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO, itemPedido.getDsProduto()));
				}
			}
		}
		validaKitIncluidoNoPedido(itemPedido);
	}

	public void validaKitIncluidoNoPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			return;
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			if (itemPedido.getProduto().isKit()) {
				ItemPedido itemPedidoJaInserido = getProdutoDoKitJaInseridoNoPedido(itemPedido);
				if (itemPedidoJaInserido != null) {
					throw new ItemKitPedidoInseridoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_ITEM_KIT_JA_INCLUIDO_PEDIDO, itemPedidoJaInserido.getDsProduto()));
				}
			} else {
				ItemPedido itemPedidoKitJaInserido = isProdutoJaInseridoNoPedidoEmUmKit(itemPedido);
				if (itemPedidoKitJaInserido != null) {
					throw new ItemKitPedidoInseridoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_JA_INSERIDO_KIT, new Object[] {itemPedido.getDsProduto(), itemPedidoKitJaInserido.getDsProduto()}));
				}
			}
		} else {
			ItemPedido itemPedidoKitJaInserido = isProdutoJaInseridoNoPedidoEmUmKitByCdKitPreenchido(itemPedido);
			if (itemPedidoKitJaInserido != null) {
				throw new ItemKitPedidoInseridoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_JA_INSERIDO_KIT, new Object[] {itemPedido.getDsProduto().toUpperCase(), itemPedidoKitJaInserido.cdKit}));
			}
		}
	}
	

	private boolean  isPermiteInclusaoItemPedidoParaKitFechado(ItemPedido itemPedido) {
		if (LavenderePdaConfig.isUsaKitProdutoFechado() && itemPedido.isFazParteKitFechado()) {
			Vector itemPedidoList = itemPedido.pedido.itemPedidoList;
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoAdicionado = (ItemPedido) itemPedidoList.items[i];
				if (itemPedido.isFazParteKitFechado() && ValueUtil.valueEquals(itemPedido, itemPedidoAdicionado)) {
					return true;
				}
			}
		}
		return false;
	}

	protected void updateItensPedido(Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			ItemPedidoPdbxDao.getInstance().update(itemPedido);
			updateVlVerbaSaldoItemPedido(itemPedido.pedido, itemPedido);
		}
	}

	public void updateItemSimples(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		calculaMargemContribuicaoItemPedido(itemPedido);
		if (!emTransacao) {
			CrudDbxDao.getCurrentDriver().startTransaction();
		}
		try {
			if (itemPedido.nuConversaoUnidadePu == 0) {
				setValoresUnidadeMedida(itemPedido);
			}
			ItemPedidoAudService.getInstance().update(itemPedido);
			getCrudDao().update(domain);
			//--
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				ItemPedidoGradeService.getInstance().update(itemPedido, itemPedido.itemPedidoGradeList);
			}
			if (!emTransacao) {
				CrudDbxDao.getCurrentDriver().commit();
			}
		} catch (Throwable ex) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_INSERIR_ITEM_VENDA, ex.getMessage()));
		} finally {
			if (!emTransacao) {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
		}
		
	}

	@Override
	public void update(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		validate(itemPedido);
		validateInsertOrUpdate(itemPedido);
		validateItemDescCascataCategoria(itemPedido);
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			RemessaEstoqueService.getInstance().updateEstoque(itemPedido);
		}
		//--
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().atualizaQtdCreditoPedido(itemPedido);
		}
		calculaMargemContribuicaoItemPedido(itemPedido);
		if (!emTransacao) {
			CrudDbxDao.getCurrentDriver().startTransaction();
		}
		try {
			beforeInsertOrUpdate(itemPedido);
			setDadosAlteracao(itemPedido);
			setPrazoEntregaItemPedido(itemPedido);
			if (LavenderePdaConfig.isPermiteEditarDescontoOuQuantidade()) {
				itemPedido.itemChanged = true;
			}
			if (LavenderePdaConfig.usaOportunidadeVenda && !itemPedido.pedido.isOportunidade()) {
				if (updateItemOportunidade(itemPedido)) {
					CrudDbxDao.getCurrentDriver().commit();
					return;
				}
			} else {
				ItemPedidoAudService.getInstance().update(itemPedido);
				getCrudDao().update(itemPedido);
			}
			//--
			if (LimiteOportunidadeService.getInstance().isPersisteLimiteVendaItensOportunidade(itemPedido)) {
				LimiteOportunidadeService.getInstance().updateVlSaldo(itemPedido);
			}
			updateVlVerbaSaldoItemPedido(itemPedido.pedido, itemPedido);
			//--
			if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
				RentabilidadeSaldoService.getInstance().updateVlRentabilidadeSaldo(itemPedido);
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
				ItemKitPedidoService.getInstance().update(itemPedido.itemKitPedidoList, itemPedido.qtItemFisico);
			}
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				if (LavenderePdaConfig.atualizarEstoqueInterno && (itemPedido.pedido.getTipoPedido() == null || !itemPedido.pedido.isIgnoraControleEstoque(itemPedido))  && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
					atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), false, Estoque.FLORIGEMESTOQUE_PDA);
				}
				ItemPedidoGradeService.getInstance().update(itemPedido, itemPedido.itemPedidoGradeList);
			}
			if (LavenderePdaConfig.atualizarEstoqueInterno || itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
				if (itemPedido.pedido.getTipoPedido() == null  || !itemPedido.pedido.isIgnoraControleEstoque()) {
					if (LavenderePdaConfig.isUsaGradeProduto1A4() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
						atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), true, Estoque.FLORIGEMESTOQUE_PDA);
					} else {
						String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && itemPedido.pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
						EstoqueService.getInstance().updateEstoqueInterno(itemPedido, itemPedido.oldQtEstoqueConsumido, false, flOrigemEstoque);
						EstoqueService.getInstance().updateEstoqueInterno(itemPedido, getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico(), false), true, flOrigemEstoque);
						itemPedido.oldQtEstoqueConsumido = getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico(), false);
					}
				}
			}
			boolean ignoraEstoque = itemPedido.pedido.getTipoPedido() != null && itemPedido.pedido.getTipoPedido().isIgnoraControleEstoque();
			if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto() && !ignoraEstoque) {
				LoteProdutoService.getInstance().consomeEstoqueLote(itemPedido);
			}
			if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
				ItemPedidoLogService.getInstance().saveItemPedidoLog(TipoRegistro.ALTERACAO, itemPedido);
			}
			marcaItemPedidoPorMotivoPendencia(itemPedido);
		} catch (Throwable ex) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_INSERIR_ITEM_VENDA, ex.getMessage()));
		} finally {
			if (!emTransacao) {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
		}
		
	}
	
	public void aplicaDescQtdSimilares(ItemPedido itemPedido) throws SQLException {
		String cdTabelaPreco = LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() ? itemPedido.getCdTabelaPreco() : TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		String cdProduto = DescQuantidadeService.getInstance().getCdProdutoDescQtd(cdTabelaPreco, itemPedido.cdProduto, itemPedido);
		if (ValueUtil.isNotEmpty(cdProduto)) {
			Vector listCdsProdutoSimilares = DescQtdeAgrSimilarService.getInstance().findDescSimilaresByFilter(itemPedido, cdProduto, LavenderePdaConfig.permiteTabPrecoItemDiferentePedido());
			if (ValueUtil.isNotEmpty(listCdsProdutoSimilares)) {
				int size = itemPedido.pedido.itemPedidoList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido item = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
					if (listCdsProdutoSimilares.contains(item.cdProduto)) {
						item.atualizandoDesc = true;
						item.flTipoEdicao = 0;
						calculate(item, item.pedido);
						update(item);
						item.flTipoEdicao = 0;
					}
				}
			}
		}
	}

	private void updateVlVerbaSaldoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (VerbaService.getInstance().isVerbaPersonalizadaGrupo() && !itemPedido.flUIChange) {
			VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, true);
		}
		if (!LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
			if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
				if ((itemPedido.vlVerbaItem != 0) || (itemPedido.vlVerbaItemOld != 0) || (LavenderePdaConfig.geraVerbaPositiva && ((itemPedido.vlVerbaItemPositivo != 0) || (itemPedido.vlVerbaItemPositivoOld != 0)))) {
					if (itemPedido.pedido.getTipoPedido() == null || !itemPedido.pedido.getTipoPedido().isIgnoraControleVerba()) {
		    			VerbaService.getInstance().updateVlSaldo(pedido, itemPedido);
		    		}
				}
			}
		}
	}

	private boolean updateItemOportunidade(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isOportunidade()) {
			if (itemPedido.pedido.itemPedidoList.contains(itemPedido)) {
				delete(itemPedido);
			}
			itemPedido.pedido.itemPedidoOportunidadeList.removeElement(itemPedido);
			return true;
		} else {
			if (itemPedido.pedido.itemPedidoOportunidadeList.contains(itemPedido)) {
				itemPedido.pedido.itemPedidoOportunidadeList.removeElement(itemPedido);
				insert(itemPedido);
				return true;
			} else {
				getCrudDao().update(itemPedido);
				return false;
			}
		}
	}

	public void deleteItemSimples(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		getCrudDao().delete(itemPedido);
		ItemPedidoAudService.getInstance().delete(itemPedido);
		//--
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			ItemPedidoGradeService.getInstance().delete(itemPedido.itemPedidoGradeList);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			try {
				SolAutorizacaoService.getInstance().deleteAllByItemPedido(itemPedido);
				itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
				if (itemPedido.pedido != null) {
					itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(itemPedido.pedido);
				}
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ERRO_EXCLUIR_SOL_AUT, ex.getMessage()));
	}
		}
	}

	@Override
	public void delete(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		try {
			getCrudDao().delete(itemPedido);
		} catch (ApplicationException e) {
			if (findByPrimaryKey(itemPedido) != null) {
				throw e;
			}
		}
		ItemPedidoAudService.getInstance().delete(itemPedido);
		//--
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
			RentabilidadeSaldoService.getInstance().deleteVlRentabilidadeSaldo(itemPedido);
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
			ItemKitPedidoService.getInstance().delete(itemPedido.itemKitPedidoList);
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa && itemPedido.pedido != null && !itemPedido.pedido.isPedidoTransmitido()) {
			RemessaEstoqueService.getInstance().deleteEstoqueConsumido(itemPedido, Estoque.FLORIGEMESTOQUE_PDA);
		}
		final Pedido pedido = itemPedido.pedido;
		if (( !pedido.isPedidoTransmitido() && LavenderePdaConfig.atualizarEstoqueInterno) || pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			if (LavenderePdaConfig.isUsaGradeProduto1A4() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
				atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), false, Estoque.FLORIGEMESTOQUE_PDA);
			} else {
				if (itemPedido.pedido.getTipoPedido() == null || !pedido.isIgnoraControleEstoque() && !(pedido.isPedidoReplicado() && pedido.getTipoPedido().isIgnoraEstoqueReplicacao()) && !ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEstReabrePedido) && !pedido.isPedidoBonificacao()) {
					itemPedido.oldQtEstoqueConsumido = itemPedido.oldQtEstoqueConsumido == 0 ? getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisicoAtualizaEstoque(), false) : itemPedido.oldQtEstoqueConsumido;
					String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
					EstoqueService.getInstance().updateEstoqueInterno(itemPedido, itemPedido.oldQtEstoqueConsumido, false, flOrigemEstoque);
				}
			}
		}
		boolean ignoraEstoque = itemPedido.pedido.getTipoPedido() != null && itemPedido.pedido.getTipoPedido().isIgnoraControleEstoque();
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()  && !ignoraEstoque) {
			LoteProdutoService.getInstance().removeEstoqueConsumido(itemPedido);
		}
		if (LavenderePdaConfig.isUsaGradeProduto1A4()) {
			if (itemPedido.itemPedidoGradeList.size() == 0) {
				ItemPedidoGradeService.getInstance().findItemPedidoGradeList(itemPedido);
			}
			ItemPedidoGradeService.getInstance().deleteByItemPedido(itemPedido);
		}
		if (LimiteOportunidadeService.getInstance().isPersisteLimiteVendaItensOportunidade(itemPedido)) {
			LimiteOportunidadeService.getInstance().deleteVlSaldo(itemPedido);
		}
		if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto() && ValueUtil.isNotEmpty(itemPedido.cdGrupoDescCli) && ValueUtil.isNotEmpty(itemPedido.cdGrupoDescProd) && !pedido.deletadoPelaIntefacePedido) {
			atualizaDescontosInterpolacaoPedido(pedido, itemPedido);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			boolean enviouSolAutorizacaoEItemPedido = false;
			try {
				enviouSolAutorizacaoEItemPedido = SolAutorizacaoService.getInstance().deleteAllByItemPedido(itemPedido);
				itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
				if (itemPedido.pedido != null) {
					itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(itemPedido.pedido);
				}
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ERRO_EXCLUIR_SOL_AUT, ex.getMessage()));
			}
			if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && !enviouSolAutorizacaoEItemPedido) {
				enviaAtualizacao(itemPedido);
			}
		}
	}
	
	public void atualizaDescontosInterpolacaoPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			PedidoService.getInstance().findItemPedidoList(pedido);
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		if (itemPedido != null) pedido.itemPedidoList.removeElement(itemPedido);
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoValue = (ItemPedido)pedido.itemPedidoList.items[i];
			itemPedidoValue.loadDescPromocional(true);
			if (!itemPedidoValue.recalculaDescPromo) continue;
			itemPedidoValue.recalculaDescPromo = false;
			PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedidoValue);
			calculate(itemPedidoValue, pedido);
			update(itemPedidoValue);
		}
	}
	
	protected void cancelaItensPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.atualizarEstoqueInterno) {
			if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
				atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), false, Estoque.FLORIGEMESTOQUE_PDA);
			} else {
				if (itemPedido.pedido.getTipoPedido() == null || !itemPedido.pedido.isIgnoraControleEstoque(itemPedido) && !(itemPedido.pedido.isPedidoReplicado() && itemPedido.pedido.getTipoPedido().isIgnoraEstoqueReplicacao()) && !ValueUtil.VALOR_SIM.equals(itemPedido.pedido.flSituacaoReservaEstReabrePedido)) {
					itemPedido.oldQtEstoqueConsumido = itemPedido.oldQtEstoqueConsumido == 0 ? getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisicoAtualizaEstoque()) : itemPedido.oldQtEstoqueConsumido;
					String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && itemPedido.pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
					EstoqueService.getInstance().updateEstoqueInterno(itemPedido, itemPedido.oldQtEstoqueConsumido, false, flOrigemEstoque);
				}
			}
		}
		VerbaService.getInstance().deleteVlSaldo(pedido, itemPedido);
		ProdutoFaltaService.getInstance().deleteProdutoFalta(itemPedido);
	}
	

	private void validateCalculate(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		//Produto
		if (itemPedido.getProduto() == null) {
			throw new ValidationException(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.PRODUTO_NOME_ENTIDADE));
		}
		if (!pedido.isPedidoBonificacao() && (LavenderePdaConfig.usaUnidadeAlternativa && (ValueUtil.isEmpty(itemPedido.cdUnidade) || ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO.equals(itemPedido.cdUnidade)))) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_UNIDADE_ALTERNATIVA);
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && !LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido);
		}
		//Item da tabela de preço
		validateItemTabelaPreco(pedido, itemPedido);
		//Item do Kit de Produtos
		if (LavenderePdaConfig.isUsaKitProdutoAberto()) {
			ItemKitService.getInstance().validateMinMaxItem(itemPedido, pedido);
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade && LavenderePdaConfig.usaUnidadeAlternativa) {
			if (itemPedido.getProdutoUnidade() != null) {
				if (RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, itemPedido.getProdutoUnidade().cdUnidade, pedido.cdTipoPedido)) {
					throw new ValidationException(Messages.RESTRICAOVENDAUN_MSG_UN_RESTRITA);
				}
			}
		}
		validateRestricaoQuantidade(itemPedido, pedido);
		//Valida produto controlado com alvara
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
		}
		
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			validaDescontoMaximoCondPag(pedido.getCondicaoPagamento(), itemPedido.vlPctDescCondPagto);
		}
	}
	
	private void validaDescontoMaximoCondPag(CondicaoPagamento condicao, double value) throws SQLException {
		if (condicao == null || condicao.vlPctMaxDesconto <= 0 || value <= condicao.vlPctMaxDesconto) return;
		throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_DESCONTO_MAXIMO_COND_PAGTO_ULTRAPASSADO, new String[] {StringUtil.getStringValue(value), StringUtil.getStringValue(condicao.vlPctMaxDesconto)}));
	}
	
	public void validateItemTabelaPreco(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (!itemPedido.usaCestaPromo && !itemPedido.possuiItemTabelaPreco()) {
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				lancaExcessaoPrecoUnidadeQtdPrazo(itemPedido, pedido);
			}
			throw new ProdutoSemPrecoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_SEM_PRECO, itemPedido.cdProduto));
		}
	}

	private void lancaExcessaoPrecoUnidadeQtdPrazo(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento();
		condicaoPagamentoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		condicaoPagamentoFilter.cdEmpresa = pedido.cdEmpresa;
		condicaoPagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
		String dsCondicaoPagamento = CondicaoPagamentoService.getInstance().findColumnByRowKey(condicaoPagamentoFilter.getRowKey(), "dsCondicaoPagamento");
		if (ValueUtil.isEmpty(dsCondicaoPagamento)) {
			dsCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
		Unidade unidade = new Unidade();
		unidade.cdEmpresa = pedido.cdEmpresa;
		unidade.cdRepresentante = pedido.cdRepresentante;
		unidade.cdUnidade = itemPedido.cdUnidade;
		String dsUnidade = UnidadeService.getInstance().findColumnByRowKey(unidade.getRowKey(), "dsUnidade");
		if (ValueUtil.isEmpty(dsUnidade)) {
			dsUnidade = itemPedido.getProduto().cdUnidade;
		}
		Vector params = new Vector();
		params.addElement(itemPedido.cdProduto);
		params.addElement(dsCondicaoPagamento);
		params.addElement(dsUnidade);
		params.addElement(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()));
		throw new ProdutoSemPrecoException(MessageUtil.getMessage(Messages.ERRO_PRODUTO_SEM_PRECO, params.items));
	}

	//Quantidade máxima do mesmo produto no pedido
	protected void validateRestricaoQuantidade(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.validaQtMaxVendaPorGrade && !itemPedido.cdItemGrade1.equals("0")) return;
		
		if ((itemPedido.getItemTabelaPreco().qtMaxVenda != 0) && (!ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha))) {
			if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedido, pedido))) {
				//verificar todos os itens do pedido e ver se já existe algum com restrição, se sim, lança exception, pois só permite um item com restrição por pedido
				if (LavenderePdaConfig.usaApenasUmProdutoQuantidadeMaxVendaNoPedido && existeItemRestricaoQuantidadeAdicionadoPedido(pedido, itemPedido)) {
					throw new ValidationException(Messages.ITEMPEDIDO_MSG_QT_MAX_ITEMRESTRICAO);
				}
				double qtMaxPorItem = itemPedido.getItemTabelaPreco().qtMaxVenda;
				if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.aplicaConversaoUnidadeNaValidacaoQtMaxVenda) {
					ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
					if (produtoUnidade.isMultiplica()) {
						qtMaxPorItem /= produtoUnidade.nuConversaoUnidade;
					} else {
						qtMaxPorItem *= produtoUnidade.nuConversaoUnidade;
					}
					qtMaxPorItem = ValueUtil.getDoubleValueTruncated(qtMaxPorItem, 0);
				}
				if ((qtMaxPorItem > 0) && (itemPedido.getQtItemFisico() > qtMaxPorItem)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTD_MAX_PERMITIDO_POR_TABELAPRECO, new String[] { StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), StringUtil.getStringValueToInterface(qtMaxPorItem, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface) }));
				}
			}
		}
		//Controle para não adicionar itens com restrição em um pedido que há itens promocionais e vice-versa
		if ((LavenderePdaConfig.qtdMinItensParaPermitirItensPromocionais > 0) && (!ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha))) {
			if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedido, pedido))) {
				if (itemPedido.getItemTabelaPreco().qtMaxVenda > 0) {
					if (existeProdutoPromocionalPedido(pedido)) {
						throw new ValidationException(Messages.ITEMPEDIDO_MSG_INFO_ADD_ITEMRESTRICAO_EM_PEDIDOPROMOCAO);
					}
				} else if (itemPedido.getItemTabelaPreco().isFlPromocao()) {
					if (existeItemRestricaoQuantidadeAdicionadoPedido(pedido, itemPedido)) {
						throw new ValidationException(Messages.ITEMPEDIDO_MSG_INFO_ADD_PROMOCAO_EM_PEDIDORESTRICAO);
					}
				}
			}
		}
	}

	public double findVlPctMaxDescontoRep() throws SQLException {
		Representante representanteFilter = new Representante();
		if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.validaPctMaxDescPorRepresentante == 1) {
			representanteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		} else {
			representanteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		Representante representante = (Representante) RepresentanteService.getInstance().findByRowKey(representanteFilter.getRowKey());
		return ValueUtil.round(representante.vlPctMaxDesconto);
	}

	public void validatePctMaxDescRep(ItemPedido itemPedido, double vlPctMaxDescRep, double vlBaseCalculo) throws SQLException {
		if (LavenderePdaConfig.validaPctMaxDescPorRepresentante > 0) {
			vlPctMaxDescRep = ValueUtil.round(vlPctMaxDescRep);
			double vlPctDescontoItem = itemPedido.vlPctDesconto;
			double vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			double vlMinPermitidoItem = ValueUtil.round(vlBaseCalculo);
			vlMinPermitidoItem *= 1 - (vlPctMaxDescRep / 100);
			vlMinPermitidoItem = getVlItemPedidoMaxDescAcrescPermitido(itemPedido, vlMinPermitidoItem, vlPctMaxDescRep, true);
			vlMinPermitidoItem = ValueUtil.round(vlMinPermitidoItem);
			if (vlPctDescontoItem > 0.0) {
				if (vlPctDescontoItem > vlPctMaxDescRep) {
					double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDescRep / 100);
					throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(vlPctMaxDescRep) }), vlPermitido);
				} 
				if (vlItemPedido < vlMinPermitidoItem) {
					throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO, new String[] { StringUtil.getStringValueToInterface(vlItemPedido), StringUtil.getStringValueToInterface(vlMinPermitidoItem) }), vlMinPermitidoItem);
				} 
			}
		}
	}

	public boolean existeItemRestricaoQuantidadeAdicionadoPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha)) {
			return false;
		}
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[i];
			if ((itemPedidoAux.getItemTabelaPreco().qtMaxVenda > 0) && !itemPedidoAux.equals(itemPedido) && !(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedidoAux, pedido))) {
				return true;
			}
		}
		return false;
	}

	public boolean existeProdutoPromocionalPedido(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getItemTabelaPreco().isFlPromocao() && ((itemPedido.getItemTabelaPreco().qtMaxVenda == 0) && !(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedido, pedido)))) {
				return true;
			}
		}
		return false;
	}

	public void calculateSimples(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		//Valida os campos obrigatórios
		validateCalculate(itemPedido, pedido);
		//Verifica questões de conversão de unidades
		aplicarConversaoUnidadeMedida(itemPedido, pedido);
		//--
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
		//Valor total do item
		itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		//--
		itemPedido.vlTotalBrutoItemPedido = ValueUtil.round(itemPedido.getQtItemFisico() * itemPedido.vlBaseItemPedido);

		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			itemPedido.qtPeso = getPesoItemPedido(itemPedido);
		}
		//--
		validateItemPedido(itemPedido);
	}
	
	public void calculate(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && itemPedido.isItemVendaNormal() && itemPedido.permiteAplicarDescProgressivoPersonalizado()) {
			DescProgConfigFaDes descProgConfigFaDes = DescProgressivoConfigService.getInstance().getDescProgConfigFaDesItem(itemPedido);
			if (itemPedido.vlPctFaixaDescQtd > 0 && descProgConfigFaDes != null) {
				reverteDescQtd(itemPedido);
			}
			DescProgressivoConfigService.getInstance().processaDescontoProgressivo(itemPedido, descProgConfigFaDes);
		}
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && itemPedido.permiteAplicarDesconto() && !itemPedido.possuiDescMaxProdCli()) {
			aplicaMaiorDescontoCascataItem(itemPedido);
		}
		if ((pedido.isPedidoReplicado() || ValueUtil.isNotEmpty(pedido.flOrigemPedidoSugestao)) && ValueUtil.isEmpty(itemPedido.cdTabelaPreco) && ValueUtil.isNotEmpty(pedido.cdTabelaPreco)) {
			itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		//Valida os campos obrigatórios
		validateCalculate(itemPedido, pedido);
		//Verifica questões de conversão de unidades
		aplicarConversaoUnidadeMedida(itemPedido, pedido);
		if (!itemPedido.usaCestaPromo) {
			//Aplica o desconto por peso
			if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlItemPedido() && !itemPedido.hasDescProgressivo() && !produtoDescQuantidadePesoBloqueado(pedido, itemPedido)) {
				aplicaDescontoQtdPesoNoVlItemPedido(pedido, itemPedido);
			}
			//Aplica o desconto por quantidade
			if (!itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, null) && !itemPedido.hasDescProgressivo() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_QNT_ITEM)) {
				calculaDescQtdItemPedido(pedido, itemPedido, false);
			}
			//Desconto Promocional por Quantidade
			if (!ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL)) {
				calculaDescPromocionalPorQtde(pedido, itemPedido);
			}
			//valor bruto do item
			itemPedido.vlTotalBrutoItemPedido = ValueUtil.round(itemPedido.getQtItemFisico() * itemPedido.vlBaseItemPedido);
			//valor unitário baseado no % de acréscimo
			if (itemPedido.isEditandoAcrescimoPct() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(), Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
				aplicaAcrescimoItemPedido(itemPedido);
			}
			//valor unitário baseado no % de Desconto
			if (itemPedido.isEditandoDescontoPct() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
				aplicaDescontoItemPedido(itemPedido);
			}
			if ((itemPedido.isEditandoQtd() && !LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) || LavenderePdaConfig.usaGradeProduto4() ) {
				if (itemPedido.vlPctAcrescimo > 0 && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(), Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
					aplicaAcrescimoItemPedido(itemPedido);
				} else if (permiteAplicarDescontoItemPedido(itemPedido) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
					aplicaDescontoItemPedido(itemPedido);
				}
			}
			//valor unitário baseado no % de DescontoProgressivo
			if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido() && !LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido()) {
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
			}
			if (LavenderePdaConfig.isUsaPrecoBaseItemBonificado() && itemPedido.isItemBonificacao()) {
				if (LavenderePdaConfig.isUsaPrecoBaseItemPedidoPrecoBonificado()) {
					itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				} else if (LavenderePdaConfig.isUsaPrecoBasePorRedutorCliente()) {
					itemPedido.vlItemPedido = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
				} else {
					itemPedido.vlItemPedido = itemPedido.getItemTabelaPreco().vlBase;
				}
				itemPedido.vlPctDesconto = 0;
				itemPedido.vlPctAcrescimo = 0;
			}
			calculaDescontoVolumeVendaMensal(pedido, itemPedido);
		}
		calculaValorItemBaseadoNoVlElementar(itemPedido);
		//% de desconto
		if (itemPedido.isEditandoValorItem() && !(LavenderePdaConfig.isUsaPrecoBaseItemBonificado() && itemPedido.isItemBonificacao())) {
			calculaPctAcrescimoDescontoItemPedido(itemPedido, pedido);
			if (!itemPedido.usaCestaPromo && !itemPedido.hasDescProgressivo()) {
				//Aplica o desconto por quantidade
				calculaDescQtdItemPedido(pedido, itemPedido, true);
			}
		}
		itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido);
		aplicaDescontoExtra(itemPedido);
		if (itemPedido.vlPctDesconto > 0) {
			aplicaDescontosEmCascataNoItemPedidoRegra1(itemPedido);
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			aplicaDescontosEmCascataNoItemPedidoRegra2(itemPedido, pedido);
		}
		aplicaDescontosAutoEmCascataNaCapaPedidoPorItem(itemPedido, pedido);
		aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(itemPedido, pedido);
		applyRedutorOptanteSimplesAposCalculoValorItem(itemPedido, pedido.getCliente());
		aplicaDescontoMaximoPorCondicaoPagamento(itemPedido);
		if (LavenderePdaConfig.aplicaApenasDescQtdOuIndiceCondPagto && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.isAplicaIndiceCondPagtoPorDescQtdIgnorado) {
			applyIndiceFinanceiroCondPagto(itemPedido, pedido);
			itemPedido.isAplicaIndiceCondPagtoPorDescQtdIgnorado = false;
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit > 0) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
		}
		//Valor total do item
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			loadDadosItemPedido(itemPedido, pedido);
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		} else {
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			loadDadosItemPedido(itemPedido, pedido);
		}
		//Carrega a verba
		if (!itemPedido.usaCestaPromo && !itemPedido.isCombo() && !itemPedido.isKitTipo3() && (!LavenderePdaConfig.usaGerenciaDeCreditoDesconto || itemPedido.qtdCreditoDesc == 0 || itemPedido.isFlTipoCadastroQuantidade())) {
			if (LavenderePdaConfig.informaVerbaManual && !pedido.isIgnoraControleVerba() && !itemPedido.isIgnoraControleVerba()) {
				itemPedido.vlVerbaItem = (itemPedido.vlVerbaManual * itemPedido.getQtItemFisico()) * -1;
				itemPedido.vlBaseFlex = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
				if (LavenderePdaConfig.isMostraFlexPositivoPedido() && VerbaService.getInstance().consomeVerbaGrupoSaldoPrecoLiberadoSenha(itemPedido)) {
					VerbaService.getInstance().aplicaVerbaPositiva(itemPedido, pedido);
				}
			}
			if (!LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido) {
				VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
			}
			if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
				VerbaService.getInstance().aplicaVerbaPorGrupoProdComTolerancia(pedido, itemPedido);
			}
			if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor() && LavenderePdaConfig.geraVerbaPositiva) {
				itemPedido.vlVerbaItemPositivo = VerbaFornecedorService.getInstance().getVlVerbaFornecedorPositiva(itemPedido);
			}
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			PontuacaoService.getInstance().aplicaPontosItem(itemPedido);
		}
		// Carrega a comissão do item do Pedido
		if ((LavenderePdaConfig.usaConfigCalculoComissao() ||
				LavenderePdaConfig.usaDescontoComissaoPorGrupo ||
				LavenderePdaConfig.usaDescontoComissaoPorProduto ||
				LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() ||
				LavenderePdaConfig.aplicaComissaoEspecialProdutoPromocional ||
				LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo())) {
			calculateComissaoItem(itemPedido, pedido);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !pedido.isPedidoBonificacao()) {
			calculaRentabilidadeItem(itemPedido, pedido);
			calculaDescontoComissao(itemPedido, pedido);
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			calculaRentabilidade(pedido, itemPedido);
			calculaDescontoComissao(itemPedido, pedido);
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
			calculaIndiceRentabilidadeItem(itemPedido, pedido);
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado && LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			calculaIndiceRentabilidadeItemSemTributos(itemPedido);
		}
		// Ignora validação do valor do item
		if ((LavenderePdaConfig.liberaComSenhaPrecoDeVenda &&
						pedido.isFlPrecoLiberadoSenha()) ||
						(LavenderePdaConfig.liberaComSenhaPrecoProduto && itemPedido.isFlPrecoLiberadoSenha()) || 
						(LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && itemPedido.isFlPrecoLiberadoSenha()) ||
						LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			itemPedido.ignoraValidacaoDesconto = true;
		} else {
			itemPedido.ignoraValidacaoDesconto = false;
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			itemPedido.qtItemEstoquePositivo = itemPedido.getQtItemEstoquePositivo();
		}
		calculateVlTotalItemPedido(pedido, itemPedido);
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlTotalItemPedido, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoTotal);
		}
		if (LavenderePdaConfig.pctMargemAgregada > 0) {
			calculateAndApplyVlAgregadoSugerido(itemPedido);
		}
		if (LavenderePdaConfig.calculaImpostosAdicionaisItemPedido) {
			calculaValorPisECofins(itemPedido, itemPedido.tributacaoConfig, itemPedido.getTributacaoItem(itemPedido.pedido.getCliente()));
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			calculaVpc(itemPedido, pedido.vlPctVpc);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			calculateValoresInterpolacaoItem(pedido, itemPedido);
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && !pedido.getCliente().isFreteEmbutido()) {
			itemPedido.vlItemFreteTributacao = itemPedido.vlItemPedido + itemPedido.vlItemPedidoFrete + itemPedido.getValoresTribRound(true);
			itemPedido.vlTotalItemFreteTributacao = (itemPedido.vlItemPedido * itemPedido.getQtItemFisico()) + (itemPedido.vlItemPedidoFrete * itemPedido.getQtItemFisico()) + itemPedido.getValoresTribRound(false);
		}
		calculaMargemContribuicaoItemRegra2(itemPedido);
		calculaPontuacaoItemPedido(itemPedido, pedido);
		if (LavenderePdaConfig.limitaPrecoMaximoConsumidor()) {
			if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorProduto()) {
				if(itemPedido.getProduto().vlPrecoMaximoConsumidor > 0 && itemPedido.vlItemPedido > itemPedido.getProduto().vlPrecoMaximoConsumidor) {
					itemPedido.vlItemPedido = itemPedido.getProduto().vlPrecoMaximoConsumidor;
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_DIFERENTE_SUGERIDO, new String[]{StringUtil.getStringValueToInterface(itemPedido.getProduto().vlPrecoMaximoConsumidor)}));					
				}
			} else if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorItemTabPreco()) {
				if(itemPedido.getItemTabelaPreco().vlPrecoMaximoConsumidor > 0 && itemPedido.vlItemPedido > itemPedido.getItemTabelaPreco().vlPrecoMaximoConsumidor) {
					itemPedido.vlItemPedido = itemPedido.getItemTabelaPreco().vlPrecoMaximoConsumidor;
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_DIFERENTE_SUGERIDO, new String[]{StringUtil.getStringValueToInterface(itemPedido.getItemTabelaPreco().vlPrecoMaximoConsumidor)}));	
				}
			}
		}
		
	}

	public boolean produtoDescQuantidadePesoBloqueado(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaBloqueioProdutosDescQtdPeso()) { 
			if (itemPedido.cdTabelaPreco != null || itemPedido.pedido.cdTabelaPreco != null) {
				DescQuantidadePeso descQuantidadePeso = new DescQuantidadePeso();
				descQuantidadePeso.cdEmpresa = itemPedido.cdEmpresa;
				descQuantidadePeso.cdRepresentante = itemPedido.cdRepresentante;
				descQuantidadePeso.cdTabelaPreco = itemPedido.cdTabelaPreco;
				descQuantidadePeso = (DescQuantidadePeso) DescQuantidadePesoService.getInstance().findByExample(descQuantidadePeso);
				DescQuantPesoPrdBlq descQuantidadePesoPrdBlq = new DescQuantPesoPrdBlq();					
				descQuantidadePesoPrdBlq.cdEmpresa = itemPedido.cdEmpresa;
				descQuantidadePesoPrdBlq.cdRepresentante = itemPedido.cdRepresentante;
				descQuantidadePesoPrdBlq.cdTabelaPreco = itemPedido.cdTabelaPreco ;
				
				if(descQuantidadePeso != null) {
					descQuantidadePeso.produtoBloqueadoList = DescQuantPesoPrdBlqService.getInstance().findProdutosBloqueados(descQuantidadePesoPrdBlq);
					if (descQuantidadePeso.produtoBloqueadoList.size() > 0) {
						if(descQuantidadePeso.produtoBloqueadoList.contains(itemPedido.cdProduto)) {
							itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
							itemPedido.vlPctFaixaDescQtdPeso = 0;
							return true;
						}
					}
				} else {
					descQuantidadePeso = new DescQuantidadePeso();
					descQuantidadePeso.cdEmpresa = itemPedido.cdEmpresa;
					descQuantidadePeso.cdRepresentante = itemPedido.cdRepresentante;
					descQuantidadePesoPrdBlq.cdTabelaPreco = "0";
					descQuantidadePeso.produtoBloqueadoList = DescQuantPesoPrdBlqService.getInstance().findProdutosBloqueados(descQuantidadePesoPrdBlq);
					if(descQuantidadePeso.produtoBloqueadoList.contains(itemPedido.cdProduto)) {
						itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
						itemPedido.vlPctFaixaDescQtdPeso = 0;
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean permiteAplicarDescontoItemPedido(ItemPedido itemPedido) {
		boolean permite = itemPedido.vlPctDesconto > 0 || itemPedido.auxiliarVariaveis.changedDescProgressivo;
		itemPedido.auxiliarVariaveis.changedDescProgressivo = false;
		return permite;
	}

	public void loadPoliticaComercial(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaPoliticaComercial() || !pedido.isPedidoAbertoEditavel()) return;
		
		if (itemPedido.politicaComercial != null && pedido.cdCondicaoPagamento != null && pedido.cdCondicaoPagamento.equals(pedido.oldCdCondicaoPagamento)) {
			itemPedido.cdPoliticaComercial = itemPedido.politicaComercial.cdPoliticaComercial; 
			return;
		}
		PoliticaComercial politicaComercial = PoliticaComercialService.getInstance().findPoliticaComercial(pedido, itemPedido);
		itemPedido.politicaComercial = politicaComercial;
		if (politicaComercial == null) {
			itemPedido.cdPoliticaComercial = null;
		} else {
			itemPedido.cdPoliticaComercial = politicaComercial.cdPoliticaComercial;
		}
	}

	
	public void loadPoliticaComercialFaixa(final ItemPedido itemPedido) throws SQLException {
		if ((!LavenderePdaConfig.usaPoliticaComercial() || ValueUtil.isEmpty(itemPedido.cdPoliticaComercial))) {
			itemPedido.politicaComercialFaixa = null;
			return;
		}
		if (LavenderePdaConfig.isValidaPctPoliticaComercial()) {
			validaVlPctMaxMinPoliticaComercial(itemPedido);
		}
		PoliticaComercialFaixa politicaComercialFaixa = PoliticaComercialFaixaService.getInstance().findPoliticaComercialFaixaByPoliticaComercial(itemPedido);
		itemPedido.politicaComercialFaixa = politicaComercialFaixa;
		if (politicaComercialFaixa == null)	return;
		itemPedido.vlPctPoliticaComercial = politicaComercialFaixa.vlPctPoliticaComercial;
	}
	
	public void reloadPoliticaComercial(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaPoliticaComercial() && ValueUtil.isNotEmpty(itemPedido.cdPoliticaComercial)) {
			itemPedido.politicaComercial = PoliticaComercialService.getInstance().findPoliticaComercialItemPedido(itemPedido);
			if (itemPedido.politicaComercial == null) {
				zeraPoliticaComercial(itemPedido);
			}
		}
	}
	
	public void reloadFaixaPoliticaComercial(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaPoliticaComercial() && ValueUtil.isNotEmpty(itemPedido.cdPoliticaComercial)) {
			if (LavenderePdaConfig.isValidaPctPoliticaComercial()) {
				if (!isValidaVlPctMaxMinPoliticaComercial(itemPedido)) {
					zeraPoliticaComercial(itemPedido);
					return;
				}
			}
			itemPedido.politicaComercialFaixa = PoliticaComercialFaixaService.getInstance().findPoliticaComercialFaixaByPoliticaComercial(itemPedido);
			if (itemPedido.politicaComercialFaixa == null) {
				zeraPoliticaComercial(itemPedido);
			} else {
				itemPedido.vlPctPoliticaComercial = itemPedido.politicaComercialFaixa.vlPctPoliticaComercial;
			}
		}
	}
	
	private boolean isValidaVlPctMaxMinPoliticaComercial(final ItemPedido itemPedido) throws SQLException {
		try {
			validaVlPctMaxMinPoliticaComercial(itemPedido);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void zeraPoliticaComercial(ItemPedido itemPedido) throws SQLException {
		itemPedido.politicaComercialFaixa = null;
		itemPedido.politicaComercial = null;
		itemPedido.cdPoliticaComercial = null;
		itemPedido.vlPctPoliticaComercial = 0;
		itemPedido.vlPctComissao = 0;
		itemPedido.vlTotalComissao = 0;
		getItemPedidoPdbxDao().updateValoresPoliticaComercial(itemPedido);
		itemPedido.pedido.alterouPoliticaComercialItem = true;
	}
	
	public void calculaDescQtdBeforeLoadPoliticaComercialFaixa(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaPoliticaComercial() || ValueUtil.isEmpty(itemPedido.cdPoliticaComercial) || !LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido || itemPedido.descQuantidade == null) {
			return;
		}
		calculaDescQtdItemPedido(pedido, itemPedido, false);
	}
	
	private void throwValidationExceptionPctPoliticaComercial(String lbDescAcres, double vlPctMin, double vlPctMax, double vlPermitido) {
		double primeiroValor = vlPctMin;
		double segundoValor = vlPctMax;
		
		if (LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial() && ((segundoValor > 0 && primeiroValor < 0) || (primeiroValor < 0 && segundoValor < 0))) {
			primeiroValor = vlPctMax;
			segundoValor = vlPctMin;
		}
		
		String lbPrimeiroValor = primeiroValor < 0 ? "acréscimo" : "desconto";
		String lbSegundoValor = segundoValor < 0 ? "acréscimo" : "desconto";
		
		if (LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial()) {
			throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_LIMITE_MINMAX_POLITICA_COMERCIAL, new String[] {
					lbDescAcres,
					StringUtil.getStringValueToInterface(Math.abs(primeiroValor)),
					lbPrimeiroValor,
					StringUtil.getStringValueToInterface(Math.abs(segundoValor)),
					lbSegundoValor
			}), vlPermitido);
		}
		throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_LIMITE_POLITICA_COMERCIAL, new String[] {
				lbDescAcres,
				StringUtil.getStringValueToInterface(Math.abs(LavenderePdaConfig.isValidaPctMaxPoliticaComercial() ? segundoValor : primeiroValor)),
				LavenderePdaConfig.isValidaPctMaxPoliticaComercial() ? lbSegundoValor : lbPrimeiroValor
		}), vlPermitido);
	}

	public void validaVlPctMaxMinPoliticaComercial(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.zerouDescAcresByPoliticaComercial || itemPedido.politicaComercial == null) return;
		PoliticaComercial politicaComercial = itemPedido.politicaComercial;
		double descontosItemPedido = ValueUtil.round(itemPedido.vlPctDesconto + itemPedido.vlPctFaixaDescQtd);
		
		if (LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial() &&
			(politicaComercial.vlPctPoliticaComercialMax == 0 && politicaComercial.vlPctPoliticaComercialMin == 0) &&
			(descontosItemPedido > 0 || itemPedido.vlPctAcrescimo > 0)) {
			throw new DescAcresMaximoException(Messages.ITEMPEDIDO_ERRO_POLITICA_COMERCIAL_NAO_PERMITE_ACRESCIMO_OU_DESCONTO, itemPedido.vlBaseItemPedido);
		}
		
		if (LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial() || LavenderePdaConfig.isValidaPctMaxPoliticaComercial()) {
			if (politicaComercial.vlPctPoliticaComercialMax > 0 && politicaComercial.vlPctPoliticaComercialMax < descontosItemPedido) {
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - politicaComercial.vlPctPoliticaComercialMax / 100);
				throwValidationExceptionPctPoliticaComercial("desconto", politicaComercial.vlPctPoliticaComercialMin, politicaComercial.vlPctPoliticaComercialMax, vlPermitido);
			} else if (politicaComercial.vlPctPoliticaComercialMax < 0 && Math.abs(politicaComercial.vlPctPoliticaComercialMax) > itemPedido.vlPctAcrescimo) {
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - politicaComercial.vlPctPoliticaComercialMax / 100);
				throwValidationExceptionPctPoliticaComercial(itemPedido.vlPctDesconto > 0 ? "desconto" : "acréscimo", politicaComercial.vlPctPoliticaComercialMin, politicaComercial.vlPctPoliticaComercialMax, vlPermitido);
			} else if (politicaComercial.vlPctPoliticaComercialMax == 0 && descontosItemPedido > 0) {
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_POLITICA_COMERCIAL_NAO_PERMITE_ACRESCIMO_DESCONTO, "desconto"), itemPedido.vlBaseItemPedido);
			}
		}
		
		if (LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial() || LavenderePdaConfig.isValidaPctMinPoliticaComercial()) {
			if (politicaComercial.vlPctPoliticaComercialMin > 0 && politicaComercial.vlPctPoliticaComercialMin > descontosItemPedido) {
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - politicaComercial.vlPctPoliticaComercialMin / 100);
				throwValidationExceptionPctPoliticaComercial(itemPedido.vlPctAcrescimo > 0 ? "acréscimo" : "desconto", politicaComercial.vlPctPoliticaComercialMin, politicaComercial.vlPctPoliticaComercialMax, vlPermitido);
			} else if (politicaComercial.vlPctPoliticaComercialMin < 0 && Math.abs(politicaComercial.vlPctPoliticaComercialMin) < itemPedido.vlPctAcrescimo) {
				double vlPermitido = itemPedido.vlBaseItemPedido * (1 - politicaComercial.vlPctPoliticaComercialMin / 100);
				throwValidationExceptionPctPoliticaComercial("acréscimo", politicaComercial.vlPctPoliticaComercialMin, politicaComercial.vlPctPoliticaComercialMax, vlPermitido);
			} else if (politicaComercial.vlPctPoliticaComercialMin == 0 && itemPedido.vlPctAcrescimo > 0) {
				throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_POLITICA_COMERCIAL_NAO_PERMITE_ACRESCIMO_DESCONTO, "acréscimo"), itemPedido.vlBaseItemPedido);					
			}
		}
	}
	
	protected void zeraDescAcresByPoliticaComercial(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaPoliticaComercial() || ValueUtil.isEmpty(itemPedido.cdPoliticaComercial) || !LavenderePdaConfig.isValidaPctPoliticaComercial() || !itemPedido.pedido.onReplicacao) return;
		if (itemPedido.politicaComercial == null) loadPoliticaComercial(itemPedido, itemPedido.pedido);
		try {			
			validaVlPctMaxMinPoliticaComercial(itemPedido);
		} catch (Throwable e) {
			itemPedido.vlPctDesconto = 0d;
			itemPedido.vlPctFaixaDescQtd = 0d;
			itemPedido.descQuantidade = null;
			itemPedido.vlPctAcrescimo = 0d;
			itemPedido.zerouDescAcresByPoliticaComercial = true;
			PedidoService.getInstance().recalculaItemPedido(itemPedido);
		}
	}
	
	public void calculaPontuacaoItemPedido(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaControlePontuacao) return;
		final PontuacaoConfig pontuacaoConfig = PontuacaoConfigService.getInstance().findPontuacaoConfigItemPedido(pedido, itemPedido);
		if (pontuacaoConfig == null) {
			itemPedido.vlPontuacaoRealizadoItem = itemPedido.vlPontuacaoBaseItem = itemPedido.vlPesoPontuacao = itemPedido.vlFatorCorrecaoFaixaDias = itemPedido.vlFatorCorrecaoFaixaPreco = itemPedido.vlPctFaixaPrecoPontuacao = itemPedido.vlBasePontuacao = 0d;
			itemPedido.cdPontuacaoConfig = null;
			itemPedido.qtDiasFaixaPontuacao = 0;
			return;
		}
		itemPedido.pontuacaoProduto = pontuacaoConfig.pontuacaoProduto;
		itemPedido.cdPontuacaoConfig = pontuacaoConfig.cdPontuacaoConfig;
		itemPedido.vlPontuacaoRealizadoItem = ValueUtil.round(PontuacaoConfigService.getInstance().getPontuacaoRealizadaItem(itemPedido, pontuacaoConfig), LavenderePdaConfig.nuCasasDecimaisPontuacao);
		itemPedido.vlPontuacaoBaseItem = ValueUtil.round(PontuacaoConfigService.getInstance().getPontuacaoBaseItem(itemPedido, pontuacaoConfig), LavenderePdaConfig.nuCasasDecimaisPontuacao);
		itemPedido.vlPesoPontuacao = PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig);
		itemPedido.vlFatorCorrecaoFaixaDias = PontuacaoConfigService.getInstance().getVlFatorCorrecaoFaixaDia(pontuacaoConfig);
		itemPedido.qtDiasFaixaPontuacao = pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.qtDiasMedios;
		itemPedido.vlFatorCorrecaoFaixaPreco = PontuacaoConfigService.getInstance().getVlFatorCorrecaoFaixaPreco(pontuacaoConfig);
		itemPedido.vlPctFaixaPrecoPontuacao = PontuacaoConfigService.getInstance().getPctDifFaixaPrecoPontuacao(itemPedido);
	}

	private void aplicaDescontoMaximoPorCondicaoPagamento(final ItemPedido itemPedido) {
		if (!LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) return; 
		itemPedido.vlItemPedido  = itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDescCondPagto / 100));
	}

	public void aplicaDescontosEmCascataNoItemPedidoRegra2(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		itemPedido.vlItemPedido = roundDescontoEmCascataRegra2(itemPedido.vlBaseItemPedido);
		//--Desconto Promocional
		if (!itemPedido.getItemTabelaPreco().isFlBloqueiaDescPromo()) {
			itemPedido.vlPctDescontoPromo = itemPedido.getItemTabelaPreco().vlPctDescPromocional;
			itemPedido.vlItemPedido = roundDescontoEmCascataRegra2(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescontoPromo / 100)));
		}
		//--Desconto padrão (param 38 domínio 1)
		if (!itemPedido.getItemTabelaPreco().isFlBloqueiaDescPadrao() && itemPedido.vlPctDesconto > 0) {
			itemPedido.vlDesconto = roundDescontoEmCascataRegra2(itemPedido.vlItemPedido - (itemPedido.vlItemPedido  * (1 - (itemPedido.vlPctDesconto / 100))));
			itemPedido.vlItemPedido -= itemPedido.vlDesconto;
			itemPedido.vlDesconto *= itemPedido.getQtItemFisico();
		}
		//--Índice condição financeira
		if (!itemPedido.getTabelaPreco().isIgnoraIndiceCondTipoPagto() && !itemPedido.getItemTabelaPreco().isFlBloqueiaDescCondicao()) {
			itemPedido.vlPctDescontoCondicao = pedido.vlPctDescontoCondicao;
			itemPedido.vlDescontoCondicao = roundDescontoEmCascataRegra2(itemPedido.vlItemPedido * (pedido.vlPctDescontoCondicao / 100));
			itemPedido.vlItemPedido -= itemPedido.vlDescontoCondicao;
			itemPedido.vlDescontoCondicao *= itemPedido.getQtItemFisico();
		}
		//--Pct Desconto3
		if (!itemPedido.getItemTabelaPreco().isFlBloqueiaDesc3()) {
			itemPedido.vlDesconto3 = roundDescontoEmCascataRegra2(itemPedido.vlItemPedido - (itemPedido.vlItemPedido  * (1 - (itemPedido.vlPctDesconto3 / 100))));
			itemPedido.vlItemPedido = itemPedido.vlItemPedido - itemPedido.vlDesconto3;
			itemPedido.vlDesconto3 *= itemPedido.getQtItemFisico();
		}
	}

	public double roundDescontoEmCascataRegra2(double value) {
		if (LavenderePdaConfig.isArredondaDescontosEmCascataItemAItemRegra2()) {
			return ValueUtil.round(value);
		}
		return value;
	}
	
	public void roundDescontoEmCascataRegra2(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
		}
	}

	private void calculaVpc(ItemPedido itemPedido,double vlpct) throws SQLException {
		if(vlpct == 0) return;
		itemPedido.vlVpc = itemPedido.vlTotalItemPedido * (vlpct / 100);
	}

	private void calculaDescontoVolumeVendaMensal(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaFaixaDescontoVolumeVendaMensalPorItem() && pedido.vlPctDescCliente != 0 && pedido.onFechamentoPedido) {
			itemPedido.vlPctDescCliente = pedido.vlPctDescCliente;
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDescCliente / 100))); 
		}
	}

	private void aplicaDescontosEmCascataNoItemPedidoRegra1(final ItemPedido itemPedido) {
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			itemPedido.vlDesconto = roundDescontoEmCascata(itemPedido.vlBaseItemPedido * itemPedido.vlPctDesconto / 100);
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido - itemPedido.vlDesconto);
			if (itemPedido.isEditandoDescontoPct() || itemPedido.isEditandoAcrescimoPct() || itemPedido.isEditandoQtd() || itemPedido.isEditandoValorItem()) {
				itemPedido.vlDesconto2 = roundDescontoEmCascata(itemPedido.vlItemPedido * itemPedido.vlPctDesconto2 / 100);
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - itemPedido.vlDesconto2);
				itemPedido.vlDesconto3 = roundDescontoEmCascata(itemPedido.vlItemPedido * itemPedido.vlPctDesconto3 / 100);
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - itemPedido.vlDesconto3);
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataSemArredondamentoRegra1()) {
				itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			}
		}
	}

	private void aplicaDescontosAutoEmCascataNaCapaPedidoPorItem(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido);
			if (itemPedido.vlPctDesconto > 0) {
				if (itemPedido.isEditandoValorItem()) {
					itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - (itemPedido.vlItemPedido * (pedido.vlPctDescCliente / 100)));
				}
				if (!itemPedido.usaCestaPromo) {
					if (itemPedido.isEditandoAcrescimoPct() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
						aplicaAcrescimoItemPedido(itemPedido);
					} else if (itemPedido.isEditandoDescontoPct() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
						aplicaAcrescimoItemPedido(itemPedido);
					} else if (!itemPedido.isEditandoValorItem() && !(LavenderePdaConfig.isUsaPrecoBaseItemBonificado() && itemPedido.isItemBonificacao())) {
						aplicaAcrescimoItemPedido(itemPedido);
						if (!itemPedido.usaCestaPromo && !itemPedido.hasDescProgressivo()) {
							aplicaAcrescimoItemPedido(itemPedido);
						}
					} else {
						if ((itemPedido.isEditandoQtd() && !LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) || LavenderePdaConfig.usaGradeProduto4() ) {
							if (itemPedido.vlPctAcrescimo > 0 && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
								aplicaAcrescimoItemPedido(itemPedido);
							} else if (permiteAplicarDescontoItemPedido(itemPedido) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
								aplicaAcrescimoItemPedido(itemPedido);
							}
						}
					}
				}
			}
			if (itemPedido.vlPctDesconto != 0 ) {
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() && pedido.vlPctDescontoCondicao != 0 ) {
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - (itemPedido.vlItemPedido * (pedido.vlPctDescontoCondicao / 100)));
			}
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() && pedido.vlPctDescCliente != 0 && !itemPedido.isEditandoValorItem()) {
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - (itemPedido.vlItemPedido * (pedido.vlPctDescCliente / 100)));
			}
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete && pedido.vlPctDescFrete != 0 ) {
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido - (itemPedido.vlItemPedido * (pedido.vlPctDescFrete / 100)));
			}
			if (itemPedido.vlPctAcrescimo > 0) {
				if (!itemPedido.usaCestaPromo) {
					if (itemPedido.isEditandoAcrescimoPct() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
						aplicaAcrescimoItemPedido(itemPedido);
					} else if (itemPedido.isEditandoDescontoPct() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
						aplicaAcrescimoItemPedido(itemPedido);
					} else if (itemPedido.isEditandoValorItem() && !(LavenderePdaConfig.isUsaPrecoBaseItemBonificado() && itemPedido.isItemBonificacao())) {
						aplicaAcrescimoItemPedido(itemPedido);
						if (!itemPedido.usaCestaPromo && !itemPedido.hasDescProgressivo()) {
							aplicaAcrescimoItemPedido(itemPedido);
						}
					} else {
						if ((itemPedido.isEditandoQtd() && !LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) || LavenderePdaConfig.usaGradeProduto4() ) {
							if (itemPedido.vlPctAcrescimo > 0 && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
								aplicaAcrescimoItemPedido(itemPedido);
							} else if (permiteAplicarDescontoItemPedido(itemPedido) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
								aplicaAcrescimoItemPedido(itemPedido);
							}
						}
					}
				}
			}
			if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItemArredondaNoFinal()) {
				ValueUtil.round(itemPedido.vlItemPedido);
			}
		}
	}

	protected void aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(final ItemPedido itemPedido, final Pedido pedido) {
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido);
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido * (1 - (pedido.vlPctDescCliente / 100)));
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido * (1 - (pedido.vlPctDescontoCondicao / 100)));
			itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlItemPedido * (1 - (pedido.vlPctDescFrete / 100)));
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
		}
	}

	private void calculaPctAcrescimoDescontoItemPedido(final ItemPedido itemPedido, final Pedido pedido) {
		if ((itemPedido.vlBaseItemPedido != 0) && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || pedido.isFlPrecoLiberadoSenha())) {
			//% de desconto
			itemPedido.vlPctDescPedido = LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem() ? itemPedido.pedido.vlPctDescItem : 0d;
			double vlItemPedidoComDescPedido = itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescPedido / 100));
			boolean isValorDigitadoMaiorQueDescontado = itemPedido.vlItemPedido >= vlItemPedidoComDescPedido;
			if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
				itemPedido.vlPctDesconto = calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido);
				if (!isValorDigitadoMaiorQueDescontado) {
					itemPedido.vlPctDesconto -= itemPedido.vlPctDescPedido;
				}
			} else if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
				try {
					double vlBaseDesconto;
					vlBaseDesconto = itemPedido.getItemTabelaPreco().vlBase;
					if (itemPedido.vlPctFaixaDescQtdPeso > 0) {
						vlBaseDesconto = ValueUtil.round(vlBaseDesconto - (vlBaseDesconto * itemPedido.vlPctFaixaDescQtdPeso) / 100);
					}
					if (itemPedido.vlItemPedido < vlBaseDesconto) {
						itemPedido.vlItemPedido = vlBaseDesconto;
					} else {
						itemPedido.vlPctDesconto = calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				itemPedido.vlPctDesconto = getVlPctDescontoSemDescQtdFromItemPedido(itemPedido);
			}

			if (LavenderePdaConfig.usaConversaoUnidadesMedida && (itemPedido.descQuantidade != null)) {
				double vlItemPedidoComDescQtde = roundDescontoEmCascata(itemPedido.vlBaseItemPedido * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
				if (vlItemPedidoComDescQtde == roundDescontoEmCascata(itemPedido.vlItemPedido)) {
					itemPedido.vlPctDesconto = itemPedido.descQuantidade.vlPctDesconto;
				}
			}
			if (itemPedido.vlPctDesconto < 0) {
				itemPedido.vlPctDesconto = 0;
			}
			//% de acréscimo
			Double vlBaseItemDescontoCapaPedido = null;
			if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
				if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() && pedido.vlPctDescCliente != 0 && itemPedido.isEditandoValorItem()) {
					vlBaseItemDescontoCapaPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido - (itemPedido.vlBaseItemPedido * (pedido.vlPctDescCliente / 100)));
				}
			}
			itemPedido.vlPctAcrescimo = roundDescontoEmCascata(((itemPedido.vlItemPedido / (vlBaseItemDescontoCapaPedido != null ? vlBaseItemDescontoCapaPedido : itemPedido.vlBaseItemPedido)) - 1) * 100);
			if (itemPedido.vlPctAcrescimo < 0) {
				itemPedido.vlPctAcrescimo = 0;
			}
		}
	}

	public void aplicaAcrescimoItemPedido(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto()) {
			double vlItemPedido = roundDescontoEmCascata(itemPedido.vlUnidadePadrao * (1 + (itemPedido.vlPctAcrescimo / 100)));
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				itemPedido.vlItemPedido = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, vlItemPedido);
			}
		} else {
			Double vlBaseItemDescontoCapaPedido = null;
			if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
				if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() && itemPedido.pedido.vlPctDescCliente != 0 && (itemPedido.isEditandoValorItem() || itemPedido.vlPctAcrescimo > 0)) {
					vlBaseItemDescontoCapaPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido - (itemPedido.vlBaseItemPedido * (itemPedido.pedido.vlPctDescCliente / 100)));
				}
			}
			if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
				if (itemPedido.vlPctAcrescimo > 0d) {
					itemPedido.vlPctDescPedido = 0d;
				} else {
					itemPedido.vlPctDescPedido = itemPedido.pedido.vlPctDescItem;
				}
				itemPedido.vlItemPedido = roundDescontoEmCascata(itemPedido.vlBaseItemPedido * (1 + ((itemPedido.vlPctAcrescimo - itemPedido.vlPctDescPedido) / 100)));
			} else {
				itemPedido.vlItemPedido = roundDescontoEmCascata((vlBaseItemDescontoCapaPedido != null ? vlBaseItemDescontoCapaPedido : itemPedido.vlBaseItemPedido) * (1 + (itemPedido.vlPctAcrescimo / 100)));
			}
		}
	}

	public void aplicaDescontoItemPedido(final ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta) {
			if (LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto()) {
				double vlItemPedido = roundDescontoEmCascata(itemPedido.vlUnidadePadrao * (1 - (itemPedido.vlPctDesconto / 100)));
				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
				if (produtoUnidade != null) {
					itemPedido.vlItemPedido = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, vlItemPedido);
				}
			} else {
				if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
					itemPedido.vlPctDescPedido = itemPedido.pedido.vlPctDescItem;
				}
				double vlPctDescontoCalculo = itemPedido.vlPctDesconto + itemPedido.vlPctDescPedido;
				if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && itemPedido.descQuantidade != null) {
					double oldVlPctDesconto = itemPedido.vlPctDesconto;
					itemPedido.vlPctDesconto = vlPctDescontoCalculo;
					itemPedido.atualizandoDesc = itemPedido.pedido.getTabelaPreco().isAplicaDescQtdeAuto() 
							|| !itemPedido.pedido.getTabelaPreco().isAplicaDescQtdeAuto() && itemPedido.isEditandoDescontoPct();
					calculaDescQtdItemPedido(itemPedido.pedido, itemPedido, false);
					itemPedido.atualizandoDesc = false;
					itemPedido.vlPctDesconto = oldVlPctDesconto;
				} else {
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (vlPctDescontoCalculo / 100)));
				}
			}
			if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
				itemPedido.vlDescontoTotalAutoDesc = ValueUtil.getDoubleValueTruncated(itemPedido.vlDescontoAuto + (itemPedido.vlBaseItemPedido * (itemPedido.vlPctDesconto / 100) * itemPedido.getQtItemFisico()), LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
			}
		}
	}
	
	private double roundDesconto(double value) {
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			return ValueUtil.round(value, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
		}
		return roundDescontoEmCascata(value);
	}
	
	private double roundDescontoEmCascata(double value) {
		if (LavenderePdaConfig.isAplicaDescontosEmCascataSemArredondamentoRegra1() || LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItemArredondaNoFinal() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItemArredondaNoFinal()) {
			return value;
		}
		return ValueUtil.round(value);
	}

	/**
	 * Calcula dados diversos com informações do item do pedido de venda como peso, frete, impostos, descontoCCP.
	 * @param itemPedido
	 * @param pedido
	 * @throws SQLException 
	 */
	
	public void loadDadosItemPedido(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (itemPedido.tributacaoConfig == null) {
			itemPedido.getTributacaoConfigItem();
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			itemPedido.qtPeso = getPesoItemPedido(itemPedido);
		}
		if ((tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) && LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd && !LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem) {
			TranspTipoPedService.getInstance().calculateFreteItemPedido(pedido, itemPedido);
		}
		if ((tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) && LavenderePdaConfig.usaPctFreteTipoFreteNoPedido && !(LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && pedido.getCliente().isFreteEmbutido())) {
			TipoFreteService.getInstance().calculateFreteItemPedido(pedido.getTipoFrete(), itemPedido);
		}
		if ((tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) && LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() && (LavenderePdaConfig.isConfigCalculoPesoPedido() || LavenderePdaConfig.usaFreteValorUnidade) && LavenderePdaConfig.isUsaTipoFretePedido() && itemPedido.getQtItemFisico() != 0 && !LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem && !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos && !(LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && pedido.getCliente().isFreteEmbutido())) {
			TipoFreteTabPrecoService.getInstance().calculateFreteItemPedidoByTipoFreteTabPrecoEPeso(itemPedido.getTipoFreteTabPreco(), itemPedido);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido() && !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
			calculaSeguroEReaplicaImpostos(itemPedido, pedido);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			if (itemPedido.tributacaoConfig != null && itemPedido.tributacaoConfig.isCalculaPisCofins() && itemPedido.tributacaoConfig.isAplicaReducaoIcmsBaseCalculoPisCofins() && !itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				calculaIcmsPersonalizado(itemPedido, pedido);
			}
			calculaValorPisECofins(itemPedido, itemPedido.tributacaoConfig, itemPedido.getTributacaoItem(itemPedido.pedido.getCliente()));
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			boolean tribConfigUpdated = !pedido.oldCdTipoPedido.equals(pedido.cdTipoPedido)||pedido.isReplicandoPedido;
			calculaIpiItemPedido(itemPedido, tribConfigUpdated);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			calculaIcmsPersonalizado(itemPedido, pedido);
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			calculaFecopItemPedido(itemPedido, pedido.getCliente());
		}	
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			calculaStItemPedido(itemPedido, pedido.getCliente());
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido && pedido.getCliente().isFlAplicaSt()) {
			itemPedido.vlSt = itemPedido.getProduto().vlSt;
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				getVlStItemPedidoComConversaoUnidade(itemPedido);
			}
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			calculaDescontoCCPNoItemPedido(itemPedido);
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			calculaVolumeTotalItemPedido(itemPedido);
		}
	}

	public double getVlStItemPedidoComConversaoUnidade(ItemPedido itemPedido) throws SQLException {
		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
		if (produtoUnidade.isMultiplica()) {
			itemPedido.vlSt = ValueUtil.round(itemPedido.getProduto().vlSt * nuConversaoUnidade);
		} else {
			itemPedido.vlSt = ValueUtil.round(itemPedido.getProduto().vlSt / nuConversaoUnidade);
		}
		return itemPedido.vlSt;
	}

	private void calculaIcmsPersonalizado(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		Tributacao tributacao = itemPedido.getTributacaoItem(pedido.getCliente());
		boolean tribConfigUpdated = !pedido.oldCdTipoPedido.equals(pedido.cdTipoPedido)|| pedido.isReplicandoPedido;
		STService.getInstance().calculaIcmsPersonalizado(itemPedido, itemPedido.getTributacaoConfigItem(tribConfigUpdated), tributacao, itemPedido.getVlBaseTributacaoItem().vlBaseIcms);
	}

	private void calculaVolumeTotalItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null && itemPedido.getQtItemFisico() != 0) {
			itemPedido.vlVolumeItem = ValueUtil.round(itemPedido.getVlVolumeArrendondado() * itemPedido.getQtItemFisico(), LavenderePdaConfig.nuCasasDecimaisVlVolume);
		}
	}

	public void calculaSeguroEReaplicaImpostos(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (itemPedido.getQtItemFisico() != 0) {
			itemPedido.vlSeguroItemPedido = 0;
			calculaValorPisECofins(itemPedido, itemPedido.tributacaoConfig, itemPedido.getTributacaoItem(itemPedido.pedido.getCliente()));
			calculaIpiItemPedido(itemPedido);
			STService.getInstance().calculaIcmsPersonalizado(itemPedido, itemPedido.tributacaoConfig, itemPedido.getTributacaoItem(pedido.getCliente()), itemPedido.getVlBaseTributacaoItem().vlBaseIcms);
			calculaStItemPedido(itemPedido, pedido.getCliente());
			double vlItemPedidoFrete = LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : itemPedido.vlItemPedidoFrete;
			double vlBrutoItem = itemPedido.vlItemPedido + vlItemPedidoFrete + (itemPedido.getVlTributos() / itemPedido.getQtItemFisico()) - (itemPedido.getVlDeducoes() / itemPedido.getQtItemFisico());
			if (pedido.isCalculaSeguro()) {
				itemPedido.vlSeguroItemPedido = ValueUtil.round(vlBrutoItem * LavenderePdaConfig.vlIndiceSeguroCalculadoNoItemPedido);
			}
		}
	}

	public void calculaImpostosParaSeguro(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			calculaValorPisECofins(itemPedido, itemPedido.getProduto().tributacaoConfig, itemPedido.getTributacaoItem(itemPedido.pedido.getCliente()));
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			calculaIpiItemPedido(itemPedido);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			Tributacao tributacao = itemPedido.getTributacaoItem(pedido.getCliente());
			STService.getInstance().calculaIcmsPersonalizado(itemPedido, itemPedido.tributacaoConfig, tributacao, itemPedido.getVlBaseTributacaoItem().vlBaseIcms);
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			calculaFecopItemPedido(itemPedido, pedido.getCliente());
		}	
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			calculaStItemPedido(itemPedido, pedido.getCliente());
		}
	}
	
	public void calculaDescontoCCPNoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.vlPctDescontoCCP == 0) {
			itemPedido.vlPctDescontoCCP = DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, itemPedido.pedido.getCliente());
		}
		itemPedido.vlDescontoCCP = itemPedido.vlItemPedido * itemPedido.vlPctDescontoCCP / 100;
	}

	public double calculaVlPctDesconto(double vlSemDesconto, double vlComDesconto) {
		return calculaVlPctDesconto(vlSemDesconto, vlComDesconto, true);
	}
	
	public double calculaVlPctDesconto(double vlSemDesconto, double vlComDesconto, boolean encontrarMenorDescontoPossivel) {
		if (vlSemDesconto == 0) {
			return 0;
		}
		double vlPctDesconto = ValueUtil.round((1 - (vlComDesconto / vlSemDesconto)) * 100);
		if (vlPctDesconto == 0 || vlPctDesconto < 0) {
			return 0;
		}
		if (encontrarMenorDescontoPossivel) {
			vlPctDesconto = getMenorPercentualDescontoPossivel(vlSemDesconto, vlComDesconto, vlPctDesconto);
		}
		return vlPctDesconto;
	}

	private double getMenorPercentualDescontoPossivel(double vlSemDesconto, double vlComDesconto, double vlPctDesconto) {
		double menorVlPctDecontoPossivel = vlPctDesconto;
		double redutor = Math.pow(10, -LavenderePdaConfig.nuCasasDecimais);
		do {
			vlPctDesconto -= redutor;
			double vlItemComDesconto = ValueUtil.round(vlSemDesconto * (1 - (vlPctDesconto / 100)));
			if (vlItemComDesconto == ValueUtil.round(vlComDesconto)) {
				menorVlPctDecontoPossivel = vlPctDesconto;
			}
		} while (menorVlPctDecontoPossivel == vlPctDesconto);
		return menorVlPctDecontoPossivel;
	}

	public double calculaVlPctAcrescimo(double vlBaseItemPedido, double vlItemPedido) {
		return ValueUtil.round(((vlItemPedido / vlBaseItemPedido) - 1) * 100);
	}
	
	public void calculaItemPedidoGradeDescontoAcrescimo(ItemPedido itemPedido) {
		itemPedido.vlPctDesconto = Math.max(0, calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido));
		itemPedido.vlPctAcrescimo = Math.max(0, calculaVlPctAcrescimo(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido));
	}
	
	public void calculaItemPedidoGradeDescontoAcrescimoComDescCapa(ItemPedido itemPedido) {
		double vlItemPedidoComDescPedido = itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescPedido / 100));
		boolean isValorDigitadoMaiorQueDescontado = itemPedido.vlItemPedido >= vlItemPedidoComDescPedido;
		double vlPctDescPedidoDescontado = isValorDigitadoMaiorQueDescontado ? 0 : itemPedido.vlPctDescPedido;
		itemPedido.vlPctDesconto = Math.max(0, calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido, false) - vlPctDescPedidoDescontado);
		itemPedido.vlPctAcrescimo = Math.max(0, calculaVlPctAcrescimo(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido));
	}
	
	public void garantirDescontoAcrescimentoNaoNegativo(ItemPedido itemPedido) {
		itemPedido.vlPctDesconto = Math.max(0, itemPedido.vlPctDesconto);
		itemPedido.vlPctAcrescimo = Math.max(0, itemPedido.vlPctAcrescimo);
	}
	
	private void calculaValorItemBaseadoNoVlElementar(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() /*&& !LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex()*/) {
			if (itemPedido.isEditandoValorElementar()) {
				aplicaValorElementarNoVlItemPedido(itemPedido);
			} else {
				calculaValorEmbalagemElementarComDescAcrescimo(itemPedido);
				//--
				itemPedido.vlItemPedido = calculaVlItemByVlElementar(itemPedido, itemPedido.vlEmbalagemElementar);
			}
		}
	}

	public void calculaValorEmbalagemElementarComDescAcrescimo(final ItemPedido itemPedido) {
		double vlBaseComDescontoAcrescimo = itemPedido.vlBaseEmbalagemElementar;
		if (itemPedido.vlPctAcrescimo > 0) {
			vlBaseComDescontoAcrescimo = itemPedido.vlBaseEmbalagemElementar * (1 + (itemPedido.vlPctAcrescimo / 100));
		} else if (itemPedido.vlPctDesconto > 0) {
			vlBaseComDescontoAcrescimo = itemPedido.vlBaseEmbalagemElementar * (1 - (itemPedido.vlPctDesconto / 100));
		}
		itemPedido.vlEmbalagemElementar = ValueUtil.round(vlBaseComDescontoAcrescimo);
	}

	public double calculaVlItemByVlElementar(final ItemPedido itemPedido, final double vlEmbalagemElementar) throws SQLException {
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			double nuConversaoUnidade = itemPedido.getItemTabelaPreco().getNuConversaoUnidade();
			return vlEmbalagemElementar * nuConversaoUnidade;
		} else {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			double nuConversaoUnidadesMedida = itemPedido.getProduto().nuConversaoUnidadesMedida;
			if (nuConversaoUnidadesMedida == 0) {
				nuConversaoUnidadesMedida = 1;
			}
			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
			if (produtoUnidade == null) {
				return vlEmbalagemElementar * nuConversaoUnidadesMedida;
			}
			if (produtoUnidade.isMultiplica()) {
				return vlEmbalagemElementar * (nuConversaoUnidadesMedida * nuConversaoUnidade);
			} else {
				return vlEmbalagemElementar * (nuConversaoUnidadesMedida / nuConversaoUnidade);
			}
		}
	}
	

	public void calculaDescQtdItemPedido(Pedido pedido, final ItemPedido itemPedido, boolean editandoValorItem) throws SQLException {
		if (itemPedido.isIgnoraDescQtd() || pedido.isPedidoBonificacao()) {
			return;
		}
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && itemTabelaPreco.descontoQuantidadeList == null) {
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto), itemPedido);
			} else {
				itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemTabelaPreco.cdTabelaPreco, itemPedido.cdProduto);
			}
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && ValueUtil.isNotEmpty(itemTabelaPreco.descontoQuantidadeList) && !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) && !itemPedido.possuiDescMaxProdCli()) {
			//Se o qtItemFisico for diferente do oldQtItemFisicoDescQtd é necessário calcular o desconto por quantidade
			double qtItemFisicoAtualDescQtd = itemPedido.getQtItemFisicoOrg();
			if ((qtItemFisicoAtualDescQtd != itemPedido.oldQtItemFisicoDescQtd || itemPedido.unidadeAlternativaChanged) || itemPedido.atualizandoDesc || LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta) {
				if (isPermiteAtualizarDescQtde(itemPedido, editandoValorItem)) {
					TabelaPreco tabelaPreco = itemTabelaPreco.getTabelaPreco();
					if (isDeveAtualizarDescQtde(pedido, itemPedido, qtItemFisicoAtualDescQtd, tabelaPreco)) {
						salvaVlDescontoFaixaDescQtdAnterior(itemPedido);
						DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
						marcaItemPedidoCalculaSimilares(itemPedido);
						aplicaDescQtdItemPedido(itemPedido);
					} else {
						itemPedido.descQuantidade = null;
						itemPedido.vlPctFaixaDescQtd = 0d;
						itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - ((itemPedido.vlPctFaixaDescQtd + itemPedido.vlPctDesconto) / 100)));
					}
					itemPedido.editandoDescQtd = false;
				}
				itemPedido.atualizandoDesc = false;
				itemPedido.oldQtItemFisicoDescQtd = itemPedido.getQtItemFisicoOrg();
			}
		}
	}
	
	private void salvaVlDescontoFaixaDescQtdAnterior(ItemPedido itemPedido) {
		if (itemPedido.descQuantidade != null && LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
			itemPedido.oldVlDescontoFaixaDescQtd = itemPedido.descQuantidade.vlDesconto;
		}
	}

	private boolean isDeveAtualizarDescQtde(Pedido pedido, ItemPedido itemPedido, double qtItemFisicoAtualDescQtd, TabelaPreco tabelaPreco) {
		boolean deveAtualizarPelaQtde = itemPedido.descQuantidade != null && qtItemFisicoAtualDescQtd >= itemPedido.descQuantidade.qtItem && !pedido.isReplicandoPedido;
		return tabelaPreco != null && ((tabelaPreco.isAplicaDescQtdeAuto() || itemPedido.editandoDescQtd || LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta)
				|| itemPedido.unidadeAlternativaChanged || itemPedido.atualizandoDesc || ValueUtil.valueNotEqualsIfNotNull(pedido.cdCondicaoPagamento, pedido.oldCdCondicaoPagamento) || deveAtualizarPelaQtde);
	}

	private boolean isPermiteAtualizarDescQtde(ItemPedido itemPedido, boolean editandoValorItem) {
		return (!itemPedido.isEditandoAcrescimoPct() || (itemPedido.isEditandoAcrescimoPct() && itemPedido.vlPctAcrescimo == 0d)) && (!itemPedido.isEditandoValorItem() || editandoValorItem)
				&& (!itemPedido.isEditandoDescontoPct() || itemPedido.editandoDescQtd || LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta || itemPedido.atualizandoDesc) && !itemPedido.zerouDescAcresByPoliticaComercial;
	}

	private void marcaItemPedidoCalculaSimilares(ItemPedido itemPedido) throws SQLException {
		boolean itemPedidoNaoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoNaoAutorizado(itemPedido, null);
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && (!itemPedido.atualizandoDesc || itemPedidoNaoAutorizado)) {
			if (itemPedido.descQuantidade != null) {
				itemPedido.alterouFaixa = itemPedidoNaoAutorizado || isAlterouFaixaDescQtdSimilaridade(itemPedido);
			} else if (itemPedido.oldVlPctFaixaDescQtd > 0) {
				itemPedido.alterouFaixa = true;
			}
		}
	}
	
	private boolean isAlterouFaixaDescQtdSimilaridade(ItemPedido itemPedido) {
		boolean alterouFaixaVlDescQtd = LavenderePdaConfig.usaDescontoPorQuantidadeValor && ValueUtil.valueNotEqualsIfNotNull(itemPedido.oldVlDescontoFaixaDescQtd, itemPedido.descQuantidade.vlDesconto);
		return itemPedido.oldVlPctFaixaDescQtd != itemPedido.descQuantidade.vlPctDesconto || itemPedido.descQuantidade.vlPctDesconto > 0d && itemPedido.isIgnoraDescQtd() || ValueUtil.isEmpty(itemPedido.flTipoAlteracao) || alterouFaixaVlDescQtd;
	}

	private void calculaDescPromocionalPorQtde(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null && itemPedido.permiteAplicarDesconto() && !itemPedido.isIgnoraDescQtdPro() && DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) {
			if (ValueUtil.round(itemPedido.getQtItemFisico()) != ValueUtil.round(itemPedido.oldQtItemFisicoDescPromocionalQtd) || pedido.isReplicandoPedido) {
				DescPromocionalService.getInstance().loadMaiorFaixaDescPromocionalPorQuantidadeItemPedido(itemPedido);
				if (itemPedido.descPromocional != null && !(itemPedido.descPromocional.equals(new DescPromocional())) || pedido.isReplicandoPedido) {
					if (!pedido.isReplicandoPedido) {
						clearDadosDesconto(itemPedido);
					}
					aplicaDescontoNoProdutoPorGrupoDescPromocional(itemPedido);
					if (LavenderePdaConfig.usaUnidadeAlternativa) {
						aplicaQtdadeElementarValorElementarNoItemPedido(itemPedido, itemPedido.vlUnidadePadrao, itemPedido.getProdutoUnidade());
						if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem()) {
							itemPedido.vlBaseEmbalagemElementar = itemPedido.vlEmbalagemElementar;
						}
					}
				} else {
					PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
				}
				itemPedido.oldQtItemFisicoDescPromocionalQtd = itemPedido.getQtItemFisico();
			}
		}
	}
	
	protected void aplicaDescQtdItemPedido(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido.descQuantidade != null && itemPedido.vlPctAcrescimo == 0 && (!LavenderePdaConfig.usaSomenteQtdDescQtdSimilar || DescQuantidadeService.getInstance().hasDescontoQuantidade(itemPedido))) {
			if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
				calculaDescontoQtdPorQuantidadeValor(itemPedido);
			} else if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta) {
				calculaDescQuantidadeApenasEmbalagemCompleta(itemPedido);
			} else if (LavenderePdaConfig.aplicaDescontoQuantidadeVlBase && !itemPedido.isIgnoraDescQtd()) {
				calculaDescQtdAplicaVlBase(itemPedido);
			} else if (!itemPedido.isIgnoraDescQtd()) {
				itemPedido.vlPctFaixaDescQtd = itemPedido.descQuantidade.vlPctDesconto;
				itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - ((itemPedido.vlPctFaixaDescQtd + itemPedido.vlPctDesconto) / 100)));
			}
			if (itemPedido.isIgnoraDescQtd()) {
				itemPedido.vlPctFaixaDescQtd = itemPedido.oldVlPctFaixaDescQtd = 0d;
			}
			if (!itemPedido.isIgnoraDescQtd()) {
				itemPedido.vlPctAcrescimo = 0;
			}
		} else {
			if (LavenderePdaConfig.aplicaDescontoQuantidadeVlBase && (itemPedido.isRecebeuDescontoPorQuantidade() || itemPedido.cancelouDescQtd)) {
				reverteDescQtd(itemPedido);
			}
			if (LavenderePdaConfig.usaDescontoPorQuantidadeValor && itemPedido.oldVlDescontoFaixaDescQtd > 0) {
				itemPedido.vlPctDesconto = 0;
			}
			itemPedido.vlPctFaixaDescQtd = 0;
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - ((itemPedido.vlPctFaixaDescQtd + itemPedido.vlPctDesconto) / 100)));
		}
		setaCdAgrupadorSimilaridadeDescQtd(itemPedido);
		itemPedido.cancelouDescQtd = false;
	}

	private void calculaDescQtdAplicaVlBase(final ItemPedido itemPedido) throws SQLException {
		if ((itemPedido.oldVlPctFaixaDescQtd > 0 || itemPedido.oldVlPctFaixaDescQtd != itemPedido.descQuantidade.vlPctDesconto) && !itemPedido.cancelouDescQtd) {
			reverteDescQtd(itemPedido);
		}
		itemPedido.flTipoEdicao = itemPedido.vlPctDesconto > 0 ? ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT : itemPedido.flTipoEdicao;
		itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
		itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
		itemPedido.vlUnidadePadrao  = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
		itemPedido.vlPctFaixaDescQtd = itemPedido.descQuantidade.vlPctDesconto;
		itemPedido.oldVlPctFaixaDescQtd = itemPedido.vlPctFaixaDescQtd;
	}

	private void calculaDescQuantidadeApenasEmbalagemCompleta(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido.gerouEmbalagemCompleta == false) {
			calculaDescQtdEmbalagemCompleta(itemPedido, false);
		}
		if (itemPedido.gerouEmbalagemCompleta) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
			aplicaDescontoCascataEmbalagemCompleta(itemPedido);
		} else {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
		}
	}

	private void calculaDescontoQtdPorQuantidadeValor(final ItemPedido itemPedido) {
		itemPedido.vlPctDesconto = ValueUtil.round((1 - ((itemPedido.vlBaseItemPedido - itemPedido.descQuantidade.vlDesconto) / itemPedido.vlBaseItemPedido)) * 100);
		itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		if (itemPedido.descQuantidade.vlDesconto != 0) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido - itemPedido.descQuantidade.vlDesconto);
		}
	}

	private void setaCdAgrupadorSimilaridadeDescQtd(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.descQuantidade == null) {
			itemPedido.cdAgrupadorSimilaridade = null;
		} else if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && itemPedido.descQuantidade.isFlAgrupadorSimilaridade()) {
			itemPedido.cdAgrupadorSimilaridade = itemPedido.getProduto().cdAgrupadorSimilaridade;
		}
	}

	public void reverteDescQtd(final ItemPedido itemPedido) throws SQLException {
		PedidoService.getInstance().loadValorBaseItemPedido(itemPedido.pedido, itemPedido);
		itemPedido.vlPctFaixaDescQtd = 0;
		itemPedido.oldVlPctFaixaDescQtd = itemPedido.vlPctFaixaDescQtd;
	}

	private void aplicaDescontoCascataEmbalagemCompleta(final ItemPedido itemPedido) {
		itemPedido.vlPctFaixaDescQtd = itemPedido.descQuantidade.vlPctDesconto;
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctFaixaDescQtd / 100))); 
	}

	public void calculateVlTotalItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		// Acrescenta o Frete no item
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco && (!LavenderePdaConfig.usaFreteApenasTipoFob || pedido.isTipoFreteFob())) {
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlTotalItemPedido + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : (itemPedido.vlItemPedidoFrete * itemPedido.getQtItemFisico())));
		}
	}

	public String consisteVlAproximadoTabelaPreco(final Vector listTabPreco, final ItemPedido itemPedido) throws SQLException {
		if ((listTabPreco != null) && (listTabPreco.size() > 0)) {
			TabelaPreco tabPrecoVlAproximado = null;
			ItemTabelaPreco itemVlAproximado = null;
			double menorValor = 0;
			double maiorValor = 0;
			int size = listTabPreco.size();
			for (int i = 0; i < size; i++) {
				TabelaPreco tabelaPreco = (TabelaPreco) listTabPreco.items[i];
				ItemTabelaPreco item = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(tabelaPreco.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido);
				//--
				if (item != null && !ValueUtil.isEmpty(item.cdProduto)) { //Somente são válidas tabelas de preço onde o produto possua preço
					if (((menorValor > item.vlUnitario) && (item.vlUnitario > 0)) || ((item.vlUnitario > 0) && (menorValor == 0))) {
						menorValor = item.vlUnitario;
					}
					if (maiorValor < item.vlUnitario) {
						maiorValor = item.vlUnitario;
					}
					//--
					if (ValueUtil.round(item.vlUnitario) <= ValueUtil.round(itemPedido.vlItemPedido)) {
						if ((itemVlAproximado == null) || (item.vlUnitario > itemVlAproximado.vlUnitario)) {
							tabPrecoVlAproximado = tabelaPreco;
							itemVlAproximado = item;
						}
					}
				}
			}
			if ((LavenderePdaConfig.ignoraPrecoMinimoListaColunaPorTabelaPreco != 1) && !LavenderePdaConfig.usaCestaPromocional) {
				itemPedido.precoMinimoListaColunaExtrapolado = false;
				if (menorValor > itemPedido.vlItemPedido) {
					if (LavenderePdaConfig.ignoraPrecoMinimoListaColunaPorTabelaPreco == 2) {
						itemPedido.precoMinimoListaColunaExtrapolado = true;
					} else if (!(LavenderePdaConfig.liberaComSenhaPrecoProduto && itemPedido.isFlPrecoLiberadoSenha())) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VLMIN_LISTACOLUNA, itemPedido.vlItemPedido));
					}
				}
			}
			if (LavenderePdaConfig.consistePrecoMaximoListaColunaPorTabelaPreco) {
				if (maiorValor < itemPedido.vlItemPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VLMAX_LISTACOLUNA, itemPedido.vlItemPedido));
				}
			}
			if (tabPrecoVlAproximado == null) {
				return itemPedido.cdTabelaPreco;
			}
			return tabPrecoVlAproximado.cdTabelaPreco;
		}
		return "";
	}
	
	public void calculaStItemPedido(final ItemPedido itemPedido, final Cliente cliente) throws SQLException {
		Tributacao tributacao = itemPedido.getTributacaoItem(cliente);
		if (tributacao != null) {
			TributacaoVlBase tributacaoVlBase = itemPedido.getVlBaseTributacaoItem();
			STService.getInstance().aplicaSTItemPedido(itemPedido, tributacao, tributacaoVlBase);
		}
	}

	public void calculaIpiItemPedido(final ItemPedido itemPedido, boolean tribConfigUpdated) throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || tribConfigUpdated) {
			IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, itemPedido.getTributacaoConfigItem(), itemPedido.getTributacaoItem(itemPedido.pedido.getCliente()));
		} else {
			IpiService.getInstance().calculaIpiItemPedidoNormal(itemPedido);
		}
	}
	
	public void calculaIpiItemPedido(final ItemPedido itemPedido) throws SQLException {
		calculaIpiItemPedido(itemPedido, false);
	}
	
	protected void calculaValorPisECofins(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao) throws SQLException {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			if (tributacaoConfig != null && (tributacaoConfig.isCalculaPisCofins() || tributacaoConfig.isCalculaEDebitaPisCofins()) && itemPedido.getQtItemFisico() > 0) {
				double vlPctPis = tributacao != null && tributacao.vlPctPis != 0 ? tributacao.vlPctPis : itemPedido.getProduto().vlPctPis;
				double vlMinPis = tributacao != null && tributacao.vlMinPis != 0 ? tributacao.vlMinPis : itemPedido.getProduto().vlMinPis;
				vlMinPis = vlMinPis * itemPedido.getQtItemFisico();
				double vlPctCofins = tributacao != null && tributacao.vlPctCofins != 0 ? tributacao.vlPctCofins : itemPedido.getProduto().vlPctCofins;
				double vlMinCofins = tributacao != null && tributacao.vlMinCofins != 0 ? tributacao.vlMinCofins : itemPedido.getProduto().vlMinCofins;
				vlMinCofins = vlMinCofins * itemPedido.getQtItemFisico();
				double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
				boolean aplicaIndiceCondPagtoVlBasePIS = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBasePIS();
				double vlTotalItemPedidoComSeguroPIS = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBasePIS, vlIndiceCondPagto);
				double vlBasePis = tributacaoConfig.isAplicaValorFreteNaBasePisCofins() ? (vlTotalItemPedidoComSeguroPIS + itemPedido.getVlTotalItemPedidoFrete()) : vlTotalItemPedidoComSeguroPIS;
				boolean aplicaIndiceCondPagtoVlBaseCOFINS = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseCOFINS();
				double vlTotalItemPedidoComSeguroCOFINS = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseCOFINS, vlIndiceCondPagto);
				double vlBaseCofins = tributacaoConfig.isAplicaValorFreteNaBasePisCofins() ? (vlTotalItemPedidoComSeguroCOFINS + itemPedido.getVlTotalItemPedidoFrete()) : vlTotalItemPedidoComSeguroCOFINS;
				double vlReducaoIcms = tributacaoConfig.isAplicaReducaoIcmsBaseCalculoPisCofins() ? itemPedido.vlTotalIcmsItem : 0;
				itemPedido.vlTotalPisItem = ValueUtil.round((vlBasePis - vlReducaoIcms) * vlPctPis / 100, LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms);
				itemPedido.vlTotalCofinsItem = ValueUtil.round((vlBaseCofins - vlReducaoIcms) * vlPctCofins / 100, LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms);
				itemPedido.vlTotalPisItem = (itemPedido.vlTotalPisItem < vlMinPis) ? vlMinPis : itemPedido.vlTotalPisItem;
				itemPedido.vlTotalCofinsItem = (itemPedido.vlTotalCofinsItem < vlMinCofins) ? vlMinPis : itemPedido.vlTotalCofinsItem;
			}
		} else if (tributacao != null) {
			itemPedido.vlPis = itemPedido.getVlItemComFrete() * tributacao.vlPctPis / 100;
			itemPedido.vlPis = (itemPedido.vlPis < tributacao.vlMinPis) ? tributacao.vlMinPis : itemPedido.vlPis;
			itemPedido.vlCofins = itemPedido.getVlItemComFrete() * tributacao.vlPctCofins / 100;
			itemPedido.vlCofins = (itemPedido.vlCofins < tributacao.vlMinCofins) ? tributacao.vlMinCofins : itemPedido.vlCofins;
		}
	}
	
	public void calculaFecopItemPedido(final ItemPedido itemPedido, final Cliente cliente) throws SQLException {
		Tributacao tributacao = itemPedido.getTributacaoItem(cliente);
		if (tributacao != null) {
			TributacaoVlBase tributacaoVlBase = itemPedido.getVlBaseTributacaoItem();
			FecopService.getInstance().aplicaFecopNoItemPedido(itemPedido, tributacao, tributacaoVlBase);
		}
	}

	protected void calculaRentabilidadeItem(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		switch (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido) {
		case 3:
			calculaRentabilidadeComImpostosValoresAdicionais(itemPedido, pedido);
			break;
		case 5:
			calculaRentabilidadeTipo5(itemPedido);
			break;
		case 6:
		case 7:
			calculaRentabilidadeTipo6ou7(itemPedido, pedido, itemPedido.vlTotalItemPedido);
			break;
		case 8:
			calculaRentabilidadeTipo8(itemPedido, pedido);
			break;
		case 9:
			calculaRentabilidadeTipo9(itemPedido, pedido);
			break;
		default:
			calculaRentabilidadeDemaisTipos(itemPedido, pedido);
		}
	}
	
	public double calculaRentabilidadeTipo9(double vlTotalItens, double vlTotalCustoItens) {
		if (vlTotalItens <= 0) return 0;
		return ValueUtil.round(((vlTotalItens / vlTotalCustoItens) - 1) * 100D);
	}
	
	private void calculaRentabilidadeTipo9(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlTotalItemTabela = itemPedido.vlBaseItemPedido * itemPedido.qtItemFisico;
		double vlPrecoTotalCustoItemNaUnidadeAlternativa = getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido);
		double vlIndiceFinanceiro = pedido.getCondicaoPagamento().vlIndiceFinanceiro;
		vlIndiceFinanceiro = vlIndiceFinanceiro == 0 ? 1 : vlIndiceFinanceiro;
		
		itemPedido.vlRentabilidade = calculaRentabilidadeTipo9(itemPedido.vlTotalItemPedido, vlPrecoTotalCustoItemNaUnidadeAlternativa);
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			itemPedido.vlRentabilidadeSug = calculaRentabilidadeTipo9(vlTotalItemTabela, vlPrecoTotalCustoItemNaUnidadeAlternativa);
		}
	}

	private void calculaRentabilidadeTipo5(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getVlItemComSt() > 0) {
			itemPedido.vlRentabilidade = ValueUtil.round((1 - (getVlPrecoCustoRentabilidade5(itemPedido) / itemPedido.getVlItemComSt())) * 100);
		} else {
			itemPedido.vlRentabilidade = 0d;
		}
	}
	
	private double getVlPrecoCustoRentabilidade5(ItemPedido itemPedido) throws SQLException {
		double precoCusto = itemPedido.getVlPrecoCusto();
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade unidade = itemPedido.getProdutoUnidade();
			if (unidade.isMultiplica()) {
				precoCusto = precoCusto * unidade.nuConversaoUnidade;
			} else if (ProdutoUnidade.FL_DIVIDE.equals(unidade.flDivideMultiplica))  {
				precoCusto = precoCusto / unidade.nuConversaoUnidade;
			}	
		}
		return precoCusto;
	}
	
	private void calculaRentabilidadeTipo6ou7(final ItemPedido itemPedido, final Pedido pedido, double vlBase) throws SQLException {
		double vlPrecoTotalCustoItemNaUnidadeAlternativa = getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido);
		if (vlPrecoTotalCustoItemNaUnidadeAlternativa > 0) {
			itemPedido.vlRentabilidade = ValueUtil.round(((vlBase / vlPrecoTotalCustoItemNaUnidadeAlternativa) - 1) * 100);
		} else {
			itemPedido.vlRentabilidade = 0;
		}
	}

	private void calculaRentabilidadeDemaisTipos(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		double vlIcms = LavenderePdaConfig.descontaIcmsRentabilidade ? itemPedido.getVlTotalIcms() : 0;
		double vlRentabilidade = itemPedido.vlTotalItemPedido - vlIcms - (ValueUtil.round(getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido)));
		itemPedido.vlRentabilidade = ValueUtil.round(vlRentabilidade);
	}
	
	private void calculaRentabilidadeTipo8(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlVendaItem = itemPedido.vlTotalItemPedido;
		double vlPrecoCustoItem = ValueUtil.round(getVlPrecoTotalCustoItem(pedido, itemPedido) + itemPedido.getSomaValoresTribPersonalizada() + getVlRetornoProduto(itemPedido));
		double vlPctRoyalt = ProdutoClienteService.getInstance().getVlPctRoyalt(itemPedido.cdProduto, pedido.cdCliente);
		double vlPctFidelidade = ProdutoClienteService.getInstance().getVlPctFidelidade(itemPedido.cdProduto, pedido.cdCliente);
		double vlPctCustoVariavel = itemPedido.getItemTabelaPreco().vlPctDiretoria + pedido.getEmpresa().vlPctComissao + vlPctFidelidade + vlPctRoyalt;
		double vlCustoVariavel = ValueUtil.round(vlVendaItem * (vlPctCustoVariavel / 100));
		double vlCustoFinanceiro = ValueUtil.round(vlVendaItem * (pedido.getCondicaoPagamento().vlPctCustoFinanceiro / 100));
		itemPedido.vlRentabilidade = ValueUtil.round(vlVendaItem - (vlPrecoCustoItem + vlCustoVariavel + vlCustoFinanceiro));
	}

	private double getVlRetornoProduto(ItemPedido itemPedido) throws SQLException {
		return itemPedido.vlRetornoProduto * getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
	}

	public void calculaRentabilidade(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		calculaRentabilidade(pedido, itemPedido, itemPedido.flTipoEdicao);
	}
	
	public void calculaRentabilidade(Pedido pedido, ItemPedido itemPedido, int flTipoEdicao) throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemRentabilidade() || (flTipoEdicao == 0 && !itemPedido.pedido.cdCondicaoPagamentoChanged) || !pedido.isPermiteUtilizarRentabilidade()) return;
		if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalComRentabilidade(itemPedido)) {
			itemPedido.vlPctMargemRentab = itemPedido.getDescPromocional().vlPctRentabilidade;
			return;
		}
		defineCdMargemRentab(pedido);
		defineCdMargemRentab(itemPedido);
		HashMap<String, Object> variavelCalculoList = VariavelCalculoService.getInstance().getHashVariavelCalculoItemPedido(pedido, itemPedido);
		HashMap<String, Object> variaveisToSave = FormulaCalculoSqlService.getInstance().executeCalculoSql(variavelCalculoList, LavenderePdaConfig.getFormulaCalculoItemPedido(), itemPedido);
		VariavelCalculoService.getInstance().populateItemPedidoWithHashValues(pedido, itemPedido, variaveisToSave);
		itemPedido.variavelCalculoList = variaveisToSave;
	}

	protected void calculaDescontoComissao(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !pedido.isPedidoBonificacao()) {
			ComiRentabilidade comiRentabilidadeAtingidaIgnoringNeutro = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getVlPctRentabilidade(true));
			ComiRentabilidade comiRentabilidadeAtingidaNotIgnoringNeutro = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getVlPctRentabilidade(false));
			if (!pedido.getTipoPedido().isIgnoraCalculoComissao()) {
				calculaComissaoByPercentualRentabilidade(itemPedido, comiRentabilidadeAtingidaNotIgnoringNeutro, comiRentabilidadeAtingidaIgnoringNeutro);
			}
			if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao && LavenderePdaConfig.usaRentabilidadeMinimaItemPedido) {
				VerbaService.getInstance().aplicaVerbaItemPedidoPorFaixaRentabilidadeComissao(pedido, itemPedido, comiRentabilidadeAtingidaNotIgnoringNeutro);
			}
		}
	}
	
	//-- Calculo de índice de rentabilidade do itemPedido retirando os tributos que já vem aplicados no valor de venda 
	public void calculaIndiceRentabilidadeItemSemTributos(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null && itemTabelaPreco.vlPctMargemRentabilidade != 0) {
			itemPedido.getItemPedidoAud().vlPctMargemRentabilidade = itemPedido.getItemTabelaPreco().vlPctMargemRentabilidade;
			itemPedido.vlRentabilidade = (getMargemPercentual(itemPedido) * LavenderePdaConfig.indiceMinimoRentabilidadePedido / itemTabelaPreco.vlPctMargemRentabilidade) * 100;
		}
	}

	private double getMargemPercentual(ItemPedido itemPedido) throws SQLException {
		double margemValor = getMargemValor(itemPedido);
		double receivaVirtual = getReceitaVirtual(itemPedido);
		if (margemValor != 0 && receivaVirtual != 0) {
			return itemPedido.getItemPedidoAud().vlPctMargemItem = margemValor / receivaVirtual;
		}
		return 0d;
	}

	public double getReceitaVirtual(ItemPedido itemPedido) throws SQLException {
		Tributacao tributacao = itemPedido.getTributacaoItem();
		double vlVerbaEmpresa = itemPedido.getItemTabelaPreco().isFlPromocao() ? itemPedido.getItemPedidoAud().vlVerbaEmpresa : 0d;
		if (tributacao != null) {
			if (tributacao.vlIcmsRetido != 0) {
				return itemPedido.getItemPedidoAud().vlReceitaVirtual = ((itemPedido.vlItemPedido * (1 - tributacao.vlPctPis / 100)) + itemPedido.vlVerbaManual + vlVerbaEmpresa - itemPedido.vlVerbaItemPositivo - tributacao.vlIcms - tributacao.vlIcmsRetido) / (1 - (tributacao.vlPctPis + tributacao.vlPctIcms) / 100); 
			} else {
				return itemPedido.getItemPedidoAud().vlReceitaVirtual = (itemPedido.vlItemPedido + itemPedido.vlVerbaManual + vlVerbaEmpresa - itemPedido.vlVerbaItemPositivo) / (1 - (tributacao.vlPctPis + tributacao.vlPctIcms) / 100);
			}
		}
		return 0d;
	}

	public double getMargemValor(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null && itemPedido.getQtItemFisico() > 0) {
			double vlVerbaEmpresa = itemTabelaPreco.isFlPromocao() ? itemPedido.getItemPedidoAud().vlVerbaEmpresa : 0d;
			return itemPedido.getItemPedidoAud().vlMargemItem = getReceitaLiquida(itemPedido) - itemTabelaPreco.vlBase + vlVerbaEmpresa - (itemPedido.vlVerbaItemPositivo / itemPedido.getQtItemFisico()) + (itemPedido.vlVerbaItem * -1 / itemPedido.getQtItemFisico()); 
		}
		return 0d;
	}

	private double getReceitaLiquida(ItemPedido itemPedido) throws SQLException {
		Tributacao tributacao = itemPedido.getTributacaoItem();
		if (tributacao != null) {
			if (tributacao.vlIcmsRetido == 0) {
				return itemPedido.getItemPedidoAud().vlReceitaLiquida = itemPedido.vlItemPedido - (tributacao.vlPctPis / 100 * itemPedido.vlItemPedido) - (tributacao.vlPctIcms / 100 * itemPedido.vlItemPedido);
			} else {
				return itemPedido.getItemPedidoAud().vlReceitaLiquida = itemPedido.vlItemPedido - (tributacao.vlPctPis / 100 * itemPedido.vlItemPedido) - tributacao.vlIcms - tributacao.vlIcmsRetido;
			}
		}
		return 0d;
	}
	//--
	
	
	private void calculaRentabilidadeComImpostosValoresAdicionais(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (itemPedido != null && pedido != null) {
			double vlTotalItemComImpostos = itemPedido.getVlTotalItemComTributos();
			double vlPrecoTotalCustoItem = getVlPrecoTotalCustoItem(pedido, itemPedido);
			double vlGastoTotalVariavelItem = itemPedido.getVlGastoTotalVariavel();
			double vlCustoTotalFixoItem = itemPedido.getVlTotalCustoFixo();
			double vlRentabilidade = vlTotalItemComImpostos - vlPrecoTotalCustoItem - vlGastoTotalVariavelItem - vlCustoTotalFixoItem;
			itemPedido.vlRentabilidade = ValueUtil.round(vlRentabilidade);
		}
	}
	
	public double getVlPrecoTotalCustoItemNaUnidadeAlternativa(final Pedido pedido, final ItemPedido itemPedido)  throws SQLException {
		return getVlPrecoTotalCustoItem(pedido, itemPedido, LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.isUsaVerba());
	}
	
	public double getVlPrecoTotalCustoItem(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		return getVlPrecoTotalCustoItem(pedido, itemPedido, false);
	}

	private double getVlPrecoTotalCustoItem(final Pedido pedido, final ItemPedido itemPedido, boolean unidadeAlternativa)  throws SQLException {
		if (LavenderePdaConfig.calculaRentabilidadePedidoRetornado && pedido.isFlOrigemPedidoErp()) {
			return itemPedido.vlTotalPrecoCusto;
		}
		if (pedido != null && itemPedido != null) {
			double vlBaseItem = itemPedido.getItemTabelaPreco().getVlBaseRentabilidade(pedido, itemPedido);
			
			if (!(isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(itemPedido))) {
				boolean aplicouIndiceEspecialVlBase = false;
				if (LavenderePdaConfig.usaIndiceEspecialCondPagtoProd && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase) {
					double vlIndiceFinanceiro = CondPagtoProdService.getInstance().findVlIndiceFinanceiroCondPagtoPorProduto(pedido, itemPedido);
					if (vlIndiceFinanceiro > 0) {
						vlBaseItem = roundUnidadeAlternativa(vlBaseItem * vlIndiceFinanceiro);
						aplicouIndiceEspecialVlBase = true;
					} 
				}
				if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase && !aplicouIndiceEspecialVlBase && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
					double vlIndiceFinanceiro = getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido);
					if (PedidoService.getInstance().isPedidoAtingiuCota(pedido, true) && pedido.onFechamentoPedido) {
						vlIndiceFinanceiro = 1;
					}
					vlIndiceFinanceiro = vlIndiceFinanceiro != 0 ? vlIndiceFinanceiro : 1;
					vlBaseItem = roundUnidadeAlternativa(vlBaseItem * vlIndiceFinanceiro);
				}
			}			
			
			double qtItemFisico = itemPedido.getQtItemFisico();
			if (unidadeAlternativa) {
				qtItemFisico = getQtItemFisicoConversaoUnidade(itemPedido, qtItemFisico);
			}
			if (LavenderePdaConfig.usaPctManualAcrescimoCustoNoPedido) {
				double vlPctAcrescCusto = ValueUtil.getDoubleValue((String) pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO));
				if (vlPctAcrescCusto != 0) {
					return vlBaseItem * qtItemFisico + (itemPedido.vlTotalItemPedido * (vlPctAcrescCusto / 100));
				}
			}
			return itemPedido.vlTotalPrecoCusto = vlBaseItem * qtItemFisico;
		}
		return 0d;
	}

	public void calculaIndiceRentabilidadeItem(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		if (itemPedido.isItemVendaNormal() && itemPedido.getQtItemFisico() != 0) {
			double vlMinimoItem = itemPedido.getItemTabelaPreco().getVlBase(pedido, itemPedido);
			if (vlMinimoItem >= itemPedido.vlBaseItemPedido) {
				vlMinimoItem = itemPedido.getItemTabelaPreco().getVlBaseEspecial(pedido, itemPedido);
			}
			double vlSaldoNegociacao = ((itemPedido.vlItemPedido - vlMinimoItem) + ((itemPedido.vlVerbaItem * -1) / itemPedido.getQtItemFisico())) - (itemPedido.vlVerbaItemPositivo / itemPedido.getQtItemFisico());
			double vlSaldoIndices = LavenderePdaConfig.indiceRentabilidadePedido - LavenderePdaConfig.indiceMinimoRentabilidadePedido;
			double vlSaldoIdeal = itemPedido.vlBaseItemPedido - vlMinimoItem;
			double vlRentabilidade;
			if (vlSaldoIdeal > 0) {
				vlRentabilidade = ((vlSaldoNegociacao * vlSaldoIndices) / vlSaldoIdeal) + LavenderePdaConfig.indiceMinimoRentabilidadePedido;
			} else {
				vlRentabilidade = LavenderePdaConfig.indiceMinimoRentabilidadePedido;
			}
			if (vlRentabilidade > LavenderePdaConfig.indiceRentabilidadePedido) {
				itemPedido.vlRentabilidade = ValueUtil.round(LavenderePdaConfig.indiceRentabilidadePedido);
			} else {
				itemPedido.vlRentabilidade = ValueUtil.round(vlRentabilidade);
			}
		} else {
			itemPedido.vlRentabilidade = 0;
		}
	}
	
	private void calculaComissaoByPercentualRentabilidade(final ItemPedido itemPedido, ComiRentabilidade comiRentabilidadeAtingidaNotIgnoringNeutro, ComiRentabilidade comiRentabilidadeAtingidaIgnoringNeutro) throws SQLException {
		if (itemPedido != null) {
			boolean possuiDescPromocionalTabPreco = itemPedido.possuiItemTabelaPreco() && itemPedido.getItemTabelaPreco().vlPctDescPromocional > 0;
			boolean possuiGrupoDescPromocional = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido);
			boolean possuiGrupoDescPromocionalQtde = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido);
			if (possuiDescPromocionalTabPreco || possuiGrupoDescPromocional || possuiGrupoDescPromocionalQtde || (itemPedido.pedido.getTipoPedido() != null && itemPedido.pedido.getTipoPedido().isBonificacao())) {
				return;
			}
			itemPedido.vlPctComissao = (comiRentabilidadeAtingidaNotIgnoringNeutro != null && comiRentabilidadeAtingidaNotIgnoringNeutro.vlPctComissao > 0) ? comiRentabilidadeAtingidaNotIgnoringNeutro.vlPctComissao : itemPedido.vlPctComissao;
			itemPedido.vlPctComissaoTotal = (comiRentabilidadeAtingidaIgnoringNeutro != null && comiRentabilidadeAtingidaIgnoringNeutro.vlPctComissao > 0) ? comiRentabilidadeAtingidaIgnoringNeutro.vlPctComissao : itemPedido.vlPctComissaoTotal;
		}
	}
	
	public double calculaVlUnidadePorEmbalagemWhenProdutoBase(ItemPedido itemPedido, Pedido pedido, ProdutoBase produtoBase, String cdTabelaPreco) throws SQLException {
		if (cdTabelaPreco == null) {
			itemPedido.cdTabelaPreco = ValueUtil.isEmpty(pedido.cdTabelaPreco) ? itemPedido.cdTabelaPreco : pedido.cdTabelaPreco;
		} else {
			itemPedido.cdTabelaPreco = cdTabelaPreco;
		}
		double retorno;
		if (itemPedido.getProduto() == null) {
			if (produtoBase instanceof Produto) {
				itemPedido.setProduto((Produto) produtoBase);
			} else {
				Produto produto = new Produto();
				produto.cdEmpresa = pedido.cdEmpresa;
				produto.cdRepresentante = pedido.cdRepresentante;
				produto.cdProduto = produtoBase.cdProduto;
				itemPedido.setProduto((Produto) ProdutoService.getInstance().findByPrimaryKey(produto));
			}
			if (itemPedido.cdUnidade == null) {
				itemPedido.setProdutoUnidade(ProdutoUnidadeService.getInstance().getProdutoUnidadePadrao(produtoBase, false, itemPedido));
				itemPedido.cdUnidade = itemPedido.getProdutoUnidade().cdUnidade;
			}
			aplicaIndices(itemPedido, pedido);
			double vlPctMaxDesconto = getVlPctMaxDescontoItemPedido(itemPedido);
			if (itemPedido.pedido.vlPctDescItem > vlPctMaxDesconto) {
				itemPedido.vlPctDesconto = vlPctMaxDesconto;
			} else {
				itemPedido.vlPctDesconto = itemPedido.pedido.vlPctDescItem;
			}
			itemPedido.vlPctAcrescimo = getVlPctAcrescimoPoliticaComercial(itemPedido.pedido.vlPctAcrescimoItem, itemPedido);
			aplicaDescontoAcrescimoDoPedido(itemPedido);
			itemPedido.qtEmbalagemElementar =  produtoBase.qtEmbalagemElementar;
			itemPedido.getProduto().vlProduto = produtoBase.vlProduto;
			itemPedido.vlItemPedido = calculaVlItemByVlElementar(itemPedido, itemPedido.vlItemPedido);
			retorno = calculaVlUnidadePorEmbalagem(itemPedido, pedido);
			itemPedido.setProduto(null);
		} else {
			retorno = calculaVlUnidadePorEmbalagem(itemPedido, pedido);
		}
		return retorno;
	}

	private void aplicaIndices(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
		boolean aplicouIndiceFinanCondPagtoProd = aplicaIndiceFinanceiroCondPagtoProd(itemPedido, pedido);
		loadPoliticaComercial(itemPedido, pedido);
		applyIndiceFinanceiroPlataformaVendaCliFin(itemPedido, pedido);
		
		if (!aplicouIndiceFinanCondPagtoProd) {
			applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		}
	}
	
	public double calculaVlUnidadePorEmbalagem(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlUnidadeEmbalagem = itemPedido.vlItemPedido;
		if (itemPedido.getProduto() != null) {
			if (LavenderePdaConfig.usaUnidadeAlternativa && !LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
				vlUnidadeEmbalagem = getVlUnidadeEmbalagem(itemPedido);
			} else {
				double nuConversaoUnidadeMedida = itemPedido.getProduto().nuConversaoUnidadesMedida != 0 ? itemPedido.getProduto().nuConversaoUnidadesMedida : itemPedido.getProdutoBase() != null && itemPedido.getProdutoBase().nuConversaoUnidadesMedida != 0 ? itemPedido.getProdutoBase().nuConversaoUnidadesMedida : 1d;
				vlUnidadeEmbalagem = (itemPedido.vlItemPedido != 0 ? itemPedido.vlItemPedido : itemPedido.getProduto().vlProduto) / nuConversaoUnidadeMedida;
				if (!"2".equals(LavenderePdaConfig.mostraValorDaUnidadePrecoPorEmbalagem)) {
					if (LavenderePdaConfig.calculaStSimplificadaItemPedido && pedido.getCliente().isFlAplicaSt()) {
						double vlStUnidade = itemPedido.getProduto().vlSt > 0 ? itemPedido.getProduto().vlSt / nuConversaoUnidadeMedida : 0;
						vlUnidadeEmbalagem += vlStUnidade;
					} else if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
						calculaStItemPedido(itemPedido, pedido.getCliente());
						double vlStUnidade = itemPedido.vlSt > 0 ? itemPedido.vlSt / nuConversaoUnidadeMedida : 0;
						vlUnidadeEmbalagem += vlStUnidade;
					}
				}
			}
		}
		return vlUnidadeEmbalagem;
	}
	
	public double getVlUnidadeEmbalagem(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProdutoUnidade() != null) {
			if (itemPedido.getProdutoUnidade().isMultiplica()) {
				return itemPedido.vlItemPedido / ((itemPedido.qtEmbalagemElementar > 0) ? itemPedido.qtEmbalagemElementar : itemPedido.getProdutoUnidade().nuConversaoUnidade);
			} else {
				return itemPedido.vlItemPedido * ((itemPedido.qtEmbalagemElementar > 0) ? itemPedido.qtEmbalagemElementar : itemPedido.getProdutoUnidade().nuConversaoUnidade);
			}
		}
		return itemPedido.vlItemPedido;
	}

	public void calculateComissaoItem(ItemPedido itemPedido, Pedido pedido) throws SQLException {	
		double pctComissao = 0;
		if (!pedido.isPedidoFechado() && pedido.getTipoPedido() != null && !pedido.getTipoPedido().isIgnoraCalculoComissao() && !pedido.isFlOrigemPedidoErp()) {
			if ((LavenderePdaConfig.aplicaComissaoEspecialProdutoPromocional && DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido))) {
				if (itemPedido.descPromocional != null) {
					pctComissao = itemPedido.descPromocional.vlPctComissao;
				}
			} else if ((LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem())) {
				pctComissao = itemPedido.vlPctComissao;
			} else if (LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo()) {
				pctComissao = ComiTabPrecoGrupoService.getInstance().getPctComissaoByItemPedido(itemPedido);
			} else if (LavenderePdaConfig.usaConfigCalculoComissao()) {
				pctComissao = getPctComissaoComConfigurador(itemPedido, pedido);
			}
		}
		if (!pedido.isPedidoFechado() && !pedido.isFlOrigemPedidoErp()) itemPedido.vlPctComissao = pctComissao;

		itemPedido.vlTotalComissao = ValueUtil.round((itemPedido.vlTotalItemPedido * itemPedido.vlPctComissao) / 100);
		if (itemPedido.getProduto().isNeutro()) {
			itemPedido.vlPctComissao = 0;
			itemPedido.vlTotalComissao = 0;
		} else if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			ComissaoPedidoRepService.getInstance().applyComissaoPedidoRepInItemPedido(itemPedido);
		}
	}

	private double getPctComissaoComConfigurador(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double pctComissao = 0;
		String[] configList = LavenderePdaConfig.getConfigCalculoComissao();
		for(String config : configList) {
			switch (config) {
			case "1":
				pctComissao = getPctComissaoByTabelaPrecoForRepresentante(itemPedido, pedido);
				break;
			case "2":
				pctComissao = getPctComissaoByItemTabelaPreco(itemPedido);
				break;
			case "3":
				pctComissao = getPctComissaoByFaixaDesconto(itemPedido, pedido, pctComissao);
				break;
			case "4":
				pctComissao = ComissaoGrupoFaixaNegService.getInstance().findVlPctComissaoByGrupoProduto(itemPedido);
				break;
			case "5":
				pctComissao = itemPedido.getPctComissaoByPoliticaComercialFaixa();
				break;
			case "6":
				pctComissao = getPctComissaoByTabelaPrecoRepForRepresentante(itemPedido, pedido);
				break;
			}
			if (pctComissao != 0) break; 
		}
		pctComissao = aplicaIndiceDeComissaoCliente(pedido, pctComissao);
		pctComissao = aplicaIndiceDeComissaoItemTabelaPreco(itemPedido, pctComissao);
		pctComissao = aplicaVariacaoComissaoProduto(itemPedido, pctComissao);
		pctComissao = aplicaVariacaoProdutoRestrito(itemPedido, pctComissao);
		return pctComissao;
	}
	
	private double aplicaVariacaoProdutoRestrito(ItemPedido itemPedido, double pctComissao) throws SQLException {
		if (LavenderePdaConfig.usaProdutoRestrito) {
		double nuMultiplicadorComissao = LavenderePdaConfig.getNuMultiplicadorComissao();
			if (nuMultiplicadorComissao > 0 && ValueUtil.VALOR_SIM.equals(ProdutoService.getInstance().findColumnByRowKey(itemPedido.getProduto().getRowKey(), "FLPRODUTORESTRITO"))) {
			return pctComissao * nuMultiplicadorComissao;
		}
		}
		return pctComissao;
	}

	private double getPctComissaoByItemTabelaPreco(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco item = itemPedido.getItemTabelaPreco();
		return item != null ? item.vlPctComissao : 0;
	}

	private double aplicaVariacaoComissaoProduto(ItemPedido itemPedido, double pctComissao) throws SQLException {
		Produto produto = itemPedido.getProduto();
		double variacao = produto.vlPctVariacaoComissao;
		variacao += pctComissao;
		return variacao >= 0 ? variacao : 0;
		
	}

	private double aplicaIndiceDeComissaoCliente(Pedido pedido, double pctComissao) throws SQLException {
		double indice = pedido.getCliente().vlIndiceComissao;
		if (indice > 0) {
			pctComissao *= 	indice;
		}
		return pctComissao;
	}
	
	private double aplicaIndiceDeComissaoItemTabelaPreco(ItemPedido itemPedido, double pctComissao) throws SQLException {
		double indice = itemPedido.getItemTabelaPreco().vlIndiceComissao;
		if (indice > 0) {
			pctComissao *= indice;
		}
		return pctComissao;
	}

	private double getPctComissaoByFaixaDesconto(final ItemPedido itemPedido, final Pedido pedido, double pctComissao) throws SQLException {
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		if (itemTabPreco != null) {
			if (pctComissao <= 0) {
				pctComissao = calculaComissaoFaixaDesconto(itemPedido);
			}
		}
		return pctComissao;
	}

	private double getPctComissaoByTabelaPrecoForRepresentante(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		double pctComissao = getPctComissaoByTabelaPreco(itemPedido);
		Representante rep = RepresentanteService.getInstance().getRepresentanteById(pedido.cdRepresentante);
		TabelaPreco tab = TabelaPrecoService.getInstance().getTabelaPreco(itemPedido.cdTabelaPreco);
		if (rep == null || tab == null) return pctComissao;
		if (rep.isRepresentanteInterno() && !ValueUtil.isEmpty(tab.vlIndiceComissaoRepInterno)) return pctComissao * tab.vlIndiceComissaoRepInterno;
		return pctComissao;
	}

	private double getPctComissaoByTabelaPreco(final ItemPedido itemPedido) throws SQLException {
		TabelaPreco tabPreco = itemPedido.getTabelaPreco();
		if (tabPreco != null) return tabPreco.vlPctComissao;
		tabPreco = itemPedido.getItemTabelaPreco().getTabelaPreco();
		if (tabPreco != null) return tabPreco.vlPctComissao;
		
		return 0;
	}
	
	private double getPctComissaoByTabelaPrecoRepForRepresentante(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		double pctComissao = getPctComissaoByTabelaPrecoRep(itemPedido);
		Representante rep = RepresentanteService.getInstance().getRepresentanteById(pedido.cdRepresentante);
		TabelaPrecoRep tab = TabelaPrecoRepService.getInstance().getTabelaPrecoRep(itemPedido.cdTabelaPreco);
		if (rep == null || tab == null) return pctComissao;
		return pctComissao;
	}
	
	private double getPctComissaoByTabelaPrecoRep(final ItemPedido itemPedido) throws SQLException {
		TabelaPrecoRep tabPreco = itemPedido.getTabelaPrecoRep();
		if(tabPreco != null) return tabPreco.vlPctComissao;
		tabPreco = itemPedido.getItemTabelaPreco().getTabelaPrecoRep();
		if (tabPreco != null) return tabPreco.vlPctComissao;
		
		return 0;
	}
	
	private double calculaComissaoFaixaDesconto(ItemPedido itemPedido) throws SQLException {
		double vlTotalDescontos = getVlTotalDescontos(itemPedido);
		DescontoComissao descontoComissao = DescontoComissaoService.getInstance().findDescComissaoByFaixa(itemPedido.cdEmpresa, itemPedido.cdRepresentante, vlTotalDescontos);
		if (descontoComissao == null) return 0;
		if (descontoComissao.isAplicaIndiceDesconto()) {
			double vlPctComissao = descontoComissao.vlPctComissao - descontoComissao.vlIndiceDesconto * (vlTotalDescontos - descontoComissao.vlPctDescontoMin);
			return vlPctComissao < descontoComissao.vlPctComissaoMin ? descontoComissao.vlPctComissaoMin : vlPctComissao;
		}
		return descontoComissao.vlPctComissao;
	}

	private double getVlTotalDescontos(ItemPedido itemPedido) {
		if (LavenderePdaConfig.usaConfigCalculoComissaoPorFaixaDesconto()) {
			return getVlTotalDescontosByConfig(itemPedido);
		} else {
			return  itemPedido.vlPctDescCliente + itemPedido.vlPctDescontoPromo + itemPedido.vlPctDesconto;
		}
	}

	private double getVlTotalDescontosByConfig(ItemPedido itemPedido) {
		String[] configList = LavenderePdaConfig.getConfigCalculoComissaoPorFaixaDesconto();
		double vlDesconto = 0;
		for(String config : configList) {
			switch (config) {
			case "1":
				vlDesconto += itemPedido.vlPctDescCliente;
				break;
			case "2":
				vlDesconto += itemPedido.vlPctDescontoPromo;
				break;
			case "3":
				vlDesconto += itemPedido.vlPctDesconto;
				break;
			}
		}
		return vlDesconto;
	}
	

	public void aplicarConversaoUnidadeMedida(final ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
			double nuConversao;
			if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado && pedido.getCliente().isCreditoAntecipado()) {
				nuConversao = itemPedido.getProduto().nuConversaoUMCreditoAntecipado;
			} else {
				nuConversao = itemPedido.getProduto().nuConversaoUnidadesMedida;
			}
			if (nuConversao == 0) {
				nuConversao = 1;
			}
			if (itemPedido.flEditandoQtItemFaturamento) {
				itemPedido.setQtItemFisico(itemPedido.qtItemFaturamento * nuConversao);
			} else if (nuConversao != 0) {
				itemPedido.qtItemFaturamento = itemPedido.getQtItemFisico() / nuConversao;
			}
		}
	}

	public void applyRedutorOptanteSimples(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && !LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
			if (cliente != null && cliente.isOptanteSimples()) {
				double vlReducaoOptanteSimples = itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples;
				itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido - vlReducaoOptanteSimples);
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlReducaoOptanteSimples = vlReducaoOptanteSimples;
				itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao - vlReducaoOptanteSimples);
			}
		}
	}
	
	public void applyRedutorOptanteSimplesAposCalculoValorItem(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
			if (cliente != null && cliente.isOptanteSimples()) {
				itemPedido.vlItemPedido -= itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples;
				itemPedido.vlReducaoOptanteSimples = itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples;
			}
		}
	}

	public double getIndiceFinanceiroCliente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido() && pedido.getCliente() != null) {
			double vlIndiceFinanceiro = 1;
			int nuCasasDecimaisCalcIndicFinanceiroCliente = LavenderePdaConfig.getNuCasasDecimaisCalcIndicFinanceiroCliente();
			if ((pedido.getCliente().vlIndiceFinanceiro > 0) && ((pedido.getTipoPedido() == null) || !pedido.getTipoPedido().isFlIgnorarIndiceFinanCli())) {
				vlIndiceFinanceiro = pedido.getCliente().vlIndiceFinanceiro;
				if (nuCasasDecimaisCalcIndicFinanceiroCliente > 0) {
					vlIndiceFinanceiro = ValueUtil.round(vlIndiceFinanceiro, nuCasasDecimaisCalcIndicFinanceiroCliente);
				}
			}
			return vlIndiceFinanceiro;
		}
		return 0d;
	}

	public void applyIndiceFinanceiroCliente(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"3".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido) 
				|| LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata
				|| LavenderePdaConfig.isAplicaDescontoCategoria()) {
			return;
		}
		double vlIndiceFinanceiro = getIndiceFinanceiroCliente(pedido);
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() && vlIndiceFinanceiro < 1) {
			return;
		}
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedido.vlPctDescCliente = 0;
			itemPedido.vlPctAcrescCliente = 0;
		}
		if (vlIndiceFinanceiro != 0) {
			double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlBaseItemPedido = vlBaseItemPedido;
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
			if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				if (vlIndiceFinanceiro < 1) {
					itemPedido.vlPctDescCliente = (1 - vlIndiceFinanceiro) * 100;
				} else {
					itemPedido.vlPctAcrescCliente = (vlIndiceFinanceiro - 1) * 100;
				}
			}
		}
	}

	//--aplica indice Politica Comercial
	public void applyIndiceFinanceiroPlataformaVendaCliFin(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			if (itemPedido.politicaComercial != null  && itemPedido.politicaComercial.acumulaIndices()) {
				PlataformaVendaCliFin plataformaVendaCliFin = itemPedido.politicaComercial.plataformaVendaCliFin;
			if (plataformaVendaCliFin != null) {
				double vlIndiceFinanceiro = plataformaVendaCliFin.vlIndiceFinanceiro;
				if (vlIndiceFinanceiro != 0) {
					itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
						// Não adicionar round, pois o erp é necessário ter o valor exato!
					itemPedido.vlIndicePlataformaVendaCliFin = vlIndiceFinanceiro;
				}
			}
		}
	}
	}
	
	public void applyIndiceFinanceiroCondPagto(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(itemPedido)) return;
		
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"4".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) return;
		
		//-- Aplica o índice
		applyIndiceFinanceiroCondPagtosVlItemPedido(itemPedido, pedido);
		aplicaJurosComBaseDiasCondPagto(itemPedido, pedido.getCondicaoPagamento());
		if (!LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) return;
		
		applyIndiceFinanceiroCliente(itemPedido, pedido);
	}

	public boolean isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(ItemPedido itemPedido) throws SQLException {
		String flPromocional = ValueUtil.VALOR_NAO;
		if (LavenderePdaConfig.anulaIndiceFinanceiroCondPagtoTabPrecoProm) {
			if (itemPedido.pedido != null && itemPedido.pedido.getTabelaPreco() != null && ValueUtil.valueEquals(itemPedido.pedido.cdTabelaPreco, itemPedido.cdTabelaPreco)) {
				flPromocional = itemPedido.pedido.getTabelaPreco().flPromocional;
			} else {
				flPromocional = TabelaPrecoService.getInstance().getFlPromocionalFromCache(itemPedido);
			}
		}
		return (LavenderePdaConfig.anulaIndiceFinanceiroCondPagtoTabPrecoProm && (ValueUtil.VALOR_SIM.equals(flPromocional)))
				|| isIgnoraIndiceFinanceiroCondPagtoProdutoPromoOrDescPromocional(itemPedido);
	}
	
	private boolean isIgnoraIndiceFinanceiroCondPagtoProdutoPromoOrDescPromocional(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.ignoraIndiceFinanceiroCondPagtoProdutoPromocional && (itemPedido.getItemTabelaPreco() != null && ValueUtil.VALOR_SIM.equals(itemPedido.getItemTabelaPreco().flPromocao) || DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido));
	}

	public boolean ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(ItemPedido itemPedido) throws SQLException {
		String flPromocional = ValueUtil.VALOR_NAO;
		if (LavenderePdaConfig.ignoraIndiceFinanceiroCondComercialProdutoPromocional) {
			if (itemPedido.pedido != null && itemPedido.pedido.getTabelaPreco() != null && ValueUtil.valueEquals(itemPedido.pedido.cdTabelaPreco, itemPedido.cdTabelaPreco)) {
				flPromocional = itemPedido.pedido.getTabelaPreco().flPromocional;
			} else {
				flPromocional = TabelaPrecoService.getInstance().getFlPromocionalFromCache(itemPedido);
			}
		}
		return (LavenderePdaConfig.ignoraIndiceFinanceiroCondComercialProdutoPromocional && (itemPedido.getItemTabelaPreco() != null && ValueUtil.VALOR_SIM.equals(itemPedido.getItemTabelaPreco().flPromocao)))
				|| (LavenderePdaConfig.ignoraIndiceFinanceiroCondComercialProdutoPromocional && ValueUtil.VALOR_SIM.equals(flPromocional));
	}

	public void applyIndiceFinanceiroCondPagtosVlItemPedido(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlIndiceFinanceiro = getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido);
		vlIndiceFinanceiro = PedidoService.getInstance().isPedidoAtingiuCota(pedido) && pedido.onFechamentoPedido ? 1 : vlIndiceFinanceiro;
		int NuCasasDecimaisCalcIndicFinanceiro  = LavenderePdaConfig.getNuCasasDecimaisCalcIndicFinanceiro();
		
		zeraDescontosSequenciaisNoItemPedido(itemPedido);
		if (vlIndiceFinanceiro == 0) return;
		
		double vlBaseItemPedido = itemPedido.vlBaseItemPedido * vlIndiceFinanceiro;
		vlBaseItemPedido = NuCasasDecimaisCalcIndicFinanceiro != 0 ? ValueUtil.round(vlBaseItemPedido, NuCasasDecimaisCalcIndicFinanceiro) : roundUnidadeAlternativa(vlBaseItemPedido); 
		itemPedido.vlBaseItemPedido = vlBaseItemPedido;
		itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
		itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
		itemPedido.vlBaseFlex = vlBaseItemPedido;
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			itemPedido.vlIndiceCondicaoPagto = vlIndiceFinanceiro;
		}
		if (!LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) return;
			
		if (vlIndiceFinanceiro < 1) {
			itemPedido.vlPctDescontoCondicao = (1 - vlIndiceFinanceiro) * 100;
		} else {
			itemPedido.vlPctAcrescimoCondicao = (vlIndiceFinanceiro - 1) * 100;
		}
	}
	
	private void zeraDescontosSequenciaisNoItemPedido(ItemPedido itemPedido) {
		if (!LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) return;
		
		itemPedido.vlPctDescontoCondicao = 0;
		itemPedido.vlPctAcrescimoCondicao = 0;
	}

	private void zeraDescAcresItemPedido(ItemPedido itemPedido) {
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlPctAcrescimo = 0;
		itemPedido.vlPctFaixaDescQtd = 0;
	}
	
	public void applyIndiceFinanceiroCondPagtosVlItemPedidoMantendoPreco(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido);
		ItemPedido itemPedidoOld = (ItemPedido) findByRowKey(itemPedido.getRowKey());
		if (itemPedidoOld != null && itemPedidoOld.vlItemPedido != 0) {
			double desconto =  (itemPedidoOld.vlItemPedido * 100) / vlBaseItemPedido;
			itemPedido.vlPctDesconto = 0;
			itemPedido.vlPctAcrescimo = 0;
			if (vlBaseItemPedido < itemPedidoOld.vlItemPedido) {
				itemPedido.vlPctAcrescimo += desconto - 100;
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
			} else if (vlBaseItemPedido > itemPedidoOld.vlItemPedido) {
				itemPedido.vlPctDesconto += 100 - desconto;
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			}
			itemPedido.vlItemPedido = itemPedidoOld.vlItemPedido; 
		} 
	}

	public double getIndiceFinanceiroCondPagtoVlItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() || LavenderePdaConfig.isAplicaDescontoCategoria() || pedido.getCondicaoPagamento() == null) return 0d;
		
		if (LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio()) {
			return getVlIndiceFinanceiroCondicaoPagamentoConformePrazoMedio(pedido.getCondicaoPagamento(), pedido.getCliente());
		}
		if ((pedido.getCliente() != null) && (pedido.getCliente().qtDiasPrazoPagtoEstendido > 0)) {
			return getVlIndiceFinanceiroCondicaoPagamentoDisponivel(pedido); 
		} 
		return getVlIndiceFinanceiroCondicaoPagamento(pedido, itemPedido);
	}
	
	protected double getVlIndiceFinanceiroCondicaoPagamentoConformePrazoMedio(CondicaoPagamento condicaoPagamentoPedido, Cliente cliente) throws SQLException {
		if (condicaoPagamentoPedido == null || cliente == null) return 0;
		
		if (cliente.vlIndiceFinanceiroCondPagto == 0) return condicaoPagamentoPedido.vlIndiceFinanceiro;
		
		int qtDiasMediosPagamentoCliente = condicaoPagamentoPedido.qtDiasMediosPagamento;
		if (!ValueUtil.valueEquals(condicaoPagamentoPedido.cdCondicaoPagamento, cliente.cdCondicaoPagamento)) {
			qtDiasMediosPagamentoCliente = CondicaoPagamentoService.getInstance().findQtDiasMediosPagamento(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCondicaoPagamento);
		}
		if (qtDiasMediosPagamentoCliente < condicaoPagamentoPedido.qtDiasMediosPagamento) {
			return condicaoPagamentoPedido.vlIndiceFinanceiro;
		}
		return cliente.vlIndiceFinanceiroCondPagto < condicaoPagamentoPedido.vlIndiceFinanceiro ? cliente.vlIndiceFinanceiroCondPagto : condicaoPagamentoPedido.vlIndiceFinanceiro;
	}

	private double getVlIndiceFinanceiroCondicaoPagamentoDisponivel(Pedido pedido) throws SQLException {
		Vector listCondicoes = pedido.getCondicoesPagamentoDisponiveis();
		if (ValueUtil.isEmpty(listCondicoes)) return 0;
		
		int qtDiasIndice = pedido.getCondicaoPagamento().qtDiasMediosPagamento - pedido.getCliente().qtDiasPrazoPagtoEstendido;
		CondicaoPagamento condPagtoIndiceFinanceiro = null;
		for (int i = 0; i < listCondicoes.size(); i++) {
			CondicaoPagamento condPagtoTemp = (CondicaoPagamento) listCondicoes.items[i];
			if (condPagtoTemp.qtDiasMediosPagamento >= qtDiasIndice && (condPagtoIndiceFinanceiro == null || condPagtoTemp.qtDiasMediosPagamento < condPagtoIndiceFinanceiro.qtDiasMediosPagamento)) {
				condPagtoIndiceFinanceiro = condPagtoTemp;
			}
		}
		return condPagtoIndiceFinanceiro != null ? condPagtoIndiceFinanceiro.vlIndiceFinanceiro : 0;
	}
	
	private double getVlIndiceFinanceiroCondicaoPagamento(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao() && pedido.isGeraCreditoCondicao() && pedido.getCondicaoPagamento().vlIndiceCredito < 1 && pedido.getCondicaoPagamento().vlIndiceCredito > 0) {
			itemPedido.vlCreditoCondicao = getVlCreditoCondicao(itemPedido);
			return 1d;
		} else {
			itemPedido.vlCreditoCondicao = 0d;
		}
		return pedido.getCondicaoPagamento().vlIndiceFinanceiro > 0.0 ?  pedido.getCondicaoPagamento().vlIndiceFinanceiro : 0;
	}
	
	private double getVlCreditoCondicao(ItemPedido itemPedido) throws SQLException {
		return itemPedido.vlBaseItemPedido * (1 - itemPedido.pedido.getCondicaoPagamento().vlIndiceCredito);
	}

	public void aplicaIndiceFinanceiroCondComercial(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlUnitarioCondComercialExcec = 0;
		if (itemPedido.getItemTabelaPreco() != null) {
			itemPedido.getItemTabelaPreco().condComercialExcec.cdProduto = null;
			String cdCondicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? itemPedido.cdCondicaoComercial : pedido.cdCondicaoComercial;
			vlUnitarioCondComercialExcec = itemPedido.getItemTabelaPreco().getCondComercialExcec(cdCondicaoComercial, itemPedido.cdItemGrade1).vlUnitario;
		}
		if (ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(itemPedido) || vlUnitarioCondComercialExcec != 0) {
			return;
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido || LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
			double vlIndiceFinanceiro = 1d;
			if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido && ValueUtil.isNotEmpty(itemPedido.cdCondicaoComercial)) {
				if (itemPedido.getCondicaoComercial() != null && itemPedido.getCondicaoComercial().vlIndiceFinanceiro > 0.0) {
					vlIndiceFinanceiro = itemPedido.getCondicaoComercial().vlIndiceFinanceiro;
				}
			} else if (pedido.getCondicaoComercial() != null && pedido.getCondicaoComercial().vlIndiceFinanceiro > 0.0) {
				vlIndiceFinanceiro = pedido.getCondicaoComercial().vlIndiceFinanceiro;
			}
			if (vlIndiceFinanceiro != 1) {
				double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
				itemPedido.vlBaseItemPedido = vlBaseItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
				itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
			}
		}
	}

	public void aplicaIndiceFinanceiroCondPagamentoPorCondComercial(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(itemPedido)) {
			return;
		}
		double vlIndiceFinanceiro = getIndiceFinanceiroCondPagamentoPorCondComercial(pedido, itemPedido.cdCondicaoComercial);
		if (vlIndiceFinanceiro != 0) {
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
		}
	}

	public double getIndiceFinanceiroCondPagamentoPorCondComercial(Pedido pedido, String cdCondicaoComercial) throws SQLException {
		if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial || LavenderePdaConfig.usaCondComercialPorCondPagto) {
			CondComCondPagto condComCondPagto = new CondComCondPagto();
			condComCondPagto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condComCondPagto.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCondPagto.class);
			condComCondPagto.cdCondicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? cdCondicaoComercial : pedido.cdCondicaoComercial;
			condComCondPagto.cdCondicaoPagamento = pedido.getCondicaoPagamento() != null ? pedido.getCondicaoPagamento().cdCondicaoPagamento : pedido.cdCondicaoPagamento;
			condComCondPagto = (CondComCondPagto) CondComCondPagtoService.getInstance().findByRowKey(condComCondPagto.getRowKey());
			if (condComCondPagto != null) {
				if (condComCondPagto.vlIndiceFinanceiro > 0.0) {
					return condComCondPagto.vlIndiceFinanceiro;
				}
			}
		}
		return 0d;
	}

	public void aplicaVariacaoPrecoProdutoRegiaoECategoriaDoCliente(ItemPedido itemPedido) throws SQLException {
		if (aplicaVariacaoPrecoProdCli(itemPedido)) {
			return;
		}
		if (LavenderePdaConfig.usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente && itemPedido.pedido.getCliente() != null) {
			double vlVariacaoPreco = getVlVariacaoPrecoProduto(itemPedido);
			if (vlVariacaoPreco != 0) {
				itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlVariacaoPreco);
				itemPedido.vlItemTabPrecoVariacaoPreco = itemPedido.vlBaseItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlVariacaoPreco);
			}
		}
	}

	private boolean aplicaVariacaoPrecoProdCli(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaVariacaoPrecoProdutoPorCliente && itemPedido.pedido.getCliente() != null) {
			double vlVariacaoPreco = getVlVariacaoPrecoProdCli(itemPedido);
			if (vlVariacaoPreco != 0 && vlVariacaoPreco != 1) {
				itemPedido.vlBaseItemPedido = !LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa ? roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlVariacaoPreco) : itemPedido.vlBaseItemPedido * vlVariacaoPreco;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlUnidadePadrao = !LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa ? roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlVariacaoPreco) : itemPedido.vlUnidadePadrao * vlVariacaoPreco;
				return  true;
			}
		}
		return false;
	}

	public double getVlVariacaoPrecoProdCli(ItemPedido itemPedido) throws SQLException {
		double vlVariacao = VariacaoProdCliService.getInstance().getVlVariacaoPrecoProdCli(itemPedido); 
		return 1 + vlVariacao / 100;
	}
	
	public double getVlVariacaoPrecoProduto(ItemPedido itemPedido) throws SQLException {
		double vlVariacaoPreco = 0d;
		double vlVariacaoPrecoExcec = VariacaoProdExcecService.getInstance().getVlVariacaoPrecoProduto(itemPedido);
		double vlVariacaoPrecoRegiao = VariacaoProdRegService.getInstance().getVlVariacaoPrecoProduto(itemPedido);
		double vlVariacaoCategoria = VariacaoProdCategService.getInstance().getVlVariacaoPrecoProduto(itemPedido);
		vlVariacaoPreco = 1 + (vlVariacaoPrecoExcec + vlVariacaoPrecoRegiao + vlVariacaoCategoria) / 100;
		return vlVariacaoPreco;
	}

	/**
	 *
	 * @param itemPedido
	 * @param condicaoPagamento
	 *            Valor base usado no cálculo do índice da condição de
	 *            pagamento, pode ser o valor de tabela, ou então, caso haja
	 *            outros índices, como o do cliente, o valor base passa a ser o
	 *            valor já ajustado.
	 */
	private void applyIndiceFinanceiroCondPagtoPorDias(final ItemPedido itemPedido, final CondicaoPagamento condicaoPagamento) throws SQLException {
		if (LavenderePdaConfig.indiceFinanceiroCondPagtoPorDias) {
			if (condicaoPagamento != null) {
				double vlIndice = 0.0;
				if (LavenderePdaConfig.indiceFinanceiroCondPagtoClientePorDias) {
					Pedido pedido = new Pedido();
					pedido.cdEmpresa = itemPedido.cdEmpresa;
					pedido.cdRepresentante = itemPedido.cdRepresentante;
					pedido.nuPedido = itemPedido.nuPedido;
					pedido.flOrigemPedido = itemPedido.flOrigemPedido;
					String cdCliente = PedidoService.getInstance().findColumnByRowKey(pedido.getRowKey(), "CDCLIENTE");
					if (cdCliente != null) {
						Cliente cliente = new Cliente();
						cliente.cdEmpresa = pedido.cdEmpresa;
						cliente.cdRepresentante = pedido.cdRepresentante;
						cliente.cdCliente = cdCliente;
						vlIndice = ValueUtil.getDoubleValue(ClienteService.getInstance().findColumnByRowKey(cliente.getRowKey(), "VLINDICEFINANCEIROCONDPAGTO"));
					}
				} else {
					vlIndice = condicaoPagamento.vlIndiceFinanceiro;
				}
				if (vlIndice == 0) {
					vlIndice = 1;
				}
				if (PedidoService.getInstance().isPedidoAtingiuCota(findPedidoByItemPedido(itemPedido))) {
					vlIndice = 1;
				}
				
				if (vlIndice != 0) {
					//Faz a conversão por dias
					int dias = condicaoPagamento.qtDiasMediosPagamento;
					if (vlIndice > 1.0) {
						vlIndice = (((vlIndice - 1.0) / 30) * dias) + 1;
					} else if (vlIndice < 1.0) {
						vlIndice = 1 - (((1 - vlIndice) / 30) * dias);
					}
					//Calcula e atribui o novo valor
					itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndice);
					itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
					itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
					itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndice);
				}
			}
		}
	}

	public void getAndApplyIndiceFinanceiroLinhaProdutoNoItemPedido(final ItemPedido itemPedido, final String cdCondicaoPagamento, final Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0) {
			CondPagtoLinha condLinha = new CondPagtoLinha();
			condLinha.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condLinha.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			condLinha.cdCondicaoPagamento = cdCondicaoPagamento;
			if (itemPedido.getProduto() != null) {
				condLinha.cdLinha = itemPedido.getProduto().cdLinha;
				condLinha.cdRegiao = "0";
				if (LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido == 2 && cliente != null) {
					condLinha.cdRegiao = cliente.cdRegiao;
				}
				condLinha = (CondPagtoLinha) CondPagtoLinhaService.getInstance().findByRowKey(condLinha.getRowKey());
				aplicaIndiceFinanceiroLinhaProdutoNoItemPedido(itemPedido, condLinha);
			}
		}
	}

	public void aplicaIndiceFinanceiroLinhaProdutoNoItemPedido(final ItemPedido itemPedido, final CondPagtoLinha condLinha) {
		if (LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0) {
			double vlIndiceFinanceiro = 1;
			if ((condLinha != null) && (condLinha.vlIndiceFinanceiro != 0)) {
				vlIndiceFinanceiro = condLinha.vlIndiceFinanceiro;
			}
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemTabelaPreco * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		}
	}

	public void aplicaIndiceFinanceiroClienteNoItemPedido(final ItemPedido itemPedido, double vlIndiceFinanceiroCliente, final boolean indiceEspecial) throws SQLException {
		if (vlIndiceFinanceiroCliente == 0) {
			vlIndiceFinanceiroCliente = 1;
		}
		if (indiceEspecial) {
			if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
				double vlItemDescIndiceCli = itemPedido.vlBaseItemPedido * vlIndiceFinanceiroCliente;
				itemPedido.vlItemPedido = ValueUtil.round(vlItemDescIndiceCli);
				itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
				itemPedido.vlPctDescPrev = (1 - vlIndiceFinanceiroCliente) * 100;
			}
		} else {
			if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido) {
				ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
				double vlPctMaxDesconto = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
					vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
				}
				double vlItemDescMaxTabela = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctMaxDesconto) / 100);
				double vlItemDescIndiceCli = itemPedido.vlBaseItemPedido * vlIndiceFinanceiroCliente;
				if (vlItemDescIndiceCli > vlItemDescMaxTabela) {
					itemPedido.vlItemPedido = ValueUtil.round(vlItemDescIndiceCli);
					itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
					itemPedido.vlPctDescPrev = (1 - vlIndiceFinanceiroCliente) * 100;
				} else {
					itemPedido.vlItemPedido = ValueUtil.round(vlItemDescMaxTabela);
					itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
					itemPedido.vlPctDescPrev = vlPctMaxDesconto;
				}
			}
		}
	}

	public void aplicaDescontoProgressivoItensBloqueados(final Vector itemPedidoListBloqueados, final double vlPctDescProg) throws SQLException {
		if (itemPedidoListBloqueados != null && vlPctDescProg >= 0) {
			ItemPedido itemPedido;
			for (int i = 0; i < itemPedidoListBloqueados.size(); i++) {
				itemPedido = (ItemPedido) itemPedidoListBloqueados.items[i];
				realizaAplicacaoDescontoProgressivoNoItemPedido(itemPedido, vlPctDescProg);
			}
		}
	}

	public boolean isAplicaDescontoProgressivoNoItemPedido(final ItemPedido itemPedido, final double vlPctDescProg) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoProgressivoPorItemFinalPedido || LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()) {
			if (itemPedido.vlPctAcrescimo > 0 || itemPedido.vlPctDesconto > 0) {
				return false;
			}
			realizaAplicacaoDescontoProgressivoNoItemPedido(itemPedido, vlPctDescProg);
			return true;
		}
		return false;
	}

	private void realizaAplicacaoDescontoProgressivoNoItemPedido(final ItemPedido itemPedido, final double vlPctDescProg) throws SQLException {
		if (vlPctDescProg >= 0 && !itemPedido.isCombo()) {
			if (LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
				itemPedido.vlItemPedido = getVlItemPedidoComDescontoProgressivo(itemPedido, vlPctDescProg);
				itemPedido.vlPctDescPrev = vlPctDescProg;
				itemPedido.vlPctDesconto = vlPctDescProg;
			} else {
				ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
				double vlPctMaxDesconto = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
					vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
				}
				if (vlPctDescProg > vlPctMaxDesconto) {
					itemPedido.vlItemPedido = getVlItemPedidoComDescontoProgressivo(itemPedido, vlPctDescProg);
					itemPedido.vlPctDescPrev = vlPctMaxDesconto;
					itemPedido.vlPctDesconto = vlPctMaxDesconto;
				} else {
					itemPedido.vlItemPedido = getVlItemPedidoComDescontoProgressivo(itemPedido, vlPctDescProg);
					itemPedido.vlPctDescPrev = vlPctDescProg;
					itemPedido.vlPctDesconto = vlPctDescProg;
				}
			}
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		}
	}

	public double getVlItemPedidoComDescontoProgressivo(ItemPedido itemPedido, double vlPctDescProg) throws SQLException {
		double vlItemPedidoComDescontoProgressivo = itemPedido.vlBaseItemPedido;
		if (vlPctDescProg >= 0) {
			if (LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
				vlItemPedidoComDescontoProgressivo = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctDescProg) / 100);
			} else {
				ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
				double vlPctMaxDesconto = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
					vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
				}
				if (vlPctDescProg > vlPctMaxDesconto) {
					vlItemPedidoComDescontoProgressivo = itemPedido.vlItemPedido - ((itemPedido.vlBaseItemPedido * vlPctMaxDesconto) / 100);
				} else {
					vlItemPedidoComDescontoProgressivo = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctDescProg) / 100);
				}
			}
		}
		if (LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivoComValorTruncado()) {
			return ValueUtil.getDoubleValueTruncated(vlItemPedidoComDescontoProgressivo, ValueUtil.doublePrecision);
		}
		return ValueUtil.round(vlItemPedidoComDescontoProgressivo);
	}
	
	public double getVlItemPedidoComDescontoProgressivoMix(ItemPedido itemPedido, double vlBase, double vlPctDescProgMix) throws SQLException {
		double vlItemPedidoComDescontoProgressivoMix = vlBase;
		if (vlPctDescProgMix >= 0) {
			if (LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
				vlItemPedidoComDescontoProgressivoMix = vlBase - ((vlBase * vlPctDescProgMix) / 100);
			} else {
				ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
				double vlPctMaxDesconto = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
				if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
					vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
				} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
					vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
				}
				if (vlPctDescProgMix > vlPctMaxDesconto) {
					vlItemPedidoComDescontoProgressivoMix = itemPedido.vlItemPedido - ((vlBase * vlPctMaxDesconto) / 100);
				} else {
					vlItemPedidoComDescontoProgressivoMix = vlBase - ((vlBase * vlPctDescProgMix) / 100);
				}
			}
		}
		if (LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivoComValorTruncado()) {
			return ValueUtil.getDoubleValueTruncated(vlItemPedidoComDescontoProgressivoMix, ValueUtil.doublePrecision);
		}
		return ValueUtil.round(vlItemPedidoComDescontoProgressivoMix);
	}

	public void aplicaDescontoProgressivoNoItemPedidoConsumindoFlex(final ItemPedido itemPedido, final double vlPctDescProg) throws SQLException {
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && !itemPedido.pedido.isPedidoCriticoOuConversaoFob()) {
			itemPedido.vlVerbaItem = 0;
			itemPedido.vlVerbaItemPositivo = 0;
			if (itemPedido.isItemBonificacao()) {
				itemPedido.vlVerbaItem = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico()) * -1;
			} else if (vlPctDescProg >= 0) {
				if (itemPedido.vlPctDesconto > vlPctDescProg) {
					double vlItemDescProg = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctDescProg) / 100);
					itemPedido.vlVerbaItem = ValueUtil.round((itemPedido.vlItemPedido - ValueUtil.round(vlItemDescProg)) * itemPedido.getQtItemFisico());
				} else if (LavenderePdaConfig.isMostraFlexPositivoPedido()) {
					double vlItemDescProg = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctDescProg) / 100);
					itemPedido.vlVerbaItemPositivo = ValueUtil.round((itemPedido.vlItemPedido - ValueUtil.round(vlItemDescProg)) * itemPedido.getQtItemFisico());
				}
			}
			itemPedido.vlPctDescPrev = vlPctDescProg;
			itemPedido.vlVerbaItem = ValueUtil.round(itemPedido.vlVerbaItem);
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		}
	}

	public void aplicaDescontoCCPNoItemPedido(final ItemPedido itemPedido, double vlDescCCP) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido) {
			ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
			double vlPctMaxDescontoItemTabelaPreco = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
				vlPctMaxDescontoItemTabelaPreco = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
			} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
				vlPctMaxDescontoItemTabelaPreco = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
			}
			if (vlDescCCP > vlPctMaxDescontoItemTabelaPreco) {
				itemPedido.vlItemPedido = itemPedido.vlItemPedido - ((itemPedido.vlBaseItemPedido * vlPctMaxDescontoItemTabelaPreco) / 100);
				itemPedido.vlPctDescPrev = vlPctMaxDescontoItemTabelaPreco;
			} else {
				itemPedido.vlItemPedido = itemPedido.vlItemPedido - ((itemPedido.vlBaseItemPedido * vlDescCCP) / 100);
				itemPedido.vlPctDescPrev = vlDescCCP;
			}
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido);
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		}
	}

	public void validaDescontoCCPPorItem(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.vlPctDesconto > 0) {
			Cliente cliente = SessionLavenderePda.getCliente();
			double descontoCCPMax = 0;
			if ((ValueUtil.isNotEmpty(cliente.cdCategoria)) && (ValueUtil.isNotEmpty(itemPedido.getProduto().cdClasse))) {
				descontoCCPMax = DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, cliente);
				if (descontoCCPMax != 0) {
					if (itemPedido.vlPctDesconto > descontoCCPMax) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, descontoCCPMax));
					}
				} else {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, descontoCCPMax));
				}
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, descontoCCPMax));
			}
		}
	}

	public void aplicaMaiorDescontoAutomaticoItemPedido(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlBaseItemPedidoIndiceCliente = 0;
		double vlBaseItemPedidoDescItemTabPreco = 0;
		double vlBaseItemPedidoIndiceCondPagto = 0;
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"2".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) {
			vlBaseItemPedidoDescItemTabPreco = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * getDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido));
		}
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"3".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) {
			vlBaseItemPedidoIndiceCliente = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * getIndiceFinanceiroCliente(pedido));
		}
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"4".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) {
			vlBaseItemPedidoIndiceCondPagto = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido));
		}
		//--
		boolean isVlBaseItemPedidoIndiceClienteEmpty = vlBaseItemPedidoIndiceCliente <= 0;
		boolean isVlBaseItemPedidoDescItemTabPrecoEmpty = vlBaseItemPedidoDescItemTabPreco <= 0;
		boolean isVlBaseItemPedidoIndiceCondPagtoEmpty = vlBaseItemPedidoIndiceCondPagto <= 0;
		//--
		double vlBaseItemPedido = 0;
		double vlBaseEmbalagemElementar = 0;
		if (!isVlBaseItemPedidoIndiceClienteEmpty && ((vlBaseItemPedidoIndiceCliente <= vlBaseItemPedidoDescItemTabPreco) || isVlBaseItemPedidoDescItemTabPrecoEmpty) && ((vlBaseItemPedidoIndiceCliente <= vlBaseItemPedidoIndiceCondPagto) || isVlBaseItemPedidoIndiceCondPagtoEmpty)) {
			vlBaseItemPedido = vlBaseItemPedidoIndiceCliente;
			vlBaseEmbalagemElementar = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * getIndiceFinanceiroCliente(pedido));
		} else if (!isVlBaseItemPedidoDescItemTabPrecoEmpty && ((vlBaseItemPedidoDescItemTabPreco <= vlBaseItemPedidoIndiceCliente) || isVlBaseItemPedidoIndiceClienteEmpty) && ((vlBaseItemPedidoDescItemTabPreco <= vlBaseItemPedidoIndiceCondPagto) || isVlBaseItemPedidoIndiceCondPagtoEmpty)) {
			vlBaseItemPedido = vlBaseItemPedidoDescItemTabPreco;
			vlBaseEmbalagemElementar = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * getDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido));
		} else if (!isVlBaseItemPedidoIndiceCondPagtoEmpty && ((vlBaseItemPedidoIndiceCondPagto <= vlBaseItemPedidoDescItemTabPreco) || isVlBaseItemPedidoDescItemTabPrecoEmpty) && ((vlBaseItemPedidoIndiceCondPagto <= vlBaseItemPedidoIndiceCliente) || isVlBaseItemPedidoIndiceClienteEmpty)) {
			vlBaseItemPedido = vlBaseItemPedidoIndiceCondPagto;
			vlBaseEmbalagemElementar = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido));
		}
		//--
		if (vlBaseItemPedido != 0) {
			itemPedido.vlBaseItemPedido = vlBaseItemPedido;
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		}
		if (vlBaseEmbalagemElementar != 0) {
			itemPedido.vlUnidadePadrao = vlBaseEmbalagemElementar;
		}
	}

	public void aplicaMaiorDescontoNoItemPedido(final ItemPedido itemPedido, final double vlPctDescProgressivo, final Cliente cliente, double vlDescCCP) throws SQLException {
		boolean indiceEspecialCliente = itemPedido.isIndiceFinanceiroEspecialCliente();
		double vlIndiceFinanceiro = cliente.getIndiceFinanceiro(indiceEspecialCliente);
		//--
		double vlPctDescCCP = LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido ? vlDescCCP : 0;
		double vlPctIndiceCliente = (1 - (vlIndiceFinanceiro != 0 ? vlIndiceFinanceiro : 1)) * 100;
		double vlPctMaxDescItemTabPreco = itemPedido.getItemTabelaPreco() != null ? itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto()) : 0;
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
			vlPctMaxDescItemTabPreco = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
		} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
			vlPctMaxDescItemTabPreco = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
		}
		//--
		if (((vlPctIndiceCliente >= vlPctDescProgressivo) && (vlPctIndiceCliente >= vlPctDescCCP)) || (indiceEspecialCliente && (vlPctIndiceCliente > vlPctMaxDescItemTabPreco))) {
			aplicaIndiceFinanceiroClienteNoItemPedido(itemPedido, vlIndiceFinanceiro, indiceEspecialCliente);
		} else if ((vlPctDescProgressivo >= vlPctDescCCP) && (vlPctDescProgressivo >= vlPctIndiceCliente)) {
			isAplicaDescontoProgressivoNoItemPedido(itemPedido, vlPctDescProgressivo);
		} else if ((vlPctDescCCP >= vlPctIndiceCliente) && (vlPctDescCCP >= vlPctDescProgressivo)) {
			aplicaDescontoCCPNoItemPedido(itemPedido, vlDescCCP);
		}
	}

	public double getMaiorPctDescontoParaItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		double vlDescCCP = LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido ? DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, pedido.getCliente()) : 0;
		//--
		boolean indiceClienteEspecial = itemPedido.isIndiceFinanceiroEspecialCliente();
		double vlPctMaxDescTabela = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
			vlPctMaxDescTabela = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
		} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
			vlPctMaxDescTabela = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
		}
		//--
		double vlPctDescProgressivo = 0.0;
		if (LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()) {
			double sumItensFaturamento = pedido.getQtItensValidosAplicarDescProgressivo();
			if (itemPedido.vlPctAcrescimo > 0) {
				vlPctDescProgressivo = 0;
			} else {
				int index = pedido.itemPedidoList.indexOf(itemPedido);
				if (index != -1) {
					ItemPedido itemPedidoOld = (ItemPedido) pedido.itemPedidoList.items[index];
					if (itemPedidoOld.vlPctAcrescimo <= 0) {
						sumItensFaturamento -= itemPedido.oldQtItemFaturamento;
					}
				}
				sumItensFaturamento += itemPedido.qtItemFaturamento;
				DescProgQtd descProgQtd = DescProgQtdService.getInstance().getDescProgQtdPedido(sumItensFaturamento);
				if (descProgQtd != null) {
					vlPctDescProgressivo = descProgQtd.vlPctDesconto;
				}
			}
		} else if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) {
			DescProgressivo descProgressivo = DescProgressivoService.getInstance().getDescProgressivoItemPedido(pedido, itemPedido);
			if (descProgressivo != null) {
				vlPctDescProgressivo = descProgressivo.vlPctDesconto;
			}
		}
		return getMaiorPctDesconto(vlPctDescProgressivo, vlDescCCP, pedido.getCliente(), vlPctMaxDescTabela, indiceClienteEspecial);
	}

	public double getMaiorPctDesconto(double vlPctDescProgressivo, double vlDescCCP, final Cliente cliente, final double vlPctMaxDescTabela, final boolean indiceClienteEspecial) {
		double vlIndiceFinanceiro = cliente.getIndiceFinanceiro(indiceClienteEspecial);
		vlIndiceFinanceiro = (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido || LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) ? vlIndiceFinanceiro : 1;
		//--
		double vlPctDescCCP = vlDescCCP;
		double vlPctIndiceCliente = (1 - (vlIndiceFinanceiro != 0 ? vlIndiceFinanceiro : 1)) * 100;
		//--
		if (((vlPctIndiceCliente >= vlPctDescProgressivo) && (vlPctIndiceCliente >= vlPctDescCCP)) || (indiceClienteEspecial && (vlPctIndiceCliente > vlPctMaxDescTabela))) {
			if (indiceClienteEspecial) {
				return vlPctIndiceCliente;
			} else {
				return vlPctIndiceCliente > vlPctMaxDescTabela ? vlPctMaxDescTabela : vlPctIndiceCliente;
			}
		} else if ((vlPctDescProgressivo >= vlPctDescCCP) && (vlPctDescProgressivo >= vlPctIndiceCliente)) {
			if (LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
				return vlPctDescProgressivo;
			} else {
				return vlPctDescProgressivo > vlPctMaxDescTabela ? vlPctMaxDescTabela : vlPctDescProgressivo;
			}

		} else if ((vlPctDescCCP >= vlPctIndiceCliente) && (vlPctDescCCP >= vlPctDescProgressivo)) {
			return vlPctDescCCP > vlPctMaxDescTabela ? vlPctMaxDescTabela : vlPctDescCCP;
		}
		return 0;
	}

	public void getAndApplyDescontoPromocionalItemTabelaPreco(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco && itemPedido.permiteAplicarDesconto() && ValueUtil.isEmpty(itemPedido.getItemTabelaPreco().descontoQuantidadeList) && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
			if (!itemPedido.isEditandoDescontoPct()) {
				double vlPctDesc = itemPedido.getItemTabelaPreco().vlPctDescPromocional;
				if (vlPctDesc > 0) {
					itemPedido.vlPctDesconto = vlPctDesc;
					itemPedido.vlItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
				}
			}
		}
	}
	
	
	public void aplicaDescontoMaximoPorCliente(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido.getCliente() == null) {
			return;
		}
		if (LavenderePdaConfig.isApresentaDescontoMaximoItemPorCliente()) {
			if (!itemPedido.isEditandoDescontoPct()) {
				double vlPctMaxDesconto  = itemPedido.pedido.getCliente().vlPctMaxDesconto;
				if (vlPctMaxDesconto != 0) {
					itemPedido.vlPctDesconto = vlPctMaxDesconto;
					itemPedido.vlItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
				}
			}
		} else if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
			if (!itemPedido.isEditandoDescontoPct()) {
				if (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
					double vlPctDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, itemPedido.vlPctDescontoCanal, itemPedido.pedido.getCliente().vlPctContratoCli);
					itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (vlPctDesconto / 100)));
					itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
					itemPedido.vlPctDesconto = 0;
				} else {
					itemPedido.vlPctDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, itemPedido.vlPctDescontoCanal, itemPedido.pedido.getCliente().vlPctContratoCli);
				}
			}
		}
	}

	public void calculateAndApplyVlAgregadoSugerido(final ItemPedido itemPedido) {
		itemPedido.vlAgregadoSugerido = calculaVlAgregadoSugerido(itemPedido.vlItemPedido, itemPedido.pctMargemAgregada);
	}
	
	public void removeDescontoCCPAoEditarItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			itemPedido.vlItemPedido += itemPedido.vlDescontoCCP;
			itemPedido.vlTotalItemPedido += itemPedido.vlDescontoCCP * itemPedido.getQtItemFisico();
			itemPedido.pedido.itemPedidoList.removeElement(itemPedido);
			itemPedido.pedido.itemPedidoList.addElement(itemPedido);
			PedidoService.getInstance().calculate(itemPedido.pedido);
		}
	}
	
	public double getDescontoPromocionalAutomaticoItemTabelaPreco(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco) {
			if (itemPedido.getItemTabelaPreco() == null) return 0d; 
			
			double vlPctDesc = itemPedido.getItemTabelaPreco().vlPctDescPromocional;
			if (vlPctDesc > 0) {
				return 1 - (vlPctDesc / 100);
			}
		}
		return 0d;
	}

	public void aplicaDescontoPromocionalAutomaticoItemTabelaPreco(final ItemPedido itemPedido) throws SQLException {
		if ((LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !"2".equals(LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido)) || itemPedido.possuiDescMaxProdCli()) {
			return;
		}
		double vlDesc = getDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && itemPedido.permiteAplicarDesconto()) {
			aplicaMaiorDescontoCascataItem(itemPedido);
			double vlDescMax = 1 - (itemPedido.vlPctDesconto / 100);
			vlDesc = vlDescMax > vlDesc ? vlDescMax : vlDesc;
		}
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedido.vlPctDescontoPromo = 0;
		}
		if (vlDesc > 0) {
			double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlDesc);
			if (!itemPedido.usaDescontoCascata) {
				if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
					if (LavenderePdaConfig.isArredondaDescontosSequenciaisNoItemPedidoApenasNoFinal()) {
						vlBaseItemPedido = ValueUtil.round(vlBaseItemPedido);
					}
					itemPedido.vlPctDescontoPromo = itemPedido.getItemTabelaPreco().vlPctDescPromocional;
				}
				itemPedido.vlBaseItemPedido = vlBaseItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			}
			itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlDesc);
		}
	}

	public void aplicaDescontoNoProdutoPorGrupoDescPromocional(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && itemPedido.permiteAplicarDesconto() && !itemPedido.bloqueiaAplicaDescPromocional && !itemPedido.possuiDescMaxProdCli()) {
			resetDescontosItemPedido(itemPedido);
			double vlFinalProdutoGrupoDescPromo = 0;
			if (!itemPedido.isIgnoraDescQtdPro()) {
				 vlFinalProdutoGrupoDescPromo = DescPromocionalService.getInstance().findVlFinalProdutoDescPromocional(itemPedido, itemPedido.vlBaseCalculoDescPromocional);
			}
			if (vlFinalProdutoGrupoDescPromo != 0) {
				vlFinalProdutoGrupoDescPromo = vlFinalProdutoGrupoDescPromo != -1 ? vlFinalProdutoGrupoDescPromo : 0;
				vlFinalProdutoGrupoDescPromo = roundUnidadeAlternativa(vlFinalProdutoGrupoDescPromo);
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
					try {
						BigDecimal qtd = BigDecimal.valueOf(itemPedido.getQtItemFisico() <= 0 ? 1 : itemPedido.getQtItemFisico()).setScale(ValueUtil.doublePrecision, BigDecimal.ROUND_HALF_UP);
						BigDecimal vlBaseInterpolacaoProdutoRound = BigDecimal.valueOf(itemPedido.vlBaseInterpolacaoProduto).setScale(LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit, BigDecimal.ROUND_HALF_UP);
						BigDecimal vlTotalBaseInterpolacao = vlBaseInterpolacaoProdutoRound.multiply(qtd).setScale(LavenderePdaConfig.nuArredondamentoRegraInterpolacaoTotal, BigDecimal.ROUND_HALF_UP);
						BigDecimal vlTotalBaseInterpolacaoParaDesconto = vlBaseInterpolacaoProdutoRound.multiply(qtd).setScale(LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit, BigDecimal.ROUND_HALF_UP);
						BigDecimal itemPedidoVlPctDescontoAuto = BigDecimal.valueOf(itemPedido.vlPctDescontoAuto).setScale(LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(0.01));
						BigDecimal descAutoD = (vlTotalBaseInterpolacao.multiply(itemPedidoVlPctDescontoAuto)).setScale(LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_FLOOR);
						BigDecimal vlBaseInterComDesc = vlTotalBaseInterpolacaoParaDesconto.subtract(descAutoD);
						BigDecimal vlFinalProdutoGrupoDescPromoBigD = vlBaseInterComDesc.divide(qtd, BigDecimal.ROUND_HALF_UP).setScale(LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit, BigDecimal.ROUND_HALF_UP);
						vlFinalProdutoGrupoDescPromo = vlFinalProdutoGrupoDescPromoBigD.doubleValue();
					} catch (InvalidNumberException e) {
						ExceptionUtil.handle(e);
					}
				}
				itemPedido.vlBaseItemPedido = vlFinalProdutoGrupoDescPromo;
				itemPedido.vlItemPedido = vlFinalProdutoGrupoDescPromo;
				itemPedido.vlUnidadePadrao = getVlUnidadePadrao(vlFinalProdutoGrupoDescPromo, itemPedido);
				if (LavenderePdaConfig.aplicaIndiceCondPgtoVlProdutoFinal() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento()) {
					applyIndiceFinanceiroValorFinalProduto(getIndiceFinanceiroValorFinalProduto(itemPedido), itemPedido);
				}
				aplicaIndiceFinanceiroCondPagamentoPorCondComercial(itemPedido, itemPedido.pedido);
				DescPromocional descPromocional = itemPedido.getDescPromocional();
				itemPedido.nuPromocao = descPromocional.nuPromocao;
				if (descPromocional.vlProdutoFinal > 0) {
					itemPedido.vlFinalPromo = descPromocional.vlProdutoFinal;
				} else if (descPromocional.vlDescontoProduto > 0) {
					itemPedido.vlDescontoPromo = descPromocional.vlDescontoProduto;
				} else if (descPromocional.vlPctDescontoProduto > 0) {
					itemPedido.vlPctDescontoPromo = descPromocional.vlPctDescontoProduto;
				}
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
					itemPedido.vlDescontoAuto = ValueUtil.getDoubleValueTruncated(itemPedido.vlBaseInterpolacaoProduto * itemPedido.descPromocional.vlPctDescontoProduto / 100, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
				}
			}
		}
	}

	private double getIndiceFinanceiroValorFinalProduto(ItemPedido itemPedido) throws SQLException {
		double vlIndiceFinanceiro = 1;
		Pedido pedido = itemPedido.pedido;
		if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido()) {
			vlIndiceFinanceiro = getIndiceFinanceiroCondPagtoVlItemPedido(pedido, itemPedido);
		} else if (LavenderePdaConfig.indiceFinanceiroCondPagtoPorDias) {
			CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
			vlIndiceFinanceiro = condicaoPagamento.vlIndiceFinanceiro;
			if (vlIndiceFinanceiro != 0) {
				int dias = condicaoPagamento.qtDiasMediosPagamento;
				if (vlIndiceFinanceiro > 1) {
					vlIndiceFinanceiro = (((vlIndiceFinanceiro - 1.0) / 30) * dias) + 1;
				} else if (vlIndiceFinanceiro < 1) {
					vlIndiceFinanceiro = 1 - (((1 - vlIndiceFinanceiro) / 30) * dias);
				}
			}
		}
		return vlIndiceFinanceiro;
	}

	private void applyIndiceFinanceiroValorFinalProduto(double vlIndiceFinanceiro, ItemPedido itemPedido) {
		if (vlIndiceFinanceiro == 0) {
			return;
		}
		itemPedido.vlItemPedido *= vlIndiceFinanceiro;
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			itemPedido.vlBaseItemPedido *= vlIndiceFinanceiro;
		}
	}
	
	private void resetDescontosItemPedido(ItemPedido itemPedido) {
		if (itemPedido == null) {
			return;
		}
		itemPedido.vlFinalPromo = 0d;
		itemPedido.vlDescontoPromo = 0d;
		itemPedido.vlPctDescontoPromo = 0d;
		itemPedido.nuPromocao = 0;
	}

	public double getVlUnidadePadrao(double vlFinalProdutoGrupoDescPromo, ItemPedido itemPedido) throws SQLException {
		double vlUnidadePadraoFinal = vlFinalProdutoGrupoDescPromo;
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				if (!LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
					if (produtoUnidade.isMultiplica()) {
						vlUnidadePadraoFinal = vlFinalProdutoGrupoDescPromo / produtoUnidade.nuConversaoUnidade;
					} else {
						vlUnidadePadraoFinal = vlFinalProdutoGrupoDescPromo * produtoUnidade.nuConversaoUnidade;
					}
				} else {
					vlUnidadePadraoFinal = vlFinalProdutoGrupoDescPromo;
				}
			}
		}
		return vlUnidadePadraoFinal;
	}

	public void retiraDescontosDoItemPedido(Pedido pedido, ItemPedido itemPedido) {
		itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		//--
		if (itemPedido.vlPctAcrescimo > 0) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 + (itemPedido.vlPctAcrescimo / 100)));
		}
		if ((itemPedido.vlPctDesconto > 0) && (itemPedido.vlPctDesconto != pedido.vlPctDescProgressivo)) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido - ValueUtil.round(itemPedido.vlBaseItemPedido * (itemPedido.vlPctDesconto / 100)));
		} else {
			itemPedido.vlPctDesconto = 0;
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido && itemPedido.vlPctDescProgressivoMix > 0) { 
			itemPedido.vlPctDescProgressivoMix = 0;
		}
		//--
		itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
	}

	public void retiraDescontosDoFimDoItemPedidoConsumindoFlex(ItemPedido itemPedido, double vlPctDescProg) throws SQLException {
		if (!itemPedido.pedido.isIgnoraControleVerba()) {
			itemPedido.vlPctDescPrev = 0;
			double vlVerba = 0;
			if (itemPedido.isItemBonificacao()) {
				vlVerba += ValueUtil.round(itemPedido.vlItemPedido) * itemPedido.getQtItemFisico();
			} else {
				double vlPctDescDadoNoFimPedido = vlPctDescProg;
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
				if (itemTabelaPreco != null) {
					double vlPctMaxDesconto = itemTabelaPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
					if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
						vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
					} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
						vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
					}
					if (vlPctMaxDesconto < vlPctDescProg) {
						vlPctDescDadoNoFimPedido = vlPctMaxDesconto;
					}
				}
				vlVerba += ValueUtil.round(ValueUtil.round((itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctDescDadoNoFimPedido) / 100)) - itemPedido.vlItemPedido) * itemPedido.getQtItemFisico());
			}
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * itemPedido.vlPctDesconto) / 100));
			vlVerba = ValueUtil.round(vlVerba * -1);
			itemPedido.vlVerbaItem = 0;
			if (vlVerba <= 0) {
				itemPedido.vlVerbaItem = ValueUtil.round(vlVerba);
			}
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
		}
	}

	public void setVlBaseItemPedidoFromItemTabelaPreco(final ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPrecoOld = itemPedido.getItemTabelaPrecoOld();
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null) {
			TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
			double newVlBaseItemPedido = 0;
			if (!LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || itemTabelaPreco.vlManualBrutoItem < 0) {
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && itemPedido.pedido != null) {
					int difPrazo = itemTabelaPreco.cdPrazoFim - itemTabelaPreco.cdPrazoIni;
					int difPrazoInfo = itemPedido.pedido.getCondicaoPagamento().qtDiasMediosPagamento - itemTabelaPreco.cdPrazoIni;
					double difVlUnit = itemTabelaPreco.vlUnitPrazoFim - itemTabelaPreco.vlUnitPrazoIni;
					if (difPrazo <= 0) {
						newVlBaseItemPedido = 0d;
					} else {
						newVlBaseItemPedido = ValueUtil.round(itemTabelaPreco.vlUnitPrazoIni + ((difPrazoInfo * difVlUnit) / difPrazo), LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
					}
				} else {
					if (!ValueUtil.isEmpty(itemPedido.cdProduto) && !ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
						if (LavenderePdaConfig.usaOportunidadeVenda && (itemPedido.isOportunidade() || itemPedido.pedido.isOportunidade()) && !itemPedido.pedido.isPedidoBonificacao()) {
							newVlBaseItemPedido = itemTabelaPreco.vlOportunidade;
						} else {
							newVlBaseItemPedido = itemTabelaPreco.vlUnitario;
						}
						if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial) {
							if (itemPedido.pedido.getCliente().isEspecial()) {
								newVlBaseItemPedido = itemTabelaPreco.vlUnitarioEspecial;
							}
						}
					}
					if (itemPedido.pedido != null) {
						String cdCondicaoComercial = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? itemPedido.cdCondicaoComercial : itemPedido.pedido.cdCondicaoComercial;
						String cdItemGrade1 = LavenderePdaConfig.usaGradeProduto1() ? itemPedido.cdItemGrade1 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						double vlUnitarioCondComercialExcec = 0d;
						if (itemTabelaPreco.condComercialExcec == null || 
						   (itemTabelaPreco.condComercialExcec.cdEmpresa == null && itemTabelaPreco.condComercialExcec.cdRepresentante == null && itemTabelaPreco.condComercialExcec.cdProduto == null &&
						    itemTabelaPreco.condComercialExcec.cdCondicaoComercial == null && itemTabelaPreco.condComercialExcec.cdItemGrade1 == null)) {
							itemTabelaPreco.getCondComercialExcec(cdCondicaoComercial, cdItemGrade1);
						}
						vlUnitarioCondComercialExcec = itemTabelaPreco.condComercialExcec != null ? itemTabelaPreco.condComercialExcec.vlUnitario : 0d;
						if (vlUnitarioCondComercialExcec != 0) {
							newVlBaseItemPedido = vlUnitarioCondComercialExcec;
						}
						if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
							newVlBaseItemPedido = ItemTabelaPrecoService.getInstance().getVlUnitarioTaxaAntecipacao(itemPedido.pedido, itemTabelaPreco, false);
							newVlBaseItemPedido = newVlBaseItemPedido < 0 ? 0 : newVlBaseItemPedido;
							itemPedido.dtPagamento = itemPedido.pedido.dtPagamento;
							itemPedido.vlBaseAntecipacao = ItemTabelaPrecoService.getInstance().getVlUnitarioTaxaAntecipacao(itemPedido.pedido, itemTabelaPreco, true);
						}
					}
					if (LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
						itemPedido.vlUnidadePadrao = newVlBaseItemPedido;
						itemPedido.vlDecisaoCanalFormulaA = itemPedido.vlDecisaoCanalFormulaA == 0 ? CanalCliGrupoService.getInstance().calculaFormulaA(itemPedido) : itemPedido.vlDecisaoCanalFormulaA;
						newVlBaseItemPedido = CanalCliGrupoService.getInstance().calculaFormulaB(itemPedido);
					}
				}
			} else {
				newVlBaseItemPedido = itemTabelaPreco.vlManualBrutoItem;
			}			
			itemPedido.vlBaseItemTabelaPreco = newVlBaseItemPedido;
			itemPedido.vlBaseCalculoDescPromocional = newVlBaseItemPedido;
			itemPedido.vlBaseItemPedido = newVlBaseItemPedido;
			itemPedido.vlItemPedido = newVlBaseItemPedido;
			itemPedido.vlUnidadePadrao = newVlBaseItemPedido;
			if (!LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
				itemPedido.vlItemPedidoFrete = (tipoPedido == null || tipoPedido.isIgnoraCalculoFrete()) ? itemTabelaPreco.vlPrecoFrete : 0d;
			}
			itemPedido.cdLinha = itemTabelaPreco.cdLinha;
			if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
				itemPedido.vlManualBrutoItem = newVlBaseItemPedido;
			}
			if (LavenderePdaConfig.usaCalculoReversoNaST && itemPedido.vlItemPedidoStReverso == 0) {
				itemPedido.vlItemPedidoStReverso = newVlBaseItemPedido;
			}
			if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
				itemPedido.vlBaseInterpolacaoProduto = newVlBaseItemPedido;
			}
			if ("2".equals(LavenderePdaConfig.usaPrecoPorUnidadeQuantidadePrazo) && ValueUtil.valueNotEqualsIfNotNull(itemTabelaPreco, itemTabelaPrecoOld)) {
				itemPedido.vlPctAcrescimo = 0;
				itemPedido.vlPctDesconto = 0;
			}
			if (LavenderePdaConfig.usaControlePontuacao) {
				itemPedido.vlPontuacaoRealizadoItem = itemPedido.vlPontuacaoBaseItem =
						itemPedido.vlPesoPontuacao = itemPedido.vlFatorCorrecaoFaixaDias =
								itemPedido.vlFatorCorrecaoFaixaPreco = itemPedido.vlPctFaixaPrecoPontuacao = 0d;
				itemPedido.vlBasePontuacao = itemTabelaPreco.vlBasePontuacao;
				itemPedido.cdPontuacaoConfig = null;
				itemPedido.qtDiasFaixaPontuacao = 0;
			}
		}
	}

	public Vector findHistoricoProdutosByCliente(final String cdCliente, final String cdProduto) throws SQLException {
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			return findHistoricoProdutosByClienteComSolAutorizacao(cdCliente, cdProduto);
		}
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdProduto = cdProduto;
		itemPedidoFilter.cdCliente = cdCliente;
		return ItemPedidoPdbxDao.getInstance().findHistoricoProdutosByCliente(itemPedidoFilter);
	}

	private Vector findHistoricoProdutosByClienteComSolAutorizacao(final String cdCliente, final String cdProduto) throws SQLException {
		Vector historicoItemList = new Vector();
		//--
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCliente = cdCliente;
		Vector pedidoClienteList = PedidoService.getInstance().findAllByExampleSummary(pedidoFilter);
		Pedido pedido;
		for (int i = 0; i < pedidoClienteList.size(); i++) {
			pedido = (Pedido)pedidoClienteList.items[i];
			StatusPedidoPda statusPedidoPda = pedido.statusPedidoPda;
			if (statusPedidoPda != null && ValueUtil.getBooleanValue(statusPedidoPda.flIgnoraHistoricoItem)) {
				continue;
			}
			ItemPedido itemPedidoFilter = new ItemPedido();
			itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
			itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
			itemPedidoFilter.nuPedido = pedido.nuPedido;
			itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			itemPedidoFilter.cdProduto = cdProduto;
			Vector itemPedidoList = findAllByExampleUnique(itemPedidoFilter);
			if (itemPedidoList.size() > 0) {
				itemPedidoFilter = (ItemPedido)itemPedidoList.items[0];
				HistoricoItem histItem = new HistoricoItem();
				histItem.dtEmissao = pedido.dtEmissao;
				histItem.vlCotacaoDolar = pedido.vlCotacaoDolar;
				histItem.qtItemFisico = itemPedidoFilter.getQtItemFisico();
				histItem.vlPctDesconto = itemPedidoFilter.vlPctDesconto;
				histItem.vlItemPedido = itemPedidoFilter.vlItemPedido;
				histItem.vlItemPedidoUnElementar = itemPedidoFilter.vlItemPedidoUnElementar;
				histItem.qtItemPedidoUnElementar = itemPedidoFilter.qtItemPedidoUnElementar;
				histItem.vlStItem = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedidoFilter.vlTotalStItem : itemPedidoFilter.vlSt;
				historicoItemList.addElement(histItem);
			}
		}
		return historicoItemList;
	}

	public int findCountPedidosByCliente(final String nuPedido, final String cdCliente) throws SQLException {
		int countPedido = ItemPedidoPdbxDao.getInstance().findCountPedidosByCliente(nuPedido, cdCliente);
		int countPedidoErp = ItemPedidoPdbxDao.getInstanceErp().findCountPedidosByCliente(nuPedido, cdCliente);
		return countPedido + countPedidoErp;
	}

	private Vector unionVectorsHistoricoItem(final String nuPedido, final String cdCliente) throws SQLException {
		Vector historicoItemList = new Vector();
		Vector historicoItemErpList = new Vector();
		//--
		historicoItemList = ItemPedidoPdbxDao.getInstance().findItemPedidoByCliente(nuPedido, cdCliente);
		//--
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdCliente = cdCliente;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
		Vector listPedidoErp = PedidoPdbxDao.getInstance().findAllByExample(pedidoFilter);
		int size = listPedidoErp.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				Pedido pedidoErp = (Pedido) listPedidoErp.items[i];
				ItemPedido itemPedidoErpFilter = new ItemPedido();
				itemPedidoErpFilter.cdEmpresa = pedidoErp.cdEmpresa;
				itemPedidoErpFilter.cdRepresentante = pedidoErp.cdRepresentante;
				itemPedidoErpFilter.nuPedido = pedidoErp.nuPedido;
				itemPedidoErpFilter.flOrigemPedido = pedidoFilter.flOrigemPedido;
				itemPedidoErpFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
				Vector itemPedidoErpList = ItemPedidoPdbxDao.getInstanceErp().findAllByExampleUnique(itemPedidoErpFilter);
				int sizeErpList = itemPedidoErpList.size();
				if (sizeErpList > 0) {
					for (int j = 0; j < sizeErpList; j++) {
						ItemPedido itemPedidoErp = (ItemPedido) itemPedidoErpList.items[j];
						int historicoItemListSize = historicoItemErpList.size();
						boolean encontrado = false;
						for (int k = 0; k < historicoItemListSize; k++) {
							ItemPedido historicoItemPedidoErp = (ItemPedido) historicoItemErpList.items[k];
							if (historicoItemPedidoErp.cdProduto.equalsIgnoreCase(itemPedidoErp.cdProduto)) {
								encontrado = true;
								historicoItemPedidoErp.qtPedidosContido++;
								break;
							}
						}
						if (!encontrado) {
							itemPedidoErp.qtPedidosContido++;
							historicoItemErpList.addElement(itemPedidoErp);
						}
					}
				}
			}
		}
		Vector historicoList;
		if (historicoItemList.size() > 0) {
			historicoList = new Vector(historicoItemList.toObjectArray());
		} else {
			historicoList = new Vector();
		}
		//--
		int sizeList = historicoItemList.size();
		int sizeErpList = historicoItemErpList.size();
		for (int i = 0; i < sizeErpList; i++) {
			boolean encontrou = false;
			ItemPedido itemPedidoErp = (ItemPedido) historicoItemErpList.items[i];
			for (int j = 0; j < sizeList; j++) {
				ItemPedido itemPedido = (ItemPedido) historicoItemList.items[j];
				if (itemPedido.cdProduto.equalsIgnoreCase(itemPedidoErp.cdProduto)) {
					itemPedido.qtPedidosContido += itemPedidoErp.qtPedidosContido;
					encontrou = true;
					break;
				}
			}
			if (!encontrou) {
				historicoList.addElement(itemPedidoErp);
			}
		}
		//--
		return historicoList;
	}

	public Vector findProdutosPendentesByCliente(final Pedido pedido, final String cdCliente, Vector itemPedidoList, boolean ignoraItens) throws SQLException {
		Vector historicoList = unionVectorsHistoricoItem(pedido.nuPedido, cdCliente);
		if (ValueUtil.isEmpty(historicoList) && !ignoraItens) {
			throw new ValidationException(Messages.ITEMPEDIDO_MSG_SEM_HISTORICO_ITEM);
		}
		Vector produtoPendenteList = new Vector();
		Produto produto = null;
		if ((historicoList != null) && (historicoList.size() > 0)) {
			for (int i = 0; i < historicoList.size(); i++) {
				ItemPedido itemHistorico = (ItemPedido) historicoList.items[i];
				boolean addProduto = true;
				produto = itemHistorico.getProduto();
				if (produto == null || ValueUtil.valueEquals(produto, new Produto())) {
					addProduto = false;
				}
				if (addProduto) {
					for (int j = 0; j < itemPedidoList.size(); j++) {
						ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
						if (itemPedido.cdProduto.equalsIgnoreCase(itemHistorico.cdProduto)) {
							addProduto = false;
							break;
						}
						itemHistorico.setItemTabelaPreco(itemHistorico.getItemTabelaPrecoAtual());
					}
				}
				if (addProduto) {
					itemHistorico.setProduto(produto);
					itemHistorico.setPedido(pedido);
					if (itemHistorico.estoque == null) {
						itemHistorico.estoque = new Estoque();
						itemHistorico.estoque.cdLocalEstoque = itemHistorico.getCdLocalEstoque();
						itemHistorico.estoque.qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemHistorico, itemHistorico.estoque.cdLocalEstoque);
					}
					produtoPendenteList.addElement(itemHistorico);
				}
			}
		}
		return produtoPendenteList;
	}

	public ItemPedido findItemPedidoUltimoPedidoCliente(final String cdCliente, final String cdProduto, final String nuPedidoDif) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().findItemPedidoUltimoPedidoCliente(cdCliente, cdProduto, nuPedidoDif);
	}

	public void clearDadosItemPedido(final ItemPedido itemPedido) throws SQLException {
		clearDadosItemPedido(itemPedido.pedido, itemPedido);
	}

	public void clearDadosItemPedido(Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		itemPedido.pedido = pedido;
		itemPedido.setQtItemFisico(0);
		itemPedido.qtItemFaturamento = 0;
		itemPedido.vlPctAcrescimo = 0;
		itemPedido.vlTotalItemPedido = 0;
		itemPedido.setOldQtItemFisico(0);
		itemPedido.oldQtItemFisicoDescQtd = 0;
		itemPedido.oldQtItemFisicoDescPromocionalQtd = 0;
		itemPedido.oldQtItemFaturamento = 0;
		itemPedido.oldQtPeso = 0;
		itemPedido.vlReducaoOptanteSimples = 0;
		itemPedido.qtItemPedidoMinimo = 0;
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedido.vlPctDescontoCondicao = 0;
			itemPedido.vlPctDescCliente = 0;
			itemPedido.vlPctDescontoPromo = 0;
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			itemPedido.vlPctAcrescimoIcms = 0;
			itemPedido.vlPctDescontoIcms = 0;
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			itemPedido.cdLoteProduto = "";
			itemPedido.usaPctMaxDescLoteProduto = false;
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			itemPedido.vlVerbaItem = 0;
			itemPedido.vlPctVerba = 0;
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				itemPedido.vlVerbaItemPositivo = 0;
			}
		}
		if (LavenderePdaConfig.informaVerbaManual) {
			itemPedido.vlVerbaManual = 0;
			itemPedido.vlVerbaItem = 0;
		}
		if (LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex()) {
			itemPedido.vlBaseFlex = 0;
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo()) {
			itemPedido.vlTotalComissao = 0;
			itemPedido.vlPctComissao = 0;
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			itemPedido.vlSt = 0;
			itemPedido.vlIcms = 0;
		}
		itemPedido.vlIpiItem = 0;
		itemPedido.vlBaseItemIpi = 0;
		//--
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			itemPedido.qtPeso = 0;
		}
		itemPedido.vlItemPedidoFrete = 0;
		itemPedido.vlTotalItemPedidoFrete = 0;
		if (LavenderePdaConfig.usaCestaPromocional) {
			itemPedido.usaCestaPromo = false;
			itemPedido.pctMaxDescCestaPromo = 0;
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto) {
			itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
			itemPedido.qtItemMinAfterLibPreco = 0;
			itemPedido.vlItemMinAfterLibPreco = 0;
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido) {
			itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
			itemPedido.cdUsuarioLiberacao = null;
			itemPedido.qtItemAfterLibPreco = 0;
			itemPedido.vlItemAfterLibPreco = 0;
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
			itemPedido.descComissaoProd = null;
			itemPedido.descComissaoGrupo = null;
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			itemPedido.qtPontosItem = 0;
		}
		itemPedido.cdUnidade = "";
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			itemPedido.nuConversaoUnidade = 0;
			itemPedido.qtEmbalagemElementar = 0;
			itemPedido.vlEmbalagemElementar = 0;
			itemPedido.vlBaseEmbalagemElementar = 0;
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			itemPedido.itemPedidoGradeList = new Vector(0);
			itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			itemPedido.flOportunidade = ValueUtil.VALOR_NAO;
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			itemPedido.vlPctDescontoCCP = 0;
			itemPedido.vlDescontoCCP = 0;
		}
		itemPedido.vlRentabilidade = 0;
		//--
		itemPedido.descQuantidade = null;
		itemPedido.descPromocional = null;
		itemPedido.cdMotivoTroca = "";
		itemPedido.dsObsMotivoTroca = "";
		itemPedido.dsObservacao = "";
		itemPedido.forcaReservaEstoque = false;
		itemPedido.nuPromocao = 0;
		itemPedido.vlFinalPromo = 0;
		itemPedido.vlPctDescontoPromo = 0;
		itemPedido.vlDescontoPromo = 0;
		itemPedido.cdSugestaoVenda = "";
		clearDescontoPromocional(itemPedido);
		clearDadosDesconto(itemPedido);
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			itemPedido.tributacaoConfig = null;
			itemPedido.vlTotalStItem = 0;
			itemPedido.vlTotalIpiItem = 0;
			itemPedido.vlTotalIcmsItem = 0;
			itemPedido.vlTotalPisItem = 0;
			itemPedido.vlTotalCofinsItem = 0;
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		}
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			itemPedido.vlPctDescontoStReverso = 0;
		}
		if (LavenderePdaConfig.usaDescMaxProdCli) {
			itemPedido.descMaxProdCli = null;
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			itemPedido.vlItemFreteTributacao = 0;
			itemPedido.vlTotalItemFreteTributacao = 0;
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			itemPedido.vlVerbaGrupoItem = 0d;
		}
		if (pedido.isGondola()) {
			itemPedido.qtItemGondola = -1;
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			itemPedido.cdPoliticaComercial = "";
			itemPedido.politicaComercial = null;
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			itemPedido.vlPctFaixaDescQtd = itemPedido.oldVlPctFaixaDescQtd = 0;
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			itemPedido.cdDescProgressivo = ValueUtil.VALOR_NI;
		}
		itemPedido.itemPedidoPorGradePrecoList = new Vector();
		itemPedido.isBonificacaoAutomatica = false;
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlPctMargemRentab = 0;
			itemPedido.cdMargemRentab = null;
		}
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			itemPedido.nuOrdemCompraCliente = ValueUtil.VALOR_NI;
			itemPedido.nuSeqOrdemCompraCliente = 0;
		}
	}

	public void clearDescontoPromocional(final ItemPedido itemPedido) {
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			itemPedido.descPromocional = null;
			itemPedido.descPromocionalComQtdList = null;
		}
	}

	public int getNextNuSeqItemPedido(final Pedido pedido) {
		int max = 0;
		if (!ValueUtil.isEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.nuSeqItemPedido > max) {
					max = itemPedido.nuSeqItemPedido;
				}
			}
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido || !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			if (!ValueUtil.isEmpty(pedido.itemPedidoTrocaList)) {
				int size = pedido.itemPedidoTrocaList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoTrocaList.items[i];
					if (itemPedido.nuSeqItemPedido > max) {
						max = itemPedido.nuSeqItemPedido;
					}
				}
			}
		}
		return max + 1;
	}

	public int getNextNuSeqItemPedidoDatabase(ItemPedido itemPedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().getNextNuSeqItemPedido(itemPedido);
	}

	private void clearDadosDesconto(final ItemPedido itemPedido) {
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			return;
		}
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlDesconto = 0;
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			itemPedido.vlPctDesconto2 = 0;
			itemPedido.vlDesconto2 = 0;
			itemPedido.vlPctDesconto3 = 0;
			itemPedido.vlDesconto3 = 0;
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			itemPedido.vlPctDescontoPromo = 0;
			itemPedido.vlDescontoCondicao = 0;
			itemPedido.vlPctDesconto3 = 0;
			itemPedido.vlDesconto3 = 0;
		}
		if (LavenderePdaConfig.usaDescontoExtra) {
			itemPedido.vlPctDesconto2 = 0;
		}
	}

	public ItemPedido itemKitToItemPedido(final Pedido pedido, final ItemKit itemKit) throws SQLException {
		ItemPedido itemPedido = getItemPedido(pedido, itemKit);
		return resetAndCalculateItemPedido(pedido, itemPedido);
	}
	
	public ItemPedido itemKitToItemPedido(final Pedido pedido, final ItemPedido itemPedido, final ItemKit itemKit) throws SQLException {
		setItemPedidoByItemKit(pedido, itemPedido, itemKit);
		return resetAndCalculateItemPedido(pedido, itemPedido);
	}

	private void setItemPedidoByItemKit(Pedido pedido, ItemPedido itemPedido, ItemKit itemKit) throws SQLException {
		if (LavenderePdaConfig.isUsaKitProdutoFechado() && LavenderePdaConfig.usaConversaoUnidadesMedida) {
			itemPedido.qtItemFaturamento = itemKit.qtMinItem;
			itemPedido.flEditandoQtItemFaturamento = true;
			aplicarConversaoUnidadeMedida(itemPedido, pedido);
		} else {
			itemPedido.setQtItemFisico(itemKit.qtMinItem);
		}
		itemPedido.cdProduto = itemKit.cdProduto;
		itemPedido.cdKit = itemKit.cdKit;
		if (itemKit.kit != null && itemKit.kit.isEditaDesconto()) {
			itemPedido.vlPctDesconto = itemKit.vlPctDesconto;
		} else {
			itemPedido.vlPctDescKitFechado = itemKit.vlPctDesconto;
		}
	}

	private ItemPedido resetAndCalculateItemPedido(final Pedido pedido, ItemPedido itemPedido) throws SQLException {
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
		}
		PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
		return itemPedido;
	}
	
	public ItemPedido getItemPedido(final Pedido pedido, final ItemKit itemKit) throws SQLException {
		ItemPedido itemPedido = new ItemPedido(pedido);
		itemPedido.pedido = pedido;
		itemPedido.setProduto(ProdutoService.getInstance().getProduto(itemKit.cdProduto));
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			itemPedido.setQtItemFisico(itemKit.qtItemKit);
		} else if (LavenderePdaConfig.isUsaKitProdutoFechado() && LavenderePdaConfig.usaConversaoUnidadesMedida) {
			itemPedido.qtItemFaturamento = itemKit.qtMinItem;
			itemPedido.flEditandoQtItemFaturamento = true;
			aplicarConversaoUnidadeMedida(itemPedido, pedido);
		} else {
			itemPedido.setQtItemFisico(itemKit.qtMinItem);
		}
		itemPedido.cdProduto = itemKit.cdProduto;
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			itemPedido.cdTabelaPreco = itemKit.cdTabelaPreco;
		} else {
			itemPedido.cdTabelaPreco = ValueUtil.isNotEmpty(pedido.cdTabelaPreco) ? pedido.cdTabelaPreco : itemKit.cdTabelaPrecoFilter;
		}
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.nuSeqItemPedido = getNextNuSeqItemPedido(pedido);
		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdUnidade = getCdUnidadeItemKit(itemPedido, itemKit);
		itemPedido.cdKit = itemKit.cdKit;
		itemPedido.dsProduto = itemKit.dsProduto;
		if (itemKit.kit != null && itemKit.kit.isEditaDesconto()) {
			itemPedido.vlPctDesconto = itemKit.vlPctDesconto;
		} else {
		itemPedido.vlPctDescKitFechado = itemKit.vlPctDesconto;
		}
		if (LavenderePdaConfig.isPermiteItemBonificado() && itemKit.isBonificado()) {
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		}
		itemPedido.setItemKit(itemKit);
		return itemPedido;
	}
	
	public ItemPedido getItemPedidoByItemPedidoGrade(ItemPedidoGrade itemPedidoGrade) throws SQLException {
		if (itemPedidoGrade != null) {
			ItemPedido itemPedidoFilter = new ItemPedido();
			itemPedidoFilter.cdEmpresa = itemPedidoGrade.cdEmpresa;
			itemPedidoFilter.cdRepresentante = itemPedidoGrade.cdRepresentante;
			itemPedidoFilter.flOrigemPedido = itemPedidoGrade.flOrigemPedido;
			itemPedidoFilter.nuPedido = itemPedidoGrade.nuPedido;
			itemPedidoFilter.cdProduto = itemPedidoGrade.cdProduto;
			itemPedidoFilter.nuSeqProduto = itemPedidoGrade.nuSeqProduto;
			return (ItemPedido) findByRowKey(itemPedidoFilter.getRowKey());
		}
		return null;
	}
	
	protected ItemPedido getItemPedidoByRemessaEstoque(ItemPedidoRemessa itemPedidoRemessa) throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = itemPedidoRemessa.cdEmpresa;
		itemPedidoFilter.cdRepresentante = itemPedidoRemessa.cdRepresentante;
		itemPedidoFilter.flOrigemPedido = itemPedidoRemessa.flOrigemPedido;
		itemPedidoFilter.nuPedido = itemPedidoRemessa.nuPedido;
		itemPedidoFilter.cdProduto = itemPedidoRemessa.cdProduto;
		itemPedidoFilter.nuSeqProduto = itemPedidoRemessa.nuSeqProduto;
		itemPedidoFilter.flTipoItemPedido = itemPedidoRemessa.flTipoItemPedido;
		return (ItemPedido) findByRowKey(itemPedidoFilter.getRowKey());
	}
	

	public ItemPedido getItemPedidoTrocaRecByCdProduto(final Pedido pedido, final String cdProduto) throws SQLException {
		return getItemPedidoByCdProduto(pedido, cdProduto, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC, ItemPedido.NUSEQPRODUTO_UNICO);
	}

	public ItemPedido getItemPedidoByCdProduto(final Pedido pedido, final String cdProduto) throws SQLException {
		return getItemPedidoByCdProduto(pedido, cdProduto, pedido.isPedidoBonificacao() ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL, ItemPedido.NUSEQPRODUTO_UNICO);
	}

	public ItemPedido getItemPedidoByCdProduto(final Pedido pedido, final String cdProduto, final int nuSeqItemPedido) throws SQLException {
		return getItemPedidoByCdProduto(pedido, cdProduto, pedido.isPedidoBonificacao() ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL, nuSeqItemPedido);
	}

	public ItemPedido getItemPedidoByCdProduto(final Pedido pedido, final String cdProduto, final String tipoItemPedido, final int nuSeqProduto) throws SQLException {
		if (pedido != null && ValueUtil.isNotEmpty(cdProduto)) {
			ItemPedido itemPedidoFilter = new ItemPedido();
			itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
			itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
			itemPedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			itemPedidoFilter.nuPedido = pedido.nuPedido;
			itemPedidoFilter.cdProduto = cdProduto;
			itemPedidoFilter.flTipoItemPedido = tipoItemPedido;
			itemPedidoFilter.nuSeqProduto = nuSeqProduto;
			return (ItemPedido) findByRowKey(itemPedidoFilter.getRowKey());
		}
		return null;
	}

	public ItemPedido getItemPedidoFromPedidoRelacionado(String cdProduto, String nuPedidoRelacionado) throws SQLException {
		if (ValueUtil.isNotEmpty(cdProduto)) {
			ItemPedido itemPedidoFilter = new ItemPedido();
			itemPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			itemPedidoFilter.nuPedido = nuPedidoRelacionado;
			itemPedidoFilter.cdProduto = cdProduto;
			Vector itemPedidoList = findAllByExample(itemPedidoFilter);
			if (ValueUtil.isNotEmpty(itemPedidoList)) {
				return (ItemPedido) itemPedidoList.items[0];
			}
		}
		return null;
	}

	public Vector getItensFromPedidoRelacionado(String nuPedidoRelacionado) throws SQLException {
		if (ValueUtil.isNotEmpty(nuPedidoRelacionado)) {
			ItemPedido itemPedidoRelFilter = new ItemPedido();
			itemPedidoRelFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemPedidoRelFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			itemPedidoRelFilter.nuPedido = nuPedidoRelacionado;
			return findAllByExample(itemPedidoRelFilter);
		}
		return null;
	}

	// Retorna o código da tabela de preço em que o produto aparece como promocional.
	public String getCdTabelaProdPromocional(final String[] tabPrecoList, final String dsTabPrecoPromo) {
		if (!ValueUtil.isEmpty(tabPrecoList)) {
			String[] dsTabPrecoPromoList = StringUtil.split(StringUtil.getStringValue(dsTabPrecoPromo), ProdutoTabPreco.SEPARADOR_CAMPOS);
			for (int j = 0; j < tabPrecoList.length; j++) {
				String cdTabela = tabPrecoList[j];
				for (int z = 0; z < dsTabPrecoPromoList.length; z++) {
					if (cdTabela.equals(dsTabPrecoPromoList[z])) {
						return cdTabela;
					}
				}
			}
		}
		return "";
	}

	// Retorna o código da tabela de preço em que o produto tem um desconto promocional.
	public String getCdTabelaProdComDescPromocional(final String[] tabPrecoList, final String dsTabPrecoPromo) {
		if (!ValueUtil.isEmpty(tabPrecoList)) {
			String[] dsTabPrecoPromoList = StringUtil.split(StringUtil.getStringValue(dsTabPrecoPromo), ProdutoTabPreco.SEPARADOR_CAMPOS);
			for (int j = 0; j < tabPrecoList.length; j++) {
				String cdTabela = tabPrecoList[j];
				for (int z = 0; z < dsTabPrecoPromoList.length; z++) {
					if (cdTabela.equals(dsTabPrecoPromoList[z])) {
						return cdTabela;
					}
				}
			}
		}
		return "";
	}

	// Retorna o código da tabela de preço em que o produto possui oportunidade de venda.
	public String getCdTabelaProdComOportunidadeVenda(final String[] tabPrecoList, final String dsTabPrecoOportunidade) {
		if (!ValueUtil.isEmpty(tabPrecoList)) {
			String[] dsTabPrecoOportunidadeList = StringUtil.split(StringUtil.getStringValue(dsTabPrecoOportunidade), ProdutoTabPreco.SEPARADOR_CAMPOS);
			for (int j = 0; j < tabPrecoList.length; j++) {
				String cdTabela = tabPrecoList[j];
				for (int z = 0; z < dsTabPrecoOportunidadeList.length; z++) {
					if (cdTabela.equals(dsTabPrecoOportunidadeList[z])) {
						return cdTabela;
					}
				}
			}
		}
		return "";
	}

	public double getTicketMedioDiarioRep(final String cdRep) throws SQLException {
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedido.cdRepresentante = cdRep;
		pedido.dtEmissao = DateUtil.getCurrentDate();
		Vector list = PedidoService.getInstance().findAllByExample(pedido);
		double sumVlTotalPedido = 0;
		double sumQtdItemFisico = 0;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			pedido = (Pedido) list.items[i];
			PedidoService.getInstance().findItemPedidoList(pedido);
			int sizeItensPeiddo = pedido.itemPedidoList.size();
			for (int j = 0; j < sizeItensPeiddo; j++) {
				sumQtdItemFisico += ((ItemPedido) pedido.itemPedidoList.items[j]).getQtItemFisico();
			}
			sumVlTotalPedido += pedido.vlTotalPedido;
		}
		pedido = null;
		list = null;
		return sumQtdItemFisico == 0 ? 0 : sumVlTotalPedido / sumQtdItemFisico;
	}

	public double getVlBaseByVlUnidadeAlternativa(ItemPedido itemPedido, double vlBase) throws SQLException {
		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		if (produtoUnidade != null) {
			return calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, vlBase, false);
		}
		return vlBase;
	}
	
	public double getVlBaseByVlUnidadeAlternativa(ItemPedido itemPedido, double vlBase, boolean naoAplicaIndiceVlBaseRentabilidade) throws SQLException {
		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		if (produtoUnidade != null) {
			return calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, vlBase, naoAplicaIndiceVlBaseRentabilidade);
		}
		return vlBase;
	}

	public double getVlTotalPedidoComSt(Pedido pedido) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().getVlTotalPedidoComSt(pedido);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().getVlTotalPedidoComSt(pedido);
		}
	}

	public double getVlTotalPedidoComIpi(Pedido pedido) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().getVlTotalPedidoComIpi(pedido);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().getVlTotalPedidoComIpi(pedido);
		}
	}

	public double getVlTotalPedidoComTributos(Pedido pedido) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().getVlTotalPedidoComTributos(pedido);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().getVlTotalPedidoComTributos(pedido);
		}
	}
	
	public void loadTributacaoItensPedido(Pedido pedido) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			pedido.itemPedidoListCalculoTributacao = ItemPedidoPdbxDao.getInstance().getItemPedidoListParaCalculoTributacao(pedido);
		} else {
			pedido.itemPedidoListCalculoTributacao = ItemPedidoPdbxDao.getInstanceErp().getItemPedidoListParaCalculoTributacao(pedido);
		}
		TributosService.getInstance().calculaVlTotalPedidoComTributosEDeducoes(pedido);
	}

	public void aplicaValorUnidadeAlternativa(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && (!LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() || itemPedido.usandoPrecoUnidadePadraoConvertida)) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			itemPedido.nuConversaoUnidade = 1;
			//--
			if (produtoUnidade != null) {
				calculaValorUnidadeAlternativa(itemPedido, produtoUnidade);
			}
		}
	}

	private void calculaValorUnidadeAlternativa(ItemPedido itemPedido, ProdutoUnidade produtoUnidade) throws SQLException {
		double newVlBaseItemPedido = calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, itemPedido.vlBaseItemTabelaPreco, false);

		itemPedido.nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
		itemPedido.vlBaseItemTabelaPreco = newVlBaseItemPedido;
		itemPedido.vlBaseItemPedido = newVlBaseItemPedido;
		itemPedido.vlBaseCalculoDescPromocional = newVlBaseItemPedido;
		itemPedido.vlItemPedido = newVlBaseItemPedido;

		if (LavenderePdaConfig.usaControlePontuacao) {
			itemPedido.vlBasePontuacao = calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, itemPedido.vlBasePontuacao, false);
		}

		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null) {
			boolean isMultiplica = produtoUnidade == null || produtoUnidade.isMultiplica();
			itemPedido.vlTotalItemTabelaPreco = calculaVlTotalItemTabelaPrecoItemPedido(itemTabelaPreco.vlUnitario, itemPedido.nuConversaoUnidade, isMultiplica);
		}

		if (produtoUnidade != null) {
			if (!LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
				if (produtoUnidade.isMultiplica()) {
					itemPedido.vlUnidadePadrao = newVlBaseItemPedido / produtoUnidade.nuConversaoUnidade;
				} else {
					itemPedido.vlUnidadePadrao = newVlBaseItemPedido * produtoUnidade.nuConversaoUnidade;
				}
			} else {
				itemPedido.vlUnidadePadrao = newVlBaseItemPedido;
			}
		}

	}

	public double calculaVlTotalItemTabelaPrecoItemPedido(double vlUnitarioItemTabelaPreco, double nuConversaoUnidade, boolean isMultiplica) {
		return (isMultiplica) ? ValueUtil.round(vlUnitarioItemTabelaPreco * nuConversaoUnidade) : ValueUtil.round(vlUnitarioItemTabelaPreco / nuConversaoUnidade);
	}

	public void loadQtElementarAndVlElementarItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			aplicaQtdadeElementarValorElementarNoItemPedido(itemPedido, itemPedido.vlUnidadePadrao, itemPedido.getProdutoUnidade());
			itemPedido.vlBaseEmbalagemElementar = itemPedido.vlEmbalagemElementar;
			if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
				double newVlItemPedido = roundUnidadeAlternativa(calculaVlItemByVlElementar(itemPedido, itemPedido.vlEmbalagemElementar));
				itemPedido.vlBaseItemPedido = newVlItemPedido;
				itemPedido.vlItemPedido = newVlItemPedido;
			}
		}
	}

	public void aplicaQtdadeElementarValorElementarNoItemPedido(ItemPedido itemPedido, double vlBaseItemPedido, ProdutoUnidade produtoUnidade) throws SQLException {
		aplicaQtdElementarItemPedido(itemPedido, produtoUnidade);
		aplicaVlEmbalagemElementarItemPedido(produtoUnidade, vlBaseItemPedido, itemPedido);
	}
	
	private String getFlDivideMultiplica(ItemPedido itemPedido, ProdutoUnidade produtoUnidade) {
		String flDivideMultiplica = !itemPedido.pedido.isPedidoAberto() ? itemPedido.flDivideMultiplicaPu : null;
		flDivideMultiplica = ValueUtil.isEmpty(flDivideMultiplica) ? produtoUnidade.flDivideMultiplica : flDivideMultiplica;
		return flDivideMultiplica;
	}
	
	private double getNuConversaoUnidade(ItemPedido itemPedido, ProdutoUnidade produtoUnidade) throws SQLException {
		double nuConversaoUnidade = !itemPedido.pedido.isPedidoAberto() ?  itemPedido.nuConversaoUnidadePu : 0;
		nuConversaoUnidade = nuConversaoUnidade == 0  ? ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade) : nuConversaoUnidade;
		return nuConversaoUnidade == 0 ? 1 : nuConversaoUnidade;
	}

	public void aplicaQtdElementarItemPedido(ItemPedido itemPedido, ProdutoUnidade produtoUnidade) throws SQLException {
		if (produtoUnidade == null) {
			produtoUnidade = new ProdutoUnidade();
			produtoUnidade.vlIndiceFinanceiro = 1;
		} else {
			if (produtoUnidade.vlIndiceFinanceiro == 0) {
				produtoUnidade.vlIndiceFinanceiro = 1;
			}
		}
		Produto produto = itemPedido.getProduto();
		double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
		if (nuConversaoUnidadesMedida == 0) {
			nuConversaoUnidadesMedida = 1;
		}
		double nuConversaoUnidade = getNuConversaoUnidade(itemPedido, produtoUnidade);
		if (produto.nuFracao > 0 && LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			nuConversaoUnidadesMedida = produto.nuFracao;
		}
		if (nuConversaoUnidadesMedida == 1 && !produtoUnidade.isMultiplica()) {
			itemPedido.qtEmbalagemElementar = nuConversaoUnidade;
		} else {
			itemPedido.qtEmbalagemElementar = getQtdEmbalagemSelecionada(getFlDivideMultiplica(itemPedido, produtoUnidade), nuConversaoUnidadesMedida, nuConversaoUnidade);
		}
	}

	public double getQtdEmbalagemSelecionada(String flDivideMultiplica, double nuConversaoUnidadesMedida, double nuConversaoUnidade) {
		if (nuConversaoUnidade == 1) {
			return nuConversaoUnidadesMedida;
		}
		if (ProdutoUnidade.FL_MULTIPLICA.equals(flDivideMultiplica)) {
			return ValueUtil.round(nuConversaoUnidadesMedida * nuConversaoUnidade);
		} else {
			return ValueUtil.round(nuConversaoUnidadesMedida / (nuConversaoUnidade == 0 ? 1 : nuConversaoUnidade));
		}
	}

	private void aplicaVlEmbalagemElementarItemPedido(ProdutoUnidade produtoUnidade, double vlBaseItemPedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
				double nuConversaoUnidade = itemPedido.getItemTabelaPreco().getNuConversaoUnidade();
				if (nuConversaoUnidade == 0) {
					nuConversaoUnidade = 1;
				}
				itemPedido.vlEmbalagemElementar = roundUnidadeAlternativa(vlBaseItemPedido / nuConversaoUnidade);
			} else {
				Produto produto = itemPedido.getProduto();
				double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
				if (nuConversaoUnidadesMedida == 0) {
					nuConversaoUnidadesMedida = 1;
				}
				if (produtoUnidade == null) {
					produtoUnidade = new ProdutoUnidade();
				}
				boolean usaUnidadeFracao = produto.nuFracao > 0 && LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto;
				int nuFracao = usaUnidadeFracao ? produto.nuFracao : 1;
				if (usaUnidadeFracao) {
					nuConversaoUnidadesMedida = nuFracao;
				}
				double nuConversaoUnidade = getNuConversaoUnidade(itemPedido, produtoUnidade);
				double vlEmbalagemElementar = 0;
				if (nuConversaoUnidade == 1) {
					vlEmbalagemElementar = vlBaseItemPedido / nuFracao;
				} else {
					String flDivideMultipla = getFlDivideMultiplica(itemPedido, produtoUnidade);
					boolean multiplica = ValueUtil.valueEquals(ProdutoUnidade.FL_MULTIPLICA, flDivideMultipla);
					double qtEmbalagemSelecionada = getQtdEmbalagemSelecionada(flDivideMultipla, nuConversaoUnidadesMedida, nuConversaoUnidade);
					double vlUnitarioUnidadeSelecionada = 0;
					if (multiplica) {
						vlUnitarioUnidadeSelecionada = roundUnidadeAlternativa(vlBaseItemPedido * nuConversaoUnidade);
					} else {
						vlUnitarioUnidadeSelecionada = vlBaseItemPedido / (nuConversaoUnidade == 0 ? 1 : nuConversaoUnidade);
					}
					if (nuConversaoUnidadesMedida == 1 && !multiplica) {
						vlEmbalagemElementar = roundUnidadeAlternativa(vlUnitarioUnidadeSelecionada);
					} else {
						vlEmbalagemElementar = roundUnidadeAlternativa(vlUnitarioUnidadeSelecionada / (qtEmbalagemSelecionada == 0 ? 1 : qtEmbalagemSelecionada));
					}
				}
				itemPedido.vlEmbalagemElementar = vlEmbalagemElementar;
			}
		}
	}

	public double calculaVlEmbalagemElementarPorProdutoUnidade(ProdutoUnidade produtoUnidade, double nuConversaoUnidadesMedida, double vlUnidade, ItemPedido itemPedido) throws SQLException {
		if (produtoUnidade.vlIndiceFinanceiro == 0) {
			produtoUnidade.vlIndiceFinanceiro = 1;
		}
		double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
		double qtEmbalagemSelecionada = getQtdEmbalagemSelecionada(produtoUnidade.flDivideMultiplica, nuConversaoUnidadesMedida, nuConversaoUnidade);
		double vlEmbalagemElementar = 0;
		if (nuConversaoUnidadesMedida == 1 && !produtoUnidade.isMultiplica()) {
			vlEmbalagemElementar = ValueUtil.round(vlUnidade);
		} else {
			vlEmbalagemElementar = ValueUtil.round(vlUnidade / qtEmbalagemSelecionada);
		}
		//-- Verifica se aplica o indice finaceiro ou não
		boolean isTabelaPromocao = ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(itemPedido.cdProduto, itemPedido.getItemTabelaPreco().cdTabelaPreco);
		boolean isDescPromocional = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido);
		if ((LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() && isTabelaPromocao) || ((LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() || LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosDescPromocional()) && isDescPromocional)) {
			return vlEmbalagemElementar;
		} else {
			return vlEmbalagemElementar * produtoUnidade.vlIndiceFinanceiro;
		}
	}

	public void aplicaValorElementarNoVlItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			itemPedido.vlItemPedido = calculaVlItemByVlElementar(itemPedido, itemPedido.vlEmbalagemElementar);
		}
	}

	public double calculaVlUnidadeAlternativa(ItemPedido itemPedido, ProdutoUnidade produtoUnidade, double vlBaseCalculo, boolean naoAplicaIndiceVlBaseRentabilidade) throws SQLException {
		double newVlBaseItemPedido = vlBaseCalculo;
		if (produtoUnidade.vlIndiceFinanceiro == 0) {
			produtoUnidade.vlIndiceFinanceiro = 1;
		}
		if (!LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			newVlBaseItemPedido = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, newVlBaseItemPedido);
		}
		return naoAplicaIndiceVlBaseRentabilidade ? newVlBaseItemPedido : aplicaIndiceFinanceiroUnidadeAlternativa(itemPedido, newVlBaseItemPedido, produtoUnidade);
	}
	
	public double aplicaMultiplicacaoDivisao(ItemPedido itemPedido, ProdutoUnidade produtoUnidade, double vlBaseCalculo) throws SQLException {
		double nuConversaoUnidade = getNuConversaoUnidade(itemPedido, produtoUnidade);
		String flDivideMultiplica = getFlDivideMultiplica(itemPedido, produtoUnidade);
		vlBaseCalculo = LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa ? ValueUtil.round(vlBaseCalculo) : vlBaseCalculo;
		if (ValueUtil.valueEquals(ProdutoUnidade.FL_MULTIPLICA, flDivideMultiplica)) {
			return ValueUtil.round(vlBaseCalculo * nuConversaoUnidade);
		} else {
			return ValueUtil.round(vlBaseCalculo / nuConversaoUnidade);
		}
	}
	
	public void aplicaMargemDesconto(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if (!(LavenderePdaConfig.usaGerenciaDeCreditoDesconto && itemPedido.qtdCreditoDesc > 0 && itemPedido.isFlTipoCadastroDesconto())) {
			if (cliente != null && LavenderePdaConfig.getVlPctMargemDescontoItemPedido() > 0) {
				if ((!DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) || itemPedido.bloqueiaAplicaDescPromocional) && !itemPedido.getProduto().isEspecial()) {
					double vlPctMargemDesconto = LavenderePdaConfig.getVlPctMargemDescontoItemPedido();
					ProdutoMargem produtoMargem = ProdutoMargemService.getInstance().getProdutoMargem(itemPedido.getProduto(), cliente.cdRamoAtividade);
					if (produtoMargem != null) {
						vlPctMargemDesconto = produtoMargem.vlPctMargemDesconto;
					}
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido / (1 - (vlPctMargemDesconto / 100)));
					itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido / (1 - (vlPctMargemDesconto / 100)));
					if (LavenderePdaConfig.usaUnidadeAlternativa) {
						itemPedido.vlEmbalagemElementar = ValueUtil.round(itemPedido.vlEmbalagemElementar / (1 - (vlPctMargemDesconto / 100)));
						itemPedido.vlUnidadePadrao = ValueUtil.round(itemPedido.vlUnidadePadrao / (1 - (vlPctMargemDesconto / 100)));
					}
				}
			}
		}
	}
	
	protected void aplicaDescontoKitFechado(ItemPedido itemPedido) throws SQLException { 
		if (LavenderePdaConfig.isUsaKitProdutoFechado() && itemPedido.isFazParteKitFechado() && !KitService.getInstance().isEditaDescontoKit(itemPedido.cdKit)) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDescKitFechado / 100))); 
			itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescKitFechado / 100))); 
		}
	}
	
	protected void aplicaDescontoProdutoRestrito(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaProdutoRestrito || itemPedido.vlPctDescProdutoRestrito == 0) return;
		
		double vlItemPedidoSemDesconto = itemPedido.vlItemPedido;
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDescProdutoRestrito / 100))); 
		itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescProdutoRestrito / 100)));
		itemPedido.vlDescProdutoRestrito = vlItemPedidoSemDesconto -  itemPedido.vlItemPedido;
	}
	
	public void aplicaMultiplicacaoDivisaoAposAplicacaoIndices(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				itemPedido.vlUnidadePadrao = ValueUtil.round(itemPedido.vlBaseItemPedido);
				double vlBase = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, itemPedido.vlBaseItemPedido);
				itemPedido.nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
				itemPedido.vlBaseItemTabelaPreco = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, itemPedido.vlBaseItemTabelaPreco);
				itemPedido.vlBaseItemPedido = vlBase;
				itemPedido.vlBaseCalculoDescPromocional = vlBase;
				itemPedido.vlItemPedido = vlBase;
			} else {
				double vlBase = ValueUtil.round(itemPedido.vlBaseItemPedido);
				itemPedido.vlBaseItemPedido = vlBase;
				itemPedido.vlBaseCalculoDescPromocional = vlBase;
				itemPedido.vlItemPedido = vlBase;
				itemPedido.vlEmbalagemElementar = ValueUtil.round(itemPedido.vlEmbalagemElementar);
			}
		}
	}

	public double aplicaIndiceFinanceiroUnidadeAlternativa(ItemPedido itemPedido, double newVlBaseItemPedido, ProdutoUnidade produtoUnidade) throws SQLException {
		if ((LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() || LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosDescPromocional()) && 
				  DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido)) {
			return newVlBaseItemPedido;
		}
		if (LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() && ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(itemPedido.cdProduto, itemPedido.getItemTabelaPreco().cdTabelaPreco)) {
		    return newVlBaseItemPedido;
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
			return newVlBaseItemPedido;
		}
		double vlIndiceFinanceiro = !itemPedido.pedido.isPedidoAberto() ? itemPedido.vlIndiceFinanceiroPu : 0;
		vlIndiceFinanceiro = vlIndiceFinanceiro == 0 ? produtoUnidade.vlIndiceFinanceiro : vlIndiceFinanceiro;
		return roundUnidadeAlternativa(newVlBaseItemPedido * vlIndiceFinanceiro);
		
	}
	
	private ItemPedido createSimpleNewItemPedido(Pedido pedido) {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		return itemPedido;
	}

	public ItemPedido createNewItemPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = createSimpleNewItemPedido(pedido);
		itemPedido.pedido = pedido;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemPedido.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		return itemPedido;
	}

	public ItemPedido createNewItemPedidoRelacionadoByItemPedidoPrincipal(final ItemPedido itemPedidoPrincipal, final Produto produto, final Pedido pedido) throws SQLException {
  		ItemPedido itemPedidoNew = createNewItemPedido(pedido);
		if (itemPedidoPrincipal == null || produto == null || pedido == null) {
			return itemPedidoNew;
		}
		itemPedidoNew.nuSeqItemPedido = getNextNuSeqItemPedido(pedido);
		itemPedidoNew.setProduto(produto);
		itemPedidoNew.vlPctDesconto = itemPedidoPrincipal.vlPctDesconto;
		itemPedidoNew.vlPctAcrescimo = itemPedidoPrincipal.vlPctAcrescimo;
		itemPedidoNew.cdTabelaPreco = itemPedidoPrincipal.cdTabelaPreco;
		setVlBaseItemPedidoFromItemTabelaPreco(itemPedidoNew);
		itemPedidoNew.vlItemPedido = itemPedidoPrincipal.vlItemPedido;
		itemPedidoNew.flEditandoQtItemFaturamento = LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento || LavenderePdaConfig.usaConversaoUnidadesMedida;
		if (itemPedidoNew.flEditandoQtItemFaturamento) {
			if (!LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento) {
				itemPedidoNew.qtItemFaturamento = ValueUtil.getIntegerValueRoundUp(ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(pedido.getCliente(), produto, produto.qtFaltanteProdutoRelacionado));
			} else {
				itemPedidoNew.qtItemFaturamento = produto.qtFaltanteProdutoRelacionado;
			}
		} else {
			itemPedidoNew.setQtItemFisico(produto.qtFaltanteProdutoRelacionado);
		}
		itemPedidoNew.cdUnidade = itemPedidoNew.getProduto().cdUnidade;
		itemPedidoNew.flPrecoLiberadoSenha = itemPedidoPrincipal.flPrecoLiberadoSenha;
		return itemPedidoNew;
	}


	public void validaVendaProdutoBaseadoRealizadoMetaGrupoProd(ItemPedido itemPedido, boolean validaRealizadoMeta, Pedido pedido, boolean updateRealizadoMetaPorGrupoProdAposEnvioServidor) throws SQLException {
		MetasPorGrupoProduto metasPorGrupoProdutoFilter = new MetasPorGrupoProduto();
		metasPorGrupoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metasPorGrupoProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		metasPorGrupoProdutoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		Vector metaList = MetasPorGrupoProdutoService.getInstance().findAllMetasVigentes(metasPorGrupoProdutoFilter);
		if (ValueUtil.isNotEmpty(metaList)) {
			int nuNivelMetaGrupoProduto;
			int nuNivelGrupoProdutoNoProduto = nuNivelGrupoProdutoNoProduto(itemPedido.getProduto());
			int count = 0;
			metaList = removeMetasQueNaoSeAplicam(metaList, itemPedido.getProduto());
			if (ValueUtil.isNotEmpty(metaList)) {
				int size = metaList.size();
				for (int i = 0; i < size; i++) {
					MetasPorGrupoProduto metasPorGrupoProdutoRemove = (MetasPorGrupoProduto) metaList.items[count];
					count++;
					nuNivelMetaGrupoProduto = nuNivelMetaGrupoProduto(metasPorGrupoProdutoRemove);
					if (nuNivelMetaGrupoProduto > nuNivelGrupoProdutoNoProduto) {
						metaList.removeElement(metasPorGrupoProdutoRemove);
						count--;
					}
				}
				MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) metaList.items[0];
				size = metaList.size();
				for (int i = 0; i < size; i++) {
					MetasPorGrupoProduto metasPorGrupoProdutoAux = (MetasPorGrupoProduto) metaList.items[i];
					if (metasPorGrupoProdutoAux.qtUnidadeMeta < metasPorGrupoProduto.qtUnidadeMeta) {
						metasPorGrupoProduto = metasPorGrupoProdutoAux;
					}
				}
				if (pedido.metaPorGrupoProdHash != null && pedido.metaPorGrupoProdHash.size() > 0 && pedido.metaPorGrupoProdHash.get(metasPorGrupoProduto.getRowKey()) != null) {
					metasPorGrupoProduto = (MetasPorGrupoProduto) pedido.metaPorGrupoProdHash.get(metasPorGrupoProduto.getRowKey());
				}
				nuNivelMetaGrupoProduto = nuNivelMetaGrupoProduto(metasPorGrupoProduto);
				if (nuNivelGrupoProdutoNoProduto >= nuNivelMetaGrupoProduto) {
					if (nuNivelMetaGrupoProduto == 1) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1)) {
							if (validaRealizadoMeta) {
								validaVlRealizadoMeta(metasPorGrupoProduto, itemPedido, nuNivelMetaGrupoProduto, pedido);
							} else {
								calculaRealizadoMeta(metasPorGrupoProduto, itemPedido, pedido, updateRealizadoMetaPorGrupoProdAposEnvioServidor);
							}
						}
					} else if (nuNivelMetaGrupoProduto == 2) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto2)) {
							if (validaRealizadoMeta) {
								validaVlRealizadoMeta(metasPorGrupoProduto, itemPedido, nuNivelMetaGrupoProduto, pedido);
							} else {
								calculaRealizadoMeta(metasPorGrupoProduto, itemPedido, pedido, updateRealizadoMetaPorGrupoProdAposEnvioServidor);
							}
						}
					} else if (nuNivelMetaGrupoProduto == 3) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto2) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto3, itemPedido.getProduto().cdGrupoProduto3)) {
							if (validaRealizadoMeta) {
								validaVlRealizadoMeta(metasPorGrupoProduto, itemPedido, nuNivelMetaGrupoProduto, pedido);
							} else {
								calculaRealizadoMeta(metasPorGrupoProduto, itemPedido, pedido, updateRealizadoMetaPorGrupoProdAposEnvioServidor);
							}
						}
					}
				}
			}
		}
	}

	public Vector removeMetasQueNaoSeAplicam(Vector metaList, Produto produto) {
		int size = metaList.size();
		int count = 0;
		int nuNivelGrupoProdProduto = nuNivelGrupoProdutoNoProduto(produto);
		for (int i = 0; i < size; i++) {
			MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) metaList.items[count];
			count++;
			int nuNivelGrupoProdMeta = nuNivelMetaGrupoProduto(metasPorGrupoProduto);
			if (nuNivelGrupoProdMeta > nuNivelGrupoProdProduto) {
				metaList.removeElement(metasPorGrupoProduto);
				count--;
			} else if (nuNivelGrupoProdMeta == 1) {
				if (!ValueUtil.valueEquals(produto.cdGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto1)) {
					metaList.removeElement(metasPorGrupoProduto);
					count--;
				}
			} else if (nuNivelGrupoProdMeta == 2) {
				if (!ValueUtil.valueEquals(produto.cdGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto1) || !ValueUtil.valueEquals(produto.cdGrupoProduto2, metasPorGrupoProduto.cdGrupoProduto2)) {
					metaList.removeElement(metasPorGrupoProduto);
					count--;
				}
			} else if (nuNivelGrupoProdMeta == 3) {
				if (!ValueUtil.valueEquals(produto.cdGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto1) || !ValueUtil.valueEquals(produto.cdGrupoProduto2, metasPorGrupoProduto.cdGrupoProduto2) || !ValueUtil.valueEquals(produto.cdGrupoProduto3, metasPorGrupoProduto.cdGrupoProduto3)) {
					metaList.removeElement(metasPorGrupoProduto);
					count--;
				}
			}
		}

		return metaList;
	}

	private void calculaRealizadoMeta(MetasPorGrupoProduto metasPorGrupoProduto, ItemPedido itemPedido, Pedido pedido, boolean updateRealizadoMetaPorGrupoProdAposEnvioServidor) throws SQLException {
		metasPorGrupoProduto.vlRealizadoPedidosPda += getPesoItemPedido(itemPedido);
		//se a meta nao estiver dentro da hash ainda, passa por todos os itens desse pedido e ve se tem algum que se adeque à ela, se sim, tambem soma
		if (pedido.metaPorGrupoProdHash.get(metasPorGrupoProduto.getRowKey()) == null) {
			PedidoService.getInstance().findItemPedidoList(pedido);
			Vector itemPedidoList = new Vector();
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				itemPedidoList.addElement(pedido.itemPedidoList.items[i]);
			}
			itemPedidoList.removeElement(itemPedido);
			int nuNivelMetaGrupoProduto = nuNivelMetaGrupoProduto(metasPorGrupoProduto);
			metasPorGrupoProduto.vlRealizadoPedidosPda += calculaRealizadoItensPedido(metasPorGrupoProduto, nuNivelMetaGrupoProduto, itemPedidoList);
		}
		pedido.metaPorGrupoProdHash.put(metasPorGrupoProduto.getRowKey(), metasPorGrupoProduto);
		if (updateRealizadoMetaPorGrupoProdAposEnvioServidor) {
			metasPorGrupoProduto.vlRealizado += metasPorGrupoProduto.vlRealizadoPedidosPda;
			MetasPorGrupoProdutoService.getInstance().update(metasPorGrupoProduto);
		}

	}

	private void validaVlRealizadoMeta(MetasPorGrupoProduto metasPorGrupoProduto, ItemPedido itemPedido, int nuNivelMetaGrupoProduto, Pedido pedido) throws SQLException {
		PedidoService.getInstance().findItemPedidoList(pedido);
		Vector itemPedidoList = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			itemPedidoList.addElement(pedido.itemPedidoList.items[i]);
		}
		itemPedidoList.removeElement(itemPedido);
		double vlTotalRealizadoMeta = calculaRealizadoOutrasMetas(pedido.metaPorGrupoProdHash, metasPorGrupoProduto) + metasPorGrupoProduto.vlRealizado + metasPorGrupoProduto.vlRealizadoPedidosPda + getPesoItemPedido(itemPedido) + calculaRealizadoItensPedido(metasPorGrupoProduto, nuNivelMetaGrupoProduto, itemPedidoList) - metasPorGrupoProduto.vlSaldoLiberadoSenha;
		if (ValueUtil.round(vlTotalRealizadoMeta) > ValueUtil.round(metasPorGrupoProduto.qtUnidadeMeta) && metasPorGrupoProduto.qtUnidadeMeta != 0) {
			if (nuNivelMetaGrupoProduto == 1) {
				GrupoProduto1 grupoProduto1 = findGrupoProduto1(metasPorGrupoProduto);
				if (grupoProduto1 != null) {
					if (ValueUtil.VALOR_SIM.equals(grupoProduto1.flBloqueiaVendaPorMeta)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_METAPORGRUPOPROD_VALIDATION, new Object[] { StringUtil.getStringValueToInterface(vlTotalRealizadoMeta - metasPorGrupoProduto.qtUnidadeMeta) }));
					}
				}
			} else if (nuNivelMetaGrupoProduto == 2) {
				GrupoProduto2 grupoProduto2 = findGrupoProduto2(metasPorGrupoProduto);
				if (grupoProduto2 != null) {
					if (ValueUtil.VALOR_SIM.equals(grupoProduto2.flBloqueiaVendaPorMeta)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_METAPORGRUPOPROD_VALIDATION, new Object[] { StringUtil.getStringValueToInterface(vlTotalRealizadoMeta - metasPorGrupoProduto.qtUnidadeMeta) }));
					}
				}
			} else if (nuNivelMetaGrupoProduto == 3) {
				GrupoProduto3 grupoProduto3 = findGrupoProduto3(metasPorGrupoProduto);
				if (grupoProduto3 != null) {
					if (ValueUtil.VALOR_SIM.equals(grupoProduto3.flBloqueiaVendaPorMeta)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_METAPORGRUPOPROD_VALIDATION, new Object[] { StringUtil.getStringValueToInterface(vlTotalRealizadoMeta - metasPorGrupoProduto.qtUnidadeMeta) }));
					}
				}
			}
		} else if (ValueUtil.round(metasPorGrupoProduto.qtUnidadeMeta) < (metasPorGrupoProduto.vlRealizado + metasPorGrupoProduto.vlRealizadoPedidosPda + getPesoItemPedido(itemPedido) + calculaRealizadoItensPedido(metasPorGrupoProduto, nuNivelMetaGrupoProduto, itemPedidoList))) {
			itemPedido.flMetaGrupoProdLiberadoSenha = ValueUtil.VALOR_SIM;
		}
	}

	public double calculaRealizadoOutrasMetas(Hashtable metaPorGrupoProdHash, MetasPorGrupoProduto metasPorGrupoProduto) {
		metaPorGrupoProdHash.remove(metasPorGrupoProduto.getRowKey());
		Vector metaList = metaPorGrupoProdHash.getValues();
		int size = metaList.size();
		double vlRealizado = 0;
		for (int i = 0; i < size; i++) {
			MetasPorGrupoProduto metasPorGrupoProdutoAux = (MetasPorGrupoProduto) metaList.items[i];
			if (isProdutoPertencenteAMeta(metasPorGrupoProduto, metasPorGrupoProdutoAux)) {
				vlRealizado += metasPorGrupoProdutoAux.vlRealizadoPedidosPda;
			}
		}
		metaPorGrupoProdHash.put(metasPorGrupoProduto.getRowKey(), metasPorGrupoProduto);
		return vlRealizado;
	}

	private boolean isProdutoPertencenteAMeta(MetasPorGrupoProduto metasPorGrupoProduto, MetasPorGrupoProduto metasPorGrupoProdutoAux) {
		int nuNivelMeta = nuNivelMetaGrupoProduto(metasPorGrupoProduto);
		int nuNivelMetaAux = nuNivelMetaGrupoProduto(metasPorGrupoProdutoAux);
		if (nuNivelMetaAux >= nuNivelMeta) {
			if (nuNivelMeta == 1) {
				if (metasPorGrupoProduto.cdGrupoProduto1.equals(metasPorGrupoProdutoAux.cdGrupoProduto1)) {
					return true;
				}
			} else if (nuNivelMeta == 2) {
				if (metasPorGrupoProduto.cdGrupoProduto1.equals(metasPorGrupoProdutoAux.cdGrupoProduto1) && metasPorGrupoProduto.cdGrupoProduto2.equals(metasPorGrupoProdutoAux.cdGrupoProduto2)) {
					return true;
				}
			} else if (nuNivelMeta == 3) {
				if (metasPorGrupoProduto.cdGrupoProduto1.equals(metasPorGrupoProdutoAux.cdGrupoProduto1) && metasPorGrupoProduto.cdGrupoProduto2.equals(metasPorGrupoProdutoAux.cdGrupoProduto2) && metasPorGrupoProduto.cdGrupoProduto3.equals(metasPorGrupoProdutoAux.cdGrupoProduto3)) {
					return true;
				}
			}
		}
		return false;
	}

	private GrupoProduto3 findGrupoProduto3(MetasPorGrupoProduto metasPorGrupoProduto) throws SQLException {
		GrupoProduto3 grupoProduto3Filter = new GrupoProduto3();
		grupoProduto3Filter.cdGrupoProduto1 = metasPorGrupoProduto.cdGrupoProduto1;
		grupoProduto3Filter.cdGrupoProduto2 = metasPorGrupoProduto.cdGrupoProduto2;
		grupoProduto3Filter.cdGrupoProduto3 = metasPorGrupoProduto.cdGrupoProduto3;
		GrupoProduto3 grupoProduto3 = (GrupoProduto3) GrupoProduto3Service.getInstance().findByRowKey(grupoProduto3Filter.getRowKey());
		return grupoProduto3;
	}

	private GrupoProduto2 findGrupoProduto2(MetasPorGrupoProduto metasPorGrupoProduto) throws SQLException {
		GrupoProduto2 grupoProduto2Filter = new GrupoProduto2();
		grupoProduto2Filter.cdGrupoProduto1 = metasPorGrupoProduto.cdGrupoProduto1;
		grupoProduto2Filter.cdGrupoProduto2 = metasPorGrupoProduto.cdGrupoProduto2;
		GrupoProduto2 grupoProduto2 = (GrupoProduto2) GrupoProduto2Service.getInstance().findByRowKey(grupoProduto2Filter.getRowKey());
		return grupoProduto2;
	}

	private GrupoProduto1 findGrupoProduto1(MetasPorGrupoProduto metasPorGrupoProduto) throws SQLException {
		GrupoProduto1 grupoProduto1Filter = new GrupoProduto1();
		grupoProduto1Filter.cdGrupoproduto1 = metasPorGrupoProduto.cdGrupoProduto1;
		GrupoProduto1 grupoProduto1 = (GrupoProduto1) GrupoProduto1Service.getInstance().findByRowKey(grupoProduto1Filter.getRowKey());
		return grupoProduto1;
	}
	
	public Vector findItemPedidoByPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(pedido);
		return findAllByExampleUnique(itemPedidoFilter);
		
	}

	public String findSqlItemPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(pedido);
		return ItemPedidoPdbxDao.getInstance().findAllByExampleSql(itemPedidoFilter);
	}

	protected ItemPedido getNewItemPedido(Pedido pedido) {
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoFilter.nuPedido = pedido.nuPedido;
		return itemPedidoFilter;
	}

	public void updateRealizadoMetaPorGrupoProdAposEnvioServidor(Pedido pedido) throws SQLException {
		PedidoService.getInstance().findItemPedidoList(pedido);
		Vector itemPedidoList = pedido.itemPedidoList;
		itemPedidoList = removeItensBonificados(itemPedidoList);
		//--
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			validaVendaProdutoBaseadoRealizadoMetaGrupoProd(itemPedido, false, pedido, true);
		}
	}

	private Vector removeItensBonificados(Vector itemPedidoList) {
		Vector itemPedidoListAux = new Vector();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			itemPedidoListAux.addElement(itemPedidoList.items[i]);
		}
		if (!LavenderePdaConfig.usaItensBonificadoCalculoRealizado) {
			int count = 0;
			int sizeAux = itemPedidoListAux.size();
			for (int i = 0; i < sizeAux; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoListAux.items[count];
				count++;
				if (itemPedido.isItemBonificacao()) {
					itemPedidoListAux.removeElement(itemPedido);
					count--;
				}
			}
			return itemPedidoListAux;
		}
		return itemPedidoList;
	}

	public double calculaRealizadoItensPedido(MetasPorGrupoProduto metasPorGrupoProduto, int nuNivelMetaGrupoProduto, Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		double qtPeso = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if ((itemPedido.isItemBonificacao() && LavenderePdaConfig.usaItensBonificadoCalculoRealizado) || itemPedido.isItemVendaNormal()) {
				if (nuNivelGrupoProdutoNoProduto(itemPedido.getProduto()) >= nuNivelMetaGrupoProduto) {
					if (nuNivelMetaGrupoProduto == 1) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1)) {
							qtPeso += getPesoItemPedido(itemPedido);
						}
					} else if (nuNivelMetaGrupoProduto == 2) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto2)) {
							qtPeso += getPesoItemPedido(itemPedido);
						}
					} else if (nuNivelMetaGrupoProduto == 3) {
						if (ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto1) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto2) && ValueUtil.valueEquals(metasPorGrupoProduto.cdGrupoProduto3, itemPedido.getProduto().cdGrupoProduto3)) {
							qtPeso += getPesoItemPedido(itemPedido);
						}
					}
				}
			}
		}
		return qtPeso;
	}

	public double getPesoItemPedido(ItemPedido itemPedido) throws SQLException {
		return getPesoItemPedido(itemPedido, itemPedido.getQtItemFisico());
	}
	
	public double getPesoItemPedido(ItemPedido itemPedido, double qtItemFisico) throws SQLException {
		if (ValueUtil.getBooleanValue(itemPedido.getProduto().flIgnoraPeso)) return 0;
		double vlPeso = itemPedido.getProduto() != null ? itemPedido.getPesoGrade() : 0.0;
		double qtItemCalculo = qtItemFisico;
		if (vlPeso == 0) {
			vlPeso = 1;
		}
		
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			double nuConversaoUnidade = itemPedido.nuConversaoUnidade == 0 ? 1 : itemPedido.nuConversaoUnidade;
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			if(LavenderePdaConfig.isConfigCalculoPesoPedidoNaoConsideraGramatura()){
				if (produtoUnidade.isMultiplica()) {
					qtItemCalculo = ValueUtil.round(qtItemFisico * nuConversaoUnidade);
				} else {
					qtItemCalculo = ValueUtil.round(qtItemFisico / nuConversaoUnidade);
				}
			}
			if(produtoUnidade.unidade == null) {
				produtoUnidade.unidade = UnidadeService.getInstance().findUnidadeByCdUnidade(produtoUnidade.cdUnidade);
			}
			if(LavenderePdaConfig.isConfigCalculoPesoPedidoConsideraGramatura()) {
				if(!ValueUtil.VALOR_SIM.equals(produtoUnidade.unidade.flCalculaPesoGramatura)) return qtItemFisico;
				qtItemCalculo = ValueUtil.round(((itemPedido.vlLargura / 1000) * (itemPedido.vlComprimento / 1000) * itemPedido.getProduto().vlGramatura) * qtItemFisico);
			}
		}
		return qtItemCalculo * vlPeso;
	}

	public int nuNivelGrupoProdutoNoProduto(Produto produto) {
		if (ValueUtil.isNotEmpty(produto.cdGrupoProduto3)) {
			return 3;
		} else if (ValueUtil.isNotEmpty(produto.cdGrupoProduto2)) {
			return 2;
		} else if (ValueUtil.isNotEmpty(produto.cdGrupoProduto1)) {
			return 1;
		} else {
			return 0;
		}
	}

	public int nuNivelMetaGrupoProduto(MetasPorGrupoProduto metasPorGrupoProduto) {
		if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto3) && !metasPorGrupoProduto.cdGrupoProduto3.equals("0")) {
			return 3;
		} else if (ValueUtil.isNotEmpty(metasPorGrupoProduto.cdGrupoProduto2) && !metasPorGrupoProduto.cdGrupoProduto2.equals("0")) {
			return 2;
		} else {
			return 1;
		}
	}

	public void updateRealizadoMetaPorGrupoProd(Pedido pedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
		pedidoList.removeElement(pedido);
		int sizePedidoList = pedidoList.size();
		if (ValueUtil.isNotEmpty(pedidoList)) {
			for (int i = 0; i < sizePedidoList; i++) {
				Pedido pedidoResult = (Pedido) pedidoList.items[i];
				PedidoService.getInstance().findItemPedidoList(pedidoResult);
				Vector itemPedidoList = pedidoResult.itemPedidoList;
				int itemSize = itemPedidoList.size();
				pedidoResult.metaPorGrupoProdHash = pedido.metaPorGrupoProdHash;
				for (int j = 0; j < itemSize; j++) {
					ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
					if ((itemPedido.isItemBonificacao() && LavenderePdaConfig.usaItensBonificadoCalculoRealizado) || itemPedido.isItemVendaNormal()) {
						validaVendaProdutoBaseadoRealizadoMetaGrupoProd(itemPedido, false, pedidoResult, false);
					}
				}
				pedido.metaPorGrupoProdHash = pedidoResult.metaPorGrupoProdHash;
			}
		}
	}

	public void validaBloqueioGrupos(ItemPedido itemPedido) throws SQLException {
		int nuNivelProduto = nuNivelGrupoProdutoNoProduto(itemPedido.getProduto());
		if (nuNivelProduto == 1) {
			throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_NAO_POSSUI_METAPORGRUPOPROD, GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1)));
		} else if (nuNivelProduto == 2) {
			throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_NAO_POSSUI_METAPORGRUPOPROD, GrupoProduto2Service.getInstance().getDsGrupoProduto(itemPedido.getProduto())));
		} else if (nuNivelProduto == 3) {
			throw new ValidationException(MessageUtil.getMessage(Messages.GRUPOPRODUTO_NAO_POSSUI_METAPORGRUPOPROD, GrupoProduto3Service.getInstance().getDsGrupoProduto(itemPedido.getProduto())));
		} else if (nuNivelProduto == 0) {
			throw new ValidationException(Messages.PRODUTO_NAO_POSSUI_GRUPOPRODUTO);
		}
	}

	public double calculaVlAgregadoSugerido(double vlItemPedido, double pctMargemAgregada) {
		return vlItemPedido * (1 + (pctMargemAgregada / 100));
	}

	public double calculaPctMargemAgregada(double vlItemPedido, double vlAgregadoSugerido) {
		//-- vlItemPedido precisa ser maior que 0 para calculo ser válido
		if (vlItemPedido > 0) {
			return ((vlAgregadoSugerido - vlItemPedido) * 100) / vlItemPedido;
		}
		return 0;
	}

	public boolean validateItemTabelaPrecoItemPedidoList(Pedido pedido, String cdCondicaoPagamento) throws SQLException {
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento();
			condicaoPagamentoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
			condicaoPagamentoFilter.cdEmpresa = pedido.cdEmpresa;
			condicaoPagamentoFilter.cdRepresentante = pedido.cdRepresentante;
			CondicaoPagamento condicaoPagamento = (CondicaoPagamento) CondicaoPagamentoService.getInstance().findByRowKey(condicaoPagamentoFilter.getRowKey());
			Vector itemPedidoList = pedido.itemPedidoList;
			Vector produtoErroList = new Vector();
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				itemPedido.setItemTabelaPrecoOld(itemPedido.getItemTabelaPreco());
				itemPedido.setItemTabelaPreco(null);
				itemPedido.cdPrazoPagtoPrecoOld = itemPedido.cdPrazoPagtoPreco;
				itemPedido.cdPrazoPagtoPreco = condicaoPagamento.cdPrazoPagtoPreco;
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco(StringUtil.getStringValue(condicaoPagamento.cdPrazoPagtoPreco));
				if (itemTabelaPreco != null && ValueUtil.isEmpty(itemTabelaPreco.cdTabelaPreco)) {
					itemPedido.getProduto().qtItemPedido = itemPedido.getQtItemFisico();
					produtoErroList.addElement(itemPedido.getProduto());
				}
			}
			if (ValueUtil.isNotEmpty(produtoErroList)) {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
					itemPedido.setItemTabelaPreco(itemPedido.getItemTabelaPrecoOld());
					itemPedido.setItemTabelaPrecoOld(null);
					itemPedido.cdPrazoPagtoPreco = itemPedido.cdPrazoPagtoPrecoOld;
					itemPedido.cdPrazoPagtoPrecoOld = -1;

				}
				ListProdutoPrecoErroWindow listProdutoPrecoErroWindow = new ListProdutoPrecoErroWindow(produtoErroList, condicaoPagamento);
				listProdutoPrecoErroWindow.popup();
				return false;
			}
		}
		return true;
	}

	public Vector findAllQtItemFisicoByExample(ItemPedido itemPedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().findAllQtItemFisicoByExample(itemPedido);
	}

	public ItemPedido createItemPedidoFilterByFrequencia(Pedido pedido) {
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoFilter.nuPedido = pedido.nuPedido;
		itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		return itemPedidoFilter;
	}

	public String getTipoDescQtdListaItemPedido(ItemPedido itemPedido) throws SQLException {
		String vlParam = LavenderePdaConfig.tipoDescricaoQtdListaItemPedido;
		if ("1".equals(vlParam) || ValueUtil.VALOR_SIM.equals(vlParam)) {
			if (ValueUtil.isNotEmpty(itemPedido.cdUnidade)) {
				return itemPedido.cdUnidade;
			}
		} else if ("2".equals(vlParam)) {
			String dsUnidade = UnidadeService.getInstance().getDsUnidade(itemPedido.cdUnidade);
			if (ValueUtil.isNotEmpty(dsUnidade)) {
				return dsUnidade;
			}
		}
		return (itemPedido.getQtItemFisico() == 1 ? Messages.LISTA_ITEMPEDIDO_DESC_QUANTIDADE_ITEM : Messages.LISTA_ITEMPEDIDO_DESC_QUANTIDADE_ITEMS);
	}
	
	public boolean aplicaIndiceFinanceiroCondPagtoProd(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (isAnulaIndiceFinanceiroCondPagtoTabPrecoPromOrProdProm(itemPedido)) {
			return false;
		} 
		double vlIndiceFinanceiro = CondPagtoProdService.getInstance().findVlIndiceFinanceiroCondPagtoPorProduto(pedido, itemPedido);
		if (vlIndiceFinanceiro != 0) {
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
			return true;
		} 
		return false;
	}

	
	public void aplicaDescontoAcrescimoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto && itemPedido.pedido.isReplicandoPedido && itemPedido.isFlPrecoLiberadoSenha()) {
			if (itemPedido.pedido.solicitaSenhaLiberaPrecoProdutoReplicacao ) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(Messages.PEDIDO_MSG_LIBERA_PRECO);
				senhaForm.setCdCliente(itemPedido.pedido.getCliente().cdCliente);
				senhaForm.setCdProduto(itemPedido.cdProduto);
				senhaForm.setQtdeProduto(itemPedido.getQtItemFisico());
				senhaForm.setVlProduto(itemPedido.vlNegociado);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_PRECO_PRODUTO);
				if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					UiUtil.showMessage(Messages.PEDIDO_MSG_LIBERA_PRECO_SUCESSO,
							Messages.ITEMPEDIDO_LABEL_QTITEMFISICO + ".: " + StringUtil.getStringValueToInterface(senhaForm.edQtdProduto.getValueDouble()) + " , " +
									Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO + ": " + StringUtil.getStringValueToInterface(senhaForm.edValorProduto.getValueDouble()), TYPE_MESSAGE.SUCCESS);
					itemPedido.qtItemMinAfterLibPreco = senhaForm.edQtdProduto.getValueDouble();
					itemPedido.vlItemMinAfterLibPreco = senhaForm.edValorProduto.getValueDouble();
					itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
					if (senhaForm.editouValoresProduto) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
					}
				} else {
					desfazPrecoLiberadoSenha(itemPedido);
				}
			} else {
				desfazPrecoLiberadoSenha(itemPedido);
			}
		} else {
			aplicaDescontoAcrescimoDoPedido(itemPedido);
		}
	}
	
	private void aplicaDescontoAcrescimoDoPedido(ItemPedido itemPedido) {
		if (itemPedido.vlPctDesconto > 0 || LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			if (LavenderePdaConfig.isAcumulaComDescDoItem()) {
				itemPedido.vlPctDescPedido = itemPedido.pedido.vlPctDescItem;
				itemPedido.vlItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescPedido / 100)));
			} else {
				itemPedido.vlItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
			}
		} if (itemPedido.vlPctAcrescimo > 0) {
			itemPedido.vlItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 + (itemPedido.vlPctAcrescimo / 100)));
		}
		
	}

	public void desfazPrecoLiberadoSenha(ItemPedido itemPedido) {
		clearDadosDesconto(itemPedido);
		itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
	}

	public double getVlItemPedidoUnidadeElementarComTributos(ItemPedido itemPedido) throws SQLException {
		double value = getVlItemPedidoComTributos(itemPedido);
		double nuConversaoUnidadeMedida = itemPedido.getProduto().nuConversaoUnidadesMedida == 0 ? 1d : itemPedido.getProduto().nuConversaoUnidadesMedida;
		return  value > 0 ? value / nuConversaoUnidadeMedida : 0;
	}

	public double getVlItemPedidoComTributos(ItemPedido itemPedido) {
		return itemPedido.vlItemPedido + itemPedido.vlSt + itemPedido.vlIpiItem + itemPedido.vlFecop;
	}

	public void loadValorNeutroItemPedidoAud(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			Tributacao tributacao = itemPedido.getTributacaoItem();
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if (tributacao != null && itemTabelaPreco != null) {
				if (tributacao.vlIcmsRetido == 0d) {
					itemPedido.getItemPedidoAud().vlItemPedidoNeutro = itemTabelaPreco.vlBase / (1 - (tributacao.vlPctIcms + tributacao.vlPctPis + itemTabelaPreco.vlPctMargemRentabilidade) / 100);
				} else {
					itemPedido.getItemPedidoAud().vlItemPedidoNeutro = (itemTabelaPreco.vlBase * (1 - (tributacao.vlPctIcms + tributacao.vlPctPis) / 100) / (1 - (tributacao.vlPctIcms + tributacao.vlPctPis + itemTabelaPreco.vlPctMargemRentabilidade) / 100) + tributacao.vlIcms + tributacao.vlIcmsRetido) / (1 - (tributacao.vlPctPis / 100));
				}
			}
		}
	}
	
	public void loadValorVerbaEmpresa(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			Tributacao tributacao = itemPedido.getTributacaoItem();
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if (tributacao != null && itemTabelaPreco != null && itemTabelaPreco.isFlPromocao() && itemPedido.getItemPedidoAud().vlItemPedidoNeutro > itemPedido.vlBaseItemPedido) {
				if (tributacao.vlIcmsRetido != 0) {
					itemPedido.getItemPedidoAud().vlVerbaEmpresa = (itemPedido.getItemPedidoAud().vlItemPedidoNeutro - itemPedido.vlBaseItemPedido) * (1 - tributacao.vlPctPis / 100);
				} else {
					itemPedido.getItemPedidoAud().vlVerbaEmpresa = (itemPedido.getItemPedidoAud().vlItemPedidoNeutro - itemPedido.vlBaseItemPedido) * (1 - (tributacao.vlPctPis + tributacao.vlPctIcms) / 100);
				}
			} else {
				itemPedido.getItemPedidoAud().vlVerbaEmpresa = 0;
			}
		}
	}
	
	public double getVlPctRentabilidadeMinima(ItemPedido itemPedido) throws SQLException {
		double vlPctMinRentabilidade = 0d;
		if (LavenderePdaConfig.usaRentabilidadeMinimaItemPedido && itemPedido.getProduto().vlPctMinRentabilidade != 0) {
			vlPctMinRentabilidade = itemPedido.getProduto().vlPctMinRentabilidade;
			if (LavenderePdaConfig.vlPctToleranciaRentabilidadeMinima > 0) {
				vlPctMinRentabilidade = vlPctMinRentabilidade - (vlPctMinRentabilidade * LavenderePdaConfig.vlPctToleranciaRentabilidadeMinima / 100);
			}
		}
		return vlPctMinRentabilidade;
	}
	
	public void aplicaVlPctFrete(ItemPedido itemPedido) throws SQLException {
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		if ((tipoPedido == null || tipoPedido.isIgnoraCalculoFrete()) || !LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem || !LavenderePdaConfig.isUsaTipoFretePedido() || LavenderePdaConfig.ocultaTabelaPrecoPedido || LavenderePdaConfig.usaFreteManualPedido) return;
			TipoFreteTabPreco tipoFreteTabPreco = itemPedido.getTipoFreteTabPreco();
			if (tipoFreteTabPreco != null) {
				itemPedido.vlItemPedidoFrete  = 0;
				itemPedido.vlTotalItemPedidoFrete = 0;
				itemPedido.pedido.vlFrete = 0;
			 	if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao() && itemPedido.pedido.isGeraCreditoFrete() && tipoFreteTabPreco.vlPctCredito < 100 && tipoFreteTabPreco.vlPctCredito > 0) {
			 		itemPedido.vlCreditoFrete = getVlCreditoFrete(itemPedido, tipoFreteTabPreco);
			 	} else {
			 		itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 + (tipoFreteTabPreco.vlPctFrete / 100)));
			 		itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			 		itemPedido.vlCreditoFrete = 0d;
				}
			}
		}
		
	private double getVlCreditoFrete(ItemPedido itemPedido, TipoFreteTabPreco tipoFreteTabPreco) {
		return ValueUtil.round(itemPedido.vlBaseItemPedido * (tipoFreteTabPreco.vlPctCredito / 100));
	}
	
	public String getDadosProdutoRestritoNosItensPedidoFormatoJson(Vector itemPedidoList) {
		JSONArray jSonProdutoList = new JSONArray();
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			for (int i = 0; i < itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				JSONObject jSon = new JSONObject();
				jSon.put("cdEmpresa", itemPedido.cdEmpresa);
				jSon.put("cdRepresentante", itemPedido.cdRepresentante);
				jSon.put("cdProduto", itemPedido.cdProduto);
				jSonProdutoList.put(jSon);
			}
		}
		return jSonProdutoList.toString();
	}
	public String getDadosItensPedidoReservaEstoqueFormatoJson(Vector itemPedidoList) throws JSONException, SQLException {
		JSONArray jSonItemPedidoList = new JSONArray();
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			for (int i = 0; i < itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				JSONObject jSon = new JSONObject();
				if (itemPedido.getProduto().isIgnoraValidacao()) continue;
				jSon.put("cdEmpresa", itemPedido.cdEmpresa);
				jSon.put("flOrigemPedido", itemPedido.flOrigemPedido);
				jSon.put("cdRepresentante", itemPedido.cdRepresentante);
				jSon.put("nuPedido", itemPedido.nuPedido);
				jSon.put("cdProduto", itemPedido.cdProduto);
				jSon.put("flTipoItemPedido", itemPedido.flTipoItemPedido);
				jSon.put("nuSeqProduto", itemPedido.nuSeqProduto);
				jSon.put("qtReserva", Sql.getValue(getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico())));
				jSon.put("cdLocalEstoque", itemPedido.getCdLocalEstoque());
				if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
					jSon.put("dtSugestaoCliente", itemPedido.dtSugestaoCliente);
					jSon.put("cdCentroCusto", itemPedido.cdCentroCusto);
				}
				if (LavenderePdaConfig.isUsaGradeProduto1A4() && ValueUtil.isNotEmpty(itemPedido.itemPedidoGradeList)) {
					setaInformacoesGradeProduto(itemPedido, jSonItemPedidoList, jSon);
				} else {
					jSon.put("cdItemGrade1", ProdutoGrade.CD_ITEM_GRADE_PADRAO);
					jSon.put("cdItemGrade2", ProdutoGrade.CD_ITEM_GRADE_PADRAO);
					jSon.put("cdItemGrade3", ProdutoGrade.CD_ITEM_GRADE_PADRAO);
					jSon.put("qtReserva", Sql.getValue(getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico())));
					jSonItemPedidoList.put(jSon);
				}
			}
		}
		return jSonItemPedidoList.length() > 0 ? jSonItemPedidoList.toString() : ValueUtil.VALOR_NI;
	}
	
	private void setaInformacoesGradeProduto(ItemPedido itemPedido, JSONArray jsonItemPedidoList, JSONObject jsonItemPedido) throws SQLException {
		int size = itemPedido.itemPedidoGradeList.size();
		for (int i = 0; i < size; i++) {
			JSONObject json = new JSONObject(jsonItemPedido, new String[]{"cdEmpresa","flOrigemPedido","cdRepresentante", "nuPedido", "cdProduto", "flTipoItemPedido", "nuSeqProduto"});
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[i];
			json.put("qtReserva", getQtItemFisicoConversaoUnidade(itemPedido, itemPedidoGrade.qtItemFisico));
			json.put("cdItemGrade1", itemPedidoGrade.cdItemGrade1);
			json.put("cdItemGrade2", itemPedidoGrade.cdItemGrade2);
			json.put("cdItemGrade3", itemPedidoGrade.cdItemGrade3);
			jsonItemPedidoList.put(json);
		}
	}
	
	public Vector getItemPedidoProblemaReservaEstoqueList(String itemPedidoProblemaReservaEstoqueJsonList) {
		Vector itemPedidoProblemaReservaEstoqueList = new Vector();
		if (ValueUtil.isNotEmpty(itemPedidoProblemaReservaEstoqueJsonList)) {
			JSONArray jsonItemPedidoArray = new JSONArray(itemPedidoProblemaReservaEstoqueJsonList);
			if (jsonItemPedidoArray != null) {
				for (int i = 0; i < jsonItemPedidoArray.length(); i++) {
					JSONObject jsonItemPedido = (JSONObject) jsonItemPedidoArray.get(i);
					ItemPedido itemPedido = new ItemPedido();
					itemPedido.cdEmpresa = jsonItemPedido.getString("cdEmpresa");
					itemPedido.flOrigemPedido = jsonItemPedido.getString("flOrigemPedido");
					itemPedido.cdRepresentante = jsonItemPedido.getString("cdRepresentante");
					itemPedido.nuPedido = jsonItemPedido.getString("nuPedido");
					itemPedido.cdProduto = jsonItemPedido.getString("cdProduto");
					itemPedido.flTipoItemPedido = jsonItemPedido.getString("flTipoItemPedido");
					itemPedido.nuSeqProduto = jsonItemPedido.getInt("nuSeqProduto");
					itemPedidoProblemaReservaEstoqueList.addElement(itemPedido);
				}
				return itemPedidoProblemaReservaEstoqueList;
			}
		}
		return null;
	}
	
	public Vector getItemPedidoComReservaEstoqueCorrenteList(String itemPedidoComReservaEstoqueCorrenteList) {
		try {
			Vector itemPedidoProblemaReservaEstoqueList = new Vector();
			if (ValueUtil.isNotEmpty(itemPedidoComReservaEstoqueCorrenteList)) {
				JSONArray jsonItemPedidoArray = new JSONArray(itemPedidoComReservaEstoqueCorrenteList);
				if (jsonItemPedidoArray != null) {
					for (int i = 0; i < jsonItemPedidoArray.length(); i++) {
						JSONObject jsonItemPedido = (JSONObject) jsonItemPedidoArray.get(i);
						ItemPedido itemPedido = new ItemPedido();
						itemPedido.cdEmpresa = jsonItemPedido.getString("cdEmpresa");
						itemPedido.flOrigemPedido = jsonItemPedido.getString("flOrigemPedido");
						itemPedido.cdRepresentante = jsonItemPedido.getString("cdRepresentante");
						itemPedido.nuPedido = jsonItemPedido.getString("nuPedido");
						itemPedido.cdProduto = jsonItemPedido.getString("cdProduto");
						itemPedido.flTipoItemPedido = jsonItemPedido.getString("flTipoItemPedido");
						itemPedido.nuSeqProduto = jsonItemPedido.getInt("nuSeqProduto");
						itemPedido.reservado = jsonItemPedido.getBoolean("isReservado");
						itemPedido.consumiuPrev = jsonItemPedido.getBoolean("consumiuPrev");
						itemPedido.qtReservaPrev = jsonItemPedido.getDouble("qtReserva");
						itemPedido.qtEstoqueDisponivel = jsonItemPedido.getDouble("qtEstoque");
						if (jsonItemPedido.get("cdLocalEstoque") != JSONObject.NULL) {
							itemPedido.cdLocalEstoque = jsonItemPedido.getString("cdLocalEstoque");
						}
						if (jsonItemPedido.get("cdCentroCusto") != JSONObject.NULL) {
							itemPedido.cdCentroCusto = jsonItemPedido.getString("cdCentroCusto");
						}
						if (jsonItemPedido.get("dtEntrega") != JSONObject.NULL) {
							itemPedido.dtEntrega = DateUtil.toDate(String.valueOf(jsonItemPedido.get("dtEntrega")));
						}
						if (jsonItemPedido.get("dtSugestaoCliente") != JSONObject.NULL) {
							itemPedido.dtSugestaoCliente = DateUtil.toDate(String.valueOf(jsonItemPedido.get("dtSugestaoCliente")));
						}
						if (ValueUtil.isEmpty(itemPedido.dtEntrega) && itemPedido.consumiuPrev) {
							itemPedido.dtEntrega = DateUtil.getCurrentDate();
						}
						itemPedidoProblemaReservaEstoqueList.addElement(itemPedido);
					}
				}
			}
			return itemPedidoProblemaReservaEstoqueList;
		} catch (Throwable e) {
			return new Vector();
		}
	}

	public boolean isItemPedidoProblemaReservaEstoque(Vector itemPedidoProblemaReservaEstoqueList, ItemPedido itemPedido) {
		return ValueUtil.isNotEmpty(itemPedidoProblemaReservaEstoqueList) &&  itemPedidoProblemaReservaEstoqueList.indexOf(itemPedido) != -1;
	}
	
	public double getQtItemFisicoConversaoUnidade(ItemPedido itemPedido, double qtItemPedido) throws SQLException {
		return getQtItemFisicoConversaoUnidade(itemPedido, qtItemPedido, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro());
	}
	
	public double getQtItemFisicoConversaoUnidade(ItemPedido itemPedido, double qtItemPedido, boolean converteParaInteiro) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
			if (produtoUnidade.isMultiplica()) {
				qtItemPedido = qtItemPedido * nuConversaoUnidade;
			} else {
				qtItemPedido = qtItemPedido / nuConversaoUnidade;
			}
			if (converteParaInteiro) {
				qtItemPedido = ValueUtil.getIntegerValueTruncated(qtItemPedido);
			}
		}
		return qtItemPedido;
	}

	public void validaQtMaxVendaProduto(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.qtMaxVendaProdutoMes > 0 && ClienteService.getInstance().isClienteValidaQtMaxVendaProduto(SessionLavenderePda.getCliente())) {
			double qtTotalItemPedido = 0d;
			double qtTotalItemPedidoErp = 0d;
			Vector pedidoList = PedidoPdbxDao.getInstance().findAllInCurrentMonth(itemPedido, Pedido.TABLE_NAME_PEDIDO);
			int size = pedidoList.size();
			for (int i = 0; i < size; i++) {
				Pedido pedido = (Pedido) pedidoList.items[i];
				qtTotalItemPedido += ItemPedidoPdbxDao.getInstance().findQtProdutoVendido(pedido, ItemPedido.TABLE_NAME_ITEMPEDIDO);
			}
			//--
			Vector pedidoErpList = PedidoPdbxDao.getInstance().findAllInCurrentMonth(itemPedido, Pedido.TABLE_NAME_PEDIDOERP);
			int sizePedidoErp = pedidoErpList.size();
			for (int i = 0; i < sizePedidoErp; i++) {
				Pedido pedido = (Pedido) pedidoErpList.items[i];
				qtTotalItemPedidoErp += ItemPedidoPdbxDao.getInstance().findQtProdutoVendido(pedido, ItemPedido.TABLE_NAME_ITEMPEDIDOERP);
			}
			double qtTotalItem = LavenderePdaConfig.isConsisteConversaoUnidades() ? itemPedido.qtItemFaturamento : itemPedido.getQtItemFisico();
			double qtTotalItemOld = LavenderePdaConfig.isConsisteConversaoUnidades() ? itemPedido.oldQtItemFaturamento : itemPedido.getQtItemFisicoAtualizaEstoque();
			qtTotalItem += qtTotalItemPedido + qtTotalItemPedidoErp - qtTotalItemOld;   
			if (qtTotalItem > LavenderePdaConfig.qtMaxVendaProdutoMes) {
				throw new ValidationException(MessageUtil.getMessage(Messages.QTMAXVENDAPRODUTO_ERRO, new Object[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.qtMaxVendaProdutoMes), StringUtil.getStringValueToInterface(qtTotalItem - LavenderePdaConfig.qtMaxVendaProdutoMes)}));
			}
		}
	}
	
	public boolean validaItensPedidoOriginalBonificacaoTroca(String pedidoRel, Vector list) throws SQLException {
		Vector itemPedidoRelList = getItensFromPedidoRelacionado(pedidoRel);
		for (int j = 0; j < list.size(); j++) {
			ItemPedido item = (ItemPedido) list.items[j];
			boolean valido = false;
			if (item != null) {
				for (int i = 0; i < itemPedidoRelList.size(); i++) {
					ItemPedido itemPedidoRel = (ItemPedido) itemPedidoRelList.items[i];
					if (itemPedidoRel != null && item.cdProduto.equals(itemPedidoRel.cdProduto)) {
						valido = true;
						break;
					}
				} 
			}
			if (!valido) {
				return false;
			}
		}
		return true;
	}
	
	public void validaQtItemFisicoMaxDoItemPedidoBonificacaoTroca(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (!ValueUtil.getBooleanValue(itemPedido.flQuantidadeLiberada)) {
			validaQtItemFisicoMaxBonificacaoTroca(pedido, itemPedido);
		}
	}
	
	public void validaQtItemFisicoMaxDosItensPedidoBonificacaoTroca(Pedido pedido) throws SQLException {
		validaQtItemFisicoMaxBonificacaoTroca(pedido, null);
	}
	
	public void validaQtItemFisicoMaxBonificacaoTroca(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (pedido.isPedidoBonificacao() && PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedido)) {
			validaQtItemFisicoMaxBonifTrocaEmRelacaoOriginal(pedido, itemPedido, pedido.nuPedidoRelBonificacao, LavenderePdaConfig.getPercentualDoItemOuPedidoOriginalNaBonificacao());
		} else if (pedido.isPedidoTroca()) {
			validaQtItemFisicoMaxBonifTrocaEmRelacaoOriginal(pedido, itemPedido, pedido.nuPedidoRelTroca, LavenderePdaConfig.getPercentualDoItemOuPedidoOriginalNaTroca());
		}
	}

	private void validaQtItemFisicoMaxBonifTrocaEmRelacaoOriginal(Pedido pedido, ItemPedido itemPedido, String nuPedidoRelacionado, double percentualItemOriginal) throws SQLException {
		if (ValueUtil.isNotEmpty(nuPedidoRelacionado)) {
			if (LavenderePdaConfig.isUsaPercQuantidadeDosItensPedidoOriginalBonificacaoTroca()) {
				validaQtItemFisicoMaxBonifTrocaEmRelacaoPedidoOriginal(pedido, itemPedido, nuPedidoRelacionado, percentualItemOriginal);
			} else {
				ItemPedido itemOriginal = getItemPedidoFromPedidoRelacionado(itemPedido.cdProduto, nuPedidoRelacionado);
				validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal(pedido, itemOriginal, percentualItemOriginal, itemPedido.getQtItemFisico());
			}
		}
	}

	private void validaQtItemFisicoMaxBonifTrocaEmRelacaoPedidoOriginal(Pedido pedido, ItemPedido itemPedido, String nuPedidoRelacionado, double percentualItemOriginal) throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		itemPedidoFilter.nuPedido = nuPedidoRelacionado;
		itemPedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
		itemPedidoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		double qtTotalVendidoPedidoOriginal = sumByExample(itemPedidoFilter, "qtItemFisico");
		if (qtTotalVendidoPedidoOriginal == 0) {
			itemPedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			qtTotalVendidoPedidoOriginal = sumByExample(itemPedidoFilter, "qtItemFisico");
		}
		validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido(pedido, itemPedido, percentualItemOriginal, qtTotalVendidoPedidoOriginal);
	}

	private void validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido(Pedido pedido, ItemPedido itemPedido, double percentualItemOriginal, double qtTotalVendidoPedidoOriginal) throws SQLException {
		Vector list = pedido.isPedidoTroca() ? pedido.itemPedidoTrocaList : pedido.itemPedidoList; 
		int size = list.size();
		double sumQtItemFisico = 0;
		boolean itemJaInserido = false;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoFromList = ((ItemPedido) list.elementAt(i));
			if (ValueUtil.getBooleanValue(itemPedidoFromList.flQuantidadeLiberada)) {
				continue;
			}
			double qtItemFisicoFromList = itemPedidoFromList.getQtItemFisico();
			if (!itemJaInserido && itemPedido != null) {
				itemJaInserido = ValueUtil.valueEquals(itemPedido, itemPedidoFromList);
				qtItemFisicoFromList = itemJaInserido ? itemPedido.getQtItemFisico() : qtItemFisicoFromList;
			}
			sumQtItemFisico += qtItemFisicoFromList;
		}
		double sumQtItemFisicoOutrosItens = sumQtItemFisico;
		if (!itemJaInserido && itemPedido != null) {
			sumQtItemFisico += itemPedido.getQtItemFisico();
		}
		
		double limiteVenda = qtTotalVendidoPedidoOriginal * percentualItemOriginal / 100;
		if (sumQtItemFisico > limiteVenda) {
			if (pedido.onFechamentoPedido) {
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_QTITENS_ACIMA_PERMITIDO_BONIFICACAOTROCA_FECHARPEDIDO);
			}
			String qtItemLimiteString = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) limiteVenda) : StringUtil.getStringValueToInterface(limiteVenda);
			double qtRestante = limiteVenda - sumQtItemFisicoOutrosItens;
			if (qtRestante < 0) {
				qtRestante = itemJaInserido ? sumQtItemFisico : limiteVenda;
			} 
			String qtRestanteString = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) qtRestante) : StringUtil.getStringValueToInterface(qtRestante);
			String detalheLimite = itemJaInserido ? MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_TOTAL_BONIFICACAOTROCA, qtRestanteString) : MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_RESTANTE_BONIFICACAOTROCA, qtRestanteString);
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_ACIMA_PERMITIDO_BONIFICACAOTROCA, new String[] { qtItemLimiteString, detalheLimite}));
		}
	}

	private void validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal(Pedido pedido, ItemPedido itemPedidoOriginal, double percentualItemOriginal, double qtdVendida) throws SQLException {
		if (itemPedidoOriginal != null) {
			double limiteVenda = itemPedidoOriginal.getQtItemFisico() * percentualItemOriginal / 100;
			if (qtdVendida > limiteVenda) {
				if (pedido.onFechamentoPedido) {
					throw new ValidationException(Messages.ITEMPEDIDO_MSG_QTITEM_ACIMA_PERMITIDO_BONIFICACAOTROCA_FECHARPEDIDO);
				}
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITEM_ACIMA_PERMITIDO_BONIFICACAOTROCA, (int) limiteVenda));
			}
		}
	}
	
	public void atualizaEstoqueGrade(ItemPedido itemPedido, boolean insere, String flOrigemEstoque) throws SQLException {
		itemPedido.cdItemGrade2 =  null;
		itemPedido.cdItemGrade3 =  null;
		Vector itemPedidoGradeList = ItemPedidoGradeService.getInstance().getItemPedidoGradeByItemPedido(itemPedido);
		for (int i = 0; i < itemPedidoGradeList.size(); i++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedidoGradeList.items[i];
			itemPedido.cdItemGrade1 =  itemPedidoGrade.cdItemGrade1;
			itemPedido.cdItemGrade2 =  itemPedidoGrade.cdItemGrade2;
			itemPedido.cdItemGrade3 =  itemPedidoGrade.cdItemGrade3;
			itemPedido.setQtItemFisico(itemPedidoGrade.qtItemFisico);
			if (!itemPedido.pedido.isIgnoraControleEstoque(itemPedido)) {
				EstoqueService.getInstance().updateEstoqueInterno(itemPedido, getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()), insere, flOrigemEstoque);
			}
		}
	}
	
	public double roundUnidadeAlternativa(double value) {
		if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
			return value;
		}
		if (LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli && ItemPedido.ignoreRoundDecisaoCanalCli) {
			return value;
		}
		if (LavenderePdaConfig.isArredondaDescontosSequenciaisNoItemPedidoApenasNoFinal()) {
			return value;
		}
		return ValueUtil.round(value);
	}
	
	public void validaItensPorLimiteCredito(Pedido pedido, ItemPedido itemPedido, boolean aoFecharPedido) throws SQLException {
		if (!LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda || !LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao()) {
			return;
		}
		if (pedido.getTipoPedido() != null && pedido.getTipoPedido().isBonificacao() && (pedido.getTipoPedido().isFlTipoCreditoFrete() || pedido.getTipoPedido().isFlTipoCreditoCondicao())) {
			pedido.vlAtualPedido = 0d;
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
				if (!item.cdProduto.equals(itemPedido.cdProduto)) {
					pedido.vlAtualPedido += item.vlTotalItemPedido;
				}
			}
			Pedido pedidoRelVenda = PedidoService.getInstance().getPedidoRelBonificacao(pedido);
			if (pedidoRelVenda != null) {
				double vlTotalCredito = pedido.getTipoPedido().isFlTipoCreditoFrete() ? pedidoRelVenda.vlTotalCreditoFrete : pedidoRelVenda.vlTotalCreditoCondicao;
				if (itemPedido.vlTotalItemPedido + pedido.vlAtualPedido > vlTotalCredito) {
					double nuItens = (vlTotalCredito - pedido.vlAtualPedido) / itemPedido.vlItemPedido;
					int nuItensValidos = (nuItens - ((int) nuItens) >= 0.5) ? (int) (nuItens + 1) : (int) nuItens;
					if (itemPedido.getQtItemFisico() > nuItensValidos) {
						String msg = new StringBuilder(Messages.ITEMPEDIDO_VALOR_INDISPONIVEL_BONIFICACAO_CREDITO).append(!aoFecharPedido ? Messages.ITEMPEDIDO_VALOR_RESTANTE_DISPONIVEL_BONIFICACAO_CREDITO : "").toString();
						double vlDisponivel = pedido.vlCreditoDisponivelConsumo - pedido.vlAtualPedido < 0 ? 0 : pedido.vlCreditoDisponivelConsumo - pedido.vlAtualPedido;
						throw new ValidationException(MessageUtil.getMessage(msg,	StringUtil.getStringValueToInterface(vlDisponivel)));
					}
				} 
			}
			pedido.vlAtualPedido += itemPedido.vlTotalItemPedido;
		}
	}
	
	protected boolean isItemPedidoPromocional(ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			if (itemPedido.getTabelaPreco() != null && ValueUtil.getBooleanValue(itemPedido.getTabelaPreco().flPromocional)) {
				return true;
			}
			if (itemPedido.getItemTabelaPreco() != null && itemPedido.getItemTabelaPreco().isFlPromocao()) {
				return true;
			}
			if (ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco) && ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(itemPedido.cdProduto, itemPedido.cdTabelaPreco)) {
				return true;
			}
		}
		return false;
	}
	
	public void aplicaPrecoItemComValoresAdicionaisEmbutidos(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			double oldQtItemFisico = itemPedido.getQtItemFisico();
			itemPedido.setQtItemFisico(1);
			if (LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() && LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.isUsaTipoFretePedido() && itemPedido.getQtItemFisico() != 0 && !LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
				TipoFreteTabPrecoService.getInstance().calculateFreteItemPedidoByTipoFreteTabPrecoEPeso(itemPedido.getTipoFreteTabPreco(), itemPedido);
				double vlItemPedidoFrete = (tipoPedido == null || !tipoPedido.isIgnoraCalculoFrete()) ? itemPedido.vlItemPedidoFrete : 0d;
				itemPedido.vlBaseItemPedido += vlItemPedidoFrete;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlTotalItemPedido = itemPedido.vlBaseItemPedido;
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
					itemPedido.vlBaseInterpolacaoProduto = ValueUtil.round(itemPedido.vlBaseInterpolacaoProduto + vlItemPedidoFrete, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
				}
			}
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
				calculaSeguroEReaplicaImpostos(itemPedido, pedido);
				itemPedido.vlBaseItemPedido += itemPedido.vlSeguroItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				calculaImpostosParaSeguro(itemPedido, pedido);
			}
			itemPedido.setQtItemFisico(oldQtItemFisico);
		}
	}
	
	public double getVlItemPedidoMaxDescAcrescPermitido(ItemPedido itemPedido, double vlItemPedidoMaxPermitido, double vlPctMaxDescAcresItem, boolean isDesconto) throws SQLException {
		if (LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto()) {
			double vlItemPedido;
			if (isDesconto) {
				vlItemPedido = roundDescontoEmCascata(itemPedido.vlUnidadePadrao * (1 - (vlPctMaxDescAcresItem / 100)));
			} else {
				vlItemPedido = roundDescontoEmCascata(itemPedido.vlUnidadePadrao * (1 + (vlPctMaxDescAcresItem / 100)));
			}
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				vlItemPedidoMaxPermitido = aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, vlItemPedido);
			}
		}
		return vlItemPedidoMaxPermitido;
	}

	public void validaItemTroca(ItemPedido itemPedido) throws SQLException {
		itemPedido.qtItemVendidoTroca = getQtItemVendidoTroca(itemPedido);
		if (itemPedido.qtItemVendidoTroca == 0) {
			throw new ValidationException(Messages.ITEM_TROCA_NAO_PERMITIDA);
		}
	}

	public double getQtItemVendidoTroca(ItemPedido itemPedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = itemPedido.cdEmpresa;
		pedidoFilter.cdRepresentante = itemPedido.cdRepresentante;
		pedidoFilter.cdCliente = itemPedido.pedido.cdCliente;
		pedidoFilter.dtEmissaoInicialFilter = new Date();
		pedidoFilter.sortAtributte = "DTEMISSAO, HREMISSAO, HRFIMEMISSAO";
		pedidoFilter.sortAsc = "S,S,S";
		DateUtil.decDay(pedidoFilter.dtEmissaoInicialFilter, LavenderePdaConfig.qtdDiasMaximoTrocaItemPedido);
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedidoErp()) {
			pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
		}
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllByExample(pedidoFilter);
		int size = pedidoList.size();
		int qtProduto = 0;
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (pedido.isPedidoVenda() && !pedido.isPedidoAberto() && (!pedido.isPedidoFechado() || pedido.isPedidoIniciadoProcessoEnvio())) {
				ItemPedido itemPedidoFilter = getNewItemPedido(pedido);
				itemPedidoFilter.cdProduto = itemPedido.cdProduto;
				itemPedidoFilter.sortAtributte = "FLTIPOITEMPEDIDO";
				itemPedidoFilter.sortAsc = "S";
				Vector itemPedidoList = findAllByExampleUnique(itemPedidoFilter);
				int sizeItens = itemPedidoList.size();
				for (int j = 0; j < sizeItens; j++) {
					ItemPedido item = (ItemPedido) itemPedidoList.items[j];
					if (item.isItemTrocaRecolher()) {
						qtProduto -= item.getQtItemFisico();
						if (qtProduto < 0) {
							qtProduto = 0;
						}
					} else { 
						qtProduto += item.getQtItemFisico();
					}
				}
			}
		}
		return qtProduto > 0 ? qtProduto : 0;
	}

	private void validaInserirItemTroca(ItemPedido itemPedido) {
		if (itemPedido.getQtItemFisico() > itemPedido.qtItemVendidoTroca) {
			throw new ValidationException(Messages.ITEM_TROCA_QTD_NAO_PERMITIDA);
		}
		validaPctMaxTrocaRecolherPedido(itemPedido.pedido, itemPedido);
	}

	public void validaPctMaxTrocaRecolherPedido(Pedido pedido, ItemPedido itemPedido) {
		if (LavenderePdaConfig.pctMaxTrocaRecolherPedido > 0) {
			double vlTotalItemsTroca = getVlTotalItemsTroca(pedido, itemPedido);
			double vlMaxItemsTroca =  LavenderePdaConfig.pctMaxTrocaRecolherPedido / 100d * pedido.vlTotalPedido;
			if (vlTotalItemsTroca > vlMaxItemsTroca) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEM_TROCA_VL_NAO_PERMITIDA_PCT_MAX, new Object[]{StringUtil.getStringValueToInterface(vlTotalItemsTroca), StringUtil.getStringValueToInterface(vlMaxItemsTroca)}), ValueUtil.VALOR_NI);
			}
		}
	}

	public double getVlTotalItemsTroca(Pedido pedido, ItemPedido itemPedido) {
		int size = pedido.itemPedidoTrocaList.size();
		double vlTotalItemsTroca = 0;
		if (itemPedido != null && (itemPedido.isItemTroca() || itemPedido.isItemTrocaRecolher())) {
			vlTotalItemsTroca += itemPedido.vlTotalItemPedido;
		}
		for (int i = 0; i < size; i++) {
			ItemPedido itemTroca = (ItemPedido) pedido.itemPedidoTrocaList.items[i];
			if (!itemTroca.equals(itemPedido)) {
				vlTotalItemsTroca += itemTroca.vlTotalItemPedido;
			}
		}
		return vlTotalItemsTroca;
	}

	public double getVlTotalItemsBonificacao(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		double vlTotalItemsBonificacao = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.isItemBonificacao()) {
				vlTotalItemsBonificacao += itemPedido.vlTotalItemPedido;
			}
		}
		return vlTotalItemsBonificacao;
	}
	
	
	public int getQtdRealizadoProducaoProduto(ProducaoProd producaoProd, String dsPeriodo, String cdRepresentante) throws SQLException {
		return getQtdRealizadoProducaoProduto(producaoProd, OrigemPedido.FLORIGEMPEDIDO_PDA, dsPeriodo, cdRepresentante) + getQtdRealizadoProducaoProduto(producaoProd, OrigemPedido.FLORIGEMPEDIDO_ERP, dsPeriodo, cdRepresentante);
	}

	public int getQtdRealizadoProducaoProduto(ProducaoProd producaoProd, String flOrigemPedido, String dsPeriodo, String cdRepresentante) throws SQLException {
		int qtdRealizado = 0;
		ItemPedido itemPedidoFilter = new ItemPedidoBuilder(producaoProd.cdEmpresa, cdRepresentante, producaoProd.cdProduto, flOrigemPedido).build();
		Vector itemPedidoList = findAllByExample(itemPedidoFilter);
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			Pedido pedidoFilter = new PedidoBuilder(itemPedido).build();
			pedidoFilter = (Pedido) PedidoService.getInstance().findByRowKey(pedidoFilter.getRowKey());
			boolean liberaEstoque = StatusPedidoPdaService.getInstance().isFlLiberaEstoque(pedidoFilter.cdStatusPedido);
			if (pedidoFilter != null && ProducaoProdService.getInstance().isPeriodoVigente(pedidoFilter.dtEmissao, dsPeriodo) && (pedidoFilter.isPedidoTransmitido() || 
					  OrigemPedido.FLORIGEMPEDIDO_ERP.equals(flOrigemPedido)) && liberaEstoque) {
				qtdRealizado += itemPedido.getQtItemFisico();
			}
		}
		return qtdRealizado;
	}	
	
	private Pedido findPedidoByItemPedido(ItemPedido itemPedido) throws SQLException {
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = itemPedido.cdEmpresa;
		pedido.cdRepresentante = itemPedido.cdRepresentante;
		pedido.flOrigemPedido = itemPedido.flOrigemPedido;
		pedido.nuPedido = itemPedido.nuPedido;
		return (Pedido) PedidoService.getInstance().findByRowKey(pedido.getRowKey());
	}
	
	
	public void ajustaDescontoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (pedido != null && itemPedido != null) {
			if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && itemPedido.isFlPrecoLiberadoSenha()) {
				itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
				itemPedido.cdUsuarioLiberacao = null;
				double vlPctMaxDescontoItemPedido = ValueUtil.round(getVlPctMaxDescontoItemPedido(itemPedido));
				if (itemPedido.vlPctDesconto > vlPctMaxDescontoItemPedido) {
					itemPedido.vlPctDescontoNaoInseridoSugestaoPedido = itemPedido.vlPctDesconto;
					itemPedido.vlPctDesconto = vlPctMaxDescontoItemPedido;
					pedido.itemPedidoDivergenciaDescontoSugestaoPedList.addElement(itemPedido);
				}
			}
		}
	}

	public void atualizaFlRestritoItensPedido(Pedido pedido, String cdProdutoRestritoNaWeb) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		if (ValueUtil.isEmpty(cdProdutoRestritoNaWeb)) {
			atualizaRestricaoParaTodosItensPedido(itemPedidoList, size);
		} else {
			atualizaRestricaoItemPedido(cdProdutoRestritoNaWeb, itemPedidoList, size);
		}
	}

	private void atualizaRestricaoItemPedido(String cdProdutoRestritoNaWeb, Vector itemPedidoList, int size) throws SQLException {
		List<String> cdProdutoRestritoNaWebList = Arrays.asList(cdProdutoRestritoNaWeb.split(";"));
		for (String cdProdutoRestrito : cdProdutoRestritoNaWebList) {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (ValueUtil.valueEquals(itemPedido.cdProduto, cdProdutoRestrito)) {
					itemPedido.flRestrito = ValueUtil.VALOR_SIM;
					updateColumn(itemPedido.getRowKey(), Pedido.NMCOLUNA_FLRESTRITO, ValueUtil.VALOR_SIM, Types.VARCHAR);
					break;
				}
			}
		 	
		}
		atualizaRestricaoParaItensPedidoNaoRestrito(cdProdutoRestritoNaWebList, itemPedidoList, size);
	}

	private void atualizaRestricaoParaTodosItensPedido(Vector itemPedidoList, int size) throws SQLException {
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			itemPedido.flRestrito = ValueUtil.VALOR_NAO;
			updateColumn(itemPedido.getRowKey(), Pedido.NMCOLUNA_FLRESTRITO, ValueUtil.VALOR_NAO, Types.VARCHAR);
		}
	}
	

	public void recalculaVlBaseFlexItens(Pedido pedido, boolean atualizaNoBanco) throws SQLException {
		try {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				pedido.isRecalculandoVlBaseFlexItens = true;
				double vlTotalVerbaPedido = 0;
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i]; 
					VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
					if (atualizaNoBanco) {
						updateColumn(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_VLBASEFLEX, itemPedido.vlBaseFlex, Types.DECIMAL);
						updateColumn(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_VERBA_ITEM, itemPedido.vlVerbaItem, Types.DECIMAL);
					}
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && atualizaNoBanco) {
						updateColumn(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_VERBA_ITEM_POSITIVO, itemPedido.vlVerbaItemPositivo, Types.DECIMAL);
					}
					vlTotalVerbaPedido += itemPedido.vlVerbaItem;
				}
				pedido.vlVerbaPedido = vlTotalVerbaPedido;
				if (atualizaNoBanco) {
					PedidoService.getInstance().updateIndicesFinanceirosPedido(vlTotalVerbaPedido, pedido);
				}
			}
		} finally {
			pedido.isRecalculandoVlBaseFlexItens = false;
		}
	}

	public void recalculaVlItemPedido(Pedido pedido) throws SQLException {
		try {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				pedido.isRecalculandoVlItemPedidoItens = true;
				pedido.isRecalculandoVlBaseFlexItens = true;
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					resetAndCalculateItemPedido(pedido, itemPedido);
					update(itemPedido);
				}
				PedidoService.getInstance().update(pedido);
			}
		} finally {
			pedido.isRecalculandoVlItemPedidoItens = false;
		}
	}

	public double calculaDescVlBaseItemPorPesoPedido(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo, boolean isRecalculando) throws SQLException {
		DescQuantidadePesoService.getInstance().loadFaixasDescPedido(pedido);
		if (pedido.isPedidoVenda()) {
			if (ValueUtil.isNotEmpty(pedido.descQuantidadePesoList)) {
				if (!produtoDescQuantidadePesoBloqueado(pedido, itemPedido)) {
				double qtPesoPedido = pedido.qtPeso + (!isRecalculando ? itemPedido.qtPeso - itemPedido.oldQtPeso : 0);
				DescQuantidadePeso descQuantidadePeso = DescQuantidadePesoService.getInstance().getDescQuantidadePesoPedido(pedido.descQuantidadePesoList, qtPesoPedido);
				pedido.descQuantidadePeso = descQuantidadePeso;
					if (pedido.descQuantidadePeso != null) {
						vlBaseCalculo = vlBaseCalculo - (descQuantidadePeso.vlPctDesconto / 100 * vlBaseCalculo);
						pedido.descQuantidadePesoOld = pedido.descQuantidadePeso;
					} else {
						itemPedido.vlPctFaixaDescQtdPeso = 0;
						itemPedido.vlPctDesconto = 0;
						itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
					}
				}
			}
		}
		return vlBaseCalculo;
	}
	
	public double calculaDescVlBaseItemPorPesoItemPedidoTabelaPreco(Pedido pedido, ItemPedido itemPedido, double vlBaseCalculo) throws SQLException {
		if (pedido.isPedidoVenda()) {
			carregarFaixasDesconto(itemPedido,pedido);
			DescQuantidadePeso descQuantidadePeso = DescQuantidadePesoService.getInstance().getDescQuantidadePesoPedido(pedido.descQuantidadePesoList, getAtualPesoPedidoTabelaPreco(pedido, itemPedido));
			pedido.descQuantidadePeso = descQuantidadePeso;
			if (pedido.descQuantidadePeso != null) {
				vlBaseCalculo = vlBaseCalculo - (descQuantidadePeso.vlPctDesconto / 100 * vlBaseCalculo);
			} else {
				itemPedido.vlPctFaixaDescQtdPeso = 0;
				itemPedido.vlPctDesconto = 0;
			}
		}
		return vlBaseCalculo;
	}
	
	public double getAtualPesoPedidoTabelaPreco(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		double pesoTabelaPreco = itemPedido.qtPeso;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itPed = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!produtoDescQuantidadePesoBloqueado(itPed.pedido, itPed)) {
				if (!ValueUtil.valueEquals(itPed.cdProduto, itemPedido.cdProduto)) {
					if (ValueUtil.valueEquals(itPed.cdTabelaPreco, itemPedido.cdTabelaPreco)) {
						pesoTabelaPreco += itPed.qtPeso;
					}
				}
			}
		}
		return pesoTabelaPreco;
	}
	
	public void aplicaDescontoComissaoPadrao(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		boolean showDescComiGrupo = false;
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
			DescComiFaixa descComiGrupo = DescComiFaixaService.getInstance().findDescComiGrupoPadrao(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdRepresentante, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.getProduto());
			if (descComiGrupo != null) {
				showDescComiGrupo = true;
			}
			DescComiFaixaService.getInstance().aplicaDescComiGrupoNoItemPedido(itemPedido, descComiGrupo, false);
		}
		if ((LavenderePdaConfig.usaDescontoComissaoPorProduto && !showDescComiGrupo)) {
			DescComiFaixa descComiProd = DescComiFaixaService.getInstance().findDescComiProdPadrao(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdRepresentante, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.getProduto().cdProduto);
			DescComiFaixaService.getInstance().aplicaDescComiProdNoItemPedido(itemPedido, descComiProd, false);
		}
		//--
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			if ((itemPedido.descComissaoGrupo == null)  && (itemPedido.descComissaoProd == null)) {
				itemPedido.setProduto(null);
				throw new ValidationException(Messages.DESCCOMIPROD_SEM_DESC);
			}
		}
	}
	
	private void atualizaRestricaoParaItensPedidoNaoRestrito(List<String> cdProdutoRestritoNaWebList, Vector itemPedidoList, int size) throws SQLException {
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			boolean achou = false;
			for (String cdProdutoRestrito : cdProdutoRestritoNaWebList) {
				if (ValueUtil.valueEquals(cdProdutoRestrito, itemPedido.cdProduto)) {
					achou = true;
					break;
				}
			}
			if (!achou) {
				itemPedido.flRestrito = ValueUtil.VALOR_NAO;
				updateColumn(itemPedido.getRowKey(), Pedido.NMCOLUNA_FLRESTRITO, ValueUtil.VALOR_NAO, Types.VARCHAR);
			}
		}
	}
	
	public void aplicaMaiorDescontoCascataItem(ItemPedido itemPedido) throws SQLException {
		itemPedido.usaDescontoCascata = true;
		double maiorDesconto = 0;
		double maxDescGrupo = LavenderePdaConfig.isUsaDescontoQtdPorGrupo() ? DescontoGrupoService.getInstance().getPctMaxDescontoPorGrupoEQuantidade(itemPedido.pedido, itemPedido) : 0;
		maiorDesconto = maxDescGrupo;
		itemPedido.tipoDesc = maiorDesconto > 0 ? 1 : 0;
		double vlIndiceFinanceiro = 0;
		GrupoProduto1 grupoProduto1 = GrupoProduto1Service.getInstance().findGrupoProduto1ByItemPedido(itemPedido);
		if ((grupoProduto1 == null || grupoProduto1.isComparaDesc()) && !itemPedido.pedido.getTipoPedido().isFlIgnorarIndiceFinanCli()) {
			vlIndiceFinanceiro = getIndiceFinanceiroCliente(itemPedido.pedido);
			vlIndiceFinanceiro = (1 - vlIndiceFinanceiro) * 100;
			if (vlIndiceFinanceiro < 100 && maiorDesconto < vlIndiceFinanceiro) {
				maiorDesconto = vlIndiceFinanceiro;
				itemPedido.tipoDesc = 2;
			}
		}
		double vlPctDescPromo = (LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco || LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco) ? itemPedido.getItemTabelaPreco().vlPctDescPromocional : 0;
		if (maiorDesconto < vlPctDescPromo) {
			maiorDesconto = vlPctDescPromo;
			itemPedido.tipoDesc = 3;
		}
		if (itemPedido.vlPctDesconto != maiorDesconto) {
			itemPedido.pedido.cdGrupoRecalc = LavenderePdaConfig.usaGrupoDescPromocionalNoDescQtdPorGrupo ? itemPedido.getProduto().cdGrupoDescProd : itemPedido.getProduto().cdGrupoProduto1;
		}
		itemPedido.vlPctDesconto = maiorDesconto;
	}
	
	public void atualizaPctDescontoCascataItens(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			String cdGrupo = LavenderePdaConfig.usaGrupoDescPromocionalNoDescQtdPorGrupo ? itemPedido.getProduto().cdGrupoDescProd : itemPedido.getProduto().cdGrupoProduto1;
			if (pedido.cdGrupoRecalc.equals(cdGrupo)) {
				PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
				getCrudDao().update(itemPedido);
			}
		}
	}
	
	public ItemPedido createItemPedidoByPedidoConsignacao(PedidoConsignacao pedidoConsignacao) {
		return new ItemPedidoBuilder(pedidoConsignacao).build();
	}

	public void validaItemsTrocaConsignacao(Pedido pedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(pedido);
		itemPedidoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC;
		if (countItensPedido(itemPedidoFilter) > 0) {
			throw new ValidationException(Messages.CONSIGNACAO_ITEMS_TROCA_NAO_PERMITIDOS);
		}
	}

	public void aplicaIndiceFinanceiroGrupoProdTabPreco(ItemPedido itemPedido) throws SQLException{
		if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
			double vlIndiceFinanceiro = 1;
			IndiceGrupoProd indiceGrupoProd = IndiceGrupoProdService.getInstance().escolheIndicePorItemPedido(itemPedido);
			if (indiceGrupoProd != null) {
				vlIndiceFinanceiro = indiceGrupoProd.vlIndiceFinanceiro;
				itemPedido.vlIndiceGrupoProd = vlIndiceFinanceiro;
			}
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao  = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
		}
	}
	public double getUltimoPrecoPraticadoComIpi(ItemPedido itemPedido) throws SQLException {
		double ultimoValor = getVlItemPedidoUltimoPedidoCliente(itemPedido);
		ItemPedido item = itemPedido.itemPedidoUltimoPedidoCliente;
		if (item != null) {
			return ultimoValor + (item.getQtItemFisico() > 0 ? (item.vlTotalIpiItem / item.getQtItemFisico()) : item.vlTotalIpiItem);
		}
		return ultimoValor;
	}
	
	public boolean isVlItemPedidoMenorVlMin(ItemPedido itemPedido) throws SQLException {
		return itemPedido.oldVlBruto != itemPedido.vlItemPedido && itemPedido.vlItemPedido < itemPedido.getItemTabelaPreco().vlMinItemPedido;
	}

	public void aplicaDescontoPorCredito(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		PedidoService.getInstance().aplicaDescontoPorCredito(pedido, itemPedido);
	}
	
	public Vector findProdutoAbaixoMin(Pedido pedido) throws SQLException{
		return ItemPedidoPdbxDao.getInstance().findProdutoAbaixoMin(pedido);
	}
	
	public Produto getProdutoAbaixoMin(Pedido pedido) throws SQLException {
		TabelaPreco tabPreco = pedido.getTabelaPreco();
		if(tabPreco == null) return null;
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
			if (getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()) < tabPreco.qtMinProduto) {
				return itemPedido.getProduto();
			}
		}
		return null;
	}
	
	private void validateQtItensGrade2(Vector itemGradePedidoList, ItemPedido itemPedido, String cdGrupoProduto1) throws SQLException {
		validateQtItensGrade(itemGradePedidoList, itemPedido, cdGrupoProduto1, true);
	}
	
	private void validateQtItensGrade1(Vector itemGradePedidoList, ItemPedido itemPedido, String cdGrupoProduto1) throws SQLException {
		validateQtItensGrade(itemGradePedidoList, itemPedido, cdGrupoProduto1, false);
	}
	
	
	private void validateQtItensGrade(Vector itemGradePedidoList, ItemPedido itemPedido, String cdGrupoProduto1, boolean grade2) throws SQLException {
		TabelaPreco tabPreco = itemPedido.pedido.getTabelaPreco();
		TabPrecoGrupoProd tabPrecoGrupoProd = TabPrecoGrupoProdService.getInstance().findByItemPedidoAndCdGrupo(itemPedido, cdGrupoProduto1);
		int size = itemGradePedidoList.size();
		Map<String, ItemGrade> hashQtdItensGrade = new HashMap<>(size);
		Vector itensPedidoGrade = grade2 ? null : new Vector(size);
		for (int j = 0; j < size; j++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemGradePedidoList.items[j];
			if (!grade2) {
				itensPedidoGrade.addElement(itemPedidoGrade);
			}
			String cdItemPedidoGrade = grade2 ? itemPedidoGrade.cdItemGrade2 : itemPedidoGrade.cdItemGrade1;
			if (ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO.equals(cdItemPedidoGrade)) {
				continue;
			}
			ItemGrade itemGrade = hashQtdItensGrade.get(cdItemPedidoGrade);
			if (itemGrade != null && itemGrade.qtItemGrade > 0) {
				hashQtdItensGrade.remove(cdItemPedidoGrade);
				itemGrade.qtItemGrade += getQtItemFisicoConversaoUnidade(itemPedido, itemPedidoGrade.qtItemFisico);
				hashQtdItensGrade.put(cdItemPedidoGrade, itemGrade);
			} else {
				itemGrade = grade2 ? itemPedidoGrade.itemGrade2 : itemPedidoGrade.itemGrade1;
				if (itemGrade == null) {
					itemGrade = new ItemGrade();
				}
				itemGrade.qtItemGrade = getQtItemFisicoConversaoUnidade(itemPedido, itemPedidoGrade.qtItemFisico);
				hashQtdItensGrade.put(cdItemPedidoGrade, itemGrade);
			}
		}
		for (ItemGrade item : hashQtdItensGrade.values()) {
			double qtMinGrade;
			if (tabPrecoGrupoProd != null) {
				qtMinGrade = grade2 ? tabPrecoGrupoProd.qtMinGrade2 : tabPrecoGrupoProd.qtMinGrade1;
				if (item.qtItemGrade < qtMinGrade) {
					if (ValueUtil.isNotEmpty(item.cdTipoItemGrade) && ValueUtil.isNotEmpty(item.cdItemGrade)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_GRUPO_ABAIXO_MIN_GRADE2, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), TipoItemGradeService.getInstance().getDsTipoItemGrade(item.cdTipoItemGrade), ItemGradeService.getInstance().getDsItemGrade(item.cdTipoItemGrade, item.cdItemGrade), StringUtil.getStringValueToInterface(item.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
					} else {
						throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_ABAIXO_MIN_GRADE, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), (grade2 ? ItemGrade.GRADE2 : ItemGrade.GRADE1), StringUtil.getStringValueToInterface(item.qtItemGrade)}));
					}
				}
			}
			if(tabPreco == null) return;
			qtMinGrade = grade2 ? tabPreco.qtMinGrade2 : tabPreco.qtMinGrade1;
			if (item.qtItemGrade < qtMinGrade && LavenderePdaConfig.usaQtMinProdTabPreco()) {
				if (ValueUtil.isNotEmpty(item.cdTipoItemGrade) && ValueUtil.isNotEmpty(item.cdItemGrade)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_ABAIXO_MIN_GRADE2, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), TipoItemGradeService.getInstance().getDsTipoItemGrade(item.cdTipoItemGrade), ItemGradeService.getInstance().getDsItemGrade(item.cdTipoItemGrade, item.cdItemGrade), tabPreco.dsTabelaPreco, StringUtil.getStringValueToInterface(item.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
				} else {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_ABAIXO_MIN_GRADE, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), (grade2 ? ItemGrade.GRADE2 : ItemGrade.GRADE1), StringUtil.getStringValueToInterface(item.qtItemGrade)}));
				}
			}
		}
	}
	
	private void validateQtMinTabPreco(ItemPedido itemPedido, boolean naoIgnoraValidacoes) throws SQLException {
		if (naoIgnoraValidacoes) {
			TabelaPreco tabPreco = itemPedido.getTabelaPreco();
			double qtItem = getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			if (tabPreco != null && qtItem < tabPreco.qtMinProduto) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_PRODUTO_ABAIXO_MIN, new Object[] {StringUtil.getStringValueToInterface(tabPreco.qtMinProduto, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), tabPreco.dsTabelaPreco, StringUtil.getStringValueToInterface(qtItem, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
			}
		}
	}
	
	private void validateQtMinVendaDescPromocional(ItemPedido itemPedido) throws SQLException {
		DescPromocional descPromocional = itemPedido.getDescPromocional();
		double qtItem = getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
		if (qtItem != 0 && descPromocional != null && descPromocional.qtMinVenda != 0 && ValueUtil.VALOR_SIM.equals(descPromocional.flValidaQtMinDescPromocional) && descPromocional.qtMinVenda > qtItem) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_DESCPROMOCIONAL_ABAIXO_MIN_VENDA, new Object[] {StringUtil.getStringValueToInterface(descPromocional.qtMinVenda, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), StringUtil.getStringValueToInterface(qtItem, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
		}
	}
	
	public String getDsTabelaPreco(ItemPedido itemPedido) throws SQLException {
		String dsTabelaPreco = null;
		if (itemPedido.getItemTabelaPreco() != null && itemPedido.getItemTabelaPreco().getTabelaPreco() != null) {
			dsTabelaPreco = itemPedido.getTabelaPreco().toString();
		}
		return StringUtil.getStringValue(dsTabelaPreco);
	}
	
	public void validateMinGradesAndQtMin(ItemPedido itemPedido) throws SQLException {
		boolean naoIgnoraValidacoes = !(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && itemPedido.pedido.isPendente());
		int precision = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface;
		if (LavenderePdaConfig.usaQtMinProdTabPrecoEClasse()) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				validateQtNiveisGrade(itemPedido);
			}
			if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional && !itemPedido.gradeAbertaValidated) {
				validateQtMinVendaDescPromocional(itemPedido);
			}
			if (naoIgnoraValidacoes) {
				TabPrecoClasseProd tabPrecoClasseProd = TabPrecoClasseProdService.getInstance().findByItemPedidoAndCdClasse(itemPedido, itemPedido.getProduto().cdClasse);
				double qtItem = itemPedido.getQtItemFisico();
				if (tabPrecoClasseProd != null && tabPrecoClasseProd.qtMinProduto > qtItem) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_PRODUTO_ABAIXO_MIN_CLASSE, new Object[] {StringUtil.getStringValueToInterface(tabPrecoClasseProd.qtMinProduto, precision), StringUtil.getStringValueToInterface(qtItem, precision)}));
				}
			}
		} else if (LavenderePdaConfig.usaQtMinProdTabPrecoEGrupo()) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				validateQtNiveisGrade(itemPedido);
			}
			if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional && !itemPedido.gradeAbertaValidated) {
				validateQtMinVendaDescPromocional(itemPedido);
			}
			if (naoIgnoraValidacoes) {
				TabPrecoGrupoProd tabPrecoGrupoProd = TabPrecoGrupoProdService.getInstance().findByItemPedidoAndCdGrupo(itemPedido, itemPedido.getProduto().cdGrupoProduto1);
				double qtItem = getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
				if (tabPrecoGrupoProd != null && tabPrecoGrupoProd.qtMinProduto > qtItem) {
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_PRODUTO_ABAIXO_MIN_GRUPO, new Object[] {StringUtil.getStringValueToInterface(tabPrecoGrupoProd.qtMinProduto, precision), StringUtil.getStringValueToInterface(qtItem, precision)}));
				}
			}
			validateQtMinTabPreco(itemPedido, naoIgnoraValidacoes);
		} else if (LavenderePdaConfig.usaQtMinProdTabPreco()) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				validateQtNiveisGrade(itemPedido);
			}
			if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional && !itemPedido.gradeAbertaValidated) {
				validateQtMinVendaDescPromocional(itemPedido);
			}
			validateQtMinTabPreco(itemPedido, naoIgnoraValidacoes);
		} else if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional && !itemPedido.gradeAbertaValidated) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				validateQtNiveisGrade(itemPedido);
			}
			validateQtMinVendaDescPromocional(itemPedido);
		}
	}
	
	private void validateGradeAberta(ItemPedido itemPedido, boolean grade2) throws SQLException {
		Vector itemGradePedidoList = itemPedido.itemPedidoGradeList;
		int size = itemGradePedidoList.size();
		Map<String, ItemGrade> hashQtdItensGrade = new HashMap<>(size);
		Map<String, Vector> itensPedidoGrade = new HashMap<>(size);
		for (int j = 0; j < size; j++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemGradePedidoList.items[j];
			Vector list = itensPedidoGrade.get(itemPedidoGrade.cdItemGrade1);
			if (list == null) {
				list = new Vector(size);
			}
			list.addElement(itemPedidoGrade);
			itensPedidoGrade.put(itemPedidoGrade.cdItemGrade1, list);
			String cdItemPedidoGrade = grade2 ? itemPedidoGrade.cdItemGrade2 : itemPedidoGrade.cdItemGrade1;
			if (ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO.equals(cdItemPedidoGrade)) {
				continue;
			}
			ItemGrade itemGrade = hashQtdItensGrade.get(cdItemPedidoGrade);
			if (itemGrade != null && itemGrade.qtItemGrade > 0) {
				itemGrade.qtItemGrade += getQtItemFisicoConversaoUnidade(itemPedido, itemPedidoGrade.qtItemFisico);
				if (itemGrade.cdItemGrade1ForValidation.indexOf(itemPedidoGrade.cdItemGrade1) == -1) {
					itemGrade.cdItemGrade1ForValidation.add(itemPedidoGrade.cdItemGrade1);
				}
				hashQtdItensGrade.put(cdItemPedidoGrade, itemGrade);
			} else {
				itemGrade = grade2 ? itemPedidoGrade.itemGrade2 : itemPedidoGrade.itemGrade1;
				if (itemGrade == null) {
					itemGrade = new ItemGrade();
				}
				itemGrade.qtItemGrade = getQtItemFisicoConversaoUnidade(itemPedido, itemPedidoGrade.qtItemFisico);
				if (itemGrade.cdItemGrade1ForValidation == null || itemGrade.cdItemGrade1ForValidation.size() > 0) {
					itemGrade.cdItemGrade1ForValidation = new ArrayList<>();
				}
				if (itemGrade.cdItemGrade1ForValidation.indexOf(itemPedidoGrade.cdItemGrade1) == -1) {
					itemGrade.cdItemGrade1ForValidation.add(itemPedidoGrade.cdItemGrade1);
				}
				hashQtdItensGrade.put(cdItemPedidoGrade, itemGrade);
			}
		}
		for (Map.Entry<String, ItemGrade> item : hashQtdItensGrade.entrySet()) {
			double qtMinGrade;
			ItemGrade itemGrade = item.getValue();
			if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional && itemPedido.getDescPromocional() != null) {
				DescPromocional descPromocional = itemPedido.getDescPromocional();
				qtMinGrade = grade2 ? descPromocional.qtMinGrade2 : descPromocional.qtMinGrade1;
				if (ValueUtil.VALOR_SIM.equals(descPromocional.flValidaQtMinDescPromocional) && qtMinGrade > itemGrade.qtItemGrade) {
					if (grade2) {
						ValidationException e = new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_DESCPROMOCIONAL_ABAIXO_MIN_GRADE2, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGrade.cdTipoItemGrade), ItemGradeService.getInstance().getDsItemGrade(itemGrade.cdTipoItemGrade, itemGrade.cdItemGrade), StringUtil.getStringValueToInterface(itemGrade.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
						if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional && ValueUtil.isNotEmpty(itemPedido.descPromocionalGradeList) && itemPedido.descPromocional != null) {
							for (String cdItemGrade1 : itemGrade.cdItemGrade1ForValidation) {
								DescPromocionalGradeService.getInstance().validateQtItemGradeMultiplos(itensPedidoGrade.get(cdItemGrade1), itemPedido.descPromocionalGradeList, itemPedido.itemGradeList, itemPedido);
							}
						} else {
							throw e;
						}
					} else {
						ValidationException e = new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_DESCPROMOCIONAL_ABAIXO_MIN_GRADE2, new Object[] {StringUtil.getStringValueToInterface(qtMinGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGrade.cdTipoItemGrade), ItemGradeService.getInstance().getDsItemGrade(itemGrade.cdTipoItemGrade, itemGrade.cdItemGrade), StringUtil.getStringValueToInterface(itemGrade.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface)}));
						if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional && ValueUtil.isNotEmpty(itemPedido.descPromocionalGradeList) && itemPedido.descPromocional != null) {
							DescPromocionalGradeService.getInstance().validateQtItemGradeMultiplos(itensPedidoGrade.get(item.getKey()), itemPedido.descPromocionalGradeList, itemPedido.itemGradeList, itemPedido);
						} else {
							throw e;
						}
					}
				}
			}
		}
	}

	public boolean avisaControleItemPedido(ItemPedido itemPedido) throws SQLException {
		Vector itemGradePedidoList = itemPedido.itemPedidoGradeList;
		int size = itemGradePedidoList.size();
		Vector listProdutosErros = new Vector();
		String[] limiteAviso = LavenderePdaConfig.getLimitesAvisoControleItemPedido();
		if (limiteAviso != null && limiteAviso.length > 0 && !ValueUtil.isEmpty(limiteAviso[3])) {
			ProdutoErro produtoErro = null;
			for (int i = 0; i < size; i++) {
				ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemGradePedidoList.items[i];
				if (itemPedidoGrade.qtItemFisico > ValueUtil.getDoubleValue(limiteAviso[3])) {
					produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.ITEMPEDIDO_EXCEDEU_LIMITE_QTDE, itemPedidoGrade.itemGrade1.toString(), itemPedidoGrade.itemGrade2.toString(), LavenderePdaConfig.isGradeProdutoModoLista() ? itemPedidoGrade.itemGrade3.toString() : null);
					produtoErro.dsVlLimite = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITE_QUANT, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(limiteAviso[3])));
					produtoErro.dsVlAtual = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_QUANT_ATUAL, StringUtil.getStringValueToInterface(itemPedidoGrade.qtItemFisico));
					produtoErro.isItemGrade = true;
					listProdutosErros.addElement(produtoErro);
				}
			}
			if (listProdutosErros.size() > 0) {
				ListLimiteAvisoWindow avisoLimiteWindow = new ListLimiteAvisoWindow(listProdutosErros);
				avisoLimiteWindow.popup();
				return !avisoLimiteWindow.continuar;
			}
		}
		return false;
	}
	
	public boolean avisoQuantidadeMaximaAtingidaItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.itemPedidoGradeList.isEmpty()) return false;
		
		Vector itemGradePedidoList = itemPedido.itemPedidoGradeList;
		int size = itemGradePedidoList.size();
		Vector listProdutosErros = new Vector();
		double limiteAviso = itemPedido.getItemTabelaPreco().qtMaxVenda;
		if ((limiteAviso > 0)) {
			ProdutoErro produtoErro = null;
			for (int i = 0; i < size; i++) {
				ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemGradePedidoList.items[i];
				if ((int) itemPedidoGrade.qtItemFisico > limiteAviso) {
					produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.ITEMPEDIDO_MSG_QTD_MAX_PERMITIDO_POR_ITEM_PEDIDO, itemPedidoGrade.itemGrade1.toString(), itemPedidoGrade.itemGrade2 != null ? itemPedidoGrade.itemGrade2.toString() : null, LavenderePdaConfig.isGradeProdutoModoLista() ? itemPedidoGrade.itemGrade3.toString() : null);
					produtoErro.dsVlLimite = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITE_QUANT, StringUtil.getStringValueToInterface(limiteAviso));
					produtoErro.dsVlAtual = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_QUANT_ATUAL, StringUtil.getStringValueToInterface(itemPedidoGrade.qtItemFisico));
					produtoErro.isItemGrade = true;
					listProdutosErros.addElement(produtoErro);
				}
			}
			if (listProdutosErros.size() > 0) {
				ListLimiteAvisoWindow avisoLimiteWindow = new ListLimiteAvisoWindow(listProdutosErros,true);
				avisoLimiteWindow.popup();
				return true;
			}
		}
		return false;
	}
	
	public Vector findItensPedidoByGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, Vector listItensPedido) throws SQLException {
    	Vector result = new Vector();
    	int size = listItensPedido.size();
		Vector listCdProduto = ProdutoPdbxDao.getInstance().findAllCdProdutoWhereGrupoProduto(cdGrupoProduto1,  cdGrupoProduto2, cdGrupoProduto3, cdGrupoProduto4);
		ProdutoBase produto;
		ItemPedido itemPedido;
		if (ValueUtil.isNotEmpty(listCdProduto)) {
			for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido) listItensPedido.items[i];
				produto = itemPedido.getProduto();
				if (listCdProduto.indexOf(produto.cdProduto) != -1) {
					result.addElement(itemPedido);
				}
			}
		}
    	return result;
    }
	
	public void controlaDescontosMudancaUnidade(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getQtItemFisico() != 0 || itemPedido.qtItemFaturamento != 0) {
			if (itemPedido.permiteAplicarDesconto() && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
				itemPedido.getItemTabelaPreco().descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto), itemPedido);
				DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
			}
			if (itemPedido.permiteAplicarDesconto() && DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) {
				itemPedido.descPromocional =  null;
				itemPedido.descPromocionalComQtdList = null;
				itemPedido.loadDescPromocional(false);
				DescPromocionalService.getInstance().loadMaiorFaixaDescPromocionalPorQuantidadeItemPedido(itemPedido);
			}
		}
	}
	
	public boolean isUnidadeAtualComDivergencia(final ItemPedido itemPedidoAtual, final ProdutoUnidade produtoUnidadeNova) throws SQLException {
		return (produtoUnidadeNova != null && ValueUtil.isNotEmpty(produtoUnidadeNova.cdUnidade))
			&& (!ValueUtil.valueEquals(produtoUnidadeNova.nuConversaoUnidade, itemPedidoAtual.nuConversaoUnidadePu)
			|| !ValueUtil.valueEquals(produtoUnidadeNova.vlIndiceFinanceiro, itemPedidoAtual.vlIndiceFinanceiroPu)
			|| !ValueUtil.valueEquals(produtoUnidadeNova.flDivideMultiplica, itemPedidoAtual.flDivideMultiplicaPu));
	}
	
	public void changeUnidadeAlternativa(ItemPedido itemPedido, String cdUnidadeAlternativa) throws SQLException {
		itemPedido.cdUnidade = cdUnidadeAlternativa;
		itemPedido.vlBaseCalculoDescPromocional = 0;
		itemPedido.oldQtItemFisicoDescPromocionalQtd = 0;
		PedidoService.getInstance().resetDadosItemPedido(itemPedido.pedido, itemPedido);
		if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) {
			itemPedido.flTipoEdicao = itemPedido.getFltipoEdicao();
		}
		controlaDescontosMudancaUnidade(itemPedido);
		PedidoService.getInstance().calculateItemPedido(itemPedido.pedido, itemPedido, false);
	}
	
	private void validateItemDescCascataCategoria(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
		Pedido pedClone = (Pedido) itemPedido.pedido.clone();
		pedClone.itemPedidoList = new Vector(itemPedido.pedido.itemPedidoList.size());
		pedClone.itemPedidoList.addElementsNotNull(itemPedido.pedido.itemPedidoList.items);
		pedClone.itemPedidoList.removeElement(itemPedido);
			pedClone.itemPedidoList.addElement(itemPedido.clone());
		Categoria categoria = pedClone.getCliente().getCategoria();
		if (categoria != null) {
			double vlPedido = pedClone.vlTotalPedido;
			if (vlPedido >= categoria.vlMinPedidoEspecial || vlPedido >= categoria.vlMinPedidoAtacado) {
				PedidoService.getInstance().calculate(pedClone);
				vlPedido = pedClone.vlTotalPedido;
				if (vlPedido < categoria.vlMinPedidoEspecial && itemPedido.pedido.vlPctDesc2 > 0 && CategoriaService.getInstance().isCategoriaDisponivelPorValorMinimo(pedClone, categoria, Categoria.TIPO_CATEGORIA_ESPECIAL)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_DESC_CATEGORIA_EDICAO, Messages.CATEGORIA_ESPECIAL));
				} else if (vlPedido < categoria.vlMinPedidoAtacado && itemPedido.pedido.vlPctDesc3 > 0 && CategoriaService.getInstance().isCategoriaDisponivelPorValorMinimo(pedClone, categoria, Categoria.TIPO_CATEGORIA_ATACADO)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_DESC_CATEGORIA_EDICAO, Messages.CATEGORIA_ATACADO));
				}
			}
		}
	}
	}
	
	public Vector findHistoricoEstoque(String cdEmpresa, String cdCliente, String cdRepresentante, String cdProduto) throws SQLException {
		return getItemPedidoPdbxDao().findHistoricoEstoque(cdEmpresa, cdCliente, cdRepresentante, cdProduto);
	}
	
	public double findVlTotalItemPedidoMeta(String cdGrupoProdutoMeta, String cdCliente) throws SQLException {
		return getItemPedidoPdbxDao().findVlTotalItemPedidoMeta(cdGrupoProdutoMeta, cdCliente);
	}

	public void validaSeKitJaFoiAdicionadoAoPedido(Pedido pedido, String cdKit) throws SQLException {
		if (LavenderePdaConfig.isUsaKitProdutoFechado()) {
			ItemPedido itemPedidoFilter = createNewItemPedido(pedido);
			itemPedidoFilter.cdKit = cdKit;
			if (ItemPedidoPdbxDao.getInstance().countItemPedidoPertencemAoKit(itemPedidoFilter) > 0) {
				throw new ValidationException(Messages.KIT_MSG_KIT_ADICIONADO_PEDIDO);
			}
		}
		
	}

	public double getVlItemPedidoUnidadePadrao(ItemPedido itemPedido, Produto produto) throws SQLException {
		itemPedido.cdUnidade = produto.cdUnidade;
		return itemPedido.vlItemPedido;
	}
	
	public int countItemPedidoSemKit(ItemPedido itemPedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().countItemPedidoSemKit(itemPedido);
	}
	
	public void setEstoqueAtualizadoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosDentroPedido()) {
			String cdLocalEstoque;
			if (LavenderePdaConfig.usaLocalEstoquePorCliente()) {
				cdLocalEstoque = itemPedido.pedido.getCliente().cdLocalEstoque;
			} else if(LavenderePdaConfig.usaLocalEstoquePorTipoPedido()) {
				cdLocalEstoque = itemPedido.pedido.getTipoPedido().cdLocalEstoque;
			} else {
				cdLocalEstoque = itemPedido.pedido.getTabelaPrecoListaProdutosFilter(itemPedido.cdTabelaPreco).cdLocalEstoque;
			}
			if (cdLocalEstoque == null && itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
				cdLocalEstoque = EmpresaService.getInstance().getLocalEstoqueEmpresa();
			}
			if (cdLocalEstoque == null) {
				cdLocalEstoque = itemPedido.getCdLocalEstoque();
			}
			itemPedido.estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP, itemPedido.pedido.getFlModoEstoque());
			if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
				itemPedido.estoque.qtEstoque = RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(itemPedido);
			}
		}
	}
	
	public int countItensSugInseridos(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = createItemPedidoFilterByFrequencia(pedido);
		itemPedido.flSugVendaPerson = ValueUtil.VALOR_SIM;
		return countByExample(itemPedido);
	}
	
	public double getQtdMinVendaUnidadeAlternativa(ItemPedido itemPedido, double qtMinVenda) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			if (produtoUnidade.isMultiplica()) {
				qtMinVenda /= produtoUnidade.nuConversaoUnidade;
			} else {
				qtMinVenda *= produtoUnidade.nuConversaoUnidade;
			}
		}
		if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
			qtMinVenda = Math.ceil(qtMinVenda);
		}
		return qtMinVenda;
	}
	
	public double getMaxDescontoItemPedido(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		double maxDesc = 0d;
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (maxDesc < itemPedido.vlPctDesconto) {
				maxDesc = itemPedido.vlPctDesconto;
			}
		}
		return maxDesc;
	}
	
	public void aplicaDescontoAcrescimoIcmsCliente(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			IcmsCliente filter = new IcmsCliente();
			filter.cdEmpresa = pedido.cdEmpresa;
			filter.cdRepresentante = pedido.cdRepresentante;
			Cliente cliente = pedido.getCliente();
			filter.cdRamoAtividade = cliente.cdRamoAtividade;
			filter.cdUf = cliente.cdEstadoComercial;
			filter.flContribuinte = cliente.flContribuinte;
			filter.flConsumidorFinal = cliente.flConsumidorFinal;
			filter.limit = 1;
			Tributacao tributacao = itemPedido.getTributacaoItem();
			Vector list;
			boolean aplicouDescAcrescConfigEspecial = false;
			if (tributacao != null) {
				filter.cdConfigIcmsEspecial = tributacao.cdTributacaoProduto;
				list = IcmsClienteService.getInstance().findAllByExample(filter);
				if (ValueUtil.isNotEmpty(list)) {
					aplicaDescontoAcrescimoIcmsCliente(itemPedido, filter, list);
					aplicouDescAcrescConfigEspecial = true;
				}
			}
			if (!aplicouDescAcrescConfigEspecial) {
				filter.cdConfigIcmsEspecial = IcmsCliente.CD_TRIBUTACAO_CONFIG_PADRAO;
				list = IcmsClienteService.getInstance().findAllByExample(filter);
				if (ValueUtil.isNotEmpty(list)) {
					aplicaDescontoAcrescimoIcmsCliente(itemPedido, filter, list);
				}
			}
		}
	}

	private void aplicaDescontoAcrescimoIcmsCliente(ItemPedido itemPedido, IcmsCliente filter, Vector list) throws SQLException {
		IcmsCliente icmsCliente = (IcmsCliente) list.items[0];
		DescontoIcms descontoIcms = DescontoIcmsService.getInstance().findDescontoIcmsPorIcmsCliente(icmsCliente);
		if (descontoIcms != null) {
			if (descontoIcms.vlPctDesconto > 0) {
				double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 - (descontoIcms.vlPctDesconto / 100)));
				itemPedido.vlBaseItemPedido = vlBaseItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
				itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * (1 - (descontoIcms.vlPctDesconto / 100)));
				itemPedido.vlPctDescontoIcms = descontoIcms.vlPctDesconto;
			} else if (descontoIcms.vlPctAcrescimo > 0) {
				double vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * (1 + (descontoIcms.vlPctAcrescimo / 100)));
				itemPedido.vlBaseItemPedido = vlBaseItemPedido;
				itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
				itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
				itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * (1 + (descontoIcms.vlPctAcrescimo / 100)));
				itemPedido.vlPctAcrescimoIcms = descontoIcms.vlPctAcrescimo;
			}
		}
	}

	public void aplicaIndiceFinanceiroClienteGrupoProd(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaIndiceClienteGrupoProd) {
			double vlIndiceClienteGrupoProd = IndiceClienteGrupoProdService.getInstance().getIndiceByItemPedido(itemPedido);
			if (vlIndiceClienteGrupoProd == 0 && vlIndiceClienteGrupoProd == 1) {
				return;
			}
			itemPedido.vlIndiceClienteGrupoProd = vlIndiceClienteGrupoProd;
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceClienteGrupoProd);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao  = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceClienteGrupoProd);
		}
	}
	
	public void loadInfosAdicionaisItemComplementar(ItemPedido complementar) throws SQLException {
		Vector itemPedidoList = ItemPedidoPdbxDao.getInstance().findItensByComplementar(complementar);
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			ItemPedido itemAdicional = (ItemPedido)itemPedidoList.items[0];
			complementar.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_MULTIPLOS_COMPLEMENTARES;
			complementar.vlItemPedidoOrigem = itemAdicional.vlItemPedido;
			complementar.cdProdutoOrigem = itemAdicional.cdProduto;
		}
	}
	
	protected void mantemPrecoNegociadoReplicacaoPedido(Pedido pedido ,ItemPedido itemPedido, boolean onReplicacaoPedido) {
		if (isDeveManterPrecoNegociado(pedido) && onReplicacaoPedido && itemPedido.vlItemPedido != itemPedido.vlNegociado && !itemPedido.isMantemValorKitNaReplicacao()) {
			itemPedido.vlItemPedido =  itemPedido.vlNegociado;
			calculaPctAcrescimoDescontoItemPedido(itemPedido, itemPedido.pedido);
			pedido.itemPedidoPrecoNegociadoList.addElement(itemPedido);
		}
	}
	
	
	protected void desfazValorNegocionado(Pedido pedido) {
		Vector itemPedidoList = pedido.itemPedidoList;
		if (isDeveManterPrecoNegociado(pedido) && ValueUtil.isNotEmpty(itemPedidoList)) {
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				itemPedido.vlItemPedido = itemPedido.vlNegociado;
				itemPedido.vlPctDesconto = itemPedido.vlPctDescInicial;
				itemPedido.vlPctAcrescimo = itemPedido.vlPctAcrescInicial;
			}
		}
	}
	
	private boolean isDeveManterPrecoNegociado(Pedido pedido) {
		return (LavenderePdaConfig.mantemPrecoNegociadoReplicacaoPedido || pedido.isPedidoReplicadoConvertidoTipoPedido);
	}

	public void limpaDescItemMaxDescProdCli(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaDescMaxProdCli && itemPedido.possuiDescMaxProdCli()) {
			clearDadosDesconto(itemPedido);
			resetDescontosItemPedido(itemPedido);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		}
	}
	
	
	public EmbalagensResultantes calculaDescQtdEmbalagemCompleta(ItemPedido itemPedido, final boolean calculaSugestaoEmb) throws SQLException {
		EmbalagensResultantes embalagensResultantes = null;
		if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta
				&& ValueUtil.isNotEmpty(itemPedido.getItemTabelaPreco().descontoQuantidadeList)
				&& itemPedido.getQtItemFisico() != 0) {
			ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
			produtoUnidadeFilter.cdEmpresa = itemPedido.cdEmpresa;
			produtoUnidadeFilter.cdRepresentante = itemPedido.cdRepresentante;
			produtoUnidadeFilter.cdProduto = itemPedido.cdProduto;
			double[] tamanhosEmbalagensList = ProdutoUnidadeService.getInstance().getTamanhosEmbalagensByExample(produtoUnidadeFilter);
			if (tamanhosEmbalagensList != null) {
				boolean qtdDigitadaFormaEmbalagemCompleta = false;
				for (double tamanhoEmbalagem : tamanhosEmbalagensList) {
					if (tamanhoEmbalagem == itemPedido.getQtItemFisico()) {
						qtdDigitadaFormaEmbalagemCompleta = true;
						break;
					}
				}
				if (!qtdDigitadaFormaEmbalagemCompleta) {
					embalagensResultantes = CalculaEmbalagensService.getInstance().getEmbalagensResultantes(tamanhosEmbalagensList, itemPedido.getQtItemFisico(), calculaSugestaoEmb);
					if (embalagensResultantes.gerouEmbalagemCompleta()) {
						itemPedido.gerouEmbalagemCompleta = true;
					} else {
						itemPedido.gerouEmbalagemCompleta = false;
					}
				} else {
					itemPedido.gerouEmbalagemCompleta = true;
				}
			}
		}
		return embalagensResultantes;
	}
	public void updateVlVpcItensPedido(Pedido pedido, double vlPctVpc) throws SQLException {
		((ItemPedidoPdbxDao) getCrudDao()).updateVlVpcItensPedido(pedido, vlPctVpc);
	}
	
	public Vector agrupaItemPedido(Vector itemPedidoList) {
		Vector itemPedidoAgrupadoList = new Vector();
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			int size = itemPedidoAgrupadoList.size();
			boolean  itemEncontrado = false;
			for (int j = 0; j < size; j++) {
				ItemPedido itemPedidoAgrupado = (ItemPedido) itemPedidoAgrupadoList.items[j];
				if (itemPedidoAgrupado.fazParteDaGradePreco(itemPedido)) {
					itemEncontrado = true;
					itemPedidoAgrupado.itemPedidoPorGradePrecoList.addElement(itemPedido);
					itemPedidoAgrupado.setQtItemFisico(itemPedidoAgrupado.getQtItemFisico() + itemPedido.getQtItemFisico());
					itemPedidoAgrupado.qtItemFaturamento += itemPedido.qtItemFaturamento;
					itemPedidoAgrupado.vlTotalItemPedido += LavenderePdaConfig.permiteAlterarValorItemComIPI ? itemPedido.vlTotalItemPedido + itemPedido.vlTotalIpiItem : itemPedido.vlTotalItemPedido;
					itemPedidoList.removeElementAt(i);
					i--;
					break;
				}
			}
			if (!itemEncontrado) {
				adicionaNovoItemPedidoAgrupado(itemPedidoAgrupadoList, itemPedido);
				itemPedidoList.removeElementAt(i);
				i--;
			}
		}
		itemPedidoList.addElementsNotNull(itemPedidoAgrupadoList.items);
		return itemPedidoList;
	}
	
	public Vector agrupaItemPedidoGrade(Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		Vector itemPedidoAgrupadoList = new Vector(size);
		HashMap<String, ItemPedido> hashItemPedidoGrade = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.getProduto().isProdutoAgrupadorGrade()) {
				if (hashItemPedidoGrade.containsKey(itemPedido.getProduto().getDsAgrupadorGrade())) {
					ItemPedido itemPedidoAgrupado = hashItemPedidoGrade.get(itemPedido.getProduto().getDsAgrupadorGrade());
					itemPedidoAgrupado.itemPedidoPorGradePrecoList.addElement(itemPedido);
					itemPedidoAgrupado.setQtItemFisico(itemPedidoAgrupado.getQtItemFisico() + itemPedido.getQtItemFisico());
					itemPedidoAgrupado.qtItemFaturamento += itemPedido.qtItemFaturamento;
					itemPedidoAgrupado.vlTotalItemPedido += LavenderePdaConfig.permiteAlterarValorItemComIPI ? itemPedido.vlTotalItemPedido + itemPedido.vlTotalIpiItem : itemPedido.vlTotalItemPedido;
				} else {
					ItemPedido itemPedidoAgrupado = getItemPedidoAgrupado(itemPedido);
					itemPedidoAgrupadoList.addElement(itemPedidoAgrupado);
					hashItemPedidoGrade.put(itemPedidoAgrupado.getProduto().getDsAgrupadorGrade(), itemPedidoAgrupado);
				}
			} else {
				itemPedidoAgrupadoList.addElement(itemPedido);
			}
		}
		return itemPedidoAgrupadoList;
	}
	
	private void adicionaNovoItemPedidoAgrupado(Vector itemPedidoAgrupadoList , ItemPedido itemPedido) {
		ItemPedido itemPedidoAgrupado = getItemPedidoAgrupado(itemPedido);
		itemPedidoAgrupadoList.addElement(itemPedidoAgrupado);
	}

	private ItemPedido getItemPedidoAgrupado(ItemPedido itemPedido) {
		ItemPedido itemPedidoAgrupado = (ItemPedido)itemPedido.clone();
		
		if (!itemPedido.isItemAgrupador) {
			//Se for item Agrupador não adiciona ele na lista dos itens convencionais;
			itemPedidoAgrupado.itemPedidoPorGradePrecoList.addElement(itemPedido);
		}
		itemPedidoAgrupado.isItemAgrupador = true;
		return itemPedidoAgrupado;
	}

	public void carregaItensGradePreco(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4()) {
			ItemPedido itemPedidoFilter = getNewItemPedido(itemPedido.pedido);
			itemPedidoFilter.cdProduto = itemPedido.cdProduto;
			itemPedidoFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
			itemPedidoFilter.cdItemGrade1 = itemPedido.cdItemGrade1;
			itemPedido.itemPedidoPorGradePrecoList = findAllByExample(itemPedidoFilter);
			totalizaValores(itemPedido);
		}
	}
	
	public void carregaItensAgrupadorGrade(ItemPedido itemPedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(itemPedido.pedido);
		itemPedidoFilter.dsAgrupadorGradeFilter = itemPedido.dsAgrupadorGradeFilter;
		itemPedidoFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedido.itemPedidoPorGradePrecoList = findAllByExample(itemPedidoFilter);
		if (!LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			totalizaValores(itemPedido);
		}
	}
	
	public Vector findItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(itemPedido.pedido);
		itemPedidoFilter.cdProduto = itemPedido.cdProduto;
		itemPedidoFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoFilter.cdItemGrade1 = itemPedido.cdItemGrade1;
		return findAllByExample(itemPedidoFilter);
	}
	
	public Vector findItemPedidoAgrupadorGrade(ItemPedido itemPedido) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(itemPedido.pedido);
		itemPedidoFilter.dsAgrupadorGradeFilter = itemPedido.getProduto().getDsAgrupadorGrade();
		itemPedidoFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
		return findAllByExample(itemPedidoFilter);
	}
	
	private void totalizaValores(ItemPedido itemPedido) {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedidoPorGradePrecoList.size(); 
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			itemPedidoGradePreco.pedido = itemPedido.pedido;
			if (itemPedido.equals(itemPedidoGradePreco)) continue;
			itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + itemPedidoGradePreco.getQtItemFisico());
			itemPedido.qtItemFaturamento += itemPedidoGradePreco.qtItemFaturamento;
			itemPedido.vlTotalItemPedido += itemPedidoGradePreco.vlTotalItemPedido;
		}
	}
	
	public ItemPedido buscaItemPedidoPorGrade(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, Vector itemPedidoList) {
		if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (ValueUtil.valueEquals(itemPedido.cdItemGrade1, cdItemGrade1) && ValueUtil.valueEquals(itemPedido.cdItemGrade2, cdItemGrade2) && ValueUtil.valueEquals(itemPedido.cdItemGrade3, cdItemGrade3)) {
					return itemPedido;
				}
			}
		}
		return new ItemPedido();
	}
	
	public Vector buscaItemPedidoPorProduto(Pedido pedido, String cdProduto) throws SQLException {
		ItemPedido itemPedidoFilter = getNewItemPedido(pedido);
		itemPedidoFilter.cdProduto = cdProduto;
		return findAllByExample(itemPedidoFilter);
	}
	

	public void aplicaIndiceVolume(ItemPedido itemPedido) {
		double vlIndiceFinanceiro = itemPedido.vlIndiceVolume;
		if (vlIndiceFinanceiro != 0) {
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
		}
	}
	
	public void deletaItemPedidoPorGrade1(Vector itemPedidoPorGradePrecoBancoList, ItemPedido itemPedido, Pedido pedido) throws SQLException {
		int size = itemPedidoPorGradePrecoBancoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoBanco = (ItemPedido) itemPedidoPorGradePrecoBancoList.items[i];
			itemPedidoBanco.pedido = itemPedido.pedido;
			delete(itemPedidoBanco);
			pedido.itemPedidoList.removeElement(itemPedidoBanco);
		}
	}
	
	public void calculateValoresInterpolacaoItem(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		try {
			int nuCasasVlUnitario = LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit;
			int nuCasasVlTotal = LavenderePdaConfig.nuArredondamentoRegraInterpolacaoTotal;
			int nuCasasTruncamento = LavenderePdaConfig.nuTruncamentoRegraDescontoVerba;
			
			// Carregamento da quantidade em bigDecimal
			BigDecimal qtd = BigDecimal.valueOf(itemPedido.getQtItemFisico() <= 0 ? 1 : itemPedido.getQtItemFisico()).setScale(ValueUtil.doublePrecision, BigDecimal.ROUND_HALF_UP);
			
			// Carregamento dos valores base
			BigDecimal vlBaseInterpolacaoProdutoRound = BigDecimal.valueOf(ValueUtil.round(itemPedido.vlBaseInterpolacaoProduto)).setScale(nuCasasVlUnitario, BigDecimal.ROUND_HALF_UP);
			BigDecimal vlTotalBaseInterpolacao = vlBaseInterpolacaoProdutoRound.multiply(qtd).setScale(nuCasasVlTotal, BigDecimal.ROUND_HALF_UP);
			
			// Calculo do valor total do desconto auto
			BigDecimal vlTotalDescontoAuto = vlTotalBaseInterpolacao.multiply(BigDecimal.valueOf(itemPedido.vlPctDescontoAuto / 100)).setScale(nuCasasTruncamento, BigDecimal.ROUND_FLOOR);
			itemPedido.vlTotalDescontoAuto = vlTotalDescontoAuto.doubleValue();
			
			// Valor total Desconto auto
			BigDecimal vlTotalDescontoAutoTotalDesc = vlTotalBaseInterpolacao.multiply(BigDecimal.valueOf(itemPedido.vlPctDescontoAuto / 100)).setScale(nuCasasTruncamento, BigDecimal.ROUND_FLOOR);
			
			// Vl Preco Efetivo UN
			BigDecimal vlPrecoEfetivoUnitario = vlBaseInterpolacaoProdutoRound.multiply(qtd).subtract(vlTotalDescontoAutoTotalDesc);
			vlPrecoEfetivoUnitario = vlPrecoEfetivoUnitario.divide(qtd, BigDecimal.ROUND_HALF_UP).setScale(nuCasasVlUnitario, BigDecimal.ROUND_HALF_UP);
			itemPedido.vlPrecoEfetivoUnitario = vlPrecoEfetivoUnitario.doubleValue();
	
			// Vl Efetivo Total do Item
			BigDecimal vlEfetivoTotalItem = vlPrecoEfetivoUnitario.multiply(qtd).setScale(nuCasasVlTotal, BigDecimal.ROUND_HALF_UP);
			itemPedido.vlEfetivoTotalItem = vlEfetivoTotalItem.doubleValue();
			
			// Aplica verba no item
			if (!itemPedido.usaCestaPromo && !itemPedido.hasDescProgressivo() && !itemPedido.isCombo() && (!LavenderePdaConfig.usaGerenciaDeCreditoDesconto || itemPedido.qtdCreditoDesc == 0 || itemPedido.isFlTipoCadastroQuantidade()) && !LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido) {
				VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
			}
			
			// Preço efetivo unitário com desconto
			BigDecimal vlPrecoEfetivoUnitarioDesc = vlPrecoEfetivoUnitario.multiply(BigDecimal.valueOf(itemPedido.vlPctDesconto / 100));
			vlPrecoEfetivoUnitarioDesc = vlPrecoEfetivoUnitario.subtract(vlPrecoEfetivoUnitarioDesc).setScale(nuCasasVlUnitario, BigDecimal.ROUND_HALF_UP);
			itemPedido.vlPrecoEfetivoUnitarioDesc = vlPrecoEfetivoUnitarioDesc.doubleValue();
			
			// Percentual do desconto efetivo
			if (itemPedido.vlBaseInterpolacaoProduto > 0) {
				BigDecimal vlPctDescontoEfetivo = BigDecimal.valueOf((vlPrecoEfetivoUnitarioDesc.doubleValue()) / vlBaseInterpolacaoProdutoRound.doubleValue());
				vlPctDescontoEfetivo = BigDecimal.valueOf(1).subtract(vlPctDescontoEfetivo);
				vlPctDescontoEfetivo = BigDecimal.valueOf(100).multiply(vlPctDescontoEfetivo).setScale(nuCasasVlTotal, BigDecimal.ROUND_HALF_UP);
				itemPedido.vlPctDescontoEfetivo = vlPctDescontoEfetivo.doubleValue();
			} else {
				itemPedido.vlPctDescontoEfetivo = 0d;
			}
			
			// Valor efetivo total item com desconto
			BigDecimal vlEfetivoTotalItemDesc = vlPrecoEfetivoUnitarioDesc.multiply(qtd).setScale(nuCasasVlTotal, BigDecimal.ROUND_HALF_UP);
			itemPedido.vlEfetivoTotalItemDesc = vlEfetivoTotalItemDesc.doubleValue();
			
			// Percentual desconto auto efetivo
			if (itemPedido.vlBaseInterpolacaoProduto > 0) {
				BigDecimal vlPctDescontoAutoEfetivo = BigDecimal.valueOf((vlPrecoEfetivoUnitario.doubleValue()) / (vlBaseInterpolacaoProdutoRound.doubleValue()));
				vlPctDescontoAutoEfetivo = BigDecimal.valueOf(1).subtract(vlPctDescontoAutoEfetivo);
				vlPctDescontoAutoEfetivo = BigDecimal.valueOf(100).multiply(vlPctDescontoAutoEfetivo).setScale(nuCasasVlTotal, BigDecimal.ROUND_HALF_UP);
				itemPedido.vlPctDescontoAutoEfetivo = vlPctDescontoAutoEfetivo.doubleValue();
			} else {
				itemPedido.vlPctDescontoAutoEfetivo = 0d;
			}
		} catch (ArithmeticException | IllegalArgumentException | InvalidNumberException e) {
			ExceptionUtil.handle(e);
			throw new ValidationException(Messages.ERRO_CALCULO_INTERPOLACAO);
		}
		
	}

	public Vector findCdProdutoComPedidoNaoEnviadoList() throws SQLException {
		return getItemPedidoPdbxDao().findCdProdutoComPedidoNaoEnviadoList();
	}

	public double realizaCalculaPrecoPorMetroQuadradoItemPedido(ItemPedido itemPedido) throws SQLException {
		Unidade unidade = UnidadeService.getInstance().findUnidadeByCdUnidade(itemPedido.cdUnidade);
		if (unidade == null || !ValueUtil.valueEquals(ValueUtil.VALOR_SIM, unidade.flCalculaPrecoMetroQuadrado)) return itemPedido.vlItemPedido;
		itemPedido.vlItemPedido = (itemPedido.vlLargura / 1000) * (itemPedido.vlComprimento / 1000) * itemPedido.vlBaseItemTabelaPreco;
		return itemPedido.vlItemPedido;
	}
	
	public boolean validaDescExtraFlex(ItemPedido itemPedido, double oldValue) throws SQLException {
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente) {
			double vlItemPedidoSemDescExtra = ValueUtil.round(itemPedido.vlItemPedido / (1 - oldValue / 100));
			return vlItemPedidoSemDescExtra >= itemPedido.getItemTabelaPreco().getVlBaseFlex(itemPedido.pedido, itemPedido);
		}
		return true;
	}
	
	private void aplicaDescontoExtra(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaDescontoExtra && itemPedido.vlPctDesconto2 > 0) {
			aplicaDescontoItemPedido(itemPedido);
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto2 / 100)));
		}
	}

	public void aplicaDescontoQtdPesoNoVlItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		itemPedido.qtPeso = getPesoItemPedido(itemPedido);
		double vlItemPedido = 0;
		if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
			carregarFaixasDesconto(itemPedido,pedido);
			if (pedido.descQuantidadePeso != null && !itemPedido.isEditandoValorItem() && !itemPedido.isEditandoDescontoPct()) {
				vlItemPedido = calculaDescVlBaseItemPorPesoItemPedidoTabelaPreco(pedido, itemPedido, itemPedido.vlBaseItemPedido);
			}
		} else {
			vlItemPedido = calculaDescVlBaseItemPorPesoPedido(pedido, itemPedido, itemPedido.vlBaseItemPedido, pedido.isRecalculandoVlItemPedidoItens);
		}
		boolean hasDescQuantidade = pedido.descQuantidadePeso != null || pedido.isRecalculandoVlItemPedidoItens;
		if (vlItemPedido > 0) {
			itemPedido.vlItemPedido = vlItemPedido;
		}
		if (hasDescQuantidade) {
			zeraDescAcresItemPedido(itemPedido);
		} else if (!LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
			resetAndCalculateItemPedido(pedido, itemPedido);
		}
		itemPedido.vlPctFaixaDescQtdPeso = pedido.descQuantidadePeso != null ? pedido.descQuantidadePeso.vlPctDesconto : 0;
		recalculaVlBaseFlexItens(pedido, true);
	}

	private void carregarFaixasDesconto(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		DescQuantidadePesoService.getInstance().loadFaixasDescItemPedidoTabelaPreco(pedido, itemPedido);
		if (ValueUtil.isNotEmpty(pedido.descQuantidadePesoList)) {
			pedido.descQuantidadePeso = DescQuantidadePesoService.getInstance().getDescQuantidadePesoPedido(pedido.descQuantidadePesoList, getAtualPesoPedidoTabelaPreco(pedido, itemPedido));
		}
	}
	
	public void recalculaDescCascataCondicaoPagamentoChange(Pedido pedido) throws SQLException { 
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) { 
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			calculate(itemPedido, pedido);
		}
		PedidoService.getInstance().updateItensPedidoAfterChanges(pedido);
		PedidoService.getInstance().calculate(pedido);
		PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
		PedidoPdbxDao.getInstance().update(pedido);
	}
	
	public double realizaCalculoPrecoPorVolumeProduto(ItemPedido itemPedido) throws SQLException {
		double volume = itemPedido.vlLargura * itemPedido.vlComprimento * itemPedido.vlAltura;
		return ValueUtil.round(volume, LavenderePdaConfig.getInfoComplementarItemPedidoListaNuCasasDecimais());
	}
	
	public void aplicaFreteEmbutido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if ((tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) && LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() && (LavenderePdaConfig.isConfigCalculoPesoPedido() || LavenderePdaConfig.usaFreteValorUnidade)) {
			double vlFreteItem = TipoFreteTabPrecoService.getInstance().getVlItemPedidoFrete(itemPedido.getTipoFreteTabPreco(), itemPedido);
			itemPedido.vlBaseItemPedido += vlFreteItem;
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlTotalItemPedido = itemPedido.vlBaseItemPedido;
		}
	}	
	public void aplicaFreteNoItemPedido(ItemPedido itemPedido) throws SQLException {
		Pedido pedido = itemPedido.pedido;
		TipoFrete tipoFrete = pedido.getTipoFrete();
		if (tipoFrete == null || !tipoFrete.isCalculaFreteItem()) return;
		
			double vlPctFrete = 0d;
			if (tipoFrete.isTipoFreteSemFrete()) {
				vlPctFrete = EmpresaService.getInstance().findVlPctFreteByCdEmpresa(pedido.cdEmpresa);
			} else {
				if (pedido.transportadoraCep == null) {
					pedido.transportadoraCep = TransportadoraCepService.getInstance().findTransportadoraCepByCdTransportadoraAndDsCepComercial(pedido.cdTransportadora, pedido.getCliente().dsCepComercial);
				}
				vlPctFrete = pedido.transportadoraCep != null ? pedido.transportadoraCep.vlPctFrete : 0;
			}
			itemPedido.vlBaseItemPedido = (((vlPctFrete / 100) + 1) * itemPedido.vlBaseItemPedido);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
	}

	public int countItemComPrecoLiberadoPorSenha(Pedido pedido) throws SQLException {
		return getItemPedidoPdbxDao().countItemComPrecoLiberadoSenha(pedido);
	}
	
	public void updateValuesVerba(ItemPedido itemPedido) throws SQLException {
		ItemPedidoPdbxDao.getInstance().updateValuesVerba(itemPedido);
	}
	
	public Vector filtraItensPedidoPorBusca(Vector list, String dsFiltro) throws SQLException {
		int size = list.size();
		Vector filteredList = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) list.items[i];
			String filtro = dsFiltro.toLowerCase();
			String dsNome = itemPedido.getDsProduto().toLowerCase();
			String cdProduto = itemPedido.cdProduto.toLowerCase();
			if (LavenderePdaConfig.usaPesquisaInicioString && !filtro.startsWith("*")) {
				if (dsNome.startsWith(filtro)) {
					filteredList.addElement(itemPedido);
					continue;
				}
			} else if (dsNome.contains((filtro.startsWith("*") ? filtro.substring(1) : filtro))){
				filteredList.addElement(itemPedido);
				continue;
			}
			if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.valueEquals(cdProduto, filtro)) {
					filteredList.addElement(itemPedido);
				} else if (!LavenderePdaConfig.usaFiltraProdutoCodigoExato() && cdProduto.contains(filtro)){
					filteredList.addElement(itemPedido);
				}
			}
		}
		return filteredList;
	}

	public double getVlPctUltimoDescontoAplicadoNoProdutoNesteCliente(Pedido pedido, String cdProduto) throws SQLException {
		return ItemPedidoPdbxDao.getInstanceErp().getVlPctUltimoDescontoAplicadoNoProdutoNesteCliente(pedido, cdProduto);
	}
	
	protected boolean permiteLiberarPedido(Pedido pedido, int maxNuSequenciaPedidoDesc) throws SQLException {
		if (LavenderePdaConfig.permiteLiberacaoPedidoPendenteOutraOrdemLiberacao && UsuarioConfigService.getInstance().isLiberaPedidoOutraOrdem()) return true; 
		
		Vector itemPedidoList = findItemPedidoErpPendenteList(pedido);
		if (ValueUtil.isEmpty(itemPedidoList)) return true;
		
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!isLiberaItemPedido(itemPedido, maxNuSequenciaPedidoDesc)) return false;
		}
		return true;
	}

	public boolean restouItensPendenteLiberacao(Pedido pedido, int nuMaxSequencia) throws SQLException {
		if (pedido == null) return false;
		
		Vector itemPedidoPendenteList = findItemPedidoErpPendenteList(pedido);
		if (ValueUtil.isEmpty(itemPedidoPendenteList)) return false;
		
		return restouItensPendenteLiberacao(pedido, itemPedidoPendenteList, nuMaxSequencia);
	}

	private boolean restouItensPendenteLiberacao(Pedido pedido, Vector itemPedidoPendenteList, int nuOrdemLiberacaoUsuario) throws SQLException {
		int size = itemPedidoPendenteList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPendenteList.items[i];
			if (!isLiberaItemPedido(itemPedido, nuOrdemLiberacaoUsuario)) continue;
			
			liberaItemPedidoPorOrdemLiberacao(pedido, itemPedido.nuOrdemLiberacao);
		}
		ItemPedido itemPedidoFilter = new ItemPedido(pedido);
		return ItemPedidoPdbxDao.getInstance().countItensPedidoPendenteLiberacao(itemPedidoFilter) > 0;
	}

	private void liberaItemPedidoPorOrdemLiberacao(Pedido pedido, int nuOrdemLiberacao) throws SQLException {
		if (nuOrdemLiberacao == 0) return;
		
		ItemPedidoPdbxDao.getInstance().liberaItemPedidoPorOrdemLiberacao(new ItemPedido(pedido, nuOrdemLiberacao));
	}

	private Vector findItemPedidoErpPendenteList(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstanceErp().findItemPedidoPendenteList(new ItemPedido(pedido));
	}
	
	protected Vector findItemPedidoPendenteList(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().findItemPedidoPendenteList(new ItemPedido(pedido));
	}
	
	private boolean isMarcaPedidoPendentePorPedidoBonificacao(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao()) return false;
		Pedido pedido = itemPedido.pedido;
		if (pedido == null || !pedido.isPedidoBonificacao()) return false;
		
		TipoPedido tipoPedido = pedido.getTipoPedido();
		return tipoPedido != null && tipoPedido.isMarcaPedidoPendentePorPedidoBonificacao();
	}

	private boolean isMarcaPedidoPendentePorPedidoTroca(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaPedidosTroca()) return false;
		Pedido pedido = itemPedido.pedido;
		if (pedido == null || !pedido.isPedidoTroca()) return false;
		
		TipoPedido tipoPedido = pedido.getTipoPedido();
		return tipoPedido != null && tipoPedido.isMarcaPedidoPendentePorPedidoTroca();
	}
	
	private boolean isMarcaPedidoPendentePorPoliticaComercial(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaPoliticaComercial()) return false;
		Pedido pedido = itemPedido.pedido;
		if (pedido == null || pedido.isPedidoBonificacao() ) return false;
		return itemPedido.politicaComercialFaixa != null && ValueUtil.isNotEmpty(itemPedido.politicaComercialFaixa.cdMotivoPendencia);
	}
	
	private boolean isMarcaPedidoPendentePorTipoFrete(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaTipoFreteDiferentePadrao()) return false;
		Pedido pedido = itemPedido.pedido;
		if (pedido == null) return false;
		TipoFrete tipoFrete = pedido.getTipoFrete();
		return tipoFrete != null && tipoFrete.isMarcaPedidoPendentePorTipoFrete();
	}
	
	private void defineCdMargemRentab(ItemPedido itemPedido) throws SQLException {
		Pedido pedido = itemPedido.pedido;
		if (pedido == null || pedido.isPedidoBonificacao() ) return;
		
		itemPedido.cdMargemRentab = MargemRentabService.getInstance().findCdMargemRent(itemPedido);
	}
	
	private void defineCdMargemRentab(Pedido pedido) throws SQLException {
		if (pedido == null || pedido.isPedidoBonificacao()) return;
		pedido.cdMargemRentab = MargemRentabService.getInstance().findCdMargemRent(pedido);
	}
	
	public void marcaItemPedidoPendente(ItemPedido itemPedido) throws SQLException {
		if ((itemPedido.pendenteMaxDesc || itemPedido.pendenteMaxAcresc) && !SessionLavenderePda.isUsuarioLiberaItemPendente()) {
			marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
				itemPedido.vlPctDescAlcada = itemPedido.vlPctDesconto;
			}
			return;
		}
		marcaFlPendente(itemPedido, ValueUtil.VALOR_NAO);
		itemPedido.vlPctDescAlcada = 0;
	}

	public void marcaItemPedidoPorMotivoPendencia(Vector itemPedidoList) throws SQLException {
		marcaItemPedidoPorMotivoPendencia(itemPedidoList, false);
	}

	public void marcaItemPedidoPorMotivoPendencia(Vector itemPedidoList, boolean persisteDadosIndividualmente) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivoPendencia()) {
			return;
		}
		List<String> itemPendenteList = new ArrayList<>();
		List<String> itemNaoPendenteList = new ArrayList<>();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			marcaItemPedidoPorMotivoPendencia(itemPedido, persisteDadosIndividualmente);
			if (itemPedido.isPendente()) {
				itemPendenteList.add(itemPedido.getRowKey());
			} else {
				itemNaoPendenteList.add(itemPedido.getRowKey());
			}
		}
		if (!persisteDadosIndividualmente) {
			if (!itemPendenteList.isEmpty()) {
				ItemPedidoPdbxDao.getInstance().updateFlPendenteItems(ValueUtil.VALOR_SIM, new Vector(itemPendenteList.toArray()));
			}
			if (!itemNaoPendenteList.isEmpty()) {
				ItemPedidoPdbxDao.getInstance().updateFlPendenteItems(ValueUtil.VALOR_NAO, new Vector(itemNaoPendenteList.toArray()));
			}
		}
	}
	
	public void marcaItemPedidoPorMotivoPendencia(ItemPedido itemPedido) throws SQLException {
		marcaItemPedidoPorMotivoPendencia(itemPedido, true);
	}

	public void marcaItemPedidoPorMotivoPendencia(ItemPedido itemPedido, boolean persisteBaseDados) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivoPendencia()) {
			return;
		}
		ItemPedido itemPedidoOld = (ItemPedido) itemPedido.clone();
		if (isMarcaPedidoPendentePorPedidoBonificacao(itemPedido) || isMarcaPedidoPendenteComItemBonificado(itemPedido)) {
			marcaItemPedidoPorPedidoBonificacao(itemPedido);
		} else if (isMarcaPedidoPendentePorPedidoTroca(itemPedido)) {
			marcaItemPedidoPorPedidoTroca(itemPedido);
		} else if (isMarcaPedidoPendentePorTipoFrete(itemPedido)) {
			marcaItemPedidoPorTipoFrete(itemPedido);
		} else if (isMarcaPedidoPendentePorItemBonificado(itemPedido)) {
			marcaItemPedidoPorItemBonificado(itemPedido);
		} else if (isMarcaPedidoPendentePorPoliticaComercial(itemPedido) && !itemPedido.descontoProgressivoAplicado) {
			marcaItemPedidoPorPoliticaComercial(itemPedido);
		} else if (!isMarcaItemPedidoPorVerbaSaldoGrupoProduto(itemPedido) && !isMarcaItemPedidoPorRentabilidade(itemPedido)) {
			marcaItemNaoPendente(itemPedido);
		}
		if (persisteBaseDados || !ValueUtil.valueEqualsIfNotNull(itemPedidoOld.cdMotivoPendencia, itemPedido.cdMotivoPendencia)
				|| !ValueUtil.valueEqualsIfNotNull(itemPedidoOld.nuOrdemLiberacao, itemPedido.nuOrdemLiberacao)) {
			try {
				ItemPedidoPdbxDao.getInstance().updateColumnsPendencia(itemPedido);
			} catch (ApplicationException ae) {
				ExceptionUtil.handle(ae);
			}
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			defineCdMargemRentab(itemPedido);
		}
	}
	
	private boolean isMarcaPedidoPendenteComItemBonificado(ItemPedido itemPedido) {
		return itemPedido.isItemBonificacao() && LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado();
	}

	private boolean isMarcaPedidoPendentePorItemBonificado(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaVendaComBonificacao()) return false;
		if (itemPedido == null || itemPedido.pedido == null || itemPedido.pedido.getTipoPedido() == null) return false;
		
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		boolean isMarcaItemPendente = tipoPedido.isPossuiMotivoPendenciaBonif() && itemPedido.isItemBonificacao() && !itemPedido.isKitTipo3();
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && isMarcaItemPendente) {
			isMarcaItemPendente = ItemPedidoBonifCfgService.getInstance().isItemComBonificacaoSaldoPendente(itemPedido);
		}
		return isMarcaItemPendente;
	}

	private void marcaItemPedidoPorItemBonificado(ItemPedido itemPedido) throws SQLException {
		marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		itemPedido.cdMotivoPendencia = tipoPedido.cdMotivoPendenciaBonif;
		itemPedido.nuOrdemLiberacao = tipoPedido.nuOrdemLiberacaoBonif;
	}

	private void marcaItemNaoPendente(ItemPedido itemPedido) throws SQLException {
		boolean wasPendente = itemPedido.isPendente();
		marcaFlPendente(itemPedido, ValueUtil.VALOR_NAO);
		itemPedido.cdMotivoPendencia = null;
		itemPedido.nuOrdemLiberacao = 0;
		if(wasPendente && ValueUtil.isNotEmpty(itemPedido.cdMargemRentab)) {
			ItemPedidoPdbxDao.getInstance().marcaItemPedidoPorRentabilidadeAgrupado(itemPedido);
		}
	}

	private void marcaItemPedidoPorPedidoBonificacao(ItemPedido itemPedido) throws SQLException {
		marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		itemPedido.cdMotivoPendencia = tipoPedido.cdMotivoPendenciaBonif;
		itemPedido.nuOrdemLiberacao = tipoPedido.nuOrdemLiberacaoBonif;
	}

	private void marcaItemPedidoPorPedidoTroca(ItemPedido itemPedido) throws SQLException {
		marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
		itemPedido.cdMotivoPendencia = tipoPedido.cdMotivoPendenciaTroca;
		itemPedido.nuOrdemLiberacao = tipoPedido.nuOrdemLiberacaoBonif;
	}
	
	private void marcaItemPedidoPorPoliticaComercial(ItemPedido itemPedido) {
		marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
		itemPedido.cdMotivoPendencia = itemPedido.politicaComercialFaixa.cdMotivoPendencia;
		itemPedido.nuOrdemLiberacao = itemPedido.politicaComercialFaixa.nuOrdemLiberacao;
	}
	
	private void marcaItemPedidoPorTipoFrete(ItemPedido itemPedido) throws SQLException {
		marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
		TipoFrete tipoFrete = itemPedido.pedido.getTipoFrete();
		itemPedido.cdMotivoPendencia = tipoFrete.cdMotivoPendencia;
		itemPedido.nuOrdemLiberacao = tipoFrete.nuOrdemLiberacao;
	}
	
	protected void marcaItemPedidoPorRentabilidade(Pedido pedido) throws SQLException {
		if (!isMarcaItemPedidoPorRentabilidade(pedido)
			|| !LavenderePdaConfig.usaConfigMargemRentabilidade()
			|| pedido.isPedidoBonificacao()) return;
		

		if (LavenderePdaConfig.usaMotivoPendenciaAgrupado()) {
			marcaItemPedidoPorRentabilidadeAgrupado(pedido);
			return;
		} 
		
		if (LavenderePdaConfig.apenasCalculaMargem()) return;
		
		validaItemPedidoComMargemRentabilidadeFaixa(pedido, pedido.itemPedidoList);
		marcaItemPedidoPorRentabilidade(pedido, pedido.itemPedidoList);
	}

	private void marcaItemPedidoPorRentabilidade(Pedido pedido, Vector itemPedidoList) throws SQLException {
		if (LavenderePdaConfig.apenasCalculaMargem() || !LavenderePdaConfig.isUsaMotivosPendenciaMargemOuRentabilidade()) return;
		MargemRentabFaixa margemRentabFaixaPedido = null;
		boolean pedidoPossuiMargemRentabFaixaOuMotivoPendencia = false;
		if (pedido.cdMargemRentab != null) {
			margemRentabFaixaPedido = MargemRentabFaixaService.getInstance().findMargemRentabFaixa(pedido.cdEmpresa, pedido.cdMargemRentab, pedido.vlPctMargemRentab);
			if (margemRentabFaixaPedido != null) pedidoPossuiMargemRentabFaixaOuMotivoPendencia = ValueUtil.isNotEmpty(margemRentabFaixaPedido.cdMotivoPendencia);
		}

		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			//--Se o item já possuir pendencia por politica, deve antes checar se a nuOrdemLiberacao da margem deve sobrescrever a politica.
			if (margemRentabFaixaPedido != null && itemPedido.politicaComercialFaixa != null && itemPedido.nuOrdemLiberacao > margemRentabFaixaPedido.nuOrdemLiberacao) continue;

			if (pedidoPossuiMargemRentabFaixaOuMotivoPendencia) {
				itemPedido.cdMotivoPendencia = margemRentabFaixaPedido.cdMotivoPendencia;
				itemPedido.nuOrdemLiberacao = margemRentabFaixaPedido.nuOrdemLiberacao;
				marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
				if (LavenderePdaConfig.usaMotivoPendenciaAgrupado()) {
					ItemPedidoPdbxDao.getInstance().marcaItemPedidoPorRentabilidadeAgrupado(itemPedido);
					return;
				}
				ItemPedidoPdbxDao.getInstance().updateColumnsPendencia(itemPedido);
				continue;
			}
			
			MargemRentabFaixa margemRentabFaixa = MargemRentabFaixaService.getInstance().findMargemRentabFaixa(itemPedido.cdEmpresa, itemPedido.cdMargemRentab, itemPedido.vlPctMargemRentab);

			if (ValueUtil.isEmpty(margemRentabFaixa.cdMotivoPendencia)) {
				marcaItemNaoPendente(itemPedido);
				continue;
			}

			itemPedido.cdMotivoPendencia = margemRentabFaixa.cdMotivoPendencia;
			itemPedido.nuOrdemLiberacao = margemRentabFaixa.nuOrdemLiberacao;
			marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
			if (LavenderePdaConfig.usaMotivoPendenciaAgrupado()) {
				ItemPedidoPdbxDao.getInstance().marcaItemPedidoPorRentabilidadeAgrupado(itemPedido);
				return;
			}
			ItemPedidoPdbxDao.getInstance().updateColumnsPendencia(itemPedido);
		}
	}

	private void marcaItemPedidoPorRentabilidadeAgrupado(Pedido pedido) throws SQLException {
		Vector itemPedidoList = findItemPedidoComMargemRentabilidade(pedido);
		int size = itemPedidoList.size();
		HashMap<String, Object> variaveis = DomainUtil.getProperties(pedido, true);
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			variaveis.put("cdMargemRentabGrupo", itemPedido.cdMargemRentab);
			FormulaCalculoSqlService.getInstance().executeCalculoSql(variaveis, LavenderePdaConfig.getFormulaCalculoItemPedidoAgrupado(), itemPedido);
			double vlPctMargemRentab = variaveis.get("vlPctMargemRentabGrupo") != null ? ValueUtil.getDoubleValue(variaveis.get("vlPctMargemRentabGrupo").toString()) : 0;
			itemPedido.vlPctMargemRentab = vlPctMargemRentab;
		}
		
		if (LavenderePdaConfig.apenasCalculaMargem() || !LavenderePdaConfig.isUsaMotivosPendenciaMargemOuRentabilidade()) return;
		
		validaItemPedidoComMargemRentabilidadeFaixa(pedido, itemPedidoList);
		marcaItemPedidoPorRentabilidade(pedido, itemPedidoList);
	}
	
	private void validaItemPedidoComMargemRentabilidadeFaixa(Pedido pedido, Vector itemPedidoList) throws SQLException {
		Vector itemPedidoErroList = new Vector();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			MargemRentabFaixa margemRentabFaixa = MargemRentabFaixaService.getInstance().findMargemRentabFaixa(itemPedido.cdEmpresa, itemPedido.cdMargemRentab, itemPedido.vlPctMargemRentab);
			if (margemRentabFaixa != null) continue;
			
			itemPedido.pedido = itemPedido.pedido != null ? itemPedido.pedido : pedido;
			itemPedidoErroList.addElement(itemPedido);
		}
		
		if (ValueUtil.isNotEmpty(itemPedidoErroList)) {
			throw new RelProdutosRentabilidadeSemAlcadaException(itemPedidoErroList);
		}
	}
	
	private boolean isMarcaItemPedidoPorRentabilidade(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivosPendenciaMargemOuRentabilidade()) return false;
		Vector itemPedidoList = findItemPedidoComMargemRentabilidade(pedido);
		return ValueUtil.isNotEmpty(itemPedidoList);
	}
	
	private boolean isMarcaItemPedidoPorRentabilidade(ItemPedido itemPedido) throws SQLException {
		return itemPedido.isPendente() && LavenderePdaConfig.isUsaMotivosPendenciaMargemOuRentabilidade() && LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.apenasCalculaMargem() && !itemPedido.pedido.isPedidoBonificacao() && ValueUtil.isNotEmpty(ItemPedidoPdbxDao.getInstance().findItemPedidoComMargemRentabilidade(itemPedido));
	}
	
	private Vector findItemPedidoComMargemRentabilidade(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido(pedido);
		itemPedido.pedido = pedido;
		return ItemPedidoPdbxDao.getInstance().findItemPedidoComMargemRentabilidade(itemPedido);
	}
	
	private boolean isMarcaItemPedidoPorVerbaSaldoGrupoProduto(ItemPedido itemPedido) throws SQLException {
		return itemPedido.isPendente() && LavenderePdaConfig.isUsaMotivosPendenciaVerbaDeGrupoExtrapolada() && VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(itemPedido.pedido);
	}
	
	public void marcaItemPedidoPorVerbaSaldoGrupoProduto(ItemPedido itemPedido, boolean isVerbaGrupoSaldoExtrapolada) throws SQLException {
		if (isVerbaGrupoSaldoExtrapolada) {
			marcaFlPendente(itemPedido, ValueUtil.VALOR_SIM);
			VerbaGrupoSaldo verbaGrupoSaldo = VerbaGrupoSaldoService.getInstance().getVerbaGrupoSaldoErp(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
			if(verbaGrupoSaldo != null) {
				itemPedido.cdMotivoPendencia = verbaGrupoSaldo.cdMotivoPendencia;
				itemPedido.nuOrdemLiberacao = verbaGrupoSaldo.nuOrdemLiberacao;
			} else {
				throw new ValidationException(Messages.VERBAGRUPOSALDO_MSG_VERBA_SALDO_INDISPONIVEL);
			}
		} else {
			marcaFlPendente(itemPedido, ValueUtil.VALOR_NAO);
			itemPedido.cdMotivoPendencia = null;
			itemPedido.nuOrdemLiberacao = 0;
		}
	}

	private void marcaFlPendente(ItemPedido itemPedido, String valor) {
		itemPedido.flPendente = valor;
		itemPedido.pedido.marcaFlPendente(itemPedido);
	}
	
	private boolean isLiberaItemPedido(ItemPedido itemPedido, int nuSequencia) throws SQLException {
		if (nuSequencia == 0) return false;
		
		if (itemPedido.motivoPendencia.isExigeLiberacaoPassandoPorTodosNiveis())  {
			return ValueUtil.valueEquals(nuSequencia, itemPedido.nuOrdemLiberacao);
		}
		if (itemPedido.motivoPendencia.isExigeLiberacaoSomenteNivelAprovacao()) {
			int nuOrdemLiberacao = itemPedido.nuOrdemLiberacao;
			return nuOrdemLiberacao <= SessionLavenderePda.nuOrdemLiberacaoUsuario;
		}
		return false;
	}

	public void updatePontuacaoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.vlPontuacaoBaseItem == 0 || itemPedido.vlPontuacaoRealizadoItem == 0) return;
		ItemPedidoPdbxDao.getInstance().updatePontuacaoItemPedido(itemPedido);
	}

	public ItemPedido buscaItemPedidoQueExigeLiberacaoSomenteNivelAprovacao(Pedido pedido) throws SQLException {
		if (pedido == null) return null;
		
		Vector itemPedidoPendenteList = findItemPedidoErpPendenteList(pedido);
		if (ValueUtil.isEmpty(itemPedidoPendenteList)) return null;
		
		int size = itemPedidoPendenteList.size();
		for (int i = 0; i < size; ) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPendenteList.items[i];
			if (itemPedido.motivoPendencia.isExigeLiberacaoPassandoPorTodosNiveis()) return null;
			
			return itemPedido;
		}
		return null;
	}	
	
	protected void calculaMargemContribuicaoItemRegra2(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || itemPedido.vlTotalItemPedido == 0) return;
		
		itemPedido.vlTotalDescProdutoRestrito = itemPedido.vlDescProdutoRestrito * itemPedido.getQtItemFisico();
		double vlTotalItemPedidoSemDesconto = itemPedido.vlTotalItemPedido + itemPedido.vlTotalDescProdutoRestrito;
		calculaVlTotalPrecoCusto(itemPedido);
		calculaVlTotalMaoDeObra(itemPedido); 
		calculaVlTotalImpostos(itemPedido, vlTotalItemPedidoSemDesconto);
		calculaVlTotalCustoComercial(itemPedido, vlTotalItemPedidoSemDesconto);
		calculaVlTotalCustoFinanceiro(itemPedido, vlTotalItemPedidoSemDesconto);
		calculaVlFrete(itemPedido);
		itemPedido.vlTotalMargemItem = vlTotalItemPedidoSemDesconto  - (itemPedido.vlTotalPrecoCusto + itemPedido.vlTotalMaoDeObra  + itemPedido.vlTotalImpostos + itemPedido.vlTotalComissao + itemPedido.vlVpc + itemPedido.vlTotalCustoComercial + itemPedido.vlTotalCustoFinanceiro + itemPedido.vlFrete);
		itemPedido.vlPctTotalMargemItem = ValueUtil.round(itemPedido.vlTotalMargemItem / vlTotalItemPedidoSemDesconto * 100);
	}


	private void calculaVlTotalPrecoCusto(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco == null) {
			itemPedido.vlTotalPrecoCusto = 0;
			return;
		}
		itemPedido.vlTotalPrecoCusto = itemTabelaPreco.vlPrecoCusto * itemPedido.vlVolumeItem;
	}
	
	private void calculaVlTotalMaoDeObra(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco == null) {
			itemPedido.vlTotalMaoDeObra = 0;
			return;
		}
		itemPedido.vlTotalMaoDeObra = itemTabelaPreco.vlMaoDeObra * itemPedido.vlVolumeItem;
	}
	
	private void calculaVlTotalImpostos(ItemPedido itemPedido, double vlTotalItemPedidoSemDesconto) throws SQLException {
		Cliente cliente = itemPedido.pedido.getCliente();
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (cliente == null || itemTabelaPreco == null) {
			itemPedido.vlTotalImpostos = 0;
			return;
		}
		
		itemPedido.vlTotalImpostos = (vlTotalItemPedidoSemDesconto * cliente.vlIndiceImpostos) - (itemTabelaPreco.vlCreditoImpostos * itemPedido.vlVolumeItem);
	}
	
	private void calculaVlTotalCustoComercial(ItemPedido itemPedido, double vlTotalItemPedidoSemDesconto) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco == null) {
			itemPedido.vlTotalCustoComercial = 0;
			return;
		}
		itemPedido.vlTotalCustoComercial = ValueUtil.round(vlTotalItemPedidoSemDesconto * itemTabelaPreco.vlPctCustoComercial/100);
	}
	
	private void calculaVlTotalCustoFinanceiro(ItemPedido itemPedido, double vlTotalItemPedidoSemDesconto) throws SQLException {
		CondicaoPagamento condicaoPagamento = itemPedido.pedido.getCondicaoPagamento();
		if (condicaoPagamento == null) {
			itemPedido.vlTotalCustoFinanceiro = 0;
			return;
		}
		itemPedido.vlTotalCustoFinanceiro = ValueUtil.round(vlTotalItemPedidoSemDesconto * condicaoPagamento.vlPctCustoFinanceiro/100);
	}
	
	private void calculaVlFrete(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco == null) {
			itemPedido.vlFrete = 0;
			return;
		}
		itemPedido.vlFrete = itemTabelaPreco.vlPrecoFrete * itemPedido.vlVolumeItem;
	}
	
    public Image getIconRentabilidadeItem(final ItemPedido itemPedido) throws SQLException {
    	int cor = MargemRentabFaixaService.getInstance().findCorMargemRentabFaixa(itemPedido.cdEmpresa, itemPedido.cdMargemRentab, itemPedido.vlPctMargemRentab); 
		return UiUtil.getIconButtonAction("images/rentabilidade.png", cor, true);
	}
    
    public void aplicaIndiceFinanceiroSupRep(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
    		double vlIndiceFinanceiro;
    		if (SessionLavenderePda.getRepresentante().vlIndiceFinanceiro >= 0) {
    			vlIndiceFinanceiro = SessionLavenderePda.getRepresentante().vlIndiceFinanceiro;
    		} else {
    			vlIndiceFinanceiro = ValueUtil.getDoubleSimpleValue(RepresentanteService.getInstance().findColumnByRowKey(SessionLavenderePda.getRepresentante().getRowKey(), Representante.NMCOLUNA_VLINDICEFINANCEIRO));
    			SessionLavenderePda.getRepresentante().vlIndiceFinanceiro = vlIndiceFinanceiro;
    		}
			vlIndiceFinanceiro = vlIndiceFinanceiro > 0 ? vlIndiceFinanceiro : 1;
			itemPedido.vlBaseItemPedido = roundUnidadeAlternativa(itemPedido.vlBaseItemPedido * vlIndiceFinanceiro);
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
			itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
			itemPedido.vlUnidadePadrao  = roundUnidadeAlternativa(itemPedido.vlUnidadePadrao * vlIndiceFinanceiro);
		}
    }

	public void sortListByGondola(Vector list, boolean sortAsc) {
		int size = list.size();
		Vector listQtGondola = new Vector();
		Vector listQtFisico = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) list.items[i];
			if (itemPedido.isGondola() && itemPedido.getQtItemFisico() == 0) {
				listQtGondola.addElement(itemPedido);
			} else {
				listQtFisico.addElement(itemPedido);
			}
		}
		SortUtil.qsortString(listQtFisico.items, 0, listQtFisico.size() - 1, sortAsc);
		SortUtil.qsortString(listQtGondola.items, 0, listQtGondola.size() - 1, sortAsc);
		list.items = VectorUtil.concatVectors(listQtFisico, listQtGondola).items;
		if (!sortAsc) {
			list.reverse();
		}
	}
    
    public ItemPedido inicializaItemPedidoVenda(Pedido pedido, Produto produto, String cdTabelaPreco, double qtItemCombo, String cdCombo) throws SQLException {
    	ItemPedido itemPedido = getItemPedidoFromCombo(pedido, produto);
	    itemPedido.cdCombo = cdCombo;
    	itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + qtItemCombo);
    	if (ValueUtil.isEmpty(pedido.cdTabelaPreco)) {
    		itemPedido.cdTabelaPreco = cdTabelaPreco;
    	} else {
    		itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
    	}
    	itemPedido.getItemTabelaPreco();
    	PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
    	return itemPedido;
    }

	private ItemPedido getItemPedidoFromCombo(Pedido pedido, Produto produto) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
				if (itemPedido.cdProduto.equals(produto.cdProduto)) {
					itemPedido.itemComboInserido = true;
					return itemPedido;
				}
			}
		}
		ItemPedido itemPedido = new ItemPedido();
    	itemPedido.cdEmpresa = pedido.cdEmpresa;
    	itemPedido.cdRepresentante = pedido.cdRepresentante;
    	itemPedido.nuPedido = pedido.nuPedido;
    	itemPedido.flOrigemPedido = pedido.flOrigemPedido;
    	itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
    	itemPedido.nuSeqItemPedido = getNextNuSeqItemPedido(pedido);
    	clearDadosItemPedido(pedido, itemPedido);
    	itemPedido.setProduto(produto);
    	if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
    		if (itemPedido.getProduto().complementar) {
				loadInfosAdicionaisItemComplementar(itemPedido);
			} else if (ValueUtil.isEmpty(itemPedido.flOrigemEscolhaItemPedido)) {
				itemPedido.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_PADRAO;
			}
    	}
    	itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    	itemPedido.cdUnidade = itemPedido.getProduto().cdUnidade;
		return itemPedido;
	}
    
    public void saveItemSugestaoCombo(ItemPedido itemPedido, Pedido pedido) throws SQLException {
    	PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
    	PedidoService.getInstance().atualizaNuSeqProduto(itemPedido);
    	if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
			itemPedido.flPromocional = StringUtil.getStringValue(itemPedido.descPromocional != null && ValueUtil.isNotEmpty(itemPedido.descPromocional.rowKey));
		}
    	validateItemBloqueadoRestrito(pedido, itemPedido);
    	if (itemPedido.itemComboInserido) {
    		PedidoService.getInstance().updateItemPedido(pedido, itemPedido);
    		itemPedido.itemComboInserido = false;
    	} else {
    		PedidoService.getInstance().insertItemPedido(pedido, itemPedido);
    	}
    }

	public void validateItemBloqueadoRestrito(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			RestricaoService.getInstance().validateProdutoRestrito(itemPedido, pedido);
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido);
		}
	}

    public void deleteItensCombo(ItemPedido itemPedido) throws SQLException {
    	Pedido pedido = itemPedido.pedido;
    	if (ItemComboService.getInstance().isItemPedidoPertenceCombo(itemPedido, itemPedido.cdProduto, null, itemPedido.cdCombo)) {
    		int size = pedido.itemPedidoList.size();
    		for (int i = 0; i < size; i++) {
    			ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
    			if (itemPedido.cdCombo.equals(item.cdCombo)) {
    				PedidoService.getInstance().deleteItemPedido(pedido, item);
    				i--;
        			size--;
    			}
    		}
    	} else if (ItemComboService.getInstance().isItemPedidoPertenceCombo(itemPedido, itemPedido.cdProduto, ItemCombo.TIPOITEMCOMBO_SECUNDARIO, itemPedido.cdCombo)) {
    		if (!isPedidoPossuiItensComboSecundario(itemPedido, itemPedido.cdCombo, false, false)) {
    			int size = pedido.itemPedidoList.size();
    			for (int i = 0; i < size; i++) {
    				ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
    				if (itemPedido.cdCombo.equals(item.cdCombo)) {
    					PedidoService.getInstance().deleteItemPedido(pedido, item);
    					i--;
    	    			size--;
    				}
    			}
    		}
    	}
    }
    
    public boolean isPedidoPossuiItensComboSecundario(ItemPedido itemPedido, String cdCombo, boolean avulso, boolean beforeDelete) throws SQLException {
    	boolean possuiItens = ItemPedidoPdbxDao.getInstance().isPedidoPossuiItensComboSecundario(itemPedido, cdCombo, avulso, beforeDelete);
    	if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
    		possuiItens |= ItemPedidoPdbxDao.getInstance().isPedidoPossuiItemSimilarSecundario(itemPedido, cdCombo, beforeDelete);
    	}
    	return possuiItens;
    }
    
    public void deleteItensComboVencidos(Pedido pedido, Vector listItensVencidos) throws SQLException {
    	Vector itemPedidoList = pedido.itemPedidoList;
    	int size = itemPedidoList.size();
    	for (int i = 0; i < size; i++) {
    		ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
			if (isItemPedidoVencido(itemPedido, listItensVencidos)) {
    			PedidoService.getInstance().deleteItemPedido(pedido, itemPedido);
    			i--;
    			size--;
    		}
    	}
    }
    
    private boolean isItemPedidoVencido(ItemPedido itemPedido, Vector listItensVencidos) {
    	int size = listItensVencidos.size();
    	for (int i = 0; i < size; i++) {
    		ItemCombo itemCombo = (ItemCombo) listItensVencidos.items[i];
    		if (itemPedido.cdProduto.equals(itemCombo.cdProduto)) {
    			listItensVencidos.removeElementAt(i);
    			return true;
    		}
    	}
    	return false;
    }
    
    public int isPedidoPossuiItemComboAvulso(Pedido pedido, String cdCombo, Vector listItensSecundarioSelecionados, Vector listItensSelecionados) throws SQLException {
		ItemPedido itemPedido = createNewItemPedido(pedido);
		itemPedido.cdCombo = cdCombo;
		return ItemPedidoPdbxDao.getInstance().isPedidoPossuiItensComboAvulsos(itemPedido, cdCombo, false, listItensSecundarioSelecionados, listItensSelecionados);
	}
    
    public Vector getCdClasseListByPedido(Pedido pedido) throws SQLException {
    	return getItemPedidoPdbxDao().findCdClasseListByPedido(pedido);
    }

	protected Map<String, Double> getQtItensByCdClasseNoPedido(Pedido pedido) throws SQLException {
		return getItemPedidoPdbxDao().getQtItensByCdClasseNoPedido(pedido);
	}

	public void updateVlPctComissao(ItemPedido itemPedido) throws SQLException {
		updateColumn(itemPedido.getRowKey() , "VLPCTCOMISSAO", itemPedido.vlPctComissao, Types.DECIMAL);
	}

	public Vector findProdutosPendentesByItemPedido(ItemPedido itemPedido) throws SQLException {
		return getItemPedidoPdbxDao().findProdutosPendentesByItemPedido(itemPedido);
	}

	public void updateCdProdutoClienteCodigoItemPedido(ItemPedido itemPedido) throws SQLException {
		updateColumn(itemPedido.getRowKey() , "CDPRODUTOCLIENTECOD", itemPedido.cdProdutoClienteCod, Types.VARCHAR);
	}

	public void updateCdProdutoClienteItensPedido(String cdCliente, ItemPedido itemPedidoFilter) throws SQLException {
		ItemPedidoPdbxDao.getInstance().updateCdProdutoClienteItensPedido(cdCliente, itemPedidoFilter);
	}

	public double getVlPctAcrescimoPoliticaComercial(double vlPctAcrescimoCapaPedido, ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaPoliticaComercial()) return vlPctAcrescimoCapaPedido;
		
		if (itemPedido.politicaComercial == null) return vlPctAcrescimoCapaPedido;
		
		double vlPctMaxAcrescimo = itemPedido.politicaComercial.vlPctPoliticaComercialMin < 0 ? itemPedido.politicaComercial.vlPctPoliticaComercialMin*-1 : 0;
		return vlPctAcrescimoCapaPedido > vlPctMaxAcrescimo ? vlPctMaxAcrescimo : vlPctAcrescimoCapaPedido;
	}
	
	public double getVlPctMaxDescontoPoliticaComercial(double vlPctMaxDescontoItem, ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaPoliticaComercial()) return vlPctMaxDescontoItem;
		
		if (itemPedido.politicaComercial == null) return vlPctMaxDescontoItem;
		
		return itemPedido.politicaComercial.vlPctPoliticaComercialMax > 0 ? itemPedido.politicaComercial.vlPctPoliticaComercialMax : 0;
	}

	public double getVlPctMaxAcrescimoPoliticaComercial(ItemPedido itemPedido) {
		if (!LavenderePdaConfig.usaPoliticaComercial()) return -1d;
		
		if (itemPedido.politicaComercial == null) return -1d;
		
		return Math.abs(itemPedido.politicaComercial.vlPctPoliticaComercialMin) > 0 ? Math.abs(itemPedido.politicaComercial.vlPctPoliticaComercialMin) : -1;
	}

	public void beforeSave(ItemPedido itemPedido, boolean isEditing, boolean fromRelGiroProduto, boolean fromRelProdutosPendentes) throws SQLException {
		updateNuSeqProdutoItemPedido(itemPedido, isEditing);
		if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
			itemPedido.flPromocional = StringUtil.getStringValue(itemPedido.descPromocional != null && ValueUtil.isNotEmpty(itemPedido.descPromocional.rowKey));
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			marcaItemPedidoPendente(itemPedido);
			if (itemPedido.isPendente()) {
				if (itemPedido.pedido.onReplicacao) {
					itemPedido.dsMotivoItemPendentePedido = itemPedido.vlPctDescAlcada > 0 ? Messages.ITEMPEDIDO_MSG_PENDENTE_ALCADA_RESUMIDO : Messages.ITEMPEDIDO_MSG_PENDENTE_RESUMIDO;
				} else {
					UiUtil.showWarnMessage(itemPedido.vlPctDescAlcada > 0 ? Messages.ITEMPEDIDO_MSG_PENDENTE_ALCADA : Messages.ITEMPEDIDO_MSG_PENDENTE);
				}
			}
		} else {
			marcaItemPedidoPorMotivoPendencia(itemPedido);
		}
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			if(fromRelGiroProduto) {
				itemPedido.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_GIROPRODUTO;
			} else if (fromRelProdutosPendentes) {
				itemPedido.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_NAO_INSERIDOS;
			}
		}
	}

	private void updateNuSeqProdutoItemPedido(ItemPedido itemPedido, boolean isEditing) throws SQLException {
		if (!isEditing || itemPedido.isBonificandoItemPeloBotao) {
			PedidoService.getInstance().atualizaNuSeqProduto(itemPedido);
		}
	}
	public void updateItemPedidoByItemKit(Pedido pedido, Vector itensGrid, Kit kit, int qtKit) throws SQLException {
		int size = itensGrid.size();
		Vector itensPedidoUpdateList = new Vector(size);
		for (int i = 0; i < size; i++) {
			String[] itemKitArray = (String[]) itensGrid.items[i];
			ItemKit itemKit = new ItemKit();
			int posicaoDesconto = 4;
			itemKit.cdEmpresa = pedido.cdEmpresa;
			itemKit.cdRepresentante = pedido.cdRepresentante;
			itemKit.cdKit = LavenderePdaConfig.isUsaKitProdutoFechado() ? kit.cdKit : null;
			itemKit.cdProduto = itemKitArray[1];
			itemKit.qtMinItem = qtKit > 0 ? ValueUtil.getDoubleValue(itemKitArray[3]) * qtKit : ValueUtil.getDoubleValue(itemKitArray[3]);
			if(LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto()) {
				itemKit.cdUnidade = itemKitArray[4];
				posicaoDesconto = 5;
			}
			itemKit.vlPctDesconto = LavenderePdaConfig.isUsaKitProdutoFechado() ? ValueUtil.getDoubleValue(itemKitArray[posicaoDesconto]) : 0;
			
			itemKit.kit = kit;
			ItemPedido itemPedido = itemKitToItemPedido(pedido, getItemPedidoByItemKit(pedido.itemPedidoList, itemKit), itemKit);
			PedidoService.getInstance().validateEstoqueParaItemKitFechado(pedido, itemPedido);
			itensPedidoUpdateList.addElement(itemPedido);
		}
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itensPedidoUpdateList.items[i];
			update(itemPedido);
		}
	}

	private ItemPedido getItemPedidoByItemKit(Vector itemPedidoList, ItemKit itemKit) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
			if (itemKit.cdEmpresa.equals(itemPedido.cdEmpresa) && itemKit.cdRepresentante.equals(itemPedido.cdRepresentante)
					&& itemKit.cdProduto.equals(itemPedido.cdProduto) && itemKit.cdKit.equals(itemPedido.cdKit)
					&& (!LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto() || itemKit.cdUnidade.equals(itemPedido.cdUnidade))) {
				return itemPedido;
			}
		}
		return null;
	}
		
	public boolean isPossuiProdutosDescQtdVencidos(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().isPossuiProdutosDescQtdVencidos(pedido);
	}
	
	public Vector findCdProdutosDescQtdVencidos(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().findCdProdutosDescQtdVencidos(pedido);
	}
	
	public Vector filtraItemPedidoListMarcador(Vector itemPedidoList, Vector marcadorList) throws SQLException {
		int size = itemPedidoList.size();
		Vector filterList = new Vector(size);
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			Vector marcadores = itemPedido.getProduto().cdMarcadores;
			if (ValueUtil.isNotEmpty(marcadores) && possuiMarcadorSelecionado(marcadorList, marcadores)) {
				filterList.addElement(itemPedido);
			}
		}
		return filterList;
	}
	
	private boolean possuiMarcadorSelecionado(Vector marcadoresSelecionados, Vector marcadorList) {
		int size = marcadorList.size();
		for (int i = 0; i < size; i++) {
			if (marcadoresSelecionados.contains(marcadorList.items[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean isPossuiItensDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().isPossuiItensDescProgressivoPersonalizadoExtrapolados(pedido);
	}

	public Vector findCdProdutosDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().findCdProdutosDescProgressivoPersonalizadoExtrapolados(pedido);
	}

	public int getMaxNuOrdemLiberacaoItemPendente() throws SQLException {
		return ItemPedidoPdbxDao.getInstanceErp().getMaxNuOrdemLiberacaoItemPendente();
	}
	
	public void updateFlPedidoPerdidoByPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		itemPedido.flPedidoPerdido = ValueUtil.VALOR_SIM;
		ItemPedidoPdbxDao.getInstance().updateFlPedidoPerdidoByPedido(itemPedido);
	}
	
	public void deleteItensKit(String cdKit, Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		Vector itemPedidoKitList = new Vector();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoKit = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEquals(cdKit, itemPedidoKit.cdKit)) {
				itemPedidoKitList.addElement(itemPedidoKit);
			}
		}
		size = itemPedidoKitList.size();
		for (int i = 0; i < size; i++) {
			PedidoService.getInstance().deleteItemPedido(pedido, (ItemPedido) itemPedidoKitList.items[i]);
		}
	}
	
	public void marcaFlAutorizadoItem(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		Vector solAutorizacaoList = SolAutorizacaoService.getInstance().findSolAutorizacaoItemsByPedido(pedido);
		int size2 = solAutorizacaoList.size();
		if (size2 > 0) {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
				String cdProduto = ((ItemPedido)itemPedidoList.items[i]).cdProduto;
				SolAutorizacao solAutorizacao = getSolAutorizacao(solAutorizacaoList, cdProduto, size2);
				if (solAutorizacao != null) {
					itemPedido.flAutorizado = solAutorizacao.flAutorizado;
					updateColumn(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_FLAUTORIZADO, itemPedido.flAutorizado, Types.VARCHAR);
				}
			}
		}
	}
	
	private SolAutorizacao getSolAutorizacao(Vector solAutorizacaoList, String cdProduto, int size) {
		for (int i = 0; i < size; i++) {
			SolAutorizacao solAutorizacao = (SolAutorizacao) solAutorizacaoList.items[i];
			if (ValueUtil.valueEquals(cdProduto, solAutorizacao.cdProduto)) return solAutorizacao;
		}
		return null;
	}
	
	public void limpaFlAutorizadoItem(Pedido pedido) throws SQLException {
		ItemPedido filter = createSimpleNewItemPedido(pedido);
		ItemPedidoPdbxDao.getInstance().updateLimpaFlAutorizadoItemPedido(filter);
	}

	public boolean validaQtItemBonificado(ItemPedido itemBonificado) throws SQLException {
		if (LavenderePdaConfig.getPercMaxQuantidadeBonificada() == 0) return true;
		int size = itemBonificado.pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemBonificado.pedido.itemPedidoList.items[i];
			if (ValueUtil.valueEquals(itemPedido.cdProduto, itemBonificado.cdProduto) && !itemPedido.isItemBonificacao() && itemBonificado.isItemBonificacao()) {
				double limitacao = getLimitacaoItemBonificado(itemPedido);
				if (limitacao < itemBonificado.getQtItemFisico()) {
					String[] args = {StringUtil.getStringValue((int) limitacao), StringUtil.getStringValue(LavenderePdaConfig.getPercMaxQuantidadeBonificada())};
					UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_NAO_PERMITE_BONIFICAR_ESSAQTD, args));
					return false;
				}
			}
		}
		return true;
	}

	public boolean validaQtItemBonificadoVinculado(ItemPedido itemPedido, double qtItemFisicoChange) throws SQLException {
		if (LavenderePdaConfig.getPercMaxQuantidadeBonificada() == 0) return true;
		int size = itemPedido.pedido.itemPedidoList.size();
		if (size == 1) return true;
		itemPedido.setQtItemFisico(qtItemFisicoChange);
		for (int i = 0; i < size; i++) {
			ItemPedido itemBonificado = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			if (ValueUtil.valueEquals(itemBonificado.cdProduto, itemPedido.cdProduto) && itemBonificado.isItemBonificacao() && !itemPedido.isItemBonificacao()) {
				double limitacao = getLimitacaoItemBonificado(itemPedido);
				if (limitacao < itemBonificado.getQtItemFisico()) {
					if (limitacao > 0) {
						String[] args = {StringUtil.getStringValue((int) limitacao), StringUtil.getStringValue((int) itemBonificado.getQtItemFisico())};
						UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITEMVENDA_INCOMPATIVEL_ITEMBONIFICADO, args));
						itemBonificado.setQtItemFisico((int) limitacao);
						itemBonificado.vlTotalItemPedido = itemBonificado.getVlTotalItem();
						update(itemBonificado);
					} else {
						UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MSG_QTITEMVENDA_NAOPERMITE_ITEMBONIFICADO);
						PedidoService.getInstance().deleteItemPedido(itemBonificado.pedido, itemBonificado);
					}
					return false;
				}
			}
		}
		return true;
	}

	private double getLimitacaoItemBonificado(ItemPedido itemPedido) throws SQLException {
		double limitacao;
		if (LavenderePdaConfig.getPercMaxQuantidadeBonificada() == 0) {
			limitacao = itemPedido.getQtItemFisico();
			if (itemPedido.getQtItemFisico() == itemPedido.getNuConversaoUnidade()) limitacao = 0;
		} else {
			limitacao = itemPedido.getQtItemFisico() * (LavenderePdaConfig.getPercMaxQuantidadeBonificada() / 100);
			double nuConversao = itemPedido.getNuConversaoUnidade();
			if (nuConversao == 0) {
				nuConversao = 1;
			}
			double resto = limitacao % nuConversao;
			if (resto > 0) limitacao -= resto;
			if (limitacao < itemPedido.getNuConversaoUnidade()) limitacao = 0;
		}
		return limitacao;
	}

	public Vector findAllByExamplePedidoComBonificacao(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedido) domain).flOrigemPedido)) {
			return ItemPedidoPdbxDao.getInstance().findAllByExamplePedidoComBonificacao(domain);
		} else {
			return ItemPedidoPdbxDao.getInstanceErp().findAllByExamplePedidoComBonificacao(domain);
		}
	}

	public boolean isPermiteBonificarItem(ItemPedido itemPedido) throws SQLException {
		return itemPedido != null && (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao
				|| LavenderePdaConfig.isPermiteBonificarQualquerProduto()
				|| LavenderePdaConfig.isUsaPoliticaBonificacao() && !LavenderePdaConfig.usaConfigBonificacaoItemPedido()
				|| itemPedido.getProduto() != null && itemPedido.getProduto().isPermiteBonificacao());
	}

	public boolean isPedidoNaoPossuiEsseItemBonificado(ItemPedido itemPedido, Pedido pedido) {
		if (pedido == null) return false;
		
		ItemPedido itemPedidoBonificado = (ItemPedido)itemPedido.clone();
		itemPedidoBonificado.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		if (LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao()) {
			return isPedidoNaoPossuiItemBonificadoKit(pedido, itemPedidoBonificado);
		}
		return !pedido.itemPedidoList.contains(itemPedidoBonificado);
	}

	private boolean isPedidoNaoPossuiItemBonificadoKit(Pedido pedido, ItemPedido itemPedidoBonificado) {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.isKitTipo3() && TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedido.flTipoItemPedido) && ValueUtil.valueEquals(itemPedido.cdProduto, itemPedidoBonificado.cdProduto)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isPedidoPossuiEsseItemNaoBonificado(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigBonificacaoItemPedido() || pedido == null) return false;
		
		return ItemPedidoPdbxDao.getInstance().findByPrimaryKeyWhereNotTipoPedidoBonificado(itemPedido) != null;
	}

	public boolean isItemBonificadoNoPedido(ItemPedido itemPedido) throws SQLException {
		int size = itemPedido.pedido.itemPedidoList.size();
		if (size == 1) return false;
		ItemPedido itemBoni = (ItemPedido) itemPedido.clone();
		itemBoni.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		return itemPedido.pedido.itemPedidoList.contains(itemBoni);
	}

	public void atualizaInfoNegociacaoItemBonificado(ItemPedido itemPedido) throws SQLException {
		if (itemPedido == null) return;
		int size = itemPedido.pedido.itemPedidoList.size();
		if (size == 1) return;
		for (int i = 0; i < size; i++) {
			ItemPedido itemBonificado = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			if (!itemBonificado.isKitTipo3() && ValueUtil.valueEquals(itemPedido.cdProduto, itemBonificado.cdProduto) && itemBonificado.isItemBonificacao() && !itemPedido.isItemBonificacao()) {
				if (itemPedido.vlItemPedido > 0) {
					itemBonificado.vlItemPedido = itemPedido.vlItemPedido;
					itemBonificado.vlTotalItemPedido = ValueUtil.round(itemBonificado.vlItemPedido * itemBonificado.getQtItemFisico());
				}
				if (itemPedido.vlPctAcrescimo >= 0){
					itemBonificado.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
				}
				if (itemPedido.vlPctDesconto >= 0) {
					itemBonificado.vlPctDesconto = itemPedido.vlPctDesconto;
				}
				update(itemBonificado);
			}
		}

	}

	public boolean isQtdPermiteBonificar(ItemPedido itemPedido) throws SQLException {
		if(!LavenderePdaConfig.isPermiteBonificarApenasProdutosInseridosNoPedido()) return true;

		return getLimitacaoItemBonificado(itemPedido) > 0;
	}

	public boolean isPermiteBonificarApenasProdutosInseridosNoPedido(ItemPedido itemPedido) {
		if(!LavenderePdaConfig.isPermiteBonificarApenasProdutosInseridosNoPedido()) return true;

		return LavenderePdaConfig.isPermiteBonificarApenasProdutosInseridosNoPedido() && itemPedido.getQtItemFisico() > 0;
	}

	public boolean deleteItemBonificadoVinculado(ItemPedido itemPedido) throws SQLException {
		int size = itemPedido.pedido.itemPedidoList.size();
		if (size == 1) return true;
		if (itemPedido != null) {
			ItemPedido itemBoni = (ItemPedido) itemPedido.clone();
			itemBoni.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
			if(itemPedido.pedido.itemPedidoList.contains(itemBoni)) {
				if (UiUtil.showConfirmYesNoMessage(Messages.ITEMPEDIDO_MSG_CONFIRMA_EXCLUSAO_ITEMBONIFICADO)) {
					PedidoService.getInstance().deleteItemPedido(itemPedido.pedido, itemBoni);
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public void updateItemAutorizadoAgrupador(ItemPedido itemPedido) throws SQLException {
		itemPedido.flAgrupadorSimilaridade = ValueUtil.VALOR_SIM;
		itemPedido.cdAgrupadorSimilaridade = itemPedido.getProduto().cdAgrupadorSimilaridade;
		ItemPedidoPdbxDao.getInstance().updateItemAutorizadoAgrupador(itemPedido);
	}
	
	public void updateQtItemFisicoSimilar(ItemPedido itemPedido, double qtItemFisico) throws SQLException {
		itemPedido.setQtItemFisico(qtItemFisico);
		updateColumn(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_QTITEMFISICO, itemPedido.getQtItemFisicoOrg(), Types.DECIMAL);
	}
	
	private String getCdUnidadeItemKit(ItemPedido itemPedido, ItemKit itemKit) throws SQLException {
		if (!LavenderePdaConfig.usaUnidadeAlternativa) {
			return itemPedido.getProduto().cdUnidade;
			
		} else if (LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto()) {
			return ValueUtil.isNotEmpty(itemKit.cdUnidade) ? itemKit.cdUnidade : itemPedido.getProduto().cdUnidade;
			
		} else {
			return null;
		}
		}

	public Vector getItemPedidoListAgrupadorGrade(Vector itemPedidoList, String dsAgrupadorGrade) throws SQLException {
		int size = itemPedidoList.size();
		Vector itemPedidoAgrupadorGradeList = new Vector(size);
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (dsAgrupadorGrade.equals(itemPedido.getProduto().getDsAgrupadorGrade())) {
				itemPedidoAgrupadorGradeList.addElement(itemPedido);
			}
		}
		return itemPedidoAgrupadorGradeList;
	}
	
	public double sumVlTotalItemAgrupadorGrade(Pedido pedido, String dsAgrupadorGrade) throws SQLException {
		ItemPedido filter = createSimpleNewItemPedido(pedido);
		filter.dsAgrupadorGradeFilter = dsAgrupadorGrade;
		return ItemPedidoPdbxDao.getInstance().sumVlTotalItemAgrupadorGrade(filter);
	}
	
	public boolean setaVlItemGradeAgrupador(boolean hasProdutoGradeComVlPermitido, double vlItemGrade, ItemPedido itemPedidoPorGradePreco) throws SQLException {
		itemPedidoPorGradePreco.vlItemPedido = vlItemGrade;
		if (itemPedidoPorGradePreco.vlPctDesconto < 0) itemPedidoPorGradePreco.vlPctDesconto = 0d;
		if (itemPedidoPorGradePreco.vlPctAcrescimo < 0) itemPedidoPorGradePreco.vlPctAcrescimo = 0d;
		try {
			validateDescAcresMax(itemPedidoPorGradePreco);
		} catch (DescAcresMaximoException e) {
			hasProdutoGradeComVlPermitido = true;
			itemPedidoPorGradePreco.vlItemPedido = e.vlPermitido;
			itemPedidoPorGradePreco.vlPctDesconto = calculaVlPctDesconto(itemPedidoPorGradePreco.vlBaseItemPedido, itemPedidoPorGradePreco.vlItemPedido);
			itemPedidoPorGradePreco.vlPctAcrescimo = calculaVlPctAcrescimo(itemPedidoPorGradePreco.vlBaseItemPedido, itemPedidoPorGradePreco.vlItemPedido);
		}
		if (itemPedidoPorGradePreco.vlPctDesconto < 0) itemPedidoPorGradePreco.vlPctDesconto = 0d;
		if (itemPedidoPorGradePreco.vlPctAcrescimo < 0) itemPedidoPorGradePreco.vlPctAcrescimo = 0d;
		return hasProdutoGradeComVlPermitido;
	}
	
	public void validateDescAcresMax(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.politicaComercial != null && LavenderePdaConfig.isValidaPctPoliticaComercial()) {
			validaVlPctMaxMinPoliticaComercial(itemPedido);
			return;
		}
		validaDescontoMaximoPermitido(itemPedido);
		validaAcrescimoMaximoPermitido(itemPedido);
	}
	
	public void enviaAtualizacao(ItemPedido itemPedido)  {
		try {
			UiUtil.showProcessingMessage();
			if (SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado() && itemPedido != null) {
				EnviaDadosThread.getInstance().enviaItemPedidoBackground(itemPedido);
				return;
			}
			if (!SyncManager.isConexaoPdaDisponivel()) {
				return;
			}
			SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), new Hashtable(0));
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}
	
	private boolean isIgnoraDescontoMaximoEmValor(ItemTabelaPreco itemTabPreco) {
		return LavenderePdaConfig.isIgnoraVlMaximoDesconto() && itemTabPreco.vlMinItemPedido > 0;
	}
	
	public void aplicaDeflatorCondPagtoItemPedido(ItemPedido itemPedido, CondicaoPagamento condPagto) throws SQLException {
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorItensFormulaJuros()) {
			if (itemPedido.getProduto().isFormaCalculoDeflatorJurosSimples()) {									
				double newVlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 + condPagto.qtDiasMediosPagamento * itemPedido.getProduto().vlPctJurosMensal / 30 / 100));
				itemPedido.vlBaseItemPedido = newVlBaseItemPedido;
				itemPedido.vlItemPedido = newVlBaseItemPedido;
			} else if (itemPedido.getProduto().isFormaCalculoDeflatorJurosComposto() && condPagto.qtDiasMediosPagamento > itemPedido.getProduto().qtDiasCarenciaJuros) {
				double newVlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * Math.pow((1 + itemPedido.getProduto().vlPctJurosMensal / 30 / 100), (condPagto.qtDiasMediosPagamento - itemPedido.getProduto().qtDiasCarenciaJuros)));
				itemPedido.vlBaseItemPedido = newVlBaseItemPedido;
				itemPedido.vlItemPedido = newVlBaseItemPedido;
			}
		}
	}
	
	public ItemPedido createAndCalculateNewItemPedidoBase(final ItemPedido itemPedidoReference, final Produto produto, final String cdItemGrade1) throws SQLException {
		ItemPedido itemPedido = createNewItemPedidoVendaBase(itemPedidoReference, produto, cdItemGrade1);
		PedidoService.getInstance().loadValorBaseItemPedido(itemPedido.pedido, itemPedido);
		return itemPedido;
	}
	
	private ItemPedido createNewItemPedidoVendaBase(final ItemPedido itemPedidoReference, Produto produto, String cdItemGrade1) throws SQLException {
		ItemPedido itemPedido = new ItemPedido(itemPedidoReference.pedido);
		itemPedido.pedido = itemPedidoReference.pedido;
		itemPedido.setProduto(produto == null ? new Produto() : produto);
		itemPedido.cdTabelaPreco = itemPedidoReference.cdTabelaPreco;
		itemPedido.cdUfClientePedido = itemPedidoReference.pedido.getCliente().dsUfPreco;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.nuSeqItemPedido = getNextNuSeqItemPedido(itemPedidoReference.pedido);
		itemPedido.cdItemGrade1 = cdItemGrade1;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdUnidade = itemPedido.getProduto().cdUnidade;

		return itemPedido;
	}

	public void preparaItemPedidoBonificacao(ItemPedido itemPedidoBonificado, double qtItemFisico, boolean onInsert) throws SQLException {
		itemPedidoBonificado.isInserindoItemPoliticaBonificacao = true;
		if (onInsert) {
			if (!LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao()) {
			itemPedidoBonificado.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
			}
			itemPedidoBonificado.nuSeqItemPedido = itemPedidoBonificado.pedido.itemPedidoList.size() == 0 ? 2 : getNextNuSeqItemPedido(itemPedidoBonificado.pedido);
		}
		itemPedidoBonificado.qtItemFisico = qtItemFisico;
		itemPedidoBonificado.dsProduto = itemPedidoBonificado.getProduto().dsProduto;
		calculate(itemPedidoBonificado, itemPedidoBonificado.pedido);
		itemPedidoBonificado.isInserindoItemPoliticaBonificacao = false;
	}

	public void deleteItemPedidoBonificadoAutomaticamenteNaExclusaoItemPedidoVenda(ItemPedido itemPedidoVenda) throws SQLException {
		Vector bonifCfgList = itemPedidoVenda.getBonifCfgList();
		BonifCfg bonifCfg;
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = ItemPedidoBonifCfgService.getInstance().getItemPedidoBonifCfgFilterBonificacoesAutomaticas(itemPedidoVenda);
		for (int i = 0; i < bonifCfgList.size(); i++) {
			bonifCfg = (BonifCfg) bonifCfgList.elementAt(i);
			if (bonifCfg.isBonificacaoAutomatica()) {
				deleteBonificacaoAutomatica(itemPedidoVenda, bonifCfg, itemPedidoBonifCfgFilter);
			} else if (bonifCfg.isTipoRegraValor()) {
				ItemPedidoBonifCfg itemPedBonCfgFilter = new ItemPedidoBonifCfg(itemPedidoVenda);
				itemPedBonCfgFilter.cdBonifCfg = bonifCfg.cdBonifCfg;
				itemPedBonCfgFilter.flTipoItemPedido = null;
				ItemPedidoBonifCfgService.getInstance().deleteAllByExample(itemPedBonCfgFilter);
			}
		}
	}

	private void deleteCreditosBonificacaoAutomatica(ItemPedido itemPedidoVenda, String cdBonifCfg) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgCreditoFilter = new ItemPedidoBonifCfg(itemPedidoVenda);
		itemPedidoBonifCfgCreditoFilter.flBonificacaoAutomatica = ValueUtil.VALOR_SIM;
		itemPedidoBonifCfgCreditoFilter.cdBonifCfg = cdBonifCfg;
		ItemPedidoBonifCfgService.getInstance().deleteAllByExample(itemPedidoBonifCfgCreditoFilter);
	}

	public void deleteBonificacaoAutomatica(ItemPedido itemPedidoVenda, BonifCfg bonifCfg, ItemPedidoBonifCfg itemPedidoBonifCfgFilter) throws SQLException {
		itemPedidoBonifCfgFilter.cdBonifCfg = bonifCfg.cdBonifCfg;
		ItemPedidoBonifCfg itemPedidoBonifCfgEncontrado = (ItemPedidoBonifCfg) ItemPedidoBonifCfgService.getInstance().findAllByExample(itemPedidoBonifCfgFilter).elementAt(0);
		if (itemPedidoBonifCfgEncontrado == null) {
			ItemPedidoBonifCfg itemPedidoBonifCfgFilterClone = (ItemPedidoBonifCfg) itemPedidoBonifCfgFilter.clone();
			itemPedidoBonifCfgFilterClone.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			itemPedidoBonifCfgFilterClone.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_CREDITO;
			itemPedidoBonifCfgEncontrado = (ItemPedidoBonifCfg) ItemPedidoBonifCfgService.getInstance().findAllByExample(itemPedidoBonifCfgFilterClone).elementAt(0);
			if (itemPedidoBonifCfgEncontrado == null) return;
		}

		ItemPedido itemPedidoBonificadoAutomatico = (ItemPedido) findByRowKey(itemPedidoBonifCfgEncontrado.getPrimaryKeyItemPedido());
		
		if (itemPedidoBonificadoAutomatico != null) {
			itemPedidoBonificadoAutomatico.pedido = itemPedidoVenda.pedido;
			if (itemPedidoBonificadoAutomatico.getQtItemFisico() > itemPedidoBonifCfgEncontrado.qtBonificacao) {
				double newQtItemFisico = itemPedidoBonificadoAutomatico.getQtItemFisico() - itemPedidoBonifCfgEncontrado.qtBonificacao;
				ItemPedidoBonifCfgService.getInstance().atualizaItemPedidoBonificacaoAutomaticaOnDelete(newQtItemFisico, itemPedidoBonificadoAutomatico);
			} else {
				delete(itemPedidoBonificadoAutomatico);
				itemPedidoVenda.pedido.itemPedidoList.removeElement(itemPedidoBonificadoAutomatico);
			}
		}
		ItemPedidoBonifCfgService.getInstance().delete(itemPedidoBonifCfgEncontrado);
		deleteCreditosBonificacaoAutomatica(itemPedidoVenda, bonifCfg.cdBonifCfg);
		atualizaMapControleRegrasPoliticaOnDelete(itemPedidoVenda, itemPedidoBonifCfgEncontrado);
	}

	private void atualizaMapControleRegrasPoliticaOnDelete(ItemPedido itemPedidoVenda, ItemPedidoBonifCfg itemPedidoBonifCfgEncontrado) {
		BonifCfgFaixaQtde bonifCfgFaixaQtde = itemPedidoVenda.pedido.bonifCfgFaixaQtdeAtualMap.get(itemPedidoBonifCfgEncontrado.cdBonifCfg);
		if (bonifCfgFaixaQtde != null) {
			bonifCfgFaixaQtde.deleted = true;
			itemPedidoVenda.pedido.bonifCfgFaixaQtdeDeletedMap = new HashMap<>();
			itemPedidoVenda.pedido.bonifCfgFaixaQtdeDeletedMap.put(itemPedidoBonifCfgEncontrado.cdBonifCfg, bonifCfgFaixaQtde);
		}
	}

	public void setCdUnidadeItemPedido(ItemPedido itemPedido, Pedido pedido, Produto itemProduto, String cdUnidadeUnidadeAlternativa) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && (!(LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens() && pedido.isPermiteInserirMultiplosItensPorVezNoPedido() &&
				(!LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() || !LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()))) && !ProdutoUnidade.CDUNIDADE_PADRAO.equals(cdUnidadeUnidadeAlternativa)) {
			itemPedido.cdUnidade = cdUnidadeUnidadeAlternativa;
		} else {
			itemPedido.cdUnidade = itemProduto.cdUnidade;
		}
	}
	private void aplicaJurosComBaseDiasCondPagto(ItemPedido itemPedido, CondicaoPagamento condPagto) throws SQLException {
		double vlBaseAntesDeflator = itemPedido.vlBaseItemPedido;
		aplicaDeflatorCondPagtoItemPedido(itemPedido, condPagto);
		
		if (vlBaseAntesDeflator == itemPedido.vlBaseItemPedido) {
			applyIndiceFinanceiroCondPagtoPorDias(itemPedido, condPagto);
		}
	}
	public ItemPedido findByPrimaryKeyAndCdKit(ItemPedido itemPedido) throws SQLException {
		return (ItemPedido) ItemPedidoPdbxDao.getInstance().findByPrimaryKeyAndCdKit(itemPedido);	
	}
	
	public boolean possuiPontuacaoProduto(ItemPedido itemPedido) throws SQLException {
		PontuacaoProduto pontuacao = new PontuacaoProduto();
		pontuacao.cdEmpresa = itemPedido.cdEmpresa;
		pontuacao.cdRepresentante = itemPedido.cdRepresentante;
		pontuacao.cdPontuacaoConfig = itemPedido.cdPontuacaoConfig;
		pontuacao.cdProduto = itemPedido.cdProduto;
		return PontuacaoProdutoService.getInstance().countByExample(pontuacao) > 0;
	}
	
	private boolean isUsaDescQtdEAcumulaDescontoManualComAFaixaQtd(ItemPedido itemPedido) {
		return LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.descQuantidade != null && !LavenderePdaConfig.usaDescontoPorQuantidadeValor 
				&& !LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta && !LavenderePdaConfig.aplicaDescontoQuantidadeVlBase && !itemPedido.isIgnoraDescQtd();
	}

	public double getVlPctDescontoSemDescQtdFromItemPedido(final ItemPedido itemPedido) {
		if (isUsaDescQtdEAcumulaDescontoManualComAFaixaQtd(itemPedido)) {
			double vlPctDesconto = ValueUtil.round((1 - itemPedido.vlItemPedido / itemPedido.vlBaseItemPedido) * 100 - itemPedido.vlPctFaixaDescQtd);
			if (vlPctDesconto < 0) {
				itemPedido.descQuantidade = null;
				itemPedido.vlPctFaixaDescQtd = 0;
				vlPctDesconto = (1 - itemPedido.vlItemPedido / itemPedido.vlBaseItemPedido) * 100;
			}
			double vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - vlPctDesconto / 100));
			return calculaVlPctDesconto(itemPedido.vlBaseItemPedido, vlItemPedido, false);
		}
		return calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido);
	}

	public String getInfosPersonalizadasItemPedido(ItemPedido itemPedido) throws SQLException {
		JSONObject itemPedidoJsonFilters = new JSONObject(new ItemPedidoDTO().copy(itemPedido));
		HashMap<String, String> infosPersonalizadas = getItemPedidoPdbxDao().getInfosPersonalizadasItemPedido(itemPedidoJsonFilters);
		itemPedido.infosPersonalizadas = infosPersonalizadas.toString();
		StringBuilder stringBuilder = new StringBuilder();
		if (infosPersonalizadas != null) {
			for (Map.Entry<String, String> entry : infosPersonalizadas.entrySet()) {
				String infoPersonalizada = StringUtil.getStringValue(entry.getKey()) + ": " + StringUtil.getStringValue(entry.getValue());
				stringBuilder.append(infoPersonalizada);
			}
		}
		return stringBuilder.toString();
	}

	public void updateNuOrdemCompraClienteItemPedidoByNuOrdemCompraPedido(Pedido pedido) throws SQLException {
		getItemPedidoPdbxDao().updateNuOrdemCompraClienteItemPedidoByNuOrdemCompraPedido(pedido);
	}

	public void validateNuOrdemCompraAndNuSequencialOrdemCompra(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		if (ValueUtil.isEmpty(itemPedido.nuOrdemCompraCliente) && ValueUtil.isEmpty(itemPedido.nuSeqOrdemCompraCliente)) {
			return;
		}
		Vector itemPedidoList = pedido.itemPedidoList;
		if (editing) {
			itemPedidoList.removeElement(itemPedido);
		}
		int size = itemPedidoList.size();
		ItemPedido itemPedidoTemp;
		for (int i = 0; i < size; i++) {
			itemPedidoTemp = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEquals(itemPedido.nuSeqOrdemCompraCliente, itemPedidoTemp.nuSeqOrdemCompraCliente)) {
				if (ValueUtil.isNotEmpty(itemPedido.nuOrdemCompraCliente) && ValueUtil.valueEquals(itemPedidoTemp.nuOrdemCompraCliente, itemPedido.nuOrdemCompraCliente)) {
					String dsProduto = itemPedidoTemp.cdProduto + " - " + itemPedidoTemp.getProduto().dsProduto;
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_SEQUENCIAL_ORDEM_COMPRA_DUPLICADO, new Object[]{dsProduto, itemPedido.nuOrdemCompraCliente, itemPedido.nuSeqOrdemCompraCliente}));
				} else if (ValueUtil.isEmpty(itemPedido.nuOrdemCompraCliente) && ValueUtil.isEmpty(itemPedidoTemp.nuOrdemCompraCliente)) {
					String dsProduto = itemPedidoTemp.cdProduto + " - " + itemPedidoTemp.getProduto().dsProduto;
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_SEQUENCIAL_DUPLICADO_SEM_ORDEM_COMPRA, new Object[]{dsProduto, itemPedido.nuOrdemCompraCliente}));
				}
			} else if (ValueUtil.valueEquals(itemPedidoTemp.cdProduto, itemPedido.cdProduto) && ValueUtil.valueEquals(itemPedidoTemp.nuOrdemCompraCliente, itemPedido.nuOrdemCompraCliente)) {
				String dsProduto = itemPedidoTemp.cdProduto + " - " + itemPedidoTemp.getProduto().dsProduto;
				throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_ITEM_COM_ORDEM_COMPRA_DUPLICADA, new Object[]{dsProduto, itemPedido.nuOrdemCompraCliente}));
			}
		}
	}

	public Vector getItemPedidoDuplicadosOrdemCompraDuplicadaByPedido(Pedido pedido) throws SQLException {
		return ItemPedidoPdbxDao.getInstance().getItemPedidoDuplicadosOrdemCompraDuplicadaByPedido(pedido);
	}
	
	public void clearRentabilidadeItensPedidoByPedido(Pedido pedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			return;
		}
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			itemPedido.vlPctMargemRentab = 0;
			itemPedido.vlBaseMargemRentab = 0;
			itemPedido.vlCustoMargemRentab = 0;
			itemPedido.cdMargemRentab = ValueUtil.VALOR_NI;
			itemPedido.vlPctComissao = 0;
			itemPedido.vlPctComissaoTotal = 0;
			update(itemPedido);
		}
	}

}
