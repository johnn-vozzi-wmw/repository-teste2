package br.com.wmw.lavenderepda.presentation.ui.combo;


import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;

public class EstoqueDisponivelComboBox extends BaseMultiComboBox {
	
	public EstoqueDisponivelComboBox() {
		super(Messages.ESTOQUE_COMBO_TITULO);
		load();
	}
	
	public String getValue() {
		Object[] items = getSelectedItems();
		String cdStatusEstoque = "";
		for(int i = 0; i < items.length; i++) {
			cdStatusEstoque += ((EstoqueDisponivel)items[i]).cdEstoqueDisponivel +";";
		}
		return cdStatusEstoque;
	}
	
	public void setSelectItens(String value) {
		unselectAll();
		String[] values = value != null ? value.split(";") : null;
		if (ValueUtil.isNotEmpty(values)) {
			for (int i = 0; i < values.length; i++) {
				select(new EstoqueDisponivel(values[i]));
			}
		}
	}

	private void load() {
		add(new EstoqueDisponivel(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_1, Messages.ESTOQUE_COMBO_COM_DISPONIBILIDADE));
		add(new EstoqueDisponivel(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_2, Messages.ESTOQUE_COMBO_SEM_DISPONIBILIDADE));
		add(new EstoqueDisponivel(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_3, Messages.ESTOQUE_COMBO_COM_PREVISAO_ESTOQUE));
		setSelectedItensDefault();
	}
	
	public void setSelectedItensDefault() {
		if (LavenderePdaConfig.isUsaOpcaoDefaultFiltroEstoqueDisponivel()) {
			setSelectItens(LavenderePdaConfig.getOpcaoDefaultFiltroEstoqueDisponivel());
		} else {
			unselectAll();
		}
	}

}
