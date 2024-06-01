package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.service.DapLaudoService;
import br.com.wmw.lavenderepda.business.service.DapMatriculaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDapForm extends LavendereCrudListForm {
	
	private LabelValue lvDsCliente;
	private LabelValue lvDsMatriculaDap;
	private LabelValue lvDsLocal;
	private LabelValue lvDsSafra;
	private DapMatricula dapMatricula;
	private ButtonAction btNovoDap;

	public ListDapForm(DapMatricula dapMatricula) throws SQLException {
		super(Messages.LISTA_DAP_LAUDO_TITULO);
		this.dapMatricula = dapMatricula;
		setBaseCrudCadForm(new CadDapForm());
		singleClickOn = true;
		lvDsCliente = new LabelValue(dapMatricula.cliente.nmRazaoSocial);
		lvDsMatriculaDap = new LabelValue(dapMatricula.cdDapMatricula);
		lvDsLocal = new LabelValue(dapMatricula.getDsLocal());
		lvDsSafra = new LabelValue(dapMatricula.dsSafra);
		constructorListContainer();
		btNovoDap = new ButtonAction(Messages.BOTAO_NOVO_DAP_LAUDO, "images/add.png");
	}
	
	private void constructorListContainer() {
		configListContainer("tb.NUSEQLAUDO");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		String[][] colsSort = new String[4][2];
		colsSort[0][0] = Messages.CAD_DAP_CAMPO_SEQUENCIA_LAUDO;
		colsSort[0][1] = "tb.NUSEQLAUDO";
		colsSort[1][0] = Messages.CAD_DAP_CAMPO_CULTURA;
		colsSort[1][1] = "dapcult.DSDAPCULTURA";
		colsSort[2][0] = Messages.CAD_DAP_CAMPO_AREA;
		colsSort[2][1] = "tb.QTAREA";
		colsSort[3][0] = Messages.CAD_DAP_CAMPO_DTEMISSAO;
		colsSort[3][1] = "tb.DTEMISSAO";
		listContainer.setColsSort(colsSort);
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		DapLaudo dapLaudo = new DapLaudo();
		dapLaudo.cdEmpresa = dapMatricula.cdEmpresa;
		dapLaudo.cdCliente = dapMatricula.cdCliente;
		dapLaudo.cdDapMatricula = dapMatricula.cdDapMatricula;
		dapLaudo.cdSafra = dapMatricula.cdSafra;
		return dapLaudo;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DapLaudoService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lvDsCliente, getLeft(), getTop() + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.LISTA_DAP_CLIENTE_COL_MATRICULA), lvDsMatriculaDap, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.LISTA_DAP_CLIENTE_COL_SAFRA), lvDsSafra, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.LISTA_DAP_CLIENTE_COL_CID_UF_LOC), lvDsLocal, getLeft(), AFTER + HEIGHT_GAP_BIG);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    	UiUtil.add(barBottomContainer, btNovoDap, 5);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btNovoDap) {
				btNovoDapClick();
			}
			break;
		}
	}
	
	@Override
	public void visibleState() {
		super.visibleState();
		try {
			btNovoDap.setVisible(isBtNovoDapVisivel());
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private boolean isBtNovoDapVisivel() throws SQLException {
		return !DapMatriculaService.getInstance().isMatriculaInvalida(dapMatricula.dtValidade);
	}

	@Override
	public void onFormExibition() throws SQLException {
		list();
	}
	
	private void btNovoDapClick() throws SQLException {
		ListDapCulturaWindow listDapClienteCulturaWindow = new ListDapCulturaWindow(dapMatricula);
		listDapClienteCulturaWindow.popup();
		if (listDapClienteCulturaWindow.dapLaudo != null) {
			show(new CadDapForm(listDapClienteCulturaWindow.dapLaudo));
		}
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(dapLaudo.getDsDapLaudo()));
		item.addElement(dapLaudo.getDsStatusLaudo());
		item.addElement(Messages.CAD_DAP_CAMPO_AREA + ": "+ StringUtil.getStringValue(dapLaudo.qtArea));
		item.addElement(StringUtil.getStringValue(dapLaudo.dtEmissao));
		return (String[]) item.toObjectArray();
	}

}
