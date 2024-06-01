package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.BaseWindow;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CatalogoParams;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.CatalogoProdutoService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.combo.EstoqueDisponivelComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1MultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalEstoqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoDestaqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import totalcross.io.File;
import totalcross.io.FileStates;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CatalogoProdutoWindow extends WmwListWindow {

	private static final String FILTRO_SEM_PRECO = "1";
	private static final String FILTRO_TEXTO = "2";

	private final ButtonPopup btGerar;
	private final ButtonPopup btLimpar;
	private final TabelaPrecoComboBox cbTabelaPreco;
	private final CheckBoolean ckSemPreco;
	private final EditMemo edDsFiltroTexto;
	private final LocalEstoqueComboBox cbLocalEstoque;
	private final EstoqueDisponivelComboBox cbEstoqueDisponivel;
	private final ProdutoDestaqueComboBox cbProdutoDestaque;
	private final CatalogoParams catalogoParams;
	private final GrupoProduto1MultiComboBox cbGrupoProduto;
	private ScrollTabbedContainer sc;

	public CatalogoProdutoWindow() throws SQLException {
		super(Messages.GERAR_CATALOGO);
		this.btGerar = new ButtonPopup(Messages.BOTAO_GERAR);
		this.btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		this.cbTabelaPreco = new TabelaPrecoComboBox();
		this.cbTabelaPreco.loadForListProduto(true);
		this.cbTabelaPreco.setSelectedIndex(0);
		this.ckSemPreco = new CheckBoolean(Messages.CATALOGO_CHECKBOX_SEMPRECO);
		this.edDsFiltroTexto = new EditMemo("", 5, 4000);
		this.cbLocalEstoque = new LocalEstoqueComboBox();
		this.cbEstoqueDisponivel = new EstoqueDisponivelComboBox();
		cbProdutoDestaque = new ProdutoDestaqueComboBox();
		catalogoParams = new CatalogoParams();
		cbGrupoProduto = new GrupoProduto1MultiComboBox();
		constructorListContainer();
		sc = new ScrollTabbedContainer(new String[] {Messages.CATALOGO_ABA_DISPONIVEIS, Messages.GERAR_CATALOGO});
		cbGrupoProduto.load();
		cbProdutoDestaque.load();
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		singleClickOn = true;
		listContainer = new GridListContainer(2, 2, false, true, true, false);
		listContainer.resizeable = false;
		listContainer.setBarTopSimple();
		listContainer.setColPosition(1, RIGHT);
	}

	private void btGerarCatalogoClick() {
		if (validateButtonAction()) {
			return;
		}
		
		if (verificaGeracaoEmAndamento()) {
			return;
		}
		final String cdTabelaPreco = this.cbTabelaPreco.getValue();
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			catalogoParams.cdUsuario = Session.getCdUsuario();
			catalogoParams.id = TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS();
			catalogoParams.cdEmpresa = SessionLavenderePda.cdEmpresa;
			catalogoParams.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			catalogoParams.cdTabelaPreco = ValueUtil.isEmpty(cdTabelaPreco) ? TabelaPreco.CDTABELAPRECO_VALOR_ZERO: cdTabelaPreco;
			catalogoParams.grupoProdutoList = getGrupoProduto1();
			catalogoParams.semPreco = this.ckSemPreco.getValue();
			catalogoParams.dsFiltroTexto = getDsFiltroTexto();
			catalogoParams.cdLocalEstoque = getLocalEstoque();
			catalogoParams.estoqueDisponivelList = getEstoqueDisponivel();
			catalogoParams.grupoDestaqueList = getProdutoDestaque();
			JSONObject json = CatalogoProdutoService.getInstance().getCatalogoProdutoJson(catalogoParams);
			new AsyncUIControl(true) {
				
				@Override
				public void execute() {
					CatalogoProdutoService.getInstance().gerarCatalogo(json);
				}
				
				@Override
				public void after() {
					if (window.getException() != null) {
						UiUtil.showErrorMessage(window.getException().getMessage());
					}
					verificaGeracaoEmAndamento();
				}
				
			}.closeKeppRunning().open();
		} else {
			UiUtil.showErrorMessage(Messages.CATALOGO_MSG_ERRO_TAB_PRECO);
		}
	}

	private boolean validateButtonAction() {
		if (sc.getActiveTab() == 0) {
			UiUtil.showInfoMessage(Messages.CATALOGO_WARN_GERAR_CATALOGO + Messages.GERAR_CATALOGO);
			return true;
		}
		return false;
	}
	
	private boolean verificaGeracaoEmAndamento() {
		if (CatalogoProdutoService.downloading) {
			UiUtil.showInfoMessage(Messages.CATALOGO_AGUARDANDO_CATALOGO);
			return true;
		} else {
			Notification notification = NotificationManager.checkNotificationAndRemove(LavendereNotificationConstants.CATALOGO_PRODUTO);
			if (notification != null) {
				try {
					notification.process();
					list();
				} catch (Exception e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
			}
		}
		return false;
	}
	
	private void limpaFiltrosClick() {
		if (validateButtonAction()) {
			return;
		}
		cbTabelaPreco.setSelectedIndex(0);
		cbEstoqueDisponivel.setSelectedItensDefault();
		ckSemPreco.setChecked(true);
		edDsFiltroTexto.setText("");
		cbGrupoProduto.unselectAll();
		cbProdutoDestaque.clear();
		if (LavenderePdaConfig.isUsaSelecaoLocalEstoqueCatalogoProduto()) {
			cbLocalEstoque.setSelectedIndex(0);
		}
	}
	
	private String getLocalEstoque() {
		return cbLocalEstoque.getValue().equals("") ? CatalogoParams.EMPTY : cbLocalEstoque.getValue();
	}
	
	private String getEstoqueDisponivel() {
		return cbEstoqueDisponivel.getValue().equals("") ? CatalogoParams.EMPTY : cbEstoqueDisponivel.getValue();
	}
	
	public String getGrupoProduto1(){
		return cbGrupoProduto.getValue().equals("") ? CatalogoParams.EMPTY : cbGrupoProduto.getValue();
	}
	
	private String getProdutoDestaque() {
		return cbProdutoDestaque.getValue().equals("") ? CatalogoParams.EMPTY : cbProdutoDestaque.getValue();
	}

	private String getDsFiltroTexto() {
		final String dsFiltroTexto = this.edDsFiltroTexto.getValue();
		return dsFiltroTexto.isEmpty() ? CatalogoParams.EMPTY : dsFiltroTexto;
	}
	
	private Vector getFileList() {
		Vector files = new Vector();
		String path = Produto.getDsFilePathPdfProduto();
		if (FileUtil.exists(path)) {
			try (File dir = new File(Produto.getDsFilePathPdfProduto())) {
				for (String string : dir.listFiles()) {
					try (File f = new File(Convert.appendPath(path, string), FileStates.READ_ONLY)) {
						final String PDF_EXTENSION = ".pdf";
						if (!f.isDir() && string.endsWith(PDF_EXTENSION)) {
							PdfFile pdfFile = new PdfFile().build(string, f);
							files.addElement(pdfFile);
						}
					}
				}
			} catch (Exception e) {
				VmUtil.debug(e);
			}
			SortUtil.qsortString(files.items, 0, files.size() - 1, false);
		}
		return files;
	}
	
	class PdfFile extends BaseDomain implements FileProperties {
		
		String id;
		String fileName;
		String absolutePath;
		Time time;
		String hrModificacao;
		Date dtModificacao;
		int size;
		
		public PdfFile build(String name, File file) throws Exception {
			this.fileName = name;
			this.absolutePath = file.getPath();
			this.id = name.replace(".pdf", ValueUtil.VALOR_NI);
			this.time = file.getTime(File.TIME_MODIFIED);
			this.hrModificacao = TimeUtil.formatTimeString(time, true, true);
			this.dtModificacao = DateUtil.getDateValue(time);
			this.size = file.getSize();
			return this;
		}

		@Override
		public String getFileName() {
			return fileName;
		}

		@Override
		public String getAbsolutePath() {
			return absolutePath;
		}

		@Override
		public String getHrModificacao() {
			return null;
		}

		@Override
		public Date getDtModificacao() {
			return null;
		}

		@Override
		public JSONObject getRequestJson() {
			return null;
		}

		@Override
		public String getHttpEndpoint() {
			return null;
		}

		@Override
		public String getNmCampoUpdateRecebimento() {
			return null;
		}

		@Override
		public String getVlCampoUpdateRecebimento() {
			return null;
		}

		@Override
		public String getPrimaryKey() {
			return id;
		}
		
		public String getSizeFormatted() {
			double b = (double) size / 1024 / 1024;
			if (b > 0d) {
				return StringUtil.getStringValue(b) + " MB";
			}
			b = (double) size / 1024;
			if (b > 0d) {
				return StringUtil.getStringValue(b) + " KB";
			}
			
			return StringUtil.getStringValue(size) + " bytes";
		}
		
		@Override
		public String getSortStringValue() {
			return time.toIso8601();
		}
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getFileList();
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		PdfFile pdfFile = (PdfFile) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(pdfFile.dtModificacao));
		item.addElement(StringUtil.getStringValue(pdfFile.hrModificacao));
		return (String[]) item.toObjectArray();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		BaseListContainer.Item selectedItem = (BaseListContainer.Item) listContainer.getSelectedItem();
		PdfFile f = ((PdfFile)selectedItem.domain);
		int op = UiUtil.showMessage("Escolha a ação para o arquivo " + f.fileName, new String[] {"Fechar", "Excluir", "Abrir"});
		switch (op) {
		case 1:
			if (UiUtil.showConfirmYesNoMessage("Deseja excluir o arquivo?")) {
				FileUtil.deleteFile(f.absolutePath);
				list();
			}
			break;
		case 2:
			VmUtil.viewer(((PdfFile)selectedItem.domain).absolutePath);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		PdfFile pdfFile = (PdfFile) domain;
		containerItem.addSublistItem(new String[] {StringUtil.getStringValue(StringUtil.getStringValue(pdfFile.fileName))});
		containerItem.addSublistItem(new String[] {StringUtil.getStringValue(pdfFile.getSizeFormatted())});
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new PdfFile();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, sc, LEFT, TOP, FILL, FILL);
		Container container = sc.getContainer(0);
		container.add(listContainer, LEFT, TOP, FILL, FILL);
		container = sc.getContainer(1);
		
		UiUtil.add(container, new LabelName(Messages.CATALOGO_TAB_PRECO), this.cbTabelaPreco, getLeft(), getNextY());
		final List<String> filtrosOpcionaisList = LavenderePdaConfig.getListaFiltrosOpcionaisCatalogo();
		if (filtrosOpcionaisList.contains(CatalogoProdutoWindow.FILTRO_SEM_PRECO)) {
			UiUtil.add(container, this.ckSemPreco, getLeft(), getNextY());
			this.ckSemPreco.setChecked(true);
		}
		if (filtrosOpcionaisList.contains(CatalogoProdutoWindow.FILTRO_TEXTO)) {
			UiUtil.add(container, new LabelName(Messages.CATALOGO_EDIT_DSFILTROTEXTO), this.edDsFiltroTexto, getLeft(),
					getNextY());
		}
		
		UiUtil.add(container, new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1()), cbGrupoProduto, getLeft(),
				Control.AFTER + BaseWindow.HEIGHT_GAP_BIG);
		
		if (LavenderePdaConfig.isUsaGrupoDestaqueProdutoCatalogo()) { 
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_PRODUTO_DESTAQUE), cbProdutoDestaque, getLeft(),
					Control.AFTER + BaseWindow.HEIGHT_GAP_BIG);
		} 
		cbProdutoDestaque.clear();
		
		if (LavenderePdaConfig.isUsaSelecaoLocalEstoqueCatalogoProduto()) {
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_FILTRO_LOCAL_ESTOQUE), this.cbLocalEstoque, getLeft(),
					Control.AFTER + BaseWindow.HEIGHT_GAP_BIG);
		} else {
			cbLocalEstoque.setSelectedIndex(-1);
		}
		if (LavenderePdaConfig.isUsaCatalogoProdutoEstoqueDisponivel()) {
			UiUtil.add(container, new LabelName(Messages.ESTOQUE_COMBO_TITULO), this.cbEstoqueDisponivel, getLeft(),
					Control.AFTER + BaseWindow.HEIGHT_GAP_BIG);
		} else {
			cbEstoqueDisponivel.clear();
		}
		addButtonPopup(this.btGerar);
		addButtonPopup(this.btLimpar);
		addButtonPopup(this.btFechar);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == this.btGerar) {
					btGerarCatalogoClick();
				} else if (event.target == btLimpar) {
					limpaFiltrosClick();
				}
				break;
			}
			default:
				break;
		}
	}

}
