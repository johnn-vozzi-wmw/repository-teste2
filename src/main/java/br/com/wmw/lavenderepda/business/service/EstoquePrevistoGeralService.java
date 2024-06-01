package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.validation.EstoquePrevistoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoquePrevistoGeralDao;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;

public class EstoquePrevistoGeralService extends CrudService {

    private static EstoquePrevistoGeralService instance = null;

    private EstoquePrevistoGeralService() { }
    
    public static EstoquePrevistoGeralService getInstance() {
        if (instance == null) {
            instance = new EstoquePrevistoGeralService();
        }
        return instance;
    }

	@Override
    protected CrudDao getCrudDao() {
        return EstoquePrevistoGeralDao.getInstance();
    }

	@Override
    public void validate(BaseDomain domain) { }

	public Double validaEstoquePrevisto(Date dtEntrega, ItemPedido itemPedido) throws SQLException {
		EstoquePrevistoGeral estoquePrevistoGeralFilter = new EstoquePrevistoGeral(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto, dtEntrega);
		Double qtdEstoquePrevisto = EstoquePrevistoGeralDao.getInstance().getQtdEstoquePrevisto(estoquePrevistoGeralFilter);
		if (qtdEstoquePrevisto == null) return null;
		
		if (itemPedido.getQtItemFisico() > qtdEstoquePrevisto) {
			String mensagem = getMensagemValidacaoEstoque(itemPedido.getDsProduto(), qtdEstoquePrevisto, itemPedido.getProduto().dsUnidadeFisica);
			throw new EstoquePrevistoException(mensagem);
		}
		return qtdEstoquePrevisto;
	}

	public Vector getErroEstoqueInsuficienteList(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.usaControleEstoquePorEstoquePrevisto()) return null;
		
		Vector itemPedidoList = pedido.itemPedidoList;
		if (ValueUtil.isEmpty(itemPedidoList)) return null;
		
		Date dtEntrega = pedido.dtEntrega;
		if (ValueUtil.isEmpty(dtEntrega)) return null;
		
		Vector erroEstoquePrevistoInsuficienteList = new Vector();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			try {
				validaEstoquePrevisto(dtEntrega, itemPedido);
			} catch (EstoquePrevistoException ex) {
				ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, ex.getMessage());
				erroEstoquePrevistoInsuficienteList.addElement(produtoErro);
			}
		}
		return erroEstoquePrevistoInsuficienteList;
	}
	
	private String getMensagemValidacaoEstoque(String dsProduto, double qtdEstoquePrevisto, String dsUnidadeFisica) {
		if (qtdEstoquePrevisto < 0) return MessageUtil.getMessage(Messages.ESTOQUE_PREVISTO_ESTOQUE_NEGATIVO, new Object[]{dsProduto, dsUnidadeFisica});
		
		String qtdEstoquePrevistoInteiro = StringUtil.getStringValueToInterface((int) qtdEstoquePrevisto);
		return MessageUtil.getMessage(Messages.ESTOQUE_PREVISTO_INSUFICIENTE, new Object[]{dsProduto, qtdEstoquePrevistoInteiro, dsUnidadeFisica});
	}
	
	public Vector findAllNaoVencidosSomados(EstoquePrevistoGeral estoquePrevistoGeralFilter) throws SQLException {
	   	return EstoquePrevistoGeralDao.getInstance().findAllNaoVencidosSomados(estoquePrevistoGeralFilter);

	}

}