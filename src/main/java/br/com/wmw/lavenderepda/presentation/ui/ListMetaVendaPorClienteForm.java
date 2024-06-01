package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.MetaVendaCliService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Button;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListMetaVendaPorClienteForm extends LavendereCrudListForm {
	
	private LabelName lbReppresentante;
	private LabelName lbCliente;
	private LabelName lbMeta;
	private RepresentanteSupervComboBox cbRepresentante;
	private String cdClienteFilter;
	private String dsMetaFilter;
	private LabelValue vlCliente;
	private EditFiltro edFiltroMeta;
	public Button btResize;
	private String sortAtributte = null;
	private String sortAsc = ValueUtil.VALOR_SIM;
	private BarContainer barTopListContainer;
	private final String SORT_ATRIBUTTE_DEFAULT = "DTINICIALVIGENCIA";
	private final String COL_CDMETAVENDACLI = "CDMETAVENDACLI";
	private final String COL_VLMETA = "vlMeta";
	private final String COL_PCTREALIZADO = "pctRealizado";
	private final String COL_PCTRESTANTE = "pctRestante";
	private final String COL_VLREALIZADO = "vlRealizado";
	private final String COL_VLRESTANTE = "vlRestante";
	private final String COL_DTINICIALVIGENCIA = "dtInicialVigencia";
	private final String COL_DTFINALVIGENCIA = "dtFinalVigencia";
	public BaseToolTip tipCliente;

	private boolean window = false;
	
	public ListMetaVendaPorClienteForm(Cliente cliente, boolean window) throws SQLException {
		super(Messages.META_VENDA_CLIENTE);
		this.window = window;
		singleClickOn = true;
		lbReppresentante = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
		cbRepresentante = new RepresentanteSupervComboBox();
        cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
        	cbRepresentante.setSelectedIndex(0);
        }
		sortAtributte = SORT_ATRIBUTTE_DEFAULT;
		if (cliente.nmRazaoSocial != null) {
			vlCliente = new LabelValue(cliente.nmRazaoSocial);
		} else {
			if (cliente.nmFantasia != null) {
				vlCliente = new LabelValue(cliente.nmFantasia);
			} else {
				vlCliente = new LabelValue("");
			}
		}
		tipCliente = new BaseToolTip(vlCliente, vlCliente.getValue());
		cdClienteFilter = cliente.getCdCliente();
		edFiltroMeta = new EditFiltro("999999999", 10);
		lbCliente = new LabelName(Messages.META_VENDA_LABEL_CLIENTE);
		lbMeta = new LabelName(Messages.META_VENDA_DESCRICAO);
		
		barTopListContainer = new BarContainer();
		btResize = new Button("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;
		
		constructorListContainer();
	}
	
    public void visibleState() {
    	super.visibleState();
    	barTopContainer.setVisible(!window);
    }
    
    protected int getTop() {
    	if (window) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }

	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}
	
	public void show() throws SQLException {
		show(this);
	}
	
	protected BaseDomain getDomainFilter() throws SQLException {
		return new MetaVendaCli();
	}

	protected CrudService getCrudService() throws SQLException {
		return MetaVendaCliService.getInstance();
	}

	private String getCdRepresentante() {
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			return SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		if (cbRepresentante.getSelectedIndex() == 0) {
			return "";
		}
		return cbRepresentante.getValue();
	}

	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		MetaVendaCli metaVendaCliFilter = getMetaVendaCliFilter(domain);
		metaVendaCliFilter.sortAtributte = sortAtributte;
		metaVendaCliFilter.sortAsc = sortAsc;

		Vector listMetaVendaCliCached = MetaVendaCliService.getInstance().findAllMetaVendaCli(metaVendaCliFilter);
		int size = listMetaVendaCliCached.size();
		for (int i = 0; i < size; i++) {
			metaVendaCliFilter = (MetaVendaCli) listMetaVendaCliCached.items[i];
			MetaVendaCliService.getInstance().calculaVlMetaVlRealizadoDaMetaVendaCli(metaVendaCliFilter);
		}

		return listMetaVendaCliCached;
	}

	private MetaVendaCli getMetaVendaCliFilter(BaseDomain domain) {
		MetaVendaCli metaVendaCliFilter = (MetaVendaCli) domain;
		metaVendaCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metaVendaCliFilter.cdRepresentante = getCdRepresentante();
		metaVendaCliFilter.cdCliente = cdClienteFilter;
		metaVendaCliFilter.dsMetaVendaCli = dsMetaFilter;
		metaVendaCliFilter.cdMetaVendaCli = dsMetaFilter;
		metaVendaCliFilter.sortAtributte = null;
		return metaVendaCliFilter;
	}
	
	private void constructorListContainer() {
		configListContainer(COL_CDMETAVENDACLI);
		listContainer = new GridListContainer(getListSize(), 3);
		getSortFields();
		listContainer.setColPosition(7, CENTER);
		listContainer.setColPosition(8, RIGHT);
		listContainer.setColPosition(10, CENTER);
		listContainer.setColPosition(11, RIGHT);
	}

	private void getSortFields() {
		listContainer.setColsSort(new String[][] {
			{ Messages.META_VENDA_LABEL_DTINICIALVIGENCIA, COL_DTINICIALVIGENCIA },
			{ Messages.META_VENDA_LABEL_DTFINALVIGENCIA, COL_DTFINALVIGENCIA },
			{ Messages.META_VENDA_LABEL_CODIGO, COL_CDMETAVENDACLI }, 
			{ "Descrição", "DSMETAVENDACLI" },
			{ Messages.META_VENDA_LABEL_VALOR_META, COL_VLMETA }, 
			{ Messages.META_VENDA_LABEL_PCT_REALIZADO, COL_PCTREALIZADO },
			{ Messages.META_VENDA_LABEL_A_REALIZAR, COL_PCTRESTANTE },
			{ Messages.META_VENDA_LABEL_VALOR_REALIZADO, COL_VLREALIZADO },
			{ Messages.META_VENDA_LABEL_REALIZAR, COL_VLRESTANTE }
		});			
	}

	private int getListSize() {
		return 12;
	}

	protected String[] getItem(Object domain) throws SQLException {
		MetaVendaCli metaVendaCli = (MetaVendaCli) domain;
		String[] item = new String[getListSize()];
		item[0] = metaVendaCli.toString();
		item[1] = "";
		item[2] = "";
		item[3] = isVigenciaInvalida(metaVendaCli) ? Messages.META_ERRO_VIGENCIA_NAO_INFORMADA : Messages.META_VENDA_VIGENCIA + ": " + metaVendaCli.dtInicialVigencia + " " + Messages.META_VENDA_ATE + " " + metaVendaCli.dtFinalVigencia;
		item[4] = "";
		item[5] = "";
		item[6] = Messages.META_VENDA_LABEL_VALOR_META;
		item[7] = "";
		item[8] = Messages.META_VENDA_LABEL_VALOR_REALIZADO +" / "+ Messages.META_VENDA_LABEL_REALIZAR;
		item[9] = StringUtil.getStringValueToInterface(metaVendaCli.vlMeta);
		item[10] = "";
		item[11] = StringUtil.getStringValueToInterface(metaVendaCli.vlRealizado) + " (" + StringUtil.getStringValueToInterface(metaVendaCli.getPctRealizadoMeta()) + "%) / "+ 
					StringUtil.getStringValueToInterface(metaVendaCli.vlRealizar) + " (" + StringUtil.getStringValueToInterface(metaVendaCli.getPctRestanteMeta()) + "%)";
		return item;
	}

	protected void setPropertiesInRowList(Item currentItem, BaseDomain domain) throws SQLException {
		MetaVendaCli metaVendaCli = (MetaVendaCli)domain;
		if(LavenderePdaConfig.grifaMetaNaoAtingida && metaVendaCli.isMetaNaoAtingida()) {
			listContainer.setContainerBackColor(currentItem, corLinhaGrid());
		}
	}
	
	private boolean isVigenciaInvalida(MetaVendaCli metaVendaCli) {
		return ValueUtil.isEmpty(metaVendaCli.dtInicialVigencia) || ValueUtil.isEmpty(metaVendaCli.dtFinalVigencia);
	}
	
	private int corLinhaGrid() throws SQLException {
		TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
		CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, 164);
		if (corSistema != null) {
			return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
		}
		return 0;
	}

	protected void onFormStart() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(this, lbReppresentante, cbRepresentante, getLeft(), getNextY());
		}
		
		UiUtil.add(this, lbCliente, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, vlCliente, getLeft(), getNextY(), PREFERRED, PREFERRED);
		
		UiUtil.add(this, lbMeta, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edFiltroMeta, getLeft(), AFTER);
		
		UiUtil.add(this, barTopListContainer, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		UiUtil.add(this, listContainer, LEFT, SAME, FILL, FILL);
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, iconsDefaultWidthHeight, iconsDefaultWidthHeight);
		
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante) {
					if (ValueUtil.isEmpty(cbRepresentante.getValue())) break;
					
					SessionLavenderePda.setRepresentante(((SupervisorRep) cbRepresentante.getSelectedItem()).representante);
					consultaMeta();
				} 
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltroMeta) {
					consultaMeta();
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS:
			case KeyEvent.ACTION_KEY_PRESS: {
				KeyEvent keyEvent = (KeyEvent) event;
				switch (keyEvent.key) {
					case SpecialKeys.ENTER: {
						consultaMeta();
						break;
					}
				}
				break;
            }
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						sortAtributte = listContainer.atributteSortSelected;
						sortAsc = listContainer.sortAsc;
						list();
					}
				}
				break;
			}
		}
	}

	private void consultaMeta() throws SQLException {
		if (ValueUtil.isNotEmpty(getCdRepresentante())) {
			if (ValueUtil.isNotEmpty(cdClienteFilter) || ValueUtil.isNotEmpty(edFiltroMeta.getValue())) {
				dsMetaFilter = edFiltroMeta.getValue();
				list();
			} else {
				UiUtil.showWarnMessage(Messages.META_VENDA_WARN_REP_CLI);
			}
		} else {
			UiUtil.showWarnMessage(Messages.META_VENDA_WARN_REP_SEARCH_CLI);
		}
	}

}