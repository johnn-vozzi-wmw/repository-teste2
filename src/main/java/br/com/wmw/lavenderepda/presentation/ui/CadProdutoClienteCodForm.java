package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteCodService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;

public class CadProdutoClienteCodForm extends BaseCrudCadForm {
	
	private CadProdutoClienteCodFormWindow cadProdutoClienteCodFormWindow;
	
	private LabelValue lvCliente;
	private LabelValue lvProduto;
	private EditText edProdutoClienteCod;
	private EditText edDsProduto;
	private BaseButton btFiltrarProduto;
	private Cliente cliente;
	private String cdProduto;
	private boolean fromPedido;
	
	public CadProdutoClienteCodForm(boolean fechamentoPedido) {
		super(Messages.CAD_PROD_CLI_COD_TITULO_CAD);
		this.fromPedido = fechamentoPedido;
		lvCliente = new LabelValue("");
		lvProduto = new LabelValue("");
		edProdutoClienteCod = new EditText("@@@@@@@@@@", 20);
	}
	
	public CadProdutoClienteCodForm(Cliente cliente) {
		super(Messages.CAD_PROD_CLI_COD_TITULO_CAD);
		lvCliente = new LabelValue("");
		this.cliente = cliente;
		edDsProduto = new EditText("@@@@@@@@@@", 100);
		edDsProduto.drawBackgroundWhenDisabled = true;
		edDsProduto.setEditable(false);
		btFiltrarProduto = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		edProdutoClienteCod = new EditText("@@@@@@@@@@", 20);
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ProdutoClienteCod();
	}

	@Override
	protected String getEntityDescription() {
		return Messages.CAD_PROD_CLI_COD_DELETE_MSG;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoClienteCodService.getInstance();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btFiltrarProduto) {
				btFiltrarProdutoClick();
			}
			break;
		case KeyEvent.SPECIAL_KEY_PRESS:
			KeyEvent key = (KeyEvent) event;
			if (key.isActionKey()) {
				salvarClick();
			}
			break;
		}
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.CAD_PROD_CLI_COD_CLIENTE), lvCliente, getLeft(), getNextY());
		if (cliente == null) {
			UiUtil.add(this, new LabelName(Messages.CAD_PROD_CLI_COD_PRODUTO), lvProduto, getLeft(), getNextY());
		} else {
			UiUtil.add(this, new LabelName(Messages.CAD_PROD_CLI_COD_PRODUTO), edDsProduto, getLeft(), AFTER, FILL - btFiltrarProduto.getPreferredWidth() - (WIDTH_GAP * 2) - WIDTH_GAP_BIG);
			UiUtil.add(this, btFiltrarProduto, RIGHT - WIDTH_GAP_BIG, SAME, PREFERRED, SAME);
		}
		UiUtil.add(this, new LabelName(Messages.CAD_PROD_CLI_COD_CODIGO), edProdutoClienteCod, getLeft(), getNextY());
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) getDomain();
		produtoClienteCod.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoClienteCod.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		produtoClienteCod.cdProdutoCliente = edProdutoClienteCod.getValue();
		if (!isEditing() && cliente != null) {
			produtoClienteCod.cdProduto = cdProduto;
			produtoClienteCod.cdCliente = cliente.cdCliente;
		}
		return produtoClienteCod;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		lvProduto.setText(StringUtil.getStringValue(
				(LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : produtoClienteCod.cdProduto.concat(" - "))
				.concat(produtoClienteCod.dsProduto)));
		lvCliente.setText(StringUtil.getStringValue(produtoClienteCod.cdCliente.concat(" - ").concat(produtoClienteCod.dsRazaoSocialCliente)));
		edProdutoClienteCod.setValue(produtoClienteCod.cdProdutoCliente);
	}
	
	private void edProdutoClienteCodSetFocus() {
 		edProdutoClienteCod.requestFocus();
		String produtoCodigo = edProdutoClienteCod.getValue();
		if (ValueUtil.isNotEmpty(produtoCodigo)) {
			edProdutoClienteCod.setCursorPos(produtoCodigo.length(), produtoCodigo.length());
		}
	}

	private void btFiltrarProdutoClick() throws SQLException {
    	ListProdutoWindow produtoForm = new ListProdutoWindow();
		produtoForm.popup();
		if (produtoForm.produto != null) {
			cdProduto = produtoForm.produto.cdProduto;
			edDsProduto.setValue(LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(produtoForm.produto.dsProduto) : StringUtil.getStringValue(produtoForm.produto));
		}
    }

	@Override
	protected void clearScreen() throws SQLException {
	}
	
	@Override
	protected void visibleState() throws SQLException {
		btExcluir.setVisible(!fromPedido && isEditing());
	}
	
	@Override
	public void close() throws SQLException {
		cadProdutoClienteCodFormWindow.unpop();
		list();
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (cliente != null) {
			lvCliente.setValue(StringUtil.getStringValue(cliente));
		}
	}
	
	@Override
	public void edit(BaseDomain domain) throws SQLException {
		ProdutoClienteCod produtoCliente = (ProdutoClienteCod) domain;
		if (fromPedido && produtoCliente != null && !produtoCliente.isPossuiCodigoProdutoCliente()) {
			editProdutoSemCodigo(domain);
		} else {
			super.edit(domain);
		}
	}

	private void editProdutoSemCodigo(BaseDomain domain) throws SQLException {
		setDomain(domain);
		domainToScreen(domain);
		visibleState();
	}
	
	public void setCadProdutoClienteCodFormWindow(CadProdutoClienteCodFormWindow cadProdutoClienteCodFormWindow) {
		this.cadProdutoClienteCodFormWindow = cadProdutoClienteCodFormWindow;
	}
	
	@Override
	public void initUI() {
		super.initUI();
		if (fromPedido || isEditing()) {
			edProdutoClienteCodSetFocus();
		}
	}
	
}
