package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.InfoEntregaPed;
import br.com.wmw.lavenderepda.business.domain.RelInadimpRep;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.RelInadimpRepService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import totalcross.ui.event.Event;

public class CadInfoEntregaPedidoForm extends BaseCrudCadForm {

	private LabelValue lvPedido;
    private LabelValue lvDtEmissao;
    private LabelValue lvCliente;
    private LabelValue lvRepresentante;
    private EditMemo edInfoEntrega;

    public CadInfoEntregaPedidoForm() {
        super(Messages.MENU_OPCAO_RELINFOENTREGAPED);
        lvPedido = new LabelValue("@@@@@@@@@@@");
        lvDtEmissao = new LabelValue("@@@@@@@@@@@");
        lvCliente = new LabelValue("@@@@@@@@@@@");
        lvRepresentante = new LabelValue("@@@@@@@@@@@");
        edInfoEntrega = new EditMemo("9999", 20, 4000);
        setReadOnly();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return Messages.RELINADIMPREP_TITULO_CADASTRO;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return RelInadimpRepService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new RelInadimpRep();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        RelInadimpRep relInadimpRep = (RelInadimpRep) getDomain();
        return relInadimpRep;
    }

    protected void visibleState() throws SQLException {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	InfoEntregaPed infoEntregaPed = (InfoEntregaPed) domain;
    	lvPedido.setValue(infoEntregaPed.nuPedido);
    	lvDtEmissao.setValue(infoEntregaPed.dtEmissao);
        lvCliente.setValue(ClienteService.getInstance().getDescriptionWithKey(infoEntregaPed.cdEmpresa, infoEntregaPed.cdRepresentante, infoEntregaPed.cdCliente));
        edInfoEntrega.setValue(infoEntregaPed.dsInfoEntrega);
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	lvRepresentante.setValue(RepresentanteService.getInstance().getDescription(infoEntregaPed.cdRepresentante));
        }
    }

    //@Override
    protected void onFormStart() throws SQLException {
        int yComponents = getTop() + HEIGHT_GAP;
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), lvRepresentante, getLeft(), yComponents);
        	yComponents = AFTER + HEIGHT_GAP;
    	}
        UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_NUPEDIDO), lvPedido, getLeft(), yComponents);
        UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), lvDtEmissao, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lvCliente, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.MENU_OPCAO_RELINFOENTREGAPED), getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, edInfoEntrega, getLeft(), AFTER + HEIGHT_GAP, FILL - 8, FILL - 8);
        edInfoEntrega.setEditable(false);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

    protected void clearScreen() throws java.sql.SQLException {
		//Nada a fazer
	}
}
