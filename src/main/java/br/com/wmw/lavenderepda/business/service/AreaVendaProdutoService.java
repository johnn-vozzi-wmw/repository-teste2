package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AreaVendaProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AreaVendaProdutoPdbxDao;

public class AreaVendaProdutoService extends CrudService {

	private static AreaVendaProdutoService instance;

	private AreaVendaProdutoService() {
		//--
	}

	public static AreaVendaProdutoService getInstance() {
		if (instance == null) {
			instance = new AreaVendaProdutoService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return AreaVendaProdutoPdbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public void validadeProdutoAreaVenda(String cdAreaVenda, String cdProduto, boolean lanceException) throws SQLException {
		AreaVendaProduto areaVendaProduto = new AreaVendaProduto();
		areaVendaProduto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		areaVendaProduto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		areaVendaProduto.cdAreavenda = cdAreaVenda;
		areaVendaProduto.cdProduto = cdProduto;
		String rowKey = findColumnByRowKey(areaVendaProduto.getRowKey(), "ROWKEY");
		if (ValueUtil.isEmpty(rowKey)) {
			if (lanceException) {
				throw new ValidationException(Messages.PEDIDO_MSG_ADD_PRODUTO_AREAVENDA_DIF);
			} else {
				UiUtil.showWarnMessage(Messages.PEDIDO_MSG_ADD_PRODUTO_AREAVENDA_DIF);
			}
		}
	}

    public String[] findCdsAreasVendaProduto(String cdProduto) throws SQLException {
    	AreaVendaProduto areaVendaProdutoFilter = new AreaVendaProduto();
    	areaVendaProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	areaVendaProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	areaVendaProdutoFilter.cdProduto = cdProduto;
    	String[] cdsAreaVenda = AreaVendaProdutoPdbxDao.getInstance().findCdsAreaVendaByExample(areaVendaProdutoFilter);
		return cdsAreaVenda != null ? cdsAreaVenda : new String[]{};
    }


}