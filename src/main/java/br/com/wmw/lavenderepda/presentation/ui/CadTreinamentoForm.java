package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Licao;
import br.com.wmw.lavenderepda.business.service.LicaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.LicaoComboBox;
import totalcross.ui.event.Event;

public class CadTreinamentoForm extends BaseCrudCadForm {
	private LabelName lbTitulo;
	
	private LabelName lbLicaoDependente;
	
	private LabelName lbConteudo;
	
	private EditMemo memoConteudo;
	
	private EditText edTitulo;
	
	private LicaoComboBox cbLicao;
	
	public CadTreinamentoForm() throws SQLException {
		super("Lição");
		lbTitulo = new LabelName("Título");
		lbLicaoDependente = new LabelName("Lição dependente");
		lbConteudo = new LabelName("Conteúdo");
		memoConteudo = new EditMemo("@", 10, UiUtil.defaultGap, 3000);
		
		
		edTitulo = new EditText("", 30);
		cbLicao = new LicaoComboBox();
		cbLicao.load();
	}
	
	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = (Licao) getDomain();
		licao.titulo = edTitulo.getValue();
		licao.cdLicaoDependente = cbLicao.getValue();
		licao.conteudo = memoConteudo.getValue();
		licao.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		return licao;
	}
	
	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = (Licao) domain;
		licao.cdLicao = LicaoService.getInstance().generateIdGlobal();
		super.insert(licao);
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Licao licao = (Licao) domain;
		edTitulo.setValue(licao.titulo);
		if(licao.cdLicaoDependente != null) {
			cbLicao.setValue(licao.cdLicaoDependente);
		}
		memoConteudo.setValue(licao.conteudo);
	}

	@Override
	protected void clearScreen() throws SQLException {
		edTitulo.setText("");
		cbLicao.setSelectedIndex(-1);
		memoConteudo.clear();
		
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		// TODO Auto-generated method stub
		return new Licao();
	}

	@Override
	protected String getEntityDescription() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		// TODO Auto-generated method stub
		return LicaoService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onFormShow() throws SQLException {
		// TODO Auto-generated method stub
		
		limpaComponentesTela();
		UiUtil.add(this, lbTitulo, edTitulo, LEFT + 10, getTop() + 10);
		UiUtil.add(this, lbLicaoDependente, cbLicao, LEFT + 10, AFTER + 10);
		UiUtil.add(this, lbConteudo, memoConteudo, LEFT + 10, AFTER + 10);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		// TODO Auto-generated method stub

	}
	
	private void limpaComponentesTela() {
		this.remove(edTitulo);
		this.remove(cbLicao);
		this.remove(memoConteudo);
		this.remove(lbTitulo);
		this.remove(lbLicaoDependente);
		this.remove(lbConteudo);
		
	}

}
