package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DapCultura;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import br.com.wmw.lavenderepda.business.service.DapCulturaService;
import br.com.wmw.lavenderepda.business.service.DapLaudoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDapCulturaWindow extends WmwListWindow  {
	
	private DapMatricula dapMatricula;
	public DapLaudo dapLaudo;

	public ListDapCulturaWindow(DapMatricula dapMatricula) {
		super(Messages.LISTA_DAP_CULTURA_WINDOW);
		this.dapMatricula = dapMatricula;
		constructorListContainer();
		singleClickOn = true;
		setDefaultRect();
	}

	private void constructorListContainer() {
    	listContainer = new GridListContainer(1, 1);
    	listContainer.setBarTopSimple();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getCrudService().findAllByExample(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DapCultura dapCultura = (DapCultura) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(dapCultura.dsDapCultura + " ("+dapCultura.qtArea+" ha) "));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return "";
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DapCulturaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		DapCultura dapCultura = new DapCultura();
		dapCultura.cdEmpresa = dapMatricula.cdEmpresa;
		dapCultura.cdCliente = dapMatricula.cdCliente;
		dapCultura.cdDapMatricula = dapMatricula.cdDapMatricula;
		dapCultura.cdSafra = dapMatricula.cdSafra;
		return dapCultura;
	}

	@Override
	protected void onFormStart() throws SQLException {
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {}
	
	private void populateDapLaudo(DapCultura dapCultura) throws SQLException {
		dapLaudo = new DapLaudo();
		dapLaudo.cdEmpresa = dapCultura.cdEmpresa;
		dapLaudo.cdSafra = dapCultura.cdSafra;
		dapLaudo.cdCliente = dapCultura.cdCliente;
		dapLaudo.cdDapMatricula = dapCultura.cdDapMatricula;
		dapLaudo.cdDapCultura = dapCultura.cdDapCultura;
		dapLaudo.nuSeqLaudo = DapLaudoService.getInstance().findMaxKey(dapLaudo, "NUSEQLAUDO") + 1;
		dapLaudo.cdDapLaudo = DapLaudoService.getInstance().generateIdGlobal();
		dapLaudo.dsSafra = dapMatricula.dsSafra;
		dapLaudo.nmRazaoSocial = dapMatricula.cliente.nmRazaoSocial;
		dapLaudo.nuCnpj = dapMatricula.cliente.nuCnpj;
		dapLaudo.dsCidade = dapMatricula.dsCidade;
		dapLaudo.dsLocalidade = dapMatricula.dsLocalidade;
		dapLaudo.cdUf = dapMatricula.cdUf;
		dapLaudo.dapCultura = dapCultura;
		dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_ABERTO;
	}

	@Override
	public void detalhesClick() throws SQLException {
		DapCultura dapCultura = (DapCultura) DapCulturaService.getInstance().findByRowKey(listContainer.getSelectedId());
		if (DapCulturaService.getInstance().isNuSequenciaValido(dapCultura, dapMatricula)) {
			populateDapLaudo(dapCultura);
			if (isExisteDapAbertoParaEssaCultura()) {
				UiUtil.showErrorMessage(Messages.CAD_DAPLAUDO_ERRO_DAP_ABERTO);
				dapLaudo = null;
				return;
			}
			unpop();
		} else {
			UiUtil.showErrorMessage(Messages.CAD_DAP_ERRO_NUMAX_SEQUENCIA);
		}
	}
	
	private boolean isExisteDapAbertoParaEssaCultura() throws SQLException {
		return DapLaudoService.getInstance().countByExample(dapLaudo) > 0;
	}
	
}
