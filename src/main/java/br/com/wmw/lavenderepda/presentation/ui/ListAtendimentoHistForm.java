package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseLabel;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelHyperLink;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.sync.transport.http.HttpRequest;
import br.com.wmw.framework.sync.transport.http.HttpResponse;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.JsonFactory;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtendimentoHist;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.json.JSONArray;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.UnitsConverter;
import totalcross.util.Vector;

public class ListAtendimentoHistForm extends LavendereCrudListForm {

	private String cdEmpresa;
	private String cdCliente;

	public ListAtendimentoHistForm(String cdEmpresa, String cdCliente) {
		super(Messages.ATENDIMENTOHIST_TITLE);
		singleClickOn = true;
		this.cdEmpresa = cdEmpresa;
		this.cdCliente = cdCliente;
		constructorListContainer();
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(2, 2, false, true, true, false);
		listResizeable = false;
		listContainer.setBarTopSimple();
		listContainer.setColPosition(1, RIGHT);
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new AtendimentoHist();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		try {
			ParamsSync sync = HttpConnectionManager.getDefaultParamsSync();
			HttpRequest httpRequest = new HttpRequest(sync);
			try (HttpResponse response = httpRequest.get(sync.baseUrl.concat(sync.basePublicServiceUrl).concat("atendimento-hist/find-by-cliente/").concat(cdEmpresa).concat("/").concat(cdCliente))) {
				List<AtendimentoHist> parse = JsonFactory.asList(response.asString(), AtendimentoHist.class);
				return new Vector(parse.toArray());
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return new Vector();
	}

	@Override
	protected void onFormStart() throws SQLException {
    	UiUtil.add(this, listContainer, LEFT, TOP + barTopContainer.getHeight(), FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException { }
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		AtendimentoHist at = (AtendimentoHist) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(at.getDtAtendimento()));
		item.addElement(StringUtil.getStringValue(at.getHrAtendimento()));
		return (String[]) item.toObjectArray();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		BaseListContainer.Item selectedItem = (BaseListContainer.Item) listContainer.getSelectedItem();
		new DetalhesAtendimentoWindow((AtendimentoHist) selectedItem.domain).popup();
	}
	
	@Override
    protected BaseDomain getDomain(BaseDomain domain) {
    	return domain;
    }
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return ((AtendimentoHist) domain).getDsAtendimento();
	}
	
	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		AtendimentoHist at = (AtendimentoHist) domain;
		containerItem.addSublistItem(new String[] {StringUtil.getStringValue(at.getCdAtendimento())});
		containerItem.addSublistItem(new String[] {StringUtil.getStringValue(at.getDsServico())});
		containerItem.addSublistItem(new String[] {StringUtil.clearEnterException(at.getDsAtendimentoResumido()).replace("¢", " ")});
	}
	
	class DetalhesAtendimentoWindow extends WmwWindow {

		private static final String TEXT = "text";
		private static final String FILENAME = "filename";
		private static final String URL_FILE = "urlFile";
		private static final String DATA_MEDIA = "dataMedia";
		private static final String MESSAGES = "messages";
		private static final String TYPE = "type";
		private static final String IS_SENT_BY_ME = "isSentByMe";
		private static final String DH_MESSAGE = "dhMessage";
		private static final String SENDER_NAME = "senderName";
		private static final String TYPE_MESSAGE = "typeMessage";
		
		private AtendimentoHist atendimentoHist;
		private ScrollTabbedContainer tab;
		private JSONObject chat;
		private BaseScrollContainer chatContainer;
		
		public DetalhesAtendimentoWindow(AtendimentoHist a) {
			super(Messages.ATENDIMENTOHIST_ATENDIMENTO.concat(" ").concat(a.getCdAtendimento()));
			this.atendimentoHist = a;
			loadChat(a.getChatId());
			if (chat != null) {
				tab = new ScrollTabbedContainer(new String[] {Messages.ATENDIMENTOHIST_ATENDIMENTO, Messages.ATENDIMENTOHIST_MENSAGENS});
			} else {
				tab = new ScrollTabbedContainer(new String[] {Messages.ATENDIMENTOHIST_ATENDIMENTO});
			}
			setDefaultRect();
		}
		
		@Override
		public void initUI() {
			super.initUI();
			UiUtil.add(scBase, tab, LEFT, TOP, FILL, FILL);
			EditMemo memo = new EditMemo("@", 30, Integer.MAX_VALUE);
			memo.setFont(UiUtil.defaultFontSmall);
			memo.setEnabled(false);
			memo.setText(atendimentoHist.getDsAtendimento());
			UiUtil.add(tab.getContainer(0), memo, LEFT + HEIGHT_GAP_BIG, TOP + HEIGHT_GAP_BIG, FILL, FILL);
			if (chat != null) {
				montaChatContainer();
			}
		}

		private void montaChatContainer() {
			try {
				if (!(chat.opt(MESSAGES) instanceof JSONArray)) {
					return;
				}
				chatContainer = new BaseScrollContainer(true);
				UiUtil.add(tab.getContainer(1), chatContainer, LEFT, TOP + HEIGHT_GAP_BIG, FILL, FILL);
				
				JSONArray array = (JSONArray) chat.opt(MESSAGES);
				JSONObject message = null;
				int yPos = TOP + HEIGHT_GAP;
				
				for (int i = 0; i < array.length(); i++) {
					if (array.opt(i) instanceof JSONObject) {
						message = (JSONObject) array.opt(i);
						if (addMessage(message, yPos)) {
							yPos = AFTER + HEIGHT_GAP;
						}
					}
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		
		private boolean addMessage(JSONObject json, int yPos) {
			try {
				boolean sentByMe = json.optBoolean(IS_SENT_BY_ME);
				int xpos = sentByMe ? RIGHT - HEIGHT_GAP_BIG: LEFT + HEIGHT_GAP_BIG;
				int widthSize = UnitsConverter.toPixels((int) (Settings.screenWidth * 0.8));
				
				Container container = createNewMessagesContainer(sentByMe);
				LabelName timestamp = new LabelName(json.getString(SENDER_NAME).concat(": ").concat(json.getString(DH_MESSAGE)));
				timestamp.setFont(UiUtil.fontVerySmall);
				BaseLabel message = new LabelValue();
				message.setFont(UiUtil.defaultFontSmall);
				
				if (isMedia(json)) {
					JSONObject dataMedia = json.optJSONObject(DATA_MEDIA);
					String urlFile = dataMedia.optString(URL_FILE);
					if (urlFile != JSONObject.NULL) {
						message = new LabelHyperLink(dataMedia.optString(TYPE).concat(" - ").concat(dataMedia.optString(FILENAME)), urlFile);
						message.setFont(UiUtil.defaultFontSmall);
					}
				} else if (json.optInt(TYPE_MESSAGE) == 0) {
					message.setForeColor(Color.getRGB("ffffff"));
					String string = json.optString(TEXT);
					if (fm.stringWidth(string) > widthSize) {
						string = Convert.insertLineBreak(widthSize, fm, string);
					}
					message.setText(string);
				}
				
				addComponents(yPos, xpos, widthSize, container, timestamp, message);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		private void addComponents(int yPos, int xpos, int widthSize, Container container, LabelName timestamp, BaseLabel message) {
			UiUtil.add(chatContainer, container, xpos, yPos, widthSize, message.getPreferredHeight() + timestamp.getPreferredHeight() + HEIGHT_GAP_BIG);
			container.add(timestamp, LEFT + HEIGHT_GAP, TOP + HEIGHT_GAP, FILL - BaseContainer.WIDTH_GAP_BIG, PREFERRED);
			int messageWidth = message instanceof LabelHyperLink ? PREFERRED : FILL - BaseContainer.WIDTH_GAP_BIG;
			container.add(message, LEFT + HEIGHT_GAP, AFTER, messageWidth, FILL);
		}
		
		private boolean isMedia(JSONObject json) {
			try {
				return json.opt(DATA_MEDIA) instanceof JSONObject && json.optInt(TYPE_MESSAGE) > 0 && json.optInt(TYPE_MESSAGE) < 7;
			} catch (Exception e) {
				return false;
			}
		}

		private Container createNewMessagesContainer(boolean sentByMe) {
			Container container = new Container();
			container.setBackColor(sentByMe ? Color.getRGB("075E54") : Color.getRGB("1a1a1a"));
			container.setBorderStyle(BORDER_ROUNDED);
			container.borderColor = container.getBackColor();
			return container;
		}

		private void loadChat(String chatId) {
			if (chatId == null) {
				return;
			}
			try {
				ParamsSync sync = HttpConnectionManager.getDefaultParamsSync();
				HttpRequest httpRequest = new HttpRequest(sync);
				try (HttpResponse response = httpRequest.get(sync.baseUrl.concat(sync.basePublicServiceUrl).concat("atendimento-hist/find-chat/".concat(chatId)))) {
					chat = new JSONObject(response.asString());
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
			
		}
		
		@Override
		public void reposition() {
			super.reposition();
			initUI();
			tab.setActiveTab(0);
		}
		
	}

}
