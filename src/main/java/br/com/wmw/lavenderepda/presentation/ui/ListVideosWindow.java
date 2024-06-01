package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.VideoProduto;
import br.com.wmw.lavenderepda.business.domain.VideoProdutoGrade;
import totalcross.io.File;
import totalcross.sys.Convert;
import totalcross.ui.Container;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListVideosWindow extends WmwListWindow {

	private final Vector videoProdutoList;
	private final Vector videoProdutoGradeList;

	public ListVideosWindow(Vector videoProdutoList, Vector videoProdutoGradeList) {
		super(Messages.VIDEOS_LISTA);
		this.videoProdutoList = videoProdutoList;
		this.videoProdutoGradeList = videoProdutoGradeList;
		singleClickOn = true;
		listContainer = new GridListContainer(1, 1);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return null;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		return new String[0];
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return null;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException { /*nao sobrescreve eventos*/ }

	@Override
	public void list() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			listContainer.removeAllContainers();
			//---
			int listSizeVideoProdutoList = videoProdutoList.size();
			int listSizeVideoProdutoGradeList = videoProdutoGradeList.size();
			Container[] all = new Container[listSizeVideoProdutoList + listSizeVideoProdutoGradeList];
			//--
			BaseListContainer.Item c;
			for (int i = 0; i < listSizeVideoProdutoList; i++) {
				all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
				VideoProduto domain = (VideoProduto) videoProdutoList.items[i];
				c.id = domain.nmVideo;
				c.setID(c.id);
				c.setItens(getItemVideoProduto(domain));
				c.domain = domain;
				c.setToolTip(getToolTip(domain));
				c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend, useLeftTopIcons);
				setPropertiesInRowList(c, domain);
			}
			for (int i = 0; i < listSizeVideoProdutoGradeList; i++) {
				all[i + listSizeVideoProdutoList] = c = new BaseListContainer.Item(listContainer.getLayout());
				VideoProdutoGrade domain = (VideoProdutoGrade) videoProdutoGradeList.items[i];
				c.id = domain.nmVideo;
				c.setID(c.id);
				c.setItens(getItemVideoProdutoGrade(domain));
				c.domain = domain;
				c.setToolTip(getToolTip(domain));
				c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend, useLeftTopIcons);
				setPropertiesInRowList(c, domain);
			}
			listContainer.addContainers(all);
		} finally {
			msg.unpop();
		}
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return StringUtil.getStringValue(domain.toString());
	}

	private String[] getItemVideoProdutoGrade(BaseDomain domain) {
		VideoProdutoGrade videoProdutoGrade = (VideoProdutoGrade) domain;
		return new String[] { StringUtil.getStringValue(videoProdutoGrade.nmVideo) };
	}

	private String[] getItemVideoProduto(BaseDomain domain) {
		VideoProduto videoProduto = (VideoProduto) domain;
		return new String[] { StringUtil.getStringValue(videoProduto.nmVideo) };
	}

	@Override
	public void detalhesClick() throws SQLException {
		String pathVideo;
		if (getSelectedDomain() instanceof VideoProdutoGrade) {
			pathVideo = VideoProdutoGrade.getPathVideo();
		} else {
			pathVideo = VideoProduto.getPathVideo();
		}
		pathVideo = Convert.appendPath(Convert.appendPath(pathVideo, SessionLavenderePda.cdEmpresa), getSelectedRowKey());
		String path = VmUtil.isSimulador() ? "file:///" + pathVideo : pathVideo;
		try (File videoFile = new File(pathVideo)) {
			if (videoFile.exists()) {
				UiUtil.videoViewer(path);
			} else {
				UiUtil.showWarnMessage(Messages.VIDEO_NAO_ENCONTRADO);
			}
		} catch (Throwable e) {
			UiUtil.showWarnMessage(Messages.VIDEO_NAO_ENCONTRADO);
		}
	}

	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.domain;
	}

}
