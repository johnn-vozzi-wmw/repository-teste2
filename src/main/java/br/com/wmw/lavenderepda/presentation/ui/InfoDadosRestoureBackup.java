package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;

public class InfoDadosRestoureBackup extends WmwCadWindow {
	
	private String[][] entidadesTab;
	private int quantidadeQtd;
	public EditText edConfirmar;
	private LabelValue labelDadosPerdidos;
	
	public InfoDadosRestoureBackup(String newTitle, String[][] entidades, int quantidade) {
		super(newTitle);
		entidadesTab = entidades;
		quantidadeQtd = quantidade;
		setBackForeColors(ColorUtil.formsBackColor, ColorUtil.sessionContainerForeColor);
		edConfirmar = new EditText("", 50);
		labelDadosPerdidos = new LabelValue("");
		labelDadosPerdidos.setForeColor(ColorUtil.softRed);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(scBase, MessageUtil.quebraLinhas(MessageUtil.getMessage(Messages.BACKUP_PEDIDOS_MSG_RESTAURAR_CONFIRM, Messages.BACKUP_CONFERENCIA_CONFIRMO), width - WIDTH_GAP_BIG), getLeft(), getNextY());
		UiUtil.add(scBase, edConfirmar, getLeft(), getNextY());
		if (quantidadeQtd > 0) {
			UiUtil.add(scBase, MessageUtil.quebraLinhas(Messages.BACKUP_RESTAURACAO_DADOS_PERDIDOS, width - WIDTH_GAP_BIG), getLeft(), getNextY());
			for (int i = 0; i < quantidadeQtd; i++) {
				labelDadosPerdidos = new LabelValue(MessageUtil.getMessage(Messages.BACKUP_RESTAURACAO_TABELA_QUANTIDADE_REGISTRO, new String[] {entidadesTab[i][0] , entidadesTab[i][1]}));
				labelDadosPerdidos.setForeColor(ColorUtil.softRed);
				UiUtil.add(scBase, labelDadosPerdidos, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
	}
	
	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		return null;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		
	}

	@Override
	protected void clearScreen() throws SQLException {
		
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return null;
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		
	}

	@Override
	protected void salvarClick() throws SQLException {
		fecharWindow();
	}
	
}