package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.service.FotoPesqMerProdConcService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoProdutoService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoRegService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderPesquisaMercadoProdutoConcorrenteWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadPesquisaMercadoProdutoForm extends BaseCrudCadForm {

	private LabelContainer lcDsPesquisaMercado;
	private EditFiltro edFiltro;
	private ButtonAction btCadastrarCoordenada;
	private ButtonAction btCadastrarFotos;
	private ButtonAction btAddItem;
	private ButtonAction btFecharPesquisa;
	private boolean barBottomContainerAdded;
	private boolean readOnly;
	private boolean cadastrouCoordenada;
	private GridListContainer produtoListContainer;
	public String nuPedido;
	private double longitude;
	private double latitude;

	public CadPesquisaMercadoProdutoForm() {
		super(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
		lcDsPesquisaMercado = new LabelContainer("");
		btFecharPesquisa = new ButtonAction(Messages.PESQUISA_MERCADO_BOTAO_FECHAR_PESQUISA, "images/fecharpedido.png");
		edFiltro = new EditFiltro("999999999", 50);
		if (LavenderePdaConfig.usaCadastroCoordenadaPesquisaMercado()) {
			btCadastrarCoordenada = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_COORDENADA, "images/gps.png");
		}
		if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado()) {
			btCadastrarFotos = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_FOTOS, "images/camera.png");
		}
		btSalvar.setVisible(false);
		if (LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado()) {
			btAddItem = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_NOVO_PRODUTO, "images/add.png");
		}
		produtoListContainer = new GridListContainer();
		produtoListContainer.usaScroolHorizontal = true;

		produtoListContainer.setID("produtoListContainer");
	}

	public GridListContainer getlistContainer() {
		return produtoListContainer;
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		return null;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
	}

	@Override
	protected void clearScreen() throws SQLException {
	}

	@Override
	public void edit(BaseDomain domain) throws SQLException {
		setDomain(domain);
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) domain;
		if (pesquisaMercadoConfig.isExpirada()) {
			throw new ValidationException(Messages.PESQUISA_MERCADO_PROD_CONC_VIGENCIA_EXTRAPOLADA);
		}
		lcDsPesquisaMercado.setDescricao(pesquisaMercadoConfig.getDescription());
		readOnly = PesquisaMercadoRegService.getInstance().isPesquisaFinalizada(getPesquisaMercadoConfig());
		if (barBottomContainerAdded) {
			loadBottomBarContainer();
		}
		visibleState();
		loadListContainer();
	}


	private void loadListContainer() throws SQLException {
		Vector domainList = getProdutoList();
		int listSize = domainList.size();
		Container[] all = new Container[listSize];
		if (listSize > 0) {
			BaseListContainer.Item c;
			PesquisaMercadoProduto pesquisaMercadoProduto;
			produtoListContainer.getLayout().setup();
			produtoListContainer.getBaseListContainer().setRect(0, 0, FILL, FILL);
			PesquisaMercadoConfig pesquisaMercadoConfig = getPesquisaMercadoConfig();
			for (int i = 0; i < listSize; i++) {
				all[i] = c = new BaseListContainer.Item(produtoListContainer.getLayout());
				pesquisaMercadoProduto = (PesquisaMercadoProduto) domainList.items[i];
				c.id = pesquisaMercadoProduto.rowKey;
				c.domain = pesquisaMercadoProduto;
				c.setItens(getItem(pesquisaMercadoProduto));
				c.setToolTip(pesquisaMercadoProduto.getDescription());
				setPropertiesInRowList(c, pesquisaMercadoProduto, pesquisaMercadoConfig);
				c.setID(c.id);
			}
			produtoListContainer.addContainers(all);
		}
	}

	private void setPropertiesInRowList(BaseListContainer.Item c, PesquisaMercadoProduto pesquisaMercadoProduto, PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		if (PesquisaMercadoRegService.getInstance().hasPesquisaMercadoForProduto(pesquisaMercadoProduto, pesquisaMercadoConfig, true)) {
			c.setBackColor(LavendereColorUtil.COR_PESQUISA_MERCADO_PRODUTO_VALOR_PREENCHIDO);
		}
	}

	private String[] getItem(PesquisaMercadoProduto pesquisaMercadoProduto) {
		return new String[]{pesquisaMercadoProduto.getDescription()};
	}

	private Vector getProdutoList() throws SQLException {
		PesquisaMercadoProduto pesquisaMercadoProdutoFilter = new PesquisaMercadoProduto();
		pesquisaMercadoProdutoFilter.cdEmpresa = getPesquisaMercadoConfig().cdEmpresa;
		pesquisaMercadoProdutoFilter.cdPesquisaMercadoConfig = getPesquisaMercadoConfig().cdPesquisaMercadoConfig;
		pesquisaMercadoProdutoFilter.dsFiltro = edFiltro.getValue();
		Vector pesquisaMercadoProdutoVector = PesquisaMercadoProdutoService.getInstance().findAllByExample(pesquisaMercadoProdutoFilter);
		if (LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado()) {
			PesquisaMercadoConfig pesquisaMercadoConfigFilter = (PesquisaMercadoConfig) getPesquisaMercadoConfig().clone();
			pesquisaMercadoConfigFilter.dsProdutoFiltro = edFiltro.getValue();
			Vector itensAdicionados = PesquisaMercadoProdutoService.getInstance().findProdutosAdicionados(pesquisaMercadoConfigFilter);
			pesquisaMercadoProdutoVector.addElementsNotNull(itensAdicionados.items);
		}
		return pesquisaMercadoProdutoVector;
	}

	private PesquisaMercadoConfig getPesquisaMercadoConfig() throws SQLException {
		return (PesquisaMercadoConfig) getDomain();
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return null;
	}

	@Override
	public void close() throws SQLException {
		super.close();
		produtoListContainer.removeAllContainers();
		produtoListContainer.clear();
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lcDsPesquisaMercado, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, produtoListContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - UiUtil.getButtonBarDefaultHeight(barBottomContainer) - WIDTH_GAP_BIG);
		UiUtil.add(this, barBottomContainer, LEFT, AFTER, FILL, FILL);
		barBottomContainerAdded = true;
		loadBottomBarContainer();
	}

	private void loadBottomBarContainer() {
		int indexButtonAction = 5;
		barBottomContainer.removeAll();
		if (LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado() && !readOnly) {
			UiUtil.add(barBottomContainer, btAddItem, indexButtonAction--);
		}
		if (LavenderePdaConfig.usaCadastroCoordenadaPesquisaMercado() && !readOnly) {
			UiUtil.add(barBottomContainer, btCadastrarCoordenada, indexButtonAction--);
		}
		if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado()) {
			UiUtil.add(barBottomContainer, btCadastrarFotos, indexButtonAction--);
		}
		UiUtil.add(barBottomContainer, btFecharPesquisa, 1);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCadastrarCoordenada) {
					btCadastrarCoordenadaClick(LavenderePdaConfig.usaCadastroAutomaticoCoordenadaPesquisaDeMercado());
				} else if (event.target == btCadastrarFotos) {
					btCadastrarFotosClick();
				} else if (event.target == btAddItem) {
					btAddItemClick();
				} else if (event.target == btFecharPesquisa) {
					btFecharPesquisaClick();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					btFiltrarClick();
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				if (event.target == edFiltro && ((KeyEvent) event).isActionKey()) {
					btFiltrarClick();
					if (produtoListContainer != null && produtoListContainer.size() == 0) {
						edFiltro.requestFocus();
					}
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (produtoListContainer.isEventoClickUnicoDisparado())) {
					produtoListContainer.setEventoClickUnicoDisparado(false);
					singleClickInList();
				}
				break;
			}
		}
	}

	private void btFecharPesquisaClick() throws SQLException {
		if (!PesquisaMercadoRegService.getInstance().hasPesquisaMercadoForConfig(getPesquisaMercadoConfig())) {
			UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_PROD_CONC_NENHUMA_PESQUISA_REALIZADA);
			return;
		}
		if (UiUtil.showConfirmYesNoMessage(Messages.PESQUISA_MERCADO_PROD_CONC_CONFIRM_FECHAR_PESQUISA) && salvarPesquisaMercado()) {
			PesquisaMercadoRegService.getInstance().updateFlFinalizada(getPesquisaMercadoConfig(), ValueUtil.VALOR_SIM);
			if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
				FotoPesqMerProdConcService.getInstance().updateFlTipoAlteracao(getPesquisaMercadoConfig());
			}
			this.readOnly = true;
			visibleState();
			if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado()) {
				barBottomContainer.removeAll();
				UiUtil.add(barBottomContainer, btCadastrarFotos, 5);
			}
		}
	}

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado()) {
			btAddItem.setVisible(LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado() && !readOnly);
		}
		if (LavenderePdaConfig.usaCadastroCoordenadaPesquisaMercado()) {
			btCadastrarCoordenada.setVisible(LavenderePdaConfig.usaCadastroCoordenadaPesquisaMercado() && !readOnly);
		}
		btFecharPesquisa.setVisible(!readOnly);
	}

	private void btCadastrarFotosClick() throws SQLException {
		ImageSliderPesquisaMercadoProdutoConcorrenteWindow imageSliderPesquisaMercadoProdutoConcorrenteWindow = new ImageSliderPesquisaMercadoProdutoConcorrenteWindow(getPesquisaMercadoConfig(), readOnly);
		imageSliderPesquisaMercadoProdutoConcorrenteWindow.popup();
	}

	private void btAddItemClick() throws SQLException {
		ListNovoProdutoPesquisaMercadoForm listNovoProdutoPesquisaMercadoForm = new ListNovoProdutoPesquisaMercadoForm(getPesquisaMercadoConfig(), nuPedido, this);
		show(listNovoProdutoPesquisaMercadoForm);
	}

	private void btCadastrarCoordenadaClick(boolean autoCadastro) {
		CadPesquisaMercadoRegCoordenadaWindow cadPesquisaMercadoRegCoordenadaWindow = new CadPesquisaMercadoRegCoordenadaWindow(autoCadastro);
		cadPesquisaMercadoRegCoordenadaWindow.popup();
		cadastrouCoordenada = cadPesquisaMercadoRegCoordenadaWindow.cadastrouCoordenada;
		latitude = cadPesquisaMercadoRegCoordenadaWindow.latitude;
		longitude = cadPesquisaMercadoRegCoordenadaWindow.longitude;
	}

	private boolean salvarPesquisaMercado() throws SQLException {
		if (cadastrouCoordenada) {
			updateCoordenadasPesquisaMercadoReg();
		} else if (LavenderePdaConfig.permiteCoordenadaAutoPesquisaMercado()) {
			double[] lat_lon = PesquisaMercadoRegService.getInstance().getCoordenadasBackground();
			if (lat_lon[0] == 0 && lat_lon[1] == 0) {
				UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_PROD_CONC_ERRO_COORDENADA);
				btCadastrarCoordenadaClick(false);
				if (cadastrouCoordenada) {
					updateCoordenadasPesquisaMercadoReg();
				}
			} else {
				latitude = lat_lon[0];
				longitude = lat_lon[1];
				updateCoordenadasPesquisaMercadoReg();
			}
		}
		if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado() && LavenderePdaConfig.obrigaInclusaoFotoPesquisaMercado() && !FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(getPesquisaMercadoConfig())) {
			UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_OBRIGATORIO_CADASTRAR_FOTO);
			btCadastrarFotosClick();
			if (!FotoPesqMerProdConcService.getInstance().hasFotoPesquisaMercadoRegistrada(getPesquisaMercadoConfig())) {
				return false;
			}
		}
		return true;
	}

	private void updateCoordenadasPesquisaMercadoReg() throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		PesquisaMercadoConfig pesquisaMercadoConfig = getPesquisaMercadoConfig();
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		pesquisaMercadoReg.cdLatitude = latitude;
		pesquisaMercadoReg.cdLongitude = longitude;
		PesquisaMercadoRegService.getInstance().updateCoordenadasByExample(pesquisaMercadoReg);
	}

	private void singleClickInList() throws SQLException {
		PesquisaMercadoProduto pesquisaMercadoProduto = (PesquisaMercadoProduto) PesquisaMercadoProdutoService.getInstance().findByRowKey(produtoListContainer.getSelectedId());
		if (pesquisaMercadoProduto == null && LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado()) {
			pesquisaMercadoProduto = PesquisaMercadoProdutoService.getInstance().findByRegRowKey(produtoListContainer.getSelectedId());
		}
		PesquisaMercadoReg pesquisaMercadoRegFilter = new PesquisaMercadoReg();
		pesquisaMercadoRegFilter.cdEmpresa = getPesquisaMercadoConfig().cdEmpresa;
		pesquisaMercadoRegFilter.cdRepresentante = getPesquisaMercadoConfig().cdRepresentante;
		pesquisaMercadoRegFilter.cdPesquisaMercadoConfig = getPesquisaMercadoConfig().cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoRegFilter.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoRegFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		if (pesquisaMercadoProduto != null) {
			pesquisaMercadoRegFilter.cdProduto = pesquisaMercadoProduto.cdProduto;
			Vector pesquisaMercadoRegList = PesquisaMercadoRegService.getInstance().findAllByExample(pesquisaMercadoRegFilter);
			CadPesquisaMercadoConcorrenteFormWindow cadPesquisaMercadoConcorrenteFormWindow = new CadPesquisaMercadoConcorrenteFormWindow(getPesquisaMercadoConfig(), pesquisaMercadoProduto, pesquisaMercadoRegList, nuPedido, false, readOnly);
			cadPesquisaMercadoConcorrenteFormWindow.popup();
			btFiltrarClick();
		}
	}

	public void btFiltrarClick() throws SQLException {
		produtoListContainer.removeAllContainers();
		produtoListContainer.clear();
		loadListContainer();
	}
}
