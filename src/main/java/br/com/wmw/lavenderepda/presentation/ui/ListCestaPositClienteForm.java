package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cesta;
import br.com.wmw.lavenderepda.business.domain.CestaPositCliente;
import br.com.wmw.lavenderepda.business.service.CampanhaService;
import br.com.wmw.lavenderepda.business.service.CestaPositClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListCestaPositClienteForm extends BaseCrudListForm {

    public ListCestaPositClienteForm() {
        super(Messages.CESTAPOSITCLIENTE_NOME_ENTIDADE);
        singleClickOn = true;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CestaPositClienteService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new CestaPositCliente();
    }

    //@Override
    protected Vector getDomainList() throws SQLException {
    	CestaPositCliente cestapositclienteFilter = new CestaPositCliente();
    	cestapositclienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cestapositclienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	cestapositclienteFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        return getCrudService().findAllByExample(cestapositclienteFilter);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	CestaPositCliente cestapositcliente = (CestaPositCliente) domain;
        String[] item = {
            cestapositcliente.rowKey,
            Cesta.getDsCesta(cestapositcliente.cdCesta),
            StringUtil.getStringValueToInterface(cestapositcliente.vlPctpositivacao),
	        CampanhaService.getInstance().getDsCampanha(cestapositcliente.cdCampanha)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	//--
        int oneChar = fm.charWidth('A');
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.CESTA_NOME_ENTIDADE, oneChar * 8, LEFT),
            new GridColDefinition(Messages.CESTA_PERCENTUAL_POSITIVACAO, oneChar * 30, RIGHT),
    		new GridColDefinition(Messages.CAMPANHA_NOME_ENTIDADE, oneChar * 20, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit, LEFT, AFTER, FILL, FILL);
    }

    public void visibleState() {
    	barBottomContainer.setVisible(false);
    	super.visibleState();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

	//@Override
	public void detalhesClick() throws SQLException {
		if (gridEdit.getSelectedIndex() != -1) {
			CestaPositCliente cestapositcliente = (CestaPositCliente)CestaPositClienteService.getInstance().findByRowKey(getSelectedRowKey());
			ListCestaPositProdutoForm listCestaPositProdutoForm = new ListCestaPositProdutoForm(cestapositcliente);
			show(listCestaPositProdutoForm);
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.CLIENTE_NOME_ENTIDADE));
		}
	}

}
