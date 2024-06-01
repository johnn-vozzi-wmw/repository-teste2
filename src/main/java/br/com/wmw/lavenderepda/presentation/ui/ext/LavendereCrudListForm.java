package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;

public abstract class LavendereCrudListForm extends BaseCrudListForm {

	public LavendereCrudListForm(String newTitle) {
		super(newTitle);
	}

	protected CrudService getConfigService() {
		return ConfigInternoService.getInstance();
	}

	protected BaseDomain getDomainConfig() {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
		configInterno.vlChave = getConfigClassName();
		configInterno.vlConfigInterno = sortAtributte + ConfigInterno.defaultSeparatorInfoValue + StringUtil.getStringValue(sortAsc);
		return configInterno;
	}

	protected void beforeOrder() {
		super.beforeOrder();
		saveListConfig();
	}

	protected void saveListConfig() {
		try {
			BaseDomain domainConfig = getDomainConfig();
			ListContainerConfig.listasConfig.put(((ConfigInterno)domainConfig).vlChave, StringUtil.split(((ConfigInterno)domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (getConfigService().findByRowKey(domainConfig.getRowKey()) == null) {
				getConfigService().insert(domainConfig);
			} else {
				getConfigService().update(domainConfig);
			}
		} catch (Throwable e) {
			//--
		}
	}

}
