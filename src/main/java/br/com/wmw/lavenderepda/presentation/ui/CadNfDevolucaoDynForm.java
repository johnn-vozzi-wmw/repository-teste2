package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.service.NfDevolucaoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadNfDevolucaoDynForm extends BaseLavendereCrudPersonCadForm {
	
	private int TABPANEL_NFDEVOLUCAO;
	private int TABPANEL_ITENS_NFDEVOLUCAO;
	
	private LabelContainer containerDadosCli;
	protected NfDevolucao notaDevolucao;
	private LabelValue lbSerie;
	private LabelValue lbNuNfDevolucao;
	private ButtonAction btAprovarNfDevolucao;
	private ButtonAction btReprovarNfDevolucao;
	private boolean onPopUp;
	public boolean aprovouNota;
	
	public CadNfDevolucaoDynForm() {
		super(null);
	}

    public CadNfDevolucaoDynForm(NfDevolucao notaDevolucao, boolean onPopUp) {
        super(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
        this.notaDevolucao = notaDevolucao;
        this.onPopUp = onPopUp;
        lbSerie = new LabelValue();
        lbNuNfDevolucao = new LabelValue();
        containerDadosCli = new LabelContainer(SessionLavenderePda.getCliente().toString());
        btAprovarNfDevolucao = new ButtonAction(Messages.NFDEVOLUCAO_APROVAR, "images/ok.png");
        btReprovarNfDevolucao = new ButtonAction(Messages.NFDEVOLUCAO_REPROVAR, "images/cancel.png");
        setReadOnly();
		if ((ValueUtil.isEmpty(notaDevolucao.flAprovacao) || NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(notaDevolucao.flAprovacao)) && !onPopUp) {
			btAprovarNfDevolucao.setVisible(true);
			btReprovarNfDevolucao.setVisible(true);
		}

	}
    
	@Override
	protected String getDsTable() throws SQLException {
		return NfDevolucao.TABLE_NAME;
	}

    @Override
    protected String getEntityDescription() {
    	return title;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return NfDevolucaoService.getInstance();
    }

    @Override
    protected BaseDomain createDomain() throws SQLException {
        return new NfDevolucao();
    }


    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	NfDevolucao nfDevolucao = (NfDevolucao) domain;
        super.domainToScreen(domain);
        
        lbSerie.setValue(nfDevolucao.cdSerie);
        lbNuNfDevolucao.setValue(nfDevolucao.nuNfDevolucao);
    }
    
	@Override
	protected void addTabDinamica() {
		if (hashTabs.size() > 1) {
			UiUtil.add(scrollable ? scBase : this, tabDinamica, getNextY(), getBottomTab(), ColorUtil.formsBackColor);
		} else {
			UiUtil.add(this, scrollContainer, LEFT, getNextY(), FILL, FILL - getBottomTab());
			scrollContainer.setBackColor(ColorUtil.formsBackColor);
		}
	}

	@Override
	protected void addTabsFixas(Vector tableTitles) throws SQLException {
		int indexNfDevolucao = tableTitles.indexOf(Messages.NFDEVOLUCAO_TAB_NFD);
		if (indexNfDevolucao == -1) {
			tableTitles.addElement(Messages.NFDEVOLUCAO_TAB_NFD);
			TABPANEL_NFDEVOLUCAO = tableTitles.size() - 1;
		} else {
			TABPANEL_NFDEVOLUCAO = indexNfDevolucao;
		}
		
		int indexItensNfD = tableTitles.indexOf(Messages.NFDEVOLUCAO_TAB_ITENS_NFD);
		if (indexItensNfD == -1) {
			tableTitles.addElement(Messages.NFDEVOLUCAO_TAB_ITENS_NFD);
			TABPANEL_ITENS_NFDEVOLUCAO = tableTitles.size() - 1;
		} else {
			TABPANEL_ITENS_NFDEVOLUCAO = indexItensNfD;
		}
	}

	@Override
	protected void addComponentesFixosInicio() throws SQLException {
		ListItensNfDevolucaoForm listItensNfDevolucaoForm;
		super.addComponentesFixosInicio();
		
		Container tabPanel = tabDinamica.getContainer(TABPANEL_NFDEVOLUCAO);
		UiUtil.add(tabPanel, new LabelName(Messages.NFDEVOLUCAO_SERIENOTA), getLeft(), getNextY());
		UiUtil.add(tabPanel, lbSerie, getLeft(), getNextY());
		
		UiUtil.add(tabPanel, new LabelName(Messages.NFDEVOLUCAO_NUNOTA), getLeft(), getNextY());
		UiUtil.add(tabPanel, lbNuNfDevolucao, getLeft(), getNextY());

		if (TABPANEL_ITENS_NFDEVOLUCAO != -1) {
			listItensNfDevolucaoForm = new ListItensNfDevolucaoForm(notaDevolucao);
			tabDinamica.setContainer(TABPANEL_ITENS_NFDEVOLUCAO, listItensNfDevolucaoForm);
		}
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		if (ValueUtil.isEmpty(notaDevolucao.flAprovacao) || NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(notaDevolucao.flAprovacao)) {
			Control controlObs = (Control) hashComponentes.get(NfDevolucao.NMCOLUNA_DSOBSERVACAO);
			if(controlObs != null) {
				controlObs.setEnabled(true);
			}
		}
	}

    @Override
	protected void addCabecalho() throws SQLException {
    	UiUtil.add(this, containerDadosCli, LEFT, super.getTop(), FILL, LabelContainer.getStaticHeight());
    }

    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (hashTabs.size() > 1) {
    		tabDinamica.setActiveTab(TABPANEL_NFDEVOLUCAO);
    	}
    }

    @Override
	protected int getTop() {
    	return containerDadosCli.getY2() + 1;
    }
    

	@Override
	protected void addComponentesFixosFim() throws SQLException {
		super.addComponentesFixosFim();
		if (!onPopUp && (ValueUtil.isEmpty(notaDevolucao.flAprovacao) || NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(notaDevolucao.flAprovacao))) {
			UiUtil.add(barBottomContainer, btAprovarNfDevolucao, 5);
			UiUtil.add(barBottomContainer, btReprovarNfDevolucao, 1);
		}
	}
	

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (onPopUp) {
			barTopContainer.setVisible(false);
		}
		if (!NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(notaDevolucao.flAprovacao)) {
			btAprovarNfDevolucao.setVisible(false);
			btReprovarNfDevolucao.setVisible(false);
		} 

	}
	
	@Override
	protected void afterSave() throws SQLException {
		domainToScreen(notaDevolucao);
		super.afterSave();
	}

	
	@Override
	protected void beforeSave() throws SQLException {
		super.beforeSave();
		notaDevolucao = (NfDevolucao) screenToDomain();
		notaDevolucao.dsObservacao = StringUtil.getStringValue(notaDevolucao.getHashValuesDinamicos().getString(NfDevolucao.NMCOLUNA_DSOBSERVACAO));
		notaDevolucao.hrAlteracao = TimeUtil.getCurrentTimeHHMM();
		notaDevolucao.dtAlteracao = new Date();
	}
	
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btAprovarNfDevolucao) {
				btAprovarNfDevolucaoClick();
			} else if (event.target == btReprovarNfDevolucao) {
				btReprovarNfDevolucaoClick();
			}
			break;
		}
		}
	}
	
	
	
	public void btReprovarNfDevolucaoClick() throws SQLException {
		processButtonClick(ValueUtil.VALOR_NAO);
	}

	public void btAprovarNfDevolucaoClick() throws SQLException {
		processButtonClick(ValueUtil.VALOR_SIM);		
	}

	protected void processButtonClick(String status) throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(ValueUtil.valueEquals(status, ValueUtil.VALOR_SIM) ? Messages.NFDEVOLUCAO_COMFIRM_APROVACAO : Messages.NFDEVOLUCAO_COMFIRM_REPROVACAO)) {
			return;
    	}
		notaDevolucao.flAprovacao = status;
		try {
			if (onPopUp) {
				salvarNovoClick();
			} else {
				salvarClick();
				voltarClick();
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			throw e;
		}
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		super.voltarClick();
		list();
	}
}
