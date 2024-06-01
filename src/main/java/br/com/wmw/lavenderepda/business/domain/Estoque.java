package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.EstoqueBuilder;
import br.com.wmw.lavenderepda.business.domain.dto.EstoqueDto;
import totalcross.util.Date;

public class Estoque extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPESTOQUE";

	public static final String CD_LOCAL_ESTOQUE_PADRAO = "0";
	public static final String FLORIGEMESTOQUE_PDA = "P";
	public static final String FLORIGEMESTOQUE_ERP = "E";
	public static final String FLORIGEMESTOQUE_TROCA = "T";
	public static final String FLORIGEMESTOQUE_WEB = "W";

	public static final String COLUMN_NAME_NUCARIMBO = "nuCarimbo";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdLocalEstoque;
	public String flOrigemEstoque;
    public double qtEstoque;
    public double qtEstoquePrevisto;
    public double qtEstoqueMin;
    public Date dtEstoquePrevisto;
    public double qtEstoqueRemessa;

    //Não persistentes
    public boolean flErroEstoqueOnline;
    private Produto produto;
    public String nuNotaRemessa;
    public String nuSerieRemessa;
    public String flModoEstoque;
    public String cdLocalEstoqueDif;
    public boolean estoqueEmpresa;
    public String dsAgrupadorGrade;
	public String nmEmpresaCurto;

    public Estoque(EstoqueBuilder estoqueBuilder) {
    	this(estoqueBuilder, false);
    }
    
    public Estoque(EstoqueBuilder estoqueBuilder, boolean todosEstoques) {
    	this.cdEmpresa = estoqueBuilder.cdEmpresa;
    	this.cdRepresentante = estoqueBuilder.cdRepresentante;
    	this.cdProduto = estoqueBuilder.cdProduto;
    	this.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
    	this.flModoEstoque = estoqueBuilder.flModoEstoque;
    	if (!todosEstoques) {
	    	this.cdItemGrade1 = ValueUtil.isEmpty(cdItemGrade1) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade1;
	    	this.cdItemGrade2 = ValueUtil.isEmpty(cdItemGrade2) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade2;
	    	this.cdItemGrade3 = ValueUtil.isEmpty(cdItemGrade3) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade3;
	    	this.cdLocalEstoque = ValueUtil.isEmpty(cdLocalEstoque) ?  Estoque.CD_LOCAL_ESTOQUE_PADRAO : cdLocalEstoque;
	    	this.qtEstoque = estoqueBuilder.qtEstoque;
    	}
    }
    
    public Estoque() {}

    public Estoque(EstoqueDto estoqueDto) {
    	try {
    		FieldMapper.copy(estoqueDto, this);
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }
    
	//Override
    public boolean equals(Object obj) {
        if (obj instanceof Estoque) {
            Estoque estoque = (Estoque) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, estoque.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, estoque.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, estoque.cdProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, estoque.cdItemGrade1) &&
                ValueUtil.valueEquals(cdItemGrade2, estoque.cdItemGrade2) &&
                ValueUtil.valueEquals(cdItemGrade3, estoque.cdItemGrade3) &&
                ValueUtil.valueEquals(cdLocalEstoque, estoque.cdLocalEstoque) &&
            	ValueUtil.valueEquals(flOrigemEstoque, estoque.flOrigemEstoque);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade1);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade2);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade3);
    	strBuffer.append(";");
    	strBuffer.append(cdLocalEstoque);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemEstoque);
        return strBuffer.toString();
    }
    
    @Override
    public String getPartialPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	return strBuffer.toString();
    }

	public Produto getProduto() {
		if (produto != null) {
			return produto;
		}
		return produto = new Produto();
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public String getKeyItemGradeEstoque() {
		return getKeyItemGrade(cdItemGrade1, cdItemGrade2, cdItemGrade3);
	}
	
	public static String getKeyItemGrade(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) {
		return cdItemGrade1 + ";" + cdItemGrade2 + ";" + cdItemGrade3;
	}

}