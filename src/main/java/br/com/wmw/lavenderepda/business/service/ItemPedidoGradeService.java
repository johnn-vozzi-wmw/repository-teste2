package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoGradeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoGradeDbxDao;
import totalcross.util.Vector;

public class ItemPedidoGradeService extends CrudService {

    private static ItemPedidoGradeService instance;

    private ItemPedidoGradeService() {
        //--
    }

    public static ItemPedidoGradeService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoGradeService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ItemPedidoGradeDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	//@Override
	public Vector findAllByExampleUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedidoGrade)domain).flOrigemPedido)) {
			return ItemPedidoGradeDbxDao.getInstance().findAllByExampleUnique(domain);
		} else {
			return ItemPedidoGradeDbxDao.getInstanceErp().findAllByExampleUnique(domain);
		}
	}

	//@Override
	public Vector findAllByExampleSummaryUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemPedidoGrade)domain).flOrigemPedido)) {
			return ItemPedidoGradeDbxDao.getInstance().findAllByExampleSummaryUnique(domain);
		} else {
			return ItemPedidoGradeDbxDao.getInstanceErp().findAllByExampleSummaryUnique(domain);
		}
	}

	protected void setDadosAlteracao(BaseDomain domain) {
		domain.cdUsuario = Session.getCdUsuario();
	}

    public void insert(ItemPedido itemPedido, Vector itemPedidoGradeList) throws SQLException {
    	for (int i = 0; i < itemPedidoGradeList.size(); i++) {
    		ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade)itemPedidoGradeList.items[i];
    		itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
    		super.insert(itemPedidoGrade);
		}
    }

    public void update(ItemPedido itemPedido, Vector itemPedidoGradeList) throws SQLException {
    	ItemPedidoGrade itemPedidoGradeExample = getItemPedidoGradeFilter(itemPedido);
    	deleteAllByExample(itemPedidoGradeExample);
    	for (int i = 0; i < itemPedidoGradeList.size(); i++) {
    		insert((ItemPedidoGrade)itemPedidoGradeList.items[i]);
    	}
    }

    public void delete(Vector itemPedidoGradeList) throws SQLException {
    	for (int i = 0; i < itemPedidoGradeList.size(); i++) {
    		super.delete((ItemPedidoGrade)itemPedidoGradeList.items[i]);
    	}
    }
    
    public void deleteByItemPedido(ItemPedido itemPedido) throws SQLException {
    	ItemPedidoGrade itemPedidoGrade = getItemPedidoGradeFilter(itemPedido);
    	deleteAllByExample(itemPedidoGrade);
    }

    public void findItemPedidoGradeList(ItemPedido itemPedido) throws SQLException {
    	ItemPedidoGrade itemPedidoGrade = getItemPedidoGradeFilter(itemPedido);
		itemPedido.itemPedidoGradeList = findAllByExampleUnique(itemPedidoGrade);
	}

	private ItemPedidoGrade getItemPedidoGradeFilter(ItemPedido itemPedido) {
		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
		itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoGrade.flOrigemPedido = itemPedido.flOrigemPedido;
		itemPedidoGrade.nuPedido = itemPedido.nuPedido;
		itemPedidoGrade.cdProduto = itemPedido.cdProduto;
		itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
		return itemPedidoGrade;
	}

	public void findItemPedidoGradeListWithGradeDates(ItemPedido itemPedido) throws SQLException {
		ItemPedidoGrade itemPedidoGrade = getItemPedidoGradeFilter(itemPedido);
		ProdutoGrade produtoGrade = new ProdutoGrade();
		produtoGrade.cdTabelaPreco = itemPedido.cdTabelaPreco;
		itemPedido.itemPedidoGradeList = ProdutoGradeDbxDao.getInstance().findAllItemPedidoGradePorItemPedido(itemPedidoGrade, produtoGrade);
	}

    public String getDescricaoItemPedidoNivel1(ItemPedido itemPedido) throws SQLException {
    	StringBuffer sb = new StringBuffer(200);
    	sb.append(ProdutoService.getInstance().getDsProduto(itemPedido.cdProduto));
    	String cdTipoItemGrade1 = ProdutoGradeService.getInstance().getCdTipoItemGrade1ByItemPedido(itemPedido);
    	if (cdTipoItemGrade1 != null) {
    		sb.append(" (");
    		sb.append(TipoItemGradeService.getInstance().getDsTipoItemGrade(cdTipoItemGrade1));
    		sb.append(" ");
    		sb.append(ItemGradeService.getInstance().getDsItemGrade(cdTipoItemGrade1, itemPedido.cdItemGrade1));
    		sb.append(")");
    	}
    	return sb.toString();
    }

    public String getDescricaoProdutoGradeNivel1(ItemPedidoGrade itemPedidoGrade, String cdTabelaPreco) throws SQLException {
    	StringBuffer sb = new StringBuffer(200);
    	sb.append(ProdutoService.getInstance().getDsProduto(itemPedidoGrade.cdProduto));
    	//--
    	String cdTipoItemGrade1 = ProdutoGradeService.getInstance().getCdTipoItemGrade1ByItemPedidoGrade(itemPedidoGrade, cdTabelaPreco);
    	if (cdTipoItemGrade1 != null) {
    		sb.append(" (");
    		sb.append(TipoItemGradeService.getInstance().getDsTipoItemGrade(cdTipoItemGrade1));
    		sb.append(" ");
    		sb.append(ItemGradeService.getInstance().getDsItemGrade(cdTipoItemGrade1, itemPedidoGrade.cdItemGrade1));
    		sb.append(")");
    	}
    	return sb.toString();
    }

    public String getDescricaoProdutoGradeCompleta(ItemPedidoGrade itemPedidoGrade) throws SQLException {
    	StringBuffer sb = new StringBuffer(200);
    	sb.append(ProdutoService.getInstance().getDsProduto(itemPedidoGrade.cdProduto));
    	//--
    	ProdutoGrade produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeByItemPedidoGrade(itemPedidoGrade);
    	if (produtoGrade != null) {
    		sb.append(" (");
    		sb.append(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtoGrade.cdTipoItemGrade1));
    		sb.append(" ");
    		sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade1, itemPedidoGrade.cdItemGrade1));
    		if (ValueUtil.isNotEmpty(produtoGrade.cdTipoItemGrade2) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdTipoItemGrade2)) {
    			sb.append(" - ");
    			sb.append(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtoGrade.cdTipoItemGrade2));
    			sb.append(" ");
    			sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade2, itemPedidoGrade.cdItemGrade2));
    			if (ValueUtil.isNotEmpty(produtoGrade.cdTipoItemGrade3) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdTipoItemGrade3)) {
    				sb.append(" - ");
    				sb.append(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtoGrade.cdTipoItemGrade3));
    				sb.append(" ");
    				sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade3, itemPedidoGrade.cdItemGrade3));
    			}
    		}
    		sb.append(")");
    	}
    	return sb.toString();
    }

    public String getDescricaoProdutoGradeResumida(ItemPedidoGrade itemPedidoGrade) throws SQLException {
    	StringBuffer sb = new StringBuffer(200);
    	sb.append(ProdutoService.getInstance().getDsProduto(itemPedidoGrade.cdProduto));
    	sb.append(getDescricaoGradeResumida(itemPedidoGrade));
    	return sb.toString();
    }

    public String getDescricaoGradeResumida(ItemPedidoGrade itemPedidoGrade) throws SQLException {
    	StringBuffer sb = new StringBuffer(200);
    	ProdutoGrade produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeByItemPedidoGrade(itemPedidoGrade);
    	if (produtoGrade != null) {
    		sb.append(" (");
    		sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade1, itemPedidoGrade.cdItemGrade1));
    		if (ValueUtil.isNotEmpty(produtoGrade.cdTipoItemGrade2) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdTipoItemGrade2)) {
    			sb.append(" - ");
    			sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade2, itemPedidoGrade.cdItemGrade2));
    			if (ValueUtil.isNotEmpty(produtoGrade.cdTipoItemGrade3) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdTipoItemGrade3)) {
    				sb.append(" - ");
    				sb.append(ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade3, itemPedidoGrade.cdItemGrade3));
    			}
    		}
    		sb.append(")");
    	}
    	return sb.toString();
    }
    
    public Vector getItemPedidoGradeByItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
		itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoGrade.cdProduto = itemPedido.cdProduto;
		itemPedidoGrade.nuPedido = itemPedido.nuPedido;
		itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
		itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoGrade.cdItemGrade1 = LavenderePdaConfig.usaGradeProduto2() ? null : itemPedido.cdItemGrade1;
		itemPedidoGrade.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		return findAllByExampleUnique(itemPedidoGrade);
    }
    
    public ItemPedidoGrade montaItemPedidoGradeByItemPedido(ItemPedido itemPedido) {
    	ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
    	itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
    	itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
    	itemPedidoGrade.cdProduto = itemPedido.cdProduto;
    	itemPedidoGrade.nuPedido = itemPedido.nuPedido;
    	itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
    	itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoGrade.cdItemGrade1 = LavenderePdaConfig.usaGradeProduto2() ? null : itemPedido.cdItemGrade1;
		itemPedidoGrade.cdItemGrade2 = itemPedido.cdItemGrade2;
		itemPedidoGrade.cdItemGrade3 = itemPedido.cdItemGrade3;
    	itemPedidoGrade.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	return itemPedidoGrade;
    }

	public void ajustaItemPedidoGrade(ItemPedido itemPedido, Pedido pedido) {
		for (int i = 0; i < itemPedido.itemPedidoGradeList.size(); i++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[i];
			itemPedidoGrade.flOrigemPedido = pedido.flOrigemPedido;
			itemPedidoGrade.nuPedido = pedido.nuPedido;
			itemPedidoGrade.cdRepresentante = pedido.cdRepresentante;
		}
	}
	
	public String getCdTabelaPreco(ItemPedidoGrade itemPedidoGrade) throws SQLException {
    	if (itemPedidoGrade != null) {
    		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
    			ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByItemPedidoGrade(itemPedidoGrade);
    			if (itemPedido != null) {
    				return itemPedido.cdTabelaPreco;
    			} else {
    				Pedido pedido = PedidoService.getInstance().findPedidoByItemPedidoGrade(itemPedidoGrade);
    				if (pedido != null) {
    					return pedido.cdTabelaPreco;
    				}
    			}
    		} else {
    			Pedido pedido = PedidoService.getInstance().findPedidoByItemPedidoGrade(itemPedidoGrade);
    			if (pedido != null) {
					return pedido.cdTabelaPreco;
				}
    		}
    	}
    	return ValueUtil.VALOR_NI;
    }
	
	public boolean hasItemPedidoGrade(ItemPedido itemPedido) throws SQLException {
		return hasItemPedidoGrade(itemPedido.cdProduto, itemPedido.nuPedido, itemPedido.flOrigemPedido, itemPedido.flTipoItemPedido);
	}

	public boolean hasItemPedidoGrade(String cdProduto, String nuPedido, String flOrigemPedido, String flTipoItemPedido) throws SQLException {
		ItemPedidoGrade itemPedidoGradeFilter = getItemPedidoGradeFilter(cdProduto, nuPedido, flOrigemPedido, flTipoItemPedido);
		return countByExample(itemPedidoGradeFilter) > 0;
	}

	private ItemPedidoGrade getItemPedidoGradeFilter(String cdProduto, String nuPedido, String flOrigemPedido, String flTipoItemPedido) {
		ItemPedidoGrade itemPedidoGradeFilter = new ItemPedidoGrade();
		itemPedidoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemPedidoGradeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ItemPedidoGrade.class);
		itemPedidoGradeFilter.flOrigemPedido = flOrigemPedido;
		itemPedidoGradeFilter.nuPedido = nuPedido;
		itemPedidoGradeFilter.cdProduto = cdProduto;
		itemPedidoGradeFilter.flTipoItemPedido = flTipoItemPedido;
		return itemPedidoGradeFilter;
	}

	public boolean hasItemPedidoGradeNivel3(String cdProduto, String nuPedido, String flOrigemPedido, String flTipoItemPedido) throws SQLException {
		ItemPedidoGrade itemPedidoGradeFilter = getItemPedidoGradeFilter(cdProduto, nuPedido, flOrigemPedido, flTipoItemPedido);
		itemPedidoGradeFilter.filtraItemPedGradeNivel3 = true;
		return countByExample(itemPedidoGradeFilter) > 0;
	}
	
	public int verificaInconsistenciasGrade(Pedido pedido) throws SQLException {
		ItemPedido itemPedido = ItemPedidoService.getInstance().getNewItemPedido(pedido);
		return verificaInconsistenciasGrade(itemPedido);
	}
	
	public int verificaInconsistenciasGrade(ItemPedido itemPedido) throws SQLException {
		ItemPedidoGrade itemPedidoGradeFilter = getItemPedidoGradeFilter(itemPedido.cdProduto, itemPedido.nuPedido,itemPedido.flOrigemPedido, itemPedido.flTipoItemPedido);
		Vector itemPedidoGradeList = findAllByExample(itemPedidoGradeFilter);
		Vector itemPedidoGradeSemInconsistenciasList = ItemPedidoGradeDbxDao.getInstance().verificaInconsistenciasGrade(itemPedido);
		if (itemPedidoGradeList.size() == itemPedidoGradeSemInconsistenciasList.size()) {
			return ItemGrade.ITEMGRADELIST_SEM_PROBLEMA_COM_GRADES;
		} 
		if (itemPedidoGradeSemInconsistenciasList.size() > 0) {
			return ItemGrade.ITEMGRADELIST_PROBLEMA_EM_ALGUMAS_GRADES;
		} 
		return ItemGrade.ITEMGRADELIST_PROBLEMA_EM_TODAS_AS_GRADES;
	}
	
}