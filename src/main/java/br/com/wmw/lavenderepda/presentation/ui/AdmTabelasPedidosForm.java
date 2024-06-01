package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabelaDb;
import br.com.wmw.lavenderepda.business.service.ParcelaPedidoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.ui.Grid;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class AdmTabelasPedidosForm extends BaseUIForm {

	private BaseButton btVerIntegridade;
	private BaseButton btRecuperar;
	private BaseButton btRecuperarLocal;
	private BaseGridEdit grid;
	private int qtTabelasCorrompidas = 0;
	public Vector listTabCorrompTemp;

	public AdmTabelasPedidosForm(Vector listTabCorrompTemp) {
		super(Messages.RECUPERACAO_PEDIDO_TITULO);
		btRecuperar = new BaseButton(Messages.RECUPERACAO_BOTAO_RECUP_REMOTA);
		btRecuperarLocal = new BaseButton(Messages.RECUPERACAO_BOTAO_RECUP_LOCAL);
		btVerIntegridade = new BaseButton(Messages.RECUPERACAO_BOTAO_VER_INTEGRIDADE);
		this.listTabCorrompTemp = listTabCorrompTemp;
	}

	protected void onFormStart() throws SQLException {
		barBottomContainer.setVisible(false);
        UiUtil.add(this, new LabelValue("Tabelas com Erros:"), LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, PREFERRED, PREFERRED);
        //--
        addGrid();
        grid.setRect(LEFT + HEIGHT_GAP, AFTER, FILL - HEIGHT_GAP, getHeight() / 3);
        UiUtil.add(this, btVerIntegridade, CENTER, AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
        UiUtil.add(this, btRecuperarLocal, RIGHT - WIDTH_GAP, BOTTOM - HEIGHT_GAP, PREFERRED, PREFERRED + 10);
        UiUtil.add(this, btRecuperar, LEFT + HEIGHT_GAP, BOTTOM - HEIGHT_GAP, PREFERRED, PREFERRED + 10);
        btRecuperar.setBackForeColors(ColorUtil.buttonExcluirForeColor, Color.WHITE);
        //--
        list();
        Grid.repaint();
		UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.RECUPERACAO_TABELAS_PEDIDOS, TYPE_MESSAGE.ERROR);
	}

	protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btRecuperar) {
	    			btRecupRemotoClick();
	    		} else if (event.target == btRecuperarLocal) {
	    			btRecupLocalClick();
	    		} else if (event.target == btVerIntegridade) {
	    			btVerIntegridadeClick();
	    		}
	    	}
    	}
	}

	private void btVerIntegridadeClick() {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		Vector tabelaList;
		try {
			tabelaList = getCheckedTabelas();
			if (!ValueUtil.isEmpty(tabelaList)) {
				StringBuffer strBuffer = new StringBuffer();
				String tableName;
				for (int i = 0; i < tabelaList.size(); i++) {
					tableName = ((TabelaDb)tabelaList.items[i]).nmTabela;
					if (Pedido.TABLE_NAME_PEDIDO.equalsIgnoreCase(tableName)) {
						try {
							PedidoPdbxDao.getInstance().findAllSimple();
						} catch (Throwable e) {
							strBuffer.append(tableName);
							strBuffer.append(Messages.ADM_TABELAS_NAOACESSIVEL);
						}
					}
					if (ItemPedido.TABLE_NAME_ITEMPEDIDO.equalsIgnoreCase(tableName)) {
						try {
							ItemPedidoPdbxDao.getInstance().findAllSimple();
						} catch (Throwable e) {
							if (strBuffer.length() > 0) {
								strBuffer.append("|");
							}
							strBuffer.append(tableName);
							strBuffer.append(Messages.ADM_TABELAS_NAOACESSIVEL);
						}
					}
					if (ParcelaPedido.TABLE_NAME.equalsIgnoreCase(tableName)) {
						try {
							ParcelaPedidoService.getInstance().findAll();
						} catch (Throwable e) {
							if (strBuffer.length() > 0) {
								strBuffer.append("|");
							}
							strBuffer.append(tableName);
							strBuffer.append(Messages.ADM_TABELAS_NAOACESSIVEL);
						}
					}
				}
				if (strBuffer.length() == 0) {
					UiUtil.showSucessMessage(Messages.ADM_TABELAS_DADOS_ACESSIVEL);
				} else {
					UiUtil.showInfoMessage(strBuffer.toString());
				}
			} else {
				UiUtil.showInfoMessage(Messages.SISTEMA_RECUPERARTABELAS_SELECAO);
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		} finally {
			tabelaList = null;
			mb.unpop();
		}
	}

	private void btRecupRemotoClick() {
		// SQLITE recuperar remoto tabelas pedido
	}

	private void btRecupLocalClick() {
		// SQLITE recuperar remoto tabelas pedido
	}

	private void addGrid() {
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(Messages.RECUPERACAO_PDB_NOME, -80, LEFT),
												new GridColDefinition(Messages.RECUPERACAO_PDB_ERRO, -20, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, true);
        UiUtil.add(this, grid);
	}

	public void list() throws java.sql.SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		Vector domainList = null;
		try {
			grid.setItems(null);
			//--
			if (ValueUtil.isEmpty(listTabCorrompTemp)) {
				domainList =  getDomainList();
			} else {
				domainList = listTabCorrompTemp;
			}
			listSize = domainList.size();
			//--
			if (listSize > 0) {
				String [][] gridItems;
				String[] item = getItem(domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItem(domainList.items[i]);
				}
				item = null;
				//--
				grid.setItems(gridItems);
			}
			if (!ValueUtil.isEmpty(listTabCorrompTemp)) {
				listTabCorrompTemp = null;
			}
		} finally {
			domainList = null;
			msg.unpop();
		}
		qtTabelasCorrompidas = listSize;
	}

    protected Vector getDomainList() throws java.sql.SQLException {
    	return new Vector();
    }

	public void clearGrid() {
		grid.removeAllElements();
		grid.clear();
	}

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	TabelaDb tabela = (TabelaDb) domain;
        String[] item = {
                StringUtil.getStringValue(tabela.nmTabela),
                StringUtil.getStringValue(tabela.nuMaiorCarimbo)};
        return item;
    }

    private Vector getCheckedTabelas() {
    	Vector list = new Vector();
		for (int i = 0; i < grid.size(); i++) {
			if (grid.isChecked(i)) {
				TabelaDb tabela = new TabelaDb();
				tabela.nmTabela = grid.getItem(i)[0];
				tabela.nuMaiorCarimbo = ValueUtil.getIntegerValue(grid.getItem(i)[1]);
				list.addElement(tabela);
			}
		}
		return list;
    }

    //@Override
    public void onFormClose() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		grid.setItems(null);
		try {
			if (qtTabelasCorrompidas > 0) {
				UiUtil.showErrorMessage(Messages.RECUPERACAO_TABELAS_PEDIDOS);
			}
		} finally {
			mb.unpop();
		}
    }

}
