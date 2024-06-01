package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;

public class ItemPedidoGradeDTO {

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public double qtItemFisico;
    public String dsItemGrade1;
    public String dsItemGrade2;
    public String dsItemGrade3;
    public ProdutoGradeDTO[] produtoGradeDTO;
    
    public ItemPedidoGradeDTO() {
		super();
	}

	public ProdutoGradeDTO[] getProdutoGradeDTO() {
		return produtoGradeDTO;
	}

	public void setProdutoGradeDTO(ProdutoGradeDTO[] produtoGradeDTO) {
		this.produtoGradeDTO = produtoGradeDTO;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}

	public String getNuPedido() {
		return nuPedido;
	}

	public String getCdProduto() {
		return cdProduto;
	}

	public String getFlTipoItemPedido() {
		return flTipoItemPedido;
	}

	public int getNuSeqProduto() {
		return nuSeqProduto;
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

	public double getQtItemFisico() {
		return qtItemFisico;
	}

	public String getDsItemGrade1() {
		return dsItemGrade1;
	}

	public String getDsItemGrade2() {
		return dsItemGrade2;
	}

	public String getDsItemGrade3() {
		return dsItemGrade3;
	}
	
	public ItemPedidoGradeDTO copy(ItemPedidoGrade itemPedidoGrade) {
		try {
			FieldMapper.copy(itemPedidoGrade, this);
			ProdutoGrade produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeByItemPedidoGrade(itemPedidoGrade);
			this.dsItemGrade1 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade1, cdItemGrade1);
			this.dsItemGrade2 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade2, cdItemGrade2);
			this.dsItemGrade3 = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade3, cdItemGrade3);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
