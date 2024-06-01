package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.EditNumberTextInteger;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.EmailUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.QrCodeWindow;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.enums.GroupTypeFile;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AreaVenda;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;
import br.com.wmw.lavenderepda.business.domain.CCCliPorTipo;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.Entrega;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.FaixaBoleto;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.PedidoDescErp;
import br.com.wmw.lavenderepda.business.domain.PlataformaVenda;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.ResourcesWmw;
import br.com.wmw.lavenderepda.business.domain.RestricaoProduto;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.dto.RecalculoDescontoProgressivoDTO;
import br.com.wmw.lavenderepda.business.enums.CamposEscolhaTransportadoraOptions;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.AreaVendaService;
import br.com.wmw.lavenderepda.business.service.AtividadePedidoService;
import br.com.wmw.lavenderepda.business.service.BonifCfgService;
import br.com.wmw.lavenderepda.business.service.BonificacaoSaldoService;
import br.com.wmw.lavenderepda.business.service.CCCliPorTipoService;
import br.com.wmw.lavenderepda.business.service.CampanhaPublicitariaService;
import br.com.wmw.lavenderepda.business.service.CanalCliGrupoService;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.CatalogoItemPedLogService;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.ComiRentabilidadeService;
import br.com.wmw.lavenderepda.business.service.ComissaoPedidoRepService;
import br.com.wmw.lavenderepda.business.service.CondTipoPagtoService;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.CondicaoNegociacaoService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.DescComiFaixaService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.DescontoVendaService;
import br.com.wmw.lavenderepda.business.service.DivisaoVendaService;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EstoquePrevistoService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FaixaBoletoService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.FotoPedidoService;
import br.com.wmw.lavenderepda.business.service.GiroProdutoService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.IndiceClienteGrupoProdService;
import br.com.wmw.lavenderepda.business.service.IpiService;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.business.service.ItemLiberacaoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoBonifCfgService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.LavendereBackupService;
import br.com.wmw.lavenderepda.business.service.LogAppService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.MargemRentabFaixaService;
import br.com.wmw.lavenderepda.business.service.MargemRentabService;
import br.com.wmw.lavenderepda.business.service.MenuCatalogoService;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaService;
import br.com.wmw.lavenderepda.business.service.NfceService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.NotaFiscalService;
import br.com.wmw.lavenderepda.business.service.NotificacaoPdaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.ParcelaPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoDescErpService;
import br.com.wmw.lavenderepda.business.service.PedidoDescService;
import br.com.wmw.lavenderepda.business.service.PedidoLogService;
import br.com.wmw.lavenderepda.business.service.PedidoRelacionadoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PesoFaixaService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoRegService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaClienteService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaService;
import br.com.wmw.lavenderepda.business.service.PoliticaComercialService;
import br.com.wmw.lavenderepda.business.service.PontExtPedService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoRelacionadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RelacionaPedProducaoException;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.ResourcesWmwService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.business.service.RotaEntregaService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.StatusNfeService;
import br.com.wmw.lavenderepda.business.service.StatusOrcamentoService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TipoFreteService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.business.service.TranspTipoPedService;
import br.com.wmw.lavenderepda.business.service.TransportadoraCepService;
import br.com.wmw.lavenderepda.business.service.TributacaoConfigService;
import br.com.wmw.lavenderepda.business.service.TributacaoService;
import br.com.wmw.lavenderepda.business.service.TributosService;
import br.com.wmw.lavenderepda.business.service.UsuarioConfigService;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import br.com.wmw.lavenderepda.business.service.VerbaClienteService;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException;
import br.com.wmw.lavenderepda.business.validation.BloqueioCondPagtoPorDiasClienteException;
import br.com.wmw.lavenderepda.business.validation.BoletoEmContingenciaNaoGeradoException;
import br.com.wmw.lavenderepda.business.validation.BonifCfgContaCorrenteException;
import br.com.wmw.lavenderepda.business.validation.ClienteAtrasadoVlTitulosException;
import br.com.wmw.lavenderepda.business.validation.ClienteBloqueadoException;
import br.com.wmw.lavenderepda.business.validation.ClienteInadimplenteException;
import br.com.wmw.lavenderepda.business.validation.CondicaoComercialVerbaException;
import br.com.wmw.lavenderepda.business.validation.CondicaoPagamentoDiferentePadraoClienteException;
import br.com.wmw.lavenderepda.business.validation.CreditoDisponivelPedidoException;
import br.com.wmw.lavenderepda.business.validation.DescItemMaiorDescProgressivoException;
import br.com.wmw.lavenderepda.business.validation.DescProgressivoPersonalizadoVigenciaException;
import br.com.wmw.lavenderepda.business.validation.DescontoAcumuladoException;
import br.com.wmw.lavenderepda.business.validation.DescontoCategoriaException;
import br.com.wmw.lavenderepda.business.validation.DescontoMaximoItemException;
import br.com.wmw.lavenderepda.business.validation.DescontoPonderadoPedidoException;
import br.com.wmw.lavenderepda.business.validation.DescontoProgressivoPedidoException;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosJsonException;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosNfeException;
import br.com.wmw.lavenderepda.business.validation.GiroProdutoException;
import br.com.wmw.lavenderepda.business.validation.ItemFechamentoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ItemParticipacaoExtrapoladoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoBloqueadoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoProdutoRestritoException;
import br.com.wmw.lavenderepda.business.validation.ItemPedidoSemQtItemFisicoGondolaException;
import br.com.wmw.lavenderepda.business.validation.ItensComProblemaPedidoSemClienteException;
import br.com.wmw.lavenderepda.business.validation.ItensDivergentesSemEstoqueException;
import br.com.wmw.lavenderepda.business.validation.ItensPedidoAbaixoPesoMinimoTabelaPrecoException;
import br.com.wmw.lavenderepda.business.validation.ItensPedidoAbaixoValorMinimoTabelaPrecoException;
import br.com.wmw.lavenderepda.business.validation.JustificativaMotivoPendenciaException;
import br.com.wmw.lavenderepda.business.validation.KitTipo3VigenciaException;
import br.com.wmw.lavenderepda.business.validation.LiberacaoDataEntregaPedidoException;
import br.com.wmw.lavenderepda.business.validation.LimiteCreditoClienteExtrapoladoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ListMultiplasSugestoesProdutosException;
import br.com.wmw.lavenderepda.business.validation.ListTransportadoraRegException;
import br.com.wmw.lavenderepda.business.validation.MarcaPendenteItemBonificacaoException;
import br.com.wmw.lavenderepda.business.validation.NaoVendaProdPedidoException;
import br.com.wmw.lavenderepda.business.validation.NfceException;
import br.com.wmw.lavenderepda.business.validation.NfeException;
import br.com.wmw.lavenderepda.business.validation.PedidoBonificacaoComVerbaGrupoSaldoNaoLiberadoPorSenhaException;
import br.com.wmw.lavenderepda.business.validation.PedidoNaoFechadoException;
import br.com.wmw.lavenderepda.business.validation.PedidoSemClienteException;
import br.com.wmw.lavenderepda.business.validation.PesquisaMercadoException;
import br.com.wmw.lavenderepda.business.validation.PositivacaoItensByFornecedorException;
import br.com.wmw.lavenderepda.business.validation.ProdutoClienteRelacionadoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoCreditoDescontoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoSemPrecoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoTipoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ProdutoTipoRelacaoException;
import br.com.wmw.lavenderepda.business.validation.ProdutosRelacionadosNaoAtendidosException;
import br.com.wmw.lavenderepda.business.validation.RecalculoPedidoException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosGradesInconsistentesException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosPendentesPedidoException;
import br.com.wmw.lavenderepda.business.validation.RelProdutosRentabilidadeSemAlcadaException;
import br.com.wmw.lavenderepda.business.validation.RentabilidadeMenorMinimaException;
import br.com.wmw.lavenderepda.business.validation.RentabilidadeNegativaException;
import br.com.wmw.lavenderepda.business.validation.ReservaEstoqueException;
import br.com.wmw.lavenderepda.business.validation.RestricaoVendaUnException;
import br.com.wmw.lavenderepda.business.validation.SugestaoItensRentabilidadeIdealException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaComCadastroComQtdPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaComCadastroSemQtdPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaDifPedidoException;
import br.com.wmw.lavenderepda.business.validation.SugestaoVendaPresenteEmOutrasEmpresasPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationBonificacaoSaldoException;
import br.com.wmw.lavenderepda.business.validation.ValidationGrupoProdutoNaoInseridoPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationItemPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMinPedidoException;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMinimoVerbaUltrapassadoException;
import br.com.wmw.lavenderepda.business.validation.ValidationVerbaPersonalizadaException;
import br.com.wmw.lavenderepda.business.validation.ValorMinimoParcelaException;
import br.com.wmw.lavenderepda.business.validation.VerbaSaldoPedidoConsumidoException;
import br.com.wmw.lavenderepda.business.validation.VerbaSaldoPedidoExtrapoladoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.AreaVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.BoletoConfigComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CargaPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CentroCustoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClasseValorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteEnderecoComboBoxMultiLine;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteSetorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteSetorOrigemComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoComercialComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoNegociacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ContatoErpComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DivisaoVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EntregaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemContaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ModoFaturamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PlataformaVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RotaEntregaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.SegmentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusOrcamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoEntregaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoFreteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoVeiculoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TransportadoraComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TributacaoComoBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.CadItemPedidoFormWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderClienteWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderPedidoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.WindowUtil;
import br.com.wmw.lavenderepda.print.PedidoConsignacaoManagerPrint;
import br.com.wmw.lavenderepda.print.PedidoNfceServicePrint;
import br.com.wmw.lavenderepda.print.PedidoNfeContingenciaManagerPrint;
import br.com.wmw.lavenderepda.print.PedidoServicePrint;
import br.com.wmw.lavenderepda.report.pdf.PdfReportManager;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.RecebeRetornoPedidoRunnable;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import br.com.wmw.lavenderepda.thread.FotoProdutoThread;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.LavendereFileChooserBoxUtil;
import br.com.wmw.lavenderepda.util.UiMessagesUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Vm;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.TextControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Vector;
import totalcross.xml.SyntaxException;

public class CadPedidoForm extends BaseLavendereCrudPersonCadForm {

	private static final String SYSTEM_VIEWER = "viewer";
	
	//TabPanel
	private static int TABPANEL_PEDIDO;
	private static int TABPANEL_ENTREGA;
	private static int TABPANEL_DETALHES;
	private static int TABPANEL_PAGTO;
	private static int TABPANEL_PAGAMENTOS;
	private static int TABPANEL_FRETE;
	private static int TABPANEL_INFO_ADICIONAL;
	private static int TABPANEL_INFO_FRETE_PEDIDO;
	private static int TABPANEL_NFE;
	private static int TABPANEL_BOLETO;
	private static int TABPANEL_NFCE;
	private static int TABPANEL_TOTALIZADORES;
	private static int TABPANEL_ATIVIDADES;
	private static int TABPANEL_DEBITO_BANCARIO;
	private static int TABPANEL_NOTAS_FISCAIS;
	//Botões
	private ButtonAction btWorkflow;
	private ButtonAction btFecharPedido;
	private ButtonAction btReabrirPedido;
	private ButtonAction btExcluirPedido;
	private ButtonAction btPerderPedido;
	private ButtonAction btNovoItem;
	private ButtonAction btListaItens;
	private ButtonAction btListaItensNfe;
	private ButtonAction btLiberarPedido;
	private ButtonAction btLiberarItensPendentes;
	private ButtonAction btDescontosConcedidos;
	private ButtonAction btConsignarPedido;
	private ButtonAction btDevolucaoConsignacaoPedido;
	private ButtonAction btRecalcularPedido;
	private ButtonAction btNotaFiscal;
	private ButtonAction btLibParcelaSolAutorizacao;
	private ButtonAction btQrCodePix;
	private BaseButton btRelacionarPedido;
	private BaseButton btNovaCargaPedido;
	private BaseButton btAlteraTransportadora;
	private BaseButton btIconeRentabilidade;
	private BaseButton btRelacionarCampanhaPublicitaria;
	private ButtonGroupBoolean bgAjudante;
	private ButtonGroupBoolean bgTaxaEntrega;
	private ButtonGroupBoolean bgAntecipaEntrega;
	private ButtonGroupBoolean bgAgendamento;
	private ButtonGroupBoolean bgAguardarPedidoComplementar;
	private ButtonGroupBoolean bgPedidoCritico;
	private ButtonOptions bmOpcoes;
	private ButtonGroupBoolean bgPedidoGondola;
	private ButtonGroupBoolean bgUsaCodigoInternoCliente;
	private ButtonGroupBoolean bgUtilizaRentabilidade;
	
	//Labels
	private LabelName lbPctDesconto;
	private LabelName lbVlDescontoCondPagto;
	private LabelName lbPctDescontoCondicaoPagamento;
	private LabelName lbPctDescontoHistoricoVendas;
	private LabelName lbPctDescItem;
	private LabelName lbPctAcrescimoItem;
	private LabelName lbVlTotalPedidoComTributos;
	private LabelName lbRentabilidade;
	private LabelName lbTabelaPreco;
	private LabelName lbVlTotalFrete;
	private LabelName lbQtPesoPedido;
	private LabelName lbAreaVenda;
	private LabelName lbPedidoRelacionado;
	private LabelName lbCdStatusNfe;
	private LabelName lbDsNaturezaOperacao;
	private LabelName lbVlChaveAcesso;
	private LabelName lbVlSerieNfe;
	private LabelName lbnuLote;
	private LabelName lbDsObservacao;
	private LabelName lbNuNfe;
	private LabelName lbVlTotalProdutosNfe;
	private LabelName lbVlTotalNfe;
	private LabelName lbDtSaida;
	private LabelName lbHrSaida;
	private LabelName lbDtEmissaoNfe;
	private LabelName lbPercDescLibRestante;
	private LabelName lbVlTotalLiberado;
	//-- NFCE
	private LabelName lbQtTotalItem;
	private LabelName lbVlTotalNfce;
	private LabelName lbVlTotalDesconto;
	private LabelName lbVlTotalLiquidoNfce;
	private LabelName lbDsFormaPagamento;
	private LabelName lbVlTotalPago;
	private LabelName lbVlTroco;
	private LabelName lbNuChaveAcesso;
	private LabelName lbNuNfce;
	private LabelName lbNuSerie;
	private LabelName lbDtEmissaoNfce;
	private LabelName lbHrEmissaoNfce;
	private LabelName lbNuProtocoloAutorizacao;
	private LabelName lbDtAutorizacao;
	private LabelName lbHrAutorizacao;
	private LabelName lbVlTotalTributos;
	private LabelName lbVlPctTributosFederais;
	private LabelName lbVlPctTributosEstaduais;
	private LabelName lbVlPctTributosMunicipais;
	//--

	//Edits
	private LabelName lbDtEntrega;
	private LabelName lbTributacao;
	private LabelName lbPossuiTaxaEntrega;
	private LabelName lbTaxaEntrega;
	private LabelName lbPossuiAjudante;
	private LabelName lbAjudante;
	private LabelName lbAntecipaEntrega;
	private LabelName lbPrecisaAgendamento;
	private LabelName lbStatusOrcamento;
	private LabelName lbObsOrcamento;
	private LabelName lbVlDesconto;
	private LabelName lbEdPctDesconto;
	private LabelName lbPedidoComplementar;
	private LabelName lbVlVerbaPedido;
	private LabelName lbVlVerbaPedidoPositiva;
	private LabelName lbVlPctIndiceFinCondPagto;
	private LabelName lbVlPctDescQuantidadePeso;
	private LabelName lbVlMinCondPagto;
	private LabelName lbVlMinTipoPed;
	private LabelName lbVlMinTipoPagto;
	private LabelName lbVlSeguroPedido;
	private LabelName lbVlBrutoPedidoMaisFrete;
	private LabelName lbVlDespesaAcessoria;
	private LabelName lbVlTotalMargem;
	private LabelName lbVlPctTotalMargem;
	private LabelName lbPedidoCritico;
	//Labels
	private LabelValue lvVlPctTotalMargem;
	private LabelValue lvVlTotalMargem;
	private LabelValue edDsCondicaoPagamento;
	private LabelValue lbPrecoLiberadoSenha;
	private LabelValue lbDsCliente;
	private LabelValue lbDsNmEmpresa;
	private LabelValue lbDsNmEmpresaCapa;
	private LabelValue lbDsStatusPedido;
	private LabelValue lbNuPedidoRelacionado;
	private LabelValue lvVlPedido;
	private LabelValue lvVlPedidoAberto;
	private LabelName lbVlMinTabelaPreco;
	private LabelValue lbVlTotalItens;
	private LabelValue lbVlEfetivo;
	private LabelValue lvVlTotalPedidoComTributosEDeducoes;
	private LabelValue lvVlTtPedidoTributosEDeducoesEDesc;
	private LabelName lbVlPrazoMedio;
	private LabelName lbNuParcelasPedido;
	private LabelValue lbNuPedido;
	private LabelValue lbDsOrigemPedido;
	private LabelValue lvDsStatusAlvara;
	private LabelValue lvVlTicketMedio;
	private LabelValue lbDtEmissao;
	private LabelValue lvVlTotalFrete;
	private EditNumberFrac edVlMinTabelaPreco;
	private EditNumberInt edPrazoMedio;
	private EditNumberInt edNuParcelaPedido;
	private LabelValue edVlPctDescontoVendaMensal;
	private LabelValue lvVlTotalPedidoComTributos;
	private LabelValue lvVlTotalBrutoItens;
	private LabelValue lvVlTotalLiberado;
	private LabelValue lvPercDescLibRestante;
	private LabelValue lvVlVerbaPedido;
	private LabelValue lvVlPctIndiceFinCondPagto;
	private LabelValue lvVlPctDescQuantidadePeso;
	private LabelValue lvVlPctRentabilidade;
	private LabelValue lvVlEscalaRentabilidade;
	private BaseButton btIconeComissaoPedido;
	private LabelValue lvVlIndiceRentabPedido;
	private LabelValue lbVlTotalFretePedido;
	private LabelValue lbVlBonificacaoPedido;
	private LabelValue lbVlTotalPedidoComFrete;
	private LabelValue lbVlTotalPedidoAbaFrete;
	private LabelValue lbVlTotalPedido;
	private LabelValue lbVlMinPromocional;
	private LabelValue lbVlTotalPedidoMaisFrete;
	private LabelValue lvVlBrutoPedidoMaisFrete;
	private LabelValue lbVlTotalPedidoTroca;
	private LabelValue lvPesoPedido;
	private LabelValue lbQtItensFaturados;
	private LabelValue lvVlTotalVolumePedido;
	private LabelValue lvVlTotalNotaCredito;
	private LabelValue lvPercMaxDesconto;
	private LabelValue lbHasNfe;
	private LabelValue lbHasBoleto;
	private LabelValue lvCdStatusNfe;
	private LabelValue lvDsNaturezaOperacao;
	private LabelValue lvVlChaveAcesso;
	private LabelValue lvVlSerieNfe;
	private LabelValue lvnuLote;
	private LabelValue lvDsObservacao;
	private LabelValue lvNuNfe;
	private LabelValue lvVlTotalNfe;
	private LabelValue lvVlTotalProdutosNfe;
	private LabelValue lvDtSaida;
	private LabelValue lvHrSaida;
	private LabelValue lvDtEmissaoNfe;
	private LabelValue lvNuPedidoOriginal;
	private LabelValue lvGeraCreditoBonificacaoCondicao;
	private LabelValue lvGeraCreditoBonificacaoFrete;
	private LabelValue lvValorCreditoDisponivel;
	private LabelValue lvUsuarioItemLiberacao;
	private LabelValue lbVlMaxPctDescFrete;
	private LabelValue lbVlMaxPctDescCondicao;
	private LabelValue lbVlPctDescCondicaoPagamento;
	private LabelValue lvVlBruto;
	private LabelValue lvVlDesconto;
	private LabelValue lbVlTotalPedidoFrete;
	private LabelValue lbVlFrete;
	private LabelValue lbNmTransportadora;
	private LabelValue lbNmTipoFrete;
	private LabelValue lbVlTotalSTPedido;
	private LabelValue lvVlTotalPedidoComissao;
	private LabelValue lvVlDescontoCondPagto;
	private LabelValue lvVlDespesaAcessoria;
	private LabelValue lvVlSeguroPedido;
	private LabelValue lvVlPesoMinimoTabPreco;
	private LabelValue lvVlTotalPedidoPorPeso;
	private LabelValue lvVlValorParcelaPedido;
	private LabelValue lvVlPontuacao;
	private LabelValue lvVlBrutoCapaPedido;
	private BaseButton btIconeFaixa;
	private LabelValue lvVlTotalPedidoComImpostos;
	private LabelValue lvVlTotalFreteItensPedido;
	private LabelValue lvVlPctComissao;
	private LabelValue lvTransportadoraCapaPedido;
	private LabelValue lvTipoFreteCapaPedido;
	private LabelValue lvVlFreteCapaPedido;
	//Combo
	public TabelaPrecoComboBox cbTabelaPreco;
	private TipoPagamentoComboBox cbTipoPagamento;
	private TipoPedidoComboBox cbTipoPedido;
	private TipoFreteComboBox cbTipoFrete;
	private ButtonGroupBoolean bgGeraCreditoBonificacaoFrete;
	private TipoEntregaComboBox cbTipoEntrega;
	private CondicaoPagamentoComboBox cbCondicaoPagamento;
	private ButtonGroupBoolean bgGeraCreditoBonificacaoCondicao;
	private ClienteSetorComboBox cbClienteSetor;
	private ClienteSetorOrigemComboBox cbClienteSetorOrigem;
	private AreaVendaComboBox cbAreaVenda;
	private RotaEntregaComboBox cbRotaEntrega;
	private SegmentoComboBox cbSegmento;
	private CondicaoComercialComboBox cbCondicaoComercial;
	private TransportadoraComboBox cbTransportadora;
	private TransportadoraComboBox cbTranspFretePersonalizado;
	private CentroCustoComboBox cbCentroCusto;
	private PlataformaVendaComboBox cbPlataformaVenda;
	private ItemContaComboBox cbItemConta;
	private ClasseValorComboBox cbClasseValor;
	private ModoFaturamentoComboBox cbModoFaturamento;
	private TributacaoComoBox cbTributacao;
	private CargaPedidoComboBox cbCargaPedido;
	private ClienteEnderecoComboBoxMultiLine cbEnderecoEntrega;
	private ClienteEnderecoComboBoxMultiLine cbEnderecoCobranca;
	private TipoVeiculoComboBox cbTipoVeiculo;
	private ContatoErpComboBox cbContatoErp;
	private BoletoConfigComboBox cbBoletoConfig;
	private CondicaoNegociacaoComboBox cbCondicaoNegociacao;
	private UnidadeComboBox cbUnidade;
	private TransportadoraComboBox cbTransportadoraAux;
	private EntregaComboBox cbEntrega;
	private StatusOrcamentoComboBox cbStatusOrcamento;
	private DivisaoVendaComboBox cbDivisaoVenda;
	//Edits
	private EditText edNuPedidoRelacionado;
	private EditText edRotaEntrega1;
	private EditText edDsCondicaoPagamentoSemCadastro;
	private EditText edNuOrdemCompraCliente2;
	private EditText edNuAgencia;
    private EditText edNuConta;
	private EditDate edDtSugestaoCliente;
	private EditDate edDtEntrega;
	private EditDate edDtCarregamento;
	private EditDate edDtPagamento;
	private EditDate edDtEntregaManual;
	private EditNumberFrac edValorMinCondPagto;
	private EditNumberFrac edValorMinTipoPagto;
	private EditNumberFrac edValorMinTipoPedido;
	private EditNumberFrac edVlSaldoCCCliente;
	private EditNumberFrac edVlPctDesconto;
	private EditNumberFrac edVlPctDescEspecial;
	private EditNumberFrac edVlPctDescItem;
	private EditNumberFrac edVlPctAcrescimoItem;
	private EditNumberFrac edVlPctDescCondicao;
	private EditNumberFrac edVlPctDescHistoricoVendas;
	private EditNumberFrac edDescontoCascataManualDescCliente;
	private EditNumberFrac edDescontoCascataManual2;
	private EditNumberFrac edDescontoCascataManual3;
	private EditNumberFrac edVlVerbaPedidoPositiva;
	private EditNumberFrac edVlPctFreteRep;
	private EditNumberFrac edVlFreteRep;
	private EditNumberFrac edVlFreteCli;
	private EditNumberFrac edVlTaxaEntrega;
	private EditNumberFrac edVlPctDescFrete;
    private EditNumberFrac edVlManualFrete;
    private EditNumberFrac edDescCascataCategoria1;
    private EditNumberFrac edDescCascataCategoria2;
    private EditNumberFrac edDescCascataCategoria3;
    private EditNumberFrac edVlDesconto;
	private EditNumberFrac edPctDesconto;
	private EditNumberFrac edVlPctVpc;
	private EditNumberInt edRotaEntrega2;
	private EditNumberInt edQtAjudante;
	private EditNumberInt edNuKmInicial;
	private EditNumberInt edNuKmFinal;
	private EditNumberMask edHrInicialIndicado;
	private EditNumberMask edHrFinalIndicado;
    private EditNumberMask edCnpjTransportadora;
    private EditNumberTextInteger edNuOrdemCompraCliente1;
    private EditMemo emObservacaoOrcamento;
    private EditMemo emObservacaoModoFaturamento;
	private EditText edNuCampanhaPublicitaria;
	private EditNumberFrac edVlFreteAdicional;

    //-- NFCE
    private LabelValue lbHasNfce;
  	private LabelValue lvQtTotalItem;
  	private LabelValue lvVlTotalNfce;
  	private LabelValue lvVlTotalDesconto;
  	private LabelValue lvVlTotalLiquidoNfce;
  	private LabelValue lvDsFormaPagamento;
  	private LabelValue lvVlTotalPago;
  	private LabelValue lvVlTroco;
  	private LabelValue lvNuChaveAcesso;
  	private LabelValue lvNuNfce;
  	private LabelValue lvNuSerie;
  	private LabelValue lvDtEmissaoNfce;
  	private LabelValue lvHrEmissaoNfce;
  	private LabelValue lvNuProtocoloAutorizacao;
  	private LabelValue lvDtAutorizacao;
  	private LabelValue lvHrAutorizacao;
  	private LabelValue lvVlTotalTributos;
  	private LabelValue lvVlPctTributosFederais;
  	private LabelValue lvVlPctTributosEstaduais;
  	private LabelValue lvVlPctTributosMunicipais;
  	//--

	private CheckBoolean ckVinculaCampanhaPublicitaria;

	//toolTips
	private BaseToolTip tipCliente;
	private BaseToolTip tipEmpresa;
	private BaseToolTip tipCdPgto;
	private BaseToolTip tipVlMinTipoPagto;
	private BaseToolTip tipVlMinCondPagto;
	private BaseToolTip tipPrazoMedio;
	private BaseToolTip tipNuParcelaPedido;
	private BaseToolTip tipVlMinTipoPedido;
	private BaseToolTip tipVlMinTabelaPreco;
	private BaseToolTip tipDsNaturezaOperacao;
	private BaseToolTip tipVlChaveAcesso;
	private BaseToolTip tipDsObservacao;
	
	
	//Controles
	private boolean controlHrFimEmissao;
	private boolean naoConsisteValidacaoFechamentoAoSalvar;
	private boolean naoAvisaClienteAtrasado;
	private boolean houveAlteracaoCampos;
	private boolean controlEnvioAutoPedido;
	public boolean inOnlyConsultaItens;
	public boolean inRelatorioMode;
	public boolean inItemRenegotiation;
	public boolean inItemNegotiationGiroProdutoPendente;
	public boolean inItemNegotiationProdutosPendentes;
	public boolean inItemNegotiationProdutosRelacionados;
	public boolean inItemNegotiationSugestaoRentabilidadeIdeal;
	public boolean openPopUpItensRentabilidadeManual;
	public boolean inConsultaVendaRelacionada;
	public boolean isPedidoConsignado;
	public boolean mostraSugestaoItemComboOnExibition;
	private boolean fechaPedidoSugestaoCombo;
	public boolean salvouItemComboSugerido;
	public boolean solicitadoAcessoGiroProduto;
	public boolean permiteRecalculo;
	public String ultimoClienteExibido;
	private String ultimaTabelaPrecoSelected;
	private String ultimaCondPgamentoSelected;
	private String ultimaCondicaoComercialSelected;
	private String ultimaTransportadoraSelected;
	private String ultimoTipoPagamentoSelected;
	private String ultimoTipoPedidoSelected;
	public boolean resultado;
	public String cdTipoPedidoAuto;
	public int nuSequenciaAgenda;
	public boolean showMessageConfirmClosePedido = true;
	public CadClienteMenuForm cadClienteMenuForm;
	private TotalizadoresPedidoForm totalizadoresPedidoForm;
	private ListParcelaPedidoForm listParcelaPedidoForm;
	private ListPagamentoPedidoForm listPagamentoPedidoForm;
	private int xTotalizadores, yTotalizadores;
	private boolean updateValuesPedidoByDtPagamento;

	private GridListContainer listGridBoleto;
	private GridListContainer listGridAtividadePedido; 
	private ListNotaFiscalForm listNotaFiscalForm;

	private Control lbMarcadores;
	
	private Cliente clienteOrigemBase;

	//Messages
	private static String BOTAO_CALCULADORA = "";
	private static String BOTAO_SALDO_BONI = "";
	private static String BOTAO_HISTORICO_TAB = "";
	private static String BOTAO_PREVISAO_DESCONTOS = "";
	private static String BOTAO_DESBLOQUEAR_PRECO = "";
	private static String BOTAO_DIFERENCAS = "";
	private static String BOTAO_ULTIMOS_PEDIDOS = "";
	private static String BOTAO_PRODUTOS_RETIRADA = "";
	private static String BOTAO_RENTABILIDADE = "";
	private static String BOTAO_FICHA_FINAN = "";
	private static String BOTAO_PESQUISA_MERCADO = "";
	private static String BOTAO_DESCONTO = "";
	private static String BOTAO_TROCA = "";
	private static String BOTAO_RELCOMISSAOPEDIDO = "";
	private static String BOTAO_CONTACORRENTECLI = "";
	private static String BOTAO_FOTOPEDIDO = "";
	private static String BOTAO_LIST_CCC = "";
	private static String BOTAO_CCC = "";
	private static String MENU_SUBSTITUICAO_TRIBUTARIA = "";
	private static String MENU_DESBLOQUEAR_LIMITADOR = "";
	private static String BOTAO_IMPRIMIR = "";
	private static String MENU_INFO_TRIBUTARIA_DETALHADA = "";
	private static String BOTAO_VISITA_FOTO = "";
	private static String BOTAO_CONVERTER_ORCAMENTO_PARA_PEDIDO = "";
	private static String BOTAO_PESO_GRUPO_PRODUTO = "";
	private static String BOTAO_REPLICAR_PEDIDO = "";
	private static String BOTAO_SUGESTAO_ITENS_RENTABILIDADE_IDEAL = "";
	private static String BOTAO_DETALHES_CALCULOS = "";
	private static String BOTAO_IMPRIMIR_BOLETO = "";
	private static String BOTAO_REGISTRAR_CHEGADA = "";
	private static String BOTAO_REGISTRAR_SAIDA = "";
	private static String BOTAO_CONFIGURACAO_DESCONTOS = "";
	private static String BOTAO_BONIFICACAO_RELACIONADA = "";
	private static String BOTAO_VENDA_RELACIONADA = "";
	private static String BOTAO_CANCELAR_PEDIDO = "";
	private static String BOTAO_REL_DESCONTOS = "";
	private static String BOTAO_REL_LIBERACOES_SENHA = "";
	private static String BOTAO_COTAS_COND_PAGTO = "";
	private static String BOTAO_PRODUTO_DESEJADO = "";
	private static String BOTAO_ANEXAR_DOC = "";
	private static String BOTAO_FAIXAS_DESC_PESO = "";
	private static String BOTAO_GERAR_PDF = "";
	private static String BOTAO_GERAR_PDF_VIA_CLIENTE = "";
	private static String BOTAO_PESQUISA_MERCADO_PRODUTO_CONCORRENTE = "";
	private static String BOTAO_ADICIONAR_KIT = "";

	public CadPedidoForm() throws SQLException {
		super(Messages.PEDIDO_NOME_ENTIDADE);
		//-- BUTTON's
		btNovoItem = new ButtonAction(LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop ? Messages.PEDIDO_LABEL_ITEM : Messages.BOTAO_ADICIONAR_ITEM, "images/add.png", true);
		btNovoItem.setID("btNovoItem");
		if (!LavenderePdaConfig.usaPedidoPerdido) {
		btExcluirPedido = new ButtonAction(FrameworkMessages.BOTAO_EXCLUIR, "images/delete.png", ColorUtil.buttonExcluirForeColor);
			btExcluirPedido.setID("btExcluirPedido");
		} else {
			btPerderPedido = new ButtonAction(Messages.BOTAO_PERDER_PEDIDO, "images/pedidoPerdido.png", ColorUtil.buttonExcluirForeColor);
		}
		btListaItens = new ButtonAction(Messages.BOTAO_ITENS_DO_PEDIDO, "images/list.png");
		btListaItens.setID("btListaItens");
		btListaItensNfe = new ButtonAction(Messages.BOTAO_ITENS_NFE, "images/list.png");
		btFecharPedido = new ButtonAction(Messages.BOTAO_FECHAR_PED, "images/fecharpedido.png");
		btFecharPedido.setID("btFecharPedido");
		btNotaFiscal = new ButtonAction(Messages.NOTA_FISCAL, "images/nfe.png");
		if ((LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep()) || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			btAlteraTransportadora = new BaseButton(Messages.FRETE_BUTTON_ALTERAR);
		}
		btRecalcularPedido = new ButtonAction(Messages.BOTAO_RECALCULAR, "images/calcular.png");
		btLibParcelaSolAutorizacao = new ButtonAction(Messages.BOTAO_LIBERAR_PARCELA, SolAutorizacaoService.IMAGES_SOLICITACAO_AUTORIZACAO_PNG);
		btQrCodePix = new ButtonAction(UiUtil.getIconButtonAction("images/pix.png"));
		if (LavenderePdaConfig.usaWorkflowStatusPedido) {
			btWorkflow = new ButtonAction(Messages.BT_WORKFLOW, "images/workflow.png");
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento || LavenderePdaConfig.usaLiberacaoProprioRep()) {
			btLiberarPedido = new ButtonAction(Messages.BOTAO_LIBERAR_PEDIDO, "images/liberarpedido.png");
			if (LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia()) {
				btDescontosConcedidos = new ButtonAction(Messages.BOTAO_LIBERARACAO_PEDIDO_PENDENTE_CONCEDIDOS, "images/desbloquear.png");
			} else {
				btDescontosConcedidos = new ButtonAction(Messages.BOTAO_DESCONTOS_CONCEDIDOS, "images/descontosconcedidos.png");
			}
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			btLiberarItensPendentes = new ButtonAction(Messages.BOTAO_LIBERAR_ITENS_PENDENTES, "images/liberaritens.png");
		}
		btReabrirPedido = new ButtonAction("");
		if (LavenderePdaConfig.permiteReabrirPedidoFechado) {
			btReabrirPedido = new ButtonAction(Messages.BOTAO_REABRIR_PEDIDO, "images/reabrirpedido.png");
		}
		btReabrirPedido.setID("btReabrirPedido");
		bmOpcoes = new ButtonOptions();
		bmOpcoes.setID("bmOpcoes");
		btRelacionarPedido = new BaseButton("", UiUtil.getColorfulImage("images/relacionar.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		btNovaCargaPedido = new BaseButton("", UiUtil.getColorfulImage("images/add.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		btConsignarPedido = new ButtonAction(Messages.BOTAO_CONSIGNAR_PEDIDO, "images/consignarPedido.png");
		btDevolucaoConsignacaoPedido = new ButtonAction(Messages.BOTAO_DEVOLUCAO_CONSIGNACAO_PEDIDO, "images/devolucaoPedidoConsignado.png");
		//-- Labels
		lbAreaVenda = new LabelName(" ");
		if (LavenderePdaConfig.usaAreaVendas && !LavenderePdaConfig.usaAreaVendaAutoNoPedido) {
			lbAreaVenda = new LabelName(Messages.PEDIDO_LABEL_AREAVENDA);
		}
		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			lbVlTotalPedidoComTributos = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_TRIBUTOS);
		} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			lbVlTotalPedidoComTributos = new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDOST);
		} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			lbVlTotalPedidoComTributos = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_IPI);
		} else {
			lbVlTotalPedidoComTributos = new LabelName(" ");
		}
		lbRentabilidade = new LabelName(Messages.PEDIDO_LABEL_PERCENTUAL_RENTABILIDADE);
		lbPrecoLiberadoSenha = new LabelValue(Messages.PEDIDO_PRECO_LIBERADO_SENHA);
		//-- COMBO's
		cbCondicaoPagamento = new CondicaoPagamentoComboBox(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO);
		cbCondicaoPagamento.setID("cbCondicaoPagamento");
		lbTabelaPreco = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbTabelaPreco.setID("cbTabelaPreco");
		cbTipoPagamento = new TipoPagamentoComboBox();
		cbTipoPagamento.setID("cbTipoPagamento");
		cbTipoPedido = new TipoPedidoComboBox(LavenderePdaConfig.tipoPedidoOcultoNoPedido, false);
		cbTipoPedido.setID("cbTipoPedido");
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			cbDivisaoVenda = new DivisaoVendaComboBox();
		}
		cbTipoFrete = new TipoFreteComboBox();
		cbClienteSetor = new ClienteSetorComboBox(LavenderePdaConfig.clienteComContratoExigeSetorPedido ? Messages.PEDIDO_LABEL_SETOR : "");
		cbClienteSetorOrigem = new ClienteSetorOrigemComboBox(LavenderePdaConfig.clienteComContratoExigeSetorPedido ? Messages.PEDIDO_LABEL_SETOR_ORIGEM : "");
		cbTipoEntrega = new TipoEntregaComboBox();
		cbAreaVenda = new AreaVendaComboBox(lbAreaVenda.getValue());
		cbRotaEntrega = new RotaEntregaComboBox();
		cbSegmento = new SegmentoComboBox();
		cbCondicaoComercial = new CondicaoComercialComboBox();
		cbEntrega = new EntregaComboBox();
		cbTransportadora = new TransportadoraComboBox();
		cbTransportadoraAux = new TransportadoraComboBox();
		cbTranspFretePersonalizado = new TransportadoraComboBox();
		cbCentroCusto = new CentroCustoComboBox();
		cbPlataformaVenda = new PlataformaVendaComboBox();
		cbItemConta = new ItemContaComboBox();
		cbClasseValor = new ClasseValorComboBox();
		cbModoFaturamento = new ModoFaturamentoComboBox();
		cbTributacao = new TributacaoComoBox();
		cbCargaPedido = new CargaPedidoComboBox();
		cbTipoVeiculo = new TipoVeiculoComboBox();
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			cbContatoErp = new ContatoErpComboBox(Messages.CONTATO_NOME_ENTIDADE);
		}
		cbCondicaoNegociacao = new CondicaoNegociacaoComboBox();
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade = new UnidadeComboBox();
		}

		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			cbStatusOrcamento = new StatusOrcamentoComboBox(false);
		}
		//--EDIT's
		lbDsCliente = new LabelValue("");
		lbDsCliente.setID("lbDsCliente");
		lbDsNmEmpresa = new LabelValue("");
		lbDsNmEmpresa.setID("lbDsNmEmpresa");
		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
			lbDsNmEmpresaCapa = new LabelValue("");
		}
		lvUsuarioItemLiberacao = new LabelValue();
		lbDsStatusPedido = new LabelValue("");
		lbDsStatusPedido.setID("lbDsStatusPedido");
		lbNuPedido = new LabelValue("");
		lbNuPedido.setID("lbNuPedido");
		lvNuPedidoOriginal = new LabelValue();
		lvNuPedidoOriginal.setID("lvNuPedidoOriginal");
		lbDsOrigemPedido = new LabelValue("");
		lbDsOrigemPedido.setID("lbDsOrigemPedido");
		lbNuPedidoRelacionado = new LabelValue("");
		edDsCondicaoPagamento = new LabelValue("");
		lvDsStatusAlvara = new LabelValue();
		lvVlTicketMedio = new LabelValue("0").useCurrency();
		lbVlVerbaPedido = new LabelName(Messages.VERBA_VERBAPEDIDO);
		lbVlVerbaPedidoPositiva = new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDOPOSITIVO);
		lbVlPctIndiceFinCondPagto = new LabelName(Messages.PEDIDO_LABEL_VLPCTINDICEFINCONDPAGTO);
		lbVlPctDescQuantidadePeso = new LabelName(Messages.PEDIDO_LABEL_VLPCTDESCQUANTIDADEPESO);
		lbPedidoRelacionado = new LabelName(Messages.PEDIDO_RELACIONADO);
		lbPctDescItem = new LabelName(Messages.PEDIDO_LABEL_SUGESTAODESCONTO_ITEM);
		lbPctAcrescimoItem = new LabelName(Messages.PEDIDO_LABEL_SUGESTAOACRESCIMO_ITEM);
		lbVlMinCondPagto = new LabelName(Messages.PEDIDO_LABEL_VLMINIMO);
		lbVlPrazoMedio = new LabelName(Messages.PEDIDO_PRAZO_MEDIO);
		lbNuParcelasPedido = new LabelName(Messages.PEDIDO_LABEL_NUMERO_PARCELA);
		lbVlMinTipoPed = new LabelName(Messages.PEDIDO_LABEL_VLMINIMO);
		lbVlMinTabelaPreco = new LabelName(Messages.PEDIDO_LABEL_VLMINIMO);
		lbVlMinTipoPagto = new LabelName(Messages.PEDIDO_LABEL_VLMINIMO_TIPOPAGAMENTO);
		edVlSaldoCCCliente = new EditNumberFrac("9999999999", 9, 3);
		edVlSaldoCCCliente.setEditable(false);
		lbPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO);
		lbPctDescontoCondicaoPagamento = new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CONDICAO_PAGAMENTO);
		lbPctDescontoHistoricoVendas = new LabelName(Messages.PEDIDO_LABEL_DESCONTO_HISTORICO_VENDAS);
		lbVlDescontoCondPagto = new LabelName(Messages.PEDIDO_LABEL_VALOR_DESCONTO);
		if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
			lbPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLDESCONTO);
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido) {
			lbPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTOPEDIDO);
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			lbPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_CLIENTE);
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0 || LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0 || LavenderePdaConfig.apresentaCampoPercentualDescontoCapaPedido() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				lbPctDesconto.setText(lbPctDesconto.getText() + " / " + Messages.PEDIDO_LABEL_PERC_MAX_DESCONTO);
			}
		}
		lvPercMaxDesconto = new LabelValue("@");
		if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0 || LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			lvPercMaxDesconto.useCurrencyValue = true;
		} else {
			lvPercMaxDesconto.usePercentValue = true;
		}
		//--
		edDsCondicaoPagamentoSemCadastro = new EditText("@@@@@@@@@@", 20);
		lbDtEmissao = new LabelValue();
		lbDtEmissao.setID("lbDtEmissao");
		edDtEntrega = new EditDate();
		edDtEntrega.setID("edDtEntrega");
		edDtSugestaoCliente = new EditDate();
		edDtCarregamento = new EditDate();

		int qtMaxCaracteres = LavenderePdaConfig.getQtMaxCaracteresOrdemCompraNoPedido();
		edNuOrdemCompraCliente1 = new EditNumberTextInteger((qtMaxCaracteres > 0) ? qtMaxCaracteres : 20);
		edNuOrdemCompraCliente2 = new EditText("@@@@@@@@@@", (qtMaxCaracteres > 0) ? qtMaxCaracteres : 20);

		edNuPedidoRelacionado = new EditText("@@@@@@@@@@", 20);
		edNuPedidoRelacionado.setEditable(false);
		edNuPedidoRelacionado.drawBackgroundWhenDisabled = true;
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			btRelacionarCampanhaPublicitaria = new BaseButton("", UiUtil.getColorfulImage("images/relacionar.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
			ckVinculaCampanhaPublicitaria = new CheckBoolean(Messages.CAMPANHA_PUBLICITARIA_PEDIDO_VIA_CAMPANHA_PUBLICITARIA);
			edNuCampanhaPublicitaria = new EditText("@@@@@@@@@@", 100);
			edNuCampanhaPublicitaria.setEditable(false);
			edNuCampanhaPublicitaria.drawBackgroundWhenDisabled = true;
		}
		lbVlTotalItens = new LabelValue("@").useCurrency();
		lbVlEfetivo = new LabelValue("@");
		lvVlTotalFrete = new LabelValue("@");
		lvVlTotalFrete.useCurrencyValue = true;
		edValorMinCondPagto = new EditNumberFrac("10000,000", 30);
		edValorMinCondPagto.drawBackgroundWhenDisabled = true;
		edValorMinCondPagto.alignment = LEFT;
		edValorMinCondPagto.setID("edValorMinCondPagto");
		edPrazoMedio = new EditNumberInt("10000,000", 30);
		edPrazoMedio.drawBackgroundWhenDisabled = true;
		edPrazoMedio.alignment = RIGHT;
		edNuParcelaPedido = new EditNumberInt("10000,000", 30);
		edNuParcelaPedido.drawBackgroundWhenDisabled = true;
		edNuParcelaPedido.alignment = RIGHT;
		edValorMinTipoPagto = new EditNumberFrac("10000,000", 30);
		edValorMinTipoPagto.drawBackgroundWhenDisabled = true;
		edValorMinTipoPagto.alignment = RIGHT;
		edValorMinTipoPedido = new EditNumberFrac("10000,000", 30);
		edValorMinTipoPedido.drawBackgroundWhenDisabled = true;
		edValorMinTipoPedido.alignment = RIGHT;
		edVlMinTabelaPreco = new EditNumberFrac("10000,000", 30);
		edVlMinTabelaPreco.drawBackgroundWhenDisabled = true;
		edVlMinTabelaPreco.alignment = RIGHT;
		lvVlTotalPedidoComTributosEDeducoes = new LabelValue("@");
		lvVlTotalPedidoComTributosEDeducoes.useCurrencyValue = true;
		lvVlTtPedidoTributosEDeducoesEDesc = new LabelValue("@");
		lvVlTtPedidoTributosEDeducoesEDesc.useCurrencyValue = true;
		lvVlTotalPedidoComImpostos = new LabelValue("@");
		lvVlTotalPedidoComImpostos.useCurrencyValue = true;
		lvVlTotalFreteItensPedido = new LabelValue("@");
		lvVlTotalFreteItensPedido.useCurrencyValue = true;
		edVlPctDesconto = new EditNumberFrac("9999999999", 9);
		lvVlDescontoCondPagto = new LabelValue("0,00");
		lvVlDescontoCondPagto.useCurrencyValue = true;
		edVlPctDescEspecial = new EditNumberFrac("9999999999", 9);
		edVlPctDescItem = new EditNumberFrac("9999999999", 9);
		edVlPctDescItem.autoSelect = true;
		edVlPctDescItem.setID("edVlPctDescItem");
		edVlPctAcrescimoItem = new EditNumberFrac("9999999999", 9);
		edVlPctAcrescimoItem.autoSelect = true;
		edVlPctAcrescimoItem.setID("edVlPctAcrescimoItem");
		edVlPctDescCondicao = new EditNumberFrac("9999999999", 9);
		edVlPctDescHistoricoVendas = new EditNumberFrac("9999999999", 9);
		edDescontoCascataManualDescCliente = new EditNumberFrac("9999999999", 9);
		edDescontoCascataManual2 = new EditNumberFrac("9999999999", 9);
		edDescontoCascataManual3 = new EditNumberFrac("9999999999", 9);
		edVlPctDescontoVendaMensal = new LabelValue("@");
		lvVlVerbaPedido = new LabelValue("@@@@@@@@@@");
		lvVlVerbaPedido.setID("lvVlVerbaPedido");
		lvVlPctIndiceFinCondPagto = new LabelValue("@");
		lvVlPctDescQuantidadePeso = new LabelValue("@");
		lvVlPctRentabilidade = new LabelValue("@");
		lvVlPctRentabilidade.setID("lvVlPctRentabilidade");
		lvVlEscalaRentabilidade = new LabelValue("@");
		lvVlEscalaRentabilidade.usePercentValue = false;
		btIconeRentabilidade = new BaseButton(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_FAIXA_ATINGIDA, PedidoUiUtil.getEmptyIcon(), LEFT, WIDTH_GAP);
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0)
			|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			btIconeRentabilidade.useBorder = false;
			btIconeRentabilidade.setBackColor(ColorUtil.formsBackColor);
		}
		btIconeComissaoPedido = new BaseButton(Messages.NENHUMA_COMISSAO_ATINGIDA, PedidoUiUtil.getEmptyIcon(), LEFT, WIDTH_GAP);
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			btIconeComissaoPedido.useBorder = false;
			btIconeComissaoPedido.setBackColor(ColorUtil.formsBackColor);
		}
		edVlVerbaPedidoPositiva = new EditNumberFrac("9999999", 9);
		edVlVerbaPedidoPositiva.setID("edVlVerbaPedidoPositiva");
		lbVlBonificacaoPedido = new LabelValue();
		lbVlBonificacaoPedido.setID("lbVlBonificacaoPedido");
		lvVlTotalMargem = new LabelValue("@");
		lvVlTotalMargem.useCurrencyValue = true;
		lvVlPctTotalMargem = new LabelValue("@");
		lvVlTotalPedidoComTributos = new LabelValue("@");
		lvVlTotalPedidoComTributos.setID("lvVlTotalPedidoComTributos");
		lvVlTotalPedidoComTributos.useCurrencyValue = true;
		lvVlTotalBrutoItens = new LabelValue("@");
		lvVlTotalBrutoItens.useCurrencyValue = true;
		lbVlTotalPedidoFrete = new LabelValue("");
		lbVlTotalPedidoFrete.useCurrencyValue = true;
		lbVlFrete = new LabelValue("");
		lbVlFrete.useCurrencyValue = true;
		lvVlPctComissao = new LabelValue("@");
		lbNmTransportadora = new LabelValue("");
		lbNmTipoFrete = new LabelValue("");
		lbVlTotalLiberado = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_LIBERADO);
		lvVlTotalLiberado = new LabelValue("@");
		lvVlTotalLiberado.useCurrencyValue = true;
		lbPercDescLibRestante = new LabelName(Messages.PEDIDO_LABEL_PERC_DESC_LIB_RESTANTE);
		lvPercDescLibRestante = new LabelValue("@");
		lbVlTotalPedido = new LabelValue("@");
		lbVlTotalPedido.setID("lbVlTotalPedido");
		lbVlTotalPedido.useCurrencyValue = true;
		lbVlMinPromocional = new LabelValue("@");
		lbVlMinPromocional.useCurrencyValue = true;
		lbVlTotalPedidoTroca = new LabelValue("@");
		lbVlTotalPedidoTroca.useCurrencyValue = true;
		lbVlTotalFrete = new LabelName(Messages.PEDIDO_LABEL_VLTOTALFRETE);
		lbQtPesoPedido = new LabelName(Messages.PRODUTO_LABEL_QTPESO);
		lvPesoPedido = new LabelValue("@");
		lvVlTotalVolumePedido = new LabelValue("@");
		lvVlTotalNotaCredito = new LabelValue("@@@@@@@@@@");
		lvVlTotalNotaCredito.useCurrencyValue = true;
		edRotaEntrega1 = new EditText("@@@@@@@@@", 20);
		edRotaEntrega2 = new EditNumberInt("@@@@@@@@@", 20);
		lvVlIndiceRentabPedido = new LabelValue("@");
		edVlPctFreteRep = new EditNumberFrac("9999999", 9);
		edVlFreteRep = new EditNumberFrac("9999999", 9);
		edVlFreteCli = new EditNumberFrac("9999999", 9);
		lbVlTotalPedidoComFrete = new LabelValue("@");
		lbVlTotalPedidoComFrete.useCurrencyValue = true;
		lbVlTotalFretePedido = new LabelValue("0.00");
		lbVlTotalFretePedido.useCurrencyValue = true;
		lbVlTotalPedidoAbaFrete = new LabelValue("@");
		lbVlTotalPedidoAbaFrete.useCurrencyValue = true;
		bgTaxaEntrega = new ButtonGroupBoolean();
		bgGeraCreditoBonificacaoCondicao = new ButtonGroupBoolean();
		lvGeraCreditoBonificacaoCondicao = new LabelValue();
		bgGeraCreditoBonificacaoFrete = new ButtonGroupBoolean();
		lvGeraCreditoBonificacaoFrete = new LabelValue();
		lvValorCreditoDisponivel = new LabelValue();
		lbPossuiTaxaEntrega = new LabelName(Messages.INFO_FRETE_LABEL_POSSUI_TAXA_ENTREGA);
		lbTaxaEntrega = new LabelName(Messages.INFO_FRETE_LABEL_VALOR_TAXA_ENTREGA);
		edVlTaxaEntrega = new EditNumberFrac("9999999", 9);
		edVlTaxaEntrega.setEnabled(false);
		lbPossuiAjudante = new LabelName(Messages.INFO_FRETE_LABEL_POSSUI_AJUDANTE);
		lbVlMaxPctDescFrete = new LabelValue();
		lbVlMaxPctDescFrete.usePercentValue = true;
		lbVlMaxPctDescCondicao = new LabelValue();
		lbVlMaxPctDescCondicao.usePercentValue = true;
		lbVlPctDescCondicaoPagamento = new LabelValue();
		lbVlPctDescCondicaoPagamento.usePercentValue = true;
		lvVlBruto = new LabelValue("0.00");
		lvVlBruto.useCurrencyValue = true;
		lvVlBrutoCapaPedido = new LabelValue("0.00");
		lvVlBrutoCapaPedido.setID("lvVlBrutoCapaPedido");
		lvVlBrutoCapaPedido.useCurrencyValue = true;
		lvVlDesconto = new LabelValue("0.00");
		lvVlDesconto.useCurrencyValue = true;
		edVlPctDescFrete = new EditNumberFrac("9999999999", 9);
		bgAjudante = new ButtonGroupBoolean();
		lbAjudante = new LabelName(Messages.INFO_FRETE_LABEL_QUANTIDADE_AJUDANTE);
		edQtAjudante = new EditNumberInt("99999", 9);
		edQtAjudante.setEnabled(false);
		lbAntecipaEntrega = new LabelName(Messages.INFO_FRETE_LABEL_ANTECIPA_ENTREGA);
		bgAntecipaEntrega = new ButtonGroupBoolean();
		lbPrecisaAgendamento = new LabelName(Messages.INFO_FRETE_LABEL_PRECISA_AGENDAMENTO);
		bgAgendamento = new ButtonGroupBoolean();
		lbHasNfe = new LabelValue(MessageUtil.quebraLinhas(Messages.NFE_LABEL_MSG_SEM_NFE));
		lbHasNfe.setVisible(false);
		lbHasBoleto = new LabelValue(MessageUtil.quebraLinhas(Messages.PEDIDOBOLETO_LABEL_SEM_BOLETO));
		lbHasBoleto.setVisible(false);
		lbVlTotalMargem = new LabelName(Messages.PEDIDO_LABEL_VLTOTALMARGEM);
		lbVlPctTotalMargem = new LabelName(Messages.PEDIDO_LABEL_VLPCTTOTALMARGEM);
		lvCdStatusNfe = new LabelValue("");
		lvDsNaturezaOperacao = new LabelValue("");
		lvVlChaveAcesso = new LabelValue("");
		lvVlSerieNfe = new LabelValue("");
		lvnuLote = new LabelValue("");
		lvDsObservacao = new LabelValue("");
		lbCdStatusNfe = new LabelName(Messages.NFE_LABEL_STATUSNFE);
		lbDsNaturezaOperacao = new LabelName(Messages.NFE_LABEL_DSNATUREZAOPERACAO);
		lbVlChaveAcesso = new LabelName(Messages.NFE_LABEL_VLCHAVEACESSO);
		lbVlSerieNfe = new LabelName(Messages.NFE_LABEL_VLSERIENFE);
		lbnuLote = new LabelName(Messages.NFE_LABEL_NULOTE);
		lbDsObservacao = new LabelName(Messages.NFE_LABEL_DSOBSERVACAO);
		lbNuNfe = new LabelName(Messages.NFE_LABEL_NUNFE);
		lbVlTotalProdutosNfe = new LabelName(Messages.NFE_LABEL_VLTOTALPRODUTOS);
		lbVlTotalNfe = new LabelName(Messages.NFE_LABEL_VLTOTALNFE);
		lbDtSaida = new LabelName(Messages.NFE_LABEL_DTSAIDA);
		lbHrSaida = new LabelName(Messages.NFE_LABEL_HRSAIDA);
		lbDtEmissaoNfe = new LabelName(Messages.NFE_LABEL_DTEMISSAO);
		lvNuNfe = new LabelValue();
		lvVlTotalProdutosNfe = new LabelValue();
		lvVlTotalNfe = new LabelValue();
		lvDtSaida = new LabelValue();
		lvHrSaida = new LabelValue();
		lvDtEmissaoNfe = new LabelValue();
		lvVlPedido = new LabelValue("0");
		lvVlPedido.useCurrencyValue = true;
		lvVlPedidoAberto = new LabelValue("0");
		lvVlPedidoAberto.useCurrencyValue = true;
		lvVlPedidoAberto.align = RIGHT;
		edNuKmInicial = new EditNumberInt("99999", 6);
		edNuKmFinal = new EditNumberInt("99999", 6);
		edHrInicialIndicado = new EditNumberMask("##:##");
		edHrFinalIndicado = new EditNumberMask("##:##");
		lbStatusOrcamento = new LabelName(Messages.STATUSORCAMENTO_LABEL_COMBO);
		lbObsOrcamento = new LabelName(Messages.OBSERVACAO_LABEL);
		listGridBoleto = new GridListContainer(4, 2);
		listGridBoleto.setColPosition(1, RIGHT);
		listGridBoleto.setColPosition(3, RIGHT);
		listGridBoleto.setBarTopSimple();
		bgAguardarPedidoComplementar = new ButtonGroupBoolean();
		lbPedidoComplementar = new LabelName(Messages.PEDIDO_AGUARDAR_PEDIDO_COMPLEMENTAR);

		bgPedidoGondola = new ButtonGroupBoolean();
		bgPedidoGondola.setID("bgPedidoGondola");

		//--
		listGridAtividadePedido = new GridListContainer(4, 2);
		listGridAtividadePedido.setColPosition(1, RIGHT);
		listGridAtividadePedido.setColPosition(2, LEFT);
		listGridAtividadePedido.setColPosition(3, RIGHT);
		listGridAtividadePedido.setBarTopSimple();
		lbVlTotalPedidoMaisFrete = new LabelValue(" ");
		lvVlBrutoPedidoMaisFrete = new LabelValue(" ");
		lbVlTotalPedidoMaisFrete.useCurrencyValue = true;
		lvVlBrutoPedidoMaisFrete.useCurrencyValue = true;
		lvVlBrutoPedidoMaisFrete.setID("lvVlBrutoPedidoMaisFrete");
		lbVlTotalSTPedido = new LabelValue("@");
		lbVlTotalSTPedido.setID("lbVlTotalSTPedido");
		lbVlTotalSTPedido.useCurrencyValue = true;
		edVlManualFrete = new EditNumberFrac("9999999999", 9);
		lbQtItensFaturados = new LabelValue("@");
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			lbVlSeguroPedido = new LabelName(Messages.PEDIDO_LABEL_VLSEGUROPEDIDO);
			lvVlSeguroPedido = new LabelValue("0,00");
			lvVlSeguroPedido.useCurrencyValue = true;
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			emObservacaoOrcamento = new EditMemo("", 3, 500);
		}
		emObservacaoModoFaturamento = new EditMemo("", 3, 500);
		emObservacaoModoFaturamento.drawBackgroundWhenDisabled = true;
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			lbDtEntrega = new LabelName(Messages.PEDIDO_LABEL_DTENTREGA);
			edDtEntregaManual = new EditDate();
		}
		lbTributacao = new LabelName(Messages.LABEL_TRIBUTACAO);
		lvVlPesoMinimoTabPreco = new LabelValue(" ");
		lvVlTotalPedidoPorPeso = new LabelValue(" ");
		lvVlValorParcelaPedido = new LabelValue(" ");
		lvVlValorParcelaPedido.useCurrencyValue = true;

		btIconeFaixa = new BaseButton(Messages.NENHUMA_PESO_FAIXA_ATINGIDA, PedidoUiUtil.getEmptyIcon(), LEFT, WIDTH_GAP);
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			btIconeFaixa.useBorder = false;
			btIconeFaixa.setBackColor(ColorUtil.formsBackColor);
		}

		//-- TOOLTIP's
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			new BaseToolTip(lbPctDescItem, Messages.PEDIDO_LABEL_SUGESTAO_DESC);
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			new BaseToolTip(lbPctAcrescimoItem, Messages.PEDIDO_LABEL_SUGESTAO_ACRESCIMO);
		}
		tipEmpresa = new BaseToolTip(lbDsNmEmpresa, "");
		tipCliente = new BaseToolTip(lbDsCliente, "");
		tipCdPgto = new BaseToolTip(edDsCondicaoPagamento, "");
		tipDsNaturezaOperacao = new BaseToolTip(lvDsNaturezaOperacao, "");
		tipVlChaveAcesso = new BaseToolTip(lvVlChaveAcesso, "");
		tipDsObservacao = new BaseToolTip(lvDsObservacao, "");
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
			tipVlMinCondPagto = new BaseToolTip(edValorMinCondPagto, Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
		if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
			tipPrazoMedio = new BaseToolTip(edPrazoMedio, Messages.CONDICAOPAGAMENTO_SEM_PRAZO_MED);
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			tipNuParcelaPedido = new BaseToolTip(edNuParcelaPedido, Messages.CONDICAOPAGAMENTO_SEM_PARCELA);
		}
		if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento) {
			tipVlMinTipoPagto = new BaseToolTip(edValorMinTipoPagto, Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) {
			tipVlMinTipoPedido = new BaseToolTip(edValorMinTipoPedido, Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
		if (LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
			tipVlMinTabelaPreco = new BaseToolTip(edVlMinTabelaPreco, Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			listParcelaPedidoForm = new ListParcelaPedidoForm();
		}
		totalizadoresPedidoForm = new TotalizadoresPedidoForm();
		totalizadoresPedidoForm.setPedido(getPedido());
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			cbEnderecoEntrega = new ClienteEnderecoComboBoxMultiLine();
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			cbEnderecoCobranca = new ClienteEnderecoComboBoxMultiLine(false, true);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			cbBoletoConfig = new BoletoConfigComboBox(Messages.DEBITOBANCARIO_LABEL_BANCO);
			edNuAgencia = new EditText("99999", 9);
			edNuConta = new EditText("@@@@@@@@@@", 30);
		}
		isPedidoConsignado = false;
		edDescCascataCategoria1 = new EditNumberFrac("999999999", 9);
		edDescCascataCategoria1.autoSelect = true;
		edDescCascataCategoria2 = new EditNumberFrac("999999999", 9);
		edDescCascataCategoria2.autoSelect = true;
		edDescCascataCategoria3 = new EditNumberFrac("999999999", 9);
		edDescCascataCategoria3.autoSelect = true;
		edVlDesconto = new EditNumberFrac("9999999999", 9);
		edVlDesconto.autoSelect = true;
		edPctDesconto = new EditNumberFrac("9999999999", 9);
		edPctDesconto.autoSelect = true;
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			edVlFreteAdicional = new EditNumberFrac("9999999999", 9);
			edVlFreteAdicional.autoSelect = true;
		}
		lbVlDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLDESCONTO + (LavenderePdaConfig.mostraPercDescMaxPedido ? " / " + Messages.PEDIDO_LABEL_PERC_MAX_DESCONTO : ""));
		lbEdPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTOPEDIDO + (LavenderePdaConfig.mostraPercDescMaxPedido ? " / " + Messages.ITEMPEDIDO_LABEL_VLPCTMAXDESCONTO : ""));
		lbVlBrutoPedidoMaisFrete = new LabelName(Messages.VALORBRUTOMAISFRETE);
		lbVlDespesaAcessoria = new LabelName(Messages.REL_LABEL_VALOR_DESPESA_ACESSORIA);
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			edCnpjTransportadora = new EditNumberMask("##.###.###/####-##");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			edDtPagamento = new EditDate();
		}

		lvVlTotalPedidoComissao = new LabelValue("0,00");
		lvVlTotalPedidoComissao.setID("lvVlTotalPedidoComissao");
		lvVlTotalPedidoComissao.useCurrencyValue = true;

		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			edVlPctVpc = new EditNumberFrac("9999999999", 9);
		}
		//-- NFCE
		lbHasNfce = new LabelValue(MessageUtil.quebraLinhas(Messages.NFCE_LABEL_MSG_SEM_NFCE));
		lbHasNfce.setVisible(false);
		lvQtTotalItem = new LabelValue("");
		lvVlTotalNfce = new LabelValue("");
		lvVlTotalDesconto = new LabelValue("");
		lvVlTotalLiquidoNfce = new LabelValue("");
		lvDsFormaPagamento = new LabelValue("");
		lvVlTotalPago = new LabelValue("");
		lvVlTroco = new LabelValue("");
		lvNuChaveAcesso = new LabelValue("");
		lvNuNfce = new LabelValue("");
		lvNuSerie = new LabelValue("");
		lvDtEmissaoNfce = new LabelValue("");
		lvHrEmissaoNfce = new LabelValue("");
		lvNuProtocoloAutorizacao = new LabelValue("");
		lvDtAutorizacao = new LabelValue("");
		lvHrAutorizacao = new LabelValue("");
		lvVlTotalTributos = new LabelValue("");
		lvVlPctTributosFederais = new LabelValue("");
		lvVlPctTributosEstaduais = new LabelValue("");
		lvVlPctTributosMunicipais = new LabelValue("");

		lbQtTotalItem = new LabelName(Messages.NFCE_LABEL_QTTOTAL);
		lbVlTotalNfce = new LabelName(Messages.NFCE_LABEL_VLTOTAL);
		lbVlTotalDesconto = new LabelName(Messages.NFCE_LABEL_VLTOTALDESCONTO);
		lbVlTotalLiquidoNfce = new LabelName(Messages.NFCE_LABEL_VLTOTALLIQUIDO);
		lbDsFormaPagamento = new LabelName(Messages.NFCE_LABEL_FORMA_PAGAMENTO);
		lbVlTotalPago = new LabelName(Messages.NFCE_LABEL_VLTOTALPAGO);
		lbVlTroco = new LabelName(Messages.NFCE_LABEL_VLTROCO);
		lbNuChaveAcesso = new LabelName(Messages.NFCE_LABEL_CHAVE_ACESSO);
		lbNuNfce = new LabelName(Messages.NFCE_LABEL_NU_NFCE);
		lbNuSerie = new LabelName(Messages.NFCE_LABEL_NU_SERIE);
		lbDtEmissaoNfce = new LabelName(Messages.NFCE_LABEL_DT_EMISSAO);
		lbHrEmissaoNfce = new LabelName(Messages.NFCE_LABEL_DT_EMISSAO);
		lbNuProtocoloAutorizacao = new LabelName(Messages.NFCE_LABEL_NU_PROTOCOLO_AUTORIZACAO);
		lbDtAutorizacao = new LabelName(Messages.NFCE_LABEL_DT_AUTORIZACAO);
		lbHrAutorizacao = new LabelName(Messages.NFCE_LABEL_HR_AUTORIZACAO);
		lbVlTotalTributos = new LabelName(Messages.NFCE_LABEL_VLTOTAL_TRIBUTOS);
		lbVlPctTributosFederais = new LabelName(Messages.NFCE_LABEL_VL_TRIBUTOS_FEDERAIS);
		lbVlPctTributosEstaduais = new LabelName(Messages.NFCE_LABEL_VL_TRIBUTOS_ESTADUAIS);
		lbVlPctTributosMunicipais = new LabelName(Messages.NFCE_LABEL_VL_TRIBUTOS_MUNICIPAIS);
		//--
		if (LavenderePdaConfig.mostraDescAcessoriaCapaPedido) {
			lvVlDespesaAcessoria = new LabelValue("0,00");
			lvVlDespesaAcessoria.useCurrencyValue = true;
		}
		if (LavenderePdaConfig.mostraPontuacaoPedidoBase() || LavenderePdaConfig.mostraPontuacaoPedidoRealizado()) {
			final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(0, 0, LavenderePdaConfig.mostraPontuacaoPedidoBase(), LavenderePdaConfig.mostraPontuacaoPedidoRealizado());
			if (vlPontuacao != null) {
				lvVlPontuacao = new LabelValue(" ");
				lvVlPontuacao.setID("lvVlPontuacao");
			}
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			lbPedidoCritico = new LabelName(Messages.PEDIDOPRODUTOCRITICO_BOTAO_LABEL);
			bgPedidoCritico = new ButtonGroupBoolean();
			bgPedidoCritico.setID("bgPedidoCritico");
			bgPedidoCritico.setValue(ValueUtil.VALOR_NAO);
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			bgUsaCodigoInternoCliente = new ButtonGroupBoolean();
		}
		if (LavenderePdaConfig.isExibeInformacoesFreteCapaPedidoEscolhaTransportadora()) {
			lvTransportadoraCapaPedido = new LabelValue("");
			lvTipoFreteCapaPedido = new LabelValue("");
			lvVlFreteCapaPedido = new LabelValue("@");
			lvVlFreteCapaPedido.useCurrencyValue = true;
		}
		if (SessionLavenderePda.getCliente() != null) {
			this.clienteOrigemBase = (Cliente) SessionLavenderePda.getCliente().clone();
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			bgUtilizaRentabilidade = new ButtonGroupBoolean();
		}
	}

	private void setLabelVlPctMaxDescontoPedido() throws SQLException {
		double vlPctMaxDesconto = 0;
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
			vlPctMaxDesconto = pedido.vlTotalItens * ((LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? pedido.getCliente().vlPctMaxDesconto : LavenderePdaConfig.permiteDescontoEmValorPorPedido) / 100);
		} else if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			vlPctMaxDesconto = pedido.vlTotalItens * (LavenderePdaConfig.aplicaDescontoNaCapaDoPedido / 100);
		} else if (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) {
			vlPctMaxDesconto = LavenderePdaConfig.permiteDescontoPercentualPorPedido;
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo()) {
				vlPctMaxDesconto = pedido.getCliente().vlPctMaxDesconto;
			}
		}
		lvPercMaxDesconto.setValue(StringUtil.getStringValueToInterface(vlPctMaxDesconto));
	}

	//-----------------------------------------------

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected String getDsTable() throws SQLException {
		return Pedido.TABLE_NAME_PEDIDO;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PedidoService.getInstance();
	}

	private PedidoService getPedidoService() throws SQLException {
		return (PedidoService) getCrudService();
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		Pedido pedido = new Pedido();
		pedido.nuSequenciaAgenda = this.nuSequenciaAgenda;
		return pedido;
	}

	public Pedido getPedido() throws SQLException {
		return (Pedido) getDomain();
	}

	public void edit(BaseDomain domain) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		getPedidoService().findItemPedidoList(pedido);
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			PedidoService.getInstance().findParcelaPedidoList(pedido);
		}
		SessionLavenderePda.setCliente(pedido.getCliente());
		if ((LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) && pedido.isPagamentoAVista()) {
			SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = pedido.flPagamentoAVista;
		}
		ClienteService.getInstance().setVlTotalPedClienteExcetoPedParam(pedido, SessionLavenderePda.getCliente());
		ClienteService.getInstance().setVlTotalPedClienteExcetoPedConsignadoParam(pedido, SessionLavenderePda.getCliente());
		if (LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()){
			pedido.parcelaMinMaxPendenteOuAutorizada = SolAutorizacaoService.getInstance().hasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX);
		}
		//--
		internalSetEnabled(pedido.isPedidoAbertoEditavel() && !inOnlyConsultaItens, false);
		controlEnvioAutoPedido = LavenderePdaConfig.sugereEnvioAutomaticoPedido || LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao;
		//--
		if(LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			pedido.cdRegiao = "";
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem() && pedido.dtPagamento != null && pedido.dtPagamento.isBefore(DateUtil.getCurrentDate()) && pedido.isPedidoAberto()) {
			pedido.dtPagamento = DateUtil.getCurrentDate();
			updateValuesPedidoByDtPagamento = true;
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep() && pedido.getTransportadora() != null && pedido.isPedidoAberto()) {
			pedido.transportadoraCep = TransportadoraCepService.getInstance().findTransportadoraCepByCdTransportadoraAndDsCepComercial(pedido.cdTransportadora, pedido.getCliente().dsCepComercial);
			if (pedido.transportadoraCep == null) {
				ListTransportadoraCepWindow listTransportadoraCepWindow = new ListTransportadoraCepWindow(pedido);
				listTransportadoraCepWindow.popup();
				this.atualizaFrete(this.getPedido(), false);
			}
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && !pedido.getCliente().isFreteEmbutido()) {
			PedidoService.getInstance().loadVlFreteDestacadoPedido(pedido);
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && pedido.isPedidoAberto() && pedido.isPedidoVenda() && ItemPedidoService.getInstance().isPossuiItensDescProgressivoPersonalizadoExtrapolados(pedido)) {
			UiUtil.showWarnMessage(Messages.DESC_PROG_CONFIG_VIGENCIA_EXTRAPOLADA_ERROR);
			PedidoService.getInstance().recalculaItensDescProgressivoPersonalizadoExtrapolados(pedido);
		}
		if (!pedido.isPedidoTransmitido() && LavenderePdaConfig.usaVigenciaDescQuantidade && ItemPedidoService.getInstance().isPossuiProdutosDescQtdVencidos(pedido)) {
			UiUtil.showWarnMessage(Messages.MSG_ITENS_DESCQTD_VENCIDOS_DETALHE);
			PedidoService.getInstance().recalculaItensDescQtdVencidos(pedido);
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && pedido.getCliente().isNovoCliente()) {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && pedido.isPedidoAberto() && (pedido.statusOrcamento == null || ValueUtil.isEmpty(pedido.statusOrcamento.cdStatusOrcamento))) {
			StatusOrcamentoService.getInstance().fillStatusOrcamentoPedido(pedido);
		}
		super.edit(domain);
		//--
		ultimoClienteExibido = pedido.cdCliente;
		pedido.oldCdCondicaoPagamento = pedido.cdCondicaoPagamento;
		pedido.oldCdTipoPedido = pedido.cdTipoPedido;
		pedido.oldCdCondicaoNegociacao = pedido.cdCondNegociacao;

		if (LavenderePdaConfig.isUsaAgendaDeVisitas() && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			pedido.setVisita(VisitaService.getInstance().findVisitaByPedido(pedido));
		}
		//--
		addItensOnButtonMenu();

		if (TABPANEL_PAGAMENTOS != 0 && listPagamentoPedidoForm != null) {
			listPagamentoPedidoForm.pedido = getPedido();
			listPagamentoPedidoForm.list();
		}
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().loadPedidoLog(pedido);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().loadCreditosPedido(pedido);
		}
		atualizaInfosDinamicasDomain();
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().initializeControlMaps(pedido);
		}
		super.domainToScreen(pedido);
		if (LavenderePdaConfig.usaPoliticaComercial() && pedido.isPedidoAbertoEditavel() && !pedido.isReplicandoPedido) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
			PedidoService.getInstance().loadPoliticaComercialOnEdit(pedido);
		}
	}

	public void add() throws java.sql.SQLException {
		super.add();
		//--
		Pedido pedido = getPedido();
		Cliente cliente = SessionLavenderePda.getCliente();
		pedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedido.cdCliente = cliente.cdCliente;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			String cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			pedido.cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentante;
			pedido.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		} else {
			pedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		PedidoService.getInstance().loadStatusPedidoPda(pedido);
		pedido.nuVersaoSistemaOrigem = LavendereConfig.getInstance().version;
		pedido.dtEmissao = DateUtil.getCurrentDate();
		pedido.hrEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.setCliente(cliente);
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() && (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0 || LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem())) {
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
				pedido.vlPctDescCliente = pedido.getCliente().vlPctMinDesconto;
			} else {
				pedido.vlDesconto = pedido.getCliente().vlPctMaxDesconto;
			}
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
			pedido.vlDesconto = pctMaxDescontoCanal;
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && cliente.isNovoCliente()) {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_SIM;
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			pedido.flEtapaVerba = "1";
		}
		if (isPermitePedidoAVistaClienteBloqueadoPorAtraso() || LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
			pedido.flPagamentoAVista = cliente.flClienteLiberadoPedidoAVista;
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			pedido.flKeyAccount = pedido.getCliente().flKeyAccount;
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			ClienteEndereco clienteEnderecoEntregaPadrao = ClienteEnderecoService.getInstance().getClienteEnderecoPadrao(pedido);
			if (clienteEnderecoEntregaPadrao != null) {
				pedido.cdEnderecoCliente = clienteEnderecoEntregaPadrao.cdEndereco;
			}
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			ClienteEndereco clienteEnderecoCobrancaPadrao = ClienteEnderecoService.getInstance().getClienteEnderecoPadrao(pedido, true);
			if (clienteEnderecoCobrancaPadrao != null) {
				pedido.cdEnderecoCobranca = clienteEnderecoCobrancaPadrao.cdEndereco;
			}
		}
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			if (cliente.vlIndiceFinanceiro > 0 && cliente.vlIndiceFinanceiro < 1) {
				pedido.vlPctDescCliente = (1 - cliente.vlIndiceFinanceiro) * 100;
			}
		}
		ultimoClienteExibido = pedido.cdCliente;
		controlHrFimEmissao = true;
		naoAvisaClienteAtrasado = !LavenderePdaConfig.validaClienteAtrasadoApenasAoFecharPedido;
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			StatusOrcamentoService.getInstance().fillStatusOrcamentoPedido(pedido);
		}
		if (LavenderePdaConfig.usaWebserviceSankhyaComplementaPedido && !pedido.isPedidoNovoCliente()) {
			PedidoService.getInstance().getAndSetInformacoesComplementaresWebserviceSankhya(pedido);
			if (ValueUtil.isNotEmpty(pedido.dtEntrega) && edDtEntrega != null) {
				edDtEntrega.setValue(pedido.dtEntrega);
			}
		}
		//--
		addItensOnButtonMenu();
		atualizaInfosDinamicasDomain();
		if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoCapaPedido()) {
			totalizadoresPedidoForm.setPedido(pedido);
		}
		if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && BonifCfgService.getInstance().isPedidoComBonifCfg(pedido)) {
			UiUtil.showInfoMessage(Messages.BONIFCFGCONTACORRENTE_POSSUI_POLITICA);
		}
	}

	private boolean isPermitePedidoAVistaClienteBloqueadoPorAtraso() {
		return LavenderePdaConfig.isApresentaPopUPPedidoAVista() || LavenderePdaConfig.isApresentaPopUPPedidoAVistaSenha();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		super.screenToDomain();
		Pedido pedido = getPedido();
		pedido.cdTabelaPreco = cbTabelaPreco.getValue();
		if (naoUsaTabelaPreco()) {
			pedido.cdTabelaPreco = "";
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			pedido.dsCondicaoPagamentoSemCadastro = edDsCondicaoPagamentoSemCadastro.getValue();
		}
		if (!PedidoUiUtil.isUsaCondicaoPgtoTipoFretePedidoRelacionado(pedido) && PedidoUiUtil.isPedidoUsaCondicaoPagamento(pedido)) {
			pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
		}
		pedido.setPagamentoAVista();
		pedido.cdTipoPedido = cbTipoPedido.getValue();
		if (isPedidoQueUsaTipoPagamento(pedido)) {
		pedido.cdTipoPagamento = cbTipoPagamento.getValue();
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				pedido.flGeraNfe = tipoPedido != null ? tipoPedido.flGeraNfe : "";
			}
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto || LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				boolean geraBoletoComTipoPagamento = !LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia();
				if (!geraBoletoComTipoPagamento || LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
				if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido() && pedido.getPagamentoPedido() != null) {
					TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pedido.getPagamentoPedido().cdTipoPagamento);
						geraBoletoComTipoPagamento = tipoPagamento != null ? ValueUtil.getBooleanValue(tipoPagamento.flBoleto) : false;
				} else if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && pedido.pagamentoPedidoList != null && pedido.pagamentoPedidoList.size() > 0) {
						geraBoletoComTipoPagamento = PagamentoPedidoService.getInstance().isAnyTipoPagamentoGeraBoletoFromPagamentoPedidoList(pedido);
				}
			}
				pedido.flGeraBoleto = tipoPedido != null ? StringUtil.getStringValue(ValueUtil.getBooleanValue(tipoPedido.flGeraBoleto) && geraBoletoComTipoPagamento) : "";
				if (ValueUtil.isNotEmpty(pedido.cdTipoPagamento) && ValueUtil.getBooleanValue(pedido.flGeraBoleto)) {
					TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pedido.cdTipoPagamento);
					pedido.flGeraBoleto = tipoPagamento != null ? StringUtil.getStringValue(tipoPagamento.flBoleto) : pedido.flGeraBoleto;
				}
			}
			if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfce()) {
				pedido.flGeraNfce = tipoPedido != null ? tipoPedido.flGeraNfce : "";
		}
		}
		pedido.cdTipoEntrega = cbTipoEntrega.getValue();
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			pedido.cdDivisaoVenda = cbDivisaoVenda.getValue();
		}
		if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 1) {
			pedido.cdRotaEntrega = edRotaEntrega1.getValue();
		} else if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 2) {
			pedido.cdRotaEntrega = edRotaEntrega2.getText();
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
			pedido.cdRotaEntrega = cbRotaEntrega.getValue();
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			pedido.vlPctDescItem = edVlPctDescItem.getValueDouble();
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			pedido.vlPctAcrescimoItem = edVlPctAcrescimoItem.getValueDouble();
		}
		pedido.cdSegmento = cbSegmento.getValue();
		pedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
		pedido.cdEntrega = cbEntrega.getValue();
		pedido.setCondicaoNegociacao(cbCondicaoNegociacao.getValue());
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial && !LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
			CondicaoComercial condicaoComercial = CondicaoComercialService.getInstance().getCondicaoComercial(pedido.cdCondicaoComercial);
			if(condicaoComercial != null) {
				pedido.cdTabelaPreco = condicaoComercial.cdTabelaPreco;
			}
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			pedido.cdUnidade = cbUnidade.getValue();
		}
		if (LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao() || LavenderePdaConfig.exibirInformacoesFreteCapaPedido()) {
			pedido.cdTransportadora = !pedido.isPedidoAberto() && ValueUtil.isEmpty(cbTransportadora.getValue()) ? pedido.cdTransportadora : cbTransportadora.getValue();
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar && LavenderePdaConfig.usaTransportadoraPedido()) {
			pedido.cdTransportadoraAux = cbTransportadoraAux.getValue();
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManual()) {
			pedido.vlFrete = edVlManualFrete.getValueDouble();
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			pedido.dtEntrega = edDtEntregaManual.getValue();
		} else {
			pedido.dtEntrega = (!LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga() || !PedidoService.getInstance().isPedidoRelacionadoCarga(pedido.cdCargaPedido)) ? edDtEntrega.getValue() : null;
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			pedido.dtSugestaoCliente = edDtSugestaoCliente.getValue();
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			pedido.dtCarregamento = edDtCarregamento.getValue();
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoApenasNumeros()) {
 			pedido.nuOrdemCompraCliente = edNuOrdemCompraCliente1.getText();
		} else if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos()) {
			pedido.nuOrdemCompraCliente = edNuOrdemCompraCliente2.getText();
		}
		SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			pedido.cdSetor = cbClienteSetor.getValue();
			pedido.cdOrigemSetor = cbClienteSetorOrigem.getValue();
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			pedido.cdAreaVenda = cbAreaVenda.getValue();
		}
		if (LavenderePdaConfig.usaAreaVendaAutoNoPedido) {
			Vector areas = AreaVendaService.getInstance().findAllByCdCliente(pedido.cdCliente);
			AreaVenda areaVenda = new AreaVenda();
			if (areas != null && areas.size() > 0) {
				areaVenda = (AreaVenda) areas.items[0];
			}
			pedido.cdAreaVenda = areaVenda.cdAreavenda;
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			pedido.cdTipoFrete = PedidoUiUtil.isUsaCondicaoPgtoTipoFretePedidoRelacionado(pedido) ? pedido.cdTipoFrete : cbTipoFrete.getValue();
			if (LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
				pedido.nuCnpjTransportadora = edCnpjTransportadora.getText().replaceAll("[^0-9]*", "");
			}
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente) {
			pedido.vlPctFreteRepresentante = edVlPctFreteRep.getValueDouble();
			pedido.vlFreteRepresentante = edVlFreteRep.getValueDouble();
			pedido.vlFreteCliente = edVlFreteCli.getValueDouble();
		}
		if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao())) {
			pedido.nuPedidoRelBonificacao = edNuPedidoRelacionado.getText();
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && pedido.isPedidoTroca()) {
			pedido.nuPedidoRelTroca = edNuPedidoRelacionado.getText();
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			pedido.vlTotalPedidoEstoquePositivo = lbVlEfetivo.getDoubleValue();
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			pedido.cdCentroCusto = cbCentroCusto.getValue();
			pedido.centroCusto = (CentroCusto) cbCentroCusto.getSelectedItem();
			pedido.cdItemConta = cbItemConta.getValue();
			pedido.cdClasseValor = cbClasseValor.getValue();
			pedido.cdModoFaturamento = cbModoFaturamento.getValue();
			pedido.dsObsModoFaturamento = emObservacaoModoFaturamento.getText();
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			pedido.nuKmInicial = edNuKmInicial.getValueInt();
			pedido.nuKmFinal = edNuKmFinal.getValueInt();
			pedido.hrInicialIndicado = ValueUtil.isNotEmpty(edHrInicialIndicado.getValue()) ? edHrInicialIndicado.getText() : ValueUtil.VALOR_NI;
			pedido.hrFinalIndicado =  ValueUtil.isNotEmpty(edHrFinalIndicado.getValue()) ? edHrFinalIndicado.getText() : ValueUtil.VALOR_NI;
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			String cdTipoVeiculo = LavenderePdaConfig.obrigaInfoAdicionalFreteNoPedido ? cbTipoVeiculo.getValue() : null;
			pedido.setInfoFretePedido(bgTaxaEntrega.getValue(), edVlTaxaEntrega.getValueDouble(), bgAjudante.getValue(), edQtAjudante.getValueInt(), bgAntecipaEntrega.getValue(), bgAgendamento.getValue(), cdTipoVeiculo);
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			pedido.flGeraCreditoFrete = ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoFrete.getValue()) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			PedidoService.getInstance().calculaValorTotalCreditoFrete(pedido);
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			pedido.flGeraCreditoCondicao = ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoCondicao.getValue()) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			PedidoService.getInstance().calculaValorTotalCreditoCondicao(pedido);
		}
		if (isShowComboTributacaoNovoCliente()) {
			pedido.cdTributacaoCliente = cbTributacao.getValue();
			pedido.getCliente().cdTributacaoCliente = cbTributacao.getValue();
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			pedido.cdCargaPedido = cbCargaPedido.getValue();
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			pedido.cdEnderecoCliente = cbEnderecoEntrega.getValue();
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			pedido.cdEnderecoCobranca = cbEnderecoCobranca.getValue();
		}
		if (LavenderePdaConfig.usaPedidoComplementar() && pedido.isPedidoComplementar()) {
			pedido.nuPedidoComplementado = edNuPedidoRelacionado.getText();
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
			pedido.vlPctDescFrete = edVlPctDescFrete.getValueDouble();
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			pedido.vlPctDescCliente = edVlPctDesconto.getValueDouble();
		}
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
			pedido.vlDescontoIndiceFinanCliente = ValueUtil.round(pedido.vlTotalBrutoItens * pedido.vlPctDesconto /100);
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			pedido.vlDesconto = edVlDesconto.getValueDouble();
			pedido.vlPctDesconto = edPctDesconto.getValueDouble();
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			pedido.vlPctDescontoCondicao = edVlPctDescCondicao.getValueDouble();
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			pedido.vlPctDescontoCondicao = edVlPctDescCondicao.getValueDouble();
			pedido.vlPctDescHistoricoVendas = edVlPctDescHistoricoVendas.getValueDouble();
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			pedido.cdContato = cbContatoErp.getValue();
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido() && ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados)) {
			pedido.setPagamentoPedido(cbBoletoConfig.getValue(), edNuAgencia.getText(), edNuConta.getText());
		}
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			pedido.vlPctDescCliente = edDescontoCascataManualDescCliente.getValueDouble();
			pedido.vlPctDescontoCondicao = edDescontoCascataManual2.getValueDouble();
			pedido.vlPctDescFrete = edDescontoCascataManual3.getValueDouble();
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			pedido.vlPctDescCliente = edDescCascataCategoria1.getValueDouble();
			pedido.vlPctDesc2 = edDescCascataCategoria2.getValueDouble();
			pedido.vlPctDesc3 = edDescCascataCategoria3.getValueDouble();
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			pedido.cdStatusOrcamento = cbStatusOrcamento.getValue();
			pedido.dsObsOrcamento = emObservacaoOrcamento.getValue();
			pedido.statusOrcamento = (StatusOrcamento) cbStatusOrcamento.getSelectedItem();
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			pedido.dtPagamento = edDtPagamento.getValue();
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			pedido.nuParcelas = edNuParcelaPedido.getValueInt();
		}
		pedido.flAguardaPedidoComplementar = bgAguardarPedidoComplementar.getValue();
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			pedido.flCritico = bgPedidoCritico.getValue();
		}
		if ( edVlPctVpc != null) pedido.vlPctVpc = edVlPctVpc.getValueDouble();
		if (LavenderePdaConfig.usaGondolaPedido) {
			pedido.flGondola = bgPedidoGondola.getValue();
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			pedido.flCodigoInternoCliente = bgUsaCodigoInternoCliente.getValue();
		}
		if(LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			pedido.cdPlataformaVenda = cbPlataformaVenda.getValue();
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			pedido.vlFreteAdicional = edVlFreteAdicional.getValueDouble();
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			pedido.flUtilizaRentabilidade = bgUtilizaRentabilidade.getValue();
		}
		//--
		return pedido;
	}

	private boolean naoUsaTabelaPreco() {
		return !LavenderePdaConfig.isUsaTabelaPrecoPedido() && !LavenderePdaConfig.usaTabelaPrecoPorSegmento
				&& !LavenderePdaConfig.usaTabelaPrecoPorCanalAtendimento && !LavenderePdaConfig.usaTabelaPrecoPorCliente
				&& !LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento && !LavenderePdaConfig.usaTabelaPrecoPorTipoPedido;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido) domain;
		super.domainToScreen(pedido);
		lbDsStatusPedido.setValue(pedido.getDsStatusPedido());
		if (pedido.isFlOrigemPedidoPda()) {
			lbNuPedido.setValue(pedido.nuPedido);
			lbNuPedidoRelacionado.setValue(pedido.nuPedidoRelacionado);
		} else {
			//Quando vem o Erp o número do pedido no Pda passa para o campo Relacionado
			lbNuPedido.setValue(pedido.nuPedidoRelacionado);
			lbNuPedidoRelacionado.setValue(pedido.nuPedido);
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			lvNuPedidoOriginal.setValue(pedido.nuPedidoOriginal);
		}
		lbDsOrigemPedido.setValue(pedido.getDsOrigemPedido());
		lbDtEmissao.setValue(pedido.dtEmissao);
		edDtEntrega.setValue(pedido.dtEntrega);
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			edDtSugestaoCliente.setValue(pedido.dtSugestaoCliente);
		}
		if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga() && ValueUtil.isNotEmpty(pedido.getCargaPedido().dtEntrega)) {
			edDtEntrega.setValue(pedido.getCargaPedido().dtEntrega);
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			edDtCarregamento.setValue(pedido.dtCarregamento);
		}
		cbTipoEntrega.setValue(pedido.cdTipoEntrega);
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			cbDivisaoVenda.carregaDivisaoVenda();
			cbDivisaoVenda.setValue(pedido.cdDivisaoVenda);
		}
		//-- TIPO PEDIDOc
		ultimoTipoPedidoSelected = pedido.cdTipoPedido;
		if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos() || LavenderePdaConfig.usaTipoPedidoPorCliente || SessionLavenderePda.isUsuarioSupervisor()) {
			cbTipoPedido.carregaTipoPedidos(pedido);
			if (LavenderePdaConfig.isUsaTipoFretePedido() && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
				cbTipoFrete.loadTipoFrete(pedido);
			}
		}
		cbTipoPedido.setValue(pedido.cdTipoPedido);
		if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			if (pedido.isPedidoBonificacao() && pedido.getTipoPedido() != null && (pedido.getTipoPedido().isFlTipoCreditoCondicao() || pedido.getTipoPedido().isFlTipoCreditoFrete())) {
				lvValorCreditoDisponivel.setValue(PedidoUiUtil.getValorCreditoDisponivel(pedido));
			} else {
				lvValorCreditoDisponivel.setValue(0d);
			}
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			carregaPctDescontoVolumeVendas(pedido);
		}
			remontaTela();
		//-- SEGMENTO
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			cbSegmento.load(pedido.getCliente());
			cbSegmento.setValue(pedido.cdSegmento);
		}
		//-- CONDICAO COMERCIAL
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			cbCondicaoComercial.carregaCondicoesComerciais(pedido);
			cbCondicaoComercial.setValue(pedido.cdCondicaoComercial);
			ultimaCondicaoComercialSelected = pedido.cdCondicaoComercial;
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			cbEntrega.load(pedido);
			if (!isEnabled() && !pedido.isPedidoAberto()) {
				cbEntrega.addEntregaByPedido(pedido);
			}
			cbEntrega.setValue(pedido.cdEntrega);
		}
		//-- CONDICAO NEGOCIACAO
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			cbCondicaoNegociacao.setValue(pedido.cdCondNegociacao);
		}
		//-- TRANSPORTADORA

		if (LavenderePdaConfig.exibirInformacoesFreteCapaPedido()) {
			cbTransportadora.carregaTransportadoras(pedido, false);
			cbTranspFretePersonalizado.carregaTransportadoras(pedido, false);
			cbTransportadora.setValue(pedido.cdTransportadora);
			cbTranspFretePersonalizado.setValue(pedido.cdTransportadora);
			ultimaTransportadoraSelected = pedido.cdTransportadora;
		} else if (LavenderePdaConfig.usaTransportadoraPedido()) {
			cbTransportadora.carregaTransportadoras(pedido, false);
			cbTransportadora.setValue(pedido.cdTransportadora);
			ultimaTransportadoraSelected = pedido.cdTransportadora;
			if (LavenderePdaConfig.usaTransportadoraAuxiliar) {
				cbTransportadoraAux.carregaTransportadoras(pedido, false);
				cbTransportadoraAux.setValue(pedido.cdTransportadoraAux);
			}
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManual()) {
			edVlManualFrete.setValue(pedido.vlFrete);
		}
		// -- ROTA DE ENTREGA
		if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 1) {
			edRotaEntrega1.setValue(pedido.cdRotaEntrega);
		} else if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 2) {
			edRotaEntrega2.setValue(ValueUtil.getIntegerValue(pedido.cdRotaEntrega));
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
			cbRotaEntrega.load(pedido.getCliente().cdCliente);
			cbRotaEntrega.setValue(pedido.cdRotaEntrega);
		}
		//-- TABELA PREÇO
		if (!LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
			cbTabelaPreco.loadTabelasPrecos(pedido);
			/*
			 * if (pedido.isPedidoTroca() && pedido.getCliente() != null) {
			 * cbTabelaPreco.setValue(pedido.getCliente().cdTabelaPreco); } else {
			 */
				cbTabelaPreco.setValue(pedido.cdTabelaPreco);
			
			}
		setValueEdVlMinTabelaPreco(pedido);
		//-- UNIDADE ALTERNATIVA
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.defaultItemType = UnidadeComboBox.DefaultItemType_SELECT_ONE_ITEM;
			cbUnidade.load();
			cbUnidade.setValue(pedido.cdUnidade);
		}
		// CONDICAO PAGAMENTO
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			cbCondicaoPagamento.loadCondicoesPagamento(pedido);

			cbCondicaoPagamento.setValue(pedido.cdCondicaoPagamento);
			ultimaCondPgamentoSelected = pedido.cdCondicaoPagamento;
		}

		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			edDsCondicaoPagamentoSemCadastro.setValue(pedido.dsCondicaoPagamentoSemCadastro);
		}
		if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
			cbTabelaPreco.loadTabelasPrecos(pedido);
			cbTabelaPreco.setValue(pedido.cdTabelaPreco);
		}
		ultimaTabelaPrecoSelected = pedido.cdTabelaPreco;
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) setValueEdValorMinTipoPedido(pedido);
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
			double qtMinValor = pedido.getCondicaoPagamento() != null ? pedido.getCondicaoPagamento().getQtMinValor(pedido.cdTabelaPreco) : 0;
			setEdValorMinCondicaoPagamento(qtMinValor);
		}
		if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
			setEdPrazoMedio(pedido.getCondicaoPagamento().qtDiasMediosPagamento);
		}
		setEdNuParcelaPedido(pedido.getCondicaoPagamento().nuParcelas, pedido.getCondicaoPagamento().isPermiteEditarParcelas());
		edDsCondicaoPagamento.setValue(pedido.getCondicaoPagamento() == null ? "" : pedido.getCondicaoPagamento().toString());
		if (!(pedido.isPedidoAberto() && !inOnlyConsultaItens) && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			tipCdPgto.setText(pedido.getCondicaoPagamento() == null ? "" : pedido.getCondicaoPagamento().getDescription());
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			bgGeraCreditoBonificacaoCondicao.setValue(pedido.isGeraCreditoCondicao() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
			lvGeraCreditoBonificacaoCondicao.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoCondicao));
			lvGeraCreditoBonificacaoCondicao.setVisible(pedido.isGeraCreditoCondicao());
		}
		//-- TIPO PAGAMENTO
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			boolean tipoPedidoExcecaoCondPagto = tipoPedido != null ? tipoPedido.isExcecaoCondPagto() : false;
			if (pedido.isPedidoAberto()) {
				cbTipoPagamento.carregaTipoPagamentos(pedido, false);
			} else if (ValueUtil.isNotEmpty(pedido.cdTipoPagamento)) {
				cbTipoPagamento.add(getTipoPagamentoSelected(pedido));
			}
			cbTipoPagamento.setValue(pedido.cdTipoPagamento);
			ultimoTipoPagamentoSelected = pedido.cdTipoPagamento;
			if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento && (pedido.getTipoPagamento() != null)) {
				double qtMinValor = pedido.getTipoPagamento().qtMinValor;
				if (qtMinValor > 0) {
					edValorMinTipoPagto.setValue(qtMinValor);
					tipVlMinTipoPagto.setText(Messages.VALOR_MINIMO_PEDIDO + ": |" + StringUtil.getStringValueToInterface(qtMinValor));
				} else {
					edValorMinTipoPagto.setText("");
					tipVlMinTipoPagto.setText(Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
				}
			}
		}
		//-- TIPO FRETE
		if (LavenderePdaConfig.isUsaTipoFretePedido() && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			cbTipoFrete.loadTipoFrete(pedido);
			if (LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
				edCnpjTransportadora.setValue(pedido.nuCnpjTransportadora);
			}
		}
		cbTipoFrete.setValue(pedido.cdTipoFrete, pedido.getCliente() != null ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO);
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && !pedido.getCliente().isFreteEmbutido()) {
			lvVlTotalFrete.setValue(pedido.vlTotalFretePedido);
		} else {
			lvVlTotalFrete.setValue(pedido.vlFrete);
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			bgGeraCreditoBonificacaoFrete.setValue(pedido.isGeraCreditoFrete() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
			lvGeraCreditoBonificacaoFrete.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoFrete));
			lvGeraCreditoBonificacaoFrete.setVisible(pedido.isGeraCreditoFrete());
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			edVlFreteAdicional.setValue(pedido.vlFreteAdicional);
		}
		//-- NFE
		if (LavenderePdaConfig.mostraAbaNfeNoPedido) {
			Nfe infoNfe = pedido.getInfoNfe();
			if (infoNfe.cdEmpresa != null) {
				lvNuNfe.setValue(infoNfe.nuNfe);
				if (LavenderePdaConfig.isUsaCampoTotalProdutosNotaImpressaoLayoutNfe()) {
					lvVlTotalProdutosNfe.setValue(infoNfe.vlTotalProdutosNfe);
				}
				lvVlTotalNfe.setValue(infoNfe.vlTotalNfe);
				lvDtSaida.setValue(infoNfe.dtSaida);
				lvHrSaida.setValue(infoNfe.hrSaida);
				lvDtEmissaoNfe.setValue(infoNfe.dtEmissao);
				lvCdStatusNfe.setValue(infoNfe.getStatusNfe());
				lvDsNaturezaOperacao.setValue(infoNfe.dsNaturezaOperacao);
				lvVlChaveAcesso.setValue(infoNfe.vlChaveAcesso);
				lvVlSerieNfe.setValue(infoNfe.dsSerieNfe);
				lvnuLote.setValue(infoNfe.nuLote);
				lvDsObservacao.setValue(infoNfe.dsObservacao);
				if (ValueUtil.isNotEmpty(infoNfe.dsNaturezaOperacao) || ValueUtil.isNotEmpty(infoNfe.vlChaveAcesso) || ValueUtil.isNotEmpty(infoNfe.dsObservacao)) {
					tipDsNaturezaOperacao.setText(MessageUtil.quebraLinhas(infoNfe.dsNaturezaOperacao));
					tipVlChaveAcesso.setText(MessageUtil.quebraLinhas(infoNfe.vlChaveAcesso));
					tipDsObservacao.setText(MessageUtil.quebraLinhas(infoNfe.dsObservacao));
				}
			}
		}
		if (LavenderePdaConfig.mostraAbaNfceNoPedido) {
			Nfce nfce = pedido.getNfce();
			lvQtTotalItem.setValue(StringUtil.getStringValueToInterface(nfce.qtTotalItem));
			lvVlTotalNfce.setValue(StringUtil.getStringValueToInterface(nfce.vlTotalNfce));
			lvVlTotalDesconto.setValue(StringUtil.getStringValueToInterface(nfce.vlTotalDesconto));
			lvVlTotalLiquidoNfce.setValue(StringUtil.getStringValueToInterface(nfce.vlTotalLiquidoNfce));
			lvDsFormaPagamento.setValue(MessageUtil.quebraLinhas(nfce.dsFormaPagamento));
			lvVlTotalPago.setValue(StringUtil.getStringValueToInterface(nfce.vlTotalPago));
			lvVlTroco.setValue(StringUtil.getStringValueToInterface(nfce.vlTroco));
			lvNuChaveAcesso.setValue(MessageUtil.quebraLinhas(nfce.nuChaveAcesso));
			lvNuNfce.setValue(nfce.nuNfce);
			lvNuSerie.setValue(nfce.nuSerie);
			lvDtEmissaoNfce.setValue(StringUtil.getStringValue(nfce.dtEmissao));
			lvHrEmissaoNfce.setValue(StringUtil.getStringValue(nfce.hrEmissao));
			lvNuProtocoloAutorizacao.setValue(nfce.nuProtocoloAutorizacao);
			lvDtAutorizacao.setValue(StringUtil.getStringValue(nfce.dtAutorizacao));
			lvHrAutorizacao.setValue(StringUtil.getStringValue(nfce.hrAutorizacao));
			lvVlTotalTributos.setValue(StringUtil.getStringValueToInterface(nfce.vlTotalTributos));
			lvVlPctTributosFederais.setValue(StringUtil.getStringValueToInterface(nfce.vlPctTributosFederais));
			lvVlPctTributosEstaduais.setValue(StringUtil.getStringValueToInterface(nfce.vlPctTributosEstaduais));
			lvVlPctTributosMunicipais.setValue(StringUtil.getStringValueToInterface(nfce.vlPctTributosMunicipais));
		}
		if (isShowComboTributacaoNovoCliente()) {
			cbTributacao.loadComboTributacao();
			if (pedido.isPedidoAberto() || pedido.isPedidoReaberto) {
			if (ValueUtil.isNotEmpty(pedido.cdTributacaoCliente)) {
				cbTributacao.setValue(pedido.cdTributacaoCliente, pedido.cdTipoPedido);
			} else {
				cbTributacao.setSelectedIndex(0);
				}
			cbTributacaoChange();
			} else {
				cbTributacao.setValue(pedido.cdTributacaoCliente, pedido.cdTipoPedido);
		}
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			cbCargaPedido.loadCargaPedido(pedido.getCliente().cdCliente, true);
			if (ValueUtil.isNotEmpty(pedido.cdCargaPedido)) {
				cbCargaPedido.setValue(pedido.cdCargaPedido);
			}
		}
		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido())  {
			STService.getInstance().calculaVlTotalPedidoComST(pedido);
			IpiService.getInstance().calculaVlTotalPedidoComIpi(pedido);
			TributosService.getInstance().calculaVlTotalPedidoComTributos(pedido);
		} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			STService.getInstance().calculaVlTotalPedidoComST(pedido);
		} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			IpiService.getInstance().calculaVlTotalPedidoComIpi(pedido);
		}
		//
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			TributosService.getInstance().calculaVlTotalPedidoComTributosEDeducoes(pedido);
		}
		//
		if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete()  || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			lbVlTotalPedidoMaisFrete.setValue(ValueUtil.round(pedido.vlTotalPedido + pedido.vlFrete));
		}
		if (LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete) {
		    lvVlBrutoPedidoMaisFrete.setValue(ValueUtil.round(TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido)));
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && pedido.getCliente().isFreteEmbutido()) {
			lvVlBrutoPedidoMaisFrete.setValue(PedidoService.getInstance().getVlTotalPedidoComFreteEmbutidoETributos(pedido));
		}

		//--
		updateVlTotalPedido();

		//--
		updateRealizadoMetaPorGrupoProd();
		//--
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && (lvVlTotalFrete.isChildOf(tabDinamica.getContainer(TABPANEL_PEDIDO)) ^ !pedido.getCliente().isFreteEmbutido())) {
			remontaTela();
			reposition();
		}
		lbDsNmEmpresa.setValue(EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa));
		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
			lbDsNmEmpresaCapa.setValue(EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa));
		}
		tipEmpresa.setText(MessageUtil.quebraLinhas(lbDsNmEmpresa.getText()));
		lbDsCliente.setValue(pedido.getCliente().toString());
		if (LavenderePdaConfig.usaApresentacaoFixaTicketMedioCapaPedido) {
			lvVlTicketMedio.setValue(pedido.getCliente().vlTicketMedio);
		}
		tipCliente.setText(MessageUtil.quebraLinhas(pedido.getCliente().toString()));
		if ((LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0)) {
			edVlPctDesconto.setValue(LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? pedido.vlPctDescCliente : pedido.vlDesconto);
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()) {
			lvVlDescontoCondPagto.setValue(pedido.vlTotalItens * (pedido.vlPctDesconto / 100));
		} else if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			lvVlDescontoCondPagto.setValue(PedidoService.getInstance().getVlDescontoPedidoDescontadoIncentivos(pedido));
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			edVlDesconto.setValue(pedido.vlDesconto);
			edPctDesconto.setValue(pedido.vlPctDesconto);
		}
		setLabelVlPctMaxDescontoPedido();
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			edVlPctDescEspecial.setValue(PedidoUiUtil.getPctDescEspecial(pedido));
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			edVlPctDescItem.setValue(pedido.vlPctDescItem);
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			edVlPctAcrescimoItem.setValue(pedido.vlPctAcrescimoItem);
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			if (isEditing()) ParcelaPedidoService.getInstance().tipoCondPagtoVencimento(pedido, pedido.getCondicaoPagamento());
			atualizaListParcelaPedidoForm(pedido);
		}
		//--
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
				lvVlVerbaPedido.setValue(VerbaService.getInstance().getVerbaPersonalizadaPedido(pedido, false));
				lvVlVerbaPedido.reposition();
			} else {
				lvVlVerbaPedido.setValue(pedido.vlVerbaPedido);
			}
			if (LavenderePdaConfig.isMostraFlexPositivoPedido(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
				edVlVerbaPedidoPositiva.setValue(pedido.vlVerbaPedidoPositivo);
			}
		}
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			lvVlVerbaPedido.setValue(pedido.vlVerbaPedido + pedido.vlVerbaPedidoPositivo);
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			lvVlPctIndiceFinCondPagto.setValue(pedido.vlPctIndiceFinCondPagto);
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			lvVlPctDescQuantidadePeso.setValue(pedido.vlPctDescQuantidadePeso);
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			lbVlBonificacaoPedido.setValue(pedido.vlBonificacaoPedido);
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			if ((cbAreaVenda.size() == 0) || !ValueUtil.valueEquals(ultimoClienteExibido, pedido.cdCliente)) { // Controle pra nao recarregar devido a perfomance
				cbAreaVenda.load(pedido.cdCliente);
				cbAreaVenda.setValue(pedido.cdAreaVenda);
			}
		}
		//--
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
			if (ValueUtil.isEmpty(pedido.getCliente().dtVencimentoAlvara)) {
				lvDsStatusAlvara.setForeColor(ColorUtil.softRed);
				lvDsStatusAlvara.setValue(Messages.PEDIDO_LABEL_STATUSALVARA_NAO);
			} else if (pedido.getCliente().isAlvaraVigente()) {
				lvDsStatusAlvara.setForeColor(ColorUtil.componentsForeColor);
				lvDsStatusAlvara.setValue(pedido.getCliente().dtVencimentoAlvara.toString());
			} else {
				lvDsStatusAlvara.setForeColor(ColorUtil.softRed);
				lvDsStatusAlvara.setValue(pedido.getCliente().dtVencimentoAlvara.toString());
			}
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
				lvVlEscalaRentabilidade.setValue(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadePedido(pedido));
			}
			lvVlPctRentabilidade.setValue(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.isOcultaRentabilidadePedido()) {
			lvVlPctRentabilidade.setValue(pedido.vlPctMargemRentab);
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvVlIndiceRentabPedido.setValue(pedido.vlRentabilidade);
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
			lvVlPctComissao.setValue(pedido.vlPctComissao);
		}
		//--
		if (LavenderePdaConfig.mostraSaldoContaCorrenteCliente) {
			double vlSaldoContaCorrente = ValueUtil.getDoubleValue(FichaFinanceiraService.getInstance().getColumnFichaFinanceira(pedido.getCliente(), "VLSALDOCONTACORRENTE"));
			edVlSaldoCCCliente.setValue(vlSaldoContaCorrente);
			if (vlSaldoContaCorrente == 0) {
				edVlSaldoCCCliente.setForeColor(ColorUtil.componentsForeColor);
			} else if (vlSaldoContaCorrente < 0) {
				edVlSaldoCCCliente.setForeColor(Color.RED);
			} else {
				edVlSaldoCCCliente.setForeColor(ColorUtil.softGreen);
			}
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			loadComboClienteSetorOrigem(pedido);
		}
		//--
		if (LavenderePdaConfig.bloqueiaTipoPagamentoNivelSuperior) {
			ultimoTipoPagamentoSelected = pedido.cdTipoPagamento;
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.isUsaTipoFretePedido() || LavenderePdaConfig.exibirInformacoesFreteCapaPedido()) {
			lbVlTotalFretePedido.setValue(pedido.vlFrete);
			edVlPctFreteRep.setValue(pedido.vlPctFreteRepresentante);
			edVlFreteRep.setValue(pedido.vlFreteRepresentante);
			edVlFreteCli.setValue(pedido.vlFreteCliente);
			lbVlTotalPedidoAbaFrete.setValue(pedido.vlTotalPedido);

			double vlTotalPedidoFrete = pedido.vlTotalPedido;
			if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos || LavenderePdaConfig.exibirInformacoesFreteCapaPedido()) {
				vlTotalPedidoFrete += pedido.vlFrete - pedido.vlFreteRepresentante;
			}
			lbVlTotalPedidoComFrete.setValue(vlTotalPedidoFrete);

			if (pedido.vlFrete != 0 && pedido.isPedidoAberto()) {
				edVlPctFreteRep.setEnabled(true);
			} else {
				edVlPctFreteRep.setEnabled(false);
			}
		}
		atualizaFrete(pedido, false);
		if (ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao)) {
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelBonificacao);
		} else if (ValueUtil.isNotEmpty(pedido.nuPedidoRelTroca)) {
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelTroca);
		} else if (ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
			edNuPedidoRelacionado.setText(pedido.nuPedidoComplementado);
		} else if (LavenderePdaConfig.usaConfigPedidoProducao()) {
			Vector pedidosRelacionados = PedidoRelacionadoService.getInstance().getPedidosRelacionadosByPedido(pedido);
			if (!pedidosRelacionados.isEmpty()) {
				edNuPedidoRelacionado.setText(getNuPedidoPedidosRelacionados(pedidosRelacionados));
			}
		} else {
			edNuPedidoRelacionado.setText("");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()){
			ckVinculaCampanhaPublicitaria.setChecked(ValueUtil.getBooleanValue(pedido.flVinculaCampanhaPublicitaria));
			if (ValueUtil.isNotEmpty(pedido.cdCampanhaPublicitaria)) {
				String[] campanhas = pedido.cdCampanhaPublicitaria.split(";");
				String dsCampanhas = ValueUtil.VALOR_NI;
				for (String cdCampanha : campanhas) {
					String dsCampanhaPublicitaria = CampanhaPublicitariaService.getInstance().findDsCampanhaPublicitariaByPedido(pedido, cdCampanha);
					if (dsCampanhaPublicitaria == null) continue;
					dsCampanhas += ValueUtil.isEmpty(dsCampanhas) ? dsCampanhaPublicitaria : ";" + dsCampanhaPublicitaria;
				}
				edNuCampanhaPublicitaria.setText(dsCampanhas);
			} else {
				edNuCampanhaPublicitaria.setText(ValueUtil.VALOR_NI);
			}
		}
		//-- INFORMAÇÕES ADICIONAIS
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			cbCentroCusto.carregaCentroCusto();
			cbCentroCusto.setValue(pedido.cdCentroCusto);
			pedido.centroCusto = (CentroCusto) cbCentroCusto.getSelectedItem();
			if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
				cbPlataformaVenda.carregaPlataformas(pedido.cdCentroCusto, pedido.cdCliente, pedido.cdRepresentante);
				cbPlataformaVenda.setValue(pedido.cdPlataformaVenda);
			}
		}
		if (LavenderePdaConfig.isUsaItemContaInformacoesAdicionais()) {
			cbItemConta.carregaItemConta();
			cbItemConta.setValue(pedido.cdItemConta);
		}
		if (LavenderePdaConfig.isUsaClasseValorInformacoesAdicionais()) {
			cbClasseValor.carregaClasseValor();
			cbClasseValor.setValue(pedido.cdClasseValor);
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			cbModoFaturamento.carregaModoFaturamento();
			cbModoFaturamento.setValue(pedido.cdModoFaturamento);
			emObservacaoModoFaturamento.setValue(pedido.dsObsModoFaturamento);
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			edNuKmInicial.setValue(pedido.nuKmInicial);
			edNuKmFinal.setValue(pedido.nuKmFinal);
			if (ValueUtil.isNotEmpty(pedido.hrInicialIndicado)) {
				edHrInicialIndicado.setText(pedido.hrInicialIndicado);
			}
			if (ValueUtil.isNotEmpty(pedido.hrInicialIndicado)) {
				edHrFinalIndicado.setText(pedido.hrFinalIndicado);
			}
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			bgTaxaEntrega.setValue(pedido.getInfoFretePedido().flTaxaEntrega);
			edVlTaxaEntrega.setValue(pedido.getInfoFretePedido().vlTaxaEntrega);
			bgAjudante.setValue(pedido.getInfoFretePedido().flAjudante);
			edQtAjudante.setValue(pedido.getInfoFretePedido().qtAjudante);
			bgAntecipaEntrega.setValue(pedido.getInfoFretePedido().flAntecipaEntrega);
			bgAgendamento.setValue(pedido.getInfoFretePedido().flAgendamento);
			cbTipoVeiculo.setValue(pedido.getInfoFretePedido().cdTipoVeiculo);
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			cbEnderecoEntrega.cleanDomainSelected();
			cbEnderecoEntrega.cleanListWindow();
			cbEnderecoEntrega.populatePedido(pedido);
			if (ValueUtil.isNotEmpty(pedido.cdEnderecoCliente)) {
				if (PedidoService.getInstance().obrigaIndicarClienteEntrega(pedido) && ValueUtil.isNotEmpty(pedido.cdClienteEntrega)) {
					cbEnderecoEntrega.setValue(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdClienteEntrega, pedido.cdEnderecoCliente);
					cbEnderecoEntrega.cdClienteEntrega = pedido.cdClienteEntrega;
				} else {
					if (pedido!= null && pedido.isPedidoNovoCliente() && LavenderePdaConfig.isApresentaEnderecoNovoCliente()) {
						cbEnderecoEntrega.setValue(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente, pedido);
					}
					else {
					cbEnderecoEntrega.setValue(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente);
				}
				}
				if (pedido.isReplicandoPedido && cbEnderecoEntrega.getValue() == null) {
					cbEnderecoEntrega.carregaClienteEnderecoPadrao(pedido, false);
				}
			}
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			cbEnderecoCobranca.cleanDomainSelected();
			cbEnderecoCobranca.cleanListWindow();
			cbEnderecoCobranca.populatePedido(pedido);
			if (ValueUtil.isNotEmpty(pedido.cdEnderecoCobranca)) {
				cbEnderecoCobranca.setValue(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCobranca);
				if (pedido.isReplicandoPedido && cbEnderecoCobranca.getValue() == null) {
					cbEnderecoCobranca.carregaClienteEnderecoPadrao(pedido, true);
				}
			}
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			lvUsuarioItemLiberacao.setValue(pedido.getItemLiberacao().nmUsuario);
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoApenasNumeros()) {
			edNuOrdemCompraCliente1.setText(pedido.nuOrdemCompraCliente);
		} else if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos()) {
			edNuOrdemCompraCliente2.setText(pedido.nuOrdemCompraCliente);
		}
		setTotalizadoresDoPedido(pedido);
		carregaBoletos();
		carregaAtividadePedido();
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			if (cbContatoErp.count() <= 0) {
				cbContatoErp.load(pedido.cdCliente);
			}
			cbContatoErp.setValue(pedido.cdContato);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			PagamentoPedido pagamentoPedido = pedido.getPagamentoPedido();
			cbBoletoConfig.setValue(pagamentoPedido.nuBanco);
			edNuAgencia.setText(pagamentoPedido.nuAgencia);
			edNuConta.setText(pagamentoPedido.nuConta);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			cbStatusOrcamento.load(pedido);
			cbStatusOrcamento.setValue(pedido.cdStatusOrcamento);
			emObservacaoOrcamento.setValue(pedido.dsObsOrcamento);
			pedido.statusOrcamento = (StatusOrcamento) cbStatusOrcamento.getSelectedItem();
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem() && pedido.dtPagamento != null) {
			edDtPagamento.setValue(pedido.dtPagamento);
			if (updateValuesPedidoByDtPagamento) {
				reloadAndUpdateValoresDoPedido(pedido, false);
			}
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			edDtEntregaManual.setValue(pedido.dtEntrega);
		}
		bgAguardarPedidoComplementar.setValue(pedido.flAguardaPedidoComplementar);
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			bgPedidoCritico.setValueBoolean(pedido.isPedidoCritico() && !pedido.isPedidoBonificacao());
		}
		if (edVlPctVpc != null) {
			edVlPctVpc.setValue(getPedido().vlPctVpc);
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			ComissaoPedidoRepService.getInstance().applyComissaoPedidoRepInPedido(getPedido());
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			edNuParcelaPedido.setValue(pedido.nuParcelas);
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			PontuacaoConfigService.getInstance().reloadPontuacaoPedido(getPedido(), null);
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			bgPedidoGondola.setValueBoolean(pedido.isGondola() && !pedido.isPedidoBonificacao());
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			PesoFaixaService.getInstance().applyPesoFaixaInPedido(pedido);
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			bgUsaCodigoInternoCliente.setValue(pedido.flCodigoInternoCliente);
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			bgUtilizaRentabilidade.setValue(pedido.flUtilizaRentabilidade);
		}
		if (LavenderePdaConfig.usaConfigPedidoProducao() && pedido.isPedidoVendaProducao()) {
			if (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao()) {
				pedido.pedidoRelacionadoList = PedidoRelacionadoService.getInstance().getPedidosRelacionadosByPedido(pedido);
			} else {
				pedido.pedidoRelacionado = PedidoRelacionadoService.getInstance().findUnicoPedidoRelacionadoByPedido(pedido);
			}
		}
	}

	private void atualizaListParcelaPedidoForm(Pedido pedido) throws SQLException {
		listParcelaPedidoForm.removeAll();
		listParcelaPedidoForm.setPedido(pedido);
		listParcelaPedidoForm.onFormStart();
		listParcelaPedidoForm.list();
	}

	private Vector getTipoPagamentoSelected(Pedido pedido) throws SQLException {
		TipoPagamento filter = new TipoPagamento();
		filter.cdEmpresa = pedido.cdEmpresa;
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			filter.cdRepresentante = pedido.cdRepresentante;
		}
		filter.cdTipoPagamento = pedido.cdTipoPagamento;
		Vector tipoPagamentoPedido = TipoPagamentoService.getInstance().findAllByExample(filter);
		return tipoPagamentoPedido;
	}

	private void carregaPctDescontoVolumeVendas(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		double vlVendasMensalRede;
		if (LavenderePdaConfig.usaVolumeVendaMensalRede && clientePossuiRede(cliente)) {
			vlVendasMensalRede = ClienteService.getInstance().sumVlVendaMensalClientesRede(cliente);
			vlVendasMensalRede = vlVendasMensalRede + pedido.vlTotalPedido;
			vlVendasMensalRede += PedidoPdbxDao.getInstance().sumVlTotalPedidosConsideraVolumeVendaMensalPorStatus(pedido);
			DescontoVenda faixaCliente = DescontoVendaService.getInstance().getDescontoVenda(cliente.cdEmpresa, cliente.cdEstadoComercial, vlVendasMensalRede);
			edVlPctDescontoVendaMensal.setValue((faixaCliente != null) ? faixaCliente.vlPctDesconto : 0d);
		} else {
			edVlPctDescontoVendaMensal.setValue(DescontoVendaService.getInstance().getVlPctDescontoVenda(pedido.cdEmpresa, cliente.cdEstadoComercial, pedido.vlTotalPedido + cliente.vlVendaMensal));
		}
	}

	private boolean isUsaComissao() {
		return LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo();
	}

	private void setEdValorMinCondicaoPagamento(double qtMinValor) {
		edValorMinCondPagto.setValue(qtMinValor);
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
			tipVlMinCondPagto.setText(qtMinValor > 0 ? Messages.VALOR_MINIMO_PEDIDO + " " + StringUtil.getStringValueToInterface(qtMinValor) : Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
	}

	private void setEdPrazoMedio(int prazoMedio) {
		edPrazoMedio.setValue(prazoMedio);
		if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
			tipPrazoMedio.setText(prazoMedio > 0 ? Messages.PEDIDO_PRAZO_MEDIO + " " + StringUtil.getStringValueToInterface(prazoMedio) : Messages.CONDICAOPAGAMENTO_SEM_PRAZO_MED);
		}
	}

	private void setEdNuParcelaPedido(int nuParcelas, boolean permiteEdicao) {
		if (!LavenderePdaConfig.isNuParcelasNoPedido()) return;

		edNuParcelaPedido.setValue(nuParcelas);
		edNuParcelaPedido.setEnabled(permiteEdicao && isEnabled());
		tipNuParcelaPedido.setText(nuParcelas > 0 ? Messages.PEDIDO_NUMERO_PARCELA + " " + StringUtil.getStringValueToInterface(nuParcelas) : Messages.CONDICAOPAGAMENTO_SEM_PARCELA);
	}

	private void setTotalizadoresDoPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoCapaPedido()) {
			totalizadoresPedidoForm.setPedido(pedido);
			totalizadoresPedidoForm.domainToScreen();
		}

		if (LavenderePdaConfig.mostraVlBrutoCapaPedido) {
			double vlTotalPedidoComTributosEDeducoesComFrete = TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido);
			lvVlBrutoCapaPedido.setValue(StringUtil.getStringValueToInterface(vlTotalPedidoComTributosEDeducoesComFrete));
		}

		if (LavenderePdaConfig.mostraValorBruto || LavenderePdaConfig.mostraValorDesconto) {
			double valorBaseTodosItens = getValorBaseTodosItens(pedido);
			if (LavenderePdaConfig.mostraValorBruto) {
				lvVlBruto.setValue(StringUtil.getStringValueToInterface(valorBaseTodosItens));
			}
			if (LavenderePdaConfig.mostraValorDesconto) {
				double vlDesconto = valorBaseTodosItens - pedido.vlTotalItens;
				lvVlDesconto.setValue(StringUtil.getStringValueToInterface(vlDesconto < 0 ? 0d : vlDesconto));
			}
		}
	}

	private double getValorBaseTodosItens(final Pedido pedido) throws SQLException {
		int sizeItensPedido = pedido.itemPedidoList.size();
		double vlTotalItensPedido = 0d;
		for (int i = 0; i < sizeItensPedido; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || itemPedido.vlPctAcrescimo > 0) {
				double vlItemPedido = LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && pedido.getCondicaoPagamento().vlIndiceFinanceiro != 0 ? itemPedido.vlItemPedido / pedido.getCondicaoPagamento().vlIndiceFinanceiro : itemPedido.vlItemPedido;
				vlTotalItensPedido += vlItemPedido * itemPedido.getQtItemFisico();
			} else {
				vlTotalItensPedido += itemPedido.getVlTotalItemBruto();
			}
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				vlTotalItensPedido += itemPedido.getVlTotalST() + itemPedido.getVlTotalIpi() + itemPedido.getVlTotalSeguro() + itemPedido.getVlTotalFrete();
			}
		}

		return vlTotalItensPedido;
	}

	private void carregaAtividadePedido() throws SQLException {
		if (LavenderePdaConfig.usaRegistroAtividadeRelacionadaPedido) {
			listGridAtividadePedido.removeAllContainers();
	    	Vector atividadePedidoList = getPedido().getAtividadePedidoList();
	    	if (ValueUtil.isNotEmpty(atividadePedidoList)) {
	    		Container[] all = new Container[atividadePedidoList.size()];
	    		BaseListContainer.Item c;
	    		for (int i = 0; i < atividadePedidoList.size(); i++) {
	    			AtividadePedido atividadePedido = (AtividadePedido) atividadePedidoList.elementAt(i);
	    			all[i] = c = new BaseListContainer.Item(listGridAtividadePedido.getLayout());
	    			c.id = atividadePedido.getRowKey();
	 		    	c.setItens(getItemGridAtividadePedido(atividadePedido));
				}
	    		listGridAtividadePedido.addContainers(all);
		}
	}
	}

	private String[] getItemGridAtividadePedido(AtividadePedido atividadePedido) {
		String[] item = { atividadePedido.statusAtividade.toString(), "",
				StringUtil.getStringValue(atividadePedido.dtAlteracao) + " - " + StringUtil.getStringValue(atividadePedido.hrAlteracao),
				StringUtil.getStringValue(atividadePedido.cdUsuarioCriacao) + " - " + StringUtil.getStringValue(atividadePedido.nmUsuarioCriacao) };
        return item;
	}

	private void carregaDetalhesAtividadePedido() throws SQLException {
		AtividadePedido atividadePedido = (AtividadePedido) AtividadePedidoService.getInstance().findByRowKeyDyn(listGridAtividadePedido.getSelectedId());
		CadAtividadePedidoWindow cadAtividadePedidoWindow = new CadAtividadePedidoWindow(atividadePedido);
		cadAtividadePedidoWindow.popup();
	}

	private void carregaBoletos() throws SQLException {
		if (LavenderePdaConfig.mostraAbaBoletoNoPedido) {
			listGridBoleto.removeAllContainers();
	    	Vector pedidoBoletoList = getPedido().getPedidoBoletoList();
	    	if (ValueUtil.isNotEmpty(pedidoBoletoList)) {
	    		Container[] all = new Container[pedidoBoletoList.size()];
	    		BaseListContainer.Item c;
	    		for (int i = 0; i < pedidoBoletoList.size(); i++) {
	    			PedidoBoleto pedidoBoleto = (PedidoBoleto) pedidoBoletoList.elementAt(i);
	    			all[i] = c = new BaseListContainer.Item(listGridBoleto.getLayout());
	    			c.id = pedidoBoleto.getRowKey();
	 		    	c.setItens(getItemGridBoleto(pedidoBoleto));
				}
	    		listGridBoleto.addContainers(all);
		}
    }
    }

	private String[] getItemGridBoleto(PedidoBoleto pedidoBoleto) {
		String[] item = {
				StringUtil.getStringValue(pedidoBoleto.nuDocumento),
				StringUtil.getStringValueToInterface(pedidoBoleto.vlBoleto),
				StringUtil.getStringValue(pedidoBoleto.dtVencimento),
				StringUtil.getStringValue(pedidoBoleto.nuSequenciaBoletoPedido)};
        return item;
	}

	private void carregaDetalhesPedidoBoleto() throws SQLException {
		PedidoBoleto pedidoBoleto = (PedidoBoleto) PedidoBoletoService.getInstance().findByRowKey(listGridBoleto.getSelectedId());
		if (pedidoBoleto != null) {
			CadPedidoBoletoWindow cadPedidoBoletoWindow = new CadPedidoBoletoWindow(pedidoBoleto);
			cadPedidoBoletoWindow.popup();
		}
	}

	private void carregaDetalhesNotaFiscal() throws SQLException {
		NotaFiscal notaFiscal = (NotaFiscal) NotaFiscalService.getInstance().findByRowKeyDyn(listNotaFiscalForm.getListContainer().getSelectedId());
		CadNotaFiscalDynWindow cadNotaFiscalDynWindow = new CadNotaFiscalDynWindow(notaFiscal);
		cadNotaFiscalDynWindow.popup();
	}

	private void setValueEdValorMinTipoPedido(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		double qtMinValor = tipoPedido != null ? tipoPedido.qtMinValor : 0;
		if (qtMinValor > 0) {
			edValorMinTipoPedido.setValue(qtMinValor);
			tipVlMinTipoPedido.setText(Messages.VALOR_MINIMO_PEDIDO + " " + qtMinValor);
		} else {
			edValorMinTipoPedido.setText("");
			tipVlMinTipoPedido.setText(Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
		}
	}

	private void setValueEdVlMinTabelaPreco(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaTabelaPrecoPedido() || (!LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco() || LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem())) return;
		TabelaPreco tabelaPreco = pedido.getTabelaPreco();
		double qtMinValor = pedido.isPedidoVenda() && tabelaPreco != null ? tabelaPreco.qtMinValor : 0;
		if (qtMinValor > 0) {
			edVlMinTabelaPreco.setValue(qtMinValor);
			tipVlMinTabelaPreco.setText(Messages.VALOR_MINIMO_PEDIDO + " " + qtMinValor);
		} else {
			edVlMinTabelaPreco.setText("");
			if (LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
				tipVlMinTabelaPreco.setText(Messages.TABELAPRECO_SEM_VALOR_MIN);
			}
		}
	}

	private void loadComboClienteSetorOrigem(Pedido pedido) throws SQLException {
		String cdContrato = getPedido().getCliente().cdContratoEspecial;
		if (!ValueUtil.isEmpty(cdContrato)) {
			loadComboClienteSetorOrigemByPedido(pedido);
		} else {
			cbClienteSetor.loadClienteSemContrato();
			cbClienteSetorOrigem.loadClienteSemContrato();
		}
	}

	private void updateRealizadoMetaPorGrupoProd() throws SQLException {
		if (LavenderePdaConfig.usaPesoGerarRealizado && LavenderePdaConfig.usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd) {
			ItemPedidoService.getInstance().updateRealizadoMetaPorGrupoProd(getPedido());
		}

	}

	private void loadComboClienteSetor(String cdSetorOrigem) throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.getCliente().isClienteContratoEspecial()) {
			cbClienteSetor.load(ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO, SessionLavenderePda.getCliente().cdContratoEspecial, cdSetorOrigem);
			cbClienteSetor.setValue(pedido.cdOrigemSetor, pedido.getCliente().cdContratoEspecial, ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO, pedido.cdSetor);
		} else {
			cbClienteSetor.load(ClienteSetorOrigem.CLIENTE_SETOR_REDE, SessionLavenderePda.getCliente().cdContratoEspecial, cdSetorOrigem);
			cbClienteSetor.setValue(pedido.cdOrigemSetor, pedido.getCliente().cdContratoEspecial, ClienteSetorOrigem.CLIENTE_SETOR_REDE, pedido.cdSetor);
		}
		pedido.cdSetor = cbClienteSetor.getValue();
	}

	private void loadComboClienteSetorOrigemByPedido(Pedido pedido) throws SQLException {
		ClienteSetorOrigem clienteSetorOrigem = new ClienteSetorOrigem();
		clienteSetorOrigem.cdEmpresa = pedido.cdEmpresa;
		clienteSetorOrigem.cdRepresentante = pedido.cdRepresentante;
		clienteSetorOrigem.cdSetor = pedido.cdSetor;
		clienteSetorOrigem.cdOrigemSetor = pedido.cdOrigemSetor;
		String cdContrato = pedido.getCliente().cdContratoEspecial;
		String cdTipoCliRede;
		if (getPedido().getCliente().isClienteContratoEspecial()) {
			clienteSetorOrigem.cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO;
			clienteSetorOrigem.cdCliRede = cdContrato;
			cbClienteSetorOrigem.load(ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO, cdContrato);
			cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO;
		} else {
			clienteSetorOrigem.cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_REDE;
			clienteSetorOrigem.cdCliRede = cdContrato;
			cbClienteSetorOrigem.load(ClienteSetorOrigem.CLIENTE_SETOR_REDE, cdContrato);
			cdTipoCliRede = ClienteSetorOrigem.CLIENTE_SETOR_REDE;
		}
		cbClienteSetorOrigem.select(clienteSetorOrigem);
		if (cbClienteSetorOrigem.defaultItemType == BaseComboBox.DefaultItemType_NONE_ITEN) {
			cbClienteSetorOrigem.setSelectedIndex(0);
		}
		if (cbClienteSetor.defaultItemType == BaseComboBox.DefaultItemType_NONE_ITEN) {
			cbClienteSetor.setSelectedIndex(0);
		}
		cbClienteSetorOrigem.setValue(pedido.cdOrigemSetor, cdContrato, cdTipoCliRede);
		cbClienteSetorOrigemClick();
	}

	private void setColorsOnIndicesRentabilidadeSemTributos() {
		if (lvVlIndiceRentabPedido.getDoubleValue() == 0) {
			lvVlIndiceRentabPedido.setForeColor(ColorUtil.componentsForeColor);
		} else if (lvVlIndiceRentabPedido.getDoubleValue() < LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			lvVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK));
		} else if (lvVlIndiceRentabPedido.getDoubleValue() <= LavenderePdaConfig.indiceMinimoRentabilidadePedido && lvVlIndiceRentabPedido.getDoubleValue() >= LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			lvVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA));
		} else {
			lvVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK));
		}
	}


	private void setColorsOnIndicesRentabilidade() {
		if (lvVlIndiceRentabPedido.getDoubleValue() == 0) {
			lvVlIndiceRentabPedido.setForeColor(ColorUtil.componentsForeColor);
		} else if (lvVlIndiceRentabPedido.getDoubleValue() < LavenderePdaConfig.indiceMinimoRentabilidadePedido) {
			lvVlIndiceRentabPedido.setForeColor(Color.darker(Color.RED));
		} else {
			lvVlIndiceRentabPedido.setForeColor(Color.darker(Color.GREEN));
		}
	}

	public void updateVlTotalPedido() throws SQLException {
		Pedido pedido = getPedido();
		lbVlTotalPedido.setValue(pedido.vlTotalPedido);
		atualizaLabelCasoValorMenorZero(lbVlTotalPedido, pedido.vlTotalPedido);
		if (LavenderePdaConfig.isMostraValorMinimoCapaPedido()) {
			lbVlMinPromocional.setValue(pedido.getVlTotalPromocional() * LavenderePdaConfig.getVlPercentualValorMinimoPromocional());
		}
		lbVlTotalPedidoTroca.setValue(pedido.vlTotalTrocaPedido);
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			TributosService.getInstance().calculaVlTotalPedidoComTributosEDeducoes(pedido);
			lvVlPedido.setValue(pedido.vlFinalPedidoDescTribFrete);
			atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlFinalPedidoDescTribFrete);
		} else {
			lvVlPedido.setValue(pedido.vlTotalPedido);
			atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlTotalPedido);
		}
		atualizaVlPedidoAberto();
		lbVlTotalItens.setValue(pedido.vlTotalItens);
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			lvVlTotalPedidoComTributosEDeducoes.setValue(pedido.vlFinalPedidoDescTribFrete);
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()) {
				lvVlTtPedidoTributosEDeducoesEDesc.setValue(pedido.vlFinalPedidoDescTribFrete + (pedido.vlPctDesconto / 100 * pedido.vlTotalItens));
			} else if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
				lvVlTtPedidoTributosEDeducoesEDesc.setValue(pedido.vlFinalPedidoDescTribFrete);
			}
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			lvVlTotalPedidoComImpostos.setValue(PedidoService.getInstance().getVlTotalItensPedidoDescontadoIncentivos(pedido));
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			lvVlTotalFreteItensPedido.setValue(PedidoService.getInstance().getVlTotalFreteItensPedido(pedido));
		}
		if (pedido.isPedidoTroca()) {
			lbVlTotalPedido.setValue(pedido.vlTrocaRecolher);
			atualizaLabelCasoValorMenorZero(lbVlTotalPedido, pedido.vlTrocaRecolher);
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			lbVlEfetivo.setValue(pedido.vlTotalPedidoEstoquePositivo);
		}
		//--
		aplicaIndiceFinanceiroClientePorPedido(pedido);
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			edVlPctDesconto.setValue(pedido.vlPctDesconto);
			lvVlDescontoCondPagto.setValue(pedido.vlTotalItens * (pedido.vlPctDesconto / 100));
		}
		lvVlDescontoCondPagto.setValue(PedidoService.getInstance().getVlDescontoPedidoDescontadoIncentivos(pedido));
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			edVlPctDesconto.setValue(pedido.vlPctDescCliente);
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			edVlDesconto.setValue(pedido.vlDesconto);
			edPctDesconto.setValue(pedido.vlPctDesconto);
		}
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			edVlPctDescEspecial.setValue(PedidoUiUtil.getPctDescEspecial(pedido));
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
			edVlPctDescFrete.setValue(pedido.vlPctDescFrete);
			String descontoMax = StringUtil.getStringValueToInterface(pedido.getTipoFrete() != null ? pedido.getTipoFrete().vlPctMaxDesconto : 0d);
			lbVlMaxPctDescFrete.setValue(descontoMax);
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			edVlPctDescCondicao.setValue(pedido.vlPctDescontoCondicao);
			String descontoMax = StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento() != null ? pedido.getCondicaoPagamento().vlPctDescontoTotalPedido : 0d);
			lbVlMaxPctDescCondicao.setValue(descontoMax);
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			edVlPctDescCondicao.setValue(pedido.vlPctDescontoCondicao);
			edVlPctDescHistoricoVendas.setValue(pedido.vlPctDescHistoricoVendas);
		}
		if (LavenderePdaConfig.exibeDescontoAcrescimoIndice()) {
			lbVlPctDescCondicaoPagamento.setValue(CondicaoPagamentoService.getInstance().loadVlPctDescAcresCondPagto(pedido));
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()) {
			lvVlTotalMargem.setValue(pedido.vlTotalMargem);
			lvVlPctTotalMargem.setValue(pedido.vlPctTotalMargem);
		}
		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			lvVlTotalPedidoComTributos.setValue(pedido.vlTtPedidoComTributos);
		} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			lvVlTotalPedidoComTributos.setValue(pedido.vlTtPedidoComSt);
		} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			lvVlTotalPedidoComTributos.setValue(pedido.vlTtPedidoComIpi);
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.utilizaNotasCredito()) {
			lvVlTotalBrutoItens.setValue(pedido.vlTotalBrutoItens);
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			String pesoPedido = StringUtil.getStringValueToInterface(pedido.qtPeso) + " " + Messages.ITEMPEDIDO_LABEL_PESO;
			if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
				pesoPedido = MessageUtil.getMessage(Messages.PEDIDO_PESO_MEDIO, StringUtil.getStringValueToInterface(pedido.qtPeso));
			}
			lvPesoPedido.setValue(pesoPedido);
			if (LavenderePdaConfig.usaFretePedidoPorToneladaCliente) {
				lvVlTotalFrete.setValue(pedido.vlFrete);
			}
		}
		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			if (LavenderePdaConfig.isUsaQtdInteiro()) {
				lbQtItensFaturados.setText(StringUtil.getStringValueToInterface((int) pedido.getQtItensFaturamento()));
			} else {
				lbQtItensFaturados.setText(StringUtil.getStringValueToInterface(pedido.getQtItensFaturamento()));
			}
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			lvVlTotalFrete.setValue(pedido.vlFrete);
			lbVlTotalFretePedido.setValue(pedido.vlFrete);
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && !pedido.getCliente().isFreteEmbutido()) {
			lvVlTotalFrete.setValue(pedido.vlTotalFretePedido);
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			lbVlBonificacaoPedido.setValue(pedido.vlBonificacaoPedido);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !pedido.isPedidoBonificacao()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
				atualizaIconeRentabilidade(btIconeRentabilidade);
			}
			if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
				lvVlEscalaRentabilidade.setValue(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadePedido(pedido));
			}
			lvVlPctRentabilidade.setValue(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			if (!LavenderePdaConfig.isOcultaFaixaRentabilidadeCapaPedido()) {
				atualizaIconeRentabilidade(btIconeRentabilidade);
			}
			if (!LavenderePdaConfig.isOcultaRentabilidadePedido()) {
				lvVlPctRentabilidade.setValue(pedido.vlPctMargemRentab);
			}
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
			lvVlPctComissao.setValue(pedido.vlPctComissao);
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvVlIndiceRentabPedido.setValue(pedido.vlRentabilidade);
			if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
				setColorsOnIndicesRentabilidade();
			} else {
				setColorsOnIndicesRentabilidadeSemTributos();
			}
		}
		if (LavenderePdaConfig.usaTransportadoraPedido() && (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.usaRateioFreteRepresentanteCliente) || LavenderePdaConfig.isUsaTipoFretePedido()) {
			lbVlTotalFretePedido.setValue(pedido.vlFrete);
			edVlPctFreteRep.setValue(pedido.vlPctFreteRepresentante);
			edVlFreteRep.setValue(pedido.vlFreteRepresentante);
			edVlFreteCli.setValue(pedido.vlFreteCliente);
			lbVlTotalPedidoAbaFrete.setValue(pedido.vlTotalPedido);

			double vlTotalPedidoFrete = pedido.vlTotalPedido;
			if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
				vlTotalPedidoFrete += pedido.vlFrete - pedido.vlFreteRepresentante;
			}
			lbVlTotalPedidoComFrete.setValue(vlTotalPedidoFrete);
			if (ValueUtil.isNotEmpty(lbVlTotalFretePedido.getValue()) && pedido.isPedidoAberto()) {
				edVlPctFreteRep.setEnabled(true);
			} else {
				edVlPctFreteRep.setEnabled(false);
			}
		}
		if (LavenderePdaConfig.mostraPercDescMaxPedido && LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			lvPercMaxDesconto.setValue(pedido.vlTotalItens * (LavenderePdaConfig.aplicaDescontoNaCapaDoPedido / 100));
		}
		Cliente cliente = pedido.getCliente();
		if (LavenderePdaConfig.mostraPercDescMaxPedido && LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
			double vlMaxDesc = pedido.vlTotalItens * ((LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? cliente.vlPctMaxDesconto : LavenderePdaConfig.permiteDescontoEmValorPorPedido) / 100);
			lvPercMaxDesconto.setValue(vlMaxDesc);
		}
		if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !pedido.isPedidoBonificacao()) {
			lvPercDescLibRestante.setValue(PedidoDescService.getInstance().getVlPctDescontoLiberacaoRestante(pedido));
			lvVlTotalLiberado.setValue(PedidoDescService.getInstance().getVlTotalPedidoLiberadoAtual(pedido));
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			lvVlTotalVolumePedido.setText(StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlVolumePedido, LavenderePdaConfig.nuCasasDecimaisVlVolume), LavenderePdaConfig.nuCasasDecimaisVlVolume));
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			lvVlTotalNotaCredito.setValue(ValueUtil.round(pedido.vlTotalNotaCredito));
		}
		if (LavenderePdaConfig.usaApresentacaoValorStCapaPedido && (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido)) {
			lbVlTotalSTPedido.setValue(STService.getInstance().getVlTotalStPedido(pedido));
		}
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			edDescontoCascataManualDescCliente.setValue(pedido.vlPctDescCliente);
			edDescontoCascataManual2.setValue(pedido.vlPctDescontoCondicao);
			edDescontoCascataManual3.setValue(pedido.vlPctDescFrete);
		}
 		if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			lbVlTotalPedidoMaisFrete.setValue(ValueUtil.round(pedido.vlTotalPedido + pedido.vlFrete));
		}
		if (LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete) {
			lvVlBrutoPedidoMaisFrete.setValue(TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido));
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && pedido.getCliente().isFreteEmbutido()) {
			lvVlBrutoPedidoMaisFrete.setValue(PedidoService.getInstance().getVlTotalPedidoComFreteEmbutidoETributos(pedido));
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			carregaPctDescontoVolumeVendas(pedido);
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			edDescCascataCategoria1.setValue(pedido.vlPctDescCliente);
			edDescCascataCategoria2.setValue(pedido.vlPctDesc2);
			edDescCascataCategoria3.setValue(pedido.vlPctDesc3);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && LavenderePdaConfig.isUsaVerba()) {
			lvVlVerbaPedido.setValue(pedido.vlVerbaPedido);
		}
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			lvVlVerbaPedido.setValue(pedido.vlVerbaPedido + pedido.vlVerbaPedidoPositivo);
		}
		if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			lvVlTotalPedidoComissao.setValue(pedido.vlComissaoPedido);
		}
		setTotalizadoresDoPedido(pedido);
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			atualizaIconeComissaoPedido(btIconeComissaoPedido);
		}
		if (LavenderePdaConfig.mostraDescAcessoriaCapaPedido && pedido.itemPedidoList != null) {
			lvVlDespesaAcessoria.setValue(PedidoService.getInstance().getVlTotalDespesaAcessoria(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			lvVlSeguroPedido.setValue(pedido.vlSeguroPedido);
		}
		if (LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()) {
			TabelaPreco tabelaPreco = pedido.getTabelaPreco();
			double qtPesoMin = pedido.isPedidoVenda() && tabelaPreco != null ? tabelaPreco.qtPesoMin : 0;
			lvVlPesoMinimoTabPreco.setValue(qtPesoMin);
			if (pedido.qtPeso > 0) {
				lvVlTotalPedidoPorPeso.setValue(pedido.vlTotalPedido / pedido.qtPeso);
			} else {
				lvVlTotalPedidoPorPeso.setValue(StringUtil.getStringValueToInterface(0d));
			}
		}
		if (isUsaComissao()) {
			PedidoService.getInstance().recalculateComissaoPedido(pedido);
			if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				lvVlTotalPedidoComissao.setValue(pedido.vlComissaoPedido);
			}
		}
		if (LavenderePdaConfig.mostraValorParcelaPedido) {
			lvVlValorParcelaPedido.setValue(PedidoService.getInstance().getValorParcela(pedido));
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			bgPedidoGondola.setEnabled(isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			bgPedidoCritico.setEnabled(isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			boolean cbTipoFreteEnabled = isEnabled() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList);
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				boolean hasSolAutorizacaoPendenteOuAutorizadaByPedido = pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, null);
				cbTipoFreteEnabled &= !hasSolAutorizacaoPendenteOuAutorizadaByPedido;
		}
			cbTipoFrete.setEditable(cbTipoFreteEnabled);
		} else if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			if (isEnabled()) {
				boolean hasSolAutorizacaoPendenteOuAutorizadaByPedido = pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, null);
				cbTipoFrete.setEditable(!hasSolAutorizacaoPendenteOuAutorizadaByPedido);
			} else {
				cbTipoFrete.setEditable(false);
			}
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			atualizaIconePesoFaixa(btIconeFaixa);
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete()) {
			edVlManualFrete.setValue(pedido.vlFrete);
		}
		setPontuacaoValues(pedido);
		repositionTabDinamica();
	}

	private void setPontuacaoValues(Pedido pedido) {
		if (!LavenderePdaConfig.usaControlePontuacao) return;
		String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(pedido.vlTotalPontuacaoRealizado, pedido.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoPedidoBase(), LavenderePdaConfig.mostraPontuacaoPedidoRealizado());
		if (ValueUtil.isNotEmpty(vlPontuacao)) {
			lvVlPontuacao.setText(vlPontuacao);
			lvVlPontuacao.setForeColor(PontuacaoConfigService.getInstance().getPontuacaoColor(pedido.vlTotalPontuacaoRealizado, pedido.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoPedidoBase(), LavenderePdaConfig.mostraPontuacaoPedidoRealizado(), false));
		}
	}

	private boolean clientePossuiRede(Cliente cliente) {
		return cliente.cdRede != null && !ValueUtil.VALOR_ZERO.equals(cliente.cdRede);
	}

	private void atualizaIconeComissaoPedido(BaseButton btIcone) {
		try {
			Pedido pedido = getPedido();
			pedido.comissaoPedidoRep = null;
			ComissaoPedidoRepService.getInstance().applyComissaoPedidoRepInPedido(pedido);
			if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				Image image = ComissaoPedidoRepService.getInstance().getIconComissao(pedido.comissaoPedidoRep, false);
				image.applyColor(ComissaoPedidoRepService.getInstance().getCorIconeComissao(pedido.comissaoPedidoRep));
				btIcone.setImage(image);
				if (pedido.comissaoPedidoRep != null) {
					btIcone.setText(StringUtil.getStringValue(pedido.comissaoPedidoRep.dsComissaoPedidoRep));
					btIcone.setForeColor(ComissaoPedidoRepService.getInstance().getCorIconeComissao(pedido.comissaoPedidoRep));
				} else {
					btIcone.setText(Messages.NENHUMA_COMISSAO_ATINGIDA);
					btIcone.setForeColor(Color.BRIGHT);
				}
			}
			repositionTabDinamica();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void atualizaIconePesoFaixa(BaseButton btIcone) {
		try {
			Pedido pedido = getPedido();
			pedido.pesoFaixa = null;
			PesoFaixaService.getInstance().applyPesoFaixaInPedido(pedido);
			Image image = PesoFaixaService.getInstance().getIconPesoFaixa(pedido.pesoFaixa);
			int corIconePesoFaixa = PesoFaixaService.getInstance().getCorIconePesoFaixa(pedido.pesoFaixa);
			image.applyColor(corIconePesoFaixa);
			btIcone.setImage(image);
			if (pedido.pesoFaixa != null) {
				btIcone.setText(StringUtil.getStringValue(pedido.pesoFaixa.dsFaixa));
				btIcone.setForeColor(corIconePesoFaixa);
			} else {
				btIcone.setText(Messages.NENHUMA_PESO_FAIXA_ATINGIDA);
				btIcone.setForeColor(Color.BRIGHT);
			}
			lvPesoPedido.setForeColor(corIconePesoFaixa);
			tabDinamica.reposition();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void aplicaIndiceFinanceiroClientePorPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
			double pctDesc = 0;
			double vlIndice = pedido.getCliente().vlIndiceFinanceiro;
			if (vlIndice != 0) {
				pctDesc = (vlIndice * 100) - 100;
				if (pctDesc < 0) {
					pctDesc *= -1;
				}
			}
			edVlPctDesconto.setValue(pctDesc);
			if (vlIndice > 1) {
				lbPctDesconto.setValue(Messages.ITEMPEDIDO_LABEL_VLPCTACRESCIMO);
			}
		}
	}

	private void atualizaLabelCasoValorMenorZero(LabelValue labelValue, double value) {
		labelValue.setValue(value > 0 ? value : 0);
	}

	private void atualizaIconeRentabilidade(BaseButton btIcone) throws SQLException {
		Pedido pedido = getPedido();

		if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !pedido.isPedidoBonificacao()) {
			MargemRentabFaixa margemRentabFaixa = MargemRentabFaixaService.getInstance().findMargemRentabFaixa(pedido);
			boolean btIconeVisivel = true;
			if (margemRentabFaixa != null) {
				if (!MargemRentabService.isOcultaPercentualFaixaRentabilidadeSePositiva(pedido)) {
					int corFaixa = MargemRentabFaixaService.getInstance().getCorMargemRentabFaixa(margemRentabFaixa.cdCorFaixa);
					btIcone.setImage(UiUtil.getIconButtonAction("images/rentabilidade.png", corFaixa, true));
					btIcone.setText(StringUtil.getStringValue(margemRentabFaixa.dsCorFaixa));
					btIcone.setForeColor(corFaixa);
				} else {
					btIconeVisivel = false;
				}
			} else {
				btIcone.setImage(UiUtil.getIconButtonAction("images/rentabilidade.png", Color.BRIGHT, true));
				btIcone.setText(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_FAIXA_ATINGIDA);
				btIcone.setForeColor(Color.BRIGHT);
			}
			btIcone.setVisible(btIconeVisivel);
			repositionTabDinamica();
			return;
		}

		if (!pedido.isPedidoBonificacao()) {
			btIcone.setImage(RentabilidadeFaixaService.getInstance().getIconRentabilidadePedido(pedido));
			RentabilidadeFaixa rentabilidadeFaixaAtingida = RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaPedido(pedido);
			if (rentabilidadeFaixaAtingida != null) {
				btIcone.setText(StringUtil.getStringValue(rentabilidadeFaixaAtingida.dsFaixa));
				btIcone.setForeColor(RentabilidadeFaixaService.getInstance().getCorRentabilidadeFaixa(rentabilidadeFaixaAtingida));
			} else {
				btIcone.setText(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_FAIXA_ATINGIDA);
				btIcone.setForeColor(Color.BRIGHT);
			}
			repositionTabDinamica();
		}

	}


	//@Override
	protected void clearScreen() throws java.sql.SQLException {
		super.clearScreen();
		Pedido pedido = getPedido();
		lbNuPedido.setText("");
		lvNuPedidoOriginal.setText("");
		lbDsOrigemPedido.setText("");
		lbDsStatusPedido.setText(pedido.statusPedidoPda.dsStatusPedido);
		lbNuPedidoRelacionado.setText("");
		edDtEntrega.setText("");
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			edDtSugestaoCliente.setText("");
		}
		edDtCarregamento.setText("");
		lbVlTotalPedido.setText("");
		lbVlMinPromocional.setText("");
		lbVlTotalPedidoTroca.setText("");
		lvVlPedido.setText("");
		lvVlBruto.setText("");
		lvVlBrutoCapaPedido.setText("");
		lvVlDesconto.setText("");
		double valorBaseTodosItens = getValorBaseTodosItens(pedido);
		double vlDesconto = valorBaseTodosItens - pedido.vlTotalItens;
		double vlTotalPedidoComTributosEDeducoesComFrete = TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido);
		lvVlBruto.setValue(StringUtil.getStringValueToInterface(valorBaseTodosItens));
		lvVlBrutoCapaPedido.setValue(StringUtil.getStringValueToInterface(vlTotalPedidoComTributosEDeducoesComFrete));
		lvVlDesconto.setValue(StringUtil.getStringValueToInterface(vlDesconto < 0 ? 0d : vlDesconto));
		lbVlPctDescCondicaoPagamento.setText("0");
		lbVlTotalItens.setText("");
		lbVlEfetivo.setText("");
		lbVlTotalPedidoMaisFrete.setText("");
		lbVlTotalSTPedido.setText("");
		if (pedido.vlDesconto > 0) {
			if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
				edVlPctDescItem.setValue(pedido.vlDesconto);
			} else {
				edVlPctDesconto.setValue(pedido.vlDesconto);
			}
		} else {
			if (LavenderePdaConfig.apresentaCampoPercentualDescontoCapaPedido()) {
				aplicaIndiceFinanceiroClientePorPedido(pedido);
			} else {
				edVlPctDesconto.setText("");
				edVlPctDescItem.setText("");
			}
		}
		lvVlDescontoCondPagto.setText("");
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() && pedido.vlPctDescCliente > 0) {
			edVlPctDesconto.setValue(pedido.vlPctDescCliente);
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			edVlDesconto.setText("");
			edPctDesconto.setText("");
		}
		edVlPctAcrescimoItem.setText(ValueUtil.VALOR_NI);
		setLabelVlPctMaxDescontoPedido();
		edVlPctDescEspecial.setText("");
		edNuOrdemCompraCliente1.setText(ValueUtil.VALOR_NI);
		edNuOrdemCompraCliente2.setText(ValueUtil.VALOR_NI);
		lbDtEmissao.setValue(pedido.dtEmissao);
		Cliente cliente = SessionLavenderePda.getCliente();
		lbDsCliente.setValue(cliente.toString());
		if (LavenderePdaConfig.usaApresentacaoFixaTicketMedioCapaPedido) {
			lvVlTicketMedio.setValue(cliente.vlTicketMedio);
		}
		lbDsNmEmpresa.setValue(EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa));
		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
			lbDsNmEmpresaCapa.setValue(EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa));
		}
		cbTipoEntrega.setValue(cliente.cdTipoEntrega);
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			bgGeraCreditoBonificacaoFrete.setValue(ValueUtil.VALOR_NAO);
			lvGeraCreditoBonificacaoFrete.setVisible(false);
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			bgGeraCreditoBonificacaoCondicao.setValue(ValueUtil.VALOR_NAO);
			lvGeraCreditoBonificacaoCondicao.setVisible(false);
		}
		//DATA DE ENTREGA
		if (edDtEntrega != null && !LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			Date dtEntrega;
			if (LavenderePdaConfig.usaWebserviceSankhyaComplementaPedido && ValueUtil.isNotEmpty(pedido.dtEntrega)) {
				dtEntrega = pedido.dtEntrega;
			} else {
				dtEntrega = PedidoService.getInstance().getDataPrevisaoEntrega(pedido, cliente);
			}
			edDtEntrega.setValue(dtEntrega);
			if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
				pedido.dtEntrega = dtEntrega;
			}
		}
		//NFE
		if (LavenderePdaConfig.mostraAbaNfeNoPedido) {
			lvNuNfe.setText("");
			lvVlTotalProdutosNfe.setText("");
			lvVlTotalNfe.setText("");
			lvDtSaida.setText("");
			lvHrSaida.setText("");
			lvDtEmissaoNfe.setText("");
			lvCdStatusNfe.setText("");
			lvDsNaturezaOperacao.setText("");
			lvVlChaveAcesso.setText("");
			lvVlSerieNfe.setText("");
			lvnuLote.setText("");
			lvDsObservacao.setText("");
		}
		//NFCE
		if (LavenderePdaConfig.mostraAbaNfceNoPedido) {
			lvQtTotalItem.setValue("");
			lvVlTotalNfce.setValue("");
			lvVlTotalDesconto.setValue("");
			lvVlTotalLiquidoNfce.setValue("");
			lvDsFormaPagamento.setValue("");
			lvVlTotalPago.setValue("");
			lvVlTroco.setValue("");
			lvNuChaveAcesso.setValue("");
			lvNuNfce.setValue("");
			lvNuSerie.setValue("");
			lvDtEmissaoNfce.setValue("");
			lvHrEmissaoNfce.setValue("");
			lvNuProtocoloAutorizacao.setValue("");
			lvDtAutorizacao.setValue("");
			lvHrAutorizacao.setValue("");
			lvVlTotalTributos.setValue("");
			lvVlPctTributosFederais.setValue("");
			lvVlPctTributosEstaduais.setValue("");
			lvVlPctTributosMunicipais.setValue("");
		}
		// Valor Total Bruto
		if (!LavenderePdaConfig.mostraValorBruto && LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			lvVlTotalPedidoComTributosEDeducoes.setText("");
			lvVlTtPedidoTributosEDeducoesEDesc.setText("");
		}
		// Valor Pedido Com Impostos
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			lvVlTotalPedidoComImpostos.setText("");
		}
		//Frete
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || usaInfoFreteAbaEntrega()) {
			lbVlTotalFretePedido.setText("");
			edVlPctFreteRep.setText("");
			edVlFreteRep.setText("");
			edVlFreteCli.setText("");
			lbVlTotalPedidoComFrete.setText("");
			lbVlTotalPedidoAbaFrete.setText("");
		}
		clearScreenTransportadoraReg();
		// ROTA ENTREGA
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
			cbRotaEntrega.load(cliente.cdCliente);
			cbRotaEntrega.setValue(cliente.cdRotaEntrega);
			if (ValueUtil.isEmpty(cbRotaEntrega.getValue()) && !"3".equals(LavenderePdaConfig.usaRotaDeEntregaNoPedidoComCadastro)) {
				cbRotaEntrega.setSelectedIndex(0);
			}
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			bgUtilizaRentabilidade.setValueBoolean(false);
			pedido.flUtilizaRentabilidade = bgUtilizaRentabilidade.getValue();
		}
		//-- TIPO PEDIDO
		preSelecionaTipoPedidoCombo(cliente);
		if (ValueUtil.isNotEmpty(cbTipoPedido.getValue())) {
			cbTipoPedidoClick();
		}
		clearScreenCombosPrincipais();
		//--
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			lvVlVerbaPedido.setText("");
			if (LavenderePdaConfig.isMostraFlexPositivoPedido(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
				edVlVerbaPedidoPositiva.setText("");
			}
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			lvVlPctIndiceFinCondPagto.setValue("");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			lvVlPctDescQuantidadePeso.setValue("");
		}
		lvVlTotalFrete.setText("");
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
			TipoFrete tipoFrete = (TipoFrete) cbTipoFrete.getSelectedItem();
			if (tipoFrete != null) {
				lbVlMaxPctDescFrete.setValue(StringUtil.getStringValueToInterface(tipoFrete.vlPctMaxDesconto));
			}
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			CondicaoPagamento condicaoPagamento = (CondicaoPagamento) cbCondicaoPagamento.getSelectedItem();
			if (condicaoPagamento != null) {
				lbVlMaxPctDescCondicao.setValue(StringUtil.getStringValueToInterface(condicaoPagamento.vlPctDescontoTotalPedido));
			}
		}
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			edDescontoCascataManualDescCliente.setValue(pedido.vlPctDescCliente);
			edDescontoCascataManual2.setValue(0);
			edDescontoCascataManual3.setValue(0);
		}
		//--
		lvVlTotalMargem.setText("");
		lvVlPctTotalMargem.setText("");
		lvVlTotalPedidoComTributos.setText("");
		lvVlTotalBrutoItens.setText("");
		lvVlTotalLiberado.setText("");
		lvPercDescLibRestante.setText("");
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			lvPesoPedido.setText("");
		}
		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			lbQtItensFaturados.setText("");
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			cbAreaVenda.load(pedido.cdCliente);
			cbAreaVenda.setSelectedIndex(BaseComboBox.DefaultItemNull);
			if (cbAreaVenda.size() == 1) {
				AreaVenda areaVenda = (AreaVenda) cbAreaVenda.getItemAt(0);
				cbAreaVenda.setValue(areaVenda.cdAreavenda);
			}
		}
		//--
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
			if (ValueUtil.isEmpty(cliente.dtVencimentoAlvara)) {
				lvDsStatusAlvara.setForeColor(ColorUtil.softRed);
				lvDsStatusAlvara.setValue(Messages.PEDIDO_LABEL_STATUSALVARA_NAO);
			} else if (cliente.isAlvaraVigente()) {
				lvDsStatusAlvara.setForeColor(ColorUtil.componentsForeColor);
				lvDsStatusAlvara.setValue(cliente.dtVencimentoAlvara.toString());
			} else {
				lvDsStatusAlvara.setForeColor(ColorUtil.softRed);
				lvDsStatusAlvara.setValue(cliente.dtVencimentoAlvara.toString());
			}
		}
		//--
		if (LavenderePdaConfig.mostraSaldoContaCorrenteCliente) {
			double vlSaldoContaCorrente = ValueUtil.getDoubleValue(FichaFinanceiraService.getInstance().getColumnFichaFinanceira(pedido.getCliente(), "VLSALDOCONTACORRENTE"));
			edVlSaldoCCCliente.setValue(vlSaldoContaCorrente);
			if (vlSaldoContaCorrente == 0) {
				edVlSaldoCCCliente.setForeColor(ColorUtil.componentsForeColor);
			} else if (vlSaldoContaCorrente < 0) {
				edVlSaldoCCCliente.setForeColor(Color.RED);
			} else {
				edVlSaldoCCCliente.setForeColor(ColorUtil.softGreen);
			}
		}
		//--
		edRotaEntrega1.setValue(cliente.cdRotaEntrega);
		edRotaEntrega2.setValue(ValueUtil.getIntegerValue(cliente.cdRotaEntrega));
		//Setor do Clietne no Pedido
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			String cdContrato = pedido.getCliente().cdContratoEspecial;
			if (ValueUtil.isNotEmpty(cdContrato)) {
				if (pedido.getCliente().isClienteContratoEspecial()) {
					//clietne com contrato
					cbClienteSetorOrigem.load(ClienteSetorOrigem.CLIENTE_SETOR_CONTRATO, cdContrato);
				} else {
					//cliente da rede
					cbClienteSetorOrigem.load(ClienteSetorOrigem.CLIENTE_SETOR_REDE, cdContrato);
				}
				if (ValueUtil.isNotEmpty(cbClienteSetorOrigem.getValue())) {
					loadComboClienteSetor(cbClienteSetorOrigem.getValue());
				} else {
					cbClienteSetor.showNoneItem();
				}
			} else {
				cbClienteSetorOrigem.loadClienteSemContrato();
				cbClienteSetor.loadClienteSemContrato();
			}
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			listParcelaPedidoForm.setPedido(pedido);
			listParcelaPedidoForm.clearGrid();
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() || (LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.isOcultaRentabilidadePedido())) {
			lvVlPctRentabilidade.setText("");
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
			lvVlEscalaRentabilidade.setText("");
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvVlIndiceRentabPedido.setText("");
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
			lvVlPctComissao.setText("");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			cbClienteSetorOrigem.setEnabled(isEnabled() && ValueUtil.isEmpty(pedido.itemPedidoList) && !cbClienteSetorOrigem.getValue().equals(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO) && (cbClienteSetorOrigem.defaultItemType != BaseComboBox.DefaultItemType_NONE_ITEN));
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			cbCentroCusto.carregaCentroCusto();
			if (cbCentroCusto.getValue() != null) {
				cbCentroCustoChange();
			} else if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
				cbPlataformaVenda.carregaPlataformas(cbCentroCusto.getValue(), getPedido().getCliente().cdCliente, getPedido().cdRepresentante);
				if (cbPlataformaVenda.getValue() != null) cbPlataformaVendaChange();
			}
		}
		if (LavenderePdaConfig.isUsaItemContaInformacoesAdicionais()) {
			cbItemConta.carregaItemConta();
			cbItemConta.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaClasseValorInformacoesAdicionais()) {
			cbClasseValor.carregaClasseValor();
			cbClasseValor.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			cbModoFaturamento.carregaModoFaturamento();
			cbModoFaturamento.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			cbTipoFrete.selectTipoFretePadrao();
			pedido.cdTipoFrete = cbTipoFrete.getValue();
			clearFieldsInfoFrete();
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			edNuKmInicial.setText("");
			edNuKmFinal.setText("");
			edHrInicialIndicado.setValue("");
			edHrFinalIndicado.setValue("");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0 && !LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			cbEnderecoEntrega.carregaClienteEnderecoPadrao(pedido, false);
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			cbEnderecoCobranca.carregaClienteEnderecoPadrao(pedido, true);
		}
		if (TABPANEL_BOLETO != 0) {
			listGridBoleto.removeAllContainers();
		}
		if (TABPANEL_ATIVIDADES != 0) {
			listGridAtividadePedido.removeAllContainers();
		}
		if (TABPANEL_NOTAS_FISCAIS != 0) {
			listNotaFiscalForm.getListContainer().removeAllContainers();
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			lvUsuarioItemLiberacao.setText("");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			lvVlTotalVolumePedido.setValue("");
		}
		if(LavenderePdaConfig.utilizaNotasCredito()) {
			lvVlTotalNotaCredito.setValue(ValueUtil.round(pedido.vlTotalNotaCredito));
			aplicaIndiceFinanceiroClientePorPedido(pedido);
			reposition();
		}

		loadFlEnviaEmailDefault(false);
		if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) {
			ButtonGroupBoolean control = getButtonGroupFlEnviaEmail();
			flEnviaEmailValueChange(control != null ? control.getValueBoolean() && isEnabled() : false, true);
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			cbContatoErp.load(pedido.cdCliente);
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			cbBoletoConfig.setSelectedIndex(BaseComboBox.DefaultItemNull);
			edNuAgencia.setText("");
			edNuConta.setText("");
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.defaultItemType = UnidadeComboBox.DefaultItemType_SELECT_ONE_ITEM;
			cbUnidade.load();
			cbUnidade.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			edVlPctDescontoVendaMensal.setValue("");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			edDescCascataCategoria1.setValue(0);
			edDescCascataCategoria2.setValue(0);
			edDescCascataCategoria3.setValue(0);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			cbStatusOrcamento.load(pedido);
			cbStatusOrcamento.setValue(pedido.cdStatusOrcamento);
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			edDtPagamento.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.mostraDescAcessoriaCapaPedido) {
			lvVlDespesaAcessoria.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			lvVlSeguroPedido.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.mostraValorParcelaPedido) {
			lvVlValorParcelaPedido.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.mostraPontuacaoPedidoBase() || LavenderePdaConfig.mostraPontuacaoPedidoRealizado()) {
			final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(0, 0, LavenderePdaConfig.mostraPontuacaoPedidoBase(), LavenderePdaConfig.mostraPontuacaoPedidoRealizado());
			if (vlPontuacao != null) {
				lvVlPontuacao.setValue(vlPontuacao);
			}
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			bgPedidoGondola.setValueBoolean(false);
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			bgUsaCodigoInternoCliente.setValueBoolean(getPedido().getCliente().isUsaCodigoInterno());
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			lvVlTotalFreteItensPedido.setText(ValueUtil.VALOR_NI);
		}
		repositionTabDinamica();
	}

	protected String getCdTipoPedidoDefault(TipoPedidoComboBox cbTipoPed, Cliente cliente, int prioridade) {
		switch (prioridade) {
		case 1:
			return this.cdTipoPedidoAuto;
		case 2:
			if (LavenderePdaConfig.usaEscolhaModoFeira && SessionLavenderePda.isModoFeira) {
				TipoPedido tipo;
				for (int i = 0; i < cbTipoPed.size(); i++) {
					tipo = (TipoPedido) cbTipoPed.getItemAt(i);
					if (tipo.isFeira()) {
						return tipo.cdTipoPedido;
					}
				}
			}
			return null;
		case 3:
			return cliente.cdTipoPedido;
		case 4:
			TipoPedido tipo;
			String cdTipoPedido = null;
			for (int i = 0; i < cbTipoPed.size(); i++) {
				tipo = (TipoPedido) cbTipoPed.getItemAt(i);
				if (tipo.isDefault()) {
					cdTipoPedido = tipo.cdTipoPedido;
					break;
				} else if (ValueUtil.isEmpty(cdTipoPedido) && !tipo.isExigeSenha()) {
					cdTipoPedido = tipo.cdTipoPedido;
				}
			}
			return cdTipoPedido;
		default:
			return ((TipoPedido) cbTipoPed.getItemAt(0)).cdTipoPedido;
		}
	}
	
	protected void preSelecionaTipoPedidoCombo(Cliente cliente) {
		if (ValueUtil.isEmpty(cbTipoPedido.getItems())) {
			return;
		}
		int prioridade = 1;
		while (cbTipoPedido.getSelectedIndex() == BaseComboBox.DefaultItemNull && prioridade <= 5) {
			cbTipoPedido.setValue(getCdTipoPedidoDefault(cbTipoPedido, cliente, prioridade++));
		}
	}

	private void clearScreenTransportadoraReg() {
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido()	&& LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			lbVlTotalPedidoFrete.setText("");
			lbVlFrete.setText("");
			lbNmTransportadora.setText("");
			lbNmTipoFrete.setText("");
			if (LavenderePdaConfig.isExibeInformacoesFreteCapaPedidoEscolhaTransportadora()) {
				lvTransportadoraCapaPedido.setText("");
				lvTipoFreteCapaPedido.setText("");
				lvVlFreteCapaPedido.setText("");
			}
		}
	}

	private void clearScreenCombosPrincipais() throws SQLException {
		Pedido pedido = getPedido();
		Cliente cliente = pedido.getCliente();
		//-- SEGMENTO
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			cbSegmento.load(pedido.getCliente());
			cbSegmento.setDefaultValue();
			if (ValueUtil.isEmpty(cbSegmento.getValue())) {
				cbSegmento.setSelectedIndex(0);
		    }
			pedido.cdSegmento = cbSegmento.getValue();
		}
		//-- CONDIÇÃO COMERCIAL
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			reloadComboCondicaoComercial();
		}
		//-- TRANPORTADORA
		if (LavenderePdaConfig.usaTransportadoraPedido()) {
			reloadComboTransportadora();
		}
		//-- TIPO FRETE
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			cbTipoFrete.setSelectedIndex(-1);
		} else if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			if (LavenderePdaConfig.usaTipoFretePorCliente || LavenderePdaConfig.usaTipoFretePorEstado) {
				cbTipoFrete.loadTipoFrete(pedido);
			}
			cbTipoFrete.selectTipoFretePadrao();
		}
		//-- CONDIÇÃO PAGAMENTO parte1
		if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
			reloadComboCondicaoPagamento();
		}
		if (isShowComboTributacaoNovoCliente()) {
			cbTributacao.loadComboTributacao();
			if (ValueUtil.isEmpty(cbTributacao.getValue())) {
				cbTributacao.setSelectedIndex(0);
			}
			pedido.getCliente().cdTributacaoCliente = cbTributacao.getValue();
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			cbCargaPedido.loadCargaPedido(pedido.getCliente().cdCliente, true);
			pedido.cdCargaPedido = cbCargaPedido.getValue();
		}
		// TABELA DE PREÇO
		if (!LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
			reloadTabelaPreco();
			reloadComboTabelaPreco();
		}
		//--
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente()) {
			//-- TIPO PAGAMENTO
			loadComboTipoPagamento(cliente);
			// CONDIÇÃO PAGAMENTO parte2
			loadComboCondicaoPagamento();
		} else {
			// CONDIÇÃO PAGAMENTO parte2
			loadComboCondicaoPagamento();
			//-- TIPO PAGAMENTO
			loadComboTipoPagamento(cliente);
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			cbClienteSetorOrigem.setEnabled(isEnabled() && ValueUtil.isEmpty(pedido.itemPedidoList) && !cbClienteSetorOrigem.getValue().equals(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO) && (cbClienteSetorOrigem.defaultItemType != BaseComboBox.DefaultItemType_NONE_ITEN));
		}
	}

	private void reloadTabelaPreco() throws SQLException {
		Pedido pedido = getPedido();
		Cliente cliente = pedido.getCliente();
		cbTabelaPreco.loadTabelasPrecos(pedido);
		pedido.cdTabelaPreco = cliente.cdTabelaPreco;
		getPedidoService().setTabPrecoUltimoPedidoNoPedidoAtual(cbTabelaPreco.getItems(), pedido);
		if (ValueUtil.isEmpty(pedido.cdTabelaPreco) && isEnabled() && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido) {
			cbTabelaPreco.setSelectedIndex(0);
			pedido.cdTabelaPreco = cbTabelaPreco.getValue();
		} else {
			cbTabelaPreco.setValue(pedido.cdTabelaPreco);
		}
		SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		ultimaTabelaPrecoSelected = cbTabelaPreco.getValue();
	}

	public void reloadTabelaPrecoSessaoEmpresa() throws SQLException {
		Pedido pedido = getPedido();
		cbTabelaPreco.loadTabelasPrecos(pedido);
		if (pedido.isPedidoTroca() && pedido.getCliente() != null) {
			cbTabelaPreco.setValue(pedido.getCliente().cdTabelaPreco);
		} else {
			cbTabelaPreco.setValue(pedido.cdTabelaPreco);
		}
		SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		ultimaTabelaPrecoSelected = cbTabelaPreco.getValue();
	}

	private void clearFieldsInfoFrete() {
		bgTaxaEntrega.setValue("");
		edVlTaxaEntrega.setText("");
		bgAjudante.setValue("");
		edQtAjudante.setText("");
		bgAntecipaEntrega.setValue("");
		bgAgendamento.setValue("");
		lbVlTotalFretePedido.setText("");
		lbVlTotalPedidoAbaFrete.setText("");
		lbVlTotalPedidoComFrete.setText("");
		cbTipoVeiculo.setSelectedIndex(-1);
	}

	private void loadComboCondicaoPagamento() throws SQLException {
		if (!LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			reloadComboCondicaoPagamento();
		}
	}

	private void loadComboTipoPagamento(Cliente cliente) throws SQLException {
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() || LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente()) {
			Pedido pedido = getPedido();
			cbTipoPagamento.carregaTipoPagamentos(pedido, false);
			//--
			cbTipoPagamento.setValue(cliente.cdTipoPagamento);
			if (ValueUtil.isEmpty(cbTipoPagamento.getValue())) {
				cbTipoPagamento.setSelectedIndex(0);
			}
			//--
			pedido.cdTipoPagamento = cbTipoPagamento.getValue();
			ultimoTipoPagamentoSelected = cbTipoPagamento.getValue();
			//--
			if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento) {
				double qtMinValor = pedido.getTipoPagamento().qtMinValor;
				if (qtMinValor > 0) {
					edValorMinTipoPagto.setValue(qtMinValor);
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(Messages.VALOR_MINIMO_PEDIDO).append(" |").append(qtMinValor);
					tipVlMinTipoPagto.setText(strBuffer.toString());
				} else {
					edValorMinTipoPagto.setText("");
					tipVlMinTipoPagto.setText(Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
				}
			}
		}
	}

	private void reloadComboTabelaPreco() throws SQLException {
		Pedido pedido = getPedido();
		cbTabelaPreco.loadTabelasPrecos(pedido);
		if (cbTabelaPreco.size() > 0) {
			if (!ValueUtil.isEmpty(ultimaTabelaPrecoSelected)) {
				cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
			}
			if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
				cbTabelaPreco.setValue(pedido.getCliente().cdTabelaPreco);
			}
			if (LavenderePdaConfig.usaSugestaoTabPrecoECondPagto && !isEditing()) {
				getPedidoService().setTabPrecoUltimoPedidoNoPedidoAtual(cbTabelaPreco.getItems(), pedido);
				cbTabelaPreco.setValue(pedido.cdTabelaPreco);
			}
			if (ValueUtil.isEmpty(cbTabelaPreco.getValue()) && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido) {
				cbTabelaPreco.setSelectedIndex(0);
			}
			pedido.cdTabelaPreco = cbTabelaPreco.getValue();
		}
		//--
		try {
			cbTabelaPrecoChange();
		} catch (Throwable e) {
			cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
			pedido.cdTabelaPreco = ultimaTabelaPrecoSelected;
		}
		//--
		SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
	}

	private void reloadComboCondicaoPagamento() throws SQLException {
		Pedido pedido = getPedido();
		cbCondicaoPagamento.loadCondicoesPagamento(pedido);
		if (cbCondicaoPagamento.size() > 0) {
			if (!ValueUtil.isEmpty(ultimaCondPgamentoSelected)) {
				cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
				pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
			}
			if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
				cbCondicaoPagamento.setValue(pedido.getCliente().cdCondicaoPagamento);
				pedido.cdCondicaoPagamento = pedido.getCliente().cdCondicaoPagamento;
			}
			if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
				cbCondicaoPagamento.setSelectedIndex(0);
				pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
			}
			if (LavenderePdaConfig.usaSugestaoTabPrecoECondPagto && !isEditing()) {
				getPedidoService().setCondPagtoUltimoPedidoNoPedidoAtual(cbCondicaoPagamento.getItems(), pedido);
				cbCondicaoPagamento.setValue(pedido.cdCondicaoPagamento);
			}
		}
		try {
			cbCondicaoPagamentoClick();
		} catch (Throwable e) {
			exceptionHandleCbCondicaoPagamento(pedido);
		}
	}

	private void exceptionHandleCbCondicaoPagamento(Pedido pedido) {
		if (ultimaCondPgamentoSelected == null) ultimaCondPgamentoSelected = "";

		if (!ultimaCondPgamentoSelected.equals(cbCondicaoPagamento.getValue())) {
			cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
			pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
		} else {
			cbCondicaoPagamento.setSelectedIndex(BaseComboBox.DefaultItemNull);
			ultimaCondPgamentoSelected = "";
			pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
		}
	}

	private void reloadComboCondicaoPagamentoFiltrandoValorMinimo() throws SQLException {
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo()) {
			Pedido pedido = getPedido();
			cbCondicaoPagamento.loadCondicoesPagamento(pedido);
			cbCondicaoPagamento.setValue(pedido.cdCondicaoPagamento);
			if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue()) && ValueUtil.isNotEmpty(ultimaCondPgamentoSelected)) {
				ultimaCondPgamentoSelected = pedido.cdCondicaoPagamento = "";
				pedido.setCondicaoPagamento(null);
				setEdValorMinCondicaoPagamento(0);
				setEdPrazoMedio(0);
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_CDCONDICAOPAGAMENTO, "", Types.VARCHAR);
				UiUtil.showWarnMessage(Messages.PEDIDO_MSG_ERRO_CONDICAO_PAGAMENTO_RETIRADA_COMBO);
			}
		}
	}

	public void reloadComboCondicaoComercial() throws SQLException {
		Pedido pedido = getPedido();
		cbCondicaoComercial.carregaCondicoesComerciais(pedido);
		if (cbCondicaoComercial.size() > 0) {
			if (!ValueUtil.isEmpty(ultimaCondicaoComercialSelected)) {
				cbCondicaoComercial.setValue(ultimaCondicaoComercialSelected);
			}
			if (ValueUtil.isNotEmpty(pedido.getCliente().cdCondicaoComercial)) {
				cbCondicaoComercial.setValue(pedido.getCliente().cdCondicaoComercial);
			}
			if (ValueUtil.isEmpty(cbCondicaoComercial.getValue())) {
				cbCondicaoComercial.setDefaultValue();
			}
			if (ValueUtil.isEmpty(cbCondicaoComercial.getValue())) {
				cbCondicaoComercial.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.isUsaCondicaoComercialCliPadraoVazio()) {
				cbCondicaoComercial.setSelectedIndex(-1);
			}
			pedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
		}
		//--
		try {
			cbCondicaoComercialChange();
		} catch (Throwable e) {
			cbCondicaoComercial.setValue(ultimaCondicaoComercialSelected);
			pedido.cdCondicaoComercial = ultimaCondicaoComercialSelected;
		}
	}

	private boolean hasCondicaoPagamentoPadraoInCombo(String cdCondicaoPagamento) throws SQLException {
		if(cbCondicaoPagamento.getItems() == null) return false;
		int size = cbCondicaoPagamento.getItems().length;
		for (int i = 0; i < size; i++) {
			CondicaoPagamento condicaoPagamento = (CondicaoPagamento) cbCondicaoPagamento.getItemAt(i);
			if (condicaoPagamento.cdCondicaoPagamento.equalsIgnoreCase(cdCondicaoPagamento)) {
				ultimaCondPgamentoSelected = cdCondicaoPagamento;
				return LavenderePdaConfig.usaPermiteCondPagtoPadraoCliTipoPed() ? ValueUtil.VALOR_SIM.equals(getPedido().getTipoPedido().flUtilizaCondPgtoPadraoCli) : true;
			}
		}
		return false;
	}

	private void setEnableCombosPrincipais() throws SQLException {
		//-- SEGMENTO
		Pedido pedido = getPedido();
		cbSegmento.setEnabled(!((LavenderePdaConfig.usaTabelaPrecoPorSegmento || LavenderePdaConfig.usaCondicaoPagamentoPorSegmento) && pedido.itemPedidoList.size() > 0) && isEnabled());
		//-- CONDICAO COMERCIAL
		setCbCondicaoComercialEnabled(pedido);
		//-- TRANSPORTADORA
		boolean transportadoraEnabled = LavenderePdaConfig.usaTransportadoraPedido() && pedido.isPedidoAbertoEditavel() && !LavenderePdaConfig.usaCalculoFretePersonalizado();
		boolean bloqueiaAlterarTransportadora = LavenderePdaConfig.isBloqueiaAlterarTransportadora() ? (ValueUtil.isEmpty(pedido.getCliente().cdTransportadora) && transportadoraEnabled) : transportadoraEnabled;
		cbTransportadora.setEditable(bloqueiaAlterarTransportadora);
		cbTransportadoraAux.setEditable(transportadoraEnabled && LavenderePdaConfig.usaTransportadoraAuxiliar);
		cbTranspFretePersonalizado.setEnabled(false);
		//-- TIPO PAGAMENTO
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() && !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente()) {
			boolean clientePossuiTipoPgtoDefault = (cbTipoPagamento.getValue() != null) && (cbTipoPagamento.getValue().equals(pedido.getCliente().cdTipoPagamento));
			cbTipoPagamento.setEnabled(!((ValueUtil.VALOR_SIM.equals(pedido.getCliente().flEspecial)) && clientePossuiTipoPgtoDefault && LavenderePdaConfig.travaTipoPagtoPadraoClienteEspecial) && isEnabled());
			edValorMinTipoPagto.setEnabled(false);
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			cbAreaVenda.setEnabled(!isEditing() || (pedido.vlTotalItens == 0));
		}
		//-- CONDICAO NEGOCIACAO
		cbCondicaoNegociacao.setEnabled(LavenderePdaConfig.usaCondicaoNegociacaoNoPedido && isEnabled());
		//--
		TabelaPreco tabPrecoSelected = cbTabelaPreco.getTabelaSelecionada();
		boolean clientePossuiTabPrecoDefaultEspecial = ClienteService.getInstance().isClientePossuiTabPrecoDefaultEspecial(pedido.getCliente(), tabPrecoSelected);
		boolean cbTabelaPrecoEnabled = isEnabled() && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido;
		cbTabelaPreco.setEnabled(cbTabelaPrecoEnabled && ((pedido.itemPedidoList.size() == 0 && isNaoPossuiPagamentos()) || LavenderePdaConfig.permiteAlterarTabelaPrecoPedido) && !clientePossuiTabPrecoDefaultEspecial);
		//--
		if (!LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			boolean clientePossuiCondPgtoDefault = false;
			if (LavenderePdaConfig.usaBloqueiaCondPagtoPadraoClienteNoPedido()) {
				boolean hasCondicaoPadraoInCombo = hasCondicaoPagamentoPadraoInCombo(pedido.getCliente().cdCondicaoPagamento);
				clientePossuiCondPgtoDefault = (cbCondicaoPagamento.getValue() != null) && hasCondicaoPadraoInCombo;
				if (hasCondicaoPadraoInCombo) cbCondicaoPagamento.setValue(pedido.getCliente().cdCondicaoPagamento);
			}
			boolean usaTabPrecoPorCondPagto = false;
			if (LavenderePdaConfig.usaTabelaPrecoPorClienteOuCondPagto) {
				usaTabPrecoPorCondPagto = pedido.usandoTabelaPrecoPorCondicaoPagamento && (pedido.itemPedidoList.size() > 0);
			} else {
				usaTabPrecoPorCondPagto = LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento && (pedido.itemPedidoList.size() > 0);
			}
			cbCondicaoPagamento.setEnabled(!clientePossuiTabPrecoDefaultEspecial && !clientePossuiCondPgtoDefault && !usaTabPrecoPorCondPagto && isEnabled());
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.setEnabled(isEnabled() && pedido.itemPedidoList.size() == 0);
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			cbClienteSetorOrigem.setEnabled(isEnabled() && ValueUtil.isEmpty(pedido.itemPedidoList) && !cbClienteSetorOrigem.getValue().equals(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO) && (cbClienteSetorOrigem.defaultItemType != BaseComboBox.DefaultItemType_NONE_ITEN));
		}

		boolean isTipoPedidoEditable = isEnabled() && !LavenderePdaConfig.bloqueiaTipoPedidoPadraoClienteNoPedido;
		isTipoPedidoEditable &= !LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente && isTipoPedidoEnabled();
		isTipoPedidoEditable &= !LavenderePdaConfig.usaTabelaPrecoPorTipoPedido || LavenderePdaConfig.usaTabelaPrecoPorTipoPedido && isPedidoSemItens(pedido);
		isTipoPedidoEditable &= !LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() || (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() && isPedidoSemItens(pedido)) || !LavenderePdaConfig.consisteConversaoUnidadesIgnoraMultiploEspecial();
		isTipoPedidoEditable &= !LavenderePdaConfig.usaPedidoBonificacao() || (LavenderePdaConfig.usaPedidoBonificacao() && (TipoPedidoService.getInstance().possuiMaisDeUmTipoBonificadoNaCarga(pedido) || isPedidoSemItens(pedido)));
		isTipoPedidoEditable |= (pedido.isReplicandoPedido && !pedido.isPedidoAbertoNaoEditavel());
		cbTipoPedido.setEditable(isTipoPedidoEditable);

		if (pedido.isPedidoTroca()) {
			cbCondicaoPagamento.setEditable(false);
			cbTabelaPreco.setEditable(false);
			cbTipoPagamento.setEditable(false);
		}
		if (pedido.isPedidoBonificacao()) {
			if (!LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
				cbTabelaPreco.setEditable(false);
			}
			cbCondicaoPagamento.setEditable(LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao && isEnabled());
			cbTipoPagamento.setEditable(false);
		}
		if (LavenderePdaConfig.isBloqueiaTipoPagamentoPadraoClienteNoPedido() && cbTipoPagamento.getValue() != null) {
			if (cbTipoPagamento.getValue().equals(pedido.getCliente().cdTipoPagamento)) {
				cbTipoPagamento.setEditable(false);
			}
		}
		cbCargaPedido.setEditable(isCbCargaPedidoEnabled());
		cbEntrega.setEditable(LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro() && pedido.isPedidoAberto() && isEnabled());
		btRelacionarPedido.setEnabled((pedido.isPedidoAberto() && ((pedido.itemPedidoTrocaList.size() == 0 && pedido.itemPedidoList.size() == 0) || pedido.isPedidoVendaProducao())) || pedido.isPedidoReplicado() || pedido.isPedidoComplementar() && ValueUtil.isEmpty(edNuPedidoRelacionado.getText()));
		cbTributacao.setEditable(isEnabled());

		//Group Booleans
		if (LavenderePdaConfig.usaPedidoProdutoCritico || LavenderePdaConfig.usaGondolaPedido) {
			if (pedido.isPedidoBonificacao()) {
				updateOtherButtonGroupBoolean(new ButtonGroupBoolean());
			}
			boolean bgEnabled = isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList);
			if (LavenderePdaConfig.usaPedidoProdutoCritico) {
				bgPedidoCritico.setEnabled(bgEnabled);
			}
			if (LavenderePdaConfig.usaGondolaPedido) {
				bgPedidoGondola.setEnabled(bgEnabled);
			}
		}
		//
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			ckVinculaCampanhaPublicitaria.setEnabled(pedido.isPedidoAberto() && !pedidoFechadoOuTransmitido());
			btRelacionarCampanhaPublicitaria.setEnabled(pedido.isPedidoAberto() && !pedidoFechadoOuTransmitido());
		}
		if (LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao() && LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && pedido.itemPedidoList.size() > 0) {
			btRelacionarPedido.setEnabled(false);
			edNuPedidoRelacionado.setEnabled(false);
		} else if (btRelacionarPedido != null && edNuPedidoRelacionado != null) {
			btRelacionarPedido.setEnabled(isEnabled());
			edNuPedidoRelacionado.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			cbDivisaoVenda.setEnabled(isEnabled());
		}
	}

	public boolean isPedidoApenasConsulta() throws SQLException {
		return getPedido().isPedidoAbertoNaoEditavel() || inOnlyConsultaItens;
	}

	private boolean isPedidoSemItens(Pedido pedido) {
		return pedido == null || ValueUtil.isEmpty(pedido.itemPedidoList) && ValueUtil.isEmpty(pedido.itemPedidoTrocaList);
	}

	private boolean isNaoPossuiPagamentos() {
		if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido || listPagamentoPedidoForm == null) return true;

		return listPagamentoPedidoForm.getListContainer().size() == 0;
	}

	private boolean isCbCargaPedidoEnabled() throws SQLException {
		Pedido pedido = getPedido();
		boolean enabledCbCargaPedido = LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente() && pedido.isPedidoAberto();
		return enabledCbCargaPedido && !LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido() ? ValueUtil.isEmpty(pedido.itemPedidoList) : enabledCbCargaPedido;
	}

	private boolean isTipoPedidoEnabled() throws SQLException {
		Pedido pedido = getPedido();
		return !LavenderePdaConfig.tipoPedidoOcultoNoPedido && (
							((pedido.itemPedidoTrocaList.size() == 0) && (pedido.itemPedidoList.size() == 0)) ||
							(ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) &&
							!LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido &&
							!LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao &&
							(!LavenderePdaConfig.isConsisteConversaoUnidades() ||
							pedido.getTipoPedido() == null ||
							(pedido.getTipoPedido() != null && pedido.getTipoPedido().isConsisteConversaoUnidade())))
							);
	}

	private boolean isShowInfoPedidoRelacionado() throws SQLException {
		Pedido pedido = getPedido();
		return (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao()))
				|| (LavenderePdaConfig.isObrigaRelacionarPedidoTroca() && pedido.isPedidoTroca())
				|| (LavenderePdaConfig.usaPedidoComplementar() && pedido.isPedidoComplementar())
				|| pedido.isPedidoVendaProducao()
				|| (LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao() && pedido.isPedidoBonificacao());
	}

	//@Override
	public void visibleState() throws SQLException {
		Pedido pedido = getPedido();
		super.visibleState();
		boolean editing = isEditing();
		btVoltar.setVisible(!isEnabled() || !editing || !(PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido()));
		btSalvar.setVisible(isEnabled() && editing && !(PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido()) || pedido.isPedidoConsignado());
		btExcluir.setVisible(false);
		controlaVisibilidadeBotaoExcluirEConsignacao();
		btNovoItem.setVisible(isEnabled() || LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop);
		btListaItens.setVisible(editing && !LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop);
		//--
		btRecalcularPedido.setVisible(pedido.isPedidoAberto() && ValueUtil.isNotEmpty(pedido.itemPedidoList) && PedidoService.getInstance().necessarioRecalculoPedido(pedido));
		btFecharPedido.setVisible(defineBtFecharPedidoVisible(pedido, editing));
		controlaVisibilidadeBotaoWorkflow();
		if (LavenderePdaConfig.usaWorkflowStatusPedido) {
			btWorkflow.setVisible(!pedido.isPedidoAberto());
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento || LavenderePdaConfig.usaLiberacaoProprioRep()) {
			btLiberarPedido.setVisible(isBotaoLiberarPedidoVisivel() && !pedido.isPedidoPerdido());
			btDescontosConcedidos.setVisible(isBotaoDescontosConcedidosVisivel());
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			btLiberarItensPendentes.setVisible(!pedido.isPedidoBonificacao() && pedido.isPedidoPendente() && SessionLavenderePda.isUsuarioLiberaItemPendente() && pedido.isPedidoItemPendente() && !LavenderePdaConfig.isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto());
		}
		if ((LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep()) || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			btAlteraTransportadora.setVisible((LavenderePdaConfig.escolhaTransportadoraPedidoPorCep() || LavenderePdaConfig.usaCalculoFretePersonalizado()) && pedido.isPedidoAberto());
		}
		boolean isBtReabrirPedidoVisible = editing && !inOnlyConsultaItens && LavenderePdaConfig.permiteReabrirPedidoFechado && pedido.isPedidoFechado() && !pedido.isPedidoIniciadoProcessoEnvio();
		isBtReabrirPedidoVisible &= (!LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente() || !pedido.getCargaPedido().isCargaFechada()) && !isPedidoTipoPagamentoRestrito(pedido);
		isBtReabrirPedidoVisible &= LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() && !pedido.getTipoPedido().isTipoPedidoBonificacaoContaCorrente() && !ItemPedidoBonifCfgService.getInstance().isPedidoComItemContaCorrente(pedido) || !LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade();
		isBtReabrirPedidoVisible &= PedidoService.getInstance().isPossuiRegistroDePedidoConsignacao(pedido) && !pedido.isTipoPedidoGeraNfeOuNfce();
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
			isBtReabrirPedidoVisible &= !pedido.isFlOrigemPedidoErp();
		}
		btReabrirPedido.setVisible(isBtReabrirPedidoVisible);
		lbDsCliente.setVisible(editing || SessionLavenderePda.isSessionCliente());
		if (tipCdPgto != null) {
			tipCdPgto.setVisible(!isEnabled() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido());
		}
		cbTipoPagamento.setVisible(!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao());
		edValorMinTipoPagto.setVisible(!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() && LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido());
		boolean apresentaTipoPedido = false;
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			if (LavenderePdaConfig.controlaExibicaoComboTipoPedidoPorPreferenciaFuncao) {
				if (SessionLavenderePda.hasPreferencia(PreferenciaFuncao.PERMITE_ALTERACAO_TIPO_PEDIDO)) {
					apresentaTipoPedido = true;
				}
			} else {
				apresentaTipoPedido = true;
			}
		}
		cbTipoPedido.setVisible(apresentaTipoPedido);
		lbVlBonificacaoPedido.setVisible(LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao);
		// IPI e ST
		boolean usaIPI = LavenderePdaConfig.isUsaCalculoIpiItemPedido();
		boolean usaST = LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido;
		boolean clienteOptanteSimples = pedido.getCliente().isOptanteSimples() && LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples;
		boolean isVlTotalPedidoComTributosVisible = (LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete && (usaIPI || (usaST && !clienteOptanteSimples))) ||
				((usaIPI || (usaST && !clienteOptanteSimples)) && !LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado);
		lvVlTotalPedidoComTributos.setVisible(isVlTotalPedidoComTributosVisible);
		lbVlTotalPedidoComTributos.setVisible(isVlTotalPedidoComTributosVisible);
		lvVlTotalMargem.setVisible(LavenderePdaConfig.usaConfigMargemContribuicaoRegra2());
		lvVlPctTotalMargem.setVisible(LavenderePdaConfig.usaConfigMargemContribuicaoRegra2());
		//--
		lvVlTotalBrutoItens.setVisible(LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.utilizaNotasCredito());
		lvVlTotalLiberado.setVisible(LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !pedido.isPedidoBonificacao());
		lvPercDescLibRestante.setVisible(LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !pedido.isPedidoBonificacao());
		edValorMinTipoPedido.setVisible(LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido());
		TipoPedido tipoPedido = cbTipoPedido.getTipoPedido();
		if (tipoPedido != null) {
			lbVlMinCondPagto.setVisible(LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !tipoPedido.isIgnoraVlMinPedido() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido());
			edValorMinCondPagto.setVisible(LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !tipoPedido.isIgnoraVlMinPedido() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido());
		} else {
			lbVlMinCondPagto.setVisible(LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido());
			edValorMinCondPagto.setVisible(LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido());
		}
		if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
			edPrazoMedio.setVisible(true);
			lbVlPrazoMedio.setVisible(true);
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			edNuParcelaPedido.setVisible(true);
			lbNuParcelasPedido.setVisible(true);
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoApenasNumeros()) {
			edNuOrdemCompraCliente1.setVisible(true);
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos()) {
			edNuOrdemCompraCliente2.setVisible(true);
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			listParcelaPedidoForm.setPermiteEdicaoParcelas(isTabPagtoPermiteEdicaoParcelas());
			atualizaListParcelaPedidoForm(pedido);
		}
		btNovaCargaPedido.setVisible(LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente() && LavenderePdaConfig.permiteCadastrarCargaNaCapaDoPedido && pedido.isPedidoAberto());
		//--
		setPrecoLiberadoSenhaVisible();
		//--
		setFreteVisible();
		//--
		setInfoRentabilidadeVisible(SessionLavenderePda.isOcultoInfoRentabilidade);
		//-
		if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.isOcultaRentabilidadePedido()) {
			lvVlPctRentabilidade.setVisible(!LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoPedido(pedido) && pedido.isPermiteUtilizarRentabilidade());
			lbRentabilidade.setVisible(true);
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
			lvVlPctComissao.setVisible(true);
		}
		//-
		totalizadoresPedidoForm.setInfoRentabilidadeVisible(SessionLavenderePda.isOcultoInfoRentabilidade);
		//--
		if (LavenderePdaConfig.mostraAbaNfeNoPedido) {
			setNfeVisible();
		}
		if (LavenderePdaConfig.mostraAbaNfceNoPedido) {
			setNfceVisible();
		}
		if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && listPagamentoPedidoForm != null) {
			listPagamentoPedidoForm.visibleState();
		}
		if (LavenderePdaConfig.mostraAbaBoletoNoPedido) {
			setBoletoVisible();
		}
		cbCondicaoComercial.setVisible(!LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip);
	}

	private boolean defineBtFecharPedidoVisible(Pedido pedido, boolean editing) throws SQLException {
		return ((!btRecalcularPedido.isVisible() && editing && (isEnabled() || isPedidoAbertoNaoEditavelPermiteFecharExcluir(pedido))
				&& !(pedido.getTipoPedido() != null && pedido.getTipoPedido().isObrigaConsignacao())) || pedido.isPedidoConsignado())
				|| isPedidoComAutorizacaoDeParcelamento(pedido);
	}

	private boolean isPedidoComAutorizacaoDeParcelamento(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()
			|| pedido.isPedidoFechado()
			|| pedido.isPedidoTransmitido()
			|| btReabrirPedido.isVisible()) return false;
		return SolAutorizacaoService.getInstance().hasSolAutorizacaoAutorizadaPedido(pedido, TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX);
	}

	private void setBtLibParcelaSolAutorizacaoVisible(Pedido pedido) {
		boolean btLibParcelaSolAutorizacaoVisible = LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax();
		if (LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento()) {
			btLibParcelaSolAutorizacaoVisible &= (pedido.statusOrcamento == null || pedido.statusOrcamento.permiteFecharPedido());
		}
		btLibParcelaSolAutorizacao.setVisible(btLibParcelaSolAutorizacaoVisible && isEnabled());
	}

	private void setNfceVisible() throws SQLException {
		Pedido pedido = getPedido();
		boolean nfceVisible = ValueUtil.isNotEmpty(pedido.getNfce().nuPedido);
		lbHasNfce.setVisible(!nfceVisible);
		lvQtTotalItem.setVisible(nfceVisible);
		lvVlTotalNfce.setVisible(nfceVisible);
		lvVlTotalDesconto.setVisible(nfceVisible);
		lvVlTotalLiquidoNfce.setVisible(nfceVisible);
		lvDsFormaPagamento.setVisible(nfceVisible);
		lvVlTotalPago.setVisible(nfceVisible);
		lvVlTroco.setVisible(nfceVisible);
		lvNuChaveAcesso.setVisible(nfceVisible);
		lvNuNfce.setVisible(nfceVisible);
		lvNuSerie.setVisible(nfceVisible);
		lvDtEmissaoNfce.setVisible(nfceVisible);
		lvHrEmissaoNfce.setVisible(nfceVisible);
		lvNuProtocoloAutorizacao.setVisible(nfceVisible);
		lvDtAutorizacao.setVisible(nfceVisible);
		lvHrAutorizacao.setVisible(nfceVisible);
		lvVlTotalTributos.setVisible(nfceVisible);
		lvVlPctTributosFederais.setVisible(nfceVisible);
		lvVlPctTributosEstaduais.setVisible(nfceVisible);
		lvVlPctTributosMunicipais.setVisible(nfceVisible);
		lbQtTotalItem.setVisible(nfceVisible);
		lbVlTotalNfce.setVisible(nfceVisible);
		lbVlTotalDesconto.setVisible(nfceVisible);
		lbVlTotalLiquidoNfce.setVisible(nfceVisible);
		lbDsFormaPagamento.setVisible(nfceVisible);
		lbVlTotalPago.setVisible(nfceVisible);
		lbVlTroco.setVisible(nfceVisible);
		lbNuChaveAcesso.setVisible(nfceVisible);
		lbNuNfce.setVisible(nfceVisible);
		lbNuSerie.setVisible(nfceVisible);
		lbDtEmissaoNfce.setVisible(nfceVisible);
		lbHrEmissaoNfce.setVisible(nfceVisible);
		lbNuProtocoloAutorizacao.setVisible(nfceVisible);
		lbDtAutorizacao.setVisible(nfceVisible);
		lbHrAutorizacao.setVisible(nfceVisible);
		lbVlTotalTributos.setVisible(nfceVisible);
		lbVlPctTributosFederais.setVisible(nfceVisible);
		lbVlPctTributosEstaduais.setVisible(nfceVisible);
		lbVlPctTributosMunicipais.setVisible(nfceVisible);
	}

	private boolean isBotaoLiberarPedidoVisivel() throws SQLException {
		Pedido pedido = getPedido();
		boolean consideraBonificacao = !LavenderePdaConfig.isUsaMotivoPendencia() ? !pedido.isPedidoBonificacao() : true;
		boolean botaoLiberarPedidoVisivel = consideraBonificacao && pedido.isPedidoPendente();
		if (LavenderePdaConfig.usaLiberacaoProprioRep()) {
			return pedido.isPedidoPendenteLibRep();
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
			double vlFaixa = ItemPedidoService.getInstance().getMaxDescontoItemPedido(pedido.itemPedidoList);
			return botaoLiberarPedidoVisivel && UsuarioDescService.getInstance().isUsuarioNaFaixaDescontoLiberacaoAlcada(vlFaixa);
		} else {
			botaoLiberarPedidoVisivel &= UsuarioDescService.getInstance().isProximoUsuarioLiberarPedido(pedido);
		}
		int nuMaxSequencia = PedidoDescErpService.getInstance().getMaxNuSequenciaLiberacao(pedido);
		if (SessionLavenderePda.nuOrdemLiberacaoUsuario == nuMaxSequencia) {
			return false;
		}
		if (LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia() || LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
			botaoLiberarPedidoVisivel = isBtLiberarPedidoVisivelPorMotivoPendencia(botaoLiberarPedidoVisivel, pedido, SessionLavenderePda.nuOrdemLiberacaoUsuario);
			if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao)) {
				botaoLiberarPedidoVisivel &= pedido.isPendente();
			}
			return botaoLiberarPedidoVisivel;
		}
		return botaoLiberarPedidoVisivel && (ValueUtil.getDoubleValue(lvPercDescLibRestante.getValue()) > 0 || LavenderePdaConfig.isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto()) ;
	}

	private boolean isBtLiberarPedidoVisivelPorMotivoPendencia(boolean botaoLiberarPedidoVisivel, Pedido pedido, int nuOrdemLiberacaoUsuario) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivoPendencia() || !pedido.isPedidoPendente()) return botaoLiberarPedidoVisivel;

		ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoQueExigeLiberacaoSomenteNivelAprovacao(pedido);
		if (itemPedido == null) return botaoLiberarPedidoVisivel;

		return nuOrdemLiberacaoUsuario == itemPedido.nuOrdemLiberacao || (LavenderePdaConfig.permiteLiberacaoPedidoPendenteOutraOrdemLiberacao && UsuarioConfigService.getInstance().isLiberaPedidoOutraOrdem());
	}
	
	private boolean isBotaoDescontosConcedidosVisivel() throws SQLException {
		return (!getPedido().isPedidoBonificacao() && !getPedido().isPedidoAberto() && !getPedido().isPedidoPerdido()) 
				|| (LavenderePdaConfig.isUsaMotivoPendencia() && (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && !getPedido().isPedidoAberto() && getPedido().isPedidoBonificacao() && ValueUtil.isNotEmpty(getPedido().nuPedidoRelBonificacao));
	}

	private void setVisibleCondicaoPagamento() {
		cbCondicaoPagamento.setVisible(isEnabled() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento());
		edDsCondicaoPagamento.setVisible(!isEnabled() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento());
	}

	private void controlaVisibilidadeBotaoExcluirEConsignacao() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.isPedidoPermiteConsignacao()) {
			if (LavenderePdaConfig.usaPedidoPerdido) {
				btPerderPedido.setVisible(false);
			} else {
				btExcluirPedido.setVisible(false);
			}
			if (!(isEnabled() && isEditing()) || pedido.isPedidoConsignado()) {
				btConsignarPedido.setVisible(false);
				btDevolucaoConsignacaoPedido.setVisible(pedido.isPedidoConsignado());
			} else {
				btConsignarPedido.setVisible(true);
				btDevolucaoConsignacaoPedido.setVisible(false);
			}
		} else {
			btConsignarPedido.setVisible(false);
			btDevolucaoConsignacaoPedido.setVisible(false);

			if (LavenderePdaConfig.usaPedidoPerdido) {
				if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
					btPerderPedido.setVisible(false);
				} else {
					btPerderPedido.setVisible(isEnabled() && isEditing());
				}
			} else {
				if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
					btExcluirPedido.setVisible(false);
				} else {
					btExcluirPedido.setVisible(isEnabled() && isEditing() || isPedidoAbertoNaoEditavelPermiteFecharExcluir(pedido));
				}
			}
		}
	}

	private void controlaVisibilidadeBotaoWorkflow() throws SQLException {
		if (LavenderePdaConfig.usaWorkflowStatusPedido) {
			Pedido pedido = getPedido();
			if (pedido.isPedidoConsignado()) {
				btWorkflow.setVisible(false);
			} else {
				btWorkflow.setVisible(!pedido.isPedidoAberto());
			}
		}
	}

	private boolean isPedidoTipoPagamentoRestrito(Pedido pedido) throws SQLException {
		TipoPagamento tipoPagamento = pedido.getTipoPagamento();
		return LavenderePdaConfig.usaTipoPagamentoRestritoVenda && tipoPagamento != null && tipoPagamento.isRestrito();
	}

	private void setBoletoVisible() throws SQLException {
		boolean isBoletoVisible = ValueUtil.isNotEmpty(getPedido().getPedidoBoletoList());
		lbHasBoleto.setVisible(!isBoletoVisible);
		listGridBoleto.setVisible(isBoletoVisible);
	}

	private void setNfeVisible() throws SQLException {
		Pedido pedido = getPedido();
		boolean nfeVisible = ValueUtil.isNotEmpty(pedido.getInfoNfe().nuPedido);
		lbHasNfe.setVisible(!nfeVisible);
		lbCdStatusNfe.setVisible(nfeVisible);
		lvCdStatusNfe.setVisible(nfeVisible);
		lbDsNaturezaOperacao.setVisible(nfeVisible);
		lvDsNaturezaOperacao.setVisible(nfeVisible);
		lbVlChaveAcesso.setVisible(nfeVisible);
		lvVlChaveAcesso.setVisible(nfeVisible);
		lbVlSerieNfe.setVisible(nfeVisible);
		lvVlSerieNfe.setVisible(nfeVisible);
		lbnuLote.setVisible(nfeVisible);
		lvnuLote.setVisible(nfeVisible);
		lbDsObservacao.setVisible(nfeVisible);
		lvDsObservacao.setVisible(nfeVisible);
		lbNuNfe.setVisible(nfeVisible);
		lvNuNfe.setVisible(nfeVisible);
		lbVlTotalProdutosNfe.setVisible(nfeVisible);
		lbVlTotalNfe.setVisible(nfeVisible);
		lvVlTotalProdutosNfe.setVisible(nfeVisible);
		lvVlTotalNfe.setVisible(nfeVisible);
		lbDtSaida.setVisible(nfeVisible);
		lvDtSaida.setVisible(nfeVisible);
		lbHrSaida.setVisible(nfeVisible);
		lvHrSaida.setVisible(nfeVisible);
		lbDtEmissaoNfe.setVisible(nfeVisible);
		lvDtEmissaoNfe.setVisible(nfeVisible);
		btListaItensNfe.setVisible(isEditing() && !isEnabled() && nfeVisible);
	}

	//@Override
	protected void refreshComponents() throws SQLException {
		setEnabled(isEnabled());
		Pedido pedido = getPedido();
		if (pedido.isPedidoConsignado()) {
			Control controlObs = (Control) hashComponentes.get(Pedido.NMCOLUNA_DSOBSERVACAO);
			controlObs.setEnabled(true);
		}
		edDsCondicaoPagamentoSemCadastro.setEditable(isEnabled());
		edNuOrdemCompraCliente1.setEditable(isEnabled());
		edVlManualFrete.setEditable(isEnabled());

		edNuOrdemCompraCliente2.setEditable(isEnabled());
		cbClienteSetorOrigem.setEnabled(isEnabled() && ValueUtil.isEmpty(pedido.itemPedidoList)
				&& !cbClienteSetorOrigem.getValue().equals(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO)
				&& (cbClienteSetorOrigem.defaultItemType != BaseComboBox.DefaultItemType_NONE_ITEN));
		cbClienteSetor.setEnabled(isEnabled()
				&& !(cbClienteSetor.getValue().equals(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO)
						|| cbClienteSetor.getItemAt(0) == null)
				&& ((cbClienteSetor.defaultItemType != BaseComboBox.DefaultItemType_Empty)
						&& (cbClienteSetor.defaultItemType != BaseComboBox.DefaultItemType_NONE_ITEN)));
		// --
		edRotaEntrega1.setEditable(isEnabled());
		edRotaEntrega2.setEditable(isEnabled());
		cbRotaEntrega.setEditable(isEnabled() && !LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente());
		edDtEntrega.setEditable(isEnabled() && (!LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()
				|| !PedidoService.getInstance().isPedidoRelacionadoCarga(pedido.cdCargaPedido)));
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			edDtSugestaoCliente.setEditable(isEnabled());
			edDtEntrega.setEditable(false);
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			edDtCarregamento.setEditable(isEnabled());
		}
		cbTipoEntrega.setEditable(isEnabled() && LavenderePdaConfig.usaTipoDeEntregaNoPedido);
		// --
		edVlPctDesconto.setEditable(((LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0)) && isEnabled() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL));
		edVlDesconto.setEditable(LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() && isEnabled() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL));
		edPctDesconto.setEditable(edVlDesconto.isEditable());
		edVlPctDescEspecial.setEditable(false);
		edVlPctDescItem.setEditable(LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && isEnabled() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL));
		edVlPctAcrescimoItem.setEditable(LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && isEnabled() &&  !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(), Cliente.ACRESCIMO_BLOQUEADO_MANUAL));
		// --
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco
				|| LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
			if (LavenderePdaConfig.isMostraFlexPositivoPedido(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
				edVlVerbaPedidoPositiva.setEnabled(false);
			}
		}
		edValorMinTipoPedido.setEnabled(false);
		edVlMinTabelaPreco.setEnabled(true);
		edVlMinTabelaPreco.setEditable(false);
		edValorMinCondPagto.setEnabled(true);
		edValorMinCondPagto.setEditable(false);
		edPrazoMedio.setEnabled(true);
		edPrazoMedio.setEditable(false);
		// --
		cbTipoFrete.setEnabled(isEnabled() && !PedidoUiUtil.isUsaCondicaoPgtoTipoFretePedidoRelacionado(pedido) && !LavenderePdaConfig.usaCalculoFretePersonalizado());
		// --
		cbCondicaoPagamento.setEnabled(isEnabled());
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido && pedido.getCliente().isClienteContratoEspecial()) {
			boolean cbCondicaoPagamentoIsNotEmpty = ValueUtil.isNotEmpty(cbCondicaoPagamento.getValue());
			boolean clientePossuiCondPgtoDefault = cbCondicaoPagamentoIsNotEmpty && cbCondicaoPagamento.getValue().equals(pedido.getCliente().cdCondicaoPagamento);
			boolean clientePossuiContrato = ValueUtil.isNotEmpty(pedido.getCliente().cdContratoEspecial) && cbCondicaoPagamentoIsNotEmpty && cbCondicaoPagamento.getValue().equals(cbClienteSetorOrigem.getCondicaoPagamento());
			cbCondicaoPagamento.setEditable((isEnabled() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento()) && !clientePossuiContrato && !clientePossuiCondPgtoDefault);
		}
		setEnableCombosPrincipais();
		// --
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
			setColorsOnIndicesRentabilidade();
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			setColorsOnIndicesRentabilidadeSemTributos();
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente
				|| LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.isUsaTipoFretePedido()) {
			if (pedido.vlFrete == 0 || !pedido.isPedidoAberto()) {
				edVlPctFreteRep.setEnabled(false);
			} else {
				edVlPctFreteRep.setEnabled(true);
			}
			edVlFreteRep.setEnabled(false);
			edVlFreteCli.setEnabled(false);
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			cbCentroCusto.setEditable(isEnabled());
			cbItemConta.setEditable(isEnabled());
			cbClasseValor.setEditable(isEnabled());
			if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
				cbPlataformaVenda.setEditable(isEnabled());
			}
			cbModoFaturamento.setEditable(isEnabled());
			emObservacaoModoFaturamento.setEnabled(isEnabled() && (pedido.isPedidoAberto() && cbModoFaturamento.getValue() != null));
			emObservacaoModoFaturamento.setEditable(emObservacaoModoFaturamento.isEnabled());
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			boolean	usaInfoAdicional = pedido.getTipoFrete() != null && pedido.getTipoFrete().isUsaInfoAdicional();
			bgTaxaEntrega.setEnabled(isEnabled() && usaInfoAdicional);
			bgAjudante.setEnabled(isEnabled() && usaInfoAdicional);
			edVlTaxaEntrega.setEnabled(isEnabled() && ValueUtil.VALOR_SIM.equals(bgTaxaEntrega.getValue()));
			edQtAjudante.setEnabled(isEnabled() && ValueUtil.VALOR_SIM.equals(bgAjudante.getValue()));
			bgAntecipaEntrega.setEnabled(isEnabled() && usaInfoAdicional);
			bgAgendamento.setEnabled(isEnabled() && usaInfoAdicional);
			boolean usaTipoFreteFob = pedido.getTipoFrete() != null && pedido.getTipoFrete().isTipoFreteFob();
			cbTipoVeiculo.setEditable(LavenderePdaConfig.obrigaInfoAdicionalFreteNoPedido && isEnabled() && usaInfoAdicional && usaTipoFreteFob);
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0)
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			atualizaIconeRentabilidade(btIconeRentabilidade);
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			edNuKmInicial.setEnabled(isEnabled());
			edNuKmFinal.setEnabled(isEnabled());
			edHrInicialIndicado.setEnabled(isEnabled());
			edHrFinalIndicado.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			cbEnderecoEntrega.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			cbEnderecoCobranca.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			setEnableBgGeraCreditoBonificacaoCondicao();
			lvGeraCreditoBonificacaoCondicao.setVisible(pedido.isGeraCreditoCondicao());
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			setEnableBgGeraCreditoBonificacaoFrete();
			lvGeraCreditoBonificacaoFrete.setVisible(pedido.isGeraCreditoFrete());
		}
		if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			lvValorCreditoDisponivel.setVisible(isShowValorCreditoDisponivel());
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
			edVlPctDescFrete.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			edVlPctDescCondicao.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && pedido.getTabelaPreco() != null) {
			edVlPctDescCondicao.setEditable(isEnabled() && !pedido.getTabelaPreco().isIgnoraIndiceCondTipoPagto());
			edVlPctDescHistoricoVendas.setEditable(isEnabled() && !pedido.getTabelaPreco().isBloqueiaDesc2());
		}
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			Cliente cliente = pedido.getCliente();
			edDescontoCascataManualDescCliente
					.setEditable(isEnabled() && (!(cliente.vlIndiceFinanceiro > 0 && cliente.vlIndiceFinanceiro < 1)
							|| LavenderePdaConfig.permiteEditarPrimeiroDescontoEmCascataManualNaCapaDoPedido));
			edDescontoCascataManual2.setEditable(isEnabled());
			edDescontoCascataManual3.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			Categoria categoria = pedido.getCliente().getCategoria();
			TabelaPreco tabPreco = pedido.getTabelaPreco();
			boolean enabled = tabPreco == null ? isEnabled() : tabPreco.isPermiteDesconto() && isEnabled();
			edDescCascataCategoria1.setEditable(enabled && pedido.getCliente().vlIndiceFinanceiro < 1);
			refreshCamposDescCascata(pedido, categoria, enabled);
		}
		if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) {
			ButtonGroupBoolean control = getButtonGroupFlEnviaEmail();
			flEnviaEmailValueChange(control != null ? control.getValueBoolean() && isEnabled() : false);
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			cbContatoErp.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.isEnviarEmailPedidoAutoCliente() && isEnabled()) {
			habilitaDesabilitaCamposEnvioEmail();
		}
		popUpSearchCliEntregaChange();
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			boolean enabled = isEnabled()
					&& ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados);
			cbBoletoConfig.setEditable(enabled);
			edNuAgencia.setEditable(enabled);
			edNuConta.setEditable(enabled);
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.setEnabled(isEnabled() && pedido.itemPedidoList.size() == 0);
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			TipoFrete tipoFrete = pedido.getTipoFrete();
			edCnpjTransportadora.setEditable(isEnabled() && tipoFrete != null && tipoFrete.isTipoFreteFob());
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			boolean editable = (isEnabled() && pedido.isPedidoAberto()) || cbStatusOrcamentoEditableBySolAutorizacao(pedido);
			editable &= !LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada || (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && (pedido.statusOrcamento == null || !pedido.statusOrcamento.isStatusPreOrcamento()));
			cbStatusOrcamento.setEditable(editable);
			emObservacaoOrcamento.setEditable(editable);
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			edDtPagamento.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			edDtEntregaManual.setEnabled(isEnabled());
		}
		refreshComponentsPedidoComplementado();
		bgAguardarPedidoComplementar.setEnabled(isEnabled());
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			bgPedidoCritico.setEnabled(isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList));
		}
		if (edVlPctVpc != null) {
			edVlPctVpc.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			atualizaIconeComissaoPedido(btIconeComissaoPedido);
		}
		if (!LavenderePdaConfig.isNuParcelasNoPedido()) {
			edNuParcelaPedido.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			bgPedidoGondola.setEnabled(isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			boolean cbTipoFreteEnabled = isEnabled() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList);
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				boolean hasSolAutorizacaoPendenteOuAutorizadaByPedido = pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, null);
				cbTipoFreteEnabled &= !hasSolAutorizacaoPendenteOuAutorizadaByPedido;
			}
			cbTipoFrete.setEditable(cbTipoFreteEnabled);
		} else if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			boolean hasSolAutorizacaoPendenteOuAutorizadaByPedido = pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, null);
			if (isEnabled()) {
				cbTipoFrete.setEditable(!hasSolAutorizacaoPendenteOuAutorizadaByPedido);
			} else {
				cbTipoFrete.setEditable(false);
			}
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			atualizaIconePesoFaixa(btIconeFaixa);
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			bgUsaCodigoInternoCliente.setEnabled(pedido.isPedidoAberto());
		}
		if (LavenderePdaConfig.usaMarcadorPedido) {
			clearMarcadores();
			adicionaMarcadoresTela();
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			edVlFreteAdicional.setEditable(isEnabled());
		}
		if (LavenderePdaConfig.usaImagemQrCode) {
			btQrCodePix.setEnabled(ValueUtil.isNotEmpty(getPedido().dsQrCodePix));;
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			bgUtilizaRentabilidade.setEnabled(isEnabled());
		}
		setBtLibParcelaSolAutorizacaoVisible(pedido);
	}

	private boolean cbStatusOrcamentoEditableBySolAutorizacao(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()
				|| pedido.isPedidoFechado()
				|| pedido.isPedidoTransmitido()
				|| pedido.statusOrcamento == null) return false;
		return SolAutorizacaoService.getInstance().hasSolAutorizacaoAutorizadaPedido(pedido, TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX);
	}

	private void refreshCamposDescCascata(Pedido pedido, Categoria categoria, boolean enabled) throws SQLException {
		boolean edCat2Enabled = enabled, edCat3Enabled = enabled;
		if (categoria != null && !categoria.equals(new Categoria())) {
			edCat2Enabled = edCat2Enabled && (categoria.isCategoriaAtacado() || categoria.isCategoriaEspecial() || pedido.vlTotalPedido >= categoria.vlMinPedidoEspecial || pedido.getCondicaoPagamento().isEspecial());
			edCat3Enabled = edCat3Enabled && (categoria.isCategoriaAtacado() || (pedido.vlTotalPedido >= categoria.vlMinPedidoAtacado && categoria.isCategoriaEspecial()) || (categoria.isCategoriaEspecial() && pedido.getCondicaoPagamento().isEspecial()));
		} else {
			edCat2Enabled = edCat2Enabled && pedido.getCondicaoPagamento().isEspecial();
			edCat3Enabled = false;
		}
		edDescCascataCategoria2.setEditable(edCat2Enabled);
		edDescCascataCategoria3.setEditable(edCat3Enabled);
		boolean updatePedido = false;
		if (pedido.isPedidoAberto() && !inOnlyConsultaItens) {
			if (!edDescCascataCategoria1.isEditable()) {
				updatePedido = true;
				edDescCascataCategoria1.setValue(0);
				pedido.vlPctDescCliente = 0;
			}
			if (!edDescCascataCategoria2.isEditable()) {
				updatePedido = true;
				edDescCascataCategoria2.setValue(0);
				pedido.vlPctDesc2 = 0;
			}
			if (!edDescCascataCategoria3.isEditable()) {
				updatePedido = true;
				edDescCascataCategoria3.setValue(0);
				pedido.vlPctDesc3 = 0;
			}
			if (updatePedido && pedido.vlTotalPedido > 0) {
				getPedidoService().update(pedido);
				updateVlTotalPedido();
			}
		}
	}

	private void refreshComponentsPedidoComplementado() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido != null && pedido.isPedidoComplementar() && ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
			Pedido pedidoComplementado = new Pedido();
			pedidoComplementado.cdEmpresa = pedido.cdEmpresa;
			pedidoComplementado.cdRepresentante = pedido.cdRepresentante;
			pedidoComplementado.nuPedido = pedido.nuPedidoComplementado;
			pedidoComplementado.flOrigemPedido = pedido.flOrigemPedido;
			Pedido pedidoComplementadoRetorno = (Pedido) getPedidoService().findByRowKey(pedidoComplementado.getRowKey());
			if (pedidoComplementadoRetorno == null) {
				pedidoComplementado.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_ERP;
				pedidoComplementadoRetorno = (Pedido) getPedidoService().findByRowKey(pedidoComplementado.getRowKey());
			}
			setPedidoComplementarComMesmaConfPedidoOriginal(pedido, pedidoComplementadoRetorno);
		}
	}

	private boolean isShowValorCreditoDisponivel() throws SQLException {
		Pedido pedido = getPedido();
		return pedido.isPedidoBonificacao() && pedido.getTipoPedido() != null && (pedido.getTipoPedido().isFlTipoCreditoCondicao() || pedido.getTipoPedido().isFlTipoCreditoFrete());
	}

	private void setPrecoLiberadoSenhaVisible() throws SQLException {
		lbPrecoLiberadoSenha.setVisible(LavenderePdaConfig.liberaComSenhaPrecoDeVenda && (getPedido().isFlPrecoLiberadoSenha()));
	}

	private void setFreteVisible() throws SQLException {
		//Frete
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco && LavenderePdaConfig.usaFreteApenasTipoFob) {
			Pedido pedido = getPedido();
			lbVlTotalFrete.setVisible(pedido.isTipoFreteFob());
			lvVlTotalFrete.setVisible(pedido.isTipoFreteFob());
		}
	}

	private void setEnabledFieldsInfoFrete(boolean enabled) throws SQLException {
		if (!enabled) {
			clearFieldsInfoFrete();
		}
		bgTaxaEntrega.setEnabled(enabled);
		edVlTaxaEntrega.setEnabled(enabled && ValueUtil.VALOR_SIM.equals(bgTaxaEntrega.getValue()));
		bgAjudante.setEnabled(enabled);
		edQtAjudante.setEnabled(enabled && ValueUtil.VALOR_SIM.equals(bgAjudante.getValue()));
		bgAntecipaEntrega.setEnabled(enabled);
		bgAgendamento.setEnabled(enabled);
		cbTipoVeiculo.setEditable(enabled && LavenderePdaConfig.obrigaInfoAdicionalFreteNoPedido && getPedido().isTipoFreteFob());
		if (!cbTipoVeiculo.isEnabled()) {
			cbTipoVeiculo.setSelectedIndex(-1);
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			setEnableBgGeraCreditoBonificacaoFrete();
			bgGeraCreditoFreteValueChange();
		}
	}

	//-----------------------------------------------

	//@Override
	public void onFormShow() throws SQLException {
		onShowCadPedidoForm();
		setDtEntregaManualVisible(getPedido());
		setTributacaoVisibleState();
		if (getPedido().isPedidoAberto()) {
			PontoGpsService.getInstance().startColetaGpsPontoEspecificoSistema();
			if (LavenderePdaConfig.enviaInformacoesVisitaOnline && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				String cdSessao = PedidoService.getInstance().generateIdGlobal();
				int nuSequencia = SessionLavenderePda.visitaAndamento != null ? SessionLavenderePda.visitaAndamento.nuSequencia : 0;
				NotificacaoPdaService.getInstance().createNotificacaoPdaInicioPedidoAndSend2Web(cdSessao, nuSequencia);
			}
			if (LavenderePdaConfig.exibePopupFreteInicioPedido() && ValueUtil.isEmpty(getPedido().cdTransportadora)) {
				exibirPopupFrete(true);
			}
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			setEnabledPopupSearchCliEntrega();
		}
		if (LavenderePdaConfig.usaBackupAutomatico > 0) {
			try {
				LavendereBackupService.getInstance().realizaBackupPeriodico();
			} catch (Throwable e) {
				UiUtil.showErrorMessage(e);
			}
		}
		if (getPedido().nuPedido == null) {
			abreMetaVendaPorClienteWindow();
		}
		exibePopupObservacaoCliente(getPedido().getCliente());
		if (LavenderePdaConfig.isOrdenaTabelasPrecoPorPesoMinimo()) {
			cbTabelaPreco.enableSearchButtons = false;
		}
		avisaMotivoBloqueioPedidoPorSolAutorizacao();
		super.onFormShow();
	}

	private void avisaMotivoBloqueioPedidoPorSolAutorizacao() throws SQLException {
		if (!LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()) return;
		if (SolAutorizacaoService.getInstance().hasSolAutorizacaoPendentePedido(getPedido(), TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX)){
			UiUtil.showInfoMessage(Messages.SOL_AUTORIZACAO_PEDIDO_BLOQUEADO);
		} else if (isPedidoComAutorizacaoDeParcelamento(getPedido())){
			UiUtil.showInfoMessage(Messages.SOL_AUTORIZACAO_PEDIDO_BLOQUEADO_AUTORIZADO);
		}
	}

	private boolean abreMetaVendaPorClienteWindow() throws SQLException {
		if (LavenderePdaConfig.usaRelatorioMetaVendaCliente) {
			ListMetaVendaPorClienteWindow metaVendaPorClienteForm = new ListMetaVendaPorClienteWindow(getPedido().getCliente());
			if (!metaVendaPorClienteForm.listMetaVendaPorClienteForm.getDomainList(new MetaVendaCli()).isEmpty()) {
				metaVendaPorClienteForm.popup();
			} else {
				return false;
			}
		}
		return true;
	}

	private void setTributacaoVisibleState() throws SQLException {
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente()) {
			boolean pedidoSemCliente = PedidoUiUtil.isPedidoSemCliente(getPedido());
			lbTributacao.setVisible(pedidoSemCliente);
			cbTributacao.setVisible(pedidoSemCliente);
			remontaTela();
			reposition();
		}
	}

	private void onShowCadPedidoForm() throws SQLException {
		repaintTitleAndButtons();
		PedidoService.validationFechamentoCount = 0;
		checkCustomerCreditAndNotify();
		showMessageTrocaTabPreco(false);
		if (StatusPedidoPdaService.getInstance().isPossuiStatusConsignacao()) {
			setaBotoesNaPrimeiraPosicao();
		}
	}

	//@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		FotoProdutoThread.setToStop();
		Util.resetImages();
		repaintTitleAndButtons();
		Pedido pedido = getPedido();
		if (inItemRenegotiation && LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			inItemRenegotiation = !inItemRenegotiation;
			previsaoDescontoClick();
		}
		if (pedido.vlFrete == 0 || !pedido.isPedidoAberto()) {
			edVlPctFreteRep.setEnabled(false);
		}
		if (inItemNegotiationGiroProdutoPendente) {
			inItemNegotiationGiroProdutoPendente = false;
			ListGiroProdutoWindow listGiroProdutoWindow = new ListGiroProdutoWindow(null, this, pedido, true, false, true);
			if (listGiroProdutoWindow.hasGiroProduto) {
				listGiroProdutoWindow.popup();
				if (listGiroProdutoWindow.fecharPedido) {
					fecharPedido();
				}
			}
		}
		if (inItemNegotiationProdutosPendentes && LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedido() && !pedido.isOportunidade() && !pedido.isPedidoAbertoNaoEditavel()
				&& !pedido.getCliente().isClienteDefaultParaNovoPedido() && !pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
			try {
				inItemNegotiationProdutosPendentes = false;
				RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(pedido, true, true, true);
				relProdutosPendentesWindow.cadPedidoForm = this;
				if (relProdutosPendentesWindow.hasProdutosPendentes()) {
					relProdutosPendentesWindow.popup();
					if (relProdutosPendentesWindow.continuaFecharPedido) {
						pedido.ignoraValidacaoProdutosPendentes = true;
						fecharPedido();
						pedido.ignoraValidacaoProdutosPendentes = false;
					}
					if (relProdutosPendentesWindow.continua) {
						pedido.ignoraValidacaoProdutosPendentes = true;
					}
				} else {
					RelProdutosPendentesWindow.cleanInstance();
				}
			} catch (ValidationException ex) {
				UiUtil.showWarnMessage(ex.getMessage());
			}
		}
		if (inItemNegotiationSugestaoRentabilidadeIdeal && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0 && !pedido.isOportunidade() && !pedido.isPedidoBonificacao()) {
			inItemNegotiationSugestaoRentabilidadeIdeal = false;
			if (pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaIdeal(pedido.getRentabilidadeFaixaList())) {
				ListSugestaoItensRentabilidadeIdealWindow listSugestaoItensRentabilidadeIdealWindow = new ListSugestaoItensRentabilidadeIdealWindow(pedido, false, openPopUpItensRentabilidadeManual);
				listSugestaoItensRentabilidadeIdealWindow.cadPedidoForm = this;
				listSugestaoItensRentabilidadeIdealWindow.singleClickOn = isEditing() && isEnabled() && pedido.isPedidoAberto();
				listSugestaoItensRentabilidadeIdealWindow.popup();
			} else {
				UiUtil.showInfoMessage(Messages.PEDIDO_MSG_RENTABILIDADE_IDEAL);
			}
		}
		openPopUpItensRentabilidadeManual = false;
		if (inItemNegotiationProdutosRelacionados) {
			showListProdutosRelacionadosIfNecessary();
		}
		if (!(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
			if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
				changeEntregaPedido(pedido);
			}
			if (LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamento() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() && isEnabled()) {
				cbCondicaoPagamento.loadCondicoesPagamento(pedido);
				cbCondicaoPagamento.setValue(pedido.cdCondicaoPagamento);
				if (ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento) && ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
					pedido.cdCondicaoPagamento = null;
					pedido.setCondicaoPagamento(null);
					PedidoService.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_CDCONDICAOPAGAMENTO, null, Types.VARCHAR);
					UiUtil.showWarnMessage(Messages.CONDICAOPAGAMENTO_ERRO_ALTERACAO_PEDIDO);
				}
			}
		}
		if (LavenderePdaConfig.isUsaSugestaoComboFechamentoPedido() && mostraSugestaoItemComboOnExibition && salvouItemComboSugerido) {
			if (sugereItemComboAntesFechamento(pedido)) {
				fecharPedido();
			}
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			if (!LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoPedido(pedido)) {
				lvVlPctRentabilidade.setText(StringUtil.getStringValueSimple(pedido.vlPctMargemRentab, 2, true));
				lvVlPctRentabilidade.setVisible(true);
			} else {
				lvVlPctRentabilidade.setVisible(false);
			}
		}
		mostraSugestaoItemComboOnExibition = false;
		salvouItemComboSugerido = false;
		ItemKitService.getInstance().clearCache();
		GrupoCliPermProdService.getInstance().clearCache();
	}

	private void changeEntregaPedido(Pedido pedido) throws SQLException {
		cbEntrega.load(pedido);
		cbEntrega.setValue(pedido.cdEntrega);
		if (isEnabled() && ValueUtil.isNotEmpty(pedido.cdEntrega) && ValueUtil.isEmpty(cbEntrega.getValue())) {
			pedido.cdEntrega = null;
			PedidoService.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_CDENTREGA, null, Types.VARCHAR);
			UiUtil.showWarnMessage(Messages.ENTREGA_ALTERACAO_PEDIDO);
		}
	}

	private void showListProdutosRelacionadosIfNecessary() throws SQLException {
		Pedido pedido = getPedido();
		ProdutoRelacionadoService.getInstance().loadProdutosRelacionadosNaoContemplados(pedido);
		if (ValueUtil.isNotEmpty(pedido.prodRelacionadosNaoContempladosList)) {
			ListProdutoRelacionadoWindow listProdutoRelacionadoWindow = new ListProdutoRelacionadoWindow(Messages.PRODUTO_RELACIONADO_FECHAMENTO_PEDIDO, pedido, false);
			listProdutoRelacionadoWindow.cadPedidoForm = this;
			listProdutoRelacionadoWindow.popup();
		} else {
			inItemNegotiationProdutosRelacionados = false;
	    	CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(this, pedido);
	    	cadItemPedidoForm.fromProdutoRelacionadoWindow = false;
	    	cadItemPedidoForm.fromProdutoRelacionadoWindowOnFechamento = false;
		}

	}

	//@Override
	protected String getBtVoltarTitle() {
		if (isEditing() && isEnabled()) {
			try {
				if (getPedido().itemPedidoList.size() == 0) {
			return " " + FrameworkMessages.BOTAO_CANCELAR;
				} else {
					return " " + FrameworkMessages.BOTAO_VOLTAR;
		}
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		return getBtVoltarTitleOriginal();
	}

	private void checkCustomerCreditAndNotify() throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente && !LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir && pedido.isPedidoAberto() && !pedido.isIgnoraControleVerba()) {
			double vlSaldoCliente = VerbaClienteService.getInstance().getVlSaldo();
			if (vlSaldoCliente != 0) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_CLIENTE, new String[] {StringUtil.getStringValue(SessionLavenderePda.getCliente().nmRazaoSocial), StringUtil.getStringValueToInterface(vlSaldoCliente)}));
			}
		}
	}

	private boolean isExibeAbaInfoFrete() throws SQLException {
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			TipoFrete tipoFreteFilter = new TipoFrete();
			tipoFreteFilter.flUsaInfoAdicional = ValueUtil.VALOR_SIM;
			return TipoFreteService.getInstance().countByExample(tipoFreteFilter) != 0;
		}
		return false;
	}

    //@Override
	protected void addTabsFixas(Vector tableTitles) throws SQLException {
		String pedidoLabel = Messages.PEDIDO_NOME_ENTIDADE;
		int indexPedido = tableTitles.indexOf(pedidoLabel);
		if (indexPedido != -1) {
			tableTitles.removeElement(pedidoLabel);
		}
		tableTitles.insertElementAt(pedidoLabel, 0);
		TABPANEL_PEDIDO = 0;
		TABPANEL_TOTALIZADORES = 0;
		int posicaoFixa = 1;
		TABPANEL_DEBITO_BANCARIO = 0;
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			addTabInfoAdicional(tableTitles, posicaoFixa);
			posicaoFixa++;
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			int indexDebito = tableTitles.indexOf(Messages.DEBITOBANCARIO_LABEL);
			if (indexDebito == -1) {
				tableTitles.insertElementAt(Messages.DEBITOBANCARIO_LABEL, posicaoFixa);
				posicaoFixa++;
				TABPANEL_DEBITO_BANCARIO = tableTitles.indexOf(Messages.DEBITOBANCARIO_LABEL);
			} else {
				TABPANEL_DEBITO_BANCARIO = indexDebito;
			}
		}
		if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoCapaPedido()) {
    		int indexTotalizadores = tableTitles.indexOf(Messages.PEDIDO_LABEL_TOTALIZADORES);
    		if (indexTotalizadores == -1) {
    			tableTitles.insertElementAt(Messages.PEDIDO_LABEL_TOTALIZADORES, posicaoFixa);
    			posicaoFixa++;
    			TABPANEL_TOTALIZADORES = tableTitles.indexOf(Messages.PEDIDO_LABEL_TOTALIZADORES);
    		} else {
    			TABPANEL_TOTALIZADORES = indexTotalizadores;
    		}
    	}
		//--
		boolean entregaPanelVisible = LavenderePdaConfig.usaTipoDeEntregaNoPedido ||
						(!LavenderePdaConfig.previsaoEntregaOcultaNoPedido && !isExibeAbaInfoFrete() && !LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido) ||
						 LavenderePdaConfig.isUsaRotaDeEntregaNoPedido() ||
						 LavenderePdaConfig.usaIndicacaoClienteEntregaPedido ||
						(LavenderePdaConfig.usaTransportadoraPedido() && !LavenderePdaConfig.usaRateioFreteRepresentanteCliente && !LavenderePdaConfig.usaCalculoFretePersonalizado()) ||
						(LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0 && !LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido);
		if (entregaPanelVisible) {
			String entregaLabel = Messages.ENTREGA_LABEL;
			int indexEntrega = tableTitles.indexOf(entregaLabel);
			if (indexEntrega == -1) {
				tableTitles.insertElementAt(entregaLabel, posicaoFixa);
				TABPANEL_ENTREGA = tableTitles.indexOf(entregaLabel);
			} else {
				TABPANEL_ENTREGA = indexEntrega;
			}
		}
		TABPANEL_PAGAMENTOS = 0;
		if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
			String pagtoLabel = Messages.PAGAMENTOPEDIDO_TITULO_CADASTRO;
			int indexPagto = tableTitles.indexOf(pagtoLabel);
			if (indexPagto == -1) {
				tableTitles.addElement(pagtoLabel);
				TABPANEL_PAGAMENTOS = tableTitles.size() - 1;
			} else {
				TABPANEL_PAGAMENTOS = indexPagto;
			}
		}
    	//--
		String detalhesLabel = Messages.PEDIDO_LABEL_DETALHES;
		int indexDetalhes = tableTitles.indexOf(detalhesLabel);
    	if (indexDetalhes == -1) {
    		tableTitles.addElement(detalhesLabel);
    		TABPANEL_DETALHES = tableTitles.size() - 1;
    	} else {
    		TABPANEL_DETALHES = indexDetalhes;
    	}
    	TABPANEL_NOTAS_FISCAIS = 0;
		if (LavenderePdaConfig.exibirNotasFiscaisPedido && !LavenderePdaConfig.ocultaNotaFiscalPedidoNaoRetornado) {
			int indexNotaFiscal = tableTitles.indexOf(Messages.NOTA_FISCAL);
			if (indexNotaFiscal == -1) {
				tableTitles.addElement(Messages.NOTA_FISCAL);
				TABPANEL_NOTAS_FISCAIS = tableTitles.indexOf(Messages.NOTA_FISCAL);
			} else {
				TABPANEL_NOTAS_FISCAIS = indexNotaFiscal;
			}
		}
    	TABPANEL_PAGTO = 0;
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			String pagtoLabel = Messages.PEDIDO_LABEL_PAGTO;
			int indexPagto = tableTitles.indexOf(pagtoLabel);
			if (indexPagto == -1) {
				tableTitles.addElement(pagtoLabel);
				TABPANEL_PAGTO = tableTitles.size() - 1;
			} else {
				TABPANEL_PAGTO = indexPagto;
			}
		}
		TABPANEL_FRETE = 0;
		if (LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido()  ||
				(LavenderePdaConfig.usaRateioFreteRepresentanteCliente && LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd) ||
				LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			String freteLabel = Messages.FRETE_LABEL;
			int indexFrete = tableTitles.indexOf(freteLabel);
			if (indexFrete == -1) {
				tableTitles.addElement(freteLabel);
				TABPANEL_FRETE = tableTitles.size() - 1;
			} else {
				TABPANEL_FRETE = indexFrete;
			}
		}
		if (!LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && isTabInfoComplementarLigada()) {
			addTabInfoAdicional(tableTitles, tableTitles.size()-1);
		}
		TABPANEL_INFO_FRETE_PEDIDO = 0;
		if (isExibeAbaInfoFrete()) {
			String infoFretePedidoLabel = Messages.INFO_FRETE_LABEL;
			int indexInfoFretePedido = tableTitles.indexOf(infoFretePedidoLabel);
			if (indexInfoFretePedido == -1) {
				tableTitles.addElement(infoFretePedidoLabel);
				TABPANEL_INFO_FRETE_PEDIDO = tableTitles.size() - 1;
			} else {
				TABPANEL_INFO_FRETE_PEDIDO = indexInfoFretePedido;
			}
		}
		TABPANEL_NFE = 0;
		if (LavenderePdaConfig.mostraAbaNfeNoPedido) {
			String nfeLabel = Messages.NFE_LABEL;
			int indexNfe = tableTitles.indexOf(nfeLabel);
			if (indexNfe == -1) {
				tableTitles.addElement(nfeLabel);
				TABPANEL_NFE = tableTitles.size() - 1;
			} else {
				TABPANEL_NFE = indexNfe;
			}
		}
		TABPANEL_BOLETO = 0;
		if (LavenderePdaConfig.mostraAbaBoletoNoPedido) {
			int indexNfe = tableTitles.indexOf(Messages.PEDIDOBOLETO_LABEL);
			if (indexNfe == -1) {
				tableTitles.addElement(Messages.PEDIDOBOLETO_LABEL);
				TABPANEL_BOLETO = tableTitles.size() - 1;
			} else {
				TABPANEL_BOLETO = indexNfe;
			}
		}
		TABPANEL_NFCE = 0;
		if (LavenderePdaConfig.mostraAbaNfceNoPedido) {
			String nfceLabel = Messages.NFCE_LABEL;
			int indexNfce = tableTitles.indexOf(nfceLabel);
			if (indexNfce == -1) {
				tableTitles.addElement(nfceLabel);
				TABPANEL_NFCE = tableTitles.size() - 1;
			} else {
				TABPANEL_NFCE = indexNfce;
			}
		}
		TABPANEL_ATIVIDADES = 0;
		if (LavenderePdaConfig.usaRegistroAtividadeRelacionadaPedido) {
			int indexAtividade = tableTitles.indexOf(Messages.ATIVIDADEPEDIDO_LABEL);
			if (indexAtividade == -1) {
				tableTitles.addElement(Messages.ATIVIDADEPEDIDO_LABEL);
				TABPANEL_ATIVIDADES = tableTitles.size() - 1;
			} else {
				TABPANEL_ATIVIDADES = indexAtividade;
    }
		}
    }

	private void addTabInfoAdicional(Vector tableTitles, int index) {
		String infoAdicionalLabel = Messages.INFO_ADICIONAL_LABEL;
		int indexInfoAdicional = tableTitles.indexOf(infoAdicionalLabel);
		if (indexInfoAdicional == -1) {
			tableTitles.insertElementAt(infoAdicionalLabel, index);
			TABPANEL_INFO_ADICIONAL = tableTitles.indexOf(infoAdicionalLabel);
		} else {
			TABPANEL_INFO_ADICIONAL = indexInfoAdicional;
		}
	}

	protected void addComponentesFixosInicio() throws SQLException {
		TipoPedido tipoPedido = getPedido().getTipoPedido();
    	// PEDIDO
		Container tabPanel = tabDinamica.getContainer(TABPANEL_PEDIDO);
		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.EMPRESA_NOME_ENTIDADE), lbDsNmEmpresaCapa, getLeft(), TOP);
			UiUtil.add(tabPanel, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lbDsCliente, getLeft(), AFTER + HEIGHT_GAP);
		} else {
			UiUtil.add(tabPanel, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lbDsCliente, getLeft(), TOP);
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			UiUtil.add(tabPanel, lbPedidoCritico, bgPedidoCritico, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_PEDIDO_GONDOLA), bgPedidoGondola, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaApresentacaoFixaTicketMedioCapaPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.CLIENTE_TICKET_MEDIO), lvVlTicketMedio, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_STATUSALVARA), lvDsStatusAlvara, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.mostraSaldoContaCorrenteCliente) {
			UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_VLSALDOCONTACORRENTE), edVlSaldoCCCliente, getLeft(), AFTER + HEIGHT_GAP);
		}

		// Dt Pagamento
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DT_PAGAMENTO), edDtPagamento, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Pagamentos
		if (TABPANEL_PAGAMENTOS != 0) {
			Pedido pedido = getPedido();
			listPagamentoPedidoForm = new ListPagamentoPedidoForm(pedido);
			listPagamentoPedidoForm.setCadPedidoForm(this);
			tabPanel = tabDinamica.getContainer(TABPANEL_PAGAMENTOS);
			UiUtil.add(tabPanel, new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLPPEDIDO), lvVlPedido, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLEMABERTO), lvVlPedidoAberto, RIGHT - WIDTH_GAP_BIG, BEFORE, PREFERRED, PREFERRED);
			if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDO_TROCA), lbVlTotalPedidoTroca, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			}
			UiUtil.add(tabPanel, listPagamentoPedidoForm, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
			if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
				lvVlPedido.setValue(pedido.vlFinalPedidoDescTribFrete);
				atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlFinalPedidoDescTribFrete);
			} else {
				lvVlPedido.setValue(pedido.vlTotalPedido);
				atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlTotalPedido);
			}
			lvVlPedidoAberto.setValue(0d);
		}
		// TOTALIZADORES
		if (TABPANEL_TOTALIZADORES != 0) {
			tabDinamica.setContainer(TABPANEL_TOTALIZADORES, totalizadoresPedidoForm);
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			UiUtil.add(tabPanel, new LabelName(Messages.DIVISAO_VENDA_LABEL_COMBO), cbDivisaoVenda, getLeft(), AFTER + HEIGHT_GAP);
		}
		// ENTREGA
		if (TABPANEL_ENTREGA != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_ENTREGA);
			//Tipo de Entrega
			if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.TIPOENTREGA_LABEL_TIPOENTREGA), cbTipoEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
			//Previsão de Entrega
			if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido && (!LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido || LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) && TABPANEL_INFO_FRETE_PEDIDO == 0) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTENTREGA), edDtEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.usaDataCarregamentoPedido && TABPANEL_INFO_FRETE_PEDIDO == 0) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTCARREGAMENTO), edDtCarregamento, getLeft(), AFTER + HEIGHT_GAP);
			}
			//Transportadora
			if (LavenderePdaConfig.usaTransportadoraPedido() && !LavenderePdaConfig.usaRateioFreteRepresentanteCliente && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
				UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORA), cbTransportadora, getLeft(), AFTER + HEIGHT_GAP);
				if (LavenderePdaConfig.usaTransportadoraAuxiliar) {
					UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORA_AUXILIAR), cbTransportadoraAux, getLeft(), AFTER + HEIGHT_GAP);
				}
			}
			if (LavenderePdaConfig.isUsaTransportadoraAuxiliar()) {
				if (LavenderePdaConfig.isUsaTipoFretePedido()) {
					if (tipoPedido == null || (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete())) {
					UiUtil.add(tabPanel, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete, getLeft(), AFTER + HEIGHT_GAP);
					}
					if (LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
						UiUtil.add(tabPanel, new LabelName(Messages.TIPOFRETE_LABEL_CNPJ), edCnpjTransportadora, getLeft(), + AFTER + HEIGHT_GAP);
					}
				}
				if (LavenderePdaConfig.isPermiteInserirFreteManual()) {
					UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_VLFRETE), edVlManualFrete, getLeft(), AFTER + HEIGHT_GAP);
				}
			}
			//Frete
			if (((LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd && !LavenderePdaConfig.usaRateioFreteRepresentanteCliente) || usaInfoFreteAbaEntrega()) && !LavenderePdaConfig.usaCalculoFretePersonalizado()) {
				//Valor total do frete
				if (!LavenderePdaConfig.ocultaValorFretePedido && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
					UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VALOR_FRETE), lbVlTotalFretePedido, getLeft(), AFTER + HEIGHT_GAP);
				}
				//Valor total do pedido
				if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
					UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO), lbVlTotalPedidoAbaFrete, getLeft(), AFTER + HEIGHT_GAP);
				}
				//Valor total do pedido com o frete
				if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
					UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO_COM_FRETE), lbVlTotalPedidoComFrete, getLeft(), AFTER + HEIGHT_GAP);
				}
			}
			//Rota Entrega - Sem Cadastro
			String labelDsRotaEntrega = Messages.PEDIDO_LABEL_DSROTAENTREGA;
			if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoSemCadastroLigado()) {
				UiUtil.add(tabPanel, new LabelName(labelDsRotaEntrega), getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
				if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 1) {
					UiUtil.add(tabPanel, edRotaEntrega1, getLeft(), AFTER);
				} else if (LavenderePdaConfig.usaRotaDeEntregaNoPedidoSemCadastro == 2) {
					UiUtil.add(tabPanel, edRotaEntrega2, getLeft(), AFTER);
				}
			}
			//Rota Entrega - Com Cadastro
			if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedidoComCadastroLigado()) {
				UiUtil.add(tabPanel, new LabelName(labelDsRotaEntrega), cbRotaEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (!LavenderePdaConfig.usaIndicacaoClienteEntregaPedido && LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0 && !LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido) {
				UiUtil.add(tabPanel, cbEnderecoEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
		}

		//FRETE
		montaTabPanelFrete();
		if (TABPANEL_PAGTO != 0) {
			tabDinamica.setContainer(TABPANEL_PAGTO, listParcelaPedidoForm);
		}
		if (TABPANEL_INFO_FRETE_PEDIDO != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_INFO_FRETE_PEDIDO);
			UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO), lbVlTotalPedidoAbaFrete, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			if(!LavenderePdaConfig.ocultaValorFretePedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VALOR_FRETE), lbVlTotalFretePedido, RIGHT - WIDTH_GAP_BIG, BEFORE, PREFERRED, PREFERRED);
			}
			if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
				UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO_COM_FRETE), lbVlTotalPedidoComFrete, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			}
			if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTENTREGA), edDtEntrega, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			}
			if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_ENDERECO_ENTREGA), cbEnderecoEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.usaDataCarregamentoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTCARREGAMENTO), edDtCarregamento, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.obrigaInfoAdicionalFreteNoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.TIPOVEICULO_LABEL_TIPOVEICULO), cbTipoVeiculo, getLeft(), AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabPanel, lbPossuiTaxaEntrega, bgTaxaEntrega, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbTaxaEntrega, edVlTaxaEntrega, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbPossuiAjudante, bgAjudante, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbAjudante, edQtAjudante, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbAntecipaEntrega, bgAntecipaEntrega, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbPrecisaAgendamento, bgAgendamento, getLeft(), AFTER + HEIGHT_GAP);
		}
		// DETALHES
		tabPanel = tabDinamica.getContainer(TABPANEL_DETALHES);
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			Representante representante = SessionLavenderePda.getRepresentante();
			representante = ValueUtil.isEmpty(representante.cdRepresentante) ? RepresentanteService.getInstance().getRepresentanteById(SessionLavenderePda.usuarioPdaRep.cdRepresentante) : representante;
			UiUtil.add(tabPanel, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(representante.toString()), getLeft(), TOP);
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO), lbDsStatusPedido, getLeft(), AFTER + HEIGHT_GAP);
		} else {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO), lbDsStatusPedido, getLeft(), TOP);
		}
		UiUtil.add(tabPanel, new LabelName(Messages.EMPRESA_NOME_ENTIDADE), lbDsNmEmpresa, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_NUPEDIDO), lbNuPedidoRelacionado, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_NUPEDIDOPDA), lbNuPedido, getLeft(), AFTER + HEIGHT_GAP);
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_NUPEDIDO_ORIGINAL), lvNuPedidoOriginal, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_FLORIGEM), lbDsOrigemPedido, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), lbDtEmissao, getLeft(), AFTER + HEIGHT_GAP);
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente() && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			UiUtil.add(tabPanel, new LabelName(Messages.ESTOQUECORRENTE_LABEL_DTENTREGA), edDtEntrega, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaImagemQrCode) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_GERARQRCODE_PIX), btQrCodePix, getLeft(), getNextY());
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLBONIFICACAO), lbVlBonificacaoPedido, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_USUARIO_LIBERACAO_ITEM), lvUsuarioItemLiberacao, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (TABPANEL_NOTAS_FISCAIS != 0) {
			Container containerNotasFiscais = tabDinamica.getContainer(TABPANEL_NOTAS_FISCAIS);
			UiUtil.add(containerNotasFiscais, listNotaFiscalForm = new ListNotaFiscalForm(getPedido(), false), LEFT, TOP, FILL, FILL);
		}
		//NFE
		if (TABPANEL_NFE != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_NFE);
			UiUtil.add(tabPanel, lbHasNfe, CENTER + WIDTH_GAP_BIG, CENTER);
			UiUtil.add(tabPanel, lbCdStatusNfe, lvCdStatusNfe, getLeft(), TOP + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDsNaturezaOperacao, lvDsNaturezaOperacao, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlChaveAcesso, lvVlChaveAcesso, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbNuNfe, lvNuNfe, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlSerieNfe, lvVlSerieNfe, getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.isUsaCampoTotalProdutosNotaImpressaoLayoutNfe()) {
				UiUtil.add(tabPanel, lbVlTotalProdutosNfe, lvVlTotalProdutosNfe, getLeft(), AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabPanel, lbVlTotalNfe, lvVlTotalNfe, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDtEmissaoNfe, lvDtEmissaoNfe, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDtSaida, lvDtSaida, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbHrSaida, lvHrSaida, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbnuLote, lvnuLote, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDsObservacao, lvDsObservacao, getLeft(), AFTER + HEIGHT_GAP);
		}
		//BOLETO BANCARIO
		if (TABPANEL_BOLETO != 0) {
			Container containerTabBoleto = tabDinamica.getContainer(TABPANEL_BOLETO);
			UiUtil.add(containerTabBoleto, listGridBoleto, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
			UiUtil.add(containerTabBoleto, lbHasBoleto, CENTER + WIDTH_GAP_BIG, CENTER);
		}

		if (TABPANEL_NFCE != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_NFCE);
			UiUtil.add(tabPanel, lbHasNfce, CENTER + WIDTH_GAP_BIG, CENTER);
			UiUtil.add(tabPanel, lbNuNfce, lvNuNfce, getLeft(), TOP + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbNuSerie, lvNuSerie, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDtEmissaoNfce, lvDtEmissaoNfce, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbHrEmissaoNfce, lvHrEmissaoNfce, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDtAutorizacao, lvDtAutorizacao, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbHrAutorizacao, lvHrAutorizacao, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbQtTotalItem, lvQtTotalItem, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTotalNfce, lvVlTotalNfce, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbDsFormaPagamento, lvDsFormaPagamento, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTotalPago, lvVlTotalPago, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTroco, lvVlTroco, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTotalDesconto, lvVlTotalDesconto, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTotalLiquidoNfce, lvVlTotalLiquidoNfce, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlTotalTributos, lvVlTotalTributos, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlPctTributosFederais, lvVlPctTributosFederais, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlPctTributosEstaduais, lvVlPctTributosEstaduais, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbVlPctTributosMunicipais, lvVlPctTributosMunicipais, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbNuChaveAcesso, lvNuChaveAcesso, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbNuProtocoloAutorizacao, lvNuProtocoloAutorizacao, getLeft(), AFTER + HEIGHT_GAP);
		}
		// ATIVIDADES PEDIDO
		if (TABPANEL_ATIVIDADES != 0) {
			UiUtil.add(tabDinamica.getContainer(TABPANEL_ATIVIDADES), listGridAtividadePedido, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		}

		// INFORMAÇÕES ADICIONAIS
		if (TABPANEL_INFO_ADICIONAL != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_INFO_ADICIONAL);
			if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
				UiUtil.add(tabPanel, new LabelName(Messages.CENTROCUSTO_LABEL_DESCRICAO), cbCentroCusto, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PLATAFORMAVENDA_LABEL_DESCRICAO), cbPlataformaVenda, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isUsaItemContaInformacoesAdicionais()) {
				UiUtil.add(tabPanel, new LabelName(Messages.ITEMCONTA_LABEL_DESCRICAO), cbItemConta, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isUsaClasseValorInformacoesAdicionais()) {
				UiUtil.add(tabPanel, new LabelName(Messages.CLASSEVALOR_LABEL_DESCRICAO), cbClasseValor, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.NUKMINICIAL_LABEL_DESCRICAO), edNuKmInicial, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, new LabelName(Messages.NUKMFINAL_LABEL_DESCRICAO), edNuKmFinal, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, new LabelName(Messages.HRINICIALINDICADO_LABEL_DESCRICAO), edHrInicialIndicado, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, new LabelName(Messages.HRFINALINDICADO_LABEL_DESCRICAO), edHrFinalIndicado, getLeft(), AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
				UiUtil.add(tabPanel, new LabelName(Messages.MODOFATURAMENTO_LABEL_DESCRICAO), cbModoFaturamento, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, new LabelName(Messages.MODOFATURAMENTO_LABEL_OBSERVACAO), emObservacaoModoFaturamento, getLeft(), AFTER + HEIGHT_GAP);
			}
			//Sugestão de data de entrega do cliente
			if (LavenderePdaConfig.isUsaReservaEstoqueCorrente() && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_DATA_SUGESTAO_CLIENTE), edDtSugestaoCliente, getLeft(), AFTER + HEIGHT_GAP);
		}
		}
		// DÉBITO BANCÁRIO
		if (TABPANEL_DEBITO_BANCARIO != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_DEBITO_BANCARIO);
			UiUtil.add(tabPanel, new LabelName(Messages.DEBITOBANCARIO_LABEL_BANCO), cbBoletoConfig, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_AGENCIA), edNuAgencia, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_CONTA), edNuConta, getLeft(), AFTER + HEIGHT_GAP);
		}
    }

	private void montaTabPanelFrete() throws SQLException {
		if (TABPANEL_FRETE != 0) {
			if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
				adicionaComponentesTabFreteCalcFretePersonalizado();
			} else {
				adicionaComponentesTabFreteComum();
			}
		}
	}

	private void adicionaComponentesTabFreteCalcFretePersonalizado() {
		Container tabPanel;
		tabPanel = tabDinamica.getContainer(TABPANEL_FRETE);
		int top = TOP;
		UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORA), lbNmTransportadora, getLeft(), top);
		top = AFTER + HEIGHT_GAP;
		UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_CDTIPOFRETE), lbNmTipoFrete, getLeft(), top);
		top = AFTER + HEIGHT_GAP;
		UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VALOR_FRETE), lbVlTotalFretePedido, getLeft(), top);
		top = AFTER + HEIGHT_GAP;
		UiUtil.add(tabPanel, btAlteraTransportadora, getLeft(), AFTER + HEIGHT_GAP);
	}

	private void adicionaComponentesTabFreteComum() throws SQLException {
		Container tabPanel;
		tabPanel = tabDinamica.getContainer(TABPANEL_FRETE);
		int top = TOP;
		if ((LavenderePdaConfig.usaRateioFreteRepresentanteCliente && LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd)) {
			if (LavenderePdaConfig.usaTransportadoraPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORA), cbTransportadora, getLeft(), top);
				top = AFTER;
			}
			if (!LavenderePdaConfig.ocultaValorFretePedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VALOR_FRETE), lbVlTotalFretePedido, getLeft(), top);
			}
			UiUtil.add(tabPanel, new LabelName(Messages.FRETE_PCT_REP), edVlPctFreteRep, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_REP), edVlFreteRep, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_CLI), edVlFreteCli, getLeft(), AFTER + HEIGHT_GAP);

			if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
				UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO), lbVlTotalPedidoAbaFrete, getLeft(), AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabPanel, new LabelName(Messages.FRETE_VL_PEDIDO_COM_FRETE), lbVlTotalPedidoComFrete, getLeft(), AFTER + HEIGHT_GAP);
		}
		if ((LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido())) {
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_NOME_ENTIDADE), lbNmTransportadora, getLeft(), top);
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_CDTIPOFRETE), lbNmTipoFrete, getLeft(), AFTER + HEIGHT_GAP);
			if ((getPedido().getTransportadora() != null && getPedido().getTransportadora().isFlMostraFrete()) || ValueUtil.isEmpty(getPedido().cdTransportadora)) {
				UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_VLFRETE), lbVlFrete, getLeft(), AFTER + HEIGHT_GAP);
			}
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_VLPEDIDO), lbVlTotalPedidoFrete, getLeft(), AFTER + HEIGHT_GAP);
			if ((LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido()	&& LavenderePdaConfig.escolhaTransportadoraPedidoPorCep())) {
				UiUtil.add(tabPanel, btAlteraTransportadora, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
	}

	public void atualizaVlPedidoAberto() throws SQLException {
		if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido && isEditing()) {
			Pedido pedido = getPedido();
			double vlPedido = LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido ? pedido.vlFinalPedidoDescTribFrete : pedido.vlTotalPedido;
			double vlPedidoAberto = vlPedido - pedido.vlTotalTrocaPedido - PagamentoPedidoService.getInstance().getVlPagamentosPedido(pedido);
			lvVlPedidoAberto.setValue(vlPedidoAberto > 0 ? vlPedidoAberto : 0);
			listPagamentoPedidoForm.vlPedidoAberto = lvVlPedidoAberto.getDoubleValue();
		}
	}

	public boolean usaInfoFreteAbaEntrega() {
		return (TABPANEL_INFO_FRETE_PEDIDO == 0) && (LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido);
	}

	@Override
	protected void addComponentesFixosFim() throws SQLException {
    	// PEDIDO
    	Container tabPanel = tabDinamica.getContainer(TABPANEL_PEDIDO);
		//Rentabilidade
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			UiUtil.add(tabPanel, new LabelName(Messages.CAD_UTILIZAR_RENTAB), bgUtilizaRentabilidade, getLeft(), AFTER + HEIGHT_GAP, FILL);
		}
		//Tipo de Pedido
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			if (LavenderePdaConfig.controlaExibicaoComboTipoPedidoPorPreferenciaFuncao) {
				if (SessionLavenderePda.hasPreferencia(PreferenciaFuncao.PERMITE_ALTERACAO_TIPO_PEDIDO)) {
					addCbTipoPedidoComponentes(tabPanel);
				}
			} else {
				addCbTipoPedidoComponentes(tabPanel);
			}
		}
		Pedido pedido = getPedido();
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.isFlIndicaAguardaComplemento()) {
			UiUtil.add(tabPanel, lbPedidoComplementar, bgAguardarPedidoComplementar, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Status Orçamento
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			UiUtil.add(tabPanel, lbStatusOrcamento, cbStatusOrcamento, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lbObsOrcamento, emObservacaoOrcamento, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Segmento
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_SEGMENTO), cbSegmento, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Tabela de Preço
		if (!LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			adicionaCbTabPrecoNaTela(tabPanel, pedido.isPedidoBonificacao());

		}
		//Setor do Cliente no Pedido
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_SETOR_ORIGEM), cbClienteSetorOrigem, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_SETOR), cbClienteSetor, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Tipo de pagamento
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente()) {
			adicionaCbTipoPagamentoNaTela(tabPanel);
		}
		//Vpc do Cliente
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			adicionaEditVpc(tabPanel);
		}
		if (LavenderePdaConfig.usaCondComercialPorCondPagto) {
			//Condição de pagamento
			adicionaCbCondicaoPagamentoNaTela(tabPanel, pedido.isPedidoBonificacao());
			//Credito Por Condição
			if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao() && pedido.isPedidoVenda()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_GERAR_CREDITO_BONIFICACAO_CONDICAO_PAGAMENTO), bgGeraCreditoBonificacaoCondicao, getLeft(), AFTER + WIDTH_GAP);
				UiUtil.add(tabPanel, lvGeraCreditoBonificacaoCondicao, bgGeraCreditoBonificacaoCondicao.getX2() + HEIGHT_GAP_BIG, SAME, PREFERRED, bgGeraCreditoBonificacaoCondicao.getHeight());
			}
			//Condicao Comercial
			adicionaCbCondComercialNaTela(tabPanel);
		} else {
			//Condicao Comercial
			adicionaCbCondComercialNaTela(tabPanel);
			//Condição de pagamento
			adicionaCbCondicaoPagamentoNaTela(tabPanel, pedido.isPedidoBonificacao());
			//Credito Por Condição
			if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao() && pedido.isPedidoVenda()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_GERAR_CREDITO_BONIFICACAO_CONDICAO_PAGAMENTO), bgGeraCreditoBonificacaoCondicao, getLeft(), AFTER + WIDTH_GAP);
				UiUtil.add(tabPanel, lvGeraCreditoBonificacaoCondicao, bgGeraCreditoBonificacaoCondicao.getX2() + HEIGHT_GAP_BIG, SAME, PREFERRED, bgGeraCreditoBonificacaoCondicao.getHeight());
			}
		}
		if ((LavenderePdaConfig.exibeDescontoAcrescimoIndice() && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca()) || (LavenderePdaConfig.exibeDescontoAcrescimoIndice() && pedido.isTipoPedidoBonificacao() && LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao)) {
			String dsLabel = LavenderePdaConfig.usaIndiceCondPagtoClienteConformePrazoMedio() ? Messages.PEDIDO_LABEL_PERC_DESC_ACRES_CONDICAO_PAGAMENTO_APLICADO : Messages.PEDIDO_LABEL_PERC_DESC_ACRES_CONDICAO_PAGAMENTO;
			UiUtil.add(tabPanel, new LabelName(dsLabel), lbVlPctDescCondicaoPagamento, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		}
		//Condição Negociacao
		adicionaCbCondNegociacaoNaTela(tabPanel);
		//Tabela de Preço
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			adicionaCbTabPrecoNaTela(tabPanel, pedido.isPedidoBonificacao());
		}
		//Tipo de pagamento
		if ((!(LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento()) && LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_NAO))
				|| (!LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente() && LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento.equals(ValueUtil.VALOR_NAO))) {
			adicionaCbTipoPagamentoNaTela(tabPanel);
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA), cbUnidade, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Frete
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			addComponentesFreteFixosFimCapaFretePersonalizado(tabPanel);
		} else if (LavenderePdaConfig.isExibeInformacoesFreteCapaPedidoEscolhaTransportadora()) {
			addComponentesFreteFixosFimCapaEscolhaTransp(tabPanel);
		} else {
			addComponentesFreteFixosFimCapaConvencional(tabPanel, tipoPedido);
		}

		if (LavenderePdaConfig.isPermiteInserirFreteManual() && !LavenderePdaConfig.isUsaTransportadoraAuxiliar() && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_VLFRETE), edVlManualFrete, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao() && pedido.isPedidoVenda()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_GERAR_CREDITO_BONIFICACAO_FRETE), bgGeraCreditoBonificacaoFrete, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, lvGeraCreditoBonificacaoFrete, bgGeraCreditoBonificacaoFrete.getX2() + HEIGHT_GAP_BIG, SAME, PREFERRED, bgGeraCreditoBonificacaoFrete.getHeight());
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			UiUtil.add(tabPanel, new LabelName(Messages.LABEL_ENTREGA), cbEntrega, getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega && pedido.getTabelaPreco() != null && pedido.getTabelaPreco().isPermiteIndicarDataEntregaManual()) {
				UiUtil.add(tabPanel, lbDtEntrega, edDtEntregaManual, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			}
		}
		//Tributação
		if (isShowComboTributacaoNovoCliente()) {
			UiUtil.add(tabPanel, lbTributacao, cbTributacao, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Carga de Pedidos
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			if (LavenderePdaConfig.permiteCadastrarCargaNaCapaDoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.CARGAPEDIDO_CARGA_PEDIDO), cbCargaPedido, getLeft(), AFTER, FILL - UiUtil.getControlPreferredHeight() - WIDTH_GAP - WIDTH_GAP_BIG);
				UiUtil.add(tabPanel, btNovaCargaPedido, AFTER + WIDTH_GAP, SAME, UiUtil.getControlPreferredHeight(), SAME);
			} else {
				UiUtil.add(tabPanel, new LabelName(Messages.CARGAPEDIDO_CARGA_PEDIDO), cbCargaPedido, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
		//Numero de Ordem de Compra do Cliente
		String labelNuOrdemCompraCliente = Messages.PEDIDO_LABEL_NUORDEMCOMPRACLIENTE;
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoApenasNumeros()) {
			UiUtil.add(tabPanel, new LabelName(labelNuOrdemCompraCliente), edNuOrdemCompraCliente1, getLeft(), AFTER + HEIGHT_GAP);
		}
		//--
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos()) {
			UiUtil.add(tabPanel, new LabelName(labelNuOrdemCompraCliente), edNuOrdemCompraCliente2, getLeft(), AFTER + HEIGHT_GAP);
		}
		//Area Venda
		if (LavenderePdaConfig.usaAreaVendas && !LavenderePdaConfig.usaAreaVendaAutoNoPedido) {
			UiUtil.add(tabPanel, lbAreaVenda, cbAreaVenda, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			UiUtil.add(tabPanel, ckVinculaCampanhaPublicitaria, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			if ((!pedidoFechadoOuTransmitido() && (ckVinculaCampanhaPublicitaria.isChecked() || ValueUtil.getBooleanValue(getPedido().flVinculaCampanhaPublicitaria))) || (pedidoFechadoOuTransmitido() && !ValueUtil.isEmpty(getPedido().cdCampanhaPublicitaria))) {
				UiUtil.add(tabPanel, new LabelName(Messages.CAMPANHA_PUBLICITARIA), getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
				UiUtil.add(tabPanel, edNuCampanhaPublicitaria, getLeft(), AFTER, FILL - btRelacionarPedido.getPreferredWidth() - WIDTH_GAP_BIG - WIDTH_GAP);
				UiUtil.add(tabPanel, btRelacionarCampanhaPublicitaria, AFTER + WIDTH_GAP, SAME, edNuCampanhaPublicitaria.getHeight(), edNuCampanhaPublicitaria.getHeight());
			}
		}
		if (isShowInfoPedidoRelacionado()) {
			lbPedidoRelacionado.setText(pedido.isPedidoVendaProducao() ? Messages.PEDIDO_PRODUCAO : Messages.PEDIDO_RELACIONADO);
			UiUtil.add(tabPanel, lbPedidoRelacionado, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edNuPedidoRelacionado, getLeft(), AFTER, FILL - btRelacionarPedido.getPreferredWidth() - WIDTH_GAP_BIG - WIDTH_GAP);
			UiUtil.add(tabPanel, btRelacionarPedido, AFTER + WIDTH_GAP, SAME, edNuPedidoRelacionado.getHeight(), edNuPedidoRelacionado.getHeight());
		}
		//Crédito
		if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda && isShowValorCreditoDisponivel()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CREDITO), lvValorCreditoDisponivel, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		}
		//Descontos do Pedido
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			UiUtil.add(tabPanel, new LabelName(LavenderePdaConfig.mostraPercDescMaxPedido ? Messages.PEDIDO_LABEL_DESCONTO_MAXIMO_CONDICAO_PAGAMENTO : Messages.PEDIDO_LABEL_DESCONTO_CONDICAO_PAGAMENTO), edVlPctDescCondicao, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				UiUtil.add(tabPanel, lbVlMaxPctDescCondicao, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, UiUtil.getControlPreferredHeight());
			}
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			UiUtil.add(tabPanel, lbPctDescontoCondicaoPagamento, edVlPctDescCondicao, getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
			UiUtil.add(tabPanel, lbPctDescontoHistoricoVendas, edVlPctDescHistoricoVendas,  getLeft() + edVlPctDescCondicao.getPreferredWidth() + WIDTH_GAP_BIG, SAME - lbPctDescontoCondicaoPagamento.getPreferredHeight(), PREFERRED);
		}
		nextLineXYTotalizadores();
		if (LavenderePdaConfig.apresentaCampoPercentualDescontoCapaPedido() || (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			UiUtil.add(tabPanel, lbPctDesconto, edVlPctDesconto, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
				UiUtil.add(tabPanel, lbVlDescontoCondPagto, lvVlDescontoCondPagto, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			} else if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				UiUtil.add(tabPanel, lvPercMaxDesconto, AFTER + WIDTH_GAP, SAME, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		} else if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			UiUtil.add(tabPanel, lbPctDesconto, edVlPctDescEspecial, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.usaDescontoPonderadoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_BRUTO_ITENS), lvVlTotalBrutoItens, RIGHT - WIDTH_GAP_BIG, lbPctDesconto.getY(), PREFERRED, UiUtil.getControlPreferredHeight());
			}
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido()) {
			UiUtil.add(tabPanel, lbVlDesconto, edVlDesconto, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				UiUtil.add(tabPanel, lvPercMaxDesconto, AFTER + WIDTH_GAP, SAME, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
			}
			UiUtil.add(tabPanel, lbEdPctDesconto, edPctDesconto, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				LabelValue lvMaxPercDesc = new LabelValue("@");
				lvMaxPercDesc.usePercentValue = true;
				lvMaxPercDesc.setValue(LavenderePdaConfig.aplicaDescontoNaCapaDoPedido);
				UiUtil.add(tabPanel, lvMaxPercDesc, AFTER + WIDTH_GAP, SAME, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
			}
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTOVENDA_MENSAL), edVlPctDescontoVendaMensal, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		// Desconto por tipo frete
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
			UiUtil.add(tabPanel, new LabelName(LavenderePdaConfig.mostraPercDescMaxPedido ? Messages.PEDIDO_LABEL_DESCONTO_MAXIMO_FRETE : Messages.PEDIDO_LABEL_DESCONTO_FRETE), edVlPctDescFrete, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
			if (LavenderePdaConfig.mostraPercDescMaxPedido) {
				UiUtil.add(tabPanel, lbVlMaxPctDescFrete, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && !pedido.isPedidoTroca()) {
			UiUtil.add(tabPanel, lbPctDescItem, edVlPctDescItem, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
				UiUtil.add(tabPanel, lbPctAcrescimoItem, AFTER + WIDTH_GAP_BIG * 4 + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
				UiUtil.add(tabPanel, edVlPctAcrescimoItem, SAME + WIDTH_GAP, AFTER, edVlPctDescItem.getPreferredWidth(), UiUtil.getControlPreferredHeight());
			}
		} else if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && !pedido.isPedidoTroca()) {
			UiUtil.add(tabPanel, lbPctAcrescimoItem, edVlPctAcrescimoItem, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		// Descontos em Cascata Manual
		if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_CLIENTE), edDescontoCascataManualDescCliente, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_2), edDescontoCascataManual2, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_3), edDescontoCascataManual3, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_CLIENTE), edDescCascataCategoria1, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_2), edDescCascataCategoria2, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_3), edDescCascataCategoria3, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		//--
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()) {
			UiUtil.add(tabPanel, lbVlTotalMargem, lvVlTotalMargem, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
			UiUtil.add(tabPanel, lbVlPctTotalMargem, lvVlPctTotalMargem, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_ENDERECO_COBRANCA), cbEnderecoCobranca, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido && !LavenderePdaConfig.usaIndicacaoClienteEntregaPedido && TABPANEL_INFO_FRETE_PEDIDO == 0) {
			if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_DTENTREGA), edDtEntrega, xTotalizadores, yTotalizadores);
				updateXYTotalizadores();
			}
			if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
				nextLineXYTotalizadores();
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_ENDERECO_ENTREGA), cbEnderecoEntrega, xTotalizadores, yTotalizadores);
				nextLineXYTotalizadores();
			}
		}

		// Obrigatoriedade do código Interno Produto
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			Cliente cliente = getPedido().getCliente();
			if (cliente != null && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.CAD_PROD_CLI_COD_VALIDAR_COD_INT), bgUsaCodigoInternoCliente, getLeft(), AFTER + HEIGHT_GAP, FILL);
			}
		}

		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.CONTATO_NOME_ENTIDADE), cbContatoErp, xTotalizadores, yTotalizadores);
		}
		if (!LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete && !LavenderePdaConfig.mostraValorBruto && LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido && LavenderePdaConfig.mostraVlBrutoCapaPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VALOR_BRUTO), lvVlBrutoCapaPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.mostraValorBruto) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VALOR_BRUTO), lvVlBruto, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();

		}
		if (LavenderePdaConfig.mostraValorDesconto) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VALOR_DESCONTO), lvVlDesconto, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.LABEL_VL_FRETE_ADICIONAL), edVlFreteAdicional, xTotalizadores, yTotalizadores + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTALITENS), lbVlTotalItens, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
				if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
					UiUtil.add(tabPanel, new LabelName(Messages.REL_LABEL_VALOR_TOTAL_COM_IMPOST_DESC), lvVlTtPedidoTributosEDeducoesEDesc, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
					updateXYTotalizadores();
				}
			}
			if (LavenderePdaConfig.utilizaNotasCredito()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_BRUTO_ITENS), lvVlTotalBrutoItens, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTAL_CREDITO), lvVlTotalNotaCredito, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();

			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDO_COM_IMPOSTOS), lvVlTotalPedidoComImpostos, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			} else {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDO), lbVlTotalPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
			if (LavenderePdaConfig.isMostraValorMinimoCapaPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VL_MIN_PROMOCIONAL), lbVlMinPromocional, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}

		if (isUsaComissao() && !LavenderePdaConfig.ocultaComissaoCapaPedidoConformeLayoutItemPedido && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PRODUTO_LABEL_COMISSAO_TOTAL), lvVlTotalPedidoComissao, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLEFETIVO), lbVlEfetivo, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			if (LavenderePdaConfig.mostraVPesoCapaPedido()) {
				lbQtPesoPedido.resetSetPositions();
				lvPesoPedido.resetSetPositions();
				UiUtil.add(tabPanel, lbQtPesoPedido, lvPesoPedido, xTotalizadores, yTotalizadores + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
			if (exibeVlTotalFreteCapaPedido(tipoPedido)) {
				UiUtil.add(tabPanel, lbVlTotalFrete, lvVlTotalFrete, xTotalizadores, yTotalizadores + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
				if (!tipoPedido.isIgnoraCalculoFrete() && LavenderePdaConfig.exibirInformacoesFreteCapaPedido() && exibeInfoFreteCapaPersonalizado(FreteConfig.EXIBE_VALOR_FRETE_CAPA)) {
					UiUtil.add(tabPanel, lbVlTotalFrete, lvVlTotalFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
					UiUtil.add(tabPanel, lbVlTotalFrete, lvVlTotalFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
					updateXYTotalizadores();
				}
			} else {
				UiUtil.add(tabPanel, lbVlBrutoPedidoMaisFrete, lvVlBrutoPedidoMaisFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}
		if (LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VL_PESOMINIMO_TABELA), lvVlPesoMinimoTabPreco, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTALPORPESO), lvVlTotalPedidoPorPeso, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.mostraValorParcelaPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_MOSTRA_VALOR_PARCELA), lvVlValorParcelaPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		//--
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() && (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.usaCalculoFretePersonalizado())
					&& !LavenderePdaConfig.ocultaValorFretePedido && !LavenderePdaConfig.configFreteEmbutidoDestacadoCliente() && (LavenderePdaConfig.exibirInformacoesFreteCapaPedido() && exibeInfoFreteCapaPersonalizado(FreteConfig.EXIBE_VALOR_FRETE_CAPA))) {
				UiUtil.add(tabPanel, lbVlTotalFrete, lvVlTotalFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
			boolean isUsaIpiOuSt = LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoIpiItemPedido();
			if ((LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete && isUsaIpiOuSt) || (!LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && !LavenderePdaConfig.usaRelSubstituicaoTributaria
					&& isUsaIpiOuSt)) {
				lbVlTotalPedidoComTributos.resetSetPositions();
				lvVlTotalPedidoComTributos.resetSetPositions();
				UiUtil.add(tabPanel, lbVlTotalPedidoComTributos, lvVlTotalPedidoComTributos, xTotalizadores, yTotalizadores + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}

			if (LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete) {
				UiUtil.add(tabPanel, lbVlBrutoPedidoMaisFrete, lvVlBrutoPedidoMaisFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}
		if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete() && tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLPEDIDO_MAIS_FRETE), lbVlTotalPedidoMaisFrete, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.usaApresentacaoValorStCapaPedido && (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido)) {
			lbVlTotalSTPedido.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_TOTAL_PEDIDO_COM_ST), lbVlTotalSTPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			if (!LavenderePdaConfig.isOcultaRentabilidadePedido()) {
				lbRentabilidade.resetSetPositions();
				lvVlPctRentabilidade.resetSetPositions();
				UiUtil.add(tabPanel, lbRentabilidade, lvVlPctRentabilidade, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
			if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_ESCALA_RENTABILIDADE), lvVlEscalaRentabilidade, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
			if ((LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 || LavenderePdaConfig.usaConfigMargemRentabilidade()) && !LavenderePdaConfig.isOcultaFaixaRentabilidadeCapaPedido()) {
				btIconeRentabilidade.resetSetPositions();
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_FAIXA_RENTABILIDADE), btIconeRentabilidade, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			if (!LavenderePdaConfig.isOcultaRentabilidadePedido()) {
				lbRentabilidade.resetSetPositions();
				lvVlPctRentabilidade.resetSetPositions();
				UiUtil.add(tabPanel, lbRentabilidade, lvVlPctRentabilidade, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				if (LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoPedido(pedido)) {
					lvVlPctRentabilidade.setVisible(false);
				}
				updateXYTotalizadores();
			}
			if (!LavenderePdaConfig.isOcultaFaixaRentabilidadeCapaPedido()) {
				btIconeRentabilidade.resetSetPositions();
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_FAIXA_RENTABILIDADE), btIconeRentabilidade, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
				updateXYTotalizadores();
			}
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() && !LavenderePdaConfig.isOcultaEscalaRentabilidadeCapaPedido()) {
			lvVlPctComissao.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_PERCENTUAL_COMISSAO), lvVlPctComissao, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			lvVlIndiceRentabPedido.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTAL_INDICE_RENTABILIDADE), lvVlIndiceRentabPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			lvVlTotalVolumePedido.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_VLTOTAL_VOLUME), lvVlTotalVolumePedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda) {
			UiUtil.add(tabPanel, lbPrecoLiberadoSenha, getLeft(), AFTER + WIDTH_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_QTITEMFATURAMENTO), lbQtItensFaturados, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			lbVlSeguroPedido.resetSetPositions();
			lvVlSeguroPedido.resetSetPositions();
			UiUtil.add(tabPanel, lbVlSeguroPedido, lvVlSeguroPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		//Valor total liberado e % restante
		if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !pedido.isPedidoBonificacao()) {
			UiUtil.add(tabPanel, lbPercDescLibRestante, lvPercDescLibRestante, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, lbVlTotalLiberado, lvVlTotalLiberado, RIGHT - WIDTH_GAP_BIG, BEFORE, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		//Icone Comissao Pedido Rep
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem() && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			UiUtil.add(tabPanel, new LabelName(Messages.LABEL_COMISSAO_PEDIDO), btIconeComissaoPedido, getLeft(), AFTER + HEIGHT_GAP_BIG, PREFERRED, UiUtil.getLabelPreferredHeight());
		}
		//Icone Peso Faixa
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio() && LavenderePdaConfig.isCalculaPesoTotalMostraPesoPorFaixa()) {
			UiUtil.add(tabPanel, new LabelName(Messages.LABEL_PESO_FAIXA), btIconeFaixa, getLeft(), AFTER + HEIGHT_GAP_BIG, PREFERRED, UiUtil.getLabelPreferredHeight());
		}
		//Despesa acessória
		if (LavenderePdaConfig.mostraDescAcessoriaCapaPedido) {
			lbVlDespesaAcessoria.resetSetPositions();
			lvVlDespesaAcessoria.resetSetPositions();
			UiUtil.add(tabPanel, lbVlDespesaAcessoria, lvVlDespesaAcessoria, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		//Verba do pedido
		if ((LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) && !pedido.isIgnoraControleVerba()) {
			UiUtil.add(tabPanel, lbVlVerbaPedido, lvVlVerbaPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		// Pontuacao do Pedido
		if (LavenderePdaConfig.mostraPontuacaoPedidoBase() || LavenderePdaConfig.mostraPontuacaoPedidoRealizado()) {
			lvVlPontuacao.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.PONTUACAO_PEDIDO), lvVlPontuacao, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}
		// Valor de frete do pedido
		if (isExibeVlFreteComponentesFixosFimCapaEscolhaTransp()) {
			lvVlFreteCapaPedido.resetSetPositions();
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORAREG_LABEL_VLFRETE), lvVlFreteCapaPedido, xTotalizadores, yTotalizadores, PREFERRED, UiUtil.getControlPreferredHeight());
			updateXYTotalizadores();
		}

		UiUtil.add(tabPanel, new Label("    "), 0, AFTER, HEIGHT_GAP * 2, HEIGHT_GAP * 2);
		// DETALHES
		tabPanel = tabDinamica.getContainer(TABPANEL_DETALHES);
		//Verba por Conta Corrente e Produto
		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) && !pedido.isIgnoraControleVerba() && !LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			UiUtil.add(tabPanel, lbVlVerbaPedido, lvVlVerbaPedido, getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.isMostraFlexPositivoPedido(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
				UiUtil.add(tabPanel, lbVlVerbaPedidoPositiva, edVlVerbaPedidoPositiva, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			UiUtil.add(tabPanel, lbVlPctIndiceFinCondPagto, lvVlPctIndiceFinCondPagto, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			UiUtil.add(tabPanel, lbVlPctDescQuantidadePeso, lvVlPctDescQuantidadePeso, getLeft(), AFTER + HEIGHT_GAP);
		}
		//ENTREGA
		if (TABPANEL_ENTREGA != 0) {
			tabPanel = tabDinamica.getContainer(TABPANEL_ENTREGA);
			if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0 && (!LavenderePdaConfig.apresentaDadosEntregaNaAbaPrincipalPedido || LavenderePdaConfig.usaIndicacaoClienteEntregaPedido)) {
				UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_ENDERECO_ENTREGA), cbEnderecoEntrega, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
    }

	private boolean exibeVlTotalFreteCapaPedido(TipoPedido tipoPedido) {
		return !LavenderePdaConfig.ocultaInfosValoresPedido && LavenderePdaConfig.usaFretePedidoPorToneladaCliente && tipoPedido != null
				&& !tipoPedido.isIgnoraCalculoFrete() && !LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()
				&& (LavenderePdaConfig.exibirInformacoesFreteCapaPedido() && exibeInfoFreteCapaPersonalizado(FreteConfig.EXIBE_VALOR_FRETE_CAPA));
	}

	private void addComponentesFreteFixosFimCapaFretePersonalizado(Container tabPanel) {
		List<String> param = Arrays.asList(LavenderePdaConfig.informacoesParaSeremExibidasFreteCapaPedido());
		if (param.contains(FreteConfig.EXIBE_TRANSPORTADORA_CAPA)) {
			UiUtil.add(tabPanel, new LabelName(Messages.TRANSPORTADORA), cbTranspFretePersonalizado, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (param.contains(FreteConfig.EXIBE_TIPO_FRETE_CAPA)) {
			UiUtil.add(tabPanel, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete, getLeft(), AFTER + HEIGHT_GAP);
		}
	}

	private void addComponentesFreteFixosFimCapaConvencional(Container tabPanel, TipoPedido tipoPedido) {
		if (LavenderePdaConfig.isUsaTipoFretePedido() && !LavenderePdaConfig.isUsaTransportadoraAuxiliar()  || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			if (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) {
				UiUtil.add(tabPanel, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete, getLeft(), AFTER + HEIGHT_GAP);
				if (ValueUtil.isEmpty(cbTipoFrete.getValue())) {
					cbTipoFrete.selectTipoFretePadrao();
				}
			}
			if (LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
				UiUtil.add(tabPanel, new LabelName(Messages.TIPOFRETE_LABEL_CNPJ), edCnpjTransportadora, getLeft(), + AFTER + HEIGHT_GAP);
			}
			if (LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.FRETE_LABEL_VLITEMPEDIDOFRETE), lvVlTotalFreteItensPedido, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
	}

	private boolean exibeInfoFreteCapaPersonalizado(String exibeTipoFreteCapa) {
		List<String> param = Arrays.asList(LavenderePdaConfig.informacoesParaSeremExibidasFreteCapaPedido());
		return param.contains(exibeTipoFreteCapa);
	}

	private void adicionaEditVpc(Container tabPanel) throws SQLException {
		double vlPct = getPedido().getCliente().vlPctVpc;
		edVlPctVpc.setValue(vlPct);
		if(LavenderePdaConfig.usaCalculoVpcItemPedidoParaTodosClientes()) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_VALOR_VPC), edVlPctVpc, getLeft(), AFTER + WIDTH_GAP);
		} else if (vlPct != 0) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_VALOR_VPC), edVlPctVpc, getLeft(), AFTER + WIDTH_GAP);
		}
	}

	private boolean isShowComboTributacaoNovoCliente() throws SQLException {
		return LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente() && PedidoUiUtil.isPedidoSemCliente(getPedido());
	}

	private void addCbTipoPedidoComponentes(Container tabPanel) {
		UiUtil.add(tabPanel, new LabelName(Messages.TIPOPEDIDO_LABEL_TIPOPEDIDO), getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) {
			UiUtil.add(tabPanel, cbTipoPedido, getLeft(), AFTER, FILL - edValorMinTipoPedido.getPreferredWidth() - WIDTH_GAP);
			UiUtil.add(tabPanel, lbVlMinTipoPed, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edValorMinTipoPedido, SAME, AFTER);
		} else {
			UiUtil.add(tabPanel, cbTipoPedido, getLeft(), AFTER);
		}
	}

	private void adicionaCbTabPrecoNaTela(Container tabPanel, boolean isBonificacao) {
		if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && (LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco() || LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem()) && !isBonificacao) {
			UiUtil.add(tabPanel, lbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, cbTabelaPreco, getLeft(), AFTER, FILL - edVlMinTabelaPreco.getPreferredWidth() - WIDTH_GAP);
			UiUtil.add(tabPanel, lbVlMinTabelaPreco, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edVlMinTabelaPreco, SAME, AFTER);
		} else if (LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
			UiUtil.add(tabPanel, lbTabelaPreco, cbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP);
		}
		tabPanel.reposition();
	}

	private void updateXYTotalizadores() {
		if (xTotalizadores == getLeft()) {
			xTotalizadores = RIGHT - UiUtil.BASE_MARGIN_GAP;
			yTotalizadores = BEFORE;
		} else {
			nextLineXYTotalizadores();
		}
	}
	
	private void nextLineXYTotalizadores() {
		xTotalizadores = getLeft();
		yTotalizadores = AFTER + HEIGHT_GAP;
	}

	private void adicionaCbCondComercialNaTela(Container tabPanel) {
		if (LavenderePdaConfig.usaCondicaoComercialPedido && !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL), cbCondicaoComercial, getLeft(), AFTER + HEIGHT_GAP);
		}
	}

	private void adicionaCbCondNegociacaoNaTela(Container tabPanel) {
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CONDICAONEGOCIACAO), cbCondicaoNegociacao, getLeft(), AFTER + HEIGHT_GAP);
		}
	}

	private void adicionaCbTipoPagamentoNaTela(Container tabPanel) throws SQLException {
		Pedido pedido = getPedido();
		if (!isPedidoQueUsaTipoPagamento(pedido)) {
			pedido.cdTipoPagamento = null;
			return;
		}
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() && !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente()) {
			UiUtil.add(tabPanel, new LabelName(Messages.TIPOPAGTO_LABEL_TIPOPAGTO), getLeft(), AFTER + HEIGHT_GAP);
			if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
				UiUtil.add(tabPanel, cbTipoPagamento, getLeft(), AFTER, FILL - edValorMinTipoPagto.getPreferredWidth() - WIDTH_GAP);
				UiUtil.add(tabPanel, lbVlMinTipoPagto, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
				UiUtil.add(tabPanel, edValorMinTipoPagto, SAME, AFTER);
			} else {
				UiUtil.add(tabPanel, cbTipoPagamento, getLeft(), AFTER);
			}
		}
	}

	private void btRelacionarPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
		if (ValueUtil.isNotEmpty(pedido.nuPedidoRelBonificacao) || ValueUtil.isNotEmpty(pedido.nuPedidoRelTroca)) {
			if (UiUtil.showConfirmYesCancelMessage(Messages.PEDIDO_RELACIONADO_REMOVER_PEDIDO) == 1) {
				limpaRelacionamentos();
			} else {
				return;
			}
		} else if (ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
			if (UiUtil.showConfirmYesCancelMessage(Messages.PEDIDO_RELACIONADO_REMOVER_PEDIDO) == 1) {
				limpaRelacionamentos();
				limpaCombosPedidoComplementarMesmaConfPedidoOriginal();
			} else {
				return;
			}
		}
		showRelPedidoRelacionadoWindow();
		if (pedido.isPedidoComplementar() && !ValueUtil.isEmpty(edNuPedidoRelacionado.getText())) btRelacionarPedido.setEnabled(false);
	}

	private void btRelacionarCampanhaPublicitariaClick() throws SQLException {
		CampanhaPublicitariaWindow campanhaPublicitariaWindow = new CampanhaPublicitariaWindow(getPedido());
		campanhaPublicitariaWindow.popup();
		if (!campanhaPublicitariaWindow.closedByBtFechar) {
			edNuCampanhaPublicitaria.setText(campanhaPublicitariaWindow.getDsCampanhaPublicitariaSelecionada());
			getPedido().cdCampanhaPublicitaria = campanhaPublicitariaWindow.getCdCampanhaPublicitariaSelecionada();
		}
	}

	private void limpaCombosPedidoComplementarMesmaConfPedidoOriginal() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.isPedidoComplementar()) {
			if (LavenderePdaConfig.usaPedidoComplementarMesmaConfPedidoOriginal) {
				loadComboTipoPagamento(pedido.getCliente());
				cbTipoPagamento.setEditable(true);
				reloadComboTabelaPreco();
				cbTabelaPreco.setEditable(true);
				reloadComboCondicaoComercial();
				cbCondicaoComercial.setEditable(true);
				reloadComboCondicaoPagamento();
				cbCondicaoPagamento.setEditable(true);
				reloadComboTransportadora();
				cbTransportadora.setEditable(true);
			}
		}
	}

	private void showRelPedidoRelacionadoWindow() throws SQLException {
		Pedido pedido = getPedido();
		RelPedidoRelacionadoWindow relPedidoRelacionadoWindow = new RelPedidoRelacionadoWindow(pedido);
		relPedidoRelacionadoWindow.popup();
		if (LavenderePdaConfig.isUsaVariosPedidosBonificados() || (pedido.isPedidoVendaProducao() && LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao())) {
			if (!relPedidoRelacionadoWindow.closedByBtFechar) {
				relacionaMultiplosPedidos(pedido, relPedidoRelacionadoWindow.pedidosRelacionados);
				if (pedido.isPedidoVendaProducao() && isEditing()) {
					if (ValueUtil.isEmpty(pedido.pedidoRelacionadoList)) {
						PedidoRelacionadoService.getInstance().deletePedidosRelacionados(pedido);
					} else {
						PedidoRelacionadoService.getInstance().insereMultiplosPedidosRelacionados(pedido, pedido.pedidoRelacionadoList);
					}
				}
			}
		} else {
			if (!relPedidoRelacionadoWindow.closedByBtFechar) {
				relacionaUnicoPedido(pedido, relPedidoRelacionadoWindow.pedidoVendaRelacionado);
				if (pedido.isPedidoVendaProducao() && isEditing()) {
					if (pedido.pedidoRelacionado == null) {
						PedidoRelacionadoService.getInstance().deletePedidosRelacionados(pedido);
					} else {
						PedidoRelacionadoService.getInstance().insereUnicoPedidoRelacionado(pedido, pedido.pedidoRelacionado);
					}
				}
			}
			if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
				lvValorCreditoDisponivel.setValue(PedidoUiUtil.getValorCreditoDisponivel(relPedidoRelacionadoWindow.pedidoVendaRelacionado));
				lvValorCreditoDisponivel.setVisible(isShowValorCreditoDisponivel());
			}
			if (PedidoUiUtil.isUsaCondicaoPgtoTipoFretePedidoRelacionado(pedido)) {
				cbTipoFrete.setValue(relPedidoRelacionadoWindow.pedidoVendaRelacionado.cdTipoFrete, pedido.getCliente() != null ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO);
				cbTipoFrete.setEnabled(false);
			}
		}

	}

	private void relacionaUnicoPedido(Pedido pedido, Pedido pedidoRelacionado) throws SQLException {
		limpaRelacionamentos();
		if (pedidoRelacionado == null) return;
		if (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao()) {
			pedido.nuPedidoRelBonificacao = pedidoRelacionado.nuPedido;
			pedido.flOrigemPedidoRelacionado = pedidoRelacionado.flOrigemPedido;
			pedido.cdCondicaoPagamento = pedidoRelacionado.cdCondicaoPagamento;
			pedido.cdTipoFrete = pedidoRelacionado.cdTipoFrete;
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelBonificacao);
		} else if (pedido.isPedidoComplementar()) {
			pedido.nuPedidoComplementado = pedidoRelacionado.nuPedido;
			//--
			setPedidoComplementarComMesmaConfPedidoOriginal(pedido, pedidoRelacionado);
			//--
			edNuPedidoRelacionado.setText(pedido.nuPedidoComplementado);
		} else if (pedido.isPedidoTroca()) {
			pedido.nuPedidoRelTroca = pedidoRelacionado.nuPedido;
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelTroca);
		} else if (pedido.isPedidoVendaProducao()) {
			edNuPedidoRelacionado.setText(pedidoRelacionado.nuPedido);
		}
		pedido.pedidoRelacionado = pedidoRelacionado;
	}

	private void relacionaMultiplosPedidos(Pedido pedido, Vector pedidosRelacionados) throws SQLException {
		limpaRelacionamentos();
		if (ValueUtil.isEmpty(pedidosRelacionados)) return;
		if (pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao()) {
			pedido.nuPedidoRelBonificacao = getNuPedidoPedidosRelacionados(pedidosRelacionados);
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelBonificacao);
		} else if (pedido.isPedidoTroca()) {
			pedido.nuPedidoRelTroca = getNuPedidoPedidosRelacionados(pedidosRelacionados);
			edNuPedidoRelacionado.setText(pedido.nuPedidoRelTroca);
		} else if (pedido.isPedidoVendaProducao()) {
			if (pedidosRelacionados.size() > 0) {
				edNuPedidoRelacionado.setText(getNuPedidoPedidosRelacionados(pedidosRelacionados));
			}
		}
		if (pedidosRelacionados.size() > 0) {
			pedido.pedidoRelacionadoList = pedidosRelacionados;
		}
	}

	private String getNuPedidoPedidosRelacionados(Vector pedidosRelacionados) {
		int pedidosRelacionadosSize = pedidosRelacionados.size();
		String nuPedidosRelacionados = ValueUtil.VALOR_NI;
		if (pedidosRelacionadosSize > 0) {
			for (int i = 0; i < pedidosRelacionadosSize; i++) {
				Pedido pedidoRelacionado = (Pedido) pedidosRelacionados.items[i];
				nuPedidosRelacionados += pedidoRelacionado.nuPedido;
				if (i < pedidosRelacionadosSize - 1) {
					nuPedidosRelacionados += ";";
				}
			}
		} else {
			if (pedidosRelacionados.items[0] == null) return nuPedidosRelacionados;
			Pedido pedidoRelacionado = (Pedido) pedidosRelacionados.items[0];
			nuPedidosRelacionados =  pedidoRelacionado.nuPedido;
		}
		return nuPedidosRelacionados;
	}

	private void setPedidoComplementarComMesmaConfPedidoOriginal(Pedido pedido,	Pedido pedidoVendaRelacionado) throws SQLException {
		if (LavenderePdaConfig.usaPedidoComplementarMesmaConfPedidoOriginal && isEnabled() && pedido.isPedidoComplementar()) {
			loadTabelaPrecoPedidoComplementar(pedido, pedidoVendaRelacionado);
			loadCondicaoComercialPedidoComplementar(pedido, pedidoVendaRelacionado);
			if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento()) {
				loadTipoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
				loadCondicaoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
			} else if (LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento()) {
				loadCondicaoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
				loadTipoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
			} else {
				loadTipoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
				loadCondicaoPagamentoPedidoComplementar(pedido, pedidoVendaRelacionado);
			}
			loadTransportadoraPedidoComplementar(pedido, pedidoVendaRelacionado);
			loadTipoEntregaPedidoComplementar(pedido, pedidoVendaRelacionado);
			loadDataEntregaPedidoComplementar(pedido, pedidoVendaRelacionado);
			loadRotaEntregaPedidoComplementar(pedido, pedidoVendaRelacionado);
			if (pedidoVendaRelacionado.getHashValuesDinamicos().exists(Pedido.NMCOLUNA_ENDPADRAO)) {
				String endPadrao = PedidoService.getInstance().findColumnByRowKey(pedidoVendaRelacionado.getRowKey(), Pedido.NMCOLUNA_ENDPADRAO);
				if (hashComponentes.get(Pedido.NMCOLUNA_ENDPADRAO) instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) hashComponentes.get(Pedido.NMCOLUNA_ENDPADRAO)).setValue(endPadrao);
				}
			}
		}
	}

	private void loadRotaEntregaPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) {
		if (cbRotaEntrega.isVisible()) {
			if (pedidoVendaRelacionado.cdRotaEntrega != null) {
				pedido.cdRotaEntrega = pedidoVendaRelacionado.cdRotaEntrega;
				cbRotaEntrega.setValue(pedido.cdRotaEntrega);
			}
		}
	}

	private void loadDataEntregaPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) {
		if (edDtEntrega.isVisible()) {
			if (pedidoVendaRelacionado.dtEntrega != null) {
				pedido.dtEntrega = pedidoVendaRelacionado.dtEntrega;
				edDtEntrega.setValue(pedido.dtEntrega);
			}
		}
	}

	private void loadTipoEntregaPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) {
		if (cbTipoEntrega.isVisible()) {
			if (pedidoVendaRelacionado.cdTipoEntrega != null) {
				pedido.cdTipoEntrega = pedidoVendaRelacionado.cdTipoEntrega;
				cbTipoEntrega.setValue(pedido.cdTipoEntrega);
			}
		}
	}

	private void loadTransportadoraPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) throws SQLException {
		if (cbTransportadora.isVisible()) {
			if (pedidoVendaRelacionado.getTransportadora() != null && pedidoVendaRelacionado.getTransportadora().cdEmpresa != null) {
				pedido.cdTransportadora = pedidoVendaRelacionado.cdTransportadora;
				cbTransportadora.setValue(pedido.cdTransportadora);
				cbTransportadora.setEditable(false);
			} else {
				cbTransportadora.setEditable(true);
			}
		}
	}

	private void loadCondicaoComercialPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) throws SQLException {
		if (cbCondicaoComercial.isVisible()) {
			if (pedidoVendaRelacionado.getCondicaoComercial() != null && pedidoVendaRelacionado.getCondicaoComercial().cdEmpresa != null) {
				pedido.cdCondicaoComercial = pedidoVendaRelacionado.cdCondicaoComercial;
				cbCondicaoComercial.setValue(pedido.cdCondicaoComercial);
				cbCondicaoComercial.setEditable(false);
			} else {
				cbCondicaoComercial.setEditable(true);
			}
		}
	}

	private void loadTabelaPrecoPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) throws SQLException {
		if (cbTabelaPreco.isVisible()) {
			if (pedidoVendaRelacionado.getTabelaPreco() != null && pedidoVendaRelacionado.getTabelaPreco().cdEmpresa != null) {
				pedido.cdTabelaPreco = pedidoVendaRelacionado.cdTabelaPreco;
				cbTabelaPreco.setValue(pedido.cdTabelaPreco);
				cbTabelaPreco.setEditable(false);
			} else {
				cbTabelaPreco.setEditable(true);
			}
		}
	}

	private void loadTipoPagamentoPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) throws SQLException {
		if (cbTipoPagamento.isVisible()) {
			if (pedidoVendaRelacionado.getTipoPagamento() != null && pedidoVendaRelacionado.getTipoPagamento().cdEmpresa != null) {
				boolean isTipoPagtoChanged = ValueUtil.valueNotEqualsIfNotNull(pedido.cdTipoPagamento, pedidoVendaRelacionado.cdTipoPagamento);
				cbTipoPagamento.carregaTipoPagamentos(pedido, false);
				cbTipoPagamento.setValue(pedidoVendaRelacionado.cdTipoPagamento);
				if (cbTipoPagamento.getValue() != null) {
					pedido.cdTipoPagamento = cbTipoPagamento.getValue();
					cbTipoPagamento.setEditable(false);
				} else {
					cbTipoPagamento.setValue(pedido.cdTipoPagamento);
					cbTipoPagamento.setEditable(true);
				}
				if (isTipoPagtoChanged) {
					cbTipoPagamentoChange();
				}
			} else {
				cbTipoPagamento.setEditable(true);
			}
		}
	}

	private void loadCondicaoPagamentoPedidoComplementar(Pedido pedido, Pedido pedidoVendaRelacionado) throws SQLException {
		if (cbCondicaoPagamento.isVisible()) {
			if (pedidoVendaRelacionado.getCondicaoPagamento() != null && pedidoVendaRelacionado.getCondicaoPagamento().cdEmpresa != null) {
				cbCondicaoPagamento.loadCondicoesPagamento(pedido);
				boolean isCondPagtoChanged = ValueUtil.valueNotEqualsIfNotNull(pedido.cdCondicaoPagamento, pedidoVendaRelacionado.cdCondicaoPagamento);
				cbCondicaoPagamento.setValue(pedidoVendaRelacionado.cdCondicaoPagamento);
				if (cbCondicaoPagamento.getValue() != null) {
					pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
					cbCondicaoPagamento.setEditable(false);
				} else {
					cbCondicaoPagamento.setValue(pedido.cdCondicaoPagamento);
					cbCondicaoPagamento.setEditable(true);
				}
				if (isCondPagtoChanged) {
					cbCondicaoPagamentoChange();
				}
			} else {
				cbCondicaoPagamento.setEditable(true);
			}
		}
	}

	private void limpaRelacionamentos() throws SQLException {
		Pedido pedido = getPedido();
		pedido.nuPedidoRelBonificacao = "";
		pedido.nuPedidoRelTroca = "";
		pedido.nuPedidoComplementado = "";
		pedido.pedidoRelacionado = null;
		if (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao()){
			pedido.pedidoRelacionadoList.removeAllElements();
		}
		edNuPedidoRelacionado.setText("");
		cbTipoFrete.setEnabled(isEnabled() && !PedidoUiUtil.isUsaCondicaoPgtoTipoFretePedidoRelacionado(pedido) && !LavenderePdaConfig.usaCalculoFretePersonalizado());
		lvValorCreditoDisponivel.setValue(0d);
		if (LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes()) {
			return;
		}
		PedidoService.getInstance().atualizaRelacionamento(pedido);
	}

	private void adicionaCbCondicaoPagamentoNaTela(Container tabPanel, boolean isBonificacao) throws SQLException {
		Pedido pedido = getPedido();
		if (!PedidoUiUtil.isPedidoUsaCondicaoPagamento(pedido)) {
			pedido.cdCondicaoPagamento = null;
			return;
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), edDsCondicaoPagamentoSemCadastro, getLeft(), AFTER + WIDTH_GAP);
		} else if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				remove(lbTabelaPreco);
				remove(cbTabelaPreco);
				if ((LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPreco() 	|| LavenderePdaConfig.isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem()) && !isBonificacao) {
					remove(lbVlMinTabelaPreco);
					remove(edVlMinTabelaPreco);
				}
			}
			UiUtil.add(tabPanel, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), getLeft(), AFTER + WIDTH_GAP);
			desenhaConfiguracaoCondicaoPagamentoNaTela(tabPanel);
			edDsCondicaoPagamento.setVisible(false);
			if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				adicionaCbTabPrecoNaTela(tabPanel, isBonificacao);
			}
		}
	}

	private void desenhaConfiguracaoCondicaoPagamentoNaTela(Container tabPanel) {
		if (!LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido() && !LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, edDsCondicaoPagamento, getLeft(), AFTER);
			UiUtil.add(tabPanel, cbCondicaoPagamento, SAME, SAME);
			return;
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && !LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, cbCondicaoPagamento, getLeft(), AFTER, FILL - edValorMinCondPagto.getPreferredWidth() - WIDTH_GAP);
			UiUtil.add(tabPanel, edDsCondicaoPagamento, SAME, SAME, FILL - edValorMinCondPagto.getPreferredWidth() - WIDTH_GAP, PREFERRED);
			UiUtil.add(tabPanel, lbVlMinCondPagto, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edValorMinCondPagto, SAME, AFTER);
			return;
		}
		if (!LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isNuParcelasNoPedido() && LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
			UiUtil.add(tabPanel, cbCondicaoPagamento, getLeft(), AFTER, FILL - edPrazoMedio.getPreferredWidth() - WIDTH_GAP);
			UiUtil.add(tabPanel, edDsCondicaoPagamento, SAME, SAME, FILL - edPrazoMedio.getPreferredWidth() - WIDTH_GAP, PREFERRED);
			UiUtil.add(tabPanel, lbVlPrazoMedio, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edPrazoMedio, SAME, AFTER);
			return;
		}
		if (!LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && LavenderePdaConfig.isNuParcelasNoPedido()) {
			int btParcelaSolAutorizacaoWidth = LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax() ? btLibParcelaSolAutorizacao.getPreferredWidth() : 0;
			UiUtil.add(tabPanel, cbCondicaoPagamento, getLeft(), AFTER, FILL - edNuParcelaPedido.getPreferredWidth() - btParcelaSolAutorizacaoWidth - WIDTH_GAP);
			UiUtil.add(tabPanel, edDsCondicaoPagamento, SAME, SAME, FILL - edNuParcelaPedido.getPreferredWidth() - btParcelaSolAutorizacaoWidth - WIDTH_GAP, PREFERRED);
			UiUtil.add(tabPanel, lbNuParcelasPedido, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edNuParcelaPedido, SAME, AFTER, FILL - btParcelaSolAutorizacaoWidth - UiUtil.BASE_MARGIN_GAP);
			if (LavenderePdaConfig.usaSolicitacaoAutorizacaoPorParcelaMinMax()) {
				UiUtil.add(tabPanel, btLibParcelaSolAutorizacao, AFTER, SAME - HEIGHT_GAP);
			}
			return;
		}  else {
			UiUtil.add(tabPanel, edDsCondicaoPagamento, getLeft(), AFTER);
			UiUtil.add(tabPanel, cbCondicaoPagamento, SAME, SAME);
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && !LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, lbVlMinCondPagto, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, edValorMinCondPagto, SAME, AFTER, edValorMinCondPagto.getPreferredWidth());
			UiUtil.add(tabPanel, lbVlPrazoMedio, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edPrazoMedio, SAME, AFTER, edPrazoMedio.getPreferredWidth());
			return;
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, lbVlMinCondPagto, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, edValorMinCondPagto, SAME, AFTER, edValorMinCondPagto.getPreferredWidth());
			UiUtil.add(tabPanel, lbNuParcelasPedido, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edNuParcelaPedido, SAME, AFTER, edPrazoMedio.getPreferredWidth());
			return;
		}
		if (!LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, lbVlPrazoMedio, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, edPrazoMedio, SAME, AFTER, edValorMinCondPagto.getPreferredWidth());
			UiUtil.add(tabPanel, lbNuParcelasPedido, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edNuParcelaPedido, SAME, AFTER, edPrazoMedio.getPreferredWidth());
			return;
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() && LavenderePdaConfig.isNuParcelasNoPedido()) {
			UiUtil.add(tabPanel, lbVlMinCondPagto, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, edValorMinCondPagto, SAME, AFTER, edValorMinCondPagto.getPreferredWidth());
			UiUtil.add(tabPanel, lbVlPrazoMedio, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edPrazoMedio, SAME, AFTER, edPrazoMedio.getPreferredWidth());
			UiUtil.add(tabPanel, lbNuParcelasPedido, AFTER + WIDTH_GAP, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, edNuParcelaPedido, SAME, AFTER, edPrazoMedio.getPreferredWidth());
		}
	}

	//@Override
	protected void onFormStart() throws SQLException {
		super.onFormStart();
		//--
		// Remove todos os botões para adicionar novamente, estava ocorrendo de botões ficarem sobrepostos na replicação de pedidos. LAV-27519
		barBottomContainer.removeAll();
		setaBotoesNaPrimeiraPosicao();
		UiUtil.add(barBottomContainer, btListaItens, 2);
		UiUtil.add(barBottomContainer, bmOpcoes, 3);
		UiUtil.add(barBottomContainer, btFecharPedido,  4);
		UiUtil.add(barBottomContainer, btRecalcularPedido,  4);
		boolean reabrirAdded = false;
		if (LavenderePdaConfig.permiteReabrirPedidoFechado) {
			UiUtil.add(barBottomContainer, btReabrirPedido, 4);
			reabrirAdded = true;
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento || LavenderePdaConfig.usaLiberacaoProprioRep()) {
			UiUtil.add(barBottomContainer, btLiberarPedido, 5);
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			UiUtil.add(barBottomContainer, btLiberarItensPendentes, 4);
		}
		if (LavenderePdaConfig.mostraAbaNfeNoPedido) {
			UiUtil.add(barBottomContainer, btListaItensNfe, 4);
			if (reabrirAdded) {
				btReabrirPedido.posOnBarBottom = 5;
				repaintTitleAndButtons();
			}
		}
		UiUtil.add(barBottomContainer, btNovoItem, 5);
	}

	private void setaBotoesNaPrimeiraPosicao() throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.ocultaNotaFiscalPedidoNaoRetornado && pedido.isFlOrigemPedidoErp() && ValueUtil.isNotEmpty(pedido.getNotaFiscalList())) {
			UiUtil.add(barBottomContainer, btNotaFiscal, 1);
		} else {
			if (getPedido().isPedidoPermiteConsignacao()) {
				UiUtil.add(barBottomContainer, btConsignarPedido, 1);
				if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
					UiUtil.add(barBottomContainer, btDevolucaoConsignacaoPedido, 1);
				}
			} else {
				if (LavenderePdaConfig.usaPedidoPerdido) {
					UiUtil.add(barBottomContainer, btPerderPedido, 1);
				} else {
				UiUtil.add(barBottomContainer, btExcluirPedido, 1);
			}

			}
			if (LavenderePdaConfig.usaWorkflowStatusPedido && !LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao && !getPedido().isPedidoAberto()) {
				UiUtil.add(barBottomContainer, btWorkflow, 1);
			} else 	if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento) {
				UiUtil.add(barBottomContainer, btDescontosConcedidos, 1);
			}
		}
	}

	private boolean isTargetComponentesDinamicos(Object target) {
		Vector hashValues = hashComponentes.getValues();
		if (target == null || ValueUtil.isEmpty(hashValues)) {
			return false;
		}
		if (target instanceof Control) {
			Control control = (Control) target;
			if (control instanceof PushButtonGroupBase) {
				return hashValues.indexOf(control) != -1 || hashValues.indexOf(control.getParent()) != -1;
			}
			return  hashValues.indexOf(control) != -1;
		}
		return false;
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target instanceof BaseComboBox || event.target == edDtEntrega.btCalendario || event.target == btRelacionarPedido ||  isTargetComponentesDinamicos(event.target)) {
					houveAlteracaoCampos = true;
				}
				if (event.target == btNovoItem) {
					if (LavenderePdaConfig.isUsaConfigMenuCatalogo()) {
						btNovoItemPedidoMenuCatalogoClick();
					} else {
						novoItemPedido();
					}
				} else if (event.target == btListaItens) {
					btListaItensClick();
				} else if (event.target == btWorkflow) {
					btWorkflowClick();
				} else if (event.target == btFecharPedido) {
					btFecharPedidoClick();
				} else if (event.target == btReabrirPedido) {
					btReabrirPedidoClick();
				} else if (event.target == btAlteraTransportadora) {
					btAlteraTransportadoraClick();
				} else if (event.target == cbTabelaPreco) {
					cbTabelaPrecoClick();
				} else if (event.target == cbUnidade) {
					getPedido().cdUnidade = cbUnidade.getValue();
				} else if (event.target == cbCondicaoPagamento) {
					cbCondicaoPagamentoClick();
				} else if (event.target == cbTipoPagamento) {
					cbTipoPagamentoChange();
				} else if (event.target == cbSegmento) {
					cbSegmentoChange();
				} else if (event.target == cbCondicaoComercial) {
					cbCondicaoComercialChange();
				} else if (event.target == cbTransportadora) {
					cbTransportadoraChange();
				} else if (event.target == cbRotaEntrega) {
					cbRotaEntregaChange();
				} else if (event.target == cbTipoPedido) {
					cbTipoPedidoClick();
				} else if (event.target == cbTipoFrete) {
					cbTipoFreteClick();
				} else if (event.target == btExcluirPedido) {
					if (PedidoService.getInstance().validaUsuarioEmissaoPedido()) {
						excluirClick();
					}
				} else if (event.target == btPerderPedido) {
					if (PedidoService.getInstance().validaUsuarioEmissaoPedido()) {
						btPerderPedidoClick();
					}
				} else if (event.target == cbClienteSetorOrigem) {
					Pedido pedido = getPedido();
					pedido.cdOrigemSetor = null;
					pedido.cdSetor = null;
					cbClienteSetorOrigemClick();
				} else if (event.target == cbClienteSetor) {
					Pedido pedido = getPedido();
					pedido.cdSetor = null;
					cbClienteSetorOrigemChange(cbClienteSetor.getCondicaoPagamento());
					cbCondicaoPagamentoChange();
				} else if (event.target == btRelacionarPedido) {
					btRelacionarPedidoClick();
				} else if (event.target == bgTaxaEntrega.bgBoolean) {
					bgTaxaEntregaClick();
				} else if (event.target == bgAjudante.bgBoolean) {
					bgAjudanteClick();
				} else if (event.target == cbCargaPedido) {
					cbCargaPedidoChange();
				} else if (event.target == cbTributacao) {
					cbTributacaoChange();
				} else if (event.target == btIconeRentabilidade) {
					if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
						RelMargemRentabFaixaWindow relMargemRentabFaixaWindow = new RelMargemRentabFaixaWindow(getPedido(), null, getPedido().vlPctMargemRentab);
						relMargemRentabFaixaWindow.popup();
					} else {
						RelRentabilidadeFaixaWindow relRentabilidadeFaixa = new RelRentabilidadeFaixaWindow(null, getPedido());
						relRentabilidadeFaixa.popup();
					}
				} else if (event.target == btIconeComissaoPedido) {
					openComissaoPedidoRepPopUp();
				} else if (event.target == btIconeFaixa) {
					openPesoFaixaPopUp();
				} else if (event.target == btNovaCargaPedido) {
					btNovaCargaPedidoClick();
				} else if (event.target == btListaItensNfe) {
					btListaItensNfeClick();
				} else if (LavenderePdaConfig.usaPctManualAcrescimoCustoNoPedido && event.target == hashComponentes.get(Pedido.NMCOLUNA_CDMOTIVOACRESCIMOCUSTO)) {
					cbAcrescimoCustoValueChange();
				} else if (event.target == btLiberarPedido) {
					if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
						btLiberarPedidoPendenteAlcadaClick();
					} else {
						btLiberarPedidoClick();
					}
				} else if (event.target == btLiberarItensPendentes) {
					btLiberarItensPendentesClick();
				} else if (event.target == btDescontosConcedidos) {
					btDescontosConcedidosClick();
				} else if (event.target == bgGeraCreditoBonificacaoFrete.bgBoolean) {
					bgGeraCreditoFreteValueChange();
				} else if (event.target == bgGeraCreditoBonificacaoCondicao.bgBoolean) {
					bgGeraCreditoCondicaoValueChange();
				} else if (event.target == cbCondicaoNegociacao) {
					cbCondicaoNegociaoChange();
				} else if (event.target == btConsignarPedido) {
					consignaEEnviaPedido();
				} else if (event.target == btDevolucaoConsignacaoPedido) {
					devolvePedidoConsignado();
				} else if (event.target == cbEntrega) {
					cbEntregaClick();
				} else if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && event.target == cbStatusOrcamento) {
					cbStatusOrcamentoClick();
				} else if (event.target == btRecalcularPedido) {
					recalculaPedidoClick();
				} else if (event.target == cbCentroCusto) {
					cbCentroCustoChange();
				} else if (event.target == cbPlataformaVenda) {
					cbPlataformaVendaChange();
				} else if (event.target == cbModoFaturamento) {
					cbModoFaturamentoChange();
				} else if (event.target == btNotaFiscal) {
					new ListNotaFiscalWindow(getPedido()).popup();
				} else if (event.target == btRelacionarCampanhaPublicitaria) {
					btRelacionarCampanhaPublicitariaClick();
				} else if (event.target == ckVinculaCampanhaPublicitaria){
					ckVinculaCampanhaPublicitariaClick();
				} else if (event.target == btQrCodePix) {
					btQrCodePixClick();
				} else if (event.target == btLibParcelaSolAutorizacao) {
					btLibParcelaSolAutorizacaoClick();
				} else if (event.target == cbDivisaoVenda) {
					cbDivisaoVendaChange();
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if ((event.target instanceof Edit) || (event.target instanceof EditMemo)) {
					houveAlteracaoCampos = true;
				}
				if (event.target == edVlDesconto) {
					edVlDescontoFocusOut();
				} else if (event.target == edPctDesconto) {
					edPctDescontoFocusOut();
				} else if (event.target == edVlPctDesconto) {
					edDescontoFocusOut();
				} else if (event.target == edVlPctFreteRep) {
					if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente && LavenderePdaConfig.usaTransportadoraPedido()) {
						calculaRateiroFrete();
					}
				} else if (event.target == edVlPctDescFrete) {
					edVlPctDescFreteFocusOut();
				} else if (event.target == edDescontoCascataManualDescCliente) {
					edDescontoCascataManualDescClienteFocusOut();
				} else if (event.target == edDescontoCascataManual2) {
					edDescontoCascataManualFocusOut2();
				} else if (event.target == edDescontoCascataManual3) {
					edDescontoCascataManualFocusOut3();
				} else if (event.target == edVlManualFrete) {
					edVlManualFreteValueChange();
				} else if ((LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) && event.target == hashComponentes.get(Pedido.NMCOLUNA_DSEMAILSDESTINO)) {
					edDsEmailDestinoChange();
				} else if (event.target == edDescCascataCategoria1) {
					edDescCascataCategoria1Change();
				} else if (event.target == edDescCascataCategoria2) {
					edDescCascataCategoria2Change();
				} else if (event.target == edDescCascataCategoria3) {
					edDescCascataCategoria3Change();
				} else if (event.target == emObservacaoModoFaturamento) {
					emObservacaoModoFaturamentoChange();
				} else if (event.target == edNuParcelaPedido) {
					edNuParcelaPedidoChange();
				} else if (event.target == edVlFreteAdicional) {
					edVlFreteAdicionalValueChange();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listGridBoleto.isEventoClickUnicoDisparado())) {
					carregaDetalhesPedidoBoleto();
				} else if ((event.target instanceof BaseListContainer.Item) && (listGridAtividadePedido.isEventoClickUnicoDisparado())) {
					carregaDetalhesAtividadePedido();
				} else if ((event.target instanceof BaseListContainer.Item) && (listNotaFiscalForm != null && listNotaFiscalForm.getListContainer().isEventoClickUnicoDisparado())) {
					carregaDetalhesNotaFiscal();
				} else if (bgPedidoGondola != null && event.target == bgPedidoGondola.bgBoolean) {
					bgPedidoGondolaClick(true);
				} else if (bgPedidoCritico != null && event.target == bgPedidoCritico.bgBoolean) {
					bgPedidoCriticoClick(true);
				} else if (bgUtilizaRentabilidade != null && event.target == bgUtilizaRentabilidade.bgBoolean) {
					bgUtilizaRentabilidadeValueChange();
				} else if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) {
				 	ButtonGroupBoolean control = getButtonGroupFlEnviaEmail();
					if (control != null && event.target == control.bgBoolean) {
						flEnviaEmailValueChange(control.getValueBoolean(), true);
					}
				}
				break;
			}

			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(BOTAO_HISTORICO_TAB)) {
						historicoTabelaPrecoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CALCULADORA)) {
						(new Calculator()).popup();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_DESBLOQUEAR_PRECO)) {
						liberaPrecoVenda();
					} else if (bmOpcoes.selectedItem.equals(Messages.SALDOBONIFICACAO_LABEL)) {
						bonificacaoSaldoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PREVISAO_DESCONTOS)) {
						previsaoDescontoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_DIFERENCAS)) {
						diferencasPedidoclick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CCC)) {
						btCCClientePorTipo();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_LIST_CCC)) {
						btListCCClientePorTipo();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_FICHA_FINAN)) {
						fichaFinanceiraClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PRODUTOS_RETIRADA)) {
						btProdutosRetiradaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_ULTIMOS_PEDIDOS)) {
						btUltimosPedidosClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_RENTABILIDADE)) {
						rentabilidadeClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PESQUISA_MERCADO)) {
						btPesquisaMercadoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PESQUISA_MERCADO_PRODUTO_CONCORRENTE)) {
						btPesquisaMercadoProdutoConcorrenteClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_TROCA)) {
						btTrocaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_DESCONTO)) {
						btVerbaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_RELCOMISSAOPEDIDO)) {
						RelComissaoPedidoWindow rel = new RelComissaoPedidoWindow(getPedido());
						rel.popup();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CONTACORRENTECLI)) {
						btExtratoCCClienteClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_FOTOPEDIDO)) {
						btFotoPedidoClick();
					} else if (bmOpcoes.selectedItem.equals(MENU_SUBSTITUICAO_TRIBUTARIA)) {
						new RelSubstituicaoTributariaWindow(getPedido()).popup();
					} else if (bmOpcoes.selectedItem.equals(MENU_DESBLOQUEAR_LIMITADOR)) {
						btDesbloquearLimitadorClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_IMPRIMIR)) {
						btImprimirPedidoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_NFE)) {
						btImprimirNfeClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_NFCE)) {
						btImprimirNfceClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_NFE_CONTINGENCIA)) {
						btImprimirNfeContingenciaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PESO_GRUPO_PRODUTO)) {
						btPesoGrupoProdClick();
					} else if (bmOpcoes.selectedItem.equals(MENU_INFO_TRIBUTARIA_DETALHADA)) {
						new RelInfoTributariaDoPedidoWindow(getPedido()).popup();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_VISITA_FOTO)) {
						btFotoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_REPLICAR_PEDIDO)) {
						btReplicarPedidoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_SUGESTAO_ITENS_RENTABILIDADE_IDEAL)) {
						openPopUpItensRentabilidadeManual = true;
						ListSugestaoItensRentabilidadeIdealWindow listSugestaoItensRentabilidadeIdeal = new ListSugestaoItensRentabilidadeIdealWindow(getPedido(), false, true);
						listSugestaoItensRentabilidadeIdeal.cadPedidoForm = this;
						listSugestaoItensRentabilidadeIdeal.singleClickOn = isEditing() && isEnabled() && getPedido().isPedidoAberto();
						listSugestaoItensRentabilidadeIdeal.popup();

					} else if (bmOpcoes.selectedItem.equals(Messages.MENU_OPCAO_DETALHES_VARIAVEIS_DE_CALCULO)) {
						if (getPedido().variavelCalculoList != null) {
							RelVariaveisCalculoWindow relVariaveisCalculoWindow = new RelVariaveisCalculoWindow(getPedido().variavelCalculoList);
							relVariaveisCalculoWindow.popup();
						} else {
							UiUtil.showInfoMessage(Messages.MSG_LISTA_VARIAVEIS_VAZIA);
						}
					} else if (bmOpcoes.selectedItem.equals(BOTAO_DETALHES_CALCULOS)) {
						btDetalhesCalculosClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_GERAR_PDF)) {
						btGerarPdfClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_GERAR_PDF_VIA_CLIENTE)) {
						btGerarPdfViaClienteClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_REGISTRAR_CHEGADA)) {
						if (CadClienteMenuForm.btRegistrarAtualizarChegadaClick(null)) {
							addItensOnButtonMenu();
							if (cadClienteMenuForm != null) {
								cadClienteMenuForm.remontaMenuFuncionalidades();
							}
						}
					} else if (bmOpcoes.selectedItem.equals(BOTAO_REGISTRAR_SAIDA)) {
						if (CadClienteMenuForm.btRegistrarAtualizarSaidaClick()) {
							addItensOnButtonMenu();
							if (cadClienteMenuForm != null) {
								cadClienteMenuForm.remontaMenuFuncionalidades();
							}
						}
					} else if (bmOpcoes.selectedItem.equals(BOTAO_IMPRIMIR_BOLETO)) {
						btImprimirBoletoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_BOLETO_PDF)) {
						btImprimirBoletoPDFClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CONFIGURACAO_DESCONTOS)) {
						btConfiguracaoDescontoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_BONIFICACAO_RELACIONADA)) {
						btBonificacaoRelacionadaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_VENDA_RELACIONADA)) {
						btBonificacaoRelacionadaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CANCELAR_PEDIDO)) {
						btCancelarPedidoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_REL_DESCONTOS)) {
						btRelDescontosClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_REL_LIBERACOES_SENHA)) {
						btRelLiberacoesSenhaClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_FAIXAS_DESC_PESO)) {
						btFaixasDescPesoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_ANEXAR_DOC)) {
						btAnexarDocClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_COTAS_COND_PAGTO)) {
						btCotasCondPagtoClick();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_PRODUTO_DESEJADO)) {
						btProdutoDesejadoClick();
					} else if (bmOpcoes.selectedItem.equals(FrameworkMessages.BOTAO_EXCLUIR)) {
						excluirClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BT_WORKFLOW)) {
						btWorkflowClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_CANCELAR_CONSIGNACAO)) {
						btCancelarConsignacaoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.PRODUTOCREDITODESCONTO_TITULO)) {
						btProdutoCreditoDescontoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_CONSIGNACAO)) {
						btImprimirPedidoConsignacaoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_DESCONTO_VENDAS_MES)) {
						if (LavenderePdaConfig.isConfigColunasDescontoVolumeVendaMensalDesligado()) {
							UiUtil.showErrorMessage(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_CONFIG_COLUNAS_EXCEPTION);
						} else {
							ListDescontoVendaVolumeMensal listDescontoVendaVolumeMensal = new ListDescontoVendaVolumeMensal(getPedido());
							listDescontoVendaVolumeMensal.popup();
						}
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_NFCE) || bmOpcoes.selectedItem.equals(Messages.BOTAO_REIMPRESSAO_NFCE)) {
						btImprimirNfceClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IGNORAR_VALIDACOES)) {
						btIgnoraValidacaoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRIMIR_PROMISSORIA)) {
						btImprimirPromissoriaClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_METACLIENTE)) {
						boolean abriuTela = abreMetaVendaPorClienteWindow();
						if (!abriuTela) {
							UiUtil.showInfoMessage(Messages.META_VENDA_NENHUMA_META_VIGENTE);
						}
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_HISTLISTATABPRECO)) {
						TabelaPreco tabPreco = getPedido().getTabelaPreco();
						if (tabPreco != null && ValueUtil.isNotEmpty(tabPreco.cdTabelaPreco)) {
							new RelHistListaTabPrecoWindow(getPedido(), tabPreco.cdListaTabelaPreco, tabPreco.cdColunaTabelaPreco, null, null).popup();
						} else {
							UiUtil.showWarnMessage(Messages.PEDIDO_HISTORICO_TABPRECO_VAZIO);
						}
					} else if (bmOpcoes.selectedItem.equals(Messages.GIROPRODUTO_NOME_ENTIDADE)) {
						btGiroProdutoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_COMISSAO_PEDIDO_REP_WINDOW)) {
						openComissaoPedidoRepPopUp();
					} else if (bmOpcoes.selectedItem.equals(Messages.VERBA_NOME_ENTIDADE)) {
						if (getPedido().isPedidoCritico()) {
							UiUtil.showWarnMessage(Messages.PEDIDOCRITICO_AVISO_VERBA_GRUPO);
						} else if (getPedido().isTipoFreteFob() && LavenderePdaConfig.usaValidaConversaoFOB()) {
							UiUtil.showWarnMessage(Messages.PEDIDO_FOB_AVISO_VERBA_GRUPO);
						} else {
							btMenuVerbaClick();
						}
					} else if (bmOpcoes.selectedItem.equals(Messages.MARGEMCONTRIBUICAO_TITLE_WINDOW)) {
						new RelMargemContribFaixaWindow(getPedido()).popup();
					} else if (bmOpcoes.selectedItem.equals(Messages.OBSERVACAO_CLIENTE)) {
						exibePopupObservacaoCliente(getPedido().getCliente());
					} else if (bmOpcoes.selectedItem.equals(Messages.CLIENTE_LABEL_SLIDEFOTOS)) {
						btFotosClienteClick(getPedido().getCliente());
					} else if (bmOpcoes.selectedItem.equals(Messages.SUGESTAO_COMBO_PRODUTO)) {
						btNovoItemClick(false);
						new ListItemComboSugestaoWindow(getPedido(), null, null, null, false, getCdTabelaPreco()).popup();
						afterCrudItemPedido();
					} else if (bmOpcoes.selectedItem.equals(Messages.REL_ITENS_PENDENTES_TITULO)) {
						if (isPedidoPossuiItemPendente()) {
							new RelMotivoPendenciaWindow(getPedido()).popup();
						} else if (isPedidoPendenteByBonificaoVendaRelacionada()) {
							new RelPendenciaBonificaoVendaRelacionada(getPedido()).popup();
						}
					} else if (bmOpcoes.selectedItem.equals(Messages.SOL_AUTORIZACAO_PEDIDO)) {
						new ListSolAutorizacaoWindow(getPedido(), null).popup();
					} else if (bmOpcoes.selectedItem.equals(Messages.CAD_PROD_CLI_COD_MENU)) {
						new ListProdutoClienteCodWindow(getPedido().getCliente(), getPedido()).popup();
					} else if (bmOpcoes.selectedItem.equals(BOTAO_ADICIONAR_KIT)) {
						btNovoItemClick(false);
						ListItemKitForm listItemKitForm = new ListItemKitForm(getPedido());
						listItemKitForm.setCadPedidoForm(this);
						show(listItemKitForm);
					} else if (bmOpcoes.selectedItem.equals(Messages.DESC_PROG_PEDIDO_BUTTON)) {
						btDescProgressivoConfigClick(getPedido());
					} else if (bmOpcoes.selectedItem.equals(Messages.MENU_CAD_PEDIDO_CONVERTER_TIPO_PEDIDO)) {
						btConverterTipoPedidoClick(getPedido());
					} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_RESERVAR_PRODUTO_ESTOQUE)){
						reservarEstoque();
					} else if (bmOpcoes.selectedItem.equals(Messages.REQUISICAO_SERV_NOME_ENTIDADE)) {
						btRequisicaoServClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.TITULO_POLITICAS_BONIFICACAO)) {
						btPoliticasBonificacaoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.MENU_OPCAO_GERAR_PDF_CATALOGO_ITENS)) {
						btGerarPdfCatalogoPedidoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.GERAR_CATALOGO)) {
						btGerarCatalogoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.CATALOGO_EXTERNO)) {
						btCatalogoExternoClick();
					} else if (bmOpcoes.selectedItem.equals(Messages.ATENDIMENTOHIST_TITLE)) {
						WindowUtil.btAtendimentoHistClick(this, getPedido().cdEmpresa, getPedido().cdCliente);
					} else if (bmOpcoes.selectedItem.equals(BOTAO_CONVERTER_ORCAMENTO_PARA_PEDIDO)) {
						btConverterOrcamentoParaPedidoClick();
					}
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if ((event.target == edVlPctDescItem) && edVlPctDescItem.isEditable()) {
					edVlPctDescontoItemValueChange();
				} else if ((event.target == edVlPctAcrescimoItem) && edVlPctAcrescimoItem.isEditable()) {
					edVlPctAcrescimoItemValueChange();
				} else if (event.target == cbEnderecoEntrega) {
					getPedido().cdEnderecoCliente = cbEnderecoEntrega.getValue();
					houveAlteracaoCampos = true;
					validaDataEntrega();
				} else if (event.target == cbEnderecoCobranca) {
					getPedido().cdEnderecoCobranca = cbEnderecoCobranca.getValue();
					houveAlteracaoCampos = true;
				} else if (event.target == edDtEntrega) {
					validaDataEntrega();
				} else if (event.target == edVlManualFrete) {
					edVlManualFreteValueChange();
				} else if (event.target == edDtPagamento) {
					edDtPagamentoChange();
				} else if (event.target == edDtEntregaManual) {
					dtEntregaManualChange();
				} else if (event.target == cbCondicaoPagamento) {
					if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) cbCondicaoPagamentoChange();
				} else if (event.target == edVlPctVpc) {
					edVlPctVpcChange();
				} else if (event.target == edVlPctDescCondicao) {
					validaECalculaVlPctDescCondicaoAfterChange();
				}
				if (LavenderePdaConfig.usaPctManualAcrescimoCustoNoPedido && event.target == hashComponentes.get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO)) {
					edAcrescimoCustoValueChange();
				}
				if (PedidoService.getInstance().obrigaIndicarClienteEntrega(getPedido())) {
					PopUpSearchFilterDyn popUpClienteEntrega = PedidoUiUtil.getPopUpSearchClienteEntrega(hashComponentes);
					if (popUpClienteEntrega != null && event.target == popUpClienteEntrega.edText) {
						popUpSearchCliEntregaChange();
					}
				}
				break;
			}
		}
	}


	private void btQrCodePixClick() throws SQLException {
		QrCodeWindow qrCodeWindow = new QrCodeWindow(Messages.LABEL_PIX, getPedido().dsQrCodePix, null);
		qrCodeWindow.popup();
	}

	private void btRequisicaoServClick() throws SQLException {
		Pedido pedido = getPedido();
		CadRequisicaoServForm cadRequisicaoServForm = new CadRequisicaoServForm(pedido.nuPedido, pedido.flOrigemPedido, pedido.cdCliente);
		cadRequisicaoServForm.add();
		show(cadRequisicaoServForm);
	}

	private void reservarEstoque() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			Pedido pedido = getPedido();
			PedidoService.getInstance().controleReservarEstoque(pedido);
			UiUtil.showSucessMessage(Messages.MSG_SUCESSO_PEDIDO_PRODUTO_RESERVADO);
		} finally {
			mb.unpop();
		}
	}

	private void btDescProgressivoConfigClick(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		if (cliente != null) {
			DescProgressivoConfig descProgressivoConfigFilter = new DescProgressivoConfig();
			descProgressivoConfigFilter.cdEmpresa = cliente.cdEmpresa;
			descProgressivoConfigFilter.cdRepresentante = cliente.cdRepresentante;
			descProgressivoConfigFilter.cliente = cliente;
			int qtRegistros = DescProgressivoConfigService.getInstance().countByCliente(descProgressivoConfigFilter);
			if (qtRegistros >= 1) {
				if ((!isEditing() || houveAlteracaoCampos) && pedido.isPedidoAberto()) {
					save();
					edit(pedido);
					houveAlteracaoCampos = false;
				}
				show(new ListDescontoProgressivoForm(cliente, this, false));
			} else {
				UiUtil.showWarnMessage(Messages.DESC_PROG_CONFIG_SEM_REGISTROS);
			}
		}
	}

	private void btFotosClienteClick(Cliente cliente) throws SQLException {
		new ImageSliderClienteWindow(cliente).popup();
	}

	private void btCatalogoExternoClick() {
		new CatalogoExternoWindow().popup();
	}

	private void bgPedidoCriticoClick(boolean updateOtherButton) {
		try {
			if (updateOtherButton) {
				updateOtherButtonGroupBoolean(bgPedidoCritico);
			}
			Pedido pedido = getPedido();
			pedido.flCritico = bgPedidoCritico.getValue();
			addItensOnButtonMenu();
			if (ValueUtil.isEmpty(pedido.nuPedido)) return;
			if (LavenderePdaConfig.isExibeComboMenuInferior()) {
				addItensOnButtonMenu();
			}
		} catch (ApplicationException appEx) {
			// Pedido ainda não foi salvo;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void repositionTabDinamica() {
		try {
			if (tabDinamica.getActiveTab() == -1) return;
			tabDinamica.reposition();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	protected void exibePopupObservacaoCliente(Cliente cliente) throws SQLException {
		if (!LavenderePdaConfig.mostraObservacaoCliente() || !getPedido().isPedidoAberto() || ValueUtil.isEmpty(cliente.dsObservacao)) return;

		ObservacaoClienteWindow observacaoClienteWindow = new ObservacaoClienteWindow(cliente.dsObservacao);
		observacaoClienteWindow.popup();
	}

	protected boolean exibirPopupFrete() throws SQLException {
		return exibirPopupFrete(false);
	}

	protected boolean exibirPopupFrete(boolean inicioPedido) throws SQLException {
		Pedido pedido = getPedido();
		boolean freteAplicado = false;
		if (!inicioPedido && pedido.pedidoComFretePersonalizadoManual()) {
			freteAplicado = exibirCadFreteManual();
		} else {
			freteAplicado = exibirPopupFretePersonalizado(pedido);
		}
		if (!freteAplicado) return false;
		if (!inicioPedido) {
			try {
				PedidoService.getInstance().calculate(pedido);
				PedidoPdbxDao.getInstance().update(pedido);
			} catch(SQLException ee) {
				ExceptionUtil.handle(ee);
			} catch (ApplicationException e) {
				ExceptionUtil.handle(e);
			}
		}
		updateVlTotalPedido();
		updateUiFretePersonalizado(pedido);
		return freteAplicado;
	}

	private boolean exibirPopupFretePersonalizado(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoPedido != null && tipoPedido.isIgnoraCalculoFrete()) return true;
		ListFreteWindow listFreteWindow = new ListFreteWindow(pedido);
		listFreteWindow.popup();
		return listFreteWindow.freteAplicado;
	}

	private boolean exibirCadFreteManual() throws SQLException {
		CadFreteManualForm cadFrete = new CadFreteManualForm(getPedido());
		cadFrete.popup();
		return cadFrete.adicionadoFreteManual;
	}

	private void openComissaoPedidoRepPopUp() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.cdComissaoPedidoRep == 0 || pedido.comissaoPedidoRep == null) {
			UiUtil.showInfoMessage(Messages.NENHUMA_COMISSAO_ATINGIDA);
		} else {
			new RelComissaoPedidoRepWindow(getPedido(), null).popup();
		}
	}

	private void openPesoFaixaPopUp() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.pesoFaixa == null) {
			UiUtil.showInfoMessage(Messages.NENHUMA_PESO_FAIXA_ATINGIDA);
		} else {
			new RelPesoFaixaWindow(getPedido()).popup();
		}
	}

	private void edVlDescontoFocusOut() throws SQLException {
		boolean updateVlTotalPedido = false;
		try {
			Pedido pedido = getPedido();
			if (edVlDesconto.getValueDouble() > 0 && pedido.vlPctDesconto > 0 && UiUtil.showConfirmYesNoMessage(Messages.MSG_CONFIRM_RETIRAR_DESCPCT)) {
				edPctDesconto.setValue(0);
				pedido.vlPctDesconto = edPctDesconto.getValueDouble();
				updateVlTotalPedido = true;
			} else if (edVlDesconto.getValueDouble() > 0 && pedido.vlPctDesconto > 0) {
				edVlDesconto.setValue(0);
				return;
			}
			if (pedido.vlTotalItens != 0) {
				double pctMaxDesconto = LavenderePdaConfig.aplicaDescontoNaCapaDoPedido;
				double newVlTotalPedido = pedido.vlTotalItens - edVlDesconto.getValueDouble();
				double pctDesconto = 100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens);
				pctDesconto = ValueUtil.round(pctDesconto);
				if (pctDesconto > ValueUtil.round(pctMaxDesconto)) {
					try {
						throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(pctMaxDesconto) + "%" }));
					} finally {
						edVlDesconto.setValue(pedido.vlDesconto);
					}
				} else {
					pedido.vlDesconto = edVlDesconto.getValueDouble();
					getPedidoService().calculate(pedido);
					updateVlTotalPedido = true;
				}
			} else {
				if (edVlDesconto.getValueDouble() > 0) {
					edVlDesconto.setValue(0);
					throw new ValidationException(Messages.MSG_NAO_POSSUI_ITEM_PEDIDO);
				}
			}
		} finally {
			if (updateVlTotalPedido) {
				updateVlTotalPedido();
			}
		}
	}

	private void edPctDescontoFocusOut() throws SQLException {
		boolean updateVlTotalPedido = false;
		try {
			Pedido pedido = getPedido();
			if (edPctDesconto.getValueDouble() > 0 && pedido.vlDesconto > 0 && UiUtil.showConfirmYesNoMessage(Messages.MSG_CONFIRM_RETIRAR_DESCVALOR)) {
				edVlDesconto.setValue(0);
				pedido.vlDesconto = edVlDesconto.getValueDouble();
				updateVlTotalPedido = true;
			} else if (edPctDesconto.getValueDouble() > 0 && pedido.vlDesconto > 0) {
				edPctDesconto.setValue(0);
				return;
			}
			if (pedido.vlTotalItens != 0) {
				double pctMaxDesconto = LavenderePdaConfig.aplicaDescontoNaCapaDoPedido;
				double pctDesconto = ValueUtil.round(edPctDesconto.getValueDouble());
				if (pctDesconto > ValueUtil.round(pctMaxDesconto)) {
					try {
						throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(pctMaxDesconto) + "%" }));
					} finally {
						edPctDesconto.setValue(pedido.vlPctDesconto);
					}
				} else {
					pedido.vlPctDesconto = edPctDesconto.getValueDouble();
					getPedidoService().calculate(pedido);
					updateVlTotalPedido = true;
				}
			} else {
				if (edPctDesconto.getValueDouble() > 0) {
					edPctDesconto.setValue(0);
					throw new ValidationException(Messages.MSG_NAO_POSSUI_ITEM_PEDIDO);
				}
			}
		} finally {
			if (updateVlTotalPedido) {
				updateVlTotalPedido();
			}
		}
	}

	private void btProdutoCreditoDescontoClick() throws SQLException {
		ListInfoProdutoCreditoDescontoWindow listInfoProdutoCreditoDescontoWindow = new ListInfoProdutoCreditoDescontoWindow(getPedido(), getCdTabelaPreco());
		listInfoProdutoCreditoDescontoWindow.popup();
	}

	private String getCdTabelaPreco() throws SQLException {
		return TabelaPrecoService.getInstance().getCdTabelaPreco(getPedido());
	}

	private void btProdutosRetiradaClick() {
		ListProdutoRetiradaWindow listProdutoRetiradaWindow = new ListProdutoRetiradaWindow(SessionLavenderePda.getCliente());
		listProdutoRetiradaWindow.popup();
	}

	private void btFaixasDescPesoClick() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		ListDescQuantidadePesoWindow listDescQuantidadePesoWindow;
		try {
			if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
				listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow(getPedido());
			} else {
				listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow();
			}
			listDescQuantidadePesoWindow.setDefaultRect();
		} finally {
			mb.unpop();
		}
		listDescQuantidadePesoWindow.popup();
	}

	private void btAnexarDocClick() {
		try {
			Pedido pedido = getPedido();
			saveAndEditPedido();
			LavendereFileChooserBoxUtil fileChooserBoxUtil = new LavendereFileChooserBoxUtil(GroupTypeFile.ALL, DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido, pedido.isPedidoFechado(), (isEditing() || pedido.isPedidoFechado()), pedido.docAnexoList);
			fileChooserBoxUtil.showListDocumentoAnexo();

		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void btFotoPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		ImageSliderPedidoWindow imageSliderPedidoWindow = new ImageSliderPedidoWindow(pedido, !pedido.isPedidoAberto() || isEditing());
		imageSliderPedidoWindow.popup();
	}

	private void validaDataEntrega() throws SQLException {
		Pedido pedido = getPedido();
		Date dtEntregaOld = pedido.dtEntrega;
		try {
			if (LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente() && ValueUtil.isNotEmpty(edDtEntrega.getValue())) {
				Date dataEntregaSelecionada = edDtEntrega.getValue();
				edDtEntrega.setValue(RotaEntregaService.getInstance().getSugestaoDataEntregaBaseadaEmUmaRota(edDtEntrega.getValue(), pedido.getCliente()));
				boolean exibeMensagem = true;
				pedido.dtEntrega = edDtEntrega.getValue();
				if (!validaEstoquePrevisto()) {
					edDtEntrega.setValue(dtEntregaOld);
					pedido.dtEntrega = dtEntregaOld;
					exibeMensagem = false;
				}
				if (!ValueUtil.valueEquals(dataEntregaSelecionada, edDtEntrega.getValue()) && exibeMensagem) {
					UiUtil.showInfoMessage(Messages.ROTA_ENTREGA_DATA_ALTERADA);
				}
			}

			if (ValueUtil.VALOR_NI.equals(edDtEntrega.getText()) || edDtEntrega.getValue() != null) {
				pedido.dtEntrega = edDtEntrega.getValue();
			} else {
				edDtEntrega.setValue(pedido.dtEntrega);
			}
			PedidoService.getInstance().validateDataEntrega(pedido, false);
		} catch (LiberacaoDataEntregaPedidoException e) {
			if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
				if (PedidoUiUtil.isLiberaSenhaDiaEntregaPedidoWindow(pedido, e.getMessage())) {
					pedido.dtEntregaLiberada = pedido.dtEntrega;
					UiUtil.showInfoMessage(Messages.PEDIDO_MSG_SUCESSO_LIBERACAO_SENHA_DATA_ENTREGA);
				} else {
					pedido.dtEntrega = dtEntregaOld;
					edDtEntrega.setValue(dtEntregaOld);
					return;
				}
			} else {
				throw e;
			}
		} catch (ValidationException e) {
			throw e;
		}
	}

	private boolean validaEstoquePrevisto() throws SQLException {
		Vector erroEstoqueInsuficienteList = EstoquePrevistoService.getInstance().getErroEstoqueInsuficienteList(getPedido());
		if (ValueUtil.isEmpty(erroEstoqueInsuficienteList)) return true;

		RelNotificacaoItemWindow relProdutoErroWindow = new RelNotificacaoItemWindow(Messages.ESTOQUE_PREVISTO_RELATORIO_ALTERAR_DATA_ENTREGA_PEDIDO, erroEstoqueInsuficienteList, false);
		relProdutoErroWindow.popup();

		return false;
	}

	private ButtonGroupBoolean getButtonGroupFlEnviaEmail() {
		Control control = (Control) hashComponentes.get(Pedido.NMCOLUNA_FLENVIAEMAIL);
		if (control != null && control instanceof ButtonGroupBoolean) {
			return (ButtonGroupBoolean) control;
		}
		return null;
	}

	private void cbCargaPedidoChange() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.getCargaPedido() != null) {
			if (CargaPedidoService.getInstance().validaDataValidadeCarga(pedido.getCargaPedido())) {
				UiUtil.showErrorMessage(Messages.CARGAPEDIDO_ERRO_TROCAR_CARGA_PEDIDO_VALIDADE);
				cbCargaPedido.setValue(pedido.cdCargaPedido);
			}
			if (cbCargaPedido.getCargaPedido() != null) {
				if (ValueUtil.isNotEmpty(cbCargaPedido.getCargaPedido().dtEntrega) && pedido.dtEmissao.isAfter(cbCargaPedido.getCargaPedido().dtEntrega)) {
					UiUtil.showErrorMessage(Messages.CARGAPEDIDO_ERRO_CARGA_PEDIDO_EMISSAO_MAIOR_ENTREGA);
					cbCargaPedido.setValue(pedido.cdCargaPedido);
				}
			}
		}
		try {
			if (ValueUtil.isNotEmpty(cbCargaPedido.getValue())) {
				PedidoService.getInstance().validaPesoMaximoCargaPedido(cbCargaPedido.getValue(), pedido.nuPedido, pedido.qtPeso);
				if (ValueUtil.isNotEmpty(cbCargaPedido.getCargaPedido().dtEntrega)) {
					edDtEntrega.setValue(cbCargaPedido.getCargaPedido().dtEntrega);
					edDtEntrega.setEditable(false);
				}
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.CARGAPEDIDO_ERRO_TROCAR_CARGA_PEDIDO_PESO + e.getMessage());
			cbCargaPedido.setValue(pedido.cdCargaPedido);
		}
	}

	private void btNovaCargaPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		CadCargaPedidoWindow cadCargaPedidoWindow = new CadCargaPedidoWindow(pedido.getCliente().cdCliente);
		cadCargaPedidoWindow.popup();
		String cdCargaPedidoAtual = cbCargaPedido.getValue();
		if (cadCargaPedidoWindow.novaCargaCriada) {
			cbCargaPedido.loadCargaPedido(pedido.getCliente().cdCliente, true);
		}
		if (ValueUtil.isNotEmpty(cadCargaPedidoWindow.cadCargaPedidoForm.cdCargaPedido)) {
			cbCargaPedido.setValue(cadCargaPedidoWindow.cadCargaPedidoForm.cdCargaPedido);
		} else {
			cbCargaPedido.setValue(cdCargaPedidoAtual);
		}
	}

	private void btPesoGrupoProdClick() throws SQLException {
		RelPesoGrupoProdPedido relPesoGrupoProdPedido = new RelPesoGrupoProdPedido(getPedido().itemPedidoList);
		relPesoGrupoProdPedido.popup();
	}

	private void edVlPctAcrescimoItemValueChange() throws SQLException {
		Pedido pedido = getPedido();

		edVlPctDescItem.setValue(0);
		pedido.vlPctDescItem = 0;

		double vlPctAcrescimoNovo = edVlPctAcrescimoItem.getValueDouble();
		double vlPctAcrescimoAnterior = pedido.vlPctAcrescimoItem;
		if (ValueUtil.round(vlPctAcrescimoNovo) == ValueUtil.round(pedido.vlPctAcrescimoItem)) return;

		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			pedido.vlPctAcrescimoItem = vlPctAcrescimoNovo;
			return;
		}
		if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_ALTERAR_SUGESTAOACRESCIMO_ITENS)) {
			edVlPctAcrescimoItem.setValue(vlPctAcrescimoNovo);
			return;
		}
		pedido.vlPctAcrescimoItem = vlPctAcrescimoNovo;
		UiUtil.showProcessingMessage();
		try {
			if (getPedidoService().aplicarPctAcrescimoDoPedidoNosItens(pedido)) {
				updateVlTotalPedido();
				validaAcrescimoMaximoAplicado(vlPctAcrescimoNovo, pedido, pedido.itemPedidoList);
			} else {
				setPreviousAcrescimoItemNoPedido(pedido, vlPctAcrescimoAnterior);
				UiUtil.showErrorMessage(Messages.PEDIDO_MSG_APLIC_SUGESTAOACRESCIMO_ERROR);
			}
		} catch (Throwable e) {
			setPreviousAcrescimoItemNoPedido(pedido, vlPctAcrescimoAnterior);
			throw e;
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void validaAcrescimoMaximoAplicado(double vlPctAcrescimoItem, Pedido pedido, Vector itemPedidoList) throws SQLException {
		if (!LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()
				|| LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) return; //validar regra

		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];

			double vlPctAcrescimoMaximo = pedido.vlPctAcrescimoItem;
			if (LavenderePdaConfig.usaPoliticaComercial()) {
				ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
				vlPctAcrescimoMaximo = ItemPedidoService.getInstance().getVlPctAcrescimoPoliticaComercial(pedido.vlPctAcrescimoItem, itemPedido);
			}

			if (vlPctAcrescimoMaximo != 0 && vlPctAcrescimoItem > vlPctAcrescimoMaximo) {
				UiUtil.showConfirmMessage(Messages.ACRESCIMO_PEDIDO_NAO_APLICADO);
				return;
			}
		}
	}

	private void edVlPctDescontoItemValueChange() throws SQLException {
		Pedido pedido = getPedido();

		edVlPctAcrescimoItem.setValue(0);
		pedido.vlPctAcrescimoItem = 0;

		double vlPctDescontoNovo = edVlPctDescItem.getValueDouble();
		if (LavenderePdaConfig.getVlPctMaxDesconto() > 0 && vlPctDescontoNovo > LavenderePdaConfig.getVlPctMaxDesconto()) {
			edVlPctDescItem.setValue(pedido.vlPctDescItem);
			throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(vlPctDescontoNovo) + "%", StringUtil.getStringValueToInterface(LavenderePdaConfig.getVlPctMaxDesconto()) + "%"}));
		}
		if (LavenderePdaConfig.getVlPctMaxDesconto() <= 0 && vlPctDescontoNovo >= 100) {
			edVlPctDescItem.setValue(pedido.vlPctDescItem);
			throw new ValidationException(Messages.ITEMPEDIDO_VALIDACO_PCTDESC_MAIOR_100);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			try {
				// Truncamento desconto
				vlPctDescontoNovo = BigDecimal.valueOf(vlPctDescontoNovo + 0.0001).setScale(LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_FLOOR).doubleValue();
			} catch (ArithmeticException | IllegalArgumentException | InvalidNumberException e) {
				vlPctDescontoNovo = ValueUtil.getDoubleValueTruncated(vlPctDescontoNovo, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
			}
			edVlPctDescItem.setValue(vlPctDescontoNovo);
		}
		double vlPctDescontoAnterior = pedido.vlPctDescItem;
		boolean marcaItemPendenteWorkflow = LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow();
		boolean usaDecisaoOuDescontoCanal = LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli;
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() || usaDecisaoOuDescontoCanal || marcaItemPendenteWorkflow) {
			double pctMaxDesconto = getPctMaxDescSugerido(pedido, usaDecisaoOuDescontoCanal, marcaItemPendenteWorkflow);
			if ((pctMaxDesconto != 0 && !marcaItemPendenteWorkflow || marcaItemPendenteWorkflow) && vlPctDescontoNovo > pctMaxDesconto) {
				try {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(vlPctDescontoNovo) + "%", StringUtil.getStringValueToInterface(pctMaxDesconto) + "%"}));
				} finally {
					edVlPctDescItem.setValue(pedido.vlPctDescItem);
				}
			}
		}
		if (ValueUtil.round(vlPctDescontoNovo) != ValueUtil.round(pedido.vlPctDescItem) && !usaDecisaoOuDescontoCanal) {
			if (pedido.itemPedidoList.size() > 0) {
				if (UiUtil.showConfirmYesNoMessage(LavenderePdaConfig.isAcumulaComDescDoItem() ? Messages.PEDIDO_MSG_ALTERAR_SUGESTAODESC_ITENS_ACUMULA : Messages.PEDIDO_MSG_ALTERAR_SUGESTAODESC_ITENS)) {
					pedido.vlPctDescItem = vlPctDescontoNovo;
					UiUtil.showProcessingMessage();
					try {
						if (getPedidoService().aplicarDescPctDoPedidoNosItens(pedido)) {
							updateVlTotalPedido();
							validaDescontoMaximoAplicado(vlPctDescontoNovo, pedido.itemPedidoList);
							if (pedido.isPendente() && marcaItemPendenteWorkflow) {
								UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MSG_PENDENTE_DESC_SUGERIDO);
							}
							if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && LavenderePdaConfig.isUsaVerba() && Math.abs(pedido.vlVerbaPedido) > 0) {
								UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_CONSUMIDA, pedido.vlVerbaPedido));
							}
						} else {
							setPreviousDescontoItemNoPedido(pedido, vlPctDescontoAnterior);
							UiUtil.showErrorMessage(Messages.PEDIDO_MSG_APLIC_SUGESTAODESC_ERROR);
						}
					} catch (DescontoAcumuladoException e) {
						setPreviousDescontoItemNoPedido(pedido, vlPctDescontoAnterior);
						UiUtil.showErrorMessage(e.getMessage());
					} catch (Throwable e) {
						setPreviousDescontoItemNoPedido(pedido, vlPctDescontoAnterior);
						throw e;
					} finally {
						UiUtil.unpopProcessingMessage();
					}
				} else {
					edVlPctDescItem.setValue(vlPctDescontoNovo);
				}
			} else {
				pedido.vlPctDescItem = vlPctDescontoNovo;
			}
			if (!LavenderePdaConfig.isAcumulaComDescDoItem()) return;

			edVlPctDescItem.setValue(pedido.vlPctDescItem);
		}
	}

	private void validaDescontoMaximoAplicado(double vlPctDescItem, Vector itemPedidoList) throws SQLException {
		if (!LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) return;

		if (LavenderePdaConfig.isAcumulaComDescDoItem()) return;

		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			double vlPctDescontoMaximo = ItemPedidoService.getInstance().getVlPctMaxDescontoItemPedido(itemPedido);
			if (vlPctDescontoMaximo != 0 && vlPctDescItem > vlPctDescontoMaximo) {
				UiUtil.showConfirmMessage(Messages.DESCONTO_PEDIDO_NAO_APLICADO);
				return;
			}
		}
	}

	private void setPreviousDescontoItemNoPedido(Pedido pedido, double vlPctDescontoAnterior) {
		edVlPctDescItem.setValue(vlPctDescontoAnterior);
		pedido.vlPctDescItem = edVlPctDescItem.getValueDouble();
	}

	private void setPreviousAcrescimoItemNoPedido(Pedido pedido, double vlPctAcrescimoAnterior) {
		edVlPctAcrescimoItem.setValue(vlPctAcrescimoAnterior);
		pedido.vlPctAcrescimoItem = edVlPctAcrescimoItem.getValueDouble();
	}

	private double getPctMaxDescSugerido(Pedido pedido, boolean usaDecisaoOuDescontoCanal, boolean usaPendenteWorkflow) throws SQLException {
		double pctMaxDesconto = pedido.getCliente().vlPctMaxDesconto;
		if (usaDecisaoOuDescontoCanal) {
			pctMaxDesconto = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
		} else if (usaPendenteWorkflow) {
			pctMaxDesconto = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(true, true);
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			pctMaxDesconto = ValueUtil.getDoubleValueTruncated(pctMaxDesconto, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
		} else {
			pctMaxDesconto = ValueUtil.round(pctMaxDesconto);
		}
		return pctMaxDesconto;
	}

	private boolean aplicaDescontosAutoEmCascataNaCapaPedidoPorItem() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList) && !getPedidoService().aplicaDescontosAutoEmCascataNaCapaPedidoPorItem(pedido)) {
			return false;
		}
		return true;
	}

	private boolean aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem() throws SQLException {
		Pedido pedido = getPedido();
		return !(ValueUtil.isNotEmpty(pedido.itemPedidoList) && !getPedidoService().aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(pedido));
	}

	private void cbTributacaoChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.getCliente().cdTributacaoCliente = cbTributacao.getValue();
		if (isEditing() && ValueUtil.isNotEmpty(cbTributacao.getValue()) && pedido.itemPedidoList.size() > 0) {
			reloadAndUpdateValoresDoPedido(pedido, false);
		}

	}

	private void btWorkflowClick() throws SQLException {
		RelHistoricoPedidoForm relHistoricoPedidoForm = new RelHistoricoPedidoForm(getPedido());
		show(relHistoricoPedidoForm);
	}

	private void cbClienteSetorOrigemClick() throws SQLException {
		cbClienteSetor.setEnabled(true);
		String cdCondPagto = cbClienteSetorOrigem.getCondicaoPagamento();
		if (cbClienteSetor.size() > 1) {
			cdCondPagto = cbClienteSetor.getCondicaoPagamento();
		}
		cbClienteSetorOrigemChange(cdCondPagto);
		cbCondicaoPagamentoChange();
		loadComboClienteSetor(cbClienteSetorOrigem.getValue());
	}

	private void cbRotaEntregaChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdRotaEntrega = cbRotaEntrega.getValue();
	}

	private void calculaRateiroFrete() throws SQLException {
		if (ValueUtil.round(edVlPctFreteRep.getValueDouble()) > ValueUtil.round(100)) {
			edVlPctFreteRep.setValue(0);
			calculaRateiroFrete();
			throw new ValidationException(Messages.FRETE_PCT_REP_MAIOR_QUE_PERMITIDO);
		}
		Pedido pedido = getPedido();
		getPedidoService().calculateRateioFretePedido(pedido, edVlPctFreteRep.getValueDouble());
		edVlFreteRep.setValue(pedido.vlFreteRepresentante);
		edVlFreteCli.setValue(pedido.vlFreteCliente);

		double vlTotalPedidoFrete = pedido.vlTotalPedido;
		if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
			vlTotalPedidoFrete += pedido.vlFrete - pedido.vlFreteRepresentante;
	}
		lbVlTotalPedidoComFrete.setValue(vlTotalPedidoFrete);
	}

	private void cbClienteSetorOrigemChange(String cdCondicaoPagamento) {
		cbCondicaoPagamento.setValue(cdCondicaoPagamento);
		String value = cbCondicaoPagamento.getValue();
		if (ValueUtil.isEmpty(value)) {
			cbCondicaoPagamento.setValue(SessionLavenderePda.getCliente().cdCondicaoPagamento);
			value = cbCondicaoPagamento.getValue();
			if (ValueUtil.isEmpty(value)) {
				if (ValueUtil.isNotEmpty(ultimaCondPgamentoSelected)) {
					cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
				}
				cbCondicaoPagamento.setEnabled(true);
				cbCondicaoPagamento.setSelectedIndex(0);
			}
		} else {
			cbCondicaoPagamento.setEnabled(false);
		}
	}

	private void bgTaxaEntregaClick() {
		edVlTaxaEntrega.setText("");
		edVlTaxaEntrega.setEnabled(ValueUtil.VALOR_SIM.equals(bgTaxaEntrega.getValue()));
	}

	private void bgAjudanteClick() {
		edQtAjudante.setText("");
		edQtAjudante.setEnabled(ValueUtil.VALOR_SIM.equals(bgAjudante.getValue()));
	}

	//@Override
	public void onFormClose() throws SQLException {
		SessionLavenderePda.clearPedidoProcessandoFechamentoList();
		updateHrFimEmissao();
		//--
		super.onFormClose();
		Pedido pedido = getPedido();
		boolean isFromListPedidoForm = prevContainer instanceof ListPedidoForm;
		if (pedido.isFlOrigemPedidoPda() && !pedido.isPedidoTransmitido() && !isFromListPedidoForm) {
			super.list();
		}
		//--
		if (pedido.isPedidoAberto()) {
			PontoGpsService.getInstance().startColetaGpsPontoEspecificoSistema();
			if (LavenderePdaConfig.enviaInformacoesVisitaOnline && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				NotificacaoPdaService.getInstance().restauraNotificacaoPdaAndSend2Web(PedidoService.getInstance().generateIdGlobal());
			}
		}
		//--
		if (!LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop) {
			getItemPedidoListForm().limpaDadosAoSairPedido();
		}
		//--
		controlaConexaoObrigatoria();
		//--
		if ((LavenderePdaConfig.usaEscolhaEmpresaPedido || LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
			EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
			SessionLavenderePda.cdEmpresaOld = null;
		}
		//--
		if (isFromListPedidoForm) {
			ListPedidoForm listPedidoFormPrev = (ListPedidoForm) prevContainer;
			if (listPedidoFormPrev.inConsultaUltimosPedidos && this.clienteOrigemBase != null) {
				SessionLavenderePda.setCliente(this.clienteOrigemBase);
			}
			voltarEListarMantendoScroll(listPedidoFormPrev);
		} else if (prevContainer instanceof ListClienteForm) {
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				ListClienteForm listClienteForm = (ListClienteForm) prevContainer;
				if (listClienteForm.isAllSelected) {
					SessionLavenderePda.setRepresentante(null);
				}
			}
		}

		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
			if(getBaseCrudListForm() != null && !ValueUtil.valueEquals(getBaseCrudListForm().getClass(), ListPedidoForm.class)) {
				SessionLavenderePda.cdEmpresaOld = null;
				BaseContainer form = new ListPedidosEmAbertoPorEmpresaForm();
				form.setRect(0, 0, FILL, FILL);
				prevContainer = form;
			}
		}

		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && SessionLavenderePda.isUsuarioSupervisor()) {
			DescPromocionalService.getInstance().clearCache();
		}
		if (LavenderePdaConfig.usaReagendamentoAgendaVisita && prevContainer instanceof CadClienteMenuForm) {
			((CadClienteMenuForm) prevContainer).remontaMenuFuncionalidades();
		}
		IndiceClienteGrupoProdService.getInstance().clearCache();
		EstoqueService.getInstance().clearCache();
		TabelaPrecoService.getInstance().clearCache();
		ItemTabelaPrecoService.getInstance().clearCache();
		TributacaoConfigService.getInstance().clearCache();
		TributacaoService.getInstance().clearCache();
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(this, pedido);
			cadItemPedidoForm.invalidateListSugPerson();
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && isEditing() && !pedido.deletadoPelaIntefacePedido) {
			if (!StatusOrcamentoService.getInstance().permiteFechamentoPedido(pedido)) {
				if (pedido.isPedidoAberto() && pedido.isFlOrigemPedidoPda() && !pedido.onFechamentoPedido && pedido.isPedidoAlteradoOrcamento()) {
					if (LavenderePdaConfig.sugereEnvioOrcamentoParaEmpresaECliente()) {
						if (UiUtil.showConfirmYesNoMessage(Messages.STATUSORCAMENTO_MSG_ENVIO_CLIENTE)) {
							pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_SIM);
							pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_ALTERADO_ORCAMENTO;
						} else {
							pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_NAO);
							pedido.flTipoAlteracao = UiUtil.showConfirmYesNoMessage(Messages.STATUSORCAMENTO_MSG_ENVIO_EMPRESA) ? Pedido.FLTIPOALTERACAO_ALTERADO_ORCAMENTO : BaseDomain.FLTIPOALTERACAO_ORIGINAL;
						}
					} else if (LavenderePdaConfig.sugereEnvioOrcamentoParaEmpresa() && !UiUtil.showConfirmYesNoMessage(Messages.STATUSORCAMENTO_MSG_ENVIO_EMPRESA)) {
						pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_ALTERADO_ORCAMENTO;
					} else if (LavenderePdaConfig.sugereEnvioOrcamentoParaCliente() && UiUtil.showConfirmYesNoMessage(Messages.STATUSORCAMENTO_MSG_ENVIO_CLIENTE)) {
						pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_SIM);
						pedido.flTipoAlteracao = Pedido.FLTIPOALTERACAO_ALTERADO_ORCAMENTO;
					}
				}
				if (pedido.isPedidoAberto() && pedido.isFlOrigemPedidoPda()) {
					if (LavenderePdaConfig.naoSugereEnvioOrcamento()) {
						pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_SIM);
					}
					PedidoService.getInstance().updatePedidoOrcamento(pedido);
				}
				if (pedido.isPedidoAlteradoOrcamento()) {
					PedidoUiUtil.enviaPedido(false, false);
				}
			} else if (pedido.isPedidoAberto() && pedido.isFlOrigemPedidoPda()){
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLTIPOALTERACAO", Pedido.FLTIPOALTERACAO_ORIGINAL, Types.VARCHAR);
			}
		}
		// Garante que quando entrar novamente na tela de pedido a aba estará setada novamente para a primeira posiçao
		if (tabDinamica != null) tabDinamica.setActiveTab(0);
		ListItemPedidoForm.invalidateInstance();
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			PoliticaComercialService.getInstance().dropTabelaTemporariaPoliticaComercialPedido();
		}
	}

	private void updateHrFimEmissao() throws SQLException {
		if (controlHrFimEmissao) {
			if (isEditing()) {
				Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(getPedido().getRowKey());
				if (pedido != null) {
					pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
					PedidoPdbxDao.getInstance().update(pedido);
					controlHrFimEmissao = false;
				}
			}
		}
	}

	private void controlaConexaoObrigatoria() throws SQLException {
		if (!SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemEnvioDados) {
			int resp = ConexaoPdaService.getInstance().isEnvioPedidosNecessario();
			if (resp != 0) {
				if (resp != 2 || ConexaoPdaService.getInstance().isNecessarioSolicitarEnviosPedidos()) {
					MainLavenderePda.getInstance().showEnvioDadosObrigatorio(resp);
				}
			}
		}
		int resp = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario();
		if (resp != 0) {
			MainLavenderePda.getInstance().showReceberDadosObrigatorio(resp);
		}
	}

	private void edDescontoFocusOut() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.vlTotalItens > 0) {
			double newVlTotalPedido = 0;
			double pctDesconto = 0;
			double pctMaxDesconto = 0;
			double pctMinDesconto = 0;
			if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0 || LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) {
				if (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) {
					pctMaxDesconto = LavenderePdaConfig.permiteDescontoEmValorPorPedido;
					newVlTotalPedido = pedido.vlTotalItens - edVlPctDesconto.getValueDouble();
					pctDesconto = 100 - ((newVlTotalPedido * 100) / pedido.vlTotalItens);
					pctDesconto = ValueUtil.round(pctDesconto);
				} else {
					pctMaxDesconto = LavenderePdaConfig.permiteDescontoPercentualPorPedido;
					pctDesconto = ValueUtil.round(edVlPctDesconto.getValueDouble());
					newVlTotalPedido = pedido.vlTotalItens - ((pedido.vlTotalItens * pctDesconto) / 100);
					newVlTotalPedido = ValueUtil.round(newVlTotalPedido);
				}
				pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? pedido.getCliente().vlPctMaxDesconto  : pctMaxDesconto;
				if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
					double pctMaxDescontoCanal = CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(new ItemPedido(), pedido.getCliente());
					pctMaxDesconto = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(new ItemPedido(), pctMaxDescontoCanal, pedido.getCliente().vlPctContratoCli);
				}
				if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
					pctMinDesconto = pedido.getCliente().vlPctMinDesconto;
				}
				if (pctDesconto > ValueUtil.round(pctMaxDesconto)) {
					try {
						throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(pctMaxDesconto) + "%" }));
					} finally {
						edVlPctDesconto.setValue(LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? pedido.vlPctDescCliente : pedido.vlDesconto);
					}
				} else if (pctMinDesconto != 0 && pctDesconto < ValueUtil.round(pctMinDesconto)) {
					try {
						throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MINIMO_NAO_ATINGIDO, new String[] { StringUtil.getStringValueToInterface(pctDesconto) + "%", StringUtil.getStringValueToInterface(pctMinDesconto) + "%" }));
					} finally {
						edVlPctDesconto.setValue(LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? pedido.vlPctDescCliente : pedido.vlDesconto);
					}
				} else if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() && LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
					double descontoClienteAnterior = pedido.vlPctDescCliente;
					pedido.vlPctDescCliente = edVlPctDesconto.getValueDouble();
					if (!aplicaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
						edVlPctDesconto.setValue(descontoClienteAnterior);
						pedido.vlPctDescCliente = descontoClienteAnterior;
					} else {
						updateVlTotalPedido();
					}
				} else {
					if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
						pedido.vlPctDescCliente = edVlPctDesconto.getValueDouble();
					} else {
						pedido.vlDesconto = edVlPctDesconto.getValueDouble();
					}
					if (!LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
						getPedidoService().calculate(pedido);
						updateVlTotalPedido();
					}
				}
			}
		} else if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo()) {
			if (edVlPctDesconto.getValueDouble() > ValueUtil.round(pedido.getCliente().vlPctMaxDesconto)) {
				try {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MAXIMO_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(edVlPctDesconto.getValueDouble()) + "%", StringUtil.getStringValueToInterface(pedido.getCliente().vlPctMaxDesconto) + "%" }));
				} finally {
					edVlPctDesconto.setValue(pedido.vlPctDescCliente);
				}
			} else if (pedido.getCliente().vlPctMinDesconto != 0 && edVlPctDesconto.getValueDouble() < ValueUtil.round(pedido.getCliente().vlPctMinDesconto)) {
				try {
					throw new ValidationException(MessageUtil.getMessage(Messages.DESCONTO_MSG_MINIMO_NAO_ATINGIDO, new String[] { StringUtil.getStringValueToInterface(edVlPctDesconto.getValueDouble()) + "%", StringUtil.getStringValueToInterface(pedido.getCliente().vlPctMinDesconto) + "%" }));
				} finally {
					edVlPctDesconto.setValue(LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() ? pedido.vlPctDescCliente : pedido.vlDesconto);
				}
			} else {
				pedido.vlPctDescCliente = edVlPctDesconto.getValueDouble();
				edVlPctDesconto.setValue(edVlPctDesconto.getValueDouble());
			}
		} else if (edVlPctDesconto.getValueDouble() > 0) {
			try {
				throw new ValidationException(Messages.DESCONTO_MSG_INVALIDO_SEM_ITENS);
			} finally {
				edVlPctDesconto.setValue(pedido.vlDesconto);
			}
		}
	}

	private void edVlPctDescFreteFocusOut() throws SQLException {
		Pedido pedido = getPedido();
		double descontoFreteAnterior = pedido.vlPctDescFrete;
		double descontoFrete = edVlPctDescFrete.getValueDouble();
		if (descontoFreteAnterior == descontoFrete) {
			return;
		}
		TipoFrete tipoFrete = pedido.getTipoFrete();
		if (tipoFrete != null && descontoFrete > ValueUtil.round(tipoFrete.vlPctMaxDesconto)) {
			edVlPctDescFrete.setValue(descontoFreteAnterior);
			Object [] params = {StringUtil.getStringValueToInterface(descontoFrete), StringUtil.getStringValueToInterface(tipoFrete.vlPctMaxDesconto)};
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_TIPOFRETE_DESCONTO_MAXIMO_ULTRAPASSADO, params));
		}
		pedido.vlPctDescFrete = edVlPctDescFrete.getValueDouble();
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			if (!aplicaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
				edVlPctDescFrete.setValue(descontoFreteAnterior);
				pedido.vlPctDescFrete = descontoFreteAnterior;
			} else {
				updateVlTotalPedido();
			}
		} else {
			getPedidoService().calculate(pedido);
			updateVlTotalPedido();
		}
	}

	private void validaECalculaVlPctDescCondicaoAfterChange() throws SQLException {
		validaECalculaVlPctDescCondicaoAfterChange(getPedido().vlPctDescontoCondicao, edVlPctDescCondicao.getValueDouble(), false);
	}

	private void validaECalculaVlPctDescCondicaoAfterChange(double descontoCondicaoAnterior, double descontoCondicao, boolean isAlterandoCondicaoPagto) throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			try {
				pedido.vlPctDescontoCondicao = descontoCondicao;
				validaDescontoPorCondicaoPagamento(pedido, descontoCondicao);
			} catch (ValidationException e) {
				edVlPctDescCondicao.setValue(descontoCondicaoAnterior);
				pedido.vlPctDescontoCondicao = descontoCondicaoAnterior;
				throw e;
			}
		}
		if (!LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			aplicaDescontosAutoEmCascataNaCapaPedidoPorItem();
			updateVlTotalPedido();
			return;
		}
		if (descontoCondicaoAnterior == descontoCondicao) {
			return;
		}
		if (descontoCondicao > ValueUtil.round(pedido.getCondicaoPagamento().vlPctDescontoTotalPedido)) {
			edVlPctDescCondicao.setValue(descontoCondicaoAnterior);
			if (isAlterandoCondicaoPagto) {
				pedido.vlPctDescontoCondicao = pedido.getCondicaoPagamento().vlPctDescontoTotalPedido;
				getPedidoService().calculate(pedido);
				updateVlTotalPedido();
			}
			Object[] params = {StringUtil.getStringValueToInterface(descontoCondicao), StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento().vlPctDescontoTotalPedido)};
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_CONDICAO_PAGAMENTO_DESCONTO_MAXIMO_ULTRAPASSADO, params));
		}
		pedido.vlPctDescontoCondicao = edVlPctDescCondicao.getValueDouble();
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
			if (!aplicaDescontosAutoEmCascataNaCapaPedidoPorItem()) {
				edVlPctDescCondicao.setValue(descontoCondicaoAnterior);
				pedido.vlPctDescontoCondicao = descontoCondicaoAnterior;
			} else {
				updateVlTotalPedido();
			}
		} else {
			getPedidoService().calculate(pedido);
			updateVlTotalPedido();
		}
	}

	private void validaDescontoPorCondicaoPagamento(Pedido pedido, double descontoCondicaoDigitado) throws SQLException {
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			double vlIndice = CondTipoPagtoService.getInstance().getVlIndice(pedido);
			double vlPctDescontoCondicaoTabela = ValueUtil.round((1.0 - vlIndice) * 100.0);
			if (descontoCondicaoDigitado > vlPctDescontoCondicaoTabela) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_DESCONTO_MAIOR_QUE_MAXIMO_PERMITIDO, vlPctDescontoCondicaoTabela));
			}
		}
	}

	private void edDescontoCascataManualFocusOut2() throws SQLException {
		Pedido pedido = getPedido();
		double vlPctDescontoCondicaoOld = pedido.vlPctDescontoCondicao;
		double vlPctDescontoCondicao = edDescontoCascataManual2.getValueDouble();
		if (vlPctDescontoCondicaoOld == vlPctDescontoCondicao) {
			return;
		}
		pedido.vlPctDescontoCondicao = vlPctDescontoCondicao;
		if (aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			updateVlTotalPedido();
		} else {
			edDescontoCascataManual2.setValue(vlPctDescontoCondicaoOld);
			pedido.vlPctDescontoCondicao = vlPctDescontoCondicaoOld;
		}
	}

	private void edDescontoCascataManualFocusOut3() throws SQLException {
		Pedido pedido = getPedido();
		double vlPctDescFreteOld = pedido.vlPctDescFrete;
		double vlPctDescFrete = edDescontoCascataManual3.getValueDouble();
		if (vlPctDescFreteOld == vlPctDescFrete) {
			return;
		}
		pedido.vlPctDescFrete = vlPctDescFrete;
		if (aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			updateVlTotalPedido();
		} else {
			edDescontoCascataManual3.setValue(vlPctDescFreteOld);
			pedido.vlPctDescFrete = vlPctDescFreteOld;
		}
	}

	private void edDescontoCascataManualDescClienteFocusOut() throws SQLException {
		Pedido pedido = getPedido();
		double vlPctDescCliOld = pedido.vlPctDescCliente;
		double vlPctDescCli = edDescontoCascataManualDescCliente.getValueDouble();
		if (vlPctDescCliOld == vlPctDescCli) {
			return;
		}
		pedido.vlPctDescCliente = vlPctDescCli;
		if (aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			updateVlTotalPedido();
		} else {
			edDescontoCascataManualDescCliente.setValue(vlPctDescCliOld);
			pedido.vlPctDescCliente = vlPctDescCliOld;
		}
	}

	private void atualizaInfosDinamicasDomain() throws SQLException {
		Pedido pedido = getPedido();
		pedido.getHashValuesDinamicos().put("CDEMPRESA", ValueUtil.isEmpty(pedido.cdEmpresa) ? "" : pedido.cdEmpresa);
		pedido.getHashValuesDinamicos().put("CDCLIENTE", ValueUtil.isEmpty(pedido.cdCliente) ? "" : pedido.cdCliente);
		pedido.getHashValuesDinamicos().put("CDTIPOPAGAMENTO", ValueUtil.isEmpty(pedido.cdTipoPagamento) ? "" : pedido.cdTipoPagamento);
		pedido.getHashValuesDinamicos().put("CDTIPOPEDIDO", ValueUtil.isEmpty(pedido.cdTipoPedido) ? "" : pedido.cdTipoPedido);
		pedido.getHashValuesDinamicos().put("CDCONDICAOPAGAMENTO", ValueUtil.isEmpty(pedido.cdCondicaoPagamento) ? "" : pedido.cdCondicaoPagamento);
		pedido.getHashValuesDinamicos().put("CDTIPOENTREGA", ValueUtil.isEmpty(pedido.cdTipoEntrega) ? "" : pedido.cdTipoEntrega);
		pedido.getHashValuesDinamicos().put("CDTABELAPRECO", StringUtil.getStringValue(pedido.cdTabelaPreco));
		pedido.getHashValuesDinamicos().put("VLTOTALPEDIDO", StringUtil.getStringValue(pedido.vlTotalPedido));
		pedido.getHashValuesDinamicos().put("VLTOTALITENS", StringUtil.getStringValue(pedido.vlTotalItens));
		pedido.getHashValuesDinamicos().put("VLTROCARECOLHER", StringUtil.getStringValue(pedido.vlTrocaRecolher));
		pedido.getHashValuesDinamicos().put("VLTROCAENTREGAR", StringUtil.getStringValue(pedido.vlTrocaEntregar));
		pedido.getHashValuesDinamicos().put("VLTOTALBRUTOITENS", StringUtil.getStringValue(pedido.vlTotalBrutoItens));
		pedido.getHashValuesDinamicos().put("QTPONTOSPEDIDO", StringUtil.getStringValue(pedido.qtPontosPedido));
		pedido.getHashValuesDinamicos().put("VLPCTDESCPROGRESSIVO", StringUtil.getStringValue(pedido.vlPctDescProgressivo));
		if (pedido.getHashValuesDinamicos().exists("CDREPRESENTANTE")) {
			pedido.getHashValuesDinamicos().put("CDREPRESENTANTE", StringUtil.getStringValue(pedido.cdRepresentante));
		}
		if (pedido.getHashValuesDinamicos().exists("VLPCTCOMISSAO")) {
			pedido.getHashValuesDinamicos().put("VLPCTCOMISSAO", StringUtil.getStringValue(pedido.vlPctComissao));

		}
	}

	public void afterCrudItemPedido() throws SQLException {
		Pedido pedido = getPedido();
		updateVlTotalPedido();
		atualizaFrete(this.getPedido(), false);
		//--
		atualizaInfosDinamicasDomain();
		super.domainToScreen(pedido); //Atualiza os campos dinamicos na interface
		//--
		setEnableCombosPrincipais();
		//--
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			generateParcelasPedidoByCondicaoPagamento();
			atualizaListParcelaPedidoForm(pedido);
		}
		//--
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
				lvVlVerbaPedido.setValue(VerbaService.getInstance().getVerbaPersonalizadaPedido(pedido, false));
				lvVlVerbaPedido.reposition();
			} else {
				lvVlVerbaPedido.setValue(pedido.vlVerbaPedido);
			}
			if (LavenderePdaConfig.isMostraFlexPositivoPedido(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
				edVlVerbaPedidoPositiva.setValue(pedido.vlVerbaPedidoPositivo);
			}
		}
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			lvVlVerbaPedido.setValue(pedido.vlVerbaPedido + pedido.vlVerbaPedidoPositivo);
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			lvVlPctIndiceFinCondPagto.setValue(pedido.vlPctIndiceFinCondPagto);
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			lvVlPctDescQuantidadePeso.setValue(pedido.vlPctDescQuantidadePeso);
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			PedidoService.getInstance().calculaValorTotalCreditoCondicao(pedido);
			lvGeraCreditoBonificacaoCondicao.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoCondicao));
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			PedidoService.getInstance().calculaValorTotalCreditoFrete(pedido);
			lvGeraCreditoBonificacaoFrete.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoFrete));
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			setEnabledPopupSearchCliEntrega();
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.setValue(pedido.cdUnidade);
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			lvVlDescontoCondPagto.setValue(PedidoService.getInstance().getVlDescontoPedidoDescontadoIncentivos(pedido));
		}
		refreshComponentsPedidoComplementado();
		reloadComboCondicaoPagamentoFiltrandoValorMinimo();
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			bgPedidoCritico.setEnabled(isEnabled() && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && ValueUtil.isEmpty(pedido.itemPedidoList));
		}
	}

	private void generateParcelasPedidoByCondicaoPagamento() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
		if (pedido.cdCondicaoPagamento != null && !pedido.itemPedidoList.isEmpty() 
				&& !isTipoCondPagtoParceladoUsuarioEmPercentual() && !isTipoCondPagtoParceladoUsuarioEmValores()) {
			pedido.parcelaPedidoList = ParcelaPedidoService.getInstance().geraParcelasAuto(pedido);
		}
	}

	public void cbTabelaPrecoChange() throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaFiltroApenasGruposProdutosExistentesCargaRepresentante && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && AbstractBaseCadItemPedidoForm.instance != null) {
			AbstractBaseCadItemPedidoForm.instance.loadGrupoProdutos(pedido);
		}
		if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco) {
			reloadComboCondicaoPagamento();
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro() && !(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
			changeEntregaPedido(pedido);
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			TabelaPreco tabPreco = pedido.getTabelaPreco();
			boolean enabled = tabPreco == null ? isEnabled() : tabPreco.isPermiteDesconto() && isEnabled();
			edDescCascataCategoria1.setEditable(enabled && pedido.getCliente().vlIndiceFinanceiro < 1);
			refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), enabled);
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			setEditableEdVlPctHistVendas(pedido);
		}
		setValueEdVlMinTabelaPreco(pedido);
		setDtEntregaManualVisible(pedido);
		ultimaTabelaPrecoSelected = cbTabelaPreco.getValue();
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			atualizaIconeComissaoPedido(btIconeComissaoPedido);
		}
		if (LavenderePdaConfig.isUsaTipoPagamentoPorTabelaPreco()) {
			loadComboTipoPagamento(pedido.getCliente());
			setEnableCombosPrincipais();
		}
	}

	private void setEditableEdVlPctHistVendas(Pedido pedido) throws SQLException {
		if (pedido.getTabelaPreco().isBloqueiaDesc2()) {
			edVlPctDescHistoricoVendas.setEditable(false);
			pedido.vlPctDescHistoricoVendas = 0.0;
			return;
		}
		edVlPctDescHistoricoVendas.setEditable(true);
	}

	public void cbTransportadoraChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdTransportadora = cbTransportadora.getValue();
		ultimaTransportadoraSelected = pedido.cdTransportadora;
		if (LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd) {
			atualizaFretePedido(pedido);
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente && LavenderePdaConfig.usaTransportadoraPedido()) {
			if (isEditing()) {
				calculaRateiroFrete();
			}
		}
	}

	private void atualizaFretePedido(Pedido pedido) throws SQLException {
		getPedidoService().calculateFretePedido(pedido, true);
		getPedidoService().calculate(pedido);
		if (isEditing()) {
			PedidoPdbxDao.getInstance().update(pedido);
			updateVlTotalPedido();
		}
	}

	private boolean ignorarRecalculoCondicaoPagamento(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento || ValueUtil.isEmpty(pedido.itemPedidoList)) return false;

		if (pedido.getCondicaoPagamento().isPermiteIgnorarRecalculo()) {
			permiteRecalculo = UiUtil.showConfirmYesNoMessage(Messages.CONFIRMA_RECALCULO_ITENS_INSERIDOS_PEDIDO_IGNORA_RECALCULO);
			pedido.ignoraRecalculoItens = !permiteRecalculo;

			pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
			ultimaCondPgamentoSelected = pedido.cdCondicaoPagamento;
			return false;
		}
		if (UiUtil.showConfirmYesNoMessage(Messages.CONFIRMA_RECALCULO_ITENS_INSERIDOS_PEDIDO_NAO_IGNORA_RECALCULO)) return false;

		cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
		pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
		return true;
	}

	public void cbCondicaoPagamentoChange() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		RecalculoDescontoProgressivoDTO recalculoDescontoProgressivoDTO = null;
		try {
			Pedido pedido = getPedido();
			if (ValueUtil.isNotEmpty(pedido.nuPedido)) {
				resetQtDiasVctoParcelas(pedido);
			}
			double ultimoVlTotalPedidoLiberado = pedido.vlTotalPedidoLiberado;
			int ultimoQtDiasCPgtoLibSenha = pedido.qtDiasCPgtoLibSenha;
			pedido.vlTotalPedidoLiberado = 0;
			pedido.qtDiasCPgtoLibSenha = 0;
			if (ignorarRecalculoCondicaoPagamento(pedido)) return;

			try {
				if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
					getPedidoService().processaTrocaDeCondicaoComercialComDesconto(pedido);
				}
				if (LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
					DescComiFaixaService.getInstance().reloadDescComiItensPedidoNaTrocaCondicao(pedido, ultimaCondPgamentoSelected, cbCondicaoPagamento.getValue());
				}
				if (LavenderePdaConfig.isConfigCalculoPesoPedido() && (LavenderePdaConfig.usaControlePesoPedidoPorCondPagto)) {
					pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
					PedidoService.getInstance().validaPesoMaximoCondicaoPagamento(pedido);
				}
				if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && pedido.itemPedidoList.size() > 0 && !isTipoPagamentoChangeAfterCondicaoPagamentoChange()) {
					ItemPedidoService.getInstance().recalculaDescCascataCondicaoPagamentoChange(pedido);
					updateVlTotalPedido();
				}
				if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(pedido.itemPedidoList) && pedido.isPedidoAberto() && ProdutoService.getInstance().isPossuiFamiliasDescProg(pedido, null)) {
					recalculoDescontoProgressivoDTO = PedidoService.getInstance().atualizaDescProgressivoPedido(pedido);
				}
				if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() && isEditing()) {
					generateAndUpdateParcelasByCondPagto();
				}
				showMensagemPedidoPendenteCondPagto(pedido);
			} catch (ValidationException ex) {
				rollbackTrocaCondPagto(ultimoVlTotalPedidoLiberado, ultimoQtDiasCPgtoLibSenha);
				throw new ValidationException(Messages.MSG_CONDICAO_PAGAMENTO_NAO_ALTERADA + ex.getMessage());
			}
			pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
			if (isEditing() && ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento) && !pedido.cdCondicaoPagamento.equals(pedido.oldCdCondicaoPagamento)) {
				pedido.cdCondicaoPagamentoChanged = true;
			}
			ultimaCondPgamentoSelected = pedido.cdCondicaoPagamento;

			recalculoDeInterpolacao(pedido);
			if (LavenderePdaConfig.usaControlePontuacao) {
				PontuacaoConfigService.getInstance().reloadPontuacaoPedido(pedido, null);
			}

			CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
			if (condicaoPagamento != null && LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !pedido.isIgnoraVlMinPedidoTipoPedido() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
				double qtMinValor = condicaoPagamento.getQtMinValor(pedido.cdTabelaPreco);
				setEdValorMinCondicaoPagamento(qtMinValor);
				setEdPrazoMedio(condicaoPagamento.qtDiasMediosPagamento);
				if (LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente() && pedido.getValorTotalValidadoComImpostos() < qtMinValor) {
					Object[] params = {condicaoPagamento.toString(), StringUtil.getStringValueToInterface(qtMinValor), StringUtil.getStringValueToInterface(pedido.getValorTotalValidadoComImpostos())};
					String message = MessageUtil.getMessage(Messages.PEDIDO_MSG_MINIMOVALORPEDIDO_CONDICAO, params);
					UiUtil.showInfoMessage(message);
				}
			}
			if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio()) {
				setEdPrazoMedio(condicaoPagamento.qtDiasMediosPagamento);
			}
			setEdNuParcelaPedido(condicaoPagamento.nuParcelas, condicaoPagamento.isPermiteEditarParcelas());
			edNuParcelaPedidoChange();
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				reloadComboTabelaPreco();
			}
			if (isTipoPagamentoChangeAfterCondicaoPagamentoChange()) {
				cbTipoPagamento.carregaTipoPagamentos(pedido, false);
				cbTipoPagamento.setValue(pedido.getCliente().cdTipoPagamento);
				if (ValueUtil.isEmpty(cbTipoPagamento.getValue())) {
					cbTipoPagamento.setSelectedIndex(0);
				}
				cbTipoPagamentoChange();
			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
				pedido.vlPctDesconto = condicaoPagamento == null ? 0 : ValueUtil.round(condicaoPagamento.vlPctDescontoTotalPedido);
			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
				if (condicaoPagamento != null) {
					lbVlMaxPctDescCondicao.setValue(condicaoPagamento.vlPctDescontoTotalPedido);
				}
			}
			if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
				setEnableBgGeraCreditoBonificacaoCondicao();
				bgGeraCreditoCondicaoValueChange();
			}
			if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
				setEnableBgGeraCreditoBonificacaoFrete();
				bgGeraCreditoFreteValueChange();
			}
			if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
				cleanInfosBancariasOnCondPagtoChange(pedido);
			}
			if (LavenderePdaConfig.mostraValorParcelaPedido) {
				updateVlTotalPedido();
			}
			reloadComboCondicaoPagamentoFiltrandoValorMinimo();
			if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
				refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
				validaECalculaVlPctDescCondicaoAfterChange(ValueUtil.round(getPedido().getCondicaoPagamento().vlPctDescontoTotalPedido), edVlPctDescCondicao.getValueDouble(), true);
			}
			if (LavenderePdaConfig.usaPoliticaComercial()) {
				PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
			}
		} finally {
			mb.unpop();
			if (recalculoDescontoProgressivoDTO != null && ValueUtil.isNotEmpty(recalculoDescontoProgressivoDTO.listItemDescontoDTO)) {
				new RelDiferencasDescontoProgressivoWindow(recalculoDescontoProgressivoDTO).popup();
			}
		}
	}

	private void resetQtDiasVctoParcelas(Pedido pedido) {
		pedido.qtDiasVctoParcelas = null;
		try {
			PedidoService.getInstance().updateColumn(pedido.rowKey, "qtDiasVctoParcelas", pedido.qtDiasVctoParcelas, Types.VARCHAR);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private void recalculoDeInterpolacao(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaInterpolacaoPrecoProduto) return;
		boolean recalculaPedido = false;
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			PedidoService.getInstance().findItemPedidoList(pedido);
		}
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			recalculaPedido = true;
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			getPedidoService().resetDadosItemPedido(pedido, itemPedido);
			getPedidoService().calculateItemPedido(pedido, itemPedido, true);
		}
		if (!recalculaPedido) return;
		reloadAndUpdateValoresDoPedido(pedido, false);
		updateVlTotalPedido();
	}

	public boolean isTipoPagamentoChangeAfterCondicaoPagamentoChange() {
		return (LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento() || LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente()) && !LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento();
	}

	public void novoItemPedido() throws SQLException {
		PedidoService.getInstance().validateBateria();
		try {
			btNovoItemClick(true);
		} catch (RelacionaPedProducaoException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void btListCCClientePorTipo() throws SQLException {
		ListCCCliPorTipoForm cCClienteCcCliPorTipo = new ListCCCliPorTipoForm(getPedido().getCliente());
		show(cCClienteCcCliPorTipo);
	}

	public void btCCClientePorTipo() throws SQLException {
		Pedido pedido = getPedido();
		Vector vetor = CCCliPorTipoService.getInstance().getCCCliPorTipoMaiAntigo(isEditing() && (pedido.itemPedidoList.size() > 0), pedido.nuPedido);
    	RelCCClientePorTipoPedidoWindow ccliCcClientePorTipoPedidoWindow = new RelCCClientePorTipoPedidoWindow();
		ccliCcClientePorTipoPedidoWindow.domainToScreen((CCCliPorTipo) vetor.items[0], (CCCliPorTipo) vetor.items[1], pedido.vlTotalPedido);
    	ccliCcClientePorTipoPedidoWindow.popup();
	}

	protected void btNovoItemClick(boolean showAddNovoItem) throws SQLException {
		if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
		ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			pedido.flCritico = bgPedidoCritico.getValue();
			if (LavenderePdaConfig.permiteApenasUmItemPedidoProdutoCritico && pedido.isPedidoCritico() && pedido.itemPedidoList.size() >= 1) {
				UiUtil.showErrorMessage(Messages.PEDIDOPRODUTOCRITICO_ERRO_QT_ITENS);
				return;
			}
		}
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		if (LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() || LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			CadItemPedidoForm.invalidateInstance();
		}
		//--
		if (LavenderePdaConfig.usaControleEstoquePorEstoquePrevisto()) {
			pedido.dtEntrega = edDtEntrega.getValue();
			PedidoService.getInstance().validateDataEntrega(pedido, false);
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			pedido.dtSugestaoCliente = edDtSugestaoCliente.getValue();
		}
		if (LavenderePdaConfig.isBloqueiaCondPagtoPorClienteAddItemPedidoFimPedido()) {
			PedidoService.getInstance().validateCondPagtoPorCliente(pedido.getCliente().cdCondicaoPagtoBloqueada, pedido.getCondicaoPagamento().cdCondicaoPagamento);
		}
		if (PedidoService.getInstance().validateUsaPedidoComplementar(pedido)) {
			throw new ValidationException(Messages.PEDIDO_RELACIONADO_ERRO_INCLUIR_ITEM_PEDIDO_COMPLEMENTAR);
		}
		if(LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && ValueUtil.isEmpty(pedido.cdCentroCusto)) {
			throw new ValidationException(Messages.PEDIDO_MSG_ERRO_SEM_CENTRO_CUSTO);
		}
		if(LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ValueUtil.isEmpty(pedido.cdPlataformaVenda)) {
			throw new ValidationException(Messages.PEDIDO_MSG_ERRO_SEM_PLATAFORMA_VENDA);
		}
		
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao && ValueUtil.isEmpty(cbDivisaoVenda.getValue())) {
			throw new ValidationException(Messages.MSG_DIVISAO_VENDA_OBRIGATORIO);
		}
		if (LavenderePdaConfig.usaConfigPedidoProducao() && pedido.isPedidoVendaProducao()) {
			if ((!LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao() && pedido.pedidoRelacionado == null) || (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao() && ValueUtil.isEmpty(pedido.pedidoRelacionadoList)) && !SessionLavenderePda.liberadoPorSenhaRelacionarPedidoProducao) {
				if (!PedidoService.getInstance().validateUsaPedidoProducao(pedido)) {
					throw new RelacionaPedProducaoException();
				}
			}
		}
		pedido.updateByClickNovoItemInPedido = true;
		if (!isEditing() || houveAlteracaoCampos) {
			save();
			edit(pedido);
		}
		pedido.updateByClickNovoItemInPedido = false;
		PedidoService.getInstance().validateObrigaRelacionarPedidoBonificaoTroca(pedido);
		validaObrigaSelecaoUnidadeAlternativaPedido();
		//--
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente() && !LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido()) {
			if (ValueUtil.isEmpty(pedido.itemPedidoList) && (ValueUtil.isEmpty(pedido.cdCargaPedido)) && (cbCargaPedido.isEnabled())) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.CARGAPEDIDO_CONFIRMACAO_DE_NAO_RELACIONAMENTO_PEDIDO)) {
					return;
			}
		}
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido() && ValueUtil.getBooleanValue(pedido.flSituacaoReservaEst)) {
			UiUtil.showWarnMessage(Messages.MSG_AVISO_PEDIDO_COM_RESERVA_PRODUTO);
		}
		//--
		if (showAddNovoItem) {
			if (LavenderePdaConfig.usaTelaAdicionarItemAoPedidoEstiloDesktop) {
				CadItemPedidoDesktopForm cadItemPedidoDesktopForm = new CadItemPedidoDesktopForm(this, pedido);
				cadItemPedidoDesktopForm.add();
				show(cadItemPedidoDesktopForm);
			} else if (pedido.isPedidoTroca()) {
				CadItemTrocaForm cadItemTrocaForm = new CadItemTrocaForm(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
				cadItemTrocaForm.add();
				show(cadItemTrocaForm);
			} else if (pedido.isPedidoBonificacao()) {
				ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
				listItemPedidoForm.novoClickFromCadPedido();
			} else {
				ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
				listItemPedidoForm.novoClickFromCadPedido();
				if (LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras) {
					listItemPedidoForm.show();
				}
			}
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
		}
		houveAlteracaoCampos = false;
	}

	private void btListaItensClick() throws SQLException {
		Pedido pedido = getPedido();
		inRelatorioMode = true;
		if (pedido.isPedidoAberto() && !inOnlyConsultaItens && houveAlteracaoCampos) {
			pedido.updateByClickNovoItemInPedido = true;
			pedido.validaDataEntrega = false;
			save();
			pedido.validaDataEntrega = true;
			pedido.updateByClickNovoItemInPedido = false;
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido() && ValueUtil.getBooleanValue(pedido.flSituacaoReservaEst)) {
			UiUtil.showWarnMessage(Messages.MSG_AVISO_PEDIDO_COM_RESERVA_PRODUTO);
		}
		ListItemPedidoForm listItemPedidoForm = getItemPedidoListForm();
		listItemPedidoForm.show();
		houveAlteracaoCampos = false;
	}

	private ListItemPedidoForm getItemPedidoListForm() throws SQLException {
		ListItemPedidoForm listItemPedidoForm;
		Pedido pedido = getPedido();
		if (pedido.isPedidoTroca()) {
			listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
		} else if (pedido.isPedidoBonificacao()) {
			listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		} else {
			if (inOnlyConsultaItens || inRelatorioMode) {
				listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			} else {
				listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			}
		}
		return listItemPedidoForm;
	}

	public void btFecharPedidoClick() throws SQLException {
		validateComponentes();
		if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
		ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		Pedido pedido = getPedido();
		PedidoService.validationFechamentoListCount = 0;
		pedido.ignoraValidacaoSugestaoDifProdutos = false;
		pedido.ignoraValidacaoSugestaoProdutosComQtde = false;
		pedido.ignoraValidacaoSugestaoProdutosSemQtde = false;
		pedido.ignoraValidacaoSugestaoItensRentabilidadeIdeal = false;
		pedido.ignoraValidacaoProdutosPendentes = false;
		pedido.ignoraGiroProdutoPendente = false;
		pedido.ignoraValidacaoAtrasoCliente = naoAvisaClienteAtrasado;
		pedido.ignoraValidacaoMultiplosSugestaoProdutos = false;
		pedido.ignoraValidacaoLimiteCreditoCliente = false;
		fecharPedido();
		if (!pedido.isPedidoAberto()) {
			createPedidoOportunidade();
		}
	}

	private boolean isTipoPedidoPermitidoParaConsignacao() throws SQLException {
		if (!getPedido().getTipoPedido().isPermiteConsignacao()) {
			UiUtil.showErrorMessage(Messages.PEDIDO_CONSIGNACAO_MSG_ERRO_TIPO_PEDIDO_NAO_HABILITADO);
			return false;
		}
		return true;
	}

	private void consignaEEnviaPedido() throws SQLException {
		if (isTipoPedidoPermitidoParaConsignacao()
				&& UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRM_CONSIGNACAO)) {
			Pedido pedido = getPedido();
			if (!PedidoUiUtil.executaValidacoesIniciais(pedido, resultado, showMessageConfirmClosePedido)) {
				return;
			}
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				screenToDomain();
				if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil
						&& ValueUtil.isNotEmpty(pedido.dtEntrega) && !pedido.isLiberadoEntrega()) {
					if (!PedidoUiUtil.isConfirmaPedidoDtEntregaFinalSemanaFeriado(pedido)) {
						return;
					}
				}
				pedido.ignoraValidacaoLimiteCreditoCliente = true;
				pedido.ignoraValidacaoSugestaoDifProdutos = true;
				pedido.ignoraValidacaoSugestaoProdutosSemQtde = true;
				pedido.ignoraValidacaoSugestaoItensRentabilidadeIdeal = true;
				pedido.ignoraValidacaoSugestaoProdutosComQtde = true;
				pedido.ignoraValidacaoMultiplosSugestaoProdutos = true;
				if (!validateFechamentoPedido()) {
					return;
				}
				if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
					ItemPedidoService.getInstance().validaItemsTrocaConsignacao(pedido);
				}
				try {
					PedidoService.getInstance().isPermiteClienteConsignar(pedido);
				} catch (ValidationException e) {
					AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
					senhaForm.setMensagem(e.getMessage());
					senhaForm.setCdCliente(pedido.cdCliente);
					senhaForm.setVlTotalPedido(pedido.vlTotalPedido);
					senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
					if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
						throw e;
					}
				}
				PedidoService.getInstance().consignaPedido(pedido);
				pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoConsignado;
				pedido.dtConsignacao = DateUtil.getCurrentDate();
				pedido.vlPedidoOriginal = LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao ? pedido.vlTotalPedido : 0;
				save();
				afterConsignaPedido();
				updateCurrentRecordInList(pedido);
				if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMA_ENVIO_CONSIGNACAO)) {
					enviaPedidosConsignado();
				}
				if (LavenderePdaConfig.isSugereImpressaoPedidoConsignacao()) {
					sugereImpressaoPedidoConsignacao();
				}
			} finally {
				resultado = false;
				msg.unpop();
				edit(pedido);
				onShowCadPedidoForm();
				super.onFormShow();
			}
		}
	}

	private void afterConsignaPedido() throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			Pedido pedido = getPedido();
			if (SessionLavenderePda.visitaAndamento != null) {
				if (LavenderePdaConfig.sugereRegistroSaidaAoFecharPedido()) {
					sugereRegistroSaida();
				} else if (LavenderePdaConfig.registraSaidaClienteAoFecharPedido) {
					Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
					if (ValueUtil.valueEquals(pedido.getCliente().cdCliente, visitaEmAndamento.cdCliente)) {
						CadClienteMenuForm.btRegistrarSaidaClick(visitaEmAndamento, true);
						cadClienteMenuForm.remontaMenuFuncionalidades();
						VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visitaEmAndamento, pedido);
					}
				}
			} else {
				Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
				if (visita != null && ValueUtil.isNotEmpty(visita.cdVisita)) {
					if (!pedido.isPedidoAberto()) {
						visita.flVisitaPositivada = ValueUtil.VALOR_SIM;
						VisitaService.getInstance().fechaVisita(visita, false);
						VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visita, pedido);
					}
				}
			}
		}
	}

	private void devolvePedidoConsignado() throws SQLException {
		ListDevolucaoPedidoConsignadoForm listDevolucaoPedidoConsignadoForm = new ListDevolucaoPedidoConsignadoForm(getPedido(), this);
		show(listDevolucaoPedidoConsignadoForm);
	}

	public void fecharPedido() throws SQLException {
		fecharPedido(true);
	}

	public void fecharPedido(boolean openPopupTransportadora) throws SQLException {
		screenToDomain();
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias && pedido.necessitaRecalculoPontuacao()) {
			if (UiUtil.showConfirmYesNoMessage(Messages.RECALCULO_PONTUACAO_PEDIDO_TITULO, Messages.RECALCULO_PONTUACAO_PEDIDO_MESSAGE)) {
				PontuacaoConfigService.getInstance().reloadPontuacaoPedido(pedido, null);
				UiUtil.showSucessMessage(Messages.RECALCULO_PONTUACAO_PEDIDO_SUCESSO);
			}
			return;
		}
		boolean ignoraEnvioErp = pedido.getTipoPedido() != null && pedido.getTipoPedido().ignoraEnvioErp();
		if (LavenderePdaConfig.permiteApenasUmItemPedidoProdutoCritico && pedido.isPedidoCritico() && pedido.itemPedidoList.size() > 1) {
			UiUtil.showErrorMessage(Messages.PEDIDOPRODUTOCRITICO_ERRO_QT_ITENS);
			return;
		}
		if (LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao() && !LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && pedido.isPedidoBonificacao() && ValueUtil.isEmpty(pedido.nuPedidoRelBonificacao)) {
			if (!UiUtil.showConfirmYesNoMessage(Messages.RELACIONA_PEDIDO_BONIFICACAO_TITULO, Messages.RELACIONA_PEDIDO_BONIFICACAO_MESSAGE)) {
				return;
			}
		}
		boolean res = false;
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && pedido.fecharPedidoComVerbaNeg) {
			res = true;
		} else if (showMessageConfirmClosePedido) {
			res = UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRM_FECHARPEDIDO);
		}
		if (ValueUtil.isNotEmpty(pedido.cdMotivoPendencia) && (!pedido.isPendente() && !isPedidoPossuiItemPendente())) {
			pedido.cdMotivoPendencia = null;
			pedido.cdMotivoPendenciaJust = null;
			pedido.dsObsMotivoPendencia = null;
			PedidoService.getInstance().updatePedidoMotivoPendente(pedido);
		}
		if (LavenderePdaConfig.isValidaPercMaxValorPedidoBonificadoNoFechamento() && !PedidoService.getInstance().validaVlTotalBonificacaoPedido(pedido)) {
			return;
		}
		if (res || !showMessageConfirmClosePedido) {
			if (!fechaPedidoSugestaoCombo && !sugereItemComboAntesFechamento(pedido)) {
				return;
			}
			fechaPedidoSugestaoCombo = false;
			if (!PedidoUiUtil.executaValidacoesIniciais(pedido, resultado, showMessageConfirmClosePedido)) {
				afterCrudItemPedido();
				return;
			}
			showMessageConfirmClosePedido = true;
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			boolean refreshPedidoToScreen = false;
			try {
				if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
					double vlPromoTotal = pedido.getVlTotalPromocional();
					if((ValueUtil.round(pedido.vlTotalBrutoItens - vlPromoTotal) < ValueUtil.round(vlPromoTotal * LavenderePdaConfig.getVlPercentualValorMinimoPromocional()))) {
						UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_VL_MIN_PROMOCIONAL, (vlPromoTotal * LavenderePdaConfig.getVlPercentualValorMinimoPromocional())));
						return;
					}
				}
				if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil && ValueUtil.isNotEmpty(pedido.dtEntrega) && !pedido.isLiberadoEntrega()) {
					if (!PedidoUiUtil.isConfirmaPedidoDtEntregaFinalSemanaFeriado(pedido)) {
						return;
					}
				}
				if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao() && openPopupTransportadora) {
					ListTransportadoraRegWindow listTransportadoraRegWindow = new ListTransportadoraRegWindow(pedido);
					listTransportadoraRegWindow.popup();
					if (!listTransportadoraRegWindow.selecionouTransportadora) {
						return;
					}
				}

				if (LavenderePdaConfig.exibePopupFreteFechamentoPedido()) exibirPopupFrete();

				if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow() && PedidoService.getInstance().isHasItemPedidoPendente(pedido)) {
					UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MSG_PENDENTE_DESC_SUGERIDO);
				}
				if (LavenderePdaConfig.usaInterpolacaoPrecoProduto && LavenderePdaConfig.isUsaVerba() && Math.abs(pedido.vlVerbaPedido) > 0) {
					UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_CONSUMIDA, pedido.vlVerbaPedido));
				}
				if (LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && LavenderePdaConfig.isIncluiDescQtdeGrupoProdValidacaoLimiteCreditoFechamentoPedido()) {
					PedidoService.getInstance().aplicaDescQtdPorGrupoProdTodosItens(pedido, true, true);
					showMessageDescQtdGrupoProdAplicadoAuto();
					lbVlTotalPedido.setValue(pedido.vlTotalPedido);
					repaintNow();
				}
				if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.isUsaMotivosPendenciaTipoFreteDiferentePadrao()) {
					ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(pedido.itemPedidoList);
				}
				refreshPedidoToScreen = true;
				if (!validateFechamentoPedido()) {
					return;
				}
				try {
					save();
				} catch (RelacionaPedProducaoException e) {
					ExceptionUtil.handle(e);
					return;
				}
				if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
					pedido = getPedido();
				}
				try {
					if (!ignoraEnvioErp && (LavenderePdaConfig.sugereEnvioAutomaticoPedido || LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao) && (pedido.isPedidoAberto() || pedido.isPedidoConsignado()) && controlEnvioAutoPedido) {
						fecharEnviarPedido(false, true, false);
						pedido.cdStatusPedido = getPedido().cdStatusPedido;
						if (LavenderePdaConfig.getSugereImpressaoDocumentosPedidoAposEnvio().size() > 0 && !pedido.isPedidoAberto()) {
							sugereImpressao(pedido);
						}
						return;
					}
					if(pedido.getQtItensLista() > 0) {
						concluiPedido(pedido);
					}
				} catch (DescItemMaiorDescProgressivoException e) {
					int result = UiUtil.showMessage(e.getMessage(), TYPE_MESSAGE.ERROR,
							new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
					if (result == 1) {
						PedidoUiUtil.showListItensDivergentesDescProgQtd(this);
					}
				} catch (VerbaSaldoPedidoExtrapoladoException e) {
					if (LavenderePdaConfig.liberaComSenhaPedidoComSaldoVerbaExtrapolado && e.getMessage().startsWith(Messages.CONSUMO_VERBA_EXCEDENTE_FECHAR_PEDIDO) && isLiberouComSenhaVerbaSaldoExtrapoladaPedido(pedido, e.getMessage())) {
						pedido.consumoVerbaSaldoLiberadoSenha = true;
					} else if (!LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado) {
						pedido.deveValidarConsumoVerbaPedido = false;
						throw e;
					}
				} catch (ValidationVerbaPersonalizadaException e) {
					if (e.getItemPedidoNegativo() == null) return;
					int result = UiUtil.showMessage(e.getMessage(), TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_OK});
					if (result == 0) {
						CadItemPedidoForm cad = CadItemPedidoForm.getInstance(this, pedido);
						try {
							res = false;
							cad.disabledBtNextPrev = true;
							cad.edit(e.getItemPedidoNegativo());
							cad.show();
						} finally {
							cad.disabledBtNextPrev = false;
						}
					}
				} catch (BoletoEmContingenciaNaoGeradoException e) {
					throw e;
				} catch (RelProdutosRentabilidadeSemAlcadaException ex) {
					relProdutosRentabilidadeSemAlcadaException(pedido, ex);
				} catch (ValidationException e) {
					if (LavenderePdaConfig.usaDescontoComissaoPorGrupo && e.getMessage().startsWith("DCG")) {
						int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
						if (result == 1) {
							PedidoUiUtil.showListItensDivergentesDescComissaoGrupo(this);
						}
					} else if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && e.getMessage().startsWith(DescontoGrupo.SIGLE_EXCEPTION)) {
						int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
						if (result == 0) {
							PedidoUiUtil.showListItensDivergentesDescQtdGrupo(this);
						}
					} else if (LavenderePdaConfig.usaDescQuantidadePorPacote && e.getMessage().startsWith(DescontoPacote.SIGLE_EXCEPTION)) {
						int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
						if (result == 0) {
							PedidoUiUtil.showListItensDivergentesDescPacote(this);
						}
					} else if (LavenderePdaConfig.usaValidaPosicaoVincoLargura() && e.getMessage().startsWith("PVL")) {
						int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
						if (result == 0) {
							PedidoUiUtil.showListItensDivergentesCalculoVinco(this);
						}
					} else if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
						if (e.getMessage().startsWith(Messages.DESCONTO_PROGRESSIVO_MSG_SALDO_NEGATIVO.substring(0, 15))) {
							UiUtil.showErrorMessage(e.getMessage());
							PedidoService.getInstance().retiraDescontosDoFimDoPedidoConsumindoFlex(pedido);
							previsaoDescontoClick();
						} else {
							throw e;
						}
					} else if (Messages.FECHAR_PEDIDO_ITENS_ERRO_RECALCULO.equals(e.getMessage())) {
						Vector itensErro = new Vector();
						populaErroCalculo(pedido, itensErro);
						RelNotificacaoItemWindow relProdutoErroWindow = new RelNotificacaoItemWindow(Messages.TITULO_REL_ITENS_ERRO_RECALCULO, itensErro, false, Messages.FECHAR_PEDIDO_ITENS_ERRO_RECALCULO);
						relProdutoErroWindow.popup();
					} else {
						throw e;
					}
				}
			} finally {
				try {
					resultado = false;
					showMessageConfirmClosePedido = true;
					msg.unpop();
					if ((MainLavenderePda.getInstance().getActualForm() == this) && refreshPedidoToScreen && !pedido.totalmenteConvertidoPedidoBonificacao) {
						edit(pedido);
						onShowCadPedidoForm();
						super.onFormShow();
						if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()
								&& pedido.getPagamentoPedido().houveErro && hashTabs.size() > 1) {
							tabDinamica.setActiveTab(TABPANEL_DEBITO_BANCARIO);
							tabDinamica.requestFocus();
							pedido.getPagamentoPedido().houveErro = false;
						}
					}
					if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoFecharPedido() && res) {
						UiMessagesUtil.mostraMensagemPedidosAbertos();
					}
					if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo()) {
						pedido.pedidoSimulacao = false;
					}
					redirecionaAoPedidoDeBonificacaoRelacionada(pedido);
				} finally {
					SessionLavenderePda.removePedidoProcessandoFechamento(pedido);
				}
				UiUtil.unpopProcessingMessage();
			}
		}
	}

	private void concluiPedido(Pedido pedido) throws SQLException {
		PedidoService.getInstance().fecharPedido(pedido);
		updateAndMostraMsgCota();
		afterFecharPedido();
		updateCurrentRecordInList(pedido);
		if (LavenderePdaConfig.usaSelecaoDocAnexoPedido()) {
			DocumentoAnexoService.getInstance().atualizaDocAnexoParaEnvio(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey(), DocumentoAnexo.FLTIPOALTERACAO_INSERIDO);
		}
		if (pedido.dtSugestaoCliente != null
				&& !pedido.dtSugestaoCliente.equals(pedido.dtEntrega)
				&& pedido.isDtSugeridaDiferenteDtEntrega) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.TROCADATAPREVISTAPEDIDO, new String[] {DateUtil.formatDateDDMMYYYY(pedido.dtSugestaoCliente), DateUtil.formatDateDDMMYYYY(pedido.dtEntrega)}));
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().atualizaDomainParaEnvio(pedido, ItemPedidoBonifCfg.FLTIPOALTERACAO_INSERIDO);
		}
	}

	private boolean isLiberouComSenhaVerbaSaldoExtrapoladaPedido(Pedido pedido, String msg) throws SQLException {
		AdmSenhaDinamicaWindow senhaDinamicaWindow = new AdmSenhaDinamicaWindow();
		senhaDinamicaWindow.setMensagem(msg);
		senhaDinamicaWindow.setCdCliente(pedido.cdCliente);
		senhaDinamicaWindow.setVlTotalPedido(pedido.vlTotalPedido);
		senhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_EXTRAPOLADO);
		if (senhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			return false;
		}
		return true;
	}

	private void sugereImpressao(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth() && ValueUtil.isNotEmpty(getPedido().getNfce().nuPedido) && tipoPedido != null && tipoPedido.isGeraNfce()) {
			sugereImpressaoNfce();
			return;
		}
		List<String> tipoDocumentoList = LavenderePdaConfig.getSugereImpressaoDocumentosPedidoAposEnvio();
		for (String tipoDocumento : tipoDocumentoList) {
			if (TipoPedido.TIPO_IMPRESSAO_NFE.equals(tipoDocumento) && LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() && tipoPedido != null && tipoPedido.isGeraNfe() && pedido.getInfoNfe().nuPedido != null) {
				sugereImpressaoNfe(pedido);
				continue;
			}
			if (TipoPedido.TIPO_IMPRESSAO_NFCE.equals(tipoDocumento) && LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth() && tipoPedido != null && tipoPedido.isGeraNfce()) {
				sugereImpressaoNfce();
				continue;
			}
			if (TipoPedido.TIPO_IMPRESSAO_NFE_CONTINGENCIA.equals(tipoDocumento) && LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth() && tipoPedido != null && tipoPedido.isGeraNfe() && pedido.getInfoNfe().nuPedido == null) {
				sugereImpressaoNfeContingencia();
				continue;
			}
			if (TipoPedido.TIPO_IMPRESSAO_PEDIDO.equals(tipoDocumento) && (LavenderePdaConfig.isPermiteImpressaoPedido() || !(tipoPedido != null && tipoPedido.isGeraNfe())) && LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0) {
				sugereImpressaoPedido();
				continue;
			}
			if (TipoPedido.TIPO_IMPRESSAO_BOLETO.equals(tipoDocumento) && LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0 && tipoPedido != null && tipoPedido.isGeraBoleto() && ValueUtil.isNotEmpty(pedido.getPedidoBoletoList())) {
				sugereImpressaoBoleto();
				continue;
			}
			if (TipoPedido.TIPO_IMPRESSAO_NOTA_PROMISSORIA.equals(tipoDocumento) && LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
				sugereImpressaoNotaPromissoria();
				continue;
			}
		}
	}

	private void sugereImpressaoNfe(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() && ValueUtil.isNotEmpty(getPedido().getInfoNfe().nuPedido)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_NFE)) {
				btImprimirNfeClick(pedido);
			}
		} else {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0) {
				if (UiUtil.showWarnConfirmYesNoMessage(Messages.PEDIDO_MSG_SEM_DADOS_NFE_SUGESTAO_PEDIDO)) {
					btImprimirPedidoClick();
				}
		}
	}
	}

	private void sugereImpressaoNfce() throws SQLException {
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() && ValueUtil.isNotEmpty(getPedido().getInfoNfce().nuPedido)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_NFCE)) {
				btImprimirNfceClick();
			}
		} else {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0) {
				if (UiUtil.showWarnConfirmYesNoMessage(Messages.PEDIDO_MSG_SEM_DADOS_NFE_SUGESTAO_PEDIDO)) {
					btImprimirPedidoClick();
				}
		}
	}
	}

	private void sugereImpressaoBoleto() throws SQLException {
		if (ValueUtil.isNotEmpty(getPedido().getPedidoBoletoList()) || LavenderePdaConfig.isUsaGeracaoBoletoApenasSolicitado()) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_BOLETO)) {
				btImprimirBoletoClick();
			}
		}
	}

	private void sugereImpressaoPedido() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_PEDIDO)) {
			btImprimirPedidoClick();
		}
	}

	private void sugereImpressaoPedidoConsignacao() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.isPedidoConsignado() && ValueUtil.isNotEmpty(pedido.getPedidoConsignacaoList())) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_PEDIDOCONSIGNACAO)) {
				btImprimirPedidoConsignacaoClick();
			}
		}
	}

	private void sugereImpressaoNotaPromissoria() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAONOTAPROMISSORIA_SUGESTAO_IMPRESSAO)) {
			btImprimirPromissoriaClick();
		}
	}

	private boolean isValidaPctMaxPoliticaComercialPedido() {
		return LavenderePdaConfig.usaPoliticaComercial() && (LavenderePdaConfig.isValidaPctMaxPoliticaComercial() || LavenderePdaConfig.isValidaPctMinMaxPoliticaComercial());
	}

	protected void afterReabrirPedido() throws SQLException {
		if (!LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			return;
		}
		if (isValidaPctMaxPoliticaComercialPedido() && MargemRentabService.getInstance().isRecalculoRentabilidadeNeeded(RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_PEDIDO_REABERTO)) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(getPedido());
		}
		recalcularRentabilidadePedido(RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_PEDIDO_REABERTO);
		ListItemPedidoForm.invalidateInstance();
	}

	protected void afterFecharPedido() throws SQLException {
		Pedido pedido = getPedido();
		PontoGpsService.getInstance().startColetaGpsPontoEspecificoSistema();
		//--
		if (pedido.isPedidoBonificacao() && pedido.pedidoRelacionado != null) {
			if (pedido.isPendente()) {
				PedidoService.getInstance().atualizaPedidoPendente(pedido);
			} else {
				PedidoService.getInstance().atualizaDescontoPedido(pedido);
			}

			if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() && pedido.isPendenteLimCred()) {
				PedidoService.getInstance().atualizaPedidoPendenteLimCred(pedido);
			}

		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && pedido.isPedidoItemPendente()) {
			UiUtil.showInfoMessage(Messages.FECHAR_PEDIDO_PEDIDO_PENDENTE);
		}
		if (pedido.flDescQtdGrupoAplicadoAuto && LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido
				&& !LavenderePdaConfig.isIncluiDescQtdeGrupoProdValidacaoLimiteCreditoFechamentoPedido()) {
			showMessageDescQtdGrupoProdAplicadoAuto();
		}
		Cliente cliente = pedido.getCliente();
		if (LavenderePdaConfig.controlaVencimentoAlvaraCliente > 0) {
        	if (cliente != null) {
    			if (!cliente.isAlvaraVigente()) {
    				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_ALVARA_VENCIDO, DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtVencimentoAlvara)));
    			}
        	}
		}
		//--
		if (LavenderePdaConfig.controlaVencimentoAfeCliente > 0) {
        	if (cliente != null) {
    			if (!cliente.isAfeVigente()) {
    				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_AFE_VENCIDO, DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtVencimentoAfe)));
    			}
        	}
        }
		//--
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			if (PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(pedido.cdCargaPedido, null) > LavenderePdaConfig.qtdPesoMinimoCargaPedido) {
				if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_CARGAPEDIDO_ATINGIDO, pedido.getCargaPedido().toString()))) {
					CargaPedidoService.getInstance().fecharCarga(pedido.getCargaPedido());
				}
			}
		}
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			if (SessionLavenderePda.visitaAndamento != null) {
				if (LavenderePdaConfig.sugereRegistroSaidaAoFecharPedido()) {
					sugereRegistroSaida();
					if (cadClienteMenuForm != null) {
						cadClienteMenuForm.remontaMenuFuncionalidades();
					}
				} else if (LavenderePdaConfig.registraSaidaClienteAoFecharPedido) {
					Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
					if (ValueUtil.valueEquals(pedido.getCliente().cdCliente, visitaEmAndamento.cdCliente)) {
						CadClienteMenuForm.btRegistrarSaidaClick(visitaEmAndamento, true);
						if (cadClienteMenuForm != null) {
							cadClienteMenuForm.remontaMenuFuncionalidades();
						}
						VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visitaEmAndamento, pedido);
					}
				}
			} else {
				Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
				if (visita != null && ValueUtil.isNotEmpty(visita.cdVisita)) {
					if (!pedido.isPedidoAberto()) {
						visita.flVisitaPositivada = ValueUtil.VALOR_SIM;
						VisitaService.getInstance().fechaVisita(visita, false);
						VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visita, pedido);
					}
				}
			}
		}
		if (LavenderePdaConfig.usaSelecaoDocAnexoPedido()) {
			DocumentoAnexoService.getInstance().atualizaDocAnexoParaEnvio(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey(), DocumentoAnexo.FLTIPOALTERACAO_INSERIDO);
		}
		if (LavenderePdaConfig.usaPedidoComplementar() && pedido.isPedidoComplementar()) {
			getPedidoService().adicionaMsgPedidoComplementadoObsPedido(pedido);
		}
		if (LavenderePdaConfig.isUsaGeracaoBoletoApenasContingencia() && LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0 && pedido.getTipoPedido().isGeraBoleto()) {
			try {
				PedidoBoletoService.getInstance().geraBoletoPedido(pedido);
			} catch (Throwable e) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOBOLETOCONTINGENCIA_MSG_ERRO, e.getMessage()));
				throw new BoletoEmContingenciaNaoGeradoException(MessageUtil.getMessage(Messages.IMPRESSAOBOLETOCONTINGENCIA_MSG_ERRO, e.getMessage()));
			}
		}
		if (LavenderePdaConfig.isSugereGeracaoPdfNoFechamento() && UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_OFFLINE_SUGERE_GERACAO) ) {
			gerarPdfPedido();
		}
		if (LavenderePdaConfig.isGeraPdfOfflineAuto()) {
			geraPdfOffline(false, true);
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() && LavenderePdaConfig.usaVolumeVendaMensalRede) {
			Vector pedidoRedeList = PedidoService.getInstance().findAllPedidosDaRede(pedido);
			PedidoService.getInstance().atualizaDescontosVolumeVendaRede(pedidoRedeList);
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia() && pedido.isPendente() && !pedido.isPedidoPossuiJustificativa()) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE);
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB() && pedido.isPendenteFob()) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_FOB);
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().atualizaDomainParaEnvio(pedido, ItemPedidoBonifCfg.FLTIPOALTERACAO_INSERIDO);
		}
		ListItemPedidoForm.invalidateInstance();
	}

	private boolean mostraAvisoPedidoPendente(Pedido pedido) throws SQLException {
		MotivoPendencia motivoPendencia = MotivoPendenciaService.getInstance().findMotivoPendenciaPrincipalPedido(pedido);
		if (motivoPendencia == null) {
			throw new ValidationException(Messages.MOTIVO_PENDENCIA_NAO_ENCONTRADO);
		}
		if (!ValueUtil.valueEqualsIfNotNull(pedido.cdMotivoPendencia, motivoPendencia.cdMotivoPendencia)) {
			limparMotivoPendencia(pedido);
		}
		CadMotivoPendenciaJustWindow cadMotivoPendenciaWindow = new CadMotivoPendenciaJustWindow(motivoPendencia, pedido);
		if (cadMotivoPendenciaWindow.naoPossuiJustificativas) return true;
		cadMotivoPendenciaWindow.popup();
		return !cadMotivoPendenciaWindow.closedByBtFechar;
	}

	protected void showMessageDescQtdGrupoProdAplicadoAuto() throws SQLException {
		int result = UiUtil.showMessage(Messages.PEDIDO_RELATORIO_GRUPOPRODUTO_NAO_INSERIDO_AUTO, TYPE_MESSAGE.SUCCESS, new String[] {FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS});
		if (result == 1) {
			PedidoUiUtil.showListItensDescQtdGrupoAplicadoAuto(this);
		}
	}

	private boolean validateFechamentoPedido() throws SQLException {
		Pedido pedido = getPedido();
		try {
			// Validações personalizadas. Validações que tem ação do usuário.

			validaDescontoPorCondicaoPagamento(pedido, pedido.vlPctDescontoCondicao);

			validaPedidoComPagamentoAVista();
			if(!PedidoUiUtil.validateValorFreteManual(pedido)) {
				return false;
			}
			PedidoService.getInstance().validateFechamentoUI(pedido, false);
		} catch (DescontoMaximoItemException e) {
			AdmSenhaDinamicaWindow senhaDinamicaWindow = new AdmSenhaDinamicaWindow();
			senhaDinamicaWindow.setMensagem(e.getMessage());
			senhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_DESCONTOMAXIMOITEM);
			if (senhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				return false;
			}
			PedidoService.getInstance().atualizaPedidoPendenteAposLiberaracaoSenha(pedido);
			return validateFechamentoPedido();
		} catch (ProdutoCreditoDescontoException e) {
			ListAvisoProdutoCreditoDescontoWindow listAvisoProdutoCreditoDescontoWindow = new ListAvisoProdutoCreditoDescontoWindow(pedido, getCdTabelaPreco());
			listAvisoProdutoCreditoDescontoWindow.popup();
			return listAvisoProdutoCreditoDescontoWindow.fecharPedido ? validateFechamentoPedido() : false;
		} catch (CreditoDisponivelPedidoException e) {
			return UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PRODUTOCREDITODESCONTO_CREDITOS_DISPONIVEIS_FECHAMENTO_PEDIDO, pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido)) ? validateFechamentoPedido() : false;
		} catch (ValidationBonificacaoSaldoException ex) {
			if (LavenderePdaConfig.liberaComSenhaSaldoBonificacaoExtrapolado) {
				if (ConfigInternoService.getInstance().isPedidoBloqueadoSaldoBonificacaoLiberadoSenha(pedido.cdCliente)) {
					pedido.flSaldoBoniLiberadoSenha = ValueUtil.VALOR_SIM;
					BonificacaoSaldoService.getInstance().consomeSaldoBonificacaoPedido(pedido, false);
					return validateFechamentoPedido();
				}
				AdmSenhaDinamicaWindow senhaDinamicaWindow = new AdmSenhaDinamicaWindow();
				senhaDinamicaWindow.setMensagem(ex.getMessage());
				senhaDinamicaWindow.setCdCliente(pedido.cdCliente);
				senhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO);
				if (senhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				pedido.flSaldoBoniLiberadoSenha = ValueUtil.VALOR_SIM;
				BonificacaoSaldoService.getInstance().consomeSaldoBonificacaoPedido(pedido, false);
				return validateFechamentoPedido();
			}
			throw ex;
		} catch (ValidationValorMinPedidoException vlMinPedex) {
			if (LavenderePdaConfig.apenasAvisaValorMinimoParaPedido) {
				String message = vlMinPedex.getMessage() + " " + Messages.PEDIDO_MSG_FECHAR_DESEJA_CONTINUAR;
				int result = UiUtil.showMessage(message, TYPE_MESSAGE.WARN, new String[] { FrameworkMessages.BOTAO_NAO, FrameworkMessages.BOTAO_SIM });
				if (result == 0) {
					return false;
				}
				return validateFechamentoPedido();
			} else {
				throw new ValidationException(vlMinPedex.getMessage());
			}
		} catch (ClienteInadimplenteException ex) {
			if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() && !ValueUtil.VALOR_SIM.equals(SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista) && (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_REDE_LIBERA_SENHA || ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_BLOQUEADO || ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_LIBERA_SENHA)) {
				UiUtil.showErrorMessage(ex.getMessage() + " " + Messages.CLIENTE_BLOQUEADO_PEDIDO_A_VISTA);
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_REDE_LIBERA_SENHA) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdCliente(pedido.getCliente().cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_REDE_ATRASADO_NOVO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				pedido.flClienteAtrasadoLiberadoSenha = ValueUtil.VALOR_SIM;
				ClienteService.getInstance().saveClienteRedeLiberadoComSenha(pedido.getCliente());
				return validateFechamentoPedido();
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_BLOQUEADO) {
				throw new ValidationException(ex.getMessage());
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_LIBERA_SENHA) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdCliente(pedido.getCliente().cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				pedido.flClienteAtrasadoLiberadoSenha = ValueUtil.VALOR_SIM;
				if (ClienteService.getInstance().isClienteAtrasadoPorVlTotalTitulosAtraso(pedido.getCliente())) {
					UiUtil.showInfoMessage(Messages.CLIENTE_ATRASADO_VLTOTAL_TITULOS_LIBERADO);
				}
				return validateFechamentoPedido();
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_AVISA) {
				if (!UiUtil.showConfirmYesNoMessage(ex.getMessage() + " Deseja Continuar?")) {
					return false;
				} else {
					return validateFechamentoPedido();
				}
			} else {
				if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO) {
					UiUtil.showInfoMessage(Messages.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO);
					return validateFechamentoPedido();
				}
				return validateFechamentoPedido();
			}
		} catch (ValidationGrupoProdutoNaoInseridoPedidoException e) {
			RelGrupoProdutoNaoInseridoPedidoWindow relGrupoProdutoNaoInseridoPedidoWindow = new RelGrupoProdutoNaoInseridoPedidoWindow(super.title, true, pedido);
			relGrupoProdutoNaoInseridoPedidoWindow.popup();
			if (!relGrupoProdutoNaoInseridoPedidoWindow.getBtClicadoFecharPedido()) {
				btNovoItemClick(true);
				return false;
			}
			return validateFechamentoPedido();
		} catch (PositivacaoItensByFornecedorException pe) {
				ListFornecedoresNaoPositivadosWindow listFornecedoresNaoPositivadosWindow = new ListFornecedoresNaoPositivadosWindow(pedido, true);
				listFornecedoresNaoPositivadosWindow.popup();
				if (listFornecedoresNaoPositivadosWindow.isValidateFechamentoPedido) {
					return validateFechamentoPedido();
				} 
				return false;
		} catch (DescontoProgressivoPedidoException de) {
			WmwInputBox ib = new WmwInputBox(Messages.DESCONTO_PROGRESSIVO_APLICAR, MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_APLICAR, StringUtil.getStringValueToInterface(pedido.getVlPctDescProgressivo())), pedido.vlPctDescProgressivo);
			ib.popup();
			if (ib.getPressedButtonIndex() == 0) {
				return false;
			}
			pedido.vlPctDescProgressivo = ib.getValueDouble();
			return validateFechamentoPedido();
		} catch (SugestaoVendaComCadastroSemQtdPedidoException se) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			listItemPedidoForm.showSugestaoProdutosFechamentoPedido();
			return false;
		} catch (ItemParticipacaoExtrapoladoException se) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
			listItemPedidoForm.showItemValorExtrapoladoFechamentoPedido();
			return false;
		} catch (SugestaoVendaComCadastroComQtdPedidoException se) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			listItemPedidoForm.showSugestaoProdutosComQtdeFechamentoPedido();
			return false;
		} catch (SugestaoVendaDifPedidoException se) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			listItemPedidoForm.showSugestaoProdDifPedidoFechamentoPedido();
			return false;
		} catch (ListMultiplasSugestoesProdutosException se) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			if (listItemPedidoForm.showListMultiplasSugestoesProdutosFechamentoPedido()) {
				return validateFechamentoPedido();
			}
			return false;
		} catch (GiroProdutoException e) {
			ListGiroProdutoWindow listGiroProdutoWindow = new ListGiroProdutoWindow(null, this, pedido, true, false, true);
			if (listGiroProdutoWindow.hasGiroProduto) {
				listGiroProdutoWindow.popup();
				if (listGiroProdutoWindow.fecharPedido) {
					return validateFechamentoPedido();
				}
				return false;
			} else {
				return validateFechamentoPedido();
			}
		} catch (RelProdutosPendentesPedidoException re) {
			RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(pedido, false, false, re.itemListPendentes, true);
			if (relProdutosPendentesWindow.hasProdutosPendentes()) {
				relProdutosPendentesWindow.cadPedidoForm = this;
				pedido.isAdiconandoItemRelProdutosPendentes = true;
				relProdutosPendentesWindow.popup();
				if (relProdutosPendentesWindow.continuaFecharPedido) {
					pedido.ignoraValidacaoProdutosPendentes = true;
					return validateFechamentoPedido();
				} else {
					return false;
				}
			} else {
				RelProdutosPendentesWindow.cleanInstance();
			}
		} catch (LimiteCreditoClienteExtrapoladoPedidoException le) {
			if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
				Pedido pedidoOriginal = getPedido();
				pedidoOriginal.flPendenteLimCred = ValueUtil.VALOR_SIM;
				pedido.ignoraValidacaoLimiteCreditoCliente = true;
				return validateFechamentoPedido();
			} else {
				if (LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
					pedido.flPendenteLimCred = ValueUtil.VALOR_SIM;
				}
				if (!LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && !LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
					UiUtil.showErrorMessage(le.getMessage() + " " + Messages.CLIENTE_BLOQUEADO_PEDIDO_A_VISTA);
					return false;
				}
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
				senhaForm.setMensagem(le.getMessage());
				senhaForm.setCdCliente(pedido.cdCliente);
				senhaForm.setVlTotalPedido(pedido.vlTotalPedido);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				} else {
					pedido.flCreditoClienteLiberadoSenha = ValueUtil.VALOR_SIM;
						pedido.cdUsuarioLiberacaoLimCred = senhaForm.cdUsuarioLiberado;
						if (LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
							pedido.flPendenteLimCred = ValueUtil.VALOR_NAO;
						}
					return validateFechamentoPedido();
				}
			}
		} catch (VerbaSaldoPedidoExtrapoladoException vspex) {
			if (LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado && vspex.getMessage().startsWith(Messages.VERBASALDO_SALDO_NEGATIVO)) {
				AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow();
				admSenhaDinamicaWindow.setMensagem(Messages.VERBASALDO_SALDO_NEGATIVO);
				admSenhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_BONIFICACAO_EXTRAPOLADO);
				admSenhaDinamicaWindow.setCdCliente(pedido.cdCliente);
				if (admSenhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				pedido.consumoVerbaSaldoLiberadoSenha = true;
				return validateFechamentoPedido();
			} else {
				throw vspex;
			}
		} catch (SugestaoVendaPresenteEmOutrasEmpresasPedidoException sugEx) {
			if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
				Vector sugestoesVigentes = SugestaoVendaService.getInstance().findAllSugestoesVendaObrigatoriasPendentesOutrasEmpresas(pedido, sugEx.flTipoSugestaoVenda, true);
				PedidoUiUtil.showPopupLiberacaoSenhaSugestaoVendaObrigatoria(pedido, sugestoesVigentes, sugEx.flTipoSugestaoVenda, true);
			} else {
				UiUtil.showInfoMessage(PedidoUiUtil.getTitlePopupAvisoSugestaoVendaObrigatoria(sugEx.flTipoSugestaoVenda), MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA_OUTRAS_EMPRESAS, pedido.getCliente().toString()));
			}
			if (SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(sugEx.flTipoSugestaoVenda)) {
				pedido.ignoraAvisoSugestaoVendaSemQtdOutrasEmpresas = true;
			} else if (SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE.equals(sugEx.flTipoSugestaoVenda)) {
				pedido.ignoraAvisoSugestaoVendaComQtdOutrasEmpresas = true;
			}
			return validateFechamentoPedido();
		} catch (ProdutosRelacionadosNaoAtendidosException pe) {
			ListProdutoRelacionadoWindow listProdutoRelacionadoWindow = new ListProdutoRelacionadoWindow(Messages.PRODUTO_RELACIONADO_FECHAMENTO_PEDIDO, pedido, false);
			listProdutoRelacionadoWindow.cadPedidoForm = this;
			listProdutoRelacionadoWindow.popup();
			if (ValueUtil.isEmpty(pedido.prodRelacionadosNaoContempladosList)) {
				return validateFechamentoPedido();
			}
			return false;
		} catch (RelProdutosGradesInconsistentesException e) {
			ListItensComGradeInconsistenteWindow listItensComGradeInconsistenteWindow = new ListItensComGradeInconsistenteWindow(pedido, this);
			listItensComGradeInconsistenteWindow.popup();
			if (ValueUtil.valueEquals(ItemGrade.ITEMGRADELIST_SEM_PROBLEMA_COM_GRADES, ItemPedidoGradeService.getInstance().verificaInconsistenciasGrade(pedido))) {
				return validateFechamentoPedido();
			}
			return false;
		} catch (PedidoSemClienteException pe) {
			if (pedido.isPedidoNovoCliente() && !pedido.cliente.isClienteDefaultParaNovoPedido()) {
				if (!validaEscolhaNovoClientePedidoSemCliente(pedido)) {
					return false;
				}
			} else if (pedido.getCliente().isClienteDefaultParaNovoPedido()) {
				if (!validaEscolhaClientePedidoSemCliente(pedido)) {
					return false;
				}
			}
			if (pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
				return false;
			} else {
				return validateFechamentoPedido();
			}
		} catch (SugestaoItensRentabilidadeIdealException se) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SHOW_POPUP_SUGESTAO_ITENS_RENT_IDEAL)) {
				ListSugestaoItensRentabilidadeIdealWindow listSugestaoItensRentabilidadeIdeal = new ListSugestaoItensRentabilidadeIdealWindow(pedido, false, false);
				listSugestaoItensRentabilidadeIdeal.cadPedidoForm = this;
				listSugestaoItensRentabilidadeIdeal.singleClickOn = isEditing() && isEnabled() && pedido.isPedidoAberto();
				listSugestaoItensRentabilidadeIdeal.popup();
				return false;
			} else {
				return validateFechamentoPedido();
			}
		} catch (ReservaEstoqueException ex) {
			if (LavenderePdaConfig.isNaoPermiteFecharPedidoSemReservaDeEstoque()) {
				String message = ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList) ? Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE : Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_ESTOQUE_GERAL;
				UiUtil.showErrorMessage(message);
				return false;
			}
			if (ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList)) {
				UiUtil.showErrorMessage(ex.getMessage());
				return false;
			} else {
				if (ValueUtil.VALOR_SIM.equals(pedido.flSituacaoReservaEst)) {
					return validateFechamentoPedido();
				} else {
				if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMACAO_ERRO_RESERVA_ESTOQUE_GERAL)) {
					pedido.ignoraGeracaoReservaEstoque = true;
					return validateFechamentoPedido();
				} else {
					return false;
				}
			}
			}
		} catch (DescontoPonderadoPedidoException ex) {
			if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
				Pedido pedidoRef = (Pedido) pedido.clone();
				Pedido pedidoOriginal = getPedido();
				if (pedidoRef.isPedidoBonificacao()) {
					if (LavenderePdaConfig.isGeraCreditoIndiceBonificacao() && pedidoRef.getTipoPedido().isFlTipoCreditoFrete() || pedidoRef.getTipoPedido().isFlTipoCreditoCondicao()) {
						pedidoOriginal.flPendente = pedidoRef.pedidoRelacionado != null && pedidoRef.pedidoRelacionado.isPendente() ? ValueUtil.VALOR_SIM :  ValueUtil.VALOR_NAO;
						return validateFechamentoPedido();
					}
					pedidoRef = pedidoRef.pedidoRelacionado;
				}
				double vlPctMaxDescontoUsuario = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(false, false);
				if (LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
					double vlPctDescontoLiberadoMediaPonderada = UsuarioDescService.getInstance().getMaxVlPctDescontoPonderadoLiberado(pedidoRef);
					if (vlPctDescontoLiberadoMediaPonderada < vlPctMaxDescontoUsuario) {
						vlPctMaxDescontoUsuario = vlPctDescontoLiberadoMediaPonderada;
					}
				}
				double vlDescontoLiberado = ValueUtil.round((pedidoRef.vlTotalBrutoItens * vlPctMaxDescontoUsuario) / 100);
				if (pedidoRef.isPedidoBonificacao() && !UiUtil.showConfirmYesNoMessage(ex.getMessage() + " " + MessageUtil.getMessage(Messages.PEDIDO_MSG_PCT_MAX_DESCONTO_ULTRAPASSADO_PEDIDO_PENDENTE, vlPctMaxDescontoUsuario) + " " + MessageUtil.getMessage(Messages.PEDIDO_MSG_PCT_MAX_DESCONTO_USUARIO_ULTRAPASSADO_BONIFICAO, pedidoRef.nuPedido))) {
					return false;
				} else if (!pedidoRef.isPedidoBonificacao()) {
					UiUtil.showWarnMessage(ex.getMessage() + " " + MessageUtil.getMessage(Messages.PEDIDO_MSG_PCT_MAX_DESCONTO_ULTRAPASSADO_PEDIDO_PENDENTE, vlPctMaxDescontoUsuario));
				}
				if (LavenderePdaConfig.isGeraCreditoIndiceBonificacao() && pedidoRef.isPedidoBonificacao() && PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedidoRef)) {
					PedidoService.getInstance().setPedidosBonificacaoComTipoCreditoPendente(pedidoRef);
				}
				PedidoDescService.getInstance().savePedidoDesc(pedidoRef, vlPctMaxDescontoUsuario, vlDescontoLiberado);
				pedidoOriginal.flPendente = ValueUtil.VALOR_SIM;
				return validateFechamentoPedido();
			} else {
				UiUtil.showErrorMessage(ex.getMessage());
				return false;
			}
		} catch (PesquisaMercadoException ex) {
			if (UiUtil.showConfirmYesNoMessage(ex.getMessage())) {
				btPesquisaMercadoClick();
				return false;
			}
			return validateFechamentoPedido();
		} catch (ItensPedidoAbaixoPesoMinimoTabelaPrecoException ex) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			listItemPedidoForm.showItemPedidoAbaixoPesoMinimoFechamentoPedido();
			return false;
		} catch (ItensPedidoAbaixoValorMinimoTabelaPrecoException ex) {
			ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(this, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			listItemPedidoForm.showItemPedidoAbaixoValorMinimoFechamentoPedido();
			return false;
		} catch (LiberacaoDataEntregaPedidoException ex) {
			if (!LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
				throw ex;
			}
			if (PedidoUiUtil.isLiberaSenhaDiaEntregaPedidoWindow(pedido, ex.getMessage())) {
				pedido.dtEntregaLiberada = pedido.dtEntrega;
				UiUtil.showInfoMessage(Messages.PEDIDO_MSG_SUCESSO_LIBERACAO_SENHA_DATA_ENTREGA);
				return validateFechamentoPedido();
			}
				return false;
		} catch (ValidationValorMinimoVerbaUltrapassadoException ex) {
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(ex.getMessage());
			senhaForm.setCdCliente(pedido.cdCliente);
			senhaForm.setVlTotalPedido(pedido.vlTotalPedido);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_TOLERANCIA_VERBA_POSITIVA);
			boolean senhaValida = senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA;
			if (senhaValida) {
				pedido.flMinVerbaLiberado = ValueUtil.VALOR_SIM;
				UiUtil.showInfoMessage(Messages.ITEMPEDIDO_LIMITE_VERBA_MINIMO_FECHAMENTO_PEDIDO_MSG_SUCESSO);
			}
			return false;
		} catch (RentabilidadeMenorMinimaException ex) {
			if (pedido.flAbaixoRentabilidadeMinima) {
				UiUtil.showErrorMessage(Messages.PEDIDO_RENTABILIDADE_BLOQUEADA);
				return false;
			}
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(ex.getMessage());
			senhaForm.setCdCliente(pedido.cdCliente);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_RENTABILIDADE_MINIMA);
			boolean senhaValida = senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA;
			if (senhaValida) {
				pedido.flRentabilidadeLiberada = ValueUtil.VALOR_SIM;
				UiUtil.showInfoMessage(Messages.PEDIDO_RENTABILIDADE_LIBERADA);
				return validateFechamentoPedido();
			}
				return false;
		} catch (RentabilidadeNegativaException ve) {
			return false;
		} catch (ClienteAtrasadoVlTitulosException ex) {
			if (LavenderePdaConfig.isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso() && LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdCliente(pedido.getCliente().cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				pedido.flClienteAtrasadoLiberadoSenha = ValueUtil.VALOR_SIM;
				return validateFechamentoPedido();
			}
			throw ex;
		} catch (AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException ex) {
			CadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow cadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow = new CadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow(pedido);
			cadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow.cadPedidoForm = this;
			cadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow.popup();
			if (!cadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow.continuaFecharPedido) return false;

			setDomain(cadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow.pedidoSimulacao);
			return validateFechamentoPedido();
		} catch (VerbaSaldoPedidoConsumidoException e) {
			int opcao = UiUtil.showMessage(e.getMessage(), TYPE_MESSAGE.WARN, new String[] {Messages.VERBASALDO_LABEL_BT_CONSUMIR_VERBA, Messages.VERBASALDO_LABEL_BT_RENEGOCIAR, Messages.BT_CANCELAR});
			if (opcao == 0) {
				pedido.deveValidarConsumoVerbaPedido = true;
				return validateFechamentoPedido();
			} else if (opcao == 1) {
				PedidoUiUtil.showListItensRenegociarConsumoVerba(this);
				return false;
			} else {
				return false;
			}
		} catch (ItemFechamentoPedidoException ex) {
			RelNotificacaoItemWindow relNotificacaoItemWindow = new RelNotificacaoItemWindow(Messages.TITULO_REL_ERROS_PEDIDO_NAO_FECHADO, pedido.erroItensFechamentoPedido, false);
			relNotificacaoItemWindow.setCadItemPedidoForm(CadItemPedidoForm.getNewCadItemPedido(this, pedido));
			relNotificacaoItemWindow.popup();
			return false;
		} catch (NaoVendaProdPedidoException ex) {
			ListNaoVendaProdPedidoWindow listMotivoNaoVendaProdutoWindow = new ListNaoVendaProdPedidoWindow(pedido);
			listMotivoNaoVendaProdutoWindow.popup();
			return !listMotivoNaoVendaProdutoWindow.closedByBtFechar;
		} catch (ValorMinimoParcelaException ex) {
			if (! LavenderePdaConfig.isLiberaComSenhaVlMinParcela()) throw ex;

			AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow();
			admSenhaDinamicaWindow.setMensagem(ex.getMessage());
			admSenhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_VALOR_MINIMO_PARCELA);
			admSenhaDinamicaWindow.setCdCliente(pedido.cdCliente);
			admSenhaDinamicaWindow.setVlTotalPedido(pedido.vlTotalPedido);
			if (admSenhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) return false;

			pedido.flValorMinParcelaLiberadoSenha = ValueUtil.VALOR_SIM;
			return validateFechamentoPedido();
		} catch (ClienteBloqueadoException e) {
			if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueado) {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(e.getMessage());
				if (ValueUtil.isNotEmpty(pedido.getCliente().dsSituacao)) {
					strBuffer.append(", ");
					strBuffer.append(pedido.getCliente().dsSituacao);
				}
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && TipoPagamentoService.getInstance().isTipoPagamentoAVista(pedido.cdTipoPagamento)) {
					SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
					pedido.flPagamentoAVista = ValueUtil.VALOR_SIM;
					return validateFechamentoPedido();
				}
				UiUtil.showErrorMessage(strBuffer.toString());
				return false;
			} else if (LavenderePdaConfig.confirmaNovoPedidoClienteBloqueado) {
				StringBuffer strBuffer = new StringBuffer();
				if (UiUtil.showConfirmYesNoMessage(strBuffer.append(e.getMessage()).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString())) {
					return validateFechamentoPedido();
				}
			}
			return false;
		} catch (BloqueioCondPagtoPorDiasClienteException ex) {
			if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto()) {
				pedido.flPendenteCondPagto = ValueUtil.VALOR_SIM;
				return validateFechamentoPedido();
			}
			return liberaBloqueioCondPagtoPorDiasSenha(pedido);
		} catch (MarcaPendenteItemBonificacaoException e) {
			return marcaPedidoPendenteComItemBonificado(pedido, e.getMessage());
		} catch (ItemPedidoSemQtItemFisicoGondolaException e) {
			return ajustaQuantidadeItensGondola(pedido, e.getMessage());
		} catch (ItemPedidoBloqueadoException e) {
			return ajustaItensBloqueados(pedido, e.getMessage());
		} catch (ItemPedidoProdutoRestritoException e) {
			return ajustaItensProdutoRestrito(pedido, e.getMessage());
		} catch (ProdutoTipoRelacaoException e) {
			return ajustaProdutosClienteRestritos(pedido, e.getMessage());
		} catch (ProdutoClienteRelacionadoException pexp) {
			return mostraPopUpProdutoClienteCod();
		} catch (KitTipo3VigenciaException e) {
			return ajustaKitTipo3Vigencia(pedido, e.kitsExtrapolados);
		} catch (DescProgressivoPersonalizadoVigenciaException e) {
			return ajustaItensDescProgressivoPersonalizadoExtrapolados(pedido);
		} catch (JustificativaMotivoPendenciaException e) {
			if (!ValueUtil.valueEquals(pedido.oldCdMotivoPendencia, pedido.cdMotivoPendencia) && pedido.oldCdMotivoPendencia != null || pedido.isPedidoPendente()) {
				limparMotivoPendencia(pedido);
			}
			return mostraAvisoPedidoPendente(pedido);
		} catch (CondicaoPagamentoDiferentePadraoClienteException ex) {
			pedido.flPendenteCondPagto = ValueUtil.VALOR_SIM;
			return validateFechamentoPedido();
		} catch (BonifCfgContaCorrenteException e) {
			return mostraPopupAjusteBonificacao(pedido, e.vlSaldo, e.vlBonificado);
		} catch (ValidationItemPedidoException e) {
			if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento 
					&& !StatusOrcamentoService.getInstance().permiteFechamentoPedido(pedido)) {
				return true;
			}
			listaItensAdvertencia(pedido);
			return SolAutorizacaoService.getInstance().isAllItensPedidoAutorizado(pedido.itemPedidoInseridosAdvertenciaList, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
		}
		avisaUsuarioDePedidoPendentePorLimCreditoOuCondPagto(pedido);
		return true;
	}

	private boolean validaEscolhaClientePedidoSemCliente(Pedido pedido) throws SQLException {
		ListClienteWindow listClienteWindow = new ListClienteWindow();
		listClienteWindow.popup();
		if (listClienteWindow.cliente != null) {
			if (naoValidaRegrasClienteAntesEscolhaPedidoSemCliente(pedido, listClienteWindow)) {
				return false;
			}
			if ("2".equals(LavenderePdaConfig.usaListaSacCliente) && SacService.getInstance().haveSacs(listClienteWindow.cliente.cdCliente)) {
				ListSacWindow listSacsWindow = new ListSacWindow(listClienteWindow.cliente);
				listSacsWindow.popup();
			}
			pedido.setCliente(listClienteWindow.cliente);
			setDomain(pedido);
			boolean retorno = executaAcoesItemPedidoEscolhaClientePedidoSemCliente();
			if (retorno && UiUtil.showConfirmYesNoMessage(Messages.MSG_DESEJA_CONFERIR_ITENS_ANTES_FECHAR_PEDIDO)) {
				return false;
			}
			return retorno;
		} else {
			return false;
		}
	}

	private boolean executaAcoesItemPedidoEscolhaClientePedidoSemCliente() throws SQLException {
		try {
			getPedido().onFechamentoPedido = true;
			getPedido().fromPedidoSemCliente = true;
			reloadTabelaPreco();
			cbTabelaPrecoClick();
			cbCondicaoPagamentoChange();
			cbTipoPagamentoChange();
			if (LavenderePdaConfig.usaCondicaoComercialPedido) {
				cbCondicaoComercialChange();
			}
		} catch (ItensComProblemaPedidoSemClienteException ve) {
			return false;
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return false;
		} finally {
			getPedido().onFechamentoPedido = false;
			getPedido().fromPedidoSemCliente = false;
		}
		return true;
	}

	private boolean naoValidaRegrasClienteAntesEscolhaPedidoSemCliente(Pedido pedido, ListClienteWindow listClienteWindow) throws SQLException {
		SessionLavenderePda.setCliente(listClienteWindow.cliente);
		try {
			if (cadClienteMenuForm == null) {
				cadClienteMenuForm = new CadClienteMenuForm();
			}
			if (cadClienteMenuForm.notValidateActionsOnSelectCliente()) {
				return true;
			}
			if (cadClienteMenuForm.periodoEntradaParaPedidoNaoValido()) {
				return true;
			}
			if (!cadClienteMenuForm.beforeCreateNovoPedido(listClienteWindow.cliente)) {
				return true;
			}
		} catch (Throwable e) {
			return true;
		} finally {
			SessionLavenderePda.setCliente(pedido.getCliente());
		}
		return false;
	}

	private boolean validaEscolhaNovoClientePedidoSemCliente(Pedido pedido) throws SQLException {
		ListNovoClienteWindow listNovoClienteWindow = new ListNovoClienteWindow(true, pedido);
		listNovoClienteWindow.popup();
		if (LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
			String messageCliente = ClienteService.getInstance().verificaClienteAberturaOuFundacao(pedido.getCliente());
			validaLimiteCreditoInCondicaoPagamentoChange(pedido);
			if (ValueUtil.isNotEmpty(messageCliente)) {
				UiUtil.showWarnMessage(messageCliente);
				UiUtil.showWarnMessage(Messages.NOVO_CLIENTE_PEDIDO_CONDICAO_PAGAMENTO);
				cbCondicaoPagamento.loadCondicoesPagamento(pedido);
				domainToScreen(pedido);
				return false;
			}
		}
		return true;
	}

	private boolean mostraPopupAjusteBonificacao(Pedido pedido, double vlSaldo, double vlBonificado) throws SQLException {
		String message = vlSaldo > 0d ? Messages.BONIFCFGCONTACORRENTE_SALDO_INSUFICIENTE_AVISO : Messages.BONIFCFGCONTACORRENTE_SALDO_INEXISTENTE_AVISO; 
		if (UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(message, new Object[]{StringUtil.getStringValueToInterface(vlSaldo),
				StringUtil.getStringValueToInterface(vlBonificado)}))) {
			BonifCfgService.getInstance().ajustaBonifContaCorrente(pedido);
			return validateFechamentoPedido();
		}
		return false;
	}

	private void limparMotivoPendencia(Pedido pedido) {
		if (!LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao()) return;
		pedido.cdMotivoPendencia = null;
		pedido.cdMotivoPendenciaJust = null;
		pedido.dsObsMotivoPendencia = null;
	}

	private boolean ajustaItensDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		UiUtil.showWarnMessage(Messages.DESC_PROG_CONFIG_VIGENCIA_EXTRAPOLADA_ERROR);
		PedidoService.getInstance().recalculaItensDescProgressivoPersonalizadoExtrapolados(pedido);
		updateVlTotalPedido();
		return false; //sempre é para voltar para a capa do pedido.
	}

	private boolean ajustaKitTipo3Vigencia(Pedido pedido, Vector kitsExtrapolados) throws SQLException {
		ListKitTipo3VigenciaExtrapoladaWindow listKitTipo3VigenciaExtrapoladaWindow = new ListKitTipo3VigenciaExtrapoladaWindow(kitsExtrapolados);
		listKitTipo3VigenciaExtrapoladaWindow.popup();
		if (listKitTipo3VigenciaExtrapoladaWindow.excluiuKits) {
			Vector itemPedidoList = new Vector();
			itemPedidoList.addElementsNotNull(pedido.itemPedidoList.items);
			int size = itemPedidoList.size();
			int sizeKit = kitsExtrapolados.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				for (int j = 0; j < sizeKit; j++) {
					if (ValueUtil.valueEquals(itemPedido.cdKit, ((Kit) kitsExtrapolados.items[j]).cdKit)) {
						PedidoService.getInstance().deleteItemPedido(pedido, itemPedido);
					}
				}
			}
			updateVlTotalPedido();
		}
		return false; //sempre é para voltar para a capa do pedido.
	}

	private boolean mostraPopUpProdutoClienteCod() throws SQLException {
		Pedido pedido = getPedido();
		Cliente cliente = SessionLavenderePda.getCliente();
		ListProdutoClienteCodWindow listProdutoClienteCodWindow = new ListProdutoClienteCodWindow(cliente, pedido);
		listProdutoClienteCodWindow.popup();
		if (listProdutoClienteCodWindow.fecharPedido) {
			return validateFechamentoPedido();
		}
		return false;
	}

	private boolean ajustaItensProdutoRestrito(Pedido pedido, String msgError) throws SQLException {
		int result = UiUtil.showMessage(msgError, TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_VOLTAR, Messages.PEDIDO_BUTTON_EXCLUIR_TODOS, Messages.BOTAO_VER_ITENS}, WIDTH_GAP_BIG * 4);
		List<ItemPedido> itemPedidosProdutoRestrito = new ArrayList<>();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			RestricaoProduto restricaoProduto = RestricaoService.getInstance().isProdutoRestrito(itemPedido.cdProduto, pedido.cdCliente, null, itemPedido.getQtItemFisico());
			if (restricaoProduto != null) {
				itemPedido.inCarrousel = true;
				itemPedido.restricaoProduto = restricaoProduto;
				itemPedidosProdutoRestrito.add(itemPedido);
			}
		}
		if (result == 1) { // excluir todos
			return deleteAllFromCarrousel(pedido, itemPedidosProdutoRestrito);
		} else if (result == 2) { // ver itens
			CadItemPedidoForm cadItemPedidoForm = setupCarrousel(pedido, itemPedidosProdutoRestrito, CadItemPedidoFormWindow.CARROUSEL_TYPE_RESTRITO);
			if (ajustouItensCarrousel(pedido, cadItemPedidoForm.ajustouTodosItensProdutoRestrito())) {
				return validateFechamentoPedido();
			}
			calculateAfterCarrousel(pedido);
		}
		return false;
	}

	private boolean ajustaItensBloqueados(Pedido pedido, String msgError) throws SQLException {
		int result = UiUtil.showMessage(msgError, TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_VOLTAR, Messages.PEDIDO_BUTTON_EXCLUIR_TODOS, Messages.BOTAO_VER_ITENS}, WIDTH_GAP_BIG * 4);
		List<ItemPedido> itemPedidosBloqueados = new ArrayList<>();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (ProdutoBloqueadoService.getInstance().isBloqueadoAllTabelaPreco(itemPedido) || ProdutoBloqueadoService.getInstance().isBloqueadoForTabelaPreco(itemPedido)) {
				itemPedido.inCarrousel = true;
				itemPedidosBloqueados.add(itemPedido);
			}
		}
		if (result == 1) { // excluir todos
			return deleteAllFromCarrousel(pedido, itemPedidosBloqueados);
		} else if (result == 2) { // ver itens
			CadItemPedidoForm cadItemPedidoForm = setupCarrousel(pedido, itemPedidosBloqueados, CadItemPedidoFormWindow.CARROUSEL_TYPE_BLOQUEADO);
			if (ajustouItensCarrousel(pedido, cadItemPedidoForm.ajustouTodosItensBloqueados())) {
				return validateFechamentoPedido();
			}
			calculateAfterCarrousel(pedido);
		}
		return false;
	}

	private void calculateAfterCarrousel(Pedido pedido) throws SQLException {
		PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
		updateVlTotalPedido();
	}

	private boolean ajustaQuantidadeItensGondola(Pedido pedido, String msgError) throws SQLException {
		int result = UiUtil.showMessage(msgError, TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_VOLTAR, Messages.BOTAO_VER_ITENS});
		if (result == 1) {
			int size = pedido.itemPedidoList.size();
			List<ItemPedido> itemPedidosInvalidos = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.getQtItemFisico() == 0) {
					itemPedidosInvalidos.add(itemPedido);
				}
			}
			CadItemPedidoForm cadItemPedidoForm = setupCarrousel(pedido, itemPedidosInvalidos, CadItemPedidoFormWindow.CARROUSEL_TYPE_GONDOLA);
			if (ajustouItensCarrousel(pedido, cadItemPedidoForm.ajustouTodosItensSemQuantidade())) {
				return validateFechamentoPedido();
			}
		}
		return false;
	}

	private boolean ajustaProdutosClienteRestritos(Pedido pedido, String msgError) throws SQLException {
		int result = UiUtil.showMessage(msgError, TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_VOLTAR, Messages.PEDIDO_BUTTON_EXCLUIR_TODOS, Messages.BOTAO_VER_ITENS}, WIDTH_GAP_BIG * 4);
		if (result > 0) {
			int size = pedido.itemPedidoList.size();
			List<ItemPedido> itemPedidosInvalidos = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				try {
					ProdutoService.getInstance().validateProdutoRelacaoDisponivel(pedido, itemPedido);
				} catch (ProdutoTipoRelacaoException e) {
					itemPedidosInvalidos.add(itemPedido);
				}
			}
			if (result == 1) { // excluir todos
				return deleteAllFromCarrousel(pedido, itemPedidosInvalidos);
			} else if (result == 2) {
				CadItemPedidoForm cadItemPedidoForm = setupCarrousel(pedido, itemPedidosInvalidos, CadItemPedidoFormWindow.CARROUSEL_TYPE_PRODUTOCLIENTE);
				if (ajustouItensCarrousel(pedido, cadItemPedidoForm.ajustouTodosItensProdutoClienteRestrito())) {
					return validateFechamentoPedido();
				}
			}
		}
		return false;
	}

	private boolean ajustouItensCarrousel(Pedido pedido, boolean ajustou) throws SQLException {
		clearMemoryAfterAjusteCarrousel(pedido);
		return ajustou;
	}

	private boolean deleteAllFromCarrousel(Pedido pedido, List<ItemPedido> itemPedidoCarrouselList) throws SQLException {
		HashSet<String> cdKitListParaExcluir = new HashSet<>();
		List<ItemPedido> itemPedidoComboListParaExcluir = new ArrayList<>();
		for (ItemPedido itemPedido : itemPedidoCarrouselList) {
			if (itemPedido.isKitTipo3()) {
				cdKitListParaExcluir.add(itemPedido.cdKit);
		}
			if (itemPedido.isCombo() && LavenderePdaConfig.isExibeComboMenuInferior()) {
				itemPedidoComboListParaExcluir.add(itemPedido);
			} else {
				getPedidoService().deleteItemPedido(pedido, itemPedido);
			}
		}
		if (!cdKitListParaExcluir.isEmpty()) {
			for (String cdKit : cdKitListParaExcluir) {
				getPedidoService().deleteItensKit(pedido, cdKit);
			}
		}
		if (!itemPedidoComboListParaExcluir.isEmpty()) {
			for (ItemPedido itemPedido : itemPedidoComboListParaExcluir) {
				ItemPedidoService.getInstance().deleteItensCombo(itemPedido);
			}
		}
		clearMemoryAfterAjusteCarrousel(pedido);
		afterCrudItemPedido();
		return pedido.itemPedidoList.size() > 0 && itemPedidoComboListParaExcluir.isEmpty() && cdKitListParaExcluir.isEmpty() && validateFechamentoPedido();
	}

	private CadItemPedidoForm setupCarrousel(Pedido pedido, List<ItemPedido> itemPedidosList, int type) throws SQLException {
		CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getNewCadItemPedido(this, pedido);
		if (type == CadItemPedidoFormWindow.CARROUSEL_TYPE_GONDOLA) {
			cadItemPedidoForm.configureCarrouselItemPedido(itemPedidosList, null, type);
			cadItemPedidoForm.editFromExternalForm(itemPedidosList.get(0), this);
		} else {
			CadItemPedidoFormWindow cadItemPedidoFormWindow = new CadItemPedidoFormWindow(cadItemPedidoForm);
			cadItemPedidoForm.configureCarrouselItemPedido(itemPedidosList, cadItemPedidoFormWindow, type);
			cadItemPedidoForm.edit(itemPedidosList.get(0));
			cadItemPedidoFormWindow.showWindow();
			for (ItemPedido itemPedido : itemPedidosList) {
				itemPedido.inCarrousel = false;
			}
		}
		return cadItemPedidoForm;
	}

	private void clearMemoryAfterAjusteCarrousel(Pedido pedido) throws SQLException {
		CadItemPedidoForm.invalidateInstance();
		pedido.itemPedidoList = null;
		PedidoService.getInstance().findItemPedidoList(pedido, true);
	}

	private boolean marcaPedidoPendenteComItemBonificado(Pedido pedido, String msgError) throws SQLException {
		if (LavenderePdaConfig.obrigaMotivoBonificacao()) {
			CadMotivoItemBonificadoWindow cadMotivoItemBonificadoWindow = new CadMotivoItemBonificadoWindow();
			cadMotivoItemBonificadoWindow.popup();
			if (cadMotivoItemBonificadoWindow.fechadoPeloBtCancelar) {
				return false;
			} else {
				pedido.dsMotivoBonificacao = cadMotivoItemBonificadoWindow.edMotivoItemBonificado.getValue();
			}
		} else {
			UiUtil.showWarnMessage(msgError);
		}
		pedido.flPendente = ValueUtil.VALOR_SIM;
		return validateFechamentoPedido();
	}

	private boolean liberaBloqueioCondPagtoPorDiasSenha(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow(SenhaDinamica.SENHA_LIBERACAO_CONDICAO_PAGAMENTO_POR_DIAS_PERMITIDO_CLIENTE);
		admSenhaDinamicaWindow.setMensagem(getMessageForPasswordScreenCondPagtoBloq(pedido));
		admSenhaDinamicaWindow.setCdCliente(cliente.cdCliente);
		admSenhaDinamicaWindow.setVlTotalPedido(pedido.vlTotalPedido);
		admSenhaDinamicaWindow.setLvCdCondPagto(pedido.getCondicaoPagamento().toString() != null ? pedido.getCondicaoPagamento().toString() : pedido.getCondicaoPagamento().cdCondicaoPagamento);
		admSenhaDinamicaWindow.setCdCondicaoPagamento(pedido.cdCondicaoPagamento);
		if (admSenhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) return false;
		pedido.qtDiasCPgtoLibSenha = LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMediosCliente() ? condicaoPagamento.qtDiasMediosPagamento : condicaoPagamento.qtDiasMaximoPagamento;
		pedido.vlTotalPedidoLiberado = pedido.vlTotalPedido;
		return validateFechamentoPedido();
	}

	private String getMessageForPasswordScreenCondPagtoBloq(Pedido pedido) {
		return pedido.qtDiasCPgtoLibSenha > 0 && pedido.vlTotalPedidoLiberado > pedido.vlTotalPedido ? Messages.MSG_LIBERA_COM_SENHA_CONDICAOPAGAMENTO_BLOQ_DIAS_CLIENTE_DIFF_VALOR : Messages.MSG_LIBERA_COM_SENHA_CONDICAOPAGAMENTO_BLOQ_DIAS_CLIENTE;
	}


	@Override
	protected void save() throws java.sql.SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			super.save();
		} finally {
			mb.unpop();
		}
	}

	@Override
	protected void updateCurrentRecordInList(BaseDomain domain) throws SQLException {
		if (cadClienteMenuForm != null && cadClienteMenuForm.getBaseCrudListForm() instanceof ListClienteForm) {
			Cliente cliente = ((Pedido) domain).getCliente();
			if (LavenderePdaConfig.grifaClienteBloqueado || LavenderePdaConfig.grifaClienteAtrasado || LavenderePdaConfig.destacaClienteBloqueadoPorAtrasoNaLista) {
				cliente.loadStatusCliente();
			}
			cadClienteMenuForm.getBaseCrudListForm().updateCurrentRecord(cliente);
		} else {
			super.updateCurrentRecordInList(domain);
	}
	}

	private void btReabrirPedidoClick() throws SQLException {
		if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
		if (LavenderePdaConfig.validaPeriodoEntregaParaPedido && !ClienteEnderecoService.getInstance().isTodosEnderecosComPeriodoEntrega(getPedido().getCliente())) {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_SEM_PERIODO_ENTREGA);
			return;
		}
		Pedido pedido = (Pedido) getDomain();
		if (ValueUtil.VALOR_SIM.equals(pedido.flBloqueadoEdicao)) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_BLOQUEADO_EDICAO);
			return;
		}
		//Confirma a reabertura
		int res = UiUtil.showConfirmYesCancelMessage(Messages.PEDIDO_MSG_CONFIRM_REABRIRPEDIDO);
		if (res != 1) {
			return;
		}
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			PedidoService.validationFechamentoListCount = 0;
			if (!PedidoUiUtil.validateReabrirPedidoUI(pedido)) {
				return;
			}
			try {
				PedidoService.getInstance().reabrirPedido(pedido);
				updateCurrentRecordInList(getDomain());
				pedido.ignoraValidacaoProdutosPendentes = false;
				pedido.ignoraAvisoSugestaoVendaSemQtdOutrasEmpresas = false;
				pedido.ignoraAvisoSugestaoVendaComQtdOutrasEmpresas = false;
				pedido.ignoraGiroProdutoPendente = false;
				pedido.isAdiconandoItemRelProdutosPendentes = false;
				clearScreenTransportadoraReg();
				if (pedido.isPedidoComplementar()) {
					cbCondicaoPagamento.setVisible(true);
				}
				afterReabrirPedido();
			} catch (PedidoNaoFechadoException e) {
				pedido.cdStatusPedido = PedidoService.getInstance().findColumnByRowKey(pedido.getRowKey(), Pedido.NMCOLUNA_CDSTATUSPEDIDO);
				throw new ValidationException(e.getMessage());
			} catch (Throwable e) {
				PedidoService.getInstance().atualizaStatusPedido(pedido, LavenderePdaConfig.cdStatusPedidoFechado, false);
				pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
				throw e;
			}
		} finally {
			msg.unpop();
			edit(pedido);
			onFormShow();
		}
	}

	//@Override
	protected void onSave() throws java.sql.SQLException {
		Pedido pedido = getPedido();
		if (!pedido.isPedidoBonificacao()) {
			if (PedidoUiUtil.positivacaoItensByFornecedores(this, pedido, isEnabled())) {
				return;
			}
			if (!validadeFechamentoPedidoOnSave()) {
				return;
			}
		}
		super.onSave();
		naoConsisteValidacaoFechamentoAoSalvar = false;
		//--
		if (LavenderePdaConfig.usaNovoPedidoParaMesmoCliente) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_NOVO_PEDIDO_MESMO_CLIENTE)) {
				new CadClienteMenuForm().btNovoPedidoClick();
			}
		}
	}

	public void setPropVisitaNoPedido(Visita visita) throws SQLException {
        Pedido pedido = getPedido();
		pedido.insertVisita = true;
		pedido.setVisita(visita);
	}

	protected void setNotaCreditoPedido(Vector notaCreditoSelecionadaList) throws SQLException {
		if (ValueUtil.isEmpty(notaCreditoSelecionadaList)) return;

		PedidoService.getInstance().criaNotaCreditoPedido(notaCreditoSelecionadaList, getPedido());
	}

	public void historicoTabelaPrecoClick() throws SQLException {
		Pedido pedido = getPedido();
		RelHistoricoTabelaPrecoWindow infoUltimasTabsUsadasForm = new RelHistoricoTabelaPrecoWindow(pedido.cdCliente);
		infoUltimasTabsUsadasForm.popup();
		boolean cbTabelaPrecoVisible = isEnabled() &&
						!(LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() &&
						(pedido.itemPedidoList.size() > 0)) &&
						!LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido &&
						!(pedido.itemPedidoList.size() > 0);
		if (!ValueUtil.isEmpty(infoUltimasTabsUsadasForm.cdTabelaPrecoSelected) && cbTabelaPrecoVisible) {
			String cdTabPrecoAtual = cbTabelaPreco.getValue();
			cbTabelaPreco.setValue(infoUltimasTabsUsadasForm.cdTabelaPrecoSelected);
			if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
				UiUtil.showErrorMessage(Messages.TABELAPRECO_INEXISTENTE);
				cbTabelaPreco.setValue(cdTabPrecoAtual);
			}
			pedido.cdTabelaPreco = cbTabelaPreco.getValue();
			cbTabelaPrecoChange();
		}
		repaint();
	}

	private void previsaoDescontoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			pedido.fecharPedidoComVerbaNeg = false;
			RelPreviaDescontosComVerbaWindow relPreviaDescontosComVerbaForm = new RelPreviaDescontosComVerbaWindow(pedido, false);
			relPreviaDescontosComVerbaForm.cadPedidoForm = this;
			relPreviaDescontosComVerbaForm.popup();
			if (pedido.fecharPedidoComVerbaNeg) {
				fecharPedido();
				pedido.fecharPedidoComVerbaNeg = !pedido.fecharPedidoComVerbaNeg;
			}
		} else {
			RelPreviaDescontosWindow relPreviaDescWindow = new RelPreviaDescontosWindow(pedido);
			relPreviaDescWindow.popup();
		}
	}

	private void fichaFinanceiraClick() throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(getPedido().getCliente());
		if (fichaFinanceira != null) {
			CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm();
			cadFichaFinanceiraForm.edit(fichaFinanceira);
			show(cadFichaFinanceiraForm);
		} else {
			UiUtil.showWarnMessage(Messages.MSG_CLIENTE_SEM_FICHAFINANCEIRA);
		}
	}

	private void btExtratoCCClienteClick() throws SQLException {
		ListContaCorrenteCliForm listContaCorrenteCliForm = new ListContaCorrenteCliForm(getPedido().getCliente());
		show(listContaCorrenteCliForm);
	}

	private void btUltimosPedidosClick() throws SQLException {
		ListPedidoForm listPedidoForm = new ListPedidoForm(getPedido());
		listPedidoForm.inConsultaUltimosPedidos = true;
		show(listPedidoForm);
	}

	public void rentabilidadeClick() throws SQLException {
		RelRentabilidadeSaldoWindow verbaForm = new RelRentabilidadeSaldoWindow(getPedido());
		verbaForm.popup();
	}

	private void diferencasPedidoclick() throws SQLException {
		if (!LavenderePdaConfig.isShowDifPedido() && !LavenderePdaConfig.isShowDifItemPedido()) {
			UiUtil.showErrorMessage(Messages.CONFIG_PARAM_1766_ERRADA);
			return;
		}
		RelDiferencasPedidoWindow relDiferencasPedidoWindow = new RelDiferencasPedidoWindow(getPedido());
		relDiferencasPedidoWindow.popup();
		if (relDiferencasPedidoWindow.btCriarPedidoDiferencaClicado) {
			criaPedidoDiferenca(getPedido(), relDiferencasPedidoWindow.itemPedidoErpDifComCorteList);
		}
	}

	private void criaPedidoDiferenca(Pedido pedido, Vector itemPedidoErpDifComCorteList) throws SQLException {
		boolean criouPedidoDiferenca = false;
		Pedido pedidoRef = (Pedido) pedido.clone();
		try {
			UiUtil.showProcessingMessage();
			pedido.itemPedidoList = getNovoItemPedidoAPartirdeItensDeCorteList(pedido, itemPedidoErpDifComCorteList);
			pedido.nuPedidoDiferenca = pedido.nuPedido;
			Cliente cliente = pedido.getCliente();
			if (!beforeCreateNovoPedido(cliente)) return;
			Pedido novoPedido = ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, cliente, pedido.itemPedidoList, false, false, false);
			PedidoService.getInstance().updatePedidoDiferenca(pedido, ValueUtil.VALOR_SIM);
			inOnlyConsultaItens = false;
			if (novoPedido != null) {
				criouPedidoDiferenca = true;
				edit(novoPedido);
				if (pedido.hasErrosInsertSugestaoPedido()) {
					RelItemPedidoDivergenciaWindow relProdutoErroWindow = new RelItemPedidoDivergenciaWindow(Messages.PEDIDO_ITENS_NAO_INSERIDOS, getPedido());
					relProdutoErroWindow.popup();
				}
				if (ValueUtil.isNotEmpty(pedido.itemPedidoEstoquePrevistoExcepList)) {
					RelItemPedidoEstoquePrevistoExcep relItemPedidoEstoquePrevistoExcep = new RelItemPedidoEstoquePrevistoExcep(pedido.itemPedidoEstoquePrevistoExcepList);
					relItemPedidoEstoquePrevistoExcep.popup();
				}
			}
		} finally {
			if (!criouPedidoDiferenca) {
				setDomain(pedidoRef);
			}
			UiUtil.unpopProcessingMessage();
		}

	}

	private Vector getNovoItemPedidoAPartirdeItensDeCorteList(Pedido pedido, Vector itemPedidoErpDifComCorteList) throws SQLException {
		Vector newItemPedidoList = new Vector();
		Vector itemPedidoList = (Vector) ItemPedidoService.getInstance().findItemPedidoByPedido(pedido);
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			montaNovaListaDeItemPedidoAPartirdeItensDeCorte(pedido, itemPedidoErpDifComCorteList, newItemPedidoList, itemPedido);
		}
		return newItemPedidoList;
	}

	private void montaNovaListaDeItemPedidoAPartirdeItensDeCorte(Pedido pedido, Vector itemPedidoErpDifComCorteList, Vector newItemPedidoList, ItemPedido itemPedido) throws SQLException {
		for (int j = 0; j < itemPedidoErpDifComCorteList.size(); j++) {
			ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif) itemPedidoErpDifComCorteList.items[j];
			if (ValueUtil.valueEquals(itemPedido.cdProduto, itemPedidoErpDif.cdProduto)) {
				itemPedido.pedido = pedido;
				ItemPedidoService.getInstance().clearDadosItemPedido(pedido, itemPedido);
				itemPedido.setQtItemFisico(itemPedidoErpDif.qtItemfisicoOrg - itemPedidoErpDif.qtItemFisicoErp);
				ItemPedidoService.getInstance().aplicarConversaoUnidadeMedida(itemPedido, pedido);
				PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
				ItemPedidoService.getInstance().calculate(itemPedido, pedido);
				newItemPedidoList.addElement(itemPedido);
			} else if (itemPedidoErpDif.qtItemFisicoErp == 0) {
				ItemPedido itemPedidoNovo = ItemPedidoService.getInstance().createNewItemPedido(pedido);
				itemPedidoNovo.cdProduto = itemPedidoErpDif.cdProduto;
				if (!newItemPedidoList.contains(itemPedidoNovo)) {
					ItemPedidoService.getInstance().clearDadosItemPedido(pedido, itemPedido);
					itemPedidoNovo.setQtItemFisico(itemPedidoErpDif.qtItemfisicoOrg);
					itemPedidoNovo.cdTabelaPreco = pedido.cdTabelaPreco;
					ItemPedidoService.getInstance().aplicarConversaoUnidadeMedida(itemPedidoNovo, pedido);
					PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedidoNovo);
					ItemPedidoService.getInstance().calculate(itemPedidoNovo, pedido);
					newItemPedidoList.addElement(itemPedidoNovo);
				}
			}
		}
	}

	private void showMessageTrocaTabPreco(boolean confirmMessage) throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.getTabelaPreco() != null && ValueUtil.isNotEmpty(pedido.getTabelaPreco().dsMsgAlerta)) {
			if (confirmMessage) {
				if (UiUtil.showConfirmYesCancelMessage(pedido.getTabelaPreco().dsMsgAlerta + " " + Messages.TABELAPRECO_MSG_DESEJA_ALTERAR) != 1) {
					cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
					pedido.cdTabelaPreco = ultimaTabelaPrecoSelected;
				}
			} else {
				if (LavenderePdaConfig.permiteAlterarTabelaPrecoPedido || (pedido.itemPedidoList.size() == 0)) {
					UiUtil.showWarnMessage(pedido.getTabelaPreco().dsMsgAlerta);
				}
			}
		}
	}


	private void cbTabelaPrecoClick() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdTabelaPreco = cbTabelaPreco.getValue();
		//--
		showMessageTrocaTabPreco(true);
		//--
		if ((LavenderePdaConfig.permiteAlterarTabelaPrecoPedido && (!pedido.itemPedidoList.isEmpty())) || pedido.fromPedidoSemCliente) {
			if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco) {
				cbCondicaoPagamento.loadCondicoesPagamento(pedido);
				if (cbCondicaoPagamento.size() == 0) {
					cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
					pedido.cdTabelaPreco = ultimaTabelaPrecoSelected;
					cbCondicaoPagamento.loadCondicoesPagamento(pedido);
					cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
					pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
					throw new ValidationException(Messages.TABELAPRECO_MSG_ALTARCAO_CONDPAGTO_INVALIDA);
				}
				cbCondicaoPagamento.setSelectedIndex(0);
				if (!validaLimiteCreditoInCondicaoPagamentoChange(pedido, true)) {
					return;
				}
				cbCondicaoPagamentoChange();
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
				setEditableEdVlPctHistVendas(pedido);
			}
			setValueEdVlMinTabelaPreco(pedido);
			setDtEntregaManualVisible(pedido);
			Vector itemErroList = new Vector();
			Vector itemAdvertenciaList = new Vector();
			Vector itensSucessoList = new Vector();
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				try {
					itemPedido.cdTabelaPrecoOld = itemPedido.cdTabelaPreco;
					itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
					if (LavenderePdaConfig.mantemPercAcrescDescProdutoAoTrocarTabPrecoPedido) {
						mantemDescontoAcrescimoAplicadoAoTrocarTabPrecoPedido(itemPedido);
					} else {
						itemPedido.vlPctDesconto = 0;
						itemPedido.vlPctAcrescimo = 0;
					}
					if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && itemPedido.permiteAplicarDesconto() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL)) {
						ItemPedidoService.getInstance().clearDescontoPromocional(itemPedido);
						if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) {
							DescPromocionalService.getInstance().loadMaiorFaixaDescPromocionalPorQuantidadeItemPedido(itemPedido);
						}
					}
					if (itemPedido.vlPctAcrescimo > 0.0) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
					} else {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
					}
					if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
						itemPedido.atualizandoDesc = true;
					}
					PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
					PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
					itensSucessoList.addElement(itemPedido);
				} catch (Throwable e) {
					addProdutoErroListaRelatorio(e, itemPedido, pedido, itemErroList, itemAdvertenciaList);
				}
			}
			LoadingBoxWindow mb = UiUtil.createProcessingMessage();
			mb.popupNonBlocking();
			if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
				TabelaPreco tabPreco = pedido.getTabelaPreco();
				boolean enabled = tabPreco == null ? isEnabled() : tabPreco.isPermiteDesconto() && isEnabled();
				edDescCascataCategoria1.setEditable(enabled && pedido.getCliente().vlIndiceFinanceiro < 1);
				refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), enabled);
			}
			try {
				if (itemErroList.isEmpty() && itemAdvertenciaList.isEmpty()) {
					getPedidoService().updateItensPedidoAfterChanges(pedido);
					getPedidoService().calculate(pedido);
					getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
					getPedidoService().update(pedido);
				} else {
					if (ValueUtil.isNotEmpty(itensSucessoList)) {
						pedido.itemPedidoList = itensSucessoList;
						getPedidoService().updateItensPedidoAfterChanges(pedido);
					}
					edit(getPedidoService().findByRowKeyDyn(pedido.getRowKey()));
					if (pedido.fromPedidoSemCliente && pedido.onFechamentoPedido) {
						showNotificacoesItensComErroPedidoSemCliente(itemErroList, itemAdvertenciaList, pedido);
					} else {
						RelNotificacaoItemWindow relInsercaoItensPedidoForm = new RelNotificacaoItemWindow(itemErroList, false);
						relInsercaoItensPedidoForm.popup();
					}
				}
			} finally {
				mb.unpop();
				updateVlTotalPedido();
				ultimaTabelaPrecoSelected = cbTabelaPreco.getValue();
			}
		} else {
			cbTabelaPrecoChange();
			ultimaTabelaPrecoSelected = cbTabelaPreco.getValue();
			updateVlTotalPedido();
		}
	}

	private void addProdutoErroListaRelatorio(Throwable e, ItemPedido itemPedido, Pedido pedido, Vector itemErroList, Vector itemAdvertenciaList) throws SQLException {
		ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, e.getMessage());
		if (pedido.fromPedidoSemCliente && pedido.onFechamentoPedido) {
			produtoErro.itemPedidoErro = itemPedido;
			if (e instanceof ProdutoSemPrecoException) {
				itemErroList.addElement(produtoErro);
			} else {
				itemAdvertenciaList.addElement(produtoErro);
			}
		} else {
			itemErroList.addElement(produtoErro);
		}
	}

	private void showNotificacoesItensComErroPedidoSemCliente(Vector itemErroList, Vector itemAdvertenciaList, Pedido pedido) throws SQLException {
		RelNotificacaoItemWindow relInsercaoItensPedidoForm = new RelNotificacaoItemWindow(Messages.REL_NOTIFICACAO_ITENS, itemErroList, itemAdvertenciaList, false, Messages.TITULO_REL_ITENS_COM_PROBLEMA_PEDIDO_SEM_CLIENTE, true);
		relInsercaoItensPedidoForm.setClearErrorLists(false);
		relInsercaoItensPedidoForm.popup();
		for (int i = 0; i < itemErroList.size(); i++) {
			ProdutoErro itemErro = (ProdutoErro) itemErroList.elementAt(i);
			ItemPedidoService.getInstance().delete(itemErro.itemPedidoErro);
		}
		PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
		PedidoService.getInstance().findItemPedidoList(pedido, true);
		setDomain(pedido);
		String conferirItens = relInsercaoItensPedidoForm.conferirInconsistencias ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		throw new ItensComProblemaPedidoSemClienteException(Messages.MSG_ERRO_PROBLEMA_FECHAR_PEDIDO_SEM_CLIENTE_PRODUTOS_ERRO, conferirItens);
	}

	private void mantemDescontoAcrescimoAplicadoAoTrocarTabPrecoPedido(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		if (!TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido) && itemPedido.permiteAplicarDesconto()) {
			double vlPctMaxDesc = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			if (itemPedido.vlPctDesconto > vlPctMaxDesc) {
				itemPedido.vlPctDesconto = vlPctMaxDesc;
			}
		}
		if (!TipoPedidoService.getInstance().isUsaAcrescimoMaximoPorTipoPedido(itemPedido.pedido) && itemPedido.permiteAplicarDesconto()) {
			double vlPctMaxAcrescimo = itemTabPreco.vlPctMaxAcrescimo;
			if (itemPedido.vlPctAcrescimo > vlPctMaxAcrescimo) {
				itemPedido.vlPctAcrescimo = vlPctMaxAcrescimo;
			}
		}
	}

	private void cbCondicaoPagamentoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.isBloqueiaCondPagtoPorClienteNoCombo()) {
			try {
				PedidoService.getInstance().validateCondPagtoPorCliente(pedido.getCliente().cdCondicaoPagtoBloqueada, cbCondicaoPagamento.getValue());
			}catch(ValidationException e){
				exceptionHandleCbCondicaoPagamento(pedido);
				throw e;
			}
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
			if ((LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente()) && ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
				cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
				if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
					cbCondicaoPagamento.setSelectedIndex(0);
				}
				pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
			} else if (pedido.getCondicaoPagamento().cdPrazoPagtoPreco == 0 || pedido.getCondicaoPagamento().getPrazoPagtoPreco() == null) {
				cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
				pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
				throw new ValidationException(Messages.CONDICAOPAGAMENTO_SEM_PRAZOPAGTOPRECO);
			}
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			if (pedido.getCondicaoPagamento().vlIndiceFinanceiro != ((CondicaoPagamento)cbCondicaoPagamento.getSelectedItem()).vlIndiceFinanceiro &&
					pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, null)) {
				String dsCondicaoPagamento = ((CondicaoPagamento)cbCondicaoPagamento.getSelectedItem()).dsCondicaoPagamento;
				cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_CONDICAO_PAGAMENTO_INDICE_DIFERENTE, new Object[] {pedido.getCondicaoPagamento().dsCondicaoPagamento, dsCondicaoPagamento}));
			}
		}

		try {
			atualizaPagamentoAVistaParaValidacao(pedido);
			validaLimiteCreditoInCondicaoPagamentoChange(pedido);
		} finally {
			//necessario para nao afetar o legado do metodo cbCondicaoPagamentoChange()
			reverteCondicaoPagamentoPosValidacao(pedido);
		}
		cbCondicaoPagamentoChange();
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
			CondicaoPagamento condicaoPagamento = (CondicaoPagamento) cbCondicaoPagamento.getSelectedItem();
			if (condicaoPagamento != null) {
				lbVlMaxPctDescCondicao.setValue(StringUtil.getStringValueToInterface(condicaoPagamento.vlPctDescontoTotalPedido));
			}
		}
		if (LavenderePdaConfig.usaCondComercialPorCondPagto && ValueUtil.isNotEmpty(cbCondicaoPagamento.getValue())) {
			reloadComboCondicaoComercial();
		}
		if (pedido.cdCondicaoPagamento != null && isEditing() && !pedido.cdCondicaoPagamento.equals(pedido.oldCdCondicaoPagamento) && pedido.itemPedidoList.size() > 0) {
			boolean aplicaIndicesFinanceiros = LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() || LavenderePdaConfig.indiceFinanceiroCondPagtoPorDias || LavenderePdaConfig.aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido > 0 || LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial || LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto;
			if (LavenderePdaConfig.usaControlePontuacao || aplicaIndicesFinanceiros
					|| LavenderePdaConfig.aplicaDescontoPedidoRepEspecial
					|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()
					|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()
					|| !ValueUtil.valueEquals(LavenderePdaConfig.usaPrecoPorUnidadeQuantidadePrazo, 'N')
					|| (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente)
					|| LavenderePdaConfig.usaPoliticaComercial()
					|| LavenderePdaConfig.usaConfigCalculoComissao()
					|| LavenderePdaConfig.habilitaRegrasAdicionaisDescPromocional()) {
				reloadPedidoAfterCondicaoPagtoChange(pedido);
			}
		}
		double qtItensLista = pedido.getQtItensLista();
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
		}
		pedido.oldCdCondicaoPagamento = pedido.cdCondicaoPagamento;
		if (!(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente()) && qtItensLista > 0 && qtItensLista < pedido.getCondicaoPagamento().getQtMinProduto(pedido.cdTabelaPreco)) {
			UiUtil.showInfoMessage(Messages.CONDICAOPAGAMENTO_MSG_AVISO_QTD);
		}
		if (LavenderePdaConfig.exibeDescontoAcrescimoIndice()) {
			lbVlPctDescCondicaoPagamento.setValue(CondicaoPagamentoService.getInstance().loadVlPctDescAcresCondPagto(pedido));
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			pedido.loadedPoliticaComercialOnCondicaoPagtoChange = false;
		}
	}

	private void showMensagemPedidoPendenteCondPagto(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagtoDiferentePadrao()) {
			if (CondicaoPagamentoService.getInstance().isCondicaoPadraoCliente(pedido.getCliente(), cbCondicaoPagamento.getValue())) {
				return;
			}
			if (!UiUtil.showConfirmYesNoMessage(Messages.CONDICAO_PAGAMENTO_NAO_PADRAO)) {
				throw new ValidationException(ValueUtil.VALOR_NI);
			}
		} else if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto()) {
			try {
				CondicaoPagamentoService.getInstance().validateCondPagtoPorDiasCliente((CondicaoPagamento)cbCondicaoPagamento.getSelectedItem(), pedido.getCliente().qtDiasMaximoPagamento);
			} catch (BloqueioCondPagtoPorDiasClienteException e ) {
				if (!UiUtil.showConfirmYesNoMessage(e.getMessage() + " " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR)) {
					throw new ValidationException(ValueUtil.VALOR_NI);
				}
			}
		}
	}

	private void reverteCondicaoPagamentoPosValidacao(Pedido pedido) {
		if (ultimaCondPgamentoSelected == null && pedido.itemPedidoList.isEmpty()) return;
		pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
		pedido.setCondicaoPagamento( new CondicaoPagamento( SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class) , ultimaCondPgamentoSelected));
	}

	private void atualizaPagamentoAVistaParaValidacao(Pedido pedido) throws SQLException {
		if (ultimaCondPgamentoSelected == null && pedido.itemPedidoList.isEmpty()) return;
		pedido.setCondicaoPagamento((CondicaoPagamento) cbCondicaoPagamento.getSelectedItem());
		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
		PedidoService.getInstance().setPedidoComPagamentoAVista(pedido);
	}

	private void reloadPedidoAfterCondicaoPagtoChange(Pedido pedido) throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		try {
			mb.popupNonBlocking();
			reloadAndUpdateValoresDoPedido(pedido, true);
			if (!pedido.ignoraRecalculoItens && pedido.itensComErroAndExceptionMap.size() > 0) {
				alteraParaCondicaoPagamentoAnterior(pedido);
				mostraPopupItensComErro(pedido);
				cbCondicaoPagamentoChange();
			}
			pedido.cdCondicaoPagamentoChanged = false;
		} finally {
			mb.unpop();
		}
	}
	
	private void mostraPopupItensComErro(Pedido pedido) throws SQLException {
		new ListItensComErroWindow(pedido).popup();
	}
	
	private void alteraParaCondicaoPagamentoAnterior(Pedido pedido) throws SQLException {
		pedido.cdCondicaoPagamento = pedido.oldCdCondicaoPagamento;
		cbCondicaoPagamento.setValue(pedido.oldCdCondicaoPagamento);
	}

	private boolean validaLimiteCreditoInCondicaoPagamentoChange(Pedido pedido) throws SQLException {
		return validaLimiteCreditoInCondicaoPagamentoChange(pedido, false);
	}

	private boolean validaLimiteCreditoInCondicaoPagamentoChange(Pedido pedido,  boolean validaAoTrocarTabelaPreco) throws SQLException {
		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
		if ((LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) && (!ValueUtil.VALOR_SIM.equals(pedido.flPedidoNovoCliente))) {
			if (pedido.getCondicaoPagamento().isIgnoraLimiteCredito()) {
				return true;
			}
			if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco && validaAoTrocarTabelaPreco) {
				Vector listCondPagto = new Vector(cbCondicaoPagamento.getItems());
				if (ValueUtil.isNotEmpty(listCondPagto)) {
					int size = listCondPagto.size();
					for (int i = 0; i < size; i++) {
						CondicaoPagamento condicaoPagamento = (CondicaoPagamento) listCondPagto.items[i];
						if (condicaoPagamento.isIgnoraLimiteCredito()) {
							cbCondicaoPagamento.setValue(condicaoPagamento.cdCondicaoPagamento);
							break;
						}
					}
				}
			}
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				try {
					//FichaFinanceiraService.getInstance().validateLimCred();
					FichaFinanceiraService.getInstance().validateLimCred(pedido ,null);
				} catch (ValidationException e) {
					if (LavenderePdaConfig.bloquearLimiteCreditoCliente) {
						String dsTabPreco = "";
						String dsCondPagto = "";
						if (cbCondicaoPagamento != null && cbCondicaoPagamento.getSelectedItem() != null) {
							dsCondPagto = ((CondicaoPagamento) cbCondicaoPagamento.getSelectedItem()).dsCondicaoPagamento;
						}
						if (cbTabelaPreco != null && cbTabelaPreco.getSelectedItem() != null) {
							dsTabPreco = StringUtil.getStringValue(((TabelaPreco) cbTabelaPreco.getSelectedItem()).dsTabelaPreco);
						}
						//--
						if (validaAoTrocarTabelaPreco) {
							cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
							pedido.cdTabelaPreco = ultimaTabelaPrecoSelected;
							//---
							double qtProduto = 0;
							if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() && !(LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && pedido.isPendente())) {
								Vector itemPedidoList = pedido.itemPedidoList;
								int size = itemPedidoList.size();
								for (int i = 0; i < size; i++) {
									ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
									qtProduto += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
								}
							} else {
								qtProduto = -1;
							}
							cbCondicaoPagamento.loadCondicoesPagamento(pedido.cdTabelaPreco, pedido.isPagamentoAVista(), qtProduto);
						}
						cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
						pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
						//--
						StringBuffer strBuffer = new StringBuffer();
						if (validaAoTrocarTabelaPreco) {
							strBuffer.append(MessageUtil.getMessage(Messages.CLIENTE_FINANCEIRO_BLOQUEADO_TROCA_TAB_PRECO, dsTabPreco));
						} else {
							strBuffer.append(MessageUtil.getMessage(Messages.CLIENTE_FINANCEIRO_BLOQUEADO_TROCA_COND_PAGTO, StringUtil.getStringValue(dsCondPagto)));
						}
						strBuffer.append(e.getMessage());
						throw new ValidationException(strBuffer.toString());
					} else if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
						if (!pedido.isFlCreditoClienteLiberadoSenha()) {
							AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
							senhaForm.setMensagem(e.getMessage());
							senhaForm.setCdCliente(SessionLavenderePda.getCliente().cdCliente);
							senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
							if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
								if (validaAoTrocarTabelaPreco) {
									cbTabelaPreco.setValue(ultimaTabelaPrecoSelected);
									pedido.cdTabelaPreco = ultimaTabelaPrecoSelected;
									//---
									double qtProduto = 0;
									if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto()) {
										Vector itemPedidoList = pedido.itemPedidoList;
										int size = itemPedidoList.size();
										for (int i = 0; i < size; i++) {
											ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
											qtProduto += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
										}
									}
									cbCondicaoPagamento.loadCondicoesPagamento(pedido.cdTabelaPreco, pedido.isPagamentoAVista(), qtProduto);
								}
								//--
								cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
								pedido.cdCondicaoPagamento = ultimaCondPgamentoSelected;
								return false;
							}
							pedido.flCreditoClienteLiberadoSenha = ValueUtil.VALOR_SIM;
						}
					} else if (LavenderePdaConfig.controlarLimiteCreditoCliente) {
						UiUtil.showWarnMessage(e.getMessage());
					}
				}
			}
		}
		return true;
	}

	private void reloadAndUpdateValoresDoPedido(Pedido pedido, boolean nonBlockingValidationWithException) throws SQLException {
 		if (getPedidoService().reloadValoresDosItensPedido(pedido, true, nonBlockingValidationWithException)) {
 			getPedidoService().updateItensPedidoAfterChanges(pedido);
			getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
			updateVlTotalPedido();
		}
	}

	private void cbSegmentoChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdSegmento = cbSegmento.getValue();
		//--
		if (LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			reloadComboTabelaPreco();
		}
		if (LavenderePdaConfig.usaCondicaoPagamentoPorSegmento) {
			reloadComboCondicaoPagamento();
		}
		if (LavenderePdaConfig.usaCondicaoComercialPorSegmentoECliente) {
			reloadComboCondicaoComercial();
		}
	}
	
	private void cbDivisaoVendaChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.cdDivisaoVenda = cbDivisaoVenda.getValue();
		if (DivisaoVendaService.getInstance().isIgnoraVerbaGrupoSaldoPorDivisaoVenda(pedido)) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				VerbaGrupoSaldoService.getInstance().estornaVerbaSaldo(pedido, itemPedido);
			}
		} else {
			reloadAndUpdateValoresDoPedido(pedido, false);
		}
		VerbaGrupoSaldoService.getInstance().recalculateAndUpdateVerbaGrupoSaldoPda();
	}

	private void cbCondicaoComercialChange() throws SQLException {
		UiUtil.showProcessingMessage();
		try {
			Pedido pedido = getPedido();
			String cdCondicaoComercialPedido = pedido.cdCondicaoComercial;
			pedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
			ultimaCondicaoComercialSelected = pedido.cdCondicaoComercial;
			// --
			if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial) {
				reloadComboCondicaoPagamento();
			}
			if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
				reloadComboTabelaPreco();
			}
			if (ValueUtil.isNotEmpty(pedido.cdCondicaoComercial) && !pedido.cdCondicaoComercial.equals(pedido.oldCdCondicaoComercial)) {
				pedido.cdCondicaoComercialChanged = true;
			}
			// --
			if (isEditing() && pedido.itemPedidoList.size() > 0) {
				if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial && ValueUtil.isEmpty(pedido.cdCondicaoPagamento)) {
					return;
				}
				if (!pedido.cdCondicaoComercial.equals(pedido.oldCdCondicaoComercial)) {
					pedido.cdCondicaoComercialChanged = true;
					Vector itemPedidoList = new Vector();
					if (PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(pedido) || LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
						int size = pedido.itemPedidoList.size();
						for (int i = 0; i < size; i++) {
							ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
							itemPedidoList.addElement(itemPedido.clone());
							itemPedido.descPromocional = new DescPromocional();
							itemPedido.retiraDescItem = true;
							itemPedido.vlVerbaItemPositivo = 0d;
							itemPedido.vlVerbaItemPositivoOld = 0d;
							if (!ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
								pedido.qtdCreditoDescontoGerado -= itemPedido.qtdCreditoDesc;
								itemPedido.qtdCreditoDesc = 0;
								itemPedido.flTipoCadastroItem = null;
								itemPedido.cdProdutoCreditoDesc = null;
							}
						}
					}
					try {
						reloadAndUpdateValoresDoPedido(pedido, false);
					} catch (Throwable e) {
						cbCondicaoComercial.setValue(cdCondicaoComercialPedido);
						pedido.cdCondicaoComercial = cdCondicaoComercialPedido;
						pedido.cdCondicaoComercialChanged = false;
						pedido.itemPedidoList = itemPedidoList;
						reloadAndUpdateValoresDoPedido(pedido, false);
						if (e instanceof CondicaoComercialVerbaException) {
							throw new ValidationException(Messages.VALIDACAO_VERBA_TROCA_CDCOMERCIAL);
						} else if (e instanceof ValidationValorMinPedidoException) {
							throw new ValidationException(Messages.VALIDACAO_ITEMPEDIDO_VL_MINIMO_PERMITIDO);
						} else {
							throw e;
						}
					}
				}
			}
			if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
				ProdutoCreditoDescService.getInstance().loadCreditosPedido(pedido);
			}
			pedido.cdCondicaoComercialChanged = false;
			pedido.oldCdCondicaoComercial = pedido.cdCondicaoComercial;
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void cbTipoPagamentoChange() throws SQLException {
		Pedido pedido = getPedido();
		String cdTipoPagamentoOld = pedido.cdTipoPagamento;
		pedido.cdTipoPagamento = cbTipoPagamento.getValue();

		try {
			validaDescontoPorCondicaoPagamento(pedido, edVlPctDescCondicao.getValueDouble());
		} catch (Throwable e) {
			pedido.cdTipoPagamento = cdTipoPagamentoOld;
			cbTipoPagamento.setValue(cdTipoPagamentoOld);
			throw e;
		}


		if (LavenderePdaConfig.bloqueiaTipoPagamentoNivelSuperior) {
			TipoPagamento tipoPagamento = pedido.getTipoPagamento();
			TipoPagamento tipoPagamentoCliente = pedido.getCliente().getTipoPagamento();
			if ((tipoPagamentoCliente != null) && (tipoPagamentoCliente.nuNivel < tipoPagamento.nuNivel)) {
				cbTipoPagamento.setValue(ultimoTipoPagamentoSelected);
				pedido.cdTipoPagamento = cbTipoPagamento.getValue();
				//--
				throw new ValidationException(Messages.TIPOPAGTO_MSG_NUNIVELSUPERIOR);
			}
		}
		if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
			double qtMinValor = pedido.getTipoPagamento().qtMinValor;
			if (qtMinValor > 0) {
				edValorMinTipoPagto.setValue(qtMinValor);
				tipVlMinTipoPagto.setText(Messages.VALOR_MINIMO_PEDIDO + " " + qtMinValor);
			} else {
				edValorMinTipoPagto.setText("");
				tipVlMinTipoPagto.setText(Messages.CONDICAOPAGAMENTO_SEM_VALOR_MIN);
			}
		}
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList) && !pedido.getTipoPagamento().isIgnoraLimiteCredito()) {
			pedido.setPagamentoAVista();
			try {
				FichaFinanceiraService.getInstance().validateLimCred(pedido, null);

			} catch (ValidationException ve) {
				if (LavenderePdaConfig.bloquearLimiteCreditoCliente) {
					StringBuffer strBuffer = new StringBuffer();
					strBuffer = strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage());
					if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
						strBuffer.append(" " + Messages.CLIENTE_FINANCEIRO_BLOQUEADO_PEDIDO_A_VISTA);
					}
					cbTipoPagamento.setValue(ultimoTipoPagamentoSelected);
					pedido.cdTipoPagamento = cbTipoPagamento.getValue();
					throw new ValidationException(strBuffer.toString());
				} else if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
					cbTipoPagamento.setValue(ultimoTipoPagamentoSelected);
					pedido.cdTipoPagamento = cbTipoPagamento.getValue();
					throw new LimiteCreditoClienteExtrapoladoPedidoException(ve.getMessage());

				}
			}
		}
		if (isEditing() && !ValueUtil.isEmpty(pedido.cdTipoPagamento) && !pedido.cdTipoPagamento.equals(ultimoTipoPagamentoSelected)) {
			if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial) {
				getPedidoService().calculate(pedido);
				getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
				PedidoPdbxDao.getInstance().update(pedido);
				updateVlTotalPedido();
			}
		}
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente()) {
			cbCondicaoPagamento.loadCondicoesPagamento(pedido);
			cbCondicaoPagamento.setSelectedIndex(0);
			edNuParcelaPedido.setValue(0);
			if (ValueUtil.isNotEmpty(cbCondicaoPagamento.getValue())) {
				reloadComboCondicaoPagamento();
			}
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && pedido.itemPedidoList.size() > 0 && !LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento()) {
			ItemPedidoService.getInstance().recalculaDescCascataCondicaoPagamentoChange(pedido);
			updateVlTotalPedido();
		}
		if (LavenderePdaConfig.mostraValorParcelaPedido) {
			updateVlTotalPedido();
		}
		ultimoTipoPagamentoSelected = pedido.cdTipoPagamento;
	}

	private void cbCondicaoNegociaoChange() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			cbCondicaoNegociacao.setValue(pedido.getCondicaoNegociacao());
			throw new ValidationException(Messages.CONDICAONEGOCIACAO_NAO_ALTERADO_COM_ITENS);
		}
		String oldCdCondicaoNegociacao = pedido.oldCdCondicaoNegociacao;
		String newCdCondicaoNegociacao = cbCondicaoNegociacao.getValue().cdCondicaoNegociacao;
		boolean houveMudancaCondicaoNegociacao = isEditing() && !ValueUtil.valueEquals(newCdCondicaoNegociacao, oldCdCondicaoNegociacao) && pedido.itemPedidoList.size() > 0;
		CondicaoNegociacao condicaoNegociacaoOld;
		CondicaoNegociacao condicaoNegociacaoNew;
		if (houveMudancaCondicaoNegociacao) {
			condicaoNegociacaoOld = CondicaoNegociacaoService.getInstance().findCondicaoNegociacao(oldCdCondicaoNegociacao);
			condicaoNegociacaoNew = CondicaoNegociacaoService.getInstance().findCondicaoNegociacao(newCdCondicaoNegociacao);
			try {
				CondicaoNegociacaoService.getInstance().validaTrocaCondicaoNegociacao(pedido, condicaoNegociacaoOld, condicaoNegociacaoNew);
			} catch (ValidationException ex) {
				cbCondicaoNegociacao.setValue(pedido.getCondicaoNegociacao());
				throw new ValidationException(Messages.CONDICAONEGOCIACAO_NAO_ALTERADO + ex.getMessage());
			} catch (Throwable e) {
				cbCondicaoNegociacao.setValue(pedido.getCondicaoNegociacao());
			}
		}
		pedido.setCondicaoNegociacao(cbCondicaoNegociacao.getValue());
	}

	private void cbTipoPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		pedido.isTipoPedidoChanged = true;
		if (LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaModoControleEstoquePorTipoPedido && pedido.itemPedidoList.size() > 0) {
			cbTipoPedido.setValue(pedido.cdTipoPedido);
			throw new ValidationException(Messages.TIPOPEDIDO_MSG_ITEM_JA_LANCADO);
		}
 		if (TipoPedidoService.getInstance().isTipoPedidoExigeSenha((TipoPedido) cbTipoPedido.getSelectedItem())) {
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(Messages.TIPOPEDIDO_MSG_FLEXIGESENHA);
			senhaForm.setCdCliente(pedido.cdCliente);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA);
			if (!senhaForm.show()) {
				if (!ValueUtil.isEmpty(pedido.cdTipoPedido)) {
					cbTipoPedido.setValue(pedido.cdTipoPedido);
				} else {
					cbTipoPedido.setSelectedIndex(BaseComboBox.DefaultItemNull);
					return;
				}
			}
			repaintNow();
		}
		boolean houveMudancaTipoPedido = isEditing() && !cbTipoPedido.getValue().equals(pedido.oldCdTipoPedido) && (pedido.itemPedidoList.size() > 0 || pedido.itemPedidoOportunidadeList.size() > 0);
    	TipoPedido tipoPedidoOld = null;
    	TipoPedido tipoPedidoNew = TipoPedidoService.getInstance().getTipoPedido(cbTipoPedido.getValue());
		if (houveMudancaTipoPedido) {
			tipoPedidoOld = TipoPedidoService.getInstance().getTipoPedido(pedido.oldCdTipoPedido);
			try {
				TipoPedidoService.getInstance().validateTrocaTipoPedido(pedido, tipoPedidoOld, tipoPedidoNew);
			} catch (ItensDivergentesSemEstoqueException ex) {
				cbTipoPedido.setValue(pedido.cdTipoPedido);
				if (tipoPedidoOld.isIgnoraControleEstoque() && !tipoPedidoNew.isIgnoraControleEstoque()) {
					RelItensDivergentesSemEstoqueWindow relItensDivergentesSemEstoqueWindow = new RelItensDivergentesSemEstoqueWindow(ex.getList(), this, this.getPedido(), Messages.REL_DIVERGENCIA_ESTOQUE);
					relItensDivergentesSemEstoqueWindow.popup();
					if (relItensDivergentesSemEstoqueWindow.chamarLista) {
						btListaItensClick();
					}
				}
			} catch (ValidationException ex) {
				cbTipoPedido.setValue(pedido.cdTipoPedido);
				if (ex instanceof ProdutoTipoPedidoException) {
					int result = UiUtil.showMessage(ex.getMessage(), TYPE_MESSAGE.ERROR, new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
					if (result == 1) {
						PedidoUiUtil.showListItensDivergentesTipoPedido(this, tipoPedidoNew);
					}
					return;
				}
				if (ex instanceof RestricaoVendaUnException) {
					int result = UiUtil.showMessage(ex.getMessage(), TYPE_MESSAGE.ERROR, new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
					if (result == 1) {
						PedidoUiUtil.showListItensDivergentesRestricaoVendaUn(this, tipoPedidoNew);
					}
					return;
				}
				throw new ValidationException(Messages.MSG_TIPO_PEDIDO_NAO_ALTERADA + ex.getMessage());
			} catch (Throwable e) {
				cbTipoPedido.setValue(pedido.cdTipoPedido);
			}
		}

		if (tipoPedidoNew != null) {
			if (!validaAlteracaoNotaCredito(pedido, tipoPedidoNew)) {
				cbTipoPedido.setValue(pedido.cdTipoPedido);
				return;
			}

			pedido.flModoEstoque = tipoPedidoNew.getFlModoEstoque();
		}
		pedido.cdTipoPedido = cbTipoPedido.getValue();

		controleEstoqueLoteAposTrocaTipoPedido(tipoPedidoOld, tipoPedidoNew, pedido);

		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) {
			setValueEdValorMinTipoPedido(pedido);
		}
		if (LavenderePdaConfig.isUsaPedidoBonificacao()) {
			if (ValueUtil.isNotEmpty(ultimoTipoPedidoSelected)) {
				TipoPedido tipoPedido = TipoPedidoService.getInstance().getTipoPedido(ultimoTipoPedidoSelected);
				if ((tipoPedido != null) && tipoPedido.isBonificacao()) {
					clearScreenCombosPrincipais();
				}
			}
			if (pedido.isPedidoBonificacao()) {
				if (!LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao) {
					cbCondicaoPagamento.setSelectedIndex(BaseComboBox.DefaultItemNull);
					cbCondicaoPagamentoChange();
				}
				if (LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
					String cdTabelapreco = cbTabelaPreco.getValue();
					cbTabelaPreco.loadTabelasPrecos(pedido);
					cbTabelaPreco.setValue(cdTabelapreco);
				} else {
					cbTabelaPreco.carregaTabelaPrecoBonificacao(pedido.getCliente());
					cbTabelaPreco.setSelectedIndex(0);
					validateTabelaBonificacao();
				}
			} else if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido) {
				reloadComboTabelaPreco();
			}
		}
		if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido) {
			reloadComboTabelaPreco();
		}
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido || LavenderePdaConfig.usaPermiteCondPagtoPadraoCliTipoPed()) {
			if (ValueUtil.VALOR_SIM.equals(pedido.getTipoPedido().flUtilizaCondPgtoPadraoCli)) {
				hasCondicaoPagamentoPadraoInCombo(pedido.getCliente().cdCondicaoPagamento);
			}
			if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento()) {
				loadComboTipoPagamento(pedido.getCliente());
			}
			reloadComboCondicaoPagamento();
		}
		if (LavenderePdaConfig.usaTransportadoraPedido() && TranspTipoPedService.getInstance().isPossuiTranspTipoPedido()) {
			reloadComboTransportadora();
		}
		if (!LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao && LavenderePdaConfig.isUsaPedidoBonificacao() && pedido.isPedidoBonificacao()) {
			cbCondicaoPagamento.setValue("");
			cbTipoPagamento.setValue("");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			if (pedido.isPedidoTroca()) {
				cbCondicaoPagamento.setSelectedIndex(BaseComboBox.DefaultItemNull);
				pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
				cbTipoPagamento.setSelectedIndex(BaseComboBox.DefaultItemNull);
				pedido.cdTipoPagamento = cbTipoPagamento.getValue();
				reloadComboTabelaPreco();
				cbCondicaoPagamentoChange();
			} else {
				clearScreenCombosPrincipais();
			}
		}
		//--
		remontaTela();
		reposition();
		setEnableCombosPrincipais();
		//--
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			boolean indicaCliEntrega = PedidoService.getInstance().obrigaIndicarClienteEntrega(pedido);
			reloadCliEnderecoComboOnPedidoChange(pedido, indicaCliEntrega);
			setEnabledPopupSearchCliEntrega();
			if (indicaCliEntrega && ValueUtil.isEmpty(pedido.cdClienteEntrega) && LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
				cbEnderecoEntrega.cleanDomainSelected();
			}
		}
		if (houveMudancaTipoPedido && tipoPedidoNew != null) {
			preparePedidoToUpdateAposTrocaTipoPedido(pedido, tipoPedidoOld, tipoPedidoNew);
			reloadAndUpdateValoresDoPedido(pedido, false);
			updateEntidadesAposTrocaTipoPedido(pedido, tipoPedidoOld, tipoPedidoNew);
		}
		limpaRelacionamentos();
		pedido.oldCdTipoPedido = pedido.cdTipoPedido;
		ultimoTipoPedidoSelected = cbTipoPedido.getValue();
		addItensOnButtonMenu();
		if (LavenderePdaConfig.isEnviarEmailPedidoAutoCliente() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) {
			habilitaDesabilitaCamposEnvioEmail();
			if (PedidoService.getInstance().isEnviaEmailByTipoPedido(pedido)) {
				loadFlEnviaEmailDefault(true);
			}
		}
		if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoCapaPedido()) {
			totalizadoresPedidoForm.clearComponentesFromScreen();
			totalizadoresPedidoForm.onFormStart();
		}
		bgAguardarPedidoComplementar.setValue(ValueUtil.VALOR_NAO);
		setValueEdVlMinTabelaPreco(pedido);
		pedido.isTipoPedidoChanged = false;
		visibleState();
	}

	private void controleEstoqueLoteAposTrocaTipoPedido(TipoPedido tipoPedidoOld, TipoPedido tipoPedidoNew, Pedido pedido) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList) || !LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) return;

		if (tipoPedidoOld.isIgnoraControleEstoque() && !tipoPedidoNew.isIgnoraControleEstoque()) {
			LoteProdutoService.getInstance().consomeEstoqueLote(pedido);
		}

		if (!tipoPedidoOld.isIgnoraControleEstoque() && tipoPedidoNew.isIgnoraControleEstoque()) {
			LoteProdutoService.getInstance().removeEstoqueConsumido(pedido);
		}


	}

	private boolean validaAlteracaoNotaCredito(Pedido pedido, TipoPedido tipoPedidoNew) throws SQLException {
		if (pedido.vlTotalNotaCredito == 0 ) return true;

		if (tipoPedidoNew.isVenda(cbTipoPedido.getValue())) return true;

		if (UiUtil.showConfirmYesNoMessage(Messages.NOTA_CREDITO_DIFERENTE_VENDA)) {
			PedidoService.getInstance().deletaNotasCredito(pedido);
			if (PedidoService.getInstance().countByExample(pedido) > 0) {
				PedidoService.getInstance().updateColumn(pedido.rowKey, Pedido.NMCOLUNA_VLTOTALNOTACREDITO, 0d, Types.DECIMAL);
			}
			pedido.notaCreditoPedidoList = new Vector(0);
			pedido.vlTotalNotaCredito = 0;
			lvVlTotalNotaCredito.setValue(0d);
			return true;
		}
		return false;
	}

	private void preparePedidoToUpdateAposTrocaTipoPedido(Pedido pedido, TipoPedido tipoPedidoOld, TipoPedido tipoPedidoNew) throws SQLException {
		boolean usaDescontoTipoPedido = !LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido();
		boolean usaAcrescimoTipoPedido = !LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido();
		if (LavenderePdaConfig.atualizarEstoqueInterno || usaDescontoTipoPedido || usaAcrescimoTipoPedido || LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (LavenderePdaConfig.atualizarEstoqueInterno) {
					itemPedido.setOldQtItemFisico(itemPedido.getQtItemFisico());
					if ((tipoPedidoOld == null || tipoPedidoOld.isIgnoraControleEstoque()) && !tipoPedidoNew.isIgnoraControleEstoque()) {
						itemPedido.setOldQtItemFisico(0);
					}
				}
				ajustaVlPctMaxDescAcrescItemPedido(tipoPedidoNew, usaDescontoTipoPedido, usaAcrescimoTipoPedido, itemPedido);
			}
		}
		if ((LavenderePdaConfig.isUsaTipoFretePedido() && !LavenderePdaConfig.isUsaTransportadoraAuxiliar() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) && !tipoPedidoNew.isIgnoraCalculoFrete()) {
			pedido.cdTipoFrete = cbTipoFrete.getValue();
		}
	}

	private void updateEntidadesAposTrocaTipoPedido(Pedido pedido, TipoPedido tipoPedidoOld, TipoPedido tipoPedidoNew) throws SQLException {
    	//Estoque
		if (LavenderePdaConfig.atualizarEstoqueInterno) {
			int size = pedido.itemPedidoList.size();
			int errosEstoqueValidation = 0;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (tipoPedidoOld != null && !tipoPedidoOld.isIgnoraControleEstoque()) {
					if(tipoPedidoNew.isIgnoraControleEstoque()) {
						String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && itemPedido.pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
						EstoqueService.getInstance().updateEstoqueInterno(itemPedido, itemPedido.getQtItemFisico(), false, flOrigemEstoque);
					}
					if(LavenderePdaConfig.usaLocalEstoquePorTipoPedido()) {
						EstoqueService.getInstance().recalculaEstoqueConsumido(itemPedido.cdProduto);
						try {
							EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
						} catch(ValidationException e) {
							errosEstoqueValidation++;
							if(errosEstoqueValidation==1) {
								UiUtil.showErrorMessage(e.getMessage());
							}
						}
					}
				}
			}
		}
		//Verba Saldo
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			int size = pedido.itemPedidoList.size();
			if (tipoPedidoNew.isSimulaControleVerba() && tipoPedidoOld != null && !tipoPedidoOld.isSimulaControleVerba() && !tipoPedidoOld.isIgnoraControleVerba()) {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					VerbaService.getInstance().deleteVlSaldo(pedido, itemPedido, true);
				}
			} else if (!tipoPedidoNew.isIgnoraControleVerba() && tipoPedidoOld != null && !tipoPedidoNew.isSimulaControleVerba() && tipoPedidoOld.isSimulaControleVerba()) {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
					VerbaService.getInstance().insertVlSaldo(pedido, itemPedido);
				}
			}
		}
    }

	private void reloadComboTransportadora() throws SQLException {
		Pedido pedido = getPedido();
		cbTransportadora.carregaTransportadoras(pedido, false);
		if (LavenderePdaConfig.usaWebserviceSankhyaComplementaPedido && ValueUtil.isNotEmpty(pedido.cdTransportadora) && pedido.preencheuCdTransportadoraSankhya) {
			cbTransportadora.setValue(pedido.cdTransportadora);
			ultimaTransportadoraSelected = pedido.cdTransportadora;
			pedido.preencheuCdTransportadoraSankhya = false;
		}
		if (cbTransportadora.size() > 0) {
			if (!ValueUtil.isEmpty(ultimaTransportadoraSelected)) {
				cbTransportadora.setValue(ultimaTransportadoraSelected);
				pedido.cdTransportadora = ultimaTransportadoraSelected;
			}
			if (ValueUtil.isEmpty(cbTransportadora.getValue())) {
				String cdTransportadoraCliente = pedido.getCliente().cdTransportadora;
				cbTransportadora.setValue(cdTransportadoraCliente);
				pedido.cdTransportadora = cdTransportadoraCliente;
			}
			if (ValueUtil.isEmpty(cbTransportadora.getValue())) {
				cbTransportadora.setSelectedIndex(BaseComboBox.DefaultItemNull);
				pedido.cdTransportadora = cbTransportadora.getValue();
			}
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar) {
			cbTransportadoraAux.carregaTransportadoras(pedido, false);
		}
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			cbTranspFretePersonalizado.carregaTransportadoras(pedido, false);
		}
		try {
			cbTransportadoraChange();
		} catch (Throwable e) {
			cbTransportadora.setValue(ultimaTransportadoraSelected);
			pedido.cdTransportadora = ultimaTransportadoraSelected;
		}
	}

	public void validateTabelaBonificacao() {
		if (!LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao && LavenderePdaConfig.isUsaTabelaPrecoPedido()) {
			if (cbTabelaPreco.size() > 1) {
				UiUtil.showWarnMessage(Messages.TABELAPRECO_BONIFICACAO_DUPLICADA);
			}
			if (cbTabelaPreco.size() == 0) {
				UiUtil.showWarnMessage(Messages.TABELAPRECO_BONIFICACAO_VAZIO);
			}
		}
	}

	private void cbTipoFreteClick() throws SQLException {
		UiUtil.showProcessingMessage();
		Pedido pedido = getPedido();
		try {
			TipoFreteService.getInstance().validateChangeTipoFrete(cbTipoFrete.getValue(), pedido.getCliente().cdEstadoComercial, pedido.itemPedidoList);
			pedido.cdTipoFrete = cbTipoFrete.getValue();
			setFreteVisible();
			TipoFrete tipoFrete = pedido.getTipoFrete();
			boolean usaInfoAdicional = tipoFrete.isUsaInfoAdicional();
			setEnabledFieldsInfoFrete(usaInfoAdicional);
			pedido.flCalculaSeguro = StringUtil.getStringValue(!(tipoFrete.isSugereSeguro() && !UiUtil.showConfirmYesNoMessage(Messages.MSG_SUGERE_APLICAR_SEGURO_PEDIDO)));
			if (ValueUtil.isNotEmpty(pedido.cdTipoFrete) && !pedido.cdTipoFrete.equals(pedido.oldCdTipoFrete)) {
				pedido.isTipoFreteChanged = true;
			}
			if (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos || LavenderePdaConfig.habilitaRegrasAdicionaisDescPromocional()) {
				reloadAndUpdateValoresDoPedido(pedido, false);
				pedido.isTipoFreteChanged = false;
			} else if (getPedidoService().recalculateFretePedido(pedido)) {
				getPedidoService().calculate(pedido);
				PedidoPdbxDao.getInstance().update(pedido);
				updateVlTotalPedido();
			}
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete) {
				lbVlMaxPctDescFrete.setValue(StringUtil.getStringValueToInterface(tipoFrete.vlPctMaxDesconto));
			}
			if (LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
				boolean tipoFreteFob = tipoFrete.isTipoFreteFob();
				edCnpjTransportadora.setEditable(tipoFreteFob);
				if (!tipoFreteFob) {
					edCnpjTransportadora.setValue(ValueUtil.VALOR_NI);
				}
			}
			pedido.flPendenteFob = ValueUtil.VALOR_NAO;
			if (LavenderePdaConfig.usaValidaConversaoFOB()) {
				addItensOnButtonMenu();
				if (tipoFrete.isTipoFreteFob() && bgPedidoGondola != null) {
					bgPedidoGondola.setValueBoolean(false);
					bgPedidoGondolaClick(false);
				}
			}
			if (LavenderePdaConfig.isExibeComboMenuInferior()) {
				addItensOnButtonMenu();
			}
			pedido.oldCdTipoFrete = pedido.cdTipoFrete;
			pedido.isTipoFreteChanged = false;
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void btPesquisaMercadoClick() throws SQLException {
		Pedido pedido = getPedido();
		saveAndEditPedido();
		ListPesquisaMercadoForm listPesquisaForm = new ListPesquisaMercadoForm(pedido.nuPedido);
		show(listPesquisaForm);
	}

	private void btPesquisaMercadoProdutoConcorrenteClick() throws SQLException {
		Pedido pedido = getPedido();
		saveAndEditPedido();
		ListPesquisaMercadoConfigForm listPesquisaMercadoConfigForm = new ListPesquisaMercadoConfigForm(pedido.nuPedido);
		show(listPesquisaMercadoConfigForm);
	}

	private void btVerbaClick() throws SQLException {
		Pedido pedido = getPedido();
		RelVerbaPedidoWindow verbaPedidoForm = new RelVerbaPedidoWindow(pedido);
		verbaPedidoForm.popup();
		lbVlTotalPedido.setValue(pedido.vlTotalPedido);
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			lvVlPedido.setValue(pedido.vlFinalPedidoDescTribFrete);
			atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlFinalPedidoDescTribFrete);
		} else {
			lvVlPedido.setValue(pedido.vlTotalPedido);
			atualizaLabelCasoValorMenorZero(lvVlPedido, pedido.vlTotalPedido);
		}
		atualizaVlPedidoAberto();
	}

	public void btTrocaClick() throws SQLException {
		Pedido pedido = getPedido();
		if (!isEditing()) {
			pedido.updateByClickNovoItemInPedido = true;
			save();
			//--
			edit(pedido);
			pedido.updateByClickNovoItemInPedido = false;
			//--
		}
		//--
		RelTrocaGerenciaWindow trocaWindow = new RelTrocaGerenciaWindow(this, pedido);
		trocaWindow.popup();
	}

	//@Override
	protected void delete(BaseDomain domain) throws java.sql.SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		Pedido pedido = (Pedido) domain;
		try {
			pedido.flUIChange = true;
			pedido.flSituacaoReservaEstReabrePedido = pedido.flSituacaoReservaEst;
			if ((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3())
					&& pedido.isPedidoComReservaEstoque()
					&& !pedido.isIgnoraControleEstoque()) {
				PedidoService.getInstance().inativaReservaEstoque(pedido);
			}
			if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
				PagamentoPedidoService.getInstance().deleteByPedido(pedido);
			}
			PedidoService.getInstance().deletaNotasCredito(pedido);
			controlHrFimEmissao = false;
			pedido.deletadoPelaIntefacePedido = true;
			PedidoService.getInstance().updatePedidoDiferenca(getPedido(), ValueUtil.VALOR_NAO);
			super.delete(domain);
			if (LavenderePdaConfig.usaVisitaFoto) {
				VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(getPedido().getVisita());
			}
			if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
				VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
			}
		} finally {
			pedido.flUIChange = false;
			msg.unpop();
		}
		if (LavenderePdaConfig.usaPesquisaMercado && LavenderePdaConfig.excluiPesquisaMercadoPedido && PesquisaMercadoService.getInstance().isPedidoComPesqMercado(pedido)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_DELETE_PESQUISAMERCADO_PEDIDO)) {
				PesquisaMercadoService.getInstance().deletePesquisaMercadoByPedido(pedido);
			} else {
				PesquisaMercadoService.getInstance().clearPedidoFromPesquisaMercado(pedido);
			}
		}
		if (LavenderePdaConfig.excluiPesquisaPedidoPesquisaMercado() && (LavenderePdaConfig.usaPesquisaPedidoPesquisaMercado() || LavenderePdaConfig.usaPesquisaItemPedidoPesquisaMercado()) && PesquisaMercadoRegService.getInstance().pedidoHasPesquisaMercadoRelacionada(pedido)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PESQUISA_MERCADO_PROD_CONC_EXCLUIR_PEDIDO)) {
				PesquisaMercadoRegService.getInstance().deletePesquisaMercadoRegByPedido(pedido);
			} else {
				PesquisaMercadoRegService.getInstance().clearNuPedidoFromPesquisaMercadoReg(pedido);
			}
		}

	}

	public void close() throws SQLException {
		if (createPedidoOportunidade()) {
			return;
		}
		SessionLavenderePda.descontoVendaSimulado = null;
		super.close();
	}

	private void liberaPrecoVenda() throws SQLException {
		Pedido pedido = getPedido();
		AdmSenhaDinamicaWindow senhaDinamicaForm = new AdmSenhaDinamicaWindow();
		senhaDinamicaForm.setMensagem(Messages.PEDIDO_MSG_LIBERA_PRECO);
		senhaDinamicaForm.setCdCliente(pedido.cdCliente);
		senhaDinamicaForm.setChaveSemente(SenhaDinamica.SENHA_PRECO_VENDA);
		if (senhaDinamicaForm.show()) {
			UiUtil.showSucessMessage(Messages.PEDIDO_MSG_LIBERA_PRECO_SUCESSO);
			pedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
			bmOpcoes.removeItem(BOTAO_DESBLOQUEAR_PRECO);
			bmOpcoes.removeItem(BOTAO_PREVISAO_DESCONTOS);
			setPrecoLiberadoSenhaVisible();
		}
		repaintNow();
	}

	private void addItensOnButtonMenu() throws SQLException {
		bmOpcoes.removeAll();
		BOTAO_CALCULADORA = Messages.MENU_UTILITARIO_CALCULADORA;
		bmOpcoes.addItem(BOTAO_CALCULADORA);
		if (isEnabled() && !LavenderePdaConfig.isOcultaAcessosRelUltimosPedidosMenuInferior()) {
			BOTAO_ULTIMOS_PEDIDOS = Messages.CLIENTE_ULTIMOS_PEDIDOS;
			bmOpcoes.addItem(BOTAO_ULTIMOS_PEDIDOS);
		}
		if (LavenderePdaConfig.usaHistoricoAtendimentoUnificado) {
			bmOpcoes.addItem(Messages.ATENDIMENTOHIST_TITLE);
		}
		if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
			BOTAO_PRODUTOS_RETIRADA = Messages.PRODUTORETIRADA_NOME_BT;
			bmOpcoes.addItem(BOTAO_PRODUTOS_RETIRADA);
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
			BOTAO_RENTABILIDADE = Messages.RENTABILIDADE_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_RENTABILIDADE);
		}
		Pedido pedido = getPedido();
		if (ValueUtil.VALOR_SIM.equals(pedido.flPossuiDiferenca)) {
			BOTAO_DIFERENCAS = Messages.BOTAO_DIFERENCAS;
			bmOpcoes.addItem(BOTAO_DIFERENCAS);
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && !pedido.isFlPrecoLiberadoSenha() && isEnabled()) {
			BOTAO_DESBLOQUEAR_PRECO = Messages.BOTAO_DESBLOQUEAR_PRECO;
			bmOpcoes.addItem(BOTAO_DESBLOQUEAR_PRECO);
		}
		if (LavenderePdaConfig.usaHistoricoTabelaPrecoUsadasPorCliente && isEnabled()) {
			BOTAO_HISTORICO_TAB = Messages.BOTAO_HISTORICO_TAB;
			bmOpcoes.addItem(BOTAO_HISTORICO_TAB);
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			BOTAO_SALDO_BONI = Messages.SALDOBONIFICACAO_LABEL;
			bmOpcoes.addItem(BOTAO_SALDO_BONI);
		}
		if ((LavenderePdaConfig.isAplicaDescontoFimDoPedido()
				|| LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) && isEditing()
				&& isEnabled()) {
			BOTAO_PREVISAO_DESCONTOS = Messages.BOTAO_PREVISAO_DESCONTOS;
			bmOpcoes.addItem(BOTAO_PREVISAO_DESCONTOS);
		}
		if (LavenderePdaConfig.usaCCClientePorTipoPedido) {
			BOTAO_CCC = Messages.CCCLIPORTIPO_MENU_PREVISAO_PEDIDO;
			BOTAO_LIST_CCC = Messages.CCCLIPORTIPO_MENU_EXTRATO_CC;
			bmOpcoes.addItem(BOTAO_CCC);
			bmOpcoes.addItem(BOTAO_LIST_CCC);
		}
		if (!LavenderePdaConfig.isOcultaFichaFinanceira()) {
			BOTAO_FICHA_FINAN = Messages.FICHAFINANCEIRA_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_FICHA_FINAN);
		}
		if (LavenderePdaConfig.mostraRelatorioContaCorrenteCliente) {
			BOTAO_CONTACORRENTECLI = Messages.CONTACORRENTECLI_LABEL_EXTRATO;
			bmOpcoes.addItem(BOTAO_CONTACORRENTECLI);
		}
		if (LavenderePdaConfig.usaFotoPedidoNoSistema) {
			BOTAO_FOTOPEDIDO = Messages.LABEL_FOTO_PEDIDO;
			bmOpcoes.addItem(BOTAO_FOTOPEDIDO);
		}
		if (LavenderePdaConfig.usaPesquisaMercado && isEnabled()) {
			BOTAO_PESQUISA_MERCADO = Messages.PESQUISAMERCADO_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_PESQUISA_MERCADO);
		}
		if (LavenderePdaConfig.usaPesquisaPedidoPesquisaMercado() && isEnabled()) {
			BOTAO_PESQUISA_MERCADO_PRODUTO_CONCORRENTE = Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_PESQUISA_MERCADO_PRODUTO_CONCORRENTE);
		}
		if (LavenderePdaConfig.usaModuloTrocaNoPedido) {
			BOTAO_TROCA = Messages.TROCA_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_TROCA);
		}
		if ((LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) && isEnabled()
				&& !pedido.isPedidoBonificacao() && !LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			BOTAO_DESCONTO = Messages.PEDIDO_LABEL_DESCONTO_VALOR;
			bmOpcoes.addItem(BOTAO_DESCONTO);
		}
		if (pedido.isStatusPedidoNaoOcultaValoresComissao() && (LavenderePdaConfig.usaConfigCalculoComissao()
				|| LavenderePdaConfig.usaDescontoComissaoPorGrupo
				|| LavenderePdaConfig.usaDescontoComissaoPorProduto)
				&& LavenderePdaConfig.listaStatusPedidoRelComissao(pedido)) {
			BOTAO_RELCOMISSAOPEDIDO = Messages.RELCOMISSAOPEDIDO_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_RELCOMISSAOPEDIDO);
		}
		if (LavenderePdaConfig.usaRelSubstituicaoTributaria && LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			MENU_SUBSTITUICAO_TRIBUTARIA = Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA;
			bmOpcoes.addItem(MENU_SUBSTITUICAO_TRIBUTARIA);
		}
		if (LavenderePdaConfig.liberaSenhaQuantidadeMaximaVendaProduto) {
			MENU_DESBLOQUEAR_LIMITADOR = Messages.MENU_OPCAO_DESBLOQUEAR_LIMITADOR;
			bmOpcoes.addItem(MENU_DESBLOQUEAR_LIMITADOR);
		}
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && !LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco) {
			MENU_INFO_TRIBUTARIA_DETALHADA = Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA;
			bmOpcoes.addItem(MENU_INFO_TRIBUTARIA_DETALHADA);
		}
		if (LavenderePdaConfig.usaVisitaFoto && pedido.isPedidoAberto()) {
			BOTAO_VISITA_FOTO = Messages.VISITA_FOTO_LABEL;
			bmOpcoes.addItem(BOTAO_VISITA_FOTO);
		}
		if (LavenderePdaConfig.calculaPesoGrupoProdutoNoPedido && LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			BOTAO_PESO_GRUPO_PRODUTO = Messages.BOTAO_PESO_GRUPO_PRODUTO;
			bmOpcoes.addItem(BOTAO_PESO_GRUPO_PRODUTO);
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido() && (!pedido.isPedidoAberto() && !pedido.isUsaBloqueioReplicacaoPedido()
				|| pedido.isConverteTipoPedidoReplicacao())) {
			if(pedido.isConverteTipoPedidoReplicacao()) {
				BOTAO_REPLICAR_PEDIDO = Messages.BOTAO_REPLICAR_PEDIDO_OUTRO_TIPO_PEDIDO;
			} else {
				BOTAO_REPLICAR_PEDIDO = Messages.BOTAO_REPLICAR_PEDIDO;
			}
			bmOpcoes.addItem(BOTAO_REPLICAR_PEDIDO);
		}
		if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0 && !pedido.isPedidoBonificacao()) {
			BOTAO_SUGESTAO_ITENS_RENTABILIDADE_IDEAL = Messages.PEDIDO_TITULO_SUGESTAO_RENTABILIDADE_IDEAL;
			bmOpcoes.addItem(BOTAO_SUGESTAO_ITENS_RENTABILIDADE_IDEAL);
		}
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && pedido.isFlOrigemPedidoPda()) {
			Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
			if (visitaEmAndamento == null) {
				BOTAO_REGISTRAR_CHEGADA = Messages.BOTAO_REGISTRAR_CHEGADA;
				bmOpcoes.addItem(BOTAO_REGISTRAR_CHEGADA);
			} else if (ValueUtil.valueEquals(visitaEmAndamento.cdCliente, pedido.getCliente().cdCliente)) {
				BOTAO_REGISTRAR_SAIDA = Messages.BOTAO_REGISTRAR_SAIDA;
				bmOpcoes.addItem(BOTAO_REGISTRAR_SAIDA);
			}
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao() && !(pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraPoliticaBonificacao())) {
			bmOpcoes.addItem(Messages.TITULO_POLITICAS_BONIFICACAO);
		}
		if (pedido.isPedidoFechado() || pedido.isPedidoCancelado()  || pedido.isPedidoTransmitido() || pedido.isFlOrigemPedidoErp()) {
			if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
				BOTAO_IMPRIMIR_BOLETO = Messages.BOTAO_IMPRESSAO_BOLETO;
				bmOpcoes.addItem(BOTAO_IMPRIMIR_BOLETO);
			}

			if (LavenderePdaConfig.isGeraPdfPedidoBoleto() && (pedido.isPedidoFechado() || pedido.isPedidoCancelado()  || pedido.isPedidoTransmitido() || pedido.isFlOrigemPedidoErp())) {
				bmOpcoes.addItem( Messages.BOTAO_IMPRESSAO_BOLETO_PDF);
			}

			if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() ) {
				TipoPedido tipoPedido = pedido != null ? pedido.getTipoPedido() : null;
				if (tipoPedido != null && tipoPedido.isGeraNfe()) {
				bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_NFE);
			}
			}
			if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
				TipoPedido tipoPedido = pedido != null ? pedido.getTipoPedido() : null;
				if (tipoPedido != null && tipoPedido.isGeraNfce()) {
					bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_NFCE);
				}
			}
			if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
				Nfe nfe = NfeService.getInstance().getNfe(getPedido().cdEmpresa, getPedido().cdRepresentante, getPedido().nuPedido, getPedido().flOrigemPedido);
				if (nfe == null || nfe.cdEmpresa == null) {
					bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_NFE_CONTINGENCIA);
				}
			}
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0) {
				BOTAO_IMPRIMIR = Messages.BOTAO_IMPRESSAO_PEDIDO;
				bmOpcoes.addItem(BOTAO_IMPRIMIR);
			}
		}
		if (Session.isModoSuporte) {
			BOTAO_DETALHES_CALCULOS = Messages.MENU_OPCAO_DETALHES_CALCULOS;
			bmOpcoes.addItem(BOTAO_DETALHES_CALCULOS);
			if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
				bmOpcoes.addItem(Messages.MENU_OPCAO_DETALHES_VARIAVEIS_DE_CALCULO);
			}
		}
		if ((LavenderePdaConfig.isUsaGeracaoPdfOffline() && isEditing()) 
				|| (LavenderePdaConfig.isUsaGeracaoPdfOnline() && (pedido.getCliente() != null && !pedido.getCliente().isClienteDefaultParaNovoPedido() && (!pedido.getCliente().isNovoCliente() || LavenderePdaConfig.isPermitePedidoNovoCliente())))) {
			BOTAO_GERAR_PDF = Messages.MENU_OPCAO_GERAR_PDF;
			bmOpcoes.addItem(BOTAO_GERAR_PDF);
			if (LavenderePdaConfig.isGeraRelPedidoItensViaClientePdf() && LavenderePdaConfig.isUsaGeracaoPdfOnline()) {
				BOTAO_GERAR_PDF_VIA_CLIENTE = Messages.MENU_OPCAO_GERAR_PDF_VIA_CLIENTE;
				bmOpcoes.addItem(BOTAO_GERAR_PDF_VIA_CLIENTE);
			}
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido
				&& LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
			BOTAO_CONFIGURACAO_DESCONTOS = Messages.USUARIODESC_NOME_ENTIDADE;
			bmOpcoes.addItem(BOTAO_CONFIGURACAO_DESCONTOS);
		}
		if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || PedidoService.getInstance().isPossuiPedidoRelacionadoNaoObrigatorio(pedido))
				&& (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() 
				|| LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao()) && !pedido.isPedidoBonificacao()
				&& PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedido)
				&& !inConsultaVendaRelacionada) {
			BOTAO_BONIFICACAO_RELACIONADA = Messages.BOTAO_BONIFICACAO_RELACIONADA;
			bmOpcoes.addItem(BOTAO_BONIFICACAO_RELACIONADA);
		}
		if ((LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || PedidoService.getInstance().isPossuiPedidoRelacionadoNaoObrigatorio(pedido))
				&& (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() 
				|| LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao()) && pedido.isPedidoBonificacao()
				&& PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedido)
				&& !inConsultaVendaRelacionada) {
			BOTAO_VENDA_RELACIONADA = Messages.BOTAO_VENDA_RELACIONADA;
			bmOpcoes.addItem(BOTAO_VENDA_RELACIONADA);
		}
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && (pedido.statusOrcamento != null && pedido.statusOrcamento.isStatusPreOrcamento())) {
			BOTAO_CONVERTER_ORCAMENTO_PARA_PEDIDO = Messages.CONVERTER_ORCAMENTO_PARA_PEDIDO;
			bmOpcoes.addItem(BOTAO_CONVERTER_ORCAMENTO_PARA_PEDIDO);
		}
		if (isUsaCancelamentoNfePedido(getPedido()) || isUsaEnvioPedidoPendenteParaAutorizacaoEquipamento(getPedido()) || isStatusOrcamentoAtualPreOrcamento(getPedido())) {
			BOTAO_CANCELAR_PEDIDO = Messages.BOTAO_CANCELAR_PEDIDO;
			bmOpcoes.addItem(BOTAO_CANCELAR_PEDIDO);
		}
		if (LavenderePdaConfig.usaRelDescontosAplicadosNoItemPedidoPorFuncionalidade) {
			BOTAO_REL_DESCONTOS = Messages.BOTAO_REL_DESCONTOS;
			bmOpcoes.addItem(BOTAO_REL_DESCONTOS);
		}
		if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
			BOTAO_REL_LIBERACOES_SENHA = Messages.REL_LIBERACOES_SENHA_TITULO;
			bmOpcoes.addItem(BOTAO_REL_LIBERACOES_SENHA);
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			BOTAO_COTAS_COND_PAGTO = Messages.BOTAO_COTAS_COND_PAGTO;
			bmOpcoes.addItem(BOTAO_COTAS_COND_PAGTO);
		}
		if (LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogo() && isEditing()) {
			BOTAO_PRODUTO_DESEJADO = Messages.PRODUTODESEJADO_LABEL_BOTAO;
			bmOpcoes.addItem(BOTAO_PRODUTO_DESEJADO);
		}
		if (LavenderePdaConfig.usaSelecaoDocAnexoPedido() && (pedido.isPedidoAberto() || pedido.isPedidoFechado())) {
			BOTAO_ANEXAR_DOC = Messages.BOTAO_ANEXAR_DOC;
			bmOpcoes.addItem(BOTAO_ANEXAR_DOC);
		}
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			BOTAO_FAIXAS_DESC_PESO = Messages.BT_DESCONTO_FAIXA_PESO;
			bmOpcoes.addItem(BOTAO_FAIXAS_DESC_PESO);
		}
		if (pedido.isPedidoPermiteConsignacao() && isEnabled() && isEditing()) {
			bmOpcoes.addItem(FrameworkMessages.BOTAO_EXCLUIR);
		}
		if (LavenderePdaConfig.usaWorkflowStatusPedido && pedido.isPedidoConsignado() && !pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.BT_WORKFLOW);
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()
				&& LavenderePdaConfig.liberaComSenhaCancelamentoConsignacao && pedido.isPedidoConsignado()) {
			bmOpcoes.addItem(Messages.BOTAO_CANCELAR_CONSIGNACAO);
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4 && pedido.isPedidoConsignado()) {
			bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_CONSIGNACAO);
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() && pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.BOTAO_DESCONTO_VENDAS_MES);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.PRODUTOCREDITODESCONTO_TITULO);
		}
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth() && (getPedido().isPedidoTransmitido() || getPedido().isFlOrigemPedidoErp())) {
			if (getPedido().isNfceImpressa()) {
				bmOpcoes.addItem(Messages.BOTAO_REIMPRESSAO_NFCE);
			} else {
				bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_NFCE);
			}
		}
		if (LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido && getPedido().isPedidoAberto() && !getPedido().isPendente()) {
			bmOpcoes.addItem(Messages.BOTAO_IGNORAR_VALIDACOES);
		}
		if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0 && !pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.BOTAO_IMPRIMIR_PROMISSORIA);
		}
		if (LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco) {
			bmOpcoes.addItem(Messages.BOTAO_HISTLISTATABPRECO);
		}
		if (LavenderePdaConfig.usaRelatorioMetaVendaCliente) {
			bmOpcoes.addItem(Messages.BOTAO_METACLIENTE);
		}
		if (LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && LavenderePdaConfig.isUsaGiroProduto() && pedido.isPedidoAberto() &&  !inItemNegotiationGiroProdutoPendente && !LavenderePdaConfig.usaBotaoGiroProdutoItemPedido) {
			bmOpcoes.addItem(Messages.GIROPRODUTO_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem() && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			bmOpcoes.addItem(Messages.BOTAO_COMISSAO_PEDIDO_REP_WINDOW);
		}
		if (isAdicionaBtVerba(pedido)) {
			bmOpcoes.addItem(Messages.VERBA_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			bmOpcoes.addItem(Messages.MARGEMCONTRIBUICAO_TITLE_WINDOW);
		}
		if (LavenderePdaConfig.mostraObservacaoCliente() && pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.OBSERVACAO_CLIENTE);
		}
		if (LavenderePdaConfig.permiteFotoClientePedido) {
			bmOpcoes.addItem(Messages.CLIENTE_LABEL_SLIDEFOTOS);
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior() && pedido.isPedidoAberto() && !pedido.isPedidoCritico() && !pedido.isTipoFreteFob() && !pedido.isPedidoBonificacao()) {
			bmOpcoes.addItem(Messages.SUGESTAO_COMBO_PRODUTO);
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia() && isPedidoPossuiItemPendente() || isPedidoPendenteByBonificaoVendaRelacionada()) {
			bmOpcoes.addItem(Messages.REL_ITENS_PENDENTES_TITULO);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			bmOpcoes.addItem(Messages.SOL_AUTORIZACAO_PEDIDO);
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido() && pedido.isUsaCodigoInternoCliente() && !pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.CAD_PROD_CLI_COD_MENU);
		}
		if (LavenderePdaConfig.isUsaKitTipo3() && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca() && pedido.isPedidoAbertoEditavel() ) {
			BOTAO_ADICIONAR_KIT = Messages.BOTAO_KIT;
			bmOpcoes.addItem(BOTAO_ADICIONAR_KIT);
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto()) {
			bmOpcoes.addItem(Messages.DESC_PROG_PEDIDO_BUTTON);
		}
		if (LavenderePdaConfig.usaConversaoTipoPedido() && pedido.isPedidoFechado() && pedido.getTipoPedido() != null && pedido.getTipoPedido().ignoraEnvioErp()) {
			bmOpcoes.addItem(Messages.MENU_CAD_PEDIDO_CONVERTER_TIPO_PEDIDO);
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizadoCapaPedido() && pedido.isPedidoAberto() && !pedido.isIgnoraControleEstoque()) {
			bmOpcoes.addItem(Messages.BOTAO_RESERVAR_PRODUTO_ESTOQUE);
		}
		if (LavenderePdaConfig.isGeraCatalogoItensPedidoOnline()) {
			bmOpcoes.addItem(Messages.MENU_OPCAO_GERAR_PDF_CATALOGO_ITENS);
		}
		if (ValueUtil.isNotEmpty(pedido.nuPedido) && MainMenu.isTelaAutorizada(MainMenu.CDTELA_REQUISICAO_SERV)) {
			bmOpcoes.addItem(Messages.REQUISICAO_SERV_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && LavenderePdaConfig.isUsaRelCatalogoCapaPedido()) {
			bmOpcoes.addItem(Messages.GERAR_CATALOGO);
		}
		if (LavenderePdaConfig.isUsaArquivoCatalogoExternoCapaPedido()) {
			bmOpcoes.addItem(Messages.CATALOGO_EXTERNO);
		}
	}

	private boolean isAdicionaBtVerba(Pedido pedido) throws SQLException {
		return !pedido.isPedidoCriticoOuConversaoFob() && !LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() && (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco && pedido.isPedidoAberto()
				|| VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir && pedido.isPedidoBonificacao());
	}

	private boolean isPedidoPossuiItemPendente() throws SQLException {
		Vector listItensPendentesPedido = ItemPedidoService.getInstance().findProdutosPendentesByItemPedido(new ItemPedido(getPedido()));
		return ValueUtil.isNotEmpty(listItensPendentesPedido);
	}
	
	private boolean isPedidoPendenteByBonificaoVendaRelacionada() throws SQLException {
		return (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) && getPedido().isPendente() && ValueUtil.isNotEmpty(getPedido().nuPedidoRelBonificacao);
	}

	private boolean isUsaCancelamentoNfePedido(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.isUsaCancelamentoNfePedido() && !pedido.isPedidoAberto() && !pedido.isPedidoCancelado();
	}

	private boolean isUsaEnvioPedidoPendenteParaAutorizacaoEquipamento(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento
				&& LavenderePdaConfig.usaCancelamentoDePedido
				&& pedido.permiteLiberacaoPedidoBonificado()
				&& (pedido.isPedidoPendente() && pedido.isPendente())
				&& isBotaoLiberarPedidoVisivel();
	}



	protected void btDesbloquearLimitadorClick() throws SQLException {
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(Messages.PEDIDO_MSG_DESBLOQUEIA_LIMITADOR);
		Pedido pedido = getPedido();
		senhaForm.setCdCliente(pedido.cdCliente);
		senhaForm.setChaveSemente(SenhaDinamica.SENHA_DESBLOQUEAR_LIMITADOR);
		if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			pedido.flMaxVendaLiberadoSenha = ValueUtil.VALOR_SIM;

		}
	}

	private boolean deletePedidoWhenNotItem() throws SQLException {
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			return false;
		}
		if (LavenderePdaConfig.usaPedidoPerdido && ValueUtil.isEmpty(getPedido().itemPedidoList)) {
			return false;
		}
		boolean result = UiUtil.showConfirmYesNoDeleteMessage(Messages.PEDIDO_MSG_SEM_ITEM_NO_PEDIDO_EXCLUIR);
		if (!result) {
			return false;
		}
		controlHrFimEmissao = false;
		delete(screenToDomain());
		return true;
	}

	public void fecharEnviarPedido(boolean isFromItemPedido) throws SQLException {
		fecharEnviarPedido(isFromItemPedido, false, true);
	}

	public void fecharEnviarPedido(boolean isFromItemPedido, boolean isFromBtFecharClick, boolean validateExtras) throws SQLException {
		fecharEnviarPedido(isFromItemPedido, isFromBtFecharClick, validateExtras, false);
	}

	public void fecharEnviarPedido(boolean isFromItemPedido, boolean isFromBtFecharClick, boolean validateExtras, boolean validateFechamentoPedido) throws SQLException {
		Pedido pedido = getPedido();
		if (!validaFechamentoPedido(pedido, validateFechamentoPedido)) return;

		save();
		if (!validaEntregaPedido(pedido, isFromBtFecharClick)) return;

		try {
			Vector pedidosList = new Vector();
			pedidosList.addElement(pedido);
			if (ValueUtil.isEmpty(PedidoService.getInstance().fecharPedidos(pedidosList, true, validateExtras, false))) {
				updateAndMostraMsgCota();
				afterFecharPedido();
				if (!LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
					NotificacaoPdaService.getInstance().restauraNotificacaoPda();
				}
				updateHrFimEmissao();
				updateCurrentRecordInList(pedido);
				//--
				boolean isPossuiConexao = PedidoUiUtil.isPossuiConexao();
				NfceService.getInstance().geraNfce(pedido, isPossuiConexao);
				NfeService.getInstance().geraNfe(pedido, isPossuiConexao);
				if (pedido.isPedidoConsignado()) {
					PedidoService.getInstance().consignaPedidoFecharPedido(pedido);
				}
				enviaPedido(isFromBtFecharClick, isFromItemPedido, isPossuiConexao, pedido.isPedidoConsignado());
			} else {
				cancelarClick();
			}
			atualizaFaixaBoletoByPedidoBoletos(pedido);
			if(LavenderePdaConfig.usaValidaPosicaoVincoLargura()) {
				PedidoService.getInstance().validaVincoCalculosPedido(pedido);
			}
		} catch (NfeException e) {
			nfeException(pedido, e);
		} catch (BoletoEmContingenciaNaoGeradoException e) {
			boletoEmContingenciaNaoGeradoException(pedido, e);
		} catch (NfceException e) {
			ExceptionUtil.handle(e);
			UiUtil.showErrorMessage(Messages.ERRO_GERACAO_NFCE_REABERTURA_PEDIDO);
			PedidoService.getInstance().reabrirPedido(pedido);
		} catch (DescItemMaiorDescProgressivoException e) {
			int result = UiUtil.showMessage(e.getMessage(), TYPE_MESSAGE.ERROR, new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
			if (result == 1) {
				PedidoUiUtil.showListItensDivergentesDescProgQtd(this);
			}
		} catch (RecalculoPedidoException re) {
			UiUtil.showWarnMessage(Messages.NECESSARIO_RECEBER_ATUALIZACAO_DADOS_FECHAMENTO_PEDIDO);
		} catch (RelProdutosRentabilidadeSemAlcadaException ex) {
			relProdutosRentabilidadeSemAlcadaException(pedido, ex);
		} catch (ValidationException e) {
			validationException(e);
		} finally {
			if (LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ || LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido()) {
				PedidoService.getInstance().atualizaStatusNfeFinalizado(pedido);
			}
		}
	}

	private void validationException(ValidationException e) throws SQLException {
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo && e.getMessage().startsWith(DescComiFaixa.SIGLE_EXCEPTION)) {
			int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { FrameworkMessages.BOTAO_OK, Messages.BOTAO_VER_ITENS });
			if (result == 1) {
				PedidoUiUtil.showListItensDivergentesDescComissaoGrupo(this);
			}
		} else if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && e.getMessage().startsWith(DescontoGrupo.SIGLE_EXCEPTION)) {
			int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
			if (result == 0) {
				PedidoUiUtil.showListItensDivergentesDescQtdGrupo(this);
			}
		} else if (LavenderePdaConfig.usaDescQuantidadePorPacote && e.getMessage().startsWith(DescontoPacote.SIGLE_EXCEPTION)) {
			int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
			if (result == 0) {
				PedidoUiUtil.showListItensDivergentesDescPacote(this);
			}
		} else if (LavenderePdaConfig.usaValidaPosicaoVincoLargura() && e.getMessage().startsWith("PVL")) {
			int result = UiUtil.showMessage(e.getMessage().substring(3), TYPE_MESSAGE.ERROR, new String[] { Messages.BOTAO_VER_ITENS, FrameworkMessages.BOTAO_CANCELAR });
			if (result == 0) {
				PedidoUiUtil.showListItensDivergentesCalculoVinco(this);
			}
		} else if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && e.getMessage().startsWith(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL.substring(0, 20))) {
			UiUtil.showErrorMessage(e.getMessage());
			previsaoDescontoClick();
		} else if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()	&& e.getMessage().equals(ListTransportadoraRegException.EXCEPTION)) {
			ExceptionUtil.handle(e);
		} else {
			throw e;
		}
	}

	private void boletoEmContingenciaNaoGeradoException(Pedido pedido, BoletoEmContingenciaNaoGeradoException e) {
		ExceptionUtil.handle(e);
		PedidoService.getInstance().reabrirPedido(pedido);
	}

	private void nfeException(Pedido pedido, NfeException e) {
		ExceptionUtil.handle(e);
		UiUtil.showErrorMessage(e.getMessage().startsWith("NFE_EXCEPTION") ? Messages.ERRO_GERACAO_NFE_REABERTURA_PEDIDO : e.getMessage());
		PedidoService.getInstance().reabrirPedido(pedido);
	}

	private void relProdutosRentabilidadeSemAlcadaException(Pedido pedido, RelProdutosRentabilidadeSemAlcadaException ex) {
		ExceptionUtil.handle(ex);
		PedidoService.getInstance().reabrirPedido(pedido);
		RelProdutosRentabilidadeSemAlcadaWindow relProdutosRentabilidadeSemAlcadaWindow = new RelProdutosRentabilidadeSemAlcadaWindow(ex.getMessage(), ex.getItemPedidoErroList());
		relProdutosRentabilidadeSemAlcadaWindow.popup();
	}

	private void enviaPedido(boolean isFromBtFecharClick, boolean isFromItemPedido, boolean isPossuiConexao, boolean isPedidoConsignado) throws SQLException {
		if (!isFromBtFecharClick) {
			enviaPedido(isFromItemPedido, isPossuiConexao);
			return;
		}

		if (LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao || (LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ && !LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) || UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMA_ENVIO_ERP)) {
			enviaPedido(isFromItemPedido, isPossuiConexao);
			if (!isPedidoConsignado) return;

			enviaPedidosConsignado();
	}
	}

	private boolean validaFechamentoPedido(Pedido pedido, boolean validateFechamentoPedido) throws SQLException {
		if (!validateFechamentoPedido) return true;

		PedidoService.validationFechamentoListCount = 0;
		pedido.ignoraValidacaoSugestaoDifProdutos = false;
		pedido.ignoraValidacaoSugestaoProdutosComQtde = false;
		pedido.ignoraValidacaoSugestaoProdutosSemQtde = false;
		pedido.ignoraValidacaoSugestaoItensRentabilidadeIdeal = false;
		pedido.ignoraValidacaoProdutosPendentes = false;
		pedido.ignoraValidacaoAtrasoCliente = naoAvisaClienteAtrasado;
		pedido.ignoraValidacaoMultiplosSugestaoProdutos = false;
		pedido.ignoraGiroProdutoPendente = false;
		pedido.ignoraValidacaoLimiteCreditoCliente = false;
		return validateFechamentoPedido();
	}

	private boolean validaEntregaPedido(Pedido pedido, boolean isFromBtFecharClick) throws SQLException {
		if (!LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil || ValueUtil.isEmpty(pedido.dtEntrega) || pedido.isLiberadoEntrega() || isFromBtFecharClick) return true;

		return PedidoUiUtil.isConfirmaPedidoDtEntregaFinalSemanaFeriado(pedido);
	}

	private void atualizaFaixaBoletoByPedidoBoletos(Pedido pedido) throws SQLException {
		final TipoPedido tipoPedido = pedido.getTipoPedido();
		if (naoGeraPedidoBoleto() || tipoPedido == null || !tipoPedido.isGeraBoleto() || ValueUtil.isEmpty(pedido.getPedidoBoletoList())) return;
		int size = pedido.getPedidoBoletoList().size();
		for (int i = 0; i < size; i++) {
			PedidoBoleto pedidoBoleto = (PedidoBoleto) pedido.getPedidoBoletoList().items[i];
			FaixaBoleto faixaBoletoFilter = new FaixaBoleto();
			faixaBoletoFilter.cdEmpresa = pedidoBoleto.cdEmpresa;
			faixaBoletoFilter.cdRepresentante = pedidoBoleto.cdRepresentante;
			faixaBoletoFilter.cdBoletoConfig = pedidoBoleto.cdBoletoConfig;
			faixaBoletoFilter.nuUltimoBoleto = pedidoBoleto.nuDocumento;
			FaixaBoletoService.getInstance().updateLastFaixaBoleto(faixaBoletoFilter);
		}
	}

	private boolean naoGeraPedidoBoleto() {
		return !LavenderePdaConfig.isUsaGeracaoBoletoApenasContingencia() || LavenderePdaConfig.usaImpressaoBoletoViaBluetooth <= 0;
	}

	private void enviaPedidosConsignado() throws SQLException {
		if (LavenderePdaConfig.usaEnvioPedidoBackground) {
			String cdSessao = PedidoService.getInstance().generateIdGlobal();
			Pedido pedido = getPedido();
			PedidoConsignacaoService.getInstance().enviaPedidosConsignacaoBackground(pedido);
			Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
			EnviaDadosThread.getInstance().enviaVisita(cdSessao, visita);
			if (!LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				NotificacaoPdaService.getInstance().restauraNotificacaoPdaAndSend2Web(cdSessao);
			}
			close();
		} else {
			PedidoUiUtil.enviaConsignacao();
		}
	}

	private void enviaPedido(boolean isFromItemPedido, boolean isPossuiConexao) throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaEnvioPedidoBackground) {
			if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
				if (BaseUIForm.isListDadosRecebidosWindowNull()) {
					BaseUIForm.setListDadosRecebidosWindow(new ListDocNaoImpressoWindow());
				}
				PedidoService.getInstance().validatePedidoFechado(pedido);
				RecebeRetornoPedidoRunnable.getInstance().setPedido(pedido);
				if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
					Pedido pedidoBonificacaoContaCorrente = PedidoService.getInstance().getPedidoBonificacaoContaCorrente(pedido);
					if (pedidoBonificacaoContaCorrente != null) {
						RecebeRetornoPedidoRunnable.getInstance().setPedidoBonificado(pedidoBonificacaoContaCorrente);
						PedidoService.getInstance().findItemPedidoList(pedido, true);
					}
				}
				RecebeRetornoPedidoRunnable.addQueue();
				close();
			} else {
				String cdSessao = PedidoService.getInstance().generateIdGlobal();
				//--Necessário para enviar também os pedidos do cliente nas demais empresas
				if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas > 0) {
					PedidoUiUtil.enviaPedidosClienteOutrasEmpresas(cdSessao, pedido);
				} else {
					EnviaDadosThread.getInstance().enviaPedido(cdSessao, pedido);
					Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
					EnviaDadosThread.getInstance().enviaVisita(cdSessao, visita);
					VisitaPedidoService.getInstance().enviaVisitaPedido(cdSessao, visita, pedido.nuPedido);
				}
				if (!LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
					NotificacaoPdaService.getInstance().restauraNotificacaoPdaAndSend2Web(cdSessao);
				}
				if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && pedido.isPendente()) {
					PedidoDescService.getInstance().enviaPedidoDescBackground(cdSessao, pedido);
					if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
						ItemLiberacaoService.getInstance().enviaItemLiberacaoBackground(cdSessao, pedido);
					}
				}
				if (LavenderePdaConfig.usaControlePontuacao) {
					PontExtPedService.getInstance().enviaExtratoBackground(cdSessao, pedido);
				}
				PedidoUiUtil.enviaRecebeDadosPedido();
				close();
			}
		} else {
			if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() && ("1".equals(pedido.getInfoNfe().dsTipoEmissao) || pedido.getInfoNfce().isNfce())) {
				if (recebeRetorno(pedido.getTipoPedido())) {
					if (LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ && LavenderePdaConfig.usaGeracaoTxtNfe) {
						if (LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao || UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMA_ENVIO_ERP)) {
							PedidoUiUtil.enviaPedido(true, isFromItemPedido, true);
						}
					}
					afterEnvioPedido();
					visibleState();
				}
			} else if (isPossuiConexao && PedidoUiUtil.enviaPedido(true, isFromItemPedido, true)) {
				Pedido ped = (Pedido) PedidoService.getInstance().findByRowKeyDyn(getPedido().getRowKey());
				edit(ped);
				TipoPedido tipoPedido = getPedido().getTipoPedido();
				if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() && !isNfeContingencia(ped)) {
					recebeRetornoDadosPedido(tipoPedido);
				}
				afterEnvioPedido();
			} else {
				verificaSeHouveErroServidorReservaProdutoAtomico(isPossuiConexao, pedido);
				edit(pedido);
				cancelarClick();
			}
		}
	}

	private boolean isNfeContingencia(Pedido ped) throws SQLException {
		if (LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido()) {
			return true;
		}
		Nfe nfe = NfeService.getInstance().getNfe(ped.cdEmpresa, ped.cdRepresentante, ped.nuPedido, ped.flOrigemPedido);
		return nfe != null && nfe.isContingencia();
	}

	private void recebeRetornoDadosPedido(TipoPedido tipoPedido) {
		boolean nfe = (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ) && tipoPedido != null && tipoPedido.isGeraNfe();
		boolean boleto = LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto && tipoPedido != null && tipoPedido.isGeraBoleto();
		boolean erpDif = LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif;
		boolean nfce = LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfce() && tipoPedido != null && tipoPedido.isGeraNfce();
		try {
			UiUtil.showProcessingMessage(msgRecebimentoOuProcessamentoDados(false, nfe, boleto, erpDif, nfce));
			Vm.sleep(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosPedido);
			UiUtil.unpopProcessingMessage();
			UiUtil.showProcessingMessage(msgRecebimentoOuProcessamentoDados(true, nfe, boleto, erpDif, nfce));
			LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
			erpToPda.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacao(nfe, boleto, erpDif, nfce));
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex.getMessage());
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void verificaSeHouveErroServidorReservaProdutoAtomico(boolean isPossuiConexao, Pedido pedido) throws SQLException {
		if (isPossuiConexao && LavenderePdaConfig.isUsaReservaEstoqueCentralizadoAtomico()) {
			try {
				String retorno = SyncManager.verificaReservaCadastradaServidor(pedido, pedido.itemPedidoList);
				if ("OK".equals(retorno)) {
					return;
				}
				pedido.itemPedidoProblemaReservaEstoqueList = ItemPedidoService.getInstance().getItemPedidoProblemaReservaEstoqueList(retorno);
			} catch (Throwable e) {
				return;
			}
			try {
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
				PedidoService.getInstance().reabrirPedido(pedido);
				updateCurrentRecordInList(pedido);
				clearScreenTransportadoraReg();
				if (pedido.isPedidoComplementar()) {
					cbCondicaoPagamento.setVisible(!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento());
				}
				UiUtil.showErrorMessage(ValueUtil.isEmpty(pedido.itemPedidoProblemaReservaEstoqueList) ? Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE_DESCONHECE_ITENS : Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	private void afterEnvioPedido() throws SQLException {
		if (!LavenderePdaConfig.isSugereImpressaoDocumentosPedidoAposEnvio()) {
			close();
		}
		if (LavenderePdaConfig.relDiferencasPedido && LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif) {
			RelDiferencasListPedidosWindow relDiferencasPedidoWindow = new RelDiferencasListPedidosWindow();
			relDiferencasPedidoWindow.popup();
		}
	}

	private void sugereImpressaoNfeContingencia() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_NFE_CONTINGENCIA)) {
			btImprimirNfeContingenciaClick();
		}
	}

	private boolean recebeRetorno(TipoPedido tipoPedido) throws SQLException, EnvioDadosJsonException {
		boolean nfe = (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.usaRetornoAutomaticoValidacaoSEFAZ) && tipoPedido != null && tipoPedido.isGeraNfe();
		boolean boleto = LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto && tipoPedido != null && tipoPedido.isGeraBoleto();
		boolean erpDif = LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif;
		boolean nfce = LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfce() && tipoPedido != null && tipoPedido.isGeraNfce();
		Pedido pedido = getPedido();
		PedidoService.getInstance().validatePedidoFechado(pedido);
		try {
			UiUtil.showProcessingMessage(msgRecebimentoOuProcessamentoDados(false, nfe, boleto, erpDif, nfce));
			Vm.sleep(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosPedido);
			UiUtil.updateProcessingMessage(msgRecebimentoOuProcessamentoDados(true, nfe, boleto, erpDif, nfce));
			LavendereWeb2Tc erpToPda2 = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync().configReadTimeout(LavenderePdaConfig.valorTimeOutRetornoDadosRelativosPedido));
			return erpToPda2.recebeRetornoPedido(pedido, false);
		} catch (EnvioDadosJsonException e) {
			estornaEnvioNfe(pedido);
			throw e;
		} catch (EnvioDadosNfeException e) {
			estornaEnvioNfe(pedido);
			try {
			PedidoService.getInstance().reabrirPedido(pedido);
			} catch (Throwable e1) {
				UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_TENTAR_REABRIR_AUTO);
			}
			throw e;
		} catch (ApplicationException e) {
			if (e.getMessage().equalsIgnoreCase("Read Timed Out")) {
				if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_INFO_TENTAR_RECEBER_NOVAMENTE)) {
					return recebeRetorno(tipoPedido);
				}
			} else {
				UiUtil.showErrorMessage(e.getMessage());
			}
		} catch (ConnectionException e) {
			edit(pedido);
			cancelarClick();
			throw e;
		} catch (ValidationException e) {
			UiUtil.showErrorMessage(e.getMessage());
			return true;
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex.getMessage());
		} finally {
			visibleState();
			UiUtil.unpopProcessingMessage();
		}
		return false;
	}

	private String msgRecebimentoOuProcessamentoDados(boolean recebimento, boolean nfe, boolean boleto, boolean erpDif, boolean nfce) {
		StringBuffer msgRecebendoDados = new StringBuffer();
		msgRecebendoDados.append(recebimento ? Messages.PEDIDO_MSG_RECEBENDO_DADOS : Messages.PEDIDO_MSG_PROCESSANDO_DADOS);
		if (nfe) {
			msgRecebendoDados.append(Messages.PEDIDO_MSG_RECEBENDO_DADOS_NFE).append(",");
		}
		if (boleto) {
			if (!erpDif && nfe) {
				msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
				msgRecebendoDados.append(" e");
			}
			msgRecebendoDados.append(Messages.PEDIDO_MSG_RECEBENDO_DADOS_BOLETO).append(",");
		}
		if (erpDif) {
			if (boleto || nfe) {
				msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
				msgRecebendoDados.append(" e");
			}
			msgRecebendoDados.append(recebimento ? Messages.PEDIDO_MSG_RECEBENDO_DADOS_PEDIDOERP : Messages.PEDIDO_MSG_PROCESSANDO_DADOS_PEDIDOERP).append(",");
		}
		if (nfce) {
			if (boleto || nfe || erpDif) {
				msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
				msgRecebendoDados.append(" e");
			}
			msgRecebendoDados.append(Messages.PEDIDO_MSG_RECEBENDO_DADOS_NFCE).append(",");
		}
		msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
		msgRecebendoDados.append(".");
		return msgRecebendoDados.toString();
	}

	private void estornaEnvioNfe(Pedido pedido) throws SQLException {
		pedido.flNfeImpressa = ValueUtil.VALOR_NAO;
		PedidoService.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_FLNFEIMPRESSA, ValueUtil.VALOR_NAO, Types.VARCHAR);
		edit(pedido);
		cancelarClick();
	}

	//@Override
	protected void salvarClick() throws SQLException {
		validateComponentes();
		Pedido pedido = getPedido();
		if (PedidoUiUtil.isCondPagtoVerificado(pedido)) {
			return;
		}
		//--
		//--
		if (LavenderePdaConfig.usaCalculoVpcItemPedido() && pedido.itemPedidoList.size() > 0) {
			calculaVpcParaItens(pedido.itemPedidoList);
		}
		if (isPedidoSemItens(pedido) && isEditing()) {
			if (deletePedidoWhenNotItem()) {
				close();
			} else {
				naoConsisteValidacaoFechamentoAoSalvar = true;
				onSave();
				naoConsisteValidacaoFechamentoAoSalvar = false;
			}
		} else {
			if (LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao && LavenderePdaConfig.usaSugestaoFechamentoAoSairPedido && isEditing() && isEnabled() && ValueUtil.isNotEmpty(pedido.itemPedidoList) && pedido.getTipoPedido() != null && !pedido.getTipoPedido().isObrigaConsignacao()) {
				if (UiUtil.showConfirmYesNoMessage(Messages.MSG_FECHAR_PEDIDO_AO_SAIR)) {
					super.screenToDomain();
					fecharEnviarPedido(false, false, false, true);
				} else {
					onSave();
				}
			} else if (LavenderePdaConfig.sugereEnvioAutomaticoPedido && pedido.isPedidoAberto() && controlEnvioAutoPedido && LavenderePdaConfig.usaSugestaoFechamentoAoSairPedido && pedido.getTipoPedido() != null && !pedido.getTipoPedido().isObrigaConsignacao()) {
				if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_ENVIAR_PEDIDOS_AGORA)) {
					super.screenToDomain();
					fecharEnviarPedido(false, false, false, true);
				} else {
				    onSave();
				}
			} else if (LavenderePdaConfig.usaSugestaoFechamentoAoSairPedido && isEditing() && isEnabled() && ValueUtil.isNotEmpty(pedido.itemPedidoList) && pedido.getTipoPedido() != null && !pedido.getTipoPedido().isObrigaConsignacao()) {
				if (UiUtil.showConfirmYesNoMessage(Messages.MSG_FECHAR_PEDIDO_AO_SAIR)) {
					showMessageConfirmClosePedido = false;
					resultado = true;
					btFecharPedidoClick();
					if (!(LavenderePdaConfig.sugereEnvioAutomaticoPedido || LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao)) {
						close();
					}
				} else {
					onSave();
				}
			} else {
				onSave();
			}
		}
		recalcularRentabilidadePedido(RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_SALVAMENTO_PEDIDO);
	}

	private void calculaVpcParaItens(Vector itemPedidoList) throws SQLException {
		if (ValueUtil.isEmpty(getPedido().nuPedido)) return;

		double vlpct = edVlPctVpc.getValueDouble();
		ItemPedidoService.getInstance().updateVlVpcItensPedido(getPedido(), vlpct);
		reloadAndUpdateValoresDoPedido(getPedido(), false);
	}

	//@Override
	protected void cancelarClick() throws SQLException {
		naoConsisteValidacaoFechamentoAoSalvar = false;
		controlEnvioAutoPedido = isEditing() && getPedido().isPedidoAberto();
	}

	//Overrride
	protected void voltarClick() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(pedido.docAnexoList) && !UiUtil.showWarnConfirmYesNoMessage(Messages.DOCANEXO_NAO_INSERIDOS)) {
			return;
		}
		if (PedidoUiUtil.positivacaoItensByFornecedores(this, pedido, isEnabled())) {
			return;
		}
		if (PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido() && isEditing() && isEnabled() && PedidoService.getInstance().findByRowKeyDyn(pedido.rowKey) != null) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_NECESSARIO_FECHAR_EXCLUIR);
			return;
		}
		ListItemPedidoForm.invalidateInstance();
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0 && LavenderePdaConfig.isPermiteImpressaoPedido()) {
			if (!ValueUtil.VALOR_SIM.equals(pedido.flImpresso) && (pedido.isPedidoFechado() || pedido.isPedidoTransmitido())) {
				int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_NAO_IMPRESSO);
				switch (result) {					case 0:
						return;
					case 2:
						btImprimirPedidoClick();
						break;
				}
			}
		} else if (LavenderePdaConfig.getSugereImpressaoDocumentosPedidoAposEnvio().contains(TipoPedido.TIPO_IMPRESSAO_NFE_CONTINGENCIA) && LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			if (!pedido.isNfeContImpressa() && !pedido.isNfeImpressa() && (pedido.isPedidoFechado() || pedido.isPedidoTransmitido()) && ValueUtil.isEmpty(pedido.getInfoNfe().nuPedido) &&
					pedido.getTipoPedido() != null && pedido.getTipoPedido().isGeraNfe()) {
				int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_NFECONTINGENCIA_NAO_IMPRESSO);
				switch (result) {
					case 0:
						return;
					case 2:
						btImprimirNfeContingenciaClick();
						break;
				}
			}
		}
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() && !ValueUtil.VALOR_SIM.equals(pedido.flNfeImpressa) && (pedido.isPedidoFechado() || pedido.isPedidoTransmitido()) && ValueUtil.isNotEmpty(pedido.getInfoNfe().nuPedido)) {
			int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_NFE_NAO_IMPRESSO);
			switch (result) {
				case 0:
					return;
				case 2:
					btImprimirNfeClick();
					break;
			}
		}
		if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0 && !pedido.isBoletoImpresso() && (pedido.isPedidoFechado() || pedido.isPedidoTransmitido()) && ValueUtil.isNotEmpty(pedido.getPedidoBoletoList())) {
			int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_BOLETO_NAO_IMPRESSO);
			switch (result) {
				case 0:
					return;
				case 2:
					btImprimirBoletoClick();
					break;
			}
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4 && !pedido.isPedidoConsignacaoImpresso() && pedido.isPedidoConsignado() && ValueUtil.isNotEmpty(pedido.getPedidoConsignacaoList())) {
			int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_PEDIDOCONSIGNACAO_NAO_IMPRESSO);
			switch (result) {
				case 0:
					return;
				case 2:
					btImprimirPedidoConsignacaoClick();
					break;
			}
		}

		if (Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(pedido.cdCliente) && LavenderePdaConfig.isPermitePedidoNovoCliente() && !isEditing()) {
			Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
			if (visita != null && Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(visita.cdCliente)) {
				VisitaService.getInstance().delete(visita);
				SessionLavenderePda.reloadVisitaAndamento();
			}
		}

		//--
		if (LavenderePdaConfig.usaVisitaFoto) {
			VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(pedido.getVisita());
		}
		boolean pedidoJaExcluido = false;
		if (isPedidoSemItens(pedido) && isEditing() && isEnabled()) {
			pedidoJaExcluido = deletePedidoWhenNotItem();
		}
		if ((LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) && pedido.isPedidoAberto()) {
			SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_NAO;
		}
		if (!pedidoJaExcluido && LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedidoAoCancelar() && pedido.isPedidoAbertoEditavel()
				&& !pedido.getCliente().isClienteDefaultParaNovoPedido() && !pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
			RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(pedido, true, true);
			relProdutosPendentesWindow.cadPedidoForm = this;
			if (relProdutosPendentesWindow.hasProdutosPendentes()) {
				relProdutosPendentesWindow.popup();
				if (!relProdutosPendentesWindow.continua) {
					return;
				}
			} else {
				RelProdutosPendentesWindow.cleanInstance();
			}
		}
		if (LavenderePdaConfig.usaFotoPedidoNoSistema && pedido.isPedidoAberto() && !isEditing() && ValueUtil.isNotEmpty(pedido.fotoList)) {
			if (UiUtil.showWarnConfirmYesNoMessage(Messages.FOTOPEDIDO_NAO_INSERIDOS)) {
				FotoPedidoService.getInstance().deleteFotos(pedido);
			} else {
				return;
			}
		}
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth() && !ValueUtil.VALOR_SIM.equals(getPedido().flNfceImpressa) && (getPedido().isPedidoTransmitido()) && ValueUtil.isNotEmpty(getPedido().getNfce().nuPedido)) {
			int result = UiUtil.showConfirmYesNoCancelMessage(Messages.PEDIDO_MSG_NFCE_NAO_IMPRESSO);
			switch (result) {
				case 0:
					return;
				case 2:
					btImprimirNfceClick();
					break;
			}
		}
		IndiceClienteGrupoProdService.getInstance().clearCache();
		//--
		barBottomContainer.removeAll();
		super.voltarClick();
	}

	private boolean createPedidoOportunidade() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido != null && ValueUtil.isNotEmpty(pedido.itemPedidoOportunidadeList)) {
			UiUtil.showProcessingMessage();
			try {
				UiUtil.showInfoMessage(Messages.OPORTUNIDADE_NOVO_PEDIDO);
				UiUtil.updateProcessingMessage(Messages.OPORTUNIDADE_NOVO_PEDIDO_AGUARDE);
				Vm.sleep(2000);
				Vector itemPedidoList = new Vector();
				try {
					int size = pedido.itemPedidoOportunidadeList.size();
					for (int i = 0; i < size; i++) {
						ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoOportunidadeList.items[i];
						itemPedidoList.addElement(new Object[] { itemPedido });
					}
					TipoPedido tipoPedido = TipoPedidoService.getInstance().findTipoPedidoOportunidade(pedido.cdEmpresa, pedido.cdRepresentante);
					pedido.cdTipoPedido = tipoPedido.cdTipoPedido;
					ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, pedido.getCliente(), pedido.itemPedidoOportunidadeList, false, false, false);
					pedido.itemPedidoOportunidadeList = new Vector();
				} catch (Throwable e) {
					if (UiUtil.showErrorConfirmYesNoMessage(Messages.OPORTUNIDADE_NOVO_PEDIDO_ERRO)) {
						RelInsercaoItemPedidoOportunidadeWindow relItemPedidoWindow = new RelInsercaoItemPedidoOportunidadeWindow(Messages.OPORTUNIDADE_ITENS, "", itemPedidoList, false);
						relItemPedidoWindow.popup();
					}
					delete(pedido);
					return false;
				}
				if (pedido != null) {
					edit(pedido);
					if (pedido.itemPedidoNaoInseridoSugestaoPedList.size() > 0) {
						RelInsercaoItemPedidoOportunidadeWindow relItemPedidoWindow = new RelInsercaoItemPedidoOportunidadeWindow(Messages.PEDIDO_ITENS_NAO_INSERIDOS, "", pedido.itemPedidoNaoInseridoSugestaoPedList, true);
						relItemPedidoWindow.popup();
					}
					return true;
				}
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
		return false;
	}

	private void btImprimirPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		try {
			if (pedido.flImpresso != null && pedido.flImpresso.equals(ValueUtil.VALOR_SIM)) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_NOVAMENTE)) {
					return;
				}
			}
			pb.makeUnmovable();
			pb.popupNonBlocking();
			pedidoServicePrint.imprimePedido();
			PedidoService.getInstance().updateFlImpressoPedido(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

	private void btImprimirNfeContingenciaClick() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(pedido.getInfoNfe().nuPedido)) {
			UiUtil.showErrorMessage(Messages.IMPRESSAONFECONTINGENCIA_MSG_ERRO_EXISTE_NFE);
			return;
		}
		if (pedido.isNfeContImpressa() && !UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_NFECONTINGENCIA_NOVAMENTE)) {
			return;
		}
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoNfeContingenciaManagerPrint pedidoNfeContingenciaManagerPrint = new PedidoNfeContingenciaManagerPrint(pedido);
			pedidoNfeContingenciaManagerPrint.imprimeNfeContingencia();
			if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 2 && UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAONFECONTINGENCIA_MSG_CONFIRMACAO_COMPROVANTE)) {
				pedidoNfeContingenciaManagerPrint.imprimeComprovanteNfeContingencia();
			}
			PedidoService.getInstance().updateFlImpressoNfeCont(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONFECONTINGENCIA_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

	private void btImprimirPedidoConsignacaoClick() {
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		try {
			Pedido pedido = getPedido();
			PedidoConsignacaoManagerPrint pedidoConsignacaoManagerPrint = new PedidoConsignacaoManagerPrint(pedido);
			if (pedido.flConsignacaoImpressa != null && pedido.flConsignacaoImpressa.equals(ValueUtil.VALOR_SIM)) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_CONSIGNACAO_NOVAMENTE)) {
					return;
				}
			}
			pb.makeUnmovable();
			pb.popupNonBlocking();
			pedidoConsignacaoManagerPrint.imprimePedidoConsignacao();
			if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 2) {
				if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOCONSIGNACAO_MSG_CONFIRMACAO_COMPROVANTE)) {
					pedidoConsignacaoManagerPrint.imprimePedidoConsignacao(true);
				}
			}
			PedidoService.getInstance().updateFlConsignacaoImpressa(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOCONSIGNACAO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

	private void btImprimirNfeClick() throws SQLException {
		btImprimirNfeClick(null);
	}

	private void btImprimirNfeClick(Pedido pedido) throws SQLException {
		if (pedido == null) {
			pedido = getPedido();
		}
		Nfe nfe = NfeService.getInstance().getNfe(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido, pedido.flOrigemPedido);

		if(nfe != null && StatusNfeService.getInstance().isStatusNfeBloqueiaImpressao(nfe)) {
				return;
		}

		if (nfe == null && pedido.isFlOrigemPedidoErp()) {
			nfe = NfeService.getInstance().findNfeByNuPedidoRelacionado(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedidoRelacionado);
		}
		if (nfe == null || nfe.cdEmpresa == null) {
			UiUtil.showErrorMessage(Messages.IMPRESSAONFE_MSG_PEDIDO_SEM_NFE);
			return;
		}
		if (LavenderePdaConfig.isExibeCondicaoPagamentoNaImpressaoComprovanteNfe() && ValueUtil.isNotEmpty(getPedido().cdCondicaoPagamento)) {
			CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(getPedido().cdCondicaoPagamento);
			if (condicaoPagamento != null && condicaoPagamento.cdEmpresa != null) {
				getPedido().setCondicaoPagamento(condicaoPagamento);
			}
		}
		if (pedido.isNfeImpressa() && !UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_NFE_NOVAMENTE)) {
			return;
		}

		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
			pedidoServicePrint.imprimeNfe();
			if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 2) {
				if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAONFE_MSG_CONFIRMACAO_COMPROVANTE)) {
					pedidoServicePrint.imprimeComprovanteNfe();
				}
			}
			PedidoService.getInstance().updateFlImpressoNfe(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONFE_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}


	private void btImprimirNfceClick() throws SQLException {
		Nfce nfce = NfceService.getInstance().getNfce(getPedido().cdEmpresa, getPedido().cdRepresentante, getPedido().nuPedido, getPedido().flOrigemPedido);
		if (!SyncManager.isConexaoPdaDisponivel()) {
			UiUtil.showErrorMessage(Messages.IMPRESSAONFCE_MSG_CONEXAO_ERRO);
			return;
		}

		if (nfce == null) {
			UiUtil.showErrorMessage(Messages.IMPRESSAONFCE_MSG_PEDIDO_SEM_NFCE);
			return;
		} else {
			this.getPedido().nfce = nfce;
		}
		if (getPedido().isNfeImpressa() && !UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_NFCE_NOVAMENTE)) {
			return;
		}

		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoNfceServicePrint nfceServicePrint = new PedidoNfceServicePrint(getPedido());
			if (nfce.isNfceContingencia()) {
				nfceServicePrint.imprimeNfceContingencia();
			} else {
				nfceServicePrint.imprimeNfce();
			}

			PedidoService.getInstance().updateFlImpressoNfce(getPedido());
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONFCE_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

	private void btImprimirBoletoContingenciaClick() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(pedido.getPedidoBoletoList())) {
			UiUtil.showErrorMessage(Messages.IMPRESSAOBOLETOCONTINGENCIA_MSG_ERRO_EXISTE_BOLETO);
			return;
		}
		if (pedido.isBoletoImpresso() && !UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_BOLETOCONTINGENCIA_NOVAMENTE)) {
			return;
		}
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoBoletoService.getInstance().geraBoletoPedido(pedido);
			PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
			pedidoServicePrint.imprimeBoleto();
			if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 2 && UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOBOLETOCONTINGENCIA_MSG_CONFIRMACAO_COMPROVANTE)) {
				pedidoServicePrint.imprimeBoleto(true);
			}
			PedidoService.getInstance().updateFlImpressoBoleto(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.quebraLinhas(MessageUtil.getMessage(Messages.IMPRESSAOBOLETOCONTINGENCIA_MSG_ERRO, ex.getMessage())));
		} finally {
			pedido.pedidoBoletoList = new Vector();
			pb.unpop();
		}
	}

	private void btImprimirBoletoPDFClick()  {
		String pdf;
		String nuPedido = "";
		try {
			nuPedido = getPedido().nuPedido;
			if (getPedido().nfe != null) {
				boolean isTituloFinanceiroRelacionadoAoPedido = TituloFinanceiroService.getInstance().isTituloFinanceiroRelacionadoByPedido(getPedido().nfe.nuNfe);
				if (!isTituloFinanceiroRelacionadoAoPedido) {
					UiUtil.showInfoMessage(Messages.MSG_ERRO_GERAR_PDF_BOLETO_NAO_RELACIONADO);
					return;
				}
			}
			pdf = SyncManager.geraPdfPedidoBoleto(getPedido());
			pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
				abrePdfOnline(pdf);
			} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_GERADO_SUCESSO)) {
				abrePdfOnline(pdf);
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} catch (Exception e) {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO, nuPedido), StringUtil.clearEnterException(e.getMessage()));
		}
	}

	private void btImprimirBoletoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (ValueUtil.isEmpty(pedido.getPedidoBoletoList())) {
			if ((pedido.isPedidoFechado() || pedido.isPedidoTransmitido()) && LavenderePdaConfig.isUsaGeracaoBoletoApenasSolicitado()) {
				btImprimirBoletoContingenciaClick();
				if (LavenderePdaConfig.mostraAbaBoletoNoPedido) {
					setBoletoVisible();
				}
				carregaBoletos();
				return;
			}
			UiUtil.showErrorMessage(Messages.IMPRESSAOBOLETO_MSG_PEDIDO_SEM_BOLETO);
			return;
		}
		if (pedido.isBoletoImpresso() && !UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_BOLETO_NOVAMENTE)) {
			return;
		}
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
			pedidoServicePrint.imprimeBoleto();
			if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 2 && UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOBOLETO_MSG_CONFIRMACAO_COMPROVANTE)) {
				pedidoServicePrint.imprimeBoleto(true);
			}
			PedidoService.getInstance().updateFlImpressoBoleto(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
		if (LavenderePdaConfig.mostraAbaBoletoNoPedido) {
			setBoletoVisible();
	}
		carregaBoletos();
	}

	private void btFotoClick() throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			if (SessionLavenderePda.visitaAndamento == null && !CadClienteMenuForm.btRegistrarAtualizarChegadaClick(null)) {
				UiUtil.showErrorMessage(Messages.MSG_REGISTRO_CHEGADA_CLIENTE_NAO_REGISTRADA);
			} else {
				SessionLavenderePda.visitaAndamento.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(SessionLavenderePda.visitaAndamento));
				CadVisitaFotoWindow visitaFoto = new CadVisitaFotoWindow(SessionLavenderePda.visitaAndamento);
				visitaFoto.popup();
				VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(SessionLavenderePda.visitaAndamento);
			}
		} else {
			Visita visita = getPedido().getVisita();
			CadVisitaFotoWindow cadVisitaFotoForm = new CadVisitaFotoWindow(visita);
			cadVisitaFotoForm.popup();
		}
	}

	private boolean pedidoFechadoOuTransmitido() throws SQLException {
		return getPedido().isPedidoFechado() || getPedido().isPedidoTransmitido();
	}

	private void ckVinculaCampanhaPublicitariaClick() throws SQLException {
		getPedido().flVinculaCampanhaPublicitaria = ckVinculaCampanhaPublicitaria.isChecked() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		if (!ckVinculaCampanhaPublicitaria.isChecked()){
			getPedido().cdCampanhaPublicitaria = null;
			edNuCampanhaPublicitaria.setText(ValueUtil.VALOR_EMBRANCO);
		}
		houveAlteracaoCampos = true;
		remontaTela();
		reposition();
	}

	private void btReplicarPedidoClick() throws SQLException {
		Pedido pedidoRef = getPedido();
		if (LavenderePdaConfig.usaEscolhaEmpresaPedido || LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) {
			if (getBaseCrudListForm() instanceof ListPedidoForm) {
				ListPedidoForm listPedidoForm = (ListPedidoForm) getBaseCrudListForm();
				if (listPedidoForm != null && listPedidoForm.inConsultaUltimosPedidos) {
					SessionLavenderePda.cdEmpresaOld = SessionLavenderePda.cdEmpresa;
					EmpresaService.getInstance().changeEmpresaSessao(pedidoRef.cdEmpresa);
				}
			}
		}
		boolean isAlteraClientePedido = true, isAlteraTipoPedido = false;
		if (LavenderePdaConfig.isIgnoraConfiguracoesReplicacao(Pedido.IGNORA_SELECAO_CLIENTE_REPLICACAO)) {
			isAlteraClientePedido = false;
		}
		if (pedidoRef.isConverteTipoPedidoReplicacao()) {
			pedidoRef.isPedidoReplicadoConvertidoTipoPedido = true;
			isAlteraClientePedido = false;
			isAlteraTipoPedido = true;
		}
		doReplicarPedido(pedidoRef, isAlteraClientePedido, isAlteraTipoPedido);
	}

	private void doReplicarPedido(Pedido pedidoRef, boolean isAlteraClientePedido, boolean isAlteraTipoPedido) throws SQLException {
		boolean inConsultaUltimosPedidos = inOnlyConsultaItens;
		preSelecionaModoFaturamento(pedidoRef);
		try {
			if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
			validaCentroCustoReplicarPedido(pedidoRef);
			Cliente cliente = getClientePedidoReplicado(pedidoRef, isAlteraClientePedido);
			Pedido pedido = (Pedido) pedidoRef.clone();
			UiUtil.showProcessingMessage();
			if (cliente != null) {
				if (setTipoPedidoPedidoReplicado(pedido, isAlteraTipoPedido)) {
					if (!beforeCreateNovoPedido(cliente, true)) return;
					cliente.flClienteLiberadoPedidoAVista = SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista;
					pedido.isReplicandoPedido = true;
					if (LavenderePdaConfig.isUsaMotivoPendencia()) {
						removeMotivoPendencia(pedido);
					}
					removeDatasDeEntrega(pedido);
					boolean aplicaVerba = isAlteraTipoPedido || (LavenderePdaConfig.usaConfirmacaoVerbaPedidoSugestao && PedidoUiUtil.showPopupUsaVerbaReplicacaoPedido(pedido));
					pedido = ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, cliente, aplicaVerba, true);
					if (pedido != null) {
						aposReplicarPedido(cliente, pedido);
						pedido.showMessageLimiteCredito = true;
						if(!isAlteraTipoPedido) {
							reloadAndUpdateValoresDoPedido(pedido, false);
						}
						pedido.isReplicandoPedido = false;
					}
				}
				SessionLavenderePda.setCliente(pedidoRef.getCliente());
			}
		} catch (Throwable e) {
			this.inOnlyConsultaItens = inConsultaUltimosPedidos;
			this.edit(pedidoRef);
			throw e;
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}
	
	public void preSelecionaModoFaturamento(Pedido pedido) {
		if(ValueUtil.isEmpty(pedido.cdDivisaoVenda)) {
			pedido.cdDivisaoVenda = Messages.COMBOBOX_FATURAMENTO_VALOR_PADRAO;
		}
	}
	
	private void removeMotivoPendencia(Pedido pedido) {
		pedido.cdMotivoPendencia = null;
		pedido.dsObsMotivoPendencia = null;
	}
	
	private static void removeDatasDeEntrega(Pedido pedido) {
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			pedido.dtEntrega = null;
			pedido.dtSugestaoCliente = null;
		}
	}

	private Cliente getClientePedidoReplicado(Pedido pedido, boolean isAlteraClientePedido) throws SQLException {
		if(isAlteraClientePedido) {
			ListClienteWindow listClienteWindow = new ListClienteWindow(true, true, false);
			listClienteWindow.popup();
			return listClienteWindow.cliente;
		}
		return pedido.getCliente();
	}

	private boolean setTipoPedidoPedidoReplicado(Pedido pedido, boolean isAlteraTipoPedido) throws SQLException {
		if(isAlteraTipoPedido) {
			ListConversaoTipoPedidoWindow cad = new ListConversaoTipoPedidoWindow(pedido);
			cad.popup();
			TipoPedido tipoPedidoSelecionado = cad.tipoPedidoSelecionado;
			if (tipoPedidoSelecionado == null) return false;
			pedido.cdTipoPedido = tipoPedidoSelecionado.cdTipoPedido;
			pedido.setTipoPedido(tipoPedidoSelecionado);
		}
		return true;
	}

	private boolean recalcularRentabilidadePedido(RecalculoRentabilidadeOptions option) throws SQLException{
		return MargemRentabService.getInstance().recalcularRentabilidadePedidoAbertoSeNecessario(getPedido(), option);
	}

	private void aposReplicarPedido(Cliente cliente, Pedido pedido) throws SQLException {
		this.inOnlyConsultaItens = false;
		this.edit(pedido);
		if (ValueUtil.VALOR_SIM.equals(cliente.flClienteLiberadoPedidoAVista) && !LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueadoAtrasado()) {
			cliente.loadStatusCliente();
			cbCondicaoPagamento.loadCondicoesPagamento(pedido);
			cbCondicaoPagamento.setSelectedIndex(0);
		}
		mostraPopUpDeAvisosAposReplicacao(pedido);
	}

	private void mostraPopUpDeAvisosAposReplicacao(Pedido pedido) {
		if (pedido.hasErrosInsertSugestaoPedido() || pedido.isPossuiItensQueMantiveramPrecoNegociado()) {
			RelItemPedidoDivergenciaWindow relProdutoErroWindow = new RelItemPedidoDivergenciaWindow(Messages.PEDIDO_ITENS_NAO_INSERIDOS, pedido);
			relProdutoErroWindow.popup();
		}
		if (ValueUtil.isNotEmpty(pedido.itemPedidoEstoquePrevistoExcepList)) {
			RelItemPedidoEstoquePrevistoExcep relItemPedidoEstoquePrevistoExcep = new RelItemPedidoEstoquePrevistoExcep(pedido.itemPedidoEstoquePrevistoExcepList);
			relItemPedidoEstoquePrevistoExcep.popup();
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			Vector itemPedidoPendenteList = pedido.getOnlyItemPedidoPendenteList();
			if (ValueUtil.isNotEmpty(itemPedidoPendenteList)) {
				new RelItensPedidoPendenteWindow(itemPedidoPendenteList).popup();
			}
		}
		if (LavenderePdaConfig.apenasAvisaDescontoAcrescimoMaximo && ValueUtil.isNotEmpty(pedido.itemPedidoAConfirmarInsercaoList)) {
			showPopUpListaItensAguardandoConfirmacao(pedido);
		}
	}

	private void showPopUpListaItensAguardandoConfirmacao(Pedido pedido) {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoNaoInseridoSugestaoPedList)) {
			pedido.itemPedidoNaoInseridoSugestaoPedList.removeAllElements();
		}
		ListItemPedidoAvisoDescAcrMaximoWindow listItemPedidoAvisoDescAcrMaximoWindow = new ListItemPedidoAvisoDescAcrMaximoWindow(pedido.itemPedidoAConfirmarInsercaoList);
		listItemPedidoAvisoDescAcrMaximoWindow.popup();
		Vector itemPedidoSelecionados = listItemPedidoAvisoDescAcrMaximoWindow.getItemPedidoSelecionadosList();
		for (int i = 0; i < itemPedidoSelecionados.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoSelecionados.items[i];
			try {
				ClienteService.getInstance().validateAndInsertItemPedidoNaReplicacao(pedido, itemPedido);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			} finally {
				itemPedido.auxiliarVariaveis.isItemComDescAcresMaxExtrapolado = false;
			}
		}
		pedido.itemPedidoAConfirmarInsercaoList = null;
		if (ValueUtil.isNotEmpty(itemPedidoSelecionados)) {
			mostraPopUpDeAvisosAposReplicacao(pedido);
		}
	}
	
	private void validaCentroCustoReplicarPedido(Pedido pedido) throws SQLException {
		if (pedido == null || !LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() || !LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) return;
		if ((ValueUtil.isEmpty(cbCentroCusto.getValue()) && !centroCustoAvailableForRep(pedido)) ||
			(LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais() && (ValueUtil.isEmpty(cbPlataformaVenda.getValue()) && !plataformaVendaAvailableForRep(pedido)))) {
			throw new ValidationException(Messages.PEDIDO_ERRO_REPLICAR_CENTROCUSTO_PLATAFORMAVENDA);
			}
		}

	private boolean plataformaVendaAvailableForRep(Pedido pedido) throws SQLException {
		PlataformaVenda plataformaVenda = PlataformaVendaService.getInstance().findPlataformaVenda(pedido.cdEmpresa, pedido.cdPlataformaVenda);
		if (ValueUtil.isEmpty(plataformaVenda.cdPlataformaVenda)) return false;
		return true;
	}

	private boolean centroCustoAvailableForRep(Pedido pedido) throws SQLException {
		CentroCusto centroCusto = CentroCustoService.getInstance().findCentroCustoInCarga(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCentroCusto);
		if (ValueUtil.isEmpty(centroCusto.cdCentroCusto)) return false;
		return true;
		}

	private boolean beforeCreateNovoPedido(Cliente cliente) throws SQLException {
		return beforeCreateNovoPedido(cliente, false);
	}

	private boolean beforeCreateNovoPedido(Cliente cliente, boolean onReplicarPedido) throws SQLException {
		if (LavenderePdaConfig.validaPeriodoEntregaParaPedido && !ClienteEnderecoService.getInstance().isTodosEnderecosComPeriodoEntrega(cliente)) {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_SEM_PERIODO_ENTREGA);
			return false;
		}
		if (cadClienteMenuForm == null) {
			cadClienteMenuForm = new CadClienteMenuForm();
		}
		if (!cadClienteMenuForm.beforeCreateNovoPedido(cliente, onReplicarPedido)) {
			return false;
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto) {
			getPedido().solicitaSenhaLiberaPrecoProdutoReplicacao = deveSolicitarSenhaLiberaPrecoProduto();
		}
		if (onReplicarPedido && !validaCentroCustoClienteSelecionado(cliente)) {
			UiUtil.showErrorMessage(Messages.PEDIDO_ERRO_REPLICAR_CLIENTE_SEM_CENTRO_CUSTO);
			return false;
		}
		if (onReplicarPedido && !validaPlataformaVendaClienteSelecionado(cliente)) {
			UiUtil.showErrorMessage(Messages.PEDIDO_ERRO_REPLICAR_CLIENTE_SEM_PLATAFORMA_VENDA);
			return false;
		}
		return true;
	}

	private boolean validaPlataformaVendaClienteSelecionado(Cliente cliente) throws SQLException {
		if (cliente == null || !LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais() || !LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) return true;
		Pedido pedido = getPedido();
		if (pedido == null) return true;
		if (!PlataformaVendaClienteService.getInstance().validatePlataformaVendaCliente(cliente, pedido)) return false;
		return true;
	}

	private boolean validaCentroCustoClienteSelecionado(Cliente cliente) throws SQLException {
		if (cliente == null || !LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() || !LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto()) return true;
		Pedido pedido = getPedido();
		if (pedido == null) return true;
		CentroCusto centroCusto = CentroCustoService.getInstance().findCentroCusto(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCentroCusto, cliente);
		if (ValueUtil.isEmpty(centroCusto.cdCentroCusto)) return false;
		return true;
	}

	private boolean deveSolicitarSenhaLiberaPrecoProduto() throws SQLException {
		int countItemPrecoLiberadoSenha = ItemPedidoService.getInstance().countItemComPrecoLiberadoPorSenha(getPedido());
		if (countItemPrecoLiberadoSenha > 0) {
			return UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.MSG_CONFIRMACAO_SOLICITA_SENHA_LIBERA_PRECO_PRODUTO_REPLICACAO, countItemPrecoLiberadoSenha));
		}
		return false;
	}

	private void btDetalhesCalculosClick() throws SQLException {
		boolean funcionalidadesDisponiveis = LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado;
		if (funcionalidadesDisponiveis) {
			Pedido pedido = getPedido();
			if (pedido != null) {
				RelDetalhesCalculosPedidoWindow relDetalhesCalculos = new RelDetalhesCalculosPedidoWindow(pedido);
				relDetalhesCalculos.popup();
			} else {
				UiUtil.showGridEmptySelectionMessage(Messages.PEDIDO_NOME_ENTIDADE);
			}
		} else {
			UiUtil.showInfoMessage(Messages.REL_DETALHES_CALCULOS_NENHUMA_FUNC_DISPONIVEL);
		}
	}

	private void btGerarPdfClick() throws SQLException {
		if (!validateGerarPDF()) return;
		preparePedidoImpressao();
		gerarPdfPedido();
	}

	private void preparePedidoImpressao() throws SQLException {
		if (getPedido().isPedidoAberto()) {
			save();
		}
		ordenaListaPelaUltimaOrdencaoListItemPedido();
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			PedidoService.getInstance().atualizaDadosGradeParaEmissaoRel(getPedido());
		}
		if (getPedido().getCliente().isNovoCliente()) {
			carregaEnderecoNovoCliente(getPedido().getCliente());
		}
	}

	private void btGerarPdfViaClienteClick() throws SQLException {
		if (LavenderePdaConfig.isUsaGeracaoPdfOnline()) {
			if (!validateGerarPDF()) return;
			preparePedidoImpressao();
			geraPdfOnlineViaCliente();
		}
	}

	private void ordenaListaPelaUltimaOrdencaoListItemPedido() throws SQLException {
		Pedido pedido = getPedido();
		String sortAttributeListItemPedido = ListContainerConfig.getDefautSortColumn(ListItemPedidoForm.class.getSimpleName());
		if (ValueUtil.isEmpty(sortAttributeListItemPedido)) {
			sortAttributeListItemPedido = LavenderePdaConfig.usaOrdenacaoDescricaoListaItemPedido ? Pedido.NMCOLUNA_DSPRODUTO : Pedido.NMCOLUNA_NUSEQITEMPEDIDO;
		}

		String sortAsc = ListContainerConfig.getDefautOrder(ListItemPedidoForm.class.getSimpleName());
		if (ValueUtil.isEmpty(sortAsc)) {
			sortAsc = ValueUtil.VALOR_SIM;
		}

		PedidoService.getInstance().realizaOrdenacaoListaItens(pedido, pedido.itemPedidoList, sortAttributeListItemPedido, sortAsc);
	}

	private boolean validateGerarPDF() throws SQLException {
		if (VmUtil.isWinCEPocketPc()) {
			UiUtil.showInfoMessage(Messages.MSG_NAO_POSSUI_SUPORTE_PDF_PEDIDO);
			return false;
		}
		Pedido pedido = getPedido();
		if (pedido == null) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO_NAO_ENCONTRADO);
			return false;
		}
		if (pedido.getQtItensLista() <= 0) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO_SEM_ITENS);
			return false;
		}
		if (pedido.isPedidoNovoCliente() && pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO_SEM_NOVO_CLIENTE);
			return false;
		}
		if (pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO_SEM_CLIENTE);
			return false;
		}
		return true;
	}

	private void geraPdfOnline() throws SQLException {
		geraPdfOnline(false);
	}

	private void geraPdfOnlineViaCliente() throws SQLException {
		geraPdfOnline(true);
	}
	
	private void geraPdfOnline(boolean viaCliente) throws SQLException {
		String nuPedido = "";
		if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto()){
			if (ValueUtil.isEmpty(getPedido().cdCentroCusto)) {
				UiUtil.showInfoMessage(Messages.MSG_PEDIDO_NAO_POSSUI_CENTRO_CUSTO);
				return;
			}
		}
		try {
			nuPedido = getPedido().nuPedido;
			UiUtil.showProcessingMessage();
			String pdf = null;
			try {
				pdf = SyncManager.geraPdfPedido(getPedido(), viaCliente);
				pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			} finally {
				UiUtil.unpopProcessingMessage();	
			}
			if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
				abrePdfOnline(pdf);
			} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_GERADO_SUCESSO)) {
				abrePdfOnline(pdf);
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, getPedido().getRowKey(), getPedido().cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_PON, StringUtil.getStringValue(getPedido().vlTotalPedido));
		} catch (Throwable e) {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO, nuPedido), StringUtil.clearEnterException(e.getMessage()));
			if (LavenderePdaConfig.isUsaGeracaoPdfOffline() && UiUtil.showConfirmYesNoMessage(Messages.SUGERE_PDF_OFFLINE)) {
				geraPdfOffline();
			}
		}
	}

	public void validaPedidoComPagamentoAVista() throws SQLException {
		if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
			Pedido pedido = getPedido();
			if (pedido != null && ValueUtil.VALOR_ZERO.equals(StringUtil.getStringValue(pedido.getCondicaoPagamento().qtDiasMediosPagamento)) && pedido.isPagamentoAVista() && pedido.getTipoPagamento().isTipoPagamentoAVista()) {
				SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
			} else {
				SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_NAO;
			}
		}
	}

	private void sugereRegistroSaida() throws SQLException {
		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
		if (ValueUtil.valueEquals(getPedido().getCliente().cdCliente, visitaEmAndamento.cdCliente)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_REGISTRAR_SAIDA)) {
				if (CadClienteMenuForm.btRegistrarSaidaClick(visitaEmAndamento, false)) {
					try {
						addItensOnButtonMenu();
					} catch (Throwable ex) {
						VmUtil.debug(ex.getMessage());
					}
				}
			}
		}
	}

	private void btListaItensNfeClick() throws SQLException {
		if (LavenderePdaConfig.usaNfePorReferencia) {
			ListItemNfeReferenciaForm listItemNfeReferenciaForm = new ListItemNfeReferenciaForm(getPedido());
			show(listItemNfeReferenciaForm);
		} else {
			ListItemNfeForm listItemNfeForm = new ListItemNfeForm(getPedido());
			show(listItemNfeForm);
		}
	}

	private void btConfiguracaoDescontoClick() throws SQLException {
		RelUsuarioDescWindow relUsuarioDescWindow = new RelUsuarioDescWindow();
		relUsuarioDescWindow.popup();
	}

	private void cbAcrescimoCustoValueChange() throws SQLException {
		CampoDinamicoComboBox cbAcrescimoCusto = (CampoDinamicoComboBox) hashComponentes.get(Pedido.NMCOLUNA_CDMOTIVOACRESCIMOCUSTO);
		if (cbAcrescimoCusto.getSelectedIndex() == 0) {
			EditNumberFrac edit = (EditNumberFrac) hashComponentes.get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO);
			if (edit != null) {
				edit.setValue(0);
				edAcrescimoCustoValueChange();
			}
		}
	}

	private void edAcrescimoCustoValueChange() throws SQLException {
		EditNumberFrac edit = (EditNumberFrac) hashComponentes.get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO);
		if (edit != null) {
			Pedido pedido = getPedido();
			pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO, StringUtil.getStringValue(edit.getValueDouble()));
			if (isEditing()) {
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO, ValueUtil.getDoubleValue((String) pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_VLPCTACRESCIMOCUSTO)), Types.DECIMAL);
			}
			PedidoService.getInstance().calculaRentabilidadeItensPedidoConsiderandoAcrescimoCusto(pedido);
			atualizaComponentesRentabilidade(lvVlPctRentabilidade, btIconeRentabilidade);
		}
	}

	private void bgGeraCreditoFreteValueChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.flGeraCreditoFrete = ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoFrete.getValue()) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		lvGeraCreditoBonificacaoFrete.setVisible(ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoFrete.getValue()) ? true : false);
		if (ValueUtil.isNotEmpty(pedido.nuPedido) && ValueUtil.isNotEmpty(pedido.flOrigemPedido)) {
			try {
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLGERACREDITOFRETE", pedido.flGeraCreditoFrete, totalcross.sql.Types.VARCHAR);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		reloadAndUpdateValoresDoPedido(pedido, false);
		PedidoService.getInstance().calculaValorTotalCreditoFrete(pedido);
		lvGeraCreditoBonificacaoFrete.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoFrete));
	}

	private void bgGeraCreditoCondicaoValueChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.flGeraCreditoCondicao = ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoCondicao.getValue()) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		lvGeraCreditoBonificacaoCondicao.setVisible(ValueUtil.VALOR_SIM.equals(bgGeraCreditoBonificacaoCondicao.getValue()) ? true : false);
		if (ValueUtil.isNotEmpty(pedido.nuPedido) && ValueUtil.isNotEmpty(pedido.flOrigemPedido)) {
			try {
				PedidoService.getInstance().updateColumn(pedido.getRowKey(), "FLGERACREDITOCONDICAO", pedido.flGeraCreditoCondicao, totalcross.sql.Types.VARCHAR);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		reloadAndUpdateValoresDoPedido(pedido, false);
		PedidoService.getInstance().calculaValorTotalCreditoCondicao(pedido);
		lvGeraCreditoBonificacaoCondicao.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoCondicao));
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			PedidoService.getInstance().calculaValorTotalCreditoFrete(pedido);
			lvGeraCreditoBonificacaoFrete.setValue(StringUtil.getStringValueToInterface(pedido.vlTotalCreditoFrete));
		}
	}

	private void atualizaComponentesRentabilidade(LabelValue lvRentabilidade, BaseButton btIcone) throws SQLException {
		Pedido pedido = getPedido();

		if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !pedido.isPedidoBonificacao() && pedido.isPermiteUtilizarRentabilidade()) {
			atualizaIconeRentabilidade(btIcone);
			return;
		}

		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && !pedido.isPedidoBonificacao()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
				atualizaIconeRentabilidade(btIcone);
			} else if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
				lvRentabilidade.usePercentValue = false;
				lvRentabilidade.setValue(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadePedido(pedido));
			} else {
				lvRentabilidade.setValue(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
			}
		}
	}

	private void btLiberarItensPendentesClick() throws SQLException {
		Pedido pedidoPendente = getPedido();
		try {
			Pedido pedido = (Pedido) pedidoPendente.clone();
			PedidoService.getInstance().liberaItensPedidoPendente(pedidoPendente);
			pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			if (!LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
				ItemLiberacaoService.getInstance().saveItemLiberacaoPedido(pedido);
			}
			PedidoService.getInstance().updatePedidoLiberacaoItemPendente(pedidoPendente);
			pedidoPendente.setItemLiberacao(ItemLiberacaoService.getInstance().getItemLiberacaoByPedido(pedidoPendente));
			double vlPctDescontoLiberacaoRestante = PedidoDescService.getInstance().getVlPctDescontoLiberacaoRestante(pedidoPendente);
			if (vlPctDescontoLiberacaoRestante <= 0) {
				PedidoService.getInstance().updateFlPendente(pedidoPendente, ValueUtil.VALOR_NAO);
			}
			if (!LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario()) {
				UiUtil.showSucessMessage(Messages.PEDIDO_MSG_ITENS_PENDENTES_LIBERACAO_TOTAL);
				sugereEnvioItemLiberacaoServidor(pedido);
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_PEDIDO_ITENS_PENDENTES_ERRO_LIBERACAO + ex.getMessage());
		} finally {
			edit(pedidoPendente);
			updateCurrentRecordInList(pedidoPendente);
		}
	}

	private void btLiberarPedidoClick() throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMACAO_LIBERACAO_PEDIDO)) return;
		Pedido pedidoOriginal = getPedido();
		Pedido pedido = (Pedido) pedidoOriginal.clone();
		int nuMaxSequencia = PedidoDescErpService.getInstance().getMaxNuSequenciaLiberacao(pedido);
		if (LavenderePdaConfig.isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto()) {
			liberaPedidoRespeitandoHierarquiaPercentualDesconto(pedidoOriginal, pedido);
		} else if (nuMaxSequencia + 1 < SessionLavenderePda.nuOrdemLiberacaoUsuario) {
			liberaPedidoPorNuOrdemLiberacao(pedidoOriginal, pedido);
		} else if (pedido.isPedidoPendenteLibRep()) {
			liberaPedidoPendenteRep(pedidoOriginal, pedido);
		} else {
			liberaPedidoPendenteDesconto(pedidoOriginal, pedido);
		}
	}

	private void liberaPedidoPendenteRep(Pedido pedidoOriginal, Pedido pedido) {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_LIBERANDO_PEDIDO);
		mb.popupNonBlocking();
		try {
			pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			String result =  SyncManager.liberaPedidoPendenteForaDeOrdem(pedido);
			if (ValueUtil.isNotEmpty(result) && "OK".equals(result)) {
				UiUtil.showInfoMessage(Messages.PEDIDO_LIBERADO_SUCESSO);
				updateCurrentRecordInList(pedidoOriginal);
				voltarClick();
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_LIBERADO_ERRO, result));
				edit(pedidoOriginal);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		} finally {
			mb.unpop();
		}
	}

	private void liberaPedidoRespeitandoHierarquiaPercentualDesconto(Pedido pedidoOriginal, Pedido pedido){
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_LIBERANDO_PEDIDO);
		mb.popupNonBlocking();
		try {
			if (OrigemPedido.FLORIGEMPEDIDO_WEB.equals(pedidoOriginal.flOrigemPedidoRelacionado)) {
				pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_WEB;
			} else {
				pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			}
			String result =  SyncManager.liberaPedidoPendenteForaDeOrdem(pedido);
			if (ValueUtil.isNotEmpty(result) && "OK".equals(result)) {
				UiUtil.showInfoMessage(Messages.PEDIDO_LIBERADO_SUCESSO);
				updateCurrentRecordInList(pedidoOriginal);
				voltarClick();
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_LIBERADO_ERRO, result));
				edit(pedidoOriginal);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		} finally {
			mb.unpop();
		}
	}

	private void liberaPedidoPendenteDesconto(Pedido pedidoOriginal, Pedido pedido) throws SQLException {
		boolean pedidoLiberadoTotal = false;
		double vlPctDescontoLiberacaoRestante = 0;
		double vlPctDescontoLiberado = 0;
		if (!LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() && !LavenderePdaConfig.isUsaMotivoPendencia()) {
			vlPctDescontoLiberacaoRestante = PedidoDescService.getInstance().getVlPctDescontoLiberacaoRestante(pedido);
			vlPctDescontoLiberado = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(false, false);
			if (LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
				double vlPctDescontoLiberadoMediaPonderada = UsuarioDescService.getInstance().getMaxVlPctDescontoPonderadoLiberado(pedido);
				if (vlPctDescontoLiberadoMediaPonderada < vlPctDescontoLiberado) {
					vlPctDescontoLiberado = vlPctDescontoLiberadoMediaPonderada;
				}
				if (vlPctDescontoLiberado > vlPctDescontoLiberacaoRestante) {
					vlPctDescontoLiberado = vlPctDescontoLiberacaoRestante;
					pedidoLiberadoTotal = true;
				}
			}
			}
		double vlTotalPedidoLiberadoAtual = PedidoDescService.getInstance().getVlTotalPedidoLiberadoAtual(pedido);
		double vlDescontoLiberado = ValueUtil.round((vlTotalPedidoLiberadoAtual * vlPctDescontoLiberado) / 100);
		if (ValueUtil.valueEquals(vlPctDescontoLiberado, vlPctDescontoLiberacaoRestante)) {
			vlDescontoLiberado = ValueUtil.round(vlTotalPedidoLiberadoAtual - pedido.getVlTotalBrutoItensComDesconto());
		}
		if (vlDescontoLiberado < 0) {
			vlDescontoLiberado = 0d;
			vlPctDescontoLiberado = 0d;
		}
		PedidoDescErpService.getInstance().savePedidoDescErp(pedido, vlPctDescontoLiberado, vlDescontoLiberado, pedido.flOrigemPedido);
		pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		PedidoDescService.getInstance().savePedidoDesc(pedido, vlPctDescontoLiberado, vlDescontoLiberado);
		if (LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
			UsuarioDescService.getInstance().adicionaVlTotalPedidoUsuarioDescPda(pedido);
		}
		//--
		pedidoLiberadoTotal = LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia() ? isPedidoLiberadoTotalmente() : pedidoLiberadoTotal;
		if (pedidoLiberadoTotal) {
			PedidoService.getInstance().updateFlPendente(pedidoOriginal, ValueUtil.VALOR_NAO);
			PedidoService.getInstance().updateFlPendentePedidoRelacionado(pedidoOriginal, ValueUtil.VALOR_NAO);
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() && pedido.isPedidoItemPendente()) {
				UiUtil.showInfoMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_LIBERACAO_TOTAL_ITENS_PENDENTES);
			} else {
				UiUtil.showSucessMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_LIBERACAO_TOTAL);
			}
		} else {
			if (LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia()) {
				UiUtil.showInfoMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_LIBERACAO_PARCIAL);
			} else {
				vlPctDescontoLiberacaoRestante = PedidoDescService.getInstance().getVlPctDescontoLiberacaoRestante(getPedido());
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_LIBERACAO_PARCIAL_DESCONTO, StringUtil.getStringValueToInterface(vlPctDescontoLiberacaoRestante)));
			}
		}
		edit(pedidoOriginal);
		updateCurrentRecordInList(pedidoOriginal);
		sugereEnvioPedidoDescServidor(pedido);
	}

	private void liberaPedidoPorNuOrdemLiberacao(Pedido pedidoOriginal, Pedido pedido) {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_LIBERANDO_PEDIDO);
		mb.popupNonBlocking();
		try {
			pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			String result =  SyncManager.liberaPedidoPendenteForaDeOrdem(pedido);
			if (ValueUtil.isNotEmpty(result) && "OK".equals(result)) {
				PedidoService.getInstance().updateFlPedidoLiberadoOutraOrdem(pedidoOriginal, ValueUtil.VALOR_SIM);
				PedidoService.getInstance().updateFlPendente(pedidoOriginal, ValueUtil.VALOR_NAO);
				UiUtil.showInfoMessage(Messages.PEDIDO_LIBERADO_SUCESSO);
				updateCurrentRecordInList(pedidoOriginal);
				voltarClick();
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_LIBERADO_ERRO, result));
				edit(pedidoOriginal);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		} finally {
			mb.unpop();
		}
	}

	private void btLiberarPedidoPendenteAlcadaClick() {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_LIBERANDO_PEDIDO);
		mb.popupNonBlocking();
		try {
			Pedido pedidoOriginal = getPedido();
			Pedido pedido = (Pedido) pedidoOriginal.clone();
			pedido.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
				savePedidoDesc(pedido);
			}
			String result =  SyncManager.liberaPedidoPendenteForaDeOrdem(pedido);
			if (ValueUtil.isNotEmpty(result) && "OK".equals(result)) {
				btLiberarItensPendentesClick();
				pedidoOriginal.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
				PedidoService.getInstance().updateStatusPedido(pedidoOriginal);
				UiUtil.showSucessMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_LIBERACAO);
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_LIBERADO_ERRO, result));
			}
			edit(pedidoOriginal);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		} finally {
			mb.unpop();
		}
	}

	private boolean isPedidoLiberadoTotalmente() throws SQLException {
		int nuMaxSequencia = PedidoDescErpService.getInstance().getMaxNuSequenciaLiberacao(getPedido());
		if (LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
			return  nuMaxSequencia  == LavenderePdaConfig.getNuOrdemLiberacaoPedidoPendente();
		}
		return !isPedidoPendentePorMotivoPendencia(nuMaxSequencia);
	}

	private boolean isPedidoPendentePorMotivoPendencia(int nuMaxSequencia) throws SQLException {
		if (!LavenderePdaConfig.isUsaMotivoPendencia()) return false;

		Pedido pedido = getPedido();
		return ItemPedidoService.getInstance().restouItensPendenteLiberacao(pedido, nuMaxSequencia);
	}

	private void btCancelarPedidoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (isStatusOrcamentoAtualPreOrcamento(pedido)) {
			cancelaPedidoStatusOrcamento(pedido);
			return;
		}
		if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return;

		if (!prossegueCancelamentoPedido(pedido.isPedidoPendente(), pedido.isPedidoTransmitido())) return;

		pedido.cdMotivoCancelamento = null;
		CadMotivoCancelamentoPedidoWindow cadMotivoCancelamentoPedidoWindow = getNewCadMotivoCancelamentoPedidoWindow();
		if (ValueUtil.isEmpty(cadMotivoCancelamentoPedidoWindow.cdMotivoCancelamento)) return;

		pedido.cdMotivoCancelamento = cadMotivoCancelamentoPedidoWindow.cdMotivoCancelamento;
		pedido.dsJustificativaCancelamento = cadMotivoCancelamentoPedidoWindow.dsJustificativaCancelamento;
		boolean pedidoCancelado = false;
		if (permiteCancelamentoPedido(pedido)) {
			if (LavenderePdaConfig.isUsaCancelamentoNfePedido()) {
				pedidoCancelado = PedidoService.getInstance().cancelaPedidoENfe(pedido, PedidoUiUtil.isPossuiConexao());
			} else {
				pedidoCancelado = PedidoService.getInstance().cancelaPedidoPendenteAprovacao(pedido);
			}
		} else {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_CANCELAR_DATA_INFERIOR_FECHAMENTO);
		}
		if (pedidoCancelado) {
			pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoCancelado;
			if (getBaseCrudListForm()!= null) {
				getBaseCrudListForm().list();
			}
		}
		edit(getPedido());
	}

	private boolean prossegueCancelamentoPedido(boolean pedidoPendenteAprovacao, boolean pedidoTransmitido) {
		if (pedidoPendenteAprovacao || pedidoTransmitido) return true;

		if (!pedidoTransmitido) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_CANCELAR_STATUS_TRANSMITIDO);
			return false;
		}
		return true;
	}

	private CadMotivoCancelamentoPedidoWindow getNewCadMotivoCancelamentoPedidoWindow() throws SQLException {
		CadMotivoCancelamentoPedidoWindow cadMotivoCancelamentoPedidoWindow = new CadMotivoCancelamentoPedidoWindow();
		cadMotivoCancelamentoPedidoWindow.popup();
		return cadMotivoCancelamentoPedidoWindow;
	}

	private boolean permiteCancelamentoPedido(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaConfigFechamentoDiarioVendas()) return true;
		FechamentoDiario ultimoFechamento = FechamentoDiarioService.getInstance().findUltimoFechamentoDiario();
		if (ultimoFechamento == null || pedido.dtFechamento  == null) return true;
		if (pedido.dtFechamento.equals(ultimoFechamento.dtFinalizacao)) {
			if (ValueUtil.isNotEmpty(ultimoFechamento.hrFinalizacao) && ValueUtil.isNotEmpty(pedido.hrFechamento)) {
				String hrFechamento = ultimoFechamento.hrFinalizacao;
				String hrPedido = pedido.hrFechamento;
				hrFechamento = !hrFechamento.contains(":") ? StringUtil.insertStringPos(hrFechamento, ":", 2) : hrFechamento;
				hrPedido = !hrPedido.contains(":") ? StringUtil.insertStringPos(hrPedido, ":", 2) : hrPedido;
				return TimeUtil.getSecondsBetween(hrPedido + ":00", hrFechamento + ":00") > 0;
			}
		} else {
			return pedido.dtFechamento.isAfter(ultimoFechamento.dtFinalizacao);
		}
		return false;
	}

	private void btCancelarConsignacaoClick() throws SQLException {
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(Messages.PEDIDO_MSG_DESBLOQUEIO_CONSIGNACAO);
		Pedido pedido = getPedido();
		senhaForm.setCdCliente(pedido.cdCliente);
		senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_CANCELAMENTO_CONSIGNACAO);
		senhaForm.setVlTotalPedido(pedido.vlTotalPedido);
		if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoCancelado;
			PedidoService.getInstance().cancelaPedido(pedido);
			PedidoConsignacaoService.getInstance().inserePedidoConsignado(pedido.itemPedidoList, PedidoConsignacao.TIPO_REGISTRO_CANCELAMENTO);
			atualizaEstoqueInterno();
			updateCurrentRecordInList(pedido);
			edit(pedido);
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMA_ENVIO_CONSIGNACAO)) {
				enviaPedidosConsignado();
			}
	}
	}

	private void atualizaEstoqueInterno() throws SQLException {
		if (LavenderePdaConfig.atualizarEstoqueInterno) {
			Pedido pedido = getPedido();
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
					ItemPedidoService.getInstance().atualizaEstoqueGrade((ItemPedido)itemPedido.clone(), false, Estoque.FLORIGEMESTOQUE_PDA);
				} else {
					if (itemPedido.pedido.getTipoPedido() == null || !itemPedido.pedido.getTipoPedido().isIgnoraControleEstoque()) {
						itemPedido.oldQtEstoqueConsumido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisicoAtualizaEstoque());
						String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && itemPedido.pedido.isPedidoVenda() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
						EstoqueService.getInstance().updateEstoqueInterno(itemPedido, itemPedido.oldQtEstoqueConsumido, false, flOrigemEstoque);
					}
				}
			}
		}
	}

	private void sugereEnvioPedidoDescServidor(Pedido pedido) {
		if (LavenderePdaConfig.sugereEnvioAutomaticoPedido && UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_PEDIDO_PENDENTE_ENVIAR_LIBERACAO)) {
			LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_ENVIANDO_DADOS);
			mb.popupNonBlocking();
			try {
				PedidoDescService.getInstance().enviaPedidoDescServidor(pedido);
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
			} finally {
				mb.unpop();
			}
		}
	}

	private void sugereEnvioItemLiberacaoServidor(Pedido pedido) {
		if (LavenderePdaConfig.sugereEnvioAutomaticoPedido && UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_PEDIDO_ITENS_PENDENTES_ENVIAR_LIBERACAO)) {
			LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_ENVIANDO_DADOS);
			mb.popupNonBlocking();
			try {
				ItemLiberacaoService.getInstance().enviaItemLiberacaoServidor(pedido);
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
			} finally {
				mb.unpop();
			}
		}
	}

	private void btDescontosConcedidosClick() throws SQLException {
		ListDescontosConcedidosWindow listDescontosConcedidosWindow = new ListDescontosConcedidosWindow(getPedido());
		listDescontosConcedidosWindow.popup();
	}

	private void btBonificacaoRelacionadaClick() throws SQLException {
		Pedido pedidoBonificacao = null;
		Pedido pedido = getPedido();
		if (!pedido.isPedidoBonificacao()) {
			Vector pedidoBonificacaoList = PedidoService.getInstance().getPedidoBonificacaoListByPedidoVenda(pedido);
			if (pedidoBonificacaoList.size() > 1) {
				RelPedidoRelacionadoWindow relPedidoRelacionadoWindow = new RelPedidoRelacionadoWindow(pedido, pedidoBonificacaoList);
				relPedidoRelacionadoWindow.popup();
				pedidoBonificacao = relPedidoRelacionadoWindow.pedidoVendaRelacionado;
			} else {
				pedidoBonificacao = ValueUtil.isNotEmpty(pedidoBonificacaoList) && pedidoBonificacaoList.size() > 0 ? (Pedido) pedidoBonificacaoList.items[0] : null;
			}
		} else {
			pedidoBonificacao = PedidoService.getInstance().getPedidoRelBonificacao(pedido);
		}
		if (pedidoBonificacao != null) {
			CadPedidoForm cadPedidoForm = new CadPedidoForm();
			cadPedidoForm.inConsultaVendaRelacionada = true;
			cadPedidoForm.edit(pedidoBonificacao);
			show(cadPedidoForm);
		}
	}

	private void setEnableBgGeraCreditoBonificacaoCondicao() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.getCondicaoPagamento() != null && (pedido.getCondicaoPagamento().vlIndiceCredito >= 1 || pedido.getCondicaoPagamento().vlIndiceCredito <= 0) && pedido.isPedidoAberto()) {
			bgGeraCreditoBonificacaoCondicao.setValue(ValueUtil.VALOR_NAO);
			bgGeraCreditoBonificacaoCondicao.setEnabled(false);
		} else {
			bgGeraCreditoBonificacaoCondicao.setEnabled(isEnabled());
		}
	}

	private void setEnableBgGeraCreditoBonificacaoFrete() {
		bgGeraCreditoBonificacaoFrete.setEnabled(isEnabled());
	}

	private void btRelDescontosClick() throws SQLException {
		RelDescontosWindow relDescontosWindow = new RelDescontosWindow((Pedido) getPedido().clone());
		relDescontosWindow.popup();
	}

	private void btRelLiberacoesSenhaClick() throws SQLException {
		RelLiberacoesSenhaWindow relLiberacoesSenhaWindow = new RelLiberacoesSenhaWindow(getPedido().getCliente().cdCliente);
		relLiberacoesSenhaWindow.popup();
	}

	public void setInfoRentabilidadeVisible(boolean isOculto) throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.isPermiteUtilizarRentabilidade()) {
			btIconeRentabilidade.setVisible(!MargemRentabService.isOcultaPercentualFaixaRentabilidadeSePositiva(pedido));
			lvVlPctRentabilidade.setVisible(!LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoPedido(pedido));
			lbRentabilidade.setVisible(!isOculto);
		} else {
			btIconeRentabilidade.setVisible(false);
			lvVlPctRentabilidade.setVisible(false);
		}
	}

	private void flEnviaEmailValueChange(boolean btFlEnviaEmailValue) throws SQLException {
		flEnviaEmailValueChange(btFlEnviaEmailValue, false);
	}

	private void flEnviaEmailValueChange(boolean btFlEnviaEmailValue, boolean isSetValue) throws SQLException {
		Control control = (Control) hashComponentes.get(Pedido.NMCOLUNA_DSEMAILSDESTINO);
		if (isEnabled() && (LavenderePdaConfig.isPermiteEnviarEmailIgnoraFlEnviaEmail() || LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) && !btFlEnviaEmailValue) {
			btFlEnviaEmailValue = true;
		}
		if (control != null) {
			String dsEmails = null;
			if (btFlEnviaEmailValue && isSetValue) {
				Pedido pedido = getPedido();
				dsEmails = pedido.getHashValuesDinamicos().getString(Pedido.NMCOLUNA_DSEMAILSDESTINO);
				if (ValueUtil.isEmpty(dsEmails)) {
					dsEmails = pedido.getCliente().dsEmail;
					if (ValueUtil.isNotEmpty(dsEmails)) {
						dsEmails = dsEmails + ";";
					}
				}
			} 
			dsEmails = StringUtil.getStringValue(dsEmails);
			if (control instanceof EditMemo) {
				EditMemo emDsEmailsDestino = (EditMemo) hashComponentes.get(Pedido.NMCOLUNA_DSEMAILSDESTINO);
				emDsEmailsDestino.setEnabled(btFlEnviaEmailValue);
				if (isSetValue) {
					emDsEmailsDestino.setValue(dsEmails);
					getPedido().getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSEMAILSDESTINO, dsEmails);
				}
			} else if (control instanceof EditText) {
				EditText edDsEmailsDestino = (EditText) hashComponentes.get(Pedido.NMCOLUNA_DSEMAILSDESTINO);
				edDsEmailsDestino.setEnabled(btFlEnviaEmailValue);
				if (isSetValue) {
					edDsEmailsDestino.setValue(dsEmails);
					getPedido().getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSEMAILSDESTINO, dsEmails);
				}
			}
		}
	}
	
	private void edDsEmailDestinoChange() throws SQLException {
		String dsEmailDestinoValues = getDsEmailDestinoValido();
		getPedido().getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSEMAILSDESTINO, dsEmailDestinoValues);
	}

	private String getDsEmailDestinoValido() {
		TextControl control = (TextControl) hashComponentes.get(Pedido.NMCOLUNA_DSEMAILSDESTINO);
		String values = control.getText();
		if (ValueUtil.isNotEmpty(values)) {
			values = values.replaceAll(",", ";");
			List<String> valuesList = Arrays.asList(values.split(";"));
			StringBuffer erro = new StringBuffer();
			for (String value : valuesList) {
				if (!EmailUtil.validateEmail(value)) {
					erro.append(", ");
					erro.append(value);
				}
			}
			if (ValueUtil.isNotEmpty(erro.toString())) {
				erro.delete(0, 2);
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_EMAILS_INVALIDO, new String[]{ erro.toString() }));
			}
		} else if (LavenderePdaConfig.isPermiteEnviarEmailAlternativoComFlEnviaEmail()) {
			throw new ValidationException(Messages.PEDIDO_MSG_EMAILS_INVALIDO_VAZIO);
		}
		return control.getText();
	}

	private void loadFlEnviaEmailDefault(boolean isAlterandoTipoPedido) throws SQLException {
		Pedido pedido = getPedido();
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && LavenderePdaConfig.naoSugereEnvioOrcamento()) {
			pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_SIM);
		} else {
			ButtonGroupBoolean flEnviaEmail = getButtonGroupFlEnviaEmail();
			if (flEnviaEmail != null && !isAlterandoTipoPedido) {
				if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, LavenderePdaConfig.usaValorPadraoSimFlEnviaEmail) || ValueUtil.valueEquals(OrigemPedido.FLORIGEMPEDIDO_PDA, LavenderePdaConfig.usaValorPadraoSimFlEnviaEmail)) {
					flEnviaEmail.setValue(ValueUtil.VALOR_SIM);
					pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_SIM);
				} else {
					flEnviaEmail.setValue(ValueUtil.VALOR_NAO);
					pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_NAO);
				}
			}
		}
	}

	private void habilitaDesabilitaCamposEnvioEmail() throws SQLException {
		Pedido pedido = getPedido();
		if (!PedidoService.getInstance().isEnviaEmailByTipoPedido(pedido)) {
			if (LavenderePdaConfig.isEnviarEmailPedidoAutoCliente()) {
				ButtonGroupBoolean flEnviaEmail = getButtonGroupFlEnviaEmail();
				if (flEnviaEmail != null) {
					flEnviaEmail.setValue(ValueUtil.VALOR_NAO);
					flEnviaEmail.setEnabled(false);
					pedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_FLENVIAEMAIL, ValueUtil.VALOR_NAO);
				}
			}
			if (LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento()) {
				flEnviaEmailValueChange(false, true);
			}
			if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido()) {
				flEnviaEmailValueChange(false, true);
			}
		} else {
			if (LavenderePdaConfig.isEnviarEmailPedidoAutoCliente()) {
				ButtonGroupBoolean flEnviaEmail = getButtonGroupFlEnviaEmail();
				if (flEnviaEmail != null) {
					flEnviaEmail.setEnabled(true);
				}
			}
			if (LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido()) {
				ButtonGroupBoolean flEnviaEmail = getButtonGroupFlEnviaEmail();
				flEnviaEmailValueChange(flEnviaEmail != null ? flEnviaEmail.getValueBoolean() && isEnabled() : false, true);
			}
		}
	}

	private void btCotasCondPagtoClick() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.getCondicaoPagamento().vlIndiceFinanceiro > 1) {
			new RelCotaCondPagto(pedido).popup();
			return;
		}
		UiUtil.showWarnMessage(Messages.MSG_COND_PAGTO_NAO_PERMITE_COTA);
	}

	private void btProdutoDesejadoClick() throws SQLException {
		show(new ListProdutoDesejadoForm(getPedido()));
	}

	private void btAlteraTransportadoraClick() throws SQLException {
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			exibirPopupFrete();
		} else {
			chamarListTransportadoraCepWindow();
		}
		if (LavenderePdaConfig.mostraValorParcelaPedido) {
			lvVlValorParcelaPedido.setValue(PedidoService.getInstance().getValorParcela(getPedido()));
		}
	}

	private void chamarListTransportadoraCepWindow() throws SQLException {
		ListTransportadoraCepWindow listTransportadoraCepWindow = new ListTransportadoraCepWindow(this.getPedido());
		listTransportadoraCepWindow.popup();
		this.atualizaFrete(this.getPedido(), false);

		Container tabPanel = tabDinamica.getContainer(TABPANEL_FRETE);
		tabPanel.removeAll();
		tabPanel.clear();
		montaTabPanelFrete();
		tabPanel.repaintNow();
	}

	private void updateAndMostraMsgCota() throws SQLException {
		Pedido pedido = getPedido();
		if (pedido.isFlCotaCondPagto()) {
			updateVlTotalPedido();
			UiUtil.showConfirmMessage(MessageUtil.getMessage(Messages.MSG_DESC_ATINGIU_COTA, pedido.vlDesconto));
		}
	}

	public void ajustaVlPctMaxDescAcrescItemPedido(TipoPedido tipoPedido, boolean usaDesconto, boolean usaAcrescimo, ItemPedido itemPedido) throws SQLException {
		if (usaDesconto && itemPedido.vlPctDesconto > 0) {
			double vlPctMaxDesconto = tipoPedido.isVlPctMaxDescontoValido() ? tipoPedido.vlPctMaxDesconto : itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			if (vlPctMaxDesconto < itemPedido.vlPctDesconto) {
				itemPedido.vlPctDesconto = vlPctMaxDesconto;
			}
		} else if (usaAcrescimo && itemPedido.vlPctAcrescimo > 0) {
			double vlPctMaxAcrescimo = tipoPedido.isVlPctMaxAcrescimoValido() ? tipoPedido.vlPctMaxAcrescimo : itemPedido.getItemTabelaPreco().vlPctMaxAcrescimo;
			if (vlPctMaxAcrescimo < itemPedido.vlPctAcrescimo) {
				itemPedido.vlPctAcrescimo = vlPctMaxAcrescimo;
			}
		}
	}

	private void setEnabledPopupSearchCliEntrega() throws SQLException {
		boolean isEnabled = getPedido().getTipoPedido().isIndicaClienteEntrega() && isEnabled();
		PopUpSearchFilterDyn popUpClienteEntrega = PedidoUiUtil.getPopUpSearchClienteEntrega(hashComponentes);
		if (popUpClienteEntrega != null) {
			popUpClienteEntrega.setEnabled(isEnabled);
			if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
				cbEnderecoEntrega.indicaClienteEntrega = isEnabled;
			}
		}
	}

	private void popUpSearchCliEntregaChange() throws SQLException {
		PopUpSearchFilterDyn popUpClienteEntrega = PedidoUiUtil.getPopUpSearchClienteEntrega(hashComponentes);
		Pedido pedido = getPedido();
		if (popUpClienteEntrega != null) {
			if (ValueUtil.valueEquals(popUpClienteEntrega.getValue(), pedido.cdCliente)) {
				UiUtil.showWarnMessage(Messages.PEDIDO_MSG_CLIENTE_ENTREGA_IGUAL_PEDIDO);
				popUpClienteEntrega.cdBaseDomain = pedido.cdClienteEntrega;
				popUpClienteEntrega.dsBaseDomain = pedido.dsClienteEntrega;
				popUpClienteEntrega.edText.setText(popUpClienteEntrega.dsBaseDomain);
				popUpClienteEntrega.btFiltrar.postPressedEvent();
			}
			if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
				cbEnderecoEntrega.cdClienteEntrega = popUpClienteEntrega.getValue();
			}
			reloadCliEnderecoComboOnPedidoChange(pedido, PedidoService.getInstance().obrigaIndicarClienteEntrega(pedido));
			houveAlteracaoCampos = true;
		}
	}

	private void reloadCliEnderecoComboOnPedidoChange(Pedido pedido, boolean indicaClienteEntrega) throws SQLException {
		boolean indicaEnderecoEntrega = LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0;
		PopUpSearchFilterDyn popUpClienteEntrega = PedidoUiUtil.getPopUpSearchClienteEntrega(hashComponentes);
		if (popUpClienteEntrega != null) {
			if (!indicaClienteEntrega) {
				pedido.cdClienteEntrega = null;
				if (indicaEnderecoEntrega) {
					cbEnderecoEntrega.cdClienteEntrega = null;
				}
				popUpClienteEntrega.edText.clear();
				popUpClienteEntrega.cdBaseDomain = ValueUtil.VALOR_NI;
				popUpClienteEntrega.dsBaseDomain = ValueUtil.VALOR_NI;
			} else {
				pedido.cdClienteEntrega = popUpClienteEntrega.cdBaseDomain;
				pedido.dsClienteEntrega = popUpClienteEntrega.dsBaseDomain;
			}
			if (indicaEnderecoEntrega) {
				cbEnderecoEntrega.indicaClienteEntrega = indicaClienteEntrega;
				cbEnderecoEntrega.carregaClienteEnderecoPadrao(pedido, false);
				cbEnderecoEntrega.reloadListEnderecoWindow();
				pedido.cdEnderecoCliente = cbEnderecoEntrega.getValue();
			}
		}
	}

	private void cleanInfosBancariasOnCondPagtoChange(Pedido pedido) throws SQLException {
		boolean enabled = isEnabled() && ValueUtil.VALOR_SIM.equals(pedido.getCondicaoPagamento().flInformaDados);
		cbBoletoConfig.setEditable(enabled);
		edNuAgencia.setEditable(enabled);
		edNuConta.setEditable(enabled);
		if (!enabled && ValueUtil.isNotEmpty(pedido.nuPedido)) {
			PagamentoPedidoService.getInstance().deleteByPedido(pedido);
			pedido.setPagamentoPedidoClear();
			cbBoletoConfig.setSelectedIndex(-1);
			edNuAgencia.setText("");
			edNuConta.setText("");
		}
	}

	private void validaObrigaSelecaoUnidadeAlternativaPedido() throws SQLException {
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() && ValueUtil.isEmpty(getPedido().cdUnidade)) {
			throw new ValidationException(Messages.PEDIDO_UNIDADE_NAO_SELECIONADA);
		}
	}

	private void edVlManualFreteValueChange() throws SQLException {
		double vlFrete = 0d;
		Pedido pedido = getPedido();
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if(pedido.vlTotalItens == 0) {
			edVlManualFrete.setValue(vlFrete);
		} else if (LavenderePdaConfig.isPermiteInserirFreteManualEUsaTipoFrete()) {
			if(LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
				vlFrete = (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) ? lvVlTotalFreteItensPedido.getDoubleValue() : 0d;
			}
			else {
				vlFrete = (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) ? edVlManualFrete.getValueDouble() : 0d;
			}
			lbVlTotalPedidoMaisFrete.setValue(pedido.vlTotalPedido + vlFrete);
			if (LavenderePdaConfig.apresentaValorTotalPedidoComTributosEFrete) {
				lvVlBrutoPedidoMaisFrete.setValue(TributosService.getInstance().getVlTotalTributoEDeducoesComFrete(pedido));
			}
		}

		double vlManualFrete = (tipoPedido != null && !tipoPedido.isIgnoraCalculoFrete()) ? edVlManualFrete.getValueDouble() : 0d;

		if (LavenderePdaConfig.isPermiteInserirFreteManualENaoUsaTipoFrete()) {
			pedido.vlTotalPedido -= pedido.vlFrete;
			pedido.vlFrete = vlManualFrete;
			pedido.vlTotalPedido = pedido.vlTotalPedido + pedido.vlFrete;
		}
		pedido.vlFrete = vlManualFrete;

		PedidoService.getInstance().divideFreteManualNosItens(pedido, true);

		updateVlTotalPedido();
	}

	public void edVlFreteAdicionalValueChange() throws SQLException {
		Pedido pedido = getPedido();
		double vlFreteAdicional = edVlFreteAdicional.getValueDouble();
		pedido.vlTotalPedido -= pedido.vlFreteAdicional;
		pedido.vlFreteAdicional = vlFreteAdicional;
		pedido.vlTotalPedido = pedido.vlTotalPedido + pedido.vlFreteAdicional;
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido() && isEditing()) {
			PedidoService.getInstance().updateVlTotalPedidoFreteAdicional(pedido);
		}
		updateVlTotalPedido();
	}

	private void btIgnoraValidacaoClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.MSG_IGNORAR_VALIDACOES)) {
			Pedido pedido = getPedido();
			PedidoService.getInstance().updateStatusPedidoIgnoraValidacao(pedido);
			LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, pedido.getRowKey(), pedido.cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_A, StringUtil.getStringValue(pedido.vlTotalPedido));
			bmOpcoes.removeItem(Messages.BOTAO_IGNORAR_VALIDACOES);
			changeEntregaPedido(pedido);
			reloadComboCondicaoPagamento();
		}
	}

	private void cbEntregaClick() throws SQLException {
		Entrega entrega = (Entrega) cbEntrega.getSelectedItem();
		boolean qtInt = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro();
		if (entrega != null) {
			Pedido pedido = getPedido();
			if (pedido.vlTotalPedido < entrega.vlMinPedido) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.ENTREGA_MSG_AVISO_VALOR, new Object[] {StringUtil.getStringValueToInterface(entrega.vlMinPedido), StringUtil.getStringValueToInterface(pedido.vlTotalPedido)}));
			} else if (pedido.getQtItensLista() < entrega.qtMinProduto) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.ENTREGA_MSG_AVISO_QTD, new Object[] {StringUtil.getStringValueToInterface(entrega.qtMinProduto, qtInt ? 0 : ValueUtil.doublePrecisionInterface), StringUtil.getStringValueToInterface(pedido.getQtItensLista(), qtInt ? 0 : ValueUtil.doublePrecisionInterface)}));
			}
			if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
				edDtEntregaManual.setText(ValueUtil.VALOR_NI);
				pedido.dtEntrega = null;
			}
		}
	}

	private void btImprimirPromissoriaClick() throws SQLException {
		Pedido pedido = getPedido();
		PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
		LoadingBoxWindow lb = UiUtil.createProcessingMessage();
		try {
			if (pedido.isPromissoriaImpressa()) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAONOTAPROMISSORIA_IMPRIMIR_NOVAMENTE)) {
					return;
				}
			}
			lb.makeUnmovable();
			lb.popupNonBlocking();
			pedidoServicePrint.imprimeNotaPromissoria();
			PedidoService.getInstance().updateFlImpressoPromissoria(pedido);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONOTAPROMISSORIA_ERRO_IMPRESSAO, e.getMessage()));
		} finally {
			lb.unpop();
		}
	}

	private void bonificacaoSaldoClick() throws SQLException {
		RelBonificacaoSaldoWindow relBonificacaoSaldoWindow = new RelBonificacaoSaldoWindow();
		relBonificacaoSaldoWindow.popup();
	}

	protected void btGiroProdutoClick() throws SQLException {
		Pedido pedido = getPedido();
		int qtde = GiroProdutoService.getInstance().findCountProdutosGiroProduto(pedido.getCliente());
		if(qtde > 0) {
			boolean sugestaoPedidoGiroProduto = pedido.isSugestaoPedidoGiroProduto();
			try {
				solicitadoAcessoGiroProduto = true;
				pedido.setSugestaoPedidoGiroProduto(true);
				this.inItemNegotiationGiroProdutoPendente = true;
				btNovoItemClick(true);
			} finally {
				pedido.setSugestaoPedidoGiroProduto(sugestaoPedidoGiroProduto);
			}
		} else {
			UiUtil.showWarnMessage(Messages.PEDIDO_NOVO_PEDIDOGIRO_SUGESTAO_NAO_ENCONTRADA);
		}
	}

	private void edDescCascataCategoria1Change() throws SQLException {
		Pedido pedido = getPedido();
		double pctDescCli = ValueUtil.round((1 - pedido.getCliente().vlIndiceFinanceiro) * 100);
		double pctDescAtual = pedido.vlPctDescCliente;
		double pctDescCat1 = edDescCascataCategoria1.getValueDouble();
		if (pctDescAtual == pctDescCat1) {
			edDescCascataCategoria1.setValue(pctDescAtual);
			return;
		}
		if ((pctDescCat1 > pctDescCli && pctDescCli > 0) || pctDescCat1 >= 100) {
			pctDescCli = pctDescCli >= 100 ? 99.99 : pctDescCli;
			edDescCascataCategoria1.setValue(pctDescAtual);
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_DESC_CLI, pctDescCli));
		} else {
			edDescCascataCategoria1.setValue(pctDescCat1);
			pedido.vlPctDescCliente = pctDescCat1;
			if (pedido.vlTotalPedido > 0) {
				try {
					getPedidoService().update(pedido);
				} catch (DescontoCategoriaException e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
				updateVlTotalPedido();
				refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
			}
		}
	}

	private void edDescCascataCategoria2Change() throws SQLException {
		Pedido pedido = getPedido();
		double pctDescAtual = pedido.vlPctDesc2;
		double pctDescEspecial = pedido.getCliente().getCategoria().vlPctDescEspecial;
		double pctDescCat2 = edDescCascataCategoria2.getValueDouble();
		if (pctDescAtual == pctDescCat2) {
			edDescCascataCategoria2.setValue(pctDescAtual);
			return;
		}
		if (pctDescCat2 > 0 && edDescCascataCategoria1.isEditable() && edDescCascataCategoria1.getValueDouble() == 0) {
			edDescCascataCategoria2.setValue(pctDescAtual);
			throw new ValidationException(Messages.PEDIDO_ERRO_DESC_CATEGORIA_SEQUENCIA);
		}
		if (pctDescCat2 > pctDescEspecial || pctDescCat2 >= 100) {
			pctDescEspecial = pctDescEspecial >= 100 ? 99.99 : pctDescEspecial;
			edDescCascataCategoria2.setValue(pctDescAtual);
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_DESC_ESPECIAL, pctDescEspecial));
		} else {
			edDescCascataCategoria2.setValue(pctDescCat2);
			pedido.vlPctDesc2 = pctDescCat2;
			if (pedido.vlTotalPedido > 0) {
				try {
				getPedidoService().update(pedido);
				} catch (DescontoCategoriaException e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
				updateVlTotalPedido();
				refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
			}
		}
	}

	private void edDescCascataCategoria3Change() throws SQLException {
		Pedido pedido = getPedido();
		double pctDescAtual = pedido.vlPctDesc3;
		double pctDescAtacado = pedido.getCliente().getCategoria().vlPctDescAtacado;
		double pctDescCat3 = edDescCascataCategoria3.getValueDouble();
		if (pctDescAtual == pctDescCat3) {
			edDescCascataCategoria3.setValue(pctDescAtual);
			return;
		}
		if (pctDescCat3 > 0 && ((edDescCascataCategoria1.isEditable() && edDescCascataCategoria1.getValueDouble() == 0) || (edDescCascataCategoria2.isEditable() && edDescCascataCategoria2.getValueDouble() == 0))) {
			edDescCascataCategoria3.setValue(pctDescAtual);
			throw new ValidationException(Messages.PEDIDO_ERRO_DESC_CATEGORIA_SEQUENCIA);
		}
		if (pctDescCat3 > pctDescAtacado || pctDescCat3 >= 100) {
			pctDescAtacado = pctDescCat3 >= 100 ? 99.99 : pctDescAtacado;
			edDescCascataCategoria3.setValue(pctDescAtual);
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_ERRO_DESC_ATACADO, pctDescAtacado));
		} else {
			edDescCascataCategoria3.setValue(pctDescCat3);
			pedido.vlPctDesc3 = pctDescCat3;
			if (pedido.vlTotalPedido > 0) {
				try {
					getPedidoService().update(pedido);
				} catch (DescontoCategoriaException e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
				updateVlTotalPedido();
				refreshCamposDescCascata(pedido, pedido.getCliente().getCategoria(), pedido.getTabelaPreco() == null ? isEnabled() : pedido.getTabelaPreco().isPermiteDesconto() && isEnabled());
			}
		}
	}

	private void edNuParcelaPedidoChange() throws SQLException {
		Pedido pedido = getPedido();
		pedido.nuParcelas = edNuParcelaPedido.getValueInt();
		try {
			PedidoService.getInstance().validaNumeroParcelasCondicaoPagamento(pedido);
		} catch (Throwable ex) {
			edNuParcelaPedido.setValue(pedido.getCondicaoPagamento().nuParcelas);
			throw ex;
		}
	}

	public void updateDataEntrega(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.calculaPrazoEntregaPorProduto) {
			return;
		}
		Date dtEntrega = PedidoService.getInstance().getDataPrevisaoEntrega(pedido, pedido.getCliente());
		edDtEntrega.setValue(dtEntrega);
		pedido.dtEntrega = dtEntrega;
		PedidoService.getInstance().updateDataEntregaPedido(pedido.getRowKey(), dtEntrega);
	}

	private void cbStatusOrcamentoClick() throws SQLException {
		StatusOrcamento status = cbStatusOrcamento.getStatusOrcamento();
		Pedido pedido = getPedido();
		String oldCdStatusOrcamento = pedido.cdStatusOrcamento;
		if (status != null && status.isStatusCancelamento()) {
			onChangeToStatusOrcamentoCancelado(status, pedido, oldCdStatusOrcamento);
		} else if (status != null) {
			pedido.cdStatusOrcamento = status.cdStatusOrcamento;
			pedido.statusOrcamento = status;
		}
		afterStatusOrcamentoPedidoChange(pedido);
	}

	public void onChangeToStatusOrcamentoCancelado(StatusOrcamento status, Pedido pedido, String oldCdStatusOrcamento) throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.STATUSORCAMENTO_CANCELAR_ORCAMENTO)) {
			try {
				UiUtil.showProcessingMessage();
				pedido.statusOrcamento = status;
				pedido.cdStatusOrcamento = status.cdStatusOrcamento;
				cbStatusOrcamento.setValue(pedido.cdStatusOrcamento);
				save();
				PedidoService.getInstance().cancelaOrcamento(pedido);
				edit(pedido);
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		} else {
			cbStatusOrcamento.setValue(oldCdStatusOrcamento);
		}
	}
	
	protected void afterStatusOrcamentoPedidoChange(Pedido pedido) {
		setBtLibParcelaSolAutorizacaoVisible(pedido);
		setCbCondicaoComercialEnabled(pedido);
	}

	private void geraPdfOffline() throws SQLException {
		if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto()){
			if (ValueUtil.isEmpty(getPedido().cdCentroCusto)) {
				UiUtil.showInfoMessage(Messages.MSG_PEDIDO_NAO_POSSUI_CENTRO_CUSTO);
				return;
			}
		}
		geraPdfOffline(false, false);
	}

	private void geraPdfOffline(boolean clickMenuInferior, boolean isGeradoAuto) throws SQLException {
		TYPE_MESSAGE typeMsg = clickMenuInferior ? TYPE_MESSAGE.ERROR : TYPE_MESSAGE.WARN;
		ResourcesWmw resourcesWmw = ResourcesWmwService.getInstance().getResourcesWmwRelatorioPdf();
		if (resourcesWmw == null) {
			UiUtil.showWarnMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_NAO_ENCONTRADO);
			return;
		}
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			PdfReportManager geradorPdf = new PdfReportManager();
			getPedido().isGeraPdfOfflineAuto = isGeradoAuto;
			geradorPdf.interpretaLayoutPedido(getPedido(), resourcesWmw.baConteudo);
			if (!isGeradoAuto) {
				aposGerarPdfOffline(geradorPdf.isArquivoGerado(), geradorPdf.getFilePath(), geradorPdf.getMsgErro(),typeMsg);
			}
		} catch (ValidationException e) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + e.getMessage(), typeMsg);
		} catch (SyntaxException e) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + Messages.RELATORIO_PDF_OFFLINE_ERRO_DEFINICAO_INCORRETA, typeMsg);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + Messages.RELATORIO_PDF_OFFLINE_ERRO_SINTAXE_INVALIDA_SQL, typeMsg);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO, typeMsg);
		} finally {
			mb.unpop();
		}
	}


	private void aposGerarPdfOffline(boolean arquivoGerado, String filePath, String msgErro, TYPE_MESSAGE typeMsg) throws SQLException {
		if (!arquivoGerado) {
			UiUtil.showMessage(Messages.RELATORIO_PDF_OFFLINE_ERRO_NAO_GERADO + StringUtil.getStringValue(msgErro), typeMsg);
			return;
		}
		if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
			abrePdfOffline(filePath);
		} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_OFFLINE_GERADO_SUCESSO)) {
			abrePdfOffline(filePath);
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.RELATORIO_PDF_OFFLINE_LOCAL_PDF, filePath));
		}
		LogAppService.getInstance().logPedido(LogApp.FL_TIPO_LOG_INFO, getPedido().getRowKey(), getPedido().cdCliente, LogApp.DS_DETALHES_EMISSAO_PEDIDO_POFF, StringUtil.getStringValue(getPedido().vlTotalPedido));
	}

	private boolean sugereItemComboAntesFechamento(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaSugestaoComboFechamentoPedido() && PedidoService.getInstance().existeItensSugestaoComboNoPedido(pedido)) {
			Vector domainList = ItemComboService.getInstance().findProdutosSugeridosByCombo(pedido, null, null, null, true);
			if (domainList.size() > 0) {
				CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(this, pedido);
				ItemCombo itemCombo = ItemComboService.getInstance().getItemComboComCdProduto(pedido.cdEmpresa, pedido.cdRepresentante, null);
				ListItemComboSugestaoWindow itemComboSugestaoWindow = new ListItemComboSugestaoWindow(pedido, itemCombo, cadItemPedidoForm, domainList, true);
				itemComboSugestaoWindow.popup();
				fechaPedidoSugestaoCombo = itemComboSugestaoWindow.fecharPedidoPressed;
				return itemComboSugestaoWindow.fecharPedidoPressed;
			} else {
				fechaPedidoSugestaoCombo = true;
				return false;
			}
		}
		return true;
	}
	private void setDtEntregaManualVisible(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			TabelaPreco tabPreco = pedido.getTabelaPreco();
			if ((tabPreco != null && tabPreco.isPermiteIndicarDataEntregaManual()) ^ edDtEntregaManual.isDisplayed()) {
				remontaTela();
				reposition();
				setVisibleCondicaoPagamento();
			}
		}
	}

	private void edDtPagamentoChange() throws SQLException {
		Date dtPagamento = edDtPagamento.getValue();
		Pedido pedido = getPedido();
		if (ValueUtil.isNotEmpty(dtPagamento)) {
			if (dtPagamento.isBefore(DateUtil.getCurrentDate())) {
				UiUtil.showErrorMessage(Messages.PEDIDO_ERRO_DTPAGAMENTO_MENOR_HOJE);
				edDtPagamento.setValue(pedido.dtPagamento);
			} else {
				pedido.dtPagamento = edDtPagamento.getValue();
				if (pedido.itemPedidoList.size() > 0) {
					LoadingBoxWindow mb = UiUtil.createProcessingMessage();
					mb.popupNonBlocking();
					Vector itemErroList = new Vector();
					int size = pedido.itemPedidoList.size();
					for (int i = 0; i < size; i++) {
						ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
						try {
							PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
							PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
						} catch (Throwable e) {
							ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, e.getMessage());
							itemErroList.addElement(produtoErro);
						}
					}
					try {
						if (itemErroList.size() == 0) {
							getPedidoService().updateItensPedidoAfterChanges(pedido);
							getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
							updateVlTotalPedido();
						} else {
							edit(getPedidoService().findByRowKeyDyn(pedido.getRowKey()));
							RelNotificacaoItemWindow relInsercaoItensPedidoForm = new RelNotificacaoItemWindow(Messages.ITEMPEDIDO_LABEL_PROBLEMAS_OCORRIDOS, itemErroList, false);
							relInsercaoItensPedidoForm.popup();
						}
					} finally {
						mb.unpop();
					}
				}
			}
		}
	}

	private void dtEntregaManualChange() throws SQLException {
		Pedido pedido = getPedido();
		try {
			Date dtEntregaManual = edDtEntregaManual.getValue();
			if (dtEntregaManual != null) {
				if (DateUtil.isAfterOrEquals(dtEntregaManual, DateUtil.getCurrentDate())) {
					cbEntrega.setSelectedIndex(-1);
					pedido.dtEntrega = dtEntregaManual;
				} else {
					UiUtil.showErrorMessage(Messages.PEDIDO_ERRO_DTENTREGA_MANUAL);
					edDtEntregaManual.setValue(pedido.dtEntrega);
				}
			}
		} catch (ValidationException e) {
			UiUtil.showErrorMessage(e);
			edDtEntregaManual.setValue(pedido.dtEntrega);
		}
	}

	public TotalizadoresPedidoForm getTotalizadoresPedidoForm() {
		return totalizadoresPedidoForm;
	}

	public void setTotalizadoresPedidoForm(TotalizadoresPedidoForm totalizadoresPedidoForm) {
		this.totalizadoresPedidoForm = totalizadoresPedidoForm;
	}

	private boolean validadeFechamentoPedidoOnSave() throws SQLException {
		boolean validationOK = true;
		if (naoConsisteValidacaoFechamentoAoSalvar) {
			return validationOK;
		}
		Pedido pedidoAtualizado = (Pedido) screenToDomain();
		try {
			PedidoService.getInstance().validateFecharPedido(pedidoAtualizado, false);
		} catch (RelProdutosPendentesPedidoException e) {
			RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(getPedido(), true, true);
			relProdutosPendentesWindow.cadPedidoForm = this;
			if (relProdutosPendentesWindow.hasProdutosPendentes()) {
				relProdutosPendentesWindow.popup();
				validationOK = relProdutosPendentesWindow.continua;
			} else {
				RelProdutosPendentesWindow.cleanInstance();
			}

			if (validationOK) {
				validationOK = validadeFechamentoPedidoOnSave();
			}
		} catch (ValidationException e) {
			StringBuffer strBuffer = new StringBuffer();
			if (e.getMessage().startsWith(DescComiFaixa.SIGLE_EXCEPTION)) {
				int result = UiUtil.showMessage(strBuffer.append(e.getMessage().substring(3)).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString(),
								TYPE_MESSAGE.WARN, new String[] { FrameworkMessages.VALOR_NAO, Messages.BOTAO_VER_ITENS, FrameworkMessages.VALOR_SIM });
				if (result == 0) {
					validationOK = false;
				} else if (result == 1) {
					PedidoUiUtil.showListItensDivergentesDescComissaoGrupo(this);
					validationOK = false;
				}
			} else if (e.getMessage().startsWith(DescontoGrupo.SIGLE_EXCEPTION)) {
				int result = UiUtil.showMessage(MessageUtil.quebraLinhas(strBuffer.append(e.getMessage().substring(3)).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString()),
								TYPE_MESSAGE.WARN, new String[] { FrameworkMessages.VALOR_NAO, Messages.BOTAO_VER_ITENS, FrameworkMessages.VALOR_SIM });
				if (result == 0) {
					validationOK = false;
				} else if (result == 1) {
					PedidoUiUtil.showListItensDivergentesDescQtdGrupo(this);
					validationOK = false;
				}
			} else if (e.getMessage().startsWith(DescontoPacote.SIGLE_EXCEPTION)) {
				int result = UiUtil.showMessage(MessageUtil.quebraLinhas(strBuffer.append(e.getMessage().substring(3)).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString()),
						TYPE_MESSAGE.WARN, new String[] { FrameworkMessages.VALOR_NAO, Messages.BOTAO_VER_ITENS, FrameworkMessages.VALOR_SIM });
				if (result == 0) {
					validationOK = false;
				} else if (result == 1) {
					PedidoUiUtil.showListItensDivergentesDescPacote(this);
					validationOK = false;
				}
			} else if (e.getMessage().startsWith("PVL")) {
				int result = UiUtil.showMessage(MessageUtil.quebraLinhas(strBuffer.append(e.getMessage().substring(3)).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString()),
						TYPE_MESSAGE.WARN, new String[]{FrameworkMessages.VALOR_NAO, Messages.BOTAO_VER_ITENS, FrameworkMessages.VALOR_SIM});
				if (result == 0) {
					validationOK = false;
				} else if (result == 1) {
					PedidoUiUtil.showListItensDivergentesCalculoVinco(this);
					validationOK = false;
				}
			} else {
				int result;
				if (ValueUtil.isEmpty(e.params)) {
					result = UiUtil.showConfirmYesCancelMessage(strBuffer.append(e.getMessage()).append(" ").append(Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString());
				} else {
					result = UiUtil.showConfirmYesCancelMessage(strBuffer.append(e.getMessage()).append(e.params).append(". ").append(Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString());
				}
				if (result == 0) {
					validationOK = false;
					PedidoService.validationFechamentoCount = 0;
				} else {
					validationOK = validadeFechamentoPedidoOnSave();
				}
			}
		} catch (RelacionaPedProducaoException e) {
			ExceptionUtil.handle(e);
			validationOK = false;
		} catch (PedidoBonificacaoComVerbaGrupoSaldoNaoLiberadoPorSenhaException e) {
			ExceptionUtil.handle(e);
			validationOK = false;
		}
		if (!validationOK) {
			PedidoService.validationFechamentoCount = 0;
		}
		return validationOK;
	}

	public void atualizaFrete(Pedido pedido, boolean calculaFrete) throws SQLException {
		if (isAtualizaFrete(pedido) || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && pedido.vlFrete > 0 && calculaFrete) {
				PedidoService.getInstance().calculaFreteTransportadoraCep(pedido);
			}
			if (pedido.cdTipoFrete != null) {
				lbVlTotalPedidoFrete.setValue(pedido.vlTotalPedido);
				lbVlTotalPedido.setValue(pedido.vlTotalPedido);
				String nmTransportadora = pedido.getTransportadora() != null ? pedido.getTransportadora().toString() : Messages.TRANSPORTADORACEP_LABEL_NOME_TRANSPORTADORA_DEFAULT;
				String dsTipoFrete = TipoFreteService.getInstance().getTipoFrete(pedido.cdTipoFrete, pedido.getCliente().dsUfPreco).toString();
				double vlFrete = LavenderePdaConfig.escolhaTransportadoraPedidoPorCep() ? pedido.vlFreteTotal : pedido.vlFrete;
				lbVlFrete.setValue(vlFrete);
				lbNmTransportadora.setValue(nmTransportadora);
				lbNmTipoFrete.setValue(dsTipoFrete);
				populateCamposFreteCapaPedidoEscolhaTransp(nmTransportadora, dsTipoFrete, vlFrete);
			}
			if (LavenderePdaConfig.usaFreteManualPedido){
				pedido.vlFrete = edVlManualFrete.getValueDouble();
				getPedidoService().divideFreteManualNosItens(pedido, false);
			}
			updateVlTotalPedido();
		}
	}

	public void updateUiFretePersonalizado(Pedido pedido) throws SQLException {
		cbTransportadora.setValue(pedido.cdTransportadora);
		cbTranspFretePersonalizado.setValue(pedido.cdTransportadora);
		if (pedido.cdTipoFrete != null) {
			lbNmTipoFrete.setValue(TipoFreteService.getInstance().getTipoFrete(pedido.cdTipoFrete, pedido.getCliente().dsUfPreco).toString());
			cbTipoFrete.setValue(pedido.cdTipoFrete, pedido.getCliente() != null ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO);
			lbNmTransportadora.setValue(pedido.getTransportadora() != null ? pedido.getTransportadora().toString() : Messages.TRANSPORTADORACEP_LABEL_NOME_TRANSPORTADORA_DEFAULT);
		} else {
			lbNmTipoFrete.setValue("");
		}
		lbVlTotalFretePedido.setValue(pedido.vlFrete);
	}

	public boolean isAtualizaFrete(Pedido pedido) {
		boolean transportadoraReg = LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao() && !pedido.isPedidoAberto();
		boolean transportadoraCep = LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep();
		return transportadoraReg || transportadoraCep;
	}

	private void recalculaPedidoClick() throws SQLException {
		if (PedidoUiUtil.validaDataUltimoRecebimentoDados() && UiUtil.showConfirmYesNoMessage(Messages.DESEJA_EFETUAR_RECALCULO_PEDIDO)) {
			Pedido pedido = getPedido();
			double vlTotalPedidoAnterior = pedido.vlTotalPedido;
			PedidoService.getInstance().atualizaPedido(pedido);
			Vector itensErro = new Vector();
			populaErroCalculo(pedido, itensErro);
			PedidoUiUtil.retornaMensagem(pedido, vlTotalPedidoAnterior, itensErro);
			domainToScreen(pedido);
			btRecalcularPedido.setVisible(false);
			btFecharPedido.setVisible(true);
		}
	}

	private void populaErroCalculo(Pedido pedido, Vector itensErro) throws SQLException {
		int size = pedido.itemPedidoList.size();
		ProdutoErro produtoErro;
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
			if (item.isErroRecalculo()) {
				produtoErro = new ProdutoErro(item.getProduto(), null, null);
				itensErro.addElement(produtoErro);
			}
		}
	}

	private void edVlPctVpcChange() throws SQLException {
		double pctVpc = edVlPctVpc.getValueDouble();
		if (pctVpc > 100) {
			edVlPctVpc.setValue(getPedido().vlPctVpc);
			throw new ValidationException(Messages.ERRO_PERCENTUAL_VPC_SUPERIOR_A_CEM);
		} else if (pctVpc < 0 ) {
			edVlPctVpc.setValue(getPedido().vlPctVpc);
			throw new ValidationException(Messages.ERRO_PERCENTUAL_VPC_INFERIOR_A_ZERO);
		}
		getPedido().vlPctVpc = pctVpc;
		calculaVpcParaItens(getPedido().itemPedidoList);
	}

	public void cbCentroCustoChange() throws SQLException {
		if (getPedido().getQtItensLista() > 0 && (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() || LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda())) {
			cbCentroCusto.setValue(getPedido().cdCentroCusto);
			throw new ValidationException(Messages.PEDIDO_MSG_ERRO_ALTERAR_CENTRO_CUSTO);
		}
		getPedido().cdCentroCusto = cbCentroCusto.getValue();
		getPedido().centroCusto = (CentroCusto) cbCentroCusto.getSelectedItem();
		if (LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
			getPedido().cdPlataformaVenda = null;
			cbPlataformaVenda.carregaPlataformas(cbCentroCusto.getValue(), getPedido().getCliente().cdCliente, getPedido().cdRepresentante);
			if (cbPlataformaVenda.getValue() != null) {
				cbPlataformaVendaChange();
			}
		} else if (LavenderePdaConfig.usaPoliticaComercial()) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(getPedido());
		}
	}

	public void cbPlataformaVendaChange() throws SQLException {
		if (getPedido().getQtItensLista() > 0 && LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda()) {
			cbPlataformaVenda.setValue(getPedido().cdPlataformaVenda);
			throw new ValidationException(Messages.PEDIDO_MSG_ERRO_ALTERAR_PLATAFORMA_VENDA);
		}
		getPedido().cdPlataformaVenda = cbPlataformaVenda.getValue();
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(getPedido());
		}
	}

	public void cbModoFaturamentoChange() throws SQLException {
		getPedido().cdModoFaturamento = cbModoFaturamento.getValue();
		if (cbModoFaturamento.getValue() == null) {
			emObservacaoModoFaturamento.setEditable(false);
			emObservacaoModoFaturamento.setText("");
		} else {
			emObservacaoModoFaturamento.setEnabled(true);
			emObservacaoModoFaturamento.setEditable(true);
		}
	}

	public void emObservacaoModoFaturamentoChange() throws SQLException {
		getPedido().dsModoFaturamento = emObservacaoModoFaturamento.getText();
	}

	private boolean isTabInfoComplementarLigada() {
		return LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido ||
				LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais() ||
				LavenderePdaConfig.isUsaClasseValorInformacoesAdicionais() ||
				LavenderePdaConfig.isUsaItemContaInformacoesAdicionais() ||
				LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais();
	}

	private void bgPedidoGondolaClick(boolean updateOtherButton) throws SQLException {
		getPedido().flGondola = bgPedidoGondola.getValue();
		if (updateOtherButton) {
			updateOtherButtonGroupBoolean(bgPedidoGondola);
			if (LavenderePdaConfig.usaValidaConversaoFOB() && getPedido().isTipoFreteFob()) {
				cbTipoFrete.selectTipoFretePadrao();
			}
		}
	}

	private void updateOtherButtonGroupBoolean(ButtonGroupBoolean buttonGroupBoolean) throws SQLException {
		if (bgPedidoCritico != null && buttonGroupBoolean != bgPedidoCritico) {
			bgPedidoCritico.setValueBoolean(false);
			bgPedidoCriticoClick(false);
		}
		if (bgPedidoGondola != null && buttonGroupBoolean != bgPedidoGondola) {
			bgPedidoGondola.setValueBoolean(false);
			bgPedidoGondolaClick(false);
		}
	}

	private void btMenuVerbaClick() throws SQLException {
		if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir && getPedido().isPedidoBonificacao()) {
			new RelVerbaClienteWindow(getPedido().getCliente(), null).popup();
		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(getPedido()) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			new RelVerbaGrupoSaldoWindow(getPedido()).popup();
		} else {
			new RelVerbaSaldoWindow(getPedido()).popup();
		}
	}


	private void btPerderPedidoClick() throws SQLException {
		Pedido pedido = getPedido();

		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			throw new ValidationException(Messages.PEDIDO_MSG_PERDERPEDIDO_QTD_ITENS);
		}

		if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return;

		CadMotivoPedidoPerdidoWindow cadMotivoPedidoPerdidoWindow = getNewCadMotivoPedidoPerdidoWindow();
		if (ValueUtil.isEmpty(cadMotivoPedidoPerdidoWindow.cdMotivoPerda)) return;

		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoPerdido;
		pedido.cdMotivoPerda = cadMotivoPedidoPerdidoWindow.cdMotivoPerda;
		pedido.dsObservacao = cadMotivoPedidoPerdidoWindow.dsObservacao;
		pedido.flPendente = ValueUtil.VALOR_NAO;

		if (PedidoService.getInstance().definePedidoPerdido(pedido)) {
			if (getBaseCrudListForm()!= null) {
				getBaseCrudListForm().list();
			}
		}
		edit(getPedido());
	}

	private CadMotivoPedidoPerdidoWindow getNewCadMotivoPedidoPerdidoWindow() throws SQLException {
		CadMotivoPedidoPerdidoWindow cadMotivoPedidoPerdidoWindow = new CadMotivoPedidoPerdidoWindow();
		cadMotivoPedidoPerdidoWindow.popup();
		return cadMotivoPedidoPerdidoWindow;
	}

	private void adicionaMarcadoresTela() throws SQLException {
		Container cPrincipal = getContainerPrincipal();
		UiUtil.add(cPrincipal, lbMarcadores = new LabelName(Messages.PEDIDO_MARCADORES), getLeft(), AFTER + HEIGHT_GAP);
		lbMarcadores.appId = 1;
		Vector marcadores = MarcadorService.getInstance().buscaMarcadoresDePedido(((Pedido) getDomain()));
		for (int i = 0; i < marcadores.size(); i++) {
			Marcador marcador = (Marcador) marcadores.items[i];
			LabelValue label = new LabelValue(marcador.dsMarcador);
			label.appId = 1;
			if (marcador.imMarcadorAtivo == null) {
				UiUtil.add(cPrincipal, label, LEFT + WIDTH_GAP_BIG, AFTER);
				continue;
			}
			Image image = UiUtil.getImage(marcador.imMarcadorAtivo);
			image = UiUtil.getSmoothScaledImage(image, lbMarcadores.getPreferredHeight(), lbMarcadores.getPreferredHeight());
			ImageControl img = new ImageControl(image);
			img.appId = 1;
			label.split(cPrincipal.getWidth() - img.getWidth() - (WIDTH_GAP_BIG * 5));
			UiUtil.add(cPrincipal, img, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(cPrincipal, label, AFTER + WIDTH_GAP_BIG, SAME);
		}
	}

	private void clearMarcadores() {
		Container cPrincipal = getContainerPrincipal();
		Control next = null;
		for (Control control = lbMarcadores; control != null;) {
			next = control.getNext();
			if (control.appId != 1) {
				break;
			}
			cPrincipal.remove(control);
			control = next;
		}
		lbMarcadores = null;
	}

	private void btConverterTipoPedidoClick(Pedido pedidoRef) throws SQLException {
		doReplicarPedido(pedidoRef, false, true);
	}

	private void listaItensAdvertencia(Pedido pedido) throws SQLException {
		RelItemPedidoDivergenciaWindow rel = new RelItemPedidoDivergenciaWindow(Messages.REL_ITENS_COM_ADVERTENCIA, pedido);
		rel.popup();
		SolAutorizacaoService.getInstance().validateSolAutorizacaoFechamentoPedido(pedido);
	}

	private void btLibParcelaSolAutorizacaoClick() throws SQLException {
		if (!UiUtil.showWarnConfirmYesNoMessage(Messages.SOL_AUTORIZACAO_CONFIRM_NEGOCIACAO_PARCELA)) return;
		SolAutorizacaoService.getInstance().insertAutorizacaoByPedido(getPedido(), TipoSolicitacaoAutorizacaoEnum.PARCELA_MIN_MAX, true);
		voltarClick();
	}

	public void gerarPdfPedido() throws SQLException {
		if (LavenderePdaConfig.isUsaGeracaoPdfOnline() && LavenderePdaConfig.isUsaGeracaoPdfOffline()) {
			
			if (LavenderePdaConfig.isGeraPdfOnlinePorPadrao()) {
				geraPdfOnline();
			} else {
				int result = UiUtil.showMessage(Messages.GERA_ONLINE_OU_OFFLINE, new String [] {Messages.BOTAO_ONLINE, Messages.BOTAO_OFFLINE, Messages.BOTAO_CANCELAR});
				if (result == 0) {
					geraPdfOnline();
				} else if (result == 1) {
					geraPdfOffline();
				}
			}
		} else if (LavenderePdaConfig.isUsaGeracaoPdfOnline()) {
			geraPdfOnline();
		} else if (LavenderePdaConfig.isUsaGeracaoPdfOffline()) {
			geraPdfOffline();
		}
	}

	public void abrePdfOffline(String filePath) {
		VmUtil.executeGarbageCollector();
		String path = Convert.appendPath((VmUtil.isSimulador() ? "file:///" : ""), filePath);
		int abriuVisualizador = Vm.exec(SYSTEM_VIEWER, path, 0, true);
		if (abriuVisualizador == -1 && !VmUtil.isJava()) {
			UiUtil.showWarnMessage(Messages.RELATORIO_PDF_OFFLINE_PREVISUALIZACAO_INDISPONIVEL + MessageUtil.getMessage(Messages.RELATORIO_PDF_OFFLINE_LOCAL_PDF, filePath));
		}
	}

	public void abrePdfOnline(String pdf) {
		VmUtil.executeGarbageCollector();
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			int executed = Vm.exec(SYSTEM_VIEWER, pdf, 0, true);
			if (executed == -1) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} else if (VmUtil.isSimulador()) {
			Vm.exec(SYSTEM_VIEWER, "file:///" + pdf, 0, true);
		}
	}
	
	private void voltarEListarMantendoScroll(ListPedidoForm listPedidoForm) throws SQLException {
		GridListContainer listContainer = listPedidoForm.getListContainer();
		if (listContainer != null) {
			int lastIndex = listContainer.getSelectedIndex();
			int lastScrollPos = listContainer.getScrollPos();
			listPedidoForm.list();
			if (lastIndex > 0) {
				Container previousContainer = listContainer.getContainer(lastIndex - 1);
				if (previousContainer == null && lastIndex > 1) previousContainer = listContainer.getContainer(lastIndex - 2);
				if (previousContainer != null) {
					if (lastIndex >= listContainer.size()) {
						lastIndex = listContainer.size() - 1;
					}
					listContainer.scrollToControl(previousContainer);
					listContainer.setSelectedIndex(lastIndex);
					listContainer.setScrollPos(lastScrollPos);
				}
			}
		}
	}

	public void carregaEnderecoNovoCliente(Cliente cliente) throws SQLException {
		NovoClienteService.getInstance().carregaEnderecoClienteOficialByNovoCliente(cliente);
	}
	
	private void saveAndEditPedido() throws SQLException {
		Pedido pedido = getPedido();
		if (!isEditing() && pedido.isPedidoAberto()) {
			save();
			edit(pedido);
		}
	}
	
	private void btPoliticasBonificacaoClick() throws SQLException {
		saveAndEditPedido();
		new ListBonifCfgWindow(getPedido()).popup();
	}

	private void btGerarPdfCatalogoPedidoClick() throws SQLException {
		if (!validateGerarPdfCatalogoPedido()){
			return;
		}
		String nuPedido = "";
		try {
			nuPedido = getPedido().nuPedido;
			String pdf = SyncManager.geraPdfCatalogoPedido(getPedido());
			pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			if (LavenderePdaConfig.isGeraHistoricoCatalogoItensPedidoOnline()) {
				CatalogoItemPedLogService.getInstance().insertCatalogoItemPedLog(getPedido());
			}
			if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
				abrePdfOnline(pdf);
			} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_GERADO_SUCESSO)) {
				abrePdfOnline(pdf);
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} catch (Exception e) {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_PEDIDO, nuPedido), StringUtil.clearEnterException(e.getMessage()));
		}
	}

	private boolean validateGerarPdfCatalogoPedido() throws SQLException {
		if (VmUtil.isWinCEPocketPc()) {
			UiUtil.showInfoMessage(Messages.MSG_NAO_POSSUI_SUPORTE_PDF_PEDIDO);
			return false;
		}
		Pedido pedido = getPedido();
		if (pedido == null) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_CATALOGO_PRODUTO_NAO_ENCONTRADO);
			return false;
		}
		if (pedido.getQtItensLista() <= 0) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_CATALOGO_PRODUTO_PEDIDO_SEM_ITENS);
			return false;
		}
		if (pedido.isPedidoNovoCliente() && pedido.getCliente().isNovoClienteDefaultParaNovoPedido()) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_CATALOGO_PRODUTO_PEDIDO_SEM_NOVO_CLIENTE);
			return false;
		}
		if (pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_GERAR_PDF_CATALOGO_PRODUTO_PEDIDO_SEM_CLIENTE);
			return false;
		}
		return true;
	}

	private void btGerarCatalogoClick() throws SQLException {
		(new CatalogoProdutoWindow()).popup();
	}
	
	private boolean isPedidoQueUsaTipoPagamento(Pedido pedido) throws SQLException {
		return !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao();
	}

	public void btNovoItemPedidoMenuCatalogoClick() throws SQLException {
		PedidoService.getInstance().validateBateria();
		try {
			btNovoItemClick(false);
			MenuCatalogo entidadePrimeiroNivel = MenuCatalogoService.getInstance().findEntidadePrimeiroNivel(ItemPedido.class.getSimpleName());
			if (entidadePrimeiroNivel != null) {
				MenuCatalogoForm menuCatalogoForm = new MenuCatalogoForm(entidadePrimeiroNivel, this);
				show(menuCatalogoForm);
			} else {
				novoItemPedido();
			}
		} catch (RelacionaPedProducaoException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void redirecionaAoPedidoDeBonificacaoRelacionada(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !pedido.getTipoPedido().isTipoPedidoBonificacaoContaCorrente() && (pedido.totalmenteConvertidoPedidoBonificacao || ItemPedidoBonifCfgService.getInstance().isPedidoComItemContaCorrente(pedido))) {
			Pedido pedidoBonifRelacionada = new Pedido();
			pedidoBonifRelacionada.cdEmpresa = pedido.cdEmpresa;
			pedidoBonifRelacionada.cdRepresentante = pedido.cdRepresentante;
			pedidoBonifRelacionada.nuPedidoRelBonificacao = pedido.nuPedido;
			pedidoBonifRelacionada.flOrigemPedido = pedido.flOrigemPedido;
			pedidoBonifRelacionada.limit = 1;
			pedidoBonifRelacionada = (Pedido) PedidoService.getInstance().findAllByExampleOnlyPda(pedidoBonifRelacionada).items[0];
			if (pedidoBonifRelacionada != null) {
				if (pedido.totalmenteConvertidoPedidoBonificacao || UiUtil.showConfirmYesNoMessage(Messages.BONIFCFG_PERGUNTA_VER_PEDIDO_BONIFICACAO_RELACIONADA)) {
					if (pedido.totalmenteConvertidoPedidoBonificacao) {
						UiUtil.showInfoMessage(FrameworkMessages.TITULO_MSG_AVISO, Messages.BONIFCFG_AVISO_REDIRECIONAMENTO_PEDIDO_BONIFICACAO);
					}
					edit(pedidoBonifRelacionada);
					show(this);
				}
			}
		}		
	}

	public void savePedidoDesc(Pedido pedido) throws SQLException {
		double vlPctDescontoLiberacaoRestante = 0;
		double vlPctDescontoLiberado = 0;
		if (!LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() && !LavenderePdaConfig.isUsaMotivoPendencia()) {
			vlPctDescontoLiberacaoRestante = PedidoDescService.getInstance().getVlPctDescontoLiberacaoRestante(pedido);
			vlPctDescontoLiberado = UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(false, false);
			if (LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
				double vlPctDescontoLiberadoMediaPonderada = UsuarioDescService.getInstance().getMaxVlPctDescontoPonderadoLiberado(pedido);
				vlPctDescontoLiberado = Math.min(vlPctDescontoLiberadoMediaPonderada, vlPctDescontoLiberacaoRestante);
			}
		}
		double vlTotalPedidoLiberadoAtual = PedidoDescService.getInstance().getVlTotalPedidoLiberadoAtual(pedido);
		double vlDescontoLiberado = (vlTotalPedidoLiberadoAtual * vlPctDescontoLiberado) / 100;
		if (ValueUtil.valueEquals(vlPctDescontoLiberado, vlPctDescontoLiberacaoRestante)) {
			vlDescontoLiberado = vlTotalPedidoLiberadoAtual - pedido.getVlTotalBrutoItensComDesconto();
		}
		vlDescontoLiberado = ValueUtil.round(Math.max(0, vlDescontoLiberado));
		vlPctDescontoLiberado = ValueUtil.round(Math.max(0, vlPctDescontoLiberado));
		PedidoDescErpService.getInstance().savePedidoDescErp(pedido, vlPctDescontoLiberado, vlDescontoLiberado, PedidoDescErp.PEDIDODESCERP_FLORIGEMERP);
		PedidoDescService.getInstance().savePedidoDesc(pedido, vlPctDescontoLiberado, vlDescontoLiberado);
	}
	
	protected void btConverterOrcamentoParaPedidoClick() throws SQLException {
		try {
			UiUtil.showProcessingMessage();
			Pedido pedido = getPedido();
			Cliente cliente = pedido.getCliente();
			cliente.somentePedidoPreOrcamento = false;
			if (!antesConverterOrcamentoParaPedido(cliente)) {
				cliente.convertendoOrcamentoEmPedido = false;
				return;
			}
			pedido.cdStatusOrcamento = null;
			StatusOrcamentoService.getInstance().fillStatusOrcamentoPedido(pedido);;
			edit(pedido);
			update(pedido);
			afterStatusOrcamentoPedidoChange(pedido);
			cliente.convertendoOrcamentoEmPedido = false;
			UiUtil.showSucessMessage(Messages.MSG_CONVERSAO_ORCAMENTO_PEDIDO);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	public boolean antesConverterOrcamentoParaPedido(Cliente cliente) throws SQLException {
		cliente.convertendoOrcamentoEmPedido = true;
		if (VisitaUiUtil.avisaEBloqueiaCriarPedidoVisitaAndamento(cliente)) {
			return false;
		}
		
		if (VisitaUiUtil.isCadastraCoordenada() && !VisitaUiUtil.mostraMensagemDeCadastroDeCoordenadasCliente()) {
			return false;
		}
		
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			if (!VisitaUiUtil.sugereRegistroChegada(cliente, null)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isStatusOrcamentoAtualPreOrcamento(Pedido pedido) {
		return LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && pedido.statusOrcamento != null && pedido.statusOrcamento.isStatusPreOrcamento();
	}
	
	protected void cancelaPedidoStatusOrcamento(Pedido pedido) throws SQLException {
		StatusOrcamento statusOrcCancelado = StatusOrcamentoService.getInstance().findStatusOrcamentoCancelado();
		if (statusOrcCancelado == null) {
			UiUtil.showErrorMessage(Messages.MSG_STATUS_ORCAMENTO_CANCELADO_NAO_CADASTRADO);
			return;
		}
		onChangeToStatusOrcamentoCancelado(statusOrcCancelado, pedido, pedido.cdStatusOrcamento);
	}
	
	private void avisaUsuarioDePedidoPendentePorLimCreditoOuCondPagto(Pedido pedido) {
		String message = ValueUtil.VALOR_NI;
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() && pedido.isPendenteLimCred()) {
			message = Messages.MSG_PEDIDO_PENDENTE_LIMCREDITO_EXTRAPOLADO;
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto() && pedido.isPendenteCondPagto()) {
			if (ValueUtil.isNotEmpty(message)) {
				message += FrameworkMessages.CONJUCAO_ADITIVA_E + FrameworkMessages.PREPOSICAO_POR + " ";
			}
			message += LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagtoDiferentePadrao() ? Messages.CONDICAO_PAGAMENTO_NAO_PADRAO_INFO_PENDENTE : Messages.MSG_PEDIDO_PENDENTE_QTDIASPAGAMENTO_CONDPAGTO;
		}
		if (ValueUtil.isNotEmpty(message)) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_PEDIDO_PENDENTE_POR, message));
		}
	}
	
	private void setCbCondicaoComercialEnabled(Pedido pedido) {
		cbCondicaoComercial.setEnabled(LavenderePdaConfig.usaCondicaoComercialPedido && (pedido.itemPedidoList.size() <= 0 || PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(pedido)) && !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && isEnabled());
	}

	private boolean isPedidoAbertoNaoEditavelPermiteFecharExcluir(Pedido pedido) {
		return pedido.isPedidoAberto() && (pedido.isEdicaoBloqueada() || pedido.parcelaMinMaxPendenteOuAutorizada);
	}
	
	private void addComponentesFreteFixosFimCapaEscolhaTransp(Container tabPanelPedido) {
		List<String> camposList = LavenderePdaConfig.getDominiosInformacoesFreteCapaPedidoEscolhaTransportadora();
		if (camposList.contains(CamposEscolhaTransportadoraOptions.CAMPO_NOME_TRANSPORTADORA.getOption())) {
			UiUtil.add(tabPanelPedido, new LabelName(Messages.TRANSPORTADORAREG_NOME_ENTIDADE), lvTransportadoraCapaPedido, getLeft(), getNextY());
		}
		if (camposList.contains(CamposEscolhaTransportadoraOptions.CAMPO_DESCRICAO_TIPOFRETE.getOption())) {
			UiUtil.add(tabPanelPedido, new LabelName(Messages.TRANSPORTADORAREG_LABEL_CDTIPOFRETE), lvTipoFreteCapaPedido, getLeft(), getNextY());
		}
	}
	
	private boolean isExibeVlFreteComponentesFixosFimCapaEscolhaTransp() throws SQLException {
		return LavenderePdaConfig.isExibeInformacoesFreteCapaPedidoEscolhaTransportadora() && LavenderePdaConfig.getDominiosInformacoesFreteCapaPedidoEscolhaTransportadora().contains(CamposEscolhaTransportadoraOptions.CAMPO_VALORFRETE.getOption()) && (ValueUtil.isEmpty(getPedido().cdTransportadora) || (getPedido().getTransportadora() != null && getPedido().getTransportadora().isFlMostraFrete()));
	}
	
	private void populateCamposFreteCapaPedidoEscolhaTransp(String nmTransportadora, String dsTipoFrete, double vlFrete) {
		if (!LavenderePdaConfig.isExibeInformacoesFreteCapaPedidoEscolhaTransportadora()) return;
		lvTransportadoraCapaPedido.setValue(nmTransportadora);
		lvTipoFreteCapaPedido.setValue(dsTipoFrete);
		lvVlFreteCapaPedido.setValue(vlFrete);
	}
	
	private void bgUtilizaRentabilidadeValueChange() throws SQLException {
		Pedido pedido = getPedido();
		if (!UiUtil.showConfirmYesNoMessage(ValueUtil.getBooleanValue(bgUtilizaRentabilidade.getValue()) ? Messages.UTILIZAR_RENTAB_CONFIRM_MUDANCA : Messages.NAO_UTILIZAR_RENTAB_CONFIRM_MUDANCA)) {
			bgUtilizaRentabilidade.setValue(pedido.flUtilizaRentabilidade);
		} else {
			pedido.flUtilizaRentabilidade = bgUtilizaRentabilidade.getValue(); 
			if (isEditing()) {
				if (bgUtilizaRentabilidade.getValueBoolean()) {
					try {
						MargemRentabService.getInstance().recalcularRentabilidadePedido(pedido, false);
					} catch (Exception e) {
						bgUtilizaRentabilidade.setValueBoolean(false);
						pedido.flUtilizaRentabilidade = bgUtilizaRentabilidade.getValue();
						PedidoService.getInstance().clearRentabilidadePedido(pedido);
						throw e;
					}
				} else {
					PedidoService.getInstance().clearRentabilidadePedido(pedido);
				}
			}
			setInfoRentabilidadeVisible(!bgUtilizaRentabilidade.getValueBoolean());
		}
	}

	private void rollbackTrocaCondPagto(double ultimoVlTotalPedidoLiberado, int ultimoQtDiasCPgtoLibSenha) throws SQLException {
		cbCondicaoPagamento.setValue(ultimaCondPgamentoSelected);
		getPedido().cdCondicaoPagamento = ultimaCondPgamentoSelected;
		getPedido().vlTotalPedidoLiberado = ultimoVlTotalPedidoLiberado;
		getPedido().qtDiasCPgtoLibSenha = ultimoQtDiasCPgtoLibSenha;
	}
	
	private void generateAndUpdateParcelasByCondPagto() throws SQLException {
		generateParcelasPedidoByCondicaoPagamento();
		listParcelaPedidoForm.setPermiteEdicaoParcelas(isTabPagtoPermiteEdicaoParcelas());
		atualizaListParcelaPedidoForm(getPedido());
	}

	private boolean isTabPagtoPermiteEdicaoParcelas() throws SQLException {
		return isEditing() && getPedido().isPedidoAberto() && (isTipoCondPagtoParceladoUsuarioEmPercentual() || isTipoCondPagtoParceladoUsuarioEmValores());
	}
	
	private boolean isTipoCondPagtoParceladoUsuarioEmPercentual() throws SQLException {
		return LavenderePdaConfig.isGeraParcelasEmPercentual() && ParcelaPedidoService.getInstance().isTipoParceladoUsuario(getPedido());
	}
	
	private boolean isTipoCondPagtoParceladoUsuarioEmValores() throws SQLException {
		return !LavenderePdaConfig.isGeraParcelasEmPercentual() && ParcelaPedidoService.getInstance().isTipoOutro(getPedido());
	}

}
