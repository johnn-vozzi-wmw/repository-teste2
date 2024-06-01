package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditFoneMask;
import br.com.wmw.framework.presentation.ui.ext.EditNumberTextInteger;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTextMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.ContatoCrmService;

public class CadSacContatoWindow extends WmwCadWindow {

    
    private Sac sac;
    private ContatoCrm contatoCrm;
	private EditText edContato;
	private EditFoneMask edFoneContato;
	private EditText edEmailContato;


	public CadSacContatoWindow(Sac sac) {
		super(Messages.CADASTRO_CONTATO_SAC);
		edContato = new EditText("@@@@@@@@@@", 100);
	    edFoneContato = new EditFoneMask();
	    edFoneContato.setValidChars(EditTextMask.VALID_NUMBER_CHARS);
	    edEmailContato = new EditText("@@@@@@@@@@", 100);
		this.sac = sac;
		this.contatoCrm = new ContatoCrm();
		setDefaultRect();
	}
	

	//@Override
	protected String getEntityDescription() {
		return title;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ContatoCrmService.getInstance();
	}

	//@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ContatoCrm();
	}

	//@Override
	protected BaseDomain screenToDomain() throws SQLException {
		contatoCrm.cdEmpresa = sac.cdEmpresa;
		contatoCrm.cdRepresentante = sac.cdRepresentante;
		contatoCrm.cdCliente = sac.cdCliente;
		contatoCrm.nmContato = edContato.getValue().trim();
		contatoCrm.nuFone = edFoneContato.getValue();
		contatoCrm.dsEmail = edEmailContato.getValue();
		contatoCrm.cdContato = getCrudService().generateIdGlobal();
		return contatoCrm;
	}

	//@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		this.contatoCrm = (ContatoCrm) domain;
		edContato.setValue(contatoCrm.cdCliente);
		edFoneContato.setValue(contatoCrm.nuFone);
		edEmailContato.setValue(contatoCrm.dsEmail);
		
	}

	//@Override
	protected void clearScreen() throws SQLException {
		edContato.setText("");
		edFoneContato.setText("");
		edEmailContato.setText("");

	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.SAC_LABEL_CONTATO + "*"), edContato, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.SAC_LABEL_FONE_CONTATO + "*"), edFoneContato, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.SAC_LABEL_EMAIL_CONTATO + "*"), edEmailContato, getLeft(), getNextY());
	 
	}
	
	

}