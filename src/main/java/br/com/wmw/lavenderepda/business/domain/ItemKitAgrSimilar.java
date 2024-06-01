package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ItemKitAgrSimilar extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPITEMKITAGRSIMILAR";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdKit;
	public String cdProduto;
	public String cdTabelaPreco;
	public String cdProdutoSimilar;
	public String flBonificado;

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemKitAgrSimilar) {
			ItemKitAgrSimilar itemKitAgrSimilar = (ItemKitAgrSimilar) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, itemKitAgrSimilar.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, itemKitAgrSimilar.cdRepresentante) &&
			ValueUtil.valueEquals(cdKit, itemKitAgrSimilar.cdKit) &&
			ValueUtil.valueEquals(cdProduto, itemKitAgrSimilar.cdProduto) &&
			ValueUtil.valueEquals(cdTabelaPreco, itemKitAgrSimilar.cdTabelaPreco) &&
			ValueUtil.valueEquals(cdProdutoSimilar, itemKitAgrSimilar.cdProdutoSimilar) &&
			ValueUtil.valueEquals(flBonificado, itemKitAgrSimilar.flBonificado);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdKit);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
	    strBuffer.append(";");
	    strBuffer.append(cdTabelaPreco);
	    strBuffer.append(";");
	    strBuffer.append(cdProdutoSimilar);
	    strBuffer.append(";");
	    strBuffer.append(flBonificado);
        return strBuffer.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		return cdProduto + "-" + cdProdutoSimilar;
	}

}