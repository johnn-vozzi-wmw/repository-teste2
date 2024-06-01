package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.dto.EstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.RemessaEstoqueDto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RemessaEstoqueDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sql.Types;
import totalcross.util.Vector;

public class RemessaEstoqueService extends CrudService {

    private static RemessaEstoqueService instance;
    
    private RemessaEstoqueService() {
        //--
    }
    
    public static RemessaEstoqueService getInstance() {
        if (instance == null) {
            instance = new RemessaEstoqueService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return RemessaEstoqueDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public void desbloquearRemessa(RemessaEstoque remessaEstoque, String cdBarras) throws Exception {
		if (ValueUtil.isEmpty(cdBarras)) {
			throw new ValidationException(Messages.REMESSAESTOQUE_LABEL_LER_CDBARRAS_ERRO);
		}
		if (!cdBarras.equals(remessaEstoque.vlChaveAcesso)) {
			throw new ValidationException(Messages.REMESSAESTOQUE_LABEL_CDBARRAS_ERRADO);
		}
		String retorno = SyncManager.desbloqueiaRemessaEstoque(remessaEstoque);
		liberaRemessaEstoque(remessaEstoque);
		
		if (LavendereWeb2Tc.SERVER_SEM_CONEXAO.equals(retorno)) {
			atualizaRemessaParaReplicacao(remessaEstoque);
		}
	}

	private void liberaRemessaEstoque(RemessaEstoque remessaEstoque) throws SQLException {
		updateColumn(remessaEstoque.getRowKey(), "flEstoqueLiberado", ValueUtil.VALOR_SIM, Types.VARCHAR);
		remessaEstoque.flEstoqueLiberado = ValueUtil.VALOR_SIM;
	}

	private void atualizaRemessaParaReplicacao(RemessaEstoque remessaEstoque) throws SQLException {
		updateColumn(remessaEstoque.getRowKey(), "flTipoAlteracao", BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
		remessaEstoque.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
	}

	public void updateEstoque(ItemPedido itemPedido) throws SQLException {
		double qtEstoqueErp = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_ERP);
		double qtEstoqueConsumido = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_PDA);
		if (itemPedido.getQtItemFisico() > itemPedido.getOldQtItemFisico()) {
			validateConsumoEstoque(itemPedido.getQtItemFisico(), qtEstoqueConsumido - itemPedido.getOldQtItemFisico(), qtEstoqueErp);
		}
		deleteEstoqueConsumido(itemPedido, Estoque.FLORIGEMESTOQUE_PDA);
		insereEstoque(itemPedido);
	}

	public void deleteEstoqueConsumido(ItemPedido itemPedido, String flOrigem) throws SQLException {
		Vector itemPedidoRemessaList = ItemPedidoRemessaService.getInstance().findAllItemPedidoRemessa(itemPedido);
		deleteEstoqueConsumido(itemPedidoRemessaList, flOrigem);
		ItemPedidoRemessaService.getInstance().deleteItemPedidoRemessa(itemPedido);
	}
	
	private void deleteEstoqueConsumido(Vector itemPedidoRemessaList, String flOrigem) throws SQLException {
		int size = itemPedidoRemessaList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) itemPedidoRemessaList.items[i];
			Estoque estoque = EstoqueService.getInstance().getEstoqueByItemPedidoRemessa(itemPedidoRemessa, flOrigem);
			if (estoque != null) {
				if (ValueUtil.valueEquals(Estoque.FLORIGEMESTOQUE_PDA, flOrigem)) {
					estoque.qtEstoque -= itemPedidoRemessa.qtEstoqueConsumido;
				} else {
					estoque.qtEstoque += itemPedidoRemessa.qtEstoqueConsumido;
				}
				try {
					EstoqueService.getInstance().updateColumn(estoque.getRowKey(), "qtEstoque", estoque.qtEstoque, Types.DECIMAL);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}
	
	protected void estornaEstoqueDaRemessa(Vector itemNfeList, Vector itemPedidoList, boolean isTipoPedidoRemessaEstoque, String flOrigemEstoque) throws SQLException {
		if (LavenderePdaConfig.usaControleEstoquePorRemessa && ValueUtil.isNotEmpty(itemPedidoList)) {
			boolean executaEstorno = LavenderePdaConfig.usaModoControleEstoquePorTipoPedido ? isTipoPedidoRemessaEstoque : true;
			if (executaEstorno) {
				excluiRemessaEstoqueNaoFinalizada(itemNfeList, itemPedidoList, flOrigemEstoque);
			}
		}
	}

	private void excluiRemessaEstoqueNaoFinalizada(Vector itemNfeList, Vector itemPedidoList, String flOrigemEstoque) throws SQLException {
		List<RemessaEstoque> remessaEstoqueList =  getRemessaEstoqueList(itemNfeList);
		for (RemessaEstoque remessaEstoque : remessaEstoqueList) {
			if (!remessaEstoque.isFinalizada()) {
				Vector itemPedidoRemessaFinalizadaList = ItemPedidoRemessaService.getInstance().findItemPedidoRemessaList(remessaEstoque);
				int size = itemPedidoRemessaFinalizadaList.size(); 
				for (int i = 0; i < size; i++) {
					ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) itemPedidoRemessaFinalizadaList.items[i];
					ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByRemessaEstoque(itemPedidoRemessa);
					if (itemPedidoList.contains(itemPedido)) {
						deleteEstoqueConsumido(itemPedido, flOrigemEstoque);
					}
				}
			}
		}
		excluiItemPedidoRemessa(itemPedidoList);
	}

	private void excluiItemPedidoRemessa(Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			ItemPedidoRemessaService.getInstance().deleteItemPedidoRemessa(itemPedido);
		}
	}
	
	public List<RemessaEstoque> getRemessaEstoqueList(Vector itemNfeList) throws SQLException {
		List<RemessaEstoque> remessaEstoqueList = new ArrayList<RemessaEstoque>();
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			final int sizeItemNfe = itemNfeList.size();
			for (int i = 0; i < sizeItemNfe; i++) {
				ItemNfe itemNfe = (ItemNfe)itemNfeList.items[i];
				ItemPedidoRemessa itemPedidoRemessa = ItemPedidoRemessaService.getInstance().getItemPedidoRemessaByItemNfe(itemNfe); 
				Vector itemPedidoRemessaList = ItemPedidoRemessaService.getInstance().findAllByExample(itemPedidoRemessa);
				final int size = itemPedidoRemessaList.size();
				for (int y = 0; y < size; y++) {
					itemPedidoRemessa = (ItemPedidoRemessa)itemPedidoRemessaList.items[y];
					RemessaEstoque remessaEstoque = findRemessaEstoque(itemPedidoRemessa);
					if (remessaEstoque != null && !remessaEstoqueList.contains(remessaEstoque)) {
						remessaEstoqueList.add(remessaEstoque);
					}
				}
			}
		}
		return remessaEstoqueList;
	}

	public void insertEstoque(ItemPedido itemPedido) throws SQLException {
		double qtEstoqueErp = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_ERP);
		double qtEstoqueConsumido = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_PDA);
		validateConsumoEstoque(itemPedido.getQtItemFisico(), qtEstoqueConsumido - itemPedido.getOldQtItemFisico(), qtEstoqueErp);
		insereEstoque(itemPedido);
	}

	private void insereEstoque(ItemPedido itemPedido) throws SQLException {
		if ("2".equals(itemPedido.pedido.flModoEstoque)) {
			return;
		}
		Vector estoqueErpList = EstoqueService.getInstance().findAllEstoqueRemessaLiberadaErp(itemPedido);
		double qtItemFisico = itemPedido.getQtItemFisico();
		int sizeEstRem = estoqueErpList.size();
		for (int i = 0; i < sizeEstRem; i++) {
			Estoque estoqueErp = (Estoque) estoqueErpList.items[i];
			Estoque estoquePdaFilter =  (Estoque) estoqueErp.clone();
			estoquePdaFilter.qtEstoque = 0d;
			estoquePdaFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
			Estoque estoquePda = (Estoque) EstoqueService.getInstance().findByPrimaryKey(estoquePdaFilter);
			double qtEstoquePda = estoquePda != null ? estoquePda.qtEstoque : 0d;
			double qtEstoqueDisponivel = estoqueErp.qtEstoque - qtEstoquePda; 
			if (qtEstoqueDisponivel > 0) {
				if (estoquePda == null || estoquePda.cdEmpresa == null) {
					estoquePda = estoquePdaFilter;
					EstoqueService.getInstance().insert(estoquePda);
				} else {
					estoquePda.nuNotaRemessa = estoqueErp.nuNotaRemessa;
					estoquePda.nuSerieRemessa = estoqueErp.nuSerieRemessa;
				}
				if (qtEstoqueDisponivel < qtItemFisico) {
					estoquePda.qtEstoque += qtEstoqueDisponivel;
					qtItemFisico -= qtEstoqueDisponivel;
					consomeEstoque(itemPedido, estoquePda, qtEstoqueDisponivel);
				} else {
					estoquePda.qtEstoque += qtItemFisico;
					consomeEstoque(itemPedido, estoquePda, qtItemFisico);
					break;
				}
			}
		}
	}

	private void consomeEstoque(ItemPedido itemPedido, Estoque estoquePda, double qtEstoqueConsumido) throws SQLException {
		EstoqueService.getInstance().updateColumn(estoquePda.getRowKey(), "qtEstoque", estoquePda.qtEstoque, Types.DECIMAL);
		ItemPedidoRemessaService.getInstance().insertItemRemessa(itemPedido, estoquePda, qtEstoqueConsumido);
	}

	private void validateConsumoEstoque(double qtItemFisico, double qtEstoqueConsumido, double qtEstoqueErp) {
		if (qtItemFisico > qtEstoqueErp - qtEstoqueConsumido) {
			String dsQtEstoque = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) (qtEstoqueErp - qtEstoqueConsumido)) : StringUtil.getStringValueToInterface(qtEstoqueErp - qtEstoqueConsumido);
			String dsQtItemFisico = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) qtItemFisico) : StringUtil.getStringValueToInterface(qtItemFisico);
			throw new ValidationException(MessageUtil.getMessage(Messages.REMESSAESTOQUE_LABEL_QT_ESTOQUE_INDISPONIVEL, new Object[]{dsQtItemFisico, dsQtEstoque}));
		}
	}

	public double getEstoqueDisponivelProduto(ItemPedido itemPedido) throws SQLException {
		double qtEstoqueErp = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_ERP);
		double qtEstoqueConsumido = EstoqueService.getInstance().getQtdEstoqueRemessa(itemPedido, Estoque.FLORIGEMESTOQUE_PDA);
		return qtEstoqueErp - qtEstoqueConsumido;
	}
	
	public Vector findAllRemessaEstoque(BaseDomain baseDomainFilter, boolean somenteLiberada) throws SQLException {
		RemessaEstoque remessaEstoqueFilter = (RemessaEstoque) baseDomainFilter;
		remessaEstoqueFilter.flFinalizada = somenteLiberada ? ValueUtil.VALOR_NAO : remessaEstoqueFilter.isFinalizadaFiltroNaTela() ? remessaEstoqueFilter.flFinalizada : null;
		return findAllByExample(baseDomainFilter);
	}
	
	private RemessaEstoque findRemessaEstoque(ItemPedidoRemessa itemPedidoRemessa) throws SQLException {
		RemessaEstoque remessaEstoqueFilter = new RemessaEstoque();
		remessaEstoqueFilter.cdEmpresa = itemPedidoRemessa.cdEmpresa;
		remessaEstoqueFilter.cdRepresentante = itemPedidoRemessa.cdRepresentante;
		remessaEstoqueFilter.nuNotaRemessa = itemPedidoRemessa.nuNotaRemessa;
		remessaEstoqueFilter.nuSerieRemessa = itemPedidoRemessa.nuSerieRemessa;
		return (RemessaEstoque)findByPrimaryKey(remessaEstoqueFilter);
	}
	
	public void insert(RemessaEstoqueDto remessaEstoqueDto) throws SQLException {
		insert(new RemessaEstoque(remessaEstoqueDto));
		for (EstoqueDto estoqueDto : remessaEstoqueDto.estoqueList) {
			Estoque estoque = new Estoque(estoqueDto);
			EstoqueService.getInstance().insert(estoque);
		}
	}
	
	public void deleteRemessasRepresentante(boolean parcial) throws SQLException {
		RemessaEstoque filter = new RemessaEstoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RemessaEstoque.class);
		filter.flTipoRemessa = parcial ? PedidoEstoqueDto.TIPOREMESSA_R : null;
		deleteAllByExample(filter);
	}
	
	public boolean isDevolucaoParcialPendente() throws SQLException {
		RemessaEstoque filter = new RemessaEstoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RemessaEstoque.class);
		filter.flTipoRemessa = PedidoEstoqueDto.TIPOREMESSA_R;
		return countByExample(filter) > 0;
	}
	
	public Vector validaAndGetListDevolucao(boolean parcial) throws SQLException {
		boolean isDevolucaoParcialPendente = RemessaEstoqueService.getInstance().isDevolucaoParcialPendente();
		if (!parcial && isDevolucaoParcialPendente) {
			throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_DEVOLUCAO_PARCIAL_PENDENTE);
		} else if (parcial && !isDevolucaoParcialPendente) {
			throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_SEM_DEVOLUCAO_PARCIAL);
		}
		Vector produtoList = ProdutoService.getInstance().getProdutoListDevolucao(parcial);
		if (!parcial && produtoList.size() == 0) {
			throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_SEM_DEVOLUCAO_TOTAL);
		}
		return produtoList;
	}

	protected Vector findAllRemessaEstoqueNoEquipamento() throws SQLException {
		return findAllByExample(getNewRemessaEstoqueFilter());
	}

	private RemessaEstoque getNewRemessaEstoqueFilter() {
		RemessaEstoque remessaEstoqueFilter = new RemessaEstoque();
		remessaEstoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		remessaEstoqueFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return remessaEstoqueFilter;
	}

	protected int qtdRemessaEstoqueFinalizada() throws SQLException {
		RemessaEstoque remessaEstoqueFilter = getNewRemessaEstoqueFilter();
		remessaEstoqueFilter.flFinalizada = RemessaEstoque.FL_FINALIZADA;
		return countByExample(remessaEstoqueFilter);
	}

	
	public int qtdEstoqueNaoLiberado() throws SQLException {
		RemessaEstoque remessaEstoqueFilter = getNewRemessaEstoqueFilter();
		remessaEstoqueFilter.flEstoqueLiberado = ValueUtil.VALOR_NAO;
		return countByExample(remessaEstoqueFilter);
	}

	public List<Object[]> buscaProdutosRemessaPor(String cdEmpresa, String cdRepresentante) throws SQLException {
		return RemessaEstoqueDbxDao.getInstance().buscaProdutosRemessaPor(cdEmpresa, cdRepresentante);
	}
	
	public List<RemessaEstoque> buscaRemessaLiberadasPor(String cdEmpresa, String cdRepresentante) throws SQLException {
		return RemessaEstoqueDbxDao.getInstance().buscaRemessaLiberadasPor(cdEmpresa, cdRepresentante);
	}
	public List<RemessaEstoque> findRemessaEstoqueDevolucao(String cdEmpresa, String cdRepresentante) throws SQLException {
		return RemessaEstoqueDbxDao.getInstance().findRemessaEstoqueDevolucao(cdEmpresa, cdRepresentante);
	}
}