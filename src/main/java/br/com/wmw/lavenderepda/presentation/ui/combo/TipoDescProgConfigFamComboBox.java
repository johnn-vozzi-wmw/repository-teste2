package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;

public class TipoDescProgConfigFamComboBox extends BaseComboBox {

	public final static String FL_CONSOME = "C";
	public final static String FL_PRODUZ = "P";
	public final static String FL_ACUMULA = "A";
	public final static String FL_ACUMULAMAX = "M";
	@SuppressWarnings("unused")
	private final boolean filterPedido;

	public TipoDescProgConfigFamComboBox() {
		this(false);
	}

	public TipoDescProgConfigFamComboBox(boolean filterPedido) {
		super(Messages.DESC_PROG_CONFIG_TIPOCONFIGFAM);
		this.defaultItemType = filterPedido ? BaseComboBox.DefaultItemType_ALL : BaseComboBox.DefaultItemType_NONE;
		this.filterPedido = filterPedido;
		if (!filterPedido) {
			load();
		}
	}

	public void setValue(String selected) {
		if (selected.equals(FL_CONSOME)) {
			select(Messages.DESC_PROG_FAM_COMBO_CONSOME);
		} else if (selected.equals(FL_PRODUZ)) {
			select(Messages.DESC_PROG_FAM_COMBO_PRODUZ);
		} else if (selected.equals(FL_ACUMULA)) {
			select(Messages.DESC_PROG_FAM_COMBO_ACUMULA);
		} else if (selected.equals(FL_ACUMULAMAX)) {
			select(Messages.DESC_PROG_FAM_COMBO_ACUMULA_MAX);
		} else {
			select(null);
		}
	}

	public void applyValue(DescProgConfigFam descProgConfigFam) {
		String select = (String) getSelectedItem();
		descProgConfigFam.flFamAcuValorMin = null;
		descProgConfigFam.flFamAcuValorMax = null;
		descProgConfigFam.flTipoFamiliaCon = null;
		descProgConfigFam.flTipoFamiliaPro = null;
		if (ValueUtil.isEmpty(select)) return;
		if (select.equals(Messages.DESC_PROG_FAM_COMBO_CONSOME)) {
			descProgConfigFam.flTipoFamiliaCon = ValueUtil.VALOR_SIM;
		} else if (select.equals(Messages.DESC_PROG_FAM_COMBO_PRODUZ)) {
			descProgConfigFam.flTipoFamiliaPro = ValueUtil.VALOR_SIM;
		} else if (select.equals(Messages.DESC_PROG_FAM_COMBO_ACUMULA)) {
			descProgConfigFam.flFamAcuValorMin = ValueUtil.VALOR_SIM;
		} else if (select.equals(Messages.DESC_PROG_FAM_COMBO_ACUMULA_MAX)) {
			descProgConfigFam.flFamAcuValorMax = ValueUtil.VALOR_SIM;
		}
	}

	public void load() {
		load(false);
	}

	public void load(boolean addDefault) {
		removeAll();
		if (addDefault) {
			addDefaultItens();
		}
		add(Messages.DESC_PROG_FAM_COMBO_CONSOME);
		add(Messages.DESC_PROG_FAM_COMBO_PRODUZ);
		add(Messages.DESC_PROG_FAM_COMBO_ACUMULA);
		add(Messages.DESC_PROG_FAM_COMBO_ACUMULA_MAX);
	}

}
