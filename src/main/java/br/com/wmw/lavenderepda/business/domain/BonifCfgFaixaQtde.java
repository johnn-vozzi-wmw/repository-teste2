package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class BonifCfgFaixaQtde extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPBONIFCFGFAIXAQTDE";
	public static String NMCOLUNA_QTVENDIDA = "QTVENDIDA";

	public String cdEmpresa;
	public String cdBonifCfg;
	public String cdFaixaQtde;
	public int qtVendida;
	public int qtBonificada;
	public String flConcedeBrindes;
	public String flConcedeMultiplos;

	//nao persistente
	public double qtFaixaFilter;
	public boolean isPerdeuFaixaBrinde;
	private BonifCfg bonifCfg;
	public double qtBonificacaoAtual;
	public boolean deleted;

	public BonifCfgFaixaQtde() {}

	public BonifCfgFaixaQtde(String cdEmpresa, String cdBonifCfg) {
		this.cdEmpresa = cdEmpresa;
		this.cdBonifCfg = cdBonifCfg;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdBonifCfg);
		primaryKey.append(";");
		primaryKey.append(cdFaixaQtde);
		primaryKey.append(";");
		return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdFaixaQtde;
	}

	@Override
	public String getDsDomain() {
		return "[" + cdEmpresa + "]" + " - " + cdBonifCfg + " - " + cdFaixaQtde;
	}

	public boolean isConcedeMultiplos() {
		return ValueUtil.VALOR_SIM.equals(flConcedeMultiplos);
	}

	@Override
	public String toString() {
		return "BonifCfgFaixaQtde{" +
				"cdEmpresa='" + cdEmpresa + '\'' +
				", cdBonifCfg='" + cdBonifCfg + '\'' +
				", cdFaixaQtde='" + cdFaixaQtde + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BonifCfgFaixaQtde) {
			BonifCfgFaixaQtde bonifCfgFaixaQtde = (BonifCfgFaixaQtde) obj;
			return ValueUtil.valueEquals(this.cdEmpresa, bonifCfgFaixaQtde.cdEmpresa)
					&& ValueUtil.valueEquals(this.cdBonifCfg, bonifCfgFaixaQtde.cdBonifCfg)
					&& ValueUtil.valueEquals(this.cdFaixaQtde, bonifCfgFaixaQtde.cdFaixaQtde);
		}
		return false;
	}
	
	public boolean isConcedeBrindes() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flConcedeBrindes);
	}

	public BonifCfg getBonifCfg() {
		if (bonifCfg == null) {
			bonifCfg = new BonifCfg(this.cdEmpresa, this.cdBonifCfg);
		}
		return bonifCfg;
	}

	public void setBonifCfg(BonifCfg bonifCfg) {
		this.bonifCfg = bonifCfg;
	}
}
