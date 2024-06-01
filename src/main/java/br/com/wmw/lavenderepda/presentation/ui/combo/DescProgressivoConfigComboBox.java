package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import totalcross.util.Vector;

public class DescProgressivoConfigComboBox extends BaseComboBox {

	public DescProgressivoConfigComboBox(int defaultType) throws SQLException {
		this(null, defaultType);
	}
	
	public DescProgressivoConfigComboBox(Cliente cliente) throws SQLException {
		this(cliente, BaseComboBox.DefaultItemType_LABEL_NENHUM);
	}
	
	public DescProgressivoConfigComboBox(Cliente cliente, int defaultItemType) throws SQLException {
		super(Messages.DESC_PROG_CONFIG_NM_ENTIDADE);
		this.defaultItemType = defaultItemType;
		if (cliente != null) {
			load(cliente);
		}
		setSelectedIndex(0);
	}

	public DescProgressivoConfig getValue() {
		Object value = getSelectedItem();
		return value == null ? null : (DescProgressivoConfig)value;
	}

	public void load(Cliente cliente) throws SQLException {
		DescProgressivoConfig descProgressivoConfig = new DescProgressivoConfig();
		descProgressivoConfig.cdEmpresa = cliente.cdEmpresa;
		descProgressivoConfig.cdRepresentante = cliente.cdRepresentante;
		descProgressivoConfig.cliente = cliente;
		descProgressivoConfig.dtInicialVigencia = DateUtil.getCurrentDate();
		descProgressivoConfig.dtFimVigencia = descProgressivoConfig.dtInicialVigencia;
		loadByExample(descProgressivoConfig);
	}
	
	public void loadAll() throws SQLException {
		DescProgressivoConfig descProgressivoConfig = new DescProgressivoConfig();
		descProgressivoConfig.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;				
		descProgressivoConfig.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descProgressivoConfig.dtFimVigencia = descProgressivoConfig.dtInicialVigencia = DateUtil.getCurrentDate();
		loadByExample(descProgressivoConfig);
	}
	
	private void loadByExample(DescProgressivoConfig example)  throws SQLException {
		removeAll();
		Vector descProgressivoConfigList = DescProgressivoConfigService.getInstance().findAllByExample(example);
		if (descProgressivoConfigList != null) {
			add(descProgressivoConfigList);
		}
	}
}
