package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class CatalogoParams extends BaseDomain {
	
	public static final String EMPTY = "empty";

	public String id;
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String grupoProdutoList;
	public String semPreco;
	public String dsFiltroTexto;
	public String grupoDestaqueList;
	public String estoqueDisponivelList;
	public String cdLocalEstoque;
	
	@Override
	public String getPrimaryKey() {
		return id;
	}
}
