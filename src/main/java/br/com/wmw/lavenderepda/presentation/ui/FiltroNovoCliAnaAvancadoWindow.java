package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class FiltroNovoCliAnaAvancadoWindow extends WmwWindow {
	
	private NovoCliente novoClienteFilter;
	private ButtonPopup btFiltrar;
	private ButtonPopup btLimpar;
	private LabelName lbDtCadastro;
	private LabelName lbDtEdicao;
	private EditDate edDtCadastroInicial;
	private EditDate edDtCadastroFinal;
	private EditDate edDtEdicaoInicial;
	private EditDate edDtEdicaoFinal;
	public boolean filtroAplicado = false;
	
	

	public FiltroNovoCliAnaAvancadoWindow(NovoCliente novoCliente) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.novoClienteFilter = novoCliente;
		btFiltrar = new ButtonPopup(Messages.BOTAO_FILTRAR);
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		lbDtCadastro = new LabelName(Messages.NOVOCLIENTEANA_LABEL_DTCADASTRO);
		lbDtEdicao = new LabelName(Messages.NOVOCLIENTEANA_LABEL_DTEDICAO);
		edDtCadastroInicial = new EditDate();
		edDtCadastroInicial.onlySelectByCalendar();
		edDtCadastroFinal = new EditDate();
		edDtCadastroFinal.onlySelectByCalendar();
		edDtEdicaoInicial = new EditDate();
		edDtEdicaoInicial.onlySelectByCalendar();
		edDtEdicaoFinal = new EditDate();
		edDtEdicaoFinal.onlySelectByCalendar();
		carregaFiltrosAplicados();
		setDefaultRect();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDtCadastro, edDtCadastroInicial, getLeft(), getNextY());
		UiUtil.add(this, edDtCadastroFinal, AFTER + WIDTH_GAP_BIG , SAME);
		UiUtil.add(this, lbDtEdicao, edDtEdicaoInicial, getLeft(), getNextY());
		UiUtil.add(this, edDtEdicaoFinal, AFTER + WIDTH_GAP_BIG , SAME);
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
	}
	
	
	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrar) {
					aplicaFiltrosNoDomainFilter();
					unpop();
				} else if (event.target == btLimpar) {
					limpaFiltrosClick();
				} 
				break;
			}
		}
				
	}
	
	private void carregaFiltrosAplicados() throws SQLException {
		edDtCadastroInicial.setValue(novoClienteFilter.dtCadastroInicial);
		edDtCadastroFinal.setValue(novoClienteFilter.dtCadastroFinal);
		edDtEdicaoInicial.setValue(novoClienteFilter.novoClienteAna.dtEdicaoUsuarioInicial);
		edDtEdicaoFinal.setValue(novoClienteFilter.novoClienteAna.dtEdicaoUsuarioFinal);
	}

	
	private void aplicaFiltrosNoDomainFilter() {
		novoClienteFilter.dtCadastroInicial = edDtCadastroInicial.getValue();
		novoClienteFilter.dtCadastroFinal = edDtCadastroFinal.getValue();
		novoClienteFilter.novoClienteAna.dtEdicaoUsuarioInicial = edDtEdicaoInicial.getValue();
		novoClienteFilter.novoClienteAna.dtEdicaoUsuarioFinal = edDtEdicaoFinal.getValue();
		filtroAplicado = true;
	}
	
	private void limpaFiltrosClick() throws SQLException {
		edDtCadastroInicial.setValue(null);
		edDtCadastroFinal.setValue(null);
		edDtEdicaoInicial.setValue(null);
		edDtEdicaoFinal.setValue(null);
		novoClienteFilter.dtCadastroInicial = edDtCadastroInicial.getValue();
		novoClienteFilter.dtCadastroFinal = edDtCadastroFinal.getValue();
		novoClienteFilter.novoClienteAna.dtEdicaoUsuarioInicial = edDtEdicaoInicial.getValue();
		novoClienteFilter.novoClienteAna.dtEdicaoUsuarioFinal = edDtEdicaoFinal.getValue();
	}	
}
