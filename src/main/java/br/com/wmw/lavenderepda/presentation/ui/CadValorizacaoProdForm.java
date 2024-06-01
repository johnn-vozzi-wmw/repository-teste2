package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ValorizacaoProdService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadValorizacaoProdForm extends BaseCrudCadForm {
	
    private EditText edDsProduto;
	private BaseButton btFiltrar;
    private EditNumberInt edQtItem;
    private EditNumberFrac edVlUnitario;
    private EditMemo edDsObservacao;
    private LabelName lbDataCadastro;
	private LabelValue lbDataValorizacao;
    
    private Produto produto;
    private boolean isEnviado;
    
    public CadValorizacaoProdForm() {
        super(Messages.VALORIZACAOPROD_TITULO_CADASTRO);
        edDsProduto = new EditText("@@@@@@@@@@", 20);
        edDsProduto.setEnabled(false);
        int tamImage = UiUtil.getLabelPreferredHeight() + HEIGHT_GAP * 2;
        btFiltrar = new BaseButton(UiUtil.getColorfulImage("images/maisfiltros.png", tamImage, tamImage));
        edQtItem = new EditNumberInt("9999999999", 10);
        edVlUnitario = new EditNumberFrac("9999999999", 3, 2);
        edVlUnitario.setEnabled(false);
        edDsObservacao = new EditMemo("", 6, 255);
        lbDataCadastro = new LabelName(Messages.VALORIZACAOPROD_LABEL_DTVALORIZACAO);
        lbDataValorizacao = new LabelValue(DateUtil.getCurrentDate().toString()+" - "+TimeUtil.getCurrentTimeHHMMSS());
        scrollable = true;
        isEnviado = false;
    }

    //-----------------------------------------------
    
    //@Override
    public String getEntityDescription() {
    	return Messages.VALORIZACAOPROD_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() {
        return ValorizacaoProdService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() {
        return new ValorizacaoProd();
    }
    
    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        ValorizacaoProd valorizacaoProd = ((ValorizacaoProd) getDomain());
        valorizacaoProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
        valorizacaoProd.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
        valorizacaoProd.dsProduto = edDsProduto.getValue();
        valorizacaoProd.qtItem = edQtItem.getValueInt();
        valorizacaoProd.vlUnitario = edVlUnitario.getValueDouble();
        valorizacaoProd.vlTotalItem = edQtItem.getValueInt() * edVlUnitario.getValueDouble();
        valorizacaoProd.dsObservacao = edDsObservacao.getValue();
        if (isEditing()) {
	        valorizacaoProd.dtAlteracao = DateUtil.getCurrentDate();
	        valorizacaoProd.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		} else {
            valorizacaoProd.cdProduto = (produto != null) ? produto.cdProduto : "";
	        valorizacaoProd.cdProdutoValorizacao = StringUtil.getStringValue(ValorizacaoProdService.getInstance().generateIdGlobal());
	        valorizacaoProd.dtValorizacao = DateUtil.getCurrentDate();
	        valorizacaoProd.hrValorizacao = TimeUtil.getCurrentTimeHHMMSS();
		}
        return valorizacaoProd;
    }
    
    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
    	Produto produtoFilter = new Produto();
    	produtoFilter.cdEmpresa = valorizacaoProd.cdEmpresa;
    	produtoFilter.cdRepresentante = valorizacaoProd.cdRepresentante;
    	produtoFilter.cdProduto = valorizacaoProd.cdProduto;
    	produtoFilter = (Produto) ProdutoService.getInstance().findByRowKey(produtoFilter.getRowKey());
    	if (produtoFilter != null) {
    		edDsProduto.setValue(produtoFilter.toString());
		}
        edQtItem.setValue(ValueUtil.getIntegerValue(valorizacaoProd.qtItem));
        edVlUnitario.setValue(valorizacaoProd.vlUnitario);
        lbDataValorizacao.setText(valorizacaoProd.dtValorizacao.toString()+" - "+valorizacaoProd.hrValorizacao);
        edDsObservacao.setValue(valorizacaoProd.dsObservacao);
        isEnviado = isEditing() && ValueUtil.valueEquals(ValorizacaoProd.FLTIPOALTERACAO_ORIGINAL, valorizacaoProd.flTipoAlteracao);
    }
    
    //@Override
    protected void clearScreen() {
        edDsProduto.setText("");
        edQtItem.setText("");
        edVlUnitario.setText("");
        lbDataValorizacao.setText(DateUtil.getCurrentDate().toString()+" - "+TimeUtil.getCurrentTimeHHMMSS());
        edDsObservacao.setText("");
    }
    
    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	lbDataCadastro.setVisible(isEditing());
    	lbDataValorizacao.setVisible(isEditing());
    	setEnabled(!(isEditing() && isEnviado));
    }
    
    public void setEnabled(boolean enabled) {
    	btFiltrar.setVisible(!isEditing());
    	btSalvar.setVisible(enabled);
        edQtItem.setEnabled(enabled);
        edDsObservacao.setEnabled(enabled);
    	btExcluir.setVisible(isEditing() && enabled);
    }
    
    //-----------------------------------------------
    
    //@Override
    protected void onFormStart() {
    	UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_DSPRODUTO), edDsProduto, getLeft(), getNextY(), FILL - (btFiltrar.getPreferredWidth() + WIDTH_GAP_BIG + WIDTH_GAP));
		UiUtil.add(this, btFiltrar, RIGHT - WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_QTITEM), edQtItem, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_VLUNITARIO), edVlUnitario, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_DSOBSERVACAO), edDsObservacao, getLeft(), getNextY());
		UiUtil.add(this, lbDataCadastro, lbDataValorizacao, getLeft(), getNextY());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btFiltrar) {
					btFiltrarClick();
				}
	    		break;
	    	}
		}
    }
    
    private void btFiltrarClick() throws SQLException {
    	ListProdutoWindow produtoForm = new ListProdutoWindow();
		produtoForm.popup();
		if (produtoForm.produto != null) {
			produto = produtoForm.produto;
	        edDsProduto.setValue(produtoForm.produto.dsProduto);
	        if (LavenderePdaConfig.usaPrecoPadraoProdutoParaSerExibidoNaLista()) {
		        edVlUnitario.setValue(produtoForm.produto.vlPrecoPadrao);
	        } else {
	        	ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(produtoForm.newListProdutoForm.itemTabelaPrecoFiltroLista.cdTabelaPreco, produto.cdProduto, produtoForm.newListProdutoForm.itemTabelaPrecoFiltroLista.cdUf);
	        	produto.itemTabelaPreco = (ItemTabelaPreco) itemTabelaPreco.clone();
        		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
        			CondicaoComercialService.getInstance().aplicaIndiceFinanceiroCondComercialProdutoBase(produto, produtoForm.newListProdutoForm.condicaoComercialFiltroLista, true);
        		}
	        	edVlUnitario.setValue(produto.itemTabelaPreco.vlUnitario);
	        }
		}
	}
}
