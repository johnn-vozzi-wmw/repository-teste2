package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MetaVenda;
import br.com.wmw.lavenderepda.business.service.MetaVendaService;

public class MetaVendaComboBox  extends BaseComboBox {

	public MetaVendaComboBox(String cdRepresentante) throws SQLException {
		super(Messages.META_VENDA_META);
		load(cdRepresentante);
	}

	public String getValue() {
		MetaVenda metaVenda = (MetaVenda)getSelectedItem();
		if (metaVenda != null) {
			return metaVenda.cdMetaVenda;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			MetaVenda metaVenda = new MetaVenda();
			metaVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
			metaVenda.cdMetaVenda = value;
			select(metaVenda);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load(String cdRepresentante) throws SQLException {
		removeAll();
		add(MetaVendaService.getInstance().findAllMetasVigentesRepresentante(cdRepresentante));
		qsort();
	}

}
