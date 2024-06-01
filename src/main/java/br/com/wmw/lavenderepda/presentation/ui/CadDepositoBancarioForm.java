package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DepositoBancario;
import br.com.wmw.lavenderepda.business.service.DepositoBancarioService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadDepositoBancarioForm extends BaseCrudCadForm {
	
	private Date dtFechamentoDiario;
	private boolean modoLeitura;
	
	private EditDate edDataDepositoBancario;
	private EditText edNumeroDepositoBancario;
	private EditNumberFrac edVlTotalDepositoBancario;

	public CadDepositoBancarioForm() throws SQLException {
		super(Messages.DEPOSITO_BANCARIO_TITULO_CADASTRO);
		criaCampos();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		DepositoBancario depositoBancario = (DepositoBancario) getDomain();
		depositoBancario.dtDepositoBancario = edDataDepositoBancario.getValue();
		depositoBancario.nuDepositoBancario = edNumeroDepositoBancario.getText();
		depositoBancario.vlTotalDepositoBancario = edVlTotalDepositoBancario.getValueDouble();
		return depositoBancario;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		DepositoBancario depositoBancario = (DepositoBancario) getDomain();
		edDataDepositoBancario.setValue(depositoBancario.dtDepositoBancario);
		edNumeroDepositoBancario.setText(depositoBancario.nuDepositoBancario);
		edVlTotalDepositoBancario.setValue(depositoBancario.vlTotalDepositoBancario);
	}

	@Override
	protected void clearScreen() throws SQLException {
		edDataDepositoBancario.setValue(dtFechamentoDiario);
		edNumeroDepositoBancario.clear();
		edVlTotalDepositoBancario.clear();
	}
	
	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (!modoLeitura) {
			edDataDepositoBancario.setEditable(!btExcluir.isVisible());
			edNumeroDepositoBancario.setEditable(!btExcluir.isVisible());
		} else {
			edDataDepositoBancario.setEditable(false);
			edNumeroDepositoBancario.setEditable(false);
			edVlTotalDepositoBancario.setEditable(false);
		}
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return DepositoBancarioService.getInstance().getNewDepositoBancario(dtFechamentoDiario);
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DepositoBancarioService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.DEPOSITO_BANCARIO_DTDEPOSITOBANCARIO), edDataDepositoBancario, getLeft(), getTop());
		UiUtil.add(this, new LabelName(Messages.DEPOSITO_BANCARIO_NUDEPOSITOBANCARIO), edNumeroDepositoBancario, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.DEPOSITO_BANCARIO_VLTOTALDEPOSITOBANCARIO), edVlTotalDepositoBancario, getLeft(), AFTER + HEIGHT_GAP);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
	public void setDtFechamentoDiario(Date dtFechamentoDiario) throws SQLException {
		this.dtFechamentoDiario = dtFechamentoDiario;
		setaModoLeitura();
	}
	
	private void setaModoLeitura() throws SQLException {
		modoLeitura = FechamentoDiarioService.getInstance().isFechamentoDiarioExecutado(dtFechamentoDiario);
		if (modoLeitura) {
			setReadOnly();
		}
	}

	private void criaCampos() {
		edDataDepositoBancario = new EditDate();
		edNumeroDepositoBancario = new EditText("@@@@@@@@@@", 20);
		edVlTotalDepositoBancario = new EditNumberFrac("9999999999", 9);
	}
	
}
