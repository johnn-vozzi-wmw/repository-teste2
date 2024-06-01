package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConcorrente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.service.FotoPesqMerProdConcService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoConcorrenteService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoProdutoService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoRegService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderPesquisaMercadoProdutoConcorrenteWindow;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadPesquisaMercadoConcorrenteFormWindow extends WmwCadWindow {

	private static class ConcorrenteGridItem {
		public int index;
		public String cdConcorrente;
		public String dsConcorrente;
		public String vlUnitario;
	}

	private final PesquisaMercadoConfig pesquisaMercadoConfig;
	private final PesquisaMercadoProduto pesquisaMercadoProduto;
	private final String nuPedido;
	private HashMap<String, ConcorrenteGridItem> concorrenteGridItemHashMap;
	private BaseGridEdit concorrenteGrid;
	private LabelValue lvCliente;
	private LabelValue lvProduto;
	private ButtonPopup btFotos;
	private final int COL_CD_CONCORRENTE = 0;
	private final int COL_DS_CONCORRENTE = 1;
	private final int COL_VL_UNITARIO = 2;

	private final boolean ignoreExcluirItem;
	private final boolean readOnly;
	private boolean firstInsert;

	public CadPesquisaMercadoConcorrenteFormWindow(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto, Vector pesquisaMercadoRegList, String nuPedido, boolean ignoreExcluirItem, boolean readOnly) throws SQLException {
		super(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
		this.pesquisaMercadoConfig = pesquisaMercadoConfig;
		this.pesquisaMercadoProduto = pesquisaMercadoProduto;
		this.nuPedido = nuPedido;
		this.ignoreExcluirItem = ignoreExcluirItem;
		this.readOnly = readOnly;
		if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
			this.btFotos = new ButtonPopup(Messages.PESQUISA_MERCADO_CADASTRO_FOTOS);
		}
		lvCliente = new LabelValue(SessionLavenderePda.getCliente().getDsDomain());
		lvProduto = new LabelValue();
		loadConcorrenteHash();
		loadVlUnitariosReg(pesquisaMercadoRegList);
		setDefaultRect();
	}

	private void loadVlUnitariosReg(Vector pesquisaMercadoRegList) {
		if (ValueUtil.isNotEmpty(pesquisaMercadoRegList)) {
			int size = pesquisaMercadoRegList.size();
			for (int i = 0; i < size; i++) {
				PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) pesquisaMercadoRegList.items[i];
				if (pesquisaMercadoReg == null) {
					continue;
				}
				ConcorrenteGridItem item = concorrenteGridItemHashMap.get(pesquisaMercadoReg.cdConcorrente);
				if (item != null) {
					item.vlUnitario = StringUtil.getStringValueToInterface(pesquisaMercadoReg.vlUnitario);
				}
			}
		} else {
			firstInsert = true;
		}
	}

	private void loadConcorrenteHash() throws SQLException {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrenteFilter = new PesquisaMercadoConcorrente();
		pesquisaMercadoConcorrenteFilter.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoConcorrenteFilter.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		Vector pesquisaMercadoConcorrenteList = PesquisaMercadoConcorrenteService.getInstance().findAllByExample(pesquisaMercadoConcorrenteFilter);
		int size = pesquisaMercadoConcorrenteList.size();
		concorrenteGridItemHashMap = new HashMap<>();
		for (int i = 0; i < size; i++) {
			PesquisaMercadoConcorrente pesquisaMercadoConcorrente = (PesquisaMercadoConcorrente) pesquisaMercadoConcorrenteList.items[i];
			ConcorrenteGridItem concorrenteGridItem = new ConcorrenteGridItem();
			concorrenteGridItem.index = i;
			String cdConcorrente = pesquisaMercadoConcorrente.cdConcorrente;
			concorrenteGridItem.cdConcorrente = cdConcorrente;
			concorrenteGridItem.dsConcorrente = pesquisaMercadoConcorrente.dsConcorrente;
			if (concorrenteGridItemHashMap.get(cdConcorrente) == null) {
				concorrenteGridItemHashMap.put(cdConcorrente, concorrenteGridItem);
			}
		}
	}

	private void carregaGrid() {
		int qtColumns = 3;
		String[][] gridItems = new String[concorrenteGridItemHashMap.values().size()][qtColumns];
		for (ConcorrenteGridItem concorrenteGridItem : concorrenteGridItemHashMap.values()) {
			int index = concorrenteGridItem.index;
			gridItems[index][COL_CD_CONCORRENTE] = concorrenteGridItem.cdConcorrente;
			gridItems[index][COL_DS_CONCORRENTE] = concorrenteGridItem.dsConcorrente;
			gridItems[index][COL_VL_UNITARIO] = concorrenteGridItem.vlUnitario;
		}
		concorrenteGrid.setItems(gridItems);
	}

	private void gridSettings() {
		concorrenteGrid.setGridControllable();
		concorrenteGrid.setColumnEditableDouble(COL_VL_UNITARIO, !readOnly, 9);
	}

	private void configureGrid() {
		GridColDefinition[] gridColDefiniton;
		gridColDefiniton = new GridColDefinition[]{
				new GridColDefinition("", 0, LEFT),
				new GridColDefinition(Messages.PESQUISA_MERCADO_PROD_CONC_WINDOW_CONCORRENTE_LABEL, -70, LEFT),
				new GridColDefinition(Messages.PESQUISA_MERCADO_PROD_CONC_WINDOW_VLUNITARIO_LABEL, -20, LEFT),
		};
		concorrenteGrid = UiUtil.createGridEdit(gridColDefiniton, false);
		concorrenteGrid.setID("concorrenteGrid");
		gridSettings();
		carregaGrid();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lvCliente, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), lvProduto, getLeft(), getNextY());
		lvProduto.setMultipleLinesText(pesquisaMercadoProduto.getDescription().trim());
		configureGrid();
		UiUtil.add(this, concorrenteGrid, LEFT, getNextY(), FILL, FILL);
	}

	@Override
	protected BaseDomain screenToDomain() {
		List<PesquisaMercadoReg> pesquisaMercadoRegList = new ArrayList<>();
		for (ConcorrenteGridItem concorrenteGridItem : concorrenteGridItemHashMap.values()) {
			PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
			pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
			pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
			pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
			if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
				pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
			} else {
				pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			}
			pesquisaMercadoReg.cdProduto = pesquisaMercadoProduto.cdProduto;
			pesquisaMercadoReg.cdConcorrente = concorrenteGridItem.cdConcorrente;
			pesquisaMercadoReg.dtEmissao = DateUtil.getCurrentDate();
			pesquisaMercadoReg.dtInsercaoProduto = pesquisaMercadoReg.dtEmissao;
			pesquisaMercadoReg.hrInsercaoProduto = TimeUtil.getCurrentTimeHHMMSS();
			pesquisaMercadoReg.vlUnitario = ValueUtil.getDoubleValueTruncated(ValueUtil.getDoubleValue(concorrenteGridItem.vlUnitario), LavenderePdaConfig.nuCasasDecimais);
			pesquisaMercadoReg.cdUsuarioEmissao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			if (ValueUtil.isNotEmpty(nuPedido)) {
				pesquisaMercadoReg.nuPedido = nuPedido;
				pesquisaMercadoReg.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			}
			pesquisaMercadoRegList.add(pesquisaMercadoReg);
		}
		PesquisaMercadoReg pesquisaMercadoRegBundle = new PesquisaMercadoReg();
		pesquisaMercadoRegBundle.pesquisaMercadoRegBundle = pesquisaMercadoRegList;
		return pesquisaMercadoRegBundle;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) {
	}

	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		try {
			switch (event.type) {
				case ControlEvent.FOCUS_OUT: {
					if (VmUtil.isSimulador() && event.target instanceof EditNumberFrac) {
						updateVlUnitarioGrid();
					}
				}
				break;
				case ControlEvent.PRESSED: {
					if (event.target == btFotos) {
						btFotosClick();
					}
				}
				break;
				case ValueChangeEvent.VALUE_CHANGE: {
					if (!VmUtil.isSimulador() && concorrenteGrid.getLastShownControl() != null) {
						String txtEdit = ((EditNumberFrac) concorrenteGrid.getLastShownControl()).getText();
						concorrenteGrid.setCellText(concorrenteGrid.getSelectedIndex(), COL_VL_UNITARIO, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(txtEdit)), true);
						updateVlUnitarioGrid();
					}
				}
				break;
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void btFotosClick() throws SQLException {
		ImageSliderPesquisaMercadoProdutoConcorrenteWindow imageSliderPesquisaMercadoProdutoConcorrenteWindow = new ImageSliderPesquisaMercadoProdutoConcorrenteWindow(pesquisaMercadoConfig, pesquisaMercadoProduto, readOnly);
		imageSliderPesquisaMercadoProdutoConcorrenteWindow.popup();
	}

	private void updateVlUnitarioGrid() {
		int index = concorrenteGrid.lastRow;
		ConcorrenteGridItem concorrenteGridItem = concorrenteGridItemHashMap.get(concorrenteGrid.getCellText(index, COL_CD_CONCORRENTE));
		concorrenteGridItem.vlUnitario = concorrenteGrid.getCellText(index, COL_VL_UNITARIO);
	}

	private boolean gridHasValues() {
		for (ConcorrenteGridItem concorrenteGridItem : concorrenteGridItemHashMap.values()) {
			double vlUnitario = ValueUtil.getDoubleValue(concorrenteGridItem.vlUnitario);
			if (vlUnitario != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void beforeSave() throws SQLException {
		super.beforeSave();
		if (gridHasValues() && LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() && LavenderePdaConfig.obrigaInclusaoFotoPesquisaMercado() && !FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(pesquisaMercadoConfig, pesquisaMercadoProduto)) {
			throw new ValidationException(Messages.PESQUISA_MERCADO_OBRIGATORIO_CADASTRAR_FOTO);
		}
	}

	@Override
	protected void onSave() throws SQLException {
		save();
		if (!gridHasValues() && LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() && FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(pesquisaMercadoConfig, pesquisaMercadoProduto)) {
			if (UiUtil.showErrorConfirmYesNoMessage(Messages.PESQUISA_MERCADO_DESEJA_DESCARTAR_FOTOS)) {
				FotoPesqMerProdConcService.getInstance().deleteAllFotosByExample(pesquisaMercadoConfig, pesquisaMercadoProduto);
				fecharWindow();
			}
		} else {
			fecharWindow();
		}
	}

	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoRegBundle = ((PesquisaMercadoReg) domain);
		List<PesquisaMercadoReg> pesquisaMercadoRegList = pesquisaMercadoRegBundle.pesquisaMercadoRegBundle;
		if ((ignoreExcluirItem || PesquisaMercadoProdutoService.getInstance().itemIsAdicionado(pesquisaMercadoProduto)) && !gridHasValues()) {
			throw new ValidationException(Messages.PESQUISA_MERCADO_NOVO_PRODUTO_SALVAR_SEM_ITENS);
		}
		for (PesquisaMercadoReg pesquisaMercadoReg : pesquisaMercadoRegList) {
			if (PesquisaMercadoRegService.getInstance().findByRowKey(pesquisaMercadoReg.getRowKey()) != null) {
				if (pesquisaMercadoReg.vlUnitario == 0) {
					PesquisaMercadoRegService.getInstance().delete(pesquisaMercadoReg);
				} else {
					PesquisaMercadoRegService.getInstance().update(pesquisaMercadoReg);
				}
			} else if (pesquisaMercadoReg.vlUnitario != 0) {
				PesquisaMercadoRegService.getInstance().insert(pesquisaMercadoReg);
			}
		}
	}

	@Override
	protected void delete(BaseDomain baseDomain) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) baseDomain;
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		pesquisaMercadoReg.cdProduto = pesquisaMercadoProduto.cdProduto;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		PesquisaMercadoRegService.getInstance().deleteAllByExample(pesquisaMercadoReg);
		if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
			FotoPesqMerProdConc fotoPesqMerProdConc = new FotoPesqMerProdConc();
			fotoPesqMerProdConc.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
			fotoPesqMerProdConc.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
			fotoPesqMerProdConc.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
			fotoPesqMerProdConc.cdProduto = pesquisaMercadoProduto.cdProduto;
			if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
				fotoPesqMerProdConc.cdCliente = ValueUtil.VALOR_ZERO;
			} else {
				fotoPesqMerProdConc.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			}
			FotoPesqMerProdConcService.getInstance().deleteAllByExample(fotoPesqMerProdConc);
		}
	}

	@Override
	protected void btFecharClick() throws SQLException {
		if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() && LavenderePdaConfig.obrigaInclusaoFotoPesquisaMercado() && !firstInsert && !FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(pesquisaMercadoConfig, pesquisaMercadoProduto)) {
			throw new ValidationException(Messages.PESQUISA_MERCADO_OBRIGATORIO_CADASTRAR_FOTO);
		}
		if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() && firstInsert && FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(pesquisaMercadoConfig, pesquisaMercadoProduto)) {
			if (UiUtil.showErrorConfirmYesNoMessage(Messages.PESQUISA_MERCADO_DESEJA_DESCARTAR_FOTOS)) {
				FotoPesqMerProdConcService.getInstance().deleteAllFotosByExample(pesquisaMercadoConfig, pesquisaMercadoProduto);
			} else {
				return;
			}
		}
		fecharWindow();
		closedByBtFechar = true;
	}

	@Override
	protected void visibleState() {
		btExcluir.setVisible(!readOnly && LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado() && !ignoreExcluirItem && PesquisaMercadoProdutoService.getInstance().itemIsAdicionado(pesquisaMercadoProduto));
		if (readOnly) {
			setReadOnly();
			setEnabled(true);
		}
		ajustaTamanhoBotoes();
	}

	@Override
	protected void addButtons() {
		addButtonPopup(btSalvar);
		if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
			addButtonPopup(btFotos);
		}
		addButtonPopup(btFechar);
		addButtonPopup(btExcluir);
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		btSalvar.setText(FrameworkMessages.BOTAO_SALVAR);
	}

	@Override
	protected void clearScreen() {

	}

	@Override
	protected BaseDomain createDomain() {
		return null;
	}

	@Override
	protected String getEntityDescription() {
		return MessageUtil.getMessage(Messages.PESQUISA_MERCADO_NOVO_PRODUTO_EXCLUIR_CONFIRM, new String[]{pesquisaMercadoProduto.cdProduto, pesquisaMercadoProduto.dsProduto});
	}

	@Override
	protected CrudService getCrudService() {
		return PesquisaMercadoRegService.getInstance();
	}

	public Control getlistContainer() {
		return concorrenteGrid;
	}
}
