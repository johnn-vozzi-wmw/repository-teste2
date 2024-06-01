package br.com.wmw.lavenderepda.presentation.ui.ext;

public class ItemContainerControlState {

	public ItemContainer itemContainer;
	public boolean choserQtdBtMenosEnabled;
	public boolean choserQtdBtMaisEnabled;
	public boolean choserQtdEdQtdEnabled;
	public boolean edVlItemPedidoEnabled;
	public boolean edVlBrutoItemPedidoEnabled;
	public boolean choserDescAscBtMenosEnabled;
	public boolean choserDescAscBtMaisEnabled;
	public boolean choserDescAscEdQtdEnabled;
	public boolean choserEstoqueClienteBtMenosEnabled;
	public boolean choserEstoqueClienteBtMaisEnabled;
	public boolean choserEstoqueClienteEdQtdEnabled;

	public ItemContainerControlState(ItemContainer itemContainer) {
		this.itemContainer = itemContainer;
		choserQtdBtMenosEnabled = false;
		choserQtdBtMaisEnabled = false;
		choserQtdEdQtdEnabled = false;
		edVlItemPedidoEnabled = false;
		edVlBrutoItemPedidoEnabled = false;
		choserDescAscBtMenosEnabled = false;
		choserDescAscBtMaisEnabled = false;
		choserDescAscEdQtdEnabled = false;
		choserEstoqueClienteBtMenosEnabled = false;
		choserEstoqueClienteBtMaisEnabled = false;
		choserEstoqueClienteEdQtdEnabled = false;

		if (itemContainer.chooserQtd != null) {
			choserQtdBtMenosEnabled = itemContainer.chooserQtd.btMenos.isEnabled();
			choserQtdBtMaisEnabled = itemContainer.chooserQtd.btMais.isEnabled();
			choserQtdEdQtdEnabled = itemContainer.chooserQtd.edF.isEnabled();
		}
		if (itemContainer.edVlItemPedido != null) {
			edVlItemPedidoEnabled = itemContainer.edVlItemPedido.isEnabled();
		}
		if (itemContainer.edVlBrutoItem != null) {
			edVlBrutoItemPedidoEnabled = itemContainer.edVlBrutoItem.isEnabled();
		}
		if (itemContainer.chooserDescAcresc != null) {
			choserDescAscBtMenosEnabled = itemContainer.chooserDescAcresc.btMenos.isEnabled();
			choserDescAscBtMaisEnabled = itemContainer.chooserDescAcresc.btMais.isEnabled();
			choserDescAscEdQtdEnabled = itemContainer.chooserDescAcresc.edF.isEnabled();
		}
		if (itemContainer.chooserEstoqueCliente != null) {
			choserEstoqueClienteBtMenosEnabled = itemContainer.chooserEstoqueCliente.btMenos.isEnabled();
			choserEstoqueClienteBtMaisEnabled = itemContainer.chooserEstoqueCliente.btMais.isEnabled();
			choserEstoqueClienteEdQtdEnabled = itemContainer.chooserEstoqueCliente.edF.isEnabled();
		}
	}

}