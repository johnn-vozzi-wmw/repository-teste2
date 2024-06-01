package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Faceamento;
import br.com.wmw.lavenderepda.business.domain.FaceamentoEstoque;
import br.com.wmw.lavenderepda.business.domain.HistoricoItem;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.FaceamentoEstoqueService;
import br.com.wmw.lavenderepda.business.service.FaceamentoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadFaceamentoForm extends BaseCrudCadForm {

    private EditText edCdProduto;
    private EditNumberFrac edQtPontoequilibrio;
    private LabelValue lvMediaVenda;
	private BaseButton btFiltrar;
	private Produto produto;
	private GridListContainer listContainer;
    private String DT_EMISSAO = Messages.PEDIDO_LABEL_DTEMISSAO;
    private String ITEMPEDIDO_LABEL_QTITENS = Messages.ITEMPEDIDO_LABEL_QTITENS;


    public CadFaceamentoForm() {
        super(Messages.FACEAMENTO_NOME_ENTIDADE);
        edCdProduto = new EditText("@@@@@@@@@@", 20);
        edCdProduto.drawBackgroundWhenDisabled = true;
        edQtPontoequilibrio = new EditNumberFrac("9999999999", 9);
        lvMediaVenda = new LabelValue("@@@@@@");
		listContainer = new GridListContainer(2, 1);
		listContainer.setBarTopSimple();
        btFiltrar = new BaseButton(UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
    }

    //@Override
    public String getEntityDescription() {
    	return Messages.FACEAMENTO_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return FaceamentoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Faceamento();
    }

    public void edit(BaseDomain domain) throws SQLException {
    	Faceamento faceamento = (Faceamento) domain;
    	produto = new Produto();
    	produto.cdEmpresa = faceamento.cdEmpresa;
    	produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produto.cdProduto = faceamento.cdProduto;
    	produto = (Produto) ProdutoService.getInstance().findByRowKey(produto.getRowKey());
    	//--
    	super.edit(faceamento);
    }

    private void carregaGrid() throws SQLException {
    	lvMediaVenda.setText("");
    	double vlMedia = 0;
    	//--
    	listContainer.removeAllContainers();
    	Vector itemPedidoList =  ItemPedidoService.getInstance().findHistoricoProdutosByCliente(SessionLavenderePda.getCliente().cdCliente, produto.cdProduto);
    	int size = itemPedidoList.size();
    	Container[] all = new Container[size];
    	if (size > 0) {
			BaseListContainer.Item c;
			HistoricoItem historicoItem;
			for (int i = 0; i < size; i++) {
		        all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
		        historicoItem = (HistoricoItem)itemPedidoList.items[i];
		        vlMedia += historicoItem.vlItemPedido;
		        c.setItens(getItem(historicoItem));
		        historicoItem = null;
			}
			historicoItem = null;
			//--
			listContainer.addContainers(all);
			vlMedia = vlMedia / size;
    	}
    	lvMediaVenda.setText(StringUtil.getStringValueToInterface(vlMedia, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
    	//--
    	edCdProduto.setText(produto.getDescription());
    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	HistoricoItem histItem = (HistoricoItem) domain;
        String[] item = {
    		ITEMPEDIDO_LABEL_QTITENS + " " + StringUtil.getStringValueToInterface(histItem.qtItemFisico),
    		DT_EMISSAO + " " + StringUtil.getStringValue(histItem.dtEmissao)};
        return item;
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        Faceamento faceamento = (Faceamento) getDomain();
        faceamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
        faceamento.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
        faceamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        faceamento.cdProduto = produto == null ? "" : ValueUtil.isEmpty(produto.cdProduto) ? "" : produto.cdProduto;
        faceamento.qtPontoEquilibrio = edQtPontoequilibrio.getValueDouble();
        return faceamento;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Faceamento faceamento = (Faceamento) domain;
        edCdProduto.setValue(produto.dsProduto);
        edQtPontoequilibrio.setValue(faceamento.qtPontoEquilibrio);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edCdProduto.setText("");
        edQtPontoequilibrio.setText("");
        lvMediaVenda.setText("");
    }

    protected void visibleState() throws SQLException {
    	super.visibleState();
    	btExcluir.setVisible(isEditing());
    	btFiltrar.setVisible(!isEditing());
    }

	protected void delete(BaseDomain domain) throws SQLException {
		Faceamento faceamento = (Faceamento) domain;
		//--
		FaceamentoEstoque faceamentoEstoque = new FaceamentoEstoque();
		faceamentoEstoque.cdEmpresa = faceamento.cdEmpresa;
		faceamentoEstoque.cdRepresentante = faceamento.cdRepresentante;
		faceamentoEstoque.cdCliente = faceamento.cdCliente;
		faceamentoEstoque.cdProduto = faceamento.cdProduto;
		faceamentoEstoque.dtCadastro = DateUtil.getCurrentDate();
		faceamentoEstoque = (FaceamentoEstoque) FaceamentoEstoqueService.getInstance().findByRowKey(faceamentoEstoque.getRowKey());
		if (faceamentoEstoque != null) {
			FaceamentoEstoqueService.getInstance().delete(faceamentoEstoque);
		}
		//--
		FaceamentoService.getInstance().delete(faceamento);
	}

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, new LabelName(Messages.FACEAMENTO_LABEL_MEDIA_VENDA), LEFT + WIDTH_GAP, BOTTOM - barBottomContainer.getHeight());
        UiUtil.add(this, lvMediaVenda, AFTER + WIDTH_GAP, SAME, FILL);
        //--
        LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	
    	UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_PRODUTO), getLeft(), AFTER + HEIGHT_GAP);
    	UiUtil.add(this, edCdProduto, getLeft(), AFTER, FILL - (btFiltrar.getPreferredWidth() + WIDTH_GAP + WIDTH_GAP_BIG));
    	edCdProduto.setEditable(false);
        UiUtil.add(this, btFiltrar, RIGHT - WIDTH_GAP_BIG, SAME , PREFERRED , UiUtil.getControlPreferredHeight());
        //--
        UiUtil.add(this, new LabelName(Messages.FACEAMENTO_LABEL_QTPONTOEQUILIBRIO), edQtPontoequilibrio, getLeft(), AFTER + HEIGHT_GAP);
        edQtPontoequilibrio.setEditable(!LavenderePdaConfig.usaEdicaoSugestaoVenda());
        //--
        UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight() - lvMediaVenda.getHeight() - HEIGHT_GAP);
    }

    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (isEditing()) {
    		carregaGrid();
    	}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
       try {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrar) {
					ListProdutoWindow produtoForm = new ListProdutoWindow();
					produtoForm.popup();
					if (produtoForm.produto != null) {
						produto = produtoForm.produto;
						carregaGrid();
					}
				}
				break;
			}
    	}
    	} catch (Throwable ex) {ex.printStackTrace();}
    }
}
