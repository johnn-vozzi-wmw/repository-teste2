package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.CatalogoParams;

public class CatalogoProdutoDTO {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String grupoProdutoList;
	public String semPreco;
	public String dsFiltroTexto;
	public String grupoDestaqueList;
	public String estoqueDisponivelList;
	public String cdLocalEstoque;
	public String id;
	public String cdUsuario;
	
	public CatalogoProdutoDTO(final CatalogoParams catalogo) {
		super();
		try {
			FieldMapper.copy(catalogo, this);
		}catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}

	public void setCdTabelaPreco(String cdTabelaPreco) {
		this.cdTabelaPreco = cdTabelaPreco;
	}

	public String getGrupoProdutoList() {
		return grupoProdutoList;
	}

	public void setGrupoProdutoList(String grupoProdutoList) {
		this.grupoProdutoList = grupoProdutoList;
	}

	public String getSemPreco() {
		return semPreco;
	}

	public void setSemPreco(String semPreco) {
		this.semPreco = semPreco;
	}

	public String getDsFiltroTexto() {
		return dsFiltroTexto;
	}

	public void setDsFiltroTexto(String dsFiltroTexto) {
		this.dsFiltroTexto = dsFiltroTexto;
	}

	public String getGrupoDestaqueList() {
		return grupoDestaqueList;
	}

	public void setGrupoDestaqueList(String grupoDestaqueList) {
		this.grupoDestaqueList = grupoDestaqueList;
	}

	public String getEstoqueDisponivelList() {
		return estoqueDisponivelList;
	}

	public void setEstoqueDisponivelList(String estoqueDisponivelList) {
		this.estoqueDisponivelList = estoqueDisponivelList;
	}

	public String getCdLocalEstoque() {
		return cdLocalEstoque;
	}

	public void setCdLocalEstoque(String cdLocalEstoque) {
		this.cdLocalEstoque = cdLocalEstoque;
	}

	public String getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(String cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
