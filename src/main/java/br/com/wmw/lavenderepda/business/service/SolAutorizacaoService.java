package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationWarnException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SolAutorizacaoDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoApp2WebRunnable;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.io.ByteArrayStream;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class SolAutorizacaoService extends CrudService {

	public static final String IMAGES_SOLICITACAO_AUTORIZACAO_PNG = "images/solicitacaoAutorizacao.png";

    private static SolAutorizacaoService instance;

    private SolAutorizacaoService() {}
	public static SolAutorizacaoService getInstance() { return (instance == null) ? instance = new SolAutorizacaoService() : instance; }

	@Override protected CrudDao getCrudDao() { return SolAutorizacaoDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

	public boolean hasSolAutorizacaoItemPedido(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		return countByExample(getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum)) > 0;
	}

	public boolean hasSolAutorizacaoPedido(Pedido pedido, boolean ignoreRemovidos) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, null);
		filter.ignoreRemovidos = ignoreRemovidos;
		return countByExample(filter) > 0;
	}

	public boolean hasSolAutorizacaoNaoAutorizadaPedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flVisualizadoDifferenceFilter = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().countItensByFilter(filter, ValueUtil.VALOR_NAO) > 0;
	}

	public boolean hasSolAutorizacaoAutorizadaPedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flVisualizadoDifferenceFilter = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().countItensByFilter(filter, ValueUtil.VALOR_SIM) > 0;
	}

	public boolean hasSolAutorizacaoPendentePedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		return SolAutorizacaoDbxDao.getInstance().countItensByFilter(filter, ValueUtil.VALOR_NI) > 0;
	}

	public boolean hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flVisualizadoDifferenceFilter = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().countItensNaoAutorizadosOuPendentesByFilter(filter) > 0;
	}

	public boolean hasSolAutorizacaoPendenteOuAutorizadaByPedido(Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (pedido == null || pedido.nuPedido == null) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		return SolAutorizacaoDbxDao.getInstance().countItensAutorizadosOuPendentesByFilter(filter) > 0;
	}
	
	public boolean hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido(Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, String cdAgrupadorSimilaridade) throws SQLException {
		if (pedido == null || pedido.nuPedido == null || ValueUtil.isEmpty(cdAgrupadorSimilaridade)) return false;
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		filter.cdAgrupadorSimilaridade = cdAgrupadorSimilaridade;
		return SolAutorizacaoDbxDao.getInstance().countItensAutorizadosOuPendentesSimilaresByFilter(filter) > 0;
	}

	public boolean isItemPedidoNaoAutorizadoOuPendente(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flVisualizadoDifferenceFilter = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().countItensNaoAutorizadosOuPendentesByFilter(filter) > 0;
	}

	public boolean isItemPedidoAutorizadoOuPendente(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		return SolAutorizacaoDbxDao.getInstance().countItensAutorizadosOuPendentesByFilter(filter) > 0;
	}

	public boolean isItemPedidoPendente(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		return SolAutorizacaoDbxDao.getInstance().countItensByFilter(filter, ValueUtil.VALOR_NI) > 0;
	}

	public boolean isItemPedidoNaoAutorizado(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flVisualizadoDifferenceFilter = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().countItensByFilter(filter, ValueUtil.VALOR_NAO) > 0;
	}

	public boolean isItemPedidoAutorizado(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		boolean hasSolAutorizacaoItemPedido = itemPedido.solAutorizacaoItemPedidoCache.getHasSolAutorizacaoItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		return hasSolAutorizacaoItemPedido && SolAutorizacaoDbxDao.getInstance().countItensNaoAutorizadosOuPendentesByFilter(filter) == 0;
	}

	public boolean deleteAllByItemPedido(final ItemPedido itemPedido) throws Exception {
		if (itemPedido.pedido == null || itemPedido.pedido.ignoreSolAutorizacaoItemExcluido) {
			return false;
		}
		SolAutorizacao solAutorizacao = getSolAutoriacaoFilterByItemPedido(itemPedido, null);
		solAutorizacao.flExcluido = ValueUtil.VALOR_SIM;
		boolean atualizouRegistros = SolAutorizacaoDbxDao.getInstance().deleteAllByItemPedido(solAutorizacao);
		if (!itemPedido.auxiliarVariaveis.removendoPedido && atualizouRegistros) {
			enviaAtualizacao(null);
			itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
			if (itemPedido.pedido != null) {
				itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(itemPedido.pedido);
			}
		}
		return atualizouRegistros;
	}

	public void deleteAllByPedido(final Pedido pedido) throws Exception {
		if (pedido == null || pedido.ignoreSolAutorizacaoItemExcluido) return;
		SolAutorizacao solAutorizacao = getSolAutorizacaoFilterByPedido(pedido, null);
		solAutorizacao.flExcluido = ValueUtil.VALOR_SIM;
		boolean atualizouRegistros = SolAutorizacaoDbxDao.getInstance().deleteAllByItemPedido(solAutorizacao);
		if (atualizouRegistros) {
			enviaAtualizacao(null);
			pedido.solAutorizacaoPedidoCache.clearCaches(pedido);
		}
	}

	@Override
	public void deleteAllByExample(BaseDomain domain) throws SQLException {
		super.deleteAllByExample(domain);
		try {
			enviaAtualizacao((SolAutorizacao) domain);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void delete(BaseDomain domain) throws SQLException {
		super.delete(domain);
		try {
			enviaAtualizacao((SolAutorizacao) domain);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void insert(BaseDomain domain) throws SQLException {
		super.insert(domain);
		try {
			enviaAtualizacao((SolAutorizacao) domain);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public void updateFlVisualizadoByItemPedido(ItemPedido itemPedido, String flVisualizado) {
		if (itemPedido.pedido == null) return;

		SolAutorizacao solAutoriacaoFilterByItemPedido = getSolAutoriacaoFilterByItemPedido(itemPedido, null);
		solAutoriacaoFilterByItemPedido.flVisualizado = flVisualizado;
		SolAutorizacaoDbxDao.getInstance().updateFlAtualizadoByExample(solAutoriacaoFilterByItemPedido);
		itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
		if (itemPedido.pedido != null) {
			itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(itemPedido.pedido);
		}
	}

	public void insertAutorizacaoByItemPedido(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, boolean sync, boolean ignoreValidate) throws SQLException {
		if (itemPedido.pedido == null) return;
		if (!ignoreValidate) {
			validateAutorizacao(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		}
		SolAutorizacao solAutorizacao = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		solAutorizacao.cdSolAutorizacao = getNextCdSolAutorizacao();
		solAutorizacao.qtItemFisico = itemPedido.getQtItemFisico();
		solAutorizacao.cdTabelaPreco = itemPedido.cdTabelaPreco;
		solAutorizacao.vlItemPedido = itemPedido.vlItemPedido;
		solAutorizacao.vlTotalItemPedido = itemPedido.vlTotalItemPedido;
		solAutorizacao.vlOriginal = ValueUtil.round(itemPedido.vlTotalBrutoItemPedido / itemPedido.getQtItemFisico());
		solAutorizacao.flAutorizado = null;
		solAutorizacao.cdUsuarioSolAutorizacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		solAutorizacao.dtSolAutorizacao = DateUtil.getCurrentDate();
		solAutorizacao.hrSolAutorizacao = TimeUtil.getCurrentTimeHHMMSS();
		solAutorizacao.flVisualizado = ValueUtil.VALOR_NAO;
		solAutorizacao.flExcluido = ValueUtil.VALOR_NAO;
		if (sync) {
			insert(solAutorizacao);
		} else {
			super.insert(solAutorizacao);
		}
		itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
		if (itemPedido.pedido != null) {
			itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(itemPedido.pedido);
		}
	}

	public void insertAutorizacaoByPedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, boolean sync) throws SQLException {
		if (pedido == null) return;
		SolAutorizacao solAutorizacao = getSolAutorizacaoFilterByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
		solAutorizacao.cdSolAutorizacao = getNextCdSolAutorizacao();
		solAutorizacao.flAutorizado = null;
		solAutorizacao.cdUsuarioSolAutorizacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		solAutorizacao.dtSolAutorizacao = DateUtil.getCurrentDate();
		solAutorizacao.hrSolAutorizacao = TimeUtil.getCurrentTimeHHMMSS();
		solAutorizacao.flVisualizado = ValueUtil.VALOR_NAO;
		solAutorizacao.flExcluido = ValueUtil.VALOR_NAO;
		solAutorizacao.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		setParcelaValues(pedido, solAutorizacao, pedido.getCondicaoPagamento());
		if (sync) {
			insert(solAutorizacao);
		} else {
			super.insert(solAutorizacao);
		}
		pedido.solAutorizacaoPedidoCache.clearCaches(pedido);
	}

	private void setParcelaValues(Pedido pedido, SolAutorizacao solAutorizacao, CondicaoPagamento condPagto) throws SQLException {
		if (condPagto == null) return;
		double valorParcela = PedidoService.getInstance().getValorParcela(pedido);
		if (condPagto.qtMinValorParcela > valorParcela) {
			solAutorizacao.vlParcelaMinMax = condPagto.qtMinValorParcela;
		} else {
			solAutorizacao.vlParcelaMinMax = condPagto.qtMaxValorParcela;
		}
		solAutorizacao.vlparcelaPedido = PedidoService.getInstance().getValorParcela(pedido);
	}

	private String getNextCdSolAutorizacao() throws SQLException {
		return getCrudDao().generateIdGlobal();
	}

	public void validateAutorizacao(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		boolean isItemPedidoPendente = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoPendente(itemPedido, tipoSolicitacaoAutorizacaoEnum);
    	if (isItemPedidoPendente) throw new ValidationException(Messages.SOL_AUTORIZACAO_JA_POSSUI_AUT_PENDENTE);
	}

	private SolAutorizacao getSolAutorizacaoFilterByPedido(final Pedido pedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) {
		SolAutorizacao solAutorizacaoFilter = new SolAutorizacao();
		solAutorizacaoFilter.cdEmpresa = pedido.cdEmpresa;
		solAutorizacaoFilter.cdRepresentante = pedido.cdRepresentante;
		solAutorizacaoFilter.cdCliente = pedido.cdCliente;
		solAutorizacaoFilter.nuPedido = pedido.nuPedido;
		solAutorizacaoFilter.flOrigemPedido = pedido.flOrigemPedido;
		solAutorizacaoFilter.tipoSolicitacaoAutorizacaoEnum = tipoSolicitacaoAutorizacaoEnum;
		solAutorizacaoFilter.cdTabelaPreco = pedido.cdTabelaPreco;
		return solAutorizacaoFilter;
	}

	private SolAutorizacao getSolAutoriacaoFilterByItemPedido(final ItemPedido itemPedido, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) {
		SolAutorizacao solAutorizacaoFilter = getSolAutorizacaoFilterByPedido(itemPedido.pedido, tipoSolicitacaoAutorizacaoEnum);
		solAutorizacaoFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
		solAutorizacaoFilter.cdProduto = itemPedido.cdProduto;
		solAutorizacaoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		return solAutorizacaoFilter;
	}

	public boolean autorizarSolicitacao(SolAutorizacao solAutorizacao) {
    	try {
		    solAutorizacao.dsObservacao = null;
		    atualizaSolicitacao(solAutorizacao, ValueUtil.VALOR_SIM);
		    if (LavenderePdaConfig.geraNovidadeAutorizacao) {
		    	RelNovSolAutorizacaoService.getInstance().createAndInsertNovidade(solAutorizacao);
		    }
		    update(solAutorizacao);
		    return enviaAtualizacao(solAutorizacao);
	    } catch (Throwable e) {
    		ExceptionUtil.handle(e);
		    return false;
	    }
	}

	public boolean negarSolicitacao(SolAutorizacao solAutorizacao) {
		try {
			atualizaSolicitacao(solAutorizacao, ValueUtil.VALOR_NAO);
			if (LavenderePdaConfig.geraNovidadeAutorizacao) {
				RelNovSolAutorizacaoService.getInstance().createAndInsertNovidade(solAutorizacao);
			}
			update(solAutorizacao);
			return enviaAtualizacao(solAutorizacao);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	public boolean enviaAtualizacao(SolAutorizacao solAutorizacao) throws Exception {
		if (SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado() && solAutorizacao != null) {
			enviaAutorizacaoBackground(solAutorizacao);
			return true;
		}
		try {
			UiUtil.showProcessingMessage();
			if (!SyncManager.isConexaoPdaDisponivel()) return false;
			return !SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), SyncManager.getInfoAtualizacaoSolAutorizacao(true));
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	public boolean recebeAtualizacao() {
    	try {
		    UiUtil.showProcessingMessage();
		    if (!SyncManager.isConexaoPdaDisponivel()) return false;
		    return !SyncManager.recebaDados(HttpConnectionManager.getDefaultParamsSync(), SyncManager.getInfoAtualizacaoSolAutorizacao(false), true);
	    } catch (Throwable e) {
    		return false;
	    } finally {
    		UiUtil.unpopProcessingMessage();
	    }
	}

	public void enviaAutorizacaoBackground(SolAutorizacao solAutorizacao) throws SQLException {
		if (solAutorizacao == null || ValueUtil.isEmpty(solAutorizacao.rowKey)) return;
		try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
				ResultSet solAutoriazacaoRS = st.executeQuery(findByRowKeySql(solAutorizacao.rowKey))) {
			if (!solAutoriazacaoRS.isBeforeFirst()) return;
			LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
			ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
			int nuLinhas = lavendereTc2Web.writeDados(SolAutorizacao.TABLE_NAME, solAutoriazacaoRS, cbasRetorno, false, null).size();
			EnviaDadosThread.getInstance().put(SolAutorizacao.TABLE_NAME, nuLinhas, cbasRetorno, SolAutorizacaoService.getInstance().generateIdGlobal(), solAutorizacao.rowKey);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void atualizaSolicitacao(SolAutorizacao solAutorizacao, String valueAutorizado) {
		solAutorizacao.flAutorizado = valueAutorizado;
		solAutorizacao.flVisualizado = ValueUtil.VALOR_NAO;
		solAutorizacao.cdUsuarioLibSolAutorizacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		solAutorizacao.nmUsuarioLibSolAutorizacao = SessionLavenderePda.usuarioPdaRep.usuario.nmUsuario;
		solAutorizacao.dtLibSolAutorizacao = DateUtil.getCurrentDate();
		solAutorizacao.hrLibSolAutorizacao = TimeUtil.getCurrentTimeHHMMSS();
		solAutorizacao.flTipoAlteracao = SolAutorizacao.FLTIPOALTERACAO_ALTERADO;
	}

	public void validateSolAutorizacaoFechamentoPedido(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) return;
		boolean hasSolAutorizacaoPendentePedido = hasSolAutorizacaoPendentePedidoInCache(pedido);
		if (hasSolAutorizacaoPendentePedido) {
			recebeAtualizacao();
			pedido.solAutorizacaoPedidoCache.clearCaches(pedido);
		}
		processaItensLiberadosSemAutorizacao(pedido, hasSolAutorizacaoPendentePedido);
		hasSolAutorizacaoPendentePedido = hasSolAutorizacaoPendentePedidoInCache(pedido);
		if (hasSolAutorizacaoPendentePedido) throw new ApplicationWarnException(Messages.SOL_AUTORIZACAO_PEDIDO_FECHAMENTO_PENDENTE);
		
		boolean hasSolAutorizacaoNaoAutorizadaPedido = pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoNaoAutorizadaPedido(pedido, null);
		if (hasSolAutorizacaoNaoAutorizadaPedido) throw new ApplicationWarnException(Messages.SOL_AUTORIZACAO_PEDIDO_FECHAMENTO_NEGADO);
	}
	
	private boolean hasSolAutorizacaoPendentePedidoInCache(Pedido pedido) throws SQLException {
		return pedido.solAutorizacaoPedidoCache.getHasSolAutorizacaoPendentePedido(pedido, null);
	}

	private void processaItensLiberadosSemAutorizacao(Pedido pedido, boolean clearAutorizacaoCaches) {
		try {
			pedido.itemPedidoList = ValueUtil.isEmpty(pedido.itemPedidoList) ? ItemPedidoService.getInstance().findItemPedidoByPedido(pedido) : pedido.itemPedidoList;
			if (ValueUtil.isEmpty(pedido.itemPedidoList)) return;
			int size = pedido.itemPedidoList.size();
			boolean usaEnvioInformacoesBackground = SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado();
			boolean possuiAutorizacaoInserida = false;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.isFlPrecoLiberadoSenha() && countByExample(getSolAutoriacaoFilterByItemPedido(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO)) == 0) {
					insertAutorizacaoByItemPedido(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO, usaEnvioInformacoesBackground, true);
					possuiAutorizacaoInserida = true;
				}
				if (clearAutorizacaoCaches || possuiAutorizacaoInserida) {
					itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
				}
			}
			if (possuiAutorizacaoInserida && !usaEnvioInformacoesBackground) {
				enviaAtualizacao(null);
			}
			if (clearAutorizacaoCaches || possuiAutorizacaoInserida) {
				pedido.solAutorizacaoPedidoCache.clearCaches(pedido);
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	public Image getIconSolicitacao(ItemPedido itemPedido) throws SQLException {
		boolean isItemPedidoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, null);
		if (isItemPedidoAutorizado) return UiUtil.getIconButtonAction(IMAGES_SOLICITACAO_AUTORIZACAO_PNG, ColorUtil.softGreen, true);
		boolean itemPedidoNaoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoNaoAutorizado(itemPedido, null);
		if (itemPedidoNaoAutorizado) return UiUtil.getIconButtonAction(IMAGES_SOLICITACAO_AUTORIZACAO_PNG, ColorUtil.softRed, true);
		return UiUtil.getIconButtonAction(IMAGES_SOLICITACAO_AUTORIZACAO_PNG, Color.BRIGHT);
	}

	public void insertAndValidadePedidoCriticoBonificado(Pedido pedido) throws Exception {
		if (pedido == null || ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnumErro;
		if (LavenderePdaConfig.usaPedidoProdutoCritico
				&& LavenderePdaConfig.usaSolicitacaoAutorizacaoPorVendaCritica()
				&& pedido.isPedidoCritico()) {
			tipoSolicitacaoAutorizacaoEnumErro = TipoSolicitacaoAutorizacaoEnum.VENDA_CRITICA;
			if (insertItens(pedido, pedido.itemPedidoList, tipoSolicitacaoAutorizacaoEnumErro)) throw new ApplicationWarnException(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_PEDIDO_PENDENTE, tipoSolicitacaoAutorizacaoEnumErro.getTitle()));
		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido)
				&& LavenderePdaConfig.usaPedidoBonificacaoConsomeVerbaGrupoProduto()
				&& LavenderePdaConfig.usaSolicitacaoAutorizacaoPorBonificacao()
				&& pedido.isPedidoBonificacao()) {
			tipoSolicitacaoAutorizacaoEnumErro = TipoSolicitacaoAutorizacaoEnum.BONIFICACAO;
			if (insertItens(pedido, pedido.itemPedidoList, tipoSolicitacaoAutorizacaoEnumErro)) throw new ApplicationWarnException(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_PEDIDO_PENDENTE, tipoSolicitacaoAutorizacaoEnumErro.getTitle()));
		}
	}

	private boolean insertItens(Pedido pedido, final Vector itemPedidoList, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws Exception {
		int size = itemPedidoList.size();
		ItemPedido itemPedido;
		boolean hasItemInserido = false;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) itemPedidoList.items[i];
			boolean hasSolAutorizacaoItemPedido = itemPedido.solAutorizacaoItemPedidoCache.getHasSolAutorizacaoItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
			if (hasSolAutorizacaoItemPedido) {
				boolean isItemPedidoAutorizadoOuPendente = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizadoOuPendente(itemPedido, tipoSolicitacaoAutorizacaoEnum);
				if (isItemPedidoAutorizadoOuPendente) continue;
				boolean itemPedidoNaoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoNaoAutorizado(itemPedido, tipoSolicitacaoAutorizacaoEnum);
				if (itemPedidoNaoAutorizado) continue;
			}

			insertAutorizacaoByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum, SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado(), false);
			hasItemInserido = true;
			itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
		}
		if (hasItemInserido && !SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado()) {
			enviaAtualizacao(null);
			pedido.solAutorizacaoPedidoCache.clearCaches(pedido);
		}
		return hasItemInserido;
	}

	public double getVlLiberadoBySolAutorizacao(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum negociacaoPreco) throws SQLException {
		if (itemPedido.pedido == null) {
			return 0;
		}

    	SolAutorizacao solAutorizacao = getSolAutoriacaoFilterByItemPedido(itemPedido, negociacaoPreco);
    	solAutorizacao.flAutorizado = ValueUtil.VALOR_SIM;
    	solAutorizacao.sortAtributte = SolAutorizacao.NM_COLUMN_CDSOLAUTORIZACAO;
    	solAutorizacao.sortAsc = ValueUtil.VALOR_NAO;
    	Vector solAutorizacaoList =  SolAutorizacaoDbxDao.getInstance().findAllByExample(solAutorizacao);
    	if (ValueUtil.isNotEmpty(solAutorizacaoList)) {
    		return ((SolAutorizacao) solAutorizacaoList.items[0]).vlItemPedido;
	    } else {
    		return itemPedido.vlItemMinAfterLibPreco;
	    }
	}
	
	public Vector findSolAutorizacaoItemsByPedido(Pedido pedido) throws SQLException {
		SolAutorizacao filter = getSolAutorizacaoFilterByPedido(pedido, null);
		filter.maxAutorizacao = true;
		return findAllByExample(filter);
	}
	
	public boolean isItemAutorizadoSimilar(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		if (itemPedido.pedido == null) return false;

		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flAgrupadorSimilaridade = ValueUtil.VALOR_SIM;
		if (countByExample(filter) > 0) {
			filter.flAgrupadorSimilaridade = null;
			return SolAutorizacaoDbxDao.getInstance().countItensNaoAutorizadosOuPendentesByFilter(filter) == 0;
		}
		return false;
	}
	
	public SolAutorizacao getSolAutorizacaoSimilares(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		filter.flAgrupadorSimilaridade = ValueUtil.VALOR_SIM;
		return SolAutorizacaoDbxDao.getInstance().getSolAutorizacao(filter);
	}
	
	public SolAutorizacao getSolAutorizacao(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		SolAutorizacao filter = getSolAutoriacaoFilterByItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
		filter.sortAtributte = SolAutorizacao.NM_COLUMN_CDSOLAUTORIZACAO;
		filter.sortAsc = ValueUtil.VALOR_NAO;
		return SolAutorizacaoDbxDao.getInstance().getSolAutorizacao(filter);
	}
	
	public boolean isAllItensPedidoAutorizado(Vector itemPedidoList, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
		for(int i = 0; i<itemPedidoList.size(); i++) {
			if (!isItemPedidoAutorizado((ItemPedido)itemPedidoList.items[i], tipoSolicitacaoAutorizacaoEnum)) {
				return false;
			}
		}
		return true;
	}
}
