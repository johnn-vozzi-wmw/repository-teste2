package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.service.DescProgConfigFamService;
import totalcross.util.Vector;

public class DescProgFamiliaComboBox extends BaseComboBox {
	
	private String cdDescProgressivo;
	
	public DescProgFamiliaComboBox() {
		super(Messages.DESC_PROG_FAMILIA_PRODUTOS_TITLE);
		this.defaultItemType = DefaultItemType_ALL;
	}
	
	public void load(DescProgressivoConfig descProg, boolean tipoConsome, boolean tipoProduz, boolean tipoAcumula, boolean tipoAcumulaMax) throws SQLException {
		removeAll();
		DescProgConfigFam filter = new DescProgConfigFam();
		if (descProg != null) {
			filter.cdEmpresa = descProg.cdEmpresa;
			filter.cdRepresentante = descProg.cdRepresentante;
			filter.cdDescProgressivo = cdDescProgressivo = descProg.cdDescProgressivo;
			filter.flTipoFamiliaCon = tipoConsome ? ValueUtil.VALOR_SIM : null;
			filter.flTipoFamiliaPro = tipoProduz ? ValueUtil.VALOR_SIM : null;
			filter.flFamAcuValorMin = tipoAcumula ? ValueUtil.VALOR_SIM : null;
			filter.flFamAcuValorMax = tipoAcumulaMax ? ValueUtil.VALOR_SIM : null;
			Vector list = DescProgConfigFamService.getInstance().findAllByExample(filter);
			add(list);
		}
	}
	
	public String getValue() {
		DescProgConfigFam descProgConfigFam = (DescProgConfigFam) getSelectedItem();
		return descProgConfigFam != null ? descProgConfigFam.cdFamiliaDescProg : null;
	}
	
	public void setValue(String cdFamilia) {
		DescProgConfigFam descProgConfigFam = new DescProgConfigFam();
		descProgConfigFam.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descProgConfigFam.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(DescProgConfigFam.class);
		descProgConfigFam.cdDescProgressivo = cdDescProgressivo;
		descProgConfigFam.cdFamiliaDescProg = cdFamilia;
		select(descProgConfigFam);
	}

}
