package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PontExtItemPed;

public class TipoMovimentacaoExtratoPontuacaoComboBox extends BaseComboBox {

	public TipoMovimentacaoExtratoPontuacaoComboBox() {
		super(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO);
		this.defaultItemType = DefaultItemType_ALL;
		load();
	}

	public void setValue(String valor) {
		if (PontExtItemPed.TIPO_LANCAMENTO_NOVO_PEDIDO.equals(valor)) {
			select(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_NOVO_PED);
			return;
		} 
		if (PontExtItemPed.TIPO_LANCAMENTO_NOVO_PEDIDO.equals(valor)) {
			select(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_DEVOLUCAO);
			return;
		} 
		if (PontExtItemPed.TIPO_LANCAMENTO_NOVO_PEDIDO.equals(valor)) {
			select(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_ESTORNO);
			return;
		}
		if (PontExtItemPed.TIPO_LANCAMENTO_CANCELAMENTO.equals(valor)) {
			select(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_CANCELAMENTO);
			return;
		}
		select(null);
	}

	public String getValue() {
		String valor = (String) getSelectedItem();
		if (Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_NOVO_PED.equals(valor)) {
			return PontExtItemPed.TIPO_LANCAMENTO_NOVO_PEDIDO;
		} 
		if (Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_DEVOLUCAO.equals(valor)) {
			return PontExtItemPed.TIPO_LANCAMENTO_DEVOLUCAO;
		}
		if (Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_ESTORNO.equals(valor)) {
			return PontExtItemPed.TIPO_LANCAMENTO_ESTORNO;
		}
		if (Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_CANCELAMENTO.equals(valor)) {
			return PontExtItemPed.TIPO_LANCAMENTO_CANCELAMENTO;
		}
		return ValueUtil.VALOR_NI;
	}

	private void load() {
		removeAll();
		addDefaultItens();
		setSelectedIndex(0);
		add(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_NOVO_PED);
		add(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_DEVOLUCAO);
		add(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_ESTORNO);
		add(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_CANCELAMENTO);
	}
	
}
