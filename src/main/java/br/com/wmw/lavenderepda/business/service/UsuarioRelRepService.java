package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.UsuarioRelRepBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoMargem;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioRelRepDao;
import totalcross.util.Vector;

public class UsuarioRelRepService extends CrudService {

    private static UsuarioRelRepService instance;
    
    private UsuarioRelRepService() {
        //--
    }
    
    public static UsuarioRelRepService getInstance() {
        if (instance == null) {
            instance = new UsuarioRelRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioRelRepDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public boolean isDescontoPermitidoParaUsuarioSelecionado(ItemPedido itemPedido, String cdUsuarioSelecionado, double vlItemInformado) throws SQLException {
    	UsuarioRelRep usuarioRelRep = (UsuarioRelRep) findByRowKey(new UsuarioRelRepBuilder(itemPedido.cdEmpresa, itemPedido.cdRepresentante, cdUsuarioSelecionado).build().getRowKey());
    	if (usuarioRelRep != null && itemPedido != null) {
    		return isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado);
    	}
    	return false;
    }
    
    protected boolean isDescontoPermitidoParaUsuarioSelecionado(UsuarioRelRep usuarioRelRep, ItemPedido itemPedido, double vlItemInformado) throws SQLException {
    	if (itemPedido.vlBaseItemPedido < vlItemInformado) {
    		double vlItemPedidoMaxPermitido = itemPedido.vlBaseItemPedido + itemPedido.vlBaseItemPedido * usuarioRelRep.vlPctMaxAcrescimo / 100;
    		vlItemPedidoMaxPermitido = ItemPedidoService.getInstance().getVlItemPedidoMaxDescAcrescPermitido(itemPedido, vlItemPedidoMaxPermitido, usuarioRelRep.vlPctMaxAcrescimo, false);
    		return ValueUtil.round(vlItemInformado) <= ValueUtil.round(vlItemPedidoMaxPermitido);
    	} else {
    		double vlPctMaxDescontoUsuario = usuarioRelRep.vlPctMaxDesconto + getVlPctMargemProduto(itemPedido);
    		double vlItemPedidoMinPermitido = itemPedido.vlBaseItemPedido -  itemPedido.vlBaseItemPedido * vlPctMaxDescontoUsuario / 100;
    		vlItemPedidoMinPermitido = ItemPedidoService.getInstance().getVlItemPedidoMaxDescAcrescPermitido(itemPedido, vlItemPedidoMinPermitido, vlPctMaxDescontoUsuario, true);
    		return ValueUtil.round(vlItemInformado) >= ValueUtil.round(vlItemPedidoMinPermitido);
    	}
    }
    
    public Double getVlPctMargemProduto(ItemPedido itemPedido) throws SQLException {
    	double vlPctMaxDescontoItem = 0d;
    	if (LavenderePdaConfig.getVlPctMargemDescontoItemPedido() > 0) {
			vlPctMaxDescontoItem = LavenderePdaConfig.getVlPctMargemDescontoItemPedido();
			ProdutoMargem produtoMargem = ProdutoMargemService.getInstance().getProdutoMargem(itemPedido.getProduto(), itemPedido.pedido.getCliente().cdRamoAtividade);
			if (produtoMargem != null) {
				vlPctMaxDescontoItem = produtoMargem.vlPctMargemDesconto;
			}
		}
    	return vlPctMaxDescontoItem;
    }


	public Vector findUsuarioRelRep(boolean liberacaoPorUsuarioEAlcada) throws SQLException {
		UsuarioRelRep usuarioRelRepFilter = new UsuarioRelRep();
		if (liberacaoPorUsuarioEAlcada) {
			Cliente cliente = SessionLavenderePda.getCliente();
			double vlSaldoCliente = FichaFinanceiraService.getInstance().getVlSaldoCliente(cliente, null) * -1;
			double vlLimiteCredito = cliente.vlLimiteCredito;
			usuarioRelRepFilter.filtraLiberacaoPorUsuarioEAlcada = true;
			usuarioRelRepFilter.vlPctAlcadaLibLimiteCredito = vlLimiteCredito > 0 ? ValueUtil.round( vlSaldoCliente * 100 / cliente.vlLimiteCredito ) : 0; 
		}
		return findAllByExample(usuarioRelRepFilter);
	}
    
}