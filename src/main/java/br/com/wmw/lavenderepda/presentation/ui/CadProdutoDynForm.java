package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustria;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustriaGeral;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevisto;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LocalEstoque;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueIndustriaGeralService;
import br.com.wmw.lavenderepda.business.service.EstoqueIndustriaService;
import br.com.wmw.lavenderepda.business.service.EstoquePrevistoGeralService;
import br.com.wmw.lavenderepda.business.service.EstoquePrevistoService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.LocalEstoqueService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.EstoqueLabelButton;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import totalcross.sys.Convert;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadProdutoDynForm extends BaseLavendereCrudPersonCadForm {

	private static final String NM_TAB_PRINCIPAL = "Principal";

	private EstoqueLabelButton lbQtEstoque;
    private LabelValue lbDsSituacao;
    private LabelValue lbQtEstoquePrevisto;
	private LabelValue lbQtEstoqueAtual;
	private LabelValue lbQtEstoqueTotal;
	private LabelValue lbQtEstoqueInd;
    private LabelValue lbDtEstoquePrevisto;
    private LabelValue lbVlVolume;
	private EditMemo memoObservacaoRestricao;
    private EditMemo memoCodAlternativo;
    private EditMemo memoAplicacaoProduto;
	private LabelName lbItemGrade1;
	private LabelName lbEstoque;
	private LabelValue lbVlItemGrade1;
	private Control lbMarcadores;
	private int iconSize;
    public ItemTabelaPreco itemTabelaPreco;
    public Pedido pedido;

    private int tabPanelDescontoQtd;

    protected BaseGridEdit gridEdit;

    private LabelValue edDsMoedaProduto;
    
    private double qtEstoqueTotal;

    public CadProdutoDynForm() {
        super(Messages.PRODUTO_NOME_ENTIDADE);
        lbQtEstoque = new EstoqueLabelButton(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO));
        lbQtEstoquePrevisto = new LabelValue("9999999999");
		lbQtEstoqueAtual =  new LabelValue("9999999999");
		lbQtEstoqueTotal =  new LabelValue("9999999999");
		lbQtEstoqueInd =  new LabelValue("9999999999");
        lbDtEstoquePrevisto = new LabelValue("@@@@@@@@@@");
        lbDsSituacao = new LabelValue("@@@@@@@@@@");
		lbItemGrade1 = new LabelName(" ");
	    lbEstoque = new LabelName(LavenderePdaConfig.isUsaLocalEstoque() ? Messages.ESTOQUE_QTD_ESTOQUE_TOTAL : Messages.ESTOQUE_NOME_ENTIDADE);
		lbVlItemGrade1 = new LabelValue(" ");
		lbVlVolume = new LabelValue("@");
        itemTabelaPreco = new ItemTabelaPreco();    
        memoCodAlternativo = new EditMemo("@", 5, 500);
        memoCodAlternativo.setEnabled(false);
        memoCodAlternativo.drawBackgroundWhenDisabled = true;      
        memoAplicacaoProduto = new EditMemo("@", 10, 40000);
        memoAplicacaoProduto.setEnabled(false);
        memoAplicacaoProduto.drawBackgroundWhenDisabled = true;
	    memoObservacaoRestricao = new EditMemo("@", 4, 500);
	    memoObservacaoRestricao.setEnabled(true);
	    memoObservacaoRestricao.setEditable(false);
	    memoObservacaoRestricao.drawBackgroundWhenDisabled = false;
        memoAplicacaoProduto.drawBackgroundWhenDisabled = true;    
        iconSize = lbDsSituacao.getPreferredHeight();
        edDsMoedaProduto = new LabelValue("@");
        setReadOnly();
    }


	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		memoCodAlternativo.getScroolBar().setEnabled(true);
		memoAplicacaoProduto.getScroolBar().setEnabled(true);
	}
    
    @Override
    protected void setActiveTabInitial() {
		if (LavenderePdaConfig.isMostraConfigTelaInfoProduto()) {
			int indexTab = tabDinamica.getIndexTab(LavenderePdaConfig.getConfigTelaInfoProdutoValue());
			if (indexTab != -1) {
				tabDinamica.setActiveTab(indexTab);
				tabDinamica.requestFocus();
			} else {
				super.setActiveTabInitial();
			}
		} else {
			super.setActiveTabInitial();
		}
    }

	//@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ProdutoService.getInstance();
    }

	protected String getDsTable() {
		return Produto.TABLE_NAME;
	}

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Produto();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        return (Produto) getDomain();
    }

    private boolean isEstoquePrevistoDisplayable() {
		return LavenderePdaConfig.getConfigGradeProduto() == 0 || LavenderePdaConfig.usaGradeProduto5();
    }
    
    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Produto produto = (Produto) domain;
    	super.domainToScreen(domain);
    	if (isEstoquePrevistoDisplayable()) {
    		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
    			lbQtEstoque.setValue(LoteProdutoService.getInstance().getQtEstoqueLoteDisponivel(produto.cdProduto));
    		} else {
    			Estoque estoque = new Estoque();
    			if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
    				estoque = EstoqueService.getInstance().getEstoqueCentroCusto(produto);
    			} else {
    				estoque = EstoqueService.getInstance().getEstoqueIgnoraLocal(produto.cdProduto);
    			}
    			
    			if (estoque.flErroEstoqueOnline) {
    				lbQtEstoque.setText("");
    			} else {
    				lbQtEstoque.setValue(ValueUtil.getDoubleValue(EstoqueService.getInstance().getEstoqueToString(estoque.qtEstoque)));
    			}
    			lbDtEstoquePrevisto.setValue(estoque.dtEstoquePrevisto);
    			lbQtEstoquePrevisto.setValue(EstoqueService.getInstance().getEstoquePrevistoToString(estoque.qtEstoquePrevisto));
    			qtEstoqueTotal += estoque.qtEstoquePrevisto;
    			if (LavenderePdaConfig.mostraEstoquePrevisto && estoque.qtEstoquePrevisto > 0) {
    				try {
	    				TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
						CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, 194);
						lbQtEstoque.setForeColor(Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB));
    				} catch (Throwable e) {
    					lbQtEstoque.setForeColor(ColorUtil.softGreen);
    				}
    			}
    		}
			lbQtEstoqueAtual.setValue(EstoqueService.getInstance().getEstoqueToString(lbQtEstoque.getDoubleValue()));
		} else {
			Estoque estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
			if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
				EstoqueService.getInstance().setEstoqueItemComParcialPrevisto(estoque);
			}
			lbQtEstoqueAtual.setValue(EstoqueService.getInstance().getEstoqueToString(estoque.qtEstoque));
    	}
        //--
        lbDsSituacao.setValue(ProdutoBloqueadoService.getInstance().getDsSituacao(produto, itemTabelaPreco));
        //--
        if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido  && !LavenderePdaConfig.isOcultaInfoPreco()) {
            tabDinamica.setContainer(tabPanelDescontoQtd, new ListDescQuantidadeForm(produto.cdProduto));
        } 
        if (LavenderePdaConfig.mostraEstoquePrevisto) {
	        if (ValueUtil.isEmpty(lbQtEstoquePrevisto.getText())) {
	        	lbQtEstoquePrevisto.setForeColor(ColorUtil.softRed);
	        } else {
	        	lbQtEstoquePrevisto.setForeColor(ColorUtil.componentsForeColor);
	        }
        }
		if (LavenderePdaConfig.isConfigGradeProduto()) {
    		lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(((Produto)getDomain()).cdTipoItemGrade1Temp));
    		lbVlItemGrade1.setValue(ItemGradeService.getInstance().getDsItemGrade(((Produto)getDomain()).cdTipoItemGrade1Temp, ((Produto)getDomain()).cdItemGrade1Temp));
        }
        if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
        	lbVlVolume.setText(StringUtil.getStringValueToInterface(produto.getVlVolumeArrendondado(), LavenderePdaConfig.nuCasasDecimaisVlVolume));
        }
		if (LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			if (ValueUtil.isEmpty(produto.cdEmpresa)) {
				produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			}
			if (ValueUtil.isEmpty(produto.cdRepresentante)) {
				produto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			}
			String codigos = StringUtil.getStringValue(ProdutoPdbxDao.getInstance().findColumnByRowKey(produto.getRowKey(), "dsCodigoAlternativo"));
			if (ValueUtil.isNotEmpty(codigos)) {
				codigos = codigos.replaceAll(";", " / ");
				memoCodAlternativo.setText(codigos);
			} else {
				memoCodAlternativo.setText("");
			}
		}
		
		if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			if (ValueUtil.isEmpty(produto.cdEmpresa)) {
				produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			}
			if (ValueUtil.isEmpty(produto.cdRepresentante)) {
				produto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			}
			String codigos = StringUtil.getStringValue(ProdutoPdbxDao.getInstance().findColumnByRowKey(produto.getRowKey(), "dsAplicacaoProduto"));
			if (ValueUtil.isNotEmpty(codigos)) {
				codigos = codigos.replaceAll(";", " / ");
				memoAplicacaoProduto.setText(codigos);
			} else {
				memoAplicacaoProduto.setText("");
			}
		}
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			if (RestricaoService.getInstance().isProdutoRestrito(produto.cdProduto, null, null, 1) != null) {
				memoObservacaoRestricao.setValue(Messages.RESTRICAO_VENDA_CLIENTE_PRODUTO_RESTRITO_VENDA_ALGUNS_CLIENTES);
			}
		}
		if (LavenderePdaConfig.exibeGridEstoquePrevisto() || LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			carregaGridEstoquePrev(produto);
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto && produto.dsMoedaProduto != null) {
			edDsMoedaProduto.setText(produto.dsMoedaProduto);
		}
    }

    protected void addTabsFixas(Vector tableTitles) {
    	if (tableTitles.size() == 0) {
    		tableTitles.addElement(NM_TAB_PRINCIPAL);
    	}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && !LavenderePdaConfig.isOcultaInfoPreco()) {
			String descontoQuantidadeNomeEntidade = Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE;
			int indexDescQtde = tableTitles.indexOf(descontoQuantidadeNomeEntidade);
	    	if (indexDescQtde == -1) {
	    		tableTitles.addElement(descontoQuantidadeNomeEntidade);
	    		tabPanelDescontoQtd = tableTitles.size() - 1;
	    	} else {
	    		tabPanelDescontoQtd = indexDescQtde;
	    	}
		}
		if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			tableTitles.addElement(Messages.PRODUTO_LABEL_TAB_APLICACAO_PRODUTO);
		}
		if (LavenderePdaConfig.exibeGridEstoquePrevisto() || LavenderePdaConfig.mostraEstoqueCorrente || LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			tableTitles.addElement(Messages.PRODUTO_LABEL_ESTOQUE_PREV);
		}
    }

	protected void addComponentesFixosFim() {
		UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTO_LABEL_FLSITUACAO), lbDsSituacao, getLeft(), AFTER + HEIGHT_GAP);
		if (ValueUtil.valueEquals("9999999999", lbQtEstoqueTotal.getValue())) {
			lbQtEstoqueTotal.setValue("0");
		}
		//--
		if (!LavenderePdaConfig.isOcultaEstoque() && !LavenderePdaConfig.usaLocalEstoque() && isEstoquePrevistoDisplayable()
				&& !(LavenderePdaConfig.usaControleEstoquePorRemessa) && !LavenderePdaConfig.mostraEstoqueCorrente && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			UiUtil.add(getContainerPrincipal(), lbEstoque, lbQtEstoque, getLeft(), AFTER + HEIGHT_GAP, getWFill());
			if (LavenderePdaConfig.mostraEstoquePrevisto && !LavenderePdaConfig.exibeGridEstoquePrevisto()) {
				UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTO_LABEL_ESTOQUEPREVISTO), lbQtEstoquePrevisto, getLeft(), AFTER  + HEIGHT_GAP);
				UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTO_LABEL_DT_ESTOQUEPREVISTO), lbDtEstoquePrevisto, getLeft(), AFTER  + HEIGHT_GAP);
			}
			lbQtEstoque.setGradeEstoqueButtonVisibility(!LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isConfigGradeProduto());
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PEDIDO_LABEL_VLTOTAL_VOLUME), lbVlVolume, getLeft(), AFTER  + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTO_LABEL_CODIGO_ALTERNATIVO), memoCodAlternativo, getLeft(), AFTER + HEIGHT_GAP, getWFill(), 100);
		}
		if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			Container container = tabDinamica.getContainer(ValueUtil.getIntegerValue((String) hashTabs.get(Messages.PRODUTO_LABEL_TAB_APLICACAO_PRODUTO))); 
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_APLICACAO_PRODUTO), memoAplicacaoProduto, getLeft(),  AFTER + HEIGHT_GAP, getWFill(), FILL - HEIGHT_GAP_BIG);
		}
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			UiUtil.add(getContainerPrincipal(), new LabelName(Messages.RESTRICAO_VENDA_CLIENTE_PRODUTO_LABEL_OBSERVACAO), memoObservacaoRestricao, getLeft(), AFTER + HEIGHT_GAP, getWFill());
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			Container container = tabDinamica.getContainer(ValueUtil.getIntegerValue((String) hashTabs.get(NM_TAB_PRINCIPAL)));
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_DSMOEDAPRODUTO), edDsMoedaProduto, getLeft(), AFTER + HEIGHT_GAP, getWFill());
		}
		gridEstoquePrevisto();
	}
	
	private void gridEstoquePrevisto() {
		if (!LavenderePdaConfig.exibeGridEstoquePrevisto() && !LavenderePdaConfig.mostraEstoqueCorrente && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			return;
		}
		Container container = tabDinamica.getContainer(ValueUtil.getIntegerValue((String) hashTabs.get(Messages.PRODUTO_LABEL_ESTOQUE_PREV)));
		if (ValueUtil.valueEquals("9999999999", lbQtEstoqueTotal.getValue())) {
			lbQtEstoqueTotal.setValue("0");
		}
		UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUEPREVISTO), lbQtEstoquePrevisto, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.mostraEstoqueCorrente && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUE_TOTAL), lbQtEstoqueTotal, getRight(), BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUE_IND), lbQtEstoqueInd, getLeft(), getNextY() + HEIGHT_GAP_BIG);
		} else if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && LavenderePdaConfig.mostraEstoqueCorrente) {
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUE_TOTAL), lbQtEstoqueTotal, getRight(), BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUEPREVISTO_ATUAL), lbQtEstoqueAtual, getLeft(), getNextY() + HEIGHT_GAP_BIG);
		} else {
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_ESTOQUE_ATUAL), lbQtEstoqueAtual, getRight(), BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(container, new LabelName(Messages.PRODUTO_LABEL_DT_ESTOQUEPREVISTO), lbDtEstoquePrevisto, getLeft(), getNextY() + HEIGHT_GAP_BIG);
		}
		UiUtil.add(container, new LabelName(), lbDtEstoquePrevisto, getLeft(), AFTER  + HEIGHT_GAP);
		
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(Messages.PRODUTO_LABEL_DT_ESTOQUEPREVISTO, fm.stringWidth("0000000000000"), LEFT),
											    new GridColDefinition(Messages.PRODUTO_LABEL_ESTOQUE_PREV_QUANTIDADE, fm.stringWidth("000000000"), LEFT),
											    new GridColDefinition(Messages.PRODUTO_LABEL_ESTOQUE_PREV_LOCAL_ESTOQUE, fm.stringWidth("00000000"), LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
    	gridEdit.sortTypes[1] = LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto ? Convert.SORT_INT : Convert.SORT_STRING;
        UiUtil.add(container, gridEdit, LEFT, lbDtEstoquePrevisto.getY2() + HEIGHT_GAP_BIG, FILL, FILL - UiUtil.getLabelPreferredHeight());
	}
	
	private void carregaGridEstoquePrev(Produto produto) throws SQLException {
		gridEdit.clear();
		qtEstoqueTotal = 0;
		lbQtEstoquePrevisto.setValue(0);
		lbQtEstoqueTotal.setValue(0);
		lbDtEstoquePrevisto.setValue("");
		Vector estoquePrevistoList = new Vector();
		double qtEstoquePrevisto = 0;
		double qtEstoquePrevistoMaisPrx = 0;
		double qtEstoqueIndustria = 0;
		double qtEstoqueIndustriaGeral = 0;
		if (LavenderePdaConfig.exibeGridEstoquePrevisto() || LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			if (LavenderePdaConfig.isUsaReservaEstoqueCorrente() || (LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && LavenderePdaConfig.mostraEstoqueCorrente)) {
				EstoquePrevistoGeral estoquePrevistoGeralFilter = new EstoquePrevistoGeral(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto, new Date());
				estoquePrevistoGeralFilter.dtEstoque = new Date();
				if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
					estoquePrevistoGeralFilter.cdLocalEstoque = pedido == null ? null : pedido.getCdLocalEstoque();
				}
				if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto()) {
					CentroCusto centroCusto = new CentroCusto();
					centroCusto.cdEmpresa = produto.cdEmpresa;
					centroCusto.cdRepresentante = produto.cdRepresentante;
					centroCusto.flOcultaEstListProd = "N";
					Vector listCentroCusto;
					if (pedido != null) {
						centroCusto.cdCliente = pedido.cdCliente;
						listCentroCusto = CentroCustoService.getInstance().findColumnValuesByExampleJoinPlataformaVendaCli(centroCusto, "cdLocalEstoque");
					} else {
						listCentroCusto = CentroCustoService.getInstance().findColumnValuesByExample(centroCusto, "cdLocalEstoque");
					}
					estoquePrevistoGeralFilter.cdLocalEstoqueList = (String[]) listCentroCusto.toObjectArray();
				}
				estoquePrevistoGeralFilter.dtEstoque = null;
				estoquePrevistoGeralFilter.cdItemGrade1 = null;
				estoquePrevistoGeralFilter.cdItemGrade2 = null;
				estoquePrevistoGeralFilter.cdItemGrade3 = null;
				estoquePrevistoGeralFilter.flEstoqueFisico = "S";
				estoquePrevistoList = EstoquePrevistoGeralService.getInstance().findAllByExample(estoquePrevistoGeralFilter);
				
				estoquePrevistoGeralFilter.dtEstoque = new Date();
				estoquePrevistoGeralFilter.flEstoqueFisico = "N";
				Vector estoquePrevistoListAux = EstoquePrevistoGeralService.getInstance().findAllNaoVencidosSomados(estoquePrevistoGeralFilter);
				int size = estoquePrevistoListAux.size();
				for (int i = 0; i < size; i++) {
					EstoquePrevistoGeral estoque = (EstoquePrevistoGeral) estoquePrevistoListAux.items[i];
					estoquePrevistoList.addElement(estoque);
				}
				size = estoquePrevistoList.size();
				for (int i = 0; i < size; i++) {
					EstoquePrevistoGeral estoque = (EstoquePrevistoGeral) estoquePrevistoList.items[i];
					if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && ValueUtil.valueEquals(estoque.flEstoqueFisico, ValueUtil.VALOR_SIM)) {
						qtEstoquePrevistoMaisPrx += estoque.qtEstoque;
					}
					qtEstoquePrevisto += estoque.qtEstoque;
					lbQtEstoquePrevisto.setValue(EstoqueService.getInstance().getEstoquePrevistoToString(qtEstoquePrevisto));

				}
			} else if (!LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
				EstoquePrevisto estoquePrevistoFilter = new EstoquePrevisto(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto);
				estoquePrevistoFilter.dtEstoque = new Date();
				estoquePrevistoList = EstoquePrevistoService.getInstance().findAllNaoVencidos(estoquePrevistoFilter);
				int size = estoquePrevistoList.size();
				for (int i = 0; i < size; i++) {
					EstoquePrevisto estoque = (EstoquePrevisto) estoquePrevistoList.items[i];
					qtEstoquePrevisto += estoque.qtEstoque;
					lbQtEstoquePrevisto.setValue(EstoqueService.getInstance().getEstoquePrevistoToString(qtEstoquePrevisto));

				}
			}
		}
		qtEstoqueTotal += qtEstoquePrevisto;
		if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && LavenderePdaConfig.mostraEstoqueCorrente) {
			lbQtEstoqueAtual.setValue(EstoqueService.getInstance().getEstoqueToString(qtEstoquePrevisto - qtEstoquePrevistoMaisPrx));
			lbQtEstoquePrevisto.setValue(EstoqueService.getInstance().getEstoquePrevistoToString(qtEstoquePrevistoMaisPrx));
		}
		if (LavenderePdaConfig.mostraEstoqueCorrente && !LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			buscarEstoqueIndustria(produto, estoquePrevistoList, qtEstoqueIndustria, qtEstoqueIndustriaGeral);
		}
		if (ValueUtil.isEmpty(estoquePrevistoList)) {
			return;
		}
		lbQtEstoqueTotal.setText(EstoqueService.getInstance().getEstoqueToString(qtEstoqueTotal));
		gridEdit.setItems(carregaGridItemsEstoquePrev(estoquePrevistoList));
	}
	
	private void buscarEstoqueIndustria(Produto produto, Vector estoquePrevistoList, double qtEstoqueIndustria, double qtEstoqueIndustriaGeral) throws SQLException {
		EstoqueIndustria estoqueIndustriaFilter = new EstoqueIndustria(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto);
		estoqueIndustriaFilter.dtEstoque = new Date();

		Vector estoqueIndustriaFilterList = EstoqueIndustriaService.getInstance().findAllNaoVencidos(estoqueIndustriaFilter);
		estoquePrevistoList.addElementsNotNull(estoqueIndustriaFilterList.items);

		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			EstoqueIndustriaGeral estoqueIndustriaGeralFilter = new EstoqueIndustriaGeral(produto.cdEmpresa,  produto.cdProduto, produto.cdRepresentante);
			estoqueIndustriaGeralFilter.dtEstoque = new Date();
			Vector estoqueIndustriaGeralFilterList  = EstoqueIndustriaGeralService.getInstance().findAllNaoVencidosSomados(estoqueIndustriaGeralFilter);

			for (int i = 0; i < estoqueIndustriaGeralFilterList.size(); i++) {
				EstoqueIndustriaGeral estoqueGeral = (EstoqueIndustriaGeral) estoqueIndustriaGeralFilterList.items[i];

				boolean temCorrespondente = false;

				for (int x = 0; x < estoqueIndustriaFilterList.size(); x++) {
					EstoqueIndustria estoque = (EstoqueIndustria) estoqueIndustriaFilterList.items[x];
					if (ValueUtil.valueEquals(estoque.cdInsumo, estoqueGeral.cdInsumo)
						&& ValueUtil.valueEquals(estoque.dtEstoque, estoqueGeral.dtEstoque)
						&& ValueUtil.valueEquals(estoque.cdLocalEstoque, estoqueGeral.cdLocalEstoque)) {
						estoque.qtEstoque += estoqueGeral.qtEstoque;
						temCorrespondente = true;
					}
				}

				if (!temCorrespondente) {
					qtEstoqueIndustriaGeral += estoqueGeral.qtEstoque;
					estoquePrevistoList.addElement(estoqueGeral);
				}
			}

			int size = estoqueIndustriaFilterList.size();
			for (int x = 0; x < size; x++) {
				EstoqueIndustria estoque = (EstoqueIndustria) estoqueIndustriaFilterList.items[x];
				qtEstoqueIndustria += estoque.qtEstoque;
			}

		} else {
			int size = estoqueIndustriaFilterList.size();
			for (int i = 0; i < size; i++) {
				EstoqueIndustria estoque = (EstoqueIndustria) estoqueIndustriaFilterList.items[i];
				qtEstoqueIndustria += estoque.qtEstoque;
			}
		}

		qtEstoqueTotal += qtEstoqueIndustria;
		qtEstoqueTotal += qtEstoqueIndustriaGeral;
		lbQtEstoqueInd.setText(StringUtil.getStringValueToInterface(qtEstoqueIndustria + qtEstoqueIndustriaGeral));
	}

	private String[][] carregaGridItemsEstoquePrev(Vector estoquePrevistoList) throws SQLException {
    	int size = estoquePrevistoList.size();
    	int qtColumns = 3;
		String[][] gridItems = new String[size][qtColumns];
		for (int i = 0; i < size; i++) {
			String[] item = new String[qtColumns];
			if (estoquePrevistoList.items[i] instanceof EstoquePrevisto) {
			EstoquePrevisto estoquePrevisto = (EstoquePrevisto) estoquePrevistoList.items[i];
				polulaItemGrid(item, estoquePrevisto.qtEstoque, estoquePrevisto.dtEstoque, estoquePrevisto.cdEmpresa, estoquePrevisto.cdRepresentante, estoquePrevisto.cdLocalEstoque);
			} else if (estoquePrevistoList.items[i] instanceof EstoquePrevistoGeral) {
				EstoquePrevistoGeral estoquePrevistoGeral = (EstoquePrevistoGeral) estoquePrevistoList.items[i];
				polulaItemGrid(item, estoquePrevistoGeral.qtEstoque, estoquePrevistoGeral.dtEstoque, estoquePrevistoGeral.cdEmpresa, estoquePrevistoGeral.cdRepresentante, estoquePrevistoGeral.cdLocalEstoque);
			} else if (estoquePrevistoList.items[i] instanceof EstoqueIndustriaGeral) {
				EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) estoquePrevistoList.items[i];
				polulaItemGrid(item, estoqueIndustriaGeral.qtEstoque, estoqueIndustriaGeral.dtEstoque, estoqueIndustriaGeral.cdEmpresa, estoqueIndustriaGeral.cdRepresentante, estoqueIndustriaGeral.cdLocalEstoque);
			} else {
				EstoqueIndustria estoqueIndustria = (EstoqueIndustria) estoquePrevistoList.items[i];
				polulaItemGrid(item, estoqueIndustria.qtEstoque, estoqueIndustria.dtEstoque, estoqueIndustria.cdEmpresa, estoqueIndustria.cdRepresentante, estoqueIndustria.cdLocalEstoque);
			}
			gridItems[i] = item;
		}
		return gridItems;
	}
	private void polulaItemGrid(String[] item, double qtEstoque, Date dtEstoque, String cdEmpresa, String cdRepresentante, String cdLocalEstoque) throws SQLException {
		LocalEstoque localEstoque;
		String qtEstoqueInterface ="";
		if (LavenderePdaConfig.exibeGridEstoquePrevisto() || LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
			qtEstoqueInterface = StringUtil.getStringValueToInterface(qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPREVISTO) ? 0 : ValueUtil.doublePrecisionInterface);
		} else {
			qtEstoqueInterface = StringUtil.getStringValueToInterface(qtEstoque, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		}
		item[0] = StringUtil.getStringValue(dtEstoque);
		item[1] = StringUtil.getStringValue(qtEstoqueInterface);
		localEstoque = LocalEstoqueService.getInstance().findDsLocalEstoque(cdEmpresa, cdRepresentante, cdLocalEstoque);
		item[2] = localEstoque != null ? localEstoque.toString() : "";
	}

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	super.onFormEvent(event);
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == lbQtEstoque.btAcao) {
	    			atualizadoInfoEstoque();
	    		} else if (event.target == lbQtEstoque.btGradeEstoque) {
	    			btEstoqueGradeClick();
				}
	    		break;
	    	}
    	}
    }

	protected void btEstoqueGradeClick() throws SQLException {
		ListEstoqueGradeWindow listProdutoSimilarWindow = new ListEstoqueGradeWindow((Produto) getDomain(), ((Produto) getDomain()).cdItemGrade1Temp, null, 0, new Vector(), false, null);
		listProdutoSimilarWindow.popup();
	}

	private void atualizadoInfoEstoque() {
		try {
			double newEstoque = PedidoUiUtil.getEstoqueProdutoServidor(((Produto) getDomain()).cdProduto, null);
			Estoque estoque = EstoqueService.getInstance().getEstoque(((Produto) getDomain()).cdProduto, Estoque.FLORIGEMESTOQUE_PDA);
			lbQtEstoque.setValue(newEstoque - estoque.qtEstoque);
		} catch (ValidationException e) {
			throw new ValidationException(e.getMessage());
		} catch (Throwable e) {
			LogPdaService.getInstance().logSyncError("Erro ao atualizar o estoque. " + e.getMessage());
			throw new ValidationException(Messages.ESTOQUE_MSG_SYNC_FAIL);
		}
	}

	protected void visibleState() throws SQLException {
		super.visibleState();
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco() && getDomain() != null) {
			String flignoravalidacao = ProdutoService.getInstance().findColumnByRowKey(getDomain().getRowKey(), "FLIGNORAVALIDACAO");
			lbQtEstoque.setVisible(!ValueUtil.getBooleanValue(flignoravalidacao));
			lbQtEstoque.setEnabled(!ValueUtil.getBooleanValue(flignoravalidacao));
			lbEstoque.setVisible(!ValueUtil.getBooleanValue(flignoravalidacao));
		} else if (!LavenderePdaConfig.isOcultaEstoque() && isEstoquePrevistoDisplayable()) {
			lbQtEstoque.setEnabled(true);
		}
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		if (LavenderePdaConfig.apresentaMarcadoresProduto()) {
			clearMarcadores();
			adicionaMarcadoresTela();
			repositionChildren();
		}
	}
	
	private void adicionaMarcadoresTela() throws SQLException {
		Container cPrincipal = getContainerPrincipal();
		UiUtil.add(cPrincipal, lbMarcadores = new LabelName(Messages.CLIENTE_MARCADORES), getLeft(), AFTER + HEIGHT_GAP);
		lbMarcadores.appId = 1;
		Vector marcadores = MarcadorService.getInstance().findMarcadoresByProduto((Produto)getDomain());
		int size = marcadores.size();
		for (int i = 0; i < size; i++) {
			Marcador marcador = (Marcador) marcadores.items[i];
			if (marcador.imMarcadorAtivo != null) {
				Image image = UiUtil.getImage(marcador.imMarcadorAtivo);
				image = UiUtil.getSmoothScaledImage(image, iconSize, iconSize);
				ImageControl img = new ImageControl(image);
				img.appId = 1;
				LabelValue label = new LabelValue(marcador.dsMarcador);
				label.split(cPrincipal.getWidth() - img.getWidth() - (WIDTH_GAP_BIG * 5));
				label.appId = 1;
				UiUtil.add(cPrincipal, img, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(cPrincipal, label, AFTER + WIDTH_GAP_BIG, SAME);
			} else if (ValueUtil.isNotEmpty(marcador.dsMarcador)) {
				LabelValue label = new LabelValue(marcador.dsMarcador);
				label.appId = 1;
				UiUtil.add(cPrincipal, label,  getLeft(), AFTER + HEIGHT_GAP);
			}
		}
	}
	
	private void clearMarcadores() {
		Container cPrincipal = getContainerPrincipal();
		Control next = null;
		for (Control control = lbMarcadores; control != null;) {
			next = control.getNext();
			if (control.appId != 1) {
				break;
			}
			cPrincipal.remove(control);
			control = next;
		}
		lbMarcadores = null;
	}

}
