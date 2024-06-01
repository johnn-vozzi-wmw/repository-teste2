package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.AbstractCadCoordenadasWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class CadPesquisaMercadoRegCoordenadaWindow extends AbstractCadCoordenadasWindow {

	private LabelName lbCliente;
	private LabelValue lvCliente;

	public CadPesquisaMercadoRegCoordenadaWindow(boolean auto) {
		super(auto);
		this.lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		this.lvCliente = new LabelValue(SessionLavenderePda.getCliente().getDsDomain());
		setDefaultRect();
	}

	@Override
	protected String getWindowTitle() {
		return Messages.PESQUISA_MERCADO_PROD_CONC_COORDENADAS;
	}

	@Override
	protected void addComponentes() {
		UiUtil.add(this, lbCliente, lvCliente, getLeft(), getNextY());
	}

	@Override
	protected int getTimeOut() {
		return LavenderePdaConfig.timeOutCoordenadaAutoPesquisaMercado();
	}

}
