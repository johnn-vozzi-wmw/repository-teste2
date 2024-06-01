package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.ProdutoGradeBuilder;

public class ProdutoGrade extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOGRADE";

	public static final String CD_ITEM_GRADE_PADRAO = "0";
	public static final String CDTABELAPRECO_PADRAO = "0";

	public static final String INFO_COMPL_VOLUME_1 = "1";
	public static final String INFO_COMPL_VOLUME_2 = "2";
	public static final String INFO_COMPL_VOLUME_3 = "3";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdTabelaPreco;
    public String cdTipoItemGrade1;
    public String cdTipoItemGrade2;
    public String cdTipoItemGrade3;
    public String nuCodigoBarras;
    public String flInfoComplVolume;
    public double vlAlturaMin;
    public double vlAlturaMax;
    public double vlLarguraMin;
    public double vlLarguraMax;
    public double vlComprimentoMin;
    public double vlComprimentoMax;
    //--
    public String dsProdutoGrade;
    public int qtItemFisico;
    public double vlAltura;
    public double vlLargura;
    public double vlComprimento;
	public double vlVolume;
	public double vlIndiceVolume;
	public double vlBaseItemTabelaPreco;
	public double vlBaseItemPedido;
	public double vlItemPedido;
	public double vlUnidadePadrao;
	public double qtPeso;
	
	public int nuOrdemLinha;
	public int nuOrdemColuna;

	public RelNovidadeProd relNovidadeProdFilter;
	public String dsAgrupadorGrade;
	public String cdCliente;
	public String flTipoRelacao;
	public String cdMarcador;
    
    public ProdutoGrade(ProdutoGradeBuilder produtoGradeBuilder) {
    	this.cdEmpresa = produtoGradeBuilder.cdEmpresa;
		this.cdRepresentante = produtoGradeBuilder.cdRepresentante;
		this.cdProduto = produtoGradeBuilder.cdProduto;
		this.cdTabelaPreco = produtoGradeBuilder.cdTabelaPreco;
    	this.cdItemGrade1 = produtoGradeBuilder.cdItemGrade1;
    	this.cdItemGrade2 = produtoGradeBuilder.cdItemGrade2;
    	this.cdItemGrade3 = produtoGradeBuilder.cdItemGrade3;
    }
    
    public ProdutoGrade() {
    	
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoGrade) {
            ProdutoGrade produtoGrade = (ProdutoGrade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoGrade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtoGrade.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, produtoGrade.cdProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, produtoGrade.cdItemGrade1) &&
                ValueUtil.valueEquals(cdItemGrade2, produtoGrade.cdItemGrade2) &&
                ValueUtil.valueEquals(cdItemGrade3, produtoGrade.cdItemGrade3) &&
                ValueUtil.valueEquals(cdTabelaPreco, produtoGrade.cdTabelaPreco);
        }
        return false;
    }

    @Override
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
        primaryKey.append(cdTabelaPreco);
        return primaryKey.toString();
    }
    
    @Override
	public String getPartialPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		return primaryKey.toString();
	}

	public boolean showPopUpInfosComplementares() {
		return INFO_COMPL_VOLUME_1.equals(flInfoComplVolume) || INFO_COMPL_VOLUME_2.equals(flInfoComplVolume) || INFO_COMPL_VOLUME_3.equals(flInfoComplVolume);
	}
	
	public static String getGradeKey(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) {
		return cdItemGrade1 + ";" + cdItemGrade2 + ";" + cdItemGrade3;
	}
	
	public String getProdutoGradeKey() {
		return getGradeKey(cdItemGrade1, cdItemGrade2, cdItemGrade3);
	}

}