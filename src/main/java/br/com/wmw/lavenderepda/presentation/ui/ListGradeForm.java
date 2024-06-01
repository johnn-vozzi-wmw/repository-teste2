package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ContratoProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListGradeForm extends BaseCrudListForm {

	private ButtonAction btApontarEstoque;
	private String QTCONTRATOPRODUTO = Messages.FORECAST_QTCONTRATOPRODUTO;
	private ContratoCliente contratoClienteVigente;

    public ListGradeForm(ContratoCliente contratoClienteVigente) {
        super(Messages.GRADE_NOME_ENTIDADE);
        this.contratoClienteVigente = contratoClienteVigente;
        btApontarEstoque = new ButtonAction(Messages.FACEAMENTOESTOQUE_TITULO_CADASTRO, "images/reabrirpedido.png");
        singleClickOn = false;
		listContainer = new GridListContainer(2, 1);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContratoProdutoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ContratoProduto();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	return contratoClienteVigente.contratoProdutoList;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ContratoProduto grade = (ContratoProduto) domain;
        String descriptionProduto = getDescriptionProduto(grade.cdProduto);
        if (LavenderePdaConfig.ocultaColunaCdProduto) {
        	descriptionProduto += " [" + grade.cdProduto + "]";
        } else {
        	descriptionProduto = grade.cdProduto + " - " + descriptionProduto;
        }
		String[] item = {
            descriptionProduto,
            QTCONTRATOPRODUTO + " " + StringUtil.getStringValueToInterface(grade.qtProdutoContrato)};
        return item;
    }

	public String getDescriptionProduto(String cdProduto) throws SQLException {
		Produto produto = new Produto();
    	produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produto.cdProduto = cdProduto;
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
        UiUtil.add(barBottomContainer, btApontarEstoque, 5);
        UiUtil.add(this, listContainer, LEFT, clienteContainer.getY2() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btApontarEstoque) {
					show(new CadGradeEstForm(contratoClienteVigente));
				}
				break;
			}
		}
    }

}