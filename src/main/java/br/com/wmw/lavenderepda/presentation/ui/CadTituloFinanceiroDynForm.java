package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import br.com.wmw.lavenderepda.business.domain.RedeClienteTitulo;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.RedeClienteTituloService;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sys.Vm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadTituloFinanceiroDynForm extends BaseLavendereCrudPersonCadForm {
	private static final String SYSTEM_VIEWER = "viewer";
	
	private int TABPANEL_TITULO;
	private int TABPANEL_ITENS_NF;
	private LabelContainer containerDadosCli;
	private ListItensNfForm listItensNfForm;
	protected RedeCliente redeCliente;
	private ButtonOptions bmOpcoes;
	public TituloFinanceiro tituloFinanceiro;

    public CadTituloFinanceiroDynForm() {
    	this(null);
    }

    public CadTituloFinanceiroDynForm(RedeClienteTitulo redeClienteFilter) {
        super(Messages.TITULOFINANCEIRO_NOME_ENTIDADE);
        this.redeCliente = (redeClienteFilter != null) ? redeClienteFilter.redeCliente : null;
        containerDadosCli = new LabelContainer((redeCliente != null) ? redeCliente.toString() : SessionLavenderePda.getCliente().toString());
		bmOpcoes = new ButtonOptions();
		if (LavenderePdaConfig.isUsaImpressaoNfePDF()) {
			bmOpcoes.addItem(Messages.BOTAO_EXPORTAR_NFE_PEDIDO_PDF);
		}

        setReadOnly();
	}
    
	protected String getDsTable() throws SQLException {
		return (redeCliente != null) ? RedeClienteTitulo.TABLE_NAME : TituloFinanceiro.TABLE_NAME;
	}

    //@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return (redeCliente != null) ? RedeClienteTituloService.getInstance() : TituloFinanceiroService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return (redeCliente != null) ? new RedeClienteTitulo() : new TituloFinanceiro();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        return getDomain();
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        super.domainToScreen(domain);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
    	super.clearScreen();
    }

    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    }

    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	int indexFinanceiro = tableTitles.indexOf(Messages.TITULOFINANCEIRO_NOME_ENTIDADE);
    	TABPANEL_TITULO = indexFinanceiro == -1 ? 0 : indexFinanceiro;
    	if (LavenderePdaConfig.exibeItensConfigTituloFinanceiro()) {
    		int indexItensNf = tableTitles.indexOf(Messages.TITULOFINANCEIRO_TAB_ITENS_NF);
    		if (indexItensNf == -1) {
    			tableTitles.addElement(Messages.TITULOFINANCEIRO_TAB_ITENS_NF);
    			TABPANEL_ITENS_NF = tableTitles.size() - 1;
    		} else {
    			TABPANEL_ITENS_NF = indexItensNf;
    }
    	}
    }
    
    @Override
    protected void addComponentesFixosInicio() throws SQLException {
    	super.addComponentesFixosInicio();
    	if (LavenderePdaConfig.exibeItensConfigTituloFinanceiro()) {
    		if (TABPANEL_ITENS_NF != -1) {
    			listItensNfForm = new ListItensNfForm((TituloFinanceiro) getDomain());
    			tabDinamica.setContainer(TABPANEL_ITENS_NF, listItensNfForm);
    		}
    	}
    }

    protected void addCabecalho() throws SQLException {
    	UiUtil.add(this, containerDadosCli, LEFT, super.getTop(), FILL, LabelContainer.getStaticHeight());
    }


    //@Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (hashTabs.size() > 1) {
    		tabDinamica.setActiveTab(TABPANEL_TITULO);
    	}
    }

    protected int getTop() {
    	return containerDadosCli.getY2() + 1;
    }
    
    @Override
    protected void afterEdit() throws SQLException {
    	if (LavenderePdaConfig.exibeItensConfigTituloFinanceiro()) {
    		listItensNfForm.tituloFinanceiro = (TituloFinanceiro) getDomain();
    		listItensNfForm.list();
    	}
    }

	@Override
	protected void addComponentesFixosFim() throws SQLException {
		super.addComponentesFixosFim();
		UiUtil.add(barBottomContainer, bmOpcoes, 3);

	}
	
	private void btImprimirNfePDFClick()  {
		String pdf;
		try {
			tituloFinanceiro = (TituloFinanceiro) getCrudService().findByRowKey(getDomain().rowKey);
			boolean isNfeRelacionada = NfeService.getInstance().isNfeRelacionadaAoTituloFinanceiro(Integer.parseInt(tituloFinanceiro.nuNf));
			if (!isNfeRelacionada) {
				UiUtil.showInfoMessage(Messages.MSG_ERRO_GERAR_PDF_NFE_NAO_RELACIONADA);
				return;
			}
			pdf = SyncManager.geraPdfTituloNfe(tituloFinanceiro);
			pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
				abrePdfOnline(pdf);
			} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_GERADO_SUCESSO)) {
				abrePdfOnline(pdf);
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} catch (Exception e) {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_ERRO_GERAR_PDF_NFE, StringUtil.getStringValue(e.getMessage())));
		}
	}
	
	private void btImprimirBoletoPDFClick()  {
		String pdf;
		try {
			tituloFinanceiro = (TituloFinanceiro) getCrudService().findByRowKey(getDomain().rowKey);
			pdf = SyncManager.geraPdfTituloBoleto(tituloFinanceiro);
			pdf = pdf.replaceAll("\\\\", "/").replaceAll("//", "/");
			if (LavenderePdaConfig.isAbrePdfGeradoAuto()) {
				abrePdfOnline(pdf);
			} else if (UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_GERADO_SUCESSO)) {
				abrePdfOnline(pdf);
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} catch (Exception e) {
			UiUtil.showInfoMessage(Messages.MSG_ERRO_GERAR_PDF_BOLETO, StringUtil.clearEnterException(e.getMessage()));
		}
	}
	
	public void abrePdfOnline(String pdf) {
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			int executed = Vm.exec(SYSTEM_VIEWER, pdf, 0, true);
			if (executed == -1) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_CAMINHO_PDF_PEDIDO, pdf));
			}
		} else if (VmUtil.isSimulador()) {
			Vm.exec(SYSTEM_VIEWER, "file:///" + pdf, 0, true);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
		case ButtonOptionsEvent.OPTION_PRESS: {
			if (event.target == bmOpcoes) {
				if (bmOpcoes.selectedItem.equals(Messages.BOTAO_EXPORTAR_NFE_PEDIDO_PDF)) {
					btImprimirNfePDFClick();
				}
				if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_BOLETO_PDF)) {
					btImprimirBoletoPDFClick();
				}
			}
		}
		}
	}
	
}
