package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescVidaUtilGrupo;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelProdutoLotesWindow extends WmwWindow {

	private static final String MOSTRA_TODOS_CAMPOS_GRID_LOTE = "0";
	private BaseGridEdit gridLoteProduto;
	private Produto produto;
	private ItemTabelaPreco itemTabelaPreco;
	private TabelaPrecoComboBox cbTabelaPreco;
	private UnidadeFederalComboBox cbUf;
	private LabelContainer lbDsProdutoContainer;
	private String cdLocal;
	private boolean ignoraLocal;
	
	public RelProdutoLotesWindow(Produto produto, String cdLocal, boolean ignoraLocal) throws SQLException {
		super(Messages.PRODUTOS_LOTES_RELATORIO);
        this.produto = produto;
        this.cdLocal = cdLocal;
        this.ignoraLocal = ignoraLocal;
        lbDsProdutoContainer = new LabelContainer(this.produto.toString());
        cbUf = new UnidadeFederalComboBox();
		cbUf.setID("cbUf");
        if (LavenderePdaConfig.usaPrecoPorUf) {
        	cbUf.carregaUf();
        	cbUf.setSelectedIndex(0);
        }
        scrollable = false;
        cbTabelaPreco = new TabelaPrecoComboBox();
        cbTabelaPreco.loadForListProduto();
        cbTabelaPreco.setSelectedIndex(0);
		cbTabelaPreco.setID("cbTabelaPreco");
		itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cbTabelaPreco.getValue(), produto.cdProduto, cbUf.getValue());
		makeUnmovable();
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, new LabelName(Messages.TABELA_PRECO), cbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP);
		if (LavenderePdaConfig.usaPrecoPorUf) {
			UiUtil.add(this, new LabelName(Messages.UF), cbUf, SAME, AFTER + HEIGHT_GAP);
		}
        GridColDefinition[] gridColDefiniton;
        if (MOSTRA_TODOS_CAMPOS_GRID_LOTE.equals(LavenderePdaConfig.configColunasGridLoteProduto)) {
			gridColDefiniton = returnAllFieldsGridLoteProduto();
		} else {
			gridColDefiniton = returnFieldsGridLoteProduto();
		}
		gridLoteProduto = UiUtil.createGridEdit(gridColDefiniton, false);
		gridLoteProduto.setGridControllable();
		gridLoteProduto.setID("gridLoteProduto");
		UiUtil.add(this, gridLoteProduto, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - footerH);
		if (gridLoteProduto.getHeight() <= UiUtil.getControlPreferredHeight() * 6) {
			gridLoteProduto.resetSetPositions();
			gridLoteProduto.setRect(LEFT, AFTER + HEIGHT_GAP_BIG, FILL, UiUtil.getControlPreferredHeight() * 8, LavenderePdaConfig.usaPrecoPorUf ? cbUf : cbTabelaPreco);
		}
		carregaGridLoteProduto();
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private GridColDefinition[] returnAllFieldsGridLoteProduto() {
		return new GridColDefinition[] {
				new GridColDefinition(Messages.LABEL_LOCAL, -25, Container.LEFT),
				new GridColDefinition(Messages.LOTEPRODUTO_LOTE, -25, Container.LEFT),
				new GridColDefinition(Messages.LOTEPRODUTO_VALIDADE, -35, Container.CENTER),
				new GridColDefinition(Messages.LOTEPRODUTO_VIDA, -20, Container.CENTER),
				new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE, -20, Container.RIGHT),
				new GridColDefinition(Messages.LOTEPRODUTO_RESERVA, -20, Container.RIGHT),
				new GridColDefinition(Messages.LOTEPRODUTO_DESCONTO, -20, Container.CENTER),
				new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -20, Container.RIGHT) };
	}
	
	private GridColDefinition[] returnFieldsGridLoteProduto() {
		List<GridColDefinition> list = new ArrayList<GridColDefinition>();
		String[] configList = LavenderePdaConfig.configColunasGridLoteProduto.split(";");
		int index = 0;
		for (String config : configList) {
			GridColDefinition field = createFieldByVlParameter(config, index);
			if (field == null) continue;
			list.add(field);
			index++;
		}
		return list.toArray(new GridColDefinition[list.size()]);
	}

	private GridColDefinition createFieldByVlParameter(String config, int index) {
		switch (config) {
		case "1":
			return new GridColDefinition(Messages.LABEL_LOCAL, -25 , Container.LEFT);
		case "2":
			return new GridColDefinition(Messages.LOTEPRODUTO_LOTE, -25, Container.LEFT);
		case "3":
			return new GridColDefinition(Messages.LOTEPRODUTO_VALIDADE, -35, Container.CENTER);
		case "4":
			return new GridColDefinition(Messages.LOTEPRODUTO_VIDA, -20, Container.CENTER);
		case "5":
			return new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE, -20, Container.RIGHT);
		case "6":
			return new GridColDefinition(Messages.LOTEPRODUTO_RESERVA, -20, Container.RIGHT);
		case "7":
			return new GridColDefinition(Messages.LOTEPRODUTO_DESCONTO, -20, Container.CENTER);
		case "8":
			return new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -20, Container.RIGHT);
		}
		return null;
	}
	
	@Override
	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbTabelaPreco || event.target == cbUf) {
					reloadGridProdutos();
				}
				break;
			default:
				break;
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void reloadGridProdutos() throws SQLException {
		itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cbTabelaPreco.getValue(), produto.cdProduto, cbUf.getValue());
		gridLoteProduto.removeAllElements();
		carregaGridLoteProduto();
	}

	public void carregaGridLoteProduto() throws SQLException {
		gridLoteProduto.setItems(null);
		gridLoteProduto.gridController.clearColors();
		LoteProduto loteProdutoFilter = new LoteProduto(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto, null);
		loteProdutoFilter.cdTabelaPrecoFilter = cbTabelaPreco.getValue();
		loteProdutoFilter.agrupaCdLoteProduto = true;
		Vector loteList = LoteProdutoService.getInstance().findLoteProdutoPor(produto.cdGrupoProduto1, loteProdutoFilter, LoteProduto.FLORIGEMSALDOERP, cdLocal, ignoraLocal);
		if (!ValueUtil.isEmpty(loteList)) {
			int size = loteList.size();
			String descString;
			String precoString;
	        for (int i = 0; i < size; i++) {
	        	LoteProduto lote = (LoteProduto) loteList.items[i];
	        	descString = DescVidaUtilGrupo.DESCVIDAUTILNAOENCONTRADO;
				precoString = DescVidaUtilGrupo.DESCVIDAUTILNAOENCONTRADO;
				double desc = 0.0;
				double preco = itemTabelaPreco.vlUnitario;
				double vldesc = ValueUtil.getDoubleValue(lote.vlPctDesconto);
				if (vldesc != 0D) {
					desc = (double) vldesc;
					preco = ValueUtil.round(preco - ((preco * desc) / 100));
					descString = StringUtil.getStringValueToInterface(desc);
					precoString = StringUtil.getStringValueToInterface(preco);
				}
				lote.vlBaseItemString = precoString;
				lote.vlPctDesconto = descString;
				String[] item = createItemGridLoteProduto(lote);
				gridLoteProduto.add(item);
				setaCorLoteProduto(lote, i);
			}
		}
		loteList = null;
	}
		
	private void setaCorLoteProduto(LoteProduto lote, int posicao) {
		if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco() && lote.qtTabPrecoLoteProd == 0) {
			gridLoteProduto.gridController.setRowForeColor(LavendereColorUtil.COR_FUNDO_GRID_LOTE_PRODUTO_VINCULADO_TABELA_PRECO, posicao);
		} else if (LavenderePdaConfig.pctVidaUtilLoteProdutoCritico > lote.vlPctvidautilproduto) {
			gridLoteProduto.gridController.setRowForeColor(LavendereColorUtil.COR_FONTE_QUE_CONTEM_LOTE_PERCENTUAL_DE_VIDA_CRITICO, posicao);
		} 
	}
	
	private String[] createItemGridLoteProduto(LoteProduto lote) {
		if (MOSTRA_TODOS_CAMPOS_GRID_LOTE.equals(LavenderePdaConfig.configColunasGridLoteProduto)) {
			return new String[] {lote.getLocal().toString(), lote.cdLoteproduto, StringUtil.getStringValue(lote.dtValidade), StringUtil.getStringValue(lote.vlPctvidautilproduto), StringUtil.getStringValue(lote.qtEstoque), StringUtil.getStringValue(lote.qtEstoquereservado), lote.vlPctDesconto, lote.vlBaseItemString};
		} else {
			return createArrayItemGridLoteProduto(lote);
		}
	}
	
	private String[] createArrayItemGridLoteProduto(LoteProduto lote) {
		List<String> list = new ArrayList<String>();
		String[] configList = LavenderePdaConfig.configColunasGridLoteProduto.split(";");
		for (String config : configList) {
			String field = createFieldItemByVlParameter(config, lote);
			if (field == null) continue;;
			list.add(field);
		}
		return list.toArray(new String[list.size()]);
	}

	private String createFieldItemByVlParameter(String config, LoteProduto lote) {
		switch (config) {
		case "1":
			String str = lote.getLocal().toString();
			return str != null ? str : "";
		case "2":
			return lote.cdLoteproduto;
		case "3":
			return StringUtil.getStringValue(lote.dtValidade);
		case "4":
			return StringUtil.getStringValueToInterface(lote.vlPctvidautilproduto);
		case "5":
			return StringUtil.getStringValueToInterface(lote.qtEstoque);
		case "6":
			return StringUtil.getStringValueToInterface(lote.qtEstoquereservado);
		case "7":
			return lote.vlPctDesconto;
		case "8":
			return lote.vlBaseItemString;
		}
		return null;
	}

	protected void onUnpop() {
		super.onUnpop();
		gridLoteProduto.setItems(null);
	}

}
