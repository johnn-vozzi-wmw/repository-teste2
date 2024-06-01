package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.AtendimentoAtivService;
import br.com.wmw.lavenderepda.business.service.SacService;
import totalcross.util.Date;

public class CadSacAtividadeWindow extends WmwCadWindow {

    private EditMemo edDsAtividade;
    private LabelValue lbCdAtividade;
    
    private Sac sac;
    private AtendimentoAtiv atendimentoAtiv;


	public CadSacAtividadeWindow(Sac sac) throws SQLException {
		super(Messages.CADASTRO_TIPO_ATIVIDADE_SAC);
		edDsAtividade = new EditMemo("@@@", 4, 2, 255).setID("edObs");
		lbCdAtividade = new LabelValue("");
		this.sac = sac;
		this.atendimentoAtiv = new AtendimentoAtiv();
		setDefaultRect();
	}
	

	//@Override
	protected String getEntityDescription() {
		return title;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return AtendimentoAtivService.getInstance();
	}

	//@Override
	protected BaseDomain createDomain() throws SQLException {
		return new AtendimentoAtiv();
	}

	//@Override
	protected BaseDomain screenToDomain() throws SQLException {
		atendimentoAtiv.dsObservacao = edDsAtividade.getValue().trim();
		atendimentoAtiv.cdEmpresa = sac.cdEmpresa;
		atendimentoAtiv.cdRepresentante = sac.cdRepresentante;
		atendimentoAtiv.cdCliente = sac.cdCliente;
		atendimentoAtiv.cdUsuarioAtendimento = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		atendimentoAtiv.cdUsuarioOriginal = sac.cdUsuario;
		atendimentoAtiv.cdSac = sac.cdSac;
		atendimentoAtiv.cdTipoSac = sac.cdTipoSac;
		atendimentoAtiv.dtAtendimento = new Date();
		atendimentoAtiv.cdAtendimentoAtividade = getCrudService().generateIdGlobal();
		atendimentoAtiv.flOrigem = "P";
		atendimentoAtiv.cdStatusAtendimento = AtendimentoAtiv.CDSTATUSATENDIMENTO_EM_ANDAMENTO;
		return atendimentoAtiv;
	}

	//@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		atendimentoAtiv = (AtendimentoAtiv) domain;
		edDsAtividade.setValue(atendimentoAtiv.dsObservacao);
		lbCdAtividade.setValue(atendimentoAtiv.cdAtendimentoAtividade);
		btExcluir.setVisible(!BaseDomain.FLTIPOALTERACAO_INSERIDO.equals(getDomain().flTipoAlteracao));
	}

	//@Override
	protected void clearScreen() throws SQLException {
		edDsAtividade.setText("");
		lbCdAtividade.setText("");
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.SAC_CODIGO_ATIVIDADES), lbCdAtividade, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.SAC_DETALHES_ATENDIMENTO_TITULO + "*"), edDsAtividade, getLeft(), getNextY());
	}

	@Override
	protected void salvarClick() throws SQLException {
		super.salvarClick();
		if (ValueUtil.valueEquals(sac.cdStatusSac, Sac.CDSTATUSSAC_NAO_INICIADO)) {
			sac.cdStatusSac = Sac.CDSTATUSSAC_EM_ANDAMENTO;
			SacService.getInstance().sacAtulizaStatus(sac);
		}
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		atendimentoAtiv = (AtendimentoAtiv) getDomain();
		atendimentoAtiv.hrAtendimento = TimeUtil.getCurrentTimeHHMM();
		atendimentoAtiv.dtAlteracao = new Date();
		atendimentoAtiv.hrAlteracao = TimeUtil.getCurrentTimeHHMM();
		super.beforeSave();
	}
	
}