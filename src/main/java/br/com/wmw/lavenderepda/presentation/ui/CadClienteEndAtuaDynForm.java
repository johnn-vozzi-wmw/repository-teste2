package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditTimeInterval;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.service.CampoLavendereService;
import br.com.wmw.lavenderepda.business.service.ClienteEndAtuaService;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class CadClienteEndAtuaDynForm extends BaseLavendereCrudPersonCadForm {
	
	private LabelValue lvDtAtualizacao;
	private LabelValue lvTipoAtualizacao;
	private ButtonGroupBoolean bgFlComercial;
    private ButtonGroupBoolean bgFlEntrega;
    private ButtonGroupBoolean bgFlEntregaPadrao;
    private ButtonGroupBoolean bgFlEntregaAgendada;
    private ButtonGroupBoolean bgFlCobranca;
    private ButtonGroupBoolean bgFlCobrancaPadrao;
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

	public CadClienteEndAtuaDynForm() {
		super(Messages.CLIENTEENDATUA_NOME_ENTIDADE);
		lvDtAtualizacao = new LabelValue();
		lvTipoAtualizacao = new LabelValue();
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
        pbDsDiaEntrega.setBackForeColors(ColorUtil.componentsBackColorDark, ColorUtil.labelNameForeColor);
        pbDsDiaAbertura.setBackForeColors(ColorUtil.componentsBackColorDark, ColorUtil.labelNameForeColor);
        lbDsPeriodoAbertura = new LabelName(Messages.CLIENTEENDERECO_LABEL_DSPERIODOABERTURA);
        edDsPeriodoAbertura = new EditTimeInterval();
        lbFlEntregaAgendada = new LabelName(Messages.CLIENTEENDERECO_LABEL_FLENTREGAAGENDADA);
        bgFlEntregaAgendada = new ButtonGroupBoolean();
        pbDsDiasEntregaEmpresa = new PushButtonGroupBase(diasSemana, false, -1, -1, UiUtil.defaultGap * 2, 1, true, PushButtonGroupBase.TYPE_MULTICHOICE);
        pbDsDiasEntregaEmpresa.setBackForeColors(ColorUtil.componentsBackColorDark, ColorUtil.labelNameForeColor);
        pbDsDiasEntregaEmpresa.setEnabled(false);
		setReadOnly();
	}
	
	@Override
	protected void addComponentesFixosInicio() throws SQLException {
		super.addComponentesFixosInicio();
		UiUtil.add(getContainerPrincipal(), new LabelName(Messages.CLIENTEENDATUA_LABEL_DTATUALIZACAO), lvDtAtualizacao, getLeft(), TOP + HEIGHT_GAP);
		UiUtil.add(getContainerPrincipal(), new LabelName(Messages.CLIENTEENDATUA_LABEL_TIPOATUALIZACAO), lvTipoAtualizacao, getLeft(), AFTER + HEIGHT_GAP);
	}
	
	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		super.domainToScreen(domain);
		ClienteEndAtua clienteEndAtua = (ClienteEndAtua) domain;
		lvDtAtualizacao.setValue(clienteEndAtua.dtAtualizacao);
		lvTipoAtualizacao.setValue(clienteEndAtua.getTipoAtualizacao());
		pbDsDiaEntrega.setValuesFormatted(getComponentValue(clienteEndAtua, "DSDIAENTREGA"));
        edDsPeriodoEntrega.setHoraInicialFinalFormatted(getComponentValue(clienteEndAtua, "DSPERIODOENTREGA"));
        edDsPeriodoEntregaAlternativo.setHoraInicialFinalFormatted(getComponentValue(clienteEndAtua, "DSPERIODOENTREGAALTERNATIVO"));
        pbDsDiaAbertura.setValuesFormatted(getComponentValue(clienteEndAtua, "DSDIAABERTURA"));
        edDsPeriodoAbertura.setHoraInicialFinalFormatted(getComponentValue(clienteEndAtua, "DSPERIODOABERTURA"));
        bgFlComercial.setValue(getComponentValue(clienteEndAtua, "FLCOMERCIAL"));
        bgFlEntrega.setValue(getComponentValue(clienteEndAtua, "FLENTREGA"));
        bgFlEntregaPadrao.setValue(getComponentValue(clienteEndAtua, "FLENTREGAPADRAO"));
        bgFlEntregaAgendada.setValue(getComponentValue(clienteEndAtua, "FLENTREGAAGENDADA"));
        bgFlCobranca.setValue(getComponentValue(clienteEndAtua, "FLCOBRANCA"));
        bgFlCobrancaPadrao.setValue(getComponentValue(clienteEndAtua, "FLCOBRANCAPADRAO"));
        if (bgFlComercial.getValueBoolean()) {
        	icComercial.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icComercial.setImage(UiUtil.getSmoothScaledImage(imgComercial, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (bgFlEntrega.getValueBoolean()) {
        	icEntrega.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icEntrega.setImage(UiUtil.getSmoothScaledImage(imgEntrega, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (bgFlEntregaPadrao.getValueBoolean()) {
        	icEntregaPadrao.getImage().applyColor2(ColorUtil.baseForeColorSystem);
        } else {
        	icEntregaPadrao.setImage(UiUtil.getSmoothScaledImage(imgEntregaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (bgFlCobranca.getValueBoolean()) {
        	icCobranca.getImage().applyColor2(Color.darker(Color.ORANGE, 80));
        } else {
        	icCobranca.setImage(UiUtil.getSmoothScaledImage(imgCobranca, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
        if (bgFlCobrancaPadrao.getValueBoolean()) {
        	icCobrancaPadrao.getImage().applyColor2(Color.darker(Color.ORANGE, 80));
        } else {
        	icCobrancaPadrao.setImage(UiUtil.getSmoothScaledImage(imgCobrancaPadrao, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG));
        }
	}

	private String getComponentValue(ClienteEndAtua clienteEndAtua, String nmCampo) {
		return StringUtil.getStringValue(clienteEndAtua.getHashValuesDinamicos().get(nmCampo));
	}

	@Override
	protected String getDsTable() throws SQLException {
		return ClienteEndAtua.TABLE_NAME;
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ClienteEndAtua();
	}

	@Override
	protected String getEntityDescription() {
		return Messages.CLIENTEENDATUA_NOME_ENTIDADE;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ClienteEndAtuaService.getInstance();
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
		Vector columnsList = hashComponentes.getKeys();
		int sizeList = columnsList.size();
		String dsColuna;
		for (int i = 0; i < sizeList; i++) {
			dsColuna = ((String)columnsList.items[i]).toUpperCase();
			Control c = (Control)hashComponentes.get(dsColuna);
			if (c instanceof Edit) {
				((Edit)c).setEditable(enabled);
			} 
		}
	}
	
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
	
	public boolean isApresentaDiasEntregaEmpresaCadastroEndereco() {
		return LavenderePdaConfig.apresentaDiasEntregaEmpresaCadastroEndereco && isCampoDisponivelEdicao(ClienteEndereco.NMCOLUNA_DSDIAENTREGA);
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
	
	@Override
	protected boolean ignoreField(String nmCampo) {
		return ClienteEndereco.NMCOLUNA_DSDIAENTREGA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_DSPERIODOENTREGA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_DSPERIODOENTREGAALTERNATIVO.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_DSDIAABERTURA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_DSPERIODOABERTURA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLCOMERCIAL.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLENTREGA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLENTREGAPADRAO.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLENTREGAAGENDADA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLCOBRANCA.toUpperCase().equals(nmCampo)
				|| ClienteEndereco.NMCOLUNA_FLCOBRANCAPADRAO.toUpperCase().equals(nmCampo);
	}
	
}
