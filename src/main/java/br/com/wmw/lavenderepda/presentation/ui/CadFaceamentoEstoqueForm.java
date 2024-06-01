package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Faceamento;
import br.com.wmw.lavenderepda.business.domain.FaceamentoEstoque;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.FaceamentoEstoqueService;
import br.com.wmw.lavenderepda.business.service.FaceamentoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.TermoCorrecaoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoOpcaoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class CadFaceamentoEstoqueForm extends BaseCrudCadForm {

    private BaseGridEdit grid;
    private final int GRID_POS_FL_TIPO_ALTERACAO = 0;
    private final int GRID_POS_CDPRODUTO = 1;
    private final int GRID_POS_ESTOQUE_ATUAL = 4;
    private final int GRID_POS_SUGESTAO_VENDA = 5;
    private final int GRID_POS_SUGESTAO_VENDA_REP = 6;
    private Vector faceamentoList;
    private String sortAttribute;
    private String sortAsc;
	private ButtonAction btLeitorCamera;
	private int[] lastSelectedXYindex;
	private int qtdProdutos;
	private GrupoProduto1ComboBox cbGrupoProduto1;
	private GrupoProduto2ComboBox cbGrupoProduto2;
	private GrupoProduto3ComboBox cbGrupoProduto3;
	private GrupoProduto4ComboBox cbGrupoProduto4;
	private LabelName lbGrupoProduto1;
	private LabelName lbGrupoProduto3;
	private PushButtonGroupBase btGroupTipoFiltros;

	protected PushButtonGroupBase btTipoPesquisaEdFiltro;
	private FornecedorComboBox cbFornecedor;
	private AtributoProdComboBox cbAtributoProd;
	private AtributoOpcaoProdComboBox cbAtributoOpcaoProd;
	private HashMap<String, Integer> posicoesbtGroupTipoFiltros = new HashMap<>();
	private Map<String, Boolean> filtrosVisiveisMap = new HashMap<>();
	private Map<String, Boolean> filtrosNaoAutomaticosMap = new HashMap<>();
	private static final String HASHKEY_PA = "PA";
	private static final String HASHKEY_AP = "AP";
	private static final String HASHKEY_DS = "DS";
	private static final String HASHKEY_CD = "CD";
	private BaseButton btFiltroAvancado;
	private BaseButton btFiltrar;
	private EditFiltro edFiltro;
	private boolean flListInicialized;
	private String dsFiltro = "";
	private boolean filterByPrincipioAtivo;
	private boolean filterByAplicacaoProduto;
	private boolean filterByCodigoProduto;
	private String cdTabelaPreco = "";

	private Vector produtoList = new Vector();

	public static String NU_FILTRO_FORNECEDOR = "1";
	public static String NU_FILTRO_GRUPOPRODUTO = "2";
	public static String NU_FILTRO_ATRIBUTO_PRODUTO = "3";

	private Map<String, FaceamentoEstoque> faceamentoEstoqueMap;

    public CadFaceamentoEstoqueForm(String sortAttribute, String sortAsc) throws SQLException{
        super(Messages.FACEAMENTOESTOQUE_TITULO_CADASTRO);
        this.sortAttribute = sortAttribute;
        this.sortAsc = sortAsc;
		flListInicialized = false;
        faceamentoEstoqueMap = new HashMap<String, FaceamentoEstoque>();
		btLeitorCamera = new ButtonAction(Messages.CAMERA, "images/barcode.png");
		cbGrupoProduto1 = new GrupoProduto1ComboBox(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
		cbGrupoProduto2 = new GrupoProduto2ComboBox(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
		cbGrupoProduto3 = new GrupoProduto3ComboBox(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
		cbGrupoProduto4 = new GrupoProduto4ComboBox(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			inicializaFiltrosGrupoProduto();
		}
		inicializaBtGroupTipoFiltros();
		btFiltrar = new BaseButton("", UiUtil.getColorfulImage("images/search.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		btFiltroAvancado = new BaseButton(UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		edFiltro = new EditFiltro("999999999", 50);
    }

	private void inicializaBtGroupTipoFiltros() {
		List<String> list = new ArrayList<>();

		if(LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() && LavenderePdaConfig.usaFiltroAtivoFaceamento) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO);
		}
		if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO);
		}
		if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO);
		}
		list.add(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO);
		btGroupTipoFiltros = new PushButtonGroupBase(list.toArray(new String[0]), true, 1, -1, 1, 1, true, PushButtonGroup.NORMAL);
		setaPosicoesHashMapbtGroupTipoFiltros(list);

		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			btTipoPesquisaEdFiltro = new PushButtonGroupBase(new String[] {Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_CONTEM, Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_INICIA}, true, 0, -1, 1, 1, true, PushButtonGroup.NORMAL);
		}
		btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
	}

	private void inicializaFiltrosGrupoProduto() throws SQLException {
		lbGrupoProduto1 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
		StringBuffer sb = new StringBuffer();
		lbGrupoProduto1 = new LabelName();
		if (!LavenderePdaConfig.ocultaGrupoProduto1) {
			sb.append(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
			cbGrupoProduto1.popupTitle = lbGrupoProduto1.getValue();
			if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
				Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
				cbGrupoProduto1.loadGrupoProduto1(null, fornecedorSelecionado);
			} else {
				cbGrupoProduto1.loadGrupoProduto1(null);
			}
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 1 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
			cbGrupoProduto2.popupTitle = Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2;
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
			}
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 2 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
			cbGrupoProduto3.popupTitle = Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3;
			lbGrupoProduto3 = new LabelName(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
			cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 3 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
			cbGrupoProduto4.popupTitle = Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4;
			cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());

		}
		String txtGruposProduto = sb.toString();
		loadLabelGruposProdutos(sb);
		if (sb.length() == 0) sb.append(txtGruposProduto);
		if (sb.indexOf(" / ") == 0) sb.delete(0, 2);
		lbGrupoProduto1.setText(sb.toString());
	}

	private void setaPosicoesHashMapbtGroupTipoFiltros (List<String> list){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_PA, i);
				continue;
			}
			else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_PA)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_PA, -1);
			}
			if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_AP, i);
				continue;
			}
			else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_AP)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_AP, -1);
			}
			if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_CD, i);
				continue;
			}
			else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_CD)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_CD, -1);
			}
			if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_DS, i);
				continue;
			}
			else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_DS)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_DS, -1);
			}
		}
		btGroupTipoFiltros.setSelectedIndex(list.size());
	}

	private void loadLabelGruposProdutos(StringBuffer sb) {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			sb.setLength(0);
			if (temp.length > 0 && lbGrupoProduto1 != null) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					sb.append(temp[0]);
					cbGrupoProduto1.popupTitle = temp[0];
				}
				if (temp.length > 1 && LavenderePdaConfig.usaFiltroGrupoProduto >= 2 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
					sb.append(" / ").append(temp[1]);
					cbGrupoProduto2.popupTitle = temp[1];
					if (temp.length > 2 && LavenderePdaConfig.usaFiltroGrupoProduto >= 3 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
						sb.append(" / ").append(temp[2]);
						cbGrupoProduto3.popupTitle = temp[2];
						if (temp.length > 3 && LavenderePdaConfig.usaFiltroGrupoProduto >= 4 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
							sb.append(" / ").append(temp[3]);
							cbGrupoProduto4.popupTitle = temp[3];
						}
					}
				}
			}
		}
	}

	private void addFiltrosGrupoProduto() {
		if (LavenderePdaConfig.usaFiltroGrupoProduto >= 1 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			UiUtil.add(this, lbGrupoProduto1, getLeft(), getNextY(), PREFERRED, PREFERRED);
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY(), FILL - (width / 2));
				UiUtil.add(this, cbGrupoProduto2, AFTER + WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
			}
			UiUtil.add(this, cbGrupoProduto3, getLeft(), getNextY(), FILL - (width / 2));
			UiUtil.add(this, cbGrupoProduto4, AFTER + WIDTH_GAP, SAME);
		} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
				UiUtil.add(this, cbGrupoProduto2, getLeft(), AFTER, FILL - (width / 2));
				UiUtil.add(this, cbGrupoProduto3, AFTER + WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
				UiUtil.add(this, cbGrupoProduto3, getLeft(), getNextY());
			}
		} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2 && LavenderePdaConfig.usaFiltroGrupoFaceamento) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
			}
			UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
		} else if ((LavenderePdaConfig.usaFiltroGrupoProduto == 1 && LavenderePdaConfig.usaFiltroGrupoFaceamento) && !LavenderePdaConfig.ocultaGrupoProduto1) {
			UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
		}
	}

	private void cbGrupoProduto2Change() throws SQLException {
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto1Change() throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}

    //@Override
    public String getEntityDescription() {
    	return Messages.FACEAMENTOESTOQUE_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return FaceamentoEstoqueService.getInstance();
    }

    //@Override	
    protected BaseDomain createDomain() throws SQLException {
        return new FaceamentoEstoque();
    }

    protected void visibleState() throws SQLException {
    	super.visibleState();
    	btExcluir.setVisible(false);
		btLeitorCamera.setVisible(LavenderePdaConfig.usaCameraParaLeituraCdBarras());
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        FaceamentoEstoque faceamentostoque = (FaceamentoEstoque)getDomain();
        return faceamentostoque;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException { }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {}

    //@Override
    protected void onFormStart() throws SQLException {
		boolean showAllFiltros = LavenderePdaConfig.isTodosFiltrosFixosTelaListaProduto();
		if (!LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoListaProduto()) {
			LavenderePdaConfig.carregaMapFiltrosProdutos(filtrosVisiveisMap, filtrosNaoAutomaticosMap, 0);
			boolean exibeFiltroAtributoProduto = LavenderePdaConfig.usaFiltroAtributoProduto && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_ATRIBUTO_PRODUTO) != null);
			boolean exibeFiltroGrupoProduto = (LavenderePdaConfig.usaFiltroGrupoProduto > 0 && LavenderePdaConfig.usaFiltroGrupoFaceamento) && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_GRUPOPRODUTO) != null);
			if (exibeFiltroGrupoProduto) {
				addFiltrosGrupoProduto();
				loadDefaultFilters();
			}
			if (exibeFiltroAtributoProduto) {
				UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_ATRIBUTO_OPCAO), cbAtributoProd, getLeft(), AFTER, FILL - (width / 2));
				UiUtil.add(this, cbAtributoOpcaoProd, AFTER + WIDTH_GAP, SAME);
			}
			if (LavenderePdaConfig.usaFiltroAtivoFaceamento) {
				if (!showAllFiltros) {
					UiUtil.add(this, btFiltroAvancado, RIGHT - WIDTH_GAP_BIG, getNextY() + HEIGHT_GAP, UiUtil.getControlPreferredHeight());
					UiUtil.add(this, btFiltrar, BEFORE - WIDTH_GAP, SAME, UiUtil.getControlPreferredHeight());
				} else {
					UiUtil.add(this, btFiltrar, RIGHT - WIDTH_GAP_BIG, getNextY(), UiUtil.getControlPreferredHeight());
				}
				int largEdFiltrar = width - btFiltrar.getX() - 1;
				if ((LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) || LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
					UiUtil.add(this, btGroupTipoFiltros, getLeft(), SAME, PREFERRED + 6, btFiltrar.getHeight());
					UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, FILL - largEdFiltrar, btFiltrar.getHeight());
				} else if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
					UiUtil.add(this, btTipoPesquisaEdFiltro, getLeft(), SAME, PREFERRED + 6, btFiltrar.getHeight());
					UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, FILL - largEdFiltrar, btFiltrar.getHeight());
				} else if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
					UiUtil.add(this, btGroupTipoFiltros, getLeft(), SAME, PREFERRED + 6, btFiltrar.getHeight());
					UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, FILL - largEdFiltrar, btFiltrar.getHeight());
				} else {
					UiUtil.add(this, edFiltro, getLeft(), SAME, FILL - largEdFiltrar, btFiltrar.getHeight());
				}
			}
		}
		createGridFaceamentoEstoque();
		carregaGridFaceamentoEstoque();
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			UiUtil.add(barBottomContainer, btLeitorCamera, 5);
		}
	}

    private void createGridFaceamentoEstoque() {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, LabelContainer.getStaticHeight());
    	//--
        GridColDefinition[] gridColDefiniton = LavenderePdaConfig.usaEdicaoSugestaoVenda() ? getGridColDefinitionComSugestaoRep() : getGridColDefinitionSemSugestaoRep();
        grid = UiUtil.createGridEdit(gridColDefiniton, false);
        UiUtil.add(this, grid);
        grid.setRect(LEFT, clienteContainer.getY2() + WIDTH_GAP, FILL, FILL - (barBottomContainer.getHeight() + HEIGHT_GAP));
        grid.setGridControllable(false);
        grid.setColumnEditableDouble(GRID_POS_ESTOQUE_ATUAL, true, 9);
        grid.useZeroAsEmpty = false;
        if (LavenderePdaConfig.usaEdicaoSugestaoVenda()) {
        	grid.setColumnEditableDouble(GRID_POS_SUGESTAO_VENDA_REP, true, 9);
        	grid.sortTypes = new int[] {Convert.SORT_STRING, Convert.SORT_STRING, Convert.SORT_STRING, Convert.SORT_DOUBLE, Convert.SORT_DOUBLE, Convert.SORT_DOUBLE, Convert.SORT_DOUBLE};
        } else {
        	grid.sortTypes = new int[] {Convert.SORT_STRING, Convert.SORT_STRING, Convert.SORT_STRING, Convert.SORT_DOUBLE, Convert.SORT_DOUBLE, Convert.SORT_DOUBLE};
        }
    }

	private GridColDefinition[] getGridColDefinitionComSugestaoRep() {
		GridColDefinition[] gridColDefiniton = {
        		new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
        		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_CDPRODUTO, 0, LEFT),
        		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -30, LEFT),
        		new GridColDefinition(Messages.FACEAMENTO_LABEL_QTPONTOEQUILIBRIO, -24, LEFT),
        		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_QTESTOQUEATUAL, -24, LEFT),
        		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_SUGESTAO_VENDA, -22, LEFT),
        		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_SUGESTAO_VENDA_REP, -22, LEFT)};
		return gridColDefiniton;
	}
	
	private GridColDefinition[] getGridColDefinitionSemSugestaoRep() {
		GridColDefinition[] gridColDefiniton = {
        		new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
        		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_CDPRODUTO, 0, LEFT),
        		new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -30, LEFT),
        		new GridColDefinition(Messages.FACEAMENTO_LABEL_QTPONTOEQUILIBRIO, -24, LEFT),
        		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_QTESTOQUEATUAL, -24, LEFT),
        		new GridColDefinition(Messages.FACEAMENTOESTOQUE_LABEL_SUGESTAO_VENDA, -22, LEFT)};
		return gridColDefiniton;
	}
    
    private void carregaGridFaceamentoEstoque() throws SQLException {
    	grid.clear();
    	grid.gridController.clearColors();
    	grid.gridController.clearDisables();
        grid.gridController.setColForeColor(LavendereColorUtil.baseForeColorSystem, GRID_POS_ESTOQUE_ATUAL);
        //--
		String cdGrupoProduto1 = cbGrupoProduto1.getValue();
		String cdGrupoProduto2 = cbGrupoProduto2.getValue();
        Faceamento faceamento = new Faceamento();
        faceamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
        faceamento.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
        faceamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		produtoList = ProdutoPdbxDao.getInstance().findAllCdProdutoWhereGrupoProdutoEmpresaRepresentanteCliente(cdGrupoProduto1, cdGrupoProduto2, null, null, faceamento.cdEmpresa, faceamento.cdRepresentante);
        faceamentoList = FaceamentoService.getInstance().findAllByExampleSummary(faceamento);
		if(dsFiltro != "") {
			produtoList = buscaProdutosPorPrincipioAtivoCodigoDescricao(TermoCorrecaoService.getInstance().getDsTermoCorrigido(dsFiltro), cdGrupoProduto1, cdGrupoProduto2, null, null, faceamento.cdEmpresa, faceamento.cdRepresentante);
		}
        int size = faceamentoList.size();
        if (size > 0) {
        	for (int i = 0; i < size; i++) {
        		faceamento = (Faceamento) faceamentoList.items[i];
        		//--
				for(int j = 0; j < produtoList.size(); j++) {
					if (faceamento.cdProduto.equals(produtoList.items[j])) {
						FaceamentoEstoque faceamentoEstoque = getBaseFaceamentoEstoque(faceamento.cdEmpresa, faceamento.cdRepresentante, faceamento.cdCliente, faceamento.cdProduto);
						faceamentoEstoque = (FaceamentoEstoque) FaceamentoEstoqueService.getInstance().findByRowKey(faceamentoEstoque.getRowKey());
						String[] item = LavenderePdaConfig.usaEdicaoSugestaoVenda() ? new String[7] : new String[6];
						//--
						item[0] = (faceamentoEstoque != null) ? StringUtil.getStringValue(faceamentoEstoque.flUltilizadoPedidoDtAtual) : "";
						item[1] = faceamento.cdProduto;
						item[2] = ProdutoService.getInstance().getDsProduto(faceamento.cdProduto);
						item[3] = StringUtil.getStringValueToInterface(faceamento.qtPontoEquilibrio);
						item[4] = (faceamentoEstoque != null) ? StringUtil.getStringValueToInterface(faceamentoEstoque.qtEstoqueAtual) : "";
						item[5] = (faceamentoEstoque != null) ? StringUtil.getStringValueToInterface(faceamentoEstoque.qtSugestaoVenda) : "";
						if (LavenderePdaConfig.usaEdicaoSugestaoVenda()) {
							item[6] = (faceamentoEstoque != null) ? StringUtil.getStringValueToInterface(faceamentoEstoque.qtSugestaoVendaRep) : "";
						}
						//--
						grid.add(item);
					}
				}
			}
        }
        grid.qsort(getSortIndex(), ValueUtil.getBooleanValue(sortAsc));
    }

	protected void filtrarClick() throws SQLException {
		btFiltrarClick(btFiltrar, null);
	}

	private void btFiltrarClick(Object target, String targetId) throws SQLException {
		if (targetId != null && filtrosNaoAutomaticosMap.get(targetId) != null) return;
		try {
			dsFiltro = edFiltro.getText();
			ProdutoUnidade produtoUnidade = null;
			if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(dsFiltro)) {
				produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras(dsFiltro);
				if (produtoUnidade != null) {
					dsFiltro = produtoUnidade.cdProduto;
				}
			}
			edFiltro.setText(dsFiltro);
			if (validateFiltro()) {
				if (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == ValorParametro.PARAM_INT_VALOR_ZERO && !isAlgumFiltroEspecialSelecionado() && isOrigemFiltrosEspeciais(target)) {
					grid.clear();
					produtoList.removeAllElements();
				} else {
					flListInicialized = true;
					list();
					showMessageFiltroQuantidadeResultados();
					if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
						edFiltro.setValue("");
					}
				}
			} else {
				if (!isOrigemFiltrosEspeciais(target)) {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
				}
				grid.clear();
				produtoList.removeAllElements();
			}
		} finally {
			setFocusInListContainer();
		}
	}

    public void reposition() {
    	super.reposition();
    }
    
    private int getSortIndex() {
    	if (sortAttribute.equalsIgnoreCase("CDPRODUTO")) {
    		return 1;
    	} else if (sortAttribute.equalsIgnoreCase("DSPRODUTO")) {
    		return 2;
    	} else if (sortAttribute.equalsIgnoreCase("QTPONTOEQUILIBRIO")) {
    		return 3;
    	}
    	return -1;
    }

	protected void insertOrUpdate(BaseDomain domain) throws SQLException {
		int size = faceamentoList.size();
		for (int i = 0; i < size; i++) {
			Faceamento faceamento = (Faceamento) faceamentoList.items[i];
			FaceamentoEstoque faceamentoEstoque = getBaseFaceamentoEstoque(faceamento.cdEmpresa, faceamento.cdRepresentante, faceamento.cdCliente, faceamento.cdProduto);
			faceamentoEstoque.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
			if (faceamentoEstoqueMap.get(faceamentoEstoque.getRowKey()) == null) {
				faceamentoEstoque.qtEstoqueAtual = 0;
				faceamentoEstoque.flUltilizadoPedidoDtAtual = ValueUtil.VALOR_NAO;
				faceamentoEstoqueMap.put(faceamentoEstoque.getRowKey(), faceamentoEstoque);
			}
		}
		for (Map.Entry<String, FaceamentoEstoque> entry : faceamentoEstoqueMap.entrySet()) {
			FaceamentoEstoque faceamentoEstoqueFilter = entry.getValue();
			int count = FaceamentoEstoqueService.getInstance().countByExample(faceamentoEstoqueFilter);
			if (count > 0) {
				update(faceamentoEstoqueFilter);
			} else {
				insert(faceamentoEstoqueFilter);
			}
		}

	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btLeitorCamera) {
					destacaItemGrid();
				} else if (event.target == cbGrupoProduto1) {
					cbGrupoProduto1Change();
					btFiltrarClick(cbGrupoProduto1, NU_FILTRO_GRUPOPRODUTO);
					carregaGridFaceamentoEstoque();
				} else if (event.target == cbGrupoProduto2) {
					cbGrupoProduto2Change();
					btFiltrarClick(cbGrupoProduto2, NU_FILTRO_GRUPOPRODUTO);
					carregaGridFaceamentoEstoque();
				} else if (event.target == btFiltrar) {
					btFiltrarClick(btFiltrar, dsFiltro);
					carregaGridFaceamentoEstoque();
				} else if (event.target == btGroupTipoFiltros) {
					btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target instanceof EditNumberFrac) {
					storeAndHighlightFaceamento(event);
				}
				break;
			}
			case ControlEvent.FOCUS_IN:
				if (event.target instanceof EditNumberFrac) {
					lastSelectedXYindex = grid.getSelectedXYIndex();
				}
				break;
			case GridEditEvent.COLUMN_PRESSED: {
				preserveGridColors(event);
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				if (event.target == edFiltro && ((KeyEvent)event).isActionKey()) {
					btFiltrarClick(btFiltrar, dsFiltro);
					if (grid != null && grid.size() == 0) {
						edFiltro.requestFocus();
					}
					grid.requestFocus();
					carregaGridFaceamentoEstoque();
				}
				break;
			}
		}
	}

	private void storeAndHighlightFaceamento(Event event) {
		EditNumberFrac ed = (EditNumberFrac) event.target;
		BaseGridEdit baseGridEdit = (BaseGridEdit) ed.getParent();
		int selectedIndex = baseGridEdit.getSelectedIndex();
		String[] items = baseGridEdit.getItem(selectedIndex);
		baseGridEdit.getSelectedXYIndex();
		FaceamentoEstoque faceamentoEstoque = getBaseFaceamentoEstoque(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.getCliente().cdCliente, items[GRID_POS_CDPRODUTO]);
		if (lastSelectedXYindex[1] == GRID_POS_ESTOQUE_ATUAL) {
			faceamentoEstoque.qtEstoqueAtual = ed.getValueDouble();
		} else {
			faceamentoEstoque.qtSugestaoVendaRep = ed.getValueDouble();
		}
		faceamentoEstoque.vlFatorFaceamento = findVlFatorFaceamento();
		double pontoEquilibrio = Messages.MOEDA.equals(Messages.MOEDA_REAL_RS) ? ValueUtil.getDoubleValue(items[3]) : ValueUtil.getDoubleValueSeparador(items[3]);
		faceamentoEstoque.qtSugestaoVenda = FaceamentoEstoqueService.getInstance().calculaSugestaoVenda(faceamentoEstoque.qtEstoqueAtual, faceamentoEstoque.vlFatorFaceamento, pontoEquilibrio);
		faceamentoEstoque.flUltilizadoPedidoDtAtual = ValueUtil.VALOR_SIM.equals(items[GRID_POS_FL_TIPO_ALTERACAO]) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		baseGridEdit.setCellText(selectedIndex, GRID_POS_SUGESTAO_VENDA, StringUtil.getStringValueToInterface(faceamentoEstoque.qtSugestaoVenda));
		faceamentoEstoqueMap.put(faceamentoEstoque.getRowKey(), faceamentoEstoque);
		grid.hideControl();
		int color = grid.getSelectedIndex() % 2 == 1 ? Color.darker(LavendereColorUtil.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO, 20) : LavendereColorUtil.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO;
		grid.gridController.setRowBackColor(color, grid.getSelectedIndex());
	}
	
	private double findVlFatorFaceamento() {
		try {
			return EmpresaService.getInstance().findVlFatorFaceamento();
		} catch (SQLException ex) {
			ExceptionUtil.handle(ex);
			return 0;
		}
	}

	private void preserveGridColors(Event event) {
		BaseGridEdit baseGridEdit = (BaseGridEdit) event.target;
		grid.gridController.clearColors();
		Vector items = baseGridEdit.getItemsVector();
		for (int i = 0; i < items.size(); i++) {
			String[] item = (String[]) items.elementAt(i);
			FaceamentoEstoque faceamentoEstoque = getBaseFaceamentoEstoque(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.getCliente().cdCliente, item[GRID_POS_CDPRODUTO]);
			if (faceamentoEstoqueMap.get(faceamentoEstoque.getRowKey()) != null) {
				grid.setSelectedIndex(i);
				int color = grid.getSelectedIndex() % 2 == 1 ? Color.darker(LavendereColorUtil.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO, 20) : LavendereColorUtil.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO;
				grid.gridController.setRowBackColor(color, grid.getSelectedIndex());
			}
			grid.setSelectedIndex(-1);
		}
	}

	private FaceamentoEstoque getBaseFaceamentoEstoque(String cdEmpresa, String cdRepresentante, String cdCliente, String item2) {
		FaceamentoEstoque faceamentoEstoque = new FaceamentoEstoque();
		faceamentoEstoque.cdEmpresa = cdEmpresa;
		faceamentoEstoque.cdRepresentante = cdRepresentante;
		faceamentoEstoque.cdCliente = cdCliente;
		faceamentoEstoque.cdProduto = item2;
		faceamentoEstoque.dtCadastro = DateUtil.getCurrentDate();
		return faceamentoEstoque;
	}

	private void destacaItemGrid() throws SQLException {
		Produto produto = realizaLeituraCamera();
		if (produto == null) return; 
		Vector listItens = grid.getItemsVector();
		for (int i = 0; i < listItens.size(); i++) {
			String[] valores = (String[]) listItens.items[i];
			if (!ValueUtil.valueEquals(produto.cdProduto, valores[1])) continue;
			grid.setSelectedIndex(i);
			grid.setPendownCol(GRID_POS_ESTOQUE_ATUAL);
			grid.exibeControleGrid(i, GRID_POS_ESTOQUE_ATUAL);
			break;
		}
	}
	
	private Produto realizaLeituraCamera() throws SQLException {
		Produto produtoFilter = new Produto();
		String cdBarras = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, "");
		produtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(getClass());
		produtoFilter.nuCodigoBarras = cdBarras;
		Produto produto = (Produto) ProdutoService.getInstance().findProdutoByCdBarras(produtoFilter);
		if (produto.cdProduto == null) {
			boolean resposta = UiUtil.showConfirmYesNoMessage(Messages.MSG_PRODUTO_NAO_ENCONTRADO_PARA_CODIGO_BARRAS);
			if (resposta) 
				realizaLeituraCamera();
			else
				return null;
		}
		return produto;
	}

	private Vector buscaProdutosPorPrincipioAtivoCodigoDescricao(String dsFiltroTermoCorrigido, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, String empresa, String representante) throws SQLException {
		Produto produtoFilter = new Produto();
		produtoFilter.dsProduto = dsFiltroTermoCorrigido;
		if(filterByPrincipioAtivo) {
			produtoFilter.dsPrincipioAtivo = dsFiltroTermoCorrigido;
		}
		if(filterByCodigoProduto) {
			produtoFilter.cdProduto = dsFiltroTermoCorrigido;
		}
		produtoList = ProdutoService.getInstance().findProdutoByDsPrincipioAtivoDsProduto(produtoFilter, cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3, cdGrupoProduto4, empresa, representante);
		return produtoList;
	}

	private boolean validateFiltro() {
		String filtro = dsFiltro;
		if (LavenderePdaConfig.usaPesquisaInicioString && dsFiltro.startsWith("*")) {
			filtro = dsFiltro.substring(1);
		}
		if ((filtro == null) || ((filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto) && !isAlgumFiltroEspecialSelecionado())) {
			return false;
		}
		return true;
	}

	private boolean isAlgumFiltroEspecialSelecionado() {
		return !cbFornecedor.isValueSelectedEmpty() ||
				!cbAtributoOpcaoProd.isValueSelectedEmpty() ||
				!cbGrupoProduto1.isValueSelectedEmpty() ||
				(!cbGrupoProduto2.isValueSelectedEmpty() && LavenderePdaConfig.ocultaGrupoProduto1);
	}

	private boolean isOrigemFiltrosEspeciais(final Object target) {
		return target == cbFornecedor || target == cbGrupoProduto1 || target == cbAtributoProd || target == cbAtributoOpcaoProd;
	}

	private boolean apresentaMensagemNuLimiteRegistrosBuscaSistema() {
		return (LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && qtdProdutos == LavenderePdaConfig.nuLinhasRetornoBuscaSistema) && LavenderePdaConfig.apresentaMensagemLimiteNuLinhasRetornoBuscaSistema;
	}

	private void showMessageFiltroQuantidadeResultados() {
		if (apresentaMensagemNuLimiteRegistrosBuscaSistema()) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_QUANTIDADE_RESULTADOS, LavenderePdaConfig.nuLinhasRetornoBuscaSistema));
		}
	}

	public void setFocusInListContainer() {
		grid.requestFocus();
		showRequestedFocus();
	}

	public void loadDefaultFilters() throws SQLException {
		//GrupoProduto
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
			cbGrupoProduto1.setSelectedIndex(0);
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				cbGrupoProduto2.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				cbGrupoProduto3.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				cbGrupoProduto4.setSelectedIndex(0);
			}
		}
	}

	private void btGroupTipoFiltrosClick(int indexSelected) {
		filterByPrincipioAtivo = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_PA);
		filterByAplicacaoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_AP);
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			filterByCodigoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_CD);
		}
		alterarTipoTeclado();
	}

	private void alterarTipoTeclado() {
		if (filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
		} else {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_TEXT);
		}
	}

}
