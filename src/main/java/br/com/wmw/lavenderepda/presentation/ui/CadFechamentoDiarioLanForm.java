package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioLanService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.TipoLancamentoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoLancamentoComboBox;
import totalcross.ui.event.Event;
import totalcross.util.Date;

import java.sql.SQLException;

public class CadFechamentoDiarioLanForm extends BaseCrudCadForm {

	private Date dtFechamentoDiario;
	private boolean modoLeitura;

	private LabelValue lvDataFechamentoDiario;
	private EditNumberFrac edVlTotalLancamento;
	private TipoLancamentoComboBox cbTipoLancamento;

	public CadFechamentoDiarioLanForm(Date dtFechamentoDiario) throws SQLException {
		super(Messages.FECHAMENTO_DIARIO_LAN_CADASTRO);
		lvDataFechamentoDiario = new LabelValue();
		cbTipoLancamento = new TipoLancamentoComboBox();
		edVlTotalLancamento = new EditNumberFrac("9999999999", 9);
		setDtFechamentoDiario(dtFechamentoDiario);
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) getDomain();
		fechamentoDiarioLan.cdTipoLancamento = cbTipoLancamento.getValue();
		fechamentoDiarioLan.vlTotalLancamento = edVlTotalLancamento.getValueDouble();
		return fechamentoDiarioLan;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) getDomain();
		cbTipoLancamento.setSelectedItem(TipoLancamentoService.getInstance().getFilterByCd(fechamentoDiarioLan.cdTipoLancamento));
		edVlTotalLancamento.setValue(fechamentoDiarioLan.vlTotalLancamento);
	}

	@Override
	protected void clearScreen() throws SQLException {
		cbTipoLancamento.clear();
		edVlTotalLancamento.clear();
	}

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (!modoLeitura) {
			cbTipoLancamento.setEditable(!btExcluir.isVisible());
			edVlTotalLancamento.setEditable(true);
		} else {
			cbTipoLancamento.setEditable(false);
			edVlTotalLancamento.setEditable(false);
		}
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return FechamentoDiarioLanService.getInstance().getFilterByDate(dtFechamentoDiario);
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return FechamentoDiarioLanService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.FECHAMENTO_DIARIO_LAN_DATA), lvDataFechamentoDiario, getLeft(), getTop() + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.FECHAMENTO_DIARIO_LAN_TIPO_LANCAMENTO), cbTipoLancamento, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.FECHAMENTO_DIARIO_LAN_TOTAL), edVlTotalLancamento, getLeft(), AFTER + HEIGHT_GAP);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {

	}

	public void setDtFechamentoDiario(Date dtFechamentoDiario) throws SQLException {
		this.dtFechamentoDiario = dtFechamentoDiario;
		lvDataFechamentoDiario.setText(dtFechamentoDiario.toString());
		setaModoLeitura();
	}

	private void setaModoLeitura() throws SQLException {
		modoLeitura = FechamentoDiarioService.getInstance().isFechamentoDiarioExecutado(dtFechamentoDiario);
		if (modoLeitura) {
			setReadOnly();
		}
	}

}
