package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.service.ConcorrenteService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoConcorrenteService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.sql.ResultSet;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListPesquisaMercadoForm extends BaseCrudListForm {
	
	public LabelContainer clienteContainer;
	
    public ListPesquisaMercadoForm(String nuPedido) throws SQLException {
        super(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
        clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
        if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
        	String cdTipoPesquisa = LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() ? PesquisaMercado.CDTIPOPESQUISA_VALOR : PesquisaMercado.CDTIPOPESQUISA_GONDOLA;
        	setBaseCrudCadForm(new CadPesquisaMercadoProdConcorrenteForm(cdTipoPesquisa));
        } else {
        	setBaseCrudCadForm(new CadPesquisaMercadoForm(nuPedido));
        }
        singleClickOn = true;
        int oneChar = fm.charWidth('A');
        GridColDefinition[] gridColDefiniton = null;
        if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
        	if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() && LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
        		gridColDefiniton = new GridColDefinition[] {
	    			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
	    			new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, oneChar * 15, LEFT),
	    			new GridColDefinition(Messages.CONCORRENTE_NOME_ENTIDADE, oneChar * 15, LEFT),
	    			new GridColDefinition(Messages.PESQUISA_MERCADO_CDTIPOPESQUISA, oneChar * 10, LEFT),
	    			new GridColDefinition(Messages.PEDIDO_LABEL_DTEMISSAO, oneChar * 10, LEFT)};
        	} else {
        		if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor()) {
        			gridColDefiniton = new GridColDefinition[] {
	        			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
	        			new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, oneChar * 15, LEFT),
	        			new GridColDefinition(Messages.CONCORRENTE_NOME_ENTIDADE, oneChar * 15, LEFT),
	        			new GridColDefinition(Messages.PRODUTO_LABEL_VLUNITARIO, oneChar * 3, RIGHT),
	        			new GridColDefinition(Messages.PEDIDO_LABEL_DTEMISSAO, oneChar * 10, LEFT)};
        		}
        		if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
        			gridColDefiniton = new GridColDefinition[] {
	        			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
	        			new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, oneChar * 15, LEFT),
	        			new GridColDefinition(Messages.CONCORRENTE_NOME_ENTIDADE, oneChar * 15, LEFT),
	        			new GridColDefinition(Messages.PESQUISA_MERCADO_QTITEMTOTAL, oneChar * 3, RIGHT),
	        			new GridColDefinition(Messages.PEDIDO_LABEL_DTEMISSAO, oneChar * 10, LEFT)};
        		}
        	}
        } else {
        	gridColDefiniton = new GridColDefinition[] {
    			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
    			new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, oneChar * 15, LEFT),
    			new GridColDefinition(Messages.CONCORRENTE_NOME_ENTIDADE, oneChar * 15, LEFT),
    			new GridColDefinition(Messages.PRODUTO_LABEL_VLUNITARIO, oneChar * 3, RIGHT),
    			new GridColDefinition(Messages.PEDIDO_LABEL_DTEMISSAO, oneChar * 10, LEFT)};
        }
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return PesquisaMercadoService.getInstance();
    }

    protected String getDataSource() throws SQLException {
    	PesquisaMercado pesqMercado = new PesquisaMercado();
    	pesqMercado.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	pesqMercado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	pesqMercado.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	return getCrudService().findAllByExampleToGrid(pesqMercado);
    }

    public String[][] getItems(String[][] items) throws SQLException {
    	String[][] itensFinal = new String[items.length][5];
    	for (int i = 0; i < items.length; i++) {
    		itensFinal[i][0] = items[i][0];
    		itensFinal[i][1] = ProdutoService.getInstance().getDescriptionWithId(items[i][3]);
    		itensFinal[i][2] = ConcorrenteService.getInstance().getDsConcorrente(items[i][4]);
    		itensFinal[i][3] = items[i][2];
    		itensFinal[i][3] = items[i][3];
    	}
    	items = null;
    	return itensFinal;
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new PesquisaMercado();
    }
    
    public void reloadClientLabel() {
    	clienteContainer.setDescricao(SessionLavenderePda.getCliente().toString());
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
        return PesquisaMercadoService.getInstance().findAllPesquisasForCliente(SessionLavenderePda.getCliente().cdCliente);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        PesquisaMercado pesquisamercado = (PesquisaMercado) domain;
        String[] item = null;
        if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
        	if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() && LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
        		item = new String[] {
                    StringUtil.getStringValue(pesquisamercado.rowKey),
                    StringUtil.getStringValue(ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisamercado)),
                    StringUtil.getStringValue(ConcorrenteService.getInstance().getDsConcorrente(pesquisamercado.cdConcorrente)),
                    StringUtil.getStringValue(pesquisamercado.getDsTipoPesquisa()),
            		StringUtil.getStringValue(pesquisamercado.dtEmissao)};
        	} else {
        		if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor()) {
        			item = new String[] {
                        StringUtil.getStringValue(pesquisamercado.rowKey),
                        StringUtil.getStringValue(ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisamercado)),
                        StringUtil.getStringValue(ConcorrenteService.getInstance().getDsConcorrente(pesquisamercado.cdConcorrente)),
                        StringUtil.getStringValueToInterface(pesquisamercado.vlUnitario),
                		StringUtil.getStringValue(pesquisamercado.dtEmissao)};
        		}
        		if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
        			item = new String[] {
                        StringUtil.getStringValue(pesquisamercado.rowKey),
                        StringUtil.getStringValue(ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisamercado)),
                        StringUtil.getStringValue(ConcorrenteService.getInstance().getDsConcorrente(pesquisamercado.cdConcorrente)),
                        StringUtil.getStringValueToInterface(pesquisamercado.qtItemTotal),
                		StringUtil.getStringValue(pesquisamercado.dtEmissao)};
        		}
        	}
        } else {
        	item = new String[] {
                StringUtil.getStringValue(pesquisamercado.rowKey),
                StringUtil.getStringValue(ProdutoService.getInstance().getDsProduto(pesquisamercado.cdProduto)),
                StringUtil.getStringValue(ConcorrenteService.getInstance().getDsConcorrente(pesquisamercado.cdConcorrente)),
                StringUtil.getStringValueToInterface(pesquisamercado.vlUnitario),
        		StringUtil.getStringValue(pesquisamercado.dtEmissao)};
        }
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(barBottomContainer, btNovo, 5);
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, gridEdit);
		gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL - (barBottomContainer.getHeight() + HEIGHT_GAP));
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	
    }
    
    public void invalidateCadInstace() throws SQLException {
    	String cdTipoPesquisa = LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() ? PesquisaMercado.CDTIPOPESQUISA_VALOR : PesquisaMercado.CDTIPOPESQUISA_GONDOLA;
    	setBaseCrudCadForm(new CadPesquisaMercadoProdConcorrenteForm(cdTipoPesquisa));
    }
    
    @Override
    public void novoClick() throws SQLException {
    	if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() && LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
    		int result = UiUtil.showMessage(Messages.PESQUISAMERCADO_NOME_ENTIDADE, Messages.PESQUISA_MERCADO_NOVA_PESQUISA_ESCOLHA, new String[] { Messages.PESQUISAMERCADO_TITULO_PESQUISA_VALOR, Messages.PESQUISAMERCADO_TITULO_PESQUISA_GONDOLA});
    		if (result == 0) {
    			setBaseCrudCadForm(new CadPesquisaMercadoProdConcorrenteForm(PesquisaMercado.CDTIPOPESQUISA_VALOR));
    		}
    		if (result == 1) {
    			setBaseCrudCadForm(new CadPesquisaMercadoProdConcorrenteForm(PesquisaMercado.CDTIPOPESQUISA_GONDOLA));
    		}
    	}
    	super.novoClick();
    }
    
    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	PesquisaMercado selectedDomain = (PesquisaMercado) super.getSelectedDomain();
    	if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes() && selectedDomain != null) {
    		setBaseCrudCadForm(new CadPesquisaMercadoProdConcorrenteForm(selectedDomain.cdTipoPesquisa));
    	}
    	return selectedDomain;
    }
    
}
