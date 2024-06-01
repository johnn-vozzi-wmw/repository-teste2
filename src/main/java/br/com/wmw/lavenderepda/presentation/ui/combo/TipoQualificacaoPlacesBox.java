package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Place;

public class TipoQualificacaoPlacesBox extends BaseComboBox {

	public TipoQualificacaoPlacesBox() {
		super(Messages.PLACES_LABEL_QUALIFICACAO);
		this.defaultItemType = DefaultItemType_ALL;
        load(false);
	}

	public TipoQualificacaoPlacesBox(boolean detalhePlace) {
		super(Messages.PLACES_LABEL_QUALIFICACAO);
		load(detalhePlace);
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (Messages.OPCAO_QUALIFICADO.equals(select)) {
			return Place.TIPO_QUALIFICADO;
		} else if (Messages.OPCAO_EM_ANALISE.equals(select)) {
			return Place.TIPO_EM_ANALISE;
		} else if (Messages.OPCAO_NAO_QUALIFICADO.equals(select)) {
			return Place.TIPO_NAO_QUALIFICADO;
		}
		return null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isEmpty(value)) {
			return;
		}
		setSelectedItem(value);
 	}

	@Override
	public void setSelectedItem(Object name) {
		if (Place.TIPO_QUALIFICADO.equals(name)) {
			name = Messages.OPCAO_QUALIFICADO;
		} else if (Place.TIPO_EM_ANALISE.equals(name)) {
			name = Messages.OPCAO_EM_ANALISE;
		} else if (Place.TIPO_NAO_QUALIFICADO.equals(name)) {
			name = Messages.OPCAO_NAO_QUALIFICADO;
		}
		super.setSelectedItem(name);
	}
	
	private void load(boolean detalhePlace) {
		if (!detalhePlace) {
			add(FrameworkMessages.OPCAO_TODOS);	
		}
		add(Messages.OPCAO_QUALIFICADO);
		add(Messages.OPCAO_EM_ANALISE);
		add(Messages.OPCAO_NAO_QUALIFICADO);
	}
}
