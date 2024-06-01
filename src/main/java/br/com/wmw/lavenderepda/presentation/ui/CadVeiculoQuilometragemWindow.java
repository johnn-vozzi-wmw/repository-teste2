package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadVeiculoQuilometragemWindow extends WmwWindow {

	private final boolean readOnly;
	private final EditText edDsPlacaVeiculo;
	private final EditNumberFrac edKmInicialVeiculo;
	private final EditNumberFrac edKmFinalVeiculo;

	private final ButtonPopup btOk;

	public FechamentoDiario fechamentoDiario;

	public CadVeiculoQuilometragemWindow(boolean readOnly, FechamentoDiario fechamentoDiario) {
		super(Messages.BOTAO_VEICULO_FECHAMENTO_DIARIO);
		this.readOnly = readOnly;
		this.fechamentoDiario = fechamentoDiario;
		if (this.fechamentoDiario == null) {
			this.fechamentoDiario = new FechamentoDiario();
		}
		edDsPlacaVeiculo = new EditText("@@@@@@@@", 8);
		edDsPlacaVeiculo.setEditable(!readOnly);
		edKmInicialVeiculo = new EditNumberFrac("9999999999", 16);
		edKmInicialVeiculo.setEditable(!readOnly);
		edKmFinalVeiculo = new EditNumberFrac("9999999999", 16);
		edKmFinalVeiculo.setEditable(!readOnly);
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		int windowHeight = titleFont.fm.height + footerH + (HEIGHT_GAP_BIG * 3) + (UiUtil.getControlPreferredHeight() * 5) + (UiUtil.getLabelPreferredHeight() * 3);
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
	}

	@Override
	public void initUI() {
		super.initUI();
		if (readOnly) {
			addButtonPopup(btFechar);
		} else {
			addButtonPopup(btOk);
		}
		UiUtil.add(this, new LabelName(Messages.VEICULO_QUILOMETRAGEM_PLACA), edDsPlacaVeiculo, getLeft(), getNextY(), fm.stringWidth("@@@@@@@@"));
		UiUtil.add(this, new LabelName(Messages.VEICULO_QUILOMETRAGEM_KM_INICIAL), edKmInicialVeiculo, getLeft(), getNextY(), getWFill());
		UiUtil.add(this, new LabelName(Messages.VEICULO_QUILOMETRAGEM_KM_FINAL), edKmFinalVeiculo, getLeft(), getNextY(), getWFill());
		setValues();
	}

	private void setValues() {
		if (fechamentoDiario != null) {
			edDsPlacaVeiculo.setValue(fechamentoDiario.dsPlacaVeiculo);
			if (fechamentoDiario.kmInicialVeiculo != 0) {
				edKmInicialVeiculo.setValue(fechamentoDiario.kmInicialVeiculo);
			}
			if (fechamentoDiario.kmFinalVeiculo != 0) {
				edKmFinalVeiculo.setValue(fechamentoDiario.kmFinalVeiculo);
			}
		}
	}

	@Override
	protected void addBtFechar() {

	}

	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		try {
			if (event.type == ControlEvent.PRESSED) {
				if (event.target == btFechar) {
					fecharWindow();
				} else if (event.target == btOk) {
					FechamentoDiarioService.getInstance().validaQuilometragemVeiculo(edKmInicialVeiculo.getValueDouble(), edKmFinalVeiculo.getValueDouble());
					fechamentoDiario.dsPlacaVeiculo = edDsPlacaVeiculo.getValue();
					fechamentoDiario.kmInicialVeiculo = edKmInicialVeiculo.getValueDouble();
					fechamentoDiario.kmFinalVeiculo = edKmFinalVeiculo.getValueDouble();
					fecharWindow();
				}
			}
		} catch (ValidationException e) {
			UiUtil.showErrorMessage(e);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
}
