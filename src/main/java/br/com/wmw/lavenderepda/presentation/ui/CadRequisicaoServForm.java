package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.RequisicaoServImagemService;
import br.com.wmw.lavenderepda.business.service.RequisicaoServService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RequisicaoServMotivoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RequisicaoServTipoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderRequisicaoServWindow;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadRequisicaoServForm extends BaseCrudCadForm {
	
	private RequisicaoServTipoComboBox cbRequisicaoServTipoCombo;
	private RequisicaoServMotivoComboBox cbRequisicaoServMotivoCombo;
	private LabelValue lvCodigoRequisicao;
	private LabelValue lvStatusRequisicao;
	private LabelValue lvRequisicaoServTipo;
	private LabelValue lvRequisicaoServMotivo;
	private LabelValue lvCliente;
	private LabelValue lvPedido;
	private LabelValue lvDataHoraRequisicao;
	private LabelValue lvObservacao;
	private EditMemo edDsObservacao;
	private EditText edDsCliente;
	private BaseButton btFiltrarCliente;
	private EditText edDsPedido;
	private BaseButton btFiltrarPedido;
    private String cdCliente;
    private String nmRazaoSocial;
	private String cdRepresentante;
    private ScrollTabbedContainer scTabRequisicao;
    private boolean edit;
    private RequisicaoServTipo requisicaoServTipoFilter;
    private String nuPedido;
    private String flOrigemPedido;
    private ButtonAction btFoto;
    
    public CadRequisicaoServForm() throws SQLException {
    	super(Messages.REQUISICAO_SERV_NOME_ENTIDADE);
    	scrollable = true;
    	cbRequisicaoServMotivoCombo = new RequisicaoServMotivoComboBox();
		cbRequisicaoServMotivoCombo.drawBackgroundWhenDisabled = true;
		cbRequisicaoServMotivoCombo.setEditable(false);
		edDsObservacao = new EditMemo("@@@@@@@@@@", 10, 500);
		edDsCliente = new EditText("@@@@@@@@@@", 100);
		edDsCliente.drawBackgroundWhenDisabled = true;
		edDsCliente.setEditable(false);
		btFiltrarCliente = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
		btFiltrarCliente.setEnabled(false);
		edDsPedido = new EditText("@@@@@@@@@@", 100);
		edDsPedido.drawBackgroundWhenDisabled = true;
		edDsPedido.setEditable(false);
		btFiltrarPedido = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
		btFiltrarPedido.setEnabled(false);
		cbRequisicaoServTipoCombo = new RequisicaoServTipoComboBox(BaseComboBox.DefaultItemType_SELECT_ONE_ITEM);
		btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
    }
    
    public CadRequisicaoServForm(boolean edit) {
    	super(Messages.REQUISICAO_SERV_NOME_ENTIDADE);
		this.edit = edit;
    	initScTabbed();
    	lvCodigoRequisicao = new LabelValue("");
		lvStatusRequisicao = new LabelValue("");
		lvRequisicaoServTipo = new LabelValue("");
		lvRequisicaoServTipo.autoMultipleLines = true;
		lvRequisicaoServMotivo = new LabelValue("");
		lvRequisicaoServMotivo.autoMultipleLines = true;
		lvCliente = new LabelValue("");
		lvCliente.autoMultipleLines = true;
		lvPedido = new LabelValue("");
		lvDataHoraRequisicao = new LabelValue("");
		lvObservacao = new LabelValue("");
		lvObservacao.autoMultipleLines = true;
		btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
    }

    public CadRequisicaoServForm(String nmRazaoSocial, String cdCliente) throws SQLException {
    	this();
    	this.nmRazaoSocial = nmRazaoSocial;
    	this.cdCliente = cdCliente;
		cbRequisicaoServTipoCombo = new RequisicaoServTipoComboBox(BaseComboBox.DefaultItemType_SELECT_ONE_ITEM, true, false);
		cbRequisicaoServTipoComboClick();
    }
    
    public CadRequisicaoServForm(String nuPedido, String flOrigemPedido, String cdCliente) throws SQLException {
    	this();
    	this.nuPedido = nuPedido;
    	this.flOrigemPedido = flOrigemPedido;
    	this.cdCliente = cdCliente;
		cbRequisicaoServTipoCombo = new RequisicaoServTipoComboBox(BaseComboBox.DefaultItemType_SELECT_ONE_ITEM, false, true);
		cbRequisicaoServTipoComboClick();
    }

	private void initScTabbed() {
        String[] abas = new String[]{Messages.REQUISICAOSERV_TAB_DETALHES, Messages.REQUISICAOSERV_TAB_RESPOSTAS};
        scTabRequisicao = new ScrollTabbedContainer(abas);
        scTabRequisicao.allSameWidth = true;
	}
	
	 @Override
	 public void add() throws java.sql.SQLException {
    	super.add();
    	RequisicaoServ requisicaoServ = (RequisicaoServ) getDomain();
        requisicaoServ.cdEmpresa = SessionLavenderePda.cdEmpresa;
        requisicaoServ.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RequisicaoServ.class);
        requisicaoServ.cdRequisicaoServ = RequisicaoServService.getInstance().generateIdGlobal();
	 }

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) getDomain();
		requisicaoServ.cdCliente = cdCliente;
		requisicaoServ.cdRepresentante = cdRepresentante;
		requisicaoServ.tipoServ.cdRequisicaoServTipo = cbRequisicaoServTipoCombo.getValue();
		requisicaoServ.motivoServ.cdRequisicaoServMotivo = cbRequisicaoServMotivoCombo.getValue();
		requisicaoServ.motivoServ.flObrigaObservacao = cbRequisicaoServMotivoCombo.getFlObrigaObservacao();
		requisicaoServ.dsObservacao = edDsObservacao.getValue();
		requisicaoServ.obrigaCliente = cbRequisicaoServTipoCombo.isFlObrigaCliente();
		requisicaoServ.obrigaPedido = cbRequisicaoServTipoCombo.isFlObrigaPedido();
		requisicaoServ.nuPedido = nuPedido;
		requisicaoServ.flOrigemPedido = flOrigemPedido;
		requisicaoServ.dtRequisicaoServ = DateUtil.getCurrentDate();
		requisicaoServ.hrRequisicaoServ = TimeUtil.getCurrentTimeHHMMSS();
		return requisicaoServ;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain;
		lvCodigoRequisicao.setValue(requisicaoServ.cdRequisicaoServ);
		lvStatusRequisicao.setValue(requisicaoServ.motivoServ.getDsStatusRequisicao());
		lvRequisicaoServTipo.setValue(requisicaoServ.tipoServ.toString());
		lvCliente.setValue(requisicaoServ.cliente.toString());
		lvPedido.setValue(requisicaoServ.nuPedido);
		lvDataHoraRequisicao.setValue(requisicaoServ.dtRequisicaoServ+" - "+requisicaoServ.hrRequisicaoServ.substring(0,5));
		lvRequisicaoServMotivo.setValue(requisicaoServ.motivoServ.toString());
		lvObservacao.setValue(requisicaoServ.dsObservacao);
		ListRequisicaoServRespForm listRequisicaoResp = new ListRequisicaoServRespForm(requisicaoServ);
		scTabRequisicao.setContainer(1, listRequisicaoResp);
	}

	@Override
	protected void clearScreen() throws SQLException {
	}
	
    @Override
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	RequisicaoServ requisicaoServ = (RequisicaoServ)domain;
    	//--
    	super.edit(requisicaoServ);
    	//--
    	requisicaoServ.setRequisicaoServImagemList(RequisicaoServImagemService.getInstance().findAllByRequisicaoServ(requisicaoServ));
    	internalSetEnabled(!BaseDomain.FLTIPOALTERACAO_ORIGINAL.equals(requisicaoServ.flTipoAlteracao), false);
    }

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new RequisicaoServ();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return RequisicaoServService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (edit) {
			// Container de Requisições
			UiUtil.add(this, scTabRequisicao, getTop() + HEIGHT_GAP, UiUtil.getBottomBarPreferredHeight());
			Container tabRequisicao = scTabRequisicao.getContainer(0);
			UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_CODIGO), lvCodigoRequisicao, getLeft(), AFTER);
			UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_STATUS), lvStatusRequisicao, getLeft(), getNextY() + HEIGHT_GAP_BIG);
			UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_COMBO_TIPO_SERVICO), lvRequisicaoServTipo, getLeft(), getNextY() + HEIGHT_GAP_BIG);
			UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_DATA_HORA), lvDataHoraRequisicao, getLeft(), getNextY() + HEIGHT_GAP_BIG);
			RequisicaoServ requisicaoServ = (RequisicaoServ) getDomain();
			if (requisicaoServ.tipoServ.isObrigaPedido()) {
				UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_COMBO_PEDIDO), lvPedido, getLeft(), getNextY() + HEIGHT_GAP_BIG);
			}
			if (requisicaoServ.tipoServ.isObrigaCliente() || requisicaoServ.tipoServ.isObrigaPedido()) {
				UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_COMBO_CLIENTE), lvCliente, getLeft(), getNextY() + HEIGHT_GAP_BIG);				
			}
			UiUtil.add(tabRequisicao, new LabelName(Messages.REQUISICAOSERV_COMBO_MOTIVO), lvRequisicaoServMotivo , getLeft(), getNextY() + HEIGHT_GAP_BIG);
			UiUtil.add(tabRequisicao, new LabelName(Messages.OBSERVACAO_LABEL), lvObservacao , getLeft(), getNextY() + HEIGHT_GAP_BIG);
		} else {
			UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_TIPO_SERVICO), cbRequisicaoServTipoCombo, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_CLIENTE), edDsCliente, getLeft(), getNextY(), FILL - btFiltrarCliente.getPreferredWidth() - UiUtil.BASE_MARGIN_GAP - WIDTH_GAP);
			UiUtil.add(this, btFiltrarCliente, getRight(), SAME, PREFERRED, SAME);
			UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_PEDIDO), edDsPedido, getLeft(), AFTER, FILL - btFiltrarPedido.getPreferredWidth() - UiUtil.BASE_MARGIN_GAP - WIDTH_GAP);
			UiUtil.add(this, btFiltrarPedido, getRight(), SAME, PREFERRED, SAME);
			UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_MOTIVO), cbRequisicaoServMotivoCombo, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(barBottomContainer, btFoto, 5);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btFiltrarCliente) {
					btFiltroClienteClick();
				} else if (event.target == btFiltrarPedido) {
					btFiltroPedidoClick();
				} else if (event.target == cbRequisicaoServTipoCombo) {
					cbRequisicaoServTipoComboClick();
				} else if (event.target == btFoto) {
					btFotoClick();
				}
				break;
		}
	}


	private void btFotoClick() throws SQLException {
		ImageSliderRequisicaoServWindow imageSliderRequisicaoServWindow = new ImageSliderRequisicaoServWindow((RequisicaoServ) getDomain(), edit);
		imageSliderRequisicaoServWindow.popup();
	}

	private void cbRequisicaoServTipoComboClick() throws SQLException {
		String cdTipo = cbRequisicaoServTipoCombo.getValue();
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NI, cdTipo)) return;
		cbRequisicaoServMotivoCombo.reloadRequisicaoServMotivoCombo(cdTipo);
		boolean comboPreenchida = cbRequisicaoServMotivoCombo.size() > 1;
		if (!comboPreenchida) {
			cbRequisicaoServMotivoCombo.setSelectedIndex(-1);	
		}
		cbRequisicaoServMotivoCombo.setEditable(comboPreenchida);
		if (ValueUtil.isEmpty(nuPedido)) {
			loadPedidoFields();
		}
		if (ValueUtil.isEmpty(nmRazaoSocial)) {
			loadClienteFields();
		}
	}

	private void loadClienteFields() {
		if (cbRequisicaoServTipoCombo.isFlObrigaCliente()) {
			requisicaoServTipoFilter = new RequisicaoServTipo();
			requisicaoServTipoFilter.flFiltroStatusCliente = cbRequisicaoServTipoCombo.getFlFiltraClienteStatus();
			requisicaoServTipoFilter.dsStatusClienteList = cbRequisicaoServTipoCombo.getDsStatusClienteList();
			btFiltrarCliente.setEnabled(true);	
			edDsCliente.setText(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
			btFiltrarPedido.setEnabled(false);
			edDsPedido.setText(ValueUtil.VALOR_NI);
			nuPedido = null;
		} else {
			btFiltrarCliente.setEnabled(false);
			edDsCliente.setText(ValueUtil.VALOR_NI);
		}
	}

	private void loadPedidoFields() {
		if (cbRequisicaoServTipoCombo.isFlObrigaPedido()) {
			btFiltrarPedido.setEnabled(true);	
			edDsPedido.setText(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);	
			btFiltrarCliente.setEnabled(false);
			edDsCliente.setText(ValueUtil.VALOR_NI);
		} else {
			btFiltrarPedido.setEnabled(false);
			edDsPedido.setText(ValueUtil.VALOR_NI);
		}
	}

	private void btFiltroClienteClick() throws SQLException {
		ListClienteWindow listCliente = new ListClienteWindow(requisicaoServTipoFilter);
		listCliente.popup();
		if (listCliente.cliente != null) {
			edDsCliente.setText(listCliente.cliente.getDescription());
			cdCliente = listCliente.cliente.cdCliente;
			cdRepresentante = listCliente.cliente.cdRepresentante;
		}
	}
	
	private void btFiltroPedidoClick() {
		ListPedidoWindow listPedidoWindow = new ListPedidoWindow();
		if (listPedidoWindow.size > 0) {
			listPedidoWindow.popup();
			try {
				Pedido pedido = (Pedido) listPedidoWindow.listPedidoForm.getSelectedDomain();
				edDsPedido.setText(pedido.nuPedido);
				nuPedido = pedido.nuPedido;
				flOrigemPedido = pedido.flOrigemPedido;
				cdCliente = pedido.cdCliente;
			} catch (Throwable e) {
				// Nenhum pedido selecionado
			}
		} else {
			UiUtil.showInfoMessage(Messages.NENHUM_PEDIDO_CANCELAMENTO_ENCONTRADO);
		}
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (nmRazaoSocial != null) {
			edDsCliente.setText(nmRazaoSocial);
		}
		if (nuPedido != null) {
			edDsPedido.setText(nuPedido);
		}
		if (cbRequisicaoServTipoCombo != null && ValueUtil.isNotEmpty(cbRequisicaoServTipoCombo.getValue())) {
			cbRequisicaoServTipoComboClick();
		}
	}
	
	@Override
	protected void visibleState() throws SQLException {
		btSalvar.setVisible(!edit);
		btExcluir.setVisible(isEditing() && isEnabled());
	}
	
	@Override
	protected boolean delete() throws SQLException {
		boolean result = UiUtil.showConfirmDeleteMessage(getEntityDescription());
		if (result) {
			delete(getDomain());
		}
		return result;
	}
	
	@Override
	protected String getBtVoltarTitle() {
		return FrameworkMessages.BOTAO_VOLTAR;
	}
	
	@Override
	protected void salvarClick() throws SQLException {
		if (isRequisicaoServPendenteParaEsseTipo() && !UiUtil.showConfirmYesNoMessage(Messages.REQUISICAOSERV_REQUISICAO_JA_EXISTE)) {
			return;
		}
		super.salvarClick();
	}

	private boolean isRequisicaoServPendenteParaEsseTipo() throws SQLException {
		RequisicaoServ requisicaoServToScreen = (RequisicaoServ) screenToDomain();
		if (ValueUtil.isEmpty(requisicaoServToScreen.cdEmpresa) || ValueUtil.isEmpty(requisicaoServToScreen.cdCliente) || ValueUtil.isEmpty(requisicaoServToScreen.tipoServ.cdRequisicaoServTipo) || ValueUtil.isEmpty(requisicaoServToScreen.motivoServ.cdRequisicaoServMotivo)) {
			return false;
		}
		RequisicaoServ requisicaoServFilter = new RequisicaoServ();
		requisicaoServFilter.cdEmpresa = requisicaoServToScreen.cdEmpresa;
		requisicaoServFilter.cdCliente = requisicaoServToScreen.cdCliente;
		requisicaoServFilter.tipoServ = requisicaoServToScreen.tipoServ;
		return RequisicaoServService.getInstance().findByNewRequisicao(requisicaoServFilter) > 0;
	}
	
	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain;
		requisicaoServ.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		requisicaoServ.cdUsuarioCriacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		super.insert(requisicaoServ);
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) getDomain();
		if (requisicaoServ.flTipoAlteracao  == null) {
			RequisicaoServImagemService.getInstance().removeImagensFisicas(requisicaoServ.getRequisicaoServImagemExcluidaList());
			RequisicaoServImagemService.getInstance().removeImagensFisicas(requisicaoServ.getRequisicaoServImagemList());
		}
		super.voltarClick();
	}
	
	@Override
	protected void delete(BaseDomain domain) throws SQLException {
		super.delete(domain);
		RequisicaoServImagemService.getInstance().excluiImagensRequisicaoServ((RequisicaoServ) domain);
	}
}
