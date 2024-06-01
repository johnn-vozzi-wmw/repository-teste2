package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemLiberacao;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemLiberacaoDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.util.Vector;

public class ItemLiberacaoService extends CrudService {

    private static ItemLiberacaoService instance;
    
    private ItemLiberacaoService() {
        //--
    }
    
    public static ItemLiberacaoService getInstance() {
        if (instance == null) {
            instance = new ItemLiberacaoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemLiberacaoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public void saveItemLiberacaoPedido(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		ItemLiberacao itemLiberacao = new ItemLiberacao();
    		itemLiberacao.cdEmpresa = pedido.cdEmpresa;
    		itemLiberacao.cdRepresentante = pedido.cdRepresentante;
    		itemLiberacao.flOrigemPedido = pedido.flOrigemPedido;
    		itemLiberacao.nuPedido = pedido.nuPedido;
    		itemLiberacao.cdUsuarioLiberacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    		itemLiberacao.nmUsuario = SessionLavenderePda.usuarioPdaRep.usuario.nmUsuario;
    		try {
				insert(itemLiberacao);
			} catch (Throwable e) {
				update(itemLiberacao);
			}
    	}
    }
    
    public ItemLiberacao getItemLiberacaoByPedido(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		ItemLiberacao itemLiberacaoFilter = new ItemLiberacao();
    		itemLiberacaoFilter.cdEmpresa = pedido.cdEmpresa;
    		itemLiberacaoFilter.cdRepresentante = pedido.cdRepresentante;
    		itemLiberacaoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
    		itemLiberacaoFilter.nuPedido = pedido.nuPedido;
    		Vector itemLiberacaoList = findAllByExample(itemLiberacaoFilter);
    		if (ValueUtil.isNotEmpty(itemLiberacaoList)) {
    			return (ItemLiberacao) itemLiberacaoList.items[0];
    		}
    	}
    	return null;
    }
    
    public ItemLiberacao getItemLiberacao(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		ItemLiberacao itemLiberacaoFilter = new ItemLiberacao();
    		itemLiberacaoFilter.cdEmpresa = pedido.cdEmpresa;
    		itemLiberacaoFilter.cdRepresentante = pedido.cdRepresentante;
    		itemLiberacaoFilter.flOrigemPedido = pedido.flOrigemPedido;
    		itemLiberacaoFilter.nuPedido = pedido.nuPedido;
    		itemLiberacaoFilter.cdUsuarioLiberacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    		return (ItemLiberacao) findByRowKey(itemLiberacaoFilter.getRowKey());
    	}
    	return null;
    }
    
    public void enviaItemLiberacaoBackground(String cdSessao, Pedido pedido) throws SQLException {
    	ItemLiberacao itemLiberacao = getItemLiberacao(pedido);
		if (itemLiberacao != null) {
			EnviaDadosThread.getInstance().montaDadosEnvioItemLiberacaoBackground(cdSessao, itemLiberacao);
		}
	}
	
	public void enviaItemLiberacaoServidor(Pedido pedido) throws Exception {
		if (pedido != null) {
			if (LavenderePdaConfig.usaEnvioPedidoBackground) {
				enviaItemLiberacaoBackground(PedidoService.getInstance().generateIdGlobal(), pedido);
			} else {
				SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), SyncManager.getInfoAtualizacaoItemLiberacao());
			}
		}
	}

}