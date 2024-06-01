package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class TipoRemessaComboBox extends BaseComboBox {
	
	public enum TipoRemessaEnum {
		TIPOREMESSA_EMPRESA("E", Messages.EMPRESA_NOME_ENTIDADE),
		TIPOREMESSA_REPRESENTANTE("R", Messages.REPRESENTANTE_NOME_ENTIDADE);
		
		public String cdTipo, dsTipo;
		
		private TipoRemessaEnum(String cdTipo, String dsTipo) {
			this.cdTipo = cdTipo;
			this.dsTipo = dsTipo;
		}
		
	}
	
	public TipoRemessaComboBox() {
		super(Messages.REMESSAESTOQUE_TIPO_REMESSA);
		defaultItemType = DefaultItemType_ALL;
		load();
	}
	
	public String getValue() {
		if (TipoRemessaEnum.TIPOREMESSA_EMPRESA.dsTipo.equals(getSelectedItem())) {
			return TipoRemessaEnum.TIPOREMESSA_EMPRESA.cdTipo;
		} else if (TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.dsTipo.equals(getSelectedItem())) {
			return TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.cdTipo;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
	
	public void setValue(String value) {
		if (TipoRemessaEnum.TIPOREMESSA_EMPRESA.cdTipo.equals(value)) {
			select(TipoRemessaEnum.TIPOREMESSA_EMPRESA.dsTipo);
		} else if (TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.cdTipo.equals(value)) {
			select(TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.dsTipo);
		} else {
			select(null);
		}
	}
	
	private void load() {
		addDefaultItens();
		add(new String[] {TipoRemessaEnum.TIPOREMESSA_EMPRESA.dsTipo, TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.dsTipo});
		setSelectedIndex(0);
	}

}
