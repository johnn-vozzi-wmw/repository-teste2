package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import br.com.wmw.lavenderepda.business.service.RequisicaoServTipMotService;

public class RequisicaoServTipoComboBox extends BaseComboBox {
	
	private boolean obrigaCliente;
	private boolean obrigaPedido;
	
	public RequisicaoServTipoComboBox(int defaultItemType) throws SQLException {
		this(defaultItemType, false, false);
	}
	
	public RequisicaoServTipoComboBox(int defaultItemType, boolean obrigaCliente, boolean obrigaPedido) throws SQLException {
		super(Messages.REQUISICAOSERV_COMBO_TIPO_SERVICO);
		this.obrigaCliente = obrigaCliente;
		this.obrigaPedido = obrigaPedido;
		this.defaultItemType = defaultItemType;
		loadRequisicaoServTipo();
	}
	
	public boolean isFlObrigaCliente() {
		if (getSelectedItem() instanceof RequisicaoServTipo) {
			return ((RequisicaoServTipo)getSelectedItem()).isObrigaCliente();			
		}
		return false;
	}
	
	public boolean isFlObrigaPedido() {
		if (getSelectedItem() instanceof RequisicaoServTipo) {
			return ((RequisicaoServTipo)getSelectedItem()).isObrigaPedido();			
		}
		return false;
	}
	
	public String getDsStatusClienteList() {
		if (getSelectedItem() instanceof RequisicaoServTipo) {
			String flFiltroStatus = getFlFiltraClienteStatus();
			 if (ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_RESTRITO, flFiltroStatus) || ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_EXCECAO, flFiltroStatus)) {
				 return ((RequisicaoServTipo)getSelectedItem()).dsStatusClienteList;
			 }
		}
		return null;
	}
	
	public String getFlFiltraClienteStatus() {
		if (getSelectedItem() instanceof RequisicaoServTipo) {
			return ((RequisicaoServTipo)getSelectedItem()).flFiltroStatusCliente;
		}
		return null;
	}
	
	
	public String getValue() {
		if (getSelectedItem() instanceof RequisicaoServTipo) {
			return ((RequisicaoServTipo)getSelectedItem()).cdRequisicaoServTipo;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private void loadRequisicaoServTipo() throws SQLException {
		add(RequisicaoServTipMotService.getInstance().findAllTipo(obrigaCliente, obrigaPedido));
		setSelectedIndex(size() == 2 ? 1 : 0);
	}
	
}
