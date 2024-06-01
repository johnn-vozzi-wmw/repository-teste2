package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.ui.Container;
import totalcross.ui.event.Event;


public class FaixaProgressItem extends Container {

	public static final String SEPARADORTOOLTIP = "§";

	private final int qtTotal;
	private final int qtAtual;
	private final int qtExtra;
	private final HashMap<Integer, String> hashMapItens;
	private final int itensWidth;
	private final int corAtual;
	private final int corExtra;
	private final String labelTipItens;

	public FaixaProgressItem(int qtTotal, int qtAtual, int qtExtra, HashMap<Integer, String> hashMapItens, String labelTipItens, int itensWidth, int corAtual, int corExtra) {
		this.qtTotal = qtTotal;
		this.qtAtual = qtAtual;
		this.qtExtra = qtExtra;
		this.hashMapItens = hashMapItens;
		this.itensWidth = itensWidth;
		this.corAtual = corAtual;
		this.corExtra = corExtra;
		this.labelTipItens = labelTipItens;
	}

	public void initUI() {
		int widthTotal = ((this.getWidth() - BaseContainer.WIDTH_GAP) / qtTotal) - BaseContainer.WIDTH_GAP;
		HashMap<Integer, String> listPosValue = new HashMap<>();
		int qtAtual = this.qtAtual;
		int qtExtra = this.qtExtra;
		for (int i = 0; i < qtTotal; i++) {
			String valorMapOld = hashMapItens.get(i);
			Container containerFam = new Container();
			containerFam.borderColor = ColorUtil.componentsBorderColor;
			containerFam.setBorderStyle(Container.BORDER_ROUNDED);
			if (qtAtual > 0) {
				containerFam.setBackColor(corAtual);
				qtAtual--;
			} else if (qtExtra > 0) {
				containerFam.setBackColor(corExtra);
				qtExtra--;
			}

			int after = i == 0 ? LEFT : AFTER;
			int gap = valorMapOld != null ? after + itensWidth : after + BaseContainer.WIDTH_GAP;
			if (ValueUtil.isNotEmpty(labelTipItens)) {
				new BaseToolTip(containerFam, MessageUtil.getMessage(labelTipItens, new String[]{StringUtil.getStringValueToInterface(i + 1), StringUtil.getStringValueToInterface(qtTotal)}));
			}
			UiUtil.add(this, containerFam, gap, TOP, widthTotal, ValueUtil.getIntegerValue(UiUtil.getControlPreferredHeight() * 0.8));

			String valorMap = hashMapItens.get(i + 1);
			if (valorMap != null) {
				Container barra = new Container();
				barra.setBackColor(ColorUtil.labelNameForeColor);
				String[] valor = valorMap.split(SEPARADORTOOLTIP);
				new BaseToolTip(barra, valor[1]);
				UiUtil.add(this, barra, AFTER + itensWidth, TOP, itensWidth, ValueUtil.getIntegerValue(UiUtil.getControlPreferredHeight() * 0.9));
				listPosValue.put(barra.getX(), valorMap);

			}
		}

		int max = listPosValue.size();
		int atual = 0;
		Map<Integer, String> treeMap = new TreeMap<>(listPosValue);
		for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
			String value = entry.getValue();
			Integer x = entry.getKey();
			String[] valor = value.split(SEPARADORTOOLTIP);
			LabelName lbVl = new LabelName(valor[0]);
			new BaseToolTip(lbVl, valor[1]);
			lbVl.setFont(lbVl.getFont().asBold());
			int width = fm.stringWidth(valor[0]);
			int y = atual == 0 ? AFTER : SAME;
			if (atual == max-1) {
				UiUtil.add(this, lbVl, (this.getX2() - width), y, width, fm.height);
			} else {
				UiUtil.add(this, lbVl, x, y, width, fm.height);
			}
			atual++;
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		this.removeAll();
		this.initUI();
	}

	public void onEvent(Event event) {}

	public static String montaStringHash(String valor, String toolTip) {
		return valor + SEPARADORTOOLTIP + toolTip;
	}

}