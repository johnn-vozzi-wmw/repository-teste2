package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ParcelaPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadParcelaPedidoForm extends BaseCrudCadForm {

    private EditNumberFrac edVlParcela;
    private EditNumberFrac edVlPctParcela;
    private EditNumberInt edQtDiasPrazo;
    private EditDate edDtVencimento;
    public Pedido pedido;

    public CadParcelaPedidoForm() {
        super(Messages.PARCELAPEDIDO_TITULO_CADASTRO);
        edVlParcela = new EditNumberFrac("9999999999", 9, 2);
        edVlPctParcela = new EditNumberFrac("9999999999", 9, 2);
        edQtDiasPrazo = new EditNumberInt("", 12);
        edDtVencimento = new EditDate();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ParcelaPedidoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ParcelaPedido();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
    	ParcelaPedido parcelaPedido = new ParcelaPedido();
        parcelaPedido.cdEmpresa = pedido.cdEmpresa;
        parcelaPedido.cdRepresentante = pedido.cdRepresentante;
        parcelaPedido.nuPedido = pedido.nuPedido;
        parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
    	parcelaPedido.cdParcela = ((ParcelaPedido)getDomain()).cdParcela;
        parcelaPedido.vlParcela = edVlParcela.getValueDouble();
        parcelaPedido.vlPctParcela = edVlPctParcela.getValueDouble();
        parcelaPedido.qtDiasPrazo = edQtDiasPrazo.getValueInt();
        if (LavenderePdaConfig.isGeraParcelasEmPercentual()) {
			Date dtVencimento = new Date();
			dtVencimento = DateUtil.getDateValue(pedido.dtEmissao);
			dtVencimento.advance(edQtDiasPrazo.getValueInt());
			edDtVencimento.setValue(dtVencimento);
		}
        parcelaPedido.dtVencimento = edDtVencimento.getValue();
        return parcelaPedido;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
        edVlParcela.setValue(parcelaPedido.vlParcela);
        edVlPctParcela.setValue(parcelaPedido.vlPctParcela);
        edQtDiasPrazo.setValue(parcelaPedido.qtDiasPrazo);
        edDtVencimento.setValue(parcelaPedido.dtVencimento);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edVlParcela.setText("");
        edVlPctParcela.setText("");
        edQtDiasPrazo.setText("");
        edDtVencimento.setText("");
    }

    public void setEnabled(boolean enabled) {
        edVlParcela.setEditable(enabled);
        edVlPctParcela.setEditable(enabled);
        edQtDiasPrazo.setEditable(enabled);
        edDtVencimento.setEditable(enabled);
    }

    //-----------------------------------------------

    //@Override
    protected void onFormStart() throws SQLException {
        int yComponents = getTop() + HEIGHT_GAP;
        if (!LavenderePdaConfig.isGeraParcelasEmPercentual()) {
			UiUtil.add(this, new LabelName(Messages.PARCELAPEDIDO_LABEL_VLPARCELA), edVlParcela, getLeft(), yComponents);
			UiUtil.add(this, new LabelName(Messages.PARCELAPEDIDO_LABEL_DTVENCIMENTO), edDtVencimento, getLeft(), AFTER + HEIGHT_GAP);
        } else {
        	UiUtil.add(this, new LabelName(Messages.PARCELAPEDIDO_LABEL_PCT), edVlPctParcela, getLeft(), yComponents);
			UiUtil.add(this, new LabelName(Messages.PARCELAPEDIDO_LABEL_PRAZO), edQtDiasPrazo, getLeft(), AFTER + HEIGHT_GAP);
        }
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException { }

    protected void insertOrUpdate(BaseDomain domain) throws SQLException {
    	ParcelaPedido parcelaPedido = (ParcelaPedido)domain;
    	getCrudService().validate(parcelaPedido);
    	((ParcelaPedidoService)getCrudService()).validateValorMinimo(parcelaPedido);
    	if (!isEditing()) {
    		((ParcelaPedidoService)getCrudService()).validateParcelaNova(pedido, parcelaPedido);
    	}
    	Vector list = pedido.parcelaPedidoList;
    	int index = -1;
		int size = list.size();
        for (int i = 0; i < size; i++) {
    		ParcelaPedido parcela = (ParcelaPedido) list.items[i];
    		if (parcela.equals(parcelaPedido)) {
    			index = i;
    		}
    	}
    	//--
    	if (index != -1) {
    		parcelaPedido = (ParcelaPedido) pedido.parcelaPedidoList.items[index];
    		parcelaPedido.vlParcela = edVlParcela.getValueDouble();
    		parcelaPedido.dtVencimento = edDtVencimento.getValue();
    		parcelaPedido.qtDiasPrazo = edQtDiasPrazo.getValueInt();
    		parcelaPedido.vlPctParcela = edVlPctParcela.getValueDouble();
    		pedido.parcelaPedidoList.items[index] = parcelaPedido;
    	} else {
    		parcelaPedido = new ParcelaPedido();
    		parcelaPedido.cdEmpresa = pedido.cdEmpresa;
    		parcelaPedido.cdRepresentante = pedido.cdRepresentante;
    		parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
    		parcelaPedido.nuPedido = pedido.nuPedido;
    		parcelaPedido.cdParcela = getCrudService().generateIdGlobal();
    		parcelaPedido.vlParcela = edVlParcela.getValueDouble();
    		parcelaPedido.qtDiasPrazo = edQtDiasPrazo.getValueInt();
    		parcelaPedido.vlPctParcela = edVlPctParcela.getValueDouble();
    		parcelaPedido.dtVencimento = edDtVencimento.getValue();
    		pedido.parcelaPedidoList.addElement(parcelaPedido);
    	}
    }

	protected void delete(BaseDomain domain) throws java.sql.SQLException {
		pedido.parcelaPedidoList.removeElement(domain);
	}

	protected void updateCurrentRecordInList(BaseDomain domain) throws SQLException {
		list();
	}

}