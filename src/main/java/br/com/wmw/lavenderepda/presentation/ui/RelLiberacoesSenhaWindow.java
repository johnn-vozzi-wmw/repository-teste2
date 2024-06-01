package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.ui.Button;
import totalcross.util.Vector;

public class RelLiberacoesSenhaWindow extends WmwWindow {
	
	private String cdCliente;
	Vector liberacoesSenhaList;

	public RelLiberacoesSenhaWindow(String cdCliente) throws SQLException {
		super(Messages.REL_LIBERACOES_SENHA_TITULO);
		this.cdCliente = cdCliente;
		loadFuncionalidadesLiberadas();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		boolean addSeparador = false;
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			ConfigInterno configInterno = getLiberacaoSenha(ConfigInternoService.getInstance().getVlChaveLiberacaoTipoPedido(cdCliente));
			if (configInterno != null && ValueUtil.isNotEmpty(configInterno.vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(configInterno.vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate())) {
				String[] vlConfigInterno = configInterno.vlConfigInterno.split("-");
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_TIPO_PEDIDO), new LabelValue(FrameworkMessages.VALOR_SIM), getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_HORA_LIBERACAO), new LabelValue(vlConfigInterno[1]), getLeft(), getNextY());
			} else {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_TIPO_PEDIDO), new LabelValue(FrameworkMessages.VALOR_NAO), getLeft(), getNextY());
			}
			addSeparador = true;
		}
		if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
			if (addSeparador) {
				Button sep1 = new Button("");
				sep1.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
				UiUtil.add(this, sep1, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - WIDTH_GAP_BIG, 1);
			}
			ConfigInterno configInterno = getLiberacaoSenha(ConfigInternoService.getInstance().getVlChaveLiberacaoClienteAtrasado(cdCliente));
			if (ValueUtil.isNotEmpty(configInterno.vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(configInterno.vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate())) {
				String[] vlConfigInterno = configInterno.vlConfigInterno.split("-");
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_CLIENTE_ATRASADO), new LabelValue(FrameworkMessages.VALOR_SIM), getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_HORA_LIBERACAO), new LabelValue(vlConfigInterno[1]), getLeft(), getNextY());
			} else {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_CLIENTE_ATRASADO), new LabelValue(FrameworkMessages.VALOR_NAO), getLeft(), getNextY());
			}
			addSeparador = true;
		}
		if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
			if (addSeparador) {
				Button sep2 = new Button("");
				sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
				UiUtil.add(this, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - WIDTH_GAP_BIG, 1);
			}
			ConfigInterno configInterno = getLiberacaoSenha(ConfigInternoService.getInstance().getVlChaveLiberacaoLimiteCredito(cdCliente));
			if (ValueUtil.isNotEmpty(configInterno.vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(configInterno.vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate())) {
				String[] vlConfigInterno = configInterno.vlConfigInterno.split("-");
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_LIMITE_CREDITO), new LabelValue(FrameworkMessages.VALOR_SIM), getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_HORA_LIBERACAO), new LabelValue(vlConfigInterno[1]), getLeft(), getNextY());
			} else {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_LIMITE_CREDITO), new LabelValue(FrameworkMessages.VALOR_NAO), getLeft(), getNextY());
			}
			addSeparador = true;
		}
		if (LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
			if (addSeparador) {
				Button sep3 = new Button("");
				sep3.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
				UiUtil.add(this, sep3, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - WIDTH_GAP_BIG, 1);
			}
			ConfigInterno configInterno = getLiberacaoSenha(ConfigInternoService.getInstance().getVlChaveLiberacaoVisitaForaAgenda(cdCliente));
			if (configInterno != null && ValueUtil.isNotEmpty(configInterno.vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(configInterno.vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate())) {
				String[] vlConfigInterno = configInterno.vlConfigInterno.split("-");
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_VISITA_FORA_AGENDA), new LabelValue(FrameworkMessages.VALOR_SIM), getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_HORA_LIBERACAO), new LabelValue(vlConfigInterno[1]), getLeft(), getNextY());
			} else {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERACAO_VISITA_FORA_AGENDA), new LabelValue(FrameworkMessages.VALOR_NAO), getLeft(), getNextY());
			}
		}
		if (LavenderePdaConfig.liberaComSenhaSaldoBonificacaoExtrapolado) {
			if (addSeparador) {
				Button sep3 = new Button("");
				sep3.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
				UiUtil.add(this, sep3, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - WIDTH_GAP_BIG, 1);
			}
			ConfigInterno configInterno = getLiberacaoSenha(ConfigInternoService.getInstance().getVlChaveLiberacaoPedidoBloqueadoSaldoBonificacao(cdCliente));
			if (configInterno != null && ValueUtil.isNotEmpty(configInterno.vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(configInterno.vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate())) {
				String[] vlConfigInterno = configInterno.vlConfigInterno.split("-");
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERASENHABONIFICACAOSALDO), new LabelValue(FrameworkMessages.VALOR_SIM), getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_HORA_LIBERACAO), new LabelValue(vlConfigInterno[1]), getLeft(), getNextY());
			} else {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_LIBERASENHABONIFICACAOSALDO), new LabelValue(FrameworkMessages.VALOR_NAO), getLeft(), getNextY());
			}
		}
	}
	
	private void loadFuncionalidadesLiberadas() throws SQLException {
		liberacoesSenhaList = ConfigInternoService.getInstance().findAllLiberacoesSenha();
		liberacoesSenhaList = ConfigInternoService.getInstance().getLiberacoesByCliente(cdCliente, liberacoesSenhaList);
	}
	
	private ConfigInterno getLiberacaoSenha(String vlChave) {
		if (ValueUtil.isNotEmpty(liberacoesSenhaList)) {
			for (int i = 0; i < liberacoesSenhaList.size(); i++) {
				ConfigInterno configInterno = (ConfigInterno) liberacoesSenhaList.items[i];
				if (configInterno.vlChave.equals(vlChave)) {
					return configInterno;
				}
			}
		}
		return new ConfigInterno();
	}

}
