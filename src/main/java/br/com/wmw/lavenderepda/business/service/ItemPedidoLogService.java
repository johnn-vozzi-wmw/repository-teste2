package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoLog;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoLog;
import br.com.wmw.lavenderepda.business.domain.TipoRegistro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoLogDbxDao;
import totalcross.sql.Types;
import totalcross.util.Vector;

public class ItemPedidoLogService extends CrudService {
	
	private ItemPedidoLog itemPedidoLog;

    private static ItemPedidoLogService instance;
    
    private ItemPedidoLogService() {
        //--
    }
    
    public static ItemPedidoLogService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoLogService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemPedidoLogDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    	//--
    }
    
    public void deleteAllEnviadosServidorByPedidoLog(PedidoLog pedidoLog) throws SQLException {
    	if (pedidoLog != null) {
    		ItemPedidoLog itemPedidoLogFilter = new ItemPedidoLog();
    		itemPedidoLogFilter.cdEmpresa = pedidoLog.cdEmpresa;
    		itemPedidoLogFilter.cdRepresentante = pedidoLog.cdRepresentante;
    		itemPedidoLogFilter.flOrigemPedido = pedidoLog.flOrigemPedido;
    		itemPedidoLogFilter.nuPedido = pedidoLog.nuPedido;
    		itemPedidoLogFilter.onlyEnviadosServidorFilter = true;
    		itemPedidoLogFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
    		deleteAllByExample(itemPedidoLogFilter);
    	}
    }
    
    public void deleteByPedidoLog(PedidoLog pedidoLog) throws SQLException {
    	if (pedidoLog != null) {
    		ItemPedidoLog itemPedidoLogFilter = new ItemPedidoLog();
    		itemPedidoLogFilter.cdEmpresa = pedidoLog.cdEmpresa;
    		itemPedidoLogFilter.cdRepresentante = pedidoLog.cdRepresentante;
    		itemPedidoLogFilter.flOrigemPedido = pedidoLog.flOrigemPedido;
    		itemPedidoLogFilter.nuPedido = pedidoLog.nuPedido;
    		deleteAllByExample(itemPedidoLogFilter);
    	}
    }
    
    public void saveItemPedidoLog(String tipoRegistro, ItemPedido itemPedido) throws SQLException {
		ItemPedidoLog newItemPedidoLog = new ItemPedidoLog.Builder(itemPedido).completo(itemPedido).build();
		if (itemPedidoLog != null && !TipoRegistro.INCLUSAO.equals(tipoRegistro)) {
			ItemPedidoLog itemPedLog = new ItemPedidoLog.Builder(itemPedido).build();
			if (TipoRegistro.EXCLUSAO.equals(tipoRegistro)) {
				itemPedLog.flTipoRegistro = tipoRegistro;
				itemPedLog.cdUsuarioCriacao = Session.getCdUsuario();
				itemPedLog.flOrigemAcao = OrigemPedido.FLORIGEMPEDIDO_PDA;
				ItemPedidoLogService.getInstance().insert(itemPedLog);
				itemPedidoLog = new ItemPedidoLog.Builder(itemPedido).completo(itemPedido).build();
			} else {
				itemPedLog = (ItemPedidoLog) ItemPedidoLog.comparableChangedProperties(newItemPedidoLog, itemPedidoLog, itemPedLog);
				if (ItemPedidoLog.isAlterado && !isRegistroJaInserido(itemPedidoLog)) {
					itemPedLog.flTipoRegistro = tipoRegistro;
					itemPedLog.cdUsuarioCriacao = Session.getCdUsuario();
					itemPedLog.flOrigemAcao = OrigemPedido.FLORIGEMPEDIDO_PDA;
					ItemPedidoLogService.getInstance().insert(itemPedLog);
					itemPedidoLog = new ItemPedidoLog.Builder(itemPedido).completo(itemPedido).build();
				}
			}
			
		} else if (TipoRegistro.INCLUSAO.equals(tipoRegistro)) {
			newItemPedidoLog.flTipoRegistro = tipoRegistro;
			ItemPedidoLogService.getInstance().insert(newItemPedidoLog);
			itemPedidoLog = newItemPedidoLog;
		}
	}

	public void loadItemPedidoLog(ItemPedido itemPedido) {
		if (itemPedidoLog == null || !itemPedidoLogPertenceItemPedido(itemPedido)) {
			itemPedidoLog = new ItemPedidoLog.Builder(itemPedido).completo(itemPedido).build();
		}
	}
	
	private boolean itemPedidoLogPertenceItemPedido(ItemPedido itemPedido) {
		if (itemPedido != null && itemPedidoLog != null) {
			return itemPedido.cdEmpresa.equals(itemPedidoLog.cdEmpresa) && itemPedido.cdRepresentante.equals(itemPedidoLog.cdRepresentante) && itemPedido.flOrigemPedido.equals(itemPedidoLog.flOrigemPedido) 
					&& itemPedido.nuPedido.equals(itemPedidoLog.nuPedido) && itemPedido.cdProduto.equals(itemPedidoLog.cdProduto) && itemPedido.flTipoItemPedido.equals(itemPedidoLog.flTipoItemPedido) && ValueUtil.valueEquals(itemPedido.nuSeqProduto, itemPedidoLog.nuSeqProduto) ;
		}
		return false;
	}
	
	@Override
	protected void setDadosAlteracao(BaseDomain domain) {
		ItemPedidoLog itemPedidoLog = (ItemPedidoLog) domain;
		itemPedidoLog.cdUsuario = Session.getCdUsuario();
		itemPedidoLog.cdUsuarioCriacao = Session.getCdUsuario();
	}
	
	public void reabreItemPedidoLogParaEnvio(Pedido pedido) throws SQLException {
		if (pedido != null) {
			updateFlTipoAlteracaoParaEnvio(pedido, BaseDomain.FLTIPOALTERACAO_ORIGINAL);
		}
	}
	
	public void fechaItemPedidoLogParaEnvio(Pedido pedido) throws SQLException {
		if (pedido != null) {
			updateFlTipoAlteracaoParaEnvio(pedido, BaseDomain.FLTIPOALTERACAO_ALTERADO);
		}
	}
	
	private void updateFlTipoAlteracaoParaEnvio(Pedido pedido, String flTipoAlteracao) throws SQLException {
		if (pedido != null) {
			ItemPedidoLog itemPedidoLogFilter = new ItemPedidoLog();
    		itemPedidoLogFilter.cdEmpresa = pedido.cdEmpresa;
    		itemPedidoLogFilter.cdRepresentante = pedido.cdRepresentante;
    		itemPedidoLogFilter.flOrigemPedido = pedido.flOrigemPedido;
    		itemPedidoLogFilter.nuPedido = pedido.nuPedido;
    		Vector itemPedidoLogList = findAllByExample(itemPedidoLogFilter);
    		for (int i = 0; i < itemPedidoLogList.size(); i++) {
				ItemPedidoLog itemPedidoLog = (ItemPedidoLog) itemPedidoLogList.items[i];
    			updateColumn(itemPedidoLog.getRowKey(), BaseDomain.NMCAMPOTIPOALTERACAO, flTipoAlteracao, Types.VARCHAR);
			}
		}
	}

	public boolean isRegistroJaInserido(ItemPedidoLog itemPedidoLog) throws SQLException {
		return findColumnByRowKey(itemPedidoLog.getRowKey(), "ROWKEY") != null;
	}

}