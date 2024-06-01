package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.util.Vector;

public class RelDetalhesCalculosPedidoWindow extends WmwWindow {
	
	private Pedido pedido;
	private ScrollTabbedContainer scrollTabbedContainer;
	private SessionContainer formulaIndiceRentPedidoContainer;
	private SessionContainer formulaMargemPercPedidoContainer;
	private LabelValue lbFormulaIndiceRentPedido;
	private LabelValue lbFormulaMargemPercPedido;
	private LabelName lbMargemPercentual;
	private LabelValue lvMargemPercentual;
	private LabelName lbMargemMinima;
	private LabelValue lvMargemMinima;
	private LabelName lbMargemEspecificaPonderada;
	private LabelValue lvMargemEspecificaPonderada;
	private LabelName lbVlIndiceRentPedido;
	private LabelValue lvVlIndiceRentPedido;
	private LabelName lbMargemValor;
	private LabelValue lvMargemValor;
	private LabelName lbReceitaVirtual;
	private LabelValue lvReceitaVirtual;
	private LabelName lbVlMargemPercentual;
	private LabelValue lvVlMargemPercentual;
	
	private int indexTab;
	
	public RelDetalhesCalculosPedidoWindow(Pedido pedido) {
		super(Messages.MENU_OPCAO_DETALHES_CALCULOS);
		this.pedido = pedido;
		Vector tabsCaptions = configuraTabsCaptions();
		scrollTabbedContainer = new ScrollTabbedContainer((String[])tabsCaptions.toObjectArray());
		formulaIndiceRentPedidoContainer = new SessionContainer();
		formulaMargemPercPedidoContainer = new SessionContainer();
		lbFormulaIndiceRentPedido = new LabelValue();
		lbFormulaIndiceRentPedido.setForeColor(ColorUtil.sessionContainerForeColor);
		lbFormulaMargemPercPedido = new LabelValue();
		lbFormulaMargemPercPedido.setForeColor(ColorUtil.sessionContainerForeColor);
		lbMargemPercentual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_PERCENTUAL);
		lvMargemPercentual = new LabelValue();
		lbMargemMinima = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_MINIMA);
		lvMargemMinima = new LabelValue();
		lbMargemEspecificaPonderada = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA_PONDERADA);
		lvMargemEspecificaPonderada = new LabelValue();
		lbVlIndiceRentPedido = new LabelName(Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_PEDIDO);
		lvVlIndiceRentPedido = new LabelValue();
		lbMargemValor = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_VALOR);
		lvMargemValor = new LabelValue();
		lbReceitaVirtual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_VIRTUAL);
		lvReceitaVirtual = new LabelValue();
		lbVlMargemPercentual = new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_PERCENTUAL);
		lvVlMargemPercentual = new LabelValue();
		
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, scrollTabbedContainer, getTop() + HEIGHT_GAP, footerH);
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			Container tabIndiceRentPedido = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesIndiceRentPedido(tabIndiceRentPedido);
			indexTab++;
			Container tabMargemPercPedido = scrollTabbedContainer.getContainer(indexTab);
			adicionaComponentesMargemPercPedido(tabMargemPercPedido);
			indexTab++;
		}
		
	}
	
	@Override
	protected void onPopup() {
	   try {
		super.onPopup();
		loadValoresCalculoIndiceRentPedido();
		loadValoresCalculoMargemPercPedido();
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void adicionaComponentesIndiceRentPedido(Container tabIndiceRentPedido) {
		UiUtil.add(tabIndiceRentPedido, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabIndiceRentPedido, formulaIndiceRentPedidoContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaIndiceRentPedidoContainer, lbFormulaIndiceRentPedido, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaIndiceRentPedido.setMultipleLinesText(getFormulaIndiceRentPedido());
		formulaIndiceRentPedidoContainer.resizeHeight();
		formulaIndiceRentPedidoContainer.resetSetPositions();
		formulaIndiceRentPedidoContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaIndiceRentPedidoContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabIndiceRentPedido, lbMargemPercentual, lvMargemPercentual, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabIndiceRentPedido, lbMargemMinima, lvMargemMinima, SAME, AFTER + HEIGHT_GAP);
		UiUtil.add(tabIndiceRentPedido, lbMargemEspecificaPonderada, lvMargemEspecificaPonderada, SAME, AFTER + HEIGHT_GAP);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabIndiceRentPedido, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabIndiceRentPedido, lbVlIndiceRentPedido, lvVlIndiceRentPedido, SAME, AFTER + HEIGHT_GAP);
	}
	
	private void adicionaComponentesMargemPercPedido(Container tabMargemPercPedido) {
		UiUtil.add(tabMargemPercPedido, new LabelName(Messages.REL_DETALHES_CALCULOS_LABEL_FORMULA), getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(tabMargemPercPedido, formulaMargemPercPedidoContainer, SAME, AFTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		UiUtil.add(formulaMargemPercPedidoContainer, lbFormulaMargemPercPedido, getLeft(), CENTER, FILL - WIDTH_GAP_BIG, PREFERRED);
		lbFormulaMargemPercPedido.setMultipleLinesText(getFormulaMargemPercentualPedido());
		formulaMargemPercPedidoContainer.resizeHeight();
		formulaMargemPercPedidoContainer.resetSetPositions();
		formulaMargemPercPedidoContainer.setRect(KEEP, KEEP, FILL - WIDTH_GAP_BIG, formulaMargemPercPedidoContainer.getHeight() + BaseContainer.HEIGHT_GAP);
		UiUtil.add(tabMargemPercPedido, lbMargemValor, lvMargemValor, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabMargemPercPedido, lbReceitaVirtual, lvReceitaVirtual, getLeft(), AFTER + HEIGHT_GAP_BIG);
		Button sep1 = new Button("");
		sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
		UiUtil.add(tabMargemPercPedido, sep1, SAME, AFTER + HEIGHT_GAP , FILL - (getWidth() / 2), 1);
		UiUtil.add(tabMargemPercPedido, lbVlMargemPercentual, lvVlMargemPercentual, SAME, AFTER + HEIGHT_GAP);
	}
	
	private Vector configuraTabsCaptions() {
		Vector tabsCaptions = new Vector();
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_PEDIDO);
			tabsCaptions.addElement(Messages.REL_DETALHES_CALCULOS_MARGEM_PERCENTUAL_PEDIDO);
		}
		return tabsCaptions;
	}
	
	private String getFormulaIndiceRentPedido() {
		String vlIndiceRentPedido = Messages.REL_DETALHES_CALCULOS_INDICE_RENTABILIDADE_PEDIDO;
		String margemPercentual = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_PERCENTUAL;
		String margemMinima = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_MINIMA;
		String margemEspecificaPonderada = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_ESPECIFICA_PONDERADA;
		return vlIndiceRentPedido + " = " + margemPercentual + " * " + margemMinima + " / " + margemEspecificaPonderada;
	}
	
	private String getFormulaMargemPercentualPedido() {
		String margemPercentualPedido = Messages.REL_DETALHES_CALCULOS_MARGEM_PERCENTUAL_PEDIDO;
		String margemValor = Messages.REL_DETALHES_CALCULOS_LABEL_MARGEM_VALOR;
		String receitaVirtual = Messages.REL_DETALHES_CALCULOS_LABEL_RECEITA_VIRTUAL;
		return margemPercentualPedido + " = " + margemValor + " / " + receitaVirtual;
	}
	
	private void loadValoresCalculoIndiceRentPedido() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			PedidoService.getInstance().calculaIndiceRentabilidadePedidoSemTributos(pedido);
			lvMargemPercentual.setValue(pedido.vlMargemPercentual);
			lvMargemMinima.setValue((double) LavenderePdaConfig.indiceMinimoRentabilidadePedido);
			lvMargemEspecificaPonderada.setValue(pedido.vlMargemEspecificaPonderada);
			lvVlIndiceRentPedido.setValue(pedido.vlRentabilidade);
		}
	}
	
	private void loadValoresCalculoMargemPercPedido() throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			PedidoService.getInstance().calculaIndiceRentabilidadePedidoSemTributos(pedido);
			lvMargemValor.setValue(pedido.vlMargemValor);
			lvReceitaVirtual.setValue(pedido.vlReceitaVirtual);
			lvVlMargemPercentual.setValue(pedido.vlMargemPercentual);
		}
	}

}
