package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Faceamento;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.FaceamentoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListFaceamentoForm extends LavendereCrudListForm {

	private ButtonAction btApontarEstoque;
	private String PONTO_EQUILIBRIO =  Messages.FACEAMENTO_LABEL_QTPONTOEQUILIBRIO;

    public ListFaceamentoForm() throws SQLException {
        super(Messages.FACEAMENTO_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadFaceamentoForm());
        btApontarEstoque = new ButtonAction(Messages.FACEAMENTOESTOQUE_TITULO_CADASTRO, "images/reabrirpedido.png");
        singleClickOn = true;
        constructorListContainer();
    }

    private void constructorListContainer() {
    	configListContainer("CDPRODUTO");
    	listContainer = new GridListContainer(2, 1);
    	listContainer.setColsSort(new String[][]{{Messages.CODIGO, "CDPRODUTO"}, {PONTO_EQUILIBRIO, "QTPONTOEQUILIBRIO"}, {Messages.FACEAMENTO_LABEL_DSFACEAMENTO, "DSPRODUTO"}});
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return FaceamentoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new Faceamento();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Faceamento faceamento = (Faceamento) domain;
    	faceamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	faceamento.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	faceamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        return getCrudService().findAllByExampleSummary(faceamento);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        Faceamento faceamento = (Faceamento) domain;
        String[] item = {
        	StringUtil.getStringValue(findDescriptionProduto(faceamento)),
            PONTO_EQUILIBRIO + " " + StringUtil.getStringValueToInterface(faceamento.qtPontoEquilibrio)};
        return item;
    }

	public String findDescriptionProduto(Faceamento faceamento) throws SQLException {
		Produto produto = new Produto();
    	produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produto.cdProduto = faceamento.cdProduto;
		return ProdutoService.getInstance().getDsProduto(produto);
	}

    //@Override
    protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
        UiUtil.add(barBottomContainer, btNovo , 5);
        UiUtil.add(barBottomContainer, btApontarEstoque, 4);
        UiUtil.add(this, listContainer);
        listContainer.setRect(LEFT, clienteContainer.getY2() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btApontarEstoque) {
					show(new CadFaceamentoEstoqueForm(this.sortAtributte, this.sortAsc));
				}
				break;
			}
		}
    }

}
