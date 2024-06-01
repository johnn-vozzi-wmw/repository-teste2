package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.ResultSetMetaData;
import totalcross.sql.Statement;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class AdmSqlExecuteForm extends BaseUIForm {

	private BaseButton btExecute;
	private BaseButton btClear;
	private EditMemo edSql;
	private LabelValue lbStatus;
	private BaseGridEdit grid;

	public AdmSqlExecuteForm() {
		super("SQL Executor");
		edSql = new EditMemo("", 6, 20000);
		edSql.setValue("select * from TBLVP");
		btExecute = new BaseButton(UiUtil.getColorfulImage("images/exec.png", UiUtil.getButtonPreferredHeight(), UiUtil.getButtonPreferredHeight(), Color.WHITE));
		btExecute.setBackColor(Color.getRGB(70, 130, 180));
		btExecute.useBorder = false;
		btExecute.transparentBackground = false;
		btClear = new BaseButton(UiUtil.getColorfulImage("images/erase.png", UiUtil.getButtonPreferredHeight(), UiUtil.getButtonPreferredHeight(), Color.WHITE));
		btClear.transparentBackground = false;
		btClear.setBackColor(ColorUtil.buttonExcluirForeColor);
		btClear.useBorder = false;
		lbStatus = new LabelValue("");
		lbStatus.autoMultipleLines = true;
	}

	@Override
	protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
		barBottomContainer.setVisible(false);
		UiUtil.add(this, edSql, getLeft(), getTop() + HEIGHT_GAP, FILL - btClear.getPreferredWidth() - UiUtil.BASE_MARGIN_GAP - WIDTH_GAP, UiUtil.getButtonPreferredHeight() * 2 + btClear.getPreferredHeight());
		UiUtil.add(this, btExecute, RIGHT - UiUtil.BASE_MARGIN_GAP, SAME, PREFERRED, UiUtil.getButtonPreferredHeight() * 2);
		UiUtil.add(this, btClear, RIGHT - UiUtil.BASE_MARGIN_GAP, AFTER, PREFERRED, PREFERRED);
		UiUtil.add(this, lbStatus, getLeft(), BOTTOM - UiUtil.BASE_MARGIN_GAP);
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		if (event.type == ControlEvent.PRESSED) {
			if (event.target == btExecute) {
				if (grid != null) {
					remove(grid);
				}
				String sql = edSql.getText();
				UiUtil.showProcessingMessage();
				try {
					VmUtil.executeGarbageCollector();
					int init = Vm.getTimeStamp();
					int end = 0;
					if (sql.toLowerCase().startsWith("select")) {
						try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
								ResultSet rs = st.executeQuery(sql)) {
							ResultSetMetaData rsmd = rs.getMetaData();
							int n = rsmd.getColumnCount();
							String[] colNames = new String[n];
							for (int i = 1; i <= n; i++) {
								colNames[i - 1] = "			" + rsmd.getColumnLabel(i) + "			";
							}
							grid = new BaseGridEdit(colNames, false);
							UiUtil.add(this,  grid, LEFT, edSql.getY2() + HEIGHT_GAP, FILL, FILL - lbStatus.getPreferredHeight() - HEIGHT_GAP - UiUtil.BASE_MARGIN_GAP);
							String[][] items = CrudDbxDao.getCurrentDriver().getStrings(rs, new Vector(50), Convert.MAX_INT_VALUE);
							grid.setItems(items);
							end = Vm.getTimeStamp() - init;
							updateStatus(items.length + " registros encontrados. " + end + " millis", false);
						}
					} else if (sql.toLowerCase().startsWith("update") || sql.toLowerCase().startsWith("delete") || sql.toLowerCase().startsWith("insert")) {
						int result = CrudDbxDao.getCurrentDriver().executeUpdate(sql);
						end = Vm.getTimeStamp() - init;
						updateStatus(result + " registros alterados. " + end + " millis", false);
					} else {
						throw new ValidationException("Comando incorreto ou não suportado!");
					}
					LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO, LogPda.LOG_CATEGORIA_COMANDO_SQL_EXECUTOR, sql);
				} catch (Throwable e) {
					updateStatus("Erro na execução do sql. " + e.getMessage(), true);
				} finally {
					UiUtil.unpopProcessingMessage();
				}
			} else if (event.target == btClear) {
				updateStatus("", false);
				edSql.setValue("");
				if (grid != null) {
					remove(grid);
				}
				edSql.requestFocus();
			}
		}
	}

	@Override
	public void onFormClose() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(true);
	}

	private void updateStatus(final String s, final boolean erro) {
		lbStatus.setForeColor(erro ? LavendereColorUtil.softRed : LavendereColorUtil.sucessColor);
		lbStatus.setMultipleLinesText(s);
		lbStatus.reposition();
		lbStatus.repaintNow();
	}
	

}
