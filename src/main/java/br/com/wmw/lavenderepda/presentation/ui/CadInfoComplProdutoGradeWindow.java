package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadInfoComplProdutoGradeWindow extends WmwWindow {

	private EditNumberInt edQtItemFisico;
	private EditNumberFrac edVlAltura;
	private EditNumberFrac edVlLargura;
	private EditNumberFrac edVlComprimento;
	private ButtonPopup btSalvar;
	private ButtonPopup btRemover;
	private boolean pedidoAberto;
	private ProdutoGrade produtoGrade;
	private LabelValue lvVolume;
	private LabelValue lvVlUnitario;
	private LabelValue lvVlTotal;
	private ItemPedido itemPedido;
	private String cdTabelaPreco;
	private Pedido pedido;
	private String cdUfClientePedido;
	private boolean editing;
	public boolean removeuItem, salvouItem;
	
	public CadInfoComplProdutoGradeWindow(Pedido pedido, ProdutoGrade produtoGrade, String cdTabelaPreco, String cdUfClientePedido, boolean pedidoAberto, double qtInserida) throws SQLException {
		super(Messages.INFO_COMPL_PRODUTO_GRADE_TITULO);
		this.pedido = pedido;
		this.cdTabelaPreco = cdTabelaPreco;
		this.pedidoAberto = pedidoAberto;
		this.cdUfClientePedido = cdUfClientePedido;
		this.produtoGrade = produtoGrade;
		edQtItemFisico = new EditNumberInt("9999999999", 9);
		edQtItemFisico.autoSelect = true;
		edVlAltura = new EditNumberFrac("9999999999", 9);
		edVlAltura.autoSelect = true;
		edVlLargura = new EditNumberFrac("9999999999", 9);
		edVlLargura.autoSelect = true;
		edVlComprimento = new EditNumberFrac("9999999999", 9);
		edVlComprimento.autoSelect = true;
		btSalvar = new ButtonPopup(Messages.LABEL_BT_SALVAR);
		btRemover = new ButtonPopup(Messages.INFO_COMPL_LABEL_REMOVER);
		lvVolume = new LabelValue();
		lvVlUnitario = new LabelValue();
		lvVlTotal = new LabelValue();
		editing = qtInserida > 0;
		setDefaultRect();
		domainToScreen(qtInserida);
		calculateVolume();
		loadVlItemPedido();
		setEditable();
	}

	
	private void loadVlItemPedido() throws SQLException {
		if (isAplicaIndiceVolume()) {
			String cdItemGrade2 = produtoGrade.cdItemGrade2 != null ? produtoGrade.cdItemGrade2 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			String cdItemGrade3 = produtoGrade.cdItemGrade3 != null ? produtoGrade.cdItemGrade3 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			ItemTabelaPreco itemTabelaPrecoGrade = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(cdTabelaPreco, produtoGrade.cdProduto, cdUfClientePedido, produtoGrade.cdItemGrade1, cdItemGrade2, cdItemGrade3);
			Produto produto = new Produto();
			produto.cdProduto = produtoGrade.cdProduto;
			produto.itemTabelaPreco = itemTabelaPrecoGrade;
			produto.vlIndiceVolume = lvVolume.getDoubleValue();
			itemPedido = ItemTabelaPrecoService.getInstance().getItemPedidoGradeComValores(pedido, produto, cdTabelaPreco, false, null);
			lvVlUnitario.setValue(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido));
			lvVlTotal.setValue(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido * edQtItemFisico.getValueInt()));
		}
	}


	private void calculateVolume() {
		if (isInfoComplVolumeObrigatorio()) {
			lvVolume.setValue(StringUtil.getStringValueToInterface(edVlAltura.getValueDouble() * edVlLargura.getValueDouble() * edVlComprimento.getValueDouble()));
		}
	}


	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(labelObrigatoria(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO)), edQtItemFisico, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_ALTURA)), edVlAltura, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_LARGURA)), edVlLargura, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLCOMPRIMENTO)), edVlComprimento, getLeft(), getNextY());
		if (isInfoComplVolumeObrigatorio()) {
			UiUtil.add(this, new LabelName(Messages.LABEL_VOlUME), lvVolume, getLeft(), getNextY());
		}
		if (isAplicaIndiceVolume()) {
			UiUtil.add(this, new LabelName(Messages.INFO_COMPL_LABEL_VLUNITARIO), lvVlUnitario, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.INFO_COMPL_LABEL_VLTOTAL), lvVlTotal, getLeft(), getNextY());
		}
		if (pedidoAberto) {
			addButtonPopup(btSalvar);
			if (editing) {
				addButtonPopup(btRemover);
			}
		}
		addButtonPopup(btFechar);
	}
	
	private String labelObrigatoria(String label) {
		return isInfoComplVolumeObrigatorio() || Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO.equals(label) ?  label + "*" : label;
	}


	private boolean isAplicaIndiceVolume() {
		return ProdutoGrade.INFO_COMPL_VOLUME_1.equals(produtoGrade.flInfoComplVolume);
	}
	
	private boolean isInfoComplVolumeObrigatorio() {
		return ProdutoGrade.INFO_COMPL_VOLUME_1.equals(produtoGrade.flInfoComplVolume) || ProdutoGrade.INFO_COMPL_VOLUME_2.equals(produtoGrade.flInfoComplVolume);
	}
	
	private void domainToScreen(double qtInserida) {
		if (qtInserida > 0) {
			edQtItemFisico.setValue(produtoGrade.qtItemFisico);
			edVlAltura.setValue(produtoGrade.vlAltura);
			edVlLargura.setValue(produtoGrade.vlLargura);
			edVlComprimento.setValue(produtoGrade.vlComprimento);
		}
	}
	
	private void setEditable() {
		edQtItemFisico.setEditable(pedidoAberto);
		edVlAltura.setEditable(pedidoAberto);
		edVlLargura.setEditable(pedidoAberto); 
		edVlComprimento.setEditable(pedidoAberto);
	}
	
	private void salvarClick() throws SQLException {
		beforeSave();
		produtoGrade.qtItemFisico = edQtItemFisico.getValueInt();
		produtoGrade.vlAltura = edVlAltura.getValueDouble();
		produtoGrade.vlLargura = edVlLargura.getValueDouble();
		produtoGrade.vlComprimento = edVlComprimento.getValueDouble();
		produtoGrade.vlIndiceVolume = lvVolume.getDoubleValue();
		setVlVendaItemPedido();
		salvouItem = true;
		fecharWindow();
	}


	private void setVlVendaItemPedido() {
		if (isAplicaIndiceVolume()) {
			produtoGrade.vlBaseItemTabelaPreco = itemPedido.vlBaseItemTabelaPreco; 		
			produtoGrade.vlBaseItemPedido = itemPedido.vlBaseItemPedido;		
			produtoGrade.vlItemPedido = itemPedido.vlItemPedido;		
			produtoGrade.vlUnidadePadrao = itemPedido.vlUnidadePadrao;
		}
	}


	private void beforeSave() {
		ProdutoGradeService.getInstance().validaQtItemFisico(edQtItemFisico.getValueInt());
		if (isInfoComplVolumeObrigatorio()) {
			ProdutoGradeService.getInstance().validaInfosComplObrigatoriasProdutoGrade(edVlAltura.getValueDouble(), edVlLargura.getValueDouble(), edVlComprimento.getValueDouble());
		} else {
			ProdutoGradeService.getInstance().validaInfosComplProdutoGrade(edVlAltura.getValueDouble(), edVlLargura.getValueDouble(), edVlComprimento.getValueDouble());
		}
		ProdutoGradeService.getInstance().validaVlMinMaxInfosComplProdutoGrade(produtoGrade, edVlAltura.getValueDouble(), edVlLargura.getValueDouble(), edVlComprimento.getValueDouble());
	}
	
	
	public void onWindowEvent(final Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					salvarClick();
				} else if (event.target == btRemover) {
					removeuItem = true;
					fecharWindow();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edVlAltura || event.target == edVlLargura || event.target == edVlComprimento || event.target == edQtItemFisico) {
					calculateVolume();
					loadVlItemPedido();
				}
				break;
			}
		}
	}
}
