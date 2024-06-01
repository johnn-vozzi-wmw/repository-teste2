package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;

public class EntidadeComboBox extends BaseComboBox {
	
	
	public EntidadeComboBox() {
		super(Messages.LABEL_ENTIDADE_COMBOBOX);
		defaultItemType = DefaultItemType_ALL;
		load();
	}
	
	public String getValue() {
		return (String)getSelectedItem();
	}
	
	private void load() {
		removeAll();
		addDefaultItens();
		add(Cliente.APPOBJ_CAMPOS_FILTRO_CLIENTE);
		add(Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO);
		if (LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado) {
			add(NovoCliente.APPOBJ_CAMPOS_FILTRO_NOVO_CLIENTE);
		}
		if (LavenderePdaConfig.geraNovidadePesquisaMercado()) {
			add(PesquisaMercadoConfig.APPOBJ_CAMPOS_FILTRO_PESQUISA_MERCADO);
		}
		if (LavenderePdaConfig.geraNovidadeAutorizacao) {
			add(SolAutorizacao.APPOBJ_CAMPOS_FILTRO_SOL_AUTORIZACAO);
		}
	}
	
	public boolean isFiltroPorCliente () {
		String value = getValue();
		return value == null || ValueUtil.valueEquals(Cliente.APPOBJ_CAMPOS_FILTRO_CLIENTE, value);
	}
	
	public boolean isFiltroPorProduto () {
		String value = getValue();
		return value == null || ValueUtil.valueEquals(Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO, value);
	}

	public boolean isFiltroPorNovoCliente() {
		String value = getValue();
		return value == null || ValueUtil.valueEquals(NovoCliente.APPOBJ_CAMPOS_FILTRO_NOVO_CLIENTE, value);
	}

	public boolean isFiltroPorPesquisaMercado() {
		String value = getValue();
		return value == null || ValueUtil.valueEquals(PesquisaMercadoConfig.APPOBJ_CAMPOS_FILTRO_PESQUISA_MERCADO, value);
	}

	public boolean isFiltroPorSolAutorizacao() {
		String value = getValue();
		return value == null || ValueUtil.valueEquals(SolAutorizacao.APPOBJ_CAMPOS_FILTRO_SOL_AUTORIZACAO, value);
	}
}
