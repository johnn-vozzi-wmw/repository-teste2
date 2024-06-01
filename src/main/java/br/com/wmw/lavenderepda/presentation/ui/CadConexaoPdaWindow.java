package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;

public class CadConexaoPdaWindow extends WmwCadWindow {

    private EditText edDsConexao;
    private EditMemo edDsUrlWebService;

    private ButtonGroupBoolean bgFlDefault;

    public CadConexaoPdaWindow() {
		super(Messages.CONEXAOPDA_TITULO_CADASTRO);
		edDsConexao = new EditText("@@@@@@@@@@", 30);
		edDsUrlWebService = new EditMemo("@@@@@@@@@@", 3, 100);
	    bgFlDefault = new ButtonGroupBoolean();
		bgFlDefault.setValue(ValueUtil.VALOR_NI);
		setDefaultRect();
    }

	//@Override
	protected String getEntityDescription() {
		return title;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ConexaoPdaService.getInstance();
	}

	//@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ConexaoPda();
	}

	//@Override
	protected BaseDomain screenToDomain() throws SQLException {
		ConexaoPda conexaoPda = (ConexaoPda) getDomain();
		conexaoPda.dsConexao = edDsConexao.getValue();
		conexaoPda.dsUrlWebService = edDsUrlWebService.getValue();
		conexaoPda.flDefault = bgFlDefault.getValue();
		return conexaoPda;
	}

	//@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		ConexaoPda conexaoPda = (ConexaoPda) domain;
		edDsConexao.setValue(conexaoPda.dsConexao);
		edDsUrlWebService.setValue(conexaoPda.dsUrlWebService);
		bgFlDefault.setValue(conexaoPda.flDefault);
		if (!BaseDomain.FLTIPOALTERACAO_INSERIDO.equals(getDomain().flTipoAlteracao)) {
			btExcluir.setVisible(false);
		} else {
			btExcluir.setVisible(true);
		}
	}

	//@Override
	protected void clearScreen() throws java.sql.SQLException {
		edDsConexao.setText("");
		edDsUrlWebService.setText("http://");
		bgFlDefault.setValue(ValueUtil.VALOR_NAO);
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.CONEXAOPDA_LABEL_DSCONEXAO), edDsConexao, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CONEXAOPDA_LABEL_DSURLWEBSERVICE), edDsUrlWebService, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CONEXAOPDA_LABEL_FLDEFAULT), bgFlDefault, getLeft(), getNextY());
	}

}