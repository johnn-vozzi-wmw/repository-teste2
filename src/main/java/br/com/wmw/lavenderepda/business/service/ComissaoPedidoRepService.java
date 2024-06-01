package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComissaoPedidoRep;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComissaoPedidoRepDbxDao;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;

public class ComissaoPedidoRepService extends CrudService {

	public static final String IMAGES_ICONE_COMISSAO_PNG = "images/comissaoPedidoRepIcon.png";
    private static ComissaoPedidoRepService instance;
    
    private ComissaoPedidoRepService() {}
    
    @Override
    public void validate(BaseDomain domain) {}
    
    public static ComissaoPedidoRepService getInstance() {
        return (instance == null) ? instance = new ComissaoPedidoRepService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ComissaoPedidoRepDbxDao.getInstance();
    }
    
    private ComissaoPedidoRep getComissaoPedidoRepFilter(final String cdEmpresa, final String cdRepresentante, final String cdTabelaPreco, final double vlPctComissaoLimite, final Integer cdComissaoPedidoRep) {
    	if (ValueUtil.isEmpty(cdEmpresa) || ValueUtil.isEmpty(cdRepresentante) || ValueUtil.isEmpty(cdTabelaPreco)) return null;
    	ComissaoPedidoRep comissaoPedidoRepFilter = new ComissaoPedidoRep();
    	comissaoPedidoRepFilter.cdEmpresa = cdEmpresa;
    	comissaoPedidoRepFilter.cdRepresentante = cdRepresentante;
    	comissaoPedidoRepFilter.cdTabelaPreco = cdTabelaPreco;
    	comissaoPedidoRepFilter.vlPctMaxComissao = vlPctComissaoLimite; 
    	if (cdComissaoPedidoRep != null) {
    		comissaoPedidoRepFilter.cdComissaoPedidoRep = cdComissaoPedidoRep;
    	}
    	return comissaoPedidoRepFilter;
    }
    
    private ComissaoPedidoRep getComissaoPedidoRepByPedido(final Pedido pedido) throws SQLException {
    	if (pedido == null) return null;
    	ComissaoPedidoRep comissaoPedidoRepFilter = getComissaoPedidoRepFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdTabelaPreco, pedido.vlPctComissaoPedido, (!pedido.isPedidoAberto()) ? pedido.cdComissaoPedidoRep : null);
    	if (comissaoPedidoRepFilter == null) return null;
    	return (!pedido.isPedidoAberto()) ? (ComissaoPedidoRep) findByPrimaryKey(comissaoPedidoRepFilter) : ComissaoPedidoRepDbxDao.getInstance().findComissaoByPercentual(comissaoPedidoRepFilter);
    }
    
    private ComissaoPedidoRep getComissaoPedidoRepByItemPedido(final ItemPedido itemPedido) throws SQLException {
    	if (itemPedido == null) return null;
    	ComissaoPedidoRep comissaoPedidoRepFilter = getComissaoPedidoRepFilter(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdTabelaPreco, itemPedido.vlPctComissao, (!itemPedido.pedido.isPedidoAberto()) ? itemPedido.cdComissaoPedidoRep : null);
    	if (comissaoPedidoRepFilter == null) return null;
		return (!itemPedido.pedido.isPedidoAberto()) ? (ComissaoPedidoRep) findByPrimaryKey(comissaoPedidoRepFilter) : ComissaoPedidoRepDbxDao.getInstance().findComissaoByPercentual(comissaoPedidoRepFilter);
    }
    
    public void applyComissaoPedidoRepInPedido(Pedido pedido) throws SQLException {
    	ComissaoPedidoRep comissaoPedidoRep = getComissaoPedidoRepByPedido(pedido);
    	if (comissaoPedidoRep == null) return;
    	pedido.comissaoPedidoRep = comissaoPedidoRep;
    	pedido.cdComissaoPedidoRep = comissaoPedidoRep.cdComissaoPedidoRep;
	}
    
    public void applyComissaoPedidoRepInItemPedido(ItemPedido itemPedido) throws SQLException {
    	ComissaoPedidoRep comissaoPedidoRep = getComissaoPedidoRepByItemPedido(itemPedido);
    	if (comissaoPedidoRep == null) return;
    	itemPedido.comissaoPedidoRep = comissaoPedidoRep;
    	itemPedido.cdComissaoPedidoRep = comissaoPedidoRep.cdComissaoPedidoRep;
    }
    
    public Image getIconComissao(final ComissaoPedidoRep comissaoPedidoRep, final boolean isCadItemPedidoForm) throws SQLException {
    	if (!isCadItemPedidoForm) {
    		return UiUtil.getColorfulImage(IMAGES_ICONE_COMISSAO_PNG, UiUtil.getLabelPreferredHeight() / 7 * 6, UiUtil.getLabelPreferredHeight() / 7 * 6, (comissaoPedidoRep != null) ? getCorIconeComissao(comissaoPedidoRep) : Color.BRIGHT);
    	}
    	return UiUtil.getIconButtonAction(IMAGES_ICONE_COMISSAO_PNG, (comissaoPedidoRep != null) ? getCorIconeComissao(comissaoPedidoRep) : Color.BRIGHT);
    }
    
    public int getCorIconeComissao(final ComissaoPedidoRep comissaoPedidoRep) {
		return (comissaoPedidoRep != null) ? Color.getRGB(comissaoPedidoRep.corSistema.vlR, comissaoPedidoRep.corSistema.vlG, comissaoPedidoRep.corSistema.vlB) : 0;
    }
    
 
}