package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoConfigService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListPesquisaMercadoConfigForm extends LavendereCrudListForm {

	private LabelContainer lcClienteSessao;
	private EditDate edDateVigenciaInicio;
	private EditDate edDateVigenciaFim;

	public ListPesquisaMercadoConfigForm() throws SQLException {
		this(null);
	}

	public ListPesquisaMercadoConfigForm(String nuPedido) throws SQLException {
		super(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
		lcClienteSessao = new LabelContainer(SessionLavenderePda.getCliente().toString());
		edDateVigenciaInicio = new EditDate();
		edDateVigenciaFim = new EditDate();
		edDateVigenciaInicio.onlySelectByCalendar();
		edDateVigenciaFim.onlySelectByCalendar();
		singleClickOn = true;
		listResizeable = false;
		CadPesquisaMercadoProdutoForm cadPesquisaMercadoProdutoForm = new CadPesquisaMercadoProdutoForm();
		cadPesquisaMercadoProdutoForm.nuPedido = nuPedido;
		setBaseCrudCadForm(cadPesquisaMercadoProdutoForm);
		constructorListContainer();
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		listContainer = new GridListContainer(LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? 7 : 6, 2);
		listContainer.setColPosition(5, RIGHT);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
		ScrollPosition.AUTO_HIDE = true;
	}

	@Override
	protected Vector getDomainList() throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfigFilter = new PesquisaMercadoConfig();
		pesquisaMercadoConfigFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pesquisaMercadoConfigFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pesquisaMercadoConfigFilter.dtInicialVigencia = edDateVigenciaInicio.getValue();
		pesquisaMercadoConfigFilter.dtFimVigencia = edDateVigenciaFim.getValue();
		return PesquisaMercadoConfigService.getInstance().findAllByExampleWithQuantidades(pesquisaMercadoConfigFilter);
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getDomainList();
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) domain;
		String[] item = new String[LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? 7 : 6];
		return PesquisaMercadoConfigService.getInstance().getListFormItem(pesquisaMercadoConfig, item);
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new PesquisaMercadoConfig();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PesquisaMercadoConfigService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lcClienteSessao, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, new LabelName(Messages.PESQUISA_MERCADO_PROD_CONC_VIGENCIA_FILTRO), edDateVigenciaInicio, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, edDateVigenciaFim, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edDateVigenciaInicio) {
					edDateVigenciaInicioClick();
				} else if (event.target == edDateVigenciaFim) {
					edDateVigenciaFimClick();
				}
			}
		}
	}

	private void edDateVigenciaInicioClick() throws SQLException {
		if (dateIsEmpty()) {
			return;
		}
		if (edDateVigenciaInicio.getValue().isAfter(edDateVigenciaFim.getValue())) {
			edDateVigenciaFim.setValue(edDateVigenciaInicio.getValue());
			list();
			return;
		}
		list();
	}

	private void edDateVigenciaFimClick() throws SQLException {
		if (dateIsEmpty()) {
			return;
		}
		if (edDateVigenciaFim.getValue().isBefore(edDateVigenciaInicio.getValue())) {
			edDateVigenciaFim.setValue(edDateVigenciaInicio.getValue());
			UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_PROD_CONC_VIGENCIA_FILTRO_ERRO_FINAL_ANTERIOR_INICIAL);
		}
		list();
	}

	private boolean dateIsEmpty() {
		return ValueUtil.isEmpty(edDateVigenciaInicio.getValue()) || ValueUtil.isEmpty(edDateVigenciaFim.getValue());
	}

	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		list();
	}

}
