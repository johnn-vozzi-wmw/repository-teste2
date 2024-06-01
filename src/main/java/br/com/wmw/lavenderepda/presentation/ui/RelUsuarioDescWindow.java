package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioDesc;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelUsuarioDescWindow extends WmwWindow {
	
	private LabelValue lvTotalFaturado;
	private LabelValue lvTotalDesconto;
	private LabelValue lvPctMaxDesconto;
	private LabelValue lvPctMaxDescPonderado;
	private LabelValue lvPctMedioDescontoPonderado;
	private ButtonPopup btRecalcular;
	
	public RelUsuarioDescWindow() throws SQLException {
		super(Messages.USUARIODESC_NOME_ENTIDADE);
		lvTotalFaturado = new LabelValue();
		lvTotalDesconto = new LabelValue();
		lvPctMaxDesconto = new LabelValue();
		lvPctMaxDescPonderado = new LabelValue();
		lvPctMedioDescontoPonderado = new LabelValue();
		btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
		loadUsuarioDescList();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.USUARIODESC_LABEL_VLTOTALFATURADO), lvTotalFaturado, getLeft(), TOP + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.USUARIODESC_LABEL_VLTOTALDESCONTO), lvTotalDesconto, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.USUARIODESC_LABEL_VLPCTMEDIODESCPONDERADO), lvPctMedioDescontoPonderado, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.USUARIODESC_LABEL_VLPCTMAXDESCONTO), lvPctMaxDesconto, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.USUARIODESC_LABEL_VLPCTMAXDESCPONDERADO), lvPctMaxDescPonderado, getLeft(), AFTER + HEIGHT_GAP);
		addButtonPopup(btRecalcular);
		addButtonPopup(btFechar);
	}
	
	private void loadUsuarioDescList() throws SQLException {
		double vlTotalFaturado = 0;
		double vlTotalDesconto = 0;
		UsuarioDesc usuarioDescFilter = new UsuarioDesc();
    	usuarioDescFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	Vector usuarioDescList = UsuarioDescService.getInstance().findAllByExample(usuarioDescFilter);
    	if (ValueUtil.isNotEmpty(usuarioDescList)) {
    		for (int i = 0; i < usuarioDescList.size(); i++) {
    			UsuarioDesc usuarioDesc = (UsuarioDesc) usuarioDescList.items[i];
				vlTotalFaturado += usuarioDesc.vlTotalFaturado;
				vlTotalDesconto += usuarioDesc.vlTotalDesconto;
				if (usuarioDesc.isOrigemDescontoErp()) {
					lvPctMaxDesconto.setValue(usuarioDesc.vlPctMaxDesconto);
					lvPctMaxDescPonderado.setValue(usuarioDesc.vlPctMaxDescPonderado);
				}
			}
    		lvTotalFaturado.setValue(vlTotalFaturado);
    		lvTotalDesconto.setValue(vlTotalDesconto);
    		double vlPctMedioDescontoPonderado = 0;
    		if (vlTotalFaturado > 0) {
    			vlPctMedioDescontoPonderado = ValueUtil.round((vlTotalDesconto / vlTotalFaturado) * 100);
    		}
    		lvPctMedioDescontoPonderado.setValue(vlPctMedioDescontoPonderado);
    	}
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecalcular) {
					UsuarioDescService.getInstance().recalculaAndUpdateUsuarioDescPda();
					loadUsuarioDescList();
					UiUtil.showSucessMessage(Messages.USUARIODESC_MSG_DADOS_RECALCULADOS_SUCESSO);
				}
				break;
			}
		}
	}

}
