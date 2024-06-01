package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ItemComboAgrSimilar extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPITEMCOMBOAGRSIMILAR";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCombo;
	public String cdProduto;
	public String cdTabelaPreco;
	public String cdProdutoSimilar;
	public String flTipoItemCombo;

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemComboAgrSimilar) {
			ItemComboAgrSimilar itemComboAgrSimilar = (ItemComboAgrSimilar) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, itemComboAgrSimilar.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, itemComboAgrSimilar.cdRepresentante) &&
			ValueUtil.valueEquals(cdCombo, itemComboAgrSimilar.cdCombo) &&
			ValueUtil.valueEquals(cdProduto, itemComboAgrSimilar.cdProduto) &&
			ValueUtil.valueEquals(cdTabelaPreco, itemComboAgrSimilar.cdTabelaPreco) &&
			ValueUtil.valueEquals(cdProdutoSimilar, itemComboAgrSimilar.cdProdutoSimilar);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCombo);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
	    strBuffer.append(";");
	    strBuffer.append(cdTabelaPreco);
	    strBuffer.append(";");
	    strBuffer.append(cdProdutoSimilar);
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