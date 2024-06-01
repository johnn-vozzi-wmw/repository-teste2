package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PlataformaVendaProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPLATAFORMAVENDAPRODUTO";

    public String cdEmpresa;
    public String cdPlataformaVenda;
    public String cdPlataformaVendaProd;
    public String cdMarca;
    public String cdLinha;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlataformaVendaProduto) {
            PlataformaVendaProduto plataformavendaproduto = (PlataformaVendaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, plataformavendaproduto.cdEmpresa) && 
                ValueUtil.valueEquals(cdPlataformaVenda, plataformavendaproduto.cdPlataformaVenda) && 
                ValueUtil.valueEquals(cdPlataformaVendaProd, plataformavendaproduto.cdPlataformaVendaProd);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVenda);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVendaProd);
        return primaryKey.toString();
    }

}