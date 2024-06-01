package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VerbaItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaItemPedidoPdbxDao;

public class VerbaItemPedidoService extends CrudService {

    private static VerbaItemPedidoService instance;

    private VerbaItemPedidoService() {
        //--
    }

    public static VerbaItemPedidoService getInstance() {
        if (instance == null) {
            instance = new VerbaItemPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaItemPedidoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

    public void insert(ItemPedido itemPedido) throws SQLException {
    	if (!ValueUtil.isEmpty(itemPedido.verbaItemPedidoList) && !itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		int size = itemPedido.verbaItemPedidoList.size();
            for (int i = 0; i < size; i++) {
        		VerbaItemPedido verbaItemPedido = (VerbaItemPedido) itemPedido.verbaItemPedidoList.items[i];
        		insert(verbaItemPedido);
        	}
    	}
    }

    //@Override
    protected void setDadosAlteracao(BaseDomain domain) {
    	domain.cdUsuario = Session.getCdUsuario();
    }

    public void update(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		//Deleta o Registros anteriores do Item do Pedido
    		delete(itemPedido);
    		if (!ValueUtil.isEmpty(itemPedido.verbaItemPedidoList)) {
    			//Insere novamente os items
    			insert(itemPedido);
    		}
    	}
    }

    /**
     * Cria um verbaItemPedido com todos os valores do item do pedido somente sem o código da verba
     * para que possam ser apagados todos os registros de VerbaItemPedido pertencentes ao ItemPedido
     * @param itemPedido
     * @return VerbaItemPedido
    * @throws SQLException 
     */
    public VerbaItemPedido getVerbaItemPedido(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba()) {
    		VerbaItemPedido verbaItemPedido = new VerbaItemPedido();
    		verbaItemPedido.cdEmpresa = itemPedido.cdEmpresa;
    		verbaItemPedido.cdRepresentante = itemPedido.cdRepresentante;
    		verbaItemPedido.flOrigemPedido = itemPedido.flOrigemPedido;
    		verbaItemPedido.nuPedido = itemPedido.nuPedido;
    		verbaItemPedido.cdProduto = itemPedido.cdProduto;
    		verbaItemPedido.flTipoItemPedido = itemPedido.flTipoItemPedido;
    		verbaItemPedido.nuSeqProduto = itemPedido.nuSeqProduto;
    		return verbaItemPedido;
    	}
    	return null;
    }

    //@Override
    public void delete(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		VerbaItemPedidoService.getInstance().deleteAllByExample(getVerbaItemPedido(itemPedido));
    	}
    }

}