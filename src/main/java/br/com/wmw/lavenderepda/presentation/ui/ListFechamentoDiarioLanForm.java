package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioLanService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.Event;
import totalcross.util.Date;

import java.sql.SQLException;

public class ListFechamentoDiarioLanForm extends LavendereCrudListForm {

	private final Date dtFechamentoDiario;
	private final RelFechamentoDiarioForm relFechamentoDiarioForm;

	public ListFechamentoDiarioLanForm(RelFechamentoDiarioForm relFechamentoDiarioForm, Date dtFechamentoDiario) throws SQLException {
		super(Messages.FECHAMENTO_DIARIO_LAN_LISTA);
		this.relFechamentoDiarioForm = relFechamentoDiarioForm;
		setBaseCrudCadForm(new CadFechamentoDiarioLanForm(dtFechamentoDiario));
		constructorListContainer();
		this.dtFechamentoDiario = dtFechamentoDiario;
		btNovo.setVisible(!FechamentoDiarioService.getInstance().isFechamentoDiarioExecutado(dtFechamentoDiario));
		listResizeable = false;
		singleClickOn = true;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return FechamentoDiarioLanService.getInstance().getFilterByDate(dtFechamentoDiario);
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return FechamentoDiarioLanService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btNovo, 5);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {

	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		return FechamentoDiarioLanService.getInstance().getItemForInterface((FechamentoDiarioLan) domain);
	}

	@Override
	protected void voltarClick() throws SQLException {
		FechamentoDiario fechamentoDiario = FechamentoDiarioService.getInstance().findFechamentoDiarioPorData(dtFechamentoDiario);
		relFechamentoDiarioForm.atualizaValores(fechamentoDiario);
		relFechamentoDiarioForm.setaVisibilidadeBotaoFechar(fechamentoDiario);
		relFechamentoDiarioForm.setaVisibilidadeBotaoLiberarSenha(fechamentoDiario);
		relFechamentoDiarioForm.ajustaBotoesDinamicamente();
		super.voltarClick();
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer("CDTIPOLANCAMENTO");
		listContainer = new GridListContainer(3, 1);
		listContainer.setColsSort(new String[][]{
				{Messages.TIPO_LANCAMENTO_CDLANCAMENTO, "CDTIPOLANCAMENTO"},
				{Messages.TIPO_LANCAMENTO_DSLANCAMENTO, "DSTIPOLANCAMENTO"},
				{Messages.FECHAMENTO_DIARIO_LAN_DSVALORTOTALLANCAMENTO, "VLTOTALLANCAMENTO"}
		});
		listContainer.setColTotalizerRight(2, Messages.FECHAMENTO_DIARIO_LAN_DSVALORTOTALLANCAMENTO);
		ScrollPosition.AUTO_HIDE = true;
	}

}
