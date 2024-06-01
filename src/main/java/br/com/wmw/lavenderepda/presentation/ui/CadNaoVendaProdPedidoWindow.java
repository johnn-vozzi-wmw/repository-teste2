package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotNaoVendaProduto;
import br.com.wmw.lavenderepda.business.domain.NaoVendaProdPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.NaoVendaProdPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotNaoVendaProdutoComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadNaoVendaProdPedidoWindow extends WmwCadWindow {
	
	private Pedido pedido;
	private Produto produtoSelecionado;
	private MotNaoVendaProdutoComboBox motNaoVendaProdutoComboBox;
	private LabelValue lbProduto;
	private LabelValue lbExigeJustificativa; 
	private EditMemo edDsJustificativa;
	
	public CadNaoVendaProdPedidoWindow(Pedido pedido, Produto produtoSelecionado) throws SQLException {
		super(Messages.MOTIVO_NAO_VENDA_PRODUTO);
		this.pedido = pedido;
		this.produtoSelecionado = produtoSelecionado;
		lbProduto = new LabelValue(produtoSelecionado.toString());
		lbProduto.autoSplit = true;
		motNaoVendaProdutoComboBox = new MotNaoVendaProdutoComboBox();
		lbExigeJustificativa = new LabelValue();
		edDsJustificativa = new EditMemo("@@@@@@@@@@", 6, 254);
		btSalvar.setText(FrameworkMessages.BOTAO_SALVAR);
		setDefaultRect();
		atualizaLabelExigeJustificava();
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		NaoVendaProdPedido naoVendaProdPedido = new NaoVendaProdPedido(pedido, produtoSelecionado.cdProduto);
		naoVendaProdPedido.cdMotivo = motNaoVendaProdutoComboBox.getValue();
		naoVendaProdPedido.dsJustificativa = edDsJustificativa.getText();
		naoVendaProdPedido.setMotNaoVendaProduto((MotNaoVendaProduto) motNaoVendaProdutoComboBox.getSelectedItem());
		return naoVendaProdPedido;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
	}

	@Override
	protected void clearScreen() throws SQLException {
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new NaoVendaProdPedido();
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NaoVendaProdPedidoService.getInstance();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbProduto, getLeft(), getTop() + HEIGHT_GAP, FILL, PREFERRED);
		UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA),  motNaoVendaProdutoComboBox, getLeft() , AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.EXIGE_JUSTIFICATIVA), lbExigeJustificativa, getLeft() , AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.JUSTIFIQUE_MOTIVO_NAO_VENDA_PRODUTO), edDsJustificativa, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - HEIGHT_GAP_BIG);
	}
	
	@Override
	protected void addButtons() {
		addButtonPopup(btSalvar);
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		try {
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == motNaoVendaProdutoComboBox) {
						atualizaLabelExigeJustificava();
					}
				}
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void atualizaLabelExigeJustificava() {
		lbExigeJustificativa.setText(StringUtil.getStringValueFromFlboolean(motNaoVendaProdutoComboBox.getFlExigeJustificativa()));
	}
	
	
}
