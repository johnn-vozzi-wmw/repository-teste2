package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CatalogoItemPedLog;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CatalogoItemPedLogPdbxDao;
import totalcross.util.Date;

import java.sql.SQLException;

public class CatalogoItemPedLogService extends CrudService {

	private static CatalogoItemPedLogService instance;


	private CatalogoItemPedLogService() {
		//--
	}

	public static CatalogoItemPedLogService getInstance() {
		if (instance == null) {
			instance = new CatalogoItemPedLogService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return CatalogoItemPedLogPdbxDao.getInstance();
	}

	public CatalogoItemPedLog getCatalogoItemPedLog(CatalogoItemPedLog catalogoItemPedLog, ItemPedido itemPedido) {
		catalogoItemPedLog.cdProduto = itemPedido.cdProduto;
		catalogoItemPedLog.flTipoItemPedido = itemPedido.flTipoItemPedido;
		catalogoItemPedLog.nuSeqProduto = itemPedido.nuSeqItemPedido;
		catalogoItemPedLog.vlItemPedido = itemPedido.vlItemPedido;
		return catalogoItemPedLog;
	}

	public void insertCatalogoItemPedLog(Pedido pedido) throws SQLException {
		CatalogoItemPedLog catalogoItemPedLog = new CatalogoItemPedLog();
		catalogoItemPedLog.cdEmpresa = pedido.cdEmpresa;
		catalogoItemPedLog.cdRepresentante = pedido.cdRepresentante;
		catalogoItemPedLog.cdCatalogoItemPedidoLog = generateIdGlobal();
		catalogoItemPedLog.flOrigemPedido = pedido.flOrigemPedido;
		catalogoItemPedLog.nuPedido = pedido.nuPedido;
		catalogoItemPedLog.cdUsuario = SessionLavenderePda.getRepresentante().cdRepresentante;
		catalogoItemPedLog.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		catalogoItemPedLog.dtGeracaoCatalogo = new Date();
		catalogoItemPedLog.hrGeracaoCatalogo = TimeUtil.getCurrentTimeHHMMSS();
		catalogoItemPedLog.dtAlteracao = new Date();
		catalogoItemPedLog.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			catalogoItemPedLog.nuSeqCatalogoItemPedidoLog = i;
			catalogoItemPedLog = getCatalogoItemPedLog(catalogoItemPedLog, itemPedido);
			CatalogoItemPedLogPdbxDao.getInstance().insert(catalogoItemPedLog);
		}
	}

}
