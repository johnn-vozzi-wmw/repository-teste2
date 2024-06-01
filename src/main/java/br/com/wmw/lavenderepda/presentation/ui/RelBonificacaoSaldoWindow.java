package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonificacaoSaldo;
import br.com.wmw.lavenderepda.business.service.BonificacaoSaldoService;
import totalcross.ui.Button;
import totalcross.util.Vector;

public class RelBonificacaoSaldoWindow extends WmwWindow {

	private LabelName lbSaldoOriginal;
	private LabelName lbSaldoConsumido;
	private LabelName lbSaldoDisponivel;
	private LabelValue lvSaldoOriginal;
	private LabelValue lvSaldoConsumido;
	private LabelValue lvSaldoDisponivel;

	public RelBonificacaoSaldoWindow() throws SQLException {
		super(Messages.SALDOBONIFICACAO_LABEL);
		lbSaldoOriginal = new LabelName(Messages.SALDOBONIFICACAO_SALDO_ORIGINAL);
		lbSaldoConsumido = new LabelName(Messages.SALDOBONIFICACAO_SALDO_CONSUMIDO);
		lbSaldoDisponivel = new LabelName(Messages.SALDOBONIFICACAO_SALDO_DISPONIVEL);
		lvSaldoOriginal = new LabelValue();
		lvSaldoOriginal.useCurrencyValue = true;
		lvSaldoConsumido = new LabelValue();
		lvSaldoConsumido.useCurrencyValue = true;
		lvSaldoDisponivel = new LabelValue();
		lvSaldoDisponivel.useCurrencyValue = true;
		loadSaldo();
		setDefaultRect();
	}
	
	private void loadSaldo() throws SQLException {
		Vector bonificacaoSaldoList = BonificacaoSaldoService.getInstance().getBonificacaoSaldoRepList();
		if (ValueUtil.isNotEmpty(bonificacaoSaldoList)) {
			BonificacaoSaldo bonificacaoSaldoErp = null;
			BonificacaoSaldo bonificacaoSaldoPda = null;
			BonificacaoSaldo bonificacaoSaldo;
			int size = bonificacaoSaldoList.size();
			for (int i = 0; i < size; i++) {
				bonificacaoSaldo = (BonificacaoSaldo) bonificacaoSaldoList.items[i];
				if (BonificacaoSaldo.FL_ORIGEM_PDA.equals(bonificacaoSaldo.flOrigemSaldo)) {
					bonificacaoSaldoPda = bonificacaoSaldo;
				} else {
					bonificacaoSaldoErp = bonificacaoSaldo;
				}
			}
			double vlSaldoErp = 0d;
			double vlSaldoPda = 0d;
			double vlSaldoDisponivel = 0d;
			if (bonificacaoSaldoErp != null) {
				vlSaldoErp = bonificacaoSaldoErp.vlSaldo;
				lvSaldoOriginal.setValue(StringUtil.getStringValueToInterface(vlSaldoErp));
			}
			if (bonificacaoSaldoPda != null) {
				vlSaldoPda = bonificacaoSaldoPda.vlSaldo;
				lvSaldoConsumido.setValue(StringUtil.getStringValueToInterface(vlSaldoPda));
			}
			vlSaldoDisponivel = vlSaldoErp + vlSaldoPda;
			lvSaldoDisponivel.setValue(StringUtil.getStringValueToInterface(vlSaldoDisponivel));
		}
	}

	@Override
	public void initUI() {
		super.initUI();
		Button sep2 = new Button("");
		sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(this, lbSaldoOriginal, lvSaldoOriginal, getLeft(), getNextY());
		UiUtil.add(this, lbSaldoConsumido, lvSaldoConsumido, getLeft(), getNextY());
		int oneChar = fm.charWidth('A');
		UiUtil.add(this, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
		UiUtil.add(this, lbSaldoDisponivel, lvSaldoDisponivel, getLeft(), getNextY());
		lvSaldoDisponivel.setForeColor(ValueUtil.getDoubleValue(lvSaldoDisponivel.getValue()) > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
	}

}
