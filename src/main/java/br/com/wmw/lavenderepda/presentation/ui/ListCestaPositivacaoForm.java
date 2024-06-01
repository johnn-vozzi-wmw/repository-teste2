package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CestaPositCliente;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.CestaPositClienteService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CampanhaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CestaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListCestaPositivacaoForm extends BaseCrudListForm {

	private CestaComboBox cbCesta;
	private CampanhaComboBox cbCampanha;
	private RepresentanteSupervComboBox cbRepresentante;
	private CheckBoolean ckEfetivado;


	public ListCestaPositivacaoForm() throws SQLException {
        super(Messages.CESTAPOSIT_NOME_ENTIDADE);
        cbCesta = new CestaComboBox(Messages.CESTA_NOME_ENTIDADE, BaseComboBox.DefaultItemType_ALL);
        cbCampanha = new CampanhaComboBox(Messages.CAMPANHA_NOME_ENTIDADE);
        ckEfetivado = new CheckBoolean(Messages.LABEL_EFETIVADO);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
		singleClickOn = true;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CestaPositClienteService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new CestaPositProduto();
    }

    //@Override
    protected Vector getDomainList() throws SQLException {
    	return CestaPositClienteService.getInstance().findAllCestaPositCli(edFiltro.getValue(), cbCampanha.getValue(), cbCesta.getValue(), ckEfetivado.isChecked());
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        CestaPositCliente cestaPositCliente = (CestaPositCliente) domain;
        String[] item = {
            cestaPositCliente.rowKey,
        	StringUtil.getStringValue(cestaPositCliente.cdCliente),
            cestaPositCliente.nmRazaoSocial,
            StringUtil.getStringValueToInterface(cestaPositCliente.vlPctpositivacao),
            StringUtil.getStringValueToInterface(cestaPositCliente.vlPctFaltante > 0 ? cestaPositCliente.vlPctFaltante : 0),
            cestaPositCliente.getCesta().toString()};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        return gridEdit.getSelectedItem()[0];
    }


    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.CAMPANHA_NOME_ENTIDADE), cbCampanha, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CESTA_NOME_ENTIDADE), cbCesta, getLeft(), getNextY());
    	UiUtil.add(this, ckEfetivado, getLeft(), getNextY());
        //--
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		edFiltro.setText("");
        //--
        int ww = fm.stringWidth("www");
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
            new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, ww * 2, LEFT),
            new GridColDefinition(Messages.LABEL_CLIENTE, ww * 5, LEFT),
            new GridColDefinition(Messages.LABEL_POSITIVADO, ww * 2, RIGHT),
            new GridColDefinition(Messages.LABEL_FALTANTE, ww * 2, RIGHT),
            new GridColDefinition(Messages.LABEL_CESTA, ww * 5, RIGHT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - HEIGHT_GAP_BIG);
    }

    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentante.setSelectedIndex(0);
        	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
            	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
            }
    	}
    	cbCampanha.setSelectedIndex(0);
    	cbCesta.setSelectedIndex(0);
    }

    //@Override
    public void list() throws SQLException {
    	super.list();
    	gridEdit.qsort(1);
    }
    
    //@Override
    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante || event.target == cbCampanha || event.target == cbCesta || event.target == ckEfetivado) {
					if (event.target == cbRepresentante && ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					}
					filtrarClick();
				}
				break;
			}
    	}
    }

    protected void filtrarClick() throws SQLException {
		list();
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			edFiltro.setValue("");
		}
        edFiltro.requestFocus();
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	CestaPositCliente cestaPositCliente = (CestaPositCliente) getSelectedDomain();
    	ListCestaPositProdutoForm listCestaPositProdutoForm = new ListCestaPositProdutoForm(cestaPositCliente, ClienteService.getInstance().getNmRazaoSocial(cestaPositCliente.cdEmpresa, cestaPositCliente.cdRepresentante, cestaPositCliente.cdCliente));
		show(listCestaPositProdutoForm);
    }
}
