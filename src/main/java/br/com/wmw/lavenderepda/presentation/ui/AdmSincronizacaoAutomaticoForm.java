package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoApp2WebRunnable;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoWeb2AppRunnable;
import totalcross.sys.Time;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class AdmSincronizacaoAutomaticoForm extends BaseUIForm {
	
	private LabelContainer containerApp2Web;
	private LabelName lbStatusApp2Web;
	private LabelValue lvStatusApp2Web;
	private LabelName lbUltimoApp2Web;
	private LabelValue lvUltimoApp2Web;
	private LabelName lbProximoApp2Web;
	private LabelValue lvProximoApp2Web;
	
	private LabelContainer containerWeb2App;
	private LabelName lbStatusWeb2App;
	private LabelValue lvStatusWeb2App;
	private LabelName lbUltimoWeb2App;
	private LabelValue lvUltimoWeb2App;
	private LabelName lbProximoWeb2App;
	private LabelValue lvProximoWeb2App;
	
//	private ButtonAction btRelLogSyncBackground;
	
	private BaseScrollContainer scrollContainer;

	public AdmSincronizacaoAutomaticoForm() {
		super(Messages.SINCRONIZACAO_AUTOMATICO_NOME_ENTIDADE);
		scrollContainer = new BaseScrollContainer(false, true);
		
		containerApp2Web = new LabelContainer(Messages.SINCRONIZACAO_AUTOMATICO_TITLE_ENVIAR_DADOS);
		lbStatusApp2Web = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_STATUS_APP2WEB);
		lvStatusApp2Web = new LabelValue(Messages.SINCRONIZACAO_AUTOMATICO_MSG_AGUARDANDO);
		lbUltimoApp2Web = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_ULTIMA_ATUALIZACAO_APP2WEB);
		lvUltimoApp2Web = new LabelValue();
		lbProximoApp2Web = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_PROXIMA_ATUALIZACAO_APP2WEB);
		lvProximoApp2Web = new LabelValue();
		
		containerWeb2App = new LabelContainer(Messages.SINCRONIZACAO_AUTOMATICO_TITLE_RECEBER_DADOS);
		lbStatusWeb2App = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_STATUS_WEB2APP);
		lvStatusWeb2App = new LabelValue(Messages.SINCRONIZACAO_AUTOMATICO_MSG_AGUARDANDO);
		lbUltimoWeb2App = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_ULTIMA_ATUALIZACAO_WEB2APP);
		lvUltimoWeb2App = new LabelValue();
		lbProximoWeb2App = new LabelName(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_PROXIMA_ATUALIZACAO_WEB2APP);
		lvProximoWeb2App = new LabelValue();
		
		//btRelLogSyncBackground = new ButtonAction(Messages.SINCRONIZACAO_AUTOMATICO_LABEL_DETALHES, "images/log.png");
		atualizaDados();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, scrollContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
		if (SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado()) {
			UiUtil.add(scrollContainer, containerApp2Web, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, LabelContainer.getStaticHeight());
			UiUtil.add(scrollContainer, lbUltimoApp2Web, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvUltimoApp2Web, getLeft(), AFTER);
			UiUtil.add(scrollContainer, lbProximoApp2Web, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvProximoApp2Web, getLeft(), AFTER);
			UiUtil.add(scrollContainer, lbStatusApp2Web, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvStatusApp2Web, getLeft(), AFTER);
		}
		if (SincronizacaoWeb2AppRunnable.getInstance().isSyncAutomaticoLigado()) {
			UiUtil.add(scrollContainer, containerWeb2App, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, LabelContainer.getStaticHeight());
			UiUtil.add(scrollContainer, lbUltimoWeb2App, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvUltimoWeb2App, getLeft(), AFTER);
			UiUtil.add(scrollContainer, lbProximoWeb2App, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvProximoWeb2App, getLeft(), AFTER);
			UiUtil.add(scrollContainer, lbStatusWeb2App, getLeft(), AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(scrollContainer, lvStatusWeb2App, getLeft(), AFTER);
		}
	}

	public void reloadVisibleState() {
		repaintNow();
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				//TODO REFATORAR
//				if (event.target == btRelLogSyncBackground) {
//					RelLogSyncBackgroundLavendereForm relLogSyncBackgroundForm = new RelLogSyncBackgroundLavendereForm();
//					show(relLogSyncBackgroundForm);
//				}
				break;
			}
		}
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		atualizaDados();
	}
	
	public void atualizaDados() {
		try {
			atualizaLabels();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void atualizaLabels() {
		try {
			updateLabelsApp2Web();
			updateLabelsWeb2App();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void updateLabelsApp2Web() {
		if (SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado()) {
			Time lastRun = SincronizacaoApp2WebRunnable.getInstance().getLastRun();
			if (SincronizacaoApp2WebRunnable.getInstance().isRunning()) {
				lvUltimoApp2Web.setText(ValueUtil.VALOR_NI);
				lvProximoApp2Web.setText(ValueUtil.VALOR_NI);
				lvStatusApp2Web.setText(Messages.SINCRONIZACAO_AUTOMATICO_MSG_ATUALIZANDO);
			} else {
				lvUltimoApp2Web.setText(DateUtil.formatDateDDMMYYYY(DateUtil.getDateValue(lastRun)) + " - " + TimeUtil.formatTimeString(lastRun, true, true));
				lvStatusApp2Web.setText(Messages.SINCRONIZACAO_AUTOMATICO_MSG_AGUARDANDO);
				Time nextRun = SincronizacaoApp2WebRunnable.getInstance().getNextRun();
				if (nextRun != null) {
					lvProximoApp2Web.setText(DateUtil.formatDateDDMMYYYY(DateUtil.getDateValue(nextRun)) + " - " + TimeUtil.formatTimeString(nextRun, true, true));
				} else {
					lvProximoApp2Web.setText(ValueUtil.VALOR_NI);
				}
			}
		}
	}

	private void updateLabelsWeb2App() {
		if (SincronizacaoWeb2AppRunnable.getInstance().isSyncAutomaticoLigado()) {
			Time lastRun = SincronizacaoWeb2AppRunnable.getInstance().getLastRun();
			if (SincronizacaoWeb2AppRunnable.getInstance().isRunning()) {
				lvUltimoWeb2App.setText(ValueUtil.VALOR_NI);
				lvProximoWeb2App.setText(ValueUtil.VALOR_NI);
				lvStatusWeb2App.setText(Messages.SINCRONIZACAO_AUTOMATICO_MSG_ATUALIZANDO);
			} else {
				lvUltimoWeb2App.setText(DateUtil.formatDateDDMMYYYY(DateUtil.getDateValue(lastRun)) + " - " + TimeUtil.formatTimeString(lastRun, true, true));
				lvStatusWeb2App.setText(Messages.SINCRONIZACAO_AUTOMATICO_MSG_AGUARDANDO);
				Time nextRun = SincronizacaoWeb2AppRunnable.getInstance().getNextRun();
				if (nextRun != null) {
					lvProximoWeb2App.setText(DateUtil.formatDateDDMMYYYY(DateUtil.getDateValue(nextRun)) + " - " + TimeUtil.formatTimeString(nextRun, true, true));
				} else {
					lvProximoWeb2App.setText(ValueUtil.VALOR_NI);
				}
			}
		}
	}
	
}
