package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.NfeEstoque;
import br.com.wmw.lavenderepda.business.service.NfeEstoqueService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoNfeComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListNfeEstoqueForm extends BaseCrudListForm {
	
	private EditDate edDtInicial;
	private EditDate edDtFinal;
	private BaseButton btFiltro;
	private TipoNfeComboBox cbTipoNfe;
	
	public ListNfeEstoqueForm() {
		super(Messages.REMESSAESTOQUE_NFES);
		listContainer = new GridListContainer(5, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		singleClickOn = true;
		edDtInicial = new EditDate();
		edDtFinal = new EditDate();
		btFiltro = new BaseButton("", UiUtil.getColorfulImage("images/search.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		cbTipoNfe = new TipoNfeComboBox();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		NfeEstoque filter = new NfeEstoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(NfeEstoque.class);
		filter.dtEmissaoInicial = edDtInicial.getValue();
		filter.dtEmissaoFinal = edDtFinal.getValue();
		filter.flTipoNfe = cbTipoNfe.getValue();
		return filter;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NfeEstoqueService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_TIPO_NFE), cbTipoNfe, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.AGENDAVISITA_LABEL_PERIODO), getLeft(), getNextY());
    	UiUtil.add(this, edDtInicial, getLeft(), AFTER);
    	UiUtil.add(this, edDtFinal, AFTER + WIDTH_GAP, SAME);
    	UiUtil.add(this, btFiltro, AFTER + WIDTH_GAP, SAME);
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btFiltro || event.target == cbTipoNfe) {
				list();
			}
			break;
		}
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		NfeEstoque nfeEstoque = (NfeEstoque) domain;
		return new String[] {
				 Messages.REMESSAESTOQUE_LABEL_NUNOTAREMESSA + " " + StringUtil.getStringValue(nfeEstoque.nuNotaRemessa),
	             Messages.REMESSAESTOQUE_LABEL_NUSERIEREMESSA + " " + StringUtil.getStringValue(nfeEstoque.nuSerieRemessa),
	             NfeEstoqueService.getInstance().getTipoNfe(nfeEstoque.flTipoNfe),
	             Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(nfeEstoque.vlTotalNfe),
	             Messages.REMESSAESTOQUE_EMISSAO + StringUtil.getStringValue(nfeEstoque.dtEmissao) + " - " + StringUtil.getStringValue(nfeEstoque.hrEmissao) 
		};
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		show(new ListItemNfeEstoqueForm((NfeEstoque)getSelectedDomain()));
	}
	
}
