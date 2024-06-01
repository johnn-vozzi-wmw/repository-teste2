package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.Control;

public class InfoExtraItemPedidoWindow extends WmwWindow {

	private ItemPedido itemPedido;
	private CadItemPedidoForm cadItemPedidoForm;
	private SessionContainer containerFields;
	private LabelContainer lbDsProdutoContainer;
	
	public InfoExtraItemPedidoWindow(ItemPedido itemPedido, Pedido pedido, CadPedidoForm cadPedidoForm) throws SQLException {
		super(Messages.ITEMPEDIDO_INFORMACOES_EXTRAS);
		this.itemPedido = itemPedido;
		lbDsProdutoContainer = new LabelContainer(MessageUtil.quebraLinhas(itemPedido.getDsProdutoWithKey(itemPedido)));
        containerFields = new SessionContainer();
        containerFields.setBackColor(getBackColor());
		cadItemPedidoForm = CadItemPedidoForm.getNewCadItemPedido(cadPedidoForm, pedido);
		cadItemPedidoForm.criaLabelsDinamicos();
		cadItemPedidoForm.setItemPedido(this.itemPedido);
		cadItemPedidoForm.fromInfoExtra = true;
		setDefaultWideRect();
	}
	
	@Override
	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(this, lbDsProdutoContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
			cadItemPedidoForm.domainToScreen(itemPedido);
			cadItemPedidoForm.populateHashEditsAndLabels(LavenderePdaConfig.ordemCamposTelaInfoExtra);
			int qtLinhasFields = ValueUtil.getIntegerValueTruncated((cadItemPedidoForm.hashEdits.size() / 3) + ((cadItemPedidoForm.hashEdits.size() % 3) == 0 ? 0 : 1));
			UiUtil.add(this, containerFields, LEFT, AFTER, FILL, (UiUtil.getControlPreferredHeight() * qtLinhasFields) + ((HEIGHT_GAP * qtLinhasFields * 2)));
			addEditsAndLabels();
			changeLbDsProdutoValue(itemPedido.getDsProdutoWithKey());
			screenResized();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee, ee.getMessage());
			throw new ValidationException(ee.getMessage());
		}
	}

	private void changeLbDsProdutoValue(String value) {
		lbDsProdutoContainer.setRect(lbDsProdutoContainer.getX(), lbDsProdutoContainer.getY(), lbDsProdutoContainer.getPreferredWidth(), lbDsProdutoContainer.getHeight());
		lbDsProdutoContainer.resizeHeight();
	}
	
	private void addEditsAndLabels() {
		cadItemPedidoForm.addEditsVenda(containerFields, BOTTOM - HEIGHT_GAP, getWidth());
		desabilitaComponentes();
	}
	
	private void desabilitaComponentes() {
		for (int i = 1; i <= cadItemPedidoForm.hashEdits.size(); i++) {
			Control control = (Control) cadItemPedidoForm.hashEdits.get(i);
			if (control != null) {
				if (control instanceof BaseEdit) {
					BaseEdit controlEdit = (BaseEdit) control;
					controlEdit.setEditable(false);
				} else if (control instanceof ButtonGroupBoolean) {
					ButtonGroupBoolean controlEdit = (ButtonGroupBoolean) control;
					controlEdit.setEnabled(false);
				} else if (control instanceof BaseComboBox) {
					BaseComboBox controlEdit = (BaseComboBox) control;
					controlEdit.setEnabled(false);
				}
			}
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			cadItemPedidoForm.btGradeItemPedido.setVisible(false);
			if (cadItemPedidoForm.lvEstoque.btGradeEstoque != null) {
				cadItemPedidoForm.lvEstoque.btGradeEstoque.setVisible(false);
			}
		}
		if (LavenderePdaConfig.usaEstoqueOnline && cadItemPedidoForm.lvEstoque.btAcao != null) {
			cadItemPedidoForm.lvEstoque.btAcao.setVisible(false);
		}
	}
	
	@Override
	public void screenResized() {
		super.screenResized();
		addEditsAndLabels();
	}
}
