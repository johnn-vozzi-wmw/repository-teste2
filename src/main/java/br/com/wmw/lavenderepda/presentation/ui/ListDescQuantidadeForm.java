package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.SortButtonEvent;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.service.DescQuantidadeService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDescQuantidadeForm extends BaseCrudListForm {

	private ItemTabelaPreco itemTabelaPreco;
	private String cdProduto;
	DescQuantidade descQuantidadeFilter;
    
    public ListDescQuantidadeForm(String cdProduto) {
        super(Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE);
        //--
        this.itemTabelaPreco = new ItemTabelaPreco();
		this.descQuantidadeFilter = new DescQuantidade();
		this.cdProduto = cdProduto;
		barBottomContainer.setVisible(false);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return DescQuantidadeService.getInstance();
    }

	protected BaseDomain getDomainFilter() throws SQLException {
		return descQuantidadeFilter;
	}

    //@Override
    protected Vector getDomainList() throws SQLException {
		DescQuantidade descontoQuantidadeFilter = (DescQuantidade) getDomainFilter();
    	if (ValueUtil.isNotEmpty(itemTabelaPreco.cdEmpresa) && ValueUtil.isNotEmpty(itemTabelaPreco.cdRepresentante) && ValueUtil.isNotEmpty(itemTabelaPreco.cdTabelaPreco)) {
    		if (itemTabelaPreco.cdProduto != null) {
    			descontoQuantidadeFilter.setCdDesconto(itemTabelaPreco.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, itemTabelaPreco.cdTabelaPreco, itemTabelaPreco.cdProduto);
    			return DescQuantidadeService.getInstance().findAllByCdDesconto(descontoQuantidadeFilter.cdDesconto);
    		}
    	} else if (ValueUtil.isNotEmpty(cdProduto)) {
    		descontoQuantidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descontoQuantidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		descontoQuantidadeFilter.cdProduto = cdProduto;
    		return DescQuantidadeService.getInstance().findAllByExample(descontoQuantidadeFilter);
    	}
    	return new Vector();
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        DescQuantidade descontoQuantidade = (DescQuantidade) domain;
        String dsTabelaPreco = TabelaPrecoService.getInstance().getDsTabelaPreco(descontoQuantidade.cdTabelaPreco);
        if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
        	dsTabelaPreco += " [" + descontoQuantidade.cdTabelaPreco + "]";
        }
        String[] item = {
                StringUtil.getStringValue(descontoQuantidade.rowKey),
                StringUtil.getStringValueToInterface(descontoQuantidade.qtItem),
                StringUtil.getStringValueToInterface(descontoQuantidade.vlPctDesconto),
                StringUtil.getStringValueToInterface(descontoQuantidade.vlDesconto),
                dsTabelaPreco,
                descontoQuantidade.cdTabelaPreco};
        return item;
    }

    //@Override
	protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
	}

    //@Override
    protected String getSelectedRowId() {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected BaseDomain getSelectedDomainOnGrid() {
        DescQuantidade descontoQuantidade = new DescQuantidade();
        String[] item = gridEdit.getSelectedItem();
        descontoQuantidade.qtItem = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(item[1]));
        if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
        	descontoQuantidade.vlDesconto = ValueUtil.getDoubleValue(item[2]);
        } else {
        	descontoQuantidade.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
        }
        descontoQuantidade.cdTabelaPreco = StringUtil.getStringValue(item[5]);
        return descontoQuantidade;
    }

    //@Override
    protected void onFormStart() throws SQLException {
        int ww = fm.stringWidth("www");
        boolean colCdDesconto = LavenderePdaConfig.usaDescontoPorQuantidadeValor;
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, ww * 3, CENTER),
            new GridColDefinition(!colCdDesconto ? Messages.DESCONTO_LABEL_VLPCTDESCONTO : FrameworkMessages.CAMPO_ID, ww * 3, CENTER),
            new GridColDefinition(colCdDesconto ? Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE : FrameworkMessages.CAMPO_ID, ww * 3, LEFT),
            new GridColDefinition(Messages.TABELAPRECO_NOME_TABPRECO, ww * 6, LEFT),
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT)};
        gridEdit = UiUtil.createGridEditDatabaseSort(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, TOP, FILL, FILL);
    }


	public String getSortAtributte(int sortCol) {
		switch (sortCol) {
			case 2: {
				return DescQuantidade.NMCOLUNA_VLPCTDESCONTO + "," + DescQuantidade.NMCOLUNA_QTITEM;
			}
			case 3: {
				return DescQuantidade.NMCOLUNA_VLDESCONTO + "," + DescQuantidade.NMCOLUNA_QTITEM;
			}
			case 4: {
				return DescQuantidade.NMCOLUNA_CDTABELAPRECO + "," + DescQuantidade.NMCOLUNA_QTITEM;
			}
			default: {
				return DescQuantidade.NMCOLUNA_QTITEM + "," + DescQuantidade.NMCOLUNA_VLPCTDESCONTO;
			}
		}
	}

	public String getSortAsc(boolean ascending) {
		if (ascending) {
			return ValueUtil.VALOR_SIM + "," + ValueUtil.VALOR_SIM;
		}
		return ValueUtil.VALOR_NAO + "," + ValueUtil.VALOR_NAO;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case SortButtonEvent.SORT_BUTTON_CLICK_EVENT: {
				int lastColSort = gridEdit.getLastColSort();
				getDomainFilter().sortAsc = getSortAsc(gridEdit.isAscending());
				getDomainFilter().sortAtributte = getSortAtributte(lastColSort);
				montaGridDescontoQuantidade();
				gridEdit.setLastColSort(lastColSort);
			}
		}
	}

	private void montaGridDescontoQuantidade() throws SQLException {
		Vector domainList = getDomainList();
		int listSize = domainList.size();
		if (listSize > 0) {
			gridEdit.removeAllElements();
			String[][] gridItems = new String[listSize][gridEdit.captions.length];
			for (int i = 0; i < listSize; i++) {
				gridItems[i] = getItem(domainList.items[i]);
			}
			gridEdit.setItems(gridItems);
		}
	}
	
}