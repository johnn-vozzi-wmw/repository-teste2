package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.ComiRentabilidadeService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.util.Vector;

public class RelRentabilidadeComissaoWindow extends WmwWindow {
	
	private ItemPedido itemPedido;
	private BaseGridEdit grid;
	private LabelContainer produtoProdutoContainer;

	public RelRentabilidadeComissaoWindow(ItemPedido itemPedido) throws SQLException {
		super(Messages.REL_RENTABILIDADE_COMISSAO_TITULO);
		this.itemPedido = itemPedido;
		produtoProdutoContainer = new LabelContainer(this.itemPedido.getProduto().toString());
		boolean usaVerbaFaixaRentComissao = LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao;
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.REL_RENTABILIDADE_LABEL_FAIXA, usaVerbaFaixaRentComissao ? -33 : -50, LEFT),
			new GridColDefinition(Messages.REL_RENTABILIDADE_LABEL_VLPCTCOMISSAO, usaVerbaFaixaRentComissao ? -33 : -50, LEFT),
			new GridColDefinition(Messages.REL_RENTABILIDADE_LABEL_VLPCTVERBA, usaVerbaFaixaRentComissao ? -33 : 0, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
		grid.setGridControllable();
		carregaGrid();
		setDefaultRect();
	}
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, produtoProdutoContainer, LEFT, getTop() + HEIGHT_GAP_BIG, FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	private void carregaGrid() throws SQLException {
		grid.removeAllElements();
		Vector comiRentabilidadeList = ComiRentabilidadeService.getInstance().findAllFaixasComiRentabilidade(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
		if (ValueUtil.isNotEmpty(comiRentabilidadeList)) {
			ComiRentabilidade comiRentabilidadeAtingida = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getVlPctRentabilidade(false));
			ComiRentabilidade comiRentabilidade;
			for (int i = 0; i < comiRentabilidadeList.size(); i++) {
				comiRentabilidade = (ComiRentabilidade) comiRentabilidadeList.items[i];
				String[] itemGrid = {
						ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidade(comiRentabilidadeList, comiRentabilidade),
						StringUtil.getStringValueToInterface(comiRentabilidade.vlPctComissao),
						StringUtil.getStringValueToInterface(comiRentabilidade.vlPctVerba)
				};
				grid.add(itemGrid);
				if (comiRentabilidadeAtingida != null && comiRentabilidadeAtingida.equals(comiRentabilidade)) {
					grid.gridController.setRowBackColor(ColorUtil.softGreen, i);
				}
			}
		}
	}

}
