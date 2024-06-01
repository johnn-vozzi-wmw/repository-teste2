package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import totalcross.util.Vector;

public class DescPromocionalComboBox extends BaseComboBox {
	
	public DescPromocionalComboBox(Pedido pedido, String cdTabelaPreco) throws SQLException {
		super(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
		defaultItemType  = DefaultItemType_LABEL_NENHUM;
		loadDescPromocional(pedido, cdTabelaPreco);
	}
	
	public DescPromocional getDescPromocional() {
		if (FrameworkMessages.OPCAO_TODOS.equalsIgnoreCase(String.valueOf(getSelectedItem()))) {
			return null;
		}
		return (DescPromocional) getSelectedItem();
	}
	
	public void setValue(DescPromocional descPromocional) {
		if (descPromocional != null) {
			select(descPromocional);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public String getValue() {
		DescPromocional descPromocional = (DescPromocional) getSelectedItem();
		if (descPromocional != null) {
			return descPromocional.getRowKey();
		}
		return null;
	}
	
	@Override
	protected void addDefaultItens() {
		super.addDefaultItens();
		add(FrameworkMessages.OPCAO_TODOS);
	}
	
	public void loadDescPromocional(Pedido pedido, String cdTabelaPreco) throws SQLException {
		removeAll();
		if (pedido != null) {
			DescPromocional descPromocionalFilter = new DescPromocional();
			descPromocionalFilter.cdEmpresa = pedido.cdEmpresa;
			descPromocionalFilter.cdRepresentante = pedido.cdRepresentante;
			descPromocionalFilter.dtInicial = DateUtil.getCurrentDate();
			descPromocionalFilter.dtFinal = DateUtil.getCurrentDate();
			descPromocionalFilter.cdCliente = pedido.getCliente().getCdCliente();
			descPromocionalFilter.cdGrupoDescCli = pedido.getCliente().cdGrupoDescCli;
			descPromocionalFilter.cdTabelaPreco = ValueUtil.isNotEmpty(cdTabelaPreco) ? cdTabelaPreco : pedido.cdTabelaPreco;
			descPromocionalFilter.cdCondicaoComercial = pedido.cdCondicaoComercial;
			Vector descPromocionalList = DescPromocionalService.getInstance().findAllByExampleCombo(descPromocionalFilter);
			descPromocionalList.qsort();
			add(descPromocionalList);
		}
	}
	
	public boolean isOpcaoTodosSelecionado() {
		return ValueUtil.valueEquals(FrameworkMessages.OPCAO_TODOS, String.valueOf(getSelectedItem()));
	}
	
}
