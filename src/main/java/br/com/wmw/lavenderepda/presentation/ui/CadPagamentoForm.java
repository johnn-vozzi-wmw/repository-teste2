package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.service.ConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConsignacaoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.Event;

public class CadPagamentoForm extends BaseCrudCadForm {

    private EditNumberFrac edVlPago;
    private EditNumberFrac edVlAdicionalPago;
    private EditMemo edDsObservacao;
    private final TipoPagamentoComboBox cbTipoPagamento;

    public CadPagamentoForm() {
        super(Messages.PAGAMENTO_NOME_ENTIDADE);
        edVlPago = new EditNumberFrac("9999999999", 9);
        edVlAdicionalPago = new EditNumberFrac("9999999999", 9);
        edDsObservacao = new EditMemo("@@@@@@@@@@", 3, 500);
		cbTipoPagamento = new TipoPagamentoComboBox();
    }

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return PagamentoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Pagamento();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        Pagamento pagamento = (Pagamento) getDomain();
        pagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
        pagamento.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        pagamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        pagamento.cdTipoPagamento = cbTipoPagamento.getValue();
        pagamento.vlPago = edVlPago.getValueDouble();
        pagamento.dsObservacao = edDsObservacao.getValue();
        if (!isEditing()) {
        	pagamento.dtPagamento = DateUtil.getCurrentDate();
        }
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	pagamento.vlAdicionalPago = edVlAdicionalPago.getValueDouble();
        }
        return pagamento;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Pagamento pagamento = (Pagamento) domain;
		cbTipoPagamento.carregaTipoPagamentosCliente(SessionLavenderePda.getCliente(), SessionLavenderePda.getCliente().cdCondicaoPagamento);
		cbTipoPagamento.setValue(pagamento.cdTipoPagamento);
		//--
        cbTipoPagamento.setValue(pagamento.cdTipoPagamento);
        edVlPago.setValue(pagamento.vlPago);
        edDsObservacao.setValue(pagamento.dsObservacao);
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	edVlAdicionalPago.setValue( pagamento.vlAdicionalPago);
        }
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
		cbTipoPagamento.carregaTipoPagamentosCliente(SessionLavenderePda.getCliente(), SessionLavenderePda.getCliente().cdCondicaoPagamento);
		cbTipoPagamento.setValue(SessionLavenderePda.getCliente().cdTipoPagamento);
		//--
        edVlPago.setText("");
        edVlAdicionalPago.setText("");
        edDsObservacao.setText("");
    }

    protected void visibleState() throws SQLException {
    	super.visibleState();
    	edVlPago.setEditable(!isEditing());
    	edVlAdicionalPago.setEditable(LavenderePdaConfig.isUsaModuloPagamentoComAdicional() && !isEditing());
    	btExcluir.setVisible(false);
    }

    public void setEnabled(boolean enabled) {
        cbTipoPagamento.setEditable(enabled);
        edVlPago.setEditable(enabled);
        edVlAdicionalPago.setEditable(LavenderePdaConfig.isUsaModuloPagamentoComAdicional() && enabled);
        edDsObservacao.setEditable(enabled);
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, new LabelName(Messages.RECADO_LABEL_CDTIPO), cbTipoPagamento, getLeft(), AFTER + WIDTH_GAP);
        UiUtil.add(this, new LabelName(Messages.LABEL_VALOR), edVlPago, getLeft(), AFTER + HEIGHT_GAP);
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	UiUtil.add(this, new LabelName(Messages.LABEL_VALOR_ADICIONAL), edVlAdicionalPago, getLeft(), AFTER + HEIGHT_GAP);
        }
        UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP);
    }

    protected void save() throws java.sql.SQLException {
		Pagamento domain = (Pagamento) screenToDomain();
		domain.editing = isEditing();
		insertOrUpdate(domain);
		list();
	}

    protected void insert(BaseDomain domain) throws java.sql.SQLException {
    	super.insert(domain);
    	//--
    	if (LavenderePdaConfig.usaModuloConsignacao) {
    		Consignacao consignacao = new Consignacao();
    		consignacao.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        	consignacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
        	consignacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		ConsignacaoPdbxDao.getInstance().updateFlPagamentoEfetuado(consignacao);
    	}
    }

    protected boolean delete() throws java.sql.SQLException {
    	boolean retorno =  super.delete();
    	//--
    	if (LavenderePdaConfig.usaModuloConsignacao && retorno) {
    		if (PagamentoService.getInstance().findAllNotSentByExample().size() == 0) {
    			ListPagamentoForm listPagamentoForm = (ListPagamentoForm)getBaseCrudListForm();
    			if (listPagamentoForm.getConsignacao() != null) {
    				listPagamentoForm.getConsignacao().flPagamentoEfetuado = ValueUtil.VALOR_NAO;
    				ConsignacaoService.getInstance().update(listPagamentoForm.getConsignacao());
    			}
    		}
    	}
    	return retorno;
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
}