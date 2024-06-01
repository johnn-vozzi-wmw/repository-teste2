package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.service.TipoSacService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSacForm extends LavendereCrudListForm {
	
	private Cliente cliente;
	public ListSacWindow listSacWindow;
	public ListNovoSacWindow listNovoSacWindow;
	public boolean onSacWindow;
	private boolean filterOnlyNewSac, filterOnlyChangedSac;
	public Vector listSac;
	private boolean relMode;
	

	
	public ListSacForm(Cliente clienteSelecionado) throws SQLException {
		this(clienteSelecionado, false);
	}
	
	public ListSacForm(Cliente clienteSelecionado, boolean onSacWindow) throws SQLException {
		this(clienteSelecionado, onSacWindow, false, false, false);
	}
	
	public ListSacForm(Cliente clienteSelecionado, boolean onSacWindow, boolean filterOnlyNewSac, boolean filterOnlyChangedSac, boolean relMode) throws SQLException {
		super(Messages.SAC_LISTA_TITULO);
		this.cliente = clienteSelecionado;
		this.filterOnlyNewSac = filterOnlyNewSac; 
		this.filterOnlyChangedSac = filterOnlyChangedSac;
		singleClickOn = true;
		this.onSacWindow = onSacWindow;
		this.relMode = relMode;


		setBaseCrudCadForm(new CadSacForm(false));
		constructorListContainer();
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(6, 2);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		listContainer.setColPosition(5, RIGHT);
		configListContainer("DTCADASTRO, HRCADASTRO", "N, N");
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new Sac();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return SacService.getInstance();
	}
	
	@Override
	public void visibleState() {
		super.visibleState();
		barTopContainer.setVisible(!onSacWindow);
		barBottomContainer.setVisible(!relMode);
	}
	
	protected int getTop() {
    	if (onSacWindow || relMode) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }
	
	@Override
	public void singleClickInList() throws SQLException {
		if (onSacWindow) {
			if (listNovoSacWindow != null) {
				listNovoSacWindow.edit(getSelectedDomain());
			} else {
				listSacWindow.edit(getSelectedDomain());
			}
		} else {
			super.singleClickInList();
		}
	}
	
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Sac sac = (Sac) domain;
		sac.cdEmpresa = SessionLavenderePda.cdEmpresa;
		sac.cdCliente = cliente.cdCliente;
		sac.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(getClass());
		if (filterOnlyChangedSac) {
			sac.flStatusAlterado = ValueUtil.VALOR_SIM;
		}
		if (filterOnlyNewSac) {
			sac.filterBySacNaoExibido = true;
		}
		listSac = SacService.getInstance().findAllByExample(sac);
		return listSac;
	}
	
	protected String[] getItem(Object domain) throws SQLException {
		Sac sac = (Sac) domain;
		String lbDtPrevisao = LavenderePdaConfig.usaOcorrenciaSACComWorkflow ? Messages.SAC_LABEL_PREVISAO + " - " + StringUtil.getStringValue(sac.dtPrevisao) : "";
		return new String[] {
				StringUtil.getStringValue(sac.cdSac),
				" - "  + StringUtil.getStringValue(TipoSacService.getInstance().getDsTipoSac(sac.cdTipoSac)),
				StringUtil.getStringValue(Messages.SAC_LABEL_CADASTRO + " - " + StringUtil.getStringValue(sac.dtCadastro) + " - " + StringUtil.getStringValue(sac.hrCadastro)),
				StringUtil.getStringValue(""),
				StringUtil.getStringValue(lbDtPrevisao),
				StringUtil.getStringValue(sac.getDsTipoSac(sac.cdStatusSac))
		};
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer); 
		if (!relMode && LavenderePdaConfig.isPermiteGerirListaSacCliente() && !LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
			UiUtil.add(barBottomContainer, btNovo, 5);
		}
		listContainer.setRect(LEFT, getTop(), FILL, relMode ? FILL : FILL - barBottomContainer.getHeight() );
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	

}
