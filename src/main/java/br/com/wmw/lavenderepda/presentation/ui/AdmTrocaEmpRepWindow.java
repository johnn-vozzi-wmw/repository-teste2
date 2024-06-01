package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class AdmTrocaEmpRepWindow extends WmwWindow {

	public static final int LOGIN_RESULT_OK = 1;
	public static final int LOGIN_RESULT_CANCEL = 2;

	public int result;

	private LabelName lbEmp;
	private LabelName lbRep;
	private EmpresaComboBox cbEmpresa;
	private RepresentanteComboBox cbRep;
	private LabelValue lvRep;
	private LabelValue lvEmp;
	private CheckBoolean ckModoFeira;
	private ButtonPopup btOK;
	private ButtonPopup btCancelar;

	public UsuarioPdaRep usuario;
	public String cdEmpresa;
	public String cdRepresentante;
	public boolean modoFeira;

	public AdmTrocaEmpRepWindow() throws SQLException {
		super(Messages.CONFIG_SESSAO);
		lbEmp = new LabelName(Messages.EMPRESA_NOME_ENTIDADE);
		cbEmpresa = new EmpresaComboBox(lbEmp.getValue());
		lbRep = new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE);
		cbRep = new RepresentanteComboBox();
		lvRep = new LabelValue();
		lvEmp = new LabelValue();
		btOK = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		ckModoFeira = new CheckBoolean(Messages.LABEL_MSG_MODOFEIRA);
		//--
		makeUnmovable();
		int height = UiUtil.getControlPreferredHeight() * (LavenderePdaConfig.usaEscolhaModoFeira ? 8 : 7);
		setRect(CENTER, CENTER, UiUtil.getScreenWidthOrigin() - WIDTH_GAP * 4, height);
		//--
		loadUsuario();
	}

	@Override
	protected void addBtFechar() {
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbEmp, getLeft(), getTop() + (UiUtil.getControlPreferredHeight() / 2) - HEIGHT_GAP);
		UiUtil.add(this, cbEmpresa, getLeft(), AFTER, FILL - WIDTH_GAP_BIG);
		UiUtil.add(this, lvEmp, SAME, SAME, FILL - WIDTH_GAP_BIG, cbEmpresa.getHeight());
		//--
		UiUtil.add(this, lbRep,  getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, cbRep, getLeft(), AFTER, FILL - WIDTH_GAP_BIG);
		UiUtil.add(this, lvRep, SAME, SAME, FILL - WIDTH_GAP_BIG, cbRep.getHeight());
		if (LavenderePdaConfig.usaEscolhaModoFeira) {
			UiUtil.add(this, ckModoFeira, SAME, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight());
		}
		//--
		addButtonPopup(btOK);
		addButtonPopup(btCancelar);
	}

	public void loadUsuario() throws SQLException {
		cbEmpresa.loadEmpresa(SessionLavenderePda.cdEmpresa);
		cbRep.carregaRepresentantes(cbEmpresa.getValue());
		ckModoFeira.setChecked(SessionLavenderePda.isModoFeira);
		//--
		visibilidadeCombo();
	}

	private void visibilidadeCombo() {
		if (cbRep.size() == 1) {
			cbRep.setSelectedIndex(0);
			cbRep.setVisible(false);
			lvRep.setVisible(true);
			lvRep.setValue(((Representante)cbRep.getSelectedItem()).toString());
		} else {
			lvRep.setVisible(false);
			cbRep.setVisible(true);
		}
		//--
		if (cbEmpresa.size() == 1) {
			cbEmpresa.setSelectedIndex(0);
			cbEmpresa.setVisible(false);
			lvEmp.setVisible(true);
			lvEmp.setValue(((Empresa)cbEmpresa.getSelectedItem()).toString());
		} else {
			lvEmp.setVisible(false);
			cbEmpresa.setVisible(true);
		}
	}

	@Override
	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btOK) {
					result = LOGIN_RESULT_OK;
					//--
					cdEmpresa = cbEmpresa.getValue();
					cdRepresentante = cbRep.getValue();
					modoFeira = ckModoFeira.isChecked();
					//--
					fecharWindow();
				} else if (event.target == btCancelar) {
					result = LOGIN_RESULT_CANCEL;
					fecharWindow();
				} else if (event.target == cbEmpresa) {
					cbRep.carregaRepresentantes(cbEmpresa.getValue());
					visibilidadeCombo();
				}
				break;
			}
		}
		} catch (Throwable ex) {ex.printStackTrace();}
	}

}