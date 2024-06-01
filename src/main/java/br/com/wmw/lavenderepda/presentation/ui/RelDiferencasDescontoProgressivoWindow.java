package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoDescontoModificadoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RecalculoDescontoProgressivoDTO;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class RelDiferencasDescontoProgressivoWindow extends WmwListWindow {

    RecalculoDescontoProgressivoDTO recalculoDescontoProgressivoDTO;

    public RelDiferencasDescontoProgressivoWindow(RecalculoDescontoProgressivoDTO recalculoDescontoProgressivoDTO) {
        super(Messages.PEDIDO_DESCONTOPROG_DIFERENCAS_RELATORIO);
        this.recalculoDescontoProgressivoDTO = recalculoDescontoProgressivoDTO;
		configureListContainer();
		makeUnmovable();
		setDefaultRect();
    }

	protected void configureListContainer() {
		listContainer = new GridListContainer(6, 2, false, false);
		listContainer.setBarTopSimple();
		listContainer.setColPosition(0, LEFT);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(2, LEFT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(4, LEFT);
		listContainer.setColPosition(5, RIGHT);
		listContainer.setColColor(3, ColorUtil.baseForeColorSystem);
		listContainer.setColColor(5, ColorUtil.baseForeColorSystem);
	}

	@Override protected String getSelectedRowKey() throws SQLException { return null; }
	@Override protected CrudService getCrudService() throws SQLException { return null; }
	@Override protected void onFormEvent(Event event) throws SQLException {}
	@Override public void detalhesClick() throws SQLException {}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedidoDescontoModificadoDTO();
    }

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return recalculoDescontoProgressivoDTO.listItemDescontoDTO;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedidoDescontoModificadoDTO itemPedidoDescontoModificadoDTO = (ItemPedidoDescontoModificadoDTO) domain;
		String descProgAnterior = itemPedidoDescontoModificadoDTO.descProgressivoAnterior != null ?
				MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_ANTERIOR, itemPedidoDescontoModificadoDTO.descProgressivoAnterior.toString()) :
				MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_ANTERIOR, Messages.DIFERENCAS_DESC_PROG_RELATORIO_SEM_DESC);
		String descProgressivoNovo = itemPedidoDescontoModificadoDTO.descProgressivoNovo != null ?
				MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_ATUAL, itemPedidoDescontoModificadoDTO.descProgressivoNovo.toString()) :
				MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_ATUAL, Messages.DIFERENCAS_DESC_PROG_RELATORIO_SEM_DESC);
		return new String[] {
				StringUtil.getStringValue(itemPedidoDescontoModificadoDTO.itemPedido.getProduto().toString()),
				ValueUtil.VALOR_NI,
				StringUtil.getStringValue(descProgAnterior),
				StringUtil.getStringValue(descProgressivoNovo),
				StringUtil.getStringValue(MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_PCT_ANTERIOR, StringUtil.getStringValueToInterface(itemPedidoDescontoModificadoDTO.vlPctDescProgAnterior))),
				StringUtil.getStringValue(MessageUtil.getMessage(Messages.DIFERENCAS_DESC_PROG_DESC_PCT_ATUAL, StringUtil.getStringValueToInterface(itemPedidoDescontoModificadoDTO.vlPctDescProgNovo)))
		};
	}

	@Override
    public void initUI() {
        try {
            super.initUI();
        } catch (Throwable ex) {
			ExceptionUtil.handle(ex);
        }
    }
	
	@Override
	public void unpop() {
		recalculoDescontoProgressivoDTO.listItemDescontoDTO = new Vector(0);
		super.unpop();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(MessageUtil.quebraLinhas(Messages.DIFERENCAS_DESC_PROG_LABEL)), getLeft(), getNextY(), FILL, UiUtil.getLabelPreferredHeight() * 2);
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
	}

}
