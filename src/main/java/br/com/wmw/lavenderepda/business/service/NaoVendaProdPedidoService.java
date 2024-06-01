package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.NaoVendaProdPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GiroProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NaoVendaProdPedidoDao;
import totalcross.util.Vector;

public class NaoVendaProdPedidoService extends CrudService {

    private static NaoVendaProdPedidoService instance;
    
    private NaoVendaProdPedidoService() {}
    
    public static NaoVendaProdPedidoService getInstance() {
        if (instance == null) {
            instance = new NaoVendaProdPedidoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return NaoVendaProdPedidoDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	NaoVendaProdPedido naoVendaProdPedido = (NaoVendaProdPedido) domain;
    	if (ValueUtil.isEmpty(naoVendaProdPedido.cdMotivo)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
		}
    	String dsJustificativa = ValueUtil.removeWhitespace(naoVendaProdPedido.dsJustificativa);
    	if (naoVendaProdPedido.getMotNaoVendaProduto().isExigeJustificativa() && ValueUtil.isEmpty(dsJustificativa)) {
    		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.JUSTIFIQUE_MOTIVO_NAO_VENDA_PRODUTO);
    	}
    }
    
    public boolean isPossuiProdutoNaoVendidos(Pedido pedido) throws SQLException {
    	pedido.produtosNaoInseridos = new Vector();
		boolean possuiPendenteGiroProduto =  isPossuiPendenteGiroProduto(pedido);
		boolean possuiSugestaoVendaComCadastro = isPossuiSugestaoVendaComCadastro(pedido);
		return possuiPendenteGiroProduto || possuiSugestaoVendaComCadastro;
	}

	private boolean isPossuiPendenteGiroProduto(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaGiroProduto() || !LavenderePdaConfig.isApresentaRelGiroProdutoFechamentoPedido()) return false;
		
		Vector giroProdutos = getGiroProdutosNaoVendidos(pedido);
		return ValueUtil.isNotEmpty(giroProdutos);
	}
	
	private Vector getGiroProdutosNaoVendidos(Pedido pedido) throws SQLException {
		GiroProduto giroProdutoFilter = new GiroProduto(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente);
		giroProdutoFilter.itemPedidoFilter = new ItemPedido();
    	giroProdutoFilter.itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
    	giroProdutoFilter.itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
    	giroProdutoFilter.itemPedidoFilter.nuPedidoListFilter = new String[] {pedido.nuPedido};
    	if (LavenderePdaConfig.usaApenasGiroProdutoPorTabelaPrecoPedido) {
    		giroProdutoFilter.cdTabelaPrecoList = new String[] {pedido.cdTabelaPreco, TabelaPreco.CDTABELAPRECO_VALOR_ZERO};
    	}
    	if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
    		giroProdutoFilter.produtoFilter.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
    		giroProdutoFilter.produtoFilter.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		Vector giroProdutos = GiroProdutoPdbxDao.getInstance().findAllByItemPedidoNaoInseridoList(pedido, giroProdutoFilter);
		if (giroProdutos == null) return new Vector();
		
		adicionaProdutoNaoInseridoNaLista(pedido, giroProdutos);
		return giroProdutos;
	}

	private void removeProdutosNaoExistentesNaTabelaProduto(Vector produtos) throws SQLException {
		int size = produtos.size();
		for (int i = 0; i < size; i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) produtos.items[i];
			String dsProduto = ProdutoService.getInstance().getDsProduto(sugestaoVendaProd.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, sugestaoVendaProd.cdProduto);
			if (ValueUtil.isEmpty(dsProduto) || ValueUtil.valueEquals(dsProduto, sugestaoVendaProd.cdProduto)) {
				produtos.removeElementAt(i);
				size--;
				i--;
			}
		}
	}
	
	private boolean isPossuiSugestaoVendaComCadastro(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido) || pedido.isPedidoTroca() || pedido.isPedidoBonificacao()) {
			return false;
		}
		Vector sugestaoVendaProds = getSugestaoVendaProdsNaoVendidos(pedido);
		return ValueUtil.isNotEmpty(sugestaoVendaProds);
	}
	
	private Vector getSugestaoVendaProdsNaoVendidos(Pedido pedido) throws SQLException {
		Vector sugestaoVendas = SugestaoVendaService.getInstance().findAllSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, false, true);
		if (ValueUtil.isEmpty(sugestaoVendas)) {
			sugestaoVendas =  SugestaoVendaService.getInstance().findAllSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true, true);
		}
		if (ValueUtil.isEmpty(sugestaoVendas)) return new Vector();
		
		Vector sugestaoVendaProds = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdSemQtdPendenteNoPedido(sugestaoVendas, pedido);
		if (sugestaoVendaProds == null) return new Vector();
		
		removeProdutosInseridosNaTabelaNaoVendaProdPedido(sugestaoVendaProds, pedido);
		removeProdutosNaoExistentesNaTabelaProduto(sugestaoVendaProds);
		adicionaProdutoNaoInseridoNaLista(pedido, sugestaoVendaProds);
		return sugestaoVendaProds;
	}
	
	private void adicionaProdutoNaoInseridoNaLista(Pedido pedido, Vector produtos) throws SQLException {
		int size = produtos.size();
		Vector produtoNaoInseridos = pedido.produtosNaoInseridos;
		for (int i = 0; i < size; i++) {
			Produto produtoNaoAdicionado = new Produto(pedido.cdEmpresa, pedido.cdRepresentante);
			if (produtos.items[i] instanceof GiroProduto) {
				GiroProduto giroProduto =(GiroProduto) produtos.items[i];
				produtoNaoAdicionado.cdProduto = giroProduto.cdProduto;
			} else {
				SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) produtos.items[i];
				produtoNaoAdicionado.cdProduto = sugestaoVendaProd.cdProduto;
			}
			produtoNaoAdicionado.dsProduto = ProdutoService.getInstance().getDsProduto(pedido.cdEmpresa, pedido.cdRepresentante, produtoNaoAdicionado.cdProduto);
			if (produtoNaoInseridos.contains(produtoNaoAdicionado)) return;
			
			produtoNaoInseridos.addElement(produtoNaoAdicionado);
		}
	}
	
	private void removeProdutosInseridosNaTabelaNaoVendaProdPedido(Vector sugestaoVendaProds, Pedido pedido) throws SQLException {
		Vector naoVendaProdPedidos = findAllByExample(new NaoVendaProdPedido(pedido, null));
		if (ValueUtil.isEmpty(naoVendaProdPedidos)) return;
		
		int size = sugestaoVendaProds.size();
		for (int i = 0; i < size; i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProds.items[i];
			if (sugestaoVendaProd == null) continue;
			NaoVendaProdPedido naoVendaProdPedidoFilter = new NaoVendaProdPedido(pedido, sugestaoVendaProd.cdProduto);
			if (!naoVendaProdPedidos.contains(naoVendaProdPedidoFilter)) continue;
			
			sugestaoVendaProds.removeElementAt(i);
			i--;
		}
	}

	public void removeProduto(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaRegistroMotivoNaoVendaProdutoSugerido || itemPedido == null || itemPedido.pedido == null) return ;
		
		NaoVendaProdPedido naoVendaProdPedido = (NaoVendaProdPedido) findByPrimaryKey(new NaoVendaProdPedido(itemPedido.pedido, itemPedido.cdProduto));
		if (naoVendaProdPedido == null) return;
		
		delete(naoVendaProdPedido);
	}

	public void deleteByPedido(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaRegistroMotivoNaoVendaProdutoSugerido) return;
		
		NaoVendaProdPedido naoVendaProdPedido = new NaoVendaProdPedido(pedido, null);
		deleteAllByExample(naoVendaProdPedido);
	}

}