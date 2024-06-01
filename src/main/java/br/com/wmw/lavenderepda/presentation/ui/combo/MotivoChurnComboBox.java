package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotivoChurn;
import br.com.wmw.lavenderepda.business.service.MotivoChurnService;
import totalcross.util.Vector;

public class MotivoChurnComboBox extends BaseComboBox {
	
	public MotivoChurnComboBox() throws SQLException {
		super(Messages.MOTIVO_CHURN_COMBO);
		this.defaultItemType = DefaultItemType_ALL;
		carregaDadosCombo();
		setSelectedIndex(0);
	}
	
	@Override
	protected void addDefaultItens() {
		super.addDefaultItens();
		adicionaSemMotivoChurn();
	}
	
	public String getValue() {
		MotivoChurn motivoChurn = (MotivoChurn) getSelectedItem();
		return motivoChurn!= null ? motivoChurn.cdMotivoChurn : ValueUtil.VALOR_NI;
	}
	
	public void setValue(String value) {
		select(new MotivoChurn(value));
	}
	
	private void adicionaSemMotivoChurn() {
		MotivoChurn motivoChurn = new MotivoChurn(MotivoChurn.CD_SEM_MOTIVO_INFORMADO);
		motivoChurn.dsMotivoChurn = MotivoChurn.DS_SEM_MOTIVO_INFORMADO; 
		add(motivoChurn);
	}
	
	private void carregaDadosCombo() throws SQLException {
		Vector motivoChurnList = MotivoChurnService.getInstance().findAll();
		motivoChurnList.qsort();
		add(motivoChurnList);
	}

}
