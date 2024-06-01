package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwComboBoxListWindow;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;

public abstract class LavendereWmwComboBoxListWindow extends WmwComboBoxListWindow {

	public LavendereWmwComboBoxListWindow(String title) {
		super(title);
		btNovo.setVisible(false);
	}
	
	protected void configListContainer(String defaultSortAtributte) {
		configListContainer(defaultSortAtributte, true);
	}

	protected void configListContainer(String defaultSortAtributte, boolean sortAscend) {
		sortAtributte = ListContainerConfig.getDefautSortColumn(getConfigClassName());
		if (ValueUtil.isEmpty(sortAtributte)) {
			sortAtributte = defaultSortAtributte;
		}
		sortAsc = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAsc)) {
			sortAsc = StringUtil.getStringValue(sortAscend);
		}
	}

	protected CrudService getConfigService() {
		return ConfigInternoService.getInstance();
	}

	protected String getConfigClassName() {
		return ClassUtil.getSimpleName(this.getClass());
	}

	protected BaseDomain getDomainConfig() {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
		configInterno.vlChave = getConfigClassName();
		configInterno.vlConfigInterno = sortAtributte+ConfigInterno.defaultSeparatorInfoValue+StringUtil.getStringValue(sortAsc);
		return configInterno;
	}

	protected void beforeOrder() {
		super.beforeOrder();
		saveListConfig();
	}

	private void saveListConfig() {
		try {
			BaseDomain domainConfig = getDomainConfig();
			ListContainerConfig.listasConfig.put(((ConfigInterno)domainConfig).vlChave, StringUtil.split(((ConfigInterno)domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (getConfigService().findByRowKey(domainConfig.getRowKey()) == null) {
				getConfigService().insert(domainConfig);
			} else {
				getConfigService().update(domainConfig);
			}
		} catch (Throwable e) {
		}
	}

}
