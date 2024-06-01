package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FaceamentoEstoque extends BaseDomain {

    public static String TABLE_NAME = "TBLVPFACEAMENTOESTOQUE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public Date dtCadastro;
    public String flUltilizadoPedidoDtAtual;
    public double qtEstoqueAtual;
    public String hrCadastro;
    public double vlFatorFaceamento;
    public double qtSugestaoVenda;
    public double qtSugestaoVendaRep;

    public FaceamentoEstoque() {
	}
    
    public FaceamentoEstoque(String cdEmpresa, Date dataCadastro) {
		this.cdEmpresa = cdEmpresa;
		this.dtCadastro = dataCadastro;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof FaceamentoEstoque) {
            FaceamentoEstoque faceamEntoestoque = (FaceamentoEstoque) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, faceamEntoestoque.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, faceamEntoestoque.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, faceamEntoestoque.cdCliente) &&
                ValueUtil.valueEquals(cdProduto, faceamEntoestoque.cdProduto) &&
                ValueUtil.valueEquals(dtCadastro, faceamEntoestoque.dtCadastro);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(dtCadastro);
        return primaryKey.toString();
    }

}