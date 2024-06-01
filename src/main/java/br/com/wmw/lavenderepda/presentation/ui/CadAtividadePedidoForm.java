package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;
import br.com.wmw.lavenderepda.business.service.AtividadePedidoService;

public class CadAtividadePedidoForm extends BaseLavendereCrudPersonCadForm {
	
	private LabelValue lvStatusAtividade;

	public CadAtividadePedidoForm(AtividadePedido atividadePedido) {
		super(Messages.ATIVIDADEPEDIDO_DETALHES);
		lvStatusAtividade = new LabelValue();
		hideComponents();
		setReadOnly();
	}

	@Override
	protected String getDsTable() {
		return AtividadePedido.TABLE_NAME;
	}

	@Override
	protected BaseDomain createDomain() {
		return new AtividadePedido();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() {
		return AtividadePedidoService.getInstance();
	}
	
	@Override
	protected void addComponentesFixosInicio() {
		UiUtil.add(this, new LabelName(Messages.ATIVIDADEPEDIDO_LABEL_STATUS_ATIVIDADE), lvStatusAtividade, getLeft(), TOP + HEIGHT_GAP);
	}
	
	
	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		AtividadePedido atividadePedido = (AtividadePedido) domain;
		lvStatusAtividade.setValue(atividadePedido.statusAtividade.toString());
		super.domainToScreen(domain);
	}
		
	private void hideComponents() {
    	barTopContainer.setVisible(false);
    	barBottomContainer.setVisible(false);
    }
	

}
