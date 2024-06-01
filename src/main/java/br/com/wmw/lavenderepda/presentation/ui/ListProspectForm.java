package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.TipoPessoa;
import br.com.wmw.lavenderepda.business.service.ProspectService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProspectForm extends LavendereCrudListForm {
	
	private RepresentanteSupervComboBox cbRepresentantesSupervisor;

	public ListProspectForm() throws SQLException {
		super(Messages.PROSPECT_NOME_ENTIDADE);
		setBaseCrudCadForm(new CadProspectForm());
		if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox();
        }
		singleClickOn = true;
		constructorListContainer();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new Prospect();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProspectService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (LavenderePdaConfig.usaCadastroProspectPF() || LavenderePdaConfig.usaCadastroProspectPJ()) {
			UiUtil.add(barBottomContainer, btNovo, 5);
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantesSupervisor, getLeft(), getNextY() + HEIGHT_GAP);
    	}
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentantesSupervisor) {
					cbRepresentanteClick();
				}
				break;
			}
		}

	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		Prospect filter = new Prospect();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		carregaRepresentante(filter);
		return getCrudService().findAllByExample(filter);
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Prospect filter = (Prospect) domain;
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		carregaRepresentante(filter);
		return getCrudService().findAllByExample(filter);
	}
	
	@Override
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
        list();
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Prospect prospect = (Prospect) domain;
		return new String[] {
				!LavenderePdaConfig.ignoraSeparadoresProspectCpfCnpj ? 
						getCpfCnpjFormatado(prospect, ValueUtil.getValidNumbers(prospect.nuCnpj)) : 
						StringUtil.getStringValue(prospect.nuCnpj) 
		};
	}
	
	private String getCpfCnpjFormatado(Prospect prospect, String cnpj) {
    	if (prospect != null) {
    		if (Messages.TIPOPESSOA_FLAG_JURIDICA.equals(prospect.flTipoPessoa)) {
    			return getCnpjFormatado(cnpj);
    		} else {
    			return getCpfFormatado(cnpj);
    		}
    	}
    	return "";
    }
	
	private String getCpfFormatado(String cpf) {
    	StringBuffer cpfFormatado = new StringBuffer();
    	if (ValueUtil.isNotEmpty(cpf)) {
	    	cpfFormatado.append(cpf.substring(0, 3));
	    	cpfFormatado.append(".");
	    	cpfFormatado.append(cpf.substring(3, 6));
	    	cpfFormatado.append(".");
	    	cpfFormatado.append(cpf.substring(6, 9));
	    	cpfFormatado.append("-");
	    	cpfFormatado.append(cpf.substring(9));
    	}
    	return cpfFormatado.toString();
    }
    
    private String getCnpjFormatado(String cnpj) {
    	StringBuffer cnpjFormatado = new StringBuffer();
    	if (ValueUtil.isNotEmpty(cnpj)) {
    		cnpjFormatado.append(cnpj.substring(0, 2));
    		cnpjFormatado.append(".");
    		cnpjFormatado.append(cnpj.substring(2, 5));
    		cnpjFormatado.append(".");
    		cnpjFormatado.append(cnpj.substring(5, 8));
    		cnpjFormatado.append("/");
    		cnpjFormatado.append(cnpj.substring(8, 12));
    		cnpjFormatado.append("-");
    		cnpjFormatado.append(cnpj.substring(12));
    	}
    	return cnpjFormatado.toString();
    }
    
    @Override
    protected String getSelectedRowKey() throws SQLException {
    	return listContainer.getSelectedId();
    }
    
    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return getCrudService().findByRowKeyDyn(getSelectedRowKey());
    }
	
	private void constructorListContainer() {
    	ScrollPosition.AUTO_HIDE = false;
    	configListContainer("NUCNPJ");
    	listContainer = new GridListContainer(1, 1);
    	listContainer.resizeable = false;
    	listContainer.btResize.setVisible(false);
    	listContainer.setColsSort(new String[][]{{Messages.NOVOCLIENTE_LABEL_NUCNPJ, "NUCNPJ"}});
    	ScrollPosition.AUTO_HIDE = true;
    }
	
	private void carregaRepresentante(Prospect prospect) {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			prospect.cdRepresentante = cbRepresentantesSupervisor.getValue();
   		} else {
   			prospect.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
   		}
	}
	
	private void cbRepresentanteClick() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		if (cbRepresentantesSupervisor.getSelectedItem() != null) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentantesSupervisor.getSelectedItem()).representante);
		}
    	try {
    		list();
    	} finally {
    		mb.unpop();
    	}
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		Prospect prospect = (Prospect) getSelectedDomain();
		SessionLavenderePda.setRepresentante(prospect.cdRepresentante, RepresentanteService.getInstance().getDescription(prospect.cdRepresentante));
		super.singleClickInList();
	}
    

}
