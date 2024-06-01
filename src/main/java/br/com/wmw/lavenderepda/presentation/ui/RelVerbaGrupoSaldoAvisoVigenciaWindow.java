package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelVerbaGrupoSaldoAvisoVigenciaWindow extends WmwListWindow {
	
	private LabelValue lvMensagemVigencia;

	public RelVerbaGrupoSaldoAvisoVigenciaWindow() {
		super(Messages.VERBAGRUPOSALDO_TITULO_AVISO_VEGENCIA);
		lvMensagemVigencia = new LabelValue();
		configListContainer();
		setDefaultRect();
	}
	
	private void configListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		listContainer.setColPosition(3, RIGHT);
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) domain;
		return VerbaGrupoSaldoService.getInstance().findAllVerbaGrupoSaldoForAvisoTerminoVigencia(verbaGrupoSaldo.cdEmpresa, verbaGrupoSaldo.cdRepresentante);
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) domain;
		String[] item = {Messages.VERBA_GRUPO_PRODUTO + GrupoProduto1Service.getInstance().getDsGrupoProduto(verbaGrupoSaldo.cdGrupoProduto1),
			"",
			Messages.VERBA_GRUPO_SALDO + StringUtil.getStringValueToInterface(verbaGrupoSaldo.vlSaldo),
			Messages.DT_FINAL_VIGENCIA + verbaGrupoSaldo.dtVigenciaFinal};
		return item;
	}

	@Override
	protected String getSelectedRowKey() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return VerbaGrupoSaldoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		VerbaGrupoSaldo verbaGrupoSaldo = new VerbaGrupoSaldo();
		verbaGrupoSaldo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		verbaGrupoSaldo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return verbaGrupoSaldo;
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, lvMensagemVigencia, getLeft(), TOP + HEIGHT_GAP, FILL - WIDTH_GAP_BIG, PREFERRED);
		lvMensagemVigencia.setMultipleLinesText(MessageUtil.getMessage(Messages.VERBAGRUPOSALDO_MSG_AVISO_VIGENCIA, LavenderePdaConfig.nuDiasRestantesAvisoSaldoVerbaGrupo));
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
	public boolean hasInfoToShow() {
		return listContainer.size() > 0;
	}

}
