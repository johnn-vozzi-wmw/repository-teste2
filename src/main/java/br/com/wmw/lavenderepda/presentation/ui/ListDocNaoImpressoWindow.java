package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeReferenciaDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;

public class ListDocNaoImpressoWindow extends WmwWindow {
	
	public ListDocNaoImpressoForm listDocNaoImpressoForm;

	public ListDocNaoImpressoWindow() {
		super(Messages.DOCS_NAO_IMPRESSOS_NOME);
		listDocNaoImpressoForm = new ListDocNaoImpressoForm();
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		listDocNaoImpressoForm.setOnWindow();
		UiUtil.add(this, listDocNaoImpressoForm , LEFT , getTop() , FILL , FILL);
		listDocNaoImpressoForm.visibleState();
	}
	
	@Override
	public void popup() {
		try {
			listDocNaoImpressoForm.loadDados();
			if (ValueUtil.isNotEmpty(NfeDao.erroOcorridoAtualizacao)) {
				UiUtil.showWarnMessage(NfeDao.erroOcorridoAtualizacao);
				NfeDao.erroOcorridoAtualizacao = "";
			}
			if (ValueUtil.isNotEmpty(ItemNfeDbxDao.erroOcorridoAtualizacao)) {
				UiUtil.showWarnMessage(ItemNfeDbxDao.erroOcorridoAtualizacao);
				ItemNfeDbxDao.erroOcorridoAtualizacao = "";
			}
			if (ValueUtil.isNotEmpty(ItemNfeReferenciaDao.erroOcorridoAtualizacao)) {
				UiUtil.showWarnMessage(ItemNfeReferenciaDao.erroOcorridoAtualizacao);
				ItemNfeReferenciaDao.erroOcorridoAtualizacao = "";
			}
			if (ValueUtil.isNotEmpty(PedidoBoletoDao.erroOcorridoAtualizacao)) {
				UiUtil.showWarnMessage(PedidoBoletoDao.erroOcorridoAtualizacao);
				PedidoBoletoDao.erroOcorridoAtualizacao = "";
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		super.popup();
	}
	
}
