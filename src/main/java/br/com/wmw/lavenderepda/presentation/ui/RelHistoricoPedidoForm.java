package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.HistoricoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.service.HistoricoPedidoService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import totalcross.ui.ImageControl;
import totalcross.ui.ScrollContainer;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class RelHistoricoPedidoForm extends BaseUIForm {

	private Pedido pedido;
	private ScrollContainer scrollSession;
	private SessionContainer sessionStatus;
	private SessionContainer sessionStatusAtual;
	private ImageControl imageControl;
	private ImageControl imageCircle;
	private LabelValue lvStatus;
	private LabelValue lvData;
	private LabelValue lvHora;
	private LabelValue lvNuOrdem;
	private Hashtable historicoPedHash;
	private Vector statusPedidoPdaList;
	private boolean lastHistoricoAdded;
	private boolean fistContainerAdded;

	public RelHistoricoPedidoForm(Pedido pedido) throws SQLException {
		super(Messages.STATUS_WORKFLOW);
		this.pedido = pedido;
		sessionStatus = new SessionContainer();
		sessionStatus.setBackColor(ColorUtil.baseBackColorSystem);
		sessionStatusAtual = new SessionContainer();
		sessionStatusAtual.setBackColor(ColorUtil.baseBackColorSystem);
		scrollSession = new BaseScrollContainer(false, true);
		scrollSession.setBackColor(ColorUtil.baseBackColorSystem);
		imageControl = new ImageControl();
		imageCircle = new ImageControl(UiUtil.getImageAndApplyColor("images/circle.png", ColorUtil.baseForeColorSystem, UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		lvStatus = new LabelValue(" ");
		lvData = new LabelValue(" ");
		lvHora = new LabelValue(" ");
		lvNuOrdem = new LabelValue(" ");
		loadHistoricoPedido();
		loadStatusPedidoPdaWorkflow();
	}

	
	public void loadHistoricoPedido() throws SQLException {
		historicoPedHash = new Hashtable("");
		HistoricoPedido historicoPedidoFilter = new  HistoricoPedido();
		historicoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		historicoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		historicoPedidoFilter.nuPedido = pedido.nuPedido;
		historicoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		Vector historicoPedidoList = HistoricoPedidoService.getInstance().findAllByExample(historicoPedidoFilter);
		int size = historicoPedidoList.size();
		for (int i = 0; i < size; i++) {
			HistoricoPedido historicoPedido = (HistoricoPedido) historicoPedidoList.items[i];
			if (i == size - 1) {
				historicoPedido.ultimoHistorico = true;
			}
			historicoPedHash.put(historicoPedido.cdStatus, historicoPedido);
		}
	}
		
	public void loadStatusPedidoPdaWorkflow() throws SQLException {
		StatusPedidoPda statusPedidoPdaFilter = new StatusPedidoPda();
		statusPedidoPdaFilter.flUsaWorkflow = ValueUtil.VALOR_SIM;
		statusPedidoPdaFilter.sortAsc = ValueUtil.VALOR_SIM;
		statusPedidoPdaFilter.sortAtributte = "nuOrdemWorkflow";
		statusPedidoPdaList = StatusPedidoPdaService.getInstance().findAllByExample(statusPedidoPdaFilter);
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		int size = statusPedidoPdaList.size();
		int count = 1;
		for (int i = 0; i < size; i++) {
			StatusPedidoPda statusPedidoPda = (StatusPedidoPda) statusPedidoPdaList.items[i];
			HistoricoPedido historicoPedido = (HistoricoPedido) historicoPedHash.get(statusPedidoPda.cdStatusPedido);
			//--
			lvStatus.setValue(StringUtil.getStringAbreviada(StringUtil.getStringValue(statusPedidoPda.dsStatusPedido),  width / 2 + width / 4));
			lvNuOrdem.setFont(UiUtil.defaultFontSmall);
			imageControl = new ImageControl(getImage(statusPedidoPda, historicoPedido));
			if (historicoPedido != null) {
				lvData.setValue(historicoPedido.dtAtualizacao);
				lvHora.setValue(historicoPedido.hrAtualizacao);
				lvData.setFont(UiUtil.defaultFontSmall);
				lvHora.setFont(UiUtil.defaultFontSmall);
				lvData.setForeColor(ColorUtil.baseForeColorSystem);
				lvHora.setForeColor(ColorUtil.baseForeColorSystem);
				lvStatus.setForeColor(ColorUtil.baseForeColorSystem);
				lvNuOrdem.setForeColor(ColorUtil.baseForeColorSystem);
			} else {
				lvData.setForeColor(ColorUtil.labelNameForeColor);
				lvHora.setForeColor(ColorUtil.labelNameForeColor);
				lvStatus.setForeColor(Color.darker(ColorUtil.labelNameForeColor, 20));
				lvNuOrdem.setForeColor(Color.darker(ColorUtil.labelNameForeColor, 20));
			}
			if (!fistContainerAdded && (historicoPedido != null || historicoPedHash.size() == 0)) {
				UiUtil.add(this, scrollSession, getLeft(), TOP + barTopContainer.getHeight(), FILL - UiUtil.BASE_MARGIN_GAP, FILL - barBottomContainer.getHeight());
				UiUtil.add(scrollSession, sessionStatus, getLeft(), TOP + HEIGHT_GAP_BIG, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() + UiUtil.getLabelPreferredHeight());
				lvNuOrdem.setValue(count++);
				addSessionStatusComponents(historicoPedido);
				fistContainerAdded = true;
			} else if (historicoPedHash.size() == 0 || lastHistoricoAdded || (!lastHistoricoAdded && historicoPedido != null)) {
				lvNuOrdem.setValue(count++);
				UiUtil.add(scrollSession, sessionStatus, getLeft(), AFTER + HEIGHT_GAP_BIG, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() + UiUtil.getLabelPreferredHeight());
				addSessionStatusComponents(historicoPedido);
			}
			//--
			clearDados();
		}
		lastHistoricoAdded = false;
		fistContainerAdded = false;
	}


	private void addSessionStatusComponents(HistoricoPedido historicoPedido) {
		UiUtil.add(sessionStatus, imageControl, CENTER, TOP + HEIGHT_GAP);
		UiUtil.add(sessionStatus, lvStatus, CENTER, AFTER);
		UiUtil.add(sessionStatus, lvData,  RIGHT, TOP, PREFERRED, PREFERRED);
		UiUtil.add(sessionStatus, lvHora,  RIGHT, AFTER, PREFERRED, PREFERRED);
		if (historicoPedido != null && historicoPedido.ultimoHistorico) {
			lastHistoricoAdded = true;
			lvNuOrdem.setForeColor(ColorUtil.componentsBackColor);
			UiUtil.add(sessionStatus, sessionStatusAtual,  LEFT, TOP, UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight());
			UiUtil.add(sessionStatusAtual, imageCircle,  CENTER, CENTER);
			UiUtil.add(sessionStatusAtual, lvNuOrdem,  CENTER, CENTER, PREFERRED, PREFERRED);
		} else {
			UiUtil.add(sessionStatus, lvNuOrdem,  getLeft(), TOP, PREFERRED, PREFERRED);
		}
	}
	

	private Image getImage(StatusPedidoPda statusPedidoPda, HistoricoPedido historicoPedido) {
		int width = (int) (UiUtil.getControlPreferredHeight() * 0.9);
		int height = width; 
		try {
			return UiUtil.getImageAndApplyColor(statusPedidoPda.imIconeWorkflow, historicoPedido == null ? Color.darker(ColorUtil.labelNameForeColor, 20) : ColorUtil.baseForeColorSystem, width, height);
		} catch (Throwable e) {
			return UiUtil.getImageAndApplyColor("images/menuIconDefault.png", historicoPedido == null ? Color.darker(ColorUtil.labelNameForeColor, 20) : ColorUtil.baseForeColorSystem, width, height);
		}
	}

	public void clearDados() {
		lvStatus = new LabelValue(" ");
		lvData = new LabelValue(" ");
		lvHora = new LabelValue(" ");
		lvNuOrdem = new LabelValue(" ");
		sessionStatus = new SessionContainer();
		sessionStatus.setBackColor(ColorUtil.baseBackColorSystem);
	}
	
	
	@Override
	protected void onFormEvent(Event arg0) throws SQLException {
	}
}
