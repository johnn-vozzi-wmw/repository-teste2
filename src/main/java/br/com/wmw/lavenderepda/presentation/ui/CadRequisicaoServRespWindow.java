package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServResp;

public class CadRequisicaoServRespWindow extends WmwWindow  {

	private LabelValue lvCdRequisicaoServResp;
	private LabelValue lvDtHrRequisicaoServResp;
	private LabelValue lvDsRequisicaoServMotivoRet;
	private LabelValue lvDsStatusRequisicao;
	private LabelValue lvObservacao;
	private LabelValue lvNmUsuario;
	private RequisicaoServResp requisicaoServResp;

	public CadRequisicaoServRespWindow(RequisicaoServResp requisicaoServResp) {
		super(Messages.REQUISICAOSERVRESP_TITULO_WINDOW);
		this.requisicaoServResp = requisicaoServResp;
		lvCdRequisicaoServResp = new LabelValue("");
		lvDtHrRequisicaoServResp = new LabelValue("");
		lvDsRequisicaoServMotivoRet = new LabelValue("");
		lvDsRequisicaoServMotivoRet.autoMultipleLines = true;
		lvDsStatusRequisicao = new LabelValue("");
		lvObservacao = new LabelValue("");
		lvObservacao.autoMultipleLines = true;
		lvNmUsuario = new LabelValue("");
		btFechar.setText(FrameworkMessages.BOTAO_FECHAR);
		setDefaultRect();
		domainToScreen();
	}

	private void domainToScreen() {
		lvCdRequisicaoServResp.setValue(requisicaoServResp.cdRequisicaoServResp);
		lvDtHrRequisicaoServResp.setValue(requisicaoServResp.dtRequisicaoServResp+" - "+requisicaoServResp.hrRequisicaoServResp.substring(0, 5));
		lvDsRequisicaoServMotivoRet.setValue(requisicaoServResp.motivoServ.toString());
		lvDsStatusRequisicao.setValue(requisicaoServResp.motivoServ.getDsStatusRequisicao());
		lvObservacao.setValue(requisicaoServResp.dsObservacao);
		lvNmUsuario.setValue(requisicaoServResp.nmUsuarioCriacao + (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades ? " ["+requisicaoServResp.cdUsuarioCriacao+"]" : ""));
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERVRESP_LABEL_RESP), lvCdRequisicaoServResp, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERVRESP_LABEL_USUARIO), lvNmUsuario, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERVRESP_LABEL_DATA_E_HORA), lvDtHrRequisicaoServResp, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERVRESP_LABEL_MOTIVO), lvDsRequisicaoServMotivoRet, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERVRESP_LABEL_STATUS), lvDsStatusRequisicao, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), lvObservacao, getLeft(), AFTER + HEIGHT_GAP);
	}
	

}
