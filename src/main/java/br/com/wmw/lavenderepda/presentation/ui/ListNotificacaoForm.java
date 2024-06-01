package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import br.com.wmw.lavenderepda.business.service.ConfigNotificacaoService;
import br.com.wmw.lavenderepda.business.service.NotificacaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusNotificacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.MainWindow;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListNotificacaoForm extends LavendereCrudListForm {

    private StatusNotificacaoComboBox cbStatusNotificacao;
    private ButtonAction btLerSelecionados;
	private Image imageLido;
	private Image naoImageLido;

	public ListNotificacaoForm() throws SQLException {
		super(Messages.NOTIFICACAO_TITULO_LISTA);
		setBaseCrudCadForm(new CadNotificacaoForm());
		btLerSelecionados = new ButtonAction(Messages.NOTIFICACAO_SISTEMA_LER, "images/recadolido.png");
		imageLido = UiUtil.getImageAndApplyColor("images/recadolido.png", ColorUtil.baseForeColorSystem);
		naoImageLido = UiUtil.getImageAndApplyColor("images/recadonaolido.png", ColorUtil.baseForeColorSystem);
		cbStatusNotificacao = new StatusNotificacaoComboBox();
		cbStatusNotificacao.addPressListener(controlEvent -> {
			try {
				setLeituraLoteVisible();
				list();
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		});
		singleClickOn = true;
		constructorListContainer();
	}

	private void setLeituraLoteVisible() {
		boolean showCheck = StatusNotificacaoComboBox.NAO_LIDO.equals(cbStatusNotificacao.getValue());
		listContainer.setCheckable(showCheck);
		btLerSelecionados.setVisible(showCheck);
	}

	public static void showNotificacaoIfHasNotificacaoNaoLidos() {
		try {
			if (ConfigNotificacaoService.getInstance().existeConfigNotificacao()) {
				Notificacao notificacao = new Notificacao();
				notificacao.flLido = ValueUtil.VALOR_NAO;
				int count = NotificacaoService.getInstance().countByExample(notificacao);
				MainWindow.getMainWindow().runOnMainThread(() -> {
					try {
						if (count > 0) {
							BaseUIForm.ativarNotificacao(new ListNotificacaoForm(), count);
						} else {
							BaseUIForm.desativarNotificacao();
						}
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				});
			} else {
				BaseUIForm.desativarNotificacao();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer(Notificacao.NMCOLUNA_CDINSTANTE, "DESC");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColsSort(new String[][] {
			{Messages.NOTIFICACAO_LABEL_ORDENACAO_DATA_HORA, Notificacao.NMCOLUNA_CDINSTANTE},
			{Messages.NOTIFICACAO_LABEL_DSTIPONOTIFICACAO, Notificacao.NMCOLUNA_DSTIPONOTIFICACAO}
		});
		listContainer.setCheckable(true);
		listContainer.showLeftImage(naoImageLido);
		listContainer.setColPosition(3, RIGHT);
		ScrollPosition.AUTO_HIDE = true;
    }

	@Override
    protected CrudService getCrudService() throws SQLException {
		return NotificacaoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		Notificacao domainFilter = new Notificacao();
		domainFilter.flLido = cbStatusNotificacao.getFlValue();
		return domainFilter;
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Notificacao notificacao = (Notificacao) domain;
		String[] item = {
			StringUtil.getStringValue(notificacao.dsMensagem),
			ValueUtil.VALOR_NI,
			StringUtil.getStringValue(notificacao.dsTipoNotificacao),
			ValueUtil.VALOR_NI
		};
		return item;
	}
	
	@Override
	public void loadDefaultFilters() throws SQLException {
		cbStatusNotificacao.setValue(StatusNotificacaoComboBox.NAO_LIDO);
		listContainer.setCheckable(true);
		btLerSelecionados.setVisible(true);
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(barBottomContainer, btLerSelecionados, 5);
		UiUtil.add(this, cbStatusNotificacao, getLeft(), getNextY() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btLerSelecionados) {
				lerSelecionados();
			}
			break;
		}
		}
	}

	private void lerSelecionados() throws SQLException {
		int[] checkedItens = listContainer.getCheckedItens();
		if (checkedItens.length <= 0) {
			UiUtil.showErrorMessage(Messages.NOTIFICACAO_SISTEMA_LER_NENHUM_SELECIONADO);
			return;
		}
		CrudDbxDao.getCurrentDriver().startTransaction();
		try {
			for (Integer i : checkedItens) {
				Notificacao notificacao = (Notificacao) ((Item) listContainer.getContainer(i)).domain;
				NotificacaoService.getInstance().marcarComoLido(notificacao);
			}
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
		list();
	}

	@Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		Notificacao notificacao = (Notificacao) domain;
		if (ValueUtil.VALOR_SIM.equals(notificacao.flLido)) {
			listContainer.setImageLeftItem(c, imageLido);
		} else {
			listContainer.setImageLeftItem(c, naoImageLido);
		}
	}
	
	@Override
	protected void afterList(Vector domainList) throws SQLException {
		super.afterList(domainList);
		showNotificacaoIfHasNotificacaoNaoLidos();
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		loadDefaultFilters();
		list();
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		list();
	}

	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getCrudService().findAllByExampleSummary(domain);
	}
}
