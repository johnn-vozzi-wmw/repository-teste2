package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CargaProduto;
import br.com.wmw.lavenderepda.business.service.CargaProdutoService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import totalcross.ui.event.Event;

public class CadEditCargaProdutoForm extends BaseCrudCadForm {


	private LabelValue lvProduto;
	private LabelValue lvQtEstoque;
	private LabelValue lvStatus;
	private EditNumberFrac edQtSolicitado;
    
    public CadEditCargaProdutoForm(CargaProduto cargaProduto) throws SQLException {
        super(Messages.CARGAPRODUTO_NOME_ENTIDADE);
        edit(cargaProduto);
        lvProduto = new LabelValue(cargaProduto.getProduto().toString());
        edQtSolicitado = new EditNumberFrac("9999999999", 9);
        edQtSolicitado.setValue(cargaProduto.qtSolicitado);
        lvQtEstoque = new LabelValue(StringUtil.getStringValueToInterface(EstoqueService.getInstance().getQtEstoqueDisponivel(cargaProduto.cdProduto)));
        lvStatus = new LabelValue(cargaProduto.getStatus());
        btSalvar.setVisible(ValueUtil.isNotEmpty(cargaProduto.flTipoAlteracao));
        btExcluir.setVisible(btSalvar.isVisible());
        edQtSolicitado.setEditable(btSalvar.isVisible());
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return Messages.CARGAPRODUTO_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() {
        return CargaProdutoService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() {
        return new CargaProduto();
    }
    
    @Override
    protected void visibleState() throws SQLException {
    	// nao deve fazer o super.visibleState
    }
    
    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        CargaProduto cargaProduto = (CargaProduto) getDomain();
        cargaProduto.qtSolicitado = edQtSolicitado.getValueDouble();
        return cargaProduto;
    }
    
    //@Override
    protected void domainToScreen(BaseDomain domain) {}
    
    //@Override
    protected void clearScreen() {}
    
    public void setEnabled(boolean enabled) {}
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {}

    //-----------------------------------------------
    
    //@Override
    protected void onFormStart() {
		UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_LABEL_CDPRODUTO), lvProduto, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_LABEL_ESTOQUE), lvQtEstoque, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_LABEL_QTSOLICITADO), edQtSolicitado, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_STATUS), lvStatus, getLeft(), getNextY());
    }

	
	@Override
	protected void delete(BaseDomain domain) throws SQLException {
		super.delete(domain);
		((ListCargaProdutoForm) prevContainer).list();
		
	}
	
}
