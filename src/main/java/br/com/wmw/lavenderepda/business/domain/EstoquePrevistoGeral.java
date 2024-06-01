package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;
import totalcross.util.Vector;

public class EstoquePrevistoGeral extends BaseDomain {

	public static String TABLE_NAME = "TBLVPESTOQUEPREVISTOGERAL";
	public static int NUDIAS_FIXO = 1;

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdLocalEstoque;
    public String flOrigemEstoque;
    public Date dtEstoque;
    public double qtEstoque;
    public Date dtAlteracao;
    public String hrAlteracao;
    public String flEstoqueFisico;
    
    public String[] cdLocalEstoqueList;

    public EstoquePrevistoGeral() { }

    public EstoquePrevistoGeral(String cdEmpresa, String cdRepresentante, String cdProduto, Date dtEntrega) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
		this.dtEstoque = getDtReferenciaEstoque(dtEntrega);
		this.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		this.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		this.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		this.cdLocalEstoque = Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		this.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
	}
    public EstoquePrevistoGeral(String cdEmpresa, String cdRepresentante, String cdProduto, Date dtEntrega, String flEstoqueFisico) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdProduto = cdProduto;
    	this.dtEstoque = getDtReferenciaEstoque(dtEntrega);
    	this.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
    	this.flEstoqueFisico = flEstoqueFisico;
    }

    public EstoquePrevistoGeral(String cdEmpresa, String cdRepresentante, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
	}

	private Date getDtReferenciaEstoque(Date dtEntrega) {
		Date dtReferenciaEstoque = DateUtil.getDateValue(dtEntrega);
		if (dtReferenciaEstoque != null) dtReferenciaEstoque.advance(-LavenderePdaConfig.nuDiasPrevisaoEntrega + NUDIAS_FIXO);
		return dtReferenciaEstoque;
	}
	
	//Override
    public boolean equals(Object obj) {
        if (obj instanceof EstoquePrevistoGeral) {
            EstoquePrevistoGeral estoquePrevisto = (EstoquePrevistoGeral) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, estoquePrevisto.cdEmpresa) && 
                ValueUtil.valueEquals(cdProduto, estoquePrevisto.cdProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, estoquePrevisto.cdItemGrade1) && 
                ValueUtil.valueEquals(cdItemGrade2, estoquePrevisto.cdItemGrade2) && 
                ValueUtil.valueEquals(cdItemGrade3, estoquePrevisto.cdItemGrade3) && 
                ValueUtil.valueEquals(cdLocalEstoque, estoquePrevisto.cdLocalEstoque) && 
                ValueUtil.valueEquals(flOrigemEstoque, estoquePrevisto.flOrigemEstoque) && 
                ValueUtil.valueEquals(dtEstoque, estoquePrevisto.dtEstoque);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade1);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade2);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade3);
        primaryKey.append(";");
        primaryKey.append(cdLocalEstoque);
        primaryKey.append(";");
        primaryKey.append(flOrigemEstoque);
        primaryKey.append(";");
        primaryKey.append(DateUtil.formatDateDb(dtEstoque));
        return primaryKey.toString();
    }

}