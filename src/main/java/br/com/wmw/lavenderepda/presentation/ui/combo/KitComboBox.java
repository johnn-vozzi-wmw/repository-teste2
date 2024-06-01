package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.KitService;
import totalcross.util.Vector;

public class KitComboBox extends BaseComboBox {

	public KitComboBox() {
		super(LavenderePdaConfig.isUsaKitProduto() ? Messages.KIT_LABEL_KIT : "");
		this.defaultItemType = DefaultItemType_ALL;
	}

	public KitComboBox(int defaultItemType) throws SQLException {
		super(Messages.KIT_LABEL_KIT);
		this.defaultItemType = defaultItemType;
		loadKit();
		setSelectedIndex(0);
	}

	public KitComboBox(int defaultItemType, String cdTabelaPreco) throws SQLException {
		super(Messages.KIT_LABEL_KIT);
		this.defaultItemType = defaultItemType;
		loadKitByTabPreco(cdTabelaPreco);
	}

	public KitComboBox(Pedido pedido) throws SQLException {
		super(Messages.KIT_LABEL_KIT);
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		if (LavenderePdaConfig.isUsaKitProduto()) {
			loadKit1e2(pedido);
		} else {
			loadKit(pedido);
		}
		setSelectedIndex(0);
	}

	public Kit getKit() {
		Kit kit = (Kit) getSelectedItem();
		if (kit != null) {
			return kit;
		} else {
			return null;
		}
	}

	public String getValue() {
		Kit kit = (Kit) getSelectedItem();
		if (kit != null) {
			return kit.cdKit;
		} else {
			return null;
		}
	}

	public Vector getCdsKit() {
		Vector cdsKit = new Vector();
		for (int i = 1; i < size(); i++) {
			Kit kit = (Kit)getItemAt(i);
			if (kit != null) {
				cdsKit.addElement(StringUtil.getStringValue(kit.cdKit));
			}
		}
		return cdsKit;
	}

	public void setValue(Kit kit) {
		if (kit != null) {
			select(kit);
		} else {
			setSelectedIndex(-1);
		}
	}

	private void loadKit() throws SQLException {
		Kit kit = new Kit();
		kit.cdEmpresa = SessionLavenderePda.cdEmpresa;
		kit.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		kit.dtVigenciaInicial = DateUtil.getCurrentDate();
		kit.dtVigenciaFinal = DateUtil.getCurrentDate();
		add(KitService.getInstance().findAllByExample(kit));
	}


	private void loadKit(Pedido pedido) throws SQLException {
		add(KitService.getInstance().loadKitListForCombo(pedido));
	}
	
	public void loadKit1e2(Pedido pedido) throws SQLException {
		removeAll();
		Vector listRsult = KitService.getInstance().loadKitListForKit1e2(pedido);
		add(listRsult);
	}

	public void loadKitByTabPreco(String cdTabelaPreco) throws SQLException {
		removeAll();
		Vector listRsult = KitService.getInstance().findAllByTabPreco(cdTabelaPreco);
		listRsult.qsort(); // Tem que ser ordenado direto na lista. Se ordenar na combo da erro
		add(listRsult);
	}
}