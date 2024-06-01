package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoVeiculo;
import br.com.wmw.lavenderepda.business.service.TipoVeiculoService;
import totalcross.util.Vector;

public class TipoVeiculoComboBox extends BaseComboBox {

	public TipoVeiculoComboBox() throws SQLException {
		super(Messages.TIPOVEICULO_LABEL_TIPOVEICULO);
		carregaTipoVeiculo();
	}
	
	public String getValue() {
		TipoVeiculo tipoVeiculo = (TipoVeiculo) getSelectedItem();
		if (tipoVeiculo != null) {
			return tipoVeiculo.cdTipoVeiculo;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			TipoVeiculo tipoVeiculo = new TipoVeiculo();
			tipoVeiculo.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoVeiculo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			tipoVeiculo.cdTipoVeiculo = value;
			select(tipoVeiculo);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public void carregaTipoVeiculo() throws SQLException {
		removeAll();
		TipoVeiculo tipoVeiculo = new TipoVeiculo();
		tipoVeiculo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoVeiculo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector tipoVeiculoList = TipoVeiculoService.getInstance().findAllByExample(tipoVeiculo);
		add(tipoVeiculoList);
		qsort();
	}
}
