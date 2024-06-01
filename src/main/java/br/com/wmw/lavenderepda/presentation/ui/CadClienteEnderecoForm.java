package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.EditInscricaoEstadual;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTimeInterval;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.EmpresaEndereco;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CampoLavendereService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ClienteEndAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.business.service.EmpresaEnderecoService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.util.CepUtil;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Hashtable;
import totalcross.util.Vector;


public class CadClienteEnderecoForm extends BaseLavendereCrudPersonCadForm {
	
	private LabelName lbNmRazaoSocial;
	private LabelValue lvNmRazaoSocial;
    private LabelValue lvCdEndereco;
    private ButtonGroupBoolean bgFlComercial;
    private ButtonGroupBoolean bgFlEntrega;
    private ButtonGroupBoolean bgFlEntregaPadrao;
    private ButtonGroupBoolean bgFlEntregaAgendada;
    private ButtonGroupBoolean bgFlCobranca;
    private ButtonGroupBoolean bgFlCobrancaPadrao;
    private LabelName lbCdEndereco;
    private LabelName lbDsDiaEntrega;
    private LabelName lbDsPeriodoEntrega;
    private LabelName lbDsPeriodoEntregaAlternativo;
    private LabelName lbDsDiaAbertura;
    private LabelName lbDsPeriodoAbertura;
    private LabelName lbFlComercial;
    private LabelName lbFlEntrega;
    private LabelName lbFlEntregaPadrao;
    private LabelName lbFlEntregaAgendada;
    private LabelName lbFlCobranca;
    private LabelName lbFlCobrancaPadrao;
    private ButtonAction btEditar;
    private ButtonAction btCancelarEdicao;
    private ImageControl icComercial;
    private Image imgComercial;
    private ImageControl icEntrega;
    private Image imgEntrega;
    private ImageControl icEntregaPadrao;
    private Image imgEntregaPadrao;
    private ImageControl icCobranca;
    private Image imgCobranca;
    private ImageControl icCobrancaPadrao;
    private Image imgCobrancaPadrao;
    private PushButtonGroupBase pbDsDiaEntrega;
    private PushButtonGroupBase pbDsDiaAbertura;
    private EditTimeInterval edDsPeriodoEntrega;
    private EditTimeInterval edDsPeriodoEntregaAlternativo;
    private EditTimeInterval edDsPeriodoAbertura;
    private PushButtonGroupBase pbDsDiasEntregaEmpresa;
    
    private ClienteEndereco clienteEnderecoOriginal;
    
    private boolean onPopup;
    
    public CadClienteEnderecoForm(boolean readOnly, boolean onPopup) {
    	super(Messages.CLIENTEENDERECO_NOME_ENTIDADE);
        this.onPopup = onPopup;
    	lbNmRazaoSocial = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
    	lvNmRazaoSocial = new LabelValue();
    	lbCdEndereco = new LabelName(Messages.CLIENTEENDERECO_LABEL_CDENDERECO);
        lvCdEndereco = new LabelValue();
        lbFlComercial = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLCOMERCIAL);
        bgFlComercial = new ButtonGroupBoolean();
        lbFlEntrega = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLENTREGA);
        bgFlEntrega = new ButtonGroupBoolean();
        bgFlEntregaPadrao = new ButtonGroupBoolean();
        lbFlEntregaPadrao = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLENTREGAPADRAO);
        lbFlCobranca = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLCOBRANCA);
        bgFlCobranca = new ButtonGroupBoolean();
        lbFlCobrancaPadrao = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLCOBRANCAPADRAO);
        bgFlCobrancaPadrao = new ButtonGroupBoolean();
        imgComercial = UiUtil.getImage("images/comercial.png");
        imgComercial.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
        icComercial = new ImageControl();
        icComercial.setImage(UiUtil.getSmoothScaledImage(imgComercial, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        imgEntrega = UiUtil.getImage("images/entrega.png");
    	imgEntrega.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	icEntrega = new ImageControl();
    	icEntrega.setImage(UiUtil.getSmoothScaledImage(imgEntrega, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
    	imgEntregaPadrao = UiUtil.getImage("images/entregaCobrancaPadrao.png");
    	imgEntregaPadrao.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	icEntregaPadrao = new ImageControl();
    	icEntregaPadrao.setImage(UiUtil.getSmoothScaledImage(imgEntregaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
    	imgCobranca = UiUtil.getImage("images/cobranca.png");
    	imgCobranca.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	icCobranca = new ImageControl();
    	icCobranca.setImage(UiUtil.getSmoothScaledImage(imgCobranca, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
    	imgCobrancaPadrao = UiUtil.getImage("images/entregaCobrancaPadrao.png");
    	imgCobrancaPadrao.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	icCobrancaPadrao = new ImageControl();
    	icCobrancaPadrao.setImage(UiUtil.getSmoothScaledImage(imgCobrancaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
    	lbDsDiaEntrega = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSDIAENTREGA);	
    	String[] diasSemana = new String[] {FrameworkMessages.DATA_SEMANA_DOMINGO_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SABADO_ABREVIADO};
        pbDsDiaEntrega = new PushButtonGroupBase(diasSemana, false, -1, -1, UiUtil.defaultGap * 2, 1, true, PushButtonGroupBase.TYPE_MULTICHOICE);
        lbDsPeriodoEntrega = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSPERIODOENTREGA);
        edDsPeriodoEntrega = new EditTimeInterval();
        lbDsPeriodoEntregaAlternativo = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSPERIODOENTREGAALTERNATIVO);
        edDsPeriodoEntregaAlternativo = new EditTimeInterval();
        lbDsDiaAbertura = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSDIAABERTURA);
        pbDsDiaAbertura = new PushButtonGroupBase(diasSemana, false, -1, -1, UiUtil.defaultGap * 2, 1, true, PushButtonGroupBase.TYPE_MULTICHOICE);
        lbDsPeriodoAbertura = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSPERIODOABERTURA);
        edDsPeriodoAbertura = new EditTimeInterval();
        lbFlEntregaAgendada = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLENTREGAAGENDADA);
        bgFlEntregaAgendada = new ButtonGroupBoolean();
        btEditar = new ButtonAction(Messages.LABEL_BOTAO_EDITAR, "images/editar.png");
        btCancelarEdicao = new ButtonAction(FrameworkMessages.BOTAO_CANCELAR, "images/cancel.png");
        if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
        	btCancelarEdicao.setVisible(false);
        	btExcluir.setVisible(true);
        	clienteEnderecoOriginal = new ClienteEndereco();
        }
        pbDsDiasEntregaEmpresa = new PushButtonGroupBase(diasSemana, false, -1, -1, UiUtil.defaultGap * 2, 1, true, PushButtonGroupBase.TYPE_MULTICHOICE);
        pbDsDiasEntregaEmpresa.setEnabled(false);
        scrollable = true;
        barBottomContainer.setVisible(!onPopup);
        barTopContainer.setVisible(!onPopup);
        if (readOnly) {
        	setReadOnly();
        }
    }
    
    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return Messages.CLIENTEENDERECO_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ClienteEnderecoService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() throws SQLException {
    	if (!isEditing()) {
    		ClienteEndereco clienteEndereco = new ClienteEndereco();
    		clienteEndereco.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		clienteEndereco.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		clienteEndereco.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    		return clienteEndereco;
    	}
        return new ClienteEndereco();
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
        super.screenToDomain();
    	ClienteEndereco clienteEndereco = (ClienteEndereco) getDomain();
        clienteEndereco.cdEndereco = lvCdEndereco.getValue();
        clienteEndereco.dsDiaEntrega = pbDsDiaEntrega.getValuesFormatted();
        clienteEndereco.dsPeriodoEntrega = edDsPeriodoEntrega.getHoraInicialFinalFormatted();
        clienteEndereco.dsPeriodoEntregaAlternativo = edDsPeriodoEntregaAlternativo.getHoraInicialFinalFormatted();
        clienteEndereco.dsDiaAbertura = pbDsDiaAbertura.getValuesFormatted();
        clienteEndereco.dsPeriodoAbertura = edDsPeriodoAbertura.getHoraInicialFinalFormatted();
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOMERCIAL)) {
    		clienteEndereco.flComercial = StringUtil.getStringValue(bgFlComercial.getValueBoolean());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGA)) {
    		clienteEndereco.flEntrega = StringUtil.getStringValue(bgFlEntrega.getValueBoolean());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGAPADRAO)) {
    		clienteEndereco.flEntregaPadrao = StringUtil.getStringValue(bgFlEntregaPadrao.getValueBoolean());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGAAGENDADA)) {
    		clienteEndereco.flEntregaAgendada = StringUtil.getStringValue(bgFlEntregaAgendada.getValueBoolean());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOBRANCA)) {
    		clienteEndereco.flCobranca = StringUtil.getStringValue(bgFlCobranca.getValueBoolean());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOBRANCAPADRAO)) {
    		clienteEndereco.flCobrancaPadrao = StringUtil.getStringValue(bgFlCobrancaPadrao.getValueBoolean());
    	}
		if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_NUCNPJ)) {
			clienteEndereco.nuCnpj = clienteEndereco.getHashValuesDinamicos().getString(ClienteEndereco.NMCOLUNA_NUCNPJ.toUpperCase());
		}
        return clienteEndereco;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
        ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
        lvCdEndereco.setValue(clienteEndereco.cdEndereco);
        pbDsDiaEntrega.setValuesFormatted(clienteEndereco.dsDiaEntrega);
        edDsPeriodoEntrega.setHoraInicialFinalFormatted(clienteEndereco.dsPeriodoEntrega);
        edDsPeriodoEntregaAlternativo.setHoraInicialFinalFormatted(clienteEndereco.dsPeriodoEntregaAlternativo);
        pbDsDiaAbertura.setValuesFormatted(clienteEndereco.dsDiaAbertura);
        edDsPeriodoAbertura.setHoraInicialFinalFormatted(clienteEndereco.dsPeriodoAbertura);
        bgFlComercial.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flComercial));
        bgFlEntrega.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flEntrega));
        bgFlEntregaPadrao.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flEntregaPadrao));
        bgFlEntregaAgendada.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flEntregaAgendada));
        bgFlCobranca.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flCobranca));
        bgFlCobrancaPadrao.setValueBoolean(ValueUtil.getBooleanValue(clienteEndereco.flCobrancaPadrao));
        if (clienteEndereco.isComercial()) {
        	icComercial.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icComercial.setImage(UiUtil.getSmoothScaledImage(imgComercial, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (clienteEndereco.isEntrega()) {
        	icEntrega.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icEntrega.setImage(UiUtil.getSmoothScaledImage(imgEntrega, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (clienteEndereco.isEntregaPadrao()) {
        	icEntregaPadrao.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icEntregaPadrao.setImage(UiUtil.getSmoothScaledImage(imgEntregaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (clienteEndereco.isCobranca()) {
        	icCobranca.getImage().applyColor2(Color.darker(Color.ORANGE, 80));
        } else {
        	icCobranca.setImage(UiUtil.getSmoothScaledImage(imgCobranca, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (clienteEndereco.isCobrancaPadrao()) {
        	icCobrancaPadrao.getImage().applyColor2(Color.darker(Color.ORANGE, 80));
        } else {
        	icCobrancaPadrao.setImage(UiUtil.getSmoothScaledImage(imgCobrancaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
    }
    
    @Override
    protected void clearScreen() throws java.sql.SQLException {
    	super.clearScreen();
        pbDsDiaEntrega.setValuesFormatted(getVlPadraoCampoDinamico(ClienteEndereco.NMCOLUNA_DSDIAENTREGA));
        edDsPeriodoEntrega.setHoraInicialFinalFormatted("");
        edDsPeriodoEntregaAlternativo.setHoraInicialFinalFormatted("");
        pbDsDiaAbertura.setValuesFormatted(getVlPadraoCampoDinamico(ClienteEndereco.NMCOLUNA_DSDIAABERTURA));
        edDsPeriodoAbertura.setHoraInicialFinalFormatted("");
        bgFlComercial.setValueBoolean(false);
        bgFlEntrega.setValueBoolean(false);
        bgFlEntregaPadrao.setValueBoolean(false);
        bgFlEntregaAgendada.setValueBoolean(false);
        bgFlCobranca.setValueBoolean(false);
        bgFlCobrancaPadrao.setValueBoolean(false);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
        pbDsDiaEntrega.setEnabled(enabled);
        edDsPeriodoEntrega.setEnabled(enabled);
        edDsPeriodoEntregaAlternativo.setEnabled(enabled);
        pbDsDiaAbertura.setEnabled(enabled);
        edDsPeriodoAbertura.setEnabled(enabled);
        bgFlComercial.setEnabled(enabled);
        bgFlEntrega.setEnabled(enabled);
        bgFlEntregaPadrao.setEnabled(enabled);
        bgFlEntregaAgendada.setEnabled(enabled);
        bgFlCobranca.setEnabled(enabled);
        bgFlCobrancaPadrao.setEnabled(enabled);
    }
    
    //-----------------------------------------------
    private boolean isCampoDisponivelEdicao(String nmColuna) {
    	Campo campoExample = new Campo();
    	campoExample.cdSistema = Campo.CD_SISTEMA_PADRAO;
    	campoExample.nmEntidade = CampoLavendereService.getCampoLavendereInstance().getNmEntidadeFromPdaTableName(ClienteEndAtua.TABLE_NAME);
    	campoExample.nmCampo = nmColuna.toUpperCase(); 
    	Vector personFields = BasePersonDomain.getConfigPersonCadList(ClienteEndAtua.TABLE_NAME);
    	int index = personFields.indexOf(campoExample);
    	if (index != -1) {
    		Campo campo = (Campo)personFields.items[index];
      		return campo.isVisivelCad() && campo.isEditavel();
    	}
    	return false;
    }

    
    private String getVlPadraoCampoDinamico(String nmColuna) {
    	Campo campoExample = new Campo();
    	campoExample.cdSistema = Campo.CD_SISTEMA_PADRAO;
   		campoExample.nmEntidade = CampoLavendereService.getCampoLavendereInstance().getNmEntidadeFromPdaTableName(ClienteEndAtua.TABLE_NAME);
    	campoExample.nmCampo = nmColuna.toUpperCase();
    	Vector personFields = BasePersonDomain.getConfigPersonCadList(ClienteEndAtua.TABLE_NAME);
    	int index = personFields.indexOf(campoExample);
    	if (index != -1) {
    		Campo campo = (Campo)personFields.items[index];
    		return campo.vlPadrao;
    	}
    	return "";
    }
    
    @Override
    protected void onFormStart() throws SQLException {
       	Container cPrincipal = getContainerPrincipal();
    	if (scrollable) {
    		cPrincipal.removeAll();
    		scBase.removeAll();
    	}
    	super.onFormStart();
    }
    
    @Override
    protected void refreshComponents() throws SQLException {
    	super.refreshComponents();
    	reposition();
    }
    
    @Override
    protected void addComponentesFixosInicio() throws SQLException {
    	super.addComponentesFixosInicio();
    	
    	Container cPrincipal = getContainerPrincipal();
	    
    	boolean exibindoEndereco = !LavenderePdaConfig.isPermiteGerenciarEnderecosCliente() || btEditar.isVisible() && isEditing();
    	if (isEditing()) {
    		UiUtil.add(cPrincipal, lbCdEndereco, lvCdEndereco, getLeft(), getTop(), FILL);
    	} else {
			lvNmRazaoSocial.setValue(SessionLavenderePda.getCliente().toString());
			UiUtil.add(cPrincipal, lbNmRazaoSocial, lvNmRazaoSocial, getLeft(), getTop(), FILL);
    	}
    	if (!exibindoEndereco) {
    		if (isEditing()) {
    			scBase.scrollToControl(lbCdEndereco);
    		} else {
    			scBase.scrollToControl(lbNmRazaoSocial);
    		}
    	}
    }
    
    @Override
    protected void addComponentesFixosFim() throws SQLException {
    	super.addComponentesFixosFim();
    	Container cPrincipal = getContainerPrincipal();
    	if (isApresentaDiasEntregaEmpresaCadastroEndereco()) {
    		UiUtil.add(cPrincipal, new LabelName(Messages.EMPRESAENDERECO_DSDIASENTREGA), pbDsDiasEntregaEmpresa, SAME, AFTER, getWFill(), UiUtil.getControlPreferredHeight());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSDIAENTREGA)) {
    		UiUtil.add(cPrincipal, lbDsDiaEntrega, pbDsDiaEntrega, SAME, AFTER, getWFill(), UiUtil.getControlPreferredHeight());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSPERIODOENTREGA)) {
    		UiUtil.add(cPrincipal, lbDsPeriodoEntrega, edDsPeriodoEntrega, SAME, AFTER);
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSPERIODOENTREGAALTERNATIVO)) {
    		UiUtil.add(cPrincipal, lbDsPeriodoEntregaAlternativo, edDsPeriodoEntregaAlternativo, SAME, AFTER);
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSDIAABERTURA)) {
    		UiUtil.add(cPrincipal, lbDsDiaAbertura, pbDsDiaAbertura, SAME, AFTER, getWFill(), UiUtil.getControlPreferredHeight());
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSPERIODOABERTURA)) {
    		UiUtil.add(cPrincipal, lbDsPeriodoAbertura, edDsPeriodoAbertura, SAME, AFTER);
    	}
    	int y = AFTER;
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOMERCIAL)) {
    		UiUtil.add(cPrincipal, lbFlComercial, bgFlComercial, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		UiUtil.add(cPrincipal, icComercial, (WIDTH_GAP_BIG * WIDTH_GAP_BIG + WIDTH_GAP_BIG * 2) + bgFlComercial.getX() * 2, bgFlComercial.getY());
    		y = bgFlComercial.getY2();
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGA)) {
    		UiUtil.add(cPrincipal, lbFlEntrega, bgFlEntrega, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		UiUtil.add(cPrincipal, icEntrega, (WIDTH_GAP_BIG * WIDTH_GAP_BIG + WIDTH_GAP_BIG * 2) + bgFlEntrega.getX() * 2, bgFlEntrega.getY());
    		y = bgFlEntrega.getY2();
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGAPADRAO)) {
    		UiUtil.add(cPrincipal, lbFlEntregaPadrao, bgFlEntregaPadrao, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		UiUtil.add(cPrincipal, icEntregaPadrao, (WIDTH_GAP_BIG * WIDTH_GAP_BIG + WIDTH_GAP_BIG * 2) + bgFlEntregaPadrao.getX() * 2, bgFlEntregaPadrao.getY());
    		y = bgFlEntregaPadrao.getY2();
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGAAGENDADA)) {
    		UiUtil.add(cPrincipal, lbFlEntregaAgendada, bgFlEntregaAgendada, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		y = bgFlEntregaAgendada.getY2();
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOBRANCA)) {
    		UiUtil.add(cPrincipal, lbFlCobranca, bgFlCobranca, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		UiUtil.add(cPrincipal, icCobranca, (WIDTH_GAP_BIG * WIDTH_GAP_BIG + WIDTH_GAP_BIG * 2) + bgFlCobranca.getX() * 2, bgFlCobranca.getY());
    		y = bgFlCobranca.getY2();
    	}
    	if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLCOBRANCAPADRAO)) {
    		UiUtil.add(cPrincipal, lbFlCobrancaPadrao, bgFlCobrancaPadrao, getLeft(), y, getWFill(), UiUtil.getControlPreferredHeight());
    		UiUtil.add(cPrincipal, icCobrancaPadrao, (WIDTH_GAP_BIG * WIDTH_GAP_BIG + WIDTH_GAP_BIG * 2) + bgFlCobrancaPadrao.getX() * 2, bgFlCobrancaPadrao.getY());
    	}
    }
    
    private boolean isPossuiComponenteFormatoInscricaoEstadual() throws SQLException {
		return getComponenteFormatoInscricaoEstadual() != null;
	}
    
    private Campo getComponenteFormatoInscricaoEstadual() throws SQLException {
    	int size = getConfigPersonCadList().size();
    	for (int i = 0; i < size; i++) {
			Campo configCad = (Campo) getConfigPersonCadList().items[i];
			if (Campo.FORMATO_INSCRICAOESTADUAL.equals(configCad.dsFormato)) {
				return configCad;
			}
		}
    	return null;
    	
    }

	@Override
    protected void addBarButtons() {
    	super.addBarButtons();
    	if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
    		UiUtil.add(barBottomContainer, btEditar, 5);
    		UiUtil.add(barBottomContainer, btCancelarEdicao, 5);
    	}
    }
    
    @Override
    protected void voltarClick() throws SQLException {
    	btVoltar.requestFocus();
    	if (isEditing()) {
    		if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente() && !btEditar.isVisible()) {
				if (UiUtil.showConfirmYesNoMessage(Messages.MSG_SAIR_CANCELAR_SEM_SALVAR)) {
					btCancelarEdicaoClick();
					super.voltarClick();
				}
    		} else {
    			super.voltarClick();
    		}
    	} else {
    		if (UiUtil.showConfirmYesNoMessage(Messages.MSG_SAIR_CANCELAR_SEM_SALVAR)) {
    			btCancelarEdicaoClick();
    			super.voltarClick();
    		}
    	}
    }
    
    @Override
    public void reposition() {
    	super.reposition();
    	if (tabDinamica != null && hashTabs != null && hashTabs.size() > 1) {    		
    		tabDinamica.reposition();
    	} else {    		
    		try {
    			remontaTela();
    		} catch (SQLException e) {
    			ExceptionUtil.handle(e);
    		}
    	}
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		try {
			switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btEditar) {
					btEditarClick();
				} else if (event.target == btCancelarEdicao && UiUtil.showConfirmYesNoMessage(Messages.MSG_SAIR_CANCELAR_SEM_SALVAR)) {
					btCancelarEdicaoClick();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				CampoDinamicoComboBox cbDsCidade = null;
				EditText edDsCidade = null;
				EditText edDsBairro = (EditText) hashComponentes.get(ClienteEndereco.NMCOLUNA_DSBAIRRO.toUpperCase());
				if (hashComponentes.get(ClienteEndereco.NMCOLUNA_DSCIDADE.toUpperCase()) instanceof CampoDinamicoComboBox) {
					cbDsCidade = (CampoDinamicoComboBox) hashComponentes.get(ClienteEndereco.NMCOLUNA_DSCIDADE.toUpperCase());
				} else {
					edDsCidade = (EditText) hashComponentes.get(ClienteEndereco.NMCOLUNA_DSCIDADE.toUpperCase());
				}

				if (isApresentaDiasEntregaEmpresaCadastroEndereco() && (event.target == edDsBairro || event.target == edDsCidade || event.target == cbDsCidade)) {
					String dsCidade = edDsCidade != null ? edDsCidade.getValue() : "";
					dsCidade = cbDsCidade != null ? cbDsCidade.getSelectedItem().toString() : dsCidade;
					EmpresaEndereco empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(((ClienteEndereco) getDomain()).cdEmpresa, edDsBairro.getValue().trim(), dsCidade.trim());
					if (empresaEndereco != null) {
						pbDsDiasEntregaEmpresa.setValuesFormatted(empresaEndereco.dsDiasEntrega);
					}
				}
				EditText edDsCep = (EditText) hashComponentes.get(ClienteEndereco.NMCOLUNA_DSCEP.toUpperCase());
				if (event.target == edDsCep && ValueUtil.isNotEmpty(edDsCep.getValue())) {
					consultaCEP(edDsCep.getValue());
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if (event.target == bgFlEntregaPadrao.bgBoolean || event.target == bgFlEntrega.bgBoolean && bgFlEntregaPadrao.getValueBoolean()) {
					bgFlEntrega.setValueBoolean(true);
				} else if (event.target == bgFlCobrancaPadrao.bgBoolean || event.target == bgFlCobranca.bgBoolean && bgFlCobrancaPadrao.getValueBoolean()) {
					bgFlCobranca.setValueBoolean(true);
				} 
				break;
			}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	

	private void btEditarClick() throws SQLException {
		setEnabled(true);
		btCancelarEdicao.setVisible(true);
		btEditar.setVisible(false);
		btSalvar.setVisible(true);
		btExcluir.setVisible(false);
		visibleStateIcones(false);
		onFormStart();
		loadClienteEnderecoOriginal();
		super.domainToScreen(getDomain());
	}
	
	private void btCancelarEdicaoClick() throws SQLException {
		domainToScreen(getDomain());
		setReadOnly();
		btCancelarEdicao.setVisible(false);
		btEditar.setVisible(true);
		btExcluir.setVisible(true);
		visibleStateIcones(true);
		carregaDiasEntrega();
		onFormStart();
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		super.beforeSave();
		ClienteEndereco clienteEndereco = (ClienteEndereco) screenToDomain();
		if (LavenderePdaConfig.isObrigaCnpjParaEnderecosEntregaCliente() && ValueUtil.isEmpty(clienteEndereco.nuCnpj) && bgFlEntrega.getValueBoolean()) {
			throw new ValidationException(Messages.CLIENTEENDERECO_ENDENTREGA_CNPJVAZIO);
		}
	}
	
	
	@Override
	protected void save() throws SQLException {
		if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
			super.screenToDomain();
			saveClienteEndAtua();
		} else {
			super.save();
		}
	}
	
	private void saveClienteEndAtua() throws SQLException {
		ClienteEndereco clienteEndereco = (ClienteEndereco) screenToDomain();
		if (clienteEndereco != null) {
			ClienteEndAtua clienteEndAtua = new ClienteEndAtua();
			clienteEndAtua.cdEmpresa = clienteEndereco.cdEmpresa;
			clienteEndAtua.cdRepresentante = clienteEndereco.cdRepresentante;
			clienteEndAtua.cdCliente = clienteEndereco.cdCliente;
			if (isEditing()) {
				clienteEndAtua.cdEndereco = clienteEndereco.cdEndereco;
				clienteEndAtua.flTipoRegistro = ClienteEndAtua.FLTIPOREGISTRO_ALTERACAO;
			} else {
				clienteEndAtua.cdEndereco = ClienteEndAtua.CDENDERECO_NOVO_CADASTRO;
				clienteEndAtua.flTipoRegistro = ClienteEndAtua.FLTIPOREGISTRO_NOVO;
			}
			clienteEndAtua.cdRegistro =  getCrudService().generateIdGlobal();
			clienteEndAtua.flOrigemAtualizacao = ClienteEndAtua.FLORIGEM_PDA;
			clienteEndAtua.dtAtualizacao = DateUtil.getCurrentDate();
			clienteEndAtua.cdUsuarioAlteracao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			ClienteEndAtuaService.getInstance().populaCamposDinamicosByClienteEndereco(clienteEndAtua, clienteEndereco);
			if (isEditing()) {
				ClienteEndAtuaService.getInstance().validateValuesDynamicsChange(clienteEndAtua, clienteEnderecoOriginal);
			}
			boolean nuCnpjEditavel = isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_NUCNPJ);
			boolean flEntregaEditavel = isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_FLENTREGA);
			if (LavenderePdaConfig.isObrigaCnpjParaEnderecosEntregaCliente() && clienteEndereco.isJuridica()) {				
				ClienteEndAtuaService.getInstance().validaCnpj(flEntregaEditavel, nuCnpjEditavel, isCampoObrigatorio(ClienteEndereco.NMCOLUNA_NUCNPJ), clienteEndereco);
			} else {
				ClienteEndAtuaService.getInstance().validaCpf(nuCnpjEditavel, flEntregaEditavel, clienteEndereco);
			}
			ClienteEndAtuaService.getInstance().insert(clienteEndAtua);
			btCancelarEdicaoClick();
			list();
			if (isEditing() && LavenderePdaConfig.isAplicaAlteracoesCadastroClienteEnderecoAutomaticamente()) {
				ClienteEnderecoService.getInstance().aplicaAlteracoesEndereco(clienteEnderecoOriginal, clienteEndereco);
				list();
			}
		}
	}
	
	@Override
	protected void excluirClick() throws SQLException {
		if (UiUtil.showConfirmDeleteMessage(getEntityDescription())) {
			ClienteEndereco clienteEndereco = (ClienteEndereco) getDomain();
			ClienteEndAtua clienteEndAtua = new ClienteEndAtua();
			clienteEndAtua.cdEmpresa = clienteEndereco.cdEmpresa;
			clienteEndAtua.cdRepresentante = clienteEndereco.cdRepresentante;
			clienteEndAtua.cdCliente = clienteEndereco.cdCliente;
			clienteEndAtua.cdEndereco = clienteEndereco.cdEndereco;
			clienteEndAtua.cdRegistro =  getCrudService().generateIdGlobal();
			clienteEndAtua.cdPeriodoEntrega = clienteEndereco.cdPeriodoEntrega;
			clienteEndAtua.flOrigemAtualizacao = ClienteEndAtua.FLORIGEM_PDA;
			clienteEndAtua.dtAtualizacao = DateUtil.getCurrentDate();
			clienteEndAtua.flTipoRegistro = ClienteEndAtua.FLTIPOREGISTRO_EXCLUSAO;
			clienteEndAtua.cdUsuarioAlteracao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			ClienteEndAtuaService.getInstance().populaCamposDinamicosByClienteEndereco(clienteEndAtua, clienteEndereco);
			ClienteEndAtuaService.getInstance().insert(clienteEndAtua);
			UiUtil.showInfoMessage(Messages.MSG_DADOS_INSERIDOS_ALTERADOS);
			close();
			list();
		}
	}
	
	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		btExcluir.setVisible(LavenderePdaConfig.isPermiteGerenciarEnderecosCliente() && isEditing());
		btEditar.setVisible(LavenderePdaConfig.isPermiteGerenciarEnderecosCliente() && isEditing());
		barBottomContainer.setVisible(!onPopup);
    	barTopContainer.setVisible(!onPopup);
	}
	
	@Override
	public void add() throws SQLException {
		super.add();
		setEnabled(true);
		visibleStateIcones(false);
		btSalvar.setVisible(true);
		onFormStart();
	}
	
	@Override
	protected void afterEdit() throws SQLException {
		super.afterEdit();
		onFormStart();
	}
	
	private void carregaDiasEntrega() throws SQLException {
		if (isApresentaDiasEntregaEmpresaCadastroEndereco()) {
			ClienteEndereco clienteEndereco = (ClienteEndereco) getDomain();
			EmpresaEndereco empresaEndereco;
			if (isEditing()) {
				empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(clienteEndereco.cdEmpresa, clienteEndereco.dsBairro, clienteEndereco.dsCidade);
			} else {
				empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(clienteEndereco.cdEmpresa);
			}
			if (empresaEndereco != null) {
				pbDsDiasEntregaEmpresa.setValuesFormatted(empresaEndereco.dsDiasEntrega);
			}
		}
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		carregaDiasEntrega();
	}
	
	private void visibleStateIcones(boolean visible) {
		icComercial.setVisible(visible);
		icEntrega.setVisible(visible);
		icEntregaPadrao.setVisible(visible);
		icCobranca.setVisible(visible);
		icCobrancaPadrao.setVisible(visible);
	}
	
	private void loadClienteEnderecoOriginal() throws SQLException {
		ClienteEndereco clienteEndereco = (ClienteEndereco) screenToDomain();
		clienteEnderecoOriginal = (ClienteEndereco) clienteEndereco.clone();
	}
	
	public boolean isApresentaDiasEntregaEmpresaCadastroEndereco() {
		return LavenderePdaConfig.apresentaDiasEntregaEmpresaCadastroEndereco && isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSDIAENTREGA);
	}

	@Override
	protected String getDsTable() throws SQLException {
		return ClienteEndereco.TABLE_NAME;
	}

	private void consultaCEP(String value) throws Exception {
		Cep cep = CepUtil.cepOffLine(value);
		boolean cepOffLine = true;
		if (cep == null) {
			cep = new Cep(value);
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}
		setaValoresNosCampos(cep, cepOffLine);		
	}

	private void setaValoresNosCampos(Cep cep, boolean cepOffline) throws SQLException {
		if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSLOGRADOURO)) {
			String dsLogradouro = cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.getDsLogradouro();
			if (ValueUtil.isNotEmpty(dsLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)	{
				Control edLogradouro = (Control) getHashComponentes().get(ClienteEndereco.NMCOLUNA_DSLOGRADOURO.toUpperCase());
				if (edLogradouro instanceof EditText) {
					((EditText) edLogradouro).setValue(dsLogradouro);
				}
			}
		}
		if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSBAIRRO)) {
			String dsBairro = cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.getDsBairro();
			if (ValueUtil.isNotEmpty(dsBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				Control edDsBairro = (Control) getHashComponentes().get(ClienteEndereco.NMCOLUNA_DSBAIRRO.toUpperCase());
				if (edDsBairro instanceof EditText) {
					((EditText) edDsBairro).setValue(dsBairro);
				}
			}
		}
		if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSCIDADE)) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.getDsCidade();
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				Control edDsCidade = (Control) getHashComponentes().get(ClienteEndereco.NMCOLUNA_DSCIDADE.toUpperCase());
				if (edDsCidade instanceof EditText) {
					((EditText) edDsCidade).setValue(dsCidade);
				}
				if (edDsCidade instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edDsCidade).setSelectedItemStartingWith(dsCidade, true);
				}
			}
		}
		if (isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSESTADO)) {
			String cepValue = cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.getDsUf();
			if (ValueUtil.isNotEmpty(cepValue) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				Control edDsEstado = (Control) getHashComponentes().get(ClienteEndereco.NMCOLUNA_DSESTADO.toUpperCase());
				if (isPossuiComponenteFormatoInscricaoEstadual()) {
					Campo campo = getComponenteFormatoInscricaoEstadual();
					if (campo != null) {
						EditInscricaoEstadual componente = (EditInscricaoEstadual) hashComponentes.get(campo.nmCampo);
						componente.setUf(cepValue);
					}
				} 
				if (edDsEstado instanceof EditText) {
					((EditText) edDsEstado).setValue(cepValue);
				} else if (edDsEstado instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edDsEstado).setValue(cepValue);
				}
			}
		}
	}

    private boolean isCampoObrigatorio(String nmColuna) {
    	Campo campoExample = new Campo();
    	campoExample.cdSistema = Campo.CD_SISTEMA_PADRAO;
    	campoExample.nmEntidade = CampoLavendereService.getCampoLavendereInstance().getNmEntidadeFromPdaTableName(ClienteEndAtua.TABLE_NAME);
    	campoExample.nmCampo = nmColuna.toUpperCase();
    	Vector personFields = BasePersonDomain.getConfigPersonCadList(ClienteEndAtua.TABLE_NAME);
    	int index = personFields.indexOf(campoExample);
    	if (index != -1) {
    		Campo campo = (Campo)personFields.items[index];
    		return campo.isObrigatorio();
    	}
    	return false;
    }
    
    @Override
    protected int getTop() {
    	if (onPopup) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }
	

    protected Hashtable getHashComponentes() {
        return hashComponentes;
    }

}
