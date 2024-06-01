package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Vector;

public class ItemCombo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPITEMCOMBO";
	public static final String TIPOITEMCOMBO_PRIMARIO = "P";
	public static final String TIPOITEMCOMBO_SECUNDARIO = "S";
	public static final int TIPO_ENCONTROU_OUTRA_COMBO = 2;
	public static final int TIPO_ENCONTROU_AVULSO = 1;
	public static final int TIPO_SEM_AVULSO = 0;

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCombo;
	public String cdTabelaPreco;
	public String cdProduto;
	public String flTipoItemCombo;
	public double vlUnitarioCombo;
	public int qtItemCombo;
	public String flAgrupadorSimilaridade;

	//Não Pesistente
	public String dsProduto;
	public Vector cdKitList;
	public String nuPedido;
	public String flOrigemPedido;
	public Combo combo;
	public String cdAgrupadorSimilaridade;
	public boolean buscandoSimilar;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemCombo) {
			ItemCombo itemCombo = (ItemCombo) obj;
			return ValueUtil.valueEquals(cdEmpresa, itemCombo.cdEmpresa)
				&& ValueUtil.valueEquals(cdRepresentante, itemCombo.cdRepresentante)
				&& ValueUtil.valueEquals(cdCombo, itemCombo.cdCombo)
				&& ValueUtil.valueEquals(cdProduto, itemCombo.cdProduto)
				&& ValueUtil.valueEquals(cdTabelaPreco, cdTabelaPreco);


		}
		return false;
	}

    public String getPrimaryKey() {
        return cdEmpresa + ";" + cdRepresentante + ";" + cdCombo + ";" + cdProduto + ";" + cdTabelaPreco;
    }
    
}