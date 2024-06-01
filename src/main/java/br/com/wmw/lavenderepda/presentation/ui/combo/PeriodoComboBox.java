package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.service.MetasPorClienteService;
import br.com.wmw.lavenderepda.business.service.MetasPorFornService;
import br.com.wmw.lavenderepda.business.service.MetasPorGrupoProdutoService;
import br.com.wmw.lavenderepda.business.service.MetasPorProdutoService;
import br.com.wmw.lavenderepda.business.service.ProducaoProdService;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorWmwService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorIndicadorPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;


public class PeriodoComboBox extends BaseComboBox {

	public static final int PERIODOVALORINDICADOR = 1;
	public static final int PERIODOMETASFORNECEDOR = 2;
	public static final int PERIODOMETASPRODUTO = 3;
	public static final int PERIODOMETASCLIENTE = 4;
	public static final int PERIODOMETASGRUPOPRODUTOPRODUTO1 = 5;
	public static final int PERIODOPRODUCAOPRODREP = 6;

	public PeriodoComboBox(int cdPeriodo) throws SQLException {
		super(cdPeriodo == PERIODOMETASGRUPOPRODUTOPRODUTO1 ? Messages.METAS_META : Messages.VALORINDICADOR_PERIODO);
		load(cdPeriodo);
	}

	public String getValue() {
		String indicador = (String)getSelectedItem();
		if (indicador != null) {
			return indicador;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			select(value);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load(int cdPeriodo) throws SQLException {
		if (cdPeriodo == PERIODOVALORINDICADOR) {
			Vector periodos = ValorIndicadorPdbxDao.getInstance().findAllDistinctPeriodo();
			if ((periodos.indexOf(ValorIndicador.PERIODO_TICKET_MEDIO) == -1) && (LavenderePdaConfig.geraApresentaTicketMedioDiario || LavenderePdaConfig.ticketMedioEmpresa >= 0)) {
	        	periodos.addElement(ValorIndicador.PERIODO_TICKET_MEDIO);
	        }
			add(periodos);
			add(ValorIndicadorWmwService.getInstance().findDistinctPeriodoList());
		} else if (cdPeriodo == PERIODOMETASFORNECEDOR) {
			add(MetasPorFornService.getInstance().findAllDistinctPeriodo());
		} else if (cdPeriodo == PERIODOMETASPRODUTO) {
			add(MetasPorProdutoService.getInstance().findAllDistinctPeriodo());
		} else if (cdPeriodo == PERIODOMETASCLIENTE) {
			add(MetasPorClienteService.getInstance().findAllDistinctPeriodo());
		} else if (cdPeriodo == PERIODOMETASGRUPOPRODUTOPRODUTO1) {
			add(MetasPorGrupoProdutoService.getInstance().findAllDistinctPeriodo());
		} else if (cdPeriodo == PERIODOPRODUCAOPRODREP) {
			Vector list = ProducaoProdService.getInstance().findAllDistinctPeriodo();
			add(list);
			setPeriodoAtual(list);
		}
	}
	
	public void reload(int cdPeriodo) throws SQLException {
		removeAll();
		load(cdPeriodo);
		setSelectedIndex(0);
		repaint();
	}

	private void setPeriodoAtual(Vector list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			String periodo = (String) list.items[i];
			Date dtInicial = DateUtil.getDateValue((StringUtil.split(periodo, '-'))[0].trim());
			Date dtFinal = DateUtil.getDateValue((StringUtil.split(periodo, '-'))[1].trim());
			if (DateUtil.isChosenDtBetweenDates(dtInicial, dtFinal, DateUtil.getCurrentDate())) {
				setSelectedItem(periodo);
				break;
			}
		}
	}
}
