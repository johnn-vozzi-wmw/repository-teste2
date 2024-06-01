package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;

public class ProdutoGradeDTO {

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdTabelaPreco;
    public String nuCodigoBarras;
    
	public ProdutoGradeDTO() {
		super();
	}
	
	public ProdutoGradeDTO(ProdutoGrade produtoGrade) {
		try {
			FieldMapper.copy(produtoGrade, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public String getCdEmpresa() {
		return cdEmpresa;
	}
	
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public String getCdItemGrade1() {
		return cdItemGrade1;
	}
	
	public String getCdItemGrade2() {
		return cdItemGrade2;
	}
	
	public String getCdItemGrade3() {
		return cdItemGrade3;
	}
	
	public String getCdTabelaPreco() {
		return cdTabelaPreco;
	}
	
	public String getNuCodigoBarras() {
		return nuCodigoBarras;
	}
	
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	
	public ProdutoGradeDTO copy(final ProdutoGrade produtoGrade) {
		try {
			FieldMapper.copy(produtoGrade, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
