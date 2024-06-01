package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CampoService;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ApplicationWarnException;
import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.exception.NoConnectionAvailableException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ReflectionUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustria;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustriaGeral;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevisto;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.FaceamentoEstoque;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.FuncaoConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoCliente;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAud;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MargemRentab;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.NotaCredito;
import br.com.wmw.lavenderepda.business.domain.NotaCreditoPedido;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import br.com.wmw.lavenderepda.business.domain.RentabilidadePedidoAux;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.ResourcesWmw;
import br.com.wmw.lavenderepda.business.domain.RestricaoProduto;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.TabPrecoGrupoProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TipoRegistro;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import br.com.wmw.lavenderepda.business.domain.dto.ClienteDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ClienteEnderecoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.InfoFretePedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfceDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoAgrSimilarDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoBonifCfgDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoDescontoModificadoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoGradeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemTabelaPrecoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.NfceDTO;
import br.com.wmw.lavenderepda.business.domain.dto.NfeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.NotaCreditoPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.PagamentoPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ParcelaPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoBoletoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoErpDifDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ProdutoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ProdutoGradeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RecalculoDescontoProgressivoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RetornoPedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.TabelaPrecoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.VenctoPagamentoPedidoDto;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.validation.AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException;
import br.com.wmw.lavenderepda.business.validation.ClienteAtrasadoVlTitulosException;
import br.com.wmw.lavenderepda.business.validation.ClienteBloqueadoException;
import br.com.wmw.lavenderepda.business.validation.CondicaoPagamentoDiferentePadraoClienteException;
import br.com.wmw.lavenderepda.business.validation.CreditoDisponivelPedidoException;
import br.com.wmw.lavenderepda.business.validation.DescProgressivoPersonalizadoVigenciaException;
import br.com.wmw.lavenderepda.business.validation.DescontoAcumuladoException;
import br.com.wmw.lavenderepda.business.validation.DescontoCategoriaException;
import br.com.wmw.lavenderepda.business.validation.DescontoMaximoItemException;
import br.com.wmw.lavenderepda.business.validation.DescontoPonderadoPedidoException;
import br.com.wmw.lavenderepda.business.validation.DescontoProgressivoPedidoException;
import br.com.wmw.lavenderepda.business.validation.FreteCalculoPersonalizadoException;
import br.com.wmw.lavenderepda.business.validation.GiroProdutoException;
import br.com.wmw.lavenderepda.business.validation.IemPedidoDuplicadoSemOrdemException;
import br.com.wmw.lavenderepda.business.validation.ItemFechamentoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ItemParticipacaoExtrapoladoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoBloqueadoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoProdutoRestritoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoSemQtItemFisicoGondolaException;
import br.com.wmw.lavenderepda.business.validation.ItensPedidoAbaixoPesoMinimoTabelaPrecoException;
import br.com.wmw.lavenderepda.business.validation.ItensPedidoAbaixoValorMinimoTabelaPrecoException;
import br.com.wmw.lavenderepda.business.validation.JustificativaMotivoPendenciaException;
import br.com.wmw.lavenderepda.business.validation.KitTipo3VigenciaException;
import br.com.wmw.lavenderepda.business.validation.LiberacaoDataEntregaPedidoException;
import br.com.wmw.lavenderepda.business.validation.LimiteCreditoClienteExtrapoladoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ListMultiplasSugestoesProdutosException;
import br.com.wmw.lavenderepda.business.validation.MarcaPendenteItemBonificacaoException;
import br.com.wmw.lavenderepda.business.validation.NaoVendaProdPedidoException;
import br.com.wmw.lavenderepda.business.validation.PedidoNaoFechadoException;
import br.com.wmw.lavenderepda.business.validation.PedidoSemClienteException;
import br.com.wmw.lavenderepda.business.validation.PesquisaMercadoException;
import br.com.wmw.lavenderepda.business.validation.PositivacaoItensByFornecedorException;
import br.com.wmw.lavenderepda.business.validation.ProdutoClienteRelacionadoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoCreditoDescontoException;
import br.com.wmw.lavenderepda.business.validation.ProdutosRelacionadosNaoAtendidosException;
import br.com.wmw.lavenderepda.business.validation.RecalculoPedidoException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosGradesInconsistentesException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosPendentesPedidoException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosRentabilidadeSemAlcadaException;
import br.com.wmw.lavenderepda.business.validation.RentabilidadeMenorMinimaException;
import br.com.wmw.lavenderepda.business.validation.RentabilidadeNegativaException;
import br.com.wmw.lavenderepda.business.validation.ReservaEstoqueException;
import br.com.wmw.lavenderepda.business.validation.SaldoBonificacaoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoItensRentabilidadeIdealException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaComCadastroComQtdPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaComCadastroSemQtdPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaDifPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaPresenteEmOutrasEmpresasPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationGrupoProdutoNaoInseridoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationItemPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMaxPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMinPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMinimoVerbaUltrapassadoException;
import br.com.wmw.lavenderepda.business.validation.ValorMaximoParcelaException;
import br.com.wmw.lavenderepda.business.validation.ValorMinimoLinhaException;
import br.com.wmw.lavenderepda.business.validation.ValorMinimoParcelaException;
import br.com.wmw.lavenderepda.business.validation.VerbaSaldoPedidoConsumidoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FechamentoDiarioDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeReferenciaDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoGradeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabPrecoGrupoProdDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListBonifCfgWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelProdutosPendentesWindow;
import br.com.wmw.lavenderepda.presentation.ui.ValidacaoIemPedidoNuOrdemDuplicadaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ValidacaoValorMinimoLinhaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.report.pdf.PdfReportManager;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.MapUtil;
import totalcross.json.JSONArray;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;
import totalcross.sql.Types;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;
import totalcross.util.regex.Matcher;
import totalcross.util.regex.Pattern;

public class PedidoService extends CrudPersonLavendereService {
	
	private static final String CDCLIENTE = "CDCLIENTE";

	private static PedidoService instance;
	public static int validationFechamentoCount;
	public static int validationFechamentoListCount;
	public Vector pedidoEnvioServidorList = new Vector();
	public boolean ignoreQuantidadeMinimaCaixasPedido;
	
	private PedidoService() {
		//--
	}

	public static PedidoService getInstance() {
		if (instance == null) {
			instance = new PedidoService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return PedidoPdbxDao.getInstance();
	}
	
	public void validateCamposDinamicos(final Pedido pedido) throws java.sql.SQLException {
		super.validate(pedido);
	}

	//@Override
	public void validate(final BaseDomain domain) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		//Dinamicos
		if (!LavenderePdaConfig.usaValidacaoCamposDinamicosObrigatoriosAoFecharPedido) {
			Campo campoDsEmailDestino = getCampoDsEmailDestino(pedido);
			String valorOriginalDoCampo = campoDsEmailDestino.flEditavel;
			campoDsEmailDestino.flEditavel = ValueUtil.VALOR_NAO;
			super.validate(domain);
			campoDsEmailDestino.flEditavel = valorOriginalDoCampo;
		}
		//cdRepresentante
		if (ValueUtil.isEmpty(pedido.cdRepresentante)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_NOME_ENTIDADE);
		}
		//nuPedido
		if (ValueUtil.isEmpty(pedido.nuPedido)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_NUPEDIDOPDA);
		}
		//flOrigemPedido
		if (ValueUtil.isEmpty(pedido.flOrigemPedido)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_FLORIGEM);
		}
		//cdCliente
		if (ValueUtil.isEmpty(pedido.cdCliente)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTE_NOME_ENTIDADE);
		}
		//dtEmissao
		if (ValueUtil.isEmpty(pedido.dtEmissao)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_DTEMISSAO);
		}
		//cdSegmento
		if (ValueUtil.isEmpty(pedido.cdSegmento) && LavenderePdaConfig.usaSegmentoNoPedido) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_SEGMENTO);
		}
		//cdCondicaoComercial
		if (ValueUtil.isEmpty(pedido.cdCondicaoComercial) && LavenderePdaConfig.usaCondicaoComercialPedido && !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL);
		}
		//cdCondicaoNegociacao
		if (ValueUtil.isEmpty(pedido.cdCondNegociacao) && LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_CONDICAONEGOCIACAO);
		}
		//cdTabelaPreco
		if (ValueUtil.isEmpty(pedido.cdTabelaPreco) && (LavenderePdaConfig.isUsaTabelaPrecoPedido() || LavenderePdaConfig.usaTabelaPrecoPorCanalAtendimento) && !pedido.isPedidoTroca()) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECO_NOME_TABPRECO);
		} else if (ValueUtil.isEmpty(pedido.cdTabelaPreco) && !LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			throw new ValidationException(Messages.PEDIDO_MSG_NENHUMA_TABELA_PRECO_DISPONIVEL_SEGMENTO);
		}
		//cdCondicaoPagamento
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			if (ValueUtil.isEmpty(pedido.dsCondicaoPagamentoSemCadastro)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO);
			} else {
				// Separa as parcelas
				String[] parcelas = StringUtil.split(pedido.dsCondicaoPagamentoSemCadastro, LavenderePdaConfig.separadorCondicaoPagamentoSemCadastro);
				int parcelaAnterior = 0;
				// Pega a maior parcela permitida
				int maiorParcela = LavenderePdaConfig.maiorParcelaCondicaoPagamentoSemCadastro;
				for (int i = 0; i < parcelas.length; i++) {
					int parcela = ValueUtil.getIntegerValue(parcelas[i]);
					if (parcela <= 0) {
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_DSCONDICAOPAGAMENTOSEMCADASTRO_INVALIDO, MessageUtil.getMessage(Messages.PEDIDO_SEPARADOR_INVALIDO, parcelas[i])));
					}
					// Verifica se a parcela e maior que anterior a ela e se respeita a maior parcela permitida
					if (parcela > maiorParcela) {
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_DSCONDICAOPAGAMENTOSEMCADASTRO_INVALIDO, MessageUtil.getMessage(Messages.PEDIDO_PARCELA_MAIOR_PERMITIDO, new Object[]{StringUtil.getStringValueToInterface(parcela), StringUtil.getStringValueToInterface(maiorParcela)})));
					} else if (parcela <= parcelaAnterior) {
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_DSCONDICAOPAGAMENTOSEMCADASTRO_INVALIDO, Messages.PEDIDO_ORDEM_DSCONDICAO_INVALIDO));
					}
					parcelaAnterior = parcela;
				}
				// Limpa caso tenha separadar informado após a ultima parcela
				if (!ValueUtil.isEmpty(parcelas)) {
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(parcelas[0]);
					for (int i = 1; i < parcelas.length; i++) {
						strBuffer.append(LavenderePdaConfig.separadorCondicaoPagamentoSemCadastro).append(parcelas[i]);
					}
					pedido.dsCondicaoPagamentoSemCadastro = strBuffer.toString();
				}
			}
		} else if (ValueUtil.isEmpty(pedido.cdCondicaoPagamento) && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido() && !pedido.isPedidoTroca() && (!pedido.isPedidoBonificacao() || LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao) && !(pedido.onReplicacao && LavenderePdaConfig.isIgnoraConfiguracoesReplicacao(Pedido.IGNORA_CONDPAGTO_REPLICACAO))) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO);
		}
		//cdTipoPagamento
		if (ValueUtil.isEmpty(pedido.cdTipoPagamento) && !LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() && !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() && !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao()) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTO_LABEL_TIPOPAGTO);
		}
		//cdTipoFrete
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (ValueUtil.isEmpty(pedido.cdTipoFrete) && LavenderePdaConfig.isUsaTipoFretePedido() && !pedido.isPedidoTroca() && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOFRETE_LABEL_TIPOFRETE);
		}
		//cdTipoPedido
		if (ValueUtil.isEmpty(pedido.cdTipoPedido) && !LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPEDIDO_LABEL_TIPOPEDIDO);
		}
		//Setor Cliente
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			String cdContrato = pedido.getCliente().cdContratoEspecial;
			if (!ValueUtil.isEmpty(cdContrato)) {
				validaClienteSetorOrigem(pedido);
			}
		}
		//Area venda
		if (LavenderePdaConfig.usaAreaVendas) {
			if (ValueUtil.isEmpty(pedido.cdAreaVenda)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_AREAVENDA);
			}
		}
		//Tributação no pedido sem cliente
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && LavenderePdaConfig.isUsaCalculaStItemPedido() && pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
			if (ValueUtil.isEmpty(pedido.cdTributacaoCliente)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LABEL_TRIBUTACAO);
			}
		}
		//Tributação no pedido novo cliente
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente() && pedido.getCliente().isNovoCliente()) {
			if (ValueUtil.isEmpty(pedido.cdTributacaoCliente)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.LABEL_TRIBUTACAO);
			}			
		}
		//Carga de Pedido
		if (LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido() && !pedido.inserindoFromSugestaoPedido) {
			if (ValueUtil.isEmpty(pedido.cdCargaPedido)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPEDIDO_CARGA_PEDIDO);
			}
		}
		//Contato
		if (LavenderePdaConfig.isObrigaInformarContatoERPClienteNoPedido() && ValueUtil.isEmpty(pedido.cdContato)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONTATO_NOME_ENTIDADE);
		}
		//Cliente Entrega
		if (obrigaIndicarClienteEntrega(pedido)) {
			if (ValueUtil.isEmpty(pedido.cdClienteEntrega)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_CLIENTE_ENTREGA);
			}
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			if (ValueUtil.isEmpty(pedido.dtPagamento)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_DT_PAGAMENTO);
			}
		}
		validaNumeroParcelasCondicaoPagamento(pedido);
	}

	public void validaNumeroParcelasCondicaoPagamento(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isNuParcelasNoPedido() || ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) return;
		
		if (pedido.getCondicaoPagamento().isPermiteEditarParcelas()) {
			if (pedido.nuParcelas <= 0) throw new ValidationException(Messages.PEDIDO_ERRO_NUMERO_PARCELAS_ZERO);
		}
		if (pedido.nuParcelas <= pedido.getCondicaoPagamento().nuParcelas) return;
		
		String nuParcelasPedido = StringUtil.getStringValue(pedido.nuParcelas);
		String nuParcelasCondicaoPagamento = StringUtil.getStringValue(pedido.getCondicaoPagamento().nuParcelas);
		throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_PARCELA_PEDIDO_MAIOR_PARCELA_CONDICAO_PAGAMENTO, new Object[] {nuParcelasPedido, nuParcelasCondicaoPagamento}));
	}
	
	private Campo getCampoDsEmailDestino(Pedido pedido) {
		if (pedido.isPedidoReplicado() || pedido.inserindoFromSugestaoPedido) {
			Vector listPerson = Pedido.getConfigPersonCadList(PedidoPdbxDao.getInstance().getTableNameToCamposDinamicos());
			int size = listPerson.size();
			for (int i = 0; i < size; i++) {
				Campo campo = (Campo)listPerson.items[i];
				String nmCampo = campo.nmCampo.toUpperCase();
				if (ValueUtil.valueEquals(Pedido.NMCOLUNA_DSEMAILSDESTINO, nmCampo)) {
					return campo;
				}
			}
		}
		return new Campo();
	}

	public void calculateRateioFretePedido(Pedido pedido, double vllPctFreteRep) {
		pedido.vlFreteRepresentante = (vllPctFreteRep / 100) * pedido.vlFrete;
		pedido.vlFreteCliente = pedido.vlFrete - pedido.vlFreteRepresentante;
		pedido.vlPctFreteRepresentante = vllPctFreteRep;
	}
	
	private Date getDataEntregaConsiderandoFeriadoEFinaisDeSemana(Date dtEntrega, int nuPosicaoDeDiasAPercorrer, Pedido pedido) throws SQLException {
		return getDataEntregaConsiderandoFeriadoEFinaisDeSemana(dtEntrega, nuPosicaoDeDiasAPercorrer, pedido, false);
	}
	
	protected Date getDataEntregaConsiderandoFeriadoEFinaisDeSemana(Date dtEntrega, int nuPosicaoDeDiasAPercorrer, Pedido pedido, boolean isValidandoNuMax) throws SQLException {
		if (nuPosicaoDeDiasAPercorrer == 0) return dtEntrega;
		Pedido pedidoAux = (Pedido) pedido.clone();
		int nuPosicaoDoDia = 0;
		int dtEntregaInicial = dtEntrega.getDay();
		nuPosicaoDeDiasAPercorrer = getNuDiasAdicionar(dtEntrega, nuPosicaoDeDiasAPercorrer, pedido);
		boolean existeDiaEmpresaEnderecoPossivel = LavenderePdaConfig.usaDiaEntregaBairroParaCalculoDataEntrega && EmpresaEnderecoService.getInstance().isExisteDiaPossivel(pedidoAux);
		do {
			loadDataEntrega(dtEntrega, nuPosicaoDeDiasAPercorrer, isValidandoNuMax, pedidoAux, nuPosicaoDoDia, dtEntregaInicial, existeDiaEmpresaEnderecoPossivel);
			if (isDiaValidoConsiderandoFinalSemana(dtEntrega)) {
				break;
			} else {
				nuPosicaoDeDiasAPercorrer = 1;
				nuPosicaoDoDia = 0;
			}
		} while (!isDiaValidoConsiderandoFinalSemana(dtEntrega));
		return dtEntrega;
	}

	private int getNuDiasAdicionar(Date dtEntrega, int nuPosicaoDeDiasAPercorrer, Pedido pedido) throws SQLException {
		boolean isDtFeriado = FeriadoService.getInstance().isFeriado(DateUtil.getCurrentDate());
		boolean adicionaNuDiasHoraLimite = PedidoUiUtil.isHoraLimiteDeEnvioParaDataDeEntregaDoPedido(pedido, dtEntrega.getDayOfWeek());
		if (adicionaNuDiasHoraLimite && !isDtFeriado) {
			nuPosicaoDeDiasAPercorrer += LavenderePdaConfig.nuDiasAdicionarHoraLimiteDataEntrega(dtEntrega.getDayOfWeek(), pedido.getCliente().isPessoaFisica());
		}
		if (!adicionaNuDiasHoraLimite && !isDtFeriado && isNaoContabilizaDiaDataEntrega(pedido, dtEntrega.getDayOfWeek())) {
			nuPosicaoDeDiasAPercorrer++;
		}
		if (LavenderePdaConfig.isPulaDataEntregaEmDiasDeFeriadoProxDiaInclusiveAtual() && isDtFeriado) {
			nuPosicaoDeDiasAPercorrer++;
		}
		return nuPosicaoDeDiasAPercorrer;
	}

	private void loadDataEntrega(Date dtEntrega, int nuPosicaoDeDiasAPercorrer, boolean isValidandoNuMax, Pedido pedidoAux, int nuPosicaoDoDia, int dtEntregaInicial, boolean existeDiaEmpresaEnderecoPossivel) throws SQLException {
		while (nuPosicaoDeDiasAPercorrer != nuPosicaoDoDia) {
			boolean diaValido = true;
			dtEntrega.advance(1);
			int dayOfWeek = dtEntrega.getDayOfWeek();
			if (FeriadoService.getInstance().isFeriado(dtEntrega)) {
				diaValido = false;
			}
			if (diaValido ||  (dtEntregaInicial == dtEntrega.getDay() && LavenderePdaConfig.isContabilizaDtEmissaoInvalidoNoCalculoEntrega())) {
				nuPosicaoDoDia++;
			}

			pedidoAux.dtEntrega = dtEntrega;
			if (!isValidandoNuMax && diaValido && nuPosicaoDeDiasAPercorrer == nuPosicaoDoDia && existeDiaEmpresaEnderecoPossivel && EmpresaEnderecoService.getInstance().isDtPrevisaoEntregaInvalidaForEmpresaEndereco(pedidoAux)) {
				nuPosicaoDoDia--;
			} else if (LavenderePdaConfig.isDataEntregaAjustadaParaSegundaFeira() && nuPosicaoDeDiasAPercorrer == nuPosicaoDoDia) {
				if (DateUtil.DATA_SEMANA_SABADO == dtEntrega.getDayOfWeek() || DateUtil.DATA_SEMANA_DOMINGO == dtEntrega.getDayOfWeek()) {
					nuPosicaoDoDia--;
				}
			}
			if (diaValido && (DateUtil.DATA_SEMANA_SABADO == dayOfWeek || DateUtil.DATA_SEMANA_DOMINGO == dayOfWeek) && LavenderePdaConfig.isNaoContabilizaFinalSemanaNoCalculoEntrega() && nuPosicaoDeDiasAPercorrer != nuPosicaoDoDia) {
				nuPosicaoDoDia--;
			}
		}
	}
	
	private boolean isNaoContabilizaDiaDataEntrega(Pedido pedido, int dayOfWeek) throws SQLException {
		if (!LavenderePdaConfig.usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedido() && !LavenderePdaConfig.usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica)
			return false;
		return ValueUtil.VALOR_NAO.equalsIgnoreCase(PedidoUiUtil.getHoraLimiteDeEnvio(pedido, dayOfWeek));
	}

	private boolean isDiaValidoConsiderandoFinalSemana(Date data) {
		boolean diaValido = true;
		if ((DateUtil.DATA_SEMANA_SABADO == data.getDayOfWeek()) || (DateUtil.DATA_SEMANA_DOMINGO == data.getDayOfWeek())) {
			if (LavenderePdaConfig.isDataEntregaContabilizandoApenasDiasUteis()) {
				diaValido = false;
			} else if (LavenderePdaConfig.isDataEntregaValidaApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == data.getDayOfWeek()) {
				diaValido = false;
			} else if (LavenderePdaConfig.isDataEntregaValidaApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == data.getDayOfWeek()) {
				diaValido = false;
			} else if (LavenderePdaConfig.isDataEntregaAjustadaParaSegundaFeira()) {
				diaValido = false;
			}
		}
		return diaValido;
	}
	
	public Date getDataPrevisaoEntrega(Pedido pedido, Cliente cliente) throws SQLException {
		Date dtEntrega = DateUtil.getCurrentDate();
		int nuDias = 0;
		
		if (LavenderePdaConfig.calculaPrazoEntregaPorProduto && pedido.itemPedidoList.size() > 0) {
			nuDias += getMaiorPrazoDosItens(pedido.itemPedidoList);
			return getDataEntregaConsiderandoFeriadoEFinaisDeSemana(dtEntrega, ++nuDias, pedido);
		}
		
		if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga() && ValueUtil.isNotEmpty(pedido.getCargaPedido().dtEntrega)) {
			dtEntrega = pedido.getCargaPedido().dtEntrega;
		} else {
			if (LavenderePdaConfig.nuDiasPrevisaoEntrega > 0) {
				nuDias += LavenderePdaConfig.nuDiasPrevisaoEntrega;
			} else if (LavenderePdaConfig.usaLeadTimePadraoClienteNoPedido) {
				nuDias += cliente.nuDiasPrevisaoEntrega; 
			}
		}
		
		if (LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente()) { 
			dtEntrega.advance(nuDias);
			return RotaEntregaService.getInstance().getSugestaoDataEntregaBaseadaEmUmaRota(dtEntrega, cliente); 
		}
		
		return getDataEntregaConsiderandoFeriadoEFinaisDeSemana(dtEntrega, nuDias, pedido);
	}
	
	private int getNudiasEntregaBaseadoNoLimiteEntrega(Pedido pedido, Date dtEntrega, int nuDiaSemana) throws SQLException {
		if (!LavenderePdaConfig.usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedido())  return 0;
		
		if (!LavenderePdaConfig.isPossuiConfiguracaoDeHoraLimite(nuDiaSemana)
				|| FeriadoService.getInstance().isDtFeriado(dtEntrega)) {
			return 1;
		}

		boolean horaLimiteUltrapassada = PedidoUiUtil.isHoraLimiteDeEnvioParaDataDeEntregaDoPedido(pedido, nuDiaSemana);

		if (horaLimiteUltrapassada) {
			return LavenderePdaConfig.nuDiasAdicionarHoraLimiteDataEntrega(nuDiaSemana, pedido.getCliente().isPessoaFisica());
		}
		
		return 0;
	}
	
	private int getMaiorPrazoDosItens(Vector itemPedidoList) {
		if(ValueUtil.isEmpty(itemPedidoList)) {
			return LavenderePdaConfig.nuDiasPrevisaoEntrega;
		}
		int nuMaxDias = 0;
		int qtItemsSemPrazo = 0;
		for (int i=0; i<itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if(itemPedido.isPedidoSemPrazoCadastrado || itemPedido.nuDiasPrazoEntrega == 0) {
				qtItemsSemPrazo++;
				continue;
			}
			if(itemPedido.nuDiasPrazoEntrega > nuMaxDias) {
				nuMaxDias = itemPedido.nuDiasPrazoEntrega;
			}
		}
		return (qtItemsSemPrazo >= itemPedidoList.size()) ? LavenderePdaConfig.nuDiasPrevisaoEntrega : nuMaxDias;
	}

	public boolean isPedidoRelacionadoCarga(String cdCargaPedido) {
		boolean pedidoRelacionadoCarga = LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido();
		if (!pedidoRelacionadoCarga && "2".equals(LavenderePdaConfig.usaCargaPedidoPorRotaEntregaDoCliente)) {
			pedidoRelacionadoCarga = ValueUtil.isNotEmpty(cdCargaPedido);
		}
		return pedidoRelacionadoCarga;
	}
	
	public void validateDataEntrega(Pedido pedido, boolean fromListaPedido) throws SQLException {
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido && (!LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga() || !isPedidoRelacionadoCarga(pedido.cdCargaPedido)) && !pedido.isPedidoTroca() && pedido.isPedidoAberto() && pedido.validaDataEntrega) {
			if (ValueUtil.isEmpty(pedido.dtEntrega)) {
				if (LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido()) {
					throw new ValidationException(Messages.CARGAPEDIDO_ERRO_DATA_ENTREGA);
				} else {
					if (fromListaPedido) {
						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.PEDIDO_LABEL_DTENTREGA);
					} else {
						throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_DTENTREGA);
					}
				}
			}
			if (pedido.dtEntrega.isBefore(DateUtil.getCurrentDate())) {
				throw new ValidationException(Messages.PEDIDO_DTENTREGA_ANTERIOR_PERMITIDO);
			}
			validateDataEntregaFinalSemanaFeriado(pedido);
			validateNuMaxDiasPrevisaoEntregaDoPedido(pedido);
			validaDataMinimaPrevEntregaParaEnvioDoPedido(pedido);
			if (!isValidoDataPrevisaoEntrega(pedido)) {
				throw new LiberacaoDataEntregaPedidoException(Messages.PEDIDO_MSG_VALIDACAO_DTENTREGA_INVALIDA_ENDERECO_ENTREGA);
			}
		}
	}
	
	public void validateDataEntregaFinalSemanaFeriado(Pedido pedido) throws SQLException {
		if (((DateUtil.DATA_SEMANA_SABADO == pedido.dtEntrega.getDayOfWeek()) || (DateUtil.DATA_SEMANA_DOMINGO == pedido.dtEntrega.getDayOfWeek())) && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
			if (LavenderePdaConfig.isDataEntregaAjustadaParaSegundaFeira() || LavenderePdaConfig.isDataEntregaContabilizandoApenasDiasUteis()) {
				throw new LiberacaoDataEntregaPedidoException(Messages.PEDIDO_DTENTREGA_FINAL_SEMANA_NAO_PERMITIDO);
			}
			if (LavenderePdaConfig.isDataEntregaValidaApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == pedido.dtEntrega.getDayOfWeek()) {
				throw new LiberacaoDataEntregaPedidoException(Messages.PEDIDO_DTENTREGA_DOMINGO_NAO_PERMITIDO);
			} 
			if (LavenderePdaConfig.isDataEntregaValidaApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == pedido.dtEntrega.getDayOfWeek()) {
				throw new LiberacaoDataEntregaPedidoException(Messages.PEDIDO_DTENTREGA_SABADO_NAO_PERMITIDO);
			}
		}
		if (FeriadoService.getInstance().isFeriado(pedido.dtEntrega) && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
			throw new LiberacaoDataEntregaPedidoException(Messages.PEDIDO_DTENTREGA_FERIADO_NAO_PERMITIDO);
		}
	}
	
	public void validateNuMaxDiasPrevisaoEntregaDoPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.getNuMaxDiasPrevisaoEntregaDoPedido() > 0 && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
			Date dateMax = DateUtil.getCurrentDate();
			int nuDiaSemana = dateMax.getDayOfWeek();
			int nuDiasAdd = getNudiasEntregaBaseadoNoLimiteEntrega(pedido, dateMax, nuDiaSemana) ;
			if (LavenderePdaConfig.isNuMaxDiasPrevisaoEntregaDoPedidoConsideraDiasCorridos()) {
				DateUtil.addDay(dateMax, LavenderePdaConfig.getNuMaxDiasPrevisaoEntregaDoPedido());
			} else {
				dateMax = getDataEntregaConsiderandoFeriadoEFinaisDeSemana(DateUtil.getCurrentDate(), LavenderePdaConfig.getNuMaxDiasPrevisaoEntregaDoPedido() + nuDiasAdd, pedido, true);
			}
			if (pedido.dtEntrega.isAfter(dateMax)) {
				throw new LiberacaoDataEntregaPedidoException(MessageUtil.getMessage(Messages.PEDIDO_DTENTREGA_POSTERIOR_PERMITIDO, new String[] { StringUtil.getStringValue(pedido.dtEntrega), StringUtil.getStringValue(dateMax) }));
			}
		}
	}

	public void validaDataMinimaPrevEntregaParaEnvioDoPedido(Pedido pedido) throws SQLException {
		if ((LavenderePdaConfig.isNuMinDiasPrevisaoEntregaDoPedidoLigado() || LavenderePdaConfig.calculaPrazoEntregaPorProduto) && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada) && (!LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga() || !isPedidoRelacionadoCarga(pedido.cdCargaPedido))) {
			Date dataMinimaPermitidaParaEntregaDoPedido = getDataMinimaPermitidaParaEntregaDoPedido(pedido);
			if (pedido.dtEntrega.isBefore(dataMinimaPermitidaParaEntregaDoPedido)) {
				try {
					throw new LiberacaoDataEntregaPedidoException(MessageUtil.getMessage(Messages.PEDIDO_DTENTREGA_BEFORE_PERMITIDO, new String[] { StringUtil.getStringValue(pedido.dtEntrega), StringUtil.getStringValue(dataMinimaPermitidaParaEntregaDoPedido) }));
				} finally {
					if (pedido.isPedidoIniciadoProcessoEnvio()) {
						updateColumn(pedido.getRowKey(), "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
					}
				}
			}
		}
	}
	
	private boolean isValidoDataPrevisaoEntregaParaEnderecoCliente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			if (LavenderePdaConfig.isSempreObrigaInclusaoEnderecoEntrega() || LavenderePdaConfig.isObrigaInclusaoEnderecoEntregaCasoExistaRegistroDeEnderecoEntrega()) {
				validaEnderecoEntregaPedido(pedido);
				boolean validaDataEntrega = true;
				if (LavenderePdaConfig.isObrigaInclusaoEnderecoEntregaCasoExistaRegistroDeEnderecoEntrega()) {
					validaDataEntrega = ClienteEnderecoService.getInstance().isExisteEnderecosEntregaParaCliente(pedido);
				}
				if (validaDataEntrega && ClienteEnderecoService.getInstance().isDtEntregaPedidoInvalidaForClienteEndereco(pedido) && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isValidoDataPrevisaoEntrega(Pedido pedido) throws SQLException {
		if (ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
			return true;
		}
		boolean dtPrevisaoEmpresaEnderecoValido = true;
		if (LavenderePdaConfig.usaDiaEntregaBairroParaCalculoDataEntrega) {
			dtPrevisaoEmpresaEnderecoValido = !EmpresaEnderecoService.getInstance().isDtPrevisaoEntregaInvalidaForEmpresaEndereco(pedido);
		}
		return dtPrevisaoEmpresaEnderecoValido && isValidoDataPrevisaoEntregaParaEnderecoCliente(pedido);
	}
	
	public Date getDataMinimaPermitidaParaEntregaDoPedido(Pedido pedido) throws SQLException {
		int prazoPedidoDias = (LavenderePdaConfig.calculaPrazoEntregaPorProduto && pedido.itemPedidoList.size() > 0) ? getMaiorPrazoDosItens(pedido.itemPedidoList) : LavenderePdaConfig.nuMinDiasPrevisaoEntregaDoPedido;
		Integer numeroMinimoDiasPedido = TipoEnderecoService.getInstance().findNumeroMinimoDiasPedidoPor(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente);
		if (numeroMinimoDiasPedido != null && numeroMinimoDiasPedido > 0) {
			prazoPedidoDias = numeroMinimoDiasPedido;
		}
		return getDataEntregaConsiderandoFeriadoEFinaisDeSemana(DateUtil.getCurrentDate(), prazoPedidoDias, pedido);
	}
	
	private void validaClienteSetorOrigem(Pedido pedido) throws SQLException {
		ClienteSetorOrigem clienteSetorOrigem = new ClienteSetorOrigem();
		String cdContrato = pedido.getCliente().cdContratoEspecial;
		if (pedido.getCliente().cdCliente.equals(cdContrato)) {
			clienteSetorOrigem.cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO;
		} else {
			clienteSetorOrigem.cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_REDE;
		}
		clienteSetorOrigem.cdCliRede = cdContrato;
		clienteSetorOrigem.cdEmpresa = SessionLavenderePda.cdEmpresa;
		clienteSetorOrigem.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		//--
		Vector clienteSetorOrigemList = ClienteSetorOrigemService.getInstance().findAllByExample(clienteSetorOrigem);
		if (ValueUtil.isEmpty(pedido.cdOrigemSetor) && ValueUtil.isNotEmpty(clienteSetorOrigemList)) {
			throw new ValidationException(Messages.PEDIDO_SETOR_ORIGEM_INVALIDO);
		} else {
			clienteSetorOrigemList = null;
		}
		//--
		if (ValueUtil.isNotEmpty(pedido.cdOrigemSetor)) {
			clienteSetorOrigem.cdOrigemSetor = pedido.cdOrigemSetor;
			Vector clienteSetorList = ClienteSetorOrigemService.getInstance().findAllByExampleSummary(clienteSetorOrigem);
			if (ValueUtil.isEmpty(pedido.cdSetor) && ValueUtil.isNotEmpty(clienteSetorList)) {
				throw new ValidationException(Messages.PEDIDO_SETOR_INVALIDO);
			} else {
				clienteSetorList = null;
			}
		}
	}
	
	private void validaQuilometragemETempoDoPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			if (pedido.getTipoPedido() != null && pedido.getTipoPedido().isFlIndicaKmTempo()) {
				if (pedido.nuKmInicial == 0) {
					throw new ValidationException(Messages.NUKMINICIAL_NAO_INFORMADO);
				}
				if (pedido.nuKmFinal == 0) {
					throw new ValidationException(Messages.NUKMFINAL_NAO_INFORMADO);
				}
				if (ValueUtil.isEmpty(pedido.hrInicialIndicado)) {
					throw new ValidationException(Messages.HRINICIALINDICADO_NAO_INFORMADO);
				}
				if (ValueUtil.isEmpty(pedido.hrFinalIndicado)) {
					throw new ValidationException(Messages.HRFINALINDICADO_NAO_INFORMADO);
				}
			}
			if (pedido.nuKmInicial < 0) {
				throw new ValidationException(Messages.KMINICIAL_MENOR_ZERO);
			}
			if (pedido.nuKmFinal < 0) {
				throw new ValidationException(Messages.KMFINAL_MENOR_ZERO);
			}
			if (pedido.nuKmInicial > 0 && pedido.nuKmFinal > 0) {
				if (pedido.nuKmInicial > pedido.nuKmFinal) {
					throw new ValidationException(Messages.KMINICIAL_MAIOR_KMFINAL);
				}
			}
			if (ValueUtil.isNotEmpty(pedido.hrInicialIndicado) && ValueUtil.isNotEmpty(pedido.hrFinalIndicado)) {
				if (!TimeUtil.isValidTimeHHMM(pedido.hrInicialIndicado)) {
					throw new ValidationException(Messages.HRINICIALINDICADO_INVALIDO);
				}
				if (!TimeUtil.isValidTimeHHMM(pedido.hrFinalIndicado)) {
					throw new ValidationException(Messages.HRFINALINDICADO_INVALIDO);
				}
				Time hrInicialIndicado = new Time(); 
				hrInicialIndicado.hour = ValueUtil.getIntegerValue(pedido.hrInicialIndicado.substring(0, 2));
				hrInicialIndicado.minute = ValueUtil.getIntegerValue(pedido.hrInicialIndicado.substring(3, 5));
				hrInicialIndicado.second = 0;
				Time hrFinalIndicado = new Time(); 
				hrFinalIndicado.hour = ValueUtil.getIntegerValue(pedido.hrFinalIndicado.substring(0, 2));
				hrFinalIndicado.minute = ValueUtil.getIntegerValue(pedido.hrFinalIndicado.substring(3, 5));
				hrFinalIndicado.second = 0;
				if (TimeUtil.getMillisRealBetween(hrInicialIndicado, hrFinalIndicado) > 0) {
					throw new ValidationException(Messages.HRINICIALINDICADO_MAIOR_HRFINALINDICADO);
				}
				
			}
			
		}
	}

	public boolean reloadValoresDosItensPedido(Pedido pedido, boolean validateItemPedido, boolean nonBlockingValidationWithException) throws SQLException {
		if (pedido.ignoraRecalculoItens) {
			return true;
		}
		boolean updateItens = false;
		if ((!pedido.isPedidoBonificacao() || LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao) && !pedido.isPedidoTroca() && !ValueUtil.isEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				ItemPedidoService.getInstance().validateItemTabelaPreco(pedido, itemPedido);
			}
			pedido.itensComErroAndExceptionMap = new HashMap<>();
			for (int i = 0; i < size; i++) {
				try {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					if (LavenderePdaConfig.usaCalculoReversoNaST && itemPedido.vlPctDescontoStReverso != 0) {
						itemPedido.flTipoEdicao  = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMST;
					} else if (itemPedido.vlPctDesconto != 0) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
					} else if (itemPedido.vlPctAcrescimo != 0) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
					}
					if (!ValueUtil.valueEquals(LavenderePdaConfig.usaPrecoPorUnidadeQuantidadePrazo, 'N')) {
						itemPedido.cdPrazoPagtoPreco = itemPedido.pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
					}
					if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && itemPedido.qtdCreditoDesc > 0 && ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem) && !DescPromocionalService.getInstance().loadDescPromocional(itemPedido, itemPedido.getProduto())) {
						ItemPedidoService.getInstance().aplicaDescontoPorCredito(pedido, itemPedido);
					} else {
						if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
							pedido.qtdCreditoDescontoConsumido -= itemPedido.qtdCreditoDesc;
							itemPedido.qtdCreditoDesc = 0;
							itemPedido.flTipoCadastroItem = null;
							itemPedido.cdProdutoCreditoDesc = null;
						}
						resetDadosItemPedido(pedido, itemPedido);
					}
					//Aplica o desconto quantidade quando alterar a condpagto do pedido ou na replicação com o flDescontoQtdAuto(tabelaPreco) = S
					itemPedido.atualizandoDesc = LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && ((pedido.isReplicandoPedido && pedido.getTabelaPreco().isAplicaDescQtdeAuto()) || !pedido.isReplicandoPedido);
					calculateItemPedido(pedido, itemPedido, validateItemPedido);
				} catch (ValidationException ve) {
					if (nonBlockingValidationWithException) {
						pedido.itensComErroAndExceptionMap.put((ItemPedido) pedido.itemPedidoList.items[i], ve);
					} else {
						throw ve;
					}
				}
			}
			updateItens = pedido.itensComErroAndExceptionMap.size() == 0;
		}
		return updateItens;
	}

	public boolean recalculateFretePedido(final Pedido pedido) throws SQLException {
		boolean updateItens = false;
		if (!pedido.isPedidoTroca() && !ValueUtil.isEmpty(pedido.itemPedidoList)) {
			updateItens = recalculateFreteItensPedido(pedido);
		}
		return updateItens;
	}

	public void calculate(final Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		double vlTotalPedido = 0;
		double vlTotalPeso = 0;
		double vlTotalBonificacao = 0;
		double vlTotalFrete = 0;
		double vlTotalBrutoItens = 0;
		double vlTotalVerbaPedido = 0;
		double vlTotalBaseItens = 0;
		double vlTotalVolumePedido = 0;
		double vlTotalMargem = 0;
		double vlTotalPrecoEfetivo = 0;
		double vlDescTotalEfetivo = 0;
		double vlTotalDescAuto = 0;
		double vlTotalBaseInterpolacao = 0;
		double vlSeguroPedido = 0;
		double vlTotalPedidoFreteTributos = 0;
		double vlTotalFretePedido = 0;
		double vlTotalPontuacaoRealizada = 0;
		double vlTotalPontuacaoBase = 0;
		BigDecimal vlTotalVerbaPedidoInterp = BigDecimal.ZERO;
		ItemPedido itemPedido;
		int size = pedido.itemPedidoList.size();
		if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete() && size == 0) {
			pedido.vlFrete = 0d;
		}
		divideFreteManualNosItens(pedido, false);
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlTotalPedido += itemPedido.vlTotalItemPedido;
			vlTotalBrutoItens += itemPedido.vlTotalBrutoItemPedido;
			vlTotalBaseItens += ValueUtil.round(itemPedido.vlBaseItemPedido * itemPedido.getQtItemFisico());
			vlTotalFrete += itemPedido.getVlTotalFrete();
			if (!ValueUtil.VALOR_SIM.equals(itemPedido.getProduto().flIgnoraPeso)) {
			vlTotalPeso += itemPedido.qtPeso;
			}
			vlTotalVerbaPedido += itemPedido.vlVerbaItem;
			if (TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedido.flTipoItemPedido)) {
				vlTotalBonificacao += itemPedido.vlTotalItemPedido;
			}
			vlTotalVolumePedido += itemPedido.vlVolumeItem;
			vlTotalMargem += itemPedido.vlTotalMargemItem;
			vlTotalPrecoEfetivo += itemPedido.vlPrecoEfetivoUnitario * itemPedido.getQtItemFisico();
			vlDescTotalEfetivo += itemPedido.vlDescontoTotalAutoDesc;
			vlTotalDescAuto += itemPedido.vlTotalDescontoAuto;
			vlTotalBaseInterpolacao += itemPedido.vlBaseInterpolacaoProduto * itemPedido.getQtItemFisico();
			vlSeguroPedido += itemPedido.getVlTotalSeguro();
			vlTotalPedidoFreteTributos += itemPedido.vlTotalItemFreteTributacao;
			vlTotalFretePedido += itemPedido.vlTotalItemPedidoFrete;
			try {
				vlTotalVerbaPedidoInterp = vlTotalVerbaPedidoInterp.add(BigDecimal.valueOf(itemPedido.vlVerbaItem * 1000));
			} catch (InvalidNumberException e) {
				ExceptionUtil.handle(e);
			}
			vlTotalPontuacaoRealizada += itemPedido.vlPontuacaoRealizadoItem;
			vlTotalPontuacaoBase += itemPedido.vlPontuacaoBaseItem;
		}
		pedido.vlTotalItens = vlTotalPedido;
		if (LavenderePdaConfig.aplicaFaixaDescontoVolumeVendaMensalPorPedido() && pedido.onFechamentoPedido) {
			pedido.vlTotalPedido = pedido.vlTotalItens * (1 - (pedido.vlPctDescCliente / 100));
		} else {
		pedido.vlTotalPedido = pedido.vlTotalItens;
		}
		pedido.vlTotalBrutoItens = vlTotalBrutoItens;
		pedido.vlTotalBaseItens = vlTotalBaseItens;
		pedido.vlVolumePedido = ValueUtil.round(vlTotalVolumePedido, LavenderePdaConfig.nuCasasDecimaisVlVolume);
		pedido.vlTotalMargem = vlTotalMargem;
		pedido.vlSeguroPedido = vlSeguroPedido;
		pedido.vlPctTotalMargem = vlTotalMargem != 0 ? vlTotalMargem / pedido.vlTotalPedido * 100 : 0;
		//--
		if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep()) {
			calculaFreteTransportadoraCep(pedido);
			if (pedido.getTransportadora() != null && pedido.getTransportadora().isFlSomaFrete()) {
				pedido.vlTotalPedido = vlTotalPedido + pedido.vlFreteTotal;
			}
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.isPermiteBonificarProdutoSemLimitesItensNoPedido()) {
			pedido.vlBonificacaoPedido = vlTotalBonificacao;
			if (!pedido.isPedidoBonificacao()) {
				vlTotalPedido = ValueUtil.round(vlTotalPedido - vlTotalBonificacao);
				pedido.vlTotalPedido = vlTotalPedido;
			}
		} else if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			pedido.vlBonificacaoPedido = vlTotalBonificacao;
			pedido.vlTotalPedido = ValueUtil.round(vlTotalPedido - vlTotalBonificacao);
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			pedido.vlFrete = vlTotalFretePedido;
		}
		//--
		calculateTrocaPedido(pedido);
		//--
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
			double vlIndiceFinanceiro = pedido.getCliente().vlIndiceFinanceiro;
			if (vlIndiceFinanceiro > 0) {
				pedido.vlTotalPedido = ValueUtil.round(vlTotalPedido * vlIndiceFinanceiro);
				pedido.vlPctDesconto = ValueUtil.round((1.0 - vlIndiceFinanceiro) * 100.0);
			}
		}
		pedido.vlTotalPedido -= pedido.vlTotalNotaCredito;
		//--
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			pedido.qtPeso = vlTotalPeso;
			if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
				pedido.qtPeso = PesoMedioPedidoService.getInstance().calculatePesoMedioPedido(pedido.qtPeso, pedido.vlTotalPedido);
				if (LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
					PesoFaixaService.getInstance().applyPesoFaixaInPedido(pedido);
				}
			}
			if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && LavenderePdaConfig.usaFretePedidoPorToneladaCliente) {
				pedido.vlFrete = pedido.getCliente().vlTonFrete * (pedido.qtPeso/1000);
			}
		}
		//--
		if ((LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso()) && !LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem) {
			pedido.vlFrete = 0;
			if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && (!LavenderePdaConfig.usaFreteApenasTipoFob || pedido.isTipoFreteFob())) {
				pedido.vlFrete = vlTotalFrete;
			}
		}
		//--
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
				try {
					vlTotalVerbaPedido = vlTotalVerbaPedidoInterp.divide(BigDecimal.valueOf(1000), LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_CEILING).doubleValue();
				} catch (ArithmeticException | IllegalArgumentException | InvalidNumberException e) {
					ExceptionUtil.handle(e);
				}
			}
			pedido.vlVerbaPedido = vlTotalVerbaPedido;
			calculaVerbaPositiva(pedido);
		}
		//--
		if (VerbaService.getInstance().permiteConsumirVerbaSupervisor(pedido) && LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao()) {
			pedido.vlVerbaPedido = ValueUtil.round(vlTotalPedido * -1);
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			aplicaDescontoCapaPedido(pedido);
		}
		if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0 || LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 && !pedido.inserindoFromSugestaoPedido) {
			aplicaDescontoValorPedido(pedido);
		}
		if (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0 && !LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			aplicaDescontoPctPedido(pedido);
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() && !LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			if (pedido.vlTotalItens > 0) {
				pedido.vlTotalPedido = pedido.vlTotalPedido - ((pedido.vlTotalPedido * pedido.vlPctDescontoCondicao) / 100);
			}
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()) {
			if (pedido.vlTotalItens > 0) {
				pedido.vlTotalPedido = pedido.vlTotalPedido - ((pedido.vlTotalPedido * pedido.vlPctDesconto) / 100);
			}
		}
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial) {
			aplicaPctDescontoRepEspecialPedido(pedido);
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete && pedido.vlPctDescFrete != 0 && !LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			pedido.vlTotalPedido = pedido.vlTotalPedido * (1 - (pedido.vlPctDescFrete / 100));
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			pedido.vlTotalPedido = pedido.vlTotalPedido + pedido.vlFreteAdicional;
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			TributosService.getInstance().calculaVlTotalPedidoComTributos(pedido);
			STService.getInstance().calculaVlTotalPedidoComST(pedido);
			IpiService.getInstance().calculaVlTotalPedidoComIpi(pedido);
		} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.usaValorComImpostosControleValorMinimoPedido) {
			STService.getInstance().calculaVlTotalPedidoComST(pedido);
		} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			IpiService.getInstance().calculaVlTotalPedidoComIpi(pedido);
		}
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			TributosService.getInstance().calculaVlTotalPedidoComTributosEDeducoes(pedido);
		}
		//--
		calculaApenasRentabilidadePedido(pedido);
		//--
		calculaRentabilidade(pedido);
		//--
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			PontuacaoService.getInstance().aplicaPontosPedido(pedido);
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			pedido.vlTotalPedidoEstoquePositivo = pedido.getVlEfetivo();
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido) {
			calculaDescontoPonderadoPedido(pedido);
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			aplicaDescCascataPorCategoria(pedido, LavenderePdaConfig.isAplicaDescontoCategoriaArredondandoCadaAplicacao(), LavenderePdaConfig.isAplicaDescontoCategoriaArredondaSoResultado(), false);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			pedido.vlPctDescontoAutoEfetivo = vlTotalBaseInterpolacao > 0 ? ValueUtil.round(100 * (1 - (vlTotalPrecoEfetivo / vlTotalBaseInterpolacao)), LavenderePdaConfig.nuArredondamentoRegraInterpolacaoTotal) : 0d;
			pedido.vlDescontoTotalAutoDesc = ValueUtil.getDoubleValueTruncated(vlDescTotalEfetivo, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
			pedido.vlTotalDescontoAuto = ValueUtil.getDoubleValueTruncated(vlTotalDescAuto, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && !pedido.getCliente().isFreteEmbutido()) {
			pedido.vlTotalPedidoFreteTributos = vlTotalPedidoFreteTributos;
			pedido.vlTotalFretePedido = vlTotalFretePedido;
		}

		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			calcularFretePersonalizado(pedido);
			if (pedido.getTransportadora() != null && pedido.getTransportadora().isFlSomaFrete()) {
				pedido.vlTotalPedido = vlTotalPedido + pedido.vlFrete;
	}
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			pedido.vlTotalPontuacaoBase = vlTotalPontuacaoBase;
			pedido.vlTotalPontuacaoRealizado = vlTotalPontuacaoRealizada;
		}
		if (isSomaFreteAoTotalPedidoNoFinalDoCalculo(tipoPedido)) {
			pedido.vlTotalPedido += pedido.vlFrete;
		}
		//--
		getItemPedidoService().atualizaPctFreteItens(pedido);
		//--
		calculateFretePedido(pedido, false);
		//--
		calculateRateioFretePedido(pedido, pedido.vlPctFreteRepresentante);
		//--
		if ((LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo())) {
			recalculateComissaoPedido(pedido);
		}

		if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex()) {
			ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedido, true);
		}
	}

	private void calculaRentabilidade(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemRentabilidade() || !pedido.isPermiteUtilizarRentabilidade()) return;
		HashMap<String, Object> variavelCalculoList = VariavelCalculoService.getInstance().getHashVariavelCalculoPedido(pedido);
		HashMap<String, Object> variaveisToSave = FormulaCalculoSqlService.getInstance().executeCalculoSql(variavelCalculoList, LavenderePdaConfig.getFormulaCalculoPedido(), pedido);
		VariavelCalculoService.getInstance().populatePedidoWithHashValues(pedido, variaveisToSave);
		pedido.variavelCalculoList = variaveisToSave;
		
		pedido.cdMargemRentab = MargemRentabService.getInstance().findCdMargemRent(pedido);
	}

	private void calcularFretePersonalizado(Pedido pedido) throws SQLException {
		FreteConfig freteConfig = pedido.getFreteConfig();
		if (freteConfig == null) return;
		freteConfig.vlPrecoFreteCalculado = BigDecimal.ZERO;
		FreteCalculoService.getInstance().calculaFreteCalculoList(pedido, freteConfig, freteConfig.listFreteCalculo);
		pedido.vlFrete = ValueUtil.toDouble(freteConfig.vlPrecoFreteCalculado);
	}

	public void calculaApenasRentabilidadePedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			calculateRentabilidadePedido(pedido);
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
			calculateIndiceRentabilidadePedido(pedido);
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			calculaIndiceRentabilidadePedidoSemTributos(pedido);
		}
	}

	public void recalculaValoresInterpolacaoReplicacao(final Pedido pedido, ItemPedido itemPedido, boolean ignoreCalculate) throws SQLException {
		DescPromocional descPromocional = itemPedido.getDescPromocional();
		itemPedido.vlPctDescontoAuto = (descPromocional != null) ? descPromocional.vlPctDescontoProduto : 0d;
		loadValorBaseItemPedido(pedido, itemPedido);
		ItemPedidoService.getInstance().aplicaPrecoItemComValoresAdicionaisEmbutidos(itemPedido, pedido);
		if (!ignoreCalculate) {
			ItemPedidoService.getInstance().calculate(itemPedido, pedido);
		}
	}

	public void calculaVerbaPositiva(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			pedido.vlVerbaPedidoPositivo = VerbaService.getInstance().somaVerbaPositiva(pedido);
			if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
				pedido.saldoVerbaPedido = pedido.vlVerbaPedidoPositivo - pedido.vlVerbaPedido;
			}
		}
	}


	private void calculaDescontoPonderadoPedido(Pedido pedido) throws SQLException {
		if (pedido != null) {
			pedido.vlPctDesconto = 0;
			pedido.vlDesconto = 0;
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					if (itemPedido.getItemTabelaPreco().vlPctDescPromocional != 0) {
						double vlItemPedidoAux = 0;
						if (itemPedido.vlPctDesconto > itemPedido.getItemTabelaPreco().vlPctDescPromocional) {
							double vlPctDescReal = itemPedido.vlPctDesconto - itemPedido.getItemTabelaPreco().vlPctDescPromocional;
							vlItemPedidoAux = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (vlPctDescReal / 100)));
							pedido.vlDesconto += ValueUtil.round(itemPedido.vlBaseItemPedido - vlItemPedidoAux) * itemPedido.getQtItemFisico();
						}
					} else {
						pedido.vlDesconto += ValueUtil.round((itemPedido.vlBaseItemPedido - itemPedido.vlItemPedido) * itemPedido.getQtItemFisico());
					}
				}
				if (pedido.vlTotalBrutoItens != 0 && pedido.vlDesconto != 0) {
					pedido.vlPctDesconto = ValueUtil.round((pedido.vlDesconto / pedido.vlTotalBrutoItens) * 100, LavenderePdaConfig.nuCasasDecimais);
				} else {
					pedido.vlPctDesconto = 0;
				}
			}
		}
	}

	public void calculaIndiceRentabilidadePedidoSemTributos(Pedido pedido) throws SQLException {
		if (pedido == null) return;
		double vlMargemPercentual = 0d;
		double vlMargemEspecificaPonderada = 0d;
		RentabilidadePedidoAux rentabilidadePedidoAuxNeutros = getRentabilidadePedidoAuxSemTribituos(pedido, false);
		RentabilidadePedidoAux rentabilidadePedidoAuxTotal = getRentabilidadePedidoAuxSemTribituos(pedido, true);
		aplicaRentabilidadeAuxPedidoSemTributos(pedido, vlMargemPercentual, vlMargemEspecificaPonderada, rentabilidadePedidoAuxNeutros, false);
		aplicaRentabilidadeAuxPedidoSemTributos(pedido, vlMargemPercentual, vlMargemEspecificaPonderada, rentabilidadePedidoAuxTotal, true);
			}

	private void aplicaRentabilidadeAuxPedidoSemTributos(Pedido pedido, double vlMargemPercentual, double vlMargemEspecificaPonderada, RentabilidadePedidoAux rentabilidadePedidoAuxNeutros, boolean isTotal) {
		if (rentabilidadePedidoAuxNeutros.dividendoMargemEspecificaPonderada != 0 && rentabilidadePedidoAuxNeutros.divisorMargemEspecificaPonderada != 0) {
			vlMargemEspecificaPonderada = rentabilidadePedidoAuxNeutros.dividendoMargemEspecificaPonderada / rentabilidadePedidoAuxNeutros.divisorMargemEspecificaPonderada;
			if (!isTotal) {
				pedido.vlMargemEspecificaPonderada = vlMargemEspecificaPonderada;
			}
			}
		if (rentabilidadePedidoAuxNeutros.vlMargemValor != 0 && rentabilidadePedidoAuxNeutros.vlReceitaVirtual != 0) {
			vlMargemPercentual = rentabilidadePedidoAuxNeutros.vlMargemValor/ rentabilidadePedidoAuxNeutros.vlReceitaVirtual;
			if (!isTotal) {
				pedido.vlMargemValor = rentabilidadePedidoAuxNeutros.vlMargemValor;
				pedido.vlReceitaVirtual = rentabilidadePedidoAuxNeutros.vlReceitaVirtual;
				pedido.vlMargemPercentual = vlMargemPercentual;
			}
		}
		if (vlMargemPercentual != 0 && vlMargemEspecificaPonderada != 0) {
			double vlRentabilidade = vlMargemPercentual * LavenderePdaConfig.indiceMinimoRentabilidadePedido / vlMargemEspecificaPonderada;
			if (!isTotal) {
				pedido.vlRentabilidade = vlRentabilidade;
				pedido.vlMargemPercentual = vlMargemPercentual;
			} else {
				pedido.vlRentabilidadeTotal = vlRentabilidade;
	}
		}
	}

	private RentabilidadePedidoAux getRentabilidadePedidoAuxSemTribituos(Pedido pedido, boolean ignoreNeutros) throws SQLException {
		RentabilidadePedidoAux rentabilidadePedidoAux = new RentabilidadePedidoAux();
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!ignoreNeutros && itemPedido.getProduto().isNeutro()) continue;
			rentabilidadePedidoAux.vlMargemValor += itemPedido.getQtItemFisico() * ItemPedidoService.getInstance().getMargemValor(itemPedido);
			rentabilidadePedidoAux.vlReceitaVirtual += itemPedido.getQtItemFisico() * ItemPedidoService.getInstance().getReceitaVirtual(itemPedido);
			rentabilidadePedidoAux.dividendoMargemEspecificaPonderada = rentabilidadePedidoAux.dividendoMargemEspecificaPonderada + ItemPedidoService.getInstance().getReceitaVirtual(itemPedido) * itemPedido.getItemTabelaPreco().vlPctMargemRentabilidade / 100 * itemPedido.getQtItemFisico();
			rentabilidadePedidoAux.divisorMargemEspecificaPonderada = rentabilidadePedidoAux.divisorMargemEspecificaPonderada + ItemPedidoService.getInstance().getReceitaVirtual(itemPedido) * itemPedido.getQtItemFisico();
			}
		return rentabilidadePedidoAux;
			}

	public void calculateRentabilidadePedido(final Pedido pedido) throws SQLException {
		if (pedido.isPedidoBonificacao()) return;
			if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8) {
				calculaRentabilidadePedidoTipo8(pedido);
				return;
			}
		RentabilidadePedidoAux rentabilidadePedidoAuxTotal = getRentabilidadePedidoAuxCalculated(pedido, true);
		RentabilidadePedidoAux rentabilidadePedidoAuxTotalNeutros = getRentabilidadePedidoAuxCalculated(pedido, false);
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 5) {
			pedido.vlRentabilidade = (rentabilidadePedidoAuxTotalNeutros.vlItensComSt > 0) ? ValueUtil.round((1 - (rentabilidadePedidoAuxTotalNeutros.vlTotalCustoDosItens / rentabilidadePedidoAuxTotalNeutros.vlItensComSt)) * 100) : 0;
			pedido.vlRentabilidadeTotal = (rentabilidadePedidoAuxTotal.vlItensComSt > 0) ? ValueUtil.round((1 - (rentabilidadePedidoAuxTotal.vlTotalCustoDosItens / rentabilidadePedidoAuxTotal.vlItensComSt)) * 100) : 0;
		} else if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 6 || LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 7) {
			calculaRentabilidadePedidoTipo6ou7(pedido, rentabilidadePedidoAuxTotalNeutros, false);
			calculaRentabilidadePedidoTipo6ou7(pedido, rentabilidadePedidoAuxTotal, true);
		} else if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 9) {
			pedido.vlRentabilidade = calculaRentabilidadePedidoTipo9(pedido, rentabilidadePedidoAuxTotalNeutros);
			pedido.vlRentabilidadeTotal = calculaRentabilidadePedidoTipo9(pedido, rentabilidadePedidoAuxTotal);
			if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
				pedido.vlRentabTotalItens = ItemPedidoService.getInstance().calculaRentabilidadeTipo9(rentabilidadePedidoAuxTotal.vlItens, rentabilidadePedidoAuxTotal.vlTotalCustoDosItens);
			}
		} else {
			pedido.vlRentabilidade = rentabilidadePedidoAuxTotalNeutros.vlTotalRentabilidade;
			pedido.vlRentabilidadeTotal = rentabilidadePedidoAuxTotal.vlTotalRentabilidade;
			}
		if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && !pedidoPossuiSomenteProdutosNeutros(pedido)) {
			pedido.flAbaixoRentabilidadeMinima = pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaMinima(pedido.getRentabilidadeFaixaList());
		}
	}

	private void calculaRentabilidadePedidoTipo6ou7(Pedido pedido, RentabilidadePedidoAux rentabilidadePedidoAux, boolean isTotal) throws SQLException {
		if (rentabilidadePedidoAux.vlTotalCustoDosItens <= 0) {
			if (isTotal) pedido.vlRentabilidadeTotal = 0;
			else pedido.vlRentabilidade = 0;
				return;
		}
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 7) {
			rentabilidadePedidoAux.vlTotalCustoDosItens = (rentabilidadePedidoAux.vlTotalCustoDosItens * pedido.getCondicaoPagamento().vlIndiceFinanceiro) + pedido.vlFrete;
		}
		if (rentabilidadePedidoAux.vlTotalCustoDosItens > 0) {
			double vlRentabilidadeCalculada = ValueUtil.round(((rentabilidadePedidoAux.vlItens / rentabilidadePedidoAux.vlTotalCustoDosItens) - 1) * 100);
			if (isTotal) pedido.vlRentabilidadeTotal = vlRentabilidadeCalculada;
			else pedido.vlRentabilidade = vlRentabilidadeCalculada;
		}
	}
	
	private double calculaRentabilidadePedidoTipo9(Pedido pedido, RentabilidadePedidoAux rentabilidadePedidoAux) throws SQLException {
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			calculaRentabilidadeSugeridaPedido(pedido, rentabilidadePedidoAux.vlItens, rentabilidadePedidoAux.vlTotalCustoDosItens);
		}
		return calculaRentabilidadeTipo9(rentabilidadePedidoAux.vlItens, rentabilidadePedidoAux.vlTotalCustoDosItens, pedido.getCondicaoPagamento().vlIndiceFinanceiro, pedido.vlFrete);
		}

	private RentabilidadePedidoAux getRentabilidadePedidoAuxCalculated(Pedido pedido, boolean ignoreProdutoNeutro) throws SQLException {
		RentabilidadePedidoAux rentabilidadePedidoAux = new RentabilidadePedidoAux();
			ItemPedido itemPedido;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if ((!ignoreProdutoNeutro && itemPedido.getProduto().isNeutro()) || itemPedido.isFazParteKitFechado())
					continue;
				double vlBaseItem = itemPedido.vlTotalItemPedido - itemPedido.vlRentabilidade;
			double valorTotalItemRentabilidade = calculaValorTotalItemParaRentabilidade(pedido, itemPedido);
				if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 5) {
				rentabilidadePedidoAux.vlItensComSt += itemPedido.getVlTotalComST();
				rentabilidadePedidoAux.vlTotalCustoDosItens += itemPedido.getVlPrecoCusto() * itemPedido.getQtItemFisico();
			} else if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 6 || LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 7 ||  LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 9) {
				double vlItemPedidoOld = itemPedido.vlItemPedido;
				itemPedido.vlItemPedido = valorTotalItemRentabilidade;
				rentabilidadePedidoAux.vlTotalCustoDosItens += ItemPedidoService.getInstance().getVlPrecoTotalCustoItemNaUnidadeAlternativa(pedido, itemPedido);
				rentabilidadePedidoAux.vlItens += valorTotalItemRentabilidade;
				itemPedido.vlItemPedido = vlItemPedidoOld;
				} else {
				rentabilidadePedidoAux.vlTotalRentabilidade += valorTotalItemRentabilidade - ValueUtil.round(vlBaseItem);
				}
			}
		return rentabilidadePedidoAux;
				}

	private double calculaValorTotalItemParaRentabilidade(Pedido pedido, ItemPedido itemPedido) {
		if (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) {
			if (pedido.vlDesconto > 0) {
				return itemPedido.vlTotalItemPedido - ((itemPedido.vlTotalItemPedido * pedido.vlDesconto) / 100);
					}
			return itemPedido.vlTotalItemPedido;
					}
		if ((LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0)) {
			double pctDesconto = ValueUtil.round((pedido.vlDesconto * 100) / pedido.vlTotalItens);
			if (pctDesconto > 0) {
				return itemPedido.vlTotalItemPedido - ((itemPedido.vlTotalItemPedido * pctDesconto) / 100);
				}
			}
		return itemPedido.vlTotalItemPedido;
			}
	
	private void calculaRentabilidadePedidoTipo8(Pedido pedido) throws SQLException {
		double vlTotalRentabilidadeItens = 0;
		double vlTotalRentabilidadeItensIgnoringNeutros = 0;
		int size = pedido.itemPedidoList.size();
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlTotalRentabilidadeItensIgnoringNeutros += itemPedido.vlRentabilidade;
			if (itemPedido.getProduto().isNeutro()) continue;
			vlTotalRentabilidadeItens += itemPedido.vlRentabilidade;
		}
		pedido.vlRentabilidade = ValueUtil.round(vlTotalRentabilidadeItens);
		pedido.vlRentabilidadeTotal = ValueUtil.round(vlTotalRentabilidadeItensIgnoringNeutros);
	}

	private void calculateIndiceRentabilidadePedido(final Pedido pedido) throws SQLException {
		int sizeItens = pedido.itemPedidoList.size();
		if (sizeItens == 0) {
			pedido.vlRentabilidade = LavenderePdaConfig.indiceMinimoRentabilidadePedido;
			return;
		}
		//--
		RentabilidadePedidoAux rentabilidadePedidoAuxNeutros = getRentabilidadePedidoAuxIndiceRentabilidadePedido(pedido, sizeItens, false);
		RentabilidadePedidoAux rentabilidadePedidoAuxTotal = getRentabilidadePedidoAuxIndiceRentabilidadePedido(pedido, sizeItens, true);
		pedido.vlRentabilidade = 0;
		if (rentabilidadePedidoAuxNeutros.vlTotalRealCorrigido > 0) {
			pedido.vlRentabilidade = getVlRentabilidadeIndiceRentabiidadePedido(rentabilidadePedidoAuxNeutros);
		}
		if (rentabilidadePedidoAuxTotal.vlTotalRealCorrigido > 0) {
			pedido.vlRentabilidadeTotal = getVlRentabilidadeIndiceRentabiidadePedido(rentabilidadePedidoAuxTotal);
		}
	}

	private double calculaRentabilidadeSugeridaPedido(Pedido pedido, double vlItens, double vlTotalCustoDosItens) throws SQLException {
		if (vlItens == 0) {
			pedido.vlRentabilidadeSug = 0;
			pedido.vlRentabSugItens = 0;
			return 0;
		}
		pedido.vlRentabSugItens = ItemPedidoService.getInstance().calculaRentabilidadeTipo9(vlItens, vlTotalCustoDosItens);
		return pedido.vlRentabilidadeSug = calculaRentabilidadeTipo9(vlItens, vlTotalCustoDosItens, pedido.getCondicaoPagamento().vlIndiceFinanceiro, 0);
	}
	
	private double calculaRentabilidadeTipo9(double vlTotalItens, double vlTotalCustoItens, double vlIndiceFinanceiro, double vlFrete) {
		if (vlTotalItens <= 0) return 0;
		return ValueUtil.round(((vlTotalItens / (vlTotalCustoItens * vlIndiceFinanceiro + vlFrete)) - 1) * 100D);
	}

	private double getVlRentabilidadeIndiceRentabiidadePedido(RentabilidadePedidoAux rentabilidadePedidoAux) {
		return ValueUtil.round((rentabilidadePedidoAux.vlTotalRealCorrigidoComIndice - rentabilidadePedidoAux.vlTotalVerbaPedido) / rentabilidadePedidoAux.vlTotalRealCorrigido);
	}

	private RentabilidadePedidoAux getRentabilidadePedidoAuxIndiceRentabilidadePedido(Pedido pedido, int sizeItens, boolean ignoreNeutros) throws SQLException {
		RentabilidadePedidoAux rentabilidadePedidoAux = new RentabilidadePedidoAux();
		ItemPedido itemPedido;
		for (int i = 0; i < sizeItens; i++) {
			itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if ((!ignoreNeutros && itemPedido.getProduto().isNeutro()) || itemPedido.isItemVendaNormal()) continue;
    		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
			double vlBase = itemTabPreco.getVlBase(pedido, itemPedido);
			double vlBaseEspecial = itemTabPreco.getVlBaseEspecial(pedido, itemPedido);
			double vlBaseItemPedido = itemPedido.vlBaseItemPedido;
			double vlFlexPatrociando = 0;
			if (vlBaseItemPedido > vlBase) {
				vlFlexPatrociando = (vlBaseEspecial - vlBase) * itemPedido.getQtItemFisico();
			}
			double vlTotalRealCorrigidoItem = ((itemPedido.vlTotalItemPedido + (itemPedido.vlVerbaItem * -1)) - itemPedido.vlVerbaItemPositivo) + vlFlexPatrociando;
			rentabilidadePedidoAux.vlTotalRealCorrigido += vlTotalRealCorrigidoItem;
			rentabilidadePedidoAux.vlTotalRealCorrigidoComIndice += vlTotalRealCorrigidoItem * itemPedido.vlRentabilidade;
			rentabilidadePedidoAux.vlTotalVerbaPedido += itemPedido.vlVerbaItem * -1;
		}
		return rentabilidadePedidoAux;
		}

	private void aplicaPctDescontoRepEspecialPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial) {
			pedido.vlPctDesconto = 0;
			pedido.vlTotalPedido = pedido.vlTotalItens;
			Representante rep = RepresentanteService.getInstance().getRepresentanteById(pedido.cdRepresentante);
			if ((rep != null) && rep.isEspecial()) {
				TipoPagamento tipoPagamento = pedido.getTipoPagamento();
				if ((tipoPagamento != null) && tipoPagamento.isEspecial()) {
					CondicaoPagamento condPagto = pedido.getCondicaoPagamento();
					if ((condPagto != null) && condPagto.isEspecial()) {
						pedido.vlPctDesconto = rep.vlPctDescEspecial;
						if (pedido.vlPctDesconto > 0) {
							pedido.vlTotalPedido = pedido.vlTotalItens - ((pedido.vlTotalItens * pedido.vlPctDesconto) / 100);
						}
					}
				}
			}
		}
	}
	
	private void aplicaDescontoCapaPedido(Pedido pedido) {
		if (pedido.vlTotalItens > 0) {
			if (pedido.vlDesconto > 0) {
				double newVlTotalPedido = pedido.vlTotalPedido - pedido.vlDesconto;
				double pctDesconto = ValueUtil.round(100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens));
				if (pctDesconto > LavenderePdaConfig.aplicaDescontoNaCapaDoPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(LavenderePdaConfig.aplicaDescontoNaCapaDoPedido) + "%"}));
				} else {
					pedido.vlTotalPedido = newVlTotalPedido;
				}
			} else if (pedido.vlPctDesconto > 0) {
				double newVlTotalPedido = ValueUtil.round(pedido.vlTotalPedido - ((pedido.vlTotalPedido * ValueUtil.round(pedido.vlPctDesconto)) / 100));
				//--
				if (ValueUtil.round(pedido.vlPctDesconto) > LavenderePdaConfig.aplicaDescontoNaCapaDoPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlPctDesconto)) + "%", StringUtil.getStringValueToInterface(LavenderePdaConfig.aplicaDescontoNaCapaDoPedido) + "%"}));
				} else {
					pedido.vlTotalPedido = newVlTotalPedido;
				}
			}
		}
	}

	private void aplicaDescontoValorPedido(Pedido pedido) throws SQLException {
		if (pedido.vlTotalItens > 0) {
			double newVlTotalPedido = pedido.vlTotalPedido - pedido.vlDesconto;
			double pctDesconto = 100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens);
			pctDesconto = ValueUtil.round(pctDesconto);
			//--
			if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
				double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescontoEmValorPorPedido;
				if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
					double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
					pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
				}
				if (pctMaxDesconto != 0 && pctDesconto > pctMaxDesconto) {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{pctDesconto + "%", pctMaxDesconto + "%"}));
				} else {
					pedido.vlTotalPedido = newVlTotalPedido;
				}
			}
			if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
				double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba;
				if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
					double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
					pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
				}
				if (pctMaxDesconto != 0 && pctDesconto > pctMaxDesconto) {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{pctDesconto + "%", pctMaxDesconto + "%"}));
				} else {
					pedido.vlTotalPedido = newVlTotalPedido;
				}
			}
		}
	}

	private void aplicaDescontoPctPedido(final Pedido pedido) throws SQLException {
		double desconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? ValueUtil.round(pedido.vlPctDescCliente) : ValueUtil.round(pedido.vlDesconto);
		if (pedido.vlTotalItens > 0) {
			double newVlTotalPedido = pedido.vlTotalPedido - ((pedido.vlTotalPedido * desconto) / 100);
			double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescontoPercentualPorPedido;
			double pctMinDesconto = ValueUtil.round(pedido.getCliente().vlPctMinDesconto);
			if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
				double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
				pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
			}
			//--
			if (pctMaxDesconto != 0 && desconto > pctMaxDesconto) {
				throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{desconto + "%", pctMaxDesconto + "%"}));
			} else if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() && pctMinDesconto != 0 && desconto < pctMinDesconto) {
				throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MINIMO_NAO_ATINGIDO, new String[]{desconto + "%", pctMinDesconto + "%"}));
			} else {
				pedido.vlTotalPedido = newVlTotalPedido;
			}
		}
	}
	
	@Override
	public String generateIdGlobal() throws SQLException {
		String nuPedido = super.generateIdGlobal();
		if (LavenderePdaConfig.isAdicionaCodigoUsuarioAoNumeroPedido()) {
			nuPedido = Session.getCdUsuario() + nuPedido;
		} else if (LavenderePdaConfig.isAdicionaCodigoUsuarioNumericoAoNumeroPedido()) {
			nuPedido = StringUtil.getStringValue(SessionLavenderePda.usuarioPdaRep.usuario.cdUsuarioNumerico) + nuPedido;
		}
		return nuPedido;
	}
	
	//@Override
	public void insert(final BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido)domain;
		pedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			pedido.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class);
			pedido.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		} else {
			pedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
		pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		pedido.nuPedido = generateIdGlobal();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		pedido.cdUsuario = Session.getCdUsuario();
		pedido.flModoEstoque = pedido.getFlModoEstoque();
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			pedido.vlPctMargemMin = LavenderePdaConfig.indiceMinimoRentabilidadePedido;
		}
		validate(domain);
		validateDuplicated(domain);
		setDadosAlteracao(domain);
		controlIgnoreFreteTipoPedido(pedido);
		getCrudDao().insert(domain);
		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_I);
		if ((LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() || LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) && !pedido.isPedidoBonificacao()) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES, ValueUtil.VALOR_SIM, pedido.getRowKey());
		}
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			if ((pedido.vlDesconto != 0) || (pedido.vlDescontoOld != 0)) {
				VerbaSaldoService.getInstance().updateVlSaldoByPedido(pedido);
			}
		}
		if (!LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() && (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isObrigaRelacionarPedidoTroca() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao())) {
			atualizaRelacionamento(pedido);
		}
		if (pedido.insertVisita) {
			VisitaService.getInstance().insertVisitaByPedido(pedido);
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			InfoFretePedidoService.getInstance().insertInfoFretePedido(pedido.getInfoFretePedido(), pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido, pedido.flOrigemPedido);
		}
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			criaVisitaPedido(pedido);
			VisitaService.getInstance().atualizaVisitaEmAndamento(pedido);
		}
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().savePedidoLog(TipoRegistro.INCLUSAO, pedido);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido() && ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados)) {
			PagamentoPedidoService.getInstance().insertDebitoBancario(pedido.getPagamentoPedido(), pedido);
		}
		if (LavenderePdaConfig.usaConfigPedidoProducao() && pedido.isPedidoVendaProducao()) {
			if (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao()) {
				PedidoRelacionadoService.getInstance().insereMultiplosPedidosRelacionados(pedido, pedido.pedidoRelacionadoList);
			} else {
				PedidoRelacionadoService.getInstance().insereUnicoPedidoRelacionado(pedido, pedido.pedidoRelacionado);				
			}
		}
		DocumentoAnexoService.getInstance().insertDocumentoAnexoList(pedido);
		FotoPedidoService.getInstance().atualizaFotosPedido(pedido);
		NotaCreditoPedidoService.getInstance().insereNotaCreditoPedido(pedido.notaCreditoPedidoList, pedido.nuPedido, pedido.flOrigemPedido);
		NotaCreditoService.getInstance().atualizaNotaCreditoUtilizada(pedido.notaCreditoPedidoList, pedido.cdCliente, ValueUtil.VALOR_SIM);
	}

	private void setaObservacaoCliente(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.mostraObservacaoCliente()) return;
		
		pedido.dsObservacaoCliente = pedido.getCliente().dsObservacao;
	}

	private void controlIgnoreFreteTipoPedido(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.isIgnoraCalculoFrete()) {
			pedido.cdTipoFrete = null;
		}
	}

	public void insertPedidoAndItensPedido(Pedido pedido) {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			String message = Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_ITENS;
			//--
			throw new ValidationException(MessageUtil.getMessage(message, ""));
		}
		try {
			insert(pedido);
			//--
			Vector itemPedidoList = pedido.itemPedidoList;
			pedido.itemPedidoList = new Vector(0);
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				itemPedido.cdEmpresa = pedido.cdEmpresa;
				itemPedido.cdRepresentante = pedido.cdRepresentante;
				itemPedido.flOrigemPedido = pedido.flOrigemPedido;
				itemPedido.nuPedido = pedido.nuPedido;
				itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				//--
				calculateItemPedido(pedido, itemPedido, true);
				insertItemPedido(pedido, itemPedido);
				
			}
			update(pedido);
		} catch (Throwable ex) {
			try {
	        	delete(pedido);
	        } catch (Throwable e) {
	        	// Não faz anda, apenas não exclui nenhum registro
	        }
	        //--
	        throw new ValidationException(MessageUtil.getMessage(Messages.CONSIGNACAO_MSG_PEDIDO_NAO_GERADO, ex.getMessage()));
		}
	}

	@Override
	public void update(final BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido)domain;
		calculate(pedido);
		validate(pedido);
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(pedido.itemPedidoList);
		}
		setDadosAlteracao(domain);
		controlIgnoreFreteTipoPedido(pedido);
		getCrudDao().update(pedido);
		//--
		if (!pedido.updateByClickNovoItemInPedido) {
			ParcelaPedidoService.getInstance().insertParcelasPedido(pedido);
		}
		//--
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 && !LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
			if ((pedido.vlDesconto != 0) || (pedido.vlDescontoOld != 0)) {
				VerbaSaldoService.getInstance().updateVlSaldoByPedido(pedido);
			}
		}
		if (!LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			VisitaService.getInstance().insertVisitaFoto(pedido.getVisita());
		}
		pedido.updateByClickSalvarItemPedido = false;
		//--
		if (LavenderePdaConfig.isUsaTipoFretePedido() && ValueUtil.isNotEmpty(pedido.getInfoFretePedido().nuPedido)) {
			InfoFretePedidoService.getInstance().insertOrUpdate(pedido.getInfoFretePedido());
		}
		if (!LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() && (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isObrigaRelacionarPedidoTroca() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao())) {
			atualizaRelacionamento(pedido);
		}
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().savePedidoLog(TipoRegistro.ALTERACAO, pedido);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			PagamentoPedido pagamentoPedido = pedido.getPagamentoPedido();
			if (pagamentoPedido != null) {
				if (ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados)) {
					PagamentoPedidoService.getInstance().updateDebitoBancario(pagamentoPedido);
				} else {
					PagamentoPedidoService.getInstance().deleteByPedido(pedido);
				}
			}
		}
	}
	

	public void atualizaStatusNfeFinalizado(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto() && pedido.getTipoPedido().isGeraNfe()) {
			pedido.flProcessandoNfeTxt = LavenderePdaConfig.cdStatusFinalizadoNfeTxt;
			getCrudDao().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_FLPROCESSANDONFETXT, pedido.flProcessandoNfeTxt, Types.VARCHAR);
		}
	}

	public void adicionaMsgPedidoComplementadoObsPedido(Pedido pedido) throws SQLException {
		StringBuffer complementoObs = new StringBuffer();
		String dsObservacao = StringUtil.getStringValue(pedido.getHashValuesDinamicos().get("DSOBSERVACAO"));
		complementoObs.append(dsObservacao);
		int index = dsObservacao.indexOf(Messages.PEDIDO_VENDA_COMPLEMENTADO);
		if (index == -1) {
			if (ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
				complementoObs.append(" ").append(Messages.PEDIDO_VENDA_COMPLEMENTADO).append(" ").append(pedido.nuPedidoComplementado);
			}
		} else {
			if (ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
				complementoObs.delete(index + Messages.PEDIDO_VENDA_COMPLEMENTADO.length(), complementoObs.length());
				complementoObs.append(" ").append(pedido.nuPedidoComplementado);
			} else {
				complementoObs.delete(index, complementoObs.length());
			}
		}
		updateColumn(pedido.getRowKey(), "DSOBSERVACAO", complementoObs.toString(), totalcross.sql.Types.VARCHAR);
	}
	
	public void atualizaRelacionamento(Pedido pedido) throws SQLException {
		Vector listPedido;
		Pedido pedidoRelacionado = new Pedido();
		pedidoRelacionado.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoRelacionado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante; 
		pedidoRelacionado.flOrigemPedido = pedido.flOrigemPedidoRelacionado;
		pedidoRelacionado.nuPedidoRelBonificacao = pedido.nuPedido;
		if (pedido.getTipoPedido() != null && ValueUtil.isNotEmpty(pedido.nuPedido)) {
			if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && !pedido.getTipoPedido().isFlTipoCreditoFrete() && !pedido.getTipoPedido().isFlTipoCreditoCondicao()) {
				listPedido = PedidoPdbxDao.getInstance().findAllByExample(pedidoRelacionado);
				if (ValueUtil.isNotEmpty(listPedido)) {
					pedidoRelacionado = (Pedido) listPedido.items[0];
					if (ValueUtil.valueEquals(pedido.flOrigemPedidoRelacionado, OrigemPedido.FLORIGEMPEDIDO_ERP)) {
						PedidoPdbxDao.getInstanceErp().updateColumn(pedidoRelacionado.getRowKey(), "NUPEDIDORELBONIFICACAO", "", totalcross.sql.Types.VARCHAR);
					}
					if (ValueUtil.valueEquals(pedido.flOrigemPedidoRelacionado, OrigemPedido.FLORIGEMPEDIDO_PDA)) {
					PedidoPdbxDao.getInstance().updateColumn(pedidoRelacionado.getRowKey(), "NUPEDIDORELBONIFICACAO", "", totalcross.sql.Types.VARCHAR);
				}
					if (LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao() && pedidoRelacionado.isPendente() && pedidoRelacionado.isPedidoFechado()) {
						updateFlPendente(pedidoRelacionado, ValueUtil.VALOR_NAO);
					}
				}
				if (pedido.getTipoPedido().isBonificacao() && ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao)) {
					pedidoRelacionado.nuPedido = pedido.nuPedidoRelBonificacao;
					if (ValueUtil.valueEquals(pedido.flOrigemPedidoRelacionado, OrigemPedido.FLORIGEMPEDIDO_ERP)) {
						Pedido ped = (Pedido) PedidoPdbxDao.getInstanceErp().findByRowKey(pedidoRelacionado.getRowKey());
						if (ped != null)
							PedidoPdbxDao.getInstanceErp().updateColumn(ped.getRowKey(), "NUPEDIDORELBONIFICACAO", pedido.nuPedido, totalcross.sql.Types.VARCHAR);
					} else {
					Pedido ped = (Pedido) PedidoPdbxDao.getInstance().findByRowKey(pedidoRelacionado.getRowKey());
					if (ped != null && (ped.isPedidoAberto() || ped.isPedidoFechado() || ped.isPedidoTransmitido())) {
						PedidoPdbxDao.getInstance().updateColumn(ped.getRowKey(), "NUPEDIDORELBONIFICACAO", pedido.nuPedido, totalcross.sql.Types.VARCHAR);
					}
				}
			}
			}
			if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && ValueUtil.isNotEmpty(pedido.nuPedido)) {
				pedidoRelacionado.nuPedidoRelBonificacao = "";
				pedidoRelacionado.nuPedidoRelTroca = pedido.nuPedido;
				listPedido = PedidoPdbxDao.getInstance().findAllByExample(pedidoRelacionado);
				if (listPedido.size() != 0) {
					pedidoRelacionado = (Pedido) listPedido.items[0];
					PedidoPdbxDao.getInstance().updateColumn(pedidoRelacionado.getRowKey(), "NUPEDIDORELTROCA", "", totalcross.sql.Types.VARCHAR);
				}  
				if (pedido.isPedidoTroca() && ValueUtil.isNotEmpty(pedido.nuPedidoRelTroca)) {
					pedidoRelacionado.nuPedido = pedido.nuPedidoRelTroca;
					Pedido ped = (Pedido) PedidoPdbxDao.getInstance().findByRowKey(pedidoRelacionado.getRowKey());
					if (ped != null && (ped.isPedidoAberto() || ped.isPedidoFechado() || ped.isPedidoTransmitido())) {
						PedidoPdbxDao.getInstance().updateColumn(pedidoRelacionado.getRowKey(), "NUPEDIDORELTROCA", pedido.nuPedido, totalcross.sql.Types.VARCHAR);
					}
				}
			}
		}
	}

	private void updateColumnNuPedidoRelBonificacaoByPedido(Pedido pedido, String flOrigemPedido) throws SQLException {
		if (ValueUtil.valueEquals(flOrigemPedido, OrigemPedido.FLORIGEMPEDIDO_ERP)) {
			PedidoPdbxDao.getInstanceErp().updateColumn(pedido.getRowKey(), "NUPEDIDORELBONIFICACAO", ValueUtil.VALOR_NI, totalcross.sql.Types.VARCHAR);
		} else if (ValueUtil.valueEquals(flOrigemPedido, OrigemPedido.FLORIGEMPEDIDO_PDA)) {
			PedidoPdbxDao.getInstance().updateColumn(pedido.getRowKey(), "NUPEDIDORELBONIFICACAO", ValueUtil.VALOR_NI, totalcross.sql.Types.VARCHAR);
		}
	}

	public void updatePedidoOnly(final BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido)domain;
		//--
		validate(pedido);
		//--
		calculate(pedido);
		//--
		getCrudDao().update(pedido);
	}

	//@Override
	public void delete(final BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido)domain;
		validateDeletePedido(pedido);
		//--
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			ParcelaPedidoService.getInstance().deleteParcelasPedido(pedido);
		}
		if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
			PagamentoPedidoService.getInstance().deleteByPedido(pedido);
		}
		NaoVendaProdPedidoService.getInstance().deleteByPedido(pedido);
		updateProdutoDesejado(pedido);
		if (LavenderePdaConfig.usaSelecaoDocAnexo()) {
			DocumentoAnexoService.getInstance().deleteDocAnexo(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey());
		}
		//Exclui também os itens
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido)pedido.itemPedidoList.items[i];
			item.auxiliarVariaveis.removendoPedido = true;
			if (LavenderePdaConfig.isAtualizaEstoqueComReservaCentralizada() && pedido.isPedidoReaberto) {
				reajustaEstoqueReservaOnDelete(pedido.flSituacaoReservaEstReabrePedido, item);
			}
			getItemPedidoService().delete(item);
			if (permiteDeletarVerbaConsumidaNoItem(pedido) && !item.isIgnoraControleVerba()) {
				if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
					item.dtEmissaoPedido = pedido.dtEmissao;
				}
				if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente && pedido.isPedidoBonificacao()) {
					VerbaClienteService.getInstance().deleteVlSaldo(item);
				} else {
					if (permiteDeletarVerbaGrupoSaldoPedidoBonificado(pedido) && (!LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada())) {
						VerbaService.getInstance().deleteVlSaldo(pedido, item);
					}
				}
			}
			ProdutoFaltaService.getInstance().deleteProdutoFalta(item);
			if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				ItemPedidoBonifCfgService.getInstance().deleteRegrasBonificacaoItemOnDeletePedido(item);
		}
		}
		ProdutoFaltaService.getInstance().deleteProdutoFaltaByPedido(pedido);
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ItemPedidoAgrSimilarService.getInstance().deleteItensPedidoAgrSimilar(pedido);
		}
		int sizeTroca = pedido.itemPedidoTrocaList.size();
		for (int i = 0; i < sizeTroca; i++) {
			ItemPedido itemTroca = (ItemPedido) pedido.itemPedidoTrocaList.items[i];
			getItemPedidoService().delete(itemTroca); 
			FotoItemTrocaService.getInstance().deleteAllFotosItemTroca(itemTroca);
		}
		//--
		try {
		super.delete(domain);
		} catch (ApplicationException e) {
			if (PedidoService.getInstance().findByPrimaryKey(domain) != null) {
				throw e;
			}
		}
		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_E, StringUtil.getStringValue(pedido.vlTotalPedido));
		//--
		if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente && pedido.getVlVerbaPedidoDisponivel() < 0) {
			VerbaSaldoService.getInstance().recalculateAndUpdateVerbaSaldoPda();
		}
		if (pedido.isPedidoAberto() || pedido.isPedidoFechado()) {
			if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 && !LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
				if ((pedido.vlDesconto != 0) || (pedido.vlDescontoOld != 0)) {
					VerbaSaldoService.getInstance().deleteVlSaldoByPedido(pedido);
				}
			}
			//Exclui as CCCli
			if (LavenderePdaConfig.usaCCClientePorTipoPedido) {
				CCCliPorTipoService.getInstance().updateCCCliPorTipoByPedido(pedido);
			}
			//Update Faceamento
			if (LavenderePdaConfig.usaFaceamento()) {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					FaceamentoEstoqueService.getInstance().findAllAndUpdateFaceamentoEstoqueByItemPedido(pedido.cdCliente, itemPedido.cdProduto);
				}
			}
			//Retira o relacionamento com pedido de venda
			if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao())) {
				Pedido pedidoRelacionado = new Pedido();
				pedidoRelacionado.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoRelacionado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante; //Representante da Sessão ou Rep. do Supervisor
				pedidoRelacionado.flOrigemPedido = pedido.flOrigemPedidoRelacionado;
				pedidoRelacionado.nuPedidoRelBonificacao = pedido.nuPedido;
				Vector listPedido = PedidoPdbxDao.getInstance().findAllByExample(pedidoRelacionado);
				if (listPedido.size() != 0) {
					pedidoRelacionado = (Pedido) listPedido.items[0];
					updateColumnNuPedidoRelBonificacaoByPedido(pedidoRelacionado, pedidoRelacionado.flOrigemPedido);
					if (LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao() && pedidoRelacionado.isPendente() && pedidoRelacionado.isPedidoFechado()) {
						updateFlPendente(pedidoRelacionado, ValueUtil.VALOR_NAO);
				}
			}
				pedido.nuPedidoRelBonificacao = ValueUtil.VALOR_NI;
			}
			//Retira o relacionamento de troca com pedido de venda
			if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && pedido.isPedidoTroca()) {
				Pedido pedidoRelacionado = new Pedido();
				pedidoRelacionado.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoRelacionado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante; //Representante da Sessão ou Rep. do Supervisor
				pedidoRelacionado.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
				pedidoRelacionado.nuPedidoRelTroca = pedido.nuPedido;
				Vector listPedido = PedidoPdbxDao.getInstance().findAllByExample(pedidoRelacionado);
				if (listPedido.size() != 0) {
					pedidoRelacionado = (Pedido) listPedido.items[0];
					PedidoPdbxDao.getInstance().updateColumn(pedidoRelacionado.getRowKey(), "NUPEDIDORELTROCA", "", totalcross.sql.Types.VARCHAR);
				}
				pedido.nuPedidoRelTroca = "";
			}
		}
		//Exclui as diferenças do pedido
		if (LavenderePdaConfig.relDiferencasPedido) {
			PedidoErpDifService.getInstance().deleteByPedido(pedido);
			ItemPedidoErpDifService.getInstance().deleteAllByPedido(pedido);
		}
		if (pedido.deletadoPelaIntefacePedido) {
			//Exclui o Pedido da Visita
			if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
				if (visita != null && ValueUtil.isNotEmpty(visita.cdCliente) && visita.cdCliente.equals(pedido.cdCliente)) {
					VisitaPedidoService.getInstance().excluirVisitaPedido(pedido, visita);
					if (!VisitaPedidoService.getInstance().isVisitaPossuiPedido(visita)) {
						visita.flVisitaPositivada = Visita.FL_VISITA_NAOPOSITIVADA;
						VisitaService.getInstance().updateColumn(visita.getRowKey(), "FLVISITAPOSITIVADA", Visita.FL_VISITA_NAOPOSITIVADA, totalcross.sql.Types.VARCHAR);
					}
				}
				if (visita != null && Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(visita.cdCliente)) {
					VisitaService.getInstance().delete(visita);
					SessionLavenderePda.reloadVisitaAndamento();
				}	
			//Exclui Visita Relacionada
			} else if (LavenderePdaConfig.isUsaAgendaDeVisitas() || LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgenda()) {
				VisitaService.getInstance().deleteByPedido(pedido);
			}
		}
		
		//Exclui as informações adicionais de frete
		if (LavenderePdaConfig.isUsaTipoFretePedido() && ValueUtil.isNotEmpty(pedido.getInfoFretePedido().nuPedido)) {
			InfoFretePedidoService.getInstance().delete(pedido.getInfoFretePedido());
		}
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().deleteByPedido(pedido);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			PagamentoPedidoService.getInstance().deleteByPedido(pedido);
	}
		if (LavenderePdaConfig.usaFotoPedidoNoSistema) {
			FotoPedidoService.getInstance().deleteFotoPedido(pedido);
	}
		//Excluir pedidos relacionados produção
		if (LavenderePdaConfig.usaConfigPedidoProducao()) {
			PedidoRelacionadoService.getInstance().deletePedidosRelacionados(pedido);
	}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && SolAutorizacaoService.getInstance().hasSolAutorizacaoPedido(pedido, true)) {
			try {
				SolAutorizacaoService.getInstance().deleteAllByPedido(pedido);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		ErroEnvioService.getInstance().removeErroEnvioPedido(pedido);
	}

	private boolean permiteDeletarVerbaGrupoSaldoPedidoBonificado(Pedido pedido) throws SQLException {
		if (!VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || !LavenderePdaConfig.usaPedidoBonificacaoConsomeVerbaGrupoProduto()) {
			return true;
		}
		return !pedido.isPedidoBonificacao() || pedido.isDeletePedidosPdaByPedidosErp;
	}

	private void validateDeletePedido(Pedido pedido) throws SQLException {
		if (!pedido.isPedidoAberto() || pedido.isDeletandoAutomaticamente) {
			return;
		}
		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && LavenderePdaConfig.geraVerbaPositiva) {
			if (pedido.vlVerbaPedidoPositivo > 0) {
				VerbaService.getInstance().validateDeleteSaldoPositivo(pedido);
			}
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() && !pedido.isPedidoBonificacao()) {
			Vector sugestoesVendaObrigatorias = VectorUtil.concatVectors(SugestaoVendaService.getInstance().findAllSugestoesValidas(SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, pedido.getCliente(), true), SugestaoVendaService.getInstance().findAllSugestoesValidas(SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, pedido.getCliente(), true));
			if (ValueUtil.isNotEmpty(sugestoesVendaObrigatorias)) {
				for (int i = 0; i < sugestoesVendaObrigatorias.size(); i++) {
					SugestaoVenda sugestaoVenda = (SugestaoVenda)sugestoesVendaObrigatorias.items[i];
					if (!SugestaoVendaService.getInstance().isSugestaoVendaLiberadoSenha(sugestaoVenda, pedido.getCliente()) && SugestaoVendaService.getInstance().isSugestaoVendaPendenteNoHistoricoPedidos(sugestaoVenda, pedido, true)) {
						throw new ValidationException(Messages.SUGESTAO_MSG_VALIDACAO_DELETE_PEDIDO);
					}
				}
			}
		}
	}

	private boolean permiteDeletarVerbaConsumidaNoItem(Pedido pedido) throws SQLException {
		return (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() && !pedido.isIgnoraControleVerba()) || 
				(((LavenderePdaConfig.persisteVerbaSaldoRep) ||
				(LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual ||
						LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto)) &&
						(pedido.isPedidoAberto() || pedido.isPedidoFechado() || pedido.isDeletePedidosPdaByPedidosErp) &&
						!LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex &&
						!pedido.isIgnoraControleVerba() &&
						!pedido.isSimulaControleVerba());
	}

	public void findItemPedidoList(final Pedido pedido) throws SQLException {
		findItemPedidoList(pedido, false);
	}

	public void findItemPedidoList(final Pedido pedido, boolean forceBusca) throws SQLException {
		if (pedido != null) {
			if (isRealizaBuscaItemPedidoList(pedido) || forceBusca) {
				pedido.itemPedidoList = new Vector(0);
				pedido.itemPedidoTrocaList = new Vector(0);
				//--
				ItemPedido itemPedidoFilter = new ItemPedido();
				itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
				itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
				itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
				itemPedidoFilter.nuPedido = pedido.nuPedido;
				itemPedidoFilter.isJoinDescQt = LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && !LavenderePdaConfig.usaAgrupadorSimilaridadeProduto;
				itemPedidoFilter.isBuscaFlKitProduto = LavenderePdaConfig.isUsaKitBaseadoNoProduto();
				Vector itemPedidoListTemp;
				if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
					itemPedidoListTemp = ItemPedidoService.getInstance().findAllByExamplePedidoComBonificacao(itemPedidoFilter);
				} else {
					itemPedidoListTemp = ItemPedidoService.getInstance().findAllByExampleUnique(itemPedidoFilter);
				}
				//--
				int size = itemPedidoListTemp.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemTemp = (ItemPedido) itemPedidoListTemp.items[i];
					itemTemp.pedido = pedido;
					if (pedido.getCliente() != null) {
						itemTemp.cdUfClientePedido = pedido.getCliente().dsUfPreco;
					}
					if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && ValueUtil.valueEqualsIfNotNull(ValueUtil.VALOR_SIM, itemTemp.flProdutoKitFromBusca)) {
						ItemKitPedidoService.getInstance().findItemKitPedidoList(itemTemp);
					}
					if (LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5()) {
						ItemPedidoGradeService.getInstance().findItemPedidoGradeList(itemTemp);
					}
					if (LavenderePdaConfig.apresentaMarcadoresProduto()) {
						if (ValueUtil.isEmpty(itemTemp.getProduto().cdMarcadores) && (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoInseridos)) {
							itemTemp.getProduto().cdMarcadores = MarcadorProdutoService.getInstance().findMarcadoresByProduto(itemTemp.getProduto(), pedido.cdCliente);
						}
					}
					if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
						itemTemp.infosPersonalizadas = ItemPedidoService.getInstance().getInfosPersonalizadasItemPedido(itemTemp);
					}
					if (itemTemp.isItemVendaNormal() || isItemVendaBonificacao(itemTemp)) {
						pedido.itemPedidoList.addElement(itemTemp);
					} else if (itemTemp.isItemTroca()) {
						pedido.itemPedidoTrocaList.addElement(itemTemp);
					}
					if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
						loadDescontoQuantidadeItemPedido(itemTemp);
				}
					//-- Para melhorar performance no resumo diário --
					if (pedido.itensBonificados != null && pedido.cdProdutoDistinctList != null){
						if (pedido.isPedidoBonificacao() || pedido.isPedidoTroca() || pedido.isOportunidade()) {
							if (pedido.isPedidoBonificacao()) {
								pedido.itensBonificados.add(itemTemp.cdProduto);
				}
						} else if (itemTemp.isItemVendaNormal() || isItemVendaBonificacao(itemTemp)) {
							if (pedido.cdProdutoDistinctList.indexOf(itemTemp.cdProduto) == -1) {
								pedido.cdProdutoDistinctList.addElement(itemTemp.cdProduto);
			}
		}
	}
					//--
				}
			}
		}
	}

	private boolean isItemVendaBonificacao(ItemPedido itemTemp) {
		return itemTemp.isItemBonificacao() && (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao || LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao());
	}

	private void loadDescontoQuantidadeItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.descQuantidade != null) {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null) {
				Vector descontoQuantidadeList = new Vector(1);
				descontoQuantidadeList.addElement(itemPedido.descQuantidade);
			itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(descontoQuantidadeList, itemPedido);
			DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
		}
	}
	}

	private boolean isRealizaBuscaItemPedidoList(final Pedido pedido) {
		if (OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedido.flOrigemPedido)) {
			return true;
		}
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop && ValueUtil.isEmpty(pedido.itemPedidoList)) {
			return true;
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList) && (pedido.vlTotalItens != 0 || pedido.vlTotalPedido != 0)) {
			return true;
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoTrocaList) && (pedido.vlTrocaRecolher != 0 || pedido.vlTotalTrocaPedido != 0 || pedido.vlTrocaEntregar != 0)) {
			return true;
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList) && pedido.isPedidoConsignado()) {
			return true;
		} 
		if (ValueUtil.isEmpty(pedido.itemPedidoList)  && LavenderePdaConfig.permiteItemPedidoComQuantidadeZero) {
			return true;
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList) && pedido.isGondola()){
			return true;
		}
		return false;
	}

	public Vector findItemPedidoListByFaceamentoEstoque(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			Vector faceamentoEstoqueListTemp = FaceamentoEstoqueService.getInstance().findAllFaceamentoEstoqueByDataAtual(pedido);
			int size = faceamentoEstoqueListTemp.size();
			//--
			Vector itemPedidoList = new Vector(size);
			for (int i = 0; i < size; i++) {
				FaceamentoEstoque faceamentoEstoque = (FaceamentoEstoque) faceamentoEstoqueListTemp.items[i];
				//--
				ItemPedido itemPedido = new ItemPedido();
				itemPedido.pedido = pedido;
				itemPedido.faceamentoEstoque = faceamentoEstoque;
				itemPedido.cdEmpresa = pedido.cdEmpresa;
				itemPedido.cdRepresentante = pedido.cdRepresentante;
				itemPedido.flOrigemPedido = pedido.flOrigemPedido;
				itemPedido.nuPedido = pedido.nuPedido;
				itemPedido.setQtItemFisico(ValueUtil.isEmpty(faceamentoEstoque.qtSugestaoVendaRep) ? faceamentoEstoque.qtSugestaoVenda : faceamentoEstoque.qtSugestaoVendaRep) ;
				itemPedido.cdProduto = faceamentoEstoque.cdProduto;
				itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
				itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
				itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
				itemPedido.cdUnidade = itemPedido.getProduto().cdUnidade;
				//--
				if (pedido.getCliente() != null) {
					itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
				}
				if (itemPedido.getQtItemFisico() > 0) {
					itemPedido.getProduto();
					itemPedidoList.addElement(itemPedido);
				}
			}
			faceamentoEstoqueListTemp = null;
			return itemPedidoList;
		}
		return new Vector(0);
	}

	private void findItemPedidoSummaryList(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			if ((pedido.vlTotalItens != 0 && ValueUtil.isEmpty(pedido.itemPedidoList)) || ((pedido.vlTrocaEntregar != 0 || pedido.vlTrocaRecolher!= 0) && ValueUtil.isEmpty(pedido.itemPedidoTrocaList))) {
				ItemPedido itemPedidoFilter = new ItemPedido();
				itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
				itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
				itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
				itemPedidoFilter.nuPedido = pedido.nuPedido;
				Vector listTemp = ItemPedidoService.getInstance().findAllByExampleSummaryUnique(itemPedidoFilter);
				//--
				int size = listTemp.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemTemp = (ItemPedido) listTemp.items[i];
					itemTemp.pedido = pedido;
					if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
						if ((itemTemp.getProduto() != null) && itemTemp.getProduto().isKit()) {
							ItemKitPedidoService.getInstance().findItemKitPedidoList(itemTemp);
						}
					}
					if (LavenderePdaConfig.isConfigGradeProduto()) {
						ItemPedidoGradeService.getInstance().findItemPedidoGradeList(itemTemp);
					}
					if (itemTemp.isItemVendaNormal() || (itemTemp.isItemBonificacao() && 
							  (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.isUsaPoliticaBonificacao()))) {
						pedido.itemPedidoList.addElement(itemTemp);
					} else if (itemTemp.isItemTroca()) {
						pedido.itemPedidoTrocaList.addElement(itemTemp);
					}
				}
				listTemp = null;
			}
		}
	}

	public void findParcelaPedidoList(final Pedido pedido) throws SQLException {
		if (pedido != null && pedido.vlTotalPedido != 0 && !pedido.updateByClickNovoItemInPedido) {
			ParcelaPedido parcelaPedidoFilter = new ParcelaPedido();
			parcelaPedidoFilter.cdEmpresa = pedido.cdEmpresa;
			parcelaPedidoFilter.cdRepresentante = pedido.cdRepresentante;
			parcelaPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			parcelaPedidoFilter.nuPedido = pedido.nuPedido;
			pedido.parcelaPedidoList = ParcelaPedidoService.getInstance().findAllByExample(parcelaPedidoFilter);
		}
	}

	private boolean recalculateFreteItensPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem && !LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				reloadValoresDosItensPedido(pedido, true, false);
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					ItemPedidoService.getInstance().update(itemPedido);
				}
				return true;
			}
		} else 	if (LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso()) {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_QTD;
					if (LavenderePdaConfig.usaFreteApenasTipoFob) {
						itemPedido.vlItemPedidoFrete = pedido.isTipoFreteFob() ? itemPedido.getItemTabelaPreco().vlPrecoFrete : 0;
					}
					calculateItemPedido(pedido, itemPedido, true);
					ItemPedidoPdbxDao.getInstance().update(itemPedido);
				}
				return true;
			}
		}
		return false;
	}

	public void updateItensPedidoAfterChanges(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			boolean transacaoJaAberta = getItemPedidoService().emTransacao;
			if (!transacaoJaAberta) {
				CrudDbxDao.getCurrentDriver().startTransaction();
				getItemPedidoService().emTransacao = true;
			}
			try {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				getItemPedidoService().update(itemPedido);
			}
			} finally {
				if (!transacaoJaAberta) {
					CrudDbxDao.getCurrentDriver().finishTransaction();
					getItemPedidoService().emTransacao = false;
		}
	}
		}
	}

	public void updateSimplesItensPedidoAfterChanges(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				getItemPedidoService().updateItemSimples(itemPedido);
			}
		}
	}

	//--------------------------------------------------------------------------------
	// Métodos de controle de descontos aplicados ao fechar o pedido
	//--------------------------------------------------------------------------------

	public boolean isAplicaIndiceFinanceiroClienteNoPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido || LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
			Cliente cliente = pedido.getCliente();
			if (cliente != null) {
				int size = pedido.itemPedidoList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					//--
					boolean indiceClienteEspecial = itemPedido.isIndiceFinanceiroEspecialCliente();
					double vlIndiceFinanceiro = cliente.getIndiceFinanceiro(indiceClienteEspecial);

					getItemPedidoService().aplicaIndiceFinanceiroClienteNoItemPedido(itemPedido, vlIndiceFinanceiro, indiceClienteEspecial);
				}
				return true;
			}
		}
		return false;
	}

	public double getVlPedidoComDescontoProgressivo(Pedido pedido) throws SQLException {
		double vlTotalPedidoComDescProg = 0;
		double vlPctDescProg = pedido.getVlPctDescProgressivo();
		int size = pedido.itemPedidoList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				vlTotalPedidoComDescProg += (itemPedido.vlPctAcrescimo <= 0 ? getItemPedidoService().getVlItemPedidoComDescontoProgressivo(itemPedido, vlPctDescProg) : itemPedido.vlItemPedido) * itemPedido.getQtItemFisico();
			}
		}
		return vlTotalPedidoComDescProg;
	}

	public boolean isAplicaDescontoProgressivoNoPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) {
			if (LavenderePdaConfig.bloqueiaDescontoProgressivoClientesEspeciais) {
				if (ValueUtil.VALOR_SIM.equals(pedido.getCliente().flEspecial)) {
					return false;
				}
			}
			if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha()) {
				return false;
			}
			double vlPctDescProg = pedido.getVlPctDescProgressivo();
			//--
			if (LavenderePdaConfig.permiteEditarDescontoProgressivo) {
				vlPctDescProg = pedido.vlPctDescProgressivo;
			}
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				itemPedido.descontoProgressivoAplicado = false;
				if (LavenderePdaConfig.ignoraDescontroProgressivoProdutoPromocional && ItemPedidoService.getInstance().isItemPedidoPromocional(itemPedido)) {
					continue;
				}
				if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
					getItemPedidoService().aplicaDescontoProgressivoNoItemPedidoConsumindoFlex(itemPedido, vlPctDescProg);
				} else {
					itemPedido.descontoProgressivoAplicado = getItemPedidoService().isAplicaDescontoProgressivoNoItemPedido(itemPedido, vlPctDescProg);
				}
			}
			pedido.vlPctDescProgressivo = vlPctDescProg;
			return true;
		}
		return false;
	}
	
	private boolean isAplicaDescontoProgressivoPorMixDeProdutos(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			int qtItensDiferentesInseridosNoPedido = getItemPedidoService().getQtItensDiferentesInseridosNoPedido(pedido.itemPedidoList);
			double vlPctDescProgMix = DescProgQtdService.getInstance().getVlPctDescProgressivoMix(pedido.cdEmpresa, pedido.cdRepresentante, qtItensDiferentesInseridosNoPedido);
			DescProgQtdService.getInstance().aplicaDescontoProgressivoPorMixDeProdutos(pedido.itemPedidoList, vlPctDescProgMix);
			pedido.vlPctDescProgressivoMix = vlPctDescProgMix;
			return true;
		}
		return false;
	}

	public boolean isAplicaDescontoCCPNoPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescontoCCPPorItemFinalPedido) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				double vlDescCCP = DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, pedido.getCliente());
				getItemPedidoService().aplicaDescontoCCPNoItemPedido(itemPedido, vlDescCCP);
			}
			return true;
		}
		return false;
	}

	public void aplicaMaiorDescontoFimPedidoNoPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaSomenteMaiorDescontoPorItemFinalPedido) {
			double vlPctDescProgressivo = 0.0;
			if (LavenderePdaConfig.aplicaDescontoProgressivoPorItemFinalPedido || LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()) {
				pedido.getVlPctDescProgressivo();
			}
			Cliente cliente = pedido.getCliente();
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				double vlDescCCP = DescontoCCPService.getInstance().getDescCCPItemPedido(itemPedido, cliente);
				//--
				getItemPedidoService().aplicaMaiorDescontoNoItemPedido(itemPedido, vlPctDescProgressivo, cliente, vlDescCCP);
			}
		}
	}

	public void retiraDescontosDoFimDoPedido(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				getItemPedidoService().retiraDescontosDoItemPedido(pedido, itemPedido);
				getItemPedidoService().calculateVlTotalItemPedido(pedido, itemPedido);
			}
		}
	}

	public void retiraDescontosDoFimDoPedidoConsumindoFlex(Pedido pedido) throws SQLException {
		if (!pedido.isIgnoraControleVerba()) {
			double vlPctDescProg = pedido.getVlPctDescProgressivo();
			if (pedido != null) {
				int size = pedido.itemPedidoList.size();
				pedido.vlVerbaPedido = 0;
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					ItemPedidoService.getInstance().retiraDescontosDoFimDoItemPedidoConsumindoFlex(itemPedido, vlPctDescProg);
					pedido.vlVerbaPedido += itemPedido.vlVerbaItem;
					pedido.vlVerbaPedidoOld = pedido.vlVerbaPedido;
					getItemPedidoService().calculateVlTotalItemPedido(pedido, itemPedido);
				}
			}
		}
	}

	public void retiraDescontosDoPedidoRepondoFlex(Pedido pedido) throws SQLException {
		if (pedido != null) {
			double vlPctDescProg = pedido.vlPctDescProgressivo;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				ItemPedidoService.getInstance().retiraDescontosDoFimDoItemPedidoConsumindoFlex(itemPedido, vlPctDescProg);
				getItemPedidoService().calculateVlTotalItemPedido(pedido, itemPedido);
			}
		}
	}

	public void updatePedidoAndItens(final Pedido pedido) throws SQLException {
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				ItemPedidoService.getInstance().calculateVlTotalItemPedido(pedido, itemPedido);
				ItemPedidoPdbxDao.getInstance().update(itemPedido);
			}
			updatePedidoOnly(pedido);
		}
	}

	//--------------------------------------------------------------------------------
	// Métodos de controle de fechamento dos pedidos
	//--------------------------------------------------------------------------------
	public void validateFecharPedido(final Pedido pedido, boolean onFechamentoPedido) throws SQLException {
		validateFecharPedido(pedido, onFechamentoPedido, false);
	}

	public void validateFecharPedido(final Pedido pedido, boolean onFechamentoPedido, boolean fromListaPedido) throws SQLException {
		validaQtParcelasCondicaoPagamento(pedido);
		if (LavenderePdaConfig.usaValidacaoCamposDinamicosObrigatoriosAoFecharPedido && onFechamentoPedido) {
			validateCamposDinamicos(pedido);
		}
		if (pedido.isPedidoTroca() && onFechamentoPedido) {
			//Quantidade de itens de troca
			if (ValueUtil.isEmpty(pedido.itemPedidoTrocaList)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_ITENS, pedido.nuPedido));
			}
			validateObrigaRelacionarPedidoTroca(pedido);
			if (LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
				if (!ItemPedidoService.getInstance().validaItensPedidoOriginalBonificacaoTroca(pedido.nuPedidoRelTroca, pedido.itemPedidoTrocaList)) {
					throw new ValidationException(Messages.PEDIDO_MSG_ITEM_FORA_PEDIDO_ORIGINAL_BONIFICACAOTROCA);
				}
				if (LavenderePdaConfig.isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal()) {
					if (LavenderePdaConfig.isUsaPercQuantidadeDoItemOriginalBonificacaoTroca()) {
						validatePctQtdItemOriginalNaBonificacaoTroca(pedido);
					} else {
						ItemPedidoService.getInstance().validaQtItemFisicoMaxDosItensPedidoBonificacaoTroca(pedido);
			}
				}
			}
			return;
		}
		if (onFechamentoPedido && LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			validaRecalculoPedidoNoFechamento(pedido);
			if (hasItensErroRecalculo(pedido.itemPedidoList)) {
				throw new ValidationException(Messages.FECHAR_PEDIDO_ITENS_ERRO_RECALCULO);
			}
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			PesoFaixaService.getInstance().validateFaixaIdealPedido(pedido);
		}
		switch (PedidoService.validationFechamentoCount) {
			case 0: {
				PedidoService.validationFechamentoCount++;
				//Status aberto
				if (!pedido.isPedidoAberto() && !pedido.isPedidoConsignado()) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_STATUS_DIFERENTE_ABERTO, StatusPedidoPdaService.getInstance().getDsStatusPedidoAberto()));
				}
			}
			case 1: {
				PedidoService.validationFechamentoCount++;
				//Quantidade de itens
				if (ValueUtil.isEmpty(pedido.itemPedidoList) && !(ValueUtil.isNotEmpty(pedido.itemPedidoTrocaList) && ((LavenderePdaConfig.percentualLimiteTrocaNoPedido == 0 || LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido())))) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_ITENS, pedido.nuPedido));
				}
			}
			case 2: {
				PedidoService.validationFechamentoCount++;
				//vlTotalPedido
				if (!LavenderePdaConfig.permiteItemPedidoComQuantidadeZero && pedido.vlTotalPedido == 0) {
					if (!LavenderePdaConfig.permiteValorZeroPedidoEItem) {
						if ((LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) && (pedido.vlTotalPedido == 0 && (!LavenderePdaConfig.isPermiteBonificarProdutoSemLimitesItensNoPedido() || (pedido.vlBonificacaoPedido == 0))) && (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.vlTotalTrocaPedido == 0)) {
							throw new ValidationException(Messages.PEDIDO_MSG_VLTOTAL_VAZIO_NO_PEDIDO);
						}
					}
				}
			}
			case 3: {
				PedidoService.validationFechamentoCount++;
				//Quantidade Minima de Itens
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.quantidadeMinimaItensPedido != 0) {
					if (pedido.itemPedidoList.size() < LavenderePdaConfig.quantidadeMinimaItensPedido) {
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_MINIMA_ITENS, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.quantidadeMinimaItensPedido), StringUtil.getStringValueToInterface(pedido.itemPedidoList.size())}));
					}
				}
			}
			case 4: {
				PedidoService.validationFechamentoCount++;
				//Quantidade mínima de itens para pedido com itens promocionais
				if (!pedido.isPedidoBonificacao() && (LavenderePdaConfig.qtdMinItensParaPermitirItensPromocionais > 0)) {
					if (pedido.isWithItensPromocionais()) {
						int qtItensValidos = pedido.getQtItensValidacaoItensPromocionais();
						if (qtItensValidos < LavenderePdaConfig.qtdMinItensParaPermitirItensPromocionais) {
							String mensagem = Messages.ITEMPEDIDO_MSG_QT_MIN_ITENS_PROMOCIONAIS;
							if (LavenderePdaConfig.isUsaKitProduto()) {
								StringBuffer strBuffer = new StringBuffer();
								mensagem = strBuffer.append(mensagem.substring(0, mensagem.length() - 1)).append(Messages.ITEMPEDIDO_MSG_QT_MIN_ITENS_PROMOCIONAIS_COMPLEMENTO).toString();
							}
							throw new ValidationException(MessageUtil.getMessage(mensagem, new String[] {StringUtil.getStringValueToInterface(LavenderePdaConfig.qtdMinItensParaPermitirItensPromocionais), StringUtil.getStringValueToInterface(qtItensValidos)}));
						}
					}
				}
			}
			case 5: {
				PedidoService.validationFechamentoCount++;
				//Valor Minimo Para o Pedido definido pela Tabela de Preço
				if (!LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido() && !pedido.isPedidoBonificacao() && !(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
					if(LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem()) {
						validaQtMinValorTabPreco(pedido);
					}
					else if(LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco()) {
					TabelaPreco tabelaPreco = pedido.getTabelaPreco();
					if (tabelaPreco != null) {
						if (pedido.getValorTotalValidadoComImpostos() < tabelaPreco.qtMinValor) {
							Object[] params = {StringUtil.getStringValue(tabelaPreco.toString()), StringUtil.getStringValueToInterface(tabelaPreco.qtMinValor), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_TABPRECO, params));
						}
					}
				}
			}
			}
			case 6: {
				PedidoService.validationFechamentoCount++;
				//Quantidade Minima de Caixas inseridas no Pedido
				validateQuantidadeMinimaCaixasPedido(pedido.isPedidoBonificacao(), pedido.getQtItensFaturamento());
			}
			case 7: {
				PedidoService.validationFechamentoCount++;
				//Valor Mínimo para o pedido defindo pelo Tipo de Pagamento
				if (!LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido() && !pedido.isPedidoBonificacao() && LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento) {
					TipoPagamento tipoPagamento = pedido.getTipoPagamento();
					if (tipoPagamento != null) {
						if (pedido.getValorTotalValidadoComImpostos() < tipoPagamento.qtMinValor) {
							Object[] params = {StringUtil.getStringValue(tipoPagamento.toString()), StringUtil.getStringValueToInterface(tipoPagamento.qtMinValor), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_TPPAGAMENTO, params));
						}
					}
				}
			}
			case 8: {
				PedidoService.validationFechamentoCount++;
				validaValorMinimoParcela(pedido);
				validaValorMaximoParcela(pedido);
			}
			case 9: {
				PedidoService.validationFechamentoCount++;
				//Valor Percentual mínimo para o Desconto Progressivo do Pedido
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.permiteEditarDescontoProgressivo) {
					double vlPctDescProgressivo = pedido.getVlPctDescProgressivo();
					if (pedido.vlPctDescProgressivo > vlPctDescProgressivo) {
						Object[] params = {StringUtil.getStringValueToInterface(pedido.vlPctDescProgressivo), StringUtil.getStringValueToInterface(vlPctDescProgressivo)};
						throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_EXTRAPOLADO_MAXIMO, params));
					}
				}
			}
			case 10: {
				PedidoService.validationFechamentoCount++;
				//Quantidade mínima por grupo de produtos, baseado no desconto por grupo de produtos
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
					DescComiFaixaService.getInstance().verificaQtdeMinimaDosItensPedido(pedido, true);
				}
			}
			case 11: {
				PedidoService.validationFechamentoCount++;
				//Kit contemplado
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isUsaKitProdutoAberto()) {
					ItemKitService.getInstance().verificaQtdeMinimaDosItensPedidoPorKit(pedido);
				}
			}
			case 12: {
				PedidoService.validationFechamentoCount++;
				//Rentabilidade positiva
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !LavenderePdaConfig.isUsaControleRentabilidadePorFaixa()) {
					if (pedido.vlRentabilidade < 0 && !LavenderePdaConfig.permiteRentabilidadeNegativaPedido) {
						throw new ValidationException(Messages.PEDIDO_MSG_RENTABILIDADE_NEGATIVA);
					}
				}
			}
			case 13: {
				PedidoService.validationFechamentoCount++;
				//usaLeadTimePadraoClienteNoPedido
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.usaLeadTimePadraoClienteNoPedido && !LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
					int nuDiasPrevisaoEntrega = pedido.getCliente().nuDiasPrevisaoEntrega;
					Date validaDtEntrega = nuDiasPrevisaoEntrega > 0 ? getDataEntregaConsiderandoFeriadoEFinaisDeSemana(new Date(), pedido.getCliente().nuDiasPrevisaoEntrega, pedido) : new Date();
					if (pedido.dtEntrega.isBefore(validaDtEntrega)) {
						throw new ValidationException(Messages.PEDIDO_DTENTREGA_ANTERIOR_PERMITIDO_CLIENTE);
					}
				}
			}
			case 14: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && onFechamentoPedido) {
					PedidoService.getInstance().isAplicaDescontoProgressivoNoPedido(pedido);
					int size = pedido.itemPedidoList.size();
					if (size > 0) {
						double vlSaldo  = ValueUtil.round(VerbaService.getInstance().getVlSaldo((ItemPedido)pedido.itemPedidoList.items[0]));
						double vlTotalVerbaConsumidaByDescProgs = 0;
						for (int i = 0; i < size; i++) {
							ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
							vlTotalVerbaConsumidaByDescProgs += itemPedido.vlVerbaItem;
						}
						vlTotalVerbaConsumidaByDescProgs = ValueUtil.round(vlTotalVerbaConsumidaByDescProgs);
						if ((vlTotalVerbaConsumidaByDescProgs * -1 > vlSaldo) && !pedido.fecharPedidoComVerbaNeg) {
							throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_SALDO_NEGATIVO, new String[] {StringUtil.getStringValueToInterface(vlSaldo + vlTotalVerbaConsumidaByDescProgs)}));
						}
					}
					PedidoService.getInstance().retiraDescontosDoFimDoPedidoConsumindoFlex(pedido);
				}
			}
			case 15: {
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.usaDescQuantidadePorPacote) {
					DescontoPacoteService.getInstance().verificaPctMaxDescontoPorPacote(pedido, true);
				}
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isUsaDescontoQtdPorGrupo()) {
					DescontoGrupoService.getInstance().verificaPctMaxDescontoPorGrupoProduto(pedido, true);
				}
				PedidoService.validationFechamentoCount++;
			}
			case 16: {
				PedidoService.validationFechamentoCount++;
				validateFecharPedidoRestricaoQuantidade(pedido);
			}
			case 17: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoLigado() && LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivo() && onFechamentoPedido) {
					DescProgQtdService.getInstance().validateDescontoMaiorDescontoProgressivo(pedido);
				}
			}
			case 18: {
				PedidoService.validationFechamentoCount++;
				if (validateApenasItemPedidoOriginalNaBonificacao(pedido) && !LavenderePdaConfig.isUsaVariosPedidosBonificados()) {
					if (!ItemPedidoService.getInstance().validaItensPedidoOriginalBonificacaoTroca(pedido.nuPedidoRelBonificacao, pedido.itemPedidoList)) {
						throw new ValidationException(Messages.PEDIDO_MSG_ITEM_FORA_PEDIDO_ORIGINAL_BONIFICACAOTROCA);
					}
				}
				if (validateUsaPedidoComplementar(pedido)) {
					throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_FECHAR_PEDIDO_COMPLEMENTADO);
				}
			}
			case 19: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && pedido.getCliente().vlMaxPedido != 0 && pedido.vlTotalPedido > pedido.getCliente().vlMaxPedido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.CLIENTE_VLMAXPEDIDO_ULTRAPASSADO, pedido.getCliente().vlMaxPedido));
				}
			}
			case 20: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaTipoFretePedido() && !pedido.isAtingiuPesoMinimo()) {
					TipoFrete tipoFrete = pedido.getTipoFrete();
					if (tipoFrete != null) {
						Object [] params = {StringUtil.getStringValueToInterface(tipoFrete.vlPesoMinimo), StringUtil.getStringValueToInterface(pedido.qtPeso)};
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_NAO_ATINGIDO, params));
					}
				}
			}
			case 21: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaTipoFretePedido()) {
					TipoFrete tipoFrete = pedido.getTipoFrete();
					if (LavenderePdaConfig.obrigaInfoAdicionalFreteNoPedido && tipoFrete.isUsaInfoAdicional()) {
						if (ValueUtil.isEmpty(pedido.getInfoFretePedido().cdTipoVeiculo) && pedido.isTipoFreteFob()) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_TIPO_VEICULO_NAO_INFORMADO, pedido.getInfoFretePedido().cdTipoVeiculo));
						}
						if (ValueUtil.isEmpty(pedido.getInfoFretePedido().flTaxaEntrega)) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_POSSUI_TAXA_ENTREGA, pedido.getInfoFretePedido().flTaxaEntrega));
						}
						if (ValueUtil.isEmpty(pedido.getInfoFretePedido().flAjudante)) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_POSSUI_AJUDANTE, pedido.getInfoFretePedido().flAjudante));
						}
						if (ValueUtil.isEmpty(pedido.getInfoFretePedido().flAntecipaEntrega)) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_POSSUI_ENTREGA_ANTECIPADA, pedido.getInfoFretePedido().flAntecipaEntrega));
						}
						if (ValueUtil.isEmpty(pedido.getInfoFretePedido().flAgendamento)) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_PRECISA_AGENDAMENTO, pedido.getInfoFretePedido().flAgendamento));
						}
					}
					if (ValueUtil.VALOR_SIM.equals(pedido.getInfoFretePedido().flTaxaEntrega)) {
						if (pedido.getInfoFretePedido().vlTaxaEntrega == 0) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_VALOR_TAXA_ENTREGA_NAO_INFORMADO, pedido.getInfoFretePedido().vlTaxaEntrega));
						}
					}
					if (ValueUtil.VALOR_SIM.equals(pedido.getInfoFretePedido().flAjudante)) {
						if (pedido.getInfoFretePedido().qtAjudante == 0) {
							throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_INFO_FRETE_QTD_AJUDANTE_NAO_INFORMADO, pedido.getInfoFretePedido().qtAjudante));
						}
					}
					if (tipoFrete != null && tipoFrete.vlMinimo > 0 && pedido.vlTotalPedido < tipoFrete.vlMinimo && !pedido.getTipoPedido().isIgnoraVlMinPedido()) {
						Object [] params = {StringUtil.getStringValueToInterface(pedido.vlTotalPedido), StringUtil.getStringValueToInterface(tipoFrete.vlMinimo)};
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_TIPOFRETE_VALOR_MINIMO_NAO_ATINGIDO, params));
					}
				}
			}
			case 22: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isGeraParcelasEmPercentual() && !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao()) {
					ParcelaPedidoService.getInstance().validateParcelas(pedido);
				}
			}
			case 23: {
				PedidoService.validationFechamentoCount++;
				validateDataEntrega(pedido, fromListaPedido);
			}
			case 24: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.usaControlePesoPedidoPorCondPagto && pedido.getCondicaoPagamento().vlMinPeso != 0 && ValueUtil.round(pedido.qtPeso) < ValueUtil.round(pedido.getCondicaoPagamento().vlMinPeso)) {
					Object [] params = {StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento().vlMinPeso), StringUtil.getStringValueToInterface(pedido.qtPeso)};
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_COND_PAGTO_NAO_ATINGIDO, params));
				}
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.usaControlePesoPedidoPorEmpresa && pedido.getEmpresa().vlMinPeso != 0 && ValueUtil.round(pedido.qtPeso) < ValueUtil.round(pedido.getEmpresa().vlMinPeso)) {
					Object [] params = {StringUtil.getStringValueToInterface(pedido.getEmpresa().vlMinPeso), StringUtil.getStringValueToInterface(pedido.qtPeso)};
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_EMPRESA_NAO_ATINGIDO, params));
				}
			}
			case 25: {
				PedidoService.validationFechamentoCount++;
				PedidoService.getInstance().validaPesoMaximoPedido(pedido);
			}
			case 26: {
				PedidoService.validationFechamentoCount++;
				validaQuilometragemETempoDoPedido(pedido);
			}
			case 27: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido) {
					validaEnderecoEntregaPedido(pedido);
				}
			}
			case 28: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0 && onFechamentoPedido && ValueUtil.isEmpty(pedido.cdEnderecoCobranca)) {
					boolean obrigaInclusaoEnderecoCobranca = LavenderePdaConfig.isSempreObrigaInclusaoEnderecoCobranca();
					if (!obrigaInclusaoEnderecoCobranca && LavenderePdaConfig.isObrigaInclusaoEnderecoCobrancaCasoExistaRegistroDeEnderecoCobranca()) {
						obrigaInclusaoEnderecoCobranca = ClienteEnderecoService.getInstance().isExisteEnderecosEntregaOuCobrancaParaCliente(pedido, true);
					}
					if (obrigaInclusaoEnderecoCobranca) {
						throw new ValidationException(Messages.PEDIDO_MSG_VALIDACAO_ENDERECO_COBRANCA);
					}
				}
			}
			case 29: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && ! pedido.isPedidoBonificacao()) {
					validateLoteFechamentoPedido(pedido);
				}
			}
			case 30: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal() && LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && pedido.isPedidoBonificacao()) {
					if (LavenderePdaConfig.isUsaPercQuantidadeDoItemOriginalBonificacaoTroca()) {
					validatePctQtdItemOriginalNaBonificacaoTroca(pedido);
					} else {
						ItemPedidoService.getInstance().validaQtItemFisicoMaxDosItensPedidoBonificacaoTroca(pedido);
				}
					
			}
			}
			case 31: {
				PedidoService.validationFechamentoCount++;
				if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[pedido.itemPedidoList.size() - 1];
					ItemPedidoService.getInstance().validaItensPorLimiteCredito(pedido, itemPedido, true);
				}
			}
			case 32: {
				PedidoService.validationFechamentoCount++;
				validateTabelaPreco(pedido); 
			}
			case 33: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
					TipoFrete tipoFrete = pedido.getTipoFrete();
					double vlPctMaxDescontoFrete = tipoFrete == null ? 0d : tipoFrete.vlPctMaxDesconto;
					if (pedido.vlPctDescFrete != 0 && (tipoFrete == null || pedido.vlPctDescFrete > ValueUtil.round(vlPctMaxDescontoFrete))) {
						Object [] params = {StringUtil.getStringValueToInterface(pedido.vlPctDescFrete), StringUtil.getStringValueToInterface(vlPctMaxDescontoFrete)};
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_TIPOFRETE_DESCONTO_MAXIMO_ULTRAPASSADO, params));
					}
				}
			}
			case 34: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
					if (pedido.vlPctDescontoCondicao != 0 && (pedido.getCondicaoPagamento() == null || pedido.vlPctDescontoCondicao > ValueUtil.round(pedido.getCondicaoPagamento().vlPctDescontoTotalPedido))) {
						Object [] params = {StringUtil.getStringValueToInterface(pedido.vlPctDescontoCondicao), StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento() == null ? 0d : pedido.getCondicaoPagamento().vlPctDescontoTotalPedido)};
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_CONDICAO_PAGAMENTO_DESCONTO_MAXIMO_ULTRAPASSADO, params));
					}
				}
			}
			case 35: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
					PagamentoPedidoService.getInstance().validaPagamentosPedido(pedido);
				}
			}
			case 36: {
				PedidoService.validationFechamentoCount++;
				validateFecharPedidoRestricaoPromocionalPedido(pedido);
			}
			case 37: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() || LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedidoErp()) {
					ItemPedidoService.getInstance().validaPctMaxTrocaRecolherPedido(pedido, null);
				}
			}
			case 38: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaDataCarregamentoPedido) {
					if (ValueUtil.isEmpty(pedido.dtCarregamento)) {
						throw new ValidationException(Messages.PEDIDO_DTCARREGAMENTO_NAO_PREENCHIDO);
					} else if (pedido.dtCarregamento.isAfter(pedido.dtEntrega)) {
						throw new ValidationException(Messages.PEDIDO_DTCARREGAMENTO_DEPOIS_ENTREGA);
					} else if (pedido.dtCarregamento.isBefore(DateUtil.getCurrentDate())) {
						throw new ValidationException(Messages.PEDIDO_DTCARREGAMENTO_ANTES_ATUAL);
					}
				}
			}
			case 39: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedidoAoSalvarPedido() && !pedido.ignoraValidacaoProdutosPendentes && !onFechamentoPedido
						&& !pedido.getCliente().isClienteDefaultParaNovoPedido() && !pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
					try {
						validaProdutosPendentes(pedido);
					} finally {
						RelProdutosPendentesWindow.cleanInstance();
					}
				}
			}
			case 40: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido() && ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados) && onFechamentoPedido) {
					PagamentoPedido pagamentoPedido = pedido.getPagamentoPedido();
					if (ValueUtil.isEmpty(pagamentoPedido.nuBanco)) {
						pagamentoPedido.houveErro = true;
		    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.DEBITOBANCARIO_LABEL_BANCO);
		    		}
		    		if (ValueUtil.isEmpty(pagamentoPedido.nuAgencia)) {
		    			pagamentoPedido.houveErro = true;
		    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.DEBITOBANCARIO_LABEL_AGENCIA);
		    		}
		    		if (ValueUtil.isEmpty(pagamentoPedido.nuConta)) {
		    			pagamentoPedido.houveErro = true;
		    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.DEBITOBANCARIO_LABEL_CONTA);
		    		}
				}
			}
			case 41: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() && LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco() && pedido.vlVerbaPedido < 0 && pedido.isPedidoVenda()) {
					String cdRepresentante;
					if (ValueUtil.isNotEmpty(pedido.cdSupervisor)) {
						cdRepresentante = pedido.cdSupervisor;
					} else {
						cdRepresentante = pedido.cdRepresentante;
					}
					if (VerbaSaldoService.getInstance().getVerbaSaldoErpVingenciaAtual(cdRepresentante) == null) {
						throw new ValidationException(Messages.VERBASALDO_ERRO_SALDO_INSUFICIENTE_FECHAMENTO);
					}
					double vlComparacaoVerbaAtual = 0;
					if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex()) {
						vlComparacaoVerbaAtual = pedido.vlVerbaPedido;
					}
					if (VerbaSaldoService.getInstance().getVlSaldo(pedido.getCdContaCorrente()) + vlComparacaoVerbaAtual < 0) {
						throw new ValidationException(Messages.VERBASALDO_ERRO_SALDO_INSUFICIENTE_FECHAMENTO);
					}
				}
			}
			case 42: {
				validationFechamentoCount++;
				if (!(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
					if (LavenderePdaConfig.usaQtMinProdTabPrecoEClasse()) {
						validateQtMinProdutoClasse(pedido);
						validatePedidoNumProdAbaixoMin(pedido);
					} else if (LavenderePdaConfig.usaQtMinProdTabPrecoEGrupo()) {
						validateQtMinProdutoGrupo(pedido);
						validatePedidoNumProdAbaixoMin(pedido);
					} else if (LavenderePdaConfig.usaQtMinProdTabPreco()) {
						validatePedidoNumProdAbaixoMin(pedido);
					}
				}
			}
			case 43: {
				validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
					TipoFrete tipoFrete = pedido.getTipoFrete();
					if (tipoFrete != null && tipoFrete.isTipoFreteFob() && !CpfCnpjValidator.isCnpjValid(pedido.nuCnpjTransportadora)) {
						throw new ValidationException(Messages.TIPOFRETE_ERRO_CNPJ_INVALIDO);
					}
				}
			}
			case 44: {
				validationFechamentoCount++;
				if (pedido.onFechamentoPedido) {
					validaFotoPedidoPresentePedido(pedido);
				}
			}
			case 45: {
				//Valida se o Status do Orçamento permite fechamento do pedido
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && pedido.onFechamentoPedido) {
					if (!StatusOrcamentoService.getInstance().permiteFechamentoPedido(pedido)) {
						throw new ValidationException(Messages.PEDIDO_MSG_FECHARPEDIDO_STAUS_ORCAMENTO);
					}
				}
			}
			case 46: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido() && SyncManager.isConexaoPdaDisponivel() && NfeService.getInstance().isPossuiNotaContingenciaNaoEnviadaServidor(pedido)) {
					throw new ValidationException(Messages.NFE_ERRO_POSSUI_NFECONTINGENCIA_NAO_ENVIADO_SERVIDOR);
				}
			}
			case 47: {
				PedidoService.validationFechamentoCount++;
				validaAplicacaoDescontoIndiceFinanceiroSaldoFlexNegativo(pedido);
			}
			case 48: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isBloqueiaCondPagtoPorClienteAddItemPedidoFimPedido()) {
					validateCondPagtoPorCliente(pedido.getCliente().cdCondicaoPagtoBloqueada, pedido.getCondicaoPagamento().cdCondicaoPagamento); 
				}	
			}
			case 49: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
					VenctoPagamentoPedidoService.getInstance().validateVctosFechamentoPedido(pedido);
				}
			}
			case 50: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.usaValorMinimoParaPedidoPorFuncao && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					validaVlMinPedido(pedido, 2);
				}
			}
			case 51: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.usaValorMinimoParaPedidoPorGrupoCliente && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					validaVlMinPedido(pedido, 3);
				}
			}
			case 52: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					validaVlMinPedido(pedido, 4);
				}
			}
			case 53: {
				PedidoService.validationFechamentoCount++;
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					int[] configValorMinimoUnicoParaPedido = LavenderePdaConfig.configValorMinimoUnicoParaPedido();
					double vlMinPedido = 0;
					vlMinFor:
					for (int i = 0; i < configValorMinimoUnicoParaPedido.length; i++) {
						vlMinPedido = validaVlMinPedido(pedido, configValorMinimoUnicoParaPedido[i]);
						if (vlMinPedido > 0) {
							break vlMinFor;
						}
					}
				}
			}
			case 54: {
				PedidoService.validationFechamentoCount++;
				validaQtMinimaProdutoPorCondPagamentoEQtMixProduto(pedido);
			}
			case 55: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() && LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
					MargemContribFaixaService.getInstance().validaMargemContribuicaoPedido(pedido);
				}
			}
			case 56: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaConfigPedidoProducao() && pedido.isPedidoVendaProducao()) {
					if (((!LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao() && pedido.pedidoRelacionado == null) || (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao() && ValueUtil.isEmpty(pedido.pedidoRelacionadoList))) && !SessionLavenderePda.liberadoPorSenhaRelacionarPedidoProducao) {				
						if (!PedidoService.getInstance().validateUsaPedidoProducao(pedido)) {
							throw new RelacionaPedProducaoException();
						}
					}
				}
			}
			case 57: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido) 
					validaCalculoFretePersonalizado(pedido);
			}	
			case 58: {
				PedidoService.validationFechamentoCount++;
				if(LavenderePdaConfig.usaValidaPosicaoVincoLargura()) {
					validaVincoCalculosPedido(pedido);
				}
			}
			case 59: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedido() && ValueUtil.valueEquals(pedido.getCliente().flObrigaOrdemCompra, ValueUtil.VALOR_SIM) && ValueUtil.isEmpty(pedido.nuOrdemCompraCliente)) {
					throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_CLIENTE_ORDEM_DE_COMPRA);
				}
			}
			case 60: {
				PedidoService.validationFechamentoCount++;
				validateConversaoUnFOB(pedido);
			}
			case 61: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaTabelaConfigMinPedido() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					validaVlMinPedido(pedido, 5);
				}
			}
			case 62: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaTabelaConfigMinPedido() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
					validaVlMinPedido(pedido, 6);
				}
			}
			case 63: {
				PedidoService.validationFechamentoCount++;
				validaConfigValorMinimoUnicoParaParcela(pedido);
			}
			case 64: {
				PedidoService.validationFechamentoCount++;
				if (pedido.getCondicaoPagamento().isPermiteEditarParcelas() && pedido.nuParcelas <= 0) {					
					throw new ValidationException(Messages.PEDIDO_ERRO_NUMERO_PARCELAS_ZERO);
				}
			}
			case 65: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isEnviarEmailPedidoAutoCliente() && LavenderePdaConfig.isPermiteEnviarEmailAlternativoComFlEnviaEmail() && ValueUtil.getBooleanValue(StringUtil.getStringValue(pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_FLENVIAEMAIL))) && ValueUtil.isEmpty((String) pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_DSEMAILSDESTINO))) {
					throw new ValidationException(Messages.PEDIDO_MSG_EMAILS_INVALIDO_VAZIO);
				}
			}
			case 66: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaPedidoBonificacaoPercentualLimiteBonificacao() > 0 && pedido.isPedidoBonificacao()) {
					validatePctLimiteBonificacao(pedido);
				}
			}
			case 67: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isObrigaVincularCampanhaPublicitariaPedido()) {
					validateObrigaVincularCampanhaPublicitaria(pedido);
				}
			}
			case 68: {
				PedidoService.validationFechamentoCount++;
				validaLimiteMargemRentab(pedido);
			}
			case 69: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido() && LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
					ValidacaoIemPedidoNuOrdemDuplicadaWindow validacaoIemPedidoNuOrdemDuplicadaWindow = new ValidacaoIemPedidoNuOrdemDuplicadaWindow(pedido);
					validacaoIemPedidoNuOrdemDuplicadaWindow.popup();
					if (validacaoIemPedidoNuOrdemDuplicadaWindow.possuiItensDuplicadosComOrdensDuplicadas) {
						throw new IemPedidoDuplicadoSemOrdemException();
					}
				}
			}
			//Estas devem ser as ultimas validações ao fechar o pedido, caso for feita alguma após, precisará voltar a verba consumida neste ponto
			case 70: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex() && pedido.isPedidoVenda() && pedido.vlVerbaPedido < 0 && onFechamentoPedido) {
					VerbaSaldoService.getInstance().consomeVerbaPedido(pedido);
					if (!pedido.inFecharPedidosEmLote && !LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
						UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.CONSUMO_VERBA_INFO_FECHAR_PEDIDO, new String[]{StringUtil.getStringValueToInterface(pedido.vlVerbaPedido)}));
					}
				}
			}
			case 71: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente && (pedido.deveValidarConsumoVerbaPedido || !LavenderePdaConfig.naoConsomeVerbaAutomaticamenteAoFecharPedido) && !LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() && pedido.isPedidoVenda() && pedido.getVlVerbaPedidoDisponivel() < 0 && onFechamentoPedido) {
					VerbaSaldoService.getInstance().consomeVerbaPedido(pedido);
					if (!pedido.inFecharPedidosEmLote && !LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
						UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.CONSUMO_VERBA_INFO_FECHAR_PEDIDO, new String[]{StringUtil.getStringValueToInterface(pedido.getVlVerbaPedidoDisponivel())}));
					}
				} 
			}
			case 72:{
				PedidoService.validationFechamentoCount++;
				validaQtMinimaProdutoPorCondPagamentoQtMinProduto(pedido);
			}
			case 73: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.isUsaPoliticaBonificacao() && onFechamentoPedido) {
					ItemPedidoBonifCfgService.getInstance().validateSaldoBonificacao(pedido);
				}
			}
			case 74: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido && LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
					VerbaService.getInstance().validateSaldo(pedido, pedido.vlVerbaPedido);
				}
			}
			case 75: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido && LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
					VerbaService.getInstance().validateSaldo(pedido, 0d);
				}
			}
			case 76: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido && pedido.getTipoPedido() != null && ValueUtil.VALOR_SIM.equals(pedido.getTipoPedido().flObrigaObservacao) && ValueUtil.isEmpty(StringUtil.getStringValue(pedido.getHashValuesDinamicos().get("DSOBSERVACAO")))) {
					throw new ValidationException(Messages.MSG_OBSERVACAO_OBRIGATORIO_TIPO_PEDIDO);
				}
			}
			case 77: {
				PedidoService.validationFechamentoCount++;
				if (onFechamentoPedido && LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao() && pedido.transportadoraReg == null) {
					throw new ValidationException(Messages.TRANSPORTADORAREG_ERRO_NENHUMA_TRANSP_SELECIONADA);
				}
			}
			case 78: {
				PedidoService.validationFechamentoCount++;
				if (LavenderePdaConfig.usaSugestaoVendaPorDivisao && ValueUtil.isEmpty(pedido.cdDivisaoVenda)) {
					throw new ValidationException(Messages.MSG_DIVISAO_VENDA_OBRIGATORIO);
				}
			}
			default:
				PedidoService.validationFechamentoCount = 0;
		}
	}

	private void validaLimiteMargemRentab(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemRentabilidade() || !LavenderePdaConfig.usaValidacaoPercentualMargemRentab() || ValueUtil.isEmpty(pedido.cdMargemRentab)) {
			return;
		}
		MargemRentab margemRentab = (MargemRentab) MargemRentabService.getInstance().findByRowKey(new MargemRentab(pedido).getRowKey());
		if (margemRentab == null) {
			return;
		}
		if (LavenderePdaConfig.validaPercentualMinimoMargemRentab()
				&& pedido.vlPctMargemRentab < margemRentab.vlPctMargemRentabMin) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PCT_MIN_MARGEMRENTAB_PEDIDO_NAO_ATINGIDO, StringUtil.getStringValueToInterface(margemRentab.vlPctMargemRentabMin)));
		}
		if (LavenderePdaConfig.validaPercentualMaximoMargemRentab()
				&& pedido.vlPctMargemRentab > margemRentab.vlPctMargemRentabMax) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PCT_MAX_MARGEMRENTAB_PEDIDO_ATINGIDO, StringUtil.getStringValueToInterface(margemRentab.vlPctMargemRentabMax)));
		}
	}

	private void validaQtParcelasCondicaoPagamento(Pedido pedido) throws SQLException {
		if (pedido.geraParcelasPorVencimento() && (pedido.parcelaPedidoList.size() != pedido.getCondicaoPagamento().nuParcelas)) {
			throw new ValidationException(Messages.PARCELAPEDIDO_DIFERENTE_CONDICAO_PAGTO);
		}
	}

	private void validaEnderecoEntregaPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0 && ValueUtil.isEmpty(pedido.cdEnderecoCliente)) {
			boolean obrigaInclusaoEnderecoEntrega = LavenderePdaConfig.isSempreObrigaInclusaoEnderecoEntrega();
			if (!obrigaInclusaoEnderecoEntrega && LavenderePdaConfig.isObrigaInclusaoEnderecoEntregaCasoExistaRegistroDeEnderecoEntrega()) {
				obrigaInclusaoEnderecoEntrega = ClienteEnderecoService.getInstance().isExisteEnderecosEntregaParaCliente(pedido);
			}
			if (obrigaInclusaoEnderecoEntrega) {
				throw new ValidationException(Messages.PEDIDO_MSG_VALIDACAO_ENDERECO_ENTREGA);
			}
		}
	}

	private void validaModoFaturamento(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais() || (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais() && pedido.getModoFaturamento() == null))
			return;
		int nuMinCaracteres = LavenderePdaConfig.getNuMinimoCaracteresObsModoFaturamento();
		if (pedido.getModoFaturamento().isExigeObservacao()) {
			if (ValueUtil.isEmpty(pedido.dsObsModoFaturamento)) {
				throw new ValidationException(Messages.MODOFATURAMENTO_OBSERVACAO_NAOPREENCHIDA);
			}
			if (pedido.dsObsModoFaturamento.length() < nuMinCaracteres) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MODOFATURAMENTO_OBSERVACAO_MINCARACTERES, nuMinCaracteres));
			}
		} else if (ValueUtil.isNotEmpty(pedido.dsObsModoFaturamento) && pedido.dsObsModoFaturamento.length() < nuMinCaracteres) {
			throw new ValidationException(MessageUtil.getMessage(Messages.MODOFATURAMENTO_OBSERVACAO_MINCARACTERES, nuMinCaracteres));
		}
	}

	private void validateQuantidadeMinimaCaixasPedido(final boolean isBonificacao, double qtItemFaturamentoPedido) throws SQLException {
		if (!isBonificacao && LavenderePdaConfig.quantidadeMinimaCaixasPedido != 0) {
			if (qtItemFaturamentoPedido < LavenderePdaConfig.quantidadeMinimaCaixasPedido && !ignoreQuantidadeMinimaCaixasPedido) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_MINIMA_CAIXAS, new String[]{StringUtil.getStringValueToInterface((double)LavenderePdaConfig.quantidadeMinimaCaixasPedido), StringUtil.getStringValueToInterface(qtItemFaturamentoPedido)}));
			}
			ignoreQuantidadeMinimaCaixasPedido = false;
		}
	}

	private void validaQtMinimaProdutoPorCondPagamentoEQtMixProduto(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto() || LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() || (LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
			return;
		}
		int qtMinMixProduto = pedido.getCondicaoPagamento().qtMinMixProduto;
		int qtProdutosPedido = pedido.itemPedidoList.size();
		if (qtMinMixProduto > qtProdutosPedido) {
			throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_QUANTIDADE_MINIMA_MIX_PRODUTOS_NAO_ATINGIDA, qtMinMixProduto));
		}
	}

	private void validaQtMinimaProdutoPorCondPagamentoQtMinProduto(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() || LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() || (LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
			return;
		}
		Vector itemPedidoList = pedido.itemPedidoList;
		int qtItensPedido = itemPedidoList.size();
		double qtProdutosPedido = 0d;
		for (int i = 0; i < qtItensPedido; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			qtProdutosPedido += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
		}
		String cdTabelaPreco = pedido.getTabelaPreco().cdTabelaPreco;
		if (qtProdutosPedido > 0) {
			CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
			int qtMinProduto = condicaoPagamento.getQtMinProduto(cdTabelaPreco);
			if (qtMinProduto > qtProdutosPedido) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_QUANTIDADE_MINIMA_PRODUTOS_NAO_ATINGIDA, qtMinProduto));
			}
		}
	}
		
	private double getQtMinValorParcela(Pedido pedido, int configValorMinimoUnicoParaPedido) throws SQLException {
		switch (configValorMinimoUnicoParaPedido) {
			case 1:
				TipoPedido tipoPedido = pedido.getTipoPedido();
				return tipoPedido != null && tipoPedido.qtMinValorParcela > 0 ? tipoPedido.qtMinValorParcela : 0;
			case 2:
				return getQtMinValorParcelaCondPagto(pedido);
			case 3:
				CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
				TipoPagamento tipoPagamento = pedido.getTipoPagamento();
				if (condicaoPagamento == null || tipoPagamento == null) return 0;
				return CondTipoPagtoService.getInstance().getQtMinValorParcelaCondPagtoTipoPagto(condicaoPagamento.cdCondicaoPagamento, tipoPagamento.cdTipoPagamento);
			case 4:
				return ConfigValorMinimoService.getInstance().findVlMinimoParcelaPedido(pedido);
			default:
				return 0;
		}
	}
		
	private double getQtMinValorParcelaCondPagto(Pedido pedido) throws SQLException {
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.isIgnoraVlMinimoParcelaCondPagto()) {
		return 0;
	}
		return condicaoPagamento != null && condicaoPagamento.qtMinValorParcela > 0 ? condicaoPagamento.qtMinValorParcela : 0;
	}
	
	private void validaValorMinimoParcela(Pedido pedido) throws SQLException {
		
		if (LavenderePdaConfig.isConfigValorMinimoUnicoParaParcela() || pedido.isValorMinParcelaLiberadoSenha()) return;
		
		for (int i = 1; i <= 4; i++) {
			validaValorMinimoParcela(pedido, i);
		}
	}

	public void validaValorMaximoParcela(Pedido pedido) throws SQLException {
		int nuParcelas = (pedido.nuParcelas == 0) ? 1 : pedido.nuParcelas;
		
		if(pedido.getCondicaoPagamento().qtMaxValorParcela > 0) {
			if (ValueUtil.round(pedido.vlTotalPedido / nuParcelas) <= ValueUtil.round(pedido.getCondicaoPagamento().qtMaxValorParcela))
				return;
			
			throw new ValorMaximoParcelaException(MessageUtil.getMessage(Messages.PEDIDO_MSG_MAXIMOVALORPEDIDO_TIPOPEDIDO,
			new String[]{ StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento().qtMaxValorParcela), StringUtil.getStringValueToInterface(pedido.vlTotalPedido / nuParcelas) }));
		}
	}

	private void validaValorMinimoParcela(Pedido pedido, int configValorMinimoUnicoParaPedido) throws SQLException {
		double qtMinValorParcela = getQtMinValorParcela(pedido,configValorMinimoUnicoParaPedido);
		validaValorMinimoParcela(pedido, qtMinValorParcela);
	}

	private void validaValorMinimoParcela(Pedido pedido, double qtMinValorParcela) throws SQLException {
		if (qtMinValorParcela == 0) return; 
			
		int nuParcelas = 0;
		
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			nuParcelas = (pedido.getCondicaoPagamento() == null) || pedido.getCondicaoPagamento().isParcelaUnicaVlMinParcela() || (pedido.nuParcelas == 0) ? 1 : pedido.nuParcelas;
		} else {
			nuParcelas = (pedido.getCondicaoPagamento() == null) || pedido.getCondicaoPagamento().isParcelaUnicaVlMinParcela() || (pedido.getCondicaoPagamento().nuParcelas == 0) ? 1 : pedido.getCondicaoPagamento().nuParcelas;
		}

		if (ValueUtil.round(pedido.vlTotalPedido / nuParcelas) >= ValueUtil.round(qtMinValorParcela)) return;

		if (LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()
				&& SolAutorizacaoService.getInstance().hasSolAutorizacaoAutorizadaPedido(pedido, TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX)) return;

				throw new ValorMinimoParcelaException(MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_TIPOPEDIDO,
			new String[]{ StringUtil.getStringValueToInterface(qtMinValorParcela), StringUtil.getStringValueToInterface(pedido.vlTotalPedido / nuParcelas) }));
			}
	
	private void validaConfigValorMinimoUnicoParaParcela (Pedido pedido) throws SQLException {
		if (pedido.isPedidoBonificacao() || !LavenderePdaConfig.isConfigValorMinimoUnicoParaParcela() || pedido.isValorMinParcelaLiberadoSenha())
			return;
		
		int[] configValorMinimoUnicoParaParcela = LavenderePdaConfig.configValorMinimoUnicoParaParcela();
		double vlMinParcela = 0;
		for (int i = 0; i < configValorMinimoUnicoParaParcela.length; i++) {
			vlMinParcela = getQtMinValorParcela(pedido, configValorMinimoUnicoParaParcela[i]);
			if (vlMinParcela > 0) break;
		}
		validaValorMinimoParcela(pedido, vlMinParcela);
	}

	private double validaVlMinPedido(Pedido pedido, int configValorMinimoUnicoParaPedido) throws SQLException {
		double vlMinPedido = 0;
		double valorTotalValidadoComImpostos = pedido.getValorTotalValidadoComImpostos();
		switch (configValorMinimoUnicoParaPedido) {
		case 1:
			if (pedido.getTipoPedido() != null && !pedido.getTipoPedido().isIgnoraVlMinPedido() || LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
				vlMinPedido = LavenderePdaConfig.valorMinimoParaPedido;
				if (ValueUtil.round(vlMinPedido) > ValueUtil.round(valorTotalValidadoComImpostos)) {
					Object[] params = {StringUtil.getStringValueToInterface(vlMinPedido), StringUtil.getStringValueToInterface(valorTotalValidadoComImpostos)};
					String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO, params);
					throw new ValidationException(message);
				}
			}
			break;
		case 2:
			FuncaoConfig funcaoConfigFilter = new FuncaoConfig();
			funcaoConfigFilter.cdFuncao = SessionLavenderePda.usuarioPdaRep.usuario.cdFuncao;
			vlMinPedido = ValueUtil.getDoubleValue(FuncaoConfigService.getInstance().findColumnByRowKey(funcaoConfigFilter.getRowKey(), "VLMINPEDIDO"));
			if (ValueUtil.round(vlMinPedido) > ValueUtil.round(valorTotalValidadoComImpostos)) {
				Object[] params = {funcaoConfigFilter.cdFuncao, StringUtil.getStringValueToInterface(vlMinPedido), StringUtil.getStringValueToInterface(valorTotalValidadoComImpostos)};
				String message = MessageUtil.getMessage(Messages.VALIDACAO_VLMIN_FUNCAO, params);
				throw new ValidationException(message);
			}
			break;
		case 3:
			String cdGrupoValorMinPedido = ClienteService.getInstance().findColumnByRowKey(pedido.getCliente().getRowKey(), "CDGRUPOVALORMINPEDIDO");
			if (cdGrupoValorMinPedido != null) {
				GrupoCliente grupoClienteFilter = new GrupoCliente();
				grupoClienteFilter.cdGrupoCliente = cdGrupoValorMinPedido; 
				vlMinPedido = ValueUtil.getDoubleValue(GrupoClienteService.getInstance().findColumnByRowKey(grupoClienteFilter.getRowKey(), "VLMINPEDIDO"));
				if (ValueUtil.round(vlMinPedido) > ValueUtil.round(valorTotalValidadoComImpostos)) {
					Object[] params = {StringUtil.getStringValueToInterface(vlMinPedido), StringUtil.getStringValueToInterface(valorTotalValidadoComImpostos)};
					String message = MessageUtil.getMessage(Messages.VALIDACAO_VLMIN_GRUPO_CLIENTE, params);
					throw new ValidationException(message);
				}
			}
			break;
		case 4:
			vlMinPedido = pedido.getCliente().isPessoaFisica() ? LavenderePdaConfig.valorMinimoPedidoPessoaFisica : LavenderePdaConfig.valorMinimoPedidoPessoaJuridica;
			if (ValueUtil.round(vlMinPedido) > ValueUtil.round(valorTotalValidadoComImpostos)) {
				Object[] params = {StringUtil.getStringValueToInterface(vlMinPedido), StringUtil.getStringValueToInterface(valorTotalValidadoComImpostos)};
				String message = MessageUtil.getMessage(pedido.getCliente().isPessoaFisica() ? Messages.VALIDACAO_VLMIN_PESSOA_FISICA : Messages.VALIDACAO_VLMIN_PESSOA_JURIDICA, params);
				throw new ValidationException(message);
			}
			break;
		case 5: 
			if (LavenderePdaConfig.usaTabelaConfigMinPedido() && !pedido.isIgnoraVlMinPedidoTipoPedido() && ConfigValorMinimoService.getInstance().isUsaFlAgrupaLinhaProdutos(pedido)) {
				ValidacaoValorMinimoLinhaWindow validacaoValorMinimoLinhaWindow = new ValidacaoValorMinimoLinhaWindow(pedido);
				validacaoValorMinimoLinhaWindow.popup();
				if (validacaoValorMinimoLinhaWindow.possuiLinhaAbaixoVlMinimo) {
					throw new ValorMinimoLinhaException();
		}
			}
			break;
		case 6:
			vlMinPedido = ConfigValorMinimoService.getInstance().findVlMinimoValorPedido(pedido);
			if (ValueUtil.round(vlMinPedido) > ValueUtil.round(valorTotalValidadoComImpostos)) {
				Object[] params = {StringUtil.getStringValueToInterface(vlMinPedido), StringUtil.getStringValueToInterface(valorTotalValidadoComImpostos)};
				String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO, params);
				throw new ValidationException(message);
			}
			break;
		}
		return vlMinPedido;
	}

	public boolean validateUsaPedidoComplementar(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaPedidoComplementar() && pedido.isPedidoComplementar() && ValueUtil.isEmpty(pedido.nuPedidoComplementado);
	}

	public boolean validateApenasItemPedidoOriginalNaBonificacao(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca
				&& (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || isPossuiPedidoRelacionadoNaoObrigatorio(pedido))
				&& pedido.isPedidoBonificacao();
	}
	
	public void validateObrigaRelacionarPedidoBonificao(final Pedido pedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.nuPedidoRelBonificacao) && ((LavenderePdaConfig.validaSaldoPedidoBonificacao || LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao()) && pedido.isPedidoBonificacao())) {
			throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_FECHAR_PEDIDO_BONIFICACAO);
		}
	}
	
	public void validateObrigaRelacionarPedidoTroca(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && pedido.isPedidoTroca() && ValueUtil.isEmpty(pedido.nuPedidoRelTroca)) {
			throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_FECHAR_PEDIDO_TROCA);
		}
	}
	
	private void validatePctQtdItemOriginalNaBonificacaoTroca(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			ItemPedidoService.getInstance().validaQtItemFisicoMaxDoItemPedidoBonificacaoTroca(pedido, itemPedido);
			}
		}
	
	public void validateObrigaRelacionarPedidoBonificaoTroca(Pedido pedido) throws SQLException {
		validateObrigaRelacionarPedidoBonificao(pedido);
		validateObrigaRelacionarPedidoTroca(pedido);
	}

	private double getVlTotalPedidosDoClienteHoje(Cliente cliente) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cliente.cdEmpresa;
		pedidoFilter.cdRepresentante = cliente.cdRepresentante;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		pedidoFilter.cdCliente = cliente.cdCliente;
		pedidoFilter.dtEmissao = DateUtil.getCurrentDate();
		Vector pedidoList = findAllByExample(pedidoFilter);
		int size = pedidoList.size();
		double vlTotalPedidos = 0;
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (!pedido.isPedidoAberto() && (pedido.getTipoPedido() != null || !pedido.getTipoPedido().isIgnoraVlMinPedido())) {
				vlTotalPedidos += pedido.vlTotalPedido;
			}
		}
		return vlTotalPedidos;
	}

	public void validaVlMinPedidosPorClienteAtingido(Pedido pedido, boolean validaApenasUsandoMultiplosPedidos) throws SQLException {
		if ((pedido.getTipoPedido() != null && !pedido.getTipoPedido().isIgnoraVlMinPedido() || LavenderePdaConfig.tipoPedidoOcultoNoPedido) && pedido.getCliente() != null  && !pedido.isPedidoBonificacao()) {
			double vlMinPedido = 0;
			boolean validaUsandoMultiplosPedidos = false;
			if (pedido.getCliente().vlMinPedido != 0) {
				vlMinPedido = pedido.getCliente().vlMinPedido;
				validaUsandoMultiplosPedidos = pedido.getCliente().isDivideVlMin();
			} else if (pedido.getCliente().getCategoria() != null && pedido.getCliente().getCategoria().vlMinPedido != 0) {
				vlMinPedido = pedido.getCliente().getCategoria().vlMinPedido;
				validaUsandoMultiplosPedidos = pedido.getCliente().getCategoria().isDivideVlMin();
			} else if (LavenderePdaConfig.isValorMinimoParaPedidoLigado()) {
				vlMinPedido = LavenderePdaConfig.valorMinimoParaPedido;
			}
			if (validaUsandoMultiplosPedidos) {
				if (ValueUtil.round(vlMinPedido) > ValueUtil.round(pedido.vlTotalPedido)) {
					double vlTotalPedidosCliente = getVlTotalPedidosDoClienteHoje(pedido.getCliente());
					if (pedido.isPedidoAberto()) {
						vlTotalPedidosCliente += pedido.vlTotalPedido;
					}
					if (ValueUtil.round(vlMinPedido) > ValueUtil.round(vlTotalPedidosCliente)) {
						String[] params = new String[] {StringUtil.getStringValue(vlMinPedido), StringUtil.getStringValue(vlTotalPedidosCliente), StringUtil.getStringValue(vlMinPedido - vlTotalPedidosCliente)};
						String message = MessageUtil.getMessage(Messages.VALOR_MINIMO_PEDIDO_NAO_ATINGIDO_ALERTA, params);
						throw new ValidationValorMinPedidoException(message, params);
					}
				}
			} else if (!validaApenasUsandoMultiplosPedidos && ValueUtil.round(vlMinPedido) > ValueUtil.round(pedido.vlTotalPedido)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.CLIENTE_VLMINPEDIDO_NAO_ATINGIDO, vlMinPedido));
			}
		}
	}
	
	private void validaVlMinPedidoNotaCredito(double vlTotalNotaCredito,  double vlTotalPedido, double vlIndiceFinanceiro) {
		if (vlTotalNotaCredito == 0) return;
		
		vlTotalNotaCredito = vlMinimoPedidoDeAcordoComNotaCredito(vlTotalNotaCredito, vlIndiceFinanceiro);
        if (vlTotalPedido >= vlTotalNotaCredito) return;
		
		Object[] params = {StringUtil.getStringValueToInterface(vlTotalNotaCredito), StringUtil.getStringValueToInterface(vlTotalPedido)};
		String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO, params); 
		throw new ValidationValorMinPedidoException(message, params);
	}
	
	public double vlMinimoPedidoDeAcordoComNotaCredito(double vlTotalNotaCredito, double vlIndiceFinanceiro) {
		if (vlTotalNotaCredito == 0) return 0;

		vlTotalNotaCredito = LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() ? (vlTotalNotaCredito * 100) / ((vlIndiceFinanceiro == 0 ? 1 : vlIndiceFinanceiro) * 100) : vlTotalNotaCredito;
        vlTotalNotaCredito += LavenderePdaConfig.getVlAdicionalNotaCredito();
        return vlTotalNotaCredito;
	}


	private void validateFecharPedidoGrupoProdutoSemItem(Pedido pedido) throws SQLException {
		Vector list = GrupoProduto1Service.getInstance().findGrupoProdutoPedido(pedido);
		if (list.size() == 0) return;
		
		throw new ValidationGrupoProdutoNaoInseridoPedidoException(Messages.MSG_GRUPOS_NAO_INSERIDOS);
	}

	public void validateFecharPedidoRestricaoQuantidade(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaApenasUmProdutoQuantidadeMaxVendaNoPedido && LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 && !ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha)) {
			int size = pedido.itemPedidoList.size();
			int nuItensNormaisDoPedido = 0;
			int nuItensRestricaoDoPedido = 0;
			int qtMinItensNormais = 0;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[i];
				if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedidoAux, pedido))) {
		    		ItemTabelaPreco itemTabPreco = itemPedidoAux.getItemTabelaPreco();
					if (itemTabPreco.qtMaxVenda == 0 && !itemTabPreco.isFlPromocao()) {
						nuItensNormaisDoPedido = nuItensNormaisDoPedido + 1;
					} else if (itemTabPreco.qtMaxVenda > 0) {
						nuItensRestricaoDoPedido = nuItensRestricaoDoPedido + 1;
						qtMinItensNormais += itemTabPreco.qtMinItensNormais != 0 ? itemTabPreco.qtMinItensNormais : LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade;
					}
				}
			}
			if ((nuItensRestricaoDoPedido > 0) && (qtMinItensNormais > nuItensNormaisDoPedido)) {
				String[] lista = { StringUtil.getStringValueToInterface(qtMinItensNormais),  StringUtil.getStringValueToInterface(nuItensNormaisDoPedido)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_BLOQUEIA_MSG_QT_MAX_ITEMRESTRICAO, lista));
			}
		}
	}
	
	private void validateFecharPedidoRestricaoPromocionalPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais() && !ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha)) {
			int size = pedido.itemPedidoList.size();
			int nuItensNormaisPedido = 0;
			int nuItensRestricaoPedido = 0;
			int nuItensPromocaoPedido = 0;
			boolean validaRegrasSeparadamente = LavenderePdaConfig.isValidaRegraProdutoRestritoEPromocionalSeparadamente();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[i];
				if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedidoAux, pedido))) {
					ItemTabelaPreco itemTabPreco = itemPedidoAux.getItemTabelaPreco();
					nuItensNormaisPedido++;
					if (itemTabPreco.qtMaxVenda > 0) {
						nuItensRestricaoPedido++;
					}
					if (ValueUtil.VALOR_SIM.equals(itemTabPreco.flPromocao) && itemTabPreco.qtMaxVenda == 0) {
						nuItensPromocaoPedido++;
					}
				}
			}
			if (validaRegrasSeparadamente && nuItensRestricaoPedido > 0 && nuItensPromocaoPedido > 0) {
				int qtValidacao = LavenderePdaConfig.getQtdMinimaProdutosPromocionais() + LavenderePdaConfig.getQtdMinimaProdutosRestritos();
				if (qtValidacao > nuItensNormaisPedido) {
					String[] lista = {StringUtil.getStringValueToInterface(qtValidacao), StringUtil.getStringValueToInterface(nuItensNormaisPedido)};
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ERRO_MIN_ITENS_PROMOCIONAIS_RESTRITOS, lista));
				}
			}
			if (nuItensRestricaoPedido > 0 && LavenderePdaConfig.getQtdMinimaProdutosRestritos() > 0) {
				validateQtdMinimaItemRestricaoQuantidade(pedido, nuItensNormaisPedido);
			}
			if (nuItensPromocaoPedido > 0) {
				if (LavenderePdaConfig.getQtdMinimaProdutosPromocionais() > nuItensNormaisPedido) {
					String[] lista = {StringUtil.getStringValueToInterface(LavenderePdaConfig.getQtdMinimaProdutosPromocionais()), StringUtil.getStringValueToInterface(nuItensNormaisPedido)};
					throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QT_MIN_ITENS_PROMOCIONAIS, lista));
				}
			}
		}
	}
	
	private void validateQtdMinimaItemRestricaoQuantidade(Pedido pedido, int nuItensNormaisPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
				if (itemTabelaPreco.qtMaxVenda > 0) {
					int qtMinItemNormal = itemTabelaPreco.qtMinItensNormais != 0 ? itemTabelaPreco.qtMinItensNormais : LavenderePdaConfig.getQtdMinimaProdutosRestritos();
					if (qtMinItemNormal > nuItensNormaisPedido) {
						String[] lista = { StringUtil.getStringValueToInterface(qtMinItemNormal),  StringUtil.getStringValueToInterface(nuItensNormaisPedido)};
						throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_BLOQUEIA_MSG_QT_MAX_ITEMRESTRICAO, lista));
					}
				}
			}
		}
	}
	
	public void validateValorMaximoPedido(final Pedido pedido) throws SQLException {
		if(pedido.getCondicaoPagamento().qtMaxValor > 0) {
			if (ValueUtil.round(pedido.getValorTotalValidadoComImpostos()) > ValueUtil.round(pedido.getCondicaoPagamento().qtMaxValor)) {
				Object[] params = {StringUtil.getStringValueToInterface(ValueUtil.round(pedido.getCondicaoPagamento().qtMaxValor)), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
				String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MAXIMOVALORPEDIDO, params);
				throw new ValidationValorMaxPedidoException(message, params);
			}
		}
		
	}
	
	public void validateValorMininoPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) return;
		
		// Parâmetro 386 - valorMinimoParaPedidoPorCondPagto
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca() && !(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente()) && !pedido.isIgnoraVlMinPedidoTipoPedido()) {
			CondicaoPagamento condPagamento = pedido.getCondicaoPagamento();
			if (condPagamento != null) {
				double qtMinValor = condPagamento.getQtMinValor(pedido.cdTabelaPreco);
				if (ValueUtil.round(pedido.getValorTotalValidadoComImpostos()) < ValueUtil.round(qtMinValor)) {
					Object[] params = {condPagamento.toString(), StringUtil.getStringValueToInterface(qtMinValor), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
					String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_CONDICAO, params);
					throw new ValidationValorMinPedidoException(message, params);
				}
			}
		}
		boolean validaVlMinPedido = !isIgnoraValidacaoVlMinPedido(pedido);
		// Parâmetro 17 - valorMinimoParaPedido
		if (LavenderePdaConfig.isValorMinimoParaPedidoLigado() && !pedido.isPedidoBonificacao() && validaVlMinPedido) {
			if (ValueUtil.round(pedido.getValorTotalValidadoComImpostos()) < ValueUtil.round(LavenderePdaConfig.valorMinimoParaPedido)) {
				Object[] params = {StringUtil.getStringValueToInterface(LavenderePdaConfig.valorMinimoParaPedido), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
				String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO, params);
				throw new ValidationValorMinPedidoException(message, params);
			}
		}
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido() && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca()) {
			validateValorMinimoParaPedidoPorTipoPedido(pedido);
		}
		//Valor minimo por cliente e categoria
		if (validaVlMinPedido) {
			validaVlMinPedidoNotaCredito(pedido.vlTotalNotaCredito, pedido.vlTotalBrutoItens,pedido.getCliente().vlIndiceFinanceiro);
			validaVlMinPedidosPorClienteAtingido(pedido, false);
		}
		
	}

	private void validateValorMinimoParaPedidoPorTipoPedido(final Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.qtMinValor != 0) {
			if (ValueUtil.round(pedido.getValorTotalValidadoComImpostos()) < ValueUtil.round(tipoPedido.qtMinValor)) {
				Object[] params = {StringUtil.getStringValue(tipoPedido.toString()), StringUtil.getStringValueToInterface(tipoPedido.qtMinValor), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
				String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_PORTIPOPEDIDO, params);
				throw new ValidationValorMinPedidoException(message, params);
			}
			ignoreQuantidadeMinimaCaixasPedido = LavenderePdaConfig.usaValorMinTipoPedidoPrioridade();
		}
	}

	public boolean isIgnoraValidacaoVlMinPedido(Pedido pedido) throws SQLException {
		if (pedido.isIgnoraVlMinPedidoTipoPedido() && LavenderePdaConfig.ignoraValidacaoValorMinPedidoConformeProdutoVendido) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				if (((ItemPedido)pedido.itemPedidoList.items[i]).getProduto().isIgnoraValorMinPedido()) {
					return true;
				}
			}
			return false;
		}
		return pedido.isIgnoraVlMinPedidoTipoPedido();
	}
	
	public void fecharPedido(final Pedido pedido) throws SQLException {
		Vector pedidoList = new Vector();
		pedido.isPedidoReaberto = false;
		pedidoList.addElement(pedido);
		fecharPedidos(pedidoList, true, false, false);
		
	}

	public String fecharPedidos(final Vector pedidoList, final boolean flPropagarExcecao, final boolean inFecharPedidosEmLote) throws SQLException {
		String retorno = fecharPedidos(pedidoList, flPropagarExcecao, true, inFecharPedidosEmLote);
		SessionLavenderePda.removePedidoProcessandoFechamento(pedidoList);
		return retorno;
	}

	public String fecharPedidos(final Vector pedidoList, final boolean flPropagarExcecao, final boolean validateExtras, final boolean inFecharPedidosEmLote) throws SQLException {
		return fecharPedidos(pedidoList, flPropagarExcecao, validateExtras, inFecharPedidosEmLote, false);
	}
	
	public void consignaPedido(final Pedido pedido) throws SQLException {
		PedidoConsignacaoService.getInstance().inserePedidoConsignado(pedido.itemPedidoList);
		fechaVisita(pedido);
	}
	
	public void consignaPedidoFecharPedido(final Pedido pedido) throws SQLException {
		PedidoConsignacaoService.getInstance().inserePedidoConsignadoFecharPedido(pedido.itemPedidoList);
		fechaVisita(pedido);
	}
	
	public boolean isPermiteClienteConsignar(final Pedido pedido) throws SQLException {
		if (pedido.itemPedidoList.size() == 0) {
			throw new ValidationException(Messages.PEDIDO_CONSIGNACAO_SEM_ITENS);
		}
		return isClientePossuiLimiteCreditoConsignado(pedido, false);
	}

	public boolean isClientePossuiLimiteCreditoConsignado(final Pedido pedido, boolean excluindoDevolucaoPedidoConsignado) throws SQLException {
		try {
			FichaFinanceiraService.getInstance().validaLimiteCreditoConsignado(pedido, excluindoDevolucaoPedidoConsignado);
		} catch (ValidationException ve) {
			throw new ValidationException(ve.getMessage()); 
		}
		return true;
	}
	
	public boolean isPossuiRegistroDePedidoConsignacao(Pedido pedido) throws SQLException {
		return PedidoConsignacaoService.getInstance().qtdPedidoConsignacaoPorPedido(pedido) == 0;
	}
	
	public boolean recalcularRentabilidadePedido(final Pedido pedido, final RecalculoRentabilidadeOptions option) throws SQLException {
		return MargemRentabService.getInstance().recalcularRentabilidadePedidoAbertoSeNecessario(pedido, option);
	}
	
	/**
	 * Fecha os pedidos passados por parâmetro, fazendo as validações necessárias
	 *
	 * @param pedidoList - lista de pedidos a serem fechados
	 * @param flPropagarExcecao 
	 * @param validateExtras 
	 * @param inFecharPedidosEmLote 
	 * @param enviaDadosServidor 
	 * @return String quantidade de pedido efetivamente fechados
	 * @throws SQLException 
	 */
	public String fecharPedidos(final Vector pedidoList, final boolean flPropagarExcecao, final boolean validateExtras, final boolean inFecharPedidosEmLote, final boolean enviaDadosServidor) throws SQLException {
		if (ValueUtil.isEmpty(pedidoList) || !validaUsuarioEmissaoPedido()) return "";
		StringBuffer strBuffer = new StringBuffer();
		int size = pedidoList.size();
		
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			pedido.onFechamentoPedido = true;
			Cliente clientePedido = pedido.getCliente();
			boolean pedidoAlterado = false;
			try {
				if (!LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
					processaSolicitacaoAutorizacao(pedido);
				}
				if (LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() && (pedido.getTipoPagamento() == null || ValueUtil.isEmpty(pedido.cdTipoPagamento))) {
					Cliente cliente = pedido.getCliente();
					TipoPagamento tipoPagamento = cliente.getTipoPagamento();
					if (tipoPagamento == null)
						throw new ValidationException(Messages.PEDIDO_MSG_ERRO_TIPO_PAGAMENTO_NENHUM_VALIDO);
					
					pedido.cdTipoPagamento = tipoPagamento.cdTipoPagamento;
				}
				TipoPagamentoService.getInstance().validaTipoPagamentoRestrito(pedido.getCliente());
				findItemPedidoList(pedido);
				SessionLavenderePda.setCliente(clientePedido);
				if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
					PedidoLogService.getInstance().loadPedidoLog(pedido);
				}
				pedidoAlterado = beforeFecharPedido(pedido);
				if (pedido.houveAtualizacaoDescProgressivoFechamento) {
					if (inFecharPedidosEmLote) {
						throw new ValidationException(Messages.MSG_AVISO_DESCPROGRESSIVO_ATUALIZADO);
					} else {
						UiUtil.showInfoMessage(Messages.MSG_AVISO_DESCPROGRESSIVO_ATUALIZADO);
					}
				}
				ClienteService.getInstance().setVlTotalPedClienteExcetoPedParam(pedido, SessionLavenderePda.getCliente());
				//--
				pedido.inFecharPedidosEmLote = inFecharPedidosEmLote;
				if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
					VerbaService.getInstance().consomeVerbaGrupoSaldoPersonalizada(pedido);
				}
				if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido() && ValueUtil.isNotEmpty(pedido.nuOrdemCompraCliente)) {
					atualizaNuOrdemCompraClienteItemPedidoByPedido(pedido);
				}
				validateFechamento(pedido, inFecharPedidosEmLote);
				if (validateExtras || pedidoAlterado) {
					validateFechamentoPedidoExtras(inFecharPedidosEmLote, pedido, pedidoAlterado);
				}
				if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && !pedido.getTipoPedido().isTipoPedidoBonificacaoContaCorrente() && ValueUtil.isNotEmpty(pedido.itemTotalmenteConvertidoBonifList)) {
					if (pedido.itemPedidoList.size() == pedido.itemTotalmenteConvertidoBonifList.size()) {
						pedido.totalmenteConvertidoPedidoBonificacao = true;
						pedido.itemTotalmenteConvertidoBonifList.removeAllElements();
						BonifCfgService.getInstance().criaPedidoBonificado((Pedido)pedido.clone());
						delete(pedido);
						continue;
					}
				}
				processaPedidoAfterValidateFechamento(pedido);
				
				if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto() && pedido.isPendenteCondPagto()) {
					pedido.cdStatusPedido = LavenderePdaConfig.getCdStatusPendenteCondPagto();
				} else if (LavenderePdaConfig.usaValidaConversaoFOB() && pedido.isPendenteFob()) {
					pedido.cdStatusPedido = LavenderePdaConfig.getCdStatusPedidoFOBPendenteAprovacao();
				} else {
					pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
				}
				pedido.dtFechamento = DateUtil.getCurrentDate();
				pedido.hrFechamento = TimeUtil.getCurrentTimeHHMM();
				pedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
				//--
				fecharItensPedido(pedido);
				//--
				SessionLavenderePda.addPedidoProcessandoFechamento(pedido);
				//--
				
				getCrudDao().update(pedido);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SA, LavenderePdaConfig.cdStatusPedidoFechado, StringUtil.getStringValue(pedido.vlTotalPedido));
				if (pedidoAlterado) {
					ItemPedidoService.getInstance().updateItensPedido(pedido.itemPedidoList);
				}
				fechaVisita(pedido);
				if (!pedido.isPedidoTroca() && !pedido.isPedidoBonificacao()) {
					if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
						ParcelaPedidoService.getInstance().reorganizaCodigoParcelas(pedido);
					}
					//--
					if (LavenderePdaConfig.aplicaSomenteMaiorDescontoPorItemFinalPedido) {
						aplicaMaiorDescontoFimPedidoNoPedido(pedido);
						updatePedidoAndItens(pedido);
					} else {
						// Colocado a negação no aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex para NÂO APLICAR DESCONTO 2 VEZES.
						if ((LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
							boolean updateItens = false;
							updateItens |= isAplicaIndiceFinanceiroClienteNoPedido(pedido);
							updateItens |= isAplicaDescontoCCPNoPedido(pedido);
							updateItens |= isAplicaDescontoProgressivoNoPedido(pedido);
							updateItens |= isAplicaDescontoProgressivoPorMixDeProdutos(pedido);
							if (updateItens) {
								updatePedidoAndItens(pedido);
							}
						}
					}
				}
				//--
				if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
					if (isAplicaDescontoProgressivoNoPedido(pedido)) {
						calcAndUpdateVlSaldoByPedidoWithDescProg(pedido);
						updatePedidoAndItens(pedido);
					}
				}
				ItemPedidoService.getInstance().marcaItemPedidoPorRentabilidade(pedido);
				PedidoService.getInstance().atualizaFlPendente(pedido);
				if (LavenderePdaConfig.destacaClienteAtendidoMes && !pedido.getCliente().isAtendido() && isTipoPedidoDestacaClienteAtendido(pedido)) {
					clientePedido.flAtendido = Cliente.CLIENTE_FLATENDIDO_PDA;
					ClienteService.getInstance().updateFlAtendido(clientePedido);
				}
				if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
					UsuarioDescService.getInstance().adicionaVlTotalPedidoUsuarioDescPda(pedido); 
				}
				if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
					PedidoLogService.getInstance().savePedidoLog(TipoRegistro.FECHAMENTO, pedido);
					PedidoLogService.getInstance().fechaPedidoLogParaEnvio(pedido);
				}
				if (inFecharPedidosEmLote && pedido.geraPdfOfflineFechamentoLote) {
					geraPdfPedidoOffline(pedido);
				}
				if (LavenderePdaConfig.usaControlePontuacao && !LavenderePdaConfig.ignoraExtratoApp) {
					PontExtPedService.getInstance().insertOrUpdateByPedido(pedido);
				}
				if (enviaDadosServidor) {
					if (LavenderePdaConfig.usaEnvioPedidoBackground) {
						pedidoEnvioServidorList.addElement(pedido);
					} else {
						if (inFecharPedidosEmLote) SessionLavenderePda.removePedidoProcessandoFechamento(pedido);
						PedidoUiUtil.enviaPedido(false, false);
					}
				}
				PedidoUiUtil.logFechamentoEquipamento(pedido);
				updateProdutoDesejado(pedido);
				
				if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && !pedido.getTipoPedido().isTipoPedidoBonificacaoContaCorrente() && ItemPedidoBonifCfgService.getInstance().isPedidoComItemContaCorrente(pedido)) {
					BonifCfgService.getInstance().criaPedidoBonificado((Pedido)pedido.clone());
				}
			} catch (RecalculoPedidoException re) {
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_FE, StringUtil.getStringValue(pedido.vlTotalPedido), re.getMessage());
				if (inFecharPedidosEmLote) {
					adicionaMensagemErroNoFechamentoPedido(strBuffer, pedido, re);
				} else {
					throw re;
				}
			} catch (ValorMinimoParcelaException ex) {
				if (inFecharPedidosEmLote) {
					adicionaMensagemErroNoFechamentoPedido(strBuffer, pedido, ex);
				} else {
				throw ex;
			} 
			} catch (RelacionaPedProducaoException e) {
				ExceptionUtil.handle(e);
			} catch (RelProdutosRentabilidadeSemAlcadaException ex) {
				throw ex;
			} catch (SaldoBonificacaoException e) {
				if (pedidoAlterado) {
					revertePedidoAndItens(pedido);
				}
				new ListBonifCfgWindow(pedido).popup();
				return ValueUtil.VALOR_NAO;
			} catch (ValorMinimoLinhaException e) {
				ExceptionUtil.handle(e);
			} catch (IemPedidoDuplicadoSemOrdemException e) {
				ExceptionUtil.handle(e);
			} catch (Throwable ex) {
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_FE, StringUtil.getStringValue(pedido.vlTotalPedido), ex.getMessage());
 				if (pedidoAlterado) {
					revertePedidoAndItens(pedido);
				}
 				removeFlagPedidoPendente(pedido);
				if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
					VerbaService.getInstance().deleteSaldoVerbaGrupoSaldoPersonalizada(pedido);
				}
				if (flPropagarExcecao) {
					if (ex instanceof ValidationException) {
						throw (ValidationException) ex;
					} else if (ex instanceof ApplicationWarnException) {
						throw (ApplicationWarnException) ex;
					}
					throw new ValidationException(ex.getMessage());
				}
				adicionaMensagemErroNoFechamentoPedido(strBuffer, pedido, ex);
			} finally {
				pedido.onFechamentoPedido = false;
			}
		}
		return strBuffer.toString();
	}

	public void validateFechamentoPedidoExtras(final boolean inFecharPedidosEmLote, Pedido pedido, boolean pedidoAlterado) throws SQLException {
		PedidoService.validationFechamentoListCount = 0;
		pedido.ignoraValidacaoSugestaoDifProdutos |= inFecharPedidosEmLote && LavenderePdaConfig.naoValidaSugestaoVendaDifPedidosAoFecharPedidoEmLote;
		pedido.ignoraValidacaoSugestaoProdutosComQtde |= inFecharPedidosEmLote && LavenderePdaConfig.naoValidaSugestaoVendaAoFecharPedidoEmLote;
		pedido.ignoraValidacaoSugestaoProdutosSemQtde |= inFecharPedidosEmLote && LavenderePdaConfig.naoValidaSugestaoVendaAoFecharPedidoEmLote;
		pedido.ignoraValidacaoMultiplosSugestaoProdutos |= inFecharPedidosEmLote;
		pedido.ignoraValidacaoAtrasoCliente |= inFecharPedidosEmLote && LavenderePdaConfig.naoAvisaClienteAtrasadoFecharPedidoEmLote;
		pedido.ignoraValidacaoMultiplosSugestaoProdutos = true;
		pedido.ignoraGiroProdutoPendente = true;
		validateFechamentoUI(pedido, inFecharPedidosEmLote);
		if (pedidoAlterado) {
			VerbaService.getInstance().validateSaldo(pedido, pedido.vlVerbaPedidoOld == 0d ? pedido.vlVerbaPedido : pedido.vlVerbaPedidoOld);
		}
	}

	private void processaPedidoAfterValidateFechamento(Pedido pedido) throws Exception, SQLException {
		if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && !pedido.getTipoPedido().isTipoPedidoBonificacaoContaCorrente() && ValueUtil.isNotEmpty(pedido.itemTotalmenteConvertidoBonifList)) {
			int size = pedido.itemTotalmenteConvertidoBonifList.size();
			ItemPedido itemPedido;
			for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido) pedido.itemTotalmenteConvertidoBonifList.elementAt(i);
				PedidoService.getInstance().deleteItemPedido(pedido, itemPedido, false, false);
			}
			PedidoService.getInstance().calculate(pedido);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			processaSolicitacaoAutorizacao(pedido);
		}
		controleReservarEstoque(pedido);
		setaObservacaoCliente(pedido);
		//--
		if ((LavenderePdaConfig.geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) && (pedido.vlVerbaPedido != 0 || pedido.vlVerbaPedidoPositivo != 0)) {
			pedido.flEtapaVerba = "1";
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido()	&& LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			double vlFreteTransp = (LavenderePdaConfig.tipoPedidoOcultoNoPedido || (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete())) ? pedido.transportadoraReg.vlFrete : 0d; 
			pedido.vlFrete = vlFreteTransp;
			pedido.cdTransportadora = pedido.transportadoraReg.cdTransportadora;
			pedido.cdTipoFrete = pedido.transportadoraReg.cdTipoFrete;
			pedido.cdRegiao = pedido.transportadoraReg.cdRegiao;
			pedido.vlTotalPedido = pedido.transportadoraReg.transportadora.isFlSomaFrete() ? pedido.vlTotalPedido + vlFreteTransp : pedido.vlTotalPedido;
			//
			if (pedido.vlFrete > 0) {
				ParcelaPedidoService.getInstance().insertParcelasPedido(pedido);
			}
		}
		//--
		if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto() && pedido.getTipoPedido() != null && pedido.getTipoPedido().isGeraNfe()) {
			pedido.flProcessandoNfeTxt = LavenderePdaConfig.cdStatusProcessandoNfeTxt;
		}
		if (LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			MargemContribFaixaService.getInstance().realizaControleVerba(pedido);
		}
		if (isRecalculoRentabilidadeNeededOnFechamentoPedido(pedido.inFecharPedidosEmLote)) {
			MargemRentabService.getInstance().recalcularRentabilidadePedido(pedido, false);
		}
	}

	public void controleReservarEstoque(Pedido pedido) throws SQLException {
		pedido.ignoraInativacaoReservaEstoque = false;
		if ((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente()
				|| LavenderePdaConfig.usaReservaEstoqueCorrenteR3())
				&& !pedido.ignoraGeracaoReservaEstoque
				&& isFlSituacaoReservaEst(pedido)
				&& !pedido.isIgnoraControleEstoque()) {
			pedido.flSituacaoReservaEstReabrePedido = pedido.flSituacaoReservaEst;
			try {
				geraReservaEstoque(pedido);
			} finally {
				if (LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido() || LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
					updateColumn(pedido.getRowKey(), "FLSITUACAORESERVAEST", pedido.flSituacaoReservaEst, Types.VARCHAR);
					pedido.flSituacaoReservaEstReabrePedido = pedido.flSituacaoReservaEst;
				}
			}
			if (!LavenderePdaConfig.isUsaReservaEstoqueCorrente() && !ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEstReabrePedido) && ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEst)) {
				EstoqueService.getInstance().updateEstoquePdaToERP(pedido);
				pedido.flSituacaoReservaEstReabrePedido = pedido.flSituacaoReservaEst;
			}
		}
	}

	private boolean isFlSituacaoReservaEst(Pedido pedido) {
		return !ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEst) || LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido() || LavenderePdaConfig.isUsaReservaEstoqueCorrente();
	}

	private boolean isRecalculoRentabilidadeNeededOnFechamentoPedido(boolean isFechamentoEmLote) {
		final RecalculoRentabilidadeOptions option = isFechamentoEmLote
													? RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_FECHAMENTO_LOTE
															: RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_FECHAMENTO_PEDIDO;
		return MargemRentabService.getInstance().isRecalculoRentabilidadeNeeded(option);
	}

	private void processaSolicitacaoAutorizacao(Pedido pedido) throws Exception {
		if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) return;
		findItemPedidoList(pedido);
		SolAutorizacaoService.getInstance().insertAndValidadePedidoCriticoBonificado(pedido);
		SolAutorizacaoService.getInstance().validateSolAutorizacaoFechamentoPedido(pedido);
		ItemPedidoService.getInstance().marcaFlAutorizadoItem(pedido);
	}

	private void atualizaFlPendente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaMotivoPendencia()) {
			Vector itemPedidoPendenteList = ItemPedidoService.getInstance().findItemPedidoPendenteList(pedido);
			pedido.flPendente = ValueUtil.isNotEmpty(itemPedidoPendenteList) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			updateColumn(pedido.getRowKey(), "FLPENDENTE", pedido.flPendente, Types.VARCHAR);
		} else if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			pedido.flPendente = isHasItemPedidoPendente(pedido) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			updateColumn(pedido.getRowKey(), "FLPENDENTE", pedido.flPendente, Types.VARCHAR);
		}
	}

	private void adicionaMensagemErroNoFechamentoPedido(StringBuffer strBuffer, Pedido pedido, Throwable ex) {
		strBuffer.append(pedido.rowKey);
		strBuffer.append("*");
		strBuffer.append(pedido.nuPedido);
		strBuffer.append("*");
		strBuffer.append(ex.toString());
		strBuffer.append("*");
		String err = ValueUtil.VALOR_NI.equals(ex.getMessage()) ? " " : StringUtil.delete(ex.getMessage(), '#');
		if (ex instanceof ValidationException && ((ValidationException) ex).params != null) {
			err += " " + ((ValidationException) ex).params;
		}
		strBuffer.append(err);
		strBuffer.append("#");
	}

	private void removeFlagPedidoPendente(Pedido pedido) throws SQLException {
		atualizaFlagItensPendentes(pedido);
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && (pedido.isPedidoItemPendente() || pedido.isDescontoLiberadoSenha())) {
			pedido.flItemPendente = "";
			pedido.flDescontoLiberadoSenha = "";
			updateColumn(pedido.getRowKey(), "FLITEMPENDENTE", ValueUtil.VALOR_NAO, Types.VARCHAR);
			updateColumn(pedido.getRowKey(), "FLDESCONTOLIBERADOSENHA", ValueUtil.VALOR_NAO, Types.VARCHAR);
		}
	}
	
	private void removeFlagValorMinParcelaLiberadoSenha(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela() || !pedido.isValorMinParcelaLiberadoSenha())
			return;
		pedido.flValorMinParcelaLiberadoSenha = ValueUtil.VALOR_NI;
		updateColumn(pedido.getRowKey(), "FLVALORMINPARCELALIBERADOSENHA ", ValueUtil.VALOR_NAO, Types.VARCHAR);
	}

	private void geraPdfPedidoOffline(Pedido pedido) {
		try {
			ResourcesWmw resourcesWmw = ResourcesWmwService.getInstance().getResourcesWmwRelatorioPdf();
			if (resourcesWmw != null) {
				PdfReportManager geradorPdf = new PdfReportManager();
				geradorPdf.interpretaLayoutPedido(pedido, resourcesWmw.baConteudo);
			}
		} catch (Throwable e) {
			pedido.msgProblemaGeracaoPdfOffline = e.getMessage();
			ExceptionUtil.handle(e);
		}
	}

	private boolean isTipoPedidoDestacaClienteAtendido(final Pedido pedido) throws SQLException {
		boolean isDestacaCliente = LavenderePdaConfig.tipoPedidoOcultoNoPedido;
		if (!isDestacaCliente && pedido.getTipoPedido() != null && pedido.getTipoPedido().isGeraAtendimento()) {
			isDestacaCliente = true;
		}
		return isDestacaCliente;
	}

	private void fechaVisita(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
			if (visitaEmAndamento != null && ValueUtil.valueEquals(visitaEmAndamento.cdCliente, pedido.cdCliente)) {
				if (LavenderePdaConfig.usaPositivacaoVisitaPorTipoPedido && pedido.getTipoPedido() != null) {
					visitaEmAndamento.flVisitaPositivada = StringUtil.getStringValue(pedido.getTipoPedido().isPermitePositivacaoVisita());
				} else {
					visitaEmAndamento.flVisitaPositivada = ValueUtil.VALOR_SIM;
				}
				VisitaService.getInstance().updateColumn(visitaEmAndamento.getRowKey(), "FLVISITAPOSITIVADA", Visita.FL_VISITA_POSITIVADA, Types.VARCHAR);
			}
			if (ValueUtil.VALOR_SIM.equals(pedido.getVisita().flPedidoSemVisita) || ValueUtil.VALOR_SIM.equals(pedido.getVisita().flPedidoOutroCliente)) {
				VisitaService.getInstance().fechaVisita(pedido.getVisita());
			}
		} else if (LavenderePdaConfig.isUsaAgendaDeVisitas()) {
			fechaVisitaParaEnvio(pedido);
		}
	}

	// Retorna true se o pedido foi alterado, pois serão necessários recalculos e revalidações.
	public boolean beforeFecharPedido(final Pedido pedido) throws SQLException {
		boolean pedidoAlterado = false;
		pedido.isPedidoReaberto = false;
		aplicaIndFinanceiroCota(pedido);
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(pedido.itemPedidoList) && ProdutoService.getInstance().isPossuiFamiliasDescProg(pedido, null)) {
			final boolean atualizouDesconto = atualizaDescProgressivoPedido(pedido).atualizouDesconto;
			pedido.houveAtualizacaoDescProgressivoFechamento = atualizouDesconto;
				}
		if (houveAlteracaoDesconto(pedido)) {
			pedidoAlterado = true;
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			if (LavenderePdaConfig.usaVolumeVendaMensalRede && pedido.getCliente() != null && pedido.getCliente().cdRede != null) {
				reajustaVlItensMudancaFaixaDescontoVolMensalRede(pedido);
			} else {
				reajustaVlItensMudancaFaixaDescontoVolMensal(pedido);
			}
			pedidoAlterado = true;
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			boolean pedidoAlteradoDescCascata = DescFaixaValorPedidoService.getInstance().aplicaDescontoItens(pedido);
			if (LavenderePdaConfig.isArredondaDescontosEmCascataNoFinalRegra2()) {
				ItemPedidoService.getInstance().roundDescontoEmCascataRegra2(pedido.itemPedidoList);
				pedidoAlteradoDescCascata = true;
			}
			if (pedidoAlteradoDescCascata) {
				pedidoAlterado = true;
				PedidoService.getInstance().calculate(pedido);
			}
		}
		if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && ItemPedidoBonifCfgService.getInstance().isPedidoComItemContaCorrente(pedido) && !pedido.isPedidoBonificacao()) {
			pedidoAlterado |= aplicaValoresBonificacaoContaCorrente(pedido);
		}
		return pedidoAlterado;
	}
	
	private boolean aplicaValoresBonificacaoContaCorrente(Pedido pedido) throws SQLException {
		return BonifCfgService.getInstance().aplicaBonifContaCorrente(pedido);
	}
	
	public boolean houveAlteracaoDesconto(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && !LavenderePdaConfig.isIncluiDescQtdeGrupoProdValidacaoLimiteCreditoFechamentoPedido()) {
			aplicaDescQtdPorGrupoProdTodosItens(pedido);
			return true;
		} else if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
			}
			return true;
		}
		return false;
	}
	
	public void aplicaDescQtdPorGrupoProdTodosItens(final Pedido pedido) throws SQLException {
		aplicaDescQtdPorGrupoProdTodosItens(pedido, false, false);
	}

	public void aplicaDescQtdPorGrupoProdTodosItens(final Pedido pedido, boolean doUpdate, boolean forcaDesconto) throws SQLException {
		DescontoGrupoService.getInstance().aplicaDescQtdGrupoAuto(pedido, forcaDesconto);
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
			if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
				VerbaService.getInstance().aplicaVerbaPorGrupoProdComTolerancia(pedido, itemPedido);
			}
			ItemPedidoService.getInstance().calculaIpiItemPedido(itemPedido);
			ItemPedidoService.getInstance().calculaStItemPedido(itemPedido, pedido.getCliente());
		}
		PedidoService.getInstance().calculate(pedido);
		if (doUpdate) {
			updatePedidoAndItens(pedido);
		}
	}
	
	public void reajustaVlItensMudancaFaixaDescontoVolMensal(Pedido pedido) throws SQLException {
		double vlTotalCalculoVolumeMensal = pedido.getCliente().vlVendaMensal + pedido.vlTotalPedido;
		pedido.vlPctDescCliente = DescontoVendaService.getInstance().getVlPctDescontoVenda(pedido.cdEmpresa, pedido.getCliente().cdEstadoComercial, vlTotalCalculoVolumeMensal);
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			calculateItemPedido(pedido, itemPedido, true);
		}
		
		calculate(pedido);
	}
	
	public void reajustaVlItensMudancaFaixaDescontoVolMensalRede(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		if (cliente == null || cliente.cdRede == null) return;
		
		double vlVendasMensalRede = ClienteService.getInstance().sumVlVendaMensalClientesRede(cliente);
		double vlTotalCalculoVolumeMensal = vlVendasMensalRede + pedido.vlTotalItens;
		vlTotalCalculoVolumeMensal += PedidoPdbxDao.getInstance().sumVlTotalPedidosConsideraVolumeVendaMensalPorStatus(pedido);
		pedido.vlPctDescCliente = DescontoVendaService.getInstance().getVlPctDescontoVenda(pedido.cdEmpresa, cliente.cdEstadoComercial, vlTotalCalculoVolumeMensal);
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			calculateItemPedido(pedido, itemPedido, true);
		}
		calculate(pedido);
	}
	
	public void revertePedidoAndItens(Pedido pedido) throws SQLException {
		pedido.itemPedidoList = new Vector(0);
		findItemPedidoList(pedido);
		if (pedido.flDescQtdGrupoAplicadoAuto || pedido.aplicouBonifContaCorrente) {
			PedidoService.getInstance().calculate(pedido);
		}
		pedido.flDescQtdGrupoAplicadoAuto = false;
		pedido.aplicouBonifContaCorrente = false;
	}

	public boolean isPedidoSemNota(Pedido pedido) {
		return LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()
				&& (LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido() || LavenderePdaConfig.isUsaGeracaoNotaNfeContingenciaPedidoSemConexao())
				&& (pedido.flProcessandoNfeTxt.equals(LavenderePdaConfig.cdStatusProcessandoNfeTxt)
					|| pedido.flProcessandoNfeTxt.equals(LavenderePdaConfig.cdStatusResetNfeTxt));
				}

	private void calcAndUpdateVlSaldoByPedidoWithDescProg(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			int size = pedido.itemPedidoList.size();
			pedido.vlVerbaPedido = 0;
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					pedido.vlVerbaPedido += ((ItemPedido) pedido.itemPedidoList.items[i]).vlVerbaItem;
				}
			}
			VerbaSaldoService.getInstance().updateVlSaldoByPedidoByVlVerbaPedido(pedido);
		}
	}
	
	public void validateReabrirPedidoUI(Pedido pedido) throws SQLException {
		switch (PedidoService.validationFechamentoListCount) {
			case 0: {
				PedidoService.validationFechamentoListCount++;
				if ((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3())
						&& pedido.isPedidoComReservaEstoque()
						&& !pedido.isIgnoraControleEstoque()) {
					try {
						String retorno = SyncManager.validaInativacaoReservaEstoqueItensPedido(pedido);
						if (ValueUtil.isEmpty(retorno) || !retorno.equals("OK")) {
							throw new ReservaEstoqueException(retorno);
						}
					} catch (Throwable ex) {
						throw new ReservaEstoqueException(ex.getMessage());
					}
				} else {
					pedido.ignoraInativacaoReservaEstoque = true;
				}
			}
			case 1:
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.validaPeriodoEntregaParaPedido && !ClienteEnderecoService.getInstance().isTodosEnderecosComPeriodoEntrega(pedido.getCliente())) {
					throw new ValidationException(Messages.PEDIDO_MSG_ERRO_SEM_PERIODO_ENTREGA);
				}
			default:
				PedidoService.validationFechamentoListCount = 0;
		}
	}


	public void validateFechamentoUI(Pedido pedido, boolean onFecharPedidosEmLote) throws SQLException {
		switch (PedidoService.validationFechamentoListCount) {
			case 0: {
				PedidoService.validationFechamentoListCount++;
				validateItemPedidoProdutoBloqueado(pedido);
			}
			case 1: {
				PedidoService.validationFechamentoListCount++;
				validatePedidoQtItemFisicoVazioGondola(pedido);
			}
			case 2: {
				PedidoService.validationFechamentoListCount++;
				validatePedidoComProdutoRestrito(pedido);
			}
			case 3: {
				PedidoService.validationFechamentoListCount++;
				validatePedidoProdutoExclusivo(pedido);
			}
			case 4: {
				PedidoService.validationFechamentoListCount++;
				//Cliente Atrasado
				if (pedido.getTipoPedido() == null || !pedido.getTipoPedido().isIgnoraClienteAtrasado()) {
					ClienteService.getInstance().validateClienteInadimplente(pedido.getCliente(), onFecharPedidosEmLote, ValueUtil.VALOR_SIM.equals(pedido.flClienteAtrasadoLiberadoSenha), pedido.ignoraValidacaoAtrasoCliente, true);
				}
			}
			case 5: {
				PedidoService.validationFechamentoListCount++;
				validatePlataformaVendaItens(pedido);
			}
			case 6: {
				PedidoService.validationFechamentoListCount++;
				validateObrigaRelacionarPedidoBonificao(pedido);
			}
			case 7: {
				PedidoService.validationFechamentoListCount++;
				//Desconto progressivo
				if (LavenderePdaConfig.permiteEditarDescontoProgressivo) {
					double vlPctDescProgressivo = pedido.getVlPctDescProgressivo();
					if (vlPctDescProgressivo > 0.0) {
						throw new DescontoProgressivoPedidoException(Messages.VALIDACAO_FECHAR_PEDIDO_DESCONTO);
					}
				}
			}
			case 8: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isAvisaUsuarioPositivacaoFornecedor() && !ValueUtil.isEmpty(pedido.itemPedidoList)) {
					Vector fornecedorList = PedidoService.getInstance().getFornecedoresNaoPositivadosList(pedido);
					if (ValueUtil.isNotEmpty(fornecedorList)) {
						throw new PositivacaoItensByFornecedorException(Messages.VALIDACAO_FECHAR_PEDIDO_FORNECEDOR);
					}
				}
			}
			case 9: {
				PedidoService.validationFechamentoListCount++;
				//Rota Entrega
				if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
					if (RotaEntregaService.getInstance().findAllRotasEntregaCliente2Combo(pedido.cdCliente).size() > 0) {
						if (ValueUtil.isEmpty(pedido.cdRotaEntrega)) {
							throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.PEDIDO_LABEL_DSROTAENTREGA);
						}
					}
				}
			}
			case 10: {
				PedidoService.validationFechamentoListCount++;
				//cdTransportadora
				if (LavenderePdaConfig.usaTransportadoraPedido() && ValueUtil.isEmpty(pedido.cdTransportadora)) {
					throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.TRANSPORTADORA);
				}
			}
			case 11: {
				PedidoService.validationFechamentoListCount++;
				//Sugestao de venda com cadastro sem quantidade minima
				if (LavenderePdaConfig.isUsaSugestaoVendaComCadastroFecharPedido() && ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido)) {
					if ((!pedido.isPedidoTroca()) && (!pedido.isPedidoBonificacao()) && !pedido.isPedidoAbertoNaoEditavel()) {
						if (!pedido.ignoraValidacaoSugestaoProdutosSemQtde && SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, false, true)) {
							throw new SugestaoVendaComCadastroSemQtdPedidoException(Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA);
						}
						if (SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true, true)) {
							throw new SugestaoVendaComCadastroSemQtdPedidoException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA, pedido.getCliente().toString()));
						}
					}
				}
			}
			case 12: {
				PedidoService.validationFechamentoListCount++;
				//Sugestao de venda com cadastro com quantidade minima
				if (LavenderePdaConfig.isUsaSugestaoVendaComCadastroFecharPedido() && ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido)) {
					if ((!pedido.isPedidoTroca()) && (!pedido.isPedidoBonificacao())) {
						if (!pedido.ignoraValidacaoSugestaoProdutosComQtde && SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, false, true)) {
							throw new SugestaoVendaComCadastroComQtdPedidoException(Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA);
						}
						if (SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true, true)) {
							throw new SugestaoVendaComCadastroComQtdPedidoException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA, pedido.getCliente().toString()));
						}
					}
				}
			}
			case 13: {
				PedidoService.validationFechamentoListCount++;
				//Sugestao de venda baseado nos ultimos pedidos
				if (LavenderePdaConfig.usaSugestaoVendaBaseadaDifPedidos > 0) {
					if (!pedido.ignoraValidacaoSugestaoDifProdutos && !pedido.isPedidoBonificacao()) {
						if ((!pedido.isPedidoTroca()) && (!pedido.isPedidoBonificacao())) {
							if (ProdutoService.getInstance().hasSugestaoVendaDifPedido(pedido)) {
								throw new SugestaoVendaDifPedidoException(Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA);
							}
						}
					}
				}
			}
			case 14: {
				PedidoService.validationFechamentoListCount++;
				//Relatório de produtos pendentes
				if (LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedidoAoFecharPedido() 
						&& !pedido.getCliente().isClienteDefaultParaNovoPedido() && !pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
					if (!pedido.ignoraValidacaoProdutosPendentes) {
						if ((onFecharPedidosEmLote && LavenderePdaConfig.isValidaProdNaoInseridosHistAoFecharPedEmLote())
							|| (!onFecharPedidosEmLote && LavenderePdaConfig.isValidaProdNaoInseridosHistAoFecharPedido())) {
							validaProdutosPendentes(pedido);
						}
					}
				}
			}
			case 15: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.mostraRelatorioGrupoProdutoNaoInseridosPedido) {
					validateFecharPedidoGrupoProdutoSemItem(pedido);
				}
			}
			case 16: {
				PedidoService.validationFechamentoListCount++;
				validateValorMininoPedido(pedido);
					validateValorMaximoPedido(pedido);
			}
			case 17: {
				PedidoService.validationFechamentoListCount++;
				if ((LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() || LavenderePdaConfig.controlarLimiteCreditoCliente || (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && !LavenderePdaConfig.controlarLimiteCreditoCliente))) {
					try {
						if ((!pedido.isFlCreditoClienteLiberadoSenha() || isSolicitavaNovamenteSenhaUsuarioAlcada(pedido.cdUsuarioLiberacaoLimCred)) && !pedido.ignoraValidacaoLimiteCreditoCliente) {
							pedido.fechandoPedidoConsignado = pedido.isPedidoConsignado();
							pedido.onFechamentoPedido = true;
							FichaFinanceiraService.getInstance().validateLimCred(pedido, null);
						}
					} catch (ValidationException ve) {
						if (LavenderePdaConfig.bloquearLimiteCreditoCliente) {
							StringBuffer strBuffer = new StringBuffer();
							strBuffer = strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage());
							if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
								strBuffer.append(" " + Messages.CLIENTE_FINANCEIRO_BLOQUEADO_PEDIDO_A_VISTA);
							}
							throw new ValidationException(strBuffer.toString());
						} else if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() || LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
							throw new LimiteCreditoClienteExtrapoladoPedidoException(ve.getMessage());
						} else if (LavenderePdaConfig.isObrigaAnexoDocClienteLimiteCredExtrapolado()) {
							throw ve;
						}
					} finally {
						pedido.onFechamentoPedido = false;
					}
				}
			}
			case 18: {
				PedidoService.validationFechamentoListCount++;
				validateInfoComplementares(pedido);
			}
			case 19: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque) {
					ItemPedido itemPedido;
					int size = pedido.itemPedidoList.size();
					for (int i = 0; i < size; i++) {
						itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
						itemPedido.setOldQtItemFisico(itemPedido.getQtItemFisico());
						itemPedido.oldQtEstoqueConsumido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisicoAtualizaEstoque());
						if (!ProdutoService.getInstance().getProduto(itemPedido.cdProduto).isPermiteEstoqueNegativo() && (pedido.getTipoPedido() != null && pedido.getTipoPedido().isRevalidaEstoqueFechamento())) {
								if (LavenderePdaConfig.atualizarEstoqueInterno) {
									EstoqueService.getInstance().recalculaEstoqueConsumido(itemPedido.cdProduto);
								}
								EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
							}
						}
					}
				}
			case 20: {
				PedidoService.validationFechamentoListCount++;
				//SugestaoVenda sem quantidade obrigatória outras empresas
				if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas != 0 && !pedido.ignoraAvisoSugestaoVendaSemQtdOutrasEmpresas && !pedido.isPedidoBonificacao()) {
					if (SugestaoVendaService.getInstance().isValidaSugestaoVendaObrigatoriaMultiplasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true)) {
						if (SugestaoVendaService.getInstance().isHasSugestoesObrigatoriasPendentesOutrasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true)) {
							throw new SugestaoVendaPresenteEmOutrasEmpresasPedidoException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA_OUTRAS_EMPRESAS, pedido.cdCliente), SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE);
						}
					}
				}
			}
			case 21: {
				PedidoService.validationFechamentoListCount++;
				//SugestaoVenda com quantidade obrigatória outras empresas
				if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas != 0 && !pedido.ignoraAvisoSugestaoVendaComQtdOutrasEmpresas && !pedido.isPedidoBonificacao()) {
					if (SugestaoVendaService.getInstance().isValidaSugestaoVendaObrigatoriaMultiplasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true)) {
						if (SugestaoVendaService.getInstance().isHasSugestoesObrigatoriasPendentesOutrasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true)) {
							throw new SugestaoVendaPresenteEmOutrasEmpresasPedidoException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA_OUTRAS_EMPRESAS, pedido.cdCliente), SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE);
						}
					}
				}
			}
			case 22: {
				PedidoService.validationFechamentoListCount++;
				ProdutoRelacionadoService.getInstance().loadProdutosRelacionadosNaoContemplados(pedido);
				if (ValueUtil.isNotEmpty(pedido.prodRelacionadosNaoContempladosList)) {
					throw new ProdutosRelacionadosNaoAtendidosException(Messages.PRODUTO_RELACIONADO_VALIDACAO_FECHAMENTO_PEDIDO);
				}
			}
			case 23: {
				PedidoService.validationFechamentoListCount++;
				if ((LavenderePdaConfig.isPermitePedidoNovoCliente() && pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) || pedido.getCliente().isClienteDefaultParaNovoPedido()) {
					throw new PedidoSemClienteException(Messages.NOVO_PEDIDO_SEM_CLIENTE);
				}
			}
			case 24: {
				PedidoService.validationFechamentoListCount++;
				if (!pedido.ignoraValidacaoSugestaoItensRentabilidadeIdeal && !pedido.isPedidoBonificacao() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0) {
					if (pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaIdeal(pedido.getRentabilidadeFaixaList())) {
						throw new SugestaoItensRentabilidadeIdealException(Messages.PEDIDO_MSG_PCT_RENTABILIDADE_ABAIXO_IDEAL);
					}
				}
			}
			case 25: {
				PedidoService.validationFechamentoListCount++;
				if ((LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento() || LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda()) 
						&& !pedido.ignoraValidacaoMultiplosSugestaoProdutos && !pedido.isPedidoAbertoNaoEditavel()) {
					if ((!pedido.isPedidoTroca()) && (!pedido.isPedidoBonificacao())) {
						throw new ListMultiplasSugestoesProdutosException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA, pedido.getCliente().toString()));
					}
				}
			}
			case 26: {
				PedidoService.validationFechamentoListCount++;
				if ((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCentralizadoAtomico()) && !pedido.isIgnoraControleEstoque()) {
					try {
						pedido.itemPedidoProblemaReservaEstoqueList = null;
						String retorno = SyncManager.validaReservaEstoqueItensPedido(pedido, pedido.itemPedidoList);
						if (ValueUtil.isEmpty(retorno) || !retorno.equals("OK")) {
							pedido.itemPedidoProblemaReservaEstoqueList = ItemPedidoService.getInstance().getItemPedidoProblemaReservaEstoqueList(retorno);
							throw new ReservaEstoqueException(Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE);
						} 
					} catch (Throwable ex) {
						throw new ReservaEstoqueException(ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList) ? Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE : Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE_GERAL); 
					}
				}
			}
			case 27: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaDescontoPonderadoPedido) {
					double vlPctMaxDesconto = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(false, false);
					Pedido pedidoAux = null;
					if (pedido.isPedidoBonificacao()) {
						pedidoAux = findPedidoRelacionadoBonificacao(pedido);
						pedido.pedidoRelacionado = pedidoAux;
					} else {
						pedidoAux = pedido;
					}
					if (LavenderePdaConfig.isGeraCreditoIndiceBonificacao() && pedido.isPedidoBonificacao() && (pedido.getTipoPedido().isFlTipoCreditoFrete() || pedido.getTipoPedido().isFlTipoCreditoCondicao())) {
						throw new DescontoPonderadoPedidoException(ValidationException.EXCEPTION_ABORT_PROCESS);
					}
					if (pedidoAux.vlPctDesconto > 0) {
						if (ValueUtil.round(pedidoAux.vlPctDesconto) > ValueUtil.round(vlPctMaxDesconto)) {
							throw new DescontoPonderadoPedidoException(MessageUtil.getMessage(Messages.PEDIDO_MSG_PCT_MAX_DESCONTO_USUARIO_ULTRAPASSADO, new Object[] {StringUtil.getStringValueToInterface(pedidoAux.vlPctDesconto)}));
						}
						if (LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
							double vlPctMaxDescPonderado = UsuarioDescService.getInstance().getVlPctMaxDescontoPonderadoUsuario();
							double vlPctMedioDescPonderado = UsuarioDescService.getInstance().getVlPctMedioDescontoPonderadoComPedidoAtual(pedidoAux);
							if (vlPctMedioDescPonderado > vlPctMaxDescPonderado) {
								throw new DescontoPonderadoPedidoException(MessageUtil.getMessage(Messages.PEDIDO_MSG_PCT_MAX_DESCONTO_USUARIO_ULTRAPASSADO, new Object[] {StringUtil.getStringValueToInterface(pedidoAux.vlPctDesconto)}));
							}
						}
						if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
							PedidoDescService.getInstance().savePedidoDesc(pedidoAux, pedidoAux.vlPctDesconto, pedidoAux.vlDesconto);
						}
					}
				}
			}
			case 28: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoFecharPedido()) {
					if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
						if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor()) {
							int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
							if (result < 1) {
								String msg = "";
								if (result == -1) {
									msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
								} else {
									msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
								}
								throw new PesquisaMercadoException(msg);
							}
						}
						if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola()) {
							int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISAGONDOLA);
							if (result < 1) {
								String msg = "";
								if (result == -1) {
									msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
								} else {
									msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
								}
								throw new PesquisaMercadoException(msg);
							}
						}
					} else {
						int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
						if (result < 1) {
							String msg = "";
							if (result == -1) {
								msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
							} else {
								msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
							}
							throw new PesquisaMercadoException(msg);
						}
					}
				}
			}
			case 29: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaPctManualAcrescimoCustoNoPedido) {
					atualizaControlesValidacaoComponentesAcrescimoCusto(pedido);
					if (pedido.isMotivoAcrescimoCustoVazio) {
						throw new ValidationException(Messages.PEDIDO_MSG_PREENCHIMENTO_MOTIVO);
					}
					if (pedido.isVlAcrescimoCustoVazio) {
						throw new ValidationException(Messages.PEDIDO_MSG_PREENCHIMENTO_ACRESCIMO);
					}
				}
			}
			case 30: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaPctMaxParticipacaoItemBonificacao && pedido.isPedidoBonificacao()) {
					calculaItemComParticipacaoBonificacaoExtrapolada(pedido);
					if (pedido.itemPedidoParticipacaoExtrapoladaList.size() > 0) {
						throw new ItemParticipacaoExtrapoladoException(Messages.PEDIDO_MSG_ERRO_PARTICIPACAO_EXTRAPOLADA);
					}
				}
			}
			case 31: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoItemPedido()) {
					for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
						ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[j];
						double pesoMinTabelaPreco =itemPedido.getTabelaPreco() != null ? itemPedido.getTabelaPreco().qtPesoMin : 0d;
						if (pedido.qtPeso < pesoMinTabelaPreco) {
							throw new ItensPedidoAbaixoPesoMinimoTabelaPrecoException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}
					}
				} else if(LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoItem()) {
					validaQtMinPesoTabPreco(pedido);
				}
			}
			case 32: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido() && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) {
					validateDataEntrega(pedido, false);
				}
			}
			case 33: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && !ValueUtil.VALOR_SIM.equals(pedido.flMinVerbaLiberado)) {
					double vlMinVerba = VerbaSaldoService.getInstance().getVlMinVerba(pedido, pedido.cdEmpresa, pedido.cdRepresentante);
					if (vlMinVerba == 0) {
						return;
					}
					double vlSaldoAtual = ValueUtil.round(VerbaSaldoService.getInstance().getVlSaldo(pedido.getCdContaCorrente(), pedido.dtEmissao));
					if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex()) {
						vlSaldoAtual += pedido.vlVerbaPedido;
					}
					double excedente = vlMinVerba - vlSaldoAtual;
					if (excedente > 0) {
						throw new ValidationValorMinimoVerbaUltrapassadoException(Messages.ITEMPEDIDO_LIMITE_VERBA_MINIMO_ULTRAPASSADO);
					}
				}
			}
			case 34: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedido.qtdCreditoDescontoGerado > pedido.qtdCreditoDescontoConsumido && !onFecharPedidosEmLote) {
					throw new CreditoDisponivelPedidoException();
				}
				if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedido.qtdCreditoDescontoGerado < pedido.qtdCreditoDescontoConsumido) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PRODUTOCREDITODESCONTO_QTD_CRED_NEGATIVO, StringUtil.getStringValueToInterface(pedido.qtdCreditoDescontoConsumido - pedido.qtdCreditoDescontoGerado)));
				}
			}
			case 35: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && LavenderePdaConfig.vlPctAvisaCreditoDesconto > 0 && !onFecharPedidosEmLote) {
					ProdutoCreditoDescService.getInstance().loadItemPedidoAvisaCreditoDescontoList(pedido);
					if (ValueUtil.isNotEmpty(pedido.itemPedidoCredDescList)) {
						throw new ProdutoCreditoDescontoException();
					}
				}
			}
			case 36: {
				PedidoService.validationFechamentoListCount++;
				//Relatório de giro produto pendente
				if (!pedido.isPedidoTroca() && !pedido.isPedidoBonificacao() && LavenderePdaConfig.showGiroProdutoFechamentoPedido() && !onFecharPedidosEmLote) {
					if (!pedido.ignoraGiroProdutoPendente && !pedido.isPedidoAbertoNaoEditavel()) {
						throw new GiroProdutoException();
					}
				}
			}
			case 37: {
				PedidoService.validationFechamentoListCount++;
				//Entrega
				if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro() && (ValueUtil.isEmpty(pedido.cdEntrega) || !EntregaService.getInstance().isEntregaVigente(pedido)) && 
						(!LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega || (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega && ValueUtil.isEmpty(pedido.dtEntrega)))) {
					throw new ValidationException(Messages.LABEL_ENTREGA_NAOPREENCHIDA);
				}
			}
			case 38: {
				PedidoService.validationFechamentoListCount++;
				//Rentabilidade;
				if (LavenderePdaConfig.isBloqueiaFechamentoPedidoRentabilidadeMinima() && !pedidoPossuiSomenteProdutosNeutros(pedido)) {
					RentabilidadeFaixa rentabilidadeFaixa = RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaPedido(pedido);
					if (pedido.flAbaixoRentabilidadeMinima || rentabilidadeFaixa != null && rentabilidadeFaixa.isFaixaMinima()) {
						throw new RentabilidadeMenorMinimaException();
					}
				}
				if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !LavenderePdaConfig.isUsaControleRentabilidadePorFaixa() && pedido.vlRentabilidade < 0) {
					if (LavenderePdaConfig.permiteRentabilidadeNegativaPedido) {
						if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_VALIDACAO_RENTABILIDADE_NEGATIVA)) {
							throw new RentabilidadeNegativaException();
						}
					} else {
						throw new ValidationException(Messages.PEDIDO_MSG_RENTABILIDADE_NEGATIVA);
					}
				}
			} 
			case 39: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.validaSaldoPedidoBonificacao && pedido.getTipoPedido() != null && pedido.getTipoPedido().isBonificacao()) {
					BonificacaoSaldoService.getInstance().consomeSaldoBonificacaoPedido(pedido, true);
				}
			} 
			case 40: {
				PedidoService.validationFechamentoListCount++;
				validaClienteAtrasadoVlTitulosEmAberto(pedido);
			} 
			case 41: {
				PedidoService.validationFechamentoListCount++;
				validaAplicacaoDescontoIndiceFinanceiroSaldoFlexNegativo(pedido);
			} 
			case 42: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && !pedido.isDescontoLiberadoSenha()) {
					validaPctMaximoDescontoItens(pedido);
				}
			}
			case 43: {
				PedidoService.validationFechamentoListCount++;
				validaEstoquePrevistoInsuficiente(pedido);
				}
			case 44: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente && LavenderePdaConfig.naoConsomeVerbaAutomaticamenteAoFecharPedido && pedido.isPedidoVenda() && pedido.getVlVerbaPedidoDisponivel() < 0 && !pedido.deveValidarConsumoVerbaPedido) {
					throw new VerbaSaldoPedidoConsumidoException(MessageUtil.getMessage(Messages.VERBASALDO_WARN_PEDIDO_CONSUMO_VERBA_MAIOR_SALDO_POSITIVO, pedido.getVlVerbaPedidoDisponivel()));
				}
			}
			case 45: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado && pedido.isPedidoBonificacao() && !pedido.consumoVerbaSaldoLiberadoSenha) {	
					VerbaSaldoService.getInstance().consomeVerbaPedido(pedido);
				}
			}
			case 46: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isConfigGradeProduto()) {
					int inconsistencia = ItemPedidoGradeService.getInstance().verificaInconsistenciasGrade(pedido);
					if (ValueUtil.valueEquals(inconsistencia, ItemGrade.ITEMGRADELIST_PROBLEMA_EM_ALGUMAS_GRADES)) {
						throw new RelProdutosGradesInconsistentesException(Messages.MENSAGEM_ERRO_ALGUMAS_GRADES_NAO_ENCONTRADAS);
					}
					if (ValueUtil.valueEquals(inconsistencia, ItemGrade.ITEMGRADELIST_PROBLEMA_EM_TODAS_AS_GRADES)) {
						throw new RelProdutosGradesInconsistentesException(Messages.MENSAGEM_ERRO_NENHUMA_GRADE_ENCONTRADA);
					}
				}
			}
			case 47: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() && ValueUtil.valueEquals(ValueUtil.VALOR_SIM, pedido.flItemPendente)) {
					atualizaPedidoPendenteItemPedidoPendente(pedido);
				}
			}
			case 48: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.isControlaDescontoUsandoVerbaAtravesMixItens()) {
					int qtItensPedidoMin = LavenderePdaConfig.controlaDescontoUsandoVerbaAtravesMixItens;
					int qtItensPedido = pedido.itemPedidoList.size();
					if (pedido.vlVerbaPedido != 0 && qtItensPedido < qtItensPedidoMin) {
						throw new ValidationException(MessageUtil.getMessage(Messages.MENSAGEM_LIMITE_DESCONTO_USANDO_VERBA_ATRAVES_MIX_ITENS_NAO_ATINGIDO, new Object[] {StringUtil.getStringValue(qtItensPedidoMin), StringUtil.getStringValue(qtItensPedido)}));
					}
				}
			}
			case 49: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaRegistroMotivoNaoVendaProdutoSugerido) {
					if (NaoVendaProdPedidoService.getInstance().isPossuiProdutoNaoVendidos(pedido)) {
						throw new NaoVendaProdPedidoException();
					}
				}
			}
			case 50: {
				PedidoService.validationFechamentoListCount++;
				//TODO Já existe no fechamento UI, verificar necessidade
				validaValorMinimoParcela(pedido);
			}
			case 51: {
				PedidoService.validationFechamentoListCount++;
				try {
					if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && !ValueUtil.getBooleanValue(SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista)) {
					ClienteService.getInstance().validateClienteBloqueado(pedido.getCliente());
					} else if (!LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
						ClienteService.getInstance().validateClienteBloqueado(pedido.getCliente());
					}
				} catch (Throwable e) {
					throw new ClienteBloqueadoException(e.getMessage());
				}
			}
			case 52: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
					validaFechamentoDescontoMaximoItensCondicaoPagamento(pedido);
				}
			}
			case 53: {
				PedidoService.validationFechamentoListCount++;
				validateBloqueiaCondPagtoPorDiasCliente(pedido);
			}
			case 54: {
				PedidoService.validationFechamentoListCount++;
				validatePedidoComItemBonificado(pedido);
			}
			case 55: {
				PedidoService.validationFechamentoListCount++;
				validaConfigValorMinimoUnicoParaParcela(pedido);
			}
			case 56: {
				PedidoService.validationFechamentoListCount++;
				if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido() && pedido.isUsaCodigoInternoCliente()) {
					throw new ProdutoClienteRelacionadoException(Messages.CAD_PROD_CLI_COD_VALIDAR_CODIGOS_PRODUTOS);
				}
			}
			case 57: {
				PedidoService.validationFechamentoListCount++;
				validaVigenciaKitTipo3(pedido);
			}
			case 58: {
				PedidoService.validationFechamentoListCount++;
				validaVigenciaDescProgressivoPersonalizado(pedido);
			}
			case 59: {
				PedidoService.validationFechamentoListCount++;
				validaJustificativaMotivoPendencia(pedido);
			} 
			case 60: {
				PedidoService.validationFechamentoListCount++;
				validaCondicaoPagamentoPadraoCliente(pedido, pedido.cdCondicaoPagamento);
			}
			case 61: {
				PedidoService.validationFechamentoListCount++;
				int itensComErros = 0;
				if (!LavenderePdaConfig.apenasAvisaDescontoAcrescimoMaximo && LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento()) {
					pedido.itemPedidoInseridosAdvertenciaList = new Vector();
					for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
						try {
							ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
							SolAutorizacao solAutorizacao = SolAutorizacaoService.getInstance().getSolAutorizacao(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
							if (solAutorizacao == null || solAutorizacao.isVisualizado()) {
								ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido);
								ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
							}
						} catch (ValidationException e) {
							itensComErros++;
							ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
							itemPedido.dsMotivoAdvertencia = e.getMessage();
							pedido.itemPedidoInseridosAdvertenciaList.addElement(itemPedido);
						}
					}
				}
				if (itensComErros > 0) {
					throw new ValidationItemPedidoException(MessageUtil.getMessage(Messages.MSG_ERRO_ITENS_IMPEDITIVOS_FECHAMENTO_PEDIDO,
							new String[]{StringUtil.getStringValue(itensComErros), StringUtil.getStringValue(pedido.itemPedidoList.size())}));
				}
			}
			case 62: {
				PedidoService.validationFechamentoListCount++;
				DocumentoAnexoService.getInstance().validaAnexoPedidoCondicaoPagamento(pedido);
			}
			case 63: {
				PedidoService.validationFechamentoListCount++;
				validadeVlFreteAdicionalPedido(pedido);
			}
			case 64: {
				validateBonificacaoContaCorrente(pedido);
			}
			case 65: {
				if (LavenderePdaConfig.isObrigaFotoItemTroca() && pedido.isPedidoTroca()) {
					int size = pedido.itemPedidoTrocaList.size();
					for (int i = 0; i < size; i++) {
						ItemPedido itemTroca = (ItemPedido) pedido.itemPedidoTrocaList.elementAt(i);
						FotoItemTroca fotoItemTroca = FotoItemTrocaService.getInstance().getFotoItemTrocaInstance(itemTroca);
						int count = FotoItemTrocaService.getInstance().countByExample(fotoItemTroca);
						if (count == 0) {
							throw new ValidationException(MessageUtil.getMessage(Messages.ERROR_PRODUTO_SEM_FOTO_TROCA, itemTroca.getProduto().toString()));
						}
					}
				}
			}
			default:
				PedidoService.validationFechamentoListCount = 0;
		}
	}
	
	public void validaQtMinPesoTabPreco(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.getTabelaPreco().qtPesoAcumuladoAtual = 0d;
			double pesoItens = 0d;
			for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
			ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[j];
				if(itemPedido.getTabelaPreco().cdTabelaPreco.equals(itemPedidoAux.getTabelaPreco().cdTabelaPreco)) {
					pesoItens += ItemPedidoService.getInstance().getPesoItemPedido(itemPedidoAux, itemPedidoAux.qtItemFisico);
				}
			}
			itemPedido.getTabelaPreco().qtPesoAcumuladoAtual += pesoItens;
		}
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if(itemPedido.getTabelaPreco().qtPesoAcumuladoAtual < itemPedido.getTabelaPreco().qtPesoMin) {
				throw new ItensPedidoAbaixoPesoMinimoTabelaPrecoException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}		
	}
	
	public void validaQtMinValorTabPreco(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.getTabelaPreco().qtValorMinAcumulado = 0d;
			double valorItens = 0d;
			for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
				ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[j];
				if (itemPedido.getTabelaPreco().cdTabelaPreco.equals(itemPedidoAux.getTabelaPreco().cdTabelaPreco)) {
					valorItens += itemPedidoAux.getVlTotalItem();
				}
			}
			itemPedido.getTabelaPreco().qtValorMinAcumulado += valorItens;
		}
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getTabelaPreco().qtValorMinAcumulado < itemPedido.getTabelaPreco().qtMinValor) {
				throw new ItensPedidoAbaixoValorMinimoTabelaPrecoException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
	}

	private void validaJustificativaMotivoPendencia(Pedido pedido) {
		if (LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao() && isPedidoPossuiMotivoPendencia(pedido)) {
			throw new JustificativaMotivoPendenciaException(Messages.VALIDACAO_FECHAR_PEDIDO_JUSTIFICATIVA_MOTIVO_PENDENCIA);
		}
	}

	private boolean isPedidoPossuiMotivoPendencia(Pedido pedido) {
		Vector itemPedidoList = pedido.itemPedidoList;
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (itemPedido.isPendente()) {
					return true;
				}
			}
		}
		return false;
	}

	private void validaVigenciaDescProgressivoPersonalizado(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ItemPedidoService.getInstance().isPossuiItensDescProgressivoPersonalizadoExtrapolados(pedido)) {
			throw new DescProgressivoPersonalizadoVigenciaException();
		}
	}

	private void validaEstoquePrevistoInsuficiente(Pedido pedido) throws SQLException {
		Vector erroEstoqueInsuficienteList = EstoquePrevistoService.getInstance().getErroEstoqueInsuficienteList(pedido);
		if (ValueUtil.isNotEmpty(erroEstoqueInsuficienteList)) {
			pedido.erroItensFechamentoPedido = erroEstoqueInsuficienteList;
			throw new ItemFechamentoPedidoException(ValueUtil.VALOR_NI);
		}
	}

	private void validatePlataformaVendaItens(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			Vector erroPlataformaVendaItensList = PlataformaVendaProdutoService.getInstance().findProdutosPlataformaDivergente(pedido);
			if (ValueUtil.isNotEmpty(erroPlataformaVendaItensList)) {
				pedido.erroItensFechamentoPedido = erroPlataformaVendaItensList;
				throw new ItemFechamentoPedidoException(ValueUtil.VALOR_NI);
			}
		}
	}

	private void validaVigenciaKitTipo3(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			Vector kitsExtrapolados = KitService.getInstance().findKitsVigenciaExtrapoladaByPedido(pedido);
			if (ValueUtil.isNotEmpty(kitsExtrapolados)) {
				throw new KitTipo3VigenciaException(kitsExtrapolados);
			}
		}
	}

		private void validateInfoComplementares(Pedido pedido) throws SQLException {
		if (pedido.getTipoPedido() != null && pedido.getTipoPedido().isObrigaTodasInfoComplementar())  {
			if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isEmpty(pedido.cdCentroCusto)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_ADICIONAL_CAMPOS_OBRIGATORIOS, Messages.CENTROCUSTO_LABEL_DESCRICAO));
			} else if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais() && ValueUtil.isEmpty(pedido.cdPlataformaVenda)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_ADICIONAL_CAMPOS_OBRIGATORIOS, Messages.PLATAFORMAVENDA_LABEL_DESCRICAO));
			} else if (LavenderePdaConfig.isUsaItemContaInformacoesAdicionais() && ValueUtil.isEmpty(pedido.cdItemConta)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_ADICIONAL_CAMPOS_OBRIGATORIOS, Messages.ITEMCONTA_LABEL_DESCRICAO));
			} else if (LavenderePdaConfig.isUsaClasseValorInformacoesAdicionais() && ValueUtil.isEmpty(pedido.cdClasseValor)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_ADICIONAL_CAMPOS_OBRIGATORIOS, Messages.CLASSEVALOR_LABEL_DESCRICAO));
			} else if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais() && ValueUtil.isEmpty(pedido.cdModoFaturamento)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_ADICIONAL_CAMPOS_OBRIGATORIOS, Messages.MODOFATURAMENTO_LABEL_DESCRICAO));
			}
			validaModoFaturamento(pedido);
		}
	}
	
	private void validaCalculoFretePersonalizado(Pedido pedido) {
		if (!LavenderePdaConfig.usaCalculoFretePersonalizado()) return;
		if (ValueUtil.isEmpty(pedido.cdTransportadora)) 
			throw new FreteCalculoPersonalizadoException(MessageUtil.getMessage(Messages.FRETE_PERSONALIZADO_EXCEPTION, Messages.TRANSPORTADORA));
		
		if (ValueUtil.isEmpty(pedido.cdTipoFrete)) 
			throw new FreteCalculoPersonalizadoException(MessageUtil.getMessage(Messages.FRETE_PERSONALIZADO_EXCEPTION, Messages.TIPOFRETE_LABEL_TIPOFRETE));
	}

	private void validaFechamentoDescontoMaximoItensCondicaoPagamento(Pedido pedido) throws SQLException {
		CondicaoPagamento cond = pedido.getCondicaoPagamento();
		if (cond == null || cond.vlPctMaxDesconto <= 0) return;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
			if (item.vlPctDescCondPagto <= cond.vlPctMaxDesconto) continue; 
			resetaDescontosCondicaoPagamento(pedido);
			throw new ValidationException(Messages.ERRO_FECHAMENTO_PEDIDO_DESCONTO_MAXIMO_CONDICAO_PAGAMENTO);
		}
	}
	
	public void processaTrocaDeCondicaoComercialComDesconto(Pedido pedido) throws SQLException {
		if (!existeItemPedidoComDescontoCondPagto(pedido)) return;
		if (UiUtil.showConfirmYesCancelMessage(Messages.INFO_TROCA_CONDICAO_PAGAMENTO_COM_DESCONTO) == 0) {
			throw new ValidationException("");
		}
		resetaDescontosCondicaoPagamento(pedido);
	}

	private void resetaDescontosCondicaoPagamento(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = null;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			itemPedido.vlPctDescCondPagto = 0; 
			getItemPedidoService().calculate(itemPedido, pedido);
		}
		updateItensPedidoAfterChanges(pedido);
		calculate(pedido);
		updatePedidoAfterCrudItemPedido(pedido);
	}

	private boolean existeItemPedidoComDescontoCondPagto(Pedido pedido) {
		ItemPedido itemPedido = null;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.vlPctDescCondPagto > 0) return true; 
		}
		return false;
	}

	public void validateBloqueiaCondPagtoPorDiasCliente(Pedido pedido) throws SQLException {
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		Cliente cliente = pedido.getCliente();
		if (condicaoPagamento != null && cliente != null && LavenderePdaConfig.isBloqueiaCondPagtoPorDiasCliente()) {
			boolean validaCondPagto = false;
			if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto()) {
				validaCondPagto = !pedido.isPendenteCondPagto();
			} else if (LavenderePdaConfig.isLiberaComSenhaCondPagamento()) {
				int qtDiasCond = LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMediosCliente() ? condicaoPagamento.qtDiasMediosPagamento : condicaoPagamento.qtDiasMaximoPagamento;
				validaCondPagto = !(pedido.qtDiasCPgtoLibSenha >= qtDiasCond && pedido.vlTotalPedidoLiberado <= pedido.vlTotalPedido);
			}
			if (validaCondPagto) {
				CondicaoPagamentoService.getInstance().validateCondPagtoPorDiasCliente(condicaoPagamento, cliente.qtDiasMaximoPagamento);
			}
		}
	}
	
	public boolean isSolicitavaNovamenteSenhaUsuarioAlcada(String cdUsuario) throws SQLException {
		if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada() && LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
			if (ValueUtil.isEmpty(cdUsuario)) return true;
			
			Vector usuarioRelReps = UsuarioRelRepService.getInstance().findUsuarioRelRep(true);
			int size = usuarioRelReps.size();
			if (size == 0) return true;
			for (int i = 0; i < size; i++) {
				UsuarioRelRep usuarioRelRep = (UsuarioRelRep) usuarioRelReps.items[i];
				if (ValueUtil.valueEquals(cdUsuario, usuarioRelRep.cdUsuarioRep)) return false;
			}
		}
		return false;
	}

	private void atualizaPedidoPendenteItemPedidoPendente(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		Vector itemPedidoPendenteList = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.vlPctDesconto > itemPedido.getItemTabelaPreco().vlPctDescAprovado) {
				itemPedidoPendenteList.addElement(itemPedido);
			}
		}		
		if (itemPedidoPendenteList.size() > 0) {
			pedido.flPendente = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLPENDENTE", pedido.flPendente, Types.VARCHAR);
		}
	}
	
	private void validaPctMaximoDescontoItens(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		Vector itemPedidoPendenteList = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.vlPctDesconto > itemPedido.getItemTabelaPreco().vlPctDescAprovado) {
				itemPedidoPendenteList.addElement(itemPedido);
			}
			if (itemPedido.vlPctDesconto > itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto())) {
				throw new DescontoMaximoItemException(Messages.VALIDACAO_PCT_DESC_MAXIMO);
			}
		}
		atualizaItensPendentes(pedido, itemPedidoPendenteList);
	}

	public void validaVincoCalculosPedido(Pedido pedido) throws SQLException {
		if (getListItemCalculoVinco(pedido).size() > 0) { 
			StringBuilder mensagem = new StringBuilder();
			mensagem.append("PVL").append(Messages.INFO_COMPLEMENTAR_VINCO_MUDANCA_NA_LARGURA);
			throw new ValidationException(mensagem.toString());
		}
	}

	public Vector getListItemCalculoVinco(Pedido pedido) {
		int size = pedido.itemPedidoList.size();
		Vector itemPedidoPendenteList = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if(ItemPedidoService.getInstance().validaPosicaoVincoLargura(itemPedido)) {
				itemPedidoPendenteList.addElement(itemPedido);
			}
		}
		return itemPedidoPendenteList;
	}

	private void atualizaItensPendentes(Pedido pedido, Vector itemPedidoPendenteList) throws SQLException {
		int sizeItensPendentes = itemPedidoPendenteList.size();
		for (int i = 0; i < sizeItensPendentes; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPendenteList.items[i];
			itemPedido.flPendente = ValueUtil.VALOR_SIM;
			ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "FLPENDENTE", ValueUtil.VALOR_SIM, Types.VARCHAR);
		}
		if (sizeItensPendentes > 0) {
			pedido.flItemPendente = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLITEMPENDENTE", pedido.flItemPendente, Types.VARCHAR);
		}
	}

	public void atualizaPedidoPendenteAposLiberaracaoSenha(Pedido pedido) throws SQLException {
		pedido.flDescontoLiberadoSenha = ValueUtil.VALOR_SIM;
		updateColumn(pedido.getRowKey(), "FLDESCONTOLIBERADOSENHA", ValueUtil.VALOR_SIM, Types.VARCHAR);
	}
	
	private void validaClienteAtrasadoVlTitulosEmAberto(Pedido pedido) throws SQLException {
		if (ClienteService.getInstance().isValidaClienteAtrasadoPorValorTotalTitulosEmAtraso(pedido, pedido.getCliente())) {
			double vlTotalTitulosAtraso = TituloFinanceiroService.getInstance().getVlTotalTitulosAtraso();
			if (vlTotalTitulosAtraso > pedido.getCliente().vlMaxTitulosAtraso) {
				String msgErro = MessageUtil.getMessage(Messages.CLIENTE_ATRASADO_VLTOTAL_TITULOS, new Object[] {StringUtil.getStringValueToInterface(vlTotalTitulosAtraso), StringUtil.getStringValueToInterface(pedido.getCliente().vlMaxTitulosAtraso)});
				if (LavenderePdaConfig.isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso()) {
					throw new ClienteAtrasadoVlTitulosException(msgErro, vlTotalTitulosAtraso);
				}
				UiUtil.showWarnMessage(msgErro);
			}
		}
	}
	
	private void validaFotoPedidoPresentePedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaFotoPedidoNoSistema && LavenderePdaConfig.usaVerificacaoFotoPedidoPresentePedido == 1) {
			FotoPedido fotoPedidoFilter = new FotoPedido();
			fotoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
			fotoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
			fotoPedidoFilter.nuPedido = pedido.nuPedido;
			fotoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			int qtFotos = FotoPedidoService.getInstance().countByExample(fotoPedidoFilter);
			if (qtFotos <= 0) {
				throw new ValidationException(Messages.PEDIDO_SEM_FOTO_CADASTRADA);
			}
		}
	}

	private void calculaDescontoComPedidoBonificacao(Pedido pedidoRelacionado, Pedido pedidoBonificacao) throws SQLException {
		calculaDescontoPonderadoPedido(pedidoRelacionado);
		pedidoRelacionado.vlDesconto += pedidoBonificacao.vlTotalPedido;
		pedidoRelacionado.vlPctDesconto = ValueUtil.round((pedidoRelacionado.vlDesconto / pedidoRelacionado.vlTotalBrutoItens) * 100);
	}

	private Pedido findPedidoRelacionadoBonificacao(Pedido pedidoBonificacao) throws SQLException {
		Pedido pedidoRelacionado = new Pedido();
		pedidoRelacionado = (Pedido) pedidoBonificacao.clone();
		pedidoRelacionado.nuPedido = pedidoBonificacao.nuPedidoRelBonificacao;
		pedidoRelacionado = (Pedido) findByRowKey(pedidoRelacionado.getRowKey());
		if (pedidoRelacionado == null) {
			return pedidoBonificacao;
		}
		findItemPedidoList(pedidoRelacionado);
		if (LavenderePdaConfig.isGeraCreditoIndiceBonificacao() && pedidoBonificacao.isPedidoBonificacao() && (pedidoBonificacao.getTipoPedido().isFlTipoCreditoFrete() || pedidoBonificacao.getTipoPedido().isFlTipoCreditoCondicao())) {
			return pedidoRelacionado;
		}
		calculaDescontoComPedidoBonificacao(pedidoRelacionado, pedidoBonificacao);
		return pedidoRelacionado;
	}

	private void validaProdutosPendentes(Pedido pedido) throws SQLException {
		if (pedido.isPedidoTroca() || pedido.isPedidoBonificacao() || pedido.isOportunidade() || pedido.isPedidoAbertoNaoEditavel() ||
				(pedido.isPedidoCritico() && LavenderePdaConfig.permiteApenasUmItemPedidoProdutoCritico && pedido.getQtItensLista() >= 1))
			return;
		Vector itemList = ProdutoService.getInstance().getProdutosHistoricoNaoInseridosPedido(pedido);
		if (!itemList.isEmpty()) {
			throw new RelProdutosPendentesPedidoException(Messages.VALIDACAO_FECHAR_PEDIDO_REL_PRODUTOS , itemList);
			}
		}
	
	private void validaAplicacaoDescontoIndiceFinanceiroSaldoFlexNegativo(Pedido pedido) {
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() && !pedido.pedidoSimulacao && (pedido.vlVerbaPedidoPositivo + pedido.vlVerbaPedido) < 0) {
			throw new AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException(Messages.APLICA_DESCONTO_INDICE_FINANCEIRO_SALDOVERBA_NEGATIVO);
		}
	}

	public void validateFechamento(final Pedido pedido, boolean fromListaPedidos) throws SQLException {
		try {
			validateFechamentoApenasViaLista(pedido);
			validateFecharPedido(pedido, true, fromListaPedidos);
		} catch (ValidationException e) {
			PedidoService.validationFechamentoCount = 0;
			throw e;
		}
	}
	
	private void validateFechamentoApenasViaLista(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil && LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido() && !ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada) && PedidoService.getInstance().isPedidoDtEntregaFinalSemanaFeriado(pedido.dtEntrega)) {
			throw new ValidationException(Messages.PEDIDO_DTENTREGA_DIA_NAO_UTIL_NAO_PERMITIDO);
		}
		if (LavenderePdaConfig.usaVigenciaDescQuantidade && ItemPedidoService.getInstance().isPossuiProdutosDescQtdVencidos(pedido)) {
			throw new ValidationException(Messages.MSG_ITENS_DESCQTD_VENCIDOS);
	}
		validateObrigaConsignacao(pedido);
	}

	private void validateObrigaConsignacao(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao() && !pedido.isPedidoConsignado()) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			if (tipoPedido != null && tipoPedido.isObrigaConsignacao()) {
				throw new ValidationException(Messages.FECHAR_PEDIDO_ERRO_ITEM_CONSIGNADO);
			}
		}
	}

	private void validateReabrirPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && isPedidoComBonificacaoRelacionada(pedido)) {
			if (!LavenderePdaConfig.isPermiteReabrirPedidoBonificacao() && pedido.isPedidoBonificacao()) {
				throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_REABRIR_PEDIDO_VENDA);
			} else if (!pedido.isPedidoBonificacao()) {
				throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_REABRIR_PEDIDO_BONIFICACAO);
		}
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && ValueUtil.isNotEmpty(pedido.nuPedidoRelTroca)) {
			throw new ValidationException(pedido.isPedidoTroca() ? Messages.PEDIDO_RELACIONADO_ERRO_REABRIR_PEDIDO_VENDA : Messages.PEDIDO_RELACIONADO_ERRO_REABRIR_PEDIDO_TROCA);
		}
		if (LavenderePdaConfig.usaPedidoComplementar() && !pedido.isPedidoComplementar()) {
			Pedido pedidoRelacionado = new Pedido();
			pedidoRelacionado.cdEmpresa = SessionLavenderePda.cdEmpresa;
			pedidoRelacionado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante; //Representante da Sessão ou Rep. do Supervisor
			pedidoRelacionado.nuPedidoComplementado = pedido.nuPedido;
			Vector listPedidoRelacionado = findAllByExample(pedidoRelacionado);
			if (listPedidoRelacionado.size() > 0) {
				throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_REABRIR_PEDIDO_COMPLEMENTADO);
			}
		}
		if (!pedido.isPedidoFechado()) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_STATUS_DIFERENTE_ABERTO, StatusPedidoPdaService.getInstance().getDsStatusPedidoAberto()));
		}
		
	}

	/**
	 * Reabre o pedido passado por parâmetro, fazendo as validações necessárias
	 *
	 * @param pedido - pedido a ser reaberto
	 */
	public void reabrirPedido(final Pedido pedido) {
		if (pedido == null)	return;
		pedido.isPedidoReaberto = true;
		pedido.ignoraValidacaoProdutosPendentes = false;
		pedido.ignoraAvisoSugestaoVendaSemQtdOutrasEmpresas = false;
		pedido.ignoraAvisoSugestaoVendaComQtdOutrasEmpresas = false;
		pedido.ignoraGiroProdutoPendente = false;
		try {
			validateReabrirPedido(pedido);
			findItemPedidoList(pedido);
			marcaItensReabrindoPedido(pedido);
			try {
				pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
				limparVariaveisControleLiberacaoCondicaoPagamento(pedido);
				PedidoPdbxDao.getInstance().updateReabrirPedido(pedido);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_RP, StringUtil.getStringValue(pedido.vlTotalPedido));
			} catch (Throwable e) {
				pedido.cdStatusPedido = PedidoService.getInstance().findColumnByRowKey(pedido.getRowKey(), Pedido.NMCOLUNA_CDSTATUSPEDIDO);
				throw e;
			}
			pedido.flSituacaoReservaEstReabrePedido = ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEst) ? pedido.flSituacaoReservaEst : ValueUtil.VALOR_NI;
			pedido.ignoraGeracaoReservaEstoque = false;
			pedido.oldCdMotivoPendencia = pedido.cdMotivoPendencia;
			if ((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente())
					&& !pedido.ignoraInativacaoReservaEstoque
					&& !pedido.isIgnoraControleEstoque()) {
				inativaReservaEstoque(pedido);
				if (pedido.flSituacaoReservaEstReabrePedido.equals(ValueUtil.VALOR_SIM)) {
					pedido.flSituacaoReservaEstReabrePedido = pedido.flSituacaoReservaEst;
				}
			}
			reabrirItensPedido(pedido);
			if (pedido.isFlCotaCondPagto()) {
				pedido.vlDesconto = 0;
			}
			if (LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
				pedido.flCreditoClienteLiberadoSenha = ValueUtil.VALOR_NAO;
				PedidoPdbxDao.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_FLCREDITOCLIENTELIBERADOSENHA, pedido.flCreditoClienteLiberadoSenha, Types.VARCHAR);
			}
			if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
				pedido.flPendente = ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() || LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
				pedido.flPendenteLimCred = ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
				pedido.flBoletoImpresso = ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido()	&& LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
				TransportadoraRegService.getInstance().removeVlFretePedido(pedido);
				ParcelaPedidoService.getInstance().insertParcelasPedido(pedido);
			}
			if (LavenderePdaConfig.isBloqueiaFechamentoPedidoRentabilidadeMinima()) {
				pedido.flRentabilidadeLiberada = "";
			}
			if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && (pedido.isPedidoItemPendente() || pedido.isDescontoLiberadoSenha())) {
				pedido.flItemPendente = "";
				pedido.flDescontoLiberadoSenha = "";
			}
			if (LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela() && pedido.isValorMinParcelaLiberadoSenha()) {
				pedido.flValorMinParcelaLiberadoSenha = ValueUtil.VALOR_NI;
			}
			if (LavenderePdaConfig.isUsaAgendaDeVisitas()) {
				reabreVisitaParaEnvio(pedido);
			}
			if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				VisitaPedidoService.getInstance().reabreVisitaPedido(pedido);
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(pedido.itemPedidoList) && ProdutoService.getInstance().isPossuiFamiliasDescProg(pedido, null)) {
				if (atualizaDescProgressivoPedido(pedido).atualizouDesconto) {
					UiUtil.showInfoMessage(Messages.MSG_AVISO_DESCPROGRESSIVO_ATUALIZADO);
				}
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
				DescFaixaValorPedidoService.getInstance().removeDescontoReabrirPedido(pedido);
				calculate(pedido);
				updatePedidoAndItens(pedido);
			}
			if (LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
				if (!(LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha())) {
					if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
						pedido.vlVerbaPedido = pedido.vlVerbaPedido * -1;
						retiraDescontosDoPedidoRepondoFlex(pedido);
						VerbaSaldoService.getInstance().updateVlSaldoByPedidoByVlVerbaPedido(pedido);
					} else {
						retiraDescontosDoFimDoPedido(pedido);
					}
				}
				updatePedidoAndItens(pedido);
			}
			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex()) {
				pedido.vlVerbaPedido = pedido.vlVerbaPedido * -1;
				VerbaSaldoService.getInstance().updateVlSaldoByPedidoByVlVerbaPedido(pedido);
				pedido.vlVerbaPedido = pedido.vlVerbaPedido * -1;
			}
			if (LavenderePdaConfig.destacaClienteAtendidoMes && (Cliente.CLIENTE_FLATENDIDO_PDA.equals(pedido.getCliente().flAtendido))) {
				if (PedidoPdbxDao.getInstance().countPedidoClienteFechadoOrTransmitidoNoMes(pedido) == 0) {
					pedido.getCliente().flAtendido = ValueUtil.VALOR_NAO;
					ClienteService.getInstance().updateFlAtendido(pedido.getCliente());
				}
			}
			if (LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido) {
				if (DescontoGrupoService.getInstance().retiraDescQtdGrupoAuto(pedido)) {
					ItemPedidoService.getInstance().updateItensPedido(pedido.itemPedidoList);
					updatePedidoOnly(pedido);
				}
			}
			if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
				UsuarioDescService.getInstance().retiraVlTotalPedidoUsuarioDescPda(pedido);
				if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
					PedidoDescService.getInstance().deletePedidoDesc(pedido);
				}
			}
			if ((LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto() && CondicaoPagamentoService.getInstance().getCondicaoPagamento(pedido.cdCondicaoPagamento).vlIndiceFinanceiro > 1) || LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
				reloadValoresDosItensPedido(pedido, true, false);
				updateItensPedidoAfterChanges(pedido);
				calculate(pedido);
				updatePedidoAfterCrudItemPedido(pedido);
			}
			
			if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
				PedidoLogService.getInstance().savePedidoLog(TipoRegistro.REABERTURA, pedido);
				PedidoLogService.getInstance().reabrePedidoLogParaEnvio(pedido);
			}
			if (LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogo()) {
				ProdutoDesejado produtoDesejado = new ProdutoDesejado.Builder(pedido).build();
				produtoDesejado.flTipoAlteracao = ProdutoDesejado.FLTIPOALTERACAO_ORIGINAL;
				ProdutoDesejadoService.getInstance().updateFlTipoAlteracaoByExample(produtoDesejado);
			}
			if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
				PedidoBoletoService.getInstance().deleteBoletosPedido(pedido);
			}
			if (LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente && pedido.getVlVerbaPedidoDisponivel() < 0) {
				VerbaSaldoService.getInstance().recalculateAndUpdateVerbaSaldoPda();
			}
			if (LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao && (pedido.vlVerbaPedido != 0 || pedido.vlVerbaPedidoPositivo != 0)) {
				MargemContribFaixaService.getInstance().estornaVerbaSaldo(pedido);
			}
			if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
				ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedido, true);
				PedidoService.getInstance().calculaVerbaPositiva(pedido);
			}
			if (LavenderePdaConfig.naoConsomeVerbaAutomaticamenteAoFecharPedido) {
				pedido.deveValidarConsumoVerbaPedido = false;
			}
			if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() && LavenderePdaConfig.usaVolumeVendaMensalRede) {
				Vector pedidoRedeList = PedidoService.getInstance().findAllPedidosDaRede(pedido);
				PedidoService.getInstance().atualizaDescontosVolumeVendaRede(pedidoRedeList);
			}
			updatePedidoPendenteComItemBonificado(pedido);
			removeFlagValorMinParcelaLiberadoSenha(pedido);
			if (LavenderePdaConfig.usaControlePontuacao) {
				PontExtPedService.getInstance().deleteByPedido(pedido);
			}
			if (LavenderePdaConfig.usaValidaConversaoFOB()) {
				pedido.flPendenteFob = ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.usaSelecaoDocAnexoPedido()) {
				DocumentoAnexoService.getInstance().atualizaDocAnexoParaEnvio(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey(), DocumentoAnexo.FLTIPOALTERACAO_ORIGINAL);
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				ItemPedidoBonifCfgService.getInstance().atualizaDomainParaEnvio(pedido, ItemPedidoBonifCfg.FLTIPOALTERACAO_ORIGINAL);
			}
		} catch (PedidoNaoFechadoException ex) {
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_RPE, StringUtil.getStringValue(pedido.vlTotalPedido), ex.getMessage());
			throw ex;
		} catch (Throwable ex) {
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_ERRO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_RPE, StringUtil.getStringValue(pedido.vlTotalPedido), ex.getMessage());
			throw new ValidationException(ex.getMessage());
		}
	}

	private void marcaItensReabrindoPedido(final Pedido pedido) {
		if (pedido == null || ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemValidate = (ItemPedido) pedido.itemPedidoList.items[i];
			itemValidate.isReabrindoPedido = true;
		}
	}

	private void limparVariaveisControleLiberacaoCondicaoPagamento(Pedido pedido) {
		if (!LavenderePdaConfig.isLiberaComSenhaCondPagamento()) return;
		pedido.qtDiasCPgtoLibSenha = 0;
		pedido.vlTotalPedidoLiberado = 0;
	}

	private void fechaVisitaParaEnvio(final Pedido pedido) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.nuPedido = pedido.nuPedido;
		visitaFilter.cdCliente = pedido.cdCliente;
		visitaFilter.dtVisita = pedido.dtEmissao;
		visitaFilter.cdEmpresa = pedido.cdEmpresa;
		visitaFilter.cdRepresentante = pedido.cdRepresentante;
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			visitaFilter.nuSequencia = pedido.nuSequenciaAgenda;
		}
		Vector visitasList = VisitaService.getInstance().findAllByExample(visitaFilter);
		int size = visitasList.size();
		Visita visita;
		for (int i = 0; i < size; i++) {
			visita = (Visita) visitasList.items[i];
			VisitaService.getInstance().fechaVisita(visita);
		}
		visitasList = null;
	}

	private void reabreVisitaParaEnvio(final Pedido pedido) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.nuPedido = pedido.nuPedido;
		visitaFilter.cdCliente = pedido.cdCliente;
		visitaFilter.dtVisita = pedido.dtEmissao;
		visitaFilter.cdEmpresa = pedido.cdEmpresa;
		visitaFilter.cdRepresentante = pedido.cdRepresentante;
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			visitaFilter.nuSequencia = pedido.nuSequenciaAgenda;
		}
		Vector visitasList = VisitaService.getInstance().findAllByExample(visitaFilter);
		int size = visitasList.size();
		Visita visita;
		for (int i = 0; i < size; i++) {
			visita = (Visita) visitasList.items[i];
			VisitaService.getInstance().reabreVisita(visita);
			if (LavenderePdaConfig.usaVisitaFoto) {
				VisitaFotoDao.getInstance().updateFotosVisitaParaEnvio(visita.cdEmpresa, visita.cdRepresentante, visita.flOrigemVisita, visita.cdVisita, visita.flTipoAlteracao);
            }
		}
		visitasList = null;
	}

	public Vector montaRelResumoPedidos(final Vector pedidos) throws SQLException {
		int qtAVista = 0;
		int qtAPrazo = 0;
		int qtItensAVista = 0;
		int qtItensAPrazo = 0;
		double vlTotalAVista = 0;
		double vlTotalAPrazo = 0;
		//--
		int size = pedidos.size();
		Pedido pedido;
		ItemPedido itemPedido;
		CondicaoPagamento condFilter;
		for (int i = 0; i < size; i++) {
			pedido = (Pedido)pedidos.items[i];
			//--
			itemPedido = new ItemPedido();
			itemPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemPedido.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			itemPedido.nuPedido = pedido.nuPedido;
			itemPedido.flOrigemPedido = pedido.flOrigemPedido;
			double qtItens = ItemPedidoService.getInstance().countItensPedido(itemPedido);
			//--
			condFilter = new CondicaoPagamento();
			condFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
			condFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
			int qtDiasMediosPagamento = ValueUtil.getIntegerValue(CondicaoPagamentoService.getInstance().findColumnByRowKey(condFilter.getRowKey(), "QTDIASMEDIOSPAGAMENTO"));
			if (qtDiasMediosPagamento > 0) {
				qtAPrazo++;
				qtItensAPrazo += qtItens;
				vlTotalAPrazo += pedido.vlTotalPedido;
			} else {
				qtAVista++;
				qtItensAVista += qtItens;
				vlTotalAVista += pedido.vlTotalPedido;
			}
		}
		Vector result =  new Vector(3);
		result.addElement(new String[] {Messages.CONDICAOPAGAMENTO_AVISTA, StringUtil.getStringValueToInterface(qtAVista), StringUtil.getStringValueToInterface(qtItensAVista), StringUtil.getStringValueToInterface(vlTotalAVista)});
		result.addElement(new String[] {Messages.CONDICAOPAGAMENTO_APRAZO, StringUtil.getStringValueToInterface(qtAPrazo), StringUtil.getStringValueToInterface(qtItensAPrazo), StringUtil.getStringValueToInterface(vlTotalAPrazo)});
		result.addElement(new String[] {Messages.LABEL_TOTAL, StringUtil.getStringValueToInterface(qtAVista + qtAPrazo), StringUtil.getStringValueToInterface(qtItensAVista + qtItensAPrazo), StringUtil.getStringValueToInterface(vlTotalAPrazo + vlTotalAVista)});
		return result;
	}
	
	public double getQtdeItensPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		return ItemPedidoService.getInstance().sumByExample(itemPedido, "QTITEMFISICO");
	}

	public double getQtItensFaturamentoPedido(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (ValueUtil.isNotEmpty(SessionLavenderePda.getRepresentante().cdRepresentante)) {
			itemPedido.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		return ItemPedidoService.getInstance().sumByExample(itemPedido, "QTITEMFATURAMENTO");
	}
	
	//-------------------------------------------------------------------------------
	// Métodos relacionados ao item do pedido
	//-------------------------------------------------------------------------------

	private ItemPedidoService getItemPedidoService() {
		return ItemPedidoService.getInstance();
	}

	public void calculateItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean validateItemPedido) throws SQLException {
		preparaCamposCalculo(pedido, itemPedido);
		getItemPedidoService().calculate(itemPedido, pedido);
		if (validateItemPedido) {
			getItemPedidoService().validate(itemPedido);
		}
	}

	private void preparaCamposCalculo(final Pedido pedido, final ItemPedido itemPedido) {
		// seta desconto e acrescimeo 0, pois serão recalculados a partir do novo valor do item inserido pelo usuário
		if (itemPedido.isEditandoValorItem()) {
			itemPedido.vlPctDesconto = 0;
			itemPedido.vlPctAcrescimo = 0;
		}
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			itemPedido.dtEmissaoPedido = pedido.dtEmissao;
		}
	}

	public void calculateTrocaPedido(final Pedido pedido) {
		double vlTrocaRec = 0;
		double vlTrocaEnt = 0;
		ItemPedido itemPedidoTemp;
		int size = pedido.itemPedidoTrocaList.size();
		for (int i = 0; i < size; i++) {
			itemPedidoTemp = (ItemPedido)pedido.itemPedidoTrocaList.items[i];
			if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(itemPedidoTemp.flTipoItemPedido)) {
				vlTrocaEnt += itemPedidoTemp.vlTotalItemPedido;
			} else {
				vlTrocaRec += itemPedidoTemp.vlTotalItemPedido;
			}
		}
		pedido.vlTrocaRecolher = vlTrocaRec;
		pedido.vlTrocaEntregar = vlTrocaEnt;
	}

	public void insertItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido) throws SQLException {
		try {
			insertItemPedido(pedido, itemPedido, updatePedido, true);
		} finally {
			itemPedido.flUIChange = false;
		}
	}
	
	public void insertItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido, boolean fromUI) throws SQLException {
		itemPedido.flUIChange = fromUI;
		validate(pedido);
		validateInsertOrUpdateItemPedido(pedido, itemPedido);
		//--
		if (pedido.itemPedidoList.size() >= LavenderePdaConfig.maximoItensPorPedido) {
			throw new ValidationException(Messages.PEDIDO_MSG_MAXIMO_ITENS);
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() && !LavenderePdaConfig.isGeraParcelasEmPercentual()) {
			ParcelaPedidoService.getInstance().validateProduto(pedido, itemPedido);
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			AreaVendaProdutoService.getInstance().validadeProdutoAreaVenda(pedido.cdAreaVenda, itemPedido.cdProduto, true);
		}
		//--
		//Verifica se é o segundo item do pedido e se atende ao mesmo grupoProdudo do primeiro item
		if (LavenderePdaConfig.usaCCClientePorTipoPedido && (pedido.itemPedidoList.size() > 0) && !ValueUtil.isEmpty(LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido)) {
			CCCliPorTipoService.getInstance().validateBasedOnTheFirstItem(pedido, itemPedido);
		}
		
		if (LavenderePdaConfig.restringeItemPedidoPorLocal && !ValueUtil.isEmpty(pedido.cdLocal)) {
			itemPedido.cdLocal = pedido.cdLocal;
		}
		//--
		getItemPedidoService().insert(itemPedido);
		//--

		if (LavenderePdaConfig.restringeItemPedidoPorLocal && ValueUtil.valueEquals(pedido.itemPedidoList.size(), 0)) {
			LoteProduto lote = LoteProdutoService.getInstance().findByItemPedido(itemPedido);
			if (lote != null) {
				pedido.cdLocal = lote.cdLocal;
			}
		}
		
		if (LavenderePdaConfig.usaCCClientePorTipoPedido && (pedido.itemPedidoList.size() == 0)) {
			CCCliPorTipoService.getInstance().updateCCCliPorTipoIfIsFirstItem(pedido, itemPedido);
		}
		//--
		if (itemPedido.isOportunidade()) {
			pedido.itemPedidoOportunidadeList.addElement(itemPedido);
		} else if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher()) {
			pedido.itemPedidoTrocaList.addElement(itemPedido);
		} else {
			pedido.itemPedidoList.addElement(itemPedido);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.isBonificacaoAutomatica && !itemPedido.isItemBrinde && !itemPedido.isKitTipo3() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, true);
			if (LavenderePdaConfig.isUsaConsumoVerbaSupervisor() && itemPedido.isItemBonificacao()) {
				VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedido));
				ItemPedidoService.getInstance().updateValuesVerba(itemPedido);
			}
		}
		Produto produto = itemPedido.getProduto();
		if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos && ValueUtil.isEmpty(produto.cdMarcadores)) {
			produto.cdMarcadores = MarcadorProdutoService.getInstance().findMarcadoresByProduto(produto, pedido.cdCliente);
		}
		//--
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido()) {
			for (int i = 0; i < pedido.itemPedidoNaoInseridoSugestaoPedList.size(); i++) {
				ItemPedido item = (ItemPedido)pedido.itemPedidoNaoInseridoSugestaoPedList.items[i];
				if (itemPedido.equals(item)) {
					pedido.itemPedidoNaoInseridoSugestaoPedList.removeElementAt(i);
					break;
				}
			}
		}
		if (updatePedido) {
		updatePedidoAfterCrudItemPedido(pedido);
	}
	}

	public void insertItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		insertItemPedido(pedido, itemPedido, true);
	}

	public void updateItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		updateItemPedido(pedido, itemPedido, true);
	}

	public void updateItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido) throws SQLException {
		try {
			updateItemPedido(pedido, itemPedido, updatePedido, true);
		} finally {
			itemPedido.flUIChange = false;
		}
	}
	
	protected void updateItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido, boolean fromUI) throws SQLException {
		itemPedido.flUIChange = fromUI;
		validateInsertOrUpdateItemPedido(pedido, itemPedido);
		if (LavenderePdaConfig.usaModuloTrocaNoPedido) {
			validateItemTroca(pedido, itemPedido);
		}
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			validateVerbaPorPedido(pedido, itemPedido);
		}
		//--
		getItemPedidoService().update(itemPedido);
		pedido.itemPedidoList.removeElement(itemPedido);
		if (itemPedido.isOportunidade()) {
			pedido.itemPedidoOportunidadeList.addElement(itemPedido);
		} else if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher()) {
			pedido.itemPedidoTrocaList.removeElement(itemPedido);
			pedido.itemPedidoTrocaList.addElement(itemPedido);
		} else {
			pedido.itemPedidoList.addElement(itemPedido);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false);
			if (LavenderePdaConfig.isUsaConsumoVerbaSupervisor() && itemPedido.isItemBonificacao()) {
				ItemPedido itemPedidoVerba = itemPedido;
				if (pedido.itemPedidoList.contains(itemPedidoVerba)) {
					itemPedidoVerba = (ItemPedido) pedido.itemPedidoList.items[pedido.itemPedidoList.indexOf(itemPedidoVerba)];
				}
				VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedidoVerba, ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedidoVerba));
				ItemPedidoService.getInstance().updateValuesVerba(itemPedidoVerba);
			}
		}
		//--
		if (updatePedido) {
		updatePedidoAfterCrudItemPedido(pedido);
	}
	}

	public void deleteItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		try {
			deleteItemPedido(pedido, itemPedido, true, true);
		} finally {
			itemPedido.flUIChange = false;
	}
	}

	public void deleteItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido) throws SQLException {
		deleteItemPedido(pedido, itemPedido, updatePedido, false);
	}
	
	public void deleteItemPedido(final Pedido pedido, final ItemPedido itemPedido, boolean updatePedido, boolean fromUI) throws SQLException {
		itemPedido.flUIChange = fromUI;
		if (itemPedido.isOportunidade() && !pedido.isOportunidade()) {
			itemPedido.pedido.itemPedidoOportunidadeList.removeElement(itemPedido);
			return;
		}
		validateDeleteItemPedido(pedido, itemPedido);
		getItemPedidoService().delete(itemPedido);
		getItemPedidoService().setEstoqueAtualizadoItemPedido(itemPedido);
		if (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) {
			if (pedido.itemPedidoList.size() == 1) {
				pedido.vlDesconto = 0;
			}
		}
		if (permiteDeletarVerbaConsumidaNoItem(pedido) && !itemPedido.isIgnoraControleVerba()) {
			if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
				itemPedido.dtEmissaoPedido = pedido.dtEmissao;
			}
			if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente && pedido.isPedidoBonificacao()) {
				VerbaClienteService.getInstance().deleteVlSaldo(itemPedido);
			} else {
				VerbaService.getInstance().deleteVlSaldo(pedido, itemPedido);
			}
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
				pedido.qtdCreditoDescontoConsumido -= itemPedido.qtdCreditoDesc;
			}
		}
		pedido.removerItemPedido(itemPedido);
		
		//Atualiza o CCCli caso seja excluido o ultimo itemPedido do pedido
		if (LavenderePdaConfig.usaCCClientePorTipoPedido && (pedido.itemPedidoList.size() == 0)) {
			CCCliPorTipoService.getInstance().updateCCCliPorTipoIfIsLastItem(pedido, itemPedido);
		}
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && itemPedido.permiteAplicarDesconto() && !itemPedido.possuiDescMaxProdCli()) {
			itemPedido.usaDescontoCascata = false;
			ItemPedidoService.getInstance().aplicaMaiorDescontoCascataItem(itemPedido);
			pedido.recalculaPedidoMudancaFaixaDesc = itemPedido.oldVlPctDesc != itemPedido.vlPctDesconto;
			pedido.itensAtingiramFaixa = itemPedido.tipoDesc == 1;
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido 
				&& (itemPedido.isRecebeuDescontoPorQuantidade() || (itemPedido.getProduto().isKit() && LavenderePdaConfig.isUsaKitBaseadoNoProduto() || LavenderePdaConfig.usaDescontoPorQuantidadeValor))) {
			ItemPedidoService.getInstance().aplicaDescQtdSimilares(itemPedido);
	}
		if (!itemPedido.isKitTipo3()) {
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto() && itemPedido.isItemVendaNormal()) {
				ItemPedidoService.getInstance().deleteItemPedidoBonificadoAutomaticamenteNaExclusaoItemPedidoVenda(itemPedido);		
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoComBrindes()) {
			if (itemPedido.isItemVendaNormal()) {
					ItemPedidoBonifCfgService.getInstance().desvincularBonificacoesOuBrindesFromPedidoOnDeleteItemVenda(itemPedido);
			} else if (itemPedido.isItemBonificacao()) {
				ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg(itemPedido);
				itemPedidoBonifCfgFilter.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE;
				Vector itemPedidoBonifCfgBrindeList = ItemPedidoBonifCfgService.getInstance().findAllByExample(itemPedidoBonifCfgFilter);
				if (ValueUtil.isNotEmpty(itemPedidoBonifCfgBrindeList)) {
					ItemPedidoBonifCfgService.getInstance().deleteAllByExample(itemPedidoBonifCfgFilter);
				}
			}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false);
					ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(itemPedido.pedido.itemPedidoList, true);
		}
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacaoFamilia()) {
				ItemPedidoBonifCfgService.getInstance().deleteItemPedidoBonigCfgByItemPedido(itemPedido);
			}
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && fromUI) {
			pedido.recalculoDescontoProgressivoDTO = atualizaDescProgressivoPedido(pedido);
		}
		if (updatePedido) {
			updatePedidoAfterCrudItemPedido(pedido, true);
		}
	}

	private void validateInsertOrUpdateItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			BonificacaoService.getInstance().validateBonificacaoPorGrupoBonificacao(pedido, itemPedido);
		}
		//valida realizado de metaPorGrupoProd
		if (LavenderePdaConfig.usaPesoGerarRealizado && LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
			if ((itemPedido.isItemBonificacao() && LavenderePdaConfig.usaItensBonificadoCalculoRealizado) || itemPedido.isItemVendaNormal()) {
				ItemPedidoService.getInstance().validaVendaProdutoBaseadoRealizadoMetaGrupoProd(itemPedido, true, pedido, false);
			}
		}
		if(LavenderePdaConfig.restringeItemPedidoPorLocal && !ValueUtil.isEmpty(pedido.cdLocal)) {
			if(!LoteProdutoService.getInstance().validaCdLocal(itemPedido, pedido.cdLocal)) {
				String dsLocalPadraoPedido = LocalService.getInstance().getDsLocal(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdLocal);
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_VALIDACAO_LOCAL_DIFERENTE, new Object[] {dsLocalPadraoPedido}));
	}
		}
		if (LavenderePdaConfig.isUsaCulturaInformacoesAdicionaisItemPedido() && LavenderePdaConfig.isUsaPragaInformacoesAdicionaisItemPedido()) {
			GrupoProduto1 grupoProduto1 = GrupoProduto1Service.getInstance().findGrupoProduto1ByItemPedido(itemPedido);
			if (grupoProduto1 != null && ValueUtil.valueEquals(grupoProduto1.flObrigaCulturaPraga, ValueUtil.VALOR_SIM) && ((ValueUtil.isEmpty(itemPedido.cdCultura) || ValueUtil.isEmpty(itemPedido.cdPraga)))) {
				throw new ValidationException(Messages.CULTURAPRAGA_MSG_ERRO_FLOBRIGA);
			}			
		}
	}

	private void validateDeleteItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().deleteItemPedido(pedido, itemPedido);
		}
		if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			BonificacaoService.getInstance().validateBonificacaoPorGrupoBonificacao(pedido, itemPedido, true);
		}
		if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
			validaDescontoEmValorPedido(pedido);
				}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido) {
			validateDeleteItemTroca(pedido, itemPedido);
		}
		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && LavenderePdaConfig.geraVerbaPositiva) {
			if (itemPedido.vlVerbaItemPositivoOld > 0) {
				VerbaService.getInstance().validateDeleteSaldoPositivo(itemPedido);
			}
		}
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			validaDescValorPorPedidoConsumindoVerba(pedido, itemPedido);
			}
		if (LavenderePdaConfig.isPermiteBonificarProduto() && !pedido.bonificacaoLiberada) {
			BonificacaoService.getInstance().validateBonificacaoItem(pedido, itemPedido, false, true);
			}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			validaDescontoCategoria(pedido, itemPedido);
			}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() && pedido.vlTotalItens > 0) {
			validaDescontoCapaPedido(pedido);
			}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto() && itemPedido.isItemBonificacao()) {
			validaDeleteItemBonificacaoAutomatica(itemPedido);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoComBrindes()) {
			ItemPedidoBonifCfgService.getInstance().validaQtdMinimaItemPedidoComBrinde(itemPedido, 0);
		}
	}

	private void validaDeleteItemBonificacaoAutomatica(ItemPedido itemPedido) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg(itemPedido);
		itemPedidoBonifCfgFilter.flBonificacaoAutomatica = ValueUtil.VALOR_SIM;
		itemPedidoBonifCfgFilter.limit = 1;
		itemPedidoBonifCfgFilter = (ItemPedidoBonifCfg) ItemPedidoBonifCfgService.getInstance().findAllByExample(itemPedidoBonifCfgFilter).elementAt(0);
		if (!itemPedido.isKitTipo3() && itemPedidoBonifCfgFilter != null) {
			try {
				Produto produto = ProdutoService.getInstance().getProduto(itemPedidoBonifCfgFilter.cdEmpresa, itemPedidoBonifCfgFilter.cdRepresentante, itemPedidoBonifCfgFilter.cdProduto);
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_EXCLUSAO_NAO_PERMITIDA_ITEM_BONIFICADO_AUTOMATICO, produto.toString()));
			} catch (NullPointerException e) {
				throw new ValidationException(Messages.PRODUTO_NAO_ENCONTRADO);
			}
		}
	}

	private void validaDescontoCapaPedido(Pedido pedido) {
				double newVlTotalPedido;
		double pctDesconto = 0;
				double pctMaxDesconto = LavenderePdaConfig.aplicaDescontoNaCapaDoPedido; 
				if (pedido.vlDesconto > 0) {
					newVlTotalPedido = pedido.vlTotalPedido - pedido.vlDesconto;
					pctDesconto = 100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens);
					pctDesconto = ValueUtil.round(pctDesconto);
				} else if (pedido.vlPctDesconto > 0) {
					pctDesconto = 0;
					if (pedido.itemPedidoList.size() == 1) {
						pedido.vlPctDesconto = 0;
					}
				}
				if (pctDesconto > pctMaxDesconto) {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(pctMaxDesconto) + "%"}));
				}
			}

	private void validaDescontoCategoria(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		Pedido pedClone = (Pedido) pedido.clone();
		pedClone.itemPedidoList = new Vector(pedido.itemPedidoList.size());
		pedClone.itemPedidoList.addElementsNotNull(pedido.itemPedidoList.items);
		pedClone.itemPedidoList.removeElement(itemPedido);
		try {
			calculate(pedClone);
		} catch (DescontoCategoriaException e) {
			String msg = Categoria.TIPO_CATEGORIA_ATACADO.equals(e.tipoCategoria) ? Messages.CATEGORIA_ATACADO : Messages.CATEGORIA_ESPECIAL;
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_DESC_CATEGORIA_EXCLUSAO_ITEM, msg));
		}
	}

	private void validaDescValorPorPedidoConsumindoVerba(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if ((LavenderePdaConfig.qtMinimaItensParaLiberarVerbaPorPedido > (pedido.itemPedidoList.size() - 1)) && (pedido.vlDesconto != 0)) {
			throw new ValidationException(Messages.VERBAPEDIDO_QT_MINIMA_EXCLUSAO);
		}
		double newVlTotalPedido = pedido.vlTotalItens - itemPedido.vlTotalItemPedido;
		double pctDesconto = 0;
		if (newVlTotalPedido > 0) {
			pctDesconto = (pedido.vlDesconto / newVlTotalPedido) * 100;
		}
		double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba;
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
			double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
			pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
		}
		if (pctMaxDesconto != 0 && pctDesconto > pctMaxDesconto || (pedido.vlDesconto > 0 && newVlTotalPedido == 0)) {
			throw new ValidationException(Messages.VERBAPEDIDO_PCTMAX_EXCLUSAO);
		}
	}

	private void validaDescontoEmValorPedido(Pedido pedido) throws SQLException {
		if (pedido.vlTotalItens > 0) {
			double newVlTotalPedido = pedido.vlTotalPedido - pedido.vlDesconto;
			double pctDesconto = 100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens);
			pctDesconto = ValueUtil.round(pctDesconto);
			double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescontoEmValorPorPedido;
			if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
				double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
				pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
			}
			if (pctMaxDesconto != 0 && pctDesconto > pctMaxDesconto) {
				throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{pctDesconto + "%", pctMaxDesconto + "%"}));
			}
		}
	}

	public void calculateFretePedido(Pedido pedido, boolean onChangeTransp) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if ((tipoPedido != null && tipoPedido.isIgnoraCalculoFrete()) || !LavenderePdaConfig.usaTransportadoraPedido() || !LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.aplicaPercentualFreteCalculoPrecoItem)
			return;
		double vlTotalFreteItensPedido = 0;
		TranspTipoPedService.getInstance().recalculateFreteItensPedido(pedido, onChangeTransp);
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlTotalFreteItensPedido += itemPedido.vlFrete;
		}
		pedido.vlFrete = vlTotalFreteItensPedido + TranspTipoPedService.getInstance().getVlFreteTransportadora(pedido.getTranspTipoPed(), pedido.vlTotalPedido);
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente && pedido.vlFrete == 0) {
			pedido.vlFreteCliente = 0;
			pedido.vlFreteRepresentante = 0;
			pedido.vlPctFreteRepresentante = 0;
		}
	}

	public void updatePedidoAfterCrudItemPedido(Pedido pedido) throws SQLException {
		updatePedidoAfterCrudItemPedido(pedido, false);
	}

	public void updatePedidoAfterCrudItemPedido(Pedido pedido, boolean removendo) throws SQLException {
		findItemPedidoList(pedido);
		pedido.removendoItem = removendo;
		//--
		if (LavenderePdaConfig.liberaComSenhaPedidoComSaldoVerbaExtrapolado && pedido.consumoVerbaSaldoLiberadoSenha && pedido.vlTotalPedidoOld > 0 && pedido.vlTotalPedidoOld != pedido.vlTotalPedido) {
			pedido.consumoVerbaSaldoLiberadoSenha = false;
			pedido.deveValidarConsumoVerbaPedido = false;
		}
		pedido.vlTotalPedidoOld = pedido.vlTotalPedido;
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			pedido.vlTotalTrocaPedido = ItemPedidoService.getInstance().getVlTotalItemsTroca(pedido, null);
		}
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && ValueUtil.isNotEmpty(pedido.cdGrupoRecalc)) {
			getItemPedidoService().atualizaPctDescontoCascataItens(pedido);
			pedido.cdGrupoRecalc = null;
		}
		//--
		update(pedido);
		//
		atualizaFlItemPendente(pedido);
		atualizaFlPendente(pedido);
		atualizaFlRestrito(pedido);
		pedido.flHouveAlteracao = ValueUtil.VALOR_SIM;
	}
	
	private void atualizaFlItemPendente(Pedido pedido) throws SQLException {
		if (pedido != null && LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			pedido.flItemPendente = StringUtil.getStringValue(isHasItemPedidoPendente(pedido));
			updateColumn(pedido.getRowKey(), "FLITEMPENDENTE", pedido.flItemPendente, Types.VARCHAR);
		}
	}
	
	public boolean isHasItemPedidoPendente(final Pedido pedido) {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.isPendente()) {
					return true;
				}
			}
		}
		return false;
 	}
	
	private void atualizaFlRestrito(Pedido pedido) {
		if (pedido != null && LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			pedido.flRestrito = StringUtil.getStringValue(isHasItemPedidoRestrito(pedido));
		}
	}
	
	private boolean isHasItemPedidoRestrito(final Pedido pedido) {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.isRestrito()) {
					return true;
				}
			}
		}
		return false;
 	}

	private void validateVerbaPorPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		if (!pedido.isIgnoraControleVerba()) {
			double vlTotalPedido = 0.0;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPed = (ItemPedido)pedido.itemPedidoList.items[i];
				if (!itemPed.equals(itemPedido)) {
					vlTotalPedido += itemPed.vlTotalItemPedido;
				}
			}
			vlTotalPedido += itemPedido.vlTotalItemPedido;
			vlTotalPedido = ValueUtil.round(vlTotalPedido);
			double newVlTotalPedido = vlTotalPedido - pedido.vlDesconto;
			double pctDesconto = 100 - (newVlTotalPedido * 100) / vlTotalPedido;
			double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba;
			if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
				double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
				pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
			}
			if (pctMaxDesconto != 0 && pctDesconto > pctMaxDesconto) {
				throw new ValidationException(Messages.VERBAPEDIDO_PCTMAX_EDICAO);
			}
		}
	}

	private void validateItemTroca(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		if (!pedido.isPedidoTroca()) {
			validadeLimiteTroca(pedido, itemPedido);
			validadeToleranciaTroca(pedido, itemPedido);
		}
	}

	public void validadeLimiteTroca(final Pedido pedido, final ItemPedido itemPedido) {
		if ((TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido)) || (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(itemPedido.flTipoItemPedido))) {
			double vlTrocaRecolher = 0.0;
			double vlTrocaEntregar = 0.0;
			int size = pedido.itemPedidoTrocaList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPed = (ItemPedido)pedido.itemPedidoTrocaList.items[i];
				if (!itemPed.equals(itemPedido) && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPed.flTipoItemPedido))) {
					vlTrocaRecolher += itemPed.vlTotalItemPedido;
				}
				if (!itemPed.equals(itemPedido) && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(itemPed.flTipoItemPedido))) {
					vlTrocaEntregar += itemPed.vlTotalItemPedido;
				}
			}
			//--
			if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido)) {
				vlTrocaRecolher += itemPedido.vlTotalItemPedido;
			} else {
				vlTrocaEntregar += itemPedido.vlTotalItemPedido;
			}
			double pctTrocaRec = 101.0;
			double pctTrocaEnt = 101.0;
			if (pedido.vlTotalPedido > 0) {
				pctTrocaRec = (vlTrocaRecolher / pedido.vlTotalPedido) * 100;
				pctTrocaEnt = (vlTrocaEntregar / pedido.vlTotalPedido) * 100;
			}
			if ((LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) && ((pctTrocaRec > LavenderePdaConfig.percentualLimiteTrocaNoPedido) || (pctTrocaEnt > LavenderePdaConfig.percentualLimiteTrocaNoPedido))) {
				throw new ValidationException(Messages.TROCA_RECOLHER_LIMITE_EXTRAPOLADO);
			}
		} else if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(itemPedido.flTipoItemPedido)) {
			double vlTotalPedido = 0.0;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPed = (ItemPedido)pedido.itemPedidoList.items[i];
				if (!itemPed.equals(itemPedido)) {
					vlTotalPedido += itemPed.vlTotalItemPedido;
				}
			}
			vlTotalPedido += itemPedido.vlTotalItemPedido;
			double vlTotalTroca = pedido.vlTrocaRecolher;
			double pctTroca = (vlTotalTroca / vlTotalPedido) * 100;
			if ((LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) && (pctTroca > LavenderePdaConfig.percentualLimiteTrocaNoPedido)) {
				throw new ValidationException(Messages.TROCA_RECOLHER_ITEMPEDIDO_EDICAO);
			}
		}
	}

	public void validadeToleranciaTroca(final Pedido pedido, final ItemPedido itemPedido) {
		if (!LavenderePdaConfig.percentualToleranciaTrocaNoPedido.equals(ValueUtil.VALOR_NAO)) {
			double pctTolerancia = ValueUtil.getDoubleValue(LavenderePdaConfig.percentualToleranciaTrocaNoPedido);
			if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido)) {
				double vlTrocaRecolher = 0.0;
				int size = pedido.itemPedidoTrocaList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPed = (ItemPedido)pedido.itemPedidoTrocaList.items[i];
					if (!itemPed.equals(itemPedido) && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPed.flTipoItemPedido))) {
						vlTrocaRecolher += itemPed.vlTotalItemPedido;
					}
				}
				//--
				vlTrocaRecolher = vlTrocaRecolher + itemPedido.vlTotalItemPedido;
				double pctTroca = 10000;
				if (vlTrocaRecolher > 0) {
					pctTroca = ((ValueUtil.round(pedido.vlTrocaEntregar) - ValueUtil.round(vlTrocaRecolher)) / ValueUtil.round(vlTrocaRecolher)) * 100;
				}
				if (pctTroca > pctTolerancia) {
					throw new ValidationException(Messages.TROCA_RECOLHER_TOLERANCIA_EXTRAPOLADA);
				}
			}  else if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(itemPedido.flTipoItemPedido)) {
				double vlTrocaEntregar = 0.0;
				int size = pedido.itemPedidoTrocaList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPed = (ItemPedido) pedido.itemPedidoTrocaList.items[i];
					if (!itemPed.equals(itemPedido) && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(itemPed.flTipoItemPedido))) {
						vlTrocaEntregar += itemPed.vlTotalItemPedido;
					}
				}
				//--
				vlTrocaEntregar = vlTrocaEntregar + itemPedido.vlTotalItemPedido;
				double pctTroca = 10000;
				if (pedido.vlTrocaRecolher > 0) {
					pctTroca = ((ValueUtil.round(vlTrocaEntregar) - ValueUtil.round(pedido.vlTrocaRecolher)) / ValueUtil.round(pedido.vlTrocaRecolher)) * 100;
				}
				if (pctTroca > pctTolerancia) {
					throw new ValidationException(Messages.TROCA_RECOLHER_TOLERANCIA_EXTRAPOLADA);
				}
			}
		}
	}

	public void insertItemPedidoTroca(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		validateItemTroca(pedido, itemPedido);
		getItemPedidoService().insertItemSimples(itemPedido);
		//--
		pedido.itemPedidoTrocaList.addElement(itemPedido);
		//--
		calculateTrocaPedido(pedido);
		getCrudDao().update(pedido);
	}

	public void updateItemTrocaPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		validateItemTroca(pedido, itemPedido);
		getItemPedidoService().updateItemSimples(itemPedido);
		//--
		pedido.itemPedidoTrocaList.removeElement(itemPedido);
		pedido.itemPedidoTrocaList.addElement(itemPedido);
		//--
		calculateTrocaPedido(pedido);
		getCrudDao().update(pedido);
	}

	public void deleteItemTrocaPedido(final Pedido pedido, final ItemPedido itemPedido, boolean validateAndCalculate) throws SQLException {
		if (validateAndCalculate) {
		validateDeleteItemTroca(pedido, itemPedido);
		}
		getItemPedidoService().deleteItemSimples(itemPedido);
		FotoItemTrocaService.getInstance().deleteAllFotosItemTroca(itemPedido);
		//--
		pedido.itemPedidoTrocaList.removeElement(itemPedido);
		//--
		if (validateAndCalculate) {
		calculateTrocaPedido(pedido);
		getCrudDao().update(pedido);
	}
	}

	public void validateDeleteItemTroca(final Pedido pedido, final ItemPedido itemPedido) {
		if (!LavenderePdaConfig.percentualToleranciaTrocaNoPedido.equals(ValueUtil.VALOR_NAO)) {
			double pctTolerancia = ValueUtil.getDoubleValue(LavenderePdaConfig.percentualToleranciaTrocaNoPedido);
			if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido)) {
				if (pedido.vlTrocaEntregar == 0) {
					return;
				}
				double vlTrocaRecolher = 0.0;
				int size = pedido.itemPedidoTrocaList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPed = (ItemPedido)pedido.itemPedidoTrocaList.items[i];
					if (!itemPed.equals(itemPedido) && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPed.flTipoItemPedido))) {
						vlTrocaRecolher += itemPed.vlTotalItemPedido;
					}
				}
				//--
				double pctTroca = 10000;
				if (vlTrocaRecolher > 0) {
					pctTroca = ((pedido.vlTrocaEntregar - vlTrocaRecolher) / vlTrocaRecolher) * 100;
				}
				if ((LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) && (pctTroca > pctTolerancia)) {
					throw new ValidationException(Messages.TROCA_RECOLHER_TOLERANCIA_EXTRAPOLADA);
				}
			}
		}
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(itemPedido.flTipoItemPedido)) {
			double vlTotalPedido = 0.0;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPed = (ItemPedido)pedido.itemPedidoList.items[i];
				if (!itemPed.equals(itemPedido)) {
					vlTotalPedido += itemPed.vlTotalItemPedido;
				}
			}
			if ((vlTotalPedido == 0) && (pedido.vlTrocaRecolher == 0) && (pedido.vlTrocaEntregar == 0)) {
				return;
			}
			//--
			double pctTrocaRec = 101.0;
			double pctTrocaEnt = 101.0;
			if (vlTotalPedido > 0) {
				pctTrocaRec = (pedido.vlTrocaRecolher / vlTotalPedido) * 100;
				pctTrocaEnt = (pedido.vlTrocaEntregar / vlTotalPedido) * 100;
			}
			if ((LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) && ((pctTrocaRec > LavenderePdaConfig.percentualLimiteTrocaNoPedido) || (pctTrocaEnt > LavenderePdaConfig.percentualLimiteTrocaNoPedido))) {
				throw new ValidationException(Messages.TROCA_RECOLHER_ITEMPEDIDO_EXCLUSAO);
			}
		}
	}

	public void insertItemPedidoBonificacao(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().setValoresUnidadeMedida(itemPedido);
		getItemPedidoService().insertItemSimples(itemPedido);
		pedido.itemPedidoList.addElement(itemPedido);
		//--
		if ((itemPedido.vlVerbaItem != 0) && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			if (LavenderePdaConfig.usaPedidoBonificacao() && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao) {
				if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
					VerbaFornecedorService.getInstance().insertVlSaldo(itemPedido);
				} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
					VerbaUsuarioService.getInstance().insertVlSaldo(itemPedido);
				} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) && LavenderePdaConfig.usaPedidoBonificacaoConsomeVerbaGrupoProduto()) {
					VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, false);
			} else if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente) {
				VerbaClienteService.getInstance().insertVlSaldo(itemPedido, pedido.cdCliente);
				} else {
					VerbaSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, false);
			}
		}
			itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
			ItemKitPedidoService.getInstance().insert(itemPedido.itemKitPedidoList, itemPedido.nuSeqProduto, pedido, 1d);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.isBonificacaoAutomatica && !itemPedido.isItemBrinde && !itemPedido.isKitTipo3()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, true);
		}
		
		Produto produto = itemPedido.getProduto();
		if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos && ValueUtil.isEmpty(produto.cdMarcadores)) {
			produto.cdMarcadores = MarcadorProdutoService.getInstance().findMarcadoresByProduto(produto, pedido.cdCliente);
		}
		
		//--
		calculate(pedido);
		getCrudDao().update(pedido);
	}

	public void updateItemPedidoBonificacao(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().updateItemSimples(itemPedido);
		//--
		pedido.itemPedidoList.removeElement(itemPedido);
		pedido.itemPedidoList.addElement(itemPedido);
		//--
		if (itemPedido.vlVerbaItem != 0) {
			if (LavenderePdaConfig.usaPedidoBonificacao() && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao) {
				if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
					VerbaFornecedorService.getInstance().updateVlSaldo(itemPedido);
				} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
					VerbaUsuarioService.getInstance().updateVlSaldo(itemPedido);
				} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) && LavenderePdaConfig.usaPedidoBonificacaoConsomeVerbaGrupoProduto()) {
					VerbaGrupoSaldoService.getInstance().updateVlSaldo(pedido, itemPedido, false);
				} else if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente) {
					VerbaClienteService.getInstance().updateVlSaldo(itemPedido);
				} else {
					VerbaSaldoService.getInstance().updateVlSaldo(itemPedido);
				}
			}
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
			ItemKitPedidoService.getInstance().update(itemPedido.itemKitPedidoList, 1d);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false);
		}
		//--
		calculate(pedido);
		getCrudDao().update(pedido);
	}

	public void deleteItemPedidoBonificacao(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().deleteItemSimples(itemPedido);
		//--
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			itemPedido.dtEmissaoPedido = pedido.dtEmissao;
		}
		if (!LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
				VerbaFornecedorService.getInstance().deleteVlSaldo(itemPedido);
			} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
				VerbaUsuarioService.getInstance().deleteVlSaldo(itemPedido);
			} else if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente) {
				VerbaClienteService.getInstance().deleteVlSaldo(itemPedido);
			} else if (LavenderePdaConfig.usaPedidoBonificacao() && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao) {
				VerbaSaldoService.getInstance().deleteVlSaldo(itemPedido);
			} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) && LavenderePdaConfig.usaPedidoBonificacaoConsomeVerbaGrupoProduto()) {
				VerbaGrupoSaldoService.getInstance().deleteVlSaldo(pedido, itemPedido, false);
			}
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido.getProduto().isKit()) {
			ItemKitPedidoService.getInstance().delete(itemPedido.itemKitPedidoList);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false);
		}
		//--
		pedido.itemPedidoList.removeElement(itemPedido);
		//--
		calculate(pedido);
		getCrudDao().update(pedido);
	}

	public Pedido findUltimoPedidoByCliente(String cdCliente) throws SQLException {
		Pedido pedidoFilter = new Pedido();
    	pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
        pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
        pedidoFilter.cdCliente = cdCliente;
		return PedidoPdbxDao.getInstance().findUltimoPedidoByExample(pedidoFilter);
	}

	public Pedido findUltimoPedidoNaoAbertoByCliente(String cdCliente) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.cdCliente = cdCliente;
		return PedidoPdbxDao.getInstance().findUltimoPedidoNaoAbertoByExample(pedidoFilter);
	}

	public void setTabPrecoUltimoPedidoNoPedidoAtual(Object[] tabelaPrecoList, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaSugestaoTabPrecoECondPagto && (tabelaPrecoList != null)) {
			Pedido lastPedido = findUltimoPedidoByCliente(pedido.cdCliente);
			if (lastPedido != null) {
				TabelaPreco tabPreco = new TabelaPreco();
				tabPreco.cdEmpresa = SessionLavenderePda.cdEmpresa;
				tabPreco.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TabelaPreco.class);
				tabPreco.cdTabelaPreco = lastPedido.cdTabelaPreco;
				//--
				Vector listTabPreco = new Vector(tabelaPrecoList);
				if (!ValueUtil.isEmpty(lastPedido.cdTabelaPreco) && (listTabPreco.indexOf(tabPreco) != -1)) {
					pedido.cdTabelaPreco = lastPedido.cdTabelaPreco;
				}
				listTabPreco = null;
			}
		}
	}

	public void setCondPagtoUltimoPedidoNoPedidoAtual(Object[] listCondicao, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaSugestaoTabPrecoECondPagto) {
			Pedido lastPedido = findUltimoPedidoByCliente(pedido.cdCliente);
			if (lastPedido != null) {
				CondicaoPagamento condPagto = new CondicaoPagamento();
				condPagto.cdEmpresa = SessionLavenderePda.cdEmpresa;
				condPagto.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
				condPagto.cdCondicaoPagamento = lastPedido.cdCondicaoPagamento;
				//--
				Vector listCondicaoPagamento = new Vector(listCondicao);
				if (!ValueUtil.isEmpty(lastPedido.cdCondicaoPagamento) && (listCondicaoPagamento.indexOf(condPagto) != -1)) {
					pedido.cdCondicaoPagamento = lastPedido.cdCondicaoPagamento;
				}
				listCondicaoPagamento = null;
			}
		}
	}
	
	private void fecharItensPedido(final Pedido pedido) throws SQLException {
		atualizaFlVendidoQtMinima(pedido);
		atualizaItemKitPedido(pedido, BaseDomain.FLTIPOALTERACAO_ALTERADO);
		atualizaItensVendidosNoMes(pedido, false);
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
		geraVerbaSaldoVigenciaItens(pedido);
	}
	}
	
	private void reabrirItensPedido(final Pedido pedido) throws SQLException {
		atualizaItemKitPedido(pedido, BaseDomain.FLTIPOALTERACAO_ORIGINAL);
		atualizaItensVendidosNoMes(pedido, true);
		atualizaFlagItensPendentes(pedido);
		deletaVerbaGrupoSaldoConsumidaPersonalizada(pedido);
		atualizaComissaoItensPedido(pedido);
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ItemPedidoService.getInstance().limpaFlAutorizadoItem(pedido);
	}
	}
	
	public void atualizaComissaoItensPedido(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			ItemPedidoService.getInstance().calculateComissaoItem(itemPedido, pedido);
			ItemPedidoService.getInstance().updateVlPctComissao(itemPedido);
		}
	}
	
	private void atualizaFlagItensPendentes(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && pedido.isPedidoItemPendente()) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.isPendente()) {
					ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "FLPENDENTE", ValueUtil.VALOR_NAO, Types.VARCHAR);
				}
			}
		}
	}
	
	private void geraVerbaSaldoVigenciaItens(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		double vlSaldoVerbaPositivo = 0d, vlSaldoVerba = 0d;
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlSaldoVerbaPositivo += itemPedido.vlVerbaItemPositivo;
			vlSaldoVerba += itemPedido.vlVerbaItem;
		}
		vlSaldoVerbaPositivo = ValueUtil.round(vlSaldoVerbaPositivo);
		vlSaldoVerba = ValueUtil.round(vlSaldoVerba);
		VerbaSaldoVigenciaService.getInstance().insertVlSaldo(vlSaldoVerbaPositivo, vlSaldoVerba, pedido);
	}
	
	private void atualizaFlVendidoQtMinima(final Pedido pedido) throws SQLException {
		if (!pedido.isPedidoBonificacao() && LavenderePdaConfig.quantidadeMinimaCaixasPedido != 0) {
			double qtItemFaturamentoPedido = pedido.getQtItensFaturamento();
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				itemPedido.flVendidoQtMinima = StringUtil.getStringValue((int)qtItemFaturamentoPedido == LavenderePdaConfig.quantidadeMinimaCaixasPedido);
				ItemPedidoPdbxDao.getInstance().update(itemPedido);
			}
		}
	}

	private void atualizaItemKitPedido(final Pedido pedido, String flTipoAlteracao) throws SQLException {
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() || LavenderePdaConfig.isAtualizaEstoqueComReservaCentralizada()) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (pedido.isPedidoReaberto) {
					reajustaEstoqueReservaOnChange(pedido.flSituacaoReservaEstReabrePedido, itemPedido);
				}
				if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
					int size2 = itemPedido.itemKitPedidoList.size();
					for (int j = 0; j < size2; j++) {
						ItemKitPedido itemKitPedido = (ItemKitPedido) itemPedido.itemKitPedidoList.items[j];
						itemKitPedido.flTipoAlteracao = flTipoAlteracao;
						ItemKitPedidoService.getInstance().update(itemKitPedido);
					}
				}
			}
		}
	}
	
	private void atualizaItensVendidosNoMes(final Pedido pedido, boolean reabrindoPedido) throws SQLException {
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente
				&& (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() || LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3())
					&& pedido != null && pedido.isPedidoEmitidoNoMesCorrente() && pedido.isPedidoVenda()) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
				ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(itemPedido.cdProduto);
					setFlVendidoProdutoTabPreco(produtoTabPreco, itemPedido, reabrindoPedido);
				} else {
					setFlVendidoProduto(itemPedido.getProduto(), itemPedido, reabrindoPedido);
					}
				}
			}
		}

	private void deletaVerbaGrupoSaldoConsumidaPersonalizada(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			VerbaService.getInstance().deleteSaldoVerbaGrupoSaldoPersonalizada(pedido);
	}
	}

	private void setaProdutoComoNaoVendido(ItemPedido itemPedidoCloneFilter, ProdutoTabPreco produtoTabPreco,
			Produto produto) throws SQLException {
		itemPedidoCloneFilter.nuPedido = null;
		Vector itemPedidoList = ItemPedidoService.getInstance().findAllByExample(itemPedidoCloneFilter);
		int countItensEncontrados = 0;
		boolean usaProdutoTabPreco = false;
		boolean usaProduto = false;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			usaProdutoTabPreco = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && produtoTabPreco != null && produtoTabPreco.cdEmpresa.equals(itemPedido.cdEmpresa) && produtoTabPreco.cdRepresentante.equals(itemPedido.cdRepresentante) && produtoTabPreco.cdProduto.equals(itemPedido.cdProduto);
			usaProduto = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && produto != null && produto.cdEmpresa.equals(itemPedido.cdEmpresa) && produto.cdRepresentante.equals(itemPedido.cdRepresentante) && produto.cdProduto.equals(itemPedido.cdProduto);
			if (usaProdutoTabPreco || usaProduto) {
				Pedido pedidoFilter = new Pedido();
				pedidoFilter.cdEmpresa = itemPedido.cdEmpresa;
				pedidoFilter.cdRepresentante = itemPedido.cdRepresentante;
				pedidoFilter.flOrigemPedido = itemPedido.flOrigemPedido;
				pedidoFilter.nuPedido = itemPedido.nuPedido;
				Pedido pedido = (Pedido) findByRowKey(pedidoFilter.getRowKey());
				if (pedido != null && !pedido.isPedidoAberto() && pedido.isPedidoEmitidoNoMesCorrente() && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca() && !pedido.isOportunidade() && !pedido.isPedidoComplementar()) {
					countItensEncontrados++;
				}
			}
		}
		if (countItensEncontrados <= 1) {
			if (usaProdutoTabPreco) {
			produtoTabPreco.flVendido = ValueUtil.VALOR_NAO;
			ProdutoTabPrecoService.getInstance().update(produtoTabPreco);
			} else if (usaProduto) {
				ProdutoService.getInstance().updateFlVendidoProduto(produto, ValueUtil.VALOR_NAO);
		}
	}
	}

	public void resetDadosItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && SolAutorizacaoService.getInstance().hasSolAutorizacaoItemPedido(itemPedido, null)
				&& SolAutorizacaoService.getInstance().isItemPedidoAutorizadoOuPendente(itemPedido,null)) return;

		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			itemPedido.getItemTabelaPreco().condComercialExcec = null;
		}
		getItemPedidoService().loadPoliticaComercial(itemPedido, pedido);
		getItemPedidoService().loadPoliticaComercialFaixa(itemPedido);
		loadValorBaseItemPedido(pedido, itemPedido);
		getItemPedidoService().loadDadosItemPedido(itemPedido, pedido);
	}
	
	public void loadValorBaseItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			itemPedido.cdUnidade = pedido.cdUnidade;
		}
		if (LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			loadValorBaseItemPedidoDecisaoCanal(pedido, itemPedido);
		} else {
			setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
			loadValorBaseItemPedidoNormal(pedido, itemPedido);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido, LavenderePdaConfig.nuArredondamentoRegraInterpolacaoUnit);
		}
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			if (ValueUtil.isEmpty(itemPedido.cdUnidade))
				throw new ValidationException(Messages.UNIDADE_PRODUTO_SEM_UNIDADE);
			itemPedido.vlItemPedido = ItemPedidoService.getInstance().realizaCalculaPrecoPorMetroQuadradoItemPedido(itemPedido);
		}
		if (LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
			itemPedido.vlIndiceVolume = ItemPedidoService.getInstance().realizaCalculoPrecoPorVolumeProduto(itemPedido);
		}
		if (itemPedido.isCombo()) {
			ComboService.getInstance().loadValorItemByItemCombo(pedido, itemPedido, itemPedido.getItemCombo());
	}
		if (itemPedido.isKitTipo3()) {
			KitService.getInstance().loadVlBaseByItemKit(pedido, itemPedido);
		}
	}
	
	private void loadValorBaseItemPedidoNormal(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		ItemPedidoService itemPedidoService = getItemPedidoService();
		itemPedidoService.aplicaValorUnidadeAlternativa(itemPedido);
		itemPedidoService.aplicaIndiceVolume(itemPedido);
		itemPedidoService.aplicaVariacaoPrecoProdutoRegiaoECategoriaDoCliente(itemPedido);
		itemPedidoService.aplicaIndiceFinanceiroCondComercial(itemPedido, pedido);
		itemPedidoService.applyRedutorOptanteSimples(itemPedido, pedido.getCliente());
		itemPedidoService.aplicaIndiceFinanceiroCondPagamentoPorCondComercial(itemPedido, pedido);
		itemPedidoService.aplicaDescontoAcrescimoIcmsCliente(itemPedido, pedido);
		boolean aplicouIndiceFinanCondPagtoProd = itemPedidoService.aplicaIndiceFinanceiroCondPagtoProd(itemPedido, pedido);
		if (LavenderePdaConfig.isAplicaMaiorDescontoNoItemPedido() && !itemPedido.possuiDescMaxProdCli()) {
			itemPedidoService.aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		}
		//--
		if (LavenderePdaConfig.usaInversaoIndiceFinanceiroClienteCondPagto) {
			aplicaIndiceCondPagtoDepoisCliente(pedido, itemPedido, itemPedidoService, aplicouIndiceFinanCondPagtoProd);
		} else {
			aplicaIndiceClienteDepoisCondPagto(pedido, itemPedido, itemPedidoService, aplicouIndiceFinanCondPagtoProd);
		}
		itemPedidoService.aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		itemPedidoService.getAndApplyIndiceFinanceiroLinhaProdutoNoItemPedido(itemPedido, pedido.cdCondicaoPagamento, pedido.getCliente());
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemPedidoService.aplicaPrecoItemComValoresAdicionaisEmbutidos(itemPedido, pedido);
		}
		itemPedidoService.aplicaDescontoNoProdutoPorGrupoDescPromocional(itemPedido);
		itemPedidoService.aplicaIndiceFinanceiroClienteGrupoProd(itemPedido);
		itemPedidoService.loadQtElementarAndVlElementarItemPedido(itemPedido);
		itemPedidoService.getAndApplyDescontoPromocionalItemTabelaPreco(itemPedido);
		itemPedidoService.aplicaDescontoMaximoPorCliente(itemPedido);
		if (!LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemPedidoService.aplicaPrecoItemComValoresAdicionaisEmbutidos(itemPedido, pedido);
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && pedido.getCliente().isFreteEmbutido()) {
			itemPedidoService.aplicaFreteEmbutido(pedido, itemPedido);
		}
		if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoMantendoVlItemPedido() && !LavenderePdaConfig.isAplicaDescontoCategoria()) {
			itemPedidoService.applyIndiceFinanceiroCondPagtosVlItemPedidoMantendoPreco(itemPedido, pedido);
		}
		itemPedidoService.aplicaIndiceFinanceiroSupRep(itemPedido);
		itemPedidoService.aplicaDescontoAcrescimoItemPedido(itemPedido);
		if (LavenderePdaConfig.pctMargemAgregada > 0) {
			itemPedido.pctMargemAgregada = LavenderePdaConfig.pctMargemAgregada;
			itemPedidoService.calculateAndApplyVlAgregadoSugerido(itemPedido);
		}
		itemPedidoService.loadValorNeutroItemPedidoAud(itemPedido);
		itemPedidoService.loadValorVerbaEmpresa(itemPedido);
	    itemPedidoService.applyRedutorOptanteSimplesAposCalculoValorItem(itemPedido, pedido.getCliente());
	    itemPedidoService.aplicaVlPctFrete(itemPedido);
	    itemPedidoService.aplicaMargemDesconto(itemPedido, pedido.getCliente());
	    itemPedidoService.aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(itemPedido, pedido);
	    itemPedidoService.aplicaIndiceFinanceiroGrupoProdTabPreco(itemPedido);
	    itemPedidoService.aplicaDescontoKitFechado(itemPedido);
	    itemPedidoService.aplicaDescontoProdutoRestrito(itemPedido);
	    if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido2ouN() && !pedido.cdCondicaoPagamentoChanged && !pedido.isTipoPedidoChanged) {
	    	itemPedidoService.limpaDescItemMaxDescProdCli(itemPedido);
	    }
		itemPedidoService.aplicaMultiplicacaoDivisaoAposAplicacaoIndices(itemPedido);
		itemPedidoService.aplicaFreteNoItemPedido(itemPedido);
	}
	
	private void aplicaIndiceClienteDepoisCondPagto(Pedido pedido, ItemPedido itemPedido, ItemPedidoService itemPedidoService, boolean aplicouIndiceFinanCondPagtoProd) throws SQLException {
		if (LavenderePdaConfig.aplicaApenasDescQtdOuIndiceCondPagto && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && !itemPedido.isAplicaIndiceCondPagtoPorDescQtdIgnorado) {
			return;
		}
		if (!LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedidoService.applyIndiceFinanceiroCliente(itemPedido, pedido);
		}
		itemPedidoService.applyIndiceFinanceiroPlataformaVendaCliFin(itemPedido, pedido);
		if (!aplicouIndiceFinanCondPagtoProd) {
			itemPedidoService.applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		}
	}

	private void aplicaIndiceCondPagtoDepoisCliente(Pedido pedido, ItemPedido itemPedido,ItemPedidoService itemPedidoService, boolean aplicouIndiceFinanCondPagtoProd) throws SQLException {
		if (!aplicouIndiceFinanCondPagtoProd) {
			itemPedidoService.applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		}
		itemPedidoService.loadPoliticaComercial(itemPedido, pedido);
		itemPedidoService.applyIndiceFinanceiroPlataformaVendaCliFin(itemPedido, pedido);
		if (!LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedidoService.applyIndiceFinanceiroCliente(itemPedido, pedido);
		}
	}
	
	public void aplicaDescontoPorCredito(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
		ProdutoCreditoDesc produtoCreditoDesconto = itemPedido.getProdutoCreditoDesc();
		itemPedido.vlBaseItemTabelaPreco = produtoCreditoDesconto.vlUnitario;
		itemPedido.vlBaseItemPedido = produtoCreditoDesconto.vlUnitario;
		itemPedido.vlItemPedido = produtoCreditoDesconto.vlUnitario;
		itemPedido.vlManualBrutoItem = produtoCreditoDesconto.vlUnitario;
		itemPedido.vlUnidadePadrao = ItemPedidoService.getInstance().getVlUnidadePadrao(produtoCreditoDesconto.vlUnitario, itemPedido);
		itemPedido.vlVerbaItem = 0;
		itemPedido.vlVerbaItemOld = 0;
		itemPedido.vlVerbaItemPositivo = 0;
		itemPedido.vlVerbaItemPositivoOld = 0;
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlPctAcrescimo = 0;
		itemPedido.vlDesconto = 0;
		loadValorBaseItemPedidoNormal(pedido, itemPedido);
		itemPedido.vlBaseFlex = itemPedido.vlBaseItemPedido;
		getItemPedidoService().loadDadosItemPedido(itemPedido, pedido);
	}
	
	private void loadValorBaseItemPedidoDecisaoCanal(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		itemPedido.vlItemPedido = itemPedido.vlDecisaoCanalFormulaA = 0d;
		ItemPedido.ignoreRoundDecisaoCanalCli = true;
		for (int i = 0; i < LavenderePdaConfig.qtdCalculoRecursivoContratoCli; i++) {
			setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
			loadValorBaseItemPedidoNormal(pedido, itemPedido);
			itemPedido.vlTotalItemPedido = itemPedido.vlItemPedido;
			getItemPedidoService().loadDadosItemPedido(itemPedido, pedido);
		}
		double newVlBase = CanalCliGrupoService.getInstance().calculaFormulaB(itemPedido);
		ItemPedido.ignoreRoundDecisaoCanalCli = false;
		if (itemPedido.vlDecisaoCanalFormulaA >= newVlBase) {
			newVlBase = itemPedido.vlDecisaoCanalFormulaA;
			itemPedido.flDecisaoCalculo = ItemPedido.CALCULO_DECISAO_CANAL_PRECO_FORMULA_A;
		} else {
			itemPedido.flDecisaoCalculo = ItemPedido.CALCULO_DECISAO_CANAL_PRECO_FORMULA_B;
		}
		itemPedido.vlBaseItemTabelaPreco = itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido = itemPedido.vlItemPedido = itemPedido.vlUnidadePadrao = newVlBase;
		loadValorBaseItemPedidoNormal(pedido, itemPedido);
	}
	
	private void setVlBaseItemPedidoFromItemTabelaPreco(ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
	}
	
	public boolean aplicarPctAcrescimoDoPedidoNosItens(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) return true; 
		
		int size = pedido.itemPedidoList.size();
		ItemPedido itemPedido = null;
		try {
			for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				if (itemPedido.isKitTipo3()) {
					continue;
				}
				itemPedido.vlPctAcrescimo = pedido.vlPctAcrescimoItem;
				if (LavenderePdaConfig.usaPoliticaComercial()) {
					ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
					itemPedido.vlPctAcrescimo = ItemPedidoService.getInstance().getVlPctAcrescimoPoliticaComercial(pedido.vlPctAcrescimoItem, itemPedido);
				}
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
				itemPedido.vlPctDesconto = 0;
				if (LavenderePdaConfig.usaPoliticaComercial()) {
					getItemPedidoService().loadPoliticaComercialFaixa(itemPedido);
				}
				getItemPedidoService().calculate(itemPedido, pedido);
				getItemPedidoService().validate(itemPedido);
			}
			validate(pedido);
			calculate(pedido);
		} catch (Throwable e) {
			itemPedido.vlPctAcrescimo = 0;
			getItemPedidoService().calculate(itemPedido, pedido);
			return false;
		}
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			getItemPedidoService().update(itemPedido);
		}
		update(pedido);
		return true;
	}
	
	public boolean aplicarDescPctDoPedidoNosItens(final Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) return true; 
			
			int size = pedido.itemPedidoList.size();
			boolean isPedidoUsaVerba = !pedido.isIgnoraControleVerba() && !pedido.isSimulaControleVerba();
			Vector oldItemPedidoList = getItemPedidoListTemp(pedido.itemPedidoList, size);
			double oldVlVerbaTotalPedido = 0d;
			try {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					if (itemPedido.isKitTipo3()) {
						continue;
					}
				if (LavenderePdaConfig.aplicaSomenteItemSemDesconto()
						&& (itemPedido.vlPctDesconto > 0 || itemPedido.vlPctDescPedido > 0
							|| itemPedido.vlPctDescontoPromo > 0 || itemPedido.vlDescontoPromo > 0)) continue;
					
				if (LavenderePdaConfig.isAcumulaComDescDoItem()) {
					itemPedido.vlPctDescPedido =  pedido.vlPctDescItem;
				} else {
					itemPedido.vlPctDesconto = pedido.vlPctDescItem;
				}
					//--
					if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
						ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
						if (itemTabelaPreco != null) {
							itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto);
							DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
						}
					}
					// Se o desconto do Pedido (pedido.vlPctDescItem) for maior que o máximo permitido pelo item (DescQtde ou MaxItemTabPreco) usa o máximo permitido.
					if (itemPedido.descQuantidade != null) {
						if (LavenderePdaConfig.usaDescontoPorQuantidadeValor && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_QNT_ITEM)) {
							if (itemPedido.descQuantidade.vlDesconto < itemPedido.vlPctDesconto) {
								itemPedido.vlPctDesconto = itemPedido.descQuantidade.vlDesconto;
							}
					} else if (itemPedido.descQuantidade.vlPctDesconto < itemPedido.vlPctDesconto) {
								itemPedido.vlPctDesconto = itemPedido.descQuantidade.vlPctDesconto;
							}
				} else if (!LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() && !LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
						double vlPctMaxDesconto = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
					if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
						vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
					} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
							vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
						}
					if (LavenderePdaConfig.usaPoliticaComercial()) {
						ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
						vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescontoPoliticaComercial(vlPctMaxDesconto, itemPedido);
					}
							if (vlPctMaxDesconto < itemPedido.vlPctDesconto) {
								itemPedido.vlPctDesconto = vlPctMaxDesconto;
							}
						}
				itemPedido.flTipoEdicao = itemPedido.vlPctAcrescimo > 0 && LavenderePdaConfig.isAcumulaComDescDoItem() ?  ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT : ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				oldVlVerbaTotalPedido += LavenderePdaConfig.usaInterpolacaoPrecoProduto && isPedidoUsaVerba ? itemPedido.vlVerbaItem + itemPedido.vlVerbaItemPositivo : 0d;
				if (!LavenderePdaConfig.isAcumulaComDescDoItem() || LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
					itemPedido.vlPctAcrescimo = 0;
					}
				if (LavenderePdaConfig.isAcumulaComDescDoItem() && (itemPedido.vlPctDescPedido + itemPedido.vlPctDesconto) >= 100) {
					throw new DescontoAcumuladoException(MessageUtil.getMessage(Messages.PEDIDO_MSG_DESCONTO_ACUMULA_ULTRAPASSADO, new String[] {StringUtil.getStringValueToInterface(itemPedido.vlPctDescPedido), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto + itemPedido.vlPctDescPedido)}));
				}
				if (LavenderePdaConfig.usaPoliticaComercial()) {
					getItemPedidoService().loadPoliticaComercialFaixa(itemPedido);
				}
					getItemPedidoService().calculate(itemPedido, pedido);
					itemPedido.ignoraValidacaoVerbaTemp = LavenderePdaConfig.usaInterpolacaoPrecoProduto;
					getItemPedidoService().validate(itemPedido);
					if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow()) {
						ItemPedidoService.getInstance().marcaItemPedidoPendente(itemPedido);
					}
				}
				validate(pedido);
				calculate(pedido);
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
					try {
						VerbaService.getInstance().validateSaldo(pedido, ValueUtil.getDoubleValueTruncated(oldVlVerbaTotalPedido, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba));
					} catch (ValidationException e) {
						pedido.itemPedidoList = oldItemPedidoList;
						throw new ValidationValorMinimoVerbaUltrapassadoException(e.getMessage(), e.params);
					}
				}
			} catch (ValidationValorMinimoVerbaUltrapassadoException e) {
				throw e;
		} catch (DescontoAcumuladoException e){
			throw e;
		} catch (Throwable e) {
				return false;
			}
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			getItemPedidoService().emTransacao = true;
			for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				getItemPedidoService().update(itemPedido);
			}
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			getItemPedidoService().emTransacao = false;
		}
			update(pedido);
		return true;
	}
	
	private Vector getItemPedidoListTemp(Vector itemPedidoList, int size) {
		Vector list = new Vector(size);
		for (int i = 0; i < size; i++) {
			list.addElement(((ItemPedido)itemPedidoList.items[i]).clone());
		}
		return list;
	}
	
	//-------------------------------------------------------------------------------
	// --
	//-------------------------------------------------------------------------------

	public void updatePedidosDiferencasLido() {
		((PedidoPdbxDao)getCrudDao()).updatePedidosDiferencasLido();
	}

	public int updateDtHrPedidosTramsmitidos(Pedido pedido) throws SQLException {
		return ((PedidoPdbxDao)getCrudDao()).updateDtHrPedidosTramsmitidos(pedido);
	}

	public void updatePedidoTramsmitidoComSucesso(String rowKey, String dsUrlTransmissao) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updatePedidoTramsmitidoComSucesso(rowKey, dsUrlTransmissao);
	}
	
	public void updatePedidoOrcamentoTransmitido(String rowKey, String dsUrlTransmissao) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updatePedidoOrcamentoTransmitido(rowKey, dsUrlTransmissao);
	}
	
	public void updatePedidoCanceladoTransmitido(String rowKey, String dsUrlTransmissao) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updatePedidoCanceladoTransmitido(rowKey, dsUrlTransmissao);
	}
	
	private void updatePedidoCancelado(Pedido pedido) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updatePedidoCancelado(pedido);
		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_C, StringUtil.getStringValue(pedido.vlTotalPedido));
	}
	
	public int updatePedidoProcessandoEnvio(Pedido pedido) throws SQLException {
		return ((PedidoPdbxDao)getCrudDao()).updatePedidoProcessandoEnvio(pedido);
	}

	public void updatePedidoProcessandoTransmissao(String rowKey) throws SQLException {
		updateColumn(rowKey, "FLTIPOALTERACAO", Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO, totalcross.sql.Types.VARCHAR);
	}
	
	public void updateDataEntregaPedido(String rowKey, Date dataEntrega) throws SQLException {
		updateColumn(rowKey, "DTENTREGA", dataEntrega, totalcross.sql.Types.DATE);
	}

	public void updateFlPossuiDiferenca(String cdEmpresa, String cdRepresentante, String nuPedido) {
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = cdEmpresa;
		pedido.cdRepresentante = cdRepresentante;
		pedido.nuPedido = nuPedido;
		pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		try {
			updateColumn(pedido.getRowKey(), "FLPOSSUIDIFERENCA", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		} catch (Throwable e) {
			// Apenas nao atualiza o pedido, pois não o encontrou (pode ter sido limpo já pelas rotinas automaticas)
		}
	}
	
	public void updatePedidoOrcamento(Pedido pedido) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updatePedidoOrcamento(pedido);
	}

	public void validateBateria() {
		if ((LavenderePdaConfig.controleNivelCriticoBateria > 0) && (Vm.getRemainingBattery() < LavenderePdaConfig.controleNivelCriticoBateria)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.BATERIA_FRACA, Vm.getRemainingBattery()));
		}
	}
	
	public Vector getFornecedoresNaoPositivadosList(Pedido pedido) throws SQLException {
		Vector fornecedorList = new Vector();
		String[] listForn = StringUtil.split(LavenderePdaConfig.avisaUsuarioPositivacaoFornecedor, ';');
		int size = pedido.itemPedidoList.size();
		if (size > 0) {
			for (int i = 0; i < listForn.length; i++) {
				boolean encontrouItem = false;
				for (int j = 0; j < size; j++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[j];
					if (itemPedido != null) {
						if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdFornecedor) && itemPedido.getProduto().cdFornecedor.equals(listForn[i])) {
							encontrouItem = true;
							break;
						}
					}
				}
				if (!encontrouItem) {
					Fornecedor fornecedor = new Fornecedor();
					fornecedor.cdEmpresa = SessionLavenderePda.cdEmpresa;
					fornecedor.cdFornecedor = listForn[i];
					fornecedor = (Fornecedor) FornecedorService.getInstance().findByRowKey(fornecedor.getRowKey());
					if (fornecedor != null) {
						fornecedorList.addElement(fornecedor);
					}
				}
			}
		}
		return fornecedorList;
	}

	public double getVlTotalDescontandoItensNeutros(Pedido pedido) throws SQLException {
		double vlTotal = pedido.vlTotalPedido;
		int size = pedido.itemPedidoList.size();
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.getProduto().isNeutro()) continue;
			vlTotal -= itemPedido.vlTotalItemPedido;
		}
		return vlTotal;
	}

	public int getQtItensDescontandoItensNeutros(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		int qtItens = size;
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.getProduto().isNeutro()) continue;
			qtItens -= 1;
		}
		return qtItens;
	}

	public void recalculateComissaoPedido(final Pedido pedido) throws SQLException {
		calculaComissaoItensPedido(pedido);
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItemTipo1()) {
			calculaDescontaComissaoRentabilidadePorItemTipo1(pedido);
		} else if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItemTipo2() && (pedido.getTipoPedido() == null || !pedido.getTipoPedido().isIgnoraCalculoComissao())) {
			calculaDescontaComissaoRentabilidadePorItemTipo2(pedido);
		} else if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			calculaMostraFaixaComissaoPedidoEItem(pedido);
		} else {
			calculaComissaoPedido(pedido);
		}
	}

	public void calculaDescontaComissaoRentabilidadePorItemTipo1(Pedido pedido) throws SQLException {
		pedido.vlComissaoPedido = getVlComissaoPedido(pedido.itemPedidoList, false);
			pedido.vlPctComissao = 0;
		double vlTotalDescontandoItensNeutros = getVlTotalDescontandoItensNeutros(pedido);
		if (vlTotalDescontandoItensNeutros > 0 && pedido.vlComissaoPedido > 0) {
			pedido.vlPctComissaoPedido = ValueUtil.round((pedido.vlComissaoPedido / vlTotalDescontandoItensNeutros) * 100);
			}
		if (pedido.vlTotalPedido > 0) {
			double vlComissaoTotal = getVlComissaoPedido(pedido.itemPedidoList, true);
			if (vlComissaoTotal > 0) {
				pedido.vlPctComissaoTotal = ValueUtil.round((vlComissaoTotal / pedido.vlTotalPedido) * 100);
			}
		}
	}

	public void calculaDescontaComissaoRentabilidadePorItemTipo2(Pedido pedido) throws SQLException {
			pedido.vlComissaoPedido = 0;
		double vlTotalDescontandoItensNeutros = getVlTotalDescontandoItensNeutros(pedido);
		if (!pedido.isPedidoAberto()) {
			pedido.vlComissaoPedido = ValueUtil.round(vlTotalDescontandoItensNeutros * pedido.vlPctComissao / 100);
			return;
		}
		ComiRentabilidade comiRentabilidadeAtingida = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(pedido.cdEmpresa, pedido.cdRepresentante, pedido.getVlPctRentabilidade(false));
			pedido.vlPctComissao = comiRentabilidadeAtingida.vlPctComissao;
		pedido.vlComissaoPedido = ValueUtil.round(vlTotalDescontandoItensNeutros * pedido.vlPctComissao / 100);
		ComiRentabilidade comiRentabilidadeAtingidaTotal = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(pedido.cdEmpresa, pedido.cdRepresentante, pedido.getVlPctRentabilidade(true));
		pedido.vlPctComissaoTotal = comiRentabilidadeAtingidaTotal.vlPctComissao;
		pedido.vlComissaoPedidoTotal = ValueUtil.round(pedido.vlTotalPedido * pedido.vlPctComissaoTotal / 100);
	}

	public void calculaMostraFaixaComissaoPedidoEItem(Pedido pedido) throws SQLException {
		double vlPctComissao = getVlPctDescComissaoItens(pedido.itemPedidoList, false);
		int qtItensDescontandoItensNeutros = getQtItensDescontandoItensNeutros(pedido);
		if (vlPctComissao > 0 && qtItensDescontandoItensNeutros > 0) {
			pedido.vlPctComissaoPedido = ValueUtil.round(vlPctComissao / qtItensDescontandoItensNeutros);
			} else {
				pedido.vlPctComissaoPedido = 0;
			}
		pedido.vlComissaoPedido = getVlComissaoPedido(pedido.itemPedidoList, false);
			pedido.vlPctComissao = pedido.vlPctComissaoPedido;

		int qtTotalItens = pedido.itemPedidoList.size();
		double vlPctTotalComissao = getVlPctDescComissaoItens(pedido.itemPedidoList, true);
		pedido.vlComissaoPedidoTotal = getVlComissaoPedido(pedido.itemPedidoList, true);
		pedido.vlPctComissaoTotal = 0;
		if (vlPctTotalComissao > 0 && qtTotalItens > 0) {
			pedido.vlPctComissaoTotal = ValueUtil.round(vlPctTotalComissao / qtTotalItens);
		}
	}

	public void calculaComissaoPedido(Pedido pedido) throws SQLException {
		pedido.vlComissaoPedido = getVlComissaoPedido(pedido.itemPedidoList, false);
		double vlTotalDescontandoItensNeutros = getVlTotalDescontandoItensNeutros(pedido);
		if (vlTotalDescontandoItensNeutros > 0 && pedido.vlComissaoPedido > 0) {
			pedido.vlPctComissaoPedido = ValueUtil.round((pedido.vlComissaoPedido / vlTotalDescontandoItensNeutros) * 100);
		}
		pedido.vlComissaoPedidoTotal = getVlComissaoPedido(pedido.itemPedidoList, true);
		if (pedido.vlTotalPedido > 0 && pedido.vlComissaoPedidoTotal > 0) {
			pedido.vlPctComissaoTotal = ValueUtil.round((pedido.vlComissaoPedidoTotal / pedido.vlTotalPedido) * 100);
		} else {
			pedido.vlPctComissaoTotal = 0;
			}
		}

	private double getVlPctDescComissaoItens(Vector itemPedidoList, boolean ignoreNeutro) throws SQLException {
		double vlPctTotal = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!ignoreNeutro && itemPedido.getProduto().isNeutro()) continue;
			vlPctTotal += itemPedido.vlPctComissao;
		}
		return vlPctTotal;
	}
	
	private double getVlComissaoPedido(Vector itemPedidoList, boolean ignoreProdutoNeutro) throws SQLException {
		double vlComissao = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!ignoreProdutoNeutro && itemPedido.getProduto().isNeutro()) {
				itemPedido.vlPctComissao = itemPedido.vlTotalComissao = 0;
				continue;
		}
			vlComissao += itemPedido.vlTotalComissao;
		}
		return vlComissao;
	}

	private void calculaComissaoItensPedido(final Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getProduto().isNeutro()) {
				itemPedido.vlPctComissao = itemPedido.vlTotalComissao = 0;
				continue;
			}
			if (pedido.isPedidoAberto() || itemPedido.vlTotalComissao == 0) {
				itemPedido.vlTotalComissao = (itemPedido.vlTotalItemPedido * itemPedido.vlPctComissao) / 100;
			}
			if (!LavenderePdaConfig.isNuCasasDecimaisComissaoLigado) {
				itemPedido.vlTotalComissao = ValueUtil.round(itemPedido.vlTotalComissao);
			} else {
				itemPedido.vlTotalComissao = ValueUtil.getDoubleValueTruncated(itemPedido.vlTotalComissao, LavenderePdaConfig.nuCasasDecimaisComissao);
			}
		}
	}

	public void addQtAdicionalKitTipo3(Pedido pedido, Vector itemKitList, int qtKit) throws SQLException {
		int size = itemKitList.size();
		List<ItemPedido> result = new ArrayList<>();
		Vector itemPedidoListOld = new Vector();
		itemPedidoListOld.addElementsNotNull(pedido.itemPedidoList.items);
		String cdProduto = "";
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			for (int i = 0; i < size; i++) {
				ItemKit itemKit = (ItemKit) itemKitList.items[i];
				cdProduto = itemKit.cdProduto;
				result.add(updateSingleItemPedidoFromItemKit3(pedido, qtKit, itemKit));
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			pedido.itemPedidoList = itemPedidoListOld;
			ItemKitService.getInstance().resetQtItemFisico(result);
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (e instanceof ValidationException) {
				String msgMotivo = Messages.ITEM_KIT_IDENTIFY_ERRO_ESTOQUE.equals(((ValidationException) e).extraParams) ? Messages.ITEM_KIT_ERRO_ESTOQUE_PRODUTO : Messages.MSG_MOTIVO;
				throw new ValidationException(e.getMessage() + " " + MessageUtil.getMessage(msgMotivo, ProdutoService.getInstance().getDsProduto(cdProduto)));
			}
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}

	public ItemPedido updateSingleItemPedidoFromItemKit3(Pedido pedido, int qtKit, ItemKit itemKit) throws SQLException {
		ItemPedido itemPedido = ItemPedidoService.getInstance().createNewItemPedido(pedido);
		itemPedido.cdProduto = itemKit.cdProduto;
		itemPedido.cdKit = itemKit.cdKit;
		if (itemKit.isBonificado()) {
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		}
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			itemPedido =  ItemPedidoService.getInstance().findByPrimaryKeyAndCdKit(itemPedido);
		} else {
			itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByPrimaryKey(itemPedido);	
		}
		itemPedido.pedido = pedido;
		itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + (itemKit.qtItemKit * qtKit));
		ItemPedidoService.getInstance().validateItemBloqueadoRestrito(pedido, itemPedido);
		validateEstoqueForKitTipo3(pedido, itemPedido);
		calculateItemPedido(pedido, itemPedido, false);
		if (LavenderePdaConfig.isPermiteItemBonificado() && itemPedido.isItemBonificacao()) {
			updateItemPedidoBonificacao(pedido, itemPedido);
		} else {
		updateItemPedido(pedido, itemPedido);
		}
		return itemPedido;
	}

	public void addItemKitTipo3(Pedido pedido, Vector itemKitList, int qtKit) throws SQLException {
		int size = itemKitList.size();
		Vector result = new Vector(size);
		String cdProduto = "";
		Vector itemPedidoListOld = new Vector();
		itemPedidoListOld.addElementsNotNull(pedido.itemPedidoList.items);
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
		for (int i = 0; i < size; i++) {
				ItemKit itemKit = (ItemKit) itemKitList.items[i];
				cdProduto = itemKit.cdProduto;
				ItemPedido itemPedido = createSingleItemPedidoFromItemKit3(pedido, qtKit, itemKit);
				result.addElement(itemPedido);
			}
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) result.items[i];
				cdProduto = itemPedido.cdProduto;
				validateEstoqueForKitTipo3(pedido, itemPedido);
				atualizaNuSeqProduto(itemPedido);
				itemPedido.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
				if (LavenderePdaConfig.isPermiteBonificarProduto() && itemPedido.isItemBonificacao()) {
					insertItemPedidoBonificacao(pedido, itemPedido);
				} else {
				insertItemPedido(pedido, itemPedido);
			}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			pedido.itemPedidoList = itemPedidoListOld;
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (e instanceof ValidationException) {
				String msgMotivo = Messages.ITEM_KIT_IDENTIFY_ERRO_ESTOQUE.equals(((ValidationException) e).extraParams) ? Messages.ITEM_KIT_ERRO_ESTOQUE_PRODUTO : Messages.MSG_MOTIVO;
				throw new ValidationException(e.getMessage() + " " + MessageUtil.getMessage(msgMotivo, ProdutoService.getInstance().getDsProduto(cdProduto)));
			}
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}

	public void validateEstoqueForKitTipo3(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
	}

	public ItemPedido createSingleItemPedidoFromItemKit3(Pedido pedido, int qtKit, ItemKit itemKit) throws SQLException {
		itemKit.qtItemKit = itemKit.qtItemKit * qtKit;
		ItemPedido itemPedido = ItemPedidoService.getInstance().itemKitToItemPedido(pedido, itemKit);
		ItemPedidoService.getInstance().validateDuplicated(itemPedido);
		ItemPedidoService.getInstance().validateItemBloqueadoRestrito(pedido, itemPedido);
		return itemPedido;
	}


	public void addItemKitList(Pedido pedido, Vector itemKitList, String cdTabelaPreco, int qtKit, Kit kit) throws SQLException {
		int size = itemKitList.size();
		Vector result = new Vector(size);
		for (int i = 0; i < size; i++) {
			String[] itemKitArray = (String[]) itemKitList.items[i];
			ItemKit itemKit = new ItemKit();
			int posicaoDesconto = 4;
			itemKit.cdKit = LavenderePdaConfig.isUsaKitProdutoFechado() ? kit.cdKit : null;
			itemKit.cdProduto = itemKitArray[1];
			itemKit.qtMinItem = qtKit > 0 ? ValueUtil.getDoubleValue(itemKitArray[3]) * qtKit : ValueUtil.getDoubleValue(itemKitArray[3]);
			itemKit.cdTabelaPrecoFilter = cdTabelaPreco;
			if(LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto()) {
				itemKit.cdUnidade = itemKitArray[4];
				posicaoDesconto = 5;
			}
			itemKit.vlPctDesconto = LavenderePdaConfig.isUsaKitProdutoFechado() ? ValueUtil.getDoubleValue(itemKitArray[posicaoDesconto]) : 0;
			itemKit.kit = kit;
			ItemPedido itemPedido = ItemPedidoService.getInstance().itemKitToItemPedido(pedido, itemKit);
			ItemPedidoService.getInstance().validateDuplicated(itemPedido);
			validateEstoqueParaItemKitFechado(pedido, itemPedido);
			result.addElement(itemPedido);
		}
		//--
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) result.items[i];
			atualizaNuSeqProduto(itemPedido);
			insertItemPedido(pedido, itemPedido);
		}
	}
	
	public void atualizaNuSeqProduto(ItemPedido itemPedido) throws SQLException {
		if (permiteIncluirMesmoProdutoNoPedido(itemPedido)) {
			if (LavenderePdaConfig.usaSequenciaInsercaoNuSeqProduto) {
				itemPedido.nuSeqProduto = itemPedido.nuSeqItemPedido;
			} else {
				String cdItemGrade1 = itemPedido.cdItemGrade1;
				String cdUnidade = itemPedido.cdUnidade;
				itemPedido.cdUnidade = null;
				itemPedido.cdItemGrade1 = LavenderePdaConfig.usaGradeProduto4() ? null : cdItemGrade1;
				itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedido, "NUSEQPRODUTO") + 1;
				itemPedido.cdItemGrade1 = cdItemGrade1;
				itemPedido.cdUnidade = cdUnidade;
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				if (itemPedido.getProduto().isKit()) {
					int size = itemPedido.itemKitPedidoList.size();
					for (int i = 0; i < size; i++) {
						ItemKitPedido itemKitPedido = (ItemKitPedido)itemPedido.itemKitPedidoList.items[i];
						itemKitPedido.nuSeqProduto = itemPedido.nuSeqProduto;
					}
				}
			}
		} else {
			ItemPedido itemPedidoExample = new ItemPedido();
			itemPedidoExample.cdEmpresa = itemPedido.cdEmpresa;
			itemPedidoExample.cdRepresentante = itemPedido.cdRepresentante;
			itemPedidoExample.flOrigemPedido = itemPedido.flOrigemPedido;
			itemPedidoExample.nuPedido = itemPedido.nuPedido;
			itemPedidoExample.flTipoItemPedido = itemPedido.flTipoItemPedido;
			itemPedidoExample.cdProduto = itemPedido.cdProduto;
			if (LavenderePdaConfig.isUsaGradeProduto1A4()) {
				itemPedidoExample.cdItemGrade1 = itemPedido.cdItemGrade1;
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
				int nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO");
				if (nuSeqProduto == 0) {
					itemPedidoExample.cdItemGrade1 = null;
					itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO") + 1;
				}
				int size = itemPedido.itemPedidoGradeList.size();
				for (int i = 0; i < size; i++) {
					ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade)itemPedido.itemPedidoGradeList.items[i];
					itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
				}
			} else if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
				itemPedidoExample.cdUnidade = itemPedido.cdUnidade;
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
				int nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO");
				if (nuSeqProduto == 0) {
					itemPedidoExample.cdUnidade = null;
					itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO") + 1;
				}
			} else if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && LavenderePdaConfig.permiteIncluirMesmoProdutoLoteDiferenteNoPedido) {
				itemPedidoExample.cdLoteProduto = itemPedido.cdLoteProduto;
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
				int nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO");
				if (nuSeqProduto == 0) {
					itemPedidoExample.cdLoteProduto = null;
					itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO") + 1;
				}
			} else {
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
			}
		}
	}

	private boolean permiteIncluirMesmoProdutoNoPedido(ItemPedido itemPedido) throws SQLException {
		return (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido || LavenderePdaConfig.usaGradeProduto4() ||
					(LavenderePdaConfig.isUsaKitProdutoFechado() && 
						(itemPedido.isFazParteKitFechado() || getItemPedidoService().countItemPedidoSemKit(itemPedido) == 0)))
							&& !LavenderePdaConfig.isUsaControleEstoquePorLoteProduto();
	}
	
	public void validateEstoqueParaItemKitFechado(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isFazParteKitFechado()) {
			try {
				EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
			} catch (Throwable ex) {
				throw new ValidationException(itemPedido.getProduto() + ": " + ex.getMessage());
			}
		}
	}

	public boolean hasPedidoToClienteToday() throws SQLException {
		Pedido pedido = new Pedido();
		pedido.dtEmissao = DateUtil.getCurrentDate();
		pedido.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		pedido.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
		pedido.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
		Vector list = PedidoService.getInstance().findAllByExampleSummary(pedido);
		return list.size() > 0;
	}

	public Vector findAllPedidosPDAAntigos() throws SQLException {
		Date dataLimite = DateUtil.getCurrentDate();
		dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaPedido - 1);
		Pedido pedido = new Pedido();
		pedido.dtEmissaoFinalFilter = dataLimite;
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedido);
		if (! ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.cdStatusPedidoCancelado)) {
			pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoCancelado;
			pedidoList = VectorUtil.concatVectors(pedidoList, PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedido));
	}
		return pedidoList;
	}
	
	public Vector findAllPedidosPDAPerdidosAntigos() throws SQLException {
		Date dataLimite = DateUtil.getCurrentDate();
		dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaPedidoPerdido - 1);
		Pedido pedido = new Pedido();
		pedido.dtEmissaoFinalFilter = dataLimite;
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoPerdido;
		return PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedido);
	}

	public Vector getItensListByTipo(final String tipoItem, final Pedido pedido) {
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItem)) {
			Vector listItemPedido = new Vector();
			listItemPedido.addElementsNotNull(pedido.itemPedidoList.items);
			return listItemPedido;
		} else if (TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItem)) {
			return pedido.itemPedidoOportunidadeList;
		} else {
			Vector itensList = pedido.itemPedidoTrocaList;
			Vector itemTrocaList = new Vector();
			int size = itensList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido item = (ItemPedido)itensList.items[i];
				if (tipoItem.equals(item.flTipoItemPedido)) {
					itemTrocaList.addElement(item);
				}
			}
			return itemTrocaList;
		}
	}

	public double getItensValuesList(final String tipoItem, final Pedido pedido) {
		double valueTotal = 0;
		Vector itensList;
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItem)) {
			return pedido.vlTotalItens;
		} else {
			itensList = pedido.itemPedidoTrocaList;
			int size = itensList.size();
			ItemPedido item;
			for (int i = 0; i < size; i++) {
				item = (ItemPedido)itensList.items[i];
				if (tipoItem.equals(item.flTipoItemPedido)) {
					valueTotal += item.vlTotalItemPedido;
				}
			}
			return valueTotal;
		}
	}

	/**
	 * Excluir todos os pedidos originais relacionados a pedidos retornados do ERP
	 *
	 * @throws SQLException 
	 */
	public void deletePedidosPdaByPedidosErp() throws SQLException {
		long tempoInicial = Vm.getTimeStamp();
		LogPdaService.getInstance().logSyncDebug(Messages.LOGPDA_MSG_SYNC_PEDIDOERP_START);
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidoOriginalByPedidoErp();
		int size = (pedidoList != null) ? pedidoList.size() : 0;
		LogPdaService.getInstance().logSyncDebug(MessageUtil.getMessage(Messages.LOGPDA_MSG_SYNC_PEDIDOERP_QTD_TOTAL, size));
		int countProcessados = 0;
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (LavenderePdaConfig.enviaPedidoEmOrcamentoParaSupervisor
					&& pedido.isPedidoAberto()) continue;
			if (ValueUtil.isNotEmpty(pedido.nuPedCompRelacionado)) {
				countProcessados = excluiPedidoComplementar(countProcessados, pedido);
			}
			countProcessados = excluiPedidoItensEDemaisRegras(countProcessados, pedido);
		}
		pedidoList = null;
		LogPdaService.getInstance().logSyncDebug(MessageUtil.getMessage(Messages.LOGPDA_MSG_SYNC_PEDIDOERP_PDA_EXCLUIDO, countProcessados));
		LogPdaService.getInstance().logSyncDebug(MessageUtil.getMessage(Messages.LOGPDA_MSG_SYNC_PEDIDOERP_TEMPO, (int)(Vm.getTimeStamp() - tempoInicial)));
	}

	private int excluiPedidoComplementar(int countProcessados, Pedido pedido) throws SQLException {
		String[] nuPedidosComplementares = pedido.nuPedCompRelacionado.split(";");
		for (String nuPedidoComplementar : nuPedidosComplementares) {
			Pedido pedidoComplementar = new Pedido();
			pedidoComplementar.cdEmpresa = pedido.cdEmpresa;
			pedidoComplementar.cdRepresentante = pedido.cdRepresentante;
			pedidoComplementar.flOrigemPedido = pedido.flOrigemPedido;
			pedidoComplementar.nuPedido = nuPedidoComplementar;
			pedidoComplementar = (Pedido) findByPrimaryKey(pedidoComplementar);
			if (pedidoComplementar == null) continue;
			countProcessados = excluiPedidoItensEDemaisRegras(countProcessados, pedidoComplementar);
		}
		return countProcessados;
	}

	private int excluiPedidoItensEDemaisRegras(int countProcessados, Pedido pedido) throws SQLException {
		try {
			//Procura o item original
			if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
				findItemPedidoList(pedido);
				int itemPedidoListSize = pedido.itemPedidoList.size();
				for (int j = 0; j < itemPedidoListSize; j++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[j];
					itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
					if (LavenderePdaConfig.geraVerbaPositiva) {
						itemPedido.vlVerbaItemPositivoOld = itemPedido.vlVerbaItemPositivo;
					}
				}
				pedido.isDeletePedidosPdaByPedidosErp = true;
			} else {
				findItemPedidoSummaryList(pedido);
			}
			if (LavenderePdaConfig.usaCriacaoPedidoErpCancelado && isExistsPedidoErpCancelado(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido)) {
				pedido.isDeletePedidosPdaByPedidosErp = true;
			}
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				pedido.ignoreSolAutorizacaoItemExcluido = true;
			}
			delete(pedido);
			countProcessados++;
		} catch (ApplicationException e) {
			// Não faz nada. Apneas não exclui, pois nao achou nenhum registro Pda
		}
		return countProcessados;
	}
	
	public Pedido insertItemPedidoByItemPedidoList(Pedido pedido, Vector itemPedidoList, int cdTipoSugestaoPedido) throws SQLException {
		//-- Insere os itens de Venda e Bonificação
		int sizeItens = itemPedidoList.size();
		pedido.countItensSuceso = 0;
		pedido.countItensErro = 0;
		ItemPedido itemPedido = new ItemPedido();
		pedido.itensErros = new Vector();
		for (int j = 0; j < sizeItens; j++) {
			try {
				itemPedido = (ItemPedido) itemPedidoList.items[j];
				//--
				if ((itemPedido.getProduto() == null) || ValueUtil.isEmpty(itemPedido.getProduto().cdProduto)) {
					throw new ValidationException("Produto ausente");
				}
				//--
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
				if ((itemTabelaPreco == null) || ValueUtil.isEmpty(itemTabelaPreco.cdProduto)) {
					throw new ValidationException("Preço ausente");
				}
				//--
				GrupoCliPermProdService.getInstance().validatePermissaoGrupoProduto(pedido, itemPedido);
				itemPedido.nuPedido = pedido.nuPedido;
				itemPedido.flOrigemPedido = pedido.flOrigemPedido;
				itemPedido.nuSeqItemPedido = j + 1;
				itemPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
				//--
				if (cdTipoSugestaoPedido == Pedido.SUGESTAO_PEDIDO_BASEADO_GIRO) {
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
					double vlItemPedidoFinal = itemPedido.vlItemPedido;
					itemPedido.cdUnidade = ValueUtil.isNotEmpty(itemPedido.getProduto().cdUnidade) ? itemPedido.getProduto().cdUnidade : ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
					if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade && isUnidadeBaseRestrita(itemPedido, pedido)) {
						if (!GiroProdutoService.getInstance().validateUnidadeItemPedidoByGiro(itemPedido, pedido)) {
							throw new ValidationException(Messages.PEDIDO_TODAS_UN_RESTRITAS);
						}
					} else {
						ItemPedidoService.getInstance().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
						itemPedido.vlItemPedido = vlItemPedidoFinal;
					}
					itemPedido.isItemPedidoSugestaoGiro = true;
					ItemPedidoService.getInstance().loadQtElementarAndVlElementarItemPedido(itemPedido);
				} else {
					ItemPedidoService.getInstance().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
				}
				//--
				if (LavenderePdaConfig.isUsaIndiceFinaceiroPorClienteOrCondicaoPagamento()) {
					boolean aplicouIndiceFinanCondPagtoProd = ItemPedidoService.getInstance().aplicaIndiceFinanceiroCondPagtoProd(itemPedido, pedido);
					if (LavenderePdaConfig.usaInversaoIndiceFinanceiroClienteCondPagto) {
						aplicaIndiceCondPagtoDepoisCliente(pedido, itemPedido, ItemPedidoService.getInstance(), aplicouIndiceFinanCondPagtoProd);
					} else {
						aplicaIndiceClienteDepoisCondPagto(pedido, itemPedido, ItemPedidoService.getInstance(), aplicouIndiceFinanCondPagtoProd);
					}
				}
				//--
				if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
					if (itemTabelaPreco != null) {
						itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto);
						DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
					}
				}
				incrementaNuSeqProduto(cdTipoSugestaoPedido, itemPedido);
				ItemPedidoService.getInstance().calculate(itemPedido, pedido);
				ClienteService.getInstance().setVlTotalPedClienteExcetoPedParam(pedido, SessionLavenderePda.getCliente());
				ClienteService.getInstance().setVlTotalPedClienteExcetoPedConsignadoParam(pedido, SessionLavenderePda.getCliente());
				itemPedido.isIgnoraMensagemEstoqueNegativo = cdTipoSugestaoPedido == Pedido.SUGESTAO_PEDIDO_BASEADO_GIRO;
				CadItemPedidoForm.validateItemPedidoUI(itemPedido, pedido);
				itemPedido.isIgnoraMensagemEstoqueNegativo = false;
				ItemPedidoService.getInstance().insert(itemPedido);
				pedido.itemPedidoList.addElement(itemPedido);
				//--
				pedido.countItensSuceso++;
			} catch (Throwable e) {
				pedido.countItensErro++;
				String message = e.getMessage();
				if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1)) {
					if (ValueUtil.isNotEmpty(itemPedido.itemPedidoGradeList)) {
						int size = itemPedido.itemPedidoGradeList.size();
						for (int i = 0; i < size; i++) {
							if (i > 0) {
								pedido.countItensErro++;
							}
							ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[i];
							ProdutoGrade produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeByItemPedidoGrade(itemPedidoGrade);
							produtoGrade = produtoGrade != null ? produtoGrade : new ProdutoGrade(); 
							String dsItemGrade1 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade1, itemPedidoGrade.cdItemGrade1);
							String dsItemGrade2 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade2, itemPedidoGrade.cdItemGrade2);
							String dsItemGrade3 = null;
							if (LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) {
								dsItemGrade3 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade3, itemPedidoGrade.cdItemGrade3);
							}
							ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, message, dsItemGrade1, dsItemGrade2, dsItemGrade3);
							addProdutoErro(pedido, e, message, produtoErro);
						}
					} else {
						String cdTipoItemGrade1 = ProdutoGradeService.getInstance().getCdTipoItemGrade1ByItemPedido(itemPedido);
						ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto,  message, ItemGradeService.getInstance().getDsItemGrade(cdTipoItemGrade1, itemPedido.cdItemGrade1), ValueUtil.VALOR_NI, ValueUtil.VALOR_NI);
						addProdutoErro(pedido, e, message, produtoErro);
					}
				} else {
					ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, message);
					addProdutoErro(pedido, e, message, produtoErro);
				}
			}
		}
		//--
		PedidoService.getInstance().findItemPedidoList(pedido);
		PedidoService.getInstance().update(pedido);
		pedido.isPedidoBaseadoGiro = false;
		pedido.bonificacaoLiberada = false; //Teve que ser setado para true antes da copia do pedido e agora é devolvido o false
		//--
		return pedido;
	}

	private void addProdutoErro(Pedido pedido, Throwable e, String message, ProdutoErro produtoErro) {
		if (e instanceof ValidationException) {
			String params = ((ValidationException)e).params;
			if (ValueUtil.isNotEmpty(params)) {
				StringBuffer strBfr = new StringBuffer();
				strBfr.append(message);
				strBfr.append(params);
				message = strBfr.toString();
			}
		}
		pedido.itensErros.addElement(produtoErro);
	}

	private void incrementaNuSeqProduto(int cdTipoSugestaoPedido, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isConfigGradeProduto() && cdTipoSugestaoPedido == Pedido.SUGESTAO_PEDIDO_BASEADO_GIRO) {
			ItemPedido itemPedidoExample = new ItemPedido();
			itemPedidoExample.cdEmpresa = itemPedido.cdEmpresa;
			itemPedidoExample.cdRepresentante = itemPedido.cdRepresentante;
			itemPedidoExample.flOrigemPedido = itemPedido.flOrigemPedido;
			itemPedidoExample.nuPedido = itemPedido.nuPedido;
			itemPedidoExample.flTipoItemPedido = itemPedido.flTipoItemPedido;
			itemPedidoExample.cdProduto = itemPedido.cdProduto;
			itemPedidoExample.cdItemGrade1 = itemPedido.cdItemGrade1;
			itemPedido.nuSeqProduto = 1;
			int nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO");
			if (nuSeqProduto == 0) {
				itemPedidoExample.cdItemGrade1 = null;
				itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedidoExample, "NUSEQPRODUTO") + 1;
			}
		}
	}

	public Vector findAllByExampleOnlyPda(Pedido pedido) throws SQLException {
		return PedidoPdbxDao.getInstance().findAllByExampleOnlyPda(pedido);
	}
	
	public Pedido createNewPedido(Cliente cliente) throws SQLException {
    	Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.dtEmissao = DateUtil.getCurrentDate();
		pedido.hrEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.dtEntrega = getDataPrevisaoEntrega(pedido, cliente);
		pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		pedido.dtTransmissaoPda = null;
		pedido.hrTransmissaoPda = "";
		pedido.flOrigemPedidoRelacionado = ValueUtil.VALOR_NI;
		pedido.cdTabelaPreco = cliente.cdTabelaPreco;
		if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && ((pedido.getTabelaPreco() == null) || ValueUtil.isEmpty(pedido.getTabelaPreco().cdTabelaPreco))) {
			return null;
		}
		pedido.nuVersaoSistemaOrigem = LavendereConfig.getInstance().version;
		//--
		TipoPedido tipoPedido = TipoPedidoService.getInstance().findTipoPedidoDefault(cliente);
		if (tipoPedido != null) {
			pedido.cdTipoPedido = tipoPedido.cdTipoPedido;
		}
		//--
		TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(cliente.cdTipoPagamento);
		if (tipoPagamento != null) {
			pedido.cdTipoPagamento = tipoPagamento.cdTipoPagamento;
		}
		//--
		if (cliente.isNovoCliente() && LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			ClienteEndereco clienteEnderecoEntregaPadrao = ClienteEnderecoService.getInstance().getClienteEnderecoPadrao(pedido);
			if (clienteEnderecoEntregaPadrao != null) {
				pedido.cdEnderecoCliente = clienteEnderecoEntregaPadrao.cdEndereco;
			}
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			ClienteEndereco clienteEnderecoCobrancaPadrao = ClienteEnderecoService.getInstance().getClienteEnderecoPadrao(pedido, true);
			if (clienteEnderecoCobrancaPadrao != null) {
				pedido.cdEnderecoCobranca = clienteEnderecoCobrancaPadrao.cdEndereco;
			}
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			Vector condicaoComercialList = CondicaoComercialService.getInstance().loadCondicaoComercialListForCombo(pedido);
			if (ValueUtil.isNotEmpty(condicaoComercialList)) {
				condicaoComercialList.qsort();
				int size = condicaoComercialList.size();
				for (int i = 0; i < size; i++) {
					CondicaoComercial condComercial = (CondicaoComercial) condicaoComercialList.items[i];
					if (condComercial.isFlDefault()) {
						pedido.cdCondicaoComercial = condComercial.cdCondicaoComercial;
						break;
					}
				}
				if (ValueUtil.isEmpty(pedido.cdCondicaoComercial)) {
				pedido.cdCondicaoComercial = ((CondicaoComercial) condicaoComercialList.items[0]).cdCondicaoComercial;
			}
		}
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && !pedido.isPedidoTroca()) {
			TipoFrete tipoFrete = TipoFreteService.getInstance().findTipoFreteDefaultParaNovoPedido(pedido);
			if (tipoFrete != null) {
				pedido.cdTipoFrete = tipoFrete.cdTipoFrete;
			}
		}
		CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(cliente.cdCondicaoPagamento);
		if (condicaoPagamento != null && condicaoPagamento.cdEmpresa != null) {
			pedido.cdCondicaoPagamento = condicaoPagamento.cdCondicaoPagamento;
		} else {
			condicaoPagamento = (CondicaoPagamento) CondicaoPagamentoService.getInstance().loadCondicoesPagamento(pedido).items[0];
			pedido.cdCondicaoPagamento = condicaoPagamento.cdCondicaoPagamento;
		}
		if (ValueUtil.isEmpty(pedido.cdTransportadora)) {
			pedido.cdTransportadora = cliente.cdTransportadora;
		}
		return pedido;
	}

	public void updateInfosPedidoAposEnvioServidor(Pedido pedido, String urlEnvioPedido) throws SQLException {
		try {
			ErroEnvioService.getInstance().removeErroEnvioPedido(pedido);
			if (LavenderePdaConfig.isUsaVerba()) {
				findItemPedidoList(pedido);
				if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente && pedido.isPedidoBonificacao()) {
					if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
						VerbaClienteService.getInstance().updateVerbaClienteGrupoProduto(pedido);
					} else {
					VerbaClienteService.getInstance().updateVerbaClienteItemPedido(pedido);
					}
				} else {
					VerbaService.getInstance().updateVerbaSaldoItemPedido(pedido);
					if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
						VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
					}
				}
			}
			if (LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
				MargemContribFaixaService.getInstance().updateVerbaSaldoPedido(pedido);
			}
			if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
				findItemPedidoList(pedido);
				RentabilidadeSaldoService.getInstance().updateRentabilidadeSaldoPda2Erp(pedido);
			}
			if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
				VerbaSaldoService.getInstance().updateVerbaSaldoForPedidosFechados(pedido);
			}
			if (LavenderePdaConfig.validaSaldoPedidoBonificacao && pedido.getTipoPedido() != null && pedido.getTipoPedido().isBonificacao()) {
				BonificacaoSaldoService.getInstance().updateBonificacaoSaldoForPedidosFechados(pedido);
			}
			if (LavenderePdaConfig.usaControleSaldoOportunidade) {
				LimiteOportunidadeService.getInstance().updateSaldoLimiteOportunidadeForPedidosEnviados(pedido);
			}
			if (atualizaEstoqueInterno(pedido) || pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
				EstoqueService.getInstance().updateEstoquePdaToERP(pedido);
			}
			if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
				LoteProdutoService.getInstance().updateEstoquePdaToERP(pedido);
			}
			if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
				ItemPedidoRemessaService.getInstance().updateEstoquePdaToERP(pedido, true);
			}
			if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
				NfeService.getInstance().atualizaNfeAposEnvioServidor();
				ItemNfeService.getInstance().atualizaItemNfeAposEnvioServidor();
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacao() && pedido.isPedidoIniciadoProcessoEnvio()) {
				ItemPedidoBonifCfgService.getInstance().updateItemPedidoBonifCfgEnviadoServidor(pedido);
			}
		} catch (Throwable e) {
			VmUtil.debug(MessageUtil.getMessage(Messages.SINCRONIZACAO_MSG_ERRO_ATALIZACAO_INFO_PEDIDO, pedido.nuPedido));	
		} finally {
			if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && pedido.cdStatusOrcamento != null && !pedido.isPedidoFechado()) {
				updatePedidoOrcamentoTransmitido(pedido.getRowKey(), urlEnvioPedido);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_ORCAMENTO_SS, StringUtil.getStringValue(pedido.vlTotalPedido));
			} else if (LavenderePdaConfig.usaDataBaseCancelamentoAutoPedidoCliente() && pedido.isPedidoCancelado() && hasMotivoCancelamentoPedidoAuto(pedido)) {
				updatePedidoCanceladoTransmitido(pedido.getRowKey(), urlEnvioPedido);
				LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_CANCELADO_SS, StringUtil.getStringValue(pedido.vlTotalPedido));
			} else {
				atualizaPedidoTransmitidoSucesso(pedido, urlEnvioPedido);
			}
		}
	}

	public void atualizaPedidoTransmitidoSucesso(Pedido pedido, String urlEnvioPedido) throws SQLException {
		updatePedidoTramsmitidoComSucesso(pedido.getRowKey(), urlEnvioPedido);
		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SA, LavenderePdaConfig.cdStatusPedidoTransmitido, StringUtil.getStringValue(pedido.vlTotalPedido));
	}

	private boolean atualizaEstoqueInterno(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.atualizarEstoqueInterno && !pedido.isPedidoCancelado() && pedido.getTipoPedido() != null && !pedido.isIgnoraControleEstoque() && pedido.getTipoPedido().isRevalidaEstoqueFechamento() && !ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEst);
	}

	public void updateFlImpressoPedido(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flImpresso = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLIMPRESSO", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		}
	}
	
	public void updateFlImpressoNfe(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flNfeImpressa = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLNFEIMPRESSA", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		}
	}
	
	public void updateFlImpressoNfeCont(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flNfeContImpressa = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLNFECONTIMPRESSA", ValueUtil.VALOR_SIM, Types.VARCHAR);
		}
	}
	
	public void updateFlImpressoBoleto(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flBoletoImpresso = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLBOLETOIMPRESSO", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		}
	}
	
	public void updateFlConsignacaoImpressa(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flConsignacaoImpressa = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLCONSIGNACAOIMPRESSA", ValueUtil.VALOR_SIM, Types.VARCHAR);
		}
	}

	public Vector findAllPedidosByFrequenciaSugestaoVenda(String cdRepresentante, String cdCliente, SugestaoVenda sugestaoVenda, boolean onDeletePedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = sugestaoVenda.cdEmpresa;
		pedidoFilter.cdRepresentante = cdRepresentante;
		pedidoFilter.cdCliente = cdCliente;
		pedidoFilter.dtEmissaoInicialFilter = DateUtil.getCurrentDate();
		if (SugestaoVenda.FLTIPOFREQUENCIA_SEMANAL.equals(sugestaoVenda.flTipoFrequencia)) {
			pedidoFilter.dtEmissaoInicialFilter = DateUtil.getFirstDayOfWeek(DateUtil.getCurrentDate());
		} else if (SugestaoVenda.FLTIPOFREQUENCIA_MENSAL.equals(sugestaoVenda.flTipoFrequencia)) {
			pedidoFilter.dtEmissaoInicialFilter = DateUtil.getFirstUtilDayOfMonth(DateUtil.getCurrentDate());
		}
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoAberto;
		return PedidoPdbxDao.getInstance().findAllPedidosByFrequenciaSugestaoVenda(pedidoFilter, onDeletePedido);
	}

	public int countPedidoConcluido(Vector pedidoList) {
		int count = 0;
		if (ValueUtil.isNotEmpty(pedidoList)) {
			for (int i = 0; i < pedidoList.size(); i++) {
				if (!((Pedido)pedidoList.items[i]).isPedidoAberto()) {
					count += 1;
				}
			}
		}
		return count;
	}

	public int countPedidosByStatus(boolean flTodasEmpresas, boolean desconsideraPedidoPendente, String cdStatusPedido) throws SQLException {
		Pedido pedidoExample = new Pedido();
		if (!flTodasEmpresas) {
			pedidoExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
		}
		pedidoExample.cdStatusPedido = cdStatusPedido;
		pedidoExample.desconsideraPedidoPendente = desconsideraPedidoPendente;
		return countByExample(pedidoExample);
	}

	public boolean isCountPedidosAbertosMaiorPermitido() throws SQLException {
		int qtdMaxPedidoAberto = LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos();
		return qtdMaxPedidoAberto >=0 && countPedidosByStatus(LavenderePdaConfig.usaVerbaUnificada, LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito(), LavenderePdaConfig.cdStatusPedidoAberto) > qtdMaxPedidoAberto;
	}
	
	public Vector findDistinctDtEmissaoUltimosPedidosNaoTransmitidos(String cdRepresentante) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = cdRepresentante;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		return PedidoPdbxDao.getInstance().findDatasUltimosPedidosNaoTransmitidosByExample(pedidoFilter);
	}

	public int countPedidosEmStatusPreOrcamento() throws SQLException {
		Pedido pedidoExample = new Pedido();
		pedidoExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoExample.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		pedidoExample.statusOrcamentoFilter = new StatusOrcamento();
		pedidoExample.statusOrcamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoExample.statusOrcamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(StatusOrcamento.class);
		pedidoExample.statusOrcamentoFilter.flStatusPreOrcamento = ValueUtil.VALOR_SIM;
		
		return countByExample(pedidoExample);
	}

	public double getVlTotalFreteItens(final Pedido pedido) {
		double vlTotalItensComFrete = 0;
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido item;
			for (int i = 0; i < size; i++) {
				item = (ItemPedido)pedido.itemPedidoList.items[i];
				if (!TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(item.flTipoItemPedido)) {
					vlTotalItensComFrete += item.getVlTotalFrete();
				}
			}
		}
		return vlTotalItensComFrete;
	}
	
	public double getVlTotalIcmsItens(final Pedido pedido) {
		double vlTotalItensComIcms = 0;
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido item;
			for (int i = 0; i < size; i++) {
				item = (ItemPedido)pedido.itemPedidoList.items[i];
				vlTotalItensComIcms += item.getVlTotalIcms();
			}
		}
		return vlTotalItensComIcms;
	}

	
	public Vector getOnlyPedidoDispLiberacao(Vector pedidoList) throws SQLException {
		Vector newPedidoList = new Vector();
		if (ValueUtil.isNotEmpty(pedidoList)) {
			boolean usuarioLiberaItensPendentes = SessionLavenderePda.isUsuarioLiberaItemPendente();
			Pedido pedido;
			for (int i = 0; i < pedidoList.size(); i++) {
				pedido = (Pedido) pedidoList.items[i];
				if (pedido.isPedidoPendente() && pedido.permiteLiberacaoPedidoBonificado() && UsuarioDescService.getInstance().isProximoUsuarioLiberarPedido(pedido) && (pedido.isPendente() || (pedido.isPedidoItemPendente() && usuarioLiberaItensPendentes))) {
					newPedidoList.addElement(pedido);
				}
			} 
		}
		return newPedidoList;
	}
	
	public Vector getOnlyPedidoItensPendentes(Vector pedidoList) throws SQLException {
		Vector newPedidoList = new Vector();
		if (ValueUtil.isNotEmpty(pedidoList)) {
			Pedido pedido;
			for (int i = 0; i < pedidoList.size(); i++) {
				pedido = (Pedido) pedidoList.items[i];
				if (pedido.isPedidoPendente() && !pedido.isPedidoBonificacao() && pedido.isPedidoItemPendente()) {
					newPedidoList.addElement(pedido);
				}
			} 
		}
		return newPedidoList;
	}
	
	public boolean hasPedidosPendentes() {
		try {
			return PedidoPdbxDao.getInstance().findCountPedidosAbertos() > 0;
		} catch (Exception e) {
			return false;
				}
			}
	
	public Vector findAllPedidosParaCalculoRealizadoMetasCliente(String cdEmpresa, String cdRepresentante, String cdCliente, Date dtInicialFilter, Date dtFinalFilter) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cdEmpresa;
		pedidoFilter.cdRepresentante = cdRepresentante;
		pedidoFilter.cdCliente = cdCliente;
		pedidoFilter.dtEmissaoInicialFilter = dtInicialFilter;
		pedidoFilter.dtEmissaoFinalFilter = dtFinalFilter;
		pedidoFilter.flFiltraPedidosFechadosTransmitidos = true;
		Vector pedidoList = PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
		int sizePedidoList = pedidoList.size();
		for (int i = 0; i < sizePedidoList; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			PedidoService.getInstance().findItemPedidoSummaryList(pedido);
		}
		return pedidoList;
	}
	
	public void validaPesoMaximoPedido(Pedido pedido) throws SQLException {
		if (pedido.itemPedidoList.size() > 0) {
			ItemPedido itemPedido = new ItemPedido();
			validaPesoMaximoPedido(pedido, itemPedido);
		}
	}
	
	public void validaPesoMaximoPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		double vlPesoTotalPedido = getVlPesoTotalPedido(pedido, itemPedido);
		validaPesoMaximoCondicaoPagamento(pedido, vlPesoTotalPedido);
		validaPesoMaximoEmpresa(pedido, vlPesoTotalPedido);
		validaPesoMaximoCargaPedido(pedido.cdCargaPedido, pedido.nuPedido, vlPesoTotalPedido);
	}

	public void validaPesoMaximoCargaPedido(String cdCargaPedido, String nuPedido, double vlPesoTotalPedido) throws SQLException {
		if (LavenderePdaConfig.isValidaPesoMaximoCargaPedido() && ValueUtil.isNotEmpty(cdCargaPedido)) {
			double vlTotalPedidosCarga = findQtPesoTotalPedidosCargaPedido(cdCargaPedido, nuPedido);
			vlTotalPedidosCarga += ValueUtil.round(vlPesoTotalPedido);
			if (ValueUtil.round(vlTotalPedidosCarga) > LavenderePdaConfig.qtdPesoMaximoCargaPedido) {
				Object [] params = {StringUtil.getStringValueToInterface(LavenderePdaConfig.qtdPesoMaximoCargaPedido), StringUtil.getStringValueToInterface(vlTotalPedidosCarga)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMAXIMO_CARGAPEDIDO_ATINGIDO, params));
			}
		}
	}

	public double findQtPesoTotalPedidosCargaPedido(String cdCargaPedido, String nuPedido) throws SQLException {
		if (!LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido() && ValueUtil.isEmpty(cdCargaPedido)) {
			return 0;
		}
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante; 
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA; 
		pedidoFilter.cdCargaPedido = cdCargaPedido; 
		pedidoFilter.nuPedido = nuPedido; 
		if (ValueUtil.isNotEmpty(nuPedido)) {
			pedidoFilter.flFiltraPedidosDif = true; 
		}
		return PedidoPdbxDao.getInstance().sumByExample(pedidoFilter, "qtPeso");
	}
	
	public void validaFuncionalidadesReplicacaoPedido(Pedido pedido) throws SQLException {
		if (pedido == null) return; 
		
		if (ValueUtil.isNotEmpty(pedido.cdSegmento)) {
			if (!SegmentoService.getInstance().isSegmentoPedidoValido(pedido, pedido.getCliente())) {
				throw new ValidationException(Messages.PEDIDO_MSG_SEGMENTO_INVALIDO); 
			}
		}
		if (ValueUtil.isNotEmpty(pedido.cdTabelaPreco)) {
			if (!LavenderePdaConfig.usaTabelaPrecoPorCanalAtendimento && !TabelaPrecoService.getInstance().isTabelaPrecoPedidoValida(pedido)) {
				throw new ValidationException(Messages.TABELAPRECO_MSG_TABELA_PRECO_INVALIDA);
			}
		}
		if (ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			if (!CondicaoPagamentoService.getInstance().isCondicaoPagamentoPedidoValida(pedido)) {
				throw new ValidationException(Messages.CONDICAOPAGAMENTO_MSG_CONDICAO_INVALIDA);
			}
		}
		if (ValueUtil.isNotEmpty(pedido.cdCondicaoComercial)) {
			if (!CondicaoComercialService.getInstance().isCondicaoComercialPedidoValida(pedido)) {
				throw new ValidationException(Messages.PEDIDO_MSG_CONDCOMERCIAL_INVALIDA);
			}
		}
	}

	private double getVlPesoTotalPedido(Pedido pedido, ItemPedido itemPedido) {
		double vlPesoTotalPedido = itemPedido.qtPeso + pedido.qtPeso;
		int size = pedido.itemPedidoList.size();
		if (itemPedido != null) {
			for (int i = 0; i < size; i++) {
				if (itemPedido.equals(pedido.itemPedidoList.items[i])) {
					vlPesoTotalPedido -= ((ItemPedido)pedido.itemPedidoList.items[i]).qtPeso;
				}
			}
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
			vlPesoTotalPedido = PesoMedioPedidoService.getInstance().calculatePesoMedioPedido(vlPesoTotalPedido, pedido.vlTotalPedido);
		}
		return vlPesoTotalPedido;
	}

	private void validaPesoMaximoEmpresa(Pedido pedido, double vlPesoTotalPedido) throws SQLException {
		if (LavenderePdaConfig.usaControlePesoPedidoPorEmpresa && !pedido.isPedidoBonificacao()) {
			double pesoMaximoEmpresa = pedido.getEmpresa().vlMaxPeso;
			if (pesoMaximoEmpresa != 0 && ValueUtil.round(vlPesoTotalPedido) > ValueUtil.round(pesoMaximoEmpresa)) {
				Object [] params = {StringUtil.getStringValueToInterface(pesoMaximoEmpresa), StringUtil.getStringValueToInterface(vlPesoTotalPedido)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMAXIMO_EMPRESA_ATINGIDO, params));
			}
		}
	}

	public void validaPesoMaximoCondicaoPagamento(Pedido pedido, double vlPesoTotalPedido) throws SQLException {
		if (LavenderePdaConfig.usaControlePesoPedidoPorCondPagto && !pedido.isPedidoBonificacao()) {
			double pesoMaximoCondPagto = pedido.getCondicaoPagamento().vlMaxPeso;
			if (pesoMaximoCondPagto != 0 && ValueUtil.round(vlPesoTotalPedido) > ValueUtil.round(pesoMaximoCondPagto)) {
				Object [] params = {StringUtil.getStringValueToInterface(pesoMaximoCondPagto), StringUtil.getStringValueToInterface(vlPesoTotalPedido)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMAXIMO_COND_PAGTO_ATINGIDO, params));
			}
		}
	}

	public void validaPesoMaximoCondicaoPagamento(Pedido pedido) throws SQLException {
		double vlPesoTotalPedido = getVlPesoTotalPedido(pedido, new ItemPedido());
		validaPesoMaximoCondicaoPagamento(pedido, vlPesoTotalPedido);
	}
	
	public Vector atualizaPedidoEItensAposAlteracaoPreco() throws SQLException {
		Date dataLimite = DateUtil.getCurrentDate();
		DateUtil.decDay(dataLimite, LavenderePdaConfig.nuDiasValidadeCustoItem);
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllByExampleOnlyPda(pedidoFilter);
		boolean atualizouItem = false;
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (pedido.dtEmissao != null && pedido.dtEmissao.isBefore(dataLimite)) {
				findItemPedidoList(pedido);
				int sizeItemPedidoList = pedido.itemPedidoList.size();
				for (int j = 0; j < sizeItemPedidoList; j++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[j];
					ItemPedidoAud itemPedidoAud = itemPedido.getItemPedidoAud();
					if (itemPedidoAud != null && ValueUtil.isNotEmpty(itemPedidoAud.cdEmpresa) && itemPedidoAud.dtInsercaoItem != null && itemPedidoAud.dtInsercaoItem.isBefore(dataLimite)) {
						ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
						if (itemTabelaPreco.vlBase != itemPedido.vlBaseFlex) {
							atualizouItem = true;
							itemPedido.getItemPedidoAud().dtVerificacaoCusto = DateUtil.getCurrentDate();
							ItemPedidoService.getInstance().loadValorNeutroItemPedidoAud(itemPedido);
							ItemPedidoService.getInstance().loadValorVerbaEmpresa(itemPedido);
							VerbaService.getInstance().calculaVerbaComImpostoERentabilidade(itemPedido);
							ItemPedidoService.getInstance().calculaIndiceRentabilidadeItemSemTributos(itemPedido);
							ItemPedidoService.getInstance().updateItemSimples(itemPedido);
							itemPedido.atualizouCusto = true;
						} else {
							itemPedido.getItemPedidoAud().dtVerificacaoCusto = DateUtil.getCurrentDate();
							ItemPedidoAudService.getInstance().update(itemPedido.getItemPedidoAud());
						}
					}
				}
				if (atualizouItem) {
					calculaIndiceRentabilidadePedidoSemTributos(pedido);
					getCrudDao().update(pedido);
					pedido.atualizouCustoItem = true;
				} else {
					pedidoList.removeElement(pedido);
					i--;
				}
			} else {
				pedidoList.removeElement(pedido);
				i--;
			}
		}
		return pedidoList;
	}

	public void validaCustoVencido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.nuDiasValidadeCustoItem > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			DateUtil.decDay(dataLimite, LavenderePdaConfig.nuDiasValidadeCustoItem);
			if (pedido.dtEmissao != null && pedido.dtEmissao.isBefore(dataLimite)) {
				findItemPedidoList(pedido);
				int sizeItemPedidoList = pedido.itemPedidoList.size();
				for (int i = 0; i < sizeItemPedidoList; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					ItemPedidoAud itemPedidoAud = itemPedido.getItemPedidoAud();
					if (itemPedidoAud != null && itemPedidoAud.dtInsercaoItem != null && itemPedidoAud.dtInsercaoItem.isBefore(dataLimite)) {
						if (ValueUtil.isEmpty(itemPedidoAud.dtVerificacaoCusto) || itemPedidoAud.dtVerificacaoCusto.isBefore(dataLimite)) {
							throw new ValidationException(Messages.PEDIDO_NAO_ENVIADO_CUSTO_DESATUALIZADO);
						}
					}
				}
			}
		}
	}
	
	public void validaPedidoComRestricaoDeItens(Pedido pedido) {
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			try {
				String cdProdutoRestritoNaWeb =  getCdProdutoRestritoNaWeb(pedido);
				if (StringUtil.contains(cdProdutoRestritoNaWeb, "ERRO")) {
					throw new Exception();
				}
				ItemPedidoService.getInstance().atualizaFlRestritoItensPedido(pedido, cdProdutoRestritoNaWeb);
				atualizaFlRestrito(pedido);
				updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_FLRESTRITO, pedido.flRestrito, Types.VARCHAR);
				throw new Exception();
			} catch (Throwable ex) {
				if (pedido.isPedidoRestrito()) {
					SessionLavenderePda.houveErroPedidosRestrito = true;
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_COM_PRODUTO_RESTRITO, pedido.nuPedido));
				}
			}
		}
	}
	
	private String getCdProdutoRestritoNaWeb(Pedido pedido) throws Exception {
		if (LavenderePdaConfig.usaRevalidacaoProdutoRestritosEnvioPedido) {
			findItemPedidoList(pedido);
			String retorno = "";
			retorno = SyncManager.revalidaRestricaoProdutoItensPedido(pedido.itemPedidoList);
			return ValueUtil.isNotEmpty(retorno) ? retorno : "";
		}
		throw new Exception();
	}
	
	public boolean isPedidoDtEntregaFinalSemanaFeriado(final Date dataEntrega) throws SQLException {
		if (ValueUtil.isNotEmpty(dataEntrega)) {
			if (DateUtil.getDayOfWeek(dataEntrega) == 0 || DateUtil.getDayOfWeek(dataEntrega) == 6) {
				return true;
			} else {
				Feriado feriadoFilter = new Feriado();
				feriadoFilter.nuDia = dataEntrega.getDay();
				feriadoFilter.nuMes = dataEntrega.getMonth();
				feriadoFilter.nuAno = dataEntrega.getYear();
				int qtdFeriado = FeriadoService.getInstance().countByExample(feriadoFilter);
				return qtdFeriado > 0;
			}
		}
		return false;
	}
	
	private void criaVisitaPedido(Pedido pedido) throws SQLException {
		if (SessionLavenderePda.visitaAndamento == null || !ValueUtil.valueEquals(SessionLavenderePda.visitaAndamento.cdCliente, pedido.cdCliente)) {
			Visita visita =  null;
			if (SessionLavenderePda.visitaAndamento == null) {
				visita = VisitaService.getInstance().geraVisitaAutomaticaPorPedido(pedido, ValueUtil.VALOR_SIM, null);
			} else {
				visita = VisitaService.getInstance().geraVisitaAutomaticaPorPedido(pedido, null, ValueUtil.VALOR_SIM);
			}
			pedido.setVisita(visita);
			VisitaPedidoService.getInstance().adicionarVisitaPedido(pedido.nuPedido, visita.cdVisita);
		} else {
			VisitaPedidoService.getInstance().adicionarVisitaPedido(pedido.nuPedido, SessionLavenderePda.visitaAndamento.cdVisita);
		}
	}

	public double getQtEstoqueFalta(ItemPedido itemPedido) {
		return itemPedido.getQtItemFisico() - itemPedido.estoque.qtEstoque;
	}
	
	private void geraReservaEstoque(Pedido pedido) {
		if (pedido != null) {
			try {
				defineDtSugestaoClienteNosItens(pedido);
				String retorno = SyncManager.geraReservaEstoqueItensPedido(pedido, pedido.itemPedidoList);
				if (ValueUtil.isEmpty(retorno) && LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido()) {
					try {
						pedido.itemPedidoProblemaReservaEstoqueList = ItemPedidoService.getInstance().getItemPedidoProblemaReservaEstoqueList(retorno);
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
					String msgErro = ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList) ? Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE : Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE_GERAL;
					if (ValueUtil.getBooleanValue(pedido.flSituacaoReservaEst)) {
						msgErro = Messages.MSG_ERRO_RESERVA_PRODUTO_CANCELADA + msgErro;
					}
					pedido.flSituacaoReservaEst = ValueUtil.VALOR_NAO;
					throw new ValidationException(msgErro);
				} else {
					pedido.itemPedidoProblemaReservaEstoqueList = new Vector();
					if (LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
						if (ValueUtil.isEmpty(retorno) || retorno.equals("[]")) {
							throw new ValidationException(Messages.ESTOQUE_CORRENTE_RETORNO_VAZIO);
						}
						pedido.itemPedidoComReservaEstoqueCorrenteList = new Vector();
						Vector listaItemPedidoRetorno = ItemPedidoService.getInstance().getItemPedidoComReservaEstoqueCorrenteList(retorno);
						boolean dtSugestaoEntregaNull = false;
						Date maiorDataEntrega = null;
						for (int i = 0; i < listaItemPedidoRetorno.size(); i++) {
							ItemPedido itemPedido = (ItemPedido) listaItemPedidoRetorno.items[i];
							if (itemPedido.dtSugestaoCliente == null) dtSugestaoEntregaNull = true;
							if (maiorDataEntrega == null
									|| DateUtil.isBefore(maiorDataEntrega, itemPedido.dtEntrega)) {
								maiorDataEntrega = itemPedido.dtEntrega;
								if (!LavenderePdaConfig.usaReservaEstoqueCorrenteR3()){
									pedido.dtEntrega = maiorDataEntrega;
						   		}
							}
							if (itemPedido.reservado) {
								pedido.itemPedidoComReservaEstoqueCorrenteList.addElement(itemPedido);
								ajustarDataPrevisao(pedido, itemPedido);
							} else {
								popularEstoqueDisponivel(pedido, itemPedido);
								pedido.itemPedidoProblemaReservaEstoqueList.addElement(itemPedido);
							}
						}
						if (ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList)) {
							String msgErro = ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList) ? Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE : Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE_GERAL;
							throw new ValidationException(msgErro);
						}
						if (!dtSugestaoEntregaNull
								&& pedido.dtSugestaoCliente != null
								&& DateUtil.isBefore(pedido.dtEntrega, pedido.dtSugestaoCliente)) {
							pedido.dtEntrega = pedido.dtSugestaoCliente;
							updateColumn(pedido.getRowKey(), "dtEntrega", pedido.dtEntrega, Types.DATE);
					}
						if (!ValueUtil.isEmpty(pedido.dtSugestaoCliente) && !pedido.dtSugestaoCliente.equals(pedido.dtEntrega)){
							pedido.isDtSugeridaDiferenteDtEntrega = true;
						}
					}
					pedido.flSituacaoReservaEst = ValueUtil.VALOR_SIM;
				}
			} catch (ValidationException e) {
				throw e;
			} catch (ConnectionException e) {
				throw new ValidationException(Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE_SEM_CONEXAO);
			} catch (Throwable e) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE, e.getMessage()));
			}
		}
	}
	
	private void defineDtSugestaoClienteNosItens(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				itemPedido.dtSugestaoCliente = pedido.dtSugestaoCliente;
				itemPedido.cdLocalEstoque = pedido.getCdLocalEstoque();
				itemPedido.cdCentroCusto = pedido.cdCentroCusto;
			}
		}
	}

	private void popularEstoqueDisponivel(Pedido pedido, ItemPedido itemPedido) {
		int index = pedido.itemPedidoList.indexOf(itemPedido);
		ItemPedido itemPedidoDoPedido = (ItemPedido) pedido.itemPedidoList.elementAt(index);
		itemPedidoDoPedido.qtEstoqueDisponivel = itemPedido.qtEstoqueDisponivel;
	}

	private void ajustarDataPrevisao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		int index = pedido.itemPedidoList.indexOf(itemPedido);
		ItemPedido itemPedidoDoPedido = (ItemPedido) pedido.itemPedidoList.elementAt(index);

		if (itemPedido.consumiuPrev) {
			if (ValueUtil.isEmpty(itemPedidoDoPedido.dtEntrega) || !itemPedidoDoPedido.dtEntrega.equals(itemPedido.dtEntrega)) {
				itemPedidoDoPedido.dtEntrega = itemPedido.dtEntrega;
				ItemPedidoService.getInstance().updateColumn(itemPedidoDoPedido.getRowKey(), "dtEntrega", itemPedidoDoPedido.dtEntrega, Types.DATE);
			}

			if (ValueUtil.isEmpty(pedido.dtEntrega)) {
				pedido.dtEntrega = ValueUtil.isNotEmpty(itemPedidoDoPedido.dtEntrega) ? itemPedidoDoPedido.dtEntrega : itemPedido.dtEntrega;
			} else if (ValueUtil.isNotEmpty(pedido.dtEntrega)
					&& ValueUtil.isNotEmpty(itemPedidoDoPedido.dtEntrega)
					&& pedido.dtEntrega.isBefore(itemPedidoDoPedido.dtEntrega)) {
				pedido.dtEntrega = itemPedidoDoPedido.dtEntrega;
				}
			updateColumn(pedido.getRowKey(), "dtEntrega", pedido.dtEntrega, Types.DATE);
			}
		}

	 private void carregaItemPedidoDoPedido(Vector estoquePrevistoList, Produto produto) throws SQLException {
        if (estoquePrevistoList == null) {
            EstoquePrevisto estoquePrevistoFilter = new EstoquePrevisto(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto);
            estoquePrevistoList = EstoquePrevistoService.getInstance().findAllByExample(estoquePrevistoFilter);

            EstoqueIndustria estoqueIndustriaFilter = new EstoqueIndustria(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto);
            Vector estoqueIndustriaFilterList = EstoqueIndustriaService.getInstance().findAllByExample(estoqueIndustriaFilter);
            estoquePrevistoList.addElementsNotNull(estoqueIndustriaFilterList.items);
	}
    }

	private boolean verificaTrocaDataPrevista(ItemPedido itemPedidoDoPedido, Date dtEntrega) throws SQLException {
		if (itemPedidoDoPedido.estoquePrevistoList == null) return false;
		carregaItemPedidoDoPedido(itemPedidoDoPedido.estoquePrevistoList, itemPedidoDoPedido.getProduto());
        int size = itemPedidoDoPedido.estoquePrevistoList.size();
        if (ValueUtil.isNotEmpty(dtEntrega)) {
            for (int i = 0; i < size; i++) {
                Object prev = itemPedidoDoPedido.estoquePrevistoList.items[i];
                Date dtPrev = null;
                if (prev instanceof EstoquePrevisto) {
                    dtPrev = ((EstoquePrevisto) prev).dtEstoque;
                } else if (prev instanceof EstoqueIndustria) {
                    dtPrev = ((EstoqueIndustria) prev).dtEstoque;
                } else if (prev instanceof EstoquePrevistoGeral) {
                    dtPrev = ((EstoquePrevistoGeral) prev).dtEstoque;
                } else if (prev instanceof EstoqueIndustriaGeral) {
                    dtPrev = ((EstoqueIndustriaGeral) prev).dtEstoque;
                }
                if (dtEntrega.equals(dtPrev)) {
                    return false;
                }
            }
        }
        return true;
    }

	public void inativaReservaEstoque(Pedido pedido) {
		if (pedido != null) {
			try {
				String retorno = SyncManager.inativaReservaEstoqueItensPedido(pedido);
				if (ValueUtil.isEmpty(retorno) || !retorno.equals("OK")) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_VALIDACAO_INATIVACAO_RESERVA_ESTOQUE, retorno));
				} else {
					pedido.flSituacaoReservaEst = ValueUtil.VALOR_NAO;
					removeDataEntregaDosItensEDoPedido(pedido);
				}
			} catch (Throwable ex) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_VALIDACAO_INATIVACAO_RESERVA_ESTOQUE, ex.getMessage()));
			}
		}
	}
	
	private void removeDataEntregaDosItensEDoPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				itemPedido.dtEntrega = null;
				ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "dtEntrega", itemPedido.dtEntrega, Types.DATE);
			}
			pedido.dtEntrega = null;
			updateColumn(pedido.getRowKey(), "dtEntrega", pedido.dtEntrega, Types.DATE);
		}
	}

	public void calculaRentabilidadeItensPedidoConsiderandoAcrescimoCusto(Pedido pedido) throws SQLException {
		reloadValoresDosItensPedido(pedido, true, false);
		updateItensPedidoAfterChanges(pedido);
		calculateRentabilidadePedido(pedido);
	}
	
	public void atualizaControlesValidacaoComponentesAcrescimoCusto(Pedido pedido) {
		double vlPctAcrescCusto = ValueUtil.getDoubleValue((String) pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO));
		String cdMotivoAcrescimoCusto = (String) pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_CDMOTIVOACRESCIMOCUSTO);
		pedido.isMotivoAcrescimoCustoVazio = vlPctAcrescCusto != 0 && ValueUtil.isEmpty(cdMotivoAcrescimoCusto);
		pedido.isVlAcrescimoCustoVazio = vlPctAcrescCusto == 0 && ValueUtil.isNotEmpty(cdMotivoAcrescimoCusto);
	}

	public void atualizaPedidoPendente(Pedido pedidoBonicacao) throws SQLException {
		try {
		PedidoPdbxDao.getInstance().marcaPedidoPendente(pedidoBonicacao.pedidoRelacionado);
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
	}
	}
	
	public void atualizaPedidoPendenteLimCred(Pedido pedidoBonicacao) throws SQLException {
		PedidoPdbxDao.getInstance().marcaPedidoPendenteLimCred(pedidoBonicacao.pedidoRelacionado);
	}
	
	public void atualizaDescontoPedido(Pedido pedidoBonicacao) throws SQLException {
		try {
		PedidoPdbxDao.getInstance().atualizaDescontoPedido(pedidoBonicacao.pedidoRelacionado);
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
	}
	}

	public void validaPedidoRelacionadoBonificacaoEmAberto(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoFilter.cdCliente = pedido.cdCliente;
			pedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			if (pedido.isPedidoBonificacao()) {
				pedidoFilter.nuPedido = pedido.nuPedidoRelBonificacao;
			} else {
				pedidoFilter.nuPedidoRelBonificacao = pedido.nuPedido;
			}
			pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
			if (countByExample(pedidoFilter) != 0) {
				throw new ValidationException(Messages.MSG_ERRO_ENVIO_PEDIDO_RELACIONADO);
			}
			if (pedido.isPedidoBonificacao()) {
				pedidoFilter.nuPedido = null;
				pedidoFilter.nuPedidoRelBonificacao = pedido.nuPedidoRelBonificacao;
			}
			if (countByExample(pedidoFilter) != 0) {
				throw new ValidationException(Messages.MSG_ERRO_ENVIO_PEDIDO_RELACIONADO);
			}
		}
	}
	
	public void calculaItemComParticipacaoBonificacaoExtrapolada(Pedido pedido) throws SQLException {
		pedido.itemPedidoParticipacaoExtrapoladaList = new Vector();
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			double vlPctMaxParticipacao = ValueUtil.round(itemPedido.getItemTabelaPreco().vlPctMaxParticipacao);
			if (vlPctMaxParticipacao < 100) {
				double vlParticipacaoExtrapolado = ValueUtil.round(pedido.vlTotalPedido * (vlPctMaxParticipacao / 100));
				if (itemPedido.vlTotalItemPedido > vlParticipacaoExtrapolado) {
					pedido.itemPedidoParticipacaoExtrapoladaList.addElement(itemPedido);
				}
			}
		}
	}
	
	private void validateLoteFechamentoPedido(Pedido pedido) throws SQLException {
		StringBuffer msgErro = new StringBuffer();
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (ValueUtil.isEmpty(itemPedido.cdLoteProduto) && !ValueUtil.VALOR_NAO.equals(itemPedido.getProduto().flLoteObrigatorio) && LoteProdutoService.getInstance().isProdutoPossuiLoteProduto(itemPedido)) {
				msgErro.append(" ").append(ProdutoService.getInstance().getDescriptionWithId(itemPedido.cdProduto)).append(",");
			} 
		}
		if (ValueUtil.isNotEmpty(msgErro.toString())) {
			msgErro.deleteCharAt(msgErro.length() - 1);
			throw new ValidationException(MessageUtil.getMessage(Messages.LOTEPRODUTO_NAOSELECIONADO_FECHAR_PEDIDO, msgErro.toString()));
		}
	}
	
	public void setPedidoComPagamentoAVista(Pedido ped) throws SQLException {
		if (ped != null && (isPermitePedidoAVistaClienteAtrasado() || isPermitePedidoAVistaClienteBloqueado())) {
			if (ValueUtil.VALOR_ZERO.equals(StringUtil.getStringValue(ped.getCondicaoPagamento().qtDiasMediosPagamento)) && (ped.getTipoPagamento().isTipoPagamentoAVista()) && !SessionLavenderePda.getCliente().flClienteAtrasadoLiberadoSenha) {
				SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
			} else {
				SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_NAO;
			}
				ped.flPagamentoAVista = SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista;
			}
		}
	
	public boolean isPermitePedidoAVistaClienteAtrasado() throws SQLException {
		return LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() && SessionLavenderePda.getCliente().isStatusAtrasado();
	}
	
	public boolean isPermitePedidoAVistaClienteBloqueado() throws SQLException {
		return LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && SessionLavenderePda.getCliente().isStatusBloqueado();
	}
	
	public void calculaValorTotalCreditoFrete(Pedido pedido) throws SQLException {
		pedido.vlTotalCreditoFrete = 0d;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			pedido.vlTotalCreditoFrete += itemPedido.getVlCreditoFrete();
		}
		if (ValueUtil.isNotEmpty(pedido.nuPedido) && ValueUtil.isNotEmpty(pedido.flOrigemPedido)) {
			try {
				updateColumn(pedido.getRowKey(), "VLTOTALCREDITOFRETE", pedido.vlTotalCreditoFrete, Types.DECIMAL);
			} catch (Throwable e) {
				//--
			}
		}
	}

	public void calculaValorTotalCreditoCondicao(Pedido pedido) throws SQLException {
		pedido.vlTotalCreditoCondicao = 0d;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			pedido.vlTotalCreditoCondicao += itemPedido.getVlCreditoCondicao();
		}
		if (ValueUtil.isNotEmpty(pedido.nuPedido) && ValueUtil.isNotEmpty(pedido.flOrigemPedido)) {
			try {
				updateColumn(pedido.getRowKey(), "VLTOTALCREDITOCONDICAO", pedido.vlTotalCreditoCondicao, Types.DECIMAL);
			} catch (Throwable e) {
				//--
			}
		}
	}

	public Pedido getPedidoRelBonificacao(Pedido pedido) throws SQLException {
		if (pedido != null) {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			pedidoFilter.nuPedido = pedido.nuPedidoRelBonificacao;
			return (Pedido) findByRowKey(pedidoFilter.getRowKey());
		}
		return null;
	}
	
	public Vector getPedidoBonificacaoListByPedidoVenda(Pedido pedido) throws SQLException {
		if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && pedido != null && pedido.cdCliente != null) {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoFilter.cdCliente = pedido.cdCliente;
			pedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
			pedidoFilter.nuPedidoRelBonificacao = pedido.nuPedido;
			return findAllByExampleDyn(pedidoFilter);
		}
		return new Vector();
	}
	
	public boolean isPedidoComBonificacaoRelacionada(Pedido pedido) throws SQLException {
		if (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao() || !LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			return ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao);
		}
		Vector pedidosBonificacaoList = getPedidoBonificacaoListByPedidoVenda(pedido);
		return ValueUtil.isNotEmpty(pedidosBonificacaoList);
	}
	
	public void setPedidosBonificacaoComTipoCreditoPendente(Pedido pedido) throws SQLException {
		Vector pedidosBonificacaoList = getPedidoBonificacaoListByPedidoVenda(pedido);
		if (ValueUtil.isNotEmpty(pedidosBonificacaoList)) {
			for (int i = 0; i < pedidosBonificacaoList.size(); i++) {
				Pedido pedidoBonificacao = (Pedido) pedidosBonificacaoList.items[i];
				try {
					updateColumn(pedidoBonificacao.getRowKey(), "FLPENDENTE", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
				} catch (Throwable e) {
					//--
				}
			} 
		}
	}
	
	public boolean cancelaPedidoPendenteAprovacao(Pedido pedido) {
		try {
			String retorno = SyncManager.cancelaPedido(pedido);
			if (!"OK".equals(retorno)) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CANCELADO_ERRO, retorno));
			} else {
				consisteCancelamentoPedidos(pedido);
				if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
					PedidoLogService.getInstance().savePedidoLog(TipoRegistro.CANCELAMENTO, pedido);
				}
				UiUtil.showSucessMessage(Messages.PEDIDO_MSG_CANCELADO_SUCESSO);
				return true;
			}
		} catch (Throwable e) {	
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CONEXAO_ERRO_CANCELAMENTO, e.getMessage()));
		}
		return false;
	}
	
	public boolean definePedidoPerdido(Pedido pedido) {
		try {
			if (pedido != null) {
				PedidoPdbxDao.getInstance().updatePedidoPerdido(pedido);
				getItemPedidoService().updateFlPedidoPerdidoByPedido(pedido);
				return true;
			}
			return false;
		} catch (Throwable e) {
			return false;
		}
	}
	
	public boolean cancelaPedidoENfe(Pedido pedido, boolean possuiConexao) throws SQLException {
		if (possuiConexao) {
			return isExecutaCancelamentoComConexao(pedido);
		}
		UiUtil.showErrorMessage(Messages.PEDIDO_MSG_CANCELAR_SEM_CONEXAO);
		return false;
	}

	private boolean isExecutaCancelamentoComConexao(Pedido pedido) throws SQLException {
		String retorno = "";
		try {
			retorno = SyncManager.cancelaPedido(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CONEXAO_ERRO_CANCELAMENTO, ex.getMessage()));
		}
		if (!"OK".equals(retorno)) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CANCELADO_ERRO, retorno));
			return false;
		}
		try {
			NfeService.getInstance().executaAcoesCancelamentoNfe(pedido);
			PedidoService.getInstance().cancelaPedido(pedido);
			UiUtil.showSucessMessage(Messages.PEDIDO_MSG_CANCELADO_SUCESSO);
			return true;
		} catch (Throwable e) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CANCELADO_ERRO, e.getMessage()));
		}
		return false;
	}
	
	public void cancelaPedido(Pedido pedido) throws SQLException {
		if (pedido == null) {
			return;
		}
			updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_CDSTATUSPEDIDO, LavenderePdaConfig.cdStatusPedidoCancelado, Types.VARCHAR);
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SA, LavenderePdaConfig.cdStatusPedidoCancelado, StringUtil.getStringValue(pedido.vlTotalPedido));
			PedidoService.getInstance().deletaNotasCredito(pedido);
		}
	
	private void consisteCancelamentoPedidos(Pedido pedido) throws SQLException {
		Vector pedidosBonificacaoList = getPedidoBonificacaoListByPedidoVenda(pedido);
		pedidosBonificacaoList.addElement(pedido);
			for (int i = 0; i < pedidosBonificacaoList.size(); i++) {
				Pedido pedidoBonificacao = (Pedido) pedidosBonificacaoList.items[i];
				try {
					PedidoPdbxDao.getInstance().updateColumnErp(pedidoBonificacao.getRowKey(), "CDSTATUSPEDIDO", LavenderePdaConfig.cdStatusPedidoCancelado, totalcross.sql.Types.VARCHAR);
					LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_SA, LavenderePdaConfig.cdStatusPedidoCancelado, StringUtil.getStringValue(pedido.vlTotalPedido));
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				}
			}
		}
	
	public void liberaItensPedidoPendente(final Pedido pedido) throws SQLException {
		if (pedido == null || ValueUtil.isEmpty(pedido.itemPedidoList)) {
			return;
		}
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.isPendente()) {
				continue;
			}
					ItemPedidoService.getInstance().updateColumnErp(itemPedido.getRowKey(), "FLPENDENTE", ValueUtil.VALOR_NAO, totalcross.sql.Types.VARCHAR);
				}
			}
	
	public void updateColumnErp(String rowkey, String dsColumn, Object value, int type) throws SQLException {
		PedidoPdbxDao.getInstance().updateColumnErp(rowkey, dsColumn, value, type);
	}
	
	public void updatePedidoLiberacaoItemPendente(Pedido pedido) throws SQLException {
		if (pedido == null) {
			return;
		}
			pedido.flItemPendente = ValueUtil.VALOR_NAO;
		if (pedido.isFlOrigemPedidoPda()) {
				updateColumn(pedido.getRowKey(), "FLITEMPENDENTE", ValueUtil.VALOR_NAO, totalcross.sql.Types.VARCHAR);
			} else {
				updateColumnErp(pedido.getRowKey(), "FLITEMPENDENTE", ValueUtil.VALOR_NAO, totalcross.sql.Types.VARCHAR);
			}
		}
	
	public void updateFlPendente(Pedido pedido, String flPendente) throws SQLException {
		if (pedido == null) {
			return;
		}
		if (pedido.isFlOrigemPedidoPda()) {
				updateColumn(pedido.getRowKey(), "FLPENDENTE", flPendente, totalcross.sql.Types.VARCHAR);
			} else {
				updateColumnErp(pedido.getRowKey(), "FLPENDENTE", flPendente, totalcross.sql.Types.VARCHAR);
			}
		}
	
	public void updateFlPendenteLimCred(Pedido pedido, String flPendente) throws SQLException {
		if (pedido == null) return;
		
		if (pedido.isFlOrigemPedidoPda()) {
				updateColumn(pedido.getRowKey(), "FLPENDENTELIMCRED", flPendente, totalcross.sql.Types.VARCHAR);
			} else {
				updateColumnErp(pedido.getRowKey(), "FLPENDENTELIMCRED", flPendente, totalcross.sql.Types.VARCHAR);
			}
		}
	
	public void updateFlPendenteCondPagto(Pedido pedido, String flPendente) throws SQLException {
		if (pedido == null) return;
		if (pedido.isFlOrigemPedidoPda()) {
			updateColumn(pedido.getRowKey(), "FLPENDENTECONDPAGTO", flPendente, totalcross.sql.Types.VARCHAR);
		} else {
			updateColumnErp(pedido.getRowKey(), "FLPENDENTECONDPAGTO", flPendente, totalcross.sql.Types.VARCHAR);
	}
	}

	public void updateFlPedidoLiberadoOutraOrdem(Pedido pedido, String flPedidoLiberadoOutraOrdem) throws SQLException {
		if (pedido == null) return;
		
			updateColumnErp(pedido.getRowKey(), "FLPEDIDOLIBERADOOUTRAORDEM", flPedidoLiberadoOutraOrdem, totalcross.sql.Types.VARCHAR);
		}

	public Pedido findPedidoByVisitaPedido(VisitaPedido visitaPedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = visitaPedido.cdEmpresa;
		pedidoFilter.cdRepresentante = visitaPedido.cdRepresentante;
		pedidoFilter.nuPedido = visitaPedido.nuPedido;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		return (Pedido) findByRowKey(pedidoFilter.getRowKey());
	}
	
	public Pedido findPedidoByItemPedidoGrade(ItemPedidoGrade itemPedidoGrade) throws SQLException {
		if (itemPedidoGrade == null) return null;

			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdEmpresa = itemPedidoGrade.cdEmpresa;
			pedidoFilter.cdRepresentante = itemPedidoGrade.cdRepresentante;
			pedidoFilter.nuPedido = itemPedidoGrade.nuPedido;
			pedidoFilter.flOrigemPedido = itemPedidoGrade.flOrigemPedido;
			return (Pedido) findByRowKey(pedidoFilter.getRowKey());
		}
	
	
	public boolean aplicaDescontosAutoEmCascataNaCapaPedidoPorItem(Pedido pedido) throws SQLException {
		return aplicaDescontosCascataNaCapaPedidoPorItem(pedido, LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo(), LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual(), LavenderePdaConfig.usaDescontoPedidoPorTipoFrete);
	}
	
	public boolean aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(Pedido pedido) throws SQLException {
		return aplicaDescontosCascataNaCapaPedidoPorItem(pedido, true, true , true);
	}

	private boolean aplicaDescontosCascataNaCapaPedidoPorItem(Pedido pedido, boolean aplicaDescCliente, boolean aplicaDescCondPgto, boolean aplicaDescFrete) throws SQLException {
		String dsProduto = "";
		try {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				dsProduto = itemPedido.getProduto().toString();
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				getItemPedidoService().calculate(itemPedido, pedido);
				getItemPedidoService().validate(itemPedido);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ALTERAR_DESCONTOS_CASCATA + dsProduto + ". " + e.getMessage());
			return false;
		}
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (aplicaDescCliente && pedido.vlPctDescCliente != 0) {
				itemPedido.vlPctDescCliente = pedido.vlPctDescCliente;
			}
			if (aplicaDescCondPgto && pedido.vlPctDescontoCondicao != 0) {
				itemPedido.vlPctDescontoCondicao = pedido.vlPctDescontoCondicao;
			}
			if (aplicaDescFrete && pedido.vlPctDescFrete != 0) {
				itemPedido.vlPctDescFrete = pedido.vlPctDescFrete;
			}
			getItemPedidoService().updateItemSimples(itemPedido);
		}
		update(pedido);
		return true;
	}

	public void validaPedidoAbertoEnvioDados() throws SQLException {
		if (countPedidosEmAberto() > 0) {
			throw new ValidationException(Messages.VALIDACAO_PEDIDO_ABERTO_DEVOLVER_ESTOQUE);
		}
	}
	
	public int countPedidosEmAberto()  throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		return countByExample(pedidoFilter);
	}
	
	public int countPedidosFechado()  throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
		return countByExample(pedidoFilter);
	}
	
	public boolean isExistePedidoDiferenteAbertoECancelado(Date dataEmissao) throws SQLException {
		return countPedidosDiferenteAbertoECancelado(dataEmissao) > 0;
	}
	
	public int countPedidosDiferenteAbertoECancelado(Date dtFechamento)  throws SQLException {
		Pedido pedidoFilter = getPedidoFilterFechamentoDiario(dtFechamento);
		int countPedidoPDA = countByExample(pedidoFilter);
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			countPedidoPDA += countPedidosDiferenteAbertoECanceladoErp(dtFechamento);
		}
		return countPedidoPDA;
	}

	public int countPedidosDiferenteAbertoECanceladoErp(Date dtFechamento)  throws SQLException {
		Pedido pedidoFilter = getPedidoFilterFechamentoDiario(dtFechamento);
		return PedidoPdbxDao.getInstanceErp().countByExample(pedidoFilter);
	}

	private Pedido getPedidoFilterFechamentoDiario(Date dtFechamento) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.flFiltraPedidosDifAbertosCancelados = true;
		pedidoFilter.dtFechamentoInicialFilter = dtFechamento;
		pedidoFilter.dtFechamentoFinalFilter = dtFechamento;
		if (dtFechamento == null) {
			FechamentoDiario fechamentoDiarioFilter = new FechamentoDiario(null);
			fechamentoDiarioFilter.cdEmpresa = pedidoFilter.cdEmpresa;
			fechamentoDiarioFilter.cdRepresentante = pedidoFilter.cdRepresentante;
			pedidoFilter.dtFechamento = FechamentoDiarioDao.getInstance().findDataPosteriorADataExclusaoFechamentoDiario(fechamentoDiarioFilter);
			pedidoFilter.flFiltraPedidosDifFechamentoDiario = true;
		}
		return pedidoFilter;
	}

	public void atualizaPedidoAposDevolucao(Vector itemPedidoDevolvidoList) throws SQLException {
		ItemPedido itemPedido = (ItemPedido)itemPedidoDevolvidoList.items[0];
		Pedido pedido = itemPedido.pedido;
		for (int i = 0; i < itemPedidoDevolvidoList.size(); i++) {
			itemPedido = (ItemPedido) itemPedidoDevolvidoList.items[i];
			if (itemPedido.getQtItemFisico() == itemPedido.qtDevolvida) {
				deleteItemPedido(pedido, itemPedido);
				itemPedidoDevolvidoList.removeElementAt(i);
				i--;
			} else {
				atualizaValoresItemPedido(itemPedido, pedido);
			}
			pedido.vlTotalDevolucoes += itemPedido.vlItemPedido * itemPedido.qtDevolvida;
		}
		atualizaPedidoERecarregaListadeItens(pedido);		
	}

	public void atualizaPedidoAntesDeExcluirDevolucao(Pedido pedido, PedidoConsignacao pedidoConsignacao) throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedidoBuilder(pedidoConsignacao).build();
		ItemPedido itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoFilter.getRowKey());
		if (itemPedido == null) {
			ItemPedidoService.getInstance().aplicaDescontoComissaoPadrao(itemPedido, pedido);
			itemPedidoFilter.pedido = pedido;
			itemPedidoFilter.cdUfClientePedido = pedido.getCliente().dsUfPreco;
			itemPedidoFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedidoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedidoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				itemPedidoFilter.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
			}
			
			resetDadosItemPedido(pedido, itemPedidoFilter);
			calculateItemPedido(pedido, itemPedidoFilter, true);
			ItemPedidoService.getInstance().insert(itemPedidoFilter);
		} else {
			itemPedido.qtDevolvida = pedidoConsignacao.qtItemFisico * -1;
		}
		itemPedido = itemPedido != null ? itemPedido : itemPedidoFilter; 
		itemPedido.pedido = pedido;
		atualizaValoresItemPedido(itemPedido, pedido);
		pedido.vlTotalDevolucoes -= itemPedido.vlItemPedido * pedidoConsignacao.qtItemFisico;
		atualizaPedidoERecarregaListadeItens(pedido);
	}
	
	private void atualizaValoresItemPedido(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		itemPedido.oldQtEstoqueConsumido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
		itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() - itemPedido.qtDevolvida);
		calculateItemPedido(pedido, itemPedido, true);
		updateItemPedido(pedido, itemPedido);
	}
	
	private void atualizaPedidoERecarregaListadeItens(Pedido pedido) throws SQLException {
		updatePedidoAfterCrudItemPedido(pedido);
		pedido.itemPedidoList = null;
		findItemPedidoList(pedido);
		pedido.itemPedidoList = ValueUtil.isNotEmpty(pedido.itemPedidoList) ? pedido.itemPedidoList : new Vector();
	}
	
	public Vector findPedidosConsignadosOrdenadosPorVencimento(Pedido pedidoFilter) throws SQLException {
		return PedidoPdbxDao.getInstance().findPedidosConsignadosOrdenadosPorVencimento(pedidoFilter);
	}
	
	
	public boolean isEnviaEmailByTipoPedido(Pedido pedido) throws SQLException {
		if (pedido != null && !LavenderePdaConfig.tipoPedidoOcultoNoPedido && pedido.getTipoPedido() != null) {
					return pedido.getTipoPedido().isEnviaEmail();
				}
		return true;
	}
	
	public boolean isPedidoAtingiuCota(Pedido pedido) throws SQLException {
		return isPedidoAtingiuCota(pedido, false);
	}
	
	public boolean isPedidoAtingiuCota(Pedido pedido, boolean isFromVlBase) throws SQLException {
		if (!pedido.isPedidoReaberto || isFromVlBase) {
			CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
			if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto() && condicaoPagamento.vlIndiceFinanceiro > 1) {
				if (pedido.itemPedidoList.size() >= condicaoPagamento.qtMixCotaItens) {
					return true;
				} else if ((!isFromVlBase ? pedido.vlTotalPedido : pedido.vlPedidoBeforeCota) >= condicaoPagamento.vlCotaItens) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean aplicaIndFinanceiroCota(Pedido pedido) throws SQLException {
		if (PedidoService.getInstance().isPedidoAtingiuCota(pedido)) {
			pedido.flCotaCondPagto = ValueUtil.VALOR_SIM;
			double valorDif = pedido.vlTotalPedido;
			pedido.vlPedidoBeforeCota = valorDif;  
			reloadValoresDosItensPedido(pedido, true, false);
			updateItensPedidoAfterChanges(pedido);
			calculate(pedido);
			updatePedidoAfterCrudItemPedido(pedido);
			pedido.vlDesconto = valorDif - pedido.vlTotalPedido;
			PedidoService.getInstance().update(pedido);
			return true;
		} else {
			pedido.flCotaCondPagto = ValueUtil.VALOR_NAO;
			PedidoService.getInstance().update(pedido);
			return false;
		}
		
	}
	
	public boolean obrigaIndicarClienteEntrega(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaIndicacaoClienteEntregaPedido && pedido.getTipoPedido().isIndicaClienteEntrega();
	}
	
	private void reajustaEstoqueReservaOnChange(String flMode, ItemPedido itemPedido) throws SQLException {
		if (flMode != null && ValueUtil.VALOR_NAO.equals(flMode)) {
			if (EstoqueService.getInstance().getEstoque(itemPedido.getProduto().cdProduto, Estoque.FLORIGEMESTOQUE_ERP) != null) {
				EstoqueService.getInstance().updateEstoqueInterno(itemPedido, ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()), true, Estoque.FLORIGEMESTOQUE_PDA);
				EstoqueService.getInstance().updateEstoqueInterno(itemPedido, ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()), true, Estoque.FLORIGEMESTOQUE_ERP);
			}
		}	
	}
	
	private void reajustaEstoqueReservaOnDelete(String flMode, ItemPedido itemPedido) throws SQLException {
		if (flMode != null && ValueUtil.VALOR_SIM.equals(flMode)) {
			if (EstoqueService.getInstance().getEstoque(itemPedido.getProduto().cdProduto, Estoque.FLORIGEMESTOQUE_ERP) != null) {
				EstoqueService.getInstance().updateEstoqueInterno(itemPedido, ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()), true, Estoque.FLORIGEMESTOQUE_ERP);
			}
		}	
 	}
	
	private Pedido getNewPedido(ItemPedido itemPedido) {
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = itemPedido.cdEmpresa;
		pedido.cdRepresentante = itemPedido.cdRepresentante;
		pedido.nuPedido = itemPedido.nuPedido;
		return pedido;
	}
	
	public boolean isPossuiPedidosSemFlReserva(ItemPedido itemPedido) throws SQLException {
		Pedido pedidoFilter = getNewPedido(itemPedido);
		pedidoFilter.flSituacaoReservaEstReabrePedido = "S";
		return PedidoPdbxDao.getInstance().isPossuiPedidosSemFlReserva(pedidoFilter);
	}
	
	public Pedido findPedidoByItemPedido(ItemPedido itemPedido) throws SQLException {
		Pedido pedidoFilter = getNewPedido(itemPedido);
		pedidoFilter.flOrigemPedido = itemPedido.flOrigemPedido;
		return (Pedido) findByRowKey(pedidoFilter.getRowKey());
	}
	
	public Pedido findPedidoRelBonificacao(Pedido pedido, boolean isPedidoRelacionadoOrigemErp) throws SQLException {
		if (isPedidoRelacionadoOrigemErp) {
			return PedidoPdbxDao.getInstanceErp().findPedidoRelBonificacao(pedido);
		}
		return PedidoPdbxDao.getInstance().findPedidoRelBonificacao(pedido);
	}
	
	public boolean isUnidadeBaseRestrita(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		return (itemPedido.isCdUnidadeIgualCdUnidadeProduto() || ValueUtil.valueEquals(itemPedido.cdUnidade, ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO)) && RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, itemPedido.cdUnidade, pedido.cdTipoPedido);
	}

	public void executaCancelamentoPedidosAbertos(Date dataBaseCancelamentoAutoPedidoCliente, String flKeyAccountFilter) throws SQLException {
		if (ValueUtil.isNotEmpty(dataBaseCancelamentoAutoPedidoCliente)) {
			String cdMotivoCancelamento = MotCancelPedidoService.getInstance().findCdMotCancelPedidoDefault();
			if (ValueUtil.isNotEmpty(cdMotivoCancelamento)) {
				Pedido pedidoFilter = new Pedido();
				pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoFilter.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? null : SessionLavenderePda.getRepresentante().cdRepresentante;
				pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
				pedidoFilter.dtEmissaoFinalFilter = dataBaseCancelamentoAutoPedidoCliente;
				pedidoFilter.flKeyAccountFilter = flKeyAccountFilter;
				Vector pedidoList = findAllByExampleOnlyPda(pedidoFilter);
				int size = pedidoList.size();
				for (int i = 0; i < size; i++) {
					Pedido pedido = (Pedido) pedidoList.items[i];
					efetuaCancelamentoPedido(cdMotivoCancelamento, pedido);
				}
			}
		}
	}

	public void efetuaCancelamentoPedido(String cdMotivoCancelamento, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().loadPedidoLog(pedido);
		}
		pedido.cdMotivoCancelamento = cdMotivoCancelamento;
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoCancelado;
		findItemPedidoList(pedido);
		int sizeItemPedido = pedido.itemPedidoList.size();
		for (int j = 0; j < sizeItemPedido; j++) {
			ItemPedidoService.getInstance().cancelaItensPedido(pedido, (ItemPedido)pedido.itemPedidoList.items[j]);
		}
		consisteCancelamentoPedidos(pedido);
		updatePedidoCancelado(pedido);
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().savePedidoLog(TipoRegistro.CANCELAMENTO, pedido);
			PedidoLogService.getInstance().fechaPedidoLogParaEnvio(pedido);
			PedidoLogService.getInstance().updateCdUsuarioCriacao(pedido);
		}
	}

	private void updateProdutoDesejado(Pedido pedido) {
		if (LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogo()) {
			ProdutoDesejado produtoDesejado = new ProdutoDesejado.Builder(pedido).build();
			produtoDesejado.flTipoAlteracao = ProdutoDesejado.FLTIPOALTERACAO_ALTERADO;
			ProdutoDesejadoService.getInstance().updateFlTipoAlteracaoByExample(produtoDesejado);
		}
	}
	
	
	private void validatePedidoNumProdAbaixoMin(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		double qtProdutos = 0;
		ItemPedido item;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido) itemPedidoList.items[i];
			qtProdutos += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(item, item.getQtItemFisico());
		}
		TabelaPreco tabPreco = pedido.getTabelaPreco();
		if (tabPreco != null && tabPreco.qtMinPedido > qtProdutos) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_PRODUTOS_ABAIXO_MIN, new Object[] {tabPreco.dsTabelaPreco, StringUtil.getStringValueToInterface(tabPreco.qtMinPedido), StringUtil.getStringValueToInterface(qtProdutos)}));
		}
	}
	
	private void validateQtMinProdutoGrupo(Pedido pedido) throws SQLException {
		TabelaPreco tabPreco = pedido.getTabelaPreco();
		if(tabPreco == null) return;
		List<GrupoProduto1> cdsGrupoProduto1 = GrupoProduto1Service.getInstance().findDistinctGrupoProdPedido(pedido);
		TabPrecoGrupoProd filter = new TabPrecoGrupoProd();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cdTabelaPreco = tabPreco.cdTabelaPreco;
		for (GrupoProduto1 grupoProduto1 : cdsGrupoProduto1) {
			filter.cdGrupoProduto1 = grupoProduto1.cdGrupoproduto1;
			TabPrecoGrupoProd tabPrecoGrupoProd = (TabPrecoGrupoProd)TabPrecoGrupoProdDbxDao.getInstance().findByRowKey(filter.getRowKey());
			double qtMinPedido = tabPrecoGrupoProd != null ? tabPrecoGrupoProd.qtMinPedido : 0;
			double qtAtual = GrupoProduto1Service.getInstance().getQtItensConversaoUniByGrupoProdutoNoPedido(pedido.itemPedidoList, grupoProduto1.cdGrupoproduto1, 0);
			if (qtAtual < qtMinPedido) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_PROD_ABAIXO_MIN_GRUPO, new Object[] {grupoProduto1.dsGrupoproduto1, StringUtil.getStringValueToInterface(qtMinPedido), StringUtil.getStringValueToInterface(qtAtual)}));
			}
		}
	}
	
	public void updateFlImpressoPromissoria(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flPromissoriaImpressa = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "flPromissoriaImpressa", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		}
	}
	
	public void updateStatusPedidoIgnoraValidacao(Pedido pedido) throws SQLException {
		pedido.flPendente = ValueUtil.VALOR_SIM;
		try {
			validateDuplicated(pedido);
		} catch (ValidationException e) {
			updateColumn(pedido.getRowKey(), "flPendente", pedido.flPendente, Types.VARCHAR);
		}
	}
	
	public String getDadosPedidoFormatoJson(final Pedido pedido, boolean geraSomenteNfeEItens) throws JSONException, SQLException {
		return getDadosPedidoFormatoJson(pedido, geraSomenteNfeEItens, false);
	}
	
	public String getDadosPedidoFormatoJson(final Pedido pedido, boolean geraSomenteNfeEItens, boolean viaCliente) throws JSONException, SQLException {
		JSONObject jsonPedido = null;
		if (!geraSomenteNfeEItens) {
			PedidoDTO pedidoDTO = new PedidoDTO(pedido);
			pedidoDTO.setUseLayoutViaClientePDA(viaCliente);
			pedidoDTO.dsObservacao = StringUtil.getStringValue(pedido.getHashValuesDinamicos().get("DSOBSERVACAO"));
			pedidoDTO.dsObsOrcamento = StringUtil.getStringValue(pedido.dsObsOrcamento);
			jsonPedido = new JSONObject(pedidoDTO);
			if (pedido.getHashValuesDinamicos() != null) {
				jsonPedido = jsonPedido.put("camposDinamicosPDA", MapUtil.convertHashtableToHashMap(pedido.getHashValuesDinamicos()));
			}
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList) || ValueUtil.isNotEmpty(pedido.itemPedidoTrocaList)) {
				int itemPedidoTrocaSize = pedido.itemPedidoTrocaList.size();
				if (pedido.itemPedidoTrocaList != null && itemPedidoTrocaSize > 0) {
					if (pedido.itemPedidoList == null) pedido.itemPedidoList = new Vector(itemPedidoTrocaSize);
					pedido.itemPedidoList.addElements(pedido.itemPedidoTrocaList.toObjectArray());
				}
				JSONObject[] itensPedido = convertToItensPedidoDTOArray(pedido.itemPedidoList);
				jsonPedido = jsonPedido.put("itemPedidoList", new JSONArray(itensPedido));
				//--Para evitar problemas ao excluir pedido, remove os itens de troca da lista normal de itens.
				if (pedido.itemPedidoTrocaList != null && itemPedidoTrocaSize > 0) {
					for (int i = 0; i < itemPedidoTrocaSize; i++) {
						ItemPedido itemPedidoTroca = (ItemPedido) pedido.itemPedidoTrocaList.elementAt(i);
						pedido.itemPedidoList.removeElement(itemPedidoTroca);
			}
				}
			}

			if (ValueUtil.isNotEmpty(pedido.parcelaPedidoList)) {
				JSONObject[] parcelaPedidoList = convertToParcelaPedidoDTOArray(pedido.parcelaPedidoList);
				jsonPedido = jsonPedido.put("parcelaPedidoList", new JSONArray(parcelaPedidoList));
			}
			if (pedido.getPagamentoPedido() != null && ValueUtil.isNotEmpty(pedido.getPagamentoPedido().cdEmpresa)) {
				jsonPedido = jsonPedido.put("debitoBancario", new JSONObject(new PagamentoPedidoDTO().copy(pedido.getPagamentoPedido())));
			}
			if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && PagamentoPedidoService.getInstance().isPedidoContemMultiplosPagamentos(pedido)) {
				jsonPedido = jsonPedido.put("pagamentoPedidoList", new JSONArray(convertToPagamentoPedidoArray(pedido)));
			}
			if (pedido.getInfoFretePedido() != null && ValueUtil.isNotEmpty(pedido.getInfoFretePedido().cdEmpresa)) {
				jsonPedido = jsonPedido.put("infoFretePedido", new JSONObject(new InfoFretePedidoDTO().copy(pedido.getInfoFretePedido())));
			} 
			if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega() || LavenderePdaConfig.isApresentaEnderecoNovoCliente()) {
				ClienteEndereco clienteEndereco  = new ClienteEndereco();
				clienteEndereco = ClienteEnderecoService.getInstance().getClienteEnderecoPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente, pedido);
				jsonPedido = jsonPedido.put("enderecoEntregaPedido", new JSONObject(new ClienteEnderecoDTO().copy(clienteEndereco)));
				jsonPedido = jsonPedido.put("camposDinamicosEnderecoEntregaPedidoPDA", MapUtil.convertHashtableToHashMap(clienteEndereco.getHashValuesDinamicos()));
			}
			if (LavenderePdaConfig.isPermitePedidoNovoCliente() && pedido.isPedidoNovoCliente()) {
				jsonPedido = jsonPedido.put("cliente", new JSONObject(new ClienteDTO().copy(pedido.getCliente())));
				jsonPedido = jsonPedido.put("camposDinamicosClientePDA", MapUtil.convertHashtableToHashMap(pedido.getCliente().getHashValuesDinamicos()));
			}
			if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
				Vector venctosPagamentoList = VenctoPagamentoPedidoService.getInstance().findVenctosByPedido(pedido);
				if (ValueUtil.isNotEmpty(venctosPagamentoList)) {
					JSONObject[] venctosPagamentoJson = convertToVenctoPagamentoDtoArray(venctosPagamentoList);
					jsonPedido.put("venctoPagamentoPedidoList", new JSONArray(venctosPagamentoJson));
				}
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				addItemPedidoBonifCfgList(jsonPedido, pedido);
		}
		}
		if (NfeService.getInstance().isGeraNfe(pedido)) {
			jsonPedido = jsonPedido == null ? new JSONObject() : jsonPedido;
			jsonPedido.put("nuPedido", pedido.nuPedido);
			if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia() && PedidoBoletoService.getInstance().isPedidoContemBoletos(pedido)) {
				jsonPedido = jsonPedido.put("pedidoBoletoList", new JSONArray(convertToPedidoBoletoArray(pedido)));
			}
			if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && PagamentoPedidoService.getInstance().isPedidoContemMultiplosPagamentos(pedido)) {
				jsonPedido = jsonPedido.put("pagamentoPedidoList", new JSONArray(convertToPagamentoPedidoArray(pedido)));
			}
			Nfe nfe = NfeService.getInstance().getNfe(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido, pedido.flOrigemPedido);
			if (nfe != null && nfe.cdEmpresa != null) {
				JSONObject[] itensNfeDTOJson = convertToItemNfeDtoArray(nfe.itemNfeList);
				JSONObject nfeDTOJson = new JSONObject(new NfeDTO(nfe));
				nfeDTOJson.put("itemNfeList", new JSONArray(itensNfeDTOJson));
				jsonPedido.put("nfe", nfeDTOJson);
			} else {
				throw new ApplicationException(Messages.NFE_ERRO_ENVIO_NENHUMA_NFE);
			}
		}
		if (NfceService.getInstance().isGeraNfce(pedido)) {
			jsonPedido = jsonPedido == null ? new JSONObject() : jsonPedido;
			jsonPedido.put("nuPedido", pedido.nuPedido);
			Nfce nfce = NfceService.getInstance().getNfce(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido, pedido.flOrigemPedido);
			if (nfce != null && nfce.cdEmpresa != null) {
				JSONObject[] itensNfceJson = convertToItemNfceDtoArray(nfce.listItensNfce);
				JSONObject nfceDTOJson = new JSONObject(new NfceDTO(nfce));
				nfceDTOJson.put("itemNfceList", new JSONArray(itensNfceJson));
				jsonPedido.put("nfce", nfceDTOJson);
			} else {
				throw new ApplicationException(Messages.NFCE_ERRO_ENVIO_NENHUMA_NFCE);
			}
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			jsonPedido = jsonPedido.put("notaCreditoPedidoList", new JSONArray(convertToNotaCreditoPedidoArray(pedido)));
			
		}
		
		return jsonPedido.toString();
	}

	private void addItemPedidoBonifCfgList(JSONObject jsonPedido, Pedido pedido) throws SQLException {
		Vector itemPedidoBonifCfg = ItemPedidoBonifCfgService.getInstance().findAllItensBonifCfgByPedido(pedido, null);
    	if (ValueUtil.isNotEmpty(itemPedidoBonifCfg)) {
    		int size = itemPedidoBonifCfg.size();
    		JSONObject[] itensPedidoBonifJson = new JSONObject[size];
    		for (int i = 0; i < size; i++) {
    			ItemPedidoBonifCfg itemPedidoBon = (ItemPedidoBonifCfg) itemPedidoBonifCfg.items[i];
    			ItemPedidoBonifCfgDTO itemPedidoBonifCfgDTO = new ItemPedidoBonifCfgDTO(itemPedidoBon);
    			itensPedidoBonifJson[i] = new JSONObject(itemPedidoBonifCfgDTO);
    		}
    		jsonPedido.put("itemPedidoBonifCfgList", new JSONArray(itensPedidoBonifJson));
    	}
	}

	private JSONObject[] convertToParcelaPedidoDTOArray(Vector parcelaPedidoList) {
		int sizeParcelasPedido = parcelaPedidoList.size();
		JSONObject[] parcelasPedidoDTO = new JSONObject[sizeParcelasPedido];
		
		for (int n = 0; n < sizeParcelasPedido; n++) {
			ParcelaPedido parcelaPedido = (ParcelaPedido) parcelaPedidoList.items[n];
			if (parcelaPedido != null) {
				parcelasPedidoDTO[n] = new JSONObject(new ParcelaPedidoDTO().copy(parcelaPedido));
			}
		}

		return parcelasPedidoDTO;
	}

	private JSONObject[] convertToItensPedidoDTOArray(Vector itemPedidoList) throws JSONException, SQLException {
		int sizeItensPedido = itemPedidoList.size();
		JSONObject[] itensPedidoDTO = new JSONObject[sizeItensPedido];
		
		for (int n = 0; n < sizeItensPedido; n++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[n];
			if (itemPedido != null) {
				itensPedidoDTO[n] = new JSONObject(new ItemPedidoDTO().copy(itemPedido));
				if (ValueUtil.isNotEmpty(itemPedido.itemPedidoGradeList)) {
					itensPedidoDTO[n] = itensPedidoDTO[n].put("itemPedidoGradeList", convertToItensPedidoGradeDTOArray(itemPedido.itemPedidoGradeList));
				}
				if (itemPedido.getItemTabelaPreco() != null) {
					JSONObject jsonObjectItemTabelaPreco = new JSONObject(new ItemTabelaPrecoDTO().copy(itemPedido.getItemTabelaPreco()));
					jsonObjectItemTabelaPreco = jsonObjectItemTabelaPreco.put("tabelaPreco", new JSONObject(new TabelaPrecoDTO().copy(itemPedido.getItemTabelaPreco().getTabelaPreco())));
					jsonObjectItemTabelaPreco = jsonObjectItemTabelaPreco.put("produto", new JSONObject(new ProdutoDTO().copy(itemPedido.getItemTabelaPreco().getProduto())));
					
					itensPedidoDTO[n] = itensPedidoDTO[n].put("itemTabelaPreco", jsonObjectItemTabelaPreco);
				}
				if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && itemPedido.isAgrupadorSimilaridade()) {
					Vector itemPedidoAgrSimilarList = ItemPedidoAgrSimilarService.getInstance().findItemPedidoAgrSimilarList(itemPedido);
					if (ValueUtil.isNotEmpty(itemPedidoAgrSimilarList)) {
						itensPedidoDTO[n].put("itemPedidoAgrSimilarList", convertToItensPedidoAgrSimilarDTOArray(itemPedidoAgrSimilarList));
			}
		}
			}
		}
		return itensPedidoDTO;
	}

	private JSONObject[] convertToItensPedidoGradeDTOArray(Vector itemPedidoGradeList) {
		int sizePedidoGradeList = itemPedidoGradeList.size();
		JSONObject[] itensPedidoGradeDTO = new JSONObject[sizePedidoGradeList];
		
		for (int n = 0; n < sizePedidoGradeList; n++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedidoGradeList.items[n];
			if (itemPedidoGrade != null) {
				itensPedidoGradeDTO[n] = new JSONObject(new ItemPedidoGradeDTO().copy(itemPedidoGrade));
				if (itemPedidoGrade.produtoGrade != null) {
					itensPedidoGradeDTO[n] = itensPedidoGradeDTO[n].put("produtoGrade", convertToProdutoGradeDTOArray(itemPedidoGrade.produtoGrade));
				}
			}
		}

		return itensPedidoGradeDTO;
	}
	
	private JSONObject[] convertToItensPedidoAgrSimilarDTOArray(Vector itemPedidoAgrSimilarList) {
		int size = itemPedidoAgrSimilarList.size();
		JSONObject[] itensPedidoAgrSimilarDTO = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar itemPedidoAgrSimilar = (ItemPedidoAgrSimilar) itemPedidoAgrSimilarList.items[i];
			itensPedidoAgrSimilarDTO[i] = new JSONObject(new ItemPedidoAgrSimilarDTO(itemPedidoAgrSimilar));
		}
		return itensPedidoAgrSimilarDTO;
	}
	
	private JSONObject convertToProdutoGradeDTOArray(ProdutoGrade produtoGrade) {
		return new JSONObject(new ProdutoGradeDTO(produtoGrade));
	}
	
	private JSONObject[] convertToPedidoBoletoArray(Pedido pedido) throws SQLException {
		Vector list = PedidoBoletoService.getInstance().findAllBoletoPedido(pedido);
		int size = list.size();
		JSONObject[] pedidoBoletoDTOList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			pedidoBoletoDTOList[i] = new JSONObject(new PedidoBoletoDTO((PedidoBoleto)list.items[i]));
		}
		return pedidoBoletoDTOList;
	}
	
	private JSONObject[] convertToPagamentoPedidoArray(Pedido pedido) throws SQLException {
		Vector list = PagamentoPedidoService.getInstance().findPagamentoPedidoByPedidoList(pedido);
		int size = list.size();
		JSONObject[] pagamentoPedidoDTOList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			pagamentoPedidoDTOList[i] = new JSONObject(new PagamentoPedidoDTO((PagamentoPedido) list.items[i]));
		}
		return pagamentoPedidoDTOList;
	}
	
	
	private JSONObject[] convertToItemNfeDtoArray(Vector itemNfeList) {
		int size = itemNfeList.size();
		JSONObject[] itemNfeDTOList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			ItemNfe itemNfe = (ItemNfe)itemNfeList.items[i];
			itemNfeDTOList[i] = new JSONObject(new ItemNfeDTO(itemNfe));
		}
		return itemNfeDTOList;
	}
	
	private JSONObject[] convertToItemNfceDtoArray(Vector itemNfceList) {
		int size = itemNfceList.size();
		JSONObject[] itemNfceDTOList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			itemNfceDTOList[i] = new JSONObject(new ItemNfceDTO((ItemNfce)itemNfceList.items[i]));
		}
		return itemNfceDTOList;
	}
	
	private JSONObject[] convertToVenctoPagamentoDtoArray(Vector venctoList) {
		int size = venctoList.size();
		JSONObject[] venctoPagamentoDtoList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			VenctoPagamentoPedido vencto = (VenctoPagamentoPedido)venctoList.items[i];
			venctoPagamentoDtoList[i] = new JSONObject(new VenctoPagamentoPedidoDto(vencto));
		}
		return venctoPagamentoDtoList;
	}
	
	private JSONObject[] convertToNotaCreditoPedidoArray(Pedido pedido) throws SQLException {
		Vector notaCreditoPedidoList = NotaCreditoPedidoService.getInstance().buscaNotaCreditoPedidoList(pedido);
		int size = notaCreditoPedidoList.size();
		JSONObject[] notaCreditoPedidoDTOList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			notaCreditoPedidoDTOList[i] = new JSONObject(new NotaCreditoPedidoDTO((NotaCreditoPedido) notaCreditoPedidoList.items[i]));
		}
		return notaCreditoPedidoDTOList;
	}
	
	public void insereDadosRetornoPedido(RetornoPedidoDTO retornoPedidoDTO, Pedido pedido) throws SQLException {
		boolean usaRetornoBackground = LavenderePdaConfig.usaEnvioPedidoBackground;
		boolean recebeuPedido = false, recebeuNfe = false, recebeuPedidoBoleto = false, recebeuNfce = false;
		if (usaRetornoBackground) {
			NfeDao.houveRecebimentoNfeBackground = false;
			PedidoBoletoDao.houveRecebimentoBoletoBackground = false;
		}
		StringBuffer entidadesErro = new StringBuffer();
		if (LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif) {
			recebeuPedido = insereDadosPedidoErpEPedidoErpDif(retornoPedidoDTO, entidadesErro);
		} else {
			recebeuNfe = insereDadosNfeItemNfe(retornoPedidoDTO, entidadesErro, usaRetornoBackground, pedido);
			recebeuPedidoBoleto = insereDadosPedidoBoleto(retornoPedidoDTO, entidadesErro, usaRetornoBackground, pedido);
			recebeuNfce = insereDadosNfceItemNfce(retornoPedidoDTO, entidadesErro, pedido);
		}
		if ((!recebeuPedido) && (!recebeuPedidoBoleto && pedido.isGeraBoleto()) && (!recebeuNfe && pedido.isGeraNfe()) && (!recebeuNfce && pedido.getTipoPedido().isGeraNfce())) {
			throw new ApplicationException(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO);
		}
		if (entidadesErro.length() > 2) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_RECEBIMENTO_RETORNO_PEDIDO_ESPEC, entidadesErro.toString()));
		}
	}
	
	private boolean insereDadosPedidoErpEPedidoErpDif(RetornoPedidoDTO retornoPedidoDTO, StringBuffer entidadesErro) throws SQLException {
		boolean recebeuPedido = false;
		if (retornoPedidoDTO.getPedidoErp() != null) {
			Pedido pedido = new Pedido(retornoPedidoDTO.getPedidoErp());
			PedidoPdbxDao.getInstanceErp().insert(pedido);
			Vector itemPedidoErpList = pedido.itemPedidoList;
			int size = itemPedidoErpList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)itemPedidoErpList.items[i];
				ItemPedidoPdbxDao.getInstanceErp().insert(itemPedido);
				if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.isNotEmpty(itemPedido.itemPedidoGradeList)) {
					int sizeGrade = itemPedido.itemPedidoGradeList.size();
					for (int k = 0; k < sizeGrade; k++) {
						ItemPedidoGradeDbxDao.getInstanceErp().insert((ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[i]);
					}
				}
			}
			recebeuPedido = true;
		} else {
			entidadesErro.append("retorno de pedidos");
		}
		if (retornoPedidoDTO.getPedidoErpDif() != null) {
			PedidoErpDifDTO pedidoErpDif = retornoPedidoDTO.getPedidoErpDif();
			PedidoErpDifService.getInstance().insert(pedidoErpDif);
			int size = pedidoErpDif.itemPedidoErpDifList.length;
			if (pedidoErpDif.itemPedidoErpDifList != null && size > 0) {
				for (int i = 0; i < size; i++) {
					ItemPedidoErpDifService.getInstance().insert(pedidoErpDif.itemPedidoErpDifList[i]);
				}
			}
		}
		return recebeuPedido;
	}
	
	private boolean insereDadosNfeItemNfe(RetornoPedidoDTO retornoPedidoDTO, StringBuffer entidadesErro, boolean usaRetornoBackground, Pedido pedido) throws SQLException {
		boolean recebeuNfe = false;
		if (((!usaRetornoBackground && LavenderePdaConfig.usaRetornoAutomaticoDadosNfe) || (usaRetornoBackground && LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()))) {
			if (retornoPedidoDTO.getNfe() != null) {
				NfeDTO nfeDTO = retornoPedidoDTO.getNfe();
				Nfe nfe = new Nfe(nfeDTO);
				NfeDao.getInstance().insert(nfe);
				if (LavenderePdaConfig.usaNfePorReferencia) {
					if (ValueUtil.isNotEmpty(nfe.itemNfeList)) {
						int size = nfe.itemNfeList.size();
						for (int i = 0; i < size; i++) {
							ItemNfeReferenciaDao.getInstance().insert((ItemNfeReferencia)nfe.itemNfeList.items[i]);
						}
					}
				} else {
					if (ValueUtil.isNotEmpty(nfeDTO.itemNfeList)) {
						int size = nfe.itemNfeList.size();
						for (int i = 0; i < size; i++) {
							ItemNfeDbxDao.getInstance().insert((ItemNfe)nfe.itemNfeList.items[i]);
						}
					}
				}
				if (usaRetornoBackground) {
					NfeDao.houveRecebimentoNfeBackground = true;
				}
				recebeuNfe = true;
				BaseUIForm.btRecebeDadosBackgroundVisible = usaRetornoBackground;
			} else if (pedido.isGeraNfe()) {
				entidadesErro.append(entidadesErro.length() > 0 ? ", nota fiscal" : "nota fiscal");
			}
		}
		return recebeuNfe;
	}
	
	private boolean insereDadosNfceItemNfce(RetornoPedidoDTO retornoPedidoDTO, StringBuffer entidadesErro, Pedido pedido) throws SQLException {
		boolean recebeuNfce = false;
		if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfce) {
			if (retornoPedidoDTO.getNfce() != null) {
				NfceDTO nfceDTO = retornoPedidoDTO.getNfce();
				Nfce nfce = new Nfce(nfceDTO);
				NfceService.getInstance().insertOrUpdateRetornoNfce(nfce);
				if (ValueUtil.isNotEmpty(nfce.listItensNfce)) {
					int size = nfce.listItensNfce.size();
					for (int i = 0; i < size; i++) {
						ItemNfceService.getInstance().insertOrUpdateItemNfceRetorno((ItemNfce)nfce.listItensNfce.items[i]);
					}
				}
				recebeuNfce = true;
			} else {
				entidadesErro.append(entidadesErro.length() > 0 ? ", NFC-e" : "NFC-e");
			}
		}
		return recebeuNfce;
	}
	
	private boolean insereDadosPedidoBoleto(RetornoPedidoDTO retornoPedidoDTO, StringBuffer entidadesErro, boolean usaRetornoBackground, Pedido pedido) throws SQLException {
		boolean recebeuPedidoBoleto = false;
		if (((!usaRetornoBackground && LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto) || (usaRetornoBackground && LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()))) {
			if (ValueUtil.isNotEmpty(retornoPedidoDTO.getPedidoBoletoList())) {
				for (PedidoBoletoDTO pedidoBoletoDTO : retornoPedidoDTO.getPedidoBoletoList()) {
					PedidoBoletoDao.getInstance().insert(new PedidoBoleto(pedidoBoletoDTO));
				}
				if (usaRetornoBackground) {
					PedidoBoletoDao.houveRecebimentoBoletoBackground = true;
				}
				recebeuPedidoBoleto = true;
				BaseUIForm.btRecebeDadosBackgroundVisible = usaRetornoBackground;
			} else if (pedido.isGeraBoleto()) {
				entidadesErro.append(entidadesErro.length() > 0 ? ", boletos" : "boletos"); 
			}
		}
		return recebeuPedidoBoleto;
	}
	
	public void validatePedidoFechado(Pedido pedido) throws SQLException {
		PedidoService.getInstance().validaPedidoRelacionadoBonificacaoEmAberto(pedido);
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro()) {
			PedidoService.getInstance().findItemPedidoList(pedido);
			if ((SugestaoVendaService.getInstance().isValidaSugestaoVendaObrigatoriaMultiplasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true) && SugestaoVendaService.getInstance().isHasSugestoesObrigatoriasPendentesOutrasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true)) ||
			    (SugestaoVendaService.getInstance().isValidaSugestaoVendaObrigatoriaMultiplasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true) && SugestaoVendaService.getInstance().isHasSugestoesObrigatoriasPendentesOutrasEmpresas(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true))) {
				throw new SugestaoVendaPresenteEmOutrasEmpresasPedidoException(MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA_OUTRAS_EMPRESAS, pedido.getCliente().toString()));
			}
		}
		try {
			if (!PedidoService.getInstance().isIgnoraValidacaoVlMinPedido(pedido) && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
				PedidoService.getInstance().validaVlMinPedidosPorClienteAtingido(pedido, true);
			}
		} catch (Throwable e) {
			throw new ValidationException(Messages.VALOR_MINIMO_PEDIDO_NAO_ATINGIDO_ERRO);
		}
		PedidoService.getInstance().validaDataMinimaPrevEntregaParaEnvioDoPedido(pedido);
		CargaPedidoService.getInstance().validaCargaEnvioPedido(pedido);
		PedidoService.getInstance().validaCustoVencido(pedido);
		PedidoService.getInstance().validaPedidoComRestricaoDeItens(pedido);
		VerbaSaldoService.getInstance().validaVerbaAindaVigente(pedido);
	}
	
	
	private void aplicaDescCascataPorCategoria(Pedido pedido, boolean arredondaCadaAplicacao, boolean arredondaFinal, boolean skipValidation) throws SQLException {
		double newVlDesconto = 0;
		double vlFinalPedido = pedido.vlTotalPedido;
		try {
			if (arredondaCadaAplicacao) {
				double desc = ValueUtil.round((vlFinalPedido * pedido.vlPctDescCliente) / 100);
				if (!skipValidation) {
					validateDescCat1(pedido, vlFinalPedido);
				}
				newVlDesconto += desc;
				vlFinalPedido = ValueUtil.round(vlFinalPedido - desc);
				desc = ValueUtil.round((vlFinalPedido * pedido.vlPctDesc2) / 100);
				if (!skipValidation) {
					validateDescCat2(pedido, vlFinalPedido);
				}
				newVlDesconto += desc;
				vlFinalPedido = ValueUtil.round(vlFinalPedido - desc);
				desc = ValueUtil.round((vlFinalPedido * pedido.vlPctDesc3) / 100);
				if (!skipValidation) {
					validateDescCat3(pedido, vlFinalPedido);
				}
				pedido.oldVlPctCat1 = pedido.vlPctDescCliente;
				pedido.oldVlPctCat2 = pedido.vlPctDesc2;
				pedido.oldVlPctCat3 = pedido.vlPctDesc3;
				newVlDesconto += desc;
				vlFinalPedido = ValueUtil.round(vlFinalPedido - desc);
				if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && pedido.getCondicaoPagamento().vlIndiceFinanceiro > 0) {
					double pctDescCond = ValueUtil.round((1 - pedido.getCondicaoPagamento().vlIndiceFinanceiro) * 100);
					if (pctDescCond > 0) { 
						desc = ValueUtil.round((vlFinalPedido * pctDescCond) / 100);
						pedido.vlPctDescontoCondicao = pctDescCond;
						newVlDesconto += desc;
						vlFinalPedido = ValueUtil.round(vlFinalPedido - desc);
					}
				}
				pedido.vlTotalPedido = vlFinalPedido;
				pedido.vlDesconto = newVlDesconto;
			} else if (arredondaFinal) {
				double desc = (vlFinalPedido * pedido.vlPctDescCliente) / 100;
				if (!skipValidation) {
					validateDescCat1(pedido, vlFinalPedido);
				}
				newVlDesconto += desc;
				vlFinalPedido -= desc;
				desc = (vlFinalPedido * pedido.vlPctDesc2) / 100;
				if (!skipValidation) {
					validateDescCat2(pedido, vlFinalPedido);
				}
				newVlDesconto += desc;
				vlFinalPedido -= desc;
				desc = (vlFinalPedido * pedido.vlPctDesc3) / 100;
				if (!skipValidation) {
					validateDescCat3(pedido, vlFinalPedido);
				}
				pedido.oldVlPctCat1 = pedido.vlPctDescCliente;
				pedido.oldVlPctCat2 = pedido.vlPctDesc2;
				pedido.oldVlPctCat3 = pedido.vlPctDesc3;
				newVlDesconto += desc;
				vlFinalPedido -= desc;
				if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido()) {
					double pctDescCond = ValueUtil.round((1 - pedido.getCondicaoPagamento().vlIndiceFinanceiro) * 100);
					if (pctDescCond > 0) {
						desc = (vlFinalPedido * pctDescCond) / 100;
						pedido.vlPctDescontoCondicao = pctDescCond;
						newVlDesconto += desc;
						vlFinalPedido -= desc;
					}
				}
				vlFinalPedido = ValueUtil.round(vlFinalPedido);
				pedido.vlTotalPedido = vlFinalPedido;
				pedido.vlDesconto = newVlDesconto;
			}
		} catch (DescontoCategoriaException e) {
			pedido.vlPctDescCliente = pedido.oldVlPctCat1;
			pedido.vlPctDesc2 = pedido.oldVlPctCat2;
			pedido.vlPctDesc3 = pedido.oldVlPctCat3;
			aplicaDescCascataPorCategoria(pedido, arredondaCadaAplicacao, arredondaFinal, true);
			throw e;
		}
	}

	private void validateDescCat3(Pedido pedido, double vlFinalPedido) throws SQLException {
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ESPECIAL, pedido.vlPctDesc3, false);
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ATACADO, pedido.vlPctDesc3, true);
	}

	private void validateDescCat2(Pedido pedido, double vlFinalPedido) throws SQLException {
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ESPECIAL, pedido.vlPctDesc2, true);
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ATACADO, pedido.vlPctDesc2, false);
	}

	private void validateDescCat1(Pedido pedido, double vlFinalPedido) throws SQLException {
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ESPECIAL, pedido.vlPctDescCliente, false);
		validateMaxDescCategoria(pedido, vlFinalPedido, Categoria.TIPO_CATEGORIA_ATACADO, pedido.vlPctDescCliente, false);
	}

	private void validateMaxDescCategoria(Pedido pedido, double vlFinalPedido, String categoria, double vlPctDesc, boolean campoProprio) throws SQLException {
		double oldPctDesc = Categoria.TIPO_CATEGORIA_ESPECIAL.equals(categoria) ? pedido.oldVlPctCat2 : Categoria.TIPO_CATEGORIA_ATACADO.equals(categoria) ? pedido.oldVlPctCat3 : 0;
		if (CategoriaService.getInstance().isCategoriaDisponivelPorValorMinimo(pedido, pedido.getCliente().getCategoria(), categoria)) {
			double maxDescCat = CategoriaService.getInstance().getVlMaxDescCategoria(vlFinalPedido, pedido.getCliente().getCategoria(), categoria, true, pedido.getCondicaoPagamento().vlIndiceFinanceiro);
			if ((campoProprio || oldPctDesc > 0) && maxDescCat > 0 && vlPctDesc > maxDescCat) {
				String msgParam = Categoria.TIPO_CATEGORIA_ESPECIAL.equals(categoria) ? Categoria.TEXT_SEGUNDO_DESC_CAT : Categoria.TEXT_TERCEIRO_DESC_CAT;
				throw new DescontoCategoriaException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_DESC_CATEGORIA, new String[]{msgParam, StringUtil.getStringValueToInterface(maxDescCat)}), categoria);
			}
		}
	}
	
	public void cancelaOrcamento(Pedido pedido) throws SQLException {
		efetuaCancelamentoPedido(null, pedido);
		pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_ALTERADO;
		pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_NAO);
		updatePedidoOrcamento(pedido);
	}
	
	protected void setDadosAlteracao(BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido) domain;
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			if (!pedido.onFechamentoPedido) {
				pedido.cdUsuario = Session.getCdUsuario();
				pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_ALTERADO_ORCAMENTO;
			} else {
				super.setDadosAlteracao(domain);
			}
		}
	}
	
	public boolean existePedidoParaNovoCliente(NovoCliente novoClienteFilter) throws SQLException {
		Pedido pedido = new Pedido();
		pedido.cdCliente = novoClienteFilter.nuCnpj;
		pedido.cdEmpresa = novoClienteFilter.cdEmpresa;
		pedido.cdRepresentante = novoClienteFilter.cdRepresentante;
		return countByExample(pedido) > 0;
	}

	public void updatePedidoDiferenca(Pedido pedido, String flPedidoDiferenca) throws SQLException {
		if (LavenderePdaConfig.geraNovoPedidoDiferencas && pedido.isGeradoDiferenca()) {
			Pedido pedidoErpFilter = new Pedido();
			pedidoErpFilter.cdEmpresa = pedido.cdEmpresa;
			pedidoErpFilter.cdRepresentante = pedido.cdRepresentante;
			pedidoErpFilter.nuPedido = pedido.nuPedidoDiferenca;
			pedidoErpFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
			pedidoErpFilter.flPedidoDiferenca = flPedidoDiferenca;
			PedidoPdbxDao.getInstance().updatePedidoDiferenca(pedidoErpFilter);
		}
	}
	
	public BaseDomain findPedidoComplementarByPedidoInPdaAndErp(Pedido pedido) throws SQLException {
        Pedido pedidoFilter = new Pedido();
        pedidoFilter.cdEmpresa = pedido.cdEmpresa;
        pedidoFilter.cdRepresentante = pedido.cdRepresentante;
        pedidoFilter.nuPedido = pedido.nuPedidoComplementado;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		Pedido pedidoRetorno = (Pedido) PedidoService.getInstance().findByRowKeyDyn(pedidoFilter.getRowKey());
		if (pedidoRetorno == null) {
			pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
			pedidoRetorno = (Pedido) PedidoService.getInstance().findByRowKeyDyn(pedidoFilter.getRowKey());
			if (pedidoRetorno == null) {
				return new Pedido();
			}
		}
		return pedidoRetorno;
	}

	public int atualizaStatusPedido(Pedido pedido, String cdStatusAtualizacao, boolean addFlagEnvioFilter) throws SQLException {
		return ((PedidoPdbxDao)getCrudDao()).updateStatusReabrirPedido(pedido.getRowKey(), cdStatusAtualizacao, addFlagEnvioFilter);
	}

	public void updateIndicesFinanceirosPedido(double vlTotalVerbaPedido, Pedido pedido) throws SQLException {
		PedidoPdbxDao.getInstance().updateIndicesFinanceirosPedido(vlTotalVerbaPedido, pedido);
	}
	
	public boolean existeItensSugestaoComboNoPedido(Pedido pedido) throws SQLException {
		ItemPedido filter = new ItemPedido();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.nuPedido = pedido.nuPedido;
		filter.flOrigemPedido = pedido.flOrigemPedido;
		return ItemPedidoPdbxDao.getInstance().countItensPertencentesCombo(filter) > 0;
	}
	
	public int countPedidosNaoTransmitidos() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
		return countByExample(pedidoFilter);
	}

	public int countPedidosNaoTransmitidosFechamentoDiario() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return PedidoPdbxDao.getInstance().countPedidosNaoTransmitidosFechamentoDiario(pedidoFilter);
	}
	
	public Vector getTiposMensagensAposFechamentoPedidoPelaLista(Vector pedidoList) {
		int maxSize = LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() ? 1 : 0;
		maxSize = LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo ? maxSize + 1 : maxSize;
		Vector messageTypeList = new Vector(maxSize);
			int size = pedidoList.size();
		Pedido pedido; 
			for (int i = 0; i < size; i++) {
			if (messageTypeList.size() == maxSize) {
				break;
				}
			pedido = (Pedido) pedidoList.elementAt(i);
			if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && pedido.isPedidoItemPendente() && !messageTypeList.contains(1)) {
				messageTypeList.addElement(1);
			}
			if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && pedido.totalmenteConvertidoPedidoBonificacao && !messageTypeList.contains(2)) {
				messageTypeList.addElement(2);
		}
	}
		return messageTypeList;
	}
	
	public void updateStatusPedido(Pedido pedido) throws SQLException {
		if (pedido != null) {
			updateColumnErp(pedido.rowKey, Pedido.NMCOLUNA_CDSTATUSPEDIDO, pedido.cdStatusPedido, Types.VARCHAR);
		}
	}
	
	public Vector getItensListApenasConsumoGeradoVerba(Pedido pedido) {
		Vector itensComConsumoGeradoVerba = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (itemPedido.vlVerbaItem < 0 || itemPedido.vlVerbaItemPositivo > 0) {
				itensComConsumoGeradoVerba.addElement(itemPedido);
			}
		}
		return itensComConsumoGeradoVerba;
	}
	
	public Vector getItensListSemEstoqueEstoqueInsuficiente(Pedido pedido) throws SQLException {
		Vector itensSemEstoque = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (ProdutoService.getInstance().produtoSemEstoque(itemPedido.getProduto(), itemPedido.getCdLocalEstoque())) {
				itensSemEstoque.addElement(itemPedido);
			} else if (itemPedido.getQtItemFisico() > itemPedido.getQtItemEstoquePositivo()){
				itensSemEstoque.addElement(itemPedido);
			}
		}
		return itensSemEstoque;
	}
	
	public void validateCondPagtoPorCliente(String condPagtoBloqueadaCliente, String condPagtoPedido) throws SQLException {
		String[] listForn = StringUtil.split(condPagtoBloqueadaCliente, ';');
		Vector vector = new Vector(listForn);
		if (vector.size() > 0) {
			for (int i = 0; i < vector.size(); i++) {
				if (ValueUtil.valueEquals(condPagtoPedido, listForn[i])) {
					throw new ValidationException(Messages.PEDIDO_ERRO_COND_PAGTO_CLIENTE_BLOQUEADA);
				}
			}
		}
	}
	
	public void atualizaDadosGradeParaEmissaoRel(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedidoGradeService.getInstance().findItemPedidoGradeListWithGradeDates((ItemPedido) pedido.itemPedidoList.items[i]);
		}
	}
	
	public void calculaFreteTransportadoraCep(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if(tipoPedido == null || tipoPedido.isIgnoraCalculoFrete()) return;
		TipoFrete tipoFrete = TipoFreteService.getInstance().getTipoFrete(pedido.cdTipoFrete, pedido.getCliente().cdEstadoComercial);	
		if (pedido.transportadoraCep == null) {
			pedido.vlPctFrete = EmpresaService.getInstance().findVlPctFreteByCdEmpresa(pedido.cdEmpresa);
			pedido.vlFrete = ValueUtil.round(pedido.getVlTotalPedidoNaoNeutro() * pedido.vlPctFrete / 100);
			pedido.vlFreteTotal = ValueUtil.round(pedido.vlTotalPedido * pedido.vlPctFrete / 100);
		} else if (tipoFrete.isCalculaFrete()) {
			pedido.vlFrete = ValueUtil.round(pedido.getVlTotalPedidoNaoNeutro() * pedido.transportadoraCep.vlPctFrete / 100 + pedido.transportadoraCep.vlTaxaColeta);
			pedido.vlFreteTotal = ValueUtil.round(pedido.vlTotalPedido * pedido.transportadoraCep.vlPctFrete / 100 + pedido.transportadoraCep.vlTaxaColeta);
			pedido.vlFrete = pedido.vlFreteTotal > pedido.transportadoraCep.vlMinFrete ? pedido.vlFrete : pedido.transportadoraCep.vlMinFrete + pedido.transportadoraCep.vlTaxaColeta;
			pedido.vlFreteTotal = pedido.vlFreteTotal > pedido.transportadoraCep.vlMinFrete ? pedido.vlFreteTotal : pedido.transportadoraCep.vlMinFrete + pedido.transportadoraCep.vlTaxaColeta;
		} else {
			pedido.vlFrete = 0;
			pedido.vlFreteTotal = 0;
		}
	}

	public int recalculaValorPedido() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdStatusPedidoFilter = LavenderePdaConfig.permiteReabrirPedidoFechado ? new String[] {LavenderePdaConfig.cdStatusPedidoAberto, LavenderePdaConfig.cdStatusPedidoFechado} : new String[] {LavenderePdaConfig.cdStatusPedidoAberto};
		Vector pedidoList = findAllByExample(pedidoFilter);
		int size = pedidoList.size();
		if (size == 0) return 0;
		
		int qtdPedidosRecalculos = 0;
		boolean recalculaNoInicioDoMes = recalculaNoInicioDoMes();
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (recalculaNoInicioDoMes || recalculaPorNuDiasAposEmissao(pedido.dtEmissao, pedido.dtUltimoRecalculoValores) || recalculaPorNuDiasAposUltimoRecalculo(pedido.dtUltimoRecalculoValores)) {
				if (pedido.isPedidoFechado()) {
					reabrirPedido(pedido);
				} else {
					findItemPedidoList(pedido);
				}
				atualizaPedido(pedido);
				qtdPedidosRecalculos++;
			}
		}
		return qtdPedidosRecalculos;
		
	}
	
	public void atualizaPedido(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		boolean recalculouSucessoAlgumItem = false;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			boolean recalculouSucessoItem = recalculaItemPedido(itemPedido);
			recalculouSucessoAlgumItem = recalculouSucessoItem ? true : recalculouSucessoAlgumItem;  
		}
		if (recalculouSucessoAlgumItem) {
			calculate(pedido);
			pedido.dtUltimoRecalculoValores = DateUtil.getCurrentDate();
			PedidoService.getInstance().update(pedido);
		} else {
			pedido.dtUltimoRecalculoValores = DateUtil.getCurrentDate();
			PedidoService.getInstance().updateColumn(pedido.getRowKey(), "DTULTIMORECALCULOVALORES", pedido.dtUltimoRecalculoValores, Types.DATE);
		}
	}
	
	protected boolean recalculaItemPedido(ItemPedido itemPedido) throws SQLException {
		try {
			double vlPctDesconto = itemPedido.vlPctDesconto;
			double vlPctAcrescimo = itemPedido.vlPctAcrescimo;
			Pedido pedido = itemPedido.pedido;
			loadValorBaseItemPedido(pedido, itemPedido);
			itemPedido.vlPctDesconto = vlPctDesconto;
			itemPedido.vlPctAcrescimo = vlPctAcrescimo;
			if (vlPctDesconto > 0) {
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			} else if (vlPctAcrescimo > 0) {
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
			} else {
				itemPedido.flTipoEdicao = 0;
			}
			CalculaEmbalagensService.getInstance().calculaDescQtdEmbalagemCompleta(itemPedido, false);
			ItemPedidoService.getInstance().calculate(itemPedido, pedido);
			if (!itemPedido.pedido.onReplicacao) ItemPedidoService.getInstance().update(itemPedido);
			return true;
		} catch (Throwable e) {
			itemPedido.flErroRecalculo = ValueUtil.VALOR_SIM;
			ItemPedidoService.getInstance().updateColumn(itemPedido.getRowKey(), "FLERRORECALCULO", ValueUtil.VALOR_SIM, Types.VARCHAR);
		}
		return false;
	}
	
	private Date dataCalculada(Date data, int nuDias) throws SQLException {
		int nuDiasAvancar = 0;
		do {
			data.advance(1);
			if (DateUtil.DATA_SEMANA_DOMINGO == data.getDayOfWeek() || FeriadoService.getInstance().isDtFeriado(data)) {
				continue;
			}
			nuDiasAvancar++;
		} while (nuDias != nuDiasAvancar);
		return data;
	}
	
	private Vector getDiasUteisMesConsiderandoSabado() throws SQLException {
		Date data = DateUtil.getCurrentDate();
    	Vector result = new Vector();
    	Date dataInicial = data;
    	dataInicial = DateUtil.getDateValue(1, dataInicial.getMonth(), dataInicial.getYear());
    	Date dtFinal = data;
    	dtFinal = DateUtil.getDateValue(dtFinal.getDaysInMonth(), dtFinal.getMonth(), dtFinal.getYear());
    	Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dataInicial));
    	while (!dtBase.isAfter(dtFinal)) {
    		if ((dtBase.getDayOfWeek() != 0) && !FeriadoService.getInstance().isDtFeriado(dtBase)) {
    			result.addElement(DateUtil.getDateValue(StringUtil.getStringValue(dtBase)));
    		}
    		DateUtil.addDay(dtBase, 1);
    	}
    	return result;
    }
	
	private boolean recalculaPorNuDiasAposEmissao(Date dtEmissao, Date dtUltimoRecalculoValores) throws SQLException {
		if (LavenderePdaConfig.nuDiasAposEmissaoRecalValPed > 0 && ValueUtil.isEmpty(dtUltimoRecalculoValores)) {
			Date dataCalculada = dataCalculada(DateUtil.getDateValue(dtEmissao), LavenderePdaConfig.nuDiasAposEmissaoRecalValPed) ;
			return DateUtil.getCurrentDate().isAfter(dataCalculada) || ValueUtil.valueEquals(DateUtil.getCurrentDate(), dataCalculada);
		}
		return false;
	}
	
	private boolean recalculaPorNuDiasAposUltimoRecalculo(Date dtUltimoRecalculoValores) throws SQLException {
		if (LavenderePdaConfig.nuDiasAposUltimoRecalculoValPed > 0 && ValueUtil.isNotEmpty(dtUltimoRecalculoValores)) {
			Date dataCalculada = dataCalculada(DateUtil.getDateValue(dtUltimoRecalculoValores), LavenderePdaConfig.nuDiasAposUltimoRecalculoValPed)  ;
			return DateUtil.getCurrentDate().isAfter(dataCalculada) || ValueUtil.valueEquals(DateUtil.getCurrentDate(), dataCalculada);
		}
		return false;
	}
	
	private boolean recalculaNoInicioDoMes() throws SQLException {
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidosInicioDoMes) {
			String dataHoraUltRecebimentoString = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraUtimoRecebimentoDados);
			Vector diasUteisMesList = getDiasUteisMesConsiderandoSabado();
			Date dataAtual =  DateUtil.getCurrentDate();
			if (ValueUtil.isEmpty(dataHoraUltRecebimentoString) && diasUteisMesList.contains(dataAtual)) return true;
			
			Date dataHoraUltRecebimento = null;
			try {
				dataHoraUltRecebimento = new Date(TimeUtil.getTime(dataHoraUltRecebimentoString));
			Date dataPrimeiroDiaUtilMes = DateUtil.getFirstUtilDayOfMonth(dataAtual);
			if (ValueUtil.isNotEmpty(dataHoraUltRecebimento)) {
					if (dataHoraUltRecebimento.isAfter(dataPrimeiroDiaUtilMes) || ValueUtil.valueEquals(dataAtual, dataHoraUltRecebimento))
						return false;
			}
			} catch (InvalidDateException e) {
				ExceptionUtil.handle(e);
			}
			return diasUteisMesList.contains(dataAtual);
		}
		return false;
	}
	
	private void validaRecalculoPedidoNoFechamento(Pedido pedido) throws SQLException {
		if (necessarioRecalculoPedido(pedido)) {
			throw new RecalculoPedidoException(Messages.NECESSARIO_RECALCULO_PEDIDO_PARA_FECHAMENTO);
		}
	}

	public boolean necessarioRecalculoPedido(Pedido pedido) throws SQLException {
		if ((pedido.isPedidoAberto() || pedido.isPedidoFechado()) && pedido.itemPedidoList.size() > 0) {
			return recalculaPorNuDiasAposEmissao(pedido.dtEmissao, pedido.dtUltimoRecalculoValores) || recalculaPorNuDiasAposUltimoRecalculo(pedido.dtUltimoRecalculoValores) || recalculaNoInicioDoMes();
		}
		return false;
	}

	private boolean hasItensErroRecalculo(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			if (((ItemPedido) itemPedidoList.items[i]).isErroRecalculo()) return true;
		}
		return false;
	}

	public Vector filtraPedidosComRecalculoPendente(Vector pedidoList) throws SQLException {
		Vector newPedidoList = new Vector();
		if (ValueUtil.isEmpty(pedidoList)) return newPedidoList;
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (necessarioRecalculoPedido(pedido)) {
				newPedidoList.addElement(pedido);
			}
		} 
		return newPedidoList;
	}
	
	public Pedido calculoRentabilidadeEmMemoria(Pedido pedidoOriginal, ItemPedido itemPedido) throws SQLException {
		Pedido pedidoCopia = (Pedido) pedidoOriginal.clone();
		pedidoCopia.itemPedidoList = new Vector();
		Vector itemPedidoList = pedidoOriginal.itemPedidoList;
		if (itemPedidoList.size() > 0) {
			populaItensInseridosNoPedidoEmMemoria(itemPedido, pedidoCopia, itemPedidoList);
		} 
		pedidoCopia.itemPedidoList.addElement(itemPedido);
		pedidoCopia.vlTotalPedido += itemPedido.vlTotalItemPedido;
		pedidoCopia.vlTotalItens  += itemPedido.vlTotalItemPedido;
		calculaFreteTransportadoraCep(pedidoCopia);
		calculateRentabilidadePedido(pedidoCopia);
		return pedidoCopia;
	}

	private void populaItensInseridosNoPedidoEmMemoria(ItemPedido itemPedido, Pedido pedidoCopia, Vector itemPedidoOriginalList) {
		int size = itemPedidoOriginalList.size();
		pedidoCopia.vlTotalPedido = 0;
		pedidoCopia.vlTotalItens = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoOriginal = (ItemPedido) itemPedidoOriginalList.items[i];
			if (ValueUtil.valueEquals(itemPedidoOriginal, itemPedido)) continue;
			
			pedidoCopia.itemPedidoList.addElement(itemPedidoOriginal);
			pedidoCopia.vlTotalPedido += itemPedidoOriginal.vlTotalItemPedido;
			pedidoCopia.vlTotalItens +=  itemPedidoOriginal.vlTotalItemPedido;
		}
	}
	
	public Vector findAllPedidosDaRede(Pedido pedido) throws SQLException {
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosDaRede(pedido);
		return pedidoList;
	}
	
	public void atualizaDescontosVolumeVendaRede(Vector pedidoRedeList) throws SQLException {
		int size = pedidoRedeList.size();
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoRedeList.elementAt(i);
			findItemPedidoList(pedido);
			pedido.onFechamentoPedido = true;
			reajustaVlItensMudancaFaixaDescontoVolMensalRede(pedido);
			update(pedido);
			pedido.onFechamentoPedido = false;
		}
	}

	public Vector getAllItensFromPedidoWithGradeInconsistences(Pedido pedido) throws SQLException {
		Vector itemPedidoInconsistentes = new Vector();
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!ValueUtil.valueEquals(ItemGrade.ITEMGRADELIST_SEM_PROBLEMA_COM_GRADES, ItemPedidoGradeService.getInstance().verificaInconsistenciasGrade(itemPedido))) {
				itemPedidoInconsistentes.addElement(itemPedido);
			}
		}
		return itemPedidoInconsistentes;
	}

	public void criaNotaCreditoPedido(Vector notaCreditoSelecionadaList, Pedido pedido) {
		int size = notaCreditoSelecionadaList.size();
		for (int i = 0; i < size; i++) {
			NotaCredito notaCredito = (NotaCredito) notaCreditoSelecionadaList.items[i];
			pedido.notaCreditoPedidoList.addElement(new NotaCreditoPedido(pedido, notaCredito.cdNotaCredito));
			pedido.vlTotalNotaCredito += notaCredito.vlNotaCredito;
		}
	}
	
	public void deletaNotasCredito(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			NotaCreditoPedidoService.getInstance().deleteByPedido(pedido);
			NotaCreditoService.getInstance().atualizaNotaCreditoUtilizada(pedido.notaCreditoPedidoList, pedido.cdCliente, ValueUtil.VALOR_NAO);
		}
	}
	
	public double somaVlTotalNotaCredito(String cdEmpresa, String cdRepresentante, Date dtEmissaoInicial, Date dtEmissaoFinal) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cdEmpresa;
		pedidoFilter.cdRepresentante= cdRepresentante;
		pedidoFilter.dtEmissaoInicialFilter= dtEmissaoInicial;
		pedidoFilter.dtEmissaoFinalFilter = dtEmissaoFinal;
		pedidoFilter.flFiltraPedidosDifAbertosCancelados = true;
		return sumByExample(pedidoFilter, "VLTOTALNOTACREDITO");
	}
	
	public double somaVlTotalDesconto(String cdEmpresa, String cdRepresentante, Date dtEmissaoInicial, Date dtEmissaoFinal) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cdEmpresa;
		pedidoFilter.cdRepresentante= cdRepresentante;
		pedidoFilter.dtEmissaoInicialFilter= dtEmissaoInicial;
		pedidoFilter.dtEmissaoFinalFilter = dtEmissaoFinal;
		pedidoFilter.flFiltraPedidosDifAbertosCancelados = true;
		pedidoFilter.filtraApenasPedidosComDescontoFinanceiro = true;
		return sumByExample(pedidoFilter, "VLDESCONTOINDICEFINANCLIENTE");
	}
	
	
	public void validateTabelaPreco(Pedido pedido) throws SQLException {
		if (pedido == null) return;
		TabelaPreco tabPreco = pedido.getTabelaPreco();
		if (tabPreco == null) return;
		
		boolean isUsaPrecoItemPorPesoMinimoItemPedido = LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoItemPedido();
		if (LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoPedido() && !isUsaPrecoItemPorPesoMinimoItemPedido && pedido.qtPeso < tabPreco.qtPesoMin) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_NAO_ATINGIDO_TABELA_PRECO, new Object[]{StringUtil.getStringValueToInterface(tabPreco.qtPesoMin), StringUtil.getStringValueToInterface(pedido.qtPeso)}));
		}
		if (!validaItemPedidoMinimoPesoQtd(isUsaPrecoItemPorPesoMinimoItemPedido)) return;
		double pesoPedido = ValueUtil.round(pedido.vlTotalPedido / pedido.qtPeso);
		boolean isQtItemListaInvalidaTabPreco = isQtItemListaInvalidaTabPreco(pedido, tabPreco);
		double numItensLista = (ValueUtil.isNotEmpty(pedido.itemPedidoList)) ? pedido.itemPedidoList.size() : 0;
		Object[] params = new Object[]{StringUtil.getStringValueToInterface(tabPreco.qtMinPedido), StringUtil.getStringValueToInterface(tabPreco.qtPesoMin), StringUtil.getStringValueToInterface(numItensLista), StringUtil.getStringValueToInterface(pesoPedido)};
		if (pesoPedido < tabPreco.qtPesoMin && isQtItemListaInvalidaTabPreco) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_QUANTIDADE_E_PESO_MINIMO_NAO_ATINGIDO_TABELA_PRECO, params));
		} else {
			if (pesoPedido < tabPreco.qtPesoMin) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_PESOCALCULADOMINIMO_NAO_ATINGIDO_TABELA_PRECO, new Object[]{StringUtil.getStringValueToInterface(tabPreco.qtPesoMin), StringUtil.getStringValueToInterface(pesoPedido)}));
			}	
			if (isQtItemListaInvalidaTabPreco) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_QUANTIDADE_MINIMA_NAO_ATINGIDO_TABELA_PRECO, new Object[]{StringUtil.getStringValueToInterface(tabPreco.qtMinPedido), StringUtil.getStringValueToInterface(numItensLista)}));
			}
		}
	}
	
	private boolean isQtItemListaInvalidaTabPreco(Pedido pedido, TabelaPreco tabPreco) {
		try {
			if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
				PedidoService.getInstance().findItemPedidoList(pedido);
			}
			double numItensLista = (ValueUtil.isNotEmpty(pedido.itemPedidoList)) ? pedido.itemPedidoList.size() : 0;
			return numItensLista < tabPreco.qtMinPedido;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			UiUtil.showErrorMessage(Messages.PROBLEMA_VALIDA_PESO_TABPRECO);
			return true;
		}
	}

	private boolean validaItemPedidoMinimoPesoQtd(boolean isUsaPrecoItemPorPesoMinimoItemPedido) {
		return LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido() && !isUsaPrecoItemPorPesoMinimoItemPedido && LavenderePdaConfig.usaQtMinProdutoPorTabelaPreco == 1;
	}
	
	public void updateCancelarPedidoFechadoOffline(Pedido pedido) throws SQLException {
		((PedidoPdbxDao)getCrudDao()).updateCancelarPedidoFechadoOffline(pedido);
	}

	public void updateProtocolo(Pedido pedido) throws SQLException {
		PedidoPdbxDao.getInstance().updateProtocolo(pedido);
	}
	
	public boolean isExistsPedidoErpCancelado(String cdEmpresa, String cdRepresentante, String nuPedido) throws SQLException {
		Pedido filter = new Pedido();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = cdRepresentante;
		filter.nuPedido = nuPedido;
		filter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
		Pedido pedido = (Pedido)findByRowKey(filter.getRowKey());
		return pedido != null && LavenderePdaConfig.cdStatusPedidoCancelado.equals(pedido.cdStatusPedido);
	}
	
	public double getVlTotalDespesaAcessoria(Vector itemPedidoList) {
		double vlTotalDespesaAcessoria = 0;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalDespesaAcessoria += itemPedido.vlDespesaAcessoria * itemPedido.getQtItemFisico();
		}
		return vlTotalDespesaAcessoria;
	}
	
	public void loadVlFreteDestacadoPedido(Pedido pedido) {
		double vlTotalPedidoFreteTributos = 0d;
		double vlTotalFrete = 0d;
		int size = pedido.itemPedidoList != null ? pedido.itemPedidoList.size() : 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			vlTotalPedidoFreteTributos += itemPedido.vlTotalItemFreteTributacao;
			vlTotalFrete += itemPedido.vlTotalItemPedidoFrete;
		}
		pedido.vlTotalPedidoFreteTributos = vlTotalPedidoFreteTributos;
		pedido.vlTotalFretePedido = vlTotalFrete;
	}
	
	public double getVlTotalPedidoComFreteEmbutidoETributos(Pedido pedido) {
		int size = pedido.itemPedidoList != null ? pedido.itemPedidoList.size() : 0;
		double vlTotalTributos = 0d;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlTotalTributos += itemPedido.vlTotalIpiItem + itemPedido.vlTotalStItem; 
		}
		return pedido.vlTotalPedido + vlTotalTributos;
	}

	public void resetFlProcessandoNfeTxt() {
		((PedidoPdbxDao)getCrudDao()).resetFlProcessandoNfeTxt();
	}
	
	public boolean permitidoRelacionamento(Pedido pedido, Vector pedidos) {
		if (!LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() || DateUtil.getDaysBetween(new Date(), pedido.dtEmissao) > LavenderePdaConfig.getDiasLimiteMultiplasTrocasBonificacoes())
			return false;
		
		int qtPedidosRelacionados = 0;
		for (int i = 0; i < pedidos.size(); i++) {
			Pedido pedidoRelacionado = (Pedido) pedidos.elementAt(i);
			if (pedido.nuPedido != pedidoRelacionado.nuPedido && (ValueUtil.valueEquals(pedido.nuPedido, pedidoRelacionado.nuPedidoRelBonificacao) || ValueUtil.valueEquals(pedido.nuPedido, pedidoRelacionado.nuPedidoRelTroca))) {
				qtPedidosRelacionados++;
			}
		}
		int limiteRelacionamentos = LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() ? LavenderePdaConfig.getQtdeMultiplasTrocasBonificacoes() : 1;
		return qtPedidosRelacionados < limiteRelacionamentos;
	}
	
	public boolean adicionaPedidoListaPedidosRelacionados(Pedido pedido, Vector listPedido) {
		if (LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes()) {
			return permitidoRelacionamento(pedido, listPedido);
		}
		return ValueUtil.isEmpty(pedido.nuPedidoRelBonificacao) && ValueUtil.isEmpty(pedido.nuPedidoRelTroca);
	}

	public Vector getListPedidoSortedByPctRent(Vector listTemp, String sortAttribute, boolean sortAsc) throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			int size = listTemp.size();
			for (int i = 0; i < size; i++) {
				PedidoService.getInstance().findItemPedidoList((Pedido) listTemp.items[i]);
			}
		}
		if (ValueUtil.valueEquals(sortAttribute, "VLRENTABILIDADE")) {
			listTemp = orderByPctRentabilidade(listTemp, sortAsc);
		}
		return listTemp;
	}
	
	private Vector orderByPctRentabilidade(Vector pedidoList, boolean sortAsc) throws SQLException {
    	int size = pedidoList.size();
    	for (int i = 0; i < size; i++) {
    		Pedido pedido = (Pedido) pedidoList.items[i];
			PedidoService.getInstance().findItemPedidoList(pedido);
		}
    	SortUtil.qsortDouble(pedidoList.items, 0, size - 1, sortAsc);
    	return pedidoList;
    }
	
	private void validatePedidoComItemBonificado(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado()) return;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.isItemBonificacao()) continue;
			throw new MarcaPendenteItemBonificacaoException(Messages.PEDIDO_MARCA_PENDENTE_ITEM_BONIFICADO);
		}
	}

	private void updatePedidoPendenteComItemBonificado(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado()) return;
		pedido.flPendente = ValueUtil.VALOR_NAO;
		updateFlPendente(pedido, ValueUtil.VALOR_NAO);
		pedido.dsMotivoBonificacao = ValueUtil.VALOR_NI;
		updateColumn(pedido.getRowKey(), "DSMOTIVOBONIFICACAO", pedido.dsMotivoBonificacao, Types.VARCHAR);
	}
	
	public double getVlPedidoPositivoAtual(final Pedido pedido) {
		double vlVerbaPos = 0d;
		ItemPedido item;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++)  {
			item = (ItemPedido) pedido.itemPedidoList.items[i];
			vlVerbaPos += item.vlVerbaItemPositivo;
		}
		return vlVerbaPos;
	}
	
	public boolean validateUsaPedidoProducao(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.getSementeSenhaPedidoProducao() == 0) {
			throw new ValidationException(Messages.PEDIDO_VENDAPRODUCAO_ERRO_NENHUM_PEDIDOPRODUCAO_SELECIONADO);
		} else {
			AdmSenhaDinamicaWindow senha = new AdmSenhaDinamicaWindow();
			senha.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_RELACIONAR_PEDIDO_PRODUCAO);
			senha.setMensagem(Messages.PEDIDO_VENDAPRODUCAO_ERRO_NENHUM_PEDIDOPRODUCAO_SELECIONADO_SENHA);
			if (senha.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) return false;
			SessionLavenderePda.liberadoPorSenhaRelacionarPedidoProducao = true;
			return true;
		}
	}
	
	public double getValorParcela(Pedido pedido) throws SQLException {
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		int nuParcelas = (condicaoPagamento == null) || (condicaoPagamento.nuParcelas == 0) ? 1 : condicaoPagamento.nuParcelas;
		Transportadora transportadora = pedido.transportadora;
		double redutor = 0;
		if (transportadora != null && transportadora.isFlSomaFrete()) {
			redutor = pedido.vlFrete;
		}
		return (TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido) - redutor) / nuParcelas;
	}

	public void updatePontuacaoPedido(Pedido pedido) throws SQLException {
		PedidoPdbxDao.getInstance().updatePontuacaoPedido(pedido);
	}

	public boolean validaUsuarioEmissaoPedido() {
		if (!SessionLavenderePda.hasPreferencia(PreferenciaFuncao.BLOQUEIA_EMISSAO_PEDIDO_SMARTPHONE)) return true;
		UiUtil.showErrorMessage(Messages.USUARIO_BLOQUEADO_NOVO_PEDIDO_FUNCAO);
		return false;
	}
	

	public void reprocessaPedidoFechadoSemNfe() {
		try {
			List<Pedido> pedidoSemNfeList = PedidoService.getInstance().findPedidosFechadosSemNfe(false);
			if (pedidoSemNfeList.size() == 0) {
				pedidoSemNfeList = PedidoService.getInstance().findPedidosFechadosSemNfe(true);
				if (pedidoSemNfeList.size() > 0) {
					for (Pedido pedido : pedidoSemNfeList) {
						PedidoService.getInstance().atualizaStatusNfeFinalizado(pedido);
					}
				}
			} else {
				UiUtil.showWarnMessage(Messages.AVISO_PEDIDOS_FECHADOS_SEM_NFE);
				List<String> pedidosReprocessadosList = new ArrayList<>();
				for (Pedido pedido : pedidoSemNfeList) {
					pedido.itemPedidoList = ItemPedidoService.getInstance().findItemPedidoByPedido(pedido);
					NfeService.getInstance().geraNfe(pedido, false);
					PedidoService.getInstance().atualizaStatusNfeFinalizado(pedido);
					pedidosReprocessadosList.add(pedido.nuPedido);
				}
				if (pedidosReprocessadosList.size() > 0)
					UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.INFO_PEDIDOS_COM_NFE_REPROCESSADA, pedidosReprocessadosList.toArray()));
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public List<Pedido> findPedidosFechadosSemNfe(boolean notNull) throws SQLException {
		return PedidoPdbxDao.getInstance().findPedidosFechadosSemNfe(notNull);
	}

	private void validatePedidoQtItemFisicoVazioGondola(Pedido pedido) {
		if (!pedido.isGondola()) {
			return;
		}
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			if (((ItemPedido) itemPedidoList.items[i]).getQtItemFisico() == 0) {
				throw new ItemPedidoSemQtItemFisicoGondolaException(Messages.PEDIDO_ITENS_SEM_QT_ITEM_FISICO_GONDOLA);
			}
		}
	}
	
	public void setCdClienteHashValuesDinamicos(Pedido pedido) {
		if (pedido == null || pedido.getHashValuesDinamicos().get(CDCLIENTE) == pedido.cdCliente) return;
		pedido.getHashValuesDinamicos().put(CDCLIENTE, pedido.cdCliente);
	}

	public void setDsEmailsDestinoHashValuesDinamicos(Pedido pedido) throws SQLException {
		if (pedido == null || pedido.getCliente().dsEmail == null) return;
		pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSEMAILSDESTINO, pedido.getCliente().dsEmail);
	}
	
	public Vector findPedidoRelacionadoList(Pedido pedidoFilter) throws SQLException {
		return VectorUtil.concatVectors(PedidoPdbxDao.getInstance().findPedidoRelacionadoList(pedidoFilter),
				PedidoPdbxDao.getInstanceErp().findPedidoRelacionadoList(pedidoFilter));
	}

	public void validateQtMinProdutoClasse(Pedido pedido) throws SQLException {
		Vector cdClasseList = ItemPedidoService.getInstance().getCdClasseListByPedido(pedido);
		if (ValueUtil.isNotEmpty(cdClasseList)) {
			Map<String, Double> qtItensClasseProdutoHash = ItemPedidoService.getInstance().getQtItensByCdClasseNoPedido(pedido);
			int size = cdClasseList.size();
			for (int i = 0; i < size; i++) {
				String cdClasse = (String) cdClasseList.items[i];
				double qtMinPedido = TabPrecoClasseProdService.getInstance().findTabPrecoClasseProdComMaiorMinimoByClasse(pedido, cdClasse);
				validateQtMinProdutoClasse(qtItensClasseProdutoHash, cdClasse, qtMinPedido);
			}
		}
	}

	private void validateQtMinProdutoClasse(Map<String, Double> qtItensClasseProdutoHash, String cdClasse, double qtMinPedido) {
		double qtAtual = qtItensClasseProdutoHash.get(cdClasse);
		if (qtAtual >= qtMinPedido) {
			return;
		}
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_PROD_ABAIXO_MIN_CLASSE_TABPRECO, new Object[]{cdClasse, StringUtil.getStringValueToInterface(qtMinPedido), StringUtil.getStringValueToInterface(qtAtual)}));
		}
		throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_PROD_ABAIXO_MIN_CLASSE, new Object[]{cdClasse, StringUtil.getStringValueToInterface(qtMinPedido), StringUtil.getStringValueToInterface(qtAtual)}));
	}

	public Vector findPedidoPdaListByStatus(final String cdStatusPedido) {
		try {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdStatusPedido = cdStatusPedido;
			return PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return new Vector();
		}
	}

	private void validateConversaoUnFOB(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		TipoFrete tipoFrete = pedido.getTipoFrete();
		if (!LavenderePdaConfig.usaValidaConversaoFOB() || tipoPedido == null || tipoFrete == null ||
			!tipoFrete.isTipoFreteFob() || pedido.isPedidoCritico() || pedido.isPedidoBonificacao()) {
			return;
		}
		if (tipoPedido.nuMinConversaoFob == 0) return;
		double qtConversaoFob = 0;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			double nuConversaoFob = itemPedido.getProduto().nuConversaoFob;
			if (nuConversaoFob == 0) {
				nuConversaoFob = 1;
			}
			qtConversaoFob += itemPedido.getQtItemFisico() / nuConversaoFob;
		}
		if (qtConversaoFob < tipoPedido.nuMinConversaoFob) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_FOB_NAO_ATINGIU_NU_CONVERSAO_FOB, 
				new String[] {
					StringUtil.getStringValueToInterface((double) tipoPedido.nuMinConversaoFob),
					StringUtil.getStringValueToInterface(qtConversaoFob)
				}
			));
		}
		pedido.flPendenteFob = ValueUtil.VALOR_SIM;
	}

	private void validateItemPedidoProdutoBloqueado(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() || !LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			return;
		}
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (ProdutoBloqueadoService.getInstance().isBloqueadoAllTabelaPreco(itemPedido) || ProdutoBloqueadoService.getInstance().isBloqueadoForTabelaPreco(itemPedido)) {
				throw new ItemPedidoBloqueadoException(Messages.PEDIDO_ITENS_PRODUTOS_BLOQUEADOS);
			}
		}
	}

	private void validatePedidoComProdutoRestrito(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			return;
		}
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			RestricaoProduto restricaoProduto = RestricaoService.getInstance().isProdutoRestrito(itemPedido.cdProduto, pedido.cdCliente, pedido.nuPedido, itemPedido.getQtItemFisico());
			if (restricaoProduto != null) {
				throw new ItemPedidoProdutoRestritoException(Messages.RESTRICAO_VENDA_ITEM_PEDIDO_PRODUTO_RESTRITO);
			}
		}
	}
	
	private void validatePedidoProdutoExclusivo(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante || LavenderePdaConfig.filtraProdutoClienteRepresentante || LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ProdutoService.getInstance().validateProdutoRelacaoDisponivel(pedido, (ItemPedido) pedido.itemPedidoList.elementAt(i));
			}
		}
	}

	private void validatePctLimiteBonificacao(Pedido pedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.nuPedidoRelBonificacao)) {
			return;
		}
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoFilter.cdUsuario = pedido.cdUsuario;
		pedidoFilter.nuPedido = pedido.nuPedidoRelBonificacao;
		pedidoFilter = findPedidoRelBonificacao(pedidoFilter, pedido.isPedidoRelacionadoOrigemErp());
		double valorLimitePedido = ValueUtil.round(pedidoFilter.vlTotalPedido * (LavenderePdaConfig.usaPedidoBonificacaoPercentualLimiteBonificacao() / 100));
		if (pedido.vlTotalPedido > valorLimitePedido) {
			String pctLimiteBonificacao = String.valueOf(ValueUtil.round(LavenderePdaConfig.usaPedidoBonificacaoPercentualLimiteBonificacao()));
			throw new ValidationException(MessageUtil.getMessage(Messages.PCT_LIMITE_BONIFICACAO_ULTRAPASSADO, new String[]{pctLimiteBonificacao, StringUtil.getStringValueToInterface(ValueUtil.round(valorLimitePedido))}));
		}
	}

	private void validateObrigaVincularCampanhaPublicitaria(Pedido pedido) throws SQLException {
		if (!ValueUtil.getBooleanValue(pedido.flVinculaCampanhaPublicitaria) || (ValueUtil.valueEquals(pedido.cdCampanhaPublicitaria, ValueUtil.VALOR_EMBRANCO) || ValueUtil.isEmpty(pedido.cdCampanhaPublicitaria))) {
			throw new ValidationException(Messages.CAMPANHA_PUBLICITARIA_OBRIGA_VINCULAR_CAMPANHA);
		}
	}

	public boolean pedidoPossuiSomenteProdutosNeutros(Pedido pedido) throws SQLException {
		if (pedido == null || ValueUtil.isEmpty(pedido.itemPedidoList)) return false;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.getProduto().isNeutro()) return false;
		}
		return true;
	}

	public void deleteItensKit(Pedido pedido, String cdKit) throws SQLException {
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
			deleteItemPedido(pedido, (ItemPedido) itemPedidoKitList.items[i]);
		}
	}
	
	public Image[] getIconsMarcadores(Pedido pedido, HashMap<String, Marcador> marcadoresHash, Map<String, Image> marcadoresMap, int iconSize) throws SQLException {
		if (!LavenderePdaConfig.usaMarcadorPedido || pedido.cdMarcadores == null) return null;
		
		List<Image> iconesIndicadores = new ArrayList<>();
		String[] marcadoresPedido = pedido.cdMarcadores.split(",");
		
		for (int i = 0; i < marcadoresPedido.length; i++) {
			Marcador marcador = marcadoresHash.get(marcadoresPedido[i]);
			if (marcador == null || marcador.imMarcadorAtivo == null) continue;
			Image image = UiUtil.getImage(marcador.imMarcadorAtivo.clone());
				image = UiUtil.getSmoothScaledImage(image, iconSize, iconSize);
				marcadoresMap.put(marcador.cdMarcador, image);
			iconesIndicadores.add(image);
			}
		
		return iconesIndicadores.toArray(new Image[iconesIndicadores.size()]);
		}
	
	public void recalculaItensDescQtdVencidos(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		Vector produtosVencidosList = ItemPedidoService.getInstance().findCdProdutosDescQtdVencidos(pedido);
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) itemPedidoList.items[i];
			if (produtosVencidosList.contains(item.cdProduto)) {
				ItemPedidoService.getInstance().reverteDescQtd(item);
				recalculaItemPedido(item);
			}
		}
		updatePedidoAfterCrudItemPedido(pedido);
	}

	public void recalculaItensDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		Vector itensExtrapolados = ItemPedidoService.getInstance().findCdProdutosDescProgressivoPersonalizadoExtrapolados(pedido);
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) itemPedidoList.items[i];
			if (itensExtrapolados.contains(item.cdProduto)) {
				item.cdDescProgressivo = null;
				item.vlPctDescProg = 0;
				item.vlPctDesconto = 0;
				recalculaItemPedido(item);
			}
		}
		updatePedidoAfterCrudItemPedido(pedido);
	}

	public void executaLimpezaPedidosNaoEnviadosErp() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		Date dataLimite = DateUtil.getCurrentDate();
		DateUtil.decDay(dataLimite, LavenderePdaConfig.getNuDiasPermanenciaRegistroAppConversao());
		pedidoFilter.dtEmissaoFinalFilter = dataLimite;
		pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
		pedidoFilter.filtraApenasTipoPedidoIgnoraEnvioErp = true;
		deleteAllByExample(pedidoFilter);
	}

	
	public void getAndSetInformacoesComplementaresWebserviceSankhya(Pedido pedido) {
		HashMap<String, Object> informacoesComplementares = getInformacoesComplementaresPedidoWebserviceSankhya(pedido);
		setInformacoesComplementaresPedido(pedido, informacoesComplementares);
	}

	public HashMap<String, Object> getInformacoesComplementaresPedidoWebserviceSankhya(Pedido pedido) {
		try {
			String body = getCamposPedidoForWebserviceSankhya(pedido);
			return SyncManager.getInformacoesComplementaresPedidoWebserviceSankhya(body);
		} catch (NoConnectionAvailableException e) {
			ExceptionUtil.handle(e);
		} catch (ValidationException e) {
			throw e;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return new HashMap<>();
	}

	private String getCamposPedidoForWebserviceSankhya(Pedido pedido) throws IllegalAccessException {
		String sql = LavenderePdaConfig.consultaSqlWebserviceSankhyaComplementaPedido;
		Pattern pattern = Pattern.compile(":[A-z0-9_-]*");
		Matcher matcher = pattern.matcher(sql);
		HashMap<String, Object> camposPedido = new HashMap<>();
		while (matcher.find()) {
			String matchedString = matcher.group(0);
			String campo = matchedString.substring(1);
			Object campoObjeto = null;
			Object campoDinamico = pedido.getHashValuesDinamicos().get(campo);
			if (campoDinamico instanceof String && ValueUtil.isNotEmpty((String) campoDinamico) ||
					!(campoDinamico instanceof String) && campoDinamico != null) {
				campoObjeto = campoDinamico;
			} else {
				Field field = ReflectionUtil.getFieldValueIgnoreCase(pedido, campo);
				if (field != null) {
					campoObjeto = field.get(pedido);
				}
			}
			if (campoObjeto instanceof Date) {
				camposPedido.put(campo, DateUtil.formatDateDb((Date) campoObjeto));
			} else if (campoObjeto != null) {
				camposPedido.put(campo, campoObjeto);
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERROR_CAMPO_WEB_SERVICE_SANKHYA, campo));
			}
			sql = sql.replaceAll(matchedString + " ", " ");
			matcher = pattern.matcher(sql);
		}
		return new JSONObject(camposPedido).toString();
	}

	public void setInformacoesComplementaresPedido(Pedido pedido, HashMap<String, Object> informacoesComplementares) {
		HashMap<String, Field> hashFields = new HashMap<>();
		for (Field field : pedido.getClass().getDeclaredFields()) {
			hashFields.put(field.getName().toUpperCase(), field);
		}
		for (Map.Entry<String, Object> entry : informacoesComplementares.entrySet()) {
			String nmCampo = entry.getKey();
			Object vlCampo = entry.getValue();
			Field field = hashFields.get(nmCampo.toUpperCase());
			if (field != null) {
				try {
					if (field.getType().getName().equals(Date.class.getName())) { 
						field.set(pedido, new Date(String.valueOf(vlCampo), Settings.DATE_DMY));
						continue;
					}
					if ("CDTRANSPORTADORA".equalsIgnoreCase(nmCampo) && LavenderePdaConfig.usaTransportadoraPedido()) {
						pedido.preencheuCdTransportadoraSankhya = true;
					}
					field.set(pedido, vlCampo);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			} else if (existeCampoDynPedido(nmCampo)) {
				pedido.getHashValuesDinamicos().put(nmCampo, vlCampo);
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERROR_CAMPO_WEB_SERVICE_SANKHYA, nmCampo));
			}
		}
	}

	private boolean existeCampoDynPedido(String nmCampo) {
		Vector camposDyn = Pedido.getConfigPersonCadList(Pedido.TABLE_NAME_PEDIDO);
		int size = camposDyn.size();
		for (int i = 0; i < size; i++) {
			if (((Campo)camposDyn.items[i]).nmCampo.equalsIgnoreCase(nmCampo)) {
				return true;
			}
		}
		return false;
	}


	public void updatePedidoMotivoPendente(Pedido pedido) throws SQLException {
		PedidoPdbxDao.getInstance().updatePedidoMotivoPendente(pedido);
	}

	public RecalculoDescontoProgressivoDTO atualizaDescProgressivoPedido(Pedido pedido) throws SQLException {
		boolean atualizouDesc = false;
		Vector listDescProg = loadDescProgConfig(pedido);
		int size = listDescProg.size();
		boolean needUpdatePedido = false;
		RecalculoDescontoProgressivoDTO recalculoDescontoProgressivoDTO = new RecalculoDescontoProgressivoDTO();
		Vector listItemDescontoAlterado = new Vector();
		boolean exibeQualquerAlteracaoDescontoProgressivo = LavenderePdaConfig.exibeQualquerAlteracaoDescontoProgressivo();
		boolean exibeAlteracaoGanhoDescontoProgressivo = LavenderePdaConfig.exibeAlteracaoGanhoDescontoProgressivo();
		boolean exibeAlteracaoPercaDescontoProgressivo = LavenderePdaConfig.exibeAlteracaoPercaDescontoProgressivo();
		boolean insereDiferenca = false;
		for (int i = 0; i < size; i++) {
			DescProgressivoConfig descProgConf = (DescProgressivoConfig) listDescProg.items[i];
			Vector itensConsumoDescProg = getItensConsumoDescProg(pedido, descProgConf.cdDescProgressivo);
			int size2 = itensConsumoDescProg.size();
			DescProgConfigFaDes descProgConfigFaDes = null;
			for (int j = 0; j < size2; j++) {
				ItemPedido itemConsumo = (ItemPedido) itensConsumoDescProg.items[j];
				if (j == 0) {
					descProgConfigFaDes = DescProgConfigFaDesService.getInstance().findFaixaDescProgByProdutoCliente(itemConsumo, true);
				}
				itemConsumo.auxiliarVariaveis.descProgRecalculo = descProgConfigFaDes;
				itemConsumo.auxiliarVariaveis.recalculandoDescProgEdit = true;
				ItemPedidoService.getInstance().calculate(itemConsumo, pedido);
				if (itemConsumo.hasDescProgressivo()) {
					itemConsumo.vlPctFaixaDescQtd = 0d;
					if ((itemConsumo.vlVerbaItem != 0 || itemConsumo.vlVerbaItemPositivo != 0) && permiteDeletarVerbaConsumidaNoItem(pedido) && !itemConsumo.isIgnoraControleVerba()) {
						VerbaService.getInstance().deleteVlSaldo(pedido, itemConsumo);
					}
				}
				atualizouDesc |= itemConsumo.auxiliarVariaveis.oldVlPctDescProg != itemConsumo.vlPctDescProg;
				atualizouDesc |= !ValueUtil.valueEquals(StringUtil.getStringValue(itemConsumo.auxiliarVariaveis.oldCdDescProgressivo), StringUtil.getStringValue(itemConsumo.cdDescProgressivo));
				insereDiferenca = exibeQualquerAlteracaoDescontoProgressivo && itemConsumo.auxiliarVariaveis.oldVlPctDescProg != itemConsumo.vlPctDescProg;
				insereDiferenca |= exibeAlteracaoGanhoDescontoProgressivo && itemConsumo.auxiliarVariaveis.oldVlPctDescProg < itemConsumo.vlPctDescProg;
				insereDiferenca |= exibeAlteracaoPercaDescontoProgressivo && itemConsumo.auxiliarVariaveis.oldVlPctDescProg > itemConsumo.vlPctDescProg;
				if (insereDiferenca) {
					listItemDescontoAlterado.addElement(
						new ItemPedidoDescontoModificadoDTO(
							itemConsumo,
							itemConsumo.auxiliarVariaveis.oldVlPctDescProg,
							itemConsumo.vlPctDescProg,
							itemConsumo.auxiliarVariaveis.oldCdDescProgressivo,
							itemConsumo.cdDescProgressivo,
							findDescProgInList(listDescProg, itemConsumo.auxiliarVariaveis.oldCdDescProgressivo),
							findDescProgInList(listDescProg, itemConsumo.cdDescProgressivo)
						)
					);
				}
				itemConsumo.auxiliarVariaveis.oldCdDescProgressivo = itemConsumo.cdDescProgressivo;
				itemConsumo.auxiliarVariaveis.oldVlPctDescProg = itemConsumo.vlPctDescProg;
				if (atualizouDesc || insereDiferenca) {
					ItemPedidoService.getInstance().aplicaDescontoItemPedido(itemConsumo);
					ItemPedidoService.getInstance().update(itemConsumo);
				}
				needUpdatePedido = true;
			}
		}
		if (needUpdatePedido) {
			updatePedidoAfterCrudItemPedido(pedido);
		}
		recalculoDescontoProgressivoDTO.atualizouDesconto = atualizouDesc;
		recalculoDescontoProgressivoDTO.listItemDescontoDTO = listItemDescontoAlterado;
		return recalculoDescontoProgressivoDTO;
	}

	public DescProgressivoConfig findDescProgInList(Vector listDescProgressivo, String cdDescProgressivo) {
		if (ValueUtil.isEmpty(listDescProgressivo) || ValueUtil.isEmpty(cdDescProgressivo)) return null;
		int size = listDescProgressivo.size();
		DescProgressivoConfig descProgressivoConfig = null;
		for (int i = 0; i <= size; i++) {
			descProgressivoConfig = (DescProgressivoConfig) listDescProgressivo.items[i];
			if (ValueUtil.valueEquals(descProgressivoConfig.cdDescProgressivo, cdDescProgressivo))
				return descProgressivoConfig;
		}
		return null;
	}
	
	private boolean isAtualizaDescontoItem(ItemPedido itemPedido, DescProgConfigFaDes descProgConfigFaDes) {
		return (descProgConfigFaDes != null && (!ValueUtil.valueEquals(descProgConfigFaDes.cdDescProgressivo, itemPedido.cdDescProgressivo) || descProgConfigFaDes.vlPctDescProg != itemPedido.vlPctDescProg)) ||
				descProgConfigFaDes == null && ValueUtil.isNotEmpty(itemPedido.cdDescProgressivo);
	}
	
	private Vector loadDescProgConfig(Pedido pedido) throws SQLException {
		Vector descProgConfList;
		DescProgressivoConfig filter = new DescProgressivoConfig();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cliente = pedido.getCliente();
		descProgConfList = DescProgressivoConfigService.getInstance().findAllByExample(filter);
		return descProgConfList;
	}
	
	private Vector getItensConsumoDescProg(Pedido pedido, String cdDescProg) throws SQLException {
		Vector cdProdList = ItemPedidoPdbxDao.getInstance().findItemPedidoConsomeDescProgList(pedido, cdDescProg);
		int size = pedido.itemPedidoList.size();
		Vector listFinal = new Vector(size);
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (cdProdList.contains(itemPedido.cdProduto)) {
				listFinal.addElement(itemPedido);
			}
		}
		return listFinal;
	}

	public boolean isInsereMultiplosSemNegociacao(Pedido pedido) throws SQLException {
		return pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && (LavenderePdaConfig.isInsereQtdDescMultipla() || LavenderePdaConfig.isInsereSomenteQtdMultipla());
	}
	

	public void realizaOrdenacaoListaItens(Pedido pedido, Vector list, String sortAtributte, String sortAsc) {
		ItemPedido.sortAttr = sortAtributte;
   		if ((LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto && Pedido.NMCOLUNA_CDPRODUTO.equals(sortAtributte)) || Pedido.NMCOLUNA_NUSEQITEMPEDIDO.equals(sortAtributte)) {
   			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
   		} else if (isOrdenaVerba()) {
		    SortUtil.qsortDouble(list.items, 0, list.size() - 1, ValueUtil.getBooleanValue(sortAsc));
	    } else if (isOrdenaGondola(pedido)) {
   			ItemPedidoService.getInstance().sortListByGondola(list, ValueUtil.getBooleanValue(sortAsc));
	    } else if (isOrdenaCombo()) {
	    	ComboService.getInstance().ordernaListaItens(list, ValueUtil.getBooleanValue(sortAsc));
	    } else if(isOrdenaEstoque()) { 
	    	SortUtil.qsortDouble(list.items, 0, list.size() -1, ValueUtil.getBooleanValue(sortAsc));
	    } else if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equalsIgnoreCase(ItemPedido.sortAttr)) {
	    	preencheProdutoDosItensPedido(list);
		    SortUtil.qsortString(list.items, 0, list.size() - 1, true);
	    } else {
   			list.qsort();
   		}
   		//Ordenação desc
   		if (sortAsc.startsWith(ValueUtil.VALOR_NAO) && !isOrdenaVerba() && !isOrdenaGondola(pedido) && !isOrdenaCombo() && !isOrdenaEstoque()) {
   			list.reverse();
   		}
	}
	
	private boolean isOrdenaGondola(Pedido pedido) {
		return pedido.isGondola() && ItemPedido.DS_COLUNA_QTITEMGONDOLA.equalsIgnoreCase(ItemPedido.sortAttr);
	}

	private boolean isOrdenaCombo() {
		return LavenderePdaConfig.isExibeComboMenuInferior() && ItemPedido.DS_COLUNA_CDCOMBO.equalsIgnoreCase(ItemPedido.sortAttr);
	}
	
	private boolean isOrdenaEstoque() {
		return LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto() && ProdutoBase.SORT_COLUMN_QTESTOQUE.equalsIgnoreCase(ItemPedido.sortAttr);
	}

	private boolean isOrdenaVerba() {
		return LavenderePdaConfig.usaOrdenacaoVerbaItemPedido && (ItemPedido.DS_COLUNA_VERBA_ITEM.equalsIgnoreCase(ItemPedido.sortAttr));
	}
	
	public void updateFlImpressoNfce(Pedido pedido) throws SQLException {
		if (pedido != null && !pedido.isFlOrigemPedidoErp()) {
			pedido.flNfeImpressa = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLNFEIMPRESSA", ValueUtil.VALOR_SIM, totalcross.sql.Types.VARCHAR);
		}
	}

	public Pedido getPedidoSumFechamentoDiario(Date dateFilter) throws SQLException {
		Pedido pedidoFilter = getPedidoFilterFechamentoDiario(dateFilter);
		return PedidoPdbxDao.getInstance().sumVlTotalPedidosFechamentoDiario(pedidoFilter);
	}
	
	public Vector findSumTotalPedidoFechamentoDiarioCliente(Date dateFilter) throws SQLException {
		Pedido pedidoFilter = getPedidoFilterFechamentoDiario(dateFilter);
		return PedidoPdbxDao.getInstance().findSumTotalPedidoFechamentoDiarioCliente(pedidoFilter);
	}
	

	public void reloadPoliticaComercialItensSync(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.elementAt(i);
			itemPedido.politicaComercial = null;
			itemPedido.cdPoliticaComercial = null;
			itemPedido.politicaComercialFaixa = null;
			itemPedido.vlPctComissao = 0;
			itemPedido.vlPctPoliticaComercial = 0;
			ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, pedido);
			ItemPedidoService.getInstance().loadPoliticaComercialFaixa(itemPedido);
			ItemPedidoService.getInstance().calculateComissaoItem(itemPedido, pedido);
			ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(itemPedido, false);
			ItemPedidoPdbxDao.getInstance().updateItemPendentePoliticaComercialReloadCondicaoPagamento(itemPedido);
		}
		pedido.loadedPoliticaComercialOnCondicaoPagtoChange = true;
	}

	public void loadPoliticaComercialOnEdit(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.elementAt(i);
			ItemPedidoService.getInstance().reloadPoliticaComercial(itemPedido);
			ItemPedidoService.getInstance().reloadFaixaPoliticaComercial(itemPedido);
			ItemPedidoService.getInstance().calculateComissaoItem(itemPedido, pedido);
		}
		if (pedido.alterouPoliticaComercialItem) {
			UiUtil.showWarnMessage(Messages.MSG_PEDIDO_AVISO_MUDANCA_POLITICA_COMERCIAL);
		}
	}

	public void reloadPoliticaComercialItensByPedidosAberto() throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		Vector pedidoList = PedidoService.getInstance().findAllByExample(pedidoFilter);
		if (ValueUtil.isNotEmpty(pedidoList)) {
			for (int i = 0; i < pedidoList.size(); i++) {
				Pedido pedido = (Pedido) pedidoList.items[i];
				PedidoService.getInstance().findItemPedidoList(pedido);
				pedido.forceReloadPoliticaComercialSync = true;
				if (pedido.itemPedidoList != null && pedido.itemPedidoList.size() > 0) {
					PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
					PedidoService.getInstance().reloadPoliticaComercialItensSync(pedido);
				}
			}
		}
	}

	public boolean validaVlTotalBonificacaoPedido(Pedido pedido) {
		if (LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble() == 0) return true;
		double pctVlTotalBonificacao = (pedido.vlBonificacaoPedido / pedido.vlTotalPedido) * 100;
		double pctMaxPedidoBonificado = LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble();
		if (pctVlTotalBonificacao > pctMaxPedidoBonificado) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_BONIFICACAO_EXTRAPOLADA, new String[] {StringUtil.getStringValueToInterface(pctVlTotalBonificacao), StringUtil.getStringValueToInterface(pctMaxPedidoBonificado)}));
			return false;
		}
		return true;
	}
	
	private void validaCondicaoPagamentoPadraoCliente(Pedido pedido, String cdCondicaoPagamento) throws SQLException {
		if (!LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagtoDiferentePadrao() || pedido.isPendenteCondPagto()) {
			return;
		}
		if (!CondicaoPagamentoService.getInstance().isCondicaoPadraoCliente(pedido.getCliente(), cdCondicaoPagamento)) {
			throw new CondicaoPagamentoDiferentePadraoClienteException(ValueUtil.VALOR_NI);
		}
	}

	private void validadeVlFreteAdicionalPedido(Pedido pedido) {
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido() && !LavenderePdaConfig.isPermiteValorFreteManualMaiorQueValorPedido()) {
			if (pedido.vlFreteAdicional > pedido.vlTotalItens) {
				throw new ValidationException(MessageUtil.getMessage(Messages.VALIDACAO_FECHAR_PEDIDO_VL_FRETE_ADICIONAL, new String[]{StringUtil.getStringValue(pedido.vlFreteAdicional), StringUtil.getStringValue(pedido.vlTotalItens)}));
			}
		}
	}

	private boolean isProdutoPresenteItemPedido(ProdutoBase produto, Vector list) {
		if (ValueUtil.isEmpty(list) || produto == null || ValueUtil.isEmpty(produto.cdProduto)) return false;
		int size = list.size();
		for (int i =  0; i < size; i++) {
			ItemPedido item = (ItemPedido) list.items[i];
			if (ValueUtil.valueEqualsIfNotNull(produto.cdProduto, item.cdProduto)) {
				return true;
		}
		}
		return false;
	}
	
	public boolean isProdutoPresenteItemPedido(ProdutoBase produto, Pedido pedido) {
		return isProdutoPresenteListaSimilares(produto, pedido) || isProdutoPresenteItemPedido(produto, pedido.itemPedidoList);
	}

	public boolean isProdutoAgrupadorGradePresentePedido(ProdutoBase produto, Vector list) {
		if (ValueUtil.isEmpty(list) || produto == null || !produto.isProdutoAgrupadorGrade()) return false;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) list.items[i];
			if (ProdutoService.getInstance().isSameAgrupadorGrade(produto, item)) {
				return true;
			}
		}
		return false;
	}

	private boolean isProdutoPresenteListaSimilares(ProdutoBase produto, Pedido pedido) {
		return LavenderePdaConfig.usaAgrupadorSimilaridadeComSolicitacaoAutorizacao() && isProdutoPresenteItemPedido(produto, pedido.getItemPedidoAgrSimilares());
	}

	public boolean possuiCondicaoComercial(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && !(new CondicaoComercial().equals(pedido.getCondicaoComercial()));
	}

	public double getVlDescontoPedidoDescontadoIncentivos(Pedido pedido) throws SQLException {
		return (pedido.vlTotalItens - getVlDeducoesPedido(pedido)) * pedido.vlPctDesconto / 100;
	}

	public double getVlTotalItensPedidoDescontadoIncentivos(Pedido pedido) throws SQLException {
		return pedido.vlTtPedidoComTributos - (getVlDeducoesPedido(pedido) + getVlDescontoPedidoDescontadoIncentivos(pedido));
	}

	public double getVlTotalFreteItensPedido(Pedido pedido) throws SQLException {
		double totalFreteItensPedido = 0d;
		if(LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				totalFreteItensPedido += itemPedido.vlTotalItemPedidoFrete;
			}
		}
		return totalFreteItensPedido;
	}

	public double getVlDeducoesPedido(Pedido pedido) throws SQLException {
		double vlDeducoes = 0;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i ++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			vlDeducoes += itemPedido.getVlDeducoes();
		}
		return vlDeducoes;
	}

	public void updateVlTotalPedidoFreteAdicional(Pedido pedido) throws SQLException {
		updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_VLFRETEADICIONAL, pedido.vlFreteAdicional, Types.DECIMAL);
		updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_VLTOTALPEDIDO, pedido.vlTotalPedido, Types.DECIMAL);
	}
	
	public void setHashValuesDinamicosByEntidadeCliente(Pedido pedido) throws SQLException {
		Campo campoFilter = new Campo();
		campoFilter.cdSistema = Campo.CD_SISTEMA_PADRAO;
		campoFilter.nmEntidade = Pedido.TABLE_NAME_PEDIDOWEB;
		campoFilter.nmEntidadeOrigem = Cliente.TABLE_NAME_WEB;
		Vector campoDynPedidoCliente = CampoService.getInstance().findAllByExample(campoFilter);
		Cliente clienteDyn = (Cliente) ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());
		for (int i = 0; i < campoDynPedidoCliente.size(); i++) {
			Campo campo = (Campo) campoDynPedidoCliente.items[i];
			pedido.getHashValuesDinamicos().put(campo.nmCampo, StringUtil.getStringValue(clienteDyn.getHashValuesDinamicos().get(campo.nmCampo)));
		}
	}

	public void recalculaPoliticasBonificacaoPedidosAbertos() {
		try {
			Pedido pedidoFilter = new Pedido();
			pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
			Vector pedidoList = PedidoService.getInstance().findAllByExample(pedidoFilter);
			if (ValueUtil.isNotEmpty(pedidoList)) {
				for (int indexPedido = 0; indexPedido < pedidoList.size(); indexPedido++) {
					Pedido pedido = (Pedido) pedidoList.items[indexPedido];
					PedidoService.getInstance().findItemPedidoList(pedido);
					if (pedido.itemPedidoList != null && pedido.itemPedidoList.size() > 0) {
						for (int indexItemPedido = 0; indexItemPedido < pedido.itemPedidoList.size(); indexItemPedido++) {
							ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.elementAt(indexItemPedido);
							ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false, false);
						}
						ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(pedido.itemPedidoList, true);
					}
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void validateBonificacaoContaCorrente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && ItemPedidoBonifCfgService.getInstance().isPedidoComItemContaCorrente(pedido)) {
			BonifCfgService.getInstance().validateBonificacaoContaCorrente(pedido);
		}
	}
	
	public Pedido getPedidoBonificacaoContaCorrente(Pedido pedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoFilter.cdCliente = pedido.cdCliente;
		pedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		pedidoFilter.nuPedidoRelBonificacao = pedido.nuPedido;
		TipoPedido tipoPedido = TipoPedidoService.getInstance().findTipoPedidoBonifContaCorrente(pedido.cdEmpresa, pedido.cdRepresentante); 
		if (tipoPedido != null) {
			pedidoFilter.cdTipoPedido = tipoPedido.cdTipoPedido;
		}
		pedidoFilter.limit = 1;
		Vector pedidos = findAllByExampleDyn(pedidoFilter);
		if (ValueUtil.isNotEmpty(pedidos)) {
			Pedido pedidoBon = (Pedido) pedidos.items[0];
			findItemPedidoList(pedidoBon);
			return pedidoBon;
		}
		return null;
	}
	
	private void preencheProdutoDosItensPedido(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			try {
				((ItemPedido)itemPedidoList.elementAt(i)).getProduto();
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	private boolean isSomaFreteAoTotalPedidoNoFinalDoCalculo(TipoPedido tipoPedido) {
		return tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && LavenderePdaConfig.isPermiteInserirFreteManualENaoUsaTipoFrete();
	}

	public void divideFreteManualNosItens(Pedido pedido, boolean onEdVlFreteChange) throws SQLException {
		if (LavenderePdaConfig.usaFreteManualPedido && ValueUtil.isNotEmpty(pedido.itemPedidoList)){
			PedidoService.getInstance().updateColumn(pedido.rowKey, Pedido.NMCOLUNA_VLFRETE, pedido.vlFrete, Types.DECIMAL);
			if (pedido.vlFrete == 0) {
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					itemPedido.vlTotalItemPedidoFrete = 0d;
					recalculaItemPedido(itemPedido);
				}
			} else {
				double vlFretePorItem = pedido.vlFrete / pedido.vlTotalPedido;
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
					itemPedido.vlTotalItemPedidoFrete = vlFretePorItem * itemTabelaPreco.vlUnitario * itemPedido.qtItemFisico;
					recalculaItemPedido(itemPedido);
				}
			}
			if (onEdVlFreteChange) atualizaPedido(pedido);
		}
	}

	public void loadStatusPedidoPda(Pedido pedido) throws SQLException {
		pedido.statusPedidoPda.cdStatusPedido = pedido.cdStatusPedido;
		pedido.statusPedidoPda = (StatusPedidoPda) StatusPedidoPdaService.getInstance().findByRowKey(pedido.statusPedidoPda.getRowKey());
		if (pedido.statusPedidoPda == null) {
			pedido.statusPedidoPda = new StatusPedidoPda();
			pedido.statusPedidoPda.cdStatusPedido = pedido.cdStatusPedido;
		}
	}

	public String[] getValuesItensPedidoForTotalizadores(Map<String, ItemPedido> itensPedido) {
		double qt = 0;
		double vlt = 0;
		for (ItemPedido item : itensPedido.values()) {
			qt += item.getQtItemFisico();
			vlt += item.vlTotalItemPedido;
		}
		return new String[]{StringUtil.getStringValueToInterface(qt, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface), StringUtil.getStringValueToInterface(vlt)};
	}

	public void setFlVendidoProduto(Produto produto, ItemPedido itemPedido, boolean reabrindoPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(produto.cdProduto)) {
			if (reabrindoPedido && OrigemPedido.FLORIGEMPEDIDO_PDA.equals(produto.flVendido)) {
				setaProdutoComoNaoVendido((ItemPedido) itemPedido.clone(), null, produto);
			} else if (!produto.isVendido()) {
				ProdutoService.getInstance().updateFlVendidoProduto(produto, OrigemPedido.FLORIGEMPEDIDO_PDA);
			}
		}
	}
	
	public void setFlVendidoProdutoTabPreco(ProdutoTabPreco produtoTabPreco, ItemPedido itemPedido, boolean reabrindoPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(produtoTabPreco.cdProduto)) {
			if (!produtoTabPreco.isVendido()) {
				produtoTabPreco.flVendido = OrigemPedido.FLORIGEMPEDIDO_PDA;
				ProdutoTabPrecoService.getInstance().update(produtoTabPreco);
			} else if (reabrindoPedido && OrigemPedido.FLORIGEMPEDIDO_PDA.equals(produtoTabPreco.flVendido)) {
				setaProdutoComoNaoVendido((ItemPedido) itemPedido.clone(), produtoTabPreco, null);
			}
		}
	}

	public void atualizaNuOrdemCompraClienteItemPedidoByPedido(Pedido pedido) throws SQLException {
		ItemPedidoService.getInstance().updateNuOrdemCompraClienteItemPedidoByNuOrdemCompraPedido(pedido);
	}

	public boolean isPossuiPedidoRelacionadoNaoObrigatorio(Pedido pedido) {
		return LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao() && ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao);
	}
	
	public boolean hasMotivoCancelamentoPedidoAuto(Pedido pedido) throws SQLException {
		String cdMotivoCancelamentoAuto = MotCancelPedidoService.getInstance().findCdMotCancelPedidoDefault();
		if (ValueUtil.isEmpty(cdMotivoCancelamentoAuto)) {
			return true;
		}
		return ValueUtil.isNotEmpty(pedido.cdMotivoCancelamento) && pedido.cdMotivoCancelamento.equals(cdMotivoCancelamentoAuto);
	}
	
	private PedidoPdbxDao getPedidoPdbxDaoByFlOrigemPedido(String flOrigemPedido) {
		return ValueUtil.valueEquals(OrigemPedido.FLORIGEMPEDIDO_PDA, flOrigemPedido) ? PedidoPdbxDao.getInstance() : PedidoPdbxDao.getInstanceErp(); 
	}
	
	public void updateFlPendentePedidoRelacionado(Pedido pedidoOriginal, String flPendente) throws SQLException {
		if (LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao() && (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && ValueUtil.isNotEmpty(pedidoOriginal.nuPedidoRelBonificacao)) {
			Pedido pedidoRelacionado = new Pedido();
			pedidoRelacionado.cdEmpresa = SessionLavenderePda.cdEmpresa;
			pedidoRelacionado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante; 
			pedidoRelacionado.flOrigemPedido = pedidoOriginal.flOrigemPedido;
			pedidoRelacionado.nuPedidoRelBonificacao = pedidoOriginal.nuPedido;
			getPedidoPdbxDaoByFlOrigemPedido(pedidoRelacionado.flOrigemPedido).updateFlPendentePedidoRelacionado(pedidoRelacionado, flPendente);
		}
	}

	public boolean isPermiteAlterarCondicaoComercialPedido(Pedido pedido) {
		StatusOrcamento statusOrcamento = pedido.statusOrcamento;
		return LavenderePdaConfig.permiteAlterarCondicaoComercialPedido || LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && statusOrcamento != null && statusOrcamento.isPermiteAlterarCondComercialPedido();
	}
	
	public void clearRentabilidadePedido(Pedido pedido) throws SQLException {
		pedido.vlPctMargemRentab = 0;
		pedido.vlBaseMargemRentab = 0;
		pedido.vlCustoMargemRentab = 0;
		pedido.cdMargemRentab = ValueUtil.VALOR_NI;
		pedido.vlPctComissaoPedido = 0;
		pedido.vlPctComissao = 0;
		pedido.vlPctComissaoTotal = 0;
		update(pedido);
		ItemPedidoService.getInstance().clearRentabilidadeItensPedidoByPedido(pedido);
	}
	
	public void updatePedidoUtilizaRentabilidade(Pedido pedido) throws SQLException {
		if (pedido != null && LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade() && ValueUtil.isEmpty(pedido.flUtilizaRentabilidade) && pedido.isPedidoAberto()) {
			pedido.flUtilizaRentabilidade = ValueUtil.VALOR_SIM;
			updateColumn(pedido.getRowKey(), "FLUTILIZARENTABILIDADE", pedido.flUtilizaRentabilidade, Types.VARCHAR);
		}
	}
}
