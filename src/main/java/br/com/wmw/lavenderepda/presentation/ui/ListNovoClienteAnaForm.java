package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.NovoClienteAnaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.ProspectService;
import br.com.wmw.lavenderepda.business.service.StatusAnaliseClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusAnaliseClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListNovoClienteAnaForm extends LavendereCrudListForm {
	
	private RepresentanteSupervComboBox cbRepresentantesSupervisor;
	private StatusAnaliseClienteComboBox cbAnalise;
	private CheckBoolean ckNaoFinalizados;
	public CheckBoolean ckApenasPendencias;
	private EditText edFiltroAvancado;
	private BaseButton btFiltroAvancado;
	private NovoCliente novoClienteFilter;
	private Prospect prospectFilter;
	
	public ListNovoClienteAnaForm() throws SQLException {
        super(Messages.NOVOCLIENTEANA_TITULO_LISTA);
        singleClickOn = true;
        cbAnalise = new StatusAnaliseClienteComboBox();
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox();
        }
        ckNaoFinalizados = new CheckBoolean(Messages.NOVOCLIENTEANA_LABEL_STATUS_NAO_FINALIZ);
        ckApenasPendencias = new CheckBoolean(Messages.NOVOCLIENTEANA_LABEL_STATUS_APENAS_PENDENCIAS);
        edFiltroAvancado = new EditText("999999999", 50);
        btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
        constructorListContainer();
    }
    
	private void constructorListContainer() {
    	listContainer = new GridListContainer(6, 2);
    	ScrollPosition.AUTO_HIDE = false;
    	configListContainer("DTCADASTRO");
    	listContainer.resizeable = false;
    	listContainer.btResize.setVisible(false);
    	listContainer.setColsSort(new String[][]{{Messages.NOVOCLIENTEANA_LABEL_RAZAOSOCIAL, "NMRAZAOSOCIAL"}, {Messages.NOVOCLIENTEANA_LABEL_DTEDICAO, "DTEDICAOUSUARIO"}, {Messages.NOVOCLIENTEANA_LABEL_DTCADASTRO, "DTCADASTRO"}});
    	listContainer.setColPosition(5, RIGHT);
    	ScrollPosition.AUTO_HIDE = true;
    }
	
    //@Override
    protected CrudService getCrudService() {
        return NovoClienteService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			if (prospectFilter == null) {
				prospectFilter = new Prospect();
			}
			return prospectFilter;
		}
    	if (novoClienteFilter == null) {
    		novoClienteFilter = new NovoCliente();
    	}
		return novoClienteFilter;
	}
    
    @Override
    public void singleClickInList() throws SQLException {
    	setBaseCrudCadForm(new CadNovoClienteAnaForm());
    	super.singleClickInList();
    }
    
	public void loadDefaultFilters() throws SQLException {
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		cbRepresentantesSupervisor.setSelectedIndex(0);
        	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentantesSupervisor.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    			if (ValueUtil.isEmpty(cbRepresentantesSupervisor.getValue())) {
    				cbRepresentantesSupervisor.setSelectedIndex(0);
    			}
    		}
        }
    }
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			prospectFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				prospectFilter.cdRepresentante = cbRepresentantesSupervisor.getValue();
			} else {
				prospectFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			}
			prospectFilter.novoClienteAna = new NovoClienteAna();
			prospectFilter.novoClienteAna.cdStatusAnalise = cbAnalise.getValue();
			prospectFilter.novoClienteAna.ckNaoFinalizados = ckNaoFinalizados.isChecked();
			prospectFilter.novoClienteAna.ckApenasPendencias = ckApenasPendencias.isChecked();
			prospectFilter.novoClienteAna.dsFiltro = edFiltroAvancado.getText();
			return NovoClienteAnaService.getInstance().findAllNovoClienteAna(prospectFilter);
		}
    	novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
   		if (SessionLavenderePda.isUsuarioSupervisor()) {
   			novoClienteFilter.cdRepresentante = cbRepresentantesSupervisor.getValue();
   		} else {
   			novoClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
   		}
    	novoClienteFilter.novoClienteAna.cdStatusAnalise = cbAnalise.getValue();
    	novoClienteFilter.novoClienteAna.ckNaoFinalizados = ckNaoFinalizados.isChecked();
    	novoClienteFilter.novoClienteAna.ckApenasPendencias = ckApenasPendencias.isChecked();
    	novoClienteFilter.novoClienteAna.dsFiltro = edFiltroAvancado.getText();
    	return NovoClienteAnaService.getInstance().findAllNovoClienteAna(novoClienteFilter);
    }
    
    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			Prospect prospect = (Prospect) ProspectService.getInstance().findByRowKeyDyn(getSelectedRowKey());
			if (prospect == null) {
				prospect = new Prospect();
				String[] chave = getSelectedRowKey().split(";");
				prospect.cdEmpresa = chave[0];
				prospect.cdRepresentante = chave[1];
				prospect.flOrigemProspect = chave[2];
				prospect.cdProspect = chave[3];
				prospect.flTipoAlteracao = Prospect.FLTIPOALTERACAO_ORIGINAL;
			}
			return prospect;
		} else {
			NovoCliente novoCliente = (NovoCliente) getCrudService().findByRowKeyDyn(getSelectedRowKey());
			if (novoCliente == null) {
				novoCliente = new NovoCliente();
				String[] chave = getSelectedRowKey().split(";");
				novoCliente.cdEmpresa = chave[0];
				novoCliente.cdRepresentante = chave[1];
				novoCliente.flOrigemNovoCliente = chave[2];
				novoCliente.cdNovoCliente = chave[3];
				novoCliente.flTipoAlteracao = NovoCliente.FLTIPOALTERACAO_ORIGINAL;
			}
			return novoCliente;
		}
    }
    
  //@Override
    protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
    }
    
    @Override
    public void visibleState() {
    	super.visibleState();
    	btNovo.setVisible(false);
    }
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			Prospect prospect = (Prospect) domain;
			String[] item = {
					StringUtil.getStringValue(prospect.novoClienteAna.nuCnpj) + " - " + StringUtil.getStringValue(prospect.novoClienteAna.nmRazaoSocial),
					"",
					Messages.NOVOCLIENTEANA_LABEL_DTCADASTRO_ABREV + " " + StringUtil.getStringValue(prospect.novoClienteAna.dtCadastro),
					"",
					Messages.NOVOCLIENTEANA_LABEL_DTEDICAO_ABREV + " " + StringUtil.getStringValue(prospect.novoClienteAna.dtEdicaoUsuario),
					StringUtil.getStringValue(StatusAnaliseClienteService.getInstance().getDsStatusAnaliseCliente(prospect.novoClienteAna.cdStatusAnalise))};
			return item;
		}
		NovoCliente novoCliente = (NovoCliente) domain;
		String[] item = {
				StringUtil.getStringValue(novoCliente.novoClienteAna.nuCnpj) + " - " + StringUtil.getStringValue(novoCliente.novoClienteAna.nmRazaoSocial),
				"",
				Messages.NOVOCLIENTEANA_LABEL_DTCADASTRO_ABREV + " " + StringUtil.getStringValue(novoCliente.novoClienteAna.dtCadastro),
				"",
				Messages.NOVOCLIENTEANA_LABEL_DTEDICAO_ABREV + " " + StringUtil.getStringValue(novoCliente.novoClienteAna.dtEdicaoUsuario),
				StringUtil.getStringValue(StatusAnaliseClienteService.getInstance().getDsStatusAnaliseCliente(novoCliente.novoClienteAna.cdStatusAnalise))};
		return item;
    }

    //--------------------------------------------------------------

    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(c, domain);
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			Prospect prospect = (Prospect) domain;
			if (prospect.novoClienteAna.isCnpjRecorrente()) {
				c.setBackColor(LavendereColorUtil.COR_FUNDO_CNPJ_DUPLICADO);
			} else if (prospect.novoClienteAna.isNaoPossuiCobertura()) {
				c.setBackColor(LavendereColorUtil.COR_FUNDO_ERRO_CAD_CIDADEUF);
			}
		} else {
			NovoCliente novoCliente = (NovoCliente) domain;
			if (novoCliente.novoClienteAna.isCnpjRecorrente()) {
				c.setBackColor(LavendereColorUtil.COR_FUNDO_CNPJ_DUPLICADO);
			} else if (novoCliente.novoClienteAna.isNaoPossuiCobertura()) {
				c.setBackColor(LavendereColorUtil.COR_FUNDO_ERRO_CAD_CIDADEUF);
			}
		}
    }
    
    @Override
    protected void onFormStart() {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantesSupervisor, getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDSTATUSANALISE), cbAnalise, getLeft(), getNextY());
    	UiUtil.add(this, ckNaoFinalizados, getLeft(), getNextY());
    	UiUtil.add(this, ckApenasPendencias, getLeft(), getNextY());
		UiUtil.add(this, edFiltroAvancado, getLeft(), getNextY() + HEIGHT_GAP, getWFill() - UiUtil.getControlPreferredHeight());
		UiUtil.add(this, btFiltroAvancado, getRight(), SAME, UiUtil.getControlPreferredHeight());
        //--
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltroAvancado) {
					btFiltroAvancadoClick(event.target);
	            } else if (event.target == cbAnalise) {
	            	cbAnaliseChange();
	            	list();
	            } else if (event.target == ckApenasPendencias) {
	            	ckNaoFinalizados.setChecked(false);
	            	list();
	            } else if (event.target == ckNaoFinalizados) {
	            	ckApenasPendencias.setChecked(false);
	            	list();
	            } else if (event.target == cbRepresentantesSupervisor) {
	            	cbRepresentanteClick();
	            }
				break;
			}
		}
    }
    
    private void cbRepresentanteClick() throws SQLException {
		if (cbRepresentantesSupervisor.getSelectedItem() != null) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentantesSupervisor.getSelectedItem()).representante);
		}
		list();
	}

    private void cbAnaliseChange() {
		if (ValueUtil.isNotEmpty(cbAnalise.getValue())) {
			ckNaoFinalizados.setEditable(false);
			ckNaoFinalizados.setChecked(false);
			ckApenasPendencias.setEditable(false);
			ckApenasPendencias.setChecked(false);
		} else {
			ckNaoFinalizados.setEditable(true);
			ckApenasPendencias.setEditable(true);
		}
	}

	private void btFiltroAvancadoClick(Object target) throws SQLException {
		FiltroNovoCliAnaAvancadoWindow filtroClienteAvancadoWindow = new FiltroNovoCliAnaAvancadoWindow((NovoCliente) getDomainFilter());
		filtroClienteAvancadoWindow.popup();
		if (filtroClienteAvancadoWindow.filtroAplicado) {
			list();
			listContainer.setFocus();
		}
	}

	@Override
    protected void filtrarClick() throws SQLException {
		super.filtrarClick();
		list();
		listContainer.setFocus();
	}
}
