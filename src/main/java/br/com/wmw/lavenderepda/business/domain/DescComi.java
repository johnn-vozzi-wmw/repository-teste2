package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class DescComi extends BaseDomain {

	public static String TABLE_NAME = "TBLVPDESCCOMI";

	public static String CDTIPODESCCOMI_PROD = "1";
	public static String CDTIPODESCCOMI_GRUPO = "2";

    public String cdEmpresa;
    public String cdDescComi;
    public String cdProduto;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String cdCliente;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String cdRamoAtividade;
    public String cdCidade;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public String cdTipoDesCcomi;

    public boolean filterCdGrupoProduto2Null;
    public boolean filterCdGrupoProduto3Null;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescComi) {
            DescComi descComi = (DescComi) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descComi.cdEmpresa) &&
                ValueUtil.valueEquals(cdDescComi, descComi.cdDescComi);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdDescComi);
        return primaryKey.toString();
    }

}