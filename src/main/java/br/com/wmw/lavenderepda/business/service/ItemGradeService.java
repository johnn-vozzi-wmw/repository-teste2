package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemGradeDbxDao;
import totalcross.util.Vector;

public class ItemGradeService extends CrudService {

    private static ItemGradeService instance;

    private ItemGradeService() {
        //--
    }

    public static ItemGradeService getInstance() {
        if (instance == null) {
            instance = new ItemGradeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemGradeDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public ItemGrade getItemGrade(String cdTipoItemGrade, String cdItemGrade) throws SQLException {
    	return (ItemGrade)findByRowKey(generateTipoItemGradeFilter(cdTipoItemGrade, cdItemGrade).getRowKey());
    }
    
    public String getDsItemGrade(String cdTipoItemGrade, String cdItemGrade) throws SQLException {
    	String dsTipoItemGrade = findColumnByRowKey(generateTipoItemGradeFilter(cdTipoItemGrade, cdItemGrade).getRowKey(), "DSITEMGRADE");
    	if (!ValueUtil.isEmpty(dsTipoItemGrade)) {
    		return dsTipoItemGrade;
    	} else {
    		return StringUtil.getStringValue(cdTipoItemGrade);
    	}
    }

	private ItemGrade generateTipoItemGradeFilter(String cdTipoItemGrade, String cdItemGrade) {
		ItemGrade tipoItemGrade = new ItemGrade();
		tipoItemGrade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoItemGrade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		tipoItemGrade.cdTipoItemGrade = cdTipoItemGrade;
		tipoItemGrade.cdItemGrade = cdItemGrade;
		return tipoItemGrade;
	}
	
    public Vector findGradeEstoqueForList(int nuGrade, String cdProduto, String cdItemGrade1, String cdTabelaPreco) throws SQLException {
    	if(nuGrade == 3) {
    		return ItemGradeDbxDao.getInstance().findGradeEstoque3ForList(cdProduto, cdItemGrade1, (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) ? cdTabelaPreco : ProdutoGrade.CDTABELAPRECO_PADRAO);
    	}
    	if(nuGrade == 2) {
    		return ItemGradeDbxDao.getInstance().findGradeEstoque2ForList(cdProduto, (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) ? cdTabelaPreco : ProdutoGrade.CDTABELAPRECO_PADRAO);
    	}
		return null;
    }

	public Vector findAllItemGradeNivel2(String cdItemGrade1, String cdProduto) throws SQLException {
		ProdutoGrade produtoGradeFilter = new ProdutoGrade();
		produtoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoGradeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ItemGrade.class);
		produtoGradeFilter.cdProduto = cdProduto;
		produtoGradeFilter.cdItemGrade1 = cdItemGrade1;
		return ItemGradeDbxDao.getInstance().findAllItemGradeNivel2(produtoGradeFilter);
	}
	
	public Vector filtraItemGradeListPorItemPedido(Vector itemGradeList, Vector itemPedidoList, boolean grade3) {
		int size = itemGradeList.size();
		int size2 = itemPedidoList.size();
		Vector filterGradeList = new Vector(size);
		for (int i = 0; i < size; i++) {
			ItemGrade itemGrade = (ItemGrade) itemGradeList.items[i];
			if (isItemGradeListaItemPedido(itemPedidoList, itemGrade.cdItemGrade, size2, grade3)) filterGradeList.addElement(itemGrade);
		}
		return filterGradeList;
	}
	
	private boolean isItemGradeListaItemPedido(Vector itemPedidoList, String cdItemGrade, int size, boolean grade3) {
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.qtItemFisico > 0d && ((!grade3 && ValueUtil.valueEquals(cdItemGrade, itemPedido.cdItemGrade2)) || (grade3 && ValueUtil.valueEquals(cdItemGrade, itemPedido.cdItemGrade3))))
				return true;
		}
		return false;
	}
	
	public boolean validateEstoqueItemAgrGrade(ItemPedido itemPedido) throws SQLException {
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoGrade = (ItemPedido)itemPedido.itemPedidoPorGradePrecoList.items[i];
			try {
				EstoqueService.getInstance().validateEstoque(itemPedidoGrade.pedido, itemPedidoGrade, false);
			} catch (ValidationException e) {
				if (permiteOuIgnoraEstoqueNegativo(itemPedidoGrade)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean permiteOuIgnoraEstoqueNegativo(ItemPedido itemPedido) throws SQLException {
		Produto produto = ProdutoService.getInstance().getProduto(itemPedido.cdProduto);
		return produto != null && produto.isPermiteEstoqueNegativo();
	}
	
}