package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.Produto;

public class ProdutoDTO {

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFornecedor;
    public String cdProduto;
    public String dsProduto;
    
	public ProdutoDTO() {
		super();
	}
    
	public ProdutoDTO copy(final Produto produto) {
		try {
			FieldMapper.copy(produto, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public String getCdFornecedor() {
		return cdFornecedor;
	}

	public String getCdProduto() {
		return cdProduto;
	}

	public String getDsProduto() {
		return dsProduto;
	}
	
}
