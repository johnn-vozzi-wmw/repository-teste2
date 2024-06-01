package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ProducaoProdRep extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPRODUCAOPRODREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public Date dtInicial;
    public Date dtFinal;
    public double qtdRateioProducao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProducaoProdRep) {
            ProducaoProdRep producaoProdRep = (ProducaoProdRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, producaoProdRep.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, producaoProdRep.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, producaoProdRep.cdProduto) && 
                ValueUtil.valueEquals(dtInicial, producaoProdRep.dtInicial) && 
                ValueUtil.valueEquals(dtFinal, producaoProdRep.dtFinal);
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
        primaryKey.append(DateUtil.formatDateDb(dtInicial));
        primaryKey.append(";");
        primaryKey.append(DateUtil.formatDateDb(dtFinal));
        return primaryKey.toString();
    }

}