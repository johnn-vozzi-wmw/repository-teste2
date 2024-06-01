package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.IAddId;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PermissaoSolAut;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.PermissaoSolAutService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.UsuarioGrupoProdutoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadSolAutorizacaoForm extends BaseCrudCadForm {

	protected ButtonPopup btAutorizar, btNaoAutorizar;
	private LabelName lbEmpresa, lbRepresentante, lbProduto, lbCliente, lbNuPedido, lbTipoSolicitacao, lbCdSequencia, lbQuantidade, lbValorOriginal, lbValorUn, lbValorTotal, lbDataSolicitacao, lbDataLiberacao, lbUsuarioResp, lbMsgResp, lbStatusSol;
	private LabelValue lvEmpresa, lvRepresentante, lvProduto, lvCliente, lvNuPedido, lvTipoSolicitacao, lvCdSequencia, lvQuantidade, lvValorOriginal, lvValorUn, lvValorTotal, lvDataSolicitacao, lvDataLiberacao, lvUsuarioResp, lvMsgResp, lvStatusSol;

	private boolean readOnly;
	private ItemPedido itemPedido;
	private Pedido pedido;
	private SolAutorizacao solAutorizacaoSelecionada;

	@Override public String getEntityDescription() {
		return Messages.SOL_AUTORIZACAO_ANALISE_AUTORIZACAO_LABEL;
	}
	@Override protected CrudService getCrudService() {
		return null;
	}
	@Override protected BaseDomain createDomain() {
		return null;
	}
	@Override protected BaseDomain screenToDomain() {
		return null;
	}

    public CadSolAutorizacaoForm(Pedido pedido, ItemPedido itemPedido, boolean readOnly, SolAutorizacao solAutorizacaoSelected) {
        super(Messages.SOL_AUTORIZACAO_ANALISE_AUTORIZACAO_LABEL);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		this.readOnly = readOnly;
		this.solAutorizacaoSelecionada = solAutorizacaoSelected;

    	lbEmpresa = new LabelName(Messages.EMPRESA_NOME_ENTIDADE);
	    lbRepresentante = new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE);
	    lbProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
	    lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
	    lbNuPedido = new LabelName(Messages.SOL_AUTORIZACAO_NU_PEDIDO);
	    lbTipoSolicitacao = new LabelName(Messages.SOL_AUTORIZACAO_DS_TIPO_SOL_AUTORIZACAO);
	    lbCdSequencia = new LabelName(Messages.SOL_AUTORIZACAO_SEQUENCIA);
	    lbQuantidade = new LabelName(Messages.SOL_AUTORIZACAO_QT_ITEM_FISICO);
	    lbValorOriginal = new LabelName(Messages.SOL_AUTORIZACAO_VL_ORIGINAL);
	    lbValorUn = new LabelName(Messages.SOL_AUTORIZACAO_VL_UNITARIO);
	    lbValorTotal = new LabelName(Messages.SOL_AUTORIZACAO_VL_TOTAL);
	    lbDataSolicitacao = new LabelName(Messages.SOL_AUTORIZACAO_DATA_HORA);
	    lbDataLiberacao = new LabelName(Messages.SOL_AUTORIZACAO_DATA_HORA_LIB);
	    lbUsuarioResp = new LabelName(Messages.SOL_AUTORIZACAO_USUARIO_RESPONSAVEL);
	    lbStatusSol = new LabelName(Messages.SOL_AUTORIZACAO_STATUS);
	    lbMsgResp = new LabelName(Messages.SOL_AUTORIZACAO_OBSERVACAO);

	    lvEmpresa  = new LabelValue().setID("lvEmpresa");
	    lvRepresentante = new LabelValue().setID("lvRepresentante");
	    lvProduto = new LabelValue().setID("lvProduto");
	    lvCliente = new LabelValue().setID("lvCliente");
	    lvNuPedido = new LabelValue().setID("lvNuPedido");
	    lvTipoSolicitacao = new LabelValue().setID("lvTipoSolicitacao");
	    lvCdSequencia = new LabelValue().setID("lvCdSequencia");
	    lvQuantidade = new LabelValue().setID("lvQuantidade");
	    lvValorOriginal = new LabelValue().setID("lvValorOriginal");
	    lvValorUn = new LabelValue().setID("lvValorUn");
	    lvValorTotal = new LabelValue().setID("lvValorTotal");
	    lvDataSolicitacao = new LabelValue().setID("lvDataSolicitacao");
	    lvDataLiberacao = new LabelValue().setID("lvDataLiberacao");
	    lvStatusSol = new LabelValue().setID("lvStatusSol");
	    lvUsuarioResp = new LabelValue().setID("lvUsuarioResp");
	    lvMsgResp = new LabelValue().setID("lvMsgResp");

	    btAutorizar = new ButtonPopup(Messages.SOL_AUTORIZACAO_AUTORIZAR);
	    btNaoAutorizar = new ButtonPopup(Messages.SOL_AUTORIZACAO_NAO_AUTORIZAR);
	}

	@Override
	protected SolAutorizacao getDomain() throws SQLException {
		return (!this.readOnly) ? (SolAutorizacao) super.getDomain() : solAutorizacaoSelecionada;
	}

	@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
		SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
		int maxWidth = this.width - WIDTH_GAP_BIG;
		lvEmpresa.setText(MessageUtil.quebraLinhas(solAutorizacao.empresa.toString(), maxWidth));
		lvRepresentante.setText(MessageUtil.quebraLinhas(solAutorizacao.representante.toString(), maxWidth));
		lvProduto.setText(MessageUtil.quebraLinhas(solAutorizacao.produto.toString(), maxWidth));
		lvCliente.setText(MessageUtil.quebraLinhas(solAutorizacao.cliente.toString(), maxWidth));
		lvNuPedido.setText(MessageUtil.quebraLinhas(solAutorizacao.nuPedido, maxWidth));
		lvTipoSolicitacao.setText(MessageUtil.quebraLinhas(solAutorizacao.tipoSolicitacaoAutorizacaoEnum.getTitle(), maxWidth));
		lvCdSequencia.setText(MessageUtil.quebraLinhas(solAutorizacao.cdSolAutorizacao, maxWidth));
		lvQuantidade.setText(MessageUtil.quebraLinhas(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) solAutorizacao.qtItemFisico) : StringUtil.getStringValueToInterface(solAutorizacao.qtItemFisico), maxWidth));
		lvValorOriginal.setText(MessageUtil.quebraLinhas(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(solAutorizacao.vlOriginal), maxWidth));
		lvValorUn.setText(MessageUtil.quebraLinhas(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(solAutorizacao.vlItemPedido), maxWidth));
		lvValorTotal.setText(MessageUtil.quebraLinhas(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(solAutorizacao.vlTotalItemPedido), maxWidth));
		lvDataSolicitacao.setText(MessageUtil.quebraLinhas(StringUtil.getStringValue(solAutorizacao.dtSolAutorizacao) + " - " + StringUtil.getStringValue(solAutorizacao.hrSolAutorizacao), maxWidth));
		lvDataLiberacao.setText(MessageUtil.quebraLinhas(StringUtil.getStringValue(solAutorizacao.dtLibSolAutorizacao) + " - " + StringUtil.getStringValue(solAutorizacao.hrLibSolAutorizacao), maxWidth));
		lvStatusSol.setText(MessageUtil.quebraLinhas(solAutorizacao.getStatus(), maxWidth));
		lvUsuarioResp.setText(MessageUtil.quebraLinhas(new UsuarioRelRep(solAutorizacao.cdUsuarioLibSolAutorizacao, solAutorizacao.nmUsuarioLibSolAutorizacao).toString(), maxWidth));
		lvMsgResp.setText(MessageUtil.quebraLinhas(solAutorizacao.dsObservacao, maxWidth));
	}

	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
	}

	@Override
    protected void clearScreen() {
	    lvEmpresa.setText(ValueUtil.VALOR_NI);
	    lvRepresentante.setText(ValueUtil.VALOR_NI);
	    lvProduto.setText(ValueUtil.VALOR_NI);
	    lvCliente.setText(ValueUtil.VALOR_NI);
	    lvNuPedido.setText(ValueUtil.VALOR_NI);
		lvTipoSolicitacao.setText(ValueUtil.VALOR_NI);
		lvCdSequencia.setText(ValueUtil.VALOR_NI);
	    lvQuantidade.setText(ValueUtil.VALOR_NI);
	    lvValorOriginal.setText(ValueUtil.VALOR_NI);
		lvValorUn.setText(ValueUtil.VALOR_NI);
	    lvValorTotal.setText(ValueUtil.VALOR_NI);
	    lvDataSolicitacao.setText(ValueUtil.VALOR_NI);
	    lvDataLiberacao.setText(ValueUtil.VALOR_NI);
		lvStatusSol.setText(ValueUtil.VALOR_NI);
		lvUsuarioResp.setText(ValueUtil.VALOR_NI);
		lvMsgResp.setText(ValueUtil.VALOR_NI);
    }

	@Override
	public void initUI() {
		if (!this.readOnly) {
			super.initUI();
			return;
		}
		try {
			domainToScreen(this.solAutorizacaoSelecionada);
			onFormStart();
			refreshComponents();
			visibleState();
		} catch (Throwable e) {
			showException(e);
		}
	}

	@Override
    protected void onFormStart() throws SQLException {
		domainToScreen(this.solAutorizacaoSelecionada);
		BaseScrollContainer bs = new BaseScrollContainer(false ,true);
	    UiUtil.add(bs, lbEmpresa, lvEmpresa, getLeft(), TOP);
	    UiUtil.add(bs, lbRepresentante, lvRepresentante, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbProduto, lvProduto, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbCliente, lvCliente, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbNuPedido, lvNuPedido, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(bs, lbTipoSolicitacao, lvTipoSolicitacao, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(bs, lbCdSequencia, lvCdSequencia, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbQuantidade, lvQuantidade, getLeft(), AFTER + HEIGHT_GAP);
	    if (this.solAutorizacaoSelecionada.tipoSolicitacaoAutorizacaoEnum == TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO) {
		    UiUtil.add(bs, lbValorOriginal, lvValorOriginal, getLeft(), AFTER + HEIGHT_GAP);
	    }
	    UiUtil.add(bs, lbValorUn, lvValorUn, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbValorTotal, lvValorTotal, getLeft(), AFTER + HEIGHT_GAP);
	    UiUtil.add(bs, lbDataSolicitacao, lvDataSolicitacao, getLeft(), AFTER + HEIGHT_GAP);
	    if (ValueUtil.isNotEmpty(this.solAutorizacaoSelecionada.dtLibSolAutorizacao)) {
	    	UiUtil.add(bs, lbDataLiberacao, lvDataLiberacao, getLeft(), AFTER + HEIGHT_GAP);
	    }
		if (ValueUtil.isNotEmpty(this.solAutorizacaoSelecionada.nmUsuarioLibSolAutorizacao)) {
			UiUtil.add(bs, lbUsuarioResp, lvUsuarioResp, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(bs, lbStatusSol, lvStatusSol, getLeft(), AFTER + HEIGHT_GAP);
		if (ValueUtil.isNotEmpty(this.solAutorizacaoSelecionada.dsObservacao)) {
			UiUtil.add(bs, lbMsgResp, lvMsgResp, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, bs, LEFT, (readOnly) ? TOP : TOP + barTopContainer.getHeight() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
    }

	@Override
    protected void addBarButtons() {
		try {
			UiUtil.add(barBottomContainer, btNaoAutorizar, LEFT + 1, TOP + 1, (width / 2) - 2, barBottomContainer.getHeight() - 1);
			UiUtil.add(barBottomContainer, btAutorizar , RIGHT - 1, TOP + 1, (width / 2) - 1, barBottomContainer.getHeight() - 1);
		} catch (Throwable e) {
			showException(e);
		}
	}

	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		SolAutorizacao solAutorizacao = getDomain();

		if (!readOnly && ValueUtil.isNotEmpty(solAutorizacao.flAutorizado)) {
			visibleStateAutorizeButtons(false);
		} else {
			PermissaoSolAut permissaoSolAut = null;
			if (!readOnly) {
				permissaoSolAut = PermissaoSolAutService.getInstance().getBySessionUser(solAutorizacao.tipoSolicitacaoAutorizacaoEnum);
			}
			if (permissaoSolAut == null || permissaoSolAut.isSomenteLeitura()) {
				visibleStateAutorizeButtons(false);
			} else {
				visibleStateAutorizeButtons(permissaoSolAut.isIgnoraUsuarioGrupoProd() || UsuarioGrupoProdutoService.getInstance().sessionUserCanAutorize(solAutorizacao.produto.cdGrupoProduto1));
			}
		}

		if (solAutorizacao.isAutorizado()) {
			lvStatusSol.setForeColor(ColorUtil.softGreen);
		} else if (solAutorizacao.isNaoAutorizado()) {
			lvStatusSol.setForeColor(ColorUtil.softRed);
		} else {
			lvStatusSol.setForeColor(ColorUtil.componentsForeColor);
		}
	}

	private void visibleStateAutorizeButtons(boolean value) {
		btAutorizar.setVisible(value);
		btNaoAutorizar.setVisible(value);
	}

	@Override
    protected void onFormEvent(Event event) throws SQLException {
	    switch (event.type) {
		    case ControlEvent.PRESSED:
			    if (event.target == btAutorizar) {
				    btAutorizarClick();
			    } else if (event.target == btNaoAutorizar) {
				    btNaoAutorizarClick();
			    }
		    break;
	    }
    }

	private void btAutorizarClick() throws SQLException {
		int res = UiUtil.showConfirmMessage(Messages.SOL_AUTORIZACAO_CONFIRMAR_AUTORIZACAO_TITLE, Messages.SOL_AUTORIZACAO_CONFIRMAR_AUTORIZACAO, new String[] {FrameworkMessages.BOTAO_CANCELAR, Messages.BT_CONFIRMAR});
		if (res != 1) return;
		SolAutorizacao solAutorizacao = this.getDomain();
		boolean sincronizado = SolAutorizacaoService.getInstance().autorizarSolicitacao(solAutorizacao);
		if (this.pedido != null) {
			this.pedido.solAutorizacaoPedidoCache.clearCaches(this.pedido);
		} else if (this.itemPedido != null) {
			if (this.itemPedido.pedido != null) {
				this.itemPedido.pedido.solAutorizacaoPedidoCache.clearCaches(this.itemPedido.pedido);
			}
			this.itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
		}
		String message = sincronizado ? Messages.SOL_AUTORIZACAO_AUTORIZADO_SYNC_SUCCESS : Messages.SOL_AUTORIZACAO_AUTORIZADO_SYNC_FAIL;
		UiUtil.showSucessMessage(message);
		reloadAutorizacaoAfterUpdater(solAutorizacao);
	}

	private void btNaoAutorizarClick() throws SQLException {
		CadMotivoSolAutorizacaoNegadaWindow cadMotivoSolAutorizacaoNegadaWindow = new CadMotivoSolAutorizacaoNegadaWindow();
		cadMotivoSolAutorizacaoNegadaWindow.popup();
		if (!cadMotivoSolAutorizacaoNegadaWindow.confirmadoNegacao) return;
		SolAutorizacao solAutorizacao = this.getDomain();
		solAutorizacao.dsObservacao = ValueUtil.isNotEmpty(cadMotivoSolAutorizacaoNegadaWindow.motivoNegacao) ? cadMotivoSolAutorizacaoNegadaWindow.motivoNegacao : null;

		boolean sincronizado = SolAutorizacaoService.getInstance().negarSolicitacao(solAutorizacao);
		String message = sincronizado ? Messages.SOL_AUTORIZACAO_NAO_AUTORIZADO_SYNC_SUCCESS : Messages.SOL_AUTORIZACAO_NAO_AUTORIZADO_SYNC_FAIL;
		UiUtil.showSucessMessage(message);
		reloadAutorizacaoAfterUpdater(solAutorizacao);
	}

	private void reloadAutorizacaoAfterUpdater(SolAutorizacao solAutorizacao) throws SQLException {
		setDomain(SolAutorizacaoService.getInstance().findByRowKey(solAutorizacao.rowKey));
		solAutorizacaoSelecionada = getDomain();
		repaintScreen();
	}

	private void repaintScreen() {
		removeAll();
		this.initUI();
	}

}
