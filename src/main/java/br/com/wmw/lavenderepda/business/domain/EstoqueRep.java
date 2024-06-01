package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class EstoqueRep extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPESTOQUEREP";
	public static final String TIPO_DEVOLUCAO_COMPLETA = "C";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdLocalEstoque;
    public double qtEstoque;
    public Date dtEstoque;
    public String hrEstoque;
    public String cdEstoqueRep;
    public String flTipoDevolucao;
    
    //Não persistentes
    public boolean deleteRegistroEnviadoServidor;
    public double vlUnitario;
    public double vlTotal;
    public String dsProduto;
    public Date dtEstoqueFilter;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof EstoqueRep) {
            EstoqueRep estoqueRep = (EstoqueRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, estoqueRep.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, estoqueRep.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, estoqueRep.cdProduto) && 
                ValueUtil.valueEquals(cdItemGrade1, estoqueRep.cdItemGrade1) && 
                ValueUtil.valueEquals(cdItemGrade2, estoqueRep.cdItemGrade2) && 
                ValueUtil.valueEquals(cdItemGrade3, estoqueRep.cdItemGrade3) && 
                ValueUtil.valueEquals(cdLocalEstoque, estoqueRep.cdLocalEstoque);
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
        return primaryKey.toString();
    }

}