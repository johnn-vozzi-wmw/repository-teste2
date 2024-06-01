package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.ElementNotFoundException;
import totalcross.util.IntHashtable;
import totalcross.util.Vector;

public class TipoNovidadeComboBox extends BaseComboBox {
	
	public TipoNovidadeComboBox() throws SQLException {
		super(Messages.RELNOVIDADEPRODUTO_LABEL_CDTIPONOVIDADE);
		this.defaultItemType = BaseComboBox.DefaultItemType_ALL;
		loadTipoNovidade();
	}

	public String getValue() throws SQLException {
		TipoNovidade tipoNovidade = (TipoNovidade) getSelectedItem();
		return tipoNovidade != null ? tipoNovidade.cdTipoNovidade : null;
	}


	public void setValue(String value) throws SQLException {
		TipoNovidade tipoNovidade = new TipoNovidade();
		tipoNovidade.cdTipoNovidade = value;
		select(tipoNovidade);
	}

	private void loadTipoNovidade() throws SQLException {
		removeAll();
		TipoNovidade tipoNovidade = new TipoNovidade();
		tipoNovidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector tipoNovidadeList = TipoNovidadeService.getInstance().findAllByExample(tipoNovidade);
		if (!LavenderePdaConfig.apresentaNovidadesClienteRelatorioNovidadeProduto) {
			tipoNovidade.cdTipoNovidade =  TipoNovidade.TIPONOVIDADECLIENTE_NOVO_CLIENTE;
			tipoNovidadeList.removeElement(tipoNovidade);
		}
		add(tipoNovidadeList);
	}

	public void addQtItensTipo(IntHashtable hashQtItensTipo) throws SQLException {
		TipoNovidade tipoNovidade;
		for (int i = 1; i < size(); i++) {
			tipoNovidade = (TipoNovidade) getItemAt(i);
			try {
				tipoNovidade.qtItensTipo = hashQtItensTipo.get(StringUtil.getStringValue(tipoNovidade.cdTipoNovidade));
			} catch (ElementNotFoundException e) {
				tipoNovidade.qtItensTipo = 0;
			}
		}
	}

}
