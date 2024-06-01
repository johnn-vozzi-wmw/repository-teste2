package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Restricao extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESTRICAO";

	public static final String VALOR_PADRAO = "0";

	public static final int NUTIPOVIGENCIA_DIARIO = 1;
	public static final int NUTIPOVIGENCIA_SEMANAL = 2;
	public static final int NUTIPOVIGENCIA_MENSAL = 3;
	public static final int NUTIPOVIGENCIA_PERSONALIZADA = 4;

	public static final int NUNIVELCLIENTE_ALTO = 1;
	public static final int NUNIVELCLIENTE_DETALHADO = 2;

	public static final int NUNIVELPRODUTO_ALTO = 1;
	public static final int NUNIVELPRODUTO_DETALHADO = 2;

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdRestricao;
	public int nuTipoVigenciaRestricao;
	public int nuNivelCliente;
	public int nuNivelProduto;
	public String dsRestricao;
	public Date dtInicialVigencia;
	public Date dtFimVigencia;

	//-- Não persistentes
	public RestricaoProduto restricaoProdutoFilter;
	public RestricaoCliente restricaoClienteFilter;
	public ItemPedido itemPedidoFilter;

	public boolean dynamicProduto;
	public boolean dynamicCliente;

	public boolean dynamicQuantidade;
	public String qtdDynamicFilter;

	//-- Filtros Vigência
	public Date dtInicialVigenciaFilterDiario;
	public Date dtFimVigenciaFilterDiario;
	public Date dtInicialVigenciaFilterSemanal;
	public Date dtFimVigenciaFilterSemanal;
	public Date dtInicialVigenciaFilterMensal;
	public Date dtFimVigenciaFilterMensal;
	public Date dtInicialVigenciaFilterPersonalizado;
	public Date dtFimVigenciaFilterPersonalizado;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdRestricao);
		return primaryKey.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Restricao)) return false;
		Restricao that = (Restricao) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa)
			&& ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante)
			&& ValueUtil.valueEquals(cdRestricao, that.cdRestricao);
	}

}